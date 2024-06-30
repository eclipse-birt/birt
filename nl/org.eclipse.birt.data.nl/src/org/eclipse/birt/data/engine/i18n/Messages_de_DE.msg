#************************************************************************
# Copyright (c) 2004, 2010 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#
#************************************************************************
# Localized messages for the DTE Engine. Messages here give
# the text of Data Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} in {1} nicht gefunden.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=Datenquelle nicht definiert: {0}.

data.engine.UndefinedDataSet=Datensatz nicht definiert: {0}.

data.engine.UnsupportedDataSourceType=Datenquelle "{0}" hat einen nicht unterstützten Typ.

data.engine.UnsupportedDataSetType=Datensatz "{0}" hat einen nicht unterstützten Typ.

data.engine.UnsupportedGroupOnClobAndBLOB=Gruppierung nach CLOB- oder BLOB-Daten "{0}" wird nicht unterstützt.

data.engine.UnsupportedSortOnClobAndBLOB=Sortierung nach CLOB- oder BLOB-Daten "{0}" wird nicht unterstützt.

data.engine.EndOfGroupOnly=Dieser Aggregatausdruck ist nur am Ende einer Gruppe verfügbar.

data.engine.InvalidAggregation=Die Aggregatdefinition ist ungültig:{0}.

data.engine.InvalidAggrGroup=Ungültige Gruppierungsebene für Aggregatfunktion: {0}.

data.engine.WrongNumAggrArgs=Falsche Anzahl von Argumenten für Aggregatfunktion: {0}.

data.engine.AggrParamGroupLevelInconsistent=In verschachtelten Aggregaten '{0}' müssen alle nicht konstanten Ausdrücke derselben Gruppenebene angehören.

data.engine.DupPropertyName=Doppelter Eigenschaftsname gefunden: {0}.

data.engine.NoSubQueryName=Teilabfrage mit Namen konnte nicht gefunden werden: {0}.

data.engine.DataSourceMissingExtId=Fehlende Erweiterungs-ID in Definition der Datenquelle "{0}".

data.engine.NullGroupStart=Der Gruppenstartwert darf nicht Null sein.

data.engine.UnsupportedDirectNestedAggregate=Direktes Verschachteln von Aggregaten wird nicht unterstützt.

data.engine.ResultClosed=QueryResults oder der zugehörige Iterator wurden geschlossen.

data.engine.NoCurrentRow=Result Iterator hat keine aktuelle Zeile. Der Iterator wurde entweder nicht gestartet, oder er wurde beendet.

data.engine.InvalidJSExpr=Ungültiger Javascript-Ausdruck: {0}.

data.engine.InvalidTotalFuncName=Ungültiger Name für Summenfunktion: {0}.

data.engine.InvalidTotalFunc=Ungültige Summenfunktion.

data.engine.NoAggrFunc=Aufruf für Aggregatfunktion wird in diesem Ausdruck nicht erwartet.

data.engine.InvalidExprHandle=Ungültiges Ausdruck-Handle.

data.engine.InvalidAggrGroupExpression=Falscher Gruppenausdruck für Aggregatfunktion: {0}.

data.engine.InvalidSortKeyColumn=Ungültige Sortierschlüsselspalte: {0}.

data.engine.InvalidGroupLevel= Die Gruppenebene im Aggregat {0} ist ungültig.

data.engine.InvalidGroupKeyName=Gruppierung nach Spalte: {0} ist kein unterstützter Vorgang.

data.engine.BadCloseScript=CloseScript in CustomDataSet konnte nicht ausgewertet werden.

data.engine.InvalidCustomFieldIdx=Ungültiger Index für benutzerdefiniertes Feld: {0}.

data.engine.InvalidGroupName=Der Gruppenname ist ungültig.

data.engine.DuplicateGroupName=Die Gruppennamen sind doppelt vorhanden.

data.engine.BadOpenScript=OpenScript in CustomDataSet konnte nicht ausgewertet werden.

data.engine.DataSourceIsOpen=Datenquelle ist bereits geöffnet.

data.engine.DataSourceNotOpen=Datenquelle ist nicht geöffnet.

data.engine.EmptyCustomFieldName=Name des benutzerdefinierten Feldes darf nicht leer sein.

data.engine.DupCustomFieldName=Doppelt vergebener benutzerdefinierter Feldname: {0}.

data.engine.NoResultSet=Ausführung der Abfrage hat kein Ergebnis zurückgegeben.

data.engine.NoResultMetadata=Metadatenergebnis ist nicht verfügbar.

data.engine.QueryAlreadyPrepared=Abfrage wurde bereits vorbereitet.

data.engine.QueryNotPrepared=Abfrage wurde noch nicht vorbereitet.

data.engine.InvalidFieldName=Ungültiger Feldname: {0}.

data.engine.DupColumnName=Doppelter Spaltenname: {0}.

data.engine.DupAlias=Doppelter Spaltenname oder -alias: {0}.

data.engine.InvalidFieldIndex=Ungültiger Feldindex: {0}.

data.engine.fail.prepareExecution=Abfrageausführung konnte nicht vorbereitet werden für Datensatz: {0}

