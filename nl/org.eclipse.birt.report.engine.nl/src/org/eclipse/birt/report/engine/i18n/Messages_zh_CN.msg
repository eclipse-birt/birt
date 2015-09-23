# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: {1} 中有 {0}。

###########################################################
Error.OutputFormatNotSupported=不支持输出格式 {0}。

Error.NullOutputFormat=呈现选项的 OutputFormat 为空。

Error.InvalidParameter=某些所需的参数值未设置或设为不兼容的数据类型。

Error.InvalidGroupParameter=报表引擎无法评估名为“{0}”的参数组。

Error.ParameterTypeIsInvalid=参数“{0}”的类型应为“{1}”，而不是“{2}”。

Error.ParamValidationFailure=参数“{0}”的验证失败。

Error.ParameterValueIsNull=未设置必需参数 {0}。

Error.ParameterValueIsBlank=必需参数 {0} 为空白。

Error.ParamScriptValidationError=参数“{0}”脚本源验证错误。

Error.ParameterGroupIsNotFoundByGroupname=根据名称“{0}”未找到参数组。

Error.ParameterInvalidGroupLevel=组“{0}”的级别计数与指定的不匹配。

Error.InvalidDesignFile=设计文件 {0} 有错误且不能运行。

Error.DesignFileNotFound=无法找到设计文件 {0}。

Error.CannotCreateExtensionInstance=报表引擎无法初始化 {0} 发射器，请确保安装发射器必需的库。

Error.MissingComputedColumnExpression=缺少计算列 {0} 的表达式。

Error.ParameterInGroupIsnotScalar=参数组“{0}”中的参数必须为标量参数。

Error.ParameterEvaluationError=参数组“{0}”的评估失败。

Error.InvalidEmitterID = 呈现选项的 EmitterID {0} 无效。

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

Error.JarNotFoundError = 未找到 Jar {0}。

Error.ScriptClassCastError = 类 {0} 不实现 {1} 接口。

Error.ScriptClassNotFoundError = 未找到类 {0}。

Error.ScriptClassIllegalAccessError = 尝试加载类 {0} 时出现 IllegalAccessException。

Error.ScriptClassInstantiationError = 无法实例化类 {0}。 确保它不是接口或抽象类。

Error.UnhandledScriptError = 执行脚本时出现未处理的异常。

###########################################################
Error.PageNotFoundError = 未找到页 {0}。

Error.PageNumberRangeError = 无效页范围: {0}。

Error.PageHintLoadingError = 无法加载页提示。

Error.PagesLoadingError = 无法加载页 {0}。

Error.InvalidGroupError = 无效组: {0}。

Error.InvalidExtensionError = 无效的扩展: {0} 和无效的格式: {1}。

Error.InvalidColumnIndexError = 无效列索引。

Error.InvalidInstanceIDError = 无效实例 ID: {0}。

Error.InvalidBookmarkError = 无效书签: {0}。

Error.ExpressionEvaluationError = 以下可见性表达式不能对合法布尔值求值: {0}。

Error.BookmarkNotFoundError = 无法找到书签: {0}。

Error.DateError = 日期: {0}。

Error.CubePositionError = 获得多维数据集位置错误。

Error.SubqueryCreateError = 子查询只能在另一个子查询/查询中创建。

Error.BindDataRetrivingError = 检索绑定数据错误。

Error.DataSourceError = 数据集的数据源不能为空。

Error.UnsupportedQueryDefinitionError = 不支持的查询定义 {0}。

Error.ReportQueryLoadingError = 报表查询加载出错，无法检索数据以生成报表。

Error.ReportQueryLoadingError2 = 无法加载报表查询: {0}。 为 ID 为 {1} 的报表元素生成报表文档时出错。

Error.ExtentionIDMissingError = 数据源定义 {0} 中缺少扩展 ID。

Error.DataExportionError = 未能按实例 ID 导出数据: {0}。

Error.UnsupportedDocumentVersionError = 不支持的文档版本: {0}。

Error.ResultsetIteratorError = 结果集迭代函数必须移动到有效行。

Error.ReportDesignNotFoundError = 报表文档 {0} 中无法找到报表设计。

Error.PreparedQueryNotFoundError = 无法找到准备的查询 {0}。

Error.IncorrectParentResultSetError = 子查询的错误父结果集: {0}。

Error.UnsupportedQueryTypeError = 不支持的查询类型 {0}。

Error.QueryNotBuiltError = 无法为元素生成查询: {0}。

Error.RenderTaskNotFinishedError = 呈现任务未完成。

Error.RenderOptionError = 必须指定呈现选项才能呈现报表。

Error.ReportArchiveError = 运行报表时未指定报表存档。

Error.ReportDocNameNotSpecifiedError = 运行报表时未指定报表文档名称。

Error.ReportRunError = 运行报表时出错。

Error.ReportDocmentOpenError = 打开报表文档失败。

Error.ReportArchiveOpenError = 无法打开报表存档。

Error.SkipError = 跨过时出错。

Error.ResultsetExtractError = 找不到结果集。

Error.FailedToInitializeEmitter = 初始化发射器失败。

###########################################################
PDFCreator = 使用 iText {1} 的 BIRT 报表引擎 {0}。

###########################################################
Error.FlashObjectNotSupported = 此报表格式不支持 Flash 对象报表项。

Error.ReportItemNotSupported = 此报表格式不支持当前报表项。

Error.ResourceUnreachable = 此报表项的资源不可访问。

Error.UpdateUserAgent = 启用 Flash 支持或下载较新的 Adobe Reader 版本以查看 PDF 文件中的 Flash 内容。

###########################################################
Error.ReportRunnableNotSet = 未设置报表可运行。

Error.FailedToLoadTOCTree = 无法加载目录树。

Error.ExceptionOnFetchingBookmarks = 获取书签时出现异常。

Error.FailedToSaveDesign = 无法保存设计。

Error.ExceptionOnCopyingArchives = 复制存档时出现异常。

Error.UnsupportedCoreStreamVersion = 不支持的核心流版本: {0}。

Error.UnsupportedEngineExtension = 不支持的引擎扩展 {0}。

Error.FailToCreateDataEngine = 无法创建数据引擎。

Error.FailedToCreateTOC = 无法创建目录。

Error.UnsupportedExprssionType = 不支持的表达式类型。

Error.UnknownContentVersion = 未知内容版本。

Error.UnknownFieldID = 未知字段 id: {0}。

Error.ResourceNotAccessible = {0} 不可访问。

Error.ERROR_WITH_STATUS=文档中存在错误: {0}。

###########################################################
Error.ElementID = 元素 ID

###########################################################
Message.TaskCancel = 任务已取消。