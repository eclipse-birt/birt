# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} 在 {0} 中未找到。

###########################################################

#load data engine factory exception
load.factory.error=加载数据适配器的工厂时出错。

context.null.error=数据上下文不能为空。

context.invalidDesigntimeMethod=ReportContext 方法 [{0}] 只能用于运行时。

adaptor.invalid.mode=数据适配器上下文的模式值 {0} 无效。

invalid.cache.option=数据适配器上下文的选项值 {0} 无效。

datasource.null.error=找不到数据集“{0}”的数据源。

datasource.exid.error=数据源定义中缺少扩展 ID: {0}。

datasource.cassandra.error=无法找到 Hector 客户端库 jar 以支持 Cassandra 脚本化数据源。

datasethandle.null.error=DataSetHandle 不能为空。

datasethandle.invalidColumnIndex=列索引无效。

adapter.invalid.datetimetype=日期时间类型“{0}”无效。

adapter.invalid.aggregationfunctionname=聚合函数 {0} 无效。

invalid.datetime.value=日期时间值“{0}”无效。

missing.join.condition=主数据集“{0}”与多维数据集“{2}”中的维“{1}”之间的联接条件缺失。

exception.error=处理过程中出错。有关详情，请参阅下列消息:

invalid.level.expression=找不到级别名称“{0}”。

invalid.measure.expression=度量“{0}”的度量表达式不能为空。

invalid.data.type=无效数据类型: {0}。

invalid.binding.expression=无效表达式: {0}。

invalid.computedColumn.expression=引用了无效数据集列“{1}”的无效计算列“{0}”。

cube.error.createhierarchy=创建层次结构时出现错误: {0}。

cube.error.createdimension=创建维时出现错误: {0}。

cube.error.createmeasure=创建度量时出现错误。

cube.error.datasetmiss=由于缺少相关数据集，无法创建多维数据集。

cube.jointColumnNotInLevels=联接列“{0}”未定义为维“{1}”中任何级别的关键字字段。

cube.unsupportedAggregationInBinding=xTab 中不支持绑定“{0}”中的 TopN/BottomN 函数。

cube.error.aggregationFilter.referCalculatedMeasure=无效聚合 "{0}"。 筛选表达式引用了计算度量 "{1}"。

cube.query.error.dimensionMiss=多维数据集中不存在维 "{0}"。

cube.query.error.levelMiss=时间类型为 "{0}" 的级别不存在于多维数据集时间维 "{1}" 中。

cube.edge.error.levelMiss=查询定义中缺少时间类型为 "{0}" 的级别。

cube.derivedMeasure.invalid.reference=无效度量引用。 衍生度量 "{0}" 引用了无效度量: "{1}"。

cube.derivedMeasure.resolve.error=解析衍生度量 "{0}" 失败。

cube.derivedMeasure.recursive.ref=解析衍生度量 "{0}" 失败，因为在 "{1}" 中检测到循环引用。

timeFunction.CURRENT_QUARTER.name = 本季度

timeFunction.CURRENT_QUARTER.description = 计算本季度的指定指标

timeFunction.CURRENT_MONTH.name = 本月

timeFunction.CURRENT_MONTH.description = 计算本月的指定指标

timeFunction.PREVIOUS_MONTH.name = 前 N 月

timeFunction.PREVIOUS_MONTH.description = 计算前 N 月的指定指标

timeFunction.PREVIOUS_WEEK_TO_DATE.name = 前 N 周至今

timeFunction.PREVIOUS_WEEK_TO_DATE.description = 计算前 N 周的周初至今的指定指标

timeFunction.PREVIOUS_MONTH_TO_DATE.name = 前 N 月初至今

timeFunction.PREVIOUS_MONTH_TO_DATE.description = 计算前 N 月初至今的指定指标

timeFunction.PREVIOUS_QUARTER.name = 前 N 季度

timeFunction.PREVIOUS_QUARTER.description = 计算前 N 季度的指定指标

