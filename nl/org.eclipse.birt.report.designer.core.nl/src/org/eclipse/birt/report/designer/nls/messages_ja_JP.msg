#/*******************************************************************************
# * Copyright (c) 2006, 2012 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
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
AbstractDataSetWizard.ModelTrans.Create=データセットの名前、ソース、型を定義
###########################################################
#CssErrDialog
CssErrDialog.shellTitle.ImportCssStyleMssageTitle=インポートに関するメッセージ

CssErrDialog.AreaTitle=CSS スタイルのインポートに関するメッセージ

CssErrDialog.AreaMessage=CSS スタイルのインポート中にエラーまたは警告が発生しました。

CssErrDialog.Severity=重要度 :

CssErrDialog.Message=メッセージ :

CssErrDialog.FatalError=重大なエラー

CssErrDialog.Error=エラー

CssErrDialog.Warning=警告

###########################################################
#AddGroupAction properties
AddGroupAction.actionMsg.addGroup=グループの挿入...

AddGroupAction.Menu.Text=グループの挿入...

AddGroupAction.stackMsg.addGroup=グループの挿入

###########################################################
#AddLibraryAction properties
AddLibraryAction.Error.FileNotFound=ファイル ''{0}'' が見つかりません。

AddLibraryAction.Error.FileIsNotLibrary=ファイル ''{0}'' の拡張子が ''{1}'' ではありません。

AddLibraryAction.Error.Message=ファイルはすでに存在します。

AddLibraryAction.Error.Title=エラー

AddLibraryAction.Text=ライブラリの追加...

AddLibraryAction.Dialog.Tilte=ライブラリの選択

###########################################################
#AddCSSAction properties
AddCSSAction.Text=CSS ファイルの追加...

###########################################################
#AddResourceAction properties
AddResourceAction.Text=リソースの追加(&R)...

AddResourceAction.Dialog.Tilte=リソースの選択

AddResourceAction.Error.Message=ファイルはすでに存在します。

AddResourceAction.Error.FileNotFound=ファイル ''{0}'' が見つかりません。

AddResourceAction.Error.Title=エラー

AddResourceAction.Error.FileIsNotResource=ファイル ''{0}'' はリソースではありません。

###########################################################
#AddCSSDialog
AddCSSWizardDialog.ShellText=CSS ファイルの追加

AddCSSWizardDialog.TitleArea=CSS ファイルをリソースフォルダに追加

AddCSSWizardDialog.Message=ここで選択した CSS ファイルのスタイルがリソースフォルダにインポートされます。

###########################################################
#MoveResourceDialog
MoveResourceDialog.Title=リソースの移動

MoveResourceDialog.Message=フォルダの選択

###########################################################
#NewLibraryDialog
NewLibraryDialog.Title=新規ライブラリ

NewLibraryDialog.Message=フォルダの選択

NewLibraryDialog.label.NewLibrary=新規ライブラリ名

###########################################################
#PublishCSSAction properties
PublishCSSAction.wizard.errorTitle=エラー

PublishCSSAction.wizard.message=CSS ファイルがすでに存在するディレクトリには、CSS ファイルを公開できません。公開するには、別のディレクトリを選択してください。

PublishCSSAction.wizard.msgDirErr=ターゲットディレクトリは存在していませんが、新たに作成することもできません。

PublishCSSAction.wizard.message.SourceFileNotExist=ソースファイルが存在しません。

PublishCSSAction.wizard.notvalidfolder=設定に無効なリソースフォルダが定義されています。


###########################################################
#ResourceAction properties
DeleteLibraryAction.Text=削除(&D)

CopyLibraryAction.Text=コピー(&C)

PasteLibraryAction.Text=貼り付け(&P)

MoveLibraryAction.Text=移動(&V)...

RenameLibraryAction.Text=名前の変更(&M)

NewFolderAction.Text=フォルダ(&F)

NewLibraryAction.Text=ライブラリ(&L)

NewResource.MenuGroup.Text=新規(&N)

RenameResourceAction.nameExists=同じ名前のファイルまたはフォルダがあります。

RenameResourceAction.inputDialogTitle=リソース名の変更

RenameResourceAction.inputDialogMessage=新しい名前 :

NewFolderAction.nameExists=同じ名前のフォルダがあります。

NewFolderAction.emptyName=名前を空白にすることはできません。

NewFolderAction.inputDialogTitle=新規フォルダ

NewFolderAction.inputDialogMessage=フォルダ名 :

DeleteResourceAction.Dialog.Title=リソースの削除

DeleteResourceAction.Dialog.Message=選択したリソースを削除しますか ?

PasteResourceAction.Dialog.Title=リソースの貼り付け

PasteResourceAction.ReadOnlyEncounter.Title=読み取り専用ファイル

PasteResourceAction.ReadOnlyEncounter.Message=ファイル ''{0}'' は読み取り専用です。先に、このファイルを書き込み可能にしてください。

PasteResourceAction.Dialog.Message=リソースはすでに存在しています。 新しいパラメタ名を指定(&N) :

PasteResourceAction.Dialog.FilenameSuffix.CopyOf=コピー

MoveResourceAction.Dialog.Title=リソースの移動

MoveResourceAction.Dialog.Message=リソースはすでに存在しています。置き換えますか ?

ResourceAction.ConvertToFile.URLIsNull=URL が NULL なので、ファイルに変換できません。

ResourceAction.FileRemoveFailure=ファイルを削除できませんでした。

LibraryNotExist.Dialog.Title=ライブラリを開く

LibraryNotExist.Dialog.Message=ライブラリが存在しません。リソースエクスプローラをリフレッシュしますか ?

DesignerNotExist.Dialog.Title=レポートデザインを開く

DesignerNotExist.Dialog.Message=レポートデザインが存在しません。リソースエクスプローラを更新しますか ?
###########################################################
#LibraryFileFilterAction properties
LibraryFileFilterAction.Text=フィルタ...

###########################################################
#AddStyleRuleAction properties
AddStyleAction.actionMsg.addStyleRule=スタイルの新規作成(&N)...

AddStyleAction.stackMsg.addStyle=スタイルの新規作成(&N)

###########################################################
#ApplyStyleAction properties
ApplyStyleAction.actionLabel.none=なし

ApplyStyleAction.stackMsg.applyStyle=スタイルの適用

###########################################################
#ApplyThemeAction properties
ApplyThemeAction.actionLabel.none=なし

ApplyThemeAction.stackMsg.applyStyle=テーマの適用

###########################################################
#AttributesBuilder properties
AttributesBuilder.Label.Cell=プロパティエディタ - セル

AttributesBuilder.Label.Column=プロパティエディタ - 列

AttributesBuilder.Label.Data=プロパティエディタ - データ

AttributesBuilder.Label.Generic=プロパティエディタ - {0}

AttributesBuilder.Label.Grid=プロパティエディタ - グリッド

AttributesBuilder.Label.Label=プロパティエディタ - ラベル

AttributesBuilder.Label.Library=プロパティエディタ - ライブラリ

AttributesBuilder.Label.List=プロパティエディタ - リスト

AttributesBuilder.Label.Cube=プロパティエディタ - キューブ

AttributesBuilder.Label.DynamicFilterParameter=プロパティエディタ - 動的フィルタパラメタ

AttributesBuilder.Label.MasterPage=プロパティエディタ - マスタページ

AttributesBuilder.Label.None=プロパティエディタ - なし

AttributesBuilder.Label.Picture=プロパティエディタ - イメージ

AttributesBuilder.Label.Report=プロパティエディタ - レポート

AttributesBuilder.Label.Row=プロパティエディタ - 行

AttributesBuilder.Label.Table=プロパティエディタ - テーブル

AttributesBuilder.Label.Text=プロパティエディタ - テキスト

AttributesBuilder.Label.TextData=プロパティエディタ - ダイナミックテキスト

AttributesBuilder.Label.DataSourceHandle=プロパティエディタ - データソース

AttributesBuilder.Label.VariableElementHandle=プロパティエディタ - 変数

AttributesBuilder.Label.DataSetHandle=プロパティエディタ - データセット

AttributesBuilder.Label.ScalarParameterHandle=プロパティエディタ - パラメタ

AttributesBuilder.Label.TemplateReportItemHandle=プロパティエディタ - テンプレートレポート項目

AttributesBuilder.Label.AutoText=プロパティエディタ - 定型句

AttributesBuilder.Label.ParameterGroup=プロパティエディタ - パラメタグループ

AttributesBuilder.Label.CascadingParameterGroup=プロパティエディタ - カスケードパラメタグループ

###########################################################
#AttributeView properties
AttributeView.dialg.Message.Warning=警告

AttributeView.dialg.Message.Yes=はい(&E)

AttributeView.dialg.Message.No=いいえ(&O)

AttributeView.dialg.Message.Cancel=キャンセル(&A)

AttributeView.dialg.Message.PromptMsg=すべてのローカルプロパティをクリアしますか ?

AttributeView.toolbar.tooltip.EditingToggleAction.ClearMsg=ローカルプロパティをクリアします。

AttributeView.toolbar.tooltip.EditingToggleAction.EnableEditingMsg=プロパティの編集を有効にします。

AttributeView.toolbar.tooltip.EditingToggleAction.DisabledActionMsg=この操作は、ローカル要素には行えません。

AttributeView.view.message.Emptypage= このレポートデザインの {3} のプロパティを編集するには、プロパティエディタのツールバーにあるボタンをクリックしてください。

AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg=元のライブラリ項目からプロパティを復元する

AttributeView.defaultText.noAttributeView = プロパティエディタは使用できません。
###########################################################
#BackGroundPreferencePage properties
BackGroundPreferencePage.displayname.Title=背景

###########################################################
#BasAttributePage properties
BasAttributePage.Label.Attributes=プロパティ :

BaseAttributePage.CommandStack.ResetStyle.Message=デフォルトのスタイルに戻す

###########################################################
#BaseInsertMenuAction properties
BaseInsertMenuAction.stackMsg.insertElement=要素の挿入



###########################################################
#BasePaletteFactory properties
BasePaletteFactory.AutoText.Confidential=社外秘

BasePaletteFactory.AutoTextLabel.CreatedBy=作成者 :

BasePaletteFactory.AutoTextLabel.CreatedOn=作成日 :

BasePaletteFactory.AutoTextLabel.Filename=ファイル名 :

BasePaletteFactory.AutoTextLabel.LastPrinted=最終印刷日 :

BasePaletteFactory.AutoTextLabel.Page=ページ :

BasePaletteFactory.AutoTextLabel.PageCount=ページ数 :

BasePaletteFactory.AutoTextLabel.PageXofY=X/Y ページ

BasePaletteFactory.categoryName.PointerSelect=ポインタでの選択

BasePaletteFactory.categoryName.RectangleSelect=ドラッグでの選択

BasePaletteFactory.Group.Items=項目

BasePaletteFactory.toolTip.PointerSelect=項目をクリックして選択します。複数の項目を選択するには、Ctrl キーを押しながらクリックします。

BasePaletteFactory.toolTip.RectangleSelect=選択範囲を囲むようにドラッグすると、囲んだ中のすべての項目が選択されます。

###########################################################
#BaseStylePreferencePage properties
BaseStylePreferencePage.displayname.Title=全般

###########################################################
#BgImageFieldEditor properties
BgImageFieldEditor.displayname.Browse=参照(&W)...

###########################################################
#BindingPage properties
BindingPage.Dataset.Label=データセット(&T) :

BindingPage.DataSetBindingSelector.Title.DataSet=データセットのバインド

BindingPage.DataSetBindingSelector.Title.LinkModel=データモデルのバインド

BindingPage.ReportItem.Label=レポート項目(&I) :

BindingPage.ReportItem.NoName=名称未指定

BindingPage.Label.Binding=バインド

BindingPage.MessageDlg.SaveParamBinding=ParamBinding の保存

BindingPage.None=なし

BindingPage.TableColumn.DataType=データ型

BindingPage.TableColumn.Parameter=パラメタ

BindingPage.TableColumn.Value=値

ColumnBingingDialog.Label.Warn=データセットやレポート項目の参照を変更すると、結果セットの列式がすべて無効になることがあります。

ColumnBindingDialog.Label.DataSet=データセット :

ColumnBindingDialog.Column.Expression=式

ColumnBindingDialog.DialogTitle=データバインドの選択

ColumnBindingDialog.Column.Name=名前

ColumnBindingDialog.Column.Filter=フィルタ

ColumnBindingDialog.Column.Function=関数

ColumnBindingDialog.Column.DisplayName=表示名

ColumnBindingDialog.Column.DisplayNameID=表示名 ID

ColumnBindingDialog.InputPrompt=<ここに新しい値を入力してください...>

ColumnBindingDialog.Button.Generate=カラムバインドの生成

ColumnBindingDialog.Column.DataType=データ型

ColumnBindingDialog.Column.AggregateOn=集計条件

ColumnBindingDialog.All=すべて

ColumnBindingDialog.NONE=なし

ColumnBindingDialog.Choice.DatasetFromContainer=コンテナのデータセット

ColumnBindingDialog.Choice.ReportItemFromContainer=コンテナの参照先レポート項目

ColumnBindingDialog.AGGREGATEON.NONE=N/A
ColumnBindingDialog.Text.Add=追加(&A)...

ColumnBindingDialog.Text.Edit=編集(&E)...

ColumnBindingDialog.Text.Del=削除(&R)

ColumnBindingDialog.Text.Refresh=再表示(&F)

ColumnBindingDialog.Text.AddAggr=集計の追加(&G)...

###########################################################
#BlockPreferencePage properties
BlockPreferencePage.displayname.Title=テキストブロック

###########################################################
#BlockPreferencePage properties
SizePreferencePage.displayname.Title=サイズ

###########################################################
#BorderCanvas properties
BorderCanvas.Label.Inherited=(継承)

###########################################################
#BorderPreferencePage properties
BorderPreferencePage.displayname.Color=カラー

BorderPreferencePage.displayname.Style=スタイル

BorderPreferencePage.displayname.Title=罫線

BorderPreferencePage.displayname.Width=幅

BorderPreferencePage.text.Bottom=下 :

BorderPreferencePage.text.Left=左 :

BorderPreferencePage.text.Right=右 :

BorderPreferencePage.text.Top=上 :

###########################################################
#BordersPage properties
BordersPage.Label.Borders=罫線(&B) :

BordersPage.Label.Color=カラー(&C):

BordersPage.Label.Presets=プリセット :

BordersPage.Label.Style=スタイル(&Y) :

BordersPage.Label.Width=幅(&T) :

BordersPage.Tooltip.All=すべての罫線

BordersPage.Tooltip.Bottom=下罫線

BordersPage.Tooltip.Left=左罫線

BordersPage.Tooltip.None=なし

BordersPage.Tooltip.Right=右罫線

BordersPage.Tooltip.Top=上罫線

BordersPage.text.Preview=プレビュー :

BordersPage.Trans.SelectBorder=罫線の選択

BordersPage.Trans.UnSelectBorder=罫線の選択解除

BordersPage.Trans.SelectAllborders=すべての罫線を選択する

BordersPage.Trans.UnSelectAllborders=すべての罫線の選択を解除する

###########################################################
#BoxPreferencePage properties
BoxPreferencePage.displayname.Title=ボックス

BoxPreferencePage.text.Margin=余白

BoxPreferencePage.text.Padding=パディング

###########################################################
#CommentsPreferencePage properties
CommentsPreferencePage.displayname.Title=コメント

###########################################################
#ButtonPaneComposite properties
ButtonPaneComposite.tooltip.MasterPage=マスタページ

ButtonPaneComposite.tooltip.Normal=標準

ButtonPaneComposite.tooltip.Ruler=ルーラ

###########################################################
#SettingsPage properties
SettingsPage.dataFetchSetting.label=データソースからフェッチする最大行数(&M) :

SettingsPage.CachePreference.button.clear=キャッシュのクリア(&C)

SettingsPage.CachePreference.checkBox.text=レポートをプレビューするためにデータセットのデータをキャッシュする(&H)

SettingsPage.CachePreference.description=キャッシュサイズの定義 :

SettingsPage.CachePreference.Filter.Tooltip=データセットのキャッシュを使用します。

SettingsPage.CachePreference.label.text=行

SettingsPage.CachePreference.messageBox.info=キャッシュがクリアされました。

SettingsPage.CachePreference.messageBox.title=キャッシュのクリア

SettingsPage.CachePreference.radioBox.entireNumText=データセット全体をキャッシュする(&E)

SettingsPage.CachePreference.radioBox.numText=キャッシュして最初に使用する(&U)

###########################################################
#CascadingParametersDialog properties
CascadingParametersDialog.Button.isRequired=必須にする(&Q)

CascadingParametersDialog.default.preview.string=サンプル文字列

CascadingParametersDialog.display.controlType.comboBox=コンボボックス

CascadingParametersDialog.display.controlType.listBox=リストボックス

CascadingParametersDialog.Label.button.changeFormat=変更(&G)...

CascadingParametersDialog.Label.button.createNew.dataset=新規作成(&W)...

CascadingParametersDialog.Label.cascadingParam.name=カスケードパラメタグループ名(&R) :

CascadingParametersDialog.Label.column.displayText=表示テキスト

CascadingParametersDialog.Label.column.dataSet=データセット

CascadingParametersDialog.Label.column.name=名前

CascadingParametersDialog.Label.column.value=値

CascadingParametersDialog.Label.createNewParam=<パラメタの新規作成...>

CascadingParametersDialog.Label.dataSets=データセット(&S) :

CascadingParametersDialog.Label.dataType=データ型(&Y)

CascadingParametersDialog.Label.defaultValue=デフォルト値(&V) :

CascadingParametersDialog.Label.displayType=表示型(&S) :

CascadingParametersDialog.Label.formatAs=書式設定 :

CascadingParametersDialog.Label.group.general=全般

CascadingParametersDialog.Label.group.moreOptions=詳細設定

CascadingParametersDialog.Label.group.properties=プロパティ

CascadingParametersDialog.Label.helpText=ヘルプテキスト(&H) :

CascadingParametersDialog.Label.listLimit=一覧の制限(&L) :

CascadingParametersDialog.Label.NoColumnAvailable=<使用可能な列がありません>

CascadingParametersDialog.Label.param.name=名前(&N):

CascadingParametersDialog.Label.parameters=パラメタ

CascadingParametersDialog.Label.preview=書式付きプレビュー

CascadingParametersDialog.Label.groupPromptText=プロンプトテキスト(&P) :

CascadingParametersDialog.Label.parameterPromptText=プロンプトテキスト(&X) :

CascadingParametersDialog.Label.selectDataSet=<データセットの選択>

CascadingParametersDialog.Label.selectDisplayColumn=<表示列が未設定です>

CascadingParametersDialog.Label.selectValueColumn=<値列が未設定です>

CascadingParametersDialog.Label.values=個の値

CascadingParametersDialog.Radio.Single=単一のデータセット(&N)

CascadingParametersDialog.Radio.Mutli=複数のデータセット(&U)

CascadingParametersDialog.Choice.NullValue=NULL 値

CascadingParametersDialog.Choice.BlankValue=空白値

CascadingParametersDialog.Choice.SelectValue=<値の選択...>

CascadingParametersDialog.Button.Add=追加(&A)

CascadingParametersDialog.Button.Edit=編集(&I)

CascadingParametersDialog.Button.Delete=削除(&T)

CascadingParametersDialog.Button.IsMultiple=複数の値を許可する(&W)

CascadingParametersDialog.DefalutValue.Add=追加(&D)

CascadingParametersDialog.ExpressionEditor.Title=式の編集

CascadingParametersDialog.Title.AddCascadingParameter=カスケードパラメタグループの追加

CascadingParametersDialog.Title.EditCascadingParameter=カスケードパラメタグループの編集

CascadingParametersDialog.items.None=<なし>

###########################################################
#CellPageGenerator properties
CellPageGenerator.List.Borders=罫線

CellPageGenerator.List.CellPadding=パディング

CellPageGenerator.List.Font=フォント

CellPageGenerator.List.General=全般


###########################################################
#ChoiceSetFactory properties
ChoiceSetFactory.choice.Auto=自動

ChoiceSetFactory.choice.None=なし

###########################################################
#CodePageAction properties
CodePageAction.actionText.code=スクリプト

###########################################################
#ColorBuilder properties
ColorBuilder.text.Auto=自動

ColorBuilder.Button.ChooseColor=カラーパネルを開く

ColorSelector.ColorDialog.Title=カラー

ColorSelector.Name=カラーセレクタ
###########################################################
#ColumnPageGenerator properties
ColumnPageGenerator.List.Borders=罫線

ColumnPageGenerator.List.CellPadding=セルのパディング

ColumnPageGenerator.List.Font=フォント

ColumnPageGenerator.List.General=全般

ColumnPageGenerator.List.Visibility=表示/非表示

###########################################################
#Commom properties
Commom.ShortDateTime.Fri=金

Commom.ShortDateTime.Mon=月

Commom.ShortDateTime.Sat=土

Commom.ShortDateTime.Sun=日

Commom.ShortDateTime.Thu=木

Commom.ShortDateTime.Tue=火

Commom.ShortDateTime.Wed=水

###########################################################
#CopyAction properties
CopyAction.text=コピー(&C)

###########################################################
#CreatePlaceHolderAction properties
CreatePlaceHolderAction.text=テンプレートのレポート項目を作成(&A)...

###########################################################
#CutAction properties
CutAction.text=切り取り(&t)

CutAction.trans=要素の切り取り

###########################################################
#dataBinding properties
dataBinding.label.selectBinding=データバインドの選択

dataBinding.message.changeDataSet=要素のデータバインドを変更しようとしています。\n\n既存のデータフィールド情報をすべて削除しますか ?

dataBinding.message.changeReference=要素のデータバインドを変更しようとしています。\n\nこの操作を実行しますか ?

dataBinding.title.changeDataSet=データセットを変更しますか ?

dataBinding.title=データバインド

dataBinding.message.haveNoName=選択しようとしたアイテムには名前が付いていません。\n\n前の手順に戻ってアイテムに名前を付けることで選択可能にしてください。

dataBinding.title.haveNoName=名前が付いていません

dataBinding.button.OK=OK(&O)

parameterBinding.title=データセットパラメタのバインド(&B)...

ParameterBindingDialog.title=データセットパラメタのバインド

dataBinding.title.generateOutputParam=バインドの生成

dataBinding.msg.generateOutputParam=データセットに1つまたは複数の出力パラメタがあります。\n\nこれらのパラメタ用にバインドを生成しますか?

###########################################################
#DataEditPart properties
DataEditPart.Figure.Dafault=データ

DataEditPart.Figure.Expression=式

DataEditPart.stackMsg.edit=データバインドの編集

DataEditPart.text.AggregateOn=集計条件

###########################################################
#DataEditPart properties
DestroyEditPart.Message=このレポート項目のコンテンツが破損しています。このレポートで作業を続けるには、まずこの項目を削除してください。そうしないと、レポートデザインファイルがさらに破損し、Report Designerが応答しなくなる可能性があります。

###########################################################
#DataPageGenerator properties
DataPageGenerator.List.Bookmark=ブックマーク

DataPageGenerator.List.Borders=罫線

DataPageGenerator.List.Duplicates=重複

DataPageGenerator.List.Expression=式

DataPageGenerator.List.Format=書式

DataPageGenerator.List.formatDateTime=日時の書式設定

DataPageGenerator.List.formatNumber=数値の書式設定

DataPageGenerator.List.formatString=文字列の書式

DataPageGenerator.List.General=全般

DataPageGenerator.List.Highlights=ハイライト

DataPageGenerator.List.HyperLink=ハイパーリンク

DataPageGenerator.List.Margin=余白

DataPageGenerator.List.Padding=パディング

DataPageGenerator.List.Section=改ページ

DataPageGenerator.List.TOC=目次

DataPageGenerator.List.Visibility=可視性

###########################################################
#dataset properties
dataset.edit=データセットの編集 - {0}

dataset.editor.settings=設定

dataset.editor.settings.dataFetchSetting.errorNumberFormat=フェッチする最大行数には、0 よりも大きい整数を入力してください。

dataset.editor.settings.dataFetchSetting.errorNumberFormatForResultSet=[結果セット番号] には正の整数を入力してください。

dataset.editor.settings.dataFetchSetting=データフェッチの設定

dataset.editor.settings.resultsetselection.resultSetSelection=結果セットの選択

dataset.editor.settings.resultsetselection.enableResultSetSelection=結果セットの選択を有効にする(&E)

dataset.editor.settings.resultsetselection.selectResultSetByName=結果セットを名前で選択(&R) :

dataset.editor.settings.resultsetselection.selectResultSetByNumber=結果セットを番号で選択(&S) :

dataset.editor.settings.dataFetchSetting.fetchAll=データソースのすべての列をフェッチ(&A)

dataset.editor.settings.cachePreference.errorNumberFormat=キャッシュの行サイズには、0 よりも大きい数値を入力してください。

dataset.editor.settings.cachePreference=キャッシュの設定

dataset.editor.computedColumns=計算列

dataset.editor.dataSource.Tooltip=データソースの表示と変更

dataset.editor.dataSource=データソース

dataset.editor.error.columnOrAliasNameAlreadyUsed=新しい列の名前またはエイリアス ({0}) は、すでにインデックス {1} の列で使用されています。

dataset.editor.error.computedColumnNameAlreadyUsed=新しい計算列の名前 ({0}) は、すでに [出力列] の列のエイリアス ({1}) として使用されています。

dataset.editor.error.duplicateParameterName=パラメタ名が重複しています。

dataset.editor.error.nonemptyDefaultvalue=デフォルト値をなくすことはできません。

dataset.editor.error.emptyName=名前は空白にできません。

dataset.editor.error.missingComputedColumnExpression=計算列 {0} の式は null または空白にできません。

dataset.editor.error.missingComputedColumnName=計算列名は null または空白にできません。

dataset.editor.error.noDataSource=データセット {0} にデータソースがありません。プロパティビューの [データソース] エントリでデータソースを選択してください。

dataset.editor.error.nonmatchedDataSource=このデータソースは、データセット "{0}" に一致しません。データセットの型 "{1}" に一致するデータソースを選択してください。

dataset.editor.error.noEditorsFound=データセットのエディタが見つかりません {0}。

dataset.editor.error.noInputParameterDefaultValue=入力パラメタ {0} に空白以外のデフォルト値を設定してください。

dataset.editor.error.title=警告

dataset.editor.error.unsupportedDataSet=このデータセット型は使用できません - {0}。

dataset.editor.error.validationParameter=入力パラメタ [{0}] の値が空です。空のままにしておきますか ?

dataset.editor.filters=フィルタ

dataset.editor.label.selectDataSource=データソースの選択(&D) :

dataset.editor.outputColumns=出力列

dataset.editor.parameters=パラメタ

dataset.editor.preview=結果のプレビュー

dataset.editor.outputparameters=出力パラメタのプレビュー

dataset.editor.title.alias=エイリアス

dataset.editor.title.columnName=列見出し

dataset.editor.title.dataType=データ型

dataset.editor.title.defaultValue=デフォルト値

dataset.editor.title.direction=方向

DataSetParameterPage.editor.title.linkToSalarParameter=リンク先のレポートパラメタ

dataset.editor.title.displayName=表示名

dataset.editor.title.displayNameKey=表示名キー

dataset.editor.title.expression=式

dataset.editor.title.aggrFunc=集計

dataset.editor.title.aggrArgu=引数(&R)

dataset.editor.title.filter=フィルタ

dataset.editor.title.helpText=ヘルプテキスト

dataset.editor.title.isInput=入力

dataset.editor.title.isNullable=Null を許可する

dataset.editor.title.isOptional=オプション

dataset.editor.title.isOutput=出力

dataset.editor.title.name=名前

dataset.editor.title.nativeName=ネイティブの名前

dataset.editor.title.operator=演算子

dataset.editor.title.type=種類

dataset.editor.title.value1=値 1

dataset.editor.title.value2=値 2

dataset.editor.inputDialog.displayName=表示名(&D)

dataset.editor.inputDialog.displayNameKey=表示名キー(&K)

dataset.editor.inputDialog.name=名前(&N)

dataset.editor.inputDialog.nativeName=ネイティブの名前(&A)

dataset.editor.inputDialog.type=型(&T)

dataset.editor.inputDialog.alias=エイリアス(&A)

dataset.editor.inputDialog.columnName=列名(&C)

dataset.editor.inputDialog.dataType=データ型(&T)

dataset.editor.inputDialog.expression=式(&E)

dataset.editor.inputDialog.aggrArgu=引数(&R)

dataset.editor.inputDialog.aggrFunc=集計(&G)

dataset.editor.inputDialog.filter=フィルタ(&F)

dataset.editor.inputDialog.value1=値 1(&1)

dataset.editor.inputDialog.value2=値 2(&2)

dataset.editor.inputDialog.operator=演算子(&O)

dataset.editor.inputDialog.defaultValue=デフォルト値(&V)

dataset.editor.inputDialog.direction=方向(&D)

dataset.editor.historybar.backward=戻る

dataset.editor.historybar.forward=進む

dataset.editor.historybar.to=:

dataset.error.noDataSources=使用できるデータソースがありません。今すぐデータソース追加しますか ?

dataset.error.dataSourceMissing=このデータセットにはデータソースがありません。

dataset.error.title.noDataSources=使用できるデータソースがありません。

dataset.message.create=新規データセットを作成します。

dataset.new.defaultName=データセット

dataset.new=データセットの新規作成

dataset.action.new=データセットの新規作成(&S)

dataset.join.new=ジョインデータセットの新規作成

dataset.action.join.new=ジョインデータセットの新規作成(&J)

dataset.resultset.preview.tooltip=クエリ結果をプレビューします。

dataset.resultset.preview.promptMessage.recordsNum=表示されているレコードの総数 : {0} 件

dataset.resultset.preview.promptMessage.MoreRecordsExist=結果セットには表示されていないレコードがあります。

dataset.outputparameters.preview.tooltip=出力パラメタをプレビューします。

dataset.wizard.label.datasetName=データセット名(&M) :

dataset.wizard.label.datasetType=データセットの型(&T) :

dataset.wizard.label.dataSource=データソース(&S) :

dataSourceSelectionPage.title=データセットにリンクします。

dataset.error.title.noReportDesign=エラー

dataset.error.msg.noReportDesign=使用できるレポートデザインがありません。

dataset.warning.invalidReponseStatus=カスタム ODA ページからのデザインの応答が無効です。ODA デザインセッションの応答状態 : {0}

###########################################################
#DataSetBasePage properties
DataSetBasePage.tooltip=名前は空白にできません。

DataSetBasePage.ScriptedDataSet.name=スクリプトデータセット

DataSetBasePage.ScriptedDataSource.name=スクリプト記述済みのデータソース

DataSetBasePage.Group.DataSourceSelection=データソースの選択

DataSetBasePage.CassandraScriptedDataSet.name=Cassandra スクリプト記述済みのデータセット

DataSetBasePage.CassandraScriptedDataSource.name=Cassandra スクリプト記述済みのデータソース

DataSetBindingSelector.Button.DeselectAll=すべての選択解除(&A)

DataSetBindingSelector.Button.SelectAll=すべて選択(&S)

DataSetBindingSelector.Combo.DataSet=データセット(&D) :

DataSetBindingSelector.Label.SelectBindingColumns=バインド列を選択してください。

DataSetBindingSelectorPage.Label.SelectBindingColumns=バインド列を選択してください。

DataSetBindingSelectorPage.Title=データセットのバインド

CassandraScriptedDataSource.display.name=Cassandra スクリプト記述済みのデータソース

CassandraScriptedDataSource.error.classNotFound=Cassandra スクリプト記述済みのデータソースをサポートする Hector クライアントライブラリの Jar が見つかりません。.

###########################################################
#JointDataSetPage properties
JointDataSetPage.pageName=ジョインデータセット

JointDataSetPage.page.detail=ジョインを定義するには、左右のフィールドからデータセットと列を選択してください。

JointDataSetPage.button.innerJoin=インナージョイン(&I)

JointDataSetPage.button.leftJoin=左アウタージョイン(&L)

JointDataSetPage.button.rightJoin=右アウタージョイン(&R)

JointDataSetPage.button.fullJoin=フルアウタージョイン(&O)

JointDataSetPage.joinType=ジョインの型

