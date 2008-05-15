# Localized messages for the DTE Engine. Messages here give 
# the text of Data Engine exceptions.

###########################################################
#for test only
Error.Msg001={1} に {0} が見つかりません。

###########################################################

# DTE error code
data.engine.UndefinedDataSource=Data source is not defined: {0}.
data.engine.UndefinedDataSet=Data set is not defined: {0}.
data.engine.UnsupportedDataSourceType=データソース "{0}" の型はサポートされていません。
data.engine.UnsupportedDataSetType=データセット "{0}" の型はサポートされていません。
data.engine.UnsupportedGroupOnClobAndBLOB=CLOB データまたは BLOB データ "{0}" はグループ化できません。
data.engine.UnsupportedSortOnClobAndBLOB=CLOB データまたは BLOB データ "{0}" はソートできません。

data.engine.EndOfGroupOnly=This aggregate expression is only available at the end of a group.
data.engine.InvalidAggregation=The aggregation definition is invalid:{0}.
data.engine.InvalidAggrGroup=Invalid grouping level for aggregate function: {0}.
data.engine.WrongNumAggrArgs=Wrong number of arguments to aggregate function: {0}.
data.engine.AggrParamGroupLevelInconsistent=In nested aggregation, all the non-constant expression must have same group level.
data.engine.DupPropertyName=Found duplicate property name: {0}.
data.engine.NoSubQueryName=Cannot find subquery with name: {0}.

data.engine.DataSourceMissingExtId=データソース {0} に拡張 ID が定義されていません。
data.engine.NullGroupStart=グループの開始値は null にできません。
data.engine.UnsupportedDirectNestedAggregate=集計を直接ネストすることはできません。

data.engine.ResultClosed=QueryResults またはその反復子が閉じられています。
data.engine.NoCurrentRow=Result Iterator には現在の行がありません。まだ開始していないか、終了しました。
data.engine.InvalidJSExpr=Invalid Javascript expression: {0}.
data.engine.InvalidTotalFuncName=Invalid total function name: {0}.
data.engine.InvalidTotalFunc=合計関数が無効です。
data.engine.NoAggrFunc=この式では、集計関数の呼び出しを使用できません。
data.engine.InvalidExprHandle=式ハンドルが無効です。
data.engine.InvalidAggrGroupExpression=Wrong group expression for aggregate function: {0}.
data.engine.InvalidSortKeyColumn=Invalid sort key column: {0}.
data.engine.InvalidGroupLevel= 層形式 {0} のグループレベルが無効です。 
data.engine.InvalidGroupKeyName=Invalid group key column name: {0}.
data.engine.BadCloseScript=CustomDataSet の closeScript を評価できません。
data.engine.InvalidCustomFieldIdx=Invalid custom field index: {0}.
data.engine.InvalidGroupName=グループ名が無効です。
data.engine.DuplicateGroupName=グループ名が重複しています。
data.engine.BadOpenScript=CustomDataSet の openScript を評価できません。
data.engine.DataSourceIsOpen=データソースがすでに開いています。
data.engine.DataSourceNotOpen=データソースが開いていません。
data.engine.EmptyCustomFieldName=カスタムフィールド名は空白にできません。
data.engine.DupCustomFieldName=Duplicate custom field name: {0}.
data.engine.NoResultSet=クエリを実行しても、結果セットが返されませんでした。
data.engine.NoResultMetadata=結果のメタデータがありません。
data.engine.QueryAlreadyPrepared=クエリはすでに記述済みです。
data.engine.QueryNotPrepared=クエリはまだ記述されていません。
data.engine.InvalidFieldName=Invalid field name: {0}.
data.engine.DupColumnName=Duplicate column name: {0}.
data.engine.DupAlias=Duplicate column name or alias: {0}.
data.engine.InvalidFieldIndex=Invalid field index: {0}.

data.engine.MatchError=Check pattern syntax. Error matching string to pattern: {0}.

