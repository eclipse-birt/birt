###############################################################################
# Copyright (c) 2004 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation  - initial API and implementation
###############################################################################

birt.viewer.title=BIRT Report Viewer
birt.viewer.title.navigation=Navigation
birt.viewer.title.error=Erreur
birt.viewer.title.complete=Complete

birt.viewer.parameter=Paramètre
birt.viewer.runreport=Exécuter l'état
birt.viewer.required=Les paramètres portant un <FONT COLOR="red">*</FONT> sont requis.

birt.viewer.viewinpdf=Afficher au format PDF
birt.viewer.maximize=Masquer les paramètres de l'état
birt.viewer.restore=Afficher les paramètres de l'état

birt.viewer.error=Message d'erreur
birt.viewer.error.noparameter=Il n'existe aucun paramètre pour cet état.
birt.viewer.error.noprinter=Cannot find any avaiable printer that supports postscript format.

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=Imprimer l'état au format PDF
birt.viewer.toolbar.printserver=Print report on the server
birt.viewer.toolbar.toc=Afficher la table des matières
birt.viewer.toolbar.parameter=Exécuter l'état
birt.viewer.toolbar.export=Exporter les données
birt.viewer.toolbar.font=Modifier la police
birt.viewer.toolbar.enableiv=Invoquer le visualiseur interactif
birt.viewer.toolbar.exportreport=Export report

###############################################################################
# navbar
###############################################################################
birt.viewer.navbar.first=Première page
birt.viewer.navbar.previous=Page précédente
birt.viewer.navbar.next=Page suivante
birt.viewer.navbar.last=Dernière page
birt.viewer.navbar.goto=Atteindre
birt.viewer.navbar.lable.goto=Atteindre la page :
birt.viewer.navbar.prompt.one=Affichage de la page
birt.viewer.navbar.prompt.two=sur
birt.viewer.navbar.error.blankpagenum=Entrez un numéro de page valide.

###############################################################################
# progress bar
###############################################################################
birt.viewer.progressbar.prompt=Traitement en cours, patientez...

###############################################################################
# Dialog 
###############################################################################
birt.viewer.dialog.ok=OK
birt.viewer.dialog.cancel=Annuler
birt.viewer.dialog.run=Exécuter l'état
birt.viewer.dialog.close=Fermer

birt.viewer.dialog.page.all=All pages
birt.viewer.dialog.page.current=Current page
birt.viewer.dialog.page.range=Pages:
birt.viewer.dialog.page.range.description=( Enter page number(s) and/or page range(s). For example: 1-6 or 1,3,6 )

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=Exception
birt.viewer.dialog.exportReport.title=Exporter l'état
birt.viewer.dialog.parameter.title=Paramètre
birt.viewer.dialog.simpleExportData.title=Exporter les données
birt.viewer.dialog.printReport.title=Print report on the server

###############################################################################
# Export report dialog 
###############################################################################
birt.viewer.dialog.export.format=Format d'exportation :
birt.viewer.dialog.export.format.pdf=PDF
birt.viewer.dialog.export.format.excel=EXCEL
birt.viewer.dialog.export.all=Exporter l'état détaillé
birt.viewer.dialog.export.all.detail=Toutes les données d'état disponibles seront exportées quelles que soient<br>les modifications dans la vue en cours.
birt.viewer.dialog.export.modified=Exporter l'état avec les modifications en cours
birt.viewer.dialog.export.modified.detail=Seules les données visibles seront exportées. Les données<BR>filtrées ou masquées ne seront pas exportées.

birt.viewer.dialog.export.pdf.fittoactual=Actual size
birt.viewer.dialog.export.pdf.fittowidth=Fit to page width
birt.viewer.dialog.export.pdf.fittowhole=Fit to whole page

###############################################################################
# Print report dialog 
###############################################################################
birt.viewer.dialog.print.onserver=Print the output document file on the server
birt.viewer.dialog.print.printer=Printer:
birt.viewer.dialog.print.status=Status:
birt.viewer.dialog.print.model=Model:
birt.viewer.dialog.print.description=Description:
accepting-jobs=Accepting Jobs

birt.viewer.dialog.print.settings=Please specify print settings
birt.viewer.dialog.print.settings.copies=Number of copies:
birt.viewer.dialog.print.settings.collate=Collate:
birt.viewer.dialog.print.settings.mode=Mode:
birt.viewer.dialog.print.settings.mode.bw=B&W
birt.viewer.dialog.print.settings.mode.color=Color
birt.viewer.dialog.print.settings.duplex=Duplex:
birt.viewer.dialog.print.settings.duplex.simplex=Simplex
birt.viewer.dialog.print.settings.duplex.horizontal=Horizontal
birt.viewer.dialog.print.settings.duplex.vertical=Vertical
birt.viewer.dialog.print.settings.pagesize=Page size:

birt.viewer.dialog.print.complete=Sent document file to the selected printer on the server.

###############################################################################
# Confirmation dialog 
###############################################################################
birt.viewer.dialog.confirmation.title=Confirmation
###############################################################################
# Export data dialog 
###############################################################################
birt.viewer.dialog.exportdata.tab.field=Champs de données
birt.viewer.dialog.exportdata.tab.filter=Critères de filtrage
birt.viewer.dialog.exportdata.resultsets=Jeux de résultats disponibles
birt.viewer.dialog.exportdata.availablecolumn=Colonnes disponibles
birt.viewer.dialog.exportdata.selectedcolumn=Colonnes sélectionnées
birt.viewer.dialog.exportdata.format=Les données seront exportées au format csv.
birt.viewer.dialog.exportdata.encoding=Codage de sortie :
birt.viewer.dialog.exportdata.encoding.other=Autre
birt.viewer.dialog.exportdata.encoding.comment=(Si vide, utilisez le codage local)
birt.viewer.dialog.exportdata.addall=Tout ajouter
birt.viewer.dialog.exportdata.add=Ajouter
birt.viewer.dialog.exportdata.removeall=Supprimer tout
birt.viewer.dialog.exportdata.remove=Supprimer
birt.viewer.dialog.exportdata.separator=Separator:

birt.viewer.sep.comma=Comma ( , )
birt.viewer.sep.semicolon=Semicolon ( ; )
birt.viewer.sep.colon=Colon ( : )
birt.viewer.sep.verticalline=Vertical Line ( | )
birt.viewer.sep.tab=Tab ( \\t )

###############################################################################
# General exception
###############################################################################
birt.viewer.generalException.DOCUMENT_FILE_ERROR=Le fichier de document {0} n''existe pas ou contient des erreurs.
birt.viewer.generalException.DOCUMENT_ACCESS_ERROR=Impossible d''accéder au fichier de document {0}.
birt.viewer.generalException.REPORT_FILE_ERROR=Le fichier d''état {0} n''existe pas ou contient des erreurs.
birt.viewer.generalException.REPORT_ACCESS_ERROR=Impossible d''accéder au fichier d''état {0}.
birt.viewer.generalException.DOCUMENT_FILE_PROCESSING=Traitement du fichier de document en cours, veuillez réessayer ultérieurement.

###############################################################################
# Report service exception
###############################################################################
birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT=Aucun fichier de document disponible pour l'extraction des données.
birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET=Aucun ensemble de résultats disponible pour l'extraction des données.
birt.viewer.reportServiceException.INVALID_TOC=Fichier TOC non valide.
birt.viewer.reportServiceException.INVALID_PARAMETER=Paramètre non valide : {0} (non défini).
birt.viewer.reportServiceException.STARTUP_REPORTENGINE_ERROR=Une erreur fatale s'est produite lors de la tentative de lancement du moteur d'états.

###############################################################################
# Birt action handler exception
###############################################################################
birt.viewer.actionException.NO_REPORT_DOCUMENT=Instance de document de rapport vide.
birt.viewer.actionException.INVALID_BOOKMARK=Le signet {0} n''est pas valide. Vérifiez-le, puis réinitialisez le lien.
birt.viewer.actionException.INVALID_PAGE_NUMBER=Numéro de page non valide. 
birt.viewer.actionException.INVALID_ID_FORMAT=Format d'ID non valide. Id={0}.
birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST=Expiration de la session ou aucun fichier de document disponible.

###############################################################################
# Birt soap bingding impl exception
###############################################################################
birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET=Aucun gestionnaire trouvé pour cette cible. Cible : {0}.

###############################################################################
# Component processor exception
###############################################################################
birt.viewer.componentProcessorException.MISSING_OPERATOR=Erreur : Opérateur manquant !

###############################################################################
# Exception dialog
###############################################################################
birt.viewer.exceptionDialog.stackTrace=Trace de la pile :
birt.viewer.exceptionDialog.showStackTrace=Trace de la pile - Afficher l'exception
birt.viewer.exceptionDialog.hideStackTrace=Trace de la pile - Masquer l'exception

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=L'ID de l'attribut doit être spécifié.
birt.viewer.taglib.INVALID_VIEWER_ID=L'ID de l'attribut contient des caractères non valides.
birt.viewer.taglib.VIEWER_ID_DUPLICATE=L'ID de l'attribut doit être unique.
birt.viewer.taglib.NO_REPORT_SOURCE=Un modèle d'état ou un document doit être spécifié.
birt.viewer.taglib.NO_REPORT_DOCUMENT=L'état est requis par le reportlet.
birt.viewer.taglib.NO_REQUESTER_NAME=Le nom du requêteur doit être spécifié.
