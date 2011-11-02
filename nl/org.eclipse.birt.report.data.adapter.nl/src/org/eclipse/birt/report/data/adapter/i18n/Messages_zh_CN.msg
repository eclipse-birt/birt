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

timeFunction.CURRENT_QUARTER.name = Current Quarter 
timeFunction.CURRENT_QUARTER.description = Calculate the specified metric for current quarter
timeFunction.CURRENT_MONTH.name = Current Month
timeFunction.CURRENT_MONTH.description = Calculate the specified metric for current month
timeFunction.PREVIOUS_MONTH.name = Previous Month
timeFunction.PREVIOUS_MONTH.description = Calculate the specified metric for previous N month
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
 