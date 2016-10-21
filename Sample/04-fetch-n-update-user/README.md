# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Session User
It would be bothersome if the user had to login every time they open your app. You can avoid this by using the cached `currentUser` object.

Whenever you use any `register` or `login` methods, the user is cached on disk. You can treat this cache as a session, and automatically assume the user is logged in:

```java
MesosferUser user = MesosferUser.getCurrentUser();
if (user != null) {
    // user logged in, open main activity
} else {
    // session not found, open login activity
}
```
## Log In User
After you allow users to register, you need be able to let them login to their account in the future. To do this, you can use the class method `logInAsync`.

```java
MesosferUser.logInAsync("myUsername", "myEncryptedPassword", new LogInCallback() {
    @Override
    public void done(MesosferUser user, MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
        
        // log in succeeded
    }
});
```

## Log Out User
You can clear the current user by logging them out:

```java
MesosferUser.logOutAsync(new LogOutCallback() {
    @Override
    public void done(MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
            
        // log out succeeded
    }
});
```

## License
    Copyright (c) 2016, Mesosfer.
    All rights reserved.

    This source code is licensed under the BSD-style license found in the
    LICENSE file in the root directory of this source tree.

[mesosfer.com]:https://mesosfer.com
[docs]:https://docs.mesosfer.com/
[cloud]:https://cloud.mesosfer.com/
[library]:../../Library/MesosferSDK-Android-0.1.0.aar