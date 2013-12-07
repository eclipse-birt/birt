###############################################################################
# Copyright (c) 2013 Actuate Corporation
# All rights reserved.
#
###############################################################################
# Plug-in Configuration
#
oda.data.source.id=org.eclipse.birt.data.oda.mongodb
#
###############################################################################
# NLS Strings
###############################################################################
#
plugin.name=ODA-Laufzeittreiber für MongoDB-Datenquelle
data.source.name=MongoDB-Datenquelle
data.set.name=MongoDB-Datensatz
connection.profile.name=MongoDB-Datenquellen-Verbindungsprofil

# Data source properties
datasource.property.mongoDbUri=&Mongo-Datenbank-URI
datasource.property.ignoreURI=URI &ignorieren
datasource.property.serverHost=&Host des Servers
datasource.property.serverPort=Servera&nschluss
datasource.property.databaseName=&Datenbankname
datasource.property.userName=B&enutzername
datasource.property.password=Kenn&wort
datasource.property.socketKeepAlive=Socket &aufrechterhalten
datasource.property.useRequestSession='Consistent &Request'-Sitzung
datasource.property.group.uriElements=Mongo-Datenbank-URI-&Elemente
datasource.property.group.options=Ergänzende Client-&Einstellung(en)

# Data set properties - query definition
dataset.property.collectionName=&Sammlungsname
dataset.property.operationType=Befehls&vorgangstyp
dataset.property.operationExpr=Befehls&ausdruck
dataset.property.selectedFields=Au&sgewählte Felder
dataset.property.findQueryExpr=&Abfrageausdruck
dataset.property.sortExpr=&Sortierungsausdruck
# Data set properties - advanced runtime properties
dataset.property.batchSize=Maximale Anzahl Ergebnisobjekte je &Batch
dataset.property.rtMDSearchLimit=Maximale Anzahl Dokumente für die Suche nach Laufzeit-&Metadaten
dataset.property.numSkipDocuments=Maximale Anzahl Dokumente zum Ü&berspringen
dataset.property.indexHints=Index&hinweisausdruck
dataset.property.queryReadPreference=&Lesevoreinstellung des Query-Cursors
dataset.property.queryReadPreferenceTags=Lesevoreinstellung Ta&g-Sätze
dataset.property.noTimeOut=&Keine Cursor-Zeitüberschreitung
dataset.property.allowsPartialResults=Ermöglicht &Teilergebnisse
dataset.property.flattenCollections=Verschachtelte Sammlungen &verflachen
#
dataset.property.value.readPrefPrimary=primär
dataset.property.value.readPrefPrimaryPreferred=primaryPreferred
dataset.property.value.readPrefSecondary=sekundär
dataset.property.value.readPrefSecondaryPreferred=secondaryPreferred
dataset.property.value.readPrefNearest=nächstes
