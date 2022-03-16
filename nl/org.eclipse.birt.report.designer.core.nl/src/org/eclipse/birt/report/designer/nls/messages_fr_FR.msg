#/*******************************************************************************
# * Copyright (c) 2006, 2012 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v2.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-2.0.html
# *
# * Contributors:
# *  Actuate Corporation - initial API and implementation
# *******************************************************************************/

#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

###########################################################
#AbstractDataSetWizard properties
AbstractDataSetWizard.ModelTrans.Create=Définir le type, la source et le nom du jeu de données
###########################################################
#CssErrDialog
CssErrDialog.shellTitle.ImportCssStyleMssageTitle=Message d'importation

CssErrDialog.AreaTitle=Message d'importation des styles CSS

CssErrDialog.AreaMessage=Des erreurs ou des avertissements sont apparus lors de l'importation des styles css.

CssErrDialog.Severity=Gravité :

CssErrDialog.Message=Message :

CssErrDialog.FatalError=Erreur fatale

CssErrDialog.Error=Erreur

CssErrDialog.Warning=Avertissement

###########################################################
#AddGroupAction properties
AddGroupAction.actionMsg.addGroup=Insérer le groupe...

AddGroupAction.Menu.Text=Insérer le groupe...

AddGroupAction.stackMsg.addGroup=Insérer le groupe

###########################################################
#AddLibraryAction properties
AddLibraryAction.Error.FileNotFound=Fichier ''{0}'' introuvable.

AddLibraryAction.Error.FileIsNotLibrary=Le fichier ''{0}'' ne se termine pas par ''{1}''.

AddLibraryAction.Error.Message=Ce fichier existe déjà.

AddLibraryAction.Error.Title=Erreur

AddLibraryAction.Text=Ajouter une bibliothèque...

AddLibraryAction.Dialog.Tilte=Sélectionnez une bibliothèque

###########################################################
#AddCSSAction properties
AddCSSAction.Text=Ajouter un fichier CSS...

###########################################################
#AddResourceAction properties
AddResourceAction.Text=Ajouter une &ressource...

AddResourceAction.Dialog.Tilte=Sélectionner une ressource

AddResourceAction.Error.Message=Ce fichier existe déjà.

AddResourceAction.Error.FileNotFound=Fichier ''{0}'' introuvable.

AddResourceAction.Error.Title=Erreur

AddResourceAction.Error.FileIsNotResource=Le fichier "{0}" n'est pas une ressource.

###########################################################
#AddCSSDialog
AddCSSWizardDialog.ShellText=Ajouter un fichier CSS

AddCSSWizardDialog.TitleArea=Ajouter un fichier CSS au dossier de ressources

AddCSSWizardDialog.Message=Sélectionner un fichier CSS pour importer des styles dans le dossier de ressources

###########################################################
#MoveResourceDialog
MoveResourceDialog.Title=Déplacer une ressource

MoveResourceDialog.Message=Sélectionner un dossier

###########################################################
#NewLibraryDialog
NewLibraryDialog.Title=Nouvelle bibliothèque

NewLibraryDialog.Message=Sélectionner un dossier

NewLibraryDialog.label.NewLibrary=Nom de la nouvelle bibliothèque

###########################################################
#PublishCSSAction properties
PublishCSSAction.wizard.errorTitle=Erreur

PublishCSSAction.wizard.message=Impossible de publier le fichier CSS dans son propre répertoire ; sélectionnez un autre répertoire pour procéder à la publication.

PublishCSSAction.wizard.msgDirErr=Le répertoire cible n'existe pas, mais ne peut pas être créé.

PublishCSSAction.wizard.message.SourceFileNotExist=Le fichier source n'existe pas.

PublishCSSAction.wizard.notvalidfolder=Le dossier de ressources défini dans les préférences n'est pas valide.


###########################################################
#ResourceAction properties
DeleteLibraryAction.Text=&Supprimer

CopyLibraryAction.Text=&Copier

PasteLibraryAction.Text=C&oller

MoveLibraryAction.Text=Dép&lacer...

RenameLibraryAction.Text=Reno&mmer

NewFolderAction.Text=&Dossier

NewLibraryAction.Text=&Bibliothèque

NewResource.MenuGroup.Text=&Nouveau

RenameResourceAction.nameExists=Il existe déjà un fichier ou un dossier portant ce nom.

RenameResourceAction.inputDialogTitle=Renommer la ressource

RenameResourceAction.inputDialogMessage=Nouveau nom :

NewFolderAction.nameExists=Il existe déjà un dossier portant ce nom.

NewFolderAction.emptyName=Le nom doit être renseigné.

NewFolderAction.inputDialogTitle=Nouveau dossier

NewFolderAction.inputDialogMessage=Nom du dossier :

DeleteResourceAction.Dialog.Title=Supprimer une ressource

DeleteResourceAction.Dialog.Message=Voulez-vous vraiment supprimer la ressource sélectionnée ?

PasteResourceAction.Dialog.Title=Coller une ressource

PasteResourceAction.ReadOnlyEncounter.Title=Fichier en lecture seule

PasteResourceAction.ReadOnlyEncounter.Message=Le fichier ''{0}'' est en lecture seule, vous devez d''abord disposer des droits en écriture.

PasteResourceAction.Dialog.Message=La ressource existe déjà. Spécifiez un &nouveau nom :

PasteResourceAction.Dialog.FilenameSuffix.CopyOf=Copie de

MoveResourceAction.Dialog.Title=Déplacer une ressource

MoveResourceAction.Dialog.Message=La ressource existe déjà. Voulez-vous la remplacer ?

ResourceAction.ConvertToFile.URLIsNull=L'URL est nulle et ne peut pas être convertie en fichier.

ResourceAction.FileRemoveFailure=Echec de suppression des fichiers.

LibraryNotExist.Dialog.Title=Ouvrir la bibliothèque

LibraryNotExist.Dialog.Message=La bibliothèque n'existe pas. Voulez-vous actualiser l'Explorateur de ressources?

DesignerNotExist.Dialog.Title=Ouvrir un modèle d'état

DesignerNotExist.Dialog.Message=Le modèle d'état n'existe pas. Voulez-vous actualiser l'Explorateur de ressources ?
###########################################################
#LibraryFileFilterAction properties
LibraryFileFilterAction.Text=Filtre...

###########################################################
#AddStyleRuleAction properties
AddStyleAction.actionMsg.addStyleRule=&Nouveau style...

AddStyleAction.stackMsg.addStyle=&Nouveau style

###########################################################
#ApplyStyleAction properties
ApplyStyleAction.actionLabel.none=Aucun

ApplyStyleAction.stackMsg.applyStyle=Appliquer un style

###########################################################
#ApplyThemeAction properties
ApplyThemeAction.actionLabel.none=Aucun

ApplyThemeAction.stackMsg.applyStyle=Appliquer le thème

###########################################################
#AttributesBuilder properties
AttributesBuilder.Label.Cell=Editeur de propriétés - Cellule

AttributesBuilder.Label.Column=Editeur de propriétés - Colonne

AttributesBuilder.Label.Data=Editeur de propriétés - Données

AttributesBuilder.Label.Generic=Editeur de propriétés - {0}

AttributesBuilder.Label.Grid=Editeur de propriétés - Grille

AttributesBuilder.Label.Label=Editeur de propriétés - Etiquette

AttributesBuilder.Label.Library=Editeur de propriétés - Bibliothèque

AttributesBuilder.Label.List=Editeur de propriétés - Liste

AttributesBuilder.Label.Cube=Editeur de propriétés - Cube

AttributesBuilder.Label.DynamicFilterParameter=Editeur de propriétés - Paramètre de filtre dynamique

AttributesBuilder.Label.MasterPage=Editeur de propriétés - Page maître

AttributesBuilder.Label.None=Editeur de propriétés - Aucun

AttributesBuilder.Label.Picture=Editeur de propriétés - Image

AttributesBuilder.Label.Report=Editeur de propriétés - Etat

AttributesBuilder.Label.Row=Editeur de propriétés - Ligne

AttributesBuilder.Label.Table=Editeur de propriétés - Table

AttributesBuilder.Label.Text=Editeur de propriétés - Texte

AttributesBuilder.Label.TextData=Editeur de propriétés - Texte dynamique

AttributesBuilder.Label.DataSourceHandle=Editeur de propriétés - Source de données

AttributesBuilder.Label.VariableElementHandle=Editeur de propriétés - Variable

AttributesBuilder.Label.DataSetHandle=Editeur de propriétés - Jeu de données

AttributesBuilder.Label.ScalarParameterHandle=Editeur de propriétés - Paramètre

AttributesBuilder.Label.TemplateReportItemHandle=Editeur de propriétés - Elément d'état de modèle

AttributesBuilder.Label.AutoText=Editeur de propriétés - AutoText

AttributesBuilder.Label.ParameterGroup=Editeur de propriétés - Groupe de paramètres

AttributesBuilder.Label.CascadingParameterGroup=Editeur de propriétés - Groupe de paramètres en cascade

###########################################################
#AttributeView properties
AttributeView.dialg.Message.Warning=Avertissement

AttributeView.dialg.Message.Yes=&Oui

AttributeView.dialg.Message.No=&Non

AttributeView.dialg.Message.Cancel=&Annuler

AttributeView.dialg.Message.PromptMsg=Voulez-vous vraiment supprimer toutes les propriétés locales ?

AttributeView.toolbar.tooltip.EditingToggleAction.ClearMsg=Effacer les propriétés locales.

AttributeView.toolbar.tooltip.EditingToggleAction.EnableEditingMsg=Activer la modification des propriétés.

AttributeView.toolbar.tooltip.EditingToggleAction.DisabledActionMsg=Cette action n'est pas disponible pour les éléments locaux.

AttributeView.view.message.Emptypage= {0} s''étend à partir de {1} dans la bibliothèque {2}. Pour modifier les propriétés de {3} dans ce modèle d''état, cliquez sur le bouton Activer dans la barre d''outils de l''Editeur de propriétés.

AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg=Restaurer les propriétés de l'élément d'état d'origine

AttributeView.defaultText.noAttributeView = L'éditeur de propriétés n'est pas disponible.
###########################################################
#BackGroundPreferencePage properties
BackGroundPreferencePage.displayname.Title=Arrière-plan

###########################################################
#BasAttributePage properties
BasAttributePage.Label.Attributes=Propriétés :

BaseAttributePage.CommandStack.ResetStyle.Message=Restaurer le style par défaut

###########################################################
#BaseInsertMenuAction properties
BaseInsertMenuAction.stackMsg.insertElement=Insérer un élément



###########################################################
#BasePaletteFactory properties
BasePaletteFactory.AutoText.Confidential=Confidentiel

BasePaletteFactory.AutoTextLabel.CreatedBy=Création par :

BasePaletteFactory.AutoTextLabel.CreatedOn=Création le :

BasePaletteFactory.AutoTextLabel.Filename=Nom de fichier :

BasePaletteFactory.AutoTextLabel.LastPrinted=Dernière impression :

BasePaletteFactory.AutoTextLabel.Page=Page :

BasePaletteFactory.AutoTextLabel.PageCount=Nombre de pages :

BasePaletteFactory.AutoTextLabel.PageXofY=Page X sur Y

BasePaletteFactory.categoryName.PointerSelect=Sélection avec le pointeur

BasePaletteFactory.categoryName.RectangleSelect=Sélection avec le rectangle

BasePaletteFactory.Group.Items=Eléments

BasePaletteFactory.toolTip.PointerSelect=Cliquez sur un élément pour le sélectionner. Pour une sélection multiple, cliquez en maintenant la touche Ctrl enfoncée.

BasePaletteFactory.toolTip.RectangleSelect=Cliquez et faites glisser le pointeur pour sélectionner plusieurs éléments contigus.

###########################################################
#BaseStylePreferencePage properties
BaseStylePreferencePage.displayname.Title=Général

###########################################################
#BgImageFieldEditor properties
BgImageFieldEditor.displayname.Browse=Par&courir...

###########################################################
#BindingPage properties
BindingPage.Dataset.Label=Je&u de données :

BindingPage.DataSetBindingSelector.Title.DataSet=Liaison de jeu de données

BindingPage.DataSetBindingSelector.Title.LinkModel=Liaison de modèle de données

BindingPage.ReportItem.Label=Elémen&t d'état :

BindingPage.ReportItem.NoName=Pas de nom

BindingPage.Label.Binding=Liaison

BindingPage.MessageDlg.SaveParamBinding=Enregistrer ParamBinding

BindingPage.None=Aucun

BindingPage.TableColumn.DataType=Type de données

BindingPage.TableColumn.Parameter=Paramètre

BindingPage.TableColumn.Value=Valeur

ColumnBingingDialog.Label.Warn=La modification du jeu de données ou de la référence de l'élément d'état risque d'invalider toutes les expressions de colonne de jeu de résultats.

ColumnBindingDialog.Label.DataSet=Jeu de données :

ColumnBindingDialog.Column.Expression=Expression

ColumnBindingDialog.DialogTitle=Sélectionner la liaison de données

ColumnBindingDialog.Column.Name=Nom

ColumnBindingDialog.Column.Filter=Filtrer

ColumnBindingDialog.Column.Function=Fonction

ColumnBindingDialog.Column.DisplayName=Nom d'affichage

ColumnBindingDialog.Column.DisplayNameID=ID du nom d'affichage

ColumnBindingDialog.InputPrompt=<Entrer la nouvelle valeur ici...>

ColumnBindingDialog.Button.Generate=Générer des liaisons de colonne

ColumnBindingDialog.Column.DataType=Type de données

ColumnBindingDialog.Column.AggregateOn=Regrouper par

ColumnBindingDialog.All=Tous

ColumnBindingDialog.NONE=Aucun

ColumnBindingDialog.Choice.DatasetFromContainer=Jeu de données du conteneur

ColumnBindingDialog.Choice.ReportItemFromContainer=Elément d'état référencé du conteneur

ColumnBindingDialog.AGGREGATEON.NONE=N/A
ColumnBindingDialog.Text.Add=&Ajouter...

ColumnBindingDialog.Text.Edit=Modifi&er...

ColumnBindingDialog.Text.Del=Supp&rimer

ColumnBindingDialog.Text.Refresh=Ac&tualiser

ColumnBindingDialog.Text.AddAggr=Ajouter l'a&grégation...

###########################################################
#BlockPreferencePage properties
BlockPreferencePage.displayname.Title=Bloc de texte

###########################################################
#BlockPreferencePage properties
SizePreferencePage.displayname.Title=Taille

###########################################################
#BorderCanvas properties
BorderCanvas.Label.Inherited=(Hérité)

###########################################################
#BorderPreferencePage properties
BorderPreferencePage.displayname.Color=Couleur

BorderPreferencePage.displayname.Style=Style

BorderPreferencePage.displayname.Title=Bordure

BorderPreferencePage.displayname.Width=Largeur

BorderPreferencePage.text.Bottom=Bas :

BorderPreferencePage.text.Left=Gauche :

BorderPreferencePage.text.Right=Droite :

BorderPreferencePage.text.Top=Haut :

###########################################################
#BordersPage properties
BordersPage.Label.Borders=&Bordures :

BordersPage.Label.Color=&Couleur :

BordersPage.Label.Presets=Valeurs par défaut :

BordersPage.Label.Style=St&yle :

BordersPage.Label.Width=&Largeur :

BordersPage.Tooltip.All=Toutes les bordures

BordersPage.Tooltip.Bottom=Bordure inférieure

BordersPage.Tooltip.Left=Bordure de gauche

BordersPage.Tooltip.None=Aucun

BordersPage.Tooltip.Right=Bordure de droite

BordersPage.Tooltip.Top=Bordure supérieure

BordersPage.text.Preview=Aperçu :

BordersPage.Trans.SelectBorder=Sélectionner une bordure

BordersPage.Trans.UnSelectBorder=Désélectionner la bordure

BordersPage.Trans.SelectAllborders=Sélectionner toutes les bordures

BordersPage.Trans.UnSelectAllborders=Désélectionner toutes les bordures

###########################################################
#BoxPreferencePage properties
BoxPreferencePage.displayname.Title=Zone

BoxPreferencePage.text.Margin=Marge

BoxPreferencePage.text.Padding=Remplissage

###########################################################
#CommentsPreferencePage properties
CommentsPreferencePage.displayname.Title=Commentaires

###########################################################
#ButtonPaneComposite properties
ButtonPaneComposite.tooltip.MasterPage=Page maître

ButtonPaneComposite.tooltip.Normal=Normal

ButtonPaneComposite.tooltip.Ruler=Règle

###########################################################
#SettingsPage properties
SettingsPage.dataFetchSetting.label=Nombre &maximum de lignes à récupérer à partir de la source de données :

SettingsPage.CachePreference.button.clear=Vider le &cache

SettingsPage.CachePreference.checkBox.text=Mettre en cac&he les données des jeux de données pour la prévisualisation de l'état

SettingsPage.CachePreference.description=Définir la taille du cache :

SettingsPage.CachePreference.Filter.Tooltip=Inclure ou exclure le cache de votre jeu de données

SettingsPage.CachePreference.label.text=lignes

SettingsPage.CachePreference.messageBox.info=Le cache a été vidé.

SettingsPage.CachePreference.messageBox.title=Cache vide

SettingsPage.CachePreference.radioBox.entireNumText=M&ettre en cache l'ensemble du jeu de données

SettingsPage.CachePreference.radioBox.numText=Mettre en cache et &utiliser en premier

###########################################################
#CascadingParametersDialog properties
CascadingParametersDialog.Button.isRequired=Est re&quis

CascadingParametersDialog.default.preview.string=Ma chaîne

CascadingParametersDialog.display.controlType.comboBox=Zone déroulante

CascadingParametersDialog.display.controlType.listBox=Liste déroulante

CascadingParametersDialog.Label.button.changeFormat=&Modifier...

CascadingParametersDialog.Label.button.createNew.dataset=&Créer...

CascadingParametersDialog.Label.cascadingParam.name=Nom de groupe de paramèt&res en cascade :

CascadingParametersDialog.Label.column.displayText=Texte d'affichage

CascadingParametersDialog.Label.column.dataSet=Jeu de données

CascadingParametersDialog.Label.column.name=Nom

CascadingParametersDialog.Label.column.value=Valeur

CascadingParametersDialog.Label.createNewParam=<Créer un nouveau paramètre...>

CascadingParametersDialog.Label.dataSets=Jeu de donnée&s :

CascadingParametersDialog.Label.dataType=T&ype de données

CascadingParametersDialog.Label.defaultValue=&Valeur par défaut :

CascadingParametersDialog.Label.displayType=Type d'a&ffichage :

CascadingParametersDialog.Label.formatAs=Format :

CascadingParametersDialog.Label.group.general=Général

CascadingParametersDialog.Label.group.moreOptions=Autres options

CascadingParametersDialog.Label.group.properties=Propriétés

CascadingParametersDialog.Label.helpText=&Texte d'aide :

CascadingParametersDialog.Label.listLimit=&Limite de liste :

CascadingParametersDialog.Label.NoColumnAvailable=<Aucune colonne disponible>

CascadingParametersDialog.Label.param.name=&Nom :

CascadingParametersDialog.Label.parameters=Paramètres

CascadingParametersDialog.Label.preview=Aperçu au format

CascadingParametersDialog.Label.groupPromptText=&Texte de l'invite :

CascadingParametersDialog.Label.parameterPromptText=Te&xte de l'invite :

CascadingParametersDialog.Label.selectDataSet=<Sélectionnez un jeu de données>

CascadingParametersDialog.Label.selectDisplayColumn=<Colonne à afficher non définie>

CascadingParametersDialog.Label.selectValueColumn=<Colonne de valeur non définie>

CascadingParametersDialog.Label.values=valeurs

CascadingParametersDialog.Radio.Single=Jeu de données u&nique

CascadingParametersDialog.Radio.Mutli=Jeu de données m&ultiple

CascadingParametersDialog.Choice.NullValue=Valeur nulle

CascadingParametersDialog.Choice.BlankValue=Valeur vierge

CascadingParametersDialog.Choice.SelectValue=<Sélectionner une valeur...>

CascadingParametersDialog.Button.Add=&Ajouter

CascadingParametersDialog.Button.Edit=Mod&ifier

CascadingParametersDialog.Button.Delete=Suppri&mer

CascadingParametersDialog.Button.IsMultiple=Auto&riser les valeurs multiples

CascadingParametersDialog.DefalutValue.Add=&Ajouter

CascadingParametersDialog.ExpressionEditor.Title=Modifier l'expression

CascadingParametersDialog.Title.AddCascadingParameter=Ajouter groupe de paramètres en cascade

CascadingParametersDialog.Title.EditCascadingParameter=Modifier groupe de paramètres en cascade

CascadingParametersDialog.items.None=<Aucun>

###########################################################
#CellPageGenerator properties
CellPageGenerator.List.Borders=Bordure

CellPageGenerator.List.CellPadding=Remplissage

CellPageGenerator.List.Font=Police

CellPageGenerator.List.General=Général


###########################################################
#ChoiceSetFactory properties
ChoiceSetFactory.choice.Auto=Auto

ChoiceSetFactory.choice.None=Aucun

###########################################################
#CodePageAction properties
CodePageAction.actionText.code=Script

###########################################################
#ColorBuilder properties
ColorBuilder.text.Auto=Auto

ColorBuilder.Button.ChooseColor=Ouvrir la palette des couleurs

ColorSelector.ColorDialog.Title=Couleur

ColorSelector.Name=Sélecteur de couleurs
###########################################################
#ColumnPageGenerator properties
ColumnPageGenerator.List.Borders=Bordure

ColumnPageGenerator.List.CellPadding=Marge intérieure

ColumnPageGenerator.List.Font=Police

ColumnPageGenerator.List.General=Général

ColumnPageGenerator.List.Visibility=Visibilité

###########################################################
#Commom properties
Commom.ShortDateTime.Fri=VEN

Commom.ShortDateTime.Mon=LUN

Commom.ShortDateTime.Sat=SAM

Commom.ShortDateTime.Sun=DIM

Commom.ShortDateTime.Thu=JEU

Commom.ShortDateTime.Tue=MAR

Commom.ShortDateTime.Wed=MER

###########################################################
#CopyAction properties
CopyAction.text=&Copier

###########################################################
#CreatePlaceHolderAction properties
CreatePlaceHolderAction.text=Créer un élément d'ét&at modèle...

###########################################################
#CutAction properties
CutAction.text=Co&uper

CutAction.trans=Couper les éléments

###########################################################
#dataBinding properties
dataBinding.label.selectBinding=Sélectionner la liaison de données :

dataBinding.message.changeDataSet=Vous allez modifier la liaison des données de l'élément.  \n\nVoulez-vous effacer toutes les informations des champs de données ?

dataBinding.message.changeReference=Vous allez modifier la liaison des données de l'élément.  \n\nSouhaitez-vous effectuer cette opération ?

dataBinding.title.changeDataSet=Modifier le jeu de données ?

dataBinding.title=Liaison de données

dataBinding.message.haveNoName=L'élément que vous avez sélectionné n'a pas de nom.  \n\nRetournez en arrière pour nommer l'élément afin de pouvoir le sélectionner.

dataBinding.title.haveNoName=Elément sans nom ?

dataBinding.button.OK=&OK

parameterBinding.title=&Liaison de paramètre du jeu de données...

ParameterBindingDialog.title=Liaison de paramètre du jeu de données

dataBinding.title.generateOutputParam=Générer des liaisons ?

dataBinding.msg.generateOutputParam=Le jeu de données comporte au moins un paramètre de sortie.\n\n Voulez-vous générer des liaisons pour ces paramètres ?

###########################################################
#DataEditPart properties
DataEditPart.Figure.Dafault=Données

DataEditPart.Figure.Expression=expression

DataEditPart.stackMsg.edit=Modifier la liaison de données

DataEditPart.text.AggregateOn=Regrouper par

###########################################################
#DataEditPart properties
DestroyEditPart.Message=Le contenu de cet élément d'état est corrompu. Pour continuer à utiliser cet état, vous devez d'abord supprimer cet élément. Dans le cas contraire, le fichier du modèle d'état risque de subir des dommages et/ou la réactivité de Report Designer risque d'être altérée.

###########################################################
#DataPageGenerator properties
DataPageGenerator.List.Bookmark=Signet

DataPageGenerator.List.Borders=Bordure

DataPageGenerator.List.Duplicates=Doublons

DataPageGenerator.List.Expression=Expression

DataPageGenerator.List.Format=Format

DataPageGenerator.List.formatDateTime=Date/heure

DataPageGenerator.List.formatNumber=Nombre

DataPageGenerator.List.formatString=Chaîne

DataPageGenerator.List.General=Général

DataPageGenerator.List.Highlights=Surbrillances

DataPageGenerator.List.HyperLink=Lien hypertexte

DataPageGenerator.List.Margin=Marge

DataPageGenerator.List.Padding=Remplissage

DataPageGenerator.List.Section=Saut de page

DataPageGenerator.List.TOC=Table des matières

DataPageGenerator.List.Visibility=Visibilité

###########################################################
#dataset properties
dataset.edit=Modifier le jeu de données - {0}

dataset.editor.settings=Paramètres

dataset.editor.settings.dataFetchSetting.errorNumberFormat=Entrez un nombre entier supérieur à zéro pour la limite de récupération de ligne.

dataset.editor.settings.dataFetchSetting.errorNumberFormatForResultSet=Entrez un entier positif pour le nombre de jeux de résultats.

dataset.editor.settings.dataFetchSetting=Paramètre de récupération de données

dataset.editor.settings.resultsetselection.resultSetSelection=Sélection du jeu de résultats

dataset.editor.settings.resultsetselection.enableResultSetSelection=Activ&er la sélection du jeu de résultats

dataset.editor.settings.resultsetselection.selectResultSetByName=Sélectionner le jeu de &résultats par nom :

dataset.editor.settings.resultsetselection.selectResultSetByNumber=&Sélectionner le jeu de résultats par nombre :

dataset.editor.settings.dataFetchSetting.fetchAll=Récupérer &toutes les lignes à partir de la source de données.

dataset.editor.settings.cachePreference.errorNumberFormat=Entrez un nombre supérieur à zéro pour la taille des lignes en cache.

dataset.editor.settings.cachePreference=Préférences de cache

dataset.editor.computedColumns=Colonnes de calcul

dataset.editor.dataSource.Tooltip=Afficher/modifier la source de données.

dataset.editor.dataSource=Source de données

dataset.editor.error.columnOrAliasNameAlreadyUsed=Le nouveau nom ou alias de colonne ({0}) est déjà utilisé par la colonne à l''index {1}.

dataset.editor.error.computedColumnNameAlreadyUsed=Le nouveau nom de la colonne de calcul ({0}) est déjà utilisé comme alias pour la colonne ({1}) dans les Colonnes de sortie.

dataset.editor.error.duplicateParameterName=Nom de paramètre en double.

dataset.editor.error.nonemptyDefaultvalue=La valeur par défaut ne peut pas être nulle.

dataset.editor.error.emptyName=Le nom ne doit pas rester vierge.

dataset.editor.error.missingComputedColumnExpression=L''expression de la colonne de calcul {0} ne peut être ni nulle, ni vide.

dataset.editor.error.missingComputedColumnName=Le nom de la colonne de calcul ne peut être ni nul, ni vide.

dataset.editor.error.noDataSource=Aucune source de données disponible pour le jeu de données {0}, sélectionnez une source de données dans l''entrée Source de données de la vue des propriétés.

dataset.editor.error.nonmatchedDataSource=Source de données sans correspondance pour le jeu de données "{0}", sélectionnez une source de données correspondant à un type de jeu de données "{1}".

dataset.editor.error.noEditorsFound=Aucun éditeur trouvé pour le jeu de données {0}.

dataset.editor.error.noInputParameterDefaultValue=Le paramètre d''entrée {0} doit comporter une valeur renseignée par défaut.

dataset.editor.error.title=Avertissement

dataset.editor.error.unsupportedDataSet=Type de jeu de données non pris en charge - {0}.

dataset.editor.error.validationParameter=Un paramètre d'entrée [{0}] contient une valeur vide. Voulez-vous laisser cette valeur vide ?

dataset.editor.filters=Filtres

dataset.editor.label.selectDataSource=Sélectionner une source de &données :

dataset.editor.outputColumns=Colonnes de sortie

dataset.editor.parameters=Paramètres

dataset.editor.preview=Prévisualiser les résultats

dataset.editor.outputparameters=Prévisualiser les paramètres de sortie

dataset.editor.title.alias=Alias

dataset.editor.title.columnName=Nom de colonne

dataset.editor.title.dataType=Type de données

dataset.editor.title.defaultValue=Valeur par défaut

dataset.editor.title.direction=Direction

DataSetParameterPage.editor.title.linkToSalarParameter=Lié au paramètre d'état

dataset.editor.title.displayName=Nom d'affichage

dataset.editor.title.displayNameKey=Clé de nom d'affichage

dataset.editor.title.expression=Expression

dataset.editor.title.aggrFunc=Agrégation

dataset.editor.title.aggrArgu=A&rgument

dataset.editor.title.filter=Filtrer

dataset.editor.title.helpText=Texte d'aide

dataset.editor.title.isInput=Est une entrée

dataset.editor.title.isNullable=Autorise une valeur nulle

dataset.editor.title.isOptional=Facultatif

dataset.editor.title.isOutput=Est une sortie

dataset.editor.title.name=Nom

dataset.editor.title.nativeName=Nom natif

dataset.editor.title.operator=Opérateur

dataset.editor.title.type=Type

dataset.editor.title.value1=Valeur 1

dataset.editor.title.value2=Valeur 2

dataset.editor.inputDialog.displayName=Nom &d'affichage

dataset.editor.inputDialog.displayNameKey=&Clé de nom d'affichage

dataset.editor.inputDialog.name=&Nom

dataset.editor.inputDialog.nativeName=Nom n&atif

dataset.editor.inputDialog.type=&Type

dataset.editor.inputDialog.alias=&Alias

dataset.editor.inputDialog.columnName=Nom de &colonne

dataset.editor.inputDialog.dataType=Type de &données

dataset.editor.inputDialog.expression=&Expression

dataset.editor.inputDialog.aggrArgu=A&rgument

dataset.editor.inputDialog.aggrFunc=A&grégation

dataset.editor.inputDialog.filter=&Filtre

dataset.editor.inputDialog.value1=Valeur &1

dataset.editor.inputDialog.value2=Valeur &2

dataset.editor.inputDialog.operator=&Opérateur

dataset.editor.inputDialog.defaultValue=&Valeur par défaut

dataset.editor.inputDialog.direction=&Direction

dataset.editor.historybar.backward=Précédent

dataset.editor.historybar.forward=Suivant

dataset.editor.historybar.to=en

dataset.error.noDataSources=Aucune source de données disponible. Voulez-vous ajouter une source de données immédiatement ?

dataset.error.dataSourceMissing=La source de données du jeu de données est manquante.

dataset.error.title.noDataSources=Aucune source de données disponible.

dataset.message.create=Créez un nouveau jeu de données.

dataset.new.defaultName=Jeu de données

dataset.new=Nouveau jeu de données

dataset.action.new=Nouveau jeu de do&nnées

dataset.join.new=Nouveau jeu de données de jointure

dataset.action.join.new=Nouveau &jeu de données de jointure

dataset.resultset.preview.tooltip=Prévisualiser les résultats de la requête.

dataset.resultset.preview.promptMessage.recordsNum={0} enregistrement(s) affiché(s) au total.

dataset.resultset.preview.promptMessage.MoreRecordsExist=Le résultat contient plus d'enregistrements qui ne sont pas affichés.

dataset.outputparameters.preview.tooltip=Prévisualiser les paramètres de sortie.

dataset.wizard.label.datasetName=No&m du jeu de données :

dataset.wizard.label.datasetType=&Type de jeu de données :

dataset.wizard.label.dataSource=&Source de données :

dataSourceSelectionPage.title=Lien avec la source de données.

dataset.error.title.noReportDesign=Erreur

dataset.error.msg.noReportDesign=Aucun modèle d'état disponible.

dataset.warning.invalidReponseStatus=Réponse de conception de la page ODA personnalisée non valide. Etat de la réponse de session de conception ODA : {0}.

###########################################################
#DataSetBasePage properties
DataSetBasePage.tooltip=Le nom doit être renseigné.

DataSetBasePage.ScriptedDataSet.name=Jeu de données nécessitant un script

DataSetBasePage.ScriptedDataSource.name=Source de données nécessitant un script

DataSetBasePage.Group.DataSourceSelection=Sélection de source de données

DataSetBasePage.CassandraScriptedDataSet.name=Jeu de données Cassandra nécessitant un script

DataSetBasePage.CassandraScriptedDataSource.name=Source de données Cassandra nécessitant un script

DataSetBindingSelector.Button.DeselectAll=&Tout désélectionner

DataSetBindingSelector.Button.SelectAll=&Sélectionner tout

DataSetBindingSelector.Combo.DataSet=&Jeu de données :

DataSetBindingSelector.Label.SelectBindingColumns=Sélectionnez les colonnes de liaison.

DataSetBindingSelectorPage.Label.SelectBindingColumns=Sélectionnez les colonnes de liaison.

DataSetBindingSelectorPage.Title=Liaison de jeu de données

CassandraScriptedDataSource.display.name=Source de données Cassandra nécessitant un script

CassandraScriptedDataSource.error.classNotFound=Impossible de trouver les JARS de la bibliothèque client Hector pour prendre en charge la source de données Cassandra nécessitant un script.

