package Serverpkg;

import sample.Equipment;
import sample.User;

import java.util.ArrayList;
import java.util.List;

public class EquipmentDB {
    private static List<String> EquipmentName = new ArrayList<>();
    private static List<String> EquipmentId = new ArrayList<>();
    private static List<String> Owner = new ArrayList<>();
    private static List<String> Contact = new ArrayList<>();
    private static List<Integer> Price = new ArrayList<>();
    private static int counter;

    static{
        counter = 1000;
    }


    public static void init(){

    }

    public static Equipment addEquipment(String name,String owner,String contact,int price){

        EquipmentName.add(name);
        EquipmentId.add("E" + counter++);
        Owner.add(owner);
        Contact.add(contact);
        Price.add(price);
        int i = EquipmentName.size()-1;
        return new Equipment(EquipmentName.get(i),EquipmentId.get(i),Owner.get(i),Contact.get(i),Price.get(i));
    }

    public static ArrayList<Equipment> getEquipments(){
        EquipmentDB.addEquipment("Harvester","Sunil","9999999999",35000);
        EquipmentDB.addEquipment("Tractor","Vishal","8888888888",25750);
        EquipmentDB.addEquipment("Reaper","Vijay","7777777777",15650);
        EquipmentDB.addEquipment("Harvester","Sham","6666666666",31000);
        EquipmentDB.addEquipment("Trolley","Hari","5555555555",34220);
        EquipmentDB.addEquipment("Cultivator","Rahul","4444444444",12130);
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        for(int i=0;i<EquipmentName.size();i++){
            equipmentList.add(new Equipment(EquipmentName.get(i),EquipmentId.get(i),Owner.get(i),Contact.get(i),
                    Price.get(i)));
        }
        System.out.println(equipmentList);
        return equipmentList;
    }

    public static ArrayList<Equipment> search(String equipmentType){
        ArrayList<Equipment> equipments = new ArrayList<>();
        for(int i=0;i<EquipmentName.size();i++){
            System.out.println(EquipmentName.get(i));
            if(EquipmentName.get(i).equalsIgnoreCase(equipmentType))
            {
                equipments.add(new Equipment(EquipmentName.get(i),EquipmentId.get(i),Owner.get(i),Contact.get(i),
                        Price.get(i)));
            }
        }
        return equipments;
    }
}
