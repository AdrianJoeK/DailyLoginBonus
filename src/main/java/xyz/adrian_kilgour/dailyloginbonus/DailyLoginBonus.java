package xyz.adrian_kilgour.dailyloginbonus;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

/**
 * The DailyLoginBonus class is the main class for the plugin, which handles daily login bonuses for players.
 * It integrates with the Vault economy plugin to reward players with in-game currency for maintaining login streaks.
 */
public final class DailyLoginBonus extends JavaPlugin {
    /**
     * Static reference to the Vault economy provider.
     */
    private static Economy economy = null;

    /**
     * Called when the plugin is enabled. Registers the event listener for player logins and attempts to
     * set up the Vault economy integration. If Vault is not available, the plugin is disabled.
     */
    @Override
    public void onEnable() {
        getLogger().info("DailyLoginBonus plugin has been enabled!");

        // Register event listeners for player logins.
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);

        // Attempt to set up the economy using Vault.
        if (!setupEconomy()) {
            getLogger().severe("Vault not found, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Check if config.yml exists and load it
        if (getResource("config.yml") == null) {
            getLogger().severe("config.yml is missing, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Save the default config file, if it doesn't exist.
        saveDefaultConfig();
    }

    /**
     * Called when the plugin is disabled. Outputs a message to the console.
     */
    @Override
    public void onDisable() {
        getLogger().info("DailyLoginBonus plugin has been disabled!");
    }

    /**
     * Attempts to set up the Vault economy integration. This check if the Vault plugin is present and if an economy povider is available.
     * @return true if Vault and economy provider are successfully set up, false otherwise.
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin in not installed on the server.");
            return false; // Vault is not installed.
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("No economy provider found. Ensure you have an economy plugin installed.");
            return false; // No economy provider found.
        }
        economy = rsp.getProvider(); // Set the economy provider.
        getLogger().info("Vault found and economy provider set.");
        return economy != null;
    }

    /**
     * Static method to retrive the economy instance provided by Vault.
     * @return the economy instance if available, null otherwise.
     */
    public static Economy getEconomy() {
        return economy;
    }
}
