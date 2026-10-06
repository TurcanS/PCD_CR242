# Lab 1 - Varianta 6

Andrei Toma si Ganenco Bogdan, grupa CR-242. Laboratoarele grupei sunt pe ramura `Grup-6`.

## Compilare si pornire

Este necesar JDK 17 sau mai nou. Din folderul repository-ului:

```powershell
cd Lab1
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Pentru rulare doar in consola:

```powershell
java -cp out Main --console
```

## Program

- `Student1Threads.java`: cele doua fire ale lui Andrei Toma, prin `Runnable`.
- `Student2Threads.java`: cele doua fire ale lui Ganenco Bogdan, prin `Runnable`.
- `Main.java`: genereaza tabloul comun de 100 de numere intre 1 si 100,
  porneste firele si le asteapta prin `join()`.
- `LabWindow.java`: interfata grafica Swing.

Pozitiile sunt numerotate de la 0: indicii impari sunt `1, 3, ..., 99`.
Th1 calculeaza `mas[1]*mas[3] + mas[5]*mas[7] + ... + mas[97]*mas[99]`.
Th2 parcurge aceleasi perechi invers, de la `(99,97)` pana la `(3,1)`.
Fiecare fir afiseaza produsele, sumele partiale si suma finala.
Firul principal afiseaza numele studentilor caracter cu caracter, la 100 ms.

Toate cele patru fire citesc acelasi tablou mas, fara regenerare sau copiere.
