# 作用

**单例模式**是一种创建型设计模式(`Singleton Pattern`)， 保证一个类只有一个实例， 并提供一个访问该实例的全局节点

# 思考

单例模式同时解决了两个问题， 所以违反了*`单一职责原则`*：

1. **保证一个类只有一个实例**,为什么要这样？

对于系统中的某些类来说，只有一个实例很重要，例如，一个系统中可以存在多个打印任务，但是只能有一个正在工作的任务；一个系统只能有一个窗口管理器或文件系统；一个系统只能有一个计时工具或ID（序号）生成器。

2. **为该实例提供一个全局访问节点**，

全局变量在使用上十分方便， 但同时也非常不安全， 因为任何代码都有可能覆盖掉那些变量的内容， 从而引发程序崩溃。

# 解决方案

所有单例的实现都包含以下两个相同的步骤：

- 将默认构造函数设为`私有`， 防止其他对象使用单例类的 `new`运算符。
- 新建一个静态构建方法作为构造函数。 该函数会 “偷偷” 调用私有构造函数来创建对象， 并将其保存在一个静态成员变量中。 此后所有对于该函数的调用都将返回这一缓存对象。

如果你的代码能够访问单例类， 那它就能调用单例类的静态方法。 无论何时调用该方法， 它总是会返回相同的对象。

# 结构
![image](https://user-images.githubusercontent.com/83335903/224267779-f512d824-3199-453f-be8c-03421afa720e.png)

可以看到，单例类(Singleton)声明了一个名为`getInstance`的静态方法,该方法调用`Singleton`方法来创建对象， 并将其保存在一个静态成员变量中,确保只有一个实例被创建。

# Java伪代码
在本例中， 数据库连接类即是一个单例。 该类不提供公有构造函数， 因此获取该对象的唯一方式是调用 获取实例方法。 该方法将缓存首次生成的对象， 并为所有后续调用返回该对象
```
// 数据库类会对`getInstance（获取实例）`方法进行定义以让客户端在程序各处都能访问相同的数据库连接实例。
class Database 
    // 保存单例实例的成员变量必须被声明为静态类型。
    private static field instance: Database

    // 单例的构造函数必须永远是私有类型，以防止使用`new`运算符直接调用构造方法。
    private constructor Database() 
        // 部分初始化代码（例如到数据库服务器的实际连接）。

    // 用于控制对单例实例的访问权限的静态方法。
    public static method getInstance() 
        if (Database.instance == null) then
            acquireThreadLock() and then
                // 确保在该线程等待解锁时，其他线程没有初始化该实例。
                if (Database.instance == null) then
                    Database.instance = new Database()
        return Database.instance

    // 最后，任何单例都必须定义一些可在其实例上执行的业务逻辑。
    public method query(sql) 
        // 比如应用的所有数据库查询请求都需要通过该方法进行。因此，你可以
        // 在这里添加限流或缓冲逻辑。
        // ……

class Application 
    method main() 
        Database foo = Database.getInstance()
        foo.query("SELECT ……")
        // ……
        Database bar = Database.getInstance()
        bar.query("SELECT ……")
        // 变量 `bar` 和 `foo` 中将包含同一个对象。

```

# 应用场景
1. 如果程序中的某个类对于所有客户端只有一个可用的实例， 可以使用单例模式。
2. 如果你需要更加严格地控制全局变量， 可以使用单例模式。

# 实现方法
1. 在类中添加一个私有静态成员变量用于保存单例实例。
2. 声明一个公有静态构建方法用于获取单例实例。
3. 在静态方法中实现"延迟初始化"。 该方法会在首次被调用时创建一个新对象， 并将其存储在静态成员变量中。 此后该方法每次被调用时都返回该实例。
4. 将类的构造函数设为私有。 类的静态方法仍能调用构造函数， 但是其他对象不能调用。
5. 检查客户端代码， 将对单例的构造函数的调用替换为对其静态构建方法的调用。
