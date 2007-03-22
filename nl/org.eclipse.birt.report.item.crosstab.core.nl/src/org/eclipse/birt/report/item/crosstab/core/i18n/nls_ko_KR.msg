###########################################################
# Crosstab name conventions
Element.Crosstab=교차 테이블
Element.CrosstabCell=Crosstab Cell
Element.CrosstabAggregationCell=Crosstab Aggregation Cell
Element.AggregationOnRow=Aggregation On Row
Element.AggregationOnColumn=Aggregation On Column
Element.MeasureView=Measure View
Element.LevelView=Level View
Element.DimensionView=Dimension View
Element.CrosstabView=Crosstab View
Element.Cube=Cube
Element.Caption=Caption
Element.CaptionID=Caption Key
Element.MeasureDirection=Measure Direction
Element.PageLayout=Page Layout
Element.EmptyCellValue=Empty Cell Value

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=큐브 차원 요소 {0}이(가) 크로스 탭 {1}의 차원 보기에 중복 적용되었습니다. 
Error.CrosstabException.DUPLICATE_MEASURE=큐브 측정 요소 {0}이(가) 크로스 탭 {1}의 측정 보기에 중복 적용되었습니다.
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=Loading crosstab item from extended item failed.
CrosstabReportItemQuery.error.query.building=Invalid crosstab model for query building.

BaseCrosstabExecutor.error.process.visibility=Error happend when processing visibility.
BaseCrosstabExecutor.error.process.bookmark=Error happend when processing bookmark.

CrosstabCellExecutor.error.restor.data.position=Eroor happend when restore data position.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=Error happend when generate child executor.
CrosstabGrandTotalRowExecutor.error.check.child.executor=Error happend when check status of child executor.

CrosstabGroupExecutor.error.close.executor=Error happend when close group executor.
CrosstabGroupExecutor.error.prepare.group=Error happend when prepare child groups.
CrosstabGroupExecutor.error.check.child.executor=Error happend when check status of child executor.

CrosstabHeaderRowExecutor.error.check.force.empty=Error happend when check force empty status of current cell.
CrosstabHeaderRowExecutor.error.check.edge.end=Error happend when check edge end status of current cell.
CrosstabHeaderRowExecutor.error.retrieve.child.executor=Error happend when retrieve child executor.

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=Error happend when generate child executor.
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=Error happend when check status of child executor.

CrosstabMeasureRowExecutor.error.generate.child.executor=Error happend when generate child executor.
CrosstabMeasureRowExecutor.error.check.child.executor=Error happend when check status of child executor.

CrosstabReportItemExecutor.error.crosstab.loading=Loading crosstab item from extended item failed.
CrosstabReportItemExecutor.error.generate.columns=
CrosstabReportItemExecutor.error.prepare.children=

CrosstabRowExecutor.error.check.force.empty=Error happend when check force empty status of current cell.
CrosstabRowExecutor.error.check.edge.start=Error happend when check edge start status of current cell.
CrosstabRowExecutor.error.retrieve.child.executor=Error happend when retrieve child executor.

CrosstabSubTotalRowExecutor.error.check.edge.start=Error happend when check edge start status of current cell.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Error happend when retrieve child executor.

TableColumnGenerator.error.process.visibility=Error happend when process column visibility.

