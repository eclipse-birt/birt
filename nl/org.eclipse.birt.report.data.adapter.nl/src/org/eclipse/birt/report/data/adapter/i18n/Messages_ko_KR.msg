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

datasource.cassandra.error=Cassandra 스크립트 데이터 원본을 지원하는 Hector 클라이언트 라이브러리 jar를 찾을 수 없습니다.

datasethandle.null.error=DataSetHandle은 널(null)이 될 수 없습니다.

datasethandle.invalidColumnIndex=잘못된 열 인덱스입니다.

adapter.invalid.datetimetype=날짜/시간 형식 "{0}"이(가) 잘못되었습니다.

adapter.invalid.aggregationfunctionname=집계 함수 {0}이(가) 잘못되었습니다.

invalid.datetime.value=날짜/시간 값 "{0}"이(가) 잘못되었습니다.

missing.join.condition=기본 데이터 집합 "{0}"과 큐브 "{2}"의 차원 "{1}" 간의 조인 조건이 없습니다.

exception.error=처리 도중 예외가 발생했습니다. 자세한 내용은 다음 메시지를 참조하십시오.

invalid.level.expression=수준 이름 "{0}"을(를) 찾을 수 없습니다.

invalid.measure.expression=측정값 "{0}"의 측정값 식은 널(null)일 수 없습니다.

invalid.data.type=잘못된 데이터 형식: {0}.

invalid.binding.expression=식 "{0}"이(가) 잘못되었습니다.

invalid.computedColumn.expression="{0}"은(는) 잘못된 데이터 집합 열 "{1}"을(를) 참조하는 잘못된 계산 열입니다.

cube.error.createhierarchy=계층 생성 도중 {0} 오류가 발생했습니다.

cube.error.createdimension=차원 생성 도중 {0} 오류가 발생했습니다.

cube.error.createmeasure=측정값 생성 도중 오류가 발생했습니다.

cube.error.datasetmiss=관련된 데이터 집합이 없어 큐브를 생성할 수 없습니다.

cube.jointColumnNotInLevels=조인 열 "{0}"이(가) 차원 "{1}"에 있는 수준의 키 필드로 정의되지 않았습니다.

cube.unsupportedAggregationInBinding=바인딩 "{0}"의 TopN/BottomN 함수는 xTab에서 지원되지 않습니다.

cube.error.aggregationFilter.referCalculatedMeasure=잘못된 집계: "{0}". 필터 식이 계산된 측정값 "{1}"을(를) 참조하였습니다.

cube.query.error.dimensionMiss=큐브에 차원 "{0}"이(가) 없습니다.

cube.query.error.levelMiss=큐브 시간 차원 "{1}"에 시간 유형 "{0}"의 수준이 없습니다.

cube.edge.error.levelMiss=시간 유형 "{0}"의 수준이 쿼리 정의에 누락되었습니다.

cube.derivedMeasure.invalid.reference=잘못된 측정값 참조입니다. 다음의 잘못된 측정값을 참조한 파생 측정값 "{0}": "{1}".

cube.derivedMeasure.resolve.error=파생 측정값 "{0}" 확인에 실패했습니다.

cube.derivedMeasure.recursive.ref=파생 측정값 "{0}" 확인에 실패했습니다. 다음 중에서 순환 참조가 감지되었기 때문입니다.: "{1}".

timeFunction.CURRENT_QUARTER.name = 현재 분기

timeFunction.CURRENT_QUARTER.description = 현재 분기에 특정된 메트릭 계산

timeFunction.CURRENT_MONTH.name = 현재 달

timeFunction.CURRENT_MONTH.description = 현재 달에 특정된 메트릭 계산

timeFunction.PREVIOUS_MONTH.name = 이전 N달

timeFunction.PREVIOUS_MONTH.description = 이전 N 달에 특정된 메트릭 계산

timeFunction.PREVIOUS_WEEK_TO_DATE.name = 이전 N주간 누계

timeFunction.PREVIOUS_WEEK_TO_DATE.description = 이전 N주 주간 누계에 특정된 메트릭 계산

timeFunction.PREVIOUS_MONTH_TO_DATE.name = 이전 N달간 누계

timeFunction.PREVIOUS_MONTH_TO_DATE.description = 이전 N달 월별 누계에 특정된 메트릭 계산

timeFunction.PREVIOUS_QUARTER.name = 이전 N분기

