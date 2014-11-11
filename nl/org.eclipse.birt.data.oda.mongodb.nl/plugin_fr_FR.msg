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
plugin.name=Pilote d'exécution ODA de la source de données MongoDB
data.source.name=Source de données MongoDB
data.set.name=Jeu de données MongoDB
connection.profile.name=Profil de connexion à la source de données MongoDB

# Data source properties
datasource.property.mongoDbUri=URI de la base de données &Mongo
datasource.property.ignoreURI=&Ignorer URI
datasource.property.serverHost=&Hôte du serveur
datasource.property.serverPort=P&ort du serveur
datasource.property.databaseName=Nom de la base de &données
datasource.property.userName=Nom d'utili&sateur
datasource.property.password=&Mot de passe
datasource.property.socketKeepAlive=Garder l'interface de connexion &active
datasource.property.useRequestSession=Session de &requête consistante
datasource.property.group.uriElements=Uniform Resource Identifier et élém&ents de la base de données Mongo
datasource.property.group.options=Paramètre(s) de client &supplémentaire

# Data set properties - query definition
dataset.property.collectionName=Nom de la &collection
dataset.property.operationType=Type de commande d'&opération
dataset.property.operationExpr=&Expression de commande
dataset.property.selectedFields=Champs sé&lectionnés
dataset.property.findQueryExpr=Expression de re&quête
dataset.property.sortExpr=Expression de &tri
# Data set properties - advanced runtime properties
dataset.property.batchSize=Nombre d'objets de résultat maximum par &lot
dataset.property.rtMDSearchLimit=Nombre maximum de documents à chercher pour les &métadonnées d'exécution
dataset.property.numSkipDocuments=Nombre maximum de documents à i&gnorer
dataset.property.indexHints=Expression d'astuce d'inde&x
dataset.property.queryReadPreference=Préférences de lectu&re pour le curseur de requête
dataset.property.queryReadPreferenceTags=Jeu(x) de ba&lises de préférence de lecture
dataset.property.noTimeOut=Aucu&ne expiration de délai pour le curseur
dataset.property.allowsPartialResults=Autorise les résultats &partiels
dataset.property.flattenCollections=&Aplatir les collections imbriquées
#
dataset.property.value.readPrefPrimary=primaire
dataset.property.value.readPrefPrimaryPreferred=primairePréféré
dataset.property.value.readPrefSecondary=secondaire
dataset.property.value.readPrefSecondaryPreferred=secondairePréféré
dataset.property.value.readPrefNearest=le plus proche
