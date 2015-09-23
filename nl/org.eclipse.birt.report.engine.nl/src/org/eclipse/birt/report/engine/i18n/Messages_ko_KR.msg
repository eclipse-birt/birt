# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=출력 형식 {0}이(가) 지원되지 않습니다.

Error.NullOutputFormat=렌더링 옵션에 대한 OutputFormat이 널(null)입니다.

Error.InvalidParameter=일부 필수 매개 변수 값이 설정되어 있지 않거나 비호환 데이터 형식으로 설정되어 있습니다.

Error.InvalidGroupParameter=보고서 엔진에서 이름이 "{0}"인 매개 변수 그룹을 계산하지 못했습니다.

Error.ParameterTypeIsInvalid=매개 변수 "{0}"의 유형은 "{2}"이(가) 아닌 "{1}"이어야 합니다.

Error.ParamValidationFailure=매개 변수 "{0}"의 유효성 검사에 실패했습니다.

Error.ParameterValueIsNull=필수 매개 변수 {0}이(가) 설정되지 않았습니다.

Error.ParameterValueIsBlank=필수 매개 변수 {0}이(가) 비어 있습니다.

Error.ParamScriptValidationError=매개 변수 "{0}" 스크립트 원본 유효성 검사 오류

Error.ParameterGroupIsNotFoundByGroupname=이름이 "{0}"인 매개 변수 그룹을 찾을 수 없습니다.

Error.ParameterInvalidGroupLevel=그룹 "{0}"의 수준 수가 지정한 수와 일치하지 않습니다.

Error.InvalidDesignFile=디자인 파일 {0}에 오류가 있으며 실행할 수 없습니다.

Error.DesignFileNotFound=디자인 파일 {0}을(를) 찾을 수 없습니다.

Error.CannotCreateExtensionInstance=보고서 엔진이 {0} 이미터를 초기화하지 못했습니다. 설치된 이미터의 필수 라이브러리를 확인하십시오.

Error.MissingComputedColumnExpression=계산된 열 {0}에 대한 식이 누락되었습니다.

Error.ParameterInGroupIsnotScalar=매개 변수 그룹 "{0}"의 매개 변수는 스칼라여야 합니다.

Error.ParameterEvaluationError=매개 변수 그룹 "{0}" 계산에 실패했습니다.

Error.InvalidEmitterID = 렌더링 옵션에 대한 EmitterID {0}이(가) 잘못되었습니다.

###########################################################
Error.ErrorsOnPage=보고서를 생성하는 도중 오류가 발생했습니다.

Error.Error=오류

Error.ScriptFileLoadError=스크립트 파일 {0}을(를) 로드할 수 없습니다.

Error.ScriptEvaluationError=스크립트 "{0}"을(를) 평가하는 도중 오류가 발생했습니다.:\n{1}

Error.TextualItemProcessingError=텍스트 항목 {0}을(를) 처리하는 도중 문제가 발생했습니다.

Error.TableItemProcessingError=테이블 {0}을(를) 처리하는 도중 문제가 발생했습니다.

Error.ExtendedItemGenerationError=확장 항목 {0}을(를) 생성하는 도중 문제가 발생했습니다.

Error.ExtendedItemRenderingError={0}의 확장 항목 {1}을(를) 렌더링하는 도중 문제가 발생했습니다.

Error.GridItemProcessingError=눈금 {0}을(를) 처리하는 도중 문제가 발생했습니다.

Error.ImageItemProcessingError=이미지 {0}을(를) 처리하는 도중 문제가 발생했습니다.

Error.MissingImageFileError=이미지 파일이 누락되었습니다.

Error.InvalidImageSourceError=잘못된 이미지 원본 형식입니다.

Error.InvalidDatabaseImageError=잘못된 데이터베이스 이미지입니다. 이미지 식은 "{0}"입니다.

Error.EmbeddedExpressionError=내장된 식 "{0}"을(를) 평가하는 도중 오류가 발생했습니다.

