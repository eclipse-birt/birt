#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

#Aggregation function UI information
Constants.Expression=&Ausdruck

TotalAve.description=Funktion Total.AVE()

TotalAve.displayName=DSCH

TotalCount.description=Funktion Total.COUNT()

TotalCount.displayName=ANZAHL

TotalCountDistinct.description=Funktion Total.COUNTDISTINCT()

TotalCountDistinct.displayName=COUNTDISTINCT

TotalFirst.description=Funktion Total.FIRST()

TotalFirst.displayName=ERSTE

TotalIrr.description=Funktion Total.IRR()

TotalIrr.displayName=IRR

TotalIrr.param.intrate=&Intrate

TotalLast.description=Funktion Total.LAST()

TotalLast.displayName=LETZTE

TotalMax.description=Funktion Total.MAX()

TotalMax.displayName=MAX

TotalMax.exception.cannot_get_max_value=Maximalwert kann nicht abgerufen werden\!

TotalMedian.description=Funktion Total.median()

TotalMedian.displayName=MEDIAN

TotalMin.description=Funktion Total.MIN()

TotalMin.displayName=MIN

TotalMin.exception.cannot_get_max_value=Maximalwert kann nicht abgerufen werden\!

TotalMirr.description=Funktion Total.MIRR()

TotalMirr.displayName=MIRR

TotalMirr.param.finance_rate=Finance &Rate

TotalMirr.param.reinvestment_rate=Re&investment Rate

TotalMode.description=Funktion Total.mode()

TotalMode.displayName=MODUS

TotalMovingAve.description=Funktion Total.MOVINGAVE()

TotalMovingAve.displayName=MOVINGAVE

TotalMovingAve.param.window=&Fenster

TotalNpv.description=Funktion Total.NPV()

TotalNpv.displayName=NPV

TotalNpv.param.rate=&Rate

TotalRunningCount.description=Funktion Total.RUNNINGCOUNT()

TotalRunningCount.displayName=RUNNINGCOUNT

TotalRunningNpv.description=Funktion Total.RUNNINGNPV()

TotalRunningNpv.displayName=RUNNINGNPV

TotalRunningNpv.param.rate=&Rate

TotalRunningSum.description=Funktion Total.RUNNINGSUM()

TotalRunningSum.displayName=RUNNINGSUM

TotalStdDev.description=Funktion Total.STDDEV()

TotalStdDev.displayName=STDDEV

TotalSum.description=Funktion Total.sum()

TotalSum.displayName=SUMME

TotalVariance.description=Funktion Total.variance()

TotalVariance.displayName=ABWEICHUNG

TotalWeightedAve.description=Funktion Total.WEIGHTEDAVE()

TotalWeightedAve.displayName=WEIGHTEDAVE

TotalWeightedAve.param.weight=&Weight

BaseTopBottomAggregation.param.n=&N

TotalIsBottomN.description=Funktion Total.IsBottomN()

TotalIsBottomN.displayName=IS-BOTTOM-N

TotalIsBottomNPercent.description=Funktion Total.IsBottomNPcent()

TotalIsBottomNPercent.displayName=IS-BOTTOM-N-PERCENT

TotalIsTopN.description=Funktion Total.isTopN()

TotalIsTopN.displayName=IS-TOP-N

TotalIsTopNPercent.description=Funktion Total.isTopNPercent()

TotalIsTopNPercent.displayName=IS-TOP-N-PERCENT

TotalPercentile.description=Funktion Total.PERCENTILE()

TotalPercentile.displayName=PERCENTILE

TotalPercentile.param.percentage=&Percentage

TotalPercentRank.description=Funktion Total.PERCENTRANK()

TotalPercentRank.displayName=PERCENTRANK

TotalPercentSum.description=Funktion Total.PERCENTSUM()

TotalPercentSum.displayName=PERCENTSUM

TotalQuartile.description=Funktion Total.QUARTILE()

TotalQuartile.displayName=QUARTIL

TotalQuartile.param.quart=&Quart

TotalRank.description=Funktion Total.RANK()

TotalRank.displayName=RANK

TotalRank.param.ascending=&Aufsteigend

TotalConcatenate.description=funtion Total.CONCATENATE()

TotalConcatenate.displayName=CONCATENATE

TotalConcatenate.param.separator=&Trennzeichen

TotalConcatenate.param.maxLength=Ma&ximal zulässige Länge

TotalConcatenate.param.showAllValues=Alle &Werte anzeigen

TotalConcatenate.paramDescription.expression=Das zu aggregierende Datenfeld.

TotalConcatenate.paramDescription.separator=Das Trennzeichen der verbundenen Zeichenfolge.

TotalConcatenate.paramDescription.maxLength=Die maximal zulässige Zeichenanzahl für die verbundene Zeichenfolge.

TotalConcatenate.paramDescription.showAllValues=Angabe, ob alle Werte angezeigt werden sollen.

#aggregation error messages
aggregation.BadOperandType=Unerwarteter Operanden-Datentyp: {0}.

aggregation.DataConversionError=Fehler bei der Konvertierung des Datentyps.

aggregation.InvalidPercentileColumn=Nicht alle Perzentilwerte können Null sein.

aggregation.BadParameterCount=Es wurde eine falsche Parameteranzahl angegeben.

aggregation.BadParameterType=Der Datentyp eines Parameters ist falsch.

aggregation.NoIrrSolution=Lösung für die IRR-Funktion (Internal Rate of Return) kann mit den angegebenen Daten nicht gefunden werden.

aggregation.InvalidTopBottomArgument=Das Argument der Filter 'Top/Bottom' muss eine Zahl sein.

aggregation.InvalidTopBottomNArgument=Das Argument der Filter 'TopN/BottomN' muss entweder Null oder eine positive Ganzzahl sein.

aggregation.InvalidTopBottomPercentArgument=Das Argument der Filter 'TopPercent/BottomPercent' muss sowohl positiv als auch kleiner als oder gleich 100 sein.

aggregation.InvalidTopBottomN=Die Gesamtzahl der Zeilen darf {0} nicht überschreiten.

aggregation.InvalidPercentileArgument=Für die Funktion 'Total.Percentile' sind als zweites Argument nur Zahlenwerte zwischen 0 und 1 (einschließlich) zulässig.

aggregation.InvalidQuartileArgument=Für die Funktion 'Total.Quartile' sind als zweites Argument nur Zahlenwerte zwischen 0 und 4 (einschließlich) zulässig.

aggregation.BadAggrFuncParam=Unzulässiger Parameter für Aggregatfunktion: {0}.

aggregation.ResetRateGuess=Benutzer sollte den neuen Wert für 'Rate Guess' zurücksetzen.

aggregation.InvalidParameterType=Ungültiger Parametertyp von Aggregat: {0}

aggregation.InvalidParameterValue=Der Wert des Parameters '{0}' in einem Aggregat '{1}' ist ungültig: {2}.

aggregation.InvalidSeparator=Ungültiges Trennzeichen für Aggregat: {0}
