###########################################################
#Solution reports descriptions

#Listing
ProductCatalog=Druckt den Classic Models-Produktkatalog, sortiert nach Produktkategorie. Enthält Informationen zum Produktnamen und zu den Kosten sowie eine Beschreibung. Erläutert die Gruppierung auf einer Ebene und die Verwendung eines Rasters innerhalb einer Tabellenzeile zum Strukturieren des Abstands. Veranschaulicht auch die Verwendung von Bildern und des <value-of>-Tags in einem Textelement, das zum Hinzufügen des Inhalts einer Datenbankspalte benötigt wird. Des Weiteren verwendet der Bericht Stile für eine leichtere Verwaltung und ein einheitliches Aussehen. 
TopSellingProducts=Zeigt ein Kreisdiagramm mit dem Erlös durch die Produktlinie an. Listet die am meisten verkauften Produkte sortiert nach Erlös auf. Erläutert die Verwendung eines Diagramms und das Sortieren von Ergebnissen. Veranschaulicht auch die Verwendung von Bildern und eines Rasters bzw. von Tabellen zur Strukturierung des Berichtinhalts. Des Weiteren verwendet der Bericht Stile für eine leichte Verwaltung und ein einheitliches Aussehen. 
TopNPercent=Dieser Bericht zeigt mithilfe der in BIRT 2.0 hinzugefügten Optionen "Erste n" und "Oberste %" ein Kreisdiagramm an. Dieses listet die ersten n Kunden nach Erlös auf, gefolgt von den ersten M% Kunden nach Erlös und nach Kundenname. Hierfür müssen die Daten mehrfach übergeben werden. Dies erfolgt jedoch durch die BIRT-Berichts-Engine selbst. N und M werden an den Bericht als Parameter übergeben, damit die Benutzer des Berichts bei dessen Ausführung die Zahl und den Prozentsatz auswählen können. 
StaticCrosstab=Dieses Beispiel veranschaulicht die Erstellung eines statischen Kreuztabellenberichts mithilfe der BIRT-Funktion "Total". Der Bericht zeigt den jährlichen Umsatzerlös nach Quartal in Bezug auf jede Produktlinie an, die derzeit von Classic Models Inc. vertrieben wird. Des Weiteren werden Summen horizontal und vertikal aggregiert. Die BIRT-Funktion "Total" kann einen zweiten Parameter akzeptieren, der die aggregierten Daten filtert. In diesem Beispiel wird die Funktion nur zum Anzeigen von Werten verwendet, die sich auf ein bestimmtes Produktangebot beziehen, z. B. auf Eisenbahnen. Der erste Parameter in der Funktion "Total" ist der zu aggregierende Wert; in diesem Beispiel ist er jeweils auf die verkaufte Menge multipliziert durch den Preis festgelegt. Beim zweiten Parameter handelt es sich um den Filter. Wenn der Filter als dataSetRow["Products::PRODUCTLINE"]=="Motorräder" festgelegt ist, wird nur die Produktlinie "Motorräder" aggregiert. 

#Business Forms
SalesInvoice=Druckt eine Rechnung für den ausgewählten Auftrag, einschließlich Kunden- und Rechnungsdetails sowie bestellte Produkte. Zeigt die Verwendung eines Parameters zum Auswählen des Auftrags, der in Rechnung gestellt werden soll, sowie Ausdrücke für einige berechnete Felder, einschließlich Discount und Gesamtsumme. Verwendet einen Ausdruck zum Erstellen der Kundenadresszeichenfolge und veranschaulicht die Unterdrückung von Nullen in Datenbankfeldern bei der Javascript-Funktion "replace". Veranschaulicht auch die Verwendung von Bildern sowie von Rastern und von Tabellen zur Strukturierung des Berichtinhalts. Des Weiteren verwendet der Bericht Stile für eine leichtere Verwaltung und ein einheitliches Aussehen. 

###########################################################
#Reporting Feature Examples descriptions

#XML Data Source
XMLDS_After=Ein einfacher Bericht, der die Verwendung von XML-Datenquellen veranschaulicht. 

