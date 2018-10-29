# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /software/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
###-----------基本配置-不能被混淆的------------

#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
-ignorewarnings

#优化时允许访问并修改有修饰符的类和类的成员
#-allowaccessmodification
#指定重新打包,所有包重命名,这个选项会进一步模糊包名
#-flattenpackagehierarchy a.b.c.d.e.f.g.
#将包里的类混淆成n个再重新打包到一个统一的package中会覆盖flattenpackagehierarchy选项
#-repackageclass *

#竭力合并接口，即使它们的实现类未实现合并后接口的所有方法。
#-mergeinterfacesaggressively
#混淆类名、属性名、方法名、变量名等，变成无意义的类似a,b,c,d...的名字
#-dontobfuscate


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
#
##support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**    # 忽略警告

#忽略小米华为
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}



#保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}
#保持Serializable实现类不被混淆
-keepnames class * implements java.io.Serializable
#保持Serializable不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举enum类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#onClick方法
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#不混淆本地方法
-keepclasseswithmembernames class * {
native <methods>;
}
#不混淆反射方法

#WebView
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
###-----------第三方jar包library混淆配置------------
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#GSON
#-keepattributes Signature-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
# Application classes that will be serialized/deserialized over Gson 下面替换成自己的实体类

#OKHTTP3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#GreenDao
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.**{*;}
-keep interface net.sqlcipher.**{*;}
#GeeenDao 自动生成代码
-keep class com.networkengine.database.table.**{*;}
#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.**{*;}
-keepattributes Signature
-keepattributes Exceptions

#Glid
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#Pinyin4
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
#-libraryjars libs/pinyin4j-2.5.0.jar
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}

#org.apache.http.legacy.
#MsgSyncLibrary
-dontwarn org.apache.**
-dontwarn android.net.**
-dontwarn com.android.internal.http.multipart.**
-keep class org.apache.**{*;}
-keep class android.net.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.eclipse.paho.android.service.**{*;}
-keep class org.eclipse.paho.client.mqttv3.**{*;}
#沙箱
-keep class com.aes.**{*;}
-keep class com.coracle.box.**{*;}
-keep class com.esotericsoftware.**{*;}
-keep class com.example.nosql2.**{*;}
-keep class de.javakaffee.kryoserializers.**{*;}
-keep class org.objectweb.asm.**{*;}
-keep class org.objenesis.**{*;}

#高德地图
-dontwarn com.amap.**
-dontwarn com.autonavi.**
#3D 地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.trace.**{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep class com.amap.api.services.**{*;}
# 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#声网
-keep class io.agora.rtc.**{*;}
#com.esotericsoftware
-dontwarn com.esotericsoftware.kryo.**
-dontwarn com.haifei.**
-dontwarn com.hyphenate.**
-dontwarn net.sf.cglib.**
-dontwarn sun.reflect.**
-dontwarn org.joda.**
-dontwarn com.wbtech.ums.**
-keep class com.wbtech.ums.**{*;}
-keep class com.nostra13.universalimageloader.**{*;}
-keep class com.tencent.**{*;}

#友盟分享和登录
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keep public class com.umeng.socialize.* {*;}

-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
     *;
    }
-keep class com.tencent.mm.opensdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public <init>(org.json.JSONObject);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
       *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }

-keepattributes Signature

-keep public class [com.coracle.xsimple].R$*{
     public static final int *;
}
-keep public class [com.xsimple.im].R$*{
     public static final int *;
}

-keep public class com.umeng.fb.** { *;}
-keep public class com.umeng.fb.ui.ThreadView { }

-keep class cor.com.plugin.moduleimage.entity**{*;}

-keep class com.cor.moduleshare.share.util.**{*;}

# -keep public class com.xsimple.im.R$*{
#    public static final int *;
# }

#mob短信验证码sdk
 -dontwarn com.mob.**
 -dontwarn cn.smssdk.**
 -keep class com.mob.**{*;}
 -keep class cn.smssdk.**{*;}

 #腾讯bugly
 -dontwarn com.tencent.bugly.**
 -keep public class com.tencent.bugly.**{*;}

 #事件总线
 -keepattributes *Annotation*
 -keepclassmembers class ** {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
 -keep enum org.greenrobot.eventbus.ThreadMode { *; }

 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(Java.lang.Throwable);
 }

#路由组件
-keep  class com.cor.router.**{*;}
-keep interface * extends com.cor.router.RouterService{*;}
#jwt
-keep class io.jsonwebtoke

# 继承路由组件基类
-keep class * extends com.cor.router.CorComponentBase {*;}
-keep interface * extends com.cor.router.RouterService{*;}

#实体
-keep  class com.networkengine.entity.**{*;}

# EventBus
-keep class org.greenrobot.eventbus.**{*;}
-keep class * extends com.networkengine.event.BaseEventBusAction{*;}

#原生能力
-keep class com.coracle.js.**{*;}
