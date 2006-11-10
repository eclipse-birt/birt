# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0}이(가) {0}에 없습니다.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=데이터 원본이 정의되지 않았습니다: {0}
data.engine.UndefinedDataSet=데이터 집합이 정의되지 않았습니다: {0}
data.engine.UnsupportedDataSourceType=데이터 원본 "{0}"은(는) 지원되지 않는 형식입니다.
data.engine.UnsupportedDataSetType=데이터 집합 "{0}"은(는) 지원되지 않는 형식입니다.
data.engine.UnsupportedGroupOnClobAndBLOB=CLOB 또는 BLOB 데이터의 그룹 "{0}"은(는) 지원되지 않습니다.
data.engine.UnsupportedSortOnClobAndBLOB=CLOB 또는 BLOB 데이터의 정렬 "{0}"은(는) 지원되지 않습니다.

data.engine.EndOfGroupOnly=이 집계식은 그룹의 끝에서만 사용할 수 있습니다.
data.engine.InvalidAggrGroup=집계 함수에 대한 그룹화 수준이 잘못되었습니다: {0}
data.engine.WrongNumAggrArgs=집계 함수에 대한 인수의 수가 잘못되었습니다: {0}

data.engine.DupPropertyName=속성 이름이 중복되었습니다: {0}
data.engine.NoSubQueryName=이름을 가진 하위 쿼리를 찾을 수 없습니다: {0}

data.engine.DataSourceMissingExtId=데이터 원본 {0}의 정의에서 확장 ID가 누락되었습니다.
data.engine.NullGroupStart=그룹 시작 값은 널(null)일 수 없습니다.
data.engine.UnsupportedDirectNestedAggregate=집계를 직접 중첩하는 것은 지원되지 않습니다.

data.engine.ResultClosed=QueryResults 또는 반복자가 닫혔습니다.
data.engine.NoCurrentRow=Result iterator에 현재 행이 없습니다. 시작되지 않았거나 종료되었습니다.
data.engine.InvalidJSExpr=잘못된 Javascript 식: {0}
data.engine.InvalidTotalFuncName=합계 함수 이름이 잘못되었습니다: {0}
data.engine.InvalidTotalFunc=잘못된 합계 함수
data.engine.NoAggrFunc=이 식에서 집계 함수 호출이 필요하지 않습니다.
data.engine.InvalidExprHandle=식 처리가 잘못되었습니다.
data.engine.InvalidAggrGroupExpression=집계 함수에 대해 잘못된 그룹 식: {0}
data.engine.InvalidSortKeyColumn=정렬 키 열이 잘못되었습니다: {0}
data.engine.InvalidGroupLevel=잘못된 그룹 수준: {0}
data.engine.InvalidGroupKeyName=그룹 키 열 이름이 잘못되었습니다: {0}
data.engine.BadCloseScript=CustomDataSet의 closeScript 계산에 실패했습니다.
data.engine.InvalidCustomFieldIdx=사용자 지정 필드 인덱스가 잘못되었습니다: {0}
data.engine.InvalidGroupName=그룹 이름이 잘못되었습니다.
data.engine.DuplicateGroupName=그룹 이름이 중복됩니다.

data.engine.BadOpenScript=CustomDataSet의 openScript 계산에 실패했습니다.
data.engine.DataSourceIsOpen=데이터 원본이 이미 열려 있습니다.
data.engine.DataSourceNotOpen=데이터 원본이 열려 있지 않습니다.
data.engine.EmptyCustomFieldName=사용자 지정 필드 이름이 비어 있지 않아야 합니다.
data.engine.DupCustomFieldName=중복된 사용자 지정 필드 이름입니다: {0}
data.engine.NoResultSet=쿼리 실행에서 결과 집합을 반환하지 않았습니다.
data.engine.NoResultMetadata=결과 메타데이터를 사용할 수 없습니다.
data.engine.QueryAlreadyPrepared=쿼리가 이미 준비되었습니다.
data.engine.QueryNotPrepared=쿼리가 아직 준비되지 않았습니다.
data.engine.InvalidFieldName=필드 이름이 잘못되었습니다: {0}
data.engine.DupColumnName=열 이름이 중복되었습니다: {0}
data.engine.DupAlias=열 이름 또는 별칭이 중복되었습니다: {0}
data.engine.InvalidFieldIndex=필드 인덱스가 잘못되었습니다: {0}

