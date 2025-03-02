package ainlabtask;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfBirthDate;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfAllergic;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<Student> tvStudent;
    @FXML
    private TableColumn<Student, Integer> colID;
    @FXML
    private TableColumn<Student, String> colName;
    @FXML
    private TableColumn<Student, String> colBirth;
    @FXML
    private TableColumn<Student, String> colAge;
    @FXML
    private TableColumn<Student, String> colAddress;
    @FXML
    private TableColumn<Student, String> colAllergic;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnSearch;

    public FXMLDocumentController() {
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnAdd) {
            insertRecord();
        } else if (event.getSource() == btnUpdate) {
            updateRecord();
        } else if (event.getSource() == btnDelete) {
            deleteRecord();
        } else if (event.getSource() == btnReset) {
            resetRecord();
        } else if (event.getSource() == btnSearch) {
            String query = tfSearch.getText().trim().toLowerCase();
            if (!query.isEmpty()) {
                search(query);
            } else {
                showStudent();
            }
        }
    }

    @FXML
    void handleMouseAction(MouseEvent event) {
        Student stud = tvStudent.getSelectionModel().getSelectedItem();
        if (stud != null) {
            tfID.setText(String.valueOf(stud.getID()));
            tfName.setText(stud.getName());
            tfBirthDate.setText(stud.getBirth_date());
            tfAge.setText(stud.getAge());
            tfAddress.setText(stud.getAddress());
            tfAllergic.setText(stud.getAllergies());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showStudent();
    }

    private void insertRecord() {
        DatabaseConnection db = new DatabaseConnection();
        db.addStudent(
            tfName.getText(),
            tfBirthDate.getText(),
            tfAge.getText(),
            tfAddress.getText(),
            tfAllergic.getText()
        );
        showStudent();
    }

    private void updateRecord() {
    Student selectedStudent = tvStudent.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
        // Update the selected student object with the new values from text fields
        selectedStudent.setName(tfName.getText());
        selectedStudent.setBirth_date(tfBirthDate.getText());
        selectedStudent.setAge(tfAge.getText());
        selectedStudent.setAddress(tfAddress.getText());
        selectedStudent.setAllergies(tfAllergic.getText());

        // Use the updated student object for the update operation
        DatabaseConnection db = new DatabaseConnection();
        db.updateStudent(selectedStudent);
        showStudent();  // Refresh the student list after updating
    } else {
        System.out.println("No student selected for update.");
    }
}


    private void deleteRecord() {
    Student selectedStudent = tvStudent.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
        // Perform delete operation only if a student is selected
        DatabaseConnection db = new DatabaseConnection();
        db.deleteStudent(selectedStudent.getID());
        showStudent();  // Refresh the list after deletion
    } else {
        // Show an alert if no student is selected
        System.out.println("No student selected for deletion.");
    }
}


    private void resetRecord() {
        tfID.clear();
        tfName.clear();
        tfBirthDate.clear();
        tfAge.clear();
        tfAddress.clear();
        tfAllergic.clear();
    }

    private void search(String query) {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        for (Student student : tvStudent.getItems()) {
            if (student.getName().toLowerCase().contains(query) ||
                student.getBirth_date().toLowerCase().contains(query) ||
                student.getAge().toLowerCase().contains(query) ||
                student.getAddress().toLowerCase().contains(query) ||
                student.getAllergies().toLowerCase().contains(query) ||
                String.valueOf(student.getID()).equals(query)) {
                filteredList.add(student);
            }
        }
        tvStudent.setItems(filteredList);
    }

    public void showStudent() {
        ObservableList<Student> studentList = getStudentList();
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBirth.setCellValueFactory(new PropertyValueFactory<>("birth_date"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAllergic.setCellValueFactory(new PropertyValueFactory<>("allergies"));
        tvStudent.setItems(studentList);
    }

    public ObservableList<Student> getStudentList() {
        ObservableList<Student> studList = FXCollections.observableArrayList();
        DatabaseConnection db = new DatabaseConnection();
        studList.addAll(db.getAllStudents());
        return studList;
    }
}
