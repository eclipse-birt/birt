###########################################################
# Crosstab name conventions
Element.Crosstab=交叉表
Element.CrosstabCell=交叉表单元格
Element.CrosstabAggregationCell=交叉表聚合单元格
Element.AggregationOnRow=遇到行时聚合
Element.AggregationOnColumn=遇到列时聚合
Element.AggregationHeaderLocation=Aggregation Header Location
Element.MeasureView=度量视图
Element.PageBreakBefore=Page Break Before
Element.PageBreakAfter=Page Break After
Element.SortDirection=Sort Direction
Element.SortType=Sort Type
Element.LevelView=级别视图
Element.DimensionView=维视图
Element.CrosstabView=交叉表视图
Element.Cube=多维数据集
Element.Dimension=Dimension
Element.Level=Level
Element.Measure=Measure
Element.Caption=标题
Element.CaptionID=标题关键字
Element.MeasureDirection=度量方向
Element.PageLayout=页面布局
Element.EmptyCellValue=空单元格值

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=多维数据集维元素 {0} 具有应用于交叉表 {1} 中的维视图的重复项。
Error.CrosstabException.DUPLICATE_MEASURE=多维数据集度量元素 {0} 具有应用于交叉表 {1} 中的度量视图的重复项。
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=从扩展项加载交叉表项失败。
CrosstabReportItemQuery.error.query.building=用于查询生成的交叉表模型无效。

CrosstabQueryHelper.error.invalid.measure=The associated measure for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.dimension=The associated dimension for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.level=The associated level for crosstab is inavlid.

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
CrosstabReportItemExecutor.error.generate.columns=Error happend when generate columns for crosstab.
CrosstabReportItemExecutor.error.prepare.children=Error happend when prepare child executor for crosstab.
CrosstabReportItemExecutor.error.invalid.cube.result=The cube result for crosstab is invalid(=null).

CrosstabRowExecutor.error.check.force.empty=检查当前单元格的强制清空状态时出错。
CrosstabRowExecutor.error.check.edge.start=检查当前单元格的边缘开始状态时出错。
CrosstabRowExecutor.error.retrieve.child.executor=检索子执行程序时出错。

CrosstabSubTotalRowExecutor.error.check.edge.start=检查当前单元格的边缘开始状态时出错。
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=检索子执行程序时出错。

TableColumnGenerator.error.process.visibility=处理列可见性时出错。

CrosstabReportItemHandle.error.locate.column.cell=Error locating relevant crosstab cell on columns
CrosstabReportItemHandle.error.locate.row.cell=Error locating relevant crosstab cell on rows
CrosstabReportItemHandle.error.set.column.width=Error setting crosstab column width
CrosstabReportItemHandle.error.set.row.height=Error setting crosstab row height
