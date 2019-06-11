# transit-cljs

Transit is a data format and a set of libraries for conveying values between
applications written in different languages. This library provides support for
marshalling Transit data to/from ClojureScript. Unlike the Java and Clojure
implementations it relies on the non-streaming JSON parsing mechanism of the
host JavaScript environment.

* [Rationale](http://blog.cognitect.com/blog/2014/7/22/transit)
* [API docs](http://cognitect.github.io/transit-cljs/)
* [Specification](http://github.com/cognitect/transit-format)
* [Getting Started](http://github.com/cognitect/transit-cljs/wiki/Getting-Started)

This implementation's major.minor version number corresponds to the version of
the Transit specification it supports.

_NOTE: Transit is intended primarily as a wire protocol for transferring data between applications. If storing Transit data durably, readers and writers are expected to use the same version of Transit and you are responsible for migrating/transforming/re-storing that data when and if the transit format changes._ 

## Releases and Dependency Information

* Latest release: 0.8.256
* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cognitect%22%20AND%20a%3A%22transit-cljs%22)

### Leiningen

Add the following to your `project.clj` `:dependencies`:

```
[com.cognitect/transit-cljs "0.8.256"]
```

### Maven

[Maven](http://maven.apache.org/) dependency information:

```xml
<dependency>
  <groupId>com.cognitect</groupId>
  <artifactId>transit-cljs</artifactId>
  <version>0.8.256</version>
</dependency>
```

## Usage

```clojure
(ns example
  (:require [cognitect.transit :as t]))

(defn roundtrip [x]
  (let [w (t/writer :json)
        r (t/reader :json)]
    (t/read r (t/write w x))))

(defn test-roundtrip []
  (let [list1 [:red :green :blue]
        list2 [:apple :pear :grape]
        data  {(t/integer 1) list1
               (t/integer 2) list2}
        data' (roundtrip data)]
    (assert (= data data'))))
```

## Default Type Mapping

Abbreviations:
* cc = cljs.core
* gm = goog.math
* cct = com.cognitect.transit

|Transit type|Write accepts|Read returns|
|------------|-------------|------------|
|null|null|null|
|string|String|String|
|boolean|Boolean|Boolean|
|integer|Number, gm.Long|Number, gm.Long|
|decimal|Number|Number|
|keyword|cc.Keyword|cc.Keyword|
|symbol|cc.Symbol|cc.Symbol|
|big integer|cct.BigInteger|cct.BigInteger|
|big decimal|cct.BigDecimal|cct.BigDecimal|
|time|Date|Date|
|uri|cct.URI|cct.URI|
|uuid|cct.UUID|cct.UUID|
|char|String|String|
|array|cc.IVector|cc.IVector|
|list|cc.IList|cc.IList|
|set|cc.ISet|cc.ISet|
|map|cc.IMap|cc.IMap|
|link|cct.Link|cct.Link|
|cmap|cct.IMap|cct.IMap|

## Contributing 

This library is open source, developed internally by Cognitect. We welcome
discussions of potential problems and enhancement suggestions on the
[transit-format mailing
list](https://groups.google.com/forum/#!forum/transit-format). Issues can be
filed using GitHub [issues](https://github.com/cognitect/transit-cljs/issues)
for this project. Because transit is incorporated into products and client
projects, we prefer to do development internally and are not accepting pull
requests or patches.

## Development

### Dependencies

Install dependencies with

```
lein deps
```

### Running the tests & benchmarks

Running the tests:

```
lein cljsbuild once test
```

In order to run the [transit-format](https://github.com/cognitect/transit-format) 
`bin/verify` you must first clone transit-format into the same parent directory 
as your transit-cljs checkout and build the roundtrip file:

```
lein cljsbuild once roundtrip
```

To run the benchmarks you must first clone 
[transit-format](https://github.com/cognitect/transit-format) into the same
parent directory as your transit-cljs checkout. 

Running the benchmarks:

```
lein cljsbuild once bench
node target/transit.bench.js
```

### Build

#### Build JAR for ClojureScript

Assuming you have a
[JDK](http://www.oracle.com/technetwork/java/javaee/downloads/java-ee-sdk-6u3-jdk-7u1-downloads-523391.html)
and [Maven](http://maven.apache.org) installed, the following will
install a JAR suitable for use from ClojureScript into your local
Maven repository.

```
build/package_local
```

## Copyright and License

Copyright © 2014 Cognitect

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
