###########################################################
# Crosstab Action mame and trans name
AddLevelHandleAction_Displayname=グループレベルの表示 / 非表示(&G)
AddLevelHandleAction_TransName=グループレベルの表示/非表示
AddMesureViewHandleAction.DisplayName=メジャーの表示 / 非表示(&M)
AddMesureViewHandleAction.TransName=メジャーの表示/非表示
AddSubTotalAction.LabelName=総計
AddSubTotalAction.TransName=合計の編集
AddSubTotalAction.DisplayName=合計(&L)
AddSubTotalAction.TotalName=合計
CrosstabMenuBuilder.EditCubeAction.DisplayName=データキューブの編集(&E)
DeleteDimensionViewHandleAction.TransName=ディメンジョンの削除
DeleteDimensionViewHandleAction.DisplayName=削除(&V)
DeleteMeasureHandleAction.DisplayName=削除(&V)
DeleteMeasureHandleAction.TransName=メジャーの削除

AddComputedMesureHandleAction.DisplayName=派生メジャーの追加(&S)
AddComputedMesureHandleAction.TransName=派生メジャーの追加
AddComputedSummaryDialog.Title=派生メジャー
AddComputedSummaryDialog.Label.Name=名前(&N) :
AddComputedSummaryDialog.Label.Expression=式(&E) :
AddComputedSummaryDialog.Label.DataType=データ型(&D) :
AddComputedSummaryDialog.ErrMsg.Msg1=メジャー名は空白にできません。
AddComputedSummaryDialog.ErrMsg.Msg2=このメジャー名はすでに使用されています。
AddComputedSummaryDialog.ErrMsg.Msg3=式は空白にできません。

ShowAsViewAction.DisplayName=表示形式
ShowAsViewAction.TransName=表示形式

MeasureComboPropertyDescriptorProvider.TransName=クロスタブの向きを変更する

###########################################################
# Crosstab Command trans name
AddDimensionViewHandleCommand.TransName=ディメンジョンの追加
AddMeasureViewHandleCommand.TransName=メジャーの追加
ChangeAreaCommand.TransName=ディメンジョンの削除
ChangeMeasureOrderCommand.TransName=メジャー順の変更
CreateDimensionViewCommand.TransName=ディメンジョンの作成
CreateMeasureViewCommand.TransName=メジャーの作成
###########################################################
# Crosstab EditPart
###########################################################
# Crosstab Command trans name
CrosstabTableEditPart.GuideText=クロスタブ
###########################################################
# Crosstab Dialog trans name
GrandTotalProvider.Column.AggregateOn=総計条件
GrandTotalProvider.Column.DataField=データフィールド
GrandTotalProvider.Column.View=ビュー
GrandTotalProvider.Column.Function=関数
GrandTotalProvider.ViewStatus=現在のビューの保存
GrandTotalProvider.ShowAs={0} として表示
SubTotalProvider.Column.DataField=データフィールド
SubTotalProvider.Column.Function=関数
SubTotalProvider.Column.AggregateOn=集計条件
SubTotalProvider.Column.ViewStatus=現在のビューの保存
SubTotalProvider.Column.View=ビュー
SubTotalProvider.Column.Position=位置
SubTotalProvider.ShowAs={0} として表示
LevelViewDialog.Title=グループレベルの表示/非表示
LevelViewDialog.Label.Info=クロスタブに表示するレベルの選択 :
AggregationDialog.Title=合計
AggregationDialog.Label.Grand=総計 :
AggregationDialog.Label.Sub=小計 :
AggregationDialog.TabItem.Title.RowArea=行領域
AggregationDialog.TabItem.Title.ColumnArea=列領域
ShowSummaryFieldDialog.Title=メジャーの表示/非表示
ShowSummaryFieldDialog.Label.Info=クロスタブに表示するメジャーの選択 :
ShowSummaryFieldDialog.Column.Measures=メジャー
ShowSummaryFieldDialog.Column.View=ビュー
ShowSummaryFieldDialog.ViewStatus=現在のビューの保存