###########################################################
#JointDataSetPage properties
JointDataSetPage.pageName=Jeu de données de jointure

JointDataSetPage.page.detail=Sélectionner un jeu de données et une colonne de chaque côté pour définir une jointure.

JointDataSetPage.button.innerJoin=Jointure &interne

JointDataSetPage.button.leftJoin=&Jointure externe gauche

JointDataSetPage.button.rightJoin=&Jointure externe droite

JointDataSetPage.button.fullJoin=J&ointure externe complète

JointDataSetPage.joinType=Types de jointures

JointDataSetPage.error.nodataset.title=Aucun jeu de données n'a été trouvé.

JointDataSetPage.error.nodataset.right=Le jeu de données droit n'a pas été trouvé.

JointDataSetPage.error.nodataset.left=Le jeu de données gauche n'a pas été trouvé.

JointDataSetPage.pageDescription=Définir le jeu de données de jointure

JointDataSetPage.query=Requête

###########################################################
#DerivedDataSet properties
DerivedDataSet.action.error.message.NoDataSet=Aucun jeu de données disponible n'a été trouvé.

DerivedDataSet.action.error.title.createDerivedDataSet=Echec de la création du jeu de données dérivées

DerivedDataSet.action.error.message.InvalidNumber.DataSet=Le nombre de jeux de données disponibles est inférieur à deux.

###########################################################
#DataSetComputedColumnsPage properties
DataSetComputedColumnsPage.ComputedColumns.Tooltip=Définissez les colonnes de calcul.

DataSetComputedColumnsPage.description=Définir des colonnes de calcul :

DataSetComputedColumnsPage.duplicatedName=Nom de colonne en double.

DataSetComputedColumnsPage.numberName=Le nom de la colonne ne doit pas être un chiffre.

DataSetComputedColumnsPage.toolTipText.Down=Déplacer la colonne de calcul vers le bas.

DataSetComputedColumnsPage.toolTipText.Up=Déplacer la colonne de calcul vers le haut.

DataSetComputedColumnsPage.toolTipText.Remove=Supprimer la colonne de calcul.

DataSetComputedColumnsPage.toolTipText.New=Nouvelle colonne de calcul.

DataSetComputedColumnsPage.toolTipText.Edit=Modifier la colonne de calcul.

DataSetComputedColumnsPage.InputDialog.title.New=Nouvelle colonne de calcul

DataSetComputedColumnsPage.InputDialog.title.Edit=Modifier la colonne de calcul

DataSetComputedColumnsPage.InputDialog.message.error.AggregationExpression=L'expression de l'agrégation ne peut pas inclure une colonne de calcul d'agrégation !

DataSetComputedColumnsPage.title.ComputedColumnInputDialog=Colonne de calcul

###########################################################
#DataSetFiltersPage properties
DataSetFiltersPage.description=Définir des filtres :

DataSetFiltersPage.exception.msg.operator.notSupport=Opérateur non pris en charge :

DataSetFiltersPage.Filter.Tooltip=Filtrez vos lignes de données au moyen d'expressions BIRT.

DataSetFiltersPage.toolTipText.Down=Déplacer le filtre vers le bas.

DataSetFiltersPage.toolTipText.Up=Déplacer le filtre vers le haut.

DataSetFiltersPage.toolTipText.Remove=Supprimer le filtre.

DataSetFiltersPage.toolTipText.New=Nouveau filtre.

DataSetFiltersPage.toolTipText.Edit=Modifier le filtre.

DataSetFiltersPage.title.FilterInputDialog=Filtrer

DataSetFiltersPage.messages.selectVauleUnavailable=Liste de valeurs non disponible avec l'expression spécifiée.

DataSetFiltersPage.DynamicFilter.DynamicParameter.NotFound=Le paramètre dynamique lié "{0}" du filtre dynamique n'existe pas.

###########################################################
#DataSetNodeProvider properties
DataSetNodeProvider.menu.text=&Modifier

###########################################################
#DataSetParametersPage properties
DataSetParametersPage.description=Définir les paramètres d'entrée :

DataSetParametersPage.Filter.Tooltip=Inclure ou exclure des paramètres de votre jeu de données.

DataSetParameterBindingInputDialog.Title=Modifier la liaison de paramètre du jeu de données

DataSetParametersPage.ErrorTitle.InvalidInput=Entrée non valide.

DataSetParameterBindingInputDialog.Label.Name=Nom du paramètre :

DataSetParametersPage.ErrorMessage.CanootBeNull=La valeur ne peut pas être nulle.

DataSetParameterBindingInputDialog.Title.NewParameter=Nouveau paramètre

DataSetParameterBindingInputDialog.Title.EditParameter=Modifier le paramètre

DataSetParameterBindingInputDialog.Label.DataType=Type de données :

DataSetParameterBindingInputDialog.Label.Value=&Valeur :

DataSetParametersPage.ParameterButton.Tooltip.Edit=Modifier le paramètre d'état

DataSetParametersPage.ParameterButton.Tooltip.New=Nouveau paramètre d'état

DataSetParametersPage.Prompt.new=<Nouveau paramètre ici...>

DataSetParametersPage.default.None=N/A
DataSetParametersPage.reportParam.None=Aucun

DataSetParametersPage.errorMessage.LinkedReportParamNotFound=Le paramètre d'état lié "{0}" du paramètre du jeu de données "{1}" n'existe pas actuellement.

DataSetParametersPage.errorMessage.InvalidType.LinkedReportParam=Si l'utilisateur définit plusieurs valeurs pour le paramètre d'état, le paramètre de l'ensemble de données utilisera uniquement la première valeur.

DataSetParameterPage.cell.linkToSalarParameter=&Lié au paramètre d'état

DataSetParameterPage.title.ParameterInputDialog=Paramètre

DataSetParameterPage.toolTipText.New=Nouveau paramètre

DataSetParameterPage.toolTipText.Edit=Modifier le paramètre

DataSetParameterPage.toolTipText.Remove=Supprimer le paramètre

DataSetParameterPage.toolTipText.Up=Déplacer le paramètre vers le haut

DataSetParameterPage.toolTipText.Down=Déplacer le paramètre vers le bas

DataSetParameterPage.WarningMessage.updatePosition=Les valeurs de position de la totalité ou d'une partie des paramètres du jeu de données sont incorrectes. Mise à jour de leurs positions selon leur séquence.

DataSetParameterPage.warning.UnmatchedParamDataType=Le type de paramètre du jeu de données ne correspond pas au type de paramètre d'état lié.

DataSetParameterPage.multiValuesType=Type de valeurs multiples

DataSetParameterPage.warningLinkedMultipleValuesParams.title=Boîte de dialogue Avertissement

DataSetParameterPage.updateReportParameter.title=Configuration de la mise à jour du paramètre d'état

DataSetParameterPage.updateReportParameter.message=Voulez-vous mettre à jour le paramètre d'état lié avec les paramètres du jeu de données des réglages ?

DataSetParameterPage.updateReportParameter.promptButtonYes=Oui

DataSetParameterPage.updateReportParameter.promptButtonNo=Non

DataSetParameterPage.updateReportParameter.propmtText=Mémoriser ma décision

PromptParameterDialog.title=Confirmer la mise à jour des attributs de modèle

PromptParameterDialog.okButton.text=Appliquer

PromptParameterDialog.parameterGroup.title=Attributs des modèles de paramètres

PromptParameterDialog.parameterGroup.label=Mettre à jour les paramètres sélectionnés avec la ou les valeurs d'attributs révisées par le modèle du jeu de données édité.

PromptParameterDialog.parameterGroup.nameColumn=Nom

PromptParameterDialog.parameterGroup.previousValue=Valeur précédente

PromptParameterDialog.parameterGroup.revisedValue=Valeur révisée

###########################################################
#PropertyBindingpage properties
PropertyBindingPage.datasource.property.empty=Aucune propriété publique n'est définie par l'extension ODA de cette source de données.

PropertyBindingPage.dataset.property.empty=Aucune propriété publique n'est définie par l'extension ODA de ce jeu de données.

PropertyBindingPage.dataset.queryText=&Texte de la requête

PropertyBindingPage.property.tooltip=Définir une liaison de propriété pour l'exécution.

PropertyBindingPage.button.tooltip=Ouvrir le Générateur d'expression pour entrer la valeur {0}.

PropertyBindingPage.label.colon=:
PropertyBindingPage.label.LeftParenthesis=(
PropertyBindingPage.label.RightParenthesis=)

###########################################################
#DataSetProcess properties
DataSetProcess.action.Delete=Supprimer le jeu de données

DataSetProcess.action.Edit=Modifier le jeu de données

DataSetProcess.action.Preview=Aperçu

DataSetProcess.text.DisplayName=Jeu de données

DataSetProcess.text.Prefix=Le(s) jeu(x) de données suivant(s) utilise(nt) cette source de données :

DataSetProcess.text.Show=Affiche la page de l'éditeur de jeu de données pour ce jeu de données.

DataSetProcess.text.Suffix=Supprimer cet élément ?

###########################################################
#datasource properties
datasource.action.edit=&Modifier

datasource.edit=Modifier la source de données - {0}

datasource.new.defaultName=Source de données

datasource.new=Nouvelle source de données

datasource.action.new=Nouvelle &source de données

datasource.wizard.label.dataSourceName=No&m de la source de données :

datasource.wizard.label.datasources=Créer à partir d'un type de source de &données de la liste suivante

datasource.wizard.label.connectionfile=Créer à partir d'un profil de &connexion de l'emplacement de stockage des profils

datasource.wizard.message.selectType=Indiquer tous les paramètres pour une nouvelle source de données ou choisir un profil de connexion prédéfini.

datasource.wizard.title.select=Sélectionner un type de source de données ou choisir un profil de connexion.

datasource.editor.property=Liaison de propriété

datasource.editor.duplicatedName=Le nom de la source de données "{0}" est dupliqué.

datasource.error.title.noReportDesign=Erreur

datasource.error.msg.noReportDesign=Aucun modèle d'état disponible.

datasource.exprotToCP.title=Exporter

datasource.exportToCP.message=Vous exportez "{0}" vers l'explorateur de source de données.

datasource.exportToCP.checkBox=&Créez un emplacement de stockage des profils de connexion pour la source de données exportée.

datasource.exportToCP.externalCheckBox=&Utilisez le profil de connexion externalisé dans la source de données.

datasource.exportToCP.label.specifyName=Nom du profil de conn&exion :

datasource.exportToCP.errorBox.button.OK=OK

datasource.exportToCP.error.emptyFileName=Vous devez indiquer un nom de profil de connexion non vide !

datasource.exportToCP.error.invalidFileName=Le nom du profil de connexion indiqué contient un ou des caractères non valides !

datasource.exportToCP.error.duplicatedFileName=Le nom du profil de connexion existe déjà !

datasource.exportToCP.error.alreadyExported=La source de données "{0}" a déjà externalisé ses propriétés.


###########################################################
#DataSourceProcess properties
DataSourceProcess.action.Delete=Supprimer la source de données

DataSourceProcess.action.Edit=Modifier la source de données

DataSourceProcess.text.DisplayName=DataSource
DataSourceProcess.text.Prefix=Le(s) jeu(x) de données suivant(s) utilise(nt) cette source de données

DataSourceProcess.text.Show=Affiche la page de l'éditeur de source de données

DataSourceProcess.text.Suffix=Supprimer cet élément ?

###########################################################
#DataSourceSelectionPage properties
DataSourceSelectionPage.ScriptDataSource.DisplayName=Source de données nécessitant un script

###########################################################
#DataSourcesSlotOutlineProcess properties
DataSourcesSlotOutlineProcess.action.New=Nouvelle source de données

###########################################################
#datatypes properties
datatypes.any=Indifféremment

datatypes.auto=Auto

datatypes.dateTime=Date Heure

datatypes.decimal=Décimal

datatypes.float=Flottante

datatypes.integer=Entier

datatypes.string=Chaîne

###########################################################
#DataView properties
DataView.defaultText.noDataView=Une vue de données n'est pas disponible.

###########################################################
#DataViewTreeViewerPage properties
DataViewTreeViewerPage.tooltip.DoubleClickToEdit=Cliquez deux fois pour modifier

DataViewTreeViewerPage.tooltip.DragToInsertColumn=Faites glisser pour insérer la colonne

DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns=Faites glisser pour insérer toutes les colonnes de jeux de données

DataViewTreeViewerPage.tooltip.DragToInsertParameter=Faites glisser pour insérer le paramètre d'état ;

###########################################################
#DebugUtil properties
DebugUtil.importProject.fail=« Echec de l'importation du fichier existant. »

###########################################################
#DecorationFieldEditor properties
DecorationFieldEditor.text.Linethrough=Texte barré

DecorationFieldEditor.text.Overline=Texte surligné

DecorationFieldEditor.text.Underline=Soulignement

###########################################################
#DefaultNodeProvider properties
DefaultNodeProvider.Dlg.Confirm=Cliquez sur Oui si vous souhaitez supprimer le {0}. Dans le cas contraire, cliquez sur Non

DefaultNodeProvider.ParameterGroup.ConfirmMessage=Vous êtes sur le point de supprimer un groupe de paramètres. Si vous confirmez la suppression, tous les paramètres du groupe seront supprimés. Voulez-vous continuer ?

DefaultNodeProvider.ParameterGroup.ConfirmTitle=Confirmer la suppression du groupe de paramètres

DefaultNodeProvider.Tree.Body=Corps

DefaultNodeProvider.Tree.Clients=Le {0} est utilisé par les éléments suivants

DefaultNodeProvider.Tree.ColumnHedings=En-têtes de colonnes

DefaultNodeProvider.Tree.Components = Eléments d'état

DefaultNodeProvider.Tree.DataSets=Jeux de données

DefaultNodeProvider.Tree.DataSources=Sources de données

DefaultNodeProvider.Tree.Detail=Détails

DefaultNodeProvider.Tree.Footer=Pied de page

DefaultNodeProvider.Tree.Groups=Groupes

DefaultNodeProvider.Tree.Header=En-tête

DefaultNodeProvider.Tree.Images=Images incorporées

DefaultNodeProvider.Tree.Invalid=Noeud non valide

DefaultNodeProvider.Tree.MasterPages=MasterPages
DefaultNodeProvider.Tree.PageSetup=Mise en page

DefaultNodeProvider.Tree.Parameters=Paramètres de l'état

DefaultNodeProvider.Tree.Reference=Avertissement

DefaultNodeProvider.Tree.Scratch=Zone de travail

DefaultNodeProvider.Tree.Styles=Styles

DefaultNodeProvider.Tree.Themes=Thèmes

DefaultNodeProvider.WarningDialog.Title=Avertissement

DefaultNodeProvider.WarningDialog.EmptyList=Aucun élément disponible pour insertion.

DefaultNodeProvider.stackMsg.changeBinding=Modifier la liaison de données

###########################################################
#DeleteAction properties
DeleteAction.text=&Supprimer

DeleteAction.trans=Supprimer les éléments

###########################################################
#DeleteColumnAction properties
DeleteColumnAction.actionMsg.delete=&Supprimer

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.actionMsg.deleteGroup=Supprimer le &groupe

###########################################################
#DeleteListGroupAction properties
DeleteListGroupAction.actionMsg.deleteGroup=Supprimer le &groupe

###########################################################
#DeleteRowAction properties
DeleteRowAction.actionMsg.delete=&Supprimer

###########################################################
#designer properties
designer.preview.browserTitle=Aperçu

designer.preview.preference.browser.autoSave=Enregistrer automatiquement avant la prévisualisation

designer.preview.preference.browser.browse=& Parcourir...

designer.preview.preference.browser.currentBrowsers=Adaptate&urs externes de navigateur Web actuels :

designer.preview.preference.browser.description=Permet de choisir les navigateurs Web utilisés pour prévisualiser les états BIRT.\nSi un seul adaptateur de navigation a été configuré, la sélection ne\npeut pas être modifiée.

designer.preview.preference.browser.details=Sélectionner un programme de navigation

designer.preview.preference.browser.program=Commande du navigateur per&sonnalisé :

designer.preview.preference.browser.svg=Activ&er le graphique SVG

designer.preview.preference.browser.extbrowser.link=Reportez-vous à <a>''{0}''</a> pour configurer le navigateur externe utilisé pour afficher les états.

designer.preview.preference.masterpagecontent=Activer le contenu de la page &maître

designer.preview.preference.appcontextkey=&Utiliser un contexte d'application personnalisé

designer.preview.preference.browser.useExternal=&Toujours utiliser des navigateurs externes

designer.preview.preference.elementname.columnwidth=125

designer.preview.preference.elementname.customname.Enabled=vrai

designer.preview.preference.elementname.customname=Nom par défaut

designer.preview.preference.elementname.defaultname.Enabled=faux

designer.preview.preference.elementname.defaultname=Nom de l'élément

designer.preview.preference.elementname.description.Enabled=faux

designer.preview.preference.elementname.description=Description

designer.preview.preference.elementname.Enabled=vrai

designer.preview.preference.locale.description=&Choisir vos informations locales :

designer.preview.preference.timezone.description=&Fuseau horaire :

designer.preview.preference.resultset.sqldatasetpage.group.title=Page de requête

designer.preview.preference.resultset.previewpage.group.title=Page de prévisualisation

designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting=Afficher le paramètre de nombre de lignes maximum

designer.preview.preference.resultset.NoLimitOfDispalyRow=Aucu&ne limite du nombre de lignes à afficher.

designer.preview.preference.resultset.maxNoOfSchema.description=Nombre maximum de &schémas à afficher :

designer.preview.preference.resultset.maxNoOfTable.description=Nombre maximum de &tables de chaque schéma à afficher :

designer.preview.preference.resultset.maxNoOfSchema.errormessage=Entrez un chiffre compris entre 0 et {0} pour le nombre maximum de schémas.

designer.preview.preference.resultset.maxNoOfTable.errormessage=Entrez un chiffre compris entre 0 et {0} pour le nombre maximum de tables de chaque schéma.

designer.preview.preference.resultset.maxrow.description=Nombre de lignes à affiche&r :

designer.preview.preference.resultset.maxrow.errormessage=Entrez un nombre compris entre 1 et {0} pour le nombre maximal de lignes.

designer.preview.preference.resultset.maxrowlevelmember.description=Nombre de membres pour chaque &ligne à afficher :

designer.preview.preference.resultset.maxrowlevelmember.errormessage=Entrez un nombre compris entre 1 et {0} pour le nombre maximal de membres par ligne.

designer.preview.preference.resultset.maxcolumnlevelmember.description=Nombre de membres pour chaque &colonne à afficher :

designer.preview.preference.resultset.maxcolumnlevelmember.errormessage=Entrez un nombre compris entre 1 et {0} pour le nombre maximal de membres par colonne.

designer.preview.preference.resultset.maxinmemorycubesize.description=&Taille de cube dans la mémoire (Mo) :

designer.preview.preference.resultset.maxinmemorycubesize.errormessage=Entrez un nombre compris entre 1 et {0} pour la taille maximale de cube dans la mémoire.

designer.preview.preference.server.description=Configuration du serveur d'application interne. Le serveur est utilisé en interne par\nla structure de prévisualisation de BIRT Report Designer.

designer.preview.preference.server.hostDescription=Nom d'&hôte ou adresse IP de la machine locale. Ne pas renseigner, détermination\nautomatique.

designer.preview.preference.server.note=Remarque :

designer.preview.preference.server.portDescription=&Port pour l'écoute par le serveur. Réglé sur 0 pour une attribution dynamique.

designer.preview.preference.server.requireRestart=Si le serveur est déjà en cours d'exécution, ces préférences ne prendront effet\nqu'après redémarrage.

designer.preview.preference.bidiOrientation.label=Orientation &Bidi :

designer.preview.preference.bidiOrientation.auto=Auto

designer.preview.preference.bidiOrientation.ltr=Gauche à droite

designer.preview.preference.bidiOrientation.rtl=Droite à gauche

designer.preview.preference.updateParameter.confirmMessage=Confirmer la mise à jour

designer.preview.preference.updateParameter.confirmButton=&Toujours demander la confirmation lors de la synchronisation des propriétés de paramètre de jeux de données avec les propriétés de paramètre ODA si la demande est modifiée.

designer.preview.preference.updateReportParameter.confirmButton=&Ne pas demander la confirmation de mise à jour d'un paramètre d'état lié.

designer.preview.preference.updateReportParameter.confirmOption.always=&Toujours :

designer.preview.preference.updateReportParameter.confirmOption.update=Mettre à jo&ur

designer.preview.preference.updateReportParameter.confirmOption.ignore=&Ignorer

designer.preview.previewaction.label.webviewer=Afficher l'état dans le visualiseur Web

designer.preview.previewaction.label=Afficher l''état au format {0}

designer.preview.previewaction.ErrorMessage=La fonction de génération de PDF n'est pas activée car iText n'est pas installé ou est introuvable.

designer.preview.previewaction.ErrorMessage.title=Avertissement

designer.preview.run.webviewer=Dans le visualiseur Web

designer.preview.run=Comme {0}

###########################################################
#DesignerActionBarContributor properties
DesignerActionBarContributor.element.group=Insérer le &groupe

DesignerActionBarContributor.element.InsertColumnLeft=Insérer des colonnes à g&auche

DesignerActionBarContributor.element.InsertColumnRight=Insérer des colonnes à &droite

DesignerActionBarContributor.element.InsertRowAbove=Insérer une ligne au-de&ssus

DesignerActionBarContributor.element.InsertRowBelow=Insérer une ligne au-desso&us

DesignerActionBarContributor.element.merge=&Fusionner les cellules

DesignerActionBarContributor.element.split=Fractionner la &cellule

designerActionBarContributor.menu.data-newdataset=Nouveau jeu de donné&es

designerActionBarContributor.menu.data-newdatasource=Nouvelle source de d&onnées

DesignerActionBarContributor.menu.data=&Données

DesignerActionBarContributor.menu.element-EditGroup=&Modifier le groupe

DesignerActionBarContributor.menu.element-LayoutMode=Mode de mise en &page

DesignerActionBarContributor.menu.element-showRuler=Afficher les &règles

DesignerActionBarContributor.menu.element.editDataBinding=Modifier la liaison de &données...

DesignerActionBarContributor.menu.element=E&lément

DesignerActionBarContributor.menu.format=Format

DesignerActionBarContributor.menu.insert=&Insérer

DesignerActionBarContributor.menu.page=Pa&ge

DesignerActionBarContributor.menu.style-rule-apply=A&ppliquer un style

DesignerActionBarContributor.menu.style-rule-edit=Mo&difier le style

DesignerActionBarContributor.menu.style-rule-remove=Supp&rimer le style

DesignerActionBarContributor.style.new=&Nouveau style...

DesignerActionBarContributor.menu.data-NewParameter=Nouveau &paramètre d'état

DesignerActionBarContributor.menu.data-NewDataSetParent=Nouveau jeu de do&nnées

designerActionBarContributor.menu.data-newDataset=Nouveau jeu de donné&es

designerActionBarContributor.menu.data-newJointDataset=Nouveau &jeu de données de jointure

DesignerActionBarContributor.menu.element.insert=&Insérer

DesignerActionBarContributor.menu.element.group=&Groupe

DesignerActionBarContributor.menu.element.style=St&yle

DesignerActionBarContributor.menu.zoomCombo.tooltip=Niveau de zoom
###########################################################
#DesignerOutlinePage properties
DesignerOutlinePage.action.Delete=Supprimer les sélections

DesignerOutlinePage.action.Hide=Masquer l'en-tête et le pied de page

DesignerOutlinePage.action.Redo=Rétablir

DesignerOutlinePage.action.Undo=Annuler

DesignerOutlinePage.menu.Filters=Filtres

###########################################################
#Content outline page switch action
ContentOutlinePage.action.text.reportOutline=Passer au contour de la source XML

ContentOutlinePage.action.text.reportXMLSourceOutline=Passer au contour de l'état

###########################################################
#DesignerPaletteFactory properties
DesignerPaletteFactory.categoryName.reportItems=Eléments d'état

DesignerPaletteFactory.elementLabel.data=Données

DesignerPaletteFactory.elementLabel.grid=Grille

DesignerPaletteFactory.elementLabel.image=Image

DesignerPaletteFactory.elementLabel.label=Etiquette

DesignerPaletteFactory.elementLabel.list=Liste

DesignerPaletteFactory.elementLabel.tabel=Table

DesignerPaletteFactory.elementLabel.text=Texte

DesignerPaletteFactory.elementName.data=Données

DesignerPaletteFactory.elementName.grid=Grille

DesignerPaletteFactory.elementName.image=Image

DesignerPaletteFactory.elementName.label=Etiquette

DesignerPaletteFactory.elementName.list=Liste

DesignerPaletteFactory.elementName.table=Table

DesignerPaletteFactory.elementName.text=Texte

DesignerPaletteFactory.elementName.textdata=Texte dynamique

DesignerPaletteFactory.toolTip.dataReportItem=Insérez une colonne de jeu de données ou le résultat d'une expression.

DesignerPaletteFactory.toolTip.gridReportItem=Insérez une présentation de ligne/colonne pour organiser d'autres éléments.

DesignerPaletteFactory.toolTip.imageReportItem=Insérez une image à partir d'un fichier ou d'une URL.

DesignerPaletteFactory.toolTip.labelReportItem=Insérez un champ de texte statique.

DesignerPaletteFactory.toolTip.listReportItem=Insérer une présentation de format flexible composée de lignes de jeu de données dans la partie en-tête/détail/pied de page

DesignerPaletteFactory.toolTip.multiLineDataReportItem=Insérer un bloc de texte (texte brut/HTML/RTF) à partir d'un fichier, d'une colonne de jeu de données ou du résultat d'une expression.

DesignerPaletteFactory.toolTip.tableReportItem=Insérer une présentation en colonne composée de lignes de jeu de données dans la partie en-tête/détail/pied de page

DesignerPaletteFactory.toolTip.textDataReportItem=Insérer un bloc de texte avec insertion optionnelle d'expression(s) de données CLOB

DesignerPaletteFactory.toolTip.textReportItem=Insérez un bloc de texte (texte brut/HTML) avec insertion optionnelle d'expression(s).

DesignerPaletteFactory.quicktool.title=Outils rapides

DesignerPaletteFactory.quicktool.agg.title=Agrégation

DesignerPaletteFactory.quicktool.agg.toolTip=Insérer un élément d'agrégation

DesignerPaletteFactory.quicktool.timeperiod.title=Période de temps relatif

DesignerPaletteFactory.quicktool.timeperiod.toolTip=Insérer l'élément TimePeriod

###########################################################
#DimensionBuilderDialog properties
DimensionBuilderDialog.LabelMeasure=&Mesurer :

DimensionBuilderDialog.LabelUnit=&Unités :

DimensionBuilderDialog.Title=Dimension Builder

###########################################################
#DNDUtil properties
DNDUtil.trans.copy=Copier les éléments

DNDUtil.trans.cut=Déplacer les éléments

###########################################################
#DuplicatesPage properties
DuplicatesPage.Check.HideDuplicates=Masquer les doublons

DuplicatesPage.Group.Repeat=Reproduction(Répéter)

DuplicatesPage.Group.RepeatonPage=Reproduction(Répéter sur la page)

DuplicatesPage.Label.RepeatInfo=Masquer les valeurs qui sont identiques à la ligne de données précédente.

DuplicatesPage.Label.RepeatOnPage.1=Masquer les valeurs qui sont identiques à la ligne précédente,

DuplicatesPage.Label.RepeatOnPage.2=\\ \\ mais toujours afficher la valeur en haut de chaque nouvelle page.

DuplicatesPage.Label.ResetonGroup=Réinitialiser au groupe suivant

DuplicatesPage.Radio.Repeat=Répéter

DuplicatesPage.Radio.RepeatonPage=Répéter sur la page

###########################################################
#EditAction properties
EditAction.text=&Modifier

###########################################################
#EditGroupAction properties
EditGroupAction.stackMsg.editGroup=Modifier le groupe

###########################################################
#EditGroupAction properties
InsertGroupHeaderFooterAction.stackMsg.editGroup=Insérer En-tête/Pied de page de groupe

InsertGroupHeaderFooterAction.Text.Header=En-tête

InsertGroupHeaderFooterAction.Text.Footer=Pied de page

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.stackMsg.deleteGroup=Supprimer le groupe

###########################################################
#EditLabelAction properties
EditLabelAction.text=&Modifier

EditorDragGuidePolicy.pixels.label=pixels

EditorGuide.bottom.label=Bas :

EditorGuide.left.label=Gauche :

EditorGuide.right.label=Droite :

EditorGuide.top.label=Haut :

###########################################################
#Element properties
Element.AbstractScalarParameter.dataType=Type de &données :

Element.AutoText.type=Type :

Element.Cell.colSpan=Plage de colonne :

Element.Cell.column=Colonne :

Element.Cell.drop=&Ignorer :

Element.Cell.height=Hauteur :

Element.Cell.rowSpan=Plage de ligne :

Element.Cell.slot.content=Contenu :

Element.Cell.verticalAlign=Alignement vertical :

Element.Cell.width=Largeur :

Element.Column.alignment=Alignement :

Element.Column.repeat=Répéter :

Element.Column.width=&Largeur :

Element.Column.suppressDuplicates=Supprimer les doublons

Element.Container.slot.reportItems=Eléments d'état :

Element.Data.action=Action :

Element.Data.helpText=Texte d'aide :

Element.Data.helpTextID=Clé du texte d'aide

Element.TextData.valueExpr=Exp&ression de valeur :

Element.DataSet.afterClose.toolTip=Commande appelée juste après la fermeture du jeu de données.

Element.DataSet.afterClose=Après la fermeture :

Element.DataSet.afterOpen.toolTip=Commande appelée juste après l'ouverture du jeu de données.

Element.DataSet.afterOpen=Après l'ouverture :

Element.DataSet.beforeClose.toolTip=Commande appelée juste avant la fermeture du jeu de données.

Element.DataSet.beforeClose=Avant la fermeture :

Element.DataSet.beforeOpen.toolTip=Commande appelée juste avant l'ouverture du jeu de données.

Element.DataSet.beforeOpen=Avant l'ouverture :

Element.DataSet.cachedMetaData=Métadonnées en cache :

Element.DataSet.columnHints=Astuces de colonne :

Element.DataSet.computedColumns=Colonnes de calcul :

Element.DataSet.dataSource=Source de données :

Element.DataSet.filter=Filtre :

Element.DataSet.inputParameters=Paramètres d'entrée :

Element.DataSet.onFetch.toolTip=Commande appelée juste avant la récupération d'une ligne de données du jeu de données.

Element.DataSet.onFetch=A la récupération :

Element.DataSet.outputParameters=Paramètres de sortie :

Element.DataSet.paramBindings=Liaisons de paramètres :

Element.DataSet.resultSet=Jeu de résultats :

Element.DataSource.afterClose.toolTip=Commande appelée juste après la fermeture de la source de données.

Element.DataSource.afterClose=Après la fermeture :

Element.DataSource.afterOpen.toolTip=Commande appelée juste après l'ouverture de la source de données.

Element.DataSource.afterOpen=Après l'ouverture :

Element.DataSource.beforeClose.toolTip=Commande appelée juste avant la fermeture de la source de données.

Element.DataSource.beforeClose=Avant la fermeture :

Element.DataSource.beforeOpen.toolTip=Commande appelée juste avant l'ouverture de la source de données.

Element.DataSource.beforeOpen=Avant l'ouverture :

Element.ExtendedItem.extension=nom d'extension :

Element.ExtendedItem.extensionName=nom d'extension :

Element.FreeForm.slot.reportItems=Eléments d'état :

Element.GraphicMasterPage.columns=Colonnes :

Element.GraphicMasterPage.columnSpacing=Espacement des colonnes :

Element.GraphicMasterPage.slot.contents=Contenu :

Element.Grid.slot.columns=colonnes :

Element.Grid.slot.row=ligne :

Element.Image.action=Action :

Element.Image.altText=&Autre texte :

Element.Image.altTextID=Clé de texte facultative :

Element.Image.helpText=Texte d'aide :

Element.Image.helpTextID=Clé du texte d'aide

Element.Image.imageName=Nom de l'image :

Element.Image.scale=échelle :

Element.Image.size=taille :

Element.Image.source=Source :

Element.Image.typeExpr=Expression de type :

Element.Image.uri=uri:
Element.Image.valueExpr=Expression de valeur :

Element.JdbcDataSource.dataSource=Source de données :

Element.JdbcDataSource.driver=Pilote :

Element.JdbcDataSource.password=Mot de passe :

Element.JdbcDataSource.url=Url:
Element.JdbcDataSource.userName=Nom d'utilisateur :

Element.JdbcSelectDataSet.getStatement.toolTip=Renvoie une instruction SQL personnalisée pour la requête.

Element.JdbcSelectDataSet.getStatement=Obtenir l'instruction :

Element.JdbcSelectDataSet.statement=Instruction :

Element.Label.action=Action :

Element.Label.helpText=Texte d'aide :

Element.Label.helpTextID=Clé du texte d'aide

Element.Label.text=Texte :

Element.Label.textID=Clé de &texte :

Element.Line.orientation=orientation :

Element.List.slot.columnHeader=columnHeader:
Element.List.slot.detail=détails :

Element.List.slot.footer=pied de page :

Element.List.slot.group=groupe :

Element.List.slot.header=en-tête :

Element.ListGroup.slot.footer=pied de page :

Element.ListGroup.slot.header=en-tête :

