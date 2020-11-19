package sample;

// saves the retrives data from the database by creating object.
public class Student {
    private final String Name;
    private final String SID;


    public Student(String SName, String StudentID) {
        Name = SName;
        SID = StudentID;
    }

    public String getStudentID() {
        return SID;
    }

    @Override
    public String toString() {
        return SID + '\'' + Name + '\'';
    }

}

