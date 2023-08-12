package com.tzapps.calculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tzapps.calculator.databinding.ActivityConvertorBinding
import com.tzapps.calculator.utils.ConverterUtils
import com.tzapps.calculator.utils.Utils
import java.math.BigDecimal

class ConvertorActivity: AppCompatActivity(),ConverterAdapter.RecyclerItemClickListener {


    companion object {
        var type = 0
        var fromIndex = 0
        var toIndex = 0
    }

    private lateinit var binding: ActivityConvertorBinding
    private val handler = Handler(Looper.myLooper()?: Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConvertorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoryRecyclerView.adapter = ConverterAdapter(this)
        binding.categoryRecyclerView.setHasFixedSize(true)
        binding.fromConvertSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                fromIndex=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.toConvertSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toIndex=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        /*try {
            when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES-> binding.fromInput.setHintTextColor(Color.WHITE)
                else -> binding.fromInput.setHintTextColor(Color.BLACK)
            }
        } catch (e: Exception) {

        }*/
        binding.convertButton.setOnClickListener {
            try {
                if (binding.fromInput.text.isNullOrEmpty())
                    return@setOnClickListener
                val f: String
                try {
                    f = binding.fromInput.text.toString()
                } catch (ex: Exception) {
                    return@setOnClickListener
                }

                when (type) {
                    0 -> {
                        handler.post { convertLength(f.toBigDecimal()) }
                    }
                    1 -> {
                        handler.post { convertArea(f.toBigDecimal()) }
                    }
                    2 -> {
                        handler.post { convertTemperature(f.toBigDecimal()) }
                    }
                    3 -> {
                        handler.post { convertVolume(f.toBigDecimal()) }
                    }
                    4 -> {
                        handler.post { convertWeight(f.toBigDecimal()) }
                    }
                }
            } catch (e: Exception) {
                Log.e("Calculator","Conversion Error: ${e.message.toString()}")
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                binding.categoryRecyclerView.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<TextView>(android.R.id.text1)?.performClick()
            } catch (e: Exception) {}
        },300)
    }

    private fun convertArea(a: BigDecimal) {
        when {
            fromIndex == toIndex -> {
                binding.toInput.text = a.toPlainString()
            }
            /*fromIndex == 5 -> {
                binding.toInput.text = when(toIndex) {
                    0-> ConverterUtils.SqmToAcres(a)
                    1-> ConverterUtils.SqmToH(a)
                    2-> ConverterUtils.SqmToSqcm(a)
                    3-> ConverterUtils.SqmToSqft(a)
                    else-> ConverterUtils.SqmToSqi(a)
                }.toPlainString()
            }
            toIndex == 5 -> {
                binding.toInput.text = when(fromIndex) {
                    0-> ConverterUtils.AcresToSqm(a)
                    1-> ConverterUtils.HToSqm(a)
                    2-> ConverterUtils.SqcmToSqm(a)
                    3-> ConverterUtils.SqftToSqm(a)
                    else-> ConverterUtils.SqiToSqm(a)
                }.toPlainString()
            }*/
            else -> {
                val ta = when(fromIndex) {
                    0-> ConverterUtils.AcresToSqm(a)
                    1-> ConverterUtils.HToSqm(a)
                    2-> ConverterUtils.SqcmToSqm(a)
                    3-> ConverterUtils.SqftToSqm(a)
                    4-> ConverterUtils.SqiToSqm(a)
                    else-> a
                }
                val tb = when(toIndex) {
                    0-> ConverterUtils.SqmToAcres(ta)
                    1-> ConverterUtils.SqmToH(ta)
                    2-> ConverterUtils.SqmToSqcm(ta)
                    3-> ConverterUtils.SqmToSqft(ta)
                    4-> ConverterUtils.SqmToSqi(ta)
                    else-> ta
                }
                binding.toInput.text = ConverterUtils.format(tb)
            }
        }
    }

    private fun convertLength(a: BigDecimal) {
        when {
            fromIndex == toIndex -> {
                binding.toInput.text = a.toPlainString()
            }
            else -> {
                val ta = when(fromIndex) {
                    0-> a
                    1-> ConverterUtils.MMToM(a)
                    2-> ConverterUtils.CMToM(a)
                    3-> ConverterUtils.KMToM(a)
                    4-> ConverterUtils.InToM(a)
                    5-> ConverterUtils.FtToM(a)
                    6-> ConverterUtils.YdToM(a)
                    7-> ConverterUtils.MiToM(a)
                    else-> ConverterUtils.NMToM(a)
                }
                val tb = when(toIndex) {
                    0-> ta
                    1-> ConverterUtils.MToMM(ta)
                    2-> ConverterUtils.MToCM(ta)
                    3-> ConverterUtils.MToKM(ta)
                    4-> ConverterUtils.MToIn(ta)
                    5-> ConverterUtils.MToFt(ta)
                    6-> ConverterUtils.MToYd(ta)
                    7-> ConverterUtils.MToMi(ta)
                    else-> ConverterUtils.MToNM(ta)
                }.toPlainString()
                binding.toInput.text = tb
            }
        }
    }


    private fun convertTemperature(a: BigDecimal) {
        when {
            fromIndex == toIndex -> {
                binding.toInput.text = a.toPlainString()
            }
            else -> {
                val ta = when(fromIndex) {
                    0-> ConverterUtils.FtoC(a)
                    1-> a
                    else-> ConverterUtils.KToC(a)
                }
                val tb = when(toIndex) {
                    0-> ConverterUtils.CToF(ta)
                    1-> ta
                    else-> ConverterUtils.CToK(ta)
                }.toPlainString()
                binding.toInput.text = tb
            }
        }
    }

    private fun convertVolume(a: BigDecimal) {
        when {
            fromIndex == toIndex -> {
                binding.toInput.text = a.toPlainString()
            }
            else -> {
                val ta = when(fromIndex) {
                    0-> ConverterUtils.GUSToL(a)
                    1-> ConverterUtils.GUKToL(a)
                    2-> a
                    else-> ConverterUtils.MLToL(a)
                }
                val tb = when(toIndex) {
                    0-> ConverterUtils.LToGUS(ta)
                    1-> ConverterUtils.LToGUK(ta)
                    2-> ta
                    else-> ConverterUtils.LToMl(ta)
                }.toPlainString()
                binding.toInput.text = tb
            }
        }
    }

    private fun convertWeight(a: BigDecimal) {
        when {
            fromIndex == toIndex -> {
                binding.toInput.text = a.toPlainString()
            }
            else -> {
                val ta = when(fromIndex) {
                    0-> a
                    1-> ConverterUtils.GToKG(a)
                    2-> ConverterUtils.TToKG(a)
                    3-> ConverterUtils.PToKG(a)
                    else-> ConverterUtils.OZToKG(a)
                }
                val tb = when(toIndex) {
                    0-> ta
                    1-> ConverterUtils.KGToG(ta)
                    2-> ConverterUtils.KGToT(ta)
                    3-> ConverterUtils.KGToP(ta)
                    else-> ConverterUtils.KGToOZ(ta)
                }.toPlainString()
                binding.toInput.text = tb
            }
        }
    }

    override fun onRecyclerItemClick(pos: Int) {
        //0 - Length, 1 - Area, 2 - Temp, 3 - Volume, 4 - Weight
        type = pos
        handler.post {
            when (pos) {
                0 -> {
                    binding.fromConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.LENGTH_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.fromConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    binding.toConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.LENGTH_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.toConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
                1 -> {
                    binding.fromConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.AREA_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.fromConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    binding.toConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.AREA_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.toConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
                2 -> {
                    binding.fromConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.TEMP_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.fromConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    binding.toConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.TEMP_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.toConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
                3 -> {
                    binding.fromConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.VOLUME_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.fromConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    binding.toConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.VOLUME_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.toConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
                4 -> {
                    binding.fromConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.WEIGHT_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.fromConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                    binding.toConvertSpinner.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, Utils.WEIGHT_CATEGORY
                    )//.apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    (binding.toConvertSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()
                }
            }
            binding.fromConvertSpinner.setSelection(0)
            binding.toConvertSpinner.setSelection(0)
        }
    }

    override fun onDestroy() {
        ConverterAdapter.lastSelectedIndex=-1
        super.onDestroy()
    }



}