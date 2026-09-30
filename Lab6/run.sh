#!/bin/sh
# Compilează și rulează lucrarea de laborator nr. 6
cd "$(dirname "$0")" || exit 1
javac -encoding UTF-8 -d out src/lab6/*.java && java -cp out lab6.Main "$@"
