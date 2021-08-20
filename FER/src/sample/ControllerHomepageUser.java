package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Serverpkg.EquipmentDB;
import Serverpkg.Order;
import Serverpkg.ServerEquipments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ControllerHomepageUser implements Controller {

    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField searchboxName;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TableView<Equipment> tableView;

    @FXML
    private TableColumn<Equipment, String> nameColumn;

    @FXML
    private TableColumn<Equipment, String> ownerColumn;

    @FXML
    private TableColumn<Equipment, Integer> priceColumn;

    @FXML
    private TableColumn<Equipment, String> contactColumn;


    public void initData(User user) {
        this.currentUser = user;

        try {
            ArrayList<Equipment> equipments = new ArrayList<>();
            List<String> request = new ArrayList<>();
            request.add("get_equipments");
//            equipments = ServerEquipments.run(request,equipments);
            equipments = EquipmentDB.getEquipments();
            // adds the equipments of the shop to the TableView to be displayed
            addToTable(equipments);

            // displays the orders made by the User
            displayOrders();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addToTable(ArrayList<Equipment> equipments) {
        // set up the columns in the table
        nameColumn.setCellValueFactory(new PropertyValueFactory<Equipment, String>("equipmentName"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<Equipment,String>("Owner"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Equipment,String>("Contact"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Equipment,Integer>("Price"));
        System.out.println(equipments);
        ObservableList<Equipment> oListequipments = FXCollections.observableArrayList(equipments);
        // load data
        tableView.setItems(oListequipments);
    }



    @FXML
    public void addToCart(ActionEvent event) throws IOException {
        // permission check, guests can't add to cart
        if (this.currentUser!=null && this.currentUser.getPermission() > 0) {

            try {

                Equipment equipment = tableView.getSelectionModel().getSelectedItem();
                if(equipment == null){
                    throw new NotSelectedException("Didn't choose an equipment");
                }
                List<String> request = new ArrayList<>();
                request.add("add_to_cart");
                request.add(this.currentUser.getEmail());

                Boolean response = ServerEquipments.run(request,equipment);

                if (response){
                    System.out.println(equipment.getEquipmentId() + " " + equipment.getEquipmentName());
                    // operation addToCart was successful
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle(String.format("Added to cart"));
                    alert.setHeaderText(String.format("Added %s to cart.", equipment.getEquipmentName()));
                    alert.showAndWait();
                } else {
                    // operation addToCart was not successful
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle(String.format("Select an Equipment"));
                    alert.setHeaderText("You have to click on a Equipment, enter the quantity and then Add.");
                    alert.showAndWait();
                }

            }catch (NotSelectedException e){
                System.out.println(e);
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(String.format("Select an Equipment"));
                alert.setHeaderText("You have to click on the equipment to add it to the cart.");
                alert.showAndWait();
            }
            catch (Exception e) {
                System.out.println(e);
            }

        } else {
            // the User is a guest, so he needs to login first
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Please login");
            alert.setHeaderText("You need to login to perform this action.");
            alert.showAndWait();
        }
    }


    @FXML
    public void search(ActionEvent event) throws IOException, ClassNotFoundException {

//        // displays the result in the tableview
        ArrayList<Equipment> Equipments = new ArrayList<>();
        ArrayList<String> request = new ArrayList<>();
        request.add("search");
        request.add(searchboxName.getText());
        Equipments = ServerEquipments.run(request);
        addToTable(Equipments);
    }


    @FXML
    public void showCart(ActionEvent event) throws IOException {
        if (this.currentUser!=null && this.currentUser.getPermission() >0) {
            Loader loader = new Loader(this.currentUser, this.rootPane);
            loader.load("cart");
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Please login");
            alert.setHeaderText("You need to login to perform this action.");
            alert.showAndWait();
        }
    }


    public void displayOrders() throws IOException {
        if(this.currentUser!=null && this.currentUser.getPermission() >0) {
            try {
                ArrayList<Order> orders = new ArrayList<>();
                List<String> request = new ArrayList<>();
                request.add("get_order_user");
                request.add(this.currentUser.getEmail());
                orders = ServerEquipments.orderRun(request);
                Order order = orders.get(0);
                TreeItem<String> rootItem = new TreeItem<String>("Orders");

                TreeItem<String> rootOrder = new TreeItem<String>(order.getEmail());

                ArrayList<String> orderId = order.getOrderId();
                ArrayList<Equipment> orderEquipments = order.getOrderedEquipments();
                ArrayList<Boolean> shipped = order.isShipped();

                for (int i = 0; i < orderId.size(); i++) {
                    TreeItem<String> rootProduct = new TreeItem<String>(orderId.get(i));
                    TreeItem<String> equipment = new TreeItem<String>(
                            String.format("%s(%s) - %s", orderEquipments.get(i).getEquipmentName(),
                                    orderEquipments.get(i).getEquipmentId(), orderEquipments.get(i).getOwner()));
                    TreeItem<String> contact = new TreeItem<>(String.format("Contact->%s",orderEquipments.get(i).getContact()));
                    TreeItem<String> price = new TreeItem<>(String.format("price->%s",orderEquipments.get(i).getPrice()));
                    TreeItem<String> ship = new TreeItem<String>(String.format("Shipped-%s", shipped.get(i)));
                    rootProduct.getChildren().addAll(equipment, contact,price,ship);
                    rootOrder.getChildren().add(rootProduct);
                }
                rootItem.getChildren().add(rootOrder);

                treeView.setRoot(rootItem);
                treeView.setShowRoot(false);
            } catch (Exception e) {
                System.out.println(e);;
            }
        }
    }


    @FXML
    public void logout(ActionEvent event) throws IOException {
        try{
            Loader loader = new Loader(new User(),rootPane);
            loader.load("login");
        }catch (NullPointerException e){
            System.out.println(e);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}