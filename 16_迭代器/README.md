
- [思考](#思考)
- [解决](#解决)

- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)
- [实现](#实现)
- [优缺点](#优缺点)


# 思考
集合是编程中最常使用的数据类型之一。可以理解为一组对象的容器。

![image](https://user-images.githubusercontent.com/83335903/224531900-d85c0d04-5bd3-4f3e-b8ce-ce451601521c.png)


大部分集合使用简单列表存储元素。 但有些集合还会使用栈、 树、 图和其他复杂的数据结构

无论集合的构成方式如何， 它都必须提供访问元素的方式， 便于其他代码使用其中的元素。 

![image](https://user-images.githubusercontent.com/83335903/224531932-dbc0ea66-75bd-474c-9c7b-7419d16ad9b7.png)

如果不断向集合中添加`遍历算法`会模糊其 “高效存储数据” 的主要职责。 此外， 有些算法可能是根据特定应用订制的，将其加入泛型集合类中会显得非常奇怪。

另一方面， 使用多种集合的客户端代码可能并不关心存储数据的方式。 不过由于集合提供不同的元素访问方式， 你的代码将不得不与特定集合类进行耦合.


# 解决

迭代器模式的主要思想是将集合的遍历行为抽取为单独的迭代器对象

![image](https://user-images.githubusercontent.com/83335903/224532113-42ea7e1c-cda0-492f-a83d-1ae56bfadd31.png)

所有迭代器必须实现相同的接口。 这样一来， 只要有合适的迭代器， 客户端代码就能兼容任何类型的集合或遍历算法。 如果你需要采用特殊方式来遍历集合， 只需创建一个新的迭代器类即可， 无需对集合或客户端进行修改。


# 结构

![image](https://user-images.githubusercontent.com/83335903/224532155-1d478a59-6f0d-4fd1-9d10-a7a94c055479.png)

1. 迭代器 （Iterator） 接口声明了遍历集合所需的操作： 获取下一个元素、 获取当前位置和重新开始迭代等。

2. 具体迭代器 （Concrete Iterators） 实现遍历集合的一种特定算法。 迭代器对象必须跟踪自身遍历的进度。 这使得多个迭代器可以相互独立地遍历同一集合。

3. 集合 （Collection） 接口声明一个或多个方法来获取与集合兼容的迭代器。 请注意， 返回方法的类型必须被声明为迭代器接口， 因此具体集合可以返回各种不同种类的迭代器。

4. 具体集合 （Concrete Collections） 会在客户端请求迭代器时返回一个特定的具体迭代器类实体。 你可能会琢磨， 剩下的集合代码在什么地方呢？ 不用担心， 它也会在同一个类中。 只是这些细节对于实际模式来说并不重要， 所以我们将其省略了而已。

5. 客户端 （Client） 通过集合和迭代器的接口与两者进行交互。 这样一来客户端无需与具体类进行耦合， 允许同一客户端代码使用各种不同的集合和迭代器。客户端通常不会自行创建迭代器， 而是会从集合中获取。 但在特定情况下， 客户端可以直接创建一个迭代器 （例如当客户端需要自定义特殊迭代器时）。

# java伪代码
Java 和 .Net 编程环境中非常常用的设计模式。这种模式用于顺序访问集合对象的元素，不需要知道集合对象的底层表示。迭代器模式属于行为型模式。
使用迭代器打印名字为例，总共分三步：
1、创建接口:
```
public interface Iterator {

    public boolean hasNext();

    public Object next();
}
```
```
public interface Container {
    public Iterator getIterator();
}
```
2、创建实现了 Container 接口的实体类。该类有实现了 Iterator 接口的内部类 NameIterator。
```
 public class NameRepository implements Container {
 
     private String names[] = {"John", "jingbin", "youlookwhat", "lookthis"};
 
     @Override
     public Iterator getIterator() {
         return new NameIterator();
     }
 
     private class NameIterator implements Iterator {
 
         int index;
 
         @Override
         public boolean hasNext() {
             if (index < names.length) {
                 return true;
             }
             return false;
         }
 
         @Override
         public Object next() {
             if (hasNext()) {
                 return names[index++];
             }
             return null;
         }
     }
 
 }
```
3、使用 NameRepository 来获取迭代器，并打印名字。
```
 NameRepository nameRepository = new NameRepository();
 for (Iterator iterator = nameRepository.getIterator(); iterator.hasNext(); ) {
     String name = (String) iterator.next();
     Log.e("---", name);
     /*
      * /---: John
      * /---: jingbin
      * /---: youlookwhat
      * /---: lookthis
      */
 }
```

# 应用场景
1. 当集合背后为复杂的数据结构， 且你希望对客户端隐藏其复杂性时 （出于使用便利性或安全性的考虑）， 可以使用迭代器模式
2. 使用该模式可以减少程序中重复的遍历代码
3. 如果你希望代码能够遍历不同的甚至是无法预知的数据结构， 可以使用迭代器模式
