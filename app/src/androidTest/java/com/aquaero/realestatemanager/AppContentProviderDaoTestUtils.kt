package com.aquaero.realestatemanager

import android.content.Context
import android.database.Cursor
import androidx.room.Room
import com.aquaero.realestatemanager.database.AppDatabase
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.AgentDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.database.dao.PoiDao
import com.aquaero.realestatemanager.database.dao.PropertyDao
import com.aquaero.realestatemanager.database.dao.PropertyPoiJoinDao
import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.AGENT_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.POI_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.PoiEnum
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.TYPE_PREPOPULATION_DATA
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object AppContentProviderDaoTestUtils {

    lateinit var typeDao: TypeDao
    lateinit var agentDao: AgentDao
    lateinit var propertyDao: PropertyDao
    lateinit var addressDao: AddressDao
    lateinit var photoDao: PhotoDao
    lateinit var poiDao: PoiDao
    lateinit var propertyPoiJoinDao: PropertyPoiJoinDao

    lateinit var queries:List<Cursor>


    fun cursorFields(dao: Any, cursor: Cursor): Pair<Any?, String?> {
        var first: Any? = null
        var second: String? = null
        when (dao) {
            propertyDao -> {
                first = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                second = cursor.getString(cursor.getColumnIndex(PropertyKey.SALE_DATE))
            }
            addressDao -> { first = cursor.getLong(cursor.getColumnIndex(AddressKey.ADDRESS_ID)) }
            photoDao -> { first = cursor.getLong(cursor.getColumnIndex(PhotoKey.PHOTO_ID)) }
            agentDao -> { first = cursor.getLong(cursor.getColumnIndex(AgentKey.AGENT_ID)) }
            typeDao -> { first = cursor.getString(cursor.getColumnIndex(TypeKey.TYPE_ID)) }
            poiDao -> { first = cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID)) }
            propertyPoiJoinDao -> {
                first = cursor.getLong(cursor.getColumnIndex(PropertyKey.PROPERTY_ID))
                second = cursor.getString(cursor.getColumnIndex(PoiKey.POI_ID))
            }
        }

        return Pair(first = first, second = second)
    }

    fun initTest(dao: Any, query: () -> Cursor): Pair<Cursor, Triple<String?, String?, String?>> {

        // Initialize properties
        val (typePr1, agentPr1, property1) = initProperties()
        val (typePr2, agentPr2, property2) = initProperties(propertyId = 2L)
        val (typePr3, agentPr3, property3) = initProperties(propertyId = 3L)
        val saleDate1 = property1.saleDate
        val saleDate2 = property2.saleDate
        val saleDate3 = property3.saleDate
        // Initialize addresses
        val address1 = initAddresses()
        val address2 = initAddresses(addressId = 2L)
        val address3 = initAddresses(addressId = 3L)
        // Initialize photos
        val photo1 = initPhotos().second
        val photo2 = initPhotos(photoId = 2L).second
        val (propertiesPh, photo3) = initPhotos(photoId = 3L)
        // Initialize propertyPoiJoins
        val ppj1 = initPropertyPoiJoin().second
        val ppj2 = initPropertyPoiJoin(ppj = 2).second
        val ppj3 = initPropertyPoiJoin(ppj = 3).second
        val (propertiesPPJ, ppj4) = initPropertyPoiJoin(ppj = 4)

        val cursor: Cursor
        var strings: Triple<String?, String?, String?> = Triple(first = null, second = null, third = null)

        runBlocking {
            // Initialize cursor and make asynchronous DAO query
            when (dao) {
                propertyDao -> {
                    // Populate database with properties
                    populateDatabaseWithProperty(type = typePr1, agent = agentPr1, property = property1)
                    populateDatabaseWithProperty(type = typePr2, agent = agentPr2, property = property2)
                    populateDatabaseWithProperty(type = typePr3, agent = agentPr3, property = property3)
                    strings = Triple(first = saleDate1, second = saleDate2, third = saleDate3)
                }
                addressDao -> {
                    // Populate database with addresses
                    populateDatabaseWithAddress(address = address1)
                    populateDatabaseWithAddress(address = address2)
                    populateDatabaseWithAddress(address = address3)
                }
                photoDao -> {
                    // Populate database with photos
                    populateDatabaseWithPhoto(properties = propertiesPh, photo = photo1)
                    populateDatabaseWithPhoto(properties = propertiesPh, photo = photo2)
                    populateDatabaseWithPhoto(properties = propertiesPh, photo = photo3)
                }
                agentDao -> {
                    // Populate database with agents
                    populateDatabaseWithAgents()
                }
                typeDao -> {
                    // Populate database with types
                    populateDatabaseWithTypes()
                }
                poiDao -> {
                    // Populate database with POIs
                    populateDatabaseWithPois()
                }
                propertyPoiJoinDao -> {
                    // Populate database with propertyPoiJoins
                    populateDatabaseWithPropertyPoiJoin(properties = propertiesPPJ, propertyPoiJoin = ppj1)
                    populateDatabaseWithPropertyPoiJoin(properties = propertiesPPJ, propertyPoiJoin = ppj2)
                    populateDatabaseWithPropertyPoiJoin(properties = propertiesPPJ, propertyPoiJoin = ppj3)
                    populateDatabaseWithPropertyPoiJoin(properties = propertiesPPJ, propertyPoiJoin = ppj4)
                }
            }
            cursor = withContext(context = Dispatchers.IO) {
                query()
            }
        }

        return Pair(first = cursor, second = strings)
    }

    fun initDatabase(context: Context) {
        val testDatabase = Room
            .inMemoryDatabaseBuilder(context = context, klass =  AppDatabase::class.java)
//            .allowMainThreadQueries()
            .build()
        typeDao = testDatabase.typeDao
        agentDao = testDatabase.agentDao
        propertyDao = testDatabase.propertyDao
        addressDao = testDatabase.addressDao
        photoDao = testDatabase.photoDao
        poiDao = testDatabase.poiDao
        propertyPoiJoinDao = testDatabase.propertyPoiJoinDao

        queries = listOf(
            propertyDao.getPropertiesWithCursor(), addressDao.getAddressesWithCursor(),
            photoDao.getPhotosWithCursor(), agentDao.getAgentsWithCursor(), typeDao.getTypesWithCursor(),
            poiDao.getPoisWithCursor(), propertyPoiJoinDao.getPropertyPoiJoinsWithCursor()
        )

        runBlocking {
            withContext(Dispatchers.IO) {
                testDatabase.clearAllTables()
            }
        }
    }

    private fun initProperties(propertyId: Long = 1L): Triple<Type, Agent, Property> {
        val saleDate1 = null
        val saleDate2 = "2024-03-01"
        val saleDate3 = null
        val type1 = Type(typeId = TypeEnum.FLAT.key)
        val type2 = Type(typeId = TypeEnum.DUPLEX.key)
        val type3 = Type(typeId = TypeEnum.HOUSE.key)
        val agent1 = Agent(agentId = 1L, firstName = "firstName1", lastName = "lastName1")
        val agent2 = Agent(agentId = 2L, firstName = "firstName2", lastName = "lastName2")
        val agent3 = Agent(agentId = 3L, firstName = "firstName3", lastName = "lastName3")

        val property1 =
            Property(
                propertyId = 1L, typeId = type1.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate1, agentId = agent1.agentId
            )
        val property2 =
            Property(
                propertyId = 2L, typeId = type2.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate2, agentId = agent2.agentId
            )
        val property3 =
            Property(
                propertyId = 3L, typeId = type3.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate3, agentId = agent3.agentId
            )

        return when (propertyId) {
            1L -> Triple(first = type1, second = agent1, third = property1)
            2L -> Triple(first = type2, second = agent2, third = property2)
            3L -> Triple(first = type3, second = agent3, third = property3)
            else -> Triple(first = type1, second = agent1, third = property1)
        }
    }

    private fun initAddresses(addressId: Long = 1L): Address {
        val address1 = Address(1L, null, null, null,
            null, null, null, null, null, null)

        val address2 = Address(2L, null, null, null,
            null, null, null, null, null, null)

        val address3 = Address(3L, null, null, null,
            null, null, null, null, null, null)

        return when (addressId) {
            1L -> address1
            2L -> address2
            3L -> address3
            else -> address1
        }
    }

    private fun initPhotos(photoId: Long = 1L): Pair<List<Property>, Photo> {
        val type = Type(typeId = TypeEnum.HOUSE.key)
        val agent = Agent(agentId = 1L, firstName = "firstName1", lastName = "lastName1")
        val property1 =
            Property(
                propertyId = 1L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )
        val property2 =
            Property(
                propertyId = 2L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )

        val photo1 = Photo(1L, "", "photo1", 1L)
        val photo2 = Photo(2L, "", "photo2", 2L)
        val photo3 = Photo(3L, "", "photo3", 1L)

        return when (photoId) {
            1L -> Pair(listOf(property1, property2), photo1)
            2L -> Pair(listOf(property1, property2), photo2)
            3L -> Pair(listOf(property1, property2), photo3)
            else -> Pair(listOf(property1, property2), photo1)
        }
    }

    private fun initPropertyPoiJoin(ppj: Int = 1): Pair<List<Property>, PropertyPoiJoin> {
        val type = Type(typeId = TypeEnum.HOUSE.key)
        val agent = Agent(agentId = 1L, firstName = "firstName1", lastName = "lastName1")
        val property1 =
            Property(
                propertyId = 1L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )
        val property2 =
            Property(
                propertyId = 2L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )
        val property3 =
            Property(
                propertyId = 3L, typeId = type.typeId, addressId = null, price = null,
                description = null, surface = null, nbOfRooms = null, nbOfBathrooms = null,
                nbOfBedrooms = null, registrationDate = null, saleDate = null, agentId = agent.agentId
            )

        val ppj1 = PropertyPoiJoin(propertyId = 1L, poiId = PoiEnum.HOSPITAL.key)
        val ppj2 = PropertyPoiJoin(propertyId = 1L, poiId = PoiEnum.RESTAURANT.key)
        val ppj3 = PropertyPoiJoin(propertyId = 2L, poiId = PoiEnum.HOSPITAL.key)
        val ppj4 = PropertyPoiJoin(propertyId = 3L, poiId = PoiEnum.SCHOOL.key)

        return when (ppj) {
            1 -> Pair(listOf(property1, property2, property3), ppj1)
            2 -> Pair(listOf(property1, property2, property3), ppj2)
            3 -> Pair(listOf(property1, property2, property3), ppj3)
            4 -> Pair(listOf(property1, property2, property3), ppj4)
            else -> Pair(listOf(property1, property2, property3), ppj1)
        }
    }

    private suspend fun populateDatabaseWithProperty(type: Type, agent: Agent, property: Property) {
        withContext(Dispatchers.IO) {
            typeDao.upsertType(type = type)
            agentDao.upsertAgent(agent = agent)
            propertyDao.upsertProperty(property = property)
        }
    }

    private suspend fun populateDatabaseWithAddress(address: Address) {
        withContext(Dispatchers.IO) {
            addressDao.upsertAddress(address = address)
        }
    }

    private suspend fun populateDatabaseWithPhoto(properties: List<Property>, photo: Photo) {
        withContext(Dispatchers.IO) {
            typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
            agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
            properties.forEach { propertyDao.upsertProperty(it) }
            photoDao.upsertPhoto(photo = photo)
        }
    }

    private suspend fun populateDatabaseWithAgents() {
        withContext(Dispatchers.IO) {
            agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
        }
    }

    private suspend fun populateDatabaseWithTypes() {
        withContext(Dispatchers.IO) {
            typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
        }
    }

    private suspend fun populateDatabaseWithPois() {
        withContext(Dispatchers.IO) {
            poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)
        }
    }

    private suspend fun populateDatabaseWithPropertyPoiJoin(
        properties: List<Property>, propertyPoiJoin: PropertyPoiJoin
    ) {
        withContext(Dispatchers.IO) {
            typeDao.prepopulateWithTypes(types = TYPE_PREPOPULATION_DATA)
            agentDao.prepopulateWithAgents(agents = AGENT_PREPOPULATION_DATA)
            poiDao.prepopulateWithPois(pois = POI_PREPOPULATION_DATA)
            properties.forEach { propertyDao.upsertProperty(it) }
            propertyPoiJoinDao.upsertPropertyPoiJoin(propertyPoiJoin = propertyPoiJoin)
        }
    }

}