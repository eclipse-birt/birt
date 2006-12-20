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
birt.viewer.title.navigation=ナビゲーション
birt.viewer.title.error=エラー

birt.viewer.parameter=パラメタ
birt.viewer.runreport=レポートの実行
birt.viewer.required=アスタリスク (<FONT COLOR="red">*</FONT>) が付いているパラメタは必須です。

birt.viewer.viewinpdf=PDF 形式で表示
birt.viewer.maximize=レポートパラメタを非表示にします。
birt.viewer.restore=レポートパラメタを表示します。

birt.viewer.error=エラーメッセージ
birt.viewer.error.noparameter=このレポートにはパラメタがありません。

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=レポートの印刷
birt.viewer.toolbar.toc=目次の表示
birt.viewer.toolbar.parameter=レポートの実行
birt.viewer.toolbar.export=データのエクスポート
birt.viewer.toolbar.font=フォントの変更
birt.viewer.toolbar.enableiv=インタラクティブビューアの起動
birt.viewer.toolbar.pdf=PDF 形式でレポートを印刷する

###############################################################################
# navbar
###############################################################################
birt.viewer.navbar.first=最初のページ
birt.viewer.navbar.previous=前のページ
birt.viewer.navbar.next=次のページ
birt.viewer.navbar.last=最後のページ
birt.viewer.navbar.goto=ジャンプ
birt.viewer.navbar.lable.goto=ページ指定ジャンプ :
birt.viewer.navbar.prompt.one=表示中のページ
birt.viewer.navbar.prompt.two=/
birt.viewer.navbar.error.blankpagenum=有効なページ番号を入力してください。

###############################################################################
# progress bar
###############################################################################
birt.viewer.progressbar.prompt=処理中です。しばらくお待ちください...

###############################################################################
# Dialog 
###############################################################################
birt.viewer.dialog.ok=OK
birt.viewer.dialog.cancel=キャンセル
birt.viewer.dialog.run=レポートの実行
birt.viewer.dialog.close=閉じる

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=例外
birt.viewer.dialog.exportReport.title=レポートのエクスポート
birt.viewer.dialog.parameter.title=パラメタ
birt.viewer.dialog.simpleExportData.title=データのエクスポート

###############################################################################
# Export report dialog 
###############################################################################
birt.viewer.dialog.export.format=エクスポート形式 :
birt.viewer.dialog.export.format.pdf=PDF
birt.viewer.dialog.export.format.excel=EXCEL
birt.viewer.dialog.export.all=詳細レポートをエクスポートする
birt.viewer.dialog.export.all.detail=現在のビューで変更した内容は反映せずに、<br>すべてのレポートデータをエクスポートします。
birt.viewer.dialog.export.modified=現在の変更をレポートに反映してエクスポートする
birt.viewer.dialog.export.modified.detail=表示されているデータだけをエクスポートします。<BR>フィルタによって隠されているデータはエクスポートされません。

###############################################################################
# Export data dialog 
###############################################################################
birt.viewer.dialog.exportdata.tab.field=データフィールド
birt.viewer.dialog.exportdata.tab.filter=フィルタの適用条件
birt.viewer.dialog.exportdata.resultsets=使用可能な結果セット
birt.viewer.dialog.exportdata.availablecolumn=使用可能な列
birt.viewer.dialog.exportdata.selectedcolumn=選択済みの列
birt.viewer.dialog.exportdata.format=データは CSV 形式でエクスポートされます。
birt.viewer.dialog.exportdata.encoding=出力エンコード :
birt.viewer.dialog.exportdata.encoding.other=その他
birt.viewer.dialog.exportdata.encoding.comment=(空白の場合は PC の地域設定のエンコードを使用)
birt.viewer.dialog.exportdata.addall=すべて追加
birt.viewer.dialog.exportdata.add=追加
birt.viewer.dialog.exportdata.removeall=すべて削除
birt.viewer.dialog.exportdata.remove=削除

###############################################################################
# General exception
###############################################################################
birt.viewer.generalException.DOCUMENT_FILE_ERROR=文書ファイル {0} は存在しないか、誤りがあります。
birt.viewer.generalException.DOCUMENT_ACCESS_ERROR=文書ファイル {0} にアクセスできません。
birt.viewer.generalException.REPORT_FILE_ERROR=レポートファイル {0} は存在しないか、誤りがあります。
birt.viewer.generalException.REPORT_ACCESS_ERROR=レポートファイル {0} にアクセスできません。
birt.viewer.generalException.DOCUMENT_FILE_PROCESSING=文書ファイルを処理しています。後でやり直してください。

###############################################################################
# Report service exception
###############################################################################
birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT=データを抽出する文書ファイルがありません。
birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET=データを抽出する結果セットがありません。
birt.viewer.reportServiceException.INVALID_TOC=目次のクエリが無効です。
birt.viewer.reportServiceException.INVALID_PARAMETER=パラメタ {0} は定義されていないため、無効です。

###############################################################################
# Birt action handler exception
###############################################################################
birt.viewer.actionException.NO_REPORT_DOCUMENT=レポート文書のインスタンスが空です。
birt.viewer.actionException.INVALID_BOOKMARK=ブックマーク {0} が無効です。確認して、リンクを設定し直してください。
birt.viewer.actionException.INVALID_PAGE_NUMBER=ページ番号が無効です。 
birt.viewer.actionException.INVALID_ID_FORMAT=ID の形式が無効です (ID={0})。
birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST=セッションがタイムアウトになったか、文書ファイルがありません。

###############################################################################
# Birt soap bingding impl exception
###############################################################################
birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET=このターゲットにハンドラが見つかりません (ターゲット : {0})。

###############################################################################
# Component processor exception
###############################################################################
birt.viewer.componentProcessorException.MISSING_OPERATOR=エラー : 演算子が見つかりません。

###############################################################################
# Exception dialog
###############################################################################
birt.viewer.exceptionDialog.stackTrace=スタックトレース :
birt.viewer.exceptionDialog.showStackTrace=Show Exception Stack Trace
birt.viewer.exceptionDialog.hideStackTrace=Hide Exception Stack Trace

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=属性 ID を指定してください。
birt.viewer.taglib.INVALID_VIEWER_ID=属性 ID に無効な文字が指定されています。
birt.viewer.taglib.VIEWER_ID_DUPLICATE=属性 ID には、他で使われていないものを指定してください。
birt.viewer.taglib.NO_REPORT_SOURCE=レポートデザインまたは文書ファイルを指定してください。
birt.viewer.taglib.NO_REPORT_DOCUMENT=Reportlet ではレポート文書ファイルが必要です。
birt.viewer.taglib.NO_REQUESTER_NAME=リクエスタの名前を指定してください。
