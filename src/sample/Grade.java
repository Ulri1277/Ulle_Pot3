package sample;

// makes a class for the retrieved information on the grades
public class Grade {
    private final String SID;
    private final String CID;
    private final Integer Grade;

    public Grade(String studentID, String courseID, Integer Grade) {
        this.SID = studentID;
        this.CID = courseID;
        this.Grade = Grade;
    }


    public Integer getGrade() {
        return Grade;
    }

    // to-string there there are used to display the information there are retrieved
    @Override
    public String toString() {
        return "" + Grade;
    }

}

