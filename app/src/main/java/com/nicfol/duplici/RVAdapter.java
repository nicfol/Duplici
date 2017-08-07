package com.nicfol.duplici;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.List;


class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private Context mContext;
    private List<Paste> pasteList;
    private PasteListSingleton pasteListSingleton = PasteListSingleton.getInstance();

    private boolean isEditModeActive;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View editLayout;

        //Normal Layout views
        private EditText viewLabel, viewText;
        private ImageView viewIcon;
        private ImageButton editButton;

        //Edit Layout Views
        private Button dismissBtnEdit, saveBtnEdit, deleteBtnEdit;

        private ViewHolder(View itemView) {
            super(itemView);
            itemView.isInEditMode();

            //Normal layout
            viewLabel = (EditText) itemView.findViewById(R.id.pasteLabel);
            viewText = (EditText) itemView.findViewById(R.id.pasteText);
            viewIcon = (ImageView) itemView.findViewById(R.id.pasteIcon);
            editButton = (ImageButton) itemView.findViewById(R.id.editButton);

            //Editing Layout
            editLayout = itemView.findViewById(R.id.editMode);
            isEditModeActive = false;

            dismissBtnEdit = (Button) itemView.findViewById(R.id.dismissBtnEdit);
            saveBtnEdit = (Button) itemView.findViewById(R.id.saveBtnEdit);
            deleteBtnEdit = (Button) itemView.findViewById(R.id.deleteBtnEdit);
        }
    }

    protected RVAdapter(Context mContext, List<Paste> pasteList) {
        this.mContext = mContext;
        this.pasteList = pasteListSingleton.getPasteList();
    }

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pastes, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RVAdapter.ViewHolder holder, final int position) {
        //TODO Close keyboard in onClicks

        //holder.setIsRecyclable(false);
        holder.viewLabel.setText(pasteList.get(holder.getAdapterPosition()).getLabel());
        holder.viewText.setText(pasteList.get(holder.getAdapterPosition()).getText());
        holder.viewText.setText(pasteList.get(holder.getAdapterPosition()).getText());

        //Update icon to display the correct one
        holder.viewIcon.setImageDrawable(MaterialDrawableBuilder.with(mContext)
                .setIcon(MaterialDrawableBuilder.IconValue.BORDER_COLOR)
                .setColor(Color.BLACK)
                .build()
        );

        //Disables the editText views
        holder.viewLabel.setEnabled(false);
        holder.viewText.setEnabled(false);

        //Event Listener for editing the layout
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLayoutToEditing(holder);
                holder.viewLabel.setEnabled(true);
                holder.viewText.setEnabled(true);
            }
        });

        //Listener for icon
        holder.viewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RVA","Icon image clicked!");
                //TODO Launch dialog with icons to select from
            }
        });

        //Listener for text changes
        holder.viewLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Disables the button if there's no text or if it's too long
                if(holder.viewLabel.getText().length() == 0 || holder.viewLabel.getText().length() > 18) {
                    holder.saveBtnEdit.setTextColor(Color.rgb(200,200,200));
                } else {
                    holder.saveBtnEdit.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                }
            }
        });

        //Listener for save
        holder.saveBtnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if(holder.viewLabel.getText().length() > 19) {
                //TODO Toast?
                Log.d("RVA","Save Btn clicked when greather than 18");
            } else if(holder.viewLabel.getText().length() == 0) {
                //TODO Toast?
                Log.d("RVA","Save Btn clicked when 0");
            } else {
                //Updates the singleton with the new data
                pasteListSingleton.updatePaste(holder.getAdapterPosition(), pasteList.get(holder.getAdapterPosition()).getDbID(),
                        String.valueOf(holder.viewLabel.getText()), String.valueOf(holder.viewText.getText()), 10); //TODO match icon

                //Set the new text to the editViews
                holder.viewLabel.setText(holder.viewLabel.getText());
                holder.viewText.setText(holder.viewText.getText());

                //Changes layout to non-edit mode and disables the editText
                changeLayoutToEditing(holder);
                holder.viewLabel.setEnabled(false);
                holder.viewText.setEnabled(false);
            }
            }
        });

        //Listener for dismiss button
        holder.dismissBtnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            changeLayoutToEditing(holder);
            holder.viewLabel.setText(pasteList.get(holder.getAdapterPosition()).getLabel());
            holder.viewText.setText(pasteList.get(holder.getAdapterPosition()).getText());


            holder.viewLabel.setEnabled(false);
            holder.viewText.setEnabled(false);
            }
        });

        //Listener for delete
        holder.deleteBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog to check if user wants to delete
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure you want to delete this paste?")
                        .setPositiveButton("Delete Paste", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                pasteListSingleton.deletePaste(pasteList.get(holder.getAdapterPosition()).getDbID());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return pasteList.size();
    }

    //Shows/hides the delete, dismiss and save buttons
    private void changeLayoutToEditing(RVAdapter.ViewHolder holder) {
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

}
