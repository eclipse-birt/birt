#************************************************************************
# Copyright (c) 2004, 2008 Actuate Corporation.
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
Error.Msg001={0} 在 {0} 中未找到。

###########################################################

# DTE error code
data.engine.UndefinedDataSource=未定义数据源: {0}。
data.engine.UndefinedDataSet=未定义数据集: {0}。
data.engine.UnsupportedDataSourceType=数据源 "{0}" 属于不支持的类型。
data.engine.UnsupportedDataSetType=数据集 "{0}" 属于不支持的类型。
data.engine.UnsupportedGroupOnClobAndBLOB=不支持对 CLOB 或 BLOB 数据 "{0}" 分组。
data.engine.UnsupportedSortOnClobAndBLOB=不支持对 CLOB 或 BLOB 数据 "{0}" 排序。

data.engine.EndOfGroupOnly=此聚合表达式仅可用于组结尾。
data.engine.InvalidAggregation=The aggregation definition is invalid:{0}.
data.engine.InvalidAggrGroup=无效的聚合函数分组级别: {0}。
data.engine.WrongNumAggrArgs=错误的聚合函数参数数: {0}。
data.engine.AggrParamGroupLevelInconsistent=In nested aggregation, all the non-constant expression must have same group level.
data.engine.DupPropertyName=找到重复属性名称: {0}。
data.engine.NoSubQueryName=无法找到具有以下名称的子查询: {0}。

data.engine.DataSourceMissingExtId=数据源 {0} 的定义中缺少扩展 ID。
data.engine.NullGroupStart=组起始值不能为空。
data.engine.UnsupportedDirectNestedAggregate=不支持聚合的直接嵌套。

data.engine.ResultClosed=QueryResults 或其迭代函数已关闭。
data.engine.NoCurrentRow=结果迭代函数没有当前行。它尚未启动或已关闭。
data.engine.InvalidJSExpr=无效的 Javascript 表达式: {0}。
data.engine.InvalidTotalFuncName=无效的总计函数名称: {0}。
data.engine.InvalidTotalFunc=无效的总计函数。
data.engine.NoAggrFunc=此表达式中不能出现聚合函数调用。
data.engine.InvalidExprHandle=无效的表达式句柄。
data.engine.InvalidAggrGroupExpression=错误的聚合函数组表达式: {0}。
data.engine.InvalidSortKeyColumn=无效的排序键列: {1}。
data.engine.InvalidGroupLevel= 聚合中的组级别: {0} 无效。 
data.engine.InvalidGroupKeyName=无效的组键列名称: {0}。
data.engine.BadCloseScript=对 CustomDataSet 中的 closeScript 求值失败。
data.engine.InvalidCustomFieldIdx=无效的自定义字段索引: {0}。
data.engine.InvalidGroupName=组名称无效。
data.engine.DuplicateGroupName=组名称重复。
data.engine.BadOpenScript=对 CustomDataSet 中的 openScript 求值失败。
data.engine.DataSourceIsOpen=数据源已经打开。
data.engine.DataSourceNotOpen=数据源未打开。
data.engine.EmptyCustomFieldName=自定义字段名称不能为空。
data.engine.DupCustomFieldName=重复的自定义字段名称: {0}。
data.engine.NoResultSet=查询执行未返回结果集。
data.engine.NoResultMetadata=结果元数据不可用。
data.engine.QueryAlreadyPrepared=已准备查询。
data.engine.QueryNotPrepared=尚未准备查询。
data.engine.InvalidFieldName=无效的字段名称: {0}。
data.engine.DupColumnName=重复的列名称: {0}。
data.engine.DupAlias=重复的列名称或别名: {0}。
data.engine.InvalidFieldIndex=无效的字段索引: {0}。

data.engine.MatchError=检查模式语法。将字符串与模式匹配时出错: {0}。