data.engine.groupIncrementalUpdate.IllegalGroupOrderState=Unzulässiger Gruppenaktualisierungsstatus: aktuelle Gruppe <{0}> größer als Verarbeitungsgruppe <{1}>.

data.engine.MatchError=Überprüfen Sie die Syntax des Musters. Fehler beim Abgleichen der Zeichenfolge mit dem Muster: {0}.

data.engine.ExpectBooleanReturnType=Skript {0} des geskripteten Datensatzes muss Boolesches Ergebnis zurückgeben.

data.engine.BadIntervalUnit=Ungültiger Intervall-Einheitswert: {0}.

data.engine.BadGroupKeyType=Gruppenschlüssel enthält nicht unterstützten Datentyp: {0}.

data.engine.group.interval=Das Festlegen von "{0}" als Intervall für eine Spalte des Typs {1} wird nicht unterstützt.

data.engine.group.interval.Rowid=In der Spalte "ROWID" werden als Intervalltyp nur numerische Intervalle unterstützt.

data.engine.group.interval.invalid=Das angegebene Intervall ist ungültig.

data.engine.group.intervalRange.invalid=Ungültiger Intervallbereich: {0}.

data.engine.NoSupportFilterOnGroup=Das Filtern nach Gruppen wird momentan nicht unterstützt.

data.engine.InvalidTopBottomArgument=Das Argument für Top/Bottom-Filter muss eine Zahl sein.

data.engine.InvalidTopBottomNArgument=Das Argument der Filter "TopN/BottomN" muss entweder Null oder eine positive Ganzzahl sein.

data.engine.InvalidTopBottomPercentArgument=Bei den Argumenten der Filter für "TopPercent/BottomPercent" muss es sich um positive Zahlen bis maximal 100 handeln;

data.engine.UnsupportedCondOp=Bedingter Operator wird nicht unterstützt: {0}.

data.engine.EmptyExpression=Der Ausdruck darf nicht leer oder gleich Null sein.

data.engine.AggregationBinding.EmptyArgument=Das Argument "{0}" ist für die Aggregatsbindung "{1}" erforderlich.

data.engine.InvalidJoinType=Ungültige Knotenpunktart

data.engine.InvalidJoinOperator=Ungültiger Knotenpunkt-Operator

data.engine.BadDataSetParamName=Nicht definierter Datensatzparameter: {0}.

data.engine.FailComputeOutputParameterValue=Ausgabeparameterwert konnte nicht ausgewertet werden: {0}.

data.engine.LinkedReportParamAllowMultiValues=Der Berichtparameter "{0}", der mehrere Werte ermöglicht, kann nicht zur Verknüpfung mit dem Datensatzparameter "{1}" verwendet werden.

#column binding
data.engine.EmptyBindingName=Bindungsname darf nicht Null oder leer sein.

data.engine.InvalidBoundColumnName=Ungültiger Name für gebundene Spalte: {0}.

data.engine.InvalidGroupKey=Gruppenschlüssel "{0}" in Gruppe {1} darf nicht von Spaltenbindungen abhängen, die in einer geringeren Gruppe als der eigenen Gruppe definiert wurden.

data.engine.ColumnBindingNotExist=Die Bindung '{0}' existiert nicht.

data.engine.ColumnBindingCycle=Bindung "{0}" hat einen Abhängigkeitszyklus.

data.engine.ColumnBindingReferToInexistColumn=Spaltenbindung "{0}" hat auf eine Bindung "{1}" verwiesen, die nicht existiert.

data.engine.ColumnBindingReferToInexistBinding=Spaltenbindung "{0}" hat auf eine Datensatzspalte "{1}" verwiesen, die nicht existiert.

data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=Die Spaltenbindung "{0}" ist nicht korrekt: die übergeordneten Abfragespaltenbindungen, zu denen Aggregate gehören, können nicht als Spaltenbindung von Teilabfragen verwendet werden.

data.engine.ReadColumnValueFromDocumentError=Der Fehler tritt beim Lesen von Spaltenwert {0} auf. Der Grund ist möglicherweise ein Laufzeitfehler bei der Auswertung des Bindungsausdrucks, also ein 'Division-durch-0'-Fehler.

data.engine.NoOuterResultsExist=Die äußeren Abfrageergebnisse existieren nicht. Der Grund hierfür ist möglicherweise ein Außenverweis in einer Abfrage ohne übergeordnete Abfrage.

data.engine.DuplicatedBindingName=Doppelter Bindungsname: {0}.

data.engine.InvalidAggrLevelKeyInSummaryQuery=Das Aggregat: {0} verweist auf einen falschen Gruppennamen.
#computed column value
data.engine.BadCompColExpr=Der Ausdruck für die berechnete Spalte "{0}" ist ungültig.

data.engine.CompCol.FailRetrieveValueComputedColumn=Wert für berechnete Spalte "{0}" konnte nicht berechnet werden.

data.engine.ScriptResult.WrongSystemComputedColumn=Bewertung von Sorte, Gruppierung, Filter oder Bindungsausdruck nicht möglich. Ein Datenfeld, das beim Ausdruck verwendet wird, ist im Datensatz nicht vorhanden.

