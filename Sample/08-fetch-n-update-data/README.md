# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Fetching Data
If you need to fetch a data with the latest data that is in the cloud, you can call the `fetchAsync` method like so:

```java
// create data from existing objecId
MesosferData data = MesosferData.createWithObjectId("objectId");
// fetching the data
user.fetchAsync(new GetCallback<MesosferUser>() {
    @Override
    public void done(MesosferData data, MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }
        
        // fetch data succeeded
    }
});
```

This will automatically update data with the latest data from cloud.

## Updating Data
After getting the data, you can update your data that stored in cloud using method `saveAsync`.

```java
MesosferData data; // fetched data
// set data
data.setData("name", "Beacon Two");
data.setData("proximityUUID", "CB10023F-A318-3394-4199-A8730C7C1AEC");
data.setData("major", 2);
data.setData("minor", 284);
data.setData("isActive", false);
data.setData("timestamp", new Date());
// execute save data
data.saveAsync(new SaveCallback() {
    @Override
    public void done(MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }

        // data updated, show success message
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