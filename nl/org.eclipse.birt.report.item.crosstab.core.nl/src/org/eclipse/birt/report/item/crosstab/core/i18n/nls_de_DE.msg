###########################################################
# Crosstab name conventions
Element.Crosstab=Kreuztabelle
Element.CrosstabCell=Kreuztabellenzelle
Element.CrosstabAggregationCell=Aggregationszelle der Kreuztabelle
Element.AggregationOnRow=Aggregation in Zeile
Element.AggregationOnColumn=Aggregation in Spalte
Element.AggregationHeaderLocation=Aggregation Header Location
Element.MeasureView=Maßeinheitsansicht
Element.PageBreakBefore=Page Break Before
Element.PageBreakAfter=Page Break After
Element.SortDirection=Sort Direction
Element.SortType=Sort Type
Element.LevelView=Ebenenansicht
Element.DimensionView=Dimensionsansicht
Element.CrosstabView=Kreuztabellenansicht
Element.Cube=Quader
Element.Dimension=Dimension
Element.Level=Level
Element.Measure=Measure
Element.Caption=Beschriftung
Element.CaptionID=Beschriftungsschlüssel
Element.MeasureDirection=Maßeinheitrichtung
Element.PageLayout=Seitenlayout
Element.EmptyCellValue=Leerer Zellenwert

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=Duplikat von Quaderdimensionselement {0} für Dimensionsansicht in Kreuztabelle {1} verwendet.
Error.CrosstabException.DUPLICATE_MEASURE=Duplikat von Quadereinheitselement {0} für Einheitsansicht in Kreuztabelle {1} verwendet.
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=Laden des Kreuztabellenelements aus erweitertem Element fehlgeschlagen.
CrosstabReportItemQuery.error.query.building=Ungültiges Kreuztabellenmodell zum Erstellen der Abfrage.

CrosstabQueryHelper.error.invalid.measure=The associated measure for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.dimension=The associated dimension for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.level=The associated level for crosstab is inavlid.

BaseCrosstabExecutor.error.process.visibility=Beim Verarbeiten der Sichtbarkeit ist ein Fehler aufgetreten.
BaseCrosstabExecutor.error.process.bookmark=Beim Verarbeiten des Lesezeichens ist ein Fehler aufgetreten.

CrosstabCellExecutor.error.restor.data.position=Beim Wiederherstellen der Datenposition ist ein Fehler aufgetreten.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=Beim Generieren des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.
CrosstabGrandTotalRowExecutor.error.check.child.executor=Beim Überprüfen des Status des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabGroupExecutor.error.close.executor=Beim Schließen des ausführenden Programms für die Gruppe ist ein Fehler aufgetreten.
CrosstabGroupExecutor.error.prepare.group=Beim Vorbereiten untergeordneter Gruppen ist ein Fehler aufgetreten.
CrosstabGroupExecutor.error.check.child.executor=Beim Überprüfen des Status des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabHeaderRowExecutor.error.check.force.empty=Beim Überprüfen des Status "Leer erzwingen" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabHeaderRowExecutor.error.check.edge.end=Beim Überprüfen des Status "Randende" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabHeaderRowExecutor.error.retrieve.child.executor=Beim Abrufen des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=Beim Generieren des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=Beim Überprüfen des Status des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabMeasureRowExecutor.error.generate.child.executor=Beim Generieren des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.
CrosstabMeasureRowExecutor.error.check.child.executor=Beim Überprüfen des Status des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabReportItemExecutor.error.crosstab.loading=Laden des Kreuztabellenelements aus erweitertem Element fehlgeschlagen.
CrosstabReportItemExecutor.error.generate.columns=Error happend when generate columns for crosstab.
CrosstabReportItemExecutor.error.prepare.children=Error happend when prepare child executor for crosstab.
CrosstabReportItemExecutor.error.invalid.cube.result=The cube result for crosstab is invalid(=null).

CrosstabRowExecutor.error.check.force.empty=Beim Überprüfen des Status "Leer erzwingen" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabRowExecutor.error.check.edge.start=Beim Überprüfen des Status "Randstart" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabRowExecutor.error.retrieve.child.executor=Beim Abrufen des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabSubTotalRowExecutor.error.check.edge.start=Beim Überprüfen des Status "Randstart" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Beim Abrufen des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

TableColumnGenerator.error.process.visibility=Beim Verarbeiten der Spaltensichtbarkeit ist ein Fehler aufgetreten.

CrosstabReportItemHandle.error.locate.column.cell=Error locating relevant crosstab cell on columns
CrosstabReportItemHandle.error.locate.row.cell=Error locating relevant crosstab cell on rows
CrosstabReportItemHandle.error.set.column.width=Error setting crosstab column width
CrosstabReportItemHandle.error.set.row.height=Error setting crosstab row height
