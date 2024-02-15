package com.aquaero.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.aquaero.realestatemanager.model.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun prepopulateWithAddresses(addresses: List<Address>)

    @Upsert     // Insert ou update (if primary key already exists)
    suspend fun upsertAddress(address: Address): Long

    @Delete
    suspend fun deleteAddress(address: Address)

    @Query("SELECT * FROM address WHERE addressId = :adId")
    fun getAddress(adId: Long): Flow<Address>

    @Query("SELECT * FROM address")
    fun getAddresses(): Flow<MutableList<Address>>

    @Query("SELECT * FROM address ORDER BY addressId ASC")
    fun getAddressesOrderedById(): Flow<MutableList<Address>>

    @Query("SELECT * FROM address ORDER BY city ASC")
    fun getAddressesOrderedByCity(): Flow<MutableList<Address>>

}