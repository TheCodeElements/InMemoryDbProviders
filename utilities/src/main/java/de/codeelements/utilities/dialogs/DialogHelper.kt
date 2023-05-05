package de.codeelements.utilities.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog

fun showTimeoutMessageBox(context: Context, title: String, msg: String) {

    val handler = Handler(Looper.getMainLooper())

    val builder =
        AlertDialog.Builder(context).setOnDismissListener(DialogInterface.OnDismissListener {
            handler.removeCallbacksAndMessages(null)
        })

    val dialog = builder.setMessage(msg)
        .setTitle(title)
        .setCancelable(true)
        .setPositiveButton(
            "Ok",
            DialogInterface.OnClickListener { dialog, id ->
            })
        .create()

    handler.postDelayed({
        dialog.dismiss()
    }, 2000)

    dialog.show()
}