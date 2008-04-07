# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=No se admite el formato de salida {0}.
Error.InvalidParameter=Algunos parámetros de valores necesarios no están establecidos o se han establecido con un tipo de datos incompatible.
Error.InvalidGroupParameter=El motor de informes no puede evaluar el grupo de parámetros llamado "{0}".
Error.ParameterTypeIsInvalid=El tipo de parámetro "{0}" que se espera es "{1}", no "{2}".
Error.ParameterValueIsNull=El parámetro requerido {0} no está establecido.
Error.ParameterValueIsBlank=El parámetro requerido {0} está en blanco.
Error.ParamScriptValidationError=Error de validación de la fuente de script del parámetro "{0}".
Error.ParameterGroupIsNotFoundByGroupname=El grupo de parámetros no se encuentra con el nombre "{0}".
Error.ParameterInvalidGroupLevel=The level count of group "{0}" does not match the specified one.
Error.InvalidDesignFile=El archivo de diseño {0} contiene errores y no se puede ejecutar.
Error.DesignFileNotFound=No se puede encontrar el archivo de diseño {0}.
Error.CannotCreateExtensionInstance=El motor de informes no puede inicializar el emisor {0}; asegúrese de que estén instaladas las librerías necesarias para este emisor.
Error.MissingComputedColumnExpression=Falta la expresión de la columna procesada {0}
Error.ParameterInGroupIsnotScalar=Los parámetros del grupo de parámetros "{0}" deben ser de escala.
Error.ParameterEvaluationError=Error en la evaluación del grupo de parámetros "{0}".

###########################################################
Error.ErrorsOnPage=Hay errores al generar informe:
Error.Error=Error
Error.ScriptFileLoadError=No es posible cargar el archivo de script {0}.
Error.ScriptEvaluationError=Hay errores al evaluar el script "{0}":\n{1}.
Error.TextualItemProcessingError=Hay problemas al procesar el elemento de texto {0}.
Error.TableItemProcessingError=Hay problemas al procesar la tabla {0}.
Error.ExtendedItemGenerationError=Hay problemas al generar el elemento ampliado {0}.
Error.ExtendedItemRenderingError=Hay problemas al procesar el elemento ampliado {1} de {0}.
Error.GridItemProcessingError=Hay problemas al procesar la cuadrícula {0}.
Error.ImageItemProcessingError=Hay problemas al procesar la imagen {0}.
Error.MissingImageFileError=Falta el archivo de imagen.
Error.InvalidImageSourceError=Tipo de fuente de imagen no válido. 
Error.InvalidDatabaseImageError=Imagen de base de datos no válida. La expresión de la imagen es "{0}". 
Error.EmbeddedExpressionError=Hay errores al evaluar la expresión incrustada "{0}". 
Error.EmbeddedImageError=Hay errores al procesar la imagen incrustada. 
Error.HTMLImageError=Hay errores al procesar la imagen incrustada en la etiqueta HTML <img>. 

Error.UndefinedDatasetError=El conjunto de datos "{0}" no está definido en el informe. 
Error.InvalidExpressionError=La expresión "{0}" no es válida. 
Error.ListProcessingError=Hay problemas al procesar el elemento de lista {0}.
Error.ErrorOnReportPage=Los siguientes elementos tienen errores:

Error.ReportErrorMessage = {0} {1}:
Error.ReportErrorMessageWithID = {0} (id = {1}):
Error.ReportErrorID = {0} ({1} veces)
Error.ReportErrorDetail = detalle: 

Error.ScriptClassCastError = Class {0} does not implement the {1} interface.
Error.ScriptClassNotFoundError = Class {0} not found.
Error.ScriptClassIllegalAccessError = IllegalAccessException when trying to load class {0}.
Error.ScriptClassInstantiationError = No se ha podido crear una instancia de la clase {0}. Asegúrese de que no es una interfaz ni una clase abstracta.
Error.UnhandledScriptError = Unhandled exception when executing script.

###########################################################
Error.PageNotFoundError = Page {0} is not found.
Error.PageNumberRangeError = Invalid page range: {0}.
Error.PageHintLoadingError = Can't load the page hint.
Error.PagesLoadingError = Can't load the pages {0}.

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
Error.ReportQueryLoadingError = Can't load the report query: {0}.
Error.ExtentionIDMissingError = Missing extension id in data source definition {0}.
Error.DataExportionError = Failed to export data by instance id :{0}.
Error.UnsupportedDocumentVersionError = Unsupported document version: {0}.
Error.ResultsetIteratorError = Result set iterator must be moved to valid row.
Error.ReportDesignNotFoundError = Can not find the report design in the report document {0}.
Error.PreparedQueryNotFoundError = Can't find the prepared query {0}.
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
