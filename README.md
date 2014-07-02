# transit-cljs

## Dependencies

Install dependencies with

```
lein deps
```

## Running the tests & benchmarks

Running the tests:

```
lein cljsbuild once test
open index.html
```

Running the benchmarks:

```
lein cljsbuild once bench
node target/transit.bench.js
```

In order to run the `bin/verify` tests you must first build the
roundtrip file:

```
lein cljsbuild once roundtrip
```

## License

Copyright © 2014 Cognitect, Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