JointDataSetPage.error.nodataset.title=データセットが見つかりません。

JointDataSetPage.error.nodataset.right=右側のデータセットが見つかりません。

JointDataSetPage.error.nodataset.left=左側のデータセットが見つかりません。

JointDataSetPage.pageDescription=ジョインデータセットの定義

JointDataSetPage.query=クエリ

###########################################################
#DerivedDataSet properties
DerivedDataSet.action.error.message.NoDataSet=使用可能なデータセットが見つかりません。

DerivedDataSet.action.error.title.createDerivedDataSet=派生データセット作成時のエラー

DerivedDataSet.action.error.message.InvalidNumber.DataSet=使用可能なデータセットの数が 2 つ未満です。

###########################################################
#DataSetComputedColumnsPage properties
DataSetComputedColumnsPage.ComputedColumns.Tooltip=計算列の定義。

DataSetComputedColumnsPage.description=計算列の定義 :

DataSetComputedColumnsPage.duplicatedName=列名が重複しています。

DataSetComputedColumnsPage.numberName=列名を数字にすることはできません。

DataSetComputedColumnsPage.toolTipText.Down=計算列を下に移動します。

DataSetComputedColumnsPage.toolTipText.Up=計算列を上に移動します。

DataSetComputedColumnsPage.toolTipText.Remove=計算列を削除します。

DataSetComputedColumnsPage.toolTipText.New=新しい計算列を作成します。

DataSetComputedColumnsPage.toolTipText.Edit=計算列を編集します。

DataSetComputedColumnsPage.InputDialog.title.New=計算列の新規作成

DataSetComputedColumnsPage.InputDialog.title.Edit=計算列の編集

DataSetComputedColumnsPage.InputDialog.message.error.AggregationExpression=集計式には集計計算列を含められません。

DataSetComputedColumnsPage.title.ComputedColumnInputDialog=計算列

###########################################################
#DataSetFiltersPage properties
DataSetFiltersPage.description=フィルタの定義 :

DataSetFiltersPage.exception.msg.operator.notSupport=この演算子は使用できません :

DataSetFiltersPage.Filter.Tooltip=BIRT 式でデータロウをフィルタ

DataSetFiltersPage.toolTipText.Down=フィルタを下に移動します。

DataSetFiltersPage.toolTipText.Up=フィルタを上に移動します。

DataSetFiltersPage.toolTipText.Remove=フィルタを削除します。

DataSetFiltersPage.toolTipText.New=新しいフィルタを作成します。

DataSetFiltersPage.toolTipText.Edit=フィルタを編集します。

DataSetFiltersPage.title.FilterInputDialog=フィルタ

DataSetFiltersPage.messages.selectVauleUnavailable=この式では値リストを使用できません。

DataSetFiltersPage.DynamicFilter.DynamicParameter.NotFound=動的フィルタのリンクされた動的パラメタ "{0}" が存在しません。

###########################################################
#DataSetNodeProvider properties
DataSetNodeProvider.menu.text=編集(&E)

###########################################################
#DataSetParametersPage properties
DataSetParametersPage.description=入力パラメタの定義 :

DataSetParametersPage.Filter.Tooltip=データセットへのパラメタのパスと取得

DataSetParameterBindingInputDialog.Title=データセットパラメタのバインドの編集

DataSetParametersPage.ErrorTitle.InvalidInput=入力エラー。

DataSetParameterBindingInputDialog.Label.Name=パラメタ名 :

DataSetParametersPage.ErrorMessage.CanootBeNull=値は null にできません。

DataSetParameterBindingInputDialog.Title.NewParameter=パラメタの新規作成

DataSetParameterBindingInputDialog.Title.EditParameter=パラメタの編集

DataSetParameterBindingInputDialog.Label.DataType=データ型 :

DataSetParameterBindingInputDialog.Label.Value=値(&V) :

DataSetParametersPage.ParameterButton.Tooltip.Edit=レポートパラメタを編集

DataSetParametersPage.ParameterButton.Tooltip.New=レポートパラメタの新規作成

DataSetParametersPage.Prompt.new=<ここに新しいパラメタを入力してください...>

DataSetParametersPage.default.None=N/A
DataSetParametersPage.reportParam.None=なし

DataSetParametersPage.errorMessage.LinkedReportParamNotFound=データセットパラメタ "{1}" のリンクされているレポートパラメタ "{0}" は存在しません。

DataSetParametersPage.errorMessage.InvalidType.LinkedReportParam=レポートパラメタに複数の値が設定されている場合、データセットパラメタは最初の値のみを使用します。

DataSetParameterPage.cell.linkToSalarParameter=リンク先のレポートパラメタ(&L)

DataSetParameterPage.title.ParameterInputDialog=パラメタ

DataSetParameterPage.toolTipText.New=パラメタの新規作成

DataSetParameterPage.toolTipText.Edit=パラメタの編集

DataSetParameterPage.toolTipText.Remove=パラメタの削除

DataSetParameterPage.toolTipText.Up=パラメタを上に移動

DataSetParameterPage.toolTipText.Down=パラメタを下に移動

DataSetParameterPage.WarningMessage.updatePosition=一部またはすべてのデータセットパラメタの位置の値が無効です。パラメタの順序に従って位置を更新しています。

DataSetParameterPage.warning.UnmatchedParamDataType=データセットパラメタの型が、リンクされているレポートパラメタの型と一致しません。

DataSetParameterPage.multiValuesType=複数の値の型

DataSetParameterPage.warningLinkedMultipleValuesParams.title=警告ダイアログ

DataSetParameterPage.updateReportParameter.title=レポートパラメタ更新の設定

DataSetParameterPage.updateReportParameter.message=データセットパラメタの設定を使用して、リンクされているレポートパラメタを更新しますか ?

DataSetParameterPage.updateReportParameter.promptButtonYes=はい

DataSetParameterPage.updateReportParameter.promptButtonNo=いいえ

DataSetParameterPage.updateReportParameter.propmtText=次回から同じ設定を使う

PromptParameterDialog.title=デザイン属性の更新の確認

PromptParameterDialog.okButton.text=適用

PromptParameterDialog.parameterGroup.title=パラメタのデザイン属性

PromptParameterDialog.parameterGroup.label=編集されたデータセットのデザインによって変更された属性の値を使用して、選択したパラメタを更新します。

PromptParameterDialog.parameterGroup.nameColumn=名前

PromptParameterDialog.parameterGroup.previousValue=以前の値

PromptParameterDialog.parameterGroup.revisedValue=修正後の値

###########################################################
#PropertyBindingpage properties
PropertyBindingPage.datasource.property.empty=このデータソースのパブリックプロパティは、ODA 拡張で定義されていません。

PropertyBindingPage.dataset.property.empty=このデータセットのパブリックプロパティは、ODA 拡張で定義されていません。

PropertyBindingPage.dataset.queryText=クエリテキスト(&T)

PropertyBindingPage.property.tooltip=実行時のプロパティのバインドを定義します。

PropertyBindingPage.button.tooltip=式ビルダを開いて {0} の値を入力してください。

PropertyBindingPage.label.colon=:
PropertyBindingPage.label.LeftParenthesis=(
PropertyBindingPage.label.RightParenthesis=)

###########################################################
#DataSetProcess properties
DataSetProcess.action.Delete=データセットの削除

DataSetProcess.action.Edit=データセットの編集

DataSetProcess.action.Preview=プレビュー

DataSetProcess.text.DisplayName=データセット

DataSetProcess.text.Prefix=このデータソースは、次のデータセットで使用されています:

DataSetProcess.text.Show=このデータセットのエディタページを表示します。

DataSetProcess.text.Suffix=この要素を削除しますか ?

###########################################################
#datasource properties
datasource.action.edit=編集(&E)

datasource.edit=データソースの編集 - {0}

datasource.new.defaultName=データソース

datasource.new=新規データソース

datasource.action.new=新規データソース(&S)

datasource.wizard.label.dataSourceName=データソース名(&M) :

datasource.wizard.label.datasources=次の一覧のデータソース型を使って作成する(&D)

datasource.wizard.label.connectionfile=プロファイルストアの接続プロファイルを使って作成する(&C)

datasource.wizard.message.selectType=新しいデータソースのすべての設定を指定するか、定義済みの接続プロファイルを選択してください。

datasource.wizard.title.select=データソースの種類または接続プロファイルを選択します。

datasource.editor.property=プロパティのバインド

datasource.editor.duplicatedName=データソース名 "{0}" が重複しています。

datasource.error.title.noReportDesign=エラー

datasource.error.msg.noReportDesign=使用できるレポートデザインがありません。

datasource.exprotToCP.title=エクスポート

datasource.exportToCP.message="{0}" をデータソースのエクスプローラにエクスポート中です。

datasource.exportToCP.checkBox=エクスポートされたデータソース用の接続プロファイルストアを作成する(&C)

datasource.exportToCP.externalCheckBox=データソースの外部化した接続プロファイルを使用する(&U)

datasource.exportToCP.label.specifyName=接続プロファイル名(&E) :

datasource.exportToCP.errorBox.button.OK=OK

datasource.exportToCP.error.emptyFileName=空でない接続プロファイル名を指定してください。

datasource.exportToCP.error.invalidFileName=指定された接続プロファイル名には無効な文字が含まれています。

datasource.exportToCP.error.duplicatedFileName=この接続プロファイル名はすでに存在します。

datasource.exportToCP.error.alreadyExported=データソース "{0}" は、プロパティがすでに外部化されています。


###########################################################
#DataSourceProcess properties
DataSourceProcess.action.Delete=データソースの削除

DataSourceProcess.action.Edit=データソースの編集

DataSourceProcess.text.DisplayName=DataSource
DataSourceProcess.text.Prefix=このデータソースは、次のデータセットで使用されています。

DataSourceProcess.text.Show=このデータソースのエディタページを表示します。

DataSourceProcess.text.Suffix=この要素を削除しますか ?

###########################################################
#DataSourceSelectionPage properties
DataSourceSelectionPage.ScriptDataSource.DisplayName=スクリプト記述済みのデータソース

###########################################################
#DataSourcesSlotOutlineProcess properties
DataSourcesSlotOutlineProcess.action.New=新規データソース

###########################################################
#datatypes properties
datatypes.any=任意

datatypes.auto=自動

datatypes.dateTime=日時

datatypes.decimal=10 進数値

datatypes.float=浮動少数値

datatypes.integer=整数

datatypes.string=文字列

###########################################################
#DataView properties
DataView.defaultText.noDataView=データビューがありません。

###########################################################
#DataViewTreeViewerPage properties
DataViewTreeViewerPage.tooltip.DoubleClickToEdit=ダブルクリックして編集します。

DataViewTreeViewerPage.tooltip.DragToInsertColumn=列をドラッグして挿入します。

DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns=すべてのデータセット列をドラッグして挿入します。

DataViewTreeViewerPage.tooltip.DragToInsertParameter=レポートパラメタをドラッグして挿入します。

###########################################################
#DebugUtil properties
DebugUtil.importProject.fail="既存のプロジェクトをインポートできません。"

###########################################################
#DecorationFieldEditor properties
DecorationFieldEditor.text.Linethrough=取り消し線

DecorationFieldEditor.text.Overline=上線

DecorationFieldEditor.text.Underline=下線

###########################################################
#DefaultNodeProvider properties
DefaultNodeProvider.Dlg.Confirm={0} を削除して次に進むには、[はい] をクリックしてください。 それ以外の場合は [いいえ] をクリックしてください。

DefaultNodeProvider.ParameterGroup.ConfirmMessage=パラメタグループを削除しようとしています。パラメタグループを削除すると、そのグループに含まれるすべてのパラメタが削除されます。続行しますか ?

DefaultNodeProvider.ParameterGroup.ConfirmTitle=パラメタグループの削除確認

DefaultNodeProvider.Tree.Body=本文

DefaultNodeProvider.Tree.Clients={0} は次の要素で使用されています。

DefaultNodeProvider.Tree.ColumnHedings=列見出し

DefaultNodeProvider.Tree.Components = レポート項目

DefaultNodeProvider.Tree.DataSets=データセット

DefaultNodeProvider.Tree.DataSources=データソース

DefaultNodeProvider.Tree.Detail=詳細

DefaultNodeProvider.Tree.Footer=フッタ

DefaultNodeProvider.Tree.Groups=グループ

DefaultNodeProvider.Tree.Header=ヘッダ

DefaultNodeProvider.Tree.Images=埋め込まれたイメージ

DefaultNodeProvider.Tree.Invalid=ノードが無効です

DefaultNodeProvider.Tree.MasterPages=MasterPages
DefaultNodeProvider.Tree.PageSetup=ページ設定

DefaultNodeProvider.Tree.Parameters=レポートパラメタ

DefaultNodeProvider.Tree.Reference=警告

DefaultNodeProvider.Tree.Scratch=スクラッチパッド

DefaultNodeProvider.Tree.Styles=スタイル

DefaultNodeProvider.Tree.Themes=テーマ

DefaultNodeProvider.WarningDialog.Title=警告

DefaultNodeProvider.WarningDialog.EmptyList=挿入できる要素はありません。

DefaultNodeProvider.stackMsg.changeBinding=データバインドの変更

###########################################################
#DeleteAction properties
DeleteAction.text=削除(&D)

DeleteAction.trans=要素の削除

###########################################################
#DeleteColumnAction properties
DeleteColumnAction.actionMsg.delete=削除(&D)

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.actionMsg.deleteGroup=グループの削除(&G)

###########################################################
#DeleteListGroupAction properties
DeleteListGroupAction.actionMsg.deleteGroup=グループの削除(&G)

###########################################################
#DeleteRowAction properties
DeleteRowAction.actionMsg.delete=削除(&D)

###########################################################
#designer properties
designer.preview.browserTitle=プレビュー

designer.preview.preference.browser.autoSave=プレビュー前に自動的に保存する

designer.preview.preference.browser.browse=参照(&B)...

designer.preview.preference.browser.currentBrowsers=現在の外部 Web ブラウザのアダプタ(&U) :

designer.preview.preference.browser.description=BIRT レポートをプレビューする Web ブラウザを選択します。\nWeb ブラウザのアダプタが 1 つしか設定されていない場合は、\n選択内容を変更できません。

designer.preview.preference.browser.details=ブラウザプログラムの選択

designer.preview.preference.browser.program=カスタムブラウザコマンド(&S) :

designer.preview.preference.browser.svg=SVG グラフを有効にする(&E)

designer.preview.preference.browser.extbrowser.link=レポートを表示する外部ブラウザの設定方法については、<a>''{0}''</a> を参照してください。

designer.preview.preference.masterpagecontent=マスタページコンテンツを有効にする(&M)

designer.preview.preference.appcontextkey=カスタムアプリケーションのコンテキストを使用する(&U)

designer.preview.preference.browser.useExternal=常に外部ブラウザを使用する(&W)

designer.preview.preference.elementname.columnwidth=125

designer.preview.preference.elementname.customname.Enabled=true

designer.preview.preference.elementname.customname=デフォルトの名前

designer.preview.preference.elementname.defaultname.Enabled=false

designer.preview.preference.elementname.defaultname=要素名

designer.preview.preference.elementname.description.Enabled=false

designer.preview.preference.elementname.description=説明

designer.preview.preference.elementname.Enabled=true

designer.preview.preference.locale.description=ロケールの選択(&C) :

designer.preview.preference.timezone.description=タイムゾーン(&T) :

designer.preview.preference.resultset.sqldatasetpage.group.title=クエリページ

designer.preview.preference.resultset.previewpage.group.title=プレビューページ

designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting=プレビュー行制限設定

designer.preview.preference.resultset.NoLimitOfDispalyRow=表示行数制限なし(&N)

designer.preview.preference.resultset.maxNoOfSchema.description=表示するスキーマの最大数(&S) :

designer.preview.preference.resultset.maxNoOfTable.description=各スキーマに表示するテーブルの最大数(&T) :

designer.preview.preference.resultset.maxNoOfSchema.errormessage=最大スキーマ数には、0 から {0} までの数を入力してください。

designer.preview.preference.resultset.maxNoOfTable.errormessage=各スキーマの最大テーブル数には、0 から {0} までの数を入力してください。

designer.preview.preference.resultset.maxrow.description=表示する行数(&R) :

designer.preview.preference.resultset.maxrow.errormessage=最大行数には、1 から {0} までの数を入力してください。

designer.preview.preference.resultset.maxrowlevelmember.description=各行レベルに表示するメンバ数(&L) :

designer.preview.preference.resultset.maxrowlevelmember.errormessage=行レベルメンバの最大数には、1 から {0} までの数を入力してください。

designer.preview.preference.resultset.maxcolumnlevelmember.description=各列レベルに表示するメンバ数(&C) :

designer.preview.preference.resultset.maxcolumnlevelmember.errormessage=列レベルメンバの最大数には、1 から {0} までの数を入力してください。

designer.preview.preference.resultset.maxinmemorycubesize.description=インメモリキューブのサイズ(MB)(&S) :

designer.preview.preference.resultset.maxinmemorycubesize.errormessage=メモリ内の最大キューブサイズには、1 から {0} までの数を入力してください。

designer.preview.preference.server.description=内部アプリケーションサーバの設定。サーバは、BIRT Report Designer\nPreview Framework 内部で使用されます。

designer.preview.preference.server.hostDescription=ローカルマシンのホスト名または IP アドレス(&H)\n自動的に決定するには、空白のままにしてください。

designer.preview.preference.server.note=注 :

designer.preview.preference.server.portDescription=サーバがリスンするポート(&P)\n動的に割り当てるには 0 に設定します。

designer.preview.preference.server.requireRestart=サーバがすでに実行している場合は、サーバを再起動するまでこれらの設定は\n有効になりません。

designer.preview.preference.bidiOrientation.label=双方向の向き(&B) :

designer.preview.preference.bidiOrientation.auto=自動

designer.preview.preference.bidiOrientation.ltr=左から右

designer.preview.preference.bidiOrientation.rtl=右から左

designer.preview.preference.updateParameter.confirmMessage=更新の確認

designer.preview.preference.updateParameter.confirmButton=データセットパラメタのプロパティと ODA パラメタのプロパティを同期する際にクエリが変更されている場合、常に確認メッセージを表示する(&A)

designer.preview.preference.updateReportParameter.confirmButton=リンクされているレポートパラメタを更新する際に確認メッセージを表示しない(&D)

designer.preview.preference.updateReportParameter.confirmOption.always=常に続行する(&W) :

designer.preview.preference.updateReportParameter.confirmOption.update=更新(&U)

designer.preview.preference.updateReportParameter.confirmOption.ignore=無視(&I)

designer.preview.previewaction.label.webviewer=Web ビューアでレポートを表示する

designer.preview.previewaction.label=レポートを {0} として表示する

designer.preview.previewaction.ErrorMessage=iText がインストールされていないか見つからないため、PDF の生成は有効になっていません。

designer.preview.previewaction.ErrorMessage.title=警告

designer.preview.run.webviewer=Web ビューアを使用

designer.preview.run=形式 : {0}

###########################################################
#DesignerActionBarContributor properties
DesignerActionBarContributor.element.group=グループの挿入(&G)

DesignerActionBarContributor.element.InsertColumnLeft=左側に列を挿入(&L)

DesignerActionBarContributor.element.InsertColumnRight=右側に列を挿入(&R)

DesignerActionBarContributor.element.InsertRowAbove=上に行を挿入(&A)

DesignerActionBarContributor.element.InsertRowBelow=下に行を挿入(&B)

DesignerActionBarContributor.element.merge=セルを結合する(&M)

DesignerActionBarContributor.element.split=セルの分割(&S)

designerActionBarContributor.menu.data-newdataset=データセットの新規作成(&E)

designerActionBarContributor.menu.data-newdatasource=データソースの新規作成(&O)

DesignerActionBarContributor.menu.data=データ(&D)

DesignerActionBarContributor.menu.element-EditGroup=グループの編集(&E)

DesignerActionBarContributor.menu.element-LayoutMode=レイアウトモード(&D)

DesignerActionBarContributor.menu.element-showRuler=ルーラの表示(&R) :

DesignerActionBarContributor.menu.element.editDataBinding=データバインドの編集(&B)...

DesignerActionBarContributor.menu.element=要素(&l)

DesignerActionBarContributor.menu.format=書式

DesignerActionBarContributor.menu.insert=挿入(&I)

DesignerActionBarContributor.menu.page=ページ(&G)

DesignerActionBarContributor.menu.style-rule-apply=スタイルの適用(&P)

DesignerActionBarContributor.menu.style-rule-edit=スタイルの編集(&D)

DesignerActionBarContributor.menu.style-rule-remove=スタイルの削除(&R)

DesignerActionBarContributor.style.new=スタイルの新規作成(&N)...

DesignerActionBarContributor.menu.data-NewParameter=レポートパラメタの新規作成(&P)

DesignerActionBarContributor.menu.data-NewDataSetParent=新規データセット(&S)

designerActionBarContributor.menu.data-newDataset=新規データセット(&E)

designerActionBarContributor.menu.data-newJointDataset=ジョインデータセットの新規作成(&J)

DesignerActionBarContributor.menu.element.insert=挿入(&I)

DesignerActionBarContributor.menu.element.group=グループ(&G)

DesignerActionBarContributor.menu.element.style=スタイル(&Y)

DesignerActionBarContributor.menu.zoomCombo.tooltip=ズームレベル
###########################################################
#DesignerOutlinePage properties
DesignerOutlinePage.action.Delete=選択項目の削除

DesignerOutlinePage.action.Hide=ヘッダとフッタの非表示

DesignerOutlinePage.action.Redo=やり直し

DesignerOutlinePage.action.Undo=元に戻す

DesignerOutlinePage.menu.Filters=フィルタ

###########################################################
#Content outline page switch action
ContentOutlinePage.action.text.reportOutline=XML ソースのアウトラインに切り換える

ContentOutlinePage.action.text.reportXMLSourceOutline=レポートのアウトラインに切り換える

###########################################################
#DesignerPaletteFactory properties
DesignerPaletteFactory.categoryName.reportItems=レポート項目

DesignerPaletteFactory.elementLabel.data=データ

DesignerPaletteFactory.elementLabel.grid=グリッド

DesignerPaletteFactory.elementLabel.image=イメージ

DesignerPaletteFactory.elementLabel.label=ラベル

DesignerPaletteFactory.elementLabel.list=リスト

DesignerPaletteFactory.elementLabel.tabel=テーブル

DesignerPaletteFactory.elementLabel.text=テキスト

DesignerPaletteFactory.elementName.data=データ

DesignerPaletteFactory.elementName.grid=グリッド

DesignerPaletteFactory.elementName.image=イメージ

DesignerPaletteFactory.elementName.label=ラベル

DesignerPaletteFactory.elementName.list=リスト

DesignerPaletteFactory.elementName.table=テーブル

DesignerPaletteFactory.elementName.text=テキスト

DesignerPaletteFactory.elementName.textdata=ダイナミックテキスト

DesignerPaletteFactory.toolTip.dataReportItem=データセットの列または式の結果を挿入します。

DesignerPaletteFactory.toolTip.gridReportItem=その他の項目を視覚的に整列するための行と列のレイアウトを挿入します。

DesignerPaletteFactory.toolTip.imageReportItem=ファイルまたは URL からイメージを挿入します。

DesignerPaletteFactory.toolTip.labelReportItem=スタティックテキストを挿入します。

DesignerPaletteFactory.toolTip.listReportItem=ヘッダ、詳細、フッタのデータセット行に、自由書式のプレゼンテーションを挿入します。

DesignerPaletteFactory.toolTip.multiLineDataReportItem=ファイル、データセット列、または式の結果から、テキストブロック (標準のテキスト、HTML、RTF のいずれか) を挿入します。

DesignerPaletteFactory.toolTip.tableReportItem=ヘッダ、詳細、フッタのデータセット行に、列のプレゼンテーションを挿入します。

DesignerPaletteFactory.toolTip.textDataReportItem=オプションで CLOB データ式を含むテキストブロックを挿入します。

DesignerPaletteFactory.toolTip.textReportItem=オプションでインライン式を含むテキストブロック (標準のテキストまたは HTML) を挿入します。

DesignerPaletteFactory.quicktool.title=クイックツール

DesignerPaletteFactory.quicktool.agg.title=集計

DesignerPaletteFactory.quicktool.agg.toolTip=集計項目の挿入

DesignerPaletteFactory.quicktool.timeperiod.title=相対期間

DesignerPaletteFactory.quicktool.timeperiod.toolTip=TimePeriod 項目の挿入

###########################################################
#DimensionBuilderDialog properties
DimensionBuilderDialog.LabelMeasure=メジャー(&M) :

DimensionBuilderDialog.LabelUnit=単位(&U) :

DimensionBuilderDialog.Title=Dimension Builder

###########################################################
#DNDUtil properties
DNDUtil.trans.copy=要素のコピー

DNDUtil.trans.cut=要素の移動

###########################################################
#DuplicatesPage properties
DuplicatesPage.Check.HideDuplicates=重複の非表示

DuplicatesPage.Group.Repeat=重複 (繰り返し)

DuplicatesPage.Group.RepeatonPage=重複 (ページ内の繰り返し)

DuplicatesPage.Label.RepeatInfo=直前のデータ行と同じ値を非表示にします。

DuplicatesPage.Label.RepeatOnPage.1=直前の行と同じ値を非表示にしますが、

DuplicatesPage.Label.RepeatOnPage.2=\\ \\ 各ページの先頭の値は常に表示します。

DuplicatesPage.Label.ResetonGroup=次のグループのリセット

DuplicatesPage.Radio.Repeat=繰り返し

DuplicatesPage.Radio.RepeatonPage=ページ内で繰り返し

###########################################################
#EditAction properties
EditAction.text=編集(&E)

###########################################################
#EditGroupAction properties
EditGroupAction.stackMsg.editGroup=グループの編集

###########################################################
#EditGroupAction properties
InsertGroupHeaderFooterAction.stackMsg.editGroup=グループヘッダ/フッタの挿入

InsertGroupHeaderFooterAction.Text.Header=ヘッダ

InsertGroupHeaderFooterAction.Text.Footer=フッタ

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.stackMsg.deleteGroup=グループの削除

###########################################################
#EditLabelAction properties
EditLabelAction.text=編集(&E)

EditorDragGuidePolicy.pixels.label=ピクセル

EditorGuide.bottom.label=下 :

EditorGuide.left.label=左 :

EditorGuide.right.label=右 :

EditorGuide.top.label=上 :

###########################################################
#Element properties
Element.AbstractScalarParameter.dataType=データ型(&D) :

Element.AutoText.type=種類 :

Element.Cell.colSpan=列の幅 :

Element.Cell.column=列 :

Element.Cell.drop=ドロップ(&D) :

Element.Cell.height=高さ :

Element.Cell.rowSpan=行の幅 :

Element.Cell.slot.content=コンテンツ :

Element.Cell.verticalAlign=垂直方向の配置 :

Element.Cell.width=幅 :

Element.Column.alignment=配置 :

Element.Column.repeat=繰り返し :

Element.Column.width=幅(&W) :

Element.Column.suppressDuplicates=重複を表示しない

Element.Container.slot.reportItems=レポート項目 :

Element.Data.action=実行 :

Element.Data.helpText=ヘルプテキスト :

Element.Data.helpTextID=ヘルプテキストキー

Element.TextData.valueExpr=値の式(&U):

Element.DataSet.afterClose.toolTip=データセットを閉じた直後に呼び出されます。

Element.DataSet.afterClose=閉じた後 :

Element.DataSet.afterOpen.toolTip=データセットを開いた直後に呼び出されます。

Element.DataSet.afterOpen=開いた後 :

Element.DataSet.beforeClose.toolTip=データセットを閉じる直前に呼び出されます。

Element.DataSet.beforeClose=閉じる前 :

Element.DataSet.beforeOpen.toolTip=データセットを開いく直前に呼び出されます。

Element.DataSet.beforeOpen=開く前 :

Element.DataSet.cachedMetaData=キャッシュされたメタデータ :

Element.DataSet.columnHints=列のヒント :

Element.DataSet.computedColumns=計算列 :

Element.DataSet.dataSource=データソース :

Element.DataSet.filter=フィルタ:

Element.DataSet.inputParameters=入力パラメタ :

Element.DataSet.onFetch.toolTip=データセットからデータ行をフェッチする直前に呼び出されます。

Element.DataSet.onFetch=フェッチ時 :

Element.DataSet.outputParameters=出力パラメタ :

Element.DataSet.paramBindings=パラメタバインド :

Element.DataSet.resultSet=結果セット :

Element.DataSource.afterClose.toolTip=データソースを閉じた直後に呼び出されます。

Element.DataSource.afterClose=閉じた後 :

Element.DataSource.afterOpen.toolTip=データソースを開いた直後に呼び出されます。

Element.DataSource.afterOpen=開いた後 :

Element.DataSource.beforeClose.toolTip=データソースを閉じる直前に呼び出されます。

Element.DataSource.beforeClose=閉じる前 :

Element.DataSource.beforeOpen.toolTip=データソースを開いく直前に呼び出されます。

Element.DataSource.beforeOpen=開く前 :

Element.ExtendedItem.extension=拡張子名 :

Element.ExtendedItem.extensionName=拡張子名 :

Element.FreeForm.slot.reportItems=レポート項目 :

Element.GraphicMasterPage.columns=列 :

Element.GraphicMasterPage.columnSpacing=列の間隔 :

Element.GraphicMasterPage.slot.contents=コンテンツ :

Element.Grid.slot.columns=列 :

Element.Grid.slot.row=行 :

Element.Image.action=実行 :

Element.Image.altText=代替テキスト(&A) :

Element.Image.altTextID=代替テキストキー(&K) :

Element.Image.helpText=ヘルプテキスト :

Element.Image.helpTextID=ヘルプテキストキー

Element.Image.imageName=イメージ名 :

Element.Image.scale=スケール :

Element.Image.size=サイズ :

Element.Image.source=ソース :

Element.Image.typeExpr=式の入力 :

Element.Image.uri=uri:
Element.Image.valueExpr=値式 :

Element.JdbcDataSource.dataSource=データソース :

Element.JdbcDataSource.driver=ドライバ :

Element.JdbcDataSource.password=パスワード :

Element.JdbcDataSource.url=Url:
Element.JdbcDataSource.userName=ユーザ名 :

Element.JdbcSelectDataSet.getStatement.toolTip=クエリのカスタム SQL 文を返します。

Element.JdbcSelectDataSet.getStatement=Get 文 :

Element.JdbcSelectDataSet.statement=文 :

Element.Label.action=実行 :

Element.Label.helpText=ヘルプテキスト :

Element.Label.helpTextID=ヘルプテキストキー

Element.Label.text=テキスト:

Element.Label.textID=テキストキー(&T) :

Element.Line.orientation=向き :

Element.List.slot.columnHeader=columnHeader:
Element.List.slot.detail=詳細 :

Element.List.slot.footer=フッタ :

Element.List.slot.group=グループ :

Element.List.slot.header=ヘッダ :

Element.ListGroup.slot.footer=フッタ :

Element.ListGroup.slot.header=ヘッダ :

Element.Listing.filter=フィルタ:

Element.Listing.onFinish.toolTip=この要素のデータセットから最後の行が読み込まれた後で、フッタバンドが作成される前に呼び出されます。

Element.Listing.onFinish=終了時 :

Element.Listing.onRow.toolTip=この要素のデータセットから各行が取得された際に、その行のコンテンツが作成される前に呼び出されます。

Element.Listing.onRow=行ごと :

Element.Listing.onStart.toolTip=データセットを開いた後、ヘッダバンドを作成して、この要素のデータセットから最初の行が取得される前に呼び出されます。

Element.Listing.onStart=開始時 :

Element.Listing.pageBreakInterval=改ページの間隔(&K):

Element.Listing.sorting=ソート :

Element.ListingGroup.filter=フィルタ:

Element.ListingGroup.groupName=名前

Element.ListingGroup.interval=間隔 :

Element.ListingGroup.intervalRange=間隔の範囲 :

Element.ListingGroup.keyExpr=キーの式

Element.ListingGroup.onFinish.toolTip=この要素のデータセットから最後の行が読み込まれた後で、フッタバンドが作成される前に呼び出されます。

Element.ListingGroup.onFinish=終了時 :

