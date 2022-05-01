package com.tzapps.calculator.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

object ConverterUtils {

    private val THIRTY_TWO = BigDecimal(32)
    private val FIVE_BY_NINE = BigDecimal(0.5556)
    private val KELVIN_CONSTANT = BigDecimal(273.15)
    private val ACRE_TO_MSQ = BigDecimal(4046.86)
    private val H_TO_MSQ = BigDecimal(10000)
    private val SQFT_TO_SQM = BigDecimal(10.764)
    private val SQM_TO_SQIN = BigDecimal(1550)
    private val HUNDRED = BigDecimal(100)
    private val THOUSAND = BigDecimal(1000)
    private val M_TO_IN = BigDecimal(39.37)
    private val M_TO_FT = BigDecimal(3.281)
    private val M_TO_YD = BigDecimal(1.094)
    private val M_TO_MI = BigDecimal(1609)
    private val M_TO_NM = BigDecimal(1852)
    private val GALUS_TO_L = BigDecimal(3.785)
    private val GALUK_TO_L = BigDecimal(4.546)
    private val KG_TO_POUND = BigDecimal(2.205)
    private val KG_TO_OZ = BigDecimal(35.274)
    private val DISPLAY_CONST = BigDecimal(99999999)

    fun format(a: BigDecimal): String {
        return if (a.compareTo(DISPLAY_CONST)==1)
            DecimalFormat("0.0E0").apply {
                roundingMode = RoundingMode.DOWN
                maximumFractionDigits = 5
            }.format(a.toDouble())
        else
            a.toPlainString()
    }

    fun FtoC(a: BigDecimal) = (BigDecimal(a.minus(THIRTY_TWO).multiply(FIVE_BY_NINE).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun CToF(a: BigDecimal) = (BigDecimal(a.multiply(BigDecimal.ONE.divide(FIVE_BY_NINE,4,RoundingMode.HALF_EVEN)).plus(THIRTY_TWO).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun CToK(a: BigDecimal) = (BigDecimal(a.add(KELVIN_CONSTANT).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun KToC(a: BigDecimal) = (BigDecimal(a.minus(KELVIN_CONSTANT).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun AcresToSqm(a: BigDecimal) = (BigDecimal(a.multiply(ACRE_TO_MSQ).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqmToAcres(a: BigDecimal) = (BigDecimal(a.divide(ACRE_TO_MSQ,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun HToSqm(a: BigDecimal) = (BigDecimal(a.multiply(H_TO_MSQ).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqmToH(a: BigDecimal) = (BigDecimal(a.divide(H_TO_MSQ,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqmToSqcm(a: BigDecimal) = (BigDecimal(a.multiply(H_TO_MSQ).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqcmToSqm(a: BigDecimal) = (BigDecimal(a.divide(H_TO_MSQ,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqftToSqm(a: BigDecimal) = (BigDecimal(a.divide(SQFT_TO_SQM,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqmToSqft(a: BigDecimal) = (BigDecimal(a.multiply(SQFT_TO_SQM).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqmToSqi(a: BigDecimal) = (BigDecimal(a.multiply(SQM_TO_SQIN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun SqiToSqm(a: BigDecimal) = (BigDecimal(a.divide(SQFT_TO_SQM,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToMM(a: BigDecimal) = (BigDecimal(a.multiply(THOUSAND).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MMToM(a: BigDecimal) = (BigDecimal(a.divide(THOUSAND,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToCM(a: BigDecimal) = (BigDecimal(a.multiply(HUNDRED).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun CMToM(a: BigDecimal) = (BigDecimal(a.divide(HUNDRED,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToKM(a: BigDecimal) = (BigDecimal(a.divide(THOUSAND,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun KMToM(a: BigDecimal) = (BigDecimal(a.multiply(THOUSAND).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToIn(a: BigDecimal) = (BigDecimal(a.multiply(M_TO_IN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun InToM(a: BigDecimal) = (BigDecimal(a.divide(M_TO_IN,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToFt(a: BigDecimal) = (BigDecimal(a.multiply(M_TO_FT).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun FtToM(a: BigDecimal) = (BigDecimal(a.divide(M_TO_FT,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToYd(a: BigDecimal) = (BigDecimal(a.multiply(M_TO_YD).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun YdToM(a: BigDecimal) = (BigDecimal(a.divide(M_TO_YD,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToMi(a: BigDecimal) = (BigDecimal(a.divide(M_TO_MI,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MiToM(a: BigDecimal) = (BigDecimal(a.multiply(M_TO_MI).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MToNM(a: BigDecimal) = (BigDecimal(a.divide(M_TO_NM,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun NMToM(a: BigDecimal) = (BigDecimal(a.multiply(M_TO_NM).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun LToMl(a: BigDecimal) = (BigDecimal(a.multiply(THOUSAND).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun MLToL(a: BigDecimal) = (BigDecimal(a.divide(THOUSAND,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun GUSToL(a: BigDecimal) = (BigDecimal(a.multiply(GALUS_TO_L).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun LToGUS(a: BigDecimal) = (BigDecimal(a.divide(GALUS_TO_L,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun GUKToL(a: BigDecimal) = (BigDecimal(a.multiply(GALUK_TO_L).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun LToGUK(a: BigDecimal) = (BigDecimal(a.divide(GALUK_TO_L,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun KGToG(a: BigDecimal) = (BigDecimal(a.multiply(THOUSAND).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun GToKG(a: BigDecimal) = (BigDecimal(a.divide(THOUSAND,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun KGToT(a: BigDecimal) = (BigDecimal(a.divide(THOUSAND,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun TToKG(a: BigDecimal) = (BigDecimal(a.multiply(THOUSAND).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun KGToP(a: BigDecimal) = (BigDecimal(a.multiply(KG_TO_POUND).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun PToKG(a: BigDecimal) = (BigDecimal(a.divide(KG_TO_POUND,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun KGToOZ(a: BigDecimal) = (BigDecimal(a.multiply(KG_TO_OZ).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

    fun OZToKG(a: BigDecimal) = (BigDecimal(a.divide(KG_TO_OZ,4,RoundingMode.HALF_EVEN).setScale(8,RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()))

}