# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.

###########################################################
#for test only
Error.Msg001=Fr : Il y a un(e) {0} dans {1}.

###########################################################
Error.OutputFormatNotSupported=Le format de sortie {0}n''est pas pris en charge.

Error.NullOutputFormat=OutputFormat pour l'option de rendu est nul.

Error.InvalidParameter=Certaines valeurs de paramètres requises ne sont pas définies ou sont définies sur un type de données incompatible.

Error.InvalidGroupParameter=Le moteur d'états n'a pas pu évaluer le groupe de paramètres nommé "{0}".

Error.ParameterTypeIsInvalid=Le type de paramètre "{0}" est attendu sous la forme "{1}" et non "{2}".

Error.ParamValidationFailure=Echec de la validation du paramètre "{0}".

Error.ParameterValueIsNull=Le paramètre requis {0} n'est pas défini.

Error.ParameterValueIsBlank=Le paramètre requis {0} est vierge.

Error.ParamScriptValidationError=Erreur de validation de la source du script du paramètre "{0}".

Error.ParameterGroupIsNotFoundByGroupname=Le groupe de paramètres n'a pas été trouvé sous le nom "{0}".

Error.ParameterInvalidGroupLevel=Le niveau de compte du groupe "{0}" ne correspond pas à celui qui est spécifié.

Error.InvalidDesignFile=Le fichier modèle {0} comporte une erreur et ne peut pas être exécuté.

Error.DesignFileNotFound=Le fichier modèle {0} est introuvable.

Error.CannotCreateExtensionInstance=L''initialisation de l''émetteur {0} par le moteur d''états a échoué, assurez-vous que les bibliothèques nécessaires à cet émetteur sont installées.

Error.MissingComputedColumnExpression=L''expression de la colonne de calcul {0} est manquante.

Error.ParameterInGroupIsnotScalar=Les paramètres du groupe de paramètres "{0}" doivent être scalaires.

Error.ParameterEvaluationError=Echec de l'évaluation du groupe de paramètres "{0}".

Error.InvalidEmitterID = EmitterID {0} pour l'option de rendu est nul.

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

Error.JarNotFoundError = Jar {0} introuvable.

Error.ScriptClassCastError = La classe {0} n'implémente pas l'interface {1}.

Error.ScriptClassNotFoundError = Classe {0} introuvable.

Error.ScriptClassIllegalAccessError = IllegalAccessException lors de la tentative de chargement de la classe {0}.

Error.ScriptClassInstantiationError = La classe {0} n''a pas pu être instanciée. Vérifiez qu''il ne s''agit pas d''une interface ou d''une classe abstraite.

Error.UnhandledScriptError = Exception non traitée lors de l'exécution du script.

###########################################################
Error.PageNotFoundError = Page {0} introuvable.

Error.PageNumberRangeError = Série de pages incorrecte : {0}.

Error.PageHintLoadingError = Impossible de charger l'indicateur de page.

Error.PagesLoadingError = Impossible de charger les pages {0}.

Error.InvalidGroupError = Groupe incorrect : {0}.

Error.InvalidExtensionError = ID d'extension non valide : {0} et format non valide : {1}.

Error.InvalidColumnIndexError = Index de colonne incorrect.

Error.InvalidInstanceIDError = ID d'instance incorrect : {0}.

Error.InvalidBookmarkError = Signet incorrect : {0}.

Error.ExpressionEvaluationError = L'expression de visibilité suivante ne correspond pas à une valeur booléenne autorisée : {0}.

Error.BookmarkNotFoundError = Signet introuvable : {0}.

Error.DateError = date : {0}.

Error.CubePositionError = Obtenir l'erreur de position du cube.

Error.SubqueryCreateError = La sous-requête ne peut être créée que dans une autre sous-requête / requête.

Error.BindDataRetrivingError = Récupération de l'erreur de liaison de données.

Error.DataSourceError = La source de données du jeu de données ne peut pas être nulle.

