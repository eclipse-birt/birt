# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001={0} introuvable dans {0}.

###########################################################

#load data engine factory exception
load.factory.error=Une erreur s'est produite lors du chargement de la génération de l'adaptateur de données.

context.null.error=Le contexte de données ne peut pas être nul.
context.invalidDesigntimeMethod=La méthode ReportContext [{0}] ne peut être utilisée que lors de l'exécution.

adaptor.invalid.mode=La valeur de mode {0} pour le contexte de l'adaptateur de données n'est pas valide.
invalid.cache.option=La valeur de mode {0} pour le contexte de l'adaptateur de données n'est pas valide.

datasource.null.error=Impossible de trouver les sources de données du jeu de données {0}".
datasource.exid.error=ID d'extension manquant dans la définition de la source de données {0}.

datasethandle.null.error=DataSetHandle ne doit pas être nul.
datasethandle.invalidColumnIndex=L'index de colonne n'est pas valide.
adapter.invalid.datetimetype=Le type de données dateheure "{0}" est incorrect.
adapter.invalid.aggregationfunctionname=The aggregation function {0} is invalid.

invalid.datetime.value=La valeur DateHeure "{0}" est incorrecte.
missing.join.condition=La condition de jointure entre le jeu de données principal "{0}" et la dimension "{1}" du cube "{2}" est absente.
exception.error=Une exception s'est produite au cours du traitement. Pour plus de détails, consultez le message suivant :
invalid.level.expression=Le nom de niveau "{0}" est introuvable.
invalid.measure.expression=L'expression de mesure"{0}" ne peut pas être nulle.
invalid.data.type=Type de données incorrect : {0}. 

cube.error.createhierarchy=Une erreur s''est produite lors de la création de la hiérarchie : {0}.
cube.error.createdimension=Une erreur s''est produite lors de la création de la dimension : {0}.
cube.error.createmeasure=Une erreur s'est produite lors de la création de la mesure.
cube.error.datasetmiss=Le cube ne peut pas être créé car le jeu de données correspondant est manquant.
cube.jointColumnNotInLevels=La colonne jointe "{0}" n'est pas définie en tant que champ clé pour aucun niveau dans la dimension "{1}".
cube.unsupportedAggregationInBinding=Fonction TopN/BottomN dans la liaison "{0}" non prise en charge dans xTab.

cube.query.error.dimensionMiss=Dimension "{0}" does not exist in cube.
cube.query.error.levelMiss=Level with time type "{0}" does not exist in cube time dimension "{1}".
cube.edge.error.levelMiss=Level with time type "{0}" is missing in query definition.

timeFunction.CURRENT_QUARTER.name = Current Quarter 
timeFunction.CURRENT_QUARTER.description = Calculate the specified metric for current quarter
timeFunction.CURRENT_MONTH.name = Current Month
timeFunction.CURRENT_MONTH.description = Calculate the specified metric for current month
timeFunction.PREVIOUS_MONTH.name = Previous Month
timeFunction.PREVIOUS_MONTH.description = Calculate the specified metric for previous N month
timeFunction.PREVIOUS_WEEK_TO_DATE.name = Previous Week to Date
timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calculate the specified metric for week to date of previous N month
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
 