package me.pajic.rearm.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.rearm.Main;

@Version(version = 1)
public class ModConfig extends Config {
    public ModConfig() {
        super(Main.CONFIG_RL);
    }

    public Bow bow = new Bow();
    public Crossbow crossbow = new Crossbow();
    public Sword sword = new Sword();
    public Axe axe = new Axe();
    public Protection protection = new Protection();
    public Tweaks tweaks = new Tweaks();

    public static class Protection extends ConfigSection {
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean meleeProtection = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean elementalProtection = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean magicProtection = new ValidatedBoolean(true);
        public ValidatedBoolean allowMultipleProtectionEnchantments = new ValidatedBoolean(true);
        public ValidatedInt maxProtectionEnchantments = new ValidatedInt(2, Integer.MAX_VALUE, 1);
    }

    public static class Tweaks extends ConfigSection {
        public ValidatedBoolean infinityFix = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean infinimending = new ValidatedBoolean(false);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean craftTippedArrowsWithRegularPotions = new ValidatedBoolean(true);
    }

    public static class Bow extends ConfigSection {
        public ValidatedBoolean enablePerfectShot = new ValidatedBoolean(true);
        public ValidatedInt perfectShotAdditionalDamage = new ValidatedInt(2, Integer.MAX_VALUE, 1);
        public ValidatedFloat perfectShotTimeframe = new ValidatedFloat(0.2F);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean enableBackstep = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedInt backstepTimeframe = new ValidatedInt(5, Integer.MAX_VALUE, 1);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean improvedMultishot = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedInt maxMultishotLevel = new ValidatedInt(3, Integer.MAX_VALUE, 1);
        @RequiresAction(action = Action.RESTART) public ValidatedInt additionalArrowsPerLevel = new ValidatedInt(1, Integer.MAX_VALUE, 1);
        public ValidatedBoolean playerDrawingSounds = new ValidatedBoolean(true);
        public ValidatedBoolean mobDrawingSounds = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean acceptMultishot = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean bowNetheriteVariant = new ValidatedBoolean(true);
    }

    public static class Crossbow extends ConfigSection {
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean improvedPiercing = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedInt percentArmorIgnoredPerLevel = new ValidatedInt(15, Integer.MAX_VALUE, 1);
        public ValidatedBoolean stopPiercingOnArmoredEntity = new ValidatedBoolean(true);
        public ValidatedBoolean fixedArrowDamage = new ValidatedBoolean(true);
        public ValidatedInt fixedArrowDamageAmount = new ValidatedInt(10, Integer.MAX_VALUE, 1);
        public ValidatedBoolean modifyFireworkDamage = new ValidatedBoolean(true);
        public ValidatedInt baseFireworkDamage = new ValidatedInt(12, Integer.MAX_VALUE, 1);
        public ValidatedInt damagePerFireworkStar = new ValidatedInt(3, Integer.MAX_VALUE, 0);
        public ValidatedBoolean modifyLoadSpeed = new ValidatedBoolean(true);
        public ValidatedFloat loadTime = new ValidatedFloat(2F);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean rejectMultishot = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean acceptPower = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean acceptInfinity = new ValidatedBoolean(true);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean crossbowNetheriteVariant = new ValidatedBoolean(true);
    }

    public static class Sword extends ConfigSection {
        public ValidatedBoolean enableCriticalCounter = new ValidatedBoolean(true);
        public ValidatedInt criticalCounterTimeframe = new ValidatedInt(10, Integer.MAX_VALUE, 1);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean improvedSweepingEdge = new ValidatedBoolean(true);
        public ValidatedFloat sweepingEdgeAdditionalDamagePerMob = new ValidatedFloat(1.0F);
        public ValidatedInt maxMobAmountUsedForDamageIncrease = new ValidatedInt(10, Integer.MAX_VALUE, 1);
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean rejectKnockback = new ValidatedBoolean(true);
    }

    public static class Axe extends ConfigSection {
        @RequiresAction(action = Action.RESTART) public ValidatedBoolean cripplingThrow = new ValidatedBoolean(true);
        public ValidatedInt cripplingThrowBleedingDuration = new ValidatedInt(120, Integer.MAX_VALUE, 1);
        public ValidatedFloat cripplingThrowBaseBleedingDPS = new ValidatedFloat(1.0F);
        public ValidatedFloat cripplingThrowBleedingDPSIncreasePerLevel = new ValidatedFloat(0.5F);
        public ValidatedInt cripplingThrowBaseSlownessAmplifier = new  ValidatedInt(1, Integer.MAX_VALUE, 1);
        public ValidatedInt cripplingThrowSlownessAmplifierIncreasePerLevel = new ValidatedInt(1, Integer.MAX_VALUE, 1);
        public ValidatedInt maxTimeStuckInTarget = new ValidatedInt(240, Integer.MAX_VALUE, 1);
        public ValidatedBoolean enableCriticalCounter = new ValidatedBoolean(false);
        public ValidatedBoolean acceptKnockback = new ValidatedBoolean(true);
        public ValidatedBoolean acceptLooting = new ValidatedBoolean(true);
    }
}
