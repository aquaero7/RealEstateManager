package com.aquaero.realestatemanager.repository_test

import android.util.Log
import com.aquaero.realestatemanager.RATE_OF_DOLLAR_IN_EURO
import com.aquaero.realestatemanager.model.Poi
import com.aquaero.realestatemanager.model.Property
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.repository.SearchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.math.roundToInt

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing SearchRepository
 */
class SearchRepositoryTestPart1 {
    private lateinit var repository: SearchRepository

    private lateinit var property1: Property
    private lateinit var type1: Type
    private lateinit var types: MutableList<Type>
    private lateinit var stringType1: String
    private lateinit var stringTypes: MutableList<String>
    private lateinit var typeId1: String
    private lateinit var typeId2: String
    private lateinit var poiId1: String
    private lateinit var poiId2: String
    private lateinit var poi1: Poi
    private lateinit var poi2: Poi

    // Created to avoid class "LOG" error when tests are run with coverage
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        // Initialize logMock
        logMock = Mockito.mockStatic(Log::class.java)

        repository = SearchRepository()

        property1 = mock(Property::class.java)

        stringType1 = "stringType1"
        typeId1 = "typeId1"
        type1 = Type(typeId1)
        types = mutableListOf(type1)
        stringTypes = mutableListOf(stringType1)
        typeId2 = "typeId2"
        poiId1 = "poiId1"
        poi1 = Poi(poiId1)
        poiId2 = "poiId2"
        poi2 = Poi(poiId2)
    }

    @After
    fun teardown() {
        // Close logMock
        logMock.close()
    }


    /**
     * Testing updateSearchResults(), clearSearchResults() and updateSearchResultsFlow()
     */
    @Test
    fun testSearchResultsAndFlow() {
        runBlocking {
            /* Testing  updateSearchResults() and updateSearchResultsFlow() */

            // Check test initial conditions (searchResults empty)
            repository.updateSearchResultsFlow()
            var result = repository.searchResultsFlow.first()
            assertEquals(0, result.size)
            assert(result.isEmpty())

            // Function under test
            repository.updateSearchResults(mutableListOf(property1))

            // Verifications and assertions when not updating the flow
            // Not updating the flow
            result = repository.searchResultsFlow.first()

            assertEquals(0, result.size)
            assert(result.isEmpty())

            // Verifications and assertions when updating the flow
            repository.updateSearchResultsFlow()    // Updating the flow
            result = repository.searchResultsFlow.first()

            assertEquals(1, result.size)
            assertEquals(mutableListOf(property1), result)

            /* Testing  clearSearchResults() */

            // Function under test
            repository.clearSearchResults()

            // Verifications and assertions
            repository.updateSearchResultsFlow()
            result = repository.searchResultsFlow.first()

            assertEquals(0, result.size)
            assert(result.isEmpty())
        }
    }

    @Test
    fun testUpdateScrollToResultsFlow() {
        runBlocking {
            // Check test initial conditions (scrollToResults = 0)
            var result = repository.scrollToResultsFlow.first()
            assertEquals(0, result)

            // Function under test when scroll = true (launched twice)
            repository.updateScrollToResultsFlow(true)
            repository.updateScrollToResultsFlow(true)

            // Verifications and assertions
            result = repository.scrollToResultsFlow.first()
            assertEquals(2, result) // Updated twice

            // Function under test when scroll = false
            repository.updateScrollToResultsFlow(false)

            // Verifications and assertions
            result = repository.scrollToResultsFlow.first()
            assertEquals(0, result)
        }
    }

    @Test
    fun testGetItemType() {
        // Function under test when types contains type
        var result = repository.getItemType(typeId1, types, stringTypes)
        // Verifications and assertions
        assertEquals(stringType1, result)

        // Function under test when types doesn't contain type
        result = repository.getItemType(typeId2, types, stringTypes)
        // Verifications and assertions
        assert(result.isEmpty())
    }

    @Test
    fun testUpdateItemPois() {
        // Check test initial conditions (itemPois is empty)
        var result = repository.getItemPois()
        assert(result.isEmpty())

        // Function updateItemPois() under test when adding a poi
        repository.updateItemPois(trueAddFalseRemove = true, poi = poi1)
        repository.updateItemPois(trueAddFalseRemove = true, poi = poi2)
        // Verifications and assertions
        result = repository.getItemPois()
        assertEquals(2, result.size)
        assertEquals(mutableListOf(poi1, poi2), result)

        // Function updateItemPois() under test when removing a poi
        repository.updateItemPois(trueAddFalseRemove = false, poi = poi1)
        // Verifications and assertions
        result = repository.getItemPois()
        assertEquals(1, result.size)
        assertEquals(mutableListOf(poi2), result)
    }

    @Test
    fun testConvertPrice() {
        val inputValue = 10
        val convertedValue = (inputValue / RATE_OF_DOLLAR_IN_EURO).roundToInt()

        // Function under test when currency is dollar
        var result = repository.convertPrice(inputValue, "$")
        // Verifications and assertions
        assertEquals(inputValue, result)

        // Function under test when currency is euro
        result = repository.convertPrice(inputValue, "€")
        // Verifications and assertions
        assertEquals(convertedValue, result)

        // Function under test when value is null currency is dollar
        result = repository.convertPrice(null, "$")
        // Verifications and assertions
        assertEquals(null, result)

        // Function under test when value is null currency is euro
        result = repository.convertPrice(null, "€")
        // Verifications and assertions
        assertEquals(null, result)
    }

}