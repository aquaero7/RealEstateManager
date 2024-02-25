package com.aquaero.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aquaero.realestatemanager.CACHE_EMPTY_STRING_VALUE
import com.aquaero.realestatemanager.CACHE_LONG_ID_VALUE
import com.aquaero.realestatemanager.CACHE_NULLABLE_VALUE
import com.aquaero.realestatemanager.NO_PHOTO_ID
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Entity(foreignKeys = [
    ForeignKey(entity = Property::class, parentColumns = ["propertyId"], childColumns = ["propertyId"],
        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE,
    ),
])
data class Photo(
    /*
     * Warning!
     * Due to the use of functions belonging to this class,
     * be sure to update their code if the name of the class property 'photoId' changes
     */
    @PrimaryKey(autoGenerate = true)
    val photoId: Long = 0,
    val uri: String,
    val label: String?,
    @ColumnInfo(index = true)
    var propertyId: Long,
) {
    /**
     * Creates a new instance of the object without its id (or with its id set to 0),
     * so that it will be autogenerated by Room. The id is identified by its name.
     */
    fun withoutId(): Photo {
        /*
         * Warning!
         * If the name of the class property 'photoId' used in this function
         * changes in the constructor, be sure to update it
         */

        // Get the primary constructor
        val constructor = this::class.primaryConstructor
            ?: error("Primary constructor not found for ${this::class.simpleName}")
        // Fill the map with parameter values, excluding the id
        val argsMap = constructor.parameters
            .filter { it.name != "photoId" }
            .associateWith { parameter -> this::class.memberProperties.first { it.name == parameter.name }.call(this) }
        // Create and return the new instance
        return constructor.callBy(argsMap)
    }

}

val NO_PHOTO = Photo(photoId = NO_PHOTO_ID, uri = "", label = "", propertyId = NO_PHOTO_ID)

val CACHE_PHOTO = Photo(
    photoId = CACHE_LONG_ID_VALUE,
    uri = CACHE_EMPTY_STRING_VALUE,
    label = CACHE_NULLABLE_VALUE,
    propertyId = CACHE_LONG_ID_VALUE
)