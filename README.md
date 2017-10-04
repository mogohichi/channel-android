# Channel SDK for Android

[ ![Download Here]() ](https://s3-us-west-2.amazonaws.com/co.getchannel.builds/android-sdk/channel.aar)

 Channel SDK is a library designed to simplify the development with our API.
 
 ## How to use Channel SDK in your Android app
 
 #### 1. Dowload our SDK to your library 
 
 #### 2. Add maven repositories and config library directory in your project build.gradle 
   ```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io"} // maven repository
        flatDir{dirs 'libs'} //direct path to your library folder
    }
}
```

 #### 3. Add dependencies to your project build.gradle
  ```gradle
        classpath 'com.github.dcendents:android-maven-gradle-plugin:1.3'
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.7.1'
```

#### 4. Add code below to your module build.gradle
 ```gradle
    compile (name: 'channel', ext:'aar')
    compile 'com.github.jkwiecien:EasyImage:1.3.1'
    compile 'com.afollestad.material-dialogs:core:0.9.0.2'
    compile "com.google.android:flexbox:0.2.5"
    compile "com.android.support:appcompat-v7:26.0.0-alpha1"
    compile "com.android.support:cardview-v7:26.0.0-alpha1"
    compile "com.android.support:design:26.0.0-alpha1"
    compile "com.github.siyamed:android-shape-imageview:0.9.3"
    compile "me.relex:circleindicator:1.2.2@aar"
```

#### 5. Setup Channel application key in your main activity
 ```java
  Channel.setupActivityWithApplicationKey(new WeakReference<Activity>((Activity)this),"YOUR_APPLICAION_KEY");
 ```
 
 #### 5. This is informations when opening the Channel chatview is required
 ```java
                HashMap<String,String> yourClientData =  new HashMap<String,String>();
                yourClientData.put("name","YOUR CLIENT NAME");
                yourClientData.put("lastname","YOUR CLIENT LASTNAME");
                Channel.chatViewWithUserID("YOUR_APP_USER_ID", userData);
 ```
 
#### 6. Show Channel in-app notification
 ```java
                //send display target Activity
                Channel.showLatestNotification(activity);
 ```
 
#### 7. Register device token 
 ```java
                Channel.saveDeviceToken("DEVICE_TOKEN");
 ```
 

#### 8. Send Firebase message to Channel for notification status tracking 
 ```java
   public class MyFirebaseMessagingService extends FirebaseMessagingService {
       @Override
       public void onMessageReceived(RemoteMessage remoteMessage) {
           CHClient.currentClient().postbackPushNotification(remoteMessage.getData());
       }
   }
 ```
 
 
