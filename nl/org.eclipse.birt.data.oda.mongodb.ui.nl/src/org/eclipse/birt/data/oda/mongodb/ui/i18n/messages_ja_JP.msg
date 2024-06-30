##************************************************************************
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#  
#*************************************************************************
#
MongoDBDataSourceWizardPage.message.default=Mongo データベースデータソース接続プロファイル

MongoDBDataSourceWizardPage.GroupTitle.URIElements=Mongo データベース URI 要素

MongoDBDataSourceWizardPage.text.label.ServerHost=サーバホスト(&H):

MongoDBDataSourceWizardPage.text.label.ServerPort=サーバのポート(&O):

MongoDBDataSourceWizardPage.text.label.DatabaseName=データベース名(&D):

MongoDBDataSourceWizardPage.text.label.UserName=ユーザ名(&U):

MongoDBDataSourceWizardPage.text.label.Password=パスワード(&W):

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=Mongo データベース URI(&M):

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=追加のクライアント設定(&C)

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=ソケットの保存(&A)

MongoDBDataSourceWizardPage.checkbox.label.RequestSession=一貫した要求セッション(&R)

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=必要な接続プロパティが見つかりません!

#
MongoDBDataSetWizardPage.label.DBName=Mongo データベース(&G):

MongoDBDataSetWizardPage.label.DBCollection=MongoDB コレクション(&C)

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=システムコレクションを含める(&I)

MongoDBDataSetWizardPage.Button.Refresh=更新(&R)

MongoDBDataSetWizardPage.label.DocumentNumber=フィールドを検索する文書数(&M):

MongoDBDataSetWizardPage.group.text=コマンドオペレーション(&O)

MongoDBDataSetWizardPage.label.OperationType=コマンドタイプ(&T):

MongoDBDataSetWizardPage.Button.FindFields=フィールドの検索(&D)

MongoDBDataSetWizardPage.Button.text.CommandExpression=式(&E)...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=コマンド式を編集します。

MongoDBDataSetWizardPage.Button.QueryExpression=クエリ式(&Q)...

MongoDBDataSetWizardPage.Button.SortExpression=ソート式(&S)...

MongoDBDataSetWizardPage.Button.AdvancedSettings=詳細設定(&V)...

MongoDBDataSetWizardPage.label.AvailableFields=選択可能なフィールド(&A):

MongoDBDataSetWizardPage.label.SelectedFields=選択済みフィールド(&S):

MongoDBDataSetWizardPage.HeaderText.SelectedFields=フィールドの表示名

MongoDBDataSetWizardPage.menuItem.remove=削除(&M)

MongoDBDataSetWizardPage.menuItem.removeAll=すべて削除(&L)

MongoDBDataSetWizardPage.message.default=データセット列のフィールドを選択してください。

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=確認して続行

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=別のコレクションを選択すると現在選択されているフィールドが破棄されます。  続行しますか ?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=確認して更新

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=変更されたコマンド式を基にして選択可能なフィールドを更新しますか ?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=エラー

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=MongoDB データソースからコレクションのリストを取得できません。

MongoDBDataSetWizardPage.ExceptionDialog.title=エラー

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=選択可能なフィールドが見つかりません。

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=アクティブなデータソースの接続がありません。

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=MongoDB データセットの更新中にエラーが発生しました。

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=コマンド式が無効です。

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=クエリ式が無効です。

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=ソート式が無効です。

MongoDBDataSetWizardPage.message.error.MissingCollectionName=コレクション名は空白にできません。

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=フィールドを検索する文書数の整数値を入力してください。

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=選択したフィールド "{0}" は存在しません。

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=コマンド式は空白にできません。

MongoDBDataSetWizardPage.message.error.MissingSelectedField=少なくとも 1 つのフィールドを選択してください。

MongoDBAdvancedSettingsDialog.dialogTitle=高度なデータセットランタイムプロパティ

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=バッチ内の結果オブジェクトの最大数(&B):

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=MongoDB カーソルによって 1 つのバッチで取得される結果オブジェクトの最大数\n  デフォルト値 = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=メタデータを検索するドキュメントの最大数(&M):

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=実行時にクエリの結果セットメタデータを取得するために\n処理する文書数  デフォルト値 = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=スキップするドキュメントの最大数(&K):

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=結果セットの最初のnドキュメントをスキップ。整数のデータ型。\nデフォルト値 = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=索引ヒント式(&H):

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=クエリの最適化のためにクエリで使用する必要がある 1 つ以上のインデックスキー。\n インデックス付きのキーを一覧表示するには、Mongo JSON ヒント式の構文を使用します。

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=クエリカーソル読取設定

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=モード(&D):

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=クエリカーソル操作の読取設定モード。

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=タグセット(&G):

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=メンバノードに設定されている必要なタグのセット。\n  アレイ内のドキュメントとして代替タグセットを指定することができます。

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=カーソルタイムアウトがありません(&N)

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=過剰なメモリの使用を防ぐために、サーバでは通常、アイドル時間 (10 分) の経過後にアイドル状態のカーソルがタイムアウトになります。

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=結果の一部を許可(&P)

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=共有 (Mongo) と共に使用するときには、クラスタのいずれかの共有がダウンしているか見つからない場合、共有システムからの\n結果の一部が許可されます。  有効になっていない場合、Mongo サーバからエラーが返されます。

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=ネストされたコレクションをフラット化(&F)

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=フィールドのネストされたコレクションを複数のデータセット行にフラット化するためのユーザオプション。

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=[結果オブジェクトの最大数] の整数値が必要です。

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=[検索するドキュメントの最大数] の整数値が必要です。

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=[スキップするドキュメントの最大数] の整数値が必要です。

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=設計時の設定のリセット

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=OK

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=設計時の「フィールド検索するドキュメント数」の値は、実行時の「メタデータ検索する最大ドキュメント数」プロパティ値に一致するようにリセットされます。

MongoDBExpressionBuilder.dialogTitle.default=式ビルダ

MongoDBExpressionBuilder.ExprssionText.label=式テキスト(&X):

MongoDBExpressionBuilder.Button.import=インポート(&M)...

MongoDBExpressionBuilder.Button.tooltip.import=ローカルファイルから式のテキストをインポートする

MongoDBExpressionBuilder.Button.export=エクスポート(&E)...

MongoDBExpressionBuilder.Button.tooltip.export=ローカルファイルに式のテキストをエクスポートする

MongoDBExpressionBuilder.Button.ValidateSyntax=構文を検証(&V)

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=式のテキストの構文を検証します。

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=式のテキストの検証

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=式の構文が有効です。

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=式の構文が無効です。

MongoDBExpressionBuilder.CommandExpression.DialogTitle=コマンド式ビルダ

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=式のテキストは空白にできません。

MongoDBExpressionBuilder.QueryExpression.DialogTitle=クエリ式ビルダ

MongoDBExpressionBuilder.SortExpression.DialogTitle=ソート式ビルダ