data.engine.MatchError=패턴 구문을 확인하십시오. 문자열을 패턴과 일치시키는 중 오류가 발생했습니다: {0} 
data.engine.BadOperandType=예기치 못한 피연산자 데이터 형식: {0}

data.engine.BadParameterCount=잘못된 매개 변수 수가 제공되었습니다.
data.engine.BadParameterType=매개 변수 중 하나에 잘못된 데이터 형식이 있습니다.
data.engine.NoIrrSolution=제공된 데이터를 포함한 Irr 함수에 대한 솔루션을 찾을 수 없습니다.
data.engine.ExpectBooleanReturnType=스크립트된 데이터 집합의 {0} 스크립트가 부울 결과를 반환해야 합니다.
data.engine.BadIntervalUnit=잘못된 간격 단위 값: {0}
data.engine.InvalidFieldName=필드 이름이 잘못되었습니다: {0}
data.engine.BadGroupKeyType=그룹 키에 지원되지 않는 데이터 형식이 있습니다: {0}
data.engine.group.interval=형식이 {1}인 열에서 {0} 간격 설정은 지원되지 않습니다.
data.engine.group.interval.Rowid=행 ID 열에서 숫자 간격만이 지원되는 간격입니다.
data.engine.group.interval.invalid=주어진 간격이 부적합합니다.
data.engine.NoSupportFilterOnGroup=현재 그룹에서 필터링이 지원되지 않습니다.
data.engine.InvalidTopBottomArgument=위쪽/아래쪽 필터의 인수는 숫자이어야 합니다.
data.engine.InvalidTopBottomNArgument=TopN/BottomN 필터의 인수는 0 또는 양의 정수여야 합니다.
data.engine.InvalidTopBottomPercentArgument=TopPercent/BottomPercent 필터의 인수는 100보다 작거나 같은 양수여야 합니다.
data.engine.InvalidTopBottomN=처리되는 총 행수는 {0}을(를) 넘지 않아야 합니다. 
data.engine.InvalidPercentileArgument=Total.Percentile 함수는 두 번째 인수로 0부터 1까지의 숫자만 사용합니다.
data.engine.InvalidQuartileArgument=Total.Quartile 함수는 두 번째 인수로 0부터 4까지의 정수만 사용합니다.
data.engine.BadAggrFuncParam=집계 함수에 대한 매개 변수가 잘못되었습니다: {0}
data.engine.ResetRateGuess=새로운 rate guess를 재설정해야 합니다.
data.engine.UnsupportedCondOp=조건부 연산자가 지원되지 않습니다: {0}
data.engine.EmptyExpression=식은 널(null)이거나 비워 둘 수 없습니다.

data.engine.InvalidJoinType=잘못된 조인 형식입니다.
data.engine.InvalidJoinOperator=잘못된 조인 연산자입니다.
data.engine.BadDataSetParamName=정의되지 않은 데이터 집합 매개 변수: {0}.

#column binding
data.engine.InvalidBoundColumnName=잘못된 바인딩된 열 이름: {0}.
data.engine.InvalidGroupKey={1} 그룹의 "{0}" 그룹 키를 하위 그룹에 정의된 열 바인딩에 종속시킬 수 없습니다. 
data.engine.ColumnBindingNotExist=열 바인딩 "{0}"이(가) 없습니다.
data.engine.ColumnBindingCycle="{0}" 열 바인딩에 종속 사이클이 형성되어 있습니다.

