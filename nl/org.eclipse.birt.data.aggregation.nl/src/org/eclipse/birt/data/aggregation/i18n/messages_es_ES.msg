#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

#Aggregation function UI information
Constants.Expression=&Expresión

TotalAve.description=función Total.AVE()

TotalAve.displayName=AVE

TotalCount.description=función Total.COUNT()

TotalCount.displayName=COUNT

TotalCountDistinct.description=función Total.COUNTDISTINCT()

TotalCountDistinct.displayName=COUNTDISTINCT

TotalFirst.description=función Total.FIRST()

TotalFirst.displayName=FIRST

TotalIrr.description=función Total.IRR()

TotalIrr.displayName=IRR

TotalIrr.param.intrate=Índice &interno

TotalLast.description=función Total.LAST()

TotalLast.displayName=LAST

TotalMax.description=función Total.MAX()

TotalMax.displayName=MAX

TotalMax.exception.cannot_get_max_value=no se puede obtener valor máximo\!

TotalMedian.description=función Total.median()

TotalMedian.displayName=MEDIAN

TotalMin.description=función Total.MIN()

TotalMin.displayName=MIN

TotalMin.exception.cannot_get_max_value=no se puede obtener valor máximo\!

TotalMirr.description=función Total.MIRR()

TotalMirr.displayName=MIRR

TotalMirr.param.finance_rate=Índice de f&inanzas

TotalMirr.param.reinvestment_rate=Índice de re&inversión

TotalMode.description=función Total.mode()

TotalMode.displayName=MODE

TotalMovingAve.description=función Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=Ve&ntana

TotalNpv.description=función Total.NPV()

TotalNpv.displayName=NPV

TotalNpv.param.rate=&Índice

TotalRunningCount.description=función Total.RUNNINGCOUNT()

TotalRunningCount.displayName=RUNNINGCOUNT

TotalRunningNpv.description=función Total.RUNNINGNPV()

TotalRunningNpv.displayName=RUNNINGNPV

TotalRunningNpv.param.rate=&Índice

TotalRunningSum.description=función Total.RUNNINGSUM()

TotalRunningSum.displayName=RUNNINGSUM

TotalStdDev.description=función Total.STDDEV()

TotalStdDev.displayName=STDDEV

TotalSum.description=función Total.sum()

TotalSum.displayName=SUM

TotalVariance.description=función Total.variance()

TotalVariance.displayName=VARIANCE

TotalWeightedAve.description=función Total.WEIGHTEDAVE()

TotalWeightedAve.displayName=WEIGHTEDAVE

TotalWeightedAve.param.weight=&Grosor

BaseTopBottomAggregation.param.n=&N

TotalIsBottomN.description=función Total.IsBottomN()

TotalIsBottomN.displayName=IS-BOTTOM-N

TotalIsBottomNPercent.description=función Total.IsBottomNPcent()

TotalIsBottomNPercent.displayName=IS-BOTTOM-N-PERCENT

TotalIsTopN.description=función Total.isTopN()

TotalIsTopN.displayName=IS-TOP-N

TotalIsTopNPercent.description=función Total.isTopNPercent

TotalIsTopNPercent.displayName=IS-TOP-N-PERCENT

TotalPercentile.description=función Total.PERCENTILE()

TotalPercentile.displayName=PERCENTILE

TotalPercentile.param.percentage=&Porcentaje

TotalPercentRank.description=función Total.PERCENTRANK()

TotalPercentRank.displayName=PERCENTRANK

TotalPercentSum.description=función Total.PERCENTSUM()

TotalPercentSum.displayName=PERCENTSUM

TotalQuartile.description=función Total.QUARTILE()

TotalQuartile.displayName=QUARTILE

TotalQuartile.param.quart=&cuartil

TotalRank.description=función Total.RANK()

TotalRank.displayName=RANK

TotalRank.param.ascending=&Ascendente

TotalConcatenate.description=funtion Total.CONCATENATE()

TotalConcatenate.displayName=CONCATENATE

TotalConcatenate.param.separator=Separad&or

TotalConcatenate.param.maxLength=Longitud má&x.

TotalConcatenate.param.showAllValues=Mostrar todos los &valores

TotalConcatenate.paramDescription.expression=campo de datos debe agregarse

TotalConcatenate.paramDescription.separator=separador del string concatenado

TotalConcatenate.paramDescription.maxLength=número máx. de caracteres del valor concatenado

TotalConcatenate.paramDescription.showAllValues=si deben mostrarse todos los valores

#aggregation error messages
aggregation.BadOperandType=Tipo de datos de operando inesperado: {0}.

aggregation.DataConversionError=Se ha producido un error en la conversión del tipo de datos.

aggregation.InvalidPercentileColumn=No todos los valores de percentil pueden ser nulos.

aggregation.BadParameterCount=Se ha proporcionado un número incorrecto de parámetros.

aggregation.BadParameterType=Uno de los parámetros tiene un tipo de datos incorrecto.

aggregation.NoIrrSolution=No se puede encontrar una solución para la función Irr con los datos proporcionados.

aggregation.InvalidTopBottomArgument=El argumento de los filtros Top/Bottom debe ser un número.

aggregation.InvalidTopBottomNArgument=El argumento de los filtros TopN/BottomN debe ser cero o un entero positivo.

aggregation.InvalidTopBottomPercentArgument=El argumento de los filtros TopPercent/BottomPercent debe ser ambos positivos e inferiores o iguales a 100.

aggregation.InvalidTopBottomN=El número total de filas procesadas debe ser inferior a {0}.

aggregation.InvalidPercentileArgument=La función Total.Percentile sólo acepta números entre el 0 y 1 (incluidos) como segundo argumento.

aggregation.InvalidQuartileArgument=La función Total.Quartile sólo acepta enteros entre 0 y 4 (incluidos) como segundo argumento.

aggregation.BadAggrFuncParam=Parámetro no válido para la función de agregación: {0}.

aggregation.ResetRateGuess=El usuario debe restablecer un nuevo rate guess.

aggregation.InvalidParameterType=Tipo de parámetro de la agregación no válido: {0}

aggregation.InvalidParameterValue=El valor del parámetro "{0}" en una agregación "{1}" no es válido: {2}.

aggregation.InvalidSeparator=Separador no válido para la agregación: {0}
