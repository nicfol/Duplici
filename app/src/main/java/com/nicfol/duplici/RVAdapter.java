package com.nicfol.duplici;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private Context mContext;
    private List<Paste> pasteList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cLabel, cText;
        public ImageView cIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            cLabel = (TextView) itemView.findViewById(R.id.pasteLabel);
            cText = (TextView) itemView.findViewById(R.id.pasteText);
            cIcon = (ImageView) itemView.findViewById(R.id.pasteIcon);
        }
    }

    public RVAdapter(Context mContext, List<Paste> pasteList) {
        this.mContext = mContext;
        this.pasteList = pasteList;
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pastes, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.cLabel.setText(position + " " + pasteList.get(position).getLabel());
        holder.cText.setText(pasteList.get(position).getText());
        holder.cIcon.setImageResource(R.drawable.moneybag); //TODO Assign DB value to imageview
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return pasteList.size();
    }
}
