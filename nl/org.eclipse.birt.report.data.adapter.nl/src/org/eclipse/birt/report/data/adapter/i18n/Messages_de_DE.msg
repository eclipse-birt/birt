# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} in {1} nicht gefunden.

###########################################################

#load data engine factory exception
load.factory.error=Beim Laden der Factory des Datenadapters ist ein Fehler aufgetreten.

context.null.error=Datenkontext darf nicht Null sein.
context.invalidDesigntimeMethod=Reportkontextmethode [{0}] kann nur in Laufzeit verwendet werden.

adaptor.invalid.mode=Der Modus-Wert {0} des Datenadapter-Kontexts ist ungültig.
invalid.cache.option=Der Optionswert {0} des Datenadapter-Kontexts ist ungültig.

datasource.null.error=Datenquelle von Datensatz '{0}' nicht gefunden.
datasource.exid.error=Fehlende Erweiterungs-ID in Datenquellendefinition: {0}.

datasethandle.null.error=Datensatz-Handle darf nicht Null sein.
datasethandle.invalidColumnIndex=Ungültiger Spaltenindex.
adapter.invalid.datetimetype=Der Typ für Datum/Zeit "{0}" ist ungültig.
adapter.invalid.aggregationfunctionname=The aggregation function {0} is invalid.

invalid.datetime.value=Datums-/Uhrzeitwert "{0}" ist ungültig.
missing.join.condition=Die Knotenpunktbedingung zwischen dem primären Datensatz '{0}' und der Dimension '{1}' im Quader '{2}' fehlt.
exception.error=Ein Ausnahmefehler ist während der Verarbeitung aufgetreten. Nähere Informationen finden Sie in der folgenden Meldung:
invalid.level.expression=Der Ebenenname "{0}" wurde nicht gefunden.
invalid.measure.expression=Der Ausdruck für die Maßeinheit "{0}" darf nicht Null sein.
invalid.data.type=Ungültiger Datentyp: {0}. 

cube.error.createhierarchy=Fehler beim Erstellen von Hierarchie:{0}.
cube.error.createdimension=Fehler beim Erstellen von Dimension:{0}.
cube.error.createmeasure=Fehler beim Erstellen von Maßeinheit.
cube.error.datasetmiss=Der Quader kann nicht erstellt werden, da der zugehörige Datensatz fehlt.
cube.jointColumnNotInLevels=Verknüpfungsspalte '{0}' ist nicht als Schlüsselfeld einer Ebene in Dimension '{1}' definiert.
cube.unsupportedAggregationInBinding=Funktion 'TopN/BottomN' in Bindung '{0}' wird in xTab nicht unterstützt.

cube.query.error.dimensionMiss=Dimension "{0}" ist nicht im Quader vorhanden.
cube.query.error.levelMiss=Ebene mit Uhrzeitformat "{0}" ist im Quader für die zeitliche Dimension "{1}" nicht vorhanden.
cube.edge.error.levelMiss=Ebene mit Uhrzeitformat "{0}" fehlt in Abfragedefinition.

