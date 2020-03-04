package com.example.firebasepmdm.adapters.interfaces;

import android.view.View;
import android.widget.TextView;

import com.example.firebasepmdm.R;
import com.example.firebasepmdm.pojos.CardsDeck;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface ICardsDecksRecyclerAdapter {

    // Methods to get the key or value of a certain position on the list (firebase use both of them)
    CardsDeck getItem(int position);
    String getKey(int position);
    // Methods to assign an OnItemClickListener to our RecyclerView and
    // start/stop listening with it events on the RecyclerView
    void setOnItemClickListener(View.OnClickListener onClickListener); // Why is OnClik instead of OnItemClick?
    void startListening();
    void stopListening();
    // Method to cast our Adapter implementations to a ordinary RecyclerAdapter
    // (we will use ordinary RecyclerView to show query's results, and it only use ordinary Adapters)
    RecyclerView.Adapter toRecyclerAdapter();


    // region VIEW HOLDER
    // We can implement ViewHolder as a subclass in the interface because it will be the same
    // for all Adapter implementations
    public class CardsDeckToShowViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHolderCardsDeckName;
        private TextView tvHolderDeckSize;
        private TextView tvHolderTargetLanguage;
        private TextView tvHolderNativeLanguage;

        public CardsDeckToShowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHolderCardsDeckName =itemView.findViewById(R.id.tv_holder_deckName);
            tvHolderDeckSize =itemView.findViewById(R.id.tv_holder_deckSize);
            tvHolderNativeLanguage =itemView.findViewById(R.id.tv_holder_nativeLanguage);
            tvHolderTargetLanguage=itemView.findViewById(R.id.tv_holder_targetLanguage);
        }

        public void bindCardsDeck(CardsDeck cardsDeck){
            tvHolderCardsDeckName.setText(cardsDeck.getName());
            tvHolderDeckSize.setText(String.valueOf(cardsDeck.getTotalCards()));
            tvHolderNativeLanguage.setText(cardsDeck.getNativeLanguage());
            tvHolderTargetLanguage.setText(cardsDeck.getTargetLanguage());
        }
    }
    // endregion

}
