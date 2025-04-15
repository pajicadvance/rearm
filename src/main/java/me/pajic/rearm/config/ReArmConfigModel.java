package me.pajic.rearm.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "rearm")
@Config(name = "rearm", wrapperName = "ReArmConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
@SuppressWarnings("unused")
public class ReArmConfigModel {
    @SectionHeader("weapons")
    @Nest public Bow bow = new Bow();
    @Nest public Crossbow crossbow = new Crossbow();
    @Nest public Sword sword = new Sword();
    @Nest public Axe axe = new Axe();

    @SectionHeader("protection")
    @RestartRequired public boolean meleeProtection = true;
    @RestartRequired public boolean elementalProtection = true;
    @RestartRequired public boolean magicProtection = true;
    public boolean allowMultipleProtectionEnchantments = true;
    @PredicateConstraint("greaterThanZero") public int maxProtectionEnchantments = 2;

    @SectionHeader("tweaks")
    public boolean infinityFix = true;
    @RestartRequired public boolean infinimending = false;
    @RestartRequired public boolean craftTippedArrowsWithRegularPotions = true;

    public static class Bow {
        public boolean enablePerfectShot = true;
        @PredicateConstraint("greaterThanZero") public int perfectShotAdditionalDamage = 2;
        @PredicateConstraint("greaterThanZero") public float perfectShotTimeframe = 0.2F;
        @RestartRequired public boolean enableBackstep = true;
        @RestartRequired @PredicateConstraint("greaterThanZero") public int backstepTimeframe = 5;
        @RestartRequired public boolean improvedMultishot = true;
        @RestartRequired @PredicateConstraint("greaterThanZero") public int maxMultishotLevel = 3;
        @RestartRequired @PredicateConstraint("greaterThanZero") public int additionalArrowsPerLevel = 1;
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
        @RestartRequired public boolean improvedPiercing = true;
        @RestartRequired @PredicateConstraint("greaterThanZero") public int percentArmorIgnoredPerLevel = 15;
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
        public boolean enableCriticalCounter = true;
        @PredicateConstraint("greaterThanZero") public int criticalCounterTimeframe = 10;
        @RestartRequired public boolean improvedSweepingEdge = true;
        @PredicateConstraint("greaterThanZero") public float sweepingEdgeAdditionalDamagePerMob = 1.0F;
        @PredicateConstraint("greaterThanZero") public int maxMobAmountUsedForDamageIncrease = 10;
        @RestartRequired public boolean rejectKnockback = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Axe {
        @RestartRequired public boolean cripplingThrow = true;
        @PredicateConstraint("greaterThanZero") public int cripplingThrowBleedingDuration = 120;
        @PredicateConstraint("greaterThanZero") public float cripplingThrowBaseBleedingDPS = 1.0F;
        @PredicateConstraint("greaterThanZero") public float cripplingThrowBleedingDPSIncreasePerLevel = 0.5F;
        @PredicateConstraint("greaterThanZero") public int cripplingThrowBaseSlownessAmplifier = 1;
        @PredicateConstraint("greaterThanZero") public int cripplingThrowSlownessAmplifierIncreasePerLevel = 1;
        public boolean acceptKnockback = true;
        public boolean acceptLooting = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static boolean greaterThanZero(int value) {
        return Predicates.greaterThanZero(value);
    }

    public static class Predicates {
        public static boolean greaterThanZero(int value) {
            return value > 0;
        }
        public static boolean greaterThanZero(float value) {
            return value > 0;
        }
        public static boolean greaterThanZero(double value) {
            return value > 0;
        }
        public static boolean positive(int value) {
            return value >= 0;
        }
    }
}
