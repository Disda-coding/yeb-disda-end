# ☁️E办后台
后台技术栈：
- MapStruct
- Mybatisplus
- Springboot
- Swagger2
- SpringSecurity
- RabbitMQ
- Redis
- Mysql
- JWT
- jackson
- EasyPOI
- WebSocket
## JWT拦截器
- 实现了令牌刷新机制
## exception
添加了全局的异常处理
## Service
### ISendMailService
- 添加了发送邮件验证码的功能
## Admin-client
添加了

# bugs
- 权限修改又有bug，外键依赖需要解决 Mapper中index改成item解决
# todolist
- 阿里巴巴规范参考
- 测试功能，记录bugs
- 统一接口
- 对比vhr（研究它认证过程
- jwt刷新方案
- 查看 学之思
- Security用户权限可以走缓存（提高并发量，其实能支撑，场景需要并发量没那么大，可以走本地缓存类似guavacache）（菜单已经走缓存了，
- 修复bug
- vhr
- 吃透！
- 研究websocket聊天
