
- [思考](#思考)
- [解决](#解决)
- [类比](#类比)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)
- [实现](#实现)
- [优缺点](#优缺点)


# 思考
有两类对象：产品和盒子 。 一个盒子中可以包含多个产品或者几个较小的盒子 。 这些小盒子中同样可以包含一些产品或更小的盒子，以此类推.

希望在这些类的基础上开发一个定购系统。 订单中可以包含无包装的简单产品， 也可以包含装满产品的盒子…… 以及其他盒子。 此时你会如何计算每张订单的总价格呢？

可以尝试直接计算：打开所有盒子，找到每件产品，然后计算总价。 这在真实世界中或许可行，但在程序中， 你并不能简单地使用循环语句来完成该工作。你必须事先知道所有产品和盒子的类别，所有盒子的嵌套层数以及其他繁杂的细节信息。 因此，直接计算极不方便， 甚至完全不可行。


# 解决
组合模式建议使用一个通用接口来与`产品`和`盒子`进行交互， 并且在该接口中声明一个计算总价的方法

方法该如何设计呢？ 对于一个产品， 该方法直接返回其价格； 对于一个盒子， 该方法遍历盒子中的所有项目， 询问每个项目的价格， 然后返回该盒子的总价格。 如果其中某个项目是小一号的盒子， 那么当前盒子也会遍历其中的所有项目， 以此类推， 直到计算出所有内部组成部分的价格。 你甚至可以在盒子的最终价格中增加额外费用， 作为该盒子的包装费用.

该方式的最大优点在于你无需了解构成树状结构的对象的具体类。 你也无需了解对象是简单的产品还是复杂的盒子。 你只需调用通用接口以相同的方式对其进行处理即可。 当你调用该方法后， 对象会将请求沿着树结构传递下去。

# 类比

![image](https://user-images.githubusercontent.com/83335903/224491044-ed2d3f26-a5fb-4380-a207-64ecd76f3775.png)

大部分国家的军队都采用层次结构管理。 每支部队包括几个师， 师由旅构成， 旅由团构成， 团可以继续划分为排。 最后， 每个排由一小队实实在在的士兵组成。 军事命令由最高层下达， 通过每个层级传递， 直到每位士兵都知道自己应该服从的命令。

# 结构

![image](https://user-images.githubusercontent.com/83335903/224491061-20eef4ca-66e9-4bf6-8660-03003eab89c8.png)

1. 组件 （Component） 接口描述了树中简单项目和复杂项目所共有的操作
2. 叶节点 （Leaf） 是树的基本结构， 一般情况下， 叶节点最终会完成大部分的实际工作， 因为它们无法将工作指派给其他部分。
3. 组合 （Composite)又名 “容器 （Container）”——是包含叶节点或其他容器等子项目的单位。 容器不知道其子项目所属的具体类， 它只通过通用的组件接口与其子项目交互.容器接收到请求后会将工作分配给自己的子项目， 处理中间结果， 然后将最终结果返回给客户端。

4. 客户端 （Client） 通过组件接口与所有项目交互。 因此， 客户端能以相同方式与树状结构中的简单或复杂项目交互

# java伪代码
大学——>学院——>专业的展示:
定义抽象组件（Component）角色
```
public abstract class Component {

    private String name;
    private String des;

    public Component(String name, String des) {
        this.name = name;
        this.des = des;
    }

    //增加
    protected void add(Component component){
        //抛出不支持操作的异常
        throw new UnsupportedOperationException();
    }

    //移除
    protected void remove(Component component){
        //抛出不支持操作的异常
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    //抽象的打印方法
    protected abstract void print();

}
```
定义树枝组件（Composite）：University 大学
```
public class University extends Component{

    //List存放的是学院的信息
    List<Component> components =  new ArrayList<>();

    public University(String name, String des) {
        super(name, des);
    }

    @Override
    protected void add(Component component) {
        components.add(component);
    }

    @Override
    protected void remove(Component component) {
        components.remove(component);
    }


    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDes() {
        return super.getDes();
    }

    //打印University包含的学院的名字
    @Override
    protected void print() {
        System.out.println("===============名称："+getName()+"描述："+getDes()+"===============");
        for (Component coms : components){
            coms.print();
        }
    }
}
```
定义树枝组件（Composite）：College 学院
```
public class College extends Component{

    //List存放的是专业的信息
    List<Component> components =  new ArrayList<>();

    public College(String name, String des) {
        super(name, des);
    }

    //实际业务中，University和College重写的add方法和remove方法可能不相同
    @Override
    protected void add(Component component) {
        components.add(component);
    }

    @Override
    protected void remove(Component component) {
        components.remove(component);
    }


    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDes() {
        return super.getDes();
    }

    //打印College包含的专业的名字
    @Override
    protected void print() {
        System.out.println("===============名称："+getName()+"描述："+getDes()+"===============");
        for (Component coms : components){
            coms.print();
        }
    }
}
```
定义叶件（Leaf）：Department 专业
```
public class Department extends Component{

    public Department(String name, String des) {
        super(name, des);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getDes() {
        return super.getDes();
    }

    @Override
    protected void print() {
        System.out.println("名称："+getName()+"描述："+getDes());
    }
}
```
编写测试类进行测试：
```
public class Client {

    public static void main(String[] args) {

        //创建大学
        Component university = new University("清华大学", "非常好的大学");

        //创建学院
        Component college1 = new College("信息工程学院", "信息工程学院好多专业");
        Component college2 = new College("建设工程学院", "建设工程学院好多专业");

        //将学院添加到学校中
        university.add(college1);
        university.add(college2);

        //创建专业，并把专业添加到学院中
        college1.add(new Department("信息安全","都来当Hacker"));
        college1.add(new Department("计算机科学与技术","计算机科学与技术好专业"));
        college1.add(new Department("网络工程","网络工程好专业"));
        college2.add(new Department("土木","土木牛马"));
        college2.add(new Department("测绘","测绘好专业"));


        university.print();
    }
}
```

![image](https://user-images.githubusercontent.com/83335903/224491618-3cb713a3-890f-4e00-8f6d-143788fe9966.png)


# 应用场景
1. 如果你需要实现树状对象结构， 可以使用组合模式
2. 如果你希望客户端代码以相同方式处理简单和复杂元素， 可以使用该模式

# 实现
1. 确保应用的核心模型能够以树状结构表示。 尝试将其分解为简单元素和容器。 记住， 容器必须能够同时包含简单元素和其他容器。
2. 声明组件接口及其一系列方法
3. 创建一个叶节点类表示简单元素。 程序中可以有多个不同的叶节点类
4. 创建一个容器类表示复杂元素。 在该类中， 创建一个数组成员变量来存储对于其子元素的引用。 该数组必须能够同时保存叶节点和容器， 因此请确保将其声明为组合接口类型
5. 最后， 在容器中定义添加和删除子元素的方法。

# 优缺点
1. 你可以利用多态和递归机制更方便地使用复杂树结构
2. 开闭原则。 无需更改现有代码， 你就可以在应用中添加新元素， 使其成为对象树的一部分。
3. 对于功能差异较大的类， 提供公共接口或许会有困难。 在特定情况下， 你需要过度一般化组件接口， 使其变得令人难以理解
