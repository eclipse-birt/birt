# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} in {1} nicht gefunden.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=Datenquelle {0} wurde nicht definiert.
data.engine.UndefinedDataSet=Datensatz {0} wurde nicht definiert.
data.engine.UnsupportedDataSourceType=Datenquelle "{0}" hat einen nicht unterstützten Typ.
data.engine.UnsupportedDataSetType=Datensatz "{0}" hat einen nicht unterstützten Typ.
data.engine.UnsupportedGroupOnClobAndBLOB=Gruppierung nach CLOB- oder BLOB-Daten "{0}" wird nicht unterstützt.
data.engine.UnsupportedSortOnClobAndBLOB=Sortierung nach CLOB- oder BLOB-Daten "{0}" wird nicht unterstützt.

data.engine.EndOfGroupOnly=Dieser Aggregatausdruck ist nur am Ende einer Gruppe verfügbar.
data.engine.InvalidAggrGroup=Ungültige Gruppierungsebene für Aggregatfunktion: {0}
data.engine.WrongNumAggrArgs=Falsche Anzahl von Argumenten für Aggregatfunktion: {0}

data.engine.DupPropertyName=Doppelt vergebener Eigenschaftenname gefunden: {0}
data.engine.NoSubQueryName=Teilabfrage mit Namen "{0}" konnte nicht gefunden werden.

data.engine.DataSourceMissingExtId=Fehlende Erweiterungs-ID in Definition der Datenquelle "{0}".
data.engine.NullGroupStart=Der Gruppenstartwert darf nicht Null sein.
data.engine.UnsupportedDirectNestedAggregate=Direktes Verschachteln von Aggregaten wird nicht unterstützt.

data.engine.ResultClosed=QueryResults oder der zugehörige Iterator wurden geschlossen.
data.engine.NoCurrentRow=Result Iterator hat keine aktuelle Zeile. Der Iterator wurde entweder nicht gestartet, oder er wurde beendet.
data.engine.InvalidJSExpr=Ungültiger Javascript-Ausdruck: {0}
data.engine.InvalidTotalFuncName=Ungültiger Name für Summenfunktion: {0}
data.engine.InvalidTotalFunc=Ungültige Summenfunktion.
data.engine.NoAggrFunc=Aufruf für Aggregatfunktion wird in diesem Ausdruck nicht erwartet.
data.engine.InvalidExprHandle=Ungültiges Ausdruck-Handle.
data.engine.InvalidAggrGroupExpression=Falscher Gruppenausdruck für Aggregatfunktion: {0}
data.engine.InvalidSortKeyColumn=Ungültige Sortierschlüsselspalte: {0}
data.engine.InvalidGroupLevel=Ungültige Gruppenebene: {0}
data.engine.InvalidGroupKeyName=Ungültiger Gruppenschlüssel-Spaltenname: {0}
data.engine.BadCloseScript=CloseScript in CustomDataSet konnte nicht ausgewertet werden.
data.engine.InvalidCustomFieldIdx=Ungültiger Index für benutzerdefiniertes Feld: {0}
data.engine.InvalidGroupName=Der Gruppenname ist ungültig.
data.engine.DuplicateGroupName=Die Gruppennamen sind doppelt vorhanden.

data.engine.BadOpenScript=OpenScript in CustomDataSet konnte nicht ausgewertet werden.
data.engine.DataSourceIsOpen=Datenquelle ist bereits geöffnet.
data.engine.DataSourceNotOpen=Datenquelle ist nicht geöffnet.
data.engine.EmptyCustomFieldName=Name des benutzerdefinierten Feldes darf nicht leer sein.
data.engine.DupCustomFieldName=Doppelt vergebener benutzerdefinierter Feldname: {0}
data.engine.NoResultSet=Ausführung der Abfrage hat kein Ergebnis zurückgegeben.
data.engine.NoResultMetadata=Metadatenergebnis ist nicht verfügbar.
data.engine.QueryAlreadyPrepared=Abfrage wurde bereits vorbereitet.
data.engine.QueryNotPrepared=Abfrage wurde noch nicht vorbereitet.
data.engine.InvalidFieldName=Ungültiger Feldname: {0}
data.engine.DupColumnName=Doppelter Spaltenname: {0}
data.engine.DupAlias=Doppelter Spaltenname oder -alias: {0}
data.engine.InvalidFieldIndex=Ungültiger Feldindex: {0}

