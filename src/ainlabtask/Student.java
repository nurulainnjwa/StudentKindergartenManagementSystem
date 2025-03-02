package ainlabtask;

public class Student {
    private int ID;
    private String name;
    private String birth_date;
    private String age;
    private String address;
    private String allergies;

    public Student(int ID, String name, String birth_date, String age, String address, String allergies) {
        this.ID = ID;
        this.name = name;
        this.birth_date = birth_date;
        this.age = age;
        this.address = address;
        this.allergies = allergies;
    }

    public int getID() { return this.ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getBirth_date() { return this.birth_date; }
    public void setBirth_date(String birth_date) { this.birth_date = birth_date; }

    public String getAge() { return this.age; }
    public void setAge(String age) { this.age = age; }

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }

    public String getAllergies() { return this.allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
}
