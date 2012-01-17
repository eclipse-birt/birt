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

datasethandle.null.error=DataSetHandle 不能为空。
datasethandle.invalidColumnIndex=列索引无效。
adapter.invalid.datetimetype=日期时间类型“{0}”无效。
adapter.invalid.aggregationfunctionname=The aggregation function {0} is invalid.

invalid.datetime.value=日期时间值“{0}”无效。
missing.join.condition=主数据集“{0}”与多维数据集“{2}”中的维“{1}”之间的联接条件缺失。
exception.error=处理过程中出错。有关详情，请参阅下列消息:
invalid.level.expression=找不到级别名称“{0}”。
invalid.measure.expression=度量“{0}”的度量表达式不能为空。
invalid.data.type=无效数据类型: {0}。 

cube.error.createhierarchy=创建层次结构时出现错误: {0}。
cube.error.createdimension=创建维时出现错误: {0}。
cube.error.createmeasure=创建度量时出现错误。
cube.error.datasetmiss=由于缺少相关数据集，无法创建多维数据集。
cube.jointColumnNotInLevels=联接列“{0}”未定义为维“{1}”中任何级别的关键字字段。
cube.unsupportedAggregationInBinding=xTab 中不支持绑定“{0}”中的 TopN/BottomN 函数。

cube.query.error.dimensionMiss=多维数据集中不存在维 "{0}"。
cube.query.error.levelMiss=时间类型为 "{0}" 的级别不存在于多维数据集时间维 "{1}" 中。
cube.edge.error.levelMiss=查询定义中缺少时间类型为 "{0}" 的级别。

timeFunction.CURRENT_QUARTER.name = 本季度 
timeFunction.CURRENT_QUARTER.description = 计算本季度的指定指标
timeFunction.CURRENT_MONTH.name = 本月
timeFunction.CURRENT_MONTH.description = 计算本月的指定指标
timeFunction.PREVIOUS_MONTH.name = 上月
timeFunction.PREVIOUS_MONTH.description = 计算前 N 月的指定指标
timeFunction.PREVIOUS_WEEK_TO_DATE.name = 上周至今
timeFunction.PREVIOUS_WEEK_TO_DATE.description = 计算前 N 月周初至今的指定指标
timeFunction.PREVIOUS_MONTH_TO_DATE.name = 上月初至今
timeFunction.PREVIOUS_MONTH_TO_DATE.description = 计算前 N 月初至今的指定指标
timeFunction.PREVIOUS_QUARTER.name = 上季度 
timeFunction.PREVIOUS_QUARTER.description = 计算前 N 季度的指定指标
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = 上季度初至今 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = 计算前 N 季度的指定指标
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = 去年 
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
timeFunction.YEAR_TO_DATE.name = 年初至今 
timeFunction.YEAR_TO_DATE.description = 计算同一年的指定指标
timeFunction.QUARTER_TO_DATE.name = 季度初至今 
timeFunction.QUARTER_TO_DATE.description = 计算同一季度的指定指标
timeFunction.MONTH_TO_DATE.name = 月初至今 
timeFunction.MONTH_TO_DATE.description = 计算同一月的指定指标
timeFunction.PREVIOUS_YEAR_TO_DATE.name = 去年初至今 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = 计算前 N 年中同一年的指定指标
timeFunction.WEEK_TO_DATE_LAST_YEAR.name = 去年周累计（周至今） 
timeFunction.WEEK_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一周的指定指标
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = 去年月累计（月至今） 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一月的指定指标
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = 去年季度累计（季度至今） 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = 计算前 N 年中同一季度的指定指标
timeFunction.CURRENT_YEAR.name = 本年度 
timeFunction.CURRENT_YEAR.description = 计算本年度的指定指标
timeFunction.WEEK_TO_DATE.name = 周初至今 
timeFunction.WEEK_TO_DATE.description = 计算同一周的指定指标
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = N 个周期前的当前周期 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = 计算 N 个周期前当前周期的指定指标
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = N 个周期前期初至今 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = 计算 N 个周期前同一周期的指定指标
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N 个周期前最近的 N 个周期 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = 计算 N 个周期前最近的 N 个周期的指定指标
timeFunction.NEXT_N_PERIODS.name = 下 N 个周期 
timeFunction.NEXT_N_PERIODS.description = 计算下 N 个周期的指定指标

timeFunction.PERIOD1 = 时间函数的第一个周期变元
timeFunction.PERIOD2 = 时间函数的第二个周期变元
timeFunction.N1 = 第一个周期的 N 值
timeFunction.N2 = 第二个周期的 N 值

timeFunction.PERIOD1.displayName = 第一个周期(&F)
timeFunction.PERIOD2.displayName = 第一个周期(&R)
timeFunction.N1.displayName = 第一个 N(&I)
timeFunction.N2.displayName = 第二个 N(&O)

timeFunction.PERIODCHOICE.YEAR_displayName =YEAR
timeFunction.PERIODCHOICE.QUARTER_displayName =QUARTER
timeFunction.PERIODCHOICE.MONTH_displayName =MONTH
timeFunction.PERIODCHOICE.WEEK_displayName =WEEK
timeFunction.PERIODCHOICE.DAY_displayName =DAY
 
timeFunction.toolTip.to=至