timeFunction.PREVIOUS_QUARTER.description = 이전 N 분기에 특정된 메트릭 계산

timeFunction.PREVIOUS_QUARTER_TO_DATE.name = 이전 N분기부터 누계

timeFunction.PREVIOUS_QUARTER_TO_DATE.description = 이전 N 분기에 특정된 메트릭 계산

timeFunction.PREVIOUS_PREVIOUS_YEAR.name = 이전 N년

timeFunction.PREVIOUS_PREVIOUS_YEAR.description = 이전 N 연도에 특정된 메트릭 계산

timeFunction.TRAILING_30_DAYS.name = 나머지 30일

timeFunction.TRAILING_30_DAYS.description = 최근 30일에 특정된 메트릭 계산

timeFunction.TRAILING_60_DAYS.name = 나머지 60일

timeFunction.TRAILING_60_DAYS.description = 최근 60일에 특정된 메트릭 계산

timeFunction.TRAILING_90_DAYS.name = 나머지 90일

timeFunction.TRAILING_120_DAYS.name = 나머지 120일

timeFunction.TRAILING_90_DAYS.description = 최근 90일에 특정된 메트릭 계산

timeFunction.TRAILING_120_DAYS.description = 최근 120일에 지정된 메트릭 계산

timeFunction.TRAILING_12_MONTHS.name = 나머지 12달

timeFunction.TRAILING_12_MONTHS.description = 최근 12달에 특정된 메트릭 계산

timeFunction.YEAR_TO_DATE.name = YTD(연간 누계)

timeFunction.YEAR_TO_DATE.description = 동일 연도에 특정된 메트릭 계산

timeFunction.QUARTER_TO_DATE.name = QTD(분기별 누계)

timeFunction.QUARTER_TO_DATE.description = 동일 분기에 특정된 메트릭 계산

timeFunction.MONTH_TO_DATE.name = MTD(월별 누계)

timeFunction.MONTH_TO_DATE.description = 동일 달에 특정된 메트릭 계산

timeFunction.PREVIOUS_YEAR_TO_DATE.name = 이전 N년간 누계

timeFunction.PREVIOUS_YEAR_TO_DATE.description = 이전 N연도에서 동일 연도에 특정된 메트릭 계산

timeFunction.WEEK_TO_DATE_LAST_YEAR.name = 전년도 주간 누계

timeFunction.WEEK_TO_DATE_LAST_YEAR.description = 이전 N연도에서 동일 주에 지정된 메트릭 계산

timeFunction.MONTH_TO_DATE_LAST_YEAR.name = 전년도 월간 누계

timeFunction.MONTH_TO_DATE_LAST_YEAR.description = 이전 N연도에서 동일 달에 특정된 메트릭 계산

timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = 전년도 분기별 누계

timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = 이전 N연도에서 동일 분기에 특정된 메트릭 계산

timeFunction.CURRENT_YEAR.name = 현재 연도

timeFunction.CURRENT_YEAR.description = 현재 연도에 특정된 메트릭 계산

timeFunction.WEEK_TO_DATE.name = WTD(주간 누계)

timeFunction.WEEK_TO_DATE.description = 동일 주에 특정된 메트릭 계산

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = N 기간부터 현재 기간

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = N 기간부터 현재 기간에 특정된 메트릭 계산

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = N 기간부터 현재 기간

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = N 기간부터 동일 기간에 특정된 메트릭 계산

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N 기간부터 나머지 N 기간

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = N 기간부터 나머지 기간에 특정된 메트릭 계산

timeFunction.TRAILING_N_MONTHS.name = 이후 N달

timeFunction.TRAILING_N_MONTHS.description = 최근 N달에 특정된 메트릭 계산

timeFunction.TRAILING_N_DAYS.name = 이후 N일

timeFunction.TRAILING_N_DAYS.description = 최근 N일에 지정된 메트릭 계산

timeFunction.NEXT_N_PERIODS.name = 이후 N 기간

timeFunction.NEXT_N_PERIODS.description = 이후 N 기간에 특정된 메트릭 계산

timeFunction.PERIOD1 = 시간 함수에 대한 최초 기간 인수

timeFunction.PERIOD2 = 시간 함수에 대한 이차 기간 인수

timeFunction.N1 = 일차 기간에 대한 N값

timeFunction.N2 = 이차 기간에 대한 N값

timeFunction.PERIOD1.displayName = 첫 번째 기간(&F)