Error.UnsupportedQueryDefinitionError = Définition de requête non prise en charge {0}.

Error.ReportQueryLoadingError = Erreur lors du chargement de la requête d'état. Impossible d'extraire les données pour générer l'état.

Error.ReportQueryLoadingError2 = Impossible de charger la requête d'état : {0}. Des erreurs sont survenues au cours de la génération de l'état pour l'élément d'état portant l'ID {1}.

Error.ExtentionIDMissingError = ID d'extension manquant dans la définition de la source de données {0}.

Error.DataExportionError = Echec de l'exportation des données par ID d'instance : {0}.

Error.UnsupportedDocumentVersionError = N° de version du document non pris en charge : {0}.

Error.ResultsetIteratorError = L'itérateur du jeu de résultats doit être déplacé dans une ligne valide.

Error.ReportDesignNotFoundError = Impossible de localiser le modèle d'état dans l'état {0}.

Error.PreparedQueryNotFoundError = Impossible de localiser la requête préparée {0}.

Error.IncorrectParentResultSetError = Jeu de résultats parents incorrect pour la sous-requête : {0}.

Error.UnsupportedQueryTypeError = Type de requête non pris en charge {0}.

Error.QueryNotBuiltError = Echec de la création d'une requête pour l'élément : {0}.

Error.RenderTaskNotFinishedError = Tâche de rendu non terminée.

Error.RenderOptionError = Les options de rendu doivent être spécifiées pour rendre un état.

Error.ReportArchiveError = L'archive d'état n'est pas spécifiée lors de l'exécution d'un état.

Error.ReportDocNameNotSpecifiedError = Le nom de l'état n'est pas spécifié lors de l'exécution d'un état.

Error.ReportRunError = Une erreur s'est produite lors de l'exécution de l'état.

Error.ReportDocmentOpenError = L'ouverture de l'état a échoué.

Error.ReportArchiveOpenError = Impossible d'ouvrir l'archive d'état.

Error.SkipError = Une erreur s'est produite lorsque vous avez ignoré l'élément.

Error.ResultsetExtractError = Jeu de résultats introuvable.

Error.FailedToInitializeEmitter = Echec de l'initialisation de l'émetteur.

###########################################################
PDFCreator = Moteur d'états BIRT {0} utilisant iText ({1}).

###########################################################
Error.FlashObjectNotSupported = Les éléments d'état d'objet Flash ne sont pas pris en charge par ce format.

Error.ReportItemNotSupported = L'élément d'état actuel n'est pas pris en charge par ce format.

Error.ResourceUnreachable = La ressource de cet élément d'état est indisponible.

Error.UpdateUserAgent = Activer la prise en charge Flash ou télécharger une version plus récente d'Adobe Reader pour afficher le contenu Flash des fichiers PDF.

###########################################################
Error.ReportRunnableNotSet = L'état exécutable n'est pas défini.

Error.FailedToLoadTOCTree = Echec du chargement de l'arborescence de la table des matières.

Error.ExceptionOnFetchingBookmarks = Exception lors de la récupération des signets.

Error.FailedToSaveDesign = Echec lors de l'enregistrement du design.

Error.ExceptionOnCopyingArchives = Exception lors de la copie des archives.

Error.UnsupportedCoreStreamVersion = Version de flux principal non prise en charge : {0}.

Error.UnsupportedEngineExtension = Extension de moteur non pris en charge {0}.

Error.FailToCreateDataEngine = Impossible de créer le moteur de données.

Error.FailedToCreateTOC = Echec de la création de la table des matières.

Error.UnsupportedExprssionType = Type d'expression non pris en charge.

Error.UnknownContentVersion = Contenu de version inconnu.

Error.UnknownFieldID = ID de champ inconnue : {0}.

Error.ResourceNotAccessible = {0} n'est pas accessible.

Error.ERROR_WITH_STATUS=Le document contient des erreurs : {0}.

###########################################################
Error.ElementID = ID de l'élément

###########################################################
Message.TaskCancel = La tâche a été annulée.