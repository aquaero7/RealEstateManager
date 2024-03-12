package com.aquaero.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.aquaero.realestatemanager.BuildConfig
import com.aquaero.realestatemanager.Path
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type

class AppContentProvider(): ContentProvider() {

    companion object {

        const val AUTHORITY: String = "${BuildConfig.APPLICATION_ID}.${Path.CONTENT_PROVIDER}"

        // Property
        val TABLE_NAME: String? = Property::class.simpleName?.lowercase()
        const val PATH_FOR_SALE: String = Path.FOR_SALE
        const val PATH_SOLD: String = Path.SOLD

        // Tor tests
        val URI_PROPERTY: Uri = Uri.parse("${Path.CONTENT}://$AUTHORITY/$TABLE_NAME")
        val URI_ADDRESS = Uri.parse("${Path.CONTENT}://$AUTHORITY/${Address::class.simpleName?.lowercase()}")
        val URI_PHOTO = Uri.parse("${Path.CONTENT}://$AUTHORITY/${Photo::class.simpleName?.lowercase()}")
        val URI_TYPE = Uri.parse("${Path.CONTENT}://$AUTHORITY/${Type::class.simpleName?.lowercase()}")
        val URI_AGENT = Uri.parse("${Path.CONTENT}://$AUTHORITY/${Agent::class.simpleName?.lowercase()}")
        val URI_POI = Uri.parse("${Path.CONTENT}://$AUTHORITY/${Poi::class.simpleName?.lowercase()}")
        val URI_PROP_POI_JOIN = Uri.parse("${Path.CONTENT}://$AUTHORITY/${PropertyPoiJoin::class.simpleName?.lowercase()}")
        //

        // Codes for uriMatcher
        private const val ALL_PROPERTIES = 1
        private const val PROPERTIES_FOR_SALE = 2
        private const val SOLD_PROPERTIES = 3

        // uriMatcher
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "$TABLE_NAME", ALL_PROPERTIES)
            addURI(AUTHORITY, "$TABLE_NAME/$PATH_FOR_SALE", PROPERTIES_FOR_SALE)
            addURI(AUTHORITY, "$TABLE_NAME/$PATH_SOLD", SOLD_PROPERTIES)
        }

    }

    private lateinit var database: AppDatabase
    private lateinit var propertyDao: PropertyDao
    private lateinit var contentResolver: ContentResolver

    override fun onCreate(): Boolean {
        val context = context ?: return false
        contentResolver = context.contentResolver
        database = AppDatabase.getInstance(context = context)
        propertyDao = database.propertyDao
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val match = uriMatcher.match(uri)
        val cursor: Cursor = when (match) {
            ALL_PROPERTIES -> { propertyDao.getAllPropertiesWithCursor() }
            PROPERTIES_FOR_SALE -> { propertyDao.getPropertiesForSaleWithCursor() }
            SOLD_PROPERTIES -> { propertyDao.getSoldPropertiesWithCursor() }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        //
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        //
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        //
        return 0
    }

}
