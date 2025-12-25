# ProjectArcana 核心架构说明 (README_CORE.md)

欢迎来到 ProjectArcana 模组开发文档。本模组为 Minecraft Forge 1.20.1 提供了一套高度可扩展的魔法与属性系统。

## 🏛️ 核心分层设计

ProjectArcana 采用了清晰的模块化架构，旨在让开发者能够轻松扩展魔法行为：

1.  **注册层 (Registry)**: 定义模组的物理存在（属性、物品、法术 ID）。
2.  **API 工具层 (API)**: 提供统一的访问接口，处理复杂的逻辑计算（如伤害公式、碰撞判定）。
3.  **能力持久化层 (Capability)**: 负责在服务端存储玩家的非原生态魔法数据，并处理 NBT 读写。
4.  **数据驱动层 (Data)**: 利用 JSON 资源包定义“觉醒原型”及其权重，无需修改代码即可平衡游戏。
5.  **网络同步层 (Networking)**: 确保客户端 HUD 能够实时反映服务端的变化。

## 📁 目录结构指南

- `site.backrer.projectarcana.api`: 所有外部交互的起点。
- `site.backrer.projectarcana.capability`: 定义数据存储结构。
- `site.backrer.projectarcana.logic`: 处理游戏内的逻辑转换（如觉醒过程）。
- `site.backrer.projectarcana.data`: JSON 资源加载器。

## 🚀 快速开始

- 想要获取玩家数据？请参阅 `API_REFERENCE.md`。
- 想要创建新法术？请参阅 `SPELL_DEVELOPMENT.md`。
