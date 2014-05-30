###########################################################
# Crosstab name conventions
Element.Crosstab=クロスタブ

Element.CrosstabCell=クロスタブセル

Element.CrosstabAggregationCell=クロスタブ集計セル

Element.AggregationOnRow=行ごとの集計

Element.AggregationOnColumn=列ごとの集計

Element.AggregationHeaderLocation=集計ヘッダの位置

Element.GrandTotalLocation=総計の場所

Element.MeasureView=メジャービュー

Element.ComputedMeasureView=メジャービュー

Element.PageBreakBefore=要素前

Element.PageBreakAfter=要素後

Element.PageBreakInside=要素内

Element.PageBreakInterval=改ページの間隔

Element.SortDirection=ソート方向

Element.SortType=ソート基準

Element.LevelView=レベルビュー

Element.DisplayField=表示フィールド

Element.DimensionView=ディメンジョンビュー

Element.CrosstabView=クロスタブビュー

Element.Cube=Cube
Element.Dimension=ディメンション

Element.Level=レベル

Element.Measure=メジャー

Element.Caption=キャプション

Element.CaptionID=キャプションキー

Element.Summary=サマリ

Element.MeasureDirection=メジャーの方向

Element.PageLayout=ページ配置

Element.EmptyCellValue=空のセル値

Element.RepeatRowHeader=行のヘッダの繰り返し

Element.RepeatColumnHeader=列のヘッダの繰り返し

Element.Mirrored=ミラー化

Element.MirrorStartingLevel=ミラー化開始レベル

Element.HideMeasureHeader=メジャーのヘッダを隠す

Element.SpanOverOnColumn=列にスパン

Element.SpanOverOnRow=行にスパン

Element.Direction.Horizontal=横

Element.Direction.Vertical=縦

Element.choice.before=要素前

Element.choice.after=要素後

Element.RowPageBreakInterval=行の改ページの間隔

Element.ColumnPageBreakInterval=列の改ページの間隔

Element.TagType=タグの種類

Method.onPrepare=準備時

Method.onCreate=作成時

Method.onRender=レンダリング時

Method.onPageBreak=改ページ

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=名前が重複しているため、キューブのディメンジョンの要素 ''{0}'' をクロスタブに適用できません。

Error.CrosstabException.DUPLICATE_MEASURE=名前が重複しているため、キューブのメジャーの要素 ''{0}'' をクロスタブに適用できません。

Error.CrosstabException.DUPLICATE_LEVEL=名前が重複しているため、キューブのレベルの要素 ''{0}'' をクロスタブに適用できません。

Error.CrosstabException.DIMENSION_NOT_FOUND=クロスタブにディメンジョンビュー ''{0}'' が見つかりません。

Error.CrosstabException.MEASURE_NOT_FOUND=クロスタブにメジャービュー ''{0}'' が見つかりません。

Error.CrosstabException.LEVEL_NOT_FOUND=クロスタブにレベルビュー ''{0}'' が見つかりません。

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=拡張項目からクロスタブ項目を読み込めませんでした。

CrosstabReportItemQuery.error.query.building=クエリ構築のクロスタブモデルが無効です。

CrosstabReportItemQuery.error.register.empty.cell.value=空のセル値の登録中にエラーが発生しました。

CrosstabQueryHelper.error.invalid.measure=クロスタブのメジャー定義が NULL になっているか、関連付けられているキューブに特定のメジャー "{0}" が存在しません。

CrosstabQueryHelper.error.invalid.dimension.row=クロスタブの行領域のディメンジョン定義が NULL になっているか、関連付けられているキューブに特定のディメンジョン "{0}" が存在しません。

CrosstabQueryHelper.error.invalid.level.row=クロスタブの行領域のレベル定義が NULL になっているか、関連付けられているキューブに特定のレベル "{0}" が存在しません。

CrosstabQueryHelper.error.invalid.dimension.column=クロスタブの列領域のディメンジョン定義が NULL になっているか、関連付けられているキューブに特定のディメンジョン "{0}" が存在しません。

CrosstabQueryHelper.error.invalid.level.column=クロスタブの列領域のレベル定義が NULL になっているか、関連付けられているキューブに特定のレベル "{0}" が存在しません。

