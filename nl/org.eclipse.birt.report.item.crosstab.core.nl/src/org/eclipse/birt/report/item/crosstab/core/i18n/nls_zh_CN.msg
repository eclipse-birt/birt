###########################################################
# Crosstab name conventions
Element.Crosstab=交叉表
Element.CrosstabCell=交叉表单元格
Element.CrosstabAggregationCell=交叉表聚合单元格
Element.AggregationOnRow=Aggregation on row
Element.AggregationOnColumn=Aggregation on column
Element.AggregationHeaderLocation=Aggregation header location
Element.MeasureView=度量视图
Element.ComputedMeasureView=计算的度量视图
Element.PageBreakBefore=Page break before
Element.PageBreakAfter=Page break after
Element.SortDirection=Sort direction
Element.SortType=Sort type
Element.LevelView=级别视图
Element.DisplayField=Display field
Element.DimensionView=维视图
Element.CrosstabView=交叉表视图
Element.Cube=多维数据集
Element.Dimension=维
Element.Level=级别
Element.Measure=度量
Element.Caption=标题
Element.CaptionID=Caption key
Element.Summary=Summary
Element.MeasureDirection=Measure direction
Element.PageLayout=页面布局
Element.EmptyCellValue=Empty cell value
Element.RepeatRowHeader=Repeat row header
Element.RepeatColumnHeader=Repeat column header
Element.Mirrored=镜像
Element.MirrorStartingLevel=Mirror starting level
Element.HideMeasureHeader=Hide measure header
Element.SpanOverOnColumn=Span over on column
Element.SpanOverOnRow=Span over on row

Method.onPrepare=On prepare
Method.onCreate=On create
Method.onRender=On render
Method.onPageBreak=On page break

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=多维数据集维元素 {0} 具有应用于交叉表 {1} 中的维视图的重复项。
Error.CrosstabException.DUPLICATE_MEASURE=多维数据集度量元素 {0} 具有应用于交叉表 {1} 中的度量视图的重复项。
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=从扩展项加载交叉表项失败。
CrosstabReportItemQuery.error.query.building=用于查询生成的交叉表模型无效。
CrosstabReportItemQuery.error.register.empty.cell.value=注册空单元格值时出错。

CrosstabQueryHelper.error.invalid.measure=交叉表包含空度量定义，或者特定度量“{0}”在关联的多维数据集内不存在。
CrosstabQueryHelper.error.invalid.dimension.row=交叉表行区域包含空维定义，或者特定维“{0}”在关联的多维数据集内不存在。
CrosstabQueryHelper.error.invalid.level.row=交叉表行区域包含空级别定义，或者特定级别“{0}”在关联的多维数据集内不存在。
CrosstabQueryHelper.error.invalid.dimension.column=交叉表列区域包含空维定义，或者特定维“{0}”在关联的多维数据集内不存在。
CrosstabQueryHelper.error.invalid.level.column=交叉表列区域包含空级别定义，或者特定级别“{0}”在关联的多维数据集内不存在。

BaseCrosstabExecutor.error.process.visibility=处理可见性时出错。
BaseCrosstabExecutor.error.process.bookmark=处理书签时出错。
BaseCrosstabExecutor.error.process.row.height=处理行高时出错。

CrosstabCellExecutor.error.restor.data.position=还原数据位置时出错。

CrosstabGrandTotalRowExecutor.error.generate.child.executor=生成子执行程序时出错。
CrosstabGrandTotalRowExecutor.error.check.child.executor=检查子执行程序的状态时出错。

CrosstabGroupExecutor.error.close.executor=关闭组组执行程序时出错。
CrosstabGroupExecutor.error.prepare.group=准备子组时出错。
CrosstabGroupExecutor.error.check.child.executor=检查子执行程序的状态时出错。

