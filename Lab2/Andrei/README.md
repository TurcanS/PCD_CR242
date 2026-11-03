# Laboratorul 2 - Andrei Toma - CR-242

Varianta 6. Prioritatea fiecarui fir din tabel este marita cu 1.
Programul include nivelul mediu, nivelul avansat si o interfata grafica
cu schema grupurilor si jurnalul executiei. Nu contine partea cu -1.
Java 8 sau mai nou, fara biblioteci externe.

## Pornire

Din folderul principal al repository-ului:

```text
javac -encoding UTF-8 -d Lab2/Andrei/out Lab2/Andrei/src/andrei/*.java
java -cp Lab2/Andrei/out andrei.Main
```

Din acest folder (`Lab2/Andrei`), comenzile sunt:

```text
javac -encoding UTF-8 -d out src/andrei/*.java
java -cp out andrei.Main
```

Implicit se deschide fereastra, cu nivelul avansat selectat.
Alege nivelul si apasa **Ruleaza programul**. Informatiile despre fire
apar in jurnal si in consola. Prioritatile sunt intotdeauna cele din tabel +1.

Doar consola, din acest folder:

```text
java -cp out andrei.Main --console --mediu
java -cp out andrei.Main --console --avansat
```

Clasele folosesc pachetul `andrei`, pentru a nu intra in conflict cu
clasele `Main` si `LabWindow` din laboratorul 1.

## Structura si prioritati

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

Sunt 10 fire si 4 grupuri, inclusiv grupul creat `Main`.
`Thb`: 7 + 1 = 8; `Th9`: 4 + 1 = 5; celelalte fire: 3 + 1 = 4.

Formula variantei 6 din PDF (pagina 7) are acolade/separatori incorecti.
Pentru consistenta in echipa, folosim aceeasi interpretare ca in partea
colegului: `G2` si `G3` sunt subgrupe ale lui `G1`. Aceasta interpretare
nu este confirmata de profesoara.

## Ce face programul

- `Main.java`: citeste optiunile si porneste consola sau fereastra.
- `Lab2.java`: creeaza grupurile si cele 10 fire, seteaza prioritatile cu +1,
  porneste firele, enumera recursiv structura si asteapta terminarea lor.
- `FirAvansat.java`: mosteneste `Thread` si suprascrie `run()`, unde fiecare
  fir isi afiseaza numele, grupul si prioritatea.
- `LabWindow.java`: afiseaza schema, alegerea nivelului si jurnalul.

La nivel mediu sunt folosite obiecte `Thread` cu `Runnable`, fara
suprascrierea metodei `Thread.run()`. La nivel avansat se foloseste
clasa `FirAvansat` cu metoda `run()` suprascrisa.

`setPriority()` stabileste prioritatea inainte de `start()`.
`start()` porneste executia separata a fiecarui fir.
`CountDownLatch` mentine firele active pana la enumerare, fara pauze
de timp alese arbitrar. `enumerate(..., false)` obtine doar firele sau
subgrupele directe; recursivitatea parcurge intreaga structura.
`join()` asteapta terminarea firelor.

Liniile `run(): ...` sunt afisate de fire, iar enumerarea de dupa ele
este afisata de coordonator. Ordinea mesajelor `run()` poate varia:
prioritatea nu garanteaza ordinea executiei. In enumerare, numele sunt
sortate doar pentru lizibilitate. Prioritatea maxima de grup `10`
este limita permisa, nu prioritatea tuturor firelor.

Material pregatit cu ajutorul AI; codul trebuie inteles pentru sustinere.
Raportul PDF/Word cerut pentru ELSE se pregateste separat.

Cod: https://github.com/TurcanS/PCD_CR242/tree/Grup-6/Lab2/Andrei
