//package com.daffamuhtar.fmkotlin.appv3
//
//import androidx.paging.PagingSource
//import com.daffamuhtar.fmkotlin.services.RepairServices
//
//private const val TMDB_STARTING_PAGE_INDEX = 1
//
//
//class RepairPagingSource(
//    private val service: RepairServices
//) : PagingSource<Int, RepairResponse3>() {
//
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepairResponse3> {
//        val pageIndex = params.key ?: TMDB_STARTING_PAGE_INDEX
//        return try {
//            val response = service.getRepairOngoingNew4(
//                loggedMechanicId = "MEC-MBA-99",
//                orderType = null,
//                stageGroup = null,
//                page = pageIndex,
//                perpage = 10
//            )
//            val repairs = response.data
//            val nextKey =
//                if (repairs.isEmpty()) {
//                    null
//                } else {
//                    // By default, initial load size = 3 * NETWORK PAGE SIZE
//                    // ensure we're not requesting duplicating items at the 2nd request
//                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
//                }
//            LoadResult.Page(
//                data = movies,
//                prevKey = if (pageIndex == TMDB_STARTING_PAGE_INDEX) null else pageIndex,
//                nextKey = nextKey
//            )
//        } catch (exception: IOException) {
//            return LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            return LoadResult.Error(exception)
//        }
//    }
//
//    /**
//     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
//     */
//    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
//        // We need to get the previous key (or next key if previous is null) of the page
//        // that was closest to the most recently accessed index.
//        // Anchor position is the most recently accessed index.
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//}