# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={1} に {0} が見つかりません。

###########################################################

#load data engine factory exception
load.factory.error=データアダプタのファクトリを読み込めません。

context.null.error=データコンテキストは NULL にできません。
context.invalidDesigntimeMethod=ReportContext メソッド [{0}] は、実行時にしか使用できません。

adaptor.invalid.mode=データアダプタのモード値 {0} が無効です。
invalid.cache.option=データアダプタのオプション値 {0} が無効です。

datasource.null.error=データセット "{0}" のデータソースが見つかりませんでした。
datasource.exid.error=データソース定義 {0} に拡張 ID がありません。

datasethandle.null.error=DataSetHandle は NULL にできません。
datasethandle.invalidColumnIndex=列のインデックスが無効です。
adapter.invalid.datetimetype=日時型 "{0}" が無効です。
adapter.invalid.aggregationfunctionname=The aggregation function {0} is invalid.

invalid.datetime.value=日時の値 "{0}" が無効です。
missing.join.condition=キューブ "{2}" のプライマリデータセット "{0}" とディメンジョン "{1}" との間にジョイン条件がありません。
exception.error=処理中に例外が発生しました。詳しくは次のメッセージを参照してください :
invalid.level.expression=レベル名 "{0}" が見つかりません。
invalid.measure.expression=メジャー "{0}" の式は null にできません。
invalid.data.type=データ型が無効です : {0} 

cube.error.createhierarchy=階層 {0} の作成中にエラーが発生しました。
cube.error.createdimension=ディメンジョン {0} の作成中にエラーが発生しました。
cube.error.createmeasure=メジャー要素の作成中にエラーが発生しました。
cube.error.datasetmiss=関連するデータセットが見つからないため、キューブを作成できません。
cube.jointColumnNotInLevels=結合列 "{0}" はディメンション "{1}" のいずれのレベルのキーフィールドとしても定義されていません。
cube.unsupportedAggregationInBinding=バインド "{0}" の TopN と BottomN 関数は、xTab ではサポートされていません。

cube.query.error.dimensionMiss=ディメンション "{0}" がキューブに存在しません。
cube.query.error.levelMiss=時間の種類が "{0}" のレベルがキューブの時間のディメンション "{1}" に存在しません。
cube.edge.error.levelMiss=時間の種類が "{0}" のレベルがクエリ定義に含まれていません。

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
 
timeFunction.toolTip.to=To
