#### 1 . 快速接入:
- Step 1. Add the JitPack repository to your build file <br>
Add it in your root build.gradle at the end of repositories:
    ```
    allprojects {
        repositories {
            maven { url 'https://www.jitpack.io' }
        }
    }
    ```
- Step 2. Add the dependency
    ```
    dependencies {
        implementation 'com.github.andnux:component:0.0.1'
        or
        implementation 'com.github.andnux.component:libpay:0.0.1'
        implementation 'com.github.andnux.component:libcompat:0.0.1'
    }
    ```
#### 2 . 组件说明:
- libpay:<br>
包含微信支付、支付宝支付、银行卡支付，方便开发者快速接入。<br>
注意：<br>
    - 微信支付：
        需要在你的报名下建立.wxapi.WXPayEntryActivity.java 如下：
         ```
         package top.andnux.component.wxapi; 
         import top.andnux.libpay.wxpay.PayEntryActivity;
         public class WXPayEntryActivity extends PayEntryActivity {
         
         }
         并配置AndroidManifest.xml
         <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop"/>
         ```
