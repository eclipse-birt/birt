#/*******************************************************************************
# * Copyright (c) 2006, 2012 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# *  Actuate Corporation - initial API and implementation
# *******************************************************************************/

#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

###########################################################
#AbstractDataSetWizard properties
AbstractDataSetWizard.ModelTrans.Create=定义数据集的名称、源和类型
###########################################################
#CssErrDialog
CssErrDialog.shellTitle.ImportCssStyleMssageTitle=导入消息

CssErrDialog.AreaTitle=导入 CSS 样式消息

CssErrDialog.AreaMessage=导入 css 样式时出现某些错误或警告。

CssErrDialog.Severity=严重性：

CssErrDialog.Message=消息:

CssErrDialog.FatalError=致命错误

CssErrDialog.Error=错误

CssErrDialog.Warning=警告

###########################################################
#AddGroupAction properties
AddGroupAction.actionMsg.addGroup=插入组...

AddGroupAction.Menu.Text=插入组...

AddGroupAction.stackMsg.addGroup=插入组

###########################################################
#AddLibraryAction properties
AddLibraryAction.Error.FileNotFound=未找到文件 ''{0}''。

AddLibraryAction.Error.FileIsNotLibrary=文件“{0}”没有以“{1}”结束。

AddLibraryAction.Error.Message=文件已经存在。

AddLibraryAction.Error.Title=错误

AddLibraryAction.Text=添加库...

AddLibraryAction.Dialog.Tilte=选择库

###########################################################
#AddCSSAction properties
AddCSSAction.Text=添加 CSS 文件...

###########################################################
#AddResourceAction properties
AddResourceAction.Text=添加资源(&R)...

AddResourceAction.Dialog.Tilte=选择资源

AddResourceAction.Error.Message=文件已经存在。

AddResourceAction.Error.FileNotFound=未找到文件 ''{0}''。

AddResourceAction.Error.Title=错误

AddResourceAction.Error.FileIsNotResource=文件“{0}”不是资源。

###########################################################
#AddCSSDialog
AddCSSWizardDialog.ShellText=添加 CSS 文件

AddCSSWizardDialog.TitleArea=将 CSS 文件添加到资源文件夹

AddCSSWizardDialog.Message=选择一个 CSS 文件以便将样式导入到资源文件夹

###########################################################
#MoveResourceDialog
MoveResourceDialog.Title=移动资源

MoveResourceDialog.Message=选择文件夹

###########################################################
#NewLibraryDialog
NewLibraryDialog.Title=新建库

NewLibraryDialog.Message=选择文件夹

NewLibraryDialog.label.NewLibrary=新库名

###########################################################
#PublishCSSAction properties
PublishCSSAction.wizard.errorTitle=错误

PublishCSSAction.wizard.message=无法将 CSS 文件发布到它所在的目录，请选择其它目录进行发布。

PublishCSSAction.wizard.msgDirErr=目标目录不存在，但无法创建。

PublishCSSAction.wizard.message.SourceFileNotExist=源文件不存在。

PublishCSSAction.wizard.notvalidfolder=首选项中定义的资源文件夹无效。


###########################################################
#ResourceAction properties
DeleteLibraryAction.Text=删除(&D)

CopyLibraryAction.Text=复制(&C)

PasteLibraryAction.Text=粘贴(&P)

MoveLibraryAction.Text=移动(&V)...

RenameLibraryAction.Text=重命名(&M)

NewFolderAction.Text=文件夹(&F)

NewLibraryAction.Text=库(&L)

NewResource.MenuGroup.Text=新建(&N)

RenameResourceAction.nameExists=具有此名称的文件或文件夹已存在

RenameResourceAction.inputDialogTitle=重命名资源

RenameResourceAction.inputDialogMessage=新名称:

NewFolderAction.nameExists=具有此名称的文件夹已存在

NewFolderAction.emptyName=名称不能为空

NewFolderAction.inputDialogTitle=新建文件夹

NewFolderAction.inputDialogMessage=文件夹名称:

DeleteResourceAction.Dialog.Title=删除资源

DeleteResourceAction.Dialog.Message=确实要删除选定资源吗?

PasteResourceAction.Dialog.Title=粘贴资源

PasteResourceAction.ReadOnlyEncounter.Title=遇到只读文件

PasteResourceAction.ReadOnlyEncounter.Message=文件“{0}”为只读，请首先确保它可以写入。

PasteResourceAction.Dialog.Message=资源存在。 指定新名称(&N):

PasteResourceAction.Dialog.FilenameSuffix.CopyOf=复制

MoveResourceAction.Dialog.Title=移动资源

MoveResourceAction.Dialog.Message=资源已存在。是否要替换?

ResourceAction.ConvertToFile.URLIsNull=URL 为空，无法转换成文件。

ResourceAction.FileRemoveFailure=文件删除失败。

LibraryNotExist.Dialog.Title=打开库

LibraryNotExist.Dialog.Message=库不存在，要刷新资源管理器吗?

DesignerNotExist.Dialog.Title=打开报表设计

DesignerNotExist.Dialog.Message=报表设计不存在，要刷新资源管理器吗?
###########################################################
#LibraryFileFilterAction properties
LibraryFileFilterAction.Text=筛选...

###########################################################
#AddStyleRuleAction properties
AddStyleAction.actionMsg.addStyleRule=新建样式(&N)...

AddStyleAction.stackMsg.addStyle=新建样式(&N)

###########################################################
#ApplyStyleAction properties
ApplyStyleAction.actionLabel.none=无

ApplyStyleAction.stackMsg.applyStyle=应用样式

###########################################################
#ApplyThemeAction properties
ApplyThemeAction.actionLabel.none=无

ApplyThemeAction.stackMsg.applyStyle=应用布景主题

###########################################################
#AttributesBuilder properties
AttributesBuilder.Label.Cell=属性编辑器 - 单元格

AttributesBuilder.Label.Column=属性编辑器 - 列

AttributesBuilder.Label.Data=属性编辑器 - 数据

AttributesBuilder.Label.Generic=属性编辑器 - {0}

AttributesBuilder.Label.Grid=属性编辑器 - 网格

AttributesBuilder.Label.Label=属性编辑器 - 标签

AttributesBuilder.Label.Library=属性编辑器 - 库

AttributesBuilder.Label.List=属性编辑器 - 列表

AttributesBuilder.Label.Cube=属性编辑器 - 多维数据集

AttributesBuilder.Label.DynamicFilterParameter=属性编辑器 - 动态筛选器参数

AttributesBuilder.Label.MasterPage=属性编辑器 - 主页

AttributesBuilder.Label.None=属性编辑器 - 无

AttributesBuilder.Label.Picture=属性编辑器 - 图像

AttributesBuilder.Label.Report=属性编辑器 - 报表

AttributesBuilder.Label.Row=属性编辑器 - 行

AttributesBuilder.Label.Table=属性编辑器 - 表

AttributesBuilder.Label.Text=属性编辑器 - 文本

AttributesBuilder.Label.TextData=属性编辑器 - 动态文本

AttributesBuilder.Label.DataSourceHandle=属性编辑器 - 数据源

AttributesBuilder.Label.VariableElementHandle=属性编辑器 - 变量

AttributesBuilder.Label.DataSetHandle=属性编辑器 - 数据集

AttributesBuilder.Label.ScalarParameterHandle=属性编辑器 - 参数

AttributesBuilder.Label.TemplateReportItemHandle=属性编辑器 - 模板报表项

AttributesBuilder.Label.AutoText=属性编辑器 - 自动文本

AttributesBuilder.Label.ParameterGroup=属性编辑器 - 参数组

AttributesBuilder.Label.CascadingParameterGroup=属性编辑器 - 级联参数组

###########################################################
#AttributeView properties
AttributeView.dialg.Message.Warning=警告

AttributeView.dialg.Message.Yes=是(&E)

AttributeView.dialg.Message.No=否(&O)

AttributeView.dialg.Message.Cancel=取消(&A)

AttributeView.dialg.Message.PromptMsg=是否确实要清除所有本地属性?

AttributeView.toolbar.tooltip.EditingToggleAction.ClearMsg=清除本地属性。

AttributeView.toolbar.tooltip.EditingToggleAction.EnableEditingMsg=启用属性编辑。

AttributeView.toolbar.tooltip.EditingToggleAction.DisabledActionMsg=此操作不适用于本地元素。

AttributeView.view.message.Emptypage= 此 {0} 扩展自库 {2} 中的 {1}。要在此报表设计中修改 {3} 的属性，请单击“属性编辑器”工具栏中的“启用”按钮。

AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg=通过原始库项目还原属性

AttributeView.defaultText.noAttributeView = 属性编辑器不可用。
###########################################################
#BackGroundPreferencePage properties
BackGroundPreferencePage.displayname.Title=背景

###########################################################
#BasAttributePage properties
BasAttributePage.Label.Attributes=属性:

BaseAttributePage.CommandStack.ResetStyle.Message=还原到默认样式

###########################################################
#BaseInsertMenuAction properties
BaseInsertMenuAction.stackMsg.insertElement=插入元素



###########################################################
#BasePaletteFactory properties
BasePaletteFactory.AutoText.Confidential=机密

BasePaletteFactory.AutoTextLabel.CreatedBy=创建者:

BasePaletteFactory.AutoTextLabel.CreatedOn=创建时间:

BasePaletteFactory.AutoTextLabel.Filename=文件名:

BasePaletteFactory.AutoTextLabel.LastPrinted=上次打印时间:

BasePaletteFactory.AutoTextLabel.Page=页:

BasePaletteFactory.AutoTextLabel.PageCount=页数:

BasePaletteFactory.AutoTextLabel.PageXofY=第 X 页，共 Y 页

BasePaletteFactory.categoryName.PointerSelect=选择指针

BasePaletteFactory.categoryName.RectangleSelect=选择矩形

BasePaletteFactory.Group.Items=数据项

BasePaletteFactory.toolTip.PointerSelect=单击要选择的某个项，或按住 Ctrl 键单击要选择的多个项。

BasePaletteFactory.toolTip.RectangleSelect=单击并拖动以选择矩形中的所有项。

###########################################################
#BaseStylePreferencePage properties
BaseStylePreferencePage.displayname.Title=常规

###########################################################
#BgImageFieldEditor properties
BgImageFieldEditor.displayname.Browse=浏览(&W)...

###########################################################
#BindingPage properties
BindingPage.Dataset.Label=数据集(&T):

BindingPage.DataSetBindingSelector.Title.DataSet=数据设置绑定

BindingPage.DataSetBindingSelector.Title.LinkModel=数据模型绑定

BindingPage.ReportItem.Label=报表项(&I):

BindingPage.ReportItem.NoName=无名称

BindingPage.Label.Binding=绑定

BindingPage.MessageDlg.SaveParamBinding=保存 ParamBinding

BindingPage.None=无

BindingPage.TableColumn.DataType=数据类型

BindingPage.TableColumn.Parameter=参数

BindingPage.TableColumn.Value=值

ColumnBingingDialog.Label.Warn=更改数据集或报表项引用会使所有结果集列表达式无效。

ColumnBindingDialog.Label.DataSet=数据集:

ColumnBindingDialog.Column.Expression=表达式

ColumnBindingDialog.DialogTitle=选择数据绑定

ColumnBindingDialog.Column.Name=名称

ColumnBindingDialog.Column.Filter=筛选器

ColumnBindingDialog.Column.Function=函数

ColumnBindingDialog.Column.DisplayName=显示名称

ColumnBindingDialog.Column.DisplayNameID=显示名称 ID

ColumnBindingDialog.InputPrompt=<在此处输入新值...>

ColumnBindingDialog.Button.Generate=生成列绑定

ColumnBindingDialog.Column.DataType=数据类型

ColumnBindingDialog.Column.AggregateOn=聚合条件

ColumnBindingDialog.All=全部

ColumnBindingDialog.NONE=无

ColumnBindingDialog.Choice.DatasetFromContainer=容器的数据集

ColumnBindingDialog.Choice.ReportItemFromContainer=容器的引用报表项

ColumnBindingDialog.AGGREGATEON.NONE=N/A
ColumnBindingDialog.Text.Add=添加(&A)...

ColumnBindingDialog.Text.Edit=编辑(&E)...

ColumnBindingDialog.Text.Del=删除(&R)

ColumnBindingDialog.Text.Refresh=刷新(&F)

ColumnBindingDialog.Text.AddAggr=添加聚合(&G)...

###########################################################
#BlockPreferencePage properties
BlockPreferencePage.displayname.Title=文本块

###########################################################
#BlockPreferencePage properties
SizePreferencePage.displayname.Title=大小

###########################################################
#BorderCanvas properties
BorderCanvas.Label.Inherited=(已继承)

###########################################################
#BorderPreferencePage properties
BorderPreferencePage.displayname.Color=颜色

BorderPreferencePage.displayname.Style=样式

BorderPreferencePage.displayname.Title=边框

BorderPreferencePage.displayname.Width=宽度

BorderPreferencePage.text.Bottom=底部:

BorderPreferencePage.text.Left=左:

BorderPreferencePage.text.Right=右:

BorderPreferencePage.text.Top=顶部:

###########################################################
#BordersPage properties
BordersPage.Label.Borders=边框(&B):

BordersPage.Label.Color=颜色(&C):

BordersPage.Label.Presets=预设:

BordersPage.Label.Style=样式(&Y):

BordersPage.Label.Width=宽度(&T):

BordersPage.Tooltip.All=所有框线

BordersPage.Tooltip.Bottom=下框线

BordersPage.Tooltip.Left=左框线

BordersPage.Tooltip.None=无

BordersPage.Tooltip.Right=右框线

BordersPage.Tooltip.Top=上框线

BordersPage.text.Preview=预览:

BordersPage.Trans.SelectBorder=选择边框

BordersPage.Trans.UnSelectBorder=取消选择边框

BordersPage.Trans.SelectAllborders=选择所有边框

BordersPage.Trans.UnSelectAllborders=取消选择所有边框

###########################################################
#BoxPreferencePage properties
BoxPreferencePage.displayname.Title=框

BoxPreferencePage.text.Margin=边距

BoxPreferencePage.text.Padding=填充

###########################################################
#CommentsPreferencePage properties
CommentsPreferencePage.displayname.Title=备注

###########################################################
#ButtonPaneComposite properties
ButtonPaneComposite.tooltip.MasterPage=主页

ButtonPaneComposite.tooltip.Normal=正常

ButtonPaneComposite.tooltip.Ruler=标尺

###########################################################
#SettingsPage properties
SettingsPage.dataFetchSetting.label=从数据源提取的最大行数(&M):

SettingsPage.CachePreference.button.clear=清除缓存(&C)

SettingsPage.CachePreference.checkBox.text=缓存数据集数据以便进行报表预览(&H)

SettingsPage.CachePreference.description=定义缓存大小:

SettingsPage.CachePreference.Filter.Tooltip=使用缓存将数据传入或传出数据集

SettingsPage.CachePreference.label.text=行

SettingsPage.CachePreference.messageBox.info=缓存已被清除

SettingsPage.CachePreference.messageBox.title=缓存清除

SettingsPage.CachePreference.radioBox.entireNumText=缓存整个数据集(&E)

SettingsPage.CachePreference.radioBox.numText=缓存和使用第一个(&U)

###########################################################
#CascadingParametersDialog properties
CascadingParametersDialog.Button.isRequired=必需(&Q)

CascadingParametersDialog.default.preview.string=我的字符串

CascadingParametersDialog.display.controlType.comboBox=组合框

CascadingParametersDialog.display.controlType.listBox=列表框

CascadingParametersDialog.Label.button.changeFormat=更改(&G)...

CascadingParametersDialog.Label.button.createNew.dataset=新建(&W)...

CascadingParametersDialog.Label.cascadingParam.name=级联参数组名称(&R):

CascadingParametersDialog.Label.column.displayText=显示文本

CascadingParametersDialog.Label.column.dataSet=数据集

CascadingParametersDialog.Label.column.name=名称

CascadingParametersDialog.Label.column.value=值

CascadingParametersDialog.Label.createNewParam=<创建新参数...>

CascadingParametersDialog.Label.dataSets=数据集(&S):

CascadingParametersDialog.Label.dataType=数据类型(&Y)

CascadingParametersDialog.Label.defaultValue=默认值(&V):

CascadingParametersDialog.Label.displayType=显示类型(&S):

CascadingParametersDialog.Label.formatAs=格式为:

CascadingParametersDialog.Label.group.general=常规

CascadingParametersDialog.Label.group.moreOptions=更多选项

CascadingParametersDialog.Label.group.properties=属性

CascadingParametersDialog.Label.helpText=帮助文本(&H):

CascadingParametersDialog.Label.listLimit=列表限制(&L):

CascadingParametersDialog.Label.NoColumnAvailable=<没有可用的列>

CascadingParametersDialog.Label.param.name=名称(&N):

CascadingParametersDialog.Label.parameters=参数

CascadingParametersDialog.Label.preview=带格式预览

CascadingParametersDialog.Label.groupPromptText=提示文本(&P):

CascadingParametersDialog.Label.parameterPromptText=提示文本(&X):

CascadingParametersDialog.Label.selectDataSet=<选择数据集>

CascadingParametersDialog.Label.selectDisplayColumn=<未设置显示列>

CascadingParametersDialog.Label.selectValueColumn=<未设置值列>

CascadingParametersDialog.Label.values=值

CascadingParametersDialog.Radio.Single=单个数据集(&N)

CascadingParametersDialog.Radio.Mutli=多个数据集(&U)

CascadingParametersDialog.Choice.NullValue=空值

CascadingParametersDialog.Choice.BlankValue=空白值

CascadingParametersDialog.Choice.SelectValue=<选择值...>

CascadingParametersDialog.Button.Add=添加(&A)

CascadingParametersDialog.Button.Edit=编辑(&I)

CascadingParametersDialog.Button.Delete=删除(&T)

CascadingParametersDialog.Button.IsMultiple=允许多个值(&W)

CascadingParametersDialog.DefalutValue.Add=添加(&D)

CascadingParametersDialog.ExpressionEditor.Title=编辑表达式

CascadingParametersDialog.Title.AddCascadingParameter=添加级联参数组

CascadingParametersDialog.Title.EditCascadingParameter=编辑级联参数组

CascadingParametersDialog.items.None=<无>

###########################################################
#CellPageGenerator properties
CellPageGenerator.List.Borders=边框

CellPageGenerator.List.CellPadding=填充

CellPageGenerator.List.Font=字体

CellPageGenerator.List.General=常规


###########################################################
#ChoiceSetFactory properties
ChoiceSetFactory.choice.Auto=自动

ChoiceSetFactory.choice.None=无

###########################################################
#CodePageAction properties
CodePageAction.actionText.code=脚本

###########################################################
#ColorBuilder properties
ColorBuilder.text.Auto=自动

ColorBuilder.Button.ChooseColor=打开色板

ColorSelector.ColorDialog.Title=颜色

ColorSelector.Name=颜色选择器
###########################################################
#ColumnPageGenerator properties
ColumnPageGenerator.List.Borders=边框

ColumnPageGenerator.List.CellPadding=单元格边距

ColumnPageGenerator.List.Font=字体

ColumnPageGenerator.List.General=常规

ColumnPageGenerator.List.Visibility=可见性

###########################################################
#Commom properties
Commom.ShortDateTime.Fri=五

Commom.ShortDateTime.Mon=一

Commom.ShortDateTime.Sat=六

Commom.ShortDateTime.Sun=日

Commom.ShortDateTime.Thu=四

Commom.ShortDateTime.Tue=二

Commom.ShortDateTime.Wed=三

###########################################################
#CopyAction properties
CopyAction.text=复制(&C)

###########################################################
#CreatePlaceHolderAction properties
CreatePlaceHolderAction.text=创建模板报表项(&A)...

###########################################################
#CutAction properties
CutAction.text=剪切(&T)

CutAction.trans=剪切元素

###########################################################
#dataBinding properties
dataBinding.label.selectBinding=选择数据绑定:

dataBinding.message.changeDataSet=您将要更改元素的数据绑定。\n\n是否要清除全部现有的数据字段信息?

dataBinding.message.changeReference=您将要更改元素的数据绑定。\n\n是否要执行操作?

dataBinding.title.changeDataSet=是否更改数据集？

dataBinding.title=数据绑定

dataBinding.message.haveNoName=您选择的项目没有名称。\n\n请返回并为项目指定名称，使其可选择。

dataBinding.title.haveNoName=没有名称?

dataBinding.button.OK=确定(&O)

parameterBinding.title=数据集参数绑定(&B)...

ParameterBindingDialog.title=数据集参数绑定

dataBinding.title.generateOutputParam=生成绑定?

dataBinding.msg.generateOutputParam=数据集具有一个或多个输出参数。\n\n 您要为它们生成绑定吗?

###########################################################
#DataEditPart properties
DataEditPart.Figure.Dafault=数据

DataEditPart.Figure.Expression=表达式

DataEditPart.stackMsg.edit=编辑数据绑定

DataEditPart.text.AggregateOn=聚合条件

###########################################################
#DataEditPart properties
DestroyEditPart.Message=此报表项的内容已损坏。如要继续使用此报表，请先删除此项目。此操作可能会导致报表设计文件进一步损坏和/或致使 report designer 无响应。

###########################################################
#DataPageGenerator properties
DataPageGenerator.List.Bookmark=书签

DataPageGenerator.List.Borders=边框

DataPageGenerator.List.Duplicates=重复项

DataPageGenerator.List.Expression=表达式

DataPageGenerator.List.Format=格式

DataPageGenerator.List.formatDateTime=日期时间格式

DataPageGenerator.List.formatNumber=数值格式

DataPageGenerator.List.formatString=字符串格式

DataPageGenerator.List.General=常规

DataPageGenerator.List.Highlights=突出显示

DataPageGenerator.List.HyperLink=超级链接

DataPageGenerator.List.Margin=边距

DataPageGenerator.List.Padding=填充

DataPageGenerator.List.Section=分页符

DataPageGenerator.List.TOC=目录

DataPageGenerator.List.Visibility=可见性

###########################################################
#dataset properties
dataset.edit=编辑数据集 - {0}

dataset.editor.settings=设置

dataset.editor.settings.dataFetchSetting.errorNumberFormat=请为行取数限制输入一个大于零的整数。

dataset.editor.settings.dataFetchSetting.errorNumberFormatForResultSet=请为结果集数值输入一个正整数。

dataset.editor.settings.dataFetchSetting=数据提取设置

dataset.editor.settings.resultsetselection.resultSetSelection=结果集选择

dataset.editor.settings.resultsetselection.enableResultSetSelection=启用结果集选择(&E)

dataset.editor.settings.resultsetselection.selectResultSetByName=按名称选择结果集(&R):

dataset.editor.settings.resultsetselection.selectResultSetByNumber=按数值选择结果集(&S):

dataset.editor.settings.dataFetchSetting.fetchAll=从数据源提取所有行(&A)。

dataset.editor.settings.cachePreference.errorNumberFormat=请为缓存行大小输入一个大于零的数。

dataset.editor.settings.cachePreference=缓存首选项

dataset.editor.computedColumns=计算列

dataset.editor.dataSource.Tooltip=查看/更改数据源。

dataset.editor.dataSource=数据源

dataset.editor.error.columnOrAliasNameAlreadyUsed=新的列名称或别名 ({0}) 已经由索引 {1} 处的列使用。

dataset.editor.error.computedColumnNameAlreadyUsed=新的计算列名称 ({0}) 已经用作输出列中列 ({1}) 的列别名。

dataset.editor.error.duplicateParameterName=重复的参数名称。

dataset.editor.error.nonemptyDefaultvalue=默认值不能为空

dataset.editor.error.emptyName=名称不能为空白。

dataset.editor.error.missingComputedColumnExpression=计算列 {0} 的表达式不能为空或空白。

dataset.editor.error.missingComputedColumnName=计算列名称不能为空或空白。

dataset.editor.error.noDataSource=没有可用于数据集的数据源 {0}，请在属性视图的“数据源”条目中选择数据源。

dataset.editor.error.nonmatchedDataSource=数据集“{0}”的数据源不匹配，请选择与数据集类型“{1}”匹配的数据源。

dataset.editor.error.noEditorsFound=未找到可用于数据集的编辑器 {0}。

dataset.editor.error.noInputParameterDefaultValue=输入参数 {0} 应具有非空默认值。

dataset.editor.error.title=警告

dataset.editor.error.unsupportedDataSet=不支持的数据集类型 - {0}。

dataset.editor.error.validationParameter=输入参数 [{0}] 具有空值。是否要保留为空?

dataset.editor.filters=筛选器

dataset.editor.label.selectDataSource=选择数据源(&D):

dataset.editor.outputColumns=输出列

dataset.editor.parameters=参数

dataset.editor.preview=预览结果

dataset.editor.outputparameters=预览输出参数

dataset.editor.title.alias=别名

dataset.editor.title.columnName=列名

dataset.editor.title.dataType=数据类型

dataset.editor.title.defaultValue=默认值

dataset.editor.title.direction=方向

DataSetParameterPage.editor.title.linkToSalarParameter=链接到报表参数

dataset.editor.title.displayName=显示名称

dataset.editor.title.displayNameKey=显示名称关键字

dataset.editor.title.expression=表达式

dataset.editor.title.aggrFunc=聚合

dataset.editor.title.aggrArgu=参数(&R)

dataset.editor.title.filter=筛选器

dataset.editor.title.helpText=帮助文本

dataset.editor.title.isInput=输入

dataset.editor.title.isNullable=允许 Null

dataset.editor.title.isOptional=可选

dataset.editor.title.isOutput=输出

dataset.editor.title.name=名称

dataset.editor.title.nativeName=本地名称

dataset.editor.title.operator=运算符

dataset.editor.title.type=类型

dataset.editor.title.value1=值 1

dataset.editor.title.value2=值 2

dataset.editor.inputDialog.displayName=显示名称(&D)

dataset.editor.inputDialog.displayNameKey=显示名称关键字(&K)

dataset.editor.inputDialog.name=名称(&N)

dataset.editor.inputDialog.nativeName=本地名称(&A):

dataset.editor.inputDialog.type=类型(&T)

dataset.editor.inputDialog.alias=别名(&A)

dataset.editor.inputDialog.columnName=列名称(&C)

dataset.editor.inputDialog.dataType=数据类型(&T)

dataset.editor.inputDialog.expression=表达式(&E)

dataset.editor.inputDialog.aggrArgu=参数(&R)

dataset.editor.inputDialog.aggrFunc=聚合(&G)

dataset.editor.inputDialog.filter=筛选器(&F)

dataset.editor.inputDialog.value1=值 1(&1)

dataset.editor.inputDialog.value2=值 2(&2)

dataset.editor.inputDialog.operator=运算符(&O)

dataset.editor.inputDialog.defaultValue=默认值(&V)

dataset.editor.inputDialog.direction=方向(&D)

dataset.editor.historybar.backward=后退

dataset.editor.historybar.forward=前进

dataset.editor.historybar.to=到

dataset.error.noDataSources=没有可用的数据源。是否要立即添加数据源?

dataset.error.dataSourceMissing=缺少此数据集的数据源。

dataset.error.title.noDataSources=无可用数据源。

dataset.message.create=创建新数据集。

dataset.new.defaultName=数据集

dataset.new=新建数据集

dataset.action.new=新建数据集(&S)

dataset.join.new=新建联接数据集

dataset.action.join.new=新建联接数据集(&J)

dataset.resultset.preview.tooltip=预览查询结果。

dataset.resultset.preview.promptMessage.recordsNum=共显示 {0} 个记录。

dataset.resultset.preview.promptMessage.MoreRecordsExist=结果包含未显示的更多记录。

dataset.outputparameters.preview.tooltip=预览输出参数。

dataset.wizard.label.datasetName=数据集名称(&M):

dataset.wizard.label.datasetType=数据集类型(&T):

dataset.wizard.label.dataSource=数据源(&S):

dataSourceSelectionPage.title=与数据源链接。

dataset.error.title.noReportDesign=错误

dataset.error.msg.noReportDesign=没有可用的报表设计。

dataset.warning.invalidReponseStatus=自定义 ODA 页面中的设计响应无效。ODA 设计会话响应状态: {0}。

###########################################################
#DataSetBasePage properties
DataSetBasePage.tooltip=名称不能为空。

DataSetBasePage.ScriptedDataSet.name=脚本化数据集

DataSetBasePage.ScriptedDataSource.name=脚本化数据源

DataSetBasePage.Group.DataSourceSelection=数据源选择

DataSetBasePage.CassandraScriptedDataSet.name=Cassandra 脚本化数据集

DataSetBasePage.CassandraScriptedDataSource.name=Cassandra 脚本化数据源

DataSetBindingSelector.Button.DeselectAll=取消全选(&A)

DataSetBindingSelector.Button.SelectAll=全选(&S)

DataSetBindingSelector.Combo.DataSet=数据集(&D):

DataSetBindingSelector.Label.SelectBindingColumns=请选择绑定列。

DataSetBindingSelectorPage.Label.SelectBindingColumns=请选择绑定列。

DataSetBindingSelectorPage.Title=数据设置绑定

CassandraScriptedDataSource.display.name=Cassandra 脚本化数据源

CassandraScriptedDataSource.error.classNotFound=无法找到 Hector 客户端库 jar 以支持 Cassandra 脚本化数据源。

###########################################################
#JointDataSetPage properties
JointDataSetPage.pageName=联接数据集

JointDataSetPage.page.detail=从每端选择一个数据集和一个列以定义联接。

JointDataSetPage.button.innerJoin=内联接(&I)

JointDataSetPage.button.leftJoin=左外联接(&L)

JointDataSetPage.button.rightJoin=右外联接(&R)

JointDataSetPage.button.fullJoin=完全外部联接(&O)