timeFunction.PERIOD2.displayName = 두 번째 기간(&R)

timeFunction.N1.displayName = 지난 기간 수

timeFunction.N2.displayName = 시작(&O)

timeFunction.N1.next.displayName = 다음(&N)

timeFunction.N2.Ago.displayName=이전(&A)

timeFunction.N1.current.displayName=현재(&U)

timeFunction.N1.period.displayName= 기간(&P)

timeFunction.trailing.N1.period.displayName=후행(&I)

timeFunction.trailing.N1.Month.displayName= 후행 개월 수(&N)

timeFunction.N1.Year.displayName = 지난 연도 수(&N)

timeFunction.N2.Year.displayName = 지난 연도 수(&N)

timeFunction.N1.Year = 지난 연도에 대한 N 값

timeFunction.N2.Year = 지난 연도에 대한 N 값

timeFunction.N1.Month.displayName = 지난 개월 수(&N)

timeFunction.N2.Month.displayName = 지난 개월 수(&N)

timeFunction.N1.Month = 지난 개월에 대한 N 값

timeFunction.N2.Month = 지난 개월에 대한 N 값

timeFunction.N1.Quarter.displayName = 지난 분기 수(&N)

timeFunction.N2.Quarter.displayName = 지난 분기 수(&N)

timeFunction.N1.Quarter = 지난 분기에 대한 N 값

timeFunction.N2.Quarter = 지난 분기에 대한 N 값

timeFunction.N1.Week.displayName = 지난 주 수(&N)

timeFunction.N2.Week.displayName = 지난 주 수(&N)

timeFunction.N1.Week = 지난 주에 대한 N 값

timeFunction.N2.Week = 지난 주에 대한 N 값

timeFunction.N1.Day.displayName = 지난 일 수(&N)

timeFunction.PERIODCHOICE.YEAR_displayName =연도

timeFunction.PERIODCHOICE.QUARTER_displayName =분기

timeFunction.PERIODCHOICE.MONTH_displayName =월

timeFunction.PERIODCHOICE.WEEK_displayName =주

timeFunction.PERIODCHOICE.DAY_displayName =일

timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =연간 누계

timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =분기별 누계

timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =월별 누계

timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =주간 누계

timeFunction.toolTip.to=To

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

datasource.cassandra.error=Cassandra 스크립트 데이터 원본을 지원하는 Hector 클라이언트 라이브러리 jar를 찾을 수 없습니다.

datasethandle.null.error=DataSetHandle은 널(null)이 될 수 없습니다.

datasethandle.invalidColumnIndex=잘못된 열 인덱스입니다.

adapter.invalid.datetimetype=날짜/시간 형식 "{0}"이(가) 잘못되었습니다.

adapter.invalid.aggregationfunctionname=집계 함수 {0}이(가) 잘못되었습니다.

invalid.datetime.value=날짜/시간 값 "{0}"이(가) 잘못되었습니다.

missing.join.condition=기본 데이터 집합 "{0}"과 큐브 "{2}"의 차원 "{1}" 간의 조인 조건이 없습니다.

exception.error=처리 도중 예외가 발생했습니다. 자세한 내용은 다음 메시지를 참조하십시오.

invalid.level.expression=수준 이름 "{0}"을(를) 찾을 수 없습니다.

invalid.measure.expression=측정값 "{0}"의 측정값 식은 널(null)일 수 없습니다.

invalid.data.type=잘못된 데이터 형식: {0}.

invalid.binding.expression=식 "{0}"이(가) 잘못되었습니다.

cube.error.createhierarchy=계층 생성 도중 {0} 오류가 발생했습니다.

cube.error.createdimension=차원 생성 도중 {0} 오류가 발생했습니다.

cube.error.createmeasure=측정값 생성 도중 오류가 발생했습니다.

cube.error.datasetmiss=관련된 데이터 집합이 없어 큐브를 생성할 수 없습니다.

cube.jointColumnNotInLevels=조인 열 "{0}"이(가) 차원 "{1}"에 있는 수준의 키 필드로 정의되지 않았습니다.

cube.unsupportedAggregationInBinding=바인딩 "{0}"의 TopN/BottomN 함수는 xTab에서 지원되지 않습니다.

cube.error.aggregationFilter.referCalculatedMeasure=잘못된 집계: "{0}". 필터 식이 계산된 측정값 "{1}"을(를) 참조하였습니다.

