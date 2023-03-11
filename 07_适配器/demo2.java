/**
 * 对象适配器，通过在内部包装一个 Adaptee 对象，把源接口转换为目标接口
 */
public class ObjectAdapter implements Target {

    // 建立一个私有的 Adaptee 对象
    private Adaptee adaptee;

    public ObjectAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        // 把表面上调用 request() 方法变成实际调用 specificRequest() 
        adaptee.specificRequest();
    }
}

//客户端代码
public class ObjectAdapterTest
{
    public static void main(String[] args)
    {
        System.out.println("对象适配器模式测试：");
        // 对客户端来说，调用的就是 Target 的 request()
        Target target = new ObjectAdapter();
        target.request();
    }
}
