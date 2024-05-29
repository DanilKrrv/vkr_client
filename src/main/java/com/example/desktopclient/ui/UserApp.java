package com.example.desktopclient.ui;


import com.example.desktopclient.database.UserDAO;
import com.example.desktopclient.model.User;

import com.example.desktopclient.database.AttestationDAO;
import com.example.desktopclient.database.UserDAO;
import com.example.desktopclient.model.Attestation;
import com.example.desktopclient.model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class UserApp extends Application {
    private TableView<User> userTableView = new TableView<>();
    private TableView<Attestation> attestationTableView = new TableView<>();
    private UserDAO userDAO = new UserDAO();
    private AttestationDAO attestationDAO = new AttestationDAO();
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<Attestation> attestationData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Management");

        // User table columns
        TableColumn<User, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> loginColumn = new TableColumn<>("Login");
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

        TableColumn<User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> fioColumn = new TableColumn<>("FIO");
        fioColumn.setCellValueFactory(new PropertyValueFactory<>("FIO"));

        userTableView.getColumns().addAll(idColumn, loginColumn, passwordColumn, fioColumn);

        // Attestation table columns
        TableColumn<Attestation, Long> attestationIdColumn = new TableColumn<>("ID");
        attestationIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Attestation, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Attestation, Boolean> isValidColumn = new TableColumn<>("Is Valid");
        isValidColumn.setCellValueFactory(new PropertyValueFactory<>("isValid"));

        TableColumn<Attestation, String> dataPassedColumn = new TableColumn<>("Data Passed");
        dataPassedColumn.setCellValueFactory(new PropertyValueFactory<>("dataPassed"));

        attestationTableView.getColumns().addAll(attestationIdColumn, titleColumn, isValidColumn, dataPassedColumn);

        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadAttestationData(newSelection.getId());
            }
        });

        HBox hbox = new HBox();
        hbox.getChildren().addAll(createUserControls(), createAttestationControls());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox, userTableView, attestationTableView);

        primaryStage.setScene(new Scene(vbox, 800, 600));
        primaryStage.show();

        loadUserData();
    }

    private void loadUserData() {
        try {
            List<User> users = userDAO.getAllUsers();
            userData.setAll(users);
            userTableView.setItems(userData);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading data from database.");
        }
    }

    private void loadAttestationData(Long userId) {
        try {
            List<Attestation> attestations = attestationDAO.getAttestationsByUserId(userId);
            attestationData.setAll(attestations);
            attestationTableView.setItems(attestationData);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading attestation data from database.");
        }
    }

    private VBox createUserControls() {
        TextField loginField = new TextField();
        loginField.setPromptText("Login");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        TextField fioField = new TextField();
        fioField.setPromptText("FIO");

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            String login = loginField.getText();
            String password = passwordField.getText();
            String fio = fioField.getText();
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setFIO(fio);
            try {
                userDAO.addUser(user);
                loadUserData();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error adding user to database.");
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                try {
                    userDAO.deleteUser(selectedUser.getId());
                    loadUserData();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error deleting user from database.");
                }
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> {
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String login = loginField.getText();
                String password = passwordField.getText();
                String fio = fioField.getText();
                selectedUser.setLogin(login);
                selectedUser.setPassword(password);
                selectedUser.setFIO(fio);
                try {
                    userDAO.updateUser(selectedUser);
                    loadUserData();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error updating user in database.");
                }
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("User Controls"), loginField, passwordField, fioField, addButton, deleteButton, updateButton);
        return vbox;
    }

    private VBox createAttestationControls() {
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        CheckBox isValidCheckbox = new CheckBox("Is Valid");

        TextField dataPassedField = new TextField();
        dataPassedField.setPromptText("Data Passed");

        Button addAttestationButton = new Button("Add Attestation");
        addAttestationButton.setOnAction(event -> {
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String title = titleField.getText();
                Boolean isValid = isValidCheckbox.isSelected();
                String dataPassed = dataPassedField.getText();
                Attestation attestation = new Attestation();
                attestation.setTitle(title);
                attestation.setIsValid(isValid);
                attestation.setDataPassed(dataPassed);
                attestation.setUserId(selectedUser.getId());
                try {
                    attestationDAO.addAttestation(attestation);
                    loadAttestationData(selectedUser.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error adding attestation to database.");
                }
            }
        });

        Button deleteAttestationButton = new Button("Delete Attestation");
        deleteAttestationButton.setOnAction(event -> {
            Attestation selectedAttestation = attestationTableView.getSelectionModel().getSelectedItem();
            if (selectedAttestation != null) {
                try {
                    attestationDAO.deleteAttestation(selectedAttestation.getId());
                    loadAttestationData(selectedAttestation.getUserId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error deleting attestation from database.");
                }
            }
        });

        Button updateAttestationButton = new Button("Update Attestation");
        updateAttestationButton.setOnAction(event -> {
            Attestation selectedAttestation = attestationTableView.getSelectionModel().getSelectedItem();
            if (selectedAttestation != null) {
                String title = titleField.getText();
                Boolean isValid = isValidCheckbox.isSelected();
                String dataPassed = dataPassedField.getText();
                selectedAttestation.setTitle(title);
                selectedAttestation.setIsValid(isValid);
                selectedAttestation.setDataPassed(dataPassed);
                try {
                    attestationDAO.updateAttestation(selectedAttestation);
                    loadAttestationData(selectedAttestation.getUserId());
                } catch (SQLException e) {
                    e.printStackTrace();
                    showError("Error updating attestation in database.");
                }
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(new Label("Attestation Controls"), titleField, isValidCheckbox, dataPassedField, addAttestationButton, deleteAttestationButton, updateAttestationButton);
        return vbox;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