Element.Listing.filter=Filtre :

Element.Listing.onFinish.toolTip=Commande appelée après la lecture de la dernière ligne du jeu de données pour cet élément, mais avant la création de la bande du pied de page.

Element.Listing.onFinish=A la fin :

Element.Listing.onRow.toolTip=Commande appelée pour chaque ligne extraite du jeu de données pour cet élément, mais avant la création de tout contenu pour cette ligne.

Element.Listing.onRow=A la ligne :

Element.Listing.onStart.toolTip=Commande appelée après l'ouverture du jeu de données, mais avant la création de la bande d'en-tête et avant l'extraction de la première ligne du jeu de données pour cet élément.

Element.Listing.onStart=Au début :

Element.Listing.pageBreakInterval=Intervalle de sau&t de page :

Element.Listing.sorting=Tri :

Element.ListingGroup.filter=Filtre :

Element.ListingGroup.groupName=Nom

Element.ListingGroup.interval=Intervalle :

Element.ListingGroup.intervalRange=Plage d'intervalles :

Element.ListingGroup.keyExpr=Expression clé :

Element.ListingGroup.onFinish.toolTip=Commande appelée après la lecture de la dernière ligne du jeu de données pour cet élément, mais avant la création de la bande du pied de page.

Element.ListingGroup.onFinish=A la fin :

Element.ListingGroup.onRow.toolTip=Commande appelée pour chaque ligne extraite du jeu de données pour cet élément, mais avant la création de tout contenu pour la ligne.

Element.ListingGroup.onRow=A la ligne :

Element.ListingGroup.onStart.toolTip=Commande appelée après l'ouverture du jeu de données, mais avant la création de la bande d'en-tête et avant l'extraction de la première ligne du jeu de données pour cet élément.

Element.ListingGroup.onStart=Au début :

Element.ListingGroup.pageBreakAfter=Saut de page après :

Element.ListingGroup.pageBreakBefore=Saut de page avant :

Element.ListingGroup.sort=Trier :

Element.ListingGroup.sortDirection=Direction du tri :

Element.ListingGroup.toc=Table des matières :

Element.MasterPage.bottomMargin=&Marge inférieure :

Element.MasterPage.columns=Colonnes :

Element.MasterPage.columnSpacing=E&space :

Element.MasterPage.height=Haut&eur :

Element.MasterPage.leftMargin=Marge de &gauche :

Element.MasterPage.margin=Marge :

Element.MasterPage.orientation=O&rientation :

Element.MasterPage.rightMargin=Marge de d&roite :

Element.MasterPage.slot.contents=Contenu :

Element.MasterPage.topMargin=Marge &supérieure :

Element.MasterPage.type=T&ype :

Element.MasterPage.width=&Largeur :

Element.Module.author=A&uteur :

Element.Module.base=Base :

Element.Module.colorPalette=Palette de couleurs :

Element.Module.comments=&Commentaires :

Element.Module.configVars=Configurer les variables :

Element.Module.createdBy=&Créé par :

Element.Module.description=D&escription :

Element.Module.descriptionID=Clé de description :

Element.Module.helpGuide=Guide d'aide :

Element.Module.images=Images :

Element.Module.includeLibraries=Inclure les bibliothèques :

Element.Module.includeResource=Fichier de r&essources :

Element.Module.includeScripts=Inclure les scripts :

Element.Module.initialize.toolTip=Commande appelée au début de l'exécution de l'état.

Element.Module.initialize=Initialiser :

Element.LayoutModule.theme=T&hèmes :

Element.Module.title=T&itre :

Element.Module.titleID=Clé de titre :

Element.Module.units=Unité&s :

Element.MultiLineData.contentTypeExpr=Expression de type de contenu :

Element.MultiLineData.valueExpr=Expression de valeur :

Element.OdaDataSet.privateDriverDesignState=Etat de conception de pilote privé :

Element.OdaDataSet.privateDriverProperties=Propriétés de pilote privé :

Element.OdaDataSet.publicDriverProperties=Propriétés de pilote public :

Element.OdaDataSet.queryScript.toolTip=A définir :

Element.OdaDataSet.queryScript=Script :

Element.OdaDataSet.queryText=Requête :

Element.OdaDataSet.resultSetName=Nom de jeu de résultats :

Element.OdaDataSet.type=Type :

Element.OdaDataSource.driverName=Nom du pilote :

Element.OdaDataSource.privateDriverProperties=Propriétés de pilote privé :

Element.OdaDataSource.publicDriverProperties=Propriétés de pilote public :

Element.Parameter.helpText=Texte d'aide :

Element.Parameter.helpTextID=C&lé du texte d'aide :

Element.Parameter.hidden=Masqué :

Element.Parameter.promptTextID=C&lé du texte de l'invite :

Element.ParameterGroup.helpText=Texte d'aide :

Element.ParameterGroup.helpTextID=C&lé du texte d'aide :

Element.ParameterGroup.slot.parameters=Paramètres :

Element.ParameterGroup.startExpanded=Lancement développé :

Element.ParameterGroup.promptTextID=&Clé du texte de l'invite :

Element.ReportDesign.afterCloseDoc.toolTip=Commande appelée juste après la fermeture de l'état pendant le processus de génération.

Element.ReportDesign.layoutPreference=Préférence de mise en page :

Element.ReportDesign.afterCloseDoc=Après fermeture du document :

Element.ReportDesign.afterFactory.toolTip=Commande appelée à la fin du processus de génération, après fermeture de l'état (le cas échéant). Il s'agit de la dernière méthode appelée au cours du processus de génération.

Element.ReportDesign.afterFactory=Après génération :

Element.ReportDesign.afterOpenDoc.toolTip=Commande appelée juste après l'ouverture de l'état pendant le processus de génération.

Element.ReportDesign.afterOpenDoc=Après ouverture du document :

Element.ReportDesign.afterRender.toolTip=Commande appelée après le début d'une action chronologique de présentation.

Element.ReportDesign.afterRender=Après rendu :

Element.ReportDesign.beforeCloseDoc.toolTip=Commande appelée juste avant la fermeture de l'état pendant le processus de génération.

Element.ReportDesign.beforeCloseDoc=Avant fermeture du document :

Element.ReportDesign.beforeFactory.toolTip=Commande appelée au début du processus de génération après la méthode d'initialisation( ) mais avant l'ouverture de l'état (le cas échéant).

Element.ReportDesign.beforeFactory=Avant génération :

Element.ReportDesign.beforeOpenDoc.toolTip=Commande appelée juste avant l'ouverture de l'état pendant le processus de génération.

Element.ReportDesign.beforeOpenDoc=Avant l'ouverture du document :

Element.ReportDesign.beforeRender.toolTip=Commande appelée avant le début d'une action chronologique de présentation.

Element.ReportDesign.beforeRender=Avant rendu :

Element.ReportDesign.displayName=Nom d'afficha&ge :

Element.ReportDesign.eventHandlerClass=&Classe de gestionnaire d'événements :

Element.ReportDesign.iconFile=Mi&niature :

Element.ReportDesign.refreshRate=Fréquence d'actualisation :

Element.ReportDesign.bidiLayoutOrientation=O&rientation de l'état :

Element.ReportElement.comments=&Commentaires :

Element.ReportElement.Cube=&Données :

Element.ReportElement.customXml=XML personnalisé :

Element.ReportElement.displayName=Nom d'a&ffichage :

Element.ReportElement.displayNameID=Clé de nom d'affichage :

Element.ReportElement.extends=Etend :

Element.ReportElement.javaClass=Classe Java :

Element.ReportElement.name=&Nom :

Element.ReportElement.propertyMask=Masque de propriété :

Element.ReportElement.style=St&yle :

Element.ReportItem.bookmark=&Signet :

Element.ReportItem.dataSet=Jeu de données :

Element.ReportItem.height=&Hauteur :

Element.ReportItem.onCreate.toolTip=Action exécutée quand l'élément est créé pendant le processus de génération.

Element.ReportItem.onCreate=A la création :

Element.ReportItem.onRender.toolTip=Action exécutée quand l'élément est préparé pour le rendu pendant la phase de présentation.

Element.ReportItem.onRender=Au rendu :

Element.ReportItem.paramBindings=Liaisons de paramètres :

Element.ReportItem.toc=&Table des matières :

Element.ReportItem.visibility=Visibilité :

Element.ReportItem.width=&Largeur :

Element.ReportItem.x=X :

Element.ReportItem.y=Y :

Element.ReportItem.altText=Texte de remplacement

Element.ReportItem.altTextID=Clé de texte de remplacement

Element.Row.bookmark=Signet :

Element.Row.height=&Hauteur :

Element.Row.slot.cell=cellule :

Element.Row.visibility=Visibilité :

Element.ScalarParameter.alignment=Alignement :

Element.ScalarParameter.allowBlank=Autoriser une valeur vierge :

Element.ScalarParameter.allowNull=Autoriser une valeur nulle :

Element.ScalarParameter.concealValue=Masquer la valeur :

Element.ScalarParameter.controlType=Type de &contrôle :

Element.ScalarParameter.defaultValue=Valeur par défaut :

Element.ScalarParameter.dynamicList.dataSetName=Nom du jeu de données :

Element.ScalarParameter.dynamicList.labelExpr=Expression de l'étiquette :

Element.ScalarParameter.dynamicList.valueExpr=Expression de valeur :

Element.ScalarParameter.dynamicList=Liste dynamique :

Element.ScalarParameter.format=Format :

Element.ScalarParameter.selectionList.fixedOrder=Ordre fixe :

Element.ScalarParameter.selectionList.mustMatch=Doit correspondre :

Element.ScalarParameter.selectionList.selectionList=Liste de sélection :

Element.ScalarParameter.selectionList=Liste de sélection :

Element.ScalarParameter.promptTextID=&Clé du texte de l'invite :

Element.ScriptDataSet.close.toolTip=Ferme le jeu de données.

Element.ScriptDataSet.close=Fermer :

Element.ScriptDataSet.describe.toolTip=Décrit le jeu de résultats.

Element.ScriptDataSet.describe=Décrire :

Element.ScriptDataSet.fetch.toolTip=Crée, définit et renvoie un objet de type DataRow.

Element.ScriptDataSet.fetch=Récupérer :

Element.ScriptDataSet.open.toolTip=Ouvre le jeu de données.

Element.ScriptDataSet.open=Ouvrir :

Element.ScriptDataSource.close.toolTip=Supprime la connexion à la source de données.

Element.ScriptDataSource.close=Fermer :

Element.ScriptDataSource.open.toolTip=Etablit une connexion à la source de données.

Element.ScriptDataSource.open=Ouvrir :

Element.SimpleMasterPage.floatingFooter=&Pied de page flottant

Element.SimpleMasterPage.footerHeight=Hau&teur de pied de page :

Element.SimpleMasterPage.headerHeight=&Hauteur d'en-tête :

Element.SimpleMasterPage.showFooterOnLast=Afficher le &pied de page sur la dernière page

Element.SimpleMasterPage.showHeaderOnFirst=Afficher l'&en-tête sur la première page

Element.SimpleMasterPage.slot.pageFooter=Pied de page :

Element.SimpleMasterPage.slot.pageHeader=En-tête de page :

Element.Style.background.attachment=&Pièce jointe :

Element.Style.background.color=&Couleur d'arrière-plan :

Element.Style.background.image=&Image d'arrière-plan :

Element.Style.background.positionX=Position &horizontale :

Element.Style.background.positionY=Position &verticale :

Element.Style.background.repeat=&Répéter :

Element.Style.background=Arrière-plan :

Element.Style.border.bottom.color=Ba&s :

Element.Style.border.bottom.style=&Bas :

Element.Style.border.bottom.width=&Bas :

Element.Style.border.fillColor=Couleur de remplissage de bordure :

Element.Style.border.hmiddle.color=Couleur de milieu de bordure à l'horizontale :

Element.Style.border.hmiddle.style=Style de milieu de bordure à l'horizontale :

Element.Style.border.hmiddle.width=Largeur de milieu de bordure à l'horizontale :

Element.Style.border.left.color=Ga&uche :

Element.Style.border.left.style=&Gauche :

Element.Style.border.left.width=G&auche :

Element.Style.border.right.color=Dr&oite :

Element.Style.border.right.style=&Droite :

Element.Style.border.right.width=Dro&ite :

Element.Style.border.top.color=H&aut :

Element.Style.border.top.style=&Haut :

Element.Style.border.top.width=Ha&ut :

Element.Style.border.vmiddle.color=Couleur de milieu de bordure à la verticale :

Element.Style.border.vmiddle.style=Style de milieu de bordure à la verticale :

Element.Style.border.vmiddle.width=Largeur de milieu de bordure à la verticale :

Element.Style.border=Bordure :

Element.Style.box=Zone :

Element.Style.canShrink=Rétrécissement poss&ible

Element.Style.color=&Couleur :

Element.Style.display=Affic&hage :

Element.Style.font.family=&Police :

Element.Style.font.size=&Taille :

Element.Style.font.style=S&tyle :

Element.Style.font.textLineThrough=&Texte barré

Element.Style.font.textOverline=&Texte surligné

Element.Style.font.textUnderline=&Texte souligné

Element.Style.font.variant=Variante de police :

Element.Style.font.weight=&Epaisseur :

Element.Style.font=&Police :

Element.Style.format.dateTime=Date/heure :

Element.Style.format.number.align=Alignement des nombres formatés :

Element.Style.format.number=Numéro :

Element.Style.format.string=Chaîne :

Element.Style.format=Format :

Element.Style.highlight.rules=Mettre les règles en surbrillance :

Element.Style.highlight.testExpr=Mettre l'expression de test en surbrillance :

Element.Style.letterSpacing=Espacement des le&ttres :

Element.Style.lineHeight=Hauteur de lig&ne :

Element.Style.map.rules=Règles de mappage :

Element.Style.map.testExpr=Expression de test de mappage :

Element.Style.margin.bottom=&Bas :

Element.Style.margin.left=Gauch&e :

Element.Style.margin.right=Dro&ite :

Element.Style.margin.top=Hau&t :

Element.Style.masterPage=&Page maîtresse :

Element.Style.orphans=&Orphelins :

Element.Style.padding.bottom=&Bas :

Element.Style.padding.left=&Gauche :

Element.Style.padding.right=&Droite :

Element.Style.padding.top=&Haut :

Element.Style.padding=Remplissage :

Element.Style.pageBreakAfter=&Après :

Element.Style.pageBreakBefore=&Avant :

Element.Style.pageBreakInside=&A l'intérieur :

Element.Style.section=Section :

Element.Style.showIfBlank=&Afficher si vierge :

Element.Style.text.align=A&lignement du texte :

Element.Style.width=&Largeur :

Element.Style.height=&Hauteur :
#bidi_hcg
Element.Style.text.direction=Direction du texte &Bidi :

Element.Style.text.indent=Retrait du te&xte :

Element.Style.text.transform=T&ransformation du texte :

Element.Style.text=Texte :

Element.Style.verticalAlign=&Alignement vertical :

Element.Style.whiteSpace=&Espace :

Element.Style.widows=&Veuves :

Element.Style.wordSpacing=&Espacement des mots :

Element.Table.caption=Légende :

Element.Table.captionID=Clé de légende :

Element.Table.repeatHeader=Répéter l'en-tête :

Element.Table.slot.columnHeader=columnHeader:
Element.Table.slot.columns=colonnes :

Element.Table.slot.detail=détails :

Element.Table.slot.footer=pied de page :

Element.Table.slot.group=groupe :

Element.Table.slot.header=en-tête :

Element.TableGroup.slot.footer=pied de page :

Element.TableGroup.slot.header=en-tête :

Element.Text.content=Contenu :

Element.Text.contentID=&Clé de contenu :

Element.Text.contentType=Ty&pe de contenu :

Element.TemplateElement.name=&Nom

Element.Toc.Sytle=St&yle

Element.VariableElement.name=&Nom :

Element.VariableElement.type=&Type :

Element.VariableElement.value=&Valeur :

###########################################################
#EmbeddedImageNodeProvider properties
EmbeddedImageNodeProvider.action.New=Nouvelle image in&corporée

###########################################################
#EmbeddedImagesNodeProvider properties
EmbeddedImagesNodeProvider.action.New=Nouvelle image in&corporée

###########################################################
#ElementIdDescriptorProvider properties
ElementIdDescriptorProvider.Display.Element.ID=ID de l'&élément :

###########################################################
#EmptyEditPart properties
EmptyEditPart.Msg.EmptyLib=La bibliothèque ne contient aucun élément visuel. Pour ajouter des éléments visuels, faites glisser l'élément de la palette vers la structure de la bibliothèque ou utilisez le menu d'insertion dans la vue de contour.

EmptyEditPart.Msg.NonVisual=Elément non visuel sélectionné.

###########################################################
#error properties
error.duplicateName=Le nouveau nom est le doublon d'un nom existant dans le même espace de noms.

error.DataSource.emptyName=Le nom de la source de données ne peut pas être vide.

error.DataSet.emptyName=Le nom du jeu de données ne peut pas être vide.

error.OutputColumn.emptyName=Le nom de la colonne de sortie ne peut pas être vide.

error.invalidName=Le nom "{0}" n''est pas valide car il contient le caractère ".", "/", "\\", "!", ";", "," .

###########################################################
#ExceptionHandler properties
ExceptionHandler.Label.ErrorCode=Code d'erreur

ExceptionHandler.Label.ErrorMessage=Message d'erreur

ExceptionHandler.Label.PluginId=ID du plug-in :

ExceptionHandler.Label.PluginName=Nom du plug-in :

ExceptionHandler.Label.PluginProvider=Fournisseur du plug-in :

ExceptionHandler.Label.PluginVersion=Version :

ExceptionHandler.Label.UnknownPlugin=Plug-in inconnu

ExceptionHandler.Meesage.UnexceptedExceptionOccur=Une exception interne inattendue s'est produite. Pour plus de détails, consultez le fichier journal.

ExceptionHandler.Message.BirtExceptionOccur=Une exception BIRT s'est produite.

ExceptionHandler.Message.CausedBy=Cause {0}.

ExceptionHandler.Message.FileNotFound=Fichier introuvable :

ExceptionHandler.Message.OutOfMemory=Mémoire insuffisante.

ExceptionHandler.Message.PartInitError=Echec de l'initialisation.

ExceptionHandler.Message.SemanticFail=Echec de la vérification sémantique.

ExceptionHandler.Message.UnexceptedExceptionOccur=Une exception interne s'est produite. Pour plus de détails, consultez le fichier journal.

ExceptionHandler.Message.UnknownHost=Hôte inconnu :

ExceptionHandler.Title.Error=Erreur

ExceptionHandler.Title.FileNotFound=Fichier introuvable.

ExceptionHandler.Title.PartInitError=Erreur de l'initialisation.

ExceptionHandler.Title.UnknownHost=Hôte inconnu

ExceptionHandler.Meesage.ExceptionOccur=Une exception inattendue s'est produite. Reportez-vous au journal pour obtenir plus de détails.

###########################################################
#ExportAction properties
ExportAction.actionMsg.export=Exporter

###########################################################
#ExportToLibraryAction properties
ExportToLibraryAction.action.text=E&xporter vers la bibliothèque...

ExportToLibraryAction.Button.Cancel=Annuler

ExportToLibraryAction.Button.No=Non

ExportToLibraryAction.Button.Yes=Oui

ExportToLibraryAction.Dialog.Message=\nIl se peut que la bibliothèque cible dispose du même élément de modèle. Souhaitez-vous l'écraser ?

ExportToLibraryAction.Dialog.Title=Exporter l'élément

ExportToLibraryAction.Message.RememberDecision=Mémo&riser ma décision


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.action.text=E&xporter vers la bibliothèque...

ExportToCPSouceAction.action.text=Externaliser vers le profil de c&onnexion...


###########################################################
#LibraryExplorerTreeViewPage tooltips
LibraryElementsToolHandleExtends_message=L'élément contient des liaisons locales sans jeu de données explicite spécifié. Cela peut ralentir les performances lors de l'exécution. Souhaitez-vous déplacer les liaisons locales vers le parent pour améliorer les performances ?

LibraryElementsToolHandleExtends_question=Déplacer les liaisons locales vers le parent pour améliorer les performances ?

LibraryElementsToolHandleExtends_toggle=Mémo&riser ma décision

LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline=Glisser-déplacer vers la vue de contour

LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout=Glisser-déplacer vers l'éditeur de présentation

LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout=Glisser-déplacer vers la vue de contour ou l'éditeur de présentation

###########################################################
#ExpressionBuidler properties
ExpressionBuidler.Dialog.Title=Générateur d'expression (JavaScript)

ExpressionBuidler.Title=Générateur d'expression

ExpressionBuilder.tooltip.Sort=Trier

ExpressionBuilder.toolTipText.Button=Ouvrir le Générateur d'expression

###########################################################
#ExpressionBuilder properties
ExpressionBuilder.Label.Category=&Catégorie :

ExpressionBuilder.Label.Functions=Double-cliquer pour &insérer :

ExpressionBuilder.Label.Hint=Indication

ExpressionBuilder.Label.Operators=Opérateurs :

ExpressionBuilder.Label.SubCategory=&Sous-catégorie :

ExpressionBuilder.Message.Prompt=Entrez une expression dans le champ Expression. Parcourez la liste des objets disponibles et cliquez deux fois sur un objet pour le copier dans votre expression.

ExpressionBuilder.ToolTip=Ouvrir le Générateur d'expression

ExpressionBuilder.toolTipText.validate=Valider

ExpressionBuilder.Script.NoError=Aucune erreur de syntaxe n'a été trouvée dans le script actuel.

ExpressionBuilder.Script.Error=[Rang : {0}, Colonne : {1}] {2}

ExpressionBuilder.Script.Warning=Avertissement

ExpressionBuilder.Script.Confirm=\nLe script actuel comporte une erreur de syntaxe. Voulez-vous vraiment l'enregistrer ?

ExpressionBuilder.toolTipText.calendar=Calendrier

ExpressionBuilder.Calendar.Button.OK=OK

ExpressionButtonProvider.Constant=Constante

ExpressionButtonProvider.Javascript=Syntaxe JavaScript

###########################################################
#ExpressionPropertyDescriptor properties
ExpressionPropertyDescriptor.text.Edit=Générateur d'e&xpression...

###########################################################
#ExpressionProvider properties
ExpressionProvider.Category.Context=Contexte

ExpressionProvider.Category.BirtObjects=Fonctions BIRT

ExpressionProvider.Category.DataSets=Jeux de données disponibles

ExpressionProvider.Category.NativeObjects=Fonctions JavaScript natives

ExpressionProvider.Category.ColumnBinding=Liaisons de colonnes disponibles

ExpressionProvider.Category.Operators=Opérateurs

ExpressionProvider.Category.Parameters=Paramètres de l'état

ExpressionProvider.Category.DataCubes=Cube de données actuel

ExpressionProvider.Category.Variables=Variables

ExpressionProvider.Label.All=---Tout---

ExpressionProvider.Operator.Add=Ajoute deux valeurs ensemble.

ExpressionProvider.Operator.AddTo=Raccourci pour ajouter à la valeur actuelle.

ExpressionProvider.Operator.And=Compare deux expressions et renvoie une valeur true si les deux expressions sont vraies.

ExpressionProvider.Operator.Assign=Définit une valeur égale à l'autre.

ExpressionProvider.Operator.BitAnd=A & B : Renvoient la manipulation de bits ET le résultat de A et B.

ExpressionProvider.Operator.BitOr=A | B : Renvoient la manipulation de bits OU le résultat de A et B.

ExpressionProvider.Operator.Dec=Réduit la valeur et renvoie le résultat.

ExpressionProvider.Operator.DividingFrom=Raccourci pour diviser la valeur actuelle.

ExpressionProvider.Tooltip.ColumnBinding=Fait référence à

ExpressionProvider.Operator.Divides=Divise la valeur de gauche par celle de droite et renvoie le résultat.

ExpressionProvider.Operator.Equals=Renvoie une valeur Vrai (True) si les éléments sont identiques.

ExpressionProvider.Operator.Greater=A > B : renvoie une valeur Vrai (True) si A est supérieur à B.

ExpressionProvider.Operator.GreaterEquals=A > B : renvoie une valeur Vrai (True) si A est supérieur ou égal à B.

ExpressionProvider.Operator.Inc=Augmente la valeur et renvoie le résultat.

ExpressionProvider.Operator.LeftBracket=Parenthèse de gauche

ExpressionProvider.Operator.RightBracket=Parenthèse de droite

ExpressionProvider.Operator.Less=A < B : renvoie une valeur Vrai (True) si A est inférieur à B.

ExpressionProvider.Operator.LessEqual=A > B : renvoie une valeur Vrai (True) si A est inférieur ou égal à B.

ExpressionProvider.Operator.Mult=Multiplie deux valeurs.

ExpressionProvider.Operator.MultTo=Raccourci pour multiplier la valeur actuelle.

ExpressionProvider.Operator.Not=!A : Renvoie une valeur vraie sur A est faux ou une valeur fausse si A est vrai.

ExpressionProvider.Operator.NotEqual=A <> B : renvoie une valeur Vrai (True) si A n'est pas égal à B.

ExpressionProvider.Operator.Or=Compare deux expressions et renvoie une valeur true si l'une des expressions est vraie.

ExpressionProvider.Operator.ReturnDec=Renvoie la valeur, puis réduit la valeur.

ExpressionProvider.Operator.ReturnInc=Renvoie la valeur, puis augmente la valeur.

ExpressionProvider.Operator.Sub=Soustrait une valeur d'une autre.

ExpressionProvider.Operator.SubFrom=Raccourci pour soustraire de la valeur actuelle.

ExpressionProvider.Operators.Assignment=Attribution

ExpressionProvider.Operators.Comparison=Comparaison

ExpressionProvider.Operators.Computational=Calcul

ExpressionProvider.Operators.Logical=Logique

ExpressionProvider.EditBindings=Modification des liaisons...

ExpressionProvider.Expression.RowNumName=RowNum

ExpressionProvider.Expression.RowNumTooltip=Expression qui renvoie le nombre de lignes actuel.

ExpressionProvider.Variables=Variables

ExpressionProvider.Variables.Report=Variables de l'état

ExpressionProvider.Variables.Page=Variables de la page

ExpressionTreeSupport.Category.Global=Global

###########################################################
#ExpressionValueCellEditor properties
ExpressionValueCellEditor.buildExpressionAction=<Générer une expression...>

ExpressionValueCellEditor.selectValueAction=<Sélectionner une valeur...>

ExpressionValueCellEditor.title=Sélectionner une valeur

###########################################################
#ExtendedEditPart properties
ExtendedEditPart.edit=Modifier {0}

###########################################################
#ExtendedItemPageGenerator properties
ExtendedItemPageGenerator.List.Bookmark=Signet

ExtendedItemPageGenerator.List.TOC=Table des matières

###########################################################
#FilterHandleProvider properties
FilterHandleProvider.ColumnName.Value=Valeur

FilterHandleProvider.Label.Filterby=Filtrer par :

FilterHandleProvider.Modify.SaveValues=Enregistrer les valeurs

###########################################################
#filters properties
filters.empty.columnName=L'expression ne peut pas être vide.

filters.error.title=Entrée non valide.

filters.operators.any=est indifféremment

filters.operators.between=Est compris entre

filters.operators.bottomN=(n) éléments du bas

filters.operators.bottomPercent=(n) pourcentage du bas

filters.operators.equal=est égal à

filters.operators.false=est faux

filters.operators.greaterThan=est supérieur à

filters.operators.greaterThanEqual=est supérieur ou égal à

filters.operators.lessThan=est inférieur à

filters.operators.lessThanEqual=est inférieur ou égal à

filters.operators.like=est identique à

filters.operators.notBetween=n'est pas compris entre

filters.operators.notEqual=n'est pas égal à

filters.operators.notNull=n'est pas nul

filters.operators.null=est nul

filters.operators.topN=fait partie des (n) premiers éléments

filters.operators.topPercent=fait partie des (n) premiers pourcentages

filters.operators.true=est vrai

filters.prompt.new=<Nouveau filtre ici...>

###########################################################
#FixTableLayoutCalculator properties
FixTableLayoutCalculator.Error.CannotParse=Impossible d'analyser la valeur d'entrée

###########################################################
#FlowMoveChildCommand properties
FlowMoveChildCommand.transLabel.moveElement=Déplacer l'élément

###########################################################
#FontPreferencePage properties
FontPreferencePage.displayname.Title=Police

FontPreferencePage.label.fontDecoration=Décoration :

FontPreferencePage.text.Preview=Aperçu de l'exemple

FontPreferencePage.text.PreviewContent=AaBbYyZz

###########################################################
#FontSizeBuilder properties
FontSizeBuilder.displayname.Auto=Auto

###########################################################
#FormatBuilder properties
FormatAdapter.DisplayName.None=Auto

FormatBuilder.Title=Générateur de format

###########################################################
#FormatDateTimeAttributePage properties
FormatDateTimeAttributePage.Trans.SetDateTimeFormat = Définissez le format Date/Heure.

###########################################################
#FormatDateTimePage properties
FormatDateTimePage.label.custom.settings.label=Exem&ples de formats (cliquez sur l'un d'entre eux pour le copier ci-dessous et le modifier) :

FormatDateTimePage.label.custom.settings=Paramètres personnalisés

FormatDateTimePage.label.format.code=&Code de format :

FormatDateTimePage.label.format.page=&Format :

FormatDateTimePage.label.format.locale=&Local :

FormatDateTimePage.label.general.preview.group=Aperçu au format

###########################################################
# LOCALE SPECIAL:
# The datetime pattern string in this text should be formatted locale-dependent if needed
FormatDateTimePage.label.guide.format=dd/MM/yyyy hh:mm:ss a

FormatDateTimePage.label.guide.text=Saisissez les valeurs de date selon le modèle suivant : dd/MM/yyyy hh:mm:ss

FormatDateTimePage.label.preview.dateTime=A&perçu de date/heure :

FormatDateTimePage.label.preview.group=Aperçu

FormatDateTimePage.label.preview.label=Aperçu au format :

FormatDateTimePage.label.table.column.format.name=Format exemple

FormatDateTimePage.label.table.column.format.result=Exemple de résultat de format

FormatDateTimePage.label.table.column.format.code=Exemple de code

FormatDateTimePage.preview.invalid.dateTime=Format de date/heure incorrect pour l'aperçu

FormatDateTimePage.preview.invalid.formatCode=Code de format incorrect

###########################################################
#FormatDateTimePreferencePage properties
FormatDateTimePreferencePage.formatDateTime.title=Date/heure

###########################################################
#FormatNumberAttributePage properties
FormatNumberAttributePage.Trans.SetNumberFormat = Définissez le format des nombres.

###########################################################
#FormatNumberPage properties
FormatNumberPage.currency.symbol.none=Aucun symbole

FormatNumberPage.label.currency.settings=Paramètres d'unité monétaire

FormatNumberPage.label.custom.preview.group=Aperçu

FormatNumberPage.label.custom.preview.label=Aperçu au format :

FormatNumberPage.label.custom.settings.lable=Exem&ples de formats (cliquez sur l'un d'entre eux pour le copier ci-dessous et le modifier) :

FormatNumberPage.label.custom.settings=Paramètres personnalisés

FormatNumberPage.label.decimal.places=Emplacement des &décimales :

FormatNumberPage.label.rounding.mode=&Mode Arrondi :

FormatNumberPage.label.fixed.settings=Paramètres fixés

FormatNumberPage.label.format.code=&Code de format :

FormatNumberPage.label.format.number.page=&Format :

FormatNumberPage.label.format.number.locale=&Local :

FormatNumberPage.label.general.preview.group=Aperçu au format

FormatNumberPage.label.negative.numbers=&Nombres négatifs :

FormatNumberPage.label.percent.settings=Paramètres de pourcentage

FormatNumberPage.label.preview.number=A&perçu du nombre :

FormatNumberPage.label.scientific.settings=Paramètres scientifiques

FormatNumberPage.label.symbol.position=Position du s&ymbole :

FormatNumberPage.label.symbol=Sym&bole :

FormatNumberPage.label.table.column.format.code=Format exemple

FormatNumberPage.label.table.column.format.result=Exemple de résultat de format

FormatNumberPage.label.use1000sSeparator=Utiliser un séparateur de mi&lliers :

FormatNumberPage.label.useLeadingZero=Utiliser un zéro à gauche

FormatNumberPage.label.useSymbolSpace=&Utiliser l'espace symbole

FormatNumberPage.preview.invalidFormatCode=Code de format incorrect

FormatNumberPage.preview.invalidNumber=Format de nombre incorrect pour l'aperçu

FormatNumberPage.symblePos.after=Après

FormatNumberPage.symblePos.before=Avant

FormatNumberPage.roundingMode.halfUp=Moitié supérieure

FormatNumberPage.roundingMode.halfDown=Moitié inférieure

FormatNumberPage.roundingMode.halfEven=Moitié au plus près

FormatNumberPage.roundingMode.up=Vers le haut

FormatNumberPage.roundingMode.down=Vers le bas

FormatNumberPage.roundingMode.ceiling=Plafond

FormatNumberPage.roundingMode.floor=Base

FormatNumberPage.roundingMode.unnecessary=Inutile(s)

###########################################################
#FormatNumberPreferencePage properties
FormatNumberPreferencePage.formatNumber.title=Nombre

###########################################################
#FormatPreferencePage properties
FormatPreferencePage.displayname.Title=Format

