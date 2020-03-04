package com.example.firebasepmdm.utilities;

import com.example.firebasepmdm.pojos.CardsDeck;
import com.example.firebasepmdm.storage.implementations.CardsDecksFirebase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseUIDBHelper {

    private FirebaseRecyclerOptions<CardsDeck> recyclerOptionsCardsDeck;
    private Query infoToShow;
    private Query biggestCardsDecks2;
    private Query targetLanguageCardsDecks;

    public FirebaseUIDBHelper() {
        infoToShow = FirebaseDatabase.getInstance().getReference()
                .child(CardsDecksFirebase.NODE_INFO_TO_SHOW);
        biggestCardsDecks2 = FirebaseDatabase.getInstance().getReference()
                .child(CardsDecksFirebase.NODE_INFO_TO_SHOW)
                .orderByChild("totalCards")
                .limitToLast(2);
        setRecyclerOptionsCardsDecks(infoToShow); // Default query
    }

    public FirebaseRecyclerOptions<CardsDeck> getRecyclerOptionsCardsDeck() {
        return recyclerOptionsCardsDeck;
    }

    public void setRecyclerOptionsCardsDecks(Query query) {
        recyclerOptionsCardsDeck = new FirebaseRecyclerOptions.Builder<CardsDeck>()
                .setQuery(query, CardsDeck.class)
                .build();
    }

    public Query getBiggestCardsDecks2() {
        return biggestCardsDecks2;
    }

    public Query getTargetLanguageCardsDecks(String targetLanguage) {
        // We need to define this query inside getter instead of in the constructor to pass the parameter
        targetLanguageCardsDecks = FirebaseDatabase.getInstance().getReference()
                .child(CardsDecksFirebase.NODE_INFO_TO_SHOW)
                .orderByChild("targetLanguage")
                .equalTo(targetLanguage);
        // It would be convenient convert to lower/uppercase all string values when we save them in the DB to compare it later
        // And offer limited options to choose languages, so we always save them with a fixed value (for example: ENG, SPA, FRE, POR, GER, ...)
        return targetLanguageCardsDecks;
    }

}
