
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)



# 思考
有一个创建和修改客户资料的对话框， 它由各种控件组成， 例如文本框 （TextField）、 复选框 （Checkbox） 和按钮（Button）等

![image](https://user-images.githubusercontent.com/83335903/224532678-319553c3-5484-4b8a-b1ed-ed4b4dd82645.png)

某些表单元素可能会直接进行互动。 例如， 选中 “我有一只狗” 复选框后可能会显示一个隐藏文本框用于输入狗狗的名字。 另一个例子是提交按钮必须在保存数据前校验所有输入内容。

![image](https://user-images.githubusercontent.com/83335903/224532698-6f31e775-fec9-48b8-9101-b293bbe94a6e.png)

如果直接在表单元素代码中实现业务逻辑， 你将很难在程序其他表单中复用这些元素类。 例如， 由于复选框类与狗狗的文本框相耦合， 所以将无法在其他表单中使用它。 你要么使用渲染资料表单时用到的所有类， 要么一个都不用


# 解决
中介者模式建议你停止组件之间的直接交流并使其相互独立。 这些组件必须调用特殊的中介者对象， 通过中介者对象重定向调用行为， 以间接的方式进行合作。 最终， 组件仅依赖于一个中介者类， 无需与多个其他组件相耦合。

在资料编辑表单的例子中， 对话框 （Dialog） 类本身将作为中介者， 其很可能已知自己所有的子元素， 因此你甚至无需在该类中引入新的依赖关系。

![image](https://user-images.githubusercontent.com/83335903/224532718-c7058e0d-1128-4fb7-99e3-0ed00a27af16.png)

绝大部分重要的修改都在实际表单元素中进行。 让我们想想提交按钮。 之前， 当用户点击按钮后， 它必须对所有表单元素数值进行校验。 而现在它的唯一工作是将点击事件通知给对话框。 收到通知后， 对话框可以自行校验数值或将任务委派给各元素。 这样一来， 按钮不再与多个表单元素相关联， 而仅依赖于对话框类。

你还可以为所有类型的对话框抽取通用接口， 进一步削弱其依赖性。 接口中将声明一个所有表单元素都能使用的通知方法， 可用于将元素中发生的事件通知给对话框。 这样一来， 所有实现了该接口的对话框都能使用这个提交按钮了。

采用这种方式， 中介者模式让你能在单个中介者对象中封装多个对象间的复杂关系网。 类所拥有的依赖关系越少， 就越易于修改、 扩展或复用。

# 类比真实世界

![image](https://user-images.githubusercontent.com/83335903/224532751-0fb35ec3-9c6c-4cc8-9555-ab813c812281.png)


# 结构

![image](https://user-images.githubusercontent.com/83335903/224532776-554e78c5-d8da-44ab-9c4e-2ba7b36298e4.png)

1. 组件 （Component） 是各种包含业务逻辑的类。 每个组件都有一个指向中介者的引用， 该引用被声明为中介者接口类型。 组件不知道中介者实际所属的类， 因此你可通过将其连接到不同的中介者以使其能在其他程序中复用。

2. 中介者 （Mediator） 接口声明了与组件交流的方法， 但通常仅包括一个通知方法。 组件可将任意上下文 （包括自己的对象） 作为该方法的参数， 只有这样接收组件和发送者类之间才不会耦合。

3. 具体中介者 （Concrete Mediator） 封装了多种组件间的关系。 具体中介者通常会保存所有组件的引用并对其进行管理， 甚至有时会对其生命周期进行管理。

4. 组件并不知道其他组件的情况。 如果组件内发生了重要事件， 它只能通知中介者。 中介者收到通知后能轻易地确定发送者， 这或许已足以判断接下来需要触发的组件了。

5. 对于组件来说， 中介者看上去完全就是一个黑箱。 发送者不知道最终会由谁来处理自己的请求， 接收者也不知道最初是谁发出了请求

# java伪代码

日常生活中，我们经常会用社交工具（QQ、微信）进行聊天。在这其中，群组聊天的设计正是中介者模式的例子，我们下面模拟一下：

>设计思路：每个聊天用户就相当于同事类，聊天服务器就相当于我们的中介者。整个流程是用户向聊天服务器发送一条消息，服务器会将此消息发送给群组中的所有人。

1、创建抽象中介者角色
```
public abstract class ChatMediator {
    public abstract void register(ChatClient client);
    public abstract void notice(ChatClient client,String message);
}
```
定义一些接口用于同事类的通讯。在这里我们定义一个加入群聊的行为和向所有人发送通知的行为

2、实现具体中介者角色
```
public class ChatServer extends ChatMediator {

    private List<ChatClient> clientList = new ArrayList<>();

    @Override
    public void notice(ChatClient client, String message) {
        for (ChatClient c : clientList) {
            if (!client.equals(c)) {
                c.receiveMessage(message);
            }
        }
    }

    @Override
    public void register(ChatClient client) {
        if (client != null && !clientList.contains(client)) {
            clientList.add(client);
        }
    }

}
```
具体中介者负责协调同事类之间的交互，notice()方法向所群组中除了自己之外的用户发送消息。


3、创建抽象同事类
```
public abstract class ChatClient {
    protected ChatMediator mediator;

    public ChatClient(ChatMediator mediator){
        this.mediator = mediator;
    }

    public abstract void sendMessage(String message);
    public abstract void receiveMessage(String message);
}
```
每个用户都有一个发送消息的方法和接收消息的方法，而消息的传递则通过中介者对象mediator处理。

4、实现具体同事类
```
public class Andy extends ChatClient {

    public Andy(ChatMediator mediator) {
        super(mediator);
        mediator.register(this);
    }

    @Override
    public void sendMessage(String message) {
        System.out.println();
        System.out.println("安迪发送一条消息：" + message);
        mediator.notice(this, message);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("安迪收到一条消息：" + message);
    }
}
```
具体同事类忽略Ben、Cat、David等等的用户，实现相似！

对于每个具体同事类（用户）而言，它并不需要知道这个群里面到底有多少人，只知道自己是负责发送和接收消息的

5、测试与运行结果

```
public class Group {

    public static void main(String[] args) {
        //聊天服务器
        ChatServer chatServer = new ChatServer();

        //三个小伙伴
        Andy andy = new Andy(chatServer);
        Ben ben = new Ben(chatServer);
        Cat cat = new Cat(chatServer);

        System.out.println("=====群聊信息=====");
        andy.sendMessage("今晚去看复仇者联盟4吧！");
        System.out.println("-----——分割线——-----");
        ben.sendMessage("好啊好啊~我期待了很久!");
        System.out.println("-----——分割线---——--");
        cat.sendMessage("我来负责买票！！！");
    }
}
```
```
=====群聊信息=====

安迪发送一条消息：今晚去看复仇者联盟4吧！
Ben哥收到一条消息：今晚去看复仇者联盟4吧！
猫猫收到一条消息：今晚去看复仇者联盟4吧！
-----——分割线——-----
Ben哥发送一条消息：好啊好啊~我期待了很久!
安迪收到一条消息：好啊好啊~我期待了很久!
猫猫收到一条消息：好啊好啊~我期待了很久!
-----——分割线---——--
猫猫发送一条消息：我来负责买票！！！
安迪收到一条消息：我来负责买票！！！
Ben哥收到一条消息：我来负责买票！！！
```
每个用户直接调用sendMessage()的方法就可以完成通讯了，就算是一个100人的大群每个用户也不用改动代码~
# 应用场景
1. 当一些对象和其他对象紧密耦合以致难以对其进行修改时， 可使用中介者模式
2. 当组件因过于依赖其他组件而无法在不同应用中复用时， 可使用中介者模式
3. 如果为了能在不同情景下复用一些基本行为， 导致你需要被迫创建大量组件子类时， 可使用中介者模式 