CrosstabHeaderRowExecutor.error.check.force.empty=检查当前单元格的强制清空状态时出错。
CrosstabHeaderRowExecutor.error.check.edge.end=检查当前单元格的边缘结束状态时出错。
CrosstabHeaderRowExecutor.error.retrieve.child.executor=检索子执行程序时出错。

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=生成子执行程序时出错。
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=检查子执行程序的状态时出错。

CrosstabMeasureRowExecutor.error.generate.child.executor=生成子执行程序时出错。
CrosstabMeasureRowExecutor.error.check.child.executor=检查子执行程序的状态时出错。

CrosstabReportItemExecutor.error.crosstab.loading=从扩展项加载交叉表项失败。
CrosstabReportItemExecutor.error.generate.columns=为交叉表生成列时出错。
CrosstabReportItemExecutor.error.prepare.children=为交叉表准备子执行程序时出错。
CrosstabReportItemExecutor.error.invalid.cube.result=交叉表的多维数据集结果无效 (=null)。

CrosstabRowExecutor.error.check.force.empty=检查当前单元格的强制清空状态时出错。
CrosstabRowExecutor.error.check.edge.start=检查当前单元格的边缘开始状态时出错。
CrosstabRowExecutor.error.retrieve.child.executor=检索子执行程序时出错。

CrosstabSubTotalRowExecutor.error.check.edge.start=检查当前单元格的边缘开始状态时出错。
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=检索子执行程序时出错。

TableColumnGenerator.error.process.visibility=处理列可见性时出错。
TableColumnGenerator.error.process.column.width=处理列宽时出错。

CrosstabReportItemHandle.error.locate.column.cell=在列中查找相关交叉表单元格时出错
CrosstabReportItemHandle.error.locate.row.cell=在行中查找相关交叉表单元格时出错
CrosstabReportItemHandle.error.set.column.width=设置交叉表列宽时出错
CrosstabReportItemHandle.error.set.row.height=设置交叉表行高时出错
CrosstabReportItemHandle.msg.change.measure.direction=更改度量方向
CrosstabReportItemHandle.msg.insert.measure=插入度量

MeasureViewHandle.info.aggregation.already.exist=请求的聚合已存在。
MeasureViewHandle.msg.add.aggregation=添加聚合

LevelViewTask.msg.remove.subtotal=删除小计
LevelViewTask.msg.add.subtotal=添加小计

DimensionViewTask.msg.remove.level=删除级别
DimensionViewTask.msg.insert.level=插入级别

CrosstabViewTask.msg.remove.dimension=删除维
CrosstabViewTask.msg.remove.grandtotal=删除总计
CrosstabViewTask.msg.add.grandtotal=添加总计

CrosstabReportItemTask.msg.add.grandtotal=添加总计
CrosstabReportItemTask.msg.set.aggregate.function=设置聚合函数
CrosstabReportItemTask.msg.pivot.dimension=透视维
CrosstabReportItemTask.msg.insert.dimension=插入维
CrosstabReportItemTask.msg.set.mirroredStartingLevel=Set Mirrored Starting Level

CrosstabReportItemHandle.Error.HasNoCube=交叉表必须能够访问一个多维
CrosstabReportItemHandle.log.validate=正在验证(...)

CrosstabScriptHandler.error.javascript=JavaScript 第 {1} 行的 {0}:\r\n{2}\r\n
CrosstabScriptHandler.info.try.register.crosstab.javascript.content=尝试注册交叉表 javascript 内容...
CrosstabScriptHandler.info.crosstab.javascript.content.registered=交叉表 javascript 内容已成功注册。
CrosstabScriptHandler.info.try.load.crosstab.java.handler=尝试加载交叉表 java 事件处理程序...
CrosstabScriptHandler.info.crosstab.java.handler.loaded=交叉表 java 事件处理程序 [{0}] 已成功加载。
CrosstabScriptHandler.info.invalid.crosstab.java.handler=交叉表 java 事件处理程序 [{0}] 不是兼容实现。
