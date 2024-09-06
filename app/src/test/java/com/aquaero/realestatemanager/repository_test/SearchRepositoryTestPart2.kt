package com.aquaero.realestatemanager.repository_test

import android.util.Log
import com.aquaero.realestatemanager.DEFAULT_LIST_INDEX
import com.aquaero.realestatemanager.DEFAULT_RADIO_INDEX
import com.aquaero.realestatemanager.DropdownMenuCategory
import com.aquaero.realestatemanager.database.dao.AddressDao
import com.aquaero.realestatemanager.database.dao.PhotoDao
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.PropertyPoiJoin
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.repository.AddressRepository
import com.aquaero.realestatemanager.repository.PhotoRepository
import com.aquaero.realestatemanager.repository.SearchRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.reflect.KFunction

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing SearchRepository
 */
class SearchRepositoryTestPart2 {
    private lateinit var repository: SearchRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var photoRepository: PhotoRepository

    private lateinit var addressDao: AddressDao
    private lateinit var photoDao: PhotoDao

    private lateinit var propertyA: Property
    private lateinit var propertyB: Property
    private lateinit var propertyC: Property
    private lateinit var propertyD: Property
    private lateinit var addressA: Address
    private lateinit var addressB: Address
    private lateinit var addressC: Address
    private lateinit var addressD: Address
    private lateinit var typeA: Type
    private lateinit var typeB: Type
    private lateinit var typeC: Type
    private lateinit var typeD: Type
    private lateinit var agentA: Agent
    private lateinit var agentB: Agent
    private lateinit var agentC: Agent
    private lateinit var agentD: Agent
    private lateinit var photoA: Photo
    private lateinit var photoB: Photo
    private lateinit var poiA: Poi
    private lateinit var poiB: Poi
    private lateinit var propertyPoiJoinA: PropertyPoiJoin
    private lateinit var propertyPoiJoinB: PropertyPoiJoin
    private lateinit var propertyPoiJoinC1: PropertyPoiJoin
    private lateinit var propertyPoiJoinC2: PropertyPoiJoin
    private lateinit var properties: MutableList<Property>
    private lateinit var addresses: MutableList<Address>
    private lateinit var types: MutableList<Type>
    private lateinit var agents: MutableList<Agent>
    private lateinit var photos: MutableList<Photo>
    private lateinit var propertyPoiJoins: MutableList<PropertyPoiJoin>
    private lateinit var typeIdA: String
    private lateinit var typeIdB: String
    private lateinit var typeIdC: String
    private lateinit var typeIdD: String
    private lateinit var poiIdA: String
    private lateinit var poiIdB: String
    private lateinit var stringType1: String
    private lateinit var stringAgent1: String

    // Created to avoid class "LOG" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        // Initialize logMock
        logMock = Mockito.mockStatic(Log::class.java)

        repository = SearchRepository()
        addressDao = mock(AddressDao::class.java)
        addressRepository = AddressRepository(addressDao)
        photoDao = mock(PhotoDao::class.java)
        photoRepository = PhotoRepository(photoDao)

        stringType1 = "stringType1"
        stringAgent1 = "stringAgent1"

        typeIdA = "typeIdA"
        typeA = Type(typeIdA)
        typeIdB = "typeIdB"
        typeB = Type(typeIdB)
        typeIdC = "typeIdC"
        typeC = Type(typeIdC)
        typeIdD = "typeIdD"
        typeD = Type(typeIdD)
        types = mutableListOf(typeA, typeB, typeC, typeD)

        agentA = Agent(1, "firstName1", null)
        agentB = Agent(2, "firstName2", "lastName2")
        agentC = Agent(3, "firstName3", "lastName3")
        agentD = Agent(4, "firstName4", "lastName4")
        agents = mutableListOf(agentA, agentB, agentC, agentD)

        photoA = Photo(1, "uri1", "label1", 2)
        photoB = Photo(2, "uri2", "label2", 3)
        photos = mutableListOf(photoA, photoB)

