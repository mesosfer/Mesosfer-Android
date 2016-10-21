# Mesosfer Starter Project for Android #


A library that gives you access to the powerful Mesosfer cloud platform from your Android app. 
For more information about Mesosfer and its features, see [Mesosfer Website][mesosfer.com] and [Mesosfer Documentations][docs].

## Basic Query User
In many cases, there is a condition that need to specify which users you want to retrieve. The `MesosferQuery` offers different ways to retrieve a list of users. 
The general pattern is to create a `MesosferQuery `, put conditions on it, and then retrieve a `List` of matching `MesosferUser`s using the `findAsync` method with a `FindCallback`. For example, to retrieve users with a firstname, use the `whereEqualTo` method to constrain the value for a key:

```java
MesosferQuery<MesosferUser> query = MesosferUser.getQuery();
query.whereEqualTo("firstname", "John");
query.findAsync(new FindCallback<MesosferUser>() {
    @Override
    public void done(List<MesosferUser> list, MesosferException e) {
        if (e != null) {
            // exception happen, handle the message
            return;
        } 

        // found the users, show in listview
    }
});
```

## Query Constraint
There are several ways to put constraints on the objects found by a `MesosferQuery`. You can filter out users with a particular key-value pair with `whereNotEqualTo`:

```java
query.whereNotEqualTo("firstname", "John");
```

You can give multiple constraints, and userss will only be in the results if they match all of the constraints. In other words, it’s like an AND of constraints.

```java
query.whereNotEqualTo("firstname", "John");
query.whereGreaterThan("height", 170);
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
// Sorts the results in ascending order by the user height
query.orderByAscending("height");

// Sorts the results in descending order by the user height
query.orderByDescending("height");
```

You can add more sort keys to the query as follows:

```java
// Sorts the results in ascending order by the user height field if the previous sort keys are equal.
query.addAscendingOrder("height");

// Sorts the results in descending order by the user weight field if the previous sort keys are equal.
query.addDescendingOrder("weight");
```

For sortable types, you can also use comparisons in queries:

```java
// Restricts to height < 170
query.whereLessThan("height", 170);

// Restricts to height <= 170
query.whereLessThanOrEqualTo("height", 170);

// Restricts to height > 170
query.whereGreaterThan("height", 170);

// Restricts to height >= 170
query.whereGreaterThanOrEqualTo("height", 170);
```

## Query on String
Use `whereStartsWith` to restrict to string values that start with a particular string. Similar to a MySQL LIKE operator, this is indexed so it is efficient for large datasets:

```java
// Finds user email that start with 'john'.
MesosferQuery<MesosferUser> query = MesosferUser.getQuery();
query.whereStartsWith("email", "john");
```

The above example will match any `MesosferUser` where the value in the `email` String key starts with “john”. For example, both “john.doe@nomail.com” and “john.cena@nomail.com” will match, but “big.john@okmail.com” or “little.john@okmail.com” will not.

Use `whereEndsWith` to restrict to string values that end with a particular string. 

```java
// Finds user email that end with '@okmail.com'.
MesosferQuery<MesosferUser> query = MesosferUser.getQuery();
query.whereEndsWith("email", "@okmail.com");
```

The above example will match any `MesosferUser` where the value in the `email` String key ends with “@okmail.com”. For example, both “big.john@okmail.com” and “little.john@okmail.com” will match, but “john.doe@nomail.com” or “john.cena@nomail.com” will not.

## Counting
If you just need to count how many objects match a query, but you do not need to retrieve all the objects that match, you can use `count` instead of `find`. For example, to count how many users have height greater than 170 centimetres:

```java
MesosferQuery<MesosferUser> query = MesosferUser.getQuery();
query.whereGreaterThan("height", 170);
query.countAsync(new CountCallback<MesosferUser>() {
    @Override
    public void done(int count, MesosferException e) {
        if (e != null) {
            // exception happen, handle the message
            return;
        } 

        // counting succeeded, show users count
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