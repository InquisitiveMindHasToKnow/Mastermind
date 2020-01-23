package org.ohmstheresistance.mastermind.rv;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ohmstheresistance.mastermind.R;

import java.util.List;

public class PrevGuessesAdapter extends RecyclerView.Adapter<PrevGuessesAdapter.PrevGuessViewHolder> {

    private List<String> guessedList;

    public PrevGuessesAdapter(List<String> guessedList) {
        this.guessedList = guessedList;
    }

    @NonNull
    @Override
    public PrevGuessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prev_guesses_itemview, viewGroup, false);
        return new PrevGuessViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrevGuessViewHolder prevGuessViewHolder, int position) {

        prevGuessViewHolder.prevGuessTextView.setText(guessedList.get(position));

    }

    @Override
    public int getItemCount() {
        return guessedList.size();
    }

    public void setData(List<String> guess) {
        guessedList = guess;
        notifyDataSetChanged();
    }

    class PrevGuessViewHolder extends RecyclerView.ViewHolder {

        private TextView prevGuessTextView;
        private ImageView prevGuessImageView;

        public PrevGuessViewHolder(@NonNull View itemView) {
            super(itemView);

            prevGuessTextView = itemView.findViewById(R.id.previous_guess_textview);
            prevGuessImageView = itemView.findViewById(R.id.prev_guess_imageview);
        }

    }
}