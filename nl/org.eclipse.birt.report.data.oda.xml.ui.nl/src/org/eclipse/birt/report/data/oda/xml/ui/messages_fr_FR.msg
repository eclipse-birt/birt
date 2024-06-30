# wizard dialog messages
wizard.title.selectFolder=Source de données XML
wizard.defaultMessage.selectFolder=Définissez les URL du fichier XML et les informations du schéma.
wizard.title.newDataSet=Nouveau jeu de données
wizard.title.selectXmlFile=Sélectionner des données XML pour l'aperçu
wizard.defaultMessage.selectFile=Sélectionnez une source XML exemple pour permettre l'aperçu de données lors de cette session.
wizard.title.xPathChoose=Définir la mise en correspondance de table
wizard.defaultMessage.selectXPath=Définissez l'élément à utiliser en tant que table en le sélectionnant dans la structure XML et en l'ajoutant à la mise en correspondance de ligne. Il ne peut y avoir qu'une seule mise en correspondance de table par jeu de données XML.
wizard.title.selectColumn=Définir la mise en correspondance de colonne
wizard.title.defineColumnMapping=Définissez la mise en correspondance de colonne en sélectionnant un élément ou un attribut dans la structure XML et en l'ajoutant à la mise en correspondance de colonne. 

#preference
designer.preview.preference.numberOfElementPassedToGetSchema=Nombre d'éléments accédés dans l'analyse de l'arborescence du schéma XML.
designer.preview.preference.numberOfElementPassedToGetSchema.errormessage=Entrez un nombre compris entre 0 et {0}.
designer.preview.preference.columnMappingPage.group.title=Page de mise en correspondance de colonne

#table mapping page messages
xPathChoosePage.messages.rowMapping=Mise en correspondance de table
xPathChoosePage.messages.xPathExpression=Expression &XPath
xPathChoosePage.messages.xmlStructure=Structure XML
xPathChoosePage.messages.xmlColumnMapping=Mise en correspondance de colonne

#sample xml data file page messages
xmlDataSetSelectionPage.messages.lineofdata=lignes de source de données
xmlDataSetSelectionPage.warning.errorReadXMLFile=Erreur lors de la lecture des données XML à partir du fichier [{0}]\n{1}. Voulez-vous tout de même continuer ?

#column mapping page messages
button.refresh=Actualiser
menu.menuItem.removeAll=Supprimer tout
menu.menuItem.remove=Supprimer
lable.selectXmlFile=Entrez l'URL de la source &XML ou accédez au fichier contenant les données :
lable.selectXmlSchmaFile=Entrez l'URL du schéma X&ML ou accédez au fichier contenant le\n schéma. Ne renseignez pas ce champ si aucun schéma n'est disponible :
label.preview=&Afficher un aperçu d'abord
label.useXMLFileFromDataSource=Utiliser le fichier XML défini dans la source de données
RowMappingDialog.title=Sélectionner ou modifier l'expression XPath.
RowMappingDialog.info=Sélectionnez une des expressions XPath prédéfinies pour le noeud sélectionné ou\n entrez une autre expression :
ColumnMappingDialog.info.columnName=Nom de colonne :
ColumnMappingDialog.info.xPath=Expression XPath :
ColumnMappingDialog.info.dataType=Type de données :
ColumnMappingDialog.info.dataPreview=Aperçu de la table
ColumnMappingDialog.prompt.new=Nouvelle mise en correspondance ici...
ColumnMappingDialog.prompt.maxRow=La source de données dépasse 500 lignes. Cette page ne permet d'afficher que les 500 premières lignes.
ColumnMappingDialog.prompt.dialog.title=Aperçu des résultats

dataset.editor.columnName=Nom
dataset.editor.xpathexpression=XPath
dataset.editor.datatype=Type
dataset.editor.rowelement=Elément de ligne
dataset.error.populateXMLTree=Impossible de récupérer la source de données XML. Le fichier de source de données XML est incorrect ou le fichier n'existe pas.

error.label=Erreur
error.selectFolder=L'URL de fichier sélectionnée n'est pas valide ou le fichier n'existe pas.
error.emptyPath=L'URL du fichier est vide.
error.xpath.canNotContainAttribute=L'expression XPath ne peut mettre en correspondance l'attribut XML.
error.dataset.maxRowNumberError=Le nombre maximal de lignes doit être renseigné par un nombre.
error.columnMapping.noMappingColumn=Veuillez mettre au moins une colonne en correspondance.
error.file.null=Toutes les URL sont vides. Vous n'avez donc pas sélectionné de source de données. Souhaitez-vous continuer ?
error.columnMapping.sameColumnName=Le nom de colonne [{0}] existe déjà. Utilisez un autre nom.
error.invalidXpath=L'expression XPath "{0}" n'est pas valide.

#data types for column mapping
datatypes.dateTime=Horodateur
datatypes.decimal=Grande décimale
datatypes.float=Double
datatypes.integer=Entier
datatypes.string=Chaîne
datatypes.date=Date
datatypes.time=Heure

#column name in column mapping table
columnName.name=Nom
columnName.xPathName=XPath
columnName.typeName=Type

dataset.new=Nouveau jeu de données
file.choose=&Parcourir...
title.warning=Avertissement

datasource.propertyPage.tooltip=Source de données XML
