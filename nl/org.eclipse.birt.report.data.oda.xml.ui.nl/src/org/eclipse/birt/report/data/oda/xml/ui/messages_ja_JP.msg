# wizard dialog messages
wizard.title.selectFolder=XML データソース
wizard.defaultMessage.selectFolder=XML ファイルとスキーマ情報に URL を定義してください。
wizard.title.newDataSet=新規データセット
wizard.title.selectXmlFile=プレビューする XML データの選択
wizard.defaultMessage.selectFile=このセッションでデータをプレビューするには、サンプルの XML ソースを選択してください。
wizard.title.xPathChoose=テーブルマッピングの定義
wizard.defaultMessage.selectXPath=テーブルとして使用される要素を定義するには、XML 構造から要素を選択して [行マッピング] に追加してください。テーブルのマッピングは、XML データセットあたり 1 個だけ含めることができます。
wizard.title.selectColumn=列マッピングの定義
wizard.title.defineColumnMapping=列マッピングを定義するには、XML 構造から要素または属性を選択して [列マッピング] に追加してください。 

#preference
designer.preview.preference.numberOfElementPassedToGetSchema=XML スキーマツリーの解析中にアクセスした要素の数
designer.preview.preference.numberOfElementPassedToGetSchema.errormessage=0～{0} までの数値を入力してください。
designer.preview.preference.columnMappingPage.group.title=カラムマッピングページ

#table mapping page messages
xPathChoosePage.messages.rowMapping=テーブルのマッピング
xPathChoosePage.messages.xPathExpression=XPath 式(&X)
xPathChoosePage.messages.xmlStructure=XML 構造
xPathChoosePage.messages.xmlColumnMapping=列マッピング

#sample xml data file page messages
xmlDataSetSelectionPage.messages.lineofdata=ソースデータの行
xmlDataSetSelectionPage.warning.errorReadXMLFile=ファイル [{0}] {1} から XML データを読み込めません。\n続行しますか ?

#column mapping page messages
button.refresh=更新
menu.menuItem.removeAll=すべて削除
menu.menuItem.remove=削除
lable.selectXmlFile=XML ソースの URL を入力するか、データが入っているファイルを参照してください(&X) :
lable.selectXmlSchmaFile=XML スキーマの URL を入力するか、スキーマが入っている\nファイルを参照してください。スキーマがない場合は、このフィールドを空白のままにしておいてください(&M) :
label.preview=最初にプレビュー(&P)
label.useXMLFileFromDataSource=データソースに定義されている XML ファイルを使用する
RowMappingDialog.title=XPath 式の選択・編集
RowMappingDialog.info=選択したノードに適用する定義済みの XPath 式を\n選択するか、別の式を入力してください :
ColumnMappingDialog.info.columnName=列名 :
ColumnMappingDialog.info.xPath=XPath 式 :
ColumnMappingDialog.info.dataType=データ型 :
ColumnMappingDialog.info.dataPreview=テーブルのプレビュー
ColumnMappingDialog.prompt.new=ここに新しいマッピングを入力してください...
ColumnMappingDialog.prompt.maxRow=500 行以上のソースデータがあります。このページには最初の 500 行だけが表示されます。
ColumnMappingDialog.prompt.dialog.title=結果のプレビュー

dataset.editor.columnName=名前
dataset.editor.xpathexpression=XPath
dataset.editor.datatype=型
dataset.editor.rowelement=行要素
dataset.error.populateXMLTree=XML データソースを取得できません。XML データソースファイルが無効か、存在しません。

error.label=エラー
error.selectFolder=選択したファイルの URL は無効か、ファイルが存在しません。
error.emptyPath=ファイルの URL が空白です。
error.xpath.canNotContainAttribute=XPath 式は XML 属性にマッピングできません。
error.dataset.maxRowNumberError=最大行数には、数値を指定してください。
error.columnMapping.noMappingColumn=少なくとも 1 つの列をマッピングしてください。
error.file.null=データソースが選択されていないため、URL がすべて空白になっています。このまま続行しますか ?
error.columnMapping.sameColumnName=列名 [{0}] はすでに使用されています。別の名前を使用してください。
error.invalidXpath=XPath 式 "{0}" が無効です。

#data types for column mapping
datatypes.dateTime=タイムスタンプ
datatypes.decimal=BigDecimal
datatypes.float=浮動小数値
datatypes.integer=整数値
datatypes.string=文字列
datatypes.date=日付
datatypes.time=時間

#column name in column mapping table
columnName.name=名前
columnName.xPathName=XPath
columnName.typeName=型

dataset.new=新規データセット
file.choose=参照(&B)...
title.warning=警告

datasource.propertyPage.tooltip=XML データソース
