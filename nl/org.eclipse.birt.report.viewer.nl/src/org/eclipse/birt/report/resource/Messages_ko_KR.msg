###############################################################################
# Copyright (c) 2004 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation  - initial API and implementation
###############################################################################

birt.viewer.title=BIRT Report Viewer
birt.viewer.title.navigation=탐색
birt.viewer.title.error=오류

birt.viewer.parameter=매개 변수
birt.viewer.runreport=보고서 실행
birt.viewer.required=<FONT COLOR="red">*</FONT>가 표시된 매개 변수가 필요합니다.

birt.viewer.viewinpdf=PDF에서 보기
birt.viewer.maximize=보고서 매개 변수 숨기기
birt.viewer.restore=보고서 매개 변수 표시

birt.viewer.error=오류 메시지
birt.viewer.error.noparameter=이 보고서에 대한 매개 변수가 없습니다.

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=보고서 인쇄
birt.viewer.toolbar.toc=목차 표시
birt.viewer.toolbar.parameter=보고서 실행
birt.viewer.toolbar.export=데이터 내보내기
birt.viewer.toolbar.font=글꼴 변경
birt.viewer.toolbar.enableiv=대화형 뷰어 호출
birt.viewer.toolbar.pdf=보고서를 PDF로 인쇄

###############################################################################
# navbar
###############################################################################
birt.viewer.navbar.first=첫 페이지
birt.viewer.navbar.previous=이전 페이지
birt.viewer.navbar.next=다음 페이지
birt.viewer.navbar.last=마지막 페이지
birt.viewer.navbar.goto=이동
birt.viewer.navbar.lable.goto=페이지로 이동:
birt.viewer.navbar.prompt.one=페이지 표시
birt.viewer.navbar.prompt.two=/
birt.viewer.navbar.error.blankpagenum=올바른 페이지 번호를 입력하십시오.
###############################################################################
# progress bar
###############################################################################
birt.viewer.progressbar.prompt=처리 중입니다. 잠시 기다려 주십시오...

###############################################################################
# Dialog 
###############################################################################
birt.viewer.dialog.ok=확인
birt.viewer.dialog.cancel=취소
birt.viewer.dialog.run=보고서 실행
birt.viewer.dialog.close=닫기

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=예외
birt.viewer.dialog.exportReport.title=보고서 내보내기
birt.viewer.dialog.parameter.title=매개 변수
birt.viewer.dialog.simpleExportData.title=데이터 내보내기

###############################################################################
# Export report dialog 
###############################################################################
birt.viewer.dialog.export.format=내보내기 형식:
birt.viewer.dialog.export.format.pdf=PDF
birt.viewer.dialog.export.format.excel=EXCEL
birt.viewer.dialog.export.all=상세 보고서 내보내기
birt.viewer.dialog.export.all.detail=현재 보기 수정 사항에 관계 없이<br>사용 가능한 모든 보고서 데이터가 내보내집니다.
birt.viewer.dialog.export.modified=현재 수정 사항을 포함하여 보고서 내보내기
birt.viewer.dialog.export.modified.detail=표시된 데이터만 내보내집니다. 필터링되었거나<BR>숨겨진 데이터는 내보내지지 않습니다.

###############################################################################
# Export data dialog 
###############################################################################
birt.viewer.dialog.exportdata.tab.field=데이터 필드
birt.viewer.dialog.exportdata.tab.filter=필터링 기준
birt.viewer.dialog.exportdata.resultsets=사용 가능한 결과 집합
birt.viewer.dialog.exportdata.availablecolumn=사용 가능한 열
birt.viewer.dialog.exportdata.selectedcolumn=선택한 열
birt.viewer.dialog.exportdata.format=데이터가 csv 형식으로 내보내집니다.
birt.viewer.dialog.exportdata.encoding=출력 인코딩 :
birt.viewer.dialog.exportdata.encoding.other=기타
birt.viewer.dialog.exportdata.encoding.comment=(비어 있다면 로컬 인코딩을 사용하십시오)
birt.viewer.dialog.exportdata.addall=모두 추가
birt.viewer.dialog.exportdata.add=추가
birt.viewer.dialog.exportdata.removeall=모두 제거
birt.viewer.dialog.exportdata.remove=제거

###############################################################################
# General exception
###############################################################################
birt.viewer.generalException.DOCUMENT_FILE_ERROR=문서 파일: {0}이(가) 없거나 오류가 있습니다.
birt.viewer.generalException.DOCUMENT_ACCESS_ERROR=문서 파일: {0}에 액세스할 수 없습니다.
birt.viewer.generalException.REPORT_FILE_ERROR=보고서 파일: {0}이(가) 없거나 오류가 있습니다.
birt.viewer.generalException.REPORT_ACCESS_ERROR=보고서 파일: {0}에 액세스할 수 없습니다.
birt.viewer.generalException.DOCUMENT_FILE_PROCESSING=문서 파일을 처리 중입니다. 나중에 다시 시도하십시오.

###############################################################################
# Report service exception
###############################################################################
birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT=데이터를 추출할 수 있는 문서 파일이 없습니다.
birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET=데이터를 추출할 수 있는 결과 집합이 없습니다.
birt.viewer.reportServiceException.INVALID_TOC=잘못된 TOC 쿼리
birt.viewer.reportServiceException.INVALID_PARAMETER=잘못된 매개 변수: {0}, 정의되지 않음
birt.viewer.reportServiceException.STARTUP_REPORTENGINE_ERROR=Fatal error occurred when try to startup report engine.

###############################################################################
# Birt action handler exception
###############################################################################
birt.viewer.actionException.NO_REPORT_DOCUMENT=보고서 문서 항목이 비어 있습니다.
birt.viewer.actionException.INVALID_BOOKMARK=책갈피 {0}이[가] 잘못되었습니다. 링크를 확인하여 다시 설정하십시오.
birt.viewer.actionException.INVALID_PAGE_NUMBER=잘못된 페이지 번호 
birt.viewer.actionException.INVALID_ID_FORMAT=잘못된 ID 형식. Id={0}
birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST=세션 시간을 초과했거나 사용 가능한 문서 파일이 없습니다.

###############################################################################
# Birt soap bingding impl exception
###############################################################################
birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET=이 대상에 대한 처리기가 없습니다. 대상: {0}

###############################################################################
# Component processor exception
###############################################################################
birt.viewer.componentProcessorException.MISSING_OPERATOR=오류: 연산자 누락!!!

###############################################################################
# Exception dialog
###############################################################################
birt.viewer.exceptionDialog.stackTrace=추적 누적:
birt.viewer.exceptionDialog.showStackTrace=Show Exception Stack Trace
birt.viewer.exceptionDialog.hideStackTrace=Hide Exception Stack Trace

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=특성 ID를 지정해야 합니다.
birt.viewer.taglib.INVALID_VIEWER_ID=특성 ID에 잘못된 문자가 포함되어 있습니다.
birt.viewer.taglib.VIEWER_ID_DUPLICATE=특성 ID는 고유해야 합니다.
birt.viewer.taglib.NO_REPORT_SOURCE=보고서 디자인 또는 문서 파일을 지정해야 합니다.
birt.viewer.taglib.NO_REPORT_DOCUMENT=리포틀릿은 보고서 문서 파일이 필요합니다.
birt.viewer.taglib.NO_REQUESTER_NAME=요청자 이름을 지정해야 합니다.