data.engine.ExpectBooleanReturnType=スクリプトデータセットの {0} スクリプトは論理値を返す必要があります。
data.engine.BadIntervalUnit=Invalid Interval unit value: {0}.
data.engine.InvalidFieldName=Invalid field name: {0}.
data.engine.BadGroupKeyType=Group key has unsupported data type: {0}.
data.engine.group.interval={1} 型の列には、{0} の間隔を設定できません。
data.engine.group.interval.Rowid=ROWID 列には、数値以外の型の間隔を設定できません。
data.engine.group.interval.invalid=指定した間隔は無効です。
data.engine.group.intervalRange.invalid=間隔の範囲 {0} が無効です。
data.engine.NoSupportFilterOnGroup=現在はグループにフィルタを適用できません。
data.engine.InvalidTopBottomArgument=Top と Bottom のフィルタの引数には、数値を指定してください。
data.engine.InvalidTopBottomNArgument=TopN と BottomN のフィルタの引数は、ゼロまたは正の整数である必要があります。
data.engine.InvalidTopBottomPercentArgument=TopPercent と BottomPercent のフィルタの引数は、両方とも 100 以下の正数である必要があります。

data.engine.UnsupportedCondOp=Conditional operator is not supported: {0}.
data.engine.EmptyExpression=式は null または空白にはできません。

data.engine.InvalidJoinType=ジョインの型が無効です。
data.engine.InvalidJoinOperator=ジョインの演算子が無効です。
data.engine.BadDataSetParamName=データセットの未定義パラメタ : {0}

#column binding
data.engine.EmptyBindingName=バインド名は null または空白にできません。 
data.engine.InvalidBoundColumnName=バインドされたカラムの名前が無効です : {0}
data.engine.InvalidGroupKey=グループ {1} のグループキー "{0}" は、それより下位のグループで定義されているカラムバインドに依存させることはできません。 
data.engine.ColumnBindingNotExist=列バインド "{0}" は存在しません。
data.engine.ColumnBindingCycle=バインド "{0}" の依存関係が循環参照になっています。 
data.engine.ColumnBindingReferToInexistColumn=列バインド "{0}" は、存在しないバインド "{1}" を参照しています。
data.engine.ColumnBindingReferToInexistBinding=Column binding "{0}" has referred to a data set column "{1}" which does not exist.
data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=列バインド "{0}" が正しくありません。親クエリの列バインドに集計が含まれていますが、サブクエリの列バインドには使用できません。
data.engine.ReadColumnValueFromDocumentError=列 {0} の値を読み取り中にエラーが発生しました。バインドの式 (ゼロ除算エラー) を評価中に実行時エラーが発生した可能性があります。
data.engine.NoOuterResultsExist=外部クエリの結果が存在しません。これは、クエリで "_outer" が参照されているのに親クエリがないことが原因と考えられます。
data.engine.DuplicatedBindingName=バインド名 {0} が重複しています。
#computed column value
data.engine.BadCompColExpr=計算列 "{0}" の式が無効です。
data.engine.CompCol.FailRetrieveValueComputedColumn=計算列 "{0}" の値を計算できません。
data.engine.ScriptResult.WrongSystemComputedColumn=ソート式、グループ式、またはフィルタ式の値を計算できません。
data.engine.ComputedColumnCycle=計算列 "{0}" の依存関係が循環参照になっています。

data.engine.BadFilterExpr=フィルタ式 "{0}" が無効です。
data.engine.BadFilterDefn=フィルタ定義式 "{0}" が無効です。

#clob and blob error
data.engine.opencloberror=CLOB 文字ストリームを開くことができません。
data.engine.readcloberror=CLOB 文字ストリームを読み取れません。
data.engine.openbloberror=BLOB バイナリストリームを開くことができません。
data.engine.readbloberror=BLOB バイナリストリームを読み取れません。

