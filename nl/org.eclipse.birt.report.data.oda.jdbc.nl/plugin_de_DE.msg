###############################################################################
# Copyright (c) 2004, 2007 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
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
datasource.name=JDBC-Datenquelle
oda.data.source.name=JDBC-Datenquelle
connection.profile.name=BIRT-JDBC-Datenquellenverbindungsprofil

datasource.property.odaDriverClass=JDBC-Treiber&klasse
datasource.property.odaURL=JDBC-Treiber-&URL
datasource.property.odaDataSource=Datenquelle
datasource.property.odaUser=Benutzer&name
datasource.property.odaPassword=Kenn&wort
datasource.property.odaJndiURL=&JNDI-URL
datasource.property.autocommit= &Automatisch ausführen
datasource.property.isolationmode=&Isolationsmodus
dataset.query.sqltype=SQL Select-Abfrage
dataset.query.sptype=Gespeicherte SQL-Prozedurabfrage
dataset.property.queryTimeOut=Zeitüberschreitung für Ab&frage (in Sekunden)
dataset.property.rowFetchSize=&Row Fetch Size
dataset.connection.autocommit.false=falsch
dataset.connection.autocommit.true=wahr
dataset.connection.isolationmode.read-uncommitted=lesen – nicht ausgeführt
dataset.connection.isolationmode.read-committed=lesen – ausgeführt
dataset.connection.isolationmode.repeatable-read=wiederholbar – lesen
dataset.connection.isolationmode.serializable=serialisierbar
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=ODA-Verbindungs-Factory
