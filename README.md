# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Download
1. Download [the latest AAR][library] and copy it on `libs` directory.
2. Define in your `app` module `build.gradle` this code below before `dependencies`

```groovy
repositories{
    flatDir {
        dirs 'libs'
    }
}
```

Then add this code below on `dependencies` :

```groovy
compile 'com.eyro.mesosfer:MesosferSDK-Android:0.1.0@aar'
```

## Setup
1. Register first to [Mesosfer Cloud][cloud]
2. Create an application to get `applicationId` and `clientKey`
3. Add this line below to your `Application` class to initialize Mesosfer SDK

```java 
Mesosfer.initialize(this, "YOUR-APPLICATION-ID-HERE", "YOUR-CLIENT-KEY-HERE");
```
Don't forget to initialize your application class to `AndroidManifest.xml`  

(Optional) You can add some custom setup :

* Enable Mesosfer Push Notification by calling `Mesosfer.setPushNotification(boolean)`
* Enable Mesosfer SDK debug logging by calling `Mesosfer.setLogLevel(int);` before initialize SDK.
* Mesosfer Log Level Mode : `LOG_LEVEL_VERBOSE`, `LOG_LEVEL_DEBUG`, `LOG_LEVEL_INFO`, `LOG_LEVEL_WARNING`, `LOG_LEVEL_ERROR`, `LOG_LEVEL_NONE`

Everything is done!

## License
    Copyright (c) 2016, Mesosfer.
    All rights reserved.

    This source code is licensed under the BSD-style license found in the
    LICENSE file in the root directory of this source tree.

[mesosfer.com]:https://mesosfer.com
[docs]:https://docs.mesosfer.com/
[cloud]:https://cloud.mesosfer.com/
[library]:Library/MesosferSDK-Android-0.1.0.aar