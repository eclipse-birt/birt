#************************************************************************
# Copyright (c) 2004, 2010 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#
#************************************************************************
# Localized messages for the DTE Engine. Messages here give
# the text of Data Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} に {0} が見つかりません。

###########################################################

# DTE error code
data.engine.UndefinedDataSource=データソースが定義されていません : {0}

data.engine.UndefinedDataSet=データセットが定義されていません : {0}

data.engine.UnsupportedDataSourceType=データソース "{0}" の型はサポートされていません。

data.engine.UnsupportedDataSetType=データセット "{0}" の型はサポートされていません。

data.engine.UnsupportedGroupOnClobAndBLOB=CLOB データまたは BLOB データ "{0}" はグループ化できません。

data.engine.UnsupportedSortOnClobAndBLOB=CLOB データまたは BLOB データ "{0}" はソートできません。

data.engine.EndOfGroupOnly=この集計式は、グループの最後でしか使用できません。

data.engine.InvalidAggregation=集計の定義が無効です: {0}。

data.engine.InvalidAggrGroup=集計関数のグループレベルが無効です : {0}

data.engine.WrongNumAggrArgs=集計関数の引数の数が正しくありません : {0}

data.engine.AggrParamGroupLevelInconsistent=ネストされた集計 "{0}" では、定数以外のすべての式が同じグループレベルでなければなりません。

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

data.engine.InvalidGroupLevel= 層形式 {0} のグループレベルが無効です。

data.engine.InvalidGroupKeyName=列 {0} でのグループ化はサポートされている操作ではありません。

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

data.engine.fail.prepareExecution=データセットにクエリの実行を準備できません : {0}

data.engine.groupIncrementalUpdate.IllegalGroupOrderState=グループの更新の状態が正しくありません。現在のグループ <{0}> がプロセスグループ <{1}> より多くなっています。

data.engine.MatchError=パターンの構文を確認してください。文字列とパターンを照合中にエラーが発生しました : {0}

data.engine.ExpectBooleanReturnType=スクリプトデータセットの {0} スクリプトは論理値を返す必要があります。

data.engine.BadIntervalUnit=Interval の単位値が無効です : {0}

data.engine.BadGroupKeyType=このデータ型のグループキーは使用できません : {0}

data.engine.group.interval={1} 型の列には、{0} の間隔を設定できません。

data.engine.group.interval.Rowid=ROWID 列には、数値以外の型の間隔を設定できません。

data.engine.group.interval.invalid=指定した間隔は無効です。

data.engine.group.intervalRange.invalid=間隔の範囲 {0} が無効です。

data.engine.NoSupportFilterOnGroup=現在はグループにフィルタを適用できません。

data.engine.InvalidTopBottomArgument=Top と Bottom のフィルタの引数には、数値を指定してください。

data.engine.InvalidTopBottomNArgument=TopN と BottomN のフィルタの引数は、ゼロまたは正の整数である必要があります。

data.engine.InvalidTopBottomPercentArgument=TopPercent と BottomPercent のフィルタの引数は、両方とも 100 以下の正数である必要があります。

data.engine.UnsupportedCondOp=条件演算子は使用できません : {0}

data.engine.EmptyExpression=式は null または空白にはできません。

data.engine.AggregationBinding.EmptyArgument=集計バインド "{1}" には引数 "{0}" が必要です。

data.engine.InvalidJoinType=ジョインの型が無効です。

data.engine.InvalidJoinOperator=ジョインの演算子が無効です。

data.engine.BadDataSetParamName=データセットの未定義パラメタ : {0}

data.engine.FailComputeOutputParameterValue=出力パラメタ値の評価エラー : {0}

data.engine.LinkedReportParamAllowMultiValues=複数の値を使用できるレポートパラメタ "{0}" は、データセットパラメタ "{1}" のリンクには使用できません。

#column binding
data.engine.EmptyBindingName=バインド名は null または空白にできません。

