# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=Das Ausgabeformat {0} wird nicht unterstützt.
Error.InvalidParameter=Einige der erforderlichen Parameterwerte sind nicht angegeben bzw. die angegebenen Werte entsprechen einem inkompatiblen Datentyp.
Error.InvalidGroupParameter=Auswertung der Parametergruppe mit dem Namen "{0}" durch Berichts-Engine fehlgeschlagen.
Error.ParameterTypeIsInvalid=Der Parametertyp "{0}" wird als "{1}" erwartet, nicht als "{2}".
Error.ParameterValueIsNull=Der erforderliche Parameter {0} wurde nicht festgelegt.
Error.ParameterValueIsBlank=Erforderlicher Parameter {0} ist leer.
Error.ParamScriptValidationError=Validierungsfehler bei Skriptquelle zu Parameter "{0}".
Error.ParameterGroupIsNotFoundByGroupname=Parametergruppe wurde durch Namen "{0}" nicht gefunden.
Error.ParameterInvalidGroupLevel=The level count of group "{0}" does not match the specified one.
Error.InvalidDesignFile=Die Designdatei {0} ist fehlerhaft und kann nicht ausgeführt werden.
Error.DesignFileNotFound=Die Designdatei {0} kann nicht gefunden werden.
Error.CannotCreateExtensionInstance=Berichts-Engine kann {0}-Emitter nicht initialisieren. Vergewissern Sie sich, dass die erforderlichen Bibliotheken für diesen Emitter installiert sind.
Error.MissingComputedColumnExpression=Die Definition für die berechnete Spalte {0} fehlt.
Error.ParameterInGroupIsnotScalar=Die Parameter in der Parametergruppe "{0}" müssen Skalargrößen-Parameter sein.
Error.ParameterEvaluationError=Die Auswertung der Parametergruppe "{0}" ist fehlgeschlagen.

###########################################################
Error.ErrorsOnPage=Beim Erstellen des Berichts sind Fehler aufgetreten:
Error.Error=Fehler
Error.ScriptFileLoadError=Scriptdatei {0} kann nicht geladen werden.
Error.ScriptEvaluationError=Fehler beim Auswerten des Skripts "{0}":\n{1}.
Error.TextualItemProcessingError=Bei der Verarbeitung des Textelements {0} sind Probleme aufgetreten.
Error.TableItemProcessingError=Beim Verarbeiten der Tabelle {0} sind Probleme aufgetreten.
Error.ExtendedItemGenerationError=Beim Erstellen des erweiterten Elements {0} sind Probleme aufgetreten.
Error.ExtendedItemRenderingError=Beim Rendern {0} des erweiterten Elements {1} sind Probleme aufgetreten.
Error.GridItemProcessingError=Beim Verarbeiten des Rasters {0} sind Probleme aufgetreten.
Error.ImageItemProcessingError=Beim Verarbeiten des Bildes {0} sind Probleme aufgetreten.
Error.MissingImageFileError=Bilddatei fehlt.
Error.InvalidImageSourceError=Ungültiger Datenquellentyp für Bild. 
Error.InvalidDatabaseImageError=Ungültiges Datenbank-Image. Ausdruck für Image lautet "{0}". 
Error.EmbeddedExpressionError=Beim Evaluieren des eingebetteten Ausdrucks "{0}" ist ein Fehler aufgetreten. 
Error.EmbeddedImageError=Beim Verarbeiten des eingebetteten Bildes ist ein Fehler aufgetreten. 
Error.HTMLImageError=Beim Verarbeiten des in den HTML-Tag '<img>' eingebetteten Bildes ist ein Fehler aufgetreten. 

Error.UndefinedDatasetError=Datensatz "{0}" ist im Bericht nicht definiert. 
Error.InvalidExpressionError=Ausdruck "{0}" ist ungültig. 
Error.ListProcessingError=Bei der Verarbeitung des Listenelements {0} sind Probleme aufgetreten.
Error.ErrorOnReportPage=Folgende Objekte weisen Fehler auf:

Error.ReportErrorMessage = {0} {1}:
Error.ReportErrorMessageWithID = {0} (id = {1}):
Error.ReportErrorID = {0} ({1} Mal)
Error.ReportErrorDetail = Detail: 

Error.ScriptClassCastError = Class {0} does not implement the {1} interface.
Error.ScriptClassNotFoundError = Class {0} not found.
Error.ScriptClassIllegalAccessError = IllegalAccessException when trying to load class {0}.
Error.ScriptClassInstantiationError = Klasse {0} konnte nicht instanziiert werden. Stellen Sie sicher, dass es sich nicht um eine Schnittstelle oder eine abstrakte Klasse handelt.
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
