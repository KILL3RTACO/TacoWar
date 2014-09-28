#TacoWar
A Bukkit Plugin designed to create a highly-customizable PvP experiences.

#Inspiration
Inspiration comes from both my own nature and a different plugin. This plugin is reverse-engineered from DeityWar, a PvP plugin which can be played at `mc.imdeity.com`, typing `/war`, and waiting for the next game to start (around 40 minutes or so). From there, I decided to add features such as `GameTypes`, `Teleporters`, and an abundance of other features.

#Features
There are a ton of features in this plugin. As they are perfected, I add `TechDemos` to [this playlist](https://www.youtube.com/playlist?list=PLt99lfdmnWJp8BnP7tIjRlOySbwKd5Kmg)

##Teleporters
Teleporters allow Players to get from one side of the map to another. The Teleporter system is based off the way they work in Halo. Teleporters are assigned a channel, and can be either a `receiver` or `transmitter` or both. Teleporters that are `transmitters` send the player to any Teleporter that is a `receiver` on the same channel.

When Players are teleported will keep their looking direction when teleporting (i.e. if a Player walks backwards into a Teleporter they come out of the Teleporter backwards).

[TacoWar TechDemo - Teleporters](https://www.youtube.com/watch?v=N2FNLJtPufo)

##Death Messages
In most multiplayer games, the kill feed displays what weapon or what type of weapon the player used, usually in the format of:

`{PLAYER} {WEAPON} {PLAYER_KILLED}`

TacoWar does this as well, however in Minecraft there are various other natural phenomenon that can kill a player. For a suicide, the death message format is:

`{CAUSE} {PLAYER_KILLED}`

 However, if a Player died via a suicide, but was shot previously by another Player, the format is:

`{CAUSE} {PLAYER_KILLED} -> {AWARDED_PLAYER}`

[TacoWar TechDemo - Death Messages](https://www.youtube.com/watch?v=UwgkVjaZt4M)

##Maps
As long as you have a map built, maps are easy to create. Areas like `Spawnpoints`, `Hills`, and `Teleporters` can be easily added with commands.

##GameTypes
`GameTypes` are also based of the way the work in Halo. The three default GameTypes so far are:

- FFA - Free for All
- KOTH - King of the Hill
- TDM - Team Deathmatch

For `KOTH`, Hills are added to map via commands. Options for GameTypes include the time limit, the GameType's name and whether teams are enabled (In FFA, this option is forced to `true`)

###Free for All
In `Free for All`, everyone is on their own team, and Players have no armor color.

###King of the Hill
In `King of the Hill`, Players or Teams fight for control of Hills. Hills are normally placed around the Map. Depending on the Options, the method of scoring points will be different. The three methods of scoring (`ScorePolicy`) are:

- PLAYER
- TEAM
- TOTAL_PLAYERS

####Player
If this ScorePolicy is set, the team is awarded points equal to the number of Players in the Hill, every second, as long as its uncontested.

####Team (Default)
If this ScorePolicy is set, the team is awarded one point every second as long as they are in control of the Hill

####Total Players
If this ScorePolicy is set, being in control simply means having more players in the Hill than any other Team. The amount of points awarded every second is the amount of Player in the controlling Team minus the Players on every other Team. Note that this has no effect if Teams are disabled.

#####Example
Red Team has 6 players in the hill. Green Team has 2 and Blue Team has 1. Therefore:

`redTeam - (greenTeam + blueTeam)`
`6 - (2 + 1) = 3`

The amount of points awarded to Red Team every second (assuming no Players die or leave the Hill) is 3.

###Team Deathmatch
In Team Deathmatch, setting the `TEAMS_ENABLED` option will not have an effect, as this option is forced to `true`. The objective is simple: kill everyone that is not on your team.

##Weapons
`Weapons` are custom items in the `Game` that are designed, like the Game itself, to be very customizable. Weapons fire different `Events` depending how they are used:

- `ON_CRIT` - When the holder does a critical hit on the other player.
- `ON_DEATH` - When the holder kills someone else with the Weapon (regardless of friendly fire).
- `ON_HIT` - When the holder hits another Player with the Weapon.
- `ON_PROJECTILE_HIT` - If the Weapon fired a projectile, like an arrow (see below), then this is when that projectile hits a block or a player.
- `ON_USE` When the holder hits a block or right clicks

There are different `Actions` that the weapon can do on each of these Events:

- `ARROW` - Fire an arrow
- `DAMAGE` - Add additional damage to the player hit (works only for `ON_HIT`)
- `EGG` - Throw an egg
- `EXPLODE` - Cause an explosion where the holder is looking, or where the projectile hit
- `FIREBALL` - Fire a fireball
- `IGNITE` - Ignite the player hit (works only for `ON_HIT`)
- `LIGHTNING` - Cause a lightning strike where the holder is looking
- `LIGHTNING_EFFECT` - Same as `LIGHTNING` but doesn't damage Players
- `SNOWBALL` - Throw a snowball

##Kits
`Kits` contain Weapons as well as Food. The items are given to the a Player whenever they respawn.

##Playlists
`Playlists` contain a list Maps, GameTypes, and Kits. This is yet another system based off Halo.

Playlists are used to determine what Maps should be played, what GameType should be played there, and what Kit to use (GameTypes can force a Kit, regardless if it is included in the Playlist)

##Config
`Maps`, `GameTypes`, `Weapons` and `Kits` use their own config file, so they can also be edited manually.

#Trivia 
Because I spend most of my time on the internet, there are many references included in the game. These can be easily found in Weapons/Kits and Maps that are available/included with the plugin. Wiki pages will explain these references in their `Trivia` section.