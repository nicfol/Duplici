package com.nicfol.duplici

import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log

@TargetApi(Build.VERSION_CODES.N)
class QuickSettingsService : TileService() {

    var pasteListSingleton = PasteListSingleton.getInstance()
    var iterator = 0

    override fun onStartListening() {
        super.onStartListening()
        pasteListSingleton.init(this)
        val tile = qsTile
        if(pasteListSingleton.getPasteList().size == 0) {
            tile.state = Tile.STATE_UNAVAILABLE
            tile.updateTile()
        } else {
            tile.state = Tile.STATE_ACTIVE
            tile.updateTile()
        }
    }

    override fun onStopListening() {
        super.onStopListening()

        val tile = qsTile
        tile.label = getString(R.string.app_name)
        //tile.icon = //TODO
    }

    override fun onTileAdded() {
        super.onTileAdded()
    }

    override fun onClick() {
        super.onClick()

        var label = pasteListSingleton.getPaste(iterator)?.label
        var text = pasteListSingleton.getPaste(iterator)?.text

        var tile = qsTile
        tile.label = pasteListSingleton.getPaste(iterator)?.label
        tile.contentDescription = pasteListSingleton.getPaste(iterator)?.text
        tile.updateTile()

        var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ClipData.newPlainText(label, text) as ClipData
        clipboard.primaryClip = clip

        var noOfPastes = pasteListSingleton.pasteList.size-1 //subtract 1 to start at 0 instead of 1

        Log.d("QS", iterator.toString())
        Log.d("QS", noOfPastes.toString())
        Log.d("QS", label + " " + text)
        Log.d("QS", clipboard.primaryClip?.toString())

        if(iterator < noOfPastes) {
            iterator += 1
        } else {
            iterator = 0
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
    }

}
