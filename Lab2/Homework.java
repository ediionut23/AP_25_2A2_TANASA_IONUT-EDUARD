import java.util.*;
/**
 * Enum care definește tipurile de proiecte disponibile.
 */
enum Type{
    THEORETICAL, PRACTICAL;
}
/**
 * Clasa Proiect reprezintă un proiect propus de un profesor.
 */
class Proiect{
    private String title;
    private Type type;
    private Theacher proposer;
    /**
     * Constructor pentru clasa Proiect.
     *
     * @param title Titlul proiectului.
     * @param type Tipul proiectului (teoretic sau practic).
     * @param proposer Profesorul care a propus proiectul.
     */
    public Proiect(String title, Type type, Theacher proposer){
        this.title = title;
        this.type = type;
        this.proposer = proposer;
    }
    /** @return Titlul proiectului. */
    public String getTitle(){
        return title;
    }
    /** @return Tipul proiectului. */
    public Type getType(){
        return type;
    }
    /** @return Profesorul care a propus proiectul. */
    public Theacher getProposer(){
        return proposer;
    }
    /** @return O reprezentare a obiectului ca string. */
    public String toString(){
        return "Title: " + title + " Type: " + type + " Proposer: " + proposer;
    }
}
/**
 * Clasa Person reprezintă o persoană generală, care poate fi student sau profesor.
 */
class Person {
    private String name;
    private String dataNasterii;
    /**
     * Constructor pentru clasa Person.
     *
     * @param name Numele persoanei.
     * @param dataNasterii Data nașterii persoanei.
     */
    public Person(String name, String dataNasterii) {
        this.name = name;
        this.dataNasterii = dataNasterii;
    }
    /** @return Numele persoanei. */
    public String getName() {
        return name;
    }
    /** @return Data nașterii persoanei. */
    public String getDataNasterii() {
        return dataNasterii;
    }
    /** @return O reprezentare a obiectului ca string. */
    public String toString() {
        return "Name: " + name + " Data nasterii: " + dataNasterii;
    }
    /** @return {@code true} dacă două persoane sunt egale, altfel {@code false}. */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
        return name.equals(person.name) && dataNasterii.equals(person.dataNasterii);

    }
}
/**
 * Clasa Student reprezintă un student cu preferințe pentru anumite proiecte.
 */
class Student extends Person{
    private int registrationNumber;
    private Proiect[] preferences;
    /**
     * Constructor pentru clasa Student.
     *
     * @param name Numele studentului.
     * @param dataNasterii Data nașterii studentului.
     * @param registrationNumber Numărul de înregistrare al studentului.
     * @param preferences Lista de proiecte preferate de student.
     */
    public Student(String name, String dataNasterii, int registrationNumber, Proiect[] preferences){
        super(name, dataNasterii);
        this.registrationNumber = registrationNumber;
        this.preferences = preferences;
    }
    /** @return Numărul de înregistrare al studentului. */
    public int getRegistrationNumber(){
        return registrationNumber;
    }
    /** @return Lista de proiecte preferate. */
    public Proiect[] getPreferences(){
        return preferences;
    }
    /** @return O reprezentare a obiectului ca string. */
    public String toString(){
        return "Name: " + getName() + " Data nasterii: " + getDataNasterii() + " Registration number: " + registrationNumber + " Preferences: " + Arrays.toString(preferences);
    }
}

/**
 * Clasa Theacher reprezintă un profesor care poate propune proiecte.
 */
class Theacher extends Person{
    private Proiect[] proiectePropuse;
    /**
     * Constructor pentru clasa Theacher.
     *
     * @param name Numele profesorului.
     * @param dataNasterii Data nașterii profesorului.
     * @param proiectePropuse Lista proiectelor propuse de profesor.
     */
    public Theacher(String name, String dataNasterii, Proiect[] proiectePropuse) {
        super(name, dataNasterii);
        this.proiectePropuse = proiectePropuse;
    }
    /** @return Lista proiectelor propuse de profesor. */
    public Proiect[] getProiectePropuse(){
        return proiectePropuse;
    }
}
/**
 * Clasa Problem reprezintă problema de alocare a studenților la proiecte.
 */
class Problem{
    private Student[] studenti;
    private Proiect[] proiecte;
    private Theacher[] theachers;
    /**
     * Constructor pentru clasa Problem.
     *
     * @param studenti Lista de studenți.
     * @param proiecte Lista de proiecte disponibile.
     * @param theachers Lista profesorilor.
     */
    public Problem(Student[] studenti, Proiect[] proiecte, Theacher[] theachers){
        this.studenti = studenti;
        this.proiecte = proiecte;
        this.theachers = theachers;
    }
    /** @return Lista de studenți. */
    public Student[] getStudenti(){
        return studenti;
    }
    /** @return Lista de proiecte. */
    public Proiect[] getProiecte(){
        return proiecte;
    }
    /** @return Lista de persoane. */
    public Person[] getAllPersons(){
        Person[] persons = new Person[studenti.length + theachers.length];
        for(int i = 0; i < studenti.length; i++){
            persons[i] = studenti[i];
        }
        for(int i = 0; i < theachers.length; i++){
            persons[i + studenti.length] = theachers[i];
        }
        return persons;
    }
}
/**
 * Clasa Solution reprezintă o soluție pentru problema de alocare.
 */
