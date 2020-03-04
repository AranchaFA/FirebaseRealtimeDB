package com.example.firebasepmdm.factories;

import com.example.firebasepmdm.adapters.implementations.FirebaseUICardsDecksRecyclerAdapter;
import com.example.firebasepmdm.adapters.interfaces.ICardsDecksRecyclerAdapter;
import com.example.firebasepmdm.utilities.FirebaseUIDBHelper;

public class CardsDecksAdapterFactory {

    public enum AdapterType {FIREBASE_RECYCLER_UI}

    ;

    public static ICardsDecksRecyclerAdapter getCardsDecksRecyclerAdapter(AdapterType adapterType) {
        switch (adapterType) {
            case FIREBASE_RECYCLER_UI:
                return new FirebaseUICardsDecksRecyclerAdapter(new FirebaseUIDBHelper());
            default:
                return new FirebaseUICardsDecksRecyclerAdapter(new FirebaseUIDBHelper());
        }
    }
}
