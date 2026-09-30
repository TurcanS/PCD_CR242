#!/bin/sh
# Compilează și rulează lucrarea de laborator nr. 5
cd "$(dirname "$0")" || exit 1
javac -encoding UTF-8 -d out src/lab5/*.java && java -cp out lab5.Main "$@"
