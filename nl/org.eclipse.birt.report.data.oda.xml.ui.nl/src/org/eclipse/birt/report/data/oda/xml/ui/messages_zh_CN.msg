# wizard dialog messages
wizard.title.selectFolder=XML 数据源
wizard.defaultMessage.selectFolder=定义 XML 文件和架构信息的 URL。
wizard.title.newDataSet=新建数据集
wizard.title.selectXmlFile=选择 XML 数据以便预览
wizard.defaultMessage.selectFile=选择一个示例 XML 源，以便在此会话中启用数据预览。
wizard.title.xPathChoose=定义表映射
wizard.defaultMessage.selectXPath=通过在 XML 结构中选择要用作表的元素并将其添加到行映射中，定义该元素。每个 XML 数据集只能有一个表映射。
wizard.title.selectColumn=定义列映射
wizard.title.defineColumnMapping=通过在 XML 结构中选择一个元素或属性并将其添加到列映射中，定义列映射。 

#preference
designer.preview.preference.numberOfElementPassedToGetSchema=XML 架构树分析中访问的元素数
designer.preview.preference.numberOfElementPassedToGetSchema.errormessage=请输入一个介于 0 和 {0} 之间的数。
designer.preview.preference.columnMappingPage.group.title=列映射页

#table mapping page messages
xPathChoosePage.messages.rowMapping=表映射
xPathChoosePage.messages.xPathExpression=XPath 表达式(&X)
xPathChoosePage.messages.xmlStructure=XML 结构
xPathChoosePage.messages.xmlColumnMapping=列映射

#sample xml data file page messages
xmlDataSetSelectionPage.messages.lineofdata=源数据的行
xmlDataSetSelectionPage.warning.errorReadXMLFile=从文件 [{0}] 读取 XML 数据时出错\n{1}。是否仍要继续?

#column mapping page messages
button.refresh=刷新
menu.menuItem.removeAll=全部删除
menu.menuItem.remove=删除
lable.selectXmlFile=输入 XML 源的 URL，或浏览到包含该数据的文件(&X):
lable.selectXmlSchmaFile=输入 XML 架构的 URL，或浏览到\n 包含该架构的文件。如果没有可用架构，请将此字段留空(&M):
label.preview=预览前(&P)
label.useXMLFileFromDataSource=使用数据源中定义的 XML 文件
RowMappingDialog.title=选择或编辑 XPath 表达式。
RowMappingDialog.info=选择您选择的节点的一个预定义 XPath 表达式 \n 或输入另一个表达式:
ColumnMappingDialog.info.columnName=列名称:
ColumnMappingDialog.info.xPath=XPath 表达式:
ColumnMappingDialog.info.dataType=数据类型:
ColumnMappingDialog.info.dataPreview=表预览
ColumnMappingDialog.prompt.new=在此处新建映射...
ColumnMappingDialog.prompt.maxRow=源数据大于 500 行。仅前 500 行可在此页面中预览。
ColumnMappingDialog.prompt.dialog.title=结果预览

dataset.editor.columnName=名称
dataset.editor.xpathexpression=XPath
dataset.editor.datatype=类型
dataset.editor.rowelement=行元素
dataset.error.populateXMLTree=无法检索 XML 数据源。XML 数据源文件无效或文件不存在。

error.label=错误
error.selectFolder=选择的文件 URL 无效或文件不存在。
error.emptyPath=文件 URL 为空。
error.xpath.canNotContainAttribute=XPath 表达式不能映射到 XML 属性。
error.dataset.maxRowNumberError=max 行应为数字。
error.columnMapping.noMappingColumn=请至少映射一列。
error.file.null=所有 URL 均为空，这意味着您尚未选择任何数据源。是否确定要保持这样？
error.columnMapping.sameColumnName=列名称 [{0}] 已经存在。请使用另一个名称。
error.invalidXpath=XPath 表达式“{0}”无效。

#data types for column mapping
datatypes.dateTime=时间戳
datatypes.decimal=大十进制
datatypes.float=双精度
datatypes.integer=整数
datatypes.string=字符串
datatypes.date=日期
datatypes.time=时间

#column name in column mapping table
columnName.name=名称
columnName.xPathName=XPath
columnName.typeName=类型

dataset.new=新数据集
file.choose=浏览(&B)...
title.warning=警告

datasource.propertyPage.tooltip=XML 数据源
