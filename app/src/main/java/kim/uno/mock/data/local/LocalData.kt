package kim.uno.mock.data.local

import kim.uno.mock.MockApp
import kim.uno.mock.data.local.room.Database
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.extension.clear
import kim.uno.mock.extension.pref
import javax.inject.Inject

class LocalData @Inject constructor() {

    private val pref by lazy { MockApp.context.pref }
    private val db by lazy { Database.master }
    private val notificationDao by lazy { db.mockDao() }

    fun clearPref() = pref.clear()

    suspend fun insertMock(mockEntity: MockEntity) = notificationDao.insert(mockEntity = mockEntity)

    suspend fun getMockList(size: Int, postTime: Long?) =
        notificationDao.getList(
            size = size,
            postTime = postTime ?: 0L
        )

    suspend fun deleteMock(mockEntity: MockEntity) = notificationDao.delete(id = mockEntity.id)

}