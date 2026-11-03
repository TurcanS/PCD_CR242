# Laboratorul 2 - Andrei Toma - CR-242

Varianta 6. Partea mea: prioritatile din conditie +1.
Java 8 sau mai nou, fara biblioteci externe.

Programul ruleaza pe rand nivelul mediu si nivelul avansat.
Rezultatul apare in consola si intr-o fereastra simpla cu text.

## Pornire

Din folderul principal al repository-ului:

```text
javac -encoding UTF-8 -d Lab2/Andrei/out Lab2/Andrei/src/andrei/*.java
java -cp Lab2/Andrei/out andrei.Main
```

Pentru rulare doar in consola:

```text
java -cp Lab2/Andrei/out andrei.Main --console
```

In IntelliJ se poate rula direct metoda main din `andrei.Main`.

## Cele trei fisiere

- `Main.java`: porneste cele doua niveluri si deschide fereastra cu rezultatul.
- `Lab2.java`: creeaza grupurile si cele 10 fire, adauga +1 la prioritati,
  porneste firele, afiseaza structura si asteapta terminarea lor.
- `FirAvansat.java`: un fir care isi afiseaza singur numele, grupul si
  prioritatea, in metoda `run()` suprascrisa.

## Structura

```text
Main
  Th1(4), Th2(4)
  G1
    ThA(4)
    G3
      Thf(4), Thb(8), Thc(4), Thd(4)
    G2
      Th8(4), Th9(5), Th3(4)
```

Sunt 10 fire si 4 grupuri, inclusiv Main.
Thb are 7 + 1 = 8. Th9 are 4 + 1 = 5. Celelalte au 3 + 1 = 4.

Formula din PDF are acolade/separatori gresiti. Folosim aceeasi interpretare
ca in partea colegului: G2 si G3 sunt in G1. Aceasta interpretare trebuie
confirmata cu profesoara.

## Cum explic programul

1. Creez grupurile cu `new ThreadGroup(parinte, nume)`.
2. Creez firele si le dau prioritatea din conditie +1, cu `setPriority()`.
3. Pornesc fiecare fir cu `start()`.
4. Citesc firele active dintr-un grup cu `enumerate(..., false)`.
   Apoi fac acelasi lucru pentru fiecare subgrup: aceasta este recursivitatea.
5. Eliberez firele si astept terminarea lor cu `join()`.

La nivel mediu folosesc Thread cu Runnable, fara sa suprascriu Thread.run().
La nivel avansat folosesc FirAvansat, care suprascrie run() si afiseaza
informatiile chiar din firul respectiv.

`CountDownLatch` este un semnal: `await()` spune "asteapta", iar
`countDown()` spune "puteti termina". Il folosesc ca firele sa ramana
active pana le enumar. Nu trebuie sa ghicesc cate milisecunde sa doarma.

`getName()` citeste numele, `getThreadGroup()` citeste grupul,
iar `getPriority()` citeste prioritatea firului.

Mesajele run() pot aparea in ordine diferita sau intre liniile enumerarii.
Prioritatea mai mare nu garanteaza ca firul afiseaza primul.
`synchronized` din afiseaza() permite unui singur fir odata sa adauge
mesajul in rezultatul comun.

Material pregatit cu ajutorul AI; codul trebuie inteles pentru sustinere.
Raportul PDF/Word pentru ELSE se pregateste separat.

Cod: https://github.com/TurcanS/PCD_CR242/tree/Grup-6/Lab2/Andrei