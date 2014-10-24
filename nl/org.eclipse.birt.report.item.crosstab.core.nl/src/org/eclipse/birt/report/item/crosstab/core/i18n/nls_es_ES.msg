###########################################################
# Crosstab name conventions
Element.Crosstab=Tabla de referencias cruzadas

Element.CrosstabCell=Celda de tabla de referencias cruzadas

Element.CrosstabAggregationCell=Celda de agregación de tabla de referencias cruzadas

Element.AggregationOnRow=Agregación en fila

Element.AggregationOnColumn=Agregación en columna

Element.AggregationHeaderLocation=Ubicación de encabezado de agregación

Element.GrandTotalLocation=Ubicación del total general

Element.MeasureView=Visualización de medidas

Element.ComputedMeasureView=Visualización de medidas procesadas

Element.PageBreakBefore=Salto de página antes

Element.PageBreakAfter=Salto de página después

Element.PageBreakInside=Salto de página interior

Element.PageBreakInterval=Intervalo de salto de página

Element.SortDirection=Dirección de orden

Element.SortType=Tipo de orden

Element.LevelView=Visualización de niveles

Element.DisplayField=Visualizar campo

Element.DimensionView=Visualización de dimensiones

Element.CrosstabView=Visualización de tablas de referencias cruzadas

Element.Cube=Cube
Element.Dimension=Dimensión

Element.Level=Nivel

Element.Measure=Medida

Element.Caption=Título

Element.CaptionID=Clave de título

Element.Summary=Resumen

Element.MeasureDirection=Dirección de medidas

Element.PageLayout=Diseño de página

Element.EmptyCellValue=Valor de celda vacía

Element.RepeatRowHeader=Repetir encabezado de fila

Element.RepeatColumnHeader=Repetir encabezado de columna

Element.Mirrored=Reflejado

Element.MirrorStartingLevel=Nivel de inicio reflejado

Element.HideMeasureHeader=Encabezado Ocultar medidas

Element.SpanOverOnColumn=Extender en columna

Element.SpanOverOnRow=Extender en fila

Element.Direction.Horizontal=Horizontal

Element.Direction.Vertical=Vertical

Element.choice.before=Antes

Element.choice.after=Después

Element.RowPageBreakInterval=Intervalo de salto de página en fila

Element.ColumnPageBreakInterval=Intervalo de salto de página en columna

Element.TagType=Tipo de etiqueta

Method.onPrepare=Al preparar

Method.onCreate=Al crear

Method.onRender=Al procesar

Method.onPageBreak=En el salto de página

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=El elemento de dimensiones cúbicas ''{0}'' no se puede aplicar a la tabla de referencias cruzadas con nombre duplicado.

Error.CrosstabException.DUPLICATE_MEASURE=El elemento de medidas cúbicas ''{0}'' no se puede aplicar a la tabla de referencias cruzadas con nombre duplicado.

Error.CrosstabException.DUPLICATE_LEVEL=El elemento de nivel cúbico ''{0}'' no se puede aplicar a la tabla de referencias cruzadas con nombre duplicado.

Error.CrosstabException.DIMENSION_NOT_FOUND=No se encuentra la visualización de dimensiones ''{0}'' en la tabla de referencias cruzadas.

Error.CrosstabException.MEASURE_NOT_FOUND=No se encuentra la visualización de medidas ''{0}'' en la tabla de referencias cruzadas.

Error.CrosstabException.LEVEL_NOT_FOUND=No se encuentra la visualización de nivel ''{0}'' en la tabla de referencias cruzadas.

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=Error al cargar elemento de tabla de referencias cruzadas desde elemento ampliado.

CrosstabReportItemQuery.error.query.building=Modelo de tabla de referencias cruzadas no válido para compilar consultas.

CrosstabReportItemQuery.error.register.empty.cell.value=Error al registrar un valor de celda vacía.

CrosstabQueryHelper.error.invalid.measure=La tabla de referencias cruzadas contiene una definición de medida nula o la medida específica "{0}" no existe en el cubo asociado.

CrosstabQueryHelper.error.invalid.dimension.row=El área de fila de la tabla de referencias cruzadas contiene una definición de dimensión nula o la dimensión específica "{0}" no existe en el cubo asociado.

CrosstabQueryHelper.error.invalid.level.row=El área de fila de la tabla de referencias cruzadas contiene una definición de nivel nula o el nivel específico "{0}" no existe en el cubo asociado.

CrosstabQueryHelper.error.invalid.dimension.column=El área de columna de la tabla de referencias cruzadas contiene una definición de dimensión nula o la dimensión específica "{0}" no existe en el cubo asociado.

