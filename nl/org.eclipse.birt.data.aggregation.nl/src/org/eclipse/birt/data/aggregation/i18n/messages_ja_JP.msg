#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

#Aggregation function UI information
Constants.Expression=式(&E)

TotalAve.description=関数 - Total.AVE()

TotalAve.displayName=AVE

TotalCount.description=関数 - Total.COUNT()

TotalCount.displayName=COUNT

TotalCountDistinct.description=関数 - Total.COUNTDISTINCT()

TotalCountDistinct.displayName=COUNTDISTINCT

TotalFirst.description=関数 - Total.FIRST()

TotalFirst.displayName=FIRST

TotalIrr.description=関数 - Total.IRR()

TotalIrr.displayName=IRR

TotalIrr.param.intrate=内部利益率(&I)

TotalLast.description=関数 - Total.LAST()

TotalLast.displayName=LAST

TotalMax.description=関数 - Total.MAX()

TotalMax.displayName=MAX

TotalMax.exception.cannot_get_max_value=最大値を取得できません。

TotalMedian.description=関数 - Total.median()

TotalMedian.displayName=MEDIAN

TotalMin.description=関数 - Total.MIN()

TotalMin.displayName=MIN

TotalMin.exception.cannot_get_max_value=最大値を取得できません。

TotalMirr.description=関数 - Total.MIRR()

TotalMirr.displayName=MIRR

TotalMirr.param.finance_rate=投資原価(&R)

TotalMirr.param.reinvestment_rate=再投資受取利率(&I)

TotalMode.description=関数 - Total.mode()

TotalMode.displayName=MODE

TotalMovingAve.description=関数 - Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=ウィンドウ(&W)

TotalNpv.description=関数 - Total.NPV()

TotalNpv.displayName=NPV

TotalNpv.param.rate=割引率(&R)

TotalRunningCount.description=関数 - Total.RUNNINGCOUNT()

TotalRunningCount.displayName=RUNNINGCOUNT

TotalRunningNpv.description=関数 - Total.RUNNINGNPV()

TotalRunningNpv.displayName=RUNNINGNPV

TotalRunningNpv.param.rate=割引率(&R)

TotalRunningSum.description=関数 - Total.RUNNINGSUM()

TotalRunningSum.displayName=RUNNINGSUM

TotalStdDev.description=関数 - Total.STDDEV()

TotalStdDev.displayName=STDDEV

TotalSum.description=関数 - Total.sum()

TotalSum.displayName=SUM

TotalVariance.description=関数 - Total.variance()

TotalVariance.displayName=VARIANCE

TotalWeightedAve.description=関数 - Total.WEIGHTEDAVE()

TotalWeightedAve.displayName=WEIGHTEDAVE

TotalWeightedAve.param.weight=重み(&W)

BaseTopBottomAggregation.param.n=N 位(&N)

TotalIsBottomN.description=関数 - Total.IsBottomN()

TotalIsBottomN.displayName=IS-BOTTOM-N

TotalIsBottomNPercent.description=関数 - Total.IsBottomNPcent()

TotalIsBottomNPercent.displayName=IS-BOTTOM-N-PERCENT

TotalIsTopN.description=関数 - Total.isTopN()

TotalIsTopN.displayName=IS-TOP-N

TotalIsTopNPercent.description=関数 - Total.isTopNPercent

TotalIsTopNPercent.displayName=IS-TOP-N-PERCENT

TotalPercentile.description=関数 - Total.PERCENTILE()

TotalPercentile.displayName=PERCENTILE

TotalPercentile.param.percentage=割合 (%)(&P)

TotalPercentRank.description=関数 - Total.PERCENTRANK()

TotalPercentRank.displayName=PERCENTRANK

TotalPercentSum.description=関数 - Total.PERCENTSUM()

TotalPercentSum.displayName=PERCENTSUM

TotalQuartile.description=関数 - Total.QUARTILE()

TotalQuartile.displayName=QUARTILE

TotalQuartile.param.quart=四分位(&Q)

TotalRank.description=関数 - Total.RANK()

TotalRank.displayName=RANK

TotalRank.param.ascending=昇順(&A)

TotalConcatenate.description=funtion Total.CONCATENATE()

TotalConcatenate.displayName=CONCATENATE

TotalConcatenate.param.separator=区切り文字(&O)

TotalConcatenate.param.maxLength=最大長(&X)

TotalConcatenate.param.showAllValues=すべての値を表示(&W)

TotalConcatenate.paramDescription.expression=集計するデータフィールド

TotalConcatenate.paramDescription.separator=結合した文字列の区切り文字

TotalConcatenate.paramDescription.maxLength=結合した値の最大文字数

TotalConcatenate.paramDescription.showAllValues=すべての値を表示

#aggregation error messages
aggregation.BadOperandType=このデータ型の演算子は使用できません : {0}

aggregation.DataConversionError=データ型を変換中にエラーが発生しました。

aggregation.InvalidPercentileColumn=百分位数はすべてを NULL にすることができません。

aggregation.BadParameterCount=指定されたパラメタの数が正しくありません。

aggregation.BadParameterType=1 つのパラメタのデータ型が正しくありません。

aggregation.NoIrrSolution=指定されたデータでは、Irr 関数の解が見つかりません。

aggregation.InvalidTopBottomArgument=Top と Bottom のフィルタの引数には、数値を指定してください。

aggregation.InvalidTopBottomNArgument=TopN と BottomN のフィルタの引数は、ゼロまたは正の整数である必要があります。

aggregation.InvalidTopBottomPercentArgument=TopPercent と BottomPercent のフィルタの引数は、両方とも 100 以下の正数である必要があります。

aggregation.InvalidTopBottomN=同時に処理できるのは {0} 行までです。

aggregation.InvalidPercentileArgument=Total.Percentile 関数の 2 つ目の引数には、0 ～ 1 までの数値しか指定できません。

aggregation.InvalidQuartileArgument=Total.Quartile 関数の 2 つ目の引数には、0 ～ 4 までの整数しか指定できません。

aggregation.BadAggrFuncParam=集計関数のパラメタが無効です : {0}

aggregation.ResetRateGuess=新しい Rate Guess をリセットしてください。

aggregation.InvalidParameterType=集計パラメタの型が無効です : {0}

aggregation.InvalidParameterValue="{0}" パラメタの値 ("{1}" 集計) は無効です : {2}

aggregation.InvalidSeparator=集計の区切りが無効です : {0}
