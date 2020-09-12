package cl.alfa.alfalab.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Orders implements Serializable {

    private String delivered_by,
            zone,
            created_at,
            delivered_date;
    private int orders_number;
    private boolean status;
    private ArrayList<Order> order;
    private Client client;

    public boolean getStatus(){
        return status;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDelivered_by() {
        return delivered_by;
    }

    public void setDelivered_by(String delivered_by) {
        this.delivered_by = delivered_by;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getOrders_number() {
        return orders_number;
    }

    public void setOrders_number(int orders_number) {
        this.orders_number = orders_number;
    }

    public ArrayList<Order> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<Order> order) {
        this.order = order;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
