![rearm_banner](https://cdn.modrinth.com/data/cached_images/c34b8d3e8eb1e0430eebb88e38e00ca0a35e5c97.png)

**ReArm (Remunition and Armament)** is a mod that overhauls combat in Minecraft unintrusively by adding new combat mechanics and rebalancing weapons, armor and enchantments. Special attention was put into making the mod feel like it belongs in the game, and not outlandish.

The mod is **fully configurable**, from disabling entire features down to editing every single damage number, cooldown and timeframe. Obviously, the mod comes configured out of the box with values which I consider to be ideal, but everyone has their preferences.

### Bow

The bow is meant for **high mobility** and **skill dependent combat**. With a fully enchanted bow, you'll be moving around packs of enemies with ease while devastating them with well-timed shots.

- New **Perfect Shot** mechanic - Releasing the bow in a short window after fully charging it makes the fired arrow deal additional critical damage. To indicate that the shot was a Perfect Shot, the arrow will leave trails behind it, and the firing sound will have a higher pitch. In turn, vanilla bow crits are removed. On default settings, this makes bows deal significantly less damage compared to vanilla if you're not making use of perfect shots.
- New **Backstep** enchantment - Pressing the action key (default: Left Alt) in a short window after hitting an enemy with an arrow makes the player dash backwards a short distance based on the level of the enchantment. Backstep can be obtained by enchanting bows in the enchanting table with a rarity similar to the Flame enchantment or in looted enchanted books.
- **Bow drawing sounds** - Drawing the bow now plays a sound, similar to the sound played when loading a crossbow. This applies to both players and mobs, so you can now hear skeletons and other players drawing their bow, indicating that you're about to get shot at.
- Improved **Multishot** enchantment - Multishot now works on bows only, and has 3 levels which add one arrow for each level, up to 5 arrows.
- **Netherite Bow** - Bows can now be upgraded to a netherite bow in the same way you upgrade tools and armor to netherite, which gives it fire resistance, significantly increased durability, and a new look.

### Crossbow

The crossbow is meant for **low mobility**, but **high and consistent damage output**. With a fully enchanted crossbow, you'll be taking down enemies in one hit by ignoring their defenses, and obliterating hordes of enemies with fireworks. 

- Improved **Piercing** enchantment - Piercing now also ignores a percentage of the enemies armor based on level, however, piercing arrows won't go through armored enemies.
- **Consistent damage** - Arrows shot from crossbows will now deal a consistent amount of damage, instead of picking a random number from a range like in vanilla. On default settings, this is an overall buff to damage dealt, and makes it easier to adapt to the damage.
- **Increased firework damage** - Firework explosion damage has been increased significantly to make them viable for use in combat with crossbows. On default settings, a firework with one firework star will already deal slightly more damage than an arrow fired from the crossbow, and a maxed out firework with seven firework stars will deal more damage than even an arrow fired from the crossbow enchanted with Power 5.
- **Increased crossbow charge time** - On default settings, loading the crossbow takes noticeably longer, in order to counterbalance the general power increase of crossbows. The Quick Charge enchantment will still decrease the loading time.
- Support **Power** and **Infinity** - Crossbows can now be enchanted with Power and Infinity.
- **Netherite Crossbow** - Crossbows can now be upgraded to a netherite crossbow in the same way you upgrade tools and armor to netherite, which gives it fire resistance, significantly increased durability, and a new look.
- Removed Multishot support - Crossbows can no longer be enchanted with Multishot (it is now a bow enchantment).

### Sword

The sword is meant for **versatile combat** and **shield synergy**. With a fully enchanted sword, you'll be easily controlling crowds of enemies and keeping them at bay, while countering their attacks with empowered critical strikes.

- New **Critical Counter** mechanic - Sword attacks made shortly after blocking an attack with a shield will critically strike for increased damage. In turn, vanilla sword critical strikes are removed.
- Improved **Sweeping Edge** enchantment - Sweeping Edge now deals increased damage and has increased range based on level and the amount of enemies hit by the attack.
- Removed Knockback support - Swords can no longer be enchanted with Knockback (it's now an axe enchantment).

### Axe

The axe is meant for **very high single target damage**. With a fully enchanted axe, you'll be able to decimate your enemy, both in close combat and at a distance.

- New **Crippling Throw** enchantment - Allows throwing the axe at an enemy, much like a trident. The axe sticks onto the enemy and slows them down. Pressing the action key recalls the axe to you and the enemy starts taking bleeding damage. Slow intensity and bleeding damage scales with enchantment level (max 3).
- Support **Looting** and **Knockback** - Axes can now be enchanted with Looting and Knockback.

### Shield

- New **Parry** mechanic - Raising the shield to block a projectile shortly before it lands parries it and sends it flying towards the shooter.
- New **Bash** enchantment - Allows performing a shield bash by pressing the action key while blocking with a shield, knocking enemies away and dealing minor damage. The intensity of the knockback and damage dealt scales with enchantment level (max 3). The shield takes durability damage based on how many targets were hit, and is put on an 8-second cooldown.
- **Netherite Shield** - Shields can now be upgraded to a netherite shield in the same way you upgrade tools and armor to netherite, which gives it fire resistance, significantly increased durability, and a new look.

### Armor rebalance

ReArm rebalances all armor in the game in order to make armor progression smoother, increase the importance of armor toughness, and spread armor toughness and knockback resistance across all armor tiers.

- Rebalanced defense values for all armor in the game. Defense now increases linearly across armor tiers, from leather to netherite:
  - Leather: 8
  - Copper/Gold: 16
  - Iron/Chainmail: 24
  - Diamond: 32
  - Netherite: 40
- Toughness now has a greater effect on damage reduction and increases the minimum possible damage reduction.
- All armor pieces now start at 0 toughness. Toughness is now obtained by enchanting armor with protection enchantments, with each type of protection enchantment granting different amounts of toughness:
  - Melee and Projectile Protection: 0.3
  - Elemental Protection: 0.1
  - Blast and Magic Protection: 0.8
- All armor pieces now grant knockback resistance based on the total defense their armor set grants. This means all armor tiers have some knockback resistance now instead of only netherite.

Every part of the armor rebalance is fully adjustable and toggleable in the mod configuration. 

Armor from other mods will be automatically modified to suit the armor rebalance, however, max defense for each armor material can be explicitly set in the mod configuration if desired.

The toughness bonus for protection enchantments from other mods can be set in the mod configuration.

### Protection changes

ReArm rebalances most protection enchantments and introduces a new protection enchantment in order to **make the choice of protection enchantments actually matter** compared to just going the usual route of Protection 4 on all armor pieces.

- **_Armor can now have up to two different Protection enchantments_**.
- **Melee Protection** - The regular Protection enchantment has been replaced with Melee Protection, which provides moderate damage resistance to most close up physical damage sources, meaning it _no longer protects against any other damage type_. The damage reduction of Melee Protection has been increased to match other protection enchantments.
- **Elemental Protection** - The Fire Protection enchantment has been replaced with Elemental Protection, which provides high resistance to fire, lightning and freeze damage and reduced burn time if you're set ablaze.
- **Magic Protection** - New Protection enchantment which provides high resistance to magic attacks.
- Normalized enchantment costs for every Protection enchantment. In vanilla, chances for each Protection enchantment to appear in the enchanting table vary wildly, with the most common ones being Protection and Projectile Protection, and the rest being quite rare. This reduces the huge gap in chances between Protection enchantments.

### Tweaks

- **Improved sneaking** - Increases the effectiveness of mob detection range reduction when sneaking. Enabled and set to 80% by default.
- **Infinity fix** - Makes bows and crossbows enchanted with Infinity not require having an arrow in the inventory. Enabled by default.
- **Craft tipped arrows with regular potions** - Changes the tipped arrow recipe to use regular potions instead of lingering ones. Enabled by default.
- **Compatible Infinity and Mending** - Allows Infinity and Mending to be used on the same bow or crossbow. Disabled by default.

### Recommended mods

- **[Better Tridents](https://modrinth.com/mod/better-tridents)**: Turns the Trident into an actually useful situational weapon.
- **[Potion Cauldron](https://modrinth.com/mod/potion-cauldron)**: Provides a very neat way of crafting tipped arrows by submerging them in a Cauldron filled with potion contents, which is an upgrade from the crafting tweak I'm providing.
- **[Item Descriptions](https://modrinth.com/mod/item-descriptions)**: ReArm has custom enchantment description text that reflects the changes made to enchantments.