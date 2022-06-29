package kim.uno.mock.data.local.room.mock

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kim.uno.mock.extension.toFormat
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

@Entity(
    tableName = "mock",
    indices = [Index(value = ["message", "postTime"], unique = true)]
)
data class MockEntity(
    @PrimaryKey(autoGenerate = true)
    @RecyclerViewAdapter.ItemDiff
    var id: Long = 0,
    @RecyclerViewAdapter.ContentsDiff
    var message: String?,
    @RecyclerViewAdapter.ContentsDiff
    val postTime: Long
) {
    val date: String
        get() = postTime.toFormat("MMM d, yyyy h:mm a")
}