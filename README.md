# Mock-android
- 스켈레톤 mock 앱 작업 모음
- Extensions, Widgets, Uitilities 샘플 코드 작성

## Progress
- RecyclerView Adapter 보일러플레이터 제거 목적 클래스 [RecyclerViewAdpater.kt](https://github.com/ikmuwn/Mock-android/blob/master/app/src/main/java/kim/uno/mock/util/recyclerview/RecyclerViewAdapter.kt) and [(RecyclerViewAdapter repo readme)](https://github.com/ikmuwn/RecyclerViewAdatper/edit/main/README.md)
- RecyclerViewAdapter.kt 구현 간소화를 위한 [RecyclerViewAdapter.Builder](https://github.com/ikmuwn/Mock-android/blob/2ce9a1f7db5964725301614ec3f2d64b632bb96e/app/src/main/java/kim/uno/mock/util/recyclerview/RecyclerViewAdapter.kt#L310)
- RecyclerViewAdapter를 활용한 Draggable 구현 [DraggableRecyclerAdapter.kt](https://github.com/ikmuwn/Mock-android/blob/master/app/src/main/java/kim/uno/mock/util/recyclerview/DraggableRecyclerAdapter.kt)
- RecyclerViewAdapter를 활용한 Infinite loop scroll 구현 [InfiniteRecyclerViewAdapter.kt](https://github.com/ikmuwn/Mock-android/blob/master/app/src/main/java/kim/uno/mock/util/recyclerview/InfiniteRecyclerViewAdapter.kt)
- 네트워크, DB 등의 리스트 조회에 적용 가능한 범용 페이지네이션 클래스 [Paging.kt](https://github.com/ikmuwn/Mock-android/blob/master/app/src/main/java/kim/uno/mock/util/Paging.kt)
