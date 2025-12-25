# API 详细参考手册 (API_REFERENCE.md)

本手册详细介绍了如何通过代码访问玩家及实体的魔法属性。

## 📍 统一入口：`MagicAPI`

`site.backrer.projectarcana.api.MagicAPI` 是您最常用的类。它会自动处理逻辑侧（Server/Client）的差异。

### 1. 基础状态获取

| 方法名 | 描述 | 返回值类型 | 说明 |
| :--- | :--- | :--- | :--- |
| `getCurrentMana(LivingEntity)` | 获取当前魔力值 | `float` | 客户端会自动从同步缓存中读取 |
| `getMaxMana(LivingEntity)` | 获取最大魔力上限 | `float` | 基于 `max_mana` 属性系统 |
| `getCurrentStagger(LivingEntity)` | 获取当前硬直值 | `float` | |
| `getMaxStagger(LivingEntity)` | 获取最大硬直上限 | `float` | 基于 `max_stagger` 属性系统 |
| `getManaRegen(LivingEntity)` | 获取魔力恢复速度 | `float` | 每秒恢复的魔力基础值 |
| `getCurrentArchetype(LivingEntity)` | 获取实体的觉醒原型 ID | `String` | 如 "projectarcana:fire_mage" |
| `getAssignedElements(LivingEntity)` | 获取实体拥有的魔法元素列表 | `List<MagicElement>` | 返回该实体觉醒的所有元素 |

### 2. 元素伤害加成

所有元素属性均以百分比形式呈现。

| 方法名 | 对应的元素 | 计算方式 |
| :--- | :--- | :--- |
| `getMetalDamage(entity)` | 金 (Metal) | 返回值为 float，1.0 代表 +1% |
| `getWoodDamage(entity)` | 木 (Wood) | 同上 |
| `getWaterDamage(entity)` | 水 (Water) | ... |
| `getFireDamage(entity)` | 火 (Fire) | ... |
| `getLightDamage(entity)` | 光 (Light) | ... |
| `getDarkDamage(entity)` | 暗 (Dark) | ... |
| `getWindDamage(entity)` | 风 (Wind) | ... |
| `getIceDamage(entity)` | 冰 (Ice) | ... |

### 3. 防御与辅助属性 (Attribute Based)

- `getSpellPower(entity)`: 获取法术强度，直接增加法术的基础威力。
- `getCooldownReduction(entity)`: 获取冷却缩减（0.0 - 1.0）。
- `getMagicResilience(entity)`: 固定值的魔法减伤（Resilience）。
- `getMagicResistance(entity)`: 百分比魔法抗性（Resistance）。

## 🧬 能力数据访问 (Capability)

核心接口为 `IMagicStats`。

### 1. 基础读写

除了 `MagicAPI` 提供的快捷访问外，你可以直接操作 `IMagicStats` 来实现更复杂的功能：

```java
entity.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(stats -> {
    // 韧性与僵直 (Toughness & Stiffness)
    float toughness = stats.getToughness(); // 减少受到的硬直百分比
    float stiffness = stats.getStiffness(); // 减少被击退的强度
    
    // 护盾系统 (Shield)
    stats.addShield(20.0f); // 增加护盾，伤害会优先扣除此处
    float currentShield = stats.getShield();
    
    // 冷却管理 (Cooldowns)
    int remainingTicks = stats.getSpellCooldown("fireball");
    stats.setSpellCooldown("fireball", 100); // 设置 5 秒冷却 (20 ticks/sec)
});
```

### 2. 生命周期管理

- `copyFrom(IMagicStats source)`: 用于玩家死亡重连时的魔力状态迁移（模组已在 `ModEvents.onPlayerCloned` 中自动处理）。

## 💥 核心辅助类

### 1. 伤害源工具 (`ArcanaDamageSources`)

用于创建符合模组逻辑的伤害源，以便某些属性（如魔法抗性）能够正确生效。

```java
// 创建一个由攻击者发起的魔法伤害源
DamageSource source = ArcanaDamageSources.magicSpell(level, attacker);
```

### 2. 网络与特效工具 (`ModMessages`)

模组内置了一些简单的特效包，方便在服务端逻辑中直接触发客户端粒子。

```java
// 在服务端触发客户端粒子效果
// particleId: 0 (火), 1 (冰/雪), 2 (默认/附魔)
ModMessages.sendToPlayer(new PacketSpawnMagicEffect(x, y, z, 0, 15), serverPlayer);
```

### 3. 配置访问 (`MagicConfig`)

可以通过 `MagicConfigDataLoader.getConfig()` 获取当前的全局配置对象，用于读取玩家的基础属性默认值。