Element.ListingGroup.onRow.toolTip=この要素のデータセットから各行を取得した際に、その行のコンテンツを作成する前に呼び出されます。

Element.ListingGroup.onRow=行ごと :

Element.ListingGroup.onStart.toolTip=データセットを開いた後、ヘッダバンドを作成して、この要素のデータセットから最初の行が取得される前に呼び出されます。

Element.ListingGroup.onStart=開始時 :

Element.ListingGroup.pageBreakAfter=要素後 :

Element.ListingGroup.pageBreakBefore=要素前 :

Element.ListingGroup.sort=ソート :

Element.ListingGroup.sortDirection=ソート方向 :

Element.ListingGroup.toc=目次 :

Element.MasterPage.bottomMargin=下余白(&B) :

Element.MasterPage.columns=列 :

Element.MasterPage.columnSpacing=間隔(&S) :

Element.MasterPage.height=高さ (&E):

Element.MasterPage.leftMargin=左余白(&L) :

Element.MasterPage.margin=余白 :

Element.MasterPage.orientation=向き(&R) :

Element.MasterPage.rightMargin=右余白(&R) :

Element.MasterPage.slot.contents=コンテンツ :

Element.MasterPage.topMargin=上余白(&T) :

Element.MasterPage.type=型(&Y) :

Element.MasterPage.width=幅 (&W):

Element.Module.author=作成者(&U) :

Element.Module.base=基本:

Element.Module.colorPalette=カラーパレット :

Element.Module.comments=コメント(&C) :

Element.Module.configVars=設定変数 :

Element.Module.createdBy=作成環境(&C) :

Element.Module.description=説明(&E) :

Element.Module.descriptionID=説明キー :

Element.Module.helpGuide=ヘルプガイド :

Element.Module.images=イメージ :

Element.Module.includeLibraries=インクルードするライブラリ :

Element.Module.includeResource=リソースファイル(&E) :

Element.Module.includeScripts=インクルードするスクリプト :

Element.Module.initialize.toolTip=レポートの実行を開始する際に呼び出されます。

Element.Module.initialize=初期化 :

Element.LayoutModule.theme=テーマ(&H) :

Element.Module.title=タイトル(&I) :

Element.Module.titleID=タイトルキー :

Element.Module.units=単位(&S) :

Element.MultiLineData.contentTypeExpr=コンテンツ型の式 :

Element.MultiLineData.valueExpr=値式 :

Element.OdaDataSet.privateDriverDesignState=プライベートドライバの設計情報 :

Element.OdaDataSet.privateDriverProperties=プライベートドライバのプロパティ :

Element.OdaDataSet.publicDriverProperties=パブリックドライバのプロパティ :

Element.OdaDataSet.queryScript.toolTip=TBD:

Element.OdaDataSet.queryScript=スクリプト:

Element.OdaDataSet.queryText=クエリ :

Element.OdaDataSet.resultSetName=結果セット名 :

Element.OdaDataSet.type=次のように入力します。

Element.OdaDataSource.driverName=ドライバ名 :

Element.OdaDataSource.privateDriverProperties=プライベートドライバのプロパティ :

Element.OdaDataSource.publicDriverProperties=パブリックドライバのプロパティ :

Element.Parameter.helpText=ヘルプテキスト :

Element.Parameter.helpTextID=ヘルプテキストキー(&Y) :

Element.Parameter.hidden=非表示 :

Element.Parameter.promptTextID=プロンプトテキストキー(&K) :

Element.ParameterGroup.helpText=ヘルプテキスト :

Element.ParameterGroup.helpTextID=ヘルプテキストキー(&Y) :

Element.ParameterGroup.slot.parameters=パラメタ :

Element.ParameterGroup.startExpanded=開始時に展開 :

Element.ParameterGroup.promptTextID=プロンプトテキストキー(&K) :

Element.ReportDesign.afterCloseDoc.toolTip=生成フェーズでレポート文書ファイルを閉じた直後に呼び出されます。

Element.ReportDesign.layoutPreference=レイアウトの設定 :

Element.ReportDesign.afterCloseDoc=文書を閉じた後 :

Element.ReportDesign.afterFactory.toolTip=生成フェーズでの処理が終了した際に、レポート文書 (該当する場合) を閉じた後に呼び出されます。これは、生成フェーズで呼び出される最後のメソッドです。

Element.ReportDesign.afterFactory=Factory での処理後 :

Element.ReportDesign.afterOpenDoc.toolTip=生成フェーズでレポート文書を開いた直後に呼び出されます。

Element.ReportDesign.afterOpenDoc=文書を開いた後 :

Element.ReportDesign.afterRender.toolTip=プレゼンテーション時のアクションを開始した後に呼び出されます。

Element.ReportDesign.afterRender=レンダリング後 :

Element.ReportDesign.beforeCloseDoc.toolTip=生成フェーズでレポート文書ファイルを閉じる直前に呼び出されます。

Element.ReportDesign.beforeCloseDoc=文書を閉じる前 :

Element.ReportDesign.beforeFactory.toolTip=生成フェーズでの処理を開始する際に、initialize( ) メソッドを実行した後、レポート文書 (該当する場合) を開く前に呼び出されます。

Element.ReportDesign.beforeFactory=Factory での処理前 :

Element.ReportDesign.beforeOpenDoc.toolTip=生成フェーズでレポート文書を開く直前に呼び出されます。

Element.ReportDesign.beforeOpenDoc=文書を開く前 :

Element.ReportDesign.beforeRender.toolTip=プレゼンテーション時のアクションを開始する前に呼び出されます。

Element.ReportDesign.beforeRender=レンダリング前 :

Element.ReportDesign.displayName=表示名(&Y) :

Element.ReportDesign.eventHandlerClass=イベントハンドラのクラス(&C) :

Element.ReportDesign.iconFile=サムネイル(&N) :

Element.ReportDesign.refreshRate=更新頻度 :

Element.ReportDesign.bidiLayoutOrientation=レポートの向き(&R) :

Element.ReportElement.comments=コメント(&C) :

Element.ReportElement.Cube=データ(&D) :

Element.ReportElement.customXml=カスタム XML :

Element.ReportElement.displayName=表示名(&Y) :

Element.ReportElement.displayNameID=表示名キー :

Element.ReportElement.extends=拡張:

Element.ReportElement.javaClass=Java クラス :

Element.ReportElement.name=名前(&N):

Element.ReportElement.propertyMask=プロパティマスク :

Element.ReportElement.style=スタイル(&Y) :

Element.ReportItem.bookmark=ブックマーク(&B) :

Element.ReportItem.dataSet=データセット :

Element.ReportItem.height=高さ(&H) :

Element.ReportItem.onCreate.toolTip=生成フェーズで要素を作成する際に実行されました。

Element.ReportItem.onCreate=作成時 :

Element.ReportItem.onRender.toolTip=プレゼンテーションフェーズで要素をレンダリング用に準備する際に実行されます。

Element.ReportItem.onRender=レンダリング時 :

Element.ReportItem.paramBindings=パラメタバインド :

Element.ReportItem.toc=目次(&T) :

Element.ReportItem.visibility=可視性 :

Element.ReportItem.width=幅(&W) :

Element.ReportItem.x=X :

Element.ReportItem.y=Y :

Element.ReportItem.altText=代替テキスト

Element.ReportItem.altTextID=代替テキストキー

Element.Row.bookmark=ブックマーク :

Element.Row.height=高さ(&H) :

Element.Row.slot.cell=セル :

Element.Row.visibility=可視性 :

Element.ScalarParameter.alignment=配置 :

Element.ScalarParameter.allowBlank=空白を許可する :

Element.ScalarParameter.allowNull=Null を許可する :

Element.ScalarParameter.concealValue=値を隠す :

Element.ScalarParameter.controlType=コントロール型(&C) :

Element.ScalarParameter.defaultValue=デフォルト値 :

Element.ScalarParameter.dynamicList.dataSetName=データセット名 :

Element.ScalarParameter.dynamicList.labelExpr=ラベル式 :

Element.ScalarParameter.dynamicList.valueExpr=値式 :

Element.ScalarParameter.dynamicList=動的なリスト :

Element.ScalarParameter.format=書式:

Element.ScalarParameter.selectionList.fixedOrder=固定順序 :

Element.ScalarParameter.selectionList.mustMatch=必ず一致 :

Element.ScalarParameter.selectionList.selectionList=選択リスト :

Element.ScalarParameter.selectionList=選択リスト :

Element.ScalarParameter.promptTextID=プロンプトテキストキー(&K) :

Element.ScriptDataSet.close.toolTip=データセットを閉じます。

Element.ScriptDataSet.close=閉じる :

Element.ScriptDataSet.describe.toolTip=結果セットを記述します。

Element.ScriptDataSet.describe=記述 :

Element.ScriptDataSet.fetch.toolTip=DataRow 型のオブジェクトを作成し、読み込み、返します。

Element.ScriptDataSet.fetch=フェッチ :

Element.ScriptDataSet.open.toolTip=データセットを開きます。

Element.ScriptDataSet.open=開く :

Element.ScriptDataSource.close.toolTip=データソースへの接続を切断します。

Element.ScriptDataSource.close=閉じる :

Element.ScriptDataSource.open.toolTip=データソースへの接続を確立します。

Element.ScriptDataSource.open=開く :

Element.SimpleMasterPage.floatingFooter=浮動フッタ(&F)

Element.SimpleMasterPage.footerHeight=フッタの高さ (&F):

Element.SimpleMasterPage.headerHeight=ヘッダの高さ (&H):

Element.SimpleMasterPage.showFooterOnLast=最終ページにフッタを表示(&F)

Element.SimpleMasterPage.showHeaderOnFirst=先頭ページにヘッダを表示(&H)

Element.SimpleMasterPage.slot.pageFooter=ページフッタ :

Element.SimpleMasterPage.slot.pageHeader=ページヘッダ :

Element.Style.background.attachment=添付ファイル(&A) :

Element.Style.background.color=背景色(&B) :

Element.Style.background.image=背景イメージ(&G) :

Element.Style.background.positionX=左右の位置(&H) :

Element.Style.background.positionY=上下の位置(&V) :

Element.Style.background.repeat=繰り返し(&R) :

Element.Style.background=背景 :

Element.Style.border.bottom.color=下(&M) :

Element.Style.border.bottom.style=下(&B) :

Element.Style.border.bottom.width=下(&:) :

Element.Style.border.fillColor=罫線の塗りつぶしの色 :

Element.Style.border.hmiddle.color=中央横の罫線の色 :

Element.Style.border.hmiddle.style=中央横の罫線の線種 :

Element.Style.border.hmiddle.width=中央横の罫線の幅 :

Element.Style.border.left.color=左(&F) :

Element.Style.border.left.style=左(&L) :

Element.Style.border.left.width=左(&E) :

Element.Style.border.right.color=右(&H) :

Element.Style.border.right.style=右(&R) :

Element.Style.border.right.width=右(&I) :

Element.Style.border.top.color=上(&P) :

Element.Style.border.top.style=上(&T) :

Element.Style.border.top.width=上(&O) :

Element.Style.border.vmiddle.color=中央縦の罫線の色 :

Element.Style.border.vmiddle.style=中央縦の罫線の線種 :

Element.Style.border.vmiddle.width=中央縦の罫線の幅 :

Element.Style.border=罫線 :

Element.Style.box=ボックス :

Element.Style.canShrink=縮小可能(&K)

Element.Style.color=カラー(&C):

Element.Style.display=表示(&P):

Element.Style.font.family=フォント(&F) :

Element.Style.font.size=サイズ(&Z) :

Element.Style.font.style=スタイル(&T) :

Element.Style.font.textLineThrough=取り消し線(&L)

Element.Style.font.textOverline=上線(&O)

Element.Style.font.textUnderline=下線(&U)

Element.Style.font.variant=バリエーション :

Element.Style.font.weight=太さ(&W) :

Element.Style.font=フォント(&F) :

Element.Style.format.dateTime=日付 / 時間 :

Element.Style.format.number.align=数値の配置 :

Element.Style.format.number=数値 :

Element.Style.format.string=文字列:

Element.Style.format=書式:

Element.Style.highlight.rules=ハイライトの規則 :

Element.Style.highlight.testExpr=テスト式のハイライト :

Element.Style.letterSpacing=文字間隔(&T) :

Element.Style.lineHeight=行の高さ(&N) :

Element.Style.map.rules=マップ規則 :

Element.Style.map.testExpr=マップのテスト式 :

Element.Style.margin.bottom=下(&B) :

Element.Style.margin.left=左(&E) :

Element.Style.margin.right=右(&I) :

Element.Style.margin.top=上(&P) :

Element.Style.masterPage=マスターページ(&P):

Element.Style.orphans=オーファン(&O) :

Element.Style.padding.bottom=下(&B) :

Element.Style.padding.left=左(&L) :

Element.Style.padding.right=右(&R) :

Element.Style.padding.top=上(&T) :

Element.Style.padding=パディング :

Element.Style.pageBreakAfter=要素後(&A) :

Element.Style.pageBreakBefore=要素前(&B) :

Element.Style.pageBreakInside=要素内(&I) :

Element.Style.section=セクション :

Element.Style.showIfBlank=空白でも表示(&W)

Element.Style.text.align=文字列の位置揃え(&L) :

Element.Style.width=幅(&W):

Element.Style.height=高さ(&H):
#bidi_hcg
Element.Style.text.direction=双方向テキスト(&B) :

Element.Style.text.indent=文字列のインデント(&X) :

Element.Style.text.transform=文字列の位置変更(&R) :

Element.Style.text=テキスト:

Element.Style.verticalAlign=垂直方向の配置(&A):

Element.Style.whiteSpace=スペース(&I):

Element.Style.widows=ウィドウ(&D) :

Element.Style.wordSpacing=単語間の間隔(&W) :

Element.Table.caption=キャプション :

Element.Table.captionID=キャプションキー :

Element.Table.repeatHeader=ヘッダの繰り返し :

Element.Table.slot.columnHeader=columnHeader:
Element.Table.slot.columns=列 :

Element.Table.slot.detail=詳細 :

Element.Table.slot.footer=フッタ :

Element.Table.slot.group=グループ :

Element.Table.slot.header=ヘッダ :

Element.TableGroup.slot.footer=フッタ :

Element.TableGroup.slot.header=ヘッダ :

Element.Text.content=コンテンツ :

Element.Text.contentID=コンテンツキー(&K) :

Element.Text.contentType=コンテンツの種類(&P) :

Element.TemplateElement.name=名前(&N)

Element.Toc.Sytle=スタイル(&Y)

Element.VariableElement.name=名前(&N):

Element.VariableElement.type=型(&T) :

Element.VariableElement.value=値(&V) :

###########################################################
#EmbeddedImageNodeProvider properties
EmbeddedImageNodeProvider.action.New=新しく埋め込まれたイメージ(&B)

###########################################################
#EmbeddedImagesNodeProvider properties
EmbeddedImagesNodeProvider.action.New=新しく埋め込まれたイメージ(&B)

###########################################################
#ElementIdDescriptorProvider properties
ElementIdDescriptorProvider.Display.Element.ID=要素 ID(&E) :

###########################################################
#EmptyEditPart properties
EmptyEditPart.Msg.EmptyLib=ライブラリにビジュアル要素がありません。ビジュアル要素を追加するには、項目をパレットからライブラリのアウトラインビューへドラッグするか、アウトラインビューの［挿入］メニューを使用します。

EmptyEditPart.Msg.NonVisual=選択した要素は、ビジュアル要素ではありません。

###########################################################
#error properties
error.duplicateName=この名前は、同じ名前空間ですでに使用されています。

error.DataSource.emptyName=データソース名は空白にできません。

error.DataSet.emptyName=データセット名は空白にできません。

error.OutputColumn.emptyName=出力列名は空白にできません。

error.invalidName="{0}" は無効な名前です。名前には "." (ピリオド)、"/" (スラッシュ)、"\\" (円マーク)、"!" (感嘆符)、";" (セミコロン)、"," (コンマ) を使用できません。

###########################################################
#ExceptionHandler properties
ExceptionHandler.Label.ErrorCode=エラーコード

ExceptionHandler.Label.ErrorMessage=エラーメッセージ

ExceptionHandler.Label.PluginId=プラグイン ID:

ExceptionHandler.Label.PluginName=プラグイン名:

ExceptionHandler.Label.PluginProvider=プラグインのプロバイダ:

ExceptionHandler.Label.PluginVersion=バージョン:

ExceptionHandler.Label.UnknownPlugin=不明なプラグイン

ExceptionHandler.Meesage.UnexceptedExceptionOccur=内部で例外が発生しました。詳しくはログファイルを参照してください。

ExceptionHandler.Message.BirtExceptionOccur=BIRT で例外が発生しました。

ExceptionHandler.Message.CausedBy=発生元: {0}

ExceptionHandler.Message.FileNotFound=見つからないファイル:

ExceptionHandler.Message.OutOfMemory=空きメモリが不足しています。

ExceptionHandler.Message.PartInitError=部分初期化中にエラーが発生しました。

ExceptionHandler.Message.SemanticFail=Semantic Checker でエラーが発生しました。

ExceptionHandler.Message.UnexceptedExceptionOccur=内部で例外が発生しました。詳しくはログファイルを参照してください。

ExceptionHandler.Message.UnknownHost=不明なホストです :

ExceptionHandler.Title.Error=エラー

ExceptionHandler.Title.FileNotFound=ファイルが見つかりません。

ExceptionHandler.Title.PartInitError=部分初期化中にエラーが発生しました。

ExceptionHandler.Title.UnknownHost=不明なホストです。

ExceptionHandler.Meesage.ExceptionOccur=予期しない例外が発生しました。 詳しくはログファイルを参照してください。

###########################################################
#ExportAction properties
ExportAction.actionMsg.export=エクスポート

###########################################################
#ExportToLibraryAction properties
ExportToLibraryAction.action.text=ライブラリにエクスポート(&X)...

ExportToLibraryAction.Button.Cancel=キャンセル

ExportToLibraryAction.Button.No=いいえ

ExportToLibraryAction.Button.Yes=はい

ExportToLibraryAction.Dialog.Message=\nエクスポート先のライブラリには、同じデザイン要素があります。 上書きしますか ?

ExportToLibraryAction.Dialog.Title=要素のエクスポート

ExportToLibraryAction.Message.RememberDecision=次回から同じ設定を使う(&R)


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.action.text=ライブラリにエクスポート(&X)...

ExportToCPSouceAction.action.text=接続プロファイルへの外部化(&O)...


###########################################################
#LibraryExplorerTreeViewPage tooltips
LibraryElementsToolHandleExtends_message=要素には明示的にデータセットが指定されていないローカルバインドが含まれているため、実行時にパフォーマンスが低下する場合があります。 パフォーマンスを向上するためローカルバインドを親に移動しますか ?

LibraryElementsToolHandleExtends_question=パフォーマンスを向上させるためにローカルバインドを親に移動しますか ?

LibraryElementsToolHandleExtends_toggle=次回から同じ設定を使う(&R)

LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline=アウトラインビューへドラッグ＆ドロップ

LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout=レイアウトエディタへドラッグ＆ドロップ

LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout=アウトラインビューまたはレイアウトエディタへドラッグ＆ドロップ

###########################################################
#ExpressionBuidler properties
ExpressionBuidler.Dialog.Title=式ビルダ (JavaScript)

ExpressionBuidler.Title=式ビルダ

ExpressionBuilder.tooltip.Sort=ソート

ExpressionBuilder.toolTipText.Button=式ビルダを開く

###########################################################
#ExpressionBuilder properties
ExpressionBuilder.Label.Category=カテゴリ(&C) :

ExpressionBuilder.Label.Functions=ダブルクリックして挿入(&I) :

ExpressionBuilder.Label.Hint=ヒント

ExpressionBuilder.Label.Operators=演算子 :

ExpressionBuilder.Label.SubCategory=サブカテゴリ(&S) :

ExpressionBuilder.Message.Prompt=[式] フィールドに式を入力してください。使用可能なオブジェクトの一覧を参照し、式に使用する項目をダブルクリックしてコピーします。

ExpressionBuilder.ToolTip=式ビルダを開く

ExpressionBuilder.toolTipText.validate=検証

ExpressionBuilder.Script.NoError=このスクリプトに構文エラーはありませんでした。

ExpressionBuilder.Script.Error=[行 :{0}、列 :{1}] {2}

ExpressionBuilder.Script.Warning=警告

ExpressionBuilder.Script.Confirm=\n現在のスクリプトに構文エラーがあります。保存しますか ?

ExpressionBuilder.toolTipText.calendar=カレンダ

ExpressionBuilder.Calendar.Button.OK=OK

ExpressionButtonProvider.Constant=定数

ExpressionButtonProvider.Javascript=JavaScript 構文

###########################################################
#ExpressionPropertyDescriptor properties
ExpressionPropertyDescriptor.text.Edit=計算式ビルダ(&X)...

###########################################################
#ExpressionProvider properties
ExpressionProvider.Category.Context=コンテキスト

ExpressionProvider.Category.BirtObjects=BIRT 関数

ExpressionProvider.Category.DataSets=使用可能なデータセット

ExpressionProvider.Category.NativeObjects=ネイディブ JavaScript 関数

ExpressionProvider.Category.ColumnBinding=使用可能なカラムバインド

ExpressionProvider.Category.Operators=演算子

ExpressionProvider.Category.Parameters=レポートパラメタ

ExpressionProvider.Category.DataCubes=現在のデータキューブ

ExpressionProvider.Category.Variables=変数

ExpressionProvider.Label.All=---すべて---

ExpressionProvider.Operator.Add=2 つの値を加算します。

ExpressionProvider.Operator.AddTo=現在の値に加算するショートカットです。

ExpressionProvider.Operator.And=2 つの式を調べ、両方の式が True の場合に、True 値を返します。

ExpressionProvider.Operator.Assign=値を別の値と等しくします。

ExpressionProvider.Operator.BitAnd=A & B : A と B のビット単位の AND を返します。

ExpressionProvider.Operator.BitOr=A | B : A と B のビット単位の OR を返します。

ExpressionProvider.Operator.Dec=値を減少させ、結果を返します。

ExpressionProvider.Operator.DividingFrom=現在の値を除算するショートカットです。

ExpressionProvider.Tooltip.ColumnBinding=参照先

ExpressionProvider.Operator.Divides=左側の値を右側の値で除算し、結果を返します。

ExpressionProvider.Operator.Equals=項が等しい場合に、True 値を返します。

ExpressionProvider.Operator.Greater=A > B : A が B より大きい場合に、True 値を返します。

ExpressionProvider.Operator.GreaterEquals=A >= B : A が B 以上の場合に、True 値を返します。

ExpressionProvider.Operator.Inc=値を増加させ、結果を返します。

ExpressionProvider.Operator.LeftBracket=左かっこ

ExpressionProvider.Operator.RightBracket=右かっこ

ExpressionProvider.Operator.Less=A < B : A が B より小さい場合に、True 値を返します。

ExpressionProvider.Operator.LessEqual=A <= B : A が B 以下の場合に、True 値を返します。

ExpressionProvider.Operator.Mult=2 つの値を乗算します。

ExpressionProvider.Operator.MultTo=現在の値を乗算するショートカットです。

ExpressionProvider.Operator.Not=!A : A が false である場合は true を、A が true である場合は false を返します。

ExpressionProvider.Operator.NotEqual=A <> B : A が B と等しくない場合に、True 値を返します。

ExpressionProvider.Operator.Or=2 つの式を調べ、どちらか一方の式だけが True の場合に、True 値を返します。

ExpressionProvider.Operator.ReturnDec=値を返してから、減少させます。

ExpressionProvider.Operator.ReturnInc=値を返してから、増加させます。

ExpressionProvider.Operator.Sub=1 つの値からもう 1 つの値を減算します。

ExpressionProvider.Operator.SubFrom=現在の値から減算するショートカットです。

ExpressionProvider.Operators.Assignment=代入

ExpressionProvider.Operators.Comparison=比較

ExpressionProvider.Operators.Computational=演算

ExpressionProvider.Operators.Logical=論理

ExpressionProvider.EditBindings=バインドの編集...

ExpressionProvider.Expression.RowNumName=RowNum

ExpressionProvider.Expression.RowNumTooltip=現在の行番号を返す式です。

ExpressionProvider.Variables=変数

ExpressionProvider.Variables.Report=レポート変数

ExpressionProvider.Variables.Page=ページ変数

ExpressionTreeSupport.Category.Global=グローバル

###########################################################
#ExpressionValueCellEditor properties
ExpressionValueCellEditor.buildExpressionAction=<式の構築...>

ExpressionValueCellEditor.selectValueAction=<値の選択...>

ExpressionValueCellEditor.title=値の選択

###########################################################
#ExtendedEditPart properties
ExtendedEditPart.edit={0} の編集

###########################################################
#ExtendedItemPageGenerator properties
ExtendedItemPageGenerator.List.Bookmark=ブックマーク

ExtendedItemPageGenerator.List.TOC=目次

###########################################################
#FilterHandleProvider properties
FilterHandleProvider.ColumnName.Value=値

FilterHandleProvider.Label.Filterby=フィルタ条件 :

FilterHandleProvider.Modify.SaveValues=値の保存

###########################################################
#filters properties
filters.empty.columnName=式は空白にできません。

filters.error.title=入力エラー。

filters.operators.any=次の値のいずれか

filters.operators.between=次の値の間

filters.operators.bottomN=下位 (n) 項目

filters.operators.bottomPercent=下位 (n) %

filters.operators.equal=次の値に等しい

filters.operators.false=False

filters.operators.greaterThan=次の値より大きい

filters.operators.greaterThanEqual=次の値以上

filters.operators.lessThan=次の値より小さい

filters.operators.lessThanEqual=次の値以下

filters.operators.like=次の値に類似

filters.operators.notBetween=次の値の間以外

filters.operators.notEqual=次の値に等しくない

filters.operators.notNull=Not Null

filters.operators.null=Null

filters.operators.topN=上位 (n) 個の項目

filters.operators.topPercent=上位 (n) パーセント

filters.operators.true=True

filters.prompt.new=<ここに新しいフィルタを入力してください...>

###########################################################
#FixTableLayoutCalculator properties
FixTableLayoutCalculator.Error.CannotParse=入力値をパースできません。

###########################################################
#FlowMoveChildCommand properties
FlowMoveChildCommand.transLabel.moveElement=要素の移動

###########################################################
#FontPreferencePage properties
FontPreferencePage.displayname.Title=フォント

FontPreferencePage.label.fontDecoration=文字飾り :

FontPreferencePage.text.Preview=サンプルのプレビュー

FontPreferencePage.text.PreviewContent=AaBbYyZz

###########################################################
#FontSizeBuilder properties
FontSizeBuilder.displayname.Auto=自動

###########################################################
#FormatBuilder properties
FormatAdapter.DisplayName.None=自動

FormatBuilder.Title=書式ビルダ

###########################################################
#FormatDateTimeAttributePage properties
FormatDateTimeAttributePage.Trans.SetDateTimeFormat = 日付/時間の形式を設定します。

###########################################################
#FormatDateTimePage properties
FormatDateTimePage.label.custom.settings.label=サンプルの書式 (次のいずれかをコピーして変更)(&P):

FormatDateTimePage.label.custom.settings=カスタム設定

FormatDateTimePage.label.format.code=書式コード(&C) :

FormatDateTimePage.label.format.page=書式設定(&F) :

FormatDateTimePage.label.format.locale=ロケール(&L) :

FormatDateTimePage.label.general.preview.group=書式付きプレビュー

###########################################################
# LOCALE SPECIAL:
# The datetime pattern string in this text should be formatted locale-dependent if needed
FormatDateTimePage.label.guide.format=yyyy/mm/dd hh:mm:ss a

FormatDateTimePage.label.guide.text=yyyy/MM/dd hh:mm:ss の形式で入力してください。

FormatDateTimePage.label.preview.dateTime=プレビューする日時(&P) :

FormatDateTimePage.label.preview.group=プレビュー

FormatDateTimePage.label.preview.label=書式付きプレビュー :

FormatDateTimePage.label.table.column.format.name=サンプルの書式

FormatDateTimePage.label.table.column.format.result=サンプルの書式の結果

FormatDateTimePage.label.table.column.format.code=サンプルコード

FormatDateTimePage.preview.invalid.dateTime=プレビューする日時形式が無効です。

FormatDateTimePage.preview.invalid.formatCode=書式コードが無効です。

###########################################################
#FormatDateTimePreferencePage properties
FormatDateTimePreferencePage.formatDateTime.title=日時の書式設定

###########################################################
#FormatNumberAttributePage properties
FormatNumberAttributePage.Trans.SetNumberFormat = 数値の書式を設定します。

###########################################################
#FormatNumberPage properties
FormatNumberPage.currency.symbol.none=記号なし

FormatNumberPage.label.currency.settings=通貨の設定

FormatNumberPage.label.custom.preview.group=プレビュー

FormatNumberPage.label.custom.preview.label=書式付きプレビュー :

FormatNumberPage.label.custom.settings.lable=サンプルの書式 (次のいずれかをコピーして変更)(&P):

FormatNumberPage.label.custom.settings=カスタム設定

FormatNumberPage.label.decimal.places=小数点以下の桁数(&D) :

FormatNumberPage.label.rounding.mode=丸めモード(&R) :

FormatNumberPage.label.fixed.settings=固定設定

FormatNumberPage.label.format.code=書式コード(&C) :

FormatNumberPage.label.format.number.page=書式設定(&F) :

FormatNumberPage.label.format.number.locale=ロケール(&L) :

FormatNumberPage.label.general.preview.group=書式付きプレビュー

FormatNumberPage.label.negative.numbers=負の数値(&N) :

FormatNumberPage.label.percent.settings=パーセントの設定

FormatNumberPage.label.preview.number=プレビューーする数値(&P) :

FormatNumberPage.label.scientific.settings=指数の設定

FormatNumberPage.label.symbol.position=記号の位置(&Y) :

FormatNumberPage.label.symbol=記号(&B):

FormatNumberPage.label.table.column.format.code=サンプルの書式

FormatNumberPage.label.table.column.format.result=サンプルの書式の結果

FormatNumberPage.label.use1000sSeparator=桁区切りを使用する(&1)

FormatNumberPage.label.useLeadingZero=先頭に 0 を付ける

FormatNumberPage.label.useSymbolSpace=シンボルスペースを使用する(&U)

FormatNumberPage.preview.invalidFormatCode=書式コードが無効です。

FormatNumberPage.preview.invalidNumber=プレビューする数値が無効です。

FormatNumberPage.symblePos.after=後ろ

FormatNumberPage.symblePos.before=前

FormatNumberPage.roundingMode.halfUp=Half Up

FormatNumberPage.roundingMode.halfDown=Half Down

FormatNumberPage.roundingMode.halfEven=Half Even

FormatNumberPage.roundingMode.up=上へ

FormatNumberPage.roundingMode.down=下へ

FormatNumberPage.roundingMode.ceiling=Ceiling

FormatNumberPage.roundingMode.floor=Floor

FormatNumberPage.roundingMode.unnecessary=不要

###########################################################
#FormatNumberPreferencePage properties
FormatNumberPreferencePage.formatNumber.title=数値の書式設定

###########################################################
#FormatPreferencePage properties
FormatPreferencePage.displayname.Title=書式

###########################################################
#FormatStringAttributePage properties
FormatStringAttributePage.Trans.SetStringFormat = 文字列の書式を設定します。

###########################################################
#FormatStringPage properties
FormatStringPage.default.preview.text=サンプル文字列

FormatStringPage.label.custom.preview.label=書式付きプレビュー :

FormatStringPage.label.custom.settings.label=サンプルの書式 (次のいずれかをコピーして変更)(&P):

FormatStringPage.label.customSettings=カスタム設定

FormatStringPage.label.format.code=書式コード(&C) :

FormatStringPage.label.formatStringAs=書式設定(&F) :

FormatStringPage.label.locale=ロケール(&L) :

FormatStringPage.label.preview.group=プレビュー

FormatStringPage.label.preview.string=プレビューする文字列(&R):

FormatStringPage.label.previewWithFormat=書式付きプレビュー

FormatStringPage.label.table.collumn.exampleFormatCode=サンプルの書式

FormatStringPage.label.table.collumn.exampleFormatResult=サンプルの書式の結果

