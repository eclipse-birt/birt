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
cube.error.aggregationFilter.referCalculatedMeasure=Invalid aggregation "{0}". The filter expression referenced a calculated measure "{1}".

cube.query.error.dimensionMiss=ディメンション "{0}" がキューブに存在しません。
cube.query.error.levelMiss=時間の種類が "{0}" のレベルがキューブの時間のディメンション "{1}" に存在しません。
cube.edge.error.levelMiss=時間の種類が "{0}" のレベルがクエリ定義に含まれていません。

cube.derivedMeasure.invalid.reference=Invalid measure reference. Derived measure "{0}" referenced an invalid measure: "{1}".
cube.derivedMeasure.resolve.error=Failed resolving derived measure "{0}".
cube.derivedMeasure.recursive.ref=Failed resolving derived measure "{0}", because circular reference is detected among: "{1}".

timeFunction.CURRENT_QUARTER.name = 当四半期 
timeFunction.CURRENT_QUARTER.description = 当四半期の指定されたメトリクスを計算する
timeFunction.CURRENT_MONTH.name = 当月
timeFunction.CURRENT_MONTH.description = 当月の指定されたメトリクスを計算する
timeFunction.PREVIOUS_MONTH.name = 前月
timeFunction.PREVIOUS_MONTH.description = 前の N 月の指定されたメトリクスを計算する
timeFunction.PREVIOUS_WEEK_TO_DATE.name = 前の週から現在まで
timeFunction.PREVIOUS_WEEK_TO_DATE.description = 前の N 月の日付までの 1 週間の指定されたメトリクスを計算する
timeFunction.PREVIOUS_MONTH_TO_DATE.name = 前月から現在まで
timeFunction.PREVIOUS_MONTH_TO_DATE.description = 前の N 月の現在までの 1 か月の指定されたメトリクスを計算する
timeFunction.PREVIOUS_QUARTER.name = 前四半期 
timeFunction.PREVIOUS_QUARTER.description = 前の N 四半期の指定されたメトリクスを計算する
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = 前四半期から現在まで 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = 前の N 四半期の指定されたメトリクスを計算する
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = 前年 
timeFunction.PREVIOUS_PREVIOUS_YEAR.description = 前の N 年の指定されたメトリクスを計算する
timeFunction.TRAILING_30_DAYS.name = 過去 30 日間 
timeFunction.TRAILING_30_DAYS.description = 過去 30 日間の指定されたメトリクスを計算する
timeFunction.TRAILING_60_DAYS.name = 過去 60 日間 
timeFunction.TRAILING_60_DAYS.description = 過去 60 日間の指定されたメトリクスを計算する
timeFunction.TRAILING_90_DAYS.name = 過去 90 日間 
timeFunction.TRAILING_120_DAYS.name = 過去 120 日間 
timeFunction.TRAILING_90_DAYS.description = 過去 90 日間の指定されたメトリクスを計算する
timeFunction.TRAILING_120_DAYS.description = 過去 120 日間の指定されたメトリクスを計算する
timeFunction.TRAILING_12_MONTHS.name = 過去 12 か月間 
timeFunction.TRAILING_12_MONTHS.description = 過去 12 か月間の指定されたメトリクスを計算する
timeFunction.YEAR_TO_DATE.name = 現在までの 1 年間 
timeFunction.YEAR_TO_DATE.description = 同年の指定されたメトリクスを計算する
timeFunction.QUARTER_TO_DATE.name = 現在までの四半期 
timeFunction.QUARTER_TO_DATE.description = 同四半期の指定されたメトリクスを計算する
timeFunction.MONTH_TO_DATE.name = 現在までの 1 か月 
timeFunction.MONTH_TO_DATE.description = 同月の指定されたメトリクスを計算する
timeFunction.PREVIOUS_YEAR_TO_DATE.name = 前年から現在まで 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = 前の N 年内の同年の指定されたメトリクスを計算する
timeFunction.WEEK_TO_DATE_LAST_YEAR.name = 現在までの 1 週間 (前年) 
timeFunction.WEEK_TO_DATE_LAST_YEAR.description = 前の N 年内の同週の指定されたメトリクスを計算する
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = 現在までの 1 か月 (前年) 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = 前の N 年内の同月の指定されたメトリクスを計算する
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = 現在までの四半期 (前年) 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = 前の N 年内の同四半期の指定されたメトリクスを計算する
timeFunction.CURRENT_YEAR.name = 当年 
timeFunction.CURRENT_YEAR.description = 当年の指定されたメトリクスを計算する
timeFunction.WEEK_TO_DATE.name = 現在までの 1 週間 
timeFunction.WEEK_TO_DATE.description = 同週の指定されたメトリクスを計算する
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = N 期間前からの現在の期間 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = N 期間前からの現在の期間の指定されたメトリクスを計算する
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = N 期間前からの現在までの期間 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = N 期間前からの同じ期間の指定されたメトリクスを計算する
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N 期間前からの過去 N 期間 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = N 期間前からの過去 N 期間の指定されたメトリクスを計算する
timeFunction.TRAILING_N_MONTHS.name = Trailing N Months 
timeFunction.TRAILING_N_MONTHS.description = Calculate the specified metric for last N months
timeFunction.TRAILING_N_DAYS.name = Trailing N Days 
timeFunction.TRAILING_N_DAYS.description = Calculate the specified metric for last N days
timeFunction.NEXT_N_PERIODS.name = 次の N 期間 
timeFunction.NEXT_N_PERIODS.description = 次の N 期間の指定されたメトリクスを計算する

