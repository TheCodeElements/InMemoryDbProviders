package de.codeelements.inmemorydbprovider

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import de.codeelements.inmemorydbprovider.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btnShowValues.setOnClickListener { this.onClickShowValues( it  ) }
        binding.btnAddToValues.setOnClickListener { this.onClickAddToValues(it) }

        binding.btnShowDebug.setOnClickListener { this.onClickShowDebug( it  ) }
        binding.btnAddToDebug.setOnClickListener { this.onClickAddToDebug(it) }

        setContentView(binding.root)
    }


    private fun showMessageBox(msg: String) {

        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this, R.style.AppTheme_AlertDialog)

        builder.setMessage(msg)
            .setTitle("Daten")
            .setCancelable(false)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create().show()

    }

    private fun onClickAddToValues(view: View?) {
        val _name = binding.varName.text.toString().trim()
        val _type = 0
        val _value = binding.varValue.text.toString().trim()

        if(_name.isNotEmpty()) {
            val values = ContentValues()
            values.put(InMemoryDbProviderInterface.field_name_values_name, _name)
            values.put(InMemoryDbProviderInterface.field_name_values_type, _type)
            values.put(InMemoryDbProviderInterface.field_name_values_value, _value)

            try {
                contentResolver.insert(InMemoryDbProviderInterface.getUri(InMemoryDbProviderInterface.TABLE_NAME_VALUES), values)
//                Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
            }
            catch(e:Exception) {

            }
        }
    }

    private fun onClickShowValues(view: View?) {
        // Retrieve employee records
        val cursor: Cursor? = contentResolver.query(
            Uri.parse(InMemoryDbProviderInterface.getUrl(InMemoryDbProviderInterface.TABLE_NAME_VALUES)),
//            Uri.parse("content://" + ProviderName + "/" + TableNameValues.lowercase()  ),
            null,
            null,
            null,
            null
        )
        cursor?.let {

            var strBuild = ""
            if (it.moveToFirst()) {
                while (!it.isAfterLast) {

                    val idxId = it.getColumnIndex(InMemoryDbProviderInterface.field_name_values_id)
                    val idxKey = it.getColumnIndex(InMemoryDbProviderInterface.field_name_values_name)
                    val idxValue = it.getColumnIndex(InMemoryDbProviderInterface.field_name_values_value)

                    strBuild +=  "${it.getString(idxId)}-${it.getString(idxKey)} : ${it.getString(idxValue)}\n"
                    it.moveToNext()
                }
            } else {
                strBuild = "No Records Found"
            }
            it.close()
            showMessageBox(strBuild)
        }

    }



    private fun onClickAddToDebug(view: View?) {
        val _key = binding.debugVarKey.text.toString().trim()
        val _value = binding.debugVarValue.text.toString().trim()

        if(_value.isNotEmpty()) {
            val values = ContentValues()
            values.put(InMemoryDbProviderInterface.field_name_debug_key, _key)
            values.put(InMemoryDbProviderInterface.field_name_debug_value, _value)

            try {
                contentResolver.insert(InMemoryDbProviderInterface.getUri(InMemoryDbProviderInterface.TABLE_NAME_DEBUG), values)
//                Toast.makeText(baseContext, "New Record Inserted", Toast.LENGTH_LONG).show()
            }
            catch(e:Exception) {

            }
        }
    }

    private fun onClickShowDebug(view: View?) {
        // Retrieve employee records
        val cursor: Cursor? = contentResolver.query(
            Uri.parse(InMemoryDbProviderInterface.getUrl(InMemoryDbProviderInterface.TABLE_NAME_DEBUG)),
            null,
            null,
            null,
            null
        )
        cursor?.let {

            var strBuild = ""
            if (it.moveToFirst()) {
                while (!it.isAfterLast) {

                    val idxKey = it.getColumnIndex(InMemoryDbProviderInterface.field_name_debug_key)
                    val idxValue = it.getColumnIndex(InMemoryDbProviderInterface.field_name_debug_value)
                    val idxDateTime = it.getColumnIndex(InMemoryDbProviderInterface.field_name_debug_receive_date_time)

                    strBuild +=  "${it.getString(idxDateTime)}-${it.getString(idxKey)}\n"
                    strBuild +=  "${it.getString(idxValue)}\n"
                    it.moveToNext()
                }
            } else {
                strBuild = "No Records Found"
            }
            it.close()
            showMessageBox(strBuild)
        }

    }

}