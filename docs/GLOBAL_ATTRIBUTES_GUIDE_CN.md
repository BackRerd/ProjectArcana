# 全局属性配置指南 (Global Attributes Config)

Project Arcana 使用数据包 (Data Pack) 来管理所有自定义魔法属性的全局默认配置。

**配置文件路径**: `src/main/resources/data/projectarcana/magic_config/global_attributes.json`

## 配置结构

该 JSON 文件包含一个 `default_attributes` 列表，每一项定义了一个属性的配置。

```json
{
  "attribute": "属性注册名",
  "value": 100.0, // 默认基础值
  "min": 0.0,     // 允许的最小值
  "max": 10000.0  // 允许的最大值
}
```

修改此文件后，新的玩家（或属性重置后的玩家）将获得这些新的基础数值。同时，`min` 和 `max` 限制会通过运行时动态修改应用到游戏引擎中。

## 属性列表详解

以下是所有可配置的魔法属性及其说明：

### 核心属性

| 属性 ID (attribute) | 说明 | 推荐范围 |
| :--- | :--- | :--- |
| `projectarcana:max_mana` | **最大魔力值**。玩家拥有的魔法值上限。 | 0 - 1,000,000 |
| `projectarcana:mana_regen` | **魔力恢复速度**。每秒恢复的魔力数值 (近似)。 | 0 - 100 |
| `projectarcana:spell_power` | **法术强度**。增加法术伤害或效果的基础百分比或固定值 (视具体实现而定)。 | 0 - 无限 |
| `projectarcana:cooldown_reduction` | **冷却缩减**。减少法术冷却时间的百分比 (0.0 = 0%, 1.0 = 100%)。 | 0.0 - 1.0 (推荐上限 0.8) |
| `projectarcana:max_stagger` | **最大硬直条**。当受到特定攻击时累计硬直，超过此值会进入眩晕状态。 | 0 - 10,000 |

### 元素伤害加成

这些属性直接增加对应元素类型法术的伤害。

| 属性 ID (attribute) | 元素类型 |
| :--- | :--- |
| `projectarcana:element_metal_damage` | 金 (Metal) |
| `projectarcana:element_wood_damage` | 木 (Wood) |
| `projectarcana:element_water_damage` | 水 (Water) |
| `projectarcana:element_fire_damage` | 火 (Fire) |
| `projectarcana:element_light_damage` | 光 (Light) |
| `projectarcana:element_dark_damage` | 暗 (Dark) |
| `projectarcana:element_wind_damage` | 风 (Wind) |
| `projectarcana:element_ice_damage` | 冰 (Ice) |

默认值通常为 `0.0`。

### 防御属性

| 属性 ID (attribute) | 说明 |
| :--- | :--- |
| `projectarcana:magic_resilience` | **魔法韧性**。减少负面魔法效果的持续时间或减少硬直累计量。 |
| `projectarcana:magic_resistance` | **魔法抗性**。直接百分比或固定值减少受到的魔法伤害。 |

## 如何修改

1.  **对于模组开发者**: 修改 `src/main/resources/.../global_attributes.json`。
2.  **对于整合包作者**: 创建一个数据包，在 `data/projectarcana/magic_config/global_attributes.json` 路径下放置相同结构的 JSON 文件以覆盖默认值。
3.  **生效**: 修改数据包后，如果是单人游戏或服务器，通常需要重启游戏（因为属性的 Min/Max 需要在注册阶段或加载早期通过反射注入，热重载可能只更新 Value 而不更新 Min/Max）。

## 注意事项

*   `min` 和 `max` 的修改使用了底层反射技术，虽然经过测试，但在极少数情况下可能与其他修改属性底层逻辑的模组冲突。
*   如果你将 `max_mana` 的 `min` 设为 100，`value` 设为 50，游戏逻辑可能会强制将其修正为 100。请确保 `min <= value <= max`。
