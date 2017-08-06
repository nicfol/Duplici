package com.nicfol.duplici

import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.service.quicksettings.TileService
import android.util.Log

@TargetApi(Build.VERSION_CODES.N)
class QuickSettingsService : TileService() {

    var iterator = 0

    override fun onTileAdded() {
        super.onTileAdded()
        Log.d("QS", "Tile added")


        var pasteList = PasteListSingleton.getInstance()
        pasteList.getPaste(1).label
    }

    override fun onStartListening() {
        super.onStartListening()
        Log.d("QS", "Start listening")
    }

    override fun onClick() {
        super.onClick()
        Log.d("QS", "Tile tapped")

        var pasteList = PasteListSingleton.getInstance()

        var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        var label = pasteList.getPaste(0).label
        var text = pasteList.getPaste(0).text


        var clip = ClipData.newPlainText(label, text) as ClipData

        clipboard.primaryClip = clip

        if(clipboard.primaryClip != null ) {
            Log.d("QS", clipboard.primaryClip.toString())
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d("QS", "Stop Listening")
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        Log.d("QS", "Tile removed")
    }

}