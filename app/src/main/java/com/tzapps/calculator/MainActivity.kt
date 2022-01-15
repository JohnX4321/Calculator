package com.tzapps.calculator

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikepenz.aboutlibraries.LibsBuilder
import com.udojava.evalex.Expression
import java.math.BigDecimal

//TODO custom parser
class MainActivity : AppCompatActivity() {

    private var parenthesisCount=0;
    private var isDecimal=false
    private var isSolve=false
    private var endExpression = ""

    private val IS_EXCEPTION=-1
    private val IS_NUMBER=0
    private val IS_OPERAND=1
    private val IS_PAR_OPEN=2
    private val IS_PAR_CLOSE=3
    private val IS_DOT=4

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
    private lateinit var moreBtn: AppCompatImageButton
    private lateinit var clearBtn: AppCompatImageButton
    private lateinit var pcBtn: AppCompatButton
    private lateinit var parenthesisBtn: AppCompatButton
    private lateinit var multiplyBtn: AppCompatButton
    private lateinit var divideBtn: AppCompatButton
    private lateinit var addBtn: AppCompatButton
    private lateinit var minusBtn: AppCompatButton
    private lateinit var dotBtn: AppCompatButton
    private lateinit var solveBtn: AppCompatButton
    private lateinit var plusMinusBtn: AppCompatButton

    private lateinit var expressionTextView: TextView
    private lateinit var resultTextView: TextView


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
        moreBtn=findViewById(R.id.btnMenu)
        clearBtn=findViewById(R.id.btnClear)
        pcBtn=findViewById(R.id.btnModulus)
        parenthesisBtn=findViewById(R.id.btnParenthesis)
        multiplyBtn=findViewById(R.id.btnMultiply)
        divideBtn=findViewById(R.id.btnDivide)
        addBtn=findViewById(R.id.btnAddition)
        minusBtn=findViewById(R.id.btnSubtract)
        dotBtn=findViewById(R.id.btnDot)
        solveBtn=findViewById(R.id.btnEquals)
        plusMinusBtn=findViewById(R.id.btnInvertNumber)

        btnZero.setOnClickListener {
            addNumber("0")
        }
        btnOne.setOnClickListener {
            addNumber("1")
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
        plusMinusBtn.setOnClickListener {
            if (expressionTextView.text.isNotEmpty()&&lastCharacter(expressionTextView.text.toString())==IS_OPERAND){
                parenthesisBtn.performClick()
                minusBtn.performClick()
            }
        }

        moreBtn.setOnClickListener {
            BottomSheetDialog(it.context).apply {
                setContentView(R.layout.layout_bottomsheet)
                findViewById<AppCompatButton>(R.id.aboutLibs)?.setOnClickListener {
                    LibsBuilder().start(context)
                    this.dismiss()
                }
            }.show()
        }

        clearBtn.setOnClickListener {
            expressionTextView.text=""
            resultTextView.text=""
            parenthesisCount=0
            isDecimal=false
            isSolve=false
        }
        solveBtn.setOnClickListener {
            if (!expressionTextView.text.toString().isNullOrEmpty())
                solve(expressionTextView.text.toString())
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
    }

    private fun addOperand(operand: String) {
       var operationLength = expressionTextView.text.length
        if (operationLength>0){
            val lastInp=expressionTextView.text[operationLength-1].toString()
            if (lastInp=="+"||lastInp=="-"||lastInp=="*"||lastInp=="/"||lastInp=="%")
                resultTextView.text="Invalid"
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
                expressionTextView.text=expressionTextView.text.toString()+"x("
                isDecimal=false
                parenthesisCount++
            }
        }
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
    }

    private fun solve(input: String) {
        var resultText=""
        var res: BigDecimal? = null
        try {
            var temp=input
            if (isSolve)
                temp=input+endExpression
            else saveLastExpression(input)
            Log.d("Input",temp)
            res=Expression(temp).setPrecision(12).eval(true)
            Log.d("Result",res.toString())
            isSolve=true
            resultText=res!!.toPlainString()//.setScale(8,BigDecimal.ROUND_HALF_UP).toPlainString()
        } catch (e: Exception) {
            e.printStackTrace()
            resultTextView.text="Invalid"
            return
        }
        if (resultText==".") {
            resultText=resultText.replace("\\.?0*$","")
        }
        resultTextView.text=resultText
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

}