#/*******************************************************************************
# * Copyright (c) 2006, 2012 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v2.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-2.0.html
# *
# * Contributors:
# *  Actuate Corporation - initial API and implementation
# *******************************************************************************/

#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

###########################################################
#AbstractDataSetWizard properties
AbstractDataSetWizard.ModelTrans.Create=데이터 집합의 이름, 원본 및 형식을 정의하십시오.
###########################################################
#CssErrDialog
CssErrDialog.shellTitle.ImportCssStyleMssageTitle=메시지 가져오기

CssErrDialog.AreaTitle=CSS 스타일 메시지 가져오기

CssErrDialog.AreaMessage=CSS 스타일을 가져오는 도중 오류 또는 경고가 발생했습니다.

CssErrDialog.Severity=심각도:

CssErrDialog.Message=메시지:

CssErrDialog.FatalError=치명적인 오류

CssErrDialog.Error=오류

CssErrDialog.Warning=경고

###########################################################
#AddGroupAction properties
AddGroupAction.actionMsg.addGroup=그룹 삽입...

AddGroupAction.Menu.Text=그룹 삽입...

AddGroupAction.stackMsg.addGroup=그룹 삽입

###########################################################
#AddLibraryAction properties
AddLibraryAction.Error.FileNotFound=파일 ''{0}''이(가) 없습니다.

AddLibraryAction.Error.FileIsNotLibrary=''{0}''파일이 ''{1}''(으)로 끝나지 않습니다.

AddLibraryAction.Error.Message=파일이 이미 있습니다.

AddLibraryAction.Error.Title=오류

AddLibraryAction.Text=라이브러리 추가...

AddLibraryAction.Dialog.Tilte=라이브러리 선택

###########################################################
#AddCSSAction properties
AddCSSAction.Text=CSS 파일 추가...

###########################################################
#AddResourceAction properties
AddResourceAction.Text=리소스 추가(&R)...

AddResourceAction.Dialog.Tilte=리소스 선택

AddResourceAction.Error.Message=파일이 이미 있습니다.

AddResourceAction.Error.FileNotFound=파일 ''{0}''이(가) 없습니다.

AddResourceAction.Error.Title=오류

AddResourceAction.Error.FileIsNotResource=파일''{0}"은(는) 리소스가 아닙니다.

###########################################################
#AddCSSDialog
AddCSSWizardDialog.ShellText=CSS 파일 추가

AddCSSWizardDialog.TitleArea=CSS 파일을 리소스 폴더에 추가

AddCSSWizardDialog.Message=스타일을 리소스 폴더로 가져올 CSS 파일 선택

###########################################################
#MoveResourceDialog
MoveResourceDialog.Title=리소스 이동

MoveResourceDialog.Message=폴더 선택

###########################################################
#NewLibraryDialog
NewLibraryDialog.Title=새 라이브러리

NewLibraryDialog.Message=폴더 선택

NewLibraryDialog.label.NewLibrary=새 라이브러리 이름

###########################################################
#PublishCSSAction properties
PublishCSSAction.wizard.errorTitle=오류

PublishCSSAction.wizard.message=CSS 파일이 들어 있는 디렉터리에는 게시할 수 없습니다. 게시할 다른 디렉터리를 선택하십시오.

PublishCSSAction.wizard.msgDirErr=대상 디렉터리가 없지만 만들 수 없습니다.

PublishCSSAction.wizard.message.SourceFileNotExist=원본 파일이 없습니다.

PublishCSSAction.wizard.notvalidfolder=기본 설정에 정의된 리소스 폴더가 올바르지 않습니다.


###########################################################
#ResourceAction properties
DeleteLibraryAction.Text=삭제(&D)

CopyLibraryAction.Text=복사(&C)

PasteLibraryAction.Text=붙여넣기(&P)

MoveLibraryAction.Text=이동(&V)...

RenameLibraryAction.Text=이름 바꾸기(&M)

NewFolderAction.Text=폴더(&F)

NewLibraryAction.Text=라이브러리(&L)

NewResource.MenuGroup.Text=새로 만들기(&N)

RenameResourceAction.nameExists=같은 이름의 파일 또는 폴더가 이미 있습니다.

RenameResourceAction.inputDialogTitle=리소스 이름 바꾸기

RenameResourceAction.inputDialogMessage=새 이름:

NewFolderAction.nameExists=같은 이름의 폴더가 이미 있습니다.

NewFolderAction.emptyName=이름은 비워 둘 수 없습니다.

NewFolderAction.inputDialogTitle=새 폴더

NewFolderAction.inputDialogMessage=폴더 이름:

DeleteResourceAction.Dialog.Title=리소스 삭제

DeleteResourceAction.Dialog.Message=선택한 리소스를 삭제하시겠습니까?

PasteResourceAction.Dialog.Title=리소스 붙여넣기

PasteResourceAction.ReadOnlyEncounter.Title=읽기 전용 파일 발생

PasteResourceAction.ReadOnlyEncounter.Message=''{0}'' 파일은 읽기 전용입니다. 먼저 파일에 쓸 수 있는지 확인하십시오.

PasteResourceAction.Dialog.Message=리소스가 있습니다. 새 이름(&N) 지정:

PasteResourceAction.Dialog.FilenameSuffix.CopyOf=복사본

MoveResourceAction.Dialog.Title=리소스 이동

MoveResourceAction.Dialog.Message=리소스가 이미 있습니다. 바꾸시겠습니까?

ResourceAction.ConvertToFile.URLIsNull=URL이 널(null)이므로 파일로 변환할 수 없습니다.

ResourceAction.FileRemoveFailure=파일 제거에 실패했습니다.

LibraryNotExist.Dialog.Title=라이브러리 열기

LibraryNotExist.Dialog.Message=라이브러리가 없습니다. 리소스 탐색기를 새로 고치시겠습니까?

DesignerNotExist.Dialog.Title=보고서 디자인 열기

DesignerNotExist.Dialog.Message=보고서 디자인이 없습니다. 리소스 탐색기를 새로 고치시겠습니까?
###########################################################
#LibraryFileFilterAction properties
LibraryFileFilterAction.Text=필터...

###########################################################
#AddStyleRuleAction properties
AddStyleAction.actionMsg.addStyleRule=새 스타일(&N)...

AddStyleAction.stackMsg.addStyle=새 스타일(&N)

###########################################################
#ApplyStyleAction properties
ApplyStyleAction.actionLabel.none=없음

ApplyStyleAction.stackMsg.applyStyle=스타일 적용

###########################################################
#ApplyThemeAction properties
ApplyThemeAction.actionLabel.none=없음

ApplyThemeAction.stackMsg.applyStyle=테마 적용

###########################################################
#AttributesBuilder properties
AttributesBuilder.Label.Cell=속성 편집기 - 셀

AttributesBuilder.Label.Column=속성 편집기 - 열

AttributesBuilder.Label.Data=속성 편집기 - 데이터

AttributesBuilder.Label.Generic=속성 편집기 - {0}

AttributesBuilder.Label.Grid=속성 편집기 - 눈금

AttributesBuilder.Label.Label=속성 편집기 - 레이블

AttributesBuilder.Label.Library=속성 편집기 - 라이브러리

AttributesBuilder.Label.List=속성 편집기 - 목록

AttributesBuilder.Label.Cube=속성 편집기 - 큐브

AttributesBuilder.Label.DynamicFilterParameter=속성 편집기 - 동적 필터 매개 변수

AttributesBuilder.Label.MasterPage=속성 편집기 - 마스터 페이지

AttributesBuilder.Label.None=속성 편집기 - 없음

AttributesBuilder.Label.Picture=속성 편집기 - 이미지

AttributesBuilder.Label.Report=속성 편집기 - 보고서

AttributesBuilder.Label.Row=속성 편집기 - 행

AttributesBuilder.Label.Table=속성 편집기 - 테이블

AttributesBuilder.Label.Text=속성 편집기 - 텍스트

AttributesBuilder.Label.TextData=속성 편집기 - 동적 텍스트

AttributesBuilder.Label.DataSourceHandle=속성 편집기 - 데이터 원본

AttributesBuilder.Label.VariableElementHandle=속성 편집기 - 변수

AttributesBuilder.Label.DataSetHandle=속성 편집기 - 데이터 집합

AttributesBuilder.Label.ScalarParameterHandle=속성 편집기 - 매개 변수

AttributesBuilder.Label.TemplateReportItemHandle=속성 편집기 - 템플릿 보고서 항목

AttributesBuilder.Label.AutoText=속성 편집기- AutoText

AttributesBuilder.Label.ParameterGroup=속성 편집기 - 매개 변수 그룹

AttributesBuilder.Label.CascadingParameterGroup=속성 편집기 - 계단식 매개 변수 그룹

###########################################################
#AttributeView properties
AttributeView.dialg.Message.Warning=경고

AttributeView.dialg.Message.Yes=예(&E)

AttributeView.dialg.Message.No=아니오(&O)

AttributeView.dialg.Message.Cancel=취소(&A)

AttributeView.dialg.Message.PromptMsg=로컬 속성을 모두 지우시겠습니까?

AttributeView.toolbar.tooltip.EditingToggleAction.ClearMsg=로컬 속성을 지웁니다.

AttributeView.toolbar.tooltip.EditingToggleAction.EnableEditingMsg=속성 편집을 사용합니다.

AttributeView.toolbar.tooltip.EditingToggleAction.DisabledActionMsg=로컬 요소에 이 작업을 사용할 수 없습니다.

AttributeView.view.message.Emptypage= 이 {0}이(가) 라이브러리 {2}의 {1}에서 확장됩니다. 이 보고서 디자인에서 이 {3}의 속성을 수정하려면, 속성 편집기의 도구 모음에서 사용 단추를 누르십시오.

AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg=원래의 라이브러리 항목에서 속성 복원

AttributeView.defaultText.noAttributeView = 속성 편집기를 사용할 수 없습니다.
###########################################################
#BackGroundPreferencePage properties
BackGroundPreferencePage.displayname.Title=배경

###########################################################
#BasAttributePage properties
BasAttributePage.Label.Attributes=속성:

BaseAttributePage.CommandStack.ResetStyle.Message=기본 스타일 복원

###########################################################
#BaseInsertMenuAction properties
BaseInsertMenuAction.stackMsg.insertElement=요소 삽입



###########################################################
#BasePaletteFactory properties
BasePaletteFactory.AutoText.Confidential=기밀

BasePaletteFactory.AutoTextLabel.CreatedBy=만든 사람

BasePaletteFactory.AutoTextLabel.CreatedOn=만든 날짜:

BasePaletteFactory.AutoTextLabel.Filename=파일 이름:

BasePaletteFactory.AutoTextLabel.LastPrinted=최종 인쇄:

BasePaletteFactory.AutoTextLabel.Page=페이지:

BasePaletteFactory.AutoTextLabel.PageCount=페이지 수:

BasePaletteFactory.AutoTextLabel.PageXofY=X/Y 페이지

BasePaletteFactory.categoryName.PointerSelect=포인터 선택

BasePaletteFactory.categoryName.RectangleSelect=사각형 선택

BasePaletteFactory.Group.Items=항목

BasePaletteFactory.toolTip.PointerSelect=항목을 눌러 선택하거나 Ctrl 키를 누른 상태에서 여러 항목을 눌러 선택합니다.

BasePaletteFactory.toolTip.RectangleSelect=누르고 끌어 사각형 내 모든 항목을 선택합니다.

###########################################################
#BaseStylePreferencePage properties
BaseStylePreferencePage.displayname.Title=일반

###########################################################
#BgImageFieldEditor properties
BgImageFieldEditor.displayname.Browse=찾아보기(&W)...

###########################################################
#BindingPage properties
BindingPage.Dataset.Label=데이터 집합(&T):

BindingPage.DataSetBindingSelector.Title.DataSet=데이터 집합 바인딩

BindingPage.DataSetBindingSelector.Title.LinkModel=데이터 모델 바인딩

BindingPage.ReportItem.Label=보고서 항목(&I):

BindingPage.ReportItem.NoName=이름 없음

BindingPage.Label.Binding=바인딩

BindingPage.MessageDlg.SaveParamBinding=ParamBinding 저장

BindingPage.None=없음

BindingPage.TableColumn.DataType=데이터 형식

BindingPage.TableColumn.Parameter=매개 변수

BindingPage.TableColumn.Value=값

ColumnBingingDialog.Label.Warn=데이터 집합 또는 보고서 항목을 변경하면 모든 결과 집합 열 식이 무효가 될 수 있습니다.

ColumnBindingDialog.Label.DataSet=데이터 집합:

ColumnBindingDialog.Column.Expression=식

ColumnBindingDialog.DialogTitle=데이터 바인딩 선택

ColumnBindingDialog.Column.Name=이름

ColumnBindingDialog.Column.Filter=필터

ColumnBindingDialog.Column.Function=함수

ColumnBindingDialog.Column.DisplayName=표시 이름

ColumnBindingDialog.Column.DisplayNameID=표시 이름 ID

ColumnBindingDialog.InputPrompt=<여기에 새 값 입력...>

ColumnBindingDialog.Button.Generate=열 바인딩 생성

ColumnBindingDialog.Column.DataType=데이터 형식

ColumnBindingDialog.Column.AggregateOn=집계 대상

ColumnBindingDialog.All=모두

ColumnBindingDialog.NONE=없음

ColumnBindingDialog.Choice.DatasetFromContainer=컨테이너의 데이터 집합

ColumnBindingDialog.Choice.ReportItemFromContainer=컨테이너 참조 보고서 항목

ColumnBindingDialog.AGGREGATEON.NONE=N/A
ColumnBindingDialog.Text.Add=추가(&A)...

ColumnBindingDialog.Text.Edit=편집(&E)...

ColumnBindingDialog.Text.Del=제거(&R)

ColumnBindingDialog.Text.Refresh=새로 고침(&F)

ColumnBindingDialog.Text.AddAggr=집계 추가(&G)...

###########################################################
#BlockPreferencePage properties
BlockPreferencePage.displayname.Title=텍스트 블록

###########################################################
#BlockPreferencePage properties
SizePreferencePage.displayname.Title=크기

###########################################################
#BorderCanvas properties
BorderCanvas.Label.Inherited=(상속됨)

###########################################################
#BorderPreferencePage properties
BorderPreferencePage.displayname.Color=색

BorderPreferencePage.displayname.Style=스타일

BorderPreferencePage.displayname.Title=테두리

BorderPreferencePage.displayname.Width=너비

BorderPreferencePage.text.Bottom=아래쪽:

BorderPreferencePage.text.Left=왼쪽:

BorderPreferencePage.text.Right=오른쪽:

BorderPreferencePage.text.Top=위쪽:

###########################################################
#BordersPage properties
BordersPage.Label.Borders=테두리(&B):

BordersPage.Label.Color=색(&C):

BordersPage.Label.Presets=미리 설정:

BordersPage.Label.Style=스타일(&Y)

BordersPage.Label.Width=너비(&T):

BordersPage.Tooltip.All=모든 테두리

BordersPage.Tooltip.Bottom=아래쪽 테두리

BordersPage.Tooltip.Left=왼쪽 테두리

BordersPage.Tooltip.None=없음

BordersPage.Tooltip.Right=오른쪽 테두리

BordersPage.Tooltip.Top=위쪽 테두리

BordersPage.text.Preview=미리 보기:

BordersPage.Trans.SelectBorder=테두리 선택

BordersPage.Trans.UnSelectBorder=테두리 선택 취소

BordersPage.Trans.SelectAllborders=모든 테두리 선택

BordersPage.Trans.UnSelectAllborders=모든 테두리 선택 취소

###########################################################
#BoxPreferencePage properties
BoxPreferencePage.displayname.Title=상자

BoxPreferencePage.text.Margin=여백

BoxPreferencePage.text.Padding=안쪽 여백

###########################################################
#CommentsPreferencePage properties
CommentsPreferencePage.displayname.Title=설명

###########################################################
#ButtonPaneComposite properties
ButtonPaneComposite.tooltip.MasterPage=마스터 페이지

ButtonPaneComposite.tooltip.Normal=기본

ButtonPaneComposite.tooltip.Ruler=눈금자

###########################################################
#SettingsPage properties
SettingsPage.dataFetchSetting.label=데이터 원본에서 가져 올 최대 행 수(&M):

SettingsPage.CachePreference.button.clear=캐시 지우기(&C)

SettingsPage.CachePreference.checkBox.text=보고서 미리 보기용 데이터 집합 데이터 캐시(&H)

SettingsPage.CachePreference.description=캐시 크기 정의:

SettingsPage.CachePreference.Filter.Tooltip=데이터 집합 안이나 밖에서 캐시 사용

SettingsPage.CachePreference.label.text=행

SettingsPage.CachePreference.messageBox.info=캐시가 지워졌습니다.

SettingsPage.CachePreference.messageBox.title=캐시 지우기

SettingsPage.CachePreference.radioBox.entireNumText=전체 데이터 집합 캐시(&E)

SettingsPage.CachePreference.radioBox.numText=먼저 캐시 후 사용(&U)

###########################################################
#CascadingParametersDialog properties
CascadingParametersDialog.Button.isRequired=필요(&Q)

CascadingParametersDialog.default.preview.string=내 문자열

CascadingParametersDialog.display.controlType.comboBox=콤보 상자

CascadingParametersDialog.display.controlType.listBox=목록 상자

CascadingParametersDialog.Label.button.changeFormat=변경(&G)...

CascadingParametersDialog.Label.button.createNew.dataset=새로 생성(&W)...

CascadingParametersDialog.Label.cascadingParam.name=계단식 매개 변수 그룹 이름(&R):

CascadingParametersDialog.Label.column.displayText=표시 텍스트

CascadingParametersDialog.Label.column.dataSet=데이터 집합

CascadingParametersDialog.Label.column.name=이름

CascadingParametersDialog.Label.column.value=값

CascadingParametersDialog.Label.createNewParam=<새 매개변수 생성...>

CascadingParametersDialog.Label.dataSets=데이터 집합(&S):

CascadingParametersDialog.Label.dataType=데이터 형식(&Y)

CascadingParametersDialog.Label.defaultValue=기본값(&V):

CascadingParametersDialog.Label.displayType=표시 형식(&S):

CascadingParametersDialog.Label.formatAs=다음으로 형식 지정:

CascadingParametersDialog.Label.group.general=일반

CascadingParametersDialog.Label.group.moreOptions=추가 옵션

CascadingParametersDialog.Label.group.properties=속성

CascadingParametersDialog.Label.helpText=도움말 텍스트(&H)

CascadingParametersDialog.Label.listLimit=목록 제한(&L):

CascadingParametersDialog.Label.NoColumnAvailable=<사용 가능한 열 없음>

CascadingParametersDialog.Label.param.name=이름(&N):

CascadingParametersDialog.Label.parameters=매개 변수

CascadingParametersDialog.Label.preview=미리보기 형식

CascadingParametersDialog.Label.groupPromptText=프롬프트 텍스트(&P):

CascadingParametersDialog.Label.parameterPromptText=프롬프트 텍스트(&X):

CascadingParametersDialog.Label.selectDataSet=<데이터 집합 선택>

CascadingParametersDialog.Label.selectDisplayColumn=<표시 열이 설정되지 않음>

CascadingParametersDialog.Label.selectValueColumn=<값 열이 설정되지 않음>

CascadingParametersDialog.Label.values=값

CascadingParametersDialog.Radio.Single=단일 데이터 집합(&N)

CascadingParametersDialog.Radio.Mutli=다중 데이터 집합(&U)

CascadingParametersDialog.Choice.NullValue=널(null) 값

CascadingParametersDialog.Choice.BlankValue=빈 값

CascadingParametersDialog.Choice.SelectValue=<값 선택...>

CascadingParametersDialog.Button.Add=추가(&A)

CascadingParametersDialog.Button.Edit=편집(&I)

CascadingParametersDialog.Button.Delete=삭제(&T)

CascadingParametersDialog.Button.IsMultiple=여러 값 허용(&W)

CascadingParametersDialog.DefalutValue.Add=추가(&D)

CascadingParametersDialog.ExpressionEditor.Title=식 편집

CascadingParametersDialog.Title.AddCascadingParameter=계단식 매개 변수 그룹 추가

CascadingParametersDialog.Title.EditCascadingParameter=계단식 매개 변수 그룹 편집

CascadingParametersDialog.items.None=<없음>

###########################################################
#CellPageGenerator properties
CellPageGenerator.List.Borders=테두리

CellPageGenerator.List.CellPadding=안쪽 여백

CellPageGenerator.List.Font=글꼴

CellPageGenerator.List.General=일반


###########################################################
#ChoiceSetFactory properties
ChoiceSetFactory.choice.Auto=자동

ChoiceSetFactory.choice.None=없음

###########################################################
#CodePageAction properties
CodePageAction.actionText.code=스크립트

###########################################################
#ColorBuilder properties
ColorBuilder.text.Auto=자동

ColorBuilder.Button.ChooseColor=색상판 열기

ColorSelector.ColorDialog.Title=색

ColorSelector.Name=색상 선택기
###########################################################
#ColumnPageGenerator properties
ColumnPageGenerator.List.Borders=테두리

ColumnPageGenerator.List.CellPadding=셀 안쪽 여백

ColumnPageGenerator.List.Font=글꼴

ColumnPageGenerator.List.General=일반

ColumnPageGenerator.List.Visibility=표시 유형

###########################################################
#Commom properties
Commom.ShortDateTime.Fri=금요일

Commom.ShortDateTime.Mon=월요일

Commom.ShortDateTime.Sat=토요일

Commom.ShortDateTime.Sun=일요일

Commom.ShortDateTime.Thu=목요일

Commom.ShortDateTime.Tue=화요일

Commom.ShortDateTime.Wed=수요일

###########################################################
#CopyAction properties
CopyAction.text=복사(&C)

###########################################################
#CreatePlaceHolderAction properties
CreatePlaceHolderAction.text=템플릿 보고서 항목 생성(&A)...

###########################################################
#CutAction properties
CutAction.text=자르기(&t)

CutAction.trans=요소 자르기

###########################################################
#dataBinding properties
dataBinding.label.selectBinding=데이터 바인딩 선택:

dataBinding.message.changeDataSet=요소 데이터 바인딩을 변경하려고 합니다.  \n\n기존의 데이터 필드 정보를 모두 지우시겠습니까?

dataBinding.message.changeReference=요소 데이터 바인딩을 변경하려고 합니다. \n\n이 작업을 수행하시겠습니까?

dataBinding.title.changeDataSet=데이터 집합을 변경하시겠습니까?

dataBinding.title=데이터 바인딩

dataBinding.message.haveNoName=선택한 항목에 이름이 없습니다.\n\n항목을 선택하려면 이전 단계로 이동하여 항목 이름을 지정하십시오.

dataBinding.title.haveNoName=이름이 없습니까?

dataBinding.button.OK=확인(&O)

parameterBinding.title=데이터 집합 매개 변수 바인딩(&B)...

ParameterBindingDialog.title=데이터 집합 매개 변수 바인딩

dataBinding.title.generateOutputParam=바인딩 생성

dataBinding.msg.generateOutputParam=데이터 집합에 하나 이상의 출력 매개 변수가 있습니다.\n\n 이에 대한 바인딩을 생성하시겠습니까?

###########################################################
#DataEditPart properties
DataEditPart.Figure.Dafault=데이터

DataEditPart.Figure.Expression=식

DataEditPart.stackMsg.edit=데이터 바인딩 편집

DataEditPart.text.AggregateOn=집계 대상

###########################################################
#DataEditPart properties
DestroyEditPart.Message=이 보고서 항목의 내용이 손상되었습니다. 이 보고서 작업을 계속하려면 이 항목을 먼저 삭제해야 합니다. 그렇지 않으면 보고서 디자인 파일까지 손상되어 Report Designer에서 사용할 수 없게 됩니다.

###########################################################
#DataPageGenerator properties
DataPageGenerator.List.Bookmark=책갈피

DataPageGenerator.List.Borders=테두리

DataPageGenerator.List.Duplicates=중복

DataPageGenerator.List.Expression=식

DataPageGenerator.List.Format=형식

DataPageGenerator.List.formatDateTime=날짜 시간 형식

DataPageGenerator.List.formatNumber=숫자 형식

DataPageGenerator.List.formatString=문자열 형식

DataPageGenerator.List.General=일반

DataPageGenerator.List.Highlights=강조

DataPageGenerator.List.HyperLink=하이퍼링크

DataPageGenerator.List.Margin=여백

DataPageGenerator.List.Padding=안쪽 여백

DataPageGenerator.List.Section=페이지 나누기

DataPageGenerator.List.TOC=목차

DataPageGenerator.List.Visibility=표시 유형

###########################################################
#dataset properties
dataset.edit=데이터 집합 편집 - {0}

dataset.editor.settings=설정

dataset.editor.settings.dataFetchSetting.errorNumberFormat=행 인출 한계에 0보다 큰 정수를 입력하십시오.

dataset.editor.settings.dataFetchSetting.errorNumberFormatForResultSet=결과 집합 수에 양수를 입력하십시오.

dataset.editor.settings.dataFetchSetting=데이터 인출 설정

dataset.editor.settings.resultsetselection.resultSetSelection=결과 집합 선택

dataset.editor.settings.resultsetselection.enableResultSetSelection=결과 집합 선택 설정(&E)

dataset.editor.settings.resultsetselection.selectResultSetByName=이름별 결과 집합 선택(&R):

dataset.editor.settings.resultsetselection.selectResultSetByNumber=숫자별 결과 집합 선택(&S):

dataset.editor.settings.dataFetchSetting.fetchAll=데이터 원본에서 모든 행을 인출합니다(&A).

dataset.editor.settings.cachePreference.errorNumberFormat=캐시 행 크기에 0보다 큰 숫자를 입력하십시오.

dataset.editor.settings.cachePreference=캐시 기본 설정

dataset.editor.computedColumns=계산 열

dataset.editor.dataSource.Tooltip=데이터 원본 보기/변경

dataset.editor.dataSource=데이터 원본

dataset.editor.error.columnOrAliasNameAlreadyUsed=새 열 이름 또는 별칭({0})이 인덱스 {1}의 열에서 이미 사용되고 있습니다.

dataset.editor.error.computedColumnNameAlreadyUsed=새 계산 열 이름({0})이 출력 열({1})의 별칭으로 이미 사용되었습니다.

dataset.editor.error.duplicateParameterName=중복된 매개 변수 이름

dataset.editor.error.nonemptyDefaultvalue=기본값은 없을 수 없습니다.

dataset.editor.error.emptyName=이름은 비워 둘 수 없습니다.

dataset.editor.error.missingComputedColumnExpression=계산된 열 {0}의 식이 널(null)이거나 비어 있을 수 없습니다.

dataset.editor.error.missingComputedColumnName=계산된 열 이름이 널(null)이거나 비어 있을 수 없습니다.

dataset.editor.error.noDataSource=데이터 집합 {0}에 사용할 수 있는 데이터 원본이 없습니다. 속성 뷰의 "데이터 집합" 항목에서 데이터 원본을 선택하십시오.

dataset.editor.error.nonmatchedDataSource=데이터 집합 "{0}"에 일치하지 않는 데이터 원본이 있습니다. 데이터 집합 형식 "{1}"에 일치하는 데이터 원본을 선택하십시오.

dataset.editor.error.noEditorsFound=데이터 집합에 대한 편집기 없음 {0}.

dataset.editor.error.noInputParameterDefaultValue=입력 매개 변수 {0}에 비어 있지 않은 기본값이 있어야 합니다.

dataset.editor.error.title=경고

dataset.editor.error.unsupportedDataSet=지원되지 않는 데이터 집합 형식 - {0}.

dataset.editor.error.validationParameter=입력 매개 변수 [{0}]의 값이 비어 있습니다. 값을 그대로 비워두시겠습니까?

dataset.editor.filters=필터

dataset.editor.label.selectDataSource=데이터 원본 선택(&D):

dataset.editor.outputColumns=열 출력

dataset.editor.parameters=매개 변수

dataset.editor.preview=결과 미리 보기

dataset.editor.outputparameters=출력 매개 변수 미리 보기

dataset.editor.title.alias=별칭

dataset.editor.title.columnName=열 이름

dataset.editor.title.dataType=데이터 형식

dataset.editor.title.defaultValue=기본값

dataset.editor.title.direction=방향

DataSetParameterPage.editor.title.linkToSalarParameter=보고서 매개 변수에 연결되어 있음

dataset.editor.title.displayName=표시 이름

dataset.editor.title.displayNameKey=표시 이름 키

dataset.editor.title.expression=식

dataset.editor.title.aggrFunc=집계

dataset.editor.title.aggrArgu=인수(&R)

dataset.editor.title.filter=필터

dataset.editor.title.helpText=도움말 텍스트

dataset.editor.title.isInput=입력입니다.

dataset.editor.title.isNullable=널(null) 값 허용

dataset.editor.title.isOptional=옵션

dataset.editor.title.isOutput=출력입니다.

dataset.editor.title.name=이름

dataset.editor.title.nativeName=고유 이름

dataset.editor.title.operator=연산자

dataset.editor.title.type=형식

dataset.editor.title.value1=값 1

dataset.editor.title.value2=값 2

dataset.editor.inputDialog.displayName=표시 이름(&D)

dataset.editor.inputDialog.displayNameKey=표시 이름 키(&K)

dataset.editor.inputDialog.name=이름(&N)

dataset.editor.inputDialog.nativeName=고유 이름(&A)

dataset.editor.inputDialog.type=형식(&T)

dataset.editor.inputDialog.alias=별칭(&A)

dataset.editor.inputDialog.columnName=열 이름(&C)

dataset.editor.inputDialog.dataType=데이터 형식(&T):

dataset.editor.inputDialog.expression=식(&E)

dataset.editor.inputDialog.aggrArgu=인수(&R)

dataset.editor.inputDialog.aggrFunc=집계(&G)

dataset.editor.inputDialog.filter=필터(&F)

dataset.editor.inputDialog.value1=값 &1

dataset.editor.inputDialog.value2=값 &2

dataset.editor.inputDialog.operator=연산자(&O)

dataset.editor.inputDialog.defaultValue=기본값(&V):

dataset.editor.inputDialog.direction=방향(&D)

dataset.editor.historybar.backward=뒤로

dataset.editor.historybar.forward=정방향

dataset.editor.historybar.to=끝

dataset.error.noDataSources=사용할 수 있는 데이터 원본이 없습니다. 데이터 원본을 지금 바로 추가하시겠습니까?

dataset.error.dataSourceMissing=이 데이터 집합의 데이터 원본이 누락되었습니다.

dataset.error.title.noDataSources=데이터 원본을 사용할 수 없습니다.

dataset.message.create=새 데이터 집합을 생성하십시오.

dataset.new.defaultName=데이터 집합

dataset.new=새 데이터 집합

dataset.action.new=새 데이터 집합(&S)

dataset.join.new=새 조인 데이터 집합

dataset.action.join.new=새 조인 데이터 집합(&J)

dataset.resultset.preview.tooltip=쿼리 결과 미리 보기

dataset.resultset.preview.promptMessage.recordsNum=총 {0}개의 레코드가 표시되었습니다.

dataset.resultset.preview.promptMessage.MoreRecordsExist=결과에는 표시되지 않은 더 많은 레코드가 포함됩니다.

dataset.outputparameters.preview.tooltip=출력 매개 변수 미리 보기

dataset.wizard.label.datasetName=데이터 집합 이름(&M):

dataset.wizard.label.datasetType=데이터 집합 형식(&T):

dataset.wizard.label.dataSource=데이터 원본(&S):

dataSourceSelectionPage.title=데이터 원본에 연결

dataset.error.title.noReportDesign=오류

dataset.error.msg.noReportDesign=보고서 디자인을 사용할 수 없습니다.

dataset.warning.invalidReponseStatus=사용자 지정 ODA 페이지의 디자인 응답이 잘못되었습니다. ODA 디자인 세션 응답 상태: {0}.

###########################################################
#DataSetBasePage properties
DataSetBasePage.tooltip=이름은 비어 있으면 안 됩니다.

DataSetBasePage.ScriptedDataSet.name=스크립트된 데이터 집합

DataSetBasePage.ScriptedDataSource.name=스크립트 데이터 원본

DataSetBasePage.Group.DataSourceSelection=데이터 원본 선택

DataSetBasePage.CassandraScriptedDataSet.name=Cassandra 스크립트 데이터 집합

DataSetBasePage.CassandraScriptedDataSource.name=Cassandra 스크립트 데이터 원본

DataSetBindingSelector.Button.DeselectAll=모두 선택(&A)

DataSetBindingSelector.Button.SelectAll=모두 선택(&S)

DataSetBindingSelector.Combo.DataSet=데이터 집합(&D):

DataSetBindingSelector.Label.SelectBindingColumns=바인딩 열을 선택하십시오.

DataSetBindingSelectorPage.Label.SelectBindingColumns=바인딩 열을 선택하십시오.

DataSetBindingSelectorPage.Title=데이터 집합 바인딩

CassandraScriptedDataSource.display.name=Cassandra 스크립트 데이터 원본

CassandraScriptedDataSource.error.classNotFound=Cassandra 스크립트 데이터 원본을 지원하는 Hector 클라이언트 라이브러리 jar를 찾을 수 없습니다.

###########################################################
#JointDataSetPage properties
JointDataSetPage.pageName=조인 데이터 집합