timeFunction.PREVIOUS_QUARTER_TO_DATE.name = 前 N 季度初至今

timeFunction.PREVIOUS_QUARTER_TO_DATE.description = 计算前 N 季度的指定指标

timeFunction.PREVIOUS_PREVIOUS_YEAR.name = 前 N 年

timeFunction.PREVIOUS_PREVIOUS_YEAR.description = 计算前 N 年的指定指标

timeFunction.TRAILING_30_DAYS.name = 最后 30 天

timeFunction.TRAILING_30_DAYS.description = 计算前 30 天的指定指标

timeFunction.TRAILING_60_DAYS.name = 最后 60 天

timeFunction.TRAILING_60_DAYS.description = 计算前 60 天的指定指标

timeFunction.TRAILING_90_DAYS.name = 最后 90 天

timeFunction.TRAILING_120_DAYS.name = 最后 120 天

timeFunction.TRAILING_90_DAYS.description = 计算前 90 天的指定指标

timeFunction.TRAILING_120_DAYS.description = 计算前 120 天的指定指标

timeFunction.TRAILING_12_MONTHS.name = 最后 12 个月

timeFunction.TRAILING_12_MONTHS.description = 计算前 12 个月的指定指标

timeFunction.YEAR_TO_DATE.name = 年初至今 (YTD)

timeFunction.YEAR_TO_DATE.description = 计算同一年的指定指标

timeFunction.QUARTER_TO_DATE.name = 季度初至今 (QTD)

timeFunction.QUARTER_TO_DATE.description = 计算同一季度的指定指标

timeFunction.MONTH_TO_DATE.name = 月初至今 (MTD)

timeFunction.MONTH_TO_DATE.description = 计算同一月的指定指标

timeFunction.PREVIOUS_YEAR_TO_DATE.name = 前 N 年初至今

timeFunction.PREVIOUS_YEAR_TO_DATE.description = 计算前 N 年中同一年的指定指标

timeFunction.WEEK_TO_DATE_LAST_YEAR.name = 去年周累计（周至今）

timeFunction.WEEK_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一周的指定指标

timeFunction.MONTH_TO_DATE_LAST_YEAR.name = 去年月累计（月至今）

timeFunction.MONTH_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一月的指定指标

timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = 去年季度累计（季度至今）

timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一季度的指定指标

timeFunction.CURRENT_YEAR.name = 本年度

timeFunction.CURRENT_YEAR.description = 计算本年度的指定指标

timeFunction.WEEK_TO_DATE.name = 周初至今 (WTD)

timeFunction.WEEK_TO_DATE.description = 计算同一周的指定指标

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = N 个周期前的当前周期

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = 计算 N 个周期前当前周期的指定指标

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = N 个周期前期初至今

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = 计算 N 个周期前同一周期的指定指标

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N 个周期前最近的 N 个周期

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = 计算 N 个周期前最近的 N 个周期的指定指标

timeFunction.TRAILING_N_MONTHS.name = 最后 N 个月

timeFunction.TRAILING_N_MONTHS.description = 计算最后 N 个月的指定指标

timeFunction.TRAILING_N_DAYS.name = 最后 N 天

timeFunction.TRAILING_N_DAYS.description = 计算最后 N 天的指定指标

timeFunction.NEXT_N_PERIODS.name = 下 N 个周期

timeFunction.NEXT_N_PERIODS.description = 计算下 N 个周期的指定指标

timeFunction.PERIOD1 = 时间函数的第一个周期变元

timeFunction.PERIOD2 = 时间函数的第二个周期变元

timeFunction.N1 = 第一个周期的 N 值

timeFunction.N2 = 第二个周期的 N 值

timeFunction.PERIOD1.displayName = 第一个周期(&F)

timeFunction.PERIOD2.displayName = 第一个周期(&R)

timeFunction.N1.displayName = N 周期前