CrosstabQueryHelper.error.invalid.level.column=El área de columna de la tabla de referencias cruzadas contiene una definición de nivel nula o el nivel específico "{0}" no existe en el cubo asociado.

BaseCrosstabExecutor.error.process.visibility=Se ha producido un error al procesar visibilidad.

BaseCrosstabExecutor.error.process.bookmark=Se ha producido un error al procesar los encabezados.

BaseCrosstabExecutor.error.process.headers=Se ha producido un error al procesar los encabezados.

BaseCrosstabExecutor.error.process.row.height=Se ha producido un error al procesar la altura de fila.

CrosstabCellExecutor.error.restor.data.position=Se ha producido un error al restaurar la posición de los datos.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=Se ha producido un error al generar ejecutor secundario.

CrosstabGrandTotalRowExecutor.error.check.child.executor=Se ha producido un error al comprobar estado del ejecutor secundario.

CrosstabHeaderExecutor.error.reset.row.position=Se ha producido un error al restablecer la posición del cursor en el extremo de fila.

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

TableColumnGenerator.error.process.column.width=Se ha producido un error al procesar la anchura de columna.

CrosstabReportItemHandle.error.locate.column.cell=Error al ubicar la celda de tabla de referencias cruzadas relevante en las columnas

CrosstabReportItemHandle.error.locate.row.cell=Error al ubicar la celda de tabla de referencias cruzadas relevante en las filas

CrosstabReportItemHandle.error.set.column.width=Error al establecer la anchura de columna de la tabla de referencias cruzadas

CrosstabReportItemHandle.error.set.row.height=Error al establecer la altura de fila de la tabla de referencias cruzadas

CrosstabReportItemHandle.msg.change.measure.direction=Cambiar dirección de medidas

CrosstabReportItemHandle.msg.insert.measure=Insertar medida

CrosstabReportItemHandle.exception.name.blank=Es necesario el nombre para ComputedMeasure.

CrosstabReportItemHandle.exception.name.duplicate=El nombre de medida especificado ya está en uso y no se puede duplicar.

MeasureViewHandle.info.aggregation.already.exist=La agregación solicitada ya existe.

MeasureViewHandle.msg.add.aggregation=Añadir agregación

LevelViewTask.msg.remove.subtotal=Eliminar subtotal

LevelViewTask.msg.add.subtotal=Añadir subtotal

DimensionViewTask.msg.remove.level=Eliminar nivel

DimensionViewTask.msg.insert.level=Insertar nivel

MeasureViewTask.msg.remove.header=Quitar encabezado de medidas

MeasureViewTask.msg.add.header=Añadir encabezado de medidas

CrosstabViewTask.msg.add.dimension=Añadir dimensión

CrosstabViewTask.msg.remove.dimension=Eliminar dimensión

CrosstabViewTask.msg.remove.grandtotal=Eliminar total general

CrosstabViewTask.msg.add.grandtotal=Añadir total general

CrosstabReportItemTask.msg.add.grandtotal=Añadir total general

CrosstabReportItemTask.msg.set.aggregate.function=Establecer función de agregación

CrosstabReportItemTask.msg.pivot.dimension=Dimensión dinámica

CrosstabReportItemTask.msg.insert.dimension=Insertar dimensión

CrosstabReportItemTask.msg.set.mirroredStartingLevel=Establecer nivel de inicio reflejado

CrosstabReportItemTask.msg.pivot.crosstab=Tabla de referencias cruzadas del eje

CrosstabReportItemHandle.Error.HasNoCube=La tabla de referencias cruzadas debe poder acceder a un cubo.

CrosstabReportItemHandle.log.validate=validando(...)

CrosstabScriptHandler.error.javascript={0} en la línea {1} de JavaScript:\r\n{2}\r\n

CrosstabScriptHandler.info.try.register.crosstab.javascript.content=Intentando registrar el contenido JavaScript de la(s) tabla(s) de referencias cruzadas...

CrosstabScriptHandler.info.crosstab.javascript.content.registered=El contenido JavaScript de la(s) tabla(s) de referencias cruzadas se ha registrado correctamente.

CrosstabScriptHandler.info.try.load.crosstab.java.handler=Intentando cargar el controlador de evento Java de la(s) tabla(s) de referencias cruzadas...

CrosstabScriptHandler.info.crosstab.java.handler.loaded=El controlador de evento Java de la(s) tabla(s) de referencias cruzadas [{0}] se ha cargado correctamente.

CrosstabScriptHandler.info.invalid.crosstab.java.handler=El controlador de evento Java de la(s) tabla(s) de referencias cruzadas [{0}] no es una implementación compatible.
