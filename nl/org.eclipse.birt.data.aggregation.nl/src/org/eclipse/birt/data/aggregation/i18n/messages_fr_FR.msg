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

TotalIrr.param.intrate=Taux de rentabilit� &interne

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

TotalMirr.param.reinvestment_rate=Taux de r�i&nvestissement

TotalMode.description=fonction Total.mode()

TotalMode.displayName=MODE

TotalMovingAve.description=fonction Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=&Fen�tre

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

TotalConcatenate.param.separator=S�parat&eur

TotalConcatenate.param.maxLength=Longueur ma&ximale

TotalConcatenate.param.showAllValues=A&fficher toutes les valeurs

TotalConcatenate.paramDescription.expression=champ de donn�es � regrouper

TotalConcatenate.paramDescription.separator=s�parateur de la cha�ne concat�n�e

TotalConcatenate.paramDescription.maxLength=nombre de caract�res maximum de la valeur concat�n�e

TotalConcatenate.paramDescription.showAllValues=permet de d�finir si toutes les valeurs doivent �tre affich�es ou non

#aggregation error messages
aggregation.BadOperandType=Type de donn�es de l'op�rande inattendu : {0}.

aggregation.DataConversionError=Une erreur s'est produite lors de la conversion du type de donn�es.

aggregation.InvalidPercentileColumn=Toutes les valeurs des centiles ne peuvent pas �tre nulles.

aggregation.BadParameterCount=Le nombre de param�tres indiqu� est incorrect.

aggregation.BadParameterType=Le type de donn�es de l'un des param�tres est incorrect.

aggregation.NoIrrSolution=Impossible de trouver une solution pour la fonction Irr avec les donn�es fournies.

aggregation.InvalidTopBottomArgument=L'argument des filtres Top/Bottom doit �tre un nombre.

aggregation.InvalidTopBottomNArgument=L'argument des filtres TopN/BottomN doit �tre z�ro ou un entier positif.

aggregation.InvalidTopBottomPercentArgument=L'argument des filtres TopPercent/BottomPercent doit �tre positif et inf�rieur ou �gal � 100.

aggregation.InvalidTopBottomN=Le nombre total de lignes � traiter ne doit pas d�passer {0}.

aggregation.InvalidPercentileArgument=La fonction Total.Percentile n'accepte que les nombres compris entre 0 et 1 (inclus) pour son second argument.

aggregation.InvalidQuartileArgument=La fonction Total.Quartile n'accepte que les entiers compris entre 0 et 4 (inclus) pour son second argument.

aggregation.BadAggrFuncParam=Param�tre incorrect pour la fonction d'agr�gation : {0}.

aggregation.ResetRateGuess=L'utilisateur devrait d�finir une nouvelle estimation de taux.

aggregation.InvalidParameterType=Type de param�tre de l'agr�gation incorrect : {0}

aggregation.InvalidParameterValue=La valeur du param�tre "{0}" dans une agr�gation "{1}" est incorrecte : {2}.

aggregation.InvalidSeparator=S�parateur pour l'agr�gation incorrect : {0}