data.engine.ComputedColumnCycle=Berechnete Spalte "{0}" hat einen Abhängigkeitszyklus.

data.engine.SortOnAggregation=Sortieren nach nicht-gruppierendem Aggregat oder berechneter Spalte für Aggregat '{0}' wird nicht unterstützt.

data.engine.BadFilterExpr=Der Filterausdruck "{0}" ist ungültig.

data.engine.BadFilterDefn=Der auf dem Ausdruck '{0}' basierende Filter konnte nicht ausgewertet werden.

#clob and blob error
data.engine.opencloberror=Fehler beim Öffnen eines CLOB-Zeichenstroms.

data.engine.readcloberror=Fehler beim Lesen eines CLOB-Zeichenstroms.

data.engine.openbloberror=Fehler beim Öffnen eines binären BLOB-Zeichenstroms.

data.engine.readbloberror=Fehler beim Lesen eines binären BLOB-Zeichenstroms.

#resultsetcache exception
data.engine.BadDestIndex=Der Wert {0} von destIndex ist ungültig. Er sollte zwischen 0 und {1} liegen.

data.engine.WriteTempError=Ausnahmefehler beim Erstellen einer temporären Datei für großen Datensatz.

data.engine.ReadTempError=Ausnahmefehler beim Lesen einer temporären Datei für großen Datensatz.

#resultiterator cache exception
data.engine.CreateCacheTempError=Ausnahme beim Erstellen einer temporären Cache-Datei für Ergebniszeilen.

data.engine.OpenCacheTempError=Ausnahme beim Öffnen einer temporären Cache-Datei für Ergebniszeilen.

data.engine.WriteCacheTempError=Ausnahme beim Schreiben einer temporären Cache-Datei für Ergebniszeilen.

data.engine.ReadCacheTempError=Ausnahme beim Lesen einer temporären Cache-Datei für Ergebniszeilen.

data.engine.CloseCacheTempError=Ausnahme beim Schließen einer temporären Cache-Datei für Ergebniszeilen.

data.engine.FailedLoadColumnValue=Fehler beim Laden von Wert für '{0}'-Typ-Spalte '{1}'.

data.engine.FailedLoadClass=Fehler beim Laden von Klasse '{0}' für '{1}'-Typ-Spalte '{2}'. Klasse '{0}' muss im Pfad zum Laden der BIRT-Engine-Klasse enthalten sein.

#load data engine factory exception
load.factory.error=Beim Laden der Factory der Datenengine tritt ein Fehler auf.

# Wrapped Error
data.engine.DataConversionError=Bei der Datentypkonversion ist ein Fehler aufgetreten.

data.engine.BadCompareExpr='{0}' kann nicht mit '{1}' verglichen werden.

data.engine.BadCompareSingleValueWithMultiValues=Der Filter konnte nicht angewendet werden, da die Filterbedingung ungültig ist. Ein einzelner Wert kann nicht mit mehreren Werten von [{0}] verglichen werden.\nGeben Sie nur einen Wert für diesen Filter an.

data.engine.BirtException=In BIRT ist ein Ausnahmefehler aufgetreten. Weitere Informationen, siehe nächster Ausnahmefehler.

# data set cache error
data.engine.datasetcache.save.error=Beim Speichern von Dateien des Datensatz-Caches ist ein Fehler aufgetreten.

data.engine.datasetcache.load.error=Beim Laden von Dateien des Datensatz-Caches ist ein Fehler aufgetreten.

data.engine.exceed.max.data.object.row=Abfrage kann nicht verarbeitet werden. Um die Abfrage auszuführen, muss entweder der Ergebnispuffer vergrößert oder die Abfrage eingegrenzt werden.

# error related with data engine context
data.engine.InvalidMode=Der Modus-Wert {0} des Datenengine-Kontexts ist ungültig.

data.engine.InvalidArchive=Bei der Er- und Darstellung darf das Berichtarchiv nicht Null sein.

data.engine.SaveReportDocumentError=Beim Speichern ist ein Fehler im Berichtdokument aufgetreten: {0}.

data.engine.LoadReportDocumentError=Beim Laden ist ein Fehler im Berichtdokument aufgetreten: {0}.

data.engine.GetSaveStreamError=Beim Versuch, den Speicherdatenstrom abzurufen, ist ein Fehler im Berichtdokument aufgetreten.

data.engine.GetLoadStreamError=Beim Versuch, den Ladedatenstrom abzurufen, ist ein Fehler im Berichtdokument aufgetreten.

data.engine.NullExpr=Ausdruck für Wertabfrage bei der Darstellung darf nicht Null sein.

data.engine.InValidExpr=Unzulässiger Ausdruck für die Wertabfrage bei der Darstellung.

data.engine.BadGroupExpr=Der Gruppenausdruck darf nicht leer sein.

data.engine.filterExprContainRowNum=Filterausdruck kann nicht auf Javascript-Objekt "row.__num" verweisen.

data.engine.sortExprContainRowNum=Sortierungsausdruck kann nicht auf Javascript-Objekt "row.__num" verweisen.

data.engine.ResultSetNotStart=Das Ergebnis wurde noch nicht gestartet.

