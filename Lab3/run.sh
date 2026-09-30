#!/bin/sh
# Compilează și rulează lucrarea de laborator nr. 3
cd "$(dirname "$0")" || exit 1
javac -encoding UTF-8 -d out src/lab3/*.java && java -cp out lab3.Main "$@"
