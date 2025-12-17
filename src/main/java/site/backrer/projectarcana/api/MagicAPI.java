package site.backrer.projectarcana.api;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import site.backrer.projectarcana.capability.ClientMagicStatsData;
import site.backrer.projectarcana.capability.MagicStatsProvider;
import site.backrer.projectarcana.registry.MagicAttributes;

public class MagicAPI {

    /**
     * 获取当前魔力值 (Get Current Mana)
     * 自动处理客户端/服务端逻辑。
     * On Client (Player): Returns synced value from ClientMagicStatsData.
     * On Server: Returns value from Capability.
     */
    public static float getCurrentMana(LivingEntity entity) {
        if (entity.level().isClientSide) {
            if (entity instanceof Player && entity.getUUID().equals(Minecraft.getInstance().player.getUUID())) {
                return ClientMagicStatsData.getMana();
            }
            // TODO: Handle other entities on client if we sync them later
            return 0;
        } else {
            return entity.getCapability(MagicStatsProvider.MAGIC_STATS).map(stats -> stats.getMana()).orElse(0f);
        }
    }

    /**
     * 获取最大魔力值 (Get Max Mana)
     * 基于属性系统。
     */
    public static float getMaxMana(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.MAX_MANA.get());
    }

    /**
     * 获取当前硬直值 (Get Current Stagger)
     */
    public static float getCurrentStagger(LivingEntity entity) {
        if (entity.level().isClientSide) {
            if (entity instanceof Player && entity.getUUID().equals(Minecraft.getInstance().player.getUUID())) {
                return ClientMagicStatsData.getStagger();
            }
            return 0;
        } else {
            return entity.getCapability(MagicStatsProvider.MAGIC_STATS).map(stats -> stats.getStagger()).orElse(0f);
        }
    }

    /**
     * 获取最大硬直值 (Get Max Stagger)
     * 基于属性系统。
     */
    public static float getMaxStagger(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.MAX_STAGGER.get());
    }

    /**
     * 获取魔力恢复速度 (Get Mana Regen)
     */
    public static float getManaRegen(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.MANA_REGEN.get());
    }

    /**
     * 获取法术强度 (Get Spell Power)
     */
    public static float getSpellPower(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.SPELL_POWER.get());
    }

    /**
     * 获取冷却缩减 (Get Cooldown Reduction)
     */
    public static float getCooldownReduction(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.COOLDOWN_REDUCTION.get());
    }

    public static float getMetalDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_METAL_DAMAGE.get());
    }

    public static float getWoodDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_WOOD_DAMAGE.get());
    }

    public static float getWaterDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_WATER_DAMAGE.get());
    }

    public static float getFireDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_FIRE_DAMAGE.get());
    }

    public static float getLightDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_LIGHT_DAMAGE.get());
    }

    public static float getDarkDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_DARK_DAMAGE.get());
    }

    public static float getWindDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_WIND_DAMAGE.get());
    }

    public static float getIceDamage(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.ELEMENT_ICE_DAMAGE.get());
    }

    // Defense
    public static float getMagicResilience(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.MAGIC_RESILIENCE.get());
    }

    public static float getMagicResistance(LivingEntity entity) {
        return (float) entity.getAttributeValue(MagicAttributes.MAGIC_RESISTANCE.get());
    }
}