Error.EmbeddedImageError=내장된 이미지를 처리하는 도중 오류가 발생했습니다.

Error.HTMLImageError=HTML <img> 태그에서 내장된 이미지를 처리하는 도중 오류가 발생했습니다.

Error.UndefinedDatasetError=보고서에서 "{0}" 데이터 집합이 정의되지 않았습니다.

Error.InvalidExpressionError=식 "{0}"이(가) 잘못되었습니다.

Error.ListProcessingError=목록 항목 {0}을(를) 처리하는 도중 문제가 발생했습니다.

Error.ErrorOnReportPage=다음 항목에 오류가 있습니다:

Error.ReportErrorMessage = {0} {1}:

Error.ReportErrorMessageWithID = {0}(ID = {1}):

Error.ReportErrorID = {0}({1}번)

Error.ReportErrorDetail = 상세 정보:

Error.JarNotFoundError = Jar {0}을(를) 찾을 수 없습니다.

Error.ScriptClassCastError = 클래스 {0}이(가) {1} 인터페이스를 구현하지 않습니다.

Error.ScriptClassNotFoundError = 클래스 {0}을(를) 찾을 수 없습니다.

Error.ScriptClassIllegalAccessError = 클래스 {0}을(를) 로드하려는 중 잘못된 액세스 예외가 발생했습니다.

Error.ScriptClassInstantiationError = 클래스 {0}을(를) 인스턴스화할 수 없습니다. 인터페이스 또는 추상 클래스가 아닌지 확인하십시오.

Error.UnhandledScriptError = 스크립트를 실행하는 중 처리되지 않은 예외가 발생했습니다.

###########################################################
Error.PageNotFoundError = 페이지 {0}을(를) 찾을 수 없습니다.

Error.PageNumberRangeError = 잘못된 페이지 범위: {0}.

Error.PageHintLoadingError = 페이지 힌트를 로드할 수 없습니다.

Error.PagesLoadingError = 페이지 {0}을(를) 로드할 수 없습니다.

Error.InvalidGroupError = 잘못된 그룹: {0}.

Error.InvalidExtensionError = 잘못된 식 ID: {0}, 잘못된 형식: {1}.

Error.InvalidColumnIndexError = 잘못된 열 인덱스

Error.InvalidInstanceIDError = 잘못된 인스턴스 ID: {0}.

Error.InvalidBookmarkError = 잘못된 책갈피: {0}.

Error.ExpressionEvaluationError = 다음 표시 유형 식이 올바른 부울 값으로 계산되지 않습니다: {0}.

Error.BookmarkNotFoundError = 책갈피를 찾을 수 없습니다: {0}.

Error.DateError = 날짜 오류: {0}.

Error.CubePositionError = 큐브 위치 가져오기 오류

Error.SubqueryCreateError = 하위 쿼리는 다른 하위 쿼리/쿼리에서만 만들 수 있습니다.

Error.BindDataRetrivingError = 바인딩 데이터 검색 오류

Error.DataSourceError = 데이터 집합의 데이터 원본은 널(null)일 수 없습니다.

Error.UnsupportedQueryDefinitionError = 지원되지 않는 쿼리 정의 {0}

Error.ReportQueryLoadingError = 보고서 쿼리 로드 중 오류가 발생했습니다. 보고서를 생성하기 위한 데이터를 검색할 수 없습니다.

Error.ReportQueryLoadingError2 = 보고서 쿼리를 로드할 수 없습니다: {0}. ID가 {1}인 보고서 요소에 대한 보고서 문서를 생성하는 도중 오류가 발생했습니다.

Error.ExtentionIDMissingError = 데이터 원본 정의 {0}에서 확장 ID가 누락되었습니다.

Error.DataExportionError = 인스턴스 ID로 데이터를 내보내지 못했습니다: {0}.

