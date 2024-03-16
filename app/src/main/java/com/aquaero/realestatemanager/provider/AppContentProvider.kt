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
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type

class AppContentProvider: ContentProvider() {

    companion object {

        private const val AUTHORITY: String = "${BuildConfig.APPLICATION_ID}.${Path.CONTENT_PROVIDER}"
        private const val PATH_ROOT: String = "${Path.CONTENT}://$AUTHORITY"

        // Property
        private val TABLE_PROPERTY: String = Property::class.simpleName!!.lowercase()
        private val PATH_PROPERTIES: String = TABLE_PROPERTY
        private val PATH_PROPERTIES_FOR_SALE: String = "$TABLE_PROPERTY/${Path.FOR_SALE}"
        private val PATH_SOLD_PROPERTIES: String = "$TABLE_PROPERTY/${Path.SOLD}"
        // Address
        private val TABLE_ADDRESS: String = Address::class.simpleName!!.lowercase()
        private val PATH_ADDRESSES: String = TABLE_ADDRESS
        // Photo
        private val TABLE_PHOTO: String = Photo::class.simpleName!!.lowercase()
        private val PATH_PHOTOS: String = TABLE_PHOTO
        // Agent
        private val TABLE_AGENT: String = Agent::class.simpleName!!.lowercase()
        private val PATH_AGENTS: String = TABLE_AGENT
        // Type
        private val TABLE_TYPE: String = Type::class.simpleName!!.lowercase()
        private val PATH_TYPES: String = TABLE_TYPE
        // Poi
        private val TABLE_POI: String = Poi::class.simpleName!!.lowercase()
        private val PATH_POIS: String = TABLE_POI
        // PropertyPoiJoin
        private val TABLE_PROPERTY_POI_JOIN: String = PropertyPoiJoin::class.simpleName!!.lowercase()
        private val PATH_PROPERTY_POI_JOINS: String = TABLE_PROPERTY_POI_JOIN

        // For tests
        val URI_PROPERTIES: Uri = Uri.parse("$PATH_ROOT/$PATH_PROPERTIES")
        val URI_PROPERTIES_FOR_SALE: Uri = Uri.parse("$PATH_ROOT/$PATH_PROPERTIES_FOR_SALE")
        val URI_SOLD_PROPERTIES: Uri = Uri.parse("$PATH_ROOT/$PATH_SOLD_PROPERTIES")
        val URI_ADDRESSES: Uri = Uri.parse("$PATH_ROOT/$PATH_ADDRESSES")
        val URI_PHOTOS: Uri = Uri.parse("$PATH_ROOT/$PATH_PHOTOS")
        val URI_AGENTS: Uri = Uri.parse("$PATH_ROOT/$PATH_AGENTS")
        val URI_TYPES: Uri = Uri.parse("$PATH_ROOT/$PATH_TYPES")
        val URI_POIS: Uri = Uri.parse("$PATH_ROOT/$PATH_POIS")
        val URI_PROPERTY_POI_JOINS: Uri = Uri.parse("$PATH_ROOT/$PATH_PROPERTY_POI_JOINS")

        // Codes for uriMatcher
        private const val CODE_PROPERTIES = 1
        private const val CODE_PROPERTIES_FOR_SALE = 2
        private const val CODE_SOLD_PROPERTIES = 3
        private const val CODE_ADDRESSES = 4
        private const val CODE_PHOTOS = 5
        private const val CODE_AGENTS = 6
        private const val CODE_TYPES = 7
        private const val CODE_POIS = 8
        private const val CODE_PROPERTY_POI_JOINS = 9

        // uriMatcher
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_PROPERTIES, CODE_PROPERTIES)
            addURI(AUTHORITY, PATH_PROPERTIES_FOR_SALE, CODE_PROPERTIES_FOR_SALE)
            addURI(AUTHORITY, PATH_SOLD_PROPERTIES, CODE_SOLD_PROPERTIES)
            addURI(AUTHORITY, PATH_ADDRESSES, CODE_ADDRESSES)
            addURI(AUTHORITY, PATH_PHOTOS, CODE_PHOTOS)
            addURI(AUTHORITY, PATH_AGENTS, CODE_AGENTS)
            addURI(AUTHORITY, PATH_TYPES, CODE_TYPES)
            addURI(AUTHORITY, PATH_POIS, CODE_POIS)
            addURI(AUTHORITY, PATH_PROPERTY_POI_JOINS, CODE_PROPERTY_POI_JOINS)
        }
    }

    private lateinit var contentResolver: ContentResolver
    private lateinit var database: AppDatabase
    private lateinit var propertyDao: PropertyDao
    private lateinit var addressDao: AddressDao
    private lateinit var photoDao: PhotoDao
    private lateinit var agentDao: AgentDao
    private lateinit var typeDao: TypeDao
    private lateinit var poiDao: PoiDao
    private lateinit var propertyPoiJoinDao: PropertyPoiJoinDao

    override fun onCreate(): Boolean {
        val context = context ?: return false
        contentResolver = context.contentResolver
        database = AppDatabase.getInstance(context = context)
        propertyDao = database.propertyDao
        addressDao = database.addressDao
        photoDao = database.photoDao
        agentDao = database.agentDao
        typeDao = database.typeDao
        poiDao = database.poiDao
        propertyPoiJoinDao = database.propertyPoiJoinDao
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
            CODE_PROPERTIES -> { propertyDao.getPropertiesWithCursor() }
            CODE_PROPERTIES_FOR_SALE -> { propertyDao.getPropertiesForSaleWithCursor() }
            CODE_SOLD_PROPERTIES -> { propertyDao.getSoldPropertiesWithCursor() }
            CODE_ADDRESSES -> { addressDao.getAddressesWithCursor() }
            CODE_PHOTOS -> { photoDao.getPhotosWithCursor() }
            CODE_AGENTS -> { agentDao.getAgentsWithCursor() }
            CODE_TYPES -> { typeDao.getTypesWithCursor() }
            CODE_POIS -> { poiDao.getPoisWithCursor() }
            CODE_PROPERTY_POI_JOINS -> { propertyPoiJoinDao.getPropertyPoiJoinsWithCursor() }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        val prefix = "vnd.android.cursor.item"
        val authority = uri.toString().substringAfter("//").substringBefore("/")
        val table = uri.toString().substringAfter("//")
            .substringAfter("/").substringBefore("/")
        return "$prefix/$authority.$table"
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
