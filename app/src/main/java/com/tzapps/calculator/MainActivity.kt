package com.tzapps.calculator

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
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
import com.tzapps.calculator.databinding.ActivityMainBinding
import com.tzapps.calculator.db.Record
import com.tzapps.calculator.db.RecordDao
import com.tzapps.calculator.db.RecordsDatabase
import com.udojava.evalex.AbstractOperator
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
    private lateinit var binding: ActivityMainBinding

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
    private lateinit var modBtn: AppCompatButton
    private lateinit var parenthesisBtn: AppCompatButton
    private lateinit var multiplyBtn: AppCompatButton
    private lateinit var divideBtn: AppCompatButton
    private lateinit var addBtn: AppCompatButton
    private lateinit var minusBtn: AppCompatButton
    private lateinit var dotBtn: AppCompatButton
    private lateinit var solveBtn: AppCompatButton
    private lateinit var scrollView: HorizontalScrollView
    private lateinit var converterBtn: AppCompatImageButton
    private lateinit var infoBtn: AppCompatImageButton
    private lateinit var pcBtn: AppCompatButton

    private lateinit var expressionTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var recordsDB: RecordDao
    private var recordsList = emptyList<Record>()
    private lateinit var adapter: HistoryAdapter

    private var lastExpCache = ""
    private var lastSolCache=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        expressionTextView=binding.placeholder
        resultTextView=binding.answer
        btnOne=binding.btnOne
        btnTwo=binding.btnTwo
        btnThree=binding.btnThree
        btnFour=binding.btnFour
        btnFive=binding.btnFive
        btnSix=binding.btnSix
        btnSeven=binding.btnSeven
        btnEight=binding.btnEight
        btnNine=binding.btnNine
        btnZero=binding.btnZero
        clearBtn=binding.btnClear
        modBtn=binding.btnModulus
        parenthesisBtn=binding.btnParenthesis
        multiplyBtn=binding.btnMultiply
        divideBtn=binding.btnDivide
        addBtn=binding.btnAddition
        minusBtn=binding.btnSubtract
        dotBtn=binding.btnDot
        solveBtn=binding.btnEquals
        historyBtn=binding.history
        backspaceBtn=binding.backSpace
        scrollView=binding.scrollView
        converterBtn = binding.converterBtn
        infoBtn = binding.infoBtn
        pcBtn=binding.btnPc
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
        modBtn.setOnClickListener {
            addOperand("M")
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
            val historyDialog = BottomSheetDialog(this,BottomSheetBehavior.STATE_HALF_EXPANDED).apply {
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
                        try {
                            recordsDB.deleteAll()
                        } catch (e: Exception) {
                            Log.e("Calculator",e.message.toString())
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            historyDialog.show()
        }

        converterBtn.setOnClickListener {
            startActivity(Intent(this,ConvertorActivity::class.java))
        }

        infoBtn.setOnClickListener {
            val b = BottomSheetDialog(this,R.style.CustomBottomSheetDialogTheme)
            b.setContentView(R.layout.layout_bottomsheet)
            val s = getString(R.string.about_text)
            val ss = SpannableString(s)
            ss.setSpan(RelativeSizeSpan(2.5f),0,4,0)
            b.findViewById<TextView>(R.id.textView)?.text = ss
            b.show()
        }

        backspaceBtn.setOnClickListener {
            if (expressionTextView.text.isNullOrEmpty()||expressionTextView.text=="0") return@setOnClickListener
            if (expressionTextView.text.length==1) {
                expressionTextView.text = "0"
                return@setOnClickListener
            }
            expressionTextView.text=expressionTextView.text.toString().substring(0,expressionTextView.text.lastIndex)
        }

        try {
            recordsDB = RecordsDatabase(this).recordsDao()
        } catch (e: Exception) {

        }
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
            if (expLocal==lastExpCache) {
                resultTextView.text=lastSolCache
                return@setOnClickListener
            }
            if (expLocal.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    solve(expressionTextView.text.toString())
                }
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
            recordsList = try {
                recordsDB.getAll()
            } catch (e: Exception) {
                emptyList()
            }
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
                expressionTextView.text= String.format(getString(R.string.string_normal),expressionTextView.text,number)//expressionTextView.text.toString()+number
            } else if (lastCharState==IS_PAR_CLOSE||lastChar=="%"){
                expressionTextView.text= String.format(getString(R.string.string_par_mult),expressionTextView.text,number)//expressionTextView.text.toString()+"*"+number
            } else if (lastCharState==IS_NUMBER||lastCharState==IS_OPERAND||lastCharState==IS_DOT){
                expressionTextView.text= String.format(getString(R.string.string_normal),expressionTextView.text,number)//expressionTextView.text.toString()+number
            }
        } else {
            expressionTextView.text= String.format(getString(R.string.string_normal),expressionTextView.text,number)//expressionTextView.text.toString()+number
        }
        scrollExpressionViewToRight()
    }

    private fun addOperand(operand: String) {
       val operationLength = expressionTextView.text.length
        if (operationLength>0){
            val lastInp=expressionTextView.text[operationLength-1].toString()
            if (lastInp=="+"||lastInp=="-"||lastInp=="*"||lastInp=="/"||(lastInp=="%"&&operand=="M")||lastInp=="M")
                resultTextView.text = getString(R.string.invalid)
            else if ((operand=="M"&&lastCharacter(lastInp)==IS_NUMBER)||(operand!="M")){
                expressionTextView.text = String.format(getString(R.string.string_normal),expressionTextView.text,operand)//expressionTextView.text.toString()+operand
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
            expressionTextView.text= String.format(getString(R.string.string_par_open),expressionTextView.text)//expressionTextView.text.toString()+"("
            isDecimal=false
            parenthesisCount++
        } else if (parenthesisCount>0&&operationLength>0) {
            val lastInput=expressionTextView.text[operationLength-1].toString()
            when (lastCharacter(lastInput)) {
                IS_NUMBER->{
                    expressionTextView.text= String.format(getString(R.string.string_par_close),expressionTextView.text)//expressionTextView.text.toString()+")"
                    parenthesisCount--
                    isDecimal=false
                }
                IS_OPERAND->{
                    if (lastInput=="%") {
                        expressionTextView.text= String.format(getString(R.string.string_par_close),expressionTextView.text)
                    } else {
                        expressionTextView.text = String.format(
                            getString(R.string.string_par_open),
                            expressionTextView.text
                        )//expressionTextView.text.toString()+"("
                    }
                    parenthesisCount++
                    isDecimal=false
                }
                IS_PAR_OPEN->{
                    expressionTextView.text=String.format(getString(R.string.string_par_open),expressionTextView.text)//expressionTextView.text.toString()+"("
                    parenthesisCount++
                    isDecimal=false
                }
                IS_PAR_CLOSE->{
                    expressionTextView.text=String.format(getString(R.string.string_par_close),expressionTextView.text)//expressionTextView.text.toString()+")"
                    parenthesisCount--
                    isDecimal=false
                }
            }
        } else if (parenthesisCount==0&&operationLength>0) {
            val lastInput=expressionTextView.text[operationLength-1].toString()
            if (lastCharacter(lastInput)==IS_OPERAND) {
                expressionTextView.text=String.format(getString(R.string.string_par_open),expressionTextView.text)//expressionTextView.text.toString()+"("
                isDecimal=false
                parenthesisCount++
            } else {
                if (operationLength==1&&lastInput=="0") {
                    expressionTextView.text = "("
                } else
                expressionTextView.text = String.format(
                        getString(R.string.string_par_mult_open),
                        expressionTextView.text
                    )//expressionTextView.text.toString()+"*("

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
                expressionTextView.text=String.format(getString(R.string.zero_dot),expressionTextView.text)//expressionTextView.text.toString()+"0."
            }
            lastCharacter(expressionTextView.text[expressionTextView.text.lastIndex].toString())==IS_NUMBER -> {
                expressionTextView.text=String.format(getString(R.string.dot),expressionTextView.text)//expressionTextView.text.toString()+"."
            }
        }
        isDecimal=true
        scrollExpressionViewToRight()
    }

    private suspend fun solve(input: String) {
        var resultText: String
        val res: BigDecimal?
        try {
            Log.d("DB","$input:$endExpression:$isSolve")
            var temp=input
            //if (isSolve)
            temp=input//+endExpression
            //else saveLastExpression(input)
            var i = temp.lastIndex
            val sb=StringBuilder(temp)
            var innerPC=false
            var isModPresent=false
            while (i>0) {
                if (i!=0) {
                    if (sb[i]=='%') {
                        if (sb[i-1]==')') {
                            var j=i-1
                            while(sb[j]!='(') {
                                if (sb[j]=='%')
                                    innerPC=true
                                j-=1
                            }
                            if (sb[j-1]=='*'||(i<temp.lastIndex-1&&(sb[i+1]=='*'))) {
                                sb.insert(i+1, "/100")
                                sb.deleteCharAt(i)
                            } else if(sb[j-1]=='/'||(i<temp.lastIndex-1&&sb[i+1]=='/')) {
                                sb.insert(j,'(')
                                sb.insert(i+1,"/100)")
                                sb.deleteCharAt(i)
                            } else if (sb[j-1]=='+') {
                                sb.deleteCharAt(i)
                                sb.setCharAt(j-1,'<')
                            } else if (sb[j-1]=='-') {
                                sb.deleteCharAt(i)
                                sb.setCharAt(j-1,'>')
                            } else if (sb[j-1]=='M') {
                                resultTextView.text=getString(R.string.invalid)
                                return
                            }
                            i = if (innerPC)
                                i-2
                            else
                                j-2
                            innerPC=false
                        } else {
                            var j=i-1
                            while((sb[j] in '0'..'9')||sb[j]=='.') {
                                j-=1
                                if (j<0)
                                    break
                            }
                            if (j>0) {
                                if (sb[j]=='*'||(i<temp.lastIndex-1&&(sb[i+1]=='*'))) {
                                    sb.insert(i+1, "/100")
                                    sb.deleteCharAt(i)
                                } else if(sb[j]=='/'||(i<temp.lastIndex-1&&sb[i+1]=='/')) {
                                    sb.deleteCharAt(i)
                                    sb.insert(j+1,'(')
                                    sb.insert(i+1,"/100)")
                                } else if (sb[j] == '+') {
                                    sb.deleteCharAt(i)
                                    sb.setCharAt(j, '<')
                                } else if (sb[j] == '-') {
                                    sb.deleteCharAt(i)
                                    sb.setCharAt(j, '>')
                                } else if (sb[j] == 'M') {
                                    resultTextView.text = getString(R.string.invalid)
                                    return
                                }
                            } else {
                                sb.insert(i + 1, "/100")
                                sb.deleteCharAt(i)
                            }
                            i=j
                        }
                    } else if (sb[i]=='M') {
                        isModPresent=true
                        i-=1
                    }
                    else {
                        i-=1
                    }
                }
            }
            temp=sb.toString()
            if (isModPresent)
                temp=temp.replace("M","%")
            Log.d("EXP",temp)
            val e=Expression(temp).setPrecision(12)
            e.addOperator(object : AbstractOperator("<",Expression.OPERATOR_PRECEDENCE_ADDITIVE+1,true) {
                override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal {
                    if (v1!=null&&v2!=null) {
                        return v1.add(v2.divide(BigDecimal(100)).multiply(v1))
                    }
                    return BigDecimal.ZERO
                }
            })
            e.addOperator(object : AbstractOperator(">",Expression.OPERATOR_PRECEDENCE_ADDITIVE+1,true,false,false) {
                override fun eval(v1: BigDecimal?, v2: BigDecimal?): BigDecimal {
                    if (v1!=null&&v2!=null) {
                        return v1.subtract(v2.divide(BigDecimal(100)).multiply(v1))
                    }
                    return BigDecimal.ZERO
                }
            })
            res=e.eval(true)
            //Log.d("EXP","$temp:$res")
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
            Log.e("Calculator",e.message.toString())
            runOnUiThread {
                resultTextView.text = getString(R.string.invalid)
            }
            return
        }
        if (resultText==".") {
            resultText=resultText.replace("\\.?0*$","")
        }
        runOnUiThread {
            resultTextView.text = resultText
        }
            try {
                if (recordsDB.countAll() == 20) {
                    val tmpR = recordsDB.getAll()[0]
                    recordsDB.delete(tmpR)
                }
                recordsDB.insert(
                    Record(
                        exprName = expressionTextView.text.toString(),
                        exp = resultText
                    )
                )
            } catch (e: Exception) {
                Log.e("Calculator",e.message.toString())
            }
        lastExpCache = input
        lastSolCache=resultText

    }

    private fun lastCharacter(last: String): Int {
        try {
            Integer.parseInt(last)
            return IS_NUMBER
        } catch (e: NumberFormatException) {}
        return when (last) {
            "+", "-", "*", "/","%","M" -> IS_OPERAND
            "(" -> IS_PAR_OPEN
            ")" -> IS_PAR_CLOSE
            "." -> IS_DOT
            else -> IS_EXCEPTION
        }
    }

    //Based on eloyzone_calculator
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("expression",expressionTextView.text.toString())
        outState.putString("result",resultTextView.text.toString())
    }



}