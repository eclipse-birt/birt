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
datasource.name=JDBC 데이터 원본
oda.data.source.name=BIRT JDBC 데이터 원본
connection.profile.name=BIRT JDBC 데이터 원본 연결 프로필

datasource.property.odaDriverClass=JDBC 드라이버 클래스
datasource.property.odaURL=JDBC 드라이버 URL
datasource.property.odaDataSource=데이터 원본
datasource.property.odaUser=사용자 이름
datasource.property.odaPassword=암호

dataset.query.sqltype=SQL Select 쿼리
dataset.query.sptype=SQL 저장된 프로시저 쿼리
dataset.property.queryTimeOut=쿼리 시간 제한(초)
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=ODA 연결 팩토리