#resultsetcache exception
data.engine.BadDestIndex=destIndex の値 {0} は無効です。0 ～ {1} までの数値を指定してください。
data.engine.WriteTempError=一時ファイルに大きなデータセットを書き込み中に例外が発生しました。
data.engine.ReadTempError=一時ファイルから大きなデータセットを読み込み中に例外が発生しました。

#resultiterator cache exception
data.engine.CreateCacheTempError=結果行をキャッシュするための一時ファイルを作成中に例外が発生しました。
data.engine.OpenCacheTempError=結果行をキャッシュするための一時ファイルを開いているときに例外が発生しました。
data.engine.WriteCacheTempError=結果行をキャッシュするための一時ファイルに書き込んでいるときに例外が発生しました。
data.engine.ReadCacheTempError=結果行をキャッシュするための一時ファイルを読み込んでいるときに例外が発生しました。
data.engine.CloseCacheTempError=結果行をキャッシュするための一時ファイルを閉じているときに例外が発生しました。

#load data engine factory exception
load.factory.error=データエンジンのファクトリを読み込めません。

# Wrapped Error
data.engine.DataConversionError=データ型を変換中にエラーが発生しました。
data.engine.BadDataTypeCondExpr=条件式のデータ型が無効です。
data.engine.BirtException=BIRT で例外 {0} が発生しました。詳しくは、次の例外を参照してください。

# data set cache error
data.engine.datasetcache.save.error=データセットキャッシュのファイルを保存できません。
data.engine.datasetcache.load.error=データセットキャッシュのファイルを読み込めません。

# error related with data engine context
data.engine.InvalidMode=データエンジンコンテキストのモードの値 {0} が無効です。
data.engine.InvalidArchive=作成時またはプレゼンテーション時は、レポートアーカイブを null にすることができません。 
data.engine.SaveReportDocumentError=A report document error occurred when saving: {0}.
data.engine.LoadReportDocumentError=A report document error occurred when loading: {0}.
data.engine.GetSaveStreamError=保存ストリームを取得する際にレポート文書でエラーが発生しました。
data.engine.GetLoadStreamError=読み込みストリームを取得する際にレポート文書でエラーが発生しました。
data.engine.NullExpr=プレゼンテーションの値を取得するには、式を null にすることができません。
data.engine.InValidExpr=式が無効なため、プレゼンテーションの値を取得できません。
data.engine.BadGroupExpr=グループ式は空白にできません。 
data.engine.filterExprContainRowNum=フィルタ式では、Java スクリプトオブジェクト "row.__num" を参照できません。
data.engine.sortExprContainRowNum=ソート式では、Java スクリプトオブジェクト "row.__num" を参照できません。


data.engine.ResultSetNotStart=結果セットはまだ開始していません。

data.engine.invalidRowIndex=Invalid row index value: {0}.
data.engine.backwardSeekError=このバージョンでは、後方シークを実行できません。
data.engine.startError=先に、次のメソッドを呼び出してください。

# query running based on report document
data.engine.rd.errorfilters=クエリに定義したフィルタはサポートされていません。

# other error
data.engine.ScriptEvalError=スクリプトの評価エラーが発生しました (ソース : {0}、行 : {1}:  {2}。
data.engine.PreparedQueryClosed=PreparedQuery インスタンスを閉じました。
data.engine.NullDefaultInParamValue=入力パラメタのデフォルト値は null にできません。
data.engine.BadGroupKeyExpr=グループキーの式 {0} が無効です。
data.engine.GroupNotExist=列 {1} で使われているグループ "{0}" は存在しません。
data.engine.BadSortKeyExpr=ソートキーの式 {0} が無効です。
data.engine.NoParamMetaForDataSource=この型のデータソースには、パラメタメタデータを取得できません。
data.engine.UnexpectedError=例外が発生したため、データエンジンを操作できません。 
data.engine.ConvertDataTypeError=パラメタ値 {0} を {1} 型に変換できません。
data.engine.CannotInstantiateAggregationFactory=集計ファクトリ {0} をインスタンス化できません。
date.engine.DuplicateAggregationName=Duplicate aggregation name: {0}.
data.engine.incorrectGroupKeyValues=グループキーの値の配列が間違っています。
data.engine.NoExpressionProcessorAvailable=ExpressionProcessor がありません。
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} を {1} に変換できません。
data.engine.NoRowUpdate=現時点はデータ行を更新できません。
data.engine.BadDataType=このデータ型は使用できません。
data.engine.BadDataExpression=データ式は null にすることができません。
data.engine.aggregation.InvalidBindingExpression=総計バインド "{0}" の式が無効です。
data.engine.InvalidExpression=式 "{0}" が無効です。

