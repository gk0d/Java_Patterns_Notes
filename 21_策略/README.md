
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)



# 思考
创建一款导游程序。 该程序的核心功能是提供美观的地图， 以帮助用户在任何城市中快速定位。

用户期待的程序新功能是自动路线规划： 他们希望输入地址后就能在地图上看到前往目的地的最快路线。

程序的首个版本只能规划公路路线。 驾车旅行的人们对此非常满意。 但很显然， 并非所有人都会在度假时开车。 因此你在下次更新时添加了规划步行路线的功能。 此后， 你又添加了规划公共交通路线的功能。

而这只是个开始。 不久后， 你又要为骑行者规划路线。 又过了一段时间， 你又要为游览城市中的所有景点规划路线。

![image](https://user-images.githubusercontent.com/83335903/224536699-00a58564-a733-4e81-9451-746bda92c1a5.png)

次添加新的路线规划算法后， 导游应用中主要类的体积就会增加一倍。 代码极其臃肿

# 解决
策略模式建议找出负责用许多不同方式完成特定任务的类， 然后将其中的算法抽取到一组被称为`策略`的独立类中。

名为`上下文`的原始类必须包含一个成员变量来存储对于每种策略的引用。 上下文并不执行任务， 而是将工作委派给已连接的策略对象。

上下文不负责选择符合任务需要的算法——客户端会将所需策略传递给上下文。 实际上， 上下文并不十分了解策略， 它会通过同样的通用接口与所有策略进行交互， 而该接口只需暴露一个方法来触发所选策略中封装的算法即可。

因此， 上下文可独立于具体策略。 这样你就可在不修改上下文代码或其他策略的情况下添加新算法或修改已有算法了。

![image](https://user-images.githubusercontent.com/83335903/224536745-d688119d-1b10-4566-8e28-c70d8144e3dc.png)




# 类比真实世界

![image](https://user-images.githubusercontent.com/83335903/224536755-03189df6-5c3a-461c-bc2e-f4a55db83a5f.png)

需要前往机场。 你可以选择乘坐公共汽车、 预约出租车或骑自行车。 这些就是你的出行策略。 你可以根据预算或时间等因素来选择其中一种策略。

# 结构

![image](https://user-images.githubusercontent.com/83335903/224536764-f173e10e-beff-48f9-89d9-499a84f0c7b0.png)

1. 上下文 （Context） 维护指向具体策略的引用， 且仅通过策略接口与该对象进行交流。

2. 策略 （Strategy） 接口是所有具体策略的通用接口， 它声明了一个上下文用于执行策略的方法。

3. 具体策略 （Concrete Strategies） 实现了上下文所用算法的各种不同变体。

4. 当上下文需要运行算法时， 它会在其已连接的策略对象上调用执行方法。 上下文不清楚其所涉及的策略类型与算法的执行方式。

5. 客户端 （Client） 会创建一个特定策略对象并将其传递给上下文。 上下文则会提供一个设置器以便客户端在运行时替换相关联的策略


# java伪代码
需求：输入一个价格和支付类型，模拟使用不同支付通道的情况。
1. 把通用方法抽离，抽象成一个父类。
```
public abstract class PayChannel {
    public abstract void pay(String price);
}
```
2. 然后，创建它的三个具体策略类：
```
public class AliyPay extends PayChannel {
    @Override
    public void pay(String price) {
        System.out.println("调起支付宝SDK，价格：" + price);
    }
}
public class UnionPay extends PayChannel {
    @Override
    public void pay(String price) {
        System.out.println("调起银联SDK，价格：" + price);
    }
}
public class WechatPay extends PayChannel {
    @Override
    public void pay(String price) {
        System.out.println("调起微信SDK，价格：" + price);
    }
}
```
3. 再创建一个环境类，用来操作不同的策略
```
public class PayContext {
    private PayChannel payChannel;

    public PayContext(PayChannel payChannel){
        this.payChannel = payChannel;
    }

    public void pay(String price){
        payChannel.pay(price);
    }
}
```
4. 最后，客户调用不同的支付策略：
```
public static void main(String[] args) {
    PayContext context = new PayContext(new WechatPay());//替换不同策略
    context.pay("100元");
}
```

# 应用场景
1. 你想使用对象中各种不同的算法变体， 并希望能在运行时切换算法时， 可使用策略模式
2. 当你有许多仅在执行某些行为时略有不同的相似类时， 可使用策略模式。
3. 当类中使用了复杂条件运算符以在同一算法的不同变体中切换时， 可使用该模式。
