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
datasource.name=JDBC 数据源
oda.data.source.name=JDBC 数据源
connection.profile.name=BIRT JDBC 数据源连接配置文件

datasource.property.odaDriverClass=JDBC 驱动程序类(&C)
datasource.property.odaURL=JDBC 驱动程序 URL(&R)
datasource.property.odaDataSource=数据源
datasource.property.odaUser=用户名(&N)
datasource.property.odaPassword=密码(&W)
datasource.property.odaJndiURL=JNDI URL(&J)
datasource.property.autocommit= 自动提交(&A)
datasource.property.isolationmode=隔离模式(&I)
dataset.query.sqltype=SQL 选择查询
dataset.query.sptype=SQL 存储过程查询
dataset.property.queryTimeOut=查询超时（秒）(&Q)
dataset.connection.autocommit.false=假
dataset.connection.autocommit.true=真
dataset.connection.isolationmode.read-uncommitted=未提交读取
dataset.connection.isolationmode.read-committed=已提交读取
dataset.connection.isolationmode.repeatable-read=可重复读取
dataset.connection.isolationmode.serializable=可串行
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=ODA 连接工厂
