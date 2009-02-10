###############################################################################
# Copyright (c) 2004, 2007 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#
###############################################################################
# Plug-in Configuration
#
oda.data.source.id=org.eclipse.birt.report.data.oda.jdbc
oda.parent.category.id=org.eclipse.datatools.connectivity.oda.profileCategory
#
###############################################################################
# NLS String
###############################################################################
datasource.name=JDBC データソース
oda.data.source.name=JDBC データソース
connection.profile.name=BIRT JDBC データソース接続プロファイル

datasource.property.odaDriverClass=JDBC ドライバクラス(&C)
datasource.property.odaURL=JDBC ドライバ URL(&R)
datasource.property.odaDataSource=データソース
datasource.property.odaUser=ユーザ名(&N)
datasource.property.odaPassword=パスワード(&W)
datasource.property.odaJndiURL=JNDI URL(&J)
datasource.property.autocommit= &Auto Commit
datasource.property.isolationmode=&Isolation Mode
dataset.query.sqltype=SQL Select クエリ
dataset.query.sptype=SQL ストアドプロシージャのクエリ
dataset.property.queryTimeOut=クエリのタイムアウト (秒)(&Q)
dataset.connection.autocommit.false=false
dataset.connection.autocommit.true=true
dataset.connection.isolationmode.read-uncommitted=read-uncommitted
dataset.connection.isolationmode.read-committed=read-committed
dataset.connection.isolationmode.repeatable-read=repeatable-read
dataset.connection.isolationmode.serializable=serializable
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=ODA 接続ファクトリ
