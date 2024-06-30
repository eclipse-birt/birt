#************************************************************************
# Copyright (c) 2004, 2010 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#
#************************************************************************
# Localized messages for the DTE Engine. Messages here give
# the text of Data Engine exceptions.

###########################################################
#for test only
Error.Msg001={0}이(가) {0}에 없습니다.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=데이터 원본이 정의되지 않았습니다: {0}.

data.engine.UndefinedDataSet=데이터 집합이 정의되지 않았습니다: {0}.

data.engine.UnsupportedDataSourceType=데이터 원본 "{0}"은(는) 지원되지 않는 형식입니다.

data.engine.UnsupportedDataSetType=데이터 집합 "{0}"은(는) 지원되지 않는 형식입니다.

data.engine.UnsupportedGroupOnClobAndBLOB=CLOB 또는 BLOB 데이터의 그룹 "{0}"은(는) 지원되지 않습니다.

data.engine.UnsupportedSortOnClobAndBLOB=CLOB 또는 BLOB 데이터의 정렬 "{0}"은(는) 지원되지 않습니다.

data.engine.EndOfGroupOnly=이 집계식은 그룹의 끝에서만 사용할 수 있습니다.

data.engine.InvalidAggregation=잘못된 집계 정의: {0}.

data.engine.InvalidAggrGroup=집계 함수에 대한 잘못된 그룹화 수준: {0}.

data.engine.WrongNumAggrArgs=집계 함수에 대한 잘못된 인수의 수: {0}.

data.engine.AggrParamGroupLevelInconsistent=중첩된 집계 "{0}"에서 상수가 아닌 식은 그룹 수준이 모두 동일해야 합니다.

data.engine.DupPropertyName=중복된 속성 이름: {0}.

data.engine.NoSubQueryName=하위 쿼리를 찾을 수 없습니다: {0}.

data.engine.DataSourceMissingExtId=데이터 원본 {0}의 정의에서 확장 ID가 누락되었습니다.

data.engine.NullGroupStart=그룹 시작 값은 널(null)일 수 없습니다.

data.engine.UnsupportedDirectNestedAggregate=집계를 직접 중첩하는 것은 지원되지 않습니다.

data.engine.ResultClosed=QueryResults 또는 반복자가 닫혔습니다.

data.engine.NoCurrentRow=Result iterator에 현재 행이 없습니다. 시작되지 않았거나 종료되었습니다.

data.engine.InvalidJSExpr=잘못된 Javascript 식: {0}.

data.engine.InvalidTotalFuncName=잘못된 합계 함수 이름: {0}.

data.engine.InvalidTotalFunc=잘못된 합계 함수

data.engine.NoAggrFunc=이 식에서 집계 함수 호출이 필요하지 않습니다.

data.engine.InvalidExprHandle=식 처리가 잘못되었습니다.

data.engine.InvalidAggrGroupExpression=집계 함수에 대한 잘못된 그룹 식: {0}.

data.engine.InvalidSortKeyColumn=잘못된 정렬 키 열: {0}.

data.engine.InvalidGroupLevel= 집계의 그룹 수준: {0}이(가) 잘못되었습니다.

data.engine.InvalidGroupKeyName=열 그룹화: {0}은(는) 지원되지 않는 작업입니다.

data.engine.BadCloseScript=CustomDataSet의 closeScript 계산에 실패했습니다.

data.engine.InvalidCustomFieldIdx=잘못된 사용자 지정 필드 인덱스: {0}.

data.engine.InvalidGroupName=그룹 이름이 잘못되었습니다.

data.engine.DuplicateGroupName=그룹 이름이 중복됩니다.

data.engine.BadOpenScript=CustomDataSet의 openScript 계산에 실패했습니다.

data.engine.DataSourceIsOpen=데이터 원본이 이미 열려 있습니다.

data.engine.DataSourceNotOpen=데이터 원본이 열려 있지 않습니다.

data.engine.EmptyCustomFieldName=사용자 지정 필드 이름이 비어 있지 않아야 합니다.

data.engine.DupCustomFieldName=중복된 사용자 지정 필드 이름: {0}.

data.engine.NoResultSet=쿼리 실행에서 결과 집합을 반환하지 않았습니다.

data.engine.NoResultMetadata=결과 메타데이터를 사용할 수 없습니다.

data.engine.QueryAlreadyPrepared=쿼리가 이미 준비되었습니다.

data.engine.QueryNotPrepared=쿼리가 아직 준비되지 않았습니다.

data.engine.InvalidFieldName=잘못된 필드 이름: {0}.

data.engine.DupColumnName=중복된 열 이름: {0}.

data.engine.DupAlias=중복된 열 이름 또는 별칭: {0}.

data.engine.InvalidFieldIndex=잘못된 필드 인덱스: {0}.

data.engine.fail.prepareExecution=데이터 집합 형식에 대해 다음 쿼리를 준비하지 못했습니다: {0}

data.engine.groupIncrementalUpdate.IllegalGroupOrderState=잘못된 그룹 업데이트 상태: 프로세스 그룹 <{1}>보다 현재 그룹 <{0}>이 큽니다.