data.engine.GetLevelMemberError=Diese Datenbindung wird außerhalb des verfügbaren Umfangs verwendet.

data.engine.invalidRowIndex=Ungültiger Zeilenindexwert: {0}.

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

date.engine.DuplicateAggregationName=Doppelter Aggregatname: {0}.

data.engine.InconsistentAggregationArgumentDefinition=Das in der Aggregaterweiterungs-Implementierung definierte Aggregat-Argument stimmt nicht mit dem in der Datei 'plugin.xml' definierten Aggregat-Argument überein.

data.engine.incorrectGroupKeyValues=Das angegebene Werte-Array der Gruppenschlüssel ist ungültig.

data.engine.NoExpressionProcessorAvailable=Kein Ausdruck-Prozessor verfügbar.

data.engine.InconvertibleDatatype=Der ursprüngliche Datentyp von Wert {0} lautet {1} und kann nicht in den zugewiesenen Datentyp {2} konvertiert werden.

data.engine.ConvertToDataTypeError={0} kann nicht in {1} umgewandelt werden.

data.engine.NoRowUpdate=Die Datenzeile kann momentan nicht aktualisiert werden.

data.engine.BadDataType=Datentyp wird nicht unterstützt: {0}.

data.engine.NotSerializableClass=Serialisierung der Klasse {0} wird nicht unterstützt.

data.engine.BadDataExpression=Der Datenausdruck darf nicht null sein.

data.engine.aggregation.InvalidBindingExpression=Berechnete Maßeinheit ist nicht akzeptabel für die Aggregatsbindung '{0}'.

data.engine.InvalidExpression=Ungültiger Ausdruck "{0}".

data.engine.BadFetchScriptReturnType=Datensatzskript-Methode '{0}' hat {1} zurückgegeben; Boolescher Wert erwartet.

data.engine.FailToExecuteScript=Skript in Funktion {0}() konnte nicht ausgeführt werden. Quelle:\n------\n' + {1} + '\n-----

data.engine.cacheFunctionWrongMode=Die Cache-Funktion funktioniert nur im Modus ''DIRECT_PRESENTATION''.

data.engine.document.invalidIndexArray=Ungültiges Index-Array.

data.engine.document.invalidGroupItem=Das entfernte Gruppenelement ist nicht korrekt.

data.engine.resultIteratorNotOpen=Result Iterator wurde noch nicht geöffnet.

data.engine.resultIteratorClosed=Result Iterator wurde geschlossen.

data.engine.invalidOutputParameterIndex=Ungültiger Ausgabeparameter-Index: {0}.

data.engine.outputParameterOutOfBound=Ausgabeparameter-Index außerhalb der zulässigen Grenzen: {0}.

data.engine.invalidOutputParameterName=Ungültiger Ausgabeparameter-Name: {0}.

data.engine.document.notSupportInPresentation=In Präsentation nicht unterstützt.

data.engine.wrongStatus=Falscher Status.

data.engine.wrongVersion=In früheren Versionen als 2.1 nicht unterstützt.

data.engine.unsupportOperationException=Nicht unterstützter Vorgang.

data.engine.possibleMixedDataTypeInColumn=Beim Übernehmen der Daten ist ein Fehler aufgetreten. Mögliche Ursache: Verschiedene Datentypen in einer Spalte des Typs "BELIEBIG".

data.engine.resultClassSaveError=Die folgenden Spaltenbezüge können nicht gefunden werden: {0}.

data.engine.invalidMemBufferSize=Die Speicher-Puffergröße "{0}" ist ungültig. Die Speicher-Puffergröße muss eine Ganzzahl größer Null sein.

data.engine.document.error.cannotLoadStream=Der Dokumentenstrom "{0}" ist ungültig.

data.engine.failToCreateTempDir=Temporäres Verzeichnis in "{0}" konnte nicht erstellt werden;

data.engine.reportItem.SubQuery.Unsupported=Gemeinsames Berichtobjekt kann nicht für Teilabfrage verwendet werden.

data.engine.emptyResultSet=Das Ergebnis ist leer.

data.engine.errorHappenWhenRetrieveResultSet=Fehler beim Abrufen von Ergebnis "{0}". Möglicherweise ist das angegebene Ergebnis nicht vorhanden.

data.engine.invalidAggregationBindingForPLSReport=Aggregatsbindung '{0}' ist bei aktivierter Seitenebenensicherheit ungültig.

data.engine.acl.dataSetCannotBeAccessed=Zugriff auf Datensatz '{0}' nicht möglich.

data.engine.acl.dataSetColumnCannotBeAccessed=Zugriff auf Datensatzspalte '{0}' nicht möglich.

data.engine.acl.cubeCannotBeAccessed=Benutzer hat keine Berechtigung für den Zugriff auf diesen Quader '{0}'

data.engine.acl.cubeLevelCannotBeAccessed=Benutzer hat keine Berechtigung für den Zugriff auf diese Ebene '{0}'

data.engine.acl.cubeMeasureCannotBeAccessed=Benutzer hat keine Berechtigung für den Zugriff auf diese Einheit '{0}'

