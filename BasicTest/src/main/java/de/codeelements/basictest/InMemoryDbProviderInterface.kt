package de.codeelements.basictest

import android.content.ContentValues
import android.content.Context
import android.net.Uri

// this static class defines items which are used from ContentProvider AND Application

object InMemoryDbProviderInterface {

    const val PROVIDER_NAME =
        "de.codeelements.inmemdb.provider" // same as manifest -> provider android:authorities

    const val TABLE_NAME_VALUES = "table_values"
    const val field_name_values_id = "id"
    const val field_name_values_name = "var_name"
    const val field_name_values_type = "var_type"
    const val field_name_values_value = "var_value"

    const val TABLE_NAME_DEBUG  = "table_debug"
    const val field_name_debug_receive_date_time = "receive_date_time"
    const val field_name_debug_key = "debug_key"
    const val field_name_debug_value = "debug_value"

    fun getUrl(tableName:String) : String {
        return "content://${PROVIDER_NAME}/${tableName}"
    }

    fun getUri(tableName:String) : Uri {
        return Uri.parse(getUrl(tableName))
    }

    fun setValue(context: Context, key:String, value:String) {

        val values = ContentValues()
        values.put(field_name_values_name, key)
        values.put(field_name_values_type, 0)
        values.put(field_name_values_value, value)

        try {
            context.contentResolver.insert(
                getUri(
                    TABLE_NAME_VALUES
                ), values
            )
        } catch (e: Exception) {

        }
    }

}