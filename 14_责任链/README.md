
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)
- [实现](#实现)
- [优缺点](#优缺点)


# 思考
正在开发一个在线订购系统。你希望对系统访问进行限制:
1. 只允许认证用户创建订单。
2. 拥有管理权限的用户也拥有所有订单的完全访问权限

规划如下：接收到包含用户凭据的请求， 应用程序就可尝试对进入系统的用户进行认证。 但如果由于用户凭据不正确而导致认证失败， 那就没有必要进行后续检查了

![image](https://user-images.githubusercontent.com/83335903/224527054-e005b1de-1670-426e-8076-bf6cd941bc6d.png)

后续增加的几个检查步骤：
1. 直接将原始数据传递给订购系统存在安全隐患。 因此你新增了额外的验证步骤来清理请求中的数据
2. 系统无法抵御暴力密码破解方式的攻击。 为了防范这种情况， 你立刻添加了一个检查步骤来过滤来自同一 IP 地址的重复错误请求。
3. 可以对包含同样数据的重复请求返回缓存中的结果， 从而提高系统响应速度

![image](https://user-images.githubusercontent.com/83335903/224527117-41749d44-d139-49e2-88c7-d9cb6a175705.png)

每次新增功能都会使其更加臃肿，系统会变得让人非常费解， 而且其维护成本也会激增。 


# 解决
责任链会将特定行为转换为被称作`处理者`的独立对象。 在上述示例中， 每个检查步骤都可被抽取为仅有单个方法的类， 并执行检查操作。请求及其数据则会被作为参数传递给该方法。

`责任链模式`建议将这些处理者连成一条链。 链上的每个处理者都有一个成员变量来保存对于下一处理者的引用。 除了处理请求外，处理者还负责沿着链传递请求。请求会在链上移动， 直至所有处理者都有机会对其进行处理

![image](https://user-images.githubusercontent.com/83335903/224527187-7bc2cdf0-3a47-47e3-bc14-1e8fd8a21fb7.png)

另一种稍微不同的方式（也是更经典一种），树状结构：例如， 当用户点击按钮时， 按钮产生的事件将沿着 GUI 元素链进行传递， 最开始是按钮的容器 （如窗体或面板）， 直至应用程序主窗口。 链上第一个能处理该事件的元素会对其进行处理。 此外， 该例还有另一个值得我们关注的地方： 它表明我们总能从对象树中抽取出链来

![image](https://user-images.githubusercontent.com/83335903/224527235-256bdb05-1502-4588-9af1-df8027c6dde2.png)

所有处理者类均实现同一接口是关键所在。 每个具体处理者仅关心下一个包含`execute`执行方法的处理者。 这样一来， 你就可以在运行时使用不同的处理者来创建链， 而无需将相关代码与处理者的具体类进行耦合


# 类比真实世界

![image](https://user-images.githubusercontent.com/83335903/224527277-d9b0dc33-41e2-433b-804a-35880ee1ba65.png)


# 结构

![image](https://user-images.githubusercontent.com/83335903/224527283-84fad1c3-c4c8-434f-b284-6a95e9e9128d.png)

1. 处理者 （Handler） 声明了所有具体处理者的通用接口。 该接口通常仅包含单个方法用于请求处理， 但有时其还会包含一个设置链上下个处理者的方法。

2. 基础处理者 （Base Handler） 是一个可选的类， 你可以将所有处理者共用的样本代码放置在其中。通常情况下， 该类中定义了一个保存对于下个处理者引用的成员变量。 客户端可通过将处理者传递给上个处理者的构造函数或设定方法来创建链。 该类还可以实现默认的处理行为： 确定下个处理者存在后再将请求传递给它。

3. 具体处理者 （Concrete Handlers） 包含处理请求的实际代码。 每个处理者接收到请求后， 都必须决定是否进行处理， 以及是否沿着链传递请求。处理者通常是独立且不可变的， 需要通过构造函数一次性地获得所有必要地数据。

4. 客户端 （Client） 可根据程序逻辑一次性或者动态地生成链。 值得注意的是， 请求可发送给链上的任意一个处理者， 而非必须是第一个处理者

# java伪代码
小华向公司申请一台Mac笔记本电脑。需要领导审批，而每个领导权限不一样，能够审批的最大金额有限制。首先肯定是向自己的小组leader提出，但是由于金额太大，超出了他的审批权限。于是小组领导就去找自己的领导，部门经理，但是部门经理也权限不够，最后到了CFO那里...

这种情况就非常适合使用责任链模式。这个预算申请的请求，事先不知道会由哪层领导处理，而各层领导的审批职责就好像铁链一样连接在一起，一个预算请求沿着这条链一直往上传... 让我们用代码来实现上面的场景吧。

设计一个所有处理器都要实现的接口：

 ```
 public interface BudgetHandler {
    void setNextHandler(BudgetHandler nextHandler);

    boolean handle(int amount);
}
 ```
 其中setNextHandler(BudgetHandler)方法负责设置下一个处理器，以便在自己不能处理此请求的情况下，将请求交给下一个处理器。handle(int) 方法负责处理请求
 
 实现各种处理器：
 小组领导类：
 ```
 public class GroupLeader implements BudgetHandler {
    private BudgetHandler nextHandler;

    @Override
    public void setNextHandler(BudgetHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(int amount) {
        Objects.requireNonNull(nextHandler);
        if(amount<1000){
            System.out.println("小钱，批了！");
            return true;
        }
        System.out.println(String.format("%d超出GroupLeader权限,请更高级管理层批复",amount));
        return nextHandler.handle(amount);
    }
}
 ```
 可见，小组领导最多可以批1000块以下的预算，再多了就批不了了。
 经理类:
 ```
 public class Manager implements BudgetHandler {
    private BudgetHandler nextHandler;

    @Override
    public void setNextHandler(BudgetHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(int amount) {
        Objects.requireNonNull(nextHandler);
        if(amount<5000){
            System.out.println("小于2000块，我这个经理可以决定：同意！");
            return true;
        }
        System.out.println(String.format("%d超出Manager权限,请更高级管理层批复",amount));
        return nextHandler.handle(amount);
    }
}
 ```
 经理最多可以批5000块以下的预算
 首席财务官类：
 ```
 public class CFO implements BudgetHandler {
    private BudgetHandler nextHandler;

    @Override
    public void setNextHandler(BudgetHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public boolean handle(int amount) {
        if(amount<50000){
            System.out.println("CFO同意,希望你再接再厉，为公司做出更大的贡献。");
            return true;
        }
        if (nextHandler!=null){
            return nextHandler.handle(amount);
        }
        //已经没有更高级的管理层来处理了
        System.out.println(String.format("%d太多了，回去好好看看能不能缩减一下",amount));
        return false;
    }
}
 ```
 客户端:每个处理器都建好了，那么怎么才能让他们连成链呢？这就是客户端的责任了
 ```
 public class DogWang2Cor {
    public void applyBudget() {
        GroupLeader leader = new GroupLeader();
        Manager manager = new Manager();
        CFO cfo = new CFO();

        leader.setNextHandler(manager);
        manager.setNextHandler(cfo);

        System.out.println(String.format("领导您好：由于开发需求，需要购买一台Mac笔记本电脑，预算为%d 望领导批准", 95000));
        if (leader.handle(95000)) {
            System.out.println("谢谢领导");
        } else {
            System.out.println("巧妇难为无米之炊，只能划船了...");
        }
    }
}
 ```
 首先，小华不知道谁最终会批准这笔预算，但是他知道入口肯定是他的小组领导，小组领导上一级就是经理，再上一级是CFO。所以在Handler中使用setNextHandler方法指定下一个Handler，最后由于CFO是最后一个处理器，所以我们就不设置Handler。最终小华拿到了自己心仪的Mac笔记本电.

# 应用场景
1. 当程序需要使用不同方式处理不同种类请求， 而且请求类型和顺序预先未知时， 可以使用责任链模式，该模式能将多个处理者连接成一条链。 接收到请求后， 它会 “询问” 每个处理者是否能够对其进行处理。 这样所有处理者都有机会来处理请求
2. 当必须按顺序执行多个处理者时， 可以使用该模式。 无论你以何种顺序将处理者连接成一条链， 所有请求都会严格按照顺序通过链上的处理者。
3. 如果所需处理者及其顺序必须在运行时进行改变， 可以使用责任链模式。如果在处理者类中有对引用成员变量的设定方法， 你将能动态地插入和移除处理者， 或者改变其顺序。

