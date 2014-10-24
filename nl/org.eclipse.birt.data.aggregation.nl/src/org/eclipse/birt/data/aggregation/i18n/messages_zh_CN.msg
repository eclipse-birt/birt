#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

#Aggregation function UI information
Constants.Expression=表达式(&E)

TotalAve.description=函数 Total.AVE()

TotalAve.displayName=AVE

TotalCount.description=函数 Total.COUNT()

TotalCount.displayName=COUNT

TotalCountDistinct.description=函数 Total.COUNTDISTINCT()

TotalCountDistinct.displayName=COUNTDISTINCT

TotalFirst.description=函数 Total.FIRST()

TotalFirst.displayName=FIRST

TotalIrr.description=函数 Total.IRR()

TotalIrr.displayName=IRR

TotalIrr.param.intrate=迭代(&I)

TotalLast.description=函数 Total.LAST()

TotalLast.displayName=LAST

TotalMax.description=函数 Total.MAX()

TotalMax.displayName=MAX

TotalMax.exception.cannot_get_max_value=无法获得最大值\!

TotalMedian.description=函数 Total.median()

TotalMedian.displayName=MEDIAN

TotalMin.description=函数 Total.MIN()

TotalMin.displayName=MIN

TotalMin.exception.cannot_get_max_value=无法获得最大值\!

TotalMirr.description=函数 Total.MIRR()

TotalMirr.displayName=MIRR

TotalMirr.param.finance_rate=财务比率(&R)

TotalMirr.param.reinvestment_rate=再投资率(&I)

TotalMode.description=函数 Total.mode()

TotalMode.displayName=MODE

TotalMovingAve.description=函数 Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=窗口(&W)

TotalNpv.description=函数 Total.NPV()

TotalNpv.displayName=NPV

TotalNpv.param.rate=比率(&R)

TotalRunningCount.description=函数 Total.RUNNINGCOUNT()

TotalRunningCount.displayName=RUNNINGCOUNT

TotalRunningNpv.description=函数 Total.RUNNINGNPV()

TotalRunningNpv.displayName=RUNNINGNPV

TotalRunningNpv.param.rate=比率(&R)

TotalRunningSum.description=函数 Total.RUNNINGSUM()

TotalRunningSum.displayName=RUNNINGSUM

TotalStdDev.description=函数 Total.STDDEV()

TotalStdDev.displayName=STDDEV

TotalSum.description=函数 Total.sum()

TotalSum.displayName=SUM

TotalVariance.description=函数 Total.variance()

TotalVariance.displayName=VARIANCE

TotalWeightedAve.description=函数 Total.WEIGHTEDAVE()

TotalWeightedAve.displayName=WEIGHTEDAVE

TotalWeightedAve.param.weight=加权(&W)

BaseTopBottomAggregation.param.n=N(&N)

TotalIsBottomN.description=函数 Total.IsBottomN()

TotalIsBottomN.displayName=IS-BOTTOM-N

TotalIsBottomNPercent.description=函数 Total.IsBottomNPcent()

TotalIsBottomNPercent.displayName=IS-BOTTOM-N-PERCENT

TotalIsTopN.description=函数 Total.isTopN()

TotalIsTopN.displayName=IS-TOP-N

TotalIsTopNPercent.description=函数 Total.isTopNPercent

TotalIsTopNPercent.displayName=IS-TOP-N-PERCENT

TotalPercentile.description=函数 Total.PERCENTILE()

TotalPercentile.displayName=PERCENTILE

TotalPercentile.param.percentage=百分比(&P)

TotalPercentRank.description=函数 Total.PERCENTRANK()

TotalPercentRank.displayName=PERCENTRANK

TotalPercentSum.description=函数 Total.PERCENTSUM()

TotalPercentSum.displayName=PERCENTSUM

TotalQuartile.description=函数 Total.QUARTILE()

TotalQuartile.displayName=QUARTILE

TotalQuartile.param.quart=四分位(&Q)

TotalRank.description=函数 Total.RANK()

TotalRank.displayName=RANK

TotalRank.param.ascending=升序(&A)

TotalConcatenate.description=函数 Total.CONCATENATE()

TotalConcatenate.displayName=CONCATENATE

TotalConcatenate.param.separator=分隔符(&O)

TotalConcatenate.param.maxLength=最大长度(&X)

TotalConcatenate.param.showAllValues=显示所有值(&W)

TotalConcatenate.paramDescription.expression=要据以聚合的数据字段

TotalConcatenate.paramDescription.separator=连接字符串的分隔符

TotalConcatenate.paramDescription.maxLength=连接值的最大字符数

TotalConcatenate.paramDescription.showAllValues=是否显示所有值

#aggregation error messages
aggregation.BadOperandType=意外操作数数据类型: {0}。

aggregation.DataConversionError=数据类型转换时出错。

aggregation.InvalidPercentileColumn=百分位数并非所有值均可为空。

aggregation.BadParameterCount=提供的参数个数不正确。

aggregation.BadParameterType=某个参数的数据类型不正确。

aggregation.NoIrrSolution=使用提供的数据无法找到 Irr 函数的解法。

aggregation.InvalidTopBottomArgument=Top/Bottom 筛选器参数必须为数字。

aggregation.InvalidTopBottomNArgument=TopN/BottomN 筛选器的参数必须为零或正整数。

aggregation.InvalidTopBottomPercentArgument=TopPercent/BottomPercent 筛选器的参数必须是正数且小于或等于 100。

aggregation.InvalidTopBottomN=处理的总行数不能超过 {0}。

aggregation.InvalidPercentileArgument=Total.Percentile 函数只接受 0 和 1 之间(包括 0 和 1)的数作为其第二个参数。

aggregation.InvalidQuartileArgument=Total.Quartile 函数只接受 0 和 4 之间(包括 0 和 4)的整数作为其第二个参数。

aggregation.BadAggrFuncParam=非法的聚合函数参数: {0}。

aggregation.ResetRateGuess=用户应重置新的比率猜测值。

aggregation.InvalidParameterType=聚合的无效参数类型: {0}

aggregation.InvalidParameterValue=“{1}”聚合中“{0}”参数的值无效: {2}。

aggregation.InvalidSeparator=聚合的无效分隔符: {0}
