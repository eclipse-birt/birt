###########################################################
# Crosstab name conventions
Element.Crosstab=크로스 탭

Element.CrosstabCell=크로스 탭 셀

Element.CrosstabAggregationCell=크로스 탭 집계 셀

Element.AggregationOnRow=행에서 집계

Element.AggregationOnColumn=열에서 집계

Element.AggregationHeaderLocation=집계 머리글 위치

Element.GrandTotalLocation=총합계 위치

Element.MeasureView=측정 보기

Element.ComputedMeasureView=계산된 측정값 보기

Element.PageBreakBefore=단락 앞에서 페이지 나누기

Element.PageBreakAfter=단락 뒤에서 페이지 나누기

Element.PageBreakInside=사이에 페이지 나누기

Element.PageBreakInterval=페이지 나누기 간격

Element.SortDirection=정렬 방향

Element.SortType=정렬 형식

Element.LevelView=수준 보기

Element.DisplayField=필드 표시

Element.DimensionView=차원 보기

Element.CrosstabView=크로스 탭 보기

Element.Cube=Cube
Element.Dimension=차원

Element.Level=수준

Element.Measure=측정

Element.Caption=캡션

Element.CaptionID=캡션 키

Element.Summary=요약

Element.MeasureDirection=측정값 방향

Element.PageLayout=페이지 레이아웃

Element.EmptyCellValue=빈 셀 값

Element.RepeatRowHeader=행 머리글 반복

Element.RepeatColumnHeader=열 머리글 반복

Element.Mirrored=미러링됨

Element.MirrorStartingLevel=미러 시작 수준

Element.HideMeasureHeader=측정값 머리글 숨기기

Element.SpanOverOnColumn=열에서 길이 재기

Element.SpanOverOnRow=행에서 길이 재기

Element.Direction.Horizontal=가로

Element.Direction.Vertical=세로

Element.choice.before=이전

Element.choice.after=이후

Element.RowPageBreakInterval=행 페이지 간격 나누기

Element.ColumnPageBreakInterval=열 페이지 간격 나누기

Element.TagType=태그 형식

Method.onPrepare=준비 시

Method.onCreate=생성 시

Method.onRender=렌더링 시

Method.onPageBreak=페이지 나누기

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=큐브 차원 요소''{0}''을(를) 중복된 이름의 크로스 탭에 적용할 수 없습니다.

Error.CrosstabException.DUPLICATE_MEASURE=큐브 측정 요소 ''{0}''을(를) 중복된 이름의 크로스 탭에 적용할 수 없습니다.

Error.CrosstabException.DUPLICATE_LEVEL=큐브 수준 요소 ''{0}''을(를) 중복된 이름의 크로스 탭에 적용할 수 없습니다.

Error.CrosstabException.DIMENSION_NOT_FOUND=크로스 탭에서 차원 보기 ''{0}''을(를) 찾을 수 없습니다.

Error.CrosstabException.MEASURE_NOT_FOUND=크로스 탭에서 측정 보기 ''{0}''을(를) 찾을 수 없습니다.

Error.CrosstabException.LEVEL_NOT_FOUND=크로스 탭에서 수준 보기 ''{0}''을(를) 찾을 수 없습니다.

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=확장 항목을 크로스 탭 항목에 로드하지 못했습니다.

CrosstabReportItemQuery.error.query.building=쿼리 생성에 맞지 않는 크로스 탭 모델입니다.

CrosstabReportItemQuery.error.register.empty.cell.value=빈 셀 값을 등록하는 중에 오류가 발생했습니다.

CrosstabQueryHelper.error.invalid.measure=크로스 탭에 널(null) 측정 정의가 있거나 관련 큐브에 특정 측정 "{0}"이(가) 없습니다.

CrosstabQueryHelper.error.invalid.dimension.row=크로스 탭 행 영역에 널(null) 차원 정의가 있거나 관련 큐브에 특정 차원 "{0}"이(가) 없습니다.

CrosstabQueryHelper.error.invalid.level.row=크로스 탭 행 영역에 널(null) 수준 정의가 있거나 관련 큐브에 특정 수준 "{0}"이(가) 없습니다.

CrosstabQueryHelper.error.invalid.dimension.column=크로스 탭 열 영역에 널(null) 차원 정의가 있거나 관련 큐브에 특정 차원 "{0}"이(가) 없습니다.

CrosstabQueryHelper.error.invalid.level.column=크로스 탭 열 영역에 널(null) 수준 정의가 있거나 관련 큐브에 특정 수준 "{0}"이(가) 없습니다.