timeFunction.N2.displayName = 自(&O)

timeFunction.N1.next.displayName = 下一步(&N)

timeFunction.N2.Ago.displayName=先前(&A)

timeFunction.N1.current.displayName=当前(&U)

timeFunction.N1.period.displayName= 周期(&U)

timeFunction.trailing.N1.period.displayName=尾随(&I)

timeFunction.trailing.N1.Month.displayName= 尾随月数(&N)

timeFunction.N1.Year.displayName = N 年前(&N)

timeFunction.N2.Year.displayName = N 年前(&N)

timeFunction.N1.Year = N 年前中的 N 值

timeFunction.N2.Year = N 年前中的 N 值

timeFunction.N1.Month.displayName = N 月前(&N)

timeFunction.N2.Month.displayName = N 月前(&N)

timeFunction.N1.Month = N 月前中的 N 值

timeFunction.N2.Month = N 月前中的 N 值

timeFunction.N1.Quarter.displayName = N 季前(&N)

timeFunction.N2.Quarter.displayName = N 季前(&N)

timeFunction.N1.Quarter = N 季度前中的 N 值

timeFunction.N2.Quarter = N 季度前中的 N 值

timeFunction.N1.Week.displayName = N 周前(&N)

timeFunction.N2.Week.displayName = N 周前(&N)

timeFunction.N1.Week = N 周前中的 N 值

timeFunction.N2.Week = N 周前中的 N 值

timeFunction.N1.Day.displayName = N 天前(&N)

timeFunction.PERIODCHOICE.YEAR_displayName =年

timeFunction.PERIODCHOICE.QUARTER_displayName =季度

timeFunction.PERIODCHOICE.MONTH_displayName =月

timeFunction.PERIODCHOICE.WEEK_displayName =周

timeFunction.PERIODCHOICE.DAY_displayName =日

timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =年初至今

timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =季度初至今

timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =月初至今

timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =周初至今

timeFunction.toolTip.to=至

 # Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} 在 {0} 中未找到。

###########################################################

#load data engine factory exception
load.factory.error=加载数据适配器的工厂时出错。

context.null.error=数据上下文不能为空。

context.invalidDesigntimeMethod=ReportContext 方法 [{0}] 只能用于运行时。

adaptor.invalid.mode=数据适配器上下文的模式值 {0} 无效。

invalid.cache.option=数据适配器上下文的选项值 {0} 无效。

datasource.null.error=找不到数据集“{0}”的数据源。

datasource.exid.error=数据源定义中缺少扩展 ID: {0}。

datasource.cassandra.error=无法找到 Hector 客户端库 jar 以支持 Cassandra 脚本化数据源。

datasethandle.null.error=DataSetHandle 不能为空。

datasethandle.invalidColumnIndex=列索引无效。

adapter.invalid.datetimetype=日期时间类型“{0}”无效。

adapter.invalid.aggregationfunctionname=聚合函数 {0} 无效。

invalid.datetime.value=日期时间值“{0}”无效。

missing.join.condition=主数据集“{0}”与多维数据集“{2}”中的维“{1}”之间的联接条件缺失。

exception.error=处理过程中出错。有关详情，请参阅下列消息:

invalid.level.expression=找不到级别名称“{0}”。

invalid.measure.expression=度量“{0}”的度量表达式不能为空。

invalid.data.type=无效数据类型: {0}。

invalid.binding.expression=无效表达式: {0}。

cube.error.createhierarchy=创建层次结构时出现错误: {0}。

cube.error.createdimension=创建维时出现错误: {0}。

cube.error.createmeasure=创建度量时出现错误。

cube.error.datasetmiss=由于缺少相关数据集，无法创建多维数据集。

cube.jointColumnNotInLevels=联接列“{0}”未定义为维“{1}”中任何级别的关键字字段。

cube.unsupportedAggregationInBinding=xTab 中不支持绑定“{0}”中的 TopN/BottomN 函数。

