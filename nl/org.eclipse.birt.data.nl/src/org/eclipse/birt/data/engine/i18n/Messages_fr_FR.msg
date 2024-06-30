#************************************************************************
# Copyright (c) 2004, 2010 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#
#************************************************************************
# Localized messages for the DTE Engine. Messages here give
# the text of Data Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} introuvable dans {0}.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=La source de données n'est pas définie : {0}.

data.engine.UndefinedDataSet=Le jeu de données n'est pas défini : {0}.

data.engine.UnsupportedDataSourceType=Le type de la source de données "{0}" n''est pas pris en charge.

data.engine.UnsupportedDataSetType=Le type du jeu de données "{0}" n''est pas pris en charge.

data.engine.UnsupportedGroupOnClobAndBLOB=Le groupement par données CLOB ou BLOB "{0}" n''est pas pris en charge.

data.engine.UnsupportedSortOnClobAndBLOB=Le tri par données CLOB ou BLOB [{0}] n''est pas pris en charge.

data.engine.EndOfGroupOnly=Cette expression d'agrégats n'est disponible qu'à la fin d'un groupe.

data.engine.InvalidAggregation=La définition d'agrégation est incorrecte :{0}.

data.engine.InvalidAggrGroup=Niveau de regroupement incorrect pour la fonction d'agrégation : {0}.

data.engine.WrongNumAggrArgs=Nombre d'arguments incorrect pour la fonction d'agrégation : {0}.

data.engine.AggrParamGroupLevelInconsistent=Dans l'agrégation imbriquée "{0}", toutes les expressions non constantes doivent avoir le même niveau de groupe.

data.engine.DupPropertyName=Nom de propriété en double trouvé : {0}.

data.engine.NoSubQueryName=Impossible de trouver une sous-requête portant le nom : {0}.

data.engine.DataSourceMissingExtId=ID d''extension manquant dans la définition de la source de données {0}.

data.engine.NullGroupStart=La valeur de début de groupe ne doit pas être nulle.

data.engine.UnsupportedDirectNestedAggregate=L'imbrication directe d'agrégats n'est pas prise en charge.

data.engine.ResultClosed=QueryResults ou son itérateur a été fermé.

data.engine.NoCurrentRow=Aucune ligne n'est associée à Result iterator. Il n'a pas démarré ou a pris fin.

data.engine.InvalidJSExpr=Expression Javascript incorrecte : {0}.

data.engine.InvalidTotalFuncName=Nom de fonction Total incorrect : {0}.

data.engine.InvalidTotalFunc=Fonction Total incorrecte.

data.engine.NoAggrFunc=Appel de fonction d'agrégation inattendu dans cette expression.

data.engine.InvalidExprHandle=Descripteur d'expression incorrect.

data.engine.InvalidAggrGroupExpression=Expression de groupe incorrecte pour la fonction d'agrégation : {0}.

data.engine.InvalidSortKeyColumn=Colonne de clé de tri incorrecte : {0}.

data.engine.InvalidGroupLevel= Le niveau de groupe dans l'agrégation est incorrect : {0}.

data.engine.InvalidGroupKeyName=Regroupement dans la colonne : {0} n'est pas une opération prise en charge.

data.engine.BadCloseScript=Echec de l'évaluation de closeScript dans CustomDataSet.

data.engine.InvalidCustomFieldIdx=Index de champ personnalisé incorrect : {0}.

data.engine.InvalidGroupName=Le nom de groupe est incorrect.

data.engine.DuplicateGroupName=Les noms de groupe sont dupliqués.

data.engine.BadOpenScript=Echec de l'évaluation de openScript dans CustomDataSet.

data.engine.DataSourceIsOpen=La source de données est déjà ouverte.

data.engine.DataSourceNotOpen=La source de données n'est pas ouverte.

data.engine.EmptyCustomFieldName=Le nom de champ personnalisé ne doit pas être vide.

data.engine.DupCustomFieldName=Nom de champ personnalisé en double : {0}.

data.engine.NoResultSet=L'exécution de la requête n'a renvoyé aucun jeu de résultats.

data.engine.NoResultMetadata=Métadonnées de résultat non disponibles.

data.engine.QueryAlreadyPrepared=La requête a déjà été préparée.

data.engine.QueryNotPrepared=La requête n'a pas encore été préparée.

data.engine.InvalidFieldName=Nom de champ incorrect : {0}.

data.engine.DupColumnName=Nom de colonne en double : {0}.

data.engine.DupAlias=Alias ou nom de colonne en double : {0}.

data.engine.InvalidFieldIndex=Index de champ incorrect : {0}.

data.engine.fail.prepareExecution=Echec de préparation de l'exécution de la requête pour le jeu de données : {0}.

data.engine.groupIncrementalUpdate.IllegalGroupOrderState=Etat de la mise à jour du groupe incorrect : groupe actuel <{0}> supérieur au groupe de traitement <{1}>.

data.engine.MatchError=Vérifiez la syntaxe du motif. Erreur de correspondance entre la chaîne et le motif : {0}.

data.engine.ExpectBooleanReturnType=Le script {0} du jeu de données nécessitant un script doit renvoyer une valeur booléenne.

data.engine.BadIntervalUnit=Valeur d'unité d'intervalle incorrecte : {0}.

data.engine.BadGroupKeyType=Le type de données de la clé de groupe n'est pas pris en charge : {0}.

data.engine.group.interval=La définition d''un intervalle {0} sur une colonne de type {1} n''est pas prise en charge.