        poiIdA = "poiIdA"
        poiA = Poi(poiIdA)
        poiIdB = "poiIdB"
        poiB = Poi(poiIdB)
        propertyPoiJoinA = PropertyPoiJoin(1, poiIdA)
        propertyPoiJoinB = PropertyPoiJoin(2, poiIdB)
        propertyPoiJoinC1 = PropertyPoiJoin(3, poiIdA)
        propertyPoiJoinC2 = PropertyPoiJoin(3, poiIdB)
        propertyPoiJoins = mutableListOf(
            propertyPoiJoinA, propertyPoiJoinB, propertyPoiJoinC1, propertyPoiJoinC2
        )

        addressA = Address(
            1, null, null, null, null,
            null, null, null, null, null
        )
        addressB = Address(
            2,"streetNumberB", "streetNameB", "addInfoB",
            "cityB", "stateB", "zipB", "countryB", 2.1, 2.2
        )
        addressC = Address(
            3,"streetNumberC", "streetNameC", "addInfoC",
            "cityC", "stateC", "zipC", "countryC", 3.1, 3.2
        )
        addressD = Address(
            4,"streetNumberD", "streetNameD", "addInfoD",
            "cityD", "stateD", "zipD", "countryD", 4.1, 4.2
        )
        addresses = mutableListOf(addressA, addressB, addressC, addressD)

