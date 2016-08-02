# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\zsx\AndroidSdk1/tools/proguard/proguard-android.txt
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

# keep framework class
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.app.backup.BackupAgent

# keep DIY views
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 对于R(资源)下的所有类及其方法，都不能被混淆
-keep class net.sxkeji.androiddevartiestpritice.R$*{
    *;
}

# 为了避免出现无法解析的情况,可以直接不混淆JSON对象所在的package.示例如下:
-keep class net.sxkeji.androiddevartiestpritice.beans.** {*;}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclasses interface * {
}

# 代码中使用了反射和注解需要增加如下规则:
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留枚举类的values和valueOf方法.因为java通过反射来调用它们,
# 所以如果被混淆,会导致ClassNotFoundException.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留Parcelable对象的static CREATOR域,这是Android用来反序列化对象的.
# 由于CREATOR是在运行时被调用,所以如果不加规则,ProGuard会把它当成无用的成员直接去掉.
-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

# 为了能够正常序列化和反序列化,不混淆实现Serializable接口的类
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# 代码混淆压缩比，0~7，默认是5
# [优化最多递归的次数为5次]
-optimizationpasses 5

# 混淆时不使用大小写混合，类名用小写
# [在一个不区分大小写的操作系统平台上(例如Window),使用大小写混淆的混淆类名可能
# 导致不同的class相互覆盖,从而crash]
-dontusemixedcaseclassnames

# 混淆时不忽略非 public 的类和成员
# [Proguard 默认不混淆非 public 的，打开这个选项更不易破解]
# -dontskipnonpubliclibraryclasses
# -dontskipnonpubliclibraryclassmembers

# Android 不需要预校验
# [preverify 是 proguard 混淆的4个步骤之一， Android 不需要，去掉可加快混淆速度]
-dontpreverify

# 有了 verbose, 混淆后会生成原类名与混淆类名的映射文件
-verbose
# 指定映射文件名称
-printmapping map.txt

# 抛出异常时保留行号
-keepattributes SourceFile, LineNumberTable

# 指定混淆使用的算法，这个算法是谷歌推荐，一般不需要改变
-optimizations !code/simplification/arithmetic,!field/,!class/merging/