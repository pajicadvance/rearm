## 2.5.10

- Fixed a crash on startup when installed alongside Very Many Players.
- Fixed startup crashes on multiple versions.
- Fixed a misleading warning about Elemental Protection not being applied showing up in the log on versions before 26.3.

## 2.5.9

- Added NeoForge 26.3 version.
- Fixed Bash dealing the same damage regardless of enchantment level.
- Fixed the Quickstep step height boost becoming permanent if the player logged out right after a Quickstep. Affected players are cleaned up the next time they log out.
- Critical Counter is now tracked by the server instead of the client, preventing clients from forcing critical hits.
- The server now validates Quickstep and axe recall requests instead of trusting the client.
- Fixed thrown axes floating in the air forever after reloading if the entity they were stuck in was gone.
- Fixed enchanting table having an unintentionally low chance to produce multiple Protection enchantments.
- Fixed fireworks dealing invalid damage when base damage is set to low values in the config.
- Fixed tridents not thrown by a player (for example from a dispenser) returning to the offhand after a reload.
- Fixed Bleeding status effect visually showing one level higher than intended.
- Piercing can no longer reduce armor below zero.
- The action key no longer sends recall and bash packets every tick while pressed.
- Backported the 26.4 fixes for [MC-297196](https://mojira.dev/MC-297196) and [MC-311974](https://mojira.dev/MC-311974) (26.3 only). Can be toggled with the Entity sync fix option in the Vanilla bug fixes config section, enabled by default.
- Fixed Melee Protection also reducing magic damage from mods whose magic damage doesn't fully bypass armor, which stacked it with Magic Protection.
- Fixed a crash when another mod changes or removes the damage reduction of Fire Protection. If a mod replaces Fire Protection's fire damage reduction with its own system, Elemental Protection now keeps protecting against the other elemental damage types.
- A few other smaller fixes.
