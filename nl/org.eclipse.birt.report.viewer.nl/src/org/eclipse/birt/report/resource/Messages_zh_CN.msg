###############################################################################
# Copyright (c) 2004 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation  - initial API and implementation
###############################################################################

birt.viewer.title=BIRT Report Viewer
birt.viewer.title.navigation=导航　
birt.viewer.title.error=错误

birt.viewer.parameter=参数
birt.viewer.runreport=运行报表
birt.viewer.required=需要带有 <FONT COLOR="red">*</FONT> 标记的参数。

birt.viewer.viewinpdf=以 PDF 查看
birt.viewer.maximize=隐藏报表参数
birt.viewer.restore=显示报表参数

birt.viewer.error=错误消息
birt.viewer.error.noparameter=此报表无参数。

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=打印报表
birt.viewer.toolbar.toc=显示目录
birt.viewer.toolbar.parameter=运行报表
birt.viewer.toolbar.export=导出数据
birt.viewer.toolbar.font=更改字体
birt.viewer.toolbar.enableiv=调用交互式查看器
birt.viewer.toolbar.pdf=以 PDF 格式打印报表

###############################################################################
# navbar
###############################################################################
birt.viewer.navbar.first=第一页
birt.viewer.navbar.previous=上一页
birt.viewer.navbar.next=下一页
birt.viewer.navbar.last=最后一页
birt.viewer.navbar.goto=转到
birt.viewer.navbar.lable.goto=转到页:
birt.viewer.navbar.prompt.one=显示页
birt.viewer.navbar.prompt.two=/
birt.viewer.navbar.error.blankpagenum=请输入有效页码。

###############################################################################
# progress bar
###############################################################################
birt.viewer.progressbar.prompt=正在处理，请稍候 ...

###############################################################################
# Dialog 
###############################################################################
birt.viewer.dialog.ok=确定
birt.viewer.dialog.cancel=取消
birt.viewer.dialog.run=运行报表
birt.viewer.dialog.close=关闭

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=例外
birt.viewer.dialog.exportReport.title=导出报表
birt.viewer.dialog.parameter.title=参数
birt.viewer.dialog.simpleExportData.title=导出数据

###############################################################################
# Export report dialog 
###############################################################################
birt.viewer.dialog.export.format=导出格式:
birt.viewer.dialog.export.format.pdf=PDF
birt.viewer.dialog.export.format.excel=EXCEL
birt.viewer.dialog.export.all=导出详细报表
birt.viewer.dialog.export.all.detail=将导出所有可用报表数据，不考虑<br>当前视图的修改。
birt.viewer.dialog.export.modified=导出具有当前修改的报表
birt.viewer.dialog.export.modified.detail=将只导出可见数据。将不导出<BR>已被筛选掉或隐藏的数据。

###############################################################################
# Export data dialog 
###############################################################################
birt.viewer.dialog.exportdata.tab.field=数据字段
birt.viewer.dialog.exportdata.tab.filter=筛选标准
birt.viewer.dialog.exportdata.resultsets=可用结果集
birt.viewer.dialog.exportdata.availablecolumn=可用列
birt.viewer.dialog.exportdata.selectedcolumn=选择的列
birt.viewer.dialog.exportdata.format=数据将以 csv 格式导出。
birt.viewer.dialog.exportdata.encoding=输出编码:
birt.viewer.dialog.exportdata.encoding.other=其它
birt.viewer.dialog.exportdata.encoding.comment=(如果为空白，使用本地编码)
birt.viewer.dialog.exportdata.addall=全部添加
birt.viewer.dialog.exportdata.add=添加
birt.viewer.dialog.exportdata.removeall=全部删除
birt.viewer.dialog.exportdata.remove=删除

###############################################################################
# General exception
###############################################################################
birt.viewer.generalException.DOCUMENT_FILE_ERROR=文档文件: {0} 不存在或包含错误。
birt.viewer.generalException.DOCUMENT_ACCESS_ERROR=无法访问文档文件: {0}。
birt.viewer.generalException.REPORT_FILE_ERROR=报表文件: {0} 不存在或包含错误。
birt.viewer.generalException.REPORT_ACCESS_ERROR=无法访问报表文件: {0}。
birt.viewer.generalException.DOCUMENT_FILE_PROCESSING=正在处理文档文件，请稍后再试。

###############################################################################
# Report service exception
###############################################################################
birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT=没有可供提取数据的文档文件。
birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET=没有可供提取数据的结果集。
birt.viewer.reportServiceException.INVALID_TOC=无效 TOC 查询。
birt.viewer.reportServiceException.INVALID_PARAMETER=无效参数: {0}，因此它未定义。

###############################################################################
# Birt action handler exception
###############################################################################
birt.viewer.actionException.NO_REPORT_DOCUMENT=报表文档实例为空。
birt.viewer.actionException.INVALID_BOOKMARK=书签: {0} 无效。请检查并重置链接。
birt.viewer.actionException.INVALID_PAGE_NUMBER=无效页码。 
birt.viewer.actionException.INVALID_ID_FORMAT=无效 id 格式。Id={0}。
birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST=会话超时或没有可用的文档文件。

###############################################################################
# Birt soap bingding impl exception
###############################################################################
birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET=无法为此目标找到处理程序。目标: {0}。

###############################################################################
# Component processor exception
###############################################################################
birt.viewer.componentProcessorException.MISSING_OPERATOR=错误: 缺少运算符!!!

###############################################################################
# Exception dialog
###############################################################################
birt.viewer.exceptionDialog.stackTrace=堆栈跟踪:
birt.viewer.exceptionDialog.showStackTrace=Show Exception Stack Trace
birt.viewer.exceptionDialog.hideStackTrace=Hide Exception Stack Trace

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=必须指定属性 ID。
birt.viewer.taglib.INVALID_VIEWER_ID=属性 ID 包含无效字符。
birt.viewer.taglib.VIEWER_ID_DUPLICATE=属性 ID 必须唯一。
birt.viewer.taglib.NO_REPORT_SOURCE=应指定报表设计或文档文件。
birt.viewer.taglib.NO_REPORT_DOCUMENT=reportlet 需要报表文档文件。
birt.viewer.taglib.NO_REQUESTER_NAME=必须指定请求者名称。