data.engine.group.interval.Rowid=L'intervalle numérique est le seul intervalle pris en charge dans la colonne ROWID.

data.engine.group.interval.invalid=L'intervalle indiqué n'est pas valide.

data.engine.group.intervalRange.invalid=Plage d''intervalles non valide : {0}.

data.engine.NoSupportFilterOnGroup=Le filtrage de groupes n'est pas pris en charge actuellement.

data.engine.InvalidTopBottomArgument=L'argument des filtres Haut/Bas doit être un nombre.

data.engine.InvalidTopBottomNArgument=L'argument des filtres TopN/BottomN doit être zéro ou un entier positif.

data.engine.InvalidTopBottomPercentArgument=L'argument des filtres TopPercent/BottomPercent doit être positif et inférieur ou égal à 100.

data.engine.UnsupportedCondOp=L'opérateur conditionnel n'est pas pris en charge : {0}.

data.engine.EmptyExpression=L'expression ne peut être ni nulle ni vide.

data.engine.AggregationBinding.EmptyArgument=L'argument "{0}" est requis par la liaison d'agrégation "{1}".

data.engine.InvalidJoinType=Type de jointure incorrect.

data.engine.InvalidJoinOperator=Opérateur de jointure incorrect.

data.engine.BadDataSetParamName=Paramètre de jeu de données non défini : {0}.

data.engine.FailComputeOutputParameterValue=Echec de l'évaluation des valeurs de paramètres de sortie : {0}.

data.engine.LinkedReportParamAllowMultiValues=Le paramètre d'état "{0}" qui autorise des valeurs multiples ne peut pas être utilisé pour la liaison au paramètre de jeu de données "{1}".

#column binding
data.engine.EmptyBindingName=Le nom de liaison ne peut être ni nul ni vide.

data.engine.InvalidBoundColumnName=Nom de colonne liée incorrecte : {0}.

data.engine.InvalidGroupKey=La clé de groupe "{0}" du groupe {1} ne peut pas dépendre des liaisons de colonne du groupe inférieur.

data.engine.ColumnBindingNotExist=La liaison "{0}" n'existe pas.

data.engine.ColumnBindingCycle=La liaison "{0}" possède un cycle de dépendance.

data.engine.ColumnBindingReferToInexistColumn=La liaison de colonne "{0}" a renvoyé vers une liaison "{1}" qui n'existe pas.

data.engine.ColumnBindingReferToInexistBinding=La liaison de colonne "{0}" a renvoyé vers une colonne de jeu de données "{1}" qui n'existe pas.

data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=La liaison de colonne "{0}" est incorrecte : les liaisons de colonne de requête parent qui incluent des agrégations ne peuvent pas être utilisées dans les liaisons de colonne d'une sous-requête.

data.engine.ReadColumnValueFromDocumentError=Une erreur s'est produite lors de la tentative de lecture de la valeur de la colonne : {0}. Une erreur d'exécution s'est peut-être produite lors de l'évaluation d'une expression de liaison, c'est-à-dire une erreur causée par une division par 0.

data.engine.NoOuterResultsExist=Les résultats de la requête externe sont introuvables. Cela peut être dû à la présence d'une référence externe dans une requête sans requête parent.

data.engine.DuplicatedBindingName=Le nom de la liaison est en double : {0}.

data.engine.InvalidAggrLevelKeyInSummaryQuery=L'agrégation : {0} se rapporte à un nom de groupe incorrect.
#computed column value
data.engine.BadCompColExpr=L''expression de la colonne de calcul "{0}" est incorrecte.

data.engine.CompCol.FailRetrieveValueComputedColumn=Echec du calcul de la valeur pour la colonne calculée "{0}".

data.engine.ScriptResult.WrongSystemComputedColumn=Impossible d'évaluer l'expression de tri, de groupe, de filtre ou de liaison. Un des champs de données utilisés dans l'expression n'existe pas dans le jeu de données.

data.engine.ComputedColumnCycle=La colonne de calcul "{0}" possède un cycle de dépendance.

data.engine.SortOnAggregation=Le tri selon l'agrégation de non-regroupement ou la colonne de calcul référencée par l'agrégation "{0}" n'est pas pris en charge.

data.engine.BadFilterExpr=L''expression de filtre "{0}" n''est pas valide.

data.engine.BadFilterDefn=Echec de l'évaluation du filtre basé sur l'expression "{0}".

#clob and blob error
data.engine.opencloberror=Erreur de flux lors de l'ouverture du caractère CLOB.

data.engine.readcloberror=Erreur de flux lors de la lecture du caractère CLOB.

data.engine.openbloberror=Erreur de flux lors de l'ouverture du flux de données BLOB.

data.engine.readbloberror=Erreur de flux lors de la lecture du flux de données BLOB.

#resultsetcache exception
data.engine.BadDestIndex=La valeur {0} de destIndex est incorrecte. Elle devrait être comprise entre 0 et {1}.

data.engine.WriteTempError=Exception lors de l'écriture du fichier temporaire pour un jeu de données volumineux.

data.engine.ReadTempError=Exception lors de la lecture du fichier temporaire pour un jeu de données volumineux.

#resultiterator cache exception
data.engine.CreateCacheTempError=Exception lors de la création du fichier temporaire pour la mise en mémoire cache des lignes de résultats.

data.engine.OpenCacheTempError=Exception lors de l'ouverture du fichier temporaire pour la mise en mémoire cache des lignes de résultats.

data.engine.WriteCacheTempError=Exception lors de l'écriture du fichier temporaire pour la mise en mémoire cache des lignes de résultats.