data.engine.ExpectBooleanReturnType=脚本化数据集的 {0} 脚本必须返回布尔结果。
data.engine.BadIntervalUnit=无效的间隔单位值: {0}。
data.engine.InvalidFieldName=无效的字段名称: {0}。
data.engine.BadGroupKeyType=组键具有不支持的数据类型: {0}。
data.engine.group.interval=不支持在类型为 {1} 的列中设置 {0} 间隔。
data.engine.group.interval.Rowid=数字间隔是 ROWID 列中唯一支持的间隔。
data.engine.group.interval.invalid=该指定间隔无效。
data.engine.group.intervalRange.invalid=无效间隔范围: {0}。
data.engine.NoSupportFilterOnGroup=当前不支持对组执行筛选。
data.engine.InvalidTopBottomArgument=顶端/底端筛选器参数必须为数字。
data.engine.InvalidTopBottomNArgument=TopN/BottomN 筛选器的参数必须为零或正整数。
data.engine.InvalidTopBottomPercentArgument=TopPercent/BottomPercent 筛选器的参数必须是正数且小于或等于 100；

data.engine.UnsupportedCondOp=不支持条件运算符: {0}。
data.engine.EmptyExpression=表达式不能为 null 或空。
data.engine.AggregationBinding.EmptyArgument=聚合绑定“{1}”需要参数“{0}”。

data.engine.InvalidJoinType=无效的联接类型。
data.engine.InvalidJoinOperator=无效的联接运算符。
data.engine.BadDataSetParamName=未定义的数据集参数: {0}。
data.engine.FailComputeOutputParameterValue=无法判定输出参数值: {0}。
data.engine.LinkedReportParamAllowMultiValues=允许多个值的报表参数“{0}”不能用于链接数据集参数“{1}”。

#column binding
data.engine.EmptyBindingName=绑定名称不能为空。 
data.engine.InvalidBoundColumnName=无效的绑定列名称: {0}。
data.engine.InvalidGroupKey=组 {1} 中的组关键字“{0}”不能依赖于在低于自身的组中定义的列绑定。 
data.engine.ColumnBindingNotExist=列绑定“{0}”不存在。
data.engine.ColumnBindingCycle=绑定“{0}”具有相关性循环。 
data.engine.ColumnBindingReferToInexistColumn=列绑定“{0}”引用了一个不存在的绑定“{1}”。
data.engine.ColumnBindingReferToInexistBinding=Column binding "{0}" has referred to a data set column "{1}" which does not exist.
data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=列绑定“{0}”不正确: 包含聚合的父查询列绑定不能用于子查询的列绑定。
data.engine.ReadColumnValueFromDocumentError=尝试读取列 {0} 的值时出错。这可能由绑定表达式的判定中的运行时错误(即除数为 0 错误)引起。
data.engine.NoOuterResultsExist=外部查询结果不存在。这可能是由没有父查询的查询中的“_outer”引用引起的。
data.engine.DuplicatedBindingName=重复的绑定名称: {0}。
#computed column value
data.engine.BadCompColExpr=计算列 "{0}" 的表达式无效。
data.engine.CompCol.FailRetrieveValueComputedColumn=未能为已计算的列“{0}”计算值。
data.engine.ScriptResult.WrongSystemComputedColumn=未能根据排序、组或筛选表达式计算值。
data.engine.ComputedColumnCycle=计算列“{0}”具有相关性循环。

data.engine.SortOnAggregation=存在排序关键字引用了聚合“{0}”的无效排序。

data.engine.BadFilterExpr=筛选表达式 "{0}" 无效。
data.engine.BadFilterDefn=使用表达式“{0}”的筛选定义无效。

#clob and blob error
data.engine.opencloberror=打开 CLOB 字符流时出错。
data.engine.readcloberror=读取 CLOB 字符流时出错。
data.engine.openbloberror=打开 BLOB binaryStream 流时出错。
data.engine.readbloberror=读取 BLOB binaryStream 流时出错。

