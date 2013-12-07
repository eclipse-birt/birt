#************************************************************************
# Copyright (c) 2012 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#************************************************************************
#
# User messages for the Excel File ODA Designer

wizard.defaultMessage.selectExcelFile=Select or enter an Excel data source file path or URI.
wizard.defaultMessage.selectDataSet=Sélectionnez le classeur et les colonnes à inclure dans votre jeu de données.
connection_CANNOT_OPEN_EXCEL_FILE_DB_DIR=Impossible d'ouvrir la source de données Excel dans le dossier sélectionné. 
ui.ExcelFileNotFound=Impossible de trouver le fichier ou de le dossier de la source de données Excel demandé 
label.selectFolder=Sél&ectionnez un dossier : 
label.selectFile=Sélectionnez un f&ichier :
label.selectworksheet=Classeur :
label.includeTypeLine=La deuxième ligne contient les types de &données.
label.includeColumnNameLine=La première ligne cont&ient les noms de colonnes.

button.selectFolder.browse=Pa&rcourir...
button.moveUp=Vers le ha&ut
button.moveDown=&Vers le bas
button.delete=&Supprimer
button.selectFileURI.menuItem.relativePath=Chemin d'accès &relatif
button.selectFileURI.menuItem.absolutePath=Chemin d'accès &absolu

editor.title.name=Nom
editor.title.originalName=Nom d'origine
editor.title.type=Type

datatypes.dateTime=Date Heure
datatypes.decimal=Décimal
datatypes.float=Flottant
datatypes.integer=Entier
datatypes.string=Chaîne
datatypes.date=Date
datatypes.time=Heure
datatypes.boolean=Booléen

tooltip.columnnameline=Vérifiez l'existence des noms de colonnes\n dans la première ligne du fichier sélectionné.
tooltip.typeline=Vérifiez l'existence de l'indicateur du type de données\n dans la deuxième ligne du fichier sélectionné.
tooltip.button.add=Ajouter toutes les colonnes sélectionnées de la table de gauche au jeu de données Excel.
tooltip.button.up=Déplacer l'élément sélectionné vers le haut.
tooltip.button.down=Déplacer l'élément sélectionné vers le bas.
tooltip.button.delete=Supprimer l'élément sélectionné.
tooltip.trailNull=Colonnes avec valeurs nulles à la fin lors de la lecture des lignes de données. 
error.selectFolder=Sélectionnez un dossier valide.
error.selectWorksheet=Please select a worksheet for the data set.
error.selectColumns=Please select at least one column for this data set.
error.emptyPath=Entrez un chemin.
error.unexpectedError=Une erreur inattendue s'est produite.
error.invalidConnectionFilePath=Le chemin d''accès source au profil de connexion spécifié, "{0}", n'existe pas.
error.errorExist=Erreur sur la page actuelle.  Souhaitez-vous l'enregistrer quand même ?
error.noFile=The Excel data source file ({0}) does not exist.
error.invalidFilePath=Le chemin du fichier est incorrect.  Voulez-vous continuer ?
error.emptyFilePath=Le chemin du fichier est vide.  Voulez-vous continuer ?
error.duplicatedNameValueOrEmpty=Valeur de nom dupliquée ou vide
error.emptyFolder=Aucun fichier de source de données Excel trouvé dans le dossier sélectionné.

title.warning=Avertissement
warning.fileExtensionInvalid=Le fichier sélectionné n'est peut-être pas un fichier Excel valide.
confirm.reselectWorksheetTitle=Confirmez pour sélectionner une feuille différente.
confirm.reselectWorksheetMessage=La sélection d'une autre feuille ne permettra pas l'enregistrement des opérations précédentes.\n\nVoulez-vous continuer ?
confirm.reselectFileTitle=Confirmez pour sélectionner un autre fichier.
confirm.reselectFileMessage=La sélection d'une autre feuille ne permettra pas l'enregistrement des opérations précédentes.\n\nVoulez-vous continuer ?

RelativeFolderSelectionDialog.Title.SelectFolder=Sélectionner un dossier
RelativeFileSelectionDialog.button.importFile=Fichier d'importation...
RelativeFileSelectionDialog.title.error=Erreur
RelativeFileSelectionDialog.title.overwrite=Ecraser
RelativeFileSelectionDialog.overwrite.msg=Un fichier portant ce nom existe déjà. Voulez-vous l'écraser ?
RelativeFileSelectionDialog.import.msg=Importation du fichier...