###########################################################
#FormatStringAttributePage properties
FormatStringAttributePage.Trans.SetStringFormat = Définissez le format de chaîne.

###########################################################
#FormatStringPage properties
FormatStringPage.default.preview.text=Ma chaîne

FormatStringPage.label.custom.preview.label=Aperçu au format :

FormatStringPage.label.custom.settings.label=Exem&ples de formats (cliquez sur l'un d'entre eux pour le copier ci-dessous et le modifier) :

FormatStringPage.label.customSettings=Paramètres personnalisés

FormatStringPage.label.format.code=&Code de format :

FormatStringPage.label.formatStringAs=&Format :

FormatStringPage.label.locale=&Local :

FormatStringPage.label.preview.group=Aperçu

FormatStringPage.label.preview.string=Aperçu de la chaî&ne :

FormatStringPage.label.previewWithFormat=Aperçu au format

FormatStringPage.label.table.collumn.exampleFormatCode=Format exemple

FormatStringPage.label.table.collumn.exampleFormatResult=Exemple de résultat de format

FormatStringPage.previewText.invalidFormatCode=Code de format incorrect

###########################################################
#FormatStringPreferencePage properties
FormatStringPreferencePage.formatString.title=Chaîne

###########################################################
#FormPage properties
FormPage.Button.Add=&Ajouter

FormPage.Button.AddWithDialog=&Ajouter...

FormPage.Button.AddWithDialog.PROP=&Ajouter un fichier...

FormPage.Button.AddWithDialog.JAR=Ajouter un fi&chier...

FormPage.Button.AddWithDialog.JS=Ajouter un fichier&...

FormPage.Button.Add.AggregateOn=Ajouter l'a&grégation

FormPage.Button.AddWithDialog.AggregateOn=Ajouter l'a&grégation...

FormPage.Button.Delete=&Supprimer

FormPage.Button.DeleteFile=&Supprimer un fichier

FormPage.Button.DeleteFile.Alt1=Supprimer un f&ichier

FormPage.Button.DeleteFile.Alt2=Supprimer un &fichier

FormPage.Button.Down=Déplacer vers le ba&s

FormPage.Button.Down.Alt1=Déplacer vers le b&as

FormPage.Button.Down.Alt2=&Déplacer vers le bas

FormPage.Button.Edit=&Modifier

FormPage.Button.EditWithDialog=&Modifier...

FormPage.Button.Up=Déplacer vers le &haut

FormPage.Button.Up.Alt1=Déplacer vers le ha&ut

FormPage.Button.Up.Alt2=Déplacer v&ers le haut

FormPage.Button.Binding.Refresh=Ac&tualiser

FormPage.Button.Binding.Clear=Effa&cer

FormPage.Menu.ModifyProperty=Modifier la propriété

FormPage.toolTipText.Up=Déplacer vers le haut

FormPage.toolTipText.Down=Déplacer vers le bas
###########################################################
#GeneralInsertMenuAction properties
GeneralInsertMenuAction.text.Data=&Données

GeneralInsertMenuAction.text.DynamicText=Texte d&ynamique

GeneralInsertMenuAction.text.Grid=&Grille

GeneralInsertMenuAction.text.Image=&Image

GeneralInsertMenuAction.text.Label=&Etiquette

GeneralInsertMenuAction.text.List=&Liste

GeneralInsertMenuAction.text.Table=Ta&ble

GeneralInsertMenuAction.text.Text=&Texte

###########################################################
#GeneralPreferencePage properties
GeneralPreferencePage.errorMsg.duplicate.styleName=Nom de style en double

GeneralPreferencePage.label.styleNameDuplicate=Le nom de style "{0}" existe déjà, choisissez un autre nom.

GeneralPreferencePage.label.styleNamePredefind=Le nom de style "{0}" est prédéfini, choisissez un autre nom.

GeneralPreferencePage.label.customStyle=&Style personnalisé

GeneralPreferencePage.Label.CustomStyle=Nom :

GeneralPreferencePage.Label.Note=REMARQUE : * appliquez les propriétés de style à un type spécifique d'élément d'état.

GeneralPreferencePage.Label.PreDefinedStyle=Sélectionner élément d'état :

GeneralPreferencePage.label.predefinedStyle=Style &prédéfini *

GeneralPreferencePage.label.nameEmpty=Nom de style vide non autorisé.

###########################################################
#GridEditPart properties
GraphicalEditorWithFlyoutPalette_Redo.Text0=&Rétablir

GraphicalEditorWithFlyoutPalette_Redo.Text1=&Rétablir {0}

GraphicalEditorWithFlyoutPalette_Redo.ToolTip0=Rétablir

GraphicalEditorWithFlyoutPalette_Redo.ToolTip1=Rétablir {0}

GraphicalEditorWithFlyoutPalette_Undo.Text0=&Annuler

GraphicalEditorWithFlyoutPalette_Undo.Text1=Ann&uler {0}

GraphicalEditorWithFlyoutPalette_Undo.ToolTip0=Annuler

GraphicalEditorWithFlyoutPalette_Undo.ToolTip1=Annuler {0}

GridEditPart.GUIDEHANDLE_TEXT = Grille

###########################################################
#GridFigure properties
GridFigure.BORDER_TEXT=Grille

###########################################################
#GridPageGenerator properties
GridPageGenerator.List.Bookmark=Signet

GridPageGenerator.List.Borders=Bordure

GridPageGenerator.List.Font=Police

GridPageGenerator.List.General=Général

GridPageGenerator.List.Margin=Marge

GridPageGenerator.List.Section=Saut de page

GridPageGenerator.List.TOC=Table des matières

GridPageGenerator.List.Visibility=Visibilité

###########################################################
#GridProvider properties
GridProvider.action.text=In&sérer une ligne

###########################################################
#GroupDialog properties
GroupDialog.Dialog.GroupDetail=Détails du groupe

GroupDialog.Label.Bookmark=&Signet :

GroupDialog.Dialog.TOCGroup=Table des matières

GroupDialog.Dialog.TOC=Express&ion d'élément :

GroupDialog.Dialog.TOCStyle=St&yle :

GroupDialog.DialogTitle.NoData=Echec de l'initialisation

GroupDialog.DialogTitle.SemanticCheckFailed=Echec de la vérification sémantique

GroupDialog.ErrorMesaage.IllegalGroup=Ce groupe est incorrect.

GroupDialog.ErrorMessage.NoData=Aucune ligne de données disponible

GroupDialog.Label.FilterSorting=Filtres et tri

GroupDialog.Label.GroupOn=Re&grouper :

GroupDialog.Label.HeaderFooter=En-tête et pied de page

GroupDialog.Label.IncludeFooter=&Inclure un pied de page de groupe

GroupDialog.Label.IncludeHeader=&Inclure un en-tête de groupe

GroupDialog.Label.Interval=Inter&valle :

GroupDialog.PageBreakBefore=Av&ant :

GroupDialog.PageBreakAfter=A&près :

GroupDialog.PageBreakInside=&A l'intérieur :

GroupDialog.Label.IntervalBase=Utiliser une valeur de base fi&xe pour l'intervalle :

GroupDialog.Label.Name=No&m :

GroupDialog.Label.Range=Gamm&e :

GroupDialog.Tab.Binding=Liaisons de données

GroupDialog.Tab.Filter=Filtres

GroupDialog.Tab.Sorting=Tri en cours

GroupDialog.RepeatHeader=Ré&péter l'en-tête

GroupDialog.Title.Edit=Modifier le groupe

GroupDialog.Title.New=Nouveau groupe

GroupDialog.PageBreak=Saut de page

GroupDialog.buttion.HideDetail=Mas&quer les détails

GroupDialog.toolTipText.openExprButton=Ouvrir le Générateur d'expression

GroupDialog.Button.Ascending=Croi&ssant

GroupDialog.Button.Descending=Dé&croissant
###########################################################
#GroupHandleProvider properties
GroupHandleProvider.Label.Groups=Liste de groupe :

###########################################################
#GroupProvider properties
GroupProvider.action.text=&Modifier le groupe

###########################################################
#GuiExtensionManager properties
GuiExtensionManager.label.name=Elément étendu

GuiExtensionManager.tooltip.insert=Insérer {0}