cube.error.aggregationFilter.referCalculatedMeasure=无效聚合 "{0}"。 筛选表达式引用了计算度量 "{1}"。

cube.query.error.dimensionMiss=多维数据集中不存在维 "{0}"。

cube.query.error.levelMiss=时间类型为 "{0}" 的级别不存在于多维数据集时间维 "{1}" 中。

cube.edge.error.levelMiss=查询定义中缺少时间类型为 "{0}" 的级别。

cube.derivedMeasure.invalid.reference=无效度量引用。 衍生度量 "{0}" 引用了无效度量: "{1}"。

cube.derivedMeasure.resolve.error=解析衍生度量 "{0}" 失败。

cube.derivedMeasure.recursive.ref=解析衍生度量 "{0}" 失败，因为在 "{1}" 中检测到循环引用。

timeFunction.CURRENT_QUARTER.name = 本季度

timeFunction.CURRENT_QUARTER.description = 计算本季度的指定指标

timeFunction.CURRENT_MONTH.name = 本月

timeFunction.CURRENT_MONTH.description = 计算本月的指定指标

timeFunction.PREVIOUS_MONTH.name = 前 N 月

timeFunction.PREVIOUS_MONTH.description = 计算前 N 月的指定指标

timeFunction.PREVIOUS_WEEK_TO_DATE.name = 前 N 周至今

timeFunction.PREVIOUS_WEEK_TO_DATE.description = 计算前 N 周的周初至今的指定指标

timeFunction.PREVIOUS_MONTH_TO_DATE.name = 前 N 月初至今

timeFunction.PREVIOUS_MONTH_TO_DATE.description = 计算前 N 月初至今的指定指标

timeFunction.PREVIOUS_QUARTER.name = 前 N 季度

timeFunction.PREVIOUS_QUARTER.description = 计算前 N 季度的指定指标

timeFunction.PREVIOUS_QUARTER_TO_DATE.name = 前 N 季度初至今

timeFunction.PREVIOUS_QUARTER_TO_DATE.description = 计算前 N 季度的指定指标

timeFunction.PREVIOUS_PREVIOUS_YEAR.name = 前 N 年

timeFunction.PREVIOUS_PREVIOUS_YEAR.description = 计算前 N 年的指定指标

timeFunction.TRAILING_30_DAYS.name = 最后 30 天

timeFunction.TRAILING_30_DAYS.description = 计算前 30 天的指定指标

timeFunction.TRAILING_60_DAYS.name = 最后 60 天

timeFunction.TRAILING_60_DAYS.description = 计算前 60 天的指定指标

timeFunction.TRAILING_90_DAYS.name = 最后 90 天

timeFunction.TRAILING_120_DAYS.name = 最后 120 天

timeFunction.TRAILING_90_DAYS.description = 计算前 90 天的指定指标

timeFunction.TRAILING_120_DAYS.description = 计算前 120 天的指定指标

timeFunction.TRAILING_12_MONTHS.name = 最后 12 个月

timeFunction.TRAILING_12_MONTHS.description = 计算前 12 个月的指定指标

timeFunction.YEAR_TO_DATE.name = 年初至今 (YTD)

timeFunction.YEAR_TO_DATE.description = 计算同一年的指定指标

timeFunction.QUARTER_TO_DATE.name = 季度初至今 (QTD)

timeFunction.QUARTER_TO_DATE.description = 计算同一季度的指定指标

timeFunction.MONTH_TO_DATE.name = 月初至今 (MTD)

timeFunction.MONTH_TO_DATE.description = 计算同一月的指定指标

timeFunction.PREVIOUS_YEAR_TO_DATE.name = 前 N 年初至今

timeFunction.PREVIOUS_YEAR_TO_DATE.description = 计算前 N 年中同一年的指定指标

timeFunction.WEEK_TO_DATE_LAST_YEAR.name = 去年周累计（周至今）

