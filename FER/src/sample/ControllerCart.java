package sample;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import Serverpkg.ServerEquipments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

class NotSelectedException extends Exception{
    private String msg;
    public NotSelectedException(String msg){
        this.msg = msg;
    }

    public String toString(){
        return msg;
    }
}

public class ControllerCart implements Controller {

    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Equipment> tableView;

    @FXML
    private TableColumn<Equipment, String> nameColumn;

    @FXML
    private TableColumn<Equipment, String> ownerColumn;

    @FXML
    private TableColumn<Equipment, String> contactColumn;

    @FXML
    private TableColumn<Equipment, Integer> priceColumn;


    public void initData(User user) {
        this.currentUser = user;

        try {
            ArrayList<Equipment> equipments = new ArrayList<>();
            List<String> request = new ArrayList<>();
            request.add("display_cart");
            request.add(this.currentUser.getEmail());
            equipments = ServerEquipments.run(request);
            addToTable(equipments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addToTable(ArrayList<Equipment> equipments) {
        // set up the columns in the table
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("equipmentName"));
        this.ownerColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("owner"));
        this.contactColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("contact"));
        this.priceColumn.setCellValueFactory(new PropertyValueFactory<Equipment,Integer>("price"));
        ObservableList<Equipment> oListEquipment = FXCollections.observableArrayList(equipments);
        // load data
        tableView.setItems(oListEquipment);
    }


    @FXML
    public void back(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("homepage_user");
    }


    @FXML
    public void buy(ActionEvent event) throws IOException, UnknownHostException {
        if (this.currentUser.getPermission() > 0) {

            try {
                List<String> request = new ArrayList<>();
                request.add("display_cart");
                request.add(this.currentUser.getEmail());
                ArrayList<Equipment> equipments = ServerEquipments.run(request);
                request.clear();
                request.add(this.currentUser.getEmail());
                Boolean response = ServerEquipments.orderRun(request,equipments);
                if (!response) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Order failed!");
                    alert.showAndWait();
                } else {
                    request.clear();
                    request.add("remove_all_from_cart");
                    request.add(this.currentUser.getEmail());
                    response = ServerEquipments.run(request, new Equipment());
                    request.clear();

                    request.add("display_cart");
                    request.add(this.currentUser.getEmail());
                    equipments = ServerEquipments.run(request);
                    addToTable(equipments);

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Order submitted!");
                    alert.setHeaderText(String.format("Your order has been placed!"));
                    alert.showAndWait();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            // user is not authorized to perform the action
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Not authorized");
            alert.setHeaderText("You are not allowed to perform this action.");
            alert.showAndWait();
        }
    }


    @FXML
    @SuppressWarnings("unchecked")
    public void displayCart(ActionEvent event) throws IOException {
        try {
            ArrayList<Equipment> equipments = new ArrayList<>();
            List<String> request = new ArrayList<>();
            request.add("display_cart");
            equipments = ServerEquipments.run(request);

            addToTable(equipments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeFromCart(ActionEvent event) throws UnknownHostException, IOException {

        try {
            // getting selection of the tableview
            Equipment equipment = tableView.getSelectionModel().getSelectedItem();

            if(equipment==null)
                throw new NotSelectedException("Didn't choose an equipment");
            Boolean response= false;
            List<String> request = new ArrayList<>();
            request.add("remove_from_cart");
            request.add(this.currentUser.getEmail());
            response = ServerEquipments.run(request,equipment);

            if (response) {
                initData(this.currentUser);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(String.format("Removed from cart"));
                alert.setHeaderText(String.format("Removed %s from cart.", equipment.getEquipmentId()));
                alert.showAndWait();
            }
        } catch(NotSelectedException e) {
            System.out.println(e);
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(String.format("Select an Equipment"));
            alert.setHeaderText("You have to click on the equipment and then Remove.");
            alert.showAndWait();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}