# NOTICE: 2.0 supporting Fabric, Bungee, and Velocity natively without the Spigot server requirement on versions 1.12+ coming soon. Join the discord for the latest news. Spigot will still be supported.

# GeyserExtras

A plugin which attempts to unify features for Bedrock Edition players on Java Edition Servers with GeyserMC, aswell as other handy features for Bedrock Players.

[![generic](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/documentation/generic_vector.svg)](https://github.com/GeyserExtras/GeyserExtras/wiki)
[![discord-singular](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-singular_vector.svg)](https://discord.gg/2FfuShKQgy)

[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/modrinth_vector.svg)](https://modrinth.com/plugin/geyserextras) 
[![hangar](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/hangar_vector.svg)](https://hangar.papermc.io/GeyserExtras/GeyserExtras)
[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/available/github_vector.svg)](https://github.com/GeyserExtras/GeyserExtras/releases)

![spigot](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/spigot_vector.svg)
![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/paper_vector.svg)
![purpur](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/purpur_vector.svg)

![bungeecord](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/bungeecord_vector.svg)
![waterfall](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact-minimal/supported/waterfall_vector.svg)

![velocity](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/velocity_vector.svg)

## Feature Support Table
**Server** refers to a Spigot-Based server with GeyserExtras, Geyser and Floodgate installed.

**Proxy** refers to a Bungee/Velocity proxy with GeyserExtras, Geyser and Floodgate installed, with the backend server being a Spigot-Based server with GeyserExtras and the `proxy-mode` option set to `true` with only Floodgate installed. 

|Feature                     |Server|Proxy|
|---------------------------|------|-----|
|Java Cooldown              |🟢    |🟢   |
|Java Combat Sounds         |🟢    |🟢   |
|Block Ghosting Fix         |🟢    |🟢   |
|Speed Bridge Fix           |🟢    |🟢   |
|Quick-Menu                 |🟢    |🟢   |
|Resource Pack Loading      |🟢    |🔴   |
|Bedrock Emote Chat         |🟢    |🟢   |
|Auto-Reconnect             |🟢    |🟢   |
|Nether Roof Fix Fog Fix    |🟢    |🟢   |
|Geyser Commands in /ge menu|🟢    |🔴   |

## Features

### Java Edition Combat Improvements
- Cooldown Indicator![An video showing the two types of Attack Indicators, attack and hot-bar, in the GeyserExtras plugin.](https://github.com/GeyserExtras/GeyserExtras/blob/master/preview/indicator.gif?raw=true)
- Combat Sounds, sweep attack, crit, strong, knockback.
  
### Bedrock Emote Chat for Java Edition players.
Java players can toggle this by typing `/emotechat`.

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

## Permissions
```yaml
permissions:
  geyserextras.menu:
    default: true
    description: Allow players to execute the /geyserextras command
  geyserextras.platformlist:
    default: true
    description: Allow players to execute the /platformlist command
  geyserextras.playerlist:
    default: true
    description: Allow players to execute the /playerlist command
  geyserextras.emotechat:
    default: true
    description: Allow players to execute the /emotechat command
```
## Plans
[Trello](https://trello.com/b/9UHPTQST)
  
## Bugs
- Sweep attack sometimes plays even if the player didn't sweep attack.