data.engine.MatchError=패턴 구문을 확인하십시오. 문자열을 패턴과 일치시키는 도중 오류가 발생했습니다: {0}.

data.engine.ExpectBooleanReturnType=스크립트된 데이터 집합의 {0} 스크립트가 부울 결과를 반환해야 합니다.

data.engine.BadIntervalUnit=잘못된 간격 단위 값: {0}.

data.engine.BadGroupKeyType=그룹 키에 지원되지 않는 데이터 형식이 있습니다: {0}.

data.engine.group.interval=형식이 {1}인 열에서 {0} 간격 설정은 지원되지 않습니다.

data.engine.group.interval.Rowid=행 ID 열에서는 숫자 간격만 지원됩니다.

data.engine.group.interval.invalid=주어진 간격이 부적합합니다.

data.engine.group.intervalRange.invalid=잘못된 간격 범위: {0}.

data.engine.NoSupportFilterOnGroup=현재 그룹에서 필터링이 지원되지 않습니다.

data.engine.InvalidTopBottomArgument=위쪽/아래쪽 필터의 인수는 숫자이어야 합니다.

data.engine.InvalidTopBottomNArgument=TopN/BottomN 필터의 인수는 0 또는 양의 정수여야 합니다.

data.engine.InvalidTopBottomPercentArgument=TopPercent/BottomPercent 필터의 인수는 100보다 작거나 같은 양수여야 합니다.

data.engine.UnsupportedCondOp=조건부 연산자가 지원되지 않습니다: {0}.

data.engine.EmptyExpression=식은 널(null)이거나 비워 둘 수 없습니다.

data.engine.AggregationBinding.EmptyArgument=집계 바인딩 "{1}"에 인수 "{0}"이(가) 필요합니다.

data.engine.InvalidJoinType=잘못된 조인 형식입니다.

data.engine.InvalidJoinOperator=잘못된 조인 연산자입니다.

data.engine.BadDataSetParamName=정의되지 않은 데이터 집합 매개 변수: {0}.

data.engine.FailComputeOutputParameterValue=출력 매개 변수 값 계산에 실패했습니다: {0}.

data.engine.LinkedReportParamAllowMultiValues=복수 값을 허용하는 보고서 매개 변수 "{0}"은(는) 데이터 집합 매개 변수 "{1}"에 연결하는 데 사용할 수 없습니다.

#column binding
data.engine.EmptyBindingName=바인딩 이름은 널(null)이거나 비워 둘 수 없습니다.

data.engine.InvalidBoundColumnName=잘못된 바인딩된 열 이름: {0}.

data.engine.InvalidGroupKey={1} 그룹의 "{0}" 그룹 키를 하위 그룹에 정의된 열 바인딩에 종속시킬 수 없습니다.

data.engine.ColumnBindingNotExist=바인딩 "{0}"이(가) 없습니다.

data.engine.ColumnBindingCycle="{0}" 바인딩에 종속 사이클이 형성되어 있습니다.

data.engine.ColumnBindingReferToInexistColumn=열 바인딩 "{0}"이(가) 존재하지 않는 바인딩 "{1}"을(를) 참조합니다.

data.engine.ColumnBindingReferToInexistBinding=열 바인딩 "{0}"이(가) 존재하지 않는 데이터 집합 열 "{1}"을(를) 참조합니다.

data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=열 바인딩"{0}"이 잘못되었습니다:집계를 포함한 부모 쿼리 열 바인딩은 하위 쿼리 열 바인딩에 사용될 수 없습니다.

data.engine.ReadColumnValueFromDocumentError=열 값을 읽는 도중 오류가 발생했습니다: {0}. 바인딩 식 평가 시 런타임 오류(예: 0으로 나누기 오류)가 발생했기 때문일 수 있습니다.

data.engine.NoOuterResultsExist=외부 쿼리 결과가 없습니다. 부모 쿼리가 없는 쿼리에서 "_outer"를 참조했기 때문일 수 있습니다.

data.engine.DuplicatedBindingName=중복된 바인딩 이름: {0}

data.engine.InvalidAggrLevelKeyInSummaryQuery=집계: {0}이(가) 잘못된 그룹 이름을 참조합니다.
#computed column value
data.engine.BadCompColExpr=계산된 열 "{0}"에 대한 식이 잘못되었습니다.

data.engine.CompCol.FailRetrieveValueComputedColumn=계산된 열 "{0}"에 대한 값을 계산하지 못했습니다.

data.engine.ScriptResult.WrongSystemComputedColumn=정렬, 그룹화, 필터 또는 바인딩 식 계산에 실패했습니다. 식에 사용되는 데이터 필드가 데이터 집합에 없습니다.

data.engine.ComputedColumnCycle=계산 열 "{0}"에 종속 사이클이 형성되어 있습니다.

data.engine.SortOnAggregation=비그룹 집계 또는 집계 "{0}"을(를) 참조하는 계산 열에서 정렬은 지원되지 않습니다.

data.engine.BadFilterExpr=필터 식 "{0}"이(가) 잘못되었습니다.

