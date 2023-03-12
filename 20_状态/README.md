
- [思考](#思考)
- [解决](#解决)
- [类比真实世界](#类比真实世界)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)



# 思考
状态模式与有限状态机的概念紧密相关:
其主要思想是程序在任意时刻仅可处于几种有限的状态中。 在任何一个特定状态中， 程序的行为都不相同， 且可瞬间从一个状态切换到另一个状态。 不过， 根据当前状态， 程序可能会切换到另外一种状态， 也可能会保持当前状态不变。 这些数量有限且预先定义的状态切换规则被称为转移

可将该方法应用在对象上。 假如你有一个文档Document类。 文档可能会处于草稿Draft 、 审阅中Moderation和 已发布Published三种状态中的一种。 文档的 publish发布方法在不同状态下的行为略有不同

- 处于 草稿状态时， 它会将文档转移到审阅中状态。
- 处于 审阅中状态时， 如果当前用户是管理员， 它会公开发布文档。
- 处于 已发布状态时， 它不会进行任何操作。

![image](https://user-images.githubusercontent.com/83335903/224536047-db6450de-bf2a-4a2d-829b-2537a3d6a805.png)

状态机通常由众多条件运算符 （ if或 switch ） 实现， 可根据对象的当前状态选择相应的行为。 ​ “状态” 通常只是对象中的一组成员变量值。 即使你之前从未听说过有限状态机， 你也很可能已经实现过状态模式。 下面的代码应该能帮助你回忆起来。

```
class Document is
    field state: string
    // ……
    method publish() is
        switch (state)
            "draft":
                state = "moderation"
                break
            "moderation":
                if (currentUser.role == "admin")
                    state = "published"
                break
            "published":
                // 什么也不做。
                break
    // ……
```



# 解决
状态模式建议为对象的所有可能状态新建一个类， 然后将所有状态的对应行为抽取到这些类中。

原始对象被称为上下文 （context）， 它并不会自行实现所有行为， 而是会保存一个指向表示当前状态的状态对象的引用， 且将所有与状态相关的工作委派给该对象。

![image](https://user-images.githubusercontent.com/83335903/224536107-6e12ddbd-3ae3-41bf-a959-0b3f1bf00b22.png)

如需将上下文转换为另外一种状态， 则需将当前活动的状态对象替换为另外一个代表新状态的对象。 采用这种方式是有前提的： 所有状态类都必须遵循同样的接口， 而且上下文必须仅通过接口与这些对象进行交互。

这个结构可能看上去与策略模式相似， 但有一个关键性的不同——在状态模式中， 特定状态知道其他所有状态的存在， 且能触发从一个状态到另一个状态的转换； 策略则几乎完全不知道其他策略的存在。

# 类比真实世界
智能手机的按键和开关会根据设备当前状态完成不同行为：

- 当手机处于解锁状态时， 按下按键将执行各种功能。
- 当手机处于锁定状态时， 按下任何按键都将解锁屏幕。
- 当手机电量不足时， 按下任何按键都将显示充电页面。

# 结构

![image](https://user-images.githubusercontent.com/83335903/224536230-8d7629db-7e29-4ab6-bbd0-ebbd2b09cedb.png)

1. 上下文 （Context） 保存了对于一个具体状态对象的引用， 并会将所有与该状态相关的工作委派给它。 上下文通过状态接口与状态对象交互， 且会提供一个设置器用于传递新的状态对象。

2. 状态 （State） 接口会声明特定于状态的方法。 这些方法应能被其他所有具体状态所理解， 因为你不希望某些状态所拥有的方法永远不会被调用。

3. 具体状态 （Concrete States） 会自行实现特定于状态的方法。 为了避免多个状态中包含相似代码， 你可以提供一个封装有部分通用行为的中间抽象类。

4. 状态对象可存储对于上下文对象的反向引用。 状态可以通过该引用从上下文处获取所需信息， 并且能触发状态转移。

5. 上下文和具体状态都可以设置上下文的下个状态， 并可通过替换连接到上下文的状态对象来完成实际的状态转换。


# java伪代码
在日常开发中，用户的登录和未登录两种状态有不同的操作是很常见的一种业务。比如登录后我们才可以进行支付、编辑资料等业务操作，未登录就提示跳转去登录界面。下面我们一起实现以下这个比较常见的逻辑：

需求：模拟登录与未登录两种状态的业务操作
 ```
 public void pay() {
    if(未登录){
        System.out.println("去登录");
        return;
    } 
    System.out.println("进行支付！");
 }

public void editMessage(){
    if(未登录){
        System.out.println("去登录");
        return;
    }     
    System.out.println("编辑个人资料！");
}

public void share(){
    if(未登录){
        System.out.println("去登录");
        return;
    }
    System.out.println("进行分享!");
}

 ```

使用状态模式设计登录与未登录逻辑
1.编写抽象状态接口:定义了不同状态下需要执行的方法
```
public interface UserState {
    void pay();
    void editMessage();
    void share();
}
```
2. 实现登录与未登录两种状态登录状态就能进行相关业务的跳转操作，而未登录状态就提示去登录。
```
public class LoginState implements UserState{

    @Override
    public void pay() {
        System.out.println("进行支付！");
    }

    @Override
    public void editMessage() {
        System.out.println("编辑个人资料！");
    }

    @Override
    public void share() {
        System.out.println("进行分享!");
    }
}
```
```
public class LogoutState implements UserState {

    @Override
    public void pay() {
        System.out.println("去登录!");
    }

    @Override
    public void editMessage() {
        System.out.println("去登录!");
    }

    @Override
    public void share() {
        System.out.println("去登录!");
    }
}
```
编写状态操作类Context:此类负责操作状态，并且实现了调用类需要用到的方法。
```
public class LoginContext {
    private UserState mUserState = new LogoutState();

    private static LoginContext sLoginContext = new LoginContext();

    private LoginContext(){

    }

    public static LoginContext getsLoginContext(){
        return sLoginContext;
    }

    public void login() {
        mUserState = new LoginState();
    }

    public void logout() {
        mUserState = new LogoutState();
    }

    public void pay() {
        mUserState.pay();
    }

    public void editMessage(){
        mUserState.editMessage();
    }

    public void share(){
        mUserState.share();
    }
    

}
```
实现:
```
public class Test {
    public static void main(String[] args) {
        LoginContext loginContext = LoginContext.getsLoginContext();

        //设置为未登录状态
        System.out.println("======未登录状态======");
        loginContext.logout();
        loginContext.editMessage();
        loginContext.pay();
        loginContext.share();

        //设置为已登录状态
        System.out.println("======已登录状态======");
        loginContext.login();
        loginContext.editMessage();
        loginContext.pay();
        loginContext.share();

    }
}
```
```
======未登录状态======
去登录!
去登录!
去登录!
======已登录状态======
编辑个人资料！
进行支付！
进行分享!
```
这样就完成了状态模式的使用了。调用pay()、share()、editMessage()等业务方法时，所有的逻辑修改都放在了状态子类，比较方便维护和扩展

# 应用场景
1. 如果对象需要根据自身当前状态进行不同行为， 同时状态的数量非常多且与状态相关的代码会频繁变更的话， 可使用状态模式。
2. 如果某个类需要根据成员变量的当前值改变自身行为， 从而需要使用大量的条件语句时， 可使用该模式
3. 当相似状态和基于条件的状态机转换中存在许多重复代码时， 可使用状态模式。


