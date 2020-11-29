package cl.alfa.alfalab.models;

import java.io.Serializable;

public class Order implements Serializable {
    private String _id,
            commentaries,
            checkin,
            checkout,
            order_type;
    private int price,
            forcedLevel;
    private boolean status,
            digitized;
    private User responsible;

    public Order(){}
    public Order(String order_type, String checkin, User responsible, String commentaries, int price, int forcedLevel, boolean digitized, boolean status){
        this.order_type = order_type;
        this.checkin = checkin;
        this.responsible = responsible;
        this.commentaries = commentaries;
        this.price = price;
        this.forcedLevel = forcedLevel;
        this.digitized = digitized;
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getForcedLevel() {
        return forcedLevel;
    }

    public void setForcedLevel(int forcedLevel) {
        this.forcedLevel = forcedLevel;
    }

    public String getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(String commentaries) {
        this.commentaries = commentaries;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
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

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isdigitized() {
        return digitized;
    }

    public void setDigitized(boolean quantity) {
        this.digitized = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