timeFunction.WEEK_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一周的指定指标

timeFunction.MONTH_TO_DATE_LAST_YEAR.name = 去年月累计（月至今）

timeFunction.MONTH_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一月的指定指标

timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = 去年季度累计（季度至今）

timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一季度的指定指标

timeFunction.CURRENT_YEAR.name = 本年度

timeFunction.CURRENT_YEAR.description = 计算本年度的指定指标

timeFunction.WEEK_TO_DATE.name = 周初至今 (WTD)

timeFunction.WEEK_TO_DATE.description = 计算同一周的指定指标

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = N 个周期前的当前周期

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = 计算 N 个周期前当前周期的指定指标

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = N 个周期前期初至今

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = 计算 N 个周期前同一周期的指定指标

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N 个周期前最近的 N 个周期

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = 计算 N 个周期前最近的 N 个周期的指定指标

timeFunction.TRAILING_N_MONTHS.name = 最后 N 个月

timeFunction.TRAILING_N_MONTHS.description = 计算最后 N 个月的指定指标

timeFunction.TRAILING_N_DAYS.name = 最后 N 天

timeFunction.TRAILING_N_DAYS.description = 计算最后 N 天的指定指标

timeFunction.NEXT_N_PERIODS.name = 下 N 个周期

timeFunction.NEXT_N_PERIODS.description = 计算下 N 个周期的指定指标

timeFunction.PERIOD1 = 时间函数的第一个周期变元

timeFunction.PERIOD2 = 时间函数的第二个周期变元

timeFunction.N1 = 第一个周期的 N 值

timeFunction.N2 = 第二个周期的 N 值

timeFunction.PERIOD1.displayName = 第一个周期(&F)

timeFunction.PERIOD2.displayName = 第一个周期(&R)

timeFunction.N1.displayName = N 周期前

timeFunction.N2.displayName = 自(&O)

timeFunction.N1.next.displayName = 下一步(&N)

timeFunction.N2.Ago.displayName=先前(&A)

timeFunction.N1.current.displayName=当前(&U)

timeFunction.N1.period.displayName= 周期(&U)

timeFunction.trailing.N1.period.displayName=尾随(&I)

timeFunction.trailing.N1.Month.displayName= 尾随月数(&N)

timeFunction.N1.Year.displayName = N 年前(&N)

timeFunction.N2.Year.displayName = N 年前(&N)

timeFunction.N1.Year = N 年前中的 N 值

timeFunction.N2.Year = N 年前中的 N 值

timeFunction.N1.Month.displayName = N 月前(&N)

timeFunction.N2.Month.displayName = N 月前(&N)

timeFunction.N1.Month = N 月前中的 N 值

timeFunction.N2.Month = N 月前中的 N 值

timeFunction.N1.Quarter.displayName = N 季前(&N)

timeFunction.N2.Quarter.displayName = N 季前(&N)

timeFunction.N1.Quarter = N 季度前中的 N 值

timeFunction.N2.Quarter = N 季度前中的 N 值

timeFunction.N1.Week.displayName = N 周前(&N)

timeFunction.N2.Week.displayName = N 周前(&N)

timeFunction.N1.Week = N 周前中的 N 值

timeFunction.N2.Week = N 周前中的 N 值

timeFunction.N1.Day.displayName = N 天前(&N)

timeFunction.PERIODCHOICE.YEAR_displayName =年

timeFunction.PERIODCHOICE.QUARTER_displayName =季度

timeFunction.PERIODCHOICE.MONTH_displayName =月

timeFunction.PERIODCHOICE.WEEK_displayName =周

timeFunction.PERIODCHOICE.DAY_displayName =日

timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =年初至今

timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =季度初至今

timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =月初至今

timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =周初至今

timeFunction.toolTip.to=至
RelativeTimePeriod.TimeLevelNotEnough=报表项不包含通过参考日期计算此相对时间周期需要的所有时间级别。
 
 
 
