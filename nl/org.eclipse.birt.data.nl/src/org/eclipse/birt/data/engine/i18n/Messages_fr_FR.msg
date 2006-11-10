# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} introuvable dans {1}.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=La source de données n''est pas définie : {0}
data.engine.UndefinedDataSet=Le jeu de données n''est pas défini : {0}
data.engine.UnsupportedDataSourceType=Le type de la source de données "{0}" n''est pas pris en charge.
data.engine.UnsupportedDataSetType=Le type du jeu de données "{0}" n''est pas pris en charge.
data.engine.UnsupportedGroupOnClobAndBLOB=Le groupement par données CLOB ou BLOB "{0}" n''est pas pris en charge.
data.engine.UnsupportedSortOnClobAndBLOB=Le tri par données CLOB ou BLOB [{0}] n''est pas pris en charge.

data.engine.EndOfGroupOnly=Cette expression d'agrégats n'est disponible qu'à la fin d'un groupe
data.engine.InvalidAggrGroup=Niveau de regroupement incorrect pour la fonction d''agrégation : {0}
data.engine.WrongNumAggrArgs=Nombre d''arguments incorrect pour la fonction d''agrégation : {0}

data.engine.DupPropertyName=Nom de propriété en double trouvé : {0}
data.engine.NoSubQueryName=Impossible de trouver une sous-requête portant le nom : {0}

data.engine.DataSourceMissingExtId=ID d''extension manquant dans la définition de la source de données {0}.
data.engine.NullGroupStart=La valeur de début de groupe ne doit pas être nulle.
data.engine.UnsupportedDirectNestedAggregate=L'imbrication directe d'agrégats n'est pas prise en charge.

data.engine.ResultClosed=QueryResults ou son itérateur a été fermé.
data.engine.NoCurrentRow=Aucune ligne n'est associée à Result iterator. Il n'a pas démarré ou a pris fin.
data.engine.InvalidJSExpr=Expression Javascript incorrecte : {0}
data.engine.InvalidTotalFuncName=Nom de fonction Total incorrect : {0}
data.engine.InvalidTotalFunc=Fonction Total incorrecte.
data.engine.NoAggrFunc=Appel de fonction d'agrégation inattendu dans cette expression.
data.engine.InvalidExprHandle=Descripteur d'expression incorrect.
data.engine.InvalidAggrGroupExpression=Expression de groupe incorrecte pour la fonction d''agrégation : {0}
data.engine.InvalidSortKeyColumn=Colonne de clé de tri incorrecte : {0}
data.engine.InvalidGroupLevel=Niveau de groupe incorrect : {0}
data.engine.InvalidGroupKeyName=Nom de colonne de clé de groupe incorrect : {0}
data.engine.BadCloseScript=Echec de l'évaluation de closeScript dans CustomDataSet.
data.engine.InvalidCustomFieldIdx=Index de champ personnalisé incorrect : {0}
data.engine.InvalidGroupName=Le nom de groupe est incorrect.
data.engine.DuplicateGroupName=Les noms de groupe sont dupliqués.

data.engine.BadOpenScript=Echec de l'évaluation de openScript dans CustomDataSet.
data.engine.DataSourceIsOpen=La source de données est déjà ouverte.
data.engine.DataSourceNotOpen=La source de données n'est pas ouverte.
data.engine.EmptyCustomFieldName=Le nom de champ personnalisé ne doit pas être vide.
data.engine.DupCustomFieldName=Nom de champ personnalisé en double : {0}
data.engine.NoResultSet=L'exécution de la requête n'a renvoyé aucun jeu de résultats.
data.engine.NoResultMetadata=Métadonnées de résultat non disponibles.
data.engine.QueryAlreadyPrepared=La requête a déjà été préparée.
data.engine.QueryNotPrepared=La requête n'a pas encore été préparée.
data.engine.InvalidFieldName=Nom de champ incorrect : {0}
data.engine.DupColumnName=Nom de colonne en double : {0}
data.engine.DupAlias=Alias ou nom de colonne en double : {0}
data.engine.InvalidFieldIndex=Index de champ incorrect : {0}

data.engine.MatchError=Vérifiez la syntaxe du motif. Erreur de correspondance entre la chaîne et le motif : {0} 
data.engine.BadOperandType=Type de données de l''opérande inattendu : {0}

