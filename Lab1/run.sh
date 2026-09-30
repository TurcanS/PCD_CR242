#!/bin/sh
# Compilează și rulează lucrarea de laborator nr. 1
cd "$(dirname "$0")" || exit 1
javac -encoding UTF-8 -d out src/lab1/*.java && java -cp out lab1.Main "$@"
