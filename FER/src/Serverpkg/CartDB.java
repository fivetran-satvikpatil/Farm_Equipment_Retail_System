package Serverpkg;

import sample.Equipment;

import java.util.ArrayList;

public class CartDB {
    public static ArrayList<String> Email = new ArrayList<>();
    public static ArrayList<ArrayList<Equipment>> cartEquipments = new ArrayList<>();

    public static Boolean addtoCart(String email, Equipment equipment){
        try{
            for(int i=0;i<Email.size();i++){
                if(Email.get(i).equals(email)){
                    for(int j=0;j<cartEquipments.get(i).size();j++){
                        if(cartEquipments.get(i).get(j).equals(equipment)){
                            return true;
                        }
                    }
                    cartEquipments.get(i).add(equipment);
                    return true;
                }
            }
        }catch(NullPointerException e){
            System.out.println(e);
        }catch(Exception e){
            System.out.println(e);
        }
        Email.add(email);
        ArrayList<Equipment> equipments = new ArrayList<>();
        equipments.add(equipment);
        cartEquipments.add(equipments);
        return true;
    }

    public static Boolean removefromCart(String email, Equipment equipment){
        int i=-1,j=-1,index=-1;
        for(i=0;i<Email.size();i++){
            if(Email.get(i).equals(email)){
                for(j=0;j<cartEquipments.get(i).size();j++){
                        if(cartEquipments.get(i).get(j).getEquipmentId().equals(equipment.getEquipmentId())){
                            break;
                        }
                    }
                cartEquipments.get(i).remove(j);
                break;
                }
            }

        return true;
    }

    public static Boolean removefromCart(String email){
        int i=-1;
        for(i=0;i<Email.size();i++){
            if(Email.get(i).equals(email))
                break;
        }
        if(i!=Email.size()){
            Email.remove(i);
            cartEquipments.remove(i);
            return true;
        }
        return false;
    }


    public static ArrayList<Equipment> displayCart(String mail){
        ArrayList<Equipment> equipments = new ArrayList<>();
        for(int i=0;i<Email.size();i++){
            if(Email.get(i).equals(mail)){
                equipments =  cartEquipments.get(i);
            }
        }
        return equipments;
    }
}
