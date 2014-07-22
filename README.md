# transit-cljs

Transit is a data format and a set of libraries for conveying values between applications written in different languages. This library provides support for marshalling Transit data to/from ClojureScript.

* [Rationale](http://blog.cognitect.com/blog/2014/7/22/transit)
* [API docs](http://cognitect.github.io/transit-cljs/)
* [Specification](http://github.com/cognitect/transit-format)

This implementation's major.minor version number corresponds to the version of the Transit specification it supports.

_NOTE: Transit is a work in progress and may evolve based on feedback.
As a result, while Transit is a great option for transferring data
between applications, it should not yet be used for storing data
durably over time. This recommendation will change when the
specification is complete._ 

## Releases and Dependency Information

* Latest release: 0.8.137
* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cognitect%22%20AND%20a%3A%22transit-cljs%22)

### Download and install

1. Download and unzip http://cdn.cognitect.com/transit/transit-js-0.8.616.zip
2. Download and unzip http://cdn.cognitect.com/transit/transit-cljs-0.8.137.zip
3. From the unzip directory: ```mvn install:install-file -DgroupId=com.cognitect -DartifactId=transit-js -Dfile=transit-js-0.8.616.jar -DpomFile=transit-js-0.8.616.pom```
4. From the unzip directory: ```mvn install:install-file -DgroupId=com.cognitect -DartifactId=transit-cljs -Dfile=transit-cljs-0.8.137.jar -DpomFile=transit-js-0.8.137.pom```

### Leiningen

Add the following to your `project.clj` `:dependencies`:

```
[com.cognitect/transit-cljs "0.8.137"]
```

Note: Jar may not yet be available on Maven Central! If so, please download and install locally until it is available.

### Maven

[Maven](http://maven.apache.org/) dependency information:

```xml
<dependency>
  <groupId>com.cognitect</groupId>
  <artifactId>transit-cljs</artifactId>
  <version>0.8.137</version>
</dependency>
```

Note: Jar may not yet be available on Maven Central! If so, please download and install locally until it is available.

## Usage

```clojurescript
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
    (asssert (= data data'))))
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
|set|cc.ISet|cc.ISet|
|map|cc.IMap|cc.IMap|
|link|cct.Link|cct.Link|

## Contributing 

This library is open source, developed internally by Cognitect. We welcome discussions of potential problems and enhancement suggestions on the [transit-format mailing list](https://groups.google.com/forum/#!forum/transit-format). Issues can be filed using GitHub [issues](https://github.com/cognitect/transit-cljs/issues) for this project. Because transit is incorporated into products and client projects, we prefer to do development internally and are not accepting pull requests or patches.


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
open index.html
```

In order to run the `bin/verify` tests you must first build the
roundtrip file:

```
lein cljsbuild once roundtrip
```

Running the benchmarks:

```
lein cljsbuild once bench
node target/transit.bench.js
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