#resultsetcache exception
data.engine.BadDestIndex=destIndex 的值 {0} 无效。该值应介于 0 和 {1} 之间。
data.engine.WriteTempError=为大数据集写入临时文件时出现异常。
data.engine.ReadTempError=为大数据集读取临时文件时出现异常。

#resultiterator cache exception
data.engine.CreateCacheTempError=创建用于缓存结果行的临时文件时出现例外。
data.engine.OpenCacheTempError=打开用于缓存结果行的临时文件时出现例外。
data.engine.WriteCacheTempError=写入用于缓存结果行的临时文件时出现例外。
data.engine.ReadCacheTempError=读取用于缓存结果行的临时文件时出现例外。
data.engine.CloseCacheTempError=关闭用于缓存结果行的临时文件时出现例外。

#load data engine factory exception
load.factory.error=加载数据引擎工厂时出错。

# Wrapped Error
data.engine.DataConversionError=转换数据类型时出错。
data.engine.BadDataTypeCondExpr=在条件表达式中发现了无效数据类型。
data.engine.BirtException=发生 BIRT 例外：{0}。查看下一个例外以了解更多信息。

# data set cache error
data.engine.datasetcache.save.error=保存数据源缓存的文件时出错。
data.engine.datasetcache.load.error=加载数据源缓存的文件时出错。

# error related with data engine context
data.engine.InvalidMode=数据引擎上下文的模式值 {0} 无效。
data.engine.InvalidArchive=在生成或展示中，报表存档不能为空。 
data.engine.SaveReportDocumentError=保存时出现报表文档错误: {0}。
data.engine.LoadReportDocumentError=加载时出现报表文档错误: {0}。
data.engine.GetSaveStreamError=获取保存流时出现报表文档错误。
data.engine.GetLoadStreamError=获取加载流时出现报表文档错误。
data.engine.NullExpr=要检索展示中的值，表达式不能为空。
data.engine.InValidExpr=表达式无效，无法检索展示中的值。
data.engine.BadGroupExpr=组表达式不能为空。 
data.engine.filterExprContainRowNum=筛选表达式不能引用 java 脚本对象 "row.__num"。
data.engine.sortExprContainRowNum=排序表达式不能引用 java 脚本对象 "row.__num"。


data.engine.ResultSetNotStart=结果集尚未启动。
data.engine.GetLevelMemberError=无法实现该级别成员，原因是尚未启动结果集。

data.engine.invalidRowIndex=无效的行索引值: {0}。
data.engine.backwardSeekError=此版本不支持反向搜寻。
data.engine.startError=应首先调用下一个方法。

# query running based on report document
data.engine.rd.errorfilters=查询中定义的筛选器不受支持。

# other error
data.engine.ScriptEvalError=评估脚本时出错(源:{0}，行:{1}): {2}。
data.engine.PreparedQueryClosed=PreparedQuery 实例已关闭。
data.engine.NullDefaultInParamValue=输入参数的默认值不能为 null。
data.engine.BadGroupKeyExpr=组键表达式 {0} 无效。
data.engine.GroupNotExist=列 {1} 中使用的组“{0}”不存在。
data.engine.BadSortKeyExpr=排序关键字表达式 {0} 无效。
data.engine.NoParamMetaForDataSource=无法获取此类型数据源的参数元数据。
data.engine.UnexpectedError=由于意外的例外，数据引擎操作失败。 
data.engine.ConvertDataTypeError=无法将参数值 {0} 转换成类型 {1}。
data.engine.CannotInstantiateAggregationFactory=无法实例化聚合工厂 {0}。
date.engine.DuplicateAggregationName=重复的聚合名称: {0}。
data.engine.InconsistentAggregationArgumentDefinition=聚合扩展实现中定义的聚合参数与 plugin.xml 中定义的聚合参数不一致。
data.engine.incorrectGroupKeyValues=给定的组键值数组不正确。
data.engine.NoExpressionProcessorAvailable=没有可用的 ExpressionProcessor。
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} 无法转换为 {1}。
data.engine.NoRowUpdate=此时无法更新数据行。
data.engine.BadDataType=数据类型不受支持 {0}。
data.engine.BadDataExpression=数据表达式不能为空。
data.engine.aggregation.InvalidBindingExpression=聚合绑定“{0}”的不可接受的表达式。
data.engine.InvalidExpression=无效表达式“{0}”。