data.engine.acl.cubeDimensionCannotBeAccessed=Benutzer hat keine Berechtigung für den Zugriff auf diese Dimension '{0}'.
# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=Der Spaltenname darf nicht leer oder gleich Null sein.

odaconsumer.ColumnPositionIsOneBased=Spaltenposition basiert auf 1 und darf nicht kleiner als 1 sein.

odaconsumer.ColumnAliasCannotBeEmpty=Bei dem Spaltenalias darf es sich nicht um eine leere Zeichenfolge handeln.

odaconsumer.CannotProcessDriverConfig=Konfiguration der Datenquellenerweiterung von {0}-Treiber kann nicht gefunden oder verarbeitet werden.

odaconsumer.OdaInitEntryNotFound=Der erste Eingangspunkt des Treibers {0} kann nicht gefunden oder instanziiert werden.

odaconsumer.OdaDriverOnUnsupportedPlatform=Der Treiber {0} bietet keine Unterstützung für {1}.

odaconsumer.CannotGenerateUrl=URL {0} für den Klassen-Loader kann nicht erstellt werden.

odaconsumer.UnsupportedNativeType=Nicht unterstützter programmeigener Typ {0} für den Datensatztyp {1} des Treibers {2}.

odaconsumer.UnsupportedDataSetType=Der Datensatztyp {0} wird vom Treiber {1} nicht unterstützt.

odaconsumer.CannotDetermineDefaultDataSetType=Kein Datensatztyp. Der Standard-Datensatztyp für Treiber {0} kann nicht festgestellt werden.

odaconsumer.InvalidDataSetType=Datensatztyp {0} ist ungültig. Der Standard-Datensatztyp für den Datensatz {1} kann nicht festgestellt werden.

odaconsumer.ParameterNameCannotBeEmptyOrNull=Der Parametername darf nicht leer oder gleich Null sein.

odaconsumer.ParameterPositionIsOneBased=Die Parameterposition darf nicht unter 1 liegen.

odaconsumer.CannotGetParameterNativeName=Über den Treiber definierter Name des Parameters kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetParameterType=Typ für Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetParameterTypeName=Typname für Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetParameterMode=Modus für Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetParameterScale=Skalierung für Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetParameterPrecision=Präzision für Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetParameterIsNullable=Es kann nicht festgestellt werden, ob Nullwerte für Parameter zulässig sind: {0}.

odaconsumer.SameParamNameForDifferentHints=Ein weiterer Parameter im Datensatz wurde mit demselben Namen {0} definiert.

odaconsumer.DifferentParamNameForSamePosition=Ein weiterer Parameter {0} im Datensatz wurde mit derselben Position {1} definiert.

odaconsumer.ParameterValueIsNull=Der Parameterwert ist gleich Null.

odaconsumer.UnsupportedParameterValueType=Nicht unterstützter Parameterwerttyp: {0}.

odaconsumer.UnknownExceptionThrown=Ein unbekannter Ausnahmefehler wurde vom letzten Aufruf der Setter-Methode ausgelöst.

odaconsumer.CannotConvertIndexedParameterValue=Parameterwert {0} bei Index {1} kann nicht von Werttyp {2} in ODA-Typ {3} umgewandelt werden.

odaconsumer.CannotConvertNamedParameterValue=Parameterwert {0} mit Parameternamen {1} kann nicht von Werttyp {2} in ODA-Typ {3} umgewandelt werden.

odaconsumer.UnrecognizedProjectedColumnName=Nicht erkannter geplanter Spaltenname: {0}.

odaconsumer.ColumnNameOrAliasAlreadyUsed=Der neue Spaltenname oder -alias {0} wird bereits von der Spalte bei Index {1} verwendet.

odaconsumer.NamedResultSetsUnsupported=Benannte Ergebnisse werden von dieser Anweisung nicht unterstützt.

odaconsumer.OutputParameterUnsupported=Ausgabeparameter werden von dieser Anweisung nicht unterstützt.

odaconsumer.UnsupportedMultipleResultSets=Mehrere Ergebnisse werden von dieser Anweisung nicht unterstützt.

odaconsumer.CannotGetConnectionMetaData=Die Verbindungsmetadaten können nicht abgerufen werden.

odaconsumer.CannotGetMaxConnections=Die Höchstanzahl von Verbindungen für die Datenquelle kann nicht abgerufen werden.

odaconsumer.CannotGetMaxStatements=Die Höchstanzahl von Abfragen für die Datenquelle kann nicht abgerufen werden.

odaconsumer.CannotGetDataSetMetaData=Die Datensatzmetadaten {0} können nicht aus der Datenquelle abgerufen werden.

odaconsumer.CannotPrepareStatement=Die folgende Abfrage für den Datensatztyp ({1}) konnte nicht vorbereitet werden: \n[{0}]

odaconsumer.CannotCloseConnection=Die Verbindung kann nicht geschlossen werden.

odaconsumer.CannotOpenConnection=Verbindung für Treiber kann nicht geöffnet werden: {0}.

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

odaconsumer.CannotGetMetaDataForNamedResultSet=Metadaten für benanntes Ergebnis können nicht abgerufen werden: {0}.

odaconsumer.CannotExecuteStatement=Anweisung kann nicht ausgeführt werden.

