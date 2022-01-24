#!/usr/bin/env bash

psql -d kuops_dev -c "DROP TABLE ops_students;"
psql -d kuops_dev -c "CREATE TABLE ops_students (id text PRIMARY KEY, name text, birth DATE, telephone text, mobile text, classroom_id text, classroom_type text, old_id text);"
