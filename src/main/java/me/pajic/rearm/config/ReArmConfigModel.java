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
    @Nest public Other other = new Other();

    public static class Bow {
        public boolean enablePerfectShot = true;
        @PredicateConstraint("positive") public int perfectShotAdditionalDamage = 2;
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
        public static boolean positive(int value) {
            return Predicates.positive(value);
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

    public static class Abilities {
        public boolean multishotAbility = true;
        public boolean piercingShotAbility = true;
        @PredicateConstraint("greaterThanZero") public int abilityCooldown = 160;

        public static boolean greaterThanZero(int value) {
            return Predicates.greaterThanZero(value);
        }
    }

    public static class Other {
        @RestartRequired public boolean craftTippedArrowsWithRegularPotions = true;
        public boolean infinityFix = true;
        @RestartRequired public boolean infinimending = true;
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
