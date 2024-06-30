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
MongoDBDataSourceWizardPage.message.default=Mongo-Datenbank-Datenquellen-Verbindungseigenschaften

MongoDBDataSourceWizardPage.GroupTitle.URIElements=Mongo-Datenbank-URI-Elemente

MongoDBDataSourceWizardPage.text.label.ServerHost=&Host des Servers:

MongoDBDataSourceWizardPage.text.label.ServerPort=Servera&nschluss:

MongoDBDataSourceWizardPage.text.label.DatabaseName=&Datenbankname:

MongoDBDataSourceWizardPage.text.label.UserName=B&enutzername:

MongoDBDataSourceWizardPage.text.label.Password=Kenn&wort:

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=&Mongo-Datenbank-URI:

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=Ergänzende &Client-Einstellungen

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=Socket &aufrechterhalten

MongoDBDataSourceWizardPage.checkbox.label.RequestSession='Consistent &Request'-Sitzung

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=Erforderliche Verbindungseigenschaft fehlt!

#
MongoDBDataSetWizardPage.label.DBName=Mon&go-Datenbank:

MongoDBDataSetWizardPage.label.DBCollection=MongoDB &Sammlung

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=Systemsammlung &einschließen

MongoDBDataSetWizardPage.Button.Refresh=&Aktualisieren

MongoDBDataSetWizardPage.label.DocumentNumber=An&zahl der Dokumente für die Suche nach Feldern:

MongoDBDataSetWizardPage.group.text=Befehl &Vorgang

MongoDBDataSetWizardPage.label.OperationType=Befehls&typ:

MongoDBDataSetWizardPage.Button.FindFields=Felder suc&hen

MongoDBDataSetWizardPage.Button.text.CommandExpression=&Ausdruck...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=Befehlsausdruck bearbeiten.

MongoDBDataSetWizardPage.Button.QueryExpression=$&Abfrageausdruck...

MongoDBDataSetWizardPage.Button.SortExpression=&Sortierungsausdruck...

MongoDBDataSetWizardPage.Button.AdvancedSettings=Er&weiterte Einstellungen...

MongoDBDataSetWizardPage.label.AvailableFields=&Verfügbare Felder:

MongoDBDataSetWizardPage.label.SelectedFields=&Ausgewählte Felder:

MongoDBDataSetWizardPage.HeaderText.SelectedFields=Feldanzeigename:

MongoDBDataSetWizardPage.menuItem.remove=En&tfernen

MongoDBDataSetWizardPage.menuItem.removeAll=A&lles entfernen

MongoDBDataSetWizardPage.message.default=Gewünschte Felder für die Datensatzspalten auswählen

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=Zum Fortfahren bestätigen

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=Bei Auswahl einer anderen Sammlung wird die momentane Auswahl der Felder verworfen.  Möchten Sie fortfahren?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=Zum Aktualisieren bestätigen

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=Möchten Sie die verfügbaren Felder auf Basis des geänderten Befehlsausdrucks aktualisieren?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=Fehler

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=Abrufen der Liste der Sammlungen von der MongoDB-Datenquelle nicht möglich.

MongoDBDataSetWizardPage.ExceptionDialog.title=Fehler

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=Verfügbare Felder wurden nicht gefunden.

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=Keine aktive Datenquellenverbindung.

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=Fehler beim Aktualisieren des MongoDB-Datensatzes.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=Ungültiger Befehlsausdruck.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=Ungültiger Abfrageausdruck.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=Ungültiger Sortierungsausdruck.

MongoDBDataSetWizardPage.message.error.MissingCollectionName=Der Sammlungsname darf nicht leer sein.

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=Geben Sie eine Ganzzahl für die Anzahl der Dokumente ein, um Felder zu finden.

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=Das ausgewählte Feld '{0}' ist nicht vorhanden.

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=Der Befehlsausdruck darf nicht leer sein.

MongoDBDataSetWizardPage.message.error.MissingSelectedField=Mindestens ein Feld muss ausgewählt sein.