data.engine.MatchError=Überprüfen Sie die Syntax des Musters. Fehler beim Abgleichen der Zeichenfolge mit dem Muster: {0} 
data.engine.BadOperandType=Unerwarteter Datentyp des Operanden: {0}

data.engine.BadParameterCount=Falsche Anzahl von Parametern wurde angegeben.
data.engine.BadParameterType=Einer der Parameter hat einen falschen Datentyp.
data.engine.NoIrrSolution=Lösung für die IRR-Funktion (Internal Rate of Return) kann mit den angegebenen Daten nicht gefunden werden.
data.engine.ExpectBooleanReturnType=Skript {0} des geskripteten Datensatzes muss Boolesches Ergebnis zurückgeben.
data.engine.BadIntervalUnit=Ungültiger Wert für interne Werte: {0}
data.engine.InvalidFieldName=Ungültiger Feldname: {0}
data.engine.BadGroupKeyType=Gruppenschlüssel hat nicht unterstützten Datentyp: {0}
data.engine.group.interval=Das Festlegen von "{0}" als Intervall für eine Spalte des Typs {1} wird nicht unterstützt.
data.engine.group.interval.Rowid=In der Spalte "rowID" werden als Intervalltyp nur numerische Intervalle unterstützt.
data.engine.group.interval.invalid=Das angegebene Intervall ist ungültig.
data.engine.NoSupportFilterOnGroup=Das Filtern nach Gruppen wird momentan nicht unterstützt.
data.engine.InvalidTopBottomArgument=Das Argument für Top/Bottom-Filter muss eine Zahl sein.
data.engine.InvalidTopBottomNArgument=Das Argument der Filter "TopN/BottomN" muss entweder Null oder eine positive Ganzzahl sein.
data.engine.InvalidTopBottomPercentArgument=Bei den Argumenten der Filter für "TopPercent/BottomPercent" muss es sich um positive Zahlen bis maximal 100 handeln;
data.engine.InvalidTopBottomN=Anzahl zu verarbeitender Zeilen darf {0} nicht überschreiten.
data.engine.InvalidPercentileArgument=Für die Funktion "Total.Percentile" sind als zweites Argument nur Zahlenwerte zwischen 0 und 1 (einschließlich) zulässig.
data.engine.InvalidQuartileArgument=Für die Funktion "Total.Quartile" sind als zweites Argument nur Zahlenwerte zwischen 0 und 4 (einschließlich) zulässig.
data.engine.BadAggrFuncParam=Unzulässiger Parameter für Aggregatfunktion: {0}
data.engine.ResetRateGuess=Benutzer sollte den neuen Wert für "Rate Guess" zurücksetzen.
data.engine.UnsupportedCondOp=Bedingter Operator "{0}" wird nicht unterstützt
data.engine.EmptyExpression=Der Ausdruck darf nicht leer oder gleich Null sein.

data.engine.InvalidJoinType=Ungültige Knotenpunktart
data.engine.InvalidJoinOperator=Ungültiger Knotenpunkt-Operator
data.engine.BadDataSetParamName=Nicht definierter Datensatzparameter: {0}.

#column binding
data.engine.InvalidBoundColumnName=Ungültiger Name für gebundene Spalte: {0}.
data.engine.InvalidGroupKey=Gruppenschlüssel "{0}" in Gruppe {1} darf nicht von Spaltenbindungen abhängen, die in einer geringeren Gruppe als der eigenen Gruppe definiert wurden. 
data.engine.ColumnBindingNotExist=Die Spaltenbindung "{0}" existiert nicht.
data.engine.ColumnBindingCycle=Die Spaltenbindung "{0}" hat einen Abhängigkeitszyklus.

