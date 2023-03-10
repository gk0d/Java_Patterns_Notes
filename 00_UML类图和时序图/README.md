

- [什么是UML?](#什么是uml)
- [UML元素](#uml元素)
  - [具体类](#具体类)
  - [抽象类](#抽象类)
  - [接口](#接口)
  - [包](#包)
- [在类图中表示关系](#在类图中表示关系)
  - [实现关系](#实现关系)
  - [泛化关系](#泛化关系)
  - [关联关系](#关联关系)
  - [依赖关系](#依赖关系)
  - [聚合关系](#聚合关系)
  - [组合关系](#组合关系)





# 什么是UML?
UML 全称是 Unified Modeling Language（统一建模语言），它以图形的方式来描述软件的概念。UML 的目标是通过一定结构的表达，来解决现实世界到软件世界的沟通问题。
# UML元素
## 具体类
具体类在类图中用矩形框表示，矩形框分为三层：第一层是`类名字`。第二层是`类的成员变量`；第三层是`类的方法`。成员变量以及方法前的访问修饰符用符号来表示：

![image](https://user-images.githubusercontent.com/83335903/224291125-5e53c849-d7df-4de3-8891-13c2aa75f59e.png)

- “+”表示 public；
- “-”表示 private；
- “#”表示 protected；
- 不带符号表示 default

## 抽象类
抽象类在UML类图中同样用矩形框表示，但是抽象类的类名以及抽象方法的名字都用`斜体字`表示
![image](https://user-images.githubusercontent.com/83335903/224291349-2145fc96-c238-48ef-b30b-51701a95dc4c.png)


## 接口
也是用矩形框表示，接口在类图中的第一层顶端用构造型 `<<interface>>`表示，下面是接口的名字，第二层是方法
![image](https://user-images.githubusercontent.com/83335903/224291613-b9949cbc-10b2-4f13-983c-5001ae6e43c6.png)

## 包
类和接口一般都出现在包中，UML类图中包的表示形式如图所示

![image](https://user-images.githubusercontent.com/83335903/224294127-693e7e9b-1f94-4671-90ea-db98f7437317.png)
# 在类图中表示关系
类和类、类和接口、接口和接口之间存在一定关系，UML类图通过连线来表示。关系共有六种类型，分别是实现关系、泛化关系、关联关系、依赖关系、聚合关系、组合关系

![image](https://user-images.githubusercontent.com/83335903/224294416-9d299418-47da-459c-a4f2-c92e811108d4.png)
## 实现关系
接口或抽象类与其实现类之间的关系，用空心三角和虚线组成的箭头来表示，从实现类指向接口

![image](https://user-images.githubusercontent.com/83335903/224331290-325f8b74-16dc-468f-96c1-8c5bacbd0562.png)

```
public interface ToyAction {
	void toyMoved();
}
```
```
public class Doll implements ToyAction{
	public Body body;
	public Cloth cloth;

	@Override
	public void toyMoved() {

	}
}
```



## 泛化关系
泛化关系（Generalization）即 Java 中的继承关系，是类与类或者接口与接口之间最常见的关系，用空心三角和实线组成的箭头表示，从子类指向父类。

![image](https://user-images.githubusercontent.com/83335903/224324754-e6cf1add-17ff-46bf-81f1-b38ca8e4388a.png)

两个子类 `Fish` 和 `Cat` 分别继承自 `Animal`
```
public class Animal {
	public String name;
	protected boolean isPet;
	private String state;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
```
```
public class Fish extends Animal {
	public String fishType;

	public void swim() {

	}
}
```
```
public class Cat extends Animal{
	protected boolean hasFeet;

	public void playToy(Doll doll){

	}
}
```

## 关联关系
双方的关系一般是平等的，分为单向关联、双向关联等，在代码层面，就是类 B 作为类 A 的属性，也可能是类 A 引用了一个类型为 B 的全局变量。如 Person 类，他拥有一个宠物猫，他们之间是关联关系。

![image](https://user-images.githubusercontent.com/83335903/224332093-ce3b8e58-e436-4f55-babc-71ff40ad7400.png)

```
public class Person {
	public Cat pet;
	public Head head;
}

```
1. 单向关联:带箭头的实线连接，箭头指向被引用或被包含的类,上面是一个单向关联关系
2. 双向关联:带双箭头的实线或者不带箭头的实线连接，双方各自持有对方类型的成员变量。例如 Customer 类中维护一个 Product[] 数组，表示一个顾客购买了哪些商品；在 Product 类中维护一个 Customer 类型的成员变量表示这个产品被哪个顾客所购买。

![image](https://user-images.githubusercontent.com/83335903/224332560-c755b584-264f-40b2-91ea-d4b351866202.png)

3. 自关联：可能会存在一些类的属性对象类型为该类本身，例如二叉树中的 TreeNode 定义

![image](https://user-images.githubusercontent.com/83335903/224332729-52cdb514-7f53-452b-8213-d58d7f9b7fa7.png)

## 依赖关系
类 A 使用到了另一个类 B，在代码层面，就是类 B 作为参数被类 A 在某个 method 方法中使用。如 Cat 类的 playToy 方法的参数就引用了 Doll 类，因此他们是依赖关系。用一个带虚线的箭头表示，由使用方指向被使用方

![image](https://user-images.githubusercontent.com/83335903/224332909-5e5ff68d-16ba-4484-9f6a-af52f90b22e8.png)



## 聚合关系
强调的整体和部分的关系，其中部分可以脱离整体而存在，他们可以具有各自的生命周期。
如 Doll 类由 Body 和 Cloth 组成，即使失去了 Cloth，Doll 也可以正常存在
用空心菱形加实线箭头表示，空心菱形在整体一方，箭头指向部分一方

![image](https://user-images.githubusercontent.com/83335903/224333145-663fa93d-086e-4129-aaf9-d3c4cdd42d3d.png)


## 组合关系

组合关系也是强调整体和部分的关系，不同的是部分不能脱离整体而存在，整体的生命周期结束也就意味着部分的生命周期结束，如人和大脑
实心菱形加实线箭头表示，实心菱形在整体一方，箭头指向部分一方

![image](https://user-images.githubusercontent.com/83335903/224333301-6cd3bdec-9940-467b-8db6-65e92f5915df.png)






