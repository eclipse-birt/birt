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
birt.viewer.title.complete=Complete

birt.viewer.parameter=参数
birt.viewer.runreport=运行报表
birt.viewer.required=需要带有 <FONT COLOR="red">*</FONT> 标记的参数。

birt.viewer.viewinpdf=以 PDF 查看
birt.viewer.maximize=隐藏报表参数
birt.viewer.restore=显示报表参数

birt.viewer.error=错误消息
birt.viewer.error.noparameter=此报表无参数。
birt.viewer.error.noprinter=Cannot find any avaiable printer that supports postscript format.

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=以 PDF 格式打印报表
birt.viewer.toolbar.printserver=Print report on the server
birt.viewer.toolbar.toc=显示目录
birt.viewer.toolbar.parameter=运行报表
birt.viewer.toolbar.export=导出数据
birt.viewer.toolbar.font=更改字体
birt.viewer.toolbar.enableiv=调用交互式查看器
birt.viewer.toolbar.exportreport=Export report

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

birt.viewer.dialog.page.all=All pages
birt.viewer.dialog.page.current=Current page
birt.viewer.dialog.page.range=Pages:
birt.viewer.dialog.page.range.description=( Enter page number(s) and/or page range(s). For example: 1-6 or 1,3,6 )

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=例外
birt.viewer.dialog.exportReport.title=导出报表
birt.viewer.dialog.parameter.title=参数
birt.viewer.dialog.simpleExportData.title=导出数据
birt.viewer.dialog.printReport.title=Print report on the server

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

birt.viewer.dialog.export.pdf.fittoactual=Actual size
birt.viewer.dialog.export.pdf.fittowidth=Fit to page width
birt.viewer.dialog.export.pdf.fittowhole=Fit to whole page

###############################################################################
# Print report dialog 
###############################################################################
birt.viewer.dialog.print.onserver=Print the output document file on the server
birt.viewer.dialog.print.printer=Printer:
birt.viewer.dialog.print.status=Status:
birt.viewer.dialog.print.model=Model:
birt.viewer.dialog.print.description=Description:
accepting-jobs=Accepting Jobs

birt.viewer.dialog.print.settings=Please specify print settings
birt.viewer.dialog.print.settings.copies=Number of copies:
birt.viewer.dialog.print.settings.collate=Collate:
birt.viewer.dialog.print.settings.mode=Mode:
birt.viewer.dialog.print.settings.mode.bw=B&W
birt.viewer.dialog.print.settings.mode.color=Color
birt.viewer.dialog.print.settings.duplex=Duplex:
birt.viewer.dialog.print.settings.duplex.simplex=Simplex
birt.viewer.dialog.print.settings.duplex.horizontal=Horizontal
birt.viewer.dialog.print.settings.duplex.vertical=Vertical
birt.viewer.dialog.print.settings.pagesize=Page size:

birt.viewer.dialog.print.complete=Sent document file to the selected printer on the server.

###############################################################################
# Confirmation dialog 
###############################################################################
birt.viewer.dialog.confirmation.title=Confirmation
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
birt.viewer.dialog.exportdata.separator=Separator:

birt.viewer.sep.comma=Comma ( , )
birt.viewer.sep.semicolon=Semicolon ( ; )
birt.viewer.sep.colon=Colon ( : )
birt.viewer.sep.verticalline=Vertical Line ( | )
birt.viewer.sep.tab=Tab ( \\t )

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
birt.viewer.reportServiceException.STARTUP_REPORTENGINE_ERROR=尝试启动报表引擎时出现致命错误。

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
birt.viewer.exceptionDialog.showStackTrace=显示例外堆栈跟踪
birt.viewer.exceptionDialog.hideStackTrace=隐藏例外堆栈跟踪

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=必须指定属性 ID。
birt.viewer.taglib.INVALID_VIEWER_ID=属性 ID 包含无效字符。
birt.viewer.taglib.VIEWER_ID_DUPLICATE=属性 ID 必须唯一。
birt.viewer.taglib.NO_REPORT_SOURCE=应指定报表设计或文档文件。
birt.viewer.taglib.NO_REPORT_DOCUMENT=reportlet 需要报表文档文件。
birt.viewer.taglib.NO_REQUESTER_NAME=必须指定请求者名称。