#computed column value
data.engine.BadCompColExpr=Der Ausdruck für die berechnete Spalte "{0}" ist ungültig.
data.engine.CompCol.WrongDataType=Der Datentyp der berechneten Spalte "{0}" lautet {1}, einer ihrer Werte ist jedoch "{2}". Dieser kann nicht in {1} konvertiert werden.
data.engine.ScriptResult.WrongDataType=Der Datentyp des Skriptausdruckergebnisses lautet {0}, einer seiner Werte ist jedoch "{1}". Dieser kann nicht in {0} konvertiert werden.

data.engine.BadFilterExpr=Der Filterausdruck "{0}" ist ungültig.
data.engine.BadFilterDefn=Die Filterdefinition mit dem Ausdruck "{0}" ist ungültig.

#clob and blob error
data.engine.opencloberror=Fehler beim Öffnen eines CLOB-Zeichenstroms.
data.engine.readcloberror=Fehler beim Lesen eines CLOB-Zeichenstroms.
data.engine.openbloberror=Fehler beim Öffnen eines binären BLOB-Zeichenstroms.
data.engine.readbloberror=Fehler beim Lesen eines binären BLOB-Zeichenstroms.

#resultsetcache exception
data.engine.BadDestIndex=Der Wert {0} von destIndex ist ungültig. Er sollte zwischen 0 und {1} liegen.
data.engine.WriteTempError=Ausnahmefehler beim Erstellen einer temporären Datei für großen Datensatz.
data.engine.ReadTempError=Ausnahmefehler beim Lesen einer temporären Datei für großen Datensatz.

#load data engine factory exception
load.factory.error=Beim Laden der Factory der Datenengine tritt ein Fehler auf.

# Wrapped Error
data.engine.DataConversionError=Bei der Datentypkonversion ist ein Fehler aufgetreten.
data.engine.BadDataTypeCondExpr=Ungültiger Datentyp in bedingtem Ausdruck gefunden.
data.engine.BirtException=In BIRT ist ein Ausnahmefehler aufgetreten: {0}. Weitere Informationen können Sie dem nächsten Ausnahmefehler entnehmen.

# data set cache error
data.engine.datasetcache.save.error=Beim Speichern von Dateien des Datensatz-Caches ist ein Fehler aufgetreten.
data.engine.datasetcache.load.error=Beim Laden von Dateien des Datensatz-Caches ist ein Fehler aufgetreten.

# error related with data engine context
data.engine.InvalidMode=Der Modus-Wert {0} des Datenengine-Kontexts ist ungültig.
data.engine.InvalidArchive=Bei der Er- und Darstellung darf das Berichtarchiv nicht Null sein. 
data.engine.SaveReportDocumentError=Beim Speichern ist ein Fehler im Berichtdokument aufgetreten: {0}
data.engine.LoadReportDocumentError=Beim Laden ist ein Fehler im Berichtdokument aufgetreten: {0}
data.engine.GetSaveStreamError=Beim Versuch, den Speicherdatenstrom abzurufen, ist ein Fehler im Berichtdokument aufgetreten.
data.engine.GetLoadStreamError=Beim Versuch, den Ladedatenstrom abzurufen, ist ein Fehler im Berichtdokument aufgetreten.
data.engine.NullExpr=Ausdruck für Wertabfrage bei der Darstellung darf nicht Null sein.
data.engine.InValidExpr=Unzulässiger Ausdruck für die Wertabfrage bei der Darstellung.
data.engine.BadGroupExpr=Der Gruppenausdruck darf nicht leer sein. 
data.engine.ResultSetNotStart=Das Ergebnis wurde noch nicht gestartet.