data.engine.BadParameterCount=Le nombre de paramètres indiqué est incorrect.
data.engine.BadParameterType=Le type de données de l'un des paramètres est incorrect.
data.engine.NoIrrSolution=Impossible de trouver une solution pour la fonction Irr avec les données fournies.
data.engine.ExpectBooleanReturnType=Le script {0} du jeu de données nécessitant un script doit renvoyer une valeur booléenne.
data.engine.BadIntervalUnit=Valeur d''unité d''intervalle incorrecte : {0}
data.engine.InvalidFieldName=Nom de champ incorrect : {0}
data.engine.BadGroupKeyType=Le type de données de la clé de groupe n''est pas pris en charge : {0}
data.engine.group.interval=La définition d''un intervalle {0} sur une colonne de type {1} n''est pas prise en charge.
data.engine.group.interval.Rowid=L'intervalle numérique est le seul intervalle pris en charge dans la colonne rowId.
data.engine.group.interval.invalid=L'intervalle indiqué n'est pas valide.
data.engine.NoSupportFilterOnGroup=Le filtrage de groupes n'est pas pris en charge actuellement.
data.engine.InvalidTopBottomArgument=L'argument des filtres Haut/Bas doit être un nombre.
data.engine.InvalidTopBottomNArgument=L'argument des filtres TopN/BottomN doit être zéro ou un entier positif.
data.engine.InvalidTopBottomPercentArgument=L'argument des filtres TopPercent/BottomPercent doit être positif et inférieur ou égal à 100.
data.engine.InvalidTopBottomN=Le nombre total de lignes à traiter ne doit pas dépasser {0}
data.engine.InvalidPercentileArgument=La fonction Total.Percentile n'accepte que les nombres compris entre 0 et 1 (inclus) pour son second argument.
data.engine.InvalidQuartileArgument=La fonction Total.Quartile n'accepte que les entiers compris entre 0 et 4 (inclus) pour son second argument.
data.engine.BadAggrFuncParam=Paramètre incorrect pour la fonction d''agrégation : {0}
data.engine.ResetRateGuess=L'utilisateur devrait définir une nouvelle estimation de taux.
data.engine.UnsupportedCondOp=L''opérateur conditionnel n''est pas pris en charge : {0}
data.engine.EmptyExpression=L'expression ne peut être ni nulle ni vide.

data.engine.InvalidJoinType=Type de jointure incorrect.
data.engine.InvalidJoinOperator=Opérateur de jointure incorrect.
data.engine.BadDataSetParamName=Paramètre de jeu de données non défini : {0}.

#column binding
data.engine.InvalidBoundColumnName=Nom de colonne liée incorrecte : {0}.
data.engine.InvalidGroupKey=La clé de groupe "{0}" du groupe {1} ne peut pas dépendre des liaisons de colonne du groupe inférieur. 
data.engine.ColumnBindingNotExist=La liaison de colonne "{0}" n'existe pas.
data.engine.ColumnBindingCycle=La liaison de colonne "{0}" possède un cycle de dépendance.

#computed column value
data.engine.BadCompColExpr=L''expression de la colonne de calcul "{0}" est incorrecte.
data.engine.CompCol.WrongDataType=Le type de données de la colonne de calcul "{0}" est {1}, mais l''une de ses valeurs est "{2}" et ne peut pas être convertie en {1}.
data.engine.ScriptResult.WrongDataType=Le type de données du résultat de l''expression de script est {0}, mais l''une de ses valeurs est "{1}", qui ne peut pas être convertie en {0}.

data.engine.BadFilterExpr=L''expression de filtre "{0}" n''est pas valide.
data.engine.BadFilterDefn=La définition de filtre utilisant l''expression "{0}" n''est pas valide.

#clob and blob error
data.engine.opencloberror=Erreur de flux lors de l'ouverture du caractère CLOB.
data.engine.readcloberror=Erreur de flux lors de la lecture du caractère CLOB.
data.engine.openbloberror=Erreur de flux lors de l'ouverture du flux de données BLOB.
data.engine.readbloberror=Erreur de flux lors de la lecture du flux de données BLOB.

#resultsetcache exception
data.engine.BadDestIndex=La valeur {0} de destIndex est incorrecte. Elle devrait être comprise entre 0 et {1}.
data.engine.WriteTempError=Exception lors de l'écriture du fichier temporaire pour un jeu de données volumineux.
data.engine.ReadTempError=Exception lors de la lecture du fichier temporaire pour un jeu de données volumineux.

#load data engine factory exception
load.factory.error=Une erreur s'est produite lors du chargement de la génération du moteur de données.