data.engine.InvalidBoundColumnName=バインドされたカラムの名前が無効です : {0}

data.engine.InvalidGroupKey=グループ {1} のグループキー "{0}" は、それより下位のグループで定義されているカラムバインドに依存させることはできません。

data.engine.ColumnBindingNotExist=バインド "{0}" は存在しません。

data.engine.ColumnBindingCycle=バインド "{0}" の依存関係が循環参照になっています。

data.engine.ColumnBindingReferToInexistColumn=列バインド "{0}" は、存在しないバインド "{1}" を参照しています。

data.engine.ColumnBindingReferToInexistBinding=列バインド "{0}" は、存在しないデータセット列 "{1}" を参照しています。

data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=列バインド "{0}" が正しくありません。親クエリの列バインドに集計が含まれていますが、サブクエリの列バインドには使用できません。

data.engine.ReadColumnValueFromDocumentError=列 {0} の値を読み取り中にエラーが発生しました。バインドの式 (ゼロ除算エラー) を評価中に実行時エラーが発生した可能性があります。

data.engine.NoOuterResultsExist=外部クエリの結果が存在しません。これは、クエリで "_outer" が参照されているのに親クエリがないことが原因と考えられます。

data.engine.DuplicatedBindingName=バインド名 {0} が重複しています。

data.engine.InvalidAggrLevelKeyInSummaryQuery=集計 {0} は正しくないグループ名を参照しています。
#computed column value
data.engine.BadCompColExpr=計算列 "{0}" の式が無効です。

data.engine.CompCol.FailRetrieveValueComputedColumn=計算列 "{0}" の値を計算できません。

data.engine.ScriptResult.WrongSystemComputedColumn=ソート、グループ化、フィルタ、またはバインドの式を評価できません。 式で使用されているデータフィールドがテータセットに存在しません。

data.engine.ComputedColumnCycle=計算列 "{0}" の依存関係が循環参照になっています。

data.engine.SortOnAggregation=グループ化されていない集計を使用するソート、または集計 "{0}" を参照する計算式はサポートされていません。

data.engine.BadFilterExpr=フィルタ式 "{0}" が無効です。

data.engine.BadFilterDefn=式 "{0}" に基づくフィルタを評価できません。

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

data.engine.FailedLoadColumnValue="{0}" 型の列 "{1}" の値を読み込めません。

data.engine.FailedLoadClass="{1}" 型の列 "{2}" のクラス "{0}" を読み込めません。 クラス "{0}"が、BIRT エンジンのクラス読み込みパスに含まれていることを確認してください。

#load data engine factory exception
load.factory.error=データエンジンのファクトリを読み込めません。

# Wrapped Error
data.engine.DataConversionError=データ型を変換中にエラーが発生しました。

data.engine.BadCompareExpr="{0}" を "{1}" と比較できません。

data.engine.BadCompareSingleValueWithMultiValues=1 つの値を [{0}] の複数の値と比較するフィルタ条件が無効であるため、フィルタを適用できません。\nこのフィルタでは値を 1 つだけ設定してください。

data.engine.BirtException=BIRT で例外が発生しました。 詳しくは、次の例外を参照してください。

# data set cache error
data.engine.datasetcache.save.error=データセットキャッシュのファイルを保存できません。

data.engine.datasetcache.load.error=データセットキャッシュのファイルを読み込めません。

data.engine.exceed.max.data.object.row=クエリを処理できません。 クエリを実行するには、結果セットのバッファサイズを増やすか、クエリを絞り込む必要があります。

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

data.engine.filterExprContainRowNum=フィルタ式では、Java スクリプトオブジェクト "row.__num" を参照できません。

data.engine.sortExprContainRowNum=ソート式では、Java スクリプトオブジェクト "row.__num" を参照できません。

data.engine.ResultSetNotStart=結果セットはまだ開始していません。

data.engine.GetLevelMemberError=このデータバインドは利用可能なスコープの外部で使用されます。

data.engine.invalidRowIndex=行のインデックス値が無効です : {0}

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