DeleteBindingDialog.Title=未使用のバインドの削除
DeleteBindingDialog.Message=この操作により、未使用のデータバインドが削除されます。\n\n未使用のバインドを削除しますか ?
DeleteBindingDialog.ToggleMessage=今後、このメッセージを表示しない
###########################################################
# Crosstab Outline Node
AreaNodeProvider.ColumnArea=列領域
AreaNodeProvider.RowArea=行領域
AreaNodeProvider.DetailArea=詳細の領域
CrossTabViewNodeProvider.ColumnArea=列領域
CrossTabViewNodeProvider.RowArea=行領域
CrossTabCellNodeProvider.Cell=セル
CrossTabLevelNodeProvider.Level=レベル - 
CrossTabMeasureNodeProvider.Detail=詳細 - 
MeasureSubNodeProvider.Header=ヘッダ
MeasureSubNodeProvider.Detail=詳細
MeasureSubNodeProvider.Aggregation=集計

CrosstabSortingModelProvider.ColumnName.GroupLevel=グループレベル
SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=エラー
SortkeyBuilder.DialogTitle.Label.Key=&Key:
CrosstabFilterModelProvider.ColumnName.GroupLevelOrMeasure=ターゲット
CrosstabSortkeyBuilder.DialogTitle.Label.GroupLevel=Group &Level:
SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=式ビルダを起動するには、ここをクリックしてしてください
SortkeyBuilder.DialogTitle.Label.Direction=&Direction:

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=全般
CrosstabPageGenerator.List.Margin=余白
CrosstabPageGenerator.List.Font=フォント
CrosstabPageGenerator.List.Borders=罫線
CrosstabPageGenerator.List.Section=ページ区切り
CrosstabPageGenerator.List.Visibility=表示/非表示
CrosstabPageGenerator.List.TOC=目次
CrosstabPageGenerator.List.Bookmark=ブックマーク
CrosstabPageGenerator.List.NamedExpressions=名前付き式
CrosstabPageGenerator.List.UserProperties=ユーザのプロパティ
CrosstabPageGenerator.List.AdvancedProperty=詳細設定

CrosstabPageGenerator.List.EmptyRowColumn=空の行と列

CrosstabPageGenerator.TabItem.Binding=バインド
CrosstabPageGenerator.TabItem.RowArea=行領域
CrosstabPageGenerator.TabItem.ColumnArea=列領域
CrosstabPageGenerator.TabItem.DetailArea=詳細の領域
CrosstabPageGenerator.TabItem.Filters=フィルタ
CrosstabPageGenerator.TabItem.Highlights=ハイライト
CrosstabPageGenerator.TabItem.map=マップ
CrosstabPageGenerator.TabItem.Sorting=ソート

CrosstabGeneralPage.ForEmptyCell=空のセルに次を表示(&S) :
LayoutMeasuresSection.DisplayName=向き :
CrosstabGeneralPage.HideMeasureHeader=メジャーのヘッダを隠す


parameterBinding.title=データセットパラメタのバインド...

###########################################################
#CrosstabEventHandlerPage properties
CrosstabEventHandlerPage.dialog.Browse=参照(&B)...

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=ソート :
Element.ReportItem.dataSet=データセット :
Element.ReportElement.Cube=データ(&D) :
BindingPage.None=なし

FilterHandleProvider.Label.Filterby=フィルタの条件 :

###########################################################
#EmptyRowColumnProvider properties
EmptyRowColumnPage.Label.Text=クロスタブレベルで空の行と列を表示するには、このチェックボックスをオンにします。
EmptyRowColumnProvider.RowView.Button.Text=空の行を表示(&R)
EmptyRowColumnProvider.ColumnView.Button.Text=空の列を表示(&C)

###########################################################
#CategoryPageGenerator properties

CategoryPageGenerator.TabItem.Attributes=プロパティ


