# HUD 开发与扩展指南 (HUD_DEVELOPMENT.md)

本指南介绍 ProjectArcana 的 HUD 渲染系统，以便开发者后续进行视觉扩展或功能增加。

## 🎨 1. 视觉布局 (Layout)

HUD 采用 **极致压缩 (Ultra-Compact)** 设计，默认固定在屏幕 **左上角 (Top-Left)**。

- **起始坐标**: `(8, 8)`
- **进度条尺寸**: 宽度 `70px`，高度 `2px`。
- **行间距**: 默认为 `10px`。
- **渲染层级**: 通过 `event.registerAboveAll` 注册，确保显示在所有原版 UI 之上。

## 🔄 2. 数据流向 (Data Flow)

HUD 仅在 **客户端** 运行，通过以下链路获取数据：

1.  **服务端同步**: `ModEvents.onPlayerTick` 每 tick 调用 `PacketSyncMagicStats`。
2.  **数据包处理**: 客户端接收包后更新 `ClientMagicStatsData` 的静态变量。
3.  **API 访问**: `MagicHUD` 通过 `MagicAPI` 获取数值。`MagicAPI` 会自动判断当前环境（客户端）并从 `ClientMagicStatsData` 读取。

## 🌐 3. 本地化系统 (Localization)

所有文本都**严禁**硬编码在渲染类中。必须使用语言文件：

- **基础标签**: `hud.projectarcana.mana`, `hud.projectarcana.ap` 等。
- **元素名称**: `element.projectarcana.[element_name]` (如 `fire` -> `火`)。
- **职业名称**: `archetype.projectarcana.[archetype_id]` (如 `adept` -> `熟练者`)。

## 🛠️ 4. 渲染工具类 (Utilities)

`MagicHUD` 内部提供了两个核心工具方法：

-   `renderBar`: 渲染一个带背景槽的进度条。支持自定义颜色和侧边 Label。
-   `renderShadowedString`: 渲染带阴影的文字，提高在复杂背景下的可读性。

## 🚀 5. 如何扩展 (How to Extend)

### 添加一个新的状态条 (如 XP)
1.  在 `ClientMagicStatsData` 和 `PacketSyncMagicStats` 中增加字段。
2.  在 `MagicAPI` 中增加对应的客户端/服务端快捷访问方法。
3.  在 `MagicHUD.render` 中增加一行逻辑：
    ```java
    renderBar(guiGraphics, mc, x, y, barWidth, barHeight, xpFill, 0xFF00FF00, "XP: " + xp);
    y += spacing;
    ```

### 改变位置
修改 `render` 方法顶部的 `x` 和 `y` 初始值即可。如果需要支持动态位置，建议接入 Forge 的配置文件系统。
