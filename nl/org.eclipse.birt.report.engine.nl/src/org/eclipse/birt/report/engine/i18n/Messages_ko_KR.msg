# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=출력 형식 {0}이(가) 지원되지 않습니다.
Error.InvalidParameter=일부 필수 매개 변수 값이 설정되어 있지 않거나 비호환 데이터 형식으로 설정되어 있습니다.
Error.InvalidGroupParameter=보고서 엔진에서 이름이 "{0}"인 매개 변수 그룹을 계산하지 못했습니다.
Error.ParameterTypeIsInvalid=매개 변수 "{0}"의 유형은 "{2}"이(가) 아닌 "{1}"이어야 합니다.
Error.ParameterValueIsNull=필수 매개 변수 {0}이(가) 설정되지 않았습니다.
Error.ParameterValueIsBlank=필수 매개 변수 {0}이(가) 비어 있습니다.
Error.ParamScriptValidationError=매개 변수 "{0}" 스크립트 원본 유효성 검사 오류
Error.ParameterGroupIsNotFoundByGroupname=이름이 "{0}"인 매개 변수 그룹을 찾을 수 없습니다.
Error.ParameterInvalidGroupLevel=The level count of group "{0}" does not match the specified one.
Error.InvalidDesignFile=디자인 파일 {0}에 오류가 있으며 실행할 수 없습니다.
Error.DesignFileNotFound=디자인 파일 {0}을(를) 찾을 수 없습니다.
Error.CannotCreateExtensionInstance=보고서 엔진이 {0} 이미터를 초기화하지 못했습니다. 설치된 이미터의 필수 라이브러리를 확인하십시오.
Error.MissingComputedColumnExpression=계산된 열 {0}에 대한 식이 누락되었습니다.
Error.ParameterInGroupIsnotScalar=매개 변수 그룹 "{0}"의 매개 변수는 스칼라여야 합니다.
Error.ParameterEvaluationError=매개 변수 그룹 "{0}" 계산에 실패했습니다.

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

Error.ScriptClassCastError = Class {0} does not implement the {1} interface.
Error.ScriptClassNotFoundError = Class {0} not found.
Error.ScriptClassIllegalAccessError = IllegalAccessException when trying to load class {0}.
Error.ScriptClassInstantiationError = 클래스 {0}을(를) 인스턴스화할 수 없습니다. 인터페이스 또는 추상 클래스가 아닌지 확인하십시오.
Error.UnhandledScriptError = Unhandled exception when executing script.

###########################################################
Error.PageNotFoundError = Page {0} is not found.
Error.PageNumberRangeError = Invalid page range: {0}.
Error.PageHintLoadingError = Can''t load the page hint.
Error.PagesLoadingError = Can''t load the pages {0}.

Error.InvalidGroupError = Invalid group : {0}.
Error.InvalidExtensionError = Invalid extension id and format.
Error.InvalidColumnIndexError = Invalid column index.
Error.InvalidInstanceIDError = Invalid instance id : {0}.
Error.InvalidBookmarkError = Invalid bookmark : {0}.

Error.ExpressionEvaluationError = The following visibility expression does not evaluate to a legal boolean value: {0}.
Error.BookmarkNotFoundError = Can not find bookmark :{0}.
Error.DateError = date:{0}.
Error.CubePositionError = Get cube position error.
Error.SubqueryCreateError = Sub-query can only be created in another sub-query/query.
Error.BindDataRetrivingError = Retrieving binding data error.
Error.DataSourceError = Data source of data set can not be null.
Error.UnsupportedQueryDefinitionError = Unsupported query definition {0}.
Error.ReportQueryLoadingError = Can''t load the report query: {0}.
Error.ExtentionIDMissingError = Missing extension id in data source definition {0}.
Error.DataExportionError = Failed to export data by instance id :{0}.
Error.UnsupportedDocumentVersionError = Unsupported document version: {0}.
Error.ResultsetIteratorError = Result set iterator must be moved to valid row.
Error.ReportDesignNotFoundError = Can not find the report design in the report document {0}.
Error.PreparedQueryNotFoundError = Can''t find the prepared query {0}.
Error.IncorrectParentResultSetError = Incorrect parent resultSet for subQuery: {0}.
Error.UnsupportedQueryTypeError = Unsupported query type {0}.
Error.RenderTaskNotFinishedError = Render task is not finished.
Error.RenderOptionError = Render options have to be specified to render a report.
Error.ReportArchiveError = Report archive is not specified when running a report.
Error.ReportDocNameNotSpecifiedError = Report document name is not specified when running a report.
Error.ReportRunError = Error happened while running the report.
Error.ReportDocmentOpenError = Failed to open the report document.
Error.ReportArchiveOpenError = Can not open the report archive.
Error.SkipError = Error happened when skipping.