JointDataSetPage.page.detail=조인을 정의하려면 양쪽에서 데이터 집합과 열을 선택합니다.

JointDataSetPage.button.innerJoin=내부 조인(&I)

JointDataSetPage.button.leftJoin=왼쪽 우선 외부 조인(&L)

JointDataSetPage.button.rightJoin=오른쪽 우선 외부 조인(&R)

JointDataSetPage.button.fullJoin=완전 외부 조인(&O)

JointDataSetPage.joinType=조인 형식

JointDataSetPage.error.nodataset.title=데이터 집합을 찾을 수 없습니다.

JointDataSetPage.error.nodataset.right=오른쪽 데이터 집합을 찾을 수 없습니다.

JointDataSetPage.error.nodataset.left=왼쪽 데이터 집합을 찾을 수 없습니다.

JointDataSetPage.pageDescription=조인 데이터 집합 정의

JointDataSetPage.query=쿼리

###########################################################
#DerivedDataSet properties
DerivedDataSet.action.error.message.NoDataSet=사용 가능한 데이터 집합을 찾을 수 없습니다.

DerivedDataSet.action.error.title.createDerivedDataSet=파생 데이터 집합을 생성할 수 없습니다.

DerivedDataSet.action.error.message.InvalidNumber.DataSet=사용 가능한 데이터 집합 수가 2보다 작습니다.

###########################################################
#DataSetComputedColumnsPage properties
DataSetComputedColumnsPage.ComputedColumns.Tooltip=계산된 열을 정의합니다.

DataSetComputedColumnsPage.description=계산된 열 정의.

DataSetComputedColumnsPage.duplicatedName=중복된 열 이름

DataSetComputedColumnsPage.numberName=숫자는 열 이름이 될 수 없습니다.

DataSetComputedColumnsPage.toolTipText.Down=계산 열을 아래로 이동

DataSetComputedColumnsPage.toolTipText.Up=계산 열을 위로 이동

DataSetComputedColumnsPage.toolTipText.Remove=계산 열 제거

DataSetComputedColumnsPage.toolTipText.New=계산 열 새로 만들기

DataSetComputedColumnsPage.toolTipText.Edit=계산 열 편집

DataSetComputedColumnsPage.InputDialog.title.New=계산 열 새로 만들기

DataSetComputedColumnsPage.InputDialog.title.Edit=계산 열 편집

DataSetComputedColumnsPage.InputDialog.message.error.AggregationExpression=집계 식이 집계 계산 열을 포함할 수 없습니다!

DataSetComputedColumnsPage.title.ComputedColumnInputDialog=계산 열

###########################################################
#DataSetFiltersPage properties
DataSetFiltersPage.description=필터 정의:

DataSetFiltersPage.exception.msg.operator.notSupport=다음 연산자는 지원되지 않습니다.

DataSetFiltersPage.Filter.Tooltip=BIRT 식을 사용하여 데이터 행을 필터링하십시오.

DataSetFiltersPage.toolTipText.Down=필터를 아래로 이동

DataSetFiltersPage.toolTipText.Up=필터를 위로 이동

DataSetFiltersPage.toolTipText.Remove=필터 제거

DataSetFiltersPage.toolTipText.New=필터 새로 만들기

DataSetFiltersPage.toolTipText.Edit=필터 편집

DataSetFiltersPage.title.FilterInputDialog=필터

DataSetFiltersPage.messages.selectVauleUnavailable=지정한 식으로 값 목록을 사용할 수 없습니다.

DataSetFiltersPage.DynamicFilter.DynamicParameter.NotFound=동적 필터의 연결된 동적 매개 변수 "{0}"이(가) 없습니다.

###########################################################
#DataSetNodeProvider properties
DataSetNodeProvider.menu.text=편집(&E)

###########################################################
#DataSetParametersPage properties
DataSetParametersPage.description=입력 매개 변수 정의:

DataSetParametersPage.Filter.Tooltip=데이터 집합 안이나 밖으로 매개 변수 전달.

DataSetParameterBindingInputDialog.Title=데이터 집합 매개 변수 바인딩 편집

DataSetParametersPage.ErrorTitle.InvalidInput=잘못된 입력.

DataSetParameterBindingInputDialog.Label.Name=매개 변수 이름:

DataSetParametersPage.ErrorMessage.CanootBeNull=값은 널(null)일 수 없습니다.

DataSetParameterBindingInputDialog.Title.NewParameter=새 매개 변수

DataSetParameterBindingInputDialog.Title.EditParameter=매개 변수 편집

DataSetParameterBindingInputDialog.Label.DataType=데이터 형식:

DataSetParameterBindingInputDialog.Label.Value=값(&V):

DataSetParametersPage.ParameterButton.Tooltip.Edit=보고서 매개 변수 편집

DataSetParametersPage.ParameterButton.Tooltip.New=새 보고서 매개 변수

DataSetParametersPage.Prompt.new=<여기에 새 매개 변수 입력>

DataSetParametersPage.default.None=N/A
DataSetParametersPage.reportParam.None=없음

DataSetParametersPage.errorMessage.LinkedReportParamNotFound=현재 데이터 집합 매개 변수 "{1}"의 연결된 보고서 매개 변수 "{0}"이(가) 없습니다.

DataSetParametersPage.errorMessage.InvalidType.LinkedReportParam=사용자가 보고서 매개 변수에 대해 여러 값을 설정하는 경우 데이터 집합 매개 변수는 첫 번째 값만 사용합니다.

DataSetParameterPage.cell.linkToSalarParameter=보고서 매개 변수에 연결됨(&L)

DataSetParameterPage.title.ParameterInputDialog=매개 변수

DataSetParameterPage.toolTipText.New=새 매개 변수

DataSetParameterPage.toolTipText.Edit=매개변수 편집

DataSetParameterPage.toolTipText.Remove=매개 변수 제거

DataSetParameterPage.toolTipText.Up=매개 변수를 위로 이동

DataSetParameterPage.toolTipText.Down=매개 변수를 아래로 이동

DataSetParameterPage.WarningMessage.updatePosition=일부 또는 모든 데이터 집합 매개 변수에 잘못된 위치값이 지정되었습니다. 순서를 기준으로 위치를 업데이트하고 있습니다.

DataSetParameterPage.warning.UnmatchedParamDataType=데이터 집합 매개 변수 형식이 연결된 보고서 매개 변수 형식과 일치하지 않습니다.

DataSetParameterPage.multiValuesType=여러 값 유형

DataSetParameterPage.warningLinkedMultipleValuesParams.title=경고 대화상자

DataSetParameterPage.updateReportParameter.title=보고서 매개 변수 업데이트 설정

DataSetParameterPage.updateReportParameter.message=데이터 집합 매개 변수 설정을 사용하여 연결된 보고서 매개 변수를 업데이트하시겠습니까?

DataSetParameterPage.updateReportParameter.promptButtonYes=예

DataSetParameterPage.updateReportParameter.promptButtonNo=아니오

DataSetParameterPage.updateReportParameter.propmtText=선택 저장

PromptParameterDialog.title=디자인 특성 업데이트 확인

PromptParameterDialog.okButton.text=적용

PromptParameterDialog.parameterGroup.title=매개 변수 디자인 특성

PromptParameterDialog.parameterGroup.label=편집된 데이터 집합 디자인에서 수정한 특성 값으로 선택한 매개 변수를 업데이트합니다.

PromptParameterDialog.parameterGroup.nameColumn=이름

PromptParameterDialog.parameterGroup.previousValue=이전 값

PromptParameterDialog.parameterGroup.revisedValue=수정된 값

###########################################################
#PropertyBindingpage properties
PropertyBindingPage.datasource.property.empty=이 데이터 원본의 ODA 확장명에 정의된 공용 속성이 없습니다.

PropertyBindingPage.dataset.property.empty=이 데이터 집합의 ODA 확장명에 정의된 공용 속성이 없습니다.

PropertyBindingPage.dataset.queryText=쿼리 텍스트(&T)

PropertyBindingPage.property.tooltip=런타임 속성 바인딩 정의

PropertyBindingPage.button.tooltip=식 빌더를 열어 {0}의 값을 입력하십시오.

PropertyBindingPage.label.colon=:
PropertyBindingPage.label.LeftParenthesis=(
PropertyBindingPage.label.RightParenthesis=)

###########################################################
#DataSetProcess properties
DataSetProcess.action.Delete=데이터 집합 삭제

DataSetProcess.action.Edit=데이터 집합 편집

DataSetProcess.action.Preview=미리 보기

DataSetProcess.text.DisplayName=데이터 집합

DataSetProcess.text.Prefix=다음 데이터 집합에서 이 데이터 원본을 사용합니다:

DataSetProcess.text.Show=데이터 집합에 대한 데이터 집합 편집기 페이지를 표시합니다.

DataSetProcess.text.Suffix=이 요소를 삭제하시겠습니까?

###########################################################
#datasource properties
datasource.action.edit=편집(&E)

datasource.edit=데이터 원본 편집 - {0}

datasource.new.defaultName=데이터 원본

datasource.new=새 데이터 원본

datasource.action.new=새 데이터 원본(&S)

datasource.wizard.label.dataSourceName=데이터 원본 이름(&M):

datasource.wizard.label.datasources=다음 목록에 있는 데이터 원본 형식으로 생성(&D)

datasource.wizard.label.connectionfile=프로필 저장소에 있는 연결 프로필로 생성(&C)

datasource.wizard.message.selectType=새 데이터 원본에 대해 모든 설정을 제공하거나 미리 정의된 연결 프로필을 선택합니다.

datasource.wizard.title.select=데이터 원본 형식을 선택하거나 연결 프로필을 선택합니다.

datasource.editor.property=속성 바인딩

datasource.editor.duplicatedName=데이터 원본 이름 "{0}"이(가) 중복됨

datasource.error.title.noReportDesign=오류

datasource.error.msg.noReportDesign=보고서 디자인을 사용할 수 없습니다.

datasource.exprotToCP.title=내보내기

datasource.exportToCP.message=데이터 원본 탐색기로 "{0}"을(를) 내보내고 있습니다.

datasource.exportToCP.checkBox=내보낸 데이터 원본에 대한 연결 프로필 저장소 생성(&C)

datasource.exportToCP.externalCheckBox=데이터 원본에 있는 외부화된 연결 프로필 사용(&U)

datasource.exportToCP.label.specifyName=연결 프로필 이름(&E):

datasource.exportToCP.errorBox.button.OK=확인

datasource.exportToCP.error.emptyFileName=비어 있지 않은 연결 프로필 이름을 지정해야 합니다!

datasource.exportToCP.error.invalidFileName=지정된 연결 프로필 이름에 잘못된 문자가 있습니다!

datasource.exportToCP.error.duplicatedFileName=연결 프로필 이름이 이미 있습니다!

datasource.exportToCP.error.alreadyExported=데이터 원본 "{0}"이(가) 이미 해당 속성을 외부화했습니다.


###########################################################
#DataSourceProcess properties
DataSourceProcess.action.Delete=데이터 원본 삭제

DataSourceProcess.action.Edit=데이터 원본 편집

DataSourceProcess.text.DisplayName=DataSource
DataSourceProcess.text.Prefix=다음 데이터 집합에서 이 데이터 원본을 사용합니다.

DataSourceProcess.text.Show=데이터 원본 편집기 페이지를 표시합니다.

DataSourceProcess.text.Suffix=이 요소를 삭제하시겠습니까?

###########################################################
#DataSourceSelectionPage properties
DataSourceSelectionPage.ScriptDataSource.DisplayName=스크립트된 데이터 원본

###########################################################
#DataSourcesSlotOutlineProcess properties
DataSourcesSlotOutlineProcess.action.New=새 데이터 원본

###########################################################
#datatypes properties
datatypes.any=모두

datatypes.auto=자동

datatypes.dateTime=날짜 시간

datatypes.decimal=십진수

datatypes.float=실수

datatypes.integer=정수

datatypes.string=문자열

###########################################################
#DataView properties
DataView.defaultText.noDataView=데이터 보기를 사용할 수 없습니다.

###########################################################
#DataViewTreeViewerPage properties
DataViewTreeViewerPage.tooltip.DoubleClickToEdit=편집하려면 두 번 클릭

DataViewTreeViewerPage.tooltip.DragToInsertColumn=열을 삽입하려면 끌기

DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns=모든 데이터 집합 열을 삽입하려면 끌기

DataViewTreeViewerPage.tooltip.DragToInsertParameter=보고서 매개 변수를 삽입하려면 끌기

###########################################################
#DebugUtil properties
DebugUtil.importProject.fail="기존 프로젝트를 가져오는 데 실패했습니다."

###########################################################
#DecorationFieldEditor properties
DecorationFieldEditor.text.Linethrough=가운데 줄

DecorationFieldEditor.text.Overline=윗줄

DecorationFieldEditor.text.Underline=밑줄

###########################################################
#DefaultNodeProvider properties
DefaultNodeProvider.Dlg.Confirm={0} 삭제를 계속하려면 '예'를 클릭하십시오. 그렇지 않은 경우 '아니요'를 클릭하십시오.

DefaultNodeProvider.ParameterGroup.ConfirmMessage=매개 변수 그룹을 삭제하려고 합니다. 매개 변수 그룹을 삭제할 경우 그룹 내 모든 매개 변수가 삭제됩니다. 계속하시겠습니까?

DefaultNodeProvider.ParameterGroup.ConfirmTitle=매개 변수 그룹 삭제 확인

DefaultNodeProvider.Tree.Body=본문

DefaultNodeProvider.Tree.Clients={0}은(는) 다음 요소에서 사용됩니다.

DefaultNodeProvider.Tree.ColumnHedings=열 머리글

DefaultNodeProvider.Tree.Components = 보고서 항목

DefaultNodeProvider.Tree.DataSets=데이터 집합

DefaultNodeProvider.Tree.DataSources=데이터 원본

DefaultNodeProvider.Tree.Detail=상세 정보

DefaultNodeProvider.Tree.Footer=바닥글

DefaultNodeProvider.Tree.Groups=그룹

DefaultNodeProvider.Tree.Header=머리글

DefaultNodeProvider.Tree.Images=내장된 이미지

DefaultNodeProvider.Tree.Invalid=잘못된 노드

DefaultNodeProvider.Tree.MasterPages=MasterPages
DefaultNodeProvider.Tree.PageSetup=페이지 설정

DefaultNodeProvider.Tree.Parameters=보고서 매개 변수

DefaultNodeProvider.Tree.Reference=경고

DefaultNodeProvider.Tree.Scratch=스크래치 패드

DefaultNodeProvider.Tree.Styles=스타일

DefaultNodeProvider.Tree.Themes=테마

DefaultNodeProvider.WarningDialog.Title=경고

DefaultNodeProvider.WarningDialog.EmptyList=삽입 가능한 요소가 없음

DefaultNodeProvider.stackMsg.changeBinding=데이터 바인딩 변경

###########################################################
#DeleteAction properties
DeleteAction.text=삭제(&D)

DeleteAction.trans=요소 삭제

###########################################################
#DeleteColumnAction properties
DeleteColumnAction.actionMsg.delete=삭제(&D)

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.actionMsg.deleteGroup=그룹 삭제(&G)

###########################################################
#DeleteListGroupAction properties
DeleteListGroupAction.actionMsg.deleteGroup=그룹 삭제(&G)

###########################################################
#DeleteRowAction properties
DeleteRowAction.actionMsg.delete=삭제(&D)

###########################################################
#designer properties
designer.preview.browserTitle=미리 보기

designer.preview.preference.browser.autoSave=미리 보기 전에 자동으로 저장합니다.

designer.preview.preference.browser.browse=찾아보기(&B)...

designer.preview.preference.browser.currentBrowsers=현재 외부 웹 브라우저 어댑터(&U):

designer.preview.preference.browser.description=이 선택으로 BIRT 보고서를 미리 보는 데 사용될 웹 브라우저가 결정됩니다.\n브라우저 어댑터가 하나만 구성된 경우 선택을 변경할 수\n없습니다.

designer.preview.preference.browser.details=브라우저 프로그램 선택

designer.preview.preference.browser.program=사용자 지정 브라우저 명령:(&S):

designer.preview.preference.browser.svg=SVG 차트 사용(&E)

designer.preview.preference.browser.extbrowser.link=보고서를 표시하는 데 사용할 외부 브라우저를 구성하려면 <a>''{0}''</a>을(를) 참조하십시오.

designer.preview.preference.masterpagecontent=마스터 페이지 내용 사용(&M)

designer.preview.preference.appcontextkey=사용자 지정 응용 프로그램 컨텍스트 사용(&U)

designer.preview.preference.browser.useExternal=항상 외부 브라우저 사용(&W)

designer.preview.preference.elementname.columnwidth=125

designer.preview.preference.elementname.customname.Enabled=참

designer.preview.preference.elementname.customname=기본 이름

designer.preview.preference.elementname.defaultname.Enabled=거짓

designer.preview.preference.elementname.defaultname=요소 이름

designer.preview.preference.elementname.description.Enabled=거짓

designer.preview.preference.elementname.description=설명

designer.preview.preference.elementname.Enabled=참

designer.preview.preference.locale.description=로케일 선택(&C):

designer.preview.preference.timezone.description=표준 시간대(&T):

designer.preview.preference.resultset.sqldatasetpage.group.title=쿼리 페이지

designer.preview.preference.resultset.previewpage.group.title=미리 보기 페이지

designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting=행 제한 설정 미리 보기

designer.preview.preference.resultset.NoLimitOfDispalyRow=표시할 행 수 제한 없음(&N)

designer.preview.preference.resultset.maxNoOfSchema.description=표시할 최대 스키마 수(&S):

designer.preview.preference.resultset.maxNoOfTable.description=표시할 각 스키마의 최대 테이블 수(&T):

designer.preview.preference.resultset.maxNoOfSchema.errormessage=최대 스키마에 대해 0과 {0} 사이의 숫자를 입력하십시오.

designer.preview.preference.resultset.maxNoOfTable.errormessage=각 스키마의 최대 테이블 수를 0에서 {0} 사이의 숫자로 입력하십시오.

designer.preview.preference.resultset.maxrow.description=표시할 행 수(&R):

designer.preview.preference.resultset.maxrow.errormessage=최대 행에 대해 1과 {0} 사이의 숫자를 입력하십시오.

designer.preview.preference.resultset.maxrowlevelmember.description=각 행 수준에서 표시할 구성원 수(&L):

designer.preview.preference.resultset.maxrowlevelmember.errormessage=최대 행 수준 구성원에 대해 1과 {0} 사이의 숫자를 입력하십시오.

designer.preview.preference.resultset.maxcolumnlevelmember.description=각 열 수준에서 표시할 구성원 수(&C):

designer.preview.preference.resultset.maxcolumnlevelmember.errormessage=최대 열 수준 구성원에 대해 1과 {0} 사이의 숫자를 입력하십시오.

designer.preview.preference.resultset.maxinmemorycubesize.description=메모리 내 큐브 크기(MB)(&S):

designer.preview.preference.resultset.maxinmemorycubesize.errormessage=최대 인 메모리 큐브 크기에 대해 1과 {0} 사이의 숫자를 입력하십시오.

designer.preview.preference.server.description=내부 응용 프로그램 서버 구성. BIRT Report Designer 미리 보기 프레임워크에서\n내부적으로 해당 서버를 사용하고 있습니다.

designer.preview.preference.server.hostDescription=로컬 컴퓨터의 호스트 이름 또는 IP 주소(&H)입니다. 자동으로 결정되게 하려면 공백으로\n두십시오.

designer.preview.preference.server.note=참고:

designer.preview.preference.server.portDescription=수신 대기할 서버의 포트(&P)입니다. 동적으로 할당하려면 0으로 설정하십시오.

designer.preview.preference.server.requireRestart=서버가 이미 실행 중인 경우 이러한 기본 설정은\n다시 시작한 후부터 적용됩니다.

designer.preview.preference.bidiOrientation.label=양방향(&B):

designer.preview.preference.bidiOrientation.auto=자동

designer.preview.preference.bidiOrientation.ltr=왼쪽에서 오른쪽

designer.preview.preference.bidiOrientation.rtl=오른쪽에서 왼쪽

designer.preview.preference.updateParameter.confirmMessage=업데이트 실행 확인

designer.preview.preference.updateParameter.confirmButton=쿼리가 변경된 경우 동기화 데이터 집합 매개 변수 속성과 ODA 매개 변수 속성을 동기화할 때마다 항상 확인합니다(&A)

designer.preview.preference.updateReportParameter.confirmButton=연결된 보고서 매개변수 업데이트에 대한 확인 프롬프트를 표시하지 않습니다(&D).

designer.preview.preference.updateReportParameter.confirmOption.always=항상(&W):

designer.preview.preference.updateReportParameter.confirmOption.update=업데이트(&U)

designer.preview.preference.updateReportParameter.confirmOption.ignore=무시(&I)

designer.preview.previewaction.label.webviewer=웹 뷰어에서 보고서 보기

designer.preview.previewaction.label={0}(으)로 보고서 보기

designer.preview.previewaction.ErrorMessage=iText가 설치되지 않았거나 찾을 수 없어서 PDF 생성 기능을 사용할 수 없습니다.

designer.preview.previewaction.ErrorMessage.title=경고

designer.preview.run.webviewer=웹 뷰어

designer.preview.run={0}로

###########################################################
#DesignerActionBarContributor properties
DesignerActionBarContributor.element.group=그룹 삽입(&G)

DesignerActionBarContributor.element.InsertColumnLeft=왼쪽에 열 삽입(&L)

DesignerActionBarContributor.element.InsertColumnRight=오른쪽에 열 삽입(&R)

DesignerActionBarContributor.element.InsertRowAbove=위에 행 삽입(&A)

DesignerActionBarContributor.element.InsertRowBelow=아래에 행 삽입(&B)

DesignerActionBarContributor.element.merge=셀 병합(&M)

DesignerActionBarContributor.element.split=셀 분할(&S)

designerActionBarContributor.menu.data-newdataset=새 데이터 집합(&E)

designerActionBarContributor.menu.data-newdatasource=새 데이터 원본(&O)

DesignerActionBarContributor.menu.data=데이터(&D)

DesignerActionBarContributor.menu.element-EditGroup=그룹 편집(&E)

DesignerActionBarContributor.menu.element-LayoutMode=레이아웃 모드(&D)

DesignerActionBarContributor.menu.element-showRuler=눈금자 표시(&R)

DesignerActionBarContributor.menu.element.editDataBinding=데이터 바인딩 편집(&B)...

DesignerActionBarContributor.menu.element=요소(&L)

DesignerActionBarContributor.menu.format=형식

DesignerActionBarContributor.menu.insert=삽입(&I)

DesignerActionBarContributor.menu.page=페이지(&G)

DesignerActionBarContributor.menu.style-rule-apply=스타일 적용(&P)

DesignerActionBarContributor.menu.style-rule-edit=스타일 편집(&D)

DesignerActionBarContributor.menu.style-rule-remove=스타일 삭제(&R)

DesignerActionBarContributor.style.new=새 스타일(&N)...

DesignerActionBarContributor.menu.data-NewParameter=새 보고서 매개 변수(&P)

DesignerActionBarContributor.menu.data-NewDataSetParent=새 데이터 집합(&S)

designerActionBarContributor.menu.data-newDataset=새 데이터 집합(&E)

designerActionBarContributor.menu.data-newJointDataset=새 조인 데이터 집합(&J)

DesignerActionBarContributor.menu.element.insert=삽입(&I)

DesignerActionBarContributor.menu.element.group=그룹(&G)

DesignerActionBarContributor.menu.element.style=스타일(&Y)

DesignerActionBarContributor.menu.zoomCombo.tooltip=확대/축소 수준
###########################################################
#DesignerOutlinePage properties
DesignerOutlinePage.action.Delete=선택 삭제

DesignerOutlinePage.action.Hide=머리글 및 바닥글 숨기기

DesignerOutlinePage.action.Redo=다시 실행

DesignerOutlinePage.action.Undo=실행 취소

DesignerOutlinePage.menu.Filters=필터

###########################################################
#Content outline page switch action
ContentOutlinePage.action.text.reportOutline=XML 소스 윤곽선으로 전환

ContentOutlinePage.action.text.reportXMLSourceOutline=보고서 윤곽선으로 전환

###########################################################
#DesignerPaletteFactory properties
DesignerPaletteFactory.categoryName.reportItems=보고서 항목

DesignerPaletteFactory.elementLabel.data=데이터

DesignerPaletteFactory.elementLabel.grid=눈금

DesignerPaletteFactory.elementLabel.image=이미지

DesignerPaletteFactory.elementLabel.label=레이블

DesignerPaletteFactory.elementLabel.list=목록

DesignerPaletteFactory.elementLabel.tabel=테이블

DesignerPaletteFactory.elementLabel.text=텍스트

DesignerPaletteFactory.elementName.data=데이터

DesignerPaletteFactory.elementName.grid=눈금

DesignerPaletteFactory.elementName.image=이미지

DesignerPaletteFactory.elementName.label=레이블

DesignerPaletteFactory.elementName.list=목록

DesignerPaletteFactory.elementName.table=테이블

DesignerPaletteFactory.elementName.text=텍스트

DesignerPaletteFactory.elementName.textdata=동적 텍스트

DesignerPaletteFactory.toolTip.dataReportItem=데이터 집합 열 또는 식 결과를 삽입합니다.

DesignerPaletteFactory.toolTip.gridReportItem=기타 항목을 시각적으로 정렬할 행/열 레이아웃을 삽입합니다.

DesignerPaletteFactory.toolTip.imageReportItem=파일 또는 URL에서 이미지를 삽입합니다.

DesignerPaletteFactory.toolTip.labelReportItem=정적 텍스트를 삽입합니다.

DesignerPaletteFactory.toolTip.listReportItem=머리글/상세 정보/바닥글 밴드에 데이터 집합 행의 가변 형식 프리젠테이션 삽입

DesignerPaletteFactory.toolTip.multiLineDataReportItem=파일, 데이터 집합 열 또는 식 결과에서 텍스트 블록(일반 텍스트/HTML/RTF) 삽입.

DesignerPaletteFactory.toolTip.tableReportItem=머리글/상세 정보/바닥글 밴드에 데이터 집합 행의 열 프리젠테이션 삽입

DesignerPaletteFactory.toolTip.textDataReportItem=옵션 CLOB 데이터 식을 사용하여 텍스트 블록 삽입

DesignerPaletteFactory.toolTip.textReportItem=선택적 인라인 식을 포함한 텍스트 블록(일반 텍스트/HTML)을 삽입합니다.

DesignerPaletteFactory.quicktool.title=빠른 도구

DesignerPaletteFactory.quicktool.agg.title=집계

DesignerPaletteFactory.quicktool.agg.toolTip=집계 항목 삽입

DesignerPaletteFactory.quicktool.timeperiod.title=상대 시간

DesignerPaletteFactory.quicktool.timeperiod.toolTip=TimePeriod 항목 입력

###########################################################
#DimensionBuilderDialog properties
DimensionBuilderDialog.LabelMeasure=측정값(&M):

DimensionBuilderDialog.LabelUnit=단위(&U):

DimensionBuilderDialog.Title=Dimension Builder

###########################################################
#DNDUtil properties
DNDUtil.trans.copy=요소 복사

DNDUtil.trans.cut=요소 이동

###########################################################
#DuplicatesPage properties
DuplicatesPage.Check.HideDuplicates=중복 항목 숨기기

DuplicatesPage.Group.Repeat=중복(반복)

DuplicatesPage.Group.RepeatonPage=중복(페이지에서 반복)

DuplicatesPage.Label.RepeatInfo=이전 데이터 행과 동일한 값을 숨깁니다.

DuplicatesPage.Label.RepeatOnPage.1=이전 행과 동일한 값을 숨기되,

DuplicatesPage.Label.RepeatOnPage.2=\\ 항상 각 새 페이지의 위쪽에 값을 표시합니다.

DuplicatesPage.Label.ResetonGroup=다음 그룹에서 재설정

DuplicatesPage.Radio.Repeat=반복

DuplicatesPage.Radio.RepeatonPage=페이지에서 반복

###########################################################
#EditAction properties
EditAction.text=편집(&E)

###########################################################
#EditGroupAction properties
EditGroupAction.stackMsg.editGroup=그룹 편집

###########################################################
#EditGroupAction properties
InsertGroupHeaderFooterAction.stackMsg.editGroup=그룹 머리글/바닥글 삽입

InsertGroupHeaderFooterAction.Text.Header=머리글

InsertGroupHeaderFooterAction.Text.Footer=바닥글

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.stackMsg.deleteGroup=그룹 삭제

###########################################################
#EditLabelAction properties
EditLabelAction.text=편집(&E)

EditorDragGuidePolicy.pixels.label=픽셀

EditorGuide.bottom.label=아래쪽:

EditorGuide.left.label=왼쪽:

EditorGuide.right.label=오른쪽:

EditorGuide.top.label=위쪽:

###########################################################
#Element properties
Element.AbstractScalarParameter.dataType=데이터 형식(&D):

Element.AutoText.type=유형:

Element.Cell.colSpan=열 범위:

Element.Cell.column=열:

Element.Cell.drop=삭제(&D):

Element.Cell.height=높이:

Element.Cell.rowSpan=행 범위:

Element.Cell.slot.content=내용:

Element.Cell.verticalAlign=세로 맞춤:

Element.Cell.width=너비:

Element.Column.alignment=맞춤:

Element.Column.repeat=반복:

Element.Column.width=너비(&W):

Element.Column.suppressDuplicates=중복 항목 숨기기

Element.Container.slot.reportItems=보고서 항목:

Element.Data.action=작업:

Element.Data.helpText=도움말 텍스트:

Element.Data.helpTextID=도움말 텍스트 키

Element.TextData.valueExpr=값 식(&U):

Element.DataSet.afterClose.toolTip=데이터 집합을 닫은 직후 호출됩니다.

Element.DataSet.afterClose=닫기 이후:

Element.DataSet.afterOpen.toolTip=데이터 집합을 연 직후 호출됩니다.

Element.DataSet.afterOpen=열기 이후:

Element.DataSet.beforeClose.toolTip=데이터 집합을 닫기 직전 호출됩니다.

Element.DataSet.beforeClose=닫기 이전:

Element.DataSet.beforeOpen.toolTip=데이터 집합을 열기 직전 호출됩니다.

Element.DataSet.beforeOpen=열기 이전:

Element.DataSet.cachedMetaData=캐시된 메타데이터:

Element.DataSet.columnHints=열 힌트:

Element.DataSet.computedColumns=계산 열:

Element.DataSet.dataSource=데이터 원본 :

Element.DataSet.filter=필터:

Element.DataSet.inputParameters=입력 매개 변수:

Element.DataSet.onFetch.toolTip=데이터 집합에서 데이터 행을 인출하기 직전 호출됩니다.

Element.DataSet.onFetch=인출 시:

Element.DataSet.outputParameters=출력 매개 변수:

Element.DataSet.paramBindings=매개 변수 바인딩:

Element.DataSet.resultSet=결과 집합:

Element.DataSource.afterClose.toolTip=데이터 원본을 닫은 직후 호출됩니다.

Element.DataSource.afterClose=닫기 이후:

Element.DataSource.afterOpen.toolTip=데이터 원본을 연 직후 호출됩니다.

Element.DataSource.afterOpen=열기 이후:

Element.DataSource.beforeClose.toolTip=데이터 원본을 닫기 직전 호출됩니다.

Element.DataSource.beforeClose=닫기 이전:

Element.DataSource.beforeOpen.toolTip=데이터 원본을 열기 직전 호출됩니다.

Element.DataSource.beforeOpen=열기 이전:

Element.ExtendedItem.extension=확장 이름:

Element.ExtendedItem.extensionName=확장 이름:

Element.FreeForm.slot.reportItems=보고서 항목:

Element.GraphicMasterPage.columns=열:

Element.GraphicMasterPage.columnSpacing=열 공백:

Element.GraphicMasterPage.slot.contents=내용:

Element.Grid.slot.columns=열:

Element.Grid.slot.row=행:

Element.Image.action=작업:

Element.Image.altText=대체 텍스트(&A):

Element.Image.altTextID=대체 텍스트 키(&K):

Element.Image.helpText=도움말 텍스트:

Element.Image.helpTextID=도움말 텍스트 키

Element.Image.imageName=이미지 이름:

Element.Image.scale=배율:

Element.Image.size=크기:

Element.Image.source=원본:

Element.Image.typeExpr=식 입력:

Element.Image.uri=uri:
Element.Image.valueExpr=값 식:

Element.JdbcDataSource.dataSource=데이터 원본 :

Element.JdbcDataSource.driver=드라이버:

Element.JdbcDataSource.password=암호:

Element.JdbcDataSource.url=Url:
Element.JdbcDataSource.userName=사용자 이름:

Element.JdbcSelectDataSet.getStatement.toolTip=쿼리에 대해 사용자 지정된 SQL 문을 반환합니다.

Element.JdbcSelectDataSet.getStatement=문 가져오기:

Element.JdbcSelectDataSet.statement=문:

Element.Label.action=작업:

Element.Label.helpText=도움말 텍스트:

Element.Label.helpTextID=도움말 텍스트 키

Element.Label.text=텍스트:

Element.Label.textID=텍스트 키(&T):

Element.Line.orientation=방향:

Element.List.slot.columnHeader=columnHeader:
Element.List.slot.detail=상세 정보:

Element.List.slot.footer=바닥글:

Element.List.slot.group=그룹:

Element.List.slot.header=머리글:

Element.ListGroup.slot.footer=바닥글:

Element.ListGroup.slot.header=머리글:

Element.Listing.filter=필터:

Element.Listing.onFinish.toolTip=이 요소에 대한 데이터 집합에서 마지막 행을 읽은 이후이며, 바닥글 밴드가 생성되기 이전에 호출됩니다.

Element.Listing.onFinish=종료 시:

Element.Listing.onRow.toolTip=이 요소에 대한 데이터 집합에서 검색된 각 행에 대해 해당 행에 대한 내용을 생성하기 전에 호출됩니다.

Element.Listing.onRow=행에서:

Element.Listing.onStart.toolTip=데이터 원본을 연 후, 머리글 밴드가 생성되기 전, 첫 번째 행이 이 요소에 대한 데이터 집합에서 검색되기 전에 호출됩니다.

Element.Listing.onStart=시작 시:

Element.Listing.pageBreakInterval=페이지 나누기 간격(&K):

Element.Listing.sorting=정렬:

Element.ListingGroup.filter=필터:

Element.ListingGroup.groupName=이름

Element.ListingGroup.interval=간격:

Element.ListingGroup.intervalRange=간격 범위:

Element.ListingGroup.keyExpr=키 식

Element.ListingGroup.onFinish.toolTip=이 요소에 대한 데이터 집합에서 마지막 행을 읽은 이후이며, 바닥글 밴드가 생성되기 이전에 호출됩니다.

Element.ListingGroup.onFinish=종료 시:

Element.ListingGroup.onRow.toolTip=이 요소에 대한 데이터 집합에서 검색된 각 행에 대해 해당 행에 대한 내용을 생성하기 전에 호출됩니다.

Element.ListingGroup.onRow=행에서:

Element.ListingGroup.onStart.toolTip=데이터 집합을 연 후, 머리글 밴드가 생성되기 전, 첫 번째 행이 이 요소에 대한 데이터 집합에서 검색되기 전에 호출됩니다.

Element.ListingGroup.onStart=시작 시:

Element.ListingGroup.pageBreakAfter=뒤에 페이지 나누기:

Element.ListingGroup.pageBreakBefore=앞에 페이지 나누기:

Element.ListingGroup.sort=정렬:

Element.ListingGroup.sortDirection=정렬 방향:

Element.ListingGroup.toc=목차:

Element.MasterPage.bottomMargin=아래쪽 여백(&B):

Element.MasterPage.columns=열:

Element.MasterPage.columnSpacing=간격(&S):

Element.MasterPage.height=높이(&E):

Element.MasterPage.leftMargin=왼쪽 여백(&L):

Element.MasterPage.margin=여백:

Element.MasterPage.orientation=방향(&R):

Element.MasterPage.rightMargin=오른쪽 여백(&R):

Element.MasterPage.slot.contents=내용:

Element.MasterPage.topMargin=위쪽 여백(&T):

Element.MasterPage.type=형식(&Y):

Element.MasterPage.width=너비(&W):

Element.Module.author=만든 이(&U):

Element.Module.base=기본:

Element.Module.colorPalette=색상표:

Element.Module.comments=설명(&C):

Element.Module.configVars=구성 변수:

Element.Module.createdBy=만든 사람(&C):

Element.Module.description=설명(&E):

Element.Module.descriptionID=설명 키:

Element.Module.helpGuide=도움말 설명서:

Element.Module.images=이미지:

Element.Module.includeLibraries=라이브러리 포함:

Element.Module.includeResource=리소스 파일(&E):

Element.Module.includeScripts=스크립트 포함:

Element.Module.initialize.toolTip=보고서 실행 시작 시 호출됩니다.

Element.Module.initialize=초기화:

Element.LayoutModule.theme=테마(&H):

Element.Module.title=제목(&I):

Element.Module.titleID=제목 키:

Element.Module.units=단위(&S):

Element.MultiLineData.contentTypeExpr=내용 형식 식:

Element.MultiLineData.valueExpr=값 식:

Element.OdaDataSet.privateDriverDesignState=개인 드라이버 디자인 상태:

Element.OdaDataSet.privateDriverProperties=개인 드라이버 속성:

Element.OdaDataSet.publicDriverProperties=공용 드라이버 속성:

Element.OdaDataSet.queryScript.toolTip=TBD:

Element.OdaDataSet.queryScript=스크립트:

Element.OdaDataSet.queryText=쿼리:

Element.OdaDataSet.resultSetName=결과 집합 이름:

Element.OdaDataSet.type=형식:

Element.OdaDataSource.driverName=드라이버 이름:

Element.OdaDataSource.privateDriverProperties=개인 드라이버 속성:

Element.OdaDataSource.publicDriverProperties=공용 드라이버 속성:

Element.Parameter.helpText=도움말 텍스트:

Element.Parameter.helpTextID=도움말 텍스트 키(&Y):

Element.Parameter.hidden=숨김:

Element.Parameter.promptTextID=프롬프트 텍스트 키(&K):

Element.ParameterGroup.helpText=도움말 텍스트:

Element.ParameterGroup.helpTextID=도움말 텍스트 키(&Y):

Element.ParameterGroup.slot.parameters=매개 변수:

Element.ParameterGroup.startExpanded=시작 확장됨:

Element.ParameterGroup.promptTextID=프롬프트 텍스트 키(&K):

Element.ReportDesign.afterCloseDoc.toolTip=생성 단계에서 보고서 문서 파일을 닫은 직후 호출됩니다.

Element.ReportDesign.layoutPreference=레이아웃 기본 설정:

Element.ReportDesign.afterCloseDoc=문서 닫기 이후:

Element.ReportDesign.afterFactory.toolTip=생성 단계의 끝과 보고서 문서(있는 경우)를 닫은 직후 호출됩니다. 생성 단계에서 호출되는 마지막 메서드입니다.

Element.ReportDesign.afterFactory=공장 이후:

Element.ReportDesign.afterOpenDoc.toolTip=생성 단계에서 보고서 문서를 연 직후 호출됩니다.

Element.ReportDesign.afterOpenDoc=문서 열기 이후:

Element.ReportDesign.afterRender.toolTip=프리젠테이션 시간 작업 시작 후에 호출됩니다.

Element.ReportDesign.afterRender=렌더링 이후:

Element.ReportDesign.beforeCloseDoc.toolTip=생성 단계에서 보고서 문서 파일을 닫은 직후 호출됩니다.

Element.ReportDesign.beforeCloseDoc=문서 닫기 이전:

Element.ReportDesign.beforeFactory.toolTip=초기화( ) 메서드 이후 생성 단계 시작 시와 보고서 문서(있는 경우)를 열기 전에 호출됩니다.

Element.ReportDesign.beforeFactory=공장 이전:

Element.ReportDesign.beforeOpenDoc.toolTip=생성 단계에서 보고서 문서를 열기 직전 호출됩니다.

Element.ReportDesign.beforeOpenDoc=문서를 열기 전:

Element.ReportDesign.beforeRender.toolTip=프리젠테이션 시간 작업 시작 전에 호출됩니다.

Element.ReportDesign.beforeRender=렌더링 이전:

Element.ReportDesign.displayName=표시 이름(&Y):

Element.ReportDesign.eventHandlerClass=이벤트 처리기 클래스(&C):

Element.ReportDesign.iconFile=섬네일(&N):

Element.ReportDesign.refreshRate=새로 고침 빈도:

Element.ReportDesign.bidiLayoutOrientation=보고서 방향(&R):

Element.ReportElement.comments=설명(&C):

Element.ReportElement.Cube=데이터(&D):

Element.ReportElement.customXml=사용자 지정 XML:

Element.ReportElement.displayName=이름 표시(&Y):

Element.ReportElement.displayNameID=표시 이름 키:

Element.ReportElement.extends=확장:

Element.ReportElement.javaClass=Java 클래스:

Element.ReportElement.name=이름(&N):

Element.ReportElement.propertyMask=속성 마스크:

Element.ReportElement.style=스타일(&Y)

Element.ReportItem.bookmark=책갈피(&B):

Element.ReportItem.dataSet=데이터 집합:

Element.ReportItem.height=높이(&H):

Element.ReportItem.onCreate.toolTip=생성 단계에서 요소 생성 시 실행됩니다.

Element.ReportItem.onCreate=생성 시:

Element.ReportItem.onRender.toolTip=프리젠테이션 단계에서 요소가 렌더링할 준비가 되었을 때 실행됩니다.

Element.ReportItem.onRender=렌더링 시:

Element.ReportItem.paramBindings=매개 변수 바인딩:

Element.ReportItem.toc=목차(&T):

Element.ReportItem.visibility=표시 유형:

Element.ReportItem.width=너비(&W):

Element.ReportItem.x=X:

Element.ReportItem.y=Y:

Element.ReportItem.altText=대체 텍스트

Element.ReportItem.altTextID=대체 텍스트 키

Element.Row.bookmark=책갈피:

Element.Row.height=높이(&H):

Element.Row.slot.cell=셀:

Element.Row.visibility=표시 유형:

Element.ScalarParameter.alignment=맞춤:

Element.ScalarParameter.allowBlank=빈 값 허용:

Element.ScalarParameter.allowNull=널(null) 허용:

Element.ScalarParameter.concealValue=값 숨기기:

Element.ScalarParameter.controlType=컨트롤 형식(&C):

Element.ScalarParameter.defaultValue=기본값:

Element.ScalarParameter.dynamicList.dataSetName=데이터 집합 이름:

Element.ScalarParameter.dynamicList.labelExpr=레이블 식:

Element.ScalarParameter.dynamicList.valueExpr=값 식:

Element.ScalarParameter.dynamicList=동적 목록:

Element.ScalarParameter.format=형식:

Element.ScalarParameter.selectionList.fixedOrder=고정된 순서:

Element.ScalarParameter.selectionList.mustMatch=일치해야 함:

Element.ScalarParameter.selectionList.selectionList=선택 목록:

Element.ScalarParameter.selectionList=선택 목록:

Element.ScalarParameter.promptTextID=프롬프트 텍스트 키(%K):

Element.ScriptDataSet.close.toolTip=데이터 집합을 닫습니다.

Element.ScriptDataSet.close=닫기:

Element.ScriptDataSet.describe.toolTip=결과 집합을 설명합니다.

Element.ScriptDataSet.describe=설명:

Element.ScriptDataSet.fetch.toolTip=형식 DataRow의 객체를 생성, 등록 및 반환합니다.

Element.ScriptDataSet.fetch=인출:

Element.ScriptDataSet.open.toolTip=데이터 집합을 엽니다.

Element.ScriptDataSet.open=열기:

Element.ScriptDataSource.close.toolTip=데이터 원본에 대한 연결을 삭제합니다.

Element.ScriptDataSource.close=닫기:

Element.ScriptDataSource.open.toolTip=데이터 원본으로 연결합니다.

Element.ScriptDataSource.open=열기:

Element.SimpleMasterPage.floatingFooter=부동 바닥글(&F)

Element.SimpleMasterPage.footerHeight=바닥글 높이(&F):

Element.SimpleMasterPage.headerHeight=머리글 높이(&H):

Element.SimpleMasterPage.showFooterOnLast=마지막에 바닥글 표시(&F)

Element.SimpleMasterPage.showHeaderOnFirst=처음에 머리글 표시(&H)

Element.SimpleMasterPage.slot.pageFooter=페이지 바닥글:

Element.SimpleMasterPage.slot.pageHeader=페이지 머리글:

Element.Style.background.attachment=첨부(&A):

Element.Style.background.color=배경색(&B):

Element.Style.background.image=배경 이미지(&G):

Element.Style.background.positionX=가로 위치(&H):

Element.Style.background.positionY=세로 위치(&V):

Element.Style.background.repeat=반복(&R):

Element.Style.background=배경:

Element.Style.border.bottom.color=아래(&M):

Element.Style.border.bottom.style=아래(&B):

Element.Style.border.bottom.width=아래쪽&:

Element.Style.border.fillColor=테두리 채우기 색:

Element.Style.border.hmiddle.color=테두리 가로 중간 색:

Element.Style.border.hmiddle.style=테두리 가로 중간 스타일:

Element.Style.border.hmiddle.width=테두리 가로 중간 너비:

Element.Style.border.left.color=왼쪽(&F):

Element.Style.border.left.style=왼쪽(&L):

Element.Style.border.left.width=왼쪽(&E):

Element.Style.border.right.color=오른쪽(&H):

Element.Style.border.right.style=오른쪽(&R):

Element.Style.border.right.width=오른쪽(&I):

Element.Style.border.top.color=위(&P):

Element.Style.border.top.style=위(&T):

Element.Style.border.top.width=위(&O):

Element.Style.border.vmiddle.color=테두리 세로 중간 색:

Element.Style.border.vmiddle.style=테두리 세로 중간 스타일:

Element.Style.border.vmiddle.width=테두리 세로 중간 너비:

Element.Style.border=테두리:

Element.Style.box=상자:

Element.Style.canShrink=축소 가능(&K)

Element.Style.color=색(&C):

Element.Style.display=표시(&P):

Element.Style.font.family=글꼴(&F):

Element.Style.font.size=크기(&Z):

Element.Style.font.style=스타일(&T):

Element.Style.font.textLineThrough=가운데 줄(&L)

Element.Style.font.textOverline=윗줄(&O)

Element.Style.font.textUnderline=밑줄(&U)

Element.Style.font.variant=글꼴 변형:

Element.Style.font.weight=두께(&W):

Element.Style.font=글꼴(&F):

Element.Style.format.dateTime=날짜/시간:

Element.Style.format.number.align=숫자 형식 맞춤:

Element.Style.format.number=번호:

Element.Style.format.string=문자열:

Element.Style.format=형식:

Element.Style.highlight.rules=규칙 강조:

Element.Style.highlight.testExpr=테스트 식 강조:

Element.Style.letterSpacing=문자 간격(&T):

Element.Style.lineHeight=줄 높이(&N):

Element.Style.map.rules=맵 규칙:

Element.Style.map.testExpr=맵 테스트 식:

Element.Style.margin.bottom=아래(&B):

Element.Style.margin.left=왼쪽(&E):

Element.Style.margin.right=오른쪽(&I):

Element.Style.margin.top=위(&P):

Element.Style.masterPage=마스터 페이지(&P):

Element.Style.orphans=첫 줄 분리(&O):

Element.Style.padding.bottom=아래(&B):

Element.Style.padding.left=왼쪽(&L):

Element.Style.padding.right=오른쪽(&R):

Element.Style.padding.top=위(&T):

Element.Style.padding=안쪽 여백:

Element.Style.pageBreakAfter=다음 이후(&A):

Element.Style.pageBreakBefore=다음 이전(&B):

Element.Style.pageBreakInside=안쪽(&I):

Element.Style.section=섹션:

Element.Style.showIfBlank=비어있는 경우 표시(&W)

Element.Style.text.align=텍스트 맞춤(&L):

Element.Style.width=너비(&W):

Element.Style.height=높이(&H):
#bidi_hcg
Element.Style.text.direction=양방향 텍스트 방향(&B):

Element.Style.text.indent=텍스트 들여쓰기(&X):

Element.Style.text.transform=텍스트 변환(&R):

Element.Style.text=텍스트:

Element.Style.verticalAlign=세로 맞춤(&A):

Element.Style.whiteSpace=공백(&I):

Element.Style.widows=마지막 줄 분리(&D):

Element.Style.wordSpacing=단어 간격(&W):

Element.Table.caption=캡션:

Element.Table.captionID=캡션 키:

Element.Table.repeatHeader=머리글 반복:

Element.Table.slot.columnHeader=columnHeader:
Element.Table.slot.columns=열:

Element.Table.slot.detail=상세 정보:

Element.Table.slot.footer=바닥글:

Element.Table.slot.group=그룹:

Element.Table.slot.header=머리글:

Element.TableGroup.slot.footer=바닥글:

Element.TableGroup.slot.header=머리글:

Element.Text.content=내용:

Element.Text.contentID=내용 키(&K):

Element.Text.contentType=내용 형식(&P):

Element.TemplateElement.name=이름(&N)

Element.Toc.Sytle=스타일(&Y)

Element.VariableElement.name=이름(&N):

Element.VariableElement.type=형식(&T):

Element.VariableElement.value=값(&V):

###########################################################
#EmbeddedImageNodeProvider properties
EmbeddedImageNodeProvider.action.New=새 포함 이미지(&B)

###########################################################
#EmbeddedImagesNodeProvider properties
EmbeddedImagesNodeProvider.action.New=새 포함 이미지(&B)

###########################################################
#ElementIdDescriptorProvider properties
ElementIdDescriptorProvider.Display.Element.ID=요소 ID(&E):

###########################################################
#EmptyEditPart properties
EmptyEditPart.Msg.EmptyLib=라이브러리에 시각적 요소가 없습니다. 시각적 요소를 추가하려면 색상표에서 라이브러리 윤곽선 보기로 항목을 끌거나 윤곽선 보기의 삽입 메뉴를 사용하십시오.

EmptyEditPart.Msg.NonVisual=보이지 않는 요소가 선택되었습니다.

###########################################################
#error properties
error.duplicateName=새 이름이 동일한 네임스페이스의 기존 이름과 중복됩니다.

error.DataSource.emptyName=데이터 원본 이름을 입력해야 합니다.

error.DataSet.emptyName=데이터 집합 이름을 입력해야 합니다.

error.OutputColumn.emptyName=출력 열 이름을 입력해야 합니다.

error.invalidName=이름 "{0}"이(가) 잘못되었습니다. 이름에는 ".", "/", "\\", "!", ";", ","와 같은 문자를 사용할 수 없습니다.

###########################################################
#ExceptionHandler properties
ExceptionHandler.Label.ErrorCode=오류 코드

ExceptionHandler.Label.ErrorMessage=오류 메시지

ExceptionHandler.Label.PluginId=플러그인 ID:

ExceptionHandler.Label.PluginName=플러그인 이름:

ExceptionHandler.Label.PluginProvider=플러그인 공급자:

ExceptionHandler.Label.PluginVersion=버전:

ExceptionHandler.Label.UnknownPlugin=알 수 없는 플러그인

ExceptionHandler.Meesage.UnexceptedExceptionOccur=예기치 못한 내부 예외가 발생했습니다. 자세한 내용은 로그 파일을 확인하십시오.

ExceptionHandler.Message.BirtExceptionOccur=BIRT 예외가 발생했습니다.

ExceptionHandler.Message.CausedBy={0}(으)로 인해 발생했습니다.

ExceptionHandler.Message.FileNotFound=파일 없음:

ExceptionHandler.Message.OutOfMemory=메모리가 부족합니다.

ExceptionHandler.Message.PartInitError=부분 초기화 실패.

ExceptionHandler.Message.SemanticFail=의미 확인 실패.

ExceptionHandler.Message.UnexceptedExceptionOccur=내부 예외가 발생했습니다. 자세한 내용은 로그 파일을 참조하십시오.

ExceptionHandler.Message.UnknownHost=알 수 없는 호스트:

ExceptionHandler.Title.Error=오류

ExceptionHandler.Title.FileNotFound=파일 없음.

ExceptionHandler.Title.PartInitError=부분 초기화 오류.

ExceptionHandler.Title.UnknownHost=알 수 없는 호스트

ExceptionHandler.Meesage.ExceptionOccur=예기치 않은 예외가 발생했습니다. 자세한 내용은 로그를 참조하십시오.

###########################################################
#ExportAction properties
ExportAction.actionMsg.export=내보내기

###########################################################
#ExportToLibraryAction properties
ExportToLibraryAction.action.text=라이브러리로 내보내기(&X)...

ExportToLibraryAction.Button.Cancel=취소

ExportToLibraryAction.Button.No=아니오

ExportToLibraryAction.Button.Yes=예

ExportToLibraryAction.Dialog.Message=\n대상 라이브러리에 동일한 디자인 요소가 있을 수 있습니다. 덮어쓰시겠습니까?

ExportToLibraryAction.Dialog.Title=요소 내보내기

ExportToLibraryAction.Message.RememberDecision=선택 저장(&R)


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.action.text=라이브러리로 내보내기(&X)...

ExportToCPSouceAction.action.text=연결 프로필로 외부화(&O)...


###########################################################
#LibraryExplorerTreeViewPage tooltips
LibraryElementsToolHandleExtends_message=요소가 지정된 명시적 데이터 집합이 없는 로컬 바인딩을 포함합니다. 이로 인해 실행 시 성능이 저하될 수 있습니다. 성능 향상을 위해 로컬 바인딩을 상위로 이동하시겠습니까?

LibraryElementsToolHandleExtends_question=성능 향상을 위해 로컬 바인딩을 상위로 이동하시겠습니까?

LibraryElementsToolHandleExtends_toggle=선택 저장(&R)

LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline=윤곽선 보기로 끌어서 놓기

LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout=레이아웃 편집기로 끌어서 놓기

LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout=윤곽선 보기 또는 레이아웃 편집기로 끌어서 놓기

###########################################################
#ExpressionBuidler properties
ExpressionBuidler.Dialog.Title=연산식 빌더(JavaScript)

ExpressionBuidler.Title=연산식 빌더

ExpressionBuilder.tooltip.Sort=정렬

ExpressionBuilder.toolTipText.Button=식 빌더 열기

###########################################################
#ExpressionBuilder properties
ExpressionBuilder.Label.Category=범주(&C):

ExpressionBuilder.Label.Functions=삽입하려면 두 번 클릭(&I):

ExpressionBuilder.Label.Hint=힌트

ExpressionBuilder.Label.Operators=연산자:

ExpressionBuilder.Label.SubCategory=하위 범주(&S):

ExpressionBuilder.Message.Prompt=식 필드에 식을 입력하십시오. 사용 가능한 객체 목록을 찾아보고 식으로 복사하려면 두 번 클릭하십시오.

ExpressionBuilder.ToolTip=식 빌더 열기

ExpressionBuilder.toolTipText.validate=확인

ExpressionBuilder.Script.NoError=현재 스크립트에 구문 오류가 없습니다.

ExpressionBuilder.Script.Error=[행:{0}, 열:{1}] {2}

ExpressionBuilder.Script.Warning=경고

ExpressionBuilder.Script.Confirm=\n현재 스크립트에 구문 오류가 있습니다. 저장하시겠습니까?

ExpressionBuilder.toolTipText.calendar=달력

ExpressionBuilder.Calendar.Button.OK=확인

ExpressionButtonProvider.Constant=상수

ExpressionButtonProvider.Javascript=JavaScript 구문

###########################################################
#ExpressionPropertyDescriptor properties
ExpressionPropertyDescriptor.text.Edit=식 빌더(&X)...

###########################################################
#ExpressionProvider properties
ExpressionProvider.Category.Context=컨텍스트

ExpressionProvider.Category.BirtObjects=BIRT 함수

ExpressionProvider.Category.DataSets=사용 가능한 데이터 집합

ExpressionProvider.Category.NativeObjects=고유 JavaScript 함수

ExpressionProvider.Category.ColumnBinding=사용 가능한 열 바인딩

ExpressionProvider.Category.Operators=연산자

ExpressionProvider.Category.Parameters=보고서 매개 변수

ExpressionProvider.Category.DataCubes=현재 데이터 큐브

ExpressionProvider.Category.Variables=변수

ExpressionProvider.Label.All=---모두---

ExpressionProvider.Operator.Add=두 값을 함께 추가합니다.

ExpressionProvider.Operator.AddTo=바로 가기(현재 값에 추가).

ExpressionProvider.Operator.And=두 식을 보고 두 식이 모두 참인 경우 참 값을 반환합니다.

ExpressionProvider.Operator.Assign=다른 값과 동일한 값을 설정합니다.

ExpressionProvider.Operator.BitAnd=A & B: A와 B의 비트 연산자 AND 결과를 반환합니다.

ExpressionProvider.Operator.BitOr=A | B: A와 B의 비트 연산자 OR 결과를 반환합니다.

ExpressionProvider.Operator.Dec=값을 줄인 후 결과를 반환합니다.

ExpressionProvider.Operator.DividingFrom=바로 가기(현재 값 나누기).

ExpressionProvider.Tooltip.ColumnBinding=참조

ExpressionProvider.Operator.Divides=왼쪽의 값을 오른쪽의 값으로 나누고 결과를 반환합니다.

ExpressionProvider.Operator.Equals=항목이 동일한 경우 참 값을 반환합니다.

ExpressionProvider.Operator.Greater=A > B: A가 B보다 큰 경우 참 값을 반환합니다.

ExpressionProvider.Operator.GreaterEquals=A >= B: A가 B보다 크거나 같은 경우 참 값을 반환합니다.

ExpressionProvider.Operator.Inc=값을 늘린 후 결과를 반환합니다.

ExpressionProvider.Operator.LeftBracket=왼쪽 대괄호

ExpressionProvider.Operator.RightBracket=오른쪽 대괄호

ExpressionProvider.Operator.Less=A < B: A가 B보다 작은 경우 참 값을 반환합니다.

ExpressionProvider.Operator.LessEqual=A <= B: A가 B보다 작거나 같은 경우 참 값을 반환합니다.

ExpressionProvider.Operator.Mult=두 값을 곱합니다.

ExpressionProvider.Operator.MultTo=바로 가기(현재 값 곱하기).

ExpressionProvider.Operator.Not=!A: A가 거짓이면 참 값을 반환하거나 A가 참이면 거짓 값을 반환합니다.

ExpressionProvider.Operator.NotEqual=A <> B: A가 B와 같지 않을 경우 참 값을 반환합니다.

ExpressionProvider.Operator.Or=두 식을 보고 둘 중 하나가 참이면 참값을 반환합니다.

ExpressionProvider.Operator.ReturnDec=값을 반환한 후 값을 줄입니다.

ExpressionProvider.Operator.ReturnInc=값을 반환한 후 값을 늘립니다.

ExpressionProvider.Operator.Sub=다른 값에서 한 값을 뺍니다.

ExpressionProvider.Operator.SubFrom=바로 가기(현재 값에서 빼기).

ExpressionProvider.Operators.Assignment=할당

ExpressionProvider.Operators.Comparison=비교

ExpressionProvider.Operators.Computational=전산

ExpressionProvider.Operators.Logical=논리

ExpressionProvider.EditBindings=바인딩 편집...

ExpressionProvider.Expression.RowNumName=RowNum

ExpressionProvider.Expression.RowNumTooltip=현재 행 개수를 반환하는 식

ExpressionProvider.Variables=변수

ExpressionProvider.Variables.Report=보고서 변수

ExpressionProvider.Variables.Page=페이지 변수

ExpressionTreeSupport.Category.Global=전역

###########################################################
#ExpressionValueCellEditor properties
ExpressionValueCellEditor.buildExpressionAction=<식 빌드...>

ExpressionValueCellEditor.selectValueAction=<값 선택...>

ExpressionValueCellEditor.title=값 선택

###########################################################
#ExtendedEditPart properties
ExtendedEditPart.edit={0} 편집

###########################################################
#ExtendedItemPageGenerator properties
ExtendedItemPageGenerator.List.Bookmark=책갈피

ExtendedItemPageGenerator.List.TOC=목차

###########################################################
#FilterHandleProvider properties
FilterHandleProvider.ColumnName.Value=값

FilterHandleProvider.Label.Filterby=필터링 기준:

FilterHandleProvider.Modify.SaveValues=값 저장

###########################################################
#filters properties
filters.empty.columnName=식을 비워둘 수 없음.

filters.error.title=잘못된 입력.

filters.operators.any=is any

filters.operators.between=사이에 있음

filters.operators.bottomN=아래쪽(n) 항목

filters.operators.bottomPercent=아래쪽(n) 퍼센트

filters.operators.equal=is equal to

filters.operators.false=is false

filters.operators.greaterThan=is greater than

filters.operators.greaterThanEqual=is greater than or equal to

filters.operators.lessThan=is less than

filters.operators.lessThanEqual=is less than or equal to

filters.operators.like=is like

filters.operators.notBetween=is not between

filters.operators.notEqual=is not equal to

filters.operators.notNull=is not null

filters.operators.null=is null

filters.operators.topN=is top (n) items

filters.operators.topPercent=is top (n) percent

filters.operators.true=is true

filters.prompt.new=<여기에 새 필터 입력...>

###########################################################
#FixTableLayoutCalculator properties
FixTableLayoutCalculator.Error.CannotParse=입력 값을 구문 분석할 수 없습니다.

###########################################################
#FlowMoveChildCommand properties
FlowMoveChildCommand.transLabel.moveElement=요소 이동

###########################################################
#FontPreferencePage properties
FontPreferencePage.displayname.Title=글꼴

FontPreferencePage.label.fontDecoration=장식:

FontPreferencePage.text.Preview=샘플 미리 보기

FontPreferencePage.text.PreviewContent=AaBbYyZz

###########################################################
#FontSizeBuilder properties
FontSizeBuilder.displayname.Auto=자동

###########################################################
#FormatBuilder properties
FormatAdapter.DisplayName.None=자동

FormatBuilder.Title=형식 빌더

###########################################################
#FormatDateTimeAttributePage properties
FormatDateTimeAttributePage.Trans.SetDateTimeFormat = 날짜 시간 형식을 설정합니다.

###########################################################
#FormatDateTimePage properties
FormatDateTimePage.label.custom.settings.label=예 형식(하나를 클릭하여 아래 복사한 다음 수정)(&P):

FormatDateTimePage.label.custom.settings=사용자 지정 설정

FormatDateTimePage.label.format.code=형식 코드(&C):

FormatDateTimePage.label.format.page=다음으로 형식 지정(&F):

FormatDateTimePage.label.format.locale=로케일(&L):

FormatDateTimePage.label.general.preview.group=형식 미리 보기

###########################################################
# LOCALE SPECIAL:
# The datetime pattern string in this text should be formatted locale-dependent if needed
FormatDateTimePage.label.guide.format=yyyy/MM/dd hh:mm:ss

FormatDateTimePage.label.guide.text=다음 형식으로 입력하십시오: yyyy/MM/dd hh:mm:ss AM/PM

FormatDateTimePage.label.preview.dateTime=미리 볼 날짜/시간(&P):

FormatDateTimePage.label.preview.group=미리 보기

FormatDateTimePage.label.preview.label=형식 미리 보기:

FormatDateTimePage.label.table.column.format.name=예 형식

FormatDateTimePage.label.table.column.format.result=예 형식 결과

FormatDateTimePage.label.table.column.format.code=코드 예

FormatDateTimePage.preview.invalid.dateTime=잘못된 미리 보기 날짜 시간

FormatDateTimePage.preview.invalid.formatCode=잘못된 형식 코드

###########################################################
#FormatDateTimePreferencePage properties
FormatDateTimePreferencePage.formatDateTime.title=날짜 시간 형식

###########################################################
#FormatNumberAttributePage properties
FormatNumberAttributePage.Trans.SetNumberFormat = 숫자 형식을 설정합니다.

###########################################################
#FormatNumberPage properties
FormatNumberPage.currency.symbol.none=기호 없음

FormatNumberPage.label.currency.settings=통화 설정

FormatNumberPage.label.custom.preview.group=미리 보기

FormatNumberPage.label.custom.preview.label=형식 미리 보기:

FormatNumberPage.label.custom.settings.lable=예 형식(하나를 클릭하여 아래 복사한 다음 수정)(&P):

FormatNumberPage.label.custom.settings=사용자 지정 설정

FormatNumberPage.label.decimal.places=소수 자릿수(&D):

FormatNumberPage.label.rounding.mode=반올림 모드(&R):

FormatNumberPage.label.fixed.settings=고정 설정

FormatNumberPage.label.format.code=형식 코드(&C):

FormatNumberPage.label.format.number.page=다음으로 형식 지정(&F):

FormatNumberPage.label.format.number.locale=로케일(&L):

FormatNumberPage.label.general.preview.group=형식 미리 보기

FormatNumberPage.label.negative.numbers=음수(&N):

FormatNumberPage.label.percent.settings=백분율 설정

FormatNumberPage.label.preview.number=미리 볼 숫자(&P):

FormatNumberPage.label.scientific.settings=공학용 설정

FormatNumberPage.label.symbol.position=기호 위치(&Y):

FormatNumberPage.label.symbol=기호(&B):

FormatNumberPage.label.table.column.format.code=예 형식

FormatNumberPage.label.table.column.format.result=예 형식 결과

FormatNumberPage.label.use1000sSeparator=1000단위 구분 기호 사용(&1)

FormatNumberPage.label.useLeadingZero=선행 0 사용

FormatNumberPage.label.useSymbolSpace=기호 공백 사용(&U)

FormatNumberPage.preview.invalidFormatCode=잘못된 형식 코드

FormatNumberPage.preview.invalidNumber=잘못된 숫자 미리 보기

FormatNumberPage.symblePos.after=이후

FormatNumberPage.symblePos.before=이전

FormatNumberPage.roundingMode.halfUp=반올림

FormatNumberPage.roundingMode.halfDown=반내림

FormatNumberPage.roundingMode.halfEven=짝수로 반올림

FormatNumberPage.roundingMode.up=위로

FormatNumberPage.roundingMode.down=아래로

FormatNumberPage.roundingMode.ceiling=정수로 올림

FormatNumberPage.roundingMode.floor=정수로 내림

