###########################################################
# Crosstab name conventions
Element.Crosstab=교차 테이블
Element.CrosstabCell=크로스 탭 셀
Element.CrosstabAggregationCell=크로스 탭 집계 셀
Element.AggregationOnRow=행에서 집계
Element.AggregationOnColumn=열에서 집계
Element.AggregationHeaderLocation=Aggregation Header Location
Element.MeasureView=측정 보기
Element.PageBreakBefore=Page Break Before
Element.PageBreakAfter=Page Break After
Element.SortDirection=Sort Direction
Element.SortType=Sort Type
Element.LevelView=수준 보기
Element.DimensionView=차원 보기
Element.CrosstabView=크로스 탭 보기
Element.Cube=큐브
Element.Dimension=Dimension
Element.Level=Level
Element.Measure=Measure
Element.Caption=캡션
Element.CaptionID=캡션 키
Element.MeasureDirection=측정 방향
Element.PageLayout=페이지 레이아웃
Element.EmptyCellValue=빈 셀 값

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=큐브 차원 요소 {0}이(가) 크로스 탭 {1}의 차원 보기에 중복 적용되었습니다. 
Error.CrosstabException.DUPLICATE_MEASURE=큐브 측정 요소 {0}이(가) 크로스 탭 {1}의 측정 보기에 중복 적용되었습니다.
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=확장 항목을 크로스 탭 항목에 로드하지 못했습니다.
CrosstabReportItemQuery.error.query.building=쿼리 생성에 맞지 않는 크로스 탭 모델입니다.

CrosstabQueryHelper.error.invalid.measure=The associated measure for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.dimension=The associated dimension for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.level=The associated level for crosstab is inavlid.

BaseCrosstabExecutor.error.process.visibility=표시 유형을 처리하는 중에 오류가 발생했습니다.
BaseCrosstabExecutor.error.process.bookmark=책갈피를 처리하는 중에 오류가 발생했습니다.

CrosstabCellExecutor.error.restor.data.position=데이터 위치를 복원하는 중에 오류가 발생했습니다.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=하위 실행기를 생성하는 중에 오류가 발생했습니다.
CrosstabGrandTotalRowExecutor.error.check.child.executor=하위 실행기의 상태를 확인하는 중에 오류가 발생했습니다.

CrosstabGroupExecutor.error.close.executor=그룹 실행기를 종료하는 중에 오류가 발생했습니다.
CrosstabGroupExecutor.error.prepare.group=하위 실행기를 준비하는 중에 오류가 발생했습니다.
CrosstabGroupExecutor.error.check.child.executor=하위 실행기의 상태를 확인하는 중에 오류가 발생했습니다.

CrosstabHeaderRowExecutor.error.check.force.empty=현재 셀의 강제 비움 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabHeaderRowExecutor.error.check.edge.end=현재 셀의 모서리 끝 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabHeaderRowExecutor.error.retrieve.child.executor=하위 실행기를 검색하는 중에 오류가 발생했습니다.

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=하위 실행기를 생성하는 중에 오류가 발생했습니다.
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=하위 실행기의 상태를 확인하는 중에 오류가 발생했습니다.

CrosstabMeasureRowExecutor.error.generate.child.executor=하위 실행기를 생성하는 중에 오류가 발생했습니다.
CrosstabMeasureRowExecutor.error.check.child.executor=하위 실행기의 상태를 확인하는 중에 오류가 발생했습니다.

CrosstabReportItemExecutor.error.crosstab.loading=확장 항목을 크로스 탭 항목에 로드하지 못했습니다.
CrosstabReportItemExecutor.error.generate.columns=Error happend when generate columns for crosstab.
CrosstabReportItemExecutor.error.prepare.children=Error happend when prepare child executor for crosstab.
CrosstabReportItemExecutor.error.invalid.cube.result=The cube result for crosstab is invalid(=null).

CrosstabRowExecutor.error.check.force.empty=현재 셀의 강제 비움 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabRowExecutor.error.check.edge.start=현재 셀의 모서리 시작 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabRowExecutor.error.retrieve.child.executor=하위 실행기를 검색하는 중에 오류가 발생했습니다.

CrosstabSubTotalRowExecutor.error.check.edge.start=현재 셀의 모서리 시작 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=하위 실행기를 검색하는 중에 오류가 발생했습니다.

TableColumnGenerator.error.process.visibility=열 표시 유형을 처리하는 중에 오류가 발생했습니다.

CrosstabReportItemHandle.error.locate.column.cell=Error locating relevant crosstab cell on columns
CrosstabReportItemHandle.error.locate.row.cell=Error locating relevant crosstab cell on rows
CrosstabReportItemHandle.error.set.column.width=Error setting crosstab column width
CrosstabReportItemHandle.error.set.row.height=Error setting crosstab row height
