package me.pajic.rearm.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "rearm")
@Config(name = "rearm", wrapperName = "ReArmConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
@SuppressWarnings("unused")
public class ReArmConfigModel {

    @Nest public Bow bow = new Bow();
    @Nest public Crossbow crossbow = new Crossbow();
    @Nest public Abilities abilities = new Abilities();
    @Nest public Protection protection = new Protection();
    @Nest public Tweaks tweaks = new Tweaks();

    public static class Bow {
        public boolean enablePerfectShot = true;
        @PredicateConstraint("greaterThanZero") public int perfectShotAdditionalDamage = 2;
        @PredicateConstraint("greaterThanZero") public float perfectShotTimeframe = 0.1F;
        @RestartRequired public boolean enableBackstep = true;
        @RestartRequired @PredicateConstraint("greaterThanZero") public int backstepTimeframe = 5;
        public boolean playerDrawingSounds = true;
        public boolean mobDrawingSounds = true;
        @RestartRequired public boolean acceptMultishot = true;
        @RestartRequired public boolean bowNetheriteVariant = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Crossbow {
        public boolean fixedArrowDamage = true;
        @PredicateConstraint("greaterThanZero") public int fixedArrowDamageAmount = 10;
        public boolean modifyFireworkDamage = true;
        @PredicateConstraint("greaterThanZero") public int baseFireworkDamage = 12;
        @PredicateConstraint("positive") public int damagePerFireworkStar = 3;
        public boolean modifyLoadSpeed = true;
        @PredicateConstraint("greaterThanZero") public float loadTime = 2F;
        @RestartRequired public boolean rejectMultishot = true;
        @RestartRequired public boolean acceptPower = true;
        @RestartRequired public boolean acceptInfinity = true;
        @RestartRequired public boolean crossbowNetheriteVariant = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean positive(int value) {
            return Predicates.positive(value);
        }
    }

    public static class Sword {
        public boolean enableLethalTempo = true;
        @PredicateConstraint("greaterThanZero") public int lethalTempoAdditionalDamagePerHit = 1;
        @PredicateConstraint("greaterThanZero") public float lethalTempoTimeframe = 0.1F;
        @PredicateConstraint("greaterThanZero") public int lethalTempoMaxStacks = 1;
        public boolean disableCriticalHits = true;
        @RestartRequired public boolean rejectKnockback = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Axe {
        public boolean acceptKnockback = true;
    }

    public static class Abilities {
        @PredicateConstraint("greaterThanZero") public int abilityCooldown = 160;
        @RestartRequired public boolean multishotAbility = true;
        @RestartRequired public boolean piercingShotAbility = true;
        @RestartRequired public boolean sweepingEdgeAbility = true;
        @RestartRequired public boolean cripplingBlowAbility = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Protection {
        @RestartRequired public boolean meleeProtection = true;
        @RestartRequired public boolean elementalProtection = true;
        @RestartRequired public boolean magicProtection = true;
        public boolean allowMultipleProtectionEnchantments = true;
        @PredicateConstraint("greaterThanZero") public int maxProtectionEnchantments = 2;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Tweaks {
        public boolean infinityFix = true;
        @RestartRequired public boolean infinimending = true;
        @RestartRequired public boolean craftTippedArrowsWithRegularPotions = true;
    }

    public static class Predicates {

        public static boolean greaterThanZero(int value) {
            return value > 0;
        }
        public static boolean greaterThanZero(float value) {
            return value > 0;
        }
        public static boolean positive(int value) {
            return value >= 0;
        }
    }
}
