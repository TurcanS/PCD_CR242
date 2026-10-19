#!/bin/sh
# Compilează și rulează lucrarea de laborator nr. 4
cd "$(dirname "$0")" || exit 1
javac -encoding UTF-8 -d out src/lab4/*.java && java -cp out lab4.Main "$@"