data.engine.BadFilterDefn=식 "{0}" 기반 필터를 계산할 수 없습니다.

#clob and blob error
data.engine.opencloberror=CLOB 문자 스트림 열기 오류입니다.

data.engine.readcloberror=CLOB 문자 스트림 읽기 오류입니다.

data.engine.openbloberror=BLOB binaryStream 스트림 열기 오류입니다.

data.engine.readbloberror=BLOB binaryStream 스트림 읽기 오류입니다.

#resultsetcache exception
data.engine.BadDestIndex=destIndex의 값 {0}이(가) 잘못되었습니다. 0과 {1} 사이의 값이어야 합니다.

data.engine.WriteTempError=큰 데이터 집합에 대해 임시 파일을 쓰는 중 예외가 발생했습니다.

data.engine.ReadTempError=큰 데이터 집합에 대해 임시 파일을 읽는 중 예외가 발생했습니다.

#resultiterator cache exception
data.engine.CreateCacheTempError=결과 행 캐싱을 위한 임시 파일을 생성하는 중 예외 발생.

data.engine.OpenCacheTempError=결과 행 캐싱을 위한 임시 파일을 여는 중 예외 발생.

data.engine.WriteCacheTempError=결과 행 캐싱을 위한 임시 파일을 쓰는 중 예외 발생.

data.engine.ReadCacheTempError=결과 행 캐싱을 위한 임시 파일을 읽는 중 예외 발생.

data.engine.CloseCacheTempError=결과 행 캐싱을 위한 임시 파일을 닫는 중 예외 발생.

data.engine.FailedLoadColumnValue="{0}" 형식 열 "{1}"의 값을 로드하지 못했습니다.

data.engine.FailedLoadClass="{1}" 형식 열 "{2}"의 클래스 "{0}"을(를) 로드하지 못했습니다. 클래스 "{0}"이(가) BIRT 엔진의 클래스 로딩 경로에 있는지 확인하십시오.

#load data engine factory exception
load.factory.error=데이터 엔진 팩토리 로드 중 오류가 발생했습니다.

# Wrapped Error
data.engine.DataConversionError=데이터 형식을 변환하는 중 오류가 발생했습니다.

data.engine.BadCompareExpr="{0}"을(를) "{1}"과(와) 비교할 수 없습니다.

data.engine.BadCompareSingleValueWithMultiValues=[{0}]의 단일 값과 복수 값을 비교하는 잘못된 필터 조건으로 인해 필터를 적용할 수 없습니다.\n이 필터에 대해 하나의 값만 설정하십시오.

data.engine.BirtException=BIRT 예외가 발생했습니다. 자세한 내용은 다음 예외를 참조하십시오.

# data set cache error
data.engine.datasetcache.save.error=데이터 집합 캐시를 저장하는 중 오류가 발생했습니다.

data.engine.datasetcache.load.error=데이터 집합 캐시 파일을 로드하는 중 오류가 발생했습니다.

data.engine.exceed.max.data.object.row=쿼리를 처리할 수 없습니다. 쿼리를 실행하려면 결과 집합 버퍼 크기를 늘리거나 쿼리 범위를 좁히십시오.

# error related with data engine context
data.engine.InvalidMode=데이터 엔진 컨텍스트의 모드 값 {0}이(가) 잘못되었습니다.

data.engine.InvalidArchive=생성 또는 프리젠테이션 시, 보고서 보관이 널(null)일 수 없습니다.

data.engine.SaveReportDocumentError=저장 중 보고서 문서 오류가 발생했습니다: {0}.

data.engine.LoadReportDocumentError=로드 중 보고서 문서 오류가 발생했습니다: {0}.

data.engine.GetSaveStreamError=저장 스트림을 가져오는 중 보고서 문서 오류가 발생했습니다.

data.engine.GetLoadStreamError=로드 스트림을 가져오는 중 보고서 문서 오류가 발생했습니다.

data.engine.NullExpr=프리젠테이션에서 값을 검색할 수 있도록 식은 널(null) 값이 될 수 없습니다.

data.engine.InValidExpr=프리젠테이션에서 값을 검색할 식이 잘못되었습니다.

data.engine.BadGroupExpr=그룹 식을 입력해야 합니다.

data.engine.filterExprContainRowNum=필터 식에서 Java 스크립트 개체 "row.__num"을 참조할 수 없습니다.

data.engine.sortExprContainRowNum=정렬 식에서 Java 스크립트 개체 "row.__num"을 참조할 수 없습니다.

data.engine.ResultSetNotStart=결과 집합이 시작되지 않았습니다.

data.engine.GetLevelMemberError=이 데이터 바인딩이 사용 가능한 범위를 벗어나 사용되고 있습니다.

data.engine.invalidRowIndex=잘못된 행 인덱스 값: {0}.

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

date.engine.DuplicateAggregationName=중복된 집계 이름: {0}.

data.engine.InconsistentAggregationArgumentDefinition=집계 확장 구현에서 정의된 집계 인수가 plugin.xml에 정의된 인수와 일치하지 않습니다.

data.engine.incorrectGroupKeyValues=지정한 그룹 키 값 배열이 잘못되었습니다.

