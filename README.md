# Project Arcana

Project Arcana 是一个基于 Minecraft Forge 1.20.1 的魔法模组，旨在提供深度可定制的魔法觉醒体验、复杂的属性系统和沉浸式的元素战斗机制。

## 🌟 核心特性 (Key Features)

### 1. 魔法觉醒系统 (Awakening System)
- **随机体质**: 玩家首次进入游戏时会随机“觉醒”一种魔法体质 (如 熟练者、智者、化身、虚无等)。
- **元素亲和**: 根据体质获得不同数量的魔法元素 (金、木、水、火、光、暗、风、冰)。
- **完全数据驱动**: 所有觉醒概率、体质属性修正均通过 **Data Pack (数据包)** 配置，实现零代码扩展职业。
- [详细文档: 核心架构说明](docs/README_CORE.md)

### 2. 实时魔法仪表盘 (Magic HUD)
- **极致压缩设计**: 位于左上角的 2px 纤细仪表盘，不遮挡视线，极致纯净。
- **全方位监控**: 实时显示魔力值 (带低蓝预警)、盾量、硬直、法术强度 (AP) 以及冷却缩减 (CD)。
- **职业与元素汉化**: 自动汉化匹配职业名称及元素单字图标。
- [详细文档: HUD 开发与扩展指南](docs/HUD_DEVELOPMENT.md)

### 3. 全局属性与法术系统
- **15+ 自定义属性**: 引入了魔力值、硬直、法术强度、冷却缩减以及 8 种元素伤害等属性。
- **配置灵活**: 所有属性的边界值均可通过 Data Pack 进行热配置。
- **配置指南**: 属性配置 JSON 位于 `data/projectarcana/magic_config/`。
- [详细文档: 法术开发与伤害公式](docs/SPELL_DEVELOPMENT.md)

### 4. 开发者 API
- **MagicAPI**: 封装了所有关于魔法属性的访问，自动处理客户端/服务端同步逻辑。
- **数据同步**: 基于 Netty 的自定义数据包传输，确保 HUD 与服务端数值毫秒级同步。
- [详细文档: 开发者 API 参考](docs/API_REFERENCE.md)

## 🛠️ 项目结构

- `src/main/java`: 核心业务代码。
- `src/main/resources/data/projectarcana`: 默认职业原型与全局属性配置 (Json)。
- `docs/`: 完整的中文技术文档库。

## 快速开始

1.  克隆仓库。
2.  运行 `gradlew genIntellijRuns` (或 `genEclipseRuns`) 环境配置。
3.  运行 `runClient` 启动游戏。
4.  进服后，观察 HUD 变化或使用 `/magicinfo` 查看你的觉醒数值。

## 协议 (License)
(在此处添加你的开源协议)
