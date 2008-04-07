# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=En: There is a {0} in the {1}.

###########################################################
Error.OutputFormatNotSupported=Le format de sortie {0}n''est pas pris en charge.
Error.InvalidParameter=Certaines valeurs de paramètres requises ne sont pas définies ou sont définies sur un type de données incompatible.
Error.InvalidGroupParameter=Le moteur d'états n'a pas pu évaluer le groupe de paramètres nommé "{0}".
Error.ParameterTypeIsInvalid=Le type de paramètre "{0}" est attendu sous la forme "{1}" et non "{2}".
Error.ParameterValueIsNull=Le paramètre requis {0} n'est pas défini.
Error.ParameterValueIsBlank=Le paramètre requis {0} est vierge.
Error.ParamScriptValidationError=Erreur de validation de la source du script du paramètre "{0}".
Error.ParameterGroupIsNotFoundByGroupname=Le groupe de paramètres n'a pas été trouvé sous le nom "{0}".
Error.ParameterInvalidGroupLevel=The level count of group "{0}" does not match the specified one.
Error.InvalidDesignFile=Le fichier modèle {0} comporte une erreur et ne peut pas être exécuté.
Error.DesignFileNotFound=Le fichier modèle {0} est introuvable.
Error.CannotCreateExtensionInstance=L''initialisation de l''émetteur {0} par le moteur d''états a échoué, assurez-vous que les bibliothèques nécessaires à cet émetteur sont installées.
Error.MissingComputedColumnExpression=L''expression de la colonne de calcul {0} est manquante.
Error.ParameterInGroupIsnotScalar=Les paramètres du groupe de paramètres "{0}" doivent être scalaires.
Error.ParameterEvaluationError=Echec de l'évaluation du groupe de paramètres "{0}".

###########################################################
Error.ErrorsOnPage=Des erreurs sont survenues lors de la création de l'état :
Error.Error=Erreur
Error.ScriptFileLoadError=Impossible de charger le fichier de script {0}.
Error.ScriptEvaluationError=Des erreurs sont survenues lors de l'évaluation du script "{0}":\n{1}.
Error.TextualItemProcessingError=Problème(s) de traitement de l''élément de texte {0}.
Error.TableItemProcessingError=Problème(s) de traitement de la table {0}.
Error.ExtendedItemGenerationError=Problème(s) de création de l''élément étendu {0}.
Error.ExtendedItemRenderingError=Problème(s) de rendu {0} de l''élément étendu {1}.
Error.GridItemProcessingError=Problème(s) de traitement de la grille {0}.
Error.ImageItemProcessingError=Problème(s) de traitement de l''image {0}.
Error.MissingImageFileError=Fichier image manquant.
Error.InvalidImageSourceError=Type de source d'image incorrect. 
Error.InvalidDatabaseImageError=Image de base de données incorrecte. L''expression de l''image est « {0} ». 
Error.EmbeddedExpressionError=Des erreurs sont survenues lors de l''évaluation de l''expression incorporée « {0} ». 
Error.EmbeddedImageError=Des erreurs sont survenues lors du traitement de l'image incorporée. 
Error.HTMLImageError=Des erreurs sont survenues lors du traitement de l'image incorporée dans la balise HTML <img>. 

Error.UndefinedDatasetError=Le jeu de données "{0}" n''est pas défini dans l''état. 
Error.InvalidExpressionError=L''expression "{0}" est incorrecte. 
Error.ListProcessingError=Problème(s) de traitement de l''élément de liste {0}.
Error.ErrorOnReportPage=Les éléments suivants comportent des erreurs :

Error.ReportErrorMessage = {0} {1}:
Error.ReportErrorMessageWithID = {0} (id = {1}):
Error.ReportErrorID = {0} ( {1} fois )
Error.ReportErrorDetail = détails : 

Error.ScriptClassCastError = Class {0} does not implement the {1} interface.
Error.ScriptClassNotFoundError = Class {0} not found.
Error.ScriptClassIllegalAccessError = IllegalAccessException when trying to load class {0}.
Error.ScriptClassInstantiationError = La classe {0} n''a pas pu être instanciée. Vérifiez qu''il ne s''agit pas d''une interface ou d''une classe abstraite.
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
