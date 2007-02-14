# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={1} に {0} が見つかりません。

###########################################################

# DTE error code
data.engine.UndefinedDataSource=データソースが定義されていません : {0}
data.engine.UndefinedDataSet=データセットが定義されていません : {0}
data.engine.UnsupportedDataSourceType=データソース "{0}" の型はサポートされていません。
data.engine.UnsupportedDataSetType=データセット "{0}" の型はサポートされていません。
data.engine.UnsupportedGroupOnClobAndBLOB=CLOB データまたは BLOB データ "{0}" はグループ化できません。
data.engine.UnsupportedSortOnClobAndBLOB=CLOB データまたは BLOB データ "{0}" はソートできません。

data.engine.EndOfGroupOnly=この集計式は、グループの最後でしか使用できません。
data.engine.InvalidAggrGroup=集計関数のグループレベルが無効です : {0}
data.engine.WrongNumAggrArgs=集計関数の引数の数が正しくありません : {0}

data.engine.DupPropertyName=プロパティ名が重複しています : {0}
data.engine.NoSubQueryName={0} という名前のサブクエリが見つかりません。

data.engine.DataSourceMissingExtId=データソース {0} に拡張 ID が定義されていません。
data.engine.NullGroupStart=グループの開始値は null にできません。
data.engine.UnsupportedDirectNestedAggregate=集計を直接ネストすることはできません。

data.engine.ResultClosed=QueryResults またはその反復子が閉じられています。
data.engine.NoCurrentRow=Result Iterator には現在の行がありません。まだ開始していないか、終了しました。
data.engine.InvalidJSExpr=JavaScript 式が無効です : {0}
data.engine.InvalidTotalFuncName=合計関数の名前が無効です : {0}
data.engine.InvalidTotalFunc=合計関数が無効です。
data.engine.NoAggrFunc=この式では、集計関数の呼び出しを使用できません。
data.engine.InvalidExprHandle=式ハンドルが無効です。
data.engine.InvalidAggrGroupExpression=集計関数のグループ式が間違っています : {0}
data.engine.InvalidSortKeyColumn=ソートキー列が無効です : {0}
data.engine.InvalidGroupLevel=グループレベルが無効です : {0}
data.engine.InvalidGroupKeyName=グループキー列の名前が無効です : {0}
data.engine.BadCloseScript=CustomDataSet の closeScript を評価できません。
data.engine.InvalidCustomFieldIdx=カスタムフィールドのインデックスが無効です : {0}
data.engine.InvalidGroupName=グループ名が無効です。
data.engine.DuplicateGroupName=グループ名が重複しています。
data.engine.BadOpenScript=CustomDataSet の openScript を評価できません。
data.engine.DataSourceIsOpen=データソースがすでに開いています。
data.engine.DataSourceNotOpen=データソースが開いていません。
data.engine.EmptyCustomFieldName=カスタムフィールド名は空白にできません。
data.engine.DupCustomFieldName=カスタムフィールド名が重複しています : {0}
data.engine.NoResultSet=クエリを実行しても、結果セットが返されませんでした。
data.engine.NoResultMetadata=結果のメタデータがありません。
data.engine.QueryAlreadyPrepared=クエリはすでに記述済みです。
data.engine.QueryNotPrepared=クエリはまだ記述されていません。
data.engine.InvalidFieldName=フィールド名が無効です : {0}
data.engine.DupColumnName=列名が重複しています : {0}
data.engine.DupAlias=列名またはエイリアスが重複しています : {0}
data.engine.InvalidFieldIndex=フィールドのインデックスが無効です : {0}

data.engine.MatchError=パターンの構文を確認してください。文字列とパターンを照合中にエラーが発生しました : {0} 
data.engine.BadOperandType=このデータ型の演算子は使用できません : {0}

