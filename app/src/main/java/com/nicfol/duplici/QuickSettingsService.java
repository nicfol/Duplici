package com.nicfol.duplici;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingsService extends TileService {

    @Override
    public void onTileAdded() {
        Log.d("QS", "Tile added");
    }

    @Override
    public void onStartListening() {
        Log.d("QS", "Start listening");
    }

    @Override
    public void onClick() {
        Log.d("QS", "Tile tapped");

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        Log.d("QS", "Updated primary clip: " + String.valueOf(clipboard.getPrimaryClipDescription().getLabel()));
        Log.d("QS", "Updated primary clip: " + String.valueOf(clipboard.getPrimaryClip().getItemAt(0).getText()));
    }

    @Override
    public void onStopListening() {
        Log.d("QS", "Stop Listening");
    }

    @Override
    public void onTileRemoved() {
        Log.d("QS", "Tile removed");
    }

}
