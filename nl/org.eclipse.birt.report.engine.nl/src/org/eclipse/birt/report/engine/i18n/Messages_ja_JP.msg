# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=ja_JP: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=出力形式 {0} はサポートされていません。
Error.InvalidParameter=いくつかの必須パラメタの値が設定されていないか、互換性のないデータ型に設定されています。
Error.InvalidGroupParameter=レポートエンジンでは、パラメタグループ "{0}" を評価できません。
Error.ParameterTypeIsInvalid=パラメタの型 "{0}" は、"{2}" ではなく "{1}" である必要があります。
Error.ParameterValueIsNull=必須パラメタ {0} が設定されていません。
Error.ParameterValueIsBlank=必須パラメタ {0} が空白のままです。
Error.ParamScriptValidationError=パラメタ "{0}" のスクリプトソースの検証エラーが発生しました。
Error.ParameterGroupIsNotFoundByGroupname="{0}" という名前のパラメタグループが見つかりません。
Error.ParameterInvalidGroupLevel=The level count of group "{0}" does not match the specified one.
Error.InvalidDesignFile=デザインファイル {0} にエラーがあるため、実行できません。
Error.DesignFileNotFound=デザインファイル {0} が見つかりません。
Error.CannotCreateExtensionInstance=レポートエンジンで {0} エミッターを初期化できません。このエミッターに必要なライブラリがインストールされていることを確認してください。
Error.MissingComputedColumnExpression=計算列 {0} の式が見つかりません。
Error.ParameterInGroupIsnotScalar=パラメタグループ "{0}" のパラメタはスカラーパラメタでなければなりません。
Error.ParameterEvaluationError=パラメタグループ "{0}" を評価できません。

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

Error.ScriptClassCastError = Class {0} does not implement the {1} interface.
Error.ScriptClassNotFoundError = Class {0} not found.
Error.ScriptClassIllegalAccessError = IllegalAccessException when trying to load class {0}.
Error.ScriptClassInstantiationError = クラス {0} をインスタンス化できませんでした。インターフェイスまたは抽象クラスでないことを確認してください。
Error.UnhandledScriptError = Unhandled exception when executing script.

###########################################################
Error.PageNotFoundError = Page {0} is not found.
Error.PageNumberRangeError = Invalid page range: {0}.
Error.PageHintLoadingError = Can''t load the page hint.
Error.PagesLoadingError = Can''t load the pages {0}.

Error.InvalidGroupError = Invalid group : {0}.
Error.InvalidExtensionError = Invalid extension id and format.
Error.InvalidColumnIndexError = Invalid column index.
Error.InvalidInstanceIDError = Invalid instance id : {0}.
Error.InvalidBookmarkError = Invalid bookmark : {0}.

Error.ExpressionEvaluationError = The following visibility expression does not evaluate to a legal boolean value: {0}.
Error.BookmarkNotFoundError = Can not find bookmark :{0}.
Error.DateError = date:{0}.
Error.CubePositionError = Get cube position error.
Error.SubqueryCreateError = Sub-query can only be created in another sub-query/query.
Error.BindDataRetrivingError = Retrieving binding data error.
Error.DataSourceError = Data source of data set can not be null.
Error.UnsupportedQueryDefinitionError = Unsupported query definition {0}.
Error.ReportQueryLoadingError = Can''t load the report query: {0}.
Error.ExtentionIDMissingError = Missing extension id in data source definition {0}.
Error.DataExportionError = Failed to export data by instance id :{0}.
Error.UnsupportedDocumentVersionError = Unsupported document version: {0}.
Error.ResultsetIteratorError = Result set iterator must be moved to valid row.
Error.ReportDesignNotFoundError = Can not find the report design in the report document {0}.
Error.PreparedQueryNotFoundError = Can''t find the prepared query {0}.
Error.IncorrectParentResultSetError = Incorrect parent resultSet for subQuery: {0}.
Error.UnsupportedQueryTypeError = Unsupported query type {0}.
Error.RenderTaskNotFinishedError = Render task is not finished.
Error.RenderOptionError = Render options have to be specified to render a report.
Error.ReportArchiveError = Report archive is not specified when running a report.
Error.ReportDocNameNotSpecifiedError = Report document name is not specified when running a report.
Error.ReportRunError = Error happened while running the report.
Error.ReportDocmentOpenError = Failed to open the report document.
Error.ReportArchiveOpenError = Can not open the report archive.
Error.SkipError = Error happened when skipping.
