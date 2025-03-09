import java.util.List;
enum Tip{
    THEORETICAL, PRACTICAL;
}

class Project{
    private Tip tip;
    private String name;

    public Project(Tip tip, String name){
        this.tip = tip;
        this.name = name;
    }
    public void setTip(Tip tip) {
        this.tip = tip;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Tip getTip() {
        return tip;
    }
    public String getName() {
        return name;
    }
    public String toString(){
        return name + " " + tip;
    }

}

class Student{
    private String FirstName;
    private String LastName;
    private Project project;
    private List<Project> preferences;

    public Student(String FirstName, String LastName, Project project, List<Project> preferences){
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.project = project;
        this.preferences = preferences;
    }
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    public void setName(String lastName, String firstName) {
        this.LastName = lastName;
        this.FirstName = firstName;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public void setPreferences(List<Project> preferences) {
        this.preferences = preferences;
    }
    public String getFirstName() {
        return FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public Project getProject() {
        return project;
    }
    public List<Project> getPreferences() {
        return preferences;
    }
    public void addPreference(Project project){
        preferences.add(project);
    }
    public String toString(){
        return FirstName + " " + LastName +" " + project + " " + preferences;
    }
}


public class compulsory {
    public static void main(String[] args){
        Project project1 = new Project(Tip.PRACTICAL, "p1");
        Project project2 = new Project(Tip.THEORETICAL, "p2");
        Project project3 = new Project(Tip.PRACTICAL, "p3");
        Project project4 = new Project(Tip.THEORETICAL, "p4");
        Project project5 = new Project(Tip.PRACTICAL, "p5");

        Student student1 = new Student("s1", "s1", project1, List.of(project1, project2, project3));
        Student student2 = new Student("s2", "s2", project2, List.of(project2, project3, project4));
        Student student3 = new Student("s3", "s3", project3, List.of(project3, project4, project5));
        Student student4 = new Student("s4", "s4", project4, List.of(project4, project5, project1));
        Student student5 = new Student("s5", "s5", project5, List.of(project5, project1, project2));

        System.out.println(student1);
        System.out.println(student2);
        System.out.println(student3);
        System.out.println(student4);
        System.out.println(student5);
    }
}
