# wizard dialog messages
wizard.title.selectFolder=XML-Datenquelle
wizard.defaultMessage.selectFolder=Geben Sie die URLs für die XML-Datei und die Schemainformationen an.
wizard.title.newDataSet=Neuer Datensatz
wizard.title.selectXmlFile=XML-Daten für Vorschau auswählen
wizard.defaultMessage.selectFile=Wählen Sie eine XML-Beispieldatenquelle aus, um die Vorschau in dieser Sitzung zu aktivieren.
wizard.title.xPathChoose=Tabellenzuordnung definieren
wizard.defaultMessage.selectXPath=Definieren Sie das Element, das als Tabelle verwendet werden soll. Wählen Sie dazu das Element in der XML-Struktur aus, und fügen Sie es der Zeilenzuordnung hinzu. Pro XML-Datensatz ist nur eine Tabellenzuordnung zulässig.
wizard.title.selectColumn=Spaltenzuordnung definieren
wizard.title.defineColumnMapping=Definieren Sie die Spaltenzuordnung, indem Sie ein Element oder Attribut in der XML-Struktur auswählen, und es der Spaltenzuordnung hinzufügen. 

#preference
designer.preview.preference.numberOfElementPassedToGetSchema=Anzahl der Elemente, die bei der XML-Schemastruktur verwendet wurden.
designer.preview.preference.numberOfElementPassedToGetSchema.errormessage=Geben Sie eine Zahl zwischen 0 und {0} ein.
designer.preview.preference.columnMappingPage.group.title=Spaltenzuordnungsseite

#table mapping page messages
xPathChoosePage.messages.rowMapping=Tabellenzuordnung
xPathChoosePage.messages.xPathExpression=&XPath-Ausdruck
xPathChoosePage.messages.xmlStructure=XML-Struktur
xPathChoosePage.messages.xmlColumnMapping=Spaltenzuordnung

#sample xml data file page messages
xmlDataSetSelectionPage.messages.lineofdata=Quelldatenzeilen
xmlDataSetSelectionPage.warning.errorReadXMLFile=Fehler beim Lesen der XML-Daten in Datei [{0}]\n{1}. Dennoch fortfahren?

#column mapping page messages
button.refresh=Aktualisieren
menu.menuItem.removeAll=Alles entfernen
menu.menuItem.remove=Entfernen
lable.selectXmlFile=Geben Sie den URL der &XML-Quelldatei an, oder suchen Sie die Datei mit den Daten:
lable.selectXmlSchmaFile=Geben Sie den URL des X&ML-Schemas an, oder suchen\n Sie nach der Datei, die das\n Schema enthält. Lassen Sie das Feld leer, falls kein Schema vorhanden ist:
label.preview=&Erstes Objekt anzeigen
label.useXMLFileFromDataSource=Die in der Datenquelle definierte XML-Datei verwenden
RowMappingDialog.title=Wählen oder bearbeiten Sie den XPath-Ausdruck.
RowMappingDialog.info=Wählen Sie für den gewählten Knoten einen der vordefinierten XPath-Ausdrücke,\n oder geben Sie einen anderen Ausdruck an:
ColumnMappingDialog.info.columnName=Spaltenname:
ColumnMappingDialog.info.xPath=XPath-Ausdruck:
ColumnMappingDialog.info.dataType=Datentyp:
ColumnMappingDialog.info.dataPreview=Vorschau der Tabelle
ColumnMappingDialog.prompt.new=Neue Zuordnung ...
ColumnMappingDialog.prompt.maxRow=Die Quelldaten umfassen mehr als 500 Zeilen. Auf dieser Seite kann nur eine Vorschau der ersten 500 Zeilen angezeigt werden.
ColumnMappingDialog.prompt.dialog.title=Ergebnisvorschau

dataset.editor.columnName=Name
dataset.editor.xpathexpression=XPath
dataset.editor.datatype=Typ
dataset.editor.rowelement=Zeilenelement
dataset.error.populateXMLTree=Die XML-Datenquelle kann nicht aufgerufen werden. Entweder ist die XML-Datenquelle ungültig, oder die Datei ist nicht vorhanden.

error.label=Fehler
error.selectFolder=Der URL für die gewählte Datei ist ungültig oder nicht vorhanden.
error.emptyPath=Der URL der Datei enthält keine Daten
error.xpath.canNotContainAttribute=Der XPath-Ausdruck kann nicht einem XML-Attribut zugeordnet werden.
error.dataset.maxRowNumberError=Der Wert für die maximale Zeilenzahl muss eine Zahl sein.
error.columnMapping.noMappingColumn=Ordnen Sie mindestens eine Spalte zu.
error.file.null=Keiner der URLs enthält Daten. Sie haben foglich keine Datenquelle ausgewählt. Soll diese Auswahl beibehalten werden?
error.columnMapping.sameColumnName=Der Spaltenname [{0}] ist bereits vorhanden. Wählen Sie einen anderen Namen.
error.invalidXpath=Der XPath-Ausdruck "{0}" ist ungültig.

#data types for column mapping
datatypes.dateTime=Timestamp
datatypes.decimal=Große Dezimalzahl
datatypes.float=Double
datatypes.integer=Ganzzahl
datatypes.string=String
datatypes.date=Date
datatypes.time=Time

#column name in column mapping table
columnName.name=Name
columnName.xPathName=XPath
columnName.typeName=Typ

dataset.new=Neuer Datensatz
file.choose=&Durchsuchen ...
title.warning=Warnung

datasource.propertyPage.tooltip=XML-Datenquelle
