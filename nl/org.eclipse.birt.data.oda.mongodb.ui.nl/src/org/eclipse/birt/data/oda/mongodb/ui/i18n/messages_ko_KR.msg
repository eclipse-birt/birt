##************************************************************************
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#  
#*************************************************************************
#
MongoDBDataSourceWizardPage.message.default=Mongo 데이터베이스 데이터 원본 연결 속성

MongoDBDataSourceWizardPage.GroupTitle.URIElements=Mongo 데이터베이스 URI 요소

MongoDBDataSourceWizardPage.text.label.ServerHost=서버 호스트(&H):

MongoDBDataSourceWizardPage.text.label.ServerPort=서버 포트(&O):

MongoDBDataSourceWizardPage.text.label.DatabaseName=데이터베이스 이름(&D):

MongoDBDataSourceWizardPage.text.label.UserName=사용자 이름(&S):

MongoDBDataSourceWizardPage.text.label.Password=암호(&W):

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=Mongo 데이터베이스 URI(&M):

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=추가 클라이언트 설정(&C)

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=소켓 연결 유지(&A)

MongoDBDataSourceWizardPage.checkbox.label.RequestSession=일관된 요청 세션(&R)

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=필수 연결 속성이 없습니다.

#
MongoDBDataSetWizardPage.label.DBName=Mongo 데이터베이스(&G):

MongoDBDataSetWizardPage.label.DBCollection=MongoDB 컬렉션(&C)

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=시스템 컬렉션 포함(&I)

MongoDBDataSetWizardPage.Button.Refresh=새로 고침(&R)

MongoDBDataSetWizardPage.label.DocumentNumber=필드를 검색할 문서 수(&M):

MongoDBDataSetWizardPage.group.text=명령 연산(&O)

MongoDBDataSetWizardPage.label.OperationType=명령 형식(&T):

MongoDBDataSetWizardPage.Button.FindFields=필드 찾기(&D)

MongoDBDataSetWizardPage.Button.text.CommandExpression=식(&E)...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=명령 식을 편집합니다.

MongoDBDataSetWizardPage.Button.QueryExpression=$쿼리 식(&Q)...

MongoDBDataSetWizardPage.Button.SortExpression=정렬 식(&S)...

MongoDBDataSetWizardPage.Button.AdvancedSettings=고급 설정(&V)...

MongoDBDataSetWizardPage.label.AvailableFields=사용 가능한 필드(&A):

MongoDBDataSetWizardPage.label.SelectedFields=선택한 필드(&S):

MongoDBDataSetWizardPage.HeaderText.SelectedFields=필드 표시 이름

MongoDBDataSetWizardPage.menuItem.remove=제거(&M)

MongoDBDataSetWizardPage.menuItem.removeAll=모두 제거(&L)

MongoDBDataSetWizardPage.message.default=데이터 집합 열에 대해 원하는 필드를 선택합니다.

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=계속하려면 확인

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=다른 컬렉션을 선택하면 현재 선택한 필드가 취소됩니다.  계속하시겠습니까?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=새로 고치려면 확인

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=변경된 명령 식을 기반으로 사용 가능한 필드를 새로 고치시겠습니까?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=오류

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=MongoDB 데이터 원본에서 컬렉션 목록을 가져오지 못했습니다.

MongoDBDataSetWizardPage.ExceptionDialog.title=오류

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=사용 가능한 필드를 찾을 수 없습니다.

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=활성 데이터 원본에 연결되지 않았습니다.

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=MongoDB 데이터 집합을 업데이트하는 중에 오류가 발생했습니다.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=잘못된 명령 식입니다.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=잘못된 쿼리 식입니다.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=잘못된 정렬 식입니다.

MongoDBDataSetWizardPage.message.error.MissingCollectionName=컬렉션 이름을 입력해야 합니다.

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=필드를 찾으려면 문서 번호의 정수 값을 입력하십시오.

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=선택한 필드 "{0}"이(가) 없습니다.

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=명령 식은 비워둘 수 없습니다.

