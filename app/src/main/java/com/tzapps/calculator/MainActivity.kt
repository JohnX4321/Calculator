package com.tzapps.calculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tzapps.calculator.db.Record
import com.tzapps.calculator.db.RecordDao
import com.tzapps.calculator.db.RecordsDatabase
import com.udojava.evalex.Expression
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

//TODO custom parser
class MainActivity : AppCompatActivity() {

    companion object {
        private const val IS_EXCEPTION=-1
        private const val IS_NUMBER=0
        private const val IS_OPERAND=1
        private const val IS_PAR_OPEN=2
        private const val IS_PAR_CLOSE=3
        private const val IS_DOT=4
    }

    private var parenthesisCount=0
    private var isDecimal=false
    private var isSolve=false
    private var endExpression = ""

    private lateinit var btnOne: AppCompatButton
    private lateinit var btnTwo: AppCompatButton
    private lateinit var btnThree: AppCompatButton
    private lateinit var btnFour: AppCompatButton
    private lateinit var btnFive: AppCompatButton
    private lateinit var btnSix: AppCompatButton
    private lateinit var btnSeven: AppCompatButton
    private lateinit var btnEight: AppCompatButton
    private lateinit var btnNine: AppCompatButton
    private lateinit var btnZero: AppCompatButton
    private lateinit var backspaceBtn: AppCompatButton
    private lateinit var historyBtn: AppCompatImageButton
    private lateinit var clearBtn: AppCompatButton
    private lateinit var pcBtn: AppCompatButton
    private lateinit var parenthesisBtn: AppCompatButton
    private lateinit var multiplyBtn: AppCompatButton
    private lateinit var divideBtn: AppCompatButton
    private lateinit var addBtn: AppCompatButton
    private lateinit var minusBtn: AppCompatButton
    private lateinit var dotBtn: AppCompatButton
    private lateinit var solveBtn: AppCompatButton
    private lateinit var moreBtn: AppCompatImageButton
    private lateinit var scrollView: HorizontalScrollView

    private lateinit var expressionTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var recordsDB: RecordDao
    private var recordsList = emptyList<Record>()
    private lateinit var adapter: HistoryAdapter

    private var lastExpCache = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        expressionTextView=findViewById(R.id.placeholder)
        resultTextView=findViewById(R.id.answer)
        btnOne=findViewById(R.id.btnOne)
        btnTwo=findViewById(R.id.btnTwo)
        btnThree=findViewById(R.id.btnThree)
        btnFour=findViewById(R.id.btnFour)
        btnFive=findViewById(R.id.btnFive)
        btnSix=findViewById(R.id.btnSix)
        btnSeven=findViewById(R.id.btnSeven)
        btnEight=findViewById(R.id.btnEight)
        btnNine=findViewById(R.id.btnNine)
        btnZero=findViewById(R.id.btnZero)
        clearBtn=findViewById(R.id.btnClear)
        pcBtn=findViewById(R.id.btnModulus)
        parenthesisBtn=findViewById(R.id.btnParenthesis)
        multiplyBtn=findViewById(R.id.btnMultiply)
        divideBtn=findViewById(R.id.btnDivide)
        addBtn=findViewById(R.id.btnAddition)
        minusBtn=findViewById(R.id.btnSubtract)
        dotBtn=findViewById(R.id.btnDot)
        solveBtn=findViewById(R.id.btnEquals)
        historyBtn=findViewById(R.id.history)
        backspaceBtn=findViewById(R.id.backSpace)
        scrollView=findViewById(R.id.scrollView)
        addNumber("0")

        if (savedInstanceState!=null) {
            expressionTextView.text=savedInstanceState.getString("expression","0")
            resultTextView.text=savedInstanceState.getString("result","")
        }

        btnZero.setOnClickListener {
            addNumber("0")
        }
        btnOne.setOnClickListener {
            addNumber("1")
            scrollExpressionViewToRight()
        }
        btnTwo.setOnClickListener {
            addNumber("2")
        }
        btnThree.setOnClickListener {
            addNumber("3")
        }
        btnFour.setOnClickListener {
            addNumber("4")
        }
        btnFive.setOnClickListener {
            addNumber("5")
        }
        btnSix.setOnClickListener {
            addNumber("6")
        }
        btnSeven.setOnClickListener {
            addNumber("7")
        }
        btnEight.setOnClickListener {
            addNumber("8")
        }
        btnNine.setOnClickListener {
            addNumber("9")
        }
        addBtn.setOnClickListener {
            addOperand("+")
        }
        minusBtn.setOnClickListener {
            addOperand("-")
        }
        multiplyBtn.setOnClickListener {
            addOperand("*")
        }
        divideBtn.setOnClickListener {
            addOperand("/")
        }
        pcBtn.setOnClickListener {
            addOperand("%")
        }
        dotBtn.setOnClickListener {
            addDecimal()
        }
        parenthesisBtn.setOnClickListener {
            addParenthesis()
        }

