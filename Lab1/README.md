# Laboratorul 1 - Varianta 6

Grupa CR-242. Ramura comuna a grupei: `Grup-6`.

## Ce este implementat

Acest commit contine partea lui **Andrei Toma**: `Student1Threads.java`, cu doua fire
create prin `Runnable`, plus pornirea comuna si afisarea grafica Swing.
Clasa colegului nu este implementata in acest commit. Programul ruleaza acum cu
doua fire de calcul; pentru predarea lucrarii in echipa trebuie integrate patru.
Firele interne Swing/JVM nu sunt firele de calcul cerute de laborator.

`Main` genereaza un singur `int[100]`, cu valori aleatorii din intervalul inclusiv
1..100. Cele doua fire primesc aceeasi referinta si doar citesc tabloul.

## Formula si numerotarea pozitiilor

Varianta 6 cere suma produselor numerelor de pe pozitii impare, doua cate doua,
dinspre inceput, respectiv dinspre sfarsit.

Folosim numerotarea de la **0**, ca indicii din exemplul Java al laboratorului:
pozitii impare = `mas[1], mas[3], ..., mas[99]`. Aceasta este o conventie explicita;
tabelul nu precizeaza separat baza numerotarii. Daca profesorul cere numerotare
de la 1, trebuie folosite in schimb indicii `0, 2, ..., 98`.

- Th1: `mas[1]*mas[3] + mas[5]*mas[7] + ... + mas[97]*mas[99]`.
- Th2: `mas[99]*mas[97] + mas[95]*mas[93] + ... + mas[3]*mas[1]`.

Fiecare fir afiseaza 25 de produse, sumele partiale si suma finala. Perechile nu
se suprapun. Sumele finale coincid, fiind aceiasi termeni parcursi invers.
Ordinea intercalarii mesajelor intre fire poate varia de la o rulare la alta.

## Rulare (JDK 17 sau mai nou)

Din folderul repository-ului, in PowerShell:

```powershell
.\Lab1\run.ps1
```

Pentru consola:

```powershell
.\Lab1\run.ps1 -Console
```

Daca politica PowerShell nu permite scriptul, poti compila si porni direct:

```powershell
cd Lab1
javac -encoding UTF-8 -d out src/*.java
java -cp out Main "Andrei Toma"
# Sau: java -cp out Main --console "Andrei Toma"
```

Numele implicit este Andrei Toma; poate fi schimbat cu `-Student "Nume Prenume"`.
Dupa `join()` pentru
toate firele inregistrate, firul principal afiseaza datele studentilor caracter
cu caracter, cu pauza de 100 ms. Swing primeste actualizarile prin `invokeLater`;
calculele si asteptarea nu blocheaza firul interfetei.

## Verificarea calculelor

Din folderul repository-ului:

```powershell
javac -encoding UTF-8 -d Lab1/out Lab1/src/*.java Lab1/tests/*.java
java -cp Lab1/out Student1ThreadsTest
```

Testul foloseste tabloul cunoscut `1..100` (suma asteptata: `85800`), verifica
25 de perechi pe fir, sensurile de parcurgere, valorile limita 1 si 100,
pastrarea tabloului nemodificat si citirea aceleiasi referinte comune.

## Partea colegului

Colegul implementeaza independent `Lab1/src/Student2Threads.java`:

1. Constructor public `Student2Threads(int[] mas, Consumer<String> output)`.
2. Doua fire distincte, numite `Student2-Th1` si `Student2-Th2`, cu `Runnable`.
3. Aceleasi doua conditii ale variantei 6, citind direct acelasi `mas` primit.
4. Metoda publica `Thread[] getThreads()` care returneaza cele doua fire.
5. Constructorul creeaza firele; `Main` le porneste, apoi le asteapta.

In `Main.java`, colegul activeaza cele trei linii marcate `COLEGUL` si completeaza
numele sau. Nu trebuie generat alt tablou, copiat `mas`, pornite firele in
constructor sau inlocuita clasa studentului 1. Dupa integrare mesajul trebuie
sa indice `Fire de calcul: 4`, cu cate 25 de produse pentru fiecare fir.

## GitHub: toate laboratoarele raman pe Grup-6

Folderul urmatorului laborator va fi `Lab2`, apoi `Lab3` etc.
Ramura `main` nu se foloseste pentru integrarea laboratoarelor grupei 6.

Clonare pentru coleg (intr-un folder in care proiectul nu exista deja):

```powershell
git clone --branch Grup-6 https://github.com/TurcanS/PCD_CR242.git
cd PCD_CR242
git switch -c Grup-6-lab1-coleg
# Implementeaza Student2Threads si activeaza integrarea in Main.
.\Lab1\run.ps1 -Student "Numele Prenumele studentului 1"
git add Lab1/src/Student2Threads.java Lab1/src/Main.java
git commit -m "Lab1: adauga cele doua fire ale studentului 2"
git push -u origin Grup-6-lab1-coleg
```

Pe GitHub: **Pull requests -> New pull request**, selecteaza **base: Grup-6**,
**compare: Grup-6-lab1-coleg**, apoi creeaza si integreaza cererea dupa verificare.
Cu GitHub CLI, crearea se poate face astfel:

```powershell
gh pr create --base Grup-6 --head Grup-6-lab1-coleg --title "Lab1: partea studentului 2" --body "Adauga Student2Threads si integreaza cele patru fire pe tabloul comun."
```

Pentru modificari viitoare la partea ta, foloseste o ramura proprie pornita din
`Grup-6`, apoi un Pull Request cu aceeasi destinatie:

```powershell
git switch Grup-6
git pull --ff-only origin Grup-6
git switch -c Grup-6-lab1-student1
# Modifica si verifica partea ta.
git add Lab1
git commit -m "Lab1: actualizeaza partea studentului 1"
git push -u origin Grup-6-lab1-student1
```

## Ce trebuie sa poti explica la sustinere

- `Runnable.run()` contine calculele; `Thread.start()` le lanseaza pe un fir nou.
- Apelarea directa a `run()` nu creeaza executie concurenta.
- `join()` asteapta terminarea unui fir; `sleep(100)` pauzeaza firul curent.
- `mas` este comun si nemodificat, iar fiecare fir are propria variabila `suma`.
- Pasul de 4 consuma doua pozitii impare fara reutilizarea elementelor.
- Interfata Swing are propriul fir de evenimente pentru actualizarea ferestrei.

Pentru predarea finala, cerinta solicita si raportul echipei cu numele, grupa,
sarcina, varianta, descrierea si linkul codului. Raportul nu este inclus aici.
PDF-ul mentioneaza separat evaluarea utilizarii IA si intelegerea codului.