#Libraries
Newsfeeds=Dieses Beispiel veranschaulicht, wie BIRT zum Erstellen wiederverwendbarer Bibliotheken für Berichtkomponenten verwendet werden kann. In diesem Beispiel haben wir zunächst eine Bibliothek erstellt. Anschließend nutzen wir URIs zum Eclipse RSS-Newsfeed als Datenquelle für einen Bericht und speichern ihn in der Bibliothek. Anschließend veröffentlichen wir die Bibliothek in einem Ressourcenordner. Dieses Beispiel zeigt die Erstellung eines nicht visuellen Elements in einer Bibliothek. Beachten Sie, dass auch visuelle Elemente in einer Bibliothek erstellt und gespeichert werden können. 
Newsfeeds_After=Dieses Beispiel veranschaulicht, wie BIRT zum Erstellen wiederverwendbarer Bibliotheken für Berichtkomponenten verwendet werden kann. In diesem Beispiel haben wir zunächst eine Bibliothek erstellt. Anschließend nutzen wir URIs zum Eclipse RSS-Newsfeed als Datenquelle für einen Bericht und speichern ihn in der Bibliothek. Anschließend veröffentlichen wir die Bibliothek in einem Ressourcenordner. Dieses Beispiel zeigt die Erstellung eines nicht visuellen Elements in einer Bibliothek. Beachten Sie, dass auch visuelle Elemente in einer Bibliothek erstellt und gespeichert werden können.

#Combination Chart
CustomerOrdersFinal=Dieses Beispiel veranschaulicht, wie Sie in BIRT ein Kombinationsdiagramm erstellen können. Des Weiteren wird gezeigt, wie mithilfe interner Lesezeichen und des Diagramm-Interaktivitätseditors ein Drilldown zu Details hinzugefügt wird. Das Kombinationsdiagramm zeigt die ausstehenden Verbindlichkeiten der fünf wichtigsten Kunden im Balkendiagrammformat mit einer Liniendatenreihe für das Kreditlimit jedes Kunden an, das überlagert angezeigt wird. Die Balken im Diagramm werden anschließend miteinander verbunden, damit Details zu jedem Kunden abgerufen werden können. 

#Cascading
cascade=Dieses Beispiel veranschaulicht die Erstellung eines BIRT-Berichts, der eine Verschachtelungsparametergruppe verwendet. Eine Verschachtelungsparametergruppe lässt das Verknüpfen einer Parametergruppe zu, wobei die Auswahl eines Werts für den ersten Parameter die für die nachfolgenden Parameter zur Verfügung stehende Auswahl beeinflusst. Verschachtelungsparameter können mit einem oder mehreren Datensätzen verknüpft werden. Die Datensätze füllen jede Ebene der Verschachtelung. In Kombination mit Datensatz-Parametern erweist sich diese Vorgehensweise beim Filtern zurückgegebener Daten für einen Bericht als besonders flexibel.

#Drill to details
MasterDetailOneReport=Dieses Beispiel veranschaulicht die Erstellung eines einzigen Berichts mit sowohl Master- als auch Detailinformationen.
OrderMasterAfter=Dieses Beispiel veranschaulicht die Erstellung eines Master- und eines detaillierten Berichts. Der Master-Bericht listet Aufträge auf und ermöglicht es dem Benutzer des Berichts, auf eine bestimmte Auftragsnummer zu klicken, um einen detaillierten Bericht für den ausgewählten Auftrag abzufragen. Dieses Beispiel veranschaulicht die Verwendung eines einfachen Parameters zum Filtern von Ergebnissen im detaillierten Bericht und die Verwendung der Hyperlink-Eigenschaft im Master-Bericht zum Übergeben dieses Parameters an den detaillierten Bericht. Die Hyperlink-Eigenschaft im Master-Bericht wird basierend auf den bei einer Abfrage abgerufenen Ergebnissen dynamisch generiert.
OrderDetailAfter=Der detaillierte Bericht, der durch Klicken auf Master-Bericht abgefragt wurde.

#Dynamic Report Parameter
ParameterToDataSetParameter=Dieses Beispiel veranschaulicht die Verknüpfung eines Berichtparameters mit einem Datensatzparameter.

#Expressions
Expressions=Dieses Beispiel veranschaulicht die Erstellung eines BIRT-Berichts, in dem Funktionen verwendet werden.

#Grouping
CustomerListAfter_Grouping=Dieses Beispiel veranschaulicht die Erstellung eines gruppierten Berichts in BIRT. Der Beispielbericht zeigt eine Liste von Kunden und gruppiert diese nach Land. Schlüsselfunktionen von BIRT, die in dieser Präsentation abgedeckt werden, sind Gruppierung, Seitenumbrüche, Inhaltsverzeichnis und das gruppenweise bzw. gesamte Aggregieren von Daten. 

