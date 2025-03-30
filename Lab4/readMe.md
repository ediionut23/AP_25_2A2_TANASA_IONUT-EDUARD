Ok deci o sa incep cu tema:
  In primul rand avem un enum pentru fiecare tip de locatie: ENUM, FRIENDLY si ENEMY. Apoi avem o clasa Locatie care reprezinta fiecare 
locatie unde avem numele si tipul. Apoi avem o clasa LocationFactory care este folosita pentru a contrui locatii random. In clasa asta am folosit
faker pentru a genera nume pentru locatiile noastre. Apoi am folosit JGraphT pentru a putea crea un graf ponderat pentru a putea apoi sa 
calculam distantele intre diferite locatii. Aici avem si o lista pentru a avea toate locatiile intr-un loc usor de accesat si am folosit si un
map care are ca cheie tipul locatie si ca item lista cu toate locatiile de tipul ala. Apoi am creat 6 locatii, cate de doua de fiecare fel, pentru
exemplificarea algoritmului(in fisierul bonus scris dupa am generat locatii, aici le am creat de "mana"). Apoi adaugam ponderi cu valori random
intre doua locatii aleator cu o variabila bool random, apoi avem clasa drona care practic ar trebui sa simuleze o drona ce sa vezi =)), defapt 
noi avem un punct de start si aplicam algoritmul lui dijkstra pentru a afla cel mai scurt drum dintre locatia de start si celelalte locatii.

BONUS: ideea a fost sa folosesc tot dijkstra pentru locatii, doar ca acum ponderile din graf sa nu mai fi "distante" ci procent de siguranta.
Acum deoarece dijkstra cauta costul minim, trebui sa transformam sigurantele astfel incat o probabilitate mai mare sa insemne un cost mai mic,
plus ca probabilitatea de siguranta din A si B se inmulteste, nu se aduna, o sa calculam logaritmic negativ pentru a transforma produsul in suma
iar la final pentru a avea tot o valoare procentuala de siguranta transformam iar cu o exponentiere negativa a costului totatl(probabilitati adunate).
Ce e in plus fata de homework: avem tot o clasa factory dar acum ne generam un anumit numarar de locatii, si facem cumva mai multe seturi,
in sensul ca o data generam 500, o data 250, etc pentru a face niste avg pentru statistica la final. Ne creem iar graful ponderat cu ponderile cum am
mentionat mai sus. Aplicam dijkstra, avem testResult pentru a face statisticile si apoi in Bonus(clasa principala) face calculele pentru 
grafuri diferite si apoi afiseaza statisticile pe care le calculeaza aici cu streamuri API. 