timeFunction.PERIOD1 = 時間機能の最初の期間の引数
timeFunction.PERIOD2 = 時間機能の 2 番目の期間の引数
timeFunction.N1 = 最初の期間の N 値
timeFunction.N2 = 2 番目の期間の N 値

timeFunction.PERIOD1.displayName = 最初の期間(&F)
timeFunction.PERIOD2.displayName = 2 番目の期間(&R)
timeFunction.N1.displayName = 最初の N(&I)
timeFunction.N2.displayName = 2 番目の N(&O)

timeFunction.N1.Year.displayName = Number of Year(s) Ago
timeFunction.N2.Year.displayName = Number of Year(s) Ago
timeFunction.N1.Year = N value for number of Year(s) ago
timeFunction.N2.Year = N value for number of Year(s) ago

timeFunction.N1.Month.displayName = Number of Month(s) Ago
timeFunction.N2.Month.displayName = Number of Month(s) Ago
timeFunction.N1.Month = N value for number of Month(s) ago
timeFunction.N2.Month = N value for number of Month(s) ago

timeFunction.N1.Quarter.displayName = Number of Quarter(s) Ago
timeFunction.N2.Quarter.displayName = Number of Quarter(s) Ago
timeFunction.N1.Quarter = N value for number of Quarter(s) ago
timeFunction.N2.Quarter = N value for number of Quarter(s) ago

timeFunction.N1.Week.displayName = Number of Week(s) Ago
timeFunction.N2.Week.displayName = Number of Week(s) Ago
timeFunction.N1.Week = N value for number of Week(s) ago
timeFunction.N2.Week = N value for number of Week(s) ago

timeFunction.N1.Day.displayName = Number of Day(s) Ago

timeFunction.PERIODCHOICE.YEAR_displayName =YEAR
timeFunction.PERIODCHOICE.QUARTER_displayName =QUARTER
timeFunction.PERIODCHOICE.MONTH_displayName =MONTH
timeFunction.PERIODCHOICE.WEEK_displayName =WEEK
timeFunction.PERIODCHOICE.DAY_displayName =DAY
 
timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =Year to Date
timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =Quarter to Date
timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =Month to Date
timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =Week to Date

timeFunction.toolTip.to=終了日
