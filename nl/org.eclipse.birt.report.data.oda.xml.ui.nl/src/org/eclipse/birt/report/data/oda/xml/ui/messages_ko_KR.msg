# wizard dialog messages
wizard.title.selectFolder=XML 데이터 원본
wizard.defaultMessage.selectFolder=XML 파일과 스키마 정보의 URL을 정의하십시오.
wizard.title.newDataSet=새 데이터 집합
wizard.title.selectXmlFile=미리 볼 XML 데이터 선택
wizard.defaultMessage.selectFile=이 세션에서 데이터 미리 보기에 사용할 예제 XML 소스를 선택합니다.
wizard.title.xPathChoose=테이블 매핑 정의
wizard.defaultMessage.selectXPath=XML 구조에서 요소를 선택하고 행 매핑에 이를 추가하여 테이블로 사용할 요소를 정의합니다. XML 데이터 집합당 하나의 테이블 매핑만 수행할 수 있습니다.
wizard.title.selectColumn=열 매핑 정의
wizard.title.defineColumnMapping=XML 구조에서 요소 또는 특성을 선택하고 열 매핑에 이를 추가하여 열 매핑을 정의합니다. 

#preference
designer.preview.preference.numberOfElementPassedToGetSchema=XML 스키마 트리 구문 분석 시 액세스하는 요소 수
designer.preview.preference.numberOfElementPassedToGetSchema.errormessage=0과 {0} 사이에 있는 숫자를 입력하십시오.
designer.preview.preference.columnMappingPage.group.title=열 매핑 페이지

#table mapping page messages
xPathChoosePage.messages.rowMapping=테이블 매핑
xPathChoosePage.messages.xPathExpression=XPath 식(&X)
xPathChoosePage.messages.xmlStructure=XML 구조
xPathChoosePage.messages.xmlColumnMapping=열 매핑

#sample xml data file page messages
xmlDataSetSelectionPage.messages.lineofdata=원본 데이터 선
xmlDataSetSelectionPage.warning.errorReadXMLFile=파일[{0}]에서 XML 데이터를 읽는 도중 오류가 발생했습니다\n{1}. 계속하시겠습니까?

#column mapping page messages
button.refresh=새로 고침
menu.menuItem.removeAll=모두 제거
menu.menuItem.remove=제거
lable.selectXmlFile=XML 소스의 URL을 입력하거나 소스 데이터가 포함된 파일을 찾습니다(&X).
lable.selectXmlSchmaFile=XML 스키마의 URL을 입력하거나 스키마가 포함된 파일을\n 찾습니다. 사용 가능한 스키마가 없는 경우 아무 것도 입력하지 않습니다(&M).
label.preview=먼저 미리 보기(&P)
label.useXMLFileFromDataSource=데이터 원본에 정의된 XML 파일 사용
RowMappingDialog.title=XPath 식을 선택하거나 편집합니다.
RowMappingDialog.info=선택한 노드에 대해 미리 정의된 XPath 식 중 하나를 선택하거나\n 다른 식을 입력합니다.
ColumnMappingDialog.info.columnName=열 이름:
ColumnMappingDialog.info.xPath=XPath 식:
ColumnMappingDialog.info.dataType=데이터 형식:
ColumnMappingDialog.info.dataPreview=테이블 미리 보기
ColumnMappingDialog.prompt.new=여기에 매핑 새로 만들기...
ColumnMappingDialog.prompt.maxRow=원본 데이터가 500줄 이상입니다. 이 페이지에는 처음 500줄만 표시됩니다.
ColumnMappingDialog.prompt.dialog.title=결과 미리 보기

dataset.editor.columnName=이름
dataset.editor.xpathexpression=XPath
dataset.editor.datatype=형식
dataset.editor.rowelement=행 요소
dataset.error.populateXMLTree=XML 데이터 원본을 검색할 수 없습니다. XML 데이터 원본 파일이 잘못되었거나 파일이 없습니다.

error.label=오류
error.selectFolder=선택한 파일 URL이 잘못되었거나 파일이 없습니다.
error.emptyPath=파일 URL이 비어 있습니다.
error.xpath.canNotContainAttribute=XPath 식이 XML 특성으로 매핑할 수 없습니다.
error.dataset.maxRowNumberError=최대 행은 숫자여야 합니다.
error.columnMapping.noMappingColumn=적어도 하나의 열을 매핑하십시오.
error.file.null=URL이 모두 비어 있습니다. 데이터 원본을 선택하지 않은 것입니다. 계속하시겠습니까?
error.columnMapping.sameColumnName=열 이름 [{0}]이(가) 이미 있습니다. 다른 이름을 사용하십시오.
error.invalidXpath=XPath 식"{0}"이(가) 잘못되었습니다.

#data types for column mapping
datatypes.dateTime=타임스탬프
datatypes.decimal=큰 십진수
datatypes.float=이중 실선
datatypes.integer=정수
datatypes.string=문자열
datatypes.date=날짜
datatypes.time=시간

#column name in column mapping table
columnName.name=이름
columnName.xPathName=XPath
columnName.typeName=형식

dataset.new=새 데이터 집합
file.choose=찾아보기(&B)...
title.warning=경고

datasource.propertyPage.tooltip=XML 데이터 원본