###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=フィルタの条件の新規作成
FilterConditionBuilder.DialogTitle.Edit=フィルタの条件の編集
FilterConditionBuilder.choice.ValueOfThisDataItem=このデータ項目の値
FilterConditionBuilder.tooltip.ExpBuilder=式ビルダを起動するには、ここをクリックしてしてください
FilterConditionBuilder.text.AND= ～
FilterConditionBuilder.text.Condition=Filter &Condition:
FilterConditionBuilder.text.GroupLevel=Group &Level:
FilterConditionBuilder.list.item1=値
FilterConditionBuilder.button.edit=編集...
FilterConditionBuilder.button.delete=削除
FilterConditionBuilder.button.deleteall=すべて削除
FilterConditionBuilder.button.add=追加
FilterConditionBuilder.button.edit.tooltip=値リストで選択した値を編集します。
FilterConditionBuilder.button.delete.tooltip=リストから選択した値を削除します。
FilterConditionBuilder.button.deleteall.tooltip=リストからすべての値を削除します。
FilterConditionBuilder.button.add.tooltip=値リストに値を追加します。
CrosstabFilterConditionBuilder.DialogMessage.New=フィルタを適用するグループレベルを選択してから、フィルタの条件を設定してください。
CrosstabFilterConditionBuilder.DialogMessage.Edit=フィルタを適用するグループレベルを選択してから、フィルタの条件を設定してください。
CrosstabFilterConditionBuilder.label.value=値
CrosstabFilterConditionBuilder.Label.Target=ターゲット : 
CrosstabFilterConditionBuilder.Button.GroupLevel=&Group Level
CrosstabFilterConditionBuilder.Button.Measure=&Measure

FilterbyTree.Bindings.Catogory.Measures=メジャー
FilterbyTree.Bindings.Catogory.Dimension=ディメンジョン
FilterbyTree.Bindings.Catogory.GrandTotal=総計
FilterbyTree.Bindings.Catogory.SubTotal=小計
FilterbyTree.Bindings.Catogory.OtherType=その他の種類
FilterbyTree.Bindings.Catogory.Undefined=未定義
FilterbyTree.Title=フィルタの条件
FilterbyTree.Message=フィルタの条件

###########################################################
#VirtualCellEditPart.java
Blank.text.column=列を定義するためにデータフィールドをここにドロップ
Blank.text.row=行を定義するために\nデータフィールドをここにドロップ
Blank.text.measure=サマリを定義するためにデータフィールドをここにドロップ

###########################################################
#CrosstabGrandTotalDialog
CrosstabGrandTotalDialog.Title=総計
CrosstabGrandTotalDialog.Text.DataField=データフィールド :
CrosstabGrandTotalDialog.Text.Function=関数 :


###########################################################
# Crosstab RowArea PageBreak
CrosstabPageBreakProvider.Column.GroupLevel=グループレベル
CrosstabPageBreakProvider.Column.Before=次の前で改ページ
CrosstabPageBreakProvider.Column.After=次の後で改ページ
CrosstabPageBreakProvider.Column.Interval=間隔

###########################################################
# Crosstab RowArea SubTotal
CrosstabSubToatalProvider.Column.AggregateOn=総計条件
CrosstabSubToatalProvider.Column.DataField=データフィールド
CrosstabSubToatalProvider.Column.Function=関数

###########################################################
#CrosstabPageBreakDialog
CrosstabPageBreakDialog.Title=ページ区切り
CrosstabPageBreakDialog.Text.GroupLevel=グループレベル :
CrosstabPageBreakDialog.Text.PageBreakBefore=次の前で改ページ :
CrosstabPageBreakDialog.Text.PageBreakAfter=次の後で改ページ :
CrosstabPageBreakDialog.Text.PageBreakInterval=改ページの間隔 :

###########################################################
#SubTotalDialog
SubTotalDialog.Title=小計
SubTotalDialog.Text.AggregateOn=総計条件 :
SubTotalDialog.Text.Function=関数 :
SubTotalDialog.Text.DataField=データフィールド :

FormPage.Menu.ModifyProperty=変更

CrosstabPageGenerator.List.SubTotals=小計
CrosstabPageGenerator.List.GrandTotals=総計
CrosstabPageGenerator.List.PageBreak=ページ区切り

PageLayoutPropertyDescriptorProvider.PageLayout=ページ配置 :
RowPageBreak.RepeatColumnHeader=Repeat &Column Header
ColumnPageBreak.RepeatRowHeader=Re&peat Row Header

###########################################################
#ExpressionValueCellEditor properties

