# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001=El valor {0} no se encuentra en {0}.

###########################################################

#load data engine factory exception
load.factory.error=Hay un error al cargar la fábrica del adaptador de datos.

context.null.error=El contexto de datos no puede ser nulo.
context.invalidDesigntimeMethod=El método ReportContext [{0}] sólo puede utilizarse en tiempo de ejecución.

adaptor.invalid.mode=El valor de modo {0} del contexto del adaptador de datos no es válido.
invalid.cache.option=El valor de opción {0} del contexto del adaptador de datos no es válido.

datasource.null.error=Error al buscar la fuente de datos del conjunto de datos "{0}".
datasource.exid.error=Falta el ID de extensión en la definición de la fuente de datos {0}.

datasethandle.null.error=DataSetHandle no puede ser nulo.
datasethandle.invalidColumnIndex=El índice de columna no es válido.
adapter.invalid.datetimetype=El tipo de fecha/hora "{0}" no es válido.
adapter.invalid.aggregationfunctionname=The aggregation function {0} is invalid.

invalid.datetime.value=El valor Fecha/Hora "{0}"no es válido.
missing.join.condition=Falta la condición de combinación entre el conjunto de datos primario "{0}" y la dimensión "{1}" en el cubo "{2}".
exception.error=Se ha producido una excepción durante el procesamiento. Consulte el siguiente mensaje para obtener más información:
invalid.level.expression=No se ha encontrado el nombre de nivel "{0}".
invalid.measure.expression=La expresión de medida para la medida "{0}" no puede ser nula.
invalid.data.type=Tipo de datos no válido: {0}. 

cube.error.createhierarchy=Se produce un error al crear la jerarquía:{0}.
cube.error.createdimension=Se produce un error al crear la dimensión:{0}.
cube.error.createmeasure=Se produce un error al crear la medida.
cube.error.datasetmiss=El cubo no se puede crear porque falta el conjunto de datos relacionado.
cube.jointColumnNotInLevels=La columna de combinación "{0}" no está definida como campo clave de ningún nivel en la dimensión "{1}".
cube.unsupportedAggregationInBinding=La función TopN/BottomN al enlazar "{0}" no se admite en xTab.

cube.query.error.dimensionMiss="{0}" de dimensión no existe en el cubo.
cube.query.error.levelMiss=El nivel con "{0}" de tipo tiempo no existe en "{1}" de dimensión con tiempo en cubo.
cube.edge.error.levelMiss=Falta el nivel con "{0}" de tipo tiempo en la definición de consulta.

timeFunction.CURRENT_QUARTER.name = Trimestre actual 
timeFunction.CURRENT_QUARTER.description = Calcular la medida especificada para el trimestre actual
timeFunction.CURRENT_MONTH.name = Mes actual
timeFunction.CURRENT_MONTH.description = Calcular la medida especificada para el mes actual
timeFunction.PREVIOUS_MONTH.name = Mes anterior
timeFunction.PREVIOUS_MONTH.description = Calcular la medida especificada para el mes N anterior
timeFunction.PREVIOUS_WEEK_TO_DATE.name = Semana anterior hasta la fecha
timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calcular la medida especificada para la semana hasta la fecha del mes N anterior
timeFunction.PREVIOUS_MONTH_TO_DATE.name = Mes anterior hasta la fecha
timeFunction.PREVIOUS_MONTH_TO_DATE.description = Calcular la medida especificada para el mes hasta la fecha del mes N anterior
timeFunction.PREVIOUS_QUARTER.name = Trimestre anterior 
timeFunction.PREVIOUS_QUARTER.description = Calcular la medida especificada para el trimestre N anterior
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = Trimestre anterior hasta la fecha 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Calcular la medida especificada para el trimestre N anterior
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = Año anterior 
timeFunction.PREVIOUS_PREVIOUS_YEAR.description = Calcular la medida especificada para el año N anterior
timeFunction.TRAILING_30_DAYS.name = 30 días finales 
timeFunction.TRAILING_30_DAYS.description = Calcular la medida especificada para los últimos 30 días
timeFunction.TRAILING_60_DAYS.name = 60 días finales 
timeFunction.TRAILING_60_DAYS.description = Calcular la medida especificada para los últimos 60 días
timeFunction.TRAILING_90_DAYS.name = 90 días finales 
timeFunction.TRAILING_120_DAYS.name = Trailing 120 Days 
timeFunction.TRAILING_90_DAYS.description = Calcular la medida especificada para los últimos 90 días
timeFunction.TRAILING_120_DAYS.description = Calculate the specified metric for last 120 days
timeFunction.TRAILING_12_MONTHS.name = 12 meses finales 
timeFunction.TRAILING_12_MONTHS.description = Calcular la medida especificada para los últimos 12 meses
timeFunction.YEAR_TO_DATE.name = Año hasta la fecha 
timeFunction.YEAR_TO_DATE.description = Calcular la medida especificada para el mismo año
timeFunction.QUARTER_TO_DATE.name = Trimestre hasta la fecha 
timeFunction.QUARTER_TO_DATE.description = Calcular la medida especificada para el mismo trimestre
timeFunction.MONTH_TO_DATE.name = Mes hasta la fecha 
timeFunction.MONTH_TO_DATE.description = Calcular la medida especificada para el mismo mes
timeFunction.PREVIOUS_YEAR_TO_DATE.name = Año anterior hasta la fecha 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = Calcular la medida especificada para el mismo año en el año N anterior
timeFunction.WEEK_TO_DATE_LAST_YEAR.name = Week to Date Last Year 
timeFunction.WEEK_TO_DATE_LAST_YEAR.description = Calculate the specified metric for same week in previous N year
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Mes hasta la fecha del año pasado 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Calcular la medida especificada para el mismo mes en el año N anterior
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Trimestre hasta la fecha del año pasado 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Calcular la medida especificada para el mismo trimestre en el año N anterior
timeFunction.CURRENT_YEAR.name = Año actual 
timeFunction.CURRENT_YEAR.description = Calcular la medida especificada para el año actual
timeFunction.WEEK_TO_DATE.name = Semana hasta la fecha 
timeFunction.WEEK_TO_DATE.description = Calcular la medida especificada para la misma semana
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Período actual desde hace N períodos 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Calcular la medida especificada para el período actual desde el período N
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Período hasta la fecha desde hace N períodos 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Calcular la medida específica para el mismo período desde hace N períodos
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N períodos finales desde hace N períodos 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Calcular la medida especificada para N períodos finales desde hace N períodos
timeFunction.NEXT_N_PERIODS.name = Siguientes N períodos 
timeFunction.NEXT_N_PERIODS.description = Calcular la medida especificada para los siguiente N períodos

timeFunction.PERIOD1 = El primer argumento de período para la función de tiempo
timeFunction.PERIOD2 = El segundo argumento de período para la función de tiempo
timeFunction.N1 = Valor N para el primer período
timeFunction.N2 = Valor N para el segundo período

timeFunction.PERIOD1.displayName = El primer período
timeFunction.PERIOD2.displayName = El segundo período
timeFunction.N1.displayName = El primer N
timeFunction.N2.displayName = El segundo N

timeFunction.toolTip.to=A
