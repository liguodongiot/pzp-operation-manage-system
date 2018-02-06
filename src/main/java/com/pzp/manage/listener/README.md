


```
HttpSessionListener有2个接口需要实现

sessionCreated  //新建一个会话时候触发也可以说是客户端第一次和服务器交互时候触发

sessionDestroyed    //销毁会话的时候  一般来说只有某个按钮触发进行销毁 或者配置定时销毁 

HttpSessionAttributeListener有3个接口需要实现

public voidattributeAdded(HttpSessionBindingEvent se) //在session中添加对象时触发此操作

public voidattributeRemoved(HttpSessionBindingEvent se) //修改、删除session中添加对象时触发此操作

public voidattributeReplaced(HttpSessionBindingEvent se)    //在Session属性被重新设置时触发此操作
```