Am început prin a crea o clasă numită Tile, care reprezintă fiecare piesă de joc. Fiecare piesă are o literă și un număr de puncte.
Am folosit un constructor pentru a le seta la început și o metodă toString pentru a le putea afişa mai ușor. Asta ajută la înțelegerea mai 
ușoară a pieselor de joc, având și litera, dar și punctele.
După aceea am creat o clasă numită TileBag, care este ca un sac de piese. În constructorul acestei clase, am adăugat 50 de piese pentru 
fiecare literă din alfabet. Punctele pentru fiecare piesă le-am ales aleatoriu între 1 și 10. Am amestecat piesele cu Collections.shuffle(),
ca să fie mai imprevizibil. Am făcut două metode importante: extractTiles care extrage un număr de piese din sac și isEmpty, care verifică 
dacă mai sunt piese în sac sau nu.
Apoi am făcut o clasă numită Dictionary, care verifică dacă un cuvânt este valid sau nu. Am citit un fișier cu cuvinte, le-am pus într-un 
set și le-am făcut majuscule, ca să fie ușor de verificat dacă un cuvânt se află în dicționarul nostru. Am avut nevoie și de o clasă Board, 
care se ocupă de cuvintele trimise de jucători. Fiecare jucător trimite un cuvânt și board-ul îl arată cu scorul asociat. Aici am folosit sincronizare,
ca să nu fie conflicte între jucători atunci când trimit cuvintele. Apoi am creat clasa TurnManager, care se ocupă de cine este la rând. Practic, 
se asigură că fiecare jucător așteaptă să fie rândul lui și, 
când termină, se trece la următorul jucător. Aceasta face ca jocul să se desfășoare în ordine și fără conflicte. A urmat clasa Player, care 
este clasa ce reprezintă fiecare jucător. Am extins-o de la Thread, ca să poată juca în paralel cu ceilalți 
jucători. Fiecare jucător are un set de piese pe care le extrage din sac, încearcă să formeze un cuvânt, calculează scorul pe baza pieselor 
folosite și trimite cuvântul la board. Dacă nu poate face un cuvânt, trece la următorul tur. Am folosit dicționarul pentru a verifica dacă
cuvintele sunt valabile și am calculat scorul pe baza pieselor utilizate.
Am avut nevoie și de o clasă TimeKeeper, care ține cont de timpul jocului. Aceasta măsoară câte secunde au trecut și oprește jocul când 
timpul se termină. Practic, întrerupe jucătorii și încheie jocul atunci când timpul e gata. La final, în clasa Compulsory, am implementat tot jocul.
Am creat obiectele pentru sacul de piese, board, dicționar și managerul de tururi.
Am creat jucătorii și am pornit fiecare thread al jucătorilor pentru a începe jocul. La final, am calculat cine a câștigat, adunând 
scorurile fiecărui jucător.
