# Project Arcana

Project Arcana 是一个基于 Minecraft Forge 1.20.1 的魔法模组，旨在提供深度可定制的魔法觉醒体验、属性系统和元素战斗机制。

## 🌟 核心特性 (Key Features)

### 1. 魔法觉醒系统 (Awakening System)
- **随机体质**: 玩家首次进入游戏时会随机“觉醒”一种魔法体质 (如 贤者 Adept, 化身 Avatar 等)。
- **元素亲和**: 根据体质获得不同数量的魔法元素 (金木水火光暗风冰)。
- **完全数据驱动**: 所有觉醒概率、体质属性修正均通过 **Data Pack (数据包)** 配置。
- [详细文档: 觉醒系统指南](docs/AWAKENING_SYSTEM_GUIDE_CN.md)

### 2. 全局属性配置 (Global Attributes)
- **自定义属性**: 引入了魔力值 (Mana)、硬直 (Stagger)、法术强度、冷却缩减以及各元素伤害加成等 15+ 种新属性。
- **配置灵活**: 所有属性的**默认值**、**最小值**和**最大值**均可通用 Data Pack 进行热配置，无需修改代码或重启 (重启推荐以应用极限值修改)。
- [详细文档: 全局属性配置指南](docs/GLOBAL_ATTRIBUTES_GUIDE_CN.md)

### 3. 数值同步与 API
- **服务端-客户端同步**: 核心数据（魔力、觉醒状态）自动同步，支持 HUD 渲染。
- **开发者 API**: 提供 `MagicAPI` 和 `AwakeningHandler` 供附属模组或插件调用。
- [详细文档: 开发者 API 文档](docs/AWAKENING_API_DOCS_CN.md)
- [详细文档: 模组开发指南](docs/DEVELOPMENT_GUIDE_CN.md)

### 4. 游戏内调试
- 使用 `/magicinfo` 指令查看当前的魔法状态面板。

## 🛠️ 项目结构

- `src/main/java`: 源代码
- `src/main/resources/data/projectarcana`: 默认数据包配置 (Config & Archetypes)
- `docs/`: 详细开发文档 (中文)

## 快速开始

1.  克隆仓库。
2.  运行 `gradlew genEclipseRuns` (或 `genIntellijRuns`)。
3.  运行 `gradlew runClient` 启动游戏。
4.  进服后，观察控制台或使用 `/magicinfo` 查看你的觉醒结果。

## 协议 (License)
(在此处添加你的开源协议)
