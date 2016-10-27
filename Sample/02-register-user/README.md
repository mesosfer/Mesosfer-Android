# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Register User
The first thing your app will do is probably ask the user to register. The following code illustrates a typical register:

```java
// create new instance of Mesosfer User
MesosferUser newUser = MesosferUser.createUser();

// set default field
newUser.setEmail("user.one@mesosfer.com");
newUser.setPassword("user1234");
newUser.setFirstName("User");
newUser.setLastName("One");

// set custom field
newUser.setData("dateOfBirth", new Date());
newUser.setData("height", 177.5);
newUser.setData("weight", 78);
newUser.setData("isMarried", true);
newUser.setData("myObject", new JSONObject());
newUser.setData("myArray", new JSONArray());

// execute register user asynchronous
newUser.registerAsync(new RegisterCallback() {
    @Override
    public void done(MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
        
        // register succeeded
    }
});
```

This call will `asynchronously` create a new user in your Mesosfer App. Before it does this, it checks to make sure that the `email` are unique. Also, it securely hashes the `password` in the cloud using bcrypt. We never store passwords in plaintext, nor will we ever transmit passwords back to the client in plaintext.

If a register isn’t successful, you should read the `MesosferException` that is returned. The most likely case is that the `email` has already been taken by another user. You should clearly communicate this to your users, and ask them try a different `email`.

## License
    Copyright (c) 2016, Mesosfer.
    All rights reserved.

    This source code is licensed under the BSD-style license found in the
    LICENSE file in the root directory of this source tree.

[mesosfer.com]:https://mesosfer.com
[docs]:https://docs.mesosfer.com/
[cloud]:https://cloud.mesosfer.com/
[library]:../../Library/MesosferSDK-Android-0.1.0.aar