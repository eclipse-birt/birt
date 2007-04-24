###########################################################
# Crosstab name conventions
Element.Crosstab=クロスタブ
Element.CrosstabCell=クロスタブセル
Element.CrosstabAggregationCell=クロスタブ総計セル
Element.AggregationOnRow=行ごとの総計
Element.AggregationOnColumn=列ごとの総計
Element.AggregationHeaderLocation=総計ヘッダの位置
Element.MeasureView=計測ビュー
Element.PageBreakBefore=次の前で改ページ
Element.PageBreakAfter=次の後で改ページ
Element.SortDirection=ソートの方向
Element.SortType=ソート基準
Element.LevelView=レベルビュー
Element.DimensionView=次元ビュー
Element.CrosstabView=クロスタブビュー
Element.Cube=キューブ
Element.Dimension=次元
Element.Level=レベル
Element.Measure=計測
Element.Caption=キャプション
Element.CaptionID=キャプションキー
Element.MeasureDirection=計測の方向
Element.PageLayout=ページ配置
Element.EmptyCellValue=空のセル値
Element.RepeatRowHeader=Repeat Row Header
Element.RepeatColumnHeader=Repeat Column Header
Element.Mirrored=Mirrored

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=重複するキューブの次元要素 {0} が、クロスタブ {1} の次元ビューに適用されています。
Error.CrosstabException.DUPLICATE_MEASURE=重複するキューブの計測要素 {0} が、クロスタブ {1} の計測ビューに適用されています。
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=拡張項目からクロスタブ項目を読み込めませんでした。
CrosstabReportItemQuery.error.query.building=クエリ構築のクロスタブモデルが無効です。
CrosstabReportItemQuery.error.register.empty.cell.value=Error on registering empty cell value.

CrosstabQueryHelper.error.invalid.measure=クロスタブに関連付けられている計測が無効です。
CrosstabQueryHelper.error.invalid.dimension=クロスタブに関連付けられている次元が無効です。
CrosstabQueryHelper.error.invalid.level=クロスタブに関連付けられているレベルが無効です。

BaseCrosstabExecutor.error.process.visibility=可視性の処理中にエラーが発生しました。
BaseCrosstabExecutor.error.process.bookmark=ブックマークの処理中にエラーが発生しました。

CrosstabCellExecutor.error.restor.data.position=データの位置の復元中にエラーが発生しました。

CrosstabGrandTotalRowExecutor.error.generate.child.executor=子の実行ファイルの生成中にエラーが発生しました。
CrosstabGrandTotalRowExecutor.error.check.child.executor=子の実行ファイルのステータスを確認中にエラーが発生しました。

CrosstabGroupExecutor.error.close.executor=グループの実行ファイルを閉じるときにエラーが発生しました。
CrosstabGroupExecutor.error.prepare.group=子グループの準備中にエラーが発生しました。
CrosstabGroupExecutor.error.check.child.executor=子の実行ファイルのステータスを確認中にエラーが発生しました。

CrosstabHeaderRowExecutor.error.check.force.empty=現在のセルの強制空ステータスの確認中にエラーが発生しました。
CrosstabHeaderRowExecutor.error.check.edge.end=現在のセルのエッジ終了のステータスの確認中にエラーが発生しました。
CrosstabHeaderRowExecutor.error.retrieve.child.executor=子の実行ファイルの取得中にエラーが発生しました。

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=子の実行ファイルの生成中にエラーが発生しました。
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=子の実行ファイルのステータスを確認中にエラーが発生しました。

CrosstabMeasureRowExecutor.error.generate.child.executor=子の実行ファイルの生成中にエラーが発生しました。
CrosstabMeasureRowExecutor.error.check.child.executor=子の実行ファイルのステータスを確認中にエラーが発生しました。

CrosstabReportItemExecutor.error.crosstab.loading=拡張項目からクロスタブ項目を読み込めませんでした。
CrosstabReportItemExecutor.error.generate.columns=クロスタブの列の生成中にエラーが発生しました。
CrosstabReportItemExecutor.error.prepare.children=クロスタブの子の実行ファイルの準備中にエラーが発生しました。
CrosstabReportItemExecutor.error.invalid.cube.result=クロスタブのキューブの結果が無効です (NULL)。

CrosstabRowExecutor.error.check.force.empty=現在のセルの強制空ステータスの確認中にエラーが発生しました。
CrosstabRowExecutor.error.check.edge.start=現在のセルのエッジ開始のステータスの確認中にエラーが発生しました。
CrosstabRowExecutor.error.retrieve.child.executor=子の実行ファイルの取得中にエラーが発生しました。

CrosstabSubTotalRowExecutor.error.check.edge.start=現在のセルのエッジ開始のステータスの確認中にエラーが発生しました。
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=子の実行ファイルの取得中にエラーが発生しました。

TableColumnGenerator.error.process.visibility=列の可視性の処理中にエラーが発生しました。

CrosstabReportItemHandle.error.locate.column.cell=カラム上で関連するクロスタブセルを検出中にエラーが発生しました。
CrosstabReportItemHandle.error.locate.row.cell=ロウ上で関連するクロスタブセルを検出中にエラーが発生しました。
CrosstabReportItemHandle.error.set.column.width=クロスタブカラムの幅を設定中にエラーが発生しました。
CrosstabReportItemHandle.error.set.row.height=クロスタブロウの高さを設定中にエラーが発生しました。
CrosstabReportItemHandle.msg.change.measure.direction=Change Measure Direction
CrosstabReportItemHandle.msg.insert.measure=Insert Measure

MeasureViewHandle.info.aggregation.already.exist=The requested aggregation is already existed.
MeasureViewHandle.msg.add.aggregation=Add Aggregation

LevelViewTask.msg.remove.subtotal=Remove Subtotal
LevelViewTask.msg.add.subtotal=Add Subtotal

DimensionViewTask.msg.remove.level=Remove Level
DimensionViewTask.msg.insert.level=Insert Level

CrosstabViewTask.msg.remove.dimension=Remove Dimension
CrosstabViewTask.msg.remove.grandtotal=Remove Grand Total
CrosstabViewTask.msg.add.grandtotal=Add Grand Total

CrosstabReportItemTask.msg.add.grandtotal=Add Grand Total
CrosstabReportItemTask.msg.set.aggregate.function=Set Aggregate Function
CrosstabReportItemTask.msg.pivot.dimension=Pivot Dimension
CrosstabReportItemTask.msg.insert.dimension=Insert Dimension