#computed column value
data.engine.BadCompColExpr=계산된 열 "{0}"에 대한 식이 잘못되었습니다.
data.engine.CompCol.WrongDataType=계산된 열 "{0}"의 데이터 형식이 {1}이지만 값 중 하나가 "{2}"이므로 {1}(으)로 변환할 수 없습니다.
data.engine.ScriptResult.WrongDataType=스크립트 식 결과의 데이터 형식은 {0}입니다. 그러나 값 중 하나가 "{1}"이어서 {0}(으)로 변환할 수 없습니다.

data.engine.BadFilterExpr=필터 식 "{0}"이(가) 잘못되었습니다.
data.engine.BadFilterDefn=식 "{0}"을(를) 사용한 필터 정의가 잘못되었습니다.

#clob and blob error
data.engine.opencloberror=CLOB 문자 스트림 열기 오류입니다.
data.engine.readcloberror=CLOB 문자 스트림 읽기 오류입니다.
data.engine.openbloberror=BLOB binaryStream 스트림 열기 오류입니다.
data.engine.readbloberror=BLOB binaryStream 스트림 읽기 오류입니다.

#resultsetcache exception
data.engine.BadDestIndex=destIndex의 값 {0}이(가) 잘못되었습니다. 0과 {1} 사이의 값이어야 합니다.
data.engine.WriteTempError=큰 데이터 집합에 대해 임시 파일을 쓰는 중 예외가 발생했습니다.
data.engine.ReadTempError=큰 데이터 집합에 대해 임시 파일을 읽는 중 예외가 발생했습니다.

#load data engine factory exception
load.factory.error=데이터 엔진 팩토리 로드 중 오류가 발생했습니다.

# Wrapped Error
data.engine.DataConversionError=데이터 형식을 변환하는 중 오류가 발생했습니다.
data.engine.BadDataTypeCondExpr=조건식에 잘못된 데이터 형식이 있습니다.
data.engine.BirtException=BIRT 예외 {0}이(가) 발생했습니다. 자세한 내용은 다음 예외를 참조하십시오.

# data set cache error
data.engine.datasetcache.save.error=데이터 집합 캐시를 저장하는 중 오류가 발생했습니다.
data.engine.datasetcache.load.error=데이터 집합 캐시 파일을 로드하는 중 오류가 발생했습니다.

# error related with data engine context
data.engine.InvalidMode=데이터 엔진 컨텍스트의 모드 값 {0}이(가) 잘못되었습니다.
data.engine.InvalidArchive=생성 또는 프리젠테이션 시, 보고서 보관이 널(null)일 수 없습니다. 
data.engine.SaveReportDocumentError=저장 중 보고서 문서 오류가 발생했습니다: {0}
data.engine.LoadReportDocumentError=로드 중 보고서 문서 오류가 발생했습니다: {0}
data.engine.GetSaveStreamError=저장 스트림을 가져오는 중 보고서 문서 오류가 발생했습니다.
data.engine.GetLoadStreamError=로드 스트림을 가져오는 중 보고서 문서 오류가 발생했습니다.
data.engine.NullExpr=프리젠테이션에서 값을 검색할 수 있도록 식은 널(null) 값이 될 수 없습니다.
data.engine.InValidExpr=프리젠테이션에서 값을 검색할 식이 잘못되었습니다.
data.engine.BadGroupExpr=그룹 식을 입력해야 합니다. 
data.engine.ResultSetNotStart=결과 집합이 시작되지 않았습니다.

data.engine.invalidRowIndex=잘못된 행 인덱스 값: {0}
data.engine.backwardSeekError=이 버전에서는 뒤로 검색이 지원되지 않습니다.
data.engine.startError=다음 메서드를 먼저 호출해야 합니다.

# query running based on report document
data.engine.rd.errorfilters=쿼리에 정의된 필터는 지원되지 않습니다.
 
