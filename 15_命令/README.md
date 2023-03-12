
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)
- [java伪代码](#java伪代码)



# 思考
开发一款新的文字编辑器，创建一个包含多个按钮的工具栏， 并让每个按钮对应编辑器的不同操作。

![image](https://user-images.githubusercontent.com/83335903/224528731-254feafb-b548-4271-b9f6-0957a24858c0.png)


所有按钮看上去都很相似， 但它们可以完成不同的操作 （打开、 保存、 打印和应用等）。 你会在哪里放置这些按钮的点击处理代码呢？ 最简单的解决方案是在使用按钮的每个地方都创建大量的子类。 这些子类中包含按钮点击后必须执行的代码。

![image](https://user-images.githubusercontent.com/83335903/224528750-ec4da86a-3484-4472-9c86-6555b554ebcb.png)

这种方式有严重缺陷。 首先， 你创建了大量的子类， 当每次修改基类 按钮时， 你都有可能需要修改所有子类的代码。 简单来说， GUI 代码以一种拙劣的方式依赖于业务逻辑中的不稳定代码

![image](https://user-images.githubusercontent.com/83335903/224528764-e7fe46ad-677e-4c8c-85bb-c2d9a4a39b54.png)

复制/粘贴文字等操作可能会在多个地方被调用。 例如用户可以点击工具栏上小小的 “复制” 按钮， 或者通过上下文菜单复制一些内容， 又或者直接使用键盘上的 Ctrl+C 。

我们的程序最初只有工具栏， 因此可以使用按钮子类来实现各种不同操作。 换句话来说， 复制按钮CopyButton子类包含复制文字的代码是可行的。 在实现了上下文菜单、 快捷方式和其他功能后， 你要么需要将操作代码复制进许多个类中， 要么需要让菜单依赖于按钮， 而后者是更糟糕的选择。


# 解决
软件分层，最常见的例子： 一层负责用户图像界面； 另一层负责业务逻辑。 GUI层负责在屏幕上渲染美观的图形， 捕获所有输入并显示用户和程序工作的结果。 当需要完成一些重要内容时 （比如计算月球轨道或撰写年度报告）， GUI 层则会将工作委派给业务逻辑底层。

这在代码中看上去就像这样： 一个 GUI 对象传递一些参数来调用一个业务逻辑对象。 这个过程通常被描述为一个对象发送请求给另一个对象。

![image](https://user-images.githubusercontent.com/83335903/224528804-74cd54ba-7a79-4262-b6c1-446ec6686e89.png)

`命令模式`建议 GUI 对象不直接提交这些请求。 你应该将请求的所有细节 （例如调用的对象、 方法名称和参数列表） 抽取出来组成命令类， 该类中仅包含一个用于触发请求的方法。

命令对象负责连接不同的 GUI 和业务逻辑对象。 此后， GUI 对象无需了解业务逻辑对象是否获得了请求， 也无需了解其对请求进行处理的方式。 GUI 对象触发命令即可， 命令对象会自行处理所有细节工作。

![image](https://user-images.githubusercontent.com/83335903/224528833-0686b1ad-5afa-4534-b65c-61b82d718e54.png)

下一步是让所有命令实现相同的接口。 该接口通常只有一个没有任何参数的执行方法， 让你能在不和具体命令类耦合的情况下使用同一请求发送者执行不同命令。 此外还有额外的好处， 现在你能在运行时切换连接至发送者的命令对象， 以此改变发送者的行为。

你可能会注意到遗漏的一块拼图——请求的参数。 GUI 对象可以给业务层对象提供一些参数。 但执行命令方法没有任何参数， 所以我们如何将请求的详情发送给接收者呢？ 答案是： 使用数据对命令进行预先配置， 或者让其能够自行获取数据。

![image](https://user-images.githubusercontent.com/83335903/224528849-5692ffd1-5a0b-4990-8961-b901c01fb66b.png)

回到文本编辑器。 应用命令模式后， 我们不再需要任何按钮子类来实现点击行为。 我们只需在 按钮Button基类中添加一个成员变量来存储对于命令对象的引用， 并在点击后执行该命令即可。

你需要为每个可能的操作实现一系列命令类， 并且根据按钮所需行为将命令和按钮连接起来。

其他菜单、 快捷方式或整个对话框等 GUI 元素都可以通过相同方式来实现。 当用户与 GUI 元素交互时， 与其连接的命令将会被执行。 现在你很可能已经猜到了， 与相同操作相关的元素将会被连接到相同的命令， 从而避免了重复代码。

最后， 命令成为了减少 GUI 和业务逻辑层之间耦合的中间层。 而这仅仅是命令模式所提供的一小部分好处！



# 类比真实世界

![image](https://user-images.githubusercontent.com/83335903/224528910-59776ab7-355d-4c2f-a5eb-3f027ced147f.png)

在市中心逛了很久的街后， 你找到了一家不错的餐厅， 坐在了临窗的座位上。 一名友善的服务员走近你， 迅速记下你点的食物， 写在一张纸上。 服务员来到厨房， 把订单贴在墙上。 过了一段时间， 厨师拿到了订单， 他根据订单来准备食物。 厨师将做好的食物和订单一起放在托盘上。 服务员看到托盘后对订单进行检查， 确保所有食物都是你要的， 然后将食物放到了你的桌上。

那张纸就是一个命令， 它在厨师开始烹饪前一直位于队列中。 命令中包含与烹饪这些食物相关的所有信息。 厨师能够根据它马上开始烹饪， 而无需跑来直接和你确认订单详情。

# 结构

![image](https://user-images.githubusercontent.com/83335903/224528915-c424f11b-bb35-493d-b97c-4cde7cfbe3c2.png)

1. 发送者 （Sender）——亦称 “触发者 （Invoker）”——类负责对请求进行初始化， 其中必须包含一个成员变量来存储对于命令对象的引用。 发送者触发命令， 而不向接收者直接发送请求。 注意， 发送者并不负责创建命令对象： 它通常会通过构造函数从客户端处获得预先生成的命令。

2. 命令 （Command） 接口通常仅声明一个执行命令的方法。

3. 具体命令 （Concrete Commands） 会实现各种类型的请求。 具体命令自身并不完成工作， 而是会将调用委派给一个业务逻辑对象。 但为了简化代码， 这些类可以进行合并。接收对象执行方法所需的参数可以声明为具体命令的成员变量。 你可以将命令对象设为不可变， 仅允许通过构造函数对这些成员变量进行初始化。

4. 接收者 （Receiver） 类包含部分业务逻辑。 几乎任何对象都可以作为接收者。 绝大部分命令只处理如何将请求传递到接收者的细节， 接收者自己会完成实际的工作。

5. 客户端 （Client） 会创建并配置具体命令对象。 客户端必须将包括接收者实体在内的所有请求参数传递给命令的构造函数。 此后， 生成的命令就可以与一个或多个发送者相关联了。

# java伪代码
核心就是将命令抽象成一个对象;本例中， 命令模式会记录已执行操作的历史记录， 以在需要时撤销操作。

![image](https://user-images.githubusercontent.com/83335903/224529066-ee95458a-1b27-43de-a8f8-da94aca6e1e1.png)

游戏的开启和关闭 , 使用命令模式实现 , 分别针对开启, 关闭 , 各自定义一个命令类：
1.命令接口：
```
package command;
/**
 * 命令接口
 *      所有的命令都要实现该接口
 */
public interface Command {
    /**
     * 执行命令方法
     */
    void execute();
}
```
开启命令类：
```
package command;
/**
 * 开启命令
 *      实现 Command 接口
 *      该类代表了一种命令
 */
public class OpenCommand implements Command{
    private Game game;
    public OpenCommand(Game game) {
        this.game = game;
    }
    @Override
    public void execute() {
        this.game.open();
    }
}
```
关闭命令类：
```
package command;
/**
 * 关闭命令
 */
public class CloseCommand implements Command {
    private Game game;
    public CloseCommand(Game game) {
        this.game = game;
    }
    @Override
    public void execute() {
        this.game.close();
    }
}
```
游戏类:
```
package command;
/**
 * 该类与命令执行的具体逻辑相关
 */
public class Game {
    private String name;
    public Game(String name) {
        this.name = name;
    }
    public void open() {
        System.out.println(this.name + " 开启");
    }
    public void close() {
        System.out.println(this.name + " 关闭");
    }
}
```
命令执行者类：
```
package command;
import java.util.ArrayList;
/**
 * 命令接收者
 *      执行命令
 */
public class Manager {
    /**
     * 存放命令
     */
    private ArrayList<Command> commands = new ArrayList<>();
    /**
     * 添加命令
     * @param command
     */
    public void addCommand(Command command) {
        commands.add(command);
    }
    /**
     * 执行命令
     */
    public void executeCommand() {
        for (Command command : commands) {
            // 逐个遍历执行命令
            command.execute();
        }
        // 命令执行完毕后 , 清空集合
        commands.clear();
    }
}
```
 测试类:
 ```
 package command;
public class Main {
    public static void main(String[] args) {
        Game game = new Game("Game 01");
        OpenCommand openCommand = new OpenCommand(game);
        CloseCommand closeCommand = new CloseCommand(game);
        // 发送命令
        Manager manager = new Manager();
        manager.addCommand(openCommand);
        manager.addCommand(closeCommand);
        // 执行命令
        manager.executeCommand();
    }
}
 
 ```
![image](https://user-images.githubusercontent.com/83335903/224531611-60b8adf6-c2e4-4778-bcd3-9873331a45cb.png)



# 应用场景
1. 需要通过操作来参数化对象， 可使用命令模式
2. 想要将操作放入队列中、 操作的执行或者远程执行操作， 可使用命令模式
3. 想要实现操作回滚功能， 可使用命令模式。
