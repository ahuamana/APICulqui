package com.android.culqi.culqi_android.CardModel;

public class CardModel {

    private String object;
    private String id;
    private String type;
    private String email;
    private String card_number;

    public CardModel(String object, String id, String type, String email, String card_number) {
        this.object = object;
        this.id = id;
        this.type = type;
        this.email = email;
        this.card_number = card_number;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }
}
