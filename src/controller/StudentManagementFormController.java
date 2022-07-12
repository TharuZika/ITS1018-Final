package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;

public class StudentManagementFormController {
    public JFXTextField txtStudentID;
    public JFXTextField txtStudentName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtAddress;
    public JFXTextField txtNic;
    public TableView tblStudents;
    public TableColumn colStudentId;
    public TableColumn colStudentName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;

    public void initialize(){
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));

        try {
            loadAllStudents();
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

    }

    public void loadAllStudents() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM student");
        ObservableList<Student> obList = FXCollections.observableArrayList();

        while (resultSet.next()){
            obList.add(
                    new Student(
                            resultSet.getString("student_id"),
                            resultSet.getString("student_name"),
                            resultSet.getString("email"),
                            resultSet.getString("contact"),
                            resultSet.getString("address"),
                            resultSet.getString("nic")
                    )
            );
        }
        tblStudents.setItems(obList);
    }

    public void addStudentOnAction(ActionEvent actionEvent) {
    }

    public void updateStudentOnAction(ActionEvent actionEvent) {
    }

    public void deleteStudentOnAction(ActionEvent actionEvent) {
    }
}
