package com.example.firebasepmdm.storage.interfaces;

import com.example.firebasepmdm.pojos.CardsDeck;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public interface ICardsDecks {

    // region LISTENERS to read data from DB

    // To bring back the number of users/decks/cards
    // (I could use the same Listener for these three options¿?)
    // (I will identify every single case with the String parameter)
    interface OnCounterReadListener{
        void onSuccess(String counterActionType,int counter);
        void onError(DatabaseError databaseError);
    };
    // To read and bring back one selected deck
    interface OnDeckReadListener{
        void onSuccess(CardsDeck cardsDeck);
        void onError(DatabaseError databaseError);
    }
    // To bring back ID's of filtered decks
    interface OnFilteredDecksListListener{
        void onSuccess(List<CardsDeck> cardsDecksList);
        void onError(DatabaseError databaseError);
    };
    // endregion LISTENERS


    // region METHODS for querying the DB

    // Actions on nodes which represents count variables (finally, I haven't used them in this project, but I checked they work correctly)
    void totalDecks(OnCounterReadListener onCounterReadListener);
    void totalCards(OnCounterReadListener onCounterReadListener);
    void totalUsers(OnCounterReadListener onCounterReadListener);

    // Actions on nodes which represents a list (cardsDecksToShow in our case)
    void addDeck(CardsDeck cardsDeck);
    void removeDeck(String idCardsDeck);
    void updateDeckBasicInfo(CardsDeck cardsDeck);
    void readDeck(String idDeck,OnDeckReadListener onDeckReadListener);

    // Ordered and filtered queries
    void read2BiggestDecks(OnFilteredDecksListListener onFilteredDecksListListener);
    void readDecksFilteredByTargetLanguage(String targetLanguage,OnFilteredDecksListListener onFilteredDecksListListener);

    // endregion METHODS
}
