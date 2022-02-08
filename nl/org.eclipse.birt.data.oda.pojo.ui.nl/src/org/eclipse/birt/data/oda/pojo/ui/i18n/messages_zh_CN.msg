###############################################################################
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation  - initial API and implementation
###############################################################################
# data source messages
DataSource.RuntimeProperty=运行时属性(&R)

DataSource.DataSetClassPath=自定义 POJO 数据集类的共享位置(&L)

DataSource.AddRelativeJars0=添加 Jar (相对路径)(&A)...

DataSource.AddAbsoluteJars0=添加 Jar (绝对路径)(&J)...

#To avoid duplicate short-cut keys
DataSource.AddRelativeJars1=添加 Jar (相对路径)(&S)...

DataSource.AddAbsoluteJars1=添加 Jar (绝对路径)(&B)...

DataSource.AddRelativeClassFolder1=添加类文件夹 (相对路径)(&E)...

DataSource.AddAbsoluteClassFolder1=添加类文件夹 (绝对路径)(&U)...

DataSource.button.tooltip.AddJar.TwoItems=向类路径添加 Jar。\n相对路径: 基于资源文件夹的相对路径。\n绝对路径: 文件系统的绝对路径。

DataSource.button.tooltip.AddJar.OneItems=向类路径添加 Jar。\n绝对路径: 文件系统的绝对路径。

DataSource.PojoClassPath=POJO 类的共享位置(&O)

DataSource.MissDataSetPojoClassPath.runtime=缺少自定义 POJO 数据集类的运行时共享位置。

DataSource.MissDataSetPojoClassPath.designtime=缺少设计时使用的自定义 POJO 数据集类的共享位置。

DataSource.PageMessage=POJO 数据源属性

DataSource.POJOClasses.tab.runtime=运行时属性(&R)

DataSource.POJOClasses.tab.designTime=设计时属性(&P)

DataSource.POJOClassTabFolderPage.promptLabel.runtime=自定义 POJO 数据集类的共享位置(&L)

DataSource.POJOClassTabFolderPage.promptLabel.designtime=自定义 POJO 类的共享位置(&L)

DataSource.POJOClassTabFolderPage.button.edit=编辑(&E)...

DataSource.POJOClassTabFolderPage.button.up=上移(&U)

DataSource.POJOClassTabFolderPage.button.down=下移(&D)

DataSource.POJOClassTabFolderPage.button.remove=删除(&V)

DataSource.POJOClassTabFolderPage.menuButton.addJar=添加 Jar

DataSource.POJOClassTabFolderPage.menuButton.item.relativePath=相对路径

DataSource.POJOClassTabFolderPage.menuButton.item.absolutePath=绝对路径

DataSource.POJOClassTabFolderPage.error.title.empty.resourceIdentifier=错误

DataSource.POJOClassTabFolderPage.error.message.empty.resourceIdentifier=无法获得应用程序资源基本 URI！

DataSource.ClassPathPage.synchronize.checkbox.message=运行时属性可用于运行时和设计时

DataSource.ClassPathPage.synchronize.checkbox.tooltip=如果勾选此选项，设计时属性编辑器将禁止编辑。

DataSource.ClassPathPage.synchronize.PrompMessage=注意：如果勾选此选项，将忽略设计时属性设置。

DataSource.ClassPathPage.testConnection.failed.runtime=无法创建与运行时属性的“{0}”的连接！

DataSource.ClassPathPage.testConnection.failed.designtime=无法创建与设计时属性的“{0}”的连接！


# data set messages
DataSet.PojoDataSetClass=POJO 数据集类名称(&P)

DataSet.AppContextKey=应用程序上下文关键字(&A)

DataSet.PojoRootClass=POJO 类名称(&C)

DataSet.Browse=浏览(&B)...

DataSet.Browse.tooltip=浏览以选择一个类。

#To avoid duplicate short-cut key
DataSet.Browse1=浏览(&R)...

DataSet.DataType.Object=Java 对象

DataSet.FailedParseQueryTitle=无法分析查询文本

DataSet.FailedParseQueryMsg=查询文本:“{0}”。

DataSet.FailedToSaveTitle=无法保存数据集

DataSet.FailedToSaveMsg=保存数据集时发生异常。

DataSet.Label.MethodNameFilter=方法名称筛选器(? = 任意字符，* = 任意字符串)(&M):

DataSet.MissPojoRootClass=未提供 POJO 类。

DataSet.Apply=应用(&Y)

DataSet.ClassStructure=POJO 类结构

DataSet.ColumnMapping=列映射(&C)

DataSet.Add=添加(&A)...

DataSet.Edit=编辑(&E)...

DataSet.Remove=删除(&R)

DataSet.Up=上移(&U)

DataSet.Down=下移(&D)

DataSet.Label.POJOClassName=POJO 类名称

DataSet.Label.prompt.ClassName=选择创建数据集列的方法和字段(&F)。