# Wrapped Error
data.engine.DataConversionError=Une erreur s'est produite lors de la conversion du type de données.
data.engine.BadDataTypeCondExpr=Type de données incorrect trouvé dans l'expression conditionnelle.
data.engine.BirtException=Une exception BIRT est survenue : {0}. Voir l''exception suivante pour plus d''informations.

# data set cache error
data.engine.datasetcache.save.error=Une erreur est survenue lors de l'enregistrement de fichiers du cache des jeux de données.
data.engine.datasetcache.load.error=Une erreur est survenue lors du chargement de fichiers du cache des jeux de données.

# error related with data engine context
data.engine.InvalidMode=La valeur de mode {0} pour le contexte du moteur de données n''est pas valide.
data.engine.InvalidArchive=Pour la génération ou la présentation, l'archive d'état ne peut pas être nulle. 
data.engine.SaveReportDocumentError=Une erreur d''état s''est produite lors de l''enregistrement : {0}
data.engine.LoadReportDocumentError=Une erreur d''état s''est produite lors du chargement : {0}
data.engine.GetSaveStreamError=Une erreur d'état s'est produite lors de la récupération du flux enregistré.
data.engine.GetLoadStreamError=Une erreur d'état s'est produite lors de la récupération du flux chargé.
data.engine.NullExpr=L'expression ne peut pas être nulle pour la récupération de la valeur dans la présentation.
data.engine.InValidExpr=Expression non valide pour récupérer la valeur dans la présentation.
data.engine.BadGroupExpr=L'expression de groupe doit être renseignée. 
data.engine.ResultSetNotStart=Le jeu de résultats n'a pas encore démarré.

data.engine.invalidRowIndex=Valeur d''index de ligne incorrecte : {0}
data.engine.backwardSeekError=La recherche en arrière n'est pas prise en charge dans cette version.
data.engine.startError=La méthode suivante doit d'abord être appelée.

# query running based on report document
data.engine.rd.errorfilters=Les filtres définis dans la requête ne sont pas pris en charge.

# other error
data.engine.ScriptEvalError=Erreur d''évaluation de script (source : {0}, ligne : {1} : {2}.
data.engine.PreparedQueryClosed=L'instance PreparedQuery est fermée.
data.engine.NullDefaultInParamValue=La valeur par défaut du paramètre d'entrée ne peut pas être nulle.
data.engine.BadGroupKeyExpr=L''expression de clé de groupe {0} est incorrecte.
data.engine.GroupNotExist=Le groupe "{0}" utilisé dans la colonne {1} n''existe pas.
data.engine.BadSortKeyExpr=L''expression de clé de tri {0} est incorrecte.
data.engine.NoParamMetaForDataSource=Impossible d'obtenir les métadonnées du paramètre pour ce type de source de données.
data.engine.UnexpectedError=Echec du moteur de données à la suite d'une exception inattendue. 
data.engine.ConvertDataTypeError=Impossible de convertir la valeur de paramètre {0} au type {1}.
data.engine.CannotInstantiateAggregationFactory=Impossible d''instancier la génération de l''agrégation {0}.
date.engine.DuplicateAggregationName=Nom de l''agrégation en double : {0}
data.engine.incorrectGroupKeyValues=Le tableau des valeurs clés de groupe donné est incorrect.
data.engine.NoExpressionProcessorAvailable=Aucun ExpressionProcessor disponible.
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} ne peut pas être converti en {1}.
data.engine.NoRowUpdate=Impossible de mettre à jour la ligne de données pour le moment.
data.engine.BadDataType=Le type de données n'est pas pris en charge.
data.engine.BadDataExpression=L'expression de données ne doit pas être nulle.

data.engine.cacheFunctionWrongMode=de création de pdf en cache ne fonctionne qu'en mode DIRECT_PRESENTATION.
data.engine.document.invalidIndexArray=Le tableau d'index n'est pas valide.
data.engine.document.invalidGroupItem=L'élément de groupe supprimé n'est pas correct.
data.engine.resultIteratorNotOpen=Result iterator n'est pas encore ouvert.
data.engine.resultIteratorClosed=Result iterator a été fermé.
data.engine.invalidOutputParameterIndex=Index de paramètre de sortie non valide : {0}
data.engine.outputParameterOutOfBound=L''index de paramètre de sortie est hors limites : {0}
data.engine.invalidOutputParameterName=Nom de paramètre de sortie non valide : {0}
data.engine.document.notSupportInPresentation=Non pris en charge dans la présentation.
data.engine.wrongStatus=Etat incorrect.
data.engine.wrongVersion=Non pris en charge dans les versions antérieures à la version 2.1.
data.engine.unsupportOperationException=Opération non prise en charge.
data.engine.resultClassSaveError=Certaines références de colonne dans la liaison de colonne n'existent pas.

