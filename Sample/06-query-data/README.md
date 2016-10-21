# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Basic Query
In many cases, there is a condition that need to specify which `data`s you want to retrieve. The `MesosferQuery` offers different ways to retrieve a list of `data`s. 
The general pattern is to create a `MesosferQuery `, put conditions on it, and then retrieve a `List` of matching `MesosferData`s using the `findAsync` method with a `FindCallback`. For example, to retrieve `Beacon`s data with a `name`, use the `whereEqualTo` method to constrain the value for a key:

```java
MesosferQuery<MesosferData> query = MesosferData.getQuery("Beacon");
query.whereEqualTo("name", "Beacon One");
query.findAsync(new FindCallback<MesosferData>() {
    @Override
    public void done(List<MesosferData> list, MesosferException e) {
        if (e != null) {
            // exception happen, handle the message
            return;
        } 

        // found the beacons, show in listview
    }
});
```

## Query Constraint
There are several ways to put constraints on the datas found by a `MesosferQuery`. You can filter out datas with a particular key-value pair with `whereNotEqualTo`:

```java
query. whereNotEqualTo("name", "Beacon One");
```

You can give multiple constraints, and datas will only be in the results if they match all of the constraints. In other words, it’s like an AND of constraints.

```java
query.whereNotEqualTo("name", "Beacon One");
query.whereGreaterThan("major", 1);
```

You can limit the number of results with `setLimit`. By default, results are limited to 100, but anything from 1 to 1000 is a valid limit:

```java
query.setLimit(20); // limit to at most 20 results
```

You can skip the first results with `setSkip`. This can be useful for pagination:

```java
query.setSkip(10); // skip the first 10 results
```

For sortable types like numbers and strings, you can control the order in which results are returned:

```java
// Sorts the results in ascending order by the beacon's major
query.orderByAscending("major");

// Sorts the results in descending order by the beacon's minor
query.orderByDescending("minor");
```

You can add more sort keys to the query as follows:

```java
// Sorts the results in ascending order by the beacon's major field if the previous sort keys are equal.
query.addAscendingOrder("major");

// Sorts the results in descending order by the beacon's minor field if the previous sort keys are equal.
query.addDescendingOrder("minor");
```

For sortable types, you can also use comparisons in queries:

```java
// Restricts to major < 123
query.whereLessThan("major", 123);

// Restricts to major <= 123
query.whereLessThanOrEqualTo("major", 123);

// Restricts to major > 123
query.whereGreaterThan("major", 123);

// Restricts to major >= 123
query.whereGreaterThanOrEqualTo("major", 123);
```

## Query on String
Use `whereStartsWith` to restrict to string values that start with a particular string. Similar to a MySQL LIKE operator, this is indexed so it is efficient for large datasets:

```java
// Finds beacon's name that start with 'Beacon'.
MesosferQuery<MesosferData> query = MesosferData.getQuery("Beacon);
query.whereStartsWith("name", "Beacon");
```

The above example will match any `MesosferData` where the value in the `name` String key starts with “Beacon”. For example, both “Beacon One” and “Beacon Two” will match, but “First Beacon” or “Second Beacon” will not.

Use `whereEndsWith` to restrict to string values that end with a particular string. 

```java
// Finds beacon's name that end with 'One'.
MesosferQuery<MesosferData> query = MesosferData.getQuery("Beacon);
query.whereEndsWith("name", "One");
```

The above example will match any `MesosferData` where the value in the `name` String key ends with “One”. For example, “Beacon One” will match, but “One Beacon” will not.

## Counting
If you just need to count how many datas match a query, but you do not need to retrieve all the datas that match, you can use `count` instead of `find`. For example, to count how many beacons have `major` greater than 123:

```java
MesosferQuery<MesosferData> query = MesosferData.getQuery("Beacon);
query.whereGreaterThan("major", 123);
query.countAsync(new CountCallback<MesosferData>() {
    @Override
    public void done(int count, MesosferException e) {
        if (e != null) {
            // exception happen, handle the message
            return;
        } 

        // counting succeeded, show beacons count
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