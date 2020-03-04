package com.example.firebasepmdm.adapters.implementations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasepmdm.R;
import com.example.firebasepmdm.adapters.interfaces.ICardsDecksRecyclerAdapter;
import com.example.firebasepmdm.pojos.CardsDeck;
import com.example.firebasepmdm.utilities.FirebaseUIDBHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseUICardsDecksRecyclerAdapter extends FirebaseRecyclerAdapter<CardsDeck,ICardsDecksRecyclerAdapter.CardsDeckToShowViewHolder> implements ICardsDecksRecyclerAdapter {

    ///////////////////////
    //    ATTRIBUTES     //
    ///////////////////////
    private View.OnClickListener onClickListener;

    ///////////////////////
    //     METHODS       //
    ///////////////////////
    public FirebaseUICardsDecksRecyclerAdapter(FirebaseUIDBHelper firebaseUIDBHelper) {
        super(firebaseUIDBHelper.getRecyclerOptionsCardsDeck());
    }

    @NonNull
    @Override
    public ICardsDecksRecyclerAdapter.CardsDeckToShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardsdeck_viewholder, parent, false);
        return new ICardsDecksRecyclerAdapter.CardsDeckToShowViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CardsDeckToShowViewHolder holder, int position, @NonNull CardsDeck cardsDeck) {
        // Bind data object to ViewHolder and assign it a listener to make it clickable
        cardsDeck.setIdCardsDeck(getKey(position)); // We need save ID to make modifications to the node when we select any item in the RecyclerView
        holder.bindCardsDeck(cardsDeck);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public String getKey(int position) {
        // Get snapshots list, get snapshot in parameter's position, get its key
        return super.getSnapshots().getSnapshot(position).getKey();
    }

    @Override
    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener=onClickListener;
    }

    @Override
    public RecyclerView.Adapter toRecyclerAdapter() {
        return (RecyclerView.Adapter) this;
    }


}