odaconsumer.CannotGetResultSet=Ergebnis kann nicht abgerufen werden.

odaconsumer.CannotGetNamedResultSet=Benanntes Ergebnis kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetMoreResults=Die Anweisung kann keine weiteren Ergebnisse abrufen.

odaconsumer.CannotFindOutParameter=Ausgabeparameter kann nicht gefunden werden: {0}.

odaconsumer.CannotCloseStatement=Anweisung kann nicht geschlossen werden.

odaconsumer.CannotCancelStatement=Abbrechen der Anweisung nicht möglich.

odaconsumer.CannotGetColumnCount=Spaltenanzahl kann nicht abgerufen werden.

odaconsumer.CannotGetColumnName=Name der Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetColumnLabel=Beschriftung der Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetColumnType=Typ der Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotFetchNextRow=Nächste Datenzeile kann nicht abgerufen werden.

odaconsumer.CannotDetermineWasNull=Es kann nicht festgestellt werden, ob der letzte abgerufene Wert gleich Null war.

odaconsumer.CannotGetIntFromColumn=Ganzzahlwert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetDoubleFromColumn=Doppelwert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetStringFromColumn=Zeichenfolgewert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetBigDecimalFromColumn=Dezimalwert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetDateFromColumn=Datumswert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetTimeFromColumn=Uhrzeitwert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetTimestampFromColumn=Timestamp-Wert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetClobFromColumn=CLOB-Wert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetBlobFromColumn=BLOB-Wert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetBooleanFromColumn=Boolescher Wert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetObjectFromColumn=Objektwert aus Spalte kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetRowPosition=Aktuelle Spaltenposition kann nicht abgerufen werden.

odaconsumer.CannotCloseResultSet=Ergebnis kann nicht geschlossen werden.

odaconsumer.CannotGetParameterCount=Parameteranzahl kann nicht abgerufen werden.

odaconsumer.CannotGetIntFromParameter=Ganzzahlwert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetDoubleFromParameter=Double-Wert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetStringFromParameter=Zeichenfolgewert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetBigDecimalFromParameter=Dezimalwert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetDateFromParameter=Datumswert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetTimeFromParameter=Uhrzeitwert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetTimestampFromParameter=Timestamp-Wert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetBlobFromParameter=BLOB-Wert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetClobFromParameter=CLOB-Wert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetBooleanFromParameter=Boolescher Wert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotGetObjectFromParameter=Objektwert aus Parameter kann nicht abgerufen werden: {0}.

odaconsumer.CannotFindInParameter=Eingabeparameter kann nicht gefunden werden: {0}.

odaconsumer.CannotSetIntParameter=Ganzzahlwert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetDoubleParameter=Doppelwert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetStringParameter=Zeichenfolgenwert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetBigDecimalParameter=Dezimalwert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetDateParameter=Datumswert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetTimeParameter=Zeitwert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetTimestampParameter=Timestamp-Wert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetBooleanParameter=Boolescher Wert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetObjectParameter=Objektwert ({0}) kann nicht auf Parameter {1} festgelegt werden.

odaconsumer.CannotSetNullParameter=Nullwert kann nicht auf Parameter {0} festgelegt werden.

odaconsumer.CannotClearInParameters=Eingabeparameterwerte können nicht gelöscht werden.

odaconsumer.UnrecognizedOdaType=Nicht erkannter ODA-Datentyp: {0}

odaconsumer.CannotGetColumnNativeDataTypeName=Programmeigener Datentypname für die Spalte "{0}" kann nicht abgerufen werden

odaconsumer.CannotGetParameterMetaData=Die Metadaten für den Parameter des ODA-Treibers können nicht abgerufen werden.

odaconsumer.CannotLoadOdaAdapter={0}-Treiber kann nicht geladen werden; DTP-zu-BIRT-ODA-Adapter-Plug-In {1} fehlt.

odaconsumer.MustSpecifyColumnName=Beim Hinzufügen eines Spaltenhinweises muss ein Spaltenname angegeben werden.

odaconsumer.CannotFindLogDirectory=Protokollverzeichnis kann nicht gefunden werden.

odaconsumer.ParameterNames=\nParameter mit Modellnamen ({0}) und über den Treiber definierten Namen ({1}) definiert.

odaconsumer.InvalidArgument=Die Methode ({0}) wurde mit einem ungültigen Argument ({1}) aufgerufen.

odaconsumer.CannotSetConnectionLocale=Verbindungsgebietsschema ({0}) kann nicht festgelegt werden.

odaconsumer.CannotCheckOpenConnection=Es konnte nicht geprüft werden, ob die Verbindung bereits hergestellt wurde.

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


# Data Olap error code
data.olap.OlapDirCreateFail=Verzeichnis {0} konnte nicht erstellt werden.

data.olap.OlapFileCreateFail=Datei {0} konnte nicht erstellt werden.

data.olap.OlapFileNotFound=OLAP-Datei {0} wurde nicht gefunden.

data.olap.OlapFileFormatInvalid=Das Dateiformat ist ungültig. Der Name des Datenobjekts lautet: {0}

data.olap.DimensionNotExist=Die Dimension {0} ist nicht vorhanden.