DataSet.button.addColumn.tooltip.mapColumn=添加要选作列的项目。

DataSet.button.removeColumn.tooltip.mapColumn=从列表中删除选定项目。

DataSet.button.tooltip.Add=添加新列。

DataSet.button.tooltip.Edit=编辑当前列。

DataSet.button.tooltip.Remove=删除当前列。

DataSet.button.tooltip.Up=上移当前列。

DataSet.button.tooltip.Down=下移当前列。

DataSet.ShowSampleData=显示示例数据(&S)

DataSet.button.tooltip.ShowSampleData=显示示例数据。

DataSet.FailedToLoadClassTitle=无法加载类。

DataSet.FailedToLoadPojoClassMsg=无法加载 POJO 类:“{0}”。

ColumnMappingPage.table.head.columnName=名称

ColumnMappingPage.table.head.MethodField=方法/字段

ColumnMappingPage.table.head.columnType=类型

ColumnMappingWizardPage.error.getQueryText=无法分析原查询文本。

ColumnMappingWizardPage.button.apply.text=应用(&P)

ColumnMappingWizardPage.button.apply.tooltip=应用输入 POJO 类名称以生成方法树。

ColumnMappingDialog.Label.ColumnName=列名称(&C):

ColumnMappingDialog.Label.ColumnMethodField=方法/字段(&M):

ColumnMappingDialog.Label.Type=列类型(&T):

ColumnMappingDialog.table.promptLabel=选择已映射至参数的方法参数(&A):

ColumnMappingDialog.table.head.mapped=映射

ColumnMappingDialog.table.head.name=名称

ColumnMappingDialog.table.head.value=值

ColumnMappingDialog.table.head.dataType=Java 数据类型

ColumnMappingDialog.button.edit=编辑(&E)...

ColumnMappingDialog.button.selectAll=全选(&S)

ColumnMappingDialog.button.deselectAll=取消全选(&D)

ColumnMappingDialog.Label.methodHierarchy=方法调用层次结构:

ColumnMappingDialog.error.DuplicatedColumnName=该列名称已被使用!

ColumnMappingDialog.error.EmptyColumnName=列名称不能为空!

ColumnMappingDialog.error.EmptyMappingPath=列映射路径不能为空!

ColumnMappingDialog.MenuItem.remove=删除

ColumnMappingDialog.MenuItem.removeAll=全部删除

MethodParameterDialog.edit.title=编辑方法参数

MethodParameterDialog.label.name=名称(&N):

MethodParameterDialog.label.value=值(&V):

MethodParameterDialog.label.dataType=Java 数据类型(&J):

MethodParameterDialog.checkBox.message=将该参数设置为映射至参数。

MethodParameterDialog.error.missingName=必须为已映射的参数指定名称!

MethodParameterDialog.error.missingValue=必须为常量参数指定常量值!

MethodParameterDialog.error.invalidParamName=已为该参数指定了一个不同的数据类型!

MethodParameterDialog.error.missingDataType=参数数据类型不能为空!

DataSet.DataType.String=字符串

DataSet.DataType.Integer=整型
#make the display label consistent with BIRT data type 
DataSet.DataType.Double=浮点

DataSet.DataType.Decimal=小数

DataSet.DataType.Date=日期

DataSet.DataType.Time=时间
#make the display label consistent with BIRT data type 
DataSet.DataType.Timestamp=日期时间

DataSet.DataType.Boolean=布尔

DataSet.DataType.Blob=Blob

DataSet.AddColumnMapping=添加列映射

DataSet.EditColumnMapping=编辑列映射

DataSet.InvalidColumnMappingPath=无效方法/字段格式:“{0}”。

DataSet.InvalidMethodName=无效方法名称:“{0}”。

DataSet.InvalidFieldName=无效字段名称:“{0}”。

DataSet.JarsSelectDlg.Title=选择 Jar/Zip

DataSet.ClassFoldersSelectDlg.Title=选择类文件夹

DataSet.PropertyPageMsg=POJO 数据集属性

DataSet.ColumnMappingPageMsg=列映射

DataSet.NoColumnMappingErrorMsg=无列映射。

DataSet.ClassNameFilter=类名称筛选器(? = 任意字符，* = 任意字符串)(&C):

DataSet.MatchingItems=匹配项(&M):

DataSet.ClassInputDialogTitle=选择类

DataSet.PreviewDialogTitle=示例数据

DataSet.FailedFetchSampleData=无法获取示例数据

ExceptionDialog.title.erro=错误

ExceptionDialog.DataSource.AddDuplicatedJar=不能多次添加相同 jar。

error.PojoDataSet.emptyClassName=类名称不能为空。

JarsSelectionDialog.button.importFile=导入文件...

JarsSelectionDialog.title.error=错误

JarsSelectionDialog.title.overwrite=覆盖

JarsSelectionDialog.overwrite.msg=具有此名称的文件已经存在，是否要覆盖它?

JarsSelectionDialog.import.msg=正在导入文件...
