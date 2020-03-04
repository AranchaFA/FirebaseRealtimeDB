package com.example.firebasepmdm.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.example.firebasepmdm.adapters.implementations.FirebaseUICardsDecksRecyclerAdapter;
import com.example.firebasepmdm.adapters.interfaces.ICardsDecksRecyclerAdapter;
import com.example.firebasepmdm.factories.CardsDecksAdapterFactory;
import com.example.firebasepmdm.pojos.CardsDeck;
import com.example.firebasepmdm.storage.implementations.CardsDecksFirebase;
import com.example.firebasepmdm.storage.interfaces.ICardsDecks;
import com.example.firebasepmdm.utilities.FirebaseUIDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasepmdm.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.List;

public class RepositoryActivity extends AppCompatActivity implements ICardsDecks.OnFilteredDecksListListener {

    ///////////////////////
    //    ATTRIBUTES     //
    ///////////////////////
    //region
    private RecyclerView rvRepository;
    private static ICardsDecksRecyclerAdapter iAdapterDB;
    private int selectedPosition;
    private CardsDecksFirebase firebaseDBHandler;
    private EditText etTargetLanguage;
    //endregion

    ///////////////////////
    //     METHODS       //
    ///////////////////////

    //region ACTIVITY'S LIFE-CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardsDeck cardsDeck = iAdapterDB.getItem(selectedPosition);
                String idCardsDeck = cardsDeck.getIdCardsDeck();
                firebaseDBHandler.removeDeck(idCardsDeck);
            }
        });
        initReferences();
        firebaseDBHandler = new CardsDecksFirebase();
        if (savedInstanceState == null) selectedPosition = -1; // The first time Activity is created->no selected position
        // Adapter (Firebase DB implementation)
        iAdapterDB = CardsDecksAdapterFactory.getCardsDecksRecyclerAdapter(CardsDecksAdapterFactory.AdapterType.FIREBASE_RECYCLER_UI);
        iAdapterDB.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restore white background to previously selected view
                if (selectedPosition != -1)
                    rvRepository.getChildAt(selectedPosition).setBackgroundColor(Color.WHITE);
                // Save new selected position and highlight its view
                selectedPosition = rvRepository.getChildAdapterPosition(view);
                view.setBackgroundColor(getResources().getColor(R.color.myBlueBackground));
                // Save selected CardsDeck
                CardsDeck selectedCardsDeck = iAdapterDB.getItem(rvRepository.getChildAdapterPosition(view));
                Toast.makeText(RepositoryActivity.this, "SORRY, " + selectedCardsDeck.getName() + "isn't available to download now :(", Toast.LENGTH_SHORT).show();
            }
        });
        iAdapterDB.startListening(); // We need "activate" listening functions FirebaeUI Adapters implements to manage list interactions itself
        rvRepository.setAdapter(iAdapterDB.toRecyclerAdapter());
        // Visual design for RecyclerView
        rvRepository.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        iAdapterDB.stopListening(); // We don't need catch events in RecyclerView when we aren't on this Activity
        super.onDestroy();
    }
    //endregion

    //region UI
    public void initReferences() {
        rvRepository = findViewById(R.id.rv_repository);
        etTargetLanguage = findViewById(R.id.et_targetLanguage);
    }
    //endregion

    // region ACTION ELEMENTS CLICKED METHODS
    public void biggestDecks2Clicked(View view) {
        changeDBQuery(new FirebaseUIDBHelper().getBiggestCardsDecks2());
        firebaseDBHandler.read2BiggestDecks(this);
    }

    public void targetLanguageClicked(View view) {
        if (etTargetLanguage.getText() != null && !TextUtils.isEmpty(etTargetLanguage.getText().toString())) {
            changeDBQuery(new FirebaseUIDBHelper().getTargetLanguageCardsDecks(etTargetLanguage.getText().toString()));
            firebaseDBHandler.readDecksFilteredByTargetLanguage(etTargetLanguage.getText().toString(), this);
        } else
            Toast.makeText(this, "Please, enter some language to search", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region AUXILIAR METHODS
    public void changeDBQuery(Query query) {
        FirebaseUIDBHelper firebaseUIDBHelper = new FirebaseUIDBHelper();
        firebaseUIDBHelper.setRecyclerOptionsCardsDecks(query);
        iAdapterDB = new FirebaseUICardsDecksRecyclerAdapter(firebaseUIDBHelper);
        iAdapterDB.startListening();
        RecyclerView.Adapter adapter = iAdapterDB.toRecyclerAdapter();
        rvRepository.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    //endregion

    //region OnFilteredDeckListListener's IMPLEMENTED METHODS
    @Override
    public void onSuccess(List<CardsDeck> cardsDecksList) {
        String cardsDecksNames="";
        for (CardsDeck readCardsDeck:cardsDecksList){
            cardsDecksNames+=readCardsDeck.getName()+", ";
        }
        Toast.makeText(this, cardsDecksNames, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region ON SAVE/ON RESTORE (DON'T WORK CORRECTLY)
    /* // This methods caused Exceptions when we turn the screen :(
    // I should use RecyclerView's methods to highlight selected items if it has them
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("SELECTED_VIEW_POSITION", selectedPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        selectedPosition = savedInstanceState.getInt("SELECTED_VIEW_POSITION");
        if (selectedPosition != -1) {
            View view = rvRepository.getChildAt(selectedPosition);
            view.setBackgroundColor(getResources().getColor(R.color.myBlueBackground));
        }

        super.onRestoreInstanceState(savedInstanceState);
    }
    */
    //endregion
}
