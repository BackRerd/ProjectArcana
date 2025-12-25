# 法术开发、伤害与碰撞指南 (SPELL_DEVELOPMENT.md)

本指南介绍如何利用 ProjectArcana 系统创建具有逻辑深度的魔法。

## 🪄 1. 创建新法术 (New Spell)

所有的法术必须实现 `IMagicSpell` 接口。**推荐**继承 `AbstractMagicSpell` 类，因为它封装了资源消耗和基本的生命周期方法。

### 模板示例：
```java
public class FireballSpell extends AbstractMagicSpell {
    @Override
    protected void onCast(LivingEntity caster) {
        // 核心逻辑：
        // 1. 生成投射物（建议配合 IMagicProjectile 接口）
        // 2. 或者直接对前方目标应用伤害：
        // applyDamage(caster, target, 15.0f); // 自动计算法强、元素等
        
        // 3. 产生视觉效果 (服务端向客户端发包)
        if (caster instanceof ServerPlayer serverPlayer) {
            ModMessages.sendToPlayer(new PacketSpawnMagicEffect(caster.getX(), caster.getY(), caster.getZ(), 0, 15), serverPlayer);
        }
    }

    @Override
    public float getManaCost() { return 20f; }

    @Override
    public int getCooldownTicks() { return 60; }

    @Override
    public Optional<MagicElement> getElement() { return Optional.of(MagicElement.FIRE); }
    
    @Override
    public String getRegistryName() { return "fireball"; }
}
```

## ⚔️ 2. 伤害计算公式 (Damage Formula)

模组在 `MagicDamageHelper` 中实现了一套标准算法，详细步骤如下：

### 第一阶段：有效基础伤害 (Strength Calculation)
`有效伤害 = (基础伤害 + 攻击者法术强度) * (1 + 攻击者对应元素加成%)`
> **注**：法术强度读取自 `SPELL_POWER` 属性；元素加成读取自对应元素的 Damage 属性（如 `ELEMENT_FIRE_DAMAGE`），1 点属性 = 1% 加成。

### 第二阶段：伤害减免 (Defense Calculation)
1.  **抗性减免 (Resistance)**：被害者的 `MAGIC_RESISTANCE` 属性，1 点 = 1% 减免，上限 80%。
2.  **韧性减免 (Resilience)**：被害者的 `MAGIC_RESILIENCE` 属性，从剩余伤害中直接减去固定点数。
`最终伤害 = 有效伤害 * (1 - 减伤系数) - 韧性值`（结果不会低于 0.5）。

### 第三阶段：后续分配 (Post-Process)
- **护盾优先**：优先消耗被害者 `IMagicStats` 中的 `Shield` 值。
- **实体伤害**：剩余伤害以 `magic_spell` 伤害类型扣除生命值。
- **硬直产生**：造成的伤害值会经过被害者的 `Toughness`（韧性）削竭后转化为 `Stagger`（硬直）值。

## 💥 3. 魔法碰撞 (Magic Collision)

投射物类法术建议实现 `IMagicProjectile`。

### 判定方法：
在你的投射物碰撞回调中（或 Tick 检测中）调用：
`MagicCollisionHelper.resolveCollision(p1, p2)`

### 内部判定逻辑：
- **抵消**：若两魔法能量（Power）差距在 20% 以内，两者均触发 `onMagicDestroyed()` 消失。
- **穿透**：若一方显著强于另一方，强者消耗掉等同于弱者的能量后继续飞行。

---

## 示例：手动应用伤害
```java
// 在任何地方触发全自动魔法伤害逻辑
MagicDamageHelper.applyMagicDamage(attacker, victim, 10.0f, MagicElement.FIRE);
```
