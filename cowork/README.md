# ☁️E办后台
## 后台技术栈：
- Springboot
    - 开发框架
- MapStruct 
  - 用于Beans的转换
- Mybatisplus
  - 用于ORM和数据库逆向
- Swagger2
  - 文档API工具
- SpringSecurity
  - 用于认证和鉴权
- RabbitMQ
  - 消息队列
- Redis
  - 用于分布式缓存
- Mysql
  - 用于数据持久化
- JWT
  - 用于自解释签名，会话信息存放在
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
## imporve
- 统一返回规范
- 使用阿里巴巴规范插件维护代码
- jwt刷新方案
# bugs
## solved
- 无法使用redis反序列化Admin，没有setAuthorities方法
  - 使用@JsonField注解，不参与序列化
# todolist
- 对比vhr（研究它认证过程
- 查看 学之思
- 修复bug
- vhr
- 吃透！
- 研究websocket聊天
