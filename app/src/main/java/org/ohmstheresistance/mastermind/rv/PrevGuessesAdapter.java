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
    private List<String> comboList;

    public PrevGuessesAdapter(List<String> guessedList, List<String> comboList) {
        this.guessedList = guessedList;
        this.comboList = comboList;
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

        String combination = String.valueOf(comboList).substring(1, 5);
        String usersGuess = prevGuessViewHolder.prevGuessTextView.getText().toString();
        String secondAndThirdNumbers = usersGuess.substring(1);
        String secondNumber = String.valueOf(secondAndThirdNumbers.charAt(0));
        String thirdNumber = String.valueOf(secondAndThirdNumbers.charAt(1));

        if(combination.equals(usersGuess)){

            prevGuessViewHolder.prevGuessImageView.setImageResource(R.drawable.correct);

        }

        if (!(combination.startsWith(String.valueOf(usersGuess.charAt(0))) ||
                String.valueOf(combination.charAt(1)).matches(secondNumber)||
                String.valueOf(combination.charAt(2)).matches(thirdNumber) ||
                combination.endsWith((usersGuess.substring(3))))) {

            prevGuessViewHolder.prevGuessImageView.setImageResource(R.drawable.wrong);
        }


    }

    @Override
    public int getItemCount() {

        return guessedList.size();
    }

    public void setData(List<String> guess) {
        guessedList = guess;
        notifyDataSetChanged();
    }

    public void setComboInfo(List<String> combo){
        comboList = combo;
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