data.engine.cacheFunctionWrongMode=缓存功能仅适用于 DIRECT_PRESENTATION 模式。
data.engine.document.invalidIndexArray=索引数组无效。
data.engine.document.invalidGroupItem=删除的组项不正确。
data.engine.resultIteratorNotOpen=结果迭代函数尚未打开。
data.engine.resultIteratorClosed=结果迭代函数已关闭。
data.engine.invalidOutputParameterIndex=无效的输出参数索引: {0}。
data.engine.outputParameterOutOfBound=输出参数索引超出边界: {0}。
data.engine.invalidOutputParameterName=无效的输出参数名称: {0}。
data.engine.document.notSupportInPresentation=在展示中不受支持。
data.engine.wrongStatus=错误状态。
data.engine.wrongVersion=在早于 2.1 的版本中不受支持。
data.engine.unsupportOperationException=不支持的操作。
data.engine.possibleMixedDataTypeInColumn=填充数据期间出错。原因可能是在 "ANY" 类型的列中包含混合类型的数据。
data.engine.resultClassSaveError=找不到以下列引用: {0}。
data.engine.invalidMemBufferSize=内存缓冲区大小“{0}”无效。内存缓冲区大小必须为大于零的整数。
data.engine.document.error.cannotLoadStream=文档流“{0}”无效。
data.engine.failToCreateTempDir=无法在“{0}”中创建临时目录； 
data.engine.reportItem.SubQuery.Unsupported=报表项共享不能用于子查询。
data.engine.emptyResultSet=结果集为空。
data.engine.errorHappenWhenRetrieveResultSet=访问结果集“{0}”时出错。指定的结果集可能不存在。
data.engine.invalidAggregationBindingForPLSReport=启用页面级别安全时，聚合绑定“{0}”无效。

# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=列名称不能为空或 null。
odaconsumer.ColumnPositionIsOneBased=Column position is 1-based and cannot be less than 1.
odaconsumer.ColumnAliasCannotBeEmpty=列别名不能为空字符串。
odaconsumer.CannotProcessDriverConfig=无法处理 {0} 驱动程序的配置。
odaconsumer.OdaInitEntryNotFound=无法找到或实例化驱动程序 {0} 的初始入口点。
odaconsumer.OdaDriverOnUnsupportedPlatform=驱动程序 {0} 不支持 {1}。
odaconsumer.CannotGenerateUrl=无法生成类加载器的 URL {0}。
odaconsumer.UnsupportedNativeType=驱动程序 {2} 的数据集类型 {1} 的本地类型 {0} 不受支持。
odaconsumer.UnsupportedDataSetType=驱动程序 {1} 不支持数据集类型 {0}。
odaconsumer.CannotDetermineDefaultDataSetType=空数据集类型。无法确定驱动程序 {0} 的默认数据集类型。
odaconsumer.InvalidDataSetType=无效的数据集类型 {0}。无法确定数据集 {1} 的默认数据集类型。
odaconsumer.ParameterNameCannotBeEmptyOrNull=参数名称不能为空或 null。
odaconsumer.ParameterPositionIsOneBased=参数位置不能小于 1。
odaconsumer.CannotGetParameterNativeName=无法获得参数的驱动程序定义名称: {0}。
odaconsumer.CannotGetParameterType=无法获得参数的类型: {0}。
odaconsumer.CannotGetParameterTypeName=无法获得参数的类型名称: {0}。
odaconsumer.CannotGetParameterMode=无法获得参数的模式: {0}。
odaconsumer.CannotGetParameterScale=无法获得参数的进位制: {0}。
odaconsumer.CannotGetParameterPrecision=无法获得参数的精度: {0}。
odaconsumer.CannotGetParameterIsNullable=无法确定参数是否可为空: {0}。
odaconsumer.SameParamNameForDifferentHints=定义数据集中的另一个参数时使用了相同的名称 {0}。
odaconsumer.DifferentParamNameForSamePosition=定义数据集中的另一个参数 {0} 时使用了相同的位置 {1}。
odaconsumer.ParameterValueIsNull=参数值为 null。
odaconsumer.UnsupportedParameterValueType=不支持的参数值类型: {0}
odaconsumer.UnknownExceptionThrown=在最后一次调用 setter 时产生未知例外。
odaconsumer.CannotConvertIndexedParameterValue=无法将索引 {1} 处的参数值 {0} 从值类型 {2} 转换为 ODA 类型 {3}。
odaconsumer.CannotConvertNamedParameterValue=无法将具有参数名 {1} 的参数值 {0} 从值类型 {2} 转换为 ODA 类型 {3}。
odaconsumer.UnrecognizedProjectedColumnName=无法识别计划的列名称:{0}
odaconsumer.ColumnNameOrAliasAlreadyUsed=在索引 {1} 处，新的列名称或别名 {0} 已经由列使用。
odaconsumer.NamedResultSetsUnsupported=此语句不支持已命名的结果集。
odaconsumer.OutputParameterUnsupported=此语句不支持输出参数。
odaconsumer.UnsupportedMultipleResultSets=此语句不支持多个结果集。
odaconsumer.CannotGetConnectionMetaData=无法获取连接元数据。
odaconsumer.CannotGetMaxConnections=无法获取数据源的最大连接数。
odaconsumer.CannotGetMaxStatements=无法获取数据源的最大查询数。
odaconsumer.CannotGetDataSetMetaData=无法从数据源获得数据集元数据 {0}。
odaconsumer.CannotPrepareStatement=未能为数据集类型 {1} 准备以下查询。\n[{0}]
odaconsumer.CannotCloseConnection=无法关闭连接。
odaconsumer.CannotOpenConnection=无法打开驱动程序的连接: {0}。
odaconsumer.CannotDetermineSupportForMultipleOpenResults=无法确定多个打开结果的支持
odaconsumer.CannotDetermineSupportForMultipleResultSets=无法确定多个结果集的支持。
odaconsumer.CannotDetermineSupportForNamedResultSets=无法确定已命名结果集的支持。
odaconsumer.CannotDetermineSupportForNamedParameters=无法确定已命名参数的支持。
odaconsumer.CannotDetermineSupportForInParameters=无法确定输入参数的支持。
odaconsumer.CannotDetermineSupportForOutParameters=无法确定输出参数的支持。
odaconsumer.CannotSetStatementProperty=无法设置语句属性。
odaconsumer.CannotSetSortSpec=无法设置排序规范。
odaconsumer.CannotSetMaxRows=无法设置最大行数。
odaconsumer.CannotGetResultSetMetaData=无法获取结果集元数据。
odaconsumer.CannotGetMetaDataForNamedResultSet=无法获得指定结果集的元数据: {0}。
odaconsumer.CannotExecuteStatement=无法执行语句。
odaconsumer.CannotGetResultSet=无法获取结果集。
odaconsumer.CannotGetNamedResultSet=无法获得指定结果集: {0}。
odaconsumer.CannotGetMoreResults=无法从语句获得更多结果集。
odaconsumer.CannotFindOutParameter=无法找到输出参数: {0}。
odaconsumer.CannotCloseStatement=无法关闭语句。
odaconsumer.CannotCancelStatement=无法取消语句。
odaconsumer.CannotGetColumnCount=无法获取列计数。
odaconsumer.CannotGetColumnName=无法获得列的名称: {0}。
odaconsumer.CannotGetColumnLabel=无法获得列的标签: {0}。
odaconsumer.CannotGetColumnType=无法获得列的类型: {0}。
odaconsumer.CannotFetchNextRow=无法获取下一个数据行。
odaconsumer.CannotDetermineWasNull=无法确定最后检索到的值是否为 null。
odaconsumer.CannotGetIntFromColumn=无法从列中获得整数值: {0}。
odaconsumer.CannotGetDoubleFromColumn=无法从列中获得双精度值: {0}。
odaconsumer.CannotGetStringFromColumn=无法从列中获得字符串值: {0}。
odaconsumer.CannotGetBigDecimalFromColumn=无法从列中获得十进制值: {0}。
odaconsumer.CannotGetDateFromColumn=无法从列中获得日期值: {0}。
odaconsumer.CannotGetTimeFromColumn=无法从列中获得时间值: {0}。
odaconsumer.CannotGetTimestampFromColumn=无法从列中获得时间戳值: {0}。
odaconsumer.CannotGetClobFromColumn=无法从列中获得 CLOB 值: {0}。
odaconsumer.CannotGetBlobFromColumn=无法从列中获得 BLOB 值: {0}。
odaconsumer.CannotGetBooleanFromColumn=无法从列中获得布尔值: {0}。
odaconsumer.CannotGetRowPosition=无法获取当前行位置。
odaconsumer.CannotCloseResultSet=无法关闭结果集。
odaconsumer.CannotGetParameterCount=无法获取参数计数。
odaconsumer.CannotGetIntFromParameter=无法从参数中获得整数值: {0}。
odaconsumer.CannotGetDoubleFromParameter=无法从参数中获得双精度值: {0}。
odaconsumer.CannotGetStringFromParameter=无法从参数中获得字符串值: {0}。
odaconsumer.CannotGetBigDecimalFromParameter=无法从参数中获得十进制值: {0}。
odaconsumer.CannotGetDateFromParameter=无法从参数中获得日期值: {0}。
odaconsumer.CannotGetTimeFromParameter=无法从参数中获得时间值: {0}。
odaconsumer.CannotGetTimestampFromParameter=无法从参数中获得时间戳值: {0}。
odaconsumer.CannotGetBlobFromParameter=无法从参数中获得 BLOB 值: {0}。
odaconsumer.CannotGetClobFromParameter=无法从参数中获得 CLOB 值: {0}。
odaconsumer.CannotGetBooleanFromParameter=无法从参数中获得布尔值: {0}。
odaconsumer.CannotFindInParameter=无法找到输入参数: {0}。
odaconsumer.CannotSetIntParameter=无法将整数值({0})设置为参数 {1}。
odaconsumer.CannotSetDoubleParameter=无法将双精度值({0})设置为参数 {1}。
odaconsumer.CannotSetStringParameter=无法将字符串值({0})设置为参数 {1}。
odaconsumer.CannotSetBigDecimalParameter=无法将十进制值({0})设置为参数 {1}。
odaconsumer.CannotSetDateParameter=无法将日期值({0})设置为参数 {1}。
odaconsumer.CannotSetTimeParameter=无法将时间值({0})设置为参数 {1}。
odaconsumer.CannotSetTimestampParameter=无法将时间戳值({0})设置为参数 {1}。
odaconsumer.CannotSetBooleanParameter=无法将布尔值({0})设置为参数 {1}。
odaconsumer.CannotSetNullParameter=无法将空值设置为参数 {0}。
odaconsumer.CannotClearInParameters=无法清除输入参数值。
odaconsumer.UnrecognizedOdaType=无法识别的 ODA 数据类型: {0}
odaconsumer.CannotGetColumnNativeDataTypeName=无法获得列的本地数据类型名称: {0}
odaconsumer.CannotGetParameterMetaData=无法获取 ODA 驱动程序参数元数据。
odaconsumer.CannotLoadOdaAdapter=无法加载 {0} 驱动程序；缺少 DTP 至 BIRT ODA 适配器插件 {1}。
odaconsumer.MustSpecifyColumnName=添加列提示时必须指定列名称。
odaconsumer.CannotFindLogDirectory=无法找到日志目录。
odaconsumer.ParameterNames=\n定义参数时使用了模型名称({0})和驱动程序定义名称({1})。
odaconsumer.InvalidArgument=使用无效的参数 ({1}) 调用了方法 ({0})。

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=调用 executeQuery() 后，无法按名称获得结果集。
odaconsumer.manager.CannotSetParamBeforePrepare=在语句准备好之前不能设置输入参数。
odaconsumer.manager.CannotGetParamBeforeExecute=在语句执行之前无法获得输出参数。
odaconsumer.manager.CannotGetParamMdBeforePrepare=在语句准备好之前不能获得参数元数据。
odaconsumer.manager.CannotSetSortSpecBeforePrepare=在语句准备就绪之前不能设置排序规范。
odaconsumer.manager.MaxConcurrentConnectionsReached=达到活动最大并发连接数。
odaconsumer.manager.ConnectionFailed=无法打开连接。
odaconsumer.manager.CannotConstructConnectionFactory=无法创建 ODA 驱动程序的连接工厂。
odaconsumer.manager.InadequatePermissionsForCompatibility=由于权限不足，无法提供反向兼容性。
odaconsumer.manager.ConnectionIsInactive=与数据源建立的连接未处于活动状态。
odaconsumer.manager.MaxConcurrentStatementsReached=此连接已达到最大活动并发语句数。
odaconsumer.manager.PrepareFailed=准备操作失败。
odaconsumer.manager.CannotGetResultSetMdBeforePrepare=在语句准备好之前不能获得结果集元数据。
odaconsumer.manager.CannotExecuteBeforePrepare=在语句准备好之前不能执行该语句。


