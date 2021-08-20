package sample;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Serverpkg.ServerEquipments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class ControllerAddEquipment implements Controller{

    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField equipmentName;

    @FXML
    private TextField ownerName;

    @FXML
    private TextField contact;

    @FXML
    private TextField price;

    public void initData(User user) {
        this.currentUser = user;
    }


   @FXML
    public void addEquipment(ActionEvent event) throws UnknownHostException, IOException {
        int amount=0;
        String nam = equipmentName.getText();
        String own = ownerName.getText();
        String con = contact.getText();
        String pri = price.getText();


        if (nam.length() == 0 || own.length() == 0 || con.length() == 0 || pri.length() == 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
        } else {
            try {
                amount = Integer.parseInt(pri);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Price");
                alert.setHeaderText("Please insert a valid price.");
                alert.showAndWait();
            } finally {
                if (this.currentUser.getPermission() > 1) {
                   List<String> request = new ArrayList<>();
                   request.add("add_equipment");
                   request.add(nam);
                   request.add(own);
                   request.add(con);
                   request.add(pri);
                   ArrayList<Equipment> equipments = new ArrayList<>();


                    try {
                        equipments = ServerEquipments.run(request);
                        Alert alert = new Alert(AlertType.INFORMATION);
                        if (equipments.size() !=0) {
                            alert.setTitle("Equipment added!");
                            alert.setHeaderText(
                                    String.format("Equipment %s by %s has been added.\nID: %s",
                                            equipments.get(0).getEquipmentName(), equipments.get(0).getOwner(),
                                            equipments.get(0).getEquipmentId()));
                        } else {
                            alert.setTitle("Equipment already inserted");
                            alert.setHeaderText("Equipment is already present in the database.");
                        }
                        alert.showAndWait();
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
            }
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("homepage_employee");
    }

}