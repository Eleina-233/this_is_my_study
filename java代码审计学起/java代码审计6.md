# 今天总结一些java漏洞里的常见名词！



引用文章：

`https://mp.weixin.qq.com/s?__biz=MzI5Mzk5NTIwMg==&mid=2247486510&idx=1&sn=1240f64a1e017736c8aef006b577bc66`

`https://www.freebuf.com/vuls/279465.html`



## Java EE 的核心技术

Java EE 有十三种核心技术，它们分别是：JDBC、JNDI、EJB、RMI、Servlet、JSP、XML、JMS、Java IDL、JTS、JTA、JavaMail 和 JAF，这里重点介绍以下几种：

1. Java 数据库连接（Java Database Connectivity，**JDBC**）是 Java 语言中用来规范客户端程序如何来访问数据库的应用程序接口，提供了诸如查询和更新数据库中数据的方法。

2. Java 命名和目录接口（Java Naming and Directory Interface，**JNDI**），是 Java 的一个目录服务应用程序界面（API），它提供一个目录系统，并将服务名称与对象关联起来，从而使得开发人员在开发过程中可以使用名称来访问对象。

   **简单的说就是比如以前连接数据库需要把参数写在 Java 类里，但现在可以直接写在配置文件里了，这个配置文件可以是 XML，也可以是 properties，或者 yml 文件，只要能解析都行。**

3. 企业级 JavaBean（Enterprise JavaBean, EJB）是一个用来构筑企业级应用的、在服务器端可被管理组件。JavaBean 个人学习的时候理解JavaBean就是实体类！

   不过这个东西在 Spring 问世后基本凉凉了，知道是什么就行。

4. 远程方法调用（Remote Method Invocation，**RMI**）是 Java 的一组拥有开发分布式应用程序的 API，它大大增强了 Java 开发分布式应用的能力。

5. Servlet（Server Applet），是用 Java 编写的服务器端程序。

   其主要功能在于交互式地浏览和修改数据，生成动态 Web 内容。

   狭义的 Servlet 是指 Java 语言实现的一个接口，广义的 Servlet 是指任何实现了这个 Servlet 接口的类，一般情况下，人们将 Servlet 理解为后者。

6. JSP（全称 JavaServer Pages）是由 Sun 公司主导创建的一种动态网页技术标准。

   JSP 部署于网络服务器上，可以响应客户端发送的请求，并根据请求内容动态地生成 HTML、XML 或其他格式文档的 Web 网页，然后返回给请求者。

7. 可扩展标记语言（eXtensible Markup Language，XML）是被设计用于传输和存储数据的语言。

8. Java 服务消息（Java Message Service，JMS）是一个 Java 平台中关于面向消息中间件（MOM）的 API，用于在两个应用程序之间或分布式系统中发送消息，进行异步通信。

9.  **JNI** 。 Java语言是基于C语言实现的，Java底层的很多API都是通过`JNI(Java Native Interface)`来实现的。

10. 





## 反射

`反射、类加载、RPC、动态代理等。这些概念是java漏洞利用的基础，例如shiro反序列化，fastjson反序列化等。`

引用文章：`https://mp.weixin.qq.com/s?__biz=MzI4Mzc0MTI0Mw==&mid=2247491725&idx=1&sn=5b51624f6474fe905e6f95d6b8e1a0e0`

![image-20211116153850417](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116153850417.png)

```java

// 1.通过字符串获取Class对象，这个字符串必须带上完整路径名
Class studentClass = Class.forName("com.test.reflection.Student");
// 2.获取声明的构造方法，传入所需参数的类名，如果有多个参数，用','连接即可
Constructor studentConstructor = studentClass.getDeclaredConstructor(String.class);
// 如果是私有的构造方法，需要调用下面这一行代码使其可使用，公有的构造方法则不需要下面这一行代码
studentConstructor.setAccessible(true);
// 使用构造方法的newInstance方法创建对象，传入构造方法所需参数，如果有多个参数，用','连接即可
Object student = studentConstructor.newInstance("小明");
// 3.获取声明的字段，传入字段名

Field studentAgeField = studentClass.getDeclaredField("studentAge");

// 如果是私有的字段，需要调用下面这一行代码使其可使用，公有的字段则不需要下面这一行代码
// studentAgeField.setAccessible(true);

// 使用字段的set方法设置字段值，传入此对象以及参数值

studentAgeField.set(student,10);

// 4.获取声明的函数，传入所需参数的类名，如果有多个参数，用','连接即可

Method studentShowMethod = studentClass.getDeclaredMethod("show",String.class);

// 如果是私有的函数，需要调用下面这一行代码使其可使用，公有的函数则不需要下面这一行代码
studentShowMethod.setAccessible(true);
// 使用函数的invoke方法调用此函数，传入此对象以及函数所需参数，如果有多个参数，用','连接即可。函数会返回一个Object对象，使用强制类型转换转成实际类型即可

Object result = studentShowMethod.invoke(student,"message");

System.out.println("result: " + result);
```