data.engine.ReadCacheTempError=Exception lors de la lecture du fichier temporaire pour la mise en mémoire cache des lignes de résultats.

data.engine.CloseCacheTempError=Exception lors de la fermeture du fichier temporaire pour la mise en mémoire cache des lignes de résultats.

data.engine.FailedLoadColumnValue=Echec du chargement de la valeur pour "{0}" colonne de type "{1}".

data.engine.FailedLoadClass=Echec du chargement de la classe "{0}" pour "{1}" colonne de type "{2}". assurez-vous que la classe "{0}" est comprise dans le chemin du chargement de classe du moteur de recherche BIRT.

#load data engine factory exception
load.factory.error=Une erreur s'est produite lors du chargement de la génération du moteur de données.

# Wrapped Error
data.engine.DataConversionError=Une erreur s'est produite lors de la conversion du type de données.

data.engine.BadCompareExpr=Impossible de comparer "{0}" avec "{1}".

data.engine.BadCompareSingleValueWithMultiValues=Echec de l'application du filtre, car la condition de filtre est incorrecte (une valeur unique est comparée avec des valeurs multiples de [{0}]).\nNe définissez qu'une seule valeur pour ce filtre.

data.engine.BirtException=Une exception BIRT s'est produite. Voir l'exception suivante pour plus d'informations.

# data set cache error
data.engine.datasetcache.save.error=Une erreur est survenue lors de l'enregistrement de fichiers du cache des jeux de données.

data.engine.datasetcache.load.error=Une erreur est survenue lors du chargement de fichiers du cache des jeux de données.

data.engine.exceed.max.data.object.row=Impossible de traiter la requête. Pour exécuter la requête, la taille du tampon du jeu de résultats doit être augmentée ou la requête doit être limitée.

# error related with data engine context
data.engine.InvalidMode=La valeur de mode {0} pour le contexte du moteur de données n''est pas valide.

data.engine.InvalidArchive=Pour la génération ou la présentation, l'archive d'état ne peut pas être nulle.

data.engine.SaveReportDocumentError=Une erreur d'état s'est produite lors de l'enregistrement : {0}.

data.engine.LoadReportDocumentError=Une erreur d'état s'est produite lors du chargement : {0}.

data.engine.GetSaveStreamError=Une erreur d'état s'est produite lors de la récupération du flux enregistré.

data.engine.GetLoadStreamError=Une erreur d'état s'est produite lors de la récupération du flux chargé.

data.engine.NullExpr=L'expression ne peut pas être nulle pour la récupération de la valeur dans la présentation.

data.engine.InValidExpr=Expression non valide pour récupérer la valeur dans la présentation.

data.engine.BadGroupExpr=L'expression de groupe doit être renseignée.

data.engine.filterExprContainRowNum=L'expression de filtre ne peut pas référencer l'objet de script java "row.__num".

data.engine.sortExprContainRowNum=L'expression de tri ne peut pas référencer l'objet de script java "row.__num".

data.engine.ResultSetNotStart=Le jeu de résultats n'a pas encore démarré.

data.engine.GetLevelMemberError=Cette liaison de données est utilisée en dehors de l'étendue proposée.

data.engine.invalidRowIndex=Valeur d'index de ligne incorrecte : {0}.

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

data.engine.NoParamMetaForDataSource=Impossible d''obtenir les métadonnées du paramètre pour ce type de source de données.

data.engine.UnexpectedError=Echec du moteur de données à la suite d'une exception inattendue.

data.engine.ConvertDataTypeError=Impossible de convertir la valeur de paramètre {0} au type {1}.

data.engine.CannotInstantiateAggregationFactory=Impossible d''instancier la génération de l''agrégation {0}.

date.engine.DuplicateAggregationName=Nom de l'agrégation en double : {0}.

data.engine.InconsistentAggregationArgumentDefinition=L'argument d'agrégation défini dans l'implémentation de l'extension d'agrégation est incohérent avec celui défini dans le fichier plugin.xml.

data.engine.incorrectGroupKeyValues=Le tableau des valeurs clés de groupe donné est incorrect.

data.engine.NoExpressionProcessorAvailable=Aucun ExpressionProcessor disponible.

data.engine.InconvertibleDatatype=Le type de données d'origine de la valeur {0} est {1}, qui ne peut pas être converti en type de données {2}.

data.engine.ConvertToDataTypeError={0} ne peut pas être converti en {1}.

data.engine.NoRowUpdate=Impossible de mettre à jour la ligne de données pour le moment.

data.engine.BadDataType=Le type de données n'est pas pris en charge{0}.

data.engine.NotSerializableClass=La classe {0} n'est pas prise en charge pour une sérialisation.

data.engine.BadDataExpression=L'expression de données ne doit pas être nulle.

data.engine.aggregation.InvalidBindingExpression=La mesure calculée n'est pas correcte pour la liaison d'agrégation "{0}".

data.engine.InvalidExpression=Expression "{0}" non valide.

data.engine.BadFetchScriptReturnType=La méthode de script de jeu de données "{0}" a renvoyé {1} ; une valeur booléenne est attendue.

data.engine.FailToExecuteScript=Echec de l'exécution du script dans la fonction {0}(). Source : \n------\n" + {1} + "\n-----

data.engine.cacheFunctionWrongMode=de création de pdf en cache ne fonctionne qu'en mode DIRECT_PRESENTATION.

data.engine.document.invalidIndexArray=Le tableau d'index n'est pas valide.