#Highlighting and Conditional Formatting
ProductListAfter=Dieses Beispiel veranschaulicht das Hinzufügen von Kennzeichnungen und bedingter Formatierung zu einem BIRT-Bericht. Es wird ein Produktlistenbericht erstellt, der alle Produkte des Unternehmens anzeigt. Der Beispielbericht verwendet abwechselnd verschiedene Farben für Zeilen und kennzeichnet Zeilen mit einem Produkt, das nur in geringen Mengen vorrätig ist. Dieser Bericht zeigt auch, wie Sie dem onCreate-Ereignis einer Zeile ein Skript hinzufügen können, um den Zeilenstil von Produkten mit hohem Preisaufschlag in kursiv zu ändern. 

#Mapping and Sorting 
CustomerListAfter_Mapping=Dieses Beispiel veranschaulicht die Verwendung der Zuordnungs- und Sortierungsfunktionen von BIRT. Die Zuordnungsfunktion ermöglicht es einem BIRT-Berichtselement, basierend auf einem Ausdruck einen anderen Wert anzuzeigen. Wenn bei einer Abfrage z. B. ein numerisches Feld für Monat (1 - 12) zurückgegeben wird, kann die Zuordnungsfunktion dazu verwendet werden, um Januar bis Dezember auszudrücken. In diesem Beispiel verwenden wir die Zuordnungsfunktion, um einen Kreditrahmen für Classic Models-Kunden anzuzeigen. Die Sortierungsfunktion wird in vielen Bereichen von BIRT verwendet und ist sehr flexibel. In diesem Beispiel wird das Hinzufügen eines Sortierungsausdrucks zu einem Tabellenelement erläutert, das an einen Berichtparameter gebunden ist. Der Parameter lässt die Eingabe einer Suchzeichenfolge zu, die anschließend im Sortierungsausdruck zum Festlegen der Sortierreihenfolge für die Tabelle verwendet wird. 

#Parallel Report 
EmployeeAfter=Dieses Beispiel veranschaulicht die Erstellung eines Berichts mit parallelen Bereichen. Es veranschaulicht auch die Verwendung von Tabellenfiltern zum Filtern angezeigter Daten. Der Bericht verwendet ein Rasterelement zum grafischen Darstellen zweier Spalten. Jeder Spalte wird eine Reihe verschachtelter Tabellen hinzugefügt, um Mitarbeiterinformationen abrufen und nach Positionshierarchie gruppieren zu können. Die Tabellen werden nach Zweigstellencode gefiltert. 

#Query Modification
NullParameter=Dieses Beispiel zeigt, wie man eine Abfrage auf Basis von Berichtparametern, die Datensatzereignisse verwenden, ändert. Die Abfrage wird beispielsweise geändert, wenn ein Wert für den Berichtparameter angegeben ist. 
OrderDetailsAfter=Dieses Beispiel veranschaulicht das Ändern einer JDBC-Abfrage mithilfe der Eigenschaftsbindung oder eines Skripts. Die Eigenschaftsbindung ist eine Funktion von BIRT, mit der Laufzeitänderungen an öffentlichen Einstellungen von Datenquellen und Datensätzen vorgenommen werden können. Dies kann nützlich sein, wenn Sie diesen beiden Elementen kontextsensitive Informationen hinzufügen möchten. Beispielsweise kann die Where-Klausel zum Abrufen des Benutzernamens aus einer Sitzung geändert oder die Datenbank-URL basierend auf einem Berichtparameter zwischen Entwicklung und Produktion umgeschaltet werden. 

#Report Elements
ProductLinesAfter=Dieses Beispiel veranschaulicht die Verwendung von Beschriftungs-, Text-, dynamischen Text-, Daten- und Bildelementen innerhalb eines Berichtdesigns. Es werden auch einige Schlüsselfunktionen und Unterschiede zwischen den Elementen erläutert. Dieses Beispiel erklärt auch das Abrufen von BLOB- und CLOB-Daten aus einer Datenquelle. 

