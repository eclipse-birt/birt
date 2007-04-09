###########################################################
# Crosstab name conventions
Element.Crosstab=Tabla de referencias cruzadas
Element.CrosstabCell=Celda de tabla de referencias cruzadas
Element.CrosstabAggregationCell=Celda de agregación de tabla de referencias cruzadas
Element.AggregationOnRow=Agregación en fila
Element.AggregationOnColumn=Agregación en columna
Element.AggregationHeaderLocation=Aggregation Header Location
Element.MeasureView=Visualización de medidas
Element.PageBreakBefore=Page Break Before
Element.PageBreakAfter=Page Break After
Element.SortDirection=Sort Direction
Element.SortType=Sort Type
Element.LevelView=Visualización de niveles
Element.DimensionView=Visualización de dimensiones
Element.CrosstabView=Visualización de tablas de referencias cruzadas
Element.Cube=Cubo
Element.Dimension=Dimension
Element.Level=Level
Element.Measure=Measure
Element.Caption=Título
Element.CaptionID=Clave de título
Element.MeasureDirection=Dirección de medidas
Element.PageLayout=Diseño de página
Element.EmptyCellValue=Valor de celda vacía

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=El elemento de dimensiones cúbicas {0} dispone de un duplicado aplicado para la visualización de dimensiones en la tabla de referencias cruzadas {1}.
Error.CrosstabException.DUPLICATE_MEASURE=El elemento de medidas cúbicas {0} dispone de un duplicado aplicado para la visualización de medidas en la tabla de referencias cruzadas {1}.
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=Error al cargar elemento de tabla de referencias cruzadas desde elemento ampliado.
CrosstabReportItemQuery.error.query.building=Modelo de tabla de referencias cruzadas no válido para compilar consultas.

CrosstabQueryHelper.error.invalid.measure=The associated measure for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.dimension=The associated dimension for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.level=The associated level for crosstab is inavlid.

BaseCrosstabExecutor.error.process.visibility=Se ha producido un error al procesar visibilidad.
BaseCrosstabExecutor.error.process.bookmark=Se ha producido un error al procesar marcador.

CrosstabCellExecutor.error.restor.data.position=Se ha producido un error al restaurar posición de datos.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=Se ha producido un error al generar ejecutor secundario.
CrosstabGrandTotalRowExecutor.error.check.child.executor=Se ha producido un error al comprobar estado del ejecutor secundario.

CrosstabGroupExecutor.error.close.executor=Se ha producido un error al cerrar ejecutor de grupos.
CrosstabGroupExecutor.error.prepare.group=Se ha producido un error al preparar grupos secundarios.
CrosstabGroupExecutor.error.check.child.executor=Se ha producido un error al comprobar estado del ejecutor secundario.

CrosstabHeaderRowExecutor.error.check.force.empty=Se ha producido un error al comprobar estado vacío de fuerza de celda actual.
CrosstabHeaderRowExecutor.error.check.edge.end=Se ha producido un error al comprobar estado final de extremo de celda actual.
CrosstabHeaderRowExecutor.error.retrieve.child.executor=Se ha producido un error al recuperar ejecutor secundario.

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=Se ha producido un error al generar ejecutor secundario.
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=Se ha producido un error al comprobar estado del ejecutor secundario.

CrosstabMeasureRowExecutor.error.generate.child.executor=Se ha producido un error al generar ejecutor secundario.
CrosstabMeasureRowExecutor.error.check.child.executor=Se ha producido un error al comprobar estado del ejecutor secundario.

CrosstabReportItemExecutor.error.crosstab.loading=Error al cargar elemento de tabla de referencias cruzadas desde elemento ampliado.
CrosstabReportItemExecutor.error.generate.columns=Error happend when generate columns for crosstab.
CrosstabReportItemExecutor.error.prepare.children=Error happend when prepare child executor for crosstab.
CrosstabReportItemExecutor.error.invalid.cube.result=The cube result for crosstab is invalid(=null).

CrosstabRowExecutor.error.check.force.empty=Se ha producido un error al comprobar estado vacío de fuerza de celda actual.
CrosstabRowExecutor.error.check.edge.start=Se ha producido un error al comprobar estado inicial de extremo de celda actual.
CrosstabRowExecutor.error.retrieve.child.executor=Se ha producido un error al recuperar ejecutor secundario.

CrosstabSubTotalRowExecutor.error.check.edge.start=Se ha producido un error al comprobar estado inicial de extremo de celda actual.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Se ha producido un error al recuperar ejecutor secundario.

TableColumnGenerator.error.process.visibility=Se ha producido un error al procesar visibilidad de columna.

CrosstabReportItemHandle.error.locate.column.cell=Error locating relevant crosstab cell on columns
CrosstabReportItemHandle.error.locate.row.cell=Error locating relevant crosstab cell on rows
CrosstabReportItemHandle.error.set.column.width=Error setting crosstab column width
CrosstabReportItemHandle.error.set.row.height=Error setting crosstab row height
