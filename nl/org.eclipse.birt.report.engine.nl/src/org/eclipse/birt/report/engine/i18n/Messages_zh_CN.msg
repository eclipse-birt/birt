# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=不支持输出格式 {0}。
Error.InvalidParameter=某些所需的参数值未设置或设为不兼容的数据类型。
Error.InvalidGroupParameter=报表引擎无法评估名为“{0}”的参数组。
Error.ParameterTypeIsInvalid=参数“{0}”的类型应为“{1}”，而不是“{2}”。
Error.ParameterValueIsNull=未设置必需参数 {0}。
Error.ParameterValueIsBlank=必需参数 {0} 为空白。
Error.ParamScriptValidationError=参数“{0}”脚本源验证错误。
Error.ParameterGroupIsNotFoundByGroupname=根据名称“{0}”未找到参数组。
Error.ParameterInvalidGroupLevel=The level count of group "{0}" does not match the specified one.
Error.InvalidDesignFile=设计文件 {0} 有错误且不能运行。
Error.DesignFileNotFound=无法找到设计文件 {0}。
Error.CannotCreateExtensionInstance=报表引擎无法初始化 {0} 发射器，请确保安装发射器必需的库。
Error.MissingComputedColumnExpression=缺少计算列 {0} 的表达式。
Error.ParameterInGroupIsnotScalar=参数组“{0}”中的参数必须为标量参数。
Error.ParameterEvaluationError=参数组“{0}”的评估失败。

###########################################################
Error.ErrorsOnPage=生成报表时出错: 
Error.Error=错误
Error.ScriptFileLoadError=无法加载脚本文件 {0}。
Error.ScriptEvaluationError=判定脚本“{0}”时出错:\n{1}。
Error.TextualItemProcessingError=处理文本项 {0} 时出错。
Error.TableItemProcessingError=处理表 {0} 时出错。
Error.ExtendedItemGenerationError=生成扩展项 {0} 时出错。
Error.ExtendedItemRenderingError=呈现 {0} 扩展项 {1} 时出错。
Error.GridItemProcessingError=处理网格 {0} 时出错。
Error.ImageItemProcessingError=处理图像 {0} 时出错。
Error.MissingImageFileError=缺少图像文件。
Error.InvalidImageSourceError=无效图像源类型。 
Error.InvalidDatabaseImageError=无效数据库图像。图像表达式“{0}”。 
Error.EmbeddedExpressionError=判定嵌入表达式“{0}”时出错。 
Error.EmbeddedImageError=处理嵌入图像时出错。 
Error.HTMLImageError=处理 HTML <img> 标记中的嵌入图像时出错。 

Error.UndefinedDatasetError=报表中未定义数据集 "{0}"。 
Error.InvalidExpressionError=表达式 "{0}" 无效。 
Error.ListProcessingError=处理列表项 {0} 时出现问题。
Error.ErrorOnReportPage=下列项有错误:

Error.ReportErrorMessage = {0} {1}:
Error.ReportErrorMessageWithID = {0} (id = {1}):
Error.ReportErrorID = {0} ( {1} 次 )
Error.ReportErrorDetail = 详细资料: 

Error.ScriptClassCastError = Class {0} does not implement the {1} interface.
Error.ScriptClassNotFoundError = Class {0} not found.
Error.ScriptClassIllegalAccessError = IllegalAccessException when trying to load class {0}.
Error.ScriptClassInstantiationError = 无法实例化类 {0}。 确保它不是接口或抽象类。
Error.UnhandledScriptError = Unhandled exception when executing script.

###########################################################
Error.PageNotFoundError = Page {0} is not found.
Error.PageNumberRangeError = Invalid page range: {0}.
Error.PageHintLoadingError = Can''t load the page hint.
Error.PagesLoadingError = Can''t load the pages {0}.

Error.InvalidGroupError = Invalid group : {0}.
Error.InvalidExtensionError = Invalid extension id and format.
Error.InvalidColumnIndexError = Invalid column index.
Error.InvalidInstanceIDError = Invalid instance id : {0}.
Error.InvalidBookmarkError = Invalid bookmark : {0}.

Error.ExpressionEvaluationError = The following visibility expression does not evaluate to a legal boolean value: {0}.
Error.BookmarkNotFoundError = Can not find bookmark :{0}.
Error.DateError = date:{0}.
Error.CubePositionError = Get cube position error.
Error.SubqueryCreateError = Sub-query can only be created in another sub-query/query.
Error.BindDataRetrivingError = Retrieving binding data error.
Error.DataSourceError = Data source of data set can not be null.
Error.UnsupportedQueryDefinitionError = Unsupported query definition {0}.
Error.ReportQueryLoadingError = Can''t load the report query: {0}.
Error.ExtentionIDMissingError = Missing extension id in data source definition {0}.
Error.DataExportionError = Failed to export data by instance id :{0}.
Error.UnsupportedDocumentVersionError = Unsupported document version: {0}.
Error.ResultsetIteratorError = Result set iterator must be moved to valid row.
Error.ReportDesignNotFoundError = Can not find the report design in the report document {0}.
Error.PreparedQueryNotFoundError = Can''t find the prepared query {0}.
Error.IncorrectParentResultSetError = Incorrect parent resultSet for subQuery: {0}.
Error.UnsupportedQueryTypeError = Unsupported query type {0}.
Error.RenderTaskNotFinishedError = Render task is not finished.
Error.RenderOptionError = Render options have to be specified to render a report.
Error.ReportArchiveError = Report archive is not specified when running a report.
Error.ReportDocNameNotSpecifiedError = Report document name is not specified when running a report.
Error.ReportRunError = Error happened while running the report.
Error.ReportDocmentOpenError = Failed to open the report document.
Error.ReportArchiveOpenError = Can not open the report archive.
Error.SkipError = Error happened when skipping.
