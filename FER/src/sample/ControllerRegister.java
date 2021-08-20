package sample;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import Serverpkg.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class ControllerRegister implements Controller {

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
    public void register(ActionEvent event) throws UnknownHostException, IOException {

        // gets data from the page
        String nam = name.getText();
        String sur = surname.getText();
        String mail = email.getText();
        String pass = password.getText();

        if (nam.length() == 0 || sur.length() == 0 || mail.length() == 0 || pass.length() == 0) {
            // all fields are required
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
        } else if (!isMail(mail)) {
            // checks if the email is valid
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Email not valid");
            alert.setHeaderText("The provided email is not valid, please retry.");
            alert.showAndWait();
        } else {
            List<String> request = new ArrayList<>();
            request.add("register_user");
            request.add(nam);
            request.add(sur);
            request.add(mail);
            request.add(pass);
            ArrayList<User> response = Server.run(request);
            this.currentUser = response.get(0);
            // loads the homepage
            try {
                Loader loader = new Loader(this.currentUser, this.rootPane);
                loader.load("homepage_user");
            } catch (Exception e) {
                System.out.println(e);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Page Missing");
                alert.setHeaderText("Page is unreachable. Try again later");
                alert.showAndWait();
            }
        }
    }
}