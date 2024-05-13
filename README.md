
# GeyserExtras

A plugin which attempts to unify features for Bedrock Edition players on Java Edition Servers with GeyserMC, aswell as other handy features for Bedrock Players.

[![Discord](https://img.shields.io/discord/715685803040309310?logo=discord&logoColor=ffffff&label=discord&labelColor=5865F2&link=https%3A%2F%2Fdiscord.gg%2F2FfuShKQgy)](https://discord.gg/2FfuShKQgy)
![Java Version Supported](https://img.shields.io/badge/java-1.20.6-6F4E37)
![Bedrock Version Supported](https://img.shields.io/badge/bedrock-1.20.81-333333)
![Supported server software](https://img.shields.io/badge/spigot-velocity-333333)

<a href="https://modrinth.com/plugin/geyserextras"><img src="https://media.beehiiv.com/cdn-cgi/image/fit=scale-down,format=auto,onerror=redirect,quality=80/uploads/publication/logo/a49f8e1b-3835-4ea1-a85b-118c6425ebc3/Modrinth_Dark_Logo.png" align="left" height="64"></a>
<a href="https://hangar.papermc.io/GeyserExtras/GeyserExtras"><img src="https://raw.githubusercontent.com/HangarMC/Hangar/9c658cef2638f4aa23ba7f6b1861057c3bb8f714/frontend/src/assets/hangar-logo.svg" align="left" height="64"></a>
<a href="https://github.com/GeyserExtras/GeyserExtras/releases"><img src="https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png" align="left" height="64"></a>

##
The plugin is in very early development. Contributions are welcomed!
##

### Java Edition Combat Improvements

- Cooldown Indicator![An video showing the two types of Attack Indicators, attack and hot-bar, in the GeyserExtras plugin.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/indicator.gif?raw=true)
- Combat Sounds, sweep attack, crit, strong, knockback.
### Quick-Menu
- Players can bind whatever emotes they want to whatever commands are in the plugins config.
- e.g, /geyser offhand, /geyser statistics, /customcommandhere, anything that can be ran by the player can be ran by the Quick-Menu.
  ![An video showing the GeyserExtras Quick-Menu Feature.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/quickmenu.gif?raw=true)
### Optional Pack Loading
- Players can load whatever packs are placed under `optionalpacks/` in the GeyserExtras Folder via the GeyserExtras Menu (`/ge`).
  ![An video showing the GeyserExtras Optional Packs Feature.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/resourcepacks.gif?raw=true)
### Skin Saving
- The plugin can automatically save player skins to the plugins folder under `skins/playerUUID/textureid.png`.
- Please note that this is only the skin that Floodgate converts for Java Players meaning that some marketplace skins like 128x128 skins or classic skins with weird geometry may not look 100% identical.

### Player List
- The player list from Java Edition (usually bound to Tab) has been recreated inside of a ServerForm. This can be accessed in the GeyserExtras Menu (`/ge`) or via `/playerlist` on Bedrock Edition.
  ![An image showing the Player List in GeyserExtras.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/playerlist.png?raw=true)

### Platform List
- A simple command which shows what platforms players are on.
- `/platformlist` | `/platforms`

![An image showing the Platform List in GeyserExtras.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/platformslist.png?raw=true)
## Fixes

### Crop Sound Fix

Crops now play the Java Edition sound for breaking and placing.

### Nether Roof Fix Fog Fix

Fixes the fog to not be the same red fog when Geyser has `above-bedrock-nether-building: true`.

![An video showing the Nether Roof Fix Fog Fix in GeyserExtras.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/netherrooffixfogfix.gif?raw=true)

### Arrow Delay Fix

Fixes the arrows to no longer be delayed when shooting, however this has the downside of making the arrows movement look very choppy. Players can configure this for themselves using the GeyserExtras Menu.

## Plans
https://trello.com/b/9UHPTQST
  
## Bugs
- Sweep attack sometimes plays even if the player didn't sweep attack.