FormatStringPage.previewText.invalidFormatCode=書式コードが無効です。

###########################################################
#FormatStringPreferencePage properties
FormatStringPreferencePage.formatString.title=文字列の書式

###########################################################
#FormPage properties
FormPage.Button.Add=追加(&A)

FormPage.Button.AddWithDialog=追加(&A)...

FormPage.Button.AddWithDialog.PROP=ファイルを追加(&A)...

FormPage.Button.AddWithDialog.JAR=ファイルを追加(&L)...

FormPage.Button.AddWithDialog.JS=ファイルを追加(&.)...

FormPage.Button.Add.AggregateOn=集計の追加(&G)

FormPage.Button.AddWithDialog.AggregateOn=集計の追加(&G)...

FormPage.Button.Delete=削除(&R)

FormPage.Button.DeleteFile=ファイルを削除(&R)

FormPage.Button.DeleteFile.Alt1=ファイルを削除(&I)

FormPage.Button.DeleteFile.Alt2=ファイルを削除(&F)

FormPage.Button.Down=下に移動(&N)

FormPage.Button.Down.Alt1=下に移動(&W)

FormPage.Button.Down.Alt2=下に移動(&D)

FormPage.Button.Edit=編集(&D)

FormPage.Button.EditWithDialog=編集(&D)...

FormPage.Button.Up=上に移動(&U)

FormPage.Button.Up.Alt1=上に移動(&P)

FormPage.Button.Up.Alt2=上に移動(&E)

FormPage.Button.Binding.Refresh=再表示(&F)

FormPage.Button.Binding.Clear=クリア(&C)

FormPage.Menu.ModifyProperty=プロパティの変更

FormPage.toolTipText.Up=上に移動

FormPage.toolTipText.Down=下に移動
###########################################################
#GeneralInsertMenuAction properties
GeneralInsertMenuAction.text.Data=データ(&D)

GeneralInsertMenuAction.text.DynamicText=ダイナミックテキスト(&Y)...

GeneralInsertMenuAction.text.Grid=グリッド(&G)

GeneralInsertMenuAction.text.Image=イメージ(&I)

GeneralInsertMenuAction.text.Label=ラベル(&L)

GeneralInsertMenuAction.text.List=リスト(&S)

GeneralInsertMenuAction.text.Table=テーブル(&B)

GeneralInsertMenuAction.text.Text=テキスト(&T)

###########################################################
#GeneralPreferencePage properties
GeneralPreferencePage.errorMsg.duplicate.styleName=スタイル名が重複しています。

GeneralPreferencePage.label.styleNameDuplicate=スタイル名 "{0}" はすでに存在します。別の名前を付けてください。

GeneralPreferencePage.label.styleNamePredefind=スタイル名 "{0}" は定義済みです。別の名前を付けてください。

GeneralPreferencePage.label.customStyle=ユーザ設定のスタイル(&C)...

GeneralPreferencePage.Label.CustomStyle=名前:

GeneralPreferencePage.Label.Note=注 : * スタイルプロパティを特定の種類のレポート要素に適用してください。

GeneralPreferencePage.Label.PreDefinedStyle=レポート要素の選択 :

GeneralPreferencePage.label.predefinedStyle=定義済みのスタイル *(&P)

GeneralPreferencePage.label.nameEmpty=スタイル名を空白にすることはできません。

###########################################################
#GridEditPart properties
GraphicalEditorWithFlyoutPalette_Redo.Text0=やり直し(&R)

GraphicalEditorWithFlyoutPalette_Redo.Text1={0} をやり直し(&R)

GraphicalEditorWithFlyoutPalette_Redo.ToolTip0=やり直し

GraphicalEditorWithFlyoutPalette_Redo.ToolTip1={0} をやり直し

GraphicalEditorWithFlyoutPalette_Undo.Text0=元に戻す(&U)

GraphicalEditorWithFlyoutPalette_Undo.Text1={0} を元に戻す(&U)

GraphicalEditorWithFlyoutPalette_Undo.ToolTip0=元に戻す

GraphicalEditorWithFlyoutPalette_Undo.ToolTip1={0} を元に戻す

GridEditPart.GUIDEHANDLE_TEXT = グリッド

###########################################################
#GridFigure properties
GridFigure.BORDER_TEXT=グリッド

###########################################################
#GridPageGenerator properties
GridPageGenerator.List.Bookmark=ブックマーク

GridPageGenerator.List.Borders=罫線

GridPageGenerator.List.Font=フォント

GridPageGenerator.List.General=全般

GridPageGenerator.List.Margin=余白

GridPageGenerator.List.Section=改ページ

GridPageGenerator.List.TOC=目次

GridPageGenerator.List.Visibility=可視性

###########################################################
#GridProvider properties
GridProvider.action.text=行の挿入(&S)

###########################################################
#GroupDialog properties
GroupDialog.Dialog.GroupDetail=グループの詳細

GroupDialog.Label.Bookmark=ブックマーク(&B) :

GroupDialog.Dialog.TOCGroup=目次

GroupDialog.Dialog.TOC=項目の式(&T) :

GroupDialog.Dialog.TOCStyle=スタイル(&Y) :

GroupDialog.DialogTitle.NoData=初期化エラー

GroupDialog.DialogTitle.SemanticCheckFailed=Semantic Checker でエラーが発生しました。

GroupDialog.ErrorMesaage.IllegalGroup=このグループは正しくありません。

GroupDialog.ErrorMessage.NoData=使用できるデータ行がありません。

GroupDialog.Label.FilterSorting=フィルタとソート

GroupDialog.Label.GroupOn=グループ化(&G) :

GroupDialog.Label.HeaderFooter=ヘッダとフッタ

GroupDialog.Label.IncludeFooter=グループフッタを含める(&F)

GroupDialog.Label.IncludeHeader=グループヘッダを含める(&H)

GroupDialog.Label.Interval=間隔(&V) :

GroupDialog.PageBreakBefore=要素前(&O) :

GroupDialog.PageBreakAfter=要素後(&F) :

GroupDialog.PageBreakInside=要素内(&I) :

GroupDialog.Label.IntervalBase=丸めた数値を間隔の基準値として使用する(&X) :

GroupDialog.Label.Name=名前(&M):

GroupDialog.Label.Range=範囲(&E) :

GroupDialog.Tab.Binding=データのバインド

GroupDialog.Tab.Filter=フィルタ

GroupDialog.Tab.Sorting=ソート

GroupDialog.RepeatHeader=ヘッダの繰り返し(&P)

GroupDialog.Title.Edit=グループの編集

GroupDialog.Title.New=グループの新規作成

GroupDialog.PageBreak=改ページ

GroupDialog.buttion.HideDetail=詳細の非表示(&H)

GroupDialog.toolTipText.openExprButton=式ビルダを開く

GroupDialog.Button.Ascending=昇順(&S)

GroupDialog.Button.Descending=降順(&C)
###########################################################
#GroupHandleProvider properties
GroupHandleProvider.Label.Groups=グループリスト :

###########################################################
#GroupProvider properties
GroupProvider.action.text=グループの編集(&E)

###########################################################
#GuiExtensionManager properties
GuiExtensionManager.label.name=拡張項目

GuiExtensionManager.tooltip.insert={0} の挿入

