package sample;
import Serverpkg.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;

public class ControllerAddEmployee implements Controller {

    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    public void initData(User user) {
        this.currentUser = user;
    }

    public Boolean isMail(String mail) {
        String mailRegex = "\\w+@\\w+\\.\\w+";
        Pattern mailValidator = Pattern.compile(mailRegex);
        Matcher mailMatcher = mailValidator.matcher(mail);
        return mailMatcher.matches();
    }

    @FXML
    public void addEmployee(ActionEvent event) throws UnknownHostException, IOException {
        // gets data
        String nam = name.getText();
        String sur = surname.getText();
        String mail = email.getText();
        String pass = password.getText();

        if (nam.length() == 0 || sur.length() == 0 || mail.length() == 0 || pass.length() == 0) {
            // all fields are required
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields.");
            alert.showAndWait();
        } else if (!isMail(mail)) {
            // email is not valid
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Email not valid");
            alert.setHeaderText("The provided email is not valid, please retry.");
            alert.showAndWait();
        } else {
            if (this.currentUser.getPermission() > 2) {
                // user is authorized to perform the action
                List<String> request = new ArrayList<>();
                request.add("register_employee");
                request.add(nam);
                request.add(sur);
                request.add(mail);
                request.add(pass);
                ArrayList<User> response = Server.run(request);

                try {
                    User newEmployee = response.get(0);
                    int permission = newEmployee.getPermission();

                    if (permission < 1) {
                        // permission < 1 = nullUser => user is already registered.
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Already registered");
                        alert.setHeaderText("There is already an account with this email.");
                        alert.showAndWait();
                    } else {
                        // employee registered successfully
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Employee registered successfully.");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    System.out.println(e);;
                }
            } else {
                // user is not authorized to perform the action
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Not authorized");
                alert.setHeaderText("You are not allowed to perform this action.");
                alert.showAndWait();
            }

            Loader loader = new Loader(this.currentUser, this.rootPane);
            loader.load("homepage_admin");
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("homepage_admin");
    }
}