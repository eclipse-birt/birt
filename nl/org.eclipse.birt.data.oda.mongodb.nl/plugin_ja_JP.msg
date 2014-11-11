###############################################################################
# Copyright (c) 2013 Actuate Corporation
# All rights reserved.
#
###############################################################################
# Plug-in Configuration
#
oda.data.source.id=org.eclipse.birt.data.oda.mongodb
#
###############################################################################
# NLS Strings
###############################################################################
#
plugin.name=MongoDB データソース ODA 実行時のドライバ
data.source.name=MongoDB データソース
data.set.name=MongoDB データセット
connection.profile.name=MongoDB データソース接続プロファイル

# Data source properties
datasource.property.mongoDbUri=Mongo データベース URI(&M)
datasource.property.ignoreURI=URI 無視(&I)
datasource.property.serverHost=サーバホスト(&H)
datasource.property.serverPort=サーバのポート(&P)
datasource.property.databaseName=データベース名(&D)
datasource.property.userName=ユーザ名(&U):
datasource.property.password=パスワード(&W)
datasource.property.socketKeepAlive=ソケットの保存(&A)
datasource.property.useRequestSession=一貫した要求セッション(&R)
datasource.property.group.uriElements=Mongo データベース URI 要素(&E)
datasource.property.group.options=追加のクライアント設定(&S)

# Data set properties - query definition
dataset.property.collectionName=コレクション名(&C)
dataset.property.operationType=コマンド操作タイプ(&O)
dataset.property.operationExpr=コマンド式(&E)
dataset.property.selectedFields=選択フィールド(&L)
dataset.property.findQueryExpr=クエリ式(&Q)
dataset.property.sortExpr=ソート式(&S)
# Data set properties - advanced runtime properties
dataset.property.batchSize=バッチ内の結果オブジェクトの最大数(&B)
dataset.property.rtMDSearchLimit=ランタイムメタデータを検索するドキュメントの最大数(&M)
dataset.property.numSkipDocuments=スキップするドキュメントの最大数(&K)
dataset.property.indexHints=索引ヒント式(&H)
dataset.property.queryReadPreference=クエリカーソル読取設定(&R)
dataset.property.queryReadPreferenceTags=読取設定タグセット(&G)
dataset.property.noTimeOut=カーソルタイムアウトがありません(&N)
dataset.property.allowsPartialResults=結果の一部を許可(&P)
dataset.property.flattenCollections=ネストされたコレクションをフラット化(&F)
#
dataset.property.value.readPrefPrimary=プライマリ
dataset.property.value.readPrefPrimaryPreferred=プライマリ優先
dataset.property.value.readPrefSecondary=セカンダリ
dataset.property.value.readPrefSecondaryPreferred=セカンダリ優先
dataset.property.value.readPrefNearest=最も近い
