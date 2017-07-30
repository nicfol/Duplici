package com.nicfol.duplici;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private Context mContext;
    private List<Paste> pasteList;

    private boolean isEditModeActive;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View normalLayout;
        public View editLayout;

        //Normal Layout views
        public TextView cLabel, cText;
        public ImageView cIcon;
        public ImageButton editButton;

        //Edit Layout Views
        public Button dismissBtnEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            normalLayout = (View) itemView.findViewById(R.id.normalMode);
            editLayout = (View) itemView.findViewById(R.id.editMode);
            isEditModeActive = false;

            //Normal layout
            cLabel = (TextView) itemView.findViewById(R.id.pasteLabel);
            cText = (TextView) itemView.findViewById(R.id.pasteText);
            cIcon = (ImageView) itemView.findViewById(R.id.pasteIcon);
            editButton = (ImageButton) itemView.findViewById(R.id.editButton);

            //Editing Layout
            dismissBtnEdit = (Button) itemView.findViewById(R.id.dismissBtnEdit);


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
    public void onBindViewHolder(final RVAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        holder.cLabel.setText(pasteList.get(position).getLabel());
        holder.cText.setText(pasteList.get(position).getText());
        //holder.cIcon.setImageResource(R.drawable.moneybag); //TODO Assign DB value to imageview



        //Event Listener for editing the layout
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLayoutToEditing(holder);
            }
        });
        //Event Listener for editing the layout
        holder.dismissBtnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLayoutToEditing(holder);
                Log.d("Dismiss Button: ", "Has been pressed");
            }
        });
    }

    public void changeLayoutToEditing(RVAdapter.ViewHolder holder) {
        if(!isEditModeActive) {
            holder.editLayout.setVisibility(View.VISIBLE);
            isEditModeActive = true;
        } else {
            holder.editLayout.setVisibility(View.GONE);
            isEditModeActive = false;
        }
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
