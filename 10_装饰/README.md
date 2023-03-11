
- [思考](#思考)
- [解决](#解决)
- [结构](#结构)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)
- [实现](#实现)
- [优缺点](#优缺点)


# 思考
正在开发一个提供通知功能的库， 其他程序可使用它向用户发送关于事件通知。

库的最初版本基于通知器Notifier类， 其中只有很少的几个成员变量， 一个构造函数和一个send发送方法。 该方法可以接收来自客户端的消息参数， 并将该消息发送给一系列的邮箱， 邮箱列表则是通过构造函数传递给通知器的。 作为客户端的第三方程序仅会创建和配置通知器对象一次， 然后在有重要事件发生时对其进行调用

![image](https://user-images.githubusercontent.com/83335903/224491880-095b4d03-3773-414d-ad8f-b2e77aa08385.png)

此后加功能：用户会希望接收关于紧急事件的手机短信， 还有些用户希望在微信上接收消息， 而公司用户则希望在 QQ 上接收消息

![image](https://user-images.githubusercontent.com/83335903/224491905-88dcdfbd-4582-4be6-91f0-bfa59cb63c96.png)

首先扩展通知器类， 然后在新的子类中加入额外的通知方法。 现在客户端要对所需通知形式的对应类进行初始化， 然后使用该类发送后续所有的通知消息

又加需求：同时使用多种通知形式

![image](https://user-images.githubusercontent.com/83335903/224491956-5e425f23-6813-4c50-bf46-8f5621310de1.png)



# 解决
我们可以将简单邮件通知行为放在`基类通知器`中， 但将所有其他通知方法放入装饰中。

![image](https://user-images.githubusercontent.com/83335903/224492022-675c05d7-0985-409c-9f18-3cb4361eb4ba.png)

客户端代码必须将基础通知器放入一系列自己所需的装饰中。 因此最后的对象将形成一个栈结构。

![image](https://user-images.githubusercontent.com/83335903/224492039-93497be0-6d7e-4119-a545-25e70a009177.png)

实际与客户端进行交互的对象将是最后一个进入栈中的装饰对象。 由于所有的装饰都实现了与通知基类相同的接口， 客户端的其他代码并不在意自己到底是与 “纯粹” 的通知器对象， 还是与装饰后的通知器对象进行交互。

我们可以使用相同方法来完成其他行为 （例如设置消息格式或者创建接收人列表）。 只要所有装饰都遵循相同的接口， 客户端就可以使用任意自定义的装饰来装饰对象

真实世界类比：穿衣服是使用装饰的一个例子。 觉得冷时， 你可以穿一件毛衣。 如果穿毛衣还觉得冷， 你可以再套上一件夹克。 如果遇到下雨， 你还可以再穿一件雨衣。 所有这些衣物都 “扩展” 了你的基本行为， 但它们并不是你的一部分， 如果你不再需要某件衣物， 可以方便地随时脱掉

![image](https://user-images.githubusercontent.com/83335903/224492082-ab806d91-789a-41d3-ad9b-36012219b1f9.png)



# 结构

![image](https://user-images.githubusercontent.com/83335903/224492089-e5e4d0fc-9612-4ee4-9a67-96ba1d43a55e.png)

1. 部件 （Component） 声明封装器和被封装对象的公用接口。

2. 具体部件 （Concrete Component） 类是被封装对象所属的类。 它定义了基础行为， 但装饰类可以改变这些行为。

3. 基础装饰 （Base Decorator） 类拥有一个指向被封装对象的引用成员变量。 该变量的类型应当被声明为通用部件接口， 这样它就可以引用具体的部件和装饰。 装饰基类会将所有操作委派给被封装的对象。

4. 具体装饰类 （Concrete Decorators） 定义了可动态添加到部件的额外行为。 具体装饰类会重写装饰基类的方法， 并在调用父类方法之前或之后进行额外的行为。

5. 客户端 （Client） 可以使用多层装饰来封装部件， 只要它能使用通用接口与所有对象互动即可

# java伪代码
例子：有个简单的视频播放功能，希望在简单的视频播放功能基础上，加上美颜、贴图等增强功能
新增接口，提供播放服务
```
public interface VideoService {
    void play();//播放视频
}
```
新增VideoServiceImpl实现上面接口
```
@Service
public class VideoServiceImpl implements VideoService {
    @Override
    public void play() {
        System.out.println("播放视频");
    }
}
```
新增测试Controller，调用该接口
```
@RestController
public class VideoController {

    @Resource(name = "videoService")
    VideoService videoService;

    @GetMapping("/default_video")
    public String defaultVideo(String decorate){
        videoService.play();
        return "原有的播放逻辑";
    }

}
```
现在需要在播放视频的基础上，加上特效如美颜、贴图，但是不要影响到原本的功能使用，可选择的给视频加上美颜、贴图两个功能
新增视频装饰器抽象基类，实现了VideoService
```
public abstract class VideoDecorator implements VideoService {
    private VideoService videoService;

    public VideoDecorator(VideoService videoService) {
        this.videoService = videoService;
    }
    @Override
    public void play() {
        this.videoService.play(); //播放视频
    }
}
```
给这个装饰器基类加上扩展功能，新增两个装饰器：美颜装饰、贴图装饰
```
public class VideoDecoratorA extends VideoDecorator {

    public VideoDecoratorA(VideoService videoService) {
        super(videoService);
    }

    /**
     * 加强的逻辑：加上美颜
     */
    public void decorate(){
        System.out.println("装饰：加上美颜");
    }
    @Override
    public void play(){
        this.decorate();
        super.play();
    }
}

public class VideoDecoratorB extends VideoDecorator {

    public VideoDecoratorB(VideoService videoService) {
        super(videoService);
    }

    /**
     * 加强的逻辑：加上贴图
      */
    public void decorate(){
        System.out.println("装饰：加上卡通贴图");
    }
    @Override
    public void play(){
        this.decorate();
        super.play(); //原有的视频播放逻辑
    }
}
。
```
创建Bean
```
@Configuration
public class decorateConfig {

    @Bean("videoService")
    VideoService videoService(){
        return new VideoServiceImpl();
    }
    @Bean("decoratorA")
    VideoDecoratorA videoDecoratorA(@Qualifier("videoService")  VideoService videoService){
        return new VideoDecoratorA(videoService);
    }

    @Bean("decoratorB")
    VideoDecoratorB videoDecoratorB(@Qualifier("videoService")  VideoService videoService){
        return new VideoDecoratorB(videoService);
    }
}

```
改下Controller，测试功能
```
@RestController
public class VideoController {

    @Resource(name = "videoService")
    VideoService videoService;

    @Resource(name = "decoratorA")
    VideoService videoDecoratorA; //加上了滤镜的装饰类

    @Resource(name = "decoratorB")
    VideoService videoDecoratorB; //加上了贴图的装饰类

    @GetMapping("/video")
    public String video(String decorate){

        if("1".equals(decorate)){
            videoDecoratorA.play();
            return "装饰者模式：播放视频-加上美颜滤镜";

        }else{
            videoDecoratorB.play();
            return "装饰者模式：播放视频-加上卡通贴图";

        }
    }

    @GetMapping("/default_video")
    public String defaultVideo(String decorate){
        videoService.play();
        return "原有的播放逻辑";
    }


}
```


# 应用场景
1. 如果你希望在无需修改代码的情况下即可使用对象， 且希望在运行时为对象新增额外的行为， 可以使用装饰模式
2.  如果用继承来扩展对象行为的方案难以实现或者根本不可行， 你可以使用该模式


# 优缺点
1. 你无需创建新子类即可扩展对象的行为
2. 你可以在运行时添加或删除对象的功能。
3. 你可以用多个装饰封装对象来组合几种行为。
4. 单一职责原则：可以将实现了许多不同行为的一个大类拆分为多个较小的类