ExpressionValueCellEditor.buildExpressionAction=<式の構築...>
ExpressionValueCellEditor.selectValueAction=<値の選択...>
ExpressionValueCellEditor.filterBy=<フィルタの条件...>
ExpressionValueCellEditor.title=値の選択
CrosstabSortKeyBuilder.Label.SelColumnMemberValue=列のメンバ値の選択
CrosstabSortKeyBuilder.Label.SelRowMemberValue=行のメンバ値の選択
CrosstabFilterConditionBuilder.Label.SelColumnMemberValue=列のメンバ値の選択
CrosstabFilterConditionBuilder.Label.SelRowMemberValue=行のメンバ値の選択
CrosstabFilterConditionBuilder.DialogTitle.Label.GroupLevel=Group &Level:
CrosstabFilterConditionBuilder.DialogTitle.Label.Measure=Mea&sure:
SelColumnMemberValue.Column.Level=レベル
SelColumnMemberValue.Column.Value=値
ChoiceSetFactory.choice.None=なし

###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=リストを検索する際にエラーが発生しました。
SelectValueDialog.messages.error.selectVauleUnavailable=式の値を取得できません。
SelectValueDialog.noExpressionSet=式が設定されていません。
SelectValueDialog.retrieving=リストの取得中...
SelectValueDialog.selectValue=値の選択
SelectValueDialog.messages.info.selectVauleUnavailable=値リストがありません。

CrosstabPreferencePage.Title=クロスタブ
CrosstabPreferencePage.filterLimit=フィルタの制限
CrosstabPreferencePage.filterLimit.prompt=表示する値の最大数(&R) : 
CrosstabPreferencePage.Error.MaxRowInvalid=数値が無効です。
CrosstabPreferencePage.autoDelBindings.Text=クロスタブ構造の変更後に未使用のバインドを削除する
CrosstabPreferencePage.autoDelBindings.Text.Prompt=確認メッセージを表示する(&P) 
CrosstabPreferencePage.autoDelBindings.Text.Always=常に続行する(&L) 
CrosstabPreferencePage.autoDelBindings.Text.Never=続行しない(&N) 

CrosstabPreferencePage.promptGroup=プロンプト
CrosstabPreferencePage.cubePopup.Text=フィールドをクロスタブにドロップし、キューブを作成した後でヒントを表示する(&H)

###########################################################
#Crosstab DataBinding properties
CrosstabDataBinding.title.ChangeCube=キューブの変更
CrosstabDataBinding.message.changeCube=クロスタブのデータバインドを変更しようとしています。\n\n既存のデータ列のバインドをすべて削除しますか ?

###########################################################
#Crosstab DropAdapter properties
DataColumnXTabDropAdapter.DataCube=データキューブ

CubeBuilder.warning.title=クロスタブ
CubeBuilder.warning.message=データキューブが正しく作成されました。\nこのデータキューブのフィールドをクロスタブのコンテナにドラッグして、クロスタブの作成を完了してください。
CubeBuilder.warning.prompt=今後、このメッセージを表示しない

###########################################################
#Crosstab drag tracker
CrosstabColumnDragTracker.ResizeColumn=列幅の変更
CrosstabColumnDragTracker.Show.Label=幅 :

CrosstabRowDragTracker.Show.Label=高さ :

###########################################################
#AggregationCellTextViewProvider
AggregationCellTextViewProvider.displayName=テキスト


###########################################################
#CrosstabSortKeyBuilder properties
CrosstabSortKeyBuilder.choice.ValueOfThisDataItem=このデータ項目の値
CrosstabSortKeyBuilder.tooltip.ExpBuilder=式ビルダを開く
CrosstabSortKeyBuilder.MSG.CreateNew=新規作成...


InsertCubeInLayoutAction.menu.text=編集(&E)
InsertCubeInLayoutAction.action.text=レイアウトに含める(&N)
InsertCubeInLayoutAction.action.message=キューブからクロスタブを作成

MapRuleBuilderDialog.MsgDlg.Title=警告
MapRuleBuilderDialog.MsgDlg.Msg=値は空にできません。

LevelHandleDropAdapter.ActionText=ディメンジョンの追加


###########################################################
#CrosstabHighlightRuleBuilder properties
CrosstabHighlightRuleBuilderDialog.text.Description.Element.Crosstab=クロスタブ
CrosstabHighlightRuleBuilderDialog.text.Description.Element.Crosstabcell=クロスタブセル
CrosstabHighlightRuleBuilderDialog.text.Description.Element.ReportElement=レポート要素
CrosstabHighlightRuleBuilderDialog.text.Description={0} に条件付き書式を適用します。条件式と書式を指定してください。\n条件式が True と評価されると、書式が適用されます。
