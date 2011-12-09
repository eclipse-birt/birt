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

timeFunction.CURRENT_QUARTER.name = Current Quarter 
timeFunction.CURRENT_QUARTER.description = Calculate the specified metric for current quarter
timeFunction.CURRENT_MONTH.name = Current Month
timeFunction.CURRENT_MONTH.description = Calculate the specified metric for current month
timeFunction.PREVIOUS_MONTH.name = Previous Month
timeFunction.PREVIOUS_MONTH.description = Calculate the specified metric for previous N month
timeFunction.PREVIOUS_WEEK_TO_DATE.name = Semana anterior hasta la fecha
timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calcular la medida especificada para la semana hasta la fecha del mes N anterior
timeFunction.PREVIOUS_MONTH_TO_DATE.name = Previous Month to Date
timeFunction.PREVIOUS_MONTH_TO_DATE.description = Calculate the specified metric for month to date of previous N month
timeFunction.PREVIOUS_QUARTER.name = Previous Quarter 
timeFunction.PREVIOUS_QUARTER.description = Calculate the specified metric for previous N quarter
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = Previous Quarter to Date 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Calculate the specified metric for previous N quarter
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = Previous Year 
timeFunction.PREVIOUS_PREVIOUS_YEAR.description = Calculate the specified metric for previous N year
timeFunction.TRAILING_30_DAYS.name = Trailing 30 Days 
timeFunction.TRAILING_30_DAYS.description = Calculate the specified metric for last 30 days
timeFunction.TRAILING_60_DAYS.name = Trailing 60 Days 
timeFunction.TRAILING_60_DAYS.description = Calculate the specified metric for last 60 days
timeFunction.TRAILING_90_DAYS.name = Trailing 90 Days 
timeFunction.TRAILING_90_DAYS.description = Calculate the specified metric for last 90 days
timeFunction.TRAILING_12_MONTHS.name = Trailing 12 Months 
timeFunction.TRAILING_12_MONTHS.description = Calculate the specified metric for last 12 months
timeFunction.YEAR_TO_DATE.name = Year to Date 
timeFunction.YEAR_TO_DATE.description = Calculate the specified metric for same year
timeFunction.QUARTER_TO_DATE.name = Quarter to Date 
timeFunction.QUARTER_TO_DATE.description = Calculate the specified metric for same quarter
timeFunction.MONTH_TO_DATE.name = Month to Date 
timeFunction.MONTH_TO_DATE.description = Calculate the specified metric for same month
timeFunction.PREVIOUS_YEAR_TO_DATE.name = previous Year to Date 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = Calculate the specified metric for same year in previous N year
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Month to Date Last Year 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Calculate the specified metric for same month in previous N year
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Quarter to Date Last Year 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Calculate the specified metric for same quarter in previous N year
timeFunction.CURRENT_YEAR.name = Current Year 
timeFunction.CURRENT_YEAR.description = Calculate the specified metric for current year
timeFunction.WEEK_TO_DATE.name = Week To Date 
timeFunction.WEEK_TO_DATE.description = Calculate the specified metric for same week
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Current Period from N Periods Ago 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Calculate the specified metric for for current period from N period ago
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Period to Date from N Periods Ago 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Calculate the specified metric for same period from N period ago
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = Trailing N Periods from N Periods Ago 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Calculate the specified metric for trailing N period from N period ago
timeFunction.NEXT_N_PERIODS.name = Next N Periods 
timeFunction.NEXT_N_PERIODS.description = Calculate the specified metric for next N periods

timeFunction.PERIOD1 = The first period argument for time function
timeFunction.PERIOD2 = The second period argument for time function
timeFunction.N1 = N value for first period
timeFunction.N2 = N value for second period

timeFunction.PERIOD1.displayName = The first period
timeFunction.PERIOD2.displayName = The second period
timeFunction.N1.displayName = The fist N
timeFunction.N2.displayName = The second N

timeFunction.toolTip.to=A
