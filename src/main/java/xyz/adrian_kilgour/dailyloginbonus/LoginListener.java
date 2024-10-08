package xyz.adrian_kilgour.dailyloginbonus;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The LoginListener class listens for player login events and handles daily login bonuses.
 * It tracks player login streaks and awards in-game currency based on the streak length.
 */
public class LoginListener implements Listener {
    private final DailyLoginBonus plugin;

    /**
     * Constructor to initialize the LoginListender with a reference to the main plugin class.
     * @param plugin the instance of the DailyLoginBonus plugin
     */
    public LoginListener(DailyLoginBonus plugin) {
        this.plugin = plugin;
    }

    /**
     * Event handler that is triggered when a player joins the server. it calculates the player's login streak,
     * awards a daily bonus, and sends messages to the player indicating their streak and reward.
     * @param event the PlayerJoinEvent triggered when a player joins the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        UUID playerUUID = player.getUniqueId();

        // Retrive configurable values for cooldown and streak reset.
        int cooldownHours = config.getInt("cooldownHours", 12);
        int streakResetHours = config.getInt("streakResetHours", 24);

        // Retreive the last login time and streak for the player.
        long lastLogin = config.getLong("players." + playerUUID + ".lastLogin", 0);
        int streak = config.getInt("players." + playerUUID + ".streak", 0);
        long currentTime = System.currentTimeMillis();

        // Convert cooldown and reset times to milliseconds.
        long cooldownInMillis = TimeUnit.HOURS.toMillis(cooldownHours);
        long streakResetInMillis = TimeUnit.HOURS.toMillis(streakResetHours);
        long timeSinceLastLogin = currentTime - lastLogin;

        // Check if the cooldown period has passed.
        if (timeSinceLastLogin < cooldownInMillis) {
            // Calculate remaining time before the player can claim the next bonus.
            long remainingTime = cooldownInMillis - timeSinceLastLogin;
            long hoursRemaining = TimeUnit.MILLISECONDS.toHours(remainingTime);
            long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % 60;

            // Notify the player that they need to wait before claiming the bonus again.
            player.sendMessage("You need to wait " + hoursRemaining + " hour(s) and " + minutesRemaining + " minute(s) before you can claim your next daily bonus.");
            return; // Stop because they cannot claim the bonus yet.
        }

        // Reset the streak if more than 24 hours have passed since the last login
        if (timeSinceLastLogin > streakResetInMillis) {
            streak = 0;
        }

        // Increment the streak as the player has logged in today
        streak++;
        config.set("players." + playerUUID + ".streak", streak);
        config.set("players." + playerUUID + ".lastLogin", currentTime);
        plugin.saveConfig();

        // Calculate the reward based on the streak and deposit it into the players account.
        int reward = getDailyReward(streak);
        DailyLoginBonus.getEconomy().depositPlayer(player, reward);

        // Notify the player about their login streak and reward.
        player.sendMessage("You have logged in for " + streak + " day(s) in a row!");
        player.sendMessage("You've been awarded " + reward + " currency!");
    }

    /**
     * Calculates the daily reward based on the players current login streak and percentage increase.
     * @param streak the current login streak
     * @return the calculated reward based on the streak and percentage increase
     */
    private int getDailyReward(int streak) {
        int baseReward = plugin.getConfig().getInt("baseReward", 5000);
        int percentageIncrease = plugin.getConfig().getInt("percentageIncrease", 20);

        // Calculate the reward using the base reward and applying the percentage increase based on the streak.
        double multiplier = 1 + ((streak - 1) * (percentageIncrease / 100.0));
        return (int) (baseReward * multiplier);
    }
}