data.engine.BadParameterCount=指定されたパラメタの数が正しくありません。
data.engine.BadParameterType=1 つのパラメタのデータ型が正しくありません。
data.engine.NoIrrSolution=指定されたデータでは、Irr 関数の解が見つかりません。
data.engine.ExpectBooleanReturnType=スクリプトデータセットの {0} スクリプトは論理値を返す必要があります。
data.engine.BadIntervalUnit=Interval の単位値が無効です : {0}
data.engine.InvalidFieldName=フィールド名が無効です : {0}
data.engine.BadGroupKeyType=このデータ型のグループキーは使用できません : {0}
data.engine.group.interval={1} 型の列には、{0} の間隔を設定できません。
data.engine.group.interval.Rowid=rowId 列には、数値以外の型の間隔を設定できません。
data.engine.group.interval.invalid=指定した間隔は無効です。
data.engine.NoSupportFilterOnGroup=現在はグループにフィルタを適用できません。
data.engine.InvalidTopBottomArgument=Top と Bottom のフィルタの引数には、数値を指定してください。
data.engine.InvalidTopBottomNArgument=TopN と BottomN のフィルタの引数は、ゼロまたは正の整数である必要があります。
data.engine.InvalidTopBottomPercentArgument=TopPercent と BottomPercent のフィルタの引数は、両方とも 100 以下の正数である必要があります。
data.engine.InvalidTopBottomN=同時に処理できるのは {0} 行までです。
data.engine.InvalidPercentileArgument=Total.Percentile 関数の 2 つ目の引数には、0 ～ 1 までの数値しか指定できません。
data.engine.InvalidQuartileArgument=Total.Quartile 関数の 2 つ目の引数には、0 ～ 4 までの整数しか指定できません。
data.engine.BadAggrFuncParam=集計関数のパラメタが無効です : {0}
data.engine.ResetRateGuess=新しい Rate Guess をリセットしてください。
data.engine.UnsupportedCondOp=条件演算子は使用できません : {0}
data.engine.EmptyExpression=式は null または空白にはできません。

data.engine.InvalidJoinType=ジョインの型が無効です。
data.engine.InvalidJoinOperator=ジョインの演算子が無効です。
data.engine.BadDataSetParamName=データセットの未定義パラメタ : {0}

#column binding
data.engine.InvalidBoundColumnName=バインドされたカラムの名前が無効です : {0}
data.engine.InvalidGroupKey=グループ {1} のグループキー "{0}" は、それより下位のグループで定義されているカラムバインドに依存させることはできません。 
data.engine.ColumnBindingNotExist=列バインド "{0}" は存在しません。
data.engine.ColumnBindingCycle=カラムバインド "{0}" の依存関係が循環参照になっています。
data.engine.ColumnBindingReferToInexistColumn=列バインド "{0}" は、存在しないデータセット列 "{1}" を参照しています。
#computed column value
data.engine.BadCompColExpr=計算列 "{0}" の式が無効です。
data.engine.CompCol.WrongDataType=計算列 "{0}" のデータ型は {1} ですが、このうち 1 つの値が "{2}" であるため、{1} に変換できません。
data.engine.ScriptResult.WrongDataType=スクリプト式の結果のデータ型は {0} ですが、このうち 1 つの値が "{1}" であるため、{0} に変換できません。

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
data.engine.SaveReportDocumentError=レポート文書を保存中にエラーが発生しました : {0}
data.engine.LoadReportDocumentError=レポート文書を読み込み中にエラーが発生しました : {0}
data.engine.GetSaveStreamError=保存ストリームを取得する際にレポート文書でエラーが発生しました。
data.engine.GetLoadStreamError=読み込みストリームを取得する際にレポート文書でエラーが発生しました。
data.engine.NullExpr=プレゼンテーションの値を取得するには、式を null にすることができません。
data.engine.InValidExpr=式が無効なため、プレゼンテーションの値を取得できません。
data.engine.BadGroupExpr=グループ式は空白にできません。 
data.engine.ResultSetNotStart=結果セットはまだ開始していません。

data.engine.invalidRowIndex=ロウのインデックス値が無効です : {0}
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
date.engine.DuplicateAggregationName=集計名が重複しています : {0}
data.engine.incorrectGroupKeyValues=グループキーの値の配列が間違っています。
data.engine.NoExpressionProcessorAvailable=ExpressionProcessor がありません。
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} を {1} に変換できません。
data.engine.NoRowUpdate=現時点はデータ行を更新できません。
data.engine.BadDataType=このデータ型は使用できません。
data.engine.BadDataExpression=データ式は null にすることができません。

