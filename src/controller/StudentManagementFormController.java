package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
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

        tblStudents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (tblStudents.getSelectionModel().getSelectedItem() != null){
                    TableView.TableViewSelectionModel selectionModel = tblStudents.getSelectionModel();
                    ObservableList selectionCells = selectionModel.getSelectedCells();
                    TablePosition tablePosition = (TablePosition) selectionCells.get(0);
                    Object val = tablePosition.getTableColumn().getCellData(newValue);

                    try {
                        ResultSet resultSet = CrudUtil.execute("SELECT * FROM student WHERE student_id=?", val);
                        while (resultSet.next()){
                            txtStudentID.setText(resultSet.getString(1));
                            txtStudentName.setText(resultSet.getString(2));
                            txtEmail.setText(resultSet.getString(3));
                            txtContact.setText(resultSet.getString(4));
                            txtAddress.setText(resultSet.getString(5));
                            txtNic.setText(resultSet.getString(6));
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
        if (txtStudentID.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please insert all data !").show();
        }else {
            Student stud = new Student(
                    txtStudentID.getText(), txtStudentName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText()
            );
            try {
                if (CrudUtil.execute("INSERT INTO student VALUES (?, ?, ?, ?, ?, ?)", stud.getStudent_id(), stud.getStudent_name(), stud.getEmail(), stud.getContact(), stud.getAddress(), stud.getNic())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Details are Saved !").show();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Details are not Saved !").show();
                }
            } catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            try {
                loadAllStudents();
            } catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
            clearFields();
        }
    }

    public void updateStudentOnAction(ActionEvent actionEvent) {
        Student upStud = new Student(
                txtStudentID.getText(), txtStudentName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNic.getText()
        );
        try {
            if (CrudUtil.execute("UPDATE student SET student_name=? , email=? , contact=? , address=? , nic=? WHERE student_id=?",  upStud.getStudent_name(), upStud.getEmail(), upStud.getContact(), upStud.getAddress(), upStud.getNic(), upStud.getStudent_id())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Details are Updated !").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Didn't Updated !").show();
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            loadAllStudents();
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        clearFields();
    }

    public void deleteStudentOnAction(ActionEvent actionEvent) {
        try {
            if (CrudUtil.execute("DELETE FROM student WHERE student_id=?", txtStudentID.getText())){
               new Alert(Alert.AlertType.CONFIRMATION, "Deleted..!").show();
               clearFields();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields(){
        txtContact.clear();
        txtNic.clear();
        txtAddress.clear();
        txtStudentName.clear();
        txtStudentID.clear();
        txtEmail.clear();
    }
}
