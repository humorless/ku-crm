#!/usr/bin/env bash
csvsql --insert ./fake_students.tsv --db-schema dbt_develop --table students --tabs --no-constraints --db postgresql://localhost/kuops_dev