# Data Olap error code
data.olap.OlapDirCreateFail=无法创建目录 {0}。
data.olap.OlapFileCreateFail=无法创建文件 {0}。
data.olap.OlapFileNotFound=找不到 OLAP 文件 {0}。
data.olap.OlapFileFormatInvalid=文件格式无效。数据对象名称为: {0}。
data.olap.DimensionNotExist=维 {0} 不存在。
data.olap.KeyValueCanNotBeNull=键列的值不能为空。键列: {0}。
data.olap.DetailMemberHaveMultiParent=详细信息成员 {0} 具有多个父项。
data.olap.OlapFileDataError=数据对象 {0} 中存在无效数据。
data.olap.DocumentObjectAlreadyExists =文档对象已存在。无法创建数据对象: {0}。
data.olap.DocumentObjectNotExists =无法从文档加载多维数据集“{0}”。文档对象不存在。
data.olap.FacttableRowNotDistinct=行在事实表中应各不相同。“{0}”出现多次。
data.olap.FacttableJointColumnNotExist=维中不存在联接列“{0}”。
data.olap.ParameterColumnOfAggregationNotExist=维中不存在参数列“{0}”。
data.olap.FacttableNullMeasureValue=度量列的值为空。“{0}”。
data.olap.InvalidDimensionPositionFacttableRow=维键值“{0}”在维“{1}”中不存在。
data.olap.measureNameNotFound=找不到度量名称“{0}”。
data.olap.levelNameNotFound=找不到级别名称“{0}”。
data.olap.dimensionNameNotFound=找不到维名称“{0}”。
data.olap.ReferencedDimensionNotFound=引用的维定义 {0} 不存在。
data.olap.ReferencedLevelNotFound=引用的级别不存在。
data.olap.cursorPositionError=光标无法定位到目标位置。
data.olap.cursorSeekError=光标无法定位到查找位置。
data.olap.CannotGetMeasureValue=无法在多维数据集中找到度量“{0}”定义，该度量不存在或无效。
data.olap.CannotFindLevel=无法在维“{2}”中的层次结构“{1}”下找到“{0}”级别定义。
data.olap.tryToAddIdenticalAggrGroup=绑定列表“{1}”上的聚合中已存在值“{0}”；
data.olap.NoEdgeDefinitionFound=由于查询既未包含行边缘，也未包含列边缘，查询执行失败。要求至少定义一个边缘。
data.olap.invalidSortDefinition=排序定义无效。 
data.olap.invalidMeasureRef=“{0}”不是有效的度量引用表达式。
data.olap.invalidLevelAttr=属性无效:
data.olap.referencedBindingNotExist=引用的绑定“{0}”不存在!
data.olap.invalidBindingReferToInexitDim=绑定“{0}”引用的级别无效。
data.olap.invalidBindingReferToInexitMeasure=绑定“{0}”引用的度量无效。  
data.olap.invalidBindingMissingAggrFunc=绑定“{0}”的聚合函数丢失。
data.olap.invalidAggrArgument=仅级别属性可以用作聚合参数。
data.olap.axis.level.CantBeNull=列成员级别不能为空!
data.olap.axis.value.CantBeNull=级别\“{0}\”的列成员值不能为空!
data.olap.fetchlimitWarning=取数大小为 {0}，小于整个结果集的长度 [{1}]。
data.olap.failComputeComputedMeasureValue=无法计算已计算的度量。
data.olap.duplicateMeasureName=重复度量名称“{0}”。
data.olap.dataBindingExpressionEmpty=数据绑定表达式不能为 null 或空!
data.olap.missingParentCursor=父结果集不存在。
data.olap.CannotAccessNullDimensionRow=不能访问空维行!
data.olap.NonexistentDimension=不存在的维:
data.olap.NonexistentLevel=不存在的级别: 
data.olap.NonexistentKeyOrAttr=不存在的关键字或属性:
data.olap.UnsupportedFunction=不支持的函数:
data.olap.UnspecifiedBindingName=未指定绑定名称。
data.olap.NotNestAggregationBinding=未多维数据集嵌套的聚合绑定: {0}
data.olap.InvalidNestAggregationExpression=多维数据集嵌套聚合的表达式无效。
data.olap.InvalidNestAggregationOn="{0}" aggregate on in cube nest aggregation "{1}" does not exist in its based aggregation(s).
data.olap.sort.exprError=设置的引用维级别只应包含一个级别!
data.olap.query.noCubeBinding=由于没有可用的多维数据集绑定，无法执行多维数据集查询!

data.cache.UnsupportedIncrementalCacheMode=尚未支持增量缓存的“{0}”模式!
# Configuration file parser error code
data.executor.configFileParser.getModeByID=无法按数据集名称获得增量缓存模式。
data.executor.configFileParser.getTSFormatByID=无法按配置文件中指定的数据集名称分析时间戳的格式。
data.executor.configFileParser.getTimeStampColumnByID=无法按配置文件中指定的数据集名称分析时间戳的格式。
data.executor.configFileParser.getQueryTextByID=无法按配置文件中指定的数据集名称获得查询文本。
data.executor.configFileParser.getParametersByID=无法按配置文件中指定的数据集名称获得参数。

data.aggregation.expression.displayName=表达式
data.aggregation.argumentError=聚合“{1}”的参数“{0}”的表达式为 null 或空!
data.olap.InvalidAggrTypeOnMeasure=多维数据集度量聚合不能为此类型:“{0}”。

#derived dataSet
data.derivedDataSet.columnNotExist=列名称“{0}”不存在!
data.derivedDataSet.UnionQueryTextParseError="无法分析联合查询。 \"{0}\""
data.derivedDataSet.JoinQueryTextParseError="无法分析联接查询。 \"{0}\""