data.engine.NoExpressionProcessorAvailable=사용 가능한 ExpressionProcessor가 없습니다.

data.engine.InconvertibleDatatype=값 {0}의 원래 dataType은 지정된 dataType {2}(으)로 변환할 수 없는 {1}입니다.

data.engine.ConvertToDataTypeError={0}은(는) {1}(으)로 변환할 수 없습니다.

data.engine.NoRowUpdate=지금은 데이터 행을 업데이트할 수 없습니다.

data.engine.BadDataType=데이터 형식이 지원되지 않습니다{0}.

data.engine.NotSerializableClass=클래스 {0}은(는) 직렬화를 지원하지 않습니다.

data.engine.BadDataExpression=데이터 식은 널(null)이 아니어야 합니다.

data.engine.aggregation.InvalidBindingExpression=계산된 측정값은 집계 바인딩 "{0}"에서 사용할 수 없습니다.

data.engine.InvalidExpression=식 "{0}"이(가) 잘못되었습니다.

data.engine.BadFetchScriptReturnType=데이터 집합 스크립트 메서드 "{0}"에서 {1}이(가) 반환되었습니다. 부울 값이 필요합니다.

data.engine.FailToExecuteScript=함수 {0}()에서 스크립트를 실행하지 못했습니다. 소스:\n------\n" + {1} + "\n-----

data.engine.cacheFunctionWrongMode=캐시 기능은 DIRECT_PRESENTATION 모드용으로만 사용됩니다.

data.engine.document.invalidIndexArray=잘못된 인덱스 배열입니다.

data.engine.document.invalidGroupItem=제거한 그룹 항목이 잘못되었습니다.

data.engine.resultIteratorNotOpen=Result Iterator를 열 수 없습니다.

data.engine.resultIteratorClosed=Result Iterator가 닫혔습니다.

data.engine.invalidOutputParameterIndex=잘못된 출력 매개 변수 인덱스: {0}.

data.engine.outputParameterOutOfBound=출력 매개 변수 인덱스가 범위를 벗어났습니다: {0}.

data.engine.invalidOutputParameterName=잘못된 출력 매개 변수 이름: {0}.

data.engine.document.notSupportInPresentation=프리젠테이션에서 지원되지 않습니다.

data.engine.wrongStatus=잘못된 상태

data.engine.wrongVersion=2.1 이전 버전에서는 지원되지 않습니다.

data.engine.unsupportOperationException=지원되지 않는 연산

data.engine.possibleMixedDataTypeInColumn=데이터 모집단 중에 오류가 발생했습니다. 이는 "모든" 형식 열에 혼합된 형식의 데이터가 포함되어 발생했을 수 있습니다.

data.engine.resultClassSaveError=다음 열 참조를 찾을 수 없음: {0}.

data.engine.invalidMemBufferSize=메모리 버퍼 크기 "{0}"은(는) 잘못되었습니다. 메모리 버퍼 크기는 0보다 큰 정수여야 합니다.

data.engine.document.error.cannotLoadStream=문서 스트림 "{0}"은(는) 잘못되었습니다.

data.engine.failToCreateTempDir="{0}"에 임시 디렉터리를 만들지 못했습니다.

data.engine.reportItem.SubQuery.Unsupported=보고서 항목 공유는 하위 쿼리에 사용할 수 없습니다.

data.engine.emptyResultSet=결과 집합이 비어 있습니다.

data.engine.errorHappenWhenRetrieveResultSet=결과 집합 "{0}"을(를) 확인하는 중에 오류가 발생했습니다. 지정된 결과 집합이 존재하지 않을 수 있습니다.

data.engine.invalidAggregationBindingForPLSReport=페이지 수준 보안을 사용하는 경우 집계 바인딩 "{0}"을(를) 사용할 수 없습니다.

data.engine.acl.dataSetCannotBeAccessed=데이터 집합 "{0}"에 액세스할 수 없습니다.

data.engine.acl.dataSetColumnCannotBeAccessed=데이터 집합 열 "{0}"에 액세스할 수 없습니다.

data.engine.acl.cubeCannotBeAccessed=사용자에게 이 큐브 "{0}"에 대한 액세스 권한이 없습니다.

data.engine.acl.cubeLevelCannotBeAccessed=사용자에게 이 수준 "{0}"에 대한 액세스 권한이 없습니다.

data.engine.acl.cubeMeasureCannotBeAccessed=사용자에게 이 측정값 "{0}"에 대한 액세스 권한이 없습니다.

data.engine.acl.cubeDimensionCannotBeAccessed=사용자는 이 차원 "{0}"에 액세스할 권한이 없습니다.
# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=열 이름은 비워 두거나 널(null)이 될 수 없습니다.

odaconsumer.ColumnPositionIsOneBased=열 위치는 1-기반이며 1 미만이 될 수 없습니다.

odaconsumer.ColumnAliasCannotBeEmpty=열 별칭은 빈 문자열이 될 수 없습니다.

odaconsumer.CannotProcessDriverConfig={0} 드라이버의 데이터 원본 확장 구성을 찾을 수 없거나 처리할 수 없습니다.

