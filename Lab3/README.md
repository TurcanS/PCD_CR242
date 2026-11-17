# Laboratorul 3 - Varianta 6

Un singur proiect: Andrei Toma (membru 1) si Ganenco Bogdan (membru 2), CR-242.
Java 8 sau mai nou, fara biblioteci externe.

## Pornire

Din folderul repository-ului, dupa adaugarea fisierelor ambilor membri:

```text
javac -encoding UTF-8 -d Lab3/out Lab3/src/*.java
java -cp Lab3/out Main
```

Doar consola: `java -cp Lab3/out Main --console`.
In IntelliJ: deschide proiectul, alege JDK si ruleaza `Main.main()`.
Rularea dureaza aproximativ 6-9 secunde, deoarece literele apar la 100 ms.

## Cine face ce

- `Main.java`: tablou comun de 100 de valori, creeaza/porneste/asteapta cele 4 fire.
- `Student1Threads.java`: Andrei, Th1 si Th2; produse pe perechi si suma cumulativa.
  Th1 asteapta Th3 prin `join()`; Th2 trimite semnal lui Th4 prin `interrupt()`.
- `Student2Threads.java`: Bogdan, Th3 si Th4; intervale, numar de elemente si suma de control.
  Asteptare cu `sleep()`, eliberata prin semnalul `interrupt()` de la predecesor.
- `LabWindow.java`: interfata comuna Swing, cu jurnalul celor patru fire.

Ambele contributii sunt in acelasi proiect pe ramura `Grup-6`.

## Varianta 6

Folosim conventia din Lab1: pozitii impare = indici Java 1, 3, ..., 99.
Th1: (1,3), (5,7), ..., (97,99). Th2: (99,97), (95,93), ..., (3,1).
Fiecare fir inmulteste valorile din pereche si adauga produsul la suma.
Sunt 25 de perechi in fiecare sens, iar sumele finale trebuie sa coincida.
Nu se filtreaza valorile impare: conditia priveste pozitiile.

Th3: 654, 655, ..., 1278 (625 de valori, suma 603750).
Th4: 908, 907, ..., 123 (786 de valori, suma 405183).
Limitele sunt incluse. Seed-ul 6 permite repetarea aceleiasi verificari.

## Sincronizare

Calculele se fac concurent, in variabile locale. Afisarea jurnalelor numerice
este ordonata explicit: Th2, Th4, Th3, Th1 (indicii 1, 3, 2, 0).
Fiecare fir asteapta afisarea jurnalului predecesorului, afiseaza propriul
jurnal complet si apoi anunta terminarea acestei etape.
Patru indicatori `volatile` din Main fac vizibila terminarea tuturor calculelor.
Niciun text final nu incepe pana cand toti cei patru indicatori sunt true.
Aceasta bariera foloseste indicatori volatile si pauze scurte, nu reprezinta
patru metode distincte de sincronizare.

Ordinea indicilor ceruta explicit este 1, 3, 2, 0, adica Th2, Th4, Th3, Th1:
Th2: Toma si Ganenco (numele de familie ale ambilor membri).
Th4: CR-242.
Th3: Programarea concurenta si distribuita.
Th1: Andrei si Bogdan (prenumele ambilor membri).
Aceasta ordine explicita difera de enumerarea Th2, Th4, Th1, Th3 din PDF.

Th1 asteapta terminarea lui Th3 prin `join()`.
Th4 asteapta intr-un `sleep()` pana cand Th2 termina afisarea numelui
si ii trimite `interrupt()`. Th3 asteapta la fel semnalul trimis de Th4,
dupa afisarea grupei. `InterruptedException` incheie asteptarea,
iar firul continua cu afisarea sa. Nu se verifica periodic starea altui fir.
Semnalul este trimis numai dupa textul complet, inclusiv ultima pauza de 100 ms.
Daca semnalul ajunge inainte de `sleep()`, indicatorul de intrerupere ramane
setat, iar `sleep()` arunca imediat exceptia. In acest protocol, intreruperea
este rezervata semnalului de continuare, nu anularii lucrarii.
Main porneste firele in ordinea Th2, Th4, Th3, Th1. Bariera asigura ca toate
firele sunt pornite si au afisat rezultatele numerice inainte de primul text.
`sleep()` singur nu garanteaza ordinea. Pentru Bogdan, ordinea este data de
semnalul `interrupt()` si de tratarea lui; pentru Andrei, asteptarea este `join()`.
`sleep(100)` este folosit si in functia comuna pentru afisarea literelor.
Acest protocol nu este prezentat ca patru metode distincte de sincronizare;
lista celor patru metode acceptate de profesoara ramane de clarificat.

Nu sunt folosite `synchronized`, `wait/notify`, semafoare sau latch-uri pentru
sincronizarea celor patru fire. Actualizarea Swing se trimite pe firul grafic.
Cele 4 fire cerute sunt firele de lucru; Java/Swing au si fire de sistem.

Lucrare pregatita cu asistenta AI. Fiecare membru trebuie sa inteleaga partea sa.
