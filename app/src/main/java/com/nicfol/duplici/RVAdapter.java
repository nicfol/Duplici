package com.nicfol.duplici;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        public EditText cLabel, cText;
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
            cLabel = (EditText) itemView.findViewById(R.id.pasteLabel);
            cText = (EditText) itemView.findViewById(R.id.pasteText);
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
        this.pasteList = pasteListSingleton.getPasteList();
        //-this.pasteList = pasteList;
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pastes, parent, false);
        return new ViewHolder(itemView);
    }

    public RVAdapter.ViewHolder PubRVA;

    @Override
    public void onBindViewHolder(final RVAdapter.ViewHolder holder, final int position) {
        PubRVA = holder;
        holder.setIsRecyclable(false);
        holder.cLabel.setText(pasteList.get(position).getLabel());
        holder.cText.setText(pasteList.get(position).getText() + pasteList.get(position).getDbID() + " " + position);
        Log.d("Database: ", "" + pasteList.get(position).getText() + pasteList.get(position).getDbID() + " " + position);
        holder.cText.setText(pasteList.get(position).getText());
        //holder.cIcon.setImageResource(R.drawable.moneybag); //-TODO Assign DB value to imageview

        holder.cLabel.setEnabled(false);
        holder.cText.setEnabled(false);

        //Event Listener for editing the layout
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLayoutToEditing(holder);
                holder.cLabel.setEnabled(true);
                holder.cText.setEnabled(true);
            }
        });


        holder.cLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(holder.cLabel.getText().length() == 0 || holder.cLabel.getText().length() > 18) {
                    holder.saveBtnEdit.setEnabled(false);
                    holder.saveBtnEdit.setTextColor(Color.rgb(200,200,200));
                } else {
                    holder.saveBtnEdit.setEnabled(true);
                    holder.saveBtnEdit.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                }
            }
        });

        //Listener for save
        holder.saveBtnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(holder.cLabel.getText().length() > 19) {
                    //TODO Toast or stuff?
                } else if(holder.cLabel.getText().length() == 0) {
                    //TODO Toast or stuff?
                } else {
                    pasteListSingleton.updatePaste(pasteList.get(position).getDbID(), String.valueOf(holder.cLabel.getText()), String.valueOf(holder.cText.getText()), 10);

                    holder.cLabel.setText(pasteList.get(position).getLabel());
                    holder.cText.setText(pasteList.get(position).getText());

                    changeLayoutToEditing(holder);
                    holder.cLabel.setEnabled(false);
                    holder.cText.setEnabled(false);
                    Log.d("save Editing: ", pasteList.get(position).getText() + pasteList.get(position).getLabel());
                }
            }
        });

        //Listener for dismiss
        holder.dismissBtnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLayoutToEditing(holder);
                holder.cLabel.setText(pasteList.get(position).getLabel());
                holder.cText.setText(pasteList.get(position).getText());


                holder.cLabel.setEnabled(false);
                holder.cText.setEnabled(false);
            }
        });

        //Listener for delete
        holder.deleteBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pasteListSingleton.deletePaste(pasteList.get(position).getDbID());
                //pasteList.remove(position);
            }
        });
    }

    public void changeLayoutToEditing(RVAdapter.ViewHolder holder) {
        if(!isEditModeActive) {
            holder.editLayout.setVisibility(View.VISIBLE);
            holder.editButton.setEnabled(false);
            isEditModeActive = true;
        } else {
            holder.editLayout.setVisibility(View.GONE);
            holder.editButton.setEnabled(true);
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
