# PCD_CR242 — Grupa 4

**Programarea Concurentă și Distribuită**
Universitatea Tehnică a Moldovei, FCIM

## Echipa

| Student | Grupa academică |
|---|---|
| Spranceana Marius | Calculatoare și Rețele – 242 (CR-242) |
| Cornos Ilie | Calculatoare și Rețele – 242 (CR-242) |

## Lucrări de laborator

| Nr. | Tema | Varianta | Cod sursă | Raport |
|---|---|---|---|---|
| 1 | Crearea thread-urilor | 4 | [Lab1/src/lab1](Lab1/src/lab1) | [Raport_Lab1_PCD_Cornos_Spranceana.docx](Lab1/Raport_Lab1_PCD_Cornos_Spranceana.docx) |
| 2 | Gruparea firelor de execuție. Modificarea priorității | 4 | [Lab2/src/lab2](Lab2/src/lab2) | [Raport_Lab2_PCD_Cornos_Spranceana.docx](Lab2/Raport_Lab2_PCD_Cornos_Spranceana.docx) |
| 3 | Sincronizarea firelor de execuție prin metodele clasei Thread | 4 | [Lab3/src/lab3](Lab3/src/lab3) | [Raport_Lab3_PCD_Cornos_Spranceana.docx](Lab3/Raport_Lab3_PCD_Cornos_Spranceana.docx) |
| 4 | Sincronizarea thread-urilor în Java (producător–consumator) | 4 | [Lab4/src/lab4](Lab4/src/lab4) | [Raport_Lab4_PCD_Cornos_Spranceana.docx](Lab4/Raport_Lab4_PCD_Cornos_Spranceana.docx) |

### Lucrarea nr. 2 — variantele individuale (varianta 4, nivelul avansat)

Prioritățile din formulă sunt ajustate diferit pentru fiecare student și limitate la intervalul 1..10.

| Student | Ajustare | Cod sursă |
|---|---|---|
| Cornos Ilie | +1 | [Lab2/Cornos_Ilie/src/lab2](Lab2/Cornos_Ilie/src/lab2) |
| Spranceana Marius | −1 | [Lab2/Spranceana_Marius/src/lab2](Lab2/Spranceana_Marius/src/lab2) |

## Rulare

Este necesar Java 17 sau o versiune mai nouă.

```sh
./Lab1/run.sh   # lucrarea nr. 1
./Lab2/run.sh   # lucrarea nr. 2
./Lab2/Cornos_Ilie/run.sh         # lucrarea nr. 2, varianta lui Cornos Ilie (+1)
./Lab2/Spranceana_Marius/run.sh   # lucrarea nr. 2, varianta lui Spranceana Marius (−1)
./Lab3/run.sh   # lucrarea nr. 3
./Lab4/run.sh   # lucrarea nr. 4
```
