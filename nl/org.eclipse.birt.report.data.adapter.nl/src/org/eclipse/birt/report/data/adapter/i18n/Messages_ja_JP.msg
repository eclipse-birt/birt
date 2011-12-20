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
timeFunction.TRAILING_120_DAYS.name = Trailing 120 Days 
timeFunction.TRAILING_90_DAYS.description = 過去 90 日間の指定されたメトリクスを計算する
timeFunction.TRAILING_120_DAYS.description = Calculate the specified metric for last 120 days
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
timeFunction.WEEK_TO_DATE_LAST_YEAR.name = Week to Date Last Year 
timeFunction.WEEK_TO_DATE_LAST_YEAR.description = Calculate the specified metric for same week in previous N year
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
timeFunction.NEXT_N_PERIODS.name = 次の N 期間 
timeFunction.NEXT_N_PERIODS.description = 次の N 期間の指定されたメトリクスを計算する

timeFunction.PERIOD1 = 時間機能の最初の期間の引数
timeFunction.PERIOD2 = 時間機能の 2 番目の期間の引数
timeFunction.N1 = 最初の期間の N 値
timeFunction.N2 = 2 番目の期間の N 値

timeFunction.PERIOD1.displayName = 最初の期間
timeFunction.PERIOD2.displayName = 2 番目の期間
timeFunction.N1.displayName = 最初の N
timeFunction.N2.displayName = 2 番目の N
 
timeFunction.toolTip.to=終了日