timeFunction.CURRENT_QUARTER.name = Aktuelles Quartal 
timeFunction.CURRENT_QUARTER.description = Angegebene Metrik für das aktuelle Quartal berechnen
timeFunction.CURRENT_MONTH.name = Aktueller Monat
timeFunction.CURRENT_MONTH.description = Angegebene Metrik für den aktuellen Monat berechnen
timeFunction.PREVIOUS_MONTH.name = Vorheriger Monat
timeFunction.PREVIOUS_MONTH.description = Angegebene Metrik für den vorherigen N-Monat berechnen
timeFunction.PREVIOUS_WEEK_TO_DATE.name = Vorangegangene Woche bis zu diesem Tag
timeFunction.PREVIOUS_WEEK_TO_DATE.description = Angegebene Metrik für Woche bis Datum des vorherigen N-Monats berechnen
timeFunction.PREVIOUS_MONTH_TO_DATE.name = Vorheriger Monat bis Datum
timeFunction.PREVIOUS_MONTH_TO_DATE.description = Angegebene Metrik für Monat bis Datum des vorherigen N-Monats berechnen
timeFunction.PREVIOUS_QUARTER.name = Vorheriges Quartal 
timeFunction.PREVIOUS_QUARTER.description = Angegebene Metrik für das vorherige N-Quartal berechnen
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = Vorheriges Quartal bis Datum 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Angegebene Metrik für das vorherige N-Quartal berechnen
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = Vorheriges Jahr 
timeFunction.PREVIOUS_PREVIOUS_YEAR.description = Angegebene Metrik für das vorherige N-Jahr berechnen
timeFunction.TRAILING_30_DAYS.name = Nachgestellte 30 Tage 
timeFunction.TRAILING_30_DAYS.description = Angegebene Metrik für die letzten 30 Tage berechnen
timeFunction.TRAILING_60_DAYS.name = Nachgestellte 60 Tage 
timeFunction.TRAILING_60_DAYS.description = Angegebene Metrik für die letzten 60 Tage berechnen
timeFunction.TRAILING_90_DAYS.name = Nachgestellte 90 Tage 
timeFunction.TRAILING_120_DAYS.name = Nachgestellte 120 Tage 
timeFunction.TRAILING_90_DAYS.description = Angegebene Metrik für die letzten 90 Tage berechnen
timeFunction.TRAILING_120_DAYS.description = Angegebene Metrik für die letzten 120 Tage berechnen
timeFunction.TRAILING_12_MONTHS.name = Nachgestellte 12 Monate 
timeFunction.TRAILING_12_MONTHS.description = Angegebene Metrik für die letzten 12 Monate berechnen
timeFunction.YEAR_TO_DATE.name = Jahr bis Datum 
timeFunction.YEAR_TO_DATE.description = Angegebene Metrik für das gleiche Jahr berechnen
timeFunction.QUARTER_TO_DATE.name = Quartal bis Datum 
timeFunction.QUARTER_TO_DATE.description = Angegebene Metrik für das gleiche Quartal berechnen
timeFunction.MONTH_TO_DATE.name = Monat bis Datum 
timeFunction.MONTH_TO_DATE.description = Angegebene Metrik für den gleichen Monat berechnen
timeFunction.PREVIOUS_YEAR_TO_DATE.name = Vorheriges Jahr bis Datum 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = Angegebene Metrik für das gleiche Jahr im vorherigen N-Jahr berechnen
timeFunction.WEEK_TO_DATE_LAST_YEAR.name = Woche bis Datum vorheriges Jahr 
timeFunction.WEEK_TO_DATE_LAST_YEAR.description = Angegebene Metrik für die gleiche Woche im vorherigen N-Jahr berechnen
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Monat bis Datum vorheriges Jahr 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Angegebene Metrik für den gleichen Monat im vorherigen N-Jahr berechnen
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Quartal bis Datum vorheriges Jahr 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Angegebene Metrik für das gleiche Quartal im vorherigen N-Jahr berechnen
timeFunction.CURRENT_YEAR.name = Aktuelles Jahr 
timeFunction.CURRENT_YEAR.description = Angegebene Metrik für das aktuelle Jahr berechnen
timeFunction.WEEK_TO_DATE.name = Woche bis Datum 
timeFunction.WEEK_TO_DATE.description = Angegebene Metrik für die gleiche Woche berechnen
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Aktuelles Zeitintervall ab vor N-Zeitintervallen 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Angegebene Metrik für das aktuelle Zeitintervall ab vor N-Zeitintervall berechnen
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Zeitintervall bis Datum ab vor N-Zeitintervallen 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Angegebene Metrik für das gleiche Intervall ab vor N-Zeitintervall berechnen
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = Nachgestellte N-Zeitintervalle ab vor N-Zeitintervallen 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Angegebene Metrik für nachgestelltes N-Zeitintervall ab vor N-Zeitintervall berechnen
timeFunction.NEXT_N_PERIODS.name = Nächste N Zeitintervalle 
timeFunction.NEXT_N_PERIODS.description = Angegebene Metrik für die nächsten N-Zeitintervalle berechnen

timeFunction.PERIOD1 = Das erste Zeitintervall-Argument für die Zeitfunktion
timeFunction.PERIOD2 = Das zweite Zeitintervall-Argument für die Zeitfunktion
timeFunction.N1 = N-Wert für das erste Zeitintervall
timeFunction.N2 = N-Wert für das zweite Zeitintervall

timeFunction.PERIOD1.displayName = Erstes Zeitinter&vall
timeFunction.PERIOD2.displayName = Zweites Zeitinte&rvall
timeFunction.N1.displayName = Erst&es N
timeFunction.N2.displayName = Z&weites N
 
timeFunction.toolTip.to=Bis
