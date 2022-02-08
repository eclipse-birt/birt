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
MongoDBDataSourceWizardPage.message.default=Mongo 数据库数据源连接属性

MongoDBDataSourceWizardPage.GroupTitle.URIElements=Mongo 数据库 URI 元素

MongoDBDataSourceWizardPage.text.label.ServerHost=服务器主机(&H):

MongoDBDataSourceWizardPage.text.label.ServerPort=服务器端口(&O):

MongoDBDataSourceWizardPage.text.label.DatabaseName=数据库名称(&D):

MongoDBDataSourceWizardPage.text.label.UserName=用户名(&S):

MongoDBDataSourceWizardPage.text.label.Password=密码(&W):

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=Mongo 数据库 URI(&M):

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=补充客户端设置(&C)

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=使套接字保持活跃状态(&A)

MongoDBDataSourceWizardPage.checkbox.label.RequestSession=一致的请求会话(&R)

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=缺少必需的连接属性！

#
MongoDBDataSetWizardPage.label.DBName=Mongo 数据库(&G):

MongoDBDataSetWizardPage.label.DBCollection=MongoDB 集合(&C)

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=包括系统集合(&I)

MongoDBDataSetWizardPage.Button.Refresh=刷新(&R)

MongoDBDataSetWizardPage.label.DocumentNumber=文档编号以搜索字段(&M):

MongoDBDataSetWizardPage.group.text=命令操作(&O)

MongoDBDataSetWizardPage.label.OperationType=命令类型(&T):

MongoDBDataSetWizardPage.Button.FindFields=查找字段(&D)

MongoDBDataSetWizardPage.Button.text.CommandExpression=表达式(&E)...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=编辑命令表达式。

MongoDBDataSetWizardPage.Button.QueryExpression=$查询表达式(&Q)...

MongoDBDataSetWizardPage.Button.SortExpression=排序表达式(&S)...

MongoDBDataSetWizardPage.Button.AdvancedSettings=高级设置(&V)...

MongoDBDataSetWizardPage.label.AvailableFields=可用字段(&A):

MongoDBDataSetWizardPage.label.SelectedFields=选定字段(&S):

MongoDBDataSetWizardPage.HeaderText.SelectedFields=字段显示名称

MongoDBDataSetWizardPage.menuItem.remove=删除(&M)

MongoDBDataSetWizardPage.menuItem.removeAll=全部删除(&L)

MongoDBDataSetWizardPage.message.default=选择数据集列的所需字段

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=确认以继续

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=选择其它集合会放弃当前“选定字段”。  是否要继续?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=确认以刷新

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=是否要根据更改的“命令表达式”刷新“可用字段”?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=错误

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=无法从 MongoDB 数据源获取集合列表。

MongoDBDataSetWizardPage.ExceptionDialog.title=错误

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=找不到可用字段。

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=无活动数据源连接。

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=更新 MongoDB 数据集时出错。

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=命令表达式无效。

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=查询表达式无效。

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=排序表达式无效。

MongoDBDataSetWizardPage.message.error.MissingCollectionName=集合名称不能为空。

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=为文档数输入一个整数值以查找字段

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=选定的字段“{0}”不存在。

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=命令表达式不得为空。

MongoDBDataSetWizardPage.message.error.MissingSelectedField=必须选择至少一个字段。

MongoDBAdvancedSettingsDialog.dialogTitle=高级数据集运行时属性

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=一批中最大的结果对象数(&B):

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=一个批次中 MongoDB cursor 提取\n的最大结果对象数。  默认值 = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=要搜索元数据的最大文档数(&M):

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=在运行时获取查询结果集元数据\n要处理的文档数。  默认值 = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=要跳过的最大文档数(&K):

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=跳过结果集中的前 n 个文档，整数数据类型。\n默认值 = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=索引提示表达式(&H):

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=多个索引键中的一个，要求该查询必须用于查询优化。\n使用 Mongo JSON 提示表达式语法列出编入索引的键。

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=查询指针读取首选项

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=模式(&D):

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=查询指针操作的读取首选项模式。

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=标记集(&G):

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=配置成员节点所需的标记集合。\n替代标记集可能指定为数组中的文档。

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=指针未超时(&N)

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=通常，当指针在一段时间内（10 分钟）不活动时，服务器便会对闲置指针做超时处理，以防占用过多内存。\n启用此选项可避免这种现象。

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=允许部分结果(&P)

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=与分片功能 (mongos) 一起使用时，如果任何分片失效或群集丢失任何分片，\n则允许分片系统产生部分结果。  如果未启用，则从 mongos 服务器返回一个错误。

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=串行化嵌套集合(&F)

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=用于将嵌套字段集合串行化成多个数据集行的用户选项。

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=“最大结果对象数”需要一个整数值!

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=“要搜索的最大文档数”需要一个整数值!

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=“要跳过的最大文档数”需要一个整数值!

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=重置设计设置

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=确定

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=已重置“搜索字段的文档数”的设计时值，以与“搜索元数据的最大文档数”运行时属性值相匹配。

MongoDBExpressionBuilder.dialogTitle.default=表达式生成器

MongoDBExpressionBuilder.ExprssionText.label=表达式文本(&X)：

MongoDBExpressionBuilder.Button.import=导入(&M)...

MongoDBExpressionBuilder.Button.tooltip.import=从本地文件导入表达式文本

MongoDBExpressionBuilder.Button.export=导出(&E)...

MongoDBExpressionBuilder.Button.tooltip.export=将表达式文本导出到本地文件

MongoDBExpressionBuilder.Button.ValidateSyntax=验证语法(&V)

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=验证表达式文本的语法。

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=表达式文本验证

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=表达式语法有效。

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=表达式语法无效。

MongoDBExpressionBuilder.CommandExpression.DialogTitle=命令表达式生成器

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=表达式文本不得为空!

MongoDBExpressionBuilder.QueryExpression.DialogTitle=查询表达式生成器

MongoDBExpressionBuilder.SortExpression.DialogTitle=排序表达式生成器





