package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Serverpkg.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class ControllerLogin implements Controller{
    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;


    public Boolean isMail(String mail) {
        String mailRegex = "\\w+@\\w+\\.\\w+";
        Pattern mailValidator = Pattern.compile(mailRegex);
        Matcher mailMatcher = mailValidator.matcher(mail);
        return mailMatcher.matches();
    }


    @FXML
    public void loadRegister(ActionEvent event) throws IOException {
        Loader loader = new Loader(new User(),this.rootPane);
        loader.load("register");
    }


    @FXML
    public void login(ActionEvent event) throws IOException {
        String mail = email.getText();
        String pass = password.getText();

        try {
            // checks if the email and the password have been inserted
            if (mail.length() > 0 && pass.length() > 0) {
                // checks if the email is valid
                if (isMail(mail)) {
                    List<String> request = new ArrayList<>();
                    request.add("login");
                    request.add(mail);
                    request.add(pass);

                    ArrayList<User> response = Server.run(request);
                    User user = response.get(0);
                    int permission = user.getPermission();
                    this.currentUser = user;

                    Loader loader = new Loader(this.currentUser, this.rootPane);
                    // different cases are distinguished based on the User's permission
                    switch (permission) {
                        // permission = 1 ->it's a user and the user's homepage is loaded
                        case 1:
                            loader.load("homepage_user");
                            break;

                        // permission = 2 ->it's an employee and the employee's homepage is loaded
                        case 2:
                            loader.load("homepage_employee");
                            break;

                        // permission = 3 ->it's an admin and the admin's homepage is loaded
                        case 3:
                            loader.load("homepage_admin");
                            break;

                        default:
                            // notifies the user if a wrong email or password are inserted
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Wrong login");
                            alert.setHeaderText("Email or password are wrong. Please retry.");
                            alert.showAndWait();
                            password.clear();
                            break;
                    }
                } else {
                    // notifies the user if the email is not valid
                    myThread mt = new myThread();
                    mt.run();
                }
            } else {
                // notifies if some fields are not filled
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("All fields must be filled");
                alert.setHeaderText("Please fill all the fields");
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Page Missing");
            alert.setHeaderText("Page is unreachable. Try again later");
            alert.showAndWait();

        }catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Cannot connect");
            alert.setHeaderText("Try again later.");
            alert.showAndWait();
        }
    }

    @FXML
    public void guestLogin(ActionEvent event) throws IOException {
        try {
            List<String> request = new ArrayList<>();
            request.add("guest");
            ArrayList<User> response = Server.run(request);
            response.get(0);

            Loader loader = new Loader(this.currentUser, this.rootPane);
            loader.load("homepage_user");

        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Page Missing");
            alert.setHeaderText("Page is unreachable. Try again later");
            alert.showAndWait();
        }catch(Exception e){
            System.out.println(e);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Cannot connect");
            alert.setHeaderText("Try again later.");
            alert.showAndWait();
        }

    }

    @Override
    public void initData(User user) {
        this.currentUser = user;
    }

}

class myThread extends Thread{
    public void run(){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Email not valid");
        alert.setHeaderText("The provided email is not valid, please retry.");
        alert.showAndWait();
    }
}
