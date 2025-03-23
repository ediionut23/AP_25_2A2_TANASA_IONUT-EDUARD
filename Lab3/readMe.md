Avem interfetele de PassengerCapable si CargoCapable si celelalte clase precum Aircraft care este clasa abstracta pe baza
caruia avem Airliner,
Freighter si Drone. Apoi avem clasa Flight la care ne intereseaza cel mai mult arvialStart si arivalEnd, ca sa vedem cand avem zborurile. 
Aici e important de mentionat ca avem o
metoda care verifica daca doua zboruri au conflict.Se creează o listă cu pistele disponibile, apoi se încearcă alocarea fiecărui zbor 
la o pistă, asigurându-se că nu există  conflicte de timp între zborurile de pe aceeași pistă. Dacă un zbor se suprapune cu altul pe
o pistă, se încearcă o altă pistă. Rezultatul este stocat într-o hartă (schedule), care asociază fiecare zbor cu o pistă.

BONUS: Iar la bonus avem aceleasi clase difera doar clasa Schedule
Se creează o listă de zboruri și acestea sunt sortate după ora de început. Pentru fiecare zbor, se verifică ce piste sunt deja 
ocupate în acel interval de timp și se atribuie o pistă liberă folosind o tehnică de colorare a grafului. Dacă numărul de piste
necesar depășește capacitatea aeroportului, se ajustează orele de aterizare pentru a evita suprapunerile. La final, fiecare zbor 
este asociat cu o pistă, iar programul afișează programarea finală.
