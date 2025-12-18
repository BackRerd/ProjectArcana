# Project Arcana - 魔法系统开发指南

本文档作为 Project Arcana 中魔法系统实现的参考手册。

## 1. 魔法属性 (Magic Attributes)
我们使用 Forge 的 `DeferredRegister` 注册了 5 个自定义属性。
**位置:** `site.backrer.projectarcana.registry.MagicAttributes`

| 属性 ID (Attribute ID) | 字段名 (Field Name) | 描述 (Description) | 默认值 | 配置项 (Config Key) |
| :--- | :--- | :--- | :--- | :--- |
| `projectarcana:max_mana` | `MAX_MANA` | 最大魔力值池 | 100.0 | `max_mana_default` |
| `projectarcana:mana_regen` | `MANA_REGEN` | 每秒魔力恢复量 (大约) | 1.0 | `mana_regen_default` |
| `projectarcana:spell_power` | `SPELL_POWER` | 法术强度加成 | 0.0 | `spell_power_default` |
| `projectarcana:cooldown_reduction` | `COOLDOWN_REDUCTION` | 冷却缩减百分比 (0.0 - 1.0) | 0.0 | `cooldown_reduction_default` |
| `projectarcana:max_stagger` | `MAX_STAGGER` | 最大硬直阈值 | 100.0 | `max_stagger_default` |
| `projectarcana:element_metal_damage` | `ELEMENT_METAL_DAMAGE` | 金属性伤害加成 | 0.0 | `element_metal_damage_default` |
| `projectarcana:element_wood_damage` | `ELEMENT_WOOD_DAMAGE` | 木属性伤害加成 | 0.0 | `element_wood_damage_default` |
| `projectarcana:element_water_damage` | `ELEMENT_WATER_DAMAGE` | 水属性伤害加成 | 0.0 | `element_water_damage_default` |
| `projectarcana:element_fire_damage` | `ELEMENT_FIRE_DAMAGE` | 火属性伤害加成 | 0.0 | `element_fire_damage_default` |
| `projectarcana:element_light_damage` | `ELEMENT_LIGHT_DAMAGE` | 光属性伤害加成 | 0.0 | `element_light_damage_default` |
| `projectarcana:element_dark_damage` | `ELEMENT_DARK_DAMAGE` | 暗属性伤害加成 | 0.0 | `element_dark_damage_default` |
| `projectarcana:element_wind_damage` | `ELEMENT_WIND_DAMAGE` | 风属性伤害加成 | 0.0 | `element_wind_damage_default` |
| `projectarcana:element_ice_damage` | `ELEMENT_ICE_DAMAGE` | 冰属性伤害加成 | 0.0 | `element_ice_damage_default` |
| `projectarcana:magic_resilience` | `MAGIC_RESILIENCE` | 魔法韧性 (降低负面效果持续时间/硬直) | 0.0 | `magic_resilience_default` |
| `projectarcana:magic_resistance` | `MAGIC_RESISTANCE` | 魔法抗性 | 0.0 | `magic_resistance_default` |


**使用示例:**
```java
double maxMana = player.getAttributeValue(MagicAttributes.MAX_MANA.get());
```

---

## 2. 魔法能力 (Magic Capability)
用于追踪频繁变化的 **当前** 数值（魔力、硬直）。
**位置:** `site.backrer.projectarcana.capability`

*   **接口:** `IMagicStats`
*   **实现:** `MagicStats`
*   **提供者:** `MagicStatsProvider` (附加到 `Entity` -> `Player`)

**数据持久化:**
数据保存到 NBT (`saveNBTData`/`loadNBTData`) 并在死亡重生时保留 (`ModEvents.onPlayerCloned`)。

---

## 3. 公共 API (MagicAPI)
一个辅助类，用于简便地访问数值，无需关心 客户端/服务端 逻辑或原始属性操作。
**位置:** `site.backrer.projectarcana.api.MagicAPI`

**方法:**
*   `getCurrentMana(LivingEntity)`: 返回当前魔力 (客户端已同步，服务端为真实值)。
*   `getMaxMana(LivingEntity)`: 返回最大魔力属性值。
*   `getCurrentStagger(LivingEntity)`
*   `getMaxStagger(LivingEntity)`
*   `getManaRegen(LivingEntity)`
*   `getSpellPower(LivingEntity)`
*   `getCooldownReduction(LivingEntity)`
*   `getMetalDamage(LivingEntity)`
*   `getWoodDamage(LivingEntity)`
*   `getWaterDamage(LivingEntity)`
*   `getFireDamage(LivingEntity)`
*   `getLightDamage(LivingEntity)`
*   `getDarkDamage(LivingEntity)`
*   `getWindDamage(LivingEntity)`
*   `getIceDamage(LivingEntity)`

**示例:**
```java
float current = MagicAPI.getCurrentMana(player);
float max = MagicAPI.getMaxMana(player);
if (current >= 10) {
    // 施放法术
}
```

---

## 4. 网络与同步 (Networking & Synchronization)
**位置:** `site.backrer.projectarcana.networking`
*   **通道:** `projectarcana:messages`
*   **数据包:** `PacketSyncMagicStats`
    *   从 服务端 发送到 客户端。
    *   更新客户端侧的 `ClientMagicStatsData`。
*   **逻辑:**
    *   `ModEvents.onPlayerTick` (服务端): 每 tick 恢复魔力并发送同步数据包给玩家。

---

## 5. 配置文件 (Configuration via Data Pack)
属性的默认值、最小值和最大值现在通过 **Data Pack (数据包)** 进行配置，而不是通过 TOML 文件。

**文件位置:** 
默认配置位于 `src/main/resources/data/projectarcana/magic_config/global_attributes.json`。
在实际游戏中，你可以通过创建新的数据包并覆盖 `data/projectarcana/magic_config/global_attributes.json` 来修改这些值。

**JSON 结构:**
```json
{
  "default_attributes": [
    {
      "attribute": "projectarcana:max_mana",
      "value": 100.0,
      "min": 0.0,
      "max": 5000000.0
    },
    ...
  ]
}
```

*   `value`: 属性的基础默认值 (当玩家首次登录或重置时生效)。
*   `min`: 属性允许的最小值 (硬性限制)。
*   `max`: 属性允许的最大值 (硬性限制)。

详细的属性说明和配置列表，请参考: **[全局属性配置指南](GLOBAL_ATTRIBUTES_GUIDE_CN.md)**

---

## 6. 调试与指令 (Commands)

模组内置了一些指令用于调试和查看状态。

### `/magicinfo`
*   **权限**: 所有玩家
*   **功能**: 显示当前玩家的魔法状态详情（中文界面），包含：
    *   **基础属性**: 职业、元素亲和、魔力 (当前/最大/恢复)、法术强度、冷却缩减、硬直值。
    *   **属性伤害**: 金、木、水、火、光、暗、风、冰这 8 种元素的伤害加成。
    *   **防御属性**: 魔法韧性、魔法抗性。

### `/magicinit`
*   **权限**: 管理员 (OP Level 2)
*   **功能**: 初始化/重置玩家属性。
    1. 清除旧体质的所有属性修饰符。
    2. 将魔法属性（魔力上限等）重置为 `global_attributes.json` 定义的默认基础值。
    3. 清除当前体质与元素，重新触发“觉醒”流程。
    4. 充满魔力值并清空硬直条。
