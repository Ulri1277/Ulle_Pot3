package sample;

import javafx.collections.ObservableList;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DatabaseRetrievals {
    // Makes statement for the connection and our statements
    Connection connection = null;
    Statement statement;

    // Create statements which required for query's in SQL codes, create the connection between database and prg.
    public void CreatedStatement() throws SQLException {
        statement = connection.createStatement();
    }

    // creates the connection between the database and the program
    public void connections(String url) throws SQLException {
        connection = getConnection(url);
    }

    // Establish a connection to our database and a try-catch statement to look for exceptions
    private void ConnectTo() {
        try {
            this.connections("jdbc:sqlite:C:/Users/Ulrik/IdeaProjects/FinalPortofolio3/src/Database/Student_Course_Grade");
        } catch (SQLException S) {
            S.printStackTrace();
        }
    }

    // make a query statement for students and courses there retrieves data and set them into lists
    public ObservableList<Student> QueryStatement_Students(ObservableList<Student> students) throws SQLException {
        System.out.println("Retrieving data on students from database");

        // makes a string for the sql statement there selects which data should be retrieved
        String SQL = "SELECT * FROM Student;";

        // sets the data into a result set as its retired
        ResultSet ResSet_Student = statement.executeQuery(SQL);

        // if-while statement there checks for the result set being null or if the element already exists
        if (ResSet_Student == null) {
            System.out.println("There are no data to be retrieved");

        }
        while (ResSet_Student != null && ResSet_Student.next()) {
            String SID = ResSet_Student.getString(1);
            String Name = ResSet_Student.getString(2);


            System.out.println(Name + " " + SID);

            // make a observable list there saves the retrieved data
            Student student = new Student(Name, SID);
            students.add(student);
        }
        return students;
    }

    // Prepared statement there allows the user interface to display the retrieved data
    public String PepStatement_Info_Student(String StudentID) throws SQLException {
        String textAreaMessage = "";

        // checks if there are connection to the database
        ConnectTo();

        System.out.println("button event action running ");

        // SQL code there selects the data on all student, their courses and the grade of the given course.
        String SQL_Information_Student = "SELECT S1.StudentName, G1.CID, G1.Grade " +
                "FROM Student AS S1 " +
                "JOIN Grades AS G1 on S1.StudentID = G1.SID " +
                "WHERE StudentID = ?;";

        // SQL for the average grade of a selected student with a uniq studentID
        String SQL_AVG_Student = "SELECT AVG(Grade) " +
                "FROM Grades " +
                " WHERE SID = ?;";

        try {

            // Query for courses and the grades for the student
            PreparedStatement PPStatementStudentINFO = connection.prepareStatement(SQL_Information_Student);
            PPStatementStudentINFO.setString(1, StudentID);

            ResultSet ResSetInfo = PPStatementStudentINFO.executeQuery();
            System.out.println("Prints courses");

            if (ResSetInfo == null) {
                textAreaMessage += "No data to retrieved for the selected course.\n";
            }
            while (ResSetInfo != null && ResSetInfo.next()) {
                if (ResSetInfo.getString(2) == null) {
                    textAreaMessage += ResSetInfo.getString(2) + "   " + ResSetInfo.getString(2) + " (not graded yet)\n";
                } else {
                    textAreaMessage += ResSetInfo.getString(2) + "   " + ResSetInfo.getString(1) + " "
                            + ResSetInfo.getInt(3) + "\n";
                }
            }
            // prepared statement for the student average grade
            PreparedStatement PPAVG = connection.prepareStatement(SQL_AVG_Student);

            PPAVG.setString(1, StudentID);
            ResultSet ReSet_Student_AVG = PPAVG.executeQuery();

            System.out.println("Prints the students av grade");

            if (ReSet_Student_AVG == null) {
                textAreaMessage += " No record";
            }
            while (ReSet_Student_AVG != null && ReSet_Student_AVG.next()) {
                if (ReSet_Student_AVG.getString(1) == null) {
                    textAreaMessage += "Student has not retrieved.";
                } else {
                    textAreaMessage += "Student" + StudentID + "Has Average grade of:" + ReSet_Student_AVG.getFloat(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return textAreaMessage;
    }

    // Makes a Query for the courses and put them into a list
    public ObservableList<Course> QueryStatement_Courses(ObservableList<Course> courses) throws SQLException {
        System.out.println("\nRetrieving data for courses");

        // Gets the data for the datatable and put them into a result set
        String SQL_Courses = "SELECT * FROM Courses;";
        ResultSet ReSet_Courses = statement.executeQuery(SQL_Courses);

        // if-while statement there checks if the statement are not null or in the list already
        if (ReSet_Courses == null) {
            System.out.println("to data to retrieve.");
        }
        while (ReSet_Courses != null && ReSet_Courses.next()) {
            String CourseName = ReSet_Courses.getString(1);
            String CourseID = ReSet_Courses.getString(2);
            Integer Year = ReSet_Courses.getInt(3);
            String Semester = ReSet_Courses.getString(4);
            String Lector = ReSet_Courses.getString(5);

            // Makes a new list there storages the data gotten from the database
            Course course = new Course(CourseName, CourseID, Year, Semester, Lector);
            courses.add(course);
        }
        return courses;
    }

    // Prepared statement to find information about the given student
    public String PepStatement_Info_Course(String CourseID) {
        String textAreaMessage = "  ";

        ConnectTo();

        System.out.println("\nRunning Button for courses");


        String SqlAVGCourse = "SELECT AVG(Grade) " +
                "FROM Grades " +
                "WHERE CID = ?;";

        try {
            PreparedStatement PrepStatement_INFO_Courses = connection.prepareStatement(SqlAVGCourse);
            PrepStatement_INFO_Courses.setString(1, CourseID);
            ResultSet rsCourse = PrepStatement_INFO_Courses.executeQuery();

            if (rsCourse == null) {
                textAreaMessage += "No records for course " + CourseID + " retrieved.\n";
            }
            while (rsCourse != null && rsCourse.next()) {
                if (rsCourse.getString(1) == null) {
                    textAreaMessage += "Course " + CourseID + " has not given grades yet.\n";
                } else {
                    textAreaMessage += CourseID + "  has an average grade of  " + rsCourse.getFloat(1) + "\n";
                }
            }
        } catch (SQLException S) {
            S.printStackTrace();
            System.out.println(S.getMessage());
        }
        return textAreaMessage;
    }

    // Imports the student data where the students have not received a grade yet. and makes them into a observable list
    // which we later can change
    public ObservableList<Student> QueryStatement_NullStudents(ObservableList<Student> NullStudents) throws SQLException {
        System.out.println("\nFetching Null Students...");

        String sql = "SELECT S1.StudentID, S1.StudentName, S1.City, G1.Grade " +
                "FROM Student S1 " +
                "JOIN Grades AS G1 ON S1.StudentID = G1.SID " +
                "WHERE Grade IS NULL;";
        ResultSet ReSet_NullStudents = statement.executeQuery(sql);

        if (ReSet_NullStudents == null) {
            System.out.println("No records retrieved.");
        }
        while (ReSet_NullStudents != null && ReSet_NullStudents.next()) {
            String ID = ReSet_NullStudents.getString(1);
            String Name = ReSet_NullStudents.getString(2);

            System.out.println(ID + " " + Name);

            Student student = new Student(Name, ID);
            NullStudents.add(student);
        }
        return NullStudents;
    }

    // Fecthes the courses there do not have been given grades for its students
    public ObservableList<Course> QueryStatement_NullCourses(ObservableList<Course> NullCourses) throws SQLException {
        System.out.println("\nFetching Null Courses...");

        String sql = "SELECT DISTINCT C1.CourseID, C1.CourseName, C1.Lector, C1.Semester, C1.Year " +
                "FROM Courses AS C1 " +
                "JOIN Grades AS G1 ON C1.CourseID = G1.CID " +
                "WHERE Grade IS NULL;";

        ResultSet ReSet_NullCourse = statement.executeQuery(sql);

        if (ReSet_NullCourse == null) {
            System.out.println("No records retrieved.");
        }
        while (ReSet_NullCourse != null && ReSet_NullCourse.next()) {
            String CourseName = ReSet_NullCourse.getString(2);
            String CourseID = ReSet_NullCourse.getString(1);
            Integer Year = ReSet_NullCourse.getInt(3);
            String Semester = ReSet_NullCourse.getString(4);
            String Lector = ReSet_NullCourse.getString(5);

            //System.out.println(CourseName + " " + CourseID + " " + Lector + " " + Semester + " " + Year);
            Course course = new Course(CourseName, CourseID, Year, Semester, Lector);
            NullCourses.add(course);
        }
        return NullCourses;
    }

    // Updates the selected students grade for a given course if there has not been given a grade.
    public String PepStatement_Insert_Grade(String StudentID, String CourseID, Integer Grade) {
        String textAreaMessage = "";

        ConnectTo();

        System.out.println("\nRunning Button for inserting grades...");
        System.out.println(StudentID + CourseID + Grade);


        String sqlInsertGrade = "UPDATE Grades SET Grade = ? WHERE SID = ? AND CID = ? AND Grade IS NULL;";

        try {
            PreparedStatement pstmtInsertGrade = connection.prepareStatement(sqlInsertGrade);
            pstmtInsertGrade.setInt(1, Grade);
            pstmtInsertGrade.setString(2, StudentID);
            pstmtInsertGrade.setString(3, CourseID);
            Integer rsInsertGrade = pstmtInsertGrade.executeUpdate();

            if (rsInsertGrade == 0) {
                textAreaMessage += "Student" + StudentID + " has already received a grade in course "
                        + CourseID + ".\n";
            }
            if (rsInsertGrade == 1) {
                textAreaMessage += "Added new grade for Student " + StudentID + " in course " + CourseID
                        + ": " + Grade + "\n";
            } else if (rsInsertGrade > 1) {
                textAreaMessage += "Error: Too many grades added. " +
                        "Please save the settings above and consult your IT department.\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return textAreaMessage;
    }

    // makes sure that the added grades are cleared form the database as the user interface are closed
    public void ClearingOfUpdatingGrade() throws SQLException {
        System.out.println("\nCleaning");

        String sqlS = "UPDATE Grades SET Grade = NULL WHERE CID = 'SDS' AND Grade IS NOT NULL;";
        Integer rsClearCourse = statement.executeUpdate(sqlS);

        if (rsClearCourse != null) {
            System.out.println("Changed " + rsClearCourse + " rows.");
        }
    }

}


