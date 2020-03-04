package com.example.firebasepmdm.pojos;


import java.util.HashMap;
import java.util.List;

public class Card {

    /////////////////////////
    //     ATTRIBUTES      //
    /////////////////////////
    // region
    private String idCard;
    private String question;
    private String answer;
    private HashMap<String,Statistics> usersStatistics; // Statistics of all users who 'use' this card
    // To use Text-to-speech function I probably will need:
    //private String originalLanguage;
    //private String translationLanguage;
    // endregion

    /////////////////////////
    //       METHODS       //
    /////////////////////////

    // Required to be a POJO :
    // -Empty constructor
    // -Getters+Setters of all attributes included in JSON structure of DB
    // -Object attributes must be POJOS, or we must implement getter+setter methods that return that attribute as a simple date (String, int,...)

    // region Constructors
    public Card() {
    }

    public Card(String idCard, String question, String answer, List<String> idUsers) {
        this.idCard = idCard;
        this.question = question;
        this.answer = answer;
        this.usersStatistics=new HashMap<>();
        for (String idUser:idUsers) {
            usersStatistics.put(idUser,new Statistics());
        }
    }

    // For test Firebase operation, we don't include userName
    public Card(String question, String answer) {
        this.question=question;
        this.answer=answer;
        this.usersStatistics=new HashMap<>();
    }
    // endregion

    // region Getters + Setters
    public String getIdCard() {
        return idCard;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public HashMap<String, Statistics> getUsersStatistics() {
        return usersStatistics;
    }

    public void setUsersStatistics(HashMap<String, Statistics> usersStatistics) {
        this.usersStatistics = usersStatistics;
    }
    // endregion

    // region Add/Remove users to Statistics list
    public String addUser(String idUser){
        if (!usersStatistics.containsKey(idUser)&&usersStatistics.put(idUser,new Statistics())!=null)
            return idUser;
        return null;
    }

    public String removeUser(String idUser){
        if (usersStatistics.containsKey(idUser)&&usersStatistics.remove(idUser)!=null)
            return idUser;
        return null;
    }
    // endregion
}
