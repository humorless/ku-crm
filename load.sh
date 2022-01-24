#!/usr/bin/env bash
csvsql --insert ./fake_students.tsv  --table students --tabs --no-constraints --db postgresql://localhost/kuops_dev
