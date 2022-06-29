package kim.uno.mock.data.local.room.mock

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mockEntity: MockEntity): Long

    @Query("SELECT DISTINCT * FROM mock WHERE postTime > :postTime ORDER BY postTime ASC LIMIT :size")
    suspend fun getList(
        size: Int,
        postTime: Long
    ): List<MockEntity>

    @Query("DELETE FROM mock WHERE id = :id")
    suspend fun delete(id: Long): Int

}