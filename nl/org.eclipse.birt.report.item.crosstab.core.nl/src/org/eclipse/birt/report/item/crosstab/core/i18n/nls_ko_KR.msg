###########################################################
# Crosstab name conventions
Element.Crosstab=Cross Tab
Element.CrosstabCell=크로스 탭 셀
Element.CrosstabAggregationCell=크로스 탭 집계 셀
Element.AggregationOnRow=행에서 집계
Element.AggregationOnColumn=열에서 집계
Element.AggregationHeaderLocation=집계 머리글 위치
Element.MeasureView=측정 보기
Element.PageBreakBefore=앞에 페이지 나누기
Element.PageBreakAfter=뒤에 페이지 나누기
Element.SortDirection=정렬 방향
Element.SortType=정렬화 형식
Element.LevelView=수준 보기
Element.DimensionView=차원 보기
Element.CrosstabView=크로스 탭 보기
Element.Cube=큐브
Element.Dimension=차원
Element.Level=수준
Element.Measure=측정
Element.Caption=캡션
Element.CaptionID=캡션 키
Element.MeasureDirection=측정 방향
Element.PageLayout=페이지 레이아웃
Element.EmptyCellValue=빈 셀 값
Element.RepeatRowHeader=행 머리글 반복
Element.RepeatColumnHeader=열 머리글 반복
Element.Mirrored=미러링됨
Element.MirroredStartingLevel=Mirrored Starting Level

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=큐브 차원 요소 {0}이(가) 크로스 탭 {1}의 차원 보기에 중복 적용되었습니다. 
Error.CrosstabException.DUPLICATE_MEASURE=큐브 측정 요소 {0}이(가) 크로스 탭 {1}의 측정 보기에 중복 적용되었습니다.
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=확장 항목을 크로스 탭 항목에 로드하지 못했습니다.
CrosstabReportItemQuery.error.query.building=쿼리 생성에 맞지 않는 크로스 탭 모델입니다.
CrosstabReportItemQuery.error.register.empty.cell.value=빈 셀 값을 등록하는 중에 오류가 발생했습니다.

CrosstabQueryHelper.error.invalid.measure=크로스 탭에 연결된 측정값이 올바르지 않습니다.
CrosstabQueryHelper.error.invalid.dimension=크로스 탭에 연결된 차원이 올바르지 않습니다.
CrosstabQueryHelper.error.invalid.level=크로스 탭에 연결된 수준이 올바르지 않습니다.

BaseCrosstabExecutor.error.process.visibility=표시 유형을 처리하는 중에 오류가 발생했습니다.
BaseCrosstabExecutor.error.process.bookmark=책갈피를 처리하는 중에 오류가 발생했습니다.
BaseCrosstabExecutor.error.process.row.height=Error happend when processing row height.

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
CrosstabReportItemExecutor.error.generate.columns=크로스 탭의 열을 생성하는 중에 오류가 발생했습니다.
CrosstabReportItemExecutor.error.prepare.children=크로스 탭의 하위 실행기를 준비하는 중에 오류가 발생했습니다.
CrosstabReportItemExecutor.error.invalid.cube.result=크로스 탭의 큐브 결과가 올바르지 않습니다(=널).

CrosstabRowExecutor.error.check.force.empty=현재 셀의 강제 비움 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabRowExecutor.error.check.edge.start=현재 셀의 모서리 시작 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabRowExecutor.error.retrieve.child.executor=하위 실행기를 검색하는 중에 오류가 발생했습니다.

CrosstabSubTotalRowExecutor.error.check.edge.start=현재 셀의 모서리 시작 상태를 확인하는 중에 오류가 발생했습니다.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=하위 실행기를 검색하는 중에 오류가 발생했습니다.

TableColumnGenerator.error.process.visibility=열 표시 유형을 처리하는 중에 오류가 발생했습니다.
TableColumnGenerator.error.process.column.width=Error happend when process column width.

CrosstabReportItemHandle.error.locate.column.cell=관련 크로스 탭 셀을 열에서 찾는 중 오류 발생
CrosstabReportItemHandle.error.locate.row.cell=관련 크로스 탭 셀을 행에서 찾는 중 오류 발생
CrosstabReportItemHandle.error.set.column.width=크로스 탭 열 너비 설정 중 오류 발생
CrosstabReportItemHandle.error.set.row.height=크로스 탭 행 높이 설정 중 오류 발생
CrosstabReportItemHandle.msg.change.measure.direction=측정값 방향 변경
CrosstabReportItemHandle.msg.insert.measure=측정값 삽입

MeasureViewHandle.info.aggregation.already.exist=요청된 집계가 이미 있습니다.
MeasureViewHandle.msg.add.aggregation=집계 추가

LevelViewTask.msg.remove.subtotal=부분합 제거
LevelViewTask.msg.add.subtotal=부분합 추가

DimensionViewTask.msg.remove.level=수준 제거
DimensionViewTask.msg.insert.level=수준 삽입

CrosstabViewTask.msg.remove.dimension=차원 제거
CrosstabViewTask.msg.remove.grandtotal=총합계 제거
CrosstabViewTask.msg.add.grandtotal=총합계 추가

CrosstabReportItemTask.msg.add.grandtotal=총합계 추가
CrosstabReportItemTask.msg.set.aggregate.function=집계 함수 설정
CrosstabReportItemTask.msg.pivot.dimension=차원 피벗
CrosstabReportItemTask.msg.insert.dimension=차원 삽입
CrosstabReportItemTask.msg.set.mirroredStartingLevel=Set Mirrored Starting Level