JointDataSetPage.joinType=联接类型

JointDataSetPage.error.nodataset.title=未找到数据集。

JointDataSetPage.error.nodataset.right=未找到右数据集。

JointDataSetPage.error.nodataset.left=未找到左数据集。

JointDataSetPage.pageDescription=定义联接数据集

JointDataSetPage.query=查询

###########################################################
#DerivedDataSet properties
DerivedDataSet.action.error.message.NoDataSet=未找到可用数据集。

DerivedDataSet.action.error.title.createDerivedDataSet=创建衍生数据集时出错

DerivedDataSet.action.error.message.InvalidNumber.DataSet=可用的数据集数少于两个。

###########################################################
#DataSetComputedColumnsPage properties
DataSetComputedColumnsPage.ComputedColumns.Tooltip=定义计算列。

DataSetComputedColumnsPage.description=定义计算列

DataSetComputedColumnsPage.duplicatedName=重复的列名称。

DataSetComputedColumnsPage.numberName=列名称不能是数字。

DataSetComputedColumnsPage.toolTipText.Down=计算列下移。

DataSetComputedColumnsPage.toolTipText.Up=计算列上移。

DataSetComputedColumnsPage.toolTipText.Remove=删除计算列。

DataSetComputedColumnsPage.toolTipText.New=新建计算列。

DataSetComputedColumnsPage.toolTipText.Edit=编辑计算列。

DataSetComputedColumnsPage.InputDialog.title.New=新建计算列

DataSetComputedColumnsPage.InputDialog.title.Edit=编辑计算列

DataSetComputedColumnsPage.InputDialog.message.error.AggregationExpression=聚合表达式不能包含聚合计算列!

DataSetComputedColumnsPage.title.ComputedColumnInputDialog=计算列

###########################################################
#DataSetFiltersPage properties
DataSetFiltersPage.description=定义筛选器:

DataSetFiltersPage.exception.msg.operator.notSupport=不支持此运算符:

DataSetFiltersPage.Filter.Tooltip=使用 BIRT 表达式筛选数据行。

DataSetFiltersPage.toolTipText.Down=筛选器下移。

DataSetFiltersPage.toolTipText.Up=筛选器上移。

DataSetFiltersPage.toolTipText.Remove=删除筛选器。

DataSetFiltersPage.toolTipText.New=新建筛选器。

DataSetFiltersPage.toolTipText.Edit=编辑筛选器。

DataSetFiltersPage.title.FilterInputDialog=筛选器

DataSetFiltersPage.messages.selectVauleUnavailable=值列表对给定的表达式不可用。

DataSetFiltersPage.DynamicFilter.DynamicParameter.NotFound=动态筛选器的链接动态参数“{0}”不存在!

###########################################################
#DataSetNodeProvider properties
DataSetNodeProvider.menu.text=编辑(&E)

###########################################################
#DataSetParametersPage properties
DataSetParametersPage.description=定义输入参数:

DataSetParametersPage.Filter.Tooltip=将参数传入或传出数据集。

DataSetParameterBindingInputDialog.Title=编辑数据集参数绑定

DataSetParametersPage.ErrorTitle.InvalidInput=无效输入。

DataSetParameterBindingInputDialog.Label.Name=参数名称:

DataSetParametersPage.ErrorMessage.CanootBeNull=该值不能为 null。

DataSetParameterBindingInputDialog.Title.NewParameter=新建参数

DataSetParameterBindingInputDialog.Title.EditParameter=编辑参数

DataSetParameterBindingInputDialog.Label.DataType=数据类型:

DataSetParameterBindingInputDialog.Label.Value=值(&V):

DataSetParametersPage.ParameterButton.Tooltip.Edit=编辑报表参数

DataSetParametersPage.ParameterButton.Tooltip.New=新建报表参数

DataSetParametersPage.Prompt.new=<此处为新参数...>

DataSetParametersPage.default.None=N/A
DataSetParametersPage.reportParam.None=无

DataSetParametersPage.errorMessage.LinkedReportParamNotFound=数据集参数“{1}”的链接报表参数“{0}”现在不存在。

DataSetParametersPage.errorMessage.InvalidType.LinkedReportParam=如果用户为报表参数设置多个值，则数据集参数仅使用第一个值。

DataSetParameterPage.cell.linkToSalarParameter=链接到报表参数(&L)

DataSetParameterPage.title.ParameterInputDialog=参数

DataSetParameterPage.toolTipText.New=新建参数

DataSetParameterPage.toolTipText.Edit=编辑参数

DataSetParameterPage.toolTipText.Remove=删除参数

DataSetParameterPage.toolTipText.Up=参数上移

DataSetParameterPage.toolTipText.Down=参数下移

DataSetParameterPage.WarningMessage.updatePosition=某些或所有数据集参数具有无效的位置值。正在根据其序列更新其位置。

DataSetParameterPage.warning.UnmatchedParamDataType=数据集类型与链接的报表参数类型不匹配。

DataSetParameterPage.multiValuesType=多值类型

DataSetParameterPage.warningLinkedMultipleValuesParams.title=警告对话框

DataSetParameterPage.updateReportParameter.title=报表参数更新设置

DataSetParameterPage.updateReportParameter.message=是否要使用数据集参数的设置来更新链接报表参数?

DataSetParameterPage.updateReportParameter.promptButtonYes=是

DataSetParameterPage.updateReportParameter.promptButtonNo=否

DataSetParameterPage.updateReportParameter.propmtText=记住我的决定

PromptParameterDialog.title=确认更新设计属性

PromptParameterDialog.okButton.text=应用

PromptParameterDialog.parameterGroup.title=参数设计属性

PromptParameterDialog.parameterGroup.label=使用已编辑数据集设计修订后的属性值更新所选参数。

PromptParameterDialog.parameterGroup.nameColumn=名称

PromptParameterDialog.parameterGroup.previousValue=以前的值

PromptParameterDialog.parameterGroup.revisedValue=修订后的值

###########################################################
#PropertyBindingpage properties
PropertyBindingPage.datasource.property.empty=ODA 扩展没有为此数据源定义公共属性。

PropertyBindingPage.dataset.property.empty=ODA 扩展没有为此数据集定义公共属性。

PropertyBindingPage.dataset.queryText=查询文本(&T)

PropertyBindingPage.property.tooltip=为运行时定义属性绑定。

PropertyBindingPage.button.tooltip=打开表达式生成器以输入 {0} 值。

PropertyBindingPage.label.colon=:
PropertyBindingPage.label.LeftParenthesis=(
PropertyBindingPage.label.RightParenthesis=)

###########################################################
#DataSetProcess properties
DataSetProcess.action.Delete=删除数据集

DataSetProcess.action.Edit=编辑数据集

DataSetProcess.action.Preview=预览

DataSetProcess.text.DisplayName=数据集

DataSetProcess.text.Prefix=以下数据集使用此数据源:

DataSetProcess.text.Show=显示该数据集的数据集编辑器页。

DataSetProcess.text.Suffix=是否确定删除此元素？

###########################################################
#datasource properties
datasource.action.edit=编辑(&E)

datasource.edit=编辑数据源 - {0}

datasource.new.defaultName=数据源

datasource.new=新建数据源

datasource.action.new=新建数据源(&S)

datasource.wizard.label.dataSourceName=数据源名称(&M):

datasource.wizard.label.datasources=根据以下列表中的数据源类型创建(&D)

datasource.wizard.label.connectionfile=根据配置文件存储中的连接配置文件创建(&C)

datasource.wizard.message.selectType=为新数据源提供所有设置，或选择预定义的连接配置文件。

datasource.wizard.title.select=选择数据源类型，或选择连接配置文件。

datasource.editor.property=属性绑定

datasource.editor.duplicatedName=数据源名称“{0}”重复。

datasource.error.title.noReportDesign=错误

datasource.error.msg.noReportDesign=没有可用的报表设计。

datasource.exprotToCP.title=导出

datasource.exportToCP.message=您正在将“{0}”导出到数据源资源管理器。

datasource.exportToCP.checkBox=为导出的数据源创建连接配置文件存储(&C)。

datasource.exportToCP.externalCheckBox=使用数据源中外部化的连接配置文件(&U)。

datasource.exportToCP.label.specifyName=连接配置文件名称(&E):

datasource.exportToCP.errorBox.button.OK=确定

datasource.exportToCP.error.emptyFileName=必须指定一个非空连接配置文件名称!

datasource.exportToCP.error.invalidFileName=指定的连接配置文件名称包含无效字符!

datasource.exportToCP.error.duplicatedFileName=该连接配置文件名称已存在!

datasource.exportToCP.error.alreadyExported=数据源“{0}”已经未外部化其属性。


###########################################################
#DataSourceProcess properties
DataSourceProcess.action.Delete=删除数据源

DataSourceProcess.action.Edit=编辑数据源

DataSourceProcess.text.DisplayName=DataSource
DataSourceProcess.text.Prefix=以下数据集使用此数据源

DataSourceProcess.text.Show=显示数据源编辑器页

DataSourceProcess.text.Suffix=是否确定删除此元素？

###########################################################
#DataSourceSelectionPage properties
DataSourceSelectionPage.ScriptDataSource.DisplayName=脚本化数据源

###########################################################
#DataSourcesSlotOutlineProcess properties
DataSourcesSlotOutlineProcess.action.New=新建数据源

###########################################################
#datatypes properties
datatypes.any=任何

datatypes.auto=自动

datatypes.dateTime=日期时间

datatypes.decimal=小数

datatypes.float=浮点

datatypes.integer=整型

datatypes.string=字符串

###########################################################
#DataView properties
DataView.defaultText.noDataView=数据视图不可用

###########################################################
#DataViewTreeViewerPage properties
DataViewTreeViewerPage.tooltip.DoubleClickToEdit=双击以进行编辑

DataViewTreeViewerPage.tooltip.DragToInsertColumn=拖动插入列

DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns=拖动以插入所有数据集列

DataViewTreeViewerPage.tooltip.DragToInsertParameter=拖动以插入报表参数;

###########################################################
#DebugUtil properties
DebugUtil.importProject.fail="导入现有项目失败。"

###########################################################
#DecorationFieldEditor properties
DecorationFieldEditor.text.Linethrough=删除线

DecorationFieldEditor.text.Overline=上划线

DecorationFieldEditor.text.Underline=下划线

###########################################################
#DefaultNodeProvider properties
DefaultNodeProvider.Dlg.Confirm=单击“是”继续删除 {0}。 否则，单击“否”

DefaultNodeProvider.ParameterGroup.ConfirmMessage=您即将删除参数组。如果您要删除参数组，将会删除该组内的所有参数。是否继续？

DefaultNodeProvider.ParameterGroup.ConfirmTitle=确认参数组删除

DefaultNodeProvider.Tree.Body=正文

DefaultNodeProvider.Tree.Clients={0} 被以下元素使用

DefaultNodeProvider.Tree.ColumnHedings=列标题

DefaultNodeProvider.Tree.Components = 报表项

DefaultNodeProvider.Tree.DataSets=数据集

DefaultNodeProvider.Tree.DataSources=数据源

DefaultNodeProvider.Tree.Detail=详细资料

DefaultNodeProvider.Tree.Footer=页脚

DefaultNodeProvider.Tree.Groups=组

DefaultNodeProvider.Tree.Header=页眉

DefaultNodeProvider.Tree.Images=嵌入图像

DefaultNodeProvider.Tree.Invalid=无效节点

DefaultNodeProvider.Tree.MasterPages=MasterPages
DefaultNodeProvider.Tree.PageSetup=页面设置

DefaultNodeProvider.Tree.Parameters=报表参数

DefaultNodeProvider.Tree.Reference=警告

DefaultNodeProvider.Tree.Scratch=便笺簿

DefaultNodeProvider.Tree.Styles=样式

DefaultNodeProvider.Tree.Themes=布景主题

DefaultNodeProvider.WarningDialog.Title=警告

DefaultNodeProvider.WarningDialog.EmptyList=没有可供插入的元素。

DefaultNodeProvider.stackMsg.changeBinding=更改数据绑定

###########################################################
#DeleteAction properties
DeleteAction.text=删除(&D)

DeleteAction.trans=删除元素

###########################################################
#DeleteColumnAction properties
DeleteColumnAction.actionMsg.delete=删除(&D)

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.actionMsg.deleteGroup=删除组(&G)

###########################################################
#DeleteListGroupAction properties
DeleteListGroupAction.actionMsg.deleteGroup=删除组(&G)

###########################################################
#DeleteRowAction properties
DeleteRowAction.actionMsg.delete=删除(&D)

###########################################################
#designer properties
designer.preview.browserTitle=预览

designer.preview.preference.browser.autoSave=预览前自动保存

designer.preview.preference.browser.browse=浏览(&B)...

designer.preview.preference.browser.currentBrowsers=当前外部 Web 浏览器适配器(&U):

designer.preview.preference.browser.description=该选择确定用于预览 BIRT 报表的 web 浏览器。\n如果仅配置了一个浏览器适配器，则无法更改\n选择。

designer.preview.preference.browser.details=选择浏览器程序

designer.preview.preference.browser.program=自定义浏览器命令(&S):

designer.preview.preference.browser.svg=启用 SVG 图表(&E)

designer.preview.preference.browser.extbrowser.link=有关配置用于显示报表的外部浏览器的信息，请参阅<a>“{0}”</a>。

designer.preview.preference.masterpagecontent=启用主页内容(&M)

designer.preview.preference.appcontextkey=使用自定义应用程序上下文(&U)

designer.preview.preference.browser.useExternal=始终使用外部浏览器(&W)

designer.preview.preference.elementname.columnwidth=125

designer.preview.preference.elementname.customname.Enabled=真

designer.preview.preference.elementname.customname=默认名称

designer.preview.preference.elementname.defaultname.Enabled=假

designer.preview.preference.elementname.defaultname=元素名称

designer.preview.preference.elementname.description.Enabled=假

designer.preview.preference.elementname.description=描述

designer.preview.preference.elementname.Enabled=真

designer.preview.preference.locale.description=选择区域设置(&C):

designer.preview.preference.timezone.description=时区(&T):

designer.preview.preference.resultset.sqldatasetpage.group.title=查询页面

designer.preview.preference.resultset.previewpage.group.title=预览页面

designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting=预览行限制设置

designer.preview.preference.resultset.NoLimitOfDispalyRow=要显示的行数无限制(&N)。

designer.preview.preference.resultset.maxNoOfSchema.description=要显示的最大架构数(&S):

designer.preview.preference.resultset.maxNoOfTable.description=每个架构中要显示的最大表数(&T):

designer.preview.preference.resultset.maxNoOfSchema.errormessage=请为最大架构数输入一个介于 0 和 {0} 之间的数。

designer.preview.preference.resultset.maxNoOfTable.errormessage=请在每个架构中分别为最大表数输入一个介于 0 和 {0} 之间的数。

designer.preview.preference.resultset.maxrow.description=要显示的行数(&R):

designer.preview.preference.resultset.maxrow.errormessage=请为最大行数输入一个介于 1 和 {0} 之间的数。

designer.preview.preference.resultset.maxrowlevelmember.description=要显示的每个行级别的计数(&L):

designer.preview.preference.resultset.maxrowlevelmember.errormessage=请为最大行级别数输入一个介于 1 和 {0} 之间的数。

designer.preview.preference.resultset.maxcolumnlevelmember.description=要显示的每个列级别的计数(&C):

designer.preview.preference.resultset.maxcolumnlevelmember.errormessage=请为最大列级别数输入一个介于 1 和 {0} 之间的数。

designer.preview.preference.resultset.maxinmemorycubesize.description=内存多维数据集大小(MB)(&S):

designer.preview.preference.resultset.maxinmemorycubesize.errormessage=请为最大内存多维数据集大小输入一个介于 1 和 {0} 之间的数。

designer.preview.preference.server.description=Internal Application Server 配置。该服务器由 \nBIRT Report Designer Preview Framework 内部使用。

designer.preview.preference.server.hostDescription=本地计算机的主机名或 IP 地址。将其留空以自动\n确定(&H)。

designer.preview.preference.server.note=注：

designer.preview.preference.server.portDescription=要监听的服务器端口。设置为 0 以动态分配(&P)。

designer.preview.preference.server.requireRestart=如果该服务器已在运行，这些首选项只有在\n重新启动后才生效。

designer.preview.preference.bidiOrientation.label=双向(&B):

designer.preview.preference.bidiOrientation.auto=自动

designer.preview.preference.bidiOrientation.ltr=从左到右

designer.preview.preference.bidiOrientation.rtl=从右到左

designer.preview.preference.updateParameter.confirmMessage=确认更新

designer.preview.preference.updateParameter.confirmButton=如果查询更改，在将数据集参数属性与 ODA 参数属性同步时始终提示确认(&A)。

designer.preview.preference.updateReportParameter.confirmButton=不要提示确认更新链接报表参数(&D)。

designer.preview.preference.updateReportParameter.confirmOption.always=始终(&W):

designer.preview.preference.updateReportParameter.confirmOption.update=更新(&U)

designer.preview.preference.updateReportParameter.confirmOption.ignore=忽略(&I)

designer.preview.previewaction.label.webviewer=在 Web 查看器中查看报表

designer.preview.previewaction.label=以 {0} 格式查看报表

designer.preview.previewaction.ErrorMessage=由于未安装或未找到 iText，不支持 PDF 生成。

designer.preview.previewaction.ErrorMessage.title=警告

designer.preview.run.webviewer=在 Web 查看器中

designer.preview.run=作为 {0}

###########################################################
#DesignerActionBarContributor properties
DesignerActionBarContributor.element.group=插入组(&G)

DesignerActionBarContributor.element.InsertColumnLeft=在左侧插入列(&L)

DesignerActionBarContributor.element.InsertColumnRight=在右侧插入列(&R)

DesignerActionBarContributor.element.InsertRowAbove=在上方插入行(&A)

DesignerActionBarContributor.element.InsertRowBelow=在下方插入行(&B)

DesignerActionBarContributor.element.merge=合并单元格(&M)

DesignerActionBarContributor.element.split=拆分单元格(&S)

designerActionBarContributor.menu.data-newdataset=新建数据集(&E)

designerActionBarContributor.menu.data-newdatasource=新建数据源(&O)

DesignerActionBarContributor.menu.data=数据(&D)

DesignerActionBarContributor.menu.element-EditGroup=编辑组(&E)

DesignerActionBarContributor.menu.element-LayoutMode=布局模式(&D)

DesignerActionBarContributor.menu.element-showRuler=显示标尺(&R)

DesignerActionBarContributor.menu.element.editDataBinding=编辑数据绑定(&B)...

DesignerActionBarContributor.menu.element=元素(&L)

DesignerActionBarContributor.menu.format=格式

DesignerActionBarContributor.menu.insert=插入(&I)

DesignerActionBarContributor.menu.page=页面(&G)

DesignerActionBarContributor.menu.style-rule-apply=应用样式(&P)

DesignerActionBarContributor.menu.style-rule-edit=编辑样式(&D)

DesignerActionBarContributor.menu.style-rule-remove=删除样式(&R)

DesignerActionBarContributor.style.new=新建样式(&N)...

DesignerActionBarContributor.menu.data-NewParameter=新建报表参数(&P)

DesignerActionBarContributor.menu.data-NewDataSetParent=新建数据集(&S)

designerActionBarContributor.menu.data-newDataset=新建数据集(&E)

designerActionBarContributor.menu.data-newJointDataset=新建联接数据集(&J)

DesignerActionBarContributor.menu.element.insert=插入(&I)

DesignerActionBarContributor.menu.element.group=分组(&G)

DesignerActionBarContributor.menu.element.style=样式(&Y)

DesignerActionBarContributor.menu.zoomCombo.tooltip=缩放级别
###########################################################
#DesignerOutlinePage properties
DesignerOutlinePage.action.Delete=删除选择

DesignerOutlinePage.action.Hide=隐藏页眉和页脚

DesignerOutlinePage.action.Redo=恢复

DesignerOutlinePage.action.Undo=撤消

DesignerOutlinePage.menu.Filters=筛选器

###########################################################
#Content outline page switch action
ContentOutlinePage.action.text.reportOutline=切换至 XML 源大纲

ContentOutlinePage.action.text.reportXMLSourceOutline=切换至报表大纲

###########################################################
#DesignerPaletteFactory properties
DesignerPaletteFactory.categoryName.reportItems=报表项

DesignerPaletteFactory.elementLabel.data=数据

DesignerPaletteFactory.elementLabel.grid=网格

DesignerPaletteFactory.elementLabel.image=图像

DesignerPaletteFactory.elementLabel.label=标签

DesignerPaletteFactory.elementLabel.list=列表

DesignerPaletteFactory.elementLabel.tabel=表

DesignerPaletteFactory.elementLabel.text=文本

DesignerPaletteFactory.elementName.data=数据

DesignerPaletteFactory.elementName.grid=网格

DesignerPaletteFactory.elementName.image=图像

DesignerPaletteFactory.elementName.label=标签

DesignerPaletteFactory.elementName.list=列表

DesignerPaletteFactory.elementName.table=表

DesignerPaletteFactory.elementName.text=文本

DesignerPaletteFactory.elementName.textdata=动态文本

DesignerPaletteFactory.toolTip.dataReportItem=插入数据集列或表达式结果。

DesignerPaletteFactory.toolTip.gridReportItem=插入行/列布局以直观排列其它项。

DesignerPaletteFactory.toolTip.imageReportItem=从文件或 URL 中插入图像。

DesignerPaletteFactory.toolTip.labelReportItem=插入一段静态文本。

DesignerPaletteFactory.toolTip.listReportItem=在页眉/详细信息/页脚区段中插入数据集行的灵活格式表示

DesignerPaletteFactory.toolTip.multiLineDataReportItem=从文件、数据集列或表达式结果中插入文本块（纯文本/HTML/RTF）。

DesignerPaletteFactory.toolTip.tableReportItem=在页眉/详细信息/页脚区段中插入数据集行的列表示

DesignerPaletteFactory.toolTip.textDataReportItem=插入带有可选 CLOB 数据表达式的文本块

DesignerPaletteFactory.toolTip.textReportItem=插入文本块（纯文本/HTML）和可选嵌入表达式。

DesignerPaletteFactory.quicktool.title=快速工具

DesignerPaletteFactory.quicktool.agg.title=聚合

DesignerPaletteFactory.quicktool.agg.toolTip=插入聚合项

DesignerPaletteFactory.quicktool.timeperiod.title=相对时间周期

DesignerPaletteFactory.quicktool.timeperiod.toolTip=插入时间周期项

###########################################################
#DimensionBuilderDialog properties
DimensionBuilderDialog.LabelMeasure=度量：

DimensionBuilderDialog.LabelUnit=单位：

DimensionBuilderDialog.Title=Dimension Builder

###########################################################
#DNDUtil properties
DNDUtil.trans.copy=复制元素

DNDUtil.trans.cut=移动元素

###########################################################
#DuplicatesPage properties
DuplicatesPage.Check.HideDuplicates=隐藏重复项

DuplicatesPage.Group.Repeat=复制（重复）

DuplicatesPage.Group.RepeatonPage=复制（页上重复）

DuplicatesPage.Label.RepeatInfo=隐藏与上一数据行相同的值。

DuplicatesPage.Label.RepeatOnPage.1=隐藏与上一行相同的值，

DuplicatesPage.Label.RepeatOnPage.2=\\ 但始终在每个新页顶部显示该值。

DuplicatesPage.Label.ResetonGroup=对以下组进行重置

DuplicatesPage.Radio.Repeat=重复

DuplicatesPage.Radio.RepeatonPage=页上重复

###########################################################
#EditAction properties
EditAction.text=编辑(&E)

###########################################################
#EditGroupAction properties
EditGroupAction.stackMsg.editGroup=编辑组

###########################################################
#EditGroupAction properties
InsertGroupHeaderFooterAction.stackMsg.editGroup=插入组页眉/页脚

InsertGroupHeaderFooterAction.Text.Header=页眉

InsertGroupHeaderFooterAction.Text.Footer=页脚

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.stackMsg.deleteGroup=删除组

###########################################################
#EditLabelAction properties
EditLabelAction.text=编辑(&E)

EditorDragGuidePolicy.pixels.label=像素

EditorGuide.bottom.label=底部:

EditorGuide.left.label=左侧:

EditorGuide.right.label=右侧:

EditorGuide.top.label=顶部:

###########################################################
#Element properties
Element.AbstractScalarParameter.dataType=数据类型(&D):

Element.AutoText.type=类型:

Element.Cell.colSpan=列跨度:

Element.Cell.column=列:

Element.Cell.drop=放置(&D):

Element.Cell.height=高度:

Element.Cell.rowSpan=行跨度:

Element.Cell.slot.content=内容:

Element.Cell.verticalAlign=垂直对齐:

Element.Cell.width=宽度:

Element.Column.alignment=对齐:

Element.Column.repeat=重复:

Element.Column.width=宽度(&W):

Element.Column.suppressDuplicates=抑制重复

Element.Container.slot.reportItems=报表项:

Element.Data.action=操作:

Element.Data.helpText=帮助文本:

Element.Data.helpTextID=帮助文本关键字

Element.TextData.valueExpr=值表达式(&U):

Element.DataSet.afterClose.toolTip=仅在关闭数据集之后调用。

Element.DataSet.afterClose=关闭后:

Element.DataSet.afterOpen.toolTip=仅在打开数据集之后调用。

Element.DataSet.afterOpen=打开后:

Element.DataSet.beforeClose.toolTip=仅在关闭数据集之前调用。

Element.DataSet.beforeClose=关闭前:

Element.DataSet.beforeOpen.toolTip=仅在打开数据集之前调用。

Element.DataSet.beforeOpen=打开前:

Element.DataSet.cachedMetaData=缓存元数据:

Element.DataSet.columnHints=列提示:

Element.DataSet.computedColumns=计算列:

Element.DataSet.dataSource=数据源:

Element.DataSet.filter=筛选器:

Element.DataSet.inputParameters=输入参数:

Element.DataSet.onFetch.toolTip=仅在从数据集获取数据行之前调用。

Element.DataSet.onFetch=获取中:

Element.DataSet.outputParameters=输出参数:

Element.DataSet.paramBindings=参数绑定:

Element.DataSet.resultSet=结果集:

Element.DataSource.afterClose.toolTip=仅在关闭数据源之后调用。

Element.DataSource.afterClose=关闭后:

Element.DataSource.afterOpen.toolTip=仅在打开数据源之后调用。

Element.DataSource.afterOpen=打开后:

Element.DataSource.beforeClose.toolTip=仅在关闭数据源之前调用。

Element.DataSource.beforeClose=关闭前:

Element.DataSource.beforeOpen.toolTip=仅在打开数据源之前调用。

Element.DataSource.beforeOpen=打开前:

Element.ExtendedItem.extension=扩展名:

Element.ExtendedItem.extensionName=扩展名:

Element.FreeForm.slot.reportItems=报表项:

Element.GraphicMasterPage.columns=列:

Element.GraphicMasterPage.columnSpacing=列间距:

Element.GraphicMasterPage.slot.contents=内容:

Element.Grid.slot.columns=列:

Element.Grid.slot.row=行:

Element.Image.action=操作:

Element.Image.altText=替代文本(&A):

Element.Image.altTextID=替代文本关键字(&K):

Element.Image.helpText=帮助文本:

Element.Image.helpTextID=帮助文本关键字

Element.Image.imageName=图像名称:

Element.Image.scale=比例:

Element.Image.size=大小:

Element.Image.source=源:

Element.Image.typeExpr=类型表达式:

Element.Image.uri=uri:
Element.Image.valueExpr=值表达式:

Element.JdbcDataSource.dataSource=数据源:

Element.JdbcDataSource.driver=驱动程序:

Element.JdbcDataSource.password=密码:

Element.JdbcDataSource.url=Url:
Element.JdbcDataSource.userName=用户名:

Element.JdbcSelectDataSet.getStatement.toolTip=返回查询的自定义 SQL 语句。

Element.JdbcSelectDataSet.getStatement=获取语句:

Element.JdbcSelectDataSet.statement=语句:

Element.Label.action=操作:

Element.Label.helpText=帮助文本:

Element.Label.helpTextID=帮助文本关键字

Element.Label.text=文本:

Element.Label.textID=文本关键字(&T):

Element.Line.orientation=方向:

Element.List.slot.columnHeader=columnHeader:
Element.List.slot.detail=详细资料:

Element.List.slot.footer=页脚:

Element.List.slot.group=组:

Element.List.slot.header=页眉:

Element.ListGroup.slot.footer=页脚:

Element.ListGroup.slot.header=页眉:

Element.Listing.filter=筛选器:

Element.Listing.onFinish.toolTip=在从此元素的数据集内读取最后一行之后调用，但在创建页脚区段之前。

Element.Listing.onFinish=完成时:

Element.Listing.onRow.toolTip=为从此元素的数据集中检索出的每行调用，但在为该行创建内容之前。

Element.Listing.onRow=遇到行时:

Element.Listing.onStart.toolTip=在打开数据集之后、在创建题头区段之前并且在从此元素的数据集检索首行之前调用。

Element.Listing.onStart=启动时:

Element.Listing.pageBreakInterval=分页符间隔(&K):

Element.Listing.sorting=排序:

Element.ListingGroup.filter=筛选器:

Element.ListingGroup.groupName=名称

Element.ListingGroup.interval=间隔:

Element.ListingGroup.intervalRange=间隔范围:

Element.ListingGroup.keyExpr=关键字表达式

Element.ListingGroup.onFinish.toolTip=在从此元素的数据集内读取最后一行之后调用，但在创建页脚区段之前。

Element.ListingGroup.onFinish=完成时:

Element.ListingGroup.onRow.toolTip=对从此元素的数据集检索的每行调用，但是在为该行创建任何内容之前调用。

