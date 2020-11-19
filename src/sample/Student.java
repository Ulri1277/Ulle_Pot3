package sample;

// saves the retrieves data in a object for students
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
        return SID + ": " + Name;
    }

}

