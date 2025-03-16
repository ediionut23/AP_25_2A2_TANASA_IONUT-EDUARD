In fisierul Homework.java avem rezolvarea la tema de la laboratorul 2, in care avem urmatoarele:
Avem un enum pentru proiecte. Avem clasa Proiect, in care sunt definite getteri, setteri si
variabile specifice. De mentionat ca un proiect are o variabila de tip Profesor, 
care reprezinta profesorul ce a propus proiectul respectiv. Un proiect poate fi propus de un singur profesor.
Clasa Person reprezinta entitati care sunt persoane, adica au nume si data nasterii. Aici avem o functie 
speciala equals, care verifica daca doua persoane nu sunt la fel, comparandu-le si determinand daca este vorba
despre aceeasi persoana. Apoi avem clasele Student si Profesor, care extind clasa Person, fiecare dintre ele avand atribute specifice.
Avem apoi clasa Problema, care organizeaza informatiile astfel incat sa putem rezolva problema asignarii 
proiectelor studentilor. In aceasta clasa exista cate un vector in care sunt stocati toti studentii, proiectele si profesorii.
Ultima este clasa Solution, care prelucreaza informatiile din Problema dupa cum urmeaza: pentru 
fiecare student, verifica proiectele pe care si le doreste, parcurgandu-le incepand cu cel preferat.
Daca gaseste un proiect liber, il asigneaza. Daca alti studenti doresc acelasi proiect dupa ce a fost deja
asignat cuiva, acestia nu il mai pot obtine si vor incerca sa isi aleaga urmatorul proiect dorit, pana cand nu
mai au alte optiuni. In aceasta clasa exista si functia printSolution, care verifica daca toti studentii au 
un proiect asignat si, daca da, afiseaza fiecare student impreuna cu proiectul care i-a fost atribuit.

BONUS: In clasa Solution exista si functia teoremaHall, care verifica daca fiecarui student ii poate fi 
asignat un proiect. Aceasta functie functioneaza in modul urmator: se genereaza toate seturile posibile
de combinatii de studenti. De exemplu, se iau toate variatiile de cate doi studenti, apoi toate variatiile 
de cate trei studenti si asa mai departe. Pentru fiecare variatie, se iau toate proiectele dorite de studentii 
respectivi, in mod unic. Daca numarul de studenti este mai mare decat numarul de proiecte disponibile, atunci nu este posibil
sa asignam fiecarui student un proiect. In caz contrar, asignarea este posibila.

