###############################################################################
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation  - initial API and implementation
###############################################################################
# data source messages
DataSource.RuntimeProperty=ランタイムプロパティ(&R)

DataSource.DataSetClassPath=カスタム POJO データセットクラスの共有先(&L)

DataSource.AddRelativeJars0=Jar の追加 (相対パス)(&A)...

DataSource.AddAbsoluteJars0=Jar の追加 (絶対パス)(&J)...

#To avoid duplicate short-cut keys
DataSource.AddRelativeJars1=Jar の追加 (相対パス)(&S)...

DataSource.AddAbsoluteJars1=Jar の追加 (絶対パス)(&B)...

DataSource.AddRelativeClassFolder1=クラスフォルダの追加 (相対パス)(&E)...

DataSource.AddAbsoluteClassFolder1=クラスフォルダの追加 (絶対パス)(&U)...

DataSource.button.tooltip.AddJar.TwoItems=jar をクラスパスに追加します。\n相対パス: リソースフォルダを基準とした相対パスです。\n絶対パス: ファイルシステムの絶対パスです。

DataSource.button.tooltip.AddJar.OneItems=jar をクラスパスに追加します。\n絶対パス: ファイルシステムの絶対パスです。

DataSource.PojoClassPath=POJO クラスの共有先(&O)

DataSource.MissDataSetPojoClassPath.runtime=カスタム POJO データセットクラスのランタイム共有先が見つかりません。

DataSource.MissDataSetPojoClassPath.designtime=デザイン時に使用されるカスタム POJO データセットクラスの共有先が見つかりません。

DataSource.PageMessage=POJO データソースのプロパティ

DataSource.POJOClasses.tab.runtime=ランタイムプロパティ(&R)

DataSource.POJOClasses.tab.designTime=デザイン時プロパティ(&P)

DataSource.POJOClassTabFolderPage.promptLabel.runtime=カスタム POJO データセットクラスの共有先(&L)

DataSource.POJOClassTabFolderPage.promptLabel.designtime=カスタム POJO クラスの共有先(&L)

DataSource.POJOClassTabFolderPage.button.edit=編集(&E)...

DataSource.POJOClassTabFolderPage.button.up=上へ(&U)

DataSource.POJOClassTabFolderPage.button.down=下へ(&D)

DataSource.POJOClassTabFolderPage.button.remove=削除(&V)

DataSource.POJOClassTabFolderPage.menuButton.addJar=Jar を追加

DataSource.POJOClassTabFolderPage.menuButton.item.relativePath=相対パス

DataSource.POJOClassTabFolderPage.menuButton.item.absolutePath=絶対パス

DataSource.POJOClassTabFolderPage.error.title.empty.resourceIdentifier=エラー

DataSource.POJOClassTabFolderPage.error.message.empty.resourceIdentifier=アプリケーションリソースのベース URI を取得できません。

DataSource.ClassPathPage.synchronize.checkbox.message=実行時とデザイン時の両方でランタイムプロパティを使用する

DataSource.ClassPathPage.synchronize.checkbox.tooltip=このオプションをオンにした場合、Designtime Properties Editor で編集できなくなります。

DataSource.ClassPathPage.synchronize.PrompMessage=注: このオプションをオンにした場合、デザイン時プロパティの設定は無視されます。

DataSource.ClassPathPage.testConnection.failed.runtime=ランタイムプロパティの "{0}" への接続を作成できません。

DataSource.ClassPathPage.testConnection.failed.designtime=デザイン時プロパティの "{0}" への接続を作成できません。


# data set messages
DataSet.PojoDataSetClass=POJO データセットのクラス名(&P)

DataSet.AppContextKey=アプリケーションのコンテキストキー(&A)

DataSet.PojoRootClass=POJO クラス名(&C)

DataSet.Browse=参照(&B)...

DataSet.Browse.tooltip=参照してクラスを選択します。

#To avoid duplicate short-cut key
DataSet.Browse1=参照(&R)...

DataSet.DataType.Object=Java オブジェクト

DataSet.FailedParseQueryTitle=クエリテキストを解析できません

DataSet.FailedParseQueryMsg=クエリテキスト: "{0}"

DataSet.FailedToSaveTitle=データセットを保存できません

DataSet.FailedToSaveMsg=データセットの保存中に例外が発生しました。

DataSet.Label.MethodNameFilter=メソッド名フィルタ (? は任意の 1 文字、* =は任意の文字列を示します)(&M):

DataSet.MissPojoRootClass=POJO クラスが指定されていません。

DataSet.Apply=適用(&Y)

DataSet.ClassStructure=POJO クラスの構造

DataSet.ColumnMapping=列マッピング(&C)

DataSet.Add=追加(&A)...

DataSet.Edit=編集(&E)...

DataSet.Remove=削除(&R)

DataSet.Up=上へ(&U)

DataSet.Down=下へ(&D)

DataSet.Label.POJOClassName=POJO クラス名

DataSet.Label.prompt.ClassName=データセット列を作成する場合は、メソッドとフィールドを選択します。(&F)

DataSet.button.addColumn.tooltip.mapColumn=選択した項目を列に追加します。

DataSet.button.removeColumn.tooltip.mapColumn=選択した項目を列テーブルから削除します。

