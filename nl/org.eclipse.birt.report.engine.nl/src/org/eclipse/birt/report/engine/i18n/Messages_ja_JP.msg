# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: がある {0} で {1}.

###########################################################
Error.OutputFormatNotSupported=出力形式 {0} はサポートされていません。

Error.NullOutputFormat=レンダリングオプションの OutputFormat が null です。

Error.InvalidParameter=いくつかの必須パラメタの値が設定されていないか、互換性のないデータ型に設定されています。

Error.InvalidGroupParameter=レポートエンジンでは、パラメタグループ "{0}" を評価できません。

Error.ParameterTypeIsInvalid=パラメタの型 "{0}" は、"{2}" ではなく "{1}" である必要があります。

Error.ParamValidationFailure=パラメタ "{0}" を検証できません。

Error.ParameterValueIsNull=必須パラメタ {0} が設定されていません。

Error.ParameterValueIsBlank=必須パラメタ {0} が空白のままです。

Error.ParamScriptValidationError=パラメタ "{0}" のスクリプトソースの検証エラーが発生しました。

Error.ParameterGroupIsNotFoundByGroupname="{0}" という名前のパラメタグループが見つかりません。

Error.ParameterInvalidGroupLevel=グループ "{0}" のレベル数が指定した数と一致しません。

Error.InvalidDesignFile=デザインファイル {0} にエラーがあるため、実行できません。

Error.DesignFileNotFound=デザインファイル {0} が見つかりません。

Error.CannotCreateExtensionInstance=レポートエンジンで {0} エミッターを初期化できません。このエミッターに必要なライブラリがインストールされていることを確認してください。

Error.MissingComputedColumnExpression=計算列 {0} の式が見つかりません。

Error.ParameterInGroupIsnotScalar=パラメタグループ "{0}" のパラメタはスカラーパラメタでなければなりません。

Error.ParameterEvaluationError=パラメタグループ "{0}" を評価できません。

Error.InvalidEmitterID = レンダリングオプションの EmitterID {0} が無効です。

###########################################################
Error.ErrorsOnPage=レポートの生成中にエラーが発生しました :

Error.Error=エラー

Error.ScriptFileLoadError=スクリプトファイル {0} を読み込めません。

Error.ScriptEvaluationError=スクリプト "{0}" の評価中にエラーが発生しました。\n{1}

Error.TextualItemProcessingError=テキスト項目 {0} の処理中にエラーが発生しました。

Error.TableItemProcessingError=テーブル項目 {0} の処理中にエラーが発生しました。

Error.ExtendedItemGenerationError=拡張項目 {0} の生成中にエラーが発生しました。

Error.ExtendedItemRenderingError=拡張項目 {1} のレンダリング中 {0} にエラーが発生しました。

Error.GridItemProcessingError=グリッド項目 {0} の処理中にエラーが発生しました。

Error.ImageItemProcessingError=イメージ {0} の処理中にエラーが発生しました。

Error.MissingImageFileError=イメージファイルが見つかりません。

Error.InvalidImageSourceError=イメージソースの型が無効です。

Error.InvalidDatabaseImageError=データベースイメージが無効です。イメージの式は "{0}" です。

Error.EmbeddedExpressionError=埋め込まれた式 "{0}" の評価中にエラーが発生しました。

Error.EmbeddedImageError=埋め込まれたイメージの処理中にエラーが発生しました。

Error.HTMLImageError=HTML の <img> タグで埋め込まれたイメージの処理中にエラーが発生しました。

Error.UndefinedDatasetError=データセット "{0}" がレポートで定義されていません。

Error.InvalidExpressionError=式 "{0}" が無効です。

Error.ListProcessingError=リスト項目 {0} の処理中にエラーが発生しました。

Error.ErrorOnReportPage=次の項目にエラーがあります :

Error.ReportErrorMessage = {0} {1}:

Error.ReportErrorMessageWithID = {0} (id = {1}) :

Error.ReportErrorID = {0} ({1} 回)

Error.ReportErrorDetail = 詳細 :

Error.JarNotFoundError = Jar {0} が見つかりません。

Error.ScriptClassCastError = クラス {0} には {1} インターフェイスが実装されていません。

Error.ScriptClassNotFoundError = クラス {0} が見つかりません。

Error.ScriptClassIllegalAccessError = クラス {0} を読み込み中に IllegalAccessException が発生しました。

Error.ScriptClassInstantiationError = クラス {0} をインスタンス化できませんでした。インターフェイスまたは抽象クラスでないことを確認してください。

Error.UnhandledScriptError = スクリプトを実行中に未処理の例外が発生しました。

###########################################################
Error.PageNotFoundError = ファイル {0} が見つかりません。

Error.PageNumberRangeError = ページの範囲 {0} が無効です。

Error.PageHintLoadingError = ページのヒントを読み込めません。