Element.ListingGroup.onRow=遇到行时:

Element.ListingGroup.onStart.toolTip=在打开数据集之后、在创建题头区段之前并且在从此元素的数据集检索首行之前调用。

Element.ListingGroup.onStart=启动时:

Element.ListingGroup.pageBreakAfter=对象后面的分页符:

Element.ListingGroup.pageBreakBefore=对象前面的分页符:

Element.ListingGroup.sort=排序:

Element.ListingGroup.sortDirection=排序方向:

Element.ListingGroup.toc=目录:

Element.MasterPage.bottomMargin=下边距(&B):

Element.MasterPage.columns=列:

Element.MasterPage.columnSpacing=间距(&S):

Element.MasterPage.height=高度(&E):

Element.MasterPage.leftMargin=左边距(&L):

Element.MasterPage.margin=边距:

Element.MasterPage.orientation=方向(&R):

Element.MasterPage.rightMargin=右边距(&R):

Element.MasterPage.slot.contents=内容:

Element.MasterPage.topMargin=上边距(&T):

Element.MasterPage.type=类型(&Y):

Element.MasterPage.width=宽度(&W):

Element.Module.author=作者(&U):

Element.Module.base=基本:

Element.Module.colorPalette=调色板:

Element.Module.comments=备注(&C):

Element.Module.configVars=配置变量:

Element.Module.createdBy=创建者(&C):

Element.Module.description=描述(&E):

Element.Module.descriptionID=说明关键字:

Element.Module.helpGuide=帮助指南:

Element.Module.images=图像:

Element.Module.includeLibraries=包含库:

Element.Module.includeResource=资源文件(&E):

Element.Module.includeScripts=包含脚本:

Element.Module.initialize.toolTip=开始执行报表时调用。

Element.Module.initialize=初始化:

Element.LayoutModule.theme=布景主题(&H):

Element.Module.title=标题(&I):

Element.Module.titleID=标题关键字:

Element.Module.units=单位(&S):

Element.MultiLineData.contentTypeExpr=内容类型表达式:

Element.MultiLineData.valueExpr=值表达式:

Element.OdaDataSet.privateDriverDesignState=私有驱动程序设计状态:

Element.OdaDataSet.privateDriverProperties=私有驱动程序属性:

Element.OdaDataSet.publicDriverProperties=公共驱动程序属性:

Element.OdaDataSet.queryScript.toolTip=TBD:

Element.OdaDataSet.queryScript=脚本:

Element.OdaDataSet.queryText=查询:

Element.OdaDataSet.resultSetName=结果集名称:

Element.OdaDataSet.type=类型:

Element.OdaDataSource.driverName=驱动程序名称:

Element.OdaDataSource.privateDriverProperties=私有驱动程序属性:

Element.OdaDataSource.publicDriverProperties=公共驱动程序属性:

Element.Parameter.helpText=帮助文本:

Element.Parameter.helpTextID=帮助文本关键字(&Y):

Element.Parameter.hidden=隐藏:

Element.Parameter.promptTextID=提示文本关键字(&K):

Element.ParameterGroup.helpText=帮助文本:

Element.ParameterGroup.helpTextID=帮助文本关键字(&Y):

Element.ParameterGroup.slot.parameters=参数:

Element.ParameterGroup.startExpanded=开始展开:

Element.ParameterGroup.promptTextID=提示文本关键字(&Y):

Element.ReportDesign.afterCloseDoc.toolTip=仅在生成阶段关闭报表文档文件之后调用。

Element.ReportDesign.layoutPreference=布局首选项:

Element.ReportDesign.afterCloseDoc=关闭文档后:

Element.ReportDesign.afterFactory.toolTip=在生成阶段结束时并且在关闭报表文档（如果有）之后调用。这是生成阶段调用的最后一个方法。

Element.ReportDesign.afterFactory=出厂后:

Element.ReportDesign.afterOpenDoc.toolTip=仅在生成阶段打开报表文档之后调用。

Element.ReportDesign.afterOpenDoc=打开文档后:

Element.ReportDesign.afterRender.toolTip=开始展示时间操作后调用。

Element.ReportDesign.afterRender=实施后:

Element.ReportDesign.beforeCloseDoc.toolTip=仅在生成阶段关闭报表文档文件之前调用。

Element.ReportDesign.beforeCloseDoc=关闭文档前:

Element.ReportDesign.beforeFactory.toolTip=在生成阶段开始时，在 initialize( ) 方法之后并且在打开报表文档（如果有）之前调用。

Element.ReportDesign.beforeFactory=出厂前:

Element.ReportDesign.beforeOpenDoc.toolTip=仅在生成阶段打开报表文档之前调用。

Element.ReportDesign.beforeOpenDoc=打开文档之前:

Element.ReportDesign.beforeRender.toolTip=开始展示时间操作前调用。

Element.ReportDesign.beforeRender=实施前:

Element.ReportDesign.displayName=显示名称(&Y):

Element.ReportDesign.eventHandlerClass=事件处理程序类(&C):

Element.ReportDesign.iconFile=缩略图(&N):

Element.ReportDesign.refreshRate=刷新率:

Element.ReportDesign.bidiLayoutOrientation=报表方向(&R):

Element.ReportElement.comments=备注(&C):

Element.ReportElement.Cube=数据(&D):

Element.ReportElement.customXml=自定义 XML:

Element.ReportElement.displayName=显示名称(&Y):

Element.ReportElement.displayNameID=显示名称关键字:

Element.ReportElement.extends=扩展:

Element.ReportElement.javaClass=Java 类:

Element.ReportElement.name=名称(&N):

Element.ReportElement.propertyMask=属性掩码:

Element.ReportElement.style=样式(&Y):

Element.ReportItem.bookmark=书签(&B):

Element.ReportItem.dataSet=数据集:

Element.ReportItem.height=高度(&H):

Element.ReportItem.onCreate.toolTip=在生成阶段创建该元素时执行。

Element.ReportItem.onCreate=创建时:

Element.ReportItem.onRender.toolTip=在展示阶段准备该元素以便绘制时执行。

Element.ReportItem.onRender=实施时:

Element.ReportItem.paramBindings=参数绑定:

Element.ReportItem.toc=目录(&T):

Element.ReportItem.visibility=可见性:

Element.ReportItem.width=宽度(&W):

Element.ReportItem.x=X:

Element.ReportItem.y=Y:

Element.ReportItem.altText=替代文本

Element.ReportItem.altTextID=替代文本关键字

Element.Row.bookmark=书签:

Element.Row.height=高度(&H):

Element.Row.slot.cell=单元格:

Element.Row.visibility=可见性:

Element.ScalarParameter.alignment=对齐:

Element.ScalarParameter.allowBlank=允许为空:

Element.ScalarParameter.allowNull=允许 Null:

Element.ScalarParameter.concealValue=隐藏值:

Element.ScalarParameter.controlType=控件类型(&C):

Element.ScalarParameter.defaultValue=默认值:

Element.ScalarParameter.dynamicList.dataSetName=数据集名称:

Element.ScalarParameter.dynamicList.labelExpr=标签表达式:

Element.ScalarParameter.dynamicList.valueExpr=值表达式:

Element.ScalarParameter.dynamicList=动态列表:

Element.ScalarParameter.format=格式:

Element.ScalarParameter.selectionList.fixedOrder=固定顺序:

Element.ScalarParameter.selectionList.mustMatch=必须匹配:

Element.ScalarParameter.selectionList.selectionList=选择列表:

Element.ScalarParameter.selectionList=选择列表:

Element.ScalarParameter.promptTextID=提示文本关键字(&Y):

Element.ScriptDataSet.close.toolTip=关闭数据集。

Element.ScriptDataSet.close=关闭:

Element.ScriptDataSet.describe.toolTip=说明结果集。

Element.ScriptDataSet.describe=说明:

Element.ScriptDataSet.fetch.toolTip=创建、填写并返回类型“数据行”对象。

Element.ScriptDataSet.fetch=获取:

Element.ScriptDataSet.open.toolTip=打开数据集。

Element.ScriptDataSet.open=打开:

Element.ScriptDataSource.close.toolTip=断开与数据源的连接。

Element.ScriptDataSource.close=关闭:

Element.ScriptDataSource.open.toolTip=建立至数据源的连接。

Element.ScriptDataSource.open=打开:

Element.SimpleMasterPage.floatingFooter=浮动脚注(&F)

Element.SimpleMasterPage.footerHeight=页脚高度(&F):

Element.SimpleMasterPage.headerHeight=页眉高度(&H):

Element.SimpleMasterPage.showFooterOnLast=在最后一页显示题注(&F)

Element.SimpleMasterPage.showHeaderOnFirst=在第一页显示题头(&H)

Element.SimpleMasterPage.slot.pageFooter=页脚:

Element.SimpleMasterPage.slot.pageHeader=页眉:

Element.Style.background.attachment=附件(&A):

Element.Style.background.color=背景色(&B):

Element.Style.background.image=背景图像(&G):

Element.Style.background.positionX=水平位置(&H):

Element.Style.background.positionY=垂直位置(&V):

Element.Style.background.repeat=重复(&R):

Element.Style.background=背景:

Element.Style.border.bottom.color=下(&M):

Element.Style.border.bottom.style=下(&B):

Element.Style.border.bottom.width=底部(&:):

Element.Style.border.fillColor=边框填充色:

Element.Style.border.hmiddle.color=边框水平中间色:

Element.Style.border.hmiddle.style=边框水平中间样式:

Element.Style.border.hmiddle.width=边框水平中间宽度:

Element.Style.border.left.color=左(&F):

Element.Style.border.left.style=左(&L):

Element.Style.border.left.width=左(&E):

Element.Style.border.right.color=右(&H):

Element.Style.border.right.style=右(&R)

Element.Style.border.right.width=右(&I):

Element.Style.border.top.color=上(&P):

Element.Style.border.top.style=上(&T):

Element.Style.border.top.width=上(&O):

Element.Style.border.vmiddle.color=边框垂直中间色:

Element.Style.border.vmiddle.style=边框垂直中间样式:

Element.Style.border.vmiddle.width=边框垂直中间宽度:

Element.Style.border=边框:

Element.Style.box=框:

Element.Style.canShrink=可以缩小(&K)

Element.Style.color=颜色(&C):

Element.Style.display=显示(&P):

Element.Style.font.family=字体(&F):

Element.Style.font.size=大小(&Z):

Element.Style.font.style=样式(&T):

Element.Style.font.textLineThrough=删除线(&L)

Element.Style.font.textOverline=上划线(&O)

Element.Style.font.textUnderline=下划线(&U)

Element.Style.font.variant=字体变体:

Element.Style.font.weight=粗细(&W):

Element.Style.font=字体(&F):

Element.Style.format.dateTime=日期/时间:

Element.Style.format.number.align=格式数值对齐:

Element.Style.format.number=编号:

Element.Style.format.string=字符串:

Element.Style.format=格式:

Element.Style.highlight.rules=突出显示规则:

Element.Style.highlight.testExpr=突出显示测试表达式:

Element.Style.letterSpacing=字母间距(&T):

Element.Style.lineHeight=行高(&N):

Element.Style.map.rules=映射规则:

Element.Style.map.testExpr=映射测试表达式:

Element.Style.margin.bottom=下(&B):

Element.Style.margin.left=左(&E):

Element.Style.margin.right=右(&I)

Element.Style.margin.top=上(&P):

Element.Style.masterPage=主页(&P):

Element.Style.orphans=孤行(&O):

Element.Style.padding.bottom=下(&B):

Element.Style.padding.left=左(&L):

Element.Style.padding.right=右(&R)

Element.Style.padding.top=上(&T):

Element.Style.padding=填充:

Element.Style.pageBreakAfter=之后(&A):

Element.Style.pageBreakBefore=之前(&B):

Element.Style.pageBreakInside=内部(&I):

Element.Style.section=区域:

Element.Style.showIfBlank=如果为空白则显示(&W)

Element.Style.text.align=文本对齐(&L):

Element.Style.width=宽度(&W):

Element.Style.height=高度(&H):
#bidi_hcg
Element.Style.text.direction=双向文本方向(&B):

Element.Style.text.indent=文本缩进(&X):

Element.Style.text.transform=文本转换(&R):

Element.Style.text=文本:

Element.Style.verticalAlign=垂直对齐(&A):

Element.Style.whiteSpace=空格(&I):

Element.Style.widows=窗口(&D):

Element.Style.wordSpacing=字间距(&W):

Element.Table.caption=标题:

Element.Table.captionID=标题关键字:

Element.Table.repeatHeader=重复标题:

Element.Table.slot.columnHeader=columnHeader:
Element.Table.slot.columns=列:

Element.Table.slot.detail=详细资料:

Element.Table.slot.footer=页脚:

Element.Table.slot.group=组:

Element.Table.slot.header=页眉:

Element.TableGroup.slot.footer=页脚:

Element.TableGroup.slot.header=页眉:

Element.Text.content=内容:

Element.Text.contentID=内容关键字(&K):

Element.Text.contentType=内容类型(&P):

Element.TemplateElement.name=名称(&N)

Element.Toc.Sytle=样式(&Y)

Element.VariableElement.name=名称(&N):

Element.VariableElement.type=类型(&T):

Element.VariableElement.value=值(&V):

###########################################################
#EmbeddedImageNodeProvider properties
EmbeddedImageNodeProvider.action.New=新建嵌入图像(&B)

###########################################################
#EmbeddedImagesNodeProvider properties
EmbeddedImagesNodeProvider.action.New=新建嵌入图像(&B)

###########################################################
#ElementIdDescriptorProvider properties
ElementIdDescriptorProvider.Display.Element.ID=元素 ID(&E):

###########################################################
#EmptyEditPart properties
EmptyEditPart.Msg.EmptyLib=库未包含可见元素。要添加可见元素，将项目从选项板拖动到库大纲视图，或者使用大纲视图中的插入菜单。

EmptyEditPart.Msg.NonVisual=选择了不可见元素。

###########################################################
#error properties
error.duplicateName=新名称与同一名称空间中的现有名称重复。

error.DataSource.emptyName=数据源名称不能为空。

error.DataSet.emptyName=数据集名称不能为空。

error.OutputColumn.emptyName=输出列名称不能为空。

error.invalidName=名称“{0}”无效，因为它包含字符 "."、"/"、"\\"、"!"、";" 和 ","。

###########################################################
#ExceptionHandler properties
ExceptionHandler.Label.ErrorCode=错误代码

ExceptionHandler.Label.ErrorMessage=错误消息

ExceptionHandler.Label.PluginId=插件 ID:

ExceptionHandler.Label.PluginName=插件名称:

ExceptionHandler.Label.PluginProvider=插件提供程序:

ExceptionHandler.Label.PluginVersion=版本:

ExceptionHandler.Label.UnknownPlugin=未知插件

ExceptionHandler.Meesage.UnexceptedExceptionOccur=发生意外的内部异常。详情请参阅日志。

ExceptionHandler.Message.BirtExceptionOccur=发生 BIRT 异常。

ExceptionHandler.Message.CausedBy=由 {0} 引起。

ExceptionHandler.Message.FileNotFound=未找到文件:

ExceptionHandler.Message.OutOfMemory=内存不足。

ExceptionHandler.Message.PartInitError=部分初始化失败。

ExceptionHandler.Message.SemanticFail=语义检查失败。

ExceptionHandler.Message.UnexceptedExceptionOccur=出现内部异常。详情请参阅日志文件。

ExceptionHandler.Message.UnknownHost=未知主机:

ExceptionHandler.Title.Error=错误

ExceptionHandler.Title.FileNotFound=未找到文件。

ExceptionHandler.Title.PartInitError=部分初始化错误。

ExceptionHandler.Title.UnknownHost=未知主机

ExceptionHandler.Meesage.ExceptionOccur=发生异常。 详情请参阅日志。

###########################################################
#ExportAction properties
ExportAction.actionMsg.export=导出

###########################################################
#ExportToLibraryAction properties
ExportToLibraryAction.action.text=导出到库(&X)...

ExportToLibraryAction.Button.Cancel=取消

ExportToLibraryAction.Button.No=否

ExportToLibraryAction.Button.Yes=是

ExportToLibraryAction.Dialog.Message=\n目标库可能具有相同的设计元素。 是否要覆盖?

ExportToLibraryAction.Dialog.Title=导出元素

ExportToLibraryAction.Message.RememberDecision=记住我的决定(&R)


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.action.text=导出到库(&X)...

ExportToCPSouceAction.action.text=外部化连接配置文件(&O)...


###########################################################
#LibraryExplorerTreeViewPage tooltips
LibraryElementsToolHandleExtends_message=元素包含本地绑定而未指定显式数据集，这可能导致运行时性能低。 是否要将本地绑定移动到父层以获得更好的性能?

LibraryElementsToolHandleExtends_question=将本地绑定移动到父层以获得更好的性能?

LibraryElementsToolHandleExtends_toggle=记住我的决定(&R)

LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline=拖放到“大纲”视图

LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout=拖放到“布局”编辑器

LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout=拖放到“大纲”视图或“布局”编辑器

###########################################################
#ExpressionBuidler properties
ExpressionBuidler.Dialog.Title=表达式生成器(JavaScript)

ExpressionBuidler.Title=表达式生成器

ExpressionBuilder.tooltip.Sort=排序

ExpressionBuilder.toolTipText.Button=打开表达式生成器

###########################################################
#ExpressionBuilder properties
ExpressionBuilder.Label.Category=类别(&C):

ExpressionBuilder.Label.Functions=双击以插入(&I):

ExpressionBuilder.Label.Hint=提示

ExpressionBuilder.Label.Operators=运算符:

ExpressionBuilder.Label.SubCategory=子类别(&S):

ExpressionBuilder.Message.Prompt=在“表达式”字段中输入一个表达式。浏览可用对象列表并双击对象以将其复制到表达式中。

ExpressionBuilder.ToolTip=打开表达式生成器

ExpressionBuilder.toolTipText.validate=验证

ExpressionBuilder.Script.NoError=当前脚本中找不到语法错误。

ExpressionBuilder.Script.Error=[行:{0}, 列:{1}] {2}

ExpressionBuilder.Script.Warning=警告

ExpressionBuilder.Script.Confirm=\n当前脚本中找到语法错误。是否确实要保存?

ExpressionBuilder.toolTipText.calendar=日历

ExpressionBuilder.Calendar.Button.OK=确定

ExpressionButtonProvider.Constant=常量

ExpressionButtonProvider.Javascript=JavaScript 语法

###########################################################
#ExpressionPropertyDescriptor properties
ExpressionPropertyDescriptor.text.Edit=表达式生成器(&X)...

###########################################################
#ExpressionProvider properties
ExpressionProvider.Category.Context=上下文

ExpressionProvider.Category.BirtObjects=BIRT 函数

ExpressionProvider.Category.DataSets=可用数据集

ExpressionProvider.Category.NativeObjects=本地 JavaScript 函数

ExpressionProvider.Category.ColumnBinding=可用列绑定

ExpressionProvider.Category.Operators=运算符

ExpressionProvider.Category.Parameters=报表参数

ExpressionProvider.Category.DataCubes=当前多维数据集

ExpressionProvider.Category.Variables=变量

ExpressionProvider.Label.All=---全部---

ExpressionProvider.Operator.Add=将两个值相加。

ExpressionProvider.Operator.AddTo=在当前值上加值的快捷方式。

ExpressionProvider.Operator.And=判断两个表达式，并且在两个表达式均为真时返回真值。

ExpressionProvider.Operator.Assign=将一个值设置为与另一个值相等。

ExpressionProvider.Operator.BitAnd=A & B: 返回 A 和 B 的逐位 AND 结果。

ExpressionProvider.Operator.BitOr=A | B: 返回 A 和 B 的逐位 OR 结果。

ExpressionProvider.Operator.Dec=递减该值，然后返回结果。

ExpressionProvider.Operator.DividingFrom=除当前值的快捷方式。

ExpressionProvider.Tooltip.ColumnBinding=引用

ExpressionProvider.Operator.Divides=将左侧的值除以右侧的值并返回结果。

ExpressionProvider.Operator.Equals=如果这些项相同，则返回真值。

ExpressionProvider.Operator.Greater=A > B: 如果 A 大于 B，则返回真值。

ExpressionProvider.Operator.GreaterEquals=A >= B: 如果 A 大于或等于 B，则返回真值。

ExpressionProvider.Operator.Inc=递增该值，然后返回结果。

ExpressionProvider.Operator.LeftBracket=左括号

ExpressionProvider.Operator.RightBracket=右括号

ExpressionProvider.Operator.Less=A < B: 如果 A 小于 B，则返回真值。

ExpressionProvider.Operator.LessEqual=A <= B: 如果 A 小于或等于 B，则返回真值。

ExpressionProvider.Operator.Mult=将两个值相乘。

ExpressionProvider.Operator.MultTo=乘以当前值的快捷方式。

ExpressionProvider.Operator.Not=!A: A 为假则返回真，A 为真则返回假。

ExpressionProvider.Operator.NotEqual=A <> B: 如果 A 不等于 B，则返回真值。

ExpressionProvider.Operator.Or=判断两个表达式，并且在两个表达式之一为真时返回真值。

ExpressionProvider.Operator.ReturnDec=返回该值，然后递减该值。

ExpressionProvider.Operator.ReturnInc=返回该值，然后递增该值。

ExpressionProvider.Operator.Sub=从另一值中减去一个值。

ExpressionProvider.Operator.SubFrom=从当前值中减值的快捷方式。

ExpressionProvider.Operators.Assignment=赋值

ExpressionProvider.Operators.Comparison=比较

ExpressionProvider.Operators.Computational=计算的

ExpressionProvider.Operators.Logical=逻辑

ExpressionProvider.EditBindings=编辑绑定...

ExpressionProvider.Expression.RowNumName=行数

ExpressionProvider.Expression.RowNumTooltip=返回当前行数的表达式。

ExpressionProvider.Variables=变量

ExpressionProvider.Variables.Report=报表变量

ExpressionProvider.Variables.Page=页面变量

ExpressionTreeSupport.Category.Global=全局

###########################################################
#ExpressionValueCellEditor properties
ExpressionValueCellEditor.buildExpressionAction=<构建表达式...>

ExpressionValueCellEditor.selectValueAction=<选择值...>

ExpressionValueCellEditor.title=选择值

###########################################################
#ExtendedEditPart properties
ExtendedEditPart.edit=编辑 {0}

###########################################################
#ExtendedItemPageGenerator properties
ExtendedItemPageGenerator.List.Bookmark=书签

ExtendedItemPageGenerator.List.TOC=目录

###########################################################
#FilterHandleProvider properties
FilterHandleProvider.ColumnName.Value=值

FilterHandleProvider.Label.Filterby=筛选依据:

FilterHandleProvider.Modify.SaveValues=保存值

###########################################################
#filters properties
filters.empty.columnName=表达式不能为空。

filters.error.title=无效输入。

filters.operators.any=任意

filters.operators.between=介于

filters.operators.bottomN=下 (n) 项

filters.operators.bottomPercent=下 (n) 百分比

filters.operators.equal=等于

filters.operators.false=为假

filters.operators.greaterThan=大于

filters.operators.greaterThanEqual=大于或等于

filters.operators.lessThan=小于

filters.operators.lessThanEqual=小于或等于

filters.operators.like=类似

filters.operators.notBetween=不介于

filters.operators.notEqual=不等于

filters.operators.notNull=不为 NULL

filters.operators.null=为 NULL

filters.operators.topN=顶部 (n) 个项目

filters.operators.topPercent=顶部 (n) 百分比

filters.operators.true=为真

filters.prompt.new=<此处为新筛选...>

###########################################################
#FixTableLayoutCalculator properties
FixTableLayoutCalculator.Error.CannotParse=无法分析输入值。

###########################################################
#FlowMoveChildCommand properties
FlowMoveChildCommand.transLabel.moveElement=移动元素

###########################################################
#FontPreferencePage properties
FontPreferencePage.displayname.Title=字体

FontPreferencePage.label.fontDecoration=修饰:

FontPreferencePage.text.Preview=示例预览

FontPreferencePage.text.PreviewContent=AaBbYyZz

###########################################################
#FontSizeBuilder properties
FontSizeBuilder.displayname.Auto=自动

###########################################################
#FormatBuilder properties
FormatAdapter.DisplayName.None=自动

FormatBuilder.Title=格式生成器

###########################################################
#FormatDateTimeAttributePage properties
FormatDateTimeAttributePage.Trans.SetDateTimeFormat = 设置日期时间格式。

###########################################################
#FormatDateTimePage properties
FormatDateTimePage.label.custom.settings.label=格式示例 (单击下面要复制的一项，然后进行修改)(&P):

FormatDateTimePage.label.custom.settings=自定义设置

FormatDateTimePage.label.format.code=格式代码(&C):

FormatDateTimePage.label.format.page=格式为(&F):

FormatDateTimePage.label.format.locale=区域设置(&L):

FormatDateTimePage.label.general.preview.group=带格式预览

###########################################################
# LOCALE SPECIAL:
# The datetime pattern string in this text should be formatted locale-dependent if needed
FormatDateTimePage.label.guide.format=yyyy/MM/dd hh:mm:ss a

FormatDateTimePage.label.guide.text=请按以下格式输入: yyyy/MM/dd hh:mm:ss AM/PM

FormatDateTimePage.label.preview.dateTime=要预览的日期时间(&P):

FormatDateTimePage.label.preview.group=预览

FormatDateTimePage.label.preview.label=带格式预览:

FormatDateTimePage.label.table.column.format.name=示例格式

FormatDateTimePage.label.table.column.format.result=示例格式结果

FormatDateTimePage.label.table.column.format.code=示例代码

FormatDateTimePage.preview.invalid.dateTime=要预览的日期时间无效

FormatDateTimePage.preview.invalid.formatCode=无效格式代码

###########################################################
#FormatDateTimePreferencePage properties
FormatDateTimePreferencePage.formatDateTime.title=日期时间格式

###########################################################
#FormatNumberAttributePage properties
FormatNumberAttributePage.Trans.SetNumberFormat = 设置数值格式。

###########################################################
#FormatNumberPage properties
FormatNumberPage.currency.symbol.none=无符号

FormatNumberPage.label.currency.settings=货币设置

FormatNumberPage.label.custom.preview.group=预览

FormatNumberPage.label.custom.preview.label=带格式预览:

FormatNumberPage.label.custom.settings.lable=格式示例 (单击下面要复制的一项，然后进行修改)(&P):

FormatNumberPage.label.custom.settings=自定义设置

FormatNumberPage.label.decimal.places=小数位(&D):

FormatNumberPage.label.rounding.mode=舍入模式(&R):

FormatNumberPage.label.fixed.settings=固定设置

FormatNumberPage.label.format.code=格式代码(&C):

FormatNumberPage.label.format.number.page=格式为(&F):

FormatNumberPage.label.format.number.locale=区域设置(&L):

FormatNumberPage.label.general.preview.group=带格式预览

FormatNumberPage.label.negative.numbers=负数(&N):

FormatNumberPage.label.percent.settings=百分比设置

FormatNumberPage.label.preview.number=要预览的数值(&P):

FormatNumberPage.label.scientific.settings=科学记数设置

FormatNumberPage.label.symbol.position=符号位置(&Y):

FormatNumberPage.label.symbol=符号(&B):

FormatNumberPage.label.table.column.format.code=示例格式

FormatNumberPage.label.table.column.format.result=示例格式结果

FormatNumberPage.label.use1000sSeparator=使用千位分隔符(&1)

FormatNumberPage.label.useLeadingZero=使用前导零

FormatNumberPage.label.useSymbolSpace=使用符号空间(&U)

FormatNumberPage.preview.invalidFormatCode=无效格式代码

FormatNumberPage.preview.invalidNumber=要预览的数值无效

FormatNumberPage.symblePos.after=之后

FormatNumberPage.symblePos.before=之前

FormatNumberPage.roundingMode.halfUp=向最近的一边舍入，距离相等时向上舍入

FormatNumberPage.roundingMode.halfDown=向最近的一边舍入，距离相等时向下舍入

FormatNumberPage.roundingMode.halfEven=向最近的一边舍入，距离相等时向偶数边舍入

FormatNumberPage.roundingMode.up=上移

FormatNumberPage.roundingMode.down=下移

FormatNumberPage.roundingMode.ceiling=向正无穷方向舍入

FormatNumberPage.roundingMode.floor=向负无穷方向舍入

FormatNumberPage.roundingMode.unnecessary=不需要舍入

###########################################################
#FormatNumberPreferencePage properties
FormatNumberPreferencePage.formatNumber.title=数值格式

###########################################################
#FormatPreferencePage properties
FormatPreferencePage.displayname.Title=格式

###########################################################
#FormatStringAttributePage properties
FormatStringAttributePage.Trans.SetStringFormat = 设置字符串格式。

###########################################################
#FormatStringPage properties
FormatStringPage.default.preview.text=我的字符串

FormatStringPage.label.custom.preview.label=带格式预览:

FormatStringPage.label.custom.settings.label=格式示例 (单击下面要复制的一项，然后进行修改)(&P):

FormatStringPage.label.customSettings=自定义设置

FormatStringPage.label.format.code=格式代码(&C):

FormatStringPage.label.formatStringAs=格式为(&F):

FormatStringPage.label.locale=区域设置(&L):

FormatStringPage.label.preview.group=预览

FormatStringPage.label.preview.string=要预览的字符串(&R):

FormatStringPage.label.previewWithFormat=带格式预览

FormatStringPage.label.table.collumn.exampleFormatCode=示例格式

FormatStringPage.label.table.collumn.exampleFormatResult=示例格式结果

FormatStringPage.previewText.invalidFormatCode=无效格式代码

###########################################################
#FormatStringPreferencePage properties
FormatStringPreferencePage.formatString.title=字符串格式

