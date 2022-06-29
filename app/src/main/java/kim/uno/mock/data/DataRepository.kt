package kim.uno.mock.data

import kim.uno.mock.data.local.LocalData
import kim.uno.mock.data.local.room.mock.MockEntity
import javax.inject.Inject

open class DataRepository @Inject constructor(
    private val localData: LocalData
) {

    suspend fun insertMock(mockEntity: MockEntity) =
        localData.insertMock(mockEntity = mockEntity)

    suspend fun getMockList(
        size: Int,
        postTime: Long?
    ) = localData.getMockList(
        size = size,
        postTime = postTime
    ).let {
        if (it.isEmpty() && postTime == null) {
            repeat(90) { index ->
                insertMock(
                    MockEntity(
                        message = "mock item ${index + 1}",
                        postTime = System.currentTimeMillis()
                    )
                )
            }
            localData.getMockList(
                size = size,
                postTime = postTime
            )
        } else {
            it
        }
    }

    suspend fun deleteMock(mockEntity: MockEntity) =
        localData.deleteMock(mockEntity = mockEntity)

}