# oda consumer error
odaconsumer.ColumnNameCannotBeEmptyOrNull=Le nom de colonne ne peut être ni vide ni nul.
odaconsumer.ColumnPositionIsOneBased=Column position is 1-based and cannot be less than 1.
odaconsumer.ColumnAliasCannotBeEmpty=L'alias de colonne ne peut pas être une chaîne vide.
odaconsumer.CannotProcessDriverConfig=Impossible de traiter les configurations du pilote {0}.
odaconsumer.OdaInitEntryNotFound=Impossible de détecter ou d''instancier le point d''entrée initial du pilote {0}.
odaconsumer.OdaDriverOnUnsupportedPlatform=Le pilote {0} ne prend pas en charge {1}.
odaconsumer.CannotGenerateUrl=Impossible de générer l''URL {0} du chargeur de classe.
odaconsumer.UnsupportedNativeType=Type natif {0} non pris en charge pour le type de jeu de données {1} du pilote {2}.
odaconsumer.UnsupportedDataSetType=Le type de jeu de données {0} n''est pas pris en charge par le pilote {1}.
odaconsumer.CannotDetermineDefaultDataSetType=Type de jeu de données nul. Impossible de déterminer le type de jeu de données par défaut pour le pilote {0}.
odaconsumer.InvalidDataSetType=Type de jeu de données incorrect {0}. Impossible de déterminer le type de jeu de données par défaut pour le jeu de données {1}.
odaconsumer.ParameterNameCannotBeEmptyOrNull=Le nom de paramètre ne peut être ni vide ni nul.
odaconsumer.ParameterPositionIsOneBased=La position du paramètre ne peut pas être inférieure à 1.
odaconsumer.CannotGetParameterTypeName=Impossible d''obtenir le nom du type du paramètre : {0}.
odaconsumer.CannotGetParameterMode=Impossible d''obtenir le mode du paramètre : {0}.
odaconsumer.CannotGetParameterScale=Impossible d''obtenir l''échelle du paramètre : {0}.
odaconsumer.CannotGetParameterPrecision=Impossible d''obtenir la précision du paramètre : {0}.
odaconsumer.CannotGetParameterIsNullable=Impossible de déterminer le caractère de nullité du paramètre : {0}.
odaconsumer.SameParamNameForDifferentHints=Un autre paramètre du jeu de données est défini avec le même nom {0}.
odaconsumer.DifferentParamNameForSamePosition=Un autre paramètre {0} du jeu de données est défini avec la même position {1}.
odaconsumer.ParameterValueIsNull=La valeur du paramètre est nulle.
odaconsumer.UnsupportedParameterValueType=Type de valeur de paramètre non pris en charge : {0}
odaconsumer.UnknownExceptionThrown=Exception inconnue basculée dans le dernier appel de la méthode de réglage.
odaconsumer.CannotConvertIndexedParameterValue=Impossible de convertir la valeur de paramètre {0} à l''index {1} à partir du type de valeur {2} vers le type ODA {3}.
odaconsumer.CannotConvertNamedParameterValue=Impossible de convertir la valeur de paramètre {0} avec le nom de paramètre {1} à partir du type de valeur {2} vers le type ODA {3}.
odaconsumer.UnrecognizedProjectedColumnName=Nom de colonne projetée non reconnu : {0}.
odaconsumer.ColumnNameOrAliasAlreadyUsed=Le nouveau nom ou alias de colonne {0} est déjà utilisé par la colonne à l''index {1}
odaconsumer.NamedResultSetsUnsupported=Cette instruction ne prend pas en charge les jeux de résultats nommés.
odaconsumer.OutputParameterUnsupported=Cette instruction ne prend pas en charge les paramètres de sortie.
odaconsumer.CannotGetConnectionMetaData=Impossible d'obtenir les métadonnées de connexion.
odaconsumer.CannotGetMaxConnections=Impossible d'obtenir le nombre maximum de connexions pour la source de données.
odaconsumer.CannotGetMaxStatements=Impossible d'obtenir le nombre maximum de requêtes pour la source de données.
odaconsumer.CannotGetDataSetMetaData=Impossible d''obtenir les métadonnées de jeu de données {0} depuis la source de données.
odaconsumer.CannotPrepareStatement=Echec de préparation de la requête suivante pour le type de jeu de données {1}. \n[{0}]
odaconsumer.CannotCloseConnection=Impossible de fermer la connexion.
odaconsumer.CannotOpenConnection=Impossible d''établir la connexion pour le pilote : {0}
odaconsumer.CannotDetermineSupportForMultipleOpenResults=Impossible de déterminer la prise en charge de plusieurs résultats ouverts.
odaconsumer.CannotDetermineSupportForMultipleResultSets=Impossible de déterminer la prise en charge de plusieurs jeux de résultats.
odaconsumer.CannotDetermineSupportForNamedResultSets=Impossible de déterminer la prise en charge des jeux de résultats nommés.
odaconsumer.CannotDetermineSupportForNamedParameters=Impossible de déterminer la prise en charge des paramètres nommés.
odaconsumer.CannotDetermineSupportForInParameters=Impossible de déterminer la prise en charge des paramètres d'entrée.
odaconsumer.CannotDetermineSupportForOutParameters=Impossible de déterminer la prise en charge des paramètres de sortie.
odaconsumer.CannotSetStatementProperty=Impossible de définir la propriété d'instruction.
odaconsumer.CannotSetSortSpec=Impossible de définir la spécification de tri.
odaconsumer.CannotSetMaxRows=Impossible de définir le nombre maximal de lignes.
odaconsumer.CannotGetResultSetMetaData=Impossible d'obtenir les métadonnées du jeu de résultats.
odaconsumer.CannotGetMetaDataForNamedResultSet=Impossible d''obtenir les métadonnées du jeu de résultats nommé : {0}
odaconsumer.CannotExecuteStatement=Impossible d'exécuter l'instruction.
odaconsumer.CannotGetResultSet=Impossible d'obtenir le jeu de résultats.
odaconsumer.CannotGetNamedResultSet=Impossible d''obtenir le jeu de résultats nommé : {0}
odaconsumer.CannotFindOutParameter=Le paramètre de sortie est introuvable : {0}
odaconsumer.CannotGetParameterType=Impossible d''obtenir le type du paramètre : {0}
odaconsumer.CannotCloseStatement=Impossible de terminer l'instruction.
odaconsumer.CannotGetColumnCount=Impossible d'obtenir le nombre de colonnes.
odaconsumer.CannotGetColumnName=Impossible d''obtenir le nom de colonne : {0}
odaconsumer.CannotGetColumnLabel=Impossible d''obtenir l''étiquette de colonne : {0}
odaconsumer.CannotGetColumnType=Impossible d''obtenir le type de colonne : {0}
odaconsumer.CannotFetchNextRow=Impossible de récupérer la ligne de données suivante.
odaconsumer.CannotDetermineWasNull=Impossible de déterminer si la dernière valeur récupérée était nulle.
odaconsumer.CannotGetIntFromColumn=Impossible d''obtenir la valeur d''entier depuis la colonne : {0}
odaconsumer.CannotGetDoubleFromColumn=Impossible d''obtenir la valeur double depuis la colonne : {0}
odaconsumer.CannotGetStringFromColumn=Impossible d''obtenir la valeur de chaîne depuis la colonne : {0}
odaconsumer.CannotGetBigDecimalFromColumn=Impossible d''obtenir la valeur décimale depuis la colonne : {0}
odaconsumer.CannotGetDateFromColumn=Impossible d''obtenir la valeur de date depuis la colonne : {0}
odaconsumer.CannotGetTimeFromColumn=Impossible d''obtenir la valeur temporelle depuis la colonne : {0}
odaconsumer.CannotGetTimestampFromColumn=Impossible d''obtenir la valeur d''horodatage depuis la colonne : {0}
odaconsumer.CannotGetClobFromColumn=Impossible d''obtenir la valeur CLOB depuis la colonne : {0}
odaconsumer.CannotGetBlobFromColumn=Impossible d''obtenir la valeur BLOB depuis la colonne : {0}
odaconsumer.CannotGetRowPosition=Impossible d'obtenir la position de la ligne actuelle.
odaconsumer.CannotCloseResultSet=Impossible de fermer le jeu de résultats.
odaconsumer.CannotGetParameterCount=Impossible d'obtenir le nombre de paramètres.
odaconsumer.CannotGetIntFromParameter=Impossible d''obtenir la valeur d''entier depuis le paramètre : {0}
odaconsumer.CannotGetDoubleFromParameter=Impossible d''obtenir la valeur double depuis le paramètre : {0}
odaconsumer.CannotGetStringFromParameter=Impossible d''obtenir la valeur de chaîne depuis le paramètre : {0}
odaconsumer.CannotGetBigDecimalFromParameter=Impossible d''obtenir la valeur décimale depuis le paramètre : {0}
odaconsumer.CannotGetDateFromParameter=Impossible d''obtenir la valeur de date depuis le paramètre : {0}
odaconsumer.CannotGetTimeFromParameter=Impossible d''obtenir la valeur temporelle depuis le paramètre : {0}
odaconsumer.CannotGetTimestampFromParameter=Impossible d''obtenir la valeur d''horodatage depuis le paramètre : {0}
odaconsumer.CannotGetBlobFromParameter=Impossible d''obtenir la valeur BLOB depuis le paramètre : {0}
odaconsumer.CannotGetClobFromParameter=Impossible d''obtenir la valeur CLOB depuis le paramètre : {0}
odaconsumer.CannotFindInParameter=Le paramètre d''entrée est introuvable : {0}
odaconsumer.CannotSetIntParameter=Impossible de définir la valeur d''entier du paramètre : {0}
odaconsumer.CannotSetDoubleParameter=Impossible de définir la valeur double du paramètre : {0}
odaconsumer.CannotSetStringParameter=Impossible de définir la valeur de chaîne du paramètre : {0}
odaconsumer.CannotSetBigDecimalParameter=Impossible de définir la valeur décimale du paramètre : {0}
odaconsumer.CannotSetDateParameter=Impossible de définir la valeur de date du paramètre : {0}
odaconsumer.CannotSetTimeParameter=Impossible de définir la valeur temporelle du paramètre : {0}
odaconsumer.CannotSetTimestampParameter=Impossible de définir la valeur d''horodatage du paramètre : {0}
odaconsumer.CannotClearInParameters=Impossible d'effacer les valeurs de paramètre d'entrée.
odaconsumer.UnrecognizedOdaType=Type de données ODA non reconnu : {0}
odaconsumer.CannotGetColumnNativeDataTypeName=Impossible d''obtenir le nom du type de données natives pour la colonne : {0}
odaconsumer.CannotGetParameterMetaData=Impossible d'obtenir les métadonnées du paramètre de pilote ODA.
odaconsumer.CannotLoadOdaAdapter=Impossible de charger le pilote {0} ; le plug-in de l''adaptateur DTP-to-BIRT ODA {1} est manquant.
odaconsumer.MustSpecifyColumnName=Un nom de colonne doit être spécifié lors de l'ajout d'une astuce de colonne.

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=Impossible d'obtenir les jeux de résultats par nom après l'appel de executeQuery().
odaconsumer.manager.CannotSetParamBeforePrepare=Impossible de définir les paramètres d'entrée avant que l'instruction n'ait été préparée.
odaconsumer.manager.CannotGetParamBeforeExecute=Impossible d'obtenir des paramètres de sortie avant l'exécution de l'instruction.
odaconsumer.manager.CannotGetParamMdBeforePrepare=Impossible d'obtenir les métadonnées du paramètre avant que l'instruction n'ait été préparée.
odaconsumer.manager.CannotSetSortSpecBeforePrepare=Impossible de définir la spécification de tri avant que l'instruction n'ait été préparée.
odaconsumer.manager.MaxConcurrentConnectionsReached=Nombre maximal de connexions actives simultanées atteint.
odaconsumer.manager.ConnectionFailed=Impossible d'ouvrir la connexion.
odaconsumer.manager.CannotConstructConnectionFactory=Impossible de créer la génération de connexion du pilote ODA.
odaconsumer.manager.InadequatePermissionsForCompatibility=Compatibilité descendante impossible en raison d'autorisations inappropriées.
odaconsumer.manager.ConnectionIsInactive=La connexion à la source de données n'est pas active.
odaconsumer.manager.MaxConcurrentStatementsReached=Nombre maximal d'instructions actives simultanées atteint pour cette connexion.
odaconsumer.manager.PrepareFailed=Echec de la préparation.
odaconsumer.manager.CannotGetResultSetMdBeforePrepare=Impossible d'obtenir les métadonnées du jeu de résultats avant que l'instruction n'ait été préparée.
odaconsumer.manager.CannotExecuteBeforePrepare=Impossible d'exécuter l'instruction avant qu'elle n'ait été préparée.
odaconsumer.CannotFindLogDirectory=Le répertoire du journal est introuvable.
