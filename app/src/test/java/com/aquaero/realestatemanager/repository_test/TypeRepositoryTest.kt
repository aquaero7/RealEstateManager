package com.aquaero.realestatemanager.repository_test

import com.aquaero.realestatemanager.database.dao.TypeDao
import com.aquaero.realestatemanager.model.Type
import com.aquaero.realestatemanager.model.TypeEnum
import com.aquaero.realestatemanager.repository.TypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
//@RunWith(RobolectricTestRunner::class)
/**
 * Testing TypeRepository
 */
class TypeRepositoryTest {
    private lateinit var typeDao: TypeDao
    private lateinit var repository: TypeRepository

    private lateinit var typeId1: String
    private lateinit var typeId2: String
    private lateinit var typeId3: String
    private lateinit var type1: Type
    private lateinit var type2: Type
    private lateinit var type1Flow: Flow<Type>
    private lateinit var types: MutableList<Type>
    private lateinit var typesOrderedById: MutableList<Type>
    private lateinit var typesFlow: Flow<MutableList<Type>>
    private lateinit var typesOrderedByIdFlow: Flow<MutableList<Type>>
    private lateinit var stringType1: String
    private lateinit var stringType2: String
    private lateinit var stringTypes: MutableList<String>
    private lateinit var otherStringTypes: MutableList<String>

    @Before
    fun setup() {
        typeDao = mock(TypeDao::class.java)
        repository = TypeRepository(typeDao)

        typeId1 = "typeId1"
        typeId2 = "typeId2"
        typeId3 = "typeId3"
        type1 = mock(Type::class.java)
        type2 = mock(Type::class.java)
        type1Flow = flowOf(type1)
        types = mutableListOf(type2, type1)
        typesOrderedById = mutableListOf(type1, type2)
        typesFlow = flowOf(types)
        typesOrderedByIdFlow = flowOf(typesOrderedById)
        stringType1 = "stringType1"
        stringType2 = "stringType2"
        stringTypes = mutableListOf(stringType2, stringType1)
        otherStringTypes = mutableListOf(stringType1)

        // Setup mocks behaviour
        doReturn(type1Flow).`when`(typeDao).getType(typeId1)
        doReturn(typesFlow).`when`(typeDao).getTypes()
        doReturn(typesOrderedByIdFlow).`when`(typeDao).getTypesOrderedById()
        doReturn(typeId1).`when`(type1).typeId
        doReturn(typeId2).`when`(type2).typeId
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testUpsertTypeInRoom() = runBlocking {
        // Function under test
        repository.upsertTypeInRoom(type1)

        // Verification
        verify(typeDao).upsertType(type1)
    }

    @Test
    fun testDeleteTypeFromRoom() = runBlocking {
        // Function under test
        repository.deleteTypeFromRoom(type1)

        // Verification
        verify(typeDao).deleteType(type1)
    }

    @Test
    fun testGetTypeFromRoom() {
        // Function under test
        val result = repository.getTypeFromRoom(typeId1)

        // Verification and assertion
        verify(typeDao).getType(typeId1)
        assertEquals(type1Flow, result)
    }

    @Test
    fun testGetTypesFromRoom() {
        // Function under test
        val result = repository.getTypesFromRoom()

        // Verification and assertion
        verify(typeDao).getTypes()
        assertEquals(typesFlow, result)
    }

    @Test
    fun testGetTypesOrderedByIdFromRoom() {
        // Function under test
        val result = repository.getTypesOrderedByIdFromRoom()

        // Verification and assertion
        verify(typeDao).getTypesOrderedById()
        assertEquals(typesOrderedByIdFlow, result)
    }

    @Test
    fun testTypeFromId() {
        // Function under test
        val result = repository.typeFromId(typeId2, types)

        // Verification and assertion
        assertEquals(type2, result)
    }

    @Test
    fun testStringType() {
        // 1- Function under test when :
        // - The type ID corresponds to a type from types
        // - types and stringTypes have the same size
        var result = repository.stringType(typeId1, types, stringTypes)
        // Assertion
        assertEquals(stringType1, result)

        // 2- Function under test when :
        // - The type ID doesn't correspond to a type from types
        // - types and stringTypes have the same size
        result = repository.stringType(typeId3, types, stringTypes)
        // Assertion (By default, should return the value at index 0 from stringAgents)
        assertEquals(stringType2, result)

        // 3- Function under test when :
        // - The type ID corresponds to a type from types
        // - types and stringTypes have different sizes
        result = repository.stringType(typeId2, types, otherStringTypes)
        // Assertion
        assertEquals(TypeEnum.UNASSIGNED.key, result)

        // 4- Function under test when :
        // - The type ID doesn't correspond to a type from types
        // - types and stringTypes have different sizes
        result = repository.stringType(typeId3, types, otherStringTypes)
        // Assertion
        assertEquals(TypeEnum.UNASSIGNED.key, result)

        // 5- Function under test when :
        // - The type ID corresponds to a type from types
        // - stringTypes is empty
        result = repository.stringType(typeId2, types, mutableListOf())
        // Assertion
        assertEquals(TypeEnum.UNASSIGNED.key, result)

        // 6- Function under test when :
        // - The type ID doesn't correspond to a type from types
        // - stringTypes is empty
        result = repository.stringType(typeId3, types, mutableListOf())
        // Assertion
        assertEquals(TypeEnum.UNASSIGNED.key, result)
    }

}