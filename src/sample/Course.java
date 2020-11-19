package sample;

// class for the course data collected from the database
public class Course {
    private final String CName;
    private final String CID;
    private final Integer Year;
    private final String Semester;
    private final String Lector;

    public Course(String courseName, String courseID, Integer year, String semester, String lector) {
        CName = courseName;
        CID = courseID;
        Year = year;
        Semester = semester;
        Lector = lector;
    }

    public String getCourseID() {
        return CID;
    }


    // makes to-string there display's the selected information to be use in the user interface
    @Override
    public String toString() {
        return CID + " " + CName + " " + Semester + "  " + Year;
    }
}