DataSet.button.tooltip.Add=新しい列を追加します。

DataSet.button.tooltip.Edit=現在の列を編集します。

DataSet.button.tooltip.Remove=現在の列を削除します。

DataSet.button.tooltip.Up=現在の列を上へ移動します。

DataSet.button.tooltip.Down=現在の列を下へ移動します。

DataSet.ShowSampleData=サンプルデータを表示(&S)

DataSet.button.tooltip.ShowSampleData=サンプルデータを表示します。

DataSet.FailedToLoadClassTitle=クラスを読み込めません。

DataSet.FailedToLoadPojoClassMsg=POJO クラスを読み込めません: "{0}"

ColumnMappingPage.table.head.columnName=名前

ColumnMappingPage.table.head.MethodField=メソッド / フィールド

ColumnMappingPage.table.head.columnType=種類

ColumnMappingWizardPage.error.getQueryText=元のクエリテキストを解析できません。

ColumnMappingWizardPage.button.apply.text=適用(&P)

ColumnMappingWizardPage.button.apply.tooltip=入力 POJO クラス名を適用して、メソッドツリーを生成します。

ColumnMappingDialog.Label.ColumnName=列名(&C):

ColumnMappingDialog.Label.ColumnMethodField=メソッド / フィールド(&M):

ColumnMappingDialog.Label.Type=列の種類(&T):

ColumnMappingDialog.table.promptLabel=パラメタにマッピングするメソッド引数を選択します(&A):

ColumnMappingDialog.table.head.mapped=マッピング済み

ColumnMappingDialog.table.head.name=名前

ColumnMappingDialog.table.head.value=値

ColumnMappingDialog.table.head.dataType=Java のデータ型

ColumnMappingDialog.button.edit=編集(&E)...

ColumnMappingDialog.button.selectAll=すべて選択(&S)

ColumnMappingDialog.button.deselectAll=すべて選択解除(&D)

ColumnMappingDialog.Label.methodHierarchy=メソッド呼び出し階層:

ColumnMappingDialog.error.DuplicatedColumnName=この列名はすでに使用されています。

ColumnMappingDialog.error.EmptyColumnName=列名は空白にできません。

ColumnMappingDialog.error.EmptyMappingPath=列マッピングパスは空白にできません。

ColumnMappingDialog.MenuItem.remove=削除

ColumnMappingDialog.MenuItem.removeAll=すべて削除

MethodParameterDialog.edit.title=メソッドパラメタの編集

MethodParameterDialog.label.name=名前(&N):

MethodParameterDialog.label.value=値(&V):

MethodParameterDialog.label.dataType=Java のデータ型(&J):

MethodParameterDialog.checkBox.message=パラメタにマッピングする引数を設定します。

MethodParameterDialog.error.missingName=マッピングされたパラメタには名前が指定されている必要があります。

MethodParameterDialog.error.missingValue=定数パラメタには定数値が指定されている必要があります。

MethodParameterDialog.error.invalidParamName=このパラメタにはすでに別のデータ型が指定されています。

MethodParameterDialog.error.missingDataType=パラメタのデータ型は空白にできません。

DataSet.DataType.String=文字列

DataSet.DataType.Integer=整数
#make the display label consistent with BIRT data type 
DataSet.DataType.Double=浮動

DataSet.DataType.Decimal=10 進数値

DataSet.DataType.Date=日付

DataSet.DataType.Time=時間
#make the display label consistent with BIRT data type 
DataSet.DataType.Timestamp=日付 / 時間

DataSet.DataType.Boolean=ブーリアン

DataSet.DataType.Blob=Blob

DataSet.AddColumnMapping=列マッピングの追加

DataSet.EditColumnMapping=列マッピングの編集

DataSet.InvalidColumnMappingPath=メソッド / フィールドの形式が無効です: "{0}"

DataSet.InvalidMethodName=メソッド名が無効です: "{0}"

DataSet.InvalidFieldName=フィールド名が無効です: "{0}"

DataSet.JarsSelectDlg.Title=JAR/Zip の選択

DataSet.ClassFoldersSelectDlg.Title=クラスフォルダの選択

DataSet.PropertyPageMsg=POJO データセットのプロパティ

DataSet.ColumnMappingPageMsg=列マッピング

DataSet.NoColumnMappingErrorMsg=列マッピングがありません。

DataSet.ClassNameFilter=クラス名フィルタ (? は任意の 1 文字、* =は任意の文字列を示します)(&C):

DataSet.MatchingItems=一致項目(&M):

DataSet.ClassInputDialogTitle=クラスの選択

DataSet.PreviewDialogTitle=サンプルデータ

DataSet.FailedFetchSampleData=サンプルデータをフェッチできません

ExceptionDialog.title.erro=エラー

ExceptionDialog.DataSource.AddDuplicatedJar=同じ JAR を何度も追加できません。

error.PojoDataSet.emptyClassName=クラス名は空白にできません。

JarsSelectionDialog.button.importFile=ファイルのインポート...

JarsSelectionDialog.title.error=エラー

JarsSelectionDialog.title.overwrite=上書き

JarsSelectionDialog.overwrite.msg=この名前のファイルはすでに存在します。上書きしますか ?

JarsSelectionDialog.import.msg=ファイルのインポート中...