data.engine.invalidRowIndex=Ungültiger Zeilenindexwert: {0}
data.engine.backwardSeekError=Die Rückwärtssuche wird in dieser Version nicht unterstützt.
data.engine.startError=Die nächste Methode muss zuerst aufgerufen werden.

# query running based on report document
data.engine.rd.errorfilters=Die in der Abfrage definierten Filter werden nicht unterstützt.
 
# other error
data.engine.ScriptEvalError=Fehler beim Auswerten des Skripts (Quelle:{0}, Zeile:{1}: {2}.)
data.engine.PreparedQueryClosed=Instanz von PreparedQuery wurde geschlossen.
data.engine.NullDefaultInParamValue=Standardwert für Eingabeparameter darf nicht Null sein.
data.engine.BadGroupKeyExpr=Gruppenschlüsselausdruck {0} ist ungültig.
data.engine.GroupNotExist=In Spalte {1} verwendete Gruppe "{0}" existiert nicht.
data.engine.BadSortKeyExpr=Sortierschlüsselausdruck {0} ist ungültig.
data.engine.NoParamMetaForDataSource=Parameter-Metadaten für diesen Datenquelltyp können nicht abgerufen werden.
data.engine.UnexpectedError=Datenengine-Vorgang aufgrund von unerwartetem Fehler fehlgeschlagen. 
data.engine.ConvertDataTypeError=Parameterwert "{0}" kann nicht in Typ "{1}" umgewandelt werden.
data.engine.CannotInstantiateAggregationFactory=Instanziieren der Aggregat-Factory nicht möglich: {0}
date.engine.DuplicateAggregationName=Doppelter Aggregatname: {0}
data.engine.incorrectGroupKeyValues=Das angegebene Werte-Array der Gruppenschlüssel ist ungültig.
data.engine.NoExpressionProcessorAvailable=Kein Ausdruck-Prozessor verfügbar.
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} kann nicht in {1} umgewandelt werden.
data.engine.NoRowUpdate=Die Datenzeile kann momentan nicht aktualisiert werden.
data.engine.BadDataType=Datentyp wird nicht unterstützt.
data.engine.BadDataExpression=Der Datenausdruck darf nicht null sein.

data.engine.cacheFunctionWrongMode=Die Cache-Funktion funktioniert nur im Modus ''DIRECT_PRESENTATION''.
data.engine.document.invalidIndexArray=Ungültiges Index-Array.
data.engine.document.invalidGroupItem=Das entfernte Gruppenelement ist nicht korrekt.
data.engine.resultIteratorNotOpen=Result Iterator wurde noch nicht geöffnet.
data.engine.resultIteratorClosed=Result Iterator wurde geschlossen.
data.engine.invalidOutputParameterIndex=Ungültiger Ausgabeparameter-Index: {0}
data.engine.outputParameterOutOfBound=Ausgabeparameter-Index außerhalb der zulässigen Grenzen: {0}
data.engine.invalidOutputParameterName=Ungültiger Ausgabeparameter-Name: {0}
data.engine.document.notSupportInPresentation=In Präsentation nicht unterstützt.
data.engine.wrongStatus=Falscher Status.
data.engine.wrongVersion=In früheren Versionen als 2.1 nicht unterstützt.
data.engine.unsupportOperationException=Nicht unterstützter Vorgang.