data.engine.InconsistentAggregationArgumentDefinition=集計拡張クラスの実装に定義された集計引数が、plugin.xml に定義された引数と一致しません。

data.engine.incorrectGroupKeyValues=グループキーの値の配列が間違っています。

data.engine.NoExpressionProcessorAvailable=ExpressionProcessor がありません。

data.engine.InconvertibleDatatype=値 {0} の元のデータ型は {1} のため、割り当てられた型 {2} のデータには変換できません。

data.engine.ConvertToDataTypeError={0} を {1} に変換できません。

data.engine.NoRowUpdate=現時点はデータ行を更新できません。

data.engine.BadDataType=このデータ型は使用できません {0}。

data.engine.NotSerializableClass=クラス {0} はシリアライズできません。

data.engine.BadDataExpression=データ式は null にすることができません。

data.engine.aggregation.InvalidBindingExpression=メジャー値は、集計のバインド "{0}" には使用できません。

data.engine.InvalidExpression=式 "{0}" が無効です。

data.engine.BadFetchScriptReturnType=データセットのスクリプトメソッド "{0}" は {1} を返しました。論理値を指定してください。

data.engine.FailToExecuteScript=関数 {0}() 内のスクリプトの実行に失敗しました。 ソース :\n------\n" + {1} + "\n-----

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

data.engine.possibleMixedDataTypeInColumn=データを収集中にエラーが発生しました。これは、"ANY" 型の列に複数の型のデータを混合させたことが原因と思われます。

data.engine.resultClassSaveError=列の参照 {0} が見つかりません。

data.engine.invalidMemBufferSize=メモリバッファサイズ "{0}" が無効です。メモリバッファサイズには、1 以上の整数を指定してください。

data.engine.document.error.cannotLoadStream=文書ストリーム "{0}" が無効です。

data.engine.failToCreateTempDir="{0}" に一時ディレクトリを作成できません。

data.engine.reportItem.SubQuery.Unsupported=サブクエリにはレポート項目を共有できません。

data.engine.emptyResultSet=結果セットは空です。

data.engine.errorHappenWhenRetrieveResultSet=結果セット "{0}" の表示中にエラーが発生しました。指定した結果セットが存在しない可能性があります。

data.engine.invalidAggregationBindingForPLSReport=ページレベルセキュリティが有効な場合、集計バインド "{0}" は無効です。

data.engine.acl.dataSetCannotBeAccessed=データセット "{0}" にアクセスできません。

data.engine.acl.dataSetColumnCannotBeAccessed=データセット列 "{0}" にアクセスできません。

data.engine.acl.cubeCannotBeAccessed=ユーザにはキューブ "{0}" にアクセスする権限がありません。

data.engine.acl.cubeLevelCannotBeAccessed=ユーザにはレベル "{0}" にアクセスする権限がありません。

data.engine.acl.cubeMeasureCannotBeAccessed=ユーザにはメジャー "{0}" にアクセスする権限がありません。

data.engine.acl.cubeDimensionCannotBeAccessed=ユーザにはディメンション "{0}" にアクセスする権限がありません。
# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=列名は空白または null にできません。

odaconsumer.ColumnPositionIsOneBased=列の位置は 1 ベースであり、1 より小さい値を指定できません。

odaconsumer.ColumnAliasCannotBeEmpty=列のエイリアスは、空白文字列にすることができません。

odaconsumer.CannotProcessDriverConfig={0} ドライバのデータソース拡張の設定が見つからないか、処理できません。

odaconsumer.OdaInitEntryNotFound=ドライバ {0} の初期エントリポイントが見つからないか、インスタンス化できません。

odaconsumer.OdaDriverOnUnsupportedPlatform=ドライバ {0} では、{1} がサポートされていません。

odaconsumer.CannotGenerateUrl=クラスローダに URL {0} を生成できません。

odaconsumer.UnsupportedNativeType=ネイティブ型 {0} は、データセットの型 {1} のドライバ {2} ではサポートされていません。