Error.UnsupportedDocumentVersionError = 지원되지 않는 문서 버전: {0}.

Error.ResultsetIteratorError = 결과 집합 반복자를 올바른 행으로 이동해야 합니다.

Error.ReportDesignNotFoundError = 보고서 문서 {0}에서 보고서 디자인을 찾을 수 없습니다.

Error.PreparedQueryNotFoundError = 준비된 쿼리 {0}을(를) 찾을 수 없습니다.

Error.IncorrectParentResultSetError = 하위 쿼리에 대한 상위 결과 집합이 잘못되었습니다: {0}.

Error.UnsupportedQueryTypeError = 지원되지 않는 쿼리 형식 {0}

Error.QueryNotBuiltError = 요소 {0}에 대한 쿼리를 작성하지 못했습니다.

Error.RenderTaskNotFinishedError = 렌더링 작업이 완료되지 않았습니다.

Error.RenderOptionError = 보고서를 렌더링하려면 렌더링 옵션을 지정해야 합니다.

Error.ReportArchiveError = 보고서를 실행하는 동안 보고서 보관을 지정하지 않았습니다.

Error.ReportDocNameNotSpecifiedError = 보고서를 실행하는 동안 보고서 문서 이름을 지정하지 않았습니다.

Error.ReportRunError = 보고서를 실행하는 동안 오류가 발생했습니다.

Error.ReportDocmentOpenError = 보고서 문서를 열지 못했습니다.

Error.ReportArchiveOpenError = 보고서 보관을 열지 못했습니다.

Error.SkipError = 건너뛰는 중 오류가 발생했습니다.

Error.ResultsetExtractError = 결과 집합을 찾을 수 없습니다.

Error.FailedToInitializeEmitter = 이미터를 초기화하지 못했습니다.

###########################################################
PDFCreator = iText {1}을(를) 사용하는 BIRT 보고서 엔진 {0}

###########################################################
Error.FlashObjectNotSupported = 플래시 개체 보고서 항목은 이 보고서 형식에서 지원되지 않습니다.

Error.ReportItemNotSupported = 현재 보고서 항목은 이 보고서 형식에서 지원되지 않습니다.

Error.ResourceUnreachable = 이 리포트 항목의 리소스에 연결할 수 없습니다.

Error.UpdateUserAgent = PDF 파일에서 플래시 콘텐츠를 보려면 플래시 지원을 활성화하거나 최신 버전의 Adobe Reader를 다운로드하십시오.

###########################################################
Error.ReportRunnableNotSet = 실행 가능한 보고서가 설정되지 않았습니다.

Error.FailedToLoadTOCTree = TOC 트리를 로드하지 못했습니다.

Error.ExceptionOnFetchingBookmarks = 책갈피를 가져오는 도중 예외가 발생했습니다.

Error.FailedToSaveDesign = 디자인을 저장하지 못했습니다.

Error.ExceptionOnCopyingArchives = 아카이브를 복사하는 동안 예외가 발생했습니다.

Error.UnsupportedCoreStreamVersion = 지원되지 않는 코어 스트림 버전입니다: {0}.

Error.UnsupportedEngineExtension = 지원되지 않는 엔진 확장명 {0}입니다.

Error.FailToCreateDataEngine = 데이터 엔진을 생성할 수 없습니다.

Error.FailedToCreateTOC = TOC를 생성하지 못했습니다.

Error.UnsupportedExprssionType = 지원되지 않는 식 형식입니다.

Error.UnknownContentVersion = 알 수 없는 콘텐츠 버전입니다.

Error.UnknownFieldID = 알 수 없는 필드 ID입니다: {0}.

Error.ResourceNotAccessible = {0}에 액세스할 수 없습니다.

Error.ERROR_WITH_STATUS=문서에 오류가 있습니다: {0}.

###########################################################
Error.ElementID = 요소 ID

###########################################################
Message.TaskCancel = 작업이 취소되었습니다.