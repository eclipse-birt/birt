###########################################################
# Crosstab name conventions
Element.Crosstab=交叉表

Element.CrosstabCell=交叉表单元格

Element.CrosstabAggregationCell=交叉表聚合单元格

Element.AggregationOnRow=根据行聚合

Element.AggregationOnColumn=根据列聚合

Element.AggregationHeaderLocation=聚合标题位置

Element.GrandTotalLocation=总计位置

Element.MeasureView=度量视图

Element.ComputedMeasureView=计算的度量视图

Element.PageBreakBefore=之前分页

Element.PageBreakAfter=之后分页

Element.PageBreakInside=内部分页符

Element.PageBreakInterval=分页符间隔

Element.SortDirection=排序方向

Element.SortType=排序类型

Element.LevelView=级别视图

Element.DisplayField=显示字段

Element.DimensionView=维视图

Element.CrosstabView=交叉表视图

Element.Cube=Cube
Element.Dimension=维

Element.Level=级别

Element.Measure=度量

Element.Caption=标题

Element.CaptionID=标题关键字

Element.Summary=摘要

Element.MeasureDirection=度量方向

Element.PageLayout=页面布局

Element.EmptyCellValue=空单元格值

Element.RepeatRowHeader=重复行标题

Element.RepeatColumnHeader=重复列标题

Element.Mirrored=镜像

Element.MirrorStartingLevel=镜像起始级别

Element.HideMeasureHeader=隐藏度量头

Element.SpanOverOnColumn=跨列

Element.SpanOverOnRow=跨行

Element.Direction.Horizontal=水平

Element.Direction.Vertical=垂直

Element.choice.before=之前

Element.choice.after=之后

Element.RowPageBreakInterval=行分页符间隔

Element.ColumnPageBreakInterval=列分页符间隔

Element.TagType=标记类型

Method.onPrepare=准备时

Method.onCreate=创建时

Method.onRender=呈现时

Method.onPageBreak=出现分页符时

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=多维数据集维元素“{0}”无法应用到名称重复的交叉表。

Error.CrosstabException.DUPLICATE_MEASURE=多维数据集度量元素“{0}”无法应用到名称重复的交叉表。

Error.CrosstabException.DUPLICATE_LEVEL=多维数据集级别元素“{0}”无法应用到名称重复的交叉表。

Error.CrosstabException.DIMENSION_NOT_FOUND=未在交叉表中找到维视图“{0}”。

Error.CrosstabException.MEASURE_NOT_FOUND=未在交叉表中找到度量视图“{0}”。

Error.CrosstabException.LEVEL_NOT_FOUND=未在交叉表中找到级别视图“{0}”。

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

BaseCrosstabExecutor.error.process.bookmark=处理页眉时出现错误。

BaseCrosstabExecutor.error.process.headers=处理页眉时出现错误。

BaseCrosstabExecutor.error.process.row.height=处理行高时出错。

CrosstabCellExecutor.error.restor.data.position=还原数据位置时出错。

CrosstabGrandTotalRowExecutor.error.generate.child.executor=生成子执行程序时出错。

CrosstabGrandTotalRowExecutor.error.check.child.executor=检查子执行程序的状态时出错。

CrosstabHeaderExecutor.error.reset.row.position=重置行边缘光标位置时出错。

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

CrosstabReportItemHandle.exception.name.blank=ComputedMeasure 需要名称。

CrosstabReportItemHandle.exception.name.duplicate=指定的度量名称已被使用，不能重复。

MeasureViewHandle.info.aggregation.already.exist=请求的聚合已存在。

MeasureViewHandle.msg.add.aggregation=添加聚合

LevelViewTask.msg.remove.subtotal=删除小计

LevelViewTask.msg.add.subtotal=添加小计

DimensionViewTask.msg.remove.level=删除级别

DimensionViewTask.msg.insert.level=插入级别

MeasureViewTask.msg.remove.header=删除度量标题

MeasureViewTask.msg.add.header=添加度量标题

CrosstabViewTask.msg.add.dimension=添加维

CrosstabViewTask.msg.remove.dimension=删除维

CrosstabViewTask.msg.remove.grandtotal=删除总计

CrosstabViewTask.msg.add.grandtotal=添加总计

CrosstabReportItemTask.msg.add.grandtotal=添加总计

CrosstabReportItemTask.msg.set.aggregate.function=设置聚合函数

CrosstabReportItemTask.msg.pivot.dimension=透视维

CrosstabReportItemTask.msg.insert.dimension=插入维

CrosstabReportItemTask.msg.set.mirroredStartingLevel=设置镜像起始级别

CrosstabReportItemTask.msg.pivot.crosstab=透视交叉表

CrosstabReportItemHandle.Error.HasNoCube=交叉表必须能够访问一个多维。

CrosstabReportItemHandle.log.validate=正在验证(...)

CrosstabScriptHandler.error.javascript=JavaScript 第 {1} 行的 {0}:\r\n{2}\r\n

CrosstabScriptHandler.info.try.register.crosstab.javascript.content=尝试注册交叉表 javascript 内容...

CrosstabScriptHandler.info.crosstab.javascript.content.registered=交叉表 javascript 内容已成功注册。

CrosstabScriptHandler.info.try.load.crosstab.java.handler=尝试加载交叉表 java 事件处理程序...

CrosstabScriptHandler.info.crosstab.java.handler.loaded=交叉表 java 事件处理程序 [{0}] 已成功加载。

CrosstabScriptHandler.info.invalid.crosstab.java.handler=交叉表 java 事件处理程序 [{0}] 不是兼容实现。
