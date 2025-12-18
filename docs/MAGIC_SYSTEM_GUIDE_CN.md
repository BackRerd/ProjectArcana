# 模块化魔法与战斗系统开发文档 (Magic System Guide)

本文件旨在指导开发者如何在 Project Arcana 框架下扩展魔法内容、处理资源消耗及实现战斗逻辑。

---

## 1. 架构概览 (Architecture Overview)

系统采用 **策略模式 (Strategy Pattern)** 实现，将魔法执行逻辑从实体类中解耦。

### 核心包路径
- **API**: `site.backrer.projectarcana.api.spell`
- **Capability**: `site.backrer.projectarcana.capability`
- **Networking**: `site.backrer.projectarcana.networking.packet`
- **Registry**: `site.backrer.projectarcana.registry`

---

## 2. 魔法策略 (Magic Strategies)

### IMagicSpell (接口)
所有魔法的基石。定义了 `cast(...)`, `getManaCost()`, `getCooldownTicks()` 等核心方法。

### AbstractMagicSpell (抽象基类)
封装了通用的重复逻辑：
- **资源自检**: 自动检查魔力是否足够。
- **资源扣除**: 自动扣除魔力值。
- **冷却触发**: (待完善) 将冷却时间写入 Capability。

### 专用策略类
- **ElementalSpell**: 绑定单一元素 (`MagicElement`)，支持属性特化逻辑。
- **ComboSpell**: 处理双元素反应（如：水 + 火 = 蒸汽爆炸）。

---

## 3. 资源与能力系统 (Resource & Capability)

通过 `IMagicStats` 存储玩家动态数据：

| 属性 | 功能说明 |
| :--- | :--- |
| **Mana** | 基础魔力，通过属性系统的 `max_mana` 决定上限。 |
| **Stagger** | 身体负荷/硬直累积。 |
| **Toughness** | 属性化的防御韧性（减少受到的击退或特定伤害）。 |
| **Stiffness** | 僵直度（影响受击后的恢复时间）。 |
| **Shield** | **魔法护盾**：吸收即将到来的魔法伤害，溢出部分转入生命值。 |
| **Cooldowns** | **独立冷却系统**：使用 `Map<String, Integer>` 存储每个魔法的剩余冷却 Tick。 |

---

## 4. 魔法伤害计算机制 (Damage Mechanics)

系统通过 `MagicDamageHelper` 实现了一套通用的魔法伤害计算公式，综合考虑了攻击者和受击者的多维属性。

### 4.1 核心公式
1. **基础计算**: `有效伤害 = (技能基础伤害 + 攻击者法术强度) * (1 + 元素伤害加成)`
2. **减伤逻辑**: `折减伤害 = 有效伤害 * (1 - 受击者魔抗百分比) - 受击者魔抗固定减免`
3. **最终伤害**: `最终伤害 = Math.max(0.5, 折减伤害)`

### 4.2 硬直累积 (Stagger)
魔法伤害会造成硬直，计算方式为：
`魔法硬直量 = 最终伤害 * 硬直倍率 * (1 - 受击者韧性减免)`

### 4.3 属性对应关系
- **法术强度**: 对应属性 `projectarcana:spell_power`。
- **元素加成**: 对应属性如 `projectarcana:element_fire_damage`。
- **魔抗系数**: 1 点魔抗对应约 1% 的百分比减伤（可配置）。
- **韧性**: 1 点韧性对应约 1% 的硬直减免。

### 4.4 魔法护盾 (Shield)
伤害触发公式变为：
`最终扣血量 = Math.max(0, 最终魔法伤害 - 当前护盾值)`
`当前护盾值 = Math.max(0, 当前护盾值 - 最终魔法伤害)`

### 4.5 魔法对撞 (Spell Collision)
当两个实体（如弹射物）都实现 `IMagicProjectile` 接口并发生碰撞时，使用 `MagicCollisionHelper` 判定：
- **抵消**: 若双方强度差异在 20% 以内，两者同时消失。
- **穿透**: 若一方强度明显更高，则弱者消失，强者保留，且强者强度减去弱者的强度。

---

## 5. 网络通信 (Networking)

### PacketSpawnMagicEffect (S2C)
用于在客户端渲染粒子效果。
- **Fire (ID 0)**: 产生火焰粒子。
- **Ice/Water (ID 1)**: 产生雪花粒子。
- **Default**: 产生附魔击中粒子。

**调用示例**:
```java
ModMessages.sendToPlayer(new PacketSpawnMagicEffect(x, y, z, 0, 10), serverPlayer);
```

---

## 5. 开发指南：如何添加新魔法

### 第一步：创建魔法类
```java
public class FireballSpell extends ElementalSpell {
    public FireballSpell() {
        super(MagicElement.FIRE);
    }

    @Override
    public float getManaCost() { return 10.0f; }

    @Override
    public int getCooldownTicks() { return 40; } // 2秒

    @Override
    public String getRegistryName() { return "fireball"; }

    @Override
    protected void applyElementalEffect(LivingEntity caster) {
        // 实现发射火球的具体逻辑
    }
}
```

### 第二步：注册魔法
在模组初始化阶段（如 `FMLCommonSetupEvent`）注册：
```java
SpellRegistry.register(new FireballSpell());
```

### 第三步：触发魔法
```java
IMagicSpell spell = SpellRegistry.get("fireball");
if (spell != null) {
    spell.cast(player);
}
```

### 第四步：造成伤害 (推荐)
在魔法类中使用 `MagicDamageHelper`：
```java
MagicDamageHelper.applyMagicDamage(caster, target, 5.0f, MagicElement.FIRE);
```

---

## 7. 后续待办 (Roadmap)
- [ ] 实现 UI 层面的魔力条显示。
- [ ] 完整接入 `ModEvents` 中的 `TickEvent` 以自动衰减 Capability 中的冷却时间。
- [ ] 扩展组合反应 (Combo) 的逻辑工厂类。
