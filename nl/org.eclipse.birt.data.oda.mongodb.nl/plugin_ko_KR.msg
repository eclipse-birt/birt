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
plugin.name=MongoDB 데이터 원본 ODA 런타임 드라이버
data.source.name=MongoDB 데이터 원본
data.set.name=MongoDB 데이터 집합
connection.profile.name=MongoDB 데이터 원본 연결 프로필

# Data source properties
datasource.property.mongoDbUri=Mongo 데이터베이스 URI(&M)
datasource.property.ignoreURI=URI 무시(&I)
datasource.property.serverHost=서버 호스트(&H)
datasource.property.serverPort=서버 포트(&O)
datasource.property.databaseName=데이터베이스 이름(&D)
datasource.property.userName=사용자 이름(&S)
datasource.property.password=암호(&W)
datasource.property.socketKeepAlive=소켓 연결 유지(&A)
datasource.property.useRequestSession=일관된 요청 세션(&R)
datasource.property.group.uriElements=Mongo 데이터베이스 URI 요소(&E)
datasource.property.group.options=추가 클라이언트 설정(&S)

# Data set properties - query definition
dataset.property.collectionName=컬렉션 이름(&C)
dataset.property.operationType=명령 연산 형식(&O)
dataset.property.operationExpr=명령 식(&E)
dataset.property.selectedFields=선택한 필드(&L)
dataset.property.findQueryExpr=쿼리 식(&Q)
dataset.property.sortExpr=정렬 식(&S)
# Data set properties - advanced runtime properties
dataset.property.batchSize=일괄 처리의 최대 결과 개체 수(&B)
dataset.property.rtMDSearchLimit=런타임 메타데이터를 검색할 최대 문서 수(&M)
dataset.property.numSkipDocuments=건너뛸 최대 문서 수(&K)
dataset.property.indexHints=힌트 식 인덱스(&H)
dataset.property.queryReadPreference=쿼리 커서 읽기 기본 설정(&R)
dataset.property.queryReadPreferenceTags=읽기 기본 설정 태그 집합(&G)
dataset.property.noTimeOut=커서 시간 제한 없음(&N)
dataset.property.allowsPartialResults=일부 결과 허용(&P)
dataset.property.flattenCollections=중첩 컬렉션 결합(&F)
#
dataset.property.value.readPrefPrimary=기본
dataset.property.value.readPrefPrimaryPreferred=기본 선호
dataset.property.value.readPrefSecondary=보조
dataset.property.value.readPrefSecondaryPreferred=보조 선호
dataset.property.value.readPrefNearest=근접
