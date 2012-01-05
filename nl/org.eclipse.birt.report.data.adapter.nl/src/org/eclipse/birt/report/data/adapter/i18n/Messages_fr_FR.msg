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

cube.query.error.dimensionMiss=La dimension "{0}" n'existe pas dans le cube.
cube.query.error.levelMiss=Le niveau associé au type Heure "{0}" n'existe pas dans la dimension Heure du cube "{1}".
cube.edge.error.levelMiss=Le niveau associé au type Heure "{0}" est manquant dans la définition de requête.

timeFunction.CURRENT_QUARTER.name = Trimestre actuel 
timeFunction.CURRENT_QUARTER.description = Calculer la caractéristique spécifique pour le trimestre actuel
timeFunction.CURRENT_MONTH.name = Mois actuel
timeFunction.CURRENT_MONTH.description = Calculer la caractéristique spécifique pour le mois actuel
timeFunction.PREVIOUS_MONTH.name = Mois précédent
timeFunction.PREVIOUS_MONTH.description = Calculer la caractéristique spécifique pour le mois N précédent
timeFunction.PREVIOUS_WEEK_TO_DATE.name = Semaine précédente jusqu'à la date
timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calculer la caractéristique spécifique pour la semaine jusqu'à la date du mois N précédent
timeFunction.PREVIOUS_MONTH_TO_DATE.name = Mois précédent jusqu'à la date
timeFunction.PREVIOUS_MONTH_TO_DATE.description = Calculer la caractéristique spécifique pour le mois jusqu'à la date du mois N précédent
timeFunction.PREVIOUS_QUARTER.name = Trimestre précédent 
timeFunction.PREVIOUS_QUARTER.description = Calculer la caractéristique spécifique pour le trimestre N précédent
timeFunction.PREVIOUS_QUARTER_TO_DATE.name = Trimestre précédent jusqu'à la date 
timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Calculer la caractéristique spécifique pour le trimestre N précédent
timeFunction.PREVIOUS_PREVIOUS_YEAR.name = Année précédente 
timeFunction.PREVIOUS_PREVIOUS_YEAR.description = Calculer la caractéristique spécifique pour l'année N précédente
timeFunction.TRAILING_30_DAYS.name = 30 jours de fin 
timeFunction.TRAILING_30_DAYS.description = Calculer la caractéristique spécifique pour les 30 derniers jours
timeFunction.TRAILING_60_DAYS.name = 60 jours de fin 
timeFunction.TRAILING_60_DAYS.description = Calculer la caractéristique spécifique pour les 60 derniers jours
timeFunction.TRAILING_90_DAYS.name = 90 jours de fin 
timeFunction.TRAILING_120_DAYS.name = 120 jours de fin 
timeFunction.TRAILING_90_DAYS.description = Calculer la caractéristique spécifique pour les 90 derniers jours
timeFunction.TRAILING_120_DAYS.description = Calculer la caractéristique spécifique pour les 120 derniers jours
timeFunction.TRAILING_12_MONTHS.name = 12 mois de fin 
timeFunction.TRAILING_12_MONTHS.description = Calculer la caractéristique spécifique pour les 12 derniers mois
timeFunction.YEAR_TO_DATE.name = Année jusqu'à la date 
timeFunction.YEAR_TO_DATE.description = Calculer la caractéristique spécifique pour la même année
timeFunction.QUARTER_TO_DATE.name = Trimestre jusqu'à la date 
timeFunction.QUARTER_TO_DATE.description = Calculer la caractéristique spécifique pour le même trimestre
timeFunction.MONTH_TO_DATE.name = Mois jusqu'à la date 
timeFunction.MONTH_TO_DATE.description = Calculer la caractéristique spécifique pour le même mois
timeFunction.PREVIOUS_YEAR_TO_DATE.name = Année précédente jusqu'à la date 
timeFunction.PREVIOUS_YEAR_TO_DATE.description = Calculer la caractéristique spécifique pour la même année de l'année N précédente
timeFunction.WEEK_TO_DATE_LAST_YEAR.name = Semaine jusqu'à la date - Année dernière 
timeFunction.WEEK_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour la même semaine de l'année N précédente
timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Mois jusqu'à la date - Année dernière 
timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour le même mois de l'année N précédente
timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Trimestre jusqu'à la date - Année dernière 
timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour le même trimestre de l'année N précédente
timeFunction.CURRENT_YEAR.name = Année actuelle 
timeFunction.CURRENT_YEAR.description = Calculer la caractéristique spécifique pour l'année actuelle
timeFunction.WEEK_TO_DATE.name = Semaine jusqu'à la date 
timeFunction.WEEK_TO_DATE.description = Calculer la caractéristique spécifique pour la même semaine
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Période actuelle depuis N périodes 
timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour la période actuelle depuis N périodes
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Période jusqu'à la date depuis N périodes 
timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour la même période depuis N périodes
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N périodes de fin depuis N périodes 
timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour N périodes de fin depuis N périodes
timeFunction.NEXT_N_PERIODS.name = Prochaines N périodes 
timeFunction.NEXT_N_PERIODS.description = Calculer la caractéristique spécifique pour les prochaines N périodes

timeFunction.PERIOD1 = L'argument de première période pour la fonction de temps
timeFunction.PERIOD2 = L'argument de deuxième période pour la fonction de temps
timeFunction.N1 = Valeur N pour la première période
timeFunction.N2 = Valeur N pour la deuxième période

timeFunction.PERIOD1.displayName = La pre&mière période
timeFunction.PERIOD2.displayName = La deuxième pé&riode
timeFunction.N1.displayName = Le prem&ier N
timeFunction.N2.displayName = Le deu&xième N

timeFunction.toolTip.to=A
