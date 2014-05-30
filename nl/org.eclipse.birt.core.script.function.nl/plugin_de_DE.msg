#Properties file for org.eclipse.birt.core.script.bre
Bundle-Name.0 = Funktions-Plug-In

dataType.String=Zeichenfolge

dataType.Any=Any

dataType.DateTime=DateTime
dataType.Integer=Integer

dataType.Double=Doppelt

dataType.Number=Number

dataType.Boolean=Boolean

dataType.DoubleArray=Doppelt[]

FunctionDesc.charLength=Gibt die Anzahl der Zeichen in der Zeichenfolge zurück.

FunctionDesc.concat=Gibt eine Zeichenfolge zurück, die aus den angegebenen Zeichenfolgen zusammengesetzt ist.

FunctionDesc.indexOf=Sucht nach dem Ziel in der Zeichenfolgequelle und gibt den Index des zuerst auftretenden Musters zurück. \nDie Suche beginnt an der Startposition. Alle Indexwerte sind 0-basiert. Wird '\nlf start' ausgelassen, wird ein Wert von 0 angenommen. \nIn der Suche nach Zeichenfolgen wird zwischen Groß- und Kleinschreibung unterschieden.

FunctionDesc.left=Gibt die ersten n Zeichen der Zeichenfolge str zurück. \nWenn 'n' 0 ist, wird eine leere Zeichenfolge zurückgegeben. \nWenn 'n' größer als die Länge von 'str' ist, wird die gesamte Zeichenfolge zurückgegeben. \nWenn 'n' nicht angegeben ist, wird der Wert 1 verwendet.

FunctionDesc.right=Gibt die letzten n Zeichen der Zeichenfolge str zurück. \nWenn 'n' 0 ist, wird eine leere Zeichenfolge zurückgegeben. Wenn n größer als die Länge von str ist, wird die gesamte Zeichenfolge zurückgegeben. \nWenn 'n' nicht angegeben ist, wird der Wert 1 verwendet.

FunctionDesc.search=Ähnlich der Funktion indexOf. Ausnahme:\n(1) Beim Zeichenfolgevergleich wird nicht zwischen Groß- und Kleinschreibung unterschieden. \n(2) Musterzeichenfolge kann Platzhalter enthalten: \n* entspricht jeder Zeichensequenz (einschließlich leer); \n? entspricht jedem Einzelzeichen.

FunctionDesc.toLower=Wandelt die Zeichen in der Zeichenfolgequelle in Kleinbuchstaben um.

FunctionDesc.toUpper=Wandelt die Zeichen in der Zeichenfolgequelle in Großbuchstaben um.

FunctionDesc.trim=Entfernt alle voran stehenden und nachfolgenden Leerzeichen (Leertaste, Tab usw.).\nKonsolidiert außerdem alle aufeinander folgenden Leerzeichen.

FunctionDesc.trimLeft=Entfernt alle voranstehenden Leerzeichen. Nachfolgende Leerzeichen und Leerzeichen zwischen Wörtern werden nicht entfernt.

FunctionDesc.trimRight=Entfernt alle nachfolgenden Leerzeichen. Voranstehende Leerzeichen und Leerzeichen zwischen Wörtern werden nicht entfernt.

FunctionDesc.anyOf=Quelle ist im Zielbereich

FunctionDesc.between=TRUE, wenn Quelle zwischen target1 und target2 liegt. Andernfalls FALSE

FunctionDesc.notBetween=TRUE, wenn Quelle nicht zwischen target1 und target2 liegt. Andernfalls FALSE

FunctionDesc.equalTo=Quelle ist gleich Ziel

FunctionDesc.greaterThan=Quelle ist größer als Ziel

FunctionDesc.lessThan=Quelle ist kleiner als Ziel

FunctionDesc.greaterOrEqual=Quelle ist größer als oder gleich Ziel

FunctionDesc.lessOrEqual=Quelle ist kleiner als oder gleich Ziel

FunctionDesc.notEqual=Quelle ist nicht gleich Ziel

FunctionDesc.like=Quelle entspricht SQL-Musterziel

FunctionDesc.notLike=Quelle entspricht nicht SQL-Musterziel

FunctionDesc.match=Quelle entspricht JavaScript-Musterziel

