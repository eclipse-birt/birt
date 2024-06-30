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
plugin.name=Controlador de tiempo de ejecución ODA de fuente de datos MongoDB

data.source.name=MongoDB Data Source
data.set.name=Conjunto de datos MongoDB

connection.profile.name=Perfil de conexión de fuente de datos MongoDB

# Data source properties
datasource.property.mongoDbUri=URI de base de datos &Mongo

datasource.property.ignoreURI=&Omitir URI

datasource.property.serverHost=&Anfitrión del servidor

datasource.property.serverPort=P&uerto del servidor:

datasource.property.databaseName=&Nombre de base de datos

datasource.property.userName=Nombre de u&suario

datasource.property.password=Cont&raseña

datasource.property.socketKeepAlive=Mantener socket &activo

datasource.property.useRequestSession=Sesión de &solicitud coherente

datasource.property.group.uriElements=&Elementos URI de base de datos Mongo

datasource.property.group.options=&Ajustes suplementarios del cliente

# Data set properties - query definition
dataset.property.collectionName=Nombre de &colección

dataset.property.operationType=Tipo de &operación de comando

dataset.property.operationExpr=&Expresión de comando

dataset.property.selectedFields=Campos se&leccionados

dataset.property.findQueryExpr=&Expresión de consulta

dataset.property.sortExpr=&Expresión de ordenación
# Data set properties - advanced runtime properties
dataset.property.batchSize=Máximo de objetos de resultado en un &lote

dataset.property.rtMDSearchLimit=Máximo de documentos para buscar para &metadatos de tiempo de ejecución

dataset.property.numSkipDocuments=Máximo de documentos para o&mitir

dataset.property.indexHints=Expresión de sugeren&cia de índice

dataset.property.queryReadPreference=Preferencia de lectu&ra de cursor de consultas

dataset.property.queryReadPreferenceTags=Conjuntos de eti&quetas de preferencia de lectura

dataset.property.noTimeOut=&Sin límite de tiempo de cursor

dataset.property.allowsPartialResults=Permite resultados &parciales

dataset.property.flattenCollections=&Contraer colecciones anidadas
#
dataset.property.value.readPrefPrimary=primario

dataset.property.value.readPrefPrimaryPreferred=primaryPreferred
dataset.property.value.readPrefSecondary=secundario

dataset.property.value.readPrefSecondaryPreferred=secondaryPreferred
dataset.property.value.readPrefNearest=más cercano
