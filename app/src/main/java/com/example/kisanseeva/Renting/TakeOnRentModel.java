package com.example.kisanseeva.Renting;

public class TakeOnRentModel {
    private String equipment_name;
    private int imdId;

    public TakeOnRentModel(String equipment_name, int imdId) {
        this.equipment_name = equipment_name;
        this.imdId = imdId;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public int getImdId() {
        return imdId;
    }

    public void setImdId(int imdId) {
        this.imdId = imdId;
    }
}
