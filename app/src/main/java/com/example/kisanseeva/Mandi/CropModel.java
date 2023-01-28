package com.example.kisanseeva.Mandi;
import java.util.ArrayList;

class Field {
    private String name;
    private String id;
    private String type;
}

class Bucket{
    String field;
    String index;
    String type;
}
// ----------------------------------------------

public class CropModel {
    private ArrayList<Crop> crops;
    private String created;
    private String updated;
    private String created_date;
    private String active;
    private String index_name;
    private String[] org;
    private String org_type;

    String source;
    private String title;
    private String external_ws_url;
    private String visualizable;
    private ArrayList<Field> field;

    private int external_ws;
    private String catalog_uuid;
    private ArrayList<String> sector;
    private Bucket target_bucket;
    private String desc;
    private ArrayList<Crop> records;

    private String message;
    private String version;
    private String status;
    private String total;
    private String count;
    private String limit;
    private String offset;

    public ArrayList<Crop> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Crop> records) {
        this.records = records;
    }

    public CropModel(ArrayList<Crop> crops, String created, String updated, String created_date, String active, String index_name, String[] org, String org_type, String source, String title, String external_ws_url, String visualizable, ArrayList<Field> field, int external_ws, String catalog_uuid, ArrayList<String> sector, Bucket target_bucket, String desc, ArrayList<Crop> records, String message, String version, String status, String total, String count, String limit, String offset) {
        this.crops = crops;
        this.created = created;
        this.updated = updated;
        this.created_date = created_date;
        this.active = active;
        this.index_name = index_name;
        this.org = org;
        this.org_type = org_type;
        this.source = source;
        this.title = title;
        this.external_ws_url = external_ws_url;
        this.visualizable = visualizable;
        this.field = field;
        this.external_ws = external_ws;
        this.catalog_uuid = catalog_uuid;
        this.sector = sector;
        this.target_bucket = target_bucket;
        this.desc = desc;
        this.records = records;
        this.message = message;
        this.version = version;
        this.status = status;
        this.total = total;
        this.count = count;
        this.limit = limit;
        this.offset = offset;
    }
}
