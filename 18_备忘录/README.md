
- [思考](#思考)
- [解决](#解决)

- [结构](#结构)
  - [基于嵌套类的实现](#基于嵌套类的实现)
  - [基于中间接口的实现](#基于中间接口的实现)
  - [封装更加严格的实现](#封装更加严格的实现)
- [java伪代码](#java伪代码)
  - [普通](#普通)
  - [使用备忘录模式优化](#使用备忘录模式优化)
- [应用场景](#应用场景)



# 思考
开发一款文字编辑器应用程序。 除了简单的文字编辑功能外， 编辑器中还要有设置文本格式和插入内嵌图片等功能
后来， 你决定让用户能撤销施加在文本上的任何操作。 你选择采用直接的方式来实现该功能： 程序在执行任何操作前会记录所有的对象状态， 并将其保存下来。 当用户此后需要撤销某个操作时， 程序将从历史记录中获取最近的快照， 然后使用它来恢复所有对象的状态

![image](https://user-images.githubusercontent.com/83335903/224533451-7df3faf5-eec7-40da-a229-1459f111e346.png)

首先， 到底该如何生成一个快照呢？ 很可能你会需要遍历对象的所有成员变量并将其数值复制保存。 但只有当对象对其内容没有严格访问权限限制的情况下， 你才能使用该方式。 不过很遗憾， 绝大部分对象会使用私有成员变量来存储重要数据， 这样别人就无法轻易查看其中的内容.

![image](https://user-images.githubusercontent.com/83335903/224533470-4487fc49-445f-44fd-8ebf-3e6b4f1b577b.png)

还有更多问题。 让我们来考虑编辑器 （Editor） 状态的实际 “快照”， 它需要包含哪些数据？ 至少必须包含实际的文本、 光标坐标和当前滚动条位置等。 你需要收集这些数据并将其放入特定容器中， 才能生成快照。

你很可能会将大量的容器对象存储在历史记录列表中。 这样一来， 容器最终大概率会成为同一个类的对象。 这个类中几乎没有任何方法， 但有许多与编辑器状态一一对应的成员变量。 为了让其他对象能保存或读取快照， 你很可能需要将快照的成员变量设为公有。 无论这些状态是否私有， 其都将暴露一切编辑器状态。 其他类会对快照类的每个小改动产生依赖， 除非这些改动仅存在于私有成员变量或方法中， 而不会影响外部类。

我们似乎走进了一条死胡同： 要么会暴露类的所有内部细节而使其过于脆弱； 要么会限制对其状态的访问权限而无法生成快照。 那么， 我们还有其他方式来实现 “撤销” 功能吗？



# 解决
我们刚才遇到的所有问题都是封装 “破损” 造成的。 一些对象试图超出其职责范围的工作。 由于在执行某些行为时需要获取数据， 所以它们侵入了其他对象的私有空间， 而不是让这些对象来完成实际的工作。

备忘录模式将创建状态快照 （Snapshot） 的工作委派给实际状态的拥有者原发器 （Originator） 对象。 这样其他对象就不再需要从 “外部” 复制编辑器状态了， 编辑器类拥有其状态的完全访问权， 因此可以自行生成快照。

模式建议将对象状态的副本存储在一个名为备忘录 （Memento） 的特殊对象中。 除了创建备忘录的对象外， 任何对象都不能访问备忘录的内容。 其他对象必须使用受限接口与备忘录进行交互， 它们可以获取快照的元数据 （创建时间和操作名称等）， 但不能获取快照中原始对象的状态。

![image](https://user-images.githubusercontent.com/83335903/224533641-47a2dc19-a76a-4a8f-8b08-53072f0aba85.png)

这种限制策略允许你将备忘录保存在通常被称为负责人 （Caretakers） 的对象中。 由于负责人仅通过受限接口与备忘录互动， 故其无法修改存储在备忘录内部的状态。 同时， 原发器拥有对备忘录所有成员的访问权限， 从而能随时恢复其以前的状态。

在文字编辑器的示例中， 我们可以创建一个独立的历史 （History） 类作为负责人。 编辑器每次执行操作前， 存储在负责人中的备忘录栈都会生长。 你甚至可以在应用的 UI 中渲染该栈， 为用户显示之前的操作历史。

当用户触发撤销操作时， 历史类将从栈中取回最近的备忘录， 并将其传递给编辑器以请求进行回滚。 由于编辑器拥有对备忘录的完全访问权限， 因此它可以使用从备忘录中获取的数值来替换自身的状态




# 结构
## 基于嵌套类的实现
该模式的经典实现方式依赖于许多流行编程语言 （例如 C++、 C# 和 Java） 所支持的嵌套类。

![image](https://user-images.githubusercontent.com/83335903/224533699-f92729bf-271e-4c88-ad9b-30acbf67540b.png)

1. 原发器 （Originator） 类可以生成自身状态的快照， 也可以在需要时通过快照恢复自身状态。

2. 备忘录 （Memento） 是原发器状态快照的值对象 （value object）。 通常做法是将备忘录设为不可变的， 并通过构造函数一次性传递数据。

3. 负责人 （Caretaker） 仅知道 “何时” 和 “为何” 捕捉原发器的状态， 以及何时恢复状态。

4. 负责人通过保存备忘录栈来记录原发器的历史状态。 当原发器需要回溯历史状态时， 负责人将从栈中获取最顶部的备忘录， 并将其传递给原发器的恢复 （restoration） 方法。

5. 在该实现方法中， 备忘录类将被嵌套在原发器中。 这样原发器就可访问备忘录的成员变量和方法， 即使这些方法被声明为私有。 另一方面， 负责人对于备忘录的成员变量和方法的访问权限非常有限： 它们只能在栈中保存备忘录， 而不能修改其状态。

## 基于中间接口的实现
另外一种实现方法适用于不支持嵌套类的编程语言 （ PHP）。

![image](https://user-images.githubusercontent.com/83335903/224533772-50a9ae9f-414a-4f65-a49c-fff983bd2be4.png)

在没有嵌套类的情况下， 你可以规定负责人仅可通过明确声明的中间接口与备忘录互动， 该接口仅声明与备忘录元数据相关的方法， 限制其对备忘录成员变量的直接访问权限。

另一方面， 原发器可以直接与备忘录对象进行交互， 访问备忘录类中声明的成员变量和方法。 这种方式的缺点在于你需要将备忘录的所有成员变量声明为公有。

## 封装更加严格的实现
如果你不想让其他类有任何机会通过备忘录来访问原发器的状态， 那么还有另一种可用的实现方式。

![image](https://user-images.githubusercontent.com/83335903/224533794-ba327d7d-cee8-48db-908b-600f0cf703cf.png)

1. 这种实现方式允许存在多种不同类型的原发器和备忘录。 每种原发器都和其相应的备忘录类进行交互。 原发器和备忘录都不会将其状态暴露给其他类。

2. 负责人此时被明确禁止修改存储在备忘录中的状态。 但负责人类将独立于原发器， 因为此时恢复方法被定义在了备忘录类中。

3. 每个备忘录将与创建了自身的原发器连接。 原发器会将自己及状态传递给备忘录的构造函数。 由于这些类之间的紧密联系， 只要原发器定义了合适的设置器 （setter）， 备忘录就能恢复其状态。

# java伪代码
## 普通

我们使用Word文档编辑，首先写完一篇文章，点完保存的操作才能顺利保存文本内容，下面我们模拟一下这个逻辑：
需求：模拟Word文档的业务
定好架构：首先用逆向思维大概构想这个功能。从使用端的角度来说，最后的调用大概是这样子
```
Word w = new Word();
  w.edit("哈哈哈");
  w.save();
  w.edit("哈哈哈哈123");
  w.restore();
```

生成Word类
```
public class Word {
    private String text;

    /**
     * 编辑文档
     *
     * @param text
     */
    public void edit(String text) {
        this.text = text;
    }

    /**
     * 保存文档
     */
    public void save() {
        //将text属性存到本地或数据库
    }

    /**
     * 恢复文档
     */
    public void restore() {
        //从本地或数据库得到text属性
    }

    @Override
    public String toString() {
        return "文本内容为：" + text;
    }
}
```
为了将text保存到本地或者数据库，实现save()和restore()方法就可以了。我们的平常在开发中的做法就是生成多一个数据管理类进行数据的管理。

生成WordDB类
```
public class WordDB {
    private String text;
    private static WordDB mInstance;

    public static WordDB getInstance(){
        if (mInstance == null){
            mInstance = new WordDB();
        }
        return mInstance;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

```
这里简单用单例模拟一下，负责存取一个文本内容。save()方法和restore()方法的实现如下：
```
    /**
     * 保存文档
     */
    public void save() {
        //将text属性存到本地或数据库
        WordDB.getInstance().setText(text);
    }

    /**
     * 恢复文档
     */
    public void restore() {
        //从本地得到text属性或数据库
        this.text = WordDB.getInstance().getText();
    }
    
```
测试与实现

```
public static void main(String[] args) {
    Word w = new Word();
    w.edit("哈哈哈");
    System.out.println(w.toString());
    w.save();
    System.out.println("=====执行保存操作=====");
    w.edit("哈哈哈哈123");
    System.out.println("重新编辑，" + w.toString());
    w.restore();
    System.out.println("=====执行恢复操作=====");
    System.out.println("恢复后，" + w.toString());
}

```

```
文本内容为：哈哈哈
=====执行保存操作=====
重新编辑，文本内容为：哈哈哈哈123
=====执行恢复操作=====
恢复后，文本内容为：哈哈哈
```


执行save()方法，相当于将text内容插入进了WordDB中去，而执行resotre()就是WordDB中把数据拿回来。

这里是我们很常用的做法，也就是简单的模拟一下本次存储功能。这种简单的“存档”功能并不需要运用到备忘录模式。




##  使用备忘录模式优化
让功能复杂一点点，我们可以随便获取某一次的保存的历史记录，看看如何实现吧。


![image](https://user-images.githubusercontent.com/83335903/224533895-2d07eb28-2214-4bca-9809-1ebeef6e81f2.png)

创建备忘录角色WordMemento类
```
public class WordMemento {
    private String text;

    public WordMemento(String text) {
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
```
使用上，备忘录角色直接将临时状态的变量保存到内存中。理解上，这种撤销和重做的功能并不需要关掉Word后再重新打开，也没必要存到本地磁盘中。

创建管理者角色WordCaretaker
```
public class WordCaretaker {

    private List<WordMemento> mementos = new ArrayList<>();

    public WordMemento getMemento(int index) {
        return mementos.get(index);
    }

    public void setMemento(WordMemento db) {
        mementos.add(db);
    }

}
```
历史记录当然不止一条，每一次的输出结果都是有顺序的，所以这个用了一个List集合去操作备忘录角色。

扩充Word类为WordOriginator:我们扩展一下功能
```
public class WordOriginator {
    private String text;
    /**
     * 编辑文档，每编辑完一次做一下保存
     *
     * @param text
     */
    public WordMemento edit(String text) {
        this.text = text;
        return new WordMemento(text);
    }
    /**
     * 恢复操作，获取上次的状态
     */
    public void restoreMemento(WordMemento m) {
        this.text = m.getText();
    }
    /**
     * 最终保存文档
     */
    public void save() {
        //将text属性存到本地或数据库
        WordDB.getInstance().setText(text);
    }
    /**
     * 重新打开时，恢复文档
     */
    public void restore() {
        //从本地得到text属性或数据库
        this.text = WordDB.getInstance().getText();
    }

    @Override
    public String toString() {
        return "文本内容为：" + text;
    }
}
```
修改了edit()方法，在每次编辑的同时，保存到备忘录（就是跟Word一样的效果）。再来一个restoreMemento()恢复方法，获取某次状态的效果
```
public class WordClient {

    private static int index = -1;
    private static WordCaretaker c = new WordCaretaker();

    public static void main(String[] args) {
        WordOriginator word = new WordOriginator();
        edit(word, "今天");
        edit(word, "今天天气");
        edit(word, "今天天气真好");
        edit(word, "今天天气真好，出去逛逛！");
        undo(word);
        undo(word);
        redo(word);
        redo(word);
    }

    /**
     * 打开文档
     */
    public static void open(WordOriginator originator) {
        originator.restore();
    }

    /**
     * 关闭文档
     */
    public static void close(WordOriginator originator) {
        originator.save();
    }

    /**
     * 编辑文档
     */
    public static void edit(WordOriginator originator, String text) {
        c.setMemento(originator.edit(text));
        index++;
        System.out.println("编辑操作，" + originator.toString());
    }

    /**
     * 撤销操作
     */
    public static void undo(WordOriginator originator) {
        index--;
        originator.restoreMemento(c.getMemento(index));
        System.out.println("撤销操作，" + originator.toString());
    }

    /**
     * 重做操作
     */
    public static void redo(WordOriginator originator) {
        index++;
        originator.restoreMemento(c.getMemento(index));
        System.out.println("重做操作，" + originator.toString());
    }


}


    /**
     * 打开文档
     */
    public static void open(WordOriginator originator){
        originator.restore();
    }

    /**
     * 关闭文档
     */
    public static void close(WordOriginator originator){
        originator.save();
    }

    /**
     * 编辑文档
     */
    public static void edit(WordOriginator originator,String text){
        c.setMemento(originator.edit(text));
        index++;
    }

    /**
     * 撤销操作
     */
    public static void undo(WordOriginator originator){
        index--;
        originator.restoreMemento(c.getMemento(index));
    }

    /**
     * 重做操作
     */
    public static void redo(WordOriginator originator){
        index++;
        originator.restoreMemento(c.getMemento(index));
    }
}
```
```
编辑操作，文本内容为：今天
编辑操作，文本内容为：今天天气
编辑操作，文本内容为：今天天气真好
编辑操作，文本内容为：今天天气真好，出去逛逛！
撤销操作，文本内容为：今天天气真好
撤销操作，文本内容为：今天天气
重做操作，文本内容为：今天天气真好
重做操作，文本内容为：今天天气真好，出去逛逛！

```


# 应用场景
1. 当你需要创建对象状态快照来恢复其之前的状态时， 可以使用备忘录模式。
2. 当直接访问对象的成员变量、 获取器或设置器将导致封装被突破时， 可以使用该模式