MongoDBDataSetWizardPage.message.error.MissingSelectedField=필드를 하나 이상 선택해야 합니다.

MongoDBAdvancedSettingsDialog.dialogTitle=고급 데이터 집합 런타임 속성

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=일괄 처리의 최대 결과 개체 수(&B):

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=MongoDB 커서를 통해 한 번의 일괄 처리에\n 반입되는 최대 결과 개체 수입니다.  기본값 = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=메타데이터를 검색할 최대 문서 수(&M):

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=실행 시 쿼리 결과 집합 메타데이터를 가져오기 위해\n 처리할 문서 수입니다.  기본값 = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=건너뛸 최대 문서 수(&K):

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=결과 집합에서 최초 n개의 문서를 건너 뜁니다. 여기서 n은 정수 데이터 형식입니다.\n기본값 = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=힌트 식 인덱스(&H):

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=쿼리 최적화를 위해 쿼리가 사용해야 하는 하나 이상의 인덱스 키입니다.\n Mongo JSON 힌트 식 구문을 사용하여 인덱스된 키를 나열합니다.

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=쿼리 커서 읽기 기본 설정

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=모드(&D):

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=쿼리 커서 작업의 읽기 기본 설정 모드입니다.

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=태그 집합(&G):

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=구성원 노드를 위해 구성된 필수 태그 집합입니다.\n  배열에서 대체 태그 집합이 문서로 지정될 수 있습니다.

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=커서 시간 제한 없음(&N)

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=일반적으로 과도한 메모리 사용을 막기 위해 10분간 작업하지 않으면 서버 시간이 초과되어 커서가 유휴 상태로 설정됩니다.\n 이 옵션을 활성화하면 이를 방지할 수 있습니다.

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=일부 결과 허용(&P)

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=(Mongo) 샤딩을 함께 사용하면 샤드가 클러스터에서 중지/누락되어도 샤딩 처리된 시스템에서\n 일부 결과가 허용됩니다.  샤딩이 활성화되지 않으면 Mongo 서버에서 오류가 반환됩니다.

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=중첩 컬렉션 결합(&F)

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=중첩 컬렉션 필드를 다수의 데이터 집합 행에 결합하는 사용자 옵션입니다.

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects="최대 결과 개체 수"의 정수 값이 필요합니다!

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch="검색할 최대 문서 수"의 정수 값이 필요합니다!

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip="건너뛸 최대 문서 수"의 정수 값이 필요합니다!

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=디자인 설정 재설정

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=확인

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message="필드를 검색할 문서 수"의 디자인 타임 값이 "메타데이터를 검색할 최대 문서 수" 런타임 속성 값과 일치하도록 재설정했습니다.

MongoDBExpressionBuilder.dialogTitle.default=연산식 빌더

MongoDBExpressionBuilder.ExprssionText.label=식 텍스트(&X):

MongoDBExpressionBuilder.Button.import=가져오기(&M)...

MongoDBExpressionBuilder.Button.tooltip.import=로컬 파일에서 식 텍스트 가져오기

MongoDBExpressionBuilder.Button.export=내보내기(&E)...

MongoDBExpressionBuilder.Button.tooltip.export=로컬 파일로 명령 식 내보내기

MongoDBExpressionBuilder.Button.ValidateSyntax=구문 확인(&V)

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=명령 식 텍스트의 구문을 확인합니다.

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=식 텍스트 확인

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=식 구문을 확인합니다.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=잘못된 식 구문입니다.

MongoDBExpressionBuilder.CommandExpression.DialogTitle=명령 식 빌더

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=식 텍스트는 비어 있으면 안 됩니다!

MongoDBExpressionBuilder.QueryExpression.DialogTitle=쿼리 식 빌더

MongoDBExpressionBuilder.SortExpression.DialogTitle=정렬 식 빌더