data.engine.cacheFunctionWrongMode=キャッシュ機能は、DIRECT_PRESENTATION モードでしか使用できません。
data.engine.document.invalidIndexArray=インデックスの配列が無効です。
data.engine.document.invalidGroupItem=削除したグループ項目が正しくありません。
data.engine.resultIteratorNotOpen=Result Iterator はまだ開いていません。
data.engine.resultIteratorClosed=Result Iterator はすでに閉じています。
data.engine.invalidOutputParameterIndex=出力パラメタのインデックス {0} が無効です。
data.engine.outputParameterOutOfBound=出力パラメタのインデックス {0} が範囲内にありません。
data.engine.invalidOutputParameterName=出力パラメタの名前 {0} が無効です。
data.engine.document.notSupportInPresentation=プレゼンテーションではサポートされていません。
data.engine.wrongStatus=ステータスが正しくありません。
data.engine.wrongVersion=2.1 よりも古いバージョンではサポートされていません。
data.engine.unsupportOperationException=この操作はサポートされていません。
data.engine.resultClassSaveError=列バインド内の列参照の一部が存在しません。
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
odaconsumer.CannotGetParameterTypeName=パラメタ {0} の型名を取得できません。
odaconsumer.CannotGetParameterMode=パラメタ {0} のモードを取得できません。
odaconsumer.CannotGetParameterScale=パラメタ {0} のスケールを取得できません。
odaconsumer.CannotGetParameterPrecision=パラメタ {0} の精度を取得できません。
odaconsumer.CannotGetParameterIsNullable=パラメタ {0} の NULL 値の許容属性を識別できません。
odaconsumer.SameParamNameForDifferentHints=このデータセット内に、同じ名前 {0} のパラメタがあります。
odaconsumer.DifferentParamNameForSamePosition=このデータセット内の同じ場所 {1} に、同じ名前 {0} のパラメタがあります。
odaconsumer.ParameterValueIsNull=パラメタ値が null です。
odaconsumer.UnsupportedParameterValueType=パラメタ値の型 {0} はサポートされていません。
odaconsumer.UnknownExceptionThrown=最後の setter 呼び出しで不明な例外が発生しました。
odaconsumer.CannotConvertIndexedParameterValue=インデックス {1} にあるパラメタの値 {0} を、値の型 {2} から ODA の型 {3} に変換できません。
odaconsumer.CannotConvertNamedParameterValue={1} という名前のパラメタの値 {0} を、値の型 {2} から ODA の型 {3} に変換できません。
odaconsumer.UnrecognizedProjectedColumnName=投影された列名 {0} を認識できません。
odaconsumer.ColumnNameOrAliasAlreadyUsed=新しい列の名前またはエイリアス {0} は、すでにインデックス {1} の列で使用されています。
odaconsumer.NamedResultSetsUnsupported=この文では、名前付きの結果セットを使用できません。
odaconsumer.OutputParameterUnsupported=この文では、出力パラメタを使用できません。
odaconsumer.CannotGetConnectionMetaData=接続メタデータを取得できません。
odaconsumer.CannotGetMaxConnections=データソースの接続の最大数を取得できません。
odaconsumer.CannotGetMaxStatements=データソースのクエリの最大数を取得できません。
odaconsumer.CannotGetDataSetMetaData=データセットのメタデータ {0} をデータソースから取得できません。
odaconsumer.CannotPrepareStatement=データセットの型 {1} に、次のクエリを記述できませんでした。\n[{0}]
odaconsumer.CannotCloseConnection=接続を閉じることができません。
odaconsumer.CannotOpenConnection=ドライバ {0} の接続を開けません。
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
odaconsumer.CannotGetMetaDataForNamedResultSet=名前付き結果セット {0} のメタデータを取得できません。
odaconsumer.CannotExecuteStatement=文を実行できません。
odaconsumer.CannotGetResultSet=結果セットを取得できません。
odaconsumer.CannotGetNamedResultSet=名前付き結果セット {0} を取得できません。
odaconsumer.CannotFindOutParameter=出力パラメタ {0} が見つかりません。
odaconsumer.CannotGetParameterType=パラメタ {0} の型を取得できません。
odaconsumer.CannotCloseStatement=文を閉じることができません。
odaconsumer.CannotGetColumnCount=列の数を取得できません。
odaconsumer.CannotGetColumnName=列 {0} の名前を取得できません。
odaconsumer.CannotGetColumnLabel=列 {0} のラベルを取得できません。
odaconsumer.CannotGetColumnType=列 {0} の型を取得できません。
odaconsumer.CannotFetchNextRow=次のデータ行をフェッチできません。
odaconsumer.CannotDetermineWasNull=最後に取得した値が null であったかどうかを識別できません。
odaconsumer.CannotGetIntFromColumn=列 {0} から整数の値を取得できません。
odaconsumer.CannotGetDoubleFromColumn=列 {0} から倍精度の値を取得できません。
odaconsumer.CannotGetStringFromColumn=列 {0} から文字列の値を取得できません。
odaconsumer.CannotGetBigDecimalFromColumn=列 {0} から小数の値を取得できません。
odaconsumer.CannotGetDateFromColumn=列 {0} から日付の値を取得できません。
odaconsumer.CannotGetTimeFromColumn=列 {0} から時刻の値を取得できません。
odaconsumer.CannotGetTimestampFromColumn=列 {0} からタイムスタンプの値を取得できません。
odaconsumer.CannotGetClobFromColumn=列 {0} から CLOB の値を取得できません。
odaconsumer.CannotGetBlobFromColumn=列 {0} から BLOB の値を取得できません。
odaconsumer.CannotGetRowPosition=現在の行の位置を取得できません
odaconsumer.CannotCloseResultSet=結果セットを閉じることができません。
odaconsumer.CannotGetParameterCount=パラメタの数を取得できません。
odaconsumer.CannotGetIntFromParameter=パラメタ {0} から整数の値を取得できません。
odaconsumer.CannotGetDoubleFromParameter=パラメタ {0} から倍精度の値を取得できません。
odaconsumer.CannotGetStringFromParameter=パラメタ {0} から文字列の値を取得できません。
odaconsumer.CannotGetBigDecimalFromParameter=パラメタ {0} から小数の値を取得できません。
odaconsumer.CannotGetDateFromParameter=パラメタ {0} から日付の値を取得できません。
odaconsumer.CannotGetTimeFromParameter=パラメタ {0} から時刻の値を取得できません。
odaconsumer.CannotGetTimestampFromParameter=パラメタ {0} からタイムスタンプの値を取得できません。
odaconsumer.CannotGetBlobFromParameter=パラメタ {0} から BLOB の値を取得できません。
odaconsumer.CannotGetClobFromParameter=パラメタ {0} から CLOB の値を取得できません。
odaconsumer.CannotFindInParameter=入力パラメタ {0} が見つかりません。
odaconsumer.CannotSetIntParameter=パラメタ {0} に整数の値を設定できません。
odaconsumer.CannotSetDoubleParameter=パラメタ {0} に倍精度の値を設定できません。
odaconsumer.CannotSetStringParameter=パラメタ {0} に文字列の値を設定できません。
odaconsumer.CannotSetBigDecimalParameter=パラメタ {0} に小数の値を設定できません。
odaconsumer.CannotSetDateParameter=パラメタ {0} に日付の値を設定できません。
odaconsumer.CannotSetTimeParameter=パラメタ {0} に時刻の値を設定できません。
odaconsumer.CannotSetTimestampParameter=パラメタ {0} にタイムスタンプの値を設定できません。
odaconsumer.CannotClearInParameters=入力パラメタの値をクリアできません。
odaconsumer.UnrecognizedOdaType=ODA データの型 {0} を識別できません。
odaconsumer.CannotGetColumnNativeDataTypeName=列 {0} のネイティブデータ型の名前を取得できません。
odaconsumer.CannotGetParameterMetaData=ODA ドライバのパラメタのメタデータを取得できません。
odaconsumer.CannotLoadOdaAdapter={0} ドライバを読み込めません。DTP-to-BIRT ODA アダプタのプラグイン {1} が見つかりません。
odaconsumer.MustSpecifyColumnName=列のヒントを追加するときは、列名を指定してください。

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
odaconsumer.CannotFindLogDirectory=ログディレクトリが見つかりません。