###########################################################
#HighlightHandleProvider properties
HighlightHandleProvider.text.Preview=AaBbCcYyZz  (フォントサイズ =

###########################################################
#HighlightRuleBuilderDialog properties
HightlightRuleBuilder.button.add=追加(&A)

HightlightRuleBuilder.button.add.tooltip=値リストに値を追加します。

HightlightRuleBuilder.button.delete=削除(&M)

HightlightRuleBuilder.button.delete.tooltip=リストから選択した値を削除します。

HightlightRuleBuilder.button.deleteall=すべて削除(&O)

HightlightRuleBuilder.button.deleteall.tooltip=リストからすべての値を削除します。

HightlightRuleBuilder.button.edit=編集(&E)...

HightlightRuleBuilder.button.edit.tooltip=値リストで選択した値を編集します。

HightlightRuleBuilder.label.value=値(&V) :

HightlightRuleBuilder.list.item1=値

HightlightRuleBuilder.MsgDlg.Msg=警告

HightlightRuleBuilder.MsgDlg.Title=値は空白にできません。

HightlightRuleBuilder.text.AND=および

HighlightRuleBuilderDialog.choice.ValueOfThisDataItem=このデータ項目の値

HighlightRuleBuilderDialog.text.AND= ～

HighlightRuleBuilderDialog.text.Any=次の値のいずれか

HighlightRuleBuilderDialog.text.BackgroundColor=背景色(&B) :

HighlightRuleBuilderDialog.text.Between=次の値の間

HighlightRuleBuilderDialog.text.Color=カラー(&C):

HighlightRuleBuilderDialog.text.Default=自動

HighlightRuleBuilderDialog.text.ET=次の値に等しい

HighlightRuleBuilderDialog.text.False=False

HighlightRuleBuilderDialog.text.Font=フォント(&F) :

HighlightRuleBuilderDialog.text.GE=次の値以上

HighlightRuleBuilderDialog.text.GT=次の値より大きい

HighlightRuleBuilderDialog.text.LE=次の値以下

HighlightRuleBuilderDialog.text.Like=次の値に類似

HighlightRuleBuilderDialog.text.LT=次の値より小さい

HighlightRuleBuilderDialog.text.NE=次の値に等しくない

HighlightRuleBuilderDialog.text.NotBetween=次の値の間以外

HighlightRuleBuilderDialog.text.NotNull=Not Null

HighlightRuleBuilderDialog.text.Null=Null

HighlightRuleBuilderDialog.text.Preview=プレビュー :

HighlightRuleBuilderDialog.text.PreviewContent=AaBbCcYyZz
HighlightRuleBuilderDialog.text.Size=サイズ(&Z) :

HighlightRuleBuilderDialog.text.Title=ハイライト

HighlightRuleBuilderDialog.text.True=True

HighlightRuleBuilderDialog.tooltip.ExpBuilder=式ビルダを開く

HighlightRuleBuilderDialog.tooltip.Bold=太字

HighlightRuleBuilderDialog.tooltip.Italic=斜体

HighlightRuleBuilderDialog.tooltip.Underline=下線

HighlightRuleBuilderDialog.tooltip.Text_Line_Through=テキストの取り消し線

HighlightRuleBuilderDialog.text.applyStyle=スタイルの適用(&Y) :

HighlightRuleBuilderDialog.displayText.None=なし

HighlightRuleBuilderDialog.text.Description={0} に条件付き書式を適用します。条件式と書式を指定してください。\n条件式が True と評価されると、書式が適用されます。

HighlightRuleBuilderDialog.text.Description.Element.Table=テーブル

HighlightRuleBuilderDialog.text.Description.Element.Grid=グリッド

HighlightRuleBuilderDialog.text.Description.Element.List=リスト

HighlightRuleBuilderDialog.text.Description.Element.Row=行

HighlightRuleBuilderDialog.text.Description.Element.Column=列

HighlightRuleBuilderDialog.text.Description.Element.DataItem=データ項目

HighlightRuleBuilderDialog.text.Description.Element.Cell=セル

HighlightRuleBuilderDialog.text.Group.Condition=条件(&D)

HighlightRuleBuilderDialog.text.Group.Format=書式

HighlightRuleBuilderDialog.text.Description.Element.ReportElement=レポート要素

###########################################################
#HighlightsPage properties
HighlightsPage.Button.Add=追加(&A)...

HighlightsPage.Button.Delete=削除(&R)

HighlightsPage.Dialog.EditHighlight=ハイライトの編集

HighlightsPage.Dialog.NewHighlight=ハイライトの新規作成

HighlightsPage.Label.Highlights=ハイライトの一覧 :

HighlightsPage.TableColumn.Condition=条件

HighlightsPage.TableColumn.Preview=プレビュー :

HighlightsPage.trans.Add=ハイライトの規則の追加

HighlightsPage.trans.Delete=ハイライトの規則の削除

HighlightsPage.trans.Edit=ハイライトの規則の編集

HighlightsPage.trans.MoveDown=ハイライトの規則を下に移動

HighlightsPage.trans.MoveUp=ハイライトの規則を上に移動

HighlightsPage.toolTipText.MoveDown=ハイライトの規則を下に移動

HighlightsPage.toolTipText.MoveUp=ハイライトの規則を上に移動

HighlightsPage.label.duplicate=重複(&C)

HighlightsPage.toolTipText.duplicate=ハイライトの規則の複製

HighlightsPage.trans.Duplicate=ハイライトの規則の複製

###########################################################
#HighlightsPreferencePage properties
HighlightsPreferencePage.displayname.Condition=条件

HighlightsPreferencePage.displayname.Preview=プレビュー

HighlightsPreferencePage.displayname.Title=ハイライト

HighlightsPreferencePage.text.Add=追加(&A)...

HighlightsPreferencePage.text.Delete=削除(&R)

HighlightsPreferencePage.text.Description=ハイライトの一覧 :

HighlightsPreferencePage.text.Edit=編集(&D)...

HighlightsPreferencePage.toolTipText.Up=ハイライト部分を上に移動

HighlightsPreferencePage.toolTipText.Down=ハイライト部分を下に移動

HighlightsPreferencePage.text.Duplicate=複製(&D)

HighlightsPreferencePage.toolTipText.Duplicate=ハイライトの複製


###########################################################
#HyperlinkBuilder properties
HyperlinkBuilder.Column.Parameters=パラメタ

HyperlinkBuilder.DrillThrough.SelectTargetReport=ターゲットレポートを選択します。

HyperlinkBuilder.DrillThrough.SelectTargetAnchor=ターゲットアンカーを選択します。

HyperlinkBuilder.DrillThrough.CreateLinkExpr=ターゲットのブックマークに一致するリンク式の作成

HyperlinkBuilder.DrillThrough.ShowTargetReport=ターゲットのレポートの表示

HyperlinkBuilder.DrillThrough.SelectFormat=ターゲットレポートの書式を選択します。

HyperlinkBuilder.DrillThrough.Tooltip=ツールヒント

HyperlinkBuilder.Step.1=手順 1 :

HyperlinkBuilder.Step.2=手順 2 :

HyperlinkBuilder.Step.3=手順 3 :

HyperlinkBuilder.Step.4=手順 4 :

HyperlinkBuilder.Step.5=手順 5 :

HyperlinkBuilder.Step.6=手順 6 :

HyperlinkBuilder.ParameterRequired=必須

HyperlinkBuilder.ParamterBuilder.Add.Title=ハイパーリンクパラメタの追加

HyperlinkBuilder.ParamterBuilder.Edit.Title=ハイパーリンクパラメタの編集

HyperlinkBuilder.Column.DataType=データ型

HyperlinkBuilder.Column.Values=値

HyperlinkBuilder.DialogTitle=ハイパーリンクのオプション

HyperlinkBuilder.ErrorMessage.InvalidReport=ターゲットレポートデザインが見つかりません。定義済みパラメータ情報が使用できません。

HyperlinkBuilder.ErrorMessage.ReportReqired=レポートファイルを指定してください。

HyperlinkBuilder.DrillThroughTargetBookmark=ターゲットのブックマーク(&T)

HyperlinkBuilder.DrillThroughLinkExpression=リンク式(&E) :

HyperlinkBuilder.Label.Bookmark=ブックマーク(&B) :

HyperlinkBuilder.Label.Format=書式:

HyperlinkBuilder.Label.LinkedExpression=リンクされた式(&E) :

HyperlinkBuilder.DrillThroughTargetToc=ターゲットレポートの目次エントリ(&B)

HyperlinkBuilder.DrillThroughSameFrame=同じフレーム(&S)

HyperlinkBuilder.DrillThroughNewWindow=新しいウィンドウを開く(&N)

HyperlinkBuilder.DrillThroughWholePage=ページ全体(&W)

HyperlinkBuilder.DrillThroughParentFrame=親フレーム(&P)

HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName=パラメタ名が重複しています。

HyperlinkBuilder.Label.Location=場所(&L) :

HyperlinkBuilder.Label.Location.hint=(例 : http://host/path、folder/file)

HyperlinkBuilder.Label.Parameters=レポートパラメタ :

HyperlinkBuilder.Label.Report=レポート :

HyperlinkBuilder.Label.SelectType=ハイパーリンクの種類 :

HyperlinkBuilder.DrillThroughHtml=HTML
HyperlinkBuilder.Label.Target=ターゲット(&T) :

HyperlinkBuilder.Label.Tooltip=ツールヒント(&I) :

HyperlinkBuilder.Radio.Bookmark=内部ブックマーク(&K)

HyperlinkBuilder.DrillThroughPdf=PDF
HyperlinkBuilder.Radio.DrillThrough=詳細表示(&D)

HyperlinkBuilder.ReportDesignButton=レポートデザイン(&R) :

HyperlinkBuilder.ReportDocumentButton=レポート文書(&C) :

HyperlinkBuilder.Radio.None=リンクなし(&O)

HyperlinkBuilder.Radio.Uri=&URI
HyperlinkBuilder.BrowseForFile=ファイルの参照

HyperlinkBuilder.Button.AddParam=追加(&A)...

HyperlinkBuilder.Button.EditParam=編集(&)...

HyperlinkBuilder.Button.RemoveAllParam=すべて削除(&L)

HyperlinkBuilder.Button.RemoveParam=削除(&M)

HyperlinkBuilder.OpenExpression=式ビルダを開く

HyperlinkBuilder.FileNameError.Title=エラー

HyperlinkBuilder.FileNameError.Message=選択したファイルは無効です。

HyperlinkBuilder.TargetReportFormat=ターゲットレポートの形式設定(&F) :

HyperlinkParameterBuilder.Label.DataType=データ型 :

HyperlinkParameterBuilder.Label.Parameter=パラメタ(&P):

HyperlinkParameterBuilder.Label.Value=値(&V) :

HyperlinkParameterBuilder.Lable.Required=必須 :

HyperlinkParameterBuilder.Required.Choice.No=いいえ

HyperlinkParameterBuilder.Required.Choice.Yes=はい

###########################################################
#HyperLinkPage properties
HyperLinkPage.Label.LnikTo=リンク先(&L) :

HyperLinkPage.Label.None=なし

HyperLinkPage.Menu.Save=ハイパーリンクの変更

HyperLinkPage.Button.Text=編集(&T)...

HyperLinkPage.toolTipText.Button=ハイパーリンクのオプションを開く　

###########################################################
#I18nPage properties
I18nPage.text.Note=注 : ローカライズを設定するには、レポートを選択し、[プロパティエディタ] の [リソース] タブでリソースファイルの名前を指定してください。

###########################################################
#ImageBuilder properties
ImageBuilder.Chooser.Title=イメージファイルを開く

ImageBuilder.DialogTitle.Edit=イメージ項目の編集

ImageBuilder.DialogTitle.ImportFailed=インポ?[トエラ?[

ImageBuilder.DialogTitle.LoadingFailed=イメージの読み込みエラー

ImageBuilder.DialogTitle.New=イメージ項目の新規作成

ImageBuilder.ErrorMessage.FileLoaded=このファイル名はすでに存在します。別の名前を指定して再ロードしてください。

ImageBuilder.ErrorMessage.LoadingFailed=イメージを正しく読み込めません。

ImageBuilder.BrowserResourceDialog.Title=イメージファイルの参照

ImageBuilder.BrowserResourceDialog.Message=リソースパスのフォルダからイメージファイルを選択してください。

ImageBuilder.ErrorMessage.OutOfMemory=イメージが大きすぎるため、メモリに入りきりません。

ImageBuilder.FileDialog.FilterMessage=サポートされているイメージファイル

ImageBuilder.Button.Browser=参照...

ImageBuilder.Button.Import=イメージの追加(&A)...

ImageBuilder.Button.SelectImageData=イメージデータの選択(&L)...

ImageBuilder.Button.Insert=挿入(&S)

ImageBuilder.Label.SelectFrom=イメージの選択元 :

ImageBuilder.Label.EnterURI=URI を入力(&N) :

ImageBuilder.Label.EnterResourceFile=リソースファイルの入力(&N) :

ImageBuilder.Label.EnterExpr=動的なイメージの式を入力(&N) :

ImageBuilder.Type.URI=&URI
ImageBuilder.Type.ResourceFile=共有リソース内のイメージファイル(&I)

ImageBuilder.Type.EmbededImage=埋め込まれたイメージ(&E)

ImageBuilder.Type.DynamicImage=動的なイメージ(&D)

ImageBuilder.Description.URI=Web 上のイメージへのリンク

ImageBuilder.Description.ResourceFile=共有リソース内のイメージへのリンク

ImageBuilder.Description.Embedded=レポートへのイメージの埋め込み(&M)

ImageBuilder.Description.Dynamic=データセット (データベース) からのイメージの取得

ImageBuilder.Button.Browser.Tooltip=イメージファイルの参照

ImageBuilder.Reload.Tooltip=イメージの再ロード

###########################################################
#ImageEditPart properties
ImageEditPart.trans.editImage=イメージの変更

###########################################################
#ImagePageGenerator properties
ImagePageGenerator.List.AltText=代替テキスト

ImagePageGenerator.List.Bookmark=ブックマーク

ImagePageGenerator.List.Borders=罫線

ImagePageGenerator.List.General=全般

ImagePageGenerator.List.HyperLink=ハイパーリンク

ImagePageGenerator.List.Reference=参照

ImagePageGenerator.List.Section=改ページ

ImagePageGenerator.List.TOC=目次

ImagePageGenerator.List.Visibility=可視性

###########################################################
#ImportCSSStyleAction properties
ImportCSSStyleAction.text=CSS スタイルのインポート(&I)...

###########################################################
#ImportLibraryAction properties
ImportLibraryAction.Error.CannotBeEmpty=名前空間を空白にすることはできません。

ImportLibraryAction.Error.HasBeenUsed=この名前空間はすでに使用されています。

ImportLibraryAction.Error.HasDot=名前空間には '.' を使用できません。

ImportLibraryAction.Message.ImportSuccessfully=ライブラリ {0} をインポートしました。

ImportLibraryAction.Message=このライブラリをレポートデザイン内で使用しますか ?

ImportLibraryAction.Namespace=名前空間 :

ImportLibraryAction.Prefix=接頭辞 :

ImportLibraryAction.Title.ImportSuccessfully=インポート完了

ImportLibraryAction.Dialog.Titile=ライブラリの使用

ImportLibraryAction.Dialog.Message=レポートに含めるライブラリを選択します。

###########################################################
#ImportLibraryDialog properties
ImportLibraryDialog.Title=ライブラリの使用

###########################################################
#ImportValueDialog properties
ImportValueDialog.Label.SelectColumn=列を選択(&C) :

ImportValueDialog.Label.SelectDataSet=データセットを選択(&D) :

ImportValueDialog.Label.SelectValue=追加する値を選択・入力(&S)

ImportValueDialog.Title=値のインポート

ImportValueDialog.Button.Add.ToolTip=追加

ImportValueDialog.Button.AddAll.ToolTip=すべて追加

ImportValueDialog.Button.Remove.ToolTip=削除

ImportValueDialog.Button.RemoveAll.ToolTip=すべて削除
###########################################################
#IncludeDetailAction properties
IncludeDetailAction.actionMsg.includeDetail=詳細(&D)

###########################################################
#IncludeFooterAction properties
IncludeFooterAction.actionMsg.includeFooter=フッタ(&F)

###########################################################
#IncludeHeaderAction properties
IncludeHeaderAction.actionMsg.includeHeader=ヘッダ(&H)

###########################################################
#IncludeListDetailAction properties
IncludeListDetailAction.actionMsg.includeDetail=詳細を含める

###########################################################
#IncludeListFooterAction properties
IncludeListFooterAction.actionMsg.includeFooter=フッタを含める

###########################################################
#IncludeListHeaderAction properties
IncludeListHeaderAction.actionMsg.includeHeader=ヘッダを含める

###########################################################
#IncludeTableGroupFooterAction properties
IncludeTableGroupFooterAction.actionMsg.includeFooter=グループフッタ

###########################################################
#IncludeTableGroupHeaderAction properties
IncludeTableGroupHeaderAction.actionMsg.includeHeader=グループヘッダ

###########################################################
#InputParameterDailog properties
InputParameterDailog.dialog.title.required=必須パラメタ (値を指定してください)

InputParameterDailog.dialog.title=パラメタの入力

InputParameterDailog.parameter.noparameter=このレポートには、パラメタが関連付けられていません。

InputParameterDialog.ErrorMessage.InvalidFile=このレポートにはエラーがあります。

InputParameterDailog.parameter.null=NULL 値

InputParameterDialog.ErrorTitle.Error=エラー

InputParameterDialog.boolean.checked=はい

###########################################################
#InsertAction properties
InsertAction.text=要素の挿入(&N)

###########################################################
#InsertColumnAction properties
InsertColumnAction.actionMsg.insert=挿入

###########################################################
#InsertColumnLeftAction properties
InsertColumnLeftAction.actionMsg.columnToLeft=列 (左)(&L)

###########################################################
#InsertColumnRightAction properties
InsertColumnRightAction.actionMsg.columnToRight=列 (右)(&R)

###########################################################
#InsertExpressionMenuAction properties
InsertExpressionMenuAction.text.Expression=式...

###########################################################
#InsertGroupAction properties
InsertGroupAction.actionMsg.group=グループの挿入(&G)

InsertGroupAction.stackMsg.addGroup=グループの追加

###########################################################
#InsertGroupActionFactory properties
InsertGroupActionFactory.label.insertGroup=グループの挿入(&G)

###########################################################
#InsertInLayoutAction properties
InsertInLayoutAction.action.text=レイアウトに含める(&N)

###########################################################
#InsertListGroupAction properties
InsertListGroupAction.actionMsg.insertGroup=グループを挿入

InsertListGroupAction.stackMsg.insertListGroup=リストグループを含める

###########################################################
#InsertPasteColumnAction properties
InsertPasteColumnAction.text=コピーした列の挿入(&S)

###########################################################
#InsertPositionGroupAction properties
InsertPositionGroupAction.Label.Above=上(&A)

InsertPositionGroupAction.Label.Below=下(&B)

InsertPositionGroupAction.Label.Into=挿入先

###########################################################
#InsertRowAboveAction properties
InsertRowAboveAction.actionMsg.rowAbove=上(&A)

###########################################################
#InsertRowAction properties
InsertRowAction.actionMsg.insert=挿入(&I)

###########################################################
#InsertRowBelowAction properties
InsertRowBelowAction.actionMsg.rowBelow=下(&B)

###########################################################
#IReportGraphicConstants properties
IReportGraphicConstants.ImageType.All=すべてのイメージファイル (*.bmp,*.JPG,*.JPEG,*.GIF,*.TIF,*.PNG,*.ICO)

IReportGraphicConstants.ImageType.Bmp=ビットマップファイル (*.bmp)

IReportGraphicConstants.ImageType.Gif=GIF ファイル (*.GIF)

IReportGraphicConstants.ImageType.Icon=アイコンファイル (*.ICO)

IReportGraphicConstants.ImageType.Jpg=JPEG ファイル (*.JPG,*.JPEG)

IReportGraphicConstants.ImageType.Png=PNG ファイル (*.PNG)

IReportGraphicConstants.ImageType.Tif=TIF ファイル (*.TIF)

###########################################################
#JSDocumentProvider properties
JSDocumentProvider.Content.Default=コンテンツの新規作成

JSDocumentProvider.document.newName=コンテンツの新規作成

###########################################################
#JSEditor properties
JSEditor.cmb.NewEventFunction=イベント関数の新規作成...

JSEditor.Label.Script=スクリプト:

JSEditor.Button.Validate=スクリプトの検証

JSEditor.Button.Reset=スクリプトのリセット

JSEditor.Button.Help=説明を表示

JSEditor.Display.DefaultText=//JS スクリプトエディタ \n

JSEditor.Display.NoExpression=この要素にはメソッドがありません。

JSEditor.Input.trial=試用

JSEditor.Text.NoText=テキストがありません。

JSEditor.Text.NoDescription=このメソッドには説明がありません。

JSEditor.Validate.Title=検証

JSEditor.Validate.NoError=このスクリプトに構文エラーはありませんでした。

JSEditor.Folding.Group=折りたたみ

JSEditor.Folding.ExpandAll.label=すべて展開

JSEditor.Folding.CollapseAll.label=すべて折りたたむ

JSEditor.Folding.CollapseMethods.label=メソッドを折りたたむ

JSEditor.Folding.CollapseComments.label=コメントを折りたたむ

###########################################################
#label properties
label.defaultValue=(デフォルト値)

label.input=入力

label.inputOutput=入力/出力

label.no=いいえ

label.output=出力

label.redo=やり直し

label.undo=元に戻す

label.yes=はい

###########################################################
#LabelEditPart properties
LabelEditPart.Figure.Default=ラベル

###########################################################
#LabelPageGenerator properties
LabelPageGenerator.List.Bookmark=ブックマーク

LabelPageGenerator.List.Borders=罫線

LabelPageGenerator.List.General=全般

LabelPageGenerator.List.HyperLink=ハイパーリンク

LabelPageGenerator.List.I18n=ローカライゼーション

LabelPageGenerator.List.Margin=余白

LabelPageGenerator.List.Padding=パディング

LabelPageGenerator.List.Section=改ページ

LabelPageGenerator.List.TOC=目次

LabelPageGenerator.List.Visibility=可視性

###########################################################
#LayoutConfigurationBlock properties
LayoutConfigurationBlock.Default.Unit.Auto=自動

LayoutConfigurationBlock.Default.Unit.Label=デフォルトの単位(&F) :
LayoutConfigurationBlock.Default.Layout.Label=デフォルトのレイアウト:
LayoutConfigurationBlock.Default.Orientation.Label=デフォルトの向き:

###########################################################
#LayoutEditor properties
LayoutEditor.ErrorMessage.OpenError=エディタを開く際にエラーが発生しました。

LayoutEditor.Page.Layout=標準

LayoutEditor.Page.MasterPage=マスタページ

LayoutEditor.Page.Source=XML ソース

###########################################################
#LayoutPageAction properties
LayoutPageAction.actionText.layout=レイアウト

###########################################################
#LibrariesNodeProvider properties
LibrariesNodeProvider.DisplayLabel.Libraries=ライブラリ

###########################################################
#LibraryExplorerView properties
LibraryExplorerView.defaultText.notAvailable=リソースがありません。

###########################################################
#LibraryExplorerProvider properties
LibraryExplorerProvider.FolderNotExist=定義されている BIRT リソースフォルダは存在しません。

###########################################################
#LibraryPage properties
LibraryPage.Label.Namespace=ネームスペース(&N):

LibraryPage.Label.Path=パス(&P) :

###########################################################
#LibraryProvider properties
LibraryProvider.openDefinedLibrary.error.dialog.message=定義済みライブラリを開けません

LibraryProvider.openDefinedLibrary.error.dialog.title=エラー

###########################################################
#ListBandControlFigure properties
ListBandControlFigure.tooltip.GroupFooter=リストグループ - フッタ

ListBandControlFigure.tooltip.GroupHeader=リストグループ - ヘッダ

ListBandControlFigure.tooltip.ListDetail=リスト - 詳細

ListBandControlFigure.tooltip.ListFooter=リスト - フッタ

ListBandControlFigure.tooltip.ListHeader=リスト - ヘッダ

###########################################################
#ListBandProvider properties
ListBandProvider.action.text=グループを挿入(&N)

###########################################################
#ListBandProxy properties
ListBandProxy.list.detail=詳細

ListBandProxy.list.footer=フッタ

ListBandProxy.list.groupFooter=GF

ListBandProxy.list.groupHeader=GH

ListBandProxy.list.header=ヘッダ

###########################################################
#ListContainProcess properties
ListContainProcess.action.New=新規作成...

ListContainProcess.action.NewRow=行の新規作成

ListContainProcess.action.NewSubSection=サブセクションの新規作成

###########################################################
#ListEditPart properties
ListEditPart.GUIDEHANDLE_TEXT = リスト

###########################################################
#ListFigure properties
ListFigure.BORDER_TEXT=リスト

###########################################################
#ListHandleAdapt properties
ListHandleAdapt.transLabel.insertGroup=グループの挿入

###########################################################
#ListPageGenerator properties
ListPageGenerator.List.Bookmark=ブックマーク

ListPageGenerator.List.Borders=罫線

ListPageGenerator.List.Font=フォント

ListPageGenerator.List.General=全般

ListPageGenerator.List.Section=改ページ

ListPageGenerator.List.TOC=目次

ListPageGenerator.List.Visibility=可視性


###########################################################
#MapPage properties
MapPage.label.add=追加(&A)...

MapPage.label.condition=条件

MapPage.label.delete=削除(&R)

MapPage.label.displayValue=表示値 :

MapPage.label.mapList=マップの一覧 :

MapPage.transName.addMapRule=マップ規則の追加

MapPage.transName.deleteMapRule=マップ規則の削除

MapPage.transName.editMapRule=マップ規則の編集

MapPage.transName.moveDownRule=マップ規則を下に移動

MapPage.transName.moveUpMapRule=マップ規則を上に移動

MapPage.toolTipText.moveUp=マップの規則を上に移動

MapPage.toolTipText.moveDown=マップの規則を下に移動

MapPage.label.duplicate=重複(&C)

MapPage.toolTipText.duplicate=マップ規則の複製

MapPage.transName.Duplicate=マップ規則の複製

###########################################################
#MapPreferencePage properties
MapPreferencePage.displayname.Condition=条件

MapPreferencePage.displayname.DisplayValue=表示値

MapPreferencePage.displayname.Title=マップ

MapPreferencePage.text.Add=追加(&A)...

MapPreferencePage.text.Delete=削除(&R)

MapPreferencePage.text.Description=マップ規則 :

MapPreferencePage.text.Edit=編集(&E)...

MapPreferencePage.toolTipText.Up=マップの規則を上に移動

MapPreferencePage.toolTipText.Down=マップの規則を下に移動

MapPreferencePage.text.Duplicate=複製(&D)

MapPreferencePage.toolTipText.Duplicate=マップ規則の複製

###########################################################
#MapRuleBuilderDialog properties
MapRuleBuilder.button.add=追加(&A)

MapRuleBuilder.button.add.tooltip=値リストに値を追加します。

MapRuleBuilder.button.delete=削除(&R)

MapRuleBuilder.button.delete.tooltip=リストから選択した値を削除します。

MapRuleBuilder.button.deleteall=すべて削除(&O)

MapRuleBuilder.button.deleteall.tooltip=リストからすべての値を削除します。

MapRuleBuilder.button.edit=編集(&E)...

MapRuleBuilder.button.edit.tooltip=値リストで選択した値を編集します。

MapRuleBuilder.DialogTitle.New=マップ規則の新規作成

MapRuleBuilder.DialogTitle.Edit=マップ規則の編集

MapRuleBuilder.Button.ResourceKey=リソースキー(&K) :

MapRuleBuilder.Button.Reset=リセット(&R)

MapRuleBuilder.Button.Browse=参照(&B)...

MapRuleBuilder.Button.Browse.Tooltip=参照してキーを選択

MapRuleBuilder.label.value=値(&V) :

MapRuleBuilder.list.item1=値

MapRuleBuilder.MsgDlg.Msg=警告

MapRuleBuilder.MsgDlg.Title=値は空白にできません。

MapRuleBuilder.text.AND=および

MapRuleBuilderDialog.text.Between=次の値の間

MapRuleBuilderDialog.text.Condition=次の条件が True の場合(&I) :

MapRuleBuilderDialog.text.Display=次の値を表示(&T) :

MapRuleBuilderDialog.text.ET=次の値に等しい

MapRuleBuilderDialog.text.GE=次の値以上

MapRuleBuilderDialog.text.GT=次の値より大きい

MapRuleBuilderDialog.text.LE=次の値以下

MapRuleBuilderDialog.text.Like=次の値に類似

MapRuleBuilderDialog.text.LT=次の値より小さい

MapRuleBuilderDialog.text.NE=次の値に等しくない

MapRuleBuilderDialog.text.NotBetween=次の値の間以外

MapRuleBuilderDialog.text.NotNull=Not Null

MapRuleBuilderDialog.text.Null=Null

MapRuleBuilderDialog.text.Title=マップ

MapRuleBuilderDialog.MsgDlg.Title=警告

MapRuleBuilderDialog.MsgDlg.Msg=値は空にできません。

###########################################################
#SortkeyBuilder dialog properties
SortkeyBuilder.DialogTitle.New=ソートキーの新規作成

SortkeyBuilder.DialogTitle.Edit=ソートキーの編集

SortkeyBuilder.DialogMessage.New=ソートキーを作成するには、次の内容を指定してください。

SortkeyBuilder.DialogMessage.Edit=ソートキーを更新するには、次の内容を編集してください。

SortkeyBuilder.DialogTitle.Label.Prompt=ソートキーの入力 :

SortkeyBuilder.DialogTitle.Label.Key=キー(&K) :

SortkeyBuilder.DialogTitle.Label.Direction=方向(&D) :

SortkeyBuilder.Label.Locale=ロケール(&L) :

SortkeyBuilder.Label.Strength=優先順位(&S) :

SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=式ビルダを開く

SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=エラー

SortkeyBuilder.Locale.Auto=自動

SortkeyBuilder.Strength.ASCII=ASCII
SortkeyBuilder.Strength.PRIMARY=プライマリ

SortkeyBuilder.Strength.SECONDARY=セカンダリ

SortkeyBuilder.Strength.TERTIARY=サード

SortkeyBuilder.Strength.QUATERNARY=フォース

SortkeyBuilder.Strength.IDENTICAL=同じ

###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=フィルタ条件の新規作成

FilterConditionBuilder.DialogTitle.Edit=フィルタ条件の編集

FilterConditionBuilder.DialogMessage.New=フィルタ条件を作成するには、次の内容を指定してください。

FilterConditionBuilder.DialogMessage.Edit=フィルタ条件を更新するには、次の内容を編集してください。

FilterConditionBuilder.choice.ValueOfThisDataItem=このデータ項目の値

FilterConditionBuilder.tooltip.ExpBuilder=式ビルダを開く

FilterConditionBuilder.text.AND= ～

FilterConditionBuilder.text.Condition=フィルタ条件の入力 :

FilterConditionBuilder.Shell.Title=フィルタ条件

FilterConditionBuilder.list.item1=値

FilterConditionBuilder.button.edit=編集(&E)...

FilterConditionBuilder.button.delete=削除(&M)

FilterConditionBuilder.button.deleteall=すべて削除(&O)

FilterConditionBuilder.button.add=追加(&A)

FilterConditionBuilder.button.edit.tooltip=値リストで選択した値を編集します。

FilterConditionBuilder.button.delete.tooltip=リストから選択した値を削除します。

FilterConditionBuilder.button.deleteall.tooltip=リストからすべての値を削除します。

FilterConditionBuilder.button.add.tooltip=値リストに値を追加します。

FilterConditionBuilder.label.value=値(&V) :

FilterConditionBuilder.multipleValue.label=値(&V)

FilterConditionBuilder.multipleValue.button.add=追加(&A)

FilterConditionBuilder.multipleValue.button.remove=削除(&R)

FilterConditionBuilder.multipleValue.button.removeAll=すべて削除(&M)

FilterConditionBuilder.multipleValue.button.edit=編集(&D)...

FilterConditionBuilder.Button.UpdateAggregation=合計の再計算(&T)

###########################################################
#MarignPropertyDescriptor properties
MarignPropertyDescriptor.error.message=入力した値が大きすぎます。

MarignPropertyDescriptor.error.title=入力した値が大きすぎます

###########################################################
#MasterPageAction properties
MasterColumnsPropertyDescriptor.Button.Text.Custom=カスタム(&C)

MasterColumnsPropertyDescriptor.Button.Text.OneColumn=1(&O)

MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns=3(&R)

MasterColumnsPropertyDescriptor.Button.Text.TwoColumns=2(&T)

MasterColumnsPropertyDescriptor.Combo.Text.Column.Number=列数(&N) :

MasterPageAction.actionText.masterPage=マスタページ

###########################################################
#MasterPageGenerator properties
MasterPageGenerator.List.Columns=列

MasterPageGenerator.List.General=全般

MasterPageGenerator.List.Borders=罫線

MasterPageGenerator.List.Header&Footer=ヘッダ/フッタ

MasterPageGenerator.List.Margins=余白

###########################################################
#MasterPageGeneralPage properties
MasterPageGeneralPage.Trans.SetType=型の設定

###########################################################
#MasterPagePaletteFactory properties
MasterPagePaletteFactory.Autotext.TotalPageCount=合計ページ数

MasterPagePaletteFactory.Autotext.Author_Page_Date=作成者番号、ページ番号、日付

MasterPagePaletteFactory.Autotext.Confidential_Page=社外秘、ページ番号

MasterPagePaletteFactory.Autotext.CreatedBy=作成者

MasterPagePaletteFactory.Autotext.CreatedOn=作成日

MasterPagePaletteFactory.Autotext.Date=日付

MasterPagePaletteFactory.Autotext.Filename=ファイル名

MasterPagePaletteFactory.Autotext.Label=AutoText
MasterPagePaletteFactory.Autotext.LastPrinted=最終印刷日

MasterPagePaletteFactory.Autotext.Page=ページ

MasterPagePaletteFactory.Autotext.PageXofY=n/m ページ

MasterPagePaletteFactory.AutotextTooltip.TotalPageCount=合計ページ数のレポート項目

MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate=作成者番号、レポート項目の日付

MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage=社外秘のレポート項目

MasterPagePaletteFactory.AutotextTooltip.CreatedBy=作成者のレポート項目

MasterPagePaletteFactory.AutotextTooltip.CreatedOn=作成日のレポート項目

MasterPagePaletteFactory.AutotextTooltip.Date=日付のレポート項目

MasterPagePaletteFactory.AutoTextTooltip.Filename=ファイル名のレポート項目

MasterPagePaletteFactory.AutotextTooltip.LastPrinted=最後に印刷したレポート項目

MasterPagePaletteFactory.AutotextTooltip.Page=ページのレポート項目

MasterPagePaletteFactory.AutotextTooltip.PageXofY=レポート項目の n/m ページ

MasterPagePaletteFactory.elementLabel.authorPageDate=作成者番号、ページ番号、日付

MasterPagePaletteFactory.elementLabel.confidentialPage=社外秘、ページ

MasterPagePaletteFactory.elementLabel.createdBy=作成者

MasterPagePaletteFactory.elementLabel.createdOn=作成日

MasterPagePaletteFactory.elementLabel.date=日付

MasterPagePaletteFactory.elementLabel.fileName=ファイル名

MasterPagePaletteFactory.elementLabel.lastPrinted=最終印刷日

MasterPagePaletteFactory.elementLabel.page=ページ

MasterPagePaletteFactory.elementLabel.pageOf=n/m ページ

MasterPagePaletteFactory.paletteDrawerName.autoText=AutoText
MasterPagePaletteFactory.ToolTip.Grid=グリッドのレポート項目

MasterPagePaletteFactory.ToolTip.Text=テキストのレポート項目

MasterPagePaletteFactory.Autotext.Variable=変数

MasterPagePaletteFactory.AutotextTooltip.Variable=可変項目

###########################################################
#MasterPageProcess properties
MasterPageProcess.action.Edit=編集

MasterPageProcess.action.New=新規作成

MasterPageProcess.text.CannotDelete=デザインには、マスタページが 1 つしか含まれていないため、削除できません。

MasterPageProcess.text.Display=[ページ設定] ダイアログボックスを表示します。

MasterPageProcess.text.DisplayName=MasterPage

###########################################################
#MergeAction properties
MergeAction.actionMsg.merge=セルを結合する(&M)

###########################################################
#ModulePage properties
ModulePage.ErrorMessage.Duplicate=この名前はすでに使われています。

ModulePage.ErrorMessage.WrongExtensionName=ファイル名の拡張子は ".properties" にしてください。

ModulePage.ErrorMessage.InfoExtensionName=ファイル名の拡張子は ".properties" にしてください。

ModulePage.text.Add=追加(&A)...

ModulePage.text.ExistingFile=既存のリソースファイル :

ModulePage.title.AddNewFile=新しいリソースファイルの追加

ModulePage.Resourcefile.Dialog.Title=リソースファイルの参照

ModulePage.Resourcefile.Dialog.Message=リソースファイルを選択するか、フォルダを選択して新しいファイル名を入力し、ファイルを作成してください。

ModulePage.text.Path=パス(&P) :

###########################################################
#MultiPageEditorContributor properties
MultiPageEditorContributor.actionText.sampleAction=サンプルアクション

MultiPageEditorContributor.actionToolTip.sampleActionToolTip=サンプルアクションのツールヒント

MultiPageEditorContributor.actionToolTipText.sampleActionToolTip=サンプルアクションのツールヒント

MultiPageEditorContributor.dialogMsg.sampleActionExecuted=サンプルアクションを実行しました。

MultiPageEditorContributor.menuText.editorMenu=エディタメニュー(&M)

###########################################################
#NewLibraryWizard properties
NewLibraryWizard.choicePageTitle.report=ライブラリ

NewLibraryWizard.displayName.NewReportFileExtension=.rptlibrary
NewLibraryWizard.displayName.NewReportFileNamePrefix=新規ライブラリ

NewLibraryWizard.error.NoProject=ワークスペースには開いているプロジェクトがありません。[ファイル]→[新規作成]→[プロジェクト] を選択して新しいプロジェクトを作成するか、既存のプロジェクトを開いてください。

NewLibraryWizard.pageDescription.createNewReport=新しいライブラリを作成します。

NewLibraryWizard.taskName.openFileForEdit=ファイルを編集モードで開く...

NewLibraryWizard.text.ConfirmSwitchPerspective=パースペクティブの切り替え確認

NewLibraryWizard.text.Container=コンテナ "

NewLibraryWizard.text.CreateReport=新しいライブラリを作成します。

NewLibraryWizard.text.Creating=作成

NewLibraryWizard.text.DoesNotExist=" は存在しません。

NewLibraryWizard.text.OpenFileForEditing=ファイルを編集モードで開く...

NewLibraryWizard.text.SwitchPerspective=この種類のファイルは {0} パースペクティブに関連付けられています。今すぐこのパースペクティブに切り替えますか ?

NewLibraryWizard.title.Choose=ライブラリの作成方法の選択

NewLibraryWizard.title.Error=エラー

NewLibraryWizard.title.New=新規ライブラリ

NewLibraryWizard.title.Report=ライブラリ

NewLibraryWizard.title.ReportWizard=ライブラリウィザード

NewLibraryWizard.title.Template=TemplateChoicePage
NewLibraryWizard.title.WizardPage=WizardPage
NewLibraryWizard.widowTitle.new=新規

NewLibraryWizard.wizardPageTitle.report=ライブラリ

###########################################################
#NewReportProjectWizard properties

NewReportProjectWizard.bin=bin
NewReportProjectWizard.caseVariantExistsError=ファイルシステムでは、大文字と小文字が区別されないため、既存のプロジェクトと同じ名前 "{0}" は使用できません。

NewReportProjectWizard.description=新しいレポートプロジェクトを作成します。

NewReportProjectWizard.errorMessage=新しいレポートプロジェクトを作成中にエラーが発生しました。

NewReportProjectWizard.errorOpeningWindow=ウィンドウを開けませんでした。

NewReportProjectWizard.internalError=内部エラー : {0}

NewReportProjectWizard.javaProject=Java プロジェクトの作成

NewReportProjectWizard.perspSwitchMessage=この種類のプロジェクトは {0} パースペクティブに関連付けられています。今すぐこのパースペクティブに切り替えますか ?

NewReportProjectWizard.perspSwitchTitle=パースペクティブの切り替え確認

NewReportProjectWizard.projectSetting=プロジェクトの設定

NewReportProjectWizard.src=src
NewReportProjectWizard.title=レポートプロジェクト

NewReportProjectWizard.windowTitle=新規レポートプロジェクト

###########################################################
#NewReportWizard properties
NewReportWizard.choicePageTitle.report=レポート

NewReportWizard.displayName.NewReportFileExtension=.rptdesign
NewReportWizard.displayName.NewReportFileNamePrefix=new_report
NewReportWizard.error.NoProject=ワークスペースには開いているプロジェクトがありません。[ファイル]→[新規作成]→[プロジェクト] を選択して新しいプロジェクトを作成するか、既存のプロジェクトを開いてください。

NewReportWizard.pageDescription.createNewReport=新しいレポートを作成します。

NewReportWizard.pageDescription.selectReportTemplate=作成するレポートの種類を選択してください。

NewReportWizard.taskName.openFileForEdit=ファイルを編集モードで開く...

NewReportWizard.text.ConfirmSwitchPerspective=パースペクティブの切り替え確認

NewReportWizard.text.Container=コンテナ "

NewReportWizard.text.CreateReport=新しいレポートを作成します。

NewReportWizard.text.Creating=作成

NewReportWizard.text.DoesNotExist=" は存在しません。

NewReportWizard.text.OpenFileForEditing=ファイルを編集モードで開く...

NewReportWizard.text.SelectTemplate=作成するレポートの種類を選択してください。

NewReportWizard.text.SwitchPerspective=この種類のファイルは {0} パースペクティブに関連付けられています。今すぐこのパースペクティブに切り替えますか ?

NewReportWizard.title.Choose=レポートの作成方法を選択してください。

NewReportWizard.title.Error=エラー

NewReportWizard.title.New=新規レポート

NewReportWizard.title.Report=レポート

NewReportWizard.title.ReportWizard=レポートウィザード

NewReportWizard.title.Template=TemplateChoicePage
NewReportWizard.title.WizardPage=WizardPage
NewReportWizard.widowTitle.new=新規作成

NewReportWizard.wizard.msgDirErr=ターゲットディレクトリは存在していませんが、新たに作成することもできません。


###########################################################
#NewResourceFileDialog properties
NewResourceFileDialog.label.NewFile=新規ファイル名(&N) :

NewResourceFileDialog.ErrorMessage=ファイル名の拡張子は .properties にしてください。

NewResourceFileDialog.ErrorMessageInvalid=ファイル名が無効です。

NewResourceFileDialog.FileExists={0} ファイルあります。 上書きしますか ?

###########################################################
#ProjectFileDialog properties
ProjectFileDialog.ErrorMessage=ディレクトリではなく、ファイルを指定してください。

ProjectFileDialog.Title=プロジェクトファイルの選択

ProjectFileDialog.Message=現在のプロジェクトのファイルを選択してください。

ProjectFileDialog.Text.Menu=メニュー

###########################################################
#ProjectSelectionDialog properties
ProjectSelectionDialog.Message=設定するプロジェクトの選択 :

ProjectSelectionDialog.Text.SpecialSettings=プロジェクト固有の設定が指定されたプロジェクトのみを表示する

ProjectSelectionDialog.Title=プロジェクト固有の設定

###########################################################
#ResourceFilterDialog properties
ResourceFilterDialog.Title=レポート要素のフィルタ

ResourceFilterDialog.Lable.Description=フィルタの説明 :

ResourceFilterDialog.Lable.Viewer=ビューから除外する要素を選択してください :

###########################################################
#NewSectionDialog properties
NewSectionDialog.displayname.Container=コンテナ

NewSectionDialog.displayname.Data=データ

NewSectionDialog.displayname.Grid=グリッド

NewSectionDialog.displayname.Image=イメージ

NewSectionDialog.displayname.Label=ラベル

NewSectionDialog.displayname.Line=線

NewSectionDialog.displayname.List=リスト

NewSectionDialog.displayname.Rectangle=四角形

NewSectionDialog.displayname.Table=テーブル

NewSectionDialog.displayname.Text=テキスト

NewSectionDialog.text.Prefix=使用可能なセクション :

NewSectionDialog.title.insertElement=要素の挿入

NewSectionDialog.Title=要素の挿入

###########################################################
#NewTemplateWizard properties
NewTemplateWizard.displayName.NewReportFileNamePrefix=新規テンプレート

NewTemplateWizard.pageDescription.createNewTemplate=新しいレポートテンプレートを作成します。

NewTemplateWizard.text.CreateReport=新しいテンプレートを作成します。

NewTemplateWizard.text.Creating=作成中

NewTemplateWizard.text.OpenFileForEditing=テンプレートファイルを編集モードで開く...

NewTemplateWizard.title.New=新規テンプレート

NewTemplateWizard.title.Report=レポート

NewTemplateWizard.title.Template=テンプレート

NewTemplateWizard.title.WizardPage=WizardPage

###########################################################
#NoneAction properties
NoneAction.text=なし

###########################################################
#OutputColumnDefnPage properties
OutputColumnDefnPage.description=フェッチスクリプトによって作成される出力列の定義を入力 :

OutputColumnDefnPage.OutputColumns.Tooltip=データ行内の列の表示と定義

OutputColumnPage.error.createNewColumn=新しい列の追加中にエラーが発生しました。

OutputColumnPage.error.invalidSelection=有効なテーブル項目が選択されていません。

###########################################################
#ColumnDefPage properties
ColumnDefPage.description=スクリプトデータセットの出力列定義ページ

ColumnDefPage.title=出力列

###########################################################
#ResultSetColumnPage properties
ResultSetColumnPage.button.add=追加(&A)...

ResultSetColumnPage.button.edit=編集(&E)...

ResultSetColumnPage.button.delete=削除(&D)

ResultSetColumnPage.button.up=上へ(&U)

ResultSetColumnPage.button.down=下へ(&W)

ResultSetColumnPage.inputDialog.newColumn.title=新規スクリプトデータセット列

ResultSetColumnPage.inputDialog.editColumn.title=スクリプトデータセット列の編集

ResultSetColumnPage.inputDialog.label.columnName=列名(&C) :

ResultSetColumnPage.inputDialog.label.dataType=データ型(&T) :

ResultSetColumnPage.inputDialog.label.alias=列エイリアス(&A) :

ResultSetColumnPage.inputDialog.label.displayName=表示名(&D) :

ResultSetColumnPage.inputDialog.label.displayNameKey=表示名キー(&K) :

ResultSetColumnPage.inputDialog.warning.emptyColumnName=入力するには列名が必要です。

ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias=列名とそのエイリアスを同一にすることはできません。

ResultSetColumnPage.inputDialog.error.duplicatedColumnName=列名 "{0}" は既に使用されています。

ResultSetColumnPage.inputDialog.error.duplicatedAlias=列エイリアス "{0}" は既に使用されています。

###########################################################
#OutputColumnsPage properties
OutputColumnPage.OutputColumns.Tooltip=データ行内の列の表示と定義

OutputColumnPage.toolTipText.Edit=出力列の編集

OutputColumnPage.title.OutputColumnInputDialog=出力列

OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn=出力列の新規作成

OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn=出力列の編集

OutputColumnPage.OutputColumns.DuplicatedName=この名前はすでに存在します。

OutputColumnsPage.description=出力列の定義 :

OutputColumnsPage.DataTypeConvert.Note=注

OutputColumnsPage.DataTypeConvert.Message=このような変換は精度の低下や実行時のエラーを引き起こす場合があります。適用しますか ?

###########################################################
#PageBreakPreferencePage properties
PageBreakPreferencePage.displayname.Title=改ページ

###########################################################
#PageSetAction properties
PageSetAction.text.code=スクリプト(&S)

PageSetAction.Text.EditCode=スクリプトの編集(&S)...

PageSetAction.text.layout=レイアウト(&L)

PageSetAction.text.master=マスター(&M)

PageSetAction.text.normal=標準(&N)

PageSetAction.text.preview=プレビュー(&P)

###########################################################
#PageSetupOutlineProcess properties
PageSetupOutlineProcess.action.Edit=ページ設定の編集

PageSetupOutlineProcess.action.New=マスタページの新規作成

PageSetupOutlineProcess.text.Display=[ページ設定] ダイアログボックスを表示します。

PageSetupOutlineProcess.text.Switch=ページマスタエディタに切り替えます。

###########################################################
#ParameterDialog properties
ParameterDialog.Boolean.False=False

ParameterDialog.Boolean.True=True

ParameterDialog.Button.ChangeFormat=変更(&G)...

ParameterDialog.Button.CreateDataSet=新規作成(&W)...

ParameterDialog.Button.ImportValue=値のインポート(&U)...

ParameterDialog.Button.RemoveDefault=デフォルトの削除(&A)

ParameterDialog.Button.SetDefault=デフォルトに設定(&A)

ParameterDialog.CheckBox.IsRequired=必須にする(&Q)

ParameterDialog.CheckBox.DoNotEchoInput=入力値を繰り返さない(&O)

ParameterDialog.CheckBox.Hidden=非表示(&H)

ParameterDialog.CheckBox.Distinct=値の重複を許可する(&L)

ParameterDialog.Choice.NoDefault=デフォルト値がありません

ParameterDialog.Choice.NullValue=NULL 値

ParameterDialog.Choice.BlankValue=空白値

ParameterDialog.Choice.None=なし

ParameterDialog.Choice.DisplayText=表示テキスト

ParameterDialog.Choice.ValueColumn=値のカラム

ParameterDialog.Choice.ASCENDING=昇順

ParameterDialog.Choice.DESCENDING=降順

ParameterDialog.Choice.SelectValue=<値の選択...>

ParameterDialog.Column.Default=デフォルト

ParameterDialog.Column.DisplayText=表示テキスト

ParameterDialog.Column.DisplayTextKey=表示テキストキー

ParameterDialog.Column.Value=値

ParameterDialog.DefaultValue.Add=追加(&A)

ParameterDialog.DisplayLabel.Combo=コンボボックス

ParameterDialog.DisplayLabel.List=リストボックス

ParameterDialog.ErrorMessage.CanootBeBlank=値は空白にできません。

ParameterDialog.ErrorMessage.CanootBeNull=値は null にできません。

ParameterDialog.ErrorMessage.DuplicatedLabel=この表示テキストはすでに存在します。

ParameterDialog.ErrorMessage.DuplicatedLabelKey=この表示テキストキーはすでに存在します。

ParameterDialog.ErrorMessage.DuplicatedName=この名前はすでに使われています。

ParameterDialog.ErrorMessage.DuplicatedValue=この値はすでに存在します。

ParameterDialog.ErrorMessage.EmptyName=名前が指定されていません。

ParameterDialog.ErrorMessage.InvalidListLimit=制限値は、0 ～ {0} の範囲で指定してください。

ParameterDialog.ErrorMessage.MismatchDataType=この値はデータ型に一致しません。

ParameterDialog.ErrorMessage.NoAvailableColumn=使用できるカラムがありません。

ParameterDialog.ErrorMessage.ValueColumnEmpty=値列を指定する必要があります。

ParameterDialog.ErrorMessage.NoDefaultValue=デフォルト値を設定してください。

ParameterDialog.ErrorTitle.InvalidListLimit=一覧の制限が無効です。

ParameterDialog.ExpressionEditor.Title=式の編集

ParameterDialog.Flag.Default=デフォルト

ParameterDialog.Group.MoreOption=次の形式で表示

ParameterDialog.Group.Properties=プロパティ

ParameterDialog.Label.DataType=データ型(&D) :

ParameterDialog.SelectionDialog.Edit=選択項目の編集

ParameterDialog.Label.DefaultValue=デフォルト値(&F) :

ParameterDialog.Label.DisplayPrompt=プロンプトの表示 :

ParameterDialog.Label.DisplayType=表示の種類(&T) :

ParameterDialog.Label.Format=書式設定 :

ParameterDialog.Label.HelpText=ヘルプテキスト(&X) :

ParameterDialog.Label.ListOfValue=値リストの選択

ParameterDialog.Label.SortGroup=ソート

ParameterDialog.Label.Name=名前(&N):

ParameterDialog.Label.None=<なし>

ParameterDialog.Label.Null=Null

ParameterDialog.Label.Preview=書式付きプレビュー

ParameterDialog.Label.PromptText=プロンプトテキスト(&P) :

ParameterDialog.Label.SelectDataSet=データセット(&A) :

ParameterDialog.Label.SortKey=方向(&B) :

ParameterDialog.Label.SortDirection=ソートの方向(&C) :

ParameterDialog.SelectionDialog.New=選択項目の新規作成

ParameterDialog.SelectionDialog.Label.DisplayTextKey=表示テキストキー(&K) :

ParameterDialog.SelectionDialog.Label.DisplayText=表示テキスト(&T) :

ParameterDialog.SelectionDialog.Label.Value=値(&V) :

ParameterDialog.SelectionDialog.Button.Resource=...
ParameterDialog.SelectionDialog.Button.Resource.Tooltip=参照してキーを選択

ParameterDialog.SelectionDialog.Button.Remove.Tooltip=キーを削除

ParameterDialog.SelectionDialog.Label.Note=注 : 表示テキストキーを設定するには、レポートを選択し、[プロパティエディタ] の [リソース] タブでリソースファイルの名前を指定してください。

ParameterDialog.Label.SelectDisplayText=表示テキストの選択(&E) :

ParameterDialog.Label.SelectValueColumn=値列を選択(&U) :

ParameterDialog.Label.Value=値 :

ParameterDialog.Label.values=個の値

ParameterDialog.Label.Sample=サンプル

ParameterDialog.Label.Listlimit=一覧の制限(&I) :

ParameterDialog.Radio.Dynamic=動的(&Y)

ParameterDialog.Radio.Static=静的(&S)

ParameterDialog.toolTipText.OpenExprButton=式ビルダを開く

ParameterDialog.datetime.prompt=日時は、{0} の形式で入力してください

ParameterDialog.date.prompt=日付は、{0} の形式で入力してください

ParameterDialog.time.prompt=時間は、{0} の形式で入力してください

ParameterDialog.Check.AllowMulti=複数の値を許可する(&V)

ParameterDialog.checkBox.InputValue=入力値

ParameterDialog.checkBox.BlankValue=空白値

ParameterDialog.checkBox.NullValue=NULL 値

ParameterDialog.emptyColumnValueExpression.title=値の列式が無効です。

ParameterDialog.emptyColumnValueExpression.message=値の列式は空白にできません。

ParameterDialog.Value.Empty=空の値

ParameterDialog.Value.Null=Null 値

ParameterDialog.message=レポートパラメタのプロパティを定義します。

###########################################################
#SelectParameterDefaultValueDialog properties
SelectParameterDefaultValueDialog.Title=値の選択

###########################################################
#ParameterDropListener properties
ParameterDropListener.Command.Copy=要素のコピー

ParameterDropListener.Command.Move=要素の移動

###########################################################
#ParameterGroupDialog properties
ParameterGroupDialog.Page.Title=全般

ParameterGroupDialog.pageTitle.general=全般

###########################################################
#ParameterGroupGeneralPage properties
ParameterGroupGeneralPage.Label.DisplayName=表示名(&D) :

ParameterGroupGeneralPage.Label.Name=名前(&N):

###########################################################
#ParameterGroupNodeProvider properties
ParameterGroupNodeProvider.Action.GroupNew=パラメタグループの新規作成

ParameterGroupNodeProvider.Action.ParameterEdit=編集(&E)

ParameterGroupNodeProvider.Action.ParameterNew=パラメタの新規作成(&N)

ParameterGroupNodeProvider.Dialogue.ParameterEdit=パラメタグループの編集

ParameterGroupNodeProvider.Dialogue.ParameterNew=パラメタの新規作成

ParameterGroupNodeProvider.Node.Group=パラメタグループ

###########################################################
#ParameterGroupProcess properties
ParameterGroupProcess.action.Delete=パラメタグループの削除

ParameterGroupProcess.action.Edit=パラメタグループの編集

ParameterGroupProcess.action.New=パラメタの新規作成

ParameterGroupProcess.text.DisplayName=ParameterGroup
ParameterGroupProcess.text.Show=このデータソースのエディタページを表示します。

###########################################################
#ParameterNodeProvider properties
ParameterNodeProvider.dial.title.editCascading=カスケードパラメタグループの編集

ParameterNodeProvider.dial.title.editScalar=パラメタの編集

ParameterNodeProvider.dial.title.newCascading=カスケードパラメタグループの新規作成

ParameterNodeProvider.menu.text.edit=編集(&E)

ParameterNodeProvider.menu.text.new=パラメタの新規作成

###########################################################
#ParameterProcess properties
ParameterProcess.action.Delete=パラメタの削除

ParameterProcess.action.Edit=パラメタの編集

ParameterProcess.text.DisplayName=パラメタ

ParameterProcess.text.Show=このデータソースのエディタページを表示します。

###########################################################
#parameters properties
parameters.datatypes.boolean=論理

parameters.datatypes.dateTime=DateTime
parameters.datatypes.decimal=10 進数値

parameters.datatypes.float=浮動少数値

parameters.datatypes.string=文字列

parameters.menuItem.reset=リセット

###########################################################
#ParametersNodeProvider properties
ParametersNodeProvider.dialogue.title.group=パラメタグループの新規作成

ParametersNodeProvider.dialogue.title.parameter=パラメタの新規作成

ParametersNodeProvider.menu.text.cascadingParameter=カスケードパラメタグループの新規作成(&D)

ParametersNodeProvider.menu.text.group=パラメタグループの新規作成(&M)

ParametersNodeProvider.menu.text.parameter=パラメタの新規作成(&A)

###########################################################
#ParametersOutlineProcess properties
ParametersOutlineProcess.action.Edit=編集

ParametersOutlineProcess.action.New=パラメタの新規作成

ParametersOutlineProcess.action.NewGroup=グループの新規作成

ParametersOutlineProcess.text.Open=パラメタエディタを開きます。

ParametersOutlineProcess.text.Show=このデータセットのエディタページを表示します。

###########################################################
#PasteAction properties
PasteAction.dlg.title=情報

PasteAction.ok=OK

PasteAction.text=貼り付け(&P)

###########################################################
#PlaceHolderEditPart properties
PlaceHolderEditPart.Trans.TransferToElement=レポート項目への転送

###########################################################
#PreviewAction properties
PreviewAction.Text=プレビュー

###########################################################
#PreviewEditor properties
PreviewEditor.parameter.tooltip=レポートパラメタの表示

PreviewEditor.parameter.hint=レポートの更新

PreviewEditor.parameter.note=注 :

PreviewEditor.parameter.info=データ行は現在 {0} に制限されています。

PreviewEditor.parameter.change=プレビューの環境設定を変更するにはここをクリック

PreviewEditor.ConfirmRefresh.Title=更新の確認

PreviewEditor.ConfirmRefresh.Message=この操作はプレビュー結果に影響する可能性があります。プレビューページを更新しますか ?

PreviewEditor.browser.notfound=内部ブラウザがありません。

###########################################################
#PreviewPageAction properties
PreviewPageAction.actionText.preview=プレビュー

###########################################################
#PropertiesAction properties
PropertiesAction.Text=プロパティ

###########################################################
#PropertyHandleTableViewer properties
PropertyHandleTableViewer.Button.New=新規(&N)...

PropertyHandleTableViewer.Button.Edit=編集(&E)...

PropertyHandleTableViewer.Button.Remove=削除(&R)

PropertyHandleTableViewer.Button.Up=上へ(&U)

PropertyHandleTableViewer.Button.Down=下へ(&D)

PropertyHandleTableViewer.Menu.Remove=削除

PropertyHandleTableViewer.Menu.Up=上へ

PropertyHandleTableViewer.Menu.Down=下へ

PropertyHandleTableViewer.Menu.RemoveAll=すべて削除

###########################################################
#PropertyHandleInputDialog properties
PropertyHandleInputDialog.Action.New=新規

PropertyHandleInputDialog.Action.Edit=編集

PropertyHandleInputDialog.messages.error.blankProperty={0} は空白にできません。

###########################################################
#PropertyAndPreferencePage properties
PropertyAndPreferencePage.Text.Configure.Special.Settings=プロジェクト固有の設定を指定...

PropertyAndPreferencePage.Text.Configure.Workspace.Settings=ワークスペースの設定を指定...

PropertyAndPreferencePage.Text.Enable.SpecialSettings=プロジェクト固有の設定を有効にする(&O)

###########################################################
#PublishTemplateAction properties
PublishTemplateAction.wizard.page.browse.cheatsheet.title=ガイドファイルの選択

PublishTemplateAction.wizard.page.browse.title=イメージファイルの選択

PublishTemplateAction.wizard.page.btn.browse=参照(&W)...

PublishTemplateAction.wizard.page.cheatsheetError=ガイドファイル ''{0}'' は存在しません。

PublishTemplateAction.wizard.page.desc=テンプレートフォルダにテンプレートを登録します。

PublishTemplateAction.wizard.page.imageError=イメージファイル ''{0}'' は存在しません。

PublishTemplateAction.wizard.page.label.cheatsheet=ガイドファイル :

PublishTemplateAction.wizard.page.label.description=説明(&D) :

PublishTemplateAction.wizard.page.label.displayName=表示名(&M) :

PublishTemplateAction.wizard.page.label.image=テンプレートのイメージ(&I) :

PublishTemplateAction.wizard.page.title=テンプレートの公開

PublishTemplateAction.wizard.title=テンプレートの公開

PublishTemplateAction.wizard.errorTitle=エラー

PublishTemplateAction.wizard.message=テンプレートがすでに存在するディレクトリには、テンプレートを公開できません。公開するには、別のディレクトリを選択してください。

PublishTemplateAction.wizard.msgDirErr=ターゲットディレクトリは存在していませんが、新たに作成することもできません。

PublishTemplateAction.wizard.notvalidfolder=設定に無効なテンプレートフォルダが定義されています。

PublishTemplateAction.wizard.page.nameInfo=表示名を指定してください。

PublishTemplateAction.wizard.message.SourceFileNotExist=ソースファイルが存在しません。

PublishTemplateAction.wizard.message.ThumbnailImageNotExist=サムネイルのイメージは存在しません。

PublishTemplateAction.wizard.message.PreviewImageNotExist=プレビュー用のイメージファイルは存在しません。

PublishTemplateAction.wizard.message.PreviewImageNotValid=プレビュー用のイメージファイルは、有効なイメージではありません。

PublishTemplateAction.SaveBeforeGenerating.dialog.title=レポートの保存

PublishTemplateAction.SaveBeforeGenerating.dialog.message={0} は変更されています。 公開する前に変更を保存しますか ?

PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes=はい(&Y)

PublishTemplateAction.SaveBeforeGenerating.dialog.button.no=いいえ(&N)
###########################################################
#PublishLibraryAction properties
PublishLibraryAction.wizard.errorTitle=エラー

PublishLibraryAction.wizard.message=ライブラリがすでに存在するディレクトリには、ライブラリを公開できません。公開するには、別のディレクトリを選択してください。

PublishLibraryAction.wizard.msgDirErr=ターゲットディレクトリは存在していませんが、新たに作成することもできません。

PublishLibraryAction.wizard.message.SourceFileNotExist=ソースファイルが存在しません。

PublishLibraryAction.wizard.notvalidfolder=定義されているライブラリフォルダは無効です。

###########################################################
#RedoAction properties
RedoAction.Tooltip={0} をやり直し

###########################################################
#ExportReportWizard
ExportToLibraryAction.wizard.windowTitle=エクスポート

ExportToLibraryAction.wizard.page.title=ファイルをライブラリにエクスポート

ExportToLibraryAction.wizard.page.desc=レポートデザインをライブラリにエクスポート

ExportToLibraryAction.wizard.page.label.filename=ファイル名(&N) :

ExportToLibraryAction.wizard.page.label.folder=フォルダ(&F) :

ExportToLibraryAction.wizard.page.button.browser=参照(&B)...

ExportToLibraryAction.wizard.page.dirdialog.message=ファイルのコンテンツディレクトリを選択してください。

ExportToLibraryAction.wizard.page.nameempty=ファイル名を指定してください。

ExportToLibraryAction.wizard.page.folderempty=フォルダを指定してください。

ExportToLibraryAction.wizard.page.foldererror=指定されたフォルダが存在しません。

###########################################################
#PublishLibraryToResourceFolderAction
PublishLibraryToResourceFolderAction.Action.Text=共有リソースフォルダへのライブラリのコピー(&B)...

###########################################################
#PublishTemplateViewAction
DefaultNodeProvider.template.suffix=.rpttemplate
PublishTemplateViewAction.action.displaytext=テンプレートをレポートの新規作成ウィザードに登録(&Z)...

###########################################################
#RefreshModuleHandleAction
RefreshModuleHandleAction.Action.Text=ライブラリの更新(&L)

RefreshModuleHandleAction.MessageBox.Title=ライブラリの更新

RefreshModuleHandleAction.MessageBox.Text=現在のデザインは変更が保存されていません。 これらの変更を最初に保存しますか ?

RefreshModuleHandleAction.MessageBox.SaveButton=上書き保存(&S)

RefreshModuleHandleAction.MessageBox.DiscardButton=破棄(&D)

RefreshModuleHandleAction.MessageBox.CancelButton=キャンセル
###########################################################
#ReloadCssStylesAction
ReloadCssStylesAction.Action.Text=CSS ファイルの再ロード(&R)

###########################################################
#ReferencePage properties
ReferencePage.Button.Edit=編集(&D)...

ReferencePage.Label.Source=ソース(&C):

###########################################################
#RefreshAction properties
RefreshAction.text=再表示(&F)

###########################################################
#RefreshLibExplorerAction properties
RefreshLibExplorerAction.Text=再表示(&F)

###########################################################
#RemoveLibraryAction properties
RemoveLibraryAction.Text=ライブラリの削除(&B)

RemoveLibraryAction.config.Title=ライブラリの削除の確認

RemoveLibraryAction.config.Message=このライブラリを削除しますか ?

###########################################################
#UseCssInReportDesignAction properties
UseCssInReportDesignAction.Text=レポートデザインで CSS を使用...

###########################################################
#UseCssInReportDesignAction properties
UseCssInThemeAction.Text=ライブラリで CSS を使用...

###########################################################
#RenameAction properties
RenameAction.text=名前の変更(&M)

###########################################################
#RenameDialog properties
RenameDialog.action.Cancel=キャンセル

RenameDialog.action.Ok=\\ \\ OK

RenameDialog.text.NewName=新しい名前 :

RenameDialog.text.Rename=名前の変更

RenameDialog.text.ToolTip=名前を空白にすることはできません。


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.SetNameAction.text=名前を設定してください。

ExportElementToLibraryAction.DialogTitle.setNameFailed=名前を設定できません。

ExportElementToLibraryAction.TransLabel.Setname=名前を設定してください。

ExportElementToLibraryAction.DialogTitle=名前の設定

ExportElementToLibraryAction.DialogMessage=選択した要素をエクスポートする前に名前を指定できますが、デフォルトの名前を使用することもできます。


###########################################################
#RenameInlineTool properties
RenameInlineTool.DialogTitle.RenameFailed=名前を変更できません。

RenameInlineTool.TransLabel.Rename=名前の変更

###########################################################
#RenameInputDialog properties
RenameInputDialog.DialogTitle=名前の変更

RenameInputDialog.DialogMessage=新しい名前を入力してください。

###########################################################
#report properties
report.designer.ui.preferences.buttonadd=追加(&D)...

report.designer.ui.preferences.buttonremove=削除(&R)

report.designer.ui.preferences.buttonremoveall=すべて削除(&A)

report.designer.ui.preferences.dialogtitle=ライブラリを開く

report.designer.ui.preferences.errormessage=ライブラリはすでに存在します。

report.designer.ui.preferences.errortitle=エラー

report.designer.ui.preferences.extensions=*.rptlibrary
report.designer.ui.preferences.labeltitle=ライブラリの追加と削除

report.designer.ui.preferences.library.export.overwrite.always=上書きする(&W)

report.designer.ui.preferences.library.export.overwrite.never=上書きしない(&N)

report.designer.ui.preferences.library.export.overwrite.prompt=確認メッセージを表示する(&P)

report.designer.ui.preferences.library.export.title=デザイン要素をライブラリにエクスポートするときに、既存の要素を上書きしますか ?

report.designer.ui.preferences.template.openDialogTitle=フォルダの選択

report.designer.ui.preferences.template.select=選択(&S)...

report.designer.ui.preferences.template.templatedir=テンプレート

report.designer.ui.preferences.template.templatefolder=テンプレートフォルダ(&T)

report.designer.ui.preferences.template.title=テンプレートフォルダを選択してください。

org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment=レポートデザインの作成時にコメントを生成する(&G)

org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment=Copyright (c) 2008 <<ここに会社名を入力>>
#bidi_hcg start
report.designer.ui.preferences.bidiproperties.defaultdirection=レポートのデフォルトの向き(&O) :

report.designer.ui.preferences.bidiproperties.ltrdirection=左から右

report.designer.ui.preferences.bidiproperties.rtldirection=右から左
#bidi_hcg end
###########################################################
#Resource Preference page properties
ResourecePreferencePage.title=ライブラリエクスプローラ、イメージ、プロパティファイルなどに使用\nするデフォルトのリソースフォルダを入力または選択してください。

ResourecePreferencePage.folder=リソースフォルダ(&R)

ResourecePreferencePage.select=選択(&S)...

ResourecePreferencePage.openDialogTitle=フォルダの参照

ResourecePreferencePage.openDialogMessage=デフォルトのリソースフォルダを選択してください :

ResourecePreferencePage.defaultResourceFolder.dispaly=<現在のプロジェクトフォルダ>

###########################################################
#ReportAdvancedLauncherTab properties
ReportAdvancedLauncherTab.DefaultsButtonText=デフォルトに戻す(&R)

ReportAdvancedLauncherTab.DeselectButtonText=すべて選択解除(&D)

ReportAdvancedLauncherTab.Name=レポートプロジェクト

ReportAdvancedLauncherTab.SelectAllButtonText=すべて選択(&S)

ReportAdvancedLauncherTab.UseListRadioText=起動するプロジェクトを一覧から選択する

ReportAdvancedLauncherTab.VisibleLabelText=選択可能なプロジェクト(&A)

ReportAdvancedLauncherTab.WorkspaceReportProjects=ワークスースのレポートプロジェクト

###########################################################
#ReportClasspathLaucnTab properties
ReportClasspathLaucnTab.DefaultButtonText=デフォルトに戻す(&R)

ReportClasspathLaucnTab.DeselectButtonText=すべて選択解除(&D)

ReportClasspathLaucnTab.Name=Java プロジェクト

ReportClasspathLaucnTab.SelectAllButtonText=すべて選択(&S)

ReportClasspathLaucnTab.UseListRadioText=起動するプロジェクトを一覧から選択する (選択したプロジェクトのクラス出力フォルダがクラスパスに追加されます)。

ReportClasspathLaucnTab.VisibleLabelText=選択可能なプロジェクト(&A)

ReportClasspathLaucnTab.WorkspaceJavaProjects=ワークスペースの Java プロジェクト

###########################################################
#ReportCreationTool properties
ReportCreationTool.ModelTrans.CreateElement=要素の挿入

###########################################################
#ReportEditor properties
ReportEditor.Button.Close=閉じる

ReportEditor.Button.Save=保存

ReportEditor.Dlg.Confirm=このファイルは、ファイルシステムから削除されました。変更を保存しますか ? または、変更を保存せずにエディタを閉じますか ?

ReportEditor.Dlg.Save=保存...

ReportEditor.error.activated.outofsync.message=このファイルは、ファイルシステムで変更されました。変更を読み込みますか ?

ReportEditor.error.activated.outofsync.title=ファイルの変更

ReportEditor.Error.Open=開けません。

ReportEditor.Error.OpenEditor=ファイルを開けません。

ReportEditor.Page.Code=スクリプト

ReportEditor.Page.Layout=レイアウト

ReportEditor.Page.MasterPage=MasterPage
ReportEditor.Page.Preview=プレビュー

ReportEditor.Page.PreviewSaveMessage=レポートファイルは変更されています。プレビューする前に保存しますか ?

ReportEditor.Page.PreviewSaveTitle=保存

ReportEditor.Page.PreviewSaveToggle=常にプレビュー前に保存する

###########################################################
#ReportLaunchConfigurationDelegate properties
ReportLaunchConfigurationDelegate.badFeatureSetup=機能の設定が正しくありません

ReportLaunchConfigurationDelegate.noStartup=スタートアップがありません

###########################################################
#ReportPage properties
ReportPage.text.Add=追加(&A)...

ReportPage.text.Browse=参照(&B)...

ReportPage.title.Location=場所 :

ReportPage.title.setPrewImg=イメージファイルの選択

ReportPage.text.Resource.Add=参照(&B)...

ReportPage.text.Thumbnail=サムネイル(&H)...

###########################################################
#ReportPageGenerator properties
ReportPageGenerator.List.Comments=コメント

ReportPageGenerator.List.Description=説明

ReportPageGenerator.List.EventHandler=イベントハンドラ

ReportPageGenerator.List.General=全般

ReportPageGenerator.List.Measurement=メジャー

ReportPageGenerator.List.NamedExpressions=名前付き式

ReportPageGenerator.List.UserProperties=ユーザのプロパティ

ReportPageGenerator.List.Resources=リソース

ReportPageGenerator.List.Resources.JarFile=JAR ファイル :

ReportPageGenerator.List.AdvancedProperty=詳細設定

#OdaDataSourceGenerator properties
DataSourcePageGenerator.List.General=全般

#OdaDataSetGenerator properties
DataSetPageGenerator.List.General=全般

DataSetPageGenerator.DataSourceName=データソース(&D)

#ScalarParameterPageGenerator properties
ScalarParameterPageGenerator.List.General=全般

ScalarParameterPageGenerator.List.I18n=ローカライゼーション

#TemplateReportItemPageGenerator properties
TemplateReportItemPageGenerator.List.General=全般

TemplateReportItemPage.description.Label.Instructions=手順(&I) :

TemplateReportItemPageGenerator.List.I18n=ローカライゼーション

TemplateReportItemPageGenerator.List.TextKey=テキストキー :

###########################################################
#ReportPropertySheetPage properties
ReportPropertySheetPage.Column.Title.Property=プロパティ

ReportPropertySheetPage.Column.Title.Value=値

ReportPropertySheetPage.Root.Default.Title=情報

ReportPropertySheetPage.Tooltip.Alphabetic=文字順にソート

ReportPropertySheetPage.Tooltip.Group=グループを基準にソート

ReportPropertySheetPage.Tooltip.Local=ローカルプロパティを表示

ReportPropertySheetPage.Value.Inherited=継承済み

###########################################################
#ReportTemplateTransferDropTargetListener properties
ReportTemplateTransferDropTargetListener.ModelTrans.CreateElement=要素の作成

ReportTemplateTransferDropTargetListener.transLabel.createElement=要素の挿入

###########################################################
#ResourceEditDialog properties
ResourceEditDialog.message.AddNote=[クイック追加] では、現在のリソースファイルにキーが保存されます。

ResourceEditDialog.message.ResourceFile=リソースファイル :

ResourceEditDialog.text.Add=追加(&A)

ResourceEditDialog.text.AddWarningMsg=キーは空白にできません。

ResourceEditDialog.text.AddWarningTitle=警告

ResourceEditDialog.text.Delete=削除(&D)

ResourceEditDialog.text.Key.TableColumn=キー

ResourceEditDialog.text.Key=キー(&K)

ResourceEditDialog.text.QuickAdd=クイック追加

ResourceEditDialog.text.Value.TableColumn=値

ResourceEditDialog.text.Value=値(&V)

###########################################################
#ResourceKeyDescriptor properties
ResourceKeyDescriptor.text.Browse=参照(&B)...

ResourceKeyDescriptor.text.Browse.Alt=参照(&W)...

ResourceKeyDescriptor.text.Reset=リセット(&R)

ResourceKeyDescriptor.text.Reset.Alt=リセット(&E)

ResourceKeyDescriptor.title.SelectKey=キーの選択

ResourceKeyDescriptor.button.reset.tooltip=キーのリセット

ResourceKeyDescriptor.button.browse.tooltip=参照してキーを選択

ResourceKey.invalid.containSpace=表示名キーが無効です。埋め込みスペースは使用できません。
###########################################################
#RevertToReportItemAction properties
RevertToReportItemAction.text=レポート項目に戻す(&R)

###########################################################
#RevertToTemplateAction properties
RevertToTemplateAction.text=テンプレート項目に戻す(&V)

###########################################################
#RowHandle properties
RowHandle.tooltip.GridRow=グリッド - 行

RowHandle.tooltip.GroupFooter=テーブルグループ - フッタ

RowHandle.tooltip.GroupHeader=テーブルグループ - ヘッダ

RowHandle.tooltip.TableDetail=テーブル - 詳細

RowHandle.tooltip.TableFooter=テーブル - フッタ

RowHandle.tooltip.TableHeader=テーブル - ヘッダ

###########################################################
#RowHandleAdapter properties
RowHandleAdapter.table.detail=詳細行

RowHandleAdapter.table.footer=フッタ行

RowHandleAdapter.table.groupFooter=グループフッタ行

RowHandleAdapter.table.groupHeader=グループヘッダ行

RowHandleAdapter.table.header=ヘッダ行

###########################################################
#RowPageGenerator properties
RowPageGenerator.List.Bookmark=ブックマーク

RowPageGenerator.List.Borders=罫線

RowPageGenerator.List.Section=改ページ

RowPageGenerator.List.CellPadding=セルのパディング

RowPageGenerator.List.Font=フォント

RowPageGenerator.List.General=全般

RowPageGenerator.List.Visibility=表示/非表示

###########################################################
#RowProvider properties
RowProvider.action.text.above=上に行を挿入(&A)

RowProvider.action.text.below=下に行を挿入(&B)

###########################################################
#SaveAsDialog properties
SaveAsDialog.message=ファイルを別の場所に保存します。

SaveAsDialog.overwriteQuestion=ファイル ''{0}'' はすでに存在します。 既存のファイルを置き換えますか ?

SaveAsDialog.Question=質問

SaveAsDialog.text=名前を付けて保存

SaveAsDialog.title=名前を付けて保存

###########################################################
#CreateDirectory properties
DirectoryDialog.createQuestion=ディレクトリ "{0}" は存在しません。新しく作成しますか ?

DirectoryDialog.Question=質問

###########################################################
#SchematicContextMenuProvider properties
SchematicContextMenuProvider.ActionText.editData=値/式の編集(&V)...

SchematicContextMenuProvider.ActionText.editLabel=編集(&E)

SchematicContextMenuProvider.label.ResetSize=イメージサイズのリセット(&Z)

SchematicContextMenuProvider.label.ResetToImageDPI=イメージの解像度を最適に調整(&I)

SchematicContextMenuProvider.label.ResetToReportDPI=レポートの解像度を最適に調整(&R)

SchematicContextMenuProvider.label.ResetToScreenDPI=画面の解像度を最適に調整(&S)

SchematicContextMenuProvider.label.RestToOrginalPixels=元のピクセルに戻す(&P)

SchematicContextMenuProvider.Menu.EditGroup=グループの編集(&G)

SchematicContextMenuProvider.Menu.Insert=挿入(&I)

SchematicContextMenuProvider.Menu.insertElement=挿入(&N)

SchematicContextMenuProvider.Menu.insertRow=行(&R)

SchematicContextMenuProvider.Menu.List=リスト

SchematicContextMenuProvider.Menu.Show=表示

SchematicContextMenuProvider.Menu.Style=スタイル(&Y)

SchematicContextMenuProvider.Menu.Style.Apply=スタイルの適用(&P)

SchematicContextMenuProvider.Menu.Theme=テーマ(&M)

SchematicContextMenuProvider.Menu.Theme.Apply=テーマの適用(&P)

SchematicContextMenuProvider.Menu.Layout=レイアウトの設定(&A)
#SchematicContextMenuProvider.menutitle.applystylerule=Apply Style
#SchematicContextMenuProvider.menutitle.applythemerule=&Apply Theme
#SchematicContextMenuProvider.menutitle.editgroup=Edit Group
#SchematicContextMenuProvider.menutitle.insert=Insert
#SchematicContextMenuProvider.menutitle.list=List
#SchematicContextMenuProvider.menutitle.show=Show
#SchematicContextMenuProvider.menutitle.style=Style
SchematicContextMenuProvider.Menu.EditStyle=スタイルの編集(&T)

SchematicContextMenuProvider.Menu.DeleteStyle=スタイルの削除(&S)

SchematicContextMenuProvider.Menu.DeleteGroup=グループの削除(&L)

SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter=グループヘッダ/フッタの挿入(&H)

SchematicContextMenuProvider.Menu.NewStyle=スタイルの新規作成(&Y)

###########################################################
#ApplyLayoutPreferenceAction properties
ApplyLayoutPreferenceAction.autoLayout=自動レイアウト(&A)

ApplyLayoutPreferenceAction.fixedLayout=固定レイアウト(&F)

###########################################################
#SectionProcess properties
SectionProcess.action.Clear=クリア

SectionProcess.action.Delete=削除

SectionProcess.action.DeleteRow=行の削除

SectionProcess.action.New=新規作成

SectionProcess.action.NewCell=新規セル

SectionProcess.action.NewGroup=グループの新規作成

SectionProcess.action.NewRow=行の新規作成

SectionProcess.action.NewSection=新規セクション

SectionProcess.action.NewSubSection=サブセクションの新規作成

SectionProcess.displayname.GridProcess=グリッド

SectionProcess.displayname.GroupProcess=グループ

SectionProcess.displayname.ListProcess=リスト

SectionProcess.displayname.TableProcess=テーブル

SectionProcess.displayname.TableRowProcess=行

SectionProcess.type.Container=(コンテナ)

SectionProcess.type.Data=(データ)

SectionProcess.type.Image=(イメージ)

SectionProcess.type.Label=(ラベル)

SectionProcess.type.List=(リスト)

SectionProcess.type.Prefix=列

SectionProcess.type.Table=(テーブル)

SectionProcess.type.Text=(テキスト)

###########################################################
#SelectAllAction properties
SelectAllAction.text=すべて選択(&A)

###########################################################
#SelectCssStyleWizard properties
SelectCssStyleWizard.wizard.title=CSS スタイルのインポート

SelectCssStyleWizard.wizardPage.description.report=ここで選択した CSS ファイルから、レポートデザインにスタイルをインポートします。

SelectCssStyleWizard.wizardPage.description.library=ここで選択した CSS ファイルから、ライブラリにスタイルをインポートします。

SelectCssStyleWizard.wizardPage.name=CSS スタイルの選択

SelectCssStyleWizard.wizardPage.title=CSS ファイルからスタイルをインポート

###########################################################
#UseCssInReportDialog properties
UseCssInReportDialog.Wizard.Title=CSS の使用

UseCssInReportDialog.Wizard.Filename=ファイル名(&F) :

UseCssInReportDialog.Label.Styles={0} の使用可能なスタイル(&A)

UseCssInReportDialog.Label.notifications=通知(&N)

UseCssInReportDialog.TitleArea.Title=レポートデザインで CSS ファイルを使用

UseCssInReportDialog.TitleArea.Message=共有ライブラリフォルダから CSS ファイルを選択し、レポートデザインのスタイルとして使用します。

UseCssInReportDialog.Label.No.File=CSS ファイルが選択されていません。

UseCssInReportDialog.Dialog.Browse=参照

UseCssInReportDialog.Dialog.Browse.Title=レポートに含める CSS ファイルを選択します。

UseCssInReportDialog.Dialog.Browse.Library.Title=ライブラリに含める CSS ファイルを選択します。

UseCssInReportDialog.Error.Already.Include={0} はすでに追加されています。

UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text=表示に CSS ファイルをインクルードする (HTML 形式のみ)(&I)

UseCssInReportDialog.Dialog.Label.viewTimeLb=表示に使用する CSS ファイルの場所 :

UseCssInReportDialog.Dialog.Label.example=例: http://mydomain/test.css、myfolder/test/css

UseCssInReportDialog.Dialog.Label.Theme.Text=テーマ(&T) :

UseCssInReportDialog.Dialog.Text.uri=&URI:
UseCssInThemeDialog.TitleArea.Title=レポートライブラリで CSS ファイルを使用

UseCssInThemeDialog.TitleArea.Message=CSS ファイルのスタイルを配置するテーマを選択してください。

CssStyleSheetNodeProvider.Label.FromServer=サーバから

CssStyleSheetNodeProvider.Tooltip.URI=(表示時に適用)


###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=リストを検索する際にエラーが発生しました。

SelectValueDialog.messages.info.selectVauleUnavailable=値リストがありません。

SelectValueDialog.messages.info.invalidSelectVauleExpression=値の選択機能では、単純な列式しか使用できません。複雑な式は拒否されます。

SelectValueDialog.messages.warning.BlobColumn.unsupported=BLOB 型の列やバインドへの参照がある式の値リストは取得できません。

SelectValueDialog.messages.error.selectVauleUnavailable=式の値を取得できません。

SelectValueDialog.messages.error.selectValueNotSupported=この種類の値の選択はサポートされていません。

SelectValueDialog.noExpressionSet=式が設定されていません。

SelectValueDialog.retrieving=リストの取得中...

SelectValueDialog.selectValue=値の選択

SelectValueDialog.SelectValue.NullValue=<Null 値>

###########################################################
#SetConstraintCommand properties
SetConstraintCommand.Error.SetConstraintError=モデルに制約を設定中にエラーが発生しました。

SetConstraintCommand.transLabel.setConstraint=制約の設定

SetConstraintCommand.commandLabel.ResizeCommand=サイズ変更
###########################################################
#ProjectFilterAction properties
ProjectFilterAction.text=フィルタ...

###########################################################
#ResourceFileFolderSelectionAction properties
ResourceFileFolderSelectionAction.text=フィルタ...

###########################################################
#ResourceFileFolderSelectionDialog properties
ResourceFileFolderSelectionDialog.Text.Menu=メニュー

ResourceFileFolderSelectionDialog.button.importFile=ファイルのインポート...

ResourceFileFolderSelectionDialog.title.error=エラー

ResourceFileFolderSelectionDialog.title.overwrite=上書き

ResourceFileFolderSelectionDialog.overwrite.msg=この名前のファイルはすでに存在します。上書きしますか ?

ResourceFileFolderSelectionDialog.import.msg=ファイルのインポート中...

###########################################################
#ShowPropertyAction properties
ShowPropertyAction.text=プロパティ(&R)

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=ソート :

###########################################################
#Spinner properties
Spinner.DialogTitle.InvalidNumber=数値が無効です

###########################################################
#SplitAction properties
SplitAction.actionMsg.splitCells=セルの分割(&S)

###########################################################
#StyleBuilder properties
StringVariableSelectionDialog.Dialog.Label=変数の説明(&V) :

StringVariableSelectionDialog.Dialog.Prompt=変数を選択してください (? は任意の 1 文字、* は任意の文字列を示します)(&C) :

StringVariableSelectionDialog.Dialog.Title=変数の選択

StyleBuilder.action.Apply=適用(&A)

StyleBuilder.Edit.Info=スタイルプロパティを変更します。このスタイルを使用しているすべてのレポート要素が更新され、新しい形式が反映されます。

StyleBuilder.New.Info=新しいスタイルを作成するには、左側のプロパティのカテゴリを選択し、プロパティの値を指定します。

StyleBuilder.Title=スタイル

SytleBuilder.DialogTitle.New=スタイルの新規作成

SytleBuilder.DialogTitle.Edit=スタイルの編集

###########################################################
#StyleComboProvider properties
StyleComboProvider.LineStyle.None=スタイルなし

###########################################################
#StyleNodeProvider properties
StyleNodeProvider.action.Edit=スタイルの編集(&E)...

StyleNodeProvider.action.New=スタイルの新規作成(&N)...

###########################################################
#StyleProcess properties
StyleProcess.action.Delete=スタイルの削除

StyleProcess.action.Edit=スタイルの編集(&E)...

StyleProcess.action.New=スタイルの新規作成(&N)...

StyleProcess.text.CannotDelete=デフォルトのスタイルを削除できません。

StyleProcess.text.Show=データセットのエディタを表示します。

###########################################################
#StylesNodeProvider properties
StylesNodeProvider.action.New=スタイルの新規作成(&N)...

###########################################################
#StylesOutlineProcess properties
StylesOutlineProcess.action.New=スタイルの新規作成(&N)...

###########################################################
#TableBandProvider properties
TableBandProvider.action.text.group=グループを挿入(&N)

TableBandProvider.action.text.row=行の挿入(&S)

###########################################################
#TableEditPart properties
TableEditPart.GUIDEHANDLE_TEXT =テーブル

TableEditPart.Label.Merge=セルを結合する

TableEditPart.Label.ResizeColumn=列幅の変更

TableEditPart.Label.ResizeRow=行幅の変更

###########################################################
#TableFigure properties
TableFigure.BORDER_TEXT=テーブル

TableArea.Button.New=新規作成(&W)...

TableArea.Button.Edit=編集(&E)...

TableArea.Button.Remove=削除(&R)

TableArea.Button.Down=下へ(&O)

TableArea.Button.RemoveAll=すべて削除(&M)

###########################################################
#TableHandleAdapter properties
TableHandleAdapter.name.detail=詳細

TableHandleAdapter.name.footer=フッタ

TableHandleAdapter.name.header=ヘッダ

TableHandleAdapter.transLabel.deleteColumn=列の削除

TableHandleAdapter.transLabel.deleteColumns=列の削除

TableHandleAdapter.transLabel.deleteRow=行の削除

TableHandleAdapter.transLabel.deleteRows=行の削除

TableHandleAdapter.transLabel.include=含める

TableHandleAdapter.transLabel.insertColumn=列の挿入

TableHandleAdapter.transLabel.insertGroup=グループの挿入

TableHandleAdapter.transLabel.insertRow=行の挿入

TableHandleAdapter.transLabel.notInclude=除外する

TableHandleAdapter.transLabel.splitCells=セルの分割

TableHandleAdapter.transLable.deleteGroup=テーブルグループの削除

###########################################################
#TableOptionDialog properties
TableOptionDialog.message.RememberGrid=このサイズを新しいグリッドに使用(&U)

TableOptionDialog.message.RememberTable=このサイズを新しいテーブルに使用(&U)

TableOptionDialog.text.Column=列数(&C) :

TableOptionDialog.text.GridRow=行数(&R) :

TableOptionDialog.text.GridSize=グリッドのサイズ

TableOptionDialog.text.TableDetail=表示する詳細の数(&D) :

TableOptionDialog.text.TableSize=テーブルのサイズ

TableOptionDialog.title.InsertGrid=グリッドの挿入

TableOptionDialog.title.InsertTable=テーブルの挿入

TableOptionDialog.text.DataSet=データセット(&S) :

TableOptionDialog.text.AutoSummary=自動サマリを有効にする(&M)

TableOptionPage.Title=テーブルの挿入

###########################################################
#TablePageGenerator properties
TablePageGenerator.List.Bookmark=ブックマーク

TablePageGenerator.List.Borders=罫線

TablePageGenerator.List.CellPadding=セルのパディング

TablePageGenerator.List.Font=フォント

TablePageGenerator.List.General=全般

TablePageGenerator.List.Marign=余白

TablePageGenerator.List.Section=改ページ

TablePageGenerator.List.TOC=目次

TablePageGenerator.List.Visibility=可視性

###########################################################
#TemplateReportItemPropertiesDialog properties
TemplateReportItemPropertiesDialog.errorMessage=プロンプトテキストは空白にできません。

TemplateReportItemPropertiesDialog.errorTitle=エラー

TemplateReportItemPropertiesDialog.group=テンプレートのレポート項目のプロパティ

TemplateReportItemPropertiesDialog.objectType=要素型 :

TemplateReportItemPropertiesDialog.nameText=名前(&N):

TemplateReportItemPropertiesDialog.promptText=手順(&I) :

TemplateReportItemPropertiesDialog.title=テンプレートのレポート項目の作成

###########################################################
#performCreatePlaceHolder action
performCreatePlaceHolder.errorMessage.title=エラー

performCreatePlaceHolder.errorMessage.content=指定した名前はすでに存在します。別の名前を指定するか、空白にしてください。

###########################################################
#TogglePropertyDescriptor properties
TogglePropertyDescriptor.toolTipText.Bold=太字

TogglePropertyDescriptor.toolTipText.Italic=斜体

TogglePropertyDescriptor.toolTipText.Underline=下線

TogglePropertyDescriptor.toolTipText.Text_Line_Through=テキストの取り消し線

###########################################################
#TextAlignPropertyDescriptor properties
TextAlignPropertyDescriptor.0=左揃え

TextAlignPropertyDescriptor.1=中央揃え

TextAlignPropertyDescriptor.2=右揃え

TextAlignPropertyDescriptor.3=両端揃え

###########################################################
#TextAreaContextMenu properties
TextAreaContextMenu.Copy.description=コピー

TextAreaContextMenu.Copy.image=

TextAreaContextMenu.Copy.label=コピー(&C)

TextAreaContextMenu.Copy.tooltip=コピー

TextAreaContextMenu.Cut.description=切り取り

TextAreaContextMenu.Cut.image=

TextAreaContextMenu.Cut.label=切り取り(&T)

TextAreaContextMenu.Cut.tooltip=切り取り

TextAreaContextMenu.Paste.description=貼り付け

TextAreaContextMenu.Paste.image=

TextAreaContextMenu.Paste.label=貼り付け(&P)

TextAreaContextMenu.Paste.tooltip=貼り付け

TextAreaContextMenu.Redo.description=やり直し

TextAreaContextMenu.Redo.image=

TextAreaContextMenu.Redo.label=やり直し(&R)

TextAreaContextMenu.Redo.tooltip=やり直し

TextAreaContextMenu.SelectAll.description=すべて選択

TextAreaContextMenu.SelectAll.image=

TextAreaContextMenu.SelectAll.label=すべて選択(&A)

TextAreaContextMenu.SelectAll.tooltip=すべて選択

TextAreaContextMenu.Undo.description=元に戻す

TextAreaContextMenu.Undo.image=

TextAreaContextMenu.Undo.label=元に戻す(&U)

TextAreaContextMenu.Undo.tooltip=元に戻す

###########################################################
#TextDataEditPart properties
TextDataEditPart.Figure.Dafault=ダイナミックテキスト

###########################################################
#TextEditDialog properties
TextEditDialog.action.text.direction=右から左へ読む(&R)

TextEditDialog.action.text.editDynamicText=ダイナミックテキストの編集(&X)

TextEditDialog.action.text.formatDateTime=日時の書式設定(&E)

TextEditDialog.action.text.formatNumber=数値の書式設定(&N)

TextEditDialog.action.text.formatString=書式文字列(&G)

TextEditDialog.action.text.formatHTML=HTML の書式設定(&H)

TextEditDialog.choice.HTML=HTML / ダイナミックテキスト形式

TextEditDialog.choice.plainText=標準のテキスト形式

TextEditDialog.choice.Auto=自動

TextEditDialog.comboText.formattingAndLayout=書式とレイアウト

TextEditDialog.format.dynamicText=ダイナミックテキスト

TextEditDialog.format.formatting=書式とレイアウト

TextEditDialog.format.lists=リスト

TextEditDialog.format.pageComposition=ページ構成

TextEditDialog.format.pageElements=ページ要素

TextEditDialog.formatChoice.content=コンテンツ

TextEditDialog.formatChoice.dynamicText=ダイナミックテキスト

TextEditDialog.formatChoice.formatting=書式設定

TextEditDialog.formatChoice.layout=レイアウト

TextEditDialog.formatChoice.lists=リスト

TextEditDialog.toolTip.tag.a=アンカー

TextEditDialog.toolTip.tag.b=太字テキスト

TextEditDialog.toolTip.tag.birtImage=BIRT 固有のイメージ

TextEditDialog.toolTip.tag.br=改行

TextEditDialog.toolTip.tag.center=テキストの中央揃え

TextEditDialog.toolTip.tag.code=プログラミングコード

TextEditDialog.toolTip.tag.dd=定義の説明

TextEditDialog.toolTip.tag.del=削除されたテキスト

TextEditDialog.toolTip.tag.dl=定義リスト

TextEditDialog.toolTip.tag.dt=定義の条件

TextEditDialog.toolTip.tag.em=強調

TextEditDialog.toolTip.tag.font=フォントの部分変更

TextEditDialog.toolTip.tag.genericStyleContainer=汎用言語 /スタイルコンテナ

TextEditDialog.toolTip.tag.h1=見出し 1

TextEditDialog.toolTip.tag.h2=見出し 2

TextEditDialog.toolTip.tag.h3=見出し 3

TextEditDialog.toolTip.tag.h4=見出し 4

TextEditDialog.toolTip.tag.h5=見出し 5

TextEditDialog.toolTip.tag.h6=見出し 6

TextEditDialog.toolTip.tag.i=斜体テキスト

TextEditDialog.toolTip.tag.img=標準 HTML イメージの参照

TextEditDialog.toolTip.tag.ins=挿入されたテキスト

TextEditDialog.toolTip.tag.li=リスト項目

TextEditDialog.toolTip.tag.p=段落

TextEditDialog.toolTip.tag.pre=書式設定済みテキスト

TextEditDialog.toolTip.tag.strong=より強い強調

TextEditDialog.toolTip.tag.sub=下付き

TextEditDialog.toolTip.tag.sup=上付き

TextEditDialog.toolTip.tag.tt=等幅テキスト

TextEditDialog.toolTip.tag.u=下線付きテキスト

TextEditDialog.toolTip.tag.ul=順不同リスト

TextEditDialog.toolTip.valueOf=値

TextEditDialog.toolTipText.copy=コピー

TextEditDialog.toolTipText.cut=切り取り

TextEditDialog.toolTipText.delete=削除

TextEditDialog.toolTipText.paste=貼り付け

TextEditDialog.toolTipText.redo=やり直し

TextEditDialog.toolTipText.undo=元に戻す

TextEditDialog.toolTipText.fx=式ビルダを開く

TextEditDialog.action.item.formatHTML=HTML の書式設定
TextEditDialog.action.item.formatDateTime=日時の書式設定

TextEditDialog.action.item.formatNumber=数値の書式設定

TextEditDialog.action.item.formatString=文字列の書式設定

TextEditDialog.title.DlgNew=テキスト項目の新規作成

TextEditDialog.title.DlgEdit=テキスト項目の編集

###########################################################
#TextEditPart properties
TextEditPart.Figure.Dafault=テキスト

TextEditPart.trans.editText=テキスト項目の編集

###########################################################
#TextExtendsTools properties
TextExtendsTools.Message.DefaultAuthor=Birt Eclipse Report Designer のバージョン

###########################################################
#TextPageGenerator properties
TextPageGenerator.List.Bookmark=ブックマーク

TextPageGenerator.List.Borders=罫線

TextPageGenerator.List.General=全般

TextPageGenerator.List.I18n=ローカライゼーション

TextPageGenerator.List.Margin=余白

TextPageGenerator.List.Padding=パディング

TextPageGenerator.List.Section=改ページ

TextPageGenerator.List.TOC=目次

TextPageGenerator.List.Visibility=可視性

###########################################################
#TextPropertyDescriptor properties
TextPropertyDescriptor.Message.Version=Eclipse BIRT Designer バージョン {0} ビルド <{1}>

###########################################################
#AutoTextPageGenerator properties
AutoTextPageGenerator.List.Bookmark=ブックマーク

AutoTextPageGenerator.List.Borders=罫線

AutoTextPageGenerator.List.General=全般

AutoTextPageGenerator.List.Margin=余白

AutoTextPageGenerator.List.Padding=パディング

AutoTextPageGenerator.List.Section=改ページ

AutoTextPageGenerator.List.TOC=目次

AutoTextPageGenerator.List.Visibility=表示/非表示

###########################################################
#ThemesNodeProvider properties
ThemesNodeProvider.action.New=テーマの新規作成(&N)...

ThemesNodeProvider.Failed=新規テーマのエラー

NewThemeDialog.DialogTitle=新規テーマ

NewThemeDialog.DialogMessage=名前を入力してください。

###########################################################
#TimeOptionDialog properties
TimeOptionDialog.Label.Format=書式

TimeOptionDialog.Label.TimeZone=TimeZone

###########################################################
#ToggleMarginVisibilityAction properties
ToggleMarginVisibilityAction.text.Label=余白の表示(&M)

ToggleMarginVisibilityAction.text.Tooltip=余白の切り替え

###########################################################
#TwinExpressionDialog properties
TwinExpressionDialog.Dialog.Title=値の選択

TwinExpressionDialog.lable.value1=値 1:

TwinExpressionDialog.lable.value2=値 2:

###########################################################
#UndoAction properties
UndoAction.Tooltip={0} を元に戻す

###########################################################
#UnitPropertyDescriptor properties
UnitPropertyDescriptor.error.message=小さい値を入力してください。

UnitPropertyDescriptor.error.title=値が大きすぎます

###########################################################
#UseLibraryAction properties
UseLibraryAction.Text=ライブラリの使用(&U)

UseLibraryAction.Error.Title=既存のファイルの上書き

UseLibraryAction.Error.Message=BIRT リソースフォルダには、ファイル ''{0}'' がすでに存在します。既存のファイルを上書きしますか ?

###########################################################
#UseCSSAction properties
UseCSSAction.Text=CSS の使用(&U)

###########################################################
#UserPropertyBuilder properties
UserPropertyBuilder.0=ユーザープロパティの新規作成

UserPropertyBuilder.ErrorMessage.DuplicatedName=この名前はすでに使用されています。

UserPropertyBuilder.ErrorMessage.NoName=名前を指定してください。

UserPropertiesHandleProvier.Column.Name=名前

UserPropertiesHandleProvier.Column.Type=種類

UserPropertyBuilder.Label.DefaultValue=デフォルト値(&V) :

UserPropertyBuilder.Label.ExpressionName=式の名前(&N) :

UserPropertyBuilder.Label.PropertyName=プロパティ名(&N) :

UserPropertyBuilder.Label.PropertyType=プロパティの種類(&T) :

UserPropertyBuilder.Title.NamedExpression=名前付き式の新規作成

###########################################################
#VisibilityPage properties
VisibilityPage.Check.HideElement=要素の非表示(&H)

VisibilityPage.Check.HTML=H&TML
VisibilityPage.Check.PDF=&PDF
VisibilityPage.Label.DetailAll=詳細 (すべての出力ファイル)

VisibilityPage.Label.DetailSpecific=詳細 (特定の出力ファイル)

VisibilityPage.Label.Expression=式 :

VisibilityPage.menu.SaveHides=非表示の項目を保存

VisibilityPage.Radio.AllOutputs=すべての出力ファイル(&A)

VisibilityPage.Radio.SpecificOutputs=特定の出力ファイル(&C)...

###########################################################
#WidgetUtil properties
WidgetUtil.MessageBox.Error=エラー

###########################################################
#wizard properties
wizard.transaction.createDataSource=データソースの名前と型を定義してください。

###########################################################
#WizardChoicePage properties
WizardChoicePage.radio.createBlank=テンプレートを使わずに空のレポートを作成する。

WizardChoicePage.radio.createFromCustom=別のレポートをコピーしてレポートを作成する。

WizardChoicePage.radio.createFromTemplate=テンプレートを使ってレポートを作成する。

###########################################################
#WizardCustomTemplatePage properties
WizardCustomTemplatePage.button.Browse=参照

WizardCustomTemplatePage.label.FileName=既存のレポートファイル名の入力または選択。

WizardCustomTemplatePage.label.FromLocal=ローカルファイルシステムの参照。

WizardCustomTemplatePage.label.FromProjectDescription=既存のレポートファイルの選択。

WizardCustomTemplatePage.label.FromProjectTitle=レポートファイルの選択

###########################################################
#WizardNewReportCreationPage properties
WizardNewReportCreationPage.Errors.nameExists=この名前はすでに使われています。

WizardNewReportCreationPage.Errors.nameEmpty=名前を空白にすることはできません。

WizardNewReportCreationPage.label.browse=参照...

WizardNewReportCreationPage.label.directory=ディレクトリ :

WizardNewReportCreationPage.label.file.location=ファイルの場所

WizardNewReportCreationPage.label.file.name=ファイル名 :

WizardNewReportCreationPage.label.select.directory=ディレクトリを選択してください。

WizardNewReportCreationPage.label.useDefault=デフォルト使用

WizardNewReportCreationPage.msg.duplicate.fileName=この名前はすでに使われています。

WizardNewReportCreationPage.msg.empty.file.locationDirectory=ファイルの場所のディレクトリは空白にできません。

WizardNewReportCreationPage.msg.empty.file.name=ファイル名は空白にできません。

###########################################################
#WizardSelectCssStylePage properties
WizardSelectCssStylePage.button.label.browse=参照(&B)...

WizardSelectCssStylePage.button.label.deselectAll=すべて選択解除(&D)

WizardSelectCssStylePage.button.label.selectAll=すべて選択(&S)

WizardSelectCssStylePage.errorMsg.FileNotExist=ファイルが見つかりません。

WizardSelectCssStylePage.info.canNotImport=\ \ (インポートできません)

WizardSelectCssStylePage.label.fileName=ファイル名(&N) :

WizardSelectCssStylePage.label.importToReportDesign=\ レポートデザインにインポート。

WizardSelectCssStylePage.label.notifications=通知 :

WizardSelectCssStylePage.label.selectStylesFrom=次のファイルからスタイルを選択

WizardSelectCssStylePage.msg.FileNameEmpty=ファイル名は空白にできません。

WizardSelectCssStylePage.text.cannot.import.style=\ - スタイルをインポートできません。

WizardSelectCssStylePage.FileNameError.Title=エラー

WizardSelectCssStylePage.FileNameError.Message=選択したファイルは無効です。

###########################################################
#WizardTemplateChoicePage properties
WizardTemplateChoicePage.label.Description=説明 :

WizardTemplateChoicePage.label.Preview=プレビュー :

WizardTemplateChoicePage.label.ReportTemplates=レポートテンプレート :

WizardTemplateChoicePage.label.ShowCheatSheets=レポート作成ガイドの表示(&S)

WizardTemplateChoicePage.label.EnableDefaultThemes=デフォルトのテーマを含める(&I)

WizardTemplateChoicePage.message.BlankReport=内容が定義されていない空白のレポートを作成します。

WizardTemplateChoicePage.message.ChartListing=グループ化されたレポートを作成します。グラフは、それぞれのグループのデータを視覚的に表します。

WizardTemplateChoicePage.message.DualColumnChartListing=グラフとリストのサブグループを 2 つ持つグループ化されたレポートを作成します。

WizardTemplateChoicePage.message.DualColumnListing=サブグループを 2 つ持つグループ化されたレポートを作成します。

WizardTemplateChoicePage.message.FirstReport=顧客リストレポートの作成手順が表示されます。

WizardTemplateChoicePage.message.GroupedListing=一部のフィールドをグループ化したリストレポートを作成します。

WizardTemplateChoicePage.message.Letter=データソースのフィールドを使用して個人用の手紙を生成するためのテンプレートを作成します。

WizardTemplateChoicePage.message.MailingLabels=郵便用のラベル

WizardTemplateChoicePage.message.SideBySideChartListing=グループ化されたレポートを作成します。それぞれのグループには、グラフとリストが並列表示されます。

WizardTemplateChoicePage.message.SimpleListing=データソースのフィールドを使用したリストレポートを作成します。

WizardTemplateChoicePage.message.Crosstab=チュートリアルを使用してクロスタブレポートを構築します。

WizardTemplateChoicePage.message.BlankAuto=表示したときに自動的にカスタマイズが開始される空白のレポートテンプレート。

WizardTemplateChoicePage.title.BlankAuto=空白の自動カスタマイズ開始レポート

WizardTemplateChoicePage.title.BlankReport=空のレポート

WizardTemplateChoicePage.title.ChartListing=グラフとリスト

WizardTemplateChoicePage.title.DualColumnChartListing=列グラフとリストの同時表示

WizardTemplateChoicePage.title.DualColumnListing=列リストの同時表示

WizardTemplateChoicePage.title.FirstReport=はじめてのレポート

WizardTemplateChoicePage.title.GroupedListing=グループごとのリスト

WizardTemplateChoicePage.title.Letter=レター

WizardTemplateChoicePage.title.MailingLabels=郵便用のラベル

WizardTemplateChoicePage.title.SideBySideChartListing=グラフとリストの並列表示

WizardTemplateChoicePage.title.SimpleListing=簡易リスト

WizardTemplateChoicePage.title.Crosstab=クロスタブ
#bidi_hcg start
WizardTemplateChoicePage.label.rtlBiDiOrientation=右から左

WizardTemplateChoicePage.label.ltrBiDiOrientation=左から右

WizardTemplateChoicePage.label.chooseBiDiDirection=レポートの向き(&R) :

WizardTemplateChoicePage.RootDisplayName=標準
#bidi_hcg end
###########################################################
#XMLSourcePage properties
XMLSourcePage.Error.Dialog.Message.InvalidFile=XML のソースページにあるエラーを修正しないと、他のページを使用できません。

XMLSourcePage.Error.Dialog.Message.SaveFile=XML のソースページが変更されました。XML のソースページを保存しないと、他のページを使用できません。

XMLSourcePage.Error.Dialog.title=エラー

XMLSourcePage.Error.Dialog.Message.PromptMsg=XML レポートデザインのソースが変更されました。変更内容を保存しますか ?

XMLSourcePage.Error.Dialog.Message.Yes=はい

XMLSourcePage.Error.Dialog.Message.No=いいえ

XMLSourcePage.Error.Dialog.Message.Cancel=キャンセル

###########################################################
#SaveReportAsWizard properties
SaveReportAsWizard.SettingPage.title=テンプレートプロパティの設定

SaveReportAsWizard.SaveAsPageTitle=名前を付けて保存

SaveReportAsWizard.SaveAsLibraryMessage=このファイルをライブラリとして別名で保存します。

SaveReportAsWizard.SaveAsReportorTemplateMessage=このファイルをレポートデザインまたはテンプレートとして別名で保存します。

SaveReportAsWizard.SettingPageTitle=レポートプロパティの設定

SaveReportAsWizard.SettingPage.message=レポートをテンプレートとして保存すると、これらのプロパティが使用されます。

SaveReportAsWizard.SaveAsWizardWindowTitle=名前を付けて保存

SaveReportAsWizard.SaveAsWizardPageTitle=名前を付けて保存

SaveReportAsWizard.SaveAsWizardPageDesc=ファイルを別の場所に保存します。

SaveReportAsWizard.SettingPage.Messages=テンプレートのプロパティを設定してください。

###########################################################
#WizardReportSettingPage properties
WizardReportSettingPage.Error.ReportorTemplate=ファイル名の拡張子は、{0} または .rpttemplate にしてください。

WizardReportSettingPage.Error.Library=ファイル名の拡張子は、.rptlibrary にしてください。

###########################################################
#WizardSaveAsPage properties
WizardSaveAsPage.FileLabel=ファイル名(&F) :

WizardSaveAsPage.Question=質問

WizardSaveAsPage.FolderEmpty=\ フォルダが空です。

WizardSaveAsPage.NoProject=\ 指定したプロジェクトは存在しません。

WizardSaveAsPage.PathOccupied=\ その場所にはすでにファイルがあります : {0}

WizardSaveAsPage.EmptyName=\ ''{0}'' の名前が空です。

WizardSaveAsPage.SelectFolder=\ フォルダの選択 :

WizardSaveAsPage.NameExists=\ 同じ名前はすでに使用されています。

WizardSaveAsPage.OverwriteQuestion=指定したファイル ''{0}'' はすでに存在します。既存のファイルを上書きしますか ?

WizardSaveAsPage.InvalidFileName=\ ''{0}'' は有効なファイル名ではありません。

WizardSaveAsPage.ContainerGroup=\ 親フォルダの入力・選択(&E) :

###########################################################
#Library DND message
Library.DND.messages.outofsync = レポートで使用しているライブラリは、変更されています。更新されているライブラリを使用するには、このレポートをいったん閉じてから開き直してください。

NamedExpressionsHandleProvier.Column.Name=名前

NamedExpressionsHandleProvier.Column.DefaultValue=デフォルト値

Library.DND.messages.cannotApplyTheme=テーマを適用できません。

JarFileFormProvider.Column.Name=名前

JsFileFormProvider.Column.Name=名前

ReportPageGenerator.List.Resources.JsFile=JavaScriptファイル :

PropertiesFileFormProvider.Column.Name=名前

ReportPageGenerator.List.Resources.PropertiesFile=プロパティファイル :

###########################################################
#ClassFinder
ClassFinder.TaskStart=ハンドラクラスを検索中...

ClassFinder.DialogTitle=クラスの選択

ClassFinder.DialogMessage=ハンドラクラスの選択(&C) :

ClassFinder.ErrorMessage=クラスが見つかりません。

ClassPathBlock_button.addExtFolder=外部クラスフォルダを追加(&F)...

ClassPathBlock_button.addExtJars=外部 Jar を追加(&X)...

ClassPathBlock_button.down=下へ

ClassPathBlock_button.edit=編集(&E)...

ClassPathBlock_button.remove=削除(&R)

ClassPathBlock_button.up=上へ

ClassPathBlock_FileDialog.edit.text=JAR の編集

ClassPathBlock_FileDialog.jar.text=JAR の選択

ClassPathBlock_FolderDialog.edit.message=クラスフォルダの新しい場所を指定 :

ClassPathBlock_FolderDialog.edit.text=クラスフォルダの編集

ClassPathBlock_FolderDialog.message=クラスフォルダの場所の選択 :

ClassPathBlock_FolderDialog.text=外部クラスフォルダの選択

ClassPathBlock_label.LibrariesList=レポートの実行パス上の JAR およびクラスフォルダ(&T) :

###########################################################
#ListingSectionPage
ListingSectionPage.RepeatHeader=レポートヘッダ(&R)

###########################################################
#ResourceEditDialog
ResourceEditDialog.NotFile.Title=ファイルが見つかりません。

ResourceEditDialog.NotFile.Message=ファイル ''{0}'' は存在しません。

ResourceEditDialog.ReadOnlyEncounter.Title=読み取り専用ファイル

ResourceEditDialog.ReadOnlyEncounter.Message=ファイル ''{0}'' は読み取り専用です。先に、このファイルを書き込み可能にしてください。

###########################################################
#EventHandler
EventHandlerPage.Browse=参照(&B)...


###########################################################
#IDEFileReportProvider
IDEClassPathBlock.button_addExtFolder=外部クラスフォルダを追加(&F)...

IDEClassPathBlock.button_addEXTJar=外部 Jar を追加(&X)...

IDEClassPathBlock.button_addJar=JAR を追加(&J)...

IDEClassPathBlock.button_addProject=プロジェクトを追加(&P)...

IDEClassPathBlock.button_addVar=変数を追加(&V)...

IDEClassPathBlock.button_down=下へ

IDEClassPathBlock.button_edit=編集(&E)...

IDEClassPathBlock.button_folder=クラスフォルダを追加(&C)...

IDEClassPathBlock.button_remove=削除(&R)

IDEClassPathBlock.button_up=上へ

IDEClassPathBlock.fLibrariesList_text=レポートの実行パス上の JAR およびクラスフォルダ(&T) :

IDEClassPathBlock.ProjectDialog_message=追加するプロジェクトの選択 :

IDEClassPathBlock.ProjectDialog_title=必須プロジェクトの選択

IDECPListLabelProvider.folder=(クラスフォルダ)

IDECPListLabelProvider.missing=(見つかりません)

IDECPListLabelProvider.new=(新規)

IDECPListLabelProvider.unknown=不明な要素

IDEFileReportProvider.ReadOnlyEncounter.Title=読み取り専用ファイル

IDEFileReportProvider.ReadOnlyEncounter.Message=ファイル ''{0}'' は読み取り専用です。先に、このファイルを書き込み可能にしてください。

IDEResourcePageHelper.Dialog.Prompt=ディレクトリの選択 :

IDEResourcePageHelper.Dialog.Text=ワークスペースへの相対的な位置の選択(&C) :

IDEResourcePageHelper.Dialog.Title=ディレクトリの選択

IDEResourcePageHelper.File.Button=ファイルシステム(&F)...

IDEResourcePageHelper.Varible.Button=変数(&B)...

IDEResourcePageHelper.WorkSpace.Button=ワークスペース(&W)...

###########################################################
#DataSetColumnBindingsFormHandleProvider
DataSetColumnBindingsFormHandleProvider.Column.Name=名前

DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID=表示名 ID

DataSetColumnBindingsFormHandleProvider.Column.DisplayName=表示名

DataSetColumnBindingsFormHandleProvider.DatasetTitle=データカラムのバインド :

DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns=バインドする列の更新

DataSetColumnBindingsFormHandleProvider.ReportItemTitle=使用可能な列バインド :

DataSetColumnBindingsFormHandleProvider.NewBinding=新規バインド

DataSetColumnBindingsFormHandleProvider.Column.DataType=データ型

DataSetColumnBindingsFormHandleProvider.Column.Expression=式

DataSetColumnBindingsFormHandleProvider.Column.AggregateOn=集計条件

DataSetColumnBindingsFormHandleProvider.Column.Filter=フィルタ

DataSetColumnBindingsFormHandleProvider.Column.Function=関数

DataSetColumnBindingsFormHandleProvider.ALL=すべて

DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns=バインドする列の生成

DataSetColumnBindingsFormHandleProvider.NONE=N/A
DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns=バインドする列の生成

DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns=バインドする列の更新

###########################################################
#DataSetColumnBindingsFormPage
DataSetColumnBindingsFormPage.Button.RemoveUnused=未使用のカラムの削除

###########################################################
#DataSetSettingsPage
DataSetSettingsPage.DataGroup.Title=BIRT Engine を設定してデータセット列の値を比較する

DataSetSettingsPage.Locale.Label=文字列の値のロケール(&L) :

DataSetSettingsPage.NullValueOrdering.Label=Null 値の順序(&O) :

###########################################################
#ResourceSelectionValidator
ResourceSelectionValidator.ExtError=選択したファイルの拡張子は次のようにしてください :

ResourceSelectionValidator.EmptyError=ファイルを選択してください。

ResourceSelectionValidator.ExtErrorSplitter=\ または

###########################################################
#GeneralPage
GeneralPage.Library.Included=ライブラリ :

GeneralPage.Library.Modified.Note=注 : このレポート項目はライブラリで編集されています。元に戻すには、{0}[プロパティの復元] をクリックしてください。

#BookMarkPage
BookMarkPage.Modified.Note=ブックマークに、A～Z、a～z、0～9、-、_、:、. 以外の特殊文字が含まれており、このために実行時に問題が発生する可能性があります。

BookMarkPage.Modified.WithoutQuotes=ブックマークが二重引用符で囲まれていないため、実行時に問題が発生する可能性があります。

###########################################################
# LOCALE SPECIAL:
# following string need be specially localized, e.g. as per local country custom, not just simple translation
# for example:
#       the social security number in US is like 760-01-0101, the format should be @@@-@@-@@@@
#       but the SSN in Swedish is like 760101-0101, the format should be @@@@@@-@@@@
# 
FormatStringPage.SimpleTextZipCode=94103

FormatStringPage.SimpleTextZipCode4=9411234

FormatStringPage.PhoneNumber=4155551111

FormatStringPage.SocialSecurityNumber=123456789

FormatStringPage.simpleTexZipCodeFormat=@@@

FormatStringPage.simpleTexZipCode4Format=@@@@@-@@@@
FormatStringPage.phoneNumberFormat=(@@@)@@@-@@@@
FormatStringPage.securityNumberFormat=@@@-@@-@@@@
FormatStringPage.Label.PreserveWhiteSpaces=スペースを残す

FormatStringPage.Preview.PreserveWhiteSpaces=\    先頭にスペースが入っている文字列

###########################################################
#PublishLibraryDialog
PublishLibraryDialog.ShellText=ライブラリの共有

PublishLibraryDialog.TitleArea=ライブラリの共有

PublishLibraryDialog.Message=共有リソースフォルダにライブラリをコピーします。

PublishLibraryDialog.Label.SourceFileName=ソースファイル(&S) :

PublishLibraryDialog.Label.FileName=ファイル名(&N) :

PublishLibraryDialog.Label.Folder=フォルダ(&D):

PublishLibraryDialog.Label.Browse=参照(&B)...

PublishLibraryDialog.Message.FileNameEmpty=ファイル名を指定してください。

PublishLibraryDialog.Message.FileNameError=ファイル名の拡張子は、.rptlibrary にしてください。

PublishLibraryDialog.Message.FolderEmpty=フォルダを公開してください。

PublishLibraryDialog.Message.SourceFileEmpty=ソースファイルの名前を指定してください。

PublishLibraryDialog.Label.Browse2=参照(&W)...

PublishLibraryDialog.AddText=ライブラリの追加

PublishLibraryDialog.AddMessage=BIRT リソースフォルダにライブラリを追加します。

WizardLibrarySettingPage.ErrorMessage=フォルダを選択してください。

WizardLibrarySettingPage.Resourcefile.Dialog.Title=リソースフォルダの参照

WizardLibrarySettingPage.Resourcefile.Dialog.Message=リソースフォルダまたはそのサブフォルダを選択してください。

PublishLibraryDialog.AddLibrary=ライブラリの追加


###########################################################
#PublishCSSDialog
PublishCSSDialog.ShellText=CSS ファイルの公開

PublishCSSDialog.TitleArea=CSS ファイルをリソースフォルダに公開

PublishCSSDialog.Message=ここで選択した CSS ファイルのスタイルがリソースフォルダにインポートされます。

PublishCSSDialog.Label.SourceFileName=ソースファイル(&S) :

PublishCSSDialog.Label.FileName=ファイル名(&N) :

PublishCSSDialog.Label.Folder=フォルダ(&F) :

PublishCSSDialog.Label.Browse=参照(&B)...

PublishCSSDialog.Label.Browse2=参照(&W)...

PublishCSSDialog.ErrorMessage.SelectFolder=フォルダを選択してください。

PublishCSSDialog.Dialog.FileSeclect.Tilte=CSS ファイルの選択

PublishCSSDialog.Error.Exception.Title=エラー

PublishCSSDialog.Error.FileNotFound=ファイル ''{0}'' が見つかりません。

PublishCSSDialog.Error.FileIsNotCSSFile=ファイル ''{0}'' の拡張子が ''{1}'' ではありません。

PublishCSSDialog.Error.FileExist=ファイルはすでに存在します。

PublishCSSDialog.Message.SourceFileEmpty=ソースファイルの名前を指定してください。

PublishCSSDialog.Message.FileNameEmpty=ファイル名を指定してください。

PublishCSSDialog.Message.FileNameError=ファイル名の拡張子は、.css にしてください。

PublishCSSDialog.Message.FolderEmpty=フォルダを公開してください。

WizardCSSSettingPage.ErrorMessage=フォルダを選択してください。

WizardCSSSettingPage.Resourcefile.Dialog.Title=リソースフォルダの参照

WizardCSSSettingPage.Resourcefile.Dialog.Message=リソースフォルダまたはそのサブフォルダを選択してください。

###########################################################
#PublishResourceDialog
PublishResourceDialog.ShellText=リソースの公開

PublishResourceDialog.TitleArea=リソースをリソースフォルダに公開

PublishResourceDialog.Message=リソースフォルダにリソースファイルを保存してください。

PublishResourceDialog.AddResource=リソースの追加

PublishResourceDialog.AddText=リソースの追加

PublishResourceDialog.AddMessage=リソースフォルダにリソースを追加してください。

PublishResourceAction.wizard.errorTitle=エラー

PublishResourceAction.wizard.message=リソースがすでに存在するディレクトリには、ライブラリを公開できません。公開するには、別のディレクトリを選択してください。

PublishResourceAction.wizard.message.SourceFileNotExist=ソースファイルが存在しません。

PublishResourceAction.wizard.notvalidfolder=設定に無効なリソースフォルダが定義されています。

PublishResourceDialog.Label.FileName=ファイル名(&N) :

PublishResourceDialog.Label.SourceFileName=ソースファイル(&S) :

PublishResourceDialog.Label.Folder=フォルダ :

PublishResourceDialog.Label.Browse=参照(&B)...

PublishResourceDialog.Label.Browse2=参照(&W)...

WizardResourceSettingPage.ErrorMessage=フォルダを選択してください。

WizardResourceSettingPage.Resourcefile.Dialog.Title=リソースフォルダの参照

WizardResourceSettingPage.Resourcefile.Dialog.Message=リソースフォルダまたはそのサブフォルダを選択してください。

PublishResourceDialog.Message.SourceFileEmpty=ソースファイルの名前を指定してください。

PublishResourceDialog.Message.FileNameEmpty=ファイル名を指定してください。

PublishResourceDialog.Message.FolderEmpty=フォルダを公開してください。

###########################################################
#EmbeddedImagesNodeProvider
EmbeddedImagesNodeProvider.FileNameError.Title=エラー

EmbeddedImagesNodeProvider.FileNameError.Message=選択したファイルは、イメージではありません。

EmbeddedImagesNodeProvider.stackMsg.insert=イメージの挿入

###########################################################
#AddElementtoAction properties
AddElementtoAction.Text=レポートに追加(&A)

ApplyThemeAction.Text=レポートに適用(&A)

###########################################################
#MultiPageReportEditor properties
MultiPageReportEditor.CheckVersion.Dialog.Title=警告

MultiPageReportEditor.CloseButton=閉じる

MultiPageReportEditor.ConfirmTitle=ファイルが存在しません

MultiPageReportEditor.ConfirmVersion.Dialog.Message=このレポートデザインは以前のバージョンの BIRT Designer で作成されたものです。このレポートを保存すると現在のバージョンにアップグレードされるので、以前のバージョンの BIRT では開けなくなる可能性があります。保存しますか ?

MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage=このエディタのコンテンツが外部から変更された可能性があります。変更を再ロードしますか ?

MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage=このエディタのコンテンツが外部から変更された可能性があります。変更されたコンテンツを再ロードしますか ?

MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage=このエディタのコンテンツが外部から変更された可能性があり、変更が保存されていません。\n\nエディタを保存して外部からの変更を再ロードしますか ?

MultiPageReportEditor.ConfirmVersion.Dialog.Title=確認

MultiPageReportEditor.SaveButton=上書き保存(&S)

MultiPageReportEditor.DiscardButton=破棄(&D)

MultiPageReportEditor.CancelButton=キャンセル

MultiPageReportEditor.SaveConfirmMessage=このファイルは削除されました。変更を保存しますか ? または、変更を保存せずにエディタを閉じますか ?

TableArea.Button.Up=上へ(&U)

###########################################################
#AddJarResourceFileFolderSelectionDialog
AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist=すでに存在するファイルを選択しています。

AddJarResourceFileFolderSelectionDialog.Message={0} ファイルを選択してください :

AddJarResourceFileFolderSelectionDialog.ErrorMessage.ExtError=ファイル名の拡張子は {0} にしてください。

AddJarResourceFileFolderSelectionDialog.Message.Or=OR


###########################################################
#AddImageResourceFileFolderSelectionDialog
AddImageResourceFileFolderSelectionDialog.Title=すでに存在するファイルを選択しています。

AddImageResourceFileFolderSelectionDialog.Message=イメージの選択 :

###########################################################
#DataItemBindingDialog properties
DataItemBindingDialog.text.Name=名前:

DataItemBindingDialog.text.displayName=表示名 :

DataItemBindingDialog.text.DataType=データ型 :

DataItemBindingDialog.text.Expression=式 :

DataItemBindingDialog.text.AggregateOn=集計条件 :

DataItemBindingDialog.text.ForceBinding=同じ式で新しいバインドを作成します。

DataItemBindingDialog.text.All=すべて

DataItemBindingDialog.text.None=N/A
DataItemBindingDialog.text.Null=なし

DataItemBindingDialog.title.CreateNewItem=データ項目バインドの新規作成

DataItemBindingDialog.title.EditDataItem=データ項目バインドの編集

DataItemBindingDialog.text.Function=関数 :

DataItemBindingDialog.text.Filter=フィルタ:

DataItemBindingDialog.text.ArgumentList=引数:

DataItemBindingDialog.Button.Add=追加

DataItemBindingDialog.Button.Edit=編集

DataItemBindingDialog.Button.Delete=削除

###########################################################
#DataColumBindingDialog properties
DataColumBindingDialog.title.CreateNewDataBinding=データバインドの新規作成

DataColumBindingDialog.title.EditDataBinding=データバインドの編集

DataColumBindingDialog.title.AggBuilder=集計ビルダ

DataColumnBindingDialog.NewBindingDialogInputNewNameMessage=データバインドの新しい名前 :

DataColumnBindingDialog.NewBindingDialogInputNewNameTitle=データバインドの新しい名前の入力

DataColumnBindingDialog.NewBindingDialogMessage=このデータバインドは、複数のレポート項目やセルで使用されています。このレポート項目に新しい式を作成しますか ? 新しいデータバインドを作成するには [はい] をクリックしてください。データバインドを全体で変更するには [いいえ] をクリックしてください。

DataColumnBindingDialog.NewBindingDialogButtonYes=はい

DataColumnBindingDialog.NewBindingDialogButtonNo=いいえ

DataColumnBindingDialog.NewBindingDialogButtonCancel=キャンセル

DataColumnBindingDialog.NewBindingDialogTitle=データバインドの新規作成

DataColumnBindingDialog.title.TimePeriodBuild=相対期間の集計ビルダ

DataColumBindingDialog.stackMsg.resetReference=参照のリセット

###########################################################
#CreatePlaceHolderAction properties
ChangeDataColumnAction.text=データ列の変更(&G)...

###########################################################
#ThumbnailBuilder properties
ThumbnailBuilder.Text.Title=サムネイルの管理

ThumbnailBuilder.Button.Text.Generate=生成(&G)

ThumbnailBuilder.Button.Text.Remove=削除(&R)

ThumbnailBuilder.Button.Text.Import=インポート(&I)...

ThumbnailBuilder.Button.Text.Browse=参照(&B)...

ThumbnailBuilder.FileDialog.Title=イメージファイルの選択

ThumbnailBuilder.FileDialog.FileNameError.Title=エラー

ThumbnailBuilder.FileDialog.FileNameError.Message=選択したファイルは、イメージではありません。

ThumbnailBuilder.Button.GenerateFromReport=レポートから生成(&N)

ThumbnailBuilder.Button.BrowseFromFileSystem=ファイルシステムの参照(&W)

ThumbnailBuilder.Button.ImportFromResource=共有リソースからインポート(&M)

ThumbnailBuilder.Image.DefaultName=サムネイルのイメージ

ThumbnailBuilder.PreviewImage.WarningDialog.Title=警告

ThumbnailBuilder.PreviewImage.WarningDialog.Prompt=サイズの大きいイメージ (1014x768 以上) を選択しました。これによりメモリ不足の例外が発生する可能性があります。\n続けますか ?


###########################################################
#FragmentResourceEntry properties
FragmentResourceEntry.RootName=フラグメント

FragmentResourceEntry.RootDisplayName=システムリソース


FragmentTemplateResourceEntry.RootName=TemplateFragments
FragmentTemplateResourceEntry.RootDisplayName=テンプレートリソース

###########################################################
#PathResourceEntry properties
PathResourceEntry.RootName=リソースフォルダ

PathResourceEntry.RootDisplayName=共有リソース

###########################################################
#ResourceFilter properties
ResourceFilter.DisplayName.CVS=CVS リソース

ResourceFilter.DisplayName.Dot=.* リソース

ResourceFilter.DisplayName.Data=*.data リソース

ResourceFilter.DisplayName.EmptyFolder=空のフォルダ

ResourceFilter.Description.CVS=名前が \"CVS\" から始まるリソースを非表示にする

ResourceFilter.Description.Dot=名前が次の文字列から始まるリソースを非表示にする

ResourceFilter.Description.Data=すべてのデータファイルを非表示にする

ResourceFilter.Description.EmptyFolder=空のフォルダを非表示にする

###########################################################
#BIRTGotoMarker properties
BIRTGotoMarker.Error.Title=エラー

BIRTGotoMarker.Error.Message=レポート要素は存在しません。

###########################################################
#TabbedList properties
TabbedPropertyList.properties.not.available=プロパティがありません。

###########################################################
#Sample Reports View properties
SampleReportsView.defautDescription=サンプルレポートをカテゴリから選択してください。

SampleReportsView.Text.Explorer=サンプルレポート

SampleReportsView.Text.Preview=プレビュー

SampleReportsView.Text.Description=説明

SampleReportsView.MessageDialog.Title=質問

SampleReportsView.MessageDialog.Message=''{0}'' はすでに存在します。置き換えますか ?

SampleReportsView.Action.exportSampleReport=エクスポート(&E)

SampleReportsView.Action.openSampleReport=インポート(&I)

SampleReportsView.Action.exportSampleReport.toolTipText=選択したサンプルレポートをエクスポートします

SampleReportsView.Action.openSampleReport.toolTipText.ide=レポートプロジェクトを作成して、レポートをプロジェクトフォルダに配置します

SampleReportsView.Action.openSampleReport.toolTipText.rcp=選択したサンプルレポートを RCP 作業フォルダに直接エクスポートします

SampleReportsView.Text.Contribute_Samples=サンプルの提供

UseCssStyleAction.text=CSS ファイルを使用(&U)...

EditUseCssStyleAction.text=CSS ファイルの編集(&E)...

EditUseCssStyleAction.EditCssTitle=CSS の編集

EditUseCssStyleAction.EditCssAreaTitle.Report=レポートデザインで CSS ファイルを編集

EditUseCssStyleAction.EditCssAreaTitle.Libary=レポートライブラリで CSS ファイルを編集

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=全般

CrosstabPageGenerator.List.Margin=余白

CrosstabPageGenerator.List.Font=フォント

CrosstabPageGenerator.List.Borders=罫線

CrosstabPageGenerator.List.Section=改ページ

CrosstabPageGenerator.List.Visibility=表示/非表示

CrosstabPageGenerator.List.TOC=目次

CrosstabPageGenerator.List.Bookmark=ブックマーク

CrosstabPageGenerator.List.NamedExpressions=名前付き式

CrosstabPageGenerator.List.UserProperties=ユーザのプロパティ

CrosstabPageGenerator.List.AdvancedProperty=詳細設定

CrosstabPageGenerator.List.SubTotals=小計

CrosstabPageGenerator.List.GrandTotals=集計

CrosstabPageGenerator.List.PageBreak=改ページ

###########################################################
#CubePageGenerator properties
CubePageGenerator.List.General=全般

CubePageGenerator.List.Comments=コメント

CubePageGenerator.List.AdvancedProperty=詳細設定


###########################################################
#LibraryLayoutEditorFormPage
LibraryLayoutEditorFormPage.warning.title=ライブラリ

LibraryLayoutEditorFormPage.warning.message=レポート項目をライブラリに追加するには、パレットから項目をドラッグするか、[挿入] メニューを使用します。\n\nレポート項目をエディタで表示するには、アウトラインビューでレポート項目をクリックします。

LibraryLayoutEditorFormPage.warning.prompt=今後、このメッセージを表示しない(&D)

###########################################################
#PageGenerator Item properties
PageGenerator.TabItem.Attributes=プロパティ

PageGenerator.TabItem.Binding=バインド

PageGenerator.TabItem.Filters=フィルタ

PageGenerator.TabItem.Groups=グループ

PageGenerator.TabItem.Highlights=ハイライト

PageGenerator.TabItem.map=マップ

PageGenerator.TabItem.Sorting=ソート

###########################################################
#AggDataDropAdapter
AggDataDropAdapter.Trans.Name=集計の作成

###########################################################
#BindingDialogHelper
BindingDialogHelper.text.Name=列バインドの名前(&B) :

BindingDialogHelper.text.DataType=データ型(&T) :

BindingDialogHelper.text.Function=関数(&F) :

BindingDialogHelper.text.DataField=データフィールド(&F) :

BindingDialogHelper.text.Filter=フィルタ条件(&C) :

BindingDialogHelper.text.AggOn=集計条件(&G) :

BindingDialogHelper.text.Table=テーブル(&L)

BindingDialogHelper.text.List=リスト(&L)

BindingDialogHelper.text.Grid=グリッド

BindingDialogHelper.text.Group=グループ(&O)

BindingDialogHelper.text.Expression=式(&E) :

BindingDialogHelper.text.displayName=表示名(&N) :

BindingDialogHelper.text.allowExport=エクスポート(&O) :

BindingDialogHelper.text.allowExport.button=エクスポートを許可する

BindingDialogHelper.text.displayNameID=表示名 ID(&S) :

BindingDialogHelper.bindingName.dataitem=列バインド :

BindingDialogHelper.bindingName.aggregation=集計

BindingDialogHelper.error.nameduplicate={0} という名前の列バインドはすでに存在します。

BindingDialogHelper.error.improperexpression=キューブのメジャーに対する集計ではこの型は使用できません : "{0}"

BindingDialogHelper.error.empty={0} を指定してください。

BindingDialogHelper.error.text.Name=列バインドの名前

BindingDialogHelper.text.Colon=:
BindingDialogHelper.warning.function=現在の関数に推奨されるデータ型は "{0}" です。選択したデータ型を保存しますか ?

BindingDialogHelper.warning.parameter=式 "{0}" の戻り値のデータ型は、パラメタ "{1}" ではサポートされていません。このデータ型を保存しますか ?

BindingDialogHelper.warning.button.yes=はい

BindingDialogHelper.warning.button.no=いいえ

BindingGroupDescriptorProvider.Flag.DataModel=\ (データモデル)

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel=データモデルのバインド

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet=データセットのバインド

###########################################################
#Scripts Node
Scripts.Node.DisplayName=スクリプト

ScriptElementNode.Action.Text=レポート要素にジャンプ(&G)

ScriptObjectNode_0=スクリプトのリセット(&R)

ReportPage.PreviewImage.Button.ToolTip=サムネイルの管理

###########################################################
#CrosstabBindingDialogHelper
CrosstabBindingDialogHelper.AggOn.All=すべて

CrosstabBindingDialogHelper.bindngName.timeperiod=期間

CrosstabBindingDialogHelper.calculation.label=期間(&P) :

CrosstabBindingDialogHelper.calculation.group=期間

CrosstabBindingDialogHelper.recentday.description=期間内の最後の日付

CrosstabBindingDialogHelper.referencedate.label=参照日(&R) :

CrosstabBindingDialogHelper.thisdate.label=使用する日付 :

CrosstabBindingDialogHelper.thisdate.example.label=指定された日付値を二重引用符で囲んでください。\n例 : "2012-01-23"、"1/23/2012"。

CrosstabBindingDialogHelper.thisdate.example.label.constant=例 : 2012-01-23、1/23/2012。

CrosstabBindingDialogHelper.timedimension.label=時間のディメンション(&M) :

CrosstabBindingDialogHelper.timeperiod.wrongdate=このディメンションは最近の参照日をサポートしていません

CrosstabBindingDialogHelper.today.label=今日

AddEditCascadingParameterDialog.label.name=名前(&N):

AddEditCascadingParameterDialog.label.dataset=データセット(&D) :

AddEditCascadingParameterDialog.label.value=値(&V) :

AddEditCascadingParameterDialog.label.displaytext=表示テキスト(&T) :

###########################################################
#FormatDateTimePattern
FormatDateTimePattern.datetiem_format_type_year=年

FormatDateTimePattern.datetiem_format_type_short_year=年 (略)

FormatDateTimePattern.datetiem_format_type_long_month_year=年月

FormatDateTimePattern.datetiem_format_type_shot_month_year=年月 (略)

FormatDateTimePattern.datetiem_format_type_month=月

FormatDateTimePattern.datetiem_format_type_long_day_of_week=曜日

FormatDateTimePattern.datetiem_format_type_day_of_month=通日 (月)

FormatDateTimePattern.datetiem_format_type_medium_day_of_year=通日 (年)

FormatDateTimePattern.datetiem_format_type_minutes=分

FormatDateTimePattern.datetiem_format_type_secontds=秒

FormatDateTimePattern.datetiem_format_type_general_time=標準時間

###############################################################################
# LOCALE SPECIAL:
# following text should be localized specifically for each locale, 
# the pattern may be different for each locale. 
# For example, in Polish locale, "general time" pattern should be "HH:mm:ss.SSS" 
# instead of "ahh:mm:ss.SSS".
#
FormatDateTimePattern.pattern.datetiem_format_type_year=yyyy

FormatDateTimePattern.pattern.datetiem_format_type_short_year=yy

FormatDateTimePattern.pattern.datetiem_format_type_long_month_year=yyyy MMMM

FormatDateTimePattern.pattern.datetiem_format_type_shot_month_year=yy MMM

FormatDateTimePattern.pattern.datetiem_format_type_month=MMMM

FormatDateTimePattern.pattern.datetiem_format_type_long_day_of_week=EEEE

FormatDateTimePattern.pattern.datetiem_format_type_day_of_month=dd

FormatDateTimePattern.pattern.datetiem_format_type_medium_day_of_year=yy MMMM dd

FormatDateTimePattern.pattern.datetiem_format_type_minutes=mm

FormatDateTimePattern.pattern.datetiem_format_type_secontds=ss

FormatDateTimePattern.pattern.datetiem_format_type_general_time=ahh:mm:ss.SSS

FormatDescriptorProvider.DisplayName.None=自動

ReportExamples.ContributeSamples.description=Bugzilla のエントリは、{0}バグを BIRT に送る{1}に送信してください。\n同時に、レポートデザインファイルと、レポート出力のスクリーンショットも添付してください。\n\n注 : スクリーンショットは、以下の条件を満たしている必要があります。\n  1. ファイル形式: *.png\n  2. 解像度 : 800 x 600\n  3. サイズ : 250 KB 以内

ReportExamples.ContributeSamples.title=サンプルの提供

ReportExamples.ContributeSamples.messages=以下に、サンプルの提供手順を示します。

ReportExamples.OpenBrowser.Error.title=エラー

ReportExamples.OpenBrowser.Error.message=Web ブラウザを開けません。

###########################################################
#CopyCellContentsContextAction
CopyCellContentsContextAction.actionText=コンテンツのコピー(&E)

###########################################################
#CopyCellContentsAction
CopyCellContentsAction.actionText=コンテンツのコピー(&E)

###########################################################
#CopyFormatAction
CopyFormatAction.text=書式のコピー(&F)

###########################################################
#PasteFormatAction
PasteFormatAction.text=書式の貼り付け(&O)

###########################################################
#InsertAggregationAction
InsertAggregationAction.text=集計(&A)


###########################################################
#InsertRelativeTimePeriodAction
InsertRelativeTimePeriodAction.text=相対期間(&V)

###########################################################
#Multiple views
MultipleAdapter.SetCurrentView=現在のビューの設定

MultipleAdapter.RemoveView=ビューの削除

MultipleGuideHandle.RemoveView=削除

CreateChartAction.text=グラフビューの作成(&W)

CreateChartHandler.text=グラフビューの作成

###########################################################
#Input Parameters
InputParameterDialog.msg.title=入力パラメタ

InputParameterDialog.msg.requiredParam=アスタリスク (*) が付いているパラメタは必須項目です。

InputParameterDialog.err.requiredParam=必須パラメタ [{0}] の値が指定されていません。

InputParameterDialog.err.invalidValueTitle=無効な値

InputParameterDialog.err.invalidValue=値 "{0}" は、型 {1} では使用できません。

InputParameterDialog.err.emptyValue={0} の入力値は空白にできません。

InputParameterDialog.err.emptyValue2={0} の 2 つ目の入力値は空白にできません。

###########################################################
#Crosstab drag tracker
ColumnDragTracker.ResizeColumn=列幅の変更

ColumnDragTracker.Show.Label=幅 :

CPListLabelProvider.misssing=(見つかりません)

RowDragTracker.ResizeRow=行幅の変更

RowDragTracker.Show.Label=高さ :

TableDragGuideTracker.Pixels=ピクセル

###########################################################
#General Designer settings
GeneralConfigurationBlock.message.general.description=レポートデザイナの一般的なオプションを設定します。

GeneralConfigurationBlock.button.text.enable.gradient=グラデーションの選択を有効にする(&G)

GeneralConfigurationBlock.button.text.enable.animation=アニメーションの選択を有効にする(&N)

GeneralConfigurationBlock.group.label=確認する

GeneralConfigurationBlock.group.defaultThemes=デフォルトのテーマライブラリ

GeneralConfigurationBlock.button.text.prompt=ライブラリのファイルを開いた後に確認メッセージを表示する(&P)

GeneralConfigurationBlock.button.text.defaultTheme.enable=デフォルトのテーマライブラリを有効にする(&T)

GeneralConfigurationBlock.button.text.defaultTheme.include=デフォルトのテーマを含める(&I)

GeneralConfigurationBlock.move.binding.group=パフォーマンスを向上させるためにローカルバインドを親に移動する

GeneralConfigurationBlock.move.binding.always=常に続行する(&W)

GeneralConfigurationBlock.move.binding.never=続行しない(&V)

GeneralConfigurationBlock.move.binding.prompt=確認メッセージを表示する(&R)

###########################################################
#ResetImageSizeAction
ResetImageOriginalSizeAction.trans.label=イメージサイズのリセット

ResetImageSizeAction.label=元のサイズに戻す(&Z)
###########################################################
#Report document editor
ReportDocumentEditor.1=レポート文書エディタ

ReportDocumentEditor.2=文書の情報

ReportDocumentEditor.3=名前:

ReportDocumentEditor.4=バージョン:

ReportDocumentEditor.5=ページ数 :

ReportDocumentEditor.errorMessage=エディタで文書を開いた際にエラーが発生しました。

###########################################################
#Export Element Dialog
ExportElementDialog.Dialog.Title=要素をライブラリにエクスポート

ExportElementDialog.Dialog.Message=エクスポート先のライブラリを選択するか、新しくエクスポート先を作成してください。

ExportElementDialog.label.NewFile=新規ファイル名(&N) :

ExportElementDialog.ErrorMessage=ファイル名の拡張子は .rptlibrary にしてください。

ExportElementDialog.ErrorMessageInvalid=ファイル名が無効です。

ExportElementDialog.ErrorMessageDuplicate=このライブラリにはすでに要素があります。

ExportElementDialog.ErrorStatusCanNotExport=要素をエクスポートできません。

ExportElementDialog.WarningMessageDuplicate.Message=コンポーネント名が重複しています。既存のライブラリコンポーネントを上書きしますか ?\n\n既存のコンポーネントを上書きする場合は、{0} をクリックします。\n要素のエクスポートダイアログボックスに戻るには、{1} をクリックします。\n操作をキャンセルするには、{2} をクリックします。

ExportElementDialog.WarningMessageDuplicate.Title=置換の確認

ExportElementDialog.WarningMessageDuplicate.OverrideImage=エクスポートされたイメージで参照されている埋め込まれたイメージは、すでにライブラリに存在します。上書きしますか ?

###########################################################
#NewStyleHandler
NewStyleHandler.transaction.label=スタイルの新規作成...

AdvancePropertyDescriptorProvider.DisplayName=詳細設定

AdvancePropertyDescriptorProvider.Tooltip.Alphabetic=文字順にソート

AdvancePropertyDescriptorProvider.Tooltip.Group=グループを基準にソート

AdvancePropertyDescriptorProvider.Tooltip.Local=ローカルプロパティを表示

PropertyEditorFactory.Boolean.True=true

PropertyEditorFactory.Boolean.False=false

PropertyEditorFactory.Value.Auto=自動

ExportElementToLibraryAction.waring=警告

ExportElementToLibraryAction.waringMsg=要素に名前が付いていませんが、名前を設定できません。

###########################################################
#ExportStyleDialog
ExportStyleDialog.Label.Text=テーマ(&T) :

ExportStyleDialog.Message=ライブラリにエクスポートするテーマを選択します。

ExportStyleDialog.Title=テーマの選択

###########################################################
#UIUtil
UIUtil.previewconfirm.title=レポートのエラー

UIUtil.previewconfirm.message=レポートにエラーがあります。プレビューを続行しますか ?

###########################################################
#FileReportProvider
FileReportProvider.CopyConfigFile.DeleteFailure=ファイルを削除できません : {0}

###########################################################
#ShowPropertiesViewMenu
ShowPropertiesViewMenu.ActionName=プロパティ(&I)

ShowPropertiesViewMenu.ErrorMessage=プロパティビューを開けませんでした

ShowPropertiesViewMenu.ErrorTitle=エラー

###########################################################
#ResetAttributePage
ResetAttributePage.Style.Restore.TooltipText=現在のページのスタイルプロパティをデフォルトの設定に戻す

ResetAttributePage.Style.Restore.Transaction.Name=スタイルを元に戻す
ResetAttributePage.Style.Restore.Menu.Name=デフォルトのスタイルを復元
ResetAttributePage.Style.Restore.All.Transaction.Name=すべてのスタイルを復元
ResetAttributePage.Style.Restore.All.Menu.Name=すべてのタブでデフォルトのスタイルを復元

###########################################################
#TextEditor
TextEditor.PasteFormattedText=書式付きテキストの貼り付け

###########################################################
#ReloadImageAction
ReloadImageAction.Text=イメージの再ロード(&M)

###########################################################
#VariablesNodeProvider
VariablesNodeProvider.NewActionName=変数の新規作成(&N)

VariablesNodeProvider.Dialog.Title=新規変数

VariablesNodeProvider.NodeName=変数

###########################################################
#VariableNodeProvider
VariableNodeProvider.DialogTitle=変数の編集

###########################################################
#SelectVariableDialog
SelectVariableDialog.AvailableVariables=使用可能な変数(&A) :

SelectVariableDialog.PageSeperator=----- ページ変数 -----

SelectVariableDialog.ReportSeperator=----- レポート変数 -----

SelectVariableDialog.Title=変数の挿入

###########################################################
#VariableDialog
VariableDialog.DataType=データ型(&T) :

VariableDialog.DefaultValue=デフォルト値(&V) :

VariableDialog.Name=名前(&N):

VariableDialog.PageVariable=ページ変数(&P)

VariableDialog.ReportVariable=レポート変数(&R)

VariableDialog.VariableType=型 :

VariableDialog.Error.NameDuplicate=この名前 \"{0}\" は、すでに使用されています。別の名前を付けてください。

###########################################################
#ExpressionBuilderConfigurationBlock
ExpressionBuilderConfigurationBlock.DefaultSyntax=デフォルトの構文(&F) :

###########################################################
#ExpressionEditor
ExpressionEditor.Error.EmptyExpression=式は空白にできません。

ExpressionEditor.Label.Expression=式(&E) :

ExpressionEditor.Title=式の編集

ExpressionSyntaxColoringPage.Accessible.Name.Background=背景色セレクタ

ExpressionSyntaxColoringPage.Accessible.Name.Foreground=前景色セレクタ

ExpressionSyntaxColoringPage.Button.Bold=太字(&B)

ExpressionSyntaxColoringPage.Button.Enable=有効化

ExpressionSyntaxColoringPage.Button.Italic=斜体(&I)

ExpressionSyntaxColoringPage.Button.Restore=デフォルトに戻す(&R)

ExpressionSyntaxColoringPage.Button.Strikethrough=取り消し線(&S)

ExpressionSyntaxColoringPage.Button.Underline=下線(&U)

ExpressionSyntaxColoringPage.Decscription=リストまたはテキスト領域のサンプルから、スタイルをカスタマイズしたいテキストの種類を選択することで、構文の強調表示をカスタマイズします。

ExpressionSyntaxColoringPage.Element.Comment=コメント

ExpressionSyntaxColoringPage.Element.Default=デフォルト

ExpressionSyntaxColoringPage.Element.Keyword=キーワード

ExpressionSyntaxColoringPage.Element.String=文字列

ExpressionSyntaxColoringPage.Label.Background=背景(&O) :

ExpressionSyntaxColoringPage.Label.Foreground=前景(&F) :

ExpressionSyntaxColoringPage.Label.SampleText=サンプル文字列(&S) :

ExpressionSyntaxColoringPage.Label.SyntaxElement=構文の要素(&Y) :

ExpressionSyntaxColoringPage.Link.ColorAndFont=デフォルトのフォントと色を <a href="org.eclipse.ui.preferencePages.GeneralTextEditor">テキストエディタ</a> および <a href="org.eclipse.ui.preferencePages.ColorsAndFonts">フォントと色</a> 環境設定ページで設定することができます。

###########################################################
#VariableDropAdapter
VariableDropAdapter.TranasctionName=変数から日付項目を作成

###########################################################
#RenameInputDialog
RenameInputDialog.Message.BlankName=名前は空白にできません。

RenameInputDialog.Message.DuplicateName=重複する名前が存在します。

###########################################################
#Breadcrumb
BreadcrumbItemDropDown.showDropDownMenu.action.toolTip=子を表示

ToggleBreadcrumbAction.tooltip.switch.breadcrumb=リンクの階層表示の切り替え

###########################################################
#HyperlinkParameterProvider Category
ReportHyperlinkParameterProvider.Category.Report=レポートパラメタ

RenderHyperlinkParameterProvider.Category.Render=レンダリングオプション
ExceptionDialog.title=エラー