class Solution{
    private Problem problem;
    private Student[] assignedStudent;
    private Proiect[] assignedProiect;
    /**
     * Constructor pentru clasa Solution.
     *
     * @param problem Problema care trebuie rezolvată.
     */
    public Solution(Problem problem){
        this.problem = problem;
        assignedStudent = new Student[problem.getStudenti().length];
        assignedProiect = new Proiect[problem.getStudenti().length];
    }
    /**
     * Aplică algoritmul de alocare a proiectelor pentru studenți.
     */
    public void allocateProjects(){
        boolean[] projectAssigned = new boolean[problem.getProiecte().length];
        for(Student student : problem.getStudenti()){
            for(Proiect prefferedProiect : student.getPreferences()){
                int proiectIndex = Arrays.asList(problem.getProiecte()).indexOf(prefferedProiect);
                if(!projectAssigned[proiectIndex]){
                    int studentIndex = Arrays.asList(problem.getStudenti()).indexOf(student);
                    assignedStudent[studentIndex] = student;
                    assignedProiect[studentIndex] = prefferedProiect;
                    projectAssigned[proiectIndex] = true;
                    break;
                }
            }
        }
    }
    /**
     * Afiseaza rezultatul dupa alocare.
     */
    public void printSolution() {
        for (int i = 0; i < assignedStudent.length; i++) {
            if (assignedStudent[i] != null && assignedProiect[i] != null) {
                System.out.println(assignedStudent[i].getName() + " -> " + assignedProiect[i].getTitle());
            } else {
                System.out.println("Student " + (i + 1) + " has not been assigned a project.");
            }
        }
    }
    /**
     * Verifică dacă Teorema lui Hall este satisfăcută.
     *
     * @return {@code true} dacă teorema este satisfăcută, altfel {@code false}.
     */
    public boolean teoremaHall(){
        int n = problem.getStudenti().length;
        for(int subsetSize = 1; subsetSize <= n; subsetSize++){
            int[] indici = new int[subsetSize];
            for(int i = 0; i < subsetSize; i++){
                indici[i] = i;
            }
            while(indici[0] < n - subsetSize){
                Set<Proiect> proiecteUnice = new HashSet<>();
                for(int index: indici){
                    proiecteUnice.addAll(Arrays.asList(problem.getStudenti()[index].getPreferences()));
                }
                if(proiecteUnice.size() < subsetSize){
                    return false;
                }

                int pos = subsetSize - 1;
                while(pos >= 0 && indici[pos] == n - subsetSize + pos){
                    pos--;
                }
                if(pos >= 0){
                    indici[pos]++;
                    for(int i = pos + 1; i < subsetSize; i++){
                        indici[i] = indici[i - 1] + 1;
                    }
                }else break;
            }
        }
        return true;
    }
}
/**
 * Clasa principală Homework care rulează programul.
 */
public class Homework {
    public static void main(String[] args) {
        Theacher theacher1 = new Theacher("Dr. Smith", "1975-06-21", new Proiect[0]);
        Theacher theacher2 = new Theacher("Prof. Johnson", "1969-11-04", new Proiect[0]);

        Proiect proiect1 = new Proiect("AI Research", Type.THEORETICAL, theacher1);
        Proiect proiect2 = new Proiect("Software Development", Type.PRACTICAL, theacher2);
        Proiect proiect3 = new Proiect("Cyber Security", Type.THEORETICAL, theacher1);
        Proiect proiect4 = new Proiect("Data Science", Type.PRACTICAL, theacher2);


        Student student1 = new Student("Alice", "2000-02-15", 101, new Proiect[]{proiect1, proiect2});
        Student student2 = new Student("Bob", "1999-07-20", 102, new Proiect[]{proiect2, proiect3});
        Student student3 = new Student("Charlie", "2001-05-10", 103, new Proiect[]{proiect3});
        Student student4 = new Student("David", "2000-12-01", 104, new Proiect[]{proiect4});

        Problem problem = new Problem(new Student[]{student1, student2, student3, student4}, new Proiect[]{proiect1, proiect2, proiect3, proiect4}, new Theacher[]{theacher1, theacher2});
        Solution solution = new Solution(problem);
        if(solution.teoremaHall()) {
            System.out.println("The Hall's theorem is satisfied.");
        }

        else{
            System.out.println("The Hall's theorem is not satisfied.");
        }
        solution.allocateProjects();
        solution.printSolution();

    }
    }