odaconsumer.OdaInitEntryNotFound=드라이버 {0}의 초기 진입점을 찾을 수 없거나 인스턴스화할 수 없습니다.

odaconsumer.OdaDriverOnUnsupportedPlatform=드라이버 {0}이(가) {1}을(를) 지원하지 않습니다.

odaconsumer.CannotGenerateUrl=클래스 로더의 URL {0}을(를) 생성할 수 없습니다.

odaconsumer.UnsupportedNativeType=드라이버 {2}의 데이터 집합 형식 {1}에 대해 지원되지 않는 고유 형식 {0}입니다.

odaconsumer.UnsupportedDataSetType=드라이버 {1}이(가) 데이터 집합 형식 {0}을 지원하지 않습니다.

odaconsumer.CannotDetermineDefaultDataSetType=널(null) 데이터 집합 형식입니다. 드라이버 {0}의 기본 데이터 집합 형식을 확인할 수 없습니다.

odaconsumer.InvalidDataSetType=잘못된 데이터 집합 형식 {0}입니다. 데이터 집합 {1}의 기본 데이터 집합 형식을 확인할 수 없습니다.

odaconsumer.ParameterNameCannotBeEmptyOrNull=매개 변수 이름은 비워 두거나 널(null)이 될 수 없습니다.

odaconsumer.ParameterPositionIsOneBased=매개 변수 위치는 1 미만이 될 수 없습니다.

odaconsumer.CannotGetParameterNativeName=드라이버 정의 매개 변수 이름을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetParameterType=매개 변수의 형식을 가져올 수 없습니다 {0}.

odaconsumer.CannotGetParameterTypeName=매개 변수의 형식 이름을 가져올 수 없습니다 {0}.

odaconsumer.CannotGetParameterMode=매개 변수의 모드를 가져올 수 없습니다: {0}.

odaconsumer.CannotGetParameterScale=매개 변수의 배율을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetParameterPrecision=매개 변수의 정밀도를 가져올 수 없습니다: {0}.

odaconsumer.CannotGetParameterIsNullable=매개 변수의 널(null) 여부를 확인할 수 있습니다: {0}.

odaconsumer.SameParamNameForDifferentHints=데이터 집합에 있는 다른 매개 변수가 동일한 이름 {0}(으)로 정의되었습니다.

odaconsumer.DifferentParamNameForSamePosition=데이터 집합에 있는 다른 매개 변수 {0}이(가) 동일한 위치 {1}(으)로 정의되었습니다.

odaconsumer.ParameterValueIsNull=매개 변수 값이 널(null)입니다.

odaconsumer.UnsupportedParameterValueType=지원되지 않는 매개 변수 값 형식: {0}.

odaconsumer.UnknownExceptionThrown=마지막 세터 호출에서 알 수 없는 예외가 발생했습니다.

odaconsumer.CannotConvertIndexedParameterValue=인덱스 {1}의 매개 변수 값 {0}을(를) {2} 값 형식에서 {3} ODA 형식으로 변환할 수 없습니다.

odaconsumer.CannotConvertNamedParameterValue=매개 변수 이름이 {1}인 매개 변수 값 {0}을(를) {2} 값 형식에서 {3} ODA 형식으로 변환할 수 없습니다.

odaconsumer.UnrecognizedProjectedColumnName=인식할 수 없는 예상 열 이름입니다: {0}

odaconsumer.ColumnNameOrAliasAlreadyUsed=새 열 이름 또는 별칭 {0}이(가) 인덱스 {1}의 열에서 이미 사용되고 있습니다.

odaconsumer.NamedResultSetsUnsupported=이 문은 명명된 결과 집합을 지원하지 않습니다.

odaconsumer.OutputParameterUnsupported=이 문은 출력 매개 변수를 지원하지 않습니다.

odaconsumer.UnsupportedMultipleResultSets=이 문은 여러 개의 결과 집합을 지원하지 않습니다.

odaconsumer.CannotGetConnectionMetaData=연결 메타데이터를 가져올 수 없습니다.

odaconsumer.CannotGetMaxConnections=데이터 원본에 대한 최대 연결 수를 가져올 수 없습니다.

odaconsumer.CannotGetMaxStatements=데이터 원본에 대한 최대 쿼리 수를 가져올 수 없습니다.

odaconsumer.CannotGetDataSetMetaData=데이터 원본에서 데이터 집합 매개 변수 {0}을(를) 가져올 수 없습니다.

odaconsumer.CannotPrepareStatement=데이터 집합 형식({1})에 대해 다음 쿼리를 준비하지 못했습니다. \n[{0}]

odaconsumer.CannotCloseConnection=연결을 닫을 수 없습니다.

odaconsumer.CannotOpenConnection=드라이버의 연결을 열 수 없습니다: {0}.

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

odaconsumer.CannotGetMetaDataForNamedResultSet=명명된 결과 집합의 메타데이터를 가져올 수 없습니다: {0}.

odaconsumer.CannotExecuteStatement=문을 실행할 수 없습니다.

odaconsumer.CannotGetResultSet=결과 집합을 가져올 수 없습니다.

