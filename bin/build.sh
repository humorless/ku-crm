#!/usr/bin/env bash

# 3. Aot compile
clj -M -e "(compile 'replware.ku-crm.core)"

# 4. Uberjar with --main-class option
clojure -M:uberjar --main-class replware.ku-crm.core
