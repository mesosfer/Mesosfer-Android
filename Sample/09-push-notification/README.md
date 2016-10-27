# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Push Notification
Push notifications are a great way to keep your users engaged and informed about your app. You can reach your entire user base quickly and effectively. This guide will help you through the setup process and the general usage of Mesosfer to send push notifications.


## Setting Up Push
The Mesosfer library provides push notifications using Google Cloud Messaging (GCM) now Firebase Cloud Messaging (FCM) if Google Play Services are available. Learn more about Google Play Services [here][gcm].

When sending pushes to Android devices with GCM, there are several pieces of information that Mesosfer keeps track of automatically:

- **Registration ID**: The GCM registration ID uniquely identifies an app/device pairing for push purposes.
- **Sender ID**: The GCM sender ID is a public number that identifies the sender of a push notification.
- **API key**: The GCM API key is a server secret that allows a server to send pushes to a registration ID on behalf of a particular sender ID.

The Mesosfer Android SDK chooses a reasonable default configuration so that you do not have to worry about GCM registration ids, sender ids, or API keys. In particular, the SDK will automatically register your app for push at startup time using Mesosfer’s sender ID (523325046971) and will store the resulting registration ID in the deviceToken field of the app’s current `MesosferInstallation`.

## Receiving Pushes
When a push notification is received, the “title” is displayed in the status bar and the “alert” is displayed alongside the “title” when the user expands the notification drawer. If you choose to subclass `com.eyro.mesosfer.PushBroadcastReceiver`, be sure to replace that name with your `class` name in the registration.

Note that some Android emulators (the ones without Google API support) don’t support GCM, so if you test your app in an emulator make sure to select an emulator image that has Google APIs installed.

###CUSTOMIZING NOTIFICATIONS

Now that your app is all set up to receive push notifications, you can start customizing the display of these notifications.

###CUSTOMIZING NOTIFICATION ICONS

The [Android style guide][Android style guide] recommends apps use a push icon that is monochromatic and flat. The default push icon is your application’s launcher icon, which is unlikely to conform to the style guide. To provide a custom push icon, add the following metadata tag to your app’s `AndroidManifest.xml`:

```
<meta-data 
    android:name="com.eyro.mesosfer.push.notification_icon"
    android:resource="@drawable/push_icon"/>
```
…where `push_icon` is the name of a drawable resource in your package. If your application needs more than one small icon, you can override `getSmallIconId` in your `PushBroadcastReceiver` subclass.

If your push has a unique context associated with an image, such as the avatar of the user who sent a message, you can use a large push icon to call attention to the notification. When a notification has a large push icon, your app’s static (small) push icon is moved to the lower right corner of the notification and the large icon takes its place. See the [Android UI documentation][Android UI documentation] for examples. To provide a large icon, you can override `getLargeIcon` in your `PushBroadcastReceiver` subclass.

###RESPONDING WITH A CUSTOM ACTIVITY

If your push has no “uri” parameter, `onPushOpen` will invoke your application’s launcher activity. To customize this behavior, you can override `getActivity` in your `PushBroadcastReceiver` subclass.

###RESPONDING WITH A URI

If you provide a “uri” field in your push, the `PushBroadcastReceiver` will open that URI when the notification is opened. If there are multiple apps capable of opening the URI, a dialog will displayed for the user. The `PushBroadcastReceiver` will manage your back stack and ensure that clicking back from the Activity handling URI will navigate the user back to the activity returned by `getActivity`.

###MANAGING THE PUSH LIFECYCLE

The push lifecycle has three phases:

1. A notification is received and the `com.eyro.mesosfer.push.intent.OPEN` Intent is fired, causing the `PushBroadcastReceiver` to call `onPushReceive`. If either “alert” or “title” are specified in the push, then a Notification is constructed using `getNotification`. This Notification uses a small icon generated using `getSmallIconId`, which defaults to the icon specified by the `com.eyro.mesosfer.push.notification_icon` metadata in your `AndroidManifest.xml`. The Notification’s large icon is generated from `getLargeIcon` which defaults to null. The notification’s `contentIntent` and `deleteIntent` are `com.eyro.mesosfer.push.intent.OPEN` and `com.eyro.mesosfer.push.intent.DELETE` respectively.

2. If the user taps on a Notification, the `com.eyro.mesosfer.push.intent.OPEN` Intent is fired. The `PushBroadcastReceiver` calls `onPushOpen`. If the push contains a “uri” parameter, an activity is launched to navigate to that URI, otherwise the activity returned by `getActivity` is launched.

3. If the user dismisses a Notification, the `com.eyro.mesosfer.push.intent.DELETE` Intent is fired. The `PushBroadcastReceiver` calls `onPushDismiss`, which does nothing by default

All of the above methods may be subclassed to customize the way your application handles push notifications. When subclassing the methods `onPushReceive`, `onPushOpen`, `onPushDismiss`, or `getNotification`, consider delegating to `super` where appropriate. For example, one might override `onPushReceive` to trigger a background operation for “silent” pushes and then delegate to `super` for all other pushes. This provides the most benefit from Mesosfer Push and makes your code forward-compatible.

## License
    Copyright (c) 2016, Mesosfer.
    All rights reserved.

    This source code is licensed under the BSD-style license found in the
    LICENSE file in the root directory of this source tree.

[mesosfer.com]:https://mesosfer.com
[docs]:https://docs.mesosfer.com/
[cloud]:https://cloud.mesosfer.com/
[gcm]:https://developers.google.com/cloud-messaging/
[Android style guide]:https://www.google.com/design/spec/style/icons.html#notification
[Android UI documentation]:http://developer.android.com/guide/topics/ui/notifiers/notifications.html#NotificationUI
[library]:../../Library/MesosferSDK-Android-0.1.0.aar