data.engine.document.invalidGroupItem=L'élément de groupe supprimé n'est pas correct.

data.engine.resultIteratorNotOpen=Result iterator n'est pas encore ouvert.

data.engine.resultIteratorClosed=Result iterator a été fermé.

data.engine.invalidOutputParameterIndex=Index de paramètre de sortie non valide : {0}.

data.engine.outputParameterOutOfBound=L'index de paramètre de sortie est hors limites : {0}.

data.engine.invalidOutputParameterName=Nom de paramètre de sortie non valide : {0}.

data.engine.document.notSupportInPresentation=Non pris en charge dans la présentation.

data.engine.wrongStatus=Etat incorrect.

data.engine.wrongVersion=Non pris en charge dans les versions antérieures à la version 2.1.

data.engine.unsupportOperationException=Opération non prise en charge.

data.engine.possibleMixedDataTypeInColumn=Une erreur s'est produite pendant la définition des données. Cela peut être dû à la présence de données mixtes dans une colonne de type « TOUT ».

data.engine.resultClassSaveError=Impossible de trouver les références des colonnes suivantes : {0}.

data.engine.invalidMemBufferSize=La taille du tampon de la mémoire "{0}" est incorrecte. La taille du tampon de la mémoire doit être un entier supérieur à zéro.

data.engine.document.error.cannotLoadStream=Le flux de document "{0}" est incorrect.

data.engine.failToCreateTempDir=Echec de la création du répertoire temporaire dans "{0}";

data.engine.reportItem.SubQuery.Unsupported=Le partage d'un élément d'état ne peut pas être utilisé pour une sous-requête.

data.engine.emptyResultSet=Le jeu de résultats est vide.

data.engine.errorHappenWhenRetrieveResultSet=Une erreur s'est produite lorsque vous avez consulté le jeu de résultats :"{0}". Il se peut que le jeu de résultats spécifié n'existe pas.

data.engine.invalidAggregationBindingForPLSReport=La liaison d'agrégation "{0}" est incorrecte lorsque la sécurité de niveau de page est activée.

data.engine.acl.dataSetCannotBeAccessed=Impossible d'accéder au jeu de données "{0}".

data.engine.acl.dataSetColumnCannotBeAccessed=Impossible d'accéder à la colonne d'ensemble de données "{0}".

data.engine.acl.cubeCannotBeAccessed=L'utilisateur ne dispose pas des autorisations d'accès à ce cube {0}.

data.engine.acl.cubeLevelCannotBeAccessed=L'utilisateur ne dispose pas des autorisations d'accès à ce niveau {0}.

data.engine.acl.cubeMeasureCannotBeAccessed=L'utilisateur ne dispose pas des autorisations d'accès à cette mesure {0}.

data.engine.acl.cubeDimensionCannotBeAccessed=L'utilisateur ne dispose pas des autorisations d'accès à cette dimension "{0}"
# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=Le nom de colonne ne peut être ni vide ni nul.

odaconsumer.ColumnPositionIsOneBased=La position de la colonne est basée sur 1 et ne peut pas être inférieure à 1.

odaconsumer.ColumnAliasCannotBeEmpty=L'alias de colonne ne peut pas être une chaîne vide.

odaconsumer.CannotProcessDriverConfig=Impossible de trouver ou de traiter la configuration de l'extension de la source de données du pilote {0}.

odaconsumer.OdaInitEntryNotFound=Impossible de détecter ou d''instancier le point d''entrée initial du pilote {0}.

odaconsumer.OdaDriverOnUnsupportedPlatform=Le pilote {0} ne prend pas en charge {1}.

odaconsumer.CannotGenerateUrl=Impossible de générer l''URL {0} du chargeur de classe.

odaconsumer.UnsupportedNativeType=Type natif {0} non pris en charge pour le type de jeu de données {1} du pilote {2}.

odaconsumer.UnsupportedDataSetType=Le type de jeu de données {0} n''est pas pris en charge par le pilote {1}.

odaconsumer.CannotDetermineDefaultDataSetType=Type de jeu de données nul. Impossible de déterminer le type de jeu de données par défaut pour le pilote {0}.

odaconsumer.InvalidDataSetType=Type de jeu de données incorrect {0}. Impossible de déterminer le type de jeu de données par défaut pour le jeu de données {1}.

odaconsumer.ParameterNameCannotBeEmptyOrNull=Le nom de paramètre ne peut être ni vide ni nul.

odaconsumer.ParameterPositionIsOneBased=La position du paramètre ne peut pas être inférieure à 1.

odaconsumer.CannotGetParameterNativeName=Impossible d''obtenir le nom défini par le pilote du paramètre : {0}.

odaconsumer.CannotGetParameterType=Impossible d''obtenir le type du paramètre : {0}.

odaconsumer.CannotGetParameterTypeName=Impossible d''obtenir le nom du type du paramètre : {0}.

odaconsumer.CannotGetParameterMode=Impossible d''obtenir le mode du paramètre : {0}.

odaconsumer.CannotGetParameterScale=Impossible d''obtenir l'échelle du paramètre : {0}.

odaconsumer.CannotGetParameterPrecision=Impossible d''obtenir la précision du paramètre : {0}.

odaconsumer.CannotGetParameterIsNullable=Impossible de déterminer le caractère de nullité du paramètre : {0}.

odaconsumer.SameParamNameForDifferentHints=Un autre paramètre du jeu de données est défini avec le même nom {0}.

odaconsumer.DifferentParamNameForSamePosition=Un autre paramètre {0} du jeu de données est défini avec la même position {1}.