        historyBtn.setOnClickListener {
            val historyDialog = BottomSheetDialog(this,BottomSheetBehavior.PEEK_HEIGHT_AUTO).apply {
                fetchHistory()
                setContentView(R.layout.dialog_history)
                val recyclerView: RecyclerView = findViewById(R.id.historyRV)!!
                adapter = HistoryAdapter(recordsList)
                recyclerView.layoutManager= LinearLayoutManager(this@MainActivity)
                recyclerView.adapter=adapter
                recyclerView.addItemDecoration(
                    DividerItemDecoration(this@MainActivity,
                        DividerItemDecoration.VERTICAL)
                )
                recyclerView.setHasFixedSize(true)
                findViewById<AppCompatButton>(R.id.historyClearBtn)!!.setOnClickListener {
                    recordsList= emptyList()
                    if (::adapter.isInitialized)
                        adapter.recordsList=recordsList
                    CoroutineScope(Dispatchers.IO).launch {
                        recordsDB.deleteAll()
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            historyDialog.show()
        }

        backspaceBtn.setOnClickListener {
            if (expressionTextView.text.isNullOrEmpty()||expressionTextView.text=="0") return@setOnClickListener
            if (expressionTextView.text.length==1) {
                expressionTextView.text = "0"
                return@setOnClickListener
            }
            expressionTextView.text=expressionTextView.text.toString().substring(0,expressionTextView.text.lastIndex)
        }

        recordsDB= RecordsDatabase(this).recordsDao()
        fetchHistory()

        clearBtn.setOnClickListener {
            expressionTextView.text=""
            resultTextView.text=""
            parenthesisCount=0
            isDecimal=false
            isSolve=false
        }
        solveBtn.setOnClickListener {
            val expLocal = expressionTextView.text.toString()
            if (expLocal==lastExpCache)
                return@setOnClickListener
            if (expLocal.isNotEmpty()) {
                solve(expressionTextView.text.toString())
                fetchHistory()
            }
        }

    }

    private fun scrollExpressionViewToRight() {
        Handler(Looper.getMainLooper()).post {
            scrollView.fullScroll(View.FOCUS_RIGHT)
        }
    }


    private fun fetchHistory() {
        CoroutineScope(Dispatchers.Main).launch {
            recordsList = recordsDB.getAll()
            if (::adapter.isInitialized) {
                adapter.recordsList = recordsList
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun addNumber(number: String) {
        val operationLength = expressionTextView.text.length
        if (operationLength>0){
            val lastChar=expressionTextView.text[operationLength-1].toString()
            val lastCharState=lastCharacter(lastChar)
            if (operationLength==1&&lastCharState==IS_NUMBER&&lastChar=="0"){
                expressionTextView.text = number
            } else if (lastCharState==IS_PAR_OPEN){
                expressionTextView.text=expressionTextView.text.toString()+number
            } else if (lastCharState==IS_PAR_CLOSE){
                expressionTextView.text=expressionTextView.text.toString()+"*"+number
            } else if (lastCharState==IS_NUMBER||lastCharState==IS_OPERAND||lastCharState==IS_DOT){
                expressionTextView.text=expressionTextView.text.toString()+number
            }
        } else {
            expressionTextView.text=expressionTextView.text.toString()+number
        }
        scrollExpressionViewToRight()
    }

    private fun addOperand(operand: String) {
       var operationLength = expressionTextView.text.length
        if (operationLength>0){
            val lastInp=expressionTextView.text[operationLength-1].toString()
            if (lastInp=="+"||lastInp=="-"||lastInp=="*"||lastInp=="/"||lastInp=="%")
                resultTextView.text = getString(R.string.invalid)
            else if (operand=="%"&&lastCharacter(lastInp)==IS_NUMBER) {
                expressionTextView.text = expressionTextView.text.toString()+operand
                isDecimal=false
                isSolve=false
                endExpression=""
            } else if (operand!="%") {
                expressionTextView.text=expressionTextView.text.toString()+operand
                isDecimal=false
                isSolve=false
                endExpression=""
            }
        } else if (operand=="-")
            expressionTextView.text="-"
        scrollExpressionViewToRight()
    }

    private fun addParenthesis() {
        val operationLength=expressionTextView.text.length
        if (operationLength==0) {
            expressionTextView.text=expressionTextView.text.toString()+"("
            isDecimal=false
            parenthesisCount++
        } else if (parenthesisCount>0&&operationLength>0) {
            val lastInput=expressionTextView.text[operationLength-1].toString()
            when (lastCharacter(lastInput)) {
                IS_NUMBER->{
                    expressionTextView.text=expressionTextView.text.toString()+")"
                    parenthesisCount--
                    isDecimal=false
                }
                IS_OPERAND->{
                    expressionTextView.text=expressionTextView.text.toString()+"("
                    parenthesisCount++
                    isDecimal=false
                }
                IS_PAR_OPEN->{
                    expressionTextView.text=expressionTextView.text.toString()+"("
                    parenthesisCount++
                    isDecimal=false
                }
                IS_PAR_CLOSE->{
                    expressionTextView.text=expressionTextView.text.toString()+")"
                    parenthesisCount--
                    isDecimal=false
                }
            }
        } else if (parenthesisCount==0&&operationLength>0) {
            val lastInput=expressionTextView.text[operationLength-1].toString()
            if (lastCharacter(lastInput)==IS_OPERAND) {
                expressionTextView.text=expressionTextView.text.toString()+"("
                isDecimal=false
                parenthesisCount++
            } else {
                expressionTextView.text=expressionTextView.text.toString()+"*("
                isDecimal=false
                parenthesisCount++
            }
        }
        scrollExpressionViewToRight()
    }

    private fun addDecimal() {
        when {
            expressionTextView.text.isEmpty() -> {
                expressionTextView.text="0."
            }
            lastCharacter(expressionTextView.text[expressionTextView.text.lastIndex].toString())==IS_OPERAND -> {
                expressionTextView.text=expressionTextView.text.toString()+"0."
            }
            lastCharacter(expressionTextView.text[expressionTextView.text.lastIndex].toString())==IS_NUMBER -> {
                expressionTextView.text=expressionTextView.text.toString()+"."
            }
        }
        isDecimal=true
        scrollExpressionViewToRight()
    }

    private fun solve(input: String) {
        var resultText: String
        val res: BigDecimal?
        try {
            var temp=input
            if (isSolve)
                temp=input+endExpression
            else saveLastExpression(input)
            res=Expression(temp).setPrecision(12).eval(true)
            isSolve=true
            resultText=res!!.toPlainString()//.setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString()
            if (resultText.length>=15) {
                val format = DecimalFormat("0.0E0").apply {
                    roundingMode=RoundingMode.DOWN
                    minimumFractionDigits = 5
                }
                resultText=format.format(res)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            resultTextView.text=getString(R.string.invalid)
            return
        }
        if (resultText==".") {
            resultText=resultText.replace("\\.?0*$","")
        }
        resultTextView.text=resultText
        CoroutineScope(Dispatchers.IO).launch {
            if (recordsDB.countAll()==20) {
                val tmpR = recordsDB.getAll()[0]
                recordsDB.delete(tmpR)
            }
            recordsDB.insert(Record(exprName = expressionTextView.text.toString(),exp = resultText))
        }
        lastExpCache = input

    }

    private fun lastCharacter(last: String): Int {
        try {
            Integer.parseInt(last)
            return IS_NUMBER
        } catch (e: NumberFormatException) {}
        return when (last) {
            "+", "-", "*", "/","%" -> IS_OPERAND
            "(" -> IS_PAR_OPEN
            ")" -> IS_PAR_CLOSE
            "." -> IS_DOT
            else -> IS_EXCEPTION
        }
    }

    private fun saveLastExpression(input: String){
        //TODO(Use Stack)
        val loExpr=input[input.lastIndex].toString()
        if (input.length>1){
            if (loExpr==")"){
                endExpression=")"
                var numOfClose=1
                for (i in input.length-2..-1) {
                    if (numOfClose>0){
                        val last=input[i].toString()
                        if (last==")") numOfClose++
                        else if (last=="(") numOfClose--
                        endExpression=last+endExpression
                    } else if (lastCharacter(input[i].toString())==IS_OPERAND){
                        endExpression=input[i]+endExpression
                        break
                    } else endExpression=""
                }
            } else if (lastCharacter(loExpr)==IS_NUMBER){
                endExpression=loExpr
                for (i in input.length-2..-1){
                    val last=input[i].toString()
                    if (lastCharacter(last)==IS_NUMBER||lastCharacter(last)==IS_DOT)
                        endExpression=last+endExpression
                    else if (lastCharacter(last)==IS_OPERAND){
                        endExpression=last+endExpression
                        break
                    }
                    if (i==0) endExpression=""
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("expression",expressionTextView.text.toString())
        outState.putString("result",resultTextView.text.toString())
    }



}