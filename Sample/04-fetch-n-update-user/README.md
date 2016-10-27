# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Fetch User
If you need to fetch data on a current user with the latest data that is in the cloud, you can call the fetchAsync method like so:

```java
MesosferUser user = MesosferUser.getCurrentUser();
user.fetchAsync(new GetCallback<MesosferUser>() {
    @Override
    public void done(MesosferUser mesosferUser, MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
        
        // fetch user data succeeded
    }
});
```

This will automatically update `currentUser` with the latest data from cloud.

## Update User
After logged in, you can update your data that stored in cloud using method `updateDataAsync`.

```java
MesosferUser user = MesosferUser.getCurrentUser();
// set default field
user.setFirstName("updatedFirstname");
user.setLastName("updatedLastname");
// set custom field
user.setData("dateOfBirth", new Date());
user.setData("height", 180.5);
user.setData("weight", 85);
user.setData("isMarried", false);
// execute update user
user.updateDataAsync(new SaveCallback() {
    @Override
    public void done(MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
        
        // update user data succeeded
    }
});
```

## Change Password
If you want to change your current password, use method `changePasswordAsync`:

```java
MesosferUser user = MesosferUser.getCurrentUser();
user.changePasswordAsync("oldPassword", "newPassword", new ChangePasswordCallback() {
    @Override
    public void done(MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
            
        // change password succeeded
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