odaconsumer.CannotGetNamedResultSet=명명된 결과 집합을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetMoreResults=문에서 결과 집합을 더 이상 가져올 수 없습니다.

odaconsumer.CannotFindOutParameter=출력 매개 변수를 찾을 수 없습니다: {0}.

odaconsumer.CannotCloseStatement=문을 닫을 수 없습니다.

odaconsumer.CannotCancelStatement=문을 취소할 수 없습니다.

odaconsumer.CannotGetColumnCount=열 수를 가져올 수 없습니다.

odaconsumer.CannotGetColumnName=열의 이름을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetColumnLabel=열의 레이블을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetColumnType=열의 형식을 가져올 수 없습니다: {0}.

odaconsumer.CannotFetchNextRow=다음 데이터 행을 인출할 수 없습니다.

odaconsumer.CannotDetermineWasNull=마지막으로 검색한 값이 널(null)인지 여부를 확인할 수 없습니다.

odaconsumer.CannotGetIntFromColumn=열에서 정수 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetDoubleFromColumn=열에서 실수 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetStringFromColumn=열에서 문자열 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetBigDecimalFromColumn=열에서 10진수 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetDateFromColumn=열에서 날짜 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetTimeFromColumn=열에서 시간 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetTimestampFromColumn=타임스탬프 값 열을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetClobFromColumn=열에서 CLOB 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetBlobFromColumn=열에서 BLOB 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetBooleanFromColumn=열에서 부울 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetObjectFromColumn=열에서 개체 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetRowPosition=현재 행 위치를 가져올 수 없습니다.

odaconsumer.CannotCloseResultSet=결과 집합을 닫을 수 없습니다.

odaconsumer.CannotGetParameterCount=매개 변수 수를 가져올 수 없습니다.

odaconsumer.CannotGetIntFromParameter=매개 변수에서 정수 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetDoubleFromParameter=매개 변수에서 실수 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetStringFromParameter=매개 변수에서 문자열 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetBigDecimalFromParameter=매개 변수에서 10진수 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetDateFromParameter=매개 변수에서 날짜 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetTimeFromParameter=매개 변수에서 시간 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetTimestampFromParameter=매개 변수에서 타임스탬프 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetBlobFromParameter=매개 변수에서 BLOB 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetClobFromParameter=매개 변수에서 CLOB 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetBooleanFromParameter=매개 변수에서 부울 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotGetObjectFromParameter=매개 변수에서 개체 값을 가져올 수 없습니다: {0}.

odaconsumer.CannotFindInParameter=입력 매개 변수를 찾을 수 없습니다: {0}.

odaconsumer.CannotSetIntParameter=매개 변수 {1}에 대한 정수 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetDoubleParameter=매개 변수 {1}에 대한 실수 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetStringParameter=매개 변수 {1}에 대한 문자열 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetBigDecimalParameter=매개 변수 {1}에 대한 10진수 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetDateParameter=매개 변수 {1}에 대한 날짜 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetTimeParameter=매개 변수 {1}에 대한 시간 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetTimestampParameter=매개 변수 {1}에 대한 타임스탬프 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetBooleanParameter=매개 변수 {1}에 대한 부울 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetObjectParameter=매개 변수 {1}에 대한 개체 값({0})을 설정할 수 없습니다.

odaconsumer.CannotSetNullParameter=매개 변수 {0}에 대한 널(null) 값을 설정할 수 없습니다.

odaconsumer.CannotClearInParameters=입력 매개 변수 값을 지울 수 없습니다.

odaconsumer.UnrecognizedOdaType=인식할 수 없는 ODA 데이터 형식입니다: {0}

odaconsumer.CannotGetColumnNativeDataTypeName=열의 고유 데이터 형식 이름을 가져올 수 없습니다: {0}

odaconsumer.CannotGetParameterMetaData=ODA 드라이버 매개 변수 메타데이터를 가져올 수 없습니다.

odaconsumer.CannotLoadOdaAdapter={0} 드라이버를 로드할 수 없습니다. DTP-to-BIRT ODA 어댑터 플러그인 {1}이(가) 누락되었습니다.

odaconsumer.MustSpecifyColumnName=열 힌트를 추가할 때 열 이름을 지정해야 합니다.

odaconsumer.CannotFindLogDirectory=로그 디렉터리를 찾을 수 없습니다.

odaconsumer.ParameterNames=\n모델 이름({0}) 및 드라이버 정의 이름({1})으로 정의된 매개 변수.

odaconsumer.InvalidArgument=메서드({0})가 잘못된 인수({1})로 호출되었습니다.

odaconsumer.CannotSetConnectionLocale=연결 로케일({0})을 설정할 수 없습니다.

odaconsumer.CannotCheckOpenConnection=연결이 이미 열려 있는지 여부를 확인할 수 없습니다.

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


# Data Olap error code
data.olap.OlapDirCreateFail=디렉터리 {0}을(를) 만들지 못했습니다.

data.olap.OlapFileCreateFail=파일 {0}을(를) 만들지 못했습니다.

data.olap.OlapFileNotFound=OLAP 파일 {0}을(를) 찾을 수 없습니다.

