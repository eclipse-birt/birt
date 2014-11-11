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
plugin.name=MongoDB 数据源 ODA 运行时驱动程序
data.source.name=MongoDB 数据源
data.set.name=MongoDB 数据集
connection.profile.name=MongoDB 数据源连接配置文件

# Data source properties
datasource.property.mongoDbUri=Mongo 数据库 URI(&M)
datasource.property.ignoreURI=忽略 URI(&I)
datasource.property.serverHost=服务器主机(&H)
datasource.property.serverPort=服务器端口(&O)
datasource.property.databaseName=数据库名称(&D)
datasource.property.userName=用户名(&S)
datasource.property.password=密码(&W)
datasource.property.socketKeepAlive=使套接字保持活跃状态(&A)
datasource.property.useRequestSession=一致的请求会话(&R)
datasource.property.group.uriElements=Mongo 数据库 URI 元素(&E)
datasource.property.group.options=补充的客户端设置(&S)

# Data set properties - query definition
dataset.property.collectionName=集合名称(&C)
dataset.property.operationType=命令操作类型(&O)
dataset.property.operationExpr=命令表达式(&E)
dataset.property.selectedFields=选择的字段(&L)
dataset.property.findQueryExpr=查询表达式(&Q)
dataset.property.sortExpr=排序表达式(&S)
# Data set properties - advanced runtime properties
dataset.property.batchSize=一批中最大的结果对象数(&B)
dataset.property.rtMDSearchLimit=要搜索运行时和元数据的最大文档数(&M)
dataset.property.numSkipDocuments=要跳过的最大文档数(&K)
dataset.property.indexHints=索引提示表达式(&H)
dataset.property.queryReadPreference=查询指针读取首选项(&R)
dataset.property.queryReadPreferenceTags=读取首选项标记集(&G)
dataset.property.noTimeOut=指针未超时(&N)
dataset.property.allowsPartialResults=允许部分结果(&P)
dataset.property.flattenCollections=串行化嵌套集合(&F)
#
dataset.property.value.readPrefPrimary=主要
dataset.property.value.readPrefPrimaryPreferred=首选主要项
dataset.property.value.readPrefSecondary=次要
dataset.property.value.readPrefSecondaryPreferred=首选次要项
dataset.property.value.readPrefNearest=最近
