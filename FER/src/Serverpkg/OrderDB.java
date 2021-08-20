package Serverpkg;

import sample.Equipment;

import java.util.ArrayList;

public class OrderDB {
    private static ArrayList<Order> orders = new ArrayList<>();



    public static Boolean addOrder(String email, ArrayList<Equipment> equipments){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getEmail().equals(email)){
                orders.get(i).addOrder(equipments);
                return true;
            }
        }
        Order order = new Order(email,equipments);
        orders.add(order);
        return true;
    }

    public static Order getOrders(String email){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getEmail().equals(email)){
                return orders.get(i);
            }
        }
        return new Order();
    }

    public static ArrayList<Order> getOrders(){
        return orders;
    }
    public static ArrayList<Order> getOrdersEmployees(){
        ArrayList<Order> ord = new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            if(!orders.get(i).isShipped().get(i)){
                ord.add(orders.get(i));
            }
        }
        return ord;
    }
    public static Boolean shipOrder(String email){
        Boolean response = false;
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getEmail().equals(email)){
                response = orders.get(i).setOrdershipped();
            }
        }
        return response;
    }


}
