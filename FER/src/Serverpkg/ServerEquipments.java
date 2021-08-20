package Serverpkg;

import sample.Equipment;

import java.util.ArrayList;
import java.util.List;

public class ServerEquipments {


    public static ArrayList<Equipment> run(List<String> request) {
        EquipmentDB.init();
        ArrayList<Equipment> response = new ArrayList<>();
        try {
            switch (request.get(0)) {

                case "add_equipment":
                    Equipment equipment = EquipmentDB.addEquipment(request.get(1), request.get(2), request.get(3),
                            Integer.parseInt(request.get(4)));
                    response.add(equipment);
                    return response;

                case "search":
                    response = EquipmentDB.search(request.get(1));
                    return response;

                case "get_equipments":
                    response = EquipmentDB.getEquipments();
                    return response;

                case "display_cart":
                    response = CartDB.displayCart(request.get(1));
                    return response;


                default:
                    break;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Boolean run(List<String> request,Equipment equipment) {
        EquipmentDB.init();
         boolean response = false;
        try {
            switch (request.get(0)) {

                case "add_to_cart":
                    response = CartDB.addtoCart(request.get(1),equipment);
                    return response;

                case "remove_from_cart":
                    response = CartDB.removefromCart(request.get(1),equipment);
                    return response;

                case "remove_all_from_cart":
                    response = CartDB.removefromCart(request.get(1));
                    return response;

                case "shipOrder":
                    response = OrderDB.shipOrder(request.get(1));
                    return response;

                default:
                    break;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static ArrayList<Order> orderRun(List<String> request) {
        ArrayList<Order> response = new ArrayList<>();
        try {
            switch (request.get(0)) {

                case "get_order_user":
                    Order order = OrderDB.getOrders(request.get(1));
                    response.add(order);
                    return response;

                case "get_orders":
                    response = OrderDB.getOrders();
                    return response;
                case "get_orders_employee":
                    response = OrderDB.getOrdersEmployees();
                    return response;

                default:
                    break;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Boolean orderRun(List<String> request,ArrayList<Equipment> equipments) {
        return OrderDB.addOrder(request.get(0),equipments);

    }
}