FunctionDesc.compareString=Die beiden Zeichenfolgen nach den angegebenen Bedingungen vergleichen

FunctionDesc.add=Summe der zwei Zahlen n1 und n2.

FunctionDesc.substract=n1 subtrahiert n2

FunctionDesc.multiple=n1 multipliziert mit n2. Diese Funktion wird nur für die Abwärtskompatibilität der Funktion multiply verwendet.

FunctionDesc.multiply=n1 multipliziert n2

FunctionDesc.divide=Dividend wird durch Divisor dividiert

FunctionDesc.round=Rundet eine Zahl auf die angegebene Anzahl von Stellen. \ndec ist eine Ganzzahl und kann negativ sein. \nDer Standardwert für dec ist 0, wenn kein Wert angegeben ist.

FunctionDesc.roundUp=Rundet eine Zahl von 0 weg auf die angegebene Anzahl von Stellen auf. \nStandardwert für dec ist 0.

FunctionDesc.roundDown=Rundet eine Zahl von 0 weg auf die angegebene Anzahl von Stellen ab. \nStandardwert für dec ist 0.

FunctionDesc.ceiling=Rundet eine Zahl von Null weg auf das nächste Vielfache von Bedeutung auf.

FunctionDesc.mod=Rest nach Division der Zahl durch Divisor. Das Ergebnis hat dasselbe Vorzeichen wie der Divisor.

FunctionDesc.safeDivide=Wenn das Ergebnis der Division des Dividenden durch den Divisor nicht Null oder unendlich ist, wird das Ergebnis zurückgegeben. \nWenn der Wert 0 ist, wird der angegebene Wert zurückgegeben.

FunctionDesc.year=4-stellige Jahreszahl des gegebenen Datum/Uhrzeit-Werts.

FunctionDesc.quarter=Quartalnummer (1 bis 4) des gegebenen Datum/Uhrzeit-Werts.

FunctionDesc.month=Monat/Uhrzeit. Option ist ein ganzzahliger Wert:\n1 (Standard): Monatsnummer wird zurückgeben (1-12);\n2: volle Monatsnamen gemäß Gebietsschema des Benutzers werden zurückgeben (z. B. Januar bis Dezember für deutsches Gebietsschema); \n3: kurze Monatsnamen gemäß Gebietsschema des Benutzers werden zurückgeben (z. B. Jan bis Dez für deutsches Gebietsschema)

FunctionDesc.week=Wochennummer des Jahres (1 bis 52) des Datum/Uhrzeit-Werts d.

FunctionDesc.day=Tagesnummer des Monats (1 bis 31).

FunctionDesc.weekDay=Wochentag. Option ist ein ganzzahliger Wert\n1: eine Zahl zwischen 1 (Sonntag) und 7 (Samstag) wird zurückgeben;\n2: eine Zahl zwischen 1 (Montag) und 7 (Sonntag) wird zurückgeben;\n3: Eine Zahl zwischen 0 (Montag) und 6 (Sonntag) wird zurückgeben;\n4: Name des Wochentags gemäß Gebietsschema des Benutzers wird zurückgeben (z. B., Sonntag bis Samstag für Deutsch);\n5: abgekürzter Name des Wochentags gemäß Gebietsschema des Benutzers wird wiedergeben (z. B., So bis Sa für Deutsch)

FunctionDesc.today=Gibt einen Timestamp-Wert zurück, der Mitternacht des aktuellen Datums ist.

FunctionDesc.now=Gibt den aktuellen Timestamp zurück.

FunctionDesc.diffYear=Differenz in Anzahl von Jahren zurückgeben.

FunctionDesc.diffMonth=Differenz in Anzahl von Monaten zurückgeben.

FunctionDesc.diffQuarter=Differenz in Anzahl von Quartalen zurückgeben.

FunctionDesc.diffWeek=Differenz in Anzahl von Wochen zurückgeben.

FunctionDesc.diffDay=Differenz in Anzahl von Tagen zurückgeben.

FunctionDesc.diffHour=Differenz in Anzahl von Stunden zurückgeben.

FunctionDesc.diffMinute=Differenz in Anzahl von Minuten zurückgeben.

