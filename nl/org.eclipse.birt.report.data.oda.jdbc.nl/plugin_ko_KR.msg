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
oda.data.source.name=JDBC 데이터 원본
connection.profile.name=BIRT JDBC 데이터 원본 연결 프로필

datasource.property.odaDriverClass=JDBC 드라이버 클래스(&C)
datasource.property.odaURL=JDBC 드라이버 URL(&R)
datasource.property.odaDataSource=데이터 원본
datasource.property.odaUser=사용자 이름(&N)
datasource.property.odaPassword=암호(&W)
datasource.property.odaJndiURL=&JNDI URL
datasource.property.autocommit= 자동 커밋(&A)
datasource.property.isolationmode=격리 모드(&I)
dataset.query.sqltype=SQL Select 쿼리
dataset.query.sptype=SQL 저장된 프로시저 쿼리
dataset.property.queryTimeOut=쿼리 시간 제한(초)(&Q)
dataset.property.rowFetchSizw=&Row Fetch Size
dataset.connection.autocommit.false=거짓
dataset.connection.autocommit.true=참
dataset.connection.isolationmode.read-uncommitted=커밋되지 않은 읽기
dataset.connection.isolationmode.read-committed=커밋된 읽기
dataset.connection.isolationmode.repeatable-read=반복 읽기
dataset.connection.isolationmode.serializable=직렬화
#
#  Below NLS messages apply to all ODA data sources; should be re-factored
#
oda.connection.factory.name=ODA 연결 팩토리
