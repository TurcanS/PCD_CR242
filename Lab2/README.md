# Laboratorul 2 — Varianta 6

Andrei Toma și Ganenco Bogdan, grupa CR-242.

Programul `Varianta6` (nivel mediu) construiește o ierarhie de grupuri de fire
și afișează, recursiv, fiecare grup împreună cu firele lui active.

Structura creată în `main`:

```
main
├── Th1, Th2
└── G1
    ├── ThA
    ├── G3: Thf, Thb, Thc, Thd
    └── G2: Th8, Th9, Th3
```

Toate firele au prioritatea 3, cu excepția `Thb` (7) și `Th9` (4). Firele nu
suprascriu `run()`: corpul lor este dat ca `Runnable` (o pauză de 1500 ms) prin
metoda `creeaza`. Toate sunt fire daemon, iar `setDaemon(true)` este apelat
înainte de `start()`.

După pornire, `main` așteaptă 300 ms, ca firele să apară în enumerare, apoi
`afiseazaArbore` parcurge grupurile cu `enumerate(..., false)`. Pentru fiecare
grup afișează `maxPriority`, iar pentru fiecare fir numele, grupa, prioritatea
și dacă este daemon. Fiindcă firele daemon nu țin programul în viață, `main`
le așteaptă explicit cu `join()`. La final, tot rezultatul este afișat și
într-o fereastră Swing.

Pentru pornire, cu JDK 17 sau mai nou, din folderul repository-ului:

```powershell
cd Lab2
javac -encoding UTF-8 -d out src/*.java
java -cp out Varianta6
```

Lucrarea a fost încărcată pe ramura `Grup-6`.
