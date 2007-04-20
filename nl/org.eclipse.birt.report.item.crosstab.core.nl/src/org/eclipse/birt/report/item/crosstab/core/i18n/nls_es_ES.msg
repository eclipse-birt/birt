###########################################################
# Crosstab name conventions
Element.Crosstab=Tabla de referencias cruzadas
Element.CrosstabCell=Celda de tabla de referencias cruzadas
Element.CrosstabAggregationCell=Celda de agregación de tabla de referencias cruzadas
Element.AggregationOnRow=Agregación en fila
Element.AggregationOnColumn=Agregación en columna
Element.AggregationHeaderLocation=Ubicación de encabezado de agregación
Element.MeasureView=Visualización de medidas
Element.PageBreakBefore=Salto de página antes
Element.PageBreakAfter=Salto de página después
Element.SortDirection=Dirección de orden
Element.SortType=Clase de clasificación
Element.LevelView=Visualización de niveles
Element.DimensionView=Visualización de dimensiones
Element.CrosstabView=Visualización de tablas de referencias cruzadas
Element.Cube=Cubo
Element.Dimension=Dimensión
Element.Level=Nivel
Element.Measure=Medida
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

CrosstabQueryHelper.error.invalid.measure=La medida asociada para la tabla de referencias cruzadas no es válida.
CrosstabQueryHelper.error.invalid.dimension=La dimensión asociada para la tabla de referencias cruzadas no es válida.
CrosstabQueryHelper.error.invalid.level=El nivel asociado para la tabla de referencias cruzadas no es válido.

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
CrosstabReportItemExecutor.error.generate.columns=Se ha producido un error al generar las columnas para la tabla de referencias cruzadas.
CrosstabReportItemExecutor.error.prepare.children=Se ha producido un error al preparar el ejecutor secundario para la tabla de referencias cruzadas.
CrosstabReportItemExecutor.error.invalid.cube.result=El resultado de cubo para la tabla de referencias cruzadas no es válido(=nulo).

CrosstabRowExecutor.error.check.force.empty=Se ha producido un error al comprobar estado vacío de fuerza de celda actual.
CrosstabRowExecutor.error.check.edge.start=Se ha producido un error al comprobar estado inicial de extremo de celda actual.
CrosstabRowExecutor.error.retrieve.child.executor=Se ha producido un error al recuperar ejecutor secundario.

CrosstabSubTotalRowExecutor.error.check.edge.start=Se ha producido un error al comprobar estado inicial de extremo de celda actual.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Se ha producido un error al recuperar ejecutor secundario.

TableColumnGenerator.error.process.visibility=Se ha producido un error al procesar visibilidad de columna.

CrosstabReportItemHandle.error.locate.column.cell=Error al ubicar la celda de tabla de referencias cruzadas relevante en las columnas
CrosstabReportItemHandle.error.locate.row.cell=Error al ubicar la celda de tabla de referencias cruzadas relevante en las filas
CrosstabReportItemHandle.error.set.column.width=Error al establecer la anchura de columna de la tabla de referencias cruzadas
CrosstabReportItemHandle.error.set.row.height=Error al establecer la altura de fila de la tabla de referencias cruzadas
