# Laboratorul 1 — Varianta 6

Andrei Toma și Ganenco Bogdan, grupa CR-242.

În partea mea, `Student1Threads`, folosesc două fire cu `Runnable`: primul
parcurge pozițiile impare de la început, iar al doilea de la sfârșit.
Înmulțesc numerele două câte două și adun produsele. Pozițiile sunt indicii
Java `1, 3, ..., 99`, numerotați de la 0.

Eu deplasez indicele cu `+4` sau `-4`. Bogdan, în `Student2Threads`, calculează
indicii folosind numărul perechii. Implementările diferă, dar suma finală
este aceeași pentru același tablou.

Avem un singur `Main`, un tablou comun de 100 de valori aleatorii între 1 și
100 și patru fire de calcul. După `join()`, programul afișează ambele nume,
caracter cu caracter, la 100 ms. `LabWindow` afișează rezultatele grafic.

Pentru pornire, cu JDK 17 sau mai nou, din folderul repository-ului:

```powershell
cd Lab1
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Partea mea a fost încărcată pe `Grup-6`; partea lui Bogdan a fost integrată
prin Pull Request din `Grup-6-lab1-bogdan` către `Grup-6`.