data.olap.OlapFileFormatInvalid=파일 형식이 잘못되었습니다. 데이터 개체 이름: {0}.

data.olap.DimensionNotExist=차원 {0}이(가) 없습니다.

data.olap.KeyValueCanNotBeNull=키 열의 값은 널(null)이 될 수 없습니다. 키 열: {0}.

data.olap.DetailMemberHaveMultiParent=세부 구성원 {0}의 상위 항목이 두 개 이상입니다.

data.olap.OlapFileDataError=데이터 개체 {0}에 잘못된 데이터가 있습니다.

data.olap.DocumentObjectAlreadyExists =문서 개체가 이미 있습니다. 데이터 개체 {0}을(를) 만들지 못했습니다.

data.olap.DocumentObjectNotExists =문서로부터 큐브 "{0}"을(를) 로드하지 못했습니다. 문서 개체가 없습니다.

data.olap.FacttableRowNotDistinct=행은 팩트 테이블에서 고유해야 합니다. "{0}"이(가) 여러 개 있습니다.

data.olap.FacttableJointColumnNotExist=조인 열 "{0}"이(가) 차원에 없습니다.

data.olap.ParameterColumnOfAggregationNotExist=매개 변수 열 "{0}"이(가) 차원에 없습니다.

data.olap.FacttableNullMeasureValue=측정 열의 값이 널(null)입니다. "{0}".

data.olap.InvalidDimensionPositionFacttableRow=큐브 "{0}"의 팩트 테이블에 잘못된 데이터 행이 있습니다. 여기서 차원 키 "{1}"의 값은 "{2}"이며, 이 값은 차원 "{3}"에 더 이상 없습니다.

data.olap.measureNameNotFound=측정값 이름 "{0}"을(를) 찾을 수 없습니다.

data.olap.levelNameNotFound=수준 이름 "{0}"을(를) 찾을 수 없습니다.

data.olap.dimensionNameNotFound=큐브 쿼리에서 "{0}" 차원을 찾을 수 없습니다. 이 차원이 없거나 유효하지 않습니다.

data.olap.ReferencedDimensionNotFound=참조되는 차원 정의:{0}이(가) 없습니다.

data.olap.ReferencedLevelNotFound=참조되는 수준이 없습니다.

data.olap.cursorPositionError=커서가 대상 위치로 이동하지 못합니다.

data.olap.cursorSeekError=커서가 검색 위치로 이동하지 못합니다.

data.olap.CannotGetMeasureValue=측정 "{0}"의 정의를 큐브에서 찾을 수 없습니다. 이 측정이 없거나 유효하지 않습니다.

data.olap.CannotFindLevel=차원 "{1}"에서 "{0}" 수준 정의를 찾을 수 없습니다.

data.olap.tryToAddIdenticalAggrGroup=바인딩 "{1}" 목록에 대한 집계에 값 "{0}"이(가) 이미 있습니다.

data.olap.NoEdgeDefinitionFound=행 가장자리나 열 가장자리를 포함하지 않기 때문에 쿼리 실행에 실패했습니다. 최소한 하나의 가장자리가 정의되어야 합니다.

data.olap.invalidSortDefinition=정렬 정의가 잘못되었습니다.

data.olap.invalidMeasureRef="{0}"은(는) 올바르지 않은 측정값 참조 식입니다.

data.olap.invalidLevelAttr=다음 특성이 잘못되었습니다.

data.olap.referencedBindingNotExist=참조되는 바인딩 "{0}"이(가) 없습니다!

data.olap.invalidBindingReferToInexitDim=바인딩 "{0}"이(가) 잘못된 수준을 참조합니다.

data.olap.invalidBindingReferToInexitMeasure=바인딩 "{0}"이(가) 잘못된 측정값을 참조합니다.

data.olap.invalidBindingMissingAggrFunc=바인딩 "{0}"의 집계 함수가 없습니다.

data.olap.invalidAggrArgument=수준 특성만 집계 인수로 사용될 수 있습니다.

data.olap.axis.level.CantBeNull=열 구성원의 수준은 널(null)이 될 수 없습니다!

data.olap.axis.value.CantBeNull=수준 \"{0}\"의 열 구성원 값은 널(null)이 될 수 없습니다!

data.olap.fetchlimitWarning=반입 크기는 {0}(으)로, 전체 결과 집합 [{1}]의 길이보다 작습니다.

data.olap.failComputeComputedMeasureValue=계산되는 측정에 대한 값을 계산하지 못했습니다.

data.olap.duplicateMeasureName=중복된 측정값 이름 \"{0}\"입니다.

data.olap.dataBindingExpressionEmpty=데이터 바인딩 식은 널(null)이거나 비워 둘 수 없습니다!

data.olap.missingParentCursor=상위 결과 집합이 없습니다.

data.olap.CannotAccessNullDimensionRow=널(null) 차원 행에 액세스할 수 없습니다!

data.olap.NonexistentDimension=존재하지 않는 차원:

data.olap.NonexistentLevel=존재하지 않는 수준:

data.olap.NonexistentKeyOrAttr=존재하지 않는 키 또는 특성:

