###############################################################################
# Copyright (c) 2004, 2007 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-2.0.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#
###############################################################################
# Plug-in Configuration
#
oda.data.source.id=org.eclipse.birt.report.data.oda.jdbc
oda.parent.category.id=org.eclipse.datatools.connectivity.oda.profileCategory
#
###############################################################################
# NLS String
###############################################################################
datasource.name=Fuente de datos JDBC

oda.data.source.name=Fuente de datos JDBC

connection.profile.name=BIRT JDBC Data Source Connection Profile
datasource.property.odaDriverClass=&Clase del controlador JDBC

datasource.property.odaURL=UR&L del controlador JDBC

datasource.property.odaDataSource=Fuente de datos

datasource.property.odaUser=&Nombre de usuario

datasource.property.odaPassword=Cont&raseña

datasource.property.odaJndiURL=&JNDI URL
datasource.property.autocommit= &Confirmar automáticamente

datasource.property.isolationmode=&Modo de aislamiento

dataset.query.sqltype=Consulta de selección de SQL

dataset.query.sptype=Consulta de stored procedure de SQL

dataset.property.queryTimeOut=Tiem&po límite de consulta (en segundos)

dataset.property.rowFetchSize=Tamaño de recuperación de &fila

dataset.connection.autocommit.false=false

dataset.connection.autocommit.true=true

dataset.connection.isolationmode.read-uncommitted=lectura confirmada

dataset.connection.isolationmode.read-committed=lectura no confirmada

dataset.connection.isolationmode.repeatable-read=lectura repetible

dataset.connection.isolationmode.serializable=serializable
               
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=ODA Connection Factory
datasource.property.ssl.trustStorePassword=Contra&señadeTrustStore:

datasource.property.ssl.keyStorePassword=ContraseñaKe&yStore:

