#!/bin/sh
# Compilează și rulează lucrarea de laborator nr. 2
cd "$(dirname "$0")" || exit 1
javac -encoding UTF-8 -d out src/lab2/*.java && java -cp out lab2.Main "$@"
