package Serverpkg;

import sample.Equipment;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Order {
    private String email;
    private ArrayList<String> orderId = new ArrayList<>();
    private ArrayList<Equipment> orderedEquipments = new ArrayList<>();
    private ArrayList<Boolean> shipped = new ArrayList<>();
    private static int counter;
    static{
        counter=100;
    }

    public Order(){

    }

    public Order(String email,ArrayList<Equipment> equipments){
        this.email = email;
        for(int i=0;i<equipments.size();i++){
            this.orderId.add("O-"+counter++);
            this.orderedEquipments.add(equipments.get(i));
            this.shipped.add(false);
        }
    }

    public void addOrder(ArrayList<Equipment> equipments){
        for(int i=0;i<equipments.size();i++){
            this.orderId.add("O-" + counter++);
            this.orderedEquipments.add(equipments.get(i));
            this.shipped.add(false);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public ArrayList<Equipment> getOrderedEquipments() {
        return orderedEquipments;
    }

    public ArrayList<String> getOrderId(){
        return orderId;
    }

    public Boolean setOrdershipped(){
        for(int i=0;i<shipped.size();i++){
            shipped.set(i,true);
            //shipped.remove(i+1);
        }
        return true;
    }
    public ArrayList<Boolean> isShipped(){
        return shipped;
    }

}
