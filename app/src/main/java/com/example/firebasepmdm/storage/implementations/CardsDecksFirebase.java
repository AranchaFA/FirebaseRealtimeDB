package com.example.firebasepmdm.storage.implementations;

import android.util.Log;

import com.example.firebasepmdm.pojos.CardsDeck;
import com.example.firebasepmdm.storage.interfaces.ICardsDecks;
import com.example.firebasepmdm.utilities.FirebaseUIDBHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class CardsDecksFirebase implements ICardsDecks {

    ///////////////////////
    //    ATTRIBUTES     //
    ///////////////////////
    //region
    public static final String NODE_INFO_TO_SHOW ="repository/infoToShow";
    public static final String NODE_INFO_TO_CLONE ="repository/infoToClone";
    public static final String READ_ACTION_TOTAL_DECKS ="totalDecks";
    public static final String READ_ACTION_TOTAL_CARDS ="totalCards";
    public static final String READ_ACTION_TOTAL_USERS ="totalUsers";

    private DatabaseReference nodeRoot;
    private DatabaseReference nodeInfoToShow;
    private DatabaseReference nodeInfoToClone; // Finally, I haven't used it in this project
    private FirebaseDatabase firebaseDatabase;
    //endregion

    ///////////////////////
    //     METHODS       //
    ///////////////////////

    //region Constructor
    public CardsDecksFirebase() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        nodeRoot=firebaseDatabase.getReference();
        nodeInfoToShow =nodeRoot.child(NODE_INFO_TO_SHOW);
        nodeInfoToClone =nodeRoot.child(NODE_INFO_TO_CLONE);
    }
    //endregion

    //region INTERFACE'S IMPLEMENTES METHODS
    @Override
    public void totalDecks(final OnCounterReadListener onCounterReadListener) {
        nodeRoot.child("totalDecks")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    onCounterReadListener.onSuccess(READ_ACTION_TOTAL_DECKS,Integer.valueOf(dataSnapshot.getValue().toString()));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    onCounterReadListener.onError(databaseError);
                }
            });
    }

    @Override
    public void totalCards(final OnCounterReadListener onCounterReadListener) {
        nodeRoot.child("totalCards")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        onCounterReadListener.onSuccess(READ_ACTION_TOTAL_CARDS,Integer.valueOf(dataSnapshot.getValue().toString()));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onCounterReadListener.onError(databaseError);
                    }
                });
    }

    @Override
    public void totalUsers(final OnCounterReadListener onCounterReadListener) {
        nodeRoot.child("totalUsers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        onCounterReadListener.onSuccess(READ_ACTION_TOTAL_USERS,Integer.valueOf(dataSnapshot.getValue().toString()));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onCounterReadListener.onError(databaseError);
                    }
                });
    }

    @Override
    public void addDeck(CardsDeck cardsDeck) {
        // Generate a new random idDeck and its node
        String idDeck = nodeInfoToShow.push().getKey();
        cardsDeck.setIdCardsDeck(idDeck);
        // We are going to use .updateChildren() to insert information in two different nodes at the same time,
        // because it's an ATOMIC ACTION: or both node's updates are success or none is made
        // 1)
        // Create maps with values to be inserted in the new nodes:
        // JSON structure will be "String" : "Object"
        Map<String, Object> deckValuesToShow = cardsDeck.toMap();
        Map<String, Object> deckValuesToClone = cardsDeck.toMapCardsList();
        // 2)
        // Create another map with each value maps and the path of the nodes in which it must be inserted
        // JSON structure will be "String" : {{ ValuesOfFirstMap }, { ValuesOfSecondMap }}
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(NODE_INFO_TO_SHOW + "/" + idDeck, deckValuesToShow);
        childUpdates.put(NODE_INFO_TO_CLONE + "/" + idDeck, deckValuesToClone);
        // 3)
        // Execute .updateChildren(), on the root node for this case
        nodeRoot.updateChildren(childUpdates);
        // In real project, we would need register the same deck in all nodes it should be...
    }

    @Override
    public void removeDeck(String idCardsDeck) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(NODE_INFO_TO_SHOW + "/" + idCardsDeck, null);
        childUpdates.put(NODE_INFO_TO_CLONE + "/" + idCardsDeck, null);
        nodeRoot.updateChildren(childUpdates);
        // In real project, we would need remove the same deck in all nodes it exists...
    }

    @Override // We'll have a different method to add/remove a card, which will also modify infoToClone node
    public void updateDeckBasicInfo(CardsDeck cardsDeck) {
        nodeInfoToShow.child(cardsDeck.getIdCardsDeck()).setValue(cardsDeck.toMap());
    }

    @Override
    public void readDeck(final String idDeck, final OnDeckReadListener onDeckReadListener) {
        nodeInfoToShow.child(idDeck).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.v("READ_DECK_SUCCESS","Deck id:"+dataSnapshot.getKey()+" read.");
                    CardsDeck cardsDeck=dataSnapshot.getValue(CardsDeck.class);
                    if (cardsDeck!=null){
                        cardsDeck.setIdCardsDeck(idDeck);
                    }
                    onDeckReadListener.onSuccess(cardsDeck);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("READ_DECK_ERROR","Reading deck error occurs: "+databaseError.getMessage());
                onDeckReadListener.onError(databaseError);
            }
        });
    }

    @Override
    public void read2BiggestDecks(final OnFilteredDecksListListener onFilteredDecksListListener) {
        final List<CardsDeck> filteredCardsDecks=new ArrayList<>();
        Query query=new FirebaseUIDBHelper().getBiggestCardsDecks2();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot deckDataSnapshot:dataSnapshot.getChildren()){
                    filteredCardsDecks.add(deckDataSnapshot.getValue(CardsDeck.class));
                    onFilteredDecksListListener.onSuccess(filteredCardsDecks);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFilteredDecksListListener.onError(databaseError);
            }
        });
    }

    @Override
    public void readDecksFilteredByTargetLanguage(String targetLanguage, final OnFilteredDecksListListener onFilteredDecksListListener) {
        final List<CardsDeck> filteredCardsDecks=new ArrayList<>();
        Query query=new FirebaseUIDBHelper().getTargetLanguageCardsDecks(targetLanguage);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot deckDataSnapshot:dataSnapshot.getChildren()){
                    filteredCardsDecks.add(deckDataSnapshot.getValue(CardsDeck.class));
                    onFilteredDecksListListener.onSuccess(filteredCardsDecks);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onFilteredDecksListListener.onError(databaseError);
            }
        });
    }
    //endregion
}
