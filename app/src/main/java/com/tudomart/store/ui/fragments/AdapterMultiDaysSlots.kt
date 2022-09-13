package com.tudomart.store.ui.fragments

import com.tudomart.store.model.ModelTimeSlotResponse
import com.tudomart.store.utils.toDateString
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.tudomart.store.R
import com.tudomart.store.databinding.RecyclerSlotBinding
import com.tudomart.store.databinding.RecyclerSlotHeaderBinding

class AdapterMultiDaysSlots(
    val data: List<Any?>,
    val selectedSlot: ModelTimeSlotResponse.Data?,
    var onClick: (ModelTimeSlotResponse.Data) -> Unit
) : BaseAdapter() {

    private val SLOT_ITEM = 0
    private val HEADER_ITEM = 1
    private var previousCheckedIcon: Int = -1
    override fun getCount(): Int = data.size

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()


    override fun getItemViewType(position: Int) =
        if (data[position] is String) HEADER_ITEM else SLOT_ITEM

    override fun getViewTypeCount() = 2
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            when (getItemViewType(position)) {
                SLOT_ITEM -> {
                    val slotData = data[position] as ModelTimeSlotResponse.Data
                    val bindingView =
                        RecyclerSlotBinding.inflate(LayoutInflater.from(parent.context))
                    selectedSlot?.let {
                        if (selectedSlot.pkTimeSlotId == (slotData).pkTimeSlotId!!) {
                            bindingView.check.visibility = View.VISIBLE
                            bindingView.txtSlot.setTextColor(
                                ContextCompat.getColor(
                                    parent.context,
                                    R.color.colorPrimary
                                )
                            )
                        }
                    }
                    slotData.strDisplayName?.let {
                        bindingView.txtSlot.text = it
                    }

                    if (slotData.intOrdersCount!! >= slotData.intCapacity!!) {
//                        val txtFull = view.findViewById<TextView>(R.id.txtFull)
                        bindingView.layParent.isEnabled = false
                        bindingView.layParent.isClickable = false
                        bindingView.layParent.setBackgroundColor(Color.parseColor("#FFF3F3F3"))
                    }
                    view = bindingView.root
                }
                HEADER_ITEM -> {
                    val bindingView =
                        RecyclerSlotHeaderBinding.inflate(LayoutInflater.from(parent!!.context))
                    view = bindingView.root
                }
            }
        }

        when (getItemViewType(position)) {

            SLOT_ITEM -> {
                val slotData = data[position] as ModelTimeSlotResponse.Data
                val bindingView = view?.let { RecyclerSlotBinding.bind(it) }
                bindingView?.apply {
                    txtSlot.text = slotData.strDisplayName
                    layParent.setOnClickListener {
                        check.visibility = VISIBLE
                        previousCheckedIcon = position
                        onClick(slotData)
                    }
                }

            }
            HEADER_ITEM -> {
                val bindingView = view?.let { RecyclerSlotHeaderBinding.bind(it) }
                bindingView?.apply {
                    try {
                        slotTitle.text =
                            getFormattedDate(data[position] as String)
                    } catch (e: Exception) {
                        slotTitle.text = data[position] as String
                    }
                }

            }

        }
        return view
    }

    private fun getFormattedDate(s: String): String {
        return try {
            s.toDateString()
        } catch (e: Exception) {
            e.printStackTrace()
            s
        }

    }
}