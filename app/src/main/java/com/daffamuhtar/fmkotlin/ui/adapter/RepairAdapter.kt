package com.daffamuhtar.fmkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daffamuhtar.fmkotlin.databinding.ItemRepairBinding
import com.daffamuhtar.fmkotlin.domain.model.Repair
import com.daffamuhtar.fmkotlin.util.RepairHelper
import com.daffamuhtar.fmkotlin.util.RepairHelper.Companion.getRepairDate
import com.daffamuhtar.fmkotlin.util.RepairHelper.Companion.getRepairIcon
import com.daffamuhtar.fmkotlin.util.RepairHelper.Companion.getRepairId
import com.daffamuhtar.fmkotlin.util.RepairHelper.Companion.getRepairTitle
import com.daffamuhtar.fmkotlin.util.VehicleHelper.Companion.getVehicleName
import java.util.*

class RepairAdapter() : RecyclerView.Adapter<RepairAdapter.LaporanViewHolder>() {

    private var allRepair = listOf<Repair>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setAllLaporan(allRepair: List<Repair>) {
        allRepair.let {
            this.allRepair = it
            notifyDataSetChanged()
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: Repair, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class LaporanViewHolder(private val view: ItemRepairBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(repair: Repair, position: Int) {
            with(view) {

                lyWorkshop.visibility = View.GONE
                lyAssignmentNote.visibility = View.GONE
                val locale = Locale("id")

                tvRepairTitle.text = getRepairTitle(repair.orderId, repair.isStoring)
                ivRepairIcon.setBackgroundResource(getRepairIcon(repair.orderId, repair.isStoring))
                tvRepairId.text = getRepairId(repair.orderId, repair.spkId)
                tvRepairDate.text = getRepairDate(repair.startAssignment)
                RepairHelper.setRepairStage(
                    view.root.context,
                    repair.stageId.toInt(),
                    repair.stageName,
                    tvRepairStage,
                    ivRepairStageIcon,
                    lyRepairStage
                )

                tvVehicleName.text = getVehicleName(
                    repair.vehicleId,
                    repair.vehicleBrand,
                    repair.vehicleType,
                    repair.vehicleVarian,
                    repair.vehicleYear,
                    repair.vehicleLicenseNumber
                )

                repair.workshopName?.let {
                    lyWorkshop.visibility = View.VISIBLE
                    tvWorkshopName.text = repair.workshopName
                    tvWowkshopAddress.text = repair.workshopLocation
                }

                repair.noteFromSA?.let {
                    lyAssignmentNote.visibility = View.VISIBLE
                    tvAssignmentNote.text = repair.noteFromSA
                }

                view.root.setOnClickListener {
                    onItemClickCallback.onItemClicked(repair, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = ItemRepairBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        holder.bind(allRepair[position], position)
    }

    override fun getItemCount(): Int = allRepair.size
}