###########################################################
#FormPage properties
FormPage.Button.Add=添加(&A)

FormPage.Button.AddWithDialog=添加(&A)...

FormPage.Button.AddWithDialog.PROP=添加文件(&A)...

FormPage.Button.AddWithDialog.JAR=添加文件(&L)...

FormPage.Button.AddWithDialog.JS=添加文件&...

FormPage.Button.Add.AggregateOn=添加聚合(&G)

FormPage.Button.AddWithDialog.AggregateOn=添加聚合(&G)...

FormPage.Button.Delete=删除(&R)

FormPage.Button.DeleteFile=删除文件(&R)

FormPage.Button.DeleteFile.Alt1=删除文件(&I)

FormPage.Button.DeleteFile.Alt2=删除文件(&F)

FormPage.Button.Down=下移(&N)

FormPage.Button.Down.Alt1=下移(&W)

FormPage.Button.Down.Alt2=下移(&D)

FormPage.Button.Edit=编辑(&D)

FormPage.Button.EditWithDialog=编辑(&D)...

FormPage.Button.Up=上移(&U)

FormPage.Button.Up.Alt1=上移(&P)

FormPage.Button.Up.Alt2=上移(&E)

FormPage.Button.Binding.Refresh=刷新(&F)

FormPage.Button.Binding.Clear=清除(&C)

FormPage.Menu.ModifyProperty=修改属性

FormPage.toolTipText.Up=上移

FormPage.toolTipText.Down=下移
###########################################################
#GeneralInsertMenuAction properties
GeneralInsertMenuAction.text.Data=数据(&D)

GeneralInsertMenuAction.text.DynamicText=动态文本(&Y)

GeneralInsertMenuAction.text.Grid=网格(&G)

GeneralInsertMenuAction.text.Image=图像(&I)

GeneralInsertMenuAction.text.Label=标签(&L)

GeneralInsertMenuAction.text.List=列表(&S)

GeneralInsertMenuAction.text.Table=表(&B)

GeneralInsertMenuAction.text.Text=文本(&T)

###########################################################
#GeneralPreferencePage properties
GeneralPreferencePage.errorMsg.duplicate.styleName=重复的样式名称。

GeneralPreferencePage.label.styleNameDuplicate=样式名称“{0}”已存在，请选择另一个名称

GeneralPreferencePage.label.styleNamePredefind=样式名称“{0}”已预定义，请选择另一个名称

GeneralPreferencePage.label.customStyle=自定义样式(&C)

GeneralPreferencePage.Label.CustomStyle=名称:

GeneralPreferencePage.Label.Note=注: * 将样式属性应用于指定类型的报表元素。

GeneralPreferencePage.Label.PreDefinedStyle=选择报表元素:

GeneralPreferencePage.label.predefinedStyle=预定义样式(&P) *

GeneralPreferencePage.label.nameEmpty=不允许空样式名称。

###########################################################
#GridEditPart properties
GraphicalEditorWithFlyoutPalette_Redo.Text0=恢复(&R)

GraphicalEditorWithFlyoutPalette_Redo.Text1=恢复 {0}(&R)

GraphicalEditorWithFlyoutPalette_Redo.ToolTip0=恢复

GraphicalEditorWithFlyoutPalette_Redo.ToolTip1=恢复 {0}

GraphicalEditorWithFlyoutPalette_Undo.Text0=撤消(&U)

GraphicalEditorWithFlyoutPalette_Undo.Text1=撤消 {0}(&U)

GraphicalEditorWithFlyoutPalette_Undo.ToolTip0=撤消

GraphicalEditorWithFlyoutPalette_Undo.ToolTip1=撤消 {0}

GridEditPart.GUIDEHANDLE_TEXT = 网格

###########################################################
#GridFigure properties
GridFigure.BORDER_TEXT=网格

###########################################################
#GridPageGenerator properties
GridPageGenerator.List.Bookmark=书签

GridPageGenerator.List.Borders=边框

GridPageGenerator.List.Font=字体

GridPageGenerator.List.General=常规

GridPageGenerator.List.Margin=边距

GridPageGenerator.List.Section=分页符

GridPageGenerator.List.TOC=目录

GridPageGenerator.List.Visibility=可见性

###########################################################
#GridProvider properties
GridProvider.action.text=插入行(&S)

###########################################################
#GroupDialog properties
GroupDialog.Dialog.GroupDetail=组详细资料

GroupDialog.Label.Bookmark=书签(&B):

GroupDialog.Dialog.TOCGroup=目录

GroupDialog.Dialog.TOC=项目表达式(&T)

GroupDialog.Dialog.TOCStyle=样式(&Y):

GroupDialog.DialogTitle.NoData=初始化失败

GroupDialog.DialogTitle.SemanticCheckFailed=语义检查失败

GroupDialog.ErrorMesaage.IllegalGroup=此组非法。

GroupDialog.ErrorMessage.NoData=没有可用的数据行。

GroupDialog.Label.FilterSorting=筛选器和排序

GroupDialog.Label.GroupOn=分组依据(&G):

GroupDialog.Label.HeaderFooter=页眉和页脚

GroupDialog.Label.IncludeFooter=包含组页脚(&F)

GroupDialog.Label.IncludeHeader=包含组页眉(&H)

GroupDialog.Label.Interval=间隔(&V):

GroupDialog.PageBreakBefore=之前(&O):

GroupDialog.PageBreakAfter=之后(&F):

GroupDialog.PageBreakInside=内部(&I):

GroupDialog.Label.IntervalBase=对间隔使用固定基值(&X):

GroupDialog.Label.Name=名称(&M):

GroupDialog.Label.Range=范围(&E):

GroupDialog.Tab.Binding=数据绑定

GroupDialog.Tab.Filter=筛选器

GroupDialog.Tab.Sorting=排序

GroupDialog.RepeatHeader=重复标题(&P)

GroupDialog.Title.Edit=编辑组

GroupDialog.Title.New=新建组

GroupDialog.PageBreak=分页符

GroupDialog.buttion.HideDetail=隐藏详细信息(&H)

GroupDialog.toolTipText.openExprButton=打开表达式生成器

GroupDialog.Button.Ascending=升序(&A)

GroupDialog.Button.Descending=降序(&C)
###########################################################
#GroupHandleProvider properties
GroupHandleProvider.Label.Groups=组列表:

###########################################################
#GroupProvider properties
GroupProvider.action.text=编辑组(&E)

###########################################################
#GuiExtensionManager properties
GuiExtensionManager.label.name=扩展项

GuiExtensionManager.tooltip.insert=插入 {0}

