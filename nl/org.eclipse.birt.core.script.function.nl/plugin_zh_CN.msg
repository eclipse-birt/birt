#Properties file for org.eclipse.birt.core.script.bre
Bundle-Name.0 = 函数插件

dataType.String=字符串

dataType.Any=任何

dataType.DateTime=DateTime
dataType.Integer=整型

dataType.Double=双精度

dataType.Number=数值

dataType.Boolean=布尔

dataType.DoubleArray=双精度[]

FunctionDesc.charLength=返回字符串中的字符数。

FunctionDesc.concat=返回所有给定字符串连接的字符串。

FunctionDesc.indexOf=在字符串源中搜索目标，并返回第一个模式的索引。 \n搜索从位置起点开始。 所有索引值均基于 0。\n如果省略开头，则假设值为 0。 \n字符串搜索区分大小写。

FunctionDesc.left=返回字符串 str 的前 n 个字符。 \n如果 n 为 0，则返回空字符串。 \n如果 n 大于 str 的长度，则返回整个字符串。 \n如果省略 n，则使用值 1。

FunctionDesc.right=返回字符串 str 的后 n 个字符。 \n如果 n 为 0，则返回空字符串。 如果 n 大于 str 的长度，则返回整个字符串。 \n如果省略 n，则使用值 1。

FunctionDesc.search=与 indexOf 函数类似，下列情况除外: \n(1) 字符串比较区分大小写 \n(2) 模式字符串可以包含通配符: \n* 匹配任何字符序列(包括空字符) \n? 匹配任何单个字符。

FunctionDesc.toLower=将字符串源中的所有字符转换为小写字母。

FunctionDesc.toUpper=将字符串源中的所有字符转换为大写字母。

FunctionDesc.trim=删除所有前导和尾随空格字符(空格、制表符等)。\n此外，所有连续空格字符视为一个。

FunctionDesc.trimLeft=删除所有前导空格。 尾随空格和单词间的空格不会被删除。

FunctionDesc.trimRight=删除所有尾随空格。 前导空格和单词间的空格不会被删除。

FunctionDesc.anyOf=源在目标范围内

FunctionDesc.between=如果源介于目标 1 与目标 2 之间，则为 TRUE。 否则为 FALSE

FunctionDesc.notBetween=如果源不介于目标 1 与目标 2 之间，则为 TRUE。 否则为 FALSE

FunctionDesc.equalTo=源等于目标

FunctionDesc.greaterThan=源大于目标

FunctionDesc.lessThan=源小于目标

FunctionDesc.greaterOrEqual=源大于或等于目标

FunctionDesc.lessOrEqual=源小于或等于目标

FunctionDesc.notEqual=源不等于目标

FunctionDesc.like=源与 SQL 模式目标相匹配

FunctionDesc.notLike=源与 SQL 模式目标不匹配

FunctionDesc.match=源与 Javascript 模式目标相匹配

FunctionDesc.compareString=按指定条件比较两个字符串

FunctionDesc.add=计算两个数 n1 和 n2 的和。

FunctionDesc.substract=n1 减去 n2

FunctionDesc.multiple=n1 乘以 n2。 使用此函数只是为了与函数“multiply”反向兼容。

FunctionDesc.multiply=n1 乘以 n2

FunctionDesc.divide=被除数除以除数

FunctionDesc.round=将数字四舍五入为指定的小数位数。 \n十进制为整数，并且可以为负数。 \n省略时，十进制的默认值为 0

FunctionDesc.roundUp=将数字远离 0 的方向向上舍入为指定的小数位数。 \n十进制的默认值为 0。

FunctionDesc.roundDown=将数字远离 0 的方向向下舍入为指定的小数位数。 \n十进制的默认值为 0。

FunctionDesc.ceiling=将数字远离 0 的方向向上舍入为最接近的舍入基数的倍数。

FunctionDesc.mod=数字除以除数所得的余数。 结果的正负号与除数相同。

FunctionDesc.safeDivide=如果被除数除以除数所得的结果不为零或无穷大，则返回结果。 \n否则，如果为零，则返回给定值。

FunctionDesc.year=指定日期/时间值的 4 位数的年度数值

FunctionDesc.quarter=指定日期/时间值的季度数值(1 至 4)

FunctionDesc.month=日期/时间值的月份。 选项为整数值:\n1 (默认值): 返回月份编号(1-12)；\n2: 根据用户区域设置返回完整的月份名称(例如: 对于英语区域设置为 January 至 December)；\n3: 根据用户区域设置返回简短的月份名称(例如: 对于英语区域设置为 Jan 至 Dec)

FunctionDesc.week=日期/时间值一年的周编号(1 至 52)。

FunctionDesc.day=一月的天编号(1 至 31)。

FunctionDesc.weekDay=星期。 选项为整数值\n1: 返回数值 1 (星期日)至 7 (星期六)；\n2: 返回数值 1 (星期一)至 7 (星期日)；\n3: 返回数值 0 (星期一)至 6 (星期日)；\n4: 根据用户区域设置返回星期的名称(例如: 对于英语为 Sunday 至 Saturday)；\n5: 根据用户区域设置返回星期的缩写名称(例如: 对于英语为 Sun 至 Sat)

FunctionDesc.today=返回当前日期午夜的时间戳值。

FunctionDesc.now=返回当前时间戳。

FunctionDesc.diffYear=返回年份数差值。

FunctionDesc.diffMonth=返回月份数差值。

FunctionDesc.diffQuarter=返回季度数差值。

FunctionDesc.diffWeek=返回周数差值。

FunctionDesc.diffDay=返回天数差值。

FunctionDesc.diffHour=返回小时数差值。

FunctionDesc.diffMinute=返回分钟数差值。

FunctionDesc.diffSecond=返回秒数差值。

FunctionDesc.addYear=为日期加上 n 年

FunctionDesc.addMonth=为日期加上 n 个月

FunctionDesc.addQuarter=为日期加上 n 个季度

FunctionDesc.addWeek=为日期加上 n 周

FunctionDesc.addDay=为日期加上 n 天

FunctionDesc.addHour=为日期加上 n 小时

FunctionDesc.addMinute=为日期加上 n 分钟

FunctionDesc.addSecond=为日期加上 n 秒

FunctionDesc.firstDayOfYear=返回该日期所在年份的第一天。

FunctionDesc.firstDayOfQuarter=返回该日期所在季度的第一天。

FunctionDesc.firstDayOfMonth=返回该日期所在月份的第一天。

FunctionDesc.firstDayOfWeek=返回该日期所在周的第一天。

FunctionDesc.ddb=某项资产在指定的单个期间内使用双倍余额递减法计算的折旧 备注

FunctionDesc.fv=根据固定利率和等额分期付款方法计算的年金未来值。

FunctionDesc.ipmt=根据固定利率和等额分期付款方法计算的年金\n在指定期间内的利息。

FunctionDesc.irr=现有数组内一系列定期现金流（付款和收款）的内部回报率

FunctionDesc.mirr=现有数组内一系列定期现金流（付款和收款）的修改回报率。

FunctionDesc.nper=根据固定利率和等额分期付款方法计算的年金期数。

FunctionDesc.npv=一系列定期现金流（正和负）按指定利率计算的净现值。

FunctionDesc.percent=计算两个数的百分比

FunctionDesc.pmt=根据固定利率和等额分期付款方法计算的年金付款。

FunctionDesc.ppmt=根据利率和固定付款计划计算的年金在指定期间内的本金付款。

FunctionDesc.pv=根据固定利率和等额分期未来付款方法计算的年金现值。

FunctionDesc.rate=年金按期间平均的利率。

FunctionDesc.sln=某项资产在单个期间内的直线折旧。

FunctionDesc.syd=某项资产在指定期间内的年限总和折旧 备注

FunctionDesc.addDate=返回开始日期加上年份数、月份数和天数得出的日期。

FunctionDesc.addTime=返回开始日期加上小时数、分钟数、秒数得出的日期。

FunctionDesc.subDate=返回开始日期减去年份数、月份数和天数得出的日期。

FunctionDesc.subTime=返回开始日期减去小时数、分钟数、秒数得出的日期。

FunctionDesc.days=两个日期之间的天数。

FunctionDesc.hours=两个日期之间的小时数

FunctionDesc.minutes=两个日期之间的分钟数

FunctionDesc.months=两个日期之间的月数

FunctionDesc.seconds=两个日期之间的秒数

FunctionDesc.years=两个日期之间的年数

FunctionDesc.date= 返回以指定的年、月、日、小时、分钟和秒进行初始化的日期对象。 \n(年、月、日为必需；小时、分钟、秒为可选)

FunctionDesc.duration.year=获取作为整数值的 YEARS 字段的值，若不存在则为 * 或 0。\nlexicalDuration 是持续时间字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.month=获取作为整数值的 MONTHS 字段的值，若不存在则为 * 或 0。\nlexicalDuration 是持续时间字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.day=获取作为整数值的 DAYS 字段的值，若不存在则为 * 或 0。\nlexicalDuration 是持续时间字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.hour=获取作为整数值的 HOURS 字段的值，若不存在则为 * 或 0。\nlexicalDuration 是持续时间字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.minute=获取作为整数值的 MINUTES 字段的值，若不存在则为 * 或 0。\nlexicalDuration 是持续时间字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.second=获取作为整数值的 SECONDS 字段的值，若不存在则为 * 或 0。\nlexicalDuration 是持续时间字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.timeInMills=返回以毫秒表示的持续时间长度。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)\nstartDate 参数用于消除此变量的歧义

FunctionDesc.duration.isLongerThan=检查此 lexicalDuration1 是否严格长于另一 lexicalDuration2。\n例如，"P1D"(一天) > "PT12H"(12 小时)，"P2Y" (两年) > "P23M" (23 个月)

FunctionDesc.duration.isShorterThan=检查 lexicalDuration1 是否严格短于另一 lexicalDuration2。\n例如，"PT12H"(12 小时) < "P1D"(一天)。

FunctionDesc.duration.add=计算其值为 lexicalDuration1+lexicalDuration2 的新持续时间。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.substract=计算其值为 lexicalDuration1-lexicalDuration2 的新持续时间。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.multiply=计算其值为值 lexicalDuration 的倍数的新持续时间。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)\nFactor is the times longer of new Duration to create

FunctionDesc.duration.negate=返回其值为 -lexicalDuration 的新持续时间对象。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.getSign=返回此持续时间的符号，以 -1、0 或 1 表示。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.compare=lexicalDuration1 与 lexicalDuration2 之间的部分顺序关系。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)

FunctionDesc.duration.addTo=将此持续时间添加到日期对象。\nlexicalDuration 是字符串表示 "PnYnMnDTnHnMnS"(例如 P5Y2M15DT4H40M20S)\nThe given Date argument is used to be modified

FunctionDesc.format=从指定的对象和模式获取格式化字符串。
