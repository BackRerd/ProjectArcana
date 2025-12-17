# 魔法觉醒系统 (Awakening System) 配置指南

本模组包含一个完全数据驱动的“魔法觉醒”系统，用于决定玩家初次进入游戏时的魔法体质（Archetype）、元素亲和以及基础属性修正。

所有的配置均通过 **Data Pack (数据包)** 的形式加载，这意味着你可以在不修改代码的情况下，调整概率、增加新的体质或修改属性加成。

## 1. 基础属性配置 (Global Attributes)
在进行觉醒配置之前，你可能需要调整所有玩家的**基础属性**（例如默认是否有 100 魔力）。

文件位置：`src/main/resources/data/projectarcana/magic_config/global_attributes.json`

该文件定义了所有魔法属性的：
- `value`: 默认基础值 (Base Value)。
- `min`: 允许的最小值。
- `max`: 允许的最大值。

**注意**: 觉醒系统给予的属性是 **Modifiers (修正值)**，它们是在这个基础值之上进行加减乘除的。

## 2. 觉醒体质配置 (Archetypes)
默认的配置文件位于：
`src/main/resources/data/projectarcana/awakening_archetypes/`

在实际的游戏或整合包中，你可以通过创建数据包来覆盖这些文件或添加新文件：
`data/projectarcana/awakening_archetypes/你的文件名.json`

### JSON 数据结构

每个 JSON 文件代表一种“体质” (Archetype)。

#### 字段说明

| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| `weight` | Double | **权重**。决定该体质出现的概率。概率 = 该体质权重 / 所有体质权重之和。 |
| `min_elements` | Integer | **最小元素数量**。随机获取的基础元素数量下限。 |
| `max_elements` | Integer | **最大元素数量**。随机获取的基础元素数量上限。 |
| `force_all_elements` | Boolean | 若为 `true`，强制获得所有 8 种元素（忽略 min/max）。 |
| `force_no_elements` | Boolean | 若为 `true`，强制获得 0 种元素（忽略 min/max）。 |
| `modifiers` | List | **属性修正列表**。觉醒时赋予玩家的永久属性加成。 |

#### Modifiers (属性修正) 结构

| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| `attribute` | String | 属性的注册名 (Registry Name)。例如 `minecraft:generic.max_health` 或 `projectarcana:max_mana`。 |
| `amount` | Double | 修正数值。 |
| `operation` | String | 运算方式。可选值：`ADDITION` (加法), `MULTIPLY_BASE`, `MULTIPLY_TOTAL`。 |
| `uuid` | String | **UUID**。必须是唯一的 UUID 字符串。用于确保修饰符的唯一性。 |
| `name` | String | 修饰符的显示名称。 |

### 配置示例

#### 示例 1: 贤者 (SAGE)
**特点**：高魔力，低生命，掌握 3-5 种元素。

```json
{
  "weight": 19.0,
  "min_elements": 3,
  "max_elements": 5,
  "force_all_elements": false,
  "force_no_elements": false,
  "modifiers": [
    {
      "attribute": "minecraft:generic.max_health",
      "amount": -4.0,
      "operation": "ADDITION",
      "uuid": "c8c1d5e6-4f2a-4e1b-9a8d-7c6b5a4e3f21",
      "name": "Awakening Health"
    },
    {
      "attribute": "projectarcana:max_mana",
      "amount": 400.0,
      "operation": "ADDITION",
      "uuid": "f1e2d3c4-b5a6-9d8e-7f0c-1b2a3d4e5f60",
      "name": "Awakening Mana"
    }
  ]
}
```

#### 示例 2: 破法者 (NULL)
**特点**：无魔法（魔力锁定为0），极高的物理抗性和生命值。

**注意**：为了强制魔力为 0，我们使用了一个极大的负数进行 `ADDITION` 运算，这比 `MULTIPLY` 更稳定。

```json
{
  "weight": 5.0,
  "min_elements": 0,
  "max_elements": 0,
  "force_all_elements": false,
  "force_no_elements": true,
  "modifiers": [
    {
      "attribute": "minecraft:generic.max_health",
      "amount": 80.0,
      "operation": "ADDITION",
      "uuid": "c8c1d5e6-4f2a-4e1b-9a8d-7c6b5a4e3f21",
      "name": "Awakening Health"
    },
    {
      "attribute": "projectarcana:max_mana",
      "amount": -1000000.0,
      "operation": "ADDITION",
      "uuid": "f1e2d3c4-b5a6-9d8e-7f0c-1b2a3d4e5f60",
      "name": "Awakening Mana Zero"
    }
  ]
}
```

## 如何添加自定义体质
1. 在 `data/projectarcana/awakening_archetypes/` 下创建一个新的 `.json` 文件。
2. 填写 `weight` 和属性参数。
3. 只需要使用 `/reload` 命令或重启游戏，新的体质就会被加载并参与随机池。