Error.PagesLoadingError = ページ {0} を読み込めません。

Error.InvalidGroupError = グループ {0} が無効です。

Error.InvalidExtensionError = 拡張 ID {0} と形式 {1} が無効です。

Error.InvalidColumnIndexError = 列インデックスが無効です。

Error.InvalidInstanceIDError = インスタンス ID {0} が無効です。

Error.InvalidBookmarkError = ブックマーク {0} が無効です。

Error.ExpressionEvaluationError = 設定式 {0} では有効な論理値を評価できません。

Error.BookmarkNotFoundError = ブックマークが見つかりません : {0}

Error.DateError = 日付 : {0}

Error.CubePositionError = キューブの位置を取得中にエラーが発生しました。

Error.SubqueryCreateError = 別のサブクエリ/クエリの中にはサブクエリを作成できません。

Error.BindDataRetrivingError = バインドデータの取得中にエラーが発生しました。

Error.DataSourceError = データセットのデータソースは null にできません。

Error.UnsupportedQueryDefinitionError = クエリ定義 {0} が無効です。

Error.ReportQueryLoadingError = レポートクエリの読み込み中にエラーが発生しました。レポートを生成するためのデータを取得できません。

Error.ReportQueryLoadingError2 = レポートクエリ {0} を読み込めません。 レポート要素 (ID : {1}) 用のレポート文書の生成中にエラーが発生しました。

Error.ExtentionIDMissingError = データソース {0} に拡張 ID が定義されていません。

Error.DataExportionError = インスタンス ID {0} のデータをエクスポートできません。

Error.UnsupportedDocumentVersionError = 文書のバージョン {0} がサポートされていません。

Error.ResultsetIteratorError = 結果セットの反復子は、有効な行に移動してください。

Error.ReportDesignNotFoundError = レポート文書 {0} にレポートデザインが見つかりません。

Error.PreparedQueryNotFoundError = 記述済みクエリ {0} が見つかりません。

Error.IncorrectParentResultSetError = subQuery {0} の親の resultSet が正しくありません。

Error.UnsupportedQueryTypeError = クエリの種類 {0} が無効です。

Error.QueryNotBuiltError = 要素 : {0}でクエリの構築に失敗しました。

Error.RenderTaskNotFinishedError = レンダリングタスクが完了していません。

Error.RenderOptionError = レポートをレンダリングするには、レンダリングオプションを指定する必要があります。

Error.ReportArchiveError = レポート実行時のレポートアーカイブが指定されていません。

Error.ReportDocNameNotSpecifiedError = レポート実行時のレポート文書名が指定されていません。

Error.ReportRunError = レポートの実行中にエラーが発生しました。

Error.ReportDocmentOpenError = レポート文書を開けません。

Error.ReportArchiveOpenError = レポートアーカイブを開けません。

Error.SkipError = 省略中にエラーが発生しました。

Error.ResultsetExtractError = 結果セットが見つかりません。

Error.FailedToInitializeEmitter = エミッターを初期化できませんでした。

###########################################################
PDFCreator = BIRT レポートエンジン {0} (iText {1} を使用)

###########################################################
Error.FlashObjectNotSupported = Flash オブジェクトのレポート項目は、このレポート形式ではサポートされていません。

Error.ReportItemNotSupported = 現在のレポート項目は、このレポート形式ではサポートされていません。

Error.ResourceUnreachable = このレポート項目のリソースにアクセスできません。

Error.UpdateUserAgent = Flash サポートを有効にするか、新しいバージョンの Adobe Reader をダウンロードして PDF ファイル内の Flash コンテンツを表示してください。

###########################################################
Error.ReportRunnableNotSet = 実行可能なレポートが設定されていません。

Error.FailedToLoadTOCTree = TOC ツリーを読み込めません。

Error.ExceptionOnFetchingBookmarks = ブックマークのフェッチ中に例外が発生しました。

Error.FailedToSaveDesign = デザインを保存できません。

Error.ExceptionOnCopyingArchives = アーカイブのコピー中に例外が発生しました。

Error.UnsupportedCoreStreamVersion = コアストリームバージョン {0} はサポートされていません。

Error.UnsupportedEngineExtension = エンジン拡張 {0} はサポートされていません。

Error.FailToCreateDataEngine = データエンジンを作成できません。

Error.FailedToCreateTOC = TOC を作成できません。

Error.UnsupportedExprssionType = この式の型はサポートされていません。

Error.UnknownContentVersion = コンテンツのバージョンが不明です。

Error.UnknownFieldID = フィールド ID {0} は不明です。

Error.ResourceNotAccessible = {0} にアクセスできません。

Error.ERROR_WITH_STATUS=ドキュメント {0} でエラーが発生しました。

###########################################################
Error.ElementID = 要素 ID

###########################################################
Message.TaskCancel = タスクがキャンセルされました。