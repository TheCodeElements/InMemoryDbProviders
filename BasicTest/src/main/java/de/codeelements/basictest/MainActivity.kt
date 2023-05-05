package de.codeelements.basictest

import android.content.ContentUris
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.codeelements.basictest.databinding.ActivityMainBinding
import de.codeelements.utilities.dialogs.showTimeoutMessageBox

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val CONTENT_URI_VALUES = InMemoryDbProviderInterface.getUri(InMemoryDbProviderInterface.TABLE_NAME_VALUES)
    val CONTENT_URL_VALUES = InMemoryDbProviderInterface.getUrl(InMemoryDbProviderInterface.TABLE_NAME_VALUES)

    val CONTENT_URI_DEBUG = InMemoryDbProviderInterface.getUri(InMemoryDbProviderInterface.TABLE_NAME_DEBUG)
    val CONTENT_URL_DEBUG = InMemoryDbProviderInterface.getUrl(InMemoryDbProviderInterface.TABLE_NAME_DEBUG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.showDataFromTableValues.setOnClickListener { this.onClickShowDataFromTableValues(it) }
        binding.addToTableValues.setOnClickListener { this.onClickAddToTableValues(it) }

        binding.btnAddToDebug.setOnClickListener {

            val values = ContentValues()

            values.put("debug_key", "example_key")
            values.put("debug_value", "example_value")

            contentResolver.insert(CONTENT_URI_DEBUG, values)
            Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()

        }

        contentResolver.registerContentObserver(
            CONTENT_URI_VALUES,
            true,
            object : ContentObserver(Handler()) {
                override fun onChange(selfChange: Boolean) { // Added in API level 1
                }

                override fun onChange(selfChange: Boolean, uri: Uri?) { // Added in API level 16
                    if (uri == null) return // todo: log
                    try {
                        test_showChangedValue( ContentUris.parseId(uri))
                    } catch (e: Exception) {
                        // todo: log
                    }
                }
            })




    }



    private fun onClickAddToTableValues(view: View?) {
        val s = binding.edValue.text.toString().trim()

        if(s.isNotEmpty()) {
            val values = ContentValues()
            values.put("var_value", s)
            contentResolver.insert(CONTENT_URI_VALUES, values)
            Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
        }
    }

    private fun test_showChangedValue(rowid : Long) {


        val requestedColumns = arrayOf<String>(
            "id",
            "var_name",
            "var_value"
        )

        val cursor : Cursor? = contentResolver.query(
            Uri.parse(CONTENT_URL_VALUES),
            requestedColumns,
            "rowid=$rowid",
            null, null
        )

        cursor?.let {

            var strBuild = ""
            if (it.moveToFirst()) {
                while (!it.isAfterLast) {

                    val idxId = it.getColumnIndex("id")
                    val idxName = it.getColumnIndex("var_name")
                    val idxValue = it.getColumnIndex("var_value")

                    strBuild +=  "${it.getString(idxId)} - ${it.getString(idxName)}:${it.getString(idxValue)}\n"
                    it.moveToNext()
                }
            } else {
                strBuild = "No Records Found"
            }
            showTimeoutMessageBox(this,"Data received", strBuild)

            it.close()
        }
    }


    private fun onClickShowDataFromTableValues(view: View?) {
        val cursor_all: Cursor? = contentResolver.query(
            Uri.parse(InMemoryDbProviderInterface.getUrl(InMemoryDbProviderInterface.TABLE_NAME_VALUES)  ),
            null,
            null,
            null,
            null
        )

        val requestedColumns = arrayOf<String>(
            "id",
            "var_name",
            "var_value"
        )



        cursor_all?.let {

            var strBuild = ""
            if (it.moveToFirst()) {
                while (!it.isAfterLast) {

                    val idxId = it.getColumnIndex("id")
                    val idxName = it.getColumnIndex("var_name")
                    val idxValue = it.getColumnIndex("var_value")

                    strBuild +=  "${it.getString(idxId)} - ${it.getString(idxName)}:${it.getString(idxValue)}\n"
                    it.moveToNext()
                }
            } else {
                strBuild = "No Records Found"
            }
            showTimeoutMessageBox(this,"Data", strBuild)

            it.close()
        }

    }



}