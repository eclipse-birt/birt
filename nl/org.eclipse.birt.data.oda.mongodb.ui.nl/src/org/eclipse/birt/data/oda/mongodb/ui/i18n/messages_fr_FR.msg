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
MongoDBDataSourceWizardPage.message.default=Propri�t�s de connexion de la source de la base de donn�es Mongo

MongoDBDataSourceWizardPage.GroupTitle.URIElements=El�ments Uniform Resource Identifier de la base de donn�es Mongo

MongoDBDataSourceWizardPage.text.label.ServerHost=&H�te du serveur :

MongoDBDataSourceWizardPage.text.label.ServerPort=&Port du serveur :

MongoDBDataSourceWizardPage.text.label.DatabaseName=Nom de la base de don&n�es :

MongoDBDataSourceWizardPage.text.label.UserName=Nom d'utili&sateur :

MongoDBDataSourceWizardPage.text.label.Password=&Mot de passe :

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=URI de la base de donn�es &Mongo :

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=Param�tres suppl�mentaires et param�tres du &client

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=Garder l'interface de connexion &active

MongoDBDataSourceWizardPage.checkbox.label.RequestSession=Session de &requ�te consistante

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=Propri�t� requise manquante pour la connexion !

#
MongoDBDataSetWizardPage.label.DBName=Base de donn�es Mon&go :

MongoDBDataSetWizardPage.label.DBCollection=&Collection MongoDB

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=&Inclure la collection du syst�me

MongoDBDataSetWizardPage.Button.Refresh=&Actualiser

MongoDBDataSetWizardPage.label.DocumentNumber=No&mbre de documents � chercher pour les champs :

MongoDBDataSetWizardPage.group.text=&Op�ration de commande

MongoDBDataSetWizardPage.label.OperationType=&Type de commande :

MongoDBDataSetWizardPage.Button.FindFields=Trouver &des champs

MongoDBDataSetWizardPage.Button.text.CommandExpression=&Expression...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=Modifier l'expression de la commande.

MongoDBDataSetWizardPage.Button.QueryExpression=Expressi&on de re$qu�te...

MongoDBDataSetWizardPage.Button.SortExpression=Expre&ssion de tri...

MongoDBDataSetWizardPage.Button.AdvancedSettings=Param�tres a&vanc�s...

MongoDBDataSetWizardPage.label.AvailableFields=&Champs disponibles :

MongoDBDataSetWizardPage.label.SelectedFields=&Champs s�lectionn�s :

MongoDBDataSetWizardPage.HeaderText.SelectedFields=Nom d'affichage du champ

MongoDBDataSetWizardPage.menuItem.remove=Suppri&mer

MongoDBDataSetWizardPage.menuItem.removeAll=Supprimer tou&t

MongoDBDataSetWizardPage.message.default=S�lectionnez les champs d�sir�s pour les colonnes de jeu de donn�es

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=Confirmez pour continuer

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=Si vous s�lectionnez une autre collection, les champs actuellement s�lectionn�s seront supprim�s.  Voulez-vous continuer ?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=Confirmez pour actualiser

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=Voulez-vous actualiser les champs disponibles selon l'expression modifi�e de la commande ?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=Erreur

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=Impossible d'obtenir la liste de collections � partir de la source de donn�es MongoDB.

MongoDBDataSetWizardPage.ExceptionDialog.title=Erreur

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=Impossible de trouver les champs disponibles.

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=Aucune connexion � une source de donn�es active.

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=Une erreur s'est produite lors de la mise � jour du jeu de donn�es MongoDB.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=Expression de la commande non valide.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=Expression de la requ�te non valide.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=Expression de tri non valide.

MongoDBDataSetWizardPage.message.error.MissingCollectionName=Le nom de la collection doit �tre renseign�.

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=Saisissez un nombre entier pour le nombre de documents afin de trouver les champs

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=Le champ s�lectionn� "{0}" n'existe pas.

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=L'expression de la commande ne doit pas �tre vide.