#Sub Report
OrdersAfter=Dieses Beispiel veranschaulicht die Erstellung eines Berichts, der verschachtelte Tabellen verwendet. Die an die innere Tabelle gebundene Abfrage enthält Parameter, um Auftragsdetails für eine gegebene Auftragsnummer in der äußeren Tabelle zurückzugeben. Dieses Beispiel erklärt auch das Verweisen der äußeren Tabellenspalten innerhalb einer verschachtelten Tabelle. Dadurch können BIRT-Berichte mit Master-Detailinformationen ausgestattet werden, die im selben Bericht enthalten sind. Während die Zeilen der äußeren Tabelle verarbeitet werden, wird die Abfrage für die innere Tabelle basierend auf den Informationen der äußeren Tabelle erneut durchgeführt. 

#Sorting
SortTableByReportParameter=Dieses Beispiel veranschaulicht die Erstellung eines Berichts, der verschachtelte Tabellen verwendet. Die an die innere Tabelle gebundene Abfrage enthält Parameter, um Auftragsdetails für eine gegebene Auftragsnummer in der äußeren Tabelle zurückzugeben. Dieses Beispiel erklärt auch das Verweisen der äußeren Tabellenspalten innerhalb einer verschachtelten Tabelle. Dadurch können BIRT-Berichte mit Master-Detailinformationen ausgestattet werden, die im selben Bericht enthalten sind. Während die Zeilen der äußeren Tabelle verarbeitet werden, wird die Abfrage für die innere Tabelle basierend auf den Informationen der äußeren Tabelle erneut durchgeführt. 

#Cross tab
CrosstabSampleRevenue=Dieses Beispiel veranschaulicht die Erstellung eines einfachen Berichts mit einem Kreuztabellen-Berichtobjekt (auf Grundlage eines BIRT-Quaders). Es wird eine Kreuztabelle mit der Produktlinie im Spalten- und dem Quartal im Zeilenbereich erstellt.
###########################################################
#Contribute Samples
Contribute_Samples=Reichen Sie einen Bugzilla-Eintrag unter https://bugs.eclipse.org/bugs/enter_bug.cgi?product=BIRTbb&bug_severity=enhancement ein, und hängen Sie die Berichtdesigndatei und den Screenshot über die Berichtausgabe an (im .png-Bildformat, mit einer Auflösung von 800 x 600 und unter 250 KB).

###########################################################
#Scripting

#Dynamic Map Rule
DynamicMapRule=Dieses Beispiel veranschaulicht die Design Engine-API innerhalb des vorFactory-Skripts, um einem Berichtselement eine Zuordnungsregel hinzuzufügen.  

#Group Separator
GroupSeparator=Dieses Beispiel veranschaulicht, wie ein Trennzeichen alle 3 Reihen innerhalb einer Gruppe hinzugefügt wird. Diese Zahl wird bei jedem Seitenumbruch zurückgesetzt.  

#Retrieve Session Information
Application_Session_IntegrationBF=Dieses Beispiel veranschaulicht die Verwendung von JavaScript, um Informationen über eine Sitzung zu erhalten.

#Scripted Data Source
ScriptedDatasourceExampleAfter=Dieser Abschnitt mit Beispielen konzentriert sich auf das Scripting innerhalb von BIRT. Einfache Berichte veranschaulichen die Scripting-Funktionalität. Die meisten Beispiele enthalten einen Vorher-/Nachher-Bericht, den Sie herunterladen können. Außerdem wird das Beispiel in einer Flash-Präsentation veranschaulicht. Um das exportierte Java-Objekt zu laden und es dem Bericht zu ermöglichen, es zu verwenden, starten Sie BIRT nach dem Öffnen dieses Beispielberichts neu.  

#Visibility Rule
Addvisrule=Dieses Beispiel veranschaulicht, wie eine Sichtbarkeitsregel in einem Skript hinzugefügt wird.  

#Chart
SimpleChartAPI=Dieses Beispiel veranschaulicht eine Reihe der in der einfachen Diagramm-API verfügbaren Javascript-Methoden.  Diese werden vom beforeFactory-Ereignis-Handler des Berichts aufgerufen.

###########################################################
#Extending BIRT

#Aggreate function extension point
optim=Dieser Abschnitt mit Beispielen konzentriert sich auf die Erweiterung von BIRT. Bei den hier aufgeführten Beispielen liegt das Hauptaugenmerk auf der Implementierung von Erweiterungspunkten, die von BIRT bereitgestellt werden. Dadurch wird es dem BIRT-Entwickler ermöglicht, das Standardverhalten von Designer oder der Runtime Engine hinzuzufügen bzw. zu ändern. Dies umfasst das Hinzufügen von Diagrammtypen, Aggregatfunktionen, Datenquellen und Berichtselementen. 


 
