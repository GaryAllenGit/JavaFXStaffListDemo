package staffListDemo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffListGUI extends Application {

    private StaffList listOfStaff;
    private ListView<Employee> staffList;
    private TextField idTF;
    private TextField surnameTF;
    private TextField firstnameTF;
    private TextField departmentTF;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Initialise the list of staff with some test data
        listOfStaff = new StaffList();
        listOfStaff.addEmployee("S001", "Gary", "Allen", "CompSci");
        listOfStaff.addEmployee("S002", "Hugh", "Osborne", "CompSci");
        listOfStaff.addEmployee("S003", "Diane", "Kitchen", "CompSci");
        listOfStaff.addEmployee("S004", "Simon", "Parkinson", "CompSci");
        listOfStaff.addEmployee("S005", "Mauro", "Valatti", "CompSci");

        // Initialise the staffList GUI component
        staffList = new ListView<>();
        staffList.setPrefSize(280,220);

        staffList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                staffListSelectionMade();
            }
        });

        staffList.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                staffListSelectionMade();
            }
        });

        // bind the list of staff to the staffList GUI component
        staffList.setItems(listOfStaff);


        // build the rest of the interface
        // start with the Title at the top
        HBox topBox = new HBox(new Label("Staff List Editor"));
        topBox.setAlignment(Pos.CENTER);

        // now the centre.  We have 2 VBoxes side by side
        // first the one to contain the staffList
        VBox centreLeftBox = new VBox(staffList);

        // now the one to contain the details of each employee
        idTF = new TextField();
        firstnameTF = new TextField();
        surnameTF = new TextField();
        departmentTF = new TextField();

        VBox centreRightBox = new VBox(
                new Label("ID"), idTF,
                new Label("First Name"), firstnameTF,
                new Label("Surname"), surnameTF,
                new Label("Department"), departmentTF);
        centreRightBox.setAlignment(Pos.CENTER);
        centreRightBox.setSpacing(2);

        // put the 2 VBoxes into a HBox
        HBox middleBox = new HBox(centreLeftBox, centreRightBox);
        middleBox.setSpacing(5);
        middleBox.setPadding(new Insets(20));

        // now the bottom section containing 4 buttons
        Button addButton = new Button("Add");
        addButton.setPrefSize(90,20);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addButtonPressed();
            }
        });

        Button removeButton = new Button("Remove");
        removeButton.setPrefSize(90,20);
        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeButtonPressed();
            }
        });

        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(90,20);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearButtonPressed();
            }
        });

        Button findButton = new Button("Find");
        findButton.setPrefSize(90,20);
        findButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                findButtonPressed();
            }
        });

        HBox lowerBox = new HBox(addButton, removeButton, clearButton, findButton);
        lowerBox.setAlignment(Pos.CENTER);
        lowerBox.setSpacing(10);

        // put all 3 HBoxes into a VBox
        VBox fullGUI = new VBox(topBox, middleBox, lowerBox);

        // put the VBox into a scene and the scene into the primary stage
        Scene scene = new Scene(fullGUI, 500, 350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private void staffListSelectionMade() {
        Employee selectedEmp = staffList.getSelectionModel().getSelectedItem();
        if (selectedEmp != null) {
            setAllTextFields(selectedEmp);
        }
    }

    private void addButtonPressed() {
        String newID = idTF.getText();
        String newSurname = surnameTF.getText();
        String newFirstname = firstnameTF.getText();
        String newDept = departmentTF.getText();

        if (newID.equals("") || newSurname.equals("") ||
                newFirstname.equals("") || newDept.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter Full Details", ButtonType.OK);
            alert.showAndWait();
        } else {
            listOfStaff.addEmployee(newID, newFirstname, newSurname, newDept);
            Employee newEmp = listOfStaff.findEmployeeByID(newID);
            staffList.getSelectionModel().select(newEmp);
            staffList.scrollTo(newEmp);
            setAllTextFields(newEmp);
        }
    }

    private void removeButtonPressed() {
        int selectedIndex = staffList.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select an Employee", ButtonType.OK);
            alert.showAndWait();
        } else {
            Employee toGo = staffList.getItems().get(selectedIndex);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + toGo + " ?",
                    ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                listOfStaff.removeEmployee(toGo.getID());
                clearAllTextFields();
                staffList.getSelectionModel().clearSelection();
            }
        }
    }

    private void clearButtonPressed() {
        clearAllTextFields();
        staffList.getSelectionModel().clearSelection();
    }

    private void findButtonPressed() {
        Employee searchedFor;

        String findID = idTF.getText();
        String findName = surnameTF.getText();

        if (findID.equals("") && findName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter an ID or a Surname", ButtonType.OK);
            alert.showAndWait();
        } else {
            if (!findID.equals("")) {
                searchedFor = listOfStaff.findEmployeeByID(findID);
            } else {
                searchedFor = listOfStaff.findEmployeeByName(findName);
            }
            if (searchedFor == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Employee Not Found", ButtonType.OK);
                alert.showAndWait();
            } else {
                setAllTextFields(searchedFor);
                staffList.getSelectionModel().select(searchedFor);
            }
        }
    }

    private void clearAllTextFields() {
        idTF.setText("");
        firstnameTF.setText("");
        surnameTF.setText("");
        departmentTF.setText("");
    }

    private void setAllTextFields(Employee e){
        idTF.setText(e.getID());
        firstnameTF.setText(e.getFirstname());
        surnameTF.setText(e.getSurname());
        departmentTF.setText(e.getDepartment());
    }
}