cube.query.error.dimensionMiss=큐브에 차원 "{0}"이(가) 없습니다.

cube.query.error.levelMiss=큐브 시간 차원 "{1}"에 시간 유형 "{0}"의 수준이 없습니다.

cube.edge.error.levelMiss=시간 유형 "{0}"의 수준이 쿼리 정의에 누락되었습니다.

cube.derivedMeasure.invalid.reference=잘못된 측정값 참조입니다. 다음의 잘못된 측정값을 참조한 파생 측정값 "{0}": "{1}".

cube.derivedMeasure.resolve.error=파생 측정값 "{0}" 확인에 실패했습니다.

cube.derivedMeasure.recursive.ref=파생 측정값 "{0}" 확인에 실패했습니다. 다음 중에서 순환 참조가 감지되었기 때문입니다.: "{1}".

timeFunction.CURRENT_QUARTER.name = 현재 분기

timeFunction.CURRENT_QUARTER.description = 현재 분기에 특정된 메트릭 계산

timeFunction.CURRENT_MONTH.name = 현재 달

timeFunction.CURRENT_MONTH.description = 현재 달에 특정된 메트릭 계산

timeFunction.PREVIOUS_MONTH.name = 이전 N달

timeFunction.PREVIOUS_MONTH.description = 이전 N 달에 특정된 메트릭 계산

timeFunction.PREVIOUS_WEEK_TO_DATE.name = 이전 N주간 누계

timeFunction.PREVIOUS_WEEK_TO_DATE.description = 이전 N주 주간 누계에 특정된 메트릭 계산

timeFunction.PREVIOUS_MONTH_TO_DATE.name = 이전 N달간 누계

timeFunction.PREVIOUS_MONTH_TO_DATE.description = 이전 N달 월별 누계에 특정된 메트릭 계산

timeFunction.PREVIOUS_QUARTER.name = 이전 N분기

timeFunction.PREVIOUS_QUARTER.description = 이전 N 분기에 특정된 메트릭 계산

timeFunction.PREVIOUS_QUARTER_TO_DATE.name = 이전 N분기부터 누계

timeFunction.PREVIOUS_QUARTER_TO_DATE.description = 이전 N 분기에 특정된 메트릭 계산

timeFunction.PREVIOUS_PREVIOUS_YEAR.name = 이전 N년

timeFunction.PREVIOUS_PREVIOUS_YEAR.description = 이전 N 연도에 특정된 메트릭 계산

timeFunction.TRAILING_30_DAYS.name = 나머지 30일

timeFunction.TRAILING_30_DAYS.description = 최근 30일에 특정된 메트릭 계산

timeFunction.TRAILING_60_DAYS.name = 나머지 60일

timeFunction.TRAILING_60_DAYS.description = 최근 60일에 특정된 메트릭 계산

timeFunction.TRAILING_90_DAYS.name = 나머지 90일

timeFunction.TRAILING_120_DAYS.name = 나머지 120일

timeFunction.TRAILING_90_DAYS.description = 최근 90일에 특정된 메트릭 계산

timeFunction.TRAILING_120_DAYS.description = 최근 120일에 지정된 메트릭 계산

timeFunction.TRAILING_12_MONTHS.name = 나머지 12달

timeFunction.TRAILING_12_MONTHS.description = 최근 12달에 특정된 메트릭 계산

timeFunction.YEAR_TO_DATE.name = YTD(연간 누계)

timeFunction.YEAR_TO_DATE.description = 동일 연도에 특정된 메트릭 계산

timeFunction.QUARTER_TO_DATE.name = QTD(분기별 누계)

timeFunction.QUARTER_TO_DATE.description = 동일 분기에 특정된 메트릭 계산

timeFunction.MONTH_TO_DATE.name = MTD(월별 누계)

timeFunction.MONTH_TO_DATE.description = 동일 달에 특정된 메트릭 계산

timeFunction.PREVIOUS_YEAR_TO_DATE.name = 이전 N년간 누계

timeFunction.PREVIOUS_YEAR_TO_DATE.description = 이전 N연도에서 동일 연도에 특정된 메트릭 계산

timeFunction.WEEK_TO_DATE_LAST_YEAR.name = 전년도 주간 누계

timeFunction.WEEK_TO_DATE_LAST_YEAR.description = 이전 N연도에서 동일 주에 지정된 메트릭 계산

