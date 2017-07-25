package com.nicfol.duplici;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        updateClip("START", "2");

        final Button button = (Button) findViewById(R.id.updateClip);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText clipLabel = (EditText) findViewById(R.id.label);
                EditText clipText = (EditText) findViewById(R.id.clip);

                if(clipLabel != null && clipText != null) {
                    try {
                        updateClip(clipLabel.getText().toString(), clipText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateClip(String label, String clipText) {
        ClipData clip = ClipData.newPlainText(label, clipText);
        clipboard.setPrimaryClip(clip);
        Log.d("QS", String.valueOf(clipboard.getPrimaryClipDescription().getLabel()));
        Log.d("QS", String.valueOf(clipboard.getPrimaryClip().getItemAt(0).getText()));
    }
}