data.engine.cacheFunctionWrongMode=キャッシュ機能は、DIRECT_PRESENTATION モードでしか使用できません。
data.engine.document.invalidIndexArray=インデックスの配列が無効です。
data.engine.document.invalidGroupItem=削除したグループ項目が正しくありません。
data.engine.resultIteratorNotOpen=Result Iterator はまだ開いていません。
data.engine.resultIteratorClosed=Result Iterator はすでに閉じています。
data.engine.invalidOutputParameterIndex=Invalid output parameter index: {0}.
data.engine.outputParameterOutOfBound=Output parameter index is out of bound: {0}.
data.engine.invalidOutputParameterName=Invalid output parameter name: {0}.
data.engine.document.notSupportInPresentation=プレゼンテーションではサポートされていません。
data.engine.wrongStatus=ステータスが正しくありません。
data.engine.wrongVersion=2.1 よりも古いバージョンではサポートされていません。
data.engine.unsupportOperationException=この操作はサポートされていません。
data.engine.possibleMixedDataTypeInColumn=データを収集中にエラーが発生しました。これは、"ANY" 型の列に複数の型のデータを混合させたことが原因と思われます。
data.engine.resultClassSaveError=列の参照 {0} が見つかりません。
data.engine.invalidMemBufferSize=メモリバッファサイズ "{0}" が無効です。メモリバッファサイズには、1 以上の整数を指定してください。
data.engine.document.error.cannotLoadStream=文書ストリーム "{0}" が無効です。
data.engine.failToCreateTempDir="{0}" に一時ディレクトリを作成できません。 
data.engine.reportItem.SubQuery.Unsupported=サブクエリが含まれているレポート項目は共有できません。


# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=列名は空白または null にできません。
odaconsumer.ColumnPositionIsOneBased=Column position is 1-based and cannot be less than 1.
odaconsumer.ColumnAliasCannotBeEmpty=列のエイリアスは、空白文字列にすることができません。
odaconsumer.CannotProcessDriverConfig={0} ドライバの設定を処理できません。
odaconsumer.OdaInitEntryNotFound=ドライバ {0} の初期エントリポイントが見つからないか、インスタンス化できません。
odaconsumer.OdaDriverOnUnsupportedPlatform=ドライバ {0} では、{1} がサポートされていません。
odaconsumer.CannotGenerateUrl=クラスローダに URL {0} を生成できません。
odaconsumer.UnsupportedNativeType=ネイティブ型 {0} は、データセットの型 {1} のドライバ {2} ではサポートされていません。
odaconsumer.UnsupportedDataSetType=データセットの型 {0} は、ドライバ {1} ではサポートされていません。
odaconsumer.CannotDetermineDefaultDataSetType=データセットの型が null のため、ドライバ {0} のデフォルトのデータセットの型を識別できません。
odaconsumer.InvalidDataSetType=データセットの型 {0} が無効です。データセット {1} のデフォルトの型を識別できません。
odaconsumer.ParameterNameCannotBeEmptyOrNull=パラメタ名は空白または null にできません。
odaconsumer.ParameterPositionIsOneBased=パラメタの位置には 1 より小さい値を指定できません。
odaconsumer.CannotGetParameterNativeName=Cannot get the driver-defined name of parameter: {0}.
odaconsumer.CannotGetParameterType=Cannot get the type for parameter: {0}.
odaconsumer.CannotGetParameterTypeName=Cannot get the type name for parameter: {0}.
odaconsumer.CannotGetParameterMode=Cannot get the mode for parameter: {0}.
odaconsumer.CannotGetParameterScale=Cannot get the scale for parameter: {0}.
odaconsumer.CannotGetParameterPrecision=Cannot get the precision for parameter: {0}.
odaconsumer.CannotGetParameterIsNullable=Cannot determine the nullability for parameter: {0}.
odaconsumer.SameParamNameForDifferentHints=このデータセット内に、同じ名前 {0} のパラメタがあります。
odaconsumer.DifferentParamNameForSamePosition=このデータセット内の同じ場所 {1} に、同じ名前 {0} のパラメタがあります。
odaconsumer.ParameterValueIsNull=パラメタ値が null です。
odaconsumer.UnsupportedParameterValueType=Unsupported parameter value type: {0}.
odaconsumer.UnknownExceptionThrown=最後の setter 呼び出しで不明な例外が発生しました。
odaconsumer.CannotConvertIndexedParameterValue=インデックス {1} にあるパラメタの値 {0} を、値の型 {2} から ODA の型 {3} に変換できません。
odaconsumer.CannotConvertNamedParameterValue={1} という名前のパラメタの値 {0} を、値の型 {2} から ODA の型 {3} に変換できません。
odaconsumer.UnrecognizedProjectedColumnName=投影された列名 {0} を認識できません。
odaconsumer.ColumnNameOrAliasAlreadyUsed=The new column name or alias {0} is already used by the column at index {1}.
odaconsumer.NamedResultSetsUnsupported=この文では、名前付きの結果セットを使用できません。
odaconsumer.OutputParameterUnsupported=この文では、出力パラメタを使用できません。
odaconsumer.CannotGetConnectionMetaData=接続メタデータを取得できません。
odaconsumer.CannotGetMaxConnections=データソースの接続の最大数を取得できません。
odaconsumer.CannotGetMaxStatements=データソースのクエリの最大数を取得できません。
odaconsumer.CannotGetDataSetMetaData=データセットのメタデータ {0} をデータソースから取得できません。
odaconsumer.CannotPrepareStatement=データセットの型 {1} に、次のクエリを記述できませんでした。\n[{0}]
odaconsumer.CannotCloseConnection=接続を閉じることができません。
odaconsumer.CannotOpenConnection=Cannot open the connection for the driver: {0}.
odaconsumer.CannotDetermineSupportForMultipleOpenResults=複数の結果を開けるかどうかを識別できません。
odaconsumer.CannotDetermineSupportForMultipleResultSets=複数の結果セットのサポート属性を識別できません。
odaconsumer.CannotDetermineSupportForNamedResultSets=名前付き結果セットのサポート属性を識別できません。
odaconsumer.CannotDetermineSupportForNamedParameters=名前付きパラメタのサポート属性を識別できません。
odaconsumer.CannotDetermineSupportForInParameters=入力パラメタのサポート属性を識別できません。
odaconsumer.CannotDetermineSupportForOutParameters=出力パラメタのサポート属性を識別できません。
odaconsumer.CannotSetStatementProperty=文のプロパティを設定できません。
odaconsumer.CannotSetSortSpec=ソート順を設定できません。
odaconsumer.CannotSetMaxRows=行の最大数を設定できません。
odaconsumer.CannotGetResultSetMetaData=結果セットのメタデータを取得できません。
odaconsumer.CannotGetMetaDataForNamedResultSet=Cannot get the metadata for the named result set: {0}.
odaconsumer.CannotExecuteStatement=文を実行できません。
odaconsumer.CannotGetResultSet=結果セットを取得できません。
odaconsumer.CannotGetNamedResultSet=Cannot get the named result set: {0}.
odaconsumer.CannotFindOutParameter=Cannot find the output parameter: {0}.
odaconsumer.CannotCloseStatement=文を閉じることができません。
odaconsumer.CannotGetColumnCount=列の数を取得できません。
odaconsumer.CannotGetColumnName=Cannot get the name for column: {0}.
odaconsumer.CannotGetColumnLabel=Cannot get the label for column: {0}.
odaconsumer.CannotGetColumnType=Cannot get the type for column: {0}.
odaconsumer.CannotFetchNextRow=次のデータ行をフェッチできません。
odaconsumer.CannotDetermineWasNull=最後に取得した値が null であったかどうかを識別できません。
odaconsumer.CannotGetIntFromColumn=Cannot get the integer value from column: {0}.
odaconsumer.CannotGetDoubleFromColumn=Cannot get the double value from column: {0}.
odaconsumer.CannotGetStringFromColumn=Cannot get the string value from column: {0}.
odaconsumer.CannotGetBigDecimalFromColumn=Cannot get the decimal value from column: {0}.
odaconsumer.CannotGetDateFromColumn=Cannot get the date value from column: {0}.
odaconsumer.CannotGetTimeFromColumn=Cannot get the time value from column: {0}.
odaconsumer.CannotGetTimestampFromColumn=Cannot get the timestamp value column: {0}.
odaconsumer.CannotGetClobFromColumn=Cannot get the CLOB value from column: {0}.
odaconsumer.CannotGetBlobFromColumn=Cannot get the BLOB value from column: {0}.
odaconsumer.CannotGetBooleanFromColumn=Cannot get the boolean value from column: {0}.
odaconsumer.CannotGetRowPosition=現在の行の位置を取得できません
odaconsumer.CannotCloseResultSet=結果セットを閉じることができません。
odaconsumer.CannotGetParameterCount=パラメタの数を取得できません。
odaconsumer.CannotGetIntFromParameter=Cannot get the integer value from parameter: {0}.
odaconsumer.CannotGetDoubleFromParameter=Cannot get the double value from parameter: {0}.
odaconsumer.CannotGetStringFromParameter=Cannot get the string value from parameter: {0}.
odaconsumer.CannotGetBigDecimalFromParameter=Cannot get the decimal value from parameter: {0}.
odaconsumer.CannotGetDateFromParameter=Cannot get the date value from parameter: {0}.
odaconsumer.CannotGetTimeFromParameter=Cannot get the time value from parameter: {0}.
odaconsumer.CannotGetTimestampFromParameter=Cannot get the timestamp value from parameter: {0}.
odaconsumer.CannotGetBlobFromParameter=Cannot get the BLOB value from parameter: {0}.
odaconsumer.CannotGetClobFromParameter=Cannot get the CLOB value from parameter: {0}.
odaconsumer.CannotGetBooleanFromParameter=Cannot get the boolean value from parameter: {0}.
odaconsumer.CannotFindInParameter=Cannot find the input parameter: {0}.
odaconsumer.CannotSetIntParameter=Cannot set the integer value ({0}) to parameter {1}.
odaconsumer.CannotSetDoubleParameter=Cannot set the double value ({0}) to parameter {1}.
odaconsumer.CannotSetStringParameter=Cannot set the string value ({0}) to parameter {1}.
odaconsumer.CannotSetBigDecimalParameter=Cannot set the decimal value ({0}) to parameter {1}.
odaconsumer.CannotSetDateParameter=Cannot set the date value ({0}) to parameter {1}.
odaconsumer.CannotSetTimeParameter=Cannot set the time value ({0}) to parameter {1}.
odaconsumer.CannotSetTimestampParameter=Cannot set the timestamp value ({0}) to parameter {1}.
odaconsumer.CannotSetBooleanParameter=Cannot set the boolean value ({0}) to parameter {1}.
odaconsumer.CannotSetNullParameter=Cannot set a null value to parameter {0}.
odaconsumer.CannotClearInParameters=入力パラメタの値をクリアできません。
odaconsumer.UnrecognizedOdaType=ODA データの型 {0} を識別できません。
odaconsumer.CannotGetColumnNativeDataTypeName=列 {0} のネイティブデータ型の名前を取得できません。
odaconsumer.CannotGetParameterMetaData=ODA ドライバのパラメタのメタデータを取得できません。
odaconsumer.CannotLoadOdaAdapter={0} ドライバを読み込めません。DTP-to-BIRT ODA アダプタのプラグイン {1} が見つかりません。
odaconsumer.MustSpecifyColumnName=列のヒントを追加するときは、列名を指定してください。
odaconsumer.CannotFindLogDirectory=ログディレクトリが見つかりません。
odaconsumer.ParameterNames=\nこのパラメタには、モデル名 ({0}) とドライバで定義された名前 ({1}) が付いています。

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=executeQuery() を呼び出した後は、結果セットを名前で取得することができません。
odaconsumer.manager.CannotSetParamBeforePrepare=文を記述する前は入力パラメタを設定できません。
odaconsumer.manager.CannotGetParamBeforeExecute=文を実行する前は出力パラメタを取得できません。
odaconsumer.manager.CannotGetParamMdBeforePrepare=文を記述する前はパラメタのメタデータを取得できません。
odaconsumer.manager.CannotSetSortSpecBeforePrepare=文を記述する前はソート順を設定できません。
odaconsumer.manager.MaxConcurrentConnectionsReached=アクティブの同時接続が制限数に達しました。
odaconsumer.manager.ConnectionFailed=接続を開けません。
odaconsumer.manager.CannotConstructConnectionFactory=ODA ドライバの接続ファクトリを作成できません。
odaconsumer.manager.InadequatePermissionsForCompatibility=必要な権限がないため、下位互換性が無効です。
odaconsumer.manager.ConnectionIsInactive=データソースへの確立済みの接続が、アクティブな状態になっていません。
odaconsumer.manager.MaxConcurrentStatementsReached=この接続では、アクティブの同時実行文が制限数に達しています。
odaconsumer.manager.PrepareFailed=準備できませんでした。
odaconsumer.manager.CannotGetResultSetMdBeforePrepare=文を記述する前は結果セットのメタデータを取得できません。
odaconsumer.manager.CannotExecuteBeforePrepare=文を記述する前は文を実行できません。