timeFunction.MONTH_TO_DATE_LAST_YEAR.name = 전년도 월간 누계

timeFunction.MONTH_TO_DATE_LAST_YEAR.description = 이전 N연도에서 동일 달에 특정된 메트릭 계산

timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = 전년도 분기별 누계

timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = 이전 N연도에서 동일 분기에 특정된 메트릭 계산

timeFunction.CURRENT_YEAR.name = 현재 연도

timeFunction.CURRENT_YEAR.description = 현재 연도에 특정된 메트릭 계산

timeFunction.WEEK_TO_DATE.name = WTD(주간 누계)

timeFunction.WEEK_TO_DATE.description = 동일 주에 특정된 메트릭 계산

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = N 기간부터 현재 기간

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = N 기간부터 현재 기간에 특정된 메트릭 계산

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = N 기간부터 현재 기간

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = N 기간부터 동일 기간에 특정된 메트릭 계산

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N 기간부터 나머지 N 기간

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = N 기간부터 나머지 기간에 특정된 메트릭 계산

timeFunction.TRAILING_N_MONTHS.name = 이후 N달

timeFunction.TRAILING_N_MONTHS.description = 최근 N달에 특정된 메트릭 계산

timeFunction.TRAILING_N_DAYS.name = 이후 N일

timeFunction.TRAILING_N_DAYS.description = 최근 N일에 지정된 메트릭 계산

timeFunction.NEXT_N_PERIODS.name = 이후 N 기간

timeFunction.NEXT_N_PERIODS.description = 이후 N 기간에 특정된 메트릭 계산

timeFunction.PERIOD1 = 시간 함수에 대한 최초 기간 인수

timeFunction.PERIOD2 = 시간 함수에 대한 이차 기간 인수

timeFunction.N1 = 일차 기간에 대한 N값

timeFunction.N2 = 이차 기간에 대한 N값

timeFunction.PERIOD1.displayName = 첫 번째 기간(&F)

timeFunction.PERIOD2.displayName = 두 번째 기간(&R)

timeFunction.N1.displayName = 지난 기간 수

timeFunction.N2.displayName = 시작(&O)

timeFunction.N1.next.displayName = 다음(&N)

timeFunction.N2.Ago.displayName=이전(&A)

timeFunction.N1.current.displayName=현재(&U)

timeFunction.N1.period.displayName= 기간(&P)

timeFunction.trailing.N1.period.displayName=후행(&I)

timeFunction.trailing.N1.Month.displayName= 후행 개월 수(&N)

timeFunction.N1.Year.displayName = 지난 연도 수(&N)

timeFunction.N2.Year.displayName = 지난 연도 수(&N)

timeFunction.N1.Year = 지난 연도에 대한 N 값

timeFunction.N2.Year = 지난 연도에 대한 N 값

timeFunction.N1.Month.displayName = 지난 개월 수(&N)

timeFunction.N2.Month.displayName = 지난 개월 수(&N)

timeFunction.N1.Month = 지난 개월에 대한 N 값

timeFunction.N2.Month = 지난 개월에 대한 N 값

timeFunction.N1.Quarter.displayName = 지난 분기 수(&N)

timeFunction.N2.Quarter.displayName = 지난 분기 수(&N)

timeFunction.N1.Quarter = 지난 분기에 대한 N 값

timeFunction.N2.Quarter = 지난 분기에 대한 N 값

timeFunction.N1.Week.displayName = 지난 주 수(&N)

timeFunction.N2.Week.displayName = 지난 주 수(&N)

timeFunction.N1.Week = 지난 주에 대한 N 값

timeFunction.N2.Week = 지난 주에 대한 N 값

timeFunction.N1.Day.displayName = 지난 일 수(&N)

timeFunction.PERIODCHOICE.YEAR_displayName =연도

timeFunction.PERIODCHOICE.QUARTER_displayName =분기

timeFunction.PERIODCHOICE.MONTH_displayName =월

timeFunction.PERIODCHOICE.WEEK_displayName =주

timeFunction.PERIODCHOICE.DAY_displayName =일

timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =연간 누계

timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =분기별 누계

timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =월별 누계

timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =주간 누계

timeFunction.toolTip.to=To
RelativeTimePeriod.TimeLevelNotEnough=보고서에는 참조 날짜를 사용하여 이 상대 시간을 계산하는 데 필요한 시간 수준이 모두 포함되어 있지는 않습니다.
 
 
 