# other error
data.engine.ScriptEvalError=스크립트 평가 중에 오류가 발생했습니다(원본: {0}, 줄: {1}:  {2}.
data.engine.PreparedQueryClosed=PreparedQuery 인스턴스가 닫혔습니다.
data.engine.NullDefaultInParamValue=입력 매개 변수의 기본값이 널(null)일 수 없습니다.
data.engine.BadGroupKeyExpr=그룹 키 식 {0}이(가) 잘못되었습니다.
data.engine.GroupNotExist=열 {1}에서 사용한 그룹 "{0}"은(는) 없습니다.
data.engine.BadSortKeyExpr=정렬 키 식 {0}이(가) 잘못되었습니다.
data.engine.NoParamMetaForDataSource=이 데이터 원본 형식에 대한 매개 변수 메타데이터를 가져올 수 없습니다.
data.engine.UnexpectedError=예기치 못한 예외로 인해 데이터 엔진 작업에 실패했습니다. 
data.engine.ConvertDataTypeError={0} 매개 변수 값을 {1} 형식으로 변환할 수 없습니다.
data.engine.CannotInstantiateAggregationFactory=집계 팩토리 {0}을(를) 인스턴스화할 수 없습니다.
date.engine.DuplicateAggregationName=중복된 집계 이름입니다: {0}
data.engine.incorrectGroupKeyValues=지정한 그룹 키 값 배열이 잘못되었습니다.
data.engine.NoExpressionProcessorAvailable=사용 가능한 ExpressionProcessor가 없습니다.
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0}은(는) {1}(으)로 변환할 수 없습니다.
data.engine.NoRowUpdate=지금은 데이터 행을 업데이트할 수 없습니다.
data.engine.BadDataType=데이터 형식이 지원되지 않습니다.
data.engine.BadDataExpression=데이터 식은 널(null)이 아니어야 합니다.

data.engine.cacheFunctionWrongMode=캐시 기능은 DIRECT_PRESENTATION 모드용으로만 사용됩니다.
data.engine.document.invalidIndexArray=잘못된 인덱스 배열입니다.
data.engine.document.invalidGroupItem=제거한 그룹 항목이 잘못되었습니다.
data.engine.resultIteratorNotOpen=Result Iterator를 열 수 없습니다.
data.engine.resultIteratorClosed=Result Iterator가 닫혔습니다.
data.engine.invalidOutputParameterIndex=잘못된 출력 매개 변수 인덱스: {0}
data.engine.outputParameterOutOfBound=출력 매개 변수 인덱스가 범위를 벗어났습니다: {0}
data.engine.invalidOutputParameterName=잘못된 출력 매개 변수 인덱스: {0}
data.engine.document.notSupportInPresentation=프리젠테이션에서 지원되지 않습니다.
data.engine.wrongStatus=잘못된 상태
data.engine.wrongVersion=2.1 이전 버전에서는 지원되지 않습니다.
data.engine.unsupportOperationException=지원되지 않는 연산
data.engine.resultClassSaveError=열 바인딩 내의 일부 열 참조가 없습니다.

