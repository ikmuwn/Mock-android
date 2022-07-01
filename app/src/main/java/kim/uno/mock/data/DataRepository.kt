package kim.uno.mock.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kim.uno.mock.data.local.LocalData
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.data.remote.github.GithubData
import kim.uno.mock.util.Logger
import retrofit2.Response
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
open class DataRepository @Inject constructor(
    private val localData: LocalData,
    private val githubData: GithubData
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


    /**
     * remote
     */

    private suspend fun <T> parseResult(
        request: suspend () -> Response<T>
    ): RepositoryResult<T> {
        return try {
            RepositoryResult.Success(request().body())
        } catch (e: Exception) {
            Logger.e(throwable = e)
            RepositoryResult.Error(e)
        }
    }

    suspend fun searchRepositories(
        query: String,
        page: Int
    ) = parseResult {
        githubData.searchRepositories(
            query = query,
            page = page
        )
    }

}