# oda consumer error
odaconsumer.ColumnNameCannotBeEmptyOrNull=Der Spaltenname darf nicht leer oder gleich Null sein.
odaconsumer.ColumnPositionIsOneBased=Column position is 1-based and cannot be less than 1.
odaconsumer.ColumnAliasCannotBeEmpty=Bei dem Spaltenalias darf es sich nicht um eine leere Zeichenfolge handeln.
odaconsumer.CannotProcessDriverConfig=Die Konfiguration des Treibers für {0} kann nicht verarbeitet werden.
odaconsumer.OdaInitEntryNotFound=Der erste Eingangspunkt des Treibers {0} kann nicht gefunden oder instanziiert werden.
odaconsumer.OdaDriverOnUnsupportedPlatform=Der Treiber {0} bietet keine Unterstützung für {1}.
odaconsumer.CannotGenerateUrl=URL {0} für den Klassen-Loader kann nicht erstellt werden.
odaconsumer.UnsupportedNativeType=Nicht unterstützter programmeigener Typ {0} für den Datensatztyp {1} des Treibers {2}.
odaconsumer.UnsupportedDataSetType=Der Datensatztyp {0} wird vom Treiber {1} nicht unterstützt.
odaconsumer.CannotDetermineDefaultDataSetType=Kein Datensatztyp. Der Standard-Datensatztyp für Treiber {0} kann nicht festgestellt werden.
odaconsumer.InvalidDataSetType=Datensatztyp {0} ist ungültig. Der Standard-Datensatztyp für den Datensatz {1} kann nicht festgestellt werden.
odaconsumer.ParameterNameCannotBeEmptyOrNull=Der Parametername darf nicht leer oder gleich Null sein.
odaconsumer.ParameterPositionIsOneBased=Die Parameterposition darf nicht unter 1 liegen.
odaconsumer.CannotGetParameterTypeName=Typname für den Parameter kann nicht abgerufen werden: {0}
odaconsumer.CannotGetParameterMode=Modus für Parameter kann nicht abgerufen werden: {0}
odaconsumer.CannotGetParameterScale=Skalierung für Parameter kann nicht abgerufen werden: {0}
odaconsumer.CannotGetParameterPrecision=Präzision für Parameter kann nicht abgerufen werden: {0}
odaconsumer.CannotGetParameterIsNullable=Es kann nicht festgestellt werden, ob Nullwerte für Parameter zulässig sind: {0}
odaconsumer.SameParamNameForDifferentHints=Ein weiterer Parameter im Datensatz wurde mit demselben Namen {0} definiert.
odaconsumer.DifferentParamNameForSamePosition=Ein weiterer Parameter {0} im Datensatz wurde mit derselben Position {1} definiert.
odaconsumer.ParameterValueIsNull=Der Parameterwert ist gleich Null.
odaconsumer.UnsupportedParameterValueType=Nicht unterstützter Parameterwerttyp: {0}
odaconsumer.UnknownExceptionThrown=Ein unbekannter Ausnahmefehler wurde vom letzten Aufruf der Setter-Methode ausgelöst.
odaconsumer.CannotConvertIndexedParameterValue=Parameterwert {0} bei Index {1} kann nicht von Werttyp {2} in ODA-Typ {3} umgewandelt werden.
odaconsumer.CannotConvertNamedParameterValue=Parameterwert {0} mit Parameternamen {1} kann nicht von Werttyp {2} in ODA-Typ {3} umgewandelt werden.
odaconsumer.UnrecognizedProjectedColumnName=Nicht erkannter geplanter Spaltenname: {0}.
odaconsumer.ColumnNameOrAliasAlreadyUsed=Der neue Spaltenname oder -alias {0} wird bereits von der Spalte bei Index {1} verwendet.
odaconsumer.NamedResultSetsUnsupported=Benannte Ergebnisse werden von dieser Anweisung nicht unterstützt.
odaconsumer.OutputParameterUnsupported=Ausgabeparameter werden von dieser Anweisung nicht unterstützt.
odaconsumer.CannotGetConnectionMetaData=Die Verbindungsmetadaten können nicht abgerufen werden.
odaconsumer.CannotGetMaxConnections=Die Höchstanzahl von Verbindungen für die Datenquelle kann nicht abgerufen werden.
odaconsumer.CannotGetMaxStatements=Die Höchstanzahl von Abfragen für die Datenquelle kann nicht abgerufen werden.
odaconsumer.CannotGetDataSetMetaData=Die Datensatzmetadaten {0} können nicht aus der Datenquelle abgerufen werden.
odaconsumer.CannotPrepareStatement=Die folgende Abfrage für den Datensatztyp {1} kann nicht vorbereitet werden. \n[{0}]
odaconsumer.CannotCloseConnection=Die Verbindung kann nicht geschlossen werden.
odaconsumer.CannotOpenConnection=Die Verbindung für den Treiber {0} kann nicht geöffnet werden
odaconsumer.CannotDetermineSupportForMultipleOpenResults=Unterstützung für mehrere offene Ergebnisse kann nicht festgestellt werden.
odaconsumer.CannotDetermineSupportForMultipleResultSets=Unterstützung für mehrere Ergebnisse kann nicht festgestellt werden.
odaconsumer.CannotDetermineSupportForNamedResultSets=Unterstützung für benannte Ergebnisse kann nicht festgestellt werden.
odaconsumer.CannotDetermineSupportForNamedParameters=Unterstützung für benannte Parameter kann nicht festgestellt werden.
odaconsumer.CannotDetermineSupportForInParameters=Unterstützung für Eingabeparameter kann nicht festgestellt werden.
odaconsumer.CannotDetermineSupportForOutParameters=Unterstützung für Ausgabeparameter kann nicht festgestellt werden.
odaconsumer.CannotSetStatementProperty=Anweisungseigenschaft kann nicht festgelegt werden.
odaconsumer.CannotSetSortSpec=Sortieranweisungen können nicht festgelegt werden.
odaconsumer.CannotSetMaxRows=Höchstanzahl von Zeilen kann nicht festgelegt werden.
odaconsumer.CannotGetResultSetMetaData=Ergebnismetadaten können nicht abgerufen werden.
odaconsumer.CannotGetMetaDataForNamedResultSet=Metadaten für das benannte Ergebnis {0} können nicht abgerufen werden.
odaconsumer.CannotExecuteStatement=Anweisung kann nicht ausgeführt werden.
odaconsumer.CannotGetResultSet=Ergebnis kann nicht abgerufen werden.
odaconsumer.CannotGetNamedResultSet=Benanntes Ergebnis {0} kann nicht abgerufen werden.
odaconsumer.CannotFindOutParameter=Ausgabeparameter {0} kann nicht gefunden werden.
odaconsumer.CannotGetParameterType=Typ für Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotCloseStatement=Anweisung kann nicht geschlossen werden.
odaconsumer.CannotGetColumnCount=Spaltenanzahl kann nicht abgerufen werden.
odaconsumer.CannotGetColumnName=Name der Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetColumnLabel=Beschriftung der Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetColumnType=Typ der Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotFetchNextRow=Nächste Datenzeile kann nicht abgerufen werden.
odaconsumer.CannotDetermineWasNull=Es kann nicht festgestellt werden, ob der letzte abgerufene Wert gleich Null war.
odaconsumer.CannotGetIntFromColumn=Ganzzahlwert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetDoubleFromColumn=Double-Wert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetStringFromColumn=Zeichenfolgewert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetBigDecimalFromColumn=Dezimalwert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetDateFromColumn=Datumswert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetTimeFromColumn=Uhrzeitwert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetTimestampFromColumn=Timestamp-Wert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetClobFromColumn=CLOB-Wert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetBlobFromColumn=BLOB-Wert aus Spalte {0} kann nicht abgerufen werden.
odaconsumer.CannotGetRowPosition=Aktuelle Spaltenposition kann nicht abgerufen werden.
odaconsumer.CannotCloseResultSet=Ergebnis kann nicht geschlossen werden.
odaconsumer.CannotGetParameterCount=Parameteranzahl kann nicht abgerufen werden.
odaconsumer.CannotGetIntFromParameter=Ganzzahlwert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetDoubleFromParameter=Double-Wert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetStringFromParameter=Zeichenfolgewert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetBigDecimalFromParameter=Dezimalwert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetDateFromParameter=Datumswert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetTimeFromParameter=Uhrzeitwert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetTimestampFromParameter=Timestamp-Wert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetBlobFromParameter=BLOB-Wert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotGetClobFromParameter=CLOB-Wert aus Parameter {0} kann nicht abgerufen werden.
odaconsumer.CannotFindInParameter=Eingabeparameter {0} kann nicht gefunden werden.
odaconsumer.CannotSetIntParameter=Ganzzahlwert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotSetDoubleParameter=Double-Wert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotSetStringParameter=Zeichenfolgewert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotSetBigDecimalParameter=Dezimalwert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotSetDateParameter=Datumswert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotSetTimeParameter=Uhrzeitwert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotSetTimestampParameter=Timestamp-Wert für Parameter {0} kann nicht festgelegt werden.
odaconsumer.CannotClearInParameters=Eingabeparameterwerte können nicht gelöscht werden.
odaconsumer.UnrecognizedOdaType=Nicht erkannter ODA-Datentyp: {0}
odaconsumer.CannotGetColumnNativeDataTypeName=Programmeigener Datentypname für die Spalte "{0}" kann nicht abgerufen werden
odaconsumer.CannotGetParameterMetaData=Die Metadaten für den Parameter des ODA-Treibers können nicht abgerufen werden.
odaconsumer.CannotLoadOdaAdapter={0}-Treiber kann nicht geladen werden; DTP-zu-BIRT-ODA-Adapter-Plug-In {1} fehlt.
odaconsumer.MustSpecifyColumnName=Beim Hinzufügen eines Spaltenhinweises muss ein Spaltenname angegeben werden.

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=Nach Aufruf von executeQuery() können Ergebnisse nicht nach Namen abgerufen werden.
odaconsumer.manager.CannotSetParamBeforePrepare=Die Eingabeparameter können erst nach Vorbereitung der Anweisung eingestellt werden.
odaconsumer.manager.CannotGetParamBeforeExecute=Abrufen der Ausgabeparameter vor Ausführung der Anweisung nicht möglich.
odaconsumer.manager.CannotGetParamMdBeforePrepare=Metadaten für Parameter können erst nach Vorbereitung der Anweisung abgerufen werden.
odaconsumer.manager.CannotSetSortSpecBeforePrepare=Die Sortierangaben können erst nach Vorbereitung der Anweisung festgelegt werden.
odaconsumer.manager.MaxConcurrentConnectionsReached=Maximale Anzahl aktiver gleichzeitiger Verbindungen wurde erreicht.
odaconsumer.manager.ConnectionFailed=Öffnen der Verbindung nicht möglich.
odaconsumer.manager.CannotConstructConnectionFactory=Verbindungs-Factory für ODA-Treiber kann nicht erstellt werden.
odaconsumer.manager.InadequatePermissionsForCompatibility=Rückwärtskompatibilität aufgrund unzureichender Berechtigungen nicht möglich.
odaconsumer.manager.ConnectionIsInactive=Die hergestellte Verbindung zur Datenquelle ist nicht aktiv.
odaconsumer.manager.MaxConcurrentStatementsReached=Maximale Anzahl aktiver gleichzeitiger Anweisungen für diese Verbindung erreicht.
odaconsumer.manager.PrepareFailed=Vorbereitung fehlgeschlagen.
odaconsumer.manager.CannotGetResultSetMdBeforePrepare=Metadaten für Ergebnisse können erst nach Vorbereitung der Anweisung abgerufen werden.
odaconsumer.manager.CannotExecuteBeforePrepare=Die Anweisung kann erst ausgeführt werden, nachdem sie vorbereitet wurde.
odaconsumer.CannotFindLogDirectory=Protokollverzeichnis kann nicht gefunden werden.
data.engine.resultClassSaveError=Einige Spaltenverweise in der Datenspaltenbindung existieren nicht.