odaconsumer.ParameterValueIsNull=La valeur du paramètre est nulle.

odaconsumer.UnsupportedParameterValueType=Type de valeur de paramètre non pris en charge : {0}.

odaconsumer.UnknownExceptionThrown=Exception inconnue basculée dans le dernier appel de la méthode de réglage.

odaconsumer.CannotConvertIndexedParameterValue=Impossible de convertir la valeur de paramètre {0} à l''index {1} à partir du type de valeur {2} vers le type ODA {3}.

odaconsumer.CannotConvertNamedParameterValue=Impossible de convertir la valeur de paramètre {0} avec le nom de paramètre {1} à partir du type de valeur {2} vers le type ODA {3}.

odaconsumer.UnrecognizedProjectedColumnName=Nom de colonne projetée non reconnu : {0}.

odaconsumer.ColumnNameOrAliasAlreadyUsed=Le nouveau nom ou alias de colonne {0} est déjà utilisé par la colonne à l'index {1}.

odaconsumer.NamedResultSetsUnsupported=Cette instruction ne prend pas en charge les jeux de résultats nommés.

odaconsumer.OutputParameterUnsupported=Cette instruction ne prend pas en charge les paramètres de sortie.

odaconsumer.UnsupportedMultipleResultSets=Cette instruction ne prend pas en charge les jeux de résultats multiples.

odaconsumer.CannotGetConnectionMetaData=Impossible d''obtenir les métadonnées de connexion.

odaconsumer.CannotGetMaxConnections=Impossible d''obtenir le nombre maximum de connexions pour la source de données.

odaconsumer.CannotGetMaxStatements=Impossible d''obtenir le nombre maximum de requêtes pour la source de données.

odaconsumer.CannotGetDataSetMetaData=Impossible d''obtenir les métadonnées de jeu de données {0} depuis la source de données.

odaconsumer.CannotPrepareStatement=Echec de préparation de la requête suivante pour le type de jeu de données ({1}). \n[{0}]

odaconsumer.CannotCloseConnection=Impossible de fermer la connexion.

odaconsumer.CannotOpenConnection=Impossible d''établir la connexion pour le pilote : {0}.

odaconsumer.CannotDetermineSupportForMultipleOpenResults=Impossible de déterminer la prise en charge de plusieurs résultats ouverts.

odaconsumer.CannotDetermineSupportForMultipleResultSets=Impossible de déterminer la prise en charge de plusieurs jeux de résultats.

odaconsumer.CannotDetermineSupportForNamedResultSets=Impossible de déterminer la prise en charge des jeux de résultats nommés.

odaconsumer.CannotDetermineSupportForNamedParameters=Impossible de déterminer la prise en charge des paramètres nommés.

odaconsumer.CannotDetermineSupportForInParameters=Impossible de déterminer la prise en charge des paramètres d'entrée.

odaconsumer.CannotDetermineSupportForOutParameters=Impossible de déterminer la prise en charge des paramètres de sortie.

odaconsumer.CannotSetStatementProperty=Impossible de définir la propriété d'instruction.

odaconsumer.CannotSetSortSpec=Impossible de définir la spécification de tri.

odaconsumer.CannotSetMaxRows=Impossible de définir le nombre maximal de lignes.

odaconsumer.CannotGetResultSetMetaData=Impossible d''obtenir les métadonnées du jeu de résultats.

odaconsumer.CannotGetMetaDataForNamedResultSet=Impossible d''obtenir les métadonnées du jeu de résultats nommé : {0}.

odaconsumer.CannotExecuteStatement=Impossible d'exécuter l'instruction.

odaconsumer.CannotGetResultSet=Impossible d''obtenir le jeu de résultats.

odaconsumer.CannotGetNamedResultSet=Impossible d''obtenir le jeu de résultats nommé : {0}.

odaconsumer.CannotGetMoreResults=Impossible d''obtenir plus de jeux de résultats à partir de l'instruction.

odaconsumer.CannotFindOutParameter=Le paramètre de sortie est introuvable : {0}.

odaconsumer.CannotCloseStatement=Impossible de terminer l'instruction.

odaconsumer.CannotCancelStatement=Impossible d'annuler l'instruction.

odaconsumer.CannotGetColumnCount=Impossible d''obtenir le nombre de colonnes.

odaconsumer.CannotGetColumnName=Impossible d''obtenir le nom de colonne : {0}.

odaconsumer.CannotGetColumnLabel=Impossible d''obtenir l'étiquette de colonne : {0}.

odaconsumer.CannotGetColumnType=Impossible d''obtenir le type de colonne : {0}.

odaconsumer.CannotFetchNextRow=Impossible de récupérer la ligne de données suivante.

odaconsumer.CannotDetermineWasNull=Impossible de déterminer si la dernière valeur récupérée était nulle.

odaconsumer.CannotGetIntFromColumn=Impossible d''obtenir la valeur d'entier depuis la colonne : {0}.

odaconsumer.CannotGetDoubleFromColumn=Impossible d''obtenir la valeur double depuis la colonne : {0}.

odaconsumer.CannotGetStringFromColumn=Impossible d''obtenir la valeur de chaîne depuis la colonne : {0}.

odaconsumer.CannotGetBigDecimalFromColumn=Impossible d''obtenir la valeur décimale depuis la colonne : {0}.

odaconsumer.CannotGetDateFromColumn=Impossible d''obtenir la valeur de date depuis la colonne : {0}.

