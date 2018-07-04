# Channel SDK for Android

 Channel SDK is a library designed to simplify the development with our API.
 
 ## How to use Channel SDK in your Android app
 
 #### 1. Download our SDK from [Release](https://github.com/mogohichi/channel-android/releases) page
 
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
    implementation (name: 'channel', ext:'aar')
    implementation 'com.github.jkwiecien:EasyImage:1.3.1'
    implementation 'com.afollestad.material-dialogs:core:0.9.0.2'
    implementation "com.google.android:flexbox:0.2.5"
    implementation "com.android.support:appcompat-v7:26.0.0-alpha1"
    implementation "com.android.support:cardview-v7:26.0.0-alpha1"
    implementation "com.android.support:design:26.0.0-alpha1"
    implementation "com.github.siyamed:android-shape-imageview:0.9.3"
    implementation "me.relex:circleindicator:1.2.2@aar"
```

#### 5. Setup Channel application key in your main activity
 ```java
  Channel.setupApplicationContextWithApplicationKey(<Your Application Context>, new ChannelCallback() {
            @Override
            public void onSuccess() {
				//place your code when success
            }

            @Override
            public void onFail(String message) {
				//Handle error with fail message
            }
        });
 ```
 
 #### 5. This is informations when opening the Channel chatview is required
 ```java
 HashMap<String,String> yourClientData =  new HashMap<String,String>();
 yourClientData.put("name","YOUR CLIENT NAME");
 yourClientData.put("lastname","YOUR CLIENT LASTNAME");
 Channel.chatViewWithUserID(<Your Activity>,"YOUR_APP_USER_ID", userData);
 ```
 or
  ```java
 Channel.chatView(<Your Activity>);
 ```
 
#### 6. Show Channel in-app notification
 ```java
 //send display target Activity
 Channel.showLatestNotification(activity, new ChannelCallback() {
            @Override
            public void onSuccess() {
				//place your code when success
            }

            @Override
            public void onFail(String message) {
				//Handle error with fail message
            }
        });
 ```
 
#### 7. Register device token 
 ```java
 Channel.saveDeviceToken("DEVICE_TOKEN", new ChannelCallback() {
            @Override
            public void onSuccess() {
				//place your code when success
            }

            @Override
            public void onFail(String message) {
				//Handle error with fail message
            }
        });
 ```
 

#### 8. Send Firebase message to Channel for notification status tracking 
 ```java
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
   Channel.postbackPushNotification(remoteMessage.getData(), new ChannelCallback() {
            @Override
            public void onSuccess() {
                //place your code when success
            }

            @Override
            public void onFail(String message) {
				//Handle error with fail message
            }
        });
    }
}
 ```
 
 
