# Laboratorul 2 — Varianta 6

Andrei Toma și Ganenco Bogdan, grupa CR-242.

Programul `Varianta6_Prioritate_scazuta` (nivel mediu si avansat) construiește o ierarhie de grupuri de fire
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

Toate firele au prioritatea 2, cu excepția `Thb` (6) și `Th9` (3). La nivel mediu, firele nu
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
java -cp out Varianta6_Prioritate_scazuta
```

Lucrarea a fost încărcată pe ramura `Grup-6`.

## Partea lui Andrei Toma (+1)

Programul lui Andrei se afla separat in [`Andrei/`](Andrei/README.md).
Include nivelul mediu, nivelul avansat si interfata grafica, cu prioritatile
din tabel marite cu 1. Codul colegului cu -1 ramane in `src/`.

Din folderul principal al repository-ului:

```text
javac -encoding UTF-8 -d Lab2/Andrei/out Lab2/Andrei/src/andrei/*.java
java -cp Lab2/Andrei/out andrei.Main
```

Pentru consola:

```text
java -cp Lab2/Andrei/out andrei.Main --console --mediu
java -cp Lab2/Andrei/out andrei.Main --console --avansat
```

Ambele implementari folosesc `G2` si `G3` ca subgrupe ale lui `G1`.
Formula din PDF are acolade/separatori incorecti; aceasta este interpretarea
comuna a echipei, care necesita confirmarea profesoarei.
