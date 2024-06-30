#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

#Aggregation function UI information
Constants.Expression=식(&E)

TotalAve.description=함수 Total.AVE()

TotalAve.displayName=AVE

TotalCount.description=함수 Total.COUNT()

TotalCount.displayName=COUNT

TotalCountDistinct.description=함수 Total.COUNTDISTINCT()

TotalCountDistinct.displayName=COUNTDISTINCT

TotalFirst.description=함수 Total.FIRST()

TotalFirst.displayName=FIRST

TotalIrr.description=함수 Total.IRR()

TotalIrr.displayName=IRR

TotalIrr.param.intrate=이율(&I)

TotalLast.description=함수 Total.LAST()

TotalLast.displayName=LAST

TotalMax.description=함수 Total.MAX()

TotalMax.displayName=MAX

TotalMax.exception.cannot_get_max_value=최대값을 가져올 수 없습니다\!

TotalMedian.description=함수 Total.median()

TotalMedian.displayName=MEDIAN

TotalMin.description=함수 Total.MIN()

TotalMin.displayName=MIN

TotalMin.exception.cannot_get_max_value=최대값을 가져올 수 없습니다\!

TotalMirr.description=함수 Total.MIRR()

TotalMirr.displayName=MIRR

TotalMirr.param.finance_rate=금융 금리(&R)

TotalMirr.param.reinvestment_rate=재투자 금리(&I)

TotalMode.description=함수 Total.mode()

TotalMode.displayName=MODE

TotalMovingAve.description=함수 Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=창(&W)

TotalNpv.description=함수 Total.NPV()

TotalNpv.displayName=NPV

TotalNpv.param.rate=비율(&R)

TotalRunningCount.description=함수 Total.RUNNINGCOUNT()

TotalRunningCount.displayName=RUNNINGCOUNT

TotalRunningNpv.description=함수 Total.RUNNINGNPV()

TotalRunningNpv.displayName=RUNNINGNPV

TotalRunningNpv.param.rate=비율(&R)

TotalRunningSum.description=함수 Total.RUNNINGSUM()

TotalRunningSum.displayName=RUNNINGSUM

TotalStdDev.description=함수 Total.STDDEV()

TotalStdDev.displayName=STDDEV

TotalSum.description=함수 Total.sum()

TotalSum.displayName=SUM

TotalVariance.description=함수 Total.variance()

TotalVariance.displayName=VARIANCE

TotalWeightedAve.description=함수 Total.WEIGHTEDAVE()

TotalWeightedAve.displayName=WEIGHTEDAVE

TotalWeightedAve.param.weight=두께(&W)

BaseTopBottomAggregation.param.n=&N

TotalIsBottomN.description=함수 Total.IsBottomN()

TotalIsBottomN.displayName=IS-BOTTOM-N

TotalIsBottomNPercent.description=함수 Total.IsBottomNPcent()

TotalIsBottomNPercent.displayName=IS-BOTTOM-N-PERCENT

TotalIsTopN.description=함수 Total.isTopN()

TotalIsTopN.displayName=IS-TOP-N

TotalIsTopNPercent.description=함수 Total.isTopNPercent()

TotalIsTopNPercent.displayName=IS-TOP-N-PERCENT

TotalPercentile.description=함수 Total.PERCENTILE()

TotalPercentile.displayName=PERCENTILE

TotalPercentile.param.percentage=백분율(&P)

TotalPercentRank.description=함수 Total.PERCENTRANK()

TotalPercentRank.displayName=PERCENTRANK

TotalPercentSum.description=함수 Total.PERCENTSUM()

TotalPercentSum.displayName=PERCENTSUM

TotalQuartile.description=함수 Total.QUARTILE()

TotalQuartile.displayName=QUARTILE

TotalQuartile.param.quart=4분위수(&Q)

TotalRank.description=함수 Total.RANK()

TotalRank.displayName=RANK

TotalRank.param.ascending=오름차순(&A)

TotalConcatenate.description=Total.CONCATENATE() 함수

TotalConcatenate.displayName=CONCATENATE

TotalConcatenate.param.separator=구분 기호(&O)

TotalConcatenate.param.maxLength=최대 길이(&X)

TotalConcatenate.param.showAllValues=모든 값 표시(&W)

TotalConcatenate.paramDescription.expression=집계한 데이터 필드

TotalConcatenate.paramDescription.separator=연결된 문자열의 구분 기호

TotalConcatenate.paramDescription.maxLength=연결된 값의 최대 문자 수

TotalConcatenate.paramDescription.showAllValues=모든 값 표시 여부

#aggregation error messages
aggregation.BadOperandType=예기치 못한 피연산자 데이터 형식: {0}.

aggregation.DataConversionError=데이터 형식을 변환하는 중 오류가 발생했습니다.

aggregation.InvalidPercentileColumn=백분위수의 일부 값은 널(null)이 될 수 없습니다.

aggregation.BadParameterCount=잘못된 매개 변수 수가 제공되었습니다.

aggregation.BadParameterType=매개 변수 중 하나에 잘못된 데이터 형식이 있습니다.

aggregation.NoIrrSolution=제공된 데이터를 포함한 IRR 함수에 대한 솔루션을 찾을 수 없습니다.

aggregation.InvalidTopBottomArgument=Top/Bottom 필터의 인수는 숫자여야 합니다.

aggregation.InvalidTopBottomNArgument=TopN/BottomN 필터의 인수는 0 또는 양의 정수여야 합니다.

aggregation.InvalidTopBottomPercentArgument=TopPercent/BottomPercent 필터의 인수는 100보다 작거나 같은 양수여야 합니다.

aggregation.InvalidTopBottomN=처리되는 총 행수는 {0}을(를) 넘지 않아야 합니다.

aggregation.InvalidPercentileArgument=Total.Percentile 함수는 두 번째 인수로 0과 1사이의 숫자만 허용합니다.

aggregation.InvalidQuartileArgument=Total.Quartile 함수는 두 번째 인수로 0과 4사이의 정수만 허용합니다.

aggregation.BadAggrFuncParam=집계 함수에 대한 잘못된 매개 변수: {0}.

aggregation.ResetRateGuess=새로운 rate guess를 재설정해야 합니다.

aggregation.InvalidParameterType=잘못된 집계 매개 변수 유형: {0}

aggregation.InvalidParameterValue=집계 "{1}"에 있는 매개 변수 "{0}"의 값이 잘못되었습니다. {2}

aggregation.InvalidSeparator=잘못된 집계 구분 기호: {0}
