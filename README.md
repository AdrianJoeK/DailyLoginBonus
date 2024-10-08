# DailyLoginBonus

[![License](https://img.shields.io/badge/License-AGPL-blue.svg)](LICENSE)

DailyLoginBonus is a customizable daily login bonus plugin for the PaperMC server software. It allows you to set a custom daily login bonus amount, and the percentage it should increase with the players daily streak.

## Features

- Customizable daily login bonus.
- Customizable reward cooldown (default 12 hours).
- Customizable streak reset time (default 24 hours).
- Customizable daily streak percentage increase (default 20%).

## Installation

1. Download the latest release of DailyLoginBonus from the [Releases](https://github.com/AdrianJoeK/DailyLoginBonus/releases) page.
2. Place the downloaded JAR file in the `plugins` directory of your Paper 1.21 server.
3. Start your Paper server to generate the default configuration file.

## Configuration

After running the server for the first time with DailyLoginBonus installed, a default configuration file will be created in the `plugins/dailyloginbonus` directory.

### Configuration File

The configuration file `config.yml` allows you to customize the plugin. Here is the default configuration:

```hocon
baseReward: 5000
percentageIncrease: 20
cooldownHours: 12
streakResetHours: 24
```

## Development
### Prerequisites
* Java 21
* Maven
### Building
Clone the repository and build the plugin using Maven:

```sh
git clone https://github.com/AdrianJoeK/DailyLoginBonus.git
cd DailyLoginBonus
mvn package
```

The compiled JAR file will be located in the target directory.

## License
DailyLoginBonus is licensed under the AGPL-3.0 License. See the [LICENSE](https://github.com/AdrianJoeK/DailyLoginBonus/blob/master/LICENSE) file for more details.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request on GitHub.

## Acknowledgements
* [PaperMC](https://papermc.io/software/paper) - The Minecraft server software that DailyLoginBonus is built for.
* [Vault](https://github.com/milkbowl/Vault) - The economy system used by DailyLoginBonus.
