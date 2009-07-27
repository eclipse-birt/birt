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
datasource.name=Source de données JDBC
oda.data.source.name=Source de données JDBC
connection.profile.name=Profil de connexion à la source de données JDBC BIRT

datasource.property.odaDriverClass=&Classe de pilote JDBC
datasource.property.odaURL=U&RL de pilote JDBC
datasource.property.odaDataSource=Source de données
datasource.property.odaUser=&Nom d'utilisateur
datasource.property.odaPassword=&Mot de passe
datasource.property.odaJndiURL=&URL JNDI
datasource.property.autocommit= Validation &automatique
datasource.property.isolationmode=Mode &isolation
dataset.query.sqltype=Requête de sélection SQL
dataset.query.sptype=Requête de procédure stockée SQL
dataset.property.queryTimeOut=Délai d'expiration de re&quête (en secondes)
dataset.property.rowFetchSizw=&Row Fetch Size
dataset.connection.autocommit.false=faux
dataset.connection.autocommit.true=vrai
dataset.connection.isolationmode.read-uncommitted=lecture non validée
dataset.connection.isolationmode.read-committed=lecture validée
dataset.connection.isolationmode.repeatable-read=lecture renouvelable
dataset.connection.isolationmode.serializable=sérialisable
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=Génération de connexion ODA