# Data Olap error code
data.olap.OlapDirCreateFail={0} ディレクトリを作成できません。
data.olap.OlapFileCreateFail=ファイル {0} を作成できません。
data.olap.OlapFileNotFound=OLAP ファイル {0} が見つかりません。
data.olap.OlapFileFormatInvalid=The file format is invalid. Data object name is: {0}.
data.olap.DimensionNotExist=次元 {0} が存在しません。
data.olap.KeyValueCanNotBeNull=キー列の値は NULL にすることができません。キー列 : {0}
data.olap.DetailMemberHaveMultiParent=詳細メンバ {0} には複数の親があります。
data.olap.OlapFileDataError=データオブジェクト {0} のデータが無効です。
data.olap.DocumentObjectAlreadyExists =ドキュメントオブジェクトがすでに存在するため、データオブジェクト {0} を作成できませんでした。
data.olap.DocumentObjectNotExists =キューブ "{0}" を文書から読み込めできません。文書オブジェクトが存在しません。
data.olap.FacttableRowNotDistinct=ファクトテーブルでは行が固有でなければなりませんが、"{0}" が複数あります。
data.olap.FacttableJointColumnNotExist=結合列 "{0}" は次元にありません。
data.olap.ParameterColumnOfAggregationNotExist=パラメタ列 "{0}" は次元にありません。
data.olap.FacttableNullMeasureValue=The value of a measure column is null. "{0}".
data.olap.InvalidDimensionPositionFacttableRow=次元のキー値 "{0}" が次元 "{1}" に含まれていません。
data.olap.measureNameNotFound=計測名 "{0}" が見つかりません。
data.olap.levelNameNotFound=レベル名 "{0}" が見つかりません。
data.olap.dimensionNameNotFound=次元名 "{0}" が見つかりません。
data.olap.ReferencedDimensionNotFound=参照した次元定義 {0} は存在しません。
data.olap.ReferencedLevelNotFound=参照したレベルは存在しません。
data.olap.cursorPositionError=カーソルでは目的の位置にナビゲートできません。
data.olap.cursorSeekError=カーソルでは参照の位置にナビゲートできません。
data.olap.CannotGetMeasureValue=キューブに計測 "{0}" の定義が見つかりません。この計測は存在しないか、無効です。
data.olap.CannotFindLevel=次元 "{2}" の階層 "{1}" に "{0}" レベルの定義が見つかりません。
data.olap.tryToAddIdenticalAggrGroup=値 "{0}" は、すでにバインドの一覧 "{1}" の集計に存在します。
data.olap.NoEdgeDefinitionFound=行と列のどちらのエッジも定義されていないため、クエリを実行できません。少なくともいずれか一方のエッジを定義してください。
data.olap.invalidSortDefinition=ソートの定義が無効です。 
data.olap.invalidMeasureRef="{0}" は、有効な計測参照式ではありません。
data.olap.invalidLevelAttr=属性が無効です。
data.olap.referencedBindingNotExist=参照先のバインド "{0}" は存在しません。
data.olap.invalidBindingReferToInexitDim=バインド "{0}" で参照しているレベルが無効です。
data.olap.invalidBindingReferToInexitMeasure=バインド "{0}" で参照している計測が無効です。  
data.olap.invalidBindingMissingAggrFunc=バインド "{0}" に集計関数がありません。
data.olap.invalidAggrArgument=集計引数に指定できるのはレベルの属性だけです。
data.olap.axis.level.CantBeNull=列メンバのレベルは null にできません。
data.olap.axis.value.CantBeNull=列メンバのレベル \"{0}\" の値は null にできません。
data.olap.fetchlimitWarning=結果セットの全体のサイズは [{1}] ですが、{0} しかフェッチされませんでした。
data.olap.failComputeComputedMeasureValue=計測値を計算できません。
data.olap.duplicateMeasureName=計測名 \"{0}\" が重複しています。
data.olap.dataBindingExpressionEmpty=データのバインド式は NULL や空白にできません。
data.olap.missingParentCursor=親結果セットが存在しません。
data.olap.CannotAccessNullDimensionRow=Null 次元の行にはアクセスできません。
data.olap.NonexistentDimension=存在しない次元 :
data.olap.NonexistentLevel=存在しないレベル : 
data.olap.NonexistentKeyOrAttr=存在しないキーまたは属性 :
data.olap.UnsupportedFunction=サポートされていない関数 :
data.olap.UnspecifiedBindingName=バインド名が指定されていません。
data.olap.NotNestAggregationBinding = ネストされた総計のバインドではありません : {0}
data.olap.sort.exprError=参照する次元レベルにつき 1 つのレベルしか含めることができません。

data.cache.UnsupportedIncrementalCacheMode=増分キャッシュの \"{0}\" モードはまだサポートされていません。
# Configuration file parser error code
data.executor.configFileParser.getModeByID=データセット名から増分キャッシュのモードを取得できませんでした。
data.executor.configFileParser.getTSFormatByID=設定ファイルで指定したデータセット名からタイムスタンプの書式を解析できませんでした。
data.executor.configFileParser.getTimeStampColumnByID=設定ファイルで指定したデータセット名からタイムスタンプの書式を解析できませんでした。
data.executor.configFileParser.getQueryTextByID=設定ファイルで指定したデータセット名からクエリテキストを取得できませんでした。
data.executor.configFileParser.getParametersByID=設定ファイルで指定したデータセット名からパラメタを取得できませんでした。

data.aggregation.expression.displayName=式
data.aggregation.argumentError=総計 \"{1}\" の引数式 \"{0}\" が null または空白です。