FormatNumberPage.roundingMode.unnecessary=필요 없음

###########################################################
#FormatNumberPreferencePage properties
FormatNumberPreferencePage.formatNumber.title=숫자 형식

###########################################################
#FormatPreferencePage properties
FormatPreferencePage.displayname.Title=형식

###########################################################
#FormatStringAttributePage properties
FormatStringAttributePage.Trans.SetStringFormat = 문자열 형식을 설정합니다.

###########################################################
#FormatStringPage properties
FormatStringPage.default.preview.text=내 문자열

FormatStringPage.label.custom.preview.label=형식 미리 보기:

FormatStringPage.label.custom.settings.label=예 형식(하나를 클릭하여 아래 복사한 다음 수정)(&P):

FormatStringPage.label.customSettings=사용자 지정 설정

FormatStringPage.label.format.code=형식 코드(&C):

FormatStringPage.label.formatStringAs=다음으로 형식 지정(&F):

FormatStringPage.label.locale=로케일(&L):

FormatStringPage.label.preview.group=미리 보기

FormatStringPage.label.preview.string=미리 볼 문자열(&R):

FormatStringPage.label.previewWithFormat=형식 미리 보기

FormatStringPage.label.table.collumn.exampleFormatCode=예 형식

FormatStringPage.label.table.collumn.exampleFormatResult=예 형식 결과

FormatStringPage.previewText.invalidFormatCode=잘못된 형식 코드

###########################################################
#FormatStringPreferencePage properties
FormatStringPreferencePage.formatString.title=문자열 형식

###########################################################
#FormPage properties
FormPage.Button.Add=추가(&A)

FormPage.Button.AddWithDialog=추가(&A)...

FormPage.Button.AddWithDialog.PROP=파일 추가(&A)...

FormPage.Button.AddWithDialog.JAR=파일 추가(&L)...

FormPage.Button.AddWithDialog.JS=파일 추가(&.)...

FormPage.Button.Add.AggregateOn=집계 추가(&G)

FormPage.Button.AddWithDialog.AggregateOn=집계 추가(&G)...

FormPage.Button.Delete=제거(&R)

FormPage.Button.DeleteFile=파일 제거(&R)

FormPage.Button.DeleteFile.Alt1=파일 제거(&I)

FormPage.Button.DeleteFile.Alt2=파일 제거(&F)

FormPage.Button.Down=아래로 이동(&N)

FormPage.Button.Down.Alt1=아래로 이동(&W)

FormPage.Button.Down.Alt2=아래로 이동(&D)

FormPage.Button.Edit=편집(&D)

FormPage.Button.EditWithDialog=편집(&D)...

FormPage.Button.Up=위로 이동(&U)

FormPage.Button.Up.Alt1=위로 이동(&P)

FormPage.Button.Up.Alt2=위로 이동(&E)

FormPage.Button.Binding.Refresh=새로 고침(&F)

FormPage.Button.Binding.Clear=지우기(&C)

FormPage.Menu.ModifyProperty=속성 수정

FormPage.toolTipText.Up=위로 이동

FormPage.toolTipText.Down=아래로 이동
###########################################################
#GeneralInsertMenuAction properties
GeneralInsertMenuAction.text.Data=데이터(&D)

GeneralInsertMenuAction.text.DynamicText=동적 텍스트(&Y)

GeneralInsertMenuAction.text.Grid=눈금(&G)

GeneralInsertMenuAction.text.Image=이미지(&I)

GeneralInsertMenuAction.text.Label=레이블(&L)

GeneralInsertMenuAction.text.List=목록(&S)

GeneralInsertMenuAction.text.Table=테이블(&B)

GeneralInsertMenuAction.text.Text=텍스트(&T)

###########################################################
#GeneralPreferencePage properties
GeneralPreferencePage.errorMsg.duplicate.styleName=중복된 스타일 이름입니다.

GeneralPreferencePage.label.styleNameDuplicate=스타일 이름 "{0}"은(는) 이미 있습니다. 다른 이름을 선택하십시오.

GeneralPreferencePage.label.styleNamePredefind=스타일 이름 "{0}"이(가) 미리 정의되었습니다. 다른 이름을 선택하십시오.

GeneralPreferencePage.label.customStyle=사용자 지정 스타일(&C)

GeneralPreferencePage.Label.CustomStyle=이름:

GeneralPreferencePage.Label.Note=참고: *스타일 속성을 특정 형식의 보고서 요소에 적용하십시오.

GeneralPreferencePage.Label.PreDefinedStyle=보고서 요소 선택:

GeneralPreferencePage.label.predefinedStyle=미리 정의된 스타일(&P)*

GeneralPreferencePage.label.nameEmpty=스타일 이름은 필수 입력 항목입니다.

###########################################################
#GridEditPart properties
GraphicalEditorWithFlyoutPalette_Redo.Text0=다시 실행(&R)

GraphicalEditorWithFlyoutPalette_Redo.Text1={0} 다시 실행(&R)

GraphicalEditorWithFlyoutPalette_Redo.ToolTip0=다시 실행

GraphicalEditorWithFlyoutPalette_Redo.ToolTip1={0} 다시 실행

GraphicalEditorWithFlyoutPalette_Undo.Text0=실행 취소(&U)

GraphicalEditorWithFlyoutPalette_Undo.Text1={0} 실행 취소(&U)

GraphicalEditorWithFlyoutPalette_Undo.ToolTip0=실행 취소

GraphicalEditorWithFlyoutPalette_Undo.ToolTip1={0} 실행 취소

GridEditPart.GUIDEHANDLE_TEXT = 눈금

###########################################################
#GridFigure properties
GridFigure.BORDER_TEXT=눈금

###########################################################
#GridPageGenerator properties
GridPageGenerator.List.Bookmark=책갈피

GridPageGenerator.List.Borders=테두리

GridPageGenerator.List.Font=글꼴

GridPageGenerator.List.General=일반

GridPageGenerator.List.Margin=여백

GridPageGenerator.List.Section=페이지 나누기

GridPageGenerator.List.TOC=목차

GridPageGenerator.List.Visibility=표시 유형

###########################################################
#GridProvider properties
GridProvider.action.text=행 삽입(&S)

###########################################################
#GroupDialog properties
GroupDialog.Dialog.GroupDetail=그룹 상세 정보

GroupDialog.Label.Bookmark=책갈피(&B):

GroupDialog.Dialog.TOCGroup=목차

GroupDialog.Dialog.TOC=항목 식(&T):

GroupDialog.Dialog.TOCStyle=스타일(&Y)

GroupDialog.DialogTitle.NoData=초기화 실패

GroupDialog.DialogTitle.SemanticCheckFailed=의미 확인에 실패했습니다.

GroupDialog.ErrorMesaage.IllegalGroup=이 그룹은 잘못되었습니다.

GroupDialog.ErrorMessage.NoData=사용할 수 있는 데이터 행이 없습니다.

GroupDialog.Label.FilterSorting=필터 및 정렬

GroupDialog.Label.GroupOn=그룹화 대상(&G):

GroupDialog.Label.HeaderFooter=머리글 및 바닥글

GroupDialog.Label.IncludeFooter=그룹 바닥글 포함(&F)

GroupDialog.Label.IncludeHeader=그룹 머리글 포함(&H)

GroupDialog.Label.Interval=간격(&V):

GroupDialog.PageBreakBefore=다음 이전(&O):

GroupDialog.PageBreakAfter=다음 이후(&F):

GroupDialog.PageBreakInside=안쪽(&I):

GroupDialog.Label.IntervalBase=고정된 기본값을 간격으로 사용(&X):

GroupDialog.Label.Name=이름(&M):

GroupDialog.Label.Range=범위(&E):

GroupDialog.Tab.Binding=데이터 바인딩

GroupDialog.Tab.Filter=필터

GroupDialog.Tab.Sorting=정렬

GroupDialog.RepeatHeader=머리글 반복(&P)

GroupDialog.Title.Edit=그룹 편집

GroupDialog.Title.New=새 그룹

GroupDialog.PageBreak=페이지 나누기

GroupDialog.buttion.HideDetail=상세 정보 숨기기(&H)

GroupDialog.toolTipText.openExprButton=식 빌더 열기

GroupDialog.Button.Ascending=오름차순(&S)

GroupDialog.Button.Descending=내림차순(&C)
###########################################################
#GroupHandleProvider properties
GroupHandleProvider.Label.Groups=그룹 목록:

###########################################################
#GroupProvider properties
GroupProvider.action.text=그룹 편집(&E)

###########################################################
#GuiExtensionManager properties
GuiExtensionManager.label.name=확장 항목

GuiExtensionManager.tooltip.insert={0} 삽입

