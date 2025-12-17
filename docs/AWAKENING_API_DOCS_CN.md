# 魔法觉醒 API 文档 (Awakening API)

本文档面向开发者，介绍如何在代码中与觉醒系统交互。

## 核心类

### 1. AwakeningHandler
位于：`site.backrer.projectarcana.logic.AwakeningHandler`

负责处理玩家觉醒的主要逻辑。

#### 方法
- `public static void awakenPlayer(Player player)`
    - **描述**: 对指定玩家执行觉醒流程。
    - **逻辑**:
        1. 从 `AwakeningDataLoader` 中根据权重随机抽取一个 `AwakeningArchetype`。
        2. 根据体质配置，随机分配魔法元素 (Magic Elements)。
        3. 应用属性修正 (Attribute Modifiers)。
        4. **将觉醒结果 (Archetype ID, 元素列表) 保存到玩家 Capability 中**。
        5. **自动同步数据到客户端**。
    - **使用场景**: 玩家首次登录事件、吃下特定物品重置觉醒等。

### 2. AwakeningDataLoader
位于：`site.backrer.projectarcana.data.AwakeningDataLoader`

负责从数据包中加载 **觉醒体质 (Archetype)** 的 JSON 配置。

#### 方法
- `public static Map<ResourceLocation, AwakeningArchetype> getArchetypes()`
    - **描述**: 获取当前加载的所有体质数据。
    - **返回**: 一个不可变的 Map，键为资源路径，值为体质对象。

- `public static Map.Entry<ResourceLocation, AwakeningArchetype> pickRandomArchetype()`
    - **描述**: 根据权重随机选择一个体质。
    - **返回**: 选中的体质条目。

### 3. MagicConfigDataLoader
位于：`site.backrer.projectarcana.data.MagicConfigDataLoader`

负责加载 **全局属性配置 (Global Attributes)**。

#### 方法
- `public static MagicConfig getConfig()`
    - **描述**: 获取当前的全局属性配置对象。
    - **返回**: `MagicConfig` 对象，包含所有属性的默认值、最小值和最大值。

### 4. MagicStats (Capability)
位于：`site.backrer.projectarcana.capability.MagicStats` (实现 `IMagicStats`)

用于存储玩家的觉醒数据。

#### 新增方法
- `String getArchetype()`: 获取玩家觉醒的体质 ID (如 `projectarcana:adept`)。
- `List<MagicElement> getElements()`: 获取玩家掌握的所有元素列表。

### 5. ClientMagicStatsData
位于：`site.backrer.projectarcana.capability.ClientMagicStatsData`

**客户端专用**。用于在 GUI 或 HUD 中获取当前玩家的觉醒信息。数据会自动从服务端同步。

#### 方法
- `public static String getArchetype()`
    - **描述**: 获取当前客户端玩家的体质 ID。
- `public static List<String> getElements()`
    - **描述**: 获取当前客户端玩家的元素列表 (String 形式)。

## 集成指南

### 触发觉醒
通常你需要在事件处理类中调用 `AwakeningHandler.awakenPlayer(player)`。

```java
@SubscribeEvent
public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    Player player = event.getEntity();
    // 检查玩家是否已经觉醒 (需要你自己实现 Capability 检查逻辑或者使用 NBT 判断)
    player.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(cap -> {
         if (cap.getArchetype().isEmpty()) { // 如果体质为空，说明未觉醒
             AwakeningHandler.awakenPlayer(player);
         }
    });
}
```

### 客户端渲染 (UI/Overlay)
如果你想在 HUD 上显示玩家的元素图标：

```java
// 仅在客户端调用
String archetype = ClientMagicStatsData.getArchetype();
List<String> elements = ClientMagicStatsData.getElements();

if (elements.contains("FIRE")) {
    // 渲染火元素图标
}
```
