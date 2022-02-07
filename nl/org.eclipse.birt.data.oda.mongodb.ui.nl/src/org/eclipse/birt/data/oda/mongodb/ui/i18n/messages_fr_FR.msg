##************************************************************************
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#  
#*************************************************************************
#
MongoDBDataSourceWizardPage.message.default=Propriétés de connexion de la source de la base de données Mongo

MongoDBDataSourceWizardPage.GroupTitle.URIElements=Eléments Uniform Resource Identifier de la base de données Mongo

MongoDBDataSourceWizardPage.text.label.ServerHost=&Hôte du serveur :

MongoDBDataSourceWizardPage.text.label.ServerPort=&Port du serveur :

MongoDBDataSourceWizardPage.text.label.DatabaseName=Nom de la base de don&nées :

MongoDBDataSourceWizardPage.text.label.UserName=Nom d'utili&sateur :

MongoDBDataSourceWizardPage.text.label.Password=&Mot de passe :

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=URI de la base de données &Mongo :

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=Paramètres supplémentaires et paramètres du &client

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=Garder l'interface de connexion &active

MongoDBDataSourceWizardPage.checkbox.label.RequestSession=Session de &requête consistante

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=Propriété requise manquante pour la connexion !

#
MongoDBDataSetWizardPage.label.DBName=Base de données Mon&go :

MongoDBDataSetWizardPage.label.DBCollection=&Collection MongoDB

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=&Inclure la collection du système

MongoDBDataSetWizardPage.Button.Refresh=&Actualiser

MongoDBDataSetWizardPage.label.DocumentNumber=No&mbre de documents à chercher pour les champs :

MongoDBDataSetWizardPage.group.text=&Opération de commande

MongoDBDataSetWizardPage.label.OperationType=&Type de commande :

MongoDBDataSetWizardPage.Button.FindFields=Trouver &des champs

MongoDBDataSetWizardPage.Button.text.CommandExpression=&Expression...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=Modifier l'expression de la commande.

MongoDBDataSetWizardPage.Button.QueryExpression=Expressi&on de re$quête...

MongoDBDataSetWizardPage.Button.SortExpression=Expre&ssion de tri...

MongoDBDataSetWizardPage.Button.AdvancedSettings=Paramètres a&vancés...

MongoDBDataSetWizardPage.label.AvailableFields=&Champs disponibles :

MongoDBDataSetWizardPage.label.SelectedFields=&Champs sélectionnés :

MongoDBDataSetWizardPage.HeaderText.SelectedFields=Nom d'affichage du champ

MongoDBDataSetWizardPage.menuItem.remove=Suppri&mer

MongoDBDataSetWizardPage.menuItem.removeAll=Supprimer tou&t

MongoDBDataSetWizardPage.message.default=Sélectionnez les champs désirés pour les colonnes de jeu de données

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=Confirmez pour continuer

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=Si vous sélectionnez une autre collection, les champs actuellement sélectionnés seront supprimés.  Voulez-vous continuer ?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=Confirmez pour actualiser

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=Voulez-vous actualiser les champs disponibles selon l'expression modifiée de la commande ?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=Erreur

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=Impossible d'obtenir la liste de collections à partir de la source de données MongoDB.

MongoDBDataSetWizardPage.ExceptionDialog.title=Erreur

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=Impossible de trouver les champs disponibles.

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=Aucune connexion à une source de données active.

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=Une erreur s'est produite lors de la mise à jour du jeu de données MongoDB.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=Expression de la commande non valide.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=Expression de la requête non valide.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=Expression de tri non valide.

MongoDBDataSetWizardPage.message.error.MissingCollectionName=Le nom de la collection doit être renseigné.

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=Saisissez un nombre entier pour le nombre de documents afin de trouver les champs

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=Le champ sélectionné "{0}" n'existe pas.

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=L'expression de la commande ne doit pas être vide.

MongoDBDataSetWizardPage.message.error.MissingSelectedField=Sélectionnez au moins un champ.

MongoDBAdvancedSettingsDialog.dialogTitle=Propriétés d'exécution avancées du jeu de données

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=Nombre maximum d'objets de résultat par &lot :

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=Nombre maximum d'objets de résultat extraits par lot\n par le curseur MongoDB.  Valeur par défaut = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=Nombre maximum de documents à chercher pour les métado&nnées :

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=Nombre de documents à traiter pour obtenir les\n métadonnées du jeu de résultats de requête à l'exécution.  Valeur par défaut = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=Nombre maximum de documents à i&gnorer :

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=Ignorer les n premiers documents du jeu de résultats ; type de données entier. \nValeur par défaut valeur = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=Expression d'astu&ce d'index :

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=L'une des clés d'index que la requête doit utiliser pour l'optimisation de la requête.\n Utilisez la syntaxe d'expression d'astuce JSON Mongo pour lister les clés indexées.

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=Préférences de lecture pour le curseur de requête

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=Mo&de :

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=Mode de préférence de lecture d'une opération de curseur de requête.

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=Je&u(x) de balises :

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=Jeu(x) de balises requises configurées pour un nœud membre.\n D'autres jeux de balises peuvent être spécifiés en tant que documents dans une matrice.

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=Aucu&ne expiration de délai pour le curseur

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=Le délai d'inactivité des curseurs du serveur expire après une période d'inactivité (10 minutes) pour éviter une utilisation excessive de la mémoire.\n L'activation de cette option vous permet d'éviter une utilisation excessive de la mémoire.

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=Autorise les résultats &partiels

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=Utilisée avec le partitionnement (mongos), cette option autorise des résultats partiels à partir d'un système partitionné, si des partitions\n ont disparu ou ont été supprimées du groupe.  Si cette option n'est pas activée, le serveur mongos renverra un message d'erreur.

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=&Aplatir les collections imbriquées

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=Cette option utilisateur permet d'aplatir une collection imbriquée de champs en plusieurs lignes de jeu de données.

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=Nécessite un nombre entier pour "Nombre maximum d'objets de résultats".

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=Nécessite un nombre entier pour "Nombre maximum de documents à chercher".

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=Nécessite un nombre entier pour "Nombre maximum de documents à ignorer".

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=Réinitialiser le paramètre de modèle de création

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=OK

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=La valeur de l'heure de conception du champ "Nombre de documents à chercher pour les champs" est réinitialisée pour correspondre à la valeur d'heure d'exécution du champ "Nombre maximum de documents à chercher pour les métadonnées".

MongoDBExpressionBuilder.dialogTitle.default=Générateur d'expression

MongoDBExpressionBuilder.ExprssionText.label=Texte de l'e&xpression :

MongoDBExpressionBuilder.Button.import=I&mportation...

MongoDBExpressionBuilder.Button.tooltip.import=Importer une expression à partir d'un fichier local

MongoDBExpressionBuilder.Button.export=&Exportation...

MongoDBExpressionBuilder.Button.tooltip.export=Exporter l'expression vers un fichier local

MongoDBExpressionBuilder.Button.ValidateSyntax=&Valider la syntaxe

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=Valider la syntaxe du texte de l'expression.

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=Validation du texte de l'expression.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=Syntaxe de l'expression valide.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=Syntaxe de l'expression non valide.

MongoDBExpressionBuilder.CommandExpression.DialogTitle=Générateur d'expression de commande

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=Le texte de l'expression ne doit pas être vide.

MongoDBExpressionBuilder.QueryExpression.DialogTitle=Générateur d'expression de requête

MongoDBExpressionBuilder.SortExpression.DialogTitle=Générateur d'expression de tri