odaconsumer.UnsupportedDataSetType=データセットの型 {0} は、ドライバ {1} ではサポートされていません。

odaconsumer.CannotDetermineDefaultDataSetType=データセットの型が null のため、ドライバ {0} のデフォルトのデータセットの型を識別できません。

odaconsumer.InvalidDataSetType=データセットの型 {0} が無効です。データセット {1} のデフォルトの型を識別できません。

odaconsumer.ParameterNameCannotBeEmptyOrNull=パラメタ名は空白または null にできません。

odaconsumer.ParameterPositionIsOneBased=パラメタの位置には 1 より小さい値を指定できません。

odaconsumer.CannotGetParameterNativeName=パラメタ {0} のドライバで定義された名前を取得できません。

odaconsumer.CannotGetParameterType=パラメタ {0} の型を取得できません。

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

odaconsumer.UnsupportedMultipleResultSets=この文では、複数の結果セットを使用できません。

odaconsumer.CannotGetConnectionMetaData=接続メタデータを取得できません。

odaconsumer.CannotGetMaxConnections=データソースの接続の最大数を取得できません。

odaconsumer.CannotGetMaxStatements=データソースのクエリの最大数を取得できません。

odaconsumer.CannotGetDataSetMetaData=データセットのメタデータ {0} をデータソースから取得できません。

odaconsumer.CannotPrepareStatement=データセットの型 ({1}) に、次のクエリを記述できませんでした。 \n[{0}]

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

odaconsumer.CannotGetMoreResults=文からこれ以上の結果セットを取得できません。

odaconsumer.CannotFindOutParameter=出力パラメタ {0} が見つかりません。

odaconsumer.CannotCloseStatement=文を閉じることができません。

odaconsumer.CannotCancelStatement=文をキャンセルできません。

odaconsumer.CannotGetColumnCount=列の数を取得できません。

odaconsumer.CannotGetColumnName=列 {0} の名前を取得できません。

odaconsumer.CannotGetColumnLabel=列 {0} のラベルを取得できません。

odaconsumer.CannotGetColumnType=列 {0} の型を取得できません。

odaconsumer.CannotFetchNextRow=次のデータ行をフェッチできません。

odaconsumer.CannotDetermineWasNull=最後に取得した値が null であったかどうかを識別できません。

odaconsumer.CannotGetIntFromColumn=列 {0} から整数の値を取得できません。

odaconsumer.CannotGetDoubleFromColumn=列 {0} から倍精度の値を取得できません。

odaconsumer.CannotGetStringFromColumn=列 {0} から文字列の値を取得できません。

odaconsumer.CannotGetBigDecimalFromColumn=列 {0} から10進数の値を取得できません。

odaconsumer.CannotGetDateFromColumn=列 {0} から日付の値を取得できません。

odaconsumer.CannotGetTimeFromColumn=列 {0} から時刻の値を取得できません。

odaconsumer.CannotGetTimestampFromColumn=列 {0} からタイムスタンプの値を取得できません。

odaconsumer.CannotGetClobFromColumn=列 {0} から CLOB の値を取得できません。

odaconsumer.CannotGetBlobFromColumn=列 {0} から BLOB の値を取得できません。

odaconsumer.CannotGetBooleanFromColumn=列 {0} から論理値を取得できません。

odaconsumer.CannotGetObjectFromColumn=列 {0} からオブジェクトの値を取得できません。

odaconsumer.CannotGetRowPosition=現在の行の位置を取得できません

odaconsumer.CannotCloseResultSet=結果セットを閉じることができません。

odaconsumer.CannotGetParameterCount=パラメタの数を取得できません。

odaconsumer.CannotGetIntFromParameter=パラメタ {0} から整数の値を取得できません。

odaconsumer.CannotGetDoubleFromParameter=パラメタ {0} から倍精度の値を取得できません。

odaconsumer.CannotGetStringFromParameter=パラメタ {0} から文字列の値を取得できません。

