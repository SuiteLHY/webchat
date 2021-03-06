/**
 * 用户界面层 (或"对外交互层"): 负责向用户显示信息和解释用户指令.
 * -> 这里指的用户可以是另一个计算机系统, 不一定是使用界面的人.
 * <p>
 * Web层主要用来处理用户请求, 并调用应用服务层 (Service) 以获
 * -> 取用户请求响应数据.
 * <p>
 * 前后端分离之后, Web层应该更改为外部接口层 (Facade / API),
 * -> 外部接口提供符合 REST 规范的响应数据.
 */
package github.com.suitelhy.dingding.log.service.client.web;