odaconsumer.CannotGetTimeFromColumn=Impossible d''obtenir la valeur temporelle depuis la colonne : {0}.

odaconsumer.CannotGetTimestampFromColumn=Impossible d''obtenir la valeur d'horodatage depuis la colonne : {0}.

odaconsumer.CannotGetClobFromColumn=Impossible d''obtenir la valeur CLOB depuis la colonne : {0}.

odaconsumer.CannotGetBlobFromColumn=Impossible d''obtenir la valeur BLOB depuis la colonne : {0}.

odaconsumer.CannotGetBooleanFromColumn=Impossible d''obtenir la valeur booléenne depuis la colonne : {0}.

odaconsumer.CannotGetObjectFromColumn=Impossible d''obtenir la valeur de l''objet depuis la colonne : {0}.

odaconsumer.CannotGetRowPosition=Impossible d''obtenir la position de la ligne actuelle.

odaconsumer.CannotCloseResultSet=Impossible de fermer le jeu de résultats.

odaconsumer.CannotGetParameterCount=Impossible d''obtenir le nombre de paramètres.

odaconsumer.CannotGetIntFromParameter=Impossible d''obtenir la valeur d'entier depuis le paramètre : {0}.

odaconsumer.CannotGetDoubleFromParameter=Impossible d''obtenir la valeur double depuis le paramètre : {0}.

odaconsumer.CannotGetStringFromParameter=Impossible d''obtenir la valeur de chaîne depuis le paramètre : {0}.

odaconsumer.CannotGetBigDecimalFromParameter=Impossible d''obtenir la valeur décimale depuis le paramètre : {0}.

odaconsumer.CannotGetDateFromParameter=Impossible d''obtenir la valeur de date depuis le paramètre : {0}.

odaconsumer.CannotGetTimeFromParameter=Impossible d''obtenir la valeur temporelle depuis le paramètre : {0}.

odaconsumer.CannotGetTimestampFromParameter=Impossible d''obtenir la valeur d'horodatage depuis le paramètre : {0}.

odaconsumer.CannotGetBlobFromParameter=Impossible d''obtenir la valeur BLOB depuis le paramètre : {0}.

odaconsumer.CannotGetClobFromParameter=Impossible d''obtenir la valeur CLOB depuis le paramètre : {0}.

odaconsumer.CannotGetBooleanFromParameter=Impossible d''obtenir la valeur booléenne depuis le paramètre : {0}.

odaconsumer.CannotGetObjectFromParameter=Impossible d''obtenir la valeur de l''objet depuis le paramètre : {0}.

odaconsumer.CannotFindInParameter=Le paramètre d'entrée est introuvable : {0}.

odaconsumer.CannotSetIntParameter=Impossible de définir la valeur d'entier ({0}) du paramètre {1}.

odaconsumer.CannotSetDoubleParameter=Impossible de définir la valeur de double ({0}) du paramètre {1}.

odaconsumer.CannotSetStringParameter=Impossible de définir la valeur de chaîne ({0}) du paramètre {1}.

odaconsumer.CannotSetBigDecimalParameter=Impossible de définir la valeur décimale ({0}) du paramètre {1}.

odaconsumer.CannotSetDateParameter=Impossible de définir la valeur de date ({0}) du paramètre {1}.

odaconsumer.CannotSetTimeParameter=Impossible de définir la valeur temporelle ({0}) du paramètre {1}.

odaconsumer.CannotSetTimestampParameter=Impossible de définir la valeur d'horodatage ({0}) du paramètre {1}.

odaconsumer.CannotSetBooleanParameter=Impossible de définir la valeur booléenne ({0}) du paramètre {1}.

odaconsumer.CannotSetObjectParameter=Impossible de définir la valeur de l''objet ({0}) du paramètre {1}.

odaconsumer.CannotSetNullParameter=Impossible d''attribuer une valeur nulle au paramètre {0}.

odaconsumer.CannotClearInParameters=Impossible d''effacer les valeurs de paramètre d'entrée.

odaconsumer.UnrecognizedOdaType=Type de données ODA non reconnu : {0}

odaconsumer.CannotGetColumnNativeDataTypeName=Impossible d''obtenir le nom du type de données natives pour la colonne : {0}

odaconsumer.CannotGetParameterMetaData=Impossible d''obtenir les métadonnées du paramètre de pilote ODA.

odaconsumer.CannotLoadOdaAdapter=Impossible de charger le pilote {0} ; le plug-in de l''adaptateur DTP-to-BIRT ODA {1} est manquant.

odaconsumer.MustSpecifyColumnName=Un nom de colonne doit être spécifié lors de l'ajout d'une astuce de colonne.

odaconsumer.CannotFindLogDirectory=Le répertoire du journal est introuvable.

odaconsumer.ParameterNames=\nParamètre défini avec le nom de modèle ({0}) et avec le nom défini par le pilote ({1}).

odaconsumer.InvalidArgument=La méthode ({0}) a été appelée avec un argument non valide ({1}).

odaconsumer.CannotSetConnectionLocale=Impossible de définir l''endroit de la connexion ({0}).

odaconsumer.CannotCheckOpenConnection=Impossible de vérifier si la connexion est déjà établie.

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=Impossible d''obtenir les jeux de résultats par nom après l'appel de executeQuery().

odaconsumer.manager.CannotSetParamBeforePrepare=Impossible de définir les paramètres d'entrée avant que l'instruction n'ait été préparée.

odaconsumer.manager.CannotGetParamBeforeExecute=Impossible d''obtenir des paramètres de sortie avant l'exécution de l'instruction.

