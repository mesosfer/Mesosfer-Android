# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Mesosfer Data
Storing data on Mesosfer is built around the `MesosferData`. Each `MesosferData` contains key-value pairs of JSON-compatible data. This data is using schema, which means that you need to specify ahead of time what keys exist on each `MesosferData` from our [Mesosfer Cloud][cloud]. Then you can simply set a key-value pairs you want to save, and our backend will store it.

For example, let's say you're set a Beacon parameters. A single `MesosferData` could contain :

```json
"isActive":true, "major":1, "name":"Beacon One", "minor":284, "proximityUUID":"CB10023F-A318-3394-4199-A8730C7C1AEC"
```

Keys must be alphanumeric strings. Values can be `String`s, `Number`s, `Date`s, `Boolean`s, or even `Array`s and `Object`s - anything that can be JSON-encoded.


## Saving data
Let’s say you want to save the Beacon described above to the [Mesosfer Cloud][cloud]. The interface is similar to a Map, plus the `saveAsync` method:

```java
MesosferData data = MesosferData.createData("Beacon");
// set data
data.setData("name", "Beacon One");
data.setData("proximityUUID", "CB10023F-A318-3394-4199-A8730C7C1AEC");
data.setData("major", 1);
data.setData("minor", 284);
data.setData("isActive", true);
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

        // data saved, show success message
    }
});
```

## Deleting Data
To delete a data from the Mesosfer Cloud, use method `deleteAsync`:

```java
MesosferData data;
data.deleteAsync(new DeleteCallback() {
    @Override
    public void done(MesosferException e) {
        // check if there is an exception happen
        if (e != null) {
            // handle the exception
            return;
        }

        // data deleted successfully
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