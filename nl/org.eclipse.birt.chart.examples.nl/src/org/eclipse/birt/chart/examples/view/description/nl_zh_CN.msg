###########################################################
#Primitive Charts
SideBySideBar=并列条形图显示相互紧靠的系列中的条形。这些条形经过排列后，均具有相同的宽度。这些条形的宽度取决于绘制的系列数。

StackedBar=堆积条形图显示垂直堆叠的条形。正值和负值分别堆叠在原点的上方和下方。

PercentStackedBar=堆积百分比条形图显示垂直堆叠的条形，堆叠的条形的总高度(从其最低点到最高点)为 100。

OverlayLine=叠加折线图显示每个系列独立于其它系列的折线。这些折线在显示时连接系列的值。

StackedLine=堆积折线图显示垂直堆叠的折线。正值和负值分别堆叠在原点的上方和下方。

PercentStackedLine=堆积百分比折线图显示垂直堆叠的折线，堆叠的折线的总高度(每个单元中从最低点到最高点)为 100。

OverlayArea=覆盖型面积图显示每个系列独立于其它系列的区域。这些区域在显示时连接系列的值。

StackedArea=堆积面积图显示垂直堆叠的区域。正值和负值分别堆叠在原点的上方和下方。

PercentStackedArea=堆积百分比面积图显示垂直堆叠的区域，堆叠的区域的总高度(每个单元中从最低点到最高点)为 100。

Pie=饼图将值显示为多个扇面。每个扇面的大小与其所表示的值成比例对应。多个系列的饼图将被绘制成多个饼图，每个饼图代表一个系列。

StandardMeter=标准仪表图包含具有区域的单个刻度盘。刻度盘的背景可分成具有不同颜色的区域。

SuperImposedMeter=超级拼版仪表图包含具有相同区域的多个刻度盘。这些刻度盘重叠在一起，以便可以表示单个区域内的多个指针。

Scatter=散点图使用类别和值数据作为坐标，以在图形上显示经过排列的值。每个数据值由一个标记表示。

BarStickStock=条形股票图包含一条垂直线和两条水平线。 垂直线的上下两个端点表示股票最高价和最低价。 两条水平线表示表示开盘价和收盘价。

CandleStickStock=烛形股票图包含一个方框以及从端点向上下伸展的线段。方框的上边和下边表示开盘价和收盘价。线段的上端点和下端点表示最高价和最低价。

Bubble=气泡图是散点图的扩展，将点显示为气泡。气泡直径与 X 和 Y 坐标外的第三个数据值成比例对应。

Difference=差异图显示两条数值线之间的区域。它们继承所有区域系列属性，也可以使用不同颜色显示负值区域。

Gantt=甘特图用于跟踪任务的持续时间和进度。它们通过日期时间 Y 轴表示，使用自定义宽度的条形，并包含可选的开始/结束标记。每个条形按其类别、开始时间和结束时间进行定义。可以覆盖若干个宽度不同的甘特图系列来跟踪已计划任务的实际进度。

Tube=管形图与条形图类似，其条形用管形表示。

Cone=锥形图与条形图类似，其条形用锥形表示。

Pyramid=棱锥图与条形图类似，其条形用棱锥表示。

Radar=标准雷达图包含由一个或多个系列多边形/线段覆盖的圆形网格。

###########################################################
#3D Charts
Bar=3 维并列条形图。

Line=3 维叠加折线图。

Area=3 维覆盖型面积图。

Tube3D=3 维并列管形图。

Cone3D=3 维并列锥形图。

Pyramid3D=3 维并列棱锥图。

###########################################################
#Combination Charts
BarAndLine=条形和折线组合图包含 2 个系列。它们共享相同类别值。


###########################################################
#Formatted Charts
AxisLabelFormat=指定轴标签的格式: Y 轴($###,###) 和 X 轴(MM/dd/yyyy)。另外，用户可以设置标签颜色、字体、边框和其它属性。

AxisTitleFormat=所有标签属性均可应用于轴标题。

ColoredByCategorySeries=图例项类型设置为类别。

OpenJavaSourceAction.Text.Description=从相关目录中打开 Java 源

OpenJavaSourceAction.Text.ToolTip=打开 Java 源

SaveXMLAction.MessageBox.Message=XML 源已存在于目录中。\n是否要替换?

SaveXMLAction.MessageBox.Text=保存 XML 源

SaveXMLAction.Text.Description=将 XML 源保存到指定的目录

SaveXMLAction.Text.ToolTip=保存 XML 源

ColoredByValueSeries=图例项类型设置为系列。

CurveFittingArea=启用区域系列的曲线拟合属性。

CurveFittingBar=为条形系列创建曲线拟合线。

CurveFittingLine=启用线条系列的曲线拟合属性。

CurveFittingStock=为股票系列创建曲线拟合线。

LegendFormat=图例可放在左侧、右侧、顶部、底部和内部。客户区域位于图例内。它包含图例项。

MarkerLine=可为 X 轴和 Y 轴创建标记线(仅限于 ChartWithAxes 类型)。

MarkerRange=可为 X 轴和 Y 轴创建标记范围(仅限于 ChartWithAxes 类型)。

MultiplePie=单个饼图包含多个饼图系列。它们可以构成一个饼图组以显示值比较。

MDialSRegionMeter=单个刻度盘上多个指针。

SDialSRegionMeter=单个刻度盘上一个指针。

PercentageValueFormat=系列值显示为百分比数字。

PlotFormat=指定绘图区域和客户区域的外观。

SeriesValueFormat=指定值数据($###,###.00)和 X 轴标签(MM/dd/yyyy)的格式。

###########################################################
#Scripted Charts
AxisLabelScript=beforeDrawAxisLabel() 允许用户操作轴和标签(Axis.getLabel())属性。

AxisTitleScript=beforeDrawAxisTitle() 允许用户操作轴和标签(Axis.getTitle())属性

BlockScript=beforeDrawBlock()允许用户操作块(例如图形、标题、图例)属性。

CurveFittingScript=beforeDrawFittingCurve()允许用户操作曲线拟合属性。

DataPointLabelScript=beforeDrawDataPointLabel()允许用户操作数据点提示和标签属性。

LegendScript=beforeDrawLegendItem() 允许用户操作图例项和边界属性。

MarkerLineScript=beforeDrawMarkerLine()允许用户操作轴和标记线属性。

MarkerRangeScript=beforeDrawMarkerRange()允许用户操作轴和标记范围属性。

SeriesScript=beforeDrawSeries()允许用户操作系列和系列呈现器(BaseRenderer 或 AxesRenderer)属性。

SeriesTitleScript=beforeDrawSeriesTitle()允许用户操作系列和标签(Series.getTitle())属性。仅适用于饼图。

###########################################################
#Data Operations
MinSlice=用户能够为扇面指定最小值(绝对值或百分比)。饼图扇面可通过设置分离属性表达式进行分离。

MultipleYAxes=一个图可以包含多个 Y 轴。

MultipleYSeries=一个图可以包含多个 Y(值)系列。

WorkingWithBIRTDataEngine=图表与 ODA/DtE (BIRT 数据引擎)配合获得分组/聚合的数据，而不是由图表本身分组/聚合。

###########################################################
#Default
DefaultDescription=请从类别中选择一个示例

###########################################################
#Example View labels
ChartExamples.Description=描述

ChartExamples.Preview=预览

ChartExamples.SelectExamples=选择示例

############################################################
#Category Names
SampleChartCategory.PrimitiveCharts=原始图表

SampleChartCategory.3DCharts=3D 图表

SampleChartCategory.CombinationCharts=组合表

SampleChartCategory.FormattedCharts=格式化图表

SampleChartCategory.ScriptedCharts=脚本化图表

SampleChartCategory.DataOperations=数据运算
#Sample Chart Names
#Primitive Charts
SampleChartName.SideBySideBar=并列条形图

SampleChartName.StackedBar=堆积条形图

SampleChartName.PercentStackedBar=堆积百分比条形图

SampleChartName.OverlayLine=叠加折线图

SampleChartName.StackedLine=堆积折线图

SampleChartName.PercentStackedLine=堆积百分比折线图

SampleChartName.OverlayArea=覆盖型面积图

SampleChartName.StackedArea=堆积面积图

SampleChartName.PercentStackedArea=堆积百分比面积图

SampleChartName.Pie=饼图

SampleChartName.StandardMeter=标准仪表图

SampleChartName.SuperImposedMeter=超级拼版仪表图

SampleChartName.Scatter=散点图

SampleChartName.BarStickStock=条形股票图

SampleChartName.CandleStickStock=烛形股票图

SampleChartName.Bubble=气泡图

SampleChartName.Difference=差异图

SampleChartName.Gantt=甘特图

SampleChartName.Tube=管形图

SampleChartName.Cone=锥形图

SampleChartName.Pyramid=棱锥图

SampleChartName.Radar=雷达图
#3D Charts
SampleChartName.Bar=条形图

SampleChartName.Line=折线图

SampleChartName.Area=面积图

SampleChartName.Tube3D=管形图

SampleChartName.Cone3D=锥形图

SampleChartName.Pyramid3D=棱锥图
#Combination Charts
SampleChartName.BarAndLine=条形和折线
#Formatted Charts
SampleChartName.AxisLabelFormat=轴标签格式

SampleChartName.AxisTitleFormat=轴标题格式

SampleChartName.ColoredByCategorySeries=按类别系列着色

SampleChartName.ColoredByValueSeries=按值系列着色

SampleChartName.CurveFittingArea=曲线拟合面积图

SampleChartName.CurveFittingBar=曲线拟合条形图

SampleChartName.CurveFittingLine=曲线拟合折线图

SampleChartName.CurveFittingStock=曲线拟合股票图

SampleChartName.LegendFormat=图例格式

SampleChartName.MarkerLine=标记线条

SampleChartName.MarkerRange=标记区域

SampleChartName.MultiplePie=多个饼图

SampleChartName.MDialSRegionMeter=M 刻度盘 S 区域仪表图

SampleChartName.SDialSRegionMeter=S 刻度盘 S 区域仪表图

SampleChartName.PercentageValueFormat=百分比值格式

SampleChartName.PlotFormat=图形格式

SampleChartName.SeriesValueFormat=系列值格式
#Scripted Charts
SampleChartName.AxisLabelScript=轴标签脚本

SampleChartName.AxisTitleScript=轴标题脚本

SampleChartName.BlockScript=块脚本

SampleChartName.CurveFittingScript=曲线拟合脚本

SampleChartName.DataPointLabelScript=数据点标签脚本

SampleChartName.LegendScript=图例脚本

SampleChartName.MarkerLineScript=标记线条脚本

SampleChartName.MarkerRangeScript=标记区域脚本

SampleChartName.SeriesScript=系列脚本

SampleChartName.SeriesTitleScript=系列标题脚本
#Data Operations
SampleChartName.MinSlice=最小扇面

SampleChartName.MultipleYAxes=多个 Y 轴

SampleChartName.MultipleYSeries=多个 Y 系列

SampleChartName.WorkingWithBIRTDataEngine=使用 BIRT 数据引擎


