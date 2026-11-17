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
  Metode pentru partea sa: `join()` si `sleep()`.
- `Student2Threads.java`: Bogdan, Th3 si Th4; intervale, numar de elemente si suma de control.
  Asteptare cu `sleep()`, eliberata prin semnalul `interrupt()` de la predecesor.
- `LabWindow.java`: interfata comuna Swing, cu jurnalul celor patru fire.

Contributia lui Andrei si fisierele comune sunt incarcate primele pe `Grup-6`.
Bogdan adauga `Lab3/src/Student2Threads.java` din pachetul primit, de pe contul sau.
Pana atunci copia GitHub nu este completa; copia locala si arhiva completa contin ambele parti.

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

Calculele se fac concurent, in variabile locale. Jurnalul fiecarui fir este
retinut local si afisat numai cand ii vine randul, ca literele sa nu se amestece.
Ordinea afisarii este Th2 -> Th4 -> Th1 -> Th3:
Toma -> CR-242 -> Andrei -> Programarea concurenta si distribuita.

Th1 asteapta terminarea lui Th4 prin `join()`.
Th4 asteapta intr-un `sleep()` pana cand Th2 termina afisarea numelui
si ii trimite `interrupt()`. Th3 asteapta la fel semnalul trimis de Th1,
dupa afisarea prenumelui. `InterruptedException` incheie asteptarea,
iar firul continua cu afisarea sa. Nu se verifica periodic starea altui fir.
Semnalul este trimis numai dupa textul complet, inclusiv ultima pauza de 100 ms.
Daca semnalul ajunge inainte de `sleep()`, indicatorul de intrerupere ramane
setat, iar `sleep()` arunca imediat exceptia. In acest protocol, intreruperea
este rezervata semnalului de continuare, nu anularii lucrarii.
Main porneste firele in ordinea Th3, Th4, Th1, Th2, ca receptorii sa fie porniti
inainte de trimiterea semnalelor. Ordinea pornirii difera de ordinea afisarii.
`sleep()` singur nu garanteaza ordinea. Pentru Bogdan, ordinea este data de
semnalul `interrupt()` si de tratarea lui; pentru Andrei, asteptarea este `join()`.
`sleep(100)` este folosit si in functia comuna pentru afisarea literelor.

Nu sunt folosite `synchronized`, `wait/notify`, semafoare sau latch-uri pentru
sincronizarea celor patru fire. Actualizarea Swing se trimite pe firul grafic.
Cele 4 fire cerute sunt firele de lucru; Java/Swing au si fire de sistem.

Lucrare pregatita cu asistenta AI. Fiecare membru trebuie sa inteleaga partea sa.
