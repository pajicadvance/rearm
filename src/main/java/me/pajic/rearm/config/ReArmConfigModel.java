package me.pajic.rearm.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "rearm")
@Config(name = "rearm", wrapperName = "ReArmConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
@SuppressWarnings("unused")
public class ReArmConfigModel {

    @SectionHeader("abilities")
    @PredicateConstraint("greaterThanZero") public int abilityCooldown = 200;
    @Nest public Multishot multishot = new Multishot();
    @Nest public PiercingShot piercingShot = new PiercingShot();
    @Nest public SweepingEdge sweepingEdge = new SweepingEdge();
    @Nest public CripplingBlow cripplingBlow = new CripplingBlow();

    @SectionHeader("ranged")
    @Nest public Bow bow = new Bow();
    @Nest public Crossbow crossbow = new Crossbow();

    @SectionHeader("melee")
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
    @RestartRequired public boolean infinimending = true;
    @RestartRequired public boolean craftTippedArrowsWithRegularPotions = true;

    public static class Multishot {
        @RestartRequired public boolean multishotAbility = true;
        @RestartRequired @PredicateConstraint("greaterThanZero") public int multishotAdditionalArrows = 6;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class PiercingShot {
        @RestartRequired public boolean piercingShotAbility = true;
    }

    public static class SweepingEdge {
        @RestartRequired public boolean sweepingEdgeAbility = true;
        @PredicateConstraint("greaterThanZero") public float sweepingEdgeAdditionalDamagePerMob = 1.5F;
        @PredicateConstraint("greaterThanZero") public int maxMobAmountUsedForDamageIncrease = 6;

        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class CripplingBlow {
        @RestartRequired public boolean cripplingBlowAbility = true;
        @PredicateConstraint("greaterThanZero") public int cripplingBlowBleedingDuration = 120;
        @PredicateConstraint("greaterThanZero") public float cripplingBlowBaseBleedingDPS = 1.0F;
        @PredicateConstraint("greaterThanZero") public float cripplingBlowBleedingDPSIncreasePerLevel = 0.5F;
        @PredicateConstraint("greaterThanZero") public int cripplingBlowSlownessDuration = 20;
        @PredicateConstraint("greaterThanZero") public int cripplingBlowBaseSlownessAmplifier = 3;
        @PredicateConstraint("greaterThanZero") public int cripplingBlowSlownessAmplifierIncreasePerLevel = 1;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
        public static boolean greaterThanZero(float value) {
            return Predicates.greaterThanZero(value);
        }
    }

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
        public boolean enableCriticalCounter = true;
        @PredicateConstraint("greaterThanZero") public int criticalCounterTimeframe = 10;
        @RestartRequired public boolean rejectKnockback = true;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Axe {
        public boolean acceptKnockback = true;
        public boolean acceptLooting = true;
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
