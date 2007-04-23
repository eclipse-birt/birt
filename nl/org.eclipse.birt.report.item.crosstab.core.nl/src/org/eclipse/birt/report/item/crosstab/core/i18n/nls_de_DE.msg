###########################################################
# Crosstab name conventions
Element.Crosstab=Kreuztabelle
Element.CrosstabCell=Kreuztabellenzelle
Element.CrosstabAggregationCell=Aggregationszelle der Kreuztabelle
Element.AggregationOnRow=Aggregation in Zeile
Element.AggregationOnColumn=Aggregation in Spalte
Element.AggregationHeaderLocation=Position der Aggregat-Kopfzeile
Element.MeasureView=Maßeinheitsansicht
Element.PageBreakBefore=Seitenumbruch vor
Element.PageBreakAfter=Seitenumbruch nach
Element.SortDirection=Sortierreihenfolge
Element.SortType=Sortiertyp
Element.LevelView=Ebenenansicht
Element.DimensionView=Dimensionsansicht
Element.CrosstabView=Kreuztabellenansicht
Element.Cube=Quader
Element.Dimension=Dimension
Element.Level=Ebene
Element.Measure=Maßeinheit
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
CrosstabReportItemQuery.error.register.empty.cell.value=Error on registering empty cell value.

CrosstabQueryHelper.error.invalid.measure=Die verknüpfte Maßeinheit für die Kreuztabelle ist ungültig.
CrosstabQueryHelper.error.invalid.dimension=Die verknüpfte Dimension für die Kreuztabelle ist ungültig.
CrosstabQueryHelper.error.invalid.level=Die verknüpfte Ebene für die Kreuztabelle ist ungültig.

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
CrosstabReportItemExecutor.error.generate.columns=Beim Erstellen der Spalten für die Kreuztabelle ist ein Fehler aufgetreten.
CrosstabReportItemExecutor.error.prepare.children=Beim Vorbereiten des untergeordneten ausführenden Programms für die Kreuztabelle ist ein Fehler aufgetreten.
CrosstabReportItemExecutor.error.invalid.cube.result=Das Quaderergebnis für die Kreuztabelle ist ungültig (=Null).

CrosstabRowExecutor.error.check.force.empty=Beim Überprüfen des Status "Leer erzwingen" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabRowExecutor.error.check.edge.start=Beim Überprüfen des Status "Randstart" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabRowExecutor.error.retrieve.child.executor=Beim Abrufen des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

CrosstabSubTotalRowExecutor.error.check.edge.start=Beim Überprüfen des Status "Randstart" der aktuellen Zelle ist ein Fehler aufgetreten.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Beim Abrufen des untergeordneten ausführenden Programms ist ein Fehler aufgetreten.

TableColumnGenerator.error.process.visibility=Beim Verarbeiten der Spaltensichtbarkeit ist ein Fehler aufgetreten.

CrosstabReportItemHandle.error.locate.column.cell=Fehler beim Suchen der relevanten Kreuztabellenzelle in Spalten
CrosstabReportItemHandle.error.locate.row.cell=Fehler beim Suchen der relevanten Kreuztabellenzelle in Zeilen
CrosstabReportItemHandle.error.set.column.width=Fehler beim Festlegen der Spaltenbreite der Kreuztabelle
CrosstabReportItemHandle.error.set.row.height=Fehler beim Festlegen der Zeilenhöhe der Kreuztabelle
CrosstabReportItemHandle.msg.change.measure.direction=Change Measure Direction
CrosstabReportItemHandle.msg.insert.measure=Insert Measure

MeasureViewHandle.info.aggregation.already.exist=The requested aggregation is already existed.
MeasureViewHandle.msg.add.aggregation=Add Aggregation

LevelViewTask.msg.remove.subtotal=Remove Subtotal
LevelViewTask.msg.add.subtotal=Add Subtotal

DimensionViewTask.msg.remove.level=Remove Level

CrosstabViewTask.msg.remove.dimension=Remove Dimension
CrosstabViewTask.msg.remove.grandtotal=Remove Grand Total
CrosstabViewTask.msg.add.grandtotal=Add Grand Total

CrosstabReportItemTask.msg.add.grandtotal=Add Grand Total
CrosstabReportItemTask.msg.set.aggregate.function=Set Aggregate Function
CrosstabReportItemTask.msg.pivot.dimension=Pivot Dimension
CrosstabReportItemTask.msg.insert.dimension=Insert Dimension
