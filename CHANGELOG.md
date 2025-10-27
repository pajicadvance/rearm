2.4.1: Fixed mixin crash on launch.

2.4.0:
## Armor rebalance

- Rebalanced defense values for all armor in the game
    - Max defense is now 40 up from 20
        - This does not mean 2x less damage taken. 40 defense still has the same efficiency as 20 defense in vanilla, it's only increased for easier armor scaling
    - Defense on vanilla armor sets now scales linearly:
        - Leather: 8
        - Copper/Gold: 16
        - Iron/Chainmail: 24
        - Diamond: 32
        - Netherite: 40
    - Armor from other mods will be automatically rebalanced, no need for explicit configuration or compatibility
    - Max defense for each armor material can be explicitly set in the mod configuration if desired
- Toughness now has a greater effect on damage reduction
    - Toughness now increases the minimum damage reduction
    - Armor without any toughness will be slightly weaker than vanilla
    - Armor with a few points of toughness is already stronger than vanilla and becomes much stronger at high toughness
- Changed how toughness is obtained on armor
    - All armor pieces now have 0 toughness
    - Toughness is now obtained by enchanting armor with protection enchantments
    - Each level of protection enchantment grants some toughness:
        - Melee and Projectile Protection: 0.3
        - Elemental Protection: 0.1
        - Blast and Magic Protection: 0.8
    - Compatibility for protection enchantments from other mods can be added using the mod configuration
- Changed how knockback resistance is applied to armor
    - All armor pieces now grant knockback resistance based on the total defense their armor set grants
    - At max defense, each piece gets 10% (matches vanilla netherite values)
- Every part of the armor rebalance is fully adjustable and toggleable in the mod configuration

## Improved sneaking

- Sneaking is now much more effective at reducing the range at which mobs can detect players
    - Range reduction when sneaking increased to 80% up from 20%
    - Can be adjusted in the mod configuration

### Changes

- [NeoForge] Updated to Minecraft 1.21.10
- [Fabric] Fixed thrown axe being invisible in 1.21.10
- Increased the damage reduction of Melee Protection to match other protection enchantments
- Knockback reduction in item tooltips now displays as a percentage instead of a number (0.1 -> 10%)