odaconsumer.manager.CannotGetParamMdBeforePrepare=Impossible d''obtenir les métadonnées du paramètre avant que l'instruction n'ait été préparée.

odaconsumer.manager.CannotSetSortSpecBeforePrepare=Impossible de définir la spécification de tri avant que l'instruction n'ait été préparée.

odaconsumer.manager.MaxConcurrentConnectionsReached=Nombre maximal de connexions actives simultanées atteint.

odaconsumer.manager.ConnectionFailed=Impossible d''ouvrir la connexion.

odaconsumer.manager.CannotConstructConnectionFactory=Impossible de créer la génération de connexion du pilote ODA.

odaconsumer.manager.InadequatePermissionsForCompatibility=Compatibilité descendante impossible en raison d'autorisations inappropriées.

odaconsumer.manager.ConnectionIsInactive=La connexion à la source de données n'est pas active.

odaconsumer.manager.MaxConcurrentStatementsReached=Nombre maximal d'instructions actives simultanées atteint pour cette connexion.

odaconsumer.manager.PrepareFailed=Echec de la préparation.

odaconsumer.manager.CannotGetResultSetMdBeforePrepare=Impossible d''obtenir les métadonnées du jeu de résultats avant que l'instruction n'ait été préparée.

odaconsumer.manager.CannotExecuteBeforePrepare=Impossible d''exécuter l'instruction avant qu'elle n'ait été préparée.


# Data Olap error code
data.olap.OlapDirCreateFail=Echec de la création du répertoire {0}.

data.olap.OlapFileCreateFail=Echec de la création du fichier {0}.

data.olap.OlapFileNotFound=Fichier OLAP {0} introuvable.

data.olap.OlapFileFormatInvalid=Le format de fichier n'est pas valide. Le nom de l'objet de données est : {0}.

data.olap.DimensionNotExist=La dimension {0} n''existe pas.

data.olap.KeyValueCanNotBeNull=La valeur de la colonne de clé ne peut pas être vide. Colonne de clé : {0}.

data.olap.DetailMemberHaveMultiParent=Le membre détaillé {0} possède plusieurs parents.

data.olap.OlapFileDataError=L''objet de données contient des données non valides {0}.

data.olap.DocumentObjectAlreadyExists =L''objet du document existe déjà. Echec de la création de l''objet de données : {0}.

data.olap.DocumentObjectNotExists =Echec du chargement du cube "{0}" à partir du document. L''objet du document n''existe pas.

data.olap.FacttableRowNotDistinct=Les lignes de la table des faits doivent être distinctes. "{0}" apparaît plusieurs fois.

data.olap.FacttableJointColumnNotExist=La colonne jointe "{0}" n''existe pas dans les dimensions.

data.olap.ParameterColumnOfAggregationNotExist=La colonne de paramètre "{0}" n''existe pas dans les dimensions.

data.olap.FacttableNullMeasureValue=Une des colonnes de mesures comporte une valeur vide. "{0}".

data.olap.InvalidDimensionPositionFacttableRow=La table des faits du cube "{0}" possède une ligne de données non valide dont la valeur de la clé de dimension "{1}" est "{2}", qui n'existe pas dans la dimension "{3}".

data.olap.measureNameNotFound=Le nom de mesure "{0}" est introuvable.

data.olap.levelNameNotFound=Le nom de niveau "{0}" est introuvable.

data.olap.dimensionNameNotFound=Impossible de trouver la dimension "{0}" dans la requête du cube. Cette dimension n'existe pas ou n'est pas valide.

data.olap.ReferencedDimensionNotFound=Définition de dimension référencée : {0} n'existe pas.

data.olap.ReferencedLevelNotFound=Le niveau référencé n'existe pas.

data.olap.cursorPositionError=Impossible de placer le curseur en position cible.

data.olap.cursorSeekError=Impossible de placer le curseur en position de recherche.

data.olap.CannotGetMeasureValue=Impossible de trouver la définition de mesure "{0}" dans le cube, la mesure n''existe pas ou est incorrecte.

data.olap.CannotFindLevel=Impossible de trouver le niveau de définition "{0}" dans la dimension "{1}".

data.olap.tryToAddIdenticalAggrGroup=La valeur "{0}" existe déjà dans l''agrégat sur la liste de la liaison "{1}" ;

data.olap.NoEdgeDefinitionFound=Echec de l'exécution de la requête car elle ne comporte aucun bord de ligne ni de colonne. Au moins un bord doit être défini.

data.olap.invalidSortDefinition=La définition de tri est incorrecte.

data.olap.invalidMeasureRef="{0}" n''est pas une expression de référence de mesure valide.

data.olap.invalidLevelAttr=L'attribut est incorrect :

data.olap.referencedBindingNotExist=La liaison référencée "{0}" n'existe pas.

data.olap.invalidBindingReferToInexitDim=La liaison "{0}" fait référence à des niveaux incorrects.

data.olap.invalidBindingReferToInexitMeasure=La liaison "{0}" fait référence à des mesures incorrectes.

data.olap.invalidBindingMissingAggrFunc=La fonction d''agrégation de la liaison "{0}" est manquante.

data.olap.invalidAggrArgument=Seul l'attribut de niveau peut servir d'argument d'agrégation.

data.olap.axis.level.CantBeNull=Le niveau de membre de colonne ne peut pas être nul.

data.olap.axis.value.CantBeNull=La valeur de membre de colonne du niveau \"{0}\" ne peut pas être nulle.