###########################################################
#HighlightHandleProvider properties
HighlightHandleProvider.text.Preview=AaBbCcYyZz (taille de police =

###########################################################
#HighlightRuleBuilderDialog properties
HightlightRuleBuilder.button.add=&Ajouter

HightlightRuleBuilder.button.add.tooltip=Ajouter la valeur à la liste

HightlightRuleBuilder.button.delete=Suppri&mer

HightlightRuleBuilder.button.delete.tooltip=Supprimer la valeur sélectionnée de la liste

HightlightRuleBuilder.button.deleteall=Supprimer t&out

HightlightRuleBuilder.button.deleteall.tooltip=Supprimer toutes les valeurs de la liste

HightlightRuleBuilder.button.edit=Modifi&er...

HightlightRuleBuilder.button.edit.tooltip=Modifier la valeur sélectionnée dans la liste

HightlightRuleBuilder.label.value=&Valeur :

HightlightRuleBuilder.list.item1=Valeur

HightlightRuleBuilder.MsgDlg.Msg=Avertissement

HightlightRuleBuilder.MsgDlg.Title=La valeur ne peut pas être vierge.

HightlightRuleBuilder.text.AND=ET

HighlightRuleBuilderDialog.choice.ValueOfThisDataItem=Valeur de l'élément de données

HighlightRuleBuilderDialog.text.AND=ET

HighlightRuleBuilderDialog.text.Any=est indifféremment

HighlightRuleBuilderDialog.text.BackgroundColor=&Couleur d'arrière-plan :

HighlightRuleBuilderDialog.text.Between=Est compris entre

HighlightRuleBuilderDialog.text.Color=&Couleur :

HighlightRuleBuilderDialog.text.Default=Auto

HighlightRuleBuilderDialog.text.ET=est égal à

HighlightRuleBuilderDialog.text.False=est faux

HighlightRuleBuilderDialog.text.Font=&Police :

HighlightRuleBuilderDialog.text.GE=est supérieur ou égal à

HighlightRuleBuilderDialog.text.GT=est supérieur à

HighlightRuleBuilderDialog.text.LE=est inférieur ou égal à

HighlightRuleBuilderDialog.text.Like=est identique à

HighlightRuleBuilderDialog.text.LT=est inférieur à

HighlightRuleBuilderDialog.text.NE=n'est pas égal à

HighlightRuleBuilderDialog.text.NotBetween=n'est pas compris entre

HighlightRuleBuilderDialog.text.NotNull=n'est pas nul

HighlightRuleBuilderDialog.text.Null=est nul

HighlightRuleBuilderDialog.text.Preview=Aperçu :

HighlightRuleBuilderDialog.text.PreviewContent=AaBbCcYyZz
HighlightRuleBuilderDialog.text.Size=&Taille :

HighlightRuleBuilderDialog.text.Title=Mise en surbrillance

HighlightRuleBuilderDialog.text.True=est vrai

HighlightRuleBuilderDialog.tooltip.ExpBuilder=Ouvrir le Générateur d'expression

HighlightRuleBuilderDialog.tooltip.Bold=Gras

HighlightRuleBuilderDialog.tooltip.Italic=Italique

HighlightRuleBuilderDialog.tooltip.Underline=Texte souligné

HighlightRuleBuilderDialog.tooltip.Text_Line_Through=Ligne de texte barré

HighlightRuleBuilderDialog.text.applyStyle=Appliquer le st&yle :

HighlightRuleBuilderDialog.displayText.None=Aucun

HighlightRuleBuilderDialog.text.Description=Appliquer un formatage conditionnel dans {0}. Spécifier l'expression conditionnelle et le format.\nLe format sera appliqué si l'expression est True.

HighlightRuleBuilderDialog.text.Description.Element.Table=table

HighlightRuleBuilderDialog.text.Description.Element.Grid=grille

HighlightRuleBuilderDialog.text.Description.Element.List=liste

HighlightRuleBuilderDialog.text.Description.Element.Row=ligne

HighlightRuleBuilderDialog.text.Description.Element.Column=colonne

HighlightRuleBuilderDialog.text.Description.Element.DataItem=élément de données

HighlightRuleBuilderDialog.text.Description.Element.Cell=cellule

HighlightRuleBuilderDialog.text.Group.Condition=Con&dition

HighlightRuleBuilderDialog.text.Group.Format=Format

HighlightRuleBuilderDialog.text.Description.Element.ReportElement=élément d'état

###########################################################
#HighlightsPage properties
HighlightsPage.Button.Add=&Ajouter...

HighlightsPage.Button.Delete=&Supprimer

HighlightsPage.Dialog.EditHighlight=Modifier la mise en surbrillance

HighlightsPage.Dialog.NewHighlight=Nouvelle mise en surbrillance

HighlightsPage.Label.Highlights=Liste de surbrillance :

HighlightsPage.TableColumn.Condition=Condition

HighlightsPage.TableColumn.Preview=Aperçu :

HighlightsPage.trans.Add=Ajouter la règle de surbrillance

HighlightsPage.trans.Delete=Supprimer la règle de surbrillance

HighlightsPage.trans.Edit=Modifier la règle de surbrillance

HighlightsPage.trans.MoveDown=Déplacer la règle de surbrillance vers le bas

HighlightsPage.trans.MoveUp=Déplacer la règle de surbrillance vers le haut

HighlightsPage.toolTipText.MoveDown=Déplacer la règle de surbrillance vers le bas

HighlightsPage.toolTipText.MoveUp=Déplacer la règle de surbrillance vers le haut

HighlightsPage.label.duplicate=Dupli&quer

HighlightsPage.toolTipText.duplicate=Dupliquer la règle de surbrillance

HighlightsPage.trans.Duplicate=Dupliquer la règle de surbrillance

###########################################################
#HighlightsPreferencePage properties
HighlightsPreferencePage.displayname.Condition=Condition

HighlightsPreferencePage.displayname.Preview=Aperçu

HighlightsPreferencePage.displayname.Title=Surbrillances

HighlightsPreferencePage.text.Add=&Ajouter...

HighlightsPreferencePage.text.Delete=&Supprimer

HighlightsPreferencePage.text.Description=Liste de surbrillance :

HighlightsPreferencePage.text.Edit=&Modifier...

HighlightsPreferencePage.toolTipText.Up=Déplacer la mise en surbrillance vers le haut

HighlightsPreferencePage.toolTipText.Down=Déplacer la mise en surbrillance vers le bas

HighlightsPreferencePage.text.Duplicate=Du&pliquer

HighlightsPreferencePage.toolTipText.Duplicate=Dupliquer la mise en surbrillance


###########################################################
#HyperlinkBuilder properties
HyperlinkBuilder.Column.Parameters=Paramètres

HyperlinkBuilder.DrillThrough.SelectTargetReport=Sélectionner un état cible

HyperlinkBuilder.DrillThrough.SelectTargetAnchor=Sélectionner une ancre cible

HyperlinkBuilder.DrillThrough.CreateLinkExpr=Créer une expression de lien correspondant au signet cible

HyperlinkBuilder.DrillThrough.ShowTargetReport=Afficher un état cible dans

HyperlinkBuilder.DrillThrough.SelectFormat=Sélectionner un format pour l'état cible

HyperlinkBuilder.DrillThrough.Tooltip=Info-bulle

HyperlinkBuilder.Step.1=Etape 1 :

HyperlinkBuilder.Step.2=Etape 2 :

HyperlinkBuilder.Step.3=Etape 3 :

HyperlinkBuilder.Step.4=Etape 4 :

HyperlinkBuilder.Step.5=Etape 5 :

HyperlinkBuilder.Step.6=Etape 6 :

HyperlinkBuilder.ParameterRequired=Obligatoire

HyperlinkBuilder.ParamterBuilder.Add.Title=Ajouter un paramètre de lien hypertexte

HyperlinkBuilder.ParamterBuilder.Edit.Title=Modifier un paramètre de lien hypertexte

HyperlinkBuilder.Column.DataType=Type de données

HyperlinkBuilder.Column.Values=Valeurs

HyperlinkBuilder.DialogTitle=Options de lien hypertexte

HyperlinkBuilder.ErrorMessage.InvalidReport=Impossible de résoudre le modèle d'état cible ; les informations du paramètre prédéfini ne seront pas disponibles.

HyperlinkBuilder.ErrorMessage.ReportReqired=Le fichier d'état doit être spécifié.

HyperlinkBuilder.DrillThroughTargetBookmark=&Signet cible

HyperlinkBuilder.DrillThroughLinkExpression=&Expression de lien :

HyperlinkBuilder.Label.Bookmark=&Signet :

HyperlinkBuilder.Label.Format=Format :

HyperlinkBuilder.Label.LinkedExpression=&Expression liée :

HyperlinkBuilder.DrillThroughTargetToc=Entrée de la ta&ble des matières dans l'état cible

HyperlinkBuilder.DrillThroughSameFrame=&Même cadre

HyperlinkBuilder.DrillThroughNewWindow=&Nouvelle fenêtre

HyperlinkBuilder.DrillThroughWholePage=Page &entière

HyperlinkBuilder.DrillThroughParentFrame=Cadre &parent

HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName=Nom de paramètre en double

HyperlinkBuilder.Label.Location=Emp&lacement :

HyperlinkBuilder.Label.Location.hint=(Exemples : http://host/path, folder/file)

HyperlinkBuilder.Label.Parameters=Paramètres de l'état :

HyperlinkBuilder.Label.Report=Etat :

HyperlinkBuilder.Label.SelectType=Sélectionner un type de lien hypertexte :

HyperlinkBuilder.DrillThroughHtml=HTML
HyperlinkBuilder.Label.Target=&Cible :

HyperlinkBuilder.Label.Tooltip=&Info-bulle :

HyperlinkBuilder.Radio.Bookmark=Si&gnet interne

HyperlinkBuilder.DrillThroughPdf=PDF
HyperlinkBuilder.Radio.DrillThrough=&Extraire

HyperlinkBuilder.ReportDesignButton=&Modèle d'état :

HyperlinkBuilder.ReportDocumentButton=Do&cument d'état :

HyperlinkBuilder.Radio.None=Auc&un lien

HyperlinkBuilder.Radio.Uri=&URI
HyperlinkBuilder.BrowseForFile=Rechercher le fichier

HyperlinkBuilder.Button.AddParam=&Ajouter...

HyperlinkBuilder.Button.EditParam=Modifier&...

HyperlinkBuilder.Button.RemoveAllParam=Supprimer tou&t

HyperlinkBuilder.Button.RemoveParam=Suppri&mer

HyperlinkBuilder.OpenExpression=Ouvrir le Générateur d'expression

HyperlinkBuilder.FileNameError.Title=Erreur

HyperlinkBuilder.FileNameError.Message=Le fichier sélectionné n'est pas valide.

HyperlinkBuilder.TargetReportFormat=&Formatage de l'état cible dans :

HyperlinkParameterBuilder.Label.DataType=Type de données :

HyperlinkParameterBuilder.Label.Parameter=&Paramètre :

HyperlinkParameterBuilder.Label.Value=&Valeur :

HyperlinkParameterBuilder.Lable.Required=Obligatoire :

HyperlinkParameterBuilder.Required.Choice.No=Non

HyperlinkParameterBuilder.Required.Choice.Yes=Oui

###########################################################
#HyperLinkPage properties
HyperLinkPage.Label.LnikTo=&Lien vers :

HyperLinkPage.Label.None=Aucun

HyperLinkPage.Menu.Save=Modifier le lien hypertexte

HyperLinkPage.Button.Text=Mo&difier...

HyperLinkPage.toolTipText.Button=Ouvrir les options de lien hypertexte

###########################################################
#I18nPage properties
I18nPage.text.Note=Remarque : pour définir la version localisée, sélectionnez l'état et définissez le nom du fichier de ressources dans l'onglet Ressources de l'Editeur de propriétés.

###########################################################
#ImageBuilder properties
ImageBuilder.Chooser.Title=Ouvrir le fichier image

ImageBuilder.DialogTitle.Edit=Modifier l'élément image

ImageBuilder.DialogTitle.ImportFailed=Echec de l'importation

ImageBuilder.DialogTitle.LoadingFailed=Echec de chargement de l'image

ImageBuilder.DialogTitle.New=Nouvel élément image

ImageBuilder.ErrorMessage.FileLoaded=Le nom de fichier existe déjà, veuillez le modifier et le charger à nouveau.

ImageBuilder.ErrorMessage.LoadingFailed=L'image ne peut être chargée correctement.

ImageBuilder.BrowserResourceDialog.Title=Parcourir les fichiers image

ImageBuilder.BrowserResourceDialog.Message=Sélectionnez les fichiers image depuis le dossier de ressources

ImageBuilder.ErrorMessage.OutOfMemory=La mémoire est insuffisante pour contenir l'image.

ImageBuilder.FileDialog.FilterMessage=Fichier image pris en charge

ImageBuilder.Button.Browser=Parcourir...

ImageBuilder.Button.Import=&Ajouter une image...

ImageBuilder.Button.SelectImageData=Sé&lectionner des données d'image...

ImageBuilder.Button.Insert=&Insertion

ImageBuilder.Label.SelectFrom=Sélectionner une image à partir de :

ImageBuilder.Label.EnterURI=E&ntrer l'URI :

ImageBuilder.Label.EnterResourceFile=E&ntrer le fichier de ressources :

ImageBuilder.Label.EnterExpr=E&ntrer une expression d'image dynamique :

ImageBuilder.Type.URI=&URI
ImageBuilder.Type.ResourceFile=Fichier &image dans les ressources partagées

ImageBuilder.Type.EmbededImage=Imag&e incorporée

ImageBuilder.Type.DynamicImage=Image &dynamique

ImageBuilder.Description.URI=Lien vers l'image sur le Web

ImageBuilder.Description.ResourceFile=Lien vers l'image dans les ressources partagées

ImageBuilder.Description.Embedded=Incorporer l'i&mage dans l'état

ImageBuilder.Description.Dynamic=Extraire l'image du jeu de données (bases de données)

ImageBuilder.Button.Browser.Tooltip=Parcourir le fichier image

ImageBuilder.Reload.Tooltip=Recharger image

###########################################################
#ImageEditPart properties
ImageEditPart.trans.editImage=Modifier l'image

###########################################################
#ImagePageGenerator properties
ImagePageGenerator.List.AltText=Texte de légende

ImagePageGenerator.List.Bookmark=Signet

ImagePageGenerator.List.Borders=Bordure

ImagePageGenerator.List.General=Général

ImagePageGenerator.List.HyperLink=Lien hypertexte

ImagePageGenerator.List.Reference=Référence

ImagePageGenerator.List.Section=Saut de page

ImagePageGenerator.List.TOC=Table des matières

ImagePageGenerator.List.Visibility=Visibilité

###########################################################
#ImportCSSStyleAction properties
ImportCSSStyleAction.text=&Importer un style CSS...

###########################################################
#ImportLibraryAction properties
ImportLibraryAction.Error.CannotBeEmpty=L'espace nom ne peut pas être vide.

ImportLibraryAction.Error.HasBeenUsed=Cet espace nom est en cours d'utilisation.

ImportLibraryAction.Error.HasDot=L'espace nom ne peut pas contenir de point (« . »).

ImportLibraryAction.Message.ImportSuccessfully=La bibliothèque {0} a été importée.

ImportLibraryAction.Message=Voulez-vous utiliser cette bibliothèque dans le modèle d'état ?

ImportLibraryAction.Namespace=Espace nom :

ImportLibraryAction.Prefix=Préfixe :

ImportLibraryAction.Title.ImportSuccessfully=Importation réussie

ImportLibraryAction.Dialog.Titile=Utiliser la bibliothèque

ImportLibraryAction.Dialog.Message=Sélectionnez une bibliothèque à inclure dans l'état.

###########################################################
#ImportLibraryDialog properties
ImportLibraryDialog.Title=Utiliser la bibliothèque

###########################################################
#ImportValueDialog properties
ImportValueDialog.Label.SelectColumn=Sélectionner une &colonne :

ImportValueDialog.Label.SelectDataSet=Sélectionner un jeu de &données :

ImportValueDialog.Label.SelectValue=&Sélectionner ou entrer la valeur à ajouter

ImportValueDialog.Title=Importer valeurs

ImportValueDialog.Button.Add.ToolTip=Ajouter

ImportValueDialog.Button.AddAll.ToolTip=Ajouter tout

ImportValueDialog.Button.Remove.ToolTip=Supprimer

ImportValueDialog.Button.RemoveAll.ToolTip=Supprimer tout
###########################################################
#IncludeDetailAction properties
IncludeDetailAction.actionMsg.includeDetail=&Détails

###########################################################
#IncludeFooterAction properties
IncludeFooterAction.actionMsg.includeFooter=&Pied de page

###########################################################
#IncludeHeaderAction properties
IncludeHeaderAction.actionMsg.includeHeader=&En-tête

###########################################################
#IncludeListDetailAction properties
IncludeListDetailAction.actionMsg.includeDetail=Inclure le détail

###########################################################
#IncludeListFooterAction properties
IncludeListFooterAction.actionMsg.includeFooter=Inclure le pied de page

###########################################################
#IncludeListHeaderAction properties
IncludeListHeaderAction.actionMsg.includeHeader=Inclure l'en-tête

###########################################################
#IncludeTableGroupFooterAction properties
IncludeTableGroupFooterAction.actionMsg.includeFooter=Pied de page de groupe

###########################################################
#IncludeTableGroupHeaderAction properties
IncludeTableGroupHeaderAction.actionMsg.includeHeader=En-tête de groupe

###########################################################
#InputParameterDailog properties
InputParameterDailog.dialog.title.required=Le paramètre requis doit contenir une valeur.

InputParameterDailog.dialog.title=Entrer les paramètres

InputParameterDailog.parameter.noparameter=Aucun paramètre associé à cet état.

InputParameterDialog.ErrorMessage.InvalidFile=Cet état contient une erreur.

InputParameterDailog.parameter.null=Valeur nulle

InputParameterDialog.ErrorTitle.Error=Erreur

InputParameterDialog.boolean.checked=Oui

###########################################################
#InsertAction properties
InsertAction.text=I&nsérer l'élément

###########################################################
#InsertColumnAction properties
InsertColumnAction.actionMsg.insert=Insérer

###########################################################
#InsertColumnLeftAction properties
InsertColumnLeftAction.actionMsg.columnToLeft=Colonne à &gauche

###########################################################
#InsertColumnRightAction properties
InsertColumnRightAction.actionMsg.columnToRight=Colonne à &droite

###########################################################
#InsertExpressionMenuAction properties
InsertExpressionMenuAction.text.Expression=Expression...

###########################################################
#InsertGroupAction properties
InsertGroupAction.actionMsg.group=Insérer le &groupe

InsertGroupAction.stackMsg.addGroup=Ajouter un groupe

###########################################################
#InsertGroupActionFactory properties
InsertGroupActionFactory.label.insertGroup=Insérer le &groupe

###########################################################
#InsertInLayoutAction properties
InsertInLayoutAction.action.text=I&nsérer dans la mise en page

###########################################################
#InsertListGroupAction properties
InsertListGroupAction.actionMsg.insertGroup=Insérer le groupe

InsertListGroupAction.stackMsg.insertListGroup=Insérer le groupe de listes

###########################################################
#InsertPasteColumnAction properties
InsertPasteColumnAction.text=In&sérer la colonne copiée

###########################################################
#InsertPositionGroupAction properties
InsertPositionGroupAction.Label.Above=&Au-dessus

InsertPositionGroupAction.Label.Below=&En dessous

InsertPositionGroupAction.Label.Into=Vers

###########################################################
#InsertRowAboveAction properties
InsertRowAboveAction.actionMsg.rowAbove=&Au-dessus

###########################################################
#InsertRowAction properties
InsertRowAction.actionMsg.insert=&Insérer

###########################################################
#InsertRowBelowAction properties
InsertRowBelowAction.actionMsg.rowBelow=&En dessous

###########################################################
#IReportGraphicConstants properties
IReportGraphicConstants.ImageType.All=Tous les fichiers image (*.bmp,*.JPG,*.JPEG,*.GIF,*.TIF,*.PNG,*.ICO)

IReportGraphicConstants.ImageType.Bmp=Fichiers Bitmap (*.bmp)

IReportGraphicConstants.ImageType.Gif=Fichiers GIF (*.GIF)

IReportGraphicConstants.ImageType.Icon=Fichiers icône (*.ICO)

IReportGraphicConstants.ImageType.Jpg=Fichiers JPEG (*.JPG,*.JPEG)

IReportGraphicConstants.ImageType.Png=Fichiers PNG (*.PNG)

IReportGraphicConstants.ImageType.Tif=Fichiers TIF (*.TIF)

###########################################################
#JSDocumentProvider properties
JSDocumentProvider.Content.Default=Nouveau contenu

JSDocumentProvider.document.newName=Nouveau contenu

###########################################################
#JSEditor properties
JSEditor.cmb.NewEventFunction=Nouvelle fonction d'événement...

JSEditor.Label.Script=Script :

JSEditor.Button.Validate=Valider le script

JSEditor.Button.Reset=Réinitialiser le script

JSEditor.Button.Help=Afficher la description

JSEditor.Display.DefaultText=//Editeur de scripts JS \n

JSEditor.Display.NoExpression=Cet élément ne contient pas de méthode.

JSEditor.Input.trial=évaluation

JSEditor.Text.NoText=Aucun texte

JSEditor.Text.NoDescription=Cette méthode ne comporte pas de description.

JSEditor.Validate.Title=Validation

JSEditor.Validate.NoError=Aucune erreur de syntaxe n'a été trouvée dans le script actuel.

JSEditor.Folding.Group=Pliage

JSEditor.Folding.ExpandAll.label=Tout développer

JSEditor.Folding.CollapseAll.label=Tout réduire

JSEditor.Folding.CollapseMethods.label=Méthodes de réduction

JSEditor.Folding.CollapseComments.label=Réduire les commentaires

###########################################################
#label properties
label.defaultValue=(Valeur par défaut)

label.input=Entrée

label.inputOutput=Entrée/Sortie

label.no=Non

label.output=Sortie

label.redo=Rétablir

label.undo=Annuler

label.yes=Oui

###########################################################
#LabelEditPart properties
LabelEditPart.Figure.Default=Etiquette

###########################################################
#LabelPageGenerator properties
LabelPageGenerator.List.Bookmark=Signet

LabelPageGenerator.List.Borders=Bordure

LabelPageGenerator.List.General=Général

LabelPageGenerator.List.HyperLink=Lien hypertexte

LabelPageGenerator.List.I18n=Localisation

LabelPageGenerator.List.Margin=Marge

LabelPageGenerator.List.Padding=Remplissage

LabelPageGenerator.List.Section=Saut de page

LabelPageGenerator.List.TOC=Table des matières

LabelPageGenerator.List.Visibility=Visibilité

###########################################################
#LayoutConfigurationBlock properties
LayoutConfigurationBlock.Default.Unit.Auto=Auto

LayoutConfigurationBlock.Default.Unit.Label=Unité par déf&aut :
LayoutConfigurationBlock.Default.Layout.Label=Mise en page par défaut :
LayoutConfigurationBlock.Default.Orientation.Label=Orientation par défaut :

###########################################################
#LayoutEditor properties
LayoutEditor.ErrorMessage.OpenError=Une erreur s'est produite lors de l'ouverture de l'éditeur.

LayoutEditor.Page.Layout=Normal

LayoutEditor.Page.MasterPage=Page maître

LayoutEditor.Page.Source=Source XML

###########################################################
#LayoutPageAction properties
LayoutPageAction.actionText.layout=Mise en page

###########################################################
#LibrariesNodeProvider properties
LibrariesNodeProvider.DisplayLabel.Libraries=Bibliothèques

###########################################################
#LibraryExplorerView properties
LibraryExplorerView.defaultText.notAvailable=Ressources non disponibles.

###########################################################
#LibraryExplorerProvider properties
LibraryExplorerProvider.FolderNotExist=Le dossier de ressources BIRT défini dans les préférences n'existe pas.

###########################################################
#LibraryPage properties
LibraryPage.Label.Namespace=Espace &nom :

LibraryPage.Label.Path=&Chemin :

###########################################################
#LibraryProvider properties
LibraryProvider.openDefinedLibrary.error.dialog.message=Erreur d'ouverture de bibliothèque définie

LibraryProvider.openDefinedLibrary.error.dialog.title=Erreur

###########################################################
#ListBandControlFigure properties
ListBandControlFigure.tooltip.GroupFooter=Groupe de listes - Pied de page

ListBandControlFigure.tooltip.GroupHeader=Groupe de listes - En-tête

ListBandControlFigure.tooltip.ListDetail=Liste - Détail

ListBandControlFigure.tooltip.ListFooter=Liste - Pied de page

ListBandControlFigure.tooltip.ListHeader=Liste - En-tête

###########################################################
#ListBandProvider properties
ListBandProvider.action.text=I&nsérer le groupe

###########################################################
#ListBandProxy properties
ListBandProxy.list.detail=Détails

ListBandProxy.list.footer=Pied de page

ListBandProxy.list.groupFooter=PPG

ListBandProxy.list.groupHeader=ETG

ListBandProxy.list.header=En-tête

###########################################################
#ListContainProcess properties
ListContainProcess.action.New=Nouveau...

ListContainProcess.action.NewRow=Nouvelle ligne

ListContainProcess.action.NewSubSection=Nouvelle sous-section

###########################################################
#ListEditPart properties
ListEditPart.GUIDEHANDLE_TEXT = Liste

###########################################################
#ListFigure properties
ListFigure.BORDER_TEXT=Liste

###########################################################
#ListHandleAdapt properties
ListHandleAdapt.transLabel.insertGroup=Insérer le groupe

###########################################################
#ListPageGenerator properties
ListPageGenerator.List.Bookmark=Signet

ListPageGenerator.List.Borders=Bordure

ListPageGenerator.List.Font=Police

ListPageGenerator.List.General=Général

ListPageGenerator.List.Section=Saut de page

ListPageGenerator.List.TOC=Table des matières

ListPageGenerator.List.Visibility=Visibilité


###########################################################
#MapPage properties
MapPage.label.add=&Ajouter...

MapPage.label.condition=Condition

MapPage.label.delete=&Supprimer

MapPage.label.displayValue=Valeur d'affichage :

MapPage.label.mapList=Liste de mappage :

MapPage.transName.addMapRule=Ajouter la règle de mappage

MapPage.transName.deleteMapRule=Supprimer la règle de mappage

MapPage.transName.editMapRule=Modifier la règle de mappage

MapPage.transName.moveDownRule=Déplacer la règle de mappage vers le bas

MapPage.transName.moveUpMapRule=Déplacer la règle de mappage vers le haut

MapPage.toolTipText.moveUp=Déplacer la règle de mappage vers le haut

MapPage.toolTipText.moveDown=Déplacer la règle de mappage vers le bas

MapPage.label.duplicate=Dupli&quer

MapPage.toolTipText.duplicate=Dupliquer la règle de mappage

MapPage.transName.Duplicate=Dupliquer la règle de mappage

###########################################################
#MapPreferencePage properties
MapPreferencePage.displayname.Condition=Condition

MapPreferencePage.displayname.DisplayValue=Valeur d'affichage

MapPreferencePage.displayname.Title=Mappage

MapPreferencePage.text.Add=&Ajouter...

MapPreferencePage.text.Delete=&Supprimer

MapPreferencePage.text.Description=Règles de mappage :

MapPreferencePage.text.Edit=Modifi&er...

MapPreferencePage.toolTipText.Up=Déplacer la règle de mappage vers le haut

MapPreferencePage.toolTipText.Down=Déplacer la règle de mappage vers le bas

MapPreferencePage.text.Duplicate=Du&pliquer

MapPreferencePage.toolTipText.Duplicate=Dupliquer la règle de mappage

###########################################################
#MapRuleBuilderDialog properties
MapRuleBuilder.button.add=&Ajouter

MapRuleBuilder.button.add.tooltip=Ajouter la valeur à la liste

MapRuleBuilder.button.delete=Supp&rimer

MapRuleBuilder.button.delete.tooltip=Supprimer la valeur sélectionnée de la liste

MapRuleBuilder.button.deleteall=Supprimer t&out

MapRuleBuilder.button.deleteall.tooltip=Supprimer toutes les valeurs de la liste

MapRuleBuilder.button.edit=Modifi&er...

MapRuleBuilder.button.edit.tooltip=Modifier la valeur sélectionnée dans la liste

MapRuleBuilder.DialogTitle.New=Nouvelle règle de mappage

MapRuleBuilder.DialogTitle.Edit=Modifier la règle de mappage

MapRuleBuilder.Button.ResourceKey=Clé de &ressource :

MapRuleBuilder.Button.Reset=&Réinitialiser

MapRuleBuilder.Button.Browse=&Parcourir...

MapRuleBuilder.Button.Browse.Tooltip=Parcourir et sélectionner une clé

MapRuleBuilder.label.value=&Valeur :

MapRuleBuilder.list.item1=Valeur

MapRuleBuilder.MsgDlg.Msg=Avertissement

MapRuleBuilder.MsgDlg.Title=La valeur ne peut pas être vierge.

MapRuleBuilder.text.AND=ET

MapRuleBuilderDialog.text.Between=est compris entre

MapRuleBuilderDialog.text.Condition=S&i la condition suivante est vraie :

MapRuleBuilderDialog.text.Display=Alors afficher la valeur suivan&te :

MapRuleBuilderDialog.text.ET=est égal à

MapRuleBuilderDialog.text.GE=est supérieur ou égal à

MapRuleBuilderDialog.text.GT=est supérieur à

MapRuleBuilderDialog.text.LE=est inférieur ou égal à

MapRuleBuilderDialog.text.Like=est identique à

MapRuleBuilderDialog.text.LT=est inférieur à

MapRuleBuilderDialog.text.NE=n'est pas égal à

MapRuleBuilderDialog.text.NotBetween=n'est pas compris entre

MapRuleBuilderDialog.text.NotNull=n'est pas nul

MapRuleBuilderDialog.text.Null=est nul

MapRuleBuilderDialog.text.Title=Mappage

MapRuleBuilderDialog.MsgDlg.Title=Avertissement

MapRuleBuilderDialog.MsgDlg.Msg=La valeur ne peut pas être vierge.

###########################################################
#SortkeyBuilder dialog properties
SortkeyBuilder.DialogTitle.New=Nouvelle clé de tri

SortkeyBuilder.DialogTitle.Edit=Modifier clé de tri

SortkeyBuilder.DialogMessage.New=Renseignez le contenu suivant pour créer une clé de tri.

SortkeyBuilder.DialogMessage.Edit=Modifiez le contenu suivant pour mettre à jour la clé de tri.

SortkeyBuilder.DialogTitle.Label.Prompt=Entrez une clé pour le tri :

SortkeyBuilder.DialogTitle.Label.Key=&Clé :

SortkeyBuilder.DialogTitle.Label.Direction=&Direction :

SortkeyBuilder.Label.Locale=&Local :

SortkeyBuilder.Label.Strength=&Intensité :

SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=Ouvrir le Générateur d'expression

SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=Erreur

SortkeyBuilder.Locale.Auto=Auto

SortkeyBuilder.Strength.ASCII=ASCII
SortkeyBuilder.Strength.PRIMARY=Primaire

SortkeyBuilder.Strength.SECONDARY=Secondaire

SortkeyBuilder.Strength.TERTIARY=Tertiaire

SortkeyBuilder.Strength.QUATERNARY=Quaternaire

SortkeyBuilder.Strength.IDENTICAL=Identique

###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=Nouvelle condition de filtre

FilterConditionBuilder.DialogTitle.Edit=Modifier la condition de filtre

FilterConditionBuilder.DialogMessage.New=Renseignez le contenu suivant pour créer une condition de filtre.

FilterConditionBuilder.DialogMessage.Edit=Modifiez le contenu suivant pour mettre à jour la condition de filtre.

FilterConditionBuilder.choice.ValueOfThisDataItem=Valeur de l'élément de données

FilterConditionBuilder.tooltip.ExpBuilder=Ouvrir le Générateur d'expression

FilterConditionBuilder.text.AND=ET

FilterConditionBuilder.text.Condition=Entrez une condition de filtre :

FilterConditionBuilder.Shell.Title=Condition de filtre

FilterConditionBuilder.list.item1=Valeur

FilterConditionBuilder.button.edit=Modifi&er...

FilterConditionBuilder.button.delete=Suppri&mer

FilterConditionBuilder.button.deleteall=Supprimer t&out

FilterConditionBuilder.button.add=&Ajouter

FilterConditionBuilder.button.edit.tooltip=Modifier la valeur sélectionnée dans la liste

FilterConditionBuilder.button.delete.tooltip=Supprimer la valeur sélectionnée de la liste

FilterConditionBuilder.button.deleteall.tooltip=Supprimer toutes les valeurs de la liste

FilterConditionBuilder.button.add.tooltip=Ajouter la valeur à la liste

FilterConditionBuilder.label.value=&Valeur :

FilterConditionBuilder.multipleValue.label=&Valeur

FilterConditionBuilder.multipleValue.button.add=&Ajouter

FilterConditionBuilder.multipleValue.button.remove=Supp&rimer

FilterConditionBuilder.multipleValue.button.removeAll=Tout suppri&mer

FilterConditionBuilder.multipleValue.button.edit=Mo&difier...

FilterConditionBuilder.Button.UpdateAggregation=Recalculer &Totaux

###########################################################
#MarignPropertyDescriptor properties
MarignPropertyDescriptor.error.message=La valeur entrée est trop grande.

MarignPropertyDescriptor.error.title=Valeur trop grande

###########################################################
#MasterPageAction properties
MasterColumnsPropertyDescriptor.Button.Text.Custom=Perso&nnalisée

MasterColumnsPropertyDescriptor.Button.Text.OneColumn=&Une

MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns=T&rois

MasterColumnsPropertyDescriptor.Button.Text.TwoColumns=&Deux

MasterColumnsPropertyDescriptor.Combo.Text.Column.Number=&Nombre de colonnes :

MasterPageAction.actionText.masterPage=Page maître

###########################################################
#MasterPageGenerator properties
MasterPageGenerator.List.Columns=Colonnes

MasterPageGenerator.List.General=Général

MasterPageGenerator.List.Borders=Bordure

MasterPageGenerator.List.Header&Footer=En-tête/Pied de page

MasterPageGenerator.List.Margins=Marge

###########################################################
#MasterPageGeneralPage properties
MasterPageGeneralPage.Trans.SetType=Définir le type

###########################################################
#MasterPagePaletteFactory properties
MasterPagePaletteFactory.Autotext.TotalPageCount=Nombre total de pages

MasterPagePaletteFactory.Autotext.Author_Page_Date=Auteur\#, Page\#, Date\#

MasterPagePaletteFactory.Autotext.Confidential_Page=Confidentiel, Page\#

MasterPagePaletteFactory.Autotext.CreatedBy=Création par

MasterPagePaletteFactory.Autotext.CreatedOn=Création le

MasterPagePaletteFactory.Autotext.Date=Date

MasterPagePaletteFactory.Autotext.Filename=Nom de fichier

MasterPagePaletteFactory.Autotext.Label=AutoText
MasterPagePaletteFactory.Autotext.LastPrinted=Dernière impression

MasterPagePaletteFactory.Autotext.Page=Page

MasterPagePaletteFactory.Autotext.PageXofY=Page n sur m

MasterPagePaletteFactory.AutotextTooltip.TotalPageCount=Elément d'état - Nombre total de pages

MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate=Elément d'état N° auteur, N° date

MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage=Elément d'état Confidentiel

MasterPagePaletteFactory.AutotextTooltip.CreatedBy=Elément d'état Création par

MasterPagePaletteFactory.AutotextTooltip.CreatedOn=Elément d'état Création le

MasterPagePaletteFactory.AutotextTooltip.Date=Elément d'état Date

MasterPagePaletteFactory.AutoTextTooltip.Filename=Elément d'état Nom de fichier

MasterPagePaletteFactory.AutotextTooltip.LastPrinted=Dernier élément d'état imprimé

MasterPagePaletteFactory.AutotextTooltip.Page=Elément d'état Page

MasterPagePaletteFactory.AutotextTooltip.PageXofY=Elément d'état Page n sur m

MasterPagePaletteFactory.elementLabel.authorPageDate=N° auteur, N° page, N° date

MasterPagePaletteFactory.elementLabel.confidentialPage=Confidentiel, Page

MasterPagePaletteFactory.elementLabel.createdBy=Création par

MasterPagePaletteFactory.elementLabel.createdOn=Création le

MasterPagePaletteFactory.elementLabel.date=Date

MasterPagePaletteFactory.elementLabel.fileName=Nom de fichier

MasterPagePaletteFactory.elementLabel.lastPrinted=Dernière impression

MasterPagePaletteFactory.elementLabel.page=Page

MasterPagePaletteFactory.elementLabel.pageOf=Page n sur m

MasterPagePaletteFactory.paletteDrawerName.autoText=AutoText
MasterPagePaletteFactory.ToolTip.Grid=Elément d'état Grille

MasterPagePaletteFactory.ToolTip.Text=Elément d'état Texte

MasterPagePaletteFactory.Autotext.Variable=Variable

MasterPagePaletteFactory.AutotextTooltip.Variable=Elément variable

###########################################################
#MasterPageProcess properties
MasterPageProcess.action.Edit=Modifier

MasterPageProcess.action.New=Nouveau

MasterPageProcess.text.CannotDelete=Le modèle ne contient qu'une page maître, que vous ne pouvez pas supprimer

MasterPageProcess.text.Display=Affiche la boîte de dialogue Mise en page

MasterPageProcess.text.DisplayName=MasterPage

###########################################################
#MergeAction properties
MergeAction.actionMsg.merge=&Fusionner les cellules

###########################################################
#ModulePage properties
ModulePage.ErrorMessage.Duplicate=Le nom est le doublon d'un nom existant.

ModulePage.ErrorMessage.WrongExtensionName=Le nom doit se terminer par « .properties ».

ModulePage.ErrorMessage.InfoExtensionName=Veuillez entrer un nom se terminant par .properties.

ModulePage.text.Add=&Ajouter...

ModulePage.text.ExistingFile=Fichiers de ressources existants :

ModulePage.title.AddNewFile=Ajouter un nouveau fichier de ressource

ModulePage.Resourcefile.Dialog.Title=Parcourir les fichiers de ressources

ModulePage.Resourcefile.Dialog.Message=Sélectionnez un fichier de ressources ou sélectionnez un dossier et créez un nouveau fichier.

ModulePage.text.Path=&Chemin :

###########################################################
#MultiPageEditorContributor properties
MultiPageEditorContributor.actionText.sampleAction=Exemple d'action

MultiPageEditorContributor.actionToolTip.sampleActionToolTip=Info-bulle d'exemple d'action

MultiPageEditorContributor.actionToolTipText.sampleActionToolTip=Info-bulle d'exemple d'action

MultiPageEditorContributor.dialogMsg.sampleActionExecuted=Exemple d'action exécutée

MultiPageEditorContributor.menuText.editorMenu=&Menu Editeur

###########################################################
#NewLibraryWizard properties
NewLibraryWizard.choicePageTitle.report=Bibliothèque

NewLibraryWizard.displayName.NewReportFileExtension=.rptlibrary
NewLibraryWizard.displayName.NewReportFileNamePrefix=nouvelle_bibliothèque

NewLibraryWizard.error.NoProject=Aucun projet n'est ouvert dans l'espace de travail. Choisissez Fichier->Nouveau->Projet pour créer un projet ou ouvrir un projet existant.

NewLibraryWizard.pageDescription.createNewReport=Créez une nouvelle Bibliothèque.

NewLibraryWizard.taskName.openFileForEdit=Ouverture du fichier à modifier...

NewLibraryWizard.text.ConfirmSwitchPerspective=Confirmer le changement de perspective

NewLibraryWizard.text.Container=Conteneur "

NewLibraryWizard.text.CreateReport=Créez une nouvelle bibliothèque.

NewLibraryWizard.text.Creating=Création en cours

NewLibraryWizard.text.DoesNotExist=" n'existe pas.

NewLibraryWizard.text.OpenFileForEditing=Ouverture du fichier à modifier...

NewLibraryWizard.text.SwitchPerspective=Ce type de fichier est associé à la perspective {0}. Souhaitez-vous basculer maintenant vers cette perspective ?

NewLibraryWizard.title.Choose=Choisir le mode de création de la bibliothèque

NewLibraryWizard.title.Error=Erreur

NewLibraryWizard.title.New=Nouvelle bibliothèque

NewLibraryWizard.title.Report=Bibliothèque

NewLibraryWizard.title.ReportWizard=Assistant de bibliothèque

NewLibraryWizard.title.Template=TemplateChoicePage
NewLibraryWizard.title.WizardPage=WizardPage
NewLibraryWizard.widowTitle.new=Nouvelle

NewLibraryWizard.wizardPageTitle.report=Bibliothèque

###########################################################
#NewReportProjectWizard properties

NewReportProjectWizard.bin=bin
NewReportProjectWizard.caseVariantExistsError=La casse n''est pas prise en compte par le système de fichiers. Un projet existant est en conflit avec ''{0}''.

NewReportProjectWizard.description=Créez un nouveau projet d'état.

NewReportProjectWizard.errorMessage=Problème lors de la création du projet d'état

NewReportProjectWizard.errorOpeningWindow=Problème d'ouverture d'une fenêtre.

NewReportProjectWizard.internalError=Erreur interne : {0}

NewReportProjectWizard.javaProject=Créer un projet Java

NewReportProjectWizard.perspSwitchMessage=Ce type de projet est associé à la perspective {0}. Souhaitez-vous basculer vers cette perspective maintenant ?

NewReportProjectWizard.perspSwitchTitle=Confirmer le changement de perspective

NewReportProjectWizard.projectSetting=Paramètre du projet

NewReportProjectWizard.src=src
NewReportProjectWizard.title=Projet d'état

NewReportProjectWizard.windowTitle=Nouveau projet d'état

###########################################################
#NewReportWizard properties
NewReportWizard.choicePageTitle.report=Etat

NewReportWizard.displayName.NewReportFileExtension=.rptdesign
NewReportWizard.displayName.NewReportFileNamePrefix=new_report
NewReportWizard.error.NoProject=Aucun projet n'est ouvert dans l'espace de travail. Choisissez Fichier->Nouveau->Projet pour créer un nouveau projet ou ouvrir un projet existant.

NewReportWizard.pageDescription.createNewReport=Créez un nouvel Etat.

NewReportWizard.pageDescription.selectReportTemplate=Choisissez le type d'état à créer.

NewReportWizard.taskName.openFileForEdit=Ouverture du fichier à modifier...

NewReportWizard.text.ConfirmSwitchPerspective=Confirmer le changement de perspective

NewReportWizard.text.Container=Conteneur "

NewReportWizard.text.CreateReport=Créez un nouvel état.

NewReportWizard.text.Creating=Création en cours

NewReportWizard.text.DoesNotExist=" n'existe pas.

NewReportWizard.text.OpenFileForEditing=Ouverture du fichier à modifier...

NewReportWizard.text.SelectTemplate=Choisissez le type d'état à créer.

NewReportWizard.text.SwitchPerspective=Ce type de fichier est associé à la perspective {0}. Souhaitez-vous basculer maintenant vers cette perspective ?

NewReportWizard.title.Choose=Choisissez le mode de création de l'état.

NewReportWizard.title.Error=Erreur

NewReportWizard.title.New=Nouvel état

NewReportWizard.title.Report=Etat

NewReportWizard.title.ReportWizard=Assistant d'état

NewReportWizard.title.Template=TemplateChoicePage
NewReportWizard.title.WizardPage=WizardPage
NewReportWizard.widowTitle.new=Nouveau

NewReportWizard.wizard.msgDirErr=Le répertoire cible n'existe pas, mais ne peut pas être créé.


###########################################################
#NewResourceFileDialog properties
NewResourceFileDialog.label.NewFile=&Nom du nouveau fichier :

NewResourceFileDialog.ErrorMessage=Le nom de fichier doit se terminer par .properties

NewResourceFileDialog.ErrorMessageInvalid=Le nom de fichier est incorrect

NewResourceFileDialog.FileExists={0} existe déjà. Voulez-vous l'écraser ?

###########################################################
#ProjectFileDialog properties
ProjectFileDialog.ErrorMessage=La sélection doit correspondre à un fichier et non à un répertoire.

ProjectFileDialog.Title=Sélectionner un fichier projet

ProjectFileDialog.Message=Sélectionner un fichier dans le projet en cours.

ProjectFileDialog.Text.Menu=menu

###########################################################
#ProjectSelectionDialog properties
ProjectSelectionDialog.Message=Sélectionnez le projet à configurer :

ProjectSelectionDialog.Text.SpecialSettings=Affichez uniquement les projets ayant des paramètres spécifiques au projet

ProjectSelectionDialog.Title=Configuration spécifique au projet

###########################################################
#ResourceFilterDialog properties
ResourceFilterDialog.Title=Filtre des éléments d'état

ResourceFilterDialog.Lable.Description=Description du filtre :

ResourceFilterDialog.Lable.Viewer=Sélectionnez les éléments à exclure de la vue :

###########################################################
#NewSectionDialog properties
NewSectionDialog.displayname.Container=Conteneur

NewSectionDialog.displayname.Data=Données

NewSectionDialog.displayname.Grid=Grille

NewSectionDialog.displayname.Image=Image

NewSectionDialog.displayname.Label=Etiquette

NewSectionDialog.displayname.Line=Ligne

NewSectionDialog.displayname.List=Liste

NewSectionDialog.displayname.Rectangle=Rectangle

NewSectionDialog.displayname.Table=Table

NewSectionDialog.displayname.Text=Texte

NewSectionDialog.text.Prefix=Sections disponibles :

NewSectionDialog.title.insertElement=Insérer un élément

NewSectionDialog.Title=Insérer un élément

###########################################################
#NewTemplateWizard properties
NewTemplateWizard.displayName.NewReportFileNamePrefix=nouveau_modèle

NewTemplateWizard.pageDescription.createNewTemplate=Créez un nouveau modèle d'état.

NewTemplateWizard.text.CreateReport=Créez un nouveau modèle.

NewTemplateWizard.text.Creating=Création en cours

NewTemplateWizard.text.OpenFileForEditing=Ouverture du fichier modèle à modifier...

NewTemplateWizard.title.New=Nouveau modèle

NewTemplateWizard.title.Report=Etat

NewTemplateWizard.title.Template=Modèle

NewTemplateWizard.title.WizardPage=WizardPage

###########################################################
#NoneAction properties
NoneAction.text=Aucun

###########################################################
#OutputColumnDefnPage properties
OutputColumnDefnPage.description=Entrer la définition des colonnes de sortie produites par le script de récupération :

OutputColumnDefnPage.OutputColumns.Tooltip=Afficher et définir les colonnes au sein d'une ligne de données

OutputColumnPage.error.createNewColumn=Une erreur s'est produite lors de l'ajout d'une nouvelle colonne.

OutputColumnPage.error.invalidSelection=Aucun élément de table valide sélectionné.

###########################################################
#ColumnDefPage properties
ColumnDefPage.description=Page de définition de la colonne de sortie du jeu de données de script

ColumnDefPage.title=Colonnes de sortie

###########################################################
#ResultSetColumnPage properties
ResultSetColumnPage.button.add=&Ajouter...

ResultSetColumnPage.button.edit=Modifi&er...

ResultSetColumnPage.button.delete=&Supprimer

ResultSetColumnPage.button.up=&Vers le haut

ResultSetColumnPage.button.down=&Vers le bas

ResultSetColumnPage.inputDialog.newColumn.title=Nouvelle colonne de jeu de données de script

ResultSetColumnPage.inputDialog.editColumn.title=Modifier la colonne de jeu de données de script

ResultSetColumnPage.inputDialog.label.columnName=Nom de &colonne :

ResultSetColumnPage.inputDialog.label.dataType=&Type de données :

ResultSetColumnPage.inputDialog.label.alias=&Alias de colonne :

ResultSetColumnPage.inputDialog.label.displayName=Nom &d'affichage :

ResultSetColumnPage.inputDialog.label.displayNameKey=&Clé de nom d'affichage :

ResultSetColumnPage.inputDialog.warning.emptyColumnName=Le nom de la colonne est requis.

ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias=Le nom de colonne et son alias doivent être différents.

ResultSetColumnPage.inputDialog.error.duplicatedColumnName=Le nom de colonne "{0}" a déjà été utilisé.

ResultSetColumnPage.inputDialog.error.duplicatedAlias=Le nom d'alias "{0}" a déjà été utilisé.

###########################################################
#OutputColumnsPage properties
OutputColumnPage.OutputColumns.Tooltip=Afficher et définir les colonnes au sein d'une ligne de données

OutputColumnPage.toolTipText.Edit=Modifier la colonne de sortie

OutputColumnPage.title.OutputColumnInputDialog=Colonne de sortie

OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn=Nouvelle colonne de sortie

OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn=Modifier la colonne de sortie

OutputColumnPage.OutputColumns.DuplicatedName=Ce nom a déjà existé.

OutputColumnsPage.description=Définir les colonnes de sortie :

OutputColumnsPage.DataTypeConvert.Note=Remarque

OutputColumnsPage.DataTypeConvert.Message=Une telle conversion peut engendrer une perte de précision ou une erreur lors de l'exécution, êtes- vous sûr de vouloir l'appliquer ?

###########################################################
#PageBreakPreferencePage properties
PageBreakPreferencePage.displayname.Title=Saut de page

###########################################################
#PageSetAction properties
PageSetAction.text.code=&Script

PageSetAction.Text.EditCode=Modifier le &script...

PageSetAction.text.layout=&Mise en page

PageSetAction.text.master=&Principal

PageSetAction.text.normal=&Normal

PageSetAction.text.preview=&Aperçu

###########################################################
#PageSetupOutlineProcess properties
PageSetupOutlineProcess.action.Edit=Modifier la mise en page

PageSetupOutlineProcess.action.New=Nouvelle page maître

PageSetupOutlineProcess.text.Display=Affiche la boîte de dialogue Mise en page

PageSetupOutlineProcess.text.Switch=Bascule vers l'éditeur maître de page

###########################################################
#ParameterDialog properties
ParameterDialog.Boolean.False=Faux

ParameterDialog.Boolean.True=Vrai

ParameterDialog.Button.ChangeFormat=&Modifier...

ParameterDialog.Button.CreateDataSet=&Créer...

ParameterDialog.Button.ImportValue=Importer des vale&urs...

ParameterDialog.Button.RemoveDefault=Supprimer la valeur par déf&aut

ParameterDialog.Button.SetDefault=Définir p&ar défaut

ParameterDialog.CheckBox.IsRequired=Est re&quis

ParameterDialog.CheckBox.DoNotEchoInput=Ne pas repr&oduire l'entrée

ParameterDialog.CheckBox.Hidden=Mas&qué

ParameterDialog.CheckBox.Distinct=Autoriser les valeurs dup&liquées

ParameterDialog.Choice.NoDefault=Aucune valeur par défaut

ParameterDialog.Choice.NullValue=Valeur nulle

ParameterDialog.Choice.BlankValue=Valeur vierge

ParameterDialog.Choice.None=Aucun

ParameterDialog.Choice.DisplayText=Texte d'affichage

ParameterDialog.Choice.ValueColumn=Colonne des valeurs

ParameterDialog.Choice.ASCENDING=Croissant

ParameterDialog.Choice.DESCENDING=Décroissant

ParameterDialog.Choice.SelectValue=<Sélectionner une valeur...>

ParameterDialog.Column.Default=Par défaut

ParameterDialog.Column.DisplayText=Texte d'affichage

ParameterDialog.Column.DisplayTextKey=Clé de texte à afficher

ParameterDialog.Column.Value=Valeur

ParameterDialog.DefaultValue.Add=&Ajouter

ParameterDialog.DisplayLabel.Combo=Zone déroulante

ParameterDialog.DisplayLabel.List=Liste déroulante

ParameterDialog.ErrorMessage.CanootBeBlank=La valeur ne peut pas être vide.

ParameterDialog.ErrorMessage.CanootBeNull=La valeur ne peut pas être nulle.

ParameterDialog.ErrorMessage.DuplicatedLabel=Le texte d'affichage existe déjà.

ParameterDialog.ErrorMessage.DuplicatedLabelKey=La clé de texte à afficher existe déjà.

ParameterDialog.ErrorMessage.DuplicatedName=Ce nom est en cours d'utilisation.

ParameterDialog.ErrorMessage.DuplicatedValue=Cette valeur existe déjà.

ParameterDialog.ErrorMessage.EmptyName=Le nom n'est pas renseigné.

ParameterDialog.ErrorMessage.InvalidListLimit=La valeur de limitation doit être comprise entre 0 et {0}

ParameterDialog.ErrorMessage.MismatchDataType=La valeur ne correspond pas au type de données.

ParameterDialog.ErrorMessage.NoAvailableColumn=Aucune colonne n'est disponible.

ParameterDialog.ErrorMessage.ValueColumnEmpty=La colonne de valeurs doit être spécifiée.

ParameterDialog.ErrorMessage.NoDefaultValue=Vous devez définir une valeur par défaut.

ParameterDialog.ErrorTitle.InvalidListLimit=Limite de liste incorrecte

ParameterDialog.ExpressionEditor.Title=Modifier l'expression

ParameterDialog.Flag.Default=Par défaut

ParameterDialog.Group.MoreOption=Afficher sous

ParameterDialog.Group.Properties=Propriétés

ParameterDialog.Label.DataType=Type de &données :

ParameterDialog.SelectionDialog.Edit=Modifier le choix de sélection

ParameterDialog.Label.DefaultValue=Valeur par dé&faut :

ParameterDialog.Label.DisplayPrompt=Afficher une invite :

ParameterDialog.Label.DisplayType=&Type d'affichage :

ParameterDialog.Label.Format=Format :

ParameterDialog.Label.HelpText=Te&xte d'aide :

ParameterDialog.Label.ListOfValue=Valeurs de la liste de sélection

ParameterDialog.Label.SortGroup=Trier

ParameterDialog.Label.Name=&Nom :

ParameterDialog.Label.None=<Aucun>

ParameterDialog.Label.Null=Nul

ParameterDialog.Label.Preview=Aperçu au format

ParameterDialog.Label.PromptText=&Texte de l'invite :

ParameterDialog.Label.SelectDataSet=&Jeu de données :

ParameterDialog.Label.SortKey=Trier &par :

ParameterDialog.Label.SortDirection=Direction du tri :

ParameterDialog.SelectionDialog.New=Nouveau choix de sélection

ParameterDialog.SelectionDialog.Label.DisplayTextKey=Clé de te&xte à afficher :

ParameterDialog.SelectionDialog.Label.DisplayText=&Texte d'affichage :

ParameterDialog.SelectionDialog.Label.Value=&Valeur :

ParameterDialog.SelectionDialog.Button.Resource=...
ParameterDialog.SelectionDialog.Button.Resource.Tooltip=Parcourir et sélectionner une clé

ParameterDialog.SelectionDialog.Button.Remove.Tooltip=Supprimer la clé

ParameterDialog.SelectionDialog.Label.Note=Remarque : pour définir la clé de texte à afficher, sélectionnez l'état et définissez le nom du fichier de ressources dans l'onglet Ressources de l'Editeur de propriétés.

ParameterDialog.Label.SelectDisplayText=Sélectionner t&exte d'affichage :

ParameterDialog.Label.SelectValueColumn=Sélectionner une colonne de vale&ur :

ParameterDialog.Label.Value=Valeurs :

ParameterDialog.Label.values=valeurs

ParameterDialog.Label.Sample=Echantillon

ParameterDialog.Label.Listlimit=L&imite de liste :

ParameterDialog.Radio.Dynamic=D&ynamique

ParameterDialog.Radio.Static=&Statique

ParameterDialog.toolTipText.OpenExprButton=Ouvrir le Générateur d'expression

ParameterDialog.datetime.prompt=Entrez les valeurs de date et d''heure selon le modèle suivant : {0}

ParameterDialog.date.prompt=Entrez les valeurs de date selon le modèle suivant : {0}

ParameterDialog.time.prompt=Entrez les valeurs d''heure selon le modèle suivant : {0}

ParameterDialog.Check.AllowMulti=Autoriser les &valeurs multiples

ParameterDialog.checkBox.InputValue=Valeur d'entrée

ParameterDialog.checkBox.BlankValue=Valeur vierge

ParameterDialog.checkBox.NullValue=Valeur nulle

ParameterDialog.emptyColumnValueExpression.title=Expression de colonne de valeur non valide

ParameterDialog.emptyColumnValueExpression.message=L'expression de colonne de valeur doit être indiquée !

ParameterDialog.Value.Empty=Valeur vide

ParameterDialog.Value.Null=Valeur nulle

ParameterDialog.message=Définissez les propriétés du paramètre d'état.

###########################################################
#SelectParameterDefaultValueDialog properties
SelectParameterDefaultValueDialog.Title=Sélectionner une valeur

###########################################################
#ParameterDropListener properties
ParameterDropListener.Command.Copy=Copier l'élément

ParameterDropListener.Command.Move=Déplacer l'élément

###########################################################
#ParameterGroupDialog properties
ParameterGroupDialog.Page.Title=Général

ParameterGroupDialog.pageTitle.general=Général

###########################################################
#ParameterGroupGeneralPage properties
ParameterGroupGeneralPage.Label.DisplayName=Nom &d'affichage :

ParameterGroupGeneralPage.Label.Name=&Nom :

###########################################################
#ParameterGroupNodeProvider properties
ParameterGroupNodeProvider.Action.GroupNew=Nouveau groupe de paramètres

ParameterGroupNodeProvider.Action.ParameterEdit=&Modifier

ParameterGroupNodeProvider.Action.ParameterNew=&Nouveau paramètre

ParameterGroupNodeProvider.Dialogue.ParameterEdit=Modifier le groupe de paramètres

ParameterGroupNodeProvider.Dialogue.ParameterNew=Nouveau paramètre

ParameterGroupNodeProvider.Node.Group=Groupe de paramètres

###########################################################
#ParameterGroupProcess properties
ParameterGroupProcess.action.Delete=Supprimer Parametergroup

ParameterGroupProcess.action.Edit=Modifier Parametergroup

ParameterGroupProcess.action.New=Nouveau paramètre

ParameterGroupProcess.text.DisplayName=ParameterGroup
ParameterGroupProcess.text.Show=affiche la page de l'éditeur de source de données

###########################################################
#ParameterNodeProvider properties
ParameterNodeProvider.dial.title.editCascading=Modifier groupe de paramètres en cascade

ParameterNodeProvider.dial.title.editScalar=Modifier le paramètre

ParameterNodeProvider.dial.title.newCascading=Nouveau groupe de paramètres en cascade

ParameterNodeProvider.menu.text.edit=&Modifier

ParameterNodeProvider.menu.text.new=Nouveau paramètre

###########################################################
#ParameterProcess properties
ParameterProcess.action.Delete=Supprimer le paramètre

ParameterProcess.action.Edit=Modifier le paramètre

ParameterProcess.text.DisplayName=Paramètre

ParameterProcess.text.Show=affiche la page de l'éditeur de source de données

###########################################################
#parameters properties
parameters.datatypes.boolean=Booléen

parameters.datatypes.dateTime=DateTime
parameters.datatypes.decimal=Décimal

parameters.datatypes.float=Flottante

parameters.datatypes.string=Chaîne

parameters.menuItem.reset=Réinitialiser

###########################################################
#ParametersNodeProvider properties
ParametersNodeProvider.dialogue.title.group=Nouveau groupe de paramètres

ParametersNodeProvider.dialogue.title.parameter=Nouveau paramètre

ParametersNodeProvider.menu.text.cascadingParameter=Nouveau groupe de paramètres en casca&de

ParametersNodeProvider.menu.text.group=Nouveau groupe de para&mètres

ParametersNodeProvider.menu.text.parameter=Nouve&au paramètre

###########################################################
#ParametersOutlineProcess properties
ParametersOutlineProcess.action.Edit=Modifier

ParametersOutlineProcess.action.New=Nouveau paramètre

ParametersOutlineProcess.action.NewGroup=Nouveau groupe

ParametersOutlineProcess.text.Open=ouvrir l'éditeur de paramètre

ParametersOutlineProcess.text.Show=affiche la page de l'éditeur de jeu de données pour ce jeu de données

###########################################################
#PasteAction properties
PasteAction.dlg.title=Informations

PasteAction.ok=OK

PasteAction.text=&Coller

###########################################################
#PlaceHolderEditPart properties
PlaceHolderEditPart.Trans.TransferToElement=Transférer vers l'élément d'état

###########################################################
#PreviewAction properties
PreviewAction.Text=Aperçu

###########################################################
#PreviewEditor properties
PreviewEditor.parameter.tooltip=Afficher les paramètres de l'état

PreviewEditor.parameter.hint=Actualiser l'état

PreviewEditor.parameter.note=Remarque :

PreviewEditor.parameter.info=Le nombre de lignes de données maximum actuel est de {0}.

PreviewEditor.parameter.change=Cliquer pour modifier les préférences d'aperçu

PreviewEditor.ConfirmRefresh.Title=Confirmer l'actualisation

PreviewEditor.ConfirmRefresh.Message=Cette opération risque d'influencer le résultat de la prévisualisation. Voulez-vous actualiser la page de prévisualisation ?

PreviewEditor.browser.notfound=Le navigateur interne n'est pas disponible.

###########################################################
#PreviewPageAction properties
PreviewPageAction.actionText.preview=Aperçu

###########################################################
#PropertiesAction properties
PropertiesAction.Text=Propriétés

###########################################################
#PropertyHandleTableViewer properties
PropertyHandleTableViewer.Button.New=&Nouveau...

PropertyHandleTableViewer.Button.Edit=&Edition

PropertyHandleTableViewer.Button.Remove=&Supprimer

PropertyHandleTableViewer.Button.Up=&Vers le haut

PropertyHandleTableViewer.Button.Down=&Vers le bas

PropertyHandleTableViewer.Menu.Remove=Supprimer

PropertyHandleTableViewer.Menu.Up=Vers le haut

PropertyHandleTableViewer.Menu.Down=Vers le bas

PropertyHandleTableViewer.Menu.RemoveAll=Supprimer tout

###########################################################
#PropertyHandleInputDialog properties
PropertyHandleInputDialog.Action.New=Nouveau

PropertyHandleInputDialog.Action.Edit=Modifier

PropertyHandleInputDialog.messages.error.blankProperty={0} ne peut pas être vide

###########################################################
#PropertyAndPreferencePage properties
PropertyAndPreferencePage.Text.Configure.Special.Settings=Configurer les paramètres spécifiques au projet...

PropertyAndPreferencePage.Text.Configure.Workspace.Settings=Configurer les paramètres de l'espace de travail...

PropertyAndPreferencePage.Text.Enable.SpecialSettings=Activer les paramètres spécifiques au pr&ojet

###########################################################
#PublishTemplateAction properties
PublishTemplateAction.wizard.page.browse.cheatsheet.title=Choisir une feuille de fraude

PublishTemplateAction.wizard.page.browse.title=Choisir un fichier image

PublishTemplateAction.wizard.page.btn.browse=Par&courir...

PublishTemplateAction.wizard.page.cheatsheetError=La feuille de fraude ''{0}'' n''existe pas.

PublishTemplateAction.wizard.page.desc=Enregistrer le modèle dans le dossier modèle.

PublishTemplateAction.wizard.page.imageError=Le fichier image ''{0}'' n''existe pas.

PublishTemplateAction.wizard.page.label.cheatsheet=Feuille de fraude :

PublishTemplateAction.wizard.page.label.description=&Description :

PublishTemplateAction.wizard.page.label.displayName=No&m d'affichage :

PublishTemplateAction.wizard.page.label.image=&Image modèle :

PublishTemplateAction.wizard.page.title=Publier le modèle

PublishTemplateAction.wizard.title=Publier le modèle

PublishTemplateAction.wizard.errorTitle=Erreur

PublishTemplateAction.wizard.message=Impossible de publier le modèle dans son propre répertoire ; sélectionnez un autre répertoire pour procéder à la publication.

PublishTemplateAction.wizard.msgDirErr=Le répertoire cible n'existe pas, mais ne peut pas être créé.

PublishTemplateAction.wizard.notvalidfolder=Le dossier modèle défini dans la préférence n'est pas valide.

PublishTemplateAction.wizard.page.nameInfo=Le nom d'affichage est requis

PublishTemplateAction.wizard.message.SourceFileNotExist=Le fichier source n'existe pas.

PublishTemplateAction.wizard.message.ThumbnailImageNotExist=L'image miniature n'existe pas.

PublishTemplateAction.wizard.message.PreviewImageNotExist=Le fichier de prévisualisation de l'image n'existe pas.

PublishTemplateAction.wizard.message.PreviewImageNotValid=Le fichier de prévisualisation de l'image n'est pas une image valide.

PublishTemplateAction.SaveBeforeGenerating.dialog.title=Enregistrer l'état

PublishTemplateAction.SaveBeforeGenerating.dialog.message={0} a été modifié(e). Enregistrer les modifications avant la publication ?

PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes=&Oui

PublishTemplateAction.SaveBeforeGenerating.dialog.button.no=&Non
###########################################################
#PublishLibraryAction properties
PublishLibraryAction.wizard.errorTitle=Erreur

PublishLibraryAction.wizard.message=Impossible de publier la bibliothèque dans son propre répertoire ; sélectionnez un autre répertoire pour procéder à la publication.

PublishLibraryAction.wizard.msgDirErr=Le répertoire cible n'existe pas, mais ne peut pas être créé.

PublishLibraryAction.wizard.message.SourceFileNotExist=Le fichier source n'existe pas.

PublishLibraryAction.wizard.notvalidfolder=Le dossier de bibliothèque défini dans la préférence n'est pas valide.

###########################################################
#RedoAction properties
RedoAction.Tooltip=Rétablir {0}

###########################################################
#ExportReportWizard
ExportToLibraryAction.wizard.windowTitle=Exporter

ExportToLibraryAction.wizard.page.title=Exporter le fichier dans la bibliothèque

ExportToLibraryAction.wizard.page.desc=Exporter le modèle d'état dans la bibliothèque

ExportToLibraryAction.wizard.page.label.filename=&Nom de fichier :

ExportToLibraryAction.wizard.page.label.folder=&Dossier :

ExportToLibraryAction.wizard.page.button.browser=& Parcourir...

ExportToLibraryAction.wizard.page.dirdialog.message=Sélectionner le répertoire de contenu du fichier

ExportToLibraryAction.wizard.page.nameempty=Le nom de fichier doit être spécifié.

ExportToLibraryAction.wizard.page.folderempty=Le dossier doit être spécifié.

ExportToLibraryAction.wizard.page.foldererror=Le dossier spécifié n'existe pas.

###########################################################
#PublishLibraryToResourceFolderAction
PublishLibraryToResourceFolderAction.Action.Text=Copier la bi&bliothèque dans le dossier de ressources partagées...

###########################################################
#PublishTemplateViewAction
DefaultNodeProvider.template.suffix=.rpttemplate
PublishTemplateViewAction.action.displaytext=Enregistrer le modèle avec l'&assistant Nouvel état...

###########################################################
#RefreshModuleHandleAction
RefreshModuleHandleAction.Action.Text=Actualiser la bib&liothèque

RefreshModuleHandleAction.MessageBox.Title=Actualiser la bibliothèque

RefreshModuleHandleAction.MessageBox.Text=Les modifications du modèle de création actuel n'ont pas été enregistrées. Souhaitez-vous d'abord enregistrer les modifications apportées ?

RefreshModuleHandleAction.MessageBox.SaveButton=&Enregistrer

RefreshModuleHandleAction.MessageBox.DiscardButton=&Refuser

RefreshModuleHandleAction.MessageBox.CancelButton=Annuler
###########################################################
#ReloadCssStylesAction
ReloadCssStylesAction.Action.Text=&Recharger un fichier CSS

###########################################################
#ReferencePage properties
ReferencePage.Button.Edit=&Modifier...

ReferencePage.Label.Source=Sour&ce :

###########################################################
#RefreshAction properties
RefreshAction.text=Ac&tualiser

###########################################################
#RefreshLibExplorerAction properties
RefreshLibExplorerAction.Text=Ac&tualiser

###########################################################
#RemoveLibraryAction properties
RemoveLibraryAction.Text=Supprimer la bibliothèque

RemoveLibraryAction.config.Title=Confirmer la suppression de la bibliothèque

RemoveLibraryAction.config.Message=Voulez-vous vraiment supprimer cette bibliothèque ?

###########################################################
#UseCssInReportDesignAction properties
UseCssInReportDesignAction.Text=Utiliser CSS dans le modèle d'état...

###########################################################
#UseCssInReportDesignAction properties
UseCssInThemeAction.Text=Utiliser CSS dans la bibliothèque...

###########################################################
#RenameAction properties
RenameAction.text=Reno&mmer

###########################################################
#RenameDialog properties
RenameDialog.action.Cancel=Annuler

RenameDialog.action.Ok=\\ \\ OK

RenameDialog.text.NewName=Nouveau nom :

RenameDialog.text.Rename=Renommer

RenameDialog.text.ToolTip=Le nom doit être renseigné.


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.SetNameAction.text=Définir le nom

ExportElementToLibraryAction.DialogTitle.setNameFailed=Echec de la définition du nom

ExportElementToLibraryAction.TransLabel.Setname=Définir le nom

ExportElementToLibraryAction.DialogTitle=Définir le Nom

ExportElementToLibraryAction.DialogMessage=Vous pouvez définir le nom de l'élément sélectionné avant de l'exporter ; sinon, un nom par défaut sera utilisé.


###########################################################
#RenameInlineTool properties
RenameInlineTool.DialogTitle.RenameFailed=Impossible de renommer

RenameInlineTool.TransLabel.Rename=Renommer

###########################################################
#RenameInputDialog properties
RenameInputDialog.DialogTitle=Renommer l'entrée

RenameInputDialog.DialogMessage=Saisissez le nouveau nom

###########################################################
#report properties
report.designer.ui.preferences.buttonadd=&Ajouter...

report.designer.ui.preferences.buttonremove=&Supprimer

report.designer.ui.preferences.buttonremoveall=Suppri&mer tout

report.designer.ui.preferences.dialogtitle=Ouvrir une bibliothèque

report.designer.ui.preferences.errormessage=Cette bibliothèque existe déjà.

report.designer.ui.preferences.errortitle=Erreur

report.designer.ui.preferences.extensions=*.rptlibrary
report.designer.ui.preferences.labeltitle=Ajouter/Supprimer des bibliothèques

report.designer.ui.preferences.library.export.overwrite.always=&Toujours

report.designer.ui.preferences.library.export.overwrite.never=&Jamais

report.designer.ui.preferences.library.export.overwrite.prompt=&Invite

report.designer.ui.preferences.library.export.title=Ecraser l'élément existant lors de l'exportation de l'élément de modèle vers la bibliothèque

report.designer.ui.preferences.template.openDialogTitle=Sélection du dossier

report.designer.ui.preferences.template.select=&Sélectionner...

report.designer.ui.preferences.template.templatedir=modèles

report.designer.ui.preferences.template.templatefolder=&Dossier modèle

report.designer.ui.preferences.template.title=Sélectionnez le dossier modèle.

org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment=&Générer un commentaire lors de la création du modèle d'état

org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment=Copyright (c) 2008 <<Nom de votre entreprise>>
#bidi_hcg start
report.designer.ui.preferences.bidiproperties.defaultdirection=&Orientation de l'état par défaut :

report.designer.ui.preferences.bidiproperties.ltrdirection=Gauche à droite

report.designer.ui.preferences.bidiproperties.rtldirection=Droite à gauche
#bidi_hcg end
###########################################################
#Resource Preference page properties
ResourecePreferencePage.title=Tapez ou sélectionnez le dossier de ressources par défaut à utiliser pour l'Explorateur de bibliothèques, les images,\nles fichiers de propriété, etc.

ResourecePreferencePage.folder=Dossier de &ressources

ResourecePreferencePage.select=&Sélectionner...

ResourecePreferencePage.openDialogTitle=Parcourir le dossier

ResourecePreferencePage.openDialogMessage=Sélectionnez le dossier de ressources par défaut :

ResourecePreferencePage.defaultResourceFolder.dispaly=<Dossier du projet en cours>

###########################################################
#ReportAdvancedLauncherTab properties
ReportAdvancedLauncherTab.DefaultsButtonText=&Restaurer les paramètres par défaut

ReportAdvancedLauncherTab.DeselectButtonText=&Désélectionner tout

ReportAdvancedLauncherTab.Name=Projets d'état

ReportAdvancedLauncherTab.SelectAllButtonText=&Sélectionner tout

ReportAdvancedLauncherTab.UseListRadioText=Sélectionnez les projets à démarrer dans la liste

ReportAdvancedLauncherTab.VisibleLabelText=&Projets disponibles

ReportAdvancedLauncherTab.WorkspaceReportProjects=projets d'état d'espace de travail

###########################################################
#ReportClasspathLaucnTab properties
ReportClasspathLaucnTab.DefaultButtonText=&Restaurer les paramètres par défaut

ReportClasspathLaucnTab.DeselectButtonText=&Désélectionner tout

ReportClasspathLaucnTab.Name=Projets Java

ReportClasspathLaucnTab.SelectAllButtonText=&Sélectionner tout

ReportClasspathLaucnTab.UseListRadioText=Sélectionnez un projet à lancer dans la liste. Le dossier de sortie de classe du projet sélectionné sera ajouté au chemin de classe.

ReportClasspathLaucnTab.VisibleLabelText=&Projets disponibles

ReportClasspathLaucnTab.WorkspaceJavaProjects=projets java d'espace de travail

###########################################################
#ReportCreationTool properties
ReportCreationTool.ModelTrans.CreateElement=Insérer l'élément

###########################################################
#ReportEditor properties
ReportEditor.Button.Close=Fermer

ReportEditor.Button.Save=Enregistrer

ReportEditor.Dlg.Confirm=Le fichier a été supprimé du système de fichiers. Souhaitez-vous enregistrer vos modifications ou fermer l'éditeur sans enregistrer ?

ReportEditor.Dlg.Save=Enregistrer...

ReportEditor.error.activated.outofsync.message=Le fichier a été modifié dans le système de fichiers. Voulez-vous enregistrer les modifications?

ReportEditor.error.activated.outofsync.title=Fichier modifié

ReportEditor.Error.Open=Erreur à l'ouverture

ReportEditor.Error.OpenEditor=Impossible d'ouvrir le fichier.

ReportEditor.Page.Code=Script

ReportEditor.Page.Layout=Mise en page

ReportEditor.Page.MasterPage=MasterPage
ReportEditor.Page.Preview=Aperçu

ReportEditor.Page.PreviewSaveMessage=Le fichier d'état a été modifié. Souhaitez-vous l'enregistrer avant de lancer l'aperçu ?

ReportEditor.Page.PreviewSaveTitle=Enregistrer

ReportEditor.Page.PreviewSaveToggle=Toujours enregistrer le fichier avant la prévisualisation.

###########################################################
#ReportLaunchConfigurationDelegate properties
ReportLaunchConfigurationDelegate.badFeatureSetup=Mauvaise configuration des fonctions

ReportLaunchConfigurationDelegate.noStartup=Pas de démarrage

###########################################################
#ReportPage properties
ReportPage.text.Add=&Ajouter...

ReportPage.text.Browse=& Parcourir...

ReportPage.title.Location=Emplacement :

ReportPage.title.setPrewImg=Sélectionner un fichier image

ReportPage.text.Resource.Add=&Parcourir...

ReportPage.text.Thumbnail=Miniat&ure...

###########################################################
#ReportPageGenerator properties
ReportPageGenerator.List.Comments=Commentaires

ReportPageGenerator.List.Description=Description

ReportPageGenerator.List.EventHandler=Gestionnaire d'événements

ReportPageGenerator.List.General=Général

ReportPageGenerator.List.Measurement=Mesure

ReportPageGenerator.List.NamedExpressions=Expressions nommées

ReportPageGenerator.List.UserProperties=Propriétés de l'utilisateur

ReportPageGenerator.List.Resources=Ressources

ReportPageGenerator.List.Resources.JarFile=Fichiers Jar :

ReportPageGenerator.List.AdvancedProperty=Avancé

#OdaDataSourceGenerator properties
DataSourcePageGenerator.List.General=Général

#OdaDataSetGenerator properties
DataSetPageGenerator.List.General=Général

DataSetPageGenerator.DataSourceName=Source de &données

#ScalarParameterPageGenerator properties
ScalarParameterPageGenerator.List.General=Général

ScalarParameterPageGenerator.List.I18n=Localisation

#TemplateReportItemPageGenerator properties
TemplateReportItemPageGenerator.List.General=Général

TemplateReportItemPage.description.Label.Instructions=&Instructions :

TemplateReportItemPageGenerator.List.I18n=Localisation

TemplateReportItemPageGenerator.List.TextKey=Clé de texte :

###########################################################
#ReportPropertySheetPage properties
ReportPropertySheetPage.Column.Title.Property=Propriété

ReportPropertySheetPage.Column.Title.Value=Valeur

ReportPropertySheetPage.Root.Default.Title=Infos

ReportPropertySheetPage.Tooltip.Alphabetic=Trier par ordre alphabétique

ReportPropertySheetPage.Tooltip.Group=Trier par groupe

ReportPropertySheetPage.Tooltip.Local=Afficher les propriétés locales

ReportPropertySheetPage.Value.Inherited=Hérité

###########################################################
#ReportTemplateTransferDropTargetListener properties
ReportTemplateTransferDropTargetListener.ModelTrans.CreateElement=Créer l'élément

ReportTemplateTransferDropTargetListener.transLabel.createElement=Insérer l'élément

###########################################################
#ResourceEditDialog properties
ResourceEditDialog.message.AddNote=La fonction Ajout rapide enregistre la clé dans le fichier de ressources en cours.

ResourceEditDialog.message.ResourceFile=Fichier de ressources :

ResourceEditDialog.text.Add=&Ajouter

ResourceEditDialog.text.AddWarningMsg=Les clés doivent être renseignées.

ResourceEditDialog.text.AddWarningTitle=Avertissement

ResourceEditDialog.text.Delete=&Supprimer

ResourceEditDialog.text.Key.TableColumn=Clé

ResourceEditDialog.text.Key=&Clé

ResourceEditDialog.text.QuickAdd=Ajout rapide

ResourceEditDialog.text.Value.TableColumn=Valeur

ResourceEditDialog.text.Value=&Valeur

###########################################################
#ResourceKeyDescriptor properties
ResourceKeyDescriptor.text.Browse=& Parcourir...

ResourceKeyDescriptor.text.Browse.Alt=Par&courir...

ResourceKeyDescriptor.text.Reset=&Réinitialiser

ResourceKeyDescriptor.text.Reset.Alt=R&éinitialiser

ResourceKeyDescriptor.title.SelectKey=Sélectionner la clé

ResourceKeyDescriptor.button.reset.tooltip=Réinitialiser la clé

ResourceKeyDescriptor.button.browse.tooltip=Parcourir et sélectionner une clé

ResourceKey.invalid.containSpace=Clé de nom d'affichage invalide, car elle ne doit contenir aucun espace intercalé
###########################################################
#RevertToReportItemAction properties
RevertToReportItemAction.text=&Restaurer l'élément d'état

###########################################################
#RevertToTemplateAction properties
RevertToTemplateAction.text=Res&taurer le modèle

###########################################################
#RowHandle properties
RowHandle.tooltip.GridRow=Grille - Ligne

RowHandle.tooltip.GroupFooter=Groupe de tables - Pied de page

RowHandle.tooltip.GroupHeader=Groupe de tables - En-tête

RowHandle.tooltip.TableDetail=Table - Détail

RowHandle.tooltip.TableFooter=Table - Pied de page

RowHandle.tooltip.TableHeader=Table - En-tête

###########################################################
#RowHandleAdapter properties
RowHandleAdapter.table.detail=Ligne de détail

RowHandleAdapter.table.footer=Ligne de pied de page

RowHandleAdapter.table.groupFooter=Ligne de pied de page de groupe

RowHandleAdapter.table.groupHeader=Ligne d'en-tête de groupe

RowHandleAdapter.table.header=Ligne d'en-tête

###########################################################
#RowPageGenerator properties
RowPageGenerator.List.Bookmark=Signet

RowPageGenerator.List.Borders=Bordure

RowPageGenerator.List.Section=Saut de page

RowPageGenerator.List.CellPadding=Marge intérieure

RowPageGenerator.List.Font=Police

RowPageGenerator.List.General=Général

RowPageGenerator.List.Visibility=Visibilité

###########################################################
#RowProvider properties
RowProvider.action.text.above=Insérer une ligne &au-dessus

RowProvider.action.text.below=Insérer une ligne au-&dessous

###########################################################
#SaveAsDialog properties
SaveAsDialog.message=Enregistrer le fichier à un autre emplacement.

SaveAsDialog.overwriteQuestion=Le fichier ''{0}'' existe déjà. Voulez-vous remplacer le fichier existant ?

SaveAsDialog.Question=Question

SaveAsDialog.text=Enregistrer sous

SaveAsDialog.title=Enregistrer sous

###########################################################
#CreateDirectory properties
DirectoryDialog.createQuestion=Le répertoire ''{0}'' n''existe pas. Souhaitez-vous le créer ?

DirectoryDialog.Question=Question

###########################################################
#SchematicContextMenuProvider properties
SchematicContextMenuProvider.ActionText.editData=Modifier &Valeur/Expression...

SchematicContextMenuProvider.ActionText.editLabel=&Modifier

SchematicContextMenuProvider.label.ResetSize=Réinitialiser la tai&lle de l'image

SchematicContextMenuProvider.label.ResetToImageDPI=Ajuster la résolution de l'&image

SchematicContextMenuProvider.label.ResetToReportDPI=Ajuster la résolution de l'&état

SchematicContextMenuProvider.label.ResetToScreenDPI=Ajuster la résolution de l'é&cran

SchematicContextMenuProvider.label.RestToOrginalPixels=En nombre de &pixels d'origine

SchematicContextMenuProvider.Menu.EditGroup=Modifier le &groupe

SchematicContextMenuProvider.Menu.Insert=&Insérer

SchematicContextMenuProvider.Menu.insertElement=I&nsérer

SchematicContextMenuProvider.Menu.insertRow=&Ligne

SchematicContextMenuProvider.Menu.List=Liste

SchematicContextMenuProvider.Menu.Show=Afficher

SchematicContextMenuProvider.Menu.Style=St&yle

SchematicContextMenuProvider.Menu.Style.Apply=A&ppliquer un style

SchematicContextMenuProvider.Menu.Theme=Thè&me

SchematicContextMenuProvider.Menu.Theme.Apply=A&ppliquer le thème

SchematicContextMenuProvider.Menu.Layout=Préférences de mise en p&age
#SchematicContextMenuProvider.menutitle.applystylerule=Apply Style
#SchematicContextMenuProvider.menutitle.applythemerule=&Apply Theme
#SchematicContextMenuProvider.menutitle.editgroup=Edit Group
#SchematicContextMenuProvider.menutitle.insert=Insert
#SchematicContextMenuProvider.menutitle.list=List
#SchematicContextMenuProvider.menutitle.show=Show
#SchematicContextMenuProvider.menutitle.style=Style
SchematicContextMenuProvider.Menu.EditStyle=&Modifier le style

SchematicContextMenuProvider.Menu.DeleteStyle=Supprimer le &style

SchematicContextMenuProvider.Menu.DeleteGroup=Supprimer &le groupe

SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter=Insérer &En-tête/Pied de page de groupe

SchematicContextMenuProvider.Menu.NewStyle=Nouveau st&yle

###########################################################
#ApplyLayoutPreferenceAction properties
ApplyLayoutPreferenceAction.autoLayout=Mise en page &automatique

ApplyLayoutPreferenceAction.fixedLayout=Mise en page &fixe

###########################################################
#SectionProcess properties
SectionProcess.action.Clear=Effacer

SectionProcess.action.Delete=Supprimer

SectionProcess.action.DeleteRow=Supprimer la ligne

SectionProcess.action.New=Nouveau

SectionProcess.action.NewCell=Nouvelle cellule

SectionProcess.action.NewGroup=Nouveau groupe

SectionProcess.action.NewRow=Nouvelle ligne

SectionProcess.action.NewSection=Nouvelle section

SectionProcess.action.NewSubSection=Nouvelle sous-section

SectionProcess.displayname.GridProcess=Grille

SectionProcess.displayname.GroupProcess=Groupe

SectionProcess.displayname.ListProcess=Liste

SectionProcess.displayname.TableProcess=Table

SectionProcess.displayname.TableRowProcess=Ligne

SectionProcess.type.Container=(Conteneur)

SectionProcess.type.Data=(Données)

SectionProcess.type.Image=(Image)

SectionProcess.type.Label=(Etiquette)

SectionProcess.type.List=(Liste)

SectionProcess.type.Prefix=Colonne

SectionProcess.type.Table=(Table)

SectionProcess.type.Text=(Texte)

###########################################################
#SelectAllAction properties
SelectAllAction.text=Sélectionner &tout

###########################################################
#SelectCssStyleWizard properties
SelectCssStyleWizard.wizard.title=Importer les styles CSS

SelectCssStyleWizard.wizardPage.description.report=Sélectionnez un fichier CSS, puis importez les styles dans le modèle d'état.

SelectCssStyleWizard.wizardPage.description.library=Sélectionnez un fichier CSS, puis importez les styles dans la bibliothèque.

SelectCssStyleWizard.wizardPage.name=Sélectionner les styles CSS

SelectCssStyleWizard.wizardPage.title=Importer le style depuis un fichier CSS

###########################################################
#UseCssInReportDialog properties
UseCssInReportDialog.Wizard.Title=Utiliser CSS

UseCssInReportDialog.Wizard.Filename=&Nom de fichier :

UseCssInReportDialog.Label.Styles=&Styles disponibles à partir de {0}

UseCssInReportDialog.Label.notifications=&Notifications

UseCssInReportDialog.TitleArea.Title=Utiliser le fichier CSS dans le modèle d'état

UseCssInReportDialog.TitleArea.Message=Sélectionnez un fichier CSS dans le dossier des bibliothèques partagées et utilisez ses styles dans le modèle d'état.

UseCssInReportDialog.Label.No.File=Aucun fichier CSS sélectionné

UseCssInReportDialog.Dialog.Browse=Parcourir

UseCssInReportDialog.Dialog.Browse.Title=Sélectionnez un fichier CSS à inclure dans l'état

UseCssInReportDialog.Dialog.Browse.Library.Title=Sélectionnez un fichier CSS à inclure dans la bibliothèque

UseCssInReportDialog.Error.Already.Include={0} avait déjà été inclus

UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text=&Inclure fichier CSS à l'heure d'affichage (appliquer uniquement au format HTML)

UseCssInReportDialog.Dialog.Label.viewTimeLb=Afficher fuseau horaire du fichier CSS :

UseCssInReportDialog.Dialog.Label.example=http://mondomaine/test.css, mondossier/test/css, par exemple

UseCssInReportDialog.Dialog.Label.Theme.Text=&Thème :

UseCssInReportDialog.Dialog.Text.uri=&URI:
UseCssInThemeDialog.TitleArea.Title=Utiliser le fichier CSS dans la bibliothèque d'états

UseCssInThemeDialog.TitleArea.Message=Sélectionnez un thème dans lequel les styles du fichier CSS doivent être placés.

CssStyleSheetNodeProvider.Label.FromServer=A partir du serveur

CssStyleSheetNodeProvider.Tooltip.URI=(Appliqué à l'heure d'affichage)


###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=Erreur de récupération de la liste

SelectValueDialog.messages.info.selectVauleUnavailable=La liste de valeurs n'est pas disponible.

SelectValueDialog.messages.info.invalidSelectVauleExpression=La fonction "Sélectionner une valeur" accepte uniquement une expression de colonne simple. Toute expression compliquée est rejetée.

SelectValueDialog.messages.warning.BlobColumn.unsupported=Impossible de récupérer la liste de valeurs de l'expression référencée vers la liaison/colonne de type Blob.

SelectValueDialog.messages.error.selectVauleUnavailable=La valeur de l'expression ne peut pas être récupérée.

SelectValueDialog.messages.error.selectValueNotSupported=Ne prend pas en charge cette valeur de sélection.

SelectValueDialog.noExpressionSet=Une expression n'a pas été définie

SelectValueDialog.retrieving=Récupération de la liste...

SelectValueDialog.selectValue=Sélectionner une valeur

SelectValueDialog.SelectValue.NullValue=<Valeur nulle>

###########################################################
#SetConstraintCommand properties
SetConstraintCommand.Error.SetConstraintError=Une erreur s'est produite lors de la définition d'une contrainte pour ce modèle.

SetConstraintCommand.transLabel.setConstraint=Définir une contrainte

SetConstraintCommand.commandLabel.ResizeCommand=Redimensionner
###########################################################
#ProjectFilterAction properties
ProjectFilterAction.text=Filtre...

###########################################################
#ResourceFileFolderSelectionAction properties
ResourceFileFolderSelectionAction.text=Filtre...

###########################################################
#ResourceFileFolderSelectionDialog properties
ResourceFileFolderSelectionDialog.Text.Menu=menu

ResourceFileFolderSelectionDialog.button.importFile=Fichier d'importation...

ResourceFileFolderSelectionDialog.title.error=Erreur

ResourceFileFolderSelectionDialog.title.overwrite=Ecraser

ResourceFileFolderSelectionDialog.overwrite.msg=Un fichier portant ce nom existe déjà. Voulez-vous l'écraser ?

ResourceFileFolderSelectionDialog.import.msg=Importation du fichier...

###########################################################
#ShowPropertyAction properties
ShowPropertyAction.text=P&ropriétés

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=Trier selon :

###########################################################
#Spinner properties
Spinner.DialogTitle.InvalidNumber=Nombre non valide

###########################################################
#SplitAction properties
SplitAction.actionMsg.splitCells=&Fractionner la cellule

###########################################################
#StyleBuilder properties
StringVariableSelectionDialog.Dialog.Label=Description de la &variable :

StringVariableSelectionDialog.Dialog.Prompt=&Choisir une variable (? = tout caractère, * = toute chaîne) :

StringVariableSelectionDialog.Dialog.Title=Sélectionnez une variable

StyleBuilder.action.Apply=&Appliquer

StyleBuilder.Edit.Info=Modifiez les propriétés de style souhaitées. Tous les éléments d'état utilisant ce style sont automatiquement mis à jour pour refléter le nouveau format.

StyleBuilder.New.Info=Créez un nouveau style en sélectionnant une catégorie de propriété sur la gauche et en spécifiant les valeurs de la propriété.

StyleBuilder.Title=Style

SytleBuilder.DialogTitle.New=Nouveau style

SytleBuilder.DialogTitle.Edit=Modifier le style

###########################################################
#StyleComboProvider properties
StyleComboProvider.LineStyle.None=Aucun style

###########################################################
#StyleNodeProvider properties
StyleNodeProvider.action.Edit=Modifi&er le style...

StyleNodeProvider.action.New=&Nouveau style...

###########################################################
#StyleProcess properties
StyleProcess.action.Delete=Supprimer le style

StyleProcess.action.Edit=Modifi&er le style...

StyleProcess.action.New=&Nouveau style...

StyleProcess.text.CannotDelete=Impossible de supprimer le style par défaut.

StyleProcess.text.Show=Affiche l'éditeur de jeu de données pour ce jeu de données.

###########################################################
#StylesNodeProvider properties
StylesNodeProvider.action.New=&Nouveau style...

###########################################################
#StylesOutlineProcess properties
StylesOutlineProcess.action.New=&Nouveau style...

###########################################################
#TableBandProvider properties
TableBandProvider.action.text.group=I&nsérer le groupe

TableBandProvider.action.text.row=In&sérer une ligne

###########################################################
#TableEditPart properties
TableEditPart.GUIDEHANDLE_TEXT =Table

TableEditPart.Label.Merge=Fusionner les cellules

TableEditPart.Label.ResizeColumn=Redimensionner la colonne

TableEditPart.Label.ResizeRow=Redimensionner la ligne

###########################################################
#TableFigure properties
TableFigure.BORDER_TEXT=Table

TableArea.Button.New=&Nouveau...

TableArea.Button.Edit=&Modifier...

TableArea.Button.Remove=&Supprimer

TableArea.Button.Down=Vers le &bas

TableArea.Button.RemoveAll=Tout suppri&mer

###########################################################
#TableHandleAdapter properties
TableHandleAdapter.name.detail=détails

TableHandleAdapter.name.footer=pied de page

TableHandleAdapter.name.header=en-tête

TableHandleAdapter.transLabel.deleteColumn=Supprimer la colonne

TableHandleAdapter.transLabel.deleteColumns=Supprimer les colonnes

TableHandleAdapter.transLabel.deleteRow=Supprimer la ligne

TableHandleAdapter.transLabel.deleteRows=Supprimer les lignes

TableHandleAdapter.transLabel.include=Inclure

TableHandleAdapter.transLabel.insertColumn=insérer une colonne

TableHandleAdapter.transLabel.insertGroup=insérer le groupe

TableHandleAdapter.transLabel.insertRow=insérer une ligne

TableHandleAdapter.transLabel.notInclude=Exclure

TableHandleAdapter.transLabel.splitCells=Fractionner la cellule

TableHandleAdapter.transLable.deleteGroup=Supprimer le groupe de tables

###########################################################
#TableOptionDialog properties
TableOptionDialog.message.RememberGrid=&Utiliser ces dimensions pour les nouvelles grilles

TableOptionDialog.message.RememberTable=&Utiliser ces dimensions pour les nouvelles tables

TableOptionDialog.text.Column=Nombre de &colonnes :

TableOptionDialog.text.GridRow=Nombre de &lignes :

TableOptionDialog.text.GridSize=Taille de la grille

TableOptionDialog.text.TableDetail=Nombre de &détails :

TableOptionDialog.text.TableSize=Taille de la table

TableOptionDialog.title.InsertGrid=Insérer une grille

TableOptionDialog.title.InsertTable=Insérer une table

TableOptionDialog.text.DataSet=Jeu de donnée&s :

TableOptionDialog.text.AutoSummary=Résu&mé auto activé

TableOptionPage.Title=Insérer une table

###########################################################
#TablePageGenerator properties
TablePageGenerator.List.Bookmark=Signet

TablePageGenerator.List.Borders=Bordure

TablePageGenerator.List.CellPadding=Marge intérieure

TablePageGenerator.List.Font=Police

TablePageGenerator.List.General=Général

TablePageGenerator.List.Marign=Marge

TablePageGenerator.List.Section=Saut de page

TablePageGenerator.List.TOC=Table des matières

TablePageGenerator.List.Visibility=Visibilité

###########################################################
#TemplateReportItemPropertiesDialog properties
TemplateReportItemPropertiesDialog.errorMessage=Le texte d'invite ne peut pas être vierge.

TemplateReportItemPropertiesDialog.errorTitle=Erreur

TemplateReportItemPropertiesDialog.group=Propriétés de l'élément d'état modèle

TemplateReportItemPropertiesDialog.objectType=Type d'élément :

TemplateReportItemPropertiesDialog.nameText=&Nom :

TemplateReportItemPropertiesDialog.promptText=&Instructions :

TemplateReportItemPropertiesDialog.title=Créer un élément d'état modèle

###########################################################
#performCreatePlaceHolder action
performCreatePlaceHolder.errorMessage.title=Erreur

performCreatePlaceHolder.errorMessage.content=Le nom spécifié existe déjà. Veuillez en choisir un autre ou laisser ce champ vierge.

###########################################################
#TogglePropertyDescriptor properties
TogglePropertyDescriptor.toolTipText.Bold=Gras

TogglePropertyDescriptor.toolTipText.Italic=Italique

TogglePropertyDescriptor.toolTipText.Underline=Texte souligné

TogglePropertyDescriptor.toolTipText.Text_Line_Through=Ligne de texte barré

###########################################################
#TextAlignPropertyDescriptor properties
TextAlignPropertyDescriptor.0=Gauche

TextAlignPropertyDescriptor.1=Centre

TextAlignPropertyDescriptor.2=Droite

TextAlignPropertyDescriptor.3=Justifié

###########################################################
#TextAreaContextMenu properties
TextAreaContextMenu.Copy.description=Copier

TextAreaContextMenu.Copy.image=

TextAreaContextMenu.Copy.label=&Copier

TextAreaContextMenu.Copy.tooltip=Copier

TextAreaContextMenu.Cut.description=Couper

TextAreaContextMenu.Cut.image=

TextAreaContextMenu.Cut.label=Co&uper

TextAreaContextMenu.Cut.tooltip=Couper

TextAreaContextMenu.Paste.description=Coller

TextAreaContextMenu.Paste.image=

TextAreaContextMenu.Paste.label=C&oller

TextAreaContextMenu.Paste.tooltip=Coller

TextAreaContextMenu.Redo.description=Rétablir

TextAreaContextMenu.Redo.image=

TextAreaContextMenu.Redo.label=&Rétablir

TextAreaContextMenu.Redo.tooltip=Rétablir

TextAreaContextMenu.SelectAll.description=Sélectionner tout

TextAreaContextMenu.SelectAll.image=

TextAreaContextMenu.SelectAll.label=Sélecti&onner tout

TextAreaContextMenu.SelectAll.tooltip=Sélectionner tout

TextAreaContextMenu.Undo.description=Annuler

TextAreaContextMenu.Undo.image=

TextAreaContextMenu.Undo.label=&Annuler

TextAreaContextMenu.Undo.tooltip=Annuler

###########################################################
#TextDataEditPart properties
TextDataEditPart.Figure.Dafault=Texte dynamique

###########################################################
#TextEditDialog properties
TextEditDialog.action.text.direction=Sens de lectu&re de droite à gauche

TextEditDialog.action.text.editDynamicText=&Modifier le texte dynamique

TextEditDialog.action.text.formatDateTime=Date/heur&e

TextEditDialog.action.text.formatNumber=&Nombre

TextEditDialog.action.text.formatString=&Chaîne de format

TextEditDialog.action.text.formatHTML=Format &HTML

TextEditDialog.choice.HTML=Texte HTML/Texte dynamique

TextEditDialog.choice.plainText=Texte brut

TextEditDialog.choice.Auto=Auto

TextEditDialog.comboText.formattingAndLayout=Formatage et mise en page

TextEditDialog.format.dynamicText=Texte dynamique

TextEditDialog.format.formatting=Formatage et mise en page

TextEditDialog.format.lists=Listes

TextEditDialog.format.pageComposition=Composition de page

TextEditDialog.format.pageElements=Eléments de page

TextEditDialog.formatChoice.content=Contenu

TextEditDialog.formatChoice.dynamicText=Texte dynamique

TextEditDialog.formatChoice.formatting=Formatage

TextEditDialog.formatChoice.layout=Mise en page

TextEditDialog.formatChoice.lists=Listes

TextEditDialog.toolTip.tag.a=Ancre

TextEditDialog.toolTip.tag.b=Style de texte en gras

TextEditDialog.toolTip.tag.birtImage=Image BIRT spécifique

TextEditDialog.toolTip.tag.br=Saut de ligne

TextEditDialog.toolTip.tag.center=Centrer le texte

TextEditDialog.toolTip.tag.code=Fragment de code machine

TextEditDialog.toolTip.tag.dd=Description de la définition

TextEditDialog.toolTip.tag.del=Texte supprimé

TextEditDialog.toolTip.tag.dl=Liste de définition

TextEditDialog.toolTip.tag.dt=Terme de la définition

TextEditDialog.toolTip.tag.em=Accentuation

TextEditDialog.toolTip.tag.font=Remplacement local de police

TextEditDialog.toolTip.tag.genericStyleContainer=Conteneur de styles/Langage générique

TextEditDialog.toolTip.tag.h1=Niveau de titre 1

TextEditDialog.toolTip.tag.h2=Niveau de titre 2

TextEditDialog.toolTip.tag.h3=Niveau de titre 3

TextEditDialog.toolTip.tag.h4=Niveau de titre 4

TextEditDialog.toolTip.tag.h5=Niveau de titre 5

TextEditDialog.toolTip.tag.h6=Niveau de titre 6

TextEditDialog.toolTip.tag.i=Style de texte en italique

TextEditDialog.toolTip.tag.img=Référence d'image HTML standard

TextEditDialog.toolTip.tag.ins=Texte inséré

TextEditDialog.toolTip.tag.li=Elément de liste

TextEditDialog.toolTip.tag.p=Paragraphe

TextEditDialog.toolTip.tag.pre=Texte préformaté

TextEditDialog.toolTip.tag.strong=Accentuation renforcée

TextEditDialog.toolTip.tag.sub=Indice

TextEditDialog.toolTip.tag.sup=Exposant

TextEditDialog.toolTip.tag.tt=Style de texte télétype ou à espacement fixe

TextEditDialog.toolTip.tag.u=Style de texte souligné

TextEditDialog.toolTip.tag.ul=Liste non numérotée

TextEditDialog.toolTip.valueOf=Valeur de

TextEditDialog.toolTipText.copy=Copier

TextEditDialog.toolTipText.cut=Couper

TextEditDialog.toolTipText.delete=Supprimer

TextEditDialog.toolTipText.paste=Coller

TextEditDialog.toolTipText.redo=Rétablir

TextEditDialog.toolTipText.undo=Annuler

TextEditDialog.toolTipText.fx=Ouvrir le générateur d'expression

TextEditDialog.action.item.formatHTML=Format HTML
TextEditDialog.action.item.formatDateTime=Format de date/heure

TextEditDialog.action.item.formatNumber=Format de nombre

TextEditDialog.action.item.formatString=Chaîne

TextEditDialog.title.DlgNew=Nouvel élément de texte

TextEditDialog.title.DlgEdit=Modifier l'élément de texte

###########################################################
#TextEditPart properties
TextEditPart.Figure.Dafault=Texte

TextEditPart.trans.editText=Modifier l'élément de texte

###########################################################
#TextExtendsTools properties
TextExtendsTools.Message.DefaultAuthor=Version de Birt Eclipse Report Designer

###########################################################
#TextPageGenerator properties
TextPageGenerator.List.Bookmark=Signet

TextPageGenerator.List.Borders=Bordure

TextPageGenerator.List.General=Général

TextPageGenerator.List.I18n=Localisation

TextPageGenerator.List.Margin=Marge

TextPageGenerator.List.Padding=Remplissage

TextPageGenerator.List.Section=Saut de page

TextPageGenerator.List.TOC=Table des matières

TextPageGenerator.List.Visibility=Visibilité

###########################################################
#TextPropertyDescriptor properties
TextPropertyDescriptor.Message.Version=Eclipse BIRT Designer version {0} révision <{1}>

###########################################################
#AutoTextPageGenerator properties
AutoTextPageGenerator.List.Bookmark=Signet

AutoTextPageGenerator.List.Borders=Bordure

AutoTextPageGenerator.List.General=Général

AutoTextPageGenerator.List.Margin=Marge

AutoTextPageGenerator.List.Padding=Remplissage

AutoTextPageGenerator.List.Section=Saut de page

AutoTextPageGenerator.List.TOC=Table des matières

AutoTextPageGenerator.List.Visibility=Visibilité

###########################################################
#ThemesNodeProvider properties
ThemesNodeProvider.action.New=&Nouveau thème...

ThemesNodeProvider.Failed=Echec du nouveau thème

NewThemeDialog.DialogTitle=Nouveau thème

NewThemeDialog.DialogMessage=Saisissez le nom

###########################################################
#TimeOptionDialog properties
TimeOptionDialog.Label.Format=Format

TimeOptionDialog.Label.TimeZone=TimeZone

###########################################################
#ToggleMarginVisibilityAction properties
ToggleMarginVisibilityAction.text.Label=Afficher les &marges

ToggleMarginVisibilityAction.text.Tooltip=Basculer les marges

###########################################################
#TwinExpressionDialog properties
TwinExpressionDialog.Dialog.Title=Sélectionner des valeurs

TwinExpressionDialog.lable.value1=Valeur1 :

TwinExpressionDialog.lable.value2=Valeur2 :

###########################################################
#UndoAction properties
UndoAction.Tooltip=Annuler {0}

###########################################################
#UnitPropertyDescriptor properties
UnitPropertyDescriptor.error.message=Entrez une valeur plus petite.\

UnitPropertyDescriptor.error.title=Valeur trop grande

###########################################################
#UseLibraryAction properties
UseLibraryAction.Text=&Utiliser la bibliothèque

UseLibraryAction.Error.Title=Remplacer le fichier existant

UseLibraryAction.Error.Message=Le fichier ''{0}'' existe déjà dans le dossier de ressources BIRT. Souhaitez-vous remplacer le fichier existant ?

###########################################################
#UseCSSAction properties
UseCSSAction.Text=&Utiliser CSS

###########################################################
#UserPropertyBuilder properties
UserPropertyBuilder.0=Nouvelle propriété d'utilisateur

UserPropertyBuilder.ErrorMessage.DuplicatedName=Le nom spécifié est déjà utilisé.

UserPropertyBuilder.ErrorMessage.NoName=Veuillez spécifier un nom.

UserPropertiesHandleProvier.Column.Name=Nom

UserPropertiesHandleProvier.Column.Type=Type

UserPropertyBuilder.Label.DefaultValue=&Valeur par défaut :

UserPropertyBuilder.Label.ExpressionName=&Nom de l'expression :

UserPropertyBuilder.Label.PropertyName=&Nom de la propriété :

UserPropertyBuilder.Label.PropertyType=&Type de propriété :

UserPropertyBuilder.Title.NamedExpression=Nouvelle expression nommée

###########################################################
#VisibilityPage properties
VisibilityPage.Check.HideElement=&Masquer l'élément

VisibilityPage.Check.HTML=H&TML
VisibilityPage.Check.PDF=&PDF
VisibilityPage.Label.DetailAll=Détails (Toutes les sorties)

VisibilityPage.Label.DetailSpecific=Détails (Sorties spécifiques)

VisibilityPage.Label.Expression=Expression :

VisibilityPage.menu.SaveHides=Enregistrer les éléments masqués

VisibilityPage.Radio.AllOutputs=&Pour toutes les sorties

VisibilityPage.Radio.SpecificOutputs=Pour les sorties spé&cifiques...

###########################################################
#WidgetUtil properties
WidgetUtil.MessageBox.Error=Erreur

###########################################################
#wizard properties
wizard.transaction.createDataSource=Définir le nom et le type de la source de données.

###########################################################
#WizardChoicePage properties
WizardChoicePage.radio.createBlank=Créer un état vierge sans modèle.

WizardChoicePage.radio.createFromCustom=Créer l'état comme copie d'un autre état.

WizardChoicePage.radio.createFromTemplate=Créer l'état d'après un modèle.

###########################################################
#WizardCustomTemplatePage properties
WizardCustomTemplatePage.button.Browse=Parcourir

WizardCustomTemplatePage.label.FileName=Enter ou sélectionner le nom d'un fichier d'état existant.

WizardCustomTemplatePage.label.FromLocal=Parcourir depuis le système de fichiers local.

WizardCustomTemplatePage.label.FromProjectDescription=Choisir un fichier d'état existant.

WizardCustomTemplatePage.label.FromProjectTitle=Sélection du fichier d'état

###########################################################
#WizardNewReportCreationPage properties
WizardNewReportCreationPage.Errors.nameExists=Ce nom existe déjà.

WizardNewReportCreationPage.Errors.nameEmpty=Les noms doivent être renseignés.

WizardNewReportCreationPage.label.browse=Parcourir...

WizardNewReportCreationPage.label.directory=Répertoire :

WizardNewReportCreationPage.label.file.location=Emplacement du fichier

WizardNewReportCreationPage.label.file.name=Nom du fichier :

WizardNewReportCreationPage.label.select.directory=Sélectionnez un répertoire.

WizardNewReportCreationPage.label.useDefault=Utiliser la valeur par défaut

WizardNewReportCreationPage.msg.duplicate.fileName=Ce nom existe déjà.

WizardNewReportCreationPage.msg.empty.file.locationDirectory=Le répertoire d'emplacement du fichier doit être renseigné.

WizardNewReportCreationPage.msg.empty.file.name=Le nom du fichier doit être renseigné.

###########################################################
#WizardSelectCssStylePage properties
WizardSelectCssStylePage.button.label.browse=& Parcourir...

WizardSelectCssStylePage.button.label.deselectAll=&Désélectionner tout

WizardSelectCssStylePage.button.label.selectAll=&Sélectionner tout

WizardSelectCssStylePage.errorMsg.FileNotExist=Le fichier n'existe pas.

WizardSelectCssStylePage.info.canNotImport=\ \ (importation impossible)

WizardSelectCssStylePage.label.fileName=&Nom de fichier :

WizardSelectCssStylePage.label.importToReportDesign=\ pour importer dans le modèle d'état.

WizardSelectCssStylePage.label.notifications=Notifications :

WizardSelectCssStylePage.label.selectStylesFrom=Sélectionner les styles dans

WizardSelectCssStylePage.msg.FileNameEmpty=Le nom du fichier doit être renseigné.

WizardSelectCssStylePage.text.cannot.import.style=\ - Impossible d'importer le style.

WizardSelectCssStylePage.FileNameError.Title=Erreur

WizardSelectCssStylePage.FileNameError.Message=Le fichier sélectionné n'est pas valide.

###########################################################
#WizardTemplateChoicePage properties
WizardTemplateChoicePage.label.Description=Description :

WizardTemplateChoicePage.label.Preview=Aperçu :

WizardTemplateChoicePage.label.ReportTemplates=Modèles d'état :

WizardTemplateChoicePage.label.ShowCheatSheets=&Afficher la feuille de fraude de création d'état

WizardTemplateChoicePage.label.EnableDefaultThemes=&Inclure les thèmes par défaut

WizardTemplateChoicePage.message.BlankReport=Créez un état vierge sans aucun contenu prédéfini.

WizardTemplateChoicePage.message.ChartListing=Créez un état groupé avec un graphique fournissant une représentation visuelle de chaque donnée de groupe.

WizardTemplateChoicePage.message.DualColumnChartListing=Créez un état groupé avec deux sous-groupes "Graphiques et listes".

WizardTemplateChoicePage.message.DualColumnListing=Créez un état groupé avec deux sous-groupes.

WizardTemplateChoicePage.message.FirstReport=Fournit des instructions sur la création de listes de clients.

WizardTemplateChoicePage.message.GroupedListing=Créez une liste dans les états avec regroupement dans certains champs.

WizardTemplateChoicePage.message.Letter=Créez un modèle permettant de générer des lettres personnalisées en utilisant les champs issus de la source de données.

WizardTemplateChoicePage.message.MailingLabels=Etiquettes de publipostage

WizardTemplateChoicePage.message.SideBySideChartListing=Créez un état groupé dans lequel les groupes contiennent chacun un graphique et une liste côte à côte.

WizardTemplateChoicePage.message.SimpleListing=Créez une liste avec des champs issus de la source de données.

WizardTemplateChoicePage.message.Crosstab=Utiliser un didacticiel pour générer un état de tableau croisé

WizardTemplateChoicePage.message.BlankAuto=Modèle d'état vierge qui activera automatiquement l'interactivité lors de son affichage.

WizardTemplateChoicePage.title.BlankAuto=Modèle d'état interactif automatique vierge

WizardTemplateChoicePage.title.BlankReport=Etat vierge

WizardTemplateChoicePage.title.ChartListing=Graphiques et listes

WizardTemplateChoicePage.title.DualColumnChartListing=Graphiques et listes de colonnes doubles

WizardTemplateChoicePage.title.DualColumnListing=Listes de colonnes doubles

WizardTemplateChoicePage.title.FirstReport=Mon premier état

WizardTemplateChoicePage.title.GroupedListing=Listes groupées

WizardTemplateChoicePage.title.Letter=Lettre

WizardTemplateChoicePage.title.MailingLabels=Etiquettes de publipostage

WizardTemplateChoicePage.title.SideBySideChartListing=Graphiques et listes juxtaposés

WizardTemplateChoicePage.title.SimpleListing=Listes simples

WizardTemplateChoicePage.title.Crosstab=Tableau croisé
#bidi_hcg start
WizardTemplateChoicePage.label.rtlBiDiOrientation=Droite à gauche

WizardTemplateChoicePage.label.ltrBiDiOrientation=Gauche à droite

WizardTemplateChoicePage.label.chooseBiDiDirection=O&rientation de l'état :

WizardTemplateChoicePage.RootDisplayName=Standard
#bidi_hcg end
###########################################################
#XMLSourcePage properties
XMLSourcePage.Error.Dialog.Message.InvalidFile=Une erreur dans la page XML source doit être corrigée avant l'utilisation d'autres pages.

XMLSourcePage.Error.Dialog.Message.SaveFile=La page XML source a été modifiée. Les autres pages ne pourront être utilisées qu'une fois la source XML enregistrée.

XMLSourcePage.Error.Dialog.title=Erreur

XMLSourcePage.Error.Dialog.Message.PromptMsg=La source du modèle d'état XML a été modifiée. Voulez-vous enregistrer les modifications ?

XMLSourcePage.Error.Dialog.Message.Yes=Oui

XMLSourcePage.Error.Dialog.Message.No=Non

XMLSourcePage.Error.Dialog.Message.Cancel=Annuler

###########################################################
#SaveReportAsWizard properties
SaveReportAsWizard.SettingPage.title=Définir les propriétés du modèle

SaveReportAsWizard.SaveAsPageTitle=Enregistrer sous

SaveReportAsWizard.SaveAsLibraryMessage=Enregistrez le fichier en tant que bibliothèque sous un autre nom.

SaveReportAsWizard.SaveAsReportorTemplateMessage=Enregistrez le fichier en tant que modèle d'état ou modèle sous un autre nom.

SaveReportAsWizard.SettingPageTitle=Définir la propriété de l'état

SaveReportAsWizard.SettingPage.message=Ces propriétés sont utilisées dans les cas où l'état est enregistré en tant que modèle.

SaveReportAsWizard.SaveAsWizardWindowTitle=Enregistrer sous

SaveReportAsWizard.SaveAsWizardPageTitle=Enregistrer sous

SaveReportAsWizard.SaveAsWizardPageDesc=Enregistrer le fichier à un autre emplacement.

SaveReportAsWizard.SettingPage.Messages=Définissez les propriétés du modèle.

###########################################################
#WizardReportSettingPage properties
WizardReportSettingPage.Error.ReportorTemplate=Le nom de fichier doit se terminer par {0} ou .rpttemplate.

WizardReportSettingPage.Error.Library=Le nom de fichier doit se terminer par .rptlibrary.

###########################################################
#WizardSaveAsPage properties
WizardSaveAsPage.FileLabel=&Nom de fichier :

WizardSaveAsPage.Question=Question

WizardSaveAsPage.FolderEmpty=\ Le dossier est vide.

WizardSaveAsPage.NoProject=\ Le projet spécifié n'existe pas.

WizardSaveAsPage.PathOccupied=\ Un fichier existe déjà à cet emplacement : {0}

WizardSaveAsPage.EmptyName=\ Le nom ''{0}'' est vide.

WizardSaveAsPage.SelectFolder=\ Sélectionner le dossier :

WizardSaveAsPage.NameExists=\ Ce nom existe déjà.

WizardSaveAsPage.OverwriteQuestion=Le fichier ''{0}'' existe déjà. Voulez-vous remplacer le fichier existant ?

WizardSaveAsPage.InvalidFileName=\ ''{0}'' n''est pas un nom de fichier valide.

WizardSaveAsPage.ContainerGroup=\ &Entrer ou sélectionner le dossier parent :

###########################################################
#Library DND message
Library.DND.messages.outofsync = La bibliothèque utilisée dans l'état a probablement été modifiée ; fermez l'état puis ouvrez-le de nouveau pour utiliser la bibliothèque mise à jour.

NamedExpressionsHandleProvier.Column.Name=Nom

NamedExpressionsHandleProvier.Column.DefaultValue=Valeur par défaut

Library.DND.messages.cannotApplyTheme=Le thème ne peut pas être appliqué.

JarFileFormProvider.Column.Name=Nom

JsFileFormProvider.Column.Name=Nom

ReportPageGenerator.List.Resources.JsFile=Fichiers Javascript :

PropertiesFileFormProvider.Column.Name=Nom

ReportPageGenerator.List.Resources.PropertiesFile=Fichiers de propriétés :

###########################################################
#ClassFinder
ClassFinder.TaskStart=Recherche de la classe de gestionnaire...

ClassFinder.DialogTitle=Sélection de la classe

ClassFinder.DialogMessage=&Choisir une classe de gestionnaire :

ClassFinder.ErrorMessage=Aucune classe trouvée

ClassPathBlock_button.addExtFolder=Ajouter un &dossier de classe externe...

ClassPathBlock_button.addExtJars=Ajouter des JAR e&xternes...

ClassPathBlock_button.down=Vers le bas

ClassPathBlock_button.edit=Modifi&er...

ClassPathBlock_button.remove=Supp&rimer

ClassPathBlock_button.up=Vers le haut

ClassPathBlock_FileDialog.edit.text=Modifier un JAR

ClassPathBlock_FileDialog.jar.text=Sélection de JAR

ClassPathBlock_FolderDialog.edit.message=Spécifiez le nouvel emplacement du dossier de classe :

ClassPathBlock_FolderDialog.edit.text=Modifier le dossier de classe

ClassPathBlock_FolderDialog.message=Sélectionnez l'emplacement du dossier de classe :

ClassPathBlock_FolderDialog.text=Sélection du dossier de classe externe

ClassPathBlock_label.LibrariesList=Dossiers de classe et plusieurs JAR dans le ch&emin d'exécution de l'état :

###########################################################
#ListingSectionPage
ListingSectionPage.RepeatHeader=&Répéter l'en-tête

###########################################################
#ResourceEditDialog
ResourceEditDialog.NotFile.Title=Fichier introuvable

ResourceEditDialog.NotFile.Message=Le fichier ''{0}'' n'existe pas.

ResourceEditDialog.ReadOnlyEncounter.Title=Fichier en lecture seule

ResourceEditDialog.ReadOnlyEncounter.Message=Le fichier ''{0}'' est en lecture seule, vous devez d''abord disposer des droits en écriture.

###########################################################
#EventHandler
EventHandlerPage.Browse=& Parcourir...


###########################################################
#IDEFileReportProvider
IDEClassPathBlock.button_addExtFolder=Ajouter un &dossier de classe externe...

IDEClassPathBlock.button_addEXTJar=Ajouter des JAR e&xternes...

IDEClassPathBlock.button_addJar=Ajouter des &JAR...

IDEClassPathBlock.button_addProject=Ajouter des &projets...

IDEClassPathBlock.button_addVar=Ajouter une &variable...

IDEClassPathBlock.button_down=Vers le bas

IDEClassPathBlock.button_edit=Modifi&er...

IDEClassPathBlock.button_folder=Ajouter un dossier de &classe...

IDEClassPathBlock.button_remove=Supp&rimer

IDEClassPathBlock.button_up=Vers le haut

IDEClassPathBlock.fLibrariesList_text=Dossiers de classe et plusieurs JAR dans le ch&emin d'exécution de l'état :

IDEClassPathBlock.ProjectDialog_message=Sélectionnez des projets à ajouter :

IDEClassPathBlock.ProjectDialog_title=Sélection de projet obligatoire

IDECPListLabelProvider.folder=(dossier de classe)

IDECPListLabelProvider.missing=(manquant)

IDECPListLabelProvider.new=(nouveau)

IDECPListLabelProvider.unknown=élément inconnu

IDEFileReportProvider.ReadOnlyEncounter.Title=Fichier en lecture seule

IDEFileReportProvider.ReadOnlyEncounter.Message=Le fichier ''{0}'' est en lecture seule, vous devez d''abord disposer des droits en écriture.

IDEResourcePageHelper.Dialog.Prompt=Choisissez un répertoire :

IDEResourcePageHelper.Dialog.Text=&Choisir un emplacement correspondant à l'espace de travail :

IDEResourcePageHelper.Dialog.Title=Sélection du répertoire

IDEResourcePageHelper.File.Button=&Système de fichiers...

IDEResourcePageHelper.Varible.Button=Varia&bles...

IDEResourcePageHelper.WorkSpace.Button=&Espace de travail...

###########################################################
#DataSetColumnBindingsFormHandleProvider
DataSetColumnBindingsFormHandleProvider.Column.Position=Position
DataSetColumnBindingsFormHandleProvider.Column.Name=Nom

DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID=ID du nom d'affichage

DataSetColumnBindingsFormHandleProvider.Column.DisplayName=Nom d'affichage

DataSetColumnBindingsFormHandleProvider.DatasetTitle=Liaison de colonne de données :

DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns=Actualiser les colonnes de liaison

DataSetColumnBindingsFormHandleProvider.ReportItemTitle=Liaison de colonne disponible :

DataSetColumnBindingsFormHandleProvider.NewBinding=Nouvelle liaison

DataSetColumnBindingsFormHandleProvider.Column.DataType=Type de données

DataSetColumnBindingsFormHandleProvider.Column.Expression=Expression

DataSetColumnBindingsFormHandleProvider.Column.AggregateOn=Regrouper par

DataSetColumnBindingsFormHandleProvider.Column.Filter=Filtrer

DataSetColumnBindingsFormHandleProvider.Column.Function=Fonction

DataSetColumnBindingsFormHandleProvider.ALL=Tous

DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns=Générer des colonnes de liaison

DataSetColumnBindingsFormHandleProvider.NONE=N/A
DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns=Générer des colonnes de liaison

DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns=Actualiser les colonnes de liaison

###########################################################
#DataSetColumnBindingsFormPage
DataSetColumnBindingsFormPage.Button.RemoveUnused=Supprimer une colonne inutilisée

###########################################################
#DataSetSettingsPage
DataSetSettingsPage.DataGroup.Title=Paramètres du moteur BIRT pour comparer les valeurs de la colonne de jeu de données

DataSetSettingsPage.Locale.Label=Informations &locales des valeurs de chaîne de caractères :

DataSetSettingsPage.NullValueOrdering.Label=&Classement des valeurs nulles :

###########################################################
#ResourceSelectionValidator
ResourceSelectionValidator.ExtError=Le fichier sélectionné doit se terminer par

ResourceSelectionValidator.EmptyError=Sélectionnez un fichier

ResourceSelectionValidator.ExtErrorSplitter=\ ou

###########################################################
#GeneralPage
GeneralPage.Library.Included=Bibliothèque :

GeneralPage.Library.Modified.Note=REMARQUE : cet élément d'état provenant de la bibliothèque a été modifié. Pour restaurer l'élément d'état d'origine, cliquez sur {0} "Restaurer les propriétés"

#BookMarkPage
BookMarkPage.Modified.Note=Le signet contient des caractères spéciaux différents de (A-Z, a-z, 0-9, '-', '_', ':', '.'). Cela peut poser problème lors de l'exécution.

BookMarkPage.Modified.WithoutQuotes=Le signet n'est pas entouré de guillemets doubles. Cela peut poser problème lors de l'exécution.

###########################################################
# LOCALE SPECIAL:
# following string need be specially localized, e.g. as per local country custom, not just simple translation
# for example:
#       the social security number in US is like 760-01-0101, the format should be @@@-@@-@@@@
#       but the SSN in Swedish is like 760101-0101, the format should be @@@@@@-@@@@
# 
FormatStringPage.SimpleTextZipCode=94103

FormatStringPage.SimpleTextZipCode4=94123

FormatStringPage.PhoneNumber=4155551111

FormatStringPage.SocialSecurityNumber=123456789

FormatStringPage.simpleTexZipCodeFormat=@@@@@

FormatStringPage.simpleTexZipCode4Format=@@@@@-@@@@
FormatStringPage.phoneNumberFormat=(@@@)@@@-@@@@
FormatStringPage.securityNumberFormat=@@@-@@-@@@@
FormatStringPage.Label.PreserveWhiteSpaces=Conserver les espaces

FormatStringPage.Preview.PreserveWhiteSpaces=\    chaîne contenant un interlignage

###########################################################
#PublishLibraryDialog
PublishLibraryDialog.ShellText=Partager la bibliothèque

PublishLibraryDialog.TitleArea=Partager la bibliothèque

PublishLibraryDialog.Message=Copiez la bibliothèque dans le dossier de ressources partagées.

PublishLibraryDialog.Label.SourceFileName=&Fichier source :

PublishLibraryDialog.Label.FileName=&Nom de fichier :

PublishLibraryDialog.Label.Folder=&Dossier :

PublishLibraryDialog.Label.Browse=& Parcourir...

PublishLibraryDialog.Message.FileNameEmpty=Nom de fichier requis.

PublishLibraryDialog.Message.FileNameError=Le nom de fichier doit se terminer par .rptlibrary.

PublishLibraryDialog.Message.FolderEmpty=Dossier de publication requis.

PublishLibraryDialog.Message.SourceFileEmpty=Nom de fichier source requis.

PublishLibraryDialog.Label.Browse2=Par&courir...

PublishLibraryDialog.AddText=Ajouter une bibliothèque

PublishLibraryDialog.AddMessage=Ajouter la bibliothèque dans le dossier de ressources BIRT

WizardLibrarySettingPage.ErrorMessage=Sélectionner un dossier

WizardLibrarySettingPage.Resourcefile.Dialog.Title=Parcourir les dossiers de ressources

WizardLibrarySettingPage.Resourcefile.Dialog.Message=Sélectionnez le dossier de ressources ou un de ses sous-dossiers.

PublishLibraryDialog.AddLibrary=Ajouter une bibliothèque


###########################################################
#PublishCSSDialog
PublishCSSDialog.ShellText=Publier le fichier CSS

PublishCSSDialog.TitleArea=Publier le fichier CSS dans le dossier de ressources

PublishCSSDialog.Message=Sélectionner un fichier CSS pour importer des styles dans le dossier de ressources

PublishCSSDialog.Label.SourceFileName=&Fichier source :

PublishCSSDialog.Label.FileName=&Nom de fichier :

PublishCSSDialog.Label.Folder=&Dossier :

PublishCSSDialog.Label.Browse=& Parcourir...

PublishCSSDialog.Label.Browse2=Par&courir...

PublishCSSDialog.ErrorMessage.SelectFolder=Sélectionner un dossier

PublishCSSDialog.Dialog.FileSeclect.Tilte=Sélectionner un fichier CSS

PublishCSSDialog.Error.Exception.Title=Erreur

PublishCSSDialog.Error.FileNotFound=Fichier ''{0}'' introuvable.

PublishCSSDialog.Error.FileIsNotCSSFile=Le fichier ''{0}'' ne se termine pas par ''{1}''.

PublishCSSDialog.Error.FileExist=Ce fichier existe déjà.

PublishCSSDialog.Message.SourceFileEmpty=Nom de fichier source requis.

PublishCSSDialog.Message.FileNameEmpty=Nom de fichier requis.

PublishCSSDialog.Message.FileNameError=Le nom de fichier doit se terminer par .css.

PublishCSSDialog.Message.FolderEmpty=Dossier de publication requis.

WizardCSSSettingPage.ErrorMessage=Sélectionner un dossier

WizardCSSSettingPage.Resourcefile.Dialog.Title=Parcourir les dossiers de ressources

WizardCSSSettingPage.Resourcefile.Dialog.Message=Sélectionnez le dossier de ressources ou un de ses sous-dossiers.

###########################################################
#PublishResourceDialog
PublishResourceDialog.ShellText=Publier les ressources

PublishResourceDialog.TitleArea=Publier les ressources dans le dossier Ressources

PublishResourceDialog.Message=Placer la ressource dans le dossier Ressources

PublishResourceDialog.AddResource=Ajouter une ressource

PublishResourceDialog.AddText=Ajouter une ressource

PublishResourceDialog.AddMessage=Ajouter une ressource dans le dossier Ressources

PublishResourceAction.wizard.errorTitle=Erreur

PublishResourceAction.wizard.message=Impossible de publier la ressource dans son propre répertoire ; sélectionnez un autre répertoire pour procéder à la publication.

PublishResourceAction.wizard.message.SourceFileNotExist=Le fichier source n'existe pas.

PublishResourceAction.wizard.notvalidfolder=Le dossier de ressources défini dans les préférences n'est pas valide.

PublishResourceDialog.Label.FileName=&Nom de fichier :

PublishResourceDialog.Label.SourceFileName=&Fichier source :

PublishResourceDialog.Label.Folder=Dossier :

PublishResourceDialog.Label.Browse=& Parcourir...

PublishResourceDialog.Label.Browse2=Par&courir...

WizardResourceSettingPage.ErrorMessage=Sélectionner un dossier

WizardResourceSettingPage.Resourcefile.Dialog.Title=Parcourir les dossiers de ressources

WizardResourceSettingPage.Resourcefile.Dialog.Message=Sélectionnez le dossier de ressources ou un de ses sous-dossiers.

PublishResourceDialog.Message.SourceFileEmpty=Nom de fichier source requis.

PublishResourceDialog.Message.FileNameEmpty=Nom de fichier requis.

PublishResourceDialog.Message.FolderEmpty=Dossier de publication requis.

###########################################################
#EmbeddedImagesNodeProvider
EmbeddedImagesNodeProvider.FileNameError.Title=Erreur

EmbeddedImagesNodeProvider.FileNameError.Message=Le fichier sélectionné n'est pas une image.

EmbeddedImagesNodeProvider.stackMsg.insert=Insérer l'image

###########################################################
#AddElementtoAction properties
AddElementtoAction.Text=&Ajouter à l'état

ApplyThemeAction.Text=&Appliquer à l'état

###########################################################
#MultiPageReportEditor properties
MultiPageReportEditor.CheckVersion.Dialog.Title=Avertissement

MultiPageReportEditor.CloseButton=Fermer

MultiPageReportEditor.ConfirmTitle=Fichier inexistant

MultiPageReportEditor.ConfirmVersion.Dialog.Message=Le modèle d'état a été créé à l'aide d'une version antérieure de BIRT Designer. L'enregistrement de ce modèle entraînera une mise à niveau vers la version actuelle ; il sera peut-être impossible de l'ouvrir dans une version antérieure de BIRT. Voulez-vous enregistrer le modèle d'état ?

MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage=Le contenu de cet éditeur a peut-être été modifié par une source externe. Voulez-vous recharger les modifications ?

MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage=Le contenu de cet éditeur a peut-être été modifié par une source externe. Voulez-vous recharger le contenu avec ces modifications ?

MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage=Le contenu de cet éditeur a peut-être été modifié par une source externe et les modifications apportées n'ont pas été enregistrées dans l'éditeur.\n\nVoulez-vous enregistrer l'éditeur et recharger les modifications externes ?

MultiPageReportEditor.ConfirmVersion.Dialog.Title=Confirmer

MultiPageReportEditor.SaveButton=&Enregistrer

MultiPageReportEditor.DiscardButton=&Refuser

MultiPageReportEditor.CancelButton=Annuler

MultiPageReportEditor.SaveConfirmMessage=Le fichier a été supprimé. Souhaitez-vous enregistrer vos modifications ou fermer l'éditeur sans enregistrer ?

TableArea.Button.Up=&Vers le haut

###########################################################
#AddJarResourceFileFolderSelectionDialog
AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist=Un ou plusieurs fichiers existent déjà.

AddJarResourceFileFolderSelectionDialog.Message=Sélectionnez {0} fichiers :

AddJarResourceFileFolderSelectionDialog.ErrorMessage.ExtError=Le nom de fichier doit se terminer par {0}.

AddJarResourceFileFolderSelectionDialog.Message.Or=ou


###########################################################
#AddImageResourceFileFolderSelectionDialog
AddImageResourceFileFolderSelectionDialog.Title=Un ou plusieurs fichiers existent déjà.

AddImageResourceFileFolderSelectionDialog.Message=Veuillez sélectionner une image :

###########################################################
#DataItemBindingDialog properties
DataItemBindingDialog.text.Name=Nom :

DataItemBindingDialog.text.displayName=Nom d'affichage :

DataItemBindingDialog.text.DataType=Type de données :

DataItemBindingDialog.text.Expression=Expression :

DataItemBindingDialog.text.AggregateOn=Regrouper par :

DataItemBindingDialog.text.ForceBinding=Créer une nouvelle liaison avec la même expression

DataItemBindingDialog.text.All=Tous

DataItemBindingDialog.text.None=N/A
DataItemBindingDialog.text.Null=Aucun

DataItemBindingDialog.title.CreateNewItem=Nouvelle liaison d'élément de données

DataItemBindingDialog.title.EditDataItem=Modifier la liaison d'élément de données

DataItemBindingDialog.text.Function=Fonction :

DataItemBindingDialog.text.Filter=Filtre :

DataItemBindingDialog.text.ArgumentList=Argument :

DataItemBindingDialog.Button.Add=Ajouter

DataItemBindingDialog.Button.Edit=Modifier

DataItemBindingDialog.Button.Delete=Supprimer

###########################################################
#DataColumBindingDialog properties
DataColumBindingDialog.title.CreateNewDataBinding=Nouvelle liaison de données

DataColumBindingDialog.title.EditDataBinding=Modifier la liaison de données

DataColumBindingDialog.title.AggBuilder=Générateur d'agrégation

DataColumnBindingDialog.NewBindingDialogInputNewNameMessage=Nouveau nom de liaison de données :

DataColumnBindingDialog.NewBindingDialogInputNewNameTitle=Saisir un nouveau nom de liaison de données

DataColumnBindingDialog.NewBindingDialogMessage=Cette liaison de données est utilisée par plusieurs éléments d'état et cellules, souhaitez-vous créer une nouvelle expression pour cet élément d'état ? Cliquez sur Oui pour créer une nouvelle liaison de données ou cliquez sur Non pour modifier la liaison de données de façon globale.

DataColumnBindingDialog.NewBindingDialogButtonYes=Oui

DataColumnBindingDialog.NewBindingDialogButtonNo=Non

DataColumnBindingDialog.NewBindingDialogButtonCancel=Annuler

DataColumnBindingDialog.NewBindingDialogTitle=Créer une nouvelle liaison de données ?

DataColumnBindingDialog.title.TimePeriodBuild=Générateur d'agrégation de l'intervalle de temps relatif

DataColumBindingDialog.stackMsg.resetReference=Réinitialiser la référence

###########################################################
#CreatePlaceHolderAction properties
ChangeDataColumnAction.text=&Modifier la colonne de données...

###########################################################
#ThumbnailBuilder properties
ThumbnailBuilder.Text.Title=Gérer la miniature

ThumbnailBuilder.Button.Text.Generate=&Générer

ThumbnailBuilder.Button.Text.Remove=&Supprimer

ThumbnailBuilder.Button.Text.Import=&Importer...

ThumbnailBuilder.Button.Text.Browse=& Parcourir...

ThumbnailBuilder.FileDialog.Title=Sélectionner un fichier image

ThumbnailBuilder.FileDialog.FileNameError.Title=Erreur

ThumbnailBuilder.FileDialog.FileNameError.Message=Le fichier sélectionné n'est pas une image.

ThumbnailBuilder.Button.GenerateFromReport=Gé&nérer à partir de l'état

ThumbnailBuilder.Button.BrowseFromFileSystem=&Parcourir depuis le système de fichiers

ThumbnailBuilder.Button.ImportFromResource=I&mporter à partir de ressources partagées

ThumbnailBuilder.Image.DefaultName=Image miniature

ThumbnailBuilder.PreviewImage.WarningDialog.Title=Avertissement

ThumbnailBuilder.PreviewImage.WarningDialog.Prompt=Vous avez sélectionné une image volumineuse (supérieure à 1 014 x 768). Ceci peut entraîner la génération d'une exception de type "Mémoire insuffisante".\nSouhaitez-vous continuer ?


###########################################################
#FragmentResourceEntry properties
FragmentResourceEntry.RootName=Fragments

FragmentResourceEntry.RootDisplayName=Ressources système


FragmentTemplateResourceEntry.RootName=TemplateFragments
FragmentTemplateResourceEntry.RootDisplayName=Ressources de modèle

###########################################################
#PathResourceEntry properties
PathResourceEntry.RootName=Dossier de ressources

PathResourceEntry.RootDisplayName=Ressources partagées

###########################################################
#ResourceFilter properties
ResourceFilter.DisplayName.CVS=Ressources CVS

ResourceFilter.DisplayName.Dot=Ressources .*

ResourceFilter.DisplayName.Data=*.ressources de données

ResourceFilter.DisplayName.EmptyFolder=Dossiers vides

ResourceFilter.Description.CVS=Masque les ressources dont le nom commence par \"CVS\"

ResourceFilter.Description.Dot=Masque les ressources dont le nom commence par un '.'

ResourceFilter.Description.Data=Masquer tous les fichiers de données

ResourceFilter.Description.EmptyFolder=Masque les dossiers vides

###########################################################
#BIRTGotoMarker properties
BIRTGotoMarker.Error.Title=Erreur

BIRTGotoMarker.Error.Message=L'élément d'état n'existe pas.

###########################################################
#TabbedList properties
TabbedPropertyList.properties.not.available=Les propriétés ne sont pas disponibles.

###########################################################
#Sample Reports View properties
SampleReportsView.defautDescription=Sélectionnez un exemple d'état dans les catégories

SampleReportsView.Text.Explorer=Exemples d'états

SampleReportsView.Text.Preview=Aperçu

SampleReportsView.Text.Description=Description

SampleReportsView.MessageDialog.Title=Question

SampleReportsView.MessageDialog.Message=''{0}'' existe déjà. Voulez-vous le remplacer ?

SampleReportsView.Action.exportSampleReport=&Exporter

SampleReportsView.Action.openSampleReport=&Importer

SampleReportsView.Action.exportSampleReport.toolTipText=Exporter l'exemple d'état sélectionné

SampleReportsView.Action.openSampleReport.toolTipText.ide=Créer un projet d'état et placer l'état dans le dossier de projet

SampleReportsView.Action.openSampleReport.toolTipText.rcp=Exporter directement l'exemple d'état sélectionné dans le dossier de l'espace de travail RCP

SampleReportsView.Text.Contribute_Samples=Exemples de contribution

UseCssStyleAction.text=&Utiliser un fichier CSS...

EditUseCssStyleAction.text=Modifi&er un fichier CSS...

EditUseCssStyleAction.EditCssTitle=Modifier CSS

EditUseCssStyleAction.EditCssAreaTitle.Report=Modifier le fichier CSS dans le modèle d'état

EditUseCssStyleAction.EditCssAreaTitle.Libary=Modifier le fichier CSS dans la bibliothèque d'états

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=Général

CrosstabPageGenerator.List.Margin=Marge

CrosstabPageGenerator.List.Font=Police

CrosstabPageGenerator.List.Borders=Bordure

CrosstabPageGenerator.List.Section=Saut de page

CrosstabPageGenerator.List.Visibility=Visibilité

CrosstabPageGenerator.List.TOC=Table des matières

CrosstabPageGenerator.List.Bookmark=Signet

CrosstabPageGenerator.List.NamedExpressions=Expressions nommées

CrosstabPageGenerator.List.UserProperties=Propriétés de l'utilisateur

CrosstabPageGenerator.List.AdvancedProperty=Avancé

CrosstabPageGenerator.List.SubTotals=Sous-totaux

CrosstabPageGenerator.List.GrandTotals=Totaux généraux

CrosstabPageGenerator.List.PageBreak=Saut de page

###########################################################
#CubePageGenerator properties
CubePageGenerator.List.General=Général

CubePageGenerator.List.Comments=Commentaires

CubePageGenerator.List.AdvancedProperty=Avancé


###########################################################
#LibraryLayoutEditorFormPage
LibraryLayoutEditorFormPage.warning.title=Bibliothèque

LibraryLayoutEditorFormPage.warning.message=Pour ajouter un élément d'état à la bibliothèque, faites glisser l'élément depuis la palette ou utilisez le menu d'insertion.\n\nPour afficher les éléments d'état dans l'éditeur, cliquez sur ces éléments en mode Plan.

LibraryLayoutEditorFormPage.warning.prompt=&Ne plus afficher ce message

###########################################################
#PageGenerator Item properties
PageGenerator.TabItem.Attributes=Propriétés

PageGenerator.TabItem.Binding=Liaison

PageGenerator.TabItem.Filters=Filtres

PageGenerator.TabItem.Groups=Groupes

PageGenerator.TabItem.Highlights=Surbrillances

PageGenerator.TabItem.map=Mappage

PageGenerator.TabItem.Sorting=Tri en cours

###########################################################
#AggDataDropAdapter
AggDataDropAdapter.Trans.Name=Créer une agrégation

###########################################################
#BindingDialogHelper
BindingDialogHelper.text.Name=Nom de la &liaison de colonne :

BindingDialogHelper.text.DataType=&Type de données :

BindingDialogHelper.text.Function=&Fonction :

BindingDialogHelper.text.DataField=C&hamp de données :

BindingDialogHelper.text.Filter=&Condition de filtre :

BindingDialogHelper.text.AggOn=Re&grouper par :

BindingDialogHelper.text.Table=Tab&le

BindingDialogHelper.text.List=&Liste

BindingDialogHelper.text.Grid=Grille

BindingDialogHelper.text.Group=Gr&oupe

BindingDialogHelper.text.Expression=&Expression :

BindingDialogHelper.text.displayName=&Nom d'affichage :

BindingDialogHelper.text.allowExport=Exp&orter :

BindingDialogHelper.text.allowExport.button=Autoriser l'exportation

BindingDialogHelper.text.displayNameID=ID du nom d'affi&chage :

BindingDialogHelper.bindingName.dataitem=Liaison de colonne

BindingDialogHelper.bindingName.aggregation=Agrégation

BindingDialogHelper.error.nameduplicate=Une liaison de colonne appelée {0} existe déjà.

BindingDialogHelper.error.improperexpression=L'agrégation sur la mesure de cube ne peut pas être du type : "{0}".

BindingDialogHelper.error.empty={0} est requis.

BindingDialogHelper.error.text.Name=Nom de la liaison de colonne

BindingDialogHelper.text.Colon=:
BindingDialogHelper.warning.function=Le type de données requis pour la fonction actuelle est "{0}". Voulez-vous vraiment conserver le type de données sélectionné ?

BindingDialogHelper.warning.parameter=Le type de données de retour de l'expression "{0}" n'est pas pris en charge pour le paramètre "{1}". Voulez-vous vraiment le conserver ?

BindingDialogHelper.warning.button.yes=Oui

BindingDialogHelper.warning.button.no=Non

BindingGroupDescriptorProvider.Flag.DataModel=\ (Modèle de données)

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel=Liaison de modèle de données

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet=Liaison de jeu de données

###########################################################
#Scripts Node
Scripts.Node.DisplayName=Scripts

ScriptElementNode.Action.Text=Attein&dre l'élément d'état

ScriptObjectNode_0=&Réinitialiser le script

ReportPage.PreviewImage.Button.ToolTip=Gérer la miniature

###########################################################
#CrosstabBindingDialogHelper
CrosstabBindingDialogHelper.AggOn.All=Tous

CrosstabBindingDialogHelper.bindngName.timeperiod=Période

CrosstabBindingDialogHelper.calculation.label=Intervalle de tem&ps :

CrosstabBindingDialogHelper.calculation.group=Intervalle de temps

CrosstabBindingDialogHelper.recentday.description=Dernière date de la période

CrosstabBindingDialogHelper.referencedate.label=Date de &référence :

CrosstabBindingDialogHelper.thisdate.label=Cette date :

CrosstabBindingDialogHelper.thisdate.example.label=Veuillez insérer la valeur de date fournie entre guillemets.\nPar exemple : "2012-01-23", "1/23/2012".

CrosstabBindingDialogHelper.thisdate.example.label.constant=Par exemple : 2012-01-23, 1/23/2012.

CrosstabBindingDialogHelper.timedimension.label=Dimension de te&mps :

CrosstabBindingDialogHelper.timeperiod.wrongdate=Cette dimension ne prend pas en charge la date de référence récente

CrosstabBindingDialogHelper.today.label=Aujourd'hui

AddEditCascadingParameterDialog.label.name=&Nom :

AddEditCascadingParameterDialog.label.dataset=&Jeu de données :

AddEditCascadingParameterDialog.label.value=&Valeur :

AddEditCascadingParameterDialog.label.displaytext=&Texte d'affichage :

###########################################################
#FormatDateTimePattern
FormatDateTimePattern.datetiem_format_type_year=Année

FormatDateTimePattern.datetiem_format_type_short_year=Année courte

FormatDateTimePattern.datetiem_format_type_long_month_year=Mois de l'année long

FormatDateTimePattern.datetiem_format_type_shot_month_year=Mois de l'année court

FormatDateTimePattern.datetiem_format_type_month=Mois

FormatDateTimePattern.datetiem_format_type_long_day_of_week=Jour de la semaine long

FormatDateTimePattern.datetiem_format_type_day_of_month=Jour du mois

FormatDateTimePattern.datetiem_format_type_medium_day_of_year=Jour de l'année moyen

FormatDateTimePattern.datetiem_format_type_minutes=Minutes

FormatDateTimePattern.datetiem_format_type_secontds=Secondes

FormatDateTimePattern.datetiem_format_type_general_time=Heure standard

###############################################################################
# LOCALE SPECIAL:
# following text should be localized specifically for each locale, 
# the pattern may be different for each locale. 
# For example, in Polish locale, "general time" pattern should be "HH:mm:ss.SSS" 
# instead of "ahh:mm:ss.SSS".
#
FormatDateTimePattern.pattern.datetiem_format_type_year=yyyy

FormatDateTimePattern.pattern.datetiem_format_type_short_year=yy

FormatDateTimePattern.pattern.datetiem_format_type_long_month_year=MMMM yyyy

FormatDateTimePattern.pattern.datetiem_format_type_shot_month_year=MMM yy

FormatDateTimePattern.pattern.datetiem_format_type_month=MMMM

FormatDateTimePattern.pattern.datetiem_format_type_long_day_of_week=EEEE

FormatDateTimePattern.pattern.datetiem_format_type_day_of_month=dd

FormatDateTimePattern.pattern.datetiem_format_type_medium_day_of_year=MMMM dd, yy

FormatDateTimePattern.pattern.datetiem_format_type_minutes=mm

FormatDateTimePattern.pattern.datetiem_format_type_secontds=ss

FormatDateTimePattern.pattern.datetiem_format_type_general_time=ahh:mm:ss.SSS

FormatDescriptorProvider.DisplayName.None=Auto

ReportExamples.ContributeSamples.description=Veuillez soumettre une entrée bugzilla {0}Soumettre un bug à BIRT{1}.\nJoignez le fichier du modèle d''état et une capture d''écran de la sortie d''état.\n\n Remarque : La capture d''écran doit être :\n  1. Au format *.png ;\n  2. A une résolution de 800*600 ;\n  3. A une taille inférieure ou égale à 250 Ko.

ReportExamples.ContributeSamples.title=Exemples de contribution

ReportExamples.ContributeSamples.messages=Les informations suivantes vont vous guider vers des exemples de contribution.

ReportExamples.OpenBrowser.Error.title=Erreur

ReportExamples.OpenBrowser.Error.message=Impossible d'ouvrir le navigateur Web.

###########################################################
#CopyCellContentsContextAction
CopyCellContentsContextAction.actionText=Copier le cont&enu

###########################################################
#CopyCellContentsAction
CopyCellContentsAction.actionText=Copier le cont&enu

###########################################################
#CopyFormatAction
CopyFormatAction.text=Copier le &format

###########################################################
#PasteFormatAction
PasteFormatAction.text=Coller le &format

###########################################################
#InsertAggregationAction
InsertAggregationAction.text=&Agrégation


###########################################################
#InsertRelativeTimePeriodAction
InsertRelativeTimePeriodAction.text=Période de temps relati&f

###########################################################
#Multiple views
MultipleAdapter.SetCurrentView=Définir la vue actuelle

MultipleAdapter.RemoveView=Supprimer la vue

MultipleGuideHandle.RemoveView=Supprimer

CreateChartAction.text=Créer un grap&hique

CreateChartHandler.text=Créer un graphique

###########################################################
#Input Parameters
InputParameterDialog.msg.title=Paramètres d'entrée

InputParameterDialog.msg.requiredParam=Les paramètres précédés d'un astérisque (*) sont obligatoires.

InputParameterDialog.err.requiredParam=La valeur du paramètre requis [{0}] n'est pas spécifiée.

InputParameterDialog.err.invalidValueTitle=Valeur non valide

InputParameterDialog.err.invalidValue=La valeur "{0}" est incorrecte avec le type "{1}".

InputParameterDialog.err.emptyValue=La valeur d'entrée pour {0} ne peut pas être vide.

InputParameterDialog.err.emptyValue2=La seconde valeur d'entrée pour {0} ne peut pas être vide.

###########################################################
#Crosstab drag tracker
ColumnDragTracker.ResizeColumn=redimensionner la colonne

ColumnDragTracker.Show.Label=Largeur :

CPListLabelProvider.misssing=(manquant)

RowDragTracker.ResizeRow=redimensionner la ligne

RowDragTracker.Show.Label=Hauteur :

TableDragGuideTracker.Pixels=pixels

###########################################################
#General Designer settings
GeneralConfigurationBlock.message.general.description=Paramètres généraux pour le concepteur d'états.

GeneralConfigurationBlock.button.text.enable.gradient=Activer la sélection du &dégradé

GeneralConfigurationBlock.button.text.enable.animation=Activer la sélection des éléments a&nimés

GeneralConfigurationBlock.group.label=Invite

GeneralConfigurationBlock.group.defaultThemes=Bibliothèque de thèmes par défaut

GeneralConfigurationBlock.button.text.prompt=Afficher l'&invite après ouverture du fichier de bibliothèque.

GeneralConfigurationBlock.button.text.defaultTheme.enable=Activer la bibliothèque de &thèmes par défaut

GeneralConfigurationBlock.button.text.defaultTheme.include=&Inclure les thèmes par défaut

GeneralConfigurationBlock.move.binding.group=Déplacer les liaisons locales vers le parent pour améliorer les performances

GeneralConfigurationBlock.move.binding.always=&Toujours

GeneralConfigurationBlock.move.binding.never=Ja&mais

GeneralConfigurationBlock.move.binding.prompt=Invite&r

###########################################################
#ResetImageSizeAction
ResetImageOriginalSizeAction.trans.label=Réinitialiser la taille de l'image

ResetImageSizeAction.label=Réinitialiser à la &taille d'origine
###########################################################
#Report document editor
ReportDocumentEditor.1=Editeur d'état

ReportDocumentEditor.2=Informations sur le document

ReportDocumentEditor.3=Nom :

ReportDocumentEditor.4=Version :

ReportDocumentEditor.5=Nombre de pages :

ReportDocumentEditor.errorMessage=Une erreur s'est produite lors de l'ouverture du document dans l'éditeur.

###########################################################
#Export Element Dialog
ExportElementDialog.Dialog.Title=Exporter les éléments vers la bibliothèque

ExportElementDialog.Dialog.Message=Sélectionnez la bibliothèque cible ou créez une nouvelle cible.

ExportElementDialog.label.NewFile=&Nom du nouveau fichier :

ExportElementDialog.ErrorMessage=Le nom de fichier doit se terminer par .rptlibrary.

ExportElementDialog.ErrorMessageInvalid=Le nom de fichier est incorrect.

ExportElementDialog.ErrorMessageDuplicate=Cet élément existe déjà dans la bibliothèque.

ExportElementDialog.ErrorStatusCanNotExport=Cet élément ne peut pas être exporté.

ExportElementDialog.WarningMessageDuplicate.Message=Nom de composant en double. Ecraser le composant de bibliothèque existant ?\n\nCliquez sur {0} pour le remplacer.\nCliquez sur {1} pour retourner à la boîte de dialogue de l'élément d'exportation.\nCliquez sur {2} pour annuler l'action.

ExportElementDialog.WarningMessageDuplicate.Title=Confirmation du remplacement

ExportElementDialog.WarningMessageDuplicate.OverrideImage=L'image incorporée référencée par l'image exportée existe déjà dans la bibliothèque. Voulez-vous l'écraser ?

###########################################################
#NewStyleHandler
NewStyleHandler.transaction.label=Nouveau style...

AdvancePropertyDescriptorProvider.DisplayName=Avancé

AdvancePropertyDescriptorProvider.Tooltip.Alphabetic=Trier par ordre alphabétique

AdvancePropertyDescriptorProvider.Tooltip.Group=Trier par groupe

AdvancePropertyDescriptorProvider.Tooltip.Local=Afficher les propriétés locales

PropertyEditorFactory.Boolean.True=vrai

PropertyEditorFactory.Boolean.False=faux

PropertyEditorFactory.Value.Auto=Auto

ExportElementToLibraryAction.waring=Avertissement

ExportElementToLibraryAction.waringMsg=L'élément ne porte pas de nom, mais il est impossible d'en définir un.

###########################################################
#ExportStyleDialog
ExportStyleDialog.Label.Text=&Thème :

ExportStyleDialog.Message=Sélectionnez un thème à exporter dans la bibliothèque.

ExportStyleDialog.Title=Sélectionner le thème

###########################################################
#UIUtil
UIUtil.previewconfirm.title=Erreurs dans l'état

UIUtil.previewconfirm.message=Cet état contient des erreurs, voulez-vous poursuivre la prévisualisation ?

###########################################################
#FileReportProvider
FileReportProvider.CopyConfigFile.DeleteFailure=Echec de la suppression de fichier : {0}

###########################################################
#ShowPropertiesViewMenu
ShowPropertiesViewMenu.ActionName=Propr&iétés

ShowPropertiesViewMenu.ErrorMessage=Echec d'ouverture de la vue des propriétés

ShowPropertiesViewMenu.ErrorTitle=Erreur

###########################################################
#ResetAttributePage
ResetAttributePage.Style.Restore.TooltipText=Restaurer les paramètres par défaut des propriétés de style de la page en cours

ResetAttributePage.Style.Restore.Transaction.Name=Restaurer le style
ResetAttributePage.Style.Restore.Menu.Name=Restaurer les styles par défaut
ResetAttributePage.Style.Restore.All.Transaction.Name=Restaurer tous les styles
ResetAttributePage.Style.Restore.All.Menu.Name=Restaurer les styles par défaut sur tous les onglets

###########################################################
#TextEditor
TextEditor.PasteFormattedText=Coller texte formaté

###########################################################
#ReloadImageAction
ReloadImageAction.Text=Recharger i&mage

###########################################################
#VariablesNodeProvider
VariablesNodeProvider.NewActionName=&Nouvelle variable

VariablesNodeProvider.Dialog.Title=Nouvelle variable

VariablesNodeProvider.NodeName=Variables

###########################################################
#VariableNodeProvider
VariableNodeProvider.DialogTitle=Modifier variable

###########################################################
#SelectVariableDialog
SelectVariableDialog.AvailableVariables=Variables disponibles :

SelectVariableDialog.PageSeperator=----- Variables de la page -----

SelectVariableDialog.ReportSeperator=----- Variables de l'état -----

SelectVariableDialog.Title=Insérer variable

###########################################################
#VariableDialog
VariableDialog.DataType=&Type de données :

VariableDialog.DefaultValue=&Valeur par défaut :

VariableDialog.Name=&Nom :

VariableDialog.PageVariable=Variable de la &page

VariableDialog.ReportVariable=Va&riable de l'état

VariableDialog.VariableType=Type :

VariableDialog.Error.NameDuplicate=Le nom \"{0}"\ est le doublon d'un nom existant. Choisissez un autre nom.

###########################################################
#ExpressionBuilderConfigurationBlock
ExpressionBuilderConfigurationBlock.DefaultSyntax=Syntaxe par déf&aut :

###########################################################
#ExpressionEditor
ExpressionEditor.Error.EmptyExpression=L'expression doit être renseignée.

ExpressionEditor.Label.Expression=&Expression :

ExpressionEditor.Title=Modifier l'expression

ExpressionSyntaxColoringPage.Accessible.Name.Background=Outil de sélection de la couleur de l'arrière-plan

ExpressionSyntaxColoringPage.Accessible.Name.Foreground=Outil de sélection de la couleur du premier plan

ExpressionSyntaxColoringPage.Button.Bold=&Gras

ExpressionSyntaxColoringPage.Button.Enable=Activer

ExpressionSyntaxColoringPage.Button.Italic=&Italique

ExpressionSyntaxColoringPage.Button.Restore=&Restaurer les paramètres par défaut

ExpressionSyntaxColoringPage.Button.Strikethrough=&Barré

ExpressionSyntaxColoringPage.Button.Underline=&Texte souligné

ExpressionSyntaxColoringPage.Decscription=Personnalisez le repérage de la syntaxe en sélectionnant le type de texte correspondant au style que vous souhaitez modifier depuis la liste ou la zone d'exemple de texte, par exemple.

ExpressionSyntaxColoringPage.Element.Comment=Commentaires

ExpressionSyntaxColoringPage.Element.Default=Par défaut

ExpressionSyntaxColoringPage.Element.Keyword=Mot-clé

ExpressionSyntaxColoringPage.Element.String=Chaîne

ExpressionSyntaxColoringPage.Label.Background=Arr&ière-plan :

ExpressionSyntaxColoringPage.Label.Foreground=&Premier plan :

ExpressionSyntaxColoringPage.Label.SampleText=&Exemple de texte :

ExpressionSyntaxColoringPage.Label.SyntaxElement=Eléments de s&yntaxe :

ExpressionSyntaxColoringPage.Link.ColorAndFont=Les couleurs et la police par défaut peuvent être configurés dans la page <a href="org.eclipse.ui.preferencePages.GeneralTextEditor">Editeurs de texte</a> et dans la page de préférences <a href="org.eclipse.ui.preferencePages.ColorsAndFonts">Couleurs et polices</a> .

###########################################################
#VariableDropAdapter
VariableDropAdapter.TranasctionName=Créer l'élément Date à partir de la variable

###########################################################
#RenameInputDialog
RenameInputDialog.Message.BlankName=Le nom doit être spécifié.

RenameInputDialog.Message.DuplicateName=Un doublon existe déjà.

###########################################################
#Breadcrumb
BreadcrumbItemDropDown.showDropDownMenu.action.toolTip=Afficher les enfants

ToggleBreadcrumbAction.tooltip.switch.breadcrumb=Basculer la suite de liens

###########################################################
#HyperlinkParameterProvider Category
ReportHyperlinkParameterProvider.Category.Report=Paramètres de l'état

RenderHyperlinkParameterProvider.Category.Render=Options de rendu
ExceptionDialog.title=Erreur