BaseCrosstabExecutor.error.process.visibility=표시 유형을 처리하는 중에 오류가 발생했습니다.

BaseCrosstabExecutor.error.process.bookmark=머리글을 처리하는 동안 오류가 발생했습니다.

BaseCrosstabExecutor.error.process.headers=머리글을 처리하는 동안 오류가 발생했습니다.

BaseCrosstabExecutor.error.process.row.height=행 높이를 처리하는 중에 오류가 발생했습니다.

CrosstabCellExecutor.error.restor.data.position=데이터 위치를 복원하는 중에 오류가 발생했습니다.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=하위 실행기를 생성하는 중에 오류가 발생했습니다.

CrosstabGrandTotalRowExecutor.error.check.child.executor=하위 실행기의 상태를 확인하는 중에 오류가 발생했습니다.

CrosstabHeaderExecutor.error.reset.row.position=행 가장자리 커서 위치를 재설정하는 중 오류가 발생했습니다.

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

TableColumnGenerator.error.process.column.width=열 너비를 처리하는 중에 오류가 발생했습니다.

CrosstabReportItemHandle.error.locate.column.cell=관련 크로스 탭 셀을 열에서 찾는 중 오류 발생

CrosstabReportItemHandle.error.locate.row.cell=관련 크로스 탭 셀을 행에서 찾는 중 오류 발생

CrosstabReportItemHandle.error.set.column.width=크로스 탭 열 너비 설정 중 오류 발생

CrosstabReportItemHandle.error.set.row.height=크로스 탭 행 높이 설정 중 오류 발생

CrosstabReportItemHandle.msg.change.measure.direction=측정값 방향 변경

CrosstabReportItemHandle.msg.insert.measure=측정값 삽입

CrosstabReportItemHandle.exception.name.blank=ComputedMeasure에 이름이 필요합니다.

CrosstabReportItemHandle.exception.name.duplicate=지정된 측정값 이름이 이미 사용 중이며 중복될 수 없습니다.

MeasureViewHandle.info.aggregation.already.exist=요청된 집계가 이미 있습니다.

MeasureViewHandle.msg.add.aggregation=집계 추가

LevelViewTask.msg.remove.subtotal=부분합 제거

LevelViewTask.msg.add.subtotal=부분합 추가

DimensionViewTask.msg.remove.level=수준 제거

DimensionViewTask.msg.insert.level=수준 삽입

MeasureViewTask.msg.remove.header=측정값 머리글 제거

MeasureViewTask.msg.add.header=측정값 머리글 추가

CrosstabViewTask.msg.add.dimension=차원 추가

CrosstabViewTask.msg.remove.dimension=차원 제거

CrosstabViewTask.msg.remove.grandtotal=총합계 제거

CrosstabViewTask.msg.add.grandtotal=총합계 추가

CrosstabReportItemTask.msg.add.grandtotal=총합계 추가

CrosstabReportItemTask.msg.set.aggregate.function=집계 함수 설정

CrosstabReportItemTask.msg.pivot.dimension=차원 피벗

CrosstabReportItemTask.msg.insert.dimension=차원 삽입

CrosstabReportItemTask.msg.set.mirroredStartingLevel=미러된 시작 수준 설정

CrosstabReportItemTask.msg.pivot.crosstab=피벗 크로스 탭

CrosstabReportItemHandle.Error.HasNoCube=크로스 탭은 하나의 큐브에 액세스할 수 있어야 합니다.

CrosstabReportItemHandle.log.validate=확인 중(...)

CrosstabScriptHandler.error.javascript=JavaScript의 줄 {1}에 있는 {0}:\r\n{2}\r\n

CrosstabScriptHandler.info.try.register.crosstab.javascript.content=크로스 탭 JavaScript 내용을 등록하십시오...

CrosstabScriptHandler.info.crosstab.javascript.content.registered=크로스 탭 JavaScript 내용을 등록했습니다.

CrosstabScriptHandler.info.try.load.crosstab.java.handler=크로스 탭 Java 이벤트 처리기를 로드하십시오...

CrosstabScriptHandler.info.crosstab.java.handler.loaded=크로스 탭 Java 이벤트 처리기 [{0}]이(가) 로드되었습니다.

CrosstabScriptHandler.info.invalid.crosstab.java.handler=크로스 탭 Java 이벤트 처리기 [{0}]의 구현이 호환되지 않습니다.
