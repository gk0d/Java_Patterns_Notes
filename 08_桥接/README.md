- [作用](#作用)
- [思考](#思考)
- [解决](#解决)

- [结构](#结构)
- [java例子](#java例子)
- [应用场景](#应用场景)
- [实现](#实现)


# 作用
可将一个大类或一系列紧密相关的类拆分为抽象和实现两个独立的层次结构， 从而能在开发时分别使用

如果要绘制矩形、圆形、椭圆、正方形，我们至少需要4个形状类，但是如果绘制的图形需要具有不同的颜色，如红色、绿色、蓝色等，此时至少有如下两种设计方案：

1. 第一种设计方案是为每一种形状都提供一套各种颜色的版本。
2. 第二种设计方案是根据实际需要对形状和颜色进行组合
对于有两个变化维度（即两个变化的原因）的系统，采用方案二来进行设计系统中类的个数更少，且系统扩展更为方便。设计方案二即是桥接模式的应用。桥接模式将继承关系转换为关联关系，从而降低了类与类之间的耦合，减少了代码编写量
# 思考
有一个几何 形状Shape类， 从它能扩展出两个子类： 圆形Circle和 方形Square 。 你希望对这样的类层次结构进行扩展以使其包含颜色， 所以你打算创建名为 红色Red和 蓝色Blue的形状子类。 但是， 由于你已有两个子类， 所以总共需要创建四个类才能覆盖所有组合， 例如 蓝色圆形BlueCircle和 红色方形RedSquare

![image](https://user-images.githubusercontent.com/83335903/224489652-4d32b322-7a44-4c51-8147-08ed4977db17.png)



# 解决
我们可以将颜色相关的代码抽取到拥有 红色和 蓝色两个子类的颜色类中， 然后在 形状类中添加一个指向某一颜色对象的引用成员变量。 现在， 形状类可以将所有与颜色相关的工作委派给连入的颜色对象。 这样的引用就成为了 形状和 颜色之间的桥梁。 此后， 新增颜色将不再需要修改形状的类层次， 反之亦然

![image](https://user-images.githubusercontent.com/83335903/224489686-1a270842-1de2-4989-95bc-57a00918e7ad.png)




# 结构

![image](https://user-images.githubusercontent.com/83335903/224489798-a6a61c6f-fd92-4712-aeb5-f508d7d8b569.png)

1. 抽象部分（Abstraction）提供高层控制逻辑，依赖于完成底层实际工作的实现对象
2. 实现部分 （Implementation） 为所有具体实现声明通用接口。 抽象部分仅能通过在这里声明的方法与实现对象交互
3. 具体实现 （Concrete Implementations） 中包括特定于平台的代码
4. 精确抽象 （Refined Abstraction） 提供控制逻辑的变体。 与其父类一样， 它们通过通用实现接口与不同的实现进行交互
5.通常情况下， 客户端 （Client） 仅关心如何与抽象部分合作。 但是， 客户端需要将抽象对象与一个实现对象连接起来



# java例子
开始星巴克只有中杯，原味和加糖这几种选择代码实现如下：
首先定义一个点咖啡接口，里面有一个下单方法，至于点哪种口味的咖啡，由其子类去决定
```
public interface ICoffee {
    void orderCoffee(int count);
}
```
原味咖啡类
```
public class CoffeeOriginal implements ICoffee {
    @Override
    public void orderCoffee(int count) {
        System.out.println(String.format("原味咖啡%d杯",count));
    }
}
```
加糖咖啡类
```
public class CoffeeWithSugar implements ICoffee {
    @Override
    public void orderCoffee(int count) {
        System.out.println(String.format("加糖咖啡%d杯",count));
    }
}
```
此时要加需求：容量分为大，中，小杯；代码如下：
```
//中杯加糖
public class CoffeeWithSugar implements ICoffee {
    @Override
    public void orderCoffee(int count) {
        System.out.println(String.format("中杯加糖咖啡%d杯",count));
    }
}
//大杯加糖
public class LargeCoffeeWithSugar implements ICoffee {
    @Override
    public void orderCoffee(int count) {
        System.out.println(String.format("大杯加糖咖啡%d杯",count));
    }
}
....
```
共需要3x2=6个类,万一后续又要出加奶，加蜂蜜等等口味，说不定还有迷你杯，女神杯等等规格的咖啡。类的数量直接爆炸。
桥接模式正合适这种场景。
可以将咖啡的容量作为抽象化Abstraction，而咖啡口味为实现化Implementor
1. 创建抽象化部分
```
//抽象化Abstraction
public abstract class Coffee {
    protected ICoffeeAdditives additives;
    public Coffee(ICoffeeAdditives additives){
        this.additives=additives;
    }
    public abstract void orderCoffee(int count);
}
```
我们可以看到，Coffee持有了ICoffeeAdditives 引用，ICoffeeAdditives 的实例是通过构造函数注入的，这个过程就是我们所说的桥接过程。我们通过这个引用就可以调用ICoffeeAdditives的方法，进而将Coffee的行为与ICoffeeAdditives的行为通过orderCoffee()方法而组合起来
下面是一个对抽象化修正的一个类,里面增加了一个品控的方法
```
//修正抽象化
public abstract class RefinedCoffee extends Coffee {
    public RefinedCoffee(ICoffeeAdditives additives) {
        super(additives);
    }
    public void checkQuality(){
        Random ran=new Random();
        System.out.println(String.format("%s 添加%s",additives.getClass().getSimpleName(),ran.nextBoolean()?"太多":"正常"));
    }
}
```
2. 创建实现化部分
```
public interface ICoffeeAdditives {
    void addSomething();
}
//加奶
public class Milk implements ICoffeeAdditives {
    @Override
    public void addSomething() {
        System.out.println("加奶");
    }
}
//加糖
public class Sugar implements ICoffeeAdditives {
    @Override
    public void addSomething() {
        System.out.println("加糖");
    }
}
```
3. 客户端调用
```
public static void main(String[] args) {
        //点两杯加奶的大杯咖啡
        RefinedCoffee largeWithMilk=new LargeCoffee(new Milk());
        largeWithMilk.orderCoffee(2);
        largeWithMilk.checkQuality();
    }
```
输出结果：
```
加奶
大杯咖啡2杯
Milk 添加太多
```

# 应用场景
1. 如果你想要拆分或重组一个具有多重功能的庞杂类 （例如能与多个数据库服务器进行交互的类）， 可以使用桥接模式
桥接模式可以将庞杂类拆分为几个类层次结构。 此后， 你可以修改任意一个类层次结构而不会影响到其他类层次结构。 这种方法可以简化代码的维护工作， 并将修改已有代码的风险降到最低。

2. 如果你希望在几个独立维度上扩展一个类， 可使用该模式

桥接建议将每个维度抽取为独立的类层次。 初始类将相关工作委派给属于对应类层次的对象， 无需自己完成所有工作

3. 如果你需要在运行时切换不同实现方法， 可使用桥接模式



# 实现
1. 明确类中独立的维度。 独立的概念可能是： 抽象/平台， 域/基础设施， 前端/后端或接口/实现。
2. 了解客户端的业务需求， 并在抽象基类中定义它们。
3. 确定在所有平台上都可执行的业务。 并在通用实现接口中声明抽象部分所需的业务。
4. 为你域内的所有平台创建实现类， 但需确保它们遵循实现部分的接口。
5. 在抽象类中添加指向实现类型的引用成员变量。 抽象部分会将大部分工作委派给该成员变量所指向的实现对象。
6. 如果你的高层逻辑有多个变体， 则可通过扩展抽象基类为每个变体创建一个精确抽象。
7. 客户端代码必须将实现对象传递给抽象部分的构造函数才能使其能够相互关联。 此后， 客户端只需与抽象对象进行交互， 无需和实现对象打交道


