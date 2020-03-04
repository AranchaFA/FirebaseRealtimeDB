package com.example.firebasepmdm.pojos;

import java.util.ArrayList;
import java.util.List;

public class User {

    // ATTRIBUTES
    private String idUser; // Works as user name
    private List<CardsDeck> cardsDeckList;

    // METHODS
    public User(String idUser) {
        this.idUser = idUser;
        this.cardsDeckList=new ArrayList<>();
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public List<CardsDeck> getCardsDeckList() {
        return cardsDeckList;
    }

    public CardsDeck addCardsDeck(CardsDeck cardsDeck){
        if (!cardsDeckList.contains(cardsDeck)&&cardsDeckList.add(cardsDeck))
            return cardsDeck;
        return null;
    }
}