data.olap.fetchlimitWarning=La taille d''extraction est {0}, elle est donc inférieure à la longueur totale du jeu de résultats[{1}].

data.olap.failComputeComputedMeasureValue=Echec du calcul de la valeur pour la mesure calculée.

data.olap.duplicateMeasureName=Nom de mesure \"{0}\" en double.

data.olap.dataBindingExpressionEmpty=L'expression de liaison de données ne peut être ni nulle ni vide.

data.olap.missingParentCursor=L'ensemble des résultats parents n'existe pas.

data.olap.CannotAccessNullDimensionRow=Impossible d''accéder à une ligne de dimension nulle !

data.olap.NonexistentDimension=Dimension non existante :

data.olap.NonexistentLevel=Niveau non existant :

data.olap.NonexistentKeyOrAttr=Clé ou attribut non existant :

data.olap.UnsupportedFunction=Fonction non prise en charge :

data.olap.UnspecifiedBindingName=Le nom de liaison n'est pas spécifié.

data.olap.NotNestAggregationBinding=Aucune liaison d'agrégation imbriquée du cube : {0}

data.olap.InvalidNestAggregationExpression=L'expression d'agrégation imbriquée du cube est incorrecte.

data.olap.InvalidNestAggregationOn=Echec du calcul de l'agrégation imbriquée "{0}" à cause de l'agrégation de niveaux inexistants dans son ou ses agrégation(s) de base.

data.olap.InvalidAggregationFilterExpr=L'expression de filtre "{0}" n'est pas valide. La liste d'agrégationOn de liaison d'agrégation "{1}" ne contient pas le niveau de dimension référencé "{2}".

data.olap.sort.exprError=Le jeu de niveaux de dimension référencés ne doit contenir qu'un seul niveau.

data.olap.query.noCubeBinding=Impossible d'exécuter la requête du cube car aucune liaison au cube n'est disponible.

data.olap.FailLoadCube=Echec du chargement du cube :"{0}".

data.olap.MissingDimensionInCube=Le cube doit contenir au moins une dimension : "{0}"

data.olap.ExceedIntegerLimit= La longueur finale définie est supérieure à la valeur d'Integer.MAX.

data.olap.ExceedColumnLimit= La longueur finale définie pour le bord de la colonne dépasse la limite de taille autorisée {0}.

data.olap.ExceedRowLimit= La longueur finale définie pour le bord de la ligne dépasse la limite de taille autorisée {0}.

data.cache.UnsupportedIncrementalCacheMode=Le mode \"{0}\" de cache incrémentiel n''est pas encore pris en charge !
# Configuration file parser error code
data.executor.configFileParser.getModeByID=Impossible d''obtenir le mode de cache incrémentiel via le nom du jeu de données.

data.executor.configFileParser.getTSFormatByID=Impossible d''analyser le format d'horodatage à l'aide du nom du jeu de données spécifié dans le fichier de configuration.

data.executor.configFileParser.getTimeStampColumnByID=Impossible d''analyser le format d'horodatage à l'aide du nom du jeu de données spécifié dans le fichier de configuration.

data.executor.configFileParser.getQueryTextByID=Impossible d''obtenir le texte de la requête à l'aide du nom du jeu de données spécifié dans le fichier de configuration.

data.executor.configFileParser.getParametersByID=Impossible d''obtenir les paramètres à l'aide du nom du jeu de données spécifié dans le fichier de configuration.

data.aggregation.expression.displayName=Expression

data.aggregation.argumentError=L'expression de l'argument \"{0}\" de l'agrégation \"{1}\" est nulle ou vide.

data.olap.InvalidAggrTypeOnMeasure=L''agrégation sur la mesure de cube ne peut pas être du type : "{0}".

#derived dataSet
data.derivedDataSet.columnNotExist=La colonne "{0}" n'existe pas !

data.derivedDataSet.UnionQueryTextParseError=Impossible d'analyser la requête Union. \"{0}\"

data.derivedDataSet.JoinQueryTextParseError=Impossible d'analyser la requête Join. \"{0}\"

data.derivedDataSet.illegalClassInstance=Initialisation d'une instance de classe incorrecte.

data.derivedDataSet.classLoadError=Echec du chargement de l'instance de classe.

data.derivedDataSet.calculatorNotExist=Le calcul n'est pas défini ou est introuvable.

data.derivedDataSet.invalidQueryText=Texte de la requête incorrect.

#data mart data source
data.datamart.failLoadDataSet = Impossible de charger le jeu de données de l'Objet de données "{0}".

data.datamart.originalDataSetNotExist = Echec de la mise à jour incrémentielle. Le jeu de données d'origine "{0}" n'existe pas.

data.datamart.incrementalDatasetMetaDataError = Echec de la mise à jour incrémentielle. Les métadonnées du jeu de données supplémentaire "{0}" sont différentes de celles du jeu de données d'origine.

data.datamart.collation=Dans le produit précédent, le classement n'est pas utilisé dans l'index de l'objet de données, même si cette option a été définie. Nous prenons désormais en charge le classement, vous devez donc générer à nouveau l'objet de données lorsque le classement est défini dans le jeu de données.

data.filter.pushDownFail= Impossible d'aligner le filtre pris en charge uniquement par un côté de l'extension ODA.

data.query.exprReferInvalidColumn=Impossible d'exécuter la requête car : la requête contient une expression non valide qui fait référence à une colonne de données incorrecte "{0}".

data.query.invalidColumnBinding=La liaison de colonnes "{0}" est incorrecte.