FunctionDesc.diffSecond=Differenz in Anzahl von Sekunden zurückgeben.

FunctionDesc.addYear=n Jahr(e) zum Datum hinzufügen

FunctionDesc.addMonth=n Monat(e) zum Datum hinzufügen

FunctionDesc.addQuarter=n Quartal(e) zum Datum hinzufügen

FunctionDesc.addWeek=n Woche(n) zum Datum hinzufügen

FunctionDesc.addDay=n Tag(e) zum Datum hinzufügen

FunctionDesc.addHour=n Stunde(n) zum Datum hinzufügen

FunctionDesc.addMinute=n Minute(n) zum Datum hinzufügen

FunctionDesc.addSecond=n Sekunde(n) zum Datum hinzufügen

FunctionDesc.firstDayOfYear=Gibt den ersten Tag des Jahres zurück, in das das Datum fällt.

FunctionDesc.firstDayOfQuarter=Gibt den ersten Tag des Quartals zurück, in das das Datum fällt.

FunctionDesc.firstDayOfMonth=Gibt den ersten Tag des Monats zurück, in den das Datum fällt.

FunctionDesc.firstDayOfWeek=Gibt den ersten Tag der Woche zurück, in die das Datum fällt.

FunctionDesc.ddb=Abschreibung eines Wirtschaftsguts über einen gegebenen, einzelnen Zeitraum mithilfe der degressiven Doppelratenabschreibung. Anmerkungen

FunctionDesc.fv=Künftiger Wert einer Annuität auf der Grundlage regelmäßiger, gleich bleibender Zahlungen und eines unveränderlichen Zinssatzes.

FunctionDesc.ipmt=Zinszahlung für einen bestimmten Zeitraum einer Annuität, \n basierend auf periodischen, gleich bleibenden Zahlungen und mit einem unveränderlichen Zinssatz.

FunctionDesc.irr=Interne Ertragsrate für eine Reihe periodischer Finanzströme (Zahlungen und Zahlungseingänge) in einem vorhandenen Array.

FunctionDesc.mirr=Überarbeitete Ertragsrate für eine Reihe periodischer Finanzströme (Zahlungen und Zahlungseingänge) in einem vorhandenen Array.

FunctionDesc.nper=Anzahl der Perioden für eine Annuität auf der Grundlage regelmäßiger, gleich bleibender Zahlungen und eines unveränderlichen Zinssatzes.

FunctionDesc.npv=Derzeitiger Nettowert einer veränderlichen Reihe von Finanzströmen (sowohl positiv als auch negativ) zu einem bestimmten Zinssatz.

FunctionDesc.percent=Berechnet das prozentuale Verhältnis zweier Zahlen.

FunctionDesc.pmt=Zahlung für eine Annuität auf der Grundlage regelmäßiger, gleich bleibender Zahlungen und eines unveränderlichen Zinssatzes.

FunctionDesc.ppmt=Kapitalzahlung für einen bestimmten Zeitraum einer Annuität auf der Grundlage eines Zinssatzes und eines konstanten Zahlungsplans.

FunctionDesc.pv=Derzeitiger Wert einer Annuität auf der Grundlage regelmäßiger, gleich bleibender, in der Zukunft zu leistenden Zahlungen und eines unveränderlichen Zinssatzes.

FunctionDesc.rate=Zinssatz pro Periode für eine Annuität.

FunctionDesc.sln=Lineare Abschreibung eines Wirtschaftsguts über einen einzelnen Zeitraum.

FunctionDesc.syd=Bemerkung zu geometrisch-degressive Abschreibung eines Wirtschaftsguts über einen bestimmten Zeitraum hinweg

FunctionDesc.addDate=Gibt ein Datum zurück, das sich aus der Addition von Jahren, Monaten und Tagen zum Startdatum ergibt.

FunctionDesc.addTime=Gibt ein Datum zurück, das sich aus der Addition von Stunden, Minuten, Sekunden zum Startdatum ergibt.

FunctionDesc.subDate=Gibt ein Datum zurück, das sich aus der Subtraktion von Jahren, Monaten und Tagen zum Startdatum ergibt.

FunctionDesc.subTime=Gibt ein Datum zurück, das sich aus der Subtraktion von Stunden, Minuten, Sekunden zum Startdatum ergibt.