odaconsumer.CannotGetBigDecimalFromParameter=パラメタ {0} から10進数の値を取得できません。

odaconsumer.CannotGetDateFromParameter=パラメタ {0} から日付の値を取得できません。

odaconsumer.CannotGetTimeFromParameter=パラメタ {0} から時刻の値を取得できません。

odaconsumer.CannotGetTimestampFromParameter=パラメタ {0} からタイムスタンプの値を取得できません。

odaconsumer.CannotGetBlobFromParameter=パラメタ {0} から BLOB の値を取得できません。

odaconsumer.CannotGetClobFromParameter=パラメタ {0} から CLOB の値を取得できません。

odaconsumer.CannotGetBooleanFromParameter=パラメタ {0} から 論理値を取得できません。

odaconsumer.CannotGetObjectFromParameter=パラメタ {0} からオブジェクトの値を取得できません。

odaconsumer.CannotFindInParameter=入力パラメタ {0} が見つかりません。

odaconsumer.CannotSetIntParameter=パラメタ {1} には整数の値 ({0}) を設定できません。

odaconsumer.CannotSetDoubleParameter=パラメタ {1} には倍精度の値 ({0}) を設定できません。

odaconsumer.CannotSetStringParameter=パラメタ {1} には文字列値 ({0}) を設定できません。

odaconsumer.CannotSetBigDecimalParameter=パラメタ {1} には10進数の値 ({0}) を設定できません。

odaconsumer.CannotSetDateParameter=パラメタ {1} には日付の値 ({0}) を設定できません。

odaconsumer.CannotSetTimeParameter=パラメタ {1} には時間の値 ({0}) を設定できません。

odaconsumer.CannotSetTimestampParameter=パラメタ {1} にはタイムスタンプの値 ({0}) を設定できません。

odaconsumer.CannotSetBooleanParameter=パラメタ {1} には論理値 ({0}) を設定できません。

odaconsumer.CannotSetObjectParameter=パラメタ {1} にはオブジェクトの値 ({0}) を設定できません。

odaconsumer.CannotSetNullParameter=パラメタ {0} には NULL 値を設定できません。

odaconsumer.CannotClearInParameters=入力パラメタの値をクリアできません。

odaconsumer.UnrecognizedOdaType=ODA データの型 {0} を識別できません。

odaconsumer.CannotGetColumnNativeDataTypeName=列 {0} のネイティブデータ型の名前を取得できません。

odaconsumer.CannotGetParameterMetaData=ODA ドライバのパラメタのメタデータを取得できません。

odaconsumer.CannotLoadOdaAdapter={0} ドライバを読み込めません。DTP-to-BIRT ODA アダプタのプラグイン {1} が見つかりません。

odaconsumer.MustSpecifyColumnName=列のヒントを追加するときは、列名を指定してください。

odaconsumer.CannotFindLogDirectory=ログディレクトリが見つかりません。

odaconsumer.ParameterNames=\nこのパラメタには、モデル名 ({0}) とドライバで定義された名前 ({1}) が付いています。

odaconsumer.InvalidArgument=無効な引数 ({1}) でメソッド ({0}) を呼び出しました。

odaconsumer.CannotSetConnectionLocale=接続ロケール ({0}) を設定できません。

odaconsumer.CannotCheckOpenConnection=接続がすでに開いているかどうかを確認できません。

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

data.olap.OlapFileFormatInvalid=ファイル形式が無効です。データオブジェクト名 : {0}

data.olap.DimensionNotExist=ディメンション {0} が存在しません。

data.olap.KeyValueCanNotBeNull=キー列の値は NULL にすることができません。キー列 : {0}

data.olap.DetailMemberHaveMultiParent=詳細メンバ {0} には複数の親があります。

data.olap.OlapFileDataError=データオブジェクト {0} のデータが無効です。

data.olap.DocumentObjectAlreadyExists =ドキュメントオブジェクトがすでに存在するため、データオブジェクト {0} を作成できませんでした。

