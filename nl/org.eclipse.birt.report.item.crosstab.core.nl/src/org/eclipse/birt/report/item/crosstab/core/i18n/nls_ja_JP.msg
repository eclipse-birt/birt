###########################################################
# Crosstab name conventions
Element.Crosstab=クロスタブ
Element.CrosstabCell=クロスタブセル
Element.CrosstabAggregationCell=クロスタブ集計セル
Element.AggregationOnRow=行ごとの集計
Element.AggregationOnColumn=列ごとの集計
Element.AggregationHeaderLocation=集計ヘッダの位置
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
Element.RepeatRowHeader=行のヘッダの繰り返し
Element.RepeatColumnHeader=列のヘッダの繰り返し
Element.Mirrored=ミラー化
Element.MirroredStartingLevel=Mirrored Starting Level

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=重複するキューブの次元要素 {0} が、クロスタブ {1} の次元ビューに適用されています。
Error.CrosstabException.DUPLICATE_MEASURE=重複するキューブの計測要素 {0} が、クロスタブ {1} の計測ビューに適用されています。
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=拡張項目からクロスタブ項目を読み込めませんでした。
CrosstabReportItemQuery.error.query.building=クエリ構築のクロスタブモデルが無効です。
CrosstabReportItemQuery.error.register.empty.cell.value=空のセル値の登録中にエラーが発生しました。

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
CrosstabReportItemHandle.msg.change.measure.direction=計測の方向を変更
CrosstabReportItemHandle.msg.insert.measure=計測の挿入

MeasureViewHandle.info.aggregation.already.exist=指定された集計はすでに存在します。
MeasureViewHandle.msg.add.aggregation=集計の追加

LevelViewTask.msg.remove.subtotal=小計の削除
LevelViewTask.msg.add.subtotal=小計の追加

DimensionViewTask.msg.remove.level=レベルの削除
DimensionViewTask.msg.insert.level=レベルの挿入

CrosstabViewTask.msg.remove.dimension=次元の削除
CrosstabViewTask.msg.remove.grandtotal=総計の削除
CrosstabViewTask.msg.add.grandtotal=総計の追加

CrosstabReportItemTask.msg.add.grandtotal=総計の追加
CrosstabReportItemTask.msg.set.aggregate.function=集計関数の設定
CrosstabReportItemTask.msg.pivot.dimension=次元の回転
CrosstabReportItemTask.msg.insert.dimension=次元の挿入
CrosstabReportItemTask.msg.set.mirroredStartingLevel=Set Mirrored Starting Level