###########################################################
#HighlightHandleProvider properties
HighlightHandleProvider.text.Preview=AaBbCcYyZz(글꼴 크기 =

###########################################################
#HighlightRuleBuilderDialog properties
HightlightRuleBuilder.button.add=추가(&A)

HightlightRuleBuilder.button.add.tooltip=값 목록에 값 추가

HightlightRuleBuilder.button.delete=제거(&M)

HightlightRuleBuilder.button.delete.tooltip=목록에서 선택한 값 제거

HightlightRuleBuilder.button.deleteall=모두 제거(&O)

HightlightRuleBuilder.button.deleteall.tooltip=목록에서 모든 값 제거

HightlightRuleBuilder.button.edit=편집(&E)...

HightlightRuleBuilder.button.edit.tooltip=값 목록에서 선택한 값 편집

HightlightRuleBuilder.label.value=값(&V):

HightlightRuleBuilder.list.item1=값

HightlightRuleBuilder.MsgDlg.Msg=경고

HightlightRuleBuilder.MsgDlg.Title=값을 입력해야 합니다.

HightlightRuleBuilder.text.AND=및

HighlightRuleBuilderDialog.choice.ValueOfThisDataItem=이 데이터 항목 값

HighlightRuleBuilderDialog.text.AND=및

HighlightRuleBuilderDialog.text.Any=모두

HighlightRuleBuilderDialog.text.BackgroundColor=배경색(&B):

HighlightRuleBuilderDialog.text.Between=사이에 있음

HighlightRuleBuilderDialog.text.Color=색(&C):

HighlightRuleBuilderDialog.text.Default=자동

HighlightRuleBuilderDialog.text.ET=같음

HighlightRuleBuilderDialog.text.False=거짓

HighlightRuleBuilderDialog.text.Font=글꼴(&F):

HighlightRuleBuilderDialog.text.GE=크거나 같음

HighlightRuleBuilderDialog.text.GT=큼

HighlightRuleBuilderDialog.text.LE=작거나 같음

HighlightRuleBuilderDialog.text.Like=유사함

HighlightRuleBuilderDialog.text.LT=작음

HighlightRuleBuilderDialog.text.NE=같지 않음

HighlightRuleBuilderDialog.text.NotBetween=사이에 있지 않음

HighlightRuleBuilderDialog.text.NotNull=널(null)이 아님

HighlightRuleBuilderDialog.text.Null=널(null)임

HighlightRuleBuilderDialog.text.Preview=미리 보기:

HighlightRuleBuilderDialog.text.PreviewContent=AaBbCcYyZz
HighlightRuleBuilderDialog.text.Size=크기(&Z):

HighlightRuleBuilderDialog.text.Title=강조

HighlightRuleBuilderDialog.text.True=참

HighlightRuleBuilderDialog.tooltip.ExpBuilder=식 빌더 열기

HighlightRuleBuilderDialog.tooltip.Bold=굵게

HighlightRuleBuilderDialog.tooltip.Italic=기울임꼴

HighlightRuleBuilderDialog.tooltip.Underline=밑줄

HighlightRuleBuilderDialog.tooltip.Text_Line_Through=텍스트 가운데 선 긋기

HighlightRuleBuilderDialog.text.applyStyle=스타일 적용(&Y):

HighlightRuleBuilderDialog.displayText.None=없음

HighlightRuleBuilderDialog.text.Description={0}에 조건부 서식을 적용합니다. 조건식 및 서식을 지정하십시오.\n조건식 계산 결과가 True인 경우 서식이 적용됩니다.

HighlightRuleBuilderDialog.text.Description.Element.Table=테이블

HighlightRuleBuilderDialog.text.Description.Element.Grid=눈금

HighlightRuleBuilderDialog.text.Description.Element.List=목록

HighlightRuleBuilderDialog.text.Description.Element.Row=행

HighlightRuleBuilderDialog.text.Description.Element.Column=열

HighlightRuleBuilderDialog.text.Description.Element.DataItem=데이터 항목

HighlightRuleBuilderDialog.text.Description.Element.Cell=셀

HighlightRuleBuilderDialog.text.Group.Condition=조건(&D)

HighlightRuleBuilderDialog.text.Group.Format=형식

HighlightRuleBuilderDialog.text.Description.Element.ReportElement=보고서 요소

###########################################################
#HighlightsPage properties
HighlightsPage.Button.Add=추가(&A)...

HighlightsPage.Button.Delete=제거(&R)

HighlightsPage.Dialog.EditHighlight=강조 편집

HighlightsPage.Dialog.NewHighlight=새 강조

HighlightsPage.Label.Highlights=강조 목록:

HighlightsPage.TableColumn.Condition=조건

HighlightsPage.TableColumn.Preview=미리 보기:

HighlightsPage.trans.Add=강조 규칙 추가

HighlightsPage.trans.Delete=강조 규칙 삭제

HighlightsPage.trans.Edit=강조 규칙 편집

HighlightsPage.trans.MoveDown=강조 규칙 아래로 이동

HighlightsPage.trans.MoveUp=강조 규칙 위로 이동

HighlightsPage.toolTipText.MoveDown=강조 규칙 아래로 이동

HighlightsPage.toolTipText.MoveUp=강조 규칙 위로 이동

HighlightsPage.label.duplicate=중복(&C)

HighlightsPage.toolTipText.duplicate=강조 규칙 중복

HighlightsPage.trans.Duplicate=강조 규칙 중복

###########################################################
#HighlightsPreferencePage properties
HighlightsPreferencePage.displayname.Condition=조건

HighlightsPreferencePage.displayname.Preview=미리 보기

HighlightsPreferencePage.displayname.Title=강조

HighlightsPreferencePage.text.Add=추가(&A)...

HighlightsPreferencePage.text.Delete=제거(&R)

HighlightsPreferencePage.text.Description=강조 목록:

HighlightsPreferencePage.text.Edit=편집(&D)...

HighlightsPreferencePage.toolTipText.Up=강조 위로 이동

HighlightsPreferencePage.toolTipText.Down=강조 아래로 이동

HighlightsPreferencePage.text.Duplicate=중복(&D)

HighlightsPreferencePage.toolTipText.Duplicate=강조 중복


###########################################################
#HyperlinkBuilder properties
HyperlinkBuilder.Column.Parameters=매개 변수

HyperlinkBuilder.DrillThrough.SelectTargetReport=대상 보고서 선택

HyperlinkBuilder.DrillThrough.SelectTargetAnchor=대상 앵커 선택

HyperlinkBuilder.DrillThrough.CreateLinkExpr=대상 책갈피에 맞는 링크 식 생성

HyperlinkBuilder.DrillThrough.ShowTargetReport=대상 보고서 표시

HyperlinkBuilder.DrillThrough.SelectFormat=대상 보고서의 형식 선택

HyperlinkBuilder.DrillThrough.Tooltip=도구 설명

HyperlinkBuilder.Step.1=1단계:

HyperlinkBuilder.Step.2=2단계:

HyperlinkBuilder.Step.3=3단계:

HyperlinkBuilder.Step.4=4단계:

HyperlinkBuilder.Step.5=5단계:

HyperlinkBuilder.Step.6=6단계:

HyperlinkBuilder.ParameterRequired=필수

HyperlinkBuilder.ParamterBuilder.Add.Title=하이퍼링크 매개 변수 추가

HyperlinkBuilder.ParamterBuilder.Edit.Title=하이퍼링크 매개 변수 편집

HyperlinkBuilder.Column.DataType=데이터 형식

HyperlinkBuilder.Column.Values=값

HyperlinkBuilder.DialogTitle=하이퍼링크 옵션

HyperlinkBuilder.ErrorMessage.InvalidReport=대상 보고서 디자인을 확인할 수 없으므로 미리 정의된 매개 변수 정보를 사용할 수 없습니다.

HyperlinkBuilder.ErrorMessage.ReportReqired=보고서 파일을 지정해야 합니다

HyperlinkBuilder.DrillThroughTargetBookmark=대상 책갈피(&T)

HyperlinkBuilder.DrillThroughLinkExpression=링크 식(&E):

HyperlinkBuilder.Label.Bookmark=책갈피(&B):

HyperlinkBuilder.Label.Format=형식:

HyperlinkBuilder.Label.LinkedExpression=링크된 식(&E):

HyperlinkBuilder.DrillThroughTargetToc=대상 보고서의 목차 항목(&B)

HyperlinkBuilder.DrillThroughSameFrame=같은 프레임(&S)

HyperlinkBuilder.DrillThroughNewWindow=새 창(&N)

HyperlinkBuilder.DrillThroughWholePage=전체 페이지(&W)

HyperlinkBuilder.DrillThroughParentFrame=상위 프레임(&P)

HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName=중복된 매개 변수 이름

HyperlinkBuilder.Label.Location=위치(&L):

HyperlinkBuilder.Label.Location.hint=(예: http://host/path, folder/file)

HyperlinkBuilder.Label.Parameters=보고서 매개 변수:

HyperlinkBuilder.Label.Report=보고서:

HyperlinkBuilder.Label.SelectType=하이퍼링크 형식 선택:

HyperlinkBuilder.DrillThroughHtml=HTML
HyperlinkBuilder.Label.Target=대상(&T):

HyperlinkBuilder.Label.Tooltip=도구 설명(&I):

HyperlinkBuilder.Radio.Bookmark=내부 책갈피(&K)

HyperlinkBuilder.DrillThroughPdf=PDF
HyperlinkBuilder.Radio.DrillThrough=드릴스루(&D)

HyperlinkBuilder.ReportDesignButton=보고서 디자인(&R):

HyperlinkBuilder.ReportDocumentButton=보고서 문서(&C):

HyperlinkBuilder.Radio.None=링크 없음(&O)

HyperlinkBuilder.Radio.Uri=&URI
HyperlinkBuilder.BrowseForFile=파일 찾아보기

HyperlinkBuilder.Button.AddParam=추가(&A)...

HyperlinkBuilder.Button.EditParam=편집(&)...

HyperlinkBuilder.Button.RemoveAllParam=모두 제거(&L)

HyperlinkBuilder.Button.RemoveParam=제거(&M)

HyperlinkBuilder.OpenExpression=식 빌더 열기

HyperlinkBuilder.FileNameError.Title=오류

HyperlinkBuilder.FileNameError.Message=선택한 파일이 잘못되었습니다.

HyperlinkBuilder.TargetReportFormat=대상 보고서 형식 지정(&F):

HyperlinkParameterBuilder.Label.DataType=데이터 형식:

HyperlinkParameterBuilder.Label.Parameter=매개 변수(&P):

HyperlinkParameterBuilder.Label.Value=값(&V):

HyperlinkParameterBuilder.Lable.Required=필수:

HyperlinkParameterBuilder.Required.Choice.No=아니오

HyperlinkParameterBuilder.Required.Choice.Yes=예

###########################################################
#HyperLinkPage properties
HyperLinkPage.Label.LnikTo=연결 대상(&L):

HyperLinkPage.Label.None=없음

HyperLinkPage.Menu.Save=하이퍼링크 변경

HyperLinkPage.Button.Text=편집(&T)...

HyperLinkPage.toolTipText.Button=하이퍼링크 옵션 열기

###########################################################
#I18nPage properties
I18nPage.text.Note=참고: 현지화를 설정하려면 속성 편집기의 리소스 탭에서 보고서를 선택하고  리소스 파일 이름을 설정하십시오.

###########################################################
#ImageBuilder properties
ImageBuilder.Chooser.Title=이미지 파일 열기

ImageBuilder.DialogTitle.Edit=이미지 항목 편집

ImageBuilder.DialogTitle.ImportFailed=가져오기 실패

ImageBuilder.DialogTitle.LoadingFailed=이미지 로드 실패

ImageBuilder.DialogTitle.New=새 이미지 항목

ImageBuilder.ErrorMessage.FileLoaded=파일 이름이 이미 있습니다. 이름을 변경하고 다시 로드하십시오.

ImageBuilder.ErrorMessage.LoadingFailed=이미지를 제대로 로드할 수 없음.

ImageBuilder.BrowserResourceDialog.Title=이미지 파일 찾아보기

ImageBuilder.BrowserResourceDialog.Message=리소스 경로 폴더에서 이미지 파일 선택

ImageBuilder.ErrorMessage.OutOfMemory=이미지가 너무 커서 메모리에 맞지 않습니다.

ImageBuilder.FileDialog.FilterMessage=지원되지 않는 이미지 파일

ImageBuilder.Button.Browser=찾아보기...

ImageBuilder.Button.Import=이미지 추가(&A)...

ImageBuilder.Button.SelectImageData=이미지 데이터 선택(&L)...

ImageBuilder.Button.Insert=삽입(&S)

ImageBuilder.Label.SelectFrom=이미지 선택 위치:

ImageBuilder.Label.EnterURI=URI 입력(&N):

ImageBuilder.Label.EnterResourceFile=리소스 파일 입력(&N):

ImageBuilder.Label.EnterExpr=동적 이미지 식 입력(&N):

ImageBuilder.Type.URI=&URI
ImageBuilder.Type.ResourceFile=공유된 리소스의 이미지 파일(&I)

ImageBuilder.Type.EmbededImage=내장된 이미지(&E)

ImageBuilder.Type.DynamicImage=동적 이미지(&D)

ImageBuilder.Description.URI=웹 상의 이미지에 링크

ImageBuilder.Description.ResourceFile=공유된 리소스의 이미지에 링크

ImageBuilder.Description.Embedded=이미지를 보고서에 포함((&M)

ImageBuilder.Description.Dynamic=데이터 집합(데이터베이스)에서 이미지 검색

ImageBuilder.Button.Browser.Tooltip=이미지 파일 찾아보기

ImageBuilder.Reload.Tooltip=이미지 다시 로드

###########################################################
#ImageEditPart properties
ImageEditPart.trans.editImage=이미지 변경

###########################################################
#ImagePageGenerator properties
ImagePageGenerator.List.AltText=ALT 텍스트

ImagePageGenerator.List.Bookmark=책갈피

ImagePageGenerator.List.Borders=테두리

ImagePageGenerator.List.General=일반

ImagePageGenerator.List.HyperLink=하이퍼링크

ImagePageGenerator.List.Reference=참조

ImagePageGenerator.List.Section=페이지 나누기

ImagePageGenerator.List.TOC=목차

ImagePageGenerator.List.Visibility=표시 유형

###########################################################
#ImportCSSStyleAction properties
ImportCSSStyleAction.text=CSS 스타일 가져오기(&I)...

###########################################################
#ImportLibraryAction properties
ImportLibraryAction.Error.CannotBeEmpty=네임스페이스는 비워 둘 수 없습니다.

ImportLibraryAction.Error.HasBeenUsed=이 네임스페이스는 사용 중입니다.

ImportLibraryAction.Error.HasDot=네임스페이스에 '.'를 사용할 수 없습니다.

ImportLibraryAction.Message.ImportSuccessfully=라이브러리 {0}을(를) 가져왔습니다.

ImportLibraryAction.Message=보고서 디자인에 이 라이브러리를 사용하시겠습니까?

ImportLibraryAction.Namespace=네임스페이스:

ImportLibraryAction.Prefix=접두사:

ImportLibraryAction.Title.ImportSuccessfully=성공적으로 가져오기

ImportLibraryAction.Dialog.Titile=라이브러리 사용

ImportLibraryAction.Dialog.Message=보고서에 포함할 라이브러리를 선택하십시오.

###########################################################
#ImportLibraryDialog properties
ImportLibraryDialog.Title=라이브러리 사용

###########################################################
#ImportValueDialog properties
ImportValueDialog.Label.SelectColumn=열 선택(&C):

ImportValueDialog.Label.SelectDataSet=데이터 집합 선택(&D):

ImportValueDialog.Label.SelectValue=추가할 값 선택 또는 입력(&S)

ImportValueDialog.Title=값 가져오기

ImportValueDialog.Button.Add.ToolTip=추가

ImportValueDialog.Button.AddAll.ToolTip=모두 추가

ImportValueDialog.Button.Remove.ToolTip=제거

ImportValueDialog.Button.RemoveAll.ToolTip=모두 제거
###########################################################
#IncludeDetailAction properties
IncludeDetailAction.actionMsg.includeDetail=상세 정보(&D)

###########################################################
#IncludeFooterAction properties
IncludeFooterAction.actionMsg.includeFooter=바닥글(&F)

###########################################################
#IncludeHeaderAction properties
IncludeHeaderAction.actionMsg.includeHeader=머리글(&H)

###########################################################
#IncludeListDetailAction properties
IncludeListDetailAction.actionMsg.includeDetail=상세 정보 포함

###########################################################
#IncludeListFooterAction properties
IncludeListFooterAction.actionMsg.includeFooter=바닥글 포함

###########################################################
#IncludeListHeaderAction properties
IncludeListHeaderAction.actionMsg.includeHeader=머리글 포함

###########################################################
#IncludeTableGroupFooterAction properties
IncludeTableGroupFooterAction.actionMsg.includeFooter=그룹 바닥글

###########################################################
#IncludeTableGroupHeaderAction properties
IncludeTableGroupHeaderAction.actionMsg.includeHeader=그룹 머리글

###########################################################
#InputParameterDailog properties
InputParameterDailog.dialog.title.required=필수 매개 변수 - 반드시 값이 있어야 함

InputParameterDailog.dialog.title=매개 변수 입력

InputParameterDailog.parameter.noparameter=이 보고서와 관련된 매개 변수가 없습니다.

InputParameterDialog.ErrorMessage.InvalidFile=보고서에 오류가 있습니다.

InputParameterDailog.parameter.null=널(null) 값

InputParameterDialog.ErrorTitle.Error=오류

InputParameterDialog.boolean.checked=예

###########################################################
#InsertAction properties
InsertAction.text=요소 삽입(&N)

###########################################################
#InsertColumnAction properties
InsertColumnAction.actionMsg.insert=삽입

###########################################################
#InsertColumnLeftAction properties
InsertColumnLeftAction.actionMsg.columnToLeft=열을 왼쪽으로(&L)

###########################################################
#InsertColumnRightAction properties
InsertColumnRightAction.actionMsg.columnToRight=열을 오른쪽으로(&R)

###########################################################
#InsertExpressionMenuAction properties
InsertExpressionMenuAction.text.Expression=식...

###########################################################
#InsertGroupAction properties
InsertGroupAction.actionMsg.group=그룹 삽입(&G)

InsertGroupAction.stackMsg.addGroup=그룹 추가

###########################################################
#InsertGroupActionFactory properties
InsertGroupActionFactory.label.insertGroup=그룹 삽입(&G)

###########################################################
#InsertInLayoutAction properties
InsertInLayoutAction.action.text=레이아웃에 삽입(&N)

###########################################################
#InsertListGroupAction properties
InsertListGroupAction.actionMsg.insertGroup=그룹 삽입

InsertListGroupAction.stackMsg.insertListGroup=목록 그룹 삽입

###########################################################
#InsertPasteColumnAction properties
InsertPasteColumnAction.text=복사한 열 삽입(&S)

###########################################################
#InsertPositionGroupAction properties
InsertPositionGroupAction.Label.Above=위(&A)

InsertPositionGroupAction.Label.Below=아래(&B)

InsertPositionGroupAction.Label.Into=다음으로

###########################################################
#InsertRowAboveAction properties
InsertRowAboveAction.actionMsg.rowAbove=위(&A)

###########################################################
#InsertRowAction properties
InsertRowAction.actionMsg.insert=삽입(&I)

###########################################################
#InsertRowBelowAction properties
InsertRowBelowAction.actionMsg.rowBelow=아래(&B)

###########################################################
#IReportGraphicConstants properties
IReportGraphicConstants.ImageType.All=모든 이미지 파일(*.bmp,*.JPG,*.JPEG,*.GIF,*.TIF,*.PNG,*.ICO)

IReportGraphicConstants.ImageType.Bmp=비트맵 파일(*.bmp)

IReportGraphicConstants.ImageType.Gif=GIF 파일(*.GIF)

IReportGraphicConstants.ImageType.Icon=아이콘 파일(*.ICO)

IReportGraphicConstants.ImageType.Jpg=JPEG 파일(*.JPG,*.JPEG)

IReportGraphicConstants.ImageType.Png=PNG 파일(*.PNG)

IReportGraphicConstants.ImageType.Tif=TIF 파일(*.TIF)

###########################################################
#JSDocumentProvider properties
JSDocumentProvider.Content.Default=새 내용

JSDocumentProvider.document.newName=새 내용

###########################################################
#JSEditor properties
JSEditor.cmb.NewEventFunction=새 이벤트 함수...

JSEditor.Label.Script=스크립트:

JSEditor.Button.Validate=스크립트 확인

JSEditor.Button.Reset=스크립트 재설정

JSEditor.Button.Help=설명 표시

JSEditor.Display.DefaultText=//JS 스크립트 편집기 \n

JSEditor.Display.NoExpression=이 요소에 메소드가 없습니다.

JSEditor.Input.trial=시도

JSEditor.Text.NoText=텍스트 없음

JSEditor.Text.NoDescription=이 메서드에 대한 설명이 없습니다.

JSEditor.Validate.Title=유효성 검사

JSEditor.Validate.NoError=현재 스크립트에 구문 오류가 없습니다.

JSEditor.Folding.Group=접기

JSEditor.Folding.ExpandAll.label=모두 확대

JSEditor.Folding.CollapseAll.label=모두 축소

JSEditor.Folding.CollapseMethods.label=메서드 축소

JSEditor.Folding.CollapseComments.label=설명 축소

###########################################################
#label properties
label.defaultValue=(기본값)

label.input=입력

label.inputOutput=입력/출력

label.no=아니오

label.output=출력

label.redo=다시 실행

label.undo=실행 취소

label.yes=예

###########################################################
#LabelEditPart properties
LabelEditPart.Figure.Default=레이블

###########################################################
#LabelPageGenerator properties
LabelPageGenerator.List.Bookmark=책갈피

LabelPageGenerator.List.Borders=테두리

LabelPageGenerator.List.General=일반

LabelPageGenerator.List.HyperLink=하이퍼링크

LabelPageGenerator.List.I18n=현지화

LabelPageGenerator.List.Margin=여백

LabelPageGenerator.List.Padding=안쪽 여백

LabelPageGenerator.List.Section=페이지 나누기

LabelPageGenerator.List.TOC=목차

LabelPageGenerator.List.Visibility=표시 유형

###########################################################
#LayoutConfigurationBlock properties
LayoutConfigurationBlock.Default.Unit.Auto=자동

LayoutConfigurationBlock.Default.Unit.Label=기본 단위(&F):

###########################################################
#LayoutEditor properties
LayoutEditor.ErrorMessage.OpenError=편집기를 여는 도중 오류가 발생했습니다.

LayoutEditor.Page.Layout=기본

LayoutEditor.Page.MasterPage=마스터 페이지

LayoutEditor.Page.Source=XML 소스

###########################################################
#LayoutPageAction properties
LayoutPageAction.actionText.layout=레이아웃

###########################################################
#LibrariesNodeProvider properties
LibrariesNodeProvider.DisplayLabel.Libraries=라이브러리

###########################################################
#LibraryExplorerView properties
LibraryExplorerView.defaultText.notAvailable=리소스를 사용할 수 없습니다.

###########################################################
#LibraryExplorerProvider properties
LibraryExplorerProvider.FolderNotExist=기본 설정에 정의된 BIRT 리소스 폴더가 없습니다.

###########################################################
#LibraryPage properties
LibraryPage.Label.Namespace=네임스페이스(&N):

LibraryPage.Label.Path=경로(&P):

###########################################################
#LibraryProvider properties
LibraryProvider.openDefinedLibrary.error.dialog.message=정의된 라이브러리 오류 열기

LibraryProvider.openDefinedLibrary.error.dialog.title=오류

###########################################################
#ListBandControlFigure properties
ListBandControlFigure.tooltip.GroupFooter=목록 그룹 - 바닥글

ListBandControlFigure.tooltip.GroupHeader=목록 그룹 - 머리글

ListBandControlFigure.tooltip.ListDetail=목록 - 상세 정보

ListBandControlFigure.tooltip.ListFooter=목록 - 바닥글

ListBandControlFigure.tooltip.ListHeader=목록 - 머리글

###########################################################
#ListBandProvider properties
ListBandProvider.action.text=그룹 삽입(&N)

###########################################################
#ListBandProxy properties
ListBandProxy.list.detail=상세 정보

ListBandProxy.list.footer=바닥글

ListBandProxy.list.groupFooter=GF

ListBandProxy.list.groupHeader=GH

ListBandProxy.list.header=머리글

###########################################################
#ListContainProcess properties
ListContainProcess.action.New=새로 만들기...

ListContainProcess.action.NewRow=새 행

ListContainProcess.action.NewSubSection=새 하위 섹션

###########################################################
#ListEditPart properties
ListEditPart.GUIDEHANDLE_TEXT = 목록

###########################################################
#ListFigure properties
ListFigure.BORDER_TEXT=목록

###########################################################
#ListHandleAdapt properties
ListHandleAdapt.transLabel.insertGroup=그룹 삽입

###########################################################
#ListPageGenerator properties
ListPageGenerator.List.Bookmark=책갈피

ListPageGenerator.List.Borders=테두리

ListPageGenerator.List.Font=글꼴

ListPageGenerator.List.General=일반

ListPageGenerator.List.Section=페이지 나누기

ListPageGenerator.List.TOC=목차

ListPageGenerator.List.Visibility=표시 유형


###########################################################
#MapPage properties
MapPage.label.add=추가(&A)...

MapPage.label.condition=조건

MapPage.label.delete=제거(&R)

MapPage.label.displayValue=값 표시:

MapPage.label.mapList=맵 목록:

MapPage.transName.addMapRule=맵 규칙 추가

MapPage.transName.deleteMapRule=맵 규칙 삭제

MapPage.transName.editMapRule=맵 규칙 편집

MapPage.transName.moveDownRule=맵 규칙 아래로 이동

MapPage.transName.moveUpMapRule=맵 규칙 위로 이동

MapPage.toolTipText.moveUp=맵 규칙 위로 이동

MapPage.toolTipText.moveDown=맵 규칙 아래로 이동

MapPage.label.duplicate=중복(&C)

MapPage.toolTipText.duplicate=맵 규칙 중복

MapPage.transName.Duplicate=맵 규칙 중복

###########################################################
#MapPreferencePage properties
MapPreferencePage.displayname.Condition=조건

MapPreferencePage.displayname.DisplayValue=값 표시

MapPreferencePage.displayname.Title=맵

MapPreferencePage.text.Add=추가(&A)...

MapPreferencePage.text.Delete=제거(&R)

MapPreferencePage.text.Description=맵 규칙:

MapPreferencePage.text.Edit=편집(&E)...

MapPreferencePage.toolTipText.Up=맵 규칙 위로 이동

MapPreferencePage.toolTipText.Down=맵 규칙 아래로 이동

MapPreferencePage.text.Duplicate=중복(&D)

MapPreferencePage.toolTipText.Duplicate=맵 규칙 중복

###########################################################
#MapRuleBuilderDialog properties
MapRuleBuilder.button.add=추가(&A)

MapRuleBuilder.button.add.tooltip=값 목록에 값 추가

MapRuleBuilder.button.delete=제거(&R)

MapRuleBuilder.button.delete.tooltip=목록에서 선택한 값 삭제

MapRuleBuilder.button.deleteall=모두 제거(&O)

MapRuleBuilder.button.deleteall.tooltip=목록에서 모든 값 제거

MapRuleBuilder.button.edit=편집(&E)...

MapRuleBuilder.button.edit.tooltip=값 목록에서 선택한 값 편집

MapRuleBuilder.DialogTitle.New=새 맵 규칙

MapRuleBuilder.DialogTitle.Edit=맵 규칙 편집

MapRuleBuilder.Button.ResourceKey=리소스 키(&K):

MapRuleBuilder.Button.Reset=재설정(&R)

MapRuleBuilder.Button.Browse=찾아보기(&B)...

MapRuleBuilder.Button.Browse.Tooltip=키를 찾아보고 선택합니다.

MapRuleBuilder.label.value=값(&V):

MapRuleBuilder.list.item1=값

MapRuleBuilder.MsgDlg.Msg=경고

MapRuleBuilder.MsgDlg.Title=값을 입력해야 합니다.

MapRuleBuilder.text.AND=및

MapRuleBuilderDialog.text.Between=사이에 있음

MapRuleBuilderDialog.text.Condition=다음 조건이 참인 경우(&I):

MapRuleBuilderDialog.text.Display=다음 값 표시(&T):

MapRuleBuilderDialog.text.ET=같음

MapRuleBuilderDialog.text.GE=크거나 같음

MapRuleBuilderDialog.text.GT=큼

MapRuleBuilderDialog.text.LE=작거나 같음

MapRuleBuilderDialog.text.Like=유사함

MapRuleBuilderDialog.text.LT=작음

MapRuleBuilderDialog.text.NE=같지 않음

MapRuleBuilderDialog.text.NotBetween=사이에 있지 않음

MapRuleBuilderDialog.text.NotNull=널(null)이 아님

MapRuleBuilderDialog.text.Null=널(null)임

MapRuleBuilderDialog.text.Title=맵

MapRuleBuilderDialog.MsgDlg.Title=경고

MapRuleBuilderDialog.MsgDlg.Msg=값을 입력해야 합니다.

###########################################################
#SortkeyBuilder dialog properties
SortkeyBuilder.DialogTitle.New=새 정렬 키

SortkeyBuilder.DialogTitle.Edit=정렬 키 편집

SortkeyBuilder.DialogMessage.New=정렬 키를 만들기 위해 다음 내용을 입력하십시오.

SortkeyBuilder.DialogMessage.Edit=정렬 키를 업데이트하기 위해 다음 내용을 편집하십시오.

SortkeyBuilder.DialogTitle.Label.Prompt=정렬할 키 입력:

SortkeyBuilder.DialogTitle.Label.Key=키(&K):

SortkeyBuilder.DialogTitle.Label.Direction=방향(&D):

SortkeyBuilder.Label.Locale=로케일(&L):

SortkeyBuilder.Label.Strength=수준(&S):

SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=식 빌더 열기

SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=오류

SortkeyBuilder.Locale.Auto=자동

SortkeyBuilder.Strength.ASCII=ASCII
SortkeyBuilder.Strength.PRIMARY=주

SortkeyBuilder.Strength.SECONDARY=보조

SortkeyBuilder.Strength.TERTIARY=3차

SortkeyBuilder.Strength.QUATERNARY=4차

SortkeyBuilder.Strength.IDENTICAL=동일

###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=새 필터 조건

FilterConditionBuilder.DialogTitle.Edit=필터 조건 편집

FilterConditionBuilder.DialogMessage.New=필터 조건을 만들기 위해 내용을 입력하십시오.

FilterConditionBuilder.DialogMessage.Edit=필터 조건을 업데이트하기 위해 다음 내용을 편집하십시오.

FilterConditionBuilder.choice.ValueOfThisDataItem=이 데이터 항목 값

FilterConditionBuilder.tooltip.ExpBuilder=식 빌더 열기

FilterConditionBuilder.text.AND=및

FilterConditionBuilder.text.Condition=필터 조건 입력:

FilterConditionBuilder.Shell.Title=필터 조건

FilterConditionBuilder.list.item1=값

FilterConditionBuilder.button.edit=편집(&E)...

FilterConditionBuilder.button.delete=제거(&M)

FilterConditionBuilder.button.deleteall=모두 제거(&O)

FilterConditionBuilder.button.add=추가(&A)

FilterConditionBuilder.button.edit.tooltip=값 목록에서 선택한 값 편집

FilterConditionBuilder.button.delete.tooltip=목록에서 선택한 값 삭제

FilterConditionBuilder.button.deleteall.tooltip=목록에서 모든 값 삭제

FilterConditionBuilder.button.add.tooltip=값 목록에 값 추가

FilterConditionBuilder.label.value=값(&V):

FilterConditionBuilder.multipleValue.label=값(&V)

FilterConditionBuilder.multipleValue.button.add=추가(&A)

FilterConditionBuilder.multipleValue.button.remove=제거(&R)

FilterConditionBuilder.multipleValue.button.removeAll=모두 제거(&M)

FilterConditionBuilder.multipleValue.button.edit=편집(&D)...

FilterConditionBuilder.Button.UpdateAggregation=합계 다시 계산(&T)

###########################################################
#MarignPropertyDescriptor properties
MarignPropertyDescriptor.error.message=입력한 값이 너무 큽니다.

MarignPropertyDescriptor.error.title=너무 큰 값

###########################################################
#MasterPageAction properties
MasterColumnsPropertyDescriptor.Button.Text.Custom=사용자 지정(&C)

MasterColumnsPropertyDescriptor.Button.Text.OneColumn=1개(&O)

MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns=3개(&R)

MasterColumnsPropertyDescriptor.Button.Text.TwoColumns=2개(&T)

MasterColumnsPropertyDescriptor.Combo.Text.Column.Number=열 개수(&N):

MasterPageAction.actionText.masterPage=마스터 페이지

###########################################################
#MasterPageGenerator properties
MasterPageGenerator.List.Columns=열

MasterPageGenerator.List.General=일반

MasterPageGenerator.List.Borders=테두리

MasterPageGenerator.List.Header&Footer=머리글/바닥글

MasterPageGenerator.List.Margins=여백

###########################################################
#MasterPageGeneralPage properties
MasterPageGeneralPage.Trans.SetType=집합 형식

###########################################################
#MasterPagePaletteFactory properties
MasterPagePaletteFactory.Autotext.TotalPageCount=전체 페이지 수

MasterPagePaletteFactory.Autotext.Author_Page_Date=만든 이\#, 페이지\#, 날짜\#

MasterPagePaletteFactory.Autotext.Confidential_Page=기밀, 페이지\#

MasterPagePaletteFactory.Autotext.CreatedBy=만든 사람

MasterPagePaletteFactory.Autotext.CreatedOn=만든 날짜

MasterPagePaletteFactory.Autotext.Date=날짜

MasterPagePaletteFactory.Autotext.Filename=파일 이름

MasterPagePaletteFactory.Autotext.Label=AutoText
MasterPagePaletteFactory.Autotext.LastPrinted=마지막 인쇄

MasterPagePaletteFactory.Autotext.Page=페이지

MasterPagePaletteFactory.Autotext.PageXofY=n / m페이지

MasterPagePaletteFactory.AutotextTooltip.TotalPageCount=전체 페이지 수 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate=만든 이 번호, 날짜 번호 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage=기밀 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.CreatedBy=만든 사람 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.CreatedOn=만든 날짜 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.Date=날짜 보고서 항목

MasterPagePaletteFactory.AutoTextTooltip.Filename=파일 이름 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.LastPrinted=마지막 인쇄 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.Page=페이지 보고서 항목

MasterPagePaletteFactory.AutotextTooltip.PageXofY=n / m페이지 보고서 항목

MasterPagePaletteFactory.elementLabel.authorPageDate=만든 이 번호, 페이지 번호, 날짜

MasterPagePaletteFactory.elementLabel.confidentialPage=기밀, 페이지

MasterPagePaletteFactory.elementLabel.createdBy=만든 사람

MasterPagePaletteFactory.elementLabel.createdOn=만든 날짜

MasterPagePaletteFactory.elementLabel.date=날짜

MasterPagePaletteFactory.elementLabel.fileName=파일 이름

MasterPagePaletteFactory.elementLabel.lastPrinted=마지막 인쇄

MasterPagePaletteFactory.elementLabel.page=페이지

MasterPagePaletteFactory.elementLabel.pageOf=n / m페이지

MasterPagePaletteFactory.paletteDrawerName.autoText=AutoText
MasterPagePaletteFactory.ToolTip.Grid=눈금 보고서 항목

MasterPagePaletteFactory.ToolTip.Text=텍스트 보고서 항목

MasterPagePaletteFactory.Autotext.Variable=변수

MasterPagePaletteFactory.AutotextTooltip.Variable=변수 항목

###########################################################
#MasterPageProcess properties
MasterPageProcess.action.Edit=편집

MasterPageProcess.action.New=새로 만들기...

MasterPageProcess.text.CannotDelete=디자인에는 마스터 페이지가 하나만 포함되며 삭제할 수 없습니다.

MasterPageProcess.text.Display=페이지 설정 대화 상자를 표시합니다.

MasterPageProcess.text.DisplayName=MasterPage

###########################################################
#MergeAction properties
MergeAction.actionMsg.merge=셀 병합(&M)

###########################################################
#ModulePage properties
ModulePage.ErrorMessage.Duplicate=이름이 기존 이름과 중복됩니다.

ModulePage.ErrorMessage.WrongExtensionName=이름은 ".properties"로 끝나야 합니다.

ModulePage.ErrorMessage.InfoExtensionName=".properties"로 끝나는 이름을 입력하십시오.

ModulePage.text.Add=추가(&A)...

ModulePage.text.ExistingFile=기존 리소스 파일:

ModulePage.title.AddNewFile=새 리소스 파일 추가

ModulePage.Resourcefile.Dialog.Title=리소스 파일 찾아보기

ModulePage.Resourcefile.Dialog.Message=리소스 파일 또는 폴더를 선택하고 생성할 파일 또는 폴더의 이름을 입력하십시오.

ModulePage.text.Path=경로(&P):

###########################################################
#MultiPageEditorContributor properties
MultiPageEditorContributor.actionText.sampleAction=샘플 작업

MultiPageEditorContributor.actionToolTip.sampleActionToolTip=샘플 작업 도구 설명

MultiPageEditorContributor.actionToolTipText.sampleActionToolTip=샘플 작업 도구 설명

MultiPageEditorContributor.dialogMsg.sampleActionExecuted=샘플 작업 실행됨

MultiPageEditorContributor.menuText.editorMenu=편집기 및 메뉴

###########################################################
#NewLibraryWizard properties
NewLibraryWizard.choicePageTitle.report=라이브러리

NewLibraryWizard.displayName.NewReportFileExtension=.rptlibrary
NewLibraryWizard.displayName.NewReportFileNamePrefix=새 라이브러리

NewLibraryWizard.error.NoProject=작업 영역에 열려 있는 프로젝트가 없습니다. 파일->새로 만들기->프로젝트를 선택하여 새 프로젝트를 만들거나 기존 프로젝트를 여십시오.

NewLibraryWizard.pageDescription.createNewReport=새 라이브러리 생성

NewLibraryWizard.taskName.openFileForEdit=편집할 파일을 여는 중...

NewLibraryWizard.text.ConfirmSwitchPerspective=원근감 전환 확인

NewLibraryWizard.text.Container=컨테이너 "

NewLibraryWizard.text.CreateReport=새 라이브러리를 생성합니다.

NewLibraryWizard.text.Creating=만드는 중

NewLibraryWizard.text.DoesNotExist="이(가) 없습니다.

NewLibraryWizard.text.OpenFileForEditing=편집할 파일을 여는 중...

NewLibraryWizard.text.SwitchPerspective=이 형식의 파일은 {0} 원근감과 관련되어 있습니다. 이 원근감으로 지금 전환하시겠습니까?

NewLibraryWizard.title.Choose=라이브러리 생성 방법 선택

NewLibraryWizard.title.Error=오류

NewLibraryWizard.title.New=새 라이브러리

NewLibraryWizard.title.Report=라이브러리

NewLibraryWizard.title.ReportWizard=라이브러리 마법사

NewLibraryWizard.title.Template=TemplateChoicePage
NewLibraryWizard.title.WizardPage=WizardPage
NewLibraryWizard.widowTitle.new=새로 만들기

NewLibraryWizard.wizardPageTitle.report=라이브러리

###########################################################
#NewReportProjectWizard properties

NewReportProjectWizard.bin=bin
NewReportProjectWizard.caseVariantExistsError=파일 시스템은 대/소문자를 구분하지 않습니다. ''{0}''와(과) 충돌하는 기존 프로젝트가 있습니다.

NewReportProjectWizard.description=새 보고서 프로젝트를 생성합니다.

NewReportProjectWizard.errorMessage=새 보고서 프로젝트를 생성하는 중 문제가 발생했습니다.

NewReportProjectWizard.errorOpeningWindow=창을 여는 중 문제가 발생했습니다.

NewReportProjectWizard.internalError=내부 오류: {0}

NewReportProjectWizard.javaProject=Java 프로젝트 생성

NewReportProjectWizard.perspSwitchMessage=이러한 종류의 프로젝트는 {0} 원근감과 관련되어 있습니다. 이 원근감으로 지금 전환하시겠습니까?

NewReportProjectWizard.perspSwitchTitle=원근감 전환 확인

NewReportProjectWizard.projectSetting=프로젝트 설정

NewReportProjectWizard.src=src
NewReportProjectWizard.title=보고서 프로젝트

NewReportProjectWizard.windowTitle=새 보고서 프로젝트

###########################################################
#NewReportWizard properties
NewReportWizard.choicePageTitle.report=보고서

NewReportWizard.displayName.NewReportFileExtension=.rptdesign
NewReportWizard.displayName.NewReportFileNamePrefix=new_report
NewReportWizard.error.NoProject=작업 영역에 열려 있는 프로젝트가 없습니다. 파일->새로 만들기->프로젝트를 선택하여 새 프로젝트를 만들거나 기존 프로젝트를 여십시오.

NewReportWizard.pageDescription.createNewReport=새 보고서 생성

NewReportWizard.pageDescription.selectReportTemplate=만들려는 보고서의 유형을 선택하십시오.

NewReportWizard.taskName.openFileForEdit=편집할 파일을 여는 중...

NewReportWizard.text.ConfirmSwitchPerspective=원근감 전환 확인

NewReportWizard.text.Container=컨테이너 "

NewReportWizard.text.CreateReport=새 보고서를 생성합니다.

NewReportWizard.text.Creating=만드는 중

NewReportWizard.text.DoesNotExist="이(가) 없습니다.

NewReportWizard.text.OpenFileForEditing=편집할 파일을 여는 중...

NewReportWizard.text.SelectTemplate=생성하려는 보고서 유형을 선택하십시오.

NewReportWizard.text.SwitchPerspective=이 형식의 파일은 {0} 원근감과 관련되어 있습니다. 이 원근감으로 지금 전환하시겠습니까?

NewReportWizard.title.Choose=보고서 생성 방법 선택

NewReportWizard.title.Error=오류

NewReportWizard.title.New=새 보고서

NewReportWizard.title.Report=보고서

NewReportWizard.title.ReportWizard=보고서 마법사

NewReportWizard.title.Template=TemplateChoicePage
NewReportWizard.title.WizardPage=WizardPage
NewReportWizard.widowTitle.new=새로 만들기

NewReportWizard.wizard.msgDirErr=대상 디렉터리가 없지만 만들 수 없습니다.


###########################################################
#NewResourceFileDialog properties
NewResourceFileDialog.label.NewFile=새 파일 이름(&N):

NewResourceFileDialog.ErrorMessage=파일 이름은 .properties로 끝나야 합니다.

NewResourceFileDialog.ErrorMessageInvalid=잘못된 파일 이름

NewResourceFileDialog.FileExists={0}이(가) 있습니다. 덮어쓰시겠습니까?

###########################################################
#ProjectFileDialog properties
ProjectFileDialog.ErrorMessage=파일은 디렉터리가 아닌 파일이어야 합니다.

ProjectFileDialog.Title=프로젝트 파일 선택

ProjectFileDialog.Message=현재 프로젝트에서 파일을 선택합니다.

ProjectFileDialog.Text.Menu=메뉴

###########################################################
#ProjectSelectionDialog properties
ProjectSelectionDialog.Message=구성할 프로젝트 선택:

ProjectSelectionDialog.Text.SpecialSettings=프로젝트별 설정이 있는 프로젝트만 표시

ProjectSelectionDialog.Title=프로젝트별 구성

###########################################################
#ResourceFilterDialog properties
ResourceFilterDialog.Title=보고서 요소 필터

ResourceFilterDialog.Lable.Description=필터 설명:

ResourceFilterDialog.Lable.Viewer=보기에서 제외할 요소를 선택합니다.

###########################################################
#NewSectionDialog properties
NewSectionDialog.displayname.Container=컨테이너

NewSectionDialog.displayname.Data=데이터

NewSectionDialog.displayname.Grid=눈금

NewSectionDialog.displayname.Image=이미지

NewSectionDialog.displayname.Label=레이블

NewSectionDialog.displayname.Line=선

NewSectionDialog.displayname.List=목록

NewSectionDialog.displayname.Rectangle=사각형

NewSectionDialog.displayname.Table=테이블

NewSectionDialog.displayname.Text=텍스트

NewSectionDialog.text.Prefix=사용 가능한 섹션:

NewSectionDialog.title.insertElement=요소 삽입

NewSectionDialog.Title=요소 삽입

###########################################################
#NewTemplateWizard properties
NewTemplateWizard.displayName.NewReportFileNamePrefix=새 템플릿

NewTemplateWizard.pageDescription.createNewTemplate=새 보고서 템플릿을 생성합니다.

NewTemplateWizard.text.CreateReport=새 템플릿을 생성합니다.

NewTemplateWizard.text.Creating=만드는 중

NewTemplateWizard.text.OpenFileForEditing=편집할 템플릿 파일을 여는 중...

NewTemplateWizard.title.New=새 템플릿

NewTemplateWizard.title.Report=보고서

NewTemplateWizard.title.Template=템플릿

NewTemplateWizard.title.WizardPage=WizardPage

###########################################################
#NoneAction properties
NoneAction.text=없음

###########################################################
#OutputColumnDefnPage properties
OutputColumnDefnPage.description=반입 스크립트가 생성하는 출력 열 정의 입력:

OutputColumnDefnPage.OutputColumns.Tooltip=데이터 행 내 열 보기 및 정의

OutputColumnPage.error.createNewColumn=새 열이 추가되는 동안 오류가 발생했습니다.

OutputColumnPage.error.invalidSelection=잘못된 테이블 항목이 선택되었습니다.

###########################################################
#ColumnDefPage properties
ColumnDefPage.description=스크립트 데이터 집합의 출력 열 정의 페이지

ColumnDefPage.title=출력 열

###########################################################
#ResultSetColumnPage properties
ResultSetColumnPage.button.add=추가(&A)...

ResultSetColumnPage.button.edit=편집(&E)...

ResultSetColumnPage.button.delete=삭제(&D)

ResultSetColumnPage.button.up=위로(&U)

ResultSetColumnPage.button.down=아래로(&W)

ResultSetColumnPage.inputDialog.newColumn.title=새 스크립트 데이터 집합 열

ResultSetColumnPage.inputDialog.editColumn.title=스크립트 데이터 집합 열 편집

ResultSetColumnPage.inputDialog.label.columnName=열 이름(&C):

ResultSetColumnPage.inputDialog.label.dataType=데이터 형식(&T):

ResultSetColumnPage.inputDialog.label.alias=열 별칭(&A):

ResultSetColumnPage.inputDialog.label.displayName=표시 이름(&D):

ResultSetColumnPage.inputDialog.label.displayNameKey=표시 이름 키(&K):

ResultSetColumnPage.inputDialog.warning.emptyColumnName=열 이름을 입력해야 합니다.

ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias=열 이름 및 별칭이 같을 수 없습니다.

ResultSetColumnPage.inputDialog.error.duplicatedColumnName=열 이름 "{0}"이(가) 이미 사용되고 있습니다.

ResultSetColumnPage.inputDialog.error.duplicatedAlias=열 별칭 "{0}"이(가) 이미 사용되고 있습니다.

###########################################################
#OutputColumnsPage properties
OutputColumnPage.OutputColumns.Tooltip=데이터 행 내 열 보기 및 정의

OutputColumnPage.toolTipText.Edit=출력 열 편집

OutputColumnPage.title.OutputColumnInputDialog=출력 열

OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn=새 출력 열

OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn=출력 열 편집

OutputColumnPage.OutputColumns.DuplicatedName=이름이 이미 존재합니다!

OutputColumnsPage.description=출력 열 정의:

OutputColumnsPage.DataTypeConvert.Note=참고

OutputColumnsPage.DataTypeConvert.Message=이 전환으로 인해 실행 시 정밀도가 낮아지거나 오류가 발생할 수 있습니다. 적용하시겠습니까?

###########################################################
#PageBreakPreferencePage properties
PageBreakPreferencePage.displayname.Title=페이지 나누기

###########################################################
#PageSetAction properties
PageSetAction.text.code=스크립트(&S)

PageSetAction.Text.EditCode=스크립트 편집(&S)...

PageSetAction.text.layout=레이아웃(&L)

PageSetAction.text.master=마스터(&M)

PageSetAction.text.normal=기본(&N)

PageSetAction.text.preview=미리 보기(&P)

###########################################################
#PageSetupOutlineProcess properties
PageSetupOutlineProcess.action.Edit=페이지 설정 편집

PageSetupOutlineProcess.action.New=새 MasterPage

PageSetupOutlineProcess.text.Display=페이지 설정 대화 상자를 표시합니다.

PageSetupOutlineProcess.text.Switch=페이지 마스터 편집기로 전환합니다.

###########################################################
#ParameterDialog properties
ParameterDialog.Boolean.False=거짓

ParameterDialog.Boolean.True=참

ParameterDialog.Button.ChangeFormat=변경(&G)...

ParameterDialog.Button.CreateDataSet=새로 생성(&W)...

ParameterDialog.Button.ImportValue=값 가져오기(&U)...

ParameterDialog.Button.RemoveDefault=기본값 제거(&A)

ParameterDialog.Button.SetDefault=기본값으로 설정(&A)

ParameterDialog.CheckBox.IsRequired=필요(&Q)

ParameterDialog.CheckBox.DoNotEchoInput=입력 반향 안 함(&O)

ParameterDialog.CheckBox.Hidden=숨김(&H)

ParameterDialog.CheckBox.Distinct=중복 값 허용(&L)

ParameterDialog.Choice.NoDefault=기본값 없음

ParameterDialog.Choice.NullValue=널(Null) 값

ParameterDialog.Choice.BlankValue=빈 값

ParameterDialog.Choice.None=없음

ParameterDialog.Choice.DisplayText=표시 텍스트

ParameterDialog.Choice.ValueColumn=값 열

ParameterDialog.Choice.ASCENDING=오름차순

ParameterDialog.Choice.DESCENDING=내림차순

ParameterDialog.Choice.SelectValue=<값 선택...>

ParameterDialog.Column.Default=기본값

ParameterDialog.Column.DisplayText=표시 텍스트

ParameterDialog.Column.DisplayTextKey=표시 텍스트 키

ParameterDialog.Column.Value=값

ParameterDialog.DefaultValue.Add=추가(&A)

ParameterDialog.DisplayLabel.Combo=콤보 상자

ParameterDialog.DisplayLabel.List=목록 상자

ParameterDialog.ErrorMessage.CanootBeBlank=값을 비워둘 수 없습니다.

ParameterDialog.ErrorMessage.CanootBeNull=값이 널(null)이 될 수 없습니다.

ParameterDialog.ErrorMessage.DuplicatedLabel=표시 텍스트가 이미 있습니다.

ParameterDialog.ErrorMessage.DuplicatedLabelKey=표시 텍스트 키가 이미 있습니다.

ParameterDialog.ErrorMessage.DuplicatedName=이름이 이미 사용 중입니다.

ParameterDialog.ErrorMessage.DuplicatedValue=값이 이미 있습니다.

ParameterDialog.ErrorMessage.EmptyName=이름이 비어 있습니다.

ParameterDialog.ErrorMessage.InvalidListLimit=제한 값은 0과 {0} 사이여야 합니다.

ParameterDialog.ErrorMessage.MismatchDataType=값이 데이터 형식에 맞지 않습니다.

ParameterDialog.ErrorMessage.NoAvailableColumn=사용할 수 있는 열이 없습니다.

ParameterDialog.ErrorMessage.ValueColumnEmpty=값 열을 지정해야 합니다.

ParameterDialog.ErrorMessage.NoDefaultValue=기본값을 설정해야 합니다.

ParameterDialog.ErrorTitle.InvalidListLimit=잘못된 목록 제한

ParameterDialog.ExpressionEditor.Title=식 편집

ParameterDialog.Flag.Default=기본값

ParameterDialog.Group.MoreOption=표시

ParameterDialog.Group.Properties=속성

ParameterDialog.Label.DataType=데이터 형식(&D):

ParameterDialog.SelectionDialog.Edit=선택 항목 편집

ParameterDialog.Label.DefaultValue=기본값(&F):

ParameterDialog.Label.DisplayPrompt=프롬프트 표시:

ParameterDialog.Label.DisplayType=표시 형식(&T):

ParameterDialog.Label.Format=다음으로 형식 지정:

ParameterDialog.Label.HelpText=도움말 텍스트(&X):

ParameterDialog.Label.ListOfValue=선택 목록 값

ParameterDialog.Label.SortGroup=정렬

ParameterDialog.Label.Name=이름(&N):

ParameterDialog.Label.None=<없음>

ParameterDialog.Label.Null=널(Null)

ParameterDialog.Label.Preview=형식 미리 보기

ParameterDialog.Label.PromptText=프롬프트 텍스트(&P):

ParameterDialog.Label.SelectDataSet=데이터 집합(&A):

ParameterDialog.Label.SortKey=정렬 기준(&B):

ParameterDialog.Label.SortDirection=정렬 방향(C):

ParameterDialog.SelectionDialog.New=새 선택 항목

ParameterDialog.SelectionDialog.Label.DisplayTextKey=표시 텍스트 키(&K):

ParameterDialog.SelectionDialog.Label.DisplayText=표시 텍스트(&T):

ParameterDialog.SelectionDialog.Label.Value=값(&V):

ParameterDialog.SelectionDialog.Button.Resource=...
ParameterDialog.SelectionDialog.Button.Resource.Tooltip=키를 찾아보고 선택합니다.

ParameterDialog.SelectionDialog.Button.Remove.Tooltip=키 제거

ParameterDialog.SelectionDialog.Label.Note=참고: 표시 텍스트 키를 설정하려면 속성 편집기의 리소스 탭에서 보고서를 선택하고 리소스 파일 이름을 설정하십시오.

ParameterDialog.Label.SelectDisplayText=표시 텍스트 선택(&E):

ParameterDialog.Label.SelectValueColumn=값 열 선택(&U):

ParameterDialog.Label.Value=값:

ParameterDialog.Label.values=값

ParameterDialog.Label.Sample=예제

ParameterDialog.Label.Listlimit=목록 제한(&I):

ParameterDialog.Radio.Dynamic=동적(&Y)

ParameterDialog.Radio.Static=정적(&S)

ParameterDialog.toolTipText.OpenExprButton=식 빌더 열기

ParameterDialog.datetime.prompt=날짜/시간 값을 다음 형식으로 입력하십시오. {0}

ParameterDialog.date.prompt=날짜 값을 다음 형식으로 입력하십시오. {0}

ParameterDialog.time.prompt=시간 값을 다음 형식으로 입력하십시오. {0}

ParameterDialog.Check.AllowMulti=여러 값 허용(&V)

ParameterDialog.checkBox.InputValue=입력 값

ParameterDialog.checkBox.BlankValue=빈 값

ParameterDialog.checkBox.NullValue=널(Null) 값

ParameterDialog.emptyColumnValueExpression.title=잘못된 값 열 식

ParameterDialog.emptyColumnValueExpression.message=값 열 식은 비워 둘 수 없습니다!

ParameterDialog.Value.Empty=빈 값

ParameterDialog.Value.Null=널(Null) 값

ParameterDialog.message=보고서 매개변수의 속성을 정의합니다.

###########################################################
#SelectParameterDefaultValueDialog properties
SelectParameterDefaultValueDialog.Title=값 선택

###########################################################
#ParameterDropListener properties
ParameterDropListener.Command.Copy=요소 복사

ParameterDropListener.Command.Move=요소 이동

###########################################################
#ParameterGroupDialog properties
ParameterGroupDialog.Page.Title=일반

ParameterGroupDialog.pageTitle.general=일반

###########################################################
#ParameterGroupGeneralPage properties
ParameterGroupGeneralPage.Label.DisplayName=표시 이름(&D):

ParameterGroupGeneralPage.Label.Name=이름(&N):

###########################################################
#ParameterGroupNodeProvider properties
ParameterGroupNodeProvider.Action.GroupNew=새 매개 변수 그룹

ParameterGroupNodeProvider.Action.ParameterEdit=편집(&E)

ParameterGroupNodeProvider.Action.ParameterNew=새 매개변수(&N)

ParameterGroupNodeProvider.Dialogue.ParameterEdit=매개 변수 그룹 편집

ParameterGroupNodeProvider.Dialogue.ParameterNew=새 매개 변수

ParameterGroupNodeProvider.Node.Group=매개 변수 그룹

###########################################################
#ParameterGroupProcess properties
ParameterGroupProcess.action.Delete=매개 변수 그룹 삭제

ParameterGroupProcess.action.Edit=매개 변수 그룹 편집

ParameterGroupProcess.action.New=새 매개 변수

ParameterGroupProcess.text.DisplayName=ParameterGroup
ParameterGroupProcess.text.Show=데이터 원본 편집기 페이지를 표시합니다.

###########################################################
#ParameterNodeProvider properties
ParameterNodeProvider.dial.title.editCascading=계단식 매개 변수 그룹 편집

ParameterNodeProvider.dial.title.editScalar=매개변수 편집

ParameterNodeProvider.dial.title.newCascading=새 계단식 매개 변수 그룹

ParameterNodeProvider.menu.text.edit=편집(&E)

ParameterNodeProvider.menu.text.new=새 매개 변수

###########################################################
#ParameterProcess properties
ParameterProcess.action.Delete=매개 변수 삭제

ParameterProcess.action.Edit=매개 변수 편집

ParameterProcess.text.DisplayName=매개 변수

ParameterProcess.text.Show=데이터 원본 편집기 페이지를 표시합니다.

###########################################################
#parameters properties
parameters.datatypes.boolean=부울

parameters.datatypes.dateTime=DateTime
parameters.datatypes.decimal=십진수

parameters.datatypes.float=실수

parameters.datatypes.string=문자열

parameters.menuItem.reset=재설정

###########################################################
#ParametersNodeProvider properties
ParametersNodeProvider.dialogue.title.group=새 매개 변수 그룹

ParametersNodeProvider.dialogue.title.parameter=새 매개 변수

ParametersNodeProvider.menu.text.cascadingParameter=새 계단식 매개 변수 그룹(&D)

ParametersNodeProvider.menu.text.group=새 매개 변수 그룹(&M)

ParametersNodeProvider.menu.text.parameter=새 매개 변수(&A)

###########################################################
#ParametersOutlineProcess properties
ParametersOutlineProcess.action.Edit=편집

ParametersOutlineProcess.action.New=새 매개 변수

ParametersOutlineProcess.action.NewGroup=새 그룹

ParametersOutlineProcess.text.Open=매개 변수 편집기 열기

ParametersOutlineProcess.text.Show=데이터 집합에 대한 데이터 집합 편집기 페이지를 표시합니다.

###########################################################
#PasteAction properties
PasteAction.dlg.title=정보

PasteAction.ok=확인

PasteAction.text=붙여넣기(&P)

###########################################################
#PlaceHolderEditPart properties
PlaceHolderEditPart.Trans.TransferToElement=보고서 항목으로 이동

###########################################################
#PreviewAction properties
PreviewAction.Text=미리 보기

###########################################################
#PreviewEditor properties
PreviewEditor.parameter.tooltip=보고서 매개 변수 표시

PreviewEditor.parameter.hint=보고서 새로 고침

PreviewEditor.parameter.note=참고:

PreviewEditor.parameter.info=현재 최대 데이터 행 수는 {0}(으)로 제한됩니다.

PreviewEditor.parameter.change=미리 보기 기본 설정을 변경하려면 클릭

PreviewEditor.ConfirmRefresh.Title=새로 고침 확인

PreviewEditor.ConfirmRefresh.Message=현재 작업이 미리 보기 결과에 영향을 미칠 수 있습니다. 미리 보기를 새로 고치겠습니까?

PreviewEditor.browser.notfound=내부 브라우저를 사용할 수 없습니다.

###########################################################
#PreviewPageAction properties
PreviewPageAction.actionText.preview=미리 보기

###########################################################
#PropertiesAction properties
PropertiesAction.Text=속성

###########################################################
#PropertyHandleTableViewer properties
PropertyHandleTableViewer.Button.New=새로 만들기(&N)...

PropertyHandleTableViewer.Button.Edit=편집(&E)...

PropertyHandleTableViewer.Button.Remove=제거(&R)

PropertyHandleTableViewer.Button.Up=위로(&U)

PropertyHandleTableViewer.Button.Down=아래로(&D)

PropertyHandleTableViewer.Menu.Remove=제거

PropertyHandleTableViewer.Menu.Up=위로

PropertyHandleTableViewer.Menu.Down=아래로

PropertyHandleTableViewer.Menu.RemoveAll=모두 제거

###########################################################
#PropertyHandleInputDialog properties
PropertyHandleInputDialog.Action.New=새로 만들기

PropertyHandleInputDialog.Action.Edit=편집

PropertyHandleInputDialog.messages.error.blankProperty={0}은(는) 비워둘 수 없습니다.

###########################################################
#PropertyAndPreferencePage properties
PropertyAndPreferencePage.Text.Configure.Special.Settings=프로젝트별 설정 구성...

PropertyAndPreferencePage.Text.Configure.Workspace.Settings=작업 영역 설정 구성...

PropertyAndPreferencePage.Text.Enable.SpecialSettings=프로젝트별 설정 사용(&O)

###########################################################
#PublishTemplateAction properties
PublishTemplateAction.wizard.page.browse.cheatsheet.title=치트 시트 파일 선택

PublishTemplateAction.wizard.page.browse.title=이미지 파일 선택

PublishTemplateAction.wizard.page.btn.browse=찾아보기(&W)...

PublishTemplateAction.wizard.page.cheatsheetError=치트 시트 파일 ''{0}''이(가) 없습니다.

PublishTemplateAction.wizard.page.desc=템플릿을 템플릿 폴더에 등록합니다.

PublishTemplateAction.wizard.page.imageError=이미지 파일 ''{0}''이(가) 없습니다.

PublishTemplateAction.wizard.page.label.cheatsheet=치트 시트 파일:

PublishTemplateAction.wizard.page.label.description=설명(&D):

PublishTemplateAction.wizard.page.label.displayName=표시 이름(&M):

PublishTemplateAction.wizard.page.label.image=템플릿 이미지(&I):

PublishTemplateAction.wizard.page.title=템플릿 게시

PublishTemplateAction.wizard.title=템플릿 게시

PublishTemplateAction.wizard.errorTitle=오류

PublishTemplateAction.wizard.message=템플릿이 이미 들어 있는 디렉터리에 템플릿을 게시할 수 없습니다. 게시할 다른 디렉터리를 선택하십시오.

PublishTemplateAction.wizard.msgDirErr=대상 디렉터리가 없지만 만들 수 없습니다.

PublishTemplateAction.wizard.notvalidfolder=기본 설정에 정의한 템플릿 폴더가 올바르지 않습니다.

PublishTemplateAction.wizard.page.nameInfo=표시 이름이 필요함

PublishTemplateAction.wizard.message.SourceFileNotExist=원본 파일이 없습니다.

PublishTemplateAction.wizard.message.ThumbnailImageNotExist=축소판 이미지가 없습니다.

PublishTemplateAction.wizard.message.PreviewImageNotExist=미리 보기 이미지 파일이 없습니다.

PublishTemplateAction.wizard.message.PreviewImageNotValid=미리 보기 이미지 파일이 올바른 이미지가 아닙니다.

PublishTemplateAction.SaveBeforeGenerating.dialog.title=보고서 저장

PublishTemplateAction.SaveBeforeGenerating.dialog.message={0}이(가) 수정되었습니다. 게시하기 전에 변경 내용을 저장하시겠습니까?

PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes=예(&Y)

PublishTemplateAction.SaveBeforeGenerating.dialog.button.no=아니요(&N)
###########################################################
#PublishLibraryAction properties
PublishLibraryAction.wizard.errorTitle=오류

PublishLibraryAction.wizard.message=라이브러리가 이미 들어 있는 디렉터리에 라이브러리를 게시할 수 없습니다. 게시할 다른 디렉터리를 선택하십시오.

PublishLibraryAction.wizard.msgDirErr=대상 디렉터리가 없지만 만들 수 없습니다.

PublishLibraryAction.wizard.message.SourceFileNotExist=원본 파일이 없습니다.

PublishLibraryAction.wizard.notvalidfolder=기본 설정에 정의한 라이브러리 폴더가 올바르지 않습니다.

###########################################################
#RedoAction properties
RedoAction.Tooltip={0} 다시 실행

###########################################################
#ExportReportWizard
ExportToLibraryAction.wizard.windowTitle=내보내기

ExportToLibraryAction.wizard.page.title=라이브러리로 파일 내보내기

ExportToLibraryAction.wizard.page.desc=라이브러리로 보고서 디자인 내보내기

ExportToLibraryAction.wizard.page.label.filename=파일 이름(&N):

ExportToLibraryAction.wizard.page.label.folder=폴더(&F):

ExportToLibraryAction.wizard.page.button.browser=찾아보기(&B)...

ExportToLibraryAction.wizard.page.dirdialog.message=파일 내용 디렉토리를 선택하십시오.

ExportToLibraryAction.wizard.page.nameempty=파일 이름을 지정해야 합니다.

ExportToLibraryAction.wizard.page.folderempty=폴더를 지정해야 합니다.

ExportToLibraryAction.wizard.page.foldererror=지정한 폴더가 없습니다.

###########################################################
#PublishLibraryToResourceFolderAction
PublishLibraryToResourceFolderAction.Action.Text=공유 리소스 폴더로 라이브러리 복사(&B)...

###########################################################
#PublishTemplateViewAction
DefaultNodeProvider.template.suffix=.rpttemplate
PublishTemplateViewAction.action.displaytext=새 보고서 마법사를 템플릿에 등록(&Z)...

###########################################################
#RefreshModuleHandleAction
RefreshModuleHandleAction.Action.Text=라이브러리 새로 고침(&L)

RefreshModuleHandleAction.MessageBox.Title=라이브러리 새로 고침

RefreshModuleHandleAction.MessageBox.Text=현재 디자인에 저장하지 않은 변경 사항이 있습니다. 먼저 이러한 변경 사항을 저장하시겠습니까?

RefreshModuleHandleAction.MessageBox.SaveButton=저장(&S)

RefreshModuleHandleAction.MessageBox.DiscardButton=취소(&D)

RefreshModuleHandleAction.MessageBox.CancelButton=취소
###########################################################
#ReloadCssStylesAction
ReloadCssStylesAction.Action.Text=CSS 파일 다시 로드(&R)

###########################################################
#ReferencePage properties
ReferencePage.Button.Edit=편집(&D)...

ReferencePage.Label.Source=원본(&C):

###########################################################
#RefreshAction properties
RefreshAction.text=새로 고침(&F)

###########################################################
#RefreshLibExplorerAction properties
RefreshLibExplorerAction.Text=새로 고침(&F)

###########################################################
#RemoveLibraryAction properties
RemoveLibraryAction.Text=라이브러리 제거(&B)

RemoveLibraryAction.config.Title=라이브러리 제거 확인

RemoveLibraryAction.config.Message=이 라이브러리를 제거하시겠습니까?

###########################################################
#UseCssInReportDesignAction properties
UseCssInReportDesignAction.Text=보고서 디자인에 CSS 사용...

###########################################################
#UseCssInReportDesignAction properties
UseCssInThemeAction.Text=라이브러리에 CSS 사용...

###########################################################
#RenameAction properties
RenameAction.text=이름 바꾸기(&M)

###########################################################
#RenameDialog properties
RenameDialog.action.Cancel=취소

RenameDialog.action.Ok=\\ \\ 확인

RenameDialog.text.NewName=새 이름:

RenameDialog.text.Rename=이름 바꾸기

RenameDialog.text.ToolTip=이름은 비워 둘 수 없습니다.


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.SetNameAction.text=이름 설정

ExportElementToLibraryAction.DialogTitle.setNameFailed=이름 설정 실패

ExportElementToLibraryAction.TransLabel.Setname=이름 설정

ExportElementToLibraryAction.DialogTitle=이름 설정

ExportElementToLibraryAction.DialogMessage=선택된 요소를 내보내기 전에 이름을 설정할 수 있고, 설정하지 않는 경우 기본 이름이 사용됩니다.


###########################################################
#RenameInlineTool properties
RenameInlineTool.DialogTitle.RenameFailed=이름 바꾸기에 실패했습니다.

RenameInlineTool.TransLabel.Rename=이름 바꾸기

###########################################################
#RenameInputDialog properties
RenameInputDialog.DialogTitle=이름 바꾸기 입력

RenameInputDialog.DialogMessage=새 이름을 입력하십시오.

###########################################################
#report properties
report.designer.ui.preferences.buttonadd=추가(&D)...

report.designer.ui.preferences.buttonremove=제거(&R)

report.designer.ui.preferences.buttonremoveall=모두 제거(&A)

report.designer.ui.preferences.dialogtitle=라이브러리 열기

report.designer.ui.preferences.errormessage=라이브러리가 이미 있습니다.

report.designer.ui.preferences.errortitle=오류

report.designer.ui.preferences.extensions=*.rptlibrary
report.designer.ui.preferences.labeltitle=라이브러리 추가/제거

report.designer.ui.preferences.library.export.overwrite.always=항상(&W)

report.designer.ui.preferences.library.export.overwrite.never=안 함(&N)

report.designer.ui.preferences.library.export.overwrite.prompt=묻기(&P)

report.designer.ui.preferences.library.export.title=디자인 요소를 라이브러리로 내보낼 때 기존 요소를 덮어쓰기

report.designer.ui.preferences.template.openDialogTitle=폴더 선택

report.designer.ui.preferences.template.select=선택(&S)...

report.designer.ui.preferences.template.templatedir=템플릿

report.designer.ui.preferences.template.templatefolder=템플릿 폴더(&T)

report.designer.ui.preferences.template.title=템플릿 폴더를 선택하십시오.

org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment=보고서 디자인 생성 시 설명 작성(&G)

org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment=Copyright (c) 2008 <<회사 이름>>
#bidi_hcg start
report.designer.ui.preferences.bidiproperties.defaultdirection=기본 보고서 방향(&O):

report.designer.ui.preferences.bidiproperties.ltrdirection=왼쪽에서 오른쪽

report.designer.ui.preferences.bidiproperties.rtldirection=오른쪽에서 왼쪽
#bidi_hcg end
###########################################################
#Resource Preference page properties
ResourecePreferencePage.title=라이브러리 탐색기, 이미지, 속성 파일 등에서 사용할 기본 리소스 폴더를\n입력하거나 선택하십시오.

ResourecePreferencePage.folder=리소스 폴더(&R)

ResourecePreferencePage.select=선택(&S)...

ResourecePreferencePage.openDialogTitle=폴더 찾아보기

ResourecePreferencePage.openDialogMessage=기본 리소스 폴더를 선택하십시오.

ResourecePreferencePage.defaultResourceFolder.dispaly=<현재 프로젝트 폴더>

###########################################################
#ReportAdvancedLauncherTab properties
ReportAdvancedLauncherTab.DefaultsButtonText=기본값 복원(&R)

ReportAdvancedLauncherTab.DeselectButtonText=모두 선택 취소(&D)

ReportAdvancedLauncherTab.Name=보고서 프로젝트

ReportAdvancedLauncherTab.SelectAllButtonText=모두 선택(&S)

ReportAdvancedLauncherTab.UseListRadioText=목록에서 실행할 프로젝트 선택

ReportAdvancedLauncherTab.VisibleLabelText=사용 가능한 프로젝트(&A)

ReportAdvancedLauncherTab.WorkspaceReportProjects=작업 영역 보고서 프로젝트

###########################################################
#ReportClasspathLaucnTab properties
ReportClasspathLaucnTab.DefaultButtonText=기본값 복원(&R)

ReportClasspathLaucnTab.DeselectButtonText=모두 선택 취소(&D)

ReportClasspathLaucnTab.Name=Java 프로젝트

ReportClasspathLaucnTab.SelectAllButtonText=모두 선택(&S)

ReportClasspathLaucnTab.UseListRadioText=목록에서 실행할 프로젝트를 선택하면, 선택 프로젝트의 클래스 출력 폴더가 클래스 경로에 추가됩니다.

ReportClasspathLaucnTab.VisibleLabelText=사용 가능한 프로젝트(&A)

ReportClasspathLaucnTab.WorkspaceJavaProjects=작업 영역 java 프로젝트

###########################################################
#ReportCreationTool properties
ReportCreationTool.ModelTrans.CreateElement=요소 삽입

###########################################################
#ReportEditor properties
ReportEditor.Button.Close=닫기

ReportEditor.Button.Save=저장

ReportEditor.Dlg.Confirm=파일 시스템에서 파일이 삭제되었습니다. 변경 사항을 저장하시겠습니까 아니면 저장하지 않고 편집기를 닫으시겠습니까?

ReportEditor.Dlg.Save=저장...

ReportEditor.error.activated.outofsync.message=파일 시스템에서 파일이 변경되었습니다. 변경 내용을 로드하시겠습니까?

ReportEditor.error.activated.outofsync.title=파일 변경됨

ReportEditor.Error.Open=열기 오류

ReportEditor.Error.OpenEditor=파일을 열 수 없습니다.

ReportEditor.Page.Code=스크립트

ReportEditor.Page.Layout=레이아웃

ReportEditor.Page.MasterPage=MasterPage
ReportEditor.Page.Preview=미리 보기

ReportEditor.Page.PreviewSaveMessage=보고서 파일이 수정되었습니다. 미리 보기 전에 파일을 저장하시겠습니까?

ReportEditor.Page.PreviewSaveTitle=저장

ReportEditor.Page.PreviewSaveToggle=미리 보기 전에 파일을 항상 저장합니다.

###########################################################
#ReportLaunchConfigurationDelegate properties
ReportLaunchConfigurationDelegate.badFeatureSetup=잘못된 기능 설정

ReportLaunchConfigurationDelegate.noStartup=시작되지 않음

###########################################################
#ReportPage properties
ReportPage.text.Add=추가(&A)...

ReportPage.text.Browse=찾아보기(&B)...

ReportPage.title.Location=위치:

ReportPage.title.setPrewImg=이미지 파일 선택

ReportPage.text.Resource.Add=찾아보기(&B)...

ReportPage.text.Thumbnail=축소판 보기(H)...

###########################################################
#ReportPageGenerator properties
ReportPageGenerator.List.Comments=설명

ReportPageGenerator.List.Description=설명

ReportPageGenerator.List.EventHandler=이벤트 처리기

ReportPageGenerator.List.General=일반

ReportPageGenerator.List.Measurement=측정

ReportPageGenerator.List.NamedExpressions=명명된 식

ReportPageGenerator.List.UserProperties=사용자 속성

ReportPageGenerator.List.Resources=리소스

ReportPageGenerator.List.Resources.JarFile=Jar 파일:

ReportPageGenerator.List.AdvancedProperty=고급

#OdaDataSourceGenerator properties
DataSourcePageGenerator.List.General=일반

#OdaDataSetGenerator properties
DataSetPageGenerator.List.General=일반

DataSetPageGenerator.DataSourceName=데이터 원본(&D)

#ScalarParameterPageGenerator properties
ScalarParameterPageGenerator.List.General=일반

ScalarParameterPageGenerator.List.I18n=현지화

#TemplateReportItemPageGenerator properties
TemplateReportItemPageGenerator.List.General=일반

TemplateReportItemPage.description.Label.Instructions=지침(&I):

TemplateReportItemPageGenerator.List.I18n=현지화

TemplateReportItemPageGenerator.List.TextKey=텍스트 키:

###########################################################
#ReportPropertySheetPage properties
ReportPropertySheetPage.Column.Title.Property=속성

ReportPropertySheetPage.Column.Title.Value=값

ReportPropertySheetPage.Root.Default.Title=정보

ReportPropertySheetPage.Tooltip.Alphabetic=알파벳순으로 정렬

ReportPropertySheetPage.Tooltip.Group=그룹별로 정렬

ReportPropertySheetPage.Tooltip.Local=로컬 속성 표시

ReportPropertySheetPage.Value.Inherited=상속됨

###########################################################
#ReportTemplateTransferDropTargetListener properties
ReportTemplateTransferDropTargetListener.ModelTrans.CreateElement=요소 만들기

ReportTemplateTransferDropTargetListener.transLabel.createElement=요소 삽입

###########################################################
#ResourceEditDialog properties
ResourceEditDialog.message.AddNote=빠른 추가는 현재 리소스 파일에 키를 저장합니다.

ResourceEditDialog.message.ResourceFile=리소스 파일:

ResourceEditDialog.text.Add=추가(&A)

ResourceEditDialog.text.AddWarningMsg=빈 키는 허용되지 않습니다.

ResourceEditDialog.text.AddWarningTitle=경고

ResourceEditDialog.text.Delete=삭제(&D)

ResourceEditDialog.text.Key.TableColumn=키

ResourceEditDialog.text.Key=키(&K)

ResourceEditDialog.text.QuickAdd=빠른 추가

ResourceEditDialog.text.Value.TableColumn=값

ResourceEditDialog.text.Value=값(&V)

###########################################################
#ResourceKeyDescriptor properties
ResourceKeyDescriptor.text.Browse=찾아보기(&B)...

ResourceKeyDescriptor.text.Browse.Alt=찾아보기(&W)...

ResourceKeyDescriptor.text.Reset=재설정(&R)

ResourceKeyDescriptor.text.Reset.Alt=재설정(&E)

ResourceKeyDescriptor.title.SelectKey=키 선택

ResourceKeyDescriptor.button.reset.tooltip=키를 재설정합니다.

ResourceKeyDescriptor.button.browse.tooltip=키를 찾아보고 선택합니다.

ResourceKey.invalid.containSpace=잘못된 표시 이름 키입니다. 표시 이름 키에 포함된 공백이 있으면 안 됩니다.
###########################################################
#RevertToReportItemAction properties
RevertToReportItemAction.text=보고서 항목으로 되돌리기(&R)

###########################################################
#RevertToTemplateAction properties
RevertToTemplateAction.text=템플릿 항목으로 되돌리기(&V)

###########################################################
#RowHandle properties
RowHandle.tooltip.GridRow=눈금 - 행

RowHandle.tooltip.GroupFooter=테이블 그룹 - 바닥글

RowHandle.tooltip.GroupHeader=테이블 그룹 - 머리글

RowHandle.tooltip.TableDetail=테이블 - 상세 정보

RowHandle.tooltip.TableFooter=테이블 - 바닥글

RowHandle.tooltip.TableHeader=테이블 - 머리글

###########################################################
#RowHandleAdapter properties
RowHandleAdapter.table.detail=상세 정보 행

RowHandleAdapter.table.footer=바닥글 행

RowHandleAdapter.table.groupFooter=그룹 바닥글 행

RowHandleAdapter.table.groupHeader=그룹 머리글 행

RowHandleAdapter.table.header=머리글 행

###########################################################
#RowPageGenerator properties
RowPageGenerator.List.Bookmark=책갈피

RowPageGenerator.List.Borders=테두리

RowPageGenerator.List.Section=페이지 나누기

RowPageGenerator.List.CellPadding=셀 안쪽 여백

RowPageGenerator.List.Font=글꼴

RowPageGenerator.List.General=일반

RowPageGenerator.List.Visibility=표시 유형

###########################################################
#RowProvider properties
RowProvider.action.text.above=위에 행 삽입(&A)

RowProvider.action.text.below=아래에 행 삽입(&B)

###########################################################
#SaveAsDialog properties
SaveAsDialog.message=다른 위치에 파일을 저장합니다.

SaveAsDialog.overwriteQuestion=파일 ''{0}''이(가) 이미 있습니다. 기존 파일을 바꾸시겠습니까?

SaveAsDialog.Question=질문

SaveAsDialog.text=다른 이름으로 저장

SaveAsDialog.title=다른 이름으로 저장

###########################################################
#CreateDirectory properties
DirectoryDialog.createQuestion=''{0}'' 디렉터리가 없습니다. 새로 만드시겠습니까?

DirectoryDialog.Question=질문

###########################################################
#SchematicContextMenuProvider properties
SchematicContextMenuProvider.ActionText.editData=값/식 편집(&V)...

SchematicContextMenuProvider.ActionText.editLabel=편집(&E)

SchematicContextMenuProvider.label.ResetSize=이미지 크기 원래대로(&Z)

SchematicContextMenuProvider.label.ResetToImageDPI=최적 이미지 해상도(&I)

SchematicContextMenuProvider.label.ResetToReportDPI=최적 보고서 해상도(&R)

SchematicContextMenuProvider.label.ResetToScreenDPI=최적 화면 해상도(&S)

SchematicContextMenuProvider.label.RestToOrginalPixels=원래 픽셀로(&P)

SchematicContextMenuProvider.Menu.EditGroup=그룹 편집(&G)

SchematicContextMenuProvider.Menu.Insert=삽입(&I)

SchematicContextMenuProvider.Menu.insertElement=삽입(&N)

SchematicContextMenuProvider.Menu.insertRow=행(&R)

SchematicContextMenuProvider.Menu.List=목록

SchematicContextMenuProvider.Menu.Show=표시

SchematicContextMenuProvider.Menu.Style=스타일(&Y)

SchematicContextMenuProvider.Menu.Style.Apply=스타일 적용(&P)

SchematicContextMenuProvider.Menu.Theme=테마(&M)

SchematicContextMenuProvider.Menu.Theme.Apply=테마 적용(&P)

SchematicContextMenuProvider.Menu.Layout=레이아웃 기본 설정(&A)
#SchematicContextMenuProvider.menutitle.applystylerule=Apply Style
#SchematicContextMenuProvider.menutitle.applythemerule=&Apply Theme
#SchematicContextMenuProvider.menutitle.editgroup=Edit Group
#SchematicContextMenuProvider.menutitle.insert=Insert
#SchematicContextMenuProvider.menutitle.list=List
#SchematicContextMenuProvider.menutitle.show=Show
#SchematicContextMenuProvider.menutitle.style=Style
SchematicContextMenuProvider.Menu.EditStyle=스타일 편집(&T)

SchematicContextMenuProvider.Menu.DeleteStyle=스타일 제거(&S)

SchematicContextMenuProvider.Menu.DeleteGroup=그룹 삭제(&L)

SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter=그룹 머리글/바닥글 삽입(&H)

SchematicContextMenuProvider.Menu.NewStyle=새 스타일(&Y)

###########################################################
#ApplyLayoutPreferenceAction properties
ApplyLayoutPreferenceAction.autoLayout=자동 레이아웃(&A)

ApplyLayoutPreferenceAction.fixedLayout=고정 레이아웃(&F)

###########################################################
#SectionProcess properties
SectionProcess.action.Clear=지우기

SectionProcess.action.Delete=삭제

SectionProcess.action.DeleteRow=행 삭제

SectionProcess.action.New=새로 만들기

SectionProcess.action.NewCell=새 셀

SectionProcess.action.NewGroup=새 그룹

SectionProcess.action.NewRow=새 행

SectionProcess.action.NewSection=새 섹션

SectionProcess.action.NewSubSection=새 하위 섹션

SectionProcess.displayname.GridProcess=눈금

SectionProcess.displayname.GroupProcess=그룹

SectionProcess.displayname.ListProcess=목록

SectionProcess.displayname.TableProcess=테이블

SectionProcess.displayname.TableRowProcess=행

SectionProcess.type.Container=(컨테이너)

SectionProcess.type.Data=(데이터)

SectionProcess.type.Image=(이미지)

SectionProcess.type.Label=(레이블)

SectionProcess.type.List=(목록)

SectionProcess.type.Prefix=열

SectionProcess.type.Table=(테이블)

SectionProcess.type.Text=(텍스트)

###########################################################
#SelectAllAction properties
SelectAllAction.text=모두 선택(&A)

###########################################################
#SelectCssStyleWizard properties
SelectCssStyleWizard.wizard.title=CSS 스타일 가져오기

SelectCssStyleWizard.wizardPage.description.report=CSS 파일을 선택하고 스타일을 보고서 디자인으로 가져옵니다.

SelectCssStyleWizard.wizardPage.description.library=CSS 파일을 선택하고 스타일을 라이브러리로 가져옵니다.

SelectCssStyleWizard.wizardPage.name=Css 스타일 선택

SelectCssStyleWizard.wizardPage.title=CSS 파일의 스타일 가져오기

###########################################################
#UseCssInReportDialog properties
UseCssInReportDialog.Wizard.Title=CSS 사용

UseCssInReportDialog.Wizard.Filename=파일 이름(&F):

UseCssInReportDialog.Label.Styles={0}에 있는 스타일(&A)

UseCssInReportDialog.Label.notifications=알림(&N)

UseCssInReportDialog.TitleArea.Title=보고서 디자인에 CSS 파일 사용...

UseCssInReportDialog.TitleArea.Message=공유 라이브러리 폴더에서 CSS 파일을 선택해 그 파일의 스타일을 보고서 디자인에 사용합니다.

UseCssInReportDialog.Label.No.File=선택된 CSS 파일이 없습니다.

UseCssInReportDialog.Dialog.Browse=찾아보기

UseCssInReportDialog.Dialog.Browse.Title=보고서에 포함할 CSS 파일 선택

UseCssInReportDialog.Dialog.Browse.Library.Title=라이브러리에 포함할 CSS 파일 선택

UseCssInReportDialog.Error.Already.Include={0}은(는) 이미 포함되어 있습니다.

UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text=볼 때 CSS 파일 포함(HTML 형식에만 적용)(&I)

UseCssInReportDialog.Dialog.Label.viewTimeLb=볼 때 사용할 CSS 파일의 위치:

UseCssInReportDialog.Dialog.Label.example=예 - http://mydomain/test.css, myfolder/test/css

UseCssInReportDialog.Dialog.Label.Theme.Text=테마(&T):

UseCssInReportDialog.Dialog.Text.uri=&URI:
UseCssInThemeDialog.TitleArea.Title=보고서 라이브러리에 있는 CSS 파일 사용

UseCssInThemeDialog.TitleArea.Message=CSS 파일의 스타일을 교체할 테마를 선택합니다.

CssStyleSheetNodeProvider.Label.FromServer=서버에서

CssStyleSheetNodeProvider.Tooltip.URI=(볼 때 적용)


###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=목록을 검색하는 도중 오류가 발생했습니다.

SelectValueDialog.messages.info.selectVauleUnavailable=값 목록을 사용할 수 없습니다.

SelectValueDialog.messages.info.invalidSelectVauleExpression="값 선택" 함수는 간단한 열 식만 받아들입니다. 복잡한 식은 거부됩니다.

SelectValueDialog.messages.warning.BlobColumn.unsupported=Blob 형식의 열/바인딩에 대해 참조되는 식의 값 목록을 검색할 수 없습니다.

SelectValueDialog.messages.error.selectVauleUnavailable=식의 값을 검색할 수 없습니다.

SelectValueDialog.messages.error.selectValueNotSupported=이와 같은 값 선택은 지원하지 않습니다.

SelectValueDialog.noExpressionSet=식이 설정되지 않았습니다.

SelectValueDialog.retrieving=목록 검색...

SelectValueDialog.selectValue=값 선택

SelectValueDialog.SelectValue.NullValue=<널(null) 값>

###########################################################
#SetConstraintCommand properties
SetConstraintCommand.Error.SetConstraintError=모델에 제약 조건을 설정할 때 오류가 발생했습니다.

SetConstraintCommand.transLabel.setConstraint=제약 조건 설정

SetConstraintCommand.commandLabel.ResizeCommand=크기 조정
###########################################################
#ProjectFilterAction properties
ProjectFilterAction.text=필터...

###########################################################
#ResourceFileFolderSelectionAction properties
ResourceFileFolderSelectionAction.text=필터...

###########################################################
#ResourceFileFolderSelectionDialog properties
ResourceFileFolderSelectionDialog.Text.Menu=메뉴

ResourceFileFolderSelectionDialog.button.importFile=파일 가져오기...

ResourceFileFolderSelectionDialog.title.error=오류

ResourceFileFolderSelectionDialog.title.overwrite=덮어쓰기

ResourceFileFolderSelectionDialog.overwrite.msg=같은 이름의 파일이 이미 있습니다. 덮어쓰시겠습니까?

ResourceFileFolderSelectionDialog.import.msg=파일을 가져오는 중...

###########################################################
#ShowPropertyAction properties
ShowPropertyAction.text=속성(&R)

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=정렬 방법:

###########################################################
#Spinner properties
Spinner.DialogTitle.InvalidNumber=잘못된 숫자

###########################################################
#SplitAction properties
SplitAction.actionMsg.splitCells=셀 분할(&S)

###########################################################
#StyleBuilder properties
StringVariableSelectionDialog.Dialog.Label=변수 설명(&V):

StringVariableSelectionDialog.Dialog.Prompt=변수 선택(? = 모든 문자, * = 모든 문자열)(&C):

StringVariableSelectionDialog.Dialog.Title=변수 선택

StyleBuilder.action.Apply=적용(&A)

StyleBuilder.Edit.Info=원하는 스타일 속성을 수정합니다. 이 스타일을 사용하는 모든 보고서 요소는 자동으로 업데이트되어 새 형식을 반영합니다.

StyleBuilder.New.Info=왼쪽의 속성 범주를 선택하고 속성 값을 지정하여 새 스타일을 생성합니다.

StyleBuilder.Title=스타일

SytleBuilder.DialogTitle.New=새 스타일

SytleBuilder.DialogTitle.Edit=스타일 편집

###########################################################
#StyleComboProvider properties
StyleComboProvider.LineStyle.None=스타일 없음

###########################################################
#StyleNodeProvider properties
StyleNodeProvider.action.Edit=스타일 편집(&E)...

StyleNodeProvider.action.New=새 스타일(&N)...

###########################################################
#StyleProcess properties
StyleProcess.action.Delete=스타일 제거

StyleProcess.action.Edit=스타일 편집(&E)...

StyleProcess.action.New=새 스타일(&N)...

StyleProcess.text.CannotDelete=기본 스타일은 제거할 수 없습니다.

StyleProcess.text.Show=데이터 집합에 대한 데이터 집합 편집기를 표시합니다.

###########################################################
#StylesNodeProvider properties
StylesNodeProvider.action.New=새 스타일(&N)...

###########################################################
#StylesOutlineProcess properties
StylesOutlineProcess.action.New=새 스타일(&N)...

###########################################################
#TableBandProvider properties
TableBandProvider.action.text.group=그룹 삽입(&N)

TableBandProvider.action.text.row=행 삽입(&S)

###########################################################
#TableEditPart properties
TableEditPart.GUIDEHANDLE_TEXT =테이블

TableEditPart.Label.Merge=셀 병합

TableEditPart.Label.ResizeColumn=열 크기 조정

TableEditPart.Label.ResizeRow=행 크기 조정

###########################################################
#TableFigure properties
TableFigure.BORDER_TEXT=테이블

TableArea.Button.New=새로 만들기(&W)...

TableArea.Button.Edit=편집(&E)...

TableArea.Button.Remove=제거(&R)

TableArea.Button.Down=아래로(&D)

TableArea.Button.RemoveAll=모두 제거(&M)

###########################################################
#TableHandleAdapter properties
TableHandleAdapter.name.detail=상세 정보

TableHandleAdapter.name.footer=바닥글

TableHandleAdapter.name.header=머리글

TableHandleAdapter.transLabel.deleteColumn=열 삭제

TableHandleAdapter.transLabel.deleteColumns=열 삭제

TableHandleAdapter.transLabel.deleteRow=행 삭제

TableHandleAdapter.transLabel.deleteRows=행 삭제

TableHandleAdapter.transLabel.include=포함

TableHandleAdapter.transLabel.insertColumn=열 삽입

TableHandleAdapter.transLabel.insertGroup=그룹 삽입

TableHandleAdapter.transLabel.insertRow=행 삽입

TableHandleAdapter.transLabel.notInclude=제외

TableHandleAdapter.transLabel.splitCells=셀 분할

TableHandleAdapter.transLable.deleteGroup=테이블 그룹 삭제

###########################################################
#TableOptionDialog properties
TableOptionDialog.message.RememberGrid=새 눈금에 대해 이 차원을 사용합니다(&U).

TableOptionDialog.message.RememberTable=새 테이블에 대해 이 차원을 사용합니다(&U).

TableOptionDialog.text.Column=열 개수(&C):

TableOptionDialog.text.GridRow=행 개수(&R):

TableOptionDialog.text.GridSize=눈금 크기

TableOptionDialog.text.TableDetail=상세 정보 개수(&D):

TableOptionDialog.text.TableSize=테이블 크기

TableOptionDialog.title.InsertGrid=눈금 삽입

TableOptionDialog.title.InsertTable=테이블 삽입

TableOptionDialog.text.DataSet=데이터 집합(&S):

TableOptionDialog.text.AutoSummary=자동 요약 대상(&M)

TableOptionPage.Title=테이블 삽입

###########################################################
#TablePageGenerator properties
TablePageGenerator.List.Bookmark=책갈피

TablePageGenerator.List.Borders=테두리

TablePageGenerator.List.CellPadding=셀 안쪽 여백

TablePageGenerator.List.Font=글꼴

TablePageGenerator.List.General=일반

TablePageGenerator.List.Marign=여백

TablePageGenerator.List.Section=페이지 나누기

TablePageGenerator.List.TOC=목차

TablePageGenerator.List.Visibility=표시 유형

###########################################################
#TemplateReportItemPropertiesDialog properties
TemplateReportItemPropertiesDialog.errorMessage=프롬프트 텍스트가 비어 있을 수 없습니다.

TemplateReportItemPropertiesDialog.errorTitle=오류

TemplateReportItemPropertiesDialog.group=템플릿 보고서 항목 속성

TemplateReportItemPropertiesDialog.objectType=요소 형식:

TemplateReportItemPropertiesDialog.nameText=이름(&N):

TemplateReportItemPropertiesDialog.promptText=지침(&I):

TemplateReportItemPropertiesDialog.title=템플릿 보고서 항목 생성

###########################################################
#performCreatePlaceHolder action
performCreatePlaceHolder.errorMessage.title=오류

performCreatePlaceHolder.errorMessage.content=지정한 이름이 이미 있습니다. 다른 이름을 선택하거나 공백으로 두십시오.

###########################################################
#TogglePropertyDescriptor properties
TogglePropertyDescriptor.toolTipText.Bold=굵게

TogglePropertyDescriptor.toolTipText.Italic=기울임꼴

TogglePropertyDescriptor.toolTipText.Underline=밑줄

TogglePropertyDescriptor.toolTipText.Text_Line_Through=텍스트 가운데 선 긋기

###########################################################
#TextAlignPropertyDescriptor properties
TextAlignPropertyDescriptor.0=왼쪽

TextAlignPropertyDescriptor.1=가운데

TextAlignPropertyDescriptor.2=오른쪽

TextAlignPropertyDescriptor.3=양쪽 맞춤

###########################################################
#TextAreaContextMenu properties
TextAreaContextMenu.Copy.description=복사

TextAreaContextMenu.Copy.image=

TextAreaContextMenu.Copy.label=복사(&C)

TextAreaContextMenu.Copy.tooltip=복사

TextAreaContextMenu.Cut.description=자르기

TextAreaContextMenu.Cut.image=

TextAreaContextMenu.Cut.label=잘라내기(&T)

TextAreaContextMenu.Cut.tooltip=자르기

TextAreaContextMenu.Paste.description=붙여넣기

TextAreaContextMenu.Paste.image=

TextAreaContextMenu.Paste.label=붙여넣기(&P)

TextAreaContextMenu.Paste.tooltip=붙여넣기

TextAreaContextMenu.Redo.description=다시 실행

TextAreaContextMenu.Redo.image=

TextAreaContextMenu.Redo.label=다시 실행(&R)

TextAreaContextMenu.Redo.tooltip=다시 실행

TextAreaContextMenu.SelectAll.description=모두 선택

TextAreaContextMenu.SelectAll.image=

TextAreaContextMenu.SelectAll.label=모두 선택(&A)

TextAreaContextMenu.SelectAll.tooltip=모두 선택

TextAreaContextMenu.Undo.description=실행 취소

TextAreaContextMenu.Undo.image=

TextAreaContextMenu.Undo.label=실행 취소(&U)

TextAreaContextMenu.Undo.tooltip=실행 취소

###########################################################
#TextDataEditPart properties
TextDataEditPart.Figure.Dafault=동적 텍스트

###########################################################
#TextEditDialog properties
TextEditDialog.action.text.direction=오른쪽에서 왼쪽 읽기(&R)

TextEditDialog.action.text.editDynamicText=동적 텍스트 편집(&X)

TextEditDialog.action.text.formatDateTime=날짜 시간 형식(&E)

TextEditDialog.action.text.formatNumber=숫자 형식(&N)

TextEditDialog.action.text.formatString=형식 문자열(&G)

TextEditDialog.action.text.formatHTML=HTML 형식(&H)

TextEditDialog.choice.HTML=HTML/동적 텍스트

TextEditDialog.choice.plainText=일반 텍스트

TextEditDialog.choice.Auto=자동

TextEditDialog.comboText.formattingAndLayout=서식 및 레이아웃

TextEditDialog.format.dynamicText=동적 텍스트

TextEditDialog.format.formatting=서식 및 레이아웃

TextEditDialog.format.lists=목록

TextEditDialog.format.pageComposition=페이지 컴퍼지션

TextEditDialog.format.pageElements=페이지 요소

TextEditDialog.formatChoice.content=내용

TextEditDialog.formatChoice.dynamicText=동적 텍스트

TextEditDialog.formatChoice.formatting=서식 지정

TextEditDialog.formatChoice.layout=레이아웃

TextEditDialog.formatChoice.lists=목록

TextEditDialog.toolTip.tag.a=앵커

TextEditDialog.toolTip.tag.b=굵은꼴 텍스트 스타일

TextEditDialog.toolTip.tag.birtImage=BIRT 특정 이미지

TextEditDialog.toolTip.tag.br=줄 바꿈

TextEditDialog.toolTip.tag.center=텍스트 가운데

TextEditDialog.toolTip.tag.code=컴퓨터 코드 조작

TextEditDialog.toolTip.tag.dd=정의 설명:

TextEditDialog.toolTip.tag.del=삭제된 텍스트

TextEditDialog.toolTip.tag.dl=정의 목록

TextEditDialog.toolTip.tag.dt=정의 용어

TextEditDialog.toolTip.tag.em=강조

TextEditDialog.toolTip.tag.font=글꼴의 로컬 변경

TextEditDialog.toolTip.tag.genericStyleContainer=일반 언어/스타일 컨테이너

TextEditDialog.toolTip.tag.h1=머리글 수준 1

TextEditDialog.toolTip.tag.h2=머리글 수준 2

TextEditDialog.toolTip.tag.h3=머리글 수준 3

TextEditDialog.toolTip.tag.h4=머리글 수준 4

TextEditDialog.toolTip.tag.h5=머리글 수준 5

TextEditDialog.toolTip.tag.h6=머리글 수준 6

TextEditDialog.toolTip.tag.i=기울임꼴 텍스트 스타일

TextEditDialog.toolTip.tag.img=표준 HTML 이미지 참조

TextEditDialog.toolTip.tag.ins=삽입된 텍스트

TextEditDialog.toolTip.tag.li=목록 항목

TextEditDialog.toolTip.tag.p=단락

TextEditDialog.toolTip.tag.pre=서식이 미리 지정된 텍스트

TextEditDialog.toolTip.tag.strong=강한 강조

TextEditDialog.toolTip.tag.sub=아래 첨자

TextEditDialog.toolTip.tag.sup=위 첨자

TextEditDialog.toolTip.tag.tt=텔레타입 또는 모노스페이스 텍스트 스타일

TextEditDialog.toolTip.tag.u=밑줄 친 텍스트 스타일

TextEditDialog.toolTip.tag.ul=순서 없는 목록

TextEditDialog.toolTip.valueOf=Value Of

TextEditDialog.toolTipText.copy=복사

TextEditDialog.toolTipText.cut=자르기

TextEditDialog.toolTipText.delete=삭제

TextEditDialog.toolTipText.paste=붙여넣기

TextEditDialog.toolTipText.redo=다시 실행

TextEditDialog.toolTipText.undo=실행 취소

TextEditDialog.toolTip.formatHTML=HTML 형식

TextEditDialog.toolTipText.fx=식 빌더 열기

TextEditDialog.action.item.formatDateTime=날짜/시간 형식

TextEditDialog.action.item.formatNumber=숫자 형식

TextEditDialog.action.item.formatString=문자열 형식

TextEditDialog.title.DlgNew=새 텍스트 항목

TextEditDialog.title.DlgEdit=텍스트 항목 편집

###########################################################
#TextEditPart properties
TextEditPart.Figure.Dafault=텍스트

TextEditPart.trans.editText=텍스트 항목 편집

###########################################################
#TextExtendsTools properties
TextExtendsTools.Message.DefaultAuthor=Birt Eclipse Report Designer 버전

###########################################################
#TextPageGenerator properties
TextPageGenerator.List.Bookmark=책갈피

TextPageGenerator.List.Borders=테두리

TextPageGenerator.List.General=일반

TextPageGenerator.List.I18n=현지화

TextPageGenerator.List.Margin=여백

TextPageGenerator.List.Padding=안쪽 여백

TextPageGenerator.List.Section=페이지 나누기

TextPageGenerator.List.TOC=목차

TextPageGenerator.List.Visibility=표시 유형

###########################################################
#TextPropertyDescriptor properties
TextPropertyDescriptor.Message.Version=Eclipse BIRT Designer 버전 {0} 빌드 <{1}>

###########################################################
#AutoTextPageGenerator properties
AutoTextPageGenerator.List.Bookmark=책갈피

AutoTextPageGenerator.List.Borders=테두리

AutoTextPageGenerator.List.General=일반

AutoTextPageGenerator.List.Margin=여백

AutoTextPageGenerator.List.Padding=안쪽

AutoTextPageGenerator.List.Section=페이지 나누기

AutoTextPageGenerator.List.TOC=목차

AutoTextPageGenerator.List.Visibility=표시 유형

###########################################################
#ThemesNodeProvider properties
ThemesNodeProvider.action.New=새 테마(&N)...

ThemesNodeProvider.Failed=새 테마 실패

NewThemeDialog.DialogTitle=새 테마

NewThemeDialog.DialogMessage=이름을 입력하십시오.

###########################################################
#TimeOptionDialog properties
TimeOptionDialog.Label.Format=형식

TimeOptionDialog.Label.TimeZone=TimeZone

###########################################################
#ToggleMarginVisibilityAction properties
ToggleMarginVisibilityAction.text.Label=여백 표시(&M)

ToggleMarginVisibilityAction.text.Tooltip=여백 전환

###########################################################
#TwinExpressionDialog properties
TwinExpressionDialog.Dialog.Title=값 선택

TwinExpressionDialog.lable.value1=Value1:

TwinExpressionDialog.lable.value2=Value2:

###########################################################
#UndoAction properties
UndoAction.Tooltip={0} 실행 취소

###########################################################
#UnitPropertyDescriptor properties
UnitPropertyDescriptor.error.message=더 작은 값을 입력하십시오\!

UnitPropertyDescriptor.error.title=값이 너무 큽니다.

###########################################################
#UseLibraryAction properties
UseLibraryAction.Text=라이브러리 사용(&U)

UseLibraryAction.Error.Title=기존 파일 대체

UseLibraryAction.Error.Message=''{0}'' 파일이 BIRT 리소스 폴더에 이미 있습니다. 기존 파일을 바꾸시겠습니까?

###########################################################
#UseCSSAction properties
UseCSSAction.Text=CSS 사용(&U)

###########################################################
#UserPropertyBuilder properties
UserPropertyBuilder.0=새 사용자 속성

UserPropertyBuilder.ErrorMessage.DuplicatedName=지정한 이름이 이미 사용되었습니다.

UserPropertyBuilder.ErrorMessage.NoName=이름을 지정하십시오.

UserPropertiesHandleProvier.Column.Name=이름

UserPropertiesHandleProvier.Column.Type=형식

UserPropertyBuilder.Label.DefaultValue=기본값(&V):

UserPropertyBuilder.Label.ExpressionName=식 이름(&N):

UserPropertyBuilder.Label.PropertyName=속성 이름(&N):

UserPropertyBuilder.Label.PropertyType=속성 형식(&T):

UserPropertyBuilder.Title.NamedExpression=새로 명명된 식

###########################################################
#VisibilityPage properties
VisibilityPage.Check.HideElement=요소 숨기기(&H)

VisibilityPage.Check.HTML=H&TML
VisibilityPage.Check.PDF=&PDF
VisibilityPage.Label.DetailAll=상세 정보(모든 출력)

VisibilityPage.Label.DetailSpecific=상세 정보(특정 출력)

VisibilityPage.Label.Expression=식:

VisibilityPage.menu.SaveHides=숨김 저장

VisibilityPage.Radio.AllOutputs=모든 출력(&A)

VisibilityPage.Radio.SpecificOutputs=특정 출력(&C)...

###########################################################
#WidgetUtil properties
WidgetUtil.MessageBox.Error=오류

###########################################################
#wizard properties
wizard.transaction.createDataSource=데이터 원본 이름 및 형식을 정의합니다.

###########################################################
#WizardChoicePage properties
WizardChoicePage.radio.createBlank=템플릿을 사용하지 않고 빈 보고서 만들기.

WizardChoicePage.radio.createFromCustom=보고서를 다른 보고서의 복사본으로 만들기.

WizardChoicePage.radio.createFromTemplate=템플릿에서 보고서 만들기.

###########################################################
#WizardCustomTemplatePage properties
WizardCustomTemplatePage.button.Browse=찾아보기

WizardCustomTemplatePage.label.FileName=기존 보고서 파일 이름 입력 또는 선택.

WizardCustomTemplatePage.label.FromLocal=로컬 파일 시스템에서 찾아보기.

WizardCustomTemplatePage.label.FromProjectDescription=기존 보고서 파일 선택.

WizardCustomTemplatePage.label.FromProjectTitle=보고서 파일 선택

###########################################################
#WizardNewReportCreationPage properties
WizardNewReportCreationPage.Errors.nameExists=이름이 이미 있습니다.

WizardNewReportCreationPage.Errors.nameEmpty=이름을 입력해야 합니다.

WizardNewReportCreationPage.label.browse=찾아보기...

WizardNewReportCreationPage.label.directory=디렉터리:

WizardNewReportCreationPage.label.file.location=파일 위치

WizardNewReportCreationPage.label.file.name=파일 이름:

WizardNewReportCreationPage.label.select.directory=디렉터리를 선택하십시오.

WizardNewReportCreationPage.label.useDefault=기본값 사용

WizardNewReportCreationPage.msg.duplicate.fileName=이름이 이미 있습니다.

WizardNewReportCreationPage.msg.empty.file.locationDirectory=파일 위치 디렉터리는 비어있지 않아야 함.

WizardNewReportCreationPage.msg.empty.file.name=파일 이름은 비어있지 않아야 합니다.

###########################################################
#WizardSelectCssStylePage properties
WizardSelectCssStylePage.button.label.browse=찾아보기(&B)...

WizardSelectCssStylePage.button.label.deselectAll=모두 선택 취소(&D)

WizardSelectCssStylePage.button.label.selectAll=모두 선택(&S)

WizardSelectCssStylePage.errorMsg.FileNotExist=파일 없음.

WizardSelectCssStylePage.info.canNotImport=\ \ (가져올 수 없음)

WizardSelectCssStylePage.label.fileName=파일 이름(&N):

WizardSelectCssStylePage.label.importToReportDesign=\보고서 디자인으로 가져오려면.

WizardSelectCssStylePage.label.notifications=알림:

WizardSelectCssStylePage.label.selectStylesFrom=다음에서 스타일 선택

WizardSelectCssStylePage.msg.FileNameEmpty=파일 이름은 비어있지 않아야 합니다.

WizardSelectCssStylePage.text.cannot.import.style=\ - 스타일을 가져올 수 없음.

WizardSelectCssStylePage.FileNameError.Title=오류

WizardSelectCssStylePage.FileNameError.Message=선택한 파일이 잘못되었습니다.

###########################################################
#WizardTemplateChoicePage properties
WizardTemplateChoicePage.label.Description=설명:

WizardTemplateChoicePage.label.Preview=미리 보기:

WizardTemplateChoicePage.label.ReportTemplates=보고서 템플릿:

WizardTemplateChoicePage.label.ShowCheatSheets=보고서 생성 치트 시트 표시(&S)

WizardTemplateChoicePage.label.EnableDefaultThemes=기본 테마 포함(&I)

WizardTemplateChoicePage.message.BlankReport=미리 정의된 내용이 없는 빈 보고서를 생성합니다.

WizardTemplateChoicePage.message.ChartListing=각 그룹 데이터에 대해 시각적 표현을 제공하는 차트를 사용하여 그룹화한 보고서를 생성합니다.

WizardTemplateChoicePage.message.DualColumnChartListing=두 개의 "차트 및 목록" 하위 그룹을 사용하여 그룹화한 보고서를 생성합니다.

WizardTemplateChoicePage.message.DualColumnListing=두 개의 하위 그룹을 사용하여 그룹화한 보고서를 생성합니다.

WizardTemplateChoicePage.message.FirstReport=고객 목록 보고서를 작성하기 위한 지침을 제공합니다.

WizardTemplateChoicePage.message.GroupedListing=일부 필드를 그룹화한 목록 보고서를 생성합니다.

WizardTemplateChoicePage.message.Letter=데이터 원본의 필드를 사용하여 개별화된 문자를 생성할 템플릿을 생성합니다.

WizardTemplateChoicePage.message.MailingLabels=전자 우편 레이블

WizardTemplateChoicePage.message.SideBySideChartListing=그룹화한 보고서를 만들며, 각 그룹은 연속 목록 및 차트를 포함합니다.

WizardTemplateChoicePage.message.SimpleListing=데이터 원본 필드를 사용하여 목록 보고서를 생성합니다.

WizardTemplateChoicePage.message.Crosstab=크로스 탭 보고서 작성 시 자습서 사용

WizardTemplateChoicePage.message.BlankAuto=볼 때 자동으로 대화형 작업을 사용하는 빈 보고서 템플릿입니다.

WizardTemplateChoicePage.title.BlankAuto=빈 자동 대화형 보고서

WizardTemplateChoicePage.title.BlankReport=빈 보고서

WizardTemplateChoicePage.title.ChartListing=차트 및 목록

WizardTemplateChoicePage.title.DualColumnChartListing=이중 열 차트 및 목록

WizardTemplateChoicePage.title.DualColumnListing=이중 열 목록

WizardTemplateChoicePage.title.FirstReport=내 첫 번째 보고서

WizardTemplateChoicePage.title.GroupedListing=그룹화한 목록

WizardTemplateChoicePage.title.Letter=레터

WizardTemplateChoicePage.title.MailingLabels=전자 우편 레이블

WizardTemplateChoicePage.title.SideBySideChartListing=연속 차트 및 목록

WizardTemplateChoicePage.title.SimpleListing=간단한 목록

WizardTemplateChoicePage.title.Crosstab=크로스 탭
#bidi_hcg start
WizardTemplateChoicePage.label.rtlBiDiOrientation=오른쪽에서 왼쪽

WizardTemplateChoicePage.label.ltrBiDiOrientation=왼쪽에서 오른쪽

WizardTemplateChoicePage.label.chooseBiDiDirection=보고서 방향(&R):

WizardTemplateChoicePage.RootDisplayName=표준
#bidi_hcg end
###########################################################
#XMLSourcePage properties
XMLSourcePage.Error.Dialog.Message.InvalidFile=XML 소스 페이지의 오류를 해결해야 다른 페이지를 사용할 수 있습니다.

XMLSourcePage.Error.Dialog.Message.SaveFile=XML 소스 페이지에 변경 사항이 있습니다. XML 소스를 저장해야 다른 페이지를 사용할 수 있습니다.

XMLSourcePage.Error.Dialog.title=오류

XMLSourcePage.Error.Dialog.Message.PromptMsg=XML 보고서 디자인 원본에 변경 사항이 있습니다. 변경 사항을 저장하시겠습니까?

XMLSourcePage.Error.Dialog.Message.Yes=예

XMLSourcePage.Error.Dialog.Message.No=아니오

XMLSourcePage.Error.Dialog.Message.Cancel=취소

###########################################################
#SaveReportAsWizard properties
SaveReportAsWizard.SettingPage.title=템플릿 속성 설정

SaveReportAsWizard.SaveAsPageTitle=다른 이름으로 저장

SaveReportAsWizard.SaveAsLibraryMessage=파일을 다른 이름을 가진 라이브러리로 저장하십시오.

SaveReportAsWizard.SaveAsReportorTemplateMessage=파일을 다른 이름을 가진 보고서 디자인 또는 템플릿으로 저장하십시오.

SaveReportAsWizard.SettingPageTitle=보고서 속성 설정

SaveReportAsWizard.SettingPage.message=이 속성은 보고서를 템플릿으로 저장하는 경우에 사용됩니다.

SaveReportAsWizard.SaveAsWizardWindowTitle=다른 이름으로 저장

SaveReportAsWizard.SaveAsWizardPageTitle=다른 이름으로 저장

SaveReportAsWizard.SaveAsWizardPageDesc=다른 위치에 파일을 저장합니다.

SaveReportAsWizard.SettingPage.Messages=템플릿 속성을 설정하십시오.

###########################################################
#WizardReportSettingPage properties
WizardReportSettingPage.Error.ReportorTemplate=파일 이름은 {0} 또는 .rpttemplate으로 끝나야 합니다.

WizardReportSettingPage.Error.Library=파일 이름은 .rptlibrary로 끝나야 합니다.

###########################################################
#WizardSaveAsPage properties
WizardSaveAsPage.FileLabel=파일 이름(&F):

WizardSaveAsPage.Question=질문

WizardSaveAsPage.FolderEmpty=\ 폴더가 비어 있습니다.

WizardSaveAsPage.NoProject=\ 지정된 프로젝트가 없습니다.

WizardSaveAsPage.PathOccupied=\ 파일이 다음 위치에 이미 있습니다: {0}

WizardSaveAsPage.EmptyName=\ ''{0}'' 이름이 비어 있습니다.

WizardSaveAsPage.SelectFolder=\ 폴더를 선택하십시오:

WizardSaveAsPage.NameExists=동일한 이름이 이미 있습니다.

WizardSaveAsPage.OverwriteQuestion="{0}" 파일이 이미 있습니다. 기존 파일을 바꾸시겠습니까?

WizardSaveAsPage.InvalidFileName=\ ''{0}''은(는) 잘못된 이름입니다.

WizardSaveAsPage.ContainerGroup=\ 부모 폴더 입력 또는 선택(&E):

###########################################################
#Library DND message
Library.DND.messages.outofsync = 보고서에 사용된 lib에 변경 사항이 있습니다. 업데이트된 lib를 사용하려면 보고서를 닫았다가 다시 여십시오.

NamedExpressionsHandleProvier.Column.Name=이름

NamedExpressionsHandleProvier.Column.DefaultValue=기본값

Library.DND.messages.cannotApplyTheme=테마를 적용할 수 없습니다.

JarFileFormProvider.Column.Name=이름

JsFileFormProvider.Column.Name=이름

ReportPageGenerator.List.Resources.JsFile=Javascript 파일:

PropertiesFileFormProvider.Column.Name=이름

ReportPageGenerator.List.Resources.PropertiesFile=속성 파일:

###########################################################
#ClassFinder
ClassFinder.TaskStart=처리기 클래스 검색 중...

ClassFinder.DialogTitle=클래스 선택

ClassFinder.DialogMessage=처리기 클래스 선택(&C):

ClassFinder.ErrorMessage=클래스를 찾을 수 없음

ClassPathBlock_button.addExtFolder=외부 클래스 폴더 추가(&F)...

ClassPathBlock_button.addExtJars=외부 JAR 추가(&X)...

ClassPathBlock_button.down=아래로

ClassPathBlock_button.edit=편집(&E)...

ClassPathBlock_button.remove=제거(&R)

ClassPathBlock_button.up=위로

ClassPathBlock_FileDialog.edit.text=JAR 편집

ClassPathBlock_FileDialog.jar.text=JAR 선택

ClassPathBlock_FolderDialog.edit.message=클래스 폴더의 새 위치 지정:

ClassPathBlock_FolderDialog.edit.text=클래스 폴더 편집

ClassPathBlock_FolderDialog.message=클래스 폴더의 위치 지정:

ClassPathBlock_FolderDialog.text=외부 클래스 폴더 선택

ClassPathBlock_label.LibrariesList=보고서 실행 경로상의 JAR 및 클래스 폴더(&T):

###########################################################
#ListingSectionPage
ListingSectionPage.RepeatHeader=머리글 반복(&R)

###########################################################
#ResourceEditDialog
ResourceEditDialog.NotFile.Title=파일 없음

ResourceEditDialog.NotFile.Message=파일 ''{0}''이(가) 없습니다.

ResourceEditDialog.ReadOnlyEncounter.Title=읽기 전용 파일 발생

ResourceEditDialog.ReadOnlyEncounter.Message=''{0}'' 파일은 읽기 전용입니다. 먼저 파일에 쓸 수 있는지 확인하십시오.

###########################################################
#EventHandler
EventHandlerPage.Browse=찾아보기(&B)...


###########################################################
#IDEFileReportProvider
IDEClassPathBlock.button_addExtFolder=외부 클래스 폴더 추가(&F)...

IDEClassPathBlock.button_addEXTJar=외부 JAR 추가(&X)...

IDEClassPathBlock.button_addJar=JAR 추가(&J)...

IDEClassPathBlock.button_addProject=프로젝트 추가(&P)...

IDEClassPathBlock.button_addVar=변수 추가(&V)...

IDEClassPathBlock.button_down=아래로

IDEClassPathBlock.button_edit=편집(&E)...

IDEClassPathBlock.button_folder=클래스 폴더 추가(&C)...

IDEClassPathBlock.button_remove=제거(&R)

IDEClassPathBlock.button_up=위로

IDEClassPathBlock.fLibrariesList_text=보고서 실행 경로상의 JAR 및 클래스 폴더(&T):

IDEClassPathBlock.ProjectDialog_message=추가할 프로젝트 선택:

IDEClassPathBlock.ProjectDialog_title=필요한 프로젝트 선택

IDECPListLabelProvider.folder=(클래스 폴더)

IDECPListLabelProvider.missing=(누락)

IDECPListLabelProvider.new=(새로 만들기)

IDECPListLabelProvider.unknown=알 수 없는 요소

IDEFileReportProvider.ReadOnlyEncounter.Title=읽기 전용 파일 발생

IDEFileReportProvider.ReadOnlyEncounter.Message=''{0}'' 파일은 읽기 전용입니다. 먼저 파일에 쓸 수 있는지 확인하십시오.

IDEResourcePageHelper.Dialog.Prompt=디렉터리 선택:

IDEResourcePageHelper.Dialog.Text=작업 영역과 관련된 위치 선택(&C):

IDEResourcePageHelper.Dialog.Title=디렉터리 선택

IDEResourcePageHelper.File.Button=파일 시스템(&F)...

IDEResourcePageHelper.Varible.Button=변수(&B)...

IDEResourcePageHelper.WorkSpace.Button=작업 영역(&W)...

###########################################################
#DataSetColumnBindingsFormHandleProvider
DataSetColumnBindingsFormHandleProvider.Column.Name=이름

DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID=표시 이름 ID

DataSetColumnBindingsFormHandleProvider.Column.DisplayName=표시 이름

DataSetColumnBindingsFormHandleProvider.DatasetTitle=데이터 열 바인딩:

DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns=바인딩 열 새로 고침

DataSetColumnBindingsFormHandleProvider.ReportItemTitle=사용 가능한 열 바인딩:

DataSetColumnBindingsFormHandleProvider.NewBinding=새 바인딩

DataSetColumnBindingsFormHandleProvider.Column.DataType=데이터 형식

DataSetColumnBindingsFormHandleProvider.Column.Expression=식

DataSetColumnBindingsFormHandleProvider.Column.AggregateOn=집계 대상

DataSetColumnBindingsFormHandleProvider.Column.Filter=필터

DataSetColumnBindingsFormHandleProvider.Column.Function=함수

DataSetColumnBindingsFormHandleProvider.ALL=모두

DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns=바인딩 열 생성

DataSetColumnBindingsFormHandleProvider.NONE=N/A
DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns=바인딩 열 생성

DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns=바인딩 열 새로 고침

###########################################################
#DataSetColumnBindingsFormPage
DataSetColumnBindingsFormPage.Button.RemoveUnused=사용하지 않는 열 제거

###########################################################
#DataSetSettingsPage
DataSetSettingsPage.DataGroup.Title=데이터 집합 열 값을 비교하기 위한 BIRT 엔진 설정

DataSetSettingsPage.Locale.Label=문자열 값 로케일(&L):

DataSetSettingsPage.NullValueOrdering.Label=널(null) 값 순서(&O):

###########################################################
#ResourceSelectionValidator
ResourceSelectionValidator.ExtError=선택한 파일은 다음으로 끝나야 합니다.

ResourceSelectionValidator.EmptyError=파일을 선택하십시오.

ResourceSelectionValidator.ExtErrorSplitter=\ 또는

###########################################################
#GeneralPage
GeneralPage.Library.Included=라이브러리:

GeneralPage.Library.Modified.Note=참고: 이 보고서 항목은 라이브러리에서 수정되었습니다. 원래의 라이브러리 항목을 복원하려면 {0}"속성 복원"을 클릭하십시오.

#BookMarkPage
BookMarkPage.Modified.Note=책갈피에 허용 문자(A-Z, a-z, 0-9, '-', '_', ':', '.') 이외의 특수 문자가 포함되어 있어 실행 시 문제가 발생할 수 있습니다.

BookMarkPage.Modified.WithoutQuotes=책갈피가 큰따옴표로 둘러싸여 있지 않아 실행 시 문제가 발생할 수 있습니다.

###########################################################
# LOCALE SPECIAL:
# following string need be specially localized, e.g. as per local country custom, not just simple translation
# for example:
#       the social security number in US is like 760-01-0101, the format should be @@@-@@-@@@@
#       but the SSN in Swedish is like 760101-0101, the format should be @@@@@@-@@@@
# 
FormatStringPage.SimpleTextZipCode=941103

FormatStringPage.SimpleTextZipCode4=941123

FormatStringPage.PhoneNumber=4155551111

FormatStringPage.SocialSecurityNumber=123456789

FormatStringPage.simpleTexZipCodeFormat=@@@-@@@

FormatStringPage.simpleTexZipCode4Format=@@@@@-@@@@
FormatStringPage.phoneNumberFormat=(@@@)@@@-@@@@
FormatStringPage.securityNumberFormat=@@@-@@-@@@@
FormatStringPage.Label.PreserveWhiteSpaces=공백 유지

FormatStringPage.Preview.PreserveWhiteSpaces=\    선행 공백이 유지된 문자열

###########################################################
#PublishLibraryDialog
PublishLibraryDialog.ShellText=라이브러리 공유

PublishLibraryDialog.TitleArea=라이브러리 공유

PublishLibraryDialog.Message=공유 리소스 폴더로 라이브러리를 복사합니다.

PublishLibraryDialog.Label.SourceFileName=원본 파일(&S):

PublishLibraryDialog.Label.FileName=파일 이름(&N):

PublishLibraryDialog.Label.Folder=폴더(&D):

PublishLibraryDialog.Label.Browse=찾아보기(&B)...

PublishLibraryDialog.Message.FileNameEmpty=파일 이름이 필요합니다.

PublishLibraryDialog.Message.FileNameError=파일 이름은 .rptlibrary로 끝나야 합니다.

PublishLibraryDialog.Message.FolderEmpty=게시 폴더가 필요합니다.

PublishLibraryDialog.Message.SourceFileEmpty=원본 파일 이름이 필요합니다.

PublishLibraryDialog.Label.Browse2=찾아보기(&W)...

PublishLibraryDialog.AddText=라이브러리 추가

PublishLibraryDialog.AddMessage=BIRT 리소스 폴더에 라이브러리 추가

WizardLibrarySettingPage.ErrorMessage=폴더를 선택하십시오.

WizardLibrarySettingPage.Resourcefile.Dialog.Title=리소스 폴더 찾아보기

WizardLibrarySettingPage.Resourcefile.Dialog.Message=리소스 폴더 또는 해당 하위 폴더를 선택하십시오.

PublishLibraryDialog.AddLibrary=라이브러리 추가


###########################################################
#PublishCSSDialog
PublishCSSDialog.ShellText=CSS 파일 게시

PublishCSSDialog.TitleArea=CSS 파일을 리소스 폴더에 게시

PublishCSSDialog.Message=스타일을 리소스 폴더로 가져올 CSS 파일 선택

PublishCSSDialog.Label.SourceFileName=원본 파일(&S):

PublishCSSDialog.Label.FileName=파일 이름(&N):

PublishCSSDialog.Label.Folder=폴더(&F):

PublishCSSDialog.Label.Browse=찾아보기(&B)...

PublishCSSDialog.Label.Browse2=찾아보기(&W)...

PublishCSSDialog.ErrorMessage.SelectFolder=폴더를 선택하십시오.

PublishCSSDialog.Dialog.FileSeclect.Tilte=CSS 파일 선택

PublishCSSDialog.Error.Exception.Title=오류

PublishCSSDialog.Error.FileNotFound=파일 ''{0}''이(가) 없습니다.

PublishCSSDialog.Error.FileIsNotCSSFile=''{0}''파일이 ''{1}''(으)로 끝나지 않습니다.

PublishCSSDialog.Error.FileExist=파일이 이미 있습니다.

PublishCSSDialog.Message.SourceFileEmpty=원본 파일 이름이 필요합니다.

PublishCSSDialog.Message.FileNameEmpty=파일 이름이 필요합니다.

PublishCSSDialog.Message.FileNameError=파일 이름은 .css로 끝나야 합니다.

PublishCSSDialog.Message.FolderEmpty=게시 폴더가 필요합니다.

WizardCSSSettingPage.ErrorMessage=폴더를 선택하십시오.

WizardCSSSettingPage.Resourcefile.Dialog.Title=리소스 폴더 찾아보기

WizardCSSSettingPage.Resourcefile.Dialog.Message=리소스 폴더 또는 그 하위 폴더를 선택하십시오.

###########################################################
#PublishResourceDialog
PublishResourceDialog.ShellText=리소스 게시

PublishResourceDialog.TitleArea=리소스 폴더에 리소스 게시

PublishResourceDialog.Message=리소스 폴더에 리소스 파일을 배치합니다.

PublishResourceDialog.AddResource=리소스 추가

PublishResourceDialog.AddText=리소스 추가

PublishResourceDialog.AddMessage=리소스 폴더에 리소스 추가

PublishResourceAction.wizard.errorTitle=오류

PublishResourceAction.wizard.message=리소스가 이미 들어 있는 디렉터리에 라이브러리를 게시할 수 없습니다. 게시할 다른 디렉터리를 선택하십시오.

PublishResourceAction.wizard.message.SourceFileNotExist=원본 파일이 없습니다.

PublishResourceAction.wizard.notvalidfolder=기본 설정에 정의된 리소스 폴더가 올바르지 않습니다.

PublishResourceDialog.Label.FileName=파일 이름(&N):

PublishResourceDialog.Label.SourceFileName=원본 파일(&S):

PublishResourceDialog.Label.Folder=폴더:

PublishResourceDialog.Label.Browse=찾아보기(&B)...

PublishResourceDialog.Label.Browse2=찾아보기(&W)...

WizardResourceSettingPage.ErrorMessage=폴더를 선택하십시오.

WizardResourceSettingPage.Resourcefile.Dialog.Title=리소스 폴더 찾아보기

WizardResourceSettingPage.Resourcefile.Dialog.Message=리소스 폴더 또는 해당 하위 폴더를 선택하십시오.

PublishResourceDialog.Message.SourceFileEmpty=원본 파일 이름이 필요합니다.

PublishResourceDialog.Message.FileNameEmpty=파일 이름이 필요합니다.

PublishResourceDialog.Message.FolderEmpty=게시 폴더가 필요합니다.

###########################################################
#EmbeddedImagesNodeProvider
EmbeddedImagesNodeProvider.FileNameError.Title=오류

EmbeddedImagesNodeProvider.FileNameError.Message=선택한 파일이 이미지가 아닙니다.

EmbeddedImagesNodeProvider.stackMsg.insert=이미지 삽입

###########################################################
#AddElementtoAction properties
AddElementtoAction.Text=보고서에 추가(&A)

ApplyThemeAction.Text=보고서에 적용(&A)

###########################################################
#MultiPageReportEditor properties
MultiPageReportEditor.CheckVersion.Dialog.Title=경고

MultiPageReportEditor.CloseButton=닫기

MultiPageReportEditor.ConfirmTitle=파일 없음

MultiPageReportEditor.ConfirmVersion.Dialog.Message=이 보고서 디자인은 이전 버전의 BIRT Designer에서 만들어졌습니다. 이 보고서 디자인을 저장하면 보고서 디자인이 현재 버전으로 업그레이드되어 이전 버전의 BIRT Designer에서 열지 못하게 될 수 있습니다. 저장하시겠습니까?

MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage=이 편집기의 내용이 외부에서 변경되었을 수 있습니다. 변경 사항을 다시 로드하시겠습니까?

MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage=이 편집기의 내용이 외부에서 변경되었을 수 있습니다. 이 변경 사항을 다시 로드하시겠습니까?

MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage=이 편집기의 내용이 외부에서 변경된 것 같습니다. 편집기에 저장하지 않는 변경 사항이 있습니다.\n\n편집기를 저장하고 변경 사항을 다시 로드하시겠습니까?

MultiPageReportEditor.ConfirmVersion.Dialog.Title=확인

MultiPageReportEditor.SaveButton=저장(&S)

MultiPageReportEditor.DiscardButton=취소(&D)

MultiPageReportEditor.CancelButton=취소

MultiPageReportEditor.SaveConfirmMessage=파일이 삭제되었습니다. 변경 사항을 저장하시겠습니까? 아니면 저장하지 않고 편집기를 닫으시겠습니까?

TableArea.Button.Up=위로(&U)

###########################################################
#AddJarResourceFileFolderSelectionDialog
AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist=선택한 파일이 하나 이상 있습니다.

AddJarResourceFileFolderSelectionDialog.Message={0} 파일을 선택하십시오.

AddJarResourceFileFolderSelectionDialog.ErrorMessage.ExtError=파일 이름은 {0}(으)로 끝나야 합니다.

AddJarResourceFileFolderSelectionDialog.Message.Or=또는


###########################################################
#AddImageResourceFileFolderSelectionDialog
AddImageResourceFileFolderSelectionDialog.Title=선택한 파일이 하나 이상 있습니다.

AddImageResourceFileFolderSelectionDialog.Message=이미지를 선택하십시오.

###########################################################
#DataItemBindingDialog properties
DataItemBindingDialog.text.Name=이름:

DataItemBindingDialog.text.displayName=표시 이름:

DataItemBindingDialog.text.DataType=데이터 형식:

DataItemBindingDialog.text.Expression=식:

DataItemBindingDialog.text.AggregateOn=집계 대상:

DataItemBindingDialog.text.ForceBinding=동일한 식으로 새 바인딩을 생성합니다.

DataItemBindingDialog.text.All=모두

DataItemBindingDialog.text.None=N/A
DataItemBindingDialog.text.Null=없음

DataItemBindingDialog.title.CreateNewItem=새 데이터 항목 바인딩

DataItemBindingDialog.title.EditDataItem=데이터 항목 바인딩 편집

DataItemBindingDialog.text.Function=함수:

DataItemBindingDialog.text.Filter=필터:

DataItemBindingDialog.text.ArgumentList=인수:

DataItemBindingDialog.Button.Add=추가

DataItemBindingDialog.Button.Edit=편집

DataItemBindingDialog.Button.Delete=삭제

###########################################################
#DataColumBindingDialog properties
DataColumBindingDialog.title.CreateNewDataBinding=새 데이터 바인딩

DataColumBindingDialog.title.EditDataBinding=데이터 바인딩 편집

DataColumBindingDialog.title.AggBuilder=집계 빌더

DataColumnBindingDialog.NewBindingDialogInputNewNameMessage=새 데이터 바인딩 이름:

DataColumnBindingDialog.NewBindingDialogInputNewNameTitle=새 데이터 바인딩 이름 입력

DataColumnBindingDialog.NewBindingDialogMessage=이 데이터 바인딩을 하나 이상의 보고서 항목 및 셀에서 사용하고 있습니다. 이 보고서 항목에 대한 새로운 식을 생성하시겠습니까? 새 데이터 바인딩을 생성하려면 '예'를, 모든 위치에서 데이터 바인딩을 변경하려면 '아니오'를 클릭하십시오.

DataColumnBindingDialog.NewBindingDialogButtonYes=예

DataColumnBindingDialog.NewBindingDialogButtonNo=아니오

DataColumnBindingDialog.NewBindingDialogButtonCancel=취소

DataColumnBindingDialog.NewBindingDialogTitle=새 데이터 바인딩을 생성하시겠습니까?

DataColumnBindingDialog.title.TimePeriodBuild=상대 시간 집계 빌더

DataColumBindingDialog.stackMsg.resetReference=참조 재설정

###########################################################
#CreatePlaceHolderAction properties
ChangeDataColumnAction.text=데이터 열 변경(&G)...

###########################################################
#ThumbnailBuilder properties
ThumbnailBuilder.Text.Title=축소판 보기 관리

ThumbnailBuilder.Button.Text.Generate=생성(&G)

ThumbnailBuilder.Button.Text.Remove=제거(&R)

ThumbnailBuilder.Button.Text.Import=가져오기(&I)...

ThumbnailBuilder.Button.Text.Browse=찾아보기(&B)...

ThumbnailBuilder.FileDialog.Title=이미지 파일 선택

ThumbnailBuilder.FileDialog.FileNameError.Title=오류

ThumbnailBuilder.FileDialog.FileNameError.Message=선택한 파일이 이미지가 아닙니다.

ThumbnailBuilder.Button.GenerateFromReport=보고서에서 생성(&N)

ThumbnailBuilder.Button.BrowseFromFileSystem=파일 시스템에서 찾아보기(&W)

ThumbnailBuilder.Button.ImportFromResource=공유 리소스에서 가져오기(&M)

ThumbnailBuilder.Image.DefaultName=축소판 이미지

ThumbnailBuilder.PreviewImage.WarningDialog.Title=경고

ThumbnailBuilder.PreviewImage.WarningDialog.Prompt=너무 큰 이미지(1014x768 이상)를 선택하여 메모리 부족 예외가 발생할 수 있습니다.\n계속하시겠습니까?


###########################################################
#FragmentResourceEntry properties
FragmentResourceEntry.RootName=조각

FragmentResourceEntry.RootDisplayName=시스템 리소스


FragmentTemplateResourceEntry.RootName=TemplateFragments
FragmentTemplateResourceEntry.RootDisplayName=템플릿 리소스

###########################################################
#PathResourceEntry properties
PathResourceEntry.RootName=리소스 폴더

PathResourceEntry.RootDisplayName=공유 리소스

###########################################################
#ResourceFilter properties
ResourceFilter.DisplayName.CVS=CVS 리소스

ResourceFilter.DisplayName.Dot=.* 리소스

ResourceFilter.DisplayName.Data=*.데이터 리소스

ResourceFilter.DisplayName.EmptyFolder=비어 있는 폴더

ResourceFilter.Description.CVS=\"CVS\"로 시작하는 이름을 가진 리소스를 숨깁니다.

ResourceFilter.Description.Dot='.'로 시작하는 이름을 가진 리소스를 숨깁니다.

ResourceFilter.Description.Data=모든 데이터 파일을 숨깁니다.

ResourceFilter.Description.EmptyFolder=비어 있는 폴더를 숨깁니다.

###########################################################
#BIRTGotoMarker properties
BIRTGotoMarker.Error.Title=오류

BIRTGotoMarker.Error.Message=보고서 요소가 없습니다.

###########################################################
#TabbedList properties
TabbedPropertyList.properties.not.available=속성은 사용할 수 없습니다.

###########################################################
#Sample Reports View properties
SampleReportsView.defautDescription=범주에서 샘플 보고서를 선택하십시오.

SampleReportsView.Text.Explorer=샘플 보고서

SampleReportsView.Text.Preview=미리 보기

SampleReportsView.Text.Description=설명

SampleReportsView.MessageDialog.Title=질문

SampleReportsView.MessageDialog.Message=''{0}''이(가) 이미 있습니다. 바꾸시겠습니까?

SampleReportsView.Action.exportSampleReport=내보내기(&E)

SampleReportsView.Action.openSampleReport=가져오기(&I)

SampleReportsView.Action.exportSampleReport.toolTipText=선택된 샘플 보고서 내보내기

SampleReportsView.Action.openSampleReport.toolTipText.ide=보고서 프로젝트를 생성하여 보고서를 프로젝트 폴더에 배치

SampleReportsView.Action.openSampleReport.toolTipText.rcp=선택된 샘플 보고서를 작업 영역 폴더에 직접 내보내기

SampleReportsView.Text.Contribute_Samples=기여 예제

UseCssStyleAction.text=CSS 파일 사용(&U)...

EditUseCssStyleAction.text=CSS 파일 편집(&E)...

EditUseCssStyleAction.EditCssTitle=CSS 편집

EditUseCssStyleAction.EditCssAreaTitle.Report=보고서 디자인의 CSS 파일 편집

EditUseCssStyleAction.EditCssAreaTitle.Libary=보고서 라이브러리의 CSS 파일 편집

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=일반

CrosstabPageGenerator.List.Margin=여백

CrosstabPageGenerator.List.Font=글꼴

CrosstabPageGenerator.List.Borders=테두리

CrosstabPageGenerator.List.Section=페이지 나누기

CrosstabPageGenerator.List.Visibility=표시 유형

CrosstabPageGenerator.List.TOC=목차

CrosstabPageGenerator.List.Bookmark=책갈피

CrosstabPageGenerator.List.NamedExpressions=명명된 식

CrosstabPageGenerator.List.UserProperties=사용자 속성

CrosstabPageGenerator.List.AdvancedProperty=고급

CrosstabPageGenerator.List.SubTotals=부분합

CrosstabPageGenerator.List.GrandTotals=총합계

CrosstabPageGenerator.List.PageBreak=페이지 나누기

###########################################################
#CubePageGenerator properties
CubePageGenerator.List.General=일반

CubePageGenerator.List.Comments=설명

CubePageGenerator.List.AdvancedProperty=고급


###########################################################
#LibraryLayoutEditorFormPage
LibraryLayoutEditorFormPage.warning.title=라이브러리

LibraryLayoutEditorFormPage.warning.message=라이브러리에 보고서 항목을 추가하려면 색상표에서 항목을 끌어 오거나 삽입 메뉴를 사용하십시오.\n\n편집기에서 보고서 항목을 보려면 윤곽선 보기에서 항목을 클릭하십시오.

LibraryLayoutEditorFormPage.warning.prompt=이 메시지를 다시 표시 안 함(&D)

###########################################################
#PageGenerator Item properties
PageGenerator.TabItem.Attributes=속성

PageGenerator.TabItem.Binding=바인딩

PageGenerator.TabItem.Filters=필터

PageGenerator.TabItem.Groups=그룹

PageGenerator.TabItem.Highlights=강조

PageGenerator.TabItem.map=맵

PageGenerator.TabItem.Sorting=정렬

###########################################################
#AggDataDropAdapter
AggDataDropAdapter.Trans.Name=집계 생성

###########################################################
#BindingDialogHelper
BindingDialogHelper.text.Name=열 바인딩 이름(&B):

BindingDialogHelper.text.DataType=데이터 형식(&T):

BindingDialogHelper.text.Function=함수(&F):

BindingDialogHelper.text.DataField=데이터 필드(&F):

BindingDialogHelper.text.Filter=필터 조건(&C):

BindingDialogHelper.text.AggOn=집계 대상(&G):

BindingDialogHelper.text.Table=테이블(&L)

BindingDialogHelper.text.List=목록(&L)

BindingDialogHelper.text.Grid=눈금

BindingDialogHelper.text.Group=그룹(&O)

BindingDialogHelper.text.Expression=식(&E):

BindingDialogHelper.text.displayName=표시 이름(&N):

BindingDialogHelper.text.allowExport=내보내기(&O):

BindingDialogHelper.text.allowExport.button=내보내기 허용

BindingDialogHelper.text.displayNameID=표시 이름 ID(&S):

BindingDialogHelper.bindingName.dataitem=열 바인딩

BindingDialogHelper.bindingName.aggregation=집계

BindingDialogHelper.error.nameduplicate=이름이 {0}인 열 바인딩이 이미 있습니다.

BindingDialogHelper.error.improperexpression=큐브 측정값의 집계는 "{0}" 유형이 될 수 없습니다.

BindingDialogHelper.error.empty={0}이(가) 필요합니다.

BindingDialogHelper.error.text.Name=열 바인딩 이름

BindingDialogHelper.text.Colon=:
BindingDialogHelper.warning.function=현재 함수에 대해 권장되는 날짜 형식은 "{0}"입니다. 선택한 데이터 형식을 그대로 유지하시겠습니까?

BindingDialogHelper.warning.parameter=식 "{0}"의 반환 데이터 형식이 매개 변수 "{1}"에서 지원되지 않습니다. 그대로 유지하시겠습니까?

BindingDialogHelper.warning.button.yes=예

BindingDialogHelper.warning.button.no=아니오

BindingGroupDescriptorProvider.Flag.DataModel=\(데이터 모델)

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel=데이터 모델 바인딩

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet=데이터 집합 바인딩

###########################################################
#Scripts Node
Scripts.Node.DisplayName=스크립트

ScriptElementNode.Action.Text=보고서 요소로 이동(&G)

ScriptObjectNode_0=스크립트 재설정(&R)

ReportPage.PreviewImage.Button.ToolTip=축소판 보기 관리

###########################################################
#CrosstabBindingDialogHelper
CrosstabBindingDialogHelper.AggOn.All=모두

CrosstabBindingDialogHelper.bindngName.timeperiod=기간

CrosstabBindingDialogHelper.calculation.label=시간(&P):

CrosstabBindingDialogHelper.calculation.group=시간

CrosstabBindingDialogHelper.recentday.description=기간 최종일

CrosstabBindingDialogHelper.referencedate.label=참조 날짜(&R):

CrosstabBindingDialogHelper.thisdate.label=날짜 지정:

CrosstabBindingDialogHelper.thisdate.example.label=제공된 날짜 값을 따옴표로 묶으십시오.\n예: "2012-01-23", "1/23/2012".

CrosstabBindingDialogHelper.thisdate.example.label.constant=예: 2012-01-23, 1/23/2012.

CrosstabBindingDialogHelper.timedimension.label=시간 차원(&M):

CrosstabBindingDialogHelper.timeperiod.wrongdate=이 차원은 최근 참조 날짜를 지원하지 않습니다.

CrosstabBindingDialogHelper.today.label=오늘

AddEditCascadingParameterDialog.label.name=이름(&N):

AddEditCascadingParameterDialog.label.dataset=데이터 집합(&D):

AddEditCascadingParameterDialog.label.value=값(&V):

AddEditCascadingParameterDialog.label.displaytext=표시 텍스트(&T):

###########################################################
#FormatDateTimePattern
FormatDateTimePattern.datetiem_format_type_year=연도

FormatDateTimePattern.datetiem_format_type_short_year=연도(짧게)

FormatDateTimePattern.datetiem_format_type_long_month_year=월(길게) 연도

FormatDateTimePattern.datetiem_format_type_shot_month_year=월(짧게) 연도

FormatDateTimePattern.datetiem_format_type_month=월

FormatDateTimePattern.datetiem_format_type_long_day_of_week=일(길게)

FormatDateTimePattern.datetiem_format_type_day_of_month=월 일

FormatDateTimePattern.datetiem_format_type_medium_day_of_year=월 일 연도

FormatDateTimePattern.datetiem_format_type_minutes=분

FormatDateTimePattern.datetiem_format_type_secontds=초

FormatDateTimePattern.datetiem_format_type_general_time=일반 시간

###############################################################################
# LOCALE SPECIAL:
# following text should be localized specifically for each locale, 
# the pattern may be different for each locale. 
# For example, in Polish locale, "general time" pattern should be "HH:mm:ss.SSS" 
# instead of "ahh:mm:ss.SSS".
#
FormatDateTimePattern.pattern.datetiem_format_type_year=yyyy

FormatDateTimePattern.pattern.datetiem_format_type_short_year=yy

FormatDateTimePattern.pattern.datetiem_format_type_long_month_year=MMMM yyyy

FormatDateTimePattern.pattern.datetiem_format_type_shot_month_year=MMM yy

FormatDateTimePattern.pattern.datetiem_format_type_month=MMMM

FormatDateTimePattern.pattern.datetiem_format_type_long_day_of_week=EEEE

FormatDateTimePattern.pattern.datetiem_format_type_day_of_month=dd

FormatDateTimePattern.pattern.datetiem_format_type_medium_day_of_year=MMMM dd, yy

FormatDateTimePattern.pattern.datetiem_format_type_minutes=mm

FormatDateTimePattern.pattern.datetiem_format_type_secontds=ss

FormatDateTimePattern.pattern.datetiem_format_type_general_time=ahh:mm:ss.SSS

FormatDescriptorProvider.DisplayName.None=자동

ReportExamples.ContributeSamples.description=Bugzilla 항목을 다음으로 제출해 주십시오. {0}BIRT로 버그 제출{1}.\n제출하실 때 보고서 디자인 파일과 보고서 출력 스크린 샷을 첨부해 주십시오.\n\n 참고: 스크린샷은 다음과 같아야 합니다.\n  1..png 이미지 형식;\n  2. 800*600 해상도;\n  3. 250 KB 이하.

ReportExamples.ContributeSamples.title=기여 예제

ReportExamples.ContributeSamples.messages=다음 정보는 예제 계산 과정을 안내해 줍니다.

ReportExamples.OpenBrowser.Error.title=오류

ReportExamples.OpenBrowser.Error.message=웹 브라우저를 열 수 없습니다.

###########################################################
#CopyCellContentsContextAction
CopyCellContentsContextAction.actionText=내용 복사(&E)

###########################################################
#CopyCellContentsAction
CopyCellContentsAction.actionText=내용 복사(&E)

###########################################################
#CopyFormatAction
CopyFormatAction.text=서식 복사(&F)

###########################################################
#PasteFormatAction
PasteFormatAction.text=서식 붙여넣기(&O)

###########################################################
#InsertAggregationAction
InsertAggregationAction.text=집계(&A)


###########################################################
#InsertRelativeTimePeriodAction
InsertRelativeTimePeriodAction.text=상대 시간(&V)

###########################################################
#Multiple views
MultipleAdapter.SetCurrentView=현재 보기 설정

MultipleAdapter.RemoveView=보기 제거

MultipleGuideHandle.RemoveView=제거

CreateChartAction.text=차트 보기 생성(&W)

CreateChartHandler.text=차트 보기 생성

###########################################################
#Input Parameters
InputParameterDialog.msg.title=입력 매개 변수

InputParameterDialog.msg.requiredParam=별표(*)가 있는 매개 변수는 필수 항목입니다.

InputParameterDialog.err.requiredParam=필수 매개 변수 [{0}]의 값이 지정되지 않았습니다.

InputParameterDialog.err.invalidValueTitle=잘못된 값

InputParameterDialog.err.invalidValue=값 "{0}"은(는) 형식 {1}에 알맞지 않습니다.

InputParameterDialog.err.emptyValue={0}에 대한 입력 값을 입력해야 합니다.

InputParameterDialog.err.emptyValue2={0}에 대한 두 번째 입력 값을 입력해야 합니다.

###########################################################
#Crosstab drag tracker
ColumnDragTracker.ResizeColumn=열 크기 조정

ColumnDragTracker.Show.Label=너비:

CPListLabelProvider.misssing=(누락)

RowDragTracker.ResizeRow=행 크기 조정

RowDragTracker.Show.Label=높이:

TableDragGuideTracker.Pixels=픽셀

###########################################################
#General Designer settings
GeneralConfigurationBlock.message.general.description=보고서 디자이너에 대한 일반 설정

GeneralConfigurationBlock.button.text.enable.gradient=그라데이션 사용 선택(&G)

GeneralConfigurationBlock.button.text.enable.animation=애니메이션 사용 선택(&N)

GeneralConfigurationBlock.group.label=묻기

GeneralConfigurationBlock.group.defaultThemes=기본 테마 라이브러리

GeneralConfigurationBlock.button.text.prompt=라이브러리 파일을 연 후 프롬프트 표시(&P)

GeneralConfigurationBlock.button.text.defaultTheme.enable=기본 테마 라이브러리 사용(&T)

GeneralConfigurationBlock.button.text.defaultTheme.include=기본 테마 포함(&I)

GeneralConfigurationBlock.move.binding.group=성능 향상을 위해 로컬 바인딩을 상위로 이동

GeneralConfigurationBlock.move.binding.always=항상(&W)

GeneralConfigurationBlock.move.binding.never=안 함(&V)

GeneralConfigurationBlock.move.binding.prompt=묻기(&R)

###########################################################
#ResetImageSizeAction
ResetImageOriginalSizeAction.trans.label=이미지 크기 원래대로

ResetImageSizeAction.label=원본 크기로 재설정(&Z)
###########################################################
#Report document editor
ReportDocumentEditor.1=보고서 문서 편집기

ReportDocumentEditor.2=문서 정보

ReportDocumentEditor.3=이름:

ReportDocumentEditor.4=버전:

ReportDocumentEditor.5=페이지 수:

ReportDocumentEditor.errorMessage=편집기에서 문서를 여는 동안 오류가 발생했습니다.

###########################################################
#Export Element Dialog
ExportElementDialog.Dialog.Title=라이브러리로 요소 내보내기

ExportElementDialog.Dialog.Message=대상 라이브러리를 선택하거나 새 대상을 생성하십시오.

ExportElementDialog.label.NewFile=새 파일 이름(&N):

ExportElementDialog.ErrorMessage=파일 이름은 .rptlibrary로 끝나야 합니다.

ExportElementDialog.ErrorMessageInvalid=파일 이름이 잘못되었습니다.

ExportElementDialog.ErrorMessageDuplicate=라이브러리에 해당 요소가 이미 있습니다.

ExportElementDialog.ErrorStatusCanNotExport=해당 요소는 내보낼 수 없습니다.

ExportElementDialog.WarningMessageDuplicate.Message=구성 요소 이름이 중복되었습니다. 기존 라이브러리 구성 요소를 무시하시겠습니까?\n\n기존 구성 요소를 바꾸려면 {0}을(를) 누르십시오.\n요소 내보내기 대화 상자로 돌아가려면 {1}을(를) 누르십시오.\n작업을 취소하려면 {2}을(를) 누르십시오.

ExportElementDialog.WarningMessageDuplicate.Title=바꾸기 확인

ExportElementDialog.WarningMessageDuplicate.OverrideImage=내보낸 이미지에서 참조한 포함된 이미지가 라이브러리에 이미 있습니다. 무시하시겠습니까?

###########################################################
#NewStyleHandler
NewStyleHandler.transaction.label=새 스타일...

AdvancePropertyDescriptorProvider.DisplayName=고급

AdvancePropertyDescriptorProvider.Tooltip.Alphabetic=알파벳순으로 정렬

AdvancePropertyDescriptorProvider.Tooltip.Group=그룹별로 정렬

AdvancePropertyDescriptorProvider.Tooltip.Local=로컬 속성 표시

PropertyEditorFactory.Boolean.True=참

PropertyEditorFactory.Boolean.False=거짓

PropertyEditorFactory.Value.Auto=자동

ExportElementToLibraryAction.waring=경고

ExportElementToLibraryAction.waringMsg=요소에 이름이 없지만 해당 요소의 이름을 설정할 수 없습니다.

###########################################################
#ExportStyleDialog
ExportStyleDialog.Label.Text=테마(&T):

ExportStyleDialog.Message=라이브러리에서 내보낼 테마를 선택합니다.

ExportStyleDialog.Title=테마 선택

###########################################################
#UIUtil
UIUtil.previewconfirm.title=보고서 오류

UIUtil.previewconfirm.message=보고서에 오류가 있습니다. 미리 보기를 계속하시겠습니까?

###########################################################
#FileReportProvider
FileReportProvider.CopyConfigFile.DeleteFailure=파일 삭제 실패: {0}

###########################################################
#ShowPropertiesViewMenu
ShowPropertiesViewMenu.ActionName=속성(&I)

ShowPropertiesViewMenu.ErrorMessage=속성 뷰를 열지 못했습니다.

ShowPropertiesViewMenu.ErrorTitle=오류

###########################################################
#ResetAttributePage
ResetAttributePage.Style.Restore.TooltipText=현재 페이지의 스타일 속성을 기본 설정으로 복원합니다.

ResetAttributePage.Style.Restore.Transaction.Name=스타일 복원

###########################################################
#TextEditor
TextEditor.PasteFormattedText=서식 있는 텍스트 붙여넣기

###########################################################
#ReloadImageAction
ReloadImageAction.Text=이미지 다시 로드(&M)

###########################################################
#VariablesNodeProvider
VariablesNodeProvider.NewActionName=새 변수(&N)

VariablesNodeProvider.Dialog.Title=새 변수

VariablesNodeProvider.NodeName=변수

###########################################################
#VariableNodeProvider
VariableNodeProvider.DialogTitle=변수 편집

###########################################################
#SelectVariableDialog
SelectVariableDialog.AvailableVariables=사용 가능한 변수(&A):

SelectVariableDialog.PageSeperator=----- 페이지 변수 -----

SelectVariableDialog.ReportSeperator=----- 보고서 변수 -----

SelectVariableDialog.Title=변수 삽입

###########################################################
#VariableDialog
VariableDialog.DataType=데이터 형식(&T):

VariableDialog.DefaultValue=기본값(&V):

VariableDialog.Name=이름(&N):

VariableDialog.PageVariable=페이지 변수(&P)

VariableDialog.ReportVariable=보고서 변수(&R)

VariableDialog.VariableType=유형:

VariableDialog.Error.NameDuplicate=이름 \"{0}\"이(가) 기존 이름과 중복됩니다. 다른 이름을 선택하십시오.

###########################################################
#ExpressionBuilderConfigurationBlock
ExpressionBuilderConfigurationBlock.DefaultSyntax=기본 구문(&F):

###########################################################
#ExpressionEditor
ExpressionEditor.Error.EmptyExpression=식은 비어 있으면 안 됩니다.

ExpressionEditor.Label.Expression=식(&E):

ExpressionEditor.Title=식 편집

ExpressionSyntaxColoringPage.Accessible.Name.Background=배경 색상 선택기

ExpressionSyntaxColoringPage.Accessible.Name.Foreground=전경 색상 선택기

ExpressionSyntaxColoringPage.Button.Bold=굵게(&B)

ExpressionSyntaxColoringPage.Button.Enable=사용

ExpressionSyntaxColoringPage.Button.Italic=기울임꼴(&I)

ExpressionSyntaxColoringPage.Button.Restore=기본값 복원(&R)

ExpressionSyntaxColoringPage.Button.Strikethrough=취소선(&S)

ExpressionSyntaxColoringPage.Button.Underline=밑줄(&U)

ExpressionSyntaxColoringPage.Decscription=목록 또는 예제 텍스트 영역에서 사용자 지정하려는 스타일의 텍스트 유형을 선택하여 구문 강조를 사용자 지정합니다.

ExpressionSyntaxColoringPage.Element.Comment=주석

ExpressionSyntaxColoringPage.Element.Default=기본값

ExpressionSyntaxColoringPage.Element.Keyword=키워드

ExpressionSyntaxColoringPage.Element.String=문자열

ExpressionSyntaxColoringPage.Label.Background=배경(&O):

ExpressionSyntaxColoringPage.Label.Foreground=전경(&F):

ExpressionSyntaxColoringPage.Label.SampleText=샘플 텍스트(&S):

ExpressionSyntaxColoringPage.Label.SyntaxElement=구문 요소(&Y):

ExpressionSyntaxColoringPage.Link.ColorAndFont=기본 색상 및 글꼴은  <a href="org.eclipse.ui.preferencePages.GeneralTextEditor">텍스트 편집기</a> 와  <a href="org.eclipse.ui.preferencePages.ColorsAndFonts">색상 및 글꼴</a>  기본 설정 페이지에서 구성할 수 있습니다.

###########################################################
#VariableDropAdapter
VariableDropAdapter.TranasctionName=변수에서 데이터 항목 생성

###########################################################
#RenameInputDialog
RenameInputDialog.Message.BlankName=이름은 비워둘 수 없습니다.

RenameInputDialog.Message.DuplicateName=중복된 이름이 있습니다.

###########################################################
#Breadcrumb
BreadcrumbItemDropDown.showDropDownMenu.action.toolTip=하위 항목 표시

ToggleBreadcrumbAction.tooltip.switch.breadcrumb=이동 경로 전환

###########################################################
#HyperlinkParameterProvider Category
ReportHyperlinkParameterProvider.Category.Report=보고서 매개 변수

RenderHyperlinkParameterProvider.Category.Render=렌더링 옵션
