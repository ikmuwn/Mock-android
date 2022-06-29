package kim.uno.mock.data.local.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kim.uno.mock.MockApp
import kim.uno.mock.data.local.room.mock.MockDao
import kim.uno.mock.data.local.room.mock.MockEntity

@Database(
    version = 1,
    entities = [MockEntity::class]
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun mockDao(): MockDao

    companion object {
        val master by lazy {
            Room.databaseBuilder(
                MockApp.context,
                kim.uno.mock.data.local.room.Database::class.java,
                "master.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}