data.olap.KeyValueCanNotBeNull=Der Wert der Schlüsselspalte darf nicht Null sein. Schlüsselspalte: {0}.

data.olap.DetailMemberHaveMultiParent=Das Detailmitglied {0} hat mehr als ein übergeordnetes Element.

data.olap.OlapFileDataError=Es gibt ungültige Daten in Datenobjekt {0}.

data.olap.DocumentObjectAlreadyExists =Das Dokumentobjekt ist bereits vorhanden. Datenobjekt konnte nicht erstellt werden: {0}.

data.olap.DocumentObjectNotExists =Fehler beim Laden von Quader "{0}" aus Dokument. Das Dokumentobjekt existiert nicht.

data.olap.FacttableRowNotDistinct=Die Zeilen sollten in der Faktentabelle eindeutig sein. "{0}" tritt mehr als einmal auf.

data.olap.FacttableJointColumnNotExist=Die Verknüpfungsspalte "{0}" existiert nicht in den Dimensionen.

data.olap.ParameterColumnOfAggregationNotExist=Die Parameterspalte "{0}" existiert nicht in den Dimensionen.

data.olap.FacttableNullMeasureValue=Der Wert einer Einheitsspalte ist Null. "{0}"

data.olap.InvalidDimensionPositionFacttableRow=Die Faktentabelle des Quaders '{0}' verfügt über eine ungültige Datenzeile, in der der Schlüsselwert '{1}' der Dimension '{2}' ist, obwohl '{2}' nicht in Dimension '{3}' enthalten ist.

data.olap.measureNameNotFound=Der Einheitsname "{0}" wurde nicht gefunden.

data.olap.levelNameNotFound=Der Ebenenname "{0}" wurde nicht gefunden.

data.olap.dimensionNameNotFound=Dimension "{0}" kann in Quaderanfrage nicht gefunden werden; die Dimension existiert nicht oder ist ungültig.

data.olap.ReferencedDimensionNotFound=Dimensionsdefinition {0}, auf die verwiesen wird, ist nicht vorhanden.

data.olap.ReferencedLevelNotFound=Ebene, auf die verwiesen wird, ist nicht vorhanden.

data.olap.cursorPositionError=Der Cursor kann nicht zur Zielposition navigieren.

data.olap.cursorSeekError=Der Cursor kann nicht zur Wunschposition navigieren.

data.olap.CannotGetMeasureValue=Definition der Maßeinheit "{0}" in Quader nicht gefunden. Die Maßeinheit existiert nicht oder ist ungültig.

data.olap.CannotFindLevel=Ebenendefinition '{0}' kann nicht in Dimension '{1}' gefunden werden.

data.olap.tryToAddIdenticalAggrGroup=Der Wert "{0}" ist bereits im Aggregat auf der Liste der Bindung "{1}" vorhanden;

data.olap.NoEdgeDefinitionFound=Ausführung der Abfrage ist fehlgeschlagen, da weder Zeilen- noch Spaltenrand enthalten ist. Mindestens ein Rand muss definiert sein.

data.olap.invalidSortDefinition=Die Sortierdefinition ist ungültig.

data.olap.invalidMeasureRef="{0}" ist kein gültiger Verweisausdruck für die Einheit.

data.olap.invalidLevelAttr=Das Attribut ist ungültig:

data.olap.referencedBindingNotExist=Die Verweisbindung "{0}" existiert nicht.

data.olap.invalidBindingReferToInexitDim=Die Bindung "{0}" verweist auf ungültige Ebenen.

data.olap.invalidBindingReferToInexitMeasure=Die Bindung "{0}" verweist auf ungültige Einheiten.

data.olap.invalidBindingMissingAggrFunc=Die Aggregatfunktion der Bindung "{0}" fehlt.

data.olap.invalidAggrArgument=Nur ein Ebenenattribut kann als Aggregat-Argument fungieren.

data.olap.axis.level.CantBeNull=Mitgliedsebene der Spalte darf nicht Null sein.

data.olap.axis.value.CantBeNull=Wert von Mitgliedsebene \"{0}\" der Spalte darf nicht Null sein.

data.olap.fetchlimitWarning=Die Anzahl der abzurufenden Datensätze beträgt {0} und ist somit geringer als die Länge des gesamten Ergebnissatzes[{1}].

data.olap.failComputeComputedMeasureValue=Werte für berechnete Maßeinheit können nicht berechnet werden.

data.olap.duplicateMeasureName=Doppelter Einheitenname \"{0}\".

data.olap.dataBindingExpressionEmpty=Datenbindungsausdruck darf nicht leer oder gleich Null sein.

data.olap.missingParentCursor=Das übergeordnete Ergebnis ist nicht vorhanden.

data.olap.CannotAccessNullDimensionRow=Zugriff auf Zeile ohne Dimension nicht möglich

data.olap.NonexistentDimension=Nicht vorhandene Dimension:

data.olap.NonexistentLevel=Nicht vorhandene Ebene:

data.olap.NonexistentKeyOrAttr=Nicht vorhandener Schlüssel oder Attribut:

data.olap.UnsupportedFunction=Nicht unterstützte Funktion:

