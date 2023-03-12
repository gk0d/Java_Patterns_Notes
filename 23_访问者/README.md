
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)

- [java伪代码](#java伪代码)
  - [第一步：构建Element](#第一步构建element)
  - [第二步：构建ObjectStructure](#第二步构建objectstructure)
  - [第三步：构建Visitor](#第三步构建visitor)
  - [客户端使用](#客户端使用)
- [应用场景](#应用场景)



# 思考
假如你的团队开发了一款能够使用巨型图像中地理信息的应用程序。 图像中的每个节点既能代表复杂实体 （例如一座城市）， 也能代表更精细的对象 （例如工业区和旅游景点等）。 如果节点代表的真实对象之间存在公路， 那么这些节点就会相互连接。 在程序内部， 每个节点的类型都由其所属的类来表示， 每个特定的节点则是一个对象。

![image](https://user-images.githubusercontent.com/83335903/224537362-515d6448-1871-4e5b-80d9-51354ad5e3ef.png)

一段时间后， 你接到了实现将图像导出到 XML 文件中的任务。 这些工作最初看上去非常简单。 你计划为每个节点类添加导出函数， 然后递归执行图像中每个节点的导出函数。 解决方案简单且优雅： 使用多态机制可以让导出方法的调用代码不会和具体的节点类相耦合。

但你不太走运， 系统架构师拒绝批准对已有节点类进行修改。 他认为这些代码已经是产品了， 不想冒险对其进行修改， 因为修改可能会引入潜在的缺陷。

![image](https://user-images.githubusercontent.com/83335903/224537394-6e83bf10-43f3-4b8c-a512-6e46a5b11d11.png)

此外， 他还质疑在节点类中包含导出 XML 文件的代码是否有意义。 这些类的主要工作是处理地理数据。 导出 XML 文件的代码放在这里并不合适。

还有另一个原因， 那就是在此项任务完成后， 营销部门很有可能会要求程序提供导出其他类型文件的功能， 或者提出其他奇怪的要求。 这样你很可能会被迫再次修改这些重要但脆弱的类。

# 解决
访问者模式建议将新行为放入一个名为`访问者`的独立类中， 而不是试图将其整合到已有类中。 现在， 需要执行操作的原始对象将作为参数被传递给访问者中的方法， 让方法能访问对象所包含的一切必要数据。

如果现在该操作能在不同类的对象上执行会怎么样呢？ 比如在我们的示例中， 各节点类导出 XML 文件的实际实现很可能会稍有不同。 因此， 访问者类可以定义一组 （而不是一个） 方法， 且每个方法可接收不同类型的参数， 如下所示：
```
class ExportVisitor implements Visitor is
    method doForCity(City c) { …… }
    method doForIndustry(Industry f) { …… }
    method doForSightSeeing(SightSeeing ss) { …… }
    // ……
```

# 类比真实世界

![image](https://user-images.githubusercontent.com/83335903/224537430-ffb0db94-e680-4436-a725-9e05c07765f7.png)

假如有这样一位非常希望赢得新客户的资深保险代理人。 他可以拜访街区中的每栋楼， 尝试向每个路人推销保险。 所以， 根据大楼内组织类型的不同， 他可以提供专门的保单：

如果建筑是居民楼， 他会推销医疗保险。
如果建筑是银行， 他会推销失窃保险。
如果建筑是咖啡厅， 他会推销火灾和洪水保险。

# 结构

![image](https://user-images.githubusercontent.com/83335903/224537457-fd36167d-00ed-49e6-82c1-dccfad3b4f75.png)

1. 访问者 （Visitor） 接口声明了一系列以对象结构的具体元素为参数的访问者方法。 如果编程语言支持重载， 这些方法的名称可以是相同的， 但是其参数一定是不同的。

2. 具体访问者 （Concrete Visitor） 会为不同的具体元素类实现相同行为的几个不同版本。

3. 元素 （Element） 接口声明了一个方法来 “接收” 访问者。 该方法必须有一个参数被声明为访问者接口类型。

4. 具体元素 （Concrete Element） 必须实现接收方法。 该方法的目的是根据当前元素类将其调用重定向到相应访问者的方法。 请注意， 即使元素基类实现了该方法， 所有子类都必须对其进行重写并调用访问者对象中的合适方法。

5. 客户端 （Client） 通常会作为集合或其他复杂对象 （例如一个组合树） 的代表。 客户端通常不知晓所有的具体元素类， 因为它们会通过抽象接口与集合中的对象进行交互。

# java伪代码
王二狗刚参加工作误入了一个大忽悠公司，公司老板不舍得花钱就给公司招了3个人，一个Hr，一个程序员，一个测试，但关键是老板啥都想做，一会社交，一会短视频。二狗多次提出说人太少，申请加几个人，至少加个保洁阿姨啊。每到此时老板就画大饼：你现在刚毕业正是要奋斗的时候，此时不奋斗什么时候奋斗？过两年公司上市了，你作为元老就财富自由拉...balabala

这个场景就很适合使用访问者模式：

大忽悠公司结构很稳定，老板舍不得花钱招人，总共就那么3个人，还是3种角色，即只有3个元素。 大忽悠公司老板想法多，这就要求这3个人承担各种新技能，即不断的给元素增加新的算法。

## 第一步：构建Element
先构建元素,只有一个accept方法，它需要一个访问者接口类型的参数
```
public interface CorporateSlave {
    void accept(CorporateSlaveVisitor visitor);
}
```
构建3个实现类:
1. 程序员：
```
public class Programmer implements CorporateSlave {
    private String name;

    public Programmer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(CorporateSlaveVisitor visitor) {
        visitor.visit(this);
    }
}
```
2. 测试：
```
public class Tester implements CorporateSlave {

    private String name;

    public Tester(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(CorporateSlaveVisitor visitor) {
        visitor.visit(this);
    }
}
```
3. HR:
同上，省略
重点在element类里面将自己传递给visitor的visit()方法
```
@Override
  public void accept(CorporateSlaveVisitor visitor) {
      visitor.visit(this);
  }
 ```

## 第二步：构建ObjectStructure
BigHuYouCompany 类里面需要包含相对稳定的元素（大忽悠老板就招这3个人，再也不肯招人），而且要求可以对这些元素迭代访问。此处我们以集合存储3位员工
```
public class BigHuYouCompany {
    private List<CorporateSlave> employee= new ArrayList<>();

    public BigHuYouCompany() {
        employee.add(new Programmer("王二狗"));
        employee.add(new HumanSource("上官无需"));
        employee.add(new Tester("牛翠花"));
    }

    public void startProject(CorporateSlaveVisitor visitor){
        for (CorporateSlave slave : employee) {
            slave.accept(visitor);
        }
    }
}
```


## 第三步：构建Visitor
Visitor 接口里面一般会存在与各元素对应的visit方法，例如此例我们有3个角色，所以这里就有3个方法:
```
public interface CorporateSlaveVisitor {
    void visit(Programmer programmer);

    void visit(HumanSource humanSource);

    void visit(Tester tester);
}
```
Visitor实现类

因为老板觉得社交是人类永恒的需求，所以开始想做社交App，他觉得他能成为微信第二。

这就相当于要为每一个元素定义一套新的算法，让程序员仿照微信开发设计app，让测试完成即时通信的测试，让人力发软文。

```
public class SocialApp implements CorporateSlaveVisitor {
    @Override
    public void visit(Programmer programmer) {
        System.out.println(String.format("%s: 给你一个月，先仿照微信搞个类似的APP出来,要能语音能发红包,将来公司上市了少不了你的，好好干...",programmer.getName()));
    }

    @Override
    public void visit(HumanSource humanSource) {
        System.out.println(String.format("%s: 咱现在缺人，你暂时就充当了陪聊吧，在程序员开发APP期间，你去发发软文，积攒点粉丝...",humanSource.getName()));
    }

    @Override
    public void visit(Tester tester) {
        System.out.println(String.format("%s: 这是咱创业的第一炮，一定要打响，测试不能掉链子啊，不能让APP带伤上战场，以后给你多招点人，你就是领导了...",tester.getName()));
    }
}
```
过了一段时间，老板又觉的短视频很火，又要做短视频，这就要求给每一员工增加一套新的算法
```
public class LiveApp implements CorporateSlaveVisitor {
    @Override
    public void visit(Programmer programmer) {
        System.out.println(String.format("%s: 最近小视频很火啊，咱能不能抄袭下抖音，搞他一炮,将来公司上市了，你的身价至少也是几千万，甚至上亿...",programmer.getName()));
    }

    @Override
    public void visit(HumanSource humanSource) {
        System.out.println(String.format("%s: 咱公司就数你长得靓，哪天化化妆，把你的事业线适当露一露，要是火了你在北京买房都不是梦...",humanSource.getName()));
    }

    @Override
    public void visit(Tester tester) {
        System.out.println(String.format("%s: 你也开个账户，边测试边直播，两不耽误...",tester.getName()));
    }
}
```

## 客户端使用
```
public class VisitorClient {

    public void startProject(){
        BigHuYouCompany bigHuYou= new BigHuYouCompany();
        //可以很轻松的更换Visitor，但是要求BigHuYouCompany的结构稳定
        System.out.println("-----------------启动社交APP项目--------------------");
        bigHuYou.startProject(new SocialApp());
        System.out.println("-----------------启动短视频APP项目--------------------");
        bigHuYou.startProject(new LiveApp());
    }
}
```
输出：
```
-----------------启动社交APP项目--------------------
王二狗: 给你一个月，先仿照微信搞个类似的APP出来,要能语音能发红包,将来公司上市了少不了你的，好好干...
上官无需: 咱现在缺人，你暂时就充当了陪聊吧，在程序员开发APP期间，你去发发软文，积攒点粉丝...
牛翠花: 这是咱创业的第一炮，一定要打响，测试不能掉链子啊，不能让APP带伤上战场，以后给你多招点人，你就是领导了...
-----------------启动短视频APP项目--------------------
王二狗: 最近小视频很火啊，咱能不能抄袭下抖音，搞他一炮,将来公司上市了，你的身价至少也是几千万，甚至上亿...
上官无需: 咱公司就数你长得靓，哪天化化妆，把你的事业线适当露一露，要是火了你在北京买房都不是梦...
牛翠花: 你也开个账户，边测试边直播，两不耽误...
```
你看虽然大忽悠老板的需求变化这么快，但至始至终我们只是在增加新的Visitor实现类，而没有去修改任何一个Element类，这就很好的符合了开闭原则。

# 应用场景
1. 如果你需要对一个复杂对象结构 （例如对象树） 中的所有元素执行某些操作， 可使用访问者模式。
2.  可使用访问者模式来清理辅助行为的业务逻辑。
3.  当某个行为仅在类层次结构中的一些类中有意义， 而在其他类中没有意义时， 可使用该模式。


