package sample;

public class Grade {
    private String SID;
    private String CID;
    private Integer Grade;

    public Grade(String studentID, String courseID, Integer Grade) {
        this.SID = studentID;
        this.CID = courseID;
        this.Grade = Grade;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public Integer getGrade() {
        return Grade;
    }

    public void setGrade(Integer grade) {
        Grade = grade;
    }

    @Override
    public String toString() {
        return "" + Grade;
    }

}

