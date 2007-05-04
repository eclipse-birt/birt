###########################################################
# Crosstab name conventions
Element.Crosstab=交叉表
Element.CrosstabCell=交叉表单元格
Element.CrosstabAggregationCell=交叉表聚合单元格
Element.AggregationOnRow=遇到行时聚合
Element.AggregationOnColumn=遇到列时聚合
Element.AggregationHeaderLocation=聚合标题位置
Element.MeasureView=度量视图
Element.PageBreakBefore=段前分页
Element.PageBreakAfter=段后分页
Element.SortDirection=排序方向
Element.SortType=排序类型
Element.LevelView=级别视图
Element.DimensionView=维视图
Element.CrosstabView=交叉表视图
Element.Cube=多维数据集
Element.Dimension=维
Element.Level=级别
Element.Measure=度量
Element.Caption=标题
Element.CaptionID=标题关键字
Element.MeasureDirection=度量方向
Element.PageLayout=页面布局
Element.EmptyCellValue=空单元格值
Element.RepeatRowHeader=重复行标题
Element.RepeatColumnHeader=重复列标题
Element.Mirrored=镜像
Element.MirroredStartingLevel=Mirrored Starting Level

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

CrosstabQueryHelper.error.invalid.measure=交叉表的关联度量无效。
CrosstabQueryHelper.error.invalid.dimension=交叉表的关联维无效。
CrosstabQueryHelper.error.invalid.level=交叉表的关联级别无效。

BaseCrosstabExecutor.error.process.visibility=处理可见性时出错。
BaseCrosstabExecutor.error.process.bookmark=处理书签时出错。

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

