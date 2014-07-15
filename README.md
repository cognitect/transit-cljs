# transit-cljs

Transit is a data format and a set of libraries for conveying values between applications written in different languages. This library provides support for marshalling Transit data to/from ClojureScript.

* [Rationale](http://i-should-be-a-link)
* [API docs](http://cognitect.github.io/transit-java/)
* [Specification](http://github.com/cognitect/transit-format)

## Releases and Dependency Information

* Latest release: TBD
* [All Released Versions](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.cognitect%22%20AND%20a%3A%22transit-cljs%22)

### Leiningen

Add the following to your `project.clj` `:dependencies`:

```
[com.cognitect/transit-cljs "TBD"]
```

### Maven

[Maven](http://maven.apache.org/) dependency information:

```xml
<dependency>
  <groupId>com.cognitect</groupId>
  <artifactId>transit-cljs</artifactId>
  <version>TBD</version>
</dependency>
```

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
|integer|Number|Number, gm.Long|
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

Please discuss potential problems or enhancements on the [transit-format mailing list](https://groups.google.com/forum/#!forum/transit-format). Issues should be filed using GitHub issues for this project.

Contributing to Cognitect projects requires a signed [Cognitect Contributor Agreement](http://cognitect.com/contributing).

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