        propertyA = Property(
            1, typeIdA,
            1, null, null, null, null,
            null, null, "2021-06-01", "2021-07-31", 1
        )
        propertyB = Property(
            2, typeIdB,
            2, 20,"descriptionB", 20, 20,
            20, 20, "2022-06-01", null, 2
        )
        propertyC = Property(
            3, typeIdC,
            3, 30,"descriptionC", 30, 30,
            30, 30, "2023-06-01", null, 3
        )
        propertyD = Property(
            4, typeIdD,
            4, 40,"descriptionD", 40, 40,
            40, 40, "2024-06-01", "2024-07-31", 4
        )
        properties = mutableListOf(propertyA, propertyB, propertyC, propertyD)
    }

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }

    /**
     * For test of setDropdownMenuCategory()
     */
    private fun launchTestsForDropdownMenuCategory(
        category: String,
        indexGetter: KFunction<*>,
        valueGetter: KFunction<*>,
        indexBefore: Int = DEFAULT_LIST_INDEX,
        valueBefore: String? = null,
        indexAfter: Int,
        valueAfter: String?
    ) {
        // Check test initial conditions (index = DEFAULT_LIST_INDEX and value is null)
        var index = indexGetter.call(repository)
        var value = valueGetter.call(repository)
        assertEquals(indexBefore, index)
        assertEquals(valueBefore, value)

        // Function under test
        repository.setDropdownMenuCategory(category, indexAfter, valueAfter)

        // Verifications and assertions
        index = indexGetter.call(repository)
        value = valueGetter.call(repository)
        assertEquals(indexAfter, index)
        assertEquals(valueAfter, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun launchTestsForFilter(
        resetFields: Boolean = true,
        fieldNames: Array<String?>? = null,
        fieldValues: Array<Any?>? = null,
        index: Int? = null,
        currency: String = "$",
        expectedResult: MutableList<Property>
    ) {
        // Sets the private repository fields by reflection
        if (resetFields) repository.clearCriteria()
        if (fieldNames != null && fieldValues != null) {
            fieldNames.forEach { fieldName ->
                val fieldValue = fieldValues[fieldNames.indexOf(fieldName)]
                if (fieldName != null) {
                    when (fieldName) {
                        "description" -> repository.setDescription(fieldValue as String?)
                        "priceMin" -> repository.setPriceMin(fieldValue as String?)
                        "priceMax" -> repository.setPriceMax(fieldValue as String?)
                        "surfaceMin" -> repository.setSurfaceMin(fieldValue as String?)
                        "surfaceMax" -> repository.setSurfaceMax(fieldValue as String?)
                        "roomsMin" -> repository.setRoomsMin(fieldValue as String?)
                        "roomsMax" -> repository.setRoomsMax(fieldValue as String?)
                        "bathroomsMin" -> repository.setBathroomsMin(fieldValue as String?)
                        "bathroomsMax" -> repository.setBathroomsMax(fieldValue as String?)
                        "bedroomsMin" -> repository.setBedroomsMin(fieldValue as String?)
                        "bedroomsMax" -> repository.setBedroomsMax(fieldValue as String?)
                        "type" -> {
                            repository.forTestingOnly_setType(fieldValue as String?)
                            repository.forTestingOnly_setTypeIndex(index as Int)
                        }
                        "agent" -> {
                            repository.forTestingOnly_setAgent(fieldValue as String?)
                            repository.forTestingOnly_setAgentIndex(index as Int)
                        }
                        "zip" -> repository.setZip(fieldValue as String?)
                        "city" -> repository.setCity(fieldValue as String?)
                        "state" -> repository.setState(fieldValue as String?)
                        "country" -> repository.setCountry(fieldValue as String?)
                        "registrationDateMin" -> repository.setRegistrationDateMin(fieldValue as String?)
                        "registrationDateMax" -> repository.setRegistrationDateMax(fieldValue as String?)
                        "saleDateMin" -> repository.setSaleDateMin(fieldValue as String?)
                        "saleDateMax" -> repository.setSaleDateMax(fieldValue as String?)
                        "salesRadioIndex" -> repository.setSalesRadioIndex(fieldValue as Int)
                        "photosRadioIndex" -> repository.setPhotosRadioIndex(fieldValue as Int)
                        "itemPois" -> repository.forTestingOnly_setItemPois(fieldValue as MutableList<Poi>)
                    }
                }
            }
        }

        val unfilteredList = properties

        // Function under test
        val result = repository.applyFilters(
            unfilteredList, addresses, types, agents, photos, propertyPoiJoins,
            currency, addressRepository, photoRepository
        )

        // Verifications and assertions
        assertEquals(expectedResult, result)
    }


    @Test
    fun testSetDropdownMenuCategory() {
        launchTestsForDropdownMenuCategory(
            category = DropdownMenuCategory.TYPE.name,
            indexGetter = SearchRepository::forTestingOnly_getTypeIndex,
            valueGetter = SearchRepository::getType,
            indexAfter = 1, valueAfter = stringType1
        )
        launchTestsForDropdownMenuCategory(
            category = DropdownMenuCategory.AGENT.name,
            indexGetter = SearchRepository::forTestingOnly_getAgentIndex,
            valueGetter = SearchRepository::getAgent,
            indexAfter = 1, valueAfter = stringAgent1
        )
    }

    /**
     * Also testing private function filterList()
     */
    @Test
    fun testApplyFilters() {
        // No filter applied
        launchTestsForFilter(expectedResult = properties)

        // Filter on digital fields with bounds
        var repoFields = arrayOf(
            arrayOf("surfaceMin", "surfaceMax"),
            arrayOf("roomsMin", "roomsMax"),
            arrayOf("bathroomsMin", "bathroomsMax"),
            arrayOf("bedroomsMin", "bedroomsMax"),
            arrayOf("priceMin", "priceMax"),
        )
        repoFields.forEach {
            // Filter on field min
            launchTestsForFilter(
                fieldNames = arrayOf(it[0]), fieldValues = arrayOf("25"),
                expectedResult = mutableListOf(propertyC, propertyD)
            )
            // Filter on field max
            launchTestsForFilter(
                fieldNames = arrayOf(it[1]), fieldValues = arrayOf("35"),
                expectedResult = mutableListOf(propertyB, propertyC)
            )
            // Filter on field min and max
            launchTestsForFilter(
                resetFields = false, fieldNames = arrayOf(it[0]), fieldValues = arrayOf("25"),
                expectedResult = mutableListOf(propertyC)
            )
        }

        // Filter on specific date fields "registrationDateMin" and "registrationDateMax" (with bounds)
        repoFields = arrayOf(arrayOf("registrationDateMin", "registrationDateMax"))
        with (repoFields[0]) {
            // Filter on field min
            launchTestsForFilter(
                fieldNames = arrayOf(this[0]), fieldValues = arrayOf("2021-12-31"),
                expectedResult = mutableListOf(propertyB, propertyC, propertyD)
            )
            // Filter on field max
            launchTestsForFilter(
                fieldNames = arrayOf(this[1]), fieldValues = arrayOf("2023-12-31"),
                expectedResult = mutableListOf(propertyA, propertyB, propertyC)
            )
            // Filter on field min and max
            launchTestsForFilter(
                resetFields = false,
                fieldNames = arrayOf(this[0]),
                fieldValues = arrayOf("2021-12-31"),
                expectedResult = mutableListOf(propertyB, propertyC)
            )
        }

        // Filter on specific date fields "saleDateMin" and "saleDateMax" (with bounds)
        repoFields = arrayOf(arrayOf("saleDateMin", "saleDateMax"))
        with (repoFields[0]) {
            // Filter on field min
            launchTestsForFilter(
                fieldNames = arrayOf(this[0]), fieldValues = arrayOf("2021-12-31"),
                expectedResult = mutableListOf(propertyD)
            )
            // Filter on field max
            launchTestsForFilter(
                fieldNames = arrayOf(this[1]), fieldValues = arrayOf("2023-12-31"),
                expectedResult = mutableListOf(propertyA)
            )
            // Filter on field min and max
            launchTestsForFilter(
                resetFields = false, fieldNames = arrayOf(this[0]), fieldValues = arrayOf("2021-12-31"),
                expectedResult = mutableListOf()
            )
        }

        // Filter on generic fields without bound
        repoFields = arrayOf(
            arrayOf("description"), arrayOf("zip"), arrayOf("city"), arrayOf("state"), arrayOf("country"),
        )
        repoFields.forEach {
            // Filter on field
            launchTestsForFilter(
                fieldNames = arrayOf(it[0]), fieldValues = arrayOf(it[0]+"C"),
                expectedResult = mutableListOf(propertyC)
            )
        }

        // Filter on specific fields "type" and "agent" (without bound)
        repoFields = arrayOf(arrayOf("type"), arrayOf("agent"))
        repoFields.forEach {
            // Filter on field
            launchTestsForFilter(
                fieldNames = arrayOf(it[0]), fieldValues = arrayOf(it[0]+"C"), index = 2,
                expectedResult = mutableListOf(propertyC)
            )
        }

        // Filter on specific fields "salesRadioIndex" and "photosRadioIndex" (without bound)
        repoFields = arrayOf(arrayOf("salesRadioIndex"), arrayOf("photosRadioIndex"))
        repoFields.forEach {
            // Filter on field when both criteria are selected
            launchTestsForFilter(
                fieldNames = arrayOf(it[0]), fieldValues = arrayOf(DEFAULT_RADIO_INDEX),
                expectedResult = properties
            )
            // Filter on field when "Sold" or "without photo" are selected
            launchTestsForFilter(
                fieldNames = arrayOf(it[0]), fieldValues = arrayOf(1),
                expectedResult = mutableListOf(propertyA, propertyD)
            )
            // Filter on field when "For sale" or "with photo" are selected
            launchTestsForFilter(
                fieldNames = arrayOf(it[0]), fieldValues = arrayOf(0),
                expectedResult = mutableListOf(propertyB, propertyC)
            )
        }

        // Filter on specific fields "itemPois" (without bound)
        repoFields = arrayOf(arrayOf("itemPois"))
        with (repoFields[0]) {
            // Filter on field when "itemPois" contains 1 POI
            launchTestsForFilter(
                fieldNames = arrayOf(this[0]), fieldValues = arrayOf(mutableListOf(poiA)),
                expectedResult = mutableListOf(propertyA, propertyC)
            )
            // Filter on field when when "itemPois" contains 1 other POI
            launchTestsForFilter(
                fieldNames = arrayOf(this[0]), fieldValues = arrayOf(mutableListOf(poiB)),
                expectedResult = mutableListOf(propertyB, propertyC)
            )
            // Filter on field when "itemPois" contains 2 POIs
            launchTestsForFilter(
                fieldNames = arrayOf(this[0]), fieldValues = arrayOf(mutableListOf(poiA, poiB)),
                expectedResult = mutableListOf(propertyC)
            )
        }
    }

}