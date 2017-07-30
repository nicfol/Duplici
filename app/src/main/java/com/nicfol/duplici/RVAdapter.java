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
    private PasteListSingleton pasteListSingleton = PasteListSingleton.getInstance();

    private boolean isEditModeActive;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View normalLayout;
        public View editLayout;

        //Normal Layout views
        public TextView cLabel, cText;
        public ImageView cIcon;
        public ImageButton editButton;

        //Edit Layout Views
        public Button dismissBtnEdit, saveBtnEdit, deleteBtnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.isInEditMode();

            normalLayout = itemView.findViewById(R.id.normalMode);
            editLayout = itemView.findViewById(R.id.editMode);
            isEditModeActive = false;

            //Normal layout
            cLabel = (TextView) itemView.findViewById(R.id.pasteLabel);
            cText = (TextView) itemView.findViewById(R.id.pasteText);
            cIcon = (ImageView) itemView.findViewById(R.id.pasteIcon);
            editButton = (ImageButton) itemView.findViewById(R.id.editButton);

            //Editing Layout
            dismissBtnEdit = (Button) itemView.findViewById(R.id.dismissBtnEdit);
            saveBtnEdit = (Button) itemView.findViewById(R.id.saveBtnEdit);
            deleteBtnEdit = (Button) itemView.findViewById(R.id.deleteBtnEdit);
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
    public void onBindViewHolder(final RVAdapter.ViewHolder holder, final int position) {
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
        holder.saveBtnEdit.setOnClickListener(new View.OnClickListener() {
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
        holder.deleteBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int del = position;

                //Delete paste from DB by finding it's ID in the list and then update the RV
                Paste pa = (Paste) pasteListSingleton.getPasteList().get(del);
                for(int i = 0; i < pasteListSingleton.getLastInsertId(); i ++) {
                    if(pasteList.get(del) != null && pasteList.get(i).getDbID() == pa.getDbID()) {
                        pasteListSingleton.deletePaste(pa.getDbID());

                        pasteListSingleton.deletePaste(i);
                        break;
                    }
                }
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
