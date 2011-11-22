# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0}이(가) {0}에 없습니다.

###########################################################

#load data engine factory exception
load.factory.error=데이터 어댑터 팩토리 로드 중 오류가 발생했습니다.

context.null.error=데이터 컨텍스트는 널(null)이 될 수 없습니다.
context.invalidDesigntimeMethod=ReportContext 메서드 [{0}]은(는) 런타임에서만 사용될 수 있습니다.

adaptor.invalid.mode=데이터 어댑터 컨텍스트의 모드 값 {0}이(가) 잘못되었습니다.
invalid.cache.option=데이터 어댑터 컨텍스트의 모드 값 {0}이(가) 잘못되었습니다.

datasource.null.error=데이터 집합 "{0}"의 데이터 원본을 찾지 못했습니다.
datasource.exid.error=데이터 원본 정의에서 확장 ID가 누락되었습니다: {0}.

datasethandle.null.error=DataSetHandle은 널(null)이 될 수 없습니다.
datasethandle.invalidColumnIndex=잘못된 열 인덱스입니다.
adapter.invalid.datetimetype=날짜/시간 형식 "{0}"이(가) 잘못되었습니다.
adapter.invalid.aggregationfunctionname=The aggregation function {0} is invalid.

invalid.datetime.value=날짜/시간 값 "{0}"이(가) 잘못되었습니다.
missing.join.condition=기본 데이터 집합 "{0}"과 큐브 "{2}"의 차원 "{1}" 간의 조인 조건이 없습니다.
exception.error=처리 도중 예외가 발생했습니다. 자세한 내용은 다음 메시지를 참조하십시오.
invalid.level.expression=수준 이름 "{0}"을(를) 찾을 수 없습니다.
invalid.measure.expression=측정값 "{0}"의 측정값 식은 널(null)일 수 없습니다.
invalid.data.type=잘못된 데이터 형식: {0}. 

cube.error.createhierarchy=계층 생성 도중 {0} 오류가 발생했습니다.
cube.error.createdimension=차원 생성 도중 {0} 오류가 발생했습니다.
cube.error.createmeasure=측정값 생성 도중 오류가 발생했습니다.
cube.error.datasetmiss=관련된 데이터 집합이 없어 큐브를 생성할 수 없습니다.
cube.jointColumnNotInLevels=조인 열 "{0}"이(가) 차원 "{1}"에 있는 수준의 키 필드로 정의되지 않았습니다.
cube.unsupportedAggregationInBinding=바인딩 "{0}"의 TopN/BottomN 함수는 xTab에서 지원되지 않습니다.

cube.query.error.dimensionMiss=Dimension "{0}" does not exist in cube.
cube.query.error.levelMiss=Level with time type "{0}" does not exist in cube time dimension "{1}".
cube.edge.error.levelMiss=Level with time type "{0}" is missing in query definition.

timeFunction.CURRENT_QUARTER.name = Current Quarter 
timeFunction.CURRENT_QUARTER.description = Calculate the specified metric for current quarter
timeFunction.CURRENT_MONTH.name = Current Month
timeFunction.CURRENT_MONTH.description = Calculate the specified metric for current month
timeFunction.PREVIOUS_MONTH.name = Previous Month
timeFunction.PREVIOUS_MONTH.description = Calculate the specified metric for previous N month
timeFunction.PREVIOUS_WEEK_TO_DATE.name = Previous Week to Date
timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calculate the specified metric for week to date of previous N month
timeFunction.PREVIOUS_MONTH_TO_DATE.name = Previous Month to Date
timeFunction.PREVIOUS_MONTH_TO_DATE.description = Calculate the specified metric for month to date of previous N month
timeFunction.PREVIOUS_QUARTER.name = Previous Quarter 
timeFunction.PREVIOUS_QUARTER.description = Calculate the specified metric for previous N quarter
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = Previous Quarter to Date 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Calculate the specified metric for previous N quarter
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = Previous Year 
timeFunction.PREVIOUS_PREVIOUS_YEAR.description = Calculate the specified metric for previous N year
timeFunction.TRAILING_30_DAYS.name = Trailing 30 Days 
timeFunction.TRAILING_30_DAYS.description = Calculate the specified metric for last 30 days
timeFunction.TRAILING_60_DAYS.name = Trailing 60 Days 
timeFunction.TRAILING_60_DAYS.description = Calculate the specified metric for last 60 days
timeFunction.TRAILING_90_DAYS.name = Trailing 90 Days 
timeFunction.TRAILING_90_DAYS.description = Calculate the specified metric for last 90 days
timeFunction.TRAILING_12_MONTHS.name = Trailing 12 Months 
timeFunction.TRAILING_12_MONTHS.description = Calculate the specified metric for last 12 months
timeFunction.YEAR_TO_DATE.name = Year to Date 
timeFunction.YEAR_TO_DATE.description = Calculate the specified metric for same year
timeFunction.QUARTER_TO_DATE.name = Quarter to Date 
timeFunction.QUARTER_TO_DATE.description = Calculate the specified metric for same quarter
timeFunction.MONTH_TO_DATE.name = Month to Date 
timeFunction.MONTH_TO_DATE.description = Calculate the specified metric for same month
timeFunction.PREVIOUS_YEAR_TO_DATE.name = previous Year to Date 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = Calculate the specified metric for same year in previous N year
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Month to Date Last Year 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Calculate the specified metric for same month in previous N year
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Quarter to Date Last Year 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Calculate the specified metric for same quarter in previous N year
timeFunction.CURRENT_YEAR.name = Current Year 
timeFunction.CURRENT_YEAR.description = Calculate the specified metric for current year
timeFunction.WEEK_TO_DATE.name = Week To Date 
timeFunction.WEEK_TO_DATE.description = Calculate the specified metric for same week
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Current Period from N Periods Ago 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Calculate the specified metric for for current period from N period ago
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Period to Date from N Periods Ago 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Calculate the specified metric for same period from N period ago
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = Trailing N Periods from N Periods Ago 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Calculate the specified metric for trailing N period from N period ago
timeFunction.NEXT_N_PERIODS.name = Next N Periods 
timeFunction.NEXT_N_PERIODS.description = Calculate the specified metric for next N periods

timeFunction.PERIOD1 = The first period argument for time function
timeFunction.PERIOD2 = The second period argument for time function
timeFunction.N1 = N value for first period
timeFunction.N2 = N value for second period

timeFunction.PERIOD1.displayName = The first period
timeFunction.PERIOD2.displayName = The second period
timeFunction.N1.displayName = The fist N
timeFunction.N2.displayName = The second N
 