data.olap.DocumentObjectNotExists =キューブ "{0}" を文書から読み込めできません。文書オブジェクトが存在しません。

data.olap.FacttableRowNotDistinct=ファクトテーブルでは行が固有でなければなりませんが、"{0}" が複数あります。

data.olap.FacttableJointColumnNotExist=ジョイント列 "{0}" はディメンションにありません。

data.olap.ParameterColumnOfAggregationNotExist=パラメタ列 "{0}" はディメンションにありません。

data.olap.FacttableNullMeasureValue=メジャー列の値が NULL です。"{0}"

data.olap.InvalidDimensionPositionFacttableRow=キューブ "{0}" のファクトテーブルに、無効なデータ行があります。ディメンションキー "{1}" の値である "{2}" は、ディメンション "{3}" に存在しません。

data.olap.measureNameNotFound=メジャー名 "{0}" が見つかりません。

data.olap.levelNameNotFound=レベル名 "{0}" が見つかりません。

data.olap.dimensionNameNotFound=キューブクエリにディメンション "{0}" が見つかりません。このディメンションは存在しないか、無効です。

data.olap.ReferencedDimensionNotFound=参照したディメンション定義 {0} は存在しません。

data.olap.ReferencedLevelNotFound=参照したレベルは存在しません。

data.olap.cursorPositionError=カーソルでは目的の位置にナビゲートできません。

data.olap.cursorSeekError=カーソルでは参照の位置にナビゲートできません。

data.olap.CannotGetMeasureValue=キューブにメジャー "{0}" の定義が見つかりません。このメジャーは存在しないか、無効です。

data.olap.CannotFindLevel=ディメンション "{1}" に "{0}" レベルの定義が見つかりません。

data.olap.tryToAddIdenticalAggrGroup=値 "{0}" は、すでにバインドの一覧 "{1}" の集計に存在します。

data.olap.NoEdgeDefinitionFound=行と列のどちらのエッジも定義されていないため、クエリを実行できません。少なくともいずれか一方のエッジを定義してください。

data.olap.invalidSortDefinition=ソートの定義が無効です。

data.olap.invalidMeasureRef="{0}" は、有効なメジャー参照式ではありません。

data.olap.invalidLevelAttr=属性が無効です。

data.olap.referencedBindingNotExist=参照先のバインド "{0}" は存在しません。

data.olap.invalidBindingReferToInexitDim=バインド "{0}" で参照しているレベルが無効です。

data.olap.invalidBindingReferToInexitMeasure=バインド "{0}" で参照しているメジャーが無効です。

data.olap.invalidBindingMissingAggrFunc=バインド "{0}" に集計関数がありません。

data.olap.invalidAggrArgument=集計引数に指定できるのはレベルの属性だけです。

data.olap.axis.level.CantBeNull=列メンバのレベルは null にできません。

data.olap.axis.value.CantBeNull=列メンバのレベル \"{0}\" の値は null にできません。

data.olap.fetchlimitWarning=結果セットの全体のサイズは [{1}] ですが、{0} しかフェッチされませんでした。

data.olap.failComputeComputedMeasureValue=メジャー値を計算できません。

data.olap.duplicateMeasureName=メジャー名 \"{0}\" が重複しています。

data.olap.dataBindingExpressionEmpty=データのバインド式は NULL や空白にできません。

data.olap.missingParentCursor=親結果セットが存在しません。

data.olap.CannotAccessNullDimensionRow=Null ディメンションの行にはアクセスできません。

data.olap.NonexistentDimension=存在しないディメンション :

data.olap.NonexistentLevel=存在しないレベル :

data.olap.NonexistentKeyOrAttr=存在しないキーまたは属性 :

data.olap.UnsupportedFunction=サポートされていない関数 :

data.olap.UnspecifiedBindingName=バインド名が指定されていません。

data.olap.NotNestAggregationBinding=キューブネストされた集計のバインドではありません : {0}