data.olap.UnsupportedFunction=지원되지 않는 함수:

data.olap.UnspecifiedBindingName=바인딩 이름이 지정되지 않았습니다.

data.olap.NotNestAggregationBinding=큐브 중첩 집계 바인딩이 아닙니다: {0}

data.olap.InvalidNestAggregationExpression=큐브 중첩 집계의 식이 잘못되었습니다.

data.olap.InvalidNestAggregationOn=해당 기본 집계에 없는 수준의 집계로 인해 중첩 집계 "{0}"을(를) 계산하지 못했습니다.

data.olap.InvalidAggregationFilterExpr=필터 식"{0}"이(가) 잘못되었습니다. 참조되는 차원 수준 "{2}"이(가) "{1}"을(를) 바인딩하는 집계의 aggregationOn 목록에 포함되어 있지 않습니다.

data.olap.sort.exprError=참조되는 차원 수준 집합에는 1개의 수준만 포함되어야 합니다!

data.olap.query.noCubeBinding=사용 가능한 큐브 바인딩이 없어 큐브 쿼리를 실행하지 못했습니다!

data.olap.FailLoadCube=큐브 "{0}"을(를) 로드하지 못했습니다.

data.olap.MissingDimensionInCube=큐브에 하나 이상의 차원이 있어야 합니다: "{0}".

data.olap.ExceedIntegerLimit= 결과 집합 길이가 Integer.MAX 값을 초과했습니다.

data.olap.ExceedColumnLimit= 열 가장자리 결과 집합 길이가 크기 제한({0})을 초과했습니다.

data.olap.ExceedRowLimit= 행 가장자리 결과 집합 길이가 크기 제한({0})을 초과했습니다.

data.cache.UnsupportedIncrementalCacheMode=증가 캐시의 \"{0}\" 모드는 아직 지원되지 않습니다.
# Configuration file parser error code
data.executor.configFileParser.getModeByID=데이터 집합 이름으로 증가 캐시를 가져오는 데 실패했습니다.

data.executor.configFileParser.getTSFormatByID=구성 파일의 지정된 데이터 집합 이름으로 타임스탬프 형식을 구문 분석하는 데 실패했습니다.

data.executor.configFileParser.getTimeStampColumnByID=구성 파일의 지정된 데이터 집합 이름으로 타임스탬프 형식을 구문 분석하는 데 실패했습니다.

data.executor.configFileParser.getQueryTextByID=구성 파일의 지정된 데이터 집합 이름으로 쿼리 텍스트를 가져오는 데 실패했습니다.

data.executor.configFileParser.getParametersByID=구성 파일의 지정된 데이터 집합 이름으로 매개 변수를 가져오는 데 실패했습니다.

data.aggregation.expression.displayName=식

data.aggregation.argumentError=집계 \"{1}\"에 대한 인수 \"{0}\"의 식이 널(null)이거나 비어 있습니다!

data.olap.InvalidAggrTypeOnMeasure=큐브 측정값의 집계는 "{0}" 유형이 될 수 없습니다.

#derived dataSet
data.derivedDataSet.columnNotExist=열 \"{0}\"이(가) 없습니다!

data.derivedDataSet.UnionQueryTextParseError=통합 쿼리를 구문 분석하지 못했습니다. \"{0}\"

data.derivedDataSet.JoinQueryTextParseError=조인 쿼리를 구문 분석하지 못했습니다. \"{0}\"

data.derivedDataSet.illegalClassInstance=초기화할 클래스 인스턴스가 잘못되었습니다.

data.derivedDataSet.classLoadError=클래스 인스턴스를 로드하지 못했습니다.

data.derivedDataSet.calculatorNotExist=계산기가 정의되지 않았거나 찾을 수 없습니다.

data.derivedDataSet.invalidQueryText=잘못된 쿼리 텍스트입니다.

#data mart data source
data.datamart.failLoadDataSet = 데이터 개체 집합 "{0}"을(를) 로드하지 못했습니다.

data.datamart.originalDataSetNotExist = 증분 업데이트에 실패했습니다. 원본 데이터 집합 "{0}"이(가) 없습니다.

data.datamart.incrementalDatasetMetaDataError = 증분 업데이트에 실패했습니다. 추가 데이터 집합 "{0}"의 메타데이터가 원본 데이터 집합의 메타데이터와 다릅니다.

data.datamart.collation=이전 제품에서는 정렬을 설정했더라도 데이터 개체 인덱스에 정렬이 사용되고 있지 않습니다. 이제 정렬이 지원되므로, 데이터 집합에서 정렬이 설정될 때 데이터 개체를 다시 생성해야 합니다.

data.filter.pushDownFail= ODA 확장 측에서만 지원되는 필터를 아래로 밀어내지 못했습니다.

data.query.exprReferInvalidColumn=쿼리에 잘못된 데이터 집합 열 "{0}"을(를) 참조하는 잘못된 식이 포함되어 있어 쿼리를 실행할 수 없습니다.

data.query.invalidColumnBinding=열 바인딩 "{0}"이(가) 잘못되었습니다.