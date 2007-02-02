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
birt.viewer.title.error=Fehler

birt.viewer.parameter=Parameter
birt.viewer.runreport=Bericht ausführen
birt.viewer.required=Mit <FONT COLOR="red">*</FONT> gekennzeichnete Parameter sind erforderlich.

birt.viewer.viewinpdf=Im PDF-Format anzeigen
birt.viewer.maximize=Berichtparameter ausblenden
birt.viewer.restore=Berichtparameter anzeigen

birt.viewer.error=Fehlermeldung
birt.viewer.error.noparameter=Für diesen Bericht sind keine Parameter vorhanden.

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=Bericht drucken
birt.viewer.toolbar.toc=Inhaltsverzeichnis anzeigen
birt.viewer.toolbar.parameter=Bericht ausführen
birt.viewer.toolbar.export=Daten exportieren
birt.viewer.toolbar.font=Schriftart ändern
birt.viewer.toolbar.enableiv=Interaktiven Viewer aufrufen
birt.viewer.toolbar.pdf=Bericht als PDF drucken

###############################################################################
# navbar
###############################################################################
birt.viewer.navbar.first=Erste Seite
birt.viewer.navbar.previous=Vorherige Seite
birt.viewer.navbar.next=Nächste Seite
birt.viewer.navbar.last=Letzte Seite
birt.viewer.navbar.goto=Gehe zu
birt.viewer.navbar.lable.goto=Gehe zu Seite:
birt.viewer.navbar.prompt.one=Angezeigte Seite: Seite
birt.viewer.navbar.prompt.two=von
birt.viewer.navbar.error.blankpagenum=Geben Sie eine gültige Seitenzahl ein.
###############################################################################
# progress bar
###############################################################################
birt.viewer.progressbar.prompt=Vorgang läuft. Bitte warten ...

###############################################################################
# Dialog 
###############################################################################
birt.viewer.dialog.ok=OK
birt.viewer.dialog.cancel=Abbrechen
birt.viewer.dialog.run=Bericht ausführen
birt.viewer.dialog.close=Schließ.en

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=Ausnahme
birt.viewer.dialog.exportReport.title=Bericht exportieren
birt.viewer.dialog.parameter.title=Parameter
birt.viewer.dialog.simpleExportData.title=Daten exportieren

###############################################################################
# Export report dialog 
###############################################################################
birt.viewer.dialog.export.format=Exportformat:
birt.viewer.dialog.export.format.pdf=PDF
birt.viewer.dialog.export.format.excel=EXCEL
birt.viewer.dialog.export.all=Detaillierten Bericht exportieren
birt.viewer.dialog.export.all.detail=Sämtliche verfügbaren Berichtdaten werden unabhängig von der<br>aktuellen Ansicht exportiert.
birt.viewer.dialog.export.modified=Bericht mit aktuellen Änderungen exportieren
birt.viewer.dialog.export.modified.detail=Es werden nur sichtbare Daten exportiert. Herausgefilterte oder<br>ausgeblendete Daten werden nicht exportiert.

###############################################################################
# Export data dialog 
###############################################################################
birt.viewer.dialog.exportdata.tab.field=Datenfelder
birt.viewer.dialog.exportdata.tab.filter=Filterkriterien
birt.viewer.dialog.exportdata.resultsets=Verfügbare Ergebnisse
birt.viewer.dialog.exportdata.availablecolumn=Verfügbare Spalten
birt.viewer.dialog.exportdata.selectedcolumn=Ausgewählte Spalten
birt.viewer.dialog.exportdata.format=Die Daten werden im CSV-Format exportiert.
birt.viewer.dialog.exportdata.encoding=Ausgabecodierung:
birt.viewer.dialog.exportdata.encoding.other=Andere
birt.viewer.dialog.exportdata.encoding.comment=(Wenn leer, lokale Codierung verwenden)
birt.viewer.dialog.exportdata.addall=Alle hinzufügen
birt.viewer.dialog.exportdata.add=Hinzuf
birt.viewer.dialog.exportdata.removeall=Alles entfernen
birt.viewer.dialog.exportdata.remove=Entfernen

###############################################################################
# General exception
###############################################################################
birt.viewer.generalException.DOCUMENT_FILE_ERROR=Die Dokumentdatei : {0} ist nicht vorhanden oder fehlerhaft.
birt.viewer.generalException.DOCUMENT_ACCESS_ERROR=Zugriff auf die Dokumentdatei : {0} nicht möglich.
birt.viewer.generalException.REPORT_FILE_ERROR=Die Berichtdatei : {0} ist nicht vorhanden oder fehlerhaft.
birt.viewer.generalException.REPORT_ACCESS_ERROR=Zugriff auf die Berichtdatei nicht möglich : {0}.
birt.viewer.generalException.DOCUMENT_FILE_PROCESSING=Dokumentdatei wird verarbeitet. Versuchen Sie es später erneut.

###############################################################################
# Report service exception
###############################################################################
birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT=Für die Extrahierung der Daten ist keine Dokumentdatei verfügbar.
birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET=Für die Extrahierung der Daten ist kein Ergebnis verfügbar.
birt.viewer.reportServiceException.INVALID_TOC=Ungültige Inhaltsverzeichnis-Anfrage.
birt.viewer.reportServiceException.INVALID_PARAMETER=Ungültiger Parameter: {0}; er ist nicht definiert.
birt.viewer.reportServiceException.STARTUP_REPORTENGINE_ERROR=Fatal error occurred when try to startup report engine.

###############################################################################
# Birt action handler exception
###############################################################################
birt.viewer.actionException.NO_REPORT_DOCUMENT=Berichtdokument-Instanz ist leer.
birt.viewer.actionException.INVALID_BOOKMARK=Das Lesezeichen {0} ist ungültig. Bitte prüfen und Verknüpfung zurücksetzen.
birt.viewer.actionException.INVALID_PAGE_NUMBER=Ungültige Seitenzahl. 
birt.viewer.actionException.INVALID_ID_FORMAT=Ungültiges ID-Format. Id={0}.
birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST=Sitzung wegen Zeitüberschreitung abgebrochen bzw. keine Dokumentdatei verfügbar.

###############################################################################
# Birt soap bingding impl exception
###############################################################################
birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET=Für dieses Ziel wurde kein Handler gefunden. Ziel: {0}.

###############################################################################
# Component processor exception
###############################################################################
birt.viewer.componentProcessorException.MISSING_OPERATOR=Fehler: Fehlender Operator!

###############################################################################
# Exception dialog
###############################################################################
birt.viewer.exceptionDialog.stackTrace=Ablaufverfolgungsstapel:
birt.viewer.exceptionDialog.showStackTrace=Show Exception Stack Trace
birt.viewer.exceptionDialog.hideStackTrace=Hide Exception Stack Trace

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=Attribut-ID muss angegeben werden.
birt.viewer.taglib.INVALID_VIEWER_ID=Attribut-ID enthält ungültige Zeichen.
birt.viewer.taglib.VIEWER_ID_DUPLICATE=Attribut-ID muss eindeutig sein.
birt.viewer.taglib.NO_REPORT_SOURCE=Berichtdesign oder Dokumentdatei muss angegeben werden.
birt.viewer.taglib.NO_REPORT_DOCUMENT=Reportlet benötigt Berichtsdokument.
birt.viewer.taglib.NO_REQUESTER_NAME=Requester-Name muss angegeben werden..
