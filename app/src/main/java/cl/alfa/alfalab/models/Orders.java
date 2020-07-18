package cl.alfa.alfalab.models;

import java.io.Serializable;

public class Orders implements Serializable {

    private String
            _id,
            responsible,
            checkin,
            checkout,
            orderType,
            commentaries;

    private Client client;

    private boolean status;

    private int number,
            price;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isStatus() {
        return status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getType() {
        return orderType;
    }

    public void setType(String type) {
        this.orderType = type;
    }

    public String getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(String commentaries) {
        this.commentaries = commentaries;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