data.olap.UnspecifiedBindingName=Bindungsname nicht angegeben.

data.olap.NotNestAggregationBinding=Aggregatsbindung nicht mit Quader verschachteln: {0}

data.olap.InvalidNestAggregationExpression=Der Ausdruck eines Quader-Verschachtelungsaggregats ist ungültig.

data.olap.InvalidNestAggregationOn=Verschachtelungsaggregat '{0}' konnte aufgrund seines Aggregats auf Ebenen, die in seinen basierten Aggregaten nicht vorhanden sind, nicht berechnet werden.

data.olap.InvalidAggregationFilterExpr=Der Filterausdruck '{0}' ist ungültig. Das Aggregat auf der Liste der Aggregatsbindung '{1}' enthält nicht den Dimensionsebenensatz '{2}', auf den verwiesen wird.

data.olap.sort.exprError=Dimensionsebenensatz, auf den verwiesen wird, darf nur eine Ebene enthalten.

data.olap.query.noCubeBinding=Fehler bei Ausführung der Quaderanfrage: Keine Quaderbindung verfügbar.

data.olap.FailLoadCube=Fehler beim Laden von Quader: '{0}'.

data.olap.MissingDimensionInCube=Der Quader muss mindestens eine Dimension enthalten: '{0}'

data.olap.ExceedIntegerLimit= Die Länge des Ergebnissatzes überschreitet Integer.MAX.

data.olap.ExceedColumnLimit= Die Länge des Ergebnissatzes bis zum Spaltenrand überschreitet die maximale Größe {0}.

data.olap.ExceedRowLimit= Die Länge des Ergebnissatzes bis zum Zeilenrand überschreitet die maximale Größe {0}.

data.cache.UnsupportedIncrementalCacheMode=Der \"{0}\"-Modus der inkrementellen Zwischenspeicherung wird noch nicht unterstützt!
# Configuration file parser error code
data.executor.configFileParser.getModeByID=Der inkrementelle Zwischenspeicherungsmodus konnte nicht anhand des Datensatznamens geladen werden.

data.executor.configFileParser.getTSFormatByID=Das Format des Timestamps konnte anhand des angegebenen Datensatznamens nicht aus der Konfigurationsdatei analysiert werden.

data.executor.configFileParser.getTimeStampColumnByID=Das Format des Timestamps konnte anhand des angegebenen Datensatznamens nicht aus der Konfigurationsdatei analysiert werden.

data.executor.configFileParser.getQueryTextByID=Der Abfragetext konnte anhand des angegebenen Datensatznamens nicht aus der Konfigurationsdatei abgerufen werden.

data.executor.configFileParser.getParametersByID=Der Parameter konnte anhand des angegebenen Datensatznamens nicht aus der Konfigurationsdatei abgerufen werden.

data.aggregation.expression.displayName=Ausdruck

data.aggregation.argumentError=Der Ausdruck des Arguments \"{0}\" für Aggregat \"{1}\" ist Null oder leer.

data.olap.InvalidAggrTypeOnMeasure=Ungültiger Typ für Aggregat in Quadereinheit: '{0}'.

#derived dataSet
data.derivedDataSet.columnNotExist=Die Spalte '{0}' ist nicht vorhanden.

data.derivedDataSet.UnionQueryTextParseError=Fehler bei Analyse von Union-Abfrage. \"{0}\"

data.derivedDataSet.JoinQueryTextParseError=Fehler bei Analyse von Join-Abfrage. \"{0}\"

data.derivedDataSet.illegalClassInstance=Unzulässige zu initialisierende Klasseninstanz.

data.derivedDataSet.classLoadError=Fehler beim Laden von Klasseninstanz.

data.derivedDataSet.calculatorNotExist=Berechnung nicht definiert oder nicht gefunden.

data.derivedDataSet.invalidQueryText=Ungültiger Abfragetext.

#data mart data source
data.datamart.failLoadDataSet = Fehler beim Laden von Datenobjekt-Datensatz '{0}'.

data.datamart.originalDataSetNotExist = Schrittweise Aktualisierung fehlgeschlagen. Der Originaldatensatz '{0}' ist nicht vorhanden.

data.datamart.incrementalDatasetMetaDataError = Schrittweise Aktualisierung fehlgeschlagen. Die Metadaten des zusätzlichen Datensatzes '{0}' unterscheiden sich von denen des Originaldatensatzes.

data.datamart.collation=Beim Vorgängerprodukt wird Kollation beim Datenobjektindex nicht verwendet, auch wenn dies von Ihnen festgelegt wurde. Wir unterstützen jetzt Kollation, deshalb müssen Sie das Datenobjekt neu erzeugen, wenn im Datensatz Kollation festgelegt ist.

data.filter.pushDownFail= Fehler beim Push Down für Filter, der nur von der ODA-Erweiterung unterstützt wird.

data.query.exprReferInvalidColumn=Abfrage kann nicht ausgeführt werden. Grund: Die Abfrage enthält einen ungültigen Ausdruck, der sich auf eine ungültige Datensatzspalte "{0}" bezieht.

data.query.invalidColumnBinding=Die Spaltenbindung '{0}' ist ungültig.