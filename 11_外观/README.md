
- [思考](#思考)
- [解决](#解决)
- [结构](#结构)
- [类比真实世界](#类比真实世界)
- [java伪代码](#java伪代码)
- [应用场景](#应用场景)
- [实现](#实现)
- [优缺点](#优缺点)


# 思考
在代码中使用某个复杂的库或框架中的众多对象。 正常情况下， 你需要负责所有对象的初始化工作、 管理其依赖关系并按正确的顺序执行方法等。

最终， 程序中类的业务逻辑将与第三方类的实现细节紧密耦合， 使得理解和维护代码的工作很难进行。


# 解决
外观类为包含许多活动部件的复杂子系统提供一个简单的接口，直接调用子系统相比， 外观提供的功能可能比较有限， 但它却包含了客户端真正关心的功能。

如果你的程序需要与包含几十种功能的复杂库整合， 但只需使用其中非常少的功能， 那么使用外观模式会非常方便
# 类比真实世界
通过电话给商店下达订单时， 接线员就是该商店的所有服务和部门的外观。 接线员为你提供了一个同购物系统、 支付网关和各种送货服务进行互动的简单语音接口


# 结构

![image](https://user-images.githubusercontent.com/83335903/224525425-f775433c-e4d0-4543-ac53-a57607a2ab2f.png)

1. 外观 （Facade） 提供了一种访问特定子系统功能的便捷方式， 其了解如何重定向客户端请求， 知晓如何操作一切活动部件。

2. 创建附加外观 （Additional Facade） 类可以避免多种不相关的功能污染单一外观， 使其变成又一个复杂结构。 客户端和其他外观都可使用附加外观。

3. 复杂子系统 （Complex Subsystem） 由数十个不同对象构成。 如果要用这些对象完成有意义的工作， 你必须深入了解子系统的实现细节， 比如按照正确顺序初始化对象和为其提供正确格式的数据。

4. 子系统类不会意识到外观的存在， 它们在系统内运作并且相互之间可直接进行交互。

5. 客户端 （Client） 使用外观代替对子系统对象的直接调用



# java伪代码
在本例中， 外观模式简化了客户端与复杂视频转换框架之间的交互。

![image](https://user-images.githubusercontent.com/83335903/224525486-239e3630-8a74-4b5f-bcbc-5bce40eb648c.png)


可以创建一个封装所需功能并隐藏其他代码的外观类， 从而无需使全部代码直接与数十个框架类进行交互。 该结构还能将未来框架升级或更换所造成的影响最小化， 因为你只需修改程序中外观方法的实现即可

```
// 这里有复杂第三方视频转换框架中的一些类。我们不知晓其中的代码，因此无法
// 对其进行简化。

class VideoFile
// ……

class OggCompressionCodec
// ……

class MPEG4CompressionCodec
// ……

class CodecFactory
// ……

class BitrateReader
// ……

class AudioMixer
// ……


// 为了将框架的复杂性隐藏在一个简单接口背后，我们创建了一个外观类。它是在
// 功能性和简洁性之间做出的权衡。
class VideoConverter is
    method convert(filename, format):File is
        file = new VideoFile(filename)
        sourceCodec = (new CodecFactory).extract(file)
        if (format == "mp4")
            destinationCodec = new MPEG4CompressionCodec()
        else
            destinationCodec = new OggCompressionCodec()
        buffer = BitrateReader.read(filename, sourceCodec)
        result = BitrateReader.convert(buffer, destinationCodec)
        result = (new AudioMixer()).fix(result)
        return new File(result)

// 应用程序的类并不依赖于复杂框架中成千上万的类。同样，如果你决定更换框架，
// 那只需重写外观类即可。
class Application is
    method main() is
        convertor = new VideoConverter()
        mp4 = convertor.convert("funny-cats-video.ogg", "mp4")
        mp4.save()
```

# 应用场景
1. 如果你需要一个指向复杂子系统的直接接口， 且该接口的功能有限， 则可以使用外观模式。
2. 如果需要将子系统组织为多层结构， 可以使用外观
