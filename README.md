# StreamVault 🎬

<div align="center">

> 🚀 多平台视频下载整合方案  通过小程序 快捷指令 API WEB 等提交单链快速下载视频 并支持收藏夹模式及监控


[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/lemon8866/StreamVault)
[![Docker Pulls](https://img.shields.io/docker/pulls/qingfeng2336/stream-vault)](https://hub.docker.com/r/qingfeng2336/stream-vault)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17+-red.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
![Build Status](https://github.com/lemon8866/StreamVault/actions/workflows/docker-image.yml/badge.svg)

</div>

> ⚠️ **注意事项**:
>
> 1. 如Docker Hub镜像版本不包含环境变量需要使用的参数（例如：代理、掩码、时区等），建议自行修改Dockerfile并编译。编译文件位于`backstage/src/main/docker/buildx`，已包含所有必要文件,编译后的jar文件不在上传,建议使用actions编译。
> 2. 更多详细部署方式和配置说明，请先查看[旧版文档](doc/README.md)和[更新日志](doc/updaterecords.md)，部署方式等可以参考旧版方式。
> 3. 已经借助AI之力完成了本项目的wiki，具体使用可以参考[项目Wiki](https://github.com/lemon8866/StreamVault/wiki)
> 4. 所有配套客户端及源码可在 `app` 文件夹下找到
> 5. 本项目相对于其他项目来说  使用的java语言  docker部署时建议添加内存限制
> 6. 本项目没有做项目拆分为多个仓库  所以本仓库包含 uniapp tauri electron java 浏览器扩展 每个平台源码
> 7. `backstage为后台服务端 java`  `app/uniapp  手机端源码`   `app/extend  浏览器扩展`  `app/desktop  桌面端客户端源码`


## ⚠️ 声明

* **请严格遵守爬虫规范，不要使用此项目进行任何违法行为。**
* **不出售、共享、加密、上传、研究和传播任何个人信息。**
* **项目及其相关代码仅供学习与研究使用，不构成任何明示或暗示的保证。**
* **使用者因使用此项目及其代码可能造成的任何形式的损失，使用者应当自行承担一切风险。**
* **如果使用者使用此项目及其代码，即代表使用者同意遵守上述规定。**

## 🌟 项目简介

StreamVault（原名：spirit）是一个视频资源管理与下载平台，支持多平台视频解析和下载，提供便捷的资源管理功能。支持API提交视频地址等

## ✨ 主要特性

### 🚀 核心功能

- 🎥 API推送单视频地址下载
- ⬇️ 多种下载方式支持（HTTP、Aria2）
- 📚 哔哩哔哩收藏夹下载与监控
- ❤️ 抖音作品与喜欢列表下载与监控 
- 📋 NFO元数据生成(由于目前机制问题 目前测试仅仅jellyfin支持显示演员头像)
- 💾 视频资源缓存管理
- 📢 下载完成Webhook通知（支持企业微信群机器人/飞书）

### 📝 已计划内容

- ~~📸 图文类模块(已实现)~~
- ~~🏠 主页投稿类监控(已完成)~~
- ~~🔄 监控类型优化~~
- ~~⚡ 应用瘦身及http下载 高可用低占用~~
- 🔧 yt-dlp内置更新方式
- Twitter主页或收藏监控
### 🎯 平台支持

状态说明：

- ✅ 支持
- ❌ 不支持
- 🤔 考虑中
- 🔨 开发中
- 🚀 未来会做


| 平台      | 单链接 | 收藏/作品/主页 | 下载类型     | 备注                                              |
| --------- | ------ | -------------- | ------------ | ------------------------------------------------- |
| 抖音      | ✅     | ✅             | HTTP/Aria2   |                                                   |
| 哔哩哔哩  | ✅     | ✅             | HTTP/Aria2   |                                                   |
| YouTube   | ✅     | 🔨             | 仅支持yt-dlp | 备注①  |
| Twitter   | ✅     | 🔨             | 仅支持yt-dlp | 备注① |
| Instagram | ✅     | 🤔             | 仅支持yt-dlp | 备注① |
| TikTok    | ✅      | 🤔             | 暂时通过yt-dlp|                                                   |
| 快手      | ✅     | 🤔             | HTTP/Aria2   |备注② |
| 微博      | ✅     | 🤔             | 暂时通过yt-dlp |                                                   |
| 红薯      | ✅     | 🤔             | 暂时通过yt-dlp |                                                   |
| 通用平台  | ✅     | ❌            |  仅支持yt-dlp| 备注③ |

* **通用平台**：除抖音、哔哩哔哩、快手外的所有平台，包括但不限于YouTube、Twitter、Instagram、微博、小红书等，均通过yt-dlp处理。详细支持列表请参考[yt-dlp官方支持站点文档](https://github.com/yt-dlp/yt-dlp/blob/master/supportedsites.md)。
* 这里通用平台只是代表没有适配生成NFO 等一些小细节问题
* 由于红薯 微博 TikTok 未添加处理 所以 暂时走通用平台进行处理
* 通用平台支持什么具体请自测
* 备注①： docker版自带 避免产生过多ts文件 还需要合并 麻烦
* 备注②： 未测试Aria2,同时如果出现captcha 自行去APP或web验证后在测试 本处不处理captcha
* 备注③： docker版自带 不接受issues 具体支持地址参考yt-dlp仓库

### 💻 技术栈

- 🛠️ 后端：Spring Boot 3.5.x + JPA + SQLite
- 📱 前端：UniApp（支持小程序、APP等多端）
- 🐳 容器化：Docker多架构支持（AMD64/ARM64）

## 🔧 部署指南

### 🐳 Docker部署（推荐）

```bash
# 拉取镜像
docker pull qingfeng2336/stream-vault

# 运行容器
docker run --name stream-vault -d -p 28083:28081 \
  -v d:/home/spirit:/app \
  -v d:/home/spirit/tmp:/tmp \
  qingfeng2336/stream-vault
```

[Docker Hub](https://hub.docker.com/r/qingfeng2336/stream-vault) | [使用文档](https://github.com/lemon8866/StreamVault/wiki)

### 🚀 快速开始

1. 🔗 访问 http://your-ip:28083/admin/login
2. 🔑 使用默认账号密码登录
3. ⚙️ 在设置中删除admin并重新新建账号
4. 🎉 开始使用

### 📦 手动部署

- 要求：Java 17
- 详细部署文档待完善

## 📸 功能展示

> 以下为 部分 系统功能截图，更多界面可在实际使用中体验，仅供示例参考。

<table>
  <tr>
    <td align="center" width="50%">
      <img src="https://i.imgur.com/AvyHgcZ.png" alt="配置页面" width="100%"><br/>
      <sub>⚙️ 配置页面</sub>
    </td>
    <td align="center" width="50%">
      <img src="https://i.imgur.com/i2DMGXq.png" alt="收藏添加" width="100%"><br/>
      <sub>📚 收藏添加</sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <img src="https://i.imgur.com/s4eWADU.png" alt="合并功能" width="100%"><br/>
      <sub>🔄 合并功能</sub>
    </td>
    <td align="center" width="50%">
      <img src="https://i.imgur.com/RB260MS.png" alt="后台首页" width="100%"><br/>
      <sub>🏠 后台首页</sub>
    </td>
  </tr>
</table>

> 提示：点击图片可查看原图细节。


## 📝 更新日志

查看 [更新日志](doc/updaterecords.md) 了解详细更新内容。

## 📱 客户端使用

### 🔗 访问方式

- 🌐 Web后台：http://your-ip:28081/admin/login
- 👤 默认账号：admin
- 🔑 默认密码：123456

### 📱 移动端支持

- 🤖 Android APP
- 💬 微信小程序（开发者模式）
- 🌍 其他UniApp支持的平台

## 🔌 API接口

### 📤 推送接口

```http
POST http://ip:port/api/processingVideos
参数：
- token: 后台设置的token
- video: 链接或分享口令
```

### 📋 获取视频列表

```http
POST http://ip:port/api/findVideos
参数：
- token: 后台设置的token（必填）
- pageNo: 页数（必填）
- pageSize: 每页数量（必填）
- videodesc: 视频描述（选填）
- videoname: 视频名称（选填）
- videoplatform: 视频平台（选填）
```

### 📝 书签提交方式

```javascript
javascript:(function(){
    var token = "你的token";  
    var url = window.location.href; 
    fetch("http://ip:port/api/processingVideos", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "token=" + encodeURIComponent(token) + "&video=" + encodeURIComponent(url)
    }).then(response => response.json())
      .then(data => alert("请求成功: " + JSON.stringify(data)))
      .catch(error => alert("请求失败: " + error));
})();
```

> ⚠️ **注意**: 通过接口获取的视频播放链接或缩略图，访问时需追加 `?apptoken=xxxx` 参数，否则无法访问。


## 🙏 致谢

项目参考及使用了以下优秀的开源项目：

- [bilibili-API-collect](https://github.com/SocialSisterYi/bilibili-API-collect)
- [parsing-tiktok-video](https://toscode.gitee.com/zong_zh/parsing-tiktok-video)
- [f2](https://github.com/Johnserf-Seed/f2)
- [Light-Year-Admin-Template](https://gitee.com/yinqi/Light-Year-Admin-Template)
- [yt-dlp](https://github.com/yt-dlp/yt-dlp)

---

## 📄 LICENSE

本项目主体代码采用 [Apache License 2.0](./LICENSE) 授权。

> **注意**：
> - 由于 `bilibili-API-collect` 代码的非商业限制（CC BY-NC 4.0 协议），包含该部分代码的版本 **禁止商业使用**。  
> - 使用本项目时，需同时遵守上述第三方项目的许可证条款。

<div align="center">
感谢所有开源项目的贡献者！
</div>