BaseCrosstabExecutor.error.process.visibility=可視性の処理中にエラーが発生しました。

BaseCrosstabExecutor.error.process.bookmark=ヘッダの処理中にエラーが発生しました。

BaseCrosstabExecutor.error.process.headers=ヘッダの処理中にエラーが発生しました。

BaseCrosstabExecutor.error.process.row.height=行の高さの処理中にエラーが発生しました。

CrosstabCellExecutor.error.restor.data.position=データの位置を復元中にエラーが発生しました。

CrosstabGrandTotalRowExecutor.error.generate.child.executor=子の実行ファイルの生成中にエラーが発生しました。

CrosstabGrandTotalRowExecutor.error.check.child.executor=子の実行ファイルのステータスを確認中にエラーが発生しました。

CrosstabHeaderExecutor.error.reset.row.position=行のエッジのカーソル位置をリセット中にエラーが発生しました。

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

TableColumnGenerator.error.process.column.width=列の幅の処理中にエラーが発生しました。

CrosstabReportItemHandle.error.locate.column.cell=カラム上で関連するクロスタブセルを検出中にエラーが発生しました。

CrosstabReportItemHandle.error.locate.row.cell=ロウ上で関連するクロスタブセルを検出中にエラーが発生しました。

CrosstabReportItemHandle.error.set.column.width=クロスタブカラムの幅を設定中にエラーが発生しました。

CrosstabReportItemHandle.error.set.row.height=クロスタブロウの高さを設定中にエラーが発生しました。

CrosstabReportItemHandle.msg.change.measure.direction=メジャーの方向を変更

CrosstabReportItemHandle.msg.insert.measure=メジャーの挿入

CrosstabReportItemHandle.exception.name.blank=メジャー値の名前を指定してください。

CrosstabReportItemHandle.exception.name.duplicate=指定したメジャー名はすでに使用されており、重複できません。

MeasureViewHandle.info.aggregation.already.exist=指定された集計はすでに存在します。

MeasureViewHandle.msg.add.aggregation=集計の追加

LevelViewTask.msg.remove.subtotal=小計の削除

LevelViewTask.msg.add.subtotal=小計の追加

DimensionViewTask.msg.remove.level=レベルの削除

DimensionViewTask.msg.insert.level=レベルの挿入

MeasureViewTask.msg.remove.header=メジャーのヘッダの削除

MeasureViewTask.msg.add.header=メジャーのヘッダの追加

CrosstabViewTask.msg.add.dimension=ディメンションの追加

CrosstabViewTask.msg.remove.dimension=ディメンジョンの削除

CrosstabViewTask.msg.remove.grandtotal=総計の削除

CrosstabViewTask.msg.add.grandtotal=総計の追加

CrosstabReportItemTask.msg.add.grandtotal=総計の追加

CrosstabReportItemTask.msg.set.aggregate.function=集計関数の設定

CrosstabReportItemTask.msg.pivot.dimension=ディメンジョンの回転

CrosstabReportItemTask.msg.insert.dimension=ディメンジョンの挿入

CrosstabReportItemTask.msg.set.mirroredStartingLevel=ミラー化開始レベルの設定

CrosstabReportItemTask.msg.pivot.crosstab=クロスタブを回転します。

CrosstabReportItemHandle.Error.HasNoCube=クロスタブは、1 つのキューブにアクセスできる必要があります。

CrosstabReportItemHandle.log.validate=検証中 (...)

CrosstabScriptHandler.error.javascript=JavaScript の {1} 行目にある {0} :\r\n{2}\r\n

CrosstabScriptHandler.info.try.register.crosstab.javascript.content=クロスタブ JavaScript のコンテンツを登録しています...

CrosstabScriptHandler.info.crosstab.javascript.content.registered=クロスタブ JavaScript のコンテンツが正しく登録されました。

CrosstabScriptHandler.info.try.load.crosstab.java.handler=クロスタブ Java イベントハンドラを読み込んでいます...

CrosstabScriptHandler.info.crosstab.java.handler.loaded=クロスタブ Java イベントハンドラ [{0}] が正しく読み込まれました。

CrosstabScriptHandler.info.invalid.crosstab.java.handler=Java イベントハンドラ [{0}] の実装の互換性がありません。