data.olap.InvalidNestAggregationExpression=キューブネストされた集計の式が無効です。

data.olap.InvalidNestAggregationOn=レベルの集計がベースとなる集計に存在しないため、ネストされた集計 "{0}"を計算できません。

data.olap.InvalidAggregationFilterExpr=フィルタ式 "{0}" が無効です。 集計バインド "{1}" の aggregationOn リストに、参照するディメンションレベル "{2}" が含まれていません。

data.olap.sort.exprError=参照するディメンションレベルにつき 1 つのレベルしか含めることができません。

data.olap.query.noCubeBinding=キューブのバインドが使用できないため、キューブクエリを実行できませんでした。

data.olap.FailLoadCube=キューブを読み込めません : "{0}"

data.olap.MissingDimensionInCube=キューブ "{0}" には少なくとも 1 つのディメンションが必要です。

data.olap.ExceedIntegerLimit= 結果セットの長さが Integer.MAX を超えています。

data.olap.ExceedColumnLimit= 列のエッジにある結果セットの長さがサイズの制限 {0} を超えています。

data.olap.ExceedRowLimit= 行のエッジにある結果セットの長さがサイズの制限 {0} を超えています。

data.cache.UnsupportedIncrementalCacheMode=増分キャッシュの \"{0}\" モードはまだサポートされていません。
# Configuration file parser error code
data.executor.configFileParser.getModeByID=データセット名から増分キャッシュのモードを取得できませんでした。

data.executor.configFileParser.getTSFormatByID=設定ファイルで指定したデータセット名からタイムスタンプの書式を解析できませんでした。

data.executor.configFileParser.getTimeStampColumnByID=設定ファイルで指定したデータセット名からタイムスタンプの書式を解析できませんでした。

data.executor.configFileParser.getQueryTextByID=設定ファイルで指定したデータセット名からクエリテキストを取得できませんでした。

data.executor.configFileParser.getParametersByID=設定ファイルで指定したデータセット名からパラメタを取得できませんでした。

data.aggregation.expression.displayName=式

data.aggregation.argumentError=集計 \"{1}\" の引数式 \"{0}\" が null または空白です。

data.olap.InvalidAggrTypeOnMeasure=キューブのメジャーに対する集計ではこの型は使用できません : "{0}"

#derived dataSet
data.derivedDataSet.columnNotExist=列 \"{0}\" が存在しません。

data.derivedDataSet.UnionQueryTextParseError=ユニオンクエリを解析できません。\"{0}\" \"{0}\"

data.derivedDataSet.JoinQueryTextParseError=ジョインクエリを解析できません。\"{0}\" \"{0}\"

data.derivedDataSet.illegalClassInstance=初期化するクラスインスタンスが無効です。

data.derivedDataSet.classLoadError=クラスインスタンスをロードできません。

data.derivedDataSet.calculatorNotExist=計算が定義されていないか、見つかりません。

data.derivedDataSet.invalidQueryText=クエリテキストが無効です。

#data mart data source
data.datamart.failLoadDataSet = データオブジェクトデータセット "{0}" を読み込めません。

data.datamart.originalDataSetNotExist = 増分更新できません。 元のデータセット "{0}" が存在しません。

data.datamart.incrementalDatasetMetaDataError = 増分更新できません。 追加のデータセット "{0}" のメタデータが、元のデータセットのものと異なります。

data.datamart.collation=以前の製品では、照合を設定した場合でもデータオブジェクトのインデックスで照合は使用されません。 現在は照合がサポートされているので、データセットで照合を設定した場合はデータオブジェクトを再生成する必要があります。

data.filter.pushDownFail= ODA 拡張側でのみサポートされるフィルタをプッシュ (下へ) できませんでした。

data.query.exprReferInvalidColumn=クエリを実行できません。理由: 無効なデータセットの列 "{0}" を参照する無効な式がクエリに含まれています。

data.query.invalidColumnBinding=列バインド "{0}" が無効です。