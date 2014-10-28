exception.base.orthogonal.null.datadefinition=给定的数据定义表达式无效。

exception.dataset.stockseries=受股票系列约束的数据项不完整。它们必须包含四个值：最高、最低、开盘、收盘。

exception.dataset.null.stockentry=数据项具有无效的空值或非数字值。

exception.dataset.invalid.ganttentry=数据项具有无效类型的值。

exception.empty.dataset=找到空数据集

exception.empty.palette=与系列 {0} 相关联的调色板为空

exception.null.values=无法在图形中定义“全空”值

exception.unknown.datatype=图表使用的数据集无效

exception.dataset.booleantype=不允许使用布尔类型

exception.undefined.riser=未在条形图系列 {0} 中定义上升器类型

exception.stock.entry.invalid.highlow=股票数据项无效。最高价 {0} 低于最低价 {1}。

exception.stock.entry.invalid.high=股票数据项无效。最高价 {0} 低于开盘价/收盘价 {1}。

exception.stock.entry.invalid.low=股票数据项无效。最低价 {0} 高于开盘价/收盘价 {1}。

exception.series.visibility=未为系列 {0} 明确设置可见性

exception.xvalue.scale.bars=条形图可能未按值比例进行绘制

exception.xvalue.scale.lines=线条可能未按值比例进行绘制

exception.xvalue.scale.stock=蜡烛可能未按值比例进行绘制

exception.invalid.tick.style.dial=为标度图系列指定的刻度样式无效。

exception.unknown.custom.dataset=无法编译由 {1} 定义的自定义数据集 {0}

exception.curve.visibility.unset=对于曲线，必须在线条属性上设置可见性

exception.visibility.line.unset=对于线条，必须在线条属性上设置可见性

exception.cannot.parse.sample=无法分析将与系列数据集关联的示例数据值

exception.no.stack.percent.3D.chart=三维图表不支持堆积和百分比功能。

exception.dial.dimension=标度图系列不支持 ''{0}''。

exception.pie.dimension=饼图系列不支持 ''{0}''。

exception.scatter.dimension=散点图系列不支持 ''{0}''。

exception.stock.dimension=无法使用维 {0} 绘制股票系列

exception.multi.orthogonal.3D.not.supported=三维图表不支持多个值轴。

exception.scatter.3D.not.supported=三维图表不支持离散类别轴。

exception.no.transposed.3D.chart=三维图表不支持置换。

exception.no.transposed.stock.chart=股票图表不支持置换。

exception.no.transposed.gantt.chart=甘特图仅支持置换。

exception.3D.points.length.less.than.3=对于三维绘制事件，点列表长度至少为 3 (>=3)。

exception.empty.icon.list=标记图像选项板列表为空。

info.sample.formats=例如\n数字: 1,2,3\n日期: 05-1-26,05-1-27,05-1-28\n文本: 'a,b,c'\n及所有用逗号隔开的多个值。

info.render.series={0}: renderSeries() [{1}/{2}]
info.stock.sample.format=例如: H2.30 L4.50 O1.53 C7.01、H5.4 L3.5 O2.9 C6.7

info.datapoint.BubbleValue=气泡值

info.datapoint.BubbleSize=气泡大小

info.datapoint.PositiveValue=正值

info.datapoint.NegativeValue=负值

info.datapoint.GanttStartDate=开始日期

info.datapoint.GanttEndDate=结束日期

info.datapoint.GanttDecorationLabel=任务标签

info.datapoint.High=最高

info.datapoint.Low=最低

info.datapoint.Open=开盘

info.datapoint.Close=收盘

exception.missing.series.for.palette.index=无法在系列定义 {1} 中找到系列 {0}，以满足计算调色板索引的需要

exception.pie.negative.dataset.values=饼图系列不能在其数据集中包含负值

exception.stock.lineattr.visibility=股票元素的线条属性必须设置为可见

exception.legend.graphic.unknown.riser=无法为未知的上升器类型 {0} 绘制图例图形

exception.unspecified.visible.series.title=未为可见的系列标题指定系列标题位置

exception.unspecified.riser.type=指定了未知的上升器类型 {0}

exception.invalid.data.point.position.scatter=当在散点图系列上进行绘制时，不支持数据点标签位置 {0}。允许的值为 ABOVE、BELOW、LEFT 和 RIGHT。

exception.invalid.data.point.position.bubble=在气泡图系列上进行绘制时，不支持数据点标签位置 {0}。允许的值为 ABOVE、BELOW、LEFT 和 RIGHT。

exception.invalid.data.point.outbound.bubble=绘图区域的范围内没有气泡。请检查轴刻度。

exception.illegal.pie.series.title.position=为系列标题位置 = {0} 指定的值无效

exception.illegal.datapoint.position.bar=当在条形图系列上进行绘制时，不支持数据点标签位置 {0}。允许的值为 OUTSIDE 和 INSIDE。

exception.illegal.datapoint.position.line=当在线条系列上进行绘制时，不支持数据点标签位置 {0}。允许的值为 ABOVE、BELOW、LEFT 和 RIGHT。

exception.illegal.datapoint.position.stock=当在股票图系列上进行绘制时，不支持数据点标签位置 {0}。允许的值为 ABOVE、BELOW、LEFT 和 RIGHT。

exception.unspecified.datapoint.visibility.pie=未设置系列 {0} 的数据点可见性

exception.invalid.datapoint.position.pie=数据点标签位置 {0} 无效

exception.invalid.datapoint.dial=不能使用空数据点或非数字数据点来绘制仪表图系列。

exception.unspecified.pie.series.title.visibility=未在模型中设置 {0} 的系列标题可见性

exception.unspecified.marker.linestyle.visibility=尚未设置标记线条样式的可见性

exception.unsupported.aggregate.function.input=聚合函数 {0} 遇到不支持的数据类型

exception.illegal.datapoint.position.bar3d=三维条形图不支持内部系列标签。

Irr.AggregateFunction.Parameters.Label.Intrate=迭代

Mirr.AggregateFunction.Parameters.Label.FinanceRate=财务比率

Mirr.AggregateFunction.Parameters.Label.ReinvestmentRate=再投资率

Npv.AggregateFunction.Parameters.Label.Rate=比率

Percentile.AggregateFunction.Parameters.Label.Percentage=百分比

Quartile.AggregateFunction.Parameters.Label.Quart=四分位

WeightedAverage.AggregateFunction.Parameters.Label.Weight=权重

MovingAverage.AggregateFunction.Parameters.Label.Window=窗口

RunningNpv.AggregateFunction.Parameters.Label.Rate=比率

Rank.AggregateFunction.Parameters.Label.Ascending=升序

Top.AggregateFunction.Parameters.Label.N=前 n 个

TopPercent.AggregateFunction.Parameters.Label.Percentage=百分比

Bottom.AggregateFunction.Parameters.Label.N=后 n 个

BottomPercent.AggregateFunction.Parameters.Label.Percentage=百分比

DataSetProcessorImpl_exception.dataset.receivednumeric=接收此类型的数据类型应为数值。