# oda consumer error
odaconsumer.ColumnNameCannotBeEmptyOrNull=열 이름은 비워 두거나 널(null)이 될 수 없습니다.
odaconsumer.ColumnPositionIsOneBased=Column position is 1-based and cannot be less than 1.
odaconsumer.ColumnAliasCannotBeEmpty=열 별칭은 빈 문자열이 될 수 없습니다.
odaconsumer.CannotProcessDriverConfig=({0}) 드라이버의 구성을 처리할 수 없습니다.
odaconsumer.OdaInitEntryNotFound=드라이버 {0}의 초기 진입점을 찾을 수 없거나 인스턴스화할 수 없습니다.
odaconsumer.OdaDriverOnUnsupportedPlatform=드라이버 {0}이(가) {1}을(를) 지원하지 않습니다.
odaconsumer.CannotGenerateUrl=클래스 로더의 URL {0}을(를) 생성할 수 없습니다.
odaconsumer.UnsupportedNativeType=드라이버 {2}의 데이터 집합 형식 {1}에 대해 지원되지 않는 고유 형식 {0}입니다.
odaconsumer.UnsupportedDataSetType=드라이버 {1}이(가) 데이터 집합 형식 {0}을 지원하지 않습니다.
odaconsumer.CannotDetermineDefaultDataSetType=널(null) 데이터 집합 형식입니다. 드라이버 {0}의 기본 데이터 집합 형식을 확인할 수 없습니다.
odaconsumer.InvalidDataSetType=잘못된 데이터 집합 형식 {0}입니다. 데이터 집합 {1}의 기본 데이터 집합 형식을 확인할 수 없습니다.
odaconsumer.ParameterNameCannotBeEmptyOrNull=매개 변수 이름은 비워 두거나 널(null)이 될 수 없습니다.
odaconsumer.ParameterPositionIsOneBased=매개 변수 위치는 1 미만이 될 수 없습니다.
odaconsumer.CannotGetParameterTypeName=매개 변수의 형식 이름을 가져올 수 없습니다: {0}
odaconsumer.CannotGetParameterMode=매개 변수의 모드를 가져올 수 없습니다: {0}
odaconsumer.CannotGetParameterScale=매개 변수의 배율을 가져올 수 없습니다: {0}
odaconsumer.CannotGetParameterPrecision=매개 변수의 정밀도를 가져올 수 없습니다: {0}
odaconsumer.CannotGetParameterIsNullable=매개 변수의 널(null) 여부를 확인할 수 있습니다: {0}
odaconsumer.SameParamNameForDifferentHints=데이터 집합에 있는 다른 매개 변수가 동일한 이름 {0}(으)로 정의되었습니다.
odaconsumer.DifferentParamNameForSamePosition=데이터 집합에 있는 다른 매개 변수 {0}이(가) 동일한 위치 {1}(으)로 정의되었습니다.
odaconsumer.ParameterValueIsNull=매개 변수 값이 널(null)입니다.
odaconsumer.UnsupportedParameterValueType=지원되지 않는 매개 변수 값 형식입니다: {0}
odaconsumer.UnknownExceptionThrown=마지막 세터 호출에서 알 수 없는 예외가 발생했습니다.
odaconsumer.CannotConvertIndexedParameterValue=인덱스 {1}의 매개 변수 값 {0}을(를) {2} 값 형식에서 {3} ODA 형식으로 변환할 수 없습니다.
odaconsumer.CannotConvertNamedParameterValue=매개 변수 이름이 {1}인 매개 변수 값 {0}을(를) {2} 값 형식에서 {3} ODA 형식으로 변환할 수 없습니다.
odaconsumer.UnrecognizedProjectedColumnName=인식할 수 없는 예상 열 이름입니다: {0}
odaconsumer.ColumnNameOrAliasAlreadyUsed=새 열 이름 또는 별칭 {0}이(가) 인덱스 {1}의 열에서 이미 사용되고 있습니다.
odaconsumer.NamedResultSetsUnsupported=이 문은 명명된 결과 집합을 지원하지 않습니다.
odaconsumer.OutputParameterUnsupported=이 문은 출력 매개 변수를 지원하지 않습니다.
odaconsumer.CannotGetConnectionMetaData=연결 메타데이터를 가져올 수 없습니다.
odaconsumer.CannotGetMaxConnections=데이터 원본에 대한 최대 연결 수를 가져올 수 없습니다.
odaconsumer.CannotGetMaxStatements=데이터 원본에 대한 최대 쿼리 수를 가져올 수 없습니다.
odaconsumer.CannotGetDataSetMetaData=데이터 원본에서 데이터 집합 매개 변수 {0}을(를) 가져올 수 없습니다.
odaconsumer.CannotPrepareStatement=데이터 집합 형식 {1}에 대해 다음 쿼리를 준비하지 못했습니다. \n[{0}]
odaconsumer.CannotCloseConnection=연결을 닫을 수 없습니다.
odaconsumer.CannotOpenConnection=드라이버의 연결을 열 수 없습니다: {0}
odaconsumer.CannotDetermineSupportForMultipleOpenResults=복수의 공개 결과에 대한 지원을 확인할 수 없습니다.
odaconsumer.CannotDetermineSupportForMultipleResultSets=복수의 결과 집합에 대한 지원을 확인할 수 없습니다.
odaconsumer.CannotDetermineSupportForNamedResultSets=명명된 결과 집합에 대한 지원을 확인할 수 없습니다.
odaconsumer.CannotDetermineSupportForNamedParameters=명명된 매개 변수에 대한 지원을 확인할 수 없습니다.
odaconsumer.CannotDetermineSupportForInParameters=입력 매개 변수에 대한 지원을 확인할 수 없습니다.
odaconsumer.CannotDetermineSupportForOutParameters=출력 매개 변수에 대한 지원을 확인할 수 없습니다.
odaconsumer.CannotSetStatementProperty=문 속성을 설정할 수 없습니다.
odaconsumer.CannotSetSortSpec=정렬 사양을 설정할 수 없습니다.
odaconsumer.CannotSetMaxRows=최대 행을 설정할 수 없습니다.
odaconsumer.CannotGetResultSetMetaData=결과 집합 메타데이터를 가져올 수 없습니다.
odaconsumer.CannotGetMetaDataForNamedResultSet=명명된 결과 집합의 메타데이터를 가져올 수 없습니다: {0}
odaconsumer.CannotExecuteStatement=문을 실행할 수 없습니다.
odaconsumer.CannotGetResultSet=결과 집합을 가져올 수 없습니다.
odaconsumer.CannotGetNamedResultSet=명명된 결과 집합을 가져올 수 없습니다: {0}
odaconsumer.CannotFindOutParameter=출력 매개 변수를 찾을 수 없습니다: {0}
odaconsumer.CannotGetParameterType=매개 변수의 형식을 가져올 수 없습니다 {0}
odaconsumer.CannotCloseStatement=문을 닫을 수 없습니다.
odaconsumer.CannotGetColumnCount=열 수를 가져올 수 없습니다.
odaconsumer.CannotGetColumnName=열의 이름을 가져올 수 없습니다: {0}
odaconsumer.CannotGetColumnLabel=열의 레이블을 가져올 수 없습니다: {0}
odaconsumer.CannotGetColumnType=열의 형식을 가져올 수 없습니다: {0}
odaconsumer.CannotFetchNextRow=다음 데이터 행을 인출할 수 없습니다.
odaconsumer.CannotDetermineWasNull=마지막으로 검색한 값이 널(null)인지 여부를 확인할 수 없습니다.
odaconsumer.CannotGetIntFromColumn=열에서 정수 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetDoubleFromColumn=열에서 이중 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetStringFromColumn=열에서 문자열 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetBigDecimalFromColumn=열에서 10진수 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetDateFromColumn=열에서 날짜 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetTimeFromColumn=열에서 시간 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetTimestampFromColumn=타임스탬프 값 열을 가져올 수 없습니다: {0}
odaconsumer.CannotGetClobFromColumn=열에서 CLOB 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetBlobFromColumn=열에서 BLOB 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetRowPosition=현재 행 위치를 가져올 수 없습니다.
odaconsumer.CannotCloseResultSet=결과 집합을 닫을 수 없습니다.
odaconsumer.CannotGetParameterCount=매개 변수 수를 가져올 수 없습니다.
odaconsumer.CannotGetIntFromParameter=매개 변수에서 정수 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetDoubleFromParameter=매개 변수에서 이중 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetStringFromParameter=매개 변수에서 문자열 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetBigDecimalFromParameter=매개 변수에서 10진수 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetDateFromParameter=매개 변수에서 날짜 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetTimeFromParameter=매개 변수에서 시간 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetTimestampFromParameter=매개 변수에서 타임스탬프 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetBlobFromParameter=매개 변수에서 BLOB 값을 가져올 수 없습니다: {0}
odaconsumer.CannotGetClobFromParameter=매개 변수에서 CLOB 값을 가져올 수 없습니다: {0}
odaconsumer.CannotFindInParameter=입력 매개 변수를 찾을 수 없습니다: {0}
odaconsumer.CannotSetIntParameter=매개 변수의 정수 값을 설정할 수 없습니다: {0}
odaconsumer.CannotSetDoubleParameter=매개 변수의 이중 값을 설정할 수 없습니다: {0}
odaconsumer.CannotSetStringParameter=매개 변수의 문자열 값을 설정할 수 없습니다: {0}
odaconsumer.CannotSetBigDecimalParameter=매개 변수의 10진수 값을 설정할 수 없습니다: {0}
odaconsumer.CannotSetDateParameter=매개 변수의 날짜 값을 설정할 수 없습니다: {0}
odaconsumer.CannotSetTimeParameter=매개 변수의 시간 값을 설정할 수 없습니다: {0}
odaconsumer.CannotSetTimestampParameter=매개 변수의 타임스탬프 값을 설정할 수 없습니다: {0}
odaconsumer.CannotClearInParameters=입력 매개 변수 값을 지울 수 없습니다.
odaconsumer.UnrecognizedOdaType=인식할 수 없는 ODA 데이터 형식입니다: {0}
odaconsumer.CannotGetColumnNativeDataTypeName=열의 고유 데이터 형식 이름을 가져올 수 없습니다: {0}
odaconsumer.CannotGetParameterMetaData=ODA 드라이버 매개 변수 메타데이터를 가져올 수 없습니다.
odaconsumer.CannotLoadOdaAdapter={0} 드라이버를 로드할 수 없습니다. DTP-to-BIRT ODA 어댑터 플러그인 {1}이(가) 누락되었습니다.
odaconsumer.MustSpecifyColumnName=열 힌트를 추가할 때 열 이름을 지정해야 합니다.

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=executeQuery()를 호출한 후 결과 집합을 이름별로 가져올 수 없습니다.
odaconsumer.manager.CannotSetParamBeforePrepare=입력 매개 변수를 설정하려면 문을 준비해야 합니다.
odaconsumer.manager.CannotGetParamBeforeExecute=문을 실행하기 전에는 출력 매개 변수를 가져올 수 없습니다.
odaconsumer.manager.CannotGetParamMdBeforePrepare=매개 변수 메타데이터를 가져오려면 문을 준비해야 합니다.
odaconsumer.manager.CannotSetSortSpecBeforePrepare=정렬 사양을 설정하려면 문을 준비해야 합니다.
odaconsumer.manager.MaxConcurrentConnectionsReached=최대 활성 동시 연결 수에 도달했습니다.
odaconsumer.manager.ConnectionFailed=연결을 열 수 없습니다.
odaconsumer.manager.CannotConstructConnectionFactory=ODA 드라이버의 연결 공장을 만들 수 없습니다.
odaconsumer.manager.InadequatePermissionsForCompatibility=부적절한 사용 권한으로 인해 이전 버전과의 호환성을 제공할 수 없습니다.
odaconsumer.manager.ConnectionIsInactive=데이터 원본과 연결이 활성화되지 않았습니다.
odaconsumer.manager.MaxConcurrentStatementsReached=이 연결의 최대 활성 동시 연결 수에 도달했습니다.
odaconsumer.manager.PrepareFailed=준비하지 못했습니다.
odaconsumer.manager.CannotGetResultSetMdBeforePrepare=결과 집합 메타데이터를 가져오려면 문을 준비해야 합니다.
odaconsumer.manager.CannotExecuteBeforePrepare=문을 실행하려면 먼저 준비해야 합니다.
odaconsumer.CannotFindLogDirectory=로그 디렉터리를 찾을 수 없습니다.