###########################################################
#HighlightHandleProvider properties
HighlightHandleProvider.text.Preview=AaBbCcYyZz (字体大小 =

###########################################################
#HighlightRuleBuilderDialog properties
HightlightRuleBuilder.button.add=添加(&A)

HightlightRuleBuilder.button.add.tooltip=将值添加到值列表

HightlightRuleBuilder.button.delete=删除(&M)

HightlightRuleBuilder.button.delete.tooltip=从列表中删除选定值

HightlightRuleBuilder.button.deleteall=全部删除(&O)

HightlightRuleBuilder.button.deleteall.tooltip=从列表中删除所有值

HightlightRuleBuilder.button.edit=编辑(&E)...

HightlightRuleBuilder.button.edit.tooltip=编辑值列表中的选定值

HightlightRuleBuilder.label.value=值(&V):

HightlightRuleBuilder.list.item1=值

HightlightRuleBuilder.MsgDlg.Msg=警告

HightlightRuleBuilder.MsgDlg.Title=值不能为空。

HightlightRuleBuilder.text.AND=AND

HighlightRuleBuilderDialog.choice.ValueOfThisDataItem=此数据项的值

HighlightRuleBuilderDialog.text.AND=与

HighlightRuleBuilderDialog.text.Any=任意

HighlightRuleBuilderDialog.text.BackgroundColor=背景色(&B):

HighlightRuleBuilderDialog.text.Between=介于

HighlightRuleBuilderDialog.text.Color=颜色(&C):

HighlightRuleBuilderDialog.text.Default=自动

HighlightRuleBuilderDialog.text.ET=等于

HighlightRuleBuilderDialog.text.False=为假

HighlightRuleBuilderDialog.text.Font=字体(&F):

HighlightRuleBuilderDialog.text.GE=大于或等于

HighlightRuleBuilderDialog.text.GT=大于

HighlightRuleBuilderDialog.text.LE=小于或等于

HighlightRuleBuilderDialog.text.Like=类似

HighlightRuleBuilderDialog.text.LT=小于

HighlightRuleBuilderDialog.text.NE=不等于

HighlightRuleBuilderDialog.text.NotBetween=不介于

HighlightRuleBuilderDialog.text.NotNull=不为 NULL

HighlightRuleBuilderDialog.text.Null=为 NULL

HighlightRuleBuilderDialog.text.Preview=预览:

HighlightRuleBuilderDialog.text.PreviewContent=AaBbCcYyZz
HighlightRuleBuilderDialog.text.Size=大小(&Z):

HighlightRuleBuilderDialog.text.Title=突出显示

HighlightRuleBuilderDialog.text.True=为真

HighlightRuleBuilderDialog.tooltip.ExpBuilder=打开表达式生成器

HighlightRuleBuilderDialog.tooltip.Bold=粗体

HighlightRuleBuilderDialog.tooltip.Italic=斜体

HighlightRuleBuilderDialog.tooltip.Underline=下划线

HighlightRuleBuilderDialog.tooltip.Text_Line_Through=文本删除线

HighlightRuleBuilderDialog.text.applyStyle=应用样式(&Y):

HighlightRuleBuilderDialog.displayText.None=无

HighlightRuleBuilderDialog.text.Description=向 {0} 应用条件格式。请指定条件表达式和格式。\n如果表达式判定为“真”，则将应用格式。

HighlightRuleBuilderDialog.text.Description.Element.Table=表

HighlightRuleBuilderDialog.text.Description.Element.Grid=网格

HighlightRuleBuilderDialog.text.Description.Element.List=列表

HighlightRuleBuilderDialog.text.Description.Element.Row=行

HighlightRuleBuilderDialog.text.Description.Element.Column=列

HighlightRuleBuilderDialog.text.Description.Element.DataItem=数据项

HighlightRuleBuilderDialog.text.Description.Element.Cell=单元格

HighlightRuleBuilderDialog.text.Group.Condition=条件(&D)

HighlightRuleBuilderDialog.text.Group.Format=格式

HighlightRuleBuilderDialog.text.Description.Element.ReportElement=报表元素

###########################################################
#HighlightsPage properties
HighlightsPage.Button.Add=添加(&A)...

HighlightsPage.Button.Delete=删除(&R)

HighlightsPage.Dialog.EditHighlight=编辑突出显示

HighlightsPage.Dialog.NewHighlight=新建突出显示

HighlightsPage.Label.Highlights=突出显示列表:

HighlightsPage.TableColumn.Condition=条件

HighlightsPage.TableColumn.Preview=预览:

HighlightsPage.trans.Add=添加突出显示规则

HighlightsPage.trans.Delete=删除突出显示规则

HighlightsPage.trans.Edit=编辑突出显示规则

HighlightsPage.trans.MoveDown=下移突出显示规则

HighlightsPage.trans.MoveUp=上移突出显示规则

HighlightsPage.toolTipText.MoveDown=突出显示规则下移

HighlightsPage.toolTipText.MoveUp=突出显示规则上移

HighlightsPage.label.duplicate=复制(&C)

HighlightsPage.toolTipText.duplicate=重复突出显示规则

HighlightsPage.trans.Duplicate=重复突出显示规则

###########################################################
#HighlightsPreferencePage properties
HighlightsPreferencePage.displayname.Condition=条件

HighlightsPreferencePage.displayname.Preview=预览

HighlightsPreferencePage.displayname.Title=突出显示

HighlightsPreferencePage.text.Add=添加(&A)...

HighlightsPreferencePage.text.Delete=删除(&R)

HighlightsPreferencePage.text.Description=突出显示列表:

HighlightsPreferencePage.text.Edit=编辑(&D)...

HighlightsPreferencePage.toolTipText.Up=突出显示上移

HighlightsPreferencePage.toolTipText.Down=突出显示下移

HighlightsPreferencePage.text.Duplicate=重复(&D)

HighlightsPreferencePage.toolTipText.Duplicate=重复突出显示


###########################################################
#HyperlinkBuilder properties
HyperlinkBuilder.Column.Parameters=参数

HyperlinkBuilder.DrillThrough.SelectTargetReport=选择目标文件

HyperlinkBuilder.DrillThrough.SelectTargetAnchor=选择目标定位标记

HyperlinkBuilder.DrillThrough.CreateLinkExpr=创建与目标书签匹配的链接表达式

HyperlinkBuilder.DrillThrough.ShowTargetReport=显示以下范围内的目标报表

HyperlinkBuilder.DrillThrough.SelectFormat=为目标报表选择格式

HyperlinkBuilder.DrillThrough.Tooltip=工具提示

HyperlinkBuilder.Step.1=第 1 步:

HyperlinkBuilder.Step.2=第 2 步:

HyperlinkBuilder.Step.3=第 3 步:

HyperlinkBuilder.Step.4=第 4 步:

HyperlinkBuilder.Step.5=第 5 步:

HyperlinkBuilder.Step.6=第 6 步:

HyperlinkBuilder.ParameterRequired=必需

HyperlinkBuilder.ParamterBuilder.Add.Title=添加超级链接参数

HyperlinkBuilder.ParamterBuilder.Edit.Title=编辑超级链接参数

HyperlinkBuilder.Column.DataType=数据类型

HyperlinkBuilder.Column.Values=值

HyperlinkBuilder.DialogTitle=超级链接选项

HyperlinkBuilder.ErrorMessage.InvalidReport=无法解析目标报表设计，预定义参数信息将不可用。

HyperlinkBuilder.ErrorMessage.ReportReqired=必须指定报表文件。

HyperlinkBuilder.DrillThroughTargetBookmark=目标书签(&T)

HyperlinkBuilder.DrillThroughLinkExpression=链接表达式(&E):

HyperlinkBuilder.Label.Bookmark=书签(&B):

HyperlinkBuilder.Label.Format=格式:

HyperlinkBuilder.Label.LinkedExpression=链接表达式(&E):

HyperlinkBuilder.DrillThroughTargetToc=目标报表中的目录项(&B)

HyperlinkBuilder.DrillThroughSameFrame=相同框架(&S)

HyperlinkBuilder.DrillThroughNewWindow=新建窗口(&N)

HyperlinkBuilder.DrillThroughWholePage=整页(&W)

HyperlinkBuilder.DrillThroughParentFrame=父框架(&P)

HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName=重复的参数名称

HyperlinkBuilder.Label.Location=位置(&L):

HyperlinkBuilder.Label.Location.hint=(示例: http://host/path, folder/file)

HyperlinkBuilder.Label.Parameters=报表参数:

HyperlinkBuilder.Label.Report=报表:

HyperlinkBuilder.Label.SelectType=选择超级链接类型:

HyperlinkBuilder.DrillThroughHtml=HTML
HyperlinkBuilder.Label.Target=目标(&T):

HyperlinkBuilder.Label.Tooltip=工具提示(&I):

HyperlinkBuilder.Radio.Bookmark=内部书签(&K)

HyperlinkBuilder.DrillThroughPdf=PDF
HyperlinkBuilder.Radio.DrillThrough=钻取(&D)

HyperlinkBuilder.ReportDesignButton=报表设计(&R):

HyperlinkBuilder.ReportDocumentButton=报表文档(&C):

HyperlinkBuilder.Radio.None=无链接(&O)

HyperlinkBuilder.Radio.Uri=&URI
HyperlinkBuilder.BrowseForFile=浏览文件

HyperlinkBuilder.Button.AddParam=添加(&A)...

HyperlinkBuilder.Button.EditParam=编辑(&)...

HyperlinkBuilder.Button.RemoveAllParam=全部删除(&L)

HyperlinkBuilder.Button.RemoveParam=删除(&M)

HyperlinkBuilder.OpenExpression=打开表达式生成器

HyperlinkBuilder.FileNameError.Title=错误

HyperlinkBuilder.FileNameError.Message=选择的文件无效。

HyperlinkBuilder.TargetReportFormat=格式化以下位置的目标报表(&F):

HyperlinkParameterBuilder.Label.DataType=数据类型:

HyperlinkParameterBuilder.Label.Parameter=参数(&P):

HyperlinkParameterBuilder.Label.Value=值(&V):

HyperlinkParameterBuilder.Lable.Required=必需:

HyperlinkParameterBuilder.Required.Choice.No=否

HyperlinkParameterBuilder.Required.Choice.Yes=是

###########################################################
#HyperLinkPage properties
HyperLinkPage.Label.LnikTo=链接至(&L):

HyperLinkPage.Label.None=无

HyperLinkPage.Menu.Save=更改超级链接

HyperLinkPage.Button.Text=编辑(&T)...

HyperLinkPage.toolTipText.Button=打开超级链接选项

###########################################################
#I18nPage properties
I18nPage.text.Note=注：要设置本地化，请选择报表，然后在属性编辑器的“资源”选项卡上设置资源文件名称。

###########################################################
#ImageBuilder properties
ImageBuilder.Chooser.Title=打开图像文件

ImageBuilder.DialogTitle.Edit=编辑图像项

ImageBuilder.DialogTitle.ImportFailed=导入失败

ImageBuilder.DialogTitle.LoadingFailed=图像加载失败

ImageBuilder.DialogTitle.New=新建图像项

ImageBuilder.ErrorMessage.FileLoaded=文件名已存在，请进行更改并重新加载。

ImageBuilder.ErrorMessage.LoadingFailed=无法正确加载图像。

ImageBuilder.BrowserResourceDialog.Title=浏览图像文件

ImageBuilder.BrowserResourceDialog.Message=从资源路径文件夹中选择图像文件

ImageBuilder.ErrorMessage.OutOfMemory=该图像太大，无法装入内存。

ImageBuilder.FileDialog.FilterMessage=支持的图像文件

ImageBuilder.Button.Browser=浏览...

ImageBuilder.Button.Import=添加图像(&A)...

ImageBuilder.Button.SelectImageData=选择图像数据(&L)...

ImageBuilder.Button.Insert=插入(&S)

ImageBuilder.Label.SelectFrom=选择图像自:

ImageBuilder.Label.EnterURI=输入 URI(&N):

ImageBuilder.Label.EnterResourceFile=输入资源文件(&N):

ImageBuilder.Label.EnterExpr=输入动态图像表达式(&N):

ImageBuilder.Type.URI=&URI
ImageBuilder.Type.ResourceFile=共享资源中的图像文件(&I)

ImageBuilder.Type.EmbededImage=嵌入图像(&E)

ImageBuilder.Type.DynamicImage=动态图像(&D)

ImageBuilder.Description.URI=链接到 web 上的图像

ImageBuilder.Description.ResourceFile=链接到共享资源中的图像

ImageBuilder.Description.Embedded=将图像嵌入到报表(&M)

ImageBuilder.Description.Dynamic=从数据集(数据库)中检索图像

ImageBuilder.Button.Browser.Tooltip=浏览图像文件

ImageBuilder.Reload.Tooltip=重新加载图像

###########################################################
#ImageEditPart properties
ImageEditPart.trans.editImage=更改图像

###########################################################
#ImagePageGenerator properties
ImagePageGenerator.List.AltText=替代文本

ImagePageGenerator.List.Bookmark=书签

ImagePageGenerator.List.Borders=边框

ImagePageGenerator.List.General=常规

ImagePageGenerator.List.HyperLink=超级链接

ImagePageGenerator.List.Reference=引用

ImagePageGenerator.List.Section=分页符

ImagePageGenerator.List.TOC=目录

ImagePageGenerator.List.Visibility=可见性

###########################################################
#ImportCSSStyleAction properties
ImportCSSStyleAction.text=导入 CSS 样式(&I)...

###########################################################
#ImportLibraryAction properties
ImportLibraryAction.Error.CannotBeEmpty=名称空间不能为空。

ImportLibraryAction.Error.HasBeenUsed=此名称空间已经在使用。

ImportLibraryAction.Error.HasDot=名称空间不应包含'.'

ImportLibraryAction.Message.ImportSuccessfully=库 {0} 已导入。

ImportLibraryAction.Message=是否要在报表设计中使用此库?

ImportLibraryAction.Namespace=名称空间:

ImportLibraryAction.Prefix=前缀:

ImportLibraryAction.Title.ImportSuccessfully=导入成功

ImportLibraryAction.Dialog.Titile=使用库

ImportLibraryAction.Dialog.Message=选择要包括在报表中的库。

###########################################################
#ImportLibraryDialog properties
ImportLibraryDialog.Title=使用库

###########################################################
#ImportValueDialog properties
ImportValueDialog.Label.SelectColumn=选择列(&C):

ImportValueDialog.Label.SelectDataSet=选择数据集(&D):

ImportValueDialog.Label.SelectValue=选择或输入要添加的值(&S)

ImportValueDialog.Title=导入值

ImportValueDialog.Button.Add.ToolTip=添加

ImportValueDialog.Button.AddAll.ToolTip=全部添加

ImportValueDialog.Button.Remove.ToolTip=删除

ImportValueDialog.Button.RemoveAll.ToolTip=全部删除
###########################################################
#IncludeDetailAction properties
IncludeDetailAction.actionMsg.includeDetail=详细信息(&D)

###########################################################
#IncludeFooterAction properties
IncludeFooterAction.actionMsg.includeFooter=页脚(&F)

###########################################################
#IncludeHeaderAction properties
IncludeHeaderAction.actionMsg.includeHeader=页眉(&H)

###########################################################
#IncludeListDetailAction properties
IncludeListDetailAction.actionMsg.includeDetail=包含详细资料

###########################################################
#IncludeListFooterAction properties
IncludeListFooterAction.actionMsg.includeFooter=包含页脚

###########################################################
#IncludeListHeaderAction properties
IncludeListHeaderAction.actionMsg.includeHeader=包含页眉

###########################################################
#IncludeTableGroupFooterAction properties
IncludeTableGroupFooterAction.actionMsg.includeFooter=组页脚

###########################################################
#IncludeTableGroupHeaderAction properties
IncludeTableGroupHeaderAction.actionMsg.includeHeader=组页眉

###########################################################
#InputParameterDailog properties
InputParameterDailog.dialog.title.required=所需的参数 - 必须具有值

InputParameterDailog.dialog.title=输入参数

InputParameterDailog.parameter.noparameter=没有与此报表相关联的参数。

InputParameterDialog.ErrorMessage.InvalidFile=报表包含错误。

InputParameterDailog.parameter.null=空值

InputParameterDialog.ErrorTitle.Error=错误

InputParameterDialog.boolean.checked=是

###########################################################
#InsertAction properties
InsertAction.text=插入元素(&N)

###########################################################
#InsertColumnAction properties
InsertColumnAction.actionMsg.insert=插入

###########################################################
#InsertColumnLeftAction properties
InsertColumnLeftAction.actionMsg.columnToLeft=在左侧插入列(&L)

###########################################################
#InsertColumnRightAction properties
InsertColumnRightAction.actionMsg.columnToRight=在右侧插入列(&R)

###########################################################
#InsertExpressionMenuAction properties
InsertExpressionMenuAction.text.Expression=表达式...

###########################################################
#InsertGroupAction properties
InsertGroupAction.actionMsg.group=插入组(&G)

InsertGroupAction.stackMsg.addGroup=添加组

###########################################################
#InsertGroupActionFactory properties
InsertGroupActionFactory.label.insertGroup=插入组(&G)

###########################################################
#InsertInLayoutAction properties
InsertInLayoutAction.action.text=在布局中插入(&N)

###########################################################
#InsertListGroupAction properties
InsertListGroupAction.actionMsg.insertGroup=插入组

InsertListGroupAction.stackMsg.insertListGroup=插入列表组

###########################################################
#InsertPasteColumnAction properties
InsertPasteColumnAction.text=插入复制的列(&S)

###########################################################
#InsertPositionGroupAction properties
InsertPositionGroupAction.Label.Above=在上面(&A)

InsertPositionGroupAction.Label.Below=在下面(&B)

InsertPositionGroupAction.Label.Into=到

###########################################################
#InsertRowAboveAction properties
InsertRowAboveAction.actionMsg.rowAbove=在上面(&A)

###########################################################
#InsertRowAction properties
InsertRowAction.actionMsg.insert=插入(&I)

###########################################################
#InsertRowBelowAction properties
InsertRowBelowAction.actionMsg.rowBelow=在下面(&B)

###########################################################
#IReportGraphicConstants properties
IReportGraphicConstants.ImageType.All=所有图像文件 (*.bmp,*.JPG,*.JPEG,*.GIF,*.TIF,*.PNG,*.ICO)

IReportGraphicConstants.ImageType.Bmp=位图文件 (*.bmp)

IReportGraphicConstants.ImageType.Gif=GIF 文件 (*.GIF)

IReportGraphicConstants.ImageType.Icon=图标文件 (*.ICO)

IReportGraphicConstants.ImageType.Jpg=JPEG 文件 (*.JPG,*.JPEG)

IReportGraphicConstants.ImageType.Png=PNG 文件 (*.PNG)

IReportGraphicConstants.ImageType.Tif=TIF 文件 (*.TIF)

###########################################################
#JSDocumentProvider properties
JSDocumentProvider.Content.Default=新内容

JSDocumentProvider.document.newName=新内容

###########################################################
#JSEditor properties
JSEditor.cmb.NewEventFunction=新建事件函数...

JSEditor.Label.Script=脚本:

JSEditor.Button.Validate=验证脚本

JSEditor.Button.Reset=重置脚本

JSEditor.Button.Help=显示描述

JSEditor.Display.DefaultText=//JS 脚本编辑器 \n

JSEditor.Display.NoExpression=此元素无方法。

JSEditor.Input.trial=试用版

JSEditor.Text.NoText=无文本

JSEditor.Text.NoDescription=此方法没有描述。

JSEditor.Validate.Title=验证

JSEditor.Validate.NoError=当前脚本中找不到语法错误。

JSEditor.Folding.Group=折叠

JSEditor.Folding.ExpandAll.label=全部展开

JSEditor.Folding.CollapseAll.label=全部折叠

JSEditor.Folding.CollapseMethods.label=折叠方法

JSEditor.Folding.CollapseComments.label=折叠备注

###########################################################
#label properties
label.defaultValue=（默认值）

label.input=输入

label.inputOutput=输入/输出

label.no=否

label.output=输出

label.redo=重复

label.undo=撤消

label.yes=是

###########################################################
#LabelEditPart properties
LabelEditPart.Figure.Default=标签

###########################################################
#LabelPageGenerator properties
LabelPageGenerator.List.Bookmark=书签

LabelPageGenerator.List.Borders=边框

LabelPageGenerator.List.General=常规

LabelPageGenerator.List.HyperLink=超级链接

LabelPageGenerator.List.I18n=本地化

LabelPageGenerator.List.Margin=边距

LabelPageGenerator.List.Padding=填充

LabelPageGenerator.List.Section=分页符

LabelPageGenerator.List.TOC=目录

LabelPageGenerator.List.Visibility=可见性

###########################################################
#LayoutConfigurationBlock properties
LayoutConfigurationBlock.Default.Unit.Auto=自动

LayoutConfigurationBlock.Default.Unit.Label=默认单位(&F):
LayoutConfigurationBlock.Default.Layout.Label=默认布局:
LayoutConfigurationBlock.Default.Orientation.Label=默认方向:

###########################################################
#LayoutEditor properties
LayoutEditor.ErrorMessage.OpenError=打开编辑器时出错。

LayoutEditor.Page.Layout=正常

LayoutEditor.Page.MasterPage=主页

LayoutEditor.Page.Source=XML 源

###########################################################
#LayoutPageAction properties
LayoutPageAction.actionText.layout=布局

###########################################################
#LibrariesNodeProvider properties
LibrariesNodeProvider.DisplayLabel.Libraries=库

###########################################################
#LibraryExplorerView properties
LibraryExplorerView.defaultText.notAvailable=资源不可用。

###########################################################
#LibraryExplorerProvider properties
LibraryExplorerProvider.FolderNotExist=首选项中定义的 BIRT 资源文件夹不存在。

###########################################################
#LibraryPage properties
LibraryPage.Label.Namespace=名称空间(&N):

LibraryPage.Label.Path=路径(&P):

###########################################################
#LibraryProvider properties
LibraryProvider.openDefinedLibrary.error.dialog.message=打开定义库错误

LibraryProvider.openDefinedLibrary.error.dialog.title=错误

###########################################################
#ListBandControlFigure properties
ListBandControlFigure.tooltip.GroupFooter=列表组 - 页脚

ListBandControlFigure.tooltip.GroupHeader=列表组 - 页眉

ListBandControlFigure.tooltip.ListDetail=列表 - 详细资料

ListBandControlFigure.tooltip.ListFooter=列表 - 页脚

ListBandControlFigure.tooltip.ListHeader=列表 - 页眉

###########################################################
#ListBandProvider properties
ListBandProvider.action.text=插入组(&N)

###########################################################
#ListBandProxy properties
ListBandProxy.list.detail=详细资料

ListBandProxy.list.footer=页脚

ListBandProxy.list.groupFooter=GF

ListBandProxy.list.groupHeader=GH

ListBandProxy.list.header=页眉

###########################################################
#ListContainProcess properties
ListContainProcess.action.New=新建...

ListContainProcess.action.NewRow=新建行

ListContainProcess.action.NewSubSection=新建子区域

###########################################################
#ListEditPart properties
ListEditPart.GUIDEHANDLE_TEXT = 列表

###########################################################
#ListFigure properties
ListFigure.BORDER_TEXT=列表

###########################################################
#ListHandleAdapt properties
ListHandleAdapt.transLabel.insertGroup=插入组

###########################################################
#ListPageGenerator properties
ListPageGenerator.List.Bookmark=书签

ListPageGenerator.List.Borders=边框

ListPageGenerator.List.Font=字体

ListPageGenerator.List.General=常规

ListPageGenerator.List.Section=分页符

ListPageGenerator.List.TOC=目录

ListPageGenerator.List.Visibility=可见性


###########################################################
#MapPage properties
MapPage.label.add=添加(&A)...

MapPage.label.condition=条件

MapPage.label.delete=删除(&R)

MapPage.label.displayValue=显示值:

MapPage.label.mapList=映射列表:

MapPage.transName.addMapRule=添加映射规则

MapPage.transName.deleteMapRule=删除映射规则

MapPage.transName.editMapRule=编辑映射规则

MapPage.transName.moveDownRule=下移映射规则

MapPage.transName.moveUpMapRule=上移映射规则

MapPage.toolTipText.moveUp=映射规则上移

MapPage.toolTipText.moveDown=映射规则下移

MapPage.label.duplicate=复制(&C)

MapPage.toolTipText.duplicate=重复映射规则

MapPage.transName.Duplicate=重复映射规则

###########################################################
#MapPreferencePage properties
MapPreferencePage.displayname.Condition=条件

MapPreferencePage.displayname.DisplayValue=显示值

MapPreferencePage.displayname.Title=映射

MapPreferencePage.text.Add=添加(&A)...

MapPreferencePage.text.Delete=删除(&R)

MapPreferencePage.text.Description=映射规则:

MapPreferencePage.text.Edit=编辑(&E)...

MapPreferencePage.toolTipText.Up=映射规则上移

MapPreferencePage.toolTipText.Down=映射规则下移

MapPreferencePage.text.Duplicate=重复(&D)

MapPreferencePage.toolTipText.Duplicate=重复映射规则

###########################################################
#MapRuleBuilderDialog properties
MapRuleBuilder.button.add=添加(&A)

MapRuleBuilder.button.add.tooltip=将值添加到值列表

MapRuleBuilder.button.delete=删除(&R)

MapRuleBuilder.button.delete.tooltip=从列表中删除选定值

MapRuleBuilder.button.deleteall=全部删除(&O)

MapRuleBuilder.button.deleteall.tooltip=从列表中删除所有值

MapRuleBuilder.button.edit=编辑(&E)...

MapRuleBuilder.button.edit.tooltip=编辑值列表中的选定值

MapRuleBuilder.DialogTitle.New=新建映射规则

MapRuleBuilder.DialogTitle.Edit=编辑映射规则

MapRuleBuilder.Button.ResourceKey=资源关键字(&K):

MapRuleBuilder.Button.Reset=重置(&R)

MapRuleBuilder.Button.Browse=浏览(&B)...

MapRuleBuilder.Button.Browse.Tooltip=浏览和选择按键

MapRuleBuilder.label.value=值(&V):

MapRuleBuilder.list.item1=值

MapRuleBuilder.MsgDlg.Msg=警告

MapRuleBuilder.MsgDlg.Title=值不能为空。

MapRuleBuilder.text.AND=AND

MapRuleBuilderDialog.text.Between=介于

MapRuleBuilderDialog.text.Condition=如果以下条件为真(&I):

MapRuleBuilderDialog.text.Display=则显示以下值(&T):

MapRuleBuilderDialog.text.ET=等于

MapRuleBuilderDialog.text.GE=大于或等于

MapRuleBuilderDialog.text.GT=大于

MapRuleBuilderDialog.text.LE=小于或等于

MapRuleBuilderDialog.text.Like=类似

MapRuleBuilderDialog.text.LT=小于

MapRuleBuilderDialog.text.NE=不等于

MapRuleBuilderDialog.text.NotBetween=不介于

MapRuleBuilderDialog.text.NotNull=不为 NULL

MapRuleBuilderDialog.text.Null=为 NULL

MapRuleBuilderDialog.text.Title=映射

MapRuleBuilderDialog.MsgDlg.Title=警告

MapRuleBuilderDialog.MsgDlg.Msg=值不能为空。

###########################################################
#SortkeyBuilder dialog properties
SortkeyBuilder.DialogTitle.New=新建排序关键字

SortkeyBuilder.DialogTitle.Edit=编辑排序关键字

SortkeyBuilder.DialogMessage.New=请填充下列内容以创建排序关键字。

SortkeyBuilder.DialogMessage.Edit=请编辑下列内容以更新排序关键字。

SortkeyBuilder.DialogTitle.Label.Prompt=输入用作排序依据的关键字:

SortkeyBuilder.DialogTitle.Label.Key=关键字(&K):

SortkeyBuilder.DialogTitle.Label.Direction=方向(&D):

SortkeyBuilder.Label.Locale=区域设置(&L):

SortkeyBuilder.Label.Strength=强度(&S):

SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=打开表达式生成器

SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=错误

SortkeyBuilder.Locale.Auto=自动

SortkeyBuilder.Strength.ASCII=ASCII
SortkeyBuilder.Strength.PRIMARY=第一

SortkeyBuilder.Strength.SECONDARY=第二

SortkeyBuilder.Strength.TERTIARY=第三

SortkeyBuilder.Strength.QUATERNARY=第四

SortkeyBuilder.Strength.IDENTICAL=相同

###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=新建筛选条件

FilterConditionBuilder.DialogTitle.Edit=编辑筛选条件

FilterConditionBuilder.DialogMessage.New=请填充下列内容以创建筛选条件。

FilterConditionBuilder.DialogMessage.Edit=请编辑下列内容以更新筛选条件。

FilterConditionBuilder.choice.ValueOfThisDataItem=此数据项的值

FilterConditionBuilder.tooltip.ExpBuilder=打开表达式生成器

FilterConditionBuilder.text.AND=与

FilterConditionBuilder.text.Condition=输入筛选条件:

FilterConditionBuilder.Shell.Title=筛选条件

FilterConditionBuilder.list.item1=值

FilterConditionBuilder.button.edit=编辑(&E)...

FilterConditionBuilder.button.delete=删除(&M)

FilterConditionBuilder.button.deleteall=全部删除(&O)

FilterConditionBuilder.button.add=添加(&A)

FilterConditionBuilder.button.edit.tooltip=编辑值列表中的选定值

FilterConditionBuilder.button.delete.tooltip=从列表中删除选定值

FilterConditionBuilder.button.deleteall.tooltip=从列表中删除所有值

FilterConditionBuilder.button.add.tooltip=将值添加到值列表

FilterConditionBuilder.label.value=值(&V):

FilterConditionBuilder.multipleValue.label=值(&V)

FilterConditionBuilder.multipleValue.button.add=添加(&A)

FilterConditionBuilder.multipleValue.button.remove=删除(&R)

FilterConditionBuilder.multipleValue.button.removeAll=全部删除(&M)

FilterConditionBuilder.multipleValue.button.edit=编辑(&D)...

FilterConditionBuilder.Button.UpdateAggregation=重新计算总计(&T)

###########################################################
#MarignPropertyDescriptor properties
MarignPropertyDescriptor.error.message=输入的值太大。

MarignPropertyDescriptor.error.title=值太大

###########################################################
#MasterPageAction properties
MasterColumnsPropertyDescriptor.Button.Text.Custom=自定义(&C)

MasterColumnsPropertyDescriptor.Button.Text.OneColumn=一(&O)

MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns=三(&R)

MasterColumnsPropertyDescriptor.Button.Text.TwoColumns=二(&T)

MasterColumnsPropertyDescriptor.Combo.Text.Column.Number=列数(&N):

MasterPageAction.actionText.masterPage=主页

###########################################################
#MasterPageGenerator properties
MasterPageGenerator.List.Columns=列

MasterPageGenerator.List.General=常规

MasterPageGenerator.List.Borders=边框

MasterPageGenerator.List.Header&Footer=页眉/页脚

MasterPageGenerator.List.Margins=边距

###########################################################
#MasterPageGeneralPage properties
MasterPageGeneralPage.Trans.SetType=集类型

###########################################################
#MasterPagePaletteFactory properties
MasterPagePaletteFactory.Autotext.TotalPageCount=总页数

MasterPagePaletteFactory.Autotext.Author_Page_Date=作者\#，页\#，日期\#

MasterPagePaletteFactory.Autotext.Confidential_Page=机密，页\#

MasterPagePaletteFactory.Autotext.CreatedBy=创建者

MasterPagePaletteFactory.Autotext.CreatedOn=创建时间

MasterPagePaletteFactory.Autotext.Date=日期

MasterPagePaletteFactory.Autotext.Filename=文件名

MasterPagePaletteFactory.Autotext.Label=AutoText
MasterPagePaletteFactory.Autotext.LastPrinted=上次打印

MasterPagePaletteFactory.Autotext.Page=页面

MasterPagePaletteFactory.Autotext.PageXofY=第 n 页，共 m 页

MasterPagePaletteFactory.AutotextTooltip.TotalPageCount=总页数报表项

MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate=作者#，日期# 报表项

MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage=机密报表项

MasterPagePaletteFactory.AutotextTooltip.CreatedBy=按报表项创建

MasterPagePaletteFactory.AutotextTooltip.CreatedOn=在创建报表项时创建

MasterPagePaletteFactory.AutotextTooltip.Date=日期报表项

MasterPagePaletteFactory.AutoTextTooltip.Filename=文件名报表项

MasterPagePaletteFactory.AutotextTooltip.LastPrinted=上次打印的报表项

MasterPagePaletteFactory.AutotextTooltip.Page=页报表项

MasterPagePaletteFactory.AutotextTooltip.PageXofY=“第 n 页，共 m 页”报表项

MasterPagePaletteFactory.elementLabel.authorPageDate=作者编号，页码，日期

MasterPagePaletteFactory.elementLabel.confidentialPage=机密，页

MasterPagePaletteFactory.elementLabel.createdBy=创建者

MasterPagePaletteFactory.elementLabel.createdOn=创建时间

MasterPagePaletteFactory.elementLabel.date=日期

MasterPagePaletteFactory.elementLabel.fileName=文件名

MasterPagePaletteFactory.elementLabel.lastPrinted=上次打印

MasterPagePaletteFactory.elementLabel.page=页面

MasterPagePaletteFactory.elementLabel.pageOf=第 n 页，共 m 页

MasterPagePaletteFactory.paletteDrawerName.autoText=AutoText
MasterPagePaletteFactory.ToolTip.Grid=网格报表项

MasterPagePaletteFactory.ToolTip.Text=文本报表项

MasterPagePaletteFactory.Autotext.Variable=变量

MasterPagePaletteFactory.AutotextTooltip.Variable=变量项

###########################################################
#MasterPageProcess properties
MasterPageProcess.action.Edit=编辑

MasterPageProcess.action.New=新建

MasterPageProcess.text.CannotDelete=设计仅包含一个主页，无法删除

MasterPageProcess.text.Display=显示页面设置对话框

MasterPageProcess.text.DisplayName=MasterPage

###########################################################
#MergeAction properties
MergeAction.actionMsg.merge=合并单元格(&M)

###########################################################
#ModulePage properties
ModulePage.ErrorMessage.Duplicate=该名称与现有名称重复。

ModulePage.ErrorMessage.WrongExtensionName=名称必须以 ".properties" 结尾。

ModulePage.ErrorMessage.InfoExtensionName=请输入以 ".properties" 结尾的名称。

ModulePage.text.Add=添加(&A)...

ModulePage.text.ExistingFile=现有源文件:

ModulePage.title.AddNewFile=添加新源文件

ModulePage.Resourcefile.Dialog.Title=浏览资源文件

ModulePage.Resourcefile.Dialog.Message=选择资源文件，或者选择文件夹并输入新的文件名创建资源文件。

ModulePage.text.Path=路径(&P):

###########################################################
#MultiPageEditorContributor properties
MultiPageEditorContributor.actionText.sampleAction=示例操作

MultiPageEditorContributor.actionToolTip.sampleActionToolTip=示例操作工具提示

MultiPageEditorContributor.actionToolTipText.sampleActionToolTip=示例操作工具提示

MultiPageEditorContributor.dialogMsg.sampleActionExecuted=执行的示例操作

MultiPageEditorContributor.menuText.editorMenu=编辑器菜单(&M)

###########################################################
#NewLibraryWizard properties
NewLibraryWizard.choicePageTitle.report=库

NewLibraryWizard.displayName.NewReportFileExtension=.rptlibrary
NewLibraryWizard.displayName.NewReportFileNamePrefix=新库

NewLibraryWizard.error.NoProject=工作区中没有任何打开项目。请选择“文件”->“新建”->“项目”，创建新项目或打开现有项目。

NewLibraryWizard.pageDescription.createNewReport=新建库。

NewLibraryWizard.taskName.openFileForEdit=打开文件进行编辑...

NewLibraryWizard.text.ConfirmSwitchPerspective=确认切换透视图

NewLibraryWizard.text.Container=容器 "

NewLibraryWizard.text.CreateReport=新建库。

NewLibraryWizard.text.Creating=创建

NewLibraryWizard.text.DoesNotExist=" 不存在。

NewLibraryWizard.text.OpenFileForEditing=打开文件进行编辑...

NewLibraryWizard.text.SwitchPerspective=此文件类型与 {0} 透视图相关联。是否立即切换至此透视图？

NewLibraryWizard.title.Choose=选择创建库的方式

NewLibraryWizard.title.Error=错误

NewLibraryWizard.title.New=新建库

NewLibraryWizard.title.Report=库

NewLibraryWizard.title.ReportWizard=库向导

NewLibraryWizard.title.Template=TemplateChoicePage
NewLibraryWizard.title.WizardPage=WizardPage
NewLibraryWizard.widowTitle.new=新建

NewLibraryWizard.wizardPageTitle.report=库

###########################################################
#NewReportProjectWizard properties

NewReportProjectWizard.bin=bin
NewReportProjectWizard.caseVariantExistsError=文件系统不区分大小写。存在一个与 ''{0}'' 冲突的现有项目。

NewReportProjectWizard.description=创建新报表项目。

NewReportProjectWizard.errorMessage=创建新报表项目时出现问题。

NewReportProjectWizard.errorOpeningWindow=打开某个窗口时出现问题。

NewReportProjectWizard.internalError=内部错误:{0}

NewReportProjectWizard.javaProject=创建 Java 项目

NewReportProjectWizard.perspSwitchMessage=此项目类型与 {0} 透视图相关联。是否立即切换至此透视图？

NewReportProjectWizard.perspSwitchTitle=确定切换透视图

NewReportProjectWizard.projectSetting=项目设置

NewReportProjectWizard.src=src
NewReportProjectWizard.title=报表项目

NewReportProjectWizard.windowTitle=新建报表项目

###########################################################
#NewReportWizard properties
NewReportWizard.choicePageTitle.report=报表

NewReportWizard.displayName.NewReportFileExtension=.rptdesign
NewReportWizard.displayName.NewReportFileNamePrefix=new_report
NewReportWizard.error.NoProject=工作区中没有任何打开项目。请选择“文件”->“新建”->“项目”，以创建新项目或打开现有项目。

NewReportWizard.pageDescription.createNewReport=创建新报表。

NewReportWizard.pageDescription.selectReportTemplate=选择要创建的报表类型

NewReportWizard.taskName.openFileForEdit=打开文件进行编辑...

NewReportWizard.text.ConfirmSwitchPerspective=确认切换透视图

NewReportWizard.text.Container=容器 "

NewReportWizard.text.CreateReport=创建新报表。

NewReportWizard.text.Creating=创建

NewReportWizard.text.DoesNotExist=" 不存在。

NewReportWizard.text.OpenFileForEditing=打开文件进行编辑...

NewReportWizard.text.SelectTemplate=选择要创建的报表类型。

NewReportWizard.text.SwitchPerspective=此文件类型与 {0} 透视图相关联。是否立即切换至此透视图？

NewReportWizard.title.Choose=选择如何创建报表。

NewReportWizard.title.Error=错误

NewReportWizard.title.New=新建报表

NewReportWizard.title.Report=报表

NewReportWizard.title.ReportWizard=报表向导

NewReportWizard.title.Template=TemplateChoicePage
NewReportWizard.title.WizardPage=WizardPage
NewReportWizard.widowTitle.new=新建

NewReportWizard.wizard.msgDirErr=目标目录不存在，但无法创建。


###########################################################
#NewResourceFileDialog properties
NewResourceFileDialog.label.NewFile=新文件名称(&N):

NewResourceFileDialog.ErrorMessage=文件名必须以 .properties 结尾

NewResourceFileDialog.ErrorMessageInvalid=文件名无效

NewResourceFileDialog.FileExists={0} 存在。 是否要覆盖?

###########################################################
#ProjectFileDialog properties
ProjectFileDialog.ErrorMessage=文件必须是文件，而不是目录。

ProjectFileDialog.Title=选择项目文件

ProjectFileDialog.Message=选择当前项目中的文件。

ProjectFileDialog.Text.Menu=菜单

###########################################################
#ProjectSelectionDialog properties
ProjectSelectionDialog.Message=选择要配置的项目:

ProjectSelectionDialog.Text.SpecialSettings=仅显示具有项目特定设置的项目

ProjectSelectionDialog.Title=项目特定配置

###########################################################
#ResourceFilterDialog properties
ResourceFilterDialog.Title=报表元素筛选器

ResourceFilterDialog.Lable.Description=筛选器描述:

ResourceFilterDialog.Lable.Viewer=选择要从视图中排除的元素:

###########################################################
#NewSectionDialog properties
NewSectionDialog.displayname.Container=容器

NewSectionDialog.displayname.Data=数据

NewSectionDialog.displayname.Grid=网格

NewSectionDialog.displayname.Image=图像

NewSectionDialog.displayname.Label=标签

NewSectionDialog.displayname.Line=线条

NewSectionDialog.displayname.List=列表

NewSectionDialog.displayname.Rectangle=矩形

NewSectionDialog.displayname.Table=表

NewSectionDialog.displayname.Text=文本

NewSectionDialog.text.Prefix=可用区域:

NewSectionDialog.title.insertElement=插入元素

NewSectionDialog.Title=插入元素

###########################################################
#NewTemplateWizard properties
NewTemplateWizard.displayName.NewReportFileNamePrefix=新模板

NewTemplateWizard.pageDescription.createNewTemplate=创建新的报表模板。

NewTemplateWizard.text.CreateReport=创建新模板。

NewTemplateWizard.text.Creating=创建

NewTemplateWizard.text.OpenFileForEditing=打开模板文件进行编辑...

NewTemplateWizard.title.New=新模板

NewTemplateWizard.title.Report=报表

NewTemplateWizard.title.Template=模板

NewTemplateWizard.title.WizardPage=WizardPage

###########################################################
#NoneAction properties
NoneAction.text=无

###########################################################
#OutputColumnDefnPage properties
OutputColumnDefnPage.description=输入取数脚本生成的输出列的定义:

OutputColumnDefnPage.OutputColumns.Tooltip=查看并定义数据行中的列

OutputColumnPage.error.createNewColumn=添加新列过程中发生某些错误。

OutputColumnPage.error.invalidSelection=没有选择有效的表格项。

###########################################################
#ColumnDefPage properties
ColumnDefPage.description=脚本数据集输出列定义页

ColumnDefPage.title=输出列

###########################################################
#ResultSetColumnPage properties
ResultSetColumnPage.button.add=添加(&A)...

ResultSetColumnPage.button.edit=编辑(&E)...

ResultSetColumnPage.button.delete=删除(&D)

ResultSetColumnPage.button.up=上移(&U)

ResultSetColumnPage.button.down=下移(&W)

ResultSetColumnPage.inputDialog.newColumn.title=新建脚本数据集列

ResultSetColumnPage.inputDialog.editColumn.title=编辑脚本数据集列

ResultSetColumnPage.inputDialog.label.columnName=列名称(&C):

ResultSetColumnPage.inputDialog.label.dataType=数据类型(&T):

ResultSetColumnPage.inputDialog.label.alias=列别名(&A):

ResultSetColumnPage.inputDialog.label.displayName=显示名称(&D):

ResultSetColumnPage.inputDialog.label.displayNameKey=显示名称关键字(&K):

ResultSetColumnPage.inputDialog.warning.emptyColumnName=要求输入列名称。

ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias=列名称和其别名应不同。

ResultSetColumnPage.inputDialog.error.duplicatedColumnName=列名称“{0}”已经在使用。

ResultSetColumnPage.inputDialog.error.duplicatedAlias=列别名“{0}”已经在使用。

###########################################################
#OutputColumnsPage properties
OutputColumnPage.OutputColumns.Tooltip=查看并定义数据行中的列

OutputColumnPage.toolTipText.Edit=编辑输出列

OutputColumnPage.title.OutputColumnInputDialog=输出列

OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn=新建输出列

OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn=编辑输出列

OutputColumnPage.OutputColumns.DuplicatedName=该名称已存在!

OutputColumnsPage.description=定义输出列:

OutputColumnsPage.DataTypeConvert.Note=注

OutputColumnsPage.DataTypeConvert.Message=此类转换可能导致精度损失或运行时出错，是否确定应用它?

###########################################################
#PageBreakPreferencePage properties
PageBreakPreferencePage.displayname.Title=分页符

###########################################################
#PageSetAction properties
PageSetAction.text.code=脚本(&S)

PageSetAction.Text.EditCode=编辑脚本(&S)...

PageSetAction.text.layout=布局(&L)

PageSetAction.text.master=主机(&M)

PageSetAction.text.normal=正常(&N)

PageSetAction.text.preview=预览(&P)

###########################################################
#PageSetupOutlineProcess properties
PageSetupOutlineProcess.action.Edit=编辑页面设置

PageSetupOutlineProcess.action.New=新建主页

PageSetupOutlineProcess.text.Display=显示页面设置对话框

PageSetupOutlineProcess.text.Switch=切换至页面主编辑器

###########################################################
#ParameterDialog properties
ParameterDialog.Boolean.False=假

ParameterDialog.Boolean.True=真

ParameterDialog.Button.ChangeFormat=更改(&G)...

ParameterDialog.Button.CreateDataSet=新建(&W)...

ParameterDialog.Button.ImportValue=导入值(&U)...

ParameterDialog.Button.RemoveDefault=删除默认值(&A)

ParameterDialog.Button.SetDefault=设为默认值(&A)

ParameterDialog.CheckBox.IsRequired=必需(&Q)

ParameterDialog.CheckBox.DoNotEchoInput=不回显输入内容(&O)

ParameterDialog.CheckBox.Hidden=隐藏(&H)

ParameterDialog.CheckBox.Distinct=允许重复值(&L)

ParameterDialog.Choice.NoDefault=无默认值

ParameterDialog.Choice.NullValue=空值

ParameterDialog.Choice.BlankValue=空白值

ParameterDialog.Choice.None=无

ParameterDialog.Choice.DisplayText=显示文本

ParameterDialog.Choice.ValueColumn=值列

ParameterDialog.Choice.ASCENDING=升序

ParameterDialog.Choice.DESCENDING=降序

ParameterDialog.Choice.SelectValue=<选择值...>

ParameterDialog.Column.Default=默认值

ParameterDialog.Column.DisplayText=显示文本

ParameterDialog.Column.DisplayTextKey=显示文本关键字

ParameterDialog.Column.Value=值

ParameterDialog.DefaultValue.Add=添加(&A)

ParameterDialog.DisplayLabel.Combo=组合框

ParameterDialog.DisplayLabel.List=列表框

ParameterDialog.ErrorMessage.CanootBeBlank=该值不能为空。

ParameterDialog.ErrorMessage.CanootBeNull=该值不能为 null。

ParameterDialog.ErrorMessage.DuplicatedLabel=该显示文本已经存在。

ParameterDialog.ErrorMessage.DuplicatedLabelKey=显示文本关键字已存在。

ParameterDialog.ErrorMessage.DuplicatedName=该名称已经在使用。

ParameterDialog.ErrorMessage.DuplicatedValue=值已经存在。

ParameterDialog.ErrorMessage.EmptyName=名称为空。

ParameterDialog.ErrorMessage.InvalidListLimit=限制值应介于 0 和 {0} 之间

ParameterDialog.ErrorMessage.MismatchDataType=值与数据类型不匹配。

ParameterDialog.ErrorMessage.NoAvailableColumn=没有可用的列。

ParameterDialog.ErrorMessage.ValueColumnEmpty=应指定值列。

ParameterDialog.ErrorMessage.NoDefaultValue=必须设置默认值。

ParameterDialog.ErrorTitle.InvalidListLimit=无效列表限制

ParameterDialog.ExpressionEditor.Title=编辑表达式

ParameterDialog.Flag.Default=默认

ParameterDialog.Group.MoreOption=显示为

ParameterDialog.Group.Properties=属性

ParameterDialog.Label.DataType=数据类型(&D):

ParameterDialog.SelectionDialog.Edit=编辑选择选项

ParameterDialog.Label.DefaultValue=默认值(&F):

ParameterDialog.Label.DisplayPrompt=显示提示:

ParameterDialog.Label.DisplayType=显示类型(&T):

ParameterDialog.Label.Format=格式为:

ParameterDialog.Label.HelpText=帮助文本(&X):

ParameterDialog.Label.ListOfValue=选择列表值

ParameterDialog.Label.SortGroup=排序

ParameterDialog.Label.Name=名称(&N):

ParameterDialog.Label.None=<无>

ParameterDialog.Label.Null=NULL

ParameterDialog.Label.Preview=带格式预览

ParameterDialog.Label.PromptText=提示文本(&P):

ParameterDialog.Label.SelectDataSet=数据集(&A):

ParameterDialog.Label.SortKey=排序依据(&B):

ParameterDialog.Label.SortDirection=排序方向(&C):

ParameterDialog.SelectionDialog.New=新建选择选项

ParameterDialog.SelectionDialog.Label.DisplayTextKey=显示文本关键字(&K):

ParameterDialog.SelectionDialog.Label.DisplayText=显示文本(&T):

ParameterDialog.SelectionDialog.Label.Value=值(&V):

ParameterDialog.SelectionDialog.Button.Resource=...
ParameterDialog.SelectionDialog.Button.Resource.Tooltip=浏览和选择按键

ParameterDialog.SelectionDialog.Button.Remove.Tooltip=删除按键

ParameterDialog.SelectionDialog.Label.Note=注：要设置显示文本关键字，请选择报表，然后在属性编辑器的“资源”选项卡上设置资源文件名称。

ParameterDialog.Label.SelectDisplayText=选择显示文本(&E):

ParameterDialog.Label.SelectValueColumn=选择值列(&U):

ParameterDialog.Label.Value=值:

ParameterDialog.Label.values=值

ParameterDialog.Label.Sample=示例

ParameterDialog.Label.Listlimit=列表限制(&I):

ParameterDialog.Radio.Dynamic=动态(&Y)

ParameterDialog.Radio.Static=静态(&S)

ParameterDialog.toolTipText.OpenExprButton=打开表达式生成器

ParameterDialog.datetime.prompt=请输入以下形式的日期时间值: {0}

ParameterDialog.date.prompt=请输入以下形式的日期值: {0}

ParameterDialog.time.prompt=请输入以下形式的时间值: {0}

ParameterDialog.Check.AllowMulti=允许多个值(&V)

ParameterDialog.checkBox.InputValue=输入值

ParameterDialog.checkBox.BlankValue=空白值

ParameterDialog.checkBox.NullValue=空值

ParameterDialog.emptyColumnValueExpression.title=无效的值列表达式

ParameterDialog.emptyColumnValueExpression.message=值列表达式不能为空!

ParameterDialog.Value.Empty=空值

ParameterDialog.Value.Null=空值

ParameterDialog.message=定义报表参数的属性。

###########################################################
#SelectParameterDefaultValueDialog properties
SelectParameterDefaultValueDialog.Title=选择值

###########################################################
#ParameterDropListener properties
ParameterDropListener.Command.Copy=复制元素

ParameterDropListener.Command.Move=移动元素

###########################################################
#ParameterGroupDialog properties
ParameterGroupDialog.Page.Title=常规

ParameterGroupDialog.pageTitle.general=常规

###########################################################
#ParameterGroupGeneralPage properties
ParameterGroupGeneralPage.Label.DisplayName=显示名称(&D):

ParameterGroupGeneralPage.Label.Name=名称(&N):

###########################################################
#ParameterGroupNodeProvider properties
ParameterGroupNodeProvider.Action.GroupNew=新建参数组

ParameterGroupNodeProvider.Action.ParameterEdit=编辑(&E)

ParameterGroupNodeProvider.Action.ParameterNew=新建参数(&N)

ParameterGroupNodeProvider.Dialogue.ParameterEdit=编辑参数组

ParameterGroupNodeProvider.Dialogue.ParameterNew=新建参数

ParameterGroupNodeProvider.Node.Group=参数组

###########################################################
#ParameterGroupProcess properties
ParameterGroupProcess.action.Delete=删除参数组

ParameterGroupProcess.action.Edit=编辑参数组

ParameterGroupProcess.action.New=新建参数

ParameterGroupProcess.text.DisplayName=ParameterGroup
ParameterGroupProcess.text.Show=显示数据源编辑器页

###########################################################
#ParameterNodeProvider properties
ParameterNodeProvider.dial.title.editCascading=编辑级联参数组

ParameterNodeProvider.dial.title.editScalar=编辑参数

ParameterNodeProvider.dial.title.newCascading=新建级联参数组

ParameterNodeProvider.menu.text.edit=编辑(&E)

ParameterNodeProvider.menu.text.new=新建参数

###########################################################
#ParameterProcess properties
ParameterProcess.action.Delete=删除参数

ParameterProcess.action.Edit=编辑参数

ParameterProcess.text.DisplayName=参数

ParameterProcess.text.Show=显示数据源编辑器页

###########################################################
#parameters properties
parameters.datatypes.boolean=布尔

parameters.datatypes.dateTime=DateTime
parameters.datatypes.decimal=小数

parameters.datatypes.float=浮点

parameters.datatypes.string=字符串

parameters.menuItem.reset=重置

###########################################################
#ParametersNodeProvider properties
ParametersNodeProvider.dialogue.title.group=新建参数组

ParametersNodeProvider.dialogue.title.parameter=新建参数

ParametersNodeProvider.menu.text.cascadingParameter=新建级联参数组(&D)

ParametersNodeProvider.menu.text.group=新建参数组(&M)

ParametersNodeProvider.menu.text.parameter=新建参数(&A)

###########################################################
#ParametersOutlineProcess properties
ParametersOutlineProcess.action.Edit=编辑

ParametersOutlineProcess.action.New=新建参数

ParametersOutlineProcess.action.NewGroup=新建组

ParametersOutlineProcess.text.Open=打开参数编辑器

ParametersOutlineProcess.text.Show=显示数据集的数据集编辑器页

###########################################################
#PasteAction properties
PasteAction.dlg.title=信息

PasteAction.ok=确定

PasteAction.text=粘贴(&P)

###########################################################
#PlaceHolderEditPart properties
PlaceHolderEditPart.Trans.TransferToElement=传送到报表项

###########################################################
#PreviewAction properties
PreviewAction.Text=预览

###########################################################
#PreviewEditor properties
PreviewEditor.parameter.tooltip=显示报表参数

PreviewEditor.parameter.hint=刷新报表

PreviewEditor.parameter.note=注:

PreviewEditor.parameter.info=当前数据行数的最大限制为 {0}。

PreviewEditor.parameter.change=单击以更改预览首选项

PreviewEditor.ConfirmRefresh.Title=确认刷新

PreviewEditor.ConfirmRefresh.Message=您的操作可能影响预览结果，是否要刷新预览页面?

PreviewEditor.browser.notfound=内部浏览器不可用。

###########################################################
#PreviewPageAction properties
PreviewPageAction.actionText.preview=预览

###########################################################
#PropertiesAction properties
PropertiesAction.Text=属性

###########################################################
#PropertyHandleTableViewer properties
PropertyHandleTableViewer.Button.New=新建(&N)...

PropertyHandleTableViewer.Button.Edit=编辑(&E)...

PropertyHandleTableViewer.Button.Remove=删除(&R)

PropertyHandleTableViewer.Button.Up=上移(&U)

PropertyHandleTableViewer.Button.Down=下移(&D)

PropertyHandleTableViewer.Menu.Remove=删除

PropertyHandleTableViewer.Menu.Up=上移

PropertyHandleTableViewer.Menu.Down=下移

PropertyHandleTableViewer.Menu.RemoveAll=全部删除

###########################################################
#PropertyHandleInputDialog properties
PropertyHandleInputDialog.Action.New=新建

PropertyHandleInputDialog.Action.Edit=编辑

PropertyHandleInputDialog.messages.error.blankProperty={0} 不能为空白

###########################################################
#PropertyAndPreferencePage properties
PropertyAndPreferencePage.Text.Configure.Special.Settings=配置项目特定设置...

PropertyAndPreferencePage.Text.Configure.Workspace.Settings=配置工作区设置...

PropertyAndPreferencePage.Text.Enable.SpecialSettings=启用项目特定设置(&O)

###########################################################
#PublishTemplateAction properties
PublishTemplateAction.wizard.page.browse.cheatsheet.title=选择备忘单文件

PublishTemplateAction.wizard.page.browse.title=选择图像文件

PublishTemplateAction.wizard.page.btn.browse=浏览(&W)...

PublishTemplateAction.wizard.page.cheatsheetError=备忘单文件 ''{0}'' 不存在。

PublishTemplateAction.wizard.page.desc=注册模板至模板文件夹。

PublishTemplateAction.wizard.page.imageError=图像文件 ''{0}'' 不存在。

PublishTemplateAction.wizard.page.label.cheatsheet=备忘单文件:

PublishTemplateAction.wizard.page.label.description=描述(&D):

PublishTemplateAction.wizard.page.label.displayName=显示名称(&M):

PublishTemplateAction.wizard.page.label.image=模板图像(&I):

PublishTemplateAction.wizard.page.title=发布模板

PublishTemplateAction.wizard.title=发布模板

PublishTemplateAction.wizard.errorTitle=错误

PublishTemplateAction.wizard.message=无法将模板发布到其所在的目录，请选择其它目录进行发布。

PublishTemplateAction.wizard.msgDirErr=目标目录不存在，但无法创建。

PublishTemplateAction.wizard.notvalidfolder=首选项中定义的模板文件夹无效。

PublishTemplateAction.wizard.page.nameInfo=必须提供显示名称

PublishTemplateAction.wizard.message.SourceFileNotExist=源文件不存在。

PublishTemplateAction.wizard.message.ThumbnailImageNotExist=缩略图不存在。

PublishTemplateAction.wizard.message.PreviewImageNotExist=预览图像文件不存在。

PublishTemplateAction.wizard.message.PreviewImageNotValid=预览图像文件不是有效的图像。

PublishTemplateAction.SaveBeforeGenerating.dialog.title=保存报表

PublishTemplateAction.SaveBeforeGenerating.dialog.message={0} 已被修改。 发布之前保存更改?

PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes=是(&Y)

PublishTemplateAction.SaveBeforeGenerating.dialog.button.no=否(&N)
###########################################################
#PublishLibraryAction properties
PublishLibraryAction.wizard.errorTitle=错误

PublishLibraryAction.wizard.message=无法将库发布到其所在的目录，请选择其它目录进行发布。

PublishLibraryAction.wizard.msgDirErr=目标目录不存在，但无法创建。

PublishLibraryAction.wizard.message.SourceFileNotExist=源文件不存在。

PublishLibraryAction.wizard.notvalidfolder=首选项中定义的库文件夹无效。

###########################################################
#RedoAction properties
RedoAction.Tooltip=恢复 {0}

###########################################################
#ExportReportWizard
ExportToLibraryAction.wizard.windowTitle=导出

ExportToLibraryAction.wizard.page.title=文件导出到库

ExportToLibraryAction.wizard.page.desc=报表设计导出到库

ExportToLibraryAction.wizard.page.label.filename=文件名(&N):

ExportToLibraryAction.wizard.page.label.folder=文件夹(&F):

ExportToLibraryAction.wizard.page.button.browser=浏览(&B)...

ExportToLibraryAction.wizard.page.dirdialog.message=选择文件内容目录

ExportToLibraryAction.wizard.page.nameempty=必须指定文件名。

ExportToLibraryAction.wizard.page.folderempty=必须指定文件夹。

ExportToLibraryAction.wizard.page.foldererror=指定的文件夹不存在

###########################################################
#PublishLibraryToResourceFolderAction
PublishLibraryToResourceFolderAction.Action.Text=将库复制到共享资源文件夹...

###########################################################
#PublishTemplateViewAction
DefaultNodeProvider.template.suffix=.rpttemplate
PublishTemplateViewAction.action.displaytext=使用新建报表向导来注册模板(&Z)...

###########################################################
#RefreshModuleHandleAction
RefreshModuleHandleAction.Action.Text=刷新库(&L)

RefreshModuleHandleAction.MessageBox.Title=刷新库

RefreshModuleHandleAction.MessageBox.Text=当前设计包含未保存的更改。 是否先保存这些更改?

RefreshModuleHandleAction.MessageBox.SaveButton=保存(&S)

RefreshModuleHandleAction.MessageBox.DiscardButton=放弃(&D)

RefreshModuleHandleAction.MessageBox.CancelButton=取消
###########################################################
#ReloadCssStylesAction
ReloadCssStylesAction.Action.Text=重新加载 CSS 文件(&R)

###########################################################
#ReferencePage properties
ReferencePage.Button.Edit=编辑(&D)...

ReferencePage.Label.Source=源(&C):

###########################################################
#RefreshAction properties
RefreshAction.text=刷新(&F)

###########################################################
#RefreshLibExplorerAction properties
RefreshLibExplorerAction.Text=刷新(&F)

###########################################################
#RemoveLibraryAction properties
RemoveLibraryAction.Text=删除库(&B)

RemoveLibraryAction.config.Title=确认库删除

RemoveLibraryAction.config.Message=是否确实要删除此库?

###########################################################
#UseCssInReportDesignAction properties
UseCssInReportDesignAction.Text=在报表设计中使用 CSS...

###########################################################
#UseCssInReportDesignAction properties
UseCssInThemeAction.Text=在库中使用 CSS...

###########################################################
#RenameAction properties
RenameAction.text=重命名(&M)

###########################################################
#RenameDialog properties
RenameDialog.action.Cancel=取消

RenameDialog.action.Ok=\\ \\ 确定

RenameDialog.text.NewName=新名称:

RenameDialog.text.Rename=重命名

RenameDialog.text.ToolTip=名称不能为空


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.SetNameAction.text=设置名称

ExportElementToLibraryAction.DialogTitle.setNameFailed=设置名称失败

ExportElementToLibraryAction.TransLabel.Setname=设置名称

ExportElementToLibraryAction.DialogTitle=设置名称

ExportElementToLibraryAction.DialogMessage=您可以在导出之前设置所选元素的名称，否则将使用默认名称。


###########################################################
#RenameInlineTool properties
RenameInlineTool.DialogTitle.RenameFailed=重命名失败

RenameInlineTool.TransLabel.Rename=重命名

###########################################################
#RenameInputDialog properties
RenameInputDialog.DialogTitle=重命名输入

RenameInputDialog.DialogMessage=请输入新名称

###########################################################
#report properties
report.designer.ui.preferences.buttonadd=添加(&D)...

report.designer.ui.preferences.buttonremove=删除(&R)

report.designer.ui.preferences.buttonremoveall=全部删除(&A)

report.designer.ui.preferences.dialogtitle=打开库

report.designer.ui.preferences.errormessage=库已经存在。

report.designer.ui.preferences.errortitle=错误

report.designer.ui.preferences.extensions=*.rptlibrary
report.designer.ui.preferences.labeltitle=添加/删除库

report.designer.ui.preferences.library.export.overwrite.always=始终(&W)

report.designer.ui.preferences.library.export.overwrite.never=从不(&N)

report.designer.ui.preferences.library.export.overwrite.prompt=提示(&P)

report.designer.ui.preferences.library.export.title=在将设计元素导出到库中时覆盖现有元素。

report.designer.ui.preferences.template.openDialogTitle=文件夹选择

report.designer.ui.preferences.template.select=选择(&S)...

report.designer.ui.preferences.template.templatedir=模板

report.designer.ui.preferences.template.templatefolder=模板文件夹(&T)

report.designer.ui.preferences.template.title=请选择模板文件夹。

org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment=创建报表设计时生成注释(&G)

org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment=版权所有 (c) 2008 <<此处是您公司的名称>>
#bidi_hcg start
report.designer.ui.preferences.bidiproperties.defaultdirection=默认报表方向(&O):

report.designer.ui.preferences.bidiproperties.ltrdirection=从左到右

report.designer.ui.preferences.bidiproperties.rtldirection=从右到左
#bidi_hcg end
###########################################################
#Resource Preference page properties
ResourecePreferencePage.title=键入或选择要用于库资源管理器、图像、\n属性文件等的默认资源文件夹。

ResourecePreferencePage.folder=资源文件夹(&R)

ResourecePreferencePage.select=选择(&S)...

ResourecePreferencePage.openDialogTitle=浏览文件夹

ResourecePreferencePage.openDialogMessage=请选择默认资源文件夹:

ResourecePreferencePage.defaultResourceFolder.dispaly=<当前项目文件夹>

###########################################################
#ReportAdvancedLauncherTab properties
ReportAdvancedLauncherTab.DefaultsButtonText=还原默认值(&R)

ReportAdvancedLauncherTab.DeselectButtonText=取消全选(&D)

ReportAdvancedLauncherTab.Name=报表项目

ReportAdvancedLauncherTab.SelectAllButtonText=全选(&S)

ReportAdvancedLauncherTab.UseListRadioText=从列表中选择要启动的项目

ReportAdvancedLauncherTab.VisibleLabelText=可用项目(&A)

ReportAdvancedLauncherTab.WorkspaceReportProjects=工作区报表项目

###########################################################
#ReportClasspathLaucnTab properties
ReportClasspathLaucnTab.DefaultButtonText=还原默认值(&R)

ReportClasspathLaucnTab.DeselectButtonText=取消全选(&D)

ReportClasspathLaucnTab.Name=Java 项目

ReportClasspathLaucnTab.SelectAllButtonText=全选(&S)

ReportClasspathLaucnTab.UseListRadioText=从列表中选择要启动的项目，选择项目的类输出文件夹将被添加到类路径中

ReportClasspathLaucnTab.VisibleLabelText=可用项目(&A)

ReportClasspathLaucnTab.WorkspaceJavaProjects=工作区 java 项目

###########################################################
#ReportCreationTool properties
ReportCreationTool.ModelTrans.CreateElement=插入元素

###########################################################
#ReportEditor properties
ReportEditor.Button.Close=关闭

ReportEditor.Button.Save=保存

ReportEditor.Dlg.Confirm=文件已从文件系统中删除。是要保存更改还是要关闭编辑器而不保存？

ReportEditor.Dlg.Save=保存...

ReportEditor.error.activated.outofsync.message=文件系统上的文件已更改。是否要加载更改？

ReportEditor.error.activated.outofsync.title=文件已更改

ReportEditor.Error.Open=打开错误

ReportEditor.Error.OpenEditor=无法打开文件。

ReportEditor.Page.Code=脚本

ReportEditor.Page.Layout=布局

ReportEditor.Page.MasterPage=MasterPage
ReportEditor.Page.Preview=预览

ReportEditor.Page.PreviewSaveMessage=报表文件已被修改。在预览之前是否要保存该文件？

ReportEditor.Page.PreviewSaveTitle=保存

ReportEditor.Page.PreviewSaveToggle=始终在预览前保存文件。

###########################################################
#ReportLaunchConfigurationDelegate properties
ReportLaunchConfigurationDelegate.badFeatureSetup=错误的功能设置

ReportLaunchConfigurationDelegate.noStartup=无启动

###########################################################
#ReportPage properties
ReportPage.text.Add=添加(&A)...

ReportPage.text.Browse=浏览(&B)...

ReportPage.title.Location=位置:

ReportPage.title.setPrewImg=选择图像文件

ReportPage.text.Resource.Add=浏览(&B)...

ReportPage.text.Thumbnail=缩略图(&H)...

###########################################################
#ReportPageGenerator properties
ReportPageGenerator.List.Comments=备注

ReportPageGenerator.List.Description=描述

ReportPageGenerator.List.EventHandler=事件处理程序

ReportPageGenerator.List.General=常规

ReportPageGenerator.List.Measurement=度量

ReportPageGenerator.List.NamedExpressions=命名表达式

ReportPageGenerator.List.UserProperties=用户属性

ReportPageGenerator.List.Resources=资源

ReportPageGenerator.List.Resources.JarFile=Jar 文件:

ReportPageGenerator.List.AdvancedProperty=高级

#OdaDataSourceGenerator properties
DataSourcePageGenerator.List.General=常规

#OdaDataSetGenerator properties
DataSetPageGenerator.List.General=常规

DataSetPageGenerator.DataSourceName=数据源(&D)

#ScalarParameterPageGenerator properties
ScalarParameterPageGenerator.List.General=常规

ScalarParameterPageGenerator.List.I18n=本地化

#TemplateReportItemPageGenerator properties
TemplateReportItemPageGenerator.List.General=常规

TemplateReportItemPage.description.Label.Instructions=说明(&I):

TemplateReportItemPageGenerator.List.I18n=本地化

TemplateReportItemPageGenerator.List.TextKey=文本关键字:

###########################################################
#ReportPropertySheetPage properties
ReportPropertySheetPage.Column.Title.Property=属性

ReportPropertySheetPage.Column.Title.Value=值

ReportPropertySheetPage.Root.Default.Title=信息

ReportPropertySheetPage.Tooltip.Alphabetic=按字母排序

ReportPropertySheetPage.Tooltip.Group=按组排序

ReportPropertySheetPage.Tooltip.Local=显示本地属性

ReportPropertySheetPage.Value.Inherited=已继承

###########################################################
#ReportTemplateTransferDropTargetListener properties
ReportTemplateTransferDropTargetListener.ModelTrans.CreateElement=创建元素

ReportTemplateTransferDropTargetListener.transLabel.createElement=插入元素

###########################################################
#ResourceEditDialog properties
ResourceEditDialog.message.AddNote=“快速添加”将关键字保存至当前资源文件。

ResourceEditDialog.message.ResourceFile=资源文件:

ResourceEditDialog.text.Add=添加(&A)

ResourceEditDialog.text.AddWarningMsg=不允许空白键。

ResourceEditDialog.text.AddWarningTitle=警告

ResourceEditDialog.text.Delete=删除(&D)

ResourceEditDialog.text.Key.TableColumn=关键字

ResourceEditDialog.text.Key=关键字(&K)

ResourceEditDialog.text.QuickAdd=快速添加

ResourceEditDialog.text.Value.TableColumn=值

ResourceEditDialog.text.Value=值(&V)

###########################################################
#ResourceKeyDescriptor properties
ResourceKeyDescriptor.text.Browse=浏览(&B)...

ResourceKeyDescriptor.text.Browse.Alt=浏览(&W)...

ResourceKeyDescriptor.text.Reset=重置(&R)

ResourceKeyDescriptor.text.Reset.Alt=重置(&E)

ResourceKeyDescriptor.title.SelectKey=选择关键字

ResourceKeyDescriptor.button.reset.tooltip=重置按键

ResourceKeyDescriptor.button.browse.tooltip=浏览和选择按键

ResourceKey.invalid.containSpace=显示名称关键字无效，它不应包含任何嵌入空格
###########################################################
#RevertToReportItemAction properties
RevertToReportItemAction.text=回复到报表项(&R)

###########################################################
#RevertToTemplateAction properties
RevertToTemplateAction.text=还原到模板项(&V)

###########################################################
#RowHandle properties
RowHandle.tooltip.GridRow=网格 - 行

RowHandle.tooltip.GroupFooter=表组 - 页脚

RowHandle.tooltip.GroupHeader=表组 - 页眉

RowHandle.tooltip.TableDetail=表 - 详细资料

RowHandle.tooltip.TableFooter=表 - 页脚

RowHandle.tooltip.TableHeader=表 - 页眉

###########################################################
#RowHandleAdapter properties
RowHandleAdapter.table.detail=明细数据行

RowHandleAdapter.table.footer=页脚行

RowHandleAdapter.table.groupFooter=组页脚行

RowHandleAdapter.table.groupHeader=组页眉行

RowHandleAdapter.table.header=页眉行

###########################################################
#RowPageGenerator properties
RowPageGenerator.List.Bookmark=书签

RowPageGenerator.List.Borders=边框

RowPageGenerator.List.Section=分页符

RowPageGenerator.List.CellPadding=单元格边距

RowPageGenerator.List.Font=字体

RowPageGenerator.List.General=常规

RowPageGenerator.List.Visibility=可见性

###########################################################
#RowProvider properties
RowProvider.action.text.above=在上方插入行(&A)

RowProvider.action.text.below=在下方插入行(&B)

###########################################################
#SaveAsDialog properties
SaveAsDialog.message=将文件保存至另一个位置。

SaveAsDialog.overwriteQuestion=文件“{0}”已存在。 是否要替换现有的文件？

SaveAsDialog.Question=问题

SaveAsDialog.text=另存为

SaveAsDialog.title=另存为

###########################################################
#CreateDirectory properties
DirectoryDialog.createQuestion=目录“{0}”不存在。是否要创建该目录?

DirectoryDialog.Question=问题

###########################################################
#SchematicContextMenuProvider properties
SchematicContextMenuProvider.ActionText.editData=编辑值/表达式(&V)...

SchematicContextMenuProvider.ActionText.editLabel=编辑(&E)

SchematicContextMenuProvider.label.ResetSize=重置图像大小(&Z):

SchematicContextMenuProvider.label.ResetToImageDPI=最佳图像分辨率(&I)

SchematicContextMenuProvider.label.ResetToReportDPI=最佳报表分辨率(&R)

SchematicContextMenuProvider.label.ResetToScreenDPI=最佳屏幕分辨率(&S)

SchematicContextMenuProvider.label.RestToOrginalPixels=单位为原始像素(&P)

SchematicContextMenuProvider.Menu.EditGroup=编辑组(&G)

SchematicContextMenuProvider.Menu.Insert=插入(&I)

SchematicContextMenuProvider.Menu.insertElement=插入(&N)

SchematicContextMenuProvider.Menu.insertRow=行(&R)

SchematicContextMenuProvider.Menu.List=列表

SchematicContextMenuProvider.Menu.Show=显示

SchematicContextMenuProvider.Menu.Style=样式(&Y)

SchematicContextMenuProvider.Menu.Style.Apply=应用样式(&P)

SchematicContextMenuProvider.Menu.Theme=布景主题(&M)

SchematicContextMenuProvider.Menu.Theme.Apply=应用布景主题(&P)

SchematicContextMenuProvider.Menu.Layout=布局首选项(&A)
#SchematicContextMenuProvider.menutitle.applystylerule=Apply Style
#SchematicContextMenuProvider.menutitle.applythemerule=&Apply Theme
#SchematicContextMenuProvider.menutitle.editgroup=Edit Group
#SchematicContextMenuProvider.menutitle.insert=Insert
#SchematicContextMenuProvider.menutitle.list=List
#SchematicContextMenuProvider.menutitle.show=Show
#SchematicContextMenuProvider.menutitle.style=Style
SchematicContextMenuProvider.Menu.EditStyle=编辑样式(&T)

SchematicContextMenuProvider.Menu.DeleteStyle=删除样式(&S)

SchematicContextMenuProvider.Menu.DeleteGroup=删除组(&L)

SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter=插入组页眉/页脚(&H)

SchematicContextMenuProvider.Menu.NewStyle=新样式(&Y)

###########################################################
#ApplyLayoutPreferenceAction properties
ApplyLayoutPreferenceAction.autoLayout=自动布局(&A)

ApplyLayoutPreferenceAction.fixedLayout=固定布局(&F)

###########################################################
#SectionProcess properties
SectionProcess.action.Clear=清除

SectionProcess.action.Delete=删除

SectionProcess.action.DeleteRow=删除行

SectionProcess.action.New=新建

SectionProcess.action.NewCell=新建单元格

SectionProcess.action.NewGroup=新建组

SectionProcess.action.NewRow=新建行

SectionProcess.action.NewSection=新建区域

SectionProcess.action.NewSubSection=新建子区域

SectionProcess.displayname.GridProcess=网格

SectionProcess.displayname.GroupProcess=组

SectionProcess.displayname.ListProcess=列表

SectionProcess.displayname.TableProcess=表

SectionProcess.displayname.TableRowProcess=行

SectionProcess.type.Container=（容器）

SectionProcess.type.Data=（数据）

SectionProcess.type.Image=(图像)

SectionProcess.type.Label=（标签）

SectionProcess.type.List=（列表）

SectionProcess.type.Prefix=列

SectionProcess.type.Table=(表)

SectionProcess.type.Text=(文本)

###########################################################
#SelectAllAction properties
SelectAllAction.text=全选(&A)

###########################################################
#SelectCssStyleWizard properties
SelectCssStyleWizard.wizard.title=导入 CSS 样式

SelectCssStyleWizard.wizardPage.description.report=选择一个 CSS 文件，将样式导入到报表设计。

SelectCssStyleWizard.wizardPage.description.library=选择一个 CSS 文件，将样式导入到库。

SelectCssStyleWizard.wizardPage.name=选择 Css 样式

SelectCssStyleWizard.wizardPage.title=从 CSS 文件导入样式

###########################################################
#UseCssInReportDialog properties
UseCssInReportDialog.Wizard.Title=使用 CSS

UseCssInReportDialog.Wizard.Filename=文件名(&F):

UseCssInReportDialog.Label.Styles=来自 {0} 的可用样式(&A)

UseCssInReportDialog.Label.notifications=通知(&N)

UseCssInReportDialog.TitleArea.Title=在报表设计中使用 CSS 文件

UseCssInReportDialog.TitleArea.Message=从共享库文件夹中选择一个 CSS 文件并在报表设计中使用其样式。

UseCssInReportDialog.Label.No.File=未选择 CSS 文件

UseCssInReportDialog.Dialog.Browse=浏览

UseCssInReportDialog.Dialog.Browse.Title=选择要包括在报表中的 CSS 文件

UseCssInReportDialog.Dialog.Browse.Library.Title=选择要包括在库中的 CSS 文件

UseCssInReportDialog.Error.Already.Include=已包括 {0}

UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text=查看时包括 CSS 文件(仅适用于 HTML 格式)(&I)

UseCssInReportDialog.Dialog.Label.viewTimeLb=CSS 文件的查看时间位置:

UseCssInReportDialog.Dialog.Label.example=如 http://mydomain/test.css、myfolder/test/css

UseCssInReportDialog.Dialog.Label.Theme.Text=布景主题(&T):

UseCssInReportDialog.Dialog.Text.uri=&URI:
UseCssInThemeDialog.TitleArea.Title=在报表库中使用 CSS 文件

UseCssInThemeDialog.TitleArea.Message=选择要放置 CSS 文件样式的布景主题。

CssStyleSheetNodeProvider.Label.FromServer=从服务器

CssStyleSheetNodeProvider.Tooltip.URI=(查看时应用)


###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=检索列表时出错

SelectValueDialog.messages.info.selectVauleUnavailable=值列表不可用。

SelectValueDialog.messages.info.invalidSelectVauleExpression=“选择值”函数仅接受简单列表达式。拒绝复杂表达式。

SelectValueDialog.messages.warning.BlobColumn.unsupported=无法检索引用 Blob 类型列/绑定的表达式的值列表。

SelectValueDialog.messages.error.selectVauleUnavailable=无法检索该表达式的值。

SelectValueDialog.messages.error.selectValueNotSupported=不支持此类值选择。

SelectValueDialog.noExpressionSet=尚未设置表达式

SelectValueDialog.retrieving=正在检索列表...

SelectValueDialog.selectValue=选择值

SelectValueDialog.SelectValue.NullValue=<空值>

###########################################################
#SetConstraintCommand properties
SetConstraintCommand.Error.SetConstraintError=对模型设置约束时出错。

SetConstraintCommand.transLabel.setConstraint=设置约束

SetConstraintCommand.commandLabel.ResizeCommand=重设大小
###########################################################
#ProjectFilterAction properties
ProjectFilterAction.text=筛选...

###########################################################
#ResourceFileFolderSelectionAction properties
ResourceFileFolderSelectionAction.text=筛选...

###########################################################
#ResourceFileFolderSelectionDialog properties
ResourceFileFolderSelectionDialog.Text.Menu=菜单

ResourceFileFolderSelectionDialog.button.importFile=导入文件...

ResourceFileFolderSelectionDialog.title.error=错误

ResourceFileFolderSelectionDialog.title.overwrite=覆盖

ResourceFileFolderSelectionDialog.overwrite.msg=具有此名称的文件已经存在，是否要覆盖它?

ResourceFileFolderSelectionDialog.import.msg=正在导入文件...

###########################################################
#ShowPropertyAction properties
ShowPropertyAction.text=属性(&R)

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=排序依据:

###########################################################
#Spinner properties
Spinner.DialogTitle.InvalidNumber=无效数字

###########################################################
#SplitAction properties
SplitAction.actionMsg.splitCells=拆分单元格(&S)

###########################################################
#StyleBuilder properties
StringVariableSelectionDialog.Dialog.Label=变量描述(&V):

StringVariableSelectionDialog.Dialog.Prompt=选择变量(? = 任意字符，* = 任意字符串)(&C):

StringVariableSelectionDialog.Dialog.Title=选择变量

StyleBuilder.action.Apply=应用(&A)

StyleBuilder.Edit.Info=修改所需的样式属性。使用此样式的所有报表元素将自动更新以反映新格式。

StyleBuilder.New.Info=通过从左侧选择属性类别并指定属性值创建新样式。

StyleBuilder.Title=样式

SytleBuilder.DialogTitle.New=新建样式

SytleBuilder.DialogTitle.Edit=编辑样式

###########################################################
#StyleComboProvider properties
StyleComboProvider.LineStyle.None=无样式

###########################################################
#StyleNodeProvider properties
StyleNodeProvider.action.Edit=编辑样式(&E)...

StyleNodeProvider.action.New=新建样式(&N)...

###########################################################
#StyleProcess properties
StyleProcess.action.Delete=删除样式

StyleProcess.action.Edit=编辑样式(&E)...

StyleProcess.action.New=新建样式(&N)...

StyleProcess.text.CannotDelete=无法删除默认样式。

StyleProcess.text.Show=显示该数据集的数据集编辑器。

###########################################################
#StylesNodeProvider properties
StylesNodeProvider.action.New=新建样式(&N)...

###########################################################
#StylesOutlineProcess properties
StylesOutlineProcess.action.New=新建样式(&N)...

###########################################################
#TableBandProvider properties
TableBandProvider.action.text.group=插入组(&N)

TableBandProvider.action.text.row=插入行(&S)

###########################################################
#TableEditPart properties
TableEditPart.GUIDEHANDLE_TEXT =表

TableEditPart.Label.Merge=合并单元格

TableEditPart.Label.ResizeColumn=重新设定列大小

TableEditPart.Label.ResizeRow=重新设定行大小

###########################################################
#TableFigure properties
TableFigure.BORDER_TEXT=表

TableArea.Button.New=新建(&W)...

TableArea.Button.Edit=编辑(&E)...

TableArea.Button.Remove=删除(&R)

TableArea.Button.Down=下移(&O)

TableArea.Button.RemoveAll=全部删除(&M)

###########################################################
#TableHandleAdapter properties
TableHandleAdapter.name.detail=详细资料

TableHandleAdapter.name.footer=页脚

TableHandleAdapter.name.header=页眉

TableHandleAdapter.transLabel.deleteColumn=删除列

TableHandleAdapter.transLabel.deleteColumns=删除列

TableHandleAdapter.transLabel.deleteRow=删除行

TableHandleAdapter.transLabel.deleteRows=删除行

TableHandleAdapter.transLabel.include=包括

TableHandleAdapter.transLabel.insertColumn=插入列

TableHandleAdapter.transLabel.insertGroup=插入组

TableHandleAdapter.transLabel.insertRow=插入行

TableHandleAdapter.transLabel.notInclude=排除

TableHandleAdapter.transLabel.splitCells=拆分单元格

TableHandleAdapter.transLable.deleteGroup=删除表组

###########################################################
#TableOptionDialog properties
TableOptionDialog.message.RememberGrid=对新网格使用这些维(&U)。

TableOptionDialog.message.RememberTable=对新表使用这些维(&U)。

TableOptionDialog.text.Column=列数(&C):

TableOptionDialog.text.GridRow=行数(&R):

TableOptionDialog.text.GridSize=网格大小

TableOptionDialog.text.TableDetail=明细数据数(&D):

TableOptionDialog.text.TableSize=表大小

TableOptionDialog.title.InsertGrid=插入网格

TableOptionDialog.title.InsertTable=插入表

TableOptionDialog.text.DataSet=数据集(&S):

TableOptionDialog.text.AutoSummary=自动汇总条件(&M)

TableOptionPage.Title=插入表

###########################################################
#TablePageGenerator properties
TablePageGenerator.List.Bookmark=书签

TablePageGenerator.List.Borders=边框

TablePageGenerator.List.CellPadding=单元格边距

TablePageGenerator.List.Font=字体

TablePageGenerator.List.General=常规

TablePageGenerator.List.Marign=边距

TablePageGenerator.List.Section=分页符

TablePageGenerator.List.TOC=目录

TablePageGenerator.List.Visibility=可见性

###########################################################
#TemplateReportItemPropertiesDialog properties
TemplateReportItemPropertiesDialog.errorMessage=提示文本不能为空。

TemplateReportItemPropertiesDialog.errorTitle=错误

TemplateReportItemPropertiesDialog.group=模板报表项属性

TemplateReportItemPropertiesDialog.objectType=元素类型:

TemplateReportItemPropertiesDialog.nameText=名称(&N):

TemplateReportItemPropertiesDialog.promptText=说明(&I):

TemplateReportItemPropertiesDialog.title=创建模板报表项

###########################################################
#performCreatePlaceHolder action
performCreatePlaceHolder.errorMessage.title=错误

performCreatePlaceHolder.errorMessage.content=您指定的名称已存在，请选择另一个名称或保留为空白。

###########################################################
#TogglePropertyDescriptor properties
TogglePropertyDescriptor.toolTipText.Bold=粗体

TogglePropertyDescriptor.toolTipText.Italic=斜体

TogglePropertyDescriptor.toolTipText.Underline=下划线

TogglePropertyDescriptor.toolTipText.Text_Line_Through=文本删除线

###########################################################
#TextAlignPropertyDescriptor properties
TextAlignPropertyDescriptor.0=左对齐

TextAlignPropertyDescriptor.1=居中

TextAlignPropertyDescriptor.2=右对齐

TextAlignPropertyDescriptor.3=两端对齐

###########################################################
#TextAreaContextMenu properties
TextAreaContextMenu.Copy.description=复制

TextAreaContextMenu.Copy.image=

TextAreaContextMenu.Copy.label=复制(&C)

TextAreaContextMenu.Copy.tooltip=复制

TextAreaContextMenu.Cut.description=剪切

TextAreaContextMenu.Cut.image=

TextAreaContextMenu.Cut.label=剪切(&T)

TextAreaContextMenu.Cut.tooltip=剪切

TextAreaContextMenu.Paste.description=粘贴

TextAreaContextMenu.Paste.image=

TextAreaContextMenu.Paste.label=粘贴(&P)

TextAreaContextMenu.Paste.tooltip=粘贴

TextAreaContextMenu.Redo.description=恢复

TextAreaContextMenu.Redo.image=

TextAreaContextMenu.Redo.label=恢复(&R)

TextAreaContextMenu.Redo.tooltip=恢复

TextAreaContextMenu.SelectAll.description=全选

TextAreaContextMenu.SelectAll.image=

TextAreaContextMenu.SelectAll.label=全选(&A)

TextAreaContextMenu.SelectAll.tooltip=全选

TextAreaContextMenu.Undo.description=撤消

TextAreaContextMenu.Undo.image=

TextAreaContextMenu.Undo.label=撤消(&U)

TextAreaContextMenu.Undo.tooltip=撤消

###########################################################
#TextDataEditPart properties
TextDataEditPart.Figure.Dafault=动态文本

###########################################################
#TextEditDialog properties
TextEditDialog.action.text.direction=从右到左阅读顺序(&R)

TextEditDialog.action.text.editDynamicText=编辑动态文本(&X)

TextEditDialog.action.text.formatDateTime=日期时间格式(&E)

TextEditDialog.action.text.formatNumber=数值格式(&N)

TextEditDialog.action.text.formatString=格式字符串(&G)

TextEditDialog.action.text.formatHTML=HTML 格式(&H)

TextEditDialog.choice.HTML=HTML/动态文本

TextEditDialog.choice.plainText=纯文本

TextEditDialog.choice.Auto=自动

TextEditDialog.comboText.formattingAndLayout=格式和布局

TextEditDialog.format.dynamicText=动态文本

TextEditDialog.format.formatting=格式和布局

TextEditDialog.format.lists=列表

TextEditDialog.format.pageComposition=页合成

TextEditDialog.format.pageElements=页面元素

TextEditDialog.formatChoice.content=内容

TextEditDialog.formatChoice.dynamicText=动态文本

TextEditDialog.formatChoice.formatting=格式化

TextEditDialog.formatChoice.layout=布局

TextEditDialog.formatChoice.lists=列表

TextEditDialog.toolTip.tag.a=定位

TextEditDialog.toolTip.tag.b=粗体文本样式

TextEditDialog.toolTip.tag.birtImage=BIRT 特定图像

TextEditDialog.toolTip.tag.br=换行符

TextEditDialog.toolTip.tag.center=文本居中

TextEditDialog.toolTip.tag.code=计算机代码片段

TextEditDialog.toolTip.tag.dd=定义描述

TextEditDialog.toolTip.tag.del=删除的文本

TextEditDialog.toolTip.tag.dl=定义列表

TextEditDialog.toolTip.tag.dt=定义术语

TextEditDialog.toolTip.tag.em=着重

TextEditDialog.toolTip.tag.font=字体的本地更改

TextEditDialog.toolTip.tag.genericStyleContainer=一般语言/风格容器

TextEditDialog.toolTip.tag.h1=第 1 级标题

TextEditDialog.toolTip.tag.h2=第 2 级标题

TextEditDialog.toolTip.tag.h3=第 3 级标题

TextEditDialog.toolTip.tag.h4=第 4 级标题

TextEditDialog.toolTip.tag.h5=第 5 级标题

TextEditDialog.toolTip.tag.h6=第 6 级标题

TextEditDialog.toolTip.tag.i=斜体文本样式

TextEditDialog.toolTip.tag.img=标准 HTML 图像引用

TextEditDialog.toolTip.tag.ins=插入的文本

TextEditDialog.toolTip.tag.li=列表项

TextEditDialog.toolTip.tag.p=段

TextEditDialog.toolTip.tag.pre=预设文本

TextEditDialog.toolTip.tag.strong=特别强调

TextEditDialog.toolTip.tag.sub=下标

TextEditDialog.toolTip.tag.sup=上标

TextEditDialog.toolTip.tag.tt=电传或等距文本样式

TextEditDialog.toolTip.tag.u=下划线文本样式

TextEditDialog.toolTip.tag.ul=无序列表

TextEditDialog.toolTip.valueOf=值

TextEditDialog.toolTipText.copy=复制

TextEditDialog.toolTipText.cut=剪切

TextEditDialog.toolTipText.delete=删除

TextEditDialog.toolTipText.paste=粘贴

TextEditDialog.toolTipText.redo=恢复

TextEditDialog.toolTipText.undo=撤消

TextEditDialog.toolTipText.fx=打开表达式生成器

TextEditDialog.action.item.formatHTML=HTML 格式
TextEditDialog.action.item.formatDateTime=设置日期时间格式

TextEditDialog.action.item.formatNumber=设置数字格式

TextEditDialog.action.item.formatString=字符串格式

TextEditDialog.title.DlgNew=新建文本项

TextEditDialog.title.DlgEdit=编辑文本项

###########################################################
#TextEditPart properties
TextEditPart.Figure.Dafault=文本

TextEditPart.trans.editText=编辑文本项

###########################################################
#TextExtendsTools properties
TextExtendsTools.Message.DefaultAuthor=Birt Eclipse Report Designer 版本

###########################################################
#TextPageGenerator properties
TextPageGenerator.List.Bookmark=书签

TextPageGenerator.List.Borders=边框

TextPageGenerator.List.General=常规

TextPageGenerator.List.I18n=本地化

TextPageGenerator.List.Margin=边距

TextPageGenerator.List.Padding=填充

TextPageGenerator.List.Section=分页符

TextPageGenerator.List.TOC=目录

TextPageGenerator.List.Visibility=可见性

###########################################################
#TextPropertyDescriptor properties
TextPropertyDescriptor.Message.Version=Eclipse BIRT Designer 版本 {0} 内部版本 <{1}>

###########################################################
#AutoTextPageGenerator properties
AutoTextPageGenerator.List.Bookmark=书签

AutoTextPageGenerator.List.Borders=边框

AutoTextPageGenerator.List.General=常规

AutoTextPageGenerator.List.Margin=边距

AutoTextPageGenerator.List.Padding=填充

AutoTextPageGenerator.List.Section=分页符

AutoTextPageGenerator.List.TOC=目录

AutoTextPageGenerator.List.Visibility=可见性

###########################################################
#ThemesNodeProvider properties
ThemesNodeProvider.action.New=新建布景主题(&N)...

ThemesNodeProvider.Failed=新建布景主题失败

NewThemeDialog.DialogTitle=新布景主题

NewThemeDialog.DialogMessage=请输入名称

###########################################################
#TimeOptionDialog properties
TimeOptionDialog.Label.Format=格式

TimeOptionDialog.Label.TimeZone=TimeZone

###########################################################
#ToggleMarginVisibilityAction properties
ToggleMarginVisibilityAction.text.Label=显示边距(&M)

ToggleMarginVisibilityAction.text.Tooltip=切换边距

###########################################################
#TwinExpressionDialog properties
TwinExpressionDialog.Dialog.Title=<选择值...>

TwinExpressionDialog.lable.value1=值 1:

TwinExpressionDialog.lable.value2=值 2:

###########################################################
#UndoAction properties
UndoAction.Tooltip=撤销 {0}

###########################################################
#UnitPropertyDescriptor properties
UnitPropertyDescriptor.error.message=请输入较小的值\!

UnitPropertyDescriptor.error.title=值太大

###########################################################
#UseLibraryAction properties
UseLibraryAction.Text=使用库(&U)

UseLibraryAction.Error.Title=替换现有文件

UseLibraryAction.Error.Message=BIRT 资源文件夹中已经存在文件“{0}”。是否要替换现有文件？

###########################################################
#UseCSSAction properties
UseCSSAction.Text=使用 CSS(&U)

###########################################################
#UserPropertyBuilder properties
UserPropertyBuilder.0=新用户属性

UserPropertyBuilder.ErrorMessage.DuplicatedName=您指定的名称已被使用。

UserPropertyBuilder.ErrorMessage.NoName=请指定一个名称。

UserPropertiesHandleProvier.Column.Name=名称

UserPropertiesHandleProvier.Column.Type=类型

UserPropertyBuilder.Label.DefaultValue=默认值(&V):

UserPropertyBuilder.Label.ExpressionName=表达式名称(&N):

UserPropertyBuilder.Label.PropertyName=属性名称(&N):

UserPropertyBuilder.Label.PropertyType=属性类型(&T):

UserPropertyBuilder.Title.NamedExpression=新命名表达式

###########################################################
#VisibilityPage properties
VisibilityPage.Check.HideElement=隐藏元素(&H)

VisibilityPage.Check.HTML=H&TML
VisibilityPage.Check.PDF=&PDF
VisibilityPage.Label.DetailAll=详细资料（所有输出）

VisibilityPage.Label.DetailSpecific=详细资料（特定输出）

VisibilityPage.Label.Expression=表达式:

VisibilityPage.menu.SaveHides=保存隐藏内容

VisibilityPage.Radio.AllOutputs=适用于所有输出(&A)

VisibilityPage.Radio.SpecificOutputs=对于特定输出(&C)...

###########################################################
#WidgetUtil properties
WidgetUtil.MessageBox.Error=错误

###########################################################
#wizard properties
wizard.transaction.createDataSource=定义数据源的名称和类型。

###########################################################
#WizardChoicePage properties
WizardChoicePage.radio.createBlank=在不使用模板的情况下创建新的空白报表。

WizardChoicePage.radio.createFromCustom=通过复制其他报表来创建报表。

WizardChoicePage.radio.createFromTemplate=使用模板创建报表。

###########################################################
#WizardCustomTemplatePage properties
WizardCustomTemplatePage.button.Browse=浏览

WizardCustomTemplatePage.label.FileName=输入或选择现有报表文件名。

WizardCustomTemplatePage.label.FromLocal=从本地文件系统中浏览。

WizardCustomTemplatePage.label.FromProjectDescription=选择现有报表文件。

WizardCustomTemplatePage.label.FromProjectTitle=报表文件选择

###########################################################
#WizardNewReportCreationPage properties
WizardNewReportCreationPage.Errors.nameExists=该名称已经存在。

WizardNewReportCreationPage.Errors.nameEmpty=名称不能为空。

WizardNewReportCreationPage.label.browse=浏览...

WizardNewReportCreationPage.label.directory=目录:

WizardNewReportCreationPage.label.file.location=文件位置

WizardNewReportCreationPage.label.file.name=文件名:

WizardNewReportCreationPage.label.select.directory=选择目录。

WizardNewReportCreationPage.label.useDefault=使用默认设置

WizardNewReportCreationPage.msg.duplicate.fileName=该名称已经存在。

WizardNewReportCreationPage.msg.empty.file.locationDirectory=文件位置目录不能为空。

WizardNewReportCreationPage.msg.empty.file.name=文件名不能为空。

###########################################################
#WizardSelectCssStylePage properties
WizardSelectCssStylePage.button.label.browse=浏览(&B)...

WizardSelectCssStylePage.button.label.deselectAll=取消全选(&D)

WizardSelectCssStylePage.button.label.selectAll=全选(&S)

WizardSelectCssStylePage.errorMsg.FileNotExist=文件不存在。

WizardSelectCssStylePage.info.canNotImport=\ \ (无法导入)

WizardSelectCssStylePage.label.fileName=文件名(&N):

WizardSelectCssStylePage.label.importToReportDesign=\ 导入到报表设计。

WizardSelectCssStylePage.label.notifications=通知:

WizardSelectCssStylePage.label.selectStylesFrom=选择样式自

WizardSelectCssStylePage.msg.FileNameEmpty=文件名不能为空。

WizardSelectCssStylePage.text.cannot.import.style=\ - 无法导入样式。

WizardSelectCssStylePage.FileNameError.Title=错误

WizardSelectCssStylePage.FileNameError.Message=选择的文件无效。

###########################################################
#WizardTemplateChoicePage properties
WizardTemplateChoicePage.label.Description=描述:

WizardTemplateChoicePage.label.Preview=预览:

WizardTemplateChoicePage.label.ReportTemplates=报表模板:

WizardTemplateChoicePage.label.ShowCheatSheets=显示报表创建备忘单(&S)

WizardTemplateChoicePage.label.EnableDefaultThemes=包含默认主题(&I)

WizardTemplateChoicePage.message.BlankReport=创建无预定义内容的空报表。

WizardTemplateChoicePage.message.ChartListing=创建分组报表，并附带直观表示每个组数据的图表。

WizardTemplateChoicePage.message.DualColumnChartListing=创建带两个“图表和列表”子组的分组报表。

WizardTemplateChoicePage.message.DualColumnListing=创建带两个子组的分组报表。

WizardTemplateChoicePage.message.FirstReport=提供创建客户列表报表的说明。

WizardTemplateChoicePage.message.GroupedListing=创建一个根据某些字段分组的列表报表。

WizardTemplateChoicePage.message.Letter=使用数据源中的字段创建一个模板以生成个性化字母

WizardTemplateChoicePage.message.MailingLabels=邮寄标签

WizardTemplateChoicePage.message.SideBySideChartListing=创建一个分组报表，其中每个组都并排包含一个图表和一个列表。

WizardTemplateChoicePage.message.SimpleListing=使用数据源中的字段创建列表报表。

WizardTemplateChoicePage.message.Crosstab=使用教程构建交叉表报表

WizardTemplateChoicePage.message.BlankAuto=空白报表模板，查看时会以交互方式自动启用。

WizardTemplateChoicePage.title.BlankAuto=空白自动交互式报表

WizardTemplateChoicePage.title.BlankReport=空白报表

WizardTemplateChoicePage.title.ChartListing=图表和列表

WizardTemplateChoicePage.title.DualColumnChartListing=双列图表和列表

WizardTemplateChoicePage.title.DualColumnListing=双列列表

WizardTemplateChoicePage.title.FirstReport=我的第一份报表

WizardTemplateChoicePage.title.GroupedListing=分组列表

WizardTemplateChoicePage.title.Letter=字母

WizardTemplateChoicePage.title.MailingLabels=邮寄标签

WizardTemplateChoicePage.title.SideBySideChartListing=并排图表和列表

WizardTemplateChoicePage.title.SimpleListing=简易列表

WizardTemplateChoicePage.title.Crosstab=交叉表
#bidi_hcg start
WizardTemplateChoicePage.label.rtlBiDiOrientation=从右到左

WizardTemplateChoicePage.label.ltrBiDiOrientation=从左到右

WizardTemplateChoicePage.label.chooseBiDiDirection=报表方向(&R):

WizardTemplateChoicePage.RootDisplayName=标准
#bidi_hcg end
###########################################################
#XMLSourcePage properties
XMLSourcePage.Error.Dialog.Message.InvalidFile=必须先改正 XML 源页中的错误，然后才能使用其它页。

XMLSourcePage.Error.Dialog.Message.SaveFile=XML 源页已更改。 直到保存 XML 源之后才能使用其它页。

XMLSourcePage.Error.Dialog.title=错误

XMLSourcePage.Error.Dialog.Message.PromptMsg=XML 报表设计源已更改。 是否要保存这些更改？

XMLSourcePage.Error.Dialog.Message.Yes=是

XMLSourcePage.Error.Dialog.Message.No=否

XMLSourcePage.Error.Dialog.Message.Cancel=取消

###########################################################
#SaveReportAsWizard properties
SaveReportAsWizard.SettingPage.title=设置模板属性

SaveReportAsWizard.SaveAsPageTitle=另存为

SaveReportAsWizard.SaveAsLibraryMessage=使用另一个名称将文件另存为库。

SaveReportAsWizard.SaveAsReportorTemplateMessage=使用另一个名称将文件另存为报表设计或模板。

SaveReportAsWizard.SettingPageTitle=设置报表属性

SaveReportAsWizard.SettingPage.message=如果报表另存为模板，使用这些属性。

SaveReportAsWizard.SaveAsWizardWindowTitle=另存为

SaveReportAsWizard.SaveAsWizardPageTitle=另存为

SaveReportAsWizard.SaveAsWizardPageDesc=将文件保存至另一个位置。

SaveReportAsWizard.SettingPage.Messages=请设置模板属性。

###########################################################
#WizardReportSettingPage properties
WizardReportSettingPage.Error.ReportorTemplate=文件名应该以 {0} 或 .rpttemplate 结尾。

WizardReportSettingPage.Error.Library=文件名应该以 .rptlibrary 结尾。

###########################################################
#WizardSaveAsPage properties
WizardSaveAsPage.FileLabel=文件名(&F):

WizardSaveAsPage.Question=问题

WizardSaveAsPage.FolderEmpty=\ 文件夹为空。

WizardSaveAsPage.NoProject=\ 指定的项目不存在。

WizardSaveAsPage.PathOccupied=\ 该位置已经存在一个文件: {0}

WizardSaveAsPage.EmptyName=\ ''{0}'' 名称为空。

WizardSaveAsPage.SelectFolder=\ 选择文件夹:

WizardSaveAsPage.NameExists=\ 相同名称已经存在。

WizardSaveAsPage.OverwriteQuestion=文件 ''{0}'' 已经存在。是否要替换现有文件？

WizardSaveAsPage.InvalidFileName=\ ''{0}''不是有效文件名。

WizardSaveAsPage.ContainerGroup=\ 输入或选择父文件夹(&E):

###########################################################
#Library DND message
Library.DND.messages.outofsync = 报表中使用的库可能已被更改，请关闭并重新打开此报表以使用更新的库。

NamedExpressionsHandleProvier.Column.Name=名称

NamedExpressionsHandleProvier.Column.DefaultValue=默认值

Library.DND.messages.cannotApplyTheme=布景主题无法应用。

JarFileFormProvider.Column.Name=名称

JsFileFormProvider.Column.Name=名称

ReportPageGenerator.List.Resources.JsFile=Javascript 文件:

PropertiesFileFormProvider.Column.Name=名称

ReportPageGenerator.List.Resources.PropertiesFile=属性文件:

###########################################################
#ClassFinder
ClassFinder.TaskStart=正在搜索处理程序类...

ClassFinder.DialogTitle=类选择

ClassFinder.DialogMessage=选择处理程序类(&C):

ClassFinder.ErrorMessage=未找到类

ClassPathBlock_button.addExtFolder=添加外部类文件夹(&F)...

ClassPathBlock_button.addExtJars=添加外部 JAR(&X)...

ClassPathBlock_button.down=下移

ClassPathBlock_button.edit=编辑(&E)...

ClassPathBlock_button.remove=删除(&R)

ClassPathBlock_button.up=上移

ClassPathBlock_FileDialog.edit.text=编辑 JAR

ClassPathBlock_FileDialog.jar.text=JAR 选择

ClassPathBlock_FolderDialog.edit.message=指定类文件夹的新位置:

ClassPathBlock_FolderDialog.edit.text=编辑类文件夹

ClassPathBlock_FolderDialog.message=选择类文件夹的位置:

ClassPathBlock_FolderDialog.text=外部类文件夹选择

ClassPathBlock_label.LibrariesList=报表执行路径上的 JAR 和类文件夹(&T):

###########################################################
#ListingSectionPage
ListingSectionPage.RepeatHeader=重复标题(&R)

###########################################################
#ResourceEditDialog
ResourceEditDialog.NotFile.Title=未找到文件

ResourceEditDialog.NotFile.Message=文件“{0}”不存在。

ResourceEditDialog.ReadOnlyEncounter.Title=遇到只读文件

ResourceEditDialog.ReadOnlyEncounter.Message=文件“{0}”为只读，请首先确保它可以写入。

###########################################################
#EventHandler
EventHandlerPage.Browse=浏览(&B)...


###########################################################
#IDEFileReportProvider
IDEClassPathBlock.button_addExtFolder=添加外部类文件夹(&F)...

IDEClassPathBlock.button_addEXTJar=添加外部 JAR(&X)...

IDEClassPathBlock.button_addJar=添加 JAR(&J)...

IDEClassPathBlock.button_addProject=添加项目(&P)...

IDEClassPathBlock.button_addVar=添加变量(&V)...

IDEClassPathBlock.button_down=下移

IDEClassPathBlock.button_edit=编辑(&E)...

IDEClassPathBlock.button_folder=添加类文件夹(&C)...

IDEClassPathBlock.button_remove=删除(&R)

IDEClassPathBlock.button_up=上移

IDEClassPathBlock.fLibrariesList_text=报表执行路径上的 JAR 和类文件夹(&T):

IDEClassPathBlock.ProjectDialog_message=选择要添加的项目:

IDEClassPathBlock.ProjectDialog_title=需要的项目选择

IDECPListLabelProvider.folder=(类文件夹)

IDECPListLabelProvider.missing=(缺少)

IDECPListLabelProvider.new=(新)

IDECPListLabelProvider.unknown=未知元素

IDEFileReportProvider.ReadOnlyEncounter.Title=遇到只读文件

IDEFileReportProvider.ReadOnlyEncounter.Message=文件“{0}”为只读，请首先确保它可以写入。

IDEResourcePageHelper.Dialog.Prompt=选择目录:

IDEResourcePageHelper.Dialog.Text=选择相对于工作区的位置(&C):

IDEResourcePageHelper.Dialog.Title=目录选择

IDEResourcePageHelper.File.Button=文件系统(&F)...

IDEResourcePageHelper.Varible.Button=变量(&B)...

IDEResourcePageHelper.WorkSpace.Button=工作区(&W)...

###########################################################
#DataSetColumnBindingsFormHandleProvider
DataSetColumnBindingsFormHandleProvider.Column.Name=名称

DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID=显示名称 ID

DataSetColumnBindingsFormHandleProvider.Column.DisplayName=显示名称

DataSetColumnBindingsFormHandleProvider.DatasetTitle=数据列绑定:

DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns=刷新绑定列

DataSetColumnBindingsFormHandleProvider.ReportItemTitle=可用列绑定:

DataSetColumnBindingsFormHandleProvider.NewBinding=新绑定

DataSetColumnBindingsFormHandleProvider.Column.DataType=数据类型

DataSetColumnBindingsFormHandleProvider.Column.Expression=表达式

DataSetColumnBindingsFormHandleProvider.Column.AggregateOn=聚合条件

DataSetColumnBindingsFormHandleProvider.Column.Filter=筛选器

DataSetColumnBindingsFormHandleProvider.Column.Function=函数

DataSetColumnBindingsFormHandleProvider.ALL=全部

DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns=生成绑定列

DataSetColumnBindingsFormHandleProvider.NONE=N/A
DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns=生成绑定列

DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns=刷新绑定列

###########################################################
#DataSetColumnBindingsFormPage
DataSetColumnBindingsFormPage.Button.RemoveUnused=删除未使用的列

###########################################################
#DataSetSettingsPage
DataSetSettingsPage.DataGroup.Title=用于比较数据集列值的 BIRT 引擎

DataSetSettingsPage.Locale.Label=字符串值的区域设置(&L):

DataSetSettingsPage.NullValueOrdering.Label=空值的顺序(&O):

###########################################################
#ResourceSelectionValidator
ResourceSelectionValidator.ExtError=选中的文件的结尾必须是

ResourceSelectionValidator.EmptyError=请选择文件

ResourceSelectionValidator.ExtErrorSplitter=\ 或

###########################################################
#GeneralPage
GeneralPage.Library.Included=库:

GeneralPage.Library.Modified.Note=注: 库中的此报表项已被修改。要还原原始库项目，请单击{0}“还原属性”

#BookMarkPage
BookMarkPage.Modified.Note=书签包含除这些字符 (A-Z、a-z、0-9、'-'、'_'、':'、'.') 以外的特殊字符，这可能导致在运行时出问题。

BookMarkPage.Modified.WithoutQuotes=书签没有使用双引号，这可能导致在运行时出问题。

###########################################################
# LOCALE SPECIAL:
# following string need be specially localized, e.g. as per local country custom, not just simple translation
# for example:
#       the social security number in US is like 760-01-0101, the format should be @@@-@@-@@@@
#       but the SSN in Swedish is like 760101-0101, the format should be @@@@@@-@@@@
# 
FormatStringPage.SimpleTextZipCode=941103

FormatStringPage.SimpleTextZipCode4=941123

FormatStringPage.PhoneNumber=4155551111

FormatStringPage.SocialSecurityNumber=123456789

FormatStringPage.simpleTexZipCodeFormat=@@@@@@

FormatStringPage.simpleTexZipCode4Format=@@@@@-@@@@
FormatStringPage.phoneNumberFormat=(@@@)@@@-@@@@
FormatStringPage.securityNumberFormat=@@@-@@-@@@@
FormatStringPage.Label.PreserveWhiteSpaces=保留空格

FormatStringPage.Preview.PreserveWhiteSpaces=\    具有前导空格的字符串

###########################################################
#PublishLibraryDialog
PublishLibraryDialog.ShellText=共享库

PublishLibraryDialog.TitleArea=共享库

PublishLibraryDialog.Message=将库复制到共享资源文件夹。

PublishLibraryDialog.Label.SourceFileName=源文件(&S):

PublishLibraryDialog.Label.FileName=文件名(&N):

PublishLibraryDialog.Label.Folder=文件夹(&D):

PublishLibraryDialog.Label.Browse=浏览(&B)...

PublishLibraryDialog.Message.FileNameEmpty=需要提供文件名。

PublishLibraryDialog.Message.FileNameError=文件名应该以 .rptlibrary 结尾。

PublishLibraryDialog.Message.FolderEmpty=需要提供发布文件夹。

PublishLibraryDialog.Message.SourceFileEmpty=需要提供源文件名。

PublishLibraryDialog.Label.Browse2=浏览(&W)...

PublishLibraryDialog.AddText=添加库

PublishLibraryDialog.AddMessage=将库添加到 BIRT 资源文件夹

WizardLibrarySettingPage.ErrorMessage=请选择文件夹

WizardLibrarySettingPage.Resourcefile.Dialog.Title=浏览资源文件夹

WizardLibrarySettingPage.Resourcefile.Dialog.Message=请选择资源文件夹或其子文件夹。

PublishLibraryDialog.AddLibrary=添加库


###########################################################
#PublishCSSDialog
PublishCSSDialog.ShellText=发布 CSS 文件

PublishCSSDialog.TitleArea=将 CSS 文件发布到资源文件夹

PublishCSSDialog.Message=选择一个 CSS 文件以便将样式导入到资源文件夹

PublishCSSDialog.Label.SourceFileName=源文件(&S):

PublishCSSDialog.Label.FileName=文件名(&N):

PublishCSSDialog.Label.Folder=文件夹(&F):

PublishCSSDialog.Label.Browse=浏览(&B)...

PublishCSSDialog.Label.Browse2=浏览(&W)...

PublishCSSDialog.ErrorMessage.SelectFolder=请选择文件夹

PublishCSSDialog.Dialog.FileSeclect.Tilte=选择 CSS 文件

PublishCSSDialog.Error.Exception.Title=错误

PublishCSSDialog.Error.FileNotFound=未找到文件“{0}”。

PublishCSSDialog.Error.FileIsNotCSSFile=文件“{0}”没有以“{1}”结束。

PublishCSSDialog.Error.FileExist=文件已经存在。

PublishCSSDialog.Message.SourceFileEmpty=需要提供源文件名。

PublishCSSDialog.Message.FileNameEmpty=需要提供文件名。

PublishCSSDialog.Message.FileNameError=文件名应该以 .css 结束。

PublishCSSDialog.Message.FolderEmpty=需要提供发布文件夹。

WizardCSSSettingPage.ErrorMessage=请选择文件夹

WizardCSSSettingPage.Resourcefile.Dialog.Title=浏览资源文件夹

WizardCSSSettingPage.Resourcefile.Dialog.Message=请选择资源文件夹或其子文件夹。

###########################################################
#PublishResourceDialog
PublishResourceDialog.ShellText=发布资源

PublishResourceDialog.TitleArea=将资源发布到资源文件夹

PublishResourceDialog.Message=将资源文件放在资源文件夹中

PublishResourceDialog.AddResource=添加资源

PublishResourceDialog.AddText=添加资源

PublishResourceDialog.AddMessage=将资源添加到资源文件夹

PublishResourceAction.wizard.errorTitle=错误

PublishResourceAction.wizard.message=无法将资源发布到其所在的目录，请选择其它目录进行发布。

PublishResourceAction.wizard.message.SourceFileNotExist=源文件不存在。

PublishResourceAction.wizard.notvalidfolder=首选项中定义的资源文件夹无效。

PublishResourceDialog.Label.FileName=文件名(&N):

PublishResourceDialog.Label.SourceFileName=源文件(&S):

PublishResourceDialog.Label.Folder=文件夹:

PublishResourceDialog.Label.Browse=浏览(&B)...

PublishResourceDialog.Label.Browse2=浏览(&W)...

WizardResourceSettingPage.ErrorMessage=请选择文件夹

WizardResourceSettingPage.Resourcefile.Dialog.Title=浏览资源文件夹

WizardResourceSettingPage.Resourcefile.Dialog.Message=请选择资源文件夹或其子文件夹。

PublishResourceDialog.Message.SourceFileEmpty=需要提供源文件名。

PublishResourceDialog.Message.FileNameEmpty=需要提供文件名。

PublishResourceDialog.Message.FolderEmpty=需要提供发布文件夹。

###########################################################
#EmbeddedImagesNodeProvider
EmbeddedImagesNodeProvider.FileNameError.Title=错误

EmbeddedImagesNodeProvider.FileNameError.Message=您选择的文件不是图像。

EmbeddedImagesNodeProvider.stackMsg.insert=插入图像

###########################################################
#AddElementtoAction properties
AddElementtoAction.Text=添加到报表(&A)

ApplyThemeAction.Text=应用于报表(&A)

###########################################################
#MultiPageReportEditor properties
MultiPageReportEditor.CheckVersion.Dialog.Title=警告

MultiPageReportEditor.CloseButton=关闭

MultiPageReportEditor.ConfirmTitle=文件不存在

MultiPageReportEditor.ConfirmVersion.Dialog.Message=报表设计是使用较早版本的 BIRT Designer 创建的。保存它将会升级到当前版本，可能使较早版本的 BIRT 无法打开它。是否仍要保存?

MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage=此编辑器的内容可能已从外部更改，要重新加载更改吗?

MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage=此编辑器的内容可能已从外部更改，要重新加载具有这些更改的内容吗?

MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage=此编辑器的内容可能已从外部更改，编辑器包含未保存的更改。\n\n是否要保存编辑器并重新加载外部更改?

MultiPageReportEditor.ConfirmVersion.Dialog.Title=确认

MultiPageReportEditor.SaveButton=保存(&S)

MultiPageReportEditor.DiscardButton=放弃(&D)

MultiPageReportEditor.CancelButton=取消

MultiPageReportEditor.SaveConfirmMessage=文件已被删除。是要保存更改还是要关闭编辑器而不保存?

TableArea.Button.Up=上移(&U)

###########################################################
#AddJarResourceFileFolderSelectionDialog
AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist=一个或多个选定的文件已经存在。

AddJarResourceFileFolderSelectionDialog.Message=请选择 {0} 文件:

AddJarResourceFileFolderSelectionDialog.ErrorMessage.ExtError=文件名必须以 {0} 结尾。

AddJarResourceFileFolderSelectionDialog.Message.Or=或


###########################################################
#AddImageResourceFileFolderSelectionDialog
AddImageResourceFileFolderSelectionDialog.Title=选择的一个或多个文件已存在。

AddImageResourceFileFolderSelectionDialog.Message=请选择图像:

###########################################################
#DataItemBindingDialog properties
DataItemBindingDialog.text.Name=名称:

DataItemBindingDialog.text.displayName=显示名称:

DataItemBindingDialog.text.DataType=数据类型:

DataItemBindingDialog.text.Expression=表达式:

DataItemBindingDialog.text.AggregateOn=聚合条件:

DataItemBindingDialog.text.ForceBinding=创建与同一表达式的新绑定

DataItemBindingDialog.text.All=全部

DataItemBindingDialog.text.None=N/A
DataItemBindingDialog.text.Null=无

DataItemBindingDialog.title.CreateNewItem=新建数据项绑定

DataItemBindingDialog.title.EditDataItem=编辑数据项绑定

DataItemBindingDialog.text.Function=函数:

DataItemBindingDialog.text.Filter=筛选器:

DataItemBindingDialog.text.ArgumentList=参数:

DataItemBindingDialog.Button.Add=添加

DataItemBindingDialog.Button.Edit=编辑

DataItemBindingDialog.Button.Delete=删除

###########################################################
#DataColumBindingDialog properties
DataColumBindingDialog.title.CreateNewDataBinding=新建数据绑定

DataColumBindingDialog.title.EditDataBinding=编辑数据绑定

DataColumBindingDialog.title.AggBuilder=聚合生成器

DataColumnBindingDialog.NewBindingDialogInputNewNameMessage=新数据绑定名称:

DataColumnBindingDialog.NewBindingDialogInputNewNameTitle=输入新数据绑定名称

DataColumnBindingDialog.NewBindingDialogMessage=此数据绑定由多个报表项和单元使用，是否要为此报表项创建新表达式? 单击“是”创建新的数据绑定，单击“否”更改所有位置的数据绑定。

DataColumnBindingDialog.NewBindingDialogButtonYes=是

DataColumnBindingDialog.NewBindingDialogButtonNo=否

DataColumnBindingDialog.NewBindingDialogButtonCancel=取消

DataColumnBindingDialog.NewBindingDialogTitle=创建新的数据绑定?

DataColumnBindingDialog.title.TimePeriodBuild=相对时间周期聚合生成器

DataColumBindingDialog.stackMsg.resetReference=重置引用

###########################################################
#CreatePlaceHolderAction properties
ChangeDataColumnAction.text=更改数据列(&G)...

###########################################################
#ThumbnailBuilder properties
ThumbnailBuilder.Text.Title=管理缩略图

ThumbnailBuilder.Button.Text.Generate=生成(&G)

ThumbnailBuilder.Button.Text.Remove=删除(&R)

ThumbnailBuilder.Button.Text.Import=导入(&I)...

ThumbnailBuilder.Button.Text.Browse=浏览(&B)...

ThumbnailBuilder.FileDialog.Title=选择图像文件

ThumbnailBuilder.FileDialog.FileNameError.Title=错误

ThumbnailBuilder.FileDialog.FileNameError.Message=您选择的文件不是图像。

ThumbnailBuilder.Button.GenerateFromReport=从报表生成(&N)

ThumbnailBuilder.Button.BrowseFromFileSystem=从文件系统浏览(&W)

ThumbnailBuilder.Button.ImportFromResource=从共享资源导入(&M)

ThumbnailBuilder.Image.DefaultName=缩略图

ThumbnailBuilder.PreviewImage.WarningDialog.Title=警告

ThumbnailBuilder.PreviewImage.WarningDialog.Prompt=您选择了大图像(大于 1014x768)，可能导致内存不足异常。\n是否继续?


###########################################################
#FragmentResourceEntry properties
FragmentResourceEntry.RootName=片段

FragmentResourceEntry.RootDisplayName=系统资源


FragmentTemplateResourceEntry.RootName=TemplateFragments
FragmentTemplateResourceEntry.RootDisplayName=模板资源

###########################################################
#PathResourceEntry properties
PathResourceEntry.RootName=资源文件夹

PathResourceEntry.RootDisplayName=共享资源

###########################################################
#ResourceFilter properties
ResourceFilter.DisplayName.CVS=CVS 资源

ResourceFilter.DisplayName.Dot=.* 资源

ResourceFilter.DisplayName.Data=*.数据资源

ResourceFilter.DisplayName.EmptyFolder=空文件夹

ResourceFilter.Description.CVS=隐藏其名称以 \"CVS\" 开头的资源

ResourceFilter.Description.Dot=隐藏其名称以 '.' 开头的资源

ResourceFilter.Description.Data=隐藏所有数据文件

ResourceFilter.Description.EmptyFolder=隐藏空文件夹

###########################################################
#BIRTGotoMarker properties
BIRTGotoMarker.Error.Title=错误

BIRTGotoMarker.Error.Message=报表元素不存在。

###########################################################
#TabbedList properties
TabbedPropertyList.properties.not.available=属性不可用。

###########################################################
#Sample Reports View properties
SampleReportsView.defautDescription=请从类别中选择一个示例报表

SampleReportsView.Text.Explorer=示例报表

SampleReportsView.Text.Preview=预览

SampleReportsView.Text.Description=描述

SampleReportsView.MessageDialog.Title=问题

SampleReportsView.MessageDialog.Message=“{0}”已存在。要替换它吗?

SampleReportsView.Action.exportSampleReport=导出(&E)

SampleReportsView.Action.openSampleReport=导入(&I)

SampleReportsView.Action.exportSampleReport.toolTipText=导出选择的示例报表

SampleReportsView.Action.openSampleReport.toolTipText.ide=创建报表项目并将报表放入项目文件夹

SampleReportsView.Action.openSampleReport.toolTipText.rcp=将选择的示例报表直接导出到 RCP 工作区文件夹

SampleReportsView.Text.Contribute_Samples=提供示例

UseCssStyleAction.text=使用 CSS 文件(&U)...

EditUseCssStyleAction.text=编辑 CSS 文件(&E)...

EditUseCssStyleAction.EditCssTitle=编辑 CSS

EditUseCssStyleAction.EditCssAreaTitle.Report=编辑报表设计中的 CSS 文件

EditUseCssStyleAction.EditCssAreaTitle.Libary=编辑报表库中的 CSS 文件

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=常规

CrosstabPageGenerator.List.Margin=边距

CrosstabPageGenerator.List.Font=字体

CrosstabPageGenerator.List.Borders=边框

CrosstabPageGenerator.List.Section=分页符

CrosstabPageGenerator.List.Visibility=可见性

CrosstabPageGenerator.List.TOC=目录

CrosstabPageGenerator.List.Bookmark=书签

CrosstabPageGenerator.List.NamedExpressions=命名表达式

CrosstabPageGenerator.List.UserProperties=用户属性

CrosstabPageGenerator.List.AdvancedProperty=高级

CrosstabPageGenerator.List.SubTotals=小计

CrosstabPageGenerator.List.GrandTotals=总计

CrosstabPageGenerator.List.PageBreak=分页符

###########################################################
#CubePageGenerator properties
CubePageGenerator.List.General=常规

CubePageGenerator.List.Comments=备注

CubePageGenerator.List.AdvancedProperty=高级


###########################################################
#LibraryLayoutEditorFormPage
LibraryLayoutEditorFormPage.warning.title=库

LibraryLayoutEditorFormPage.warning.message=要向库中添加报表项，从选项板中拖动项目或使用插入菜单。\n\n要在编辑器中查看报表项，请单击大纲视图中的报表项。

LibraryLayoutEditorFormPage.warning.prompt=不再显示此消息(&D)

###########################################################
#PageGenerator Item properties
PageGenerator.TabItem.Attributes=属性

PageGenerator.TabItem.Binding=绑定

PageGenerator.TabItem.Filters=筛选器

PageGenerator.TabItem.Groups=组

PageGenerator.TabItem.Highlights=突出显示

PageGenerator.TabItem.map=映射

PageGenerator.TabItem.Sorting=排序

###########################################################
#AggDataDropAdapter
AggDataDropAdapter.Trans.Name=创建聚合

###########################################################
#BindingDialogHelper
BindingDialogHelper.text.Name=列绑定名称(&B):

BindingDialogHelper.text.DataType=数据类型(&T):

BindingDialogHelper.text.Function=函数(&F):

BindingDialogHelper.text.DataField=数据字段(&F):

BindingDialogHelper.text.Filter=筛选条件(&C):

BindingDialogHelper.text.AggOn=聚合条件(&G):

BindingDialogHelper.text.Table=表(&L)

BindingDialogHelper.text.List=列表(&L)

BindingDialogHelper.text.Grid=网格

BindingDialogHelper.text.Group=组(&O)

BindingDialogHelper.text.Expression=表达式(&E):

BindingDialogHelper.text.displayName=显示名称(&N):

BindingDialogHelper.text.allowExport=导出(&O)：

BindingDialogHelper.text.allowExport.button=允许导出

BindingDialogHelper.text.displayNameID=显示名称 ID(&S):

BindingDialogHelper.bindingName.dataitem=列绑定

BindingDialogHelper.bindingName.aggregation=聚合

BindingDialogHelper.error.nameduplicate=名为 {0} 的列绑定已存在。

BindingDialogHelper.error.improperexpression=多维数据集度量聚合不能为此类型:“{0}”。

BindingDialogHelper.error.empty={0} 是必需的。

BindingDialogHelper.error.text.Name=列绑定名称

BindingDialogHelper.text.Colon=:
BindingDialogHelper.warning.function=当前函数的建议数据类型为“{0}”，是否要保留您选择的数据类型?

BindingDialogHelper.warning.parameter=参数“{1}”不支持表达式“{0}”的返回数据类型，是否要保留?

BindingDialogHelper.warning.button.yes=是

BindingDialogHelper.warning.button.no=否

BindingGroupDescriptorProvider.Flag.DataModel=\ (数据模型)

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel=数据模型绑定

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet=数据设置绑定

###########################################################
#Scripts Node
Scripts.Node.DisplayName=脚本

ScriptElementNode.Action.Text=转到报表元素(&G)

ScriptObjectNode_0=重置脚本(&R)

ReportPage.PreviewImage.Button.ToolTip=管理缩略图

###########################################################
#CrosstabBindingDialogHelper
CrosstabBindingDialogHelper.AggOn.All=全部

CrosstabBindingDialogHelper.bindngName.timeperiod=周期

CrosstabBindingDialogHelper.calculation.label=时间周期(&P):

CrosstabBindingDialogHelper.calculation.group=时间周期

CrosstabBindingDialogHelper.recentday.description=期限中的最后日期

CrosstabBindingDialogHelper.referencedate.label=参考日期(&R):

CrosstabBindingDialogHelper.thisdate.label=此日期:

CrosstabBindingDialogHelper.thisdate.example.label=请用引号引起提供的日期值。\n例如："2012-01-23"、"1/23/2012"。

CrosstabBindingDialogHelper.thisdate.example.label.constant=例如：2012-01-23、1/23/2012。

CrosstabBindingDialogHelper.timedimension.label=时间维(&M):

CrosstabBindingDialogHelper.timeperiod.wrongdate=此维不支持最近参考日期

CrosstabBindingDialogHelper.today.label=今天

AddEditCascadingParameterDialog.label.name=名称(&N):

AddEditCascadingParameterDialog.label.dataset=数据集(&D):

AddEditCascadingParameterDialog.label.value=值(&V):

AddEditCascadingParameterDialog.label.displaytext=显示文本(&T):

###########################################################
#FormatDateTimePattern
FormatDateTimePattern.datetiem_format_type_year=年

FormatDateTimePattern.datetiem_format_type_short_year=长格式年

FormatDateTimePattern.datetiem_format_type_long_month_year=长格式年月

FormatDateTimePattern.datetiem_format_type_shot_month_year=短格式年月

FormatDateTimePattern.datetiem_format_type_month=月

FormatDateTimePattern.datetiem_format_type_long_day_of_week=长格式星期

FormatDateTimePattern.datetiem_format_type_day_of_month=月中第几天

FormatDateTimePattern.datetiem_format_type_medium_day_of_year=中等格式年中第几天

FormatDateTimePattern.datetiem_format_type_minutes=分钟

FormatDateTimePattern.datetiem_format_type_secontds=秒

FormatDateTimePattern.datetiem_format_type_general_time=常规时间

###############################################################################
# LOCALE SPECIAL:
# following text should be localized specifically for each locale, 
# the pattern may be different for each locale. 
# For example, in Polish locale, "general time" pattern should be "HH:mm:ss.SSS" 
# instead of "ahh:mm:ss.SSS".
#
FormatDateTimePattern.pattern.datetiem_format_type_year=yyyy

FormatDateTimePattern.pattern.datetiem_format_type_short_year=yy

FormatDateTimePattern.pattern.datetiem_format_type_long_month_year=MMMM yyyy

FormatDateTimePattern.pattern.datetiem_format_type_shot_month_year=MMM yy

FormatDateTimePattern.pattern.datetiem_format_type_month=MMMM

FormatDateTimePattern.pattern.datetiem_format_type_long_day_of_week=EEEE

FormatDateTimePattern.pattern.datetiem_format_type_day_of_month=dd

FormatDateTimePattern.pattern.datetiem_format_type_medium_day_of_year=MMMM dd, yy

FormatDateTimePattern.pattern.datetiem_format_type_minutes=mm

FormatDateTimePattern.pattern.datetiem_format_type_secontds=ss

FormatDateTimePattern.pattern.datetiem_format_type_general_time=ahh:mm:ss.SSS

FormatDescriptorProvider.DisplayName.None=自动

ReportExamples.ContributeSamples.description=请在{0}将错误提交到 BIRT{1}提交 bugzilla 条目。\n附加报表设计文件和报表输出的屏幕快照。\n\n 注意: 屏幕快照应该为:\n  1. *.png 图像格式；\n  2. 分辨率 800*600；\n  3. 不大于 250 KB。

ReportExamples.ContributeSamples.title=提供示例

ReportExamples.ContributeSamples.messages=以下信息将引导您提供示例。

ReportExamples.OpenBrowser.Error.title=错误

ReportExamples.OpenBrowser.Error.message=无法打开 Web 浏览器。

###########################################################
#CopyCellContentsContextAction
CopyCellContentsContextAction.actionText=复制内容(&E)

###########################################################
#CopyCellContentsAction
CopyCellContentsAction.actionText=复制内容(&E)

###########################################################
#CopyFormatAction
CopyFormatAction.text=复制格式(&F)

###########################################################
#PasteFormatAction
PasteFormatAction.text=粘贴格式(&O)

###########################################################
#InsertAggregationAction
InsertAggregationAction.text=聚合(&A)


###########################################################
#InsertRelativeTimePeriodAction
InsertRelativeTimePeriodAction.text=相对时间期限(&V)

###########################################################
#Multiple views
MultipleAdapter.SetCurrentView=设置当前视图

MultipleAdapter.RemoveView=删除视图

MultipleGuideHandle.RemoveView=删除

CreateChartAction.text=创建图表视图(&W)

CreateChartHandler.text=创建图表视图

###########################################################
#Input Parameters
InputParameterDialog.msg.title=输入参数

InputParameterDialog.msg.requiredParam=带有 * 的参数为必需参数。

InputParameterDialog.err.requiredParam=未指定所需参数 [{0}] 的值。

InputParameterDialog.err.invalidValueTitle=无效值

InputParameterDialog.err.invalidValue=具有类型 {1} 的值“{0}”无效。

InputParameterDialog.err.emptyValue={0}的 输入值不能为空。

InputParameterDialog.err.emptyValue2={0} 的第二个输入值不能为空。

###########################################################
#Crosstab drag tracker
ColumnDragTracker.ResizeColumn=重新设定列大小

ColumnDragTracker.Show.Label=宽度:

CPListLabelProvider.misssing=(缺少)

RowDragTracker.ResizeRow=重新设定行大小

RowDragTracker.Show.Label=高度:

TableDragGuideTracker.Pixels=像素

###########################################################
#General Designer settings
GeneralConfigurationBlock.message.general.description=报表设计器的常规设置

GeneralConfigurationBlock.button.text.enable.gradient=用渐变选择(&G)

GeneralConfigurationBlock.button.text.enable.animation=启用动画选择(&N)

GeneralConfigurationBlock.group.label=提示

GeneralConfigurationBlock.group.defaultThemes=默认主题库

GeneralConfigurationBlock.button.text.prompt=打开库文件后显示提示(&P)。

GeneralConfigurationBlock.button.text.defaultTheme.enable=启用默认主题库(&T)

GeneralConfigurationBlock.button.text.defaultTheme.include=包含默认主题(&I)

GeneralConfigurationBlock.move.binding.group=将本地绑定移动到父层以获得更好的性能

GeneralConfigurationBlock.move.binding.always=始终(&W)

GeneralConfigurationBlock.move.binding.never=从不(&V)

GeneralConfigurationBlock.move.binding.prompt=提示(&R)

###########################################################
#ResetImageSizeAction
ResetImageOriginalSizeAction.trans.label=重置图像大小

ResetImageSizeAction.label=置为原始大小(&Z)
###########################################################
#Report document editor
ReportDocumentEditor.1=报表文档编辑器

ReportDocumentEditor.2=文档信息

ReportDocumentEditor.3=名称:

ReportDocumentEditor.4=版本:

ReportDocumentEditor.5=页数:

ReportDocumentEditor.errorMessage=在编辑器中打开文档时出错。

###########################################################
#Export Element Dialog
ExportElementDialog.Dialog.Title=将元素导出到库

ExportElementDialog.Dialog.Message=选择目标库或创建新目标。

ExportElementDialog.label.NewFile=新文件名称(&N):

ExportElementDialog.ErrorMessage=文件名必须以 .rptlibrary 结尾。

ExportElementDialog.ErrorMessageInvalid=文件名无效。

ExportElementDialog.ErrorMessageDuplicate=元素在库中已存在。

ExportElementDialog.ErrorStatusCanNotExport=元素无法导出。

ExportElementDialog.WarningMessageDuplicate.Message=重复的组件名称。是否替代现有的库组件?\n\n单击 {0} 替换现有的库组件。\n单击 {1} 返回导出元素对话框。\n单击 {2} 取消操作。

ExportElementDialog.WarningMessageDuplicate.Title=确认替换

ExportElementDialog.WarningMessageDuplicate.OverrideImage=由导出的图像引用的嵌入图像已存在。是否要替代它?

###########################################################
#NewStyleHandler
NewStyleHandler.transaction.label=新建样式...

AdvancePropertyDescriptorProvider.DisplayName=促进

AdvancePropertyDescriptorProvider.Tooltip.Alphabetic=按字母排序

AdvancePropertyDescriptorProvider.Tooltip.Group=按组排序

AdvancePropertyDescriptorProvider.Tooltip.Local=显示本地属性

PropertyEditorFactory.Boolean.True=真

PropertyEditorFactory.Boolean.False=假

PropertyEditorFactory.Value.Auto=自动

ExportElementToLibraryAction.waring=警告

ExportElementToLibraryAction.waringMsg=元素没有名称，但不能为其设置名称。

###########################################################
#ExportStyleDialog
ExportStyleDialog.Label.Text=布景主题(&T):

ExportStyleDialog.Message=在库中选择要导出的布景主题。

ExportStyleDialog.Title=选择布景主题

###########################################################
#UIUtil
UIUtil.previewconfirm.title=报表中存在错误

UIUtil.previewconfirm.message=报表包含错误，是否继续预览?

###########################################################
#FileReportProvider
FileReportProvider.CopyConfigFile.DeleteFailure=删除文件失败: {0}

###########################################################
#ShowPropertiesViewMenu
ShowPropertiesViewMenu.ActionName=属性(&I)

ShowPropertiesViewMenu.ErrorMessage=无法打开属性视图

ShowPropertiesViewMenu.ErrorTitle=错误

###########################################################
#ResetAttributePage
ResetAttributePage.Style.Restore.TooltipText=将当前页的样式属性还原到默认设置

ResetAttributePage.Style.Restore.Transaction.Name=还原样式
ResetAttributePage.Style.Restore.Menu.Name=恢复默认样式
ResetAttributePage.Style.Restore.All.Transaction.Name=恢复全部样式
ResetAttributePage.Style.Restore.All.Menu.Name=在所有选项卡上恢复默认样式

###########################################################
#TextEditor
TextEditor.PasteFormattedText=粘贴带格式文本

###########################################################
#ReloadImageAction
ReloadImageAction.Text=重新加载图像(&M)

###########################################################
#VariablesNodeProvider
VariablesNodeProvider.NewActionName=新建变量(&N)

VariablesNodeProvider.Dialog.Title=新建变量

VariablesNodeProvider.NodeName=变量

###########################################################
#VariableNodeProvider
VariableNodeProvider.DialogTitle=编辑变量

###########################################################
#SelectVariableDialog
SelectVariableDialog.AvailableVariables=可用变量(&A):

SelectVariableDialog.PageSeperator=----- 页面变量 -----

SelectVariableDialog.ReportSeperator=----- 报表变量 -----

SelectVariableDialog.Title=插入变量

###########################################################
#VariableDialog
VariableDialog.DataType=数据类型(&T):

VariableDialog.DefaultValue=默认值(&V):

VariableDialog.Name=名称(&N):

VariableDialog.PageVariable=页面变量(&P)

VariableDialog.ReportVariable=报表变量(&R)

VariableDialog.VariableType=类型:

VariableDialog.Error.NameDuplicate=名称“{0}”与现有名称重复。请选择一个不同的名称。

###########################################################
#ExpressionBuilderConfigurationBlock
ExpressionBuilderConfigurationBlock.DefaultSyntax=默认语法(&F):

###########################################################
#ExpressionEditor
ExpressionEditor.Error.EmptyExpression=表达式不能为空。

ExpressionEditor.Label.Expression=表达式(&E):

ExpressionEditor.Title=编辑表达式

ExpressionSyntaxColoringPage.Accessible.Name.Background=背景色选择器

ExpressionSyntaxColoringPage.Accessible.Name.Foreground=前景色选择器

ExpressionSyntaxColoringPage.Button.Bold=粗体(&B)

ExpressionSyntaxColoringPage.Button.Enable=启用

ExpressionSyntaxColoringPage.Button.Italic=斜体(&I)

ExpressionSyntaxColoringPage.Button.Restore=还原默认设置(&R)

ExpressionSyntaxColoringPage.Button.Strikethrough=删除线(&S)

ExpressionSyntaxColoringPage.Button.Underline=下划线(&U)

ExpressionSyntaxColoringPage.Decscription=在列表或示例文本区域，通过选择您想要自定义其样式的文本类型来自定义语法突出显示。

ExpressionSyntaxColoringPage.Element.Comment=注释

ExpressionSyntaxColoringPage.Element.Default=默认值

ExpressionSyntaxColoringPage.Element.Keyword=关键字

ExpressionSyntaxColoringPage.Element.String=字符串

ExpressionSyntaxColoringPage.Label.Background=背景(&O):

ExpressionSyntaxColoringPage.Label.Foreground=前景(&F):

ExpressionSyntaxColoringPage.Label.SampleText=示例文本(&S):

ExpressionSyntaxColoringPage.Label.SyntaxElement=语法元素(&Y):

ExpressionSyntaxColoringPage.Link.ColorAndFont=默认颜色和字体可在 <a href="org.eclipse.ui.preferencePages.GeneralTextEditor">文本编辑器</a> 以及 <a href="org.eclipse.ui.preferencePages.ColorsAndFonts">颜色和字体</a> 首选项页面上配置。

###########################################################
#VariableDropAdapter
VariableDropAdapter.TranasctionName=使用变量创建数据项

###########################################################
#RenameInputDialog
RenameInputDialog.Message.BlankName=名称不能为空白。

RenameInputDialog.Message.DuplicateName=存在重复名称。

###########################################################
#Breadcrumb
BreadcrumbItemDropDown.showDropDownMenu.action.toolTip=显示子项

ToggleBreadcrumbAction.tooltip.switch.breadcrumb=切换 Breadcrumb

###########################################################
#HyperlinkParameterProvider Category
ReportHyperlinkParameterProvider.Category.Report=报表参数

RenderHyperlinkParameterProvider.Category.Render=呈现选项
ExceptionDialog.title=错误
