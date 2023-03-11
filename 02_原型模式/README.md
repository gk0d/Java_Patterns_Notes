- [作用](#作用)
- [思考](#思考)
- [解决方案](#解决方案)
- [结构](#结构)
- [Java伪代码](#java伪代码)
- [Java案例](#java案例)
  - [浅克隆](#浅克隆)
  - [深度克隆](#深度克隆)
- [应用场景](#应用场景)
- [实现方式](#实现方式)




# 作用

**用于创建重复的对象，同时又能保证性能**。这种类型的设计模式属于创建型模式，它提供了一种创建对象的最佳方式。

原型模式我们也称为`克隆模式`，即一个某个对象为原型克隆出来一个一模一样的对象，该对象的属性和原型对象一模一样。而且对于原型对象没有任何影响。原型模式的克隆方式有两种：`浅克隆`和`深度克隆`



# 思考

1. 如果你有一个对象，并希望生成与其完全相同的一个复制品，该如何实现呢？ 

首先，新建一个属于相同类的对象。 然后，遍历原始对象的所有成员变量，并将成员变量值复制到新对象中。但有些对象可能拥有私有成员变量，它们在对象本身以外是不可见的，所以复制不可行。

2. 还有一个问题，必须知道对象所属的类才能创建复制品，所以代码必须依赖该类。
3. 另外一个问题： 有时你只知道对象所实现的接口， 而不知道其所属的具体类， 

# 解决方案

将克隆过程委派给`被克隆的实际对象`，该模式为所有支持克隆的对象声明了一个通用接口， 该接口让你能够克隆对象， 同时又无需将代码和对象所属类耦合。 通常情况下，这样的接口中仅包含一个 `克隆`方法。

所有的类对 `克隆`方法的实现都非常相似。 该方法会创建一个当前类的对象， 然后将原始对象所有的成员变量值复制到新建的类中。 你甚至可以复制私有成员变量， 因为绝大部分编程语言都允许对象访问其同类对象的私有成员变量。

# 结构

**基本结构：**


![image](https://user-images.githubusercontent.com/83335903/224468210-c628cede-1deb-4f7a-a505-5d802ad8f57d.png)



1. **原型** （Prototype） `接口`将对克隆方法进行声明。 在绝大多数情况下， 其中只会有一个名为 `clone`克隆的方法。
2. **具体原型** （Concrete Prototype） 类将实现克隆方法。 除了将原始对象的数据复制到克隆体中之外， 该方法有时还需处理克隆过程中的极端情况， 例如克隆关联对象和梳理递归依赖等等。
3. **客户端** （Client） 可以复制实现了原型接口的任何对象。





**原型注册表结构**

![image](https://user-images.githubusercontent.com/83335903/224468217-940219c5-de08-4f8d-8f65-82d66cbe1306.png)



**原型注册表** （Prototype Registry） 提供了一种访问常用原型的简单方法， 其中存储了一系列可供随时复制的预生成对象。 最简单的注册表原型是一个 `名称 → 原型`的哈希表。 但如果需要使用名称以外的条件进行搜索， 你可以创建更加完善的注册表版本。

# Java伪代码

![image](https://user-images.githubusercontent.com/83335903/224468272-c4b1dbe8-298f-4488-8108-5e8d88512cf8.png)

所有形状类都遵循同一个提供克隆方法的接口。 在复制自身成员变量值到结果对象前， 子类可调用其父类的克隆方法。
```
// 基础原型。
abstract class Shape 
    field X: int
    field Y: int
    field color: string

    // 常规构造函数。
    constructor Shape() 
        // ……

    // 原型构造函数。使用已有对象的数值来初始化一个新对象。
    constructor Shape(source: Shape) 
        this()
        this.X = source.X
        this.Y = source.Y
        this.color = source.color

    // clone（克隆）操作会返回一个形状子类。
    abstract method clone():Shape


// 具体原型。克隆方法会创建一个新对象并将其传递给构造函数。直到构造函数运
// 行完成前，它都拥有指向新克隆对象的引用。因此，任何人都无法访问未完全生
// 成的克隆对象。这可以保持克隆结果的一致。
class Rectangle extends Shape 
    field width: int
    field height: int

    constructor Rectangle(source: Rectangle) is
        // 需要调用父构造函数来复制父类中定义的私有成员变量。
        super(source)
        this.width = source.width
        this.height = source.height

    method clone():Shape is
        return new Rectangle(this)


class Circle extends Shape 
    field radius: int

    constructor Circle(source: Circle) 
        super(source)
        this.radius = source.radius

    method clone():Shape 
        return new Circle(this)


// 客户端代码中的某个位置。
class Application 
    field shapes: array of Shape

    constructor Application() 
        Circle circle = new Circle()
        circle.X = 10
        circle.Y = 10
        circle.radius = 20
        shapes.add(circle)

        Circle anotherCircle = circle.clone()
        shapes.add(anotherCircle)
        // 变量 `anotherCircle（另一个圆）`与 `circle（圆）`对象的内
        // 容完全一样。

        Rectangle rectangle = new Rectangle()
        rectangle.width = 10
        rectangle.height = 20
        shapes.add(rectangle)

    method businessLogic() 
        // 原型是很强大的东西，因为它能在不知晓对象类型的情况下生成一个与
        // 其完全相同的复制品。
        Array shapesCopy = new Array of Shapes.

        // 例如，我们不知晓形状数组中元素的具体类型，只知道它们都是形状。
        // 但在多态机制的帮助下，当我们在某个形状上调用 `clone（克隆）`
        // 方法时，程序会检查其所属的类并调用其中所定义的克隆方法。这样，
        // 我们将获得一个正确的复制品，而不是一组简单的形状对象。
        foreach (s in shapes) do
            shapesCopy.add(s.clone())

        // `shapesCopy（形状副本）`数组中包含 `shape（形状）`数组所有
        // 子元素的复制品。
```
# Java案例

## 浅克隆

创建一个新对象，新对象的属性和原来对象完全相同，对于非基本类型属性(如引用类型)，仍指向原有属性所指向的对象的内存地址，以`复制邮件`为例。

这里一个包下分别有3个类。



![image](https://user-images.githubusercontent.com/83335903/224468332-0818bd58-cbf8-4e9d-9fdb-b3ac1624be6a.png)


1. 首先将`Object`作为抽象原型类，在Object 中提供了克隆方法`clone()`,用于创建一个原型对象,其 clone()方法具体实现由JVM完成,用户在使用时无须关心

![image](https://user-images.githubusercontent.com/83335903/224468341-024f4c99-693c-49b8-8af6-e1957c2d036a.png)




2. 附件类Attachment

为了更好地说明浅克隆和深克隆的区别,在本实例中引入了附件类Attachment,邮件类Email与附件类是组合关联关系,在邮件类中定义一个附件类对象,作为其成员对象

```c
package prototype;
/**
 * @author gk0d
 * @create 2023-03-10-22:20
 */
public class Attachment {

    public void download(){
        System.out.println("下载附件");
    }
}
```

3. 具体原型类Email(邮件类)

Email类是具体原型类,也是Object类的子类。在Java语言中,只有实现了Cloneable接口的类才能够使用clone()方法来进行复制，因此Email类实现了Cloneable接口。在Email类中覆盖了Object的clone()方法,通过直接或者间接调用Object的clone()方法返回一个克隆的原型对象。在Email类中定义了一个成员对象attachment,其类型为Attachment

```c
package prototype;

/**
 * @author gk0d
 * @create 2023-03-10-22:20
 */
public class Email implements Cloneable {

    private Attachment attachment=null;

    public Email(){
        this.attachment=new Attachment();
    }

    @Override
    public Object clone(){
        Email clone =null;

        try {
            clone=(Email) super.clone();
        }catch (CloneNotSupportedException e){
            System.out.println("Clone failure!");
        }

        return clone;
    }

    public Attachment getAttachment(){
        return this.attachment;
    }

    public void display(){
        System.out.println("查看邮件");
    }

}
```

4. 客户端测试类Client

在Client 客户端测试类中,比较原型对象和复制对象是否一致﹐并比较其成员对象attachment的引用是否一致。

```c
package prototype;

/**
 * @author gk0d
 * @create 2023-03-10-22:20
 */
public class Client {
    public static void main(String[] args) {
        Email email,copyEmail;

        email=new Email();

        copyEmail= (Email) email.clone();

        System.out.println("email == copyEmail?");
        System.out.println(email == copyEmail);

        System.out.println("email.getAttachment() == copyEmail.getAttachment()?");
        System.out.println(email.getAttachment() == copyEmail.getAttachment());
    }
}
```

![image](https://user-images.githubusercontent.com/83335903/224468352-f29c051d-67a4-49bd-8f85-e906b1f283c1.png)


通过结果可以看出,表达式(email==copyEmail)结果为false,即通过复制得到的对象与原型对象的引用不一致,也就是说明在内存中存在两个完全不同的对象，一个是原型对象，一个是克隆生成的对象。
但是表达式(email.getAttachment( ) == copyEmail.getAttachment())结果为true,两个对象的成员对象是同一个,说明虽然对象本身复制了一份,但其成员对象在内存中没有复制,原型对象和克隆对象维持了对相同的成员对象的引用



## 深度克隆

使用深克隆实现邮件复制,即复制邮件的同时复制附件

![image](https://user-images.githubusercontent.com/83335903/224468373-353a4ff2-8133-44df-adb8-46579ea09e6b.png)



1. 附件类 Attachment

作为Email类的成员对象,在深克隆中,Attachment类型的对象也将被写入流中,因此Attachment类也需要实现Serializable接口。
```
package prototype2;

import java.io.Serializable;

/**
 * @author gk0d
 * @create 2023-03-10-22:20
 */
public class Attachment implements Serializable {

    public void download(){
        System.out.println("下载附件");
    }
}

```

2. 具体原型类Email(邮件类)

Email作为具体原型类,由于实现的是深克隆,无须使用Object的 clone()方法,因此无须实现Cloneable接口;可以通过序列化的方式实现深克隆(代码中粗体部分),由于要将Email类型的对象写入流中,因此Email类需要实现Serializable接口。
```
package prototype2;

import java.io.*;

/**
 * @author gk0d
 * @create 2023-03-10-22:18
 */
public class Email implements Serializable {

    private Attachment attachment=null;

    public Email(){
        this.attachment=new Attachment();
    }


    public Object deepClone() throws IOException,ClassNotFoundException, OptionalDataException{
        //将对象写入流中
        ByteArrayOutputStream bao=new ByteArrayOutputStream();
        ObjectOutputStream oos =new ObjectOutputStream(bao);
        oos.writeObject(this);

        //将对象从流中取出
        ByteArrayInputStream bis =new ByteArrayInputStream(bao.toByteArray());
        ObjectInputStream ois =new ObjectInputStream(bis);
        return (ois.readObject());
    }


    public Attachment getAttachment(){
        return this.attachment;
    }

    public void display(){
        System.out.println("查看邮件");
    }
}

```

3. 客户端测试类Client

在Client客户端测试类中,我们仍然比较深克隆后原型对象和拷贝对象是否一致,并比较其成员对象attachment的引用是否一致
```
package prototype2;

/**
 * @author gk0d
 * @create 2023-03-11-22:28
 */
public class Client {

    public static void main(String[] args) {
        Email email,copyEmail = null;
        email=new Email();

        try {
            copyEmail = (Email) email.deepClone();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("email==copyEmail?");
        System.out.println(email==copyEmail);

        System.out.println("email.getAttachment()==copyEmail.getAttachment()?");
        System.out.println(email.getAttachment()==copyEmail.getAttachment());
    }
}

```

# 应用场景

需要复制一些对象，同时又希望代码独立于这些对象所属的具体类，可以使用原型模式。

通常出现在代码需要处理第三方代码通过接口传递过来的对象时。 即使不考虑代码耦合的情况， 你的代码也不能依赖这些对象所属的具体类， 因为你不知道它们的具体信息。

原型模式为客户端代码提供一个通用接口， 客户端代码可通过这一接口与所有实现了克隆的对象进行交互， 它也使得客户端代码与其所克隆的对象具体类独立开来

# 实现方式

1. 创建原型接口， 并在其中声明 `克隆`方法。 如果你已有类层次结构， 则只需在其所有类中添加该方法即可。

2. 原型类必须另行定义一个以该类对象为参数的构造函数。 构造函数必须复制参数对象中的所有成员变量值到新建实体中。 如果你需要修改子类， 则必须调用父类构造函数， 让父类复制其私有成员变量值。

   如果编程语言不支持方法重载， 那么你可能需要定义一个特殊方法来复制对象数据。 在构造函数中进行此类处理比较方便， 因为它在调用 `new`运算符后会马上返回结果对象。

3. 克隆方法通常只有一行代码： 使用 `new`运算符调用原型版本的构造函数。 注意， 每个类都必须显式重写克隆方法并使用自身类名调用 `new`运算符。 否则， 克隆方法可能会生成父类的对象。

4. 你还可以创建一个中心化原型注册表， 用于存储常用原型。

   你可以新建一个工厂类来实现注册表， 或者在原型基类中添加一个获取原型的静态方法。 该方法必须能够根据客户端代码设定的条件进行搜索。 搜索条件可以是简单的字符串， 或者是一组复杂的搜索参数。 找到合适的原型后， 注册表应对原型进行克隆， 并将复制生成的对象返回给客户端。

   最后还要将对子类构造函数的直接调用替换为对原型注册表工厂方法的调用。
