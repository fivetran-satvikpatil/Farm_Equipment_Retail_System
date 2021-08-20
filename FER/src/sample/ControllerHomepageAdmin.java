package sample;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Serverpkg.Order;
import Serverpkg.Server;
import Serverpkg.ServerEquipments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ControllerHomepageAdmin implements Controller {
    private User currentUser;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Text name;

    public void initData(User user) {
        this.currentUser = user;
        this.name.setText(this.currentUser.getName());
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void displayEmployees(ActionEvent event) throws IOException {
        if (this.currentUser.getPermission() > 2) {

            List<String> request = new ArrayList<>();
            request.add("get_employees");

            try {
                ArrayList<User> employees = Server.run(request);
                TreeItem<String> rootItem = new TreeItem<String>("Employees");

                for (User employee : employees) {
                    TreeItem<String> rootEmployee = new TreeItem<String>(employee.getEmail());
                    TreeItem<String> name = new TreeItem<String>("Name: " + employee.getName());
                    TreeItem<String> surname = new TreeItem<String>("Surname: " + employee.getSurname());
                    rootEmployee.getChildren().addAll(name, surname);
                    rootItem.getChildren().add(rootEmployee);
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

    @FXML
    public void displayOrders(ActionEvent event) throws IOException {
        if (this.currentUser.getPermission() > 2) {

            try {
                ArrayList<Order> orders = new ArrayList<>();
                List<String> request = new ArrayList<>();
                request.add("get_orders");
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


    @FXML
    public void displayUsers(ActionEvent event) throws UnknownHostException, IOException {
        if (this.currentUser.getPermission() > 2) {

            List<String> request = new ArrayList<>();
            request.add("get_users");
            try {
                ArrayList<User> users = Server.run(request);
                TreeItem<String> rootItem = new TreeItem<String>("Users");

                for (User user : users) {
                    TreeItem<String> rootUser = new TreeItem<String>(user.getEmail());
                    TreeItem<String> name = new TreeItem<String>("Name: " + user.getName());
                    TreeItem<String> surname = new TreeItem<String>("Surname: " + user.getSurname());
                    rootUser.getChildren().addAll(name, surname);
                    rootItem.getChildren().add(rootUser);
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

    @FXML
    public void displayEquipments(ActionEvent event) throws IOException {
        if (this.currentUser.getPermission() > 2) {
            // user is authorized to perform the action
            List<String> request = new ArrayList<>();
            request.add("get_equipments");

            try {
                ArrayList<Equipment> equipments = ServerEquipments.run(request);
                TreeItem<String> rootItem = new TreeItem<String>("Equipments");

                for (Equipment equipment : equipments) {
                    TreeItem<String> equipmentName = new TreeItem<String>(equipment.getEquipmentName());
                    TreeItem<String> equipmentId = new TreeItem<String>(
                            "Equipment ID: " + equipment.getEquipmentId());
                    TreeItem<String> owner = new TreeItem<String>("Owner: " + equipment.getOwner());
                    TreeItem<String> contact = new TreeItem<String>("Contact: " + equipment.getContact());
                    TreeItem<String> price = new TreeItem<String>(
                            "Price: " + Integer.toString(equipment.getPrice()));
                    equipmentName.getChildren().addAll(equipmentId,owner,contact,price);
                    rootItem.getChildren().add(equipmentName);
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

    @FXML
    public void loadEmployee(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("homepage_employee");
    }

    @FXML
    public void loadUser(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("homepage_user");
    }

    @FXML
    public void loadAddEmployee(ActionEvent event) throws IOException {
        Loader loader = new Loader(this.currentUser, this.rootPane);
        loader.load("add_employee");
    }


    @FXML
    public void logout(ActionEvent event) throws IOException {
//        AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
//        rootPane.getChildren().setAll(pane);
        Loader loader = new Loader(new User(),this.rootPane);
        loader.load("login");
    }
}
