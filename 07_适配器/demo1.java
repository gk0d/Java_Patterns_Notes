/**
 * 这是客户所期待的接口，目标可以是具体的或抽象类，也可以是接口
 */
public interface Target {
    void request();
}

/**
 * 需要适配的类，被访问和适配的现存组件库中的组件接口
 */
public class Adaptee {
    public void specificRequest(){
        System.out.println("适配者中的业务代码被调用！");
    }
}

/**
 * 类适配器类
 */
public class ClassAdapter extends Adaptee implements Target{
    @Override
    public void request() {
        specificRequest();
    }
}

//客户端代码
public class ClassAdapterTest
{
    public static void main(String[] args)
    {
        System.out.println("类适配器模式测试：");
        Target target = new ClassAdapter();
        target.request();
    }
}
