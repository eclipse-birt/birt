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

datasource.cassandra.error=Impossible de trouver les JARS de la bibliothèque client Hector pour prendre en charge la source de données Cassandra nécessitant un script.

datasethandle.null.error=DataSetHandle ne doit pas être nul.

datasethandle.invalidColumnIndex=L'index de colonne n'est pas valide.

adapter.invalid.datetimetype=Le type de données dateheure "{0}" est incorrect.

adapter.invalid.aggregationfunctionname=La fonction d'agrégation {0} est incorrecte.

invalid.datetime.value=La valeur DateHeure "{0}" est incorrecte.

missing.join.condition=La condition de jointure entre le jeu de données principal "{0}" et la dimension "{1}" du cube "{2}" est absente.

exception.error=Une exception s'est produite au cours du traitement. Pour plus de détails, consultez le message suivant :

invalid.level.expression=Le nom de niveau "{0}" est introuvable.

invalid.measure.expression=L'expression de mesure"{0}" ne peut pas être nulle.

invalid.data.type=Type de données incorrect : {0}.

invalid.binding.expression=Expression {0} non valide.

invalid.computedColumn.expression=Colonne de calcul "{0}" incorrecte, qui fait référence à une colonne de jeu de données non valide "{1}".

cube.error.createhierarchy=Une erreur s''est produite lors de la création de la hiérarchie : {0}.

cube.error.createdimension=Une erreur s''est produite lors de la création de la dimension : {0}.

cube.error.createmeasure=Une erreur s'est produite lors de la création de la mesure.

cube.error.datasetmiss=Le cube ne peut pas être créé car le jeu de données correspondant est manquant.

cube.jointColumnNotInLevels=La colonne jointe "{0}" n'est pas définie en tant que champ clé pour aucun niveau dans la dimension "{1}".

cube.unsupportedAggregationInBinding=Fonction TopN/BottomN dans la liaison "{0}" non prise en charge dans xTab.

cube.error.aggregationFilter.referCalculatedMeasure=Agrégation non valide "{0}". L'expression de filtre a référencé une mesure calculée "{1}".

cube.query.error.dimensionMiss=La dimension "{0}" n'existe pas dans le cube.

cube.query.error.levelMiss=Le niveau associé au type Heure "{0}" n'existe pas dans la dimension Heure du cube "{1}".

cube.edge.error.levelMiss=Le niveau associé au type Heure "{0}" est manquant dans la définition de requête.

cube.derivedMeasure.invalid.reference=Référence de mesure non valide. La mesure dérivée "{0}" fait référence à une mesure non valide : "{1}".

cube.derivedMeasure.resolve.error=Echec de la résolution de la mesure dérivée "{0}".

cube.derivedMeasure.recursive.ref=Echec de la résolution de la mesure dérivée "{0}" car une référence circulaire a été détectée dans : "{1}".

timeFunction.CURRENT_QUARTER.name = Trimestre actuel

timeFunction.CURRENT_QUARTER.description = Calculer la caractéristique spécifique pour le trimestre actuel

timeFunction.CURRENT_MONTH.name = Mois actuel

timeFunction.CURRENT_MONTH.description = Calculer la caractéristique spécifique pour le mois actuel

timeFunction.PREVIOUS_MONTH.name = N mois précédent(s)

timeFunction.PREVIOUS_MONTH.description = Calculer la caractéristique spécifique pour le mois N précédent

timeFunction.PREVIOUS_WEEK_TO_DATE.name = N semaine(s) précédente(s) jusqu'à la date

timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calculer la caractéristique spécifique pour la semaine jusqu'à la date de la semaine N précédente

timeFunction.PREVIOUS_MONTH_TO_DATE.name = N mois précédent(s) jusqu'à la date

timeFunction.PREVIOUS_MONTH_TO_DATE.description = Calculer la caractéristique spécifique pour le mois jusqu'à la date du mois N précédent

timeFunction.PREVIOUS_QUARTER.name = N trimestre(s) précédent(s)

timeFunction.PREVIOUS_QUARTER.description = Calculer la caractéristique spécifique pour le trimestre N précédent

timeFunction.PREVIOUS_QUARTER_TO_DATE.name = N trimestre(s) précédent(s) jusqu'à la date

timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Calculer la caractéristique spécifique pour le trimestre N précédent

timeFunction.PREVIOUS_PREVIOUS_YEAR.name = N année(s) précédente(s)

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

timeFunction.YEAR_TO_DATE.name = Année jusqu'à la date (YTD)

timeFunction.YEAR_TO_DATE.description = Calculer la caractéristique spécifique pour la même année

timeFunction.QUARTER_TO_DATE.name = Trimestre jusqu'à la date (QTD)

timeFunction.QUARTER_TO_DATE.description = Calculer la caractéristique spécifique pour le même trimestre

timeFunction.MONTH_TO_DATE.name = Mois jusqu'à la date (MTD)

timeFunction.MONTH_TO_DATE.description = Calculer la caractéristique spécifique pour le même mois

timeFunction.PREVIOUS_YEAR_TO_DATE.name = N année(s) précédente(s) jusqu'à la date

timeFunction.PREVIOUS_YEAR_TO_DATE.description = Calculer la caractéristique spécifique pour la même année de l'année N précédente

timeFunction.WEEK_TO_DATE_LAST_YEAR.name = Semaine jusqu'à la date - Année dernière

timeFunction.WEEK_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour la même semaine de l'année N précédente

timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Mois jusqu'à la date - Année dernière

timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour le même mois de l'année N précédente

timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Trimestre jusqu'à la date - Année dernière

timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour le même trimestre de l'année N précédente

timeFunction.CURRENT_YEAR.name = Année actuelle

timeFunction.CURRENT_YEAR.description = Calculer la caractéristique spécifique pour l'année actuelle

timeFunction.WEEK_TO_DATE.name = Semaine jusqu'à la date (WTD)

timeFunction.WEEK_TO_DATE.description = Calculer la caractéristique spécifique pour la même semaine

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Période actuelle depuis N périodes

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour la période actuelle depuis N périodes

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Période jusqu'à la date depuis N périodes

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour la même période depuis N périodes

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N périodes de fin depuis N périodes

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour N périodes de fin depuis N périodes

timeFunction.TRAILING_N_MONTHS.name = N mois de fin

timeFunction.TRAILING_N_MONTHS.description = Calculer la caractéristique spécifique pour les N derniers mois

timeFunction.TRAILING_N_DAYS.name = N jours de fin

timeFunction.TRAILING_N_DAYS.description = Calculer la caractéristique spécifique pour les N jours de fin

timeFunction.NEXT_N_PERIODS.name = Prochaines N périodes

timeFunction.NEXT_N_PERIODS.description = Calculer la caractéristique spécifique pour les prochaines N périodes

timeFunction.PERIOD1 = L'argument de première période pour la fonction de temps

timeFunction.PERIOD2 = L'argument de deuxième période pour la fonction de temps

timeFunction.N1 = Valeur N pour la première période

timeFunction.N2 = Valeur N pour la deuxième période

timeFunction.PERIOD1.displayName = La pre&mière période

timeFunction.PERIOD2.displayName = La deuxième pé&riode

timeFunction.N1.displayName = Nombre de périodes écoulées depuis

timeFunction.N2.displayName = &De

timeFunction.N1.next.displayName = &Suivant

timeFunction.N2.Ago.displayName=Il &y a

timeFunction.N1.current.displayName=&Actuel

timeFunction.N1.period.displayName= &Période

timeFunction.trailing.N1.period.displayName=De &fin

timeFunction.trailing.N1.Month.displayName= &Nombre de mois de fin

timeFunction.N1.Year.displayName = &Nombre d'années écoulées depuis

timeFunction.N2.Year.displayName = &Nombre d'années écoulées depuis

timeFunction.N1.Year = Valeur N pour le nombre d'années écoulées depuis

timeFunction.N2.Year = Valeur N pour le nombre d'années écoulées depuis

timeFunction.N1.Month.displayName = &Nombre de mois écoulés depuis

timeFunction.N2.Month.displayName = &Nombre de mois écoulés depuis

timeFunction.N1.Month = Valeur N pour le nombre de mois écoulés depuis

timeFunction.N2.Month = Valeur N pour le nombre de mois écoulés depuis

timeFunction.N1.Quarter.displayName = &Nombre de trimestres écoulés depuis

timeFunction.N2.Quarter.displayName = &Nombre de trimestres écoulés depuis

timeFunction.N1.Quarter = Valeur N pour le nombre de trimestres écoulés depuis

timeFunction.N2.Quarter = Valeur N pour le nombre de trimestres écoulés depuis

timeFunction.N1.Week.displayName = &Nombre de semaines écoulées depuis

timeFunction.N2.Week.displayName = &Nombre de semaines écoulées depuis

timeFunction.N1.Week = Valeur N pour le nombre de semaines écoulées depuis

timeFunction.N2.Week = Valeur N pour le nombre de semaines écoulées depuis

timeFunction.N1.Day.displayName = &Nombre de jours écoulés depuis

timeFunction.PERIODCHOICE.YEAR_displayName =ANNÉE

timeFunction.PERIODCHOICE.QUARTER_displayName =TRIMESTRE

timeFunction.PERIODCHOICE.MONTH_displayName =MOIS

timeFunction.PERIODCHOICE.WEEK_displayName =SEMAINE

timeFunction.PERIODCHOICE.DAY_displayName =JOUR

timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =Année jusqu'à la date

timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =Trimestre jusqu'à la date

timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =Mois jusqu'à la date

timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =Semaine jusqu'à la date

timeFunction.toolTip.to=A

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

datasource.cassandra.error=Impossible de trouver les JARS de la bibliothèque client Hector pour prendre en charge la source de données Cassandra nécessitant un script.

datasethandle.null.error=DataSetHandle ne doit pas être nul.

datasethandle.invalidColumnIndex=L'index de colonne n'est pas valide.

adapter.invalid.datetimetype=Le type de données dateheure "{0}" est incorrect.

adapter.invalid.aggregationfunctionname=La fonction d'agrégation {0} est incorrecte.

invalid.datetime.value=La valeur DateHeure "{0}" est incorrecte.

missing.join.condition=La condition de jointure entre le jeu de données principal "{0}" et la dimension "{1}" du cube "{2}" est absente.

exception.error=Une exception s'est produite au cours du traitement. Pour plus de détails, consultez le message suivant :

invalid.level.expression=Le nom de niveau "{0}" est introuvable.

invalid.measure.expression=L'expression de mesure"{0}" ne peut pas être nulle.

invalid.data.type=Type de données incorrect : {0}.

invalid.binding.expression=Expression {0} non valide.

cube.error.createhierarchy=Une erreur s''est produite lors de la création de la hiérarchie : {0}.

cube.error.createdimension=Une erreur s''est produite lors de la création de la dimension : {0}.

cube.error.createmeasure=Une erreur s'est produite lors de la création de la mesure.

cube.error.datasetmiss=Le cube ne peut pas être créé car le jeu de données correspondant est manquant.

cube.jointColumnNotInLevels=La colonne jointe "{0}" n'est pas définie en tant que champ clé pour aucun niveau dans la dimension "{1}".

cube.unsupportedAggregationInBinding=Fonction TopN/BottomN dans la liaison "{0}" non prise en charge dans xTab.

cube.error.aggregationFilter.referCalculatedMeasure=Agrégation non valide "{0}". L'expression de filtre a référencé une mesure calculée "{1}".

cube.query.error.dimensionMiss=La dimension "{0}" n'existe pas dans le cube.

cube.query.error.levelMiss=Le niveau associé au type Heure "{0}" n'existe pas dans la dimension Heure du cube "{1}".

cube.edge.error.levelMiss=Le niveau associé au type Heure "{0}" est manquant dans la définition de requête.

cube.derivedMeasure.invalid.reference=Référence de mesure non valide. La mesure dérivée "{0}" fait référence à une mesure non valide : "{1}".