师傅一缕 我感觉终于终于懂了！👾👾👾👾👾



### 案例

利用java的反射去执行命令。

java中可以用`Runtime.getRuntime().exec("calc");`

用反射实现

```java
        // 获取Class对象
        //Class对象 里保存是Runtime类的信息   java里每一个类都有一个Class对象
        Class runtimeClass1 = Class.forName("java.lang.Runtime");

//        Object m = runtimeClass1.newInstance(); // Runtime类是构造函数是私有的 无法直接new对象

        Method method = runtimeClass1.getMethod("exec", String.class);//调用Runtime类 exec方法
        Method getRuntime = runtimeClass1.getMethod("getRuntime");//调用Runtime类 getRuntime方法
        Object run = getRuntime.invoke(runtimeClass1);//通过invoke调用对象 的方法 这里通过invoke调用Class对象 里的getRuntime方法 new了个Runtime对象
        method.invoke(run,"calc"); // 最后通过invoke调用Runtime对象 里的exec方法
```



还可以`setAccessible(true)`来突破private的限制，



```java
        Class<?> run = Class.forName("java.lang.Runtime");//CLASS对象

        Constructor<?> c = run.getDeclaredConstructor();//获取Runtime类构造方法

        c.setAccessible(true);//可以获取私有构造方法


        Object o = c.newInstance();//new一个对象

        Method method = run.getMethod("exec", String.class);//获取Runtime类的exec方法

        method.invoke(o,"calc");// invoke调用
```

![image-20211116160747307](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116160747307.png)



![image-20211116164038020](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116164038020.png)



![image-20211116164132239](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116164132239.png)



![image-20211116164225005](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116164225005.png)



![image-20211116164341488](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116164341488.png)



![image-20211116164702849](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\image-20211116164702849.png)



流程：

![](D:\oneDrive云存储\OneDrive\桌面\java代码审计学起\我的学习项目\this_is_my_study\java代码审计学起\java代码审计6.assets\60f40319e401fd4fe05480ac.png)

后面就到Process

`就 Process start --》ProcessImpl.start -》  return new ProcessImpl --》  RCE`



### 获取Class对象的几种方式

```java
        //Class<?> run = ClassLoader.getSystemClassLoader().loadClass("java.lang.Runtime");//CLASS对象
        //Class<?> run = Class.forName("java.lang.Runtime");
        // Class<Runtime> run = Runtime.class;
        // 还有 一种通过对象获取Class对象 a = new A(); a.getClass();
```



### 补充

注意，有⼀点很有趣，使⽤功能”.class”来创建Class对象的引⽤时，不会⾃动初始化该Class对 象，使⽤`forName()`会⾃动初始化该Class对象



⾸先调⽤的是 static {} ，其次是 {} ，最后是构造函数。 其中， static {} 就是在“类初始化”的时候调⽤的，⽽ {} 中的代码会放在构造函数的 super() 后⾯， 但在当前构造函数内容的前⾯。

如果有任意加载类的接口！

我们可以用这个特性让他执行我们恶意类中static 代码！





## 序列化

Java序列化对象因其可以方便的将对象转换成字节数组，又可以方便快速的将字节数组反序列化成Java对象而被非常频繁的被用于`Socket`传输。 在`RMI(Java远程方法调用-Java Remote Method Invocation)`和`JMX(Java管理扩展-Java Management Extensions)`服务中对象**反序列化机制被强制性使用**。在`Http`请求中也时常会被用到反序列化机制，如：直接接收序列化请求的后端服务、使用Base编码序列化字节字符串的方式传递等。



在`java`中，反序列化对象是通过继承`Serializable`接口来实现的，只要一个类实现了`java.io.Serializable`接口，那么它就可以被序列化。





### 魔术方法

在php序列化的过程中，会判断是否有`__sleep()`魔术方法，如果存在这个魔术方法，会先执行这个方法，反序列`unserialize()`的时候先执行的是`__wakeup()`魔术方法。



java同理，`readObject()`=`__wakeup()`，还有一个`writeObject()`=`__sleep()`，这两个方法默认是可以不写的，我在上面的代码中重写了`readObject()`方法，通过自定义的`writeObject()`和`readObject()`方法可以允许用户控制序列化的过程，比如可以在序列化的过程中动态改变序列化的数值。









