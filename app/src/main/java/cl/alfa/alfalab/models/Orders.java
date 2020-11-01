package cl.alfa.alfalab.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Orders implements Serializable {

    private String delivered_by,
            zone,
            created_at,
            delivered_date;
    private Integer orders_number;
    private boolean status;
    private ArrayList<Order> orderList;
    private Client client;

    public Orders(Orders orders){
        this.zone = orders.zone;
        this.created_at = orders.created_at;
        this.orders_number = orders.orders_number;
        this.status = orders.status;
        this.orderList = orders.orderList;
        this.client = orders.client;
    }

    public Orders(){}

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

    public Integer getOrders_number() {
        return orders_number;
    }

    public void setOrders_number(int orders_number) {
        this.orders_number = orders_number;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> order) {
        this.orderList = order;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
