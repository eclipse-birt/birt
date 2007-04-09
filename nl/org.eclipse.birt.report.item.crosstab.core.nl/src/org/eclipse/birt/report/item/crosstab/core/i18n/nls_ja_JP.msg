###########################################################
# Crosstab name conventions
Element.Crosstab=クロスタブ
Element.CrosstabCell=クロスタブセル
Element.CrosstabAggregationCell=クロスタブ総計セル
Element.AggregationOnRow=行ごとの総計
Element.AggregationOnColumn=列ごとの総計
Element.AggregationHeaderLocation=Aggregation Header Location
Element.MeasureView=計測ビュー
Element.PageBreakBefore=Page Break Before
Element.PageBreakAfter=Page Break After
Element.SortDirection=Sort Direction
Element.SortType=Sort Type
Element.LevelView=レベルビュー
Element.DimensionView=次元ビュー
Element.CrosstabView=クロスタブビュー
Element.Cube=キューブ
Element.Dimension=Dimension
Element.Level=Level
Element.Measure=Measure
Element.Caption=キャプション
Element.CaptionID=キャプションキー
Element.MeasureDirection=計測の方向
Element.PageLayout=ページ配置
Element.EmptyCellValue=空のセル値

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=重複するキューブの次元要素 {0} が、クロスタブ {1} の次元ビューに適用されています。
Error.CrosstabException.DUPLICATE_MEASURE=重複するキューブの計測要素 {0} が、クロスタブ {1} の計測ビューに適用されています。
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=拡張項目からクロスタブ項目を読み込めませんでした。
CrosstabReportItemQuery.error.query.building=クエリ構築のクロスタブモデルが無効です。

CrosstabQueryHelper.error.invalid.measure=The associated measure for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.dimension=The associated dimension for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.level=The associated level for crosstab is inavlid.

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
CrosstabReportItemExecutor.error.generate.columns=Error happend when generate columns for crosstab.
CrosstabReportItemExecutor.error.prepare.children=Error happend when prepare child executor for crosstab.
CrosstabReportItemExecutor.error.invalid.cube.result=The cube result for crosstab is invalid(=null).

CrosstabRowExecutor.error.check.force.empty=現在のセルの強制空ステータスの確認中にエラーが発生しました。
CrosstabRowExecutor.error.check.edge.start=現在のセルのエッジ開始のステータスの確認中にエラーが発生しました。
CrosstabRowExecutor.error.retrieve.child.executor=子の実行ファイルの取得中にエラーが発生しました。

CrosstabSubTotalRowExecutor.error.check.edge.start=現在のセルのエッジ開始のステータスの確認中にエラーが発生しました。
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=子の実行ファイルの取得中にエラーが発生しました。

TableColumnGenerator.error.process.visibility=列の可視性の処理中にエラーが発生しました。

CrosstabReportItemHandle.error.locate.column.cell=Error locating relevant crosstab cell on columns
CrosstabReportItemHandle.error.locate.row.cell=Error locating relevant crosstab cell on rows
CrosstabReportItemHandle.error.set.column.width=Error setting crosstab column width
CrosstabReportItemHandle.error.set.row.height=Error setting crosstab row height