MongoDBAdvancedSettingsDialog.dialogTitle=Erweiterte Datensatz-Laufzeiteigenschaften

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=Maximale Anzahl Ergebnisobjekte je &Batch:

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=Die maximale Anzahl Ergebnisobjekte, die in einem Batch\n vom MongoDB-Cursor abgerufen werden.  Standardwert = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=Maximale Anzahl Dokumente für die Suche nach &Metadaten:

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=Die Anzahl der zu verarbeitenden Dokumente, die in einer Abfrage\n der Ergebnismetadaten bei Laufzeit erhalten werden.  Standardwert = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=Maximale Anzahl Dokumente zum Ü&berspringen:

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=Die ersten n Dokumente in den Ergebnisdaten überspringen; Ganzzahltyp.\nStandardwert = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=Index&hinweisausdruck:

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=Einer von mehreren Indexschlüsseln, die die Abfrage zur Abfrageoptimierung nutzen muss.\n Verwenden Sie die Syntax des Mongo-JSON-Hinweisausdrucks, um die Indexschlüssel aufzulisten.

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=Lesevoreinstellung des Query-Cursors

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=Mo&dus:

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=Der Modus der Lesevoreinstellung des Query-Cursorvorgangs.

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=Ta&g-Sätze:

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=Sätze erforderlicher Tags, die für einen Mitgliedsknoten konfiguriert wurden.\n Alternative Tag-Sätze können als Dokumente in einem Array angegeben werden.

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=&Keine Cursor-Zeitüberschreitung

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=Der Server bricht normalerweise inaktive Cursor aufgrund einer Zeitüberschreitung (10 Minuten) ab, um übermäßige Speicherverwendung zu verhindern.\n Das kann durch Aktivieren dieser Option verhindert werden.

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=Ermöglicht &Teilergebnisse

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=Bei Verwendung mit Sharding (Mongos) sind Teilergebnisse von einem geteilten System zulässig, wenn Teile\n vom Cluster ausgeschaltet sind oder fehlen.  Ohne Aktivierung wird ein Fehler vom Mongos-Server zurückgegeben.

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=Verschachtelte Sammlungen &verflachen

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=Eine Benutzeroption zum Verflachen einer verschachtelten Sammlung mit mehreren Ebenen in mehrere Datensatzzeilen.

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=Erfordert eine Ganzzahl für 'Maximale Anzahl Ergebnisobjekte'!

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=Erfordert eine Ganzzahl für 'Maximale Anzahl Dokumente für die Suche'!

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=Erfordert eine Ganzzahl für 'Maximale Anzahl Dokumente zum Überspringen'!

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=Entwurfseinstellung zurücksetzen

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=OK

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=Der Entwurfszeit-Wert von 'Anzahl der Dokumente zur Suche nach Feldern' wurde zurückgesetzt, um mit der Laufzeiteigenschaft 'Maximale Dokumente zur Suche nach Metadaten' übereinzustimmen.

MongoDBExpressionBuilder.dialogTitle.default=Ausdruckserstellung

MongoDBExpressionBuilder.ExprssionText.label=A&usdruckstext:

MongoDBExpressionBuilder.Button.import=I&mportieren...

MongoDBExpressionBuilder.Button.tooltip.import=Ausdruckstext aus einer lokalen Datei importieren

MongoDBExpressionBuilder.Button.export=&Exportieren...

MongoDBExpressionBuilder.Button.tooltip.export=Ausdruckstext in lokale Datei exportieren

MongoDBExpressionBuilder.Button.ValidateSyntax=Syntax &validieren

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=Validieren Sie die Syntax des Ausdruckstexts.

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=Validierung des Ausdruckstexts

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=Gültige Ausdruckssyntax.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=Ungültige Ausdruckssyntax.

MongoDBExpressionBuilder.CommandExpression.DialogTitle=Befehl 'Ausdruckserstellung'

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=Ausdruckstext darf nicht leer sein!

MongoDBExpressionBuilder.QueryExpression.DialogTitle=Abfrageausdruckserstellung

MongoDBExpressionBuilder.SortExpression.DialogTitle=Sortierausdruckserstellung





