package kim.uno.mock.ui.recyclerview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kim.uno.mock.MainCoroutineRule
import kim.uno.mock.data.DataRepository
import kim.uno.mock.data.local.room.mock.MockEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RecyclerViewViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockDataRepository: DataRepository
    private lateinit var viewModel: RecyclerViewViewModel
    private lateinit var mockList: ArrayList<MockEntity>

    @Before
    fun setUp() {
        mockList = ArrayList<MockEntity>().apply {
            repeat(5) { index ->
                add(
                    MockEntity(
                        message = "mock item ${index + 1}",
                        postTime = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    @Test
    fun `get mock list`() = runBlocking {
        Mockito.`when`(
            mockDataRepository.getMockList(
                size = 20,
                postTime = null
            )
        ).thenReturn(mockList)

        viewModel = RecyclerViewViewModel(mockDataRepository)
        viewModel.refresh()
        viewModel.mockList.observeForever { }

        Assert.assertEquals(viewModel.mockList.value, mockList)
    }

}
