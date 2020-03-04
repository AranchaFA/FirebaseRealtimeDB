package com.example.firebasepmdm.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasepmdm.R;
import com.example.firebasepmdm.pojos.CardsDeck;
import com.example.firebasepmdm.storage.implementations.CardsDecksFirebase;
import com.example.firebasepmdm.storage.interfaces.ICardsDecks;
import com.google.firebase.database.DatabaseError;

public class OptionsActivity extends AppCompatActivity implements ICardsDecks.OnDeckReadListener {

    ///////////////////////
    //    ATTRIBUTES     //
    ///////////////////////
    //region
    private static final int BACK_FROM_REPOSITORY =0 ;
    private CardsDecksFirebase firebaseDBHandler;
    private EditText etDeckID, etDeckName, etTargetLanguage, etNativeLanguage;
    private TextView tvSize;
    private LinearLayout layoutInfo;
    private CardsDeck selectedCardsDeck; // It only change to null value when we click newDeck! It could cause problems
    //endregion

    ///////////////////////
    //     METHODS       //
    ///////////////////////

    //region ACTIVITY LIFE-CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        initReferences();
        if (savedInstanceState == null) {
            layoutInfo.setVisibility(View.GONE);
        }
        firebaseDBHandler = new CardsDecksFirebase();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        layoutInfo.setVisibility(savedInstanceState.getInt("infoVisibility"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("infoVisibility", layoutInfo.getVisibility());
        super.onSaveInstanceState(outState);
    }
    //endregion

    //region UI METHODS
    private void initReferences() {
        etDeckID = findViewById(R.id.et_idSearched);
        etDeckName = findViewById(R.id.et_deckName);
        etNativeLanguage = findViewById(R.id.et_nativeLanguage);
        etTargetLanguage = findViewById(R.id.et_targetLanguage);
        tvSize = findViewById(R.id.tv_size);
        layoutInfo = findViewById(R.id.layout_info);
    }

    private void paintLayoutInfo() {
        if (selectedCardsDeck != null) {
            etDeckName.setText(selectedCardsDeck.getName());
            etTargetLanguage.setText(selectedCardsDeck.getTargetLanguage());
            etNativeLanguage.setText(selectedCardsDeck.getNativeLanguage());
            tvSize.setText(String.valueOf(selectedCardsDeck.getTotalCards()));
        } else {
            etDeckName.setText("");
            etTargetLanguage.setText("");
            etNativeLanguage.setText("");
            tvSize.setText("");
        }
        layoutInfo.setVisibility(View.VISIBLE);
    }
    //endregion

    //region ACTION ELEMENTS CLICKED METHODS
    public void newDeckClicked(View view) {
        selectedCardsDeck = null;
        paintLayoutInfo();
    }

    public void openRepositoryClicked(View view) {
        startActivityForResult(new Intent(this, RepositoryActivity.class),BACK_FROM_REPOSITORY);
    }

    public void searchDeckByIDClicked(View view) {
        if (etDeckID.getText().toString() != null && !TextUtils.isEmpty(etDeckID.getText().toString())) {
            firebaseDBHandler.readDeck(etDeckID.getText().toString(), this);
        } else {
            Toast.makeText(this, "Write an ID", Toast.LENGTH_SHORT).show();
        }
    }

    public void aceptDeckInfoClicked(View view) {
        if (!checkFilledFields()) {
            Toast.makeText(this, "PLEASE! Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            if (selectedCardsDeck != null) // Edit a searched deck
                firebaseDBHandler.updateDeckBasicInfo(chargeDeckInfo(selectedCardsDeck.getIdCardsDeck()));
            else firebaseDBHandler.addDeck(chargeDeckInfo(null)); // Add a new deck
        }
    }
    //endregion

    // region OnDeckReadListener IMPLEMENTED METHODS
    @Override
    public void onSuccess(CardsDeck cardsDeck) {
        if (cardsDeck != null) {
            selectedCardsDeck = cardsDeck;
            Log.v("ID: ", selectedCardsDeck.getIdCardsDeck());
            // We must paint info on this method, because if we try to access selectedCardsDeck info
            // immediately after firebase query on searckDeckByID method, this line of code
            // will be executed BEFORE we have received data from Firebase DB, so selectedCardsDeck info
            // will not have been updated!! (Firebase queries are ASYNCHRONOUS)
            paintLayoutInfo();

        } else {
            Toast.makeText(this, "This ID doesn't exist! :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(this, "We can't connect to DB :(", Toast.LENGTH_SHORT).show();
    }
    // endregion

    //region AUXILIAR METHODS
    private CardsDeck chargeDeckInfo(String deckID) { // ID=null if we want to add a new Deck
        CardsDeck cardsDeck;
        if (deckID==null) { // Add new deck
            cardsDeck =new CardsDeck(true);
            cardsDeck.setTotalCards(1); // At the first time, we create every deck with 1 card
        } else { // Update searched deck
            cardsDeck = new CardsDeck(false);
            cardsDeck.setIdCardsDeck(deckID);
            cardsDeck.setTotalCards(Integer.valueOf(tvSize.getText().toString()));
        }
        cardsDeck.setName(etDeckName.getText().toString());
        cardsDeck.setTargetLanguage(etTargetLanguage.getText().toString());
        cardsDeck.setNativeLanguage(etNativeLanguage.getText().toString());

        return cardsDeck;
    }

    public boolean checkFilledFields() {
        if (etDeckName.getText() == null || TextUtils.isEmpty(etDeckName.getText().toString()))
            return false;
        if (etNativeLanguage.getText() == null || TextUtils.isEmpty(etNativeLanguage.getText().toString()))
            return false;
        if (etTargetLanguage.getText() == null || TextUtils.isEmpty(etTargetLanguage.getText().toString()))
            return false;
        return true;
    }
    //endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case BACK_FROM_REPOSITORY: // To hide layoutInfo when this Activity is restored after open repository
                layoutInfo.setVisibility(View.GONE);
                break;
        }
    }
}
