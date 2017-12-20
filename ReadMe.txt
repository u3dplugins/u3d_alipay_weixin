1.插件实现了移动手机端的android支付
2.分两种支付，一是支付宝支付，二是微信支付
3.实现了U3D与java之间的交互的桥SDKPlugin里面的函数，只要继承AbsU3DListener即可
4.SKDPlugin是处理U3D与java的桥梁，里面还包含了U3D方面的C#代码的逻辑实现。

alipay 支付宝的接入：alipay下面的所有文件+extend下面的PluginAlipay

微信接入
com.weixin下面是的是服务器需要实现的代码(统一下单逻辑，非plugin内容)
并将这个类放入到相应的位置,放开
消息回调类必须是：应用程序的包名.wxapi.WXPayEntryActivity
WXPayEntryActivity 实现了 IWXAPIEventHandler 
必须继承 android.app.Activity 才能实现消息监听机制

如果需要展示支付消息的话
就需要将
工程导成Android工程，拷贝WXPayEntryActivity 到相应的位置，并将res拷贝到响应的位置，代码取消注释 setContentView(R.layout.pay_result);
修改 onResp 去相应展示

res文件夹: 微信支付消息的界面资源

AndroidManifest.xml中要添加

必填的是：
<activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop"/>
            
可以不用添加的
intent-filter添加到action.MAIN的那个并列
<!-- 微信支付 -->
	  <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:scheme="wx20652a715bba49fa"/>
	  </intent-filter>
	  
微信App开发支付流程：
1 . 注册微信公众号，
2 . 成为开发者
此链接是如何成为开发者
http://jingyan.baidu.com/article/25648fc1df68c29191fd00db.html

下面这个链接是如何配置android,ios的微信应用
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5

以下是支付请求流程:
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1