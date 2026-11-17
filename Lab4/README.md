# Laboratorul 4 - Varianta 6

Un singur proiect: Andrei Toma (membru 1) si Ganenco Bogdan (membru 2), CR-242.
Java 8 sau mai nou, fara biblioteci externe.

## Pornire

Din folderul repository-ului:

```text
javac -encoding UTF-8 -d Lab4/out Lab4/src/*.java
java -cp Lab4/out Main
```

Doar consola: `java -cp Lab4/out Main --console`.
In IntelliJ: deschide proiectul, alege JDK si ruleaza `Main.main()`.

## Varianta 6

| X producatori | Y consumatori | Z numere/consumator | D capacitate | F | Obiecte |
|---|---|---|---|---|---|
| 2 | 4 | 4 | 7 | 2 | numere impare |

Fiecare producator genereaza odata 2 numere impare aleatoare (1..99).
Fiecare consumator ia cate un singur numar, pana are Z = 4.
In total consumatorii au nevoie de Y x Z = 16 numere.
Depozitul este o colectie `ArrayList<Integer>` cu cel mult D = 7 elemente.

## Cele 4 clase si cine ce face

- `Producator.java`: Andrei. Firul producator genereaza perechi de numere.
- `Consumator.java`: Bogdan. Firul consumator ia numere pana are 4.
- `Depozit.java`: clasa de baza, facuta impreuna. Contine colectia si
  sincronizarea. Metoda `pune()` (producatorii) este scrisa de Andrei,
  metoda `ia()` (consumatorii) este scrisa de Bogdan.
- `Main.java`: Andrei. Creeaza depozitul si cele 6 fire, le porneste,
  le asteapta cu `join()`, afiseaza rezumatul si fereastra grafica.

## Sincronizare

Toata sincronizarea este in `Depozit`, in metode `synchronized`.
In `run()` nu exista `synchronized`, `wait()` sau `notifyAll()`:
firele doar apeleaza `depozit.pune(...)` sau `depozit.ia(...)`.
Nu sunt folosite clase de sincronizare (semafoare, lock-uri, cozi blocante etc.).

`ia()`: daca depozitul este gol, consumatorul scrie "depozitul este gol"
si asteapta cu `wait()`. Dupa ce ia un numar, apeleaza `notifyAll()`,
ca producatorii sa afle ca s-a eliberat un loc.

`pune()`: daca depozitul este plin, producatorul scrie "depozitul este plin"
si asteapta cu `wait()`. Daca sunt 2 locuri, pune ambele numere.
Daca este un singur loc liber, pune primul numar, iar al doilea se pierde.
Dupa ce pune, apeleaza `notifyAll()` pentru consumatori.

`wait()` este mereu intr-un `while`, nu intr-un `if`: dupa trezire firul
verifica din nou conditia, deoarece alt fir poate sa fi luat deja locul sau numarul.

## Oprirea producatorilor

Metoda aleasa: producatorii se opresc singuri, cand s-au pus in depozit
cel putin 16 numere (cat au nevoie toti consumatorii).
Atunci `pune()` intoarce 0, bucla din `run()` se intrerupe cu `break`,
iar `run()` se termina normal, deci firul moare. Producatorul care observa
sfarsitul apeleaza `notifyAll()`, ca un producator care astepta la depozit
plin sa se trezeasca si sa se opreasca si el. Nu folosim fire daemon,
`stop()` sau bucle infinite de asteptare.

De ce nu se blocheaza programul: cat timp nu s-au produs 16 numere,
nici consumatorii nu au primit 16, deci cineva va elibera un loc.
Dupa 16 numere produse, producatorii nu mai asteapta deloc.
Uneori ultima pereche pune 17 numere, deci ramane 1 numar in depozit;
depozitul nu ramane plin.

Final asteptat: `produse=16` sau `17`, `consumate=16`, `ramase in depozit=0` sau `1`.

Lucrare pregatita cu asistenta AI. Fiecare membru trebuie sa inteleaga partea sa.