MongoDBDataSetWizardPage.message.error.MissingSelectedField=S�lectionnez au moins un champ.

MongoDBAdvancedSettingsDialog.dialogTitle=Propri�t�s d'ex�cution avanc�es du jeu de donn�es

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=Nombre maximum d'objets de r�sultat par &lot :

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=Nombre maximum d'objets de r�sultat extraits par lot\n par le curseur MongoDB.  Valeur par d�faut = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=Nombre maximum de documents � chercher pour les m�tado&nn�es :

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=Nombre de documents � traiter pour obtenir les\n m�tadonn�es du jeu de r�sultats de requ�te � l'ex�cution.  Valeur par d�faut = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=Nombre maximum de documents � i&gnorer :

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=Ignorer les n premiers documents du jeu de r�sultats ; type de donn�es entier. \nValeur par d�faut valeur = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=Expression d'astu&ce d'index :

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=L'une des cl�s d'index que la requ�te doit utiliser pour l'optimisation de la requ�te.\n Utilisez la syntaxe d'expression d'astuce JSON Mongo pour lister les cl�s index�es.

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=Pr�f�rences de lecture pour le curseur de requ�te

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=Mo&de :

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=Mode de pr�f�rence de lecture d'une op�ration de curseur de requ�te.

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=Je&u(x) de balises :

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=Jeu(x) de balises requises configur�es pour un n�ud membre.\n D'autres jeux de balises peuvent �tre sp�cifi�s en tant que documents dans une matrice.

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=Aucu&ne expiration de d�lai pour le curseur

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=Le d�lai d'inactivit� des curseurs du serveur expire apr�s une p�riode d'inactivit� (10 minutes) pour �viter une utilisation excessive de la m�moire.\n L'activation de cette option vous permet d'�viter une utilisation excessive de la m�moire.

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=Autorise les r�sultats &partiels

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=Utilis�e avec le partitionnement (mongos), cette option autorise des r�sultats partiels � partir d'un syst�me partitionn�, si des partitions\n ont disparu ou ont �t� supprim�es du groupe.  Si cette option n'est pas activ�e, le serveur mongos renverra un message d'erreur.

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=&Aplatir les collections imbriqu�es

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=Cette option utilisateur permet d'aplatir une collection imbriqu�e de champs en plusieurs lignes de jeu de donn�es.

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=N�cessite un nombre entier pour "Nombre maximum d'objets de r�sultats".

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=N�cessite un nombre entier pour "Nombre maximum de documents � chercher".

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=N�cessite un nombre entier pour "Nombre maximum de documents � ignorer".

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=R�initialiser le param�tre de mod�le de cr�ation

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=OK

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=La valeur de l'heure de conception du champ "Nombre de documents � chercher pour les champs" est r�initialis�e pour correspondre � la valeur d'heure d'ex�cution du champ "Nombre maximum de documents � chercher pour les m�tadonn�es".

MongoDBExpressionBuilder.dialogTitle.default=G�n�rateur d'expression

MongoDBExpressionBuilder.ExprssionText.label=Texte de l'e&xpression :

MongoDBExpressionBuilder.Button.import=I&mportation...

MongoDBExpressionBuilder.Button.tooltip.import=Importer une expression � partir d'un fichier local

MongoDBExpressionBuilder.Button.export=&Exportation...

MongoDBExpressionBuilder.Button.tooltip.export=Exporter l'expression vers un fichier local

MongoDBExpressionBuilder.Button.ValidateSyntax=&Valider la syntaxe

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=Valider la syntaxe du texte de l'expression.

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=Validation du texte de l'expression.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=Syntaxe de l'expression valide.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=Syntaxe de l'expression non valide.

MongoDBExpressionBuilder.CommandExpression.DialogTitle=G�n�rateur d'expression de commande

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=Le texte de l'expression ne doit pas �tre vide.

MongoDBExpressionBuilder.QueryExpression.DialogTitle=G�n�rateur d'expression de requ�te

MongoDBExpressionBuilder.SortExpression.DialogTitle=G�n�rateur d'expression de tri