FunctionDesc.days=Anzahl der Tage zwischen zwei Datumsangaben

FunctionDesc.hours=Anzahl der Stunden zwischen zwei Datumsangaben

FunctionDesc.minutes=Anzahl der Minuten zwischen zwei Datumsangaben

FunctionDesc.months=Anzahl der Monate zwischen zwei Datumsangaben

FunctionDesc.seconds=Anzahl der Sekunden zwischen zwei Datumsangaben

FunctionDesc.years=Anzahl der Jahre zwischen zwei Datumsangaben

FunctionDesc.date= Gibt ein Datumsobjekt zurück, das mit gegebenem Jahr, Monat, Tag, Stunden, Minuten und Sekunden initialisiert wird. \n(Jahr, Monat, Tag sind erforderlich; Stunden, Minuten, Sekunden sind optional)

FunctionDesc.duration.year=Ruft den Wert des Felds JAHRE als Ganzzahl ab, * oder 0, wenn nicht angegeben.\nlexicalDuration ist die Repräsentation der Dauerzeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.month=Ruft den Wert des Felds MONATE als Ganzzahl ab, * oder 0, wenn nicht angegeben.\nlexicalDuration ist die Repräsentation der Dauerzeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.day=Ruft den Wert des Felds TAGE als Ganzzahl ab, * oder 0, wenn nicht angegeben.\nlexicalDuration ist die Repräsentation der Dauerzeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.hour=Ruft den Wert des Felds STUNDEN als Ganzzahl ab, * oder 0, wenn nicht angegeben.\nlexicalDuration ist die Repräsentation der Dauerzeichenfolge 'PnYnMnDTnHnMnS'(z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.minute=Ruft den Wert des Felds MINUTEN als Ganzzahl ab, * oder 0, wenn nicht angegeben.\nlexicalDuration ist die Repräsentation der Dauerzeichenfolge 'PnYnMnDTnHnMnS'(z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.second=Ruft den Wert des Felds SEKUNDEN als Ganzzahl ab, * oder 0, wenn nicht angegeben.\nlexicalDuration ist die Repräsentation der Dauerzeichenfolge 'PnYnMnDTnHnMnS'(z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.timeInMills=Gibt den Wert der Dauer in Millisekunden wieder.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S)\nDas Argument startDate wird verwendet, um diese Varianz eindeutig zu machen

FunctionDesc.duration.isLongerThan=Prüft, ob lexicalDuration1 stets länger ist als lexicalDuration2.\nZum Beispiel, 'P1D' (ein Tag) > 'PT12H' (12 Stunden) und 'P2Y' (zwei Jahre) > 'P23M' (23 Monate)

FunctionDesc.duration.isShorterThan=Prüft, ob lexicalDuration1 stets kürzer ist als lexicalDuration2.\nZum Beispiel: 'PT12H' (12 Stunden) < 'P1D' (ein Tag).

FunctionDesc.duration.add=Gibt den Wert der Dauer in Millisekunden wieder.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S).

FunctionDesc.duration.substract=Berechnet eine neue Dauer, deren Wert lexicalDuration1 - lexicalDuration2 ist.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S).

FunctionDesc.duration.multiply=Berechnet eine neue Dauer, deren Wert um den Faktor länger als der Wert von lexicalDuration ist.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S).\nFactor is the times longer of new Duration to create

FunctionDesc.duration.negate=Gibt eine neue Dauer in Millisekunden wieder, deren Wert -lexicalDuration ist.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S).

FunctionDesc.duration.getSign=Gibt das Vorzeichen der Dauer als -1, 0 oder 1 zurück.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S).

FunctionDesc.duration.compare=Teilordnungsrelation zwischen lexicalDuration1 und lexicalDuration2.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S)

FunctionDesc.duration.addTo=Fügt diese Dauer zu einem Datenobjekt hinzu.\nlexicalDuration ist die Repräsentation der Zeichenfolge 'PnYnMnDTnHnMnS' (z. B. P5Y2M15DT4H40M20S)\nDas Argument startDate wird verwendet, um diese Varianz eindeutig zu machen

FunctionDesc.format=Ruft die formatierte Zeichenfolge aus dem angegebenen Objekt und Muster ab.
