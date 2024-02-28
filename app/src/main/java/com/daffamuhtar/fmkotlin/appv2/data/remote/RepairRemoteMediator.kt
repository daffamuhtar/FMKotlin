package com.daffamuhtar.fmkotlin.appv2.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.daffamuhtar.fmkotlin.appv2.data.local.FleetifyMechanicDatabase
import com.daffamuhtar.fmkotlin.appv2.data.local.RepairEntity
import com.daffamuhtar.fmkotlin.appv2.data.mapper.toRepairEntity
import com.daffamuhtar.fmkotlin.services.RepairServices
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val fleetifyMechanicDatabase: FleetifyMechanicDatabase,
    private val repairServices: RepairServices
): RemoteMediator<Int, RepairEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepairEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val repairs = repairServices.getRepairOngoingNew3(
                loggedMechanicId = "MEC-MBA-99",
                orderType = null,
                stageGroup = null,
                page = loadKey,
                perpage = 10
            )

            fleetifyMechanicDatabase.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    fleetifyMechanicDatabase.dao.clearAll()
                }
                val repairEntity = repairs.data.map { it.toRepairEntity() }
                fleetifyMechanicDatabase.dao.upsertAll(repairEntity)
            }

            MediatorResult.Success(
                endOfPaginationReached = repairs.data.isEmpty()
            )
        } catch(e: IOException) {
            MediatorResult.Error(e)
        } catch(e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}