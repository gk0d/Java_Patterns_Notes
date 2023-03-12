
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)



# 思考
开发一款分析公司文档的数据挖掘程序。 用户需要向程序输入各种格式 （PDF、 DOC 或 CSV） 的文档， 程序则会试图从这些文件中抽取有意义的数据， 并以统一的格式将其返回给用户。

该程序的首个版本仅支持 DOC 文件。 在接下来的一个版本中， 程序能够支持 CSV 文件。 一个月后， 你 “教会” 了程序从 PDF 文件中抽取数据

![image](https://user-images.githubusercontent.com/83335903/224536970-9a06f7ed-9a20-476b-90cf-1754a0a9e310.png)

一段时间后， 你发现这三个类中包含许多相似代码。 尽管这些类处理不同数据格式的代码完全不同， 但数据处理和分析的代码却几乎完全一样。 如果能在保持算法结构完整的情况下去除重复代码， 这难道不是一件很棒的事情吗？

还有另一个与使用这些类的客户端代码相关的问题： 客户端代码中包含许多条件语句， 以根据不同的处理对象类型选择合适的处理过程。 如果所有处理数据的类都拥有相同的接口或基类， 那么你就可以去除客户端代码中的条件语句， 转而使用多态机制来在处理对象上调用函数。

# 解决
模板方法模式建议将算法分解为一系列步骤， 然后将这些步骤改写为方法， 最后在 “模板方法” 中依次调用这些方法。 步骤可以是抽象的， 也可以有一些默认的实现。 为了能够使用算法， 客户端需要自行提供子类并实现所有的抽象步骤。 如有必要还需重写一些步骤 （但这一步中不包括模板方法自身）。

让我们考虑如何在数据挖掘应用中实现上述方案。 我们可为图中的三个解析算法创建一个基类， 该类将定义调用了一系列不同文档处理步骤的模板方法

![image](https://user-images.githubusercontent.com/83335903/224537004-81592ef7-15d3-4f00-a63a-065bfed6f5ba.png)

首先， 我们将所有步骤声明为`抽象`类型， 强制要求子类自行实现这些方法。 在我们的例子中， 子类中已有所有必要的实现， 因此我们只需调整这些方法的签名， 使之与超类的方法匹配即可。

现在， 让我们看看如何去除重复代码。 对于不同的数据格式， 打开和关闭文件以及抽取和解析数据的代码都不同， 因此无需修改这些方法。 但分析原始数据和生成报告等其他步骤的实现方式非常相似， 因此可将其提取到基类中， 以让子类共享这些代码。

正如你所看到的那样， 我们有两种类型的步骤：

- 抽象步骤必须由各个子类来实现
- 可选步骤已有一些默认实现， 但仍可在需要时进行重写

还有另一种名为`钩子`的步骤。 `钩子`是内容为空的可选步骤。 即使不重写钩子， 模板方法也能工作。 钩子通常放置在算法重要步骤的前后， 为子类提供额外的算法扩展点。

# 类比真实世界

![image](https://user-images.githubusercontent.com/83335903/224537028-41e4e16b-6e25-4d81-a03f-25f4edb00ab7.png)

模板方法可用于建造大量房屋。 标准房屋建造方案中可提供几个扩展点， 允许潜在房屋业主调整成品房屋的部分细节。

每个建造步骤 （例如打地基、 建造框架、 建造墙壁和安装水电管线等） 都能进行微调， 这使得成品房屋会略有不同。


# 结构

![image](https://user-images.githubusercontent.com/83335903/224537045-abee243d-c082-4461-b0c1-0be788c5749e.png)

1. 抽象类 （AbstractClass） 会声明作为算法步骤的方法， 以及依次调用它们的实际模板方法。 算法步骤可以被声明为 抽象类型， 也可以提供一些默认实现。

2.具体类 （ConcreteClass） 可以重写所有步骤， 但不能重写模板方法自身

# java伪代码
制作豆浆的程序简单点来说就是选材—>添加配料—>浸泡—>放到豆浆机打碎，通过添加不同的配料，可以制作出不同口味的豆浆，但是选材、浸泡和放到豆浆机打碎这几个步骤对于制作每种口味的豆浆都是一样的
1. 创建抽象类：

```
package templatemethod.pattern;

//豆浆类，抽象类
public abstract class SoyaMilk {
    //这是模板方法，用final修饰，不允许子类覆盖。模板方法定义了制作豆浆的程序
    final void  prepareRecipe(){
        selectMaterial();
        addCondiments();
        soak();
        beat();
    }

    //选材方法，选择黄豆
    void selectMaterial(){
         System.out.println("第一步、选择好了新鲜黄豆");
    }

    //可以添加不同的配料，在这里设置为抽象方法，子类必须实现
    abstract void addCondiments();

    //浸泡
    void soak(){
        System.out.println("第三步、黄豆和配料开始浸泡，大概需要5个小时");
    }

    //放到豆浆机打碎
    void beat(){
        System.out.println("第四步、黄豆的配料放到豆浆机打碎");
    }

}
```
2. 创建红枣豆浆
```
package templatemethod.pattern;

//红枣豆浆
public class ReddatesSoyaMilk extends SoyaMilk{
    //实现父类的添加配料方法
    @Override
    void addCondiments() {
        System.out.println("第二步、添加红枣配料");

    }

}
```
3. 创建核桃豆浆
```
package templatemethod.pattern;

//核桃豆浆
public class NutSoyaMilk extends SoyaMilk{
    //实现父类的添加配料方法
    @Override
    void addCondiments() {
        System.out.println("第二步、添加核桃配料");      
    }

}
```
4. 测试制作豆浆
```
package templatemethod.pattern;

public class SoyaMilkTest {
    public static void main(String[] args){
        //制作红枣豆浆
        System.out.println();
        System.out.println("-----制作红枣豆浆步骤-------");
        SoyaMilk reddatesSoyaMilk = new ReddatesSoyaMilk();
        reddatesSoyaMilk.prepareRecipe();

        //制作核桃豆浆
        System.out.println();
        System.out.println("-----制作核桃豆浆步骤-------");
        SoyaMilk nutSoyaMilk = new NutSoyaMilk();
        nutSoyaMilk.prepareRecipe();

    }
}
```

![image](https://user-images.githubusercontent.com/83335903/224537181-0e554a19-5f02-4e10-b623-bf58e12c5442.png)



# 应用场景
1. 当你只希望客户端扩展某个特定算法步骤， 而不是整个算法或其结构时， 可使用模板方法模式
2. 当多个类的算法除一些细微不同之外几乎完全一样时， 你可使用该模式。 但其后果就是， 只要算法发生变化， 你就可能需要修改所有的类。

