package edu.mills.cs115;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * AZCategoryAdapter implements the adapter for the RecyclerView in  {@link AZCategoryFragment}.
 *
 * @author barango
 */
class AZCategoryAdapter extends RecyclerView.Adapter<AZCategoryAdapter.ViewHolder> {
    private final static String TAG = "AZCategoryAdapter";
    private String[] categories;
    private Listener listener;

    /**
     *
     */
    public static interface Listener {
        public void onClick(int position);
    }

    /**
     * Provides and holds a reference to the CardView used in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        /**
         * Specifies the CardView to store in the RecyclerView.
         *
         * @param cardView the CardView to store
         */
        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    /**
     * Class constructor specifying the list of categories to use in a CardView.
     *
     * @param categories the list of categories
     */
    public AZCategoryAdapter(String[] categories){
        this.categories = categories;
    }

    /**
     * Registers the CardView listener.
     *
     * @param listener the listener to register
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.az_category_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "POSITION IS: " + position);
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(categories[position]);
        cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(position);
                    }
                    Log.d(TAG, "CATEGORY: " + position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }
}
