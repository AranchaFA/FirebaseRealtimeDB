package com.example.firebasepmdm.pojos;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class CardsDeck {

    /////////////////////////
    //     ATTRIBUTES      //
    /////////////////////////
    // region
    private String idCardsDeck;
    private String name;
    private List<Card> cardsList;
    private HashMap<String,Statistics> usersDeckStatistics; // key->idUser, value->statistics of that user for this deck
    private String targetLanguage;
    private String nativeLanguage;
    private int totalCards; // I need it for Firebase project, it's not definitive, in real project I will get it from cardsList.size()

    // endregion

    /////////////////////////
    //       METHODS       //
    /////////////////////////

    //  For make the class a POJO
    // -Empty constructor
    // -Getters+Setters of all attributes included in JSON structure of DB
    // -Object attributes must be POJOS, or we must implement getter+setter methods that return that attribute as a simple date (String, int,...)

    // region Constructors
    // Empty constructor
    public CardsDeck() {
    }

    public CardsDeck(boolean newDeck) {
        // When we create a new deck, we need to create at least one card for its list
        // If the cardList is empty, Firebase don't create a node for this deck inside infoToClone repository node
        if (newDeck){
            this.cardsList=new ArrayList<>();
            cardsList.add(new Card("Wellcome to QuizCard! :)","Hello world!"));
        }
    }
    // endregion

    // region Getters + Setters
    public String getIdCardsDeck() {
        return idCardsDeck;
    }

    public void setIdCardsDeck(String idCardsDeck) {
        this.idCardsDeck = idCardsDeck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCardsList() {
        return cardsList;
    }

    public void setCardsList(List<Card> cardsList) {
        this.cardsList = cardsList;
    }

    public HashMap<String, Statistics> getUsersDeckStatistics() {
        return usersDeckStatistics;
    }

    public void setUsersDeckStatistics(HashMap<String, Statistics> usersDeckStatistics) {
        this.usersDeckStatistics = usersDeckStatistics;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public void setTotalCards(int totalCards) {
        this.totalCards = totalCards;
    }

    // endregion

    // region Add/Remove cards to list (NOT USED ON FIREBASE'S PROJECT)
    public Card addCard(Card card){
        if (cardsList.add(card)) return card;
        return null;
    }

    public String addUser(String idUser){
        if (!usersDeckStatistics.containsKey(idUser)&&usersDeckStatistics.put(idUser,new Statistics())!=null)
            return idUser;
        return null;
    }
    // endregion

    // region Equals + HashCode (NOT USED ON FIREBASE'S PROJECT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardsDeck)) return false;
        CardsDeck cardsDeck = (CardsDeck) o;
        return getIdCardsDeck().equals(cardsDeck.getIdCardsDeck());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCardsDeck());
    }
    // endregion

    // region toMap (CardsDecks and CardsList)
    public Map<String, Object> toMap() {
        HashMap<String, Object> deckMap = new HashMap<>();
        deckMap.put("name", name);
        deckMap.put("targetLanguage", targetLanguage);
        deckMap.put("nativeLanguage", nativeLanguage);
        if (cardsList!=null)
            deckMap.put("totalCards", cardsList.size());
        else deckMap.put("totalCards", totalCards);  // I need it for Firebase project, it's not definitive

        return deckMap;
    }

    public Map<String, Object> toMapCardsList() { // (NOT USED ON FIREBASE'S PROJECT)
        HashMap<String, Object> cardsMap = new HashMap<>();
        for (Card card:cardsList) {
            cardsMap.put(card.getQuestion(), card.getAnswer());
        }
        return cardsMap;
    }
    // endregion

}
