package sample;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import Serverpkg.Order;
import Serverpkg.ServerEquipments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ControllerHomepageEmployee implements Controller {

    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Text name;


    public void initData(User user) {
        this.currentUser = user;
        name.setText(this.currentUser.getName());

        try {
            displayOrders();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    @FXML
    public void loadAddEquipment(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("add_equipment");
    }


    @FXML
    public void loadShop(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("homepage_user");
    }


    @FXML
    public void shipOrder(ActionEvent event) throws UnknownHostException, IOException {
        if (this.currentUser.getPermission() > 1) {
            // user is not authorized to perform the action
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            // the item is selected
            if (selectedItem != null) {

                while (selectedItem.getParent() != treeView.getRoot()) {
                    selectedItem = selectedItem.getParent();
                }
                // warning message if the item is not selected
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Select an order");
                alert.setHeaderText("Please select an order.");
                alert.showAndWait();
                return;
            }
            Boolean shipped=false;
            try {
                List<String> request = new ArrayList<>();
                request.add("shipOrder");
                request.add(selectedItem.getValue());
                System.out.println(selectedItem.getValue());
                shipped = ServerEquipments.run(request, new Equipment());
                // server's answer is true -> the order has been shipped correctly
                if (shipped) {
                    displayOrders();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Shipping successfull");
                    alert.setHeaderText(
                            String.format("Email %s orders has been shipped", selectedItem.getValue()));
                    alert.showAndWait();
                    // server's answer is false -> the order has not been shipped correctly
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Shipping failed.");
                    alert.setHeaderText("Unable to ship order. Try again later.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            // the page is refreshed and the orders are updated (only not shipped
            // orders can be visualized by the employee)
            initData(this.currentUser);
        } else {

            // user is not authorized to perform the action
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Not authorized");
            alert.setHeaderText("You are not allowed to perform this action.");
            alert.showAndWait();
        }
    }


    @FXML
    public void logout(ActionEvent event) throws IOException {
        Loader loader = new Loader(new User(),rootPane);
        loader.load("login");
    }

    public void displayOrders() throws IOException {
        if (this.currentUser.getPermission() > 1) {
            try {
                ArrayList<Order> orders = new ArrayList<>();
                List<String> request = new ArrayList<>();
                request.add("get_orders_employee");
                orders = ServerEquipments.orderRun(request);
                TreeItem<String> rootItem = new TreeItem<String>("Orders");

                for (Order order : orders) {
                    TreeItem<String> rootOrder = new TreeItem<String>(order.getEmail());

                    ArrayList<String> orderId = order.getOrderId();
                    ArrayList<Equipment> orderEquipments = order.getOrderedEquipments();
                    ArrayList<Boolean> shipped = order.isShipped();

                    for (int i=0;i<orderId.size();i++) {
                        TreeItem<String> rootProduct = new TreeItem<String>(orderId.get(i));
                        TreeItem<String> equipment = new TreeItem<String>(
                                String.format("%s(%s) - %s",orderEquipments.get(i).getEquipmentName(),
                                        orderEquipments.get(i).getEquipmentId(), orderEquipments.get(i).getOwner()));
                        TreeItem<String> contact = new TreeItem<>(String.format("Contact->%s",orderEquipments.get(i).getContact()));
                        TreeItem<String> price = new TreeItem<>(String.format("price->%s",orderEquipments.get(i).getPrice()));
                        TreeItem<String> ship = new TreeItem<String>(String.format("Shipped->%s",shipped.get(i)));
                        rootProduct.getChildren().addAll(equipment,contact,price,ship);
                        rootOrder.getChildren().add(rootProduct);
                    }
                    rootItem.getChildren().add(rootOrder);
                }
                treeView.setRoot(rootItem);
                treeView.setShowRoot(false);
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
}