cube.derivedMeasure.resolve.error=Echec de la résolution de la mesure dérivée "{0}".

cube.derivedMeasure.recursive.ref=Echec de la résolution de la mesure dérivée "{0}" car une référence circulaire a été détectée dans : "{1}".

timeFunction.CURRENT_QUARTER.name = Trimestre actuel

timeFunction.CURRENT_QUARTER.description = Calculer la caractéristique spécifique pour le trimestre actuel

timeFunction.CURRENT_MONTH.name = Mois actuel

timeFunction.CURRENT_MONTH.description = Calculer la caractéristique spécifique pour le mois actuel

timeFunction.PREVIOUS_MONTH.name = N mois précédent(s)

timeFunction.PREVIOUS_MONTH.description = Calculer la caractéristique spécifique pour le mois N précédent

timeFunction.PREVIOUS_WEEK_TO_DATE.name = N semaine(s) précédente(s) jusqu'à la date

timeFunction.PREVIOUS_WEEK_TO_DATE.description = Calculer la caractéristique spécifique pour la semaine jusqu'à la date de la semaine N précédente

timeFunction.PREVIOUS_MONTH_TO_DATE.name = N mois précédent(s) jusqu'à la date

timeFunction.PREVIOUS_MONTH_TO_DATE.description = Calculer la caractéristique spécifique pour le mois jusqu'à la date du mois N précédent

timeFunction.PREVIOUS_QUARTER.name = N trimestre(s) précédent(s)

timeFunction.PREVIOUS_QUARTER.description = Calculer la caractéristique spécifique pour le trimestre N précédent

timeFunction.PREVIOUS_QUARTER_TO_DATE.name = N trimestre(s) précédent(s) jusqu'à la date

timeFunction.PREVIOUS_QUARTER_TO_DATE.description = Calculer la caractéristique spécifique pour le trimestre N précédent

timeFunction.PREVIOUS_PREVIOUS_YEAR.name = N année(s) précédente(s)

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

timeFunction.YEAR_TO_DATE.name = Année jusqu'à la date (YTD)

timeFunction.YEAR_TO_DATE.description = Calculer la caractéristique spécifique pour la même année

timeFunction.QUARTER_TO_DATE.name = Trimestre jusqu'à la date (QTD)

timeFunction.QUARTER_TO_DATE.description = Calculer la caractéristique spécifique pour le même trimestre

timeFunction.MONTH_TO_DATE.name = Mois jusqu'à la date (MTD)

timeFunction.MONTH_TO_DATE.description = Calculer la caractéristique spécifique pour le même mois

timeFunction.PREVIOUS_YEAR_TO_DATE.name = N année(s) précédente(s) jusqu'à la date

timeFunction.PREVIOUS_YEAR_TO_DATE.description = Calculer la caractéristique spécifique pour la même année de l'année N précédente

timeFunction.WEEK_TO_DATE_LAST_YEAR.name = Semaine jusqu'à la date - Année dernière

timeFunction.WEEK_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour la même semaine de l'année N précédente

timeFunction.MONTH_TO_DATE_LAST_YEAR.name = Mois jusqu'à la date - Année dernière

timeFunction.MONTH_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour le même mois de l'année N précédente

timeFunction.QUARTER_TO_DATE_LAST_YEAR.name = Trimestre jusqu'à la date - Année dernière

timeFunction.QUARTER_TO_DATE_LAST_YEAR.description = Calculer la caractéristique spécifique pour le même trimestre de l'année N précédente

timeFunction.CURRENT_YEAR.name = Année actuelle

timeFunction.CURRENT_YEAR.description = Calculer la caractéristique spécifique pour l'année actuelle

timeFunction.WEEK_TO_DATE.name = Semaine jusqu'à la date (WTD)

timeFunction.WEEK_TO_DATE.description = Calculer la caractéristique spécifique pour la même semaine

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.name = Période actuelle depuis N périodes

timeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour la période actuelle depuis N périodes

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.name = Période jusqu'à la date depuis N périodes

timeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour la même période depuis N périodes

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.name = N périodes de fin depuis N périodes

timeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO.description = Calculer la caractéristique spécifique pour N périodes de fin depuis N périodes

timeFunction.TRAILING_N_MONTHS.name = N mois de fin

timeFunction.TRAILING_N_MONTHS.description = Calculer la caractéristique spécifique pour les N derniers mois

timeFunction.TRAILING_N_DAYS.name = N jours de fin

timeFunction.TRAILING_N_DAYS.description = Calculer la caractéristique spécifique pour les N jours de fin

timeFunction.NEXT_N_PERIODS.name = Prochaines N périodes

timeFunction.NEXT_N_PERIODS.description = Calculer la caractéristique spécifique pour les prochaines N périodes

timeFunction.PERIOD1 = L'argument de première période pour la fonction de temps

timeFunction.PERIOD2 = L'argument de deuxième période pour la fonction de temps

timeFunction.N1 = Valeur N pour la première période

timeFunction.N2 = Valeur N pour la deuxième période

timeFunction.PERIOD1.displayName = La pre&mière période

timeFunction.PERIOD2.displayName = La deuxième pé&riode

timeFunction.N1.displayName = Nombre de périodes écoulées depuis

timeFunction.N2.displayName = &De

timeFunction.N1.next.displayName = &Suivant

timeFunction.N2.Ago.displayName=Il &y a

timeFunction.N1.current.displayName=&Actuel

timeFunction.N1.period.displayName= &Période

timeFunction.trailing.N1.period.displayName=De &fin

timeFunction.trailing.N1.Month.displayName= &Nombre de mois de fin

timeFunction.N1.Year.displayName = &Nombre d'années écoulées depuis

timeFunction.N2.Year.displayName = &Nombre d'années écoulées depuis

timeFunction.N1.Year = Valeur N pour le nombre d'années écoulées depuis

timeFunction.N2.Year = Valeur N pour le nombre d'années écoulées depuis

timeFunction.N1.Month.displayName = &Nombre de mois écoulés depuis

timeFunction.N2.Month.displayName = &Nombre de mois écoulés depuis

timeFunction.N1.Month = Valeur N pour le nombre de mois écoulés depuis

timeFunction.N2.Month = Valeur N pour le nombre de mois écoulés depuis

timeFunction.N1.Quarter.displayName = &Nombre de trimestres écoulés depuis

timeFunction.N2.Quarter.displayName = &Nombre de trimestres écoulés depuis

timeFunction.N1.Quarter = Valeur N pour le nombre de trimestres écoulés depuis

timeFunction.N2.Quarter = Valeur N pour le nombre de trimestres écoulés depuis

timeFunction.N1.Week.displayName = &Nombre de semaines écoulées depuis

timeFunction.N2.Week.displayName = &Nombre de semaines écoulées depuis

timeFunction.N1.Week = Valeur N pour le nombre de semaines écoulées depuis

timeFunction.N2.Week = Valeur N pour le nombre de semaines écoulées depuis

timeFunction.N1.Day.displayName = &Nombre de jours écoulés depuis

timeFunction.PERIODCHOICE.YEAR_displayName =ANNÉE

timeFunction.PERIODCHOICE.QUARTER_displayName =TRIMESTRE

timeFunction.PERIODCHOICE.MONTH_displayName =MOIS

timeFunction.PERIODCHOICE.WEEK_displayName =SEMAINE

timeFunction.PERIODCHOICE.DAY_displayName =JOUR

timeFunction.PERIODCHOICE.PeriodToDate.YEAR_displayName =Année jusqu'à la date

timeFunction.PERIODCHOICE.PeriodToDate.QUARTER_displayName =Trimestre jusqu'à la date

timeFunction.PERIODCHOICE.PeriodToDate.MONTH_displayName =Mois jusqu'à la date

timeFunction.PERIODCHOICE.PeriodToDate.WEEK_displayName =Semaine jusqu'à la date

timeFunction.toolTip.to=A
RelativeTimePeriod.TimeLevelNotEnough=L'élément de rapport ne contient pas tous les niveaux temporels pour calculer cette période de temps relative avec une date de référence.
 
 
 
