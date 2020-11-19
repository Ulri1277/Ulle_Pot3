package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.sql.SQLException;

public class Controller {

    // making a box for selection of studentID and CourseID
    public ComboBox comboBoxSearchStudentID;
    public ComboBox comboBoxSearchCourseID;

    // making a box for inserting grades for students with null
    public ComboBox comboBoxStudentsInsertGrade;
    public ComboBox comboBoxCoursesInsertGrade;
    public ComboBox comboBoxGradesInsertGrade;

    // making a box and a display filed for the information on a student
    public Button buttonSearchForInfoStudent;
    public TextArea textAreaInfoStudent;

    // making a box and display field for the information on a course
    public Button buttonStartSearch;
    public TextArea textAreaInfoCourse;

    // button and display field for the null grades, and
    // a box to select the given student with null grade from the course
    public Button buttonInsertGrade;
    public TextArea textAreaInsertGrade;

    // makes observable lists for the three data-types and two for the null students and course
    // so it can be retrieved from the two classes there connects the grades to the courses and students
    ObservableList<Student> students = FXCollections.observableArrayList();

    ObservableList<Course> courses = FXCollections.observableArrayList();

    ObservableList<Student> Null_Grade_Student = FXCollections.observableArrayList();
    ObservableList<Course> Null_Grade_Courses = FXCollections.observableArrayList();


    ObservableList<Grade> addDanishGrades = FXCollections.observableArrayList();


    // Establish a connection between the scene builder and the or program
    public void initialize() {

        // retrieves data from database
        DatabaseRetrievals DatRev = new DatabaseRetrievals();


        // sets the items there can be selected in our combobox
        comboBoxSearchStudentID.setItems(students);
        comboBoxSearchCourseID.setItems(courses);


        comboBoxStudentsInsertGrade.setItems(Null_Grade_Student);
        comboBoxCoursesInsertGrade.setItems(Null_Grade_Courses);


        comboBoxGradesInsertGrade.setItems(addDanishGrades);
        addDanishGrades = addDanishGrades(addDanishGrades);


        // Attempt to connect to a database with a set url and retrieve the data necessary for setup.
        try {
            String url = "jdbc:sqlite:C:/Users/Ulrik/IdeaProjects/FinalPortofolio3/src/Database/Student_Course_Grade";
            DatRev.connections(url);
            DatRev.CreatedStatement();

            DatRev.ClearingOfUpdatingGrade();

            // Imports the student and course table, to be used in the combobox(selection)
            students = DatRev.QueryStatement_Students(students);
            courses = DatRev.QueryStatement_Courses(courses);

            Null_Grade_Student = DatRev.QueryStatement_NullStudents(Null_Grade_Student);
            Null_Grade_Courses = DatRev.QueryStatement_NullCourses(Null_Grade_Courses);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in try-catch line 37");
        }

    }

    // Action for the button press to fetch the information for the selected object
    // and displaying it in textarea connected to the selected button
    public void searchForInfoStudent(ActionEvent actionEvent) throws SQLException {
        textAreaInfoStudent.clear();
        // Error handling in case the button is pressed while no item is selected yet.
        if (comboBoxSearchStudentID.getSelectionModel().getSelectedItem() == null) {
            textAreaInfoStudent.appendText("Please select a student from the list above.\n");
            return;
        }
        textAreaInfoStudent.appendText("You chose Student "
                + comboBoxSearchStudentID.getSelectionModel().getSelectedItem() + ".\n"
                + "(Student Name, Course ID, Grade)\n");

        Student student = (Student) comboBoxSearchStudentID.getSelectionModel().getSelectedItem();
        DatabaseRetrievals DatRev = new DatabaseRetrievals();
        textAreaInfoStudent.appendText(DatRev.PepStatement_Info_Student(student.getStudentID()));
    }

    public void searchForInfoCourse(ActionEvent actionEvent) {
        textAreaInfoCourse.clear();
        // Error handling in case the button is pressed while no item is selected yet.
        if (comboBoxSearchCourseID.getSelectionModel().getSelectedItem() == null) {
            textAreaInfoCourse.appendText("Please select a course from the List above.\n");
            return;
        }
        textAreaInfoCourse.appendText("You chose Course "
                + comboBoxSearchCourseID.getSelectionModel().getSelectedItem() + ".\n"
                + "(Course ID, Average Grade)\n");
        Course courses = (Course) comboBoxSearchCourseID.getSelectionModel().getSelectedItem();
        DatabaseRetrievals DatRev = new DatabaseRetrievals();
        textAreaInfoCourse.appendText(DatRev.PepStatement_Info_Course(courses.getCourseID()));
    }

    // inserts the missing grades with a button press action-event
    public void insertGrade(ActionEvent actionEvent) {
        textAreaInsertGrade.clear();
        if (comboBoxStudentsInsertGrade.getSelectionModel().getSelectedItem() == null ||
                comboBoxCoursesInsertGrade.getSelectionModel().getSelectedItem() == null ||
                comboBoxGradesInsertGrade.getSelectionModel().getSelectedItem() == null) {
            textAreaInsertGrade.appendText("Please make sure that all boxes have an item selected.\n");
            return;
        }
        textAreaInsertGrade.appendText("You chose Student "
                + comboBoxStudentsInsertGrade.getSelectionModel().getSelectedItem() + "\nand course "
                + comboBoxCoursesInsertGrade.getSelectionModel().getSelectedItem() + " to receive grade "
                + comboBoxGradesInsertGrade.getSelectionModel().getSelectedItem() + ".\n");

        Student student = (Student) comboBoxStudentsInsertGrade.getSelectionModel().getSelectedItem();
        Course course = (Course) comboBoxCoursesInsertGrade.getSelectionModel().getSelectedItem();
        Grade grade = (Grade) comboBoxGradesInsertGrade.getSelectionModel().getSelectedItem();

        System.out.println("137 Control" + student.getStudentID() + course.getCourseID() + course.toString() + +grade.getGrade());
        DatabaseRetrievals DatRev = new DatabaseRetrievals();
        textAreaInsertGrade.appendText(DatRev.PepStatement_Insert_Grade(
                student.getStudentID(), course.getCourseID(), grade.getGrade()));

    }

    // a observable list for the grades we are able to give the students
    public ObservableList<Grade> addDanishGrades(ObservableList<Grade> danishGradingScale) {
        Grade unacceptable = new Grade("", "", -03);
        danishGradingScale.add(unacceptable);
        Grade inadequate = new Grade("", "", 00);
        danishGradingScale.add(inadequate);
        Grade adequate = new Grade("", "", 02);
        danishGradingScale.add(adequate);
        Grade fair = new Grade("", "", 04);
        danishGradingScale.add(fair);
        Grade good = new Grade("", "", 07);
        danishGradingScale.add(good);
        Grade veryGood = new Grade("", "", 10);
        danishGradingScale.add(veryGood);
        Grade excellent = new Grade("", "", 12);
        danishGradingScale.add(excellent);
        return danishGradingScale;
    }

}
