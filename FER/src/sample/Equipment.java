package sample;


import javafx.scene.image.Image;

import java.io.Serializable;

public class Equipment implements Serializable {

    private String equipmentName;
    private String equipmentId;
    private String owner;
    private String contact;
    private int price;


    public Equipment() {
        this.equipmentName="";
        this.equipmentId="";
        this.contact="";
        this.owner="";
        this.price=0;
    }

    public Equipment(String equipmentName,String equipmentId,String owner,String contact,int price) {
        this.equipmentName = equipmentName;
        this.equipmentId = equipmentId;
        this.owner = owner;
        this.contact = contact;
        this.price = price;
    }


    public String getEquipmentName() {
        return equipmentName;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getOwner() {
        return owner;
    }

    public String getContact() {
        return contact;
    }

    public int getPrice() {
        return price;
    }
}