# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.


###########################################################
#for test only
Error.Msg001=未在 {1} 中找到 {0}。

###########################################################
# Boolean type names
Property.Boolean.True=真

Property.Boolean.False=假

###########################################################
# Property type names
Property.boolean=布尔

Property.choice=选择

Property.float=浮点

Property.color=颜色

Property.dateTime=DateTime
Property.dimension=维

Property.expression=表达式

Property.html=Html
Property.integer=整型

Property.messageId=消息 ID

Property.name=名称

Property.number=数值

Property.points=点

Property.string=字符串

Property.uri=Uri
Property.xml=Xml
Property.elementRef=元素引用

Property.column=列

Property.variant=变体

Property.structList=结构列表

Property.extends=扩展

Property.script=脚本

Property.list=列表

Property.memberKey=成员键

Property.literalString=文字字符串

Property.element=元素

Property.structRef=结构引用

Property.contentElement=元素为属性值

Property.locale=区域设置

Property.struct=结构

###########################################################
# Choice values
  
#0.colors
Choices.colors.maroon=栗色

Choices.colors.red=红色

Choices.colors.orange=橙色

Choices.colors.yellow=黄色

Choices.colors.olive=橄榄色

Choices.colors.purple=紫色

Choices.colors.fuchsia=紫红色

Choices.colors.white=白色

Choices.colors.lime=酸橙色

Choices.colors.green=绿色

Choices.colors.navy=深蓝色

Choices.colors.blue=蓝色

Choices.colors.aqua=水绿色

Choices.colors.teal=青色

Choices.colors.black=黑色

Choices.colors.silver=银色

Choices.colors.gray=灰色
  
#1.fontWeight
Choices.fontWeight.normal=正常

Choices.fontWeight.bold=粗体

Choices.fontWeight.bolder=略粗

Choices.fontWeight.lighter=略淡

Choices.fontWeight.100=100

Choices.fontWeight.200=200

Choices.fontWeight.300=300

Choices.fontWeight.400=400

Choices.fontWeight.500=500

Choices.fontWeight.600=600

Choices.fontWeight.700=700

Choices.fontWeight.800=800

Choices.fontWeight.900=900

#2.textUnderline
Choices.textUnderline.none=无下划线

Choices.textUnderline.underline=下划线
  
#3.lineStyle
Choices.lineStyle.none=无线条样式

Choices.lineStyle.solid=实线

Choices.lineStyle.dotted=点线

Choices.lineStyle.dashed=虚线

Choices.lineStyle.double=双精度

Choices.lineStyle.groove=槽线

Choices.lineStyle.ridge=脊线

Choices.lineStyle.inset=内凹

Choices.lineStyle.outset=外凸
  
#4.units
Choices.units.in=英寸

Choices.units.cm=厘米

Choices.units.mm=毫米

Choices.units.pt=点

Choices.units.pc=十二点活字

Choices.units.em=ems

Choices.units.ex=exs

Choices.units.px=像素

Choices.units.%=%

#5.paramDataType
Choices.paramType.string=字符串

Choices.paramType.float=浮点

Choices.paramType.decimal=小数

Choices.paramType.dateTime=日期时间

Choices.paramType.boolean=布尔

Choices.paramType.integer=整数

Choices.paramType.any=任何

Choices.paramType.date=日期

Choices.paramType.time=时间

Choices.paramType.javaObject=Java 对象

#6.paramControl
Choices.paramControl.text-box=文本框

Choices.paramControl.list-box=组合框/列表框

Choices.paramControl.radio-button=单选按钮

Choices.paramControl.check-box=复选框

Choices.paramControl.auto-suggest=文本框 - 自动建议

#7.textAlign
Choices.textAlign.left=靠左

Choices.textAlign.center=居中

Choices.textAlign.right=靠右

Choices.textAlign.justify=两端对齐
  
#8.pageSize
Choices.pageSize.custom=自定义

Choices.pageSize.us-letter=美国信封

Choices.pageSize.us-legal=美国正规

Choices.pageSize.a4=A4
Choices.pageSize.a3=A3
Choices.pageSize.a5=A5
Choices.pageSize.us-ledger=美国分类帐

Choices.pageSize.us-super-b=美国 Super B
  
#9.pageOrientation
Choices.pageOrientation.auto=自动

Choices.pageOrientation.portrait=纵向

Choices.pageOrientation.landscape=横向
   
#12.interval
Choices.interval.none=无间隔

Choices.interval.prefix=前缀

Choices.interval.year=年

Choices.interval.quarter=季

Choices.interval.month=月

Choices.interval.week=周

Choices.interval.day=日

Choices.interval.hour=小时

Choices.interval.minute=分钟

Choices.interval.second=秒

Choices.interval.interval=间隔

#interval type
Choices.intervalType.none=无间隔

Choices.intervalType.prefix=前缀

Choices.intervalType.interval=间隔

#13.sortDirection
Choices.sortDirection.asc=升序

Choices.sortDirection.desc=降序
  
#14.mapOperator
Choices.mapOperator.eq=等于

Choices.mapOperator.ne=不等于

Choices.mapOperator.lt=小于

Choices.mapOperator.le=小于或等于

Choices.mapOperator.ge=大于或等于

Choices.mapOperator.gt=大于

Choices.mapOperator.between=介于

Choices.mapOperator.notBetween=不介于

Choices.mapOperator.isNull=为 NULL

Choices.mapOperator.isNotNull=不为 NULL

Choices.mapOperator.isTrue=为真

Choices.mapOperator.isFalse=为假

Choices.mapOperator.like=类似

Choices.mapOperator.match=匹配

Choices.mapOperator.any=任何

Choices.mapOperator.top-n=前 n 个

Choices.mapOperator.bottom-n=后 n 个

Choices.mapOperator.top-percent=最高百分比

Choices.mapOperator.bottom-percent=最低百分比

Choices.mapOperator.not-like=非类似于

Choices.mapOperator.not-match=不匹配

Choices.mapOperator.not-in=不属于

Choices.mapOperator.in=位置
  
#15.imageRefType
Choices.imageRefType.none=无图像

Choices.imageRefType.url=URL
Choices.imageRefType.file=文件

Choices.imageRefType.expr=表达式

Choices.imageRefType.embed=嵌入图像
  
#16.imageSize
Choices.imageSize.size-to-image=调整为图像大小

Choices.imageSize.scale-to-item=缩放至项目

Choices.imageSize.clip=剪裁
  
#17.lineOrientation
Choices.lineOrientation.horizontal=水平

Choices.lineOrientation.vertical=垂直
  
#18.rowAlign
Choices.rowAlign.top=顶部

Choices.rowAlign.middle=中间

Choices.rowAlign.bottom=底部
  
#19.sectionAlign
Choices.sectionAlign.left=左

Choices.sectionAlign.center=中

Choices.sectionAlign.right=右
  
#21.dropType
Choices.dropType.none=无

Choices.dropType.detail=详细资料

Choices.dropType.all=全部
    
#22.imageType
Choices.imageType.image/bmp=image/bmp

Choices.imageType.image/gif=image/gif

Choices.imageType.image/png=image/png

Choices.imageType.image/x-png=image/x-png

Choices.imageType.image/jpeg=image/jpeg

Choices.imageType.image/ico=image/ico

Choices.imageType.image/tiff=image/tiff

Choices.imageType.image/svg+xml=image/svg+xml

Choices.imageType.image/x-icon=image/x-icon
  
#23.lineSpacing
Choices.lineSpacing.lines=线条

Choices.lineSpacing.exact=精确
  
#24.lineType
Choices.lineType.none=无线条类型

Choices.lineType.solid=实线

Choices.lineType.dotted=点线

Choices.lineType.dashed=虚线

Choices.lineType.double=双线
  
#25.pageBreakBefore
Choices.pageBreakBefore.auto=自动

Choices.pageBreakBefore.always=始终

Choices.pageBreakBefore.avoid=避免

Choices.pageBreakBefore.alwaysExcludingFirst=始终排除第一个

#26.pageBreakAfter
Choices.pageBreakAfter.auto=自动

Choices.pageBreakAfter.always=始终

Choices.pageBreakAfter.avoid=避免

Choices.pageBreakAfter.alwaysExcludingLast=始终排除最后一个

#27.actionLinkType
Choices.actionLinkType.none=无操作链接类型

Choices.actionLinkType.hyperlink=HyperLink
Choices.actionLinkType.drill-through=钻取

Choices.actionLinkType.bookmark-link=书签链接
  
# actionFormatType
Choices.actionFormatType.html=html
Choices.actionFormatType.pdf=pdf
  
#28.drillThroughLinkType
Choices.drillThroughType.none=无追溯类型

Choices.drillThroughType.bookmark-link=书签链接

Choices.drillThroughType.search=搜索
 
#29.fontSize
Choices.fontSize.x-small=X 小

Choices.fontSize.xx-small=XX 小

Choices.fontSize.small=小

Choices.fontSize.medium=中等

Choices.fontSize.large=大

Choices.fontSize.x-large=X 大

Choices.fontSize.xx-large=XX 大

Choices.fontSize.larger=更大

Choices.fontSize.smaller=更小

#30.fontFamily
Choices.fontFamily.serif=Serif

Choices.fontFamily.sans-serif=Sans Serif

Choices.fontFamily.cursive=Cursive

Choices.fontFamily.fantasy=幻想

Choices.fontFamily.monospace=等距

#31.fontStyle
Choices.fontStyle.normal=正常

Choices.fontStyle.italic=斜体

Choices.fontStyle.oblique=倾斜

#32.fontVariant
Choices.fontVariant.normal=正常

Choices.fontVariant.small-caps=小写

#33.backgroundRepeat
Choices.backgroundRepeat.repeat=重复

Choices.backgroundRepeat.repeat-x=重复 X

Choices.backgroundRepeat.repeat-y=重复 Y

Choices.backgroundRepeat.no-repeat=不重复

#34.backgroundAttachment
Choices.backgroundAttachment.scroll=滚动

Choices.backgroundAttachment.fixed=固定

#35.backgroundPositionX
Choices.backgroundPositionX.left=左

Choices.backgroundPositionX.center=中

Choices.backgroundPositionX.right=右

#36.backgroundPositionY
Choices.backgroundPositionY.top=顶部

Choices.backgroundPositionY.center=中间

Choices.backgroundPositionY.bottom=底部

#37.transform
Choices.transform.capitalize=大写

Choices.transform.uppercase=大写字母

Choices.transform.lowercase=小写字母

Choices.transform.none=无转换

#38.normal
Choices.normal.normal=正常

#39.verticalAlign
Choices.verticalAlign.baseline=基线

Choices.verticalAlign.sub=下标

Choices.verticalAlign.super=上标

Choices.verticalAlign.top=顶部

Choices.verticalAlign.text-top=顶部文本

Choices.verticalAlign.middle=中间

Choices.verticalAlign.bottom=底部

Choices.verticalAlign.text-bottom=底部文本

#40.whiteSpace
Choices.whiteSpace.normal=正常

Choices.whiteSpace.pre=预设格式

Choices.whiteSpace.nowrap=不换行

#41.display
Choices.display.block=块

Choices.display.inline=嵌入式

Choices.display.none=不显示

#43.pageBreakInside
Choices.pageBreakInside.avoid=避免

Choices.pageBreakInside.auto=自动

#44.margin
Choices.margine.auto=自动

#45.textOverline
Choices.textOverline.none=无覆盖文本

Choices.textOverline.overline=上划线

#46.textLineThrough
Choices.textLineThrough.none=无删除文本

Choices.textLineThrough.line-through=删除线

#48.lineWidth
Choices.lineWidth.thin=细

Choices.lineWidth.medium=中

Choices.lineWidth.thick=粗

#49.inherit
Choices.inherit.inherit=继承

#50. textContentType
Choices.textContentType.auto=自动

Choices.textContentType.plain=无格式

Choices.textContentType.html=HTML
Choices.textContentType.rtf=RTF

#51. formatType
Choices.formatType.all=全部

Choices.formatType.viewer=查看器

Choices.formatType.email=电子邮件

Choices.formatType.print=打印

Choices.formatType.pdf=PDF

Choices.formatType.rtf=RTF

Choices.formatType.reportlet=报表项

Choices.formatType.excel=Excel
Choices.formatType.word=Word
Choices.formatType.powerpoint=Powerpoint

#52. filterOperator
Choices.filterOperator.eq=等于

Choices.filterOperator.ne=不等于

Choices.filterOperator.lt=小于

Choices.filterOperator.le=小于或等于

Choices.filterOperator.ge=大于或等于

Choices.filterOperator.gt=大于

Choices.filterOperator.between=介于

Choices.filterOperator.notBetween=不介于

Choices.filterOperator.isNull=为 NULL

Choices.filterOperator.isNotNull=不为 NULL

Choices.filterOperator.isTrue=为真

Choices.filterOperator.isFalse=为假

Choices.filterOperator.like=类似

Choices.filterOperator.not-like=非类似于

Choices.filterOperator.top-n=前 n 个

Choices.filterOperator.bottom-n=后 n 个

Choices.filterOperator.top-percent=最高百分比

Choices.filterOperator.bottom-percent=最低百分比

Choices.filterOperator.any=任何

Choices.filterOperator.match=匹配

Choices.filterOperator.not-match=不匹配

Choices.filterOperator.in=位置

Choices.filterOperator.not-in=不属于

#53. propertyMaskType
Choices.propertyMaskType.change=更改

Choices.propertyMaskType.lock=锁定

Choices.propertyMaskType.hide=隐藏

#53. columnDataType
Choices.columnDataType.any=任何

Choices.columnDataType.integer=整型

Choices.columnDataType.string=字符串

Choices.columnDataType.date-time=日期时间

Choices.columnDataType.decimal=小数

Choices.columnDataType.float=浮点

Choices.columnDataType.structure=结构

Choices.columnDataType.table=表

Choices.columnDataType.boolean=布尔

Choices.columnDataType.time=时间

Choices.columnDataType.date=日期

Choices.columnDataType.blob=Blob

Choices.columnDataType.javaObject=Java 对象

#54. searchType
Choices.searchType.any=任何

Choices.searchType.indexed=编入索引

Choices.searchType.none=无搜索类型

#55. exportType
Choices.exportType.none=无导出类型

Choices.exportType.if-realized=如果实现

Choices.exportType.always=始终

#56. analysisType
Choices.analysisType.dimension=维

Choices.analysisType.attribute=属性

Choices.analysisType.measure=度量

#57. scalarParamAlign
Choices.scalarParamAlign.auto=自动

Choices.scalarParamAlign.left=左

Choices.scalarParamAlign.center=中

Choices.scalarParamAlign.right=右

#58. columnAlign
Choices.columnAlign.left=左

Choices.columnAlign.center=中

Choices.columnAlign.right=右

#59. dateTimeFormat
Choices.dateTimeFormat.unformatted=无格式

Choices.dateTimeFormat.generalDate=常规日期

Choices.dateTimeFormat.longDate=长日期

Choices.dateTimeFormat.mediumDate=中日期

Choices.dateTimeFormat.shortDate=短日期

Choices.dateTimeFormat.longTime=长时间

Choices.dateTimeFormat.mediumTime=中时间

Choices.dateTimeFormat.shortTime=短时间

Choices.dateTimeFormat.custom=自定义

#59. dateFormat
Choices.dateFormat.unformatted=无格式

Choices.dateFormat.generalDate=常规日期

Choices.dateFormat.longDate=长日期

Choices.dateFormat.mediumDate=中等日期

Choices.dateFormat.shortDate=短日期

Choices.dateFormat.custom=自定义

#59. timeFormat
Choices.timeFormat.unformatted=无格式

Choices.timeFormat.longTime=长时间

Choices.timeFormat.mediumTime=中等时间

Choices.timeFormat.shortTime=短时间

Choices.timeFormat.custom=自定义
    
#60. numberFormat
Choices.numberFormat.unformatted=无格式

Choices.numberFormat.generalNumber=常规数字

Choices.numberFormat.currency=货币

Choices.numberFormat.fixed=固定

Choices.numberFormat.percent=百分比

Choices.numberFormat.scientific=科学记数

Choices.numberFormat.standard=标准

Choices.numberFormat.custom=自定义
    
#59. StringFormat
Choices.stringFormat.unformatted=无格式

Choices.stringFormat.upper=大写字母

Choices.stringFormat.lower=小写字母

Choices.stringFormat.zipCode=邮政编码

Choices.stringFormat.zipCode+4=邮政编码 + 4

Choices.stringFormat.phoneNumber=电话号码

Choices.stringFormat.socialSecurityNumber=社会保险号

Choices.stringFormat.custom=自定义

#60. targetNames
Choices.targetNames.blank=空白

Choices.targetNames.self=自

Choices.targetNames.parent=父

Choices.targetNames.top=顶部

#50. textDataContentType
Choices.textDataContentType.auto=自动

Choices.textDataContentType.plain=无格式

Choices.textDataContentType.html=HTML
Choices.textDataContentType.rtf=RTF

#51. paramaValueType
Choices.paramValueType.static=静态

Choices.paramValueType.dynamic=动态

#52. templateElementType
Choices.templateElementType.table=表

Choices.templateElementType.freeForm=FreeForm
Choices.templateElementType.data=数据

Choices.templateElementType.grid=网格

Choices.templateElementType.image=图像

Choices.templateElementType.label=标签

Choices.templateElementType.list=列表

Choices.templateElementType.text=文本

Choices.templateElementType.extendedItem=ExtendedItem
Choices.templateElementType.textData=TextData
Choices.templateElementType.dataSet=数据集

#61. sortType
Choices.sortType.none=无排序类型

Choices.sortType.sortOnGroupkey=按组键排序

Choices.sortType.complexSort=复杂排序

#62. joinType
Choices.joinType.inner=内联接

Choices.joinType.leftOut=左向外联接

Choices.joinType.rightOut=右向外联接

Choices.joinType.fullOut=完全外部联接

#63. joinOperator
Choices.joinOperator.equals=等于

#64. actionTargetFileType
Choices.actionTargetFileType.report-design=报表设计

Choices.actionTargetFileType.report-document=报表文档

#65. actionBookmarkType
Choices.actionBookmarkType.bookmark=书签

Choices.actionBookmarkType.toc=目录

#66. autoTextType
Choices.autoTextType.page-number=页码

Choices.autoTextType.total-page=总页数

Choices.autoTextType.page-number-unfiltered=未筛选的页数

Choices.autoTextType.total-page-unfiltered=未筛选的总页数

Choices.autoTextType.page-variable=页面变量

#67. dataSetMode
Choices.dataSetMode.single=单个数据集

Choices.dataSetMode.multiple=多个数据集

#68. filterTarget
Choices.filterTarget.DataSet=数据集

Choices.filterTarget.ResultSet=结果集

Choices.filterTarget.OdaDataSet=ODA 数据集

#69. viewAction
Choices.viewAction.NoChange=无更改

Choices.viewAction.Changed=已更改

Choices.viewAction.Added=已添加

Choices.viewAction.Deleted=已删除

#70. aggregationFunction
Choices.aggregationFunction.sum=SUM

Choices.aggregationFunction.count=COUNT

Choices.aggregationFunction.min=MIN

Choices.aggregationFunction.max=MAX

Choices.aggregationFunction.average=AVERAGE

Choices.aggregationFunction.weighted-avg=WEIGHTEDAVG

Choices.aggregationFunction.stddev=STDDEV

Choices.aggregationFunction.first=FIRST

Choices.aggregationFunction.last=LAST

Choices.aggregationFunction.mode=MODE

Choices.aggregationFunction.moving-ave=MOVINGAVE

Choices.aggregationFunction.median=MEDIAN

Choices.aggregationFunction.variance=VARIANCE

Choices.aggregationFunction.running-sum=RUNNINGSUM

Choices.aggregationFunction.irr=IRR

Choices.aggregationFunction.mirr=MIRR

Choices.aggregationFunction.npv=NPV

Choices.aggregationFunction.running-npv=RUNNINGNPV

Choices.aggregationFunction.count-distinct=COUNTDISTINCT

Choices.aggregationFunction.running-count=RUNNINGCOUNT

Choices.aggregationFunction.is-top-n=IS-TOP-N

Choices.aggregationFunction.is-bottom-n=IS-BOTTOM-N

Choices.aggregationFunction.is-top-n-percent=IS-TOP-N-PERCENT

Choices.aggregationFunction.is-bottom-n-percent=IS-BOTTOM-N-PERCENT

Choices.aggregationFunction.percent-rank=PERCENT-RANK

Choices.aggregationFunction.percentile=PERCENTILE

Choices.aggregationFunction.quartile=QUARTILE

Choices.aggregationFunction.percent-sum=PERCENT-SUM

Choices.aggregationFunction.rank=排列

#71. levelType
Choices.levelType.dynamic=动态

Choices.levelType.mirrored=镜像

#72. accessPermission
Choices.accessPermission.allow=允许

Choices.accessPermission.disallow=禁止

#73. paramSortValues
Choices.paramSortValues.value=值

Choices.paramSortValues.label=显示文本

#74. reportLayoutPreference
Choices.reportLayoutPreference.fixedLayout=固定布局

Choices.reportLayoutPreference.autoLayout=自动布局

#75. dateTimeLevelType
Choices.dateTimeLevelType.year=年

Choices.dateTimeLevelType.month=月

Choices.dateTimeLevelType.quarter=季

Choices.dateTimeLevelType.weekOfMonth=月中第几周

Choices.dateTimeLevelType.weekOfYear=年中第几周

Choices.dateTimeLevelType.dayOfWeek=星期几

Choices.dateTimeLevelType.dayOfMonth=月中第几天

Choices.dateTimeLevelType.dayOfYear=年中第几天

Choices.dateTimeLevelType.hour=小时

Choices.dateTimeLevelType.minute=分钟

Choices.dateTimeLevelType.second=秒

#76. scalarParamType
Choices.scalarParamType.simple=简单

Choices.scalarParamType.multi-value=多个值

#77. bidiLayoutOrientation   
Choices.bidiDirection.rtl=从右到左

Choices.bidiDirection.ltr=从左到右

#78. backgroundSize
Choices.backgroundSize.auto=自动

Choices.backgroundSize.contain=包含

Choices.backgroundSize.cover=包括

#79 scopeType
Choices.scopeType.row=行

Choices.scopeType.col=列

Choices.scopeType.rowgroup=行组

Choices.scopeType.colgroup=列组

#80 dynamicFilterChoice
Choices.dynamicFilterChoice.simple=简单

Choices.dynamicFilterChoice.advanced=高级

#81 variableType
Choices.variableType.report=报表变量

Choices.variableType.page=页面变量

#82 nullValueOrderingType
Choices.nullValueOrderingType.unknown=未知

Choices.nullValueOrderingType.nullIsFirst=空值在前

Choices.nullValueOrderingType.nullIsLast=空值在后

#83 dataVersion
Choices.dataVersion.transient=暂时

Choices.dataVersion.latest=最新

#84 selectorType
Choices.selectorType.list=列表

Choices.selectorType.dropdown=下拉框

Choices.selectorType.slider=滑块

Choices.selectorType.checkBox=复选框

Choices.selectorType.radioButton=RadioButton
Choices.selectorType.textBox=文本框

Choices.selectorType.calendar=日历

Choices.selectorType.currentSelections=当前选择

#85 thumbType
Choices.thumbType.single=单

Choices.thumbType.dual=双

#86
Choices.accessType.transient=暂时

Choices.accessType.latest=最新

Choices.accessType.specificVersion=SpecificVersion

#87 overflow
Choices.overflow.visible=可见

Choices.overflow.hidden=隐藏

Choices.overflow.scroll=滚动

Choices.overflow.auto=自动

#88 GadgetContentType
Choices.gadgetContentType.html=HTML
Choices.gadgetContentType.url=URL
Choices.gadgetContentType.video=视频

Choices.gadgetContentType.videourl=视频 URL

Choices.gadgetContentType.text=文本

Choices.gadgetContentType.image=图像

#89 WindowStatus
Choices.windowStatus.normal=正常

Choices.windowStatus.maximized=已最大化

#90 reportItemThemeType
Choices.reportItemThemeType.Table=表布景主题

Choices.reportItemThemeType.List=列表布景主题

Choices.reportItemThemeType.Grid=网格布景主题

#91 nullsOrdering
Choices.nullsOrdering.nullsLowest=空值下限

Choices.nullsOrdering.nullsHighest=空值上限

Choices.nullsOrdering.excludeNulls=排除空值

#92 filterConditionType
Choices.filterConditionType.slicer=限制器

Choices.filterConditionType.simple=简单

#93 flyoutPosition
Choices.flyoutPosition.left=靠左

Choices.flyoutPosition.right=靠右

#94 referenceDateType
Choices.referenceDateType.today=今天

Choices.referenceDateType.fixedDate=固定日期

Choices.referenceDateType.endingDateInDimension=维的结束日期

###########################################################
# Classes

Class.optionalArgument=...

#0.String
Class.String=字符串函数

Class.String.toolTip=对象类型的成员和内置字符串对象的实例

Class.String.String=字符串

Class.String.String.toolTip=返回 ToString(value) 计算的字符串值

Class.String.length=长度

Class.String.length.toolTip=返回字符串的长度

Class.String.fromCharCode=fromCharCode
Class.String.fromCharCode.toolTip=返回由多个 Unicode 字符值形成的字符串

Class.String.charAt=charAt

Class.String.charAt.toolTip=返回字符串对象的指定索引处的字符

Class.String.charCodeAt=charCodeAt

Class.String.charCodeAt.toolTip=返回表示字符串对象中指定位置的字符的 Unicode 编码的整数

Class.String.concat=concat

Class.String.concat.toolTip=返回包含当前字符串与提供的任何字符串连接形成的字符串的字符串值

Class.String.indexOf=indexOf

Class.String.indexOf.toolTip=返回字符串对象中的第一个子字符串

Class.String.lastIndexOf=lastIndexOf

Class.String.lastIndexOf.toolTip=返回字符串对象中的最后一个子字符串

Class.String.localeCompare=localeCompare
Class.String.localeCompare.toolTip=返回表示当前区域的两个字符串是否相同的值

Class.String.match=匹配

Class.String.match.toolTip=使用常规表达式模式执行字符串搜索，并返回包含搜索结果的数组

Class.String.replace=替换

Class.String.replace.toolTip=返回使用常规表达式或搜索字符串替换文本的字符串副本

Class.String.search=搜索

Class.String.search.toolTip=返回常规表达式搜索中第一个匹配的子字符串的位置

Class.String.slice=切片

Class.String.slice.toolTip=返回将此对象转换为字符串的结果的子字符串

Class.String.split=拆分

Class.String.split.toolTip=返回将字符串分隔为子字符串的结果的字符串数组

Class.String.substr=substr

Class.String.substr.toolTip=返回从指定位置开始并具有指定长度的子字符串

Class.String.substring=子字符串

Class.String.substring.toolTip=返回字符串对象中指定位置的子字符串

Class.String.toLowerCase=toLowerCase

Class.String.toLowerCase.toolTip=将字符串中的字符逐个转换为小写字母

Class.String.toLocaleLowerCase=toLocaleLowerCase

Class.String.toLocaleLowerCase.toolTip=返回一个字符串，其中所有字母字符均已转换为小写字母，并考虑主机环境的当前区域

Class.String.toUpperCase=toUpperCase

Class.String.toUpperCase.toolTip=将字符串中的字符逐个转换为大写字母

Class.String.toLocaleUpperCase=toLocaleUpperCase

Class.String.toLocaleUpperCase.toolTip=返回一个字符串，其中所有字母字符均已转换为大写字母，并考虑主机环境的当前区域

Class.String.toString=toString
Class.String.toString.toolTip=返回此字符串值

Class.String.valueOf=valueOf
Class.String.valueOf.toolTip=返回此字符串值

Class.String.String.object=对象

Class.String.fromCharCode.c=c

Class.String.charAt.n=n
Class.String.charCodeAt.n=n
Class.String.concat.value=值

Class.String.indexOf.substring=子字符串

Class.String.indexOf.start=开始

Class.String.lastIndexOf.substring=子字符串

Class.String.lastIndexOf.start=开始

Class.String.localeCompare.target=目标

Class.String.match.regexp=regexp

Class.String.replace.regexp=regexp

Class.String.replace.replacement=替换

Class.String.search.regexp=regexp

Class.String.slice.start=开始

Class.String.slice.end=结束

Class.String.split.delimitor=分隔符

Class.String.split.limit=限制

Class.String.substr.start=开始

Class.String.substr.length=长度

Class.String.substring.from=自

Class.String.substring.to=到

#1.Global
Class.Global=全局函数

Class.Global.toolTip=一个内部对象，其目的是将全局方法收集到一个对象

Class.Global.NaN=NaN

Class.Global.NaN.toolTip=初始值不是数值

Class.Global.Infinity=无穷大

Class.Global.Infinity.toolTip=返回 Number.POSITIVE_INFINITY 的值（大于最大数 (Number.MAX_VALUE) 的值，可以用 JScript 表示）

Class.Global.undefined=未定义

Class.Global.undefined.toolTip=未定义初始值

Class.Global.decodeURI=decodeURI
Class.Global.decodeURI.toolTip=返回已编码的统一资源标识符 (URI) 的未编码版本

Class.Global.decodeURIComponent=decodeURIComponent
Class.Global.decodeURIComponent.toolTip=返回统一资源标识符 (URI) 的已编码组件的未编码版本

Class.Global.encodeURI=encodeURI
Class.Global.encodeURI.toolTip=返回已编码为有效统一资源标识符 (URI) 的字符串

Class.Global.encodeURIComponent=encodeURIComponent
Class.Global.encodeURIComponent.toolTip=返回已编码为统一资源标识符 (URI) 的有效组件的字符串

Class.Global.escape=换码

Class.Global.escape.toolTip=返回所有计算机均可读取的编码字符串对象

Class.Global.eval=求值

Class.Global.eval.toolTip=对 JScript 代码求值并执行

Class.Global.isNaN=isNaN

Class.Global.isNaN.toolTip=将 ToNumber 应用于其参数，如果计算结果是 NaN，则返回真

Class.Global.isFinite=isFinite
Class.Global.isFinite.toolTip=返回表示提供的数值是否为有限值的布尔值

Class.Global.parseFloat=parseFloat
Class.Global.parseFloat.toolTip=返回从字符串转换的浮点数

Class.Global.parseInt=parseInt

Class.Global.parseInt.toolTip=返回从字符串转换的整数

Class.Global.unescape=未换码

Class.Global.unescape.toolTip=返回字符串对象使用换码方法编码的字符串


Class.Global.decodeURI.uri=uri
Class.Global.decodeURIComponent.s=s

Class.Global.encodeURI.uri=uri
Class.Global.encodeURIComponent.s=s

Class.Global.escape.s=s

Class.Global.eval.code=代码

Class.Global.isNaN.x=x
Class.Global.isFinite.n=n
Class.Global.parseFloat.s=s

Class.Global.parseInt.s=s

Class.Global.parseInt.radix=基数

Class.Global.unescape.s=秒

#2.Array
Class.Array=数组函数

Class.Array.toolTip=为所有数据类型的可重新调整大小数组提供支持

Class.Array.Array=数组

Class.Array.Array.toolTip=创建并初始化新数组对象

Class.Array.length=长度

Class.Array.length.toolTip=返回比数组中定义的最大元素大 1 的整数值

Class.Array.concat=concat

Class.Array.concat.toolTip=返回由当前数组和任何附加项组合的新数组

Class.Array.join=连接

Class.Array.join.toolTip=返回一个字符串值，它包含连接在一起并由指定的分隔符分隔的数组的所有元素

Class.Array.pop=弹出

Class.Array.pop.toolTip=数组中的最后一个元素已删除并已返回

Class.Array.push=推

Class.Array.push.toolTip=参数已按其出现的顺序附加到数组末尾

Class.Array.reverse=反转

Class.Array.reverse.toolTip=数组的元素已按逆序重新排列

Class.Array.shift=移动

Class.Array.shift.toolTip=数组中的第一个元素已删除并已返回

Class.Array.slice=切片

Class.Array.slice.toolTip=返回包含数组一部分的数组

Class.Array.sort=排序

Class.Array.sort.toolTip=此数组的元素已排序

Class.Array.splice=接合

Class.Array.splice.toolTip=删除数组中的元素，必要时在数组中添加新元素，并返回已删除的元素。返回已从数组中删除的元素

Class.Array.toLocaleString=toLocaleString
Class.Array.toLocaleString.toolTip=将数组的元素转换为字符串

Class.Array.toString=toString
Class.Array.toString.toolTip=返回为此不带参数的对象调用的内联方法

Class.Array.unshift=unshift

Class.Array.unshift.toolTip=此参数已附加到数组开头的前面，从而此参数在数组中的顺序与它们在参数列表中显示的顺序相同

Class.Array.Array.size=大小

Class.Array.Array.element=元素

Class.Array.concat.value=值

Class.Array.join.separator=分隔符

Class.Array.push.value=值

Class.Array.slice.start=开始

Class.Array.slice.end=结束

Class.Array.sort.orderfunc=orderfunc

Class.Array.splice.start=开始

Class.Array.splice.deleteCount=deleteCount
Class.Array.unshift.value=值

#3. Boolean
Class.Boolean=布尔函数

Class.Boolean.toolTip=对象类型的成员和内置布尔对象的实例

Class.Boolean.Boolean=布尔

Class.Boolean.Boolean.toolTip=返回 ToBoolean(value) 计算的布尔值

Class.Boolean.toString=toString
Class.Boolean.toString.toolTip=返回对象的字符串表示

Class.Boolean.valueOf=valueOf
Class.Boolean.valueOf.toolTip=返回此布尔值

Class.Boolean.Boolean.value=值

#4. Date
Class.Date=日期函数

Class.Date.toolTip=一个能够进行基本存储和恢复日期和时间的对象

Class.Date.Date=日期

Class.Date.Date.toolTip=返回表示当前时间的字符串

Class.Date.getDate=getDate
Class.Date.getDate.toolTip=返回使用本地时间的日期对象中某月的日期

Class.Date.getDay=getDay
Class.Date.getDay.toolTip=返回使用本地时间的日期对象中某星期的日期

Class.Date.getFullYear=getFullYear
Class.Date.getFullYear.toolTip=返回使用本地时间的日期对象中的年份值

Class.Date.getHours=getHours
Class.Date.getHours.toolTip=返回使用本地时间的日期对象中的小时值

Class.Date.getMilliseconds=getMilliseconds
Class.Date.getMilliseconds.toolTip=返回使用本地时间的日期对象中的毫秒值

Class.Date.getMinutes=getMinutes
Class.Date.getMinutes.toolTip=返回使用本地时间的日期对象中的分钟值

Class.Date.getMonth=getMonth
Class.Date.getMonth.toolTip=返回使用本地时间的日期对象中的月份值

Class.Date.getSeconds=getSeconds
Class.Date.getSeconds.toolTip=返回使用本地时间的日期对象中的秒值

Class.Date.getTime=getTime
Class.Date.getTime.toolTip=返回日期对象中的时间值

Class.Date.getTimezoneOffset=getTimezoneOffset
Class.Date.getTimezoneOffset.toolTip=返回本地时间和 UTC 时间的差值（以分钟为单位）

Class.Date.getUTCDate=getUTCDate
Class.Date.getUTCDate.toolTip=返回使用协调世界时 (UTC) 的日期对象中的日期

Class.Date.getUTCDay=getUTCDay
Class.Date.getUTCDay.toolTip=返回使用协调世界时 (UTC) 的日期对象中某星期的日期

Class.Date.getUTCFullYear=getUTCFullYear
Class.Date.getUTCFullYear.toolTip=返回使用协调世界时 (UTC) 的日期对象中的年份值

Class.Date.getUTCHours=getUTCHours
Class.Date.getUTCHours.toolTip=返回使用协调世界时 (UTC) 的日期对象中的小时值

Class.Date.getUTCMilliseconds=getUTCMilliseconds
Class.Date.getUTCMilliseconds.toolTip=返回使用协调世界时 (UTC) 的日期对象中的毫秒值

Class.Date.getUTCMinutes=getUTCMinutes
Class.Date.getUTCMinutes.toolTip=返回使用协调世界时 (UTC) 的日期对象中的分钟值

Class.Date.getUTCMonth=getUTCMonth
Class.Date.getUTCMonth.toolTip=返回使用协调世界时 (UTC) 的日期对象中的月份值

Class.Date.getUTCSeconds=getUTCSeconds
Class.Date.getUTCSeconds.toolTip=返回使用协调世界时 (UTC) 的日期对象中的秒值

Class.Date.getYear=getYear
Class.Date.getYear.toolTip=返回日期对象中的年份值

Class.Date.parse=分析

Class.Date.parse.toolTip=分析包含日期的字符串，并返回毫秒数

Class.Date.setDate=setDate
Class.Date.setDate.toolTip=在本地时区中设置日期对象的日期

Class.Date.setFullYear=setFullYear
Class.Date.setFullYear.toolTip=设置使用本地时间的日期对象中的年份值

Class.Date.setHours=setHours
Class.Date.setHours.toolTip=设置使用本地时间的日期对象中的小时值

Class.Date.setMilliseconds=setMilliseconds
Class.Date.setMilliseconds.toolTip=设置使用本地时间的日期对象中的毫秒值

Class.Date.setMinutes=setMinutes
Class.Date.setMinutes.toolTip=设置使用本地时间的日期对象中的分钟值

Class.Date.setMonth=setMonth
Class.Date.setMonth.toolTip=设置使用本地时间的日期对象中的月份值

Class.Date.setSeconds=setSeconds
Class.Date.setSeconds.toolTip=设置使用本地时间的日期对象中的秒值

Class.Date.setTime=setTime
Class.Date.setTime.toolTip=设置使用本地时间的日期对象中的时间值

Class.Date.setUTCDate=setUTCDate
Class.Date.setUTCDate.toolTip=设置使用协调世界时 (UTC) 的日期对象中的数字日期

Class.Date.setUTCFullYear=setUTCFullYear
Class.Date.setUTCFullYear.toolTip=设置使用协调世界时 (UTC) 的日期对象中的年份值

Class.Date.setUTCHours=setUTCHours
Class.Date.setUTCHours.toolTip=设置使用协调世界时 (UTC) 的日期对象中的小时值

Class.Date.setUTCMilliseconds=setUTCMilliseconds
Class.Date.setUTCMilliseconds.toolTip=设置使用协调世界时 (UTC) 的日期对象中的毫秒值

Class.Date.setUTCMinutes=setUTCMinutes
Class.Date.setUTCMinutes.toolTip=设置使用协调世界时 (UTC) 的日期对象中的分钟值

Class.Date.setUTCMonth=setUTCMonth
Class.Date.setUTCMonth.toolTip=设置使用协调世界时 (UTC) 的日期对象中的月份值

Class.Date.setUTCSeconds=setUTCSeconds
Class.Date.setUTCSeconds.toolTip=设置使用协调世界时 (UTC) 的日期对象中的秒值

Class.Date.setYear=setYear
Class.Date.setYear.toolTip=设置日期对象中的年份值

Class.Date.toDateString=toDateString
Class.Date.toDateString.toolTip=返回表示“日期”的字符串值

Class.Date.toGMTString=toGMTString
Class.Date.toGMTString.toolTip=返回表示 GMT 日期的字符串值

Class.Date.toLocaleDateString=toLocaleDateString
Class.Date.toLocaleDateString.toolTip=返回表示当前时区中的“日期”的字符串值

Class.Date.toLocaleString=toLocaleString
Class.Date.toLocaleString.toolTip=返回表示当前时区中的日期的字符串值

Class.Date.toLocaleTimeString=toLocaleTimeString
Class.Date.toLocaleTimeString.toolTip=返回表示当前时区中的“时间”的字符串值

Class.Date.toString=toString
Class.Date.toString.toolTip=返回字符串值

Class.Date.toTimeString=toTimeString
Class.Date.toTimeString.toolTip=返回表示“时间”的字符串值

Class.Date.toUTCString=toUTCString
Class.Date.toUTCString.toolTip=返回以方便易懂的形式表示 UTC 日期的字符串值

Class.Date.UTC=UTC
Class.Date.UTC.toolTip=根据年、月、日（可选）、小时、分钟、秒和毫秒计算日期

Class.Date.valueOf=valueOf
Class.Date.valueOf.toolTip=返回表示时间值的数值

Class.Date.Date.milliseconds=毫秒

Class.Date.Date.datestring=日期字符串

Class.Date.Date.year=年

Class.Date.Date.month=月

Class.Date.Date.day=日

Class.Date.Date.hours=小时

Class.Date.Date.minutes=分钟

Class.Date.Date.seconds=秒

Class.Date.Date.ms=ms

Class.Date.parse.date=日期

Class.Date.setDate.day_of_month=day_of_month
Class.Date.setFullYear.year=年

Class.Date.setFullYear.month=月

Class.Date.setFullYear.minutes=分钟

Class.Date.setFullYear.day=日

Class.Date.setHours.hours=小时

Class.Date.setHours.minutes=分钟

Class.Date.setHours.seconds=秒

Class.Date.setHours.millis=毫秒

Class.Date.setMilliseconds.millis=毫秒

Class.Date.setMinutes.minutes=分钟

Class.Date.setMinutes.seconds=秒

Class.Date.setMinutes.millis=毫秒

Class.Date.setMonth.month=月

Class.Date.setMonth.day=日

Class.Date.setSeconds.seconds=秒

Class.Date.setSeconds.millis=毫秒

Class.Date.setTime.millis=毫秒

Class.Date.setUTCDate.day_of_month=day_of_month
Class.Date.setUTCFullYear.year=年

Class.Date.setUTCFullYear.month=月

Class.Date.setUTCFullYear.minutes=分钟

Class.Date.setUTCFullYear.day=日

Class.Date.setUTCHours.hours=小时

Class.Date.setUTCHours.minutes=分钟

Class.Date.setUTCHours.seconds=秒

Class.Date.setUTCHours.millis=毫秒

Class.Date.setUTCMilliseconds.millis=毫秒

Class.Date.setUTCMinutes.minutes=分钟

Class.Date.setUTCMinutes.seconds=秒

Class.Date.setUTCMinutes.millis=毫秒

Class.Date.setUTCMonth.month=月

Class.Date.setUTCMonth.day=日

Class.Date.setUTCSeconds.seconds=秒

Class.Date.setUTCSeconds.millis=毫秒

Class.Date.setYear.year=年

Class.Date.UTC.year=年

Class.Date.UTC.month=月

Class.Date.UTC.day=日

Class.Date.UTC.hours=小时

Class.Date.UTC.minutes=分钟

Class.Date.UTC.seconds=秒

Class.Date.UTC.ms=毫秒

#5. Math
Class.Math=数学函数

Class.Math.toolTip=一种具有基本数学函数性和常量的内部对象

Class.Math.E=E

Class.Math.E.toolTip=e 的数值约等于 2.7182818284590452354

Class.Math.LN10=LN10

Class.Math.LN10.toolTip=10 的自然对数值约等于 2.302585092994046

Class.Math.LN2=LN2

Class.Math.LN2.toolTip=2 的自然对数值约等于 0.6931471805599453

Class.Math.LOG10E=LOG10E

Class.Math.LOG10E.toolTip=以 10 为底数的 e 的对数值约等于 0.4342944819032518

Class.Math.LOG2E=LOG2E

Class.Math.LOG2E.toolTip=以 2 为底数的 e 的对数值约等于 0.4342944819032518

Class.Math.PI=PI

Class.Math.PI.toolTip=圆周率 PI 的数值约等于 3.1415926535897932

Class.Math.SQRT1_2=SQRT1_2
Class.Math.SQRT1_2.toolTip=1/2 的平方根的数值约等于 0.7071067811865476

Class.Math.SQRT2=SQRT2

Class.Math.SQRT2.toolTip=2 的平方根的数值约等于 0.7071067811865476

Class.Math.abs=abs

Class.Math.abs.toolTip=返回绝对值

Class.Math.acos=acos

Class.Math.acos.toolTip=返回与实现相关的反余弦的近似值

Class.Math.asin=asin

Class.Math.asin.toolTip=返回与实现相关的反正弦的近似值

Class.Math.atan=atan

Class.Math.atan.toolTip=返回与实现相关的反正切的近似值

Class.Math.atan2=atan2

Class.Math.atan2.toolTip=返回与实现相关的参数 y 与 x 的商的反正切的近似值，其中 y 和 x 的符号用于确定结果的象限

Class.Math.ceil=ceil

Class.Math.ceil.toolTip=返回不小于 x 且等于数学整数的最小数值

Class.Math.cos=cos

Class.Math.cos.toolTip=返回与实现相关的余弦的近似值

Class.Math.exp=exp

Class.Math.exp.toolTip=返回与实现相关的指数函数的近似值

Class.Math.floor=floor

Class.Math.floor.toolTip=返回不大于 x 且等于数学整数的最大数值

Class.Math.log=log

Class.Math.log.toolTip=返回与实现相关的自然对数近似值

Class.Math.max=max

Class.Math.max.toolTip=返回最大结果值

Class.Math.min=min

Class.Math.min.toolTip=返回最小结果值

Class.Math.pow=pow

Class.Math.pow.toolTip=返回与实现相关的 x 的 y 次方的结果的近似值

Class.Math.random=random

Class.Math.random.toolTip=返回大于或等于 0 但小于 1 的负数值

Class.Math.round=round

Class.Math.round.toolTip=返回与 x 最接近的且等于数学整数的数值

Class.Math.sin=sin

Class.Math.sin.toolTip=返回与实现相关的正弦的近似值

Class.Math.sqrt=sqrt

Class.Math.sqrt.toolTip=返回与实现相关的平方根的近似值

Class.Math.tan=tan

Class.Math.tan.toolTip=返回与实现相关的正切的近似值


Class.Math.abs.x=x
Class.Math.acos.x=x
Class.Math.asin.x=x
Class.Math.atan.x=x
Class.Math.atan2.y=y

Class.Math.atan2.x=x
Class.Math.ceil.x=x
Class.Math.cos.x=x
Class.Math.exp.x=x
Class.Math.floor.x=x
Class.Math.log.x=x
Class.Math.max.args=参数

Class.Math.min.args=参数

Class.Math.pow.x=x
Class.Math.pow.y=y

Class.Math.round.x=x
Class.Math.sin.x=x
Class.Math.sqrt.x=x
Class.Math.tan.x=x


#6. Number
Class.Number=数值函数

Class.Number.toolTip=数字常量的数字数据和占位符的对象表示

Class.Number.Number=数值

Class.Number.Number.toolTip=返回 ToNumber(value) 计算的数值

Class.Number.MAX_VALUE=MAX_VALUE
Class.Number.MAX_VALUE.toolTip=数值类型的最大正有限值约等于 1.79E+308

Class.Number.MIN_VALUE=MIN_VALUE
Class.Number.MIN_VALUE.toolTip=数值类型的最小负值约等于 5.00E-324

Class.Number.NaN=NaN

Class.Number.NaN.toolTip=非数值

Class.Number.NEGATIVE_INFINITY=NEGATIVE_INFINITY
Class.Number.NEGATIVE_INFINITY.toolTip=返回可以用 JScipt 表示并小于最小负数 (-Number.MAX_VALUE) 的值

Class.Number.POSITIVE_INFINITY=POSITIVE_INFINITY
Class.Number.POSITIVE_INFINITY.toolTip=返回可以用 JScript 表示并大于最大数 (Number.MAX_VALUE) 的值

Class.Number.toExponential=toExponential

Class.Number.toExponential.toolTip=返回包含以指数表示法表示的数字的字符串

Class.Number.toFixed=toFixed

Class.Number.toFixed.toolTip=返回以定点表示法表示的字符串

Class.Number.toLocaleString=toLocaleString
Class.Number.toLocaleString.toolTip=返回与主机环境的当前区域相应的字符串值

Class.Number.toPrecision=toPrecision

Class.Number.toPrecision.toolTip=返回包含以指示表示法或定点表示法表示、带指定的数字的数字的字符串

Class.Number.toString=toString
Class.Number.toString.toolTip=返回对象的字符串表示

Class.Number.valueOf=valueOf
Class.Number.valueOf.toolTip=返回指定的对象的基元值

Class.Number.Number.value=值

Class.Number.toExpoential.digits=数位

Class.Number.toFixed.digits=数位

Class.Number.toPrecision.precision=精度

Class.Number.toString.radix=基数

#7. Object
Class.Object=对象函数

Class.Object.toolTip=对象类型的成员

Class.Object.Object=对象

Class.Object.Object.toolTip=创建对象

Class.Object.hasOwnProperty=hasOwnProperty

Class.Object.hasOwnProperty.toolTip=返回表示对象是否有指定的名称的属性的布尔值

Class.Object.isPrototypeOf=isPrototypeOf

Class.Object.isPrototypeOf.toolTip=返回表示其它对象的原型链中是否有对象的布尔值

Class.Object.propertyIsEnumerable=propertyIsEnumerable
Class.Object.propertyIsEnumerable.toolTip=返回表示对象是否有指定的属性以及对象是否可枚举的布尔值

Class.Object.toLocaleString=toLocaleString
Class.Object.toLocaleString.toolTip=返回使用当前区域转换为字符串的日期

Class.Object.toString=toString
Class.Object.toString.toolTip=返回对象的字符串表示

Class.Object.valueOf=valueOf
Class.Object.valueOf.toolTip=返回使用当前区域转换为字符串的日期

Class.Object.Object.value=值

Class.Object.hasOwnProperty.propname=propname

Class.Object.isPrototypeOf.o=o

Class.Object.propertyIsEnumerable.propname=propname

#8. RegExp
Class.RegExp=RegExp 函数

Class.RegExp.toolTip=支持简单的常规表达式

Class.RegExp.RegExp=RegExp

Class.RegExp.RegExp.toolTip=将该语法应用于输入模式字符串

Class.RegExp.global=全局

Class.RegExp.global.toolTip=设置或返回一个布尔值，该值指示模式应与整个搜索字符串中的所有符合值匹配，还是仅与第一个符合值匹配

Class.RegExp.ignoreCase=ignoreCase
Class.RegExp.ignoreCase.toolTip=设置或返回一个布尔值，该值指示模式搜索是否区分大小写

Class.RegExp.lastIndex=lastIndex
Class.RegExp.lastIndex.toolTip=一个整数，它指定开始下一个匹配的字符串位置

Class.RegExp.source=源

Class.RegExp.source.toolTip=表示当前常规表达式的模式形式的字符串

Class.RegExp.exec=exec

Class.RegExp.exec.toolTip=根据常规表达式执行字符串的常规表达式匹配

Class.RegExp.test=检验

Class.RegExp.test.toolTip=根据指定的字符串执行常规表达式搜索并返回一个布尔值，表示是否找到模式匹配项

Class.RegExp.toString=toString
Class.RegExp.toString.toolTip=转换为表示当前常规表达式的模式形式的字符串

Class.RegExp.RegExp.pattern=模式

Class.RegExp.RegExp.attributes=属性

Class.RegExp.exec.string=字符串

Class.RegExp.test.string=字符串

#9.  ElementDefn
Class.ElementDefn=ElementDefn 类

Class.ElementDefn.toolTip=为报表元素提供设计信息

Class.ElementDefn.name=名称

Class.ElementDefn.name.toolTip=提供元素的名称

Class.ElementDefn.extendsElement=extendsElement
Class.ElementDefn.extendsElement.toolTip=提供此元素扩展的父元素的名称

Class.ElementDefn.propertyDefns=propertyDefns
Class.ElementDefn.propertyDefns.toolTip=属性定义数组

Class.ElementDefn.slotDefns=slotDefns
Class.ElementDefn.slotDefns.toolTip=属性框定义数组

Class.ElementDefn.property=[anyProperty]
Class.ElementDefn.property.toolTip=提供具有此名称的设计时属性的值

Class.ElementDefn.properties=属性

Class.ElementDefn.properties.toolTip=按属性名称索引的属性值的数组

Class.ElementDefn.slots=属性框

Class.ElementDefn.slots.toolTip=许多元素为容器：它们包含其它报表元素

Class.ElementDefn.allowsUserProperties=allowsUserProperties
Class.ElementDefn.allowsUserProperties.toolTip=元素允许用户属性则为真，否则为假

Class.ElementDefn.hasStyle=hasStyle
Class.ElementDefn.hasStyle.toolTip=元素具有样式则为真，否则为假

Class.ElementDefn.container=容器

Class.ElementDefn.container.toolTip=返回包含此元素的元素定义（如果有）

Class.ElementDefn.containerSlot=containerSlot
Class.ElementDefn.containerSlot.toolTip=获取包含此元素的属性框的名称


#10. ElementState

Class.ElementState=ElementState
Class.ElementState.toolTip=表示报表元素的运行时声明

Class.ElementState.design=设计

Class.ElementState.design.toolTip=为此实例返回元素定义对象

Class.ElementState.property=属性

Class.ElementState.property.toolTip=允许应用程序获取或设置具有指定名称的报表元素属性的运行时值

Class.ElementState.container=容器

Class.ElementState.container.toolTip=返回包含此元素的元素声明对象（如果有）

Class.ElementState.slot=属性框

Class.ElementState.slot.toolTip=提供对此元素包含的元素的访问权

#11. Color
Class.Color=Color 类

Class.Color.toolTip=提供处理颜色值（RGB 值）的方法

Class.Color.transparent=透明

Class.Color.transparent.toolTip=属性设置为 CSS 透明值则返回真

Class.Color.rgb=RGB

Class.Color.rgb.toolTip=获取 RGB 值形式的颜色

Class.Color.value=值

Class.Color.value.toolTip=获取名称（如果设置为名称）或 CSS RGB 字符串（如果设置为 RGB）形式的颜色

Class.Color.cssValue=cssValue
Class.Color.cssValue.toolTip=获取可在 CSS 中设置的形式的颜色：CSS 颜色名称或 RGB 值


#12. Dimension
Class.Dimension=Dimension 类

Class.Dimension.toolTip=维属性对象处理 CSS 维属性的分析

Class.Dimension.value=值

Class.Dimension.value.toolTip=返回 CSS 兼容的维值

Class.Dimension.measure=度量

Class.Dimension.measure.toolTip=获取维的度量部分；如果使用常量定义维，则为空

Class.Dimension.units=单位

Class.Dimension.units.toolTip=获取维的单位部分；如果使用常量定义维，则为空

#13. PropertyDefn
Class.PropertyDefn=PropertyDefn 类

Class.PropertyDefn.toolTip=属性定义对象提供有关 BIRT 属性或用户定义属性的元数据信息

Class.PropertyDefn.isUserProperty=isUserProperty
Class.PropertyDefn.isUserProperty.toolTip=如果是用户定义的属性则为真；如果是系统定义的属性则为假

Class.PropertyDefn.name=名称

Class.PropertyDefn.name.toolTip=内部属性名称

Class.PropertyDefn.group=组

Class.PropertyDefn.group.toolTip=包含属性的组的名称

Class.PropertyDefn.canInherit=canInherit
Class.PropertyDefn.canInherit.toolTip=属性可继承则为真，否则为假

Class.PropertyDefn.isStyle=isStyle
Class.PropertyDefn.isStyle.toolTip=属性为元素样式的一部分则为真，否则为假

Class.PropertyDefn.type=类型

Class.PropertyDefn.type.toolTip=属性类型

Class.PropertyDefn.choices=选择

Class.PropertyDefn.choices.toolTip=属性选项数组

Class.PropertyDefn.defaultValue=defaultValue
Class.PropertyDefn.defaultValue.toolTip=属性的默认值

#14. Report
Class.Report=Report 类

Class.Report.toolTip=提供对有关报表及报表运行上下文的所有信息的访问权

Class.Report.design=设计

Class.Report.design.toolTip=返回描述报表设计的 ReportDefn 对象

Class.Report.document=文档

Class.Report.document.toolTip=返回一个报表文档对象，它提供有关工厂中正在创建或展示引擎中正在读取的报表文档的信息

Class.Report.params=参数

Class.Report.params.toolTip=返回传递至报表的参数值的对象数组

Class.Report.dataSets=数据集

Class.Report.dataSets.toolTip=返回报表中定义的数据集的数组

Class.Report.dataSources=dataSources
Class.Report.dataSources.toolTip=返回报表中定义的数据源（连接）的数组

Class.Report.config=配置

Class.Report.config.toolTip=返回可用于报表的配置变量的数组

Class.Report.reportContext=reportContext
Class.Report.reportContext.toolTip=与此报表相关的上下文

#15. ReportDoc
Class.ReportDoc=ReportDoc

Class.ReportDoc.toolTip=提供有关所产生或所查看的报表的信息

Class.ReportDoc.design=设计

Class.ReportDoc.design.toolTip=为此实例返回根设计对象

Class.ReportDoc.fileName=fileName
Class.ReportDoc.fileName.toolTip=此文件写入的文件（如果有）的名称

Class.ReportDoc.destination=目标

Class.ReportDoc.destination.toolTip=文件系统或临时目录等目的地种类

Class.ReportDoc.body=正文

Class.ReportDoc.body.toolTip=主体内的区域的列表


#16. ReportDefn

Class.ReportDefn=ReportDefn
Class.ReportDefn.toolTip=提供有关报表的全面设计信息

Class.ReportDefn.findStyle=findStyle
Class.ReportDefn.findStyle.toolTip=根据样式名称查找样式

Class.ReportDefn.findStyle.styleName=styleName
Class.ReportDefn.findReportItem=findReportItem
Class.ReportDefn.findReportItem.toolTip=根据报表项名称查找报表项

Class.ReportDefn.findReportItem.itemName=itemName
Class.ReportDefn.findDataSource=findDataSource
Class.ReportDefn.findDataSource.toolTip=根据数据源名称查找数据源

Class.ReportDefn.findDataSource.sourceName=sourceName
Class.ReportDefn.findDataSet=findDataSet
Class.ReportDefn.findDataSet.toolTip=根据数据集名称查找数据集

Class.ReportDefn.findDataSet.dataSetName=dataSetName
Class.ReportDefn.findParameter=findParameter
Class.ReportDefn.findParameter.toolTip=根据参数名称查找参数

Class.ReportDefn.findParameter.parameterName=parameterName


#17. DataSet
Class.DataSet=DataSet 类

Class.DataSet.toolTip=提供有关数据集的设计和运行时信息

Class.DataSet.row=行

Class.DataSet.row.toolTip=表示当前最内层的行

Class.DataSet.rows=行

Class.DataSet.rows.toolTip=允许访问此层次结构内的行

Class.DataSet.queryText=queryText
Class.DataSet.queryText.toolTip=提供要发送至 ODA 数据源的查询文本

Class.DataSet.columnDefns=columnDefns
Class.DataSet.columnDefns.toolTip=列定义数组

Class.DataSet.schema=架构

Class.DataSet.schema.toolTip=一个 DataSetParamMetadata 对象，它提供有关 ODA 数据源的架构信息

Class.DataSet.inputParams=inputParams
Class.DataSet.inputParams.toolTip=提供对作为参数数组的输入参数的值的访问权

Class.DataSet.outputParams=outputParams
Class.DataSet.outputParams.toolTip=数据集的输出参数值的数组

Class.DataSet.extensionProperties=extensionProperties
Class.DataSet.extensionProperties.toolTip=为 ODA 数据集提供 ODA 公共属性

Class.DataSet.privateProperties=privateProperties
Class.DataSet.privateProperties.toolTip=为 ODA 数据集提供私有属性

Class.DataSet.dataSource=DataSource
Class.DataSet.dataSource.toolTip=返回此数据集使用的 DataSource 对象。


#18. DataRow
Class.DataRow=DataRow 类

Class.DataRow.toolTip=表示从数据集提取的一行

Class.DataRow.DataRow=DataRow

Class.DataRow.DataRow.toolTip=创建提供了数据集的空白数据行或作为另一个数据行副本的数据行

Class.DataRow.DataRow.ds=ds

Class.DataRow.DataRow.r=r

Class.DataRow.column=列

Class.DataRow.column.toolTip=返回提供了列名称的列的值

Class.DataRow.rowNo=rowNo

Class.DataRow.rowNo.toolTip=行在结果集内的位置

Class.DataRow.dataSet=dataSet

Class.DataRow.dataSet.toolTip=返回创建行的 DataSet 对象

Class.DataRow.schema=架构

Class.DataRow.schema.toolTip=返回数据行的列描述数组

Class.DataRow.columnDefns=columnDefns
Class.DataRow.columnDefns.toolTip=它是定义列元数据的 ColumnDefn 的数组。它是 dataSet.columnDefns 的快捷方式。

#19. ColumnDefn
Class.ColumnDefn=ColumnDefn 类

Class.ColumnDefn.toolTip=描述数据行列的元数据

Class.ColumnDefn.index=索引

Class.ColumnDefn.index.toolTip=返回数据集的结果集数据行中的列的索引

Class.ColumnDefn.name=名称

Class.ColumnDefn.name.toolTip=返回数据集定义的列的名称

Class.ColumnDefn.type=类型

Class.ColumnDefn.type.toolTip=返回列的数据类型

Class.ColumnDefn.expr=到期

Class.ColumnDefn.expr.toolTip=列可以使用 BIRT 表达式来定义

Class.ColumnDefn.INTEGER=整型

Class.ColumnDefn.INTEGER.toolTip=用于整数列的常量

Class.ColumnDefn.FLOAT=浮点

Class.ColumnDefn.FLOAT.toolTip=用于浮点列的常量

Class.ColumnDefn.DECIMAL=小数

Class.ColumnDefn.DECIMAL.toolTip=用于定点精度小数的常量

Class.ColumnDefn.BOOLEAN=布尔

Class.ColumnDefn.BOOLEAN.toolTip=用于布尔值的常量

Class.ColumnDefn.STRING=字符串

Class.ColumnDefn.STRING.toolTip=用于字符串列的常量

Class.ColumnDefn.BLOB=BLOB

Class.ColumnDefn.BLOB.toolTip=用于二进制大对象 (BLOB) 列的常量


#20. DataSetParamMetaData
Class.DataSetParamMetaData=DataSetParamMetaData 类

Class.DataSetParamMetaData.toolTip=提供有关 ODA 数据集的输入和输出参数的信息

Class.DataSetParamMetaData.description=描述

Class.DataSetParamMetaData.description.toolTip=ODA 提供者提供的数据集描述

Class.DataSetParamMetaData.resultSets=resultSets
Class.DataSetParamMetaData.resultSets.toolTip=描述 ODA 数据集返回的每个结果集的 ResultSetDefn 对象数组

Class.DataSetParamMetaData.inputParams=inputParams
Class.DataSetParamMetaData.inputParams.toolTip=DataSetInputParam 对象形式的输入参数定义数组

Class.DataSetParamMetaData.outputParams=outputParams
Class.DataSetParamMetaData.outputParams.toolTip=DataSetOutputParam 对象形式的输出参数定义数组

#21. ResultSetDefn
Class.ResultSetDefn=ResultSetDefn

Class.ResultSetDefn.toolTip=提供列定义数组

Class.ResultSetDefn.name=名称

Class.ResultSetDefn.name.toolTip=结果集的名称

Class.ResultSetDefn.columns=列

Class.ResultSetDefn.columns.toolTip=ODA 数据集具有它们返回的一组固定列

Class.ResultSetDefn.dataSet=dataSet

Class.ResultSetDefn.dataSet.toolTip=反向引用提供此结果集的数据集

#22. ResultSetColumn
Class.ResultSetColumn=ResultSetColumn

Class.ResultSetColumn.toolTip=描述 ODA 结果集内的一个列

Class.ResultSetColumn.name=名称

Class.ResultSetColumn.name.toolTip=数据行列的唯一外部名称

Class.ResultSetColumn.position=位置

Class.ResultSetColumn.position.toolTip=结果集内某个列的以 1 为基的索引位置（从左到右的顺序）

Class.ResultSetColumn.displayName=displayName
Class.ResultSetColumn.displayName.toolTip=列的业务名称

Class.ResultSetColumn.nativeTypeCode=nativeTypeCode
Class.ResultSetColumn.nativeTypeCode.toolTip=列的本地数据类型代码

Class.ResultSetColumn.nativeTypeName=nativeTypeName
Class.ResultSetColumn.nativeTypeName.toolTip=本地数据类型的显示名称

Class.ResultSetColumn.displayLength=displayLength
Class.ResultSetColumn.displayLength.toolTip=数据值的默认显示长度

Class.ResultSetColumn.displayFormat=displayFormat
Class.ResultSetColumn.displayFormat.toolTip=一个字符串，指示在报表输出中显示此数据的首选格式

Class.ResultSetColumn.horizAlign=horizAlign
Class.ResultSetColumn.horizAlign.toolTip=一个枚举值，指示在默认情况下，列值应显示为左对齐、居中、右对齐还是自动对齐

Class.ResultSetColumn.heading=标题

Class.ResultSetColumn.heading.toolTip=任意格式的字符串，表明列标题中的列

Class.ResultSetColumn.description=描述

Class.ResultSetColumn.description.toolTip=任意格式的字符串，提供列的扩展描述

Class.ResultSetColumn.helpText=helpText
Class.ResultSetColumn.helpText.toolTip=任意格式的字符串，提供列的扩展描述

Class.ResultSetColumn.lineage=沿袭

Class.ResultSetColumn.lineage.toolTip=任意格式的字符串，指定列值的最初来源

Class.ResultSetColumn.axis=轴

Class.ResultSetColumn.axis.toolTip=适用于从多维数据源检索列的情况

Class.ResultSetColumn.axisType=axisType
Class.ResultSetColumn.axisType.toolTip=其值为“维成员”、“维属性”或“度量”之一

Class.ResultSetColumn.onColumnLayout=onColumnLayout

Class.ResultSetColumn.onColumnLayout.toolTip=关于此列在报表输出中如何布局的提示

Class.ResultSetColumn.DIMENSION_MEMBER=DIMENSION_MEMBER
Class.ResultSetColumn.DIMENSION_MEMBER.toolTip=axisType 属性的选项

Class.ResultSetColumn.DIMENSION_ATTRIBUTE=DIMENSION_ATTRIBUTE
Class.ResultSetColumn.DIMENSION_ATTRIBUTE.toolTip=axisType 属性的选项

Class.ResultSetColumn.MEASURE=MEASURE

Class.ResultSetColumn.MEASURE.toolTip=axisType 属性的选项


#23. DataSetInputParam
Class.DataSetInputParam=DataSetInputParam 类

Class.DataSetInputParam.toolTip=描述 ODA 数据集的一个输入参数

Class.DataSetInputParam.name=名称

Class.DataSetInputParam.name.toolTip=输入参数的唯一名称

Class.DataSetInputParam.displayName=DataSetInputParam

Class.DataSetInputParam.displayName.toolTip=输入参数的业务名称

Class.DataSetInputParam.description=描述

Class.DataSetInputParam.description.toolTip=任意格式的字符串，提供参数的扩展描述

Class.DataSetInputParam.nativeTypeCode=nativeTypeCode
Class.DataSetInputParam.nativeTypeCode.toolTip=参数的本地数据类型代码

Class.DataSetInputParam.nativeTypeName=nativeTypeName
Class.DataSetInputParam.nativeTypeName.toolTip=本地数据类型的显示名称

Class.DataSetInputParam.recordDefn=recordDefn
Class.DataSetInputParam.recordDefn.toolTip=复参数中每个字段的定义



#24. DataSetOutputParam
Class.DataSetOutputParam=DataSetOutputParam 类

Class.DataSetOutputParam.toolTip=描述 ODA 数据集的一个输出参数

Class.DataSetOutputParam.name=名称

Class.DataSetOutputParam.name.toolTip=输出参数的唯一名称

Class.DataSetOutputParam.displayName=displayName
Class.DataSetOutputParam.displayName.toolTip=输出参数的业务名称

Class.DataSetOutputParam.description=描述

Class.DataSetOutputParam.description.toolTip=任意格式的字符串，提供参数的扩展描述

Class.DataSetOutputParam.nativeTypeCode=nativeTypeCode
Class.DataSetOutputParam.nativeTypeCode.toolTip=参数的本地数据类型代码

Class.DataSetOutputParam.nativeTypeName=nativeTypeName
Class.DataSetOutputParam.nativeTypeName.toolTip=本地数据类型的显示名称

Class.DataSetOutputParam.recordDefn=recordDefn
Class.DataSetOutputParam.recordDefn.toolTip=复参数中每个字段的定义


#25. ItemState

Class.ItemState=ItemState
Class.ItemState.toolTip=表示报表元素的运行时声明

Class.ItemState.row=行

Class.ItemState.row.toolTip=表示当前最内层的行

Class.ItemState.rows=行

Class.ItemState.rows.toolTip=允许访问此层次结构内的行


#26. Total
Class.Total=总计（聚合)函数

Class.Total.toolTip=聚合函数的容器

Class.Total.OVERALL=OVERALL

Class.Total.OVERALL.toolTip=用于请求数据集总计的组参数的常量

Class.Total.NO_FILTER=NO_FILTER
Class.Total.NO_FILTER.toolTip=一个常量，用于聚合函数的筛选参数，指明不存在筛选器

Class.Total.sum=sum

Class.Total.sum.toolTip=数值序列的总和

Class.Total.runningSum=runningSum

Class.Total.runningSum.toolTip=与求和类似，但是显示截至报表中指定位置的运行总计

Class.Total.count=计数

Class.Total.count.toolTip=行计数

Class.Total.runningCount=runningCount
Class.Total.runningCount.toolTip=当前组中正在运行的行数

Class.Total.countDistinct=countDistinct
Class.Total.countDistinct.toolTip=特异值计数

Class.Total.max=max

Class.Total.max.toolTip=数值、字符串或日期序列的最大值

Class.Total.min=min

Class.Total.min.toolTip=数值、字符串或日期序列的最小值

Class.Total.ave=ave

Class.Total.ave.toolTip=数值、字符串或日期序列的平均值

Class.Total.weightedAve=weightedAve

Class.Total.weightedAve.toolTip=数值序列的加权平均值

Class.Total.weightedAve.weight=权重

Class.Total.movingAve=movingAve

Class.Total.movingAve.toolTip=数值序列的运行平均值，其中用户指定计算平均值时要考虑的值个数

Class.Total.movingAve.window=窗口

Class.Total.median=中位数

Class.Total.median.toolTip=数值序列的数学中值

Class.Total.mode=众数

Class.Total.mode.toolTip=值序列的数学众数

Class.Total.stdDev=stdDev

Class.Total.stdDev.toolTip=数值序列的数学标准偏差

Class.Total.variance=方差

Class.Total.variance.toolTip=数值序列的数学方差

Class.Total.first=第一

Class.Total.first.toolTip=序列中的第一个值

Class.Total.last=最后

Class.Total.last.toolTip=序列中的最后一个值

Class.Total.rank=等级

Class.Total.rank.toolTip=以一个系列返回一个数、字符串或日期/时间值的等级，从 1 开始

Class.Total.rank.ascending=升序

Class.Total.percentRank=percentRank
Class.Total.percentRank.toolTip=返回一个值的百分比等级。返回值在范围 [0..1] 内

Class.Total.percentile=百分位

Class.Total.percentile.toolTip=返回百分位值。pct 范围从 0 到 1。如果 pct 为 0，返回值等于 Total.min(expr)。如果 pct 为 1，返回值等于 Total.max(expr)

Class.Total.percentile.pct=百分比

Class.Total.quartile=四分位数

Class.Total.quartile.toolTip=返回四分位值。quart 是 1 到 4 范围内的整数。如果 quart 为 1，返回值等于 Total.percentile(expr,0.25)；如果 quart 为 2，返回值等于 Total.percentile(expr,0.5)，依此类推

Class.Total.quartile.quart=四分位

Class.Total.percentSum=percentSum

Class.Total.percentSum.toolTip=Total.Equals 的百分比等于 expr/Total.sum(expr, [group])

Class.Total.isTopN=isTopN

Class.Total.isTopN.toolTip=如果值为组中前 n 个值之一，返回真。否则返回假

Class.Total.isTopN.n=n
Class.Total.isTopNPercent=isTopNPercent

Class.Total.isTopNPercent.toolTip=如果值为组中前 (pct * 100)% 个值之一，返回真。否则返回假

Class.Total.isTopNPercent.pct=百分比

Class.Total.isBottomN=isBottomN

Class.Total.isBottomN.toolTip=如果值为组中后 n 个值之一，返回真。否则返回假

Class.Total.isBottomN.n=n
Class.Total.isBottomNPercent=isBottomNPercent

Class.Total.isBottomNPercent.toolTip=如果值为组中后 (pct * 100)% 个值之一，返回真。否则返回假

Class.Total.isBottomNPercent.pct=百分比

#27. Finance
Class.Finance=财务函数

Class.Finance.toolTip=静态财务功能的集合

Class.Finance.ddb=ddb

Class.Finance.ddb.toolTip=某项资产在指定的单个期间内使用双倍余额递减法计算的折旧

Class.Finance.ddb.initialCost=initialCost
Class.Finance.ddb.salvageValue=salvageValue
Class.Finance.ddb.assetLifespan=assetLifespan
Class.Finance.ddb.singlePeriod=singlePeriod
Class.Finance.sln=sln

Class.Finance.sln.toolTip=某项资产在单个期间内的直线折旧

Class.Finance.sln.initialCost=initialCost
Class.Finance.sln.salvageValue=salvageValue
Class.Finance.sln.assetLifespan=assetLifespan 
Class.Finance.syd=syd

Class.Finance.syd.toolTip=某项资产在指定期间内的年限总和折旧

Class.Finance.syd.initialCost=initialCost
Class.Finance.syd.salvageValue=salvageValue
Class.Finance.syd.assetLifespan=assetLifespan
Class.Finance.syd.singlePeriod=singlePeriod
Class.Finance.fv=fv

Class.Finance.fv.toolTip=根据固定利率和等额分期付款方法计算的年金未来值

Class.Finance.fv.ratePerPeriod=ratePerPeriod

Class.Finance.fv.numberPayPeriods=numberPayPeriods

Class.Finance.fv.eachPmt=eachPmt

Class.Finance.fv.presentValue=presentValue
Class.Finance.fv.whenDue=whenDue

Class.Finance.ipmt=ipmt

Class.Finance.ipmt.toolTip=根据固定利率和等额分期付款方法计算的年金在指定期间内的利息

Class.Finance.ipmt.ratePerPeriod=ratePerPeriod

Class.Finance.ipmt.singlePeriod=singlePeriod
Class.Finance.ipmt.numberPayPeriods=numberPayPeriods

Class.Finance.ipmt.presentValue=presentValue
Class.Finance.ipmt.futureValue=futureValue
Class.Finance.ipmt.whenDue=whenDue

Class.Finance.nper=nper

Class.Finance.nper.toolTip=根据固定利率和等额分期付款方法计算的年金期数

Class.Finance.nper.ratePerPeriod=ratePerPeriod

Class.Finance.nper.eachPmt=eachPmt

Class.Finance.nper.presentValue=presentValue
Class.Finance.nper.futureValue=futureValue
Class.Finance.nper.whenDue=whenDue

Class.Finance.pmt=pmt

Class.Finance.pmt.toolTip=根据固定利率和等额分期付款方法计算的年金付款

Class.Finance.pmt.ratePerPeriod=ratePerPeriod

Class.Finance.pmt.numberPayPeriods=numberPayPeriods

Class.Finance.pmt.presentValue=presentValue
Class.Finance.pmt.futureValue=futureValue
Class.Finance.pmt.whenDue=whenDue

Class.Finance.ppmt=ppmt

Class.Finance.ppmt.toolTip=根据固定利率和等额分期付款方法计算的年金在指定期间内的本金

Class.Finance.ppmt.ratePerPeriod=ratePerPeriod

Class.Finance.ppmt.singlePeriod=singlePeriod
Class.Finance.ppmt.numberPayPeriods=numberPayPeriods

Class.Finance.ppmt.presentValue=presentValue
Class.Finance.ppmt.futureValue=futureValue
Class.Finance.ppmt.whenDue=whenDue

Class.Finance.pv=PV

Class.Finance.pv.toolTip=根据固定利率和等额分期未来付款方法计算的年金现值

Class.Finance.pv.ratePerPeriod=ratePerPeriod

Class.Finance.pv.numberPayPeriods=numberPayPeriods

Class.Finance.pv.eachPmt=eachPmt

Class.Finance.pv.futureValue=futureValue
Class.Finance.pv.whenDue=whenDue

Class.Finance.rate=比率

Class.Finance.rate.toolTip=年金按期间平均的利率

Class.Finance.rate.numberPayPeriods=numberPayPeriods

Class.Finance.rate.eachPmt=eachPmt

Class.Finance.rate.presentValue=presentValue
Class.Finance.rate.futureValue=futureValue
Class.Finance.rate.whenDue=whenDue

Class.Finance.rate.startingGuess=startingGuess
Class.Finance.irr=irr

Class.Finance.irr.toolTip=现有数组内一系列定期现金流（付款和收款）的内部回报率

Class.Finance.irr.cashArray=cashArray
Class.Finance.irr.startingGuess=startingGuess
Class.Finance.npv=npv

Class.Finance.npv.toolTip=一系列定期现金流（正和负）按指定利率计算的净现值

Class.Finance.npv.rate=比率

Class.Finance.npv.cashArray=cashArray 
Class.Finance.runningnpv=npv

Class.Finance.runningnpv.toolTip=一系列定期现金流（正和负）按指定利率计算的净现值

Class.Finance.runningnpv.rate=比率

Class.Finance.runningnpv.cashArray=cashArray 
Class.Finance.mirr=mirr

Class.Finance.mirr.toolTip=现有数组内一系列定期现金流（付款和收款）的修改内部回报率

Class.Finance.mirr.cashArray=cashArray
Class.Finance.mirr.rate=比率

Class.Finance.percent=百分比

Class.Finance.percent.toolTip=计算两个数的百分比

Class.Finance.percent.denom=denom

Class.Finance.percent.num=数字

Class.Finance.percent.rate=比率

#28. DateTimeSpan
Class.DateTimeSpan=DateTimeSpan 函数

Class.DateTimeSpan.toolTip=用于处理两个日期或时间之间跨度的函数集合

Class.DateTimeSpan.years=年

Class.DateTimeSpan.years.toolTip=返回两个日期之间的整年数

Class.DateTimeSpan.years.startDate=startDate
Class.DateTimeSpan.years.endDate=endDate
Class.DateTimeSpan.months=月

Class.DateTimeSpan.months.toolTip=返回两个日期之间的整月数

Class.DateTimeSpan.months.startDate=startDate
Class.DateTimeSpan.months.endDate=endDate
Class.DateTimeSpan.days=日

Class.DateTimeSpan.days.toolTip=返回两个日期之间的天数

Class.DateTimeSpan.days.startDate=startDate
Class.DateTimeSpan.days.endDate=endDate
Class.DateTimeSpan.hours=小时

Class.DateTimeSpan.hours.toolTip=返回两个日期/时间之间的小时数

Class.DateTimeSpan.hours.startDate=startDate
Class.DateTimeSpan.hours.endDate=endDate
Class.DateTimeSpan.minutes=分钟

Class.DateTimeSpan.minutes.toolTip=返回两个日期/时间之间的分钟数

Class.DateTimeSpan.minutes.startDate=startDate
Class.DateTimeSpan.minutes.endDate=endDate
Class.DateTimeSpan.seconds=秒

Class.DateTimeSpan.seconds.toolTip=返回两个日期/时间之间的秒数

Class.DateTimeSpan.seconds.startDate=startDate
Class.DateTimeSpan.seconds.endDate=endDate

Class.DateTimeSpan.addDate=addDate
Class.DateTimeSpan.addDate.toolTip=在某个日期上增加一个年数、月数或天数

Class.DateTimeSpan.addDate.startDate=startDate
Class.DateTimeSpan.addDate.years=年

Class.DateTimeSpan.addDate.months=月

Class.DateTimeSpan.addDate.days=日



Class.DateTimeSpan.addTime=addTime
Class.DateTimeSpan.addTime.toolTip=在某个日期/时间上增加一个小时数、分钟数或秒数

Class.DateTimeSpan.addTime.startDate=startDate
Class.DateTimeSpan.addTime.hours=小时

Class.DateTimeSpan.addTime.minutes=分钟

Class.DateTimeSpan.addTime.seconds=秒


Class.DateTimeSpan.subDate=subDate
Class.DateTimeSpan.subDate.toolTip=从某个日期减去一个年数、月数或天数

Class.DateTimeSpan.subDate.startDate=startDate
Class.DateTimeSpan.subDate.years=年

Class.DateTimeSpan.subDate.months=月

Class.DateTimeSpan.subDate.days=日



Class.DateTimeSpan.subTime=subTime
Class.DateTimeSpan.subTime.toolTip=从某个日期减去一个小时数、分钟数或秒数

Class.DateTimeSpan.subTime.startDate=startDate
Class.DateTimeSpan.subTime.hours=小时

Class.DateTimeSpan.subTime.minutes=分钟

Class.DateTimeSpan.subTime.seconds=秒

#29.GlobalBirt
Class.GlobalBirt=全局变量

Class.GlobalBirt.toolTip=提供一些全局变量，使表达式和脚本能够访问数据行以及有关报表的信息

Class.GlobalBirt.datasets=Birt 数据集

Class.GlobalBirt.datasets.toolTip=返回报表中定义的数据集的数组

Class.GlobalBirt.params=Birt 参数

Class.GlobalBirt.params.toolTip=返回传递至报表的参数值的对象数组

Class.GlobalBirt.row=Birt 行

Class.GlobalBirt.row.toolTip=数据集或报表元素的当前数据行

Class.GlobalBirt.rows=Birt 行

Class.GlobalBirt.rows.toolTip=为当前上下文定义的行栈

Class.GlobalBirt.config=Birt 配置

Class.GlobalBirt.config.toolTip=返回可用于报表的配置变量数组。表示为名称/值对

Class.GlobalBirt.report=Birt Report

Class.GlobalBirt.report.toolTip=提供对有关报表及报表运行上下文的所有信息的访问权

Class.GlobalBirt.CurrentPage=当前页码

Class.GlobalBirt.CurrentPage.toolTip=返回当前页码。

Class.GlobalBirt.TotalPage=总页数

Class.GlobalBirt.TotalPage.toolTip=返回总页数。

Class.GlobalBirt.this=此对象

Class.GlobalBirt.this.toolTip=表示此对象

Class.GlobalBirt.appContext=AppContext
Class.GlobalBirt.appContext.toolTip=与此报表相关的上下文

#30 DataSource
Class.DataSource=DataSource 类

Class.DataSource.toolTip=数据源类提供对数据源的工厂时间状态的访问。DataSource 对象可通过使用它的 DataSet 对象来访问。

Class.DataSource.extensionID=extensionID

Class.DataSource.extensionID.toolTip=返回 ODA 驱动程序扩展所定义的 ODA 数据源的 extensionID 值。extensionID 唯一地标识 BIRT 环境中的数据源的类型。不能更新此属性。

Class.DataSource.isOpen=isOpen

Class.DataSource.isOpen.toolTip=返回表示是否已连接数据源的布尔值

Class.DataSource.extensionProperties=extensionProperties
Class.DataSource.extensionProperties.toolTip=为 ODA 数据源提供私有属性

#31. BirtComp
 
Class.BirtComp=BirtComp
Class.BirtComp.toolTip=提供 Birt 数据比较支持

Class.BirtComp.equalTo=equalsTo

Class.BirtComp.equalTo.toolTip=x 等于 y

Class.BirtComp.equalTo.x=x
Class.BirtComp.equalTo.y=y

Class.BirtComp.greaterThan=greaterThan
Class.BirtComp.greaterThan.toolTip=x 大于 y

Class.BirtComp.greaterThan.x=x
Class.BirtComp.greaterThan.y=y

Class.BirtComp.lessThan=lessThan
Class.BirtComp.lessThan.toolTip=x 小于 y

Class.BirtComp.lessThan.x=x
Class.BirtComp.lessThan.y=y

Class.BirtComp.greaterOrEqual=greaterOrEqual

Class.BirtComp.greaterOrEqual.toolTip=x 大于或等于 y

Class.BirtComp.greaterOrEqual.x=x
Class.BirtComp.greaterOrEqual.y=y

Class.BirtComp.lessOrEqual=较小或相等

Class.BirtComp.lessOrEqual.toolTip=x 小于或等于 y

Class.BirtComp.lessOrEqual.x=x
Class.BirtComp.lessOrEqual.y=y

Class.BirtComp.notEqual=notEqual
Class.BirtComp.notEqual.toolTip=x 不等于 y

Class.BirtComp.notEqual.x=x
Class.BirtComp.notEqual.y=y

Class.BirtComp.between=介于

Class.BirtComp.between.toolTip=值大于或等于 r1 且小于或等于 r2

Class.BirtComp.between.val=值

Class.BirtComp.between.r1=r1

Class.BirtComp.between.r2=r2

Class.BirtComp.like=类似

Class.BirtComp.like.toolTip=x 与 SQL 模式 y 匹配

Class.BirtComp.like.x=x
Class.BirtComp.like.y=y

Class.BirtComp.match=匹配

Class.BirtComp.match.toolTip=x 与 Javascript 模式 y 匹配

Class.BirtComp.match.x=x
Class.BirtComp.match.y=y

Class.BirtComp.anyOf=anyOf

Class.BirtComp.anyOf.toolTip=值是否在下列参数内

Class.BirtComp.anyOf.val=值


###########################################################
# Structures
Structure.MapRule=MapRule
Structure.HighlightRule=HighlightRule
Structure.CustomColor=CustomColor
Structure.SearchKey=SearchKey

Structure.Action=操作

Structure.UserProperty=UserProperty
Structure.SortKey=排序关键字

Structure.FilterCondition=筛选条件

Structure.ConfigVar=配置变量

Structure.HideRule=隐藏

Structure.IncludeScript=包含脚本

Structure.EmbeddedImage=嵌入图像

Structure.OdaProperty=ODA 属性

Structure.ParamBinding=参数绑定

Structure.ResultSetColumn=列

Structure.ComputedColumn=计算列

Structure.ColumnHint=列提示

Structure.IncludedLibrary=包括的库

Structure.DataSetParam=数据集参数

Structure.PropertyMask=属性掩码

Structure.ExtendedProperty=扩展属性

Structure.CachedMetaData=缓存元数据

Structure.NumberFormatValue=数字格式值

Structure.StringFormatValue=字符串格式值

Structure.DateTimeFormatValue=日期/时间格式值

Structure.DateFormatValue = 日期格式值

Structure.TimeFormatValue = 时间格式值

Structure.UserMethod=用户方法

Structure.PropertyBinding=属性绑定

Structure.JoinCondition=联接条件

Structure.ParameterFormatValue=参数格式值

Structure.OdaDesignerState=Oda 设计器声明

Structure.ScriptLib=脚本库

Structure.IncludedCssStyleSheet=包括的 CSS 样式表

Structure.DimensionJoinCondition=维联接条件

Structure.FormatValue =格式类型

Structure.CalculationArgument=计算参数

#0.MapRule
Structure.MapRule.testExpr=TestExpression
Structure.MapRule.operator=运算符

Structure.MapRule.value1=值 1

Structure.MapRule.value2=值 2

Structure.MapRule.display=显示

Structure.MapRule.displayID=显示 ID

Structure.MapRule.isDesignTime=为设计时间
  
#1.HighlightRule
Structure.HighlightRule.operator=运算符

Structure.HighlightRule.isDesignTime=为设计时间

Structure.HighlightRule.value1=值 1

Structure.HighlightRule.value2=值 2

Structure.HighlightRule.testExpr = TestExpression
Structure.HighlightRule.strikethrough=删除线

Structure.HighlightRule.border.top.style=样式

Structure.HighlightRule.border.top.width=宽度

Structure.HighlightRule.border.top.color=颜色

Structure.HighlightRule.border.left.style=样式

Structure.HighlightRule.border.left.width=宽度

Structure.HighlightRule.border.left.color=颜色

Structure.HighlightRule.border.bottom.style=样式

Structure.HighlightRule.border.bottom.width=宽度

Structure.HighlightRule.border.bottom.color=颜色

Structure.HighlightRule.border.right.style=样式

Structure.HighlightRule.border.right.width=宽度

Structure.HighlightRule.border.right.color=颜色

Structure.HighlightRule.background.color=背景色

Structure.HighlightRule.background.image=背景图像

Structure.HighlightRule.backgroundRepeat=背景重复

Structure.HighlightRule.format.dateTime=日期时间格式

Structure.HighlightRule.format.number=数值格式

Structure.HighlightRule.format.number.align=数值对齐

Structure.HighlightRule.format.string=字符串格式

Structure.HighlightRule.font.family=字体

Structure.HighlightRule.font.size=字号

Structure.HighlightRule.font.style=字体样式

Structure.HighlightRule.font.weight=字重

Structure.HighlightRule.font.variant=字体变体

Structure.HighlightRule.font.color=颜色

Structure.HighlightRule.font.textUnderline=下划线

Structure.HighlightRule.font.textOverline=上划线

Structure.HighlightRule.font.textLineThrough=删除线

Structure.HighlightRule.padding.top=顶部间隙

Structure.HighlightRule.padding.left=左侧间隙

Structure.HighlightRule.padding.bottom=底部间隙

Structure.HighlightRule.padding.right=右侧间隙

Structure.HighlightRule.text.align=文本对齐

Structure.HighlightRule.text.indent=文本缩进

Structure.HighlightRule.text.transform=文本转换

Structure.HighlightRule.style=样式

Structure.HighlightRule.text.direction=文本方向

#2.CustomColor
Structure.CustomColor.name=名称

Structure.CustomColor.color=颜色

Structure.CustomColor.displayName=显示名称

Structure.CustomColor.displayNameID=显示名称关键字
  
#4.DrillThroughSearchKeys
Structure.SearchKey.expression=表达式
  
#5.UserProperty
Structure.UserProperty.name=名称

Structure.UserProperty.type=类型

Structure.UserProperty.isVisible=可见

Structure.UserProperty.displayNameID=显示名称关键字

Structure.UserProperty.displayName=显示名称

#6.ConfigVar
Structure.ConfigVar.name=名称

Structure.ConfigVar.value=值

#7. SortKey
Structure.SortKey.key=排序关键字

Structure.SortKey.direction=排序方向

Structure.SortKey.strength=排序强度

Structure.SortKey.locale=排序区域设置

#8. Hide
Structure.HideRule.format=格式

Structure.HideRule.valueExpr=表达式

#9. IncludeScript
Structure.IncludeScript.fileName=文件名

#10.EmbeddedImage
Structure.EmbeddedImage.name=名称

Structure.EmbeddedImage.type=类型

Structure.EmbeddedImage.data=数据

Structure.EmbeddedImage.libReference=库引用

#11.FilterCondition
Structure.FilterCondition.operator=运算符

Structure.FilterCondition.expr=表达式

Structure.FilterCondition.value1=值 1

Structure.FilterCondition.value2=值 2

Structure.FilterCondition.filterTarget=筛选目标

Structure.FilterCondition.isOptional=可选

Structure.FilterCondition.extensionName=扩展名

Structure.FilterCondition.extensionExprId=扩展表达式 Id

Structure.FilterCondition.pushDown=下推

Structure.FilterCondition.dynamicFilterParameter=动态筛选器参数

Structure.FilterCondition.type=类型

Structure.FilterCondition.updateAggregation=更新聚合
Structure.FilterCondition.customValue=自定义值

#10. PropertyMask
Structure.PropertyMask.name=名称

Structure.PropertyMask.mask=掩码

#11.OdaProperty
Structure.OdaProperty.name=名称

Structure.OdaProperty.value=值

#13. ParamBinding
Structure.ParamBinding.paramName=参数名称

Structure.ParamBinding.expression=表达式

#14. ResultSetColumn
Structure.ResultSetColumn.position=位置

Structure.ResultSetColumn.name=名称

Structure.ResultSetColumn.dataType=数据类型

Structure.ResultSetColumn.nativeDataType=本地数据类型代码

#15. ComputedColumn
Structure.ComputedColumn.name=列名

Structure.ComputedColumn.displayName=列显示名称

Structure.ComputedColumn.displayNameID=列显示名称 ID

Structure.ComputedColumn.expression=表达式

Structure.ComputedColumn.dataType=数据类型

Structure.ComputedColumn.aggregateOn=AggregateOn 表达式

Structure.ComputedColumn.aggregateFunction=AggregateOn 函数

Structure.ComputedColumn.arguments=AggregateOn 函数参数

Structure.ComputedColumn.filterExpr=筛选表达式

Structure.ComputedColumn.allowExport=允许导出

Structure.ComputedColumn.calculationType=计算类型

Structure.ComputedColumn.calculationArguments=计算参数

Structure.ComputedColumn.refDateType=参考日期类型

Structure.ComputedColumn.refDateValue=参考日期值

Structure.ComputedColumn.timeDimension=时间维

#16. ColumnHint
Structure.ColumnHint.columnName=列名

Structure.ColumnHint.alias=别名

Structure.ColumnHint.searching=正在搜索

Structure.ColumnHint.export=导出

Structure.ColumnHint.analysis=分析

Structure.ColumnHint.parentLevel=父层

Structure.ColumnHint.format=格式

Structure.ColumnHint.helpText=帮助文本

Structure.ColumnHint.helpTextID=帮助文本资源关键字

Structure.ColumnHint.displayName=显示名称

Structure.ColumnHint.displayNameID=显示名称资源关键字

Structure.ColumnHint.onColumnLayout=在列布局上

Structure.ColumnHint.heading=标题

Structure.ColumnHint.headingID=标题资源关键字

Structure.ColumnHint.displayLength=显示长度

Structure.ColumnHint.horizontalAlign=水平对齐

Structure.ColumnHint.wordWrap=文字环绕

Structure.ColumnHint.textFormat=文本格式

Structure.ColumnHint.description=描述

Structure.ColumnHint.descriptionID=说明资源关键字

Structure.ColumnHint.action=操作

Structure.ColumnHint.ACLExpression=ACL 表达式

Structure.ColumnHint.analysisColumn=分析列

Structure.ColumnHint.valueFormat=值格式

Structure.ColumnHint.indexColumn=索引列

Structure.ColumnHint.compressed=压缩

#12. SelectionList Choice
Structure.SelectionChoice=选择选项

Structure.SelectionChoice.value=值

Structure.SelectionChoice.label=标签

Structure.SelectionChoice.labelID=标签资源关键字

#13. IncludedLibrary
Structure.IncludedLibrary.fileName=文件名

Structure.IncludedLibrary.namespace=名称空间

#14. IncludedCssStyleSheet
Structure.IncludedCssStyleSheet.fileName=文件名

Structure.IncludedCssStyleSheet.externalCssURI=外部 CSS URI

Structure.IncludedCssStyleSheet.useExternalCss=使用外部 CSS

#15. DataSetParam
Structure.DataSetParam.name=名称

Structure.DataSetParam.dataType=数据类型

Structure.DataSetParam.position=位置

Structure.DataSetParam.defaultValue=默认值

Structure.DataSetParam.isOptional=可选

Structure.DataSetParam.allowNull=允许 Null

Structure.DataSetParam.isInput=为输入参数

Structure.DataSetParam.isOutput=为输出参数

Structure.DataSetParam.nativeDataType=本地数据类型代码

Structure.DataSetParam.displayName=显示名称

Structure.DataSetParam.displayNameID=显示名称资源关键字

Structure.DataSetParam.heading=标题

Structure.DataSetParam.headingID=标题资源关键字

Structure.DataSetParam.helpText=帮助文本

Structure.DataSetParam.helpTextID=帮助文本资源关键字

Structure.DataSetParam.description=描述

Structure.DataSetParam.descriptionID=说明资源关键字

#16. ExtendedProperty
Structure.ExtendedProperty.name=名称

Structure.ExtendedProperty.value=值

#17. Action
Structure.Action.linkType=链接类型

Structure.Action.toolTip=工具提示

Structure.Action.targetWindow=目标

Structure.Action.linkExpr=链接表达式

Structure.Action.uri=位置

Structure.Action.targetBookmark=书签链接

Structure.Action.reportName=报表名称

Structure.Action.paramBindings=参数绑定

Structure.Action.search=搜索关键字

Structure.Action.formatType=格式类型

Structure.Action.targetFileType=目标文件类型

Structure.Action.targetBookmarkType=目标书签类型

#18. CachedMetaData
Structure.CachedMetaData.parameters=参数

Structure.CachedMetaData.resultSet=结果集列

#19. NumberFormatValue
Structure.NumberFormatValue.category=类别

Structure.NumberFormatValue.pattern=模式

Structure.NumberFormatValue.locale=区域设置

#20. StringFormatValue
Structure.StringFormatValue.category=类别

Structure.StringFormatValue.pattern=模式

Structure.StringFormatValue.locale=区域设置

#21. DateTimeFormatValue
Structure.DateTimeFormatValue.category=类别

Structure.DateTimeFormatValue.pattern=模式

Structure.DateTimeFormatValue.locale=区域设置

#21. DateFormatValue
Structure.DateFormatValue.category=类别

Structure.DateFormatValue.pattern=模式

Structure.DateFormatValue.locale=区域设置

#21. TimeFormatValue
Structure.TimeFormatValue.category=类别

Structure.TimeFormatValue.pattern=模式

Structure.TimeFormatValue.locale=区域设置

#22. UserMethod
Structure.UserMethod.name=名称

#23. PropertyBinding
Structure.PropertyBinding.name=名称

Structure.PropertyBinding.id=元素 ID

Structure.PropertyBinding.value=值

#24. JoinCondition
Structure.JoinCondition.joinType=联接类型

Structure.JoinCondition.joinOperator=联接运算符

Structure.JoinCondition.leftDataSet=左数据集

Structure.JoinCondition.rightDataSet=右数据集

Structure.JoinCondition.leftExpression=左表达式

Structure.JoinCondition.rightExpression=右表达式

#25. ParameterFormatValue
Structure.ParameterFormatValue.category=类别

Structure.ParameterFormatValue.pattern=模式

Structure.ParameterFormatValue.locale=区域设置

#26. OdaDesignerState
Structure.OdaDesignerState.version=版本

Structure.OdaDesignerState.stateContentAsString=内容声明为字符串

Structure.OdaDesignerState.stateContentAsBlob=内容声明为 Blob

#27. OdaResultSetColumn
Structure.OdaResultSetColumn=OdaResultSetColumn

Structure.OdaResultSetColumn.position=位置

Structure.OdaResultSetColumn.name=名称

Structure.OdaResultSetColumn.nativeName=本地名称

Structure.OdaResultSetColumn.dataType=数据类型

Structure.OdaResultSetColumn.nativeDataType=本地数据类型代码

#28. OdaDataSetParam
Structure.OdaDataSetParam=OdaDataSetParam

Structure.OdaDataSetParam.name=名称

Structure.OdaDataSetParam.paramName=参数名称

Structure.OdaDataSetParam.nativeName=本地名称

Structure.OdaDataSetParam.dataType=数据类型

Structure.OdaDataSetParam.nativeDataType=本地数据类型代码

Structure.OdaDataSetParam.position=位置

Structure.OdaDataSetParam.defaultValue=默认值

Structure.OdaDataSetParam.isOptional=可选

Structure.OdaDataSetParam.allowNull=允许 Null

Structure.OdaDataSetParam.isInput=输入

Structure.OdaDataSetParam.isOutput=输出

Structure.OdaDataSetParam.displayName=显示名称

Structure.OdaDataSetParam.displayNameID=显示名称资源关键字

Structure.OdaDataSetParam.heading=标题

Structure.OdaDataSetParam.headingID=标题资源关键字

Structure.OdaDataSetParam.helpText=帮助文本

Structure.OdaDataSetParam.helpTextID=帮助文本资源关键字

Structure.OdaDataSetParam.description=描述

Structure.OdaDataSetParam.descriptionID=说明资源关键字

#27. ScriptLib
Structure.ScriptLib.name =脚本库名称

#28. TOC
Structure.TOC=目录

Structure.TOC.Expression=表达式

Structure.TOC.TOCStyle=目录的样式

Structure.TOC.border.top.style=上边框样式

Structure.TOC.border.top.width=上边框宽度

Structure.TOC.border.top.color=上边框颜色

Structure.TOC.border.left.style=左边框样式

Structure.TOC.border.left.width=左边框宽度

Structure.TOC.border.left.color=左边框颜色

Structure.TOC.border.bottom.style=下边框样式

Structure.TOC.border.bottom.width=下边框宽度

Structure.TOC.border.bottom.color=下边框颜色

Structure.TOC.border.right.style=右边框样式

Structure.TOC.border.right.width=右边框宽度

Structure.TOC.border.right.color=右边框颜色

Structure.TOC.background.color=背景色

Structure.TOC.format.dateTime=日期时间格式

Structure.TOC.format.number=数值格式

Structure.TOC.format.number.align=数值对齐格式

Structure.TOC.format.string=字符串格式

Structure.TOC.font.family=字体系列

Structure.TOC.font.size=字号

Structure.TOC.font.style=字体样式

Structure.TOC.font.weight=字体粗细

Structure.TOC.font.variant=字体变体

Structure.TOC.font.color=字体颜色

Structure.TOC.font.textUnderline=文本下划线字体

Structure.TOC.font.textOverline=文本上划线字体

Structure.TOC.font.textLineThrough=文本删除线字体

Structure.TOC.text.align=文本对齐

Structure.TOC.text.indent=文本缩进

Structure.TOC.text.transform=文本转换

Structure.TOC.text.direction=文本方向

#29. DimensionCondition
Structure.DimensionCondition=维条件

Structure.DimensionCondition.joinConditions=联接条件

Structure.DimensionCondition.hierarchy=层次结构

#30. Rule
Structure.Rule=规则

Structure.Rule.ruleExpre=规则表达式

Structure.Rule.displayExpre=显示表达式

#31. LevelAttribute
Structure.LevelAttribute=级别属性

Structure.LevelAttribute.name=名称

Structure.LevelAttribute.dataType=数据类型

#32. OdaLevelAttribute
Structure.OdaLevelAttribute=ODA 级别属性

Structure.OdaLevelAttribute.name=名称

Structure.OdaLevelAttribute.dataType=数据类型

Structure.OdaLevelAttribute.nativeName=本地名称

Structure.OdaLevelAttribute.nativeDataType=本地数据类型代码

#33. DimensionJoinCondition
Structure.DimensionJoinCondition.cubeKey=多维数据集关键字

Structure.DimensionJoinCondition.hierarchyKey=层次结构关键字

Structure.DimensionJoinCondition.level=级别

#34. AggregationArgument
Structure.AggregationArgument=聚合参数

Structure.AggregationArgument.name=参数名称

Structure.AggregationArgument.value=参数值

#35. Variable
Structure.Variable=变量

Structure.Variable.name=名称

Structure.Variable.value=值

#36. SortHint
Structure.SortHint=排序提示

Structure.SortHint.columnName=列名

Structure.SortHint.position=列位置

Structure.SortHint.direction=排序方向

Structure.SortHint.nullValueOrdering=空值顺序

Structure.SortHint.isOptional=可选

#37. IncludedDataMart
Structure.IncludedDataMart=包括的 DataMart

Structure.IncludedDataMart.fileName=文件名

Structure.IncludedDataMart.namespace=名称空间

#38. TimeInterval
Structure.TimeInterval=时间间隔

Structure.TimeInterval.measure=度量

Structure.TimeInterval.unit=单位

#39. FormatValue
Structure.FormatValue.category=类别

Structure.FormatValue.pattern=模式

Structure.FormatValue.locale=区域设置

#40. CalculationArgument
Structure.CalculationArgument.name=计算参数名称

Structure.CalculationArgument.value=计算参数值

###########################################################
# Element names
Element.Module=模块

Element.LayoutModule=布局模块

Element.ReportDesign=报表设计

Element.DesignElement=设计元素

Element.ReportElement=报表元素

Element.DataSource=数据源

Element.JdbcDataSource=JDBC 数据源

Element.DataSet=数据集

Element.SimpleDataSet=简单数据集

Element.JdbcSelectDataSet=JDBC 选择数据集

Element.Parameter=参数

Element.AbstractScalarParameter=抽象标量参数

Element.ScalarParameter=ScalarParameter
Element.DynamicFilterParameter=动态筛选器参数

Element.ParameterGroup=参数组

Element.CascadingParameterGroup=级联参数组

Element.Style=样式

Element.MasterPage=MasterPage
Element.ReportItem=报表项

Element.Label=标签

Element.AutoText=AutoText
Element.FreeForm=FreeForm
Element.Data=数据

Element.Text=文本

Element.Library=库

Element.ListingGroup=列表组

Element.List=列表

Element.ListGroup=列表组

Element.Listint=列表

Element.Table=表

Element.TableGroup=表组

Element.Column=列

Element.Row=行

Element.Cell=单元格

Element.Image=图像

Element.Grid=网格

Element.Line=行

Element.Tile=平铺

Element.Rectangle=矩形

Element.GraphicMasterPage=图形主页

Element.SimpleMasterPage=简单主页

Element.TextData=动态文本

Element.ExtendedItem=扩展项

Element.ScriptDataSource=脚本数据源

Element.ScriptDataSet=脚本数据集

Element.Listing=列表

Element.OdaDataSet=ODA 数据集

Element.OdaDataSource=ODA 数据源

Element.Theme=布景主题

Element.TemplateParameterDefinition=模板参数定义

Element.TemplateElement=模板元素

Element.TemplateReportItem=模板报表项

Element.TemplateDataSet=模板数据集

Element.JointDataSet=联接数据集

Element.Cube=Cube
Element.Dimension=维

Element.Hierarchy=层次结构

Element.Level=级别

Element.Measure=度量

Element.MeasureGroup=度量组

Element.TabularCube=表格式多维数据集

Element.TabularDimension=表格式维

Element.TabularHierarchy=表格式层次结构

Element.TabularLevel=表格式级别

Element.TabularMeasure=表格式度量

Element.TabularMeasureGroup=度量组

Element.OdaCube=ODA 多维数据集

Element.OdaDimension=ODA 维

Element.OdaHierarchy=ODA 层次结构

Element.OdaLevel=ODA 级别

Element.OdaMeasure=ODA 度量

Element.OdaMeasureGroup=ODA 度量组

Element.MemberValue=成员值

Element.SortElement=排序元素

Element.FilterConditionElement=筛选条件元素

Element.AbstractMultiViews=抽象化多个视图

Element.MultiViews=多个视图

Element.VariableElement=变量

Element.DataGroup=数据组

Element.DerivedDataSet=衍生数据集

Element.DataMart=数据对象

Element.DataMartDataSource=数据对象数据源

Element.DataMartDataSet=数据对象数据集

Element.DataMartCube=数据对象多维数据集

Element.TimeDimension=时间维

Element.ReportItemTheme=报表项布景主题

Element.AbstractTheme=抽象布景主题

Element.DataMartLinkedDataModel=数据对象数据模型

Element.AliasDataSet =别名数据集

Element.LinkedDataModel =链接的数据模型

Element.CategoryColumn =类别列

Element.LinkedDataModel.pushDown=数据模型下推

Element.Category = 类别

Element.ColumnLink = 列链接

Element.ColumnLinkPath = 列链接路径

Element.DrillPath = 钻取路径

Element.LinkedDataModelCube = 链接的数据模型多维数据集

Element.LinkedDataModelDataSet = 链接的数据模型数据集

###########################################################
# Element property names

# Module ( Prefix = Element.Module )
Element.Module.author=作者

Element.Module.title=标题

Element.Module.titleID=标题关键字

Element.Module.description=描述

Element.Module.descriptionID=说明关键字

Element.Module.comments=备注

Element.Module.includeResource=源文件

Element.Module.createdBy=创建者

Element.Module.includeScripts=包含脚本

Element.Module.scriptLibs=脚本库

Element.Module.externalMetadata=外部元数据

Element.Module.propertyBindings=属性绑定

# LayoutModule ( Prefix = Element.LayoutModule )
Element.LayoutModule.subject=主题

Element.LayoutModule.helpGuide=帮助指南

Element.LayoutModule.colorPalette=调色板

Element.LayoutModule.configVars=配置变量

Element.LayoutModule.images=图像

Element.LayoutModule.libraries=库

Element.LayoutModule.base=基本

Element.LayoutModule.units=单位

Element.LayoutModule.theme=布景主题


# Library ( Prefix = Element.Library )
Element.Library.slot.styles=样式

Element.Library.slot.parameters=参数

Element.Library.slot.dataSources=数据源

Element.Library.slot.dataSets=数据集

Element.Library.slot.pageSetup=页面设置

Element.Library.slot.components=组件

Element.Library.slot.themes=布景主题

Element.Library.slot.cubes=多维数据集

#1. Report Design ( Prefix = Element.ReportDesign )
Element.ReportDesign.refreshRate=刷新比率

Element.ReportDesign.eventHandlerClass=事件处理程序类

Element.ReportDesign.newHandlerOnEachEvent=在每个事件上新建处理程序

Element.ReportDesign.beforeFactory=出厂前

Element.ReportDesign.beforeFactory.this=报表设计

Element.ReportDesign.beforeFactory.reportContext=报表上下文

Element.ReportDesign.afterFactory=出厂后

Element.ReportDesign.afterFactory.reportContext=报表上下文

Element.ReportDesign.beforeOpenDoc=打开文档之前

Element.ReportDesign.beforeOpenDoc.reportContext=报表上下文

Element.ReportDesign.afterOpenDoc=打开文档后

Element.ReportDesign.afterOpenDoc.reportContext=报表上下文

Element.ReportDesign.beforeCloseDoc=关闭文档前

Element.ReportDesign.beforeCloseDoc.reportContext=报表上下文

Element.ReportDesign.afterCloseDoc=关闭文档后

Element.ReportDesign.afterCloseDoc.reportContext=报表上下文

Element.ReportDesign.beforeRender=实施前

Element.ReportDesign.beforeRender.reportContext=报表上下文

Element.ReportDesign.afterRender=实施后

Element.ReportDesign.afterRender.reportContext=报表上下文

Element.ReportDesign.onPageStart.this=主页

Element.ReportDesign.onPageStart.reportContext=报表上下文

Element.ReportDesign.onPageEnd.this=主页

Element.ReportDesign.onPageEnd.reportContext=报表上下文

Element.ReportDesign.displayNameID=显示名称关键字

Element.ReportDesign.displayName=显示名称

Element.ReportDesign.iconFile=图标文件

Element.ReportDesign.cheatSheet=备忘单

Element.ReportDesign.thumbnail=缩略图

Element.ReportDesign.layoutPreference=布局首选项

Element.ReportDesign.bidiLayoutOrientation=报表方向

Element.ReportDesign.beforeFactory.toolTip=调用时间为:开始出厂时，在 onPrepare ( ) 方法之后并且在打开报表文档（如果有）之前

Element.ReportDesign.afterFactory.toolTip=调用时间为出厂结束时、关闭报表文档（如果有）后。 这是工厂中调用的最后一个方法

Element.ReportDesign.beforeOpenDoc.toolTip=仅在工厂中打开报表文档前调用

Element.ReportDesign.afterOpenDoc.toolTip=仅在工厂中打开报表文档后调用

Element.ReportDesign.beforeCloseDoc.toolTip=仅在工厂中关闭报表文档文件前调用

Element.ReportDesign.afterCloseDoc.toolTip=仅在工厂中关闭报表文档文件后调用

Element.ReportDesign.beforeRender.toolTip=开始展示时间操作前调用

Element.ReportDesign.afterRender.toolTip=开始展示时间操作后调用

Element.ReportDesign.onPageStart.toolTip=页面启动时调用。

Element.ReportDesign.onPageEnd.toolTip=页面终止时调用。

Element.ReportDesign.slot.styles=样式

Element.ReportDesign.slot.parameters=参数

Element.ReportDesign.slot.dataSources=数据源

Element.ReportDesign.slot.dataSets=数据集

Element.ReportDesign.slot.pageSetup=页面设置

Element.ReportDesign.slot.components=组件

Element.ReportDesign.slot.body=正文

Element.ReportDesign.slot.scratchPad=便笺簿

Element.ReportDesign.slot.templateParameterDefinitions=模板参数定义

Element.ReportDesign.slot.cubes=多维数据集元素

Element.ReportDesign.slot.themes=布景主题

Element.ReportDesign.initialize=初始化

Element.ReportDesign.initialize.toolTip=开始执行报表时调用。

Element.ReportDesign.initialize.reportContext=报表上下文

Element.ReportDesign.cssStyleSheets=CSS 样式表

Element.ReportDesign.enableACL=启用数据安全性

Element.ReportDesign.ACLExpression=ACL 表达式

Element.ReportDesign.cascadeACL=层叠 ACL

Element.ReportDesign.imageDPI=图像 DPI

Element.ReportDesign.pageVariables=页面变量

Element.ReportDesign.dataObjects= 数据对象

Element.ReportDesign.locale=区域设置

Element.ReportDesign.onPageStart=页面启动时

Element.ReportDesign.onPageEnd=页面终止时

Element.ReportDesign.dataMarts=DataMarts
Element.ReportDesign.onPrepare=准备时

Element.ReportDesign.onPrepare.this=报表设计

Element.ReportDesign.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.ReportDesign.onPrepare.reportContext=报表上下文

Element.ReportDesign.onContentUpdate= 在内容更新时

Element.ReportDesign.onContentUpdate.toolTip=当在 BIRT Viewer 中更新报表内容时在客户端上执行。

Element.ReportDesign.onContentUpdate.this=报表设计

Element.ReportDesign.clientInitialize=客户端初始化

Element.ReportDesign.clientInitialize.toolTip=加载客户端 Java 脚本库。

Element.ReportDesign.clientInitialize.this=报表设计

Element.ReportDesign.language=语言

Element.ReportDesign.PDFAccessible=可访问 PDF

#2. Report Element ( Prefix = Element.ReportElement )
Element.ReportElement.displayName=显示名称

Element.ReportElement.displayNameID=显示名称关键字

Element.ReportElement.comments=备注

Element.ReportElement.propertyMask=属性掩码

Element.ReportElement.name=名称

Element.ReportElement.extends=扩展

Element.ReportElement.style=样式

Element.ReportElement.eventHandlerClass=事件处理程序类

Element.ReportElement.newHandlerOnEachEvent=在每个事件上新建处理程序

Element.ReportElement.viewAction=查看操作

#3. JdbcDataSource  ( prefix  Element.JdbcDataSource )
Element.JdbcDataSource.url=Url

Element.JdbcDataSource.userName=用户名

Element.JdbcDataSource.password=密码

Element.JdbcDataSource.driver=驱动程序

Element.JdbcDataSource.dataSource=数据源

#4. DataSet  ( prefix  Element.DataSet )
Element.DataSet.resultSetHints=结果集提示

Element.DataSet.computedColumns=计算列

Element.DataSet.filter=筛选器

Element.DataSet.columnHints=列提示

Element.DataSet.cachedMetaData=缓存元数据

Element.DataSet.rowFetchLimit=从数据集提取的最大行数

Element.DataSet.needsCache=需要数据引擎缓存

Element.DataSet.parameters=数据集参数

Element.DataSet.sortHints=排序提示

Element.DataSet.ACLExpression=ACL 表达式

Element.DataSet.RowACLExpression=行 ACL 表达式

Element.DataSet.isVisible=可见

Element.DataSet.locale=区域设置

Element.DataSet.nullsOrdering=空值排序

#4. SimpleDataSet  ( prefix  Element.SimpleDataSet )
Element.SimpleDataSet.beforeOpen=打开前

Element.SimpleDataSet.beforeOpen.toolTip=仅在 BIRT 打开数据集前调用

Element.SimpleDataSet.beforeOpen.this=数据集

Element.SimpleDataSet.beforeOpen.reportContext=报表上下文

Element.SimpleDataSet.beforeClose=关闭前

Element.SimpleDataSet.beforeClose.this=数据集

Element.SimpleDataSet.beforeClose.toolTip=仅在 BIRT 关闭数据集前调用

Element.SimpleDataSet.beforeClose.reportContext=报表上下文

Element.SimpleDataSet.onFetch=获取中

Element.SimpleDataSet.onFetch.toolTip=仅在 BIRT 从数据集获取数据行前调用

Element.SimpleDataSet.onFetch.reportContext=报表上下文

Element.SimpleDataSet.onFetch.this=数据集

Element.SimpleDataSet.onFetch.row=数据集行

Element.SimpleDataSet.afterOpen=打开后

Element.SimpleDataSet.afterOpen.this=数据集

Element.SimpleDataSet.afterOpen.toolTip=仅在 BIRT 打开数据集后调用

Element.SimpleDataSet.afterOpen.reportContext=报表上下文

Element.SimpleDataSet.afterClose=关闭后

Element.SimpleDataSet.afterClose.toolTip=仅在 BIRT 关闭数据集后调用

Element.SimpleDataSet.afterClose.reportContext=报表上下文

Element.SimpleDataSet.paramBindings=参数绑定

Element.SimpleDataSet.refTemplateParameter=参考模板参数定义

Element.SimpleDataSet.cachedRowCount=缓存行计数

Element.SimpleDataSet.dataSetRowLimit=数据集行限制
  
#5. JdbcSelectDataSet  ( prefix  Element.JdbcSelectDataSet )
Element.JdbcSelectDataSet.statement=语句

Element.JdbcSelectDataSet.getStatement=获取语句

Element.JdbcSelectDataSet.getStatement.toolTip=返回查询的自定义 SQL 语句
  
#6. Parameter  ( prefix  Element.Parameter )
Element.Parameter.helpText=帮助文本

Element.Parameter.helpTextID=帮助文本关键字

Element.Parameter.hidden=隐藏

Element.Parameter.validate=验证

Element.Parameter.validate.reportContext=报表上下文

Element.Parameter.validate.toolTip=对该参数的自定义验证返回真或假

Element.Parameter.promptText=提示文本

Element.Parameter.promptTextID=提示文本关键字
  
#7. ScalarParameter  ( prefix  Element.ScalarParameter )
Element.ScalarParameter.paramType=标量参数类型

Element.ScalarParameter.displayPrompt=显示提示

Element.ScalarParameter.concealValue=隐藏值

Element.ScalarParameter.format=格式

Element.ScalarParameter.controlType=控件类型

Element.ScalarParameter.alignment=对齐

Element.ScalarParameter.selectionList=选择列表

Element.ScalarParameter.selectionList.mustMatch=必须匹配

Element.ScalarParameter.selectionList.fixedOrder=固定顺序

Element.ScalarParameter.boundDataColumns=绑定的数据列

Element.ScalarParameter.autoSuggestThreshold=自动建议阈值

Element.ScalarParameter.getDefaultValueList=获得默认值列表

Element.ScalarParameter.getDefaultValueList.reportContext=报表上下文

Element.ScalarParameter.getDefaultValueList.toolTip=返回参数的默认值列表

Element.ScalarParameter.getSelectionValueList=获得选择值列表

Element.ScalarParameter.getSelectionValueList.reportContext=报表上下文

Element.ScalarParameter.getSelectionValueList.toolTip=如果参数控制类型为“list”或“combo”则返回选择值列表

Element.ScalarParameter.defaultValue=默认值
  
#8. ParameterGroup  ( prefix  Element.ParameterGroup )
Element.ParameterGroup.startExpanded=开始展开

Element.ParameterGroup.helpText=帮助文本

Element.ParameterGroup.helpTextID=帮助文本关键字

Element.ParameterGroup.promptText=提示文本

Element.ParameterGroup.promptTextID=提示文本关键字

Element.ParameterGroup.slot.parameters=参数

# CascadingParameterGroup (prefix Element.CascadingParameterGroup)
Element.CascadingParameterGroup.dataSet=数据集

Element.CascadingParameterGroup.dataSetMode=单个或多个数据集

Element.CascadingParameterGroup.slot.parameters=参数

#9. MasterPage ( Prefix = Element.MasterPage )
Element.MasterPage.type=类型

Element.MasterPage.height=高度

Element.MasterPage.width=宽度

Element.MasterPage.orientation=方向

Element.MasterPage.columns=列

Element.MasterPage.columnSpacing=列间距

Element.MasterPage.margin=边距

Element.MasterPage.topMargin=上边距

Element.MasterPage.leftMargin=左边距

Element.MasterPage.bottomMargin=下边距

Element.MasterPage.rightMargin=右边距

Element.MasterPage.slot.contents=目录

Element.MasterPage.onPageStart=页面启动时

Element.MasterPage.onPageEnd=页面终止时

Element.MasterPage.onPageStart.toolTip=页面启动时调用。

Element.MasterPage.onPageEnd.toolTip=页面终止时调用。

Element.MasterPage.onPageStart.this=主页

Element.MasterPage.onPageStart.reportContext=报表上下文

Element.MasterPage.onPageEnd.this=主页

Element.MasterPage.onPageEnd.reportContext=报表上下文

#9. Style ( Prefix = Element.Style)
Element.Style.background=背景

Element.Style.background.attachment=背景附件

Element.Style.background.color=背景色

Element.Style.background.image=背景图像

Element.Style.background.imageType=背景图像类型

Element.Style.background.positionX=水平位置

Element.Style.background.positionY=垂直位置

Element.Style.background.repeat=背景重复

Element.Style.background.size.width=背景大小宽度

Element.Style.background.size.height=背景大小高度

Element.Style.border=边框

Element.Style.border.bottom.color=下边框颜色

Element.Style.border.bottom.style=下边框样式

Element.Style.border.bottom.width=下边框宽度

Element.Style.border.fillColor=边框填充色

Element.Style.border.hmiddle.color=边框水平中间色

Element.Style.border.hmiddle.style=边框水平中间样式

Element.Style.border.hmiddle.width=边框水平中间宽度

Element.Style.border.left.color=左边框颜色

Element.Style.border.left.style=左边框样式

Element.Style.border.left.width=左边框宽度

Element.Style.border.right.color=右边框颜色

Element.Style.border.right.style=右边框样式

Element.Style.border.right.width=右边框宽度

Element.Style.border.top.color=上边框颜色

Element.Style.border.top.style=上边框样式

Element.Style.border.top.width=上边框宽度

Element.Style.border.vmiddle.color=边框垂直中间色

Element.Style.border.vmiddle.style=边框垂直中间样式

Element.Style.border.vmiddle.width=边框垂直中间宽度

Element.Style.box=框

Element.Style.canShrink=缩短

Element.Style.display=显示

Element.Style.font=字体

Element.Style.color=字体颜色

Element.Style.font.family=字体系列

Element.Style.font.size=字号

Element.Style.font.style=字体样式

Element.Style.font.textLineThrough=删除线

Element.Style.font.textUnderline=下划线

Element.Style.font.textOverline=上划线

Element.Style.font.variant=字体变体

Element.Style.font.weight=字体粗细

Element.Style.format=格式

Element.Style.format.dateTime=日期时间格式

Element.Style.format.date=日期格式

Element.Style.format.time=时间格式

Element.Style.format.number=数值格式

Element.Style.format.number.align=数值对齐

Element.Style.format.string=字符串格式

Element.Style.highlight.testExpr=突出显示测试表达式

Element.Style.highlight.rules=突出显示规则

Element.Style.letterSpacing=字母间距

Element.Style.lineHeight=行高

Element.Style.map.rules=映射规则

Element.Style.margin.top=上边距

Element.Style.margin.bottom=下边距

Element.Style.margin.left=左边距

Element.Style.margin.right=右边距

Element.Style.masterPage=主页

Element.Style.padding=填充

Element.Style.padding.bottom=底部间隙

Element.Style.padding.left=左侧间隙

Element.Style.padding.right=右侧间隙

Element.Style.padding.top=顶部间隙

Element.Style.pageBreakBefore=对象前面的分页符

Element.Style.pageBreakAfter=对象后面的分页符

Element.Style.pageBreakInside=对象内部的分页符

Element.Style.section=区域

Element.Style.showIfBlank=如果为空白则显示

Element.Style.text=文本

Element.Style.text.align=文本对齐

Element.Style.text.direction=文本方向

Element.Style.text.indent=文本缩进

Element.Style.text.transform=文本转换

Element.Style.verticalAlign=垂直对齐

Element.Style.whiteSpace=空白

Element.Style.widows=弧行

Element.Style.orphans=孤行

Element.Style.wordSpacing=字间距

Element.Style.overflow=溢出

Element.Style.height=高度

Element.Style.width=宽度

#10.ReportItem  ( prefix  Element.ReportItem )
Element.ReportItem.bookmark=书签

Element.ReportItem.bookmarkDisplayName=书签显示名称

Element.ReportItem.toc=目录

Element.ReportItem.dataSet=数据集

Element.ReportItem.cube=多维数据集

Element.ReportItem.x=X
Element.ReportItem.y=Y
Element.ReportItem.height=高度

Element.ReportItem.width=宽度

Element.ReportItem.visibility=可见性

Element.ReportItem.paramBindings=参数绑定

Element.ReportItem.refTemplateParameter=参考模板参数定义

Element.ReportItem.boundDataColumns=绑定的数据列

Element.ReportItem.zIndex=Z 索引

Element.ReportItem.dataBindingRef=数据绑定引用

Element.ReportItem.multiViews=多个视图

Element.ReportItem.ACLExpression=ACL 表达式

Element.ReportItem.cascadeACL=层叠 ACL

Element.ReportItem.allowExport=允许导出

Element.ReportItem.pushDown=下推

Element.ReportItem.theme=布景主题

Element.ReportItem.language=语言

Element.ReportItem.order=顺序

Element.ReportItem.altText=替代文本

Element.ReportItem.altTextID=替代文本关键字

Element.ReportItem.linkedDataModel=链接的数据模型

#11.Label  ( prefix  Element.Label )
Element.Label.text=内容

Element.Label.textID=内容关键字

Element.Label.helpTextID=帮助文本关键字

Element.Label.helpText=帮助文本

Element.Label.action=操作

Element.Label.onPrepare=准备时

Element.Label.onPrepare.this=标签

Element.Label.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Label.onPrepare.reportContext=报表上下文

Element.Label.onCreate=创建时

Element.Label.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Label.onCreate.this=标签实例

Element.Label.onCreate.reportContext=报表上下文

Element.Label.onRender=实施时

Element.Label.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Label.onRender.this=标签实例

Element.Label.onRender.reportContext=报表上下文

Element.Label.onPageBreak=出现分页符时

Element.Label.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.Label.onPageBreak.this=标签实例

Element.Label.onPageBreak.reportContext=报表上下文

Element.Label.tagType=PDF 标记类型

Element.Label.altText=替代文本
  
#12.Element.FreeForm  ( Prefix Element.FreeForm )
Element.FreeForm.slot.reportItems=报表项
  
#12.Container  ( prefix  Element.Container )
Element.Container.slot.reportItems=报表项
  
#13.Data  ( prefix  Element.Data )
Element.Data.resultSetColumn=结果集列

Element.Data.helpText=帮助文本

Element.Data.helpTextID=帮助文本关键字

Element.Data.action=操作

Element.Data.onPrepare=准备时

Element.Data.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Data.onPrepare.this=数据

Element.Data.onPrepare.reportContext=报表上下文

Element.Data.onCreate=创建时

Element.Data.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Data.onCreate.this=数据实例

Element.Data.onCreate.reportContext=报表上下文

Element.Data.onRender=实施时

Element.Data.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Data.onRender.this=数据实例

Element.Data.onRender.reportContext=报表上下文

Element.Data.onPageBreak=出现分页符时

Element.Data.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.Data.onPageBreak.this=数据实例

Element.Data.onPageBreak.reportContext=报表上下文

Element.Data.tagType=PDF 标记类型

Element.Data.altText=替代文本
  
#14.Text  ( prefix  Element.Text )
Element.Text.content=内容

Element.Text.contentType=内容类型

Element.Text.contentID=内容关键字

Element.Text.onPrepare=准备时

Element.Text.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Text.onPrepare.this=文本

Element.Text.onPrepare.reportContext=报表上下文

Element.Text.onCreate=创建时

Element.Text.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Text.onCreate.this=文本实例

Element.Text.onCreate.reportContext=报表上下文

Element.Text.onRender=实施时

Element.Text.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Text.onRender.this=文本实例

Element.Text.onRender.reportContext=报表上下文

Element.Text.onPageBreak=出现分页符时

Element.Text.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.Text.onPageBreak.this=文本实例

Element.Text.onPageBreak.reportContext=报表上下文

Element.Text.hasExpression=具有表达式

Element.Text.tagType=PDF 标记类型

Element.Text.altText=替代文本
  
#15.ListingGroup  ( prefix  Element.ListingGroup )
Element.ListingGroup.keyExpr=关键字表达式

Element.ListingGroup.sort=排序

Element.ListingGroup.bookmark=书签

Element.ListingGroup.bookmarkDisplayName=书签显示名称

Element.ListingGroup.toc=目录

Element.ListingGroup.hideDetail=隐藏明细数据

Element.ListingGroup.groupName=名称

Element.ListingGroup.Interval=间隔

Element.ListingGroup.intervalBase=间隔基数

Element.ListingGroup.interval=间隔

Element.ListingGroup.intervalRange=间隔范围

Element.ListingGroup.sortDirection=排序方向

Element.ListingGroup.eventHandlerClass=事件处理程序类

Element.ListingGroup.newHandlerOnEachEvent=在每个事件上新建处理程序

Element.ListingGroup.pageBreakBefore=对象前面的分页符

Element.ListingGroup.pageBreakAfter=对象后面的分页符

Element.ListingGroup.pageBreakInside=内部分页符

Element.ListingGroup.filter=筛选器

Element.ListingGroup.repeatHeader=重复标题

Element.ListingGroup.onStart=启动时

Element.ListingGroup.onStart.toolTip=调用时间为打开数据集之后但在创建题头区段之前，但早于从此元素的数据集内检索首行的时间

Element.ListingGroup.onRow=遇到行时

Element.ListingGroup.onRow.toolTip=为从此元素的数据集中检索出的每行调用，但在为该行创建内容之前

Element.ListingGroup.onFinish=完成时

Element.ListingGroup.onFinish.toolTip=在从此元素的数据集内读取最后一行之后调用，但在创建页脚区段之前

Element.ListingGroup.sortType=排序类型

Element.ListingGroup.boundDataColumns=绑定的数据列

Element.ListingGroup.viewAction=查看操作

Element.ListingGroup.onCreate=创建时

Element.ListingGroup.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.ListingGroup.onCreate.this=列表组

Element.ListingGroup.onCreate.reportContext=报表上下文

Element.ListingGroup.onRender=绘制时

Element.ListingGroup.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.ListingGroup.onRender.this=列表组

Element.ListingGroup.onRender.reportContext=报表上下文

Element.ListingGroup.ACLExpression=ACL 表达式

Element.ListingGroup.cascadeACL=层叠 ACL

Element.ListingGroup.showDetailFilter=显示详细信息筛选器

#16.List  ( prefix  Element.List )
Element.List.slot.header=题头

Element.List.slot.groups=组

Element.List.slot.detail=详细资料

Element.List.slot.footer=脚注

Element.List.onPrepare=准备时

Element.List.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.List.onPrepare.this=列表

Element.List.onPrepare.reportContext=报表上下文

Element.List.onCreate=创建时

Element.List.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.List.onCreate.this=列表实例

Element.List.onCreate.reportContext=报表上下文

Element.List.onRender=实施时

Element.List.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.List.onRender.this=列表实例

Element.List.onRender.reportContext=报表上下文

Element.List.onPageBreak=出现分页符时

Element.List.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.List.onPageBreak.this=列表实例

Element.List.onPageBreak.reportContext=报表上下文

Element.List.tagType=PDF 标记类型

Element.List.altText=替代文本
  
#17.ListGroup  ( prefix  Element.ListGroup )
Element.ListGroup.slot.header=题头

Element.ListGroup.slot.footer=页脚

Element.ListGroup.onPrepare=准备时

Element.ListGroup.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.ListGroup.onPrepare.this=列表组

Element.ListGroup.onPrepare.reportContext=报表上下文

Element.ListGroup.onPageBreak=出现分页符时

Element.ListGroup.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.ListGroup.onPageBreak.this=列表组

Element.ListGroup.onPageBreak.reportContext=报表上下文
  
#18.Table  ( prefix  Element.Table )
Element.Table.caption=标题

Element.Table.summary=摘要

Element.Table.isSummaryTable=摘要表

Element.Table.captionID=标题关键字

Element.Table.slot.header=题头

Element.Table.slot.groups=组

Element.Table.slot.detail=详细资料

Element.Table.slot.footer=脚注

Element.Table.slot.columns=列

Element.Table.onPrepare=准备时

Element.Table.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Table.onPrepare.this=表

Element.Table.onPrepare.reportContext=报表上下文

Element.Table.onPrepare.object=对象

Element.Table.onCreate=创建时

Element.Table.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Table.onCreate.this=表实例

Element.Table.onCreate.reportContext=报表上下文

Element.Table.onCreate.object=对象

Element.Table.onRender=实施时

Element.Table.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Table.onRender.this=表实例

Element.Table.onRender.reportContext=报表上下文

Element.Table.onRender.object=对象

Element.Table.onPageBreak=出现分页符时

Element.Table.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.Table.onPageBreak.this=表实例

Element.Table.onPageBreak.reportContext=报表上下文

Element.Table.tagType=PDF 标记类型
  
#19.TableGroup  ( prefix  Element.TableGroup )
Element.TableGroup.slot.header=题头

Element.TableGroup.slot.footer=脚注

Element.TableGroup.onPrepare=准备时

Element.TableGroup.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.TableGroup.onPrepare.this=表组

Element.TableGroup.onPrepare.reportContext=报表上下文

Element.TableGroup.onPageBreak=出现分页符时

Element.TableGroup.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.TableGroup.onPageBreak.this=表组

Element.TableGroup.onPageBreak.reportContext=报表上下文
  
#20.Column  ( prefix  Element.Column )
Element.Column.repeat=重复

Element.Column.width=宽度

Element.Column.alignment=对齐

Element.Column.suppressDuplicates=抑制重复

Element.Column.visibility=可见性

Element.Column.viewAction=查看操作
  
#21.Row  ( prefix  Element.Row )
Element.Row.visibility=可见性

Element.Row.height=高度

Element.Row.bookmark=书签

Element.Row.bookmarkDisplayName=书签显示名称

Element.Row.eventHandlerClass=事件处理程序类

Element.Row.newHandlerOnEachEvent=在每个事件上新建处理程序

Element.Row.slot.cells=单元格

Element.Row.onPrepare=准备时

Element.Row.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Row.onPrepare.this=行

Element.Row.onPrepare.reportContext=报表上下文

Element.Row.onCreate=创建时

Element.Row.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Row.onCreate.this=行实例

Element.Row.onCreate.reportContext=报表上下文

Element.Row.onRender=绘制时

Element.Row.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Row.onRender.this=行实例

Element.Row.onRender.reportContext=报表上下文

Element.Row.suppressDuplicates=抑制重复

Element.Row.viewAction=查看操作

Element.Row.repeatable=可重复

Element.Row.tagType=PDF 标记类型

Element.Row.language=语言
  
#22.Cell  ( prefix  Element.Cell )
Element.Cell.column=列

Element.Cell.colSpan=列跨度

Element.Cell.rowSpan=行跨度

Element.Cell.verticalAlign=垂直对齐

Element.Cell.drop=下拉

Element.Cell.height=高度

Element.Cell.slot.content=内容

Element.Cell.width=宽度

Element.Cell.eventHandlerClass=事件处理程序类

Element.Cell.newHandlerOnEachEvent=在每个事件上新建处理程序

Element.Cell.scope=范围

Element.Cell.bookmark=书签

Element.Cell.bookmarkDisplayName=书签显示名称

Element.Cell.headers=页眉

Element.Cell.onPrepare=准备时

Element.Cell.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Cell.onPrepare.this=单元格

Element.Cell.onPrepare.reportContext=报表上下文

Element.Cell.onCreate=创建时

Element.Cell.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Cell.onCreate.this=单元格实例

Element.Cell.onCreate.reportContext=报表上下文

Element.Cell.onRender=绘制时

Element.Cell.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Cell.onRender.this=单元格实例

Element.Cell.onRender.reportContext=报表上下文

Element.Cell.viewAction=查看操作

Element.Cell.diagonal=对角线

Element.Cell.diagonalNumber=对角线数

Element.Cell.antidiagonalNumber=反对角线数

Element.Cell.diagonalThickness=对角线粗细

Element.Cell.diagonalColor=对角线颜色

Element.Cell.antidiagonalThickness=反对角线粗细

Element.Cell.diagonalStyle=对角线样式

Element.Cell.antidiagonalStyle=反对角线样式

Element.Cell.antidiagonalColor=反对角线颜色

Element.Cell.tagType=PDF 标记类型

Element.Cell.language=语言

Element.Cell.altText=替代文本
  
#23.Image  ( prefix  Element.Image )
Element.Image.altText=替代文本

Element.Image.altTextID=替代文本关键字

Element.Image.helpText=帮助文本

Element.Image.helpTextID=帮助文本关键字

Element.Image.size=大小

Element.Image.scale=刻度

Element.Image.source=源

Element.Image.uri=URI
Element.Image.imageName=图像名称

Element.Image.valueExpr=值表达式

Element.Image.typeExpr=类型表达式

Element.Image.action=操作

Element.Image.onPrepare=准备时

Element.Image.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Image.onPrepare.this=图像

Element.Image.onPrepare.reportContext=报表上下文

Element.Image.onCreate=创建时

Element.Image.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Image.onCreate.this=图像实例

Element.Image.onCreate.dataRow=数据行

Element.Image.onCreate.reportContext=报表上下文

Element.Image.onRender=实施时

Element.Image.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Image.onRender.this=图像实例

Element.Image.onRender.reportContext=报表上下文

Element.Image.onPageBreak=出现分页符时

Element.Image.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.Image.onPageBreak.this=图像实例

Element.Image.onPageBreak.reportContext=报表上下文

Element.Image.fitToContainer=适合容器

Element.Image.proportionalScale=比例尺

Element.Image.tagType=PDF 标记类型
  
#24.Grid  ( prefix  Element.Grid )
Element.Grid.slot.columns=列

Element.Grid.slot.rows=行

Element.Grid.onPrepare=准备时

Element.Grid.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.Grid.onPrepare.this=网格

Element.Grid.onPrepare.reportContext=报表上下文

Element.Grid.onCreate=创建时

Element.Grid.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.Grid.onCreate.this=网格实例

Element.Grid.onCreate.dataRow=数据行

Element.Grid.onCreate.reportContext=报表上下文

Element.Grid.onRender=实施时

Element.Grid.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.Grid.onRender.this=网格实例

Element.Grid.onRender.reportContext=报表上下文

Element.Grid.onPageBreak=出现分页符时

Element.Grid.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.Grid.onPageBreak.this=网格实例

Element.Grid.onPageBreak.reportContext=报表上下文

Element.Grid.caption=标题

Element.Grid.captionID=标题关键字

Element.Grid.summary=摘要

Element.Grid.tagType=PDF 标记类型
  
#25.Line  ( prefix  Element.Line )
Element.Line.orientation=方向
  
#26.TextData  ( prefix  Element.TextData )
Element.TextData.valueExpr=值表达式

Element.TextData.contentType=内容类型

Element.TextData.onPrepare=准备时

Element.TextData.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.TextData.onPrepare.this=动态文本

Element.TextData.onPrepare.reportContext=报表上下文

Element.TextData.onPrepare.object=对象

Element.TextData.onCreate=创建时

Element.TextData.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.TextData.onCreate.this=动态文本实例

Element.TextData.onCreate.dataRow=数据行

Element.TextData.onCreate.reportContext=报表上下文

Element.TextData.onRender=实施时

Element.TextData.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.TextData.onRender.this=动态文本实例

Element.TextData.onRender.reportContext=报表上下文

Element.TextData.onPageBreak=出现分页符时

Element.TextData.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.TextData.onPageBreak.this=动态文本实例

Element.TextData.onPageBreak.reportContext=报表上下文

Element.TextData.hasExpression=具有表达式

Element.TextData.tagType=PDF 标记类型

Element.TextData.altText=替代文本

#27.ExtendedItem   ( prefix  Element.ExtendedItem )
Element.ExtendedItem.altText=替代文本

Element.ExtendedItem.altTextID=替代文本关键字

Element.ExtendedItem.extensionName=扩展名

Element.ExtendedItem.filter=筛选器

Element.ExtendedItem.onPrepare=准备时

Element.ExtendedItem.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.ExtendedItem.onPrepare.this=扩展项

Element.ExtendedItem.onPrepare.reportContext=报表上下文

Element.ExtendedItem.onCreate=创建时

Element.ExtendedItem.onCreate.toolTip=在工厂中创建该元素时执行

Element.ExtendedItem.onCreate.this=扩展项实例

Element.ExtendedItem.onCreate.reportContext=报表上下文

Element.ExtendedItem.onRender=实施时

Element.ExtendedItem.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行。

Element.ExtendedItem.onRender.this=扩展项实例

Element.ExtendedItem.onRender.reportContext=报表上下文

Element.ExtendedItem.onPageBreak=出现分页符时

Element.ExtendedItem.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.ExtendedItem.onPageBreak.this=扩展项实例

Element.ExtendedItem.onPageBreak.reportContext=报表上下文

Element.ExtendedItem.extensionVersion=扩展版本

#28.DataSource ( prefix Element.DataSource )
Element.DataSource.beforeOpen=打开前

Element.DataSource.beforeOpen.toolTip=仅在 BIRT 打开数据源前调用此方法

Element.DataSource.beforeOpen.this=数据源

Element.DataSource.beforeOpen.reportContext=报表上下文

Element.DataSource.beforeClose=关闭前

Element.DataSource.beforeClose.toolTip=仅在 BIRT 关闭数据源前调用此方法

Element.DataSource.beforeClose.reportContext=报表上下文

Element.DataSource.beforeClose.this=数据源

Element.DataSource.afterOpen=打开后

Element.DataSource.afterOpen.toolTip=仅在 BIRT 打开数据源后调用此方法

Element.DataSource.afterOpen.reportContext=报表上下文

Element.DataSource.afterOpen.this=数据源

Element.DataSource.afterClose=关闭后

Element.DataSource.afterClose.toolTip=仅在 BIRT 关闭数据源后调用此方法

Element.DataSource.afterClose.reportContext=报表上下文

#29.ScriptDataSource ( prefix Element.ScriptDataSource )
Element.ScriptDataSource.open=打开

Element.ScriptDataSource.open.toolTip=建立至数据源的连接

Element.ScriptDataSource.open.this=脚本数据源

Element.ScriptDataSource.close=关闭

Element.ScriptDataSource.close.toolTip=断开至数据源的连接

Element.ScriptDataSource.close.this=脚本数据源

#31 ScriptDataSet ( prefix Element.ScriptDataSet )
Element.ScriptDataSet.dataSource=数据源

Element.ScriptDataSet.open=打开

Element.ScriptDataSet.open.toolTip=打开数据集

Element.ScriptDataSet.open.this=脚本数据集

Element.ScriptDataSet.describe=说明

Element.ScriptDataSet.describe.toolTip=说明结果集

Element.ScriptDataSet.describe.reportContext=报表上下文

Element.ScriptDataSet.describe.this=脚本数据集

Element.ScriptDataSet.describe.dataSetMetaData=数据集元数据

Element.ScriptDataSet.fetch=获取

Element.ScriptDataSet.fetch.currentRow=当前行

Element.ScriptDataSet.fetch.toolTip=创建、填写并返回类型“数据行”对象

Element.ScriptDataSet.fetch.reportContext=报表上下文

Element.ScriptDataSet.fetch.this=脚本数据集

Element.ScriptDataSet.close=关闭

Element.ScriptDataSet.close.toolTip=关闭数据集

Element.ScriptDataSet.close.this=脚本数据集

Element.ScriptDataSet.resultSetHints=结果集提示

#32 Listing ( prefix Element.Lising )
Element.Listing.sorting=排序

Element.Listing.filter=筛选器

Element.Listing.repeatHeader=重复标题

Element.Listing.pageBreakInterval=分页符间隔

Element.Listing.sortByGroups=按组排序

Element.Listing.onStart=启动时

Element.Listing.onStart.toolTip=调用时间为打开数据集之后但在创建题头区段之前，但早于从此元素的数据集内检索首行的时间

Element.Listing.onRow=遇到行时

Element.Listing.onRow.toolTip=为从此元素的数据集中检索出的每行调用，但在为该行创建内容之前

Element.Listing.onFinish=完成时

Element.Listing.onFinish.toolTip=在从此元素的数据集内读取最后一行之后调用，但在创建页脚区段之前

#33 GraphicMasterPage ( Prefix Element.GraphicMasterPage)
Element.GraphicMasterPage.slot.contents=内容

#34 SimpleMasterPage ( Prefix Element.SimpleMasterPage)
Element.SimpleMasterPage.showHeaderOnFirst=在最后一行显示脚注

Element.SimpleMasterPage.showFooterOnLast=在最后一页显示脚注

Element.SimpleMasterPage.headerHeight=页眉高度

Element.SimpleMasterPage.footerHeight=页脚高度

Element.SimpleMasterPage.floatingFooter=浮动脚注

Element.SimpleMasterPage.slot.pageHeader=页眉

Element.SimpleMasterPage.slot.pageFooter=页脚

#35 OdaDataSource ( Prefix Element.OdaDataSource )
Element.OdaDataSource.driverName=驱动程序名称

Element.OdaDataSource.extensionID=扩展 ID

Element.OdaDataSource.publicDriverProperties=公共驱动程序属性

Element.OdaDataSource.privateDriverProperties=私有驱动程序属性

Element.OdaDataSource.designerState=设计器声明

#36 OdaDataSet ( Prefix Element.OdaDataSet )
Element.OdaDataSet.extensionID=扩展 ID

Element.OdaDataSet.resultSet=结果集列

Element.OdaDataSet.queryText=查询

Element.OdaDataSet.type=类型

Element.OdaDataSet.resultSetName=结果集名称

Element.OdaDataSet.publicDriverProperties=公共驱动程序属性

Element.OdaDataSet.privateDriverProperties=私有驱动程序属性

Element.OdaDataSet.designerState=设计器声明

Element.OdaDataSet.designerValues=设计器值

Element.OdaDataSet.resultSetNumber=结果集数值

Element.OdaDataSet.dataSource=数据源

#37 DesignElement ( Prefix Element.DesignElement )
Element.DesignElement.customXml=自定义 XML

Element.DesignElement.propertyMasks=属性掩码

Element.DesignElement.userProperties=用户属性

#38 Theme ( Prefix Element.Theme )
Element.Theme.slot.styles=样式

#39 TemplateParameterDefinition ( Prefix Element.TemplateParameterDefinition )
Element.TemplateParameterDefinition.name=名称

Element.TemplateParameterDefinition.allowedType=允许的类型

Element.TemplateParameterDefinition.description=描述

Element.TemplateParameterDefinition.descriptionID=说明 ID

Element.TemplateParameterDefinition.slot.default=默认

#40 TemplateElement ( Prefix Element.TemplateElement )
Element.TemplateElement.name=名称

Element.TemplateElement.refTemplateParameter=引用模板参数

Element.TemplateElement.displayName=显示名称

Element.TemplateElement.displayNameID=显示名称 ID

#41 JointDataset ( Prefix Element.JointDataset )
Element.JointDataSet.resultSet=结果集列

Element.JointDataSet.dataSets=数据集

Element.JointDataSet.joinConditions=联接条件

#42 Rectangle  ( prefix  Element.Rectangle )

#43. TemplateReportItem
Element.TemplateReportItem.visibility=可见性

#44.AutoText ( prefix Element.AutoText )
Element.AutoText.type = 自动图文集的类型

Element.AutoText.onPrepare=准备时

Element.AutoText.onPrepare.this=AutoText
Element.AutoText.onPrepare.toolTip=在元素设置过程中执行。 在此更改元素设计。 数据绑定尚不可用。

Element.AutoText.onPrepare.reportContext=报表上下文

Element.AutoText.onCreate=创建时

Element.AutoText.onCreate.toolTip=在工厂中创建该元素时执行的脚本。

Element.AutoText.onCreate.this=自动图文集实例

Element.AutoText.onCreate.reportContext=报表上下文

Element.AutoText.onRender=绘制时

Element.AutoText.onRender.toolTip=准备该元素以便在展示引擎中呈现时执行的脚本。

Element.AutoText.onRender.this=自动图文集实例

Element.AutoText.onRender.reportContext=报表上下文

Element.AutoText.onPageBreak=出现分页符时

Element.AutoText.onPageBreak.toolTip=遇到分页符时，对页面中的所有报表项执行

Element.AutoText.onPageBreak.this=自动图文集实例

Element.AutoText.onPageBreak.reportContext=报表上下文

Element.AutoText.pageVariable=页面变量

#45. Cube ( prefix Element.Cube )
Element.Cube.filter=筛选条件

Element.Cube.defaultMeasureGroup=默认度量组

Element.Cube.ACLExpression=ACL 表达式

#46. Dimension ( prefix Element.Dimension )
Element.Dimension.isTimeType=是时间类型

Element.Dimension.defaultHierarchy=默认层次结构

Element.Dimension.ACLExpression=ACL 表达式

#47. Hierarchy ( prefix Element.Hierarchy )
Element.Hierarchy.filter=筛选条件

#48. Level ( prefix Element.Level )
Element.Level.dataType=数据类型

Element.Level.dateTimeLevelType=日期时间级别类型

Element.Level.dateTimeFormat=日期时间格式

Element.Level.Interval=间隔

Element.Level.intervalBase=间隔基数

Element.Level.interval=间隔

Element.Level.intervalRange=间隔范围

Element.Level.levelType=级别类型

Element.Level.staticValues=静态值

Element.Level.defaultValue=默认值

Element.Level.ACLExpression=ACL 表达式

Element.Level.MemberACLExpression=成员 ACL 表达式

Element.Level.action=操作

Element.Level.format=格式

Element.Level.alignment=对齐

#49. Measure ( prefix Element.Measure )
Element.Measure.function=函数

Element.Measure.isCalculated=已计算

Element.Measure.measureExpression=度量表达式

Element.Measure.dataType=数据类型

Element.Measure.ACLExpression=ACL 表达式

Element.Measure.action=操作

Element.Measure.format=格式

Element.Measure.alignment=对齐

Element.Measure.isVisible=可见

#50. MeasureGroup ( prefix Element.MeasureGroup )
Element.MeasureGroup.measures=度量

#51. TabularCube ( prefix Element.TabularCube )
Element.TabularCube.dataSet=数据集

Element.TabularCube.dimensions=维

Element.TabularCube.measureGroups=度量组

Element.TabularCube.dimensionConditions=维条件

Element.TabularCube.autoKey=自动主键

#52. TabularDimension ( prefix Element.TabularDimension )
Element.TabularDimension.hierarchies=层次结构

Element.TabularDimension.internalDimensionRef=内部维引用

#53. TabularHierarchy ( prefix Element.TabularHierarchy )
Element.TabularHierarchy.dataSet=数据集

Element.TabularHierarchy.levels=级别

Element.TabularHierarchy.primaryKeys=主键

#54. TabularLevel ( prefix Element.TabularLevel )
Element.TabularLevel.columnName=列名称

Element.TabularLevel.displayColumnName=显示列名称

Element.TabularLevel.attributes=属性

#55. OdaCube ( prefix Element.OdaCube )
Element.OdaCube.dimensions=维

Element.OdaCube.measureGroups=度量组

Element.OdaCube.nativeName=本地名称

#56. OdaDimension ( prefix Element.OdaDimension )
Element.OdaDimension.hierarchies=层次结构

Element.OdaDimension.nativeName=本地名称

#57. OdaHierarchy ( prefix Element.OdaHierarchy )
Element.OdaHierarchy.levels=级别

Element.OdaHierarchy.nativeName=本地名称

#58. OdaLevel ( prefix Element.OdaLevel )
Element.OdaLevel.attributes=属性

#59. MemberValue ( prefix Element.MemberValue )
Element.MemberValue.value=值

Element.MemberValue.level=级别

Element.MemberValue.memberValues=成员值

Element.MemberValue.filter=筛选器

#60. SortElement ( prefix Element.SortElement )
Element.SortElement.key=关键字

Element.SortElement.direction=方向

Element.SortElement.member=成员

Element.SortElement.strength=排序强度

Element.SortElement.locale=排序区域设置

#61. FilterConditionElement ( prefix Element.FilterConditionElement )
Element.FilterConditionElement.operator=运算符

Element.FilterConditionElement.expr=表达式

Element.FilterConditionElement.value1=值 1

Element.FilterConditionElement.value2=值 2

Element.FilterConditionElement.filterTarget=筛选目标

Element.FilterConditionElement.member=成员

Element.FilterConditionElement.isOptional=可选

Element.FilterConditionElement.extensionName=扩展名

Element.FilterConditionElement.extensionExprId=扩展表达式 Id

Element.FilterConditionElement.pushDown=下推

Element.FilterConditionElement.dynamicFilterParameter=动态筛选器参数

Element.FilterConditionElement.type=类型

Element.FilterConditionElement.updateAggregation=更新聚合


#62. AbstractMultiViews ( prefix Element.AbstractMultiViews )
Element.AbstractMultiViews.index=索引

#63. MultiViews ( prefix Element.MultiViews )
Element.MultiViews.views=视图

#64. VariableElement ( prefix Element.VariableElement )
Element.VariableElement.name=名称

Element.VariableElement.value=值

Element.VariableElement.type=类型

#65. DataGroup  ( prefix  Element.DataGroup )
Element.DataGroup.groupName=名称

Element.DataGroup.keyExpr=关键字表达式

Element.DataGroup.sort=排序

Element.DataGroup.filter=筛选器

Element.DataGroup.Interval=间隔

Element.DataGroup.interval=间隔

Element.DataGroup.intervalBase=间隔基数

Element.DataGroup.intervalRange=间隔范围

Element.DataGroup.sortDirection=排序方向

Element.DataGroup.sortType=排序类型

#66. DerivedDataSet ( prefix Element.DerivedDataSet )
Element.DerivedDataSet.inputDataSets=输入数据集

Element.DerivedDataSet.queryText=查询文本

Element.DerivedDataSet.extensionID=扩展 ID

#67. AbstractScalarParameter  ( prefix  AbstractScalarParameter )
Element.AbstractScalarParameter.isRequired=必需

Element.AbstractScalarParameter.dynamicList=动态列表

Element.AbstractScalarParameter.dynamicList.dataSetName=数据集名称

Element.AbstractScalarParameter.dynamicList.linkedDataModelName=链接的数据模型名称

Element.AbstractScalarParameter.dynamicList.valueExpr=值表达式

Element.AbstractScalarParameter.dynamicList.labelExpr=标签表达式

Element.AbstractScalarParameter.listLimit=列表限制

Element.AbstractScalarParameter.sortDirection=排序方向

Element.AbstractScalarParameter.sortBy=排序依据

Element.AbstractScalarParameter.valueType=值类型

Element.AbstractScalarParameter.sortByColumn=按列排序

Element.AbstractScalarParameter.selectionList=选择列表

Element.AbstractScalarParameter.dataType=数据类型

Element.AbstractScalarParameter.distinct=特异

#68. DynamicFilterParameter  ( prefix  Element.DynamicFilterParameter )
Element.DynamicFilterParameter.displayType=显示类型

Element.DynamicFilterParameter.filterOperator=筛选运算符

Element.DynamicFilterParameter.column=列

Element.DynamicFilterParameter.defaultValue=默认值

Element.DynamicFilterParameter.nativeDataType=本地数据类型

Element.DynamicFilterParameter.controlType=控件类型

#78 DataMart ( prefix  Element.DataMart )
Element.DataMart.slot.dataSources=数据源

Element.DataMart.slot.dataSets=数据集

Element.DataMart.slot.cubes=多维数据集

Element.DataMart.slot.parameters=参数

Element.DataMart.slot.dimensions=共享维

Element.DataMart.enableACL=启用数据安全性

Element.DataMart.ACLExpression=ACL 表达式

Element.DataMart.enableIncrementalUpdate=启用增量更新

#79 DataMartDataSource ( prefix Element.DataMartDataSource )
Element.DataMartDataSource.datamartURL =Data mart URL

Element.DataMartDataSource.datamartVersion=Data mart 版本

Element.DataMartDataSource.paramBindings=参数绑定

Element.DataMartDataSource.accessType=访问类型

#80 DataMartDataSet ( prefix Element.DataMartDataSet )
Element.DataMartDataSet.dataSource=数据源

Element.DataMartDataSet.dataObject=数据对象

Element.DataMartDataSet.selectedColumns=选定列

#81 DataMartCube ( prefix Element.DataMartCube )
Element.DataMartCube.dataSource=数据源

Element.DataMartCube.dataObject=数据对象

#82 TimeDimension ( prefix Element.TimeDimension )
Element.TimeDimension.startTimePoint=开始时间

Element.TimeDimension.endTimePoint=结束时间

Element.TimeDimension.hierarchies=层次结构

#83 DataMartLinkedDataModel ( prefix Element.DataMartLinkedDataModel )
Element.DataMartLinkedDataModel.dataSource=数据源

Element.DataMartLinkedDataModel.dataObject=数据对象

#63 ReportItemTheme ( Prefix Element.ReportItemTheme )
Element.ReportItemTheme.slot.styles=样式

Element.ReportItemTheme.type=类型

Element.ReportItemTheme.customValues=自定义值

#64 AbstractTheme ( Prefix Element.AbstractTheme )
Element.AbstractTheme.cssStyleSheets=CSS 样式表

#Category
Element.Category.helpText = 帮助文本

Element.Category.helpTextKey = 帮助文本资源关键字

#Category Column
Element.CategoryColumn.aggregationFunction = 聚合函数

Element.CategoryColumn.aliasDataSet = 别名数据集

Element.CategoryColumn.dataType = 数据类型

Element.CategoryColumn.expression =表达式

Element.CategoryColumn.resultSetColumnName = 结果集列名称

Element.CategoryColumn.helpText =帮助文本

Element.CategoryColumn.ACLExpression =ACL 表达式

Element.CategoryColumn.heading =标题

Element.CategoryColumn.horizontalAlign =水平对齐

Element.CategoryColumn.format =格式

Element.CategoryColumn.valueFormat =值格式

Element.CategoryColumn.action =操作

Element.CategoryColumn.analysis =分析

###########################################################
# Predefined style
Style.Report=报表

Style.List=列表


Style.GroupHeader1=GroupHeader1
Style.GroupHeader2=GroupHeader1
Style.GroupHeader3=GroupHeader1
Style.GroupHeader4=GroupHeader1
Style.GroupHeader5=GroupHeader1
Style.GroupHeader6=GroupHeader1
Style.GroupHeader7=GroupHeader1
Style.GroupHeader8=GroupHeader8
Style.GroupHeader9=GroupHeader9
Style.GroupFooter1=GroupFooter1
Style.GroupFooter2=GroupFooter2
Style.GroupFooter3=GroupFooter3
Style.GroupFooter4=GroupFooter4
Style.GroupFooter5=GroupFooter5
Style.GroupFooter6=GroupFooter6
Style.GroupFooter7=GroupFooter7
Style.GroupFooter8=GroupFooter8
Style.GroupFooter9=GroupFooter9

Style.ListHeader=ListHeader
Style.ListFooter=ListFooter
Style.ListGroupHeader1=ListGroupHeader1

Style.ListGroupHeader2=ListGroupHeader2

Style.ListGroupHeader3=ListGroupHeader3

Style.ListGroupHeader4=ListGroupHeader4

Style.ListGroupHeader5=ListGroupHeader5

Style.ListGroupHeader6=ListGroupHeader6

Style.ListGroupHeader7=ListGroupHeader7

Style.ListGroupHeader8=ListGroupHeader8

Style.ListGroupHeader9=ListGroupHeader9

Style.ListGroupFooter1=ListGroupFooter1

Style.ListGroupFooter2=ListGroupFooter2

Style.ListGroupFooter3=ListGroupFooter3

Style.ListGroupFooter4=ListGroupFooter4

Style.ListGroupFooter5=ListGroupFooter5

Style.ListGroupFooter6=ListGroupFooter6

Style.ListGroupFooter7=ListGroupFooter7

Style.ListGroupFooter8=ListGroupFooter8

Style.ListGroupFooter9=ListGroupFooter9


Style.ListDetail=ListDetail
Style.Table=表

Style.TableHeader=TableHeader
Style.TableHeaderCell=TableHeaderCell
Style.TableFooter=TableFooter
Style.TableFooterCell=TableFooterCell
Style.TableGroupHeader1=TableGroupHeader1

Style.TableGroupHeader2=TableGroupHeader2

Style.TableGroupHeader3=TableGroupHeader3

Style.TableGroupHeader4=TableGroupHeader4

Style.TableGroupHeader5=TableGroupHeader5

Style.TableGroupHeader6=TableGroupHeader6

Style.TableGroupHeader7=TableGroupHeader7

Style.TableGroupHeader8=TableGroupHeader8

Style.TableGroupHeader9=TableGroupHeader9

Style.TableGroupHeaderCell=TableGroupHeaderCell

Style.TableGroupFooter1=TableGroupFooter1

Style.TableGroupFooter2=TableGroupFooter2

Style.TableGroupFooter3=TableGroupFooter3

Style.TableGroupFooter4=TableGroupFooter4

Style.TableGroupFooter5=TableGroupFooter5

Style.TableGroupFooter6=TableGroupFooter6

Style.TableGroupFooter7=TableGroupFooter7

Style.TableGroupFooter8=TableGroupFooter8

Style.TableGroupFooter9=TableGroupFooter9

Style.TableGroupFooterCell=TableGroupFooterCell


Style.TableColumnHeader=TableColumnHeader
Style.TableDetail=TableDetail
Style.TableDetailCell=TableDetailCell
Style.Page=页面

Style.Matrix=矩阵

Style.MatrixRowHeader1=MatrixRowHeader1

Style.MatrixRowHeader2=MatrixRowHeader2

Style.MatrixRowHeader3=MatrixRowHeader3

Style.MatrixRowHeader4=MatrixRowHeader4

Style.MatrixRowHeader5=MatrixRowHeader5

Style.MatrixRowHeader6=MatrixRowHeader6

Style.MatrixRowHeader7=MatrixRowHeader7

Style.MatrixRowHeader8=MatrixRowHeader8

Style.MatrixRowHeader9=MatrixRowHeader9

Style.MatrixRowFooter1=MatrixRowFooter1

Style.MatrixRowFooter2=MatrixRowFooter2

Style.MatrixRowFooter3=MatrixRowFooter3

Style.MatrixRowFooter4=MatrixRowFooter4

Style.MatrixRowFooter5=MatrixRowFooter5

Style.MatrixRowFooter6=MatrixRowFooter6

Style.MatrixRowFooter7=MatrixRowFooter7

Style.MatrixRowFooter8=MatrixRowFooter8

Style.MatrixRowFooter9=MatrixRowFooter9

Style.MatrixColumnHeader1=MatrixColumnHeader1

Style.MatrixColumnHeader2=MatrixColumnHeader2

Style.MatrixColumnHeader3=MatrixColumnHeader3

Style.MatrixColumnHeader4=MatrixColumnHeader4

Style.MatrixColumnHeader5=MatrixColumnHeader5

Style.MatrixColumnHeader6=MatrixColumnHeader6

Style.MatrixColumnHeader7=MatrixColumnHeader7

Style.MatrixColumnHeader8=MatrixColumnHeader8

Style.MatrixColumnHeader9=MatrixColumnHeader9

Style.MatrixColumnFooter1=MatrixColumnFooter1

Style.MatrixColumnFooter2=MatrixColumnFooter2

Style.MatrixColumnFooter3=MatrixColumnFooter3

Style.MatrixColumnFooter4=MatrixColumnFooter4

Style.MatrixColumnFooter5=MatrixColumnFooter5

Style.MatrixColumnFooter6=MatrixColumnFooter6

Style.MatrixColumnFooter7=MatrixColumnFooter7

Style.MatrixColumnFooter8=MatrixColumnFooter8

Style.MatrixColumnFooter9=MatrixColumnFooter9


Style.MatrixCell=MatrixCell
Style.Chart=图表

Style.ChartTitle=ChartTitle
Style.ChartLegend=ChartLegend
Style.ChartValue=ChartValue
Style.ChartAxis=ChartAxis
Style.Label=标签

Style.Data=数据

Style.Number=数值

Style.String=字符串

Style.Text=文本

Style.Grid=网格

Style.Toc=TOC Level0

Style.Toc1=TOC Level1

Style.Toc2=TOC Level2

Style.Toc3=TOC Level3

Style.Toc4=TOC Level4

Style.Toc5=TOC Level5

Style.Toc6=TOC Level6

Style.Toc7=TOC Level7

Style.Toc8=TOC Level8

Style.Toc9=TOC Level9


Style.FreeForm=FreeForm
Style.Line=行

Style.Rectangle=矩形

Style.TextData=TextData
Style.ExtendedItem=ExtendedItem
Style.Image=图像

###########################################################
# Messages.
Message.NamePrefixNew=新建

Message.ChangeProperty=更改属性:{0}

Message.ChangePropertyDefinition=更改属性定义

Message.MoveItem=移动项目

Message.AddTranslation=添加转换

Message.DropTranslation=删除转换

Message.ChangeTranslation=更改转换

Message.MoveContent=移动内容

Message.AddProperty=添加属性

Message.DropProperty=删除属性

Message.SetName=设定名称

Message.AddElement=添加元素

Message.DropElement=删除元素

Message.SetStyle=设定样式

Message.ReplaceItem=替换项目

Message.SetLocale=设定位置

Message.SetTranslationText=设定转换文本

Message.MoveElement=移动元素

Message.InsertElement=插入元素

Message.DeleteElement=删除元素

Message.SetExtends=设定扩展

Message.ChangeItem=更改项

Message.AddItem=添加项

Message.InsertItem=插入项

Message.RemoveItem=删除项

Message.ReplaceElement=替换元素

Message.SetTheme=设置布景主题

Message.ChangePropertyEncryption=更改属性的加密:{0}

Message.RenameCSSFile=重命名 CSS 文件

Message.ImportCSSStyles=导入 CSS 样式

Message.InsertAndPasteColumnBand=插入和粘贴列

Message.PasteColumnBand=粘贴列

Message.ShiftColumnBand=移动列

Message.InsertColumnBand=插入列

Message.InsertRow=插入行

Message.InsertAndPasteRow=插入和粘贴行

Message.PasteRow=粘贴行

Message.ShiftRow=移动行

Message.ClearProperties=清除属性

Message.CreateTemplateElement=创建模板元素

Message.TransformToReportItem=转换为报表项

Message.TransformToDataSet=转换为数据集

Message.ExportElementToLibrary=元素导出到库

Message.ExportElementToDataMart=将元素导出到 DataMart

###########################################################
#Element new names.

New.ReportDesign=NewReportDesign
New.Parameter=NewParameter
New.CascadingParameterGroup=新建级联参数

New.ScalarParameter=NewParameter
New.DynamicFilterParameter=新动态筛选器参数

New.ParameterGroup=NewParameterGroup
New.Style=NewStyle
New.MasterPage=NewMasterPage
New.ReportItem=NewReportItem
New.Label=NewLabel
New.FreeForm=NewFreeForm
New.Data=NewData
New.Text=NewText
New.ListingGroup=NewListingGroup
New.List=NewList
New.ListGroup=NewListGroup
New.Listint=NewListing
New.Table=NewTable
New.TableGroup=NewTableGroup
New.Column=NewColumn
New.Row=新行

New.Cell=NewCell
New.Image=NewImage
New.Grid=NewGrid
New.Line=NewLine
New.Tile=NewTile
New.Rectangle=NewRectangle
New.GraphicMasterPage=新图形主页

New.SimpleMasterPage=新简单主页

New.TextData=NewTextData
New.ExtendedItem=NewExtendedItem
New.ScriptDataSource=新脚本数据源

New.ScriptDataSet=新脚本数据集

New.OdaDataSource=新数据ODA数据源

New.OdaDataSet=新ODA数据集

New.TemplateParameterDefinition=新模板参数定义

New.TemplateDataSet=新模板数据集

New.TemplateReportItem=新模板报表项

New.Theme=NewTheme
New.AutoText=NewAutoText
New.TabularCube=多维数据集

New.TabularDimension=组

New.TabularHierarchy=NewTabularHierarchy
New.TabularLevel=NewTabularLevel
New.TabularMeasure=NewTabularMeasure
New.TabularMeasureGroup=摘要字段

New.OdaCube=新建 ODA 多维数据集

New.OdaDimension=NewOdaDimension
New.OdaHierarchy=NewOdaHierarchy
New.OdaLevel=NewOdaLevel
New.OdaMeasure=NewOdaMeasure
New.OdaMeasureGroup=NewOdaMeasureGroup
New.DerivedDataSet=NewDerivedDataSet
New.VariableElement=NewVariableElement
New.DataMartDataSource=NewDataMartDataSource
New.DataMartDataSet=NewDataMartDataSet
New.DataMartCube=NewDataMartCube
New.TimeDimension=NewTimeDimension
New.ReportItemTheme=新报表项背景主题

New.CategoryColumn=NewCategoryColumn

###########################################################
#Default theme name.
Theme.defaultThemeName=defaultTheme

###########################################################
# Error Messages

#ContentException
Error.ContentException.SLOT_NOT_FOUND={0} 没有已定义的属性框 (id:{1})。

Error.ContentException.WRONG_TYPE={0} 的属性框 ({2}) 中不允许 {1}。

Error.ContentException.CONTENT_NOT_FOUND={0} 未显示在 {2} 的属性框 ({1}) 中。

Error.ContentException.NOT_CONTAINER={0} 不是容器。

Error.ContentException.SLOT_IS_FULL={1} 的属性框 ({0}) 已满。

Error.ContentException.RECURSIVE={0} 在某些层包含 {1}，因此不能将前者移至后者。

Error.ContentException.HAS_NO_CONTAINER={0} 没有容器，这就表示不能将其添加至报表中。

Error.ContentException.MOVE_FORBIDDEN=由于 {0} 或其内容正被引用，因此不能移动。

Error.ContentException.HAS_DESCENDENTS=由于 {0} 具有附属项，不能移动或删除。

Error.ContentException.INVALID_CONTEXT_CONTAINMENT={1} 的属性框 ({2}) 中不允许直接或间接存在 {0}。

Error.ContentException.STRUCTURE_CHANGE_FORBIDDEN=元素 "{0}" 是子元素或虚拟元素（在子元素内部），无法更改其结构。

Error.ContentException.CONTENT_NAME_REQUIRED=由于 {0} 没有名称，不允许添加到 {1} 的属性框 {2} 中。

Error.ContentException.INVALID_TEMPLATE_ELEMENT={0} 没有显式模板参数定义，因此不能插入到 {1} 的属性框 ({2}) 中。

Error.ContentException.TEMPLATE_TRANSFORM_FORBIDDEN=该元素不能转换为模板。

Error.ContentException.GROUPS_CHANGE_FORBIDDEN=元素“{0}”具有报表项共享，不能添加、删除或移动其组。

Error.ContentException.CONTENT_NOT_ALLOWED_PASTED=复制内容不允许粘贴到 {0} 的属性框 {1}，因为它无效。

Error.ContentException.CONTENT_ALREADY_INSERTED=不允许将元素“{0}”添加到“{1}”的 ({2})，因为它已经在设计树中。

Error.ContentException.INVALID_POSITION=位置超出范围且无效。

Error.ContentException.SHARE_DIMENSION_NOT_EXIST=无法在报表/库中插入元素 [{0}]，因为共享维 [{1}] 不存在。

#CustomMsgException
Error.CustomMsgException.RESOURCE_KEY_REQUIRED=必须提供转换的资源关键字。

Error.CustomMsgException.DUPLICATE_LOCALE=已定义资源 "{1}" 的区域设置 "{0}" 的转换。

Error.CustomMsgException.INVALID_LOCALE=区域设置 "{0}" 为无效的区域设置名称。

Error.CustomMsgException.TRANSLATION_NOT_FOUND=未找到资源 "{1}" 的区域设置 "{0}" 的转换。

    
#ExtendsException

#InvalidParentException
Error.InvalidParentException.NO_PARENT=元素“{0}”没有父项，不能本地化。

Error.InvalidParentException.PARENT_NOT_FOUND=父元素“{0}”不存在。

Error.InvalidParentException.UNNAMED_PARENT=父元素无名称。

Error.InvalidParentException.PARENT_NOT_INCLUDE=不包含父元素“{0}”的库。

#WrongTypeException
Error.WrongTypeException.WRONG_TYPE=父元素 {0} 与目标元素 {1} 的元素类型不同。

Error.WrongTypeException.WRONG_EXTENSION_TYPE=父元素 {0} 与目标元素 {1} 的扩展名类型不同。

#CircularExtendsException
Error.CircularExtendsException.SELF_EXTEND={0} 本身不能扩展。

Error.CircularExtendsException.CIRCULAR=由于 {0} 已扩展自 {1}，因此找到循环扩展。

#ExtendsForbiddenException
Error.ExtendsForbiddenException.PARENT_NOT_IN_COMPONENT=父元素“{0}”不是组件中的顶层报表项。

Error.ExtendsForbiddenException.EXTENDS_FORBIDDEN=元素“{0}”在子元素内或已有父元素，无法扩展。

Error.ExtendsForbiddenException.CANT_EXTEND=不允许衍生元素“{0}”。

Error.ExtendsForbiddenException.RESULT_SET_SHARED_CANT_EXTEND=不允许衍生共享结果集报表项“{0}”。
    
#NameException
Error.NameException.NAME_REQUIRED=要求提供 {0} 的名称。

Error.NameException.NAME_FORBIDDEN={0} 不允许具有名称。

Error.NameException.DUPLICATE=名称 "{0}" 与现有名称重复。请选择一个不同的名称。

Error.NameException.HAS_REFERENCES={0} 具有衍生元素，因此无法清除该名称。

Error.NameException.INVALID_NAME=名称“{0}”无效，因为它包含字符 "."、"/"、"\"、"!"、";" 和 ","。

Error.NameException.INVALID_STYLE_NAME=样式名称“{0}”对于 CSS2 规格无效。
    
#PropertyNameException
Error.PropertyNameException.PROPERTY_NOT_VALID=属性 "{0}" 对元素 "{1}" 无效。

Error.PropertyNameException.MEMBER_NOT_VALID=成员 "{0}" 对结构 "{1}" 无效。

    
#PropertyValueException
Error.PropertyValueException.INVALID_VALUE=具有类型 "{1}" 的值 "{0}" 无效。

Error.PropertyValueException.PROPERTY_CHANGE_FORBIDDEN=由于元素 "{1}" 包括在子元素内，不能更改属性 "{0}" 值。

Error.PropertyValueException.CHOICE_NOT_FOUND=未找到选择值 "{0}"。

Error.PropertyValueException.NOT_LIST_TYPE=属性 "{0}" 不是列表类型。

Error.PropertyValueException.ITEM_NOT_FOUND=未在属性 "{0}" 中找到指定结构。

Error.PropertyValueException.WRONG_ELEMENT_TYPE="{0}" 不是有效的元素引用类型。

Error.PropertyValueException.VALUE_EXISTS=值 "{0}" 已存在。

Error.PropertyValueException.VALUE_REQUIRED=要求提供属性 "{0}" 的值。

Error.PropertyValueException.VALUE_LOCKED=由于属性 "{0}" 已锁定，因此无法设定其值。

Error.PropertyValueException.UNIT_NOT_ALLOWED=属性 "{1}" 不允许存在单位 "{0}"。

Error.PropertyValueException.CHOICE_NOT_ALLOWED=不允许存在选择值 "{0}"。

Error.PropertyValueException.WRONG_ITEM_TYPE=结构类型为 "{0}"，而非 "{1}"。

Error.PropertyValueException.NEGATIVE_VALUE=属性 "{1}" 的值 "{0}" 无效，该值不能为负。

Error.PropertyValueException.NON_POSITIVE_VALUE=属性 "{1}" 的值 "{0}" 无效，该值应为正值。

Error.PropertyValueException.EXTENSION_SETTING_FORBIDDEN=ExtendedItem“{1}”的属性“{0}”禁止通过命令设置。

Error.PropertyValueException.DOT_FORBIDDEN=名称 "{0}" 无效，因为它包含字符 "."。

Error.PropertyValueException.NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE=不支持报表项主题类型“{0}”。
    
#SemanticError
Error.SemanticError.INCONSISTENT_TABLE_COL_COUNT=表 "{0}" 的列计数不一致。

Error.SemanticError.INCONSISTENT_TABLE_COL_COUNT_COZ_DROP=由于组标题中某些单元格的删除属性，表“{0}”具有不一致的列计数。

Error.SemanticError.INCONSISTENT_GRID_COL_COUNT=网格 "{0}" 的列计数不一致。

Error.SemanticError.OVERLAPPING_CELLS={0} 中的行具有重叠单元格。

Error.SemanticError.INCONSISTENT_TABLE_DROP_HEADINGS=表 "{0}" 定义的放置标题无效。

Error.SemanticError.INVALID_PAGE_SIZE=主页 "{0}" 的页面大小无效。

Error.SemanticError.MISSING_PAGE_SIZE=由于页面类型 "{0}" 为自定义类型，因此要求提供主页 "{0}" 的大小。

Error.SemanticError.CANNOT_SPECIFY_PAGE_SIZE=由于页面类型 "{0}" 不是自定义类型，因此不能指定主页 "{0}" 的大小。

Error.SemanticError.INVALID_PAGE_MARGINS=主页 "{0}" 的边距值无效。

Error.SemanticError.MISSING_MASTER_PAGE=报表至少必须具有一个主页。

Error.SemanticError.INVALID_MULTI_COLUMN=主页“{0}”在页面内容外具有列跨度。

Error.SemanticError.INVALID_ELEMENT_REF=元素“{0}”具有引用未定义的元素或引用自身的属性“{1}”:“{2}”。

Error.SemanticError.INVALID_DATA_BINDING_REF=元素“{0}”具有属性 dataBindingRef，该属性引用的元素“{1}”是该元素的容器或内容。

Error.SemanticError.MISSING_SQL_STMT=JDBC 选择数据集 "{0}" 必须具有 SQL 语句。

Error.SemanticError.MISSING_DATA_SET={0} 必须能够访问一个数据集。

Error.SemanticError.INVALID_IMAGE_SCALE_VALUE=图像 "{0}" 的缩放值必须为正。

Error.SemanticError.IMAGE_FILE_NOT_FOUND=未在图像 "{1}" 中找到已定义的文件 "{0}"。

Error.SemanticError.NEGATIVE_REPORT_REFRESH_RATE=报表的刷新率必须为非负值。

Error.SemanticError.NEGATIVE_CELL_COLUMN=表/网格/表组 "{0}" 具有起始列为负的单元格。

Error.SemanticError.NEGATIVE_WIDTH=元素 "{0}" 或其内容的宽度不得为负。

Error.SemanticError.NEGATIVE_HEIGHT=元素 "{0}" 或其内容的高度不得为负。

Error.SemanticError.INVALID_COLUMN_REPEAT=表 "{0}" 具有重复项小于 1 的列。

Error.SemanticError.INVALID_PAGE_COLUMNS=主页 "{0}" 具有小于 1 的列。

Error.SemanticError.NEGATIVE_FONT_SIZE=样式 "{0}" 的字体大小不得为负。

Error.SemanticError.NEGATIVE_BORDER_WIDTH=样式 "{0}" 的边框宽度不得为负。

Error.SemanticError.INVALID_PROPERTY_NAME=未定义元素 "{1}" 中的属性 "{0}"。

Error.SemanticError.UNSUPPORTED_ELEMENT=仍不支持元素 {0}。

Error.SemanticError.AT_LEAST_ONE_COLUMN=数据集 "{0}" 的结果集至少必须具有一个结果集列。

Error.SemanticError.NEGATIVE_LINE_HEIGHT=元素 "{0}" 的行高必须为非负值。

Error.SemanticError.DUPLICATE_GROUP_NAME=表/列表 "{0}" 具有重复的组名 "{1}"。

Error.SemanticError.INVALID_CUSTOM_COLOR_NAME=由于自定义颜色名称 "{0}" 为 CSS 标准颜色名称，因此不能使用。

Error.SemanticError.DUPLICATE_CUSTOM_COLOR_NAME=调色板上具有重复的自定义颜色名称“{0}”。

Error.SemanticError.EXTENSION_NOT_FOUND=未找到元素 "{1}" 的扩展名 "{0}"。

Error.SemanticError.MISSING_EXTENSION=必须定义元素 "{0}" 的扩展名。

Error.SemanticError.PROPERTY_COPY_FORBIDDEN=禁止复制属性 "{0}"。

Error.SemanticError.COLUMN_COPY_FORBIDDEN=禁止复制元素 "{1}" 的列 "{0}"。

Error.SemanticError.COLUMN_PASTE_FORBIDDEN=禁止粘贴元素 "{1}"。

Error.SemanticError.COLUMN_PASTE_DIFFERENT_LAYOUT=粘贴区域与复制区域的布局不同。

Error.SemanticError.INVALID_STRUCTURE_REF=元素 "{0}" 具有属性 "{1}"，该属性引用了未定义的元素 "{2}".

Error.SemanticError.INVALID_SCALAR_PARAMETER_TYPE=类型标量参数 "{0}" 无效。级联参数组内的参数应为动态类型。

Error.SemanticError.MISSING_TEMPLATE_PARAMETER_TYPE=元素 {0} 必须显式定义 "allowedType"。

Error.SemanticError.INCONSISTENT_TEMPLATE_PARAMETER_TYPE=元素 {0} 中的默认元素 {1} 不是一种 {2}。

Error.SemanticError.INVALID_LIBRARY_REFERENCE={0} "{1}" 的库引用 "{2}" 无效，因为引用的结构不存在。

Error.SemanticError.INVALID_PROPERTY_BINDING_ID={0} 中的属性绑定 "{1}" 引用不存在的元素。

Error.SemanticError.PROPERTY_BINDING_FORBIDDEN=元素 "{0}" 不在设计树中，因此它不能设置任何属性绑定。

Error.SemanticError.INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT=主页 "{0}" 内部不允许直接或间接存在表或列表。

Error.SemanticError.DATA_SET_MISSED_IN_JOINT_DATA_SET = 联接数据集“{0}”中缺少数据集“{1}”。

Error.SemanticError.MISSING_COLUMN_BINDING = 未定义数据“{0}”与列名称“{1}”的列绑定。

Error.SemanticError.COLUMN_INSERT_FORBIDDEN=禁止对元素“{0}”执行插入操作。

Error.SemanticError.CANNOT_SPECIFY_VALUE=由于“{4}”的值“{3}”，元素“{0}”无法为属性“{2}”指定值“{1}”。

Error.SemanticError.ROW_COPY_FORBIDDEN=禁止对元素“{0}”执行复制操作。

Error.SemanticError.ROW_PASTE_FORBIDDEN=禁止对元素“{0}”执行粘贴操作。

Error.SemanticError.ROW_INSERT_FORBIDDEN=禁止对元素“{0}”执行插入操作。

Error.SemanticError.ROW_SHIFT_FORBIDDEN=禁止对元素“{0}”执行移位操作。

Error.SemanticError.ROW_INSERTANDPASTE_FORBIDDEN=禁止对元素“{0}”执行插入和粘贴操作。

Error.SemanticError.CIRCULAR_ELEMENT_REFERNECE=找到循环元素引用，因为 {0} 已经引用 {1}。

Error.SemanticError.INCONSISTENT_DATA_GROUP=元素 {0} 的数据绑定引用与它引用的元素 {1} 的组数不同。

Error.SemanticError.INVALID_MANIFEST=元素 {0} 具有一个无效清单。

Error.SemanticError.VALUE_FORBIDDEN=列表属性“{0}”为空，它不能具有任何项。

Error.SemanticError.TABLE_NO_COLUMN_FOUND = 无法计算表格宽度，因为没有找到任何列。

Error.SemanticError.TABLE_COLUMN_WITH_NO_WIDTH = 无法计算表格宽度，因为索引 {0} 处的列宽未定义。

Error.SemanticError.TABLE_COLUMN_INCONSISTENT_UNIT_TYPE = 无法计算表格宽度，因为它包含使用绝对单位和相对单位的多列，而不是百分比。

Error.SemanticError.TABLE_COLUMN_INCONSISTENT_RELATIVE_UNIT = 无法计算表格宽度，因为它包含使用不同相对单位的多列。

Error.SemanticError.TABLE_COLUMN_ILLEGAL_PERCENTAGE = 无法计算表格宽度，因为使用百分比的列宽总和大于或等于 100%。

Error.SemanticError.CANNOT_SPECIFY_DATA_OBJECT=报表项目“{0}”无法指定多维数据集或数据集，因为外部报表项目可能会定义不同的多维数据集或数据集和多个视图。

#SemanticException
Error.SemanticException.EXPORT_ELEMENT_FAIL=目标库中元素名称重复，元素 {0} 无法导出。请重命名导出的元素名称。

#StyleException
Error.StyleException.FORBIDDEN={0} 不得具有样式。

Error.StyleException.NOT_FOUND=未找到 {1} 使用的样式 "{0}"。
    
#ThemeException
Error.ThemeException.NOT_FOUND=未找到 {1} 使用的布景主题“{0}”。

Error.ThemeException.WRONG_TYPE={1} 使用的布景主题“{0}”是错误类型报表项布景主题。
    
#DesignParserException
Error.DesignParserException.FILE_NOT_FOUND=未找到文件 "{0}"。

Error.DesignParserException.FILE_FORMAT_NOT_SUPPORT=不支持文件“{0}”的格式。

Error.DesignParserException.RGB_REQUIRED=自定义颜色必须具有正确的 RGB 值。

Error.DesignParserException.COLOR_NAME_REQUIRED=自定义颜色必须具有颜色名称。

Error.DesignParserException.ILLEGAL_EXTENDS=元素无法扩展。

Error.DesignParserException.IMAGE_REF_CONFLICT=图像具有冲突引用。

Error.DesignParserException.INVALID_IMAGEREF_EXPR_VALUE=图像引用表达式值无效。

Error.DesignParserException.INVALID_IMAGE_URL_VALUE=图像 URL 值无效。

Error.DesignParserException.INVALID_IMAGE_NAME_VALUE=图像名称值无效。

Error.DesignParserException.ACTION_REPORTNAME_REQUIRED=该操作要求提供报表名称。

Error.DesignParserException.ACTION_PARAMETER_NAME_REQUIRED=该操作要求提供参数名称。

Error.DesignParserException.CHOICE_RESTRICTION_VIOLATION=已违反选择限制。

Error.DesignParserException.MESSAGE_KEY_REQUIRED=用户定义的消息必须指定“关键字”值。

Error.DesignParserException.DUPLICATE_TRANSLATION_LOCALE=具有相同区域设置的两个转换出现在一个用户定义消息中。

Error.DesignParserException.NAME_REQUIRED=要求提供元素属性名称或结构成员名称。

Error.DesignParserException.WRONG_STRUCTURE_LIST_TYPE=此类元素属性或结构成员不是结构列表类型。

Error.DesignParserException.WRONG_EXTENDED_PROPERTY_TYPE=该属性不是扩展属性。

Error.DesignParserException.INVALID_STRUCTURE_NAME=该名称不是结构名称。

Error.DesignParserException.INVALID_PROPERTY_SYNTAX=该属性必须定义为元素属性。

Error.DesignParserException.UNDEFINED_PROPERTY=属性"{0}"未被定义。

Error.DesignParserException.UNSUPPORTED_ENCODING=不支持此 XML 文件的编码格式。

Error.DesignParserException.PROPERTY_IS_NOT_ENCRYPTABLE=属性 "{0}" 不可加密。

Error.DesignParserException.UNSUPPORTED_VERSION=版本为“{0}”的报表文件无效，因为它大于最新的受支持报表文件。

Error.DesignParserException.INVALID_VERSION=报表版本 "{0}" 无效。

Error.DesignParserException.INVALID_ELEMENT_ID={0} 中的 ID {1} 无效。

Error.DesignParserException.VIRTUAL_PARENT_NOT_FOUND=BaseID "{0}" 无法引用虚拟父元素或父元素不包括的引用的父元素。

Error.DesignParserException.DUPLICATE_ELEMENT_ID={0} 的 ID 与 {1} 的 ID 重复。

Error.DesignParserException.INCONSISTENT_TEMPLATE_ELEMENT_TYPE=模板元素 {0} 与参考模板参数定义 {1} 的默认元素不一致。

Error.DesignParserException.MISSING_TEMPLATE_PARAMETER_DEFAULT=元素 {0} 必须显式定义默认元素。

Error.DesignParserException.WRONG_SIMPLE_LIST_TYPE=没有这样的一个元素属性是简单列表类型。

#DesignFileException
Error.DesignFileException.SYNTAX_ERROR=找到语法错误，并显示错误详细资料。

Error.DesignFileException.SEMANTIC_WARNING=找到语义警告，并显示警告详细资料。

Error.DesignFileException.SEMANTIC_ERROR=找到语义错误，并显示错误详细资料。

Error.DesignFileException.INVALID_XML=设计文件为无效的 xml 文件。

#XMLParserException
Error.XMLParserException.UNKNOWN_TAG=XML 文件包含不支持的元素。

Error.XMLParserException.SAX_ERROR=SAX 检测到错误并显示文件的基本 XML 语法。

Error.XMLParserException.INVALID_BOOLEAN=布尔属性未包含有效值。

Error.XMLParserException.WARNINGS_FOUND=分析完成，但出现不可恢复错误。

Error.XMLParserException.EXCEPTION=出现一般异常。

Error.XMLParserException.INVALID_INTEGER=整型属性包含了一个无效值。
    
#UserPropertyException
Error.UserPropertyException.NAME_REQUIRED=用户属性必须具有名称。

Error.UserPropertyException.DUPLICATE_NAME=名称为 "{0}" 的用户属性已存在。

Error.UserPropertyException.INVALID_TYPE=用户属性类型无效。

Error.UserPropertyException.MISSING_CHOICES=用户属性类型为选项，但未定义任何选项。

Error.UserPropertyException.INVALID_DISPLAY_ID=显示名称 ID 或显示名称均不是根据用户属性 "{0}" 定义的。

Error.UserPropertyException.NOT_FOUND=无法找到用户属性 "{0}"。

Error.UserPropertyException.USER_PROP_DISALLOWED={0} 不得具有用户属性。

Error.UserPropertyException.CHOICE_VALUE_REQUIRED=用户属性 "{0}" 要求提供用户定义选择值。

Error.UserPropertyException.CHOICE_NAME_REQUIRED=用户属性 "{0}" 要求提供用户定义选择名称。

Error.UserPropertyException.INVALID_CHOICE_VALUE=该选择值对于用户属性 "{0}" 的类型无效。

Error.UserPropertyException.INVALID_DEFINITION=用户属性定义无效。

Error.UserPropertyException.INVALID_DEFAULT_VALUE={0} 中用户属性 "{1}" 的默认值 "{2}" 对类型 "{3}" 无效。

#MetaDataReaderException
Error.MetaDataReaderException.META_DATA_ERROR=元数据无效。

#LibraryException
Error.LibraryException.LIBRARY_NOT_FOUND=未找到具有名称空间 "{0}" 的库。

Error.LibraryException.DUPLICATE_LIBRARY_NAMESPACE=库名称空间 "{0}" 已存在。

Error.LibraryException.LIBRARY_INCLUDED_RECURSIVELY=递归包括具有名称空间 "{0}" 的库。

Error.LibraryException.LIBRARY_HAS_DESCENDENTS=由于库“{0}”在当前模块中具有附属项“{1}”，不能删除。

Error.LibraryException.LIBRARY_ALREADY_INCLUDED=已包括库 "{0}"，一个库不能添加两次。

Error.LibraryException.INVALID_LIBRARY_NAMESPACE=库的名称空间“{0}”无效，因为它为空或包含非法字符。

#StyleSheetException
Error.StyleSheetException.STYLE_SHEET_NOT_FOUND=未找到样式表资源。

Error.StyleSheetException.SYNTAX_ERROR=存在某些与 CSS2 不一致的语法错误。

#IncludedCssStyleSheetException
Error.CSSException.CSS_NOT_FOUND=未找到名称为“{0}”的样式表。

Error.CSSException.DUPLICATE_CSS=名称为“{0}”的 CSS 样式表已存在。

Error.CSSException.BADCSSFILE=“{0}”文件中存在某些语法错误。

Error.CssException.READONLY=“{0}”文件中的样式为只读。

#StyleSheetParserException
Error.StyleSheetParserException.RULE_NOT_SUPPORTED=不支持规则 "{0}"。

Error.StyleSheetParserException.STYLE_NOT_SUPPORTED=不支持样式 "{0}"。

Error.StyleSheetParserException.PROPERTY_NOT_SUPPORTED=不支持 CSS2 属性 "{0}" 的值 "{1}"。

Error.StyleSheetParserException.INVALID_SHORT_HAND_CSSPROPERTY_VALUE=速记属性 "{0}" 的值 "{1}" 对 CSS2 语法无效。

Error.StyleSheetParserException.INVALID_SIMPLE_CSSPROPERTY_VALUE=属性 "{0}" 的值 "{1}" 无效。
	
#TemplateException
Error.TemplateException.INVALID_TEMPLATE_ELEMENT_TYPE={0} 不是报表项或数据集元素，因此不能转换为模板元素。

Error.TemplateException.TRANSFORM_TO_REPORT_ITEM_FORBIDDEN={0} 没有显式模板参数定义，因此不能转换为报表项。

Error.TemplateException.TRANSFORM_TO_DATA_SET_FORBIDDEN={0} 没有显式模板参数定义，因此不能转换为数据集。

Error.TemplateException.REVERT_TO_TEMPLATE_FORBIDDEN={0} 没有显式模板参数定义，因此不能回复为模板元素。

Error.TemplateException.CREATE_TEMPLATE_ELEMENT_FORBIDDEN={0} 不在报表中，因此不能执行任何转换。

Error.TemplateException.TEMPLATE_ELEMENT_NOT_SUPPORTED=模板元素在库中不受支持。

#VersionInfo
Info.VersionInfo.INVALID_DESIGN_FILE=正在打开的设计文件不是有效的设计文件，或者该文件不存在。

Info.VersionInfo.CONVERT_INFO=设计文件是使用较早版本的 BIRT 创建的。单击“确定”将它转换为该产品当前版本支持的格式。

Info.VersionInfo.INVALID_VERSION=不支持版本“{0}”的报表文件。

Info.VersionInfo.LATER_VERSION=设计文件由较高版本的 BIRT 创建。 单击“确定”将其转换为该产品当前版本支持的格式。

Info.VersionInfo.EXTENSION_COMPATIBILITY =设计/库可能已被更改，因为对正在打开的设计/库或其包含的库应用了后向兼容性。

#EncryptionException
Error.EncryptionException.INVALID_ENCRYPTABLE_PROPERTY={0} 中的属性 {1} 不可加密，因此不能更改加密。

Error.EncryptionException.INVALID_ENCRYPTION=加密 ID“{0}”无效，因为不存在此类加密。

#Dashboard Exporter
Message.ExportGadgetToSharedGadgetFile=将小工具导出到共享小工具文件。

Message.ExportTabToSharedTabFile=将选项卡导出到共享选项卡文件。

#Dashboard Exception
Error.ExternalGadgetFileIncludedRecursively=外部小工具递归包括。

Error.NotFoundExporter=无法找到仪表板导出器。
