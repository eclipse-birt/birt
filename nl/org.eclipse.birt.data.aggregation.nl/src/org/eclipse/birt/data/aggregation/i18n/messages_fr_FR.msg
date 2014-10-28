#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

#Aggregation function UI information
Constants.Expression=&Expression

TotalAve.description=fonction Total.AVE()

TotalAve.displayName=MOYENNE

TotalCount.description=fonction Total.COUNT()

TotalCount.displayName=NOMBRE

TotalCountDistinct.description=fonction Total.COUNTDISTINCT()

TotalCountDistinct.displayName=NOMBRE DISTINCT

TotalFirst.description=fonction Total.FIRST()

TotalFirst.displayName=PREMIER

TotalIrr.description=fonction Total.IRR()

TotalIrr.displayName=IRR

TotalIrr.param.intrate=Taux de rentabilité &interne

TotalLast.description=fonction Total.LAST()

TotalLast.displayName=DERNIER

TotalMax.description=fonction Total.MAX()

TotalMax.displayName=MAX

TotalMax.exception.cannot_get_max_value=impossible d'obtenir la valeur max\!

TotalMedian.description=fonction Total.median()

TotalMedian.displayName=MEDIANE

TotalMin.description=fonction Total.MIN()

TotalMin.displayName=MIN

TotalMin.exception.cannot_get_max_value=impossible d'obtenir la valeur max\!

TotalMirr.description=fonction Total.MIRR()

TotalMirr.displayName=MIRR

TotalMirr.param.finance_rate=Taux de &financement

TotalMirr.param.reinvestment_rate=Taux de réi&nvestissement

TotalMode.description=fonction Total.mode()

TotalMode.displayName=MODE

TotalMovingAve.description=fonction Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=&Fenêtre

TotalNpv.description=fonction Total.NPV()

TotalNpv.displayName=NPV

TotalNpv.param.rate=&Taux

TotalRunningCount.description=fonction Total.RUNNINGCOUNT()

TotalRunningCount.displayName=RUNNINGCOUNT

TotalRunningNpv.description=fonction Total.RUNNINGNPV()

TotalRunningNpv.displayName=RUNNINGNPV

TotalRunningNpv.param.rate=&Taux

TotalRunningSum.description=fonction Total.RUNNINGSUM()

TotalRunningSum.displayName=CUMUL

TotalStdDev.description=fonction Total.STDDEV()

TotalStdDev.displayName=STDDEV

TotalSum.description=fonction Total.sum()

TotalSum.displayName=SOMME

TotalVariance.description=fonction Total.variance()

TotalVariance.displayName=VARIANCE

TotalWeightedAve.description=fonction Total.WEIGHTEDAVE()

TotalWeightedAve.displayName=MOYENNE PONDEREE

TotalWeightedAve.param.weight=&Epaisseur

BaseTopBottomAggregation.param.n=&N

TotalIsBottomN.description=fonction Total.IsBottomN()

TotalIsBottomN.displayName=IS-BOTTOM-N

TotalIsBottomNPercent.description=fonction Total.IsBottomNPcent()

TotalIsBottomNPercent.displayName=IS-BOTTOM-N-PERCENT

TotalIsTopN.description=fonction Total.isTopN()

TotalIsTopN.displayName=IS-TOP-N

TotalIsTopNPercent.description=fonction Total.isTopNPercent

TotalIsTopNPercent.displayName=IS-TOP-N-PERCENT

TotalPercentile.description=fonction Total.PERCENTILE()

TotalPercentile.displayName=CENTILE

TotalPercentile.param.percentage=Pour&centage

TotalPercentRank.description=fonction Total.PERCENTRANK()

TotalPercentRank.displayName=PERCENTRANK

TotalPercentSum.description=fonction Total.PERCENTSUM()

TotalPercentSum.displayName=PERCENTSUM

TotalQuartile.description=fonction Total.QUARTILE()

TotalQuartile.displayName=QUARTILE

TotalQuartile.param.quart=&Quartile

TotalRank.description=fonction Total.RANK()

TotalRank.displayName=RANG

TotalRank.param.ascending=Croiss&ant

TotalConcatenate.description=fonction Total.CONCATENATE()

TotalConcatenate.displayName=CONCATENATE

TotalConcatenate.param.separator=Séparat&eur

TotalConcatenate.param.maxLength=Longueur ma&ximale

TotalConcatenate.param.showAllValues=A&fficher toutes les valeurs

TotalConcatenate.paramDescription.expression=champ de données à regrouper

TotalConcatenate.paramDescription.separator=séparateur de la chaîne concaténée

TotalConcatenate.paramDescription.maxLength=nombre de caractères maximum de la valeur concaténée

TotalConcatenate.paramDescription.showAllValues=permet de définir si toutes les valeurs doivent être affichées ou non

#aggregation error messages
aggregation.BadOperandType=Type de données de l'opérande inattendu : {0}.

aggregation.DataConversionError=Une erreur s'est produite lors de la conversion du type de données.

aggregation.InvalidPercentileColumn=Toutes les valeurs des centiles ne peuvent pas être nulles.

aggregation.BadParameterCount=Le nombre de paramètres indiqué est incorrect.

aggregation.BadParameterType=Le type de données de l'un des paramètres est incorrect.

aggregation.NoIrrSolution=Impossible de trouver une solution pour la fonction Irr avec les données fournies.

aggregation.InvalidTopBottomArgument=L'argument des filtres Top/Bottom doit être un nombre.

aggregation.InvalidTopBottomNArgument=L'argument des filtres TopN/BottomN doit être zéro ou un entier positif.

aggregation.InvalidTopBottomPercentArgument=L'argument des filtres TopPercent/BottomPercent doit être positif et inférieur ou égal à 100.

aggregation.InvalidTopBottomN=Le nombre total de lignes à traiter ne doit pas dépasser {0}.

aggregation.InvalidPercentileArgument=La fonction Total.Percentile n'accepte que les nombres compris entre 0 et 1 (inclus) pour son second argument.

aggregation.InvalidQuartileArgument=La fonction Total.Quartile n'accepte que les entiers compris entre 0 et 4 (inclus) pour son second argument.

aggregation.BadAggrFuncParam=Paramètre incorrect pour la fonction d'agrégation : {0}.

aggregation.ResetRateGuess=L'utilisateur devrait définir une nouvelle estimation de taux.

aggregation.InvalidParameterType=Type de paramètre de l'agrégation incorrect : {0}

aggregation.InvalidParameterValue=La valeur du paramètre "{0}" dans une agrégation "{1}" est incorrecte : {2}.

aggregation.InvalidSeparator=Séparateur pour l'agrégation incorrect : {0}
