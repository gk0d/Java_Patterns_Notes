- [作用](#作用)
- [思考](#思考)
- [解决](#解决)
- [结构](#结构)
  - [对象适配器](#对象适配器)
  - [类适配器](#类适配器)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)
- [实现](#实现)
- [优缺点](#优缺点)

# 作用
适配器模式(Adapter Pattern) ：将一个接口转换成客户希望的另一个接口，适配器模式使接口不兼容的那些类可以一起工作，其别名为包装器(Wrapper)
# 思考
正在开发一款股票市场监测程序， 它会从不同来源下载 XML 格式的股票数据， 然后呈现出美观的图表
此时决定在程序中整合一个第三方智能分析函数库，但分析函数库只兼容 JSON 格式的数据

![image](https://user-images.githubusercontent.com/83335903/224488217-55ec90a3-d353-4701-85fa-42c8cc2807ff.png)



# 解决
创建一个`适配器`。 这是一个特殊的对象， 能够转换对象接口，使其能与其他对象进行交互.

适配器不仅可以转换不同格式的数据， 其还有助于采用不同接口的对象之间的合作。 它的运作方式如下：

1. 适配器实现与其中一个现有对象兼容的接口。
2. 现有对象可以使用该接口安全地调用适配器方法。
3. 适配器方法被调用后将以另一个对象兼容的格式和顺序将请求传递给该对象

有时你甚至可以创建一个双向适配器来实现双向转换调用。


![image](https://user-images.githubusercontent.com/83335903/224488332-15511827-f303-4d0c-b190-38e0b0898123.png)

为了解决数据格式不兼容的问题， 你可以为分析函数库中的每个类创建将 XML 转换为 JSON 格式的适配器， 然后让客户端仅通过这些适配器来与函数库进行交流。 当某个适配器被调用时， 它会将传入的 XML 数据转换为 JSON 结构， 并将其传递给被封装分析对象的相应方法

# 结构
## 对象适配器
适配器实现了其中一个对象的接口， 并对另一个对象进行封装。 所有流行的编程语言都可以实现适配器

![image](https://user-images.githubusercontent.com/83335903/224488512-1cdd9966-84f6-478f-ac68-5881226a77e4.png)

1. 客户端 （Client） 是包含当前程序业务逻辑的类。
2. 客户端接口 （Client Interface） 描述了其他类与客户端代码合作时必须遵循的协议
3. 服务 （Service） 中有一些功能类 （通常来自第三方或遗留系统）。 客户端与其接口不兼容， 因此无法直接调用其功能
4. 适配器 （Adapter） 是一个可以同时与客户端和服务交互的类： 它在实现客户端接口的同时封装了服务对象。 适配器接受客户端通过适配器接口发起的调用， 并将其转换为适用于被封装服务对象的调用
5. 客户端代码只需通过接口与适配器交互即可， 无需与具体的适配器类耦合。 因此， 你可以向程序中添加新类型的适配器而无需修改已有代码。 这在服务类的接口被更改或替换时很有用： 你无需修改客户端代码就可以创建新的适配器类。

## 类适配器
这一实现使用了继承机制： 适配器同时继承两个对象的接口

![image](https://user-images.githubusercontent.com/83335903/224488582-5cfd6783-f224-4bb9-b309-2363d903ac35.png)

类适配器不需要封装任何对象， 因为它同时继承了客户端和服务的行为。 适配功能在重写的方法中完成。 最后生成的适配器可替代已有的客户端类进行使用


# java伪代码
类适配器代码如下：
```
/**
 * 这是客户所期待的接口，目标可以是具体的或抽象类，也可以是接口
 */
public interface Target {
    void request();
}

/**
 * 需要适配的类，被访问和适配的现存组件库中的组件接口
 */
public class Adaptee {
    public void specificRequest(){
        System.out.println("适配者中的业务代码被调用！");
    }
}

/**
 * 类适配器类
 */
public class ClassAdapter extends Adaptee implements Target{
    @Override
    public void request() {
        specificRequest();
    }
}

//客户端代码
public class ClassAdapterTest
{
    public static void main(String[] args)
    {
        System.out.println("类适配器模式测试：");
        Target target = new ClassAdapter();
        target.request();
    }
}
```
运行结果如下：
```
类适配器模式测试：
适配者中的业务代码被调用！
```
对象适配器的代码如下：
```
/**
 * 对象适配器，通过在内部包装一个 Adaptee 对象，把源接口转换为目标接口
 */
public class ObjectAdapter implements Target {

    // 建立一个私有的 Adaptee 对象
    private Adaptee adaptee;

    public ObjectAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        // 把表面上调用 request() 方法变成实际调用 specificRequest() 
        adaptee.specificRequest();
    }
}

//客户端代码
public class ObjectAdapterTest
{
    public static void main(String[] args)
    {
        System.out.println("对象适配器模式测试：");
        // 对客户端来说，调用的就是 Target 的 request()
        Target target = new ObjectAdapter();
        target.request();
    }
}

```
运行结果如下：
```
类适配器模式测试：
适配者中的业务代码被调用！
```

# 应用场景
1. 希望使用某个类， 但是其接口与其他代码不兼容时， 可以使用适配器类
2. 需要复用这样一些类， 他们处于同一个继承体系， 并且他们又有了额外的一些共同的方法， 但是这些共同的方法不是所有在这一继承体系中的子类所具有的共性
# 实现
1. 确保至少有两个类的接口不兼容：  
    1. 一个无法修改 （通常是第三方、 遗留系统或者存在众多已有依赖的类） 的功能性`服务类`。
    2. 一个或多个使用服务类的`客户端类`
2. 声明客户端接口， 描述客户端如何与服务交互
3. 创建遵循客户端接口的适配器类。 所有方法暂时都为空
4. 在适配器类中添加一个成员变量用于保存对于服务对象的引用。 通常情况下会通过构造函数对该成员变量进行初始化， 但有时在调用其方法时将该变量传递给适配器会更方便
5. 依次实现适配器类客户端接口的所有方法。 适配器会将实际工作委派给服务对象， 自身只负责接口或数据格式的转换
6. 客户端必须通过客户端接口使用适配器。 这样一来， 你就可以在不影响客户端代码的情况下修改或扩展适配器

