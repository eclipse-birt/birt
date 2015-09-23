# Localized messages for the Design Engine. Messages here give names
# of items in the Report Object Model, and the text of Design Engine
# errors.


###########################################################
#for test only
Error.Msg001={1} に {0} が見つかりません。

###########################################################
# Boolean type names
Property.Boolean.True=true

Property.Boolean.False=false

###########################################################
# Property type names
Property.boolean=ブーリアン

Property.choice=選択肢

Property.float=浮動

Property.color=カラー

Property.dateTime=DateTime
Property.dimension=ディメンション

Property.expression=式

Property.html=Html
Property.integer=整数

Property.messageId=MessageId

Property.name=名前

Property.number=番号

Property.points=ポイント

Property.string=文字列

Property.uri=Uri
Property.xml=Xml
Property.elementRef=要素参照

Property.column=列

Property.variant=バリアント

Property.structList=構造リスト

Property.extends=拡張

Property.script=スクリプト

Property.list=リスト

Property.memberKey=メンバキー

Property.literalString=リテラル文字列

Property.element=要素

Property.structRef=構造参照

Property.contentElement=プロパティ値としての要素

Property.locale=ロケール

Property.struct=構造体

###########################################################
# Choice values
  
#0.colors
Choices.colors.maroon=えび茶色

Choices.colors.red=赤

Choices.colors.orange=オレンジ

Choices.colors.yellow=黄

Choices.colors.olive=オリーブ

Choices.colors.purple=紫

Choices.colors.fuchsia=赤紫

Choices.colors.white=白

Choices.colors.lime=黄緑

Choices.colors.green=緑

Choices.colors.navy=紺

Choices.colors.blue=青

Choices.colors.aqua=水色

Choices.colors.teal=青緑

Choices.colors.black=黒

Choices.colors.silver=シルバー

Choices.colors.gray=灰色
  
#1.fontWeight
Choices.fontWeight.normal=標準

Choices.fontWeight.bold=太字

Choices.fontWeight.bolder=極太

Choices.fontWeight.lighter=細字

Choices.fontWeight.100=100

Choices.fontWeight.200=200

Choices.fontWeight.300=300

Choices.fontWeight.400=400

Choices.fontWeight.500=500

Choices.fontWeight.600=600

Choices.fontWeight.700=700

Choices.fontWeight.800=800

Choices.fontWeight.900=900

#2.textUnderline
Choices.textUnderline.none=下線なし

Choices.textUnderline.underline=下線
  
#3.lineStyle
Choices.lineStyle.none=線スタイルなし

Choices.lineStyle.solid=実線

Choices.lineStyle.dotted=点線

Choices.lineStyle.dashed=破線

Choices.lineStyle.double=二重線

Choices.lineStyle.groove=くぼみ

Choices.lineStyle.ridge=浮き出し

Choices.lineStyle.inset=インセット

Choices.lineStyle.outset=アウトセット
  
#4.units
Choices.units.in=インチ

Choices.units.cm=センチ

Choices.units.mm=ミリ

Choices.units.pt=ポイント

Choices.units.pc=パイカ

Choices.units.em=エム

Choices.units.ex=エックスハイト

Choices.units.px=ピクセル

Choices.units.%=%

#5.paramDataType
Choices.paramType.string=文字列

Choices.paramType.float=浮動小数値

Choices.paramType.decimal=10 進数値

Choices.paramType.dateTime=日付/時間

Choices.paramType.boolean=論理

Choices.paramType.integer=整数値

Choices.paramType.any=任意

Choices.paramType.date=日付

Choices.paramType.time=時間

Choices.paramType.javaObject=Java オブジェクト

#6.paramControl
Choices.paramControl.text-box=テキストボックス

Choices.paramControl.list-box=コンボボックス/リストボックス

Choices.paramControl.radio-button=ラジオボタン

Choices.paramControl.check-box=チェックボックス

Choices.paramControl.auto-suggest=テキストボックス - 入力補完

#7.textAlign
Choices.textAlign.left=左揃え

Choices.textAlign.center=中央揃え

Choices.textAlign.right=右揃え

Choices.textAlign.justify=両端揃え
  
#8.pageSize
Choices.pageSize.custom=カスタム

Choices.pageSize.us-letter=レター

Choices.pageSize.us-legal=リーガル

Choices.pageSize.a4=A4
Choices.pageSize.a3=A3
Choices.pageSize.a5=A5
Choices.pageSize.us-ledger=レジャー

Choices.pageSize.us-super-b=Super B
  
#9.pageOrientation
Choices.pageOrientation.auto=自動

Choices.pageOrientation.portrait=縦

Choices.pageOrientation.landscape=横
   
#12.interval
Choices.interval.none=間隔なし

Choices.interval.prefix=接頭辞

Choices.interval.year=年

Choices.interval.quarter=四半期

Choices.interval.month=月

Choices.interval.week=週

Choices.interval.day=日

Choices.interval.hour=時

Choices.interval.minute=分

Choices.interval.second=秒

Choices.interval.interval=間隔

#interval type
Choices.intervalType.none=間隔なし

Choices.intervalType.prefix=接頭辞

Choices.intervalType.interval=間隔

#13.sortDirection
Choices.sortDirection.asc=昇順

Choices.sortDirection.desc=降順
  
#14.mapOperator
Choices.mapOperator.eq=次の値に等しい

Choices.mapOperator.ne=次の値に等しくない

Choices.mapOperator.lt=次の値より小さい

Choices.mapOperator.le=次の値以下

Choices.mapOperator.ge=次の値以上

Choices.mapOperator.gt=次の値より大きい

Choices.mapOperator.between=次の値の間

Choices.mapOperator.notBetween=次の値の間以外

Choices.mapOperator.isNull=Null

Choices.mapOperator.isNotNull=Not Null

Choices.mapOperator.isTrue=True

Choices.mapOperator.isFalse=False

Choices.mapOperator.like=次の値に類似

Choices.mapOperator.match=次にマッチする

Choices.mapOperator.any=任意

Choices.mapOperator.top-n=上位 n

Choices.mapOperator.bottom-n=下位 n

Choices.mapOperator.top-percent=上位 %

Choices.mapOperator.bottom-percent=下位 %

Choices.mapOperator.not-like=次の値に類似しない

Choices.mapOperator.not-match=次にマッチしない

Choices.mapOperator.not-in=次に含まれない

Choices.mapOperator.in=次に含まれる
  
#15.imageRefType
Choices.imageRefType.none=イメージなし

Choices.imageRefType.url=URL
Choices.imageRefType.file=ファイル

Choices.imageRefType.expr=式

Choices.imageRefType.embed=埋め込まれたイメージ
  
#16.imageSize
Choices.imageSize.size-to-image=イメージに合わせる

Choices.imageSize.scale-to-item=項目に合わせる

Choices.imageSize.clip=クリップ
  
#17.lineOrientation
Choices.lineOrientation.horizontal=横

Choices.lineOrientation.vertical=縦
  
#18.rowAlign
Choices.rowAlign.top=上端揃え

Choices.rowAlign.middle=中央揃え (上下)

Choices.rowAlign.bottom=下端揃え
  
#19.sectionAlign
Choices.sectionAlign.left=左揃え

Choices.sectionAlign.center=中央揃え (左右)

Choices.sectionAlign.right=右揃え
  
#21.dropType
Choices.dropType.none=なし

Choices.dropType.detail=詳細

Choices.dropType.all=すべて
    
#22.imageType
Choices.imageType.image/bmp=イメージ/bmp

Choices.imageType.image/gif=イメージ/gif

Choices.imageType.image/png=イメージ/png

Choices.imageType.image/x-png=イメージ/x-png

Choices.imageType.image/jpeg=イメージ/jpeg

Choices.imageType.image/ico=イメージ/ico

Choices.imageType.image/tiff=イメージ/tiff

Choices.imageType.image/svg+xml=イメージ/svg+xml

Choices.imageType.image/x-icon=イメージ/x アイコン
  
#23.lineSpacing
Choices.lineSpacing.lines=線

Choices.lineSpacing.exact=固定値
  
#24.lineType
Choices.lineType.none=線種なし

Choices.lineType.solid=実線

Choices.lineType.dotted=点線

Choices.lineType.dashed=破線

Choices.lineType.double=二重線
  
#25.pageBreakBefore
Choices.pageBreakBefore.auto=自動

Choices.pageBreakBefore.always=強制

Choices.pageBreakBefore.avoid=禁止

Choices.pageBreakBefore.alwaysExcludingFirst=強制（1ページ目除く）

#26.pageBreakAfter
Choices.pageBreakAfter.auto=自動

Choices.pageBreakAfter.always=強制

Choices.pageBreakAfter.avoid=禁止

Choices.pageBreakAfter.alwaysExcludingLast=強制（最後のページを除く）

#27.actionLinkType
Choices.actionLinkType.none=アクションリンクの種類なし

Choices.actionLinkType.hyperlink=HyperLink
Choices.actionLinkType.drill-through=ドリルスルー

Choices.actionLinkType.bookmark-link=ブックマークリンク
  
# actionFormatType
Choices.actionFormatType.html=html
Choices.actionFormatType.pdf=pdf
  
#28.drillThroughLinkType
Choices.drillThroughType.none=ドリルスルーリンクの種類なし

Choices.drillThroughType.bookmark-link=ブックマークリンク

Choices.drillThroughType.search=検索
 
#29.fontSize
Choices.fontSize.x-small=極小

Choices.fontSize.xx-small=超極小

Choices.fontSize.small=小

Choices.fontSize.medium=中

Choices.fontSize.large=大

Choices.fontSize.x-large=特大

Choices.fontSize.xx-large=超特大

Choices.fontSize.larger=拡大

Choices.fontSize.smaller=縮小

#30.fontFamily
Choices.fontFamily.serif=MS P 明朝

Choices.fontFamily.sans-serif=MS P ゴシック

Choices.fontFamily.cursive=手書きフォント

Choices.fontFamily.fantasy=装飾フォント

Choices.fontFamily.monospace=固定ピッチフォント

#31.fontStyle
Choices.fontStyle.normal=標準

Choices.fontStyle.italic=イタリック体

Choices.fontStyle.oblique=斜体

#32.fontVariant
Choices.fontVariant.normal=標準

Choices.fontVariant.small-caps=小型英大文字

#33.backgroundRepeat
Choices.backgroundRepeat.repeat=繰り返し

Choices.backgroundRepeat.repeat-x=X の繰り返し

Choices.backgroundRepeat.repeat-y=Y の繰り返し

Choices.backgroundRepeat.no-repeat=繰り返さない

#34.backgroundAttachment
Choices.backgroundAttachment.scroll=スクロール

Choices.backgroundAttachment.fixed=固定

#35.backgroundPositionX
Choices.backgroundPositionX.left=左揃え

Choices.backgroundPositionX.center=中央揃え (左右)

Choices.backgroundPositionX.right=右揃え

#36.backgroundPositionY
Choices.backgroundPositionY.top=上端揃え

Choices.backgroundPositionY.center=中央揃え (上下)

Choices.backgroundPositionY.bottom=下端揃え

#37.transform
Choices.transform.capitalize=先頭文字のみ大文字

Choices.transform.uppercase=大文字

Choices.transform.lowercase=小文字

Choices.transform.none=変換なし

#38.normal
Choices.normal.normal=標準

#39.verticalAlign
Choices.verticalAlign.baseline=ベースライン

Choices.verticalAlign.sub=下付き

Choices.verticalAlign.super=上付き

Choices.verticalAlign.top=上

Choices.verticalAlign.text-top=テキスト上揃え

Choices.verticalAlign.middle=中央揃え (上下)

Choices.verticalAlign.bottom=下

Choices.verticalAlign.text-bottom=テキスト下揃え

#40.whiteSpace
Choices.whiteSpace.normal=標準

Choices.whiteSpace.pre=書式設定済み

Choices.whiteSpace.nowrap=折り返しなし

#41.display
Choices.display.block=ブロック

Choices.display.inline=インライン

Choices.display.none=表示なし

#43.pageBreakInside
Choices.pageBreakInside.avoid=禁止

Choices.pageBreakInside.auto=自動

#44.margin
Choices.margine.auto=自動

#45.textOverline
Choices.textOverline.none=上線なし

Choices.textOverline.overline=上線

#46.textLineThrough
Choices.textLineThrough.none=取り消し線なし

Choices.textLineThrough.line-through=取り消し線

#48.lineWidth
Choices.lineWidth.thin=細

Choices.lineWidth.medium=中

Choices.lineWidth.thick=太

#49.inherit
Choices.inherit.inherit=継承

#50. textContentType
Choices.textContentType.auto=自動

Choices.textContentType.plain=標準のテキスト形式

Choices.textContentType.html=HTML
Choices.textContentType.rtf=RTF

#51. formatType
Choices.formatType.all=すべて

Choices.formatType.viewer=ビューア

Choices.formatType.email=メール

Choices.formatType.print=印刷

Choices.formatType.pdf=PDF

Choices.formatType.rtf=RTF

Choices.formatType.reportlet=レポートレット

Choices.formatType.excel=Excel
Choices.formatType.word=Word
Choices.formatType.powerpoint=Powerpoint

#52. filterOperator
Choices.filterOperator.eq=次の値に等しい

Choices.filterOperator.ne=次の値に等しくない

Choices.filterOperator.lt=次の値より小さい

Choices.filterOperator.le=次の値以下

Choices.filterOperator.ge=次の値以上

Choices.filterOperator.gt=次の値より大きい

Choices.filterOperator.between=次の値の間

Choices.filterOperator.notBetween=次の値の間以外

Choices.filterOperator.isNull=Null

Choices.filterOperator.isNotNull=Not Null

Choices.filterOperator.isTrue=True

Choices.filterOperator.isFalse=False

Choices.filterOperator.like=次の値に類似

Choices.filterOperator.not-like=次の値に類似しない

Choices.filterOperator.top-n=上位 n

Choices.filterOperator.bottom-n=下位 n

Choices.filterOperator.top-percent=上位 %

Choices.filterOperator.bottom-percent=下位 %

Choices.filterOperator.any=任意

Choices.filterOperator.match=次にマッチする

Choices.filterOperator.not-match=次にマッチしない

Choices.filterOperator.in=次に含まれる

Choices.filterOperator.not-in=次に含まれない

#53. propertyMaskType
Choices.propertyMaskType.change=変更

Choices.propertyMaskType.lock=固定

Choices.propertyMaskType.hide=非表示

#53. columnDataType
Choices.columnDataType.any=任意

Choices.columnDataType.integer=整数

Choices.columnDataType.string=文字列

Choices.columnDataType.date-time=日時

Choices.columnDataType.decimal=10 進数値

Choices.columnDataType.float=浮動小数値

Choices.columnDataType.structure=構造

Choices.columnDataType.table=テーブル

Choices.columnDataType.boolean=論理

Choices.columnDataType.time=時間

Choices.columnDataType.date=日付

Choices.columnDataType.blob=Blob

Choices.columnDataType.javaObject=Java オブジェクト

#54. searchType
Choices.searchType.any=任意

Choices.searchType.indexed=インデックス付き

Choices.searchType.none=検索の種類なし

#55. exportType
Choices.exportType.none=エクスポートの種類なし

Choices.exportType.if-realized=一致したページ

Choices.exportType.always=強制

#56. analysisType
Choices.analysisType.dimension=ディメンション

Choices.analysisType.attribute=属性

Choices.analysisType.measure=メジャー

#57. scalarParamAlign
Choices.scalarParamAlign.auto=自動

Choices.scalarParamAlign.left=左揃え

Choices.scalarParamAlign.center=中央揃え (左右)

Choices.scalarParamAlign.right=右揃え

#58. columnAlign
Choices.columnAlign.left=左揃え

Choices.columnAlign.center=中央揃え (左右)

Choices.columnAlign.right=右揃え

#59. dateTimeFormat
Choices.dateTimeFormat.unformatted=書式未設定

Choices.dateTimeFormat.generalDate=日付 (標準)

Choices.dateTimeFormat.longDate=日付 (L)

Choices.dateTimeFormat.mediumDate=日付 (M)

Choices.dateTimeFormat.shortDate=日付 (S)

Choices.dateTimeFormat.longTime=時刻 (L)

Choices.dateTimeFormat.mediumTime=時刻 (M)

Choices.dateTimeFormat.shortTime=時刻 (S)

Choices.dateTimeFormat.custom=カスタム

#59. dateFormat
Choices.dateFormat.unformatted=書式未設定

Choices.dateFormat.generalDate=日付 (標準)

Choices.dateFormat.longDate=日付 (L)

Choices.dateFormat.mediumDate=日付 (M)

Choices.dateFormat.shortDate=日付 (S)

Choices.dateFormat.custom=カスタム

#59. timeFormat
Choices.timeFormat.unformatted=書式未設定

Choices.timeFormat.longTime=時刻 (L)

Choices.timeFormat.mediumTime=時刻 (M)

Choices.timeFormat.shortTime=時刻 (S)

Choices.timeFormat.custom=カスタム
    
#60. numberFormat
Choices.numberFormat.unformatted=書式未設定

Choices.numberFormat.generalNumber=数値

Choices.numberFormat.currency=通貨

Choices.numberFormat.fixed=固定

Choices.numberFormat.percent=パーセント

Choices.numberFormat.scientific=指数

Choices.numberFormat.standard=標準

Choices.numberFormat.custom=カスタム
    
#59. StringFormat
Choices.stringFormat.unformatted=書式未設定

Choices.stringFormat.upper=大文字

Choices.stringFormat.lower=小文字

Choices.stringFormat.zipCode=郵便番号

Choices.stringFormat.zipCode+4=郵便番号 + 4

Choices.stringFormat.phoneNumber=電話番号

Choices.stringFormat.socialSecurityNumber=社会保険番号

Choices.stringFormat.custom=カスタム

#60. targetNames
Choices.targetNames.blank=空白

Choices.targetNames.self=自身

Choices.targetNames.parent=親

Choices.targetNames.top=上

#50. textDataContentType
Choices.textDataContentType.auto=自動

Choices.textDataContentType.plain=標準のテキスト形式

Choices.textDataContentType.html=HTML
Choices.textDataContentType.rtf=RTF

#51. paramaValueType
Choices.paramValueType.static=静的

Choices.paramValueType.dynamic=動的

#52. templateElementType
Choices.templateElementType.table=テーブル

Choices.templateElementType.freeForm=FreeForm
Choices.templateElementType.data=データ

Choices.templateElementType.grid=グリッド

Choices.templateElementType.image=イメージ

Choices.templateElementType.label=ラベル

Choices.templateElementType.list=リスト

Choices.templateElementType.text=テキスト

Choices.templateElementType.extendedItem=ExtendedItem
Choices.templateElementType.textData=TextData
Choices.templateElementType.dataSet=データセット

#61. sortType
Choices.sortType.none=ソート基準なし

Choices.sortType.sortOnGroupkey=グループキーでソート

Choices.sortType.complexSort=高度なソート

#62. joinType
Choices.joinType.inner=インナージョイン

Choices.joinType.leftOut=左アウタージョイン

Choices.joinType.rightOut=右アウタージョイン

Choices.joinType.fullOut=完全アウタージョイン

#63. joinOperator
Choices.joinOperator.equals=等しい

#64. actionTargetFileType
Choices.actionTargetFileType.report-design=レポートデザイン

Choices.actionTargetFileType.report-document=レポート文書

#65. actionBookmarkType
Choices.actionBookmarkType.bookmark=ブックマーク

Choices.actionBookmarkType.toc=目次

#66. autoTextType
Choices.autoTextType.page-number=ページ番号

Choices.autoTextType.total-page=合計ページ数

Choices.autoTextType.page-number-unfiltered=フィルタされていないページ番号

Choices.autoTextType.total-page-unfiltered=フィルタされていない合計ページ数

Choices.autoTextType.page-variable=ページ変数

#67. dataSetMode
Choices.dataSetMode.single=1 つのデータセット

Choices.dataSetMode.multiple=複数のデータセット

#68. filterTarget
Choices.filterTarget.DataSet=データセット

Choices.filterTarget.ResultSet=結果セット

Choices.filterTarget.OdaDataSet=ODA データセット

#69. viewAction
Choices.viewAction.NoChange=変更なし

Choices.viewAction.Changed=変更済み

Choices.viewAction.Added=追加済み

Choices.viewAction.Deleted=削除済み

#70. aggregationFunction
Choices.aggregationFunction.sum=SUM

Choices.aggregationFunction.count=COUNT

Choices.aggregationFunction.min=MIN

Choices.aggregationFunction.max=MAX

Choices.aggregationFunction.average=AVERAGE

Choices.aggregationFunction.weighted-avg=WEIGHTEDAVG

Choices.aggregationFunction.stddev=STDDEV

Choices.aggregationFunction.first=FIRST

Choices.aggregationFunction.last=LAST

Choices.aggregationFunction.mode=MODE

Choices.aggregationFunction.moving-ave=MOVINGAVE

Choices.aggregationFunction.median=MEDIAN

Choices.aggregationFunction.variance=VARIANCE

Choices.aggregationFunction.running-sum=RUNNINGSUM

Choices.aggregationFunction.irr=IRR

Choices.aggregationFunction.mirr=MIRR

Choices.aggregationFunction.npv=NPV

Choices.aggregationFunction.running-npv=RUNNINGNPV

Choices.aggregationFunction.count-distinct=COUNTDISTINCT

Choices.aggregationFunction.running-count=RUNNINGCOUNT

Choices.aggregationFunction.is-top-n=IS-TOP-N

Choices.aggregationFunction.is-bottom-n=IS-BOTTOM-N

Choices.aggregationFunction.is-top-n-percent=IS-TOP-N-PERCENT

Choices.aggregationFunction.is-bottom-n-percent=IS-BOTTOM-N-PERCENT

Choices.aggregationFunction.percent-rank=PERCENT-RANK

Choices.aggregationFunction.percentile=PERCENTILE

Choices.aggregationFunction.quartile=QUARTILE

Choices.aggregationFunction.percent-sum=PERCENT-SUM

Choices.aggregationFunction.rank=RANK

#71. levelType
Choices.levelType.dynamic=動的

Choices.levelType.mirrored=ミラー化

#72. accessPermission
Choices.accessPermission.allow=許可する

Choices.accessPermission.disallow=許可しない

#73. paramSortValues
Choices.paramSortValues.value=値

Choices.paramSortValues.label=表示テキスト

#74. reportLayoutPreference
Choices.reportLayoutPreference.fixedLayout=固定レイアウト

Choices.reportLayoutPreference.autoLayout=自動レイアウト

#75. dateTimeLevelType
Choices.dateTimeLevelType.year=年

Choices.dateTimeLevelType.month=月

Choices.dateTimeLevelType.quarter=四半期

Choices.dateTimeLevelType.weekOfMonth=暦週 (月)

Choices.dateTimeLevelType.weekOfYear=暦週 (年)

Choices.dateTimeLevelType.dayOfWeek=通日 (週)

Choices.dateTimeLevelType.dayOfMonth=通日 (月)

Choices.dateTimeLevelType.dayOfYear=通日 (年)

Choices.dateTimeLevelType.hour=時

Choices.dateTimeLevelType.minute=分

Choices.dateTimeLevelType.second=秒

#76. scalarParamType
Choices.scalarParamType.simple=簡易

Choices.scalarParamType.multi-value=複数の値

#77. bidiLayoutOrientation   
Choices.bidiDirection.rtl=右から左

Choices.bidiDirection.ltr=左から右

#78. backgroundSize
Choices.backgroundSize.auto=自動

Choices.backgroundSize.contain=含める

Choices.backgroundSize.cover=補う

#79 scopeType
Choices.scopeType.row=行

Choices.scopeType.col=列

Choices.scopeType.rowgroup=行グループ

Choices.scopeType.colgroup=列グループ

#80 dynamicFilterChoice
Choices.dynamicFilterChoice.simple=簡易

Choices.dynamicFilterChoice.advanced=詳細設定

#81 variableType
Choices.variableType.report=レポート変数

Choices.variableType.page=ページ変数

#82 nullValueOrderingType
Choices.nullValueOrderingType.unknown=不明

Choices.nullValueOrderingType.nullIsFirst=Null が最初

Choices.nullValueOrderingType.nullIsLast=Null が最後

#83 dataVersion
Choices.dataVersion.transient=一時的

Choices.dataVersion.latest=最新

#84 selectorType
Choices.selectorType.list=リスト

Choices.selectorType.dropdown=ドロップダウンリスト

Choices.selectorType.slider=スライダ

Choices.selectorType.checkBox=CheckBox

Choices.selectorType.radioButton=RadioButton
Choices.selectorType.textBox=TextBox

Choices.selectorType.calendar=カレンダ

Choices.selectorType.currentSelections=現在の選択

#85 thumbType
Choices.thumbType.single=1 つ

Choices.thumbType.dual=2 つ

#86
Choices.accessType.transient=一時的

Choices.accessType.latest=最新

Choices.accessType.specificVersion=SpecificVersion

#87 overflow
Choices.overflow.visible=表示

Choices.overflow.hidden=非表示

Choices.overflow.scroll=スクロール

Choices.overflow.auto=自動

#88 GadgetContentType
Choices.gadgetContentType.html=HTML
Choices.gadgetContentType.url=URL
Choices.gadgetContentType.video=ビデオ

Choices.gadgetContentType.videourl=ビデオの URL

Choices.gadgetContentType.text=テキスト

Choices.gadgetContentType.image=イメージ

#89 WindowStatus
Choices.windowStatus.normal=標準

Choices.windowStatus.maximized=最大化

#90 reportItemThemeType
Choices.reportItemThemeType.Table=テーブルテーマ

Choices.reportItemThemeType.List=リストテーマ

Choices.reportItemThemeType.Grid=グリッドテーマ

#91 nullsOrdering
Choices.nullsOrdering.nullsLowest=Null (最低)

Choices.nullsOrdering.nullsHighest=Null (最高)

Choices.nullsOrdering.excludeNulls=Null を除外する

#92 filterConditionType
Choices.filterConditionType.slicer=スライサ

Choices.filterConditionType.simple=簡易

#93 flyoutPosition
Choices.flyoutPosition.left=左

Choices.flyoutPosition.right=右

#94 referenceDateType
Choices.referenceDateType.today=今日

Choices.referenceDateType.fixedDate=固定された日付

Choices.referenceDateType.endingDateInDimension=ディメンション内の終了日付

###########################################################
# Classes

Class.optionalArgument=...

#0.String
Class.String=String 関数

Class.String.toolTip=Object 型のメンバと組み込みの String オブジェクトのインスタンス

Class.String.String=文字列

Class.String.String.toolTip=ToString(value) で計算された文字列値を返します

Class.String.length=length

Class.String.length.toolTip=文字列の長さを返します

Class.String.fromCharCode=fromCharCode
Class.String.fromCharCode.toolTip=Unicode 文字から成る文字列値を返します

Class.String.charAt=charAt

Class.String.charAt.toolTip=String オブジェクトの指定されたインデックスの文字を返します

Class.String.charCodeAt=charCodeAt

Class.String.charCodeAt.toolTip=String オブジェクトの指定された場所にある文字の Unicode エンコードを示す整数を返します

Class.String.concat=concat

Class.String.concat.toolTip=現在の文字列と指定された文字列の結合を含む文字列値を返します

Class.String.indexOf=indexOf

Class.String.indexOf.toolTip=String オブジェクト内で一番最初に検出された部分文字列を返します

Class.String.lastIndexOf=lastIndexOf

Class.String.lastIndexOf.toolTip=String オブジェクト内で一番最後に検出された部分文字列を返します

Class.String.localeCompare=localeCompare
Class.String.localeCompare.toolTip=2 つの文字列が現在のロケールで同等であるかどうかを示す値を返します

Class.String.match=match

Class.String.match.toolTip=正規表現パターンを使用した文字列を検索し、その結果の配列を返します

Class.String.replace=replace

Class.String.replace.toolTip=正規表現または検索文字列を使用してテキストが置換された文字列のコピーを返します

Class.String.search=search

Class.String.search.toolTip=正規表現検索で最初に検出された部分文字列の位置を返します

Class.String.slice=slice

Class.String.slice.toolTip=このオブジェクトを文字列に変換した結果の部分文字列を返します

Class.String.split=split

Class.String.split.toolTip=文字列を部分文字列に分割した結果の文字列の配列を返します

Class.String.substr=substr

Class.String.substr.toolTip=指定した場所から始まり、指定した長さを持つ部分文字列を返します

Class.String.substring=substring

Class.String.substring.toolTip=String オブジェクト内の指定した場所にある部分文字列を返します

Class.String.toLowerCase=toLowerCase

Class.String.toLowerCase.toolTip=文字列内の欧文文字を 1 つずつ小文字に変換します。

Class.String.toLocaleLowerCase=toLocaleLowerCase

Class.String.toLocaleLowerCase.toolTip=ホスト環境の現在のロケールに基づき、すべての欧文文字を小文字に変換した文字列を返します

Class.String.toUpperCase=toUpperCase

Class.String.toUpperCase.toolTip=文字列内の文字を 1 つずつ大文字に変換します。

Class.String.toLocaleUpperCase=toLocaleUpperCase

Class.String.toLocaleUpperCase.toolTip=ホスト環境の現在のロケールに基づき、すべての欧文文字を大文字に変換した文字列を返します

Class.String.toString=toString
Class.String.toString.toolTip=この文字列値を返します

Class.String.valueOf=valueOf
Class.String.valueOf.toolTip=この文字列値を返します

Class.String.String.object=オブジェクト

Class.String.fromCharCode.c=c

Class.String.charAt.n=n
Class.String.charCodeAt.n=n
Class.String.concat.value=value

Class.String.indexOf.substring=substring

Class.String.indexOf.start=start

Class.String.lastIndexOf.substring=substring

Class.String.lastIndexOf.start=start

Class.String.localeCompare.target=target

Class.String.match.regexp=正規表現

Class.String.replace.regexp=正規表現

Class.String.replace.replacement=置換

Class.String.search.regexp=正規表現

Class.String.slice.start=start

Class.String.slice.end=end

Class.String.split.delimitor=区切り文字

Class.String.split.limit=制限

Class.String.substr.start=start

Class.String.substr.length=length

Class.String.substring.from=from

Class.String.substring.to=?

#1.Global
Class.Global=Global 関数

Class.Global.toolTip=グローバルメソッドを 1 つのオブジェクトに収集するための固有のオブジェクト

Class.Global.NaN=NaN

Class.Global.NaN.toolTip=初期値は数値ではありません

Class.Global.Infinity=無期限

Class.Global.Infinity.toolTip=JScript で表すことのできる最大値 (Number.MAX_VALUE) より大きい Number.POSITIVE_INFINITY の値を返します

Class.Global.undefined=undefined

Class.Global.undefined.toolTip=初期値は定義されていません

Class.Global.decodeURI=decodeURI
Class.Global.decodeURI.toolTip=エンコードされた URI (Uniform Resource Identifier) の、エンコードされていないバージョンを返します

Class.Global.decodeURIComponent=decodeURIComponent
Class.Global.decodeURIComponent.toolTip=URI (Uniform Resource Identifier) のエンコードされたコンポーネントの、エンコードされていないバージョンを返します

Class.Global.encodeURI=encodeURI
Class.Global.encodeURI.toolTip=有効な URI (Uniform Resource Identifier) としてエンコードされた文字列を返します

Class.Global.encodeURIComponent=encodeURIComponent
Class.Global.encodeURIComponent.toolTip=URI (Uniform Resource Identifier) の有効なコンポーネントとしてエンコードされた文字列を返します

Class.Global.escape=escape

Class.Global.escape.toolTip=すべてのコンピュータで読み取ることができる、エンコードされた String オブジェクトを返します

Class.Global.eval=eval

Class.Global.eval.toolTip=JScript コードを評価し、実行します

Class.Global.isNaN=isNaN

Class.Global.isNaN.toolTip=ToNumber をその引数に適用し、結果が NaN (非数値) の場合に True を返します

Class.Global.isFinite=isFinite
Class.Global.isFinite.toolTip=入力した数値が有限値かどうかを示す論理値を返します

Class.Global.parseFloat=parseFloat
Class.Global.parseFloat.toolTip=文字列から変換した浮動小数点数を返します

Class.Global.parseInt=parseInt

Class.Global.parseInt.toolTip=文字列から変換した整数を返します

Class.Global.unescape=unescape

Class.Global.unescape.toolTip=エスケープメソッドを使用してエンコードされた String オブジェクトからデコードした文字列を返します


Class.Global.decodeURI.uri=uri
Class.Global.decodeURIComponent.s=s

Class.Global.encodeURI.uri=uri
Class.Global.encodeURIComponent.s=s

Class.Global.escape.s=s

Class.Global.eval.code=code

Class.Global.isNaN.x=x
Class.Global.isFinite.n=n
Class.Global.parseFloat.s=s

Class.Global.parseInt.s=s

Class.Global.parseInt.radix=radix

Class.Global.unescape.s=s

#2.Array
Class.Array=Array 関数

Class.Array.toolTip=すべてのデータ型の resizable 配列をサポートします。

Class.Array.Array=配列

Class.Array.Array.toolTip=新しい Array オブジェクトを作成して初期化します。

Class.Array.length=length

Class.Array.length.toolTip=配列内で定義されている最大の要素に 1 を加算した整数値を返します

Class.Array.concat=concat

Class.Array.concat.toolTip=現在の配列と追加の項目を組み合わせた新しい配列を返します

Class.Array.join=join

Class.Array.join.toolTip=配列のすべての要素を結合し、指定した文字で区切った文字列値を返します

Class.Array.pop=pop

Class.Array.pop.toolTip=配列の最後の要素を配列から削除して返します

Class.Array.push=push

Class.Array.push.toolTip=引数を配列の最後に同じ順序で追加します

Class.Array.reverse=reverse

Class.Array.reverse.toolTip=配列の要素を逆順に並べ替えます

Class.Array.shift=shift

Class.Array.shift.toolTip=配列の最初の要素を配列から削除して返します

Class.Array.slice=slice

Class.Array.slice.toolTip=配列のセクションを含む配列を返します

Class.Array.sort=sort

Class.Array.sort.toolTip=この配列の要素をソートします

Class.Array.splice=splice

Class.Array.splice.toolTip=配列から要素を削除し、必要に応じてその位置に新しい要素を挿入します。配列から削除した要素を返します

Class.Array.toLocaleString=toLocaleString
Class.Array.toLocaleString.toolTip=配列の要素を文字列に変換します

Class.Array.toString=toString
Class.Array.toString.toolTip=このオブジェクトで引数がない場合に、組み込みジョインメソッドが呼び出されたときと同じ値を返します

Class.Array.unshift=unshift

Class.Array.unshift.toolTip=引数を、引数リストと同じ順番で配列の最初に追加します

Class.Array.Array.size=size

Class.Array.Array.element=element

Class.Array.concat.value=value

Class.Array.join.separator=区切り文字

Class.Array.push.value=value

Class.Array.slice.start=start

Class.Array.slice.end=end

Class.Array.sort.orderfunc=orderfunc

Class.Array.splice.start=start

Class.Array.splice.deleteCount=deleteCount
Class.Array.unshift.value=値

#3. Boolean
Class.Boolean=Boolean 関数

Class.Boolean.toolTip=Object 型のメンバと組み込みの Boolean オブジェクトのインスタンス

Class.Boolean.Boolean=ブーリアン

Class.Boolean.Boolean.toolTip=ToBoolean(value) で計算された論理値を返します

Class.Boolean.toString=toString
Class.Boolean.toString.toolTip=オブジェクトの文字列表記を返します

Class.Boolean.valueOf=valueOf
Class.Boolean.valueOf.toolTip=この論理値を返します

Class.Boolean.Boolean.value=値

#4. Date
Class.Date=Date 関数

Class.Date.toolTip=日付と時間の基本的な保管と取得を可能にするオブジェクト

Class.Date.Date=日付

Class.Date.Date.toolTip=現在の時間を示す文字列を返します

Class.Date.getDate=getDate
Class.Date.getDate.toolTip=Date オブジェクトの日付の値を現地時間で返します

Class.Date.getDay=getDay
Class.Date.getDay.toolTip=Date オブジェクトの曜日の値を現地時間で返します

Class.Date.getFullYear=getFullYear
Class.Date.getFullYear.toolTip=Date オブジェクトの年の値を現地時間で返します

Class.Date.getHours=getHours
Class.Date.getHours.toolTip=Date オブジェクトの時間 (時) の値を現地時間で返します

Class.Date.getMilliseconds=getMilliseconds
Class.Date.getMilliseconds.toolTip=Date オブジェクトの時間 (ミリ秒) の値を現地時間で返します

Class.Date.getMinutes=getMinutes
Class.Date.getMinutes.toolTip=Date オブジェクトの時間 (分) の値を現地時間で返します

Class.Date.getMonth=getMonth
Class.Date.getMonth.toolTip=Date オブジェクトの月の値を現地時間で返します

Class.Date.getSeconds=getSeconds
Class.Date.getSeconds.toolTip=Date オブジェクトの時間 (秒) の値を現地時間で返します

Class.Date.getTime=getTime
Class.Date.getTime.toolTip=Date オブジェクトの時間の値を返します

Class.Date.getTimezoneOffset=getTimezoneOffset
Class.Date.getTimezoneOffset.toolTip=現地時間と UTC (協定世界時) の時差 (分) を返します

Class.Date.getUTCDate=getUTCDate
Class.Date.getUTCDate.toolTip=Date オブジェクトの日付の値を UTC (協定世界時) で返します

Class.Date.getUTCDay=getUTCDay
Class.Date.getUTCDay.toolTip=Date オブジェクトの曜日の値を UTC (協定世界時) で返します

Class.Date.getUTCFullYear=getUTCFullYear
Class.Date.getUTCFullYear.toolTip=Date オブジェクトの年の値を UTC (協定世界時) で返します

Class.Date.getUTCHours=getUTCHours
Class.Date.getUTCHours.toolTip=Date オブジェクトの時間 (時) の値を UTC (協定世界時) で返します

Class.Date.getUTCMilliseconds=getUTCMilliseconds
Class.Date.getUTCMilliseconds.toolTip=Date オブジェクトの時間 (ミリ秒) の値を UTC (協定世界時) で返します

Class.Date.getUTCMinutes=getUTCMinutes
Class.Date.getUTCMinutes.toolTip=Date オブジェクトの時間 (分) の値を UTC (協定世界時) で返します

Class.Date.getUTCMonth=getUTCMonth
Class.Date.getUTCMonth.toolTip=Date オブジェクトの月の値を UTC (協定世界時) で返します

Class.Date.getUTCSeconds=getUTCSeconds
Class.Date.getUTCSeconds.toolTip=Date オブジェクトの時間 (秒) の値を UTC (協定世界時) で返します

Class.Date.getYear=getYear
Class.Date.getYear.toolTip=Date オブジェクトの年の値を返します

Class.Date.parse=パース

Class.Date.parse.toolTip=日付を含む文字列を解析し、ミリ秒の数値を返します

Class.Date.setDate=setDate
Class.Date.setDate.toolTip=Date オブジェクトの日付の値を現地時間で設定します

Class.Date.setFullYear=setFullYear
Class.Date.setFullYear.toolTip=Date オブジェクトの年の値を現地時間で設定します

Class.Date.setHours=setHours
Class.Date.setHours.toolTip=Date オブジェクトの時間 (時) の値を現地時間で設定します

Class.Date.setMilliseconds=setMilliseconds
Class.Date.setMilliseconds.toolTip=Date オブジェクトの時間 (ミリ秒) の値を現地時間で設定します

Class.Date.setMinutes=setMinutes
Class.Date.setMinutes.toolTip=Date オブジェクトの時間 (分) の値を現地時間で設定します

Class.Date.setMonth=setMonth
Class.Date.setMonth.toolTip=Date オブジェクトの月の値を現地時間で設定します

Class.Date.setSeconds=setSeconds
Class.Date.setSeconds.toolTip=Date オブジェクトの時間 (秒) の値を現地時間で設定します

Class.Date.setTime=setTime
Class.Date.setTime.toolTip=Date オブジェクトの時間の値を現地時間で設定します

Class.Date.setUTCDate=setUTCDate
Class.Date.setUTCDate.toolTip=Date オブジェクトの日付 (数値) を UTC (協定世界時) で設定します

Class.Date.setUTCFullYear=setUTCFullYear
Class.Date.setUTCFullYear.toolTip=Date オブジェクトの年の値を UTC (協定世界時) で設定します

Class.Date.setUTCHours=setUTCHours
Class.Date.setUTCHours.toolTip=Date オブジェクトの時間 (時) の値を UTC (協定世界時) で設定します

Class.Date.setUTCMilliseconds=setUTCMilliseconds
Class.Date.setUTCMilliseconds.toolTip=Date オブジェクトの時間 (ミリ秒) の値を UTC (協定世界時) で設定します

Class.Date.setUTCMinutes=setUTCMinutes
Class.Date.setUTCMinutes.toolTip=Date オブジェクトの時間 (分) の値を UTC (協定世界時) で設定します

Class.Date.setUTCMonth=setUTCMonth
Class.Date.setUTCMonth.toolTip=Date オブジェクトの月の値を UTC (協定世界時) で設定します

Class.Date.setUTCSeconds=setUTCSeconds
Class.Date.setUTCSeconds.toolTip=Date オブジェクトの時間 (秒) の値を UTC (協定世界時) で設定します

Class.Date.setYear=setYear
Class.Date.setYear.toolTip=Date オブジェクトの年の値を設定します

Class.Date.toDateString=toDateString
Class.Date.toDateString.toolTip=日付を示す文字列値を返します

Class.Date.toGMTString=toGMTString
Class.Date.toGMTString.toolTip=日付を示す文字列値を GMT (グリニッジ標準時) で返します

Class.Date.toLocaleDateString=toLocaleDateString
Class.Date.toLocaleDateString.toolTip=現在のタイムゾーンに基づいた日付を示す文字列値を返します

Class.Date.toLocaleString=toLocaleString
Class.Date.toLocaleString.toolTip=現在のタイムゾーンに基づいた日付を示す文字列値を返します

Class.Date.toLocaleTimeString=toLocaleTimeString
Class.Date.toLocaleTimeString.toolTip=現在のタイムゾーンに基づいた時間を示す文字列値を返します

Class.Date.toString=toString
Class.Date.toString.toolTip=文字列値を返します

Class.Date.toTimeString=toTimeString
Class.Date.toTimeString.toolTip=時間を示す文字列値を返します

Class.Date.toUTCString=toUTCString
Class.Date.toUTCString.toolTip=日付を示す文字列値を、人が認識できる書式で、UTC (協定世界時) で返します

Class.Date.UTC=UTC
Class.Date.UTC.toolTip=年、月、および必要に応じて日、時、分、秒、ミリ秒から日付を計算します

Class.Date.valueOf=valueOf
Class.Date.valueOf.toolTip=この時間の値である数値を返します

Class.Date.Date.milliseconds=ミリ秒

Class.Date.Date.datestring=datestring

Class.Date.Date.year=年

Class.Date.Date.month=月

Class.Date.Date.day=日

Class.Date.Date.hours=時間

Class.Date.Date.minutes=分

Class.Date.Date.seconds=秒

Class.Date.Date.ms=ミリ秒

Class.Date.parse.date=日付

Class.Date.setDate.day_of_month=day_of_month
Class.Date.setFullYear.year=年

Class.Date.setFullYear.month=月

Class.Date.setFullYear.minutes=分

Class.Date.setFullYear.day=日

Class.Date.setHours.hours=時間

Class.Date.setHours.minutes=分

Class.Date.setHours.seconds=秒

Class.Date.setHours.millis=ミリ秒

Class.Date.setMilliseconds.millis=ミリ秒

Class.Date.setMinutes.minutes=分

Class.Date.setMinutes.seconds=秒

Class.Date.setMinutes.millis=ミリ秒

Class.Date.setMonth.month=月

Class.Date.setMonth.day=日

Class.Date.setSeconds.seconds=秒

Class.Date.setSeconds.millis=ミリ秒

Class.Date.setTime.millis=ミリ秒

Class.Date.setUTCDate.day_of_month=day_of_month
Class.Date.setUTCFullYear.year=年

Class.Date.setUTCFullYear.month=月

Class.Date.setUTCFullYear.minutes=分

Class.Date.setUTCFullYear.day=日

Class.Date.setUTCHours.hours=時間

Class.Date.setUTCHours.minutes=分

Class.Date.setUTCHours.seconds=秒

Class.Date.setUTCHours.millis=ミリ秒

Class.Date.setUTCMilliseconds.millis=ミリ秒

Class.Date.setUTCMinutes.minutes=分

Class.Date.setUTCMinutes.seconds=秒

Class.Date.setUTCMinutes.millis=ミリ秒

Class.Date.setUTCMonth.month=月

Class.Date.setUTCMonth.day=日

Class.Date.setUTCSeconds.seconds=秒

Class.Date.setUTCSeconds.millis=ミリ秒

Class.Date.setYear.year=年

Class.Date.UTC.year=年

Class.Date.UTC.month=月

Class.Date.UTC.day=日

Class.Date.UTC.hours=時間

Class.Date.UTC.minutes=分

Class.Date.UTC.seconds=秒

Class.Date.UTC.ms=ミリ秒

#5. Math
Class.Math=Math 関数

Class.Math.toolTip=基本的な関数と定数を備えた固有のオブジェクト

Class.Math.E=E

Class.Math.E.toolTip=e の数値は約 2.7182818284590452354 です

Class.Math.LN10=LN10

Class.Math.LN10.toolTip=10 の自然対数の数値 (約 2.302585092994046)

Class.Math.LN2=LN2

Class.Math.LN2.toolTip=2 の自然対数の数値 (約 0.6931471805599453)

Class.Math.LOG10E=LOG10E

Class.Math.LOG10E.toolTip=10 を底とする e の対数の数値は約 0.4342944819032518 です

Class.Math.LOG2E=LOG2E

Class.Math.LOG2E.toolTip=2 を底とする e の対数の数値は約 1.4426950408889634 です

Class.Math.PI=PI

Class.Math.PI.toolTip=PI (円の外周の直径に対する割合) の数値 (約 3.1415926535897932)

Class.Math.SQRT1_2=SQRT1_2
Class.Math.SQRT1_2.toolTip=1/2 の平方根の数値 (約 0.7071067811865476)

Class.Math.SQRT2=SQRT2

Class.Math.SQRT2.toolTip=2 の平方根の数値 (約 1.4142135623730951)

Class.Math.abs=abs

Class.Math.abs.toolTip=絶対値を返します

Class.Math.acos=acos

Class.Math.acos.toolTip=逆余弦の、インプリメンテーションに依存する近似値を返します

Class.Math.asin=asin

Class.Math.asin.toolTip=逆正弦の、インプリメンテーションに依存する近似値を返します

Class.Math.atan=atan

Class.Math.atan.toolTip=逆正接の、インプリメンテーションに依存する近似値を返します

Class.Math.atan2=atan2

Class.Math.atan2.toolTip=引数 y および x の商 y/x (記号 y および x により結果の象限が決まります) の逆正接の、インプリメンテーションに依存する近似値を返します

Class.Math.ceil=ceil

Class.Math.ceil.toolTip=x 以上の整数の最小数値を返します

Class.Math.cos=cos

Class.Math.cos.toolTip=余弦の、インプリメンテーションに依存する近似値を返します

Class.Math.exp=exp

Class.Math.exp.toolTip=指数関数の、インプリメンテーションに依存する近似値を返します

Class.Math.floor=floor

Class.Math.floor.toolTip=x 以下の整数の最大数値を返します

Class.Math.log=log

Class.Math.log.toolTip=自然対数の、インプリメンテーションに依存する近似値を返します

Class.Math.max=max

Class.Math.max.toolTip=結果の最大値を返します

Class.Math.min=min

Class.Math.min.toolTip=結果の最小値を返します

Class.Math.pow=pow

Class.Math.pow.toolTip=x を y 乗した結果の、インプリメンテーションに依存する近似値を返します

Class.Math.random=random

Class.Math.random.toolTip=0 以上 1 未満の、正符号の付いた数値を返します

Class.Math.round=round

Class.Math.round.toolTip=x に最も近い整数の数値を返します

Class.Math.sin=sin

Class.Math.sin.toolTip=正弦の、インプリメンテーションに依存する近似値を返します

Class.Math.sqrt=sqrt

Class.Math.sqrt.toolTip=平方根の、インプリメンテーションに依存する近似値を返します

Class.Math.tan=tan

Class.Math.tan.toolTip=正接の、インプリメンテーションに依存する近似値を返します


Class.Math.abs.x=x
Class.Math.acos.x=x
Class.Math.asin.x=x
Class.Math.atan.x=x
Class.Math.atan2.y=y

Class.Math.atan2.x=x
Class.Math.ceil.x=x
Class.Math.cos.x=x
Class.Math.exp.x=x
Class.Math.floor.x=x
Class.Math.log.x=x
Class.Math.max.args=args

Class.Math.min.args=args

Class.Math.pow.x=x
Class.Math.pow.y=y

Class.Math.round.x=x
Class.Math.sin.x=x
Class.Math.sqrt.x=x
Class.Math.tan.x=x


#6. Number
Class.Number=Number 関数

Class.Number.toolTip=オブジェクトとしての数値データと数値定数のプレースホルダー

Class.Number.Number=番号

Class.Number.Number.toolTip=ToNumber(value) で計算された数値を返します

Class.Number.MAX_VALUE=MAX_VALUE
Class.Number.MAX_VALUE.toolTip=数値型の最大の正の有限値 (約 1.79E+308)

Class.Number.MIN_VALUE=MIN_VALUE
Class.Number.MIN_VALUE.toolTip=数値型の最小の正の値 (約 5.00E-324)

Class.Number.NaN=NaN

Class.Number.NaN.toolTip=数値ではありません

Class.Number.NEGATIVE_INFINITY=NEGATIVE_INFINITY
Class.Number.NEGATIVE_INFINITY.toolTip=JScript で表すことのできる最小の負の値 (-Number.MAX_VALUE) より小さい負の値を返します

Class.Number.POSITIVE_INFINITY=POSITIVE_INFINITY
Class.Number.POSITIVE_INFINITY.toolTip=JScript で表すことのできる最大の値 (Number.MAX_VALUE) より大きい値を返します

Class.Number.toExponential=toExponential

Class.Number.toExponential.toolTip=指数表記で表した数値を含む文字列を返します

Class.Number.toFixed=toFixed

Class.Number.toFixed.toolTip=数値を固定小数点表記で表した文字列を返します

Class.Number.toLocaleString=toLocaleString
Class.Number.toLocaleString.toolTip=値を、ホスト環境の現在のロケールに適切な文字列値として返します

Class.Number.toPrecision=toPrecision

Class.Number.toPrecision.toolTip=指数または固定小数点表記で表された、指定した桁数の数値を含む文字列を返します

Class.Number.toString=toString
Class.Number.toString.toolTip=オブジェクトの文字列表記を返します

Class.Number.valueOf=valueOf
Class.Number.valueOf.toolTip=指定されたオブジェクトのプリミティブ値を返します

Class.Number.Number.value=値

Class.Number.toExpoential.digits=桁

Class.Number.toFixed.digits=桁

Class.Number.toPrecision.precision=精度

Class.Number.toString.radix=基数

#7. Object
Class.Object=Object 関数

Class.Object.toolTip=Object 型のメンバ

Class.Object.Object=オブジェクト

Class.Object.Object.toolTip=オブジェクトを作成します

Class.Object.hasOwnProperty=hasOwnProperty

Class.Object.hasOwnProperty.toolTip=オブジェクトが指定された名前のプロパティを持つかどうかを示す論理値を返します

Class.Object.isPrototypeOf=isPrototypeOf

Class.Object.isPrototypeOf.toolTip=オブジェクトが他のオブジェクトのプロトタイプのチェーン内に存在するかどうかを示す論理値を返します

Class.Object.propertyIsEnumerable=propertyIsEnumerable
Class.Object.propertyIsEnumerable.toolTip=指定したプロパティがオブジェクトの一部であるかどうか、および列挙可能かどうかを示す論理値を返します

Class.Object.toLocaleString=toLocaleString
Class.Object.toLocaleString.toolTip=現在のロケールに基づいた、文字列に変換された日付を返します

Class.Object.toString=toString
Class.Object.toString.toolTip=オブジェクトの文字列表記を返します

Class.Object.valueOf=valueOf
Class.Object.valueOf.toolTip=現在のロケールに基づいた、文字列に変換された日付を返します

Class.Object.Object.value=値

Class.Object.hasOwnProperty.propname=プロパティ名

Class.Object.isPrototypeOf.o=o

Class.Object.propertyIsEnumerable.propname=プロパティ名

#8. RegExp
Class.RegExp=RegExp 関数

Class.RegExp.toolTip=簡易正規表現をサポートします

Class.RegExp.RegExp=正規表現

Class.RegExp.RegExp.toolTip=入力パターン文字列に文法を適用します

Class.RegExp.global=global

Class.RegExp.global.toolTip=パターンが検索文字列全体のすべての項目に一致するか、最初の 1 項目だけに一致するかを示す論理値を設定または返します

Class.RegExp.ignoreCase=ignoreCase
Class.RegExp.ignoreCase.toolTip=パターン検索で大文字と小文字を区別するかどうかを示す論理値を設定または返します

Class.RegExp.lastIndex=lastIndex
Class.RegExp.lastIndex.toolTip=次の検索を開始する文字列の位置を指定する整数

Class.RegExp.source=source

Class.RegExp.source.toolTip=現在の正規表現を表すパターンの文字列

Class.RegExp.exec=exec

Class.RegExp.exec.toolTip=正規表現に対して文字列の正規表現検索を行います

Class.RegExp.test=test

Class.RegExp.test.toolTip=指定した文字列に対して正規表現検索を行い、一致するパターンが検出されたかどうかを示す論理値を返します

Class.RegExp.toString=toString
Class.RegExp.toString.toolTip=現在の正規表現を表すパターンの文字列に変換します

Class.RegExp.RegExp.pattern=パターン

Class.RegExp.RegExp.attributes=属性

Class.RegExp.exec.string=文字列

Class.RegExp.test.string=文字列

#9.  ElementDefn
Class.ElementDefn=ElementDefn クラス

Class.ElementDefn.toolTip=レポート要素のデザイン情報を表示します

Class.ElementDefn.name=名前

Class.ElementDefn.name.toolTip=要素の名前を表示します

Class.ElementDefn.extendsElement=extendsElement
Class.ElementDefn.extendsElement.toolTip=この要素の親要素の名前を表示します

Class.ElementDefn.propertyDefns=propertyDefns
Class.ElementDefn.propertyDefns.toolTip=プロパティの定義の配列

Class.ElementDefn.slotDefns=slotDefns
Class.ElementDefn.slotDefns.toolTip=スロットの定義の配列

Class.ElementDefn.property=[anyProperty]
Class.ElementDefn.property.toolTip=この名前が付いている設計時プロパティの値を表示します

Class.ElementDefn.properties=プロパティ

Class.ElementDefn.properties.toolTip=プロパティ名に基づいてインデックスが生成されたプロパティの値の配列

Class.ElementDefn.slots=スロット

Class.ElementDefn.slots.toolTip=多くの要素は、他のレポート要素を含んでいるコンテナです

Class.ElementDefn.allowsUserProperties=allowsUserProperties
Class.ElementDefn.allowsUserProperties.toolTip=要素にユーザプロパティを指定できる場合は True、それ以外は False です

Class.ElementDefn.hasStyle=hasStyle
Class.ElementDefn.hasStyle.toolTip=要素にスタイルが指定されている場合は True、それ以外は False です

Class.ElementDefn.container=コンテナ

Class.ElementDefn.container.toolTip=この要素を含む要素の定義がある場合は、その定義を返します

Class.ElementDefn.containerSlot=containerSlot
Class.ElementDefn.containerSlot.toolTip=この要素を含むスロットの名前を取得します


#10. ElementState

Class.ElementState=ElementState
Class.ElementState.toolTip=レポート要素の実行時の状態を表します

Class.ElementState.design=デザイン

Class.ElementState.design.toolTip=このインスタンスの要素の定義オブジェクトを返します

Class.ElementState.property=プロパティ

Class.ElementState.property.toolTip=指定した名前のレポート要素プロパティの実行時の値を、アプリケーションによって取得・設定可能にします

Class.ElementState.container=コンテナ

Class.ElementState.container.toolTip=この要素を含む要素の状態オブジェクトがある場合は、そのオブジェクトを返します

Class.ElementState.slot=スロット

Class.ElementState.slot.toolTip=この要素に含まれる要素へのアクセスを可能にします

#11. Color
Class.Color=Color クラス

Class.Color.toolTip=カラー値 (RGB 値) を名前として扱うメソッドを提供します。

Class.Color.transparent=透明

Class.Color.transparent.toolTip=プロパティが CSS 透明値に設定されている場合に True を返します

Class.Color.rgb=RGB

Class.Color.rgb.toolTip=色の RGB 値を取得します

Class.Color.value=値

Class.Color.value.toolTip=色の名前 (名前として設定されている場合) または CSS RGB の文字列 (RGB 値として設定されている場合) を取得します

Class.Color.cssValue=cssValue
Class.Color.cssValue.toolTip=色を CSS で設定可能な形式 (CSS カラー名または RGB 値)で取得します


#12. Dimension
Class.Dimension=Dimension クラス

Class.Dimension.toolTip=ディメンションプロパティオブジェクトによって、CSS ディメンションプロパティの解析が処理されます

Class.Dimension.value=値

Class.Dimension.value.toolTip=CSS と互換性のあるディメンションの値を返します

Class.Dimension.measure=メジャー

Class.Dimension.measure.toolTip=ディメンションのメジャー部分を取得します。ディメンションが定数を使用して定義されている場合は、Null を返します

Class.Dimension.units=単位

Class.Dimension.units.toolTip=ディメンションの単位部分を取得します。ディメンションが定数を使用して定義されている場合は、Null を返します

#13. PropertyDefn
Class.PropertyDefn=PropertyDefn クラス

Class.PropertyDefn.toolTip=プロパティの定義オブジェクトは、BIRT 定義プロパティまたはユーザ定義プロパティについてのメタデータ情報を提供します

Class.PropertyDefn.isUserProperty=isUserProperty
Class.PropertyDefn.isUserProperty.toolTip=このプロパティがユーザ定義プロパティの場合は True、システム定義プロパティの場合は False です

Class.PropertyDefn.name=名前

Class.PropertyDefn.name.toolTip=内部プロパティ名

Class.PropertyDefn.group=グループ

Class.PropertyDefn.group.toolTip=プロパティを含むグループの名前

Class.PropertyDefn.canInherit=canInherit
Class.PropertyDefn.canInherit.toolTip=プロパティが継承される場合は True、それ以外は False です

Class.PropertyDefn.isStyle=isStyle
Class.PropertyDefn.isStyle.toolTip=プロパティが要素のスタイルの一部である場合は True、それ以外は False です

Class.PropertyDefn.type=型

Class.PropertyDefn.type.toolTip=プロパティの種類

Class.PropertyDefn.choices=選択肢

Class.PropertyDefn.choices.toolTip=プロパティの選択項目の配列

Class.PropertyDefn.defaultValue=defaultValue
Class.PropertyDefn.defaultValue.toolTip=プロパティのデフォルト値

#14. Report
Class.Report=Report クラス

Class.Report.toolTip=レポートおよびレポートが実行されるコンテキストに関するすべての情報へのアクセスを可能にします

Class.Report.design=デザイン

Class.Report.design.toolTip=レポートのデザインを記述する ReportDefn オブジェクトを返します

Class.Report.document=文書

Class.Report.document.toolTip=Factory で作成中、またはプレゼンテーションエンジンで読み取り中のレポート文書についての情報を提供するレポート文書オブジェクトを返します

Class.Report.params=パラメタ

Class.Report.params.toolTip=レポートに渡されたパラメタ値のオブジェクトの配列を返します

Class.Report.dataSets=データセット

Class.Report.dataSets.toolTip=レポートで定義されたデータセットの配列を返します

Class.Report.dataSources=dataSources
Class.Report.dataSources.toolTip=レポートで定義されたデータソース (接続) の配列を返します

Class.Report.config=設定

Class.Report.config.toolTip=レポートで使用可能な設定変数の配列を返します

Class.Report.reportContext=reportContext
Class.Report.reportContext.toolTip=このレポートに関連したコンテキスト

#15. ReportDoc
Class.ReportDoc=ReportDoc

Class.ReportDoc.toolTip=作成中または表示中のレポートについての情報を表示します

Class.ReportDoc.design=デザイン

Class.ReportDoc.design.toolTip=このインスタンスのルートデザインオブジェクトを返します

Class.ReportDoc.fileName=fileName
Class.ReportDoc.fileName.toolTip=このファイルが書き込まれているファイルがある場合は、そのファイルの名前

Class.ReportDoc.destination=宛先

Class.ReportDoc.destination.toolTip=ファイルシステムや一時ディレクトリなど、保存先の種類

Class.ReportDoc.body=本文

Class.ReportDoc.body.toolTip=本文に含まれるセクションのリスト


#16. ReportDefn

Class.ReportDefn=ReportDefn
Class.ReportDefn.toolTip=レポートのデザインについての全般的な情報を表示します

Class.ReportDefn.findStyle=findStyle
Class.ReportDefn.findStyle.toolTip=名前に基づいてスタイルを検索します

Class.ReportDefn.findStyle.styleName=styleName
Class.ReportDefn.findReportItem=findReportItem
Class.ReportDefn.findReportItem.toolTip=名前に基づいてレポート項目を検索します

Class.ReportDefn.findReportItem.itemName=itemName
Class.ReportDefn.findDataSource=findDataSource
Class.ReportDefn.findDataSource.toolTip=名前に基づいてデータソースを検索します

Class.ReportDefn.findDataSource.sourceName=sourceName
Class.ReportDefn.findDataSet=findDataSet
Class.ReportDefn.findDataSet.toolTip=名前に基づいてデータセットを検索します

Class.ReportDefn.findDataSet.dataSetName=dataSetName
Class.ReportDefn.findParameter=findParameter
Class.ReportDefn.findParameter.toolTip=名前に基づいてパラメタを検索します

Class.ReportDefn.findParameter.parameterName=parameterName


#17. DataSet
Class.DataSet=DataSet クラス

Class.DataSet.toolTip=データセットについての設計時および実行時の情報を表示します

Class.DataSet.row=行

Class.DataSet.row.toolTip=現在最も内側にある行を示します

Class.DataSet.rows=行

Class.DataSet.rows.toolTip=この階層内にある行へのアクセスを許可します

Class.DataSet.queryText=queryText
Class.DataSet.queryText.toolTip=ODA データソースに送信するクエリテキストを表示します

Class.DataSet.columnDefns=columnDefns
Class.DataSet.columnDefns.toolTip=列定義の配列

Class.DataSet.schema=スキーマ

Class.DataSet.schema.toolTip=ODA データソースについてのスキーマ情報を提供する DataSetParamMetadata オブジェクト

Class.DataSet.inputParams=inputParams
Class.DataSet.inputParams.toolTip=パラメタの配列である入力パラメタの値へのアクセスを可能にします

Class.DataSet.outputParams=outputParams
Class.DataSet.outputParams.toolTip=データセットの出力パラメタの値の配列

Class.DataSet.extensionProperties=extensionProperties
Class.DataSet.extensionProperties.toolTip=ODA データセットの ODA パブリックプロパティを表示します

Class.DataSet.privateProperties=privateProperties
Class.DataSet.privateProperties.toolTip=ODA データセットのプライベートプロパティを表示します

Class.DataSet.dataSource=DataSource
Class.DataSet.dataSource.toolTip=このデータセットで使用されている DataSource オブジェクトを返します。


#18. DataRow
Class.DataRow=DataRow クラス

Class.DataRow.toolTip=データセットから取得された 1 つの行を示します

Class.DataRow.DataRow=DataRow

Class.DataRow.DataRow.toolTip=指定したデータセットから空白のデータ行を作成するか、別のデータ行をコピーして空白のデータ行を作成します

Class.DataRow.DataRow.ds=ds

Class.DataRow.DataRow.r=r

Class.DataRow.column=column

Class.DataRow.column.toolTip=指定した名前の列の値を返します

Class.DataRow.rowNo=rowNo

Class.DataRow.rowNo.toolTip=結果セット内の行の位置

Class.DataRow.dataSet=dataSet

Class.DataRow.dataSet.toolTip=行を作成した DataSet オブジェクトを返します

Class.DataRow.schema=schema

Class.DataRow.schema.toolTip=データ行の列定義の配列を返します

Class.DataRow.columnDefns=columnDefns
Class.DataRow.columnDefns.toolTip=列のメタデータは、ColumnDefn の配列によって定義され、dataSet.columnDefns へのショートカットとなります。

#19. ColumnDefn
Class.ColumnDefn=ColumnDefn クラス

Class.ColumnDefn.toolTip=データ行の列のメタデータを記述します

Class.ColumnDefn.index=索引

Class.ColumnDefn.index.toolTip=データセットの結果セットのデータ行内にある列のインデックスを返します

Class.ColumnDefn.name=名前

Class.ColumnDefn.name.toolTip=データセットによって定義されている列名を返します

Class.ColumnDefn.type=型

Class.ColumnDefn.type.toolTip=列のデータ型を返します

Class.ColumnDefn.expr=範囲

Class.ColumnDefn.expr.toolTip=BIRT 式を使用して列を定義できます

Class.ColumnDefn.INTEGER=INTEGER

Class.ColumnDefn.INTEGER.toolTip=整数列の定数

Class.ColumnDefn.FLOAT=FLOAT

Class.ColumnDefn.FLOAT.toolTip=浮動小数点列の定数

Class.ColumnDefn.DECIMAL=DECIMAL

Class.ColumnDefn.DECIMAL.toolTip=固定精度10進数の定数

Class.ColumnDefn.BOOLEAN=BOOLEAN

Class.ColumnDefn.BOOLEAN.toolTip=論理値の定数

Class.ColumnDefn.STRING=STRING

Class.ColumnDefn.STRING.toolTip=文字列列の定数

Class.ColumnDefn.BLOB=BLOB

Class.ColumnDefn.BLOB.toolTip=BLOB (Binary Large OBject) 列の定数


#20. DataSetParamMetaData
Class.DataSetParamMetaData=DataSetParamMetaData クラス

Class.DataSetParamMetaData.toolTip=ODA データセットの入力および出力パラメタについての情報を表示します

Class.DataSetParamMetaData.description=説明

Class.DataSetParamMetaData.description.toolTip=ODA プロバイダによるデータセットの説明

Class.DataSetParamMetaData.resultSets=resultSets
Class.DataSetParamMetaData.resultSets.toolTip=ODA データセットから返された各結果セットを記述する ResultSetDefn オブジェクトの配列

Class.DataSetParamMetaData.inputParams=inputParams
Class.DataSetParamMetaData.inputParams.toolTip=DataSetInputParam オブジェクトである入力パラメタ定義の配列

Class.DataSetParamMetaData.outputParams=outputParams
Class.DataSetParamMetaData.outputParams.toolTip=DataSetOutputParam オブジェクトである出力パラメタ定義の配列

#21. ResultSetDefn
Class.ResultSetDefn=ResultSetDefn

Class.ResultSetDefn.toolTip=列定義の配列を提供します

Class.ResultSetDefn.name=名前

Class.ResultSetDefn.name.toolTip=結果セットの名前

Class.ResultSetDefn.columns=列

Class.ResultSetDefn.columns.toolTip=ODA データセットは、特定のコラムのセットを返します

Class.ResultSetDefn.dataSet=dataSet

Class.ResultSetDefn.dataSet.toolTip=この結果セットを提供するデータセットを参照します

#22. ResultSetColumn
Class.ResultSetColumn=ResultSetColumn

Class.ResultSetColumn.toolTip=ODA 結果セット内の 1 つの列を記述します

Class.ResultSetColumn.name=名前

Class.ResultSetColumn.name.toolTip=データ行の列のユニークな外部名

Class.ResultSetColumn.position=位置

Class.ResultSetColumn.position.toolTip=結果セット内の列の 1 ベースのインデックスの位置 (左から右へ)

Class.ResultSetColumn.displayName=displayName
Class.ResultSetColumn.displayName.toolTip=列のビジネス名

Class.ResultSetColumn.nativeTypeCode=nativeTypeCode
Class.ResultSetColumn.nativeTypeCode.toolTip=列のネイティブのデータ型コード

Class.ResultSetColumn.nativeTypeName=nativeTypeName
Class.ResultSetColumn.nativeTypeName.toolTip=ネイティブのデータ型の表示名

Class.ResultSetColumn.displayLength=displayLength
Class.ResultSetColumn.displayLength.toolTip=データ値のデフォルトの表示長

Class.ResultSetColumn.displayFormat=displayFormat
Class.ResultSetColumn.displayFormat.toolTip=レポート出力で、このデータに適した表示形式を示す文字列

Class.ResultSetColumn.horizAlign=horizAlign
Class.ResultSetColumn.horizAlign.toolTip=コラムの値のデフォルトの表示方法 (左揃え、中央揃え、右揃え、または両端揃え) を示す列挙値

Class.ResultSetColumn.heading=見出し

Class.ResultSetColumn.heading.toolTip=列ヘッダの列を指定する自由形式の文字列

Class.ResultSetColumn.description=説明

Class.ResultSetColumn.description.toolTip=列の詳細な説明を提供する自由形式の文字列

Class.ResultSetColumn.helpText=helpText
Class.ResultSetColumn.helpText.toolTip=列の詳細な説明を提供する自由形式の文字列

Class.ResultSetColumn.lineage=行数

Class.ResultSetColumn.lineage.toolTip=列値の元のソースに名前を付ける自由形式の文字列

Class.ResultSetColumn.axis=軸

Class.ResultSetColumn.axis.toolTip=列がマルチディメンションデータソースから取得された場合に適用されます

Class.ResultSetColumn.axisType=axisType
Class.ResultSetColumn.axisType.toolTip=その値は、ディメンションのメンバ、ディメンションの属性、またはメジャーのいずれかです

Class.ResultSetColumn.onColumnLayout=onColumnLayout

Class.ResultSetColumn.onColumnLayout.toolTip=この列をレポート出力に配置する方法についてのヒント

Class.ResultSetColumn.DIMENSION_MEMBER=DIMENSION_MEMBER
Class.ResultSetColumn.DIMENSION_MEMBER.toolTip=axisType プロパティの選択項目

Class.ResultSetColumn.DIMENSION_ATTRIBUTE=DIMENSION_ATTRIBUTE
Class.ResultSetColumn.DIMENSION_ATTRIBUTE.toolTip=axisType プロパティの選択項目

Class.ResultSetColumn.MEASURE=MEASURE

Class.ResultSetColumn.MEASURE.toolTip=axisType プロパティの選択項目


#23. DataSetInputParam
Class.DataSetInputParam=DataSetInputParam クラス

Class.DataSetInputParam.toolTip=ODA データセットの 1 つの入力パラメタを記述します

Class.DataSetInputParam.name=名前

Class.DataSetInputParam.name.toolTip=入力パラメタのユニークな名前

Class.DataSetInputParam.displayName=DataSetInputParam

Class.DataSetInputParam.displayName.toolTip=入力パラメタのビジネス名

Class.DataSetInputParam.description=説明

Class.DataSetInputParam.description.toolTip=パラメタの詳細な説明を提供する自由形式の文字列

Class.DataSetInputParam.nativeTypeCode=nativeTypeCode
Class.DataSetInputParam.nativeTypeCode.toolTip=パラメタのネイティブのデータ型コード

Class.DataSetInputParam.nativeTypeName=nativeTypeName
Class.DataSetInputParam.nativeTypeName.toolTip=ネイティブのデータ型の表示名

Class.DataSetInputParam.recordDefn=recordDefn
Class.DataSetInputParam.recordDefn.toolTip=複合パラメタの各フィールドの定義



#24. DataSetOutputParam
Class.DataSetOutputParam=DataSetOutputParam クラス

Class.DataSetOutputParam.toolTip=ODA データセットの 1 つの出力パラメタを記述します

Class.DataSetOutputParam.name=名前

Class.DataSetOutputParam.name.toolTip=出力パラメタのユニークな名前

Class.DataSetOutputParam.displayName=displayName
Class.DataSetOutputParam.displayName.toolTip=出力パラメタのビジネス名

Class.DataSetOutputParam.description=説明

Class.DataSetOutputParam.description.toolTip=パラメタの詳細な説明を提供する自由形式の文字列

Class.DataSetOutputParam.nativeTypeCode=nativeTypeCode
Class.DataSetOutputParam.nativeTypeCode.toolTip=パラメタのネイティブのデータ型コード

Class.DataSetOutputParam.nativeTypeName=nativeTypeName
Class.DataSetOutputParam.nativeTypeName.toolTip=ネイティブのデータ型の表示名

Class.DataSetOutputParam.recordDefn=recordDefn
Class.DataSetOutputParam.recordDefn.toolTip=複合パラメタの各フィールドの定義


#25. ItemState

Class.ItemState=ItemState
Class.ItemState.toolTip=レポート要素の実行時の状態を表します

Class.ItemState.row=行

Class.ItemState.row.toolTip=現在最も内側にある行を示します

Class.ItemState.rows=行

Class.ItemState.rows.toolTip=この階層内にある行へのアクセスを許可します


#26. Total
Class.Total=Total(Aggregate) 関数

Class.Total.toolTip=集計関数のホルダー

Class.Total.OVERALL=OVERALL

Class.Total.OVERALL.toolTip=データセット全体の合計を要求するグループ引数に使用される定数

Class.Total.NO_FILTER=NO_FILTER
Class.Total.NO_FILTER.toolTip=集計関数でフィルタを使っていないことを示す、フィルタ引数の定数。

Class.Total.sum=sum

Class.Total.sum.toolTip=数値のシーケンスの合計

Class.Total.runningSum=runningSum

Class.Total.runningSum.toolTip=レポートの指定された位置までの現在の合計を表示します

Class.Total.count=count

Class.Total.count.toolTip=行数

Class.Total.runningCount=runningCount
Class.Total.runningCount.toolTip=現在のグループの行数

Class.Total.countDistinct=countDistinct
Class.Total.countDistinct.toolTip=Distinct 値数

Class.Total.max=max

Class.Total.max.toolTip=数値、文字列、または日付のシーケンスの最大値

Class.Total.min=min

Class.Total.min.toolTip=数値、文字列、または日付のシーケンスの最小値

Class.Total.ave=ave

Class.Total.ave.toolTip=数値または日付のシーケンスの平均値

Class.Total.weightedAve=weightedAve

Class.Total.weightedAve.toolTip=数値のシーケンスの加重平均

Class.Total.weightedAve.weight=weight

Class.Total.movingAve=movingAve

Class.Total.movingAve.toolTip=ユーザが計算に含める値の数を指定する場合の、数値のシーケンスの移動平均

Class.Total.movingAve.window=window

Class.Total.median=median

Class.Total.median.toolTip=数値のシーケンスの中央値

Class.Total.mode=mode

Class.Total.mode.toolTip=値のシーケンスの最頻値

Class.Total.stdDev=stdDev

Class.Total.stdDev.toolTip=数値のシーケンスの標準偏差

Class.Total.variance=variance

Class.Total.variance.toolTip=数値のシーケンスの分散

Class.Total.first=first

Class.Total.first.toolTip=シーケンスの最初の値

Class.Total.last=last

Class.Total.last.toolTip=シーケンスの最後の値

Class.Total.rank=rank

Class.Total.rank.toolTip=系列の中で数値、文字列、日時の値の順序を 1 から返します。

Class.Total.rank.ascending=昇順

Class.Total.percentRank=percentRank
Class.Total.percentRank.toolTip=値の割合 (%) の順序を返します。戻り値は範囲 [0..1] です。

Class.Total.percentile=percentile

Class.Total.percentile.toolTip=百分位の値 (pct : 0～1 の範囲) を返します。pct が 0 の場合は戻り値が Total.min(expr) と等しくなり、pct が 1 の場合は戻り値が Total.max(expr) と等しくなります。

Class.Total.percentile.pct=割合 (%)

Class.Total.quartile=quartile

Class.Total.quartile.toolTip=四分位の値 (quart : 1～4 の整数) を返します。quart が 1 の場合は戻り値が Total.percentile(expr,0.25) と等しくなり、quart が 2 の場合は戻り値が Total.percentile(expr,0.5) と等しくなります (以下同上)。

Class.Total.quartile.quart=quart

Class.Total.percentSum=percentSum

Class.Total.percentSum.toolTip=expr/Total.sum(expr, [group]) に対する Total.Equals のパーセント

Class.Total.isTopN=isTopN

Class.Total.isTopN.toolTip=値がグループ内の上位 n 番目である場合は true を返し、それ以外の場合は false を返します。

Class.Total.isTopN.n=n
Class.Total.isTopNPercent=isTopNPercent

Class.Total.isTopNPercent.toolTip=値がグループ内の上位 (pct * 100) % である場合は true を返し、それ以外の場合は false を返します。

Class.Total.isTopNPercent.pct=割合 (%)

Class.Total.isBottomN=isBottomN

Class.Total.isBottomN.toolTip=値がグループ内の下位 n 番目である場合は true を返し、それ以外の場合は false を返します。

Class.Total.isBottomN.n=n
Class.Total.isBottomNPercent=isBottomNPercent

Class.Total.isBottomNPercent.toolTip=値がグループ内の下位 (pct * 100) % である場合は true を返し、それ以外の場合は false を返します。

Class.Total.isBottomNPercent.pct=割合 (%)

#27. Finance
Class.Finance=Finance 関数

Class.Finance.toolTip=静的財務関数の集まり

Class.Finance.ddb=ddb

Class.Finance.ddb.toolTip=二倍定率法を使用して算出した、特定の 1 期間の資産の減価償却

Class.Finance.ddb.initialCost=initialCost
Class.Finance.ddb.salvageValue=salvageValue
Class.Finance.ddb.assetLifespan=assetLifespan
Class.Finance.ddb.singlePeriod=singlePeriod
Class.Finance.sln=sln

Class.Finance.sln.toolTip=1 期間の資産の定額償却

Class.Finance.sln.initialCost=initialCost
Class.Finance.sln.salvageValue=salvageValue
Class.Finance.sln.assetLifespan=assetLifespan 
Class.Finance.syd=syd

Class.Finance.syd.toolTip=年数の合計に基づいた、特定の期間の資産の減価償却

Class.Finance.syd.initialCost=initialCost
Class.Finance.syd.salvageValue=salvageValue
Class.Finance.syd.assetLifespan=assetLifespan
Class.Finance.syd.singlePeriod=singlePeriod
Class.Finance.fv=fv

Class.Finance.fv.toolTip=定期的な定額支払いと一定利率に基づいた年金の将来価値

Class.Finance.fv.ratePerPeriod=ratePerPeriod

Class.Finance.fv.numberPayPeriods=numberPayPeriods

Class.Finance.fv.eachPmt=eachPmt

Class.Finance.fv.presentValue=presentValue
Class.Finance.fv.whenDue=whenDue

Class.Finance.ipmt=ipmt

Class.Finance.ipmt.toolTip=定期的な定額支払いと一定利率に基づいた特定期間の年金の利払い

Class.Finance.ipmt.ratePerPeriod=ratePerPeriod

Class.Finance.ipmt.singlePeriod=singlePeriod
Class.Finance.ipmt.numberPayPeriods=numberPayPeriods

Class.Finance.ipmt.presentValue=presentValue
Class.Finance.ipmt.futureValue=futureValue
Class.Finance.ipmt.whenDue=whenDue

Class.Finance.nper=nper

Class.Finance.nper.toolTip=定期的な定額支払いと一定利率に基づいた年金の期間数

Class.Finance.nper.ratePerPeriod=ratePerPeriod

Class.Finance.nper.eachPmt=eachPmt

Class.Finance.nper.presentValue=presentValue
Class.Finance.nper.futureValue=futureValue
Class.Finance.nper.whenDue=whenDue

Class.Finance.pmt=pmt

Class.Finance.pmt.toolTip=定期的な定額支払いと一定利率に基づいた年金の支払い

Class.Finance.pmt.ratePerPeriod=ratePerPeriod

Class.Finance.pmt.numberPayPeriods=numberPayPeriods

Class.Finance.pmt.presentValue=presentValue
Class.Finance.pmt.futureValue=futureValue
Class.Finance.pmt.whenDue=whenDue

Class.Finance.ppmt=ppmt

Class.Finance.ppmt.toolTip=定期的な定額支払いと一定利率に基づいた、特定期間の年金の元金支払い

Class.Finance.ppmt.ratePerPeriod=ratePerPeriod

Class.Finance.ppmt.singlePeriod=singlePeriod
Class.Finance.ppmt.numberPayPeriods=numberPayPeriods

Class.Finance.ppmt.presentValue=presentValue
Class.Finance.ppmt.futureValue=futureValue
Class.Finance.ppmt.whenDue=whenDue

Class.Finance.pv=pv

Class.Finance.pv.toolTip=将来の定期的な定額支払いと一定利率に基づいた年金の現在価値

Class.Finance.pv.ratePerPeriod=ratePerPeriod

Class.Finance.pv.numberPayPeriods=numberPayPeriods

Class.Finance.pv.eachPmt=eachPmt

Class.Finance.pv.futureValue=futureValue
Class.Finance.pv.whenDue=whenDue

Class.Finance.rate=rate

Class.Finance.rate.toolTip=期間あたりの年金の利率

Class.Finance.rate.numberPayPeriods=numberPayPeriods

Class.Finance.rate.eachPmt=eachPmt

Class.Finance.rate.presentValue=presentValue
Class.Finance.rate.futureValue=futureValue
Class.Finance.rate.whenDue=whenDue

Class.Finance.rate.startingGuess=startingGuess
Class.Finance.irr=irr

Class.Finance.irr.toolTip=既存の配列内の、一連の定期的なキャッシュフロー、支払い、収入の内部収益率

Class.Finance.irr.cashArray=cashArray
Class.Finance.irr.startingGuess=startingGuess
Class.Finance.npv=npv

Class.Finance.npv.toolTip=変動する一連の定期的なキャッシュフロー (流出入) の、特定の利率での正味現在価値

Class.Finance.npv.rate=rate

Class.Finance.npv.cashArray=cashArray 
Class.Finance.runningnpv=npv

Class.Finance.runningnpv.toolTip=変動する一連の定期的なキャッシュフロー (流出入) の、特定の利率での正味現在価値。

Class.Finance.runningnpv.rate=rate

Class.Finance.runningnpv.cashArray=cashArray 
Class.Finance.mirr=mirr

Class.Finance.mirr.toolTip=既存の配列内の、一連の定期的なキャッシュフロー (支払いおよび収入) の修正内部収益率

Class.Finance.mirr.cashArray=cashArray
Class.Finance.mirr.rate=rate

Class.Finance.percent=percent

Class.Finance.percent.toolTip=2 つの数値の割合 (%) を計算します

Class.Finance.percent.denom=denom

Class.Finance.percent.num=num

Class.Finance.percent.rate=rate

#28. DateTimeSpan
Class.DateTimeSpan=DateTimeSpan 関数

Class.DateTimeSpan.toolTip=2 つの日付 / 時間の間隔に関する関数の集まり

Class.DateTimeSpan.years=年

Class.DateTimeSpan.years.toolTip=2 つの日付の間の完全な年数を返します

Class.DateTimeSpan.years.startDate=startDate
Class.DateTimeSpan.years.endDate=endDate
Class.DateTimeSpan.months=月

Class.DateTimeSpan.months.toolTip=2 つの日付の間の完全な月数を返します

Class.DateTimeSpan.months.startDate=startDate
Class.DateTimeSpan.months.endDate=endDate
Class.DateTimeSpan.days=日

Class.DateTimeSpan.days.toolTip=2 つの日付の間の日数を返します

Class.DateTimeSpan.days.startDate=startDate
Class.DateTimeSpan.days.endDate=endDate
Class.DateTimeSpan.hours=時間

Class.DateTimeSpan.hours.toolTip=2 つの日付 / 時間の間の時間数を返します

Class.DateTimeSpan.hours.startDate=startDate
Class.DateTimeSpan.hours.endDate=endDate
Class.DateTimeSpan.minutes=分

Class.DateTimeSpan.minutes.toolTip=2 つの日付 / 時間の間の分数を返します

Class.DateTimeSpan.minutes.startDate=startDate
Class.DateTimeSpan.minutes.endDate=endDate
Class.DateTimeSpan.seconds=秒

Class.DateTimeSpan.seconds.toolTip=2 つの日付 / 時間の間の秒数を返します

Class.DateTimeSpan.seconds.startDate=startDate
Class.DateTimeSpan.seconds.endDate=endDate

Class.DateTimeSpan.addDate=addDate
Class.DateTimeSpan.addDate.toolTip=日付の年、月、または日に数を加算します

Class.DateTimeSpan.addDate.startDate=startDate
Class.DateTimeSpan.addDate.years=年

Class.DateTimeSpan.addDate.months=月

Class.DateTimeSpan.addDate.days=日



Class.DateTimeSpan.addTime=addTime
Class.DateTimeSpan.addTime.toolTip=日付 / 時間の時間、分、または秒に数を加算します

Class.DateTimeSpan.addTime.startDate=startDate
Class.DateTimeSpan.addTime.hours=時間

Class.DateTimeSpan.addTime.minutes=分

Class.DateTimeSpan.addTime.seconds=秒


Class.DateTimeSpan.subDate=subDate
Class.DateTimeSpan.subDate.toolTip=日付の年、月、または日から数を減算します

Class.DateTimeSpan.subDate.startDate=startDate
Class.DateTimeSpan.subDate.years=年

Class.DateTimeSpan.subDate.months=月

Class.DateTimeSpan.subDate.days=日



Class.DateTimeSpan.subTime=subTime
Class.DateTimeSpan.subTime.toolTip=日付の時間、分、または秒から数を減算します

Class.DateTimeSpan.subTime.startDate=startDate
Class.DateTimeSpan.subTime.hours=時間

Class.DateTimeSpan.subTime.minutes=分

Class.DateTimeSpan.subTime.seconds=秒

#29.GlobalBirt
Class.GlobalBirt=Global 変数

Class.GlobalBirt.toolTip=式やスクリプトからデータ行やレポートについての情報へのアクセスを許可する多数のグローバル変数を提供します

Class.GlobalBirt.datasets=Birt Data Sets

Class.GlobalBirt.datasets.toolTip=レポートで定義されたデータセットの配列を返します

Class.GlobalBirt.params=Birt Parameters

Class.GlobalBirt.params.toolTip=レポートに渡されたパラメタ値のオブジェクトの配列を返します

Class.GlobalBirt.row=Birt Row

Class.GlobalBirt.row.toolTip=データセットまたはレポート要素の現在のデータ行

Class.GlobalBirt.rows=Birt Rows

Class.GlobalBirt.rows.toolTip=現在のコンテキストに定義されている行のスタック

Class.GlobalBirt.config=Birt Config

Class.GlobalBirt.config.toolTip=レポートで使用可能な設定変数の配列を返します。名前と値の組み合わせで表されます

Class.GlobalBirt.report=Birt Report

Class.GlobalBirt.report.toolTip=レポートおよびレポートが実行されるコンテキストに関するすべての情報へのアクセスを可能にします

Class.GlobalBirt.CurrentPage=現在のページ番号

Class.GlobalBirt.CurrentPage.toolTip=現在のページの番号を返します。

Class.GlobalBirt.TotalPage=合計ページ数

Class.GlobalBirt.TotalPage.toolTip=合計ページ数を返します。

Class.GlobalBirt.this=このオブジェクト

Class.GlobalBirt.this.toolTip=このオブジェクトを表す

Class.GlobalBirt.appContext=AppContext
Class.GlobalBirt.appContext.toolTip=このレポートに関連したコンテキスト

#30 DataSource
Class.DataSource=DataSource クラス

Class.DataSource.toolTip=Factory 実行時の状態のデータソースには、データソースのクラスを使ってアクセスします。DataSource オブジェクトには、そのオブジェクトを使用する DataSet オブジェクトを使用してアクセスします。

Class.DataSource.extensionID=extensionID

Class.DataSource.extensionID.toolTip=ODA ドライバ拡張で定義されている ODAデータソースの extensionID の値を返します。extensionID は、BIRT 環境のデータソースのユニークな型を識別します。このプロパティは更新できません。

Class.DataSource.isOpen=isOpen

Class.DataSource.isOpen.toolTip=データソースが接続されているかどうかを示す論理値を返します。

Class.DataSource.extensionProperties=extensionProperties
Class.DataSource.extensionProperties.toolTip=ODA データソースのプライベートプロパティを表示します。

#31. BirtComp
 
Class.BirtComp=BirtComp
Class.BirtComp.toolTip=データを比較する

Class.BirtComp.equalTo=equalsTo

Class.BirtComp.equalTo.toolTip=x は y と等しい

Class.BirtComp.equalTo.x=x
Class.BirtComp.equalTo.y=y

Class.BirtComp.greaterThan=greaterThan
Class.BirtComp.greaterThan.toolTip=x は y より大きい

Class.BirtComp.greaterThan.x=x
Class.BirtComp.greaterThan.y=y

Class.BirtComp.lessThan=lessThan
Class.BirtComp.lessThan.toolTip=x は y より小さい

Class.BirtComp.lessThan.x=x
Class.BirtComp.lessThan.y=y

Class.BirtComp.greaterOrEqual=greaterOrEqual

Class.BirtComp.greaterOrEqual.toolTip=x は y 以上

Class.BirtComp.greaterOrEqual.x=x
Class.BirtComp.greaterOrEqual.y=y

Class.BirtComp.lessOrEqual=lessOrEqual

Class.BirtComp.lessOrEqual.toolTip=x は y 以下

Class.BirtComp.lessOrEqual.x=x
Class.BirtComp.lessOrEqual.y=y

Class.BirtComp.notEqual=notEqual
Class.BirtComp.notEqual.toolTip=x は y と等しくない

Class.BirtComp.notEqual.x=x
Class.BirtComp.notEqual.y=y

Class.BirtComp.between=間

Class.BirtComp.between.toolTip=r1 以上 r2 以下

Class.BirtComp.between.val=値

Class.BirtComp.between.r1=r1

Class.BirtComp.between.r2=r2

Class.BirtComp.like=like

Class.BirtComp.like.toolTip=x は SQL のパターン y と一致

Class.BirtComp.like.x=x
Class.BirtComp.like.y=y

Class.BirtComp.match=match

Class.BirtComp.match.toolTip=x は JavaScript のパターン y と一致

Class.BirtComp.match.x=x
Class.BirtComp.match.y=y

Class.BirtComp.anyOf=anyOf

Class.BirtComp.anyOf.toolTip=値が次の引数の範囲内にあるかどうか

Class.BirtComp.anyOf.val=値


###########################################################
# Structures
Structure.MapRule=MapRule
Structure.HighlightRule=HighlightRule
Structure.CustomColor=CustomColor
Structure.SearchKey=SearchKey

Structure.Action=アクション

Structure.UserProperty=UserProperty
Structure.SortKey=ソートキー

Structure.FilterCondition=フィルタ条件

Structure.ConfigVar=変数の設定

Structure.HideRule=非表示

Structure.IncludeScript=インクルードするスクリプト

Structure.EmbeddedImage=埋め込まれたイメージ

Structure.OdaProperty=ODA プロパティ

Structure.ParamBinding=パラメタバインド

Structure.ResultSetColumn=列

Structure.ComputedColumn=計算列

Structure.ColumnHint=列のヒント

Structure.IncludedLibrary=インクルードするライブラリ

Structure.DataSetParam=データセットのパラメタ

Structure.PropertyMask=プロパティマスク

Structure.ExtendedProperty=拡張プロパティ

Structure.CachedMetaData=キャッシュされたメタデータ

Structure.NumberFormatValue=数値の書式の値

Structure.StringFormatValue=数値の書式の文字列

Structure.DateTimeFormatValue=日付/時間の書式の値

Structure.DateFormatValue = 日付の書式の値

Structure.TimeFormatValue = 時間の書式の値

Structure.UserMethod=ユーザメソッド

Structure.PropertyBinding=プロパティのバインド

Structure.JoinCondition=ジョインの条件

Structure.ParameterFormatValue=パラメタの書式の値

Structure.OdaDesignerState=ODA Designer ステータス

Structure.ScriptLib=スクリプトライブラリ

Structure.IncludedCssStyleSheet=インクルードされている CSS スタイルシート

Structure.DimensionJoinCondition=ディメンションのジョイン条件

Structure.FormatValue =書式の値

Structure.CalculationArgument=計算の引数

#0.MapRule
Structure.MapRule.testExpr=TestExpression
Structure.MapRule.operator=演算子

Structure.MapRule.value1=値 1

Structure.MapRule.value2=値 2

Structure.MapRule.display=表示

Structure.MapRule.displayID=displayID

Structure.MapRule.isDesignTime=Is Design 時間
  
#1.HighlightRule
Structure.HighlightRule.operator=演算子

Structure.HighlightRule.isDesignTime=Is Design 時間

Structure.HighlightRule.value1=値 1

Structure.HighlightRule.value2=値 2

Structure.HighlightRule.testExpr = TestExpression
Structure.HighlightRule.strikethrough=取り消し線

Structure.HighlightRule.border.top.style=スタイル

Structure.HighlightRule.border.top.width=幅

Structure.HighlightRule.border.top.color=カラー

Structure.HighlightRule.border.left.style=スタイル

Structure.HighlightRule.border.left.width=幅

Structure.HighlightRule.border.left.color=カラー

Structure.HighlightRule.border.bottom.style=スタイル

Structure.HighlightRule.border.bottom.width=幅

Structure.HighlightRule.border.bottom.color=カラー

Structure.HighlightRule.border.right.style=スタイル

Structure.HighlightRule.border.right.width=幅

Structure.HighlightRule.border.right.color=カラー

Structure.HighlightRule.background.color=背景色

Structure.HighlightRule.background.image=背景イメージ

Structure.HighlightRule.backgroundRepeat=背景を繰り返す

Structure.HighlightRule.format.dateTime=日付/時間の書式

Structure.HighlightRule.format.number=数値の書式

Structure.HighlightRule.format.number.align=数値の配置

Structure.HighlightRule.format.string=文字列の書式

Structure.HighlightRule.font.family=フォント

Structure.HighlightRule.font.size=サイズ

Structure.HighlightRule.font.style=スタイル

Structure.HighlightRule.font.weight=太さ

Structure.HighlightRule.font.variant=バリエーション

Structure.HighlightRule.font.color=カラー

Structure.HighlightRule.font.textUnderline=下線

Structure.HighlightRule.font.textOverline=上線

Structure.HighlightRule.font.textLineThrough=取り消し線

Structure.HighlightRule.padding.top=パディング (上)

Structure.HighlightRule.padding.left=パディング (左)

Structure.HighlightRule.padding.bottom=パディング (下)

Structure.HighlightRule.padding.right=パディング (右)

Structure.HighlightRule.text.align=文字列の位置揃え

Structure.HighlightRule.text.indent=文字列のインデント

Structure.HighlightRule.text.transform=文字列の位置変更

Structure.HighlightRule.style=スタイル

Structure.HighlightRule.text.direction=文字列の向き

#2.CustomColor
Structure.CustomColor.name=名前

Structure.CustomColor.color=カラー

Structure.CustomColor.displayName=表示名

Structure.CustomColor.displayNameID=表示名のキー
  
#4.DrillThroughSearchKeys
Structure.SearchKey.expression=式
  
#5.UserProperty
Structure.UserProperty.name=名前

Structure.UserProperty.type=種類

Structure.UserProperty.isVisible=表示する

Structure.UserProperty.displayNameID=表示名キー

Structure.UserProperty.displayName=表示名

#6.ConfigVar
Structure.ConfigVar.name=名前

Structure.ConfigVar.value=値

#7. SortKey
Structure.SortKey.key=ソートキー

Structure.SortKey.direction=ソートの方向

Structure.SortKey.strength=ソートの度合い

Structure.SortKey.locale=ソートのロケール

#8. Hide
Structure.HideRule.format=書式

Structure.HideRule.valueExpr=式

#9. IncludeScript
Structure.IncludeScript.fileName=ファイル名

#10.EmbeddedImage
Structure.EmbeddedImage.name=名前

Structure.EmbeddedImage.type=種類

Structure.EmbeddedImage.data=データ

Structure.EmbeddedImage.libReference=ライブラリ参照

#11.FilterCondition
Structure.FilterCondition.operator=演算子

Structure.FilterCondition.expr=式

Structure.FilterCondition.value1=値 1

Structure.FilterCondition.value2=値 2

Structure.FilterCondition.filterTarget=フィルタのターゲット

Structure.FilterCondition.isOptional=オプション

Structure.FilterCondition.extensionName=拡張子名

Structure.FilterCondition.extensionExprId=拡張式 ID

Structure.FilterCondition.pushDown=プッシュ (下へ)

Structure.FilterCondition.dynamicFilterParameter=動的フィルタパラメタ

Structure.FilterCondition.type=種類

Structure.FilterCondition.updateAggregation=集計の更新
Structure.FilterCondition.customValue=カスタム値

#10. PropertyMask
Structure.PropertyMask.name=名前

Structure.PropertyMask.mask=マスク

#11.OdaProperty
Structure.OdaProperty.name=名前

Structure.OdaProperty.value=値

#13. ParamBinding
Structure.ParamBinding.paramName=パラメタ名

Structure.ParamBinding.expression=式

#14. ResultSetColumn
Structure.ResultSetColumn.position=位置

Structure.ResultSetColumn.name=名前

Structure.ResultSetColumn.dataType=データ型

Structure.ResultSetColumn.nativeDataType=ネイティブのデータ型コード

#15. ComputedColumn
Structure.ComputedColumn.name=列名

Structure.ComputedColumn.displayName=列の表示名

Structure.ComputedColumn.displayNameID=列の表示名 ID

Structure.ComputedColumn.expression=式

Structure.ComputedColumn.dataType=データ型

Structure.ComputedColumn.aggregateOn=AggregateOn 式

Structure.ComputedColumn.aggregateFunction=AggregateOn 関数

Structure.ComputedColumn.arguments=AggregateOn 関数の引数

Structure.ComputedColumn.filterExpr=フィルタ式

Structure.ComputedColumn.allowExport=エクスポートを許可する

Structure.ComputedColumn.calculationType=計算の種類

Structure.ComputedColumn.calculationArguments=計算の引数

Structure.ComputedColumn.refDateType=参照日の種類

Structure.ComputedColumn.refDateValue=参照日の値

Structure.ComputedColumn.timeDimension=時間のディメンション

#16. ColumnHint
Structure.ColumnHint.columnName=列名

Structure.ColumnHint.alias=エイリアス

Structure.ColumnHint.searching=検索中

Structure.ColumnHint.export=エクスポート

Structure.ColumnHint.analysis=解析

Structure.ColumnHint.parentLevel=親レベル

Structure.ColumnHint.format=書式

Structure.ColumnHint.helpText=ヘルプテキスト

Structure.ColumnHint.helpTextID=ヘルプテキストのリソースキー

Structure.ColumnHint.displayName=表示名

Structure.ColumnHint.displayNameID=表示名のリソースキー

Structure.ColumnHint.onColumnLayout=列のレイアウト時

Structure.ColumnHint.heading=見出し

Structure.ColumnHint.headingID=見出しリソースキー

Structure.ColumnHint.displayLength=表示長

Structure.ColumnHint.horizontalAlign=横方向の配置

Structure.ColumnHint.wordWrap=文字列の折り返し

Structure.ColumnHint.textFormat=テキストの書式

Structure.ColumnHint.description=説明

Structure.ColumnHint.descriptionID=説明リソースキー

Structure.ColumnHint.action=アクション

Structure.ColumnHint.ACLExpression=ACL 式

Structure.ColumnHint.analysisColumn=分析列

Structure.ColumnHint.valueFormat=値の書式

Structure.ColumnHint.indexColumn=インデックス列

Structure.ColumnHint.compressed=圧縮済み

#12. SelectionList Choice
Structure.SelectionChoice=選択項目

Structure.SelectionChoice.value=値

Structure.SelectionChoice.label=ラベル

Structure.SelectionChoice.labelID=ラベルのリソースキー

#13. IncludedLibrary
Structure.IncludedLibrary.fileName=ファイル名

Structure.IncludedLibrary.namespace=ネームスペース

#14. IncludedCssStyleSheet
Structure.IncludedCssStyleSheet.fileName=ファイル名

Structure.IncludedCssStyleSheet.externalCssURI=外部の CSS URI

Structure.IncludedCssStyleSheet.useExternalCss=外部 CSS を使用

#15. DataSetParam
Structure.DataSetParam.name=名前

Structure.DataSetParam.dataType=データ型

Structure.DataSetParam.position=位置

Structure.DataSetParam.defaultValue=デフォルト値

Structure.DataSetParam.isOptional=オプション

Structure.DataSetParam.allowNull=Null を許可する

Structure.DataSetParam.isInput=入力パラメタ

Structure.DataSetParam.isOutput=出力パラメタ

Structure.DataSetParam.nativeDataType=ネイティブのデータ型コード

Structure.DataSetParam.displayName=表示名

Structure.DataSetParam.displayNameID=表示名リソースキー

Structure.DataSetParam.heading=見出し

Structure.DataSetParam.headingID=見出しリソースキー

Structure.DataSetParam.helpText=ヘルプテキスト

Structure.DataSetParam.helpTextID=ヘルプテキストリソースキー

Structure.DataSetParam.description=説明

Structure.DataSetParam.descriptionID=説明リソースキー

#16. ExtendedProperty
Structure.ExtendedProperty.name=名前

Structure.ExtendedProperty.value=値

#17. Action
Structure.Action.linkType=リンクの種類

Structure.Action.toolTip=ツールヒント

Structure.Action.targetWindow=ターゲット

Structure.Action.linkExpr=リンク式

Structure.Action.uri=場所

Structure.Action.targetBookmark=ブックマークリンク

Structure.Action.reportName=レポート名

Structure.Action.paramBindings=パラメタバインド

Structure.Action.search=検索キー

Structure.Action.formatType=形式

Structure.Action.targetFileType=ターゲットのファイルの種類

Structure.Action.targetBookmarkType=ターゲットのブックマークの種類

#18. CachedMetaData
Structure.CachedMetaData.parameters=パラメタ

Structure.CachedMetaData.resultSet=結果セット列

#19. NumberFormatValue
Structure.NumberFormatValue.category=カテゴリ

Structure.NumberFormatValue.pattern=パターン

Structure.NumberFormatValue.locale=ロケール

#20. StringFormatValue
Structure.StringFormatValue.category=カテゴリ

Structure.StringFormatValue.pattern=パターン

Structure.StringFormatValue.locale=ロケール

#21. DateTimeFormatValue
Structure.DateTimeFormatValue.category=カテゴリ

Structure.DateTimeFormatValue.pattern=パターン

Structure.DateTimeFormatValue.locale=ロケール

#21. DateFormatValue
Structure.DateFormatValue.category=カテゴリ

Structure.DateFormatValue.pattern=パターン

Structure.DateFormatValue.locale=ロケール

#21. TimeFormatValue
Structure.TimeFormatValue.category=カテゴリ

Structure.TimeFormatValue.pattern=パターン

Structure.TimeFormatValue.locale=ロケール

#22. UserMethod
Structure.UserMethod.name=名前

#23. PropertyBinding
Structure.PropertyBinding.name=名前

Structure.PropertyBinding.id=要素ID

Structure.PropertyBinding.value=値

#24. JoinCondition
Structure.JoinCondition.joinType=ジョインの型

Structure.JoinCondition.joinOperator=ジョイン演算子

Structure.JoinCondition.leftDataSet=左側のデータセット

Structure.JoinCondition.rightDataSet=右側のデータセット

Structure.JoinCondition.leftExpression=左側の式

Structure.JoinCondition.rightExpression=右側の式

#25. ParameterFormatValue
Structure.ParameterFormatValue.category=カテゴリ

Structure.ParameterFormatValue.pattern=パターン

Structure.ParameterFormatValue.locale=ロケール

#26. OdaDesignerState
Structure.OdaDesignerState.version=バージョン

Structure.OdaDesignerState.stateContentAsString=文字列のステータス内容

Structure.OdaDesignerState.stateContentAsBlob=BLOB のステータス内容

#27. OdaResultSetColumn
Structure.OdaResultSetColumn=ODA 結果セット列

Structure.OdaResultSetColumn.position=位置

Structure.OdaResultSetColumn.name=名前

Structure.OdaResultSetColumn.nativeName=ネイティブの名前

Structure.OdaResultSetColumn.dataType=データ型

Structure.OdaResultSetColumn.nativeDataType=ネイティブのデータ型コード

#28. OdaDataSetParam
Structure.OdaDataSetParam=ODA データセットパラメタ

Structure.OdaDataSetParam.name=名前

Structure.OdaDataSetParam.paramName=パラメタ名

Structure.OdaDataSetParam.nativeName=ネイティブの名前

Structure.OdaDataSetParam.dataType=データ型

Structure.OdaDataSetParam.nativeDataType=ネイティブのデータ型コード

Structure.OdaDataSetParam.position=位置

Structure.OdaDataSetParam.defaultValue=デフォルト値

Structure.OdaDataSetParam.isOptional=オプション

Structure.OdaDataSetParam.allowNull=Null を許可する

Structure.OdaDataSetParam.isInput=入力

Structure.OdaDataSetParam.isOutput=出力

Structure.OdaDataSetParam.displayName=表示名

Structure.OdaDataSetParam.displayNameID=表示名リソースキー

Structure.OdaDataSetParam.heading=見出し

Structure.OdaDataSetParam.headingID=見出しリソースキー

Structure.OdaDataSetParam.helpText=ヘルプテキスト

Structure.OdaDataSetParam.helpTextID=ヘルプテキストのリソースキー

Structure.OdaDataSetParam.description=説明

Structure.OdaDataSetParam.descriptionID=説明リソースキー

#27. ScriptLib
Structure.ScriptLib.name =スクリプトライブラリの名前

#28. TOC
Structure.TOC=目次

Structure.TOC.Expression=式

Structure.TOC.TOCStyle=目次のスタイル

Structure.TOC.border.top.style=一番上の罫線の線種

Structure.TOC.border.top.width=一番上の罫線の幅

Structure.TOC.border.top.color=一番上の罫線の色

Structure.TOC.border.left.style=左の罫線の線種

Structure.TOC.border.left.width=左の罫線の幅

Structure.TOC.border.left.color=左の罫線の色

Structure.TOC.border.bottom.style=一番下の罫線の線種

Structure.TOC.border.bottom.width=一番下の罫線の幅

Structure.TOC.border.bottom.color=一番下の罫線の色

Structure.TOC.border.right.style=右の罫線の線種

Structure.TOC.border.right.width=右の罫線の幅

Structure.TOC.border.right.color=右の罫線の色

Structure.TOC.background.color=背景色

Structure.TOC.format.dateTime=日付/時間の書式

Structure.TOC.format.number=数値の書式

Structure.TOC.format.number.align=数値の配置の書式

Structure.TOC.format.string=文字列の書式

Structure.TOC.font.family=フォントファミリ

Structure.TOC.font.size=サイズ

Structure.TOC.font.style=スタイル

Structure.TOC.font.weight=太さ

Structure.TOC.font.variant=バリエーション

Structure.TOC.font.color=文字の色

Structure.TOC.font.textUnderline=下線

Structure.TOC.font.textOverline=上線

Structure.TOC.font.textLineThrough=取り消し線

Structure.TOC.text.align=文字配置

Structure.TOC.text.indent=文字列のインデント

Structure.TOC.text.transform=文字列の位置変更

Structure.TOC.text.direction=文字列の向き

#29. DimensionCondition
Structure.DimensionCondition=ディメンション条件

Structure.DimensionCondition.joinConditions=ジョインの条件

Structure.DimensionCondition.hierarchy=階層

#30. Rule
Structure.Rule=規則

Structure.Rule.ruleExpre=規則式

Structure.Rule.displayExpre=表示式

#31. LevelAttribute
Structure.LevelAttribute=レベルの属性

Structure.LevelAttribute.name=名前

Structure.LevelAttribute.dataType=データ型

#32. OdaLevelAttribute
Structure.OdaLevelAttribute=ODA レベルの属性

Structure.OdaLevelAttribute.name=名前

Structure.OdaLevelAttribute.dataType=データ型

Structure.OdaLevelAttribute.nativeName=ネイティブの名前

Structure.OdaLevelAttribute.nativeDataType=ネイティブのデータ型コード

#33. DimensionJoinCondition
Structure.DimensionJoinCondition.cubeKey=キューブキー

Structure.DimensionJoinCondition.hierarchyKey=階層キー

Structure.DimensionJoinCondition.level=レベル

#34. AggregationArgument
Structure.AggregationArgument=集計引数

Structure.AggregationArgument.name=引数の名前

Structure.AggregationArgument.value=引数の値

#35. Variable
Structure.Variable=変数

Structure.Variable.name=名前

Structure.Variable.value=値

#36. SortHint
Structure.SortHint=ソートのヒント

Structure.SortHint.columnName=列名

Structure.SortHint.position=列の位置

Structure.SortHint.direction=ソートの方向

Structure.SortHint.nullValueOrdering=Null 値の順序

Structure.SortHint.isOptional=オプション

#37. IncludedDataMart
Structure.IncludedDataMart=含まれる DataMart

Structure.IncludedDataMart.fileName=ファイル名

Structure.IncludedDataMart.namespace=ネームスペース

#38. TimeInterval
Structure.TimeInterval=時間の間隔

Structure.TimeInterval.measure=メジャー

Structure.TimeInterval.unit=単位

#39. FormatValue
Structure.FormatValue.category=カテゴリ

Structure.FormatValue.pattern=パターン

Structure.FormatValue.locale=ロケール

#40. CalculationArgument
Structure.CalculationArgument.name=計算の引数名

Structure.CalculationArgument.value=計算の引数値

###########################################################
# Element names
Element.Module=モジュール

Element.LayoutModule=レイアウトモジュール

Element.ReportDesign=レポートデザイン

Element.DesignElement=Design 要素

Element.ReportElement=レポートの要素

Element.DataSource=データソース

Element.JdbcDataSource=JDBC データソース

Element.DataSet=データセット

Element.SimpleDataSet=簡易データセット

Element.JdbcSelectDataSet=JDBC 選択データセット

Element.Parameter=パラメタ

Element.AbstractScalarParameter=抽象スカラーパラメタ

Element.ScalarParameter=ScalarParameter
Element.DynamicFilterParameter=動的フィルタパラメタ

Element.ParameterGroup=パラメタグループ

Element.CascadingParameterGroup=カスケードパラメタグループ

Element.Style=スタイル

Element.MasterPage=MasterPage
Element.ReportItem=レポート項目

Element.Label=ラベル

Element.AutoText=AutoText
Element.FreeForm=FreeForm
Element.Data=データ

Element.Text=テキスト

Element.Library=ライブラリ

Element.ListingGroup=リスティンググループ

Element.List=リスト

Element.ListGroup=リストグループ

Element.Listint=リスティング

Element.Table=テーブル

Element.TableGroup=テーブルグループ

Element.Column=列

Element.Row=行

Element.Cell=セル

Element.Image=イメージ

Element.Grid=グリッド

Element.Line=線

Element.Tile=並べて表示

Element.Rectangle=四角形

Element.GraphicMasterPage=グラフィックマスタページ

Element.SimpleMasterPage=簡易マスタページ

Element.TextData=ダイナミックテキスト

Element.ExtendedItem=拡張項目

Element.ScriptDataSource=スクリプトデータソース

Element.ScriptDataSet=スクリプトデータセット

Element.Listing=リスティング

Element.OdaDataSet=ODA データセット

Element.OdaDataSource=ODA データソース

Element.Theme=テーマ

Element.TemplateParameterDefinition=テンプレートのパラメタの定義

Element.TemplateElement=テンプレートの要素

Element.TemplateReportItem=テンプレートのレポート項目

Element.TemplateDataSet=テンプレートのデータセット

Element.JointDataSet=ジョインデータセット

Element.Cube=Cube
Element.Dimension=ディメンション

Element.Hierarchy=階層

Element.Level=レベル

Element.Measure=メジャー

Element.MeasureGroup=メジャーグループ

Element.TabularCube=テーブル形式のキューブ

Element.TabularDimension=テーブル形式のディメンション

Element.TabularHierarchy=テーブル形式の階層

Element.TabularLevel=テーブル形式のレベル

Element.TabularMeasure=テーブル形式のメジャー

Element.TabularMeasureGroup=テーブル形式のメジャーグループ

Element.OdaCube=ODA キューブ

Element.OdaDimension=ODA ディメンション

Element.OdaHierarchy=ODA 階層

Element.OdaLevel=ODA レベル

Element.OdaMeasure=ODA メジャー

Element.OdaMeasureGroup=ODA メジャーグループ

Element.MemberValue=メンバ値

Element.SortElement=ソート要素

Element.FilterConditionElement=フィルタの条件要素

Element.AbstractMultiViews=抽象マルチビュー

Element.MultiViews=マルチビュー

Element.VariableElement=変数

Element.DataGroup=データグループ

Element.DerivedDataSet=派生データセット

Element.DataMart=データオブジェクト

Element.DataMartDataSource=データオブジェクトデータソース

Element.DataMartDataSet=データオブジェクトのデータセット

Element.DataMartCube=データオブジェクトキューブ

Element.TimeDimension=時間のディメンション

Element.ReportItemTheme=レポート項目テーマ

Element.AbstractTheme=抽象テーマ

Element.DataMartLinkedDataModel=データオブジェクトデータモデル

Element.AliasDataSet =エイリアスデータセット

Element.LinkedDataModel =リンクされたデータモデル

Element.CategoryColumn =カテゴリ列

Element.LinkedDataModel.pushDown=データモデルプッシュ (下へ)

Element.Category = カテゴリ

Element.ColumnLink = 列リンク

Element.ColumnLinkPath = 列リンクのパス

Element.DrillPath = ドリルパス

Element.LinkedDataModelCube = リンクされたデータモデルキューブ

Element.LinkedDataModelDataSet = リンクされたデータモデルデータセット

###########################################################
# Element property names

# Module ( Prefix = Element.Module )
Element.Module.author=作成者

Element.Module.title=タイトル

Element.Module.titleID=タイトルキー

Element.Module.description=説明

Element.Module.descriptionID=説明キー

Element.Module.comments=コメント

Element.Module.includeResource=リソースファイル

Element.Module.createdBy=作成者

Element.Module.includeScripts=インクルードするスクリプト

Element.Module.scriptLibs=スクリプトライブラリ

Element.Module.externalMetadata=外部メタデータ

Element.Module.propertyBindings=プロパティのバインド

# LayoutModule ( Prefix = Element.LayoutModule )
Element.LayoutModule.subject=件名

Element.LayoutModule.helpGuide=ヘルプガイド

Element.LayoutModule.colorPalette=カラーパレット

Element.LayoutModule.configVars=変数の設定

Element.LayoutModule.images=イメージ

Element.LayoutModule.libraries=ライブラリ

Element.LayoutModule.base=基本

Element.LayoutModule.units=単位

Element.LayoutModule.theme=テーマ


# Library ( Prefix = Element.Library )
Element.Library.slot.styles=スタイル

Element.Library.slot.parameters=パラメタ

Element.Library.slot.dataSources=データソース

Element.Library.slot.dataSets=データセット

Element.Library.slot.pageSetup=ページ設定

Element.Library.slot.components=コンポーネント

Element.Library.slot.themes=テーマ

Element.Library.slot.cubes=キューブ

#1. Report Design ( Prefix = Element.ReportDesign )
Element.ReportDesign.refreshRate=更新頻度

Element.ReportDesign.eventHandlerClass=イベントハンドラのクラス

Element.ReportDesign.newHandlerOnEachEvent=各イベントの新しいハンドラ

Element.ReportDesign.beforeFactory=Factory での処理前

Element.ReportDesign.beforeFactory.this=レポートデザイン

Element.ReportDesign.beforeFactory.reportContext=レポートのコンテキスト

Element.ReportDesign.afterFactory=Factory での処理後

Element.ReportDesign.afterFactory.reportContext=レポートのコンテキスト

Element.ReportDesign.beforeOpenDoc=文書を開く前

Element.ReportDesign.beforeOpenDoc.reportContext=レポートのコンテキスト

Element.ReportDesign.afterOpenDoc=文書を開いた後

Element.ReportDesign.afterOpenDoc.reportContext=レポートのコンテキスト

Element.ReportDesign.beforeCloseDoc=文書を閉じる前

Element.ReportDesign.beforeCloseDoc.reportContext=レポートのコンテキスト

Element.ReportDesign.afterCloseDoc=文書を閉じた後

Element.ReportDesign.afterCloseDoc.reportContext=レポートのコンテキスト

Element.ReportDesign.beforeRender=レンダリング前

Element.ReportDesign.beforeRender.reportContext=レポートのコンテキスト

Element.ReportDesign.afterRender=レンダリング後

Element.ReportDesign.afterRender.reportContext=レポートのコンテキスト

Element.ReportDesign.onPageStart.this=マスタページ

Element.ReportDesign.onPageStart.reportContext=レポートのコンテキスト

Element.ReportDesign.onPageEnd.this=マスタページ

Element.ReportDesign.onPageEnd.reportContext=レポートのコンテキスト

Element.ReportDesign.displayNameID=表示名キー

Element.ReportDesign.displayName=表示名

Element.ReportDesign.iconFile=アイコンファイル

Element.ReportDesign.cheatSheet=ガイドの表示

Element.ReportDesign.thumbnail=サムネイル

Element.ReportDesign.layoutPreference=レイアウトの設定

Element.ReportDesign.bidiLayoutOrientation=レポートの向き

Element.ReportDesign.beforeFactory.toolTip=Factory での処理を開始する際に、onPrepare( ) メソッドを実行した後で、レポート文書 (該当する場合) を開く前に呼び出されます。

Element.ReportDesign.afterFactory.toolTip=Factory での処理が終了した際に、レポート文書 (該当する場合) を閉じた後に呼び出されます。これは、Factory で呼び出される最後のメソッドです

Element.ReportDesign.beforeOpenDoc.toolTip=Factory でレポート文書を開く直前に呼び出されます

Element.ReportDesign.afterOpenDoc.toolTip=Factory でレポート文書を開いた直後に呼び出されます

Element.ReportDesign.beforeCloseDoc.toolTip=Factory でレポート文書ファイルを閉じる直前に呼び出されます

Element.ReportDesign.afterCloseDoc.toolTip=Factory でレポート文書ファイルを閉じた直後に呼び出されます

Element.ReportDesign.beforeRender.toolTip=プレゼンテーション時のアクションを開始する前に呼び出されます

Element.ReportDesign.afterRender.toolTip=プレゼンテーション時のアクションを開始した後に呼び出されます

Element.ReportDesign.onPageStart.toolTip=ページの開始時に呼び出されます。

Element.ReportDesign.onPageEnd.toolTip=ページの終了時に呼び出されます。

Element.ReportDesign.slot.styles=スタイル

Element.ReportDesign.slot.parameters=パラメタ

Element.ReportDesign.slot.dataSources=データソース

Element.ReportDesign.slot.dataSets=データセット

Element.ReportDesign.slot.pageSetup=ページ設定

Element.ReportDesign.slot.components=コンポーネント

Element.ReportDesign.slot.body=本文

Element.ReportDesign.slot.scratchPad=スクラッチパッド

Element.ReportDesign.slot.templateParameterDefinitions=テンプレートのパラメタの定義

Element.ReportDesign.slot.cubes=キューブ要素

Element.ReportDesign.slot.themes=テーマ

Element.ReportDesign.initialize=初期化

Element.ReportDesign.initialize.toolTip=レポートの実行を開始する際に呼び出されます。

Element.ReportDesign.initialize.reportContext=レポートのコンテキスト

Element.ReportDesign.cssStyleSheets=CSS スタイルシート

Element.ReportDesign.enableACL=データセキュリティの有効化

Element.ReportDesign.ACLExpression=ACL 式

Element.ReportDesign.cascadeACL=ACL をカスケードする

Element.ReportDesign.imageDPI=イメージの DPI

Element.ReportDesign.pageVariables=ページ変数

Element.ReportDesign.dataObjects= データオブジェクト

Element.ReportDesign.locale=ロケール

Element.ReportDesign.onPageStart=ページの開始時

Element.ReportDesign.onPageEnd=ページの終了時

Element.ReportDesign.dataMarts=DataMarts
Element.ReportDesign.onPrepare=準備時

Element.ReportDesign.onPrepare.this=レポートデザイン

Element.ReportDesign.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.ReportDesign.onPrepare.reportContext=レポートのコンテキスト

Element.ReportDesign.onContentUpdate= 内容の更新時

Element.ReportDesign.onContentUpdate.toolTip=BIRT Viewer でレポートの内容が更新されるときにクライアント側で実行します。

Element.ReportDesign.onContentUpdate.this=レポートデザイン

Element.ReportDesign.clientInitialize=クライアント初期化

Element.ReportDesign.clientInitialize.toolTip=クライアントサイドの java スクリプトライブラリをロードします。

Element.ReportDesign.clientInitialize.this=レポートデザイン

Element.ReportDesign.language=言語

Element.ReportDesign.PDFAccessible=アクセシブル PDF

#2. Report Element ( Prefix = Element.ReportElement )
Element.ReportElement.displayName=表示名

Element.ReportElement.displayNameID=表示名キー

Element.ReportElement.comments=コメント

Element.ReportElement.propertyMask=プロパティマスク

Element.ReportElement.name=名前

Element.ReportElement.extends=拡張

Element.ReportElement.style=スタイル

Element.ReportElement.eventHandlerClass=イベントハンドラのクラス

Element.ReportElement.newHandlerOnEachEvent=各イベントの新しいハンドラ

Element.ReportElement.viewAction=アクションの表示

#3. JdbcDataSource  ( prefix  Element.JdbcDataSource )
Element.JdbcDataSource.url=URL

Element.JdbcDataSource.userName=ユーザ名

Element.JdbcDataSource.password=パスワード

Element.JdbcDataSource.driver=ドライバ

Element.JdbcDataSource.dataSource=データソース

#4. DataSet  ( prefix  Element.DataSet )
Element.DataSet.resultSetHints=結果セットのヒント

Element.DataSet.computedColumns=計算カラム

Element.DataSet.filter=フィルタ

Element.DataSet.columnHints=カラムのヒント

Element.DataSet.cachedMetaData=キャッシュされたメタデータ

Element.DataSet.rowFetchLimit=データセットからフェッチする最大行数

Element.DataSet.needsCache=データエンジン用のキャッシュが必要です。

Element.DataSet.parameters=データセットのパラメタ

Element.DataSet.sortHints=ソートのヒント

Element.DataSet.ACLExpression=ACL 式

Element.DataSet.RowACLExpression=行の ACL 式

Element.DataSet.isVisible=表示

Element.DataSet.locale=ロケール

Element.DataSet.nullsOrdering=Null の順序

#4. SimpleDataSet  ( prefix  Element.SimpleDataSet )
Element.SimpleDataSet.beforeOpen=開く前

Element.SimpleDataSet.beforeOpen.toolTip=BIRT によってデータセットが開かれる直前に呼び出されます

Element.SimpleDataSet.beforeOpen.this=データセット

Element.SimpleDataSet.beforeOpen.reportContext=レポートのコンテキスト

Element.SimpleDataSet.beforeClose=閉じる前

Element.SimpleDataSet.beforeClose.this=データセット

Element.SimpleDataSet.beforeClose.toolTip=BIRT によってデータセットが閉じられる直前に呼び出されます

Element.SimpleDataSet.beforeClose.reportContext=レポートのコンテキスト

Element.SimpleDataSet.onFetch=フェッチ時

Element.SimpleDataSet.onFetch.toolTip=BIRT によってデータセットからデータ行がフェッチされる直前に呼び出されます

Element.SimpleDataSet.onFetch.reportContext=レポートのコンテキスト

Element.SimpleDataSet.onFetch.this=データセット

Element.SimpleDataSet.onFetch.row=データセット行

Element.SimpleDataSet.afterOpen=開いた後

Element.SimpleDataSet.afterOpen.this=データセット

Element.SimpleDataSet.afterOpen.toolTip=BIRT によってデータセットが開かれた直後に呼び出されます

Element.SimpleDataSet.afterOpen.reportContext=レポートのコンテキスト

Element.SimpleDataSet.afterClose=閉じた後

Element.SimpleDataSet.afterClose.toolTip=BIRT によってデータセットが閉じられた直後に呼び出されます

Element.SimpleDataSet.afterClose.reportContext=レポートのコンテキスト

Element.SimpleDataSet.paramBindings=パラメタバインド

Element.SimpleDataSet.refTemplateParameter=参照先テンプレートのパラメタの定義

Element.SimpleDataSet.cachedRowCount=キャッシュ済みの行数

Element.SimpleDataSet.dataSetRowLimit=データセット行の制限
  
#5. JdbcSelectDataSet  ( prefix  Element.JdbcSelectDataSet )
Element.JdbcSelectDataSet.statement=文

Element.JdbcSelectDataSet.getStatement=Get 文

Element.JdbcSelectDataSet.getStatement.toolTip=クエリのカスタム SQL 文を返します
  
#6. Parameter  ( prefix  Element.Parameter )
Element.Parameter.helpText=ヘルプテキスト

Element.Parameter.helpTextID=ヘルプテキストキー

Element.Parameter.hidden=非表示

Element.Parameter.validate=検証

Element.Parameter.validate.reportContext=レポートのコンテキスト

Element.Parameter.validate.toolTip=パラメタのカスタム検証を行い、true または false を返します。

Element.Parameter.promptText=プロンプトテキスト

Element.Parameter.promptTextID=プロンプトテキストキー
  
#7. ScalarParameter  ( prefix  Element.ScalarParameter )
Element.ScalarParameter.paramType=スカラーパラメタの型

Element.ScalarParameter.displayPrompt=プロンプトの表示

Element.ScalarParameter.concealValue=値を隠す

Element.ScalarParameter.format=書式

Element.ScalarParameter.controlType=コントロール型

Element.ScalarParameter.alignment=配置

Element.ScalarParameter.selectionList=選択リスト

Element.ScalarParameter.selectionList.mustMatch=必ず一致

Element.ScalarParameter.selectionList.fixedOrder=固定順序

Element.ScalarParameter.boundDataColumns=バインドされたデータカラム

Element.ScalarParameter.autoSuggestThreshold=入力補完のしきい値

Element.ScalarParameter.getDefaultValueList=デフォルト値リストの取得

Element.ScalarParameter.getDefaultValueList.reportContext=レポートのコンテキスト

Element.ScalarParameter.getDefaultValueList.toolTip=パラメタのデフォルト値リストを返します

Element.ScalarParameter.getSelectionValueList=選択値リストの取得

Element.ScalarParameter.getSelectionValueList.reportContext=レポートのコンテキスト

Element.ScalarParameter.getSelectionValueList.toolTip=パラメタコントロール型が [リスト] または [コンボ] のときに、選択値リストを返します。

Element.ScalarParameter.defaultValue=デフォルト値
  
#8. ParameterGroup  ( prefix  Element.ParameterGroup )
Element.ParameterGroup.startExpanded=開始時に展開

Element.ParameterGroup.helpText=ヘルプテキスト

Element.ParameterGroup.helpTextID=ヘルプテキストキー

Element.ParameterGroup.promptText=プロンプトテキスト

Element.ParameterGroup.promptTextID=プロンプトテキストキー

Element.ParameterGroup.slot.parameters=パラメタ

# CascadingParameterGroup (prefix Element.CascadingParameterGroup)
Element.CascadingParameterGroup.dataSet=データセット

Element.CascadingParameterGroup.dataSetMode=1 つまたは複数のデータセット

Element.CascadingParameterGroup.slot.parameters=パラメタ

#9. MasterPage ( Prefix = Element.MasterPage )
Element.MasterPage.type=種類

Element.MasterPage.height=高さ

Element.MasterPage.width=幅

Element.MasterPage.orientation=向き

Element.MasterPage.columns=列

Element.MasterPage.columnSpacing=列の間隔

Element.MasterPage.margin=余白

Element.MasterPage.topMargin=上余白

Element.MasterPage.leftMargin=左余白

Element.MasterPage.bottomMargin=下余白

Element.MasterPage.rightMargin=右余白

Element.MasterPage.slot.contents=コンテンツ

Element.MasterPage.onPageStart=ページの開始時

Element.MasterPage.onPageEnd=ページの終了時

Element.MasterPage.onPageStart.toolTip=ページの開始時に呼び出されます。

Element.MasterPage.onPageEnd.toolTip=ページの終了時に呼び出されます。

Element.MasterPage.onPageStart.this=マスタページ

Element.MasterPage.onPageStart.reportContext=レポートのコンテキスト

Element.MasterPage.onPageEnd.this=マスタページ

Element.MasterPage.onPageEnd.reportContext=レポートのコンテキスト

#9. Style ( Prefix = Element.Style)
Element.Style.background=背景

Element.Style.background.attachment=背景の添付

Element.Style.background.color=背景色

Element.Style.background.image=背景イメージ

Element.Style.background.imageType=背景イメージの種類

Element.Style.background.positionX=左右の位置

Element.Style.background.positionY=上下の位置

Element.Style.background.repeat=背景を繰り返す

Element.Style.background.size.width=背景の幅

Element.Style.background.size.height=背景の高さ

Element.Style.border=罫線

Element.Style.border.bottom.color=一番下の罫線の色

Element.Style.border.bottom.style=一番下の罫線の線種

Element.Style.border.bottom.width=一番下の罫線の幅

Element.Style.border.fillColor=罫線の塗りつぶしの色

Element.Style.border.hmiddle.color=中央横の罫線の色

Element.Style.border.hmiddle.style=中央横の罫線の線種

Element.Style.border.hmiddle.width=中央横の罫線の幅

Element.Style.border.left.color=左の罫線の色

Element.Style.border.left.style=左の罫線の線種

Element.Style.border.left.width=左の罫線の幅

Element.Style.border.right.color=右の罫線の色

Element.Style.border.right.style=右の罫線の線種

Element.Style.border.right.width=右の罫線の幅

Element.Style.border.top.color=一番上の罫線の色

Element.Style.border.top.style=一番上の罫線の線種

Element.Style.border.top.width=一番上の罫線の幅

Element.Style.border.vmiddle.color=中央縦の罫線の色

Element.Style.border.vmiddle.style=中央縦の罫線の線種

Element.Style.border.vmiddle.width=中央縦の罫線の幅

Element.Style.box=ボックス

Element.Style.canShrink=縮小

Element.Style.display=表示

Element.Style.font=フォント

Element.Style.color=文字の色 :

Element.Style.font.family=フォントファミリ

Element.Style.font.size=サイズ

Element.Style.font.style=スタイル

Element.Style.font.textLineThrough=取り消し線

Element.Style.font.textUnderline=下線

Element.Style.font.textOverline=上線

Element.Style.font.variant=バリエーション

Element.Style.font.weight=太さ

Element.Style.format=書式

Element.Style.format.dateTime=日付/時間の書式

Element.Style.format.date=日付の書式

Element.Style.format.time=時間の書式

Element.Style.format.number=数値の書式

Element.Style.format.number.align=数値の配置

Element.Style.format.string=文字列の書式

Element.Style.highlight.testExpr=テスト式のハイライト

Element.Style.highlight.rules=ハイライトの規則

Element.Style.letterSpacing=文字間隔

Element.Style.lineHeight=行の高さ

Element.Style.map.rules=マップの規則

Element.Style.margin.top=余白 (上)

Element.Style.margin.bottom=余白 (下)

Element.Style.margin.left=余白 (左)

Element.Style.margin.right=余白 (右)

Element.Style.masterPage=マスタページ

Element.Style.padding=パディング

Element.Style.padding.bottom=パディング (下)

Element.Style.padding.left=パディング (左)

Element.Style.padding.right=パディング (右)

Element.Style.padding.top=パディング (上)

Element.Style.pageBreakBefore=要素前

Element.Style.pageBreakAfter=要素後

Element.Style.pageBreakInside=要素内

Element.Style.section=セクション

Element.Style.showIfBlank=空白でも表示

Element.Style.text=テキスト

Element.Style.text.align=文字列の位置揃え

Element.Style.text.direction=文字列の向き

Element.Style.text.indent=文字列のインデント

Element.Style.text.transform=文字列の位置変更

Element.Style.verticalAlign=垂直方向の配置

Element.Style.whiteSpace=スペース

Element.Style.widows=ウィドウ

Element.Style.orphans=オーファン

Element.Style.wordSpacing=単語間の間隔

Element.Style.overflow=オーバーフロー

Element.Style.height=高さ

Element.Style.width=幅

#10.ReportItem  ( prefix  Element.ReportItem )
Element.ReportItem.bookmark=ブックマーク

Element.ReportItem.bookmarkDisplayName=表示名のブックマーク

Element.ReportItem.toc=目次

Element.ReportItem.dataSet=データセット

Element.ReportItem.cube=データキューブ

Element.ReportItem.x=X
Element.ReportItem.y=Y
Element.ReportItem.height=高さ

Element.ReportItem.width=幅

Element.ReportItem.visibility=可視性

Element.ReportItem.paramBindings=パラメタバインド

Element.ReportItem.refTemplateParameter=参照先テンプレートのパラメタの定義

Element.ReportItem.boundDataColumns=バインドされたデータカラム

Element.ReportItem.zIndex=重ね順

Element.ReportItem.dataBindingRef=データバインドの参照

Element.ReportItem.multiViews=マルチビュー

Element.ReportItem.ACLExpression=ACL 式

Element.ReportItem.cascadeACL=ACL をカスケードする

Element.ReportItem.allowExport=エクスポートを許可する

Element.ReportItem.pushDown=プッシュ (下へ)

Element.ReportItem.theme=テーマ

Element.ReportItem.language=言語

Element.ReportItem.order=順序

Element.ReportItem.altText=代替テキスト

Element.ReportItem.altTextID=代替テキストキー

Element.ReportItem.linkedDataModel=リンクされたデータモデル

#11.Label  ( prefix  Element.Label )
Element.Label.text=コンテンツ

Element.Label.textID=コンテンツキー

Element.Label.helpTextID=ヘルプテキストキー

Element.Label.helpText=ヘルプテキスト

Element.Label.action=アクション

Element.Label.onPrepare=準備時

Element.Label.onPrepare.this=ラベル

Element.Label.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Label.onPrepare.reportContext=レポートのコンテキスト

Element.Label.onCreate=作成時

Element.Label.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Label.onCreate.this=ラベルインスタンス

Element.Label.onCreate.reportContext=レポートのコンテキスト

Element.Label.onRender=レンダリング時

Element.Label.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Label.onRender.this=ラベルインスタンス

Element.Label.onRender.reportContext=レポートのコンテキスト

Element.Label.onPageBreak=改ページ

Element.Label.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.Label.onPageBreak.this=ラベルインスタンス

Element.Label.onPageBreak.reportContext=レポートのコンテキスト

Element.Label.tagType=PDF タグの種類

Element.Label.altText=代替テキスト
  
#12.Element.FreeForm  ( Prefix Element.FreeForm )
Element.FreeForm.slot.reportItems=レポート項目
  
#12.Container  ( prefix  Element.Container )
Element.Container.slot.reportItems=レポート項目
  
#13.Data  ( prefix  Element.Data )
Element.Data.resultSetColumn=結果セットカラム

Element.Data.helpText=ヘルプテキスト

Element.Data.helpTextID=ヘルプテキストキー

Element.Data.action=アクション

Element.Data.onPrepare=準備時

Element.Data.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Data.onPrepare.this=データ

Element.Data.onPrepare.reportContext=レポートのコンテキスト

Element.Data.onCreate=作成時

Element.Data.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Data.onCreate.this=データインスタンス

Element.Data.onCreate.reportContext=レポートのコンテキスト

Element.Data.onRender=レンダリング時

Element.Data.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Data.onRender.this=データインスタンス

Element.Data.onRender.reportContext=レポートのコンテキスト

Element.Data.onPageBreak=改ページ

Element.Data.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.Data.onPageBreak.this=データインスタンス

Element.Data.onPageBreak.reportContext=レポートのコンテキスト

Element.Data.tagType=PDF タグの種類

Element.Data.altText=代替テキスト
  
#14.Text  ( prefix  Element.Text )
Element.Text.content=コンテンツ

Element.Text.contentType=コンテンツの種類

Element.Text.contentID=コンテンツキー

Element.Text.onPrepare=準備時

Element.Text.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Text.onPrepare.this=テキスト

Element.Text.onPrepare.reportContext=レポートのコンテキスト

Element.Text.onCreate=作成時

Element.Text.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Text.onCreate.this=テキストインスタンス

Element.Text.onCreate.reportContext=レポートのコンテキスト

Element.Text.onRender=レンダリング時

Element.Text.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Text.onRender.this=テキストインスタンス

Element.Text.onRender.reportContext=レポートのコンテキスト

Element.Text.onPageBreak=改ページ

Element.Text.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.Text.onPageBreak.this=テキストインスタンス

Element.Text.onPageBreak.reportContext=レポートのコンテキスト

Element.Text.hasExpression=式あり

Element.Text.tagType=PDF タグの種類

Element.Text.altText=代替テキスト
  
#15.ListingGroup  ( prefix  Element.ListingGroup )
Element.ListingGroup.keyExpr=キーの式

Element.ListingGroup.sort=ソート

Element.ListingGroup.bookmark=ブックマーク

Element.ListingGroup.bookmarkDisplayName=表示名のブックマーク

Element.ListingGroup.toc=目次

Element.ListingGroup.hideDetail=詳細の非表示

Element.ListingGroup.groupName=名前

Element.ListingGroup.Interval=間隔

Element.ListingGroup.intervalBase=間隔のベース

Element.ListingGroup.interval=間隔

Element.ListingGroup.intervalRange=間隔の範囲

Element.ListingGroup.sortDirection=ソート方向

Element.ListingGroup.eventHandlerClass=イベントハンドラのクラス

Element.ListingGroup.newHandlerOnEachEvent=各イベントの新しいハンドラ

Element.ListingGroup.pageBreakBefore=要素前

Element.ListingGroup.pageBreakAfter=要素後

Element.ListingGroup.pageBreakInside=要素内

Element.ListingGroup.filter=フィルタ

Element.ListingGroup.repeatHeader=ヘッダの繰り返し

Element.ListingGroup.onStart=開始時

Element.ListingGroup.onStart.toolTip=データセットを開いた後で、ヘッダバンドの作成前、およびこの要素のデータセットから最初の行が取得される前に呼び出されます

Element.ListingGroup.onRow=行ごと

Element.ListingGroup.onRow.toolTip=この要素のデータセットから各行が取得された際に、その行のコンテンツが作成される前に呼び出されます

Element.ListingGroup.onFinish=終了時

Element.ListingGroup.onFinish.toolTip=この要素のデータセットから最後の行が読み込まれた後で、フッタバンドが作成される前に呼び出されます

Element.ListingGroup.sortType=ソート基準

Element.ListingGroup.boundDataColumns=バインドされたデータカラム

Element.ListingGroup.viewAction=アクションの表示

Element.ListingGroup.onCreate=作成時

Element.ListingGroup.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.ListingGroup.onCreate.this=リスティンググループ

Element.ListingGroup.onCreate.reportContext=レポートのコンテキスト

Element.ListingGroup.onRender=レンダリング時

Element.ListingGroup.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.ListingGroup.onRender.this=リスティンググループ

Element.ListingGroup.onRender.reportContext=レポートのコンテキスト

Element.ListingGroup.ACLExpression=ACL 式

Element.ListingGroup.cascadeACL=ACL をカスケードする

Element.ListingGroup.showDetailFilter=フィルタの詳細を表示

#16.List  ( prefix  Element.List )
Element.List.slot.header=ヘッダ

Element.List.slot.groups=グループ

Element.List.slot.detail=詳細

Element.List.slot.footer=フッタ

Element.List.onPrepare=準備時

Element.List.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.List.onPrepare.this=リスト

Element.List.onPrepare.reportContext=レポートのコンテキスト

Element.List.onCreate=作成時

Element.List.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.List.onCreate.this=リストインスタンス

Element.List.onCreate.reportContext=レポートのコンテキスト

Element.List.onRender=レンダリング時

Element.List.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.List.onRender.this=リストインスタンス

Element.List.onRender.reportContext=レポートのコンテキスト

Element.List.onPageBreak=改ページ

Element.List.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.List.onPageBreak.this=リストインスタンス

Element.List.onPageBreak.reportContext=レポートのコンテキスト

Element.List.tagType=PDF タグの種類

Element.List.altText=代替テキスト
  
#17.ListGroup  ( prefix  Element.ListGroup )
Element.ListGroup.slot.header=ヘッダ

Element.ListGroup.slot.footer=フッタ

Element.ListGroup.onPrepare=準備時

Element.ListGroup.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.ListGroup.onPrepare.this=リストグループ

Element.ListGroup.onPrepare.reportContext=レポートのコンテキスト

Element.ListGroup.onPageBreak=改ページ

Element.ListGroup.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.ListGroup.onPageBreak.this=リストグループ

Element.ListGroup.onPageBreak.reportContext=レポートのコンテキスト
  
#18.Table  ( prefix  Element.Table )
Element.Table.caption=キャプション

Element.Table.summary=サマリ

Element.Table.isSummaryTable=サマリテーブルの設定

Element.Table.captionID=キャプションキー

Element.Table.slot.header=ヘッダ

Element.Table.slot.groups=グループ

Element.Table.slot.detail=詳細

Element.Table.slot.footer=フッタ

Element.Table.slot.columns=列

Element.Table.onPrepare=準備時

Element.Table.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Table.onPrepare.this=テーブル

Element.Table.onPrepare.reportContext=レポートのコンテキスト

Element.Table.onPrepare.object=オブジェクト

Element.Table.onCreate=作成時

Element.Table.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Table.onCreate.this=テーブルインスタンス

Element.Table.onCreate.reportContext=レポートのコンテキスト

Element.Table.onCreate.object=オブジェクト

Element.Table.onRender=レンダリング時

Element.Table.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Table.onRender.this=テーブルインスタンス

Element.Table.onRender.reportContext=レポートのコンテキスト

Element.Table.onRender.object=オブジェクト

Element.Table.onPageBreak=改ページ

Element.Table.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.Table.onPageBreak.this=テーブルインスタンス

Element.Table.onPageBreak.reportContext=レポートのコンテキスト

Element.Table.tagType=PDF タグの種類
  
#19.TableGroup  ( prefix  Element.TableGroup )
Element.TableGroup.slot.header=ヘッダ

Element.TableGroup.slot.footer=フッタ

Element.TableGroup.onPrepare=準備時

Element.TableGroup.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.TableGroup.onPrepare.this=テーブルグループ

Element.TableGroup.onPrepare.reportContext=レポートのコンテキスト

Element.TableGroup.onPageBreak=改ページ

Element.TableGroup.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.TableGroup.onPageBreak.this=テーブルグループ

Element.TableGroup.onPageBreak.reportContext=レポートのコンテキスト
  
#20.Column  ( prefix  Element.Column )
Element.Column.repeat=繰り返し

Element.Column.width=幅

Element.Column.alignment=配置

Element.Column.suppressDuplicates=重複を表示しない

Element.Column.visibility=可視性

Element.Column.viewAction=アクションの表示
  
#21.Row  ( prefix  Element.Row )
Element.Row.visibility=可視性

Element.Row.height=高さ

Element.Row.bookmark=ブックマーク

Element.Row.bookmarkDisplayName=表示名のブックマーク

Element.Row.eventHandlerClass=イベントハンドラのクラス

Element.Row.newHandlerOnEachEvent=各イベントの新しいハンドラ

Element.Row.slot.cells=セル

Element.Row.onPrepare=準備時

Element.Row.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Row.onPrepare.this=行

Element.Row.onPrepare.reportContext=レポートのコンテキスト

Element.Row.onCreate=作成時

Element.Row.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Row.onCreate.this=行インスタンス

Element.Row.onCreate.reportContext=レポートのコンテキスト

Element.Row.onRender=レンダリング時

Element.Row.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Row.onRender.this=行インスタンス

Element.Row.onRender.reportContext=レポートのコンテキスト

Element.Row.suppressDuplicates=重複を表示しない

Element.Row.viewAction=アクションの表示

Element.Row.repeatable=反復可能

Element.Row.tagType=PDF タグの種類

Element.Row.language=言語
  
#22.Cell  ( prefix  Element.Cell )
Element.Cell.column=列

Element.Cell.colSpan=列の幅

Element.Cell.rowSpan=行の幅

Element.Cell.verticalAlign=垂直方向の配置

Element.Cell.drop=削除

Element.Cell.height=高さ

Element.Cell.slot.content=コンテンツ

Element.Cell.width=幅

Element.Cell.eventHandlerClass=イベントハンドラのクラス

Element.Cell.newHandlerOnEachEvent=各イベントの新しいハンドラ

Element.Cell.scope=スコープ

Element.Cell.bookmark=ブックマーク

Element.Cell.bookmarkDisplayName=表示名のブックマーク

Element.Cell.headers=ヘッダ

Element.Cell.onPrepare=準備時

Element.Cell.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Cell.onPrepare.this=セル

Element.Cell.onPrepare.reportContext=レポートのコンテキスト

Element.Cell.onCreate=作成時

Element.Cell.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Cell.onCreate.this=セルインスタンス

Element.Cell.onCreate.reportContext=レポートのコンテキスト

Element.Cell.onRender=レンダリング時

Element.Cell.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Cell.onRender.this=セルインスタンス

Element.Cell.onRender.reportContext=レポートのコンテキスト

Element.Cell.viewAction=アクションの表示

Element.Cell.diagonal=ダイアゴナル

Element.Cell.diagonalNumber=対角線番号

Element.Cell.antidiagonalNumber=非対角線番号

Element.Cell.diagonalThickness=対角線の幅

Element.Cell.diagonalColor=対角線の色

Element.Cell.antidiagonalThickness=非対角線の幅

Element.Cell.diagonalStyle=対角線のスタイル

Element.Cell.antidiagonalStyle=非対角線のスタイル

Element.Cell.antidiagonalColor=非対角線の色

Element.Cell.tagType=PDF タグの種類

Element.Cell.language=言語

Element.Cell.altText=代替テキスト
  
#23.Image  ( prefix  Element.Image )
Element.Image.altText=代替テキスト

Element.Image.altTextID=代替テキストキー

Element.Image.helpText=ヘルプテキスト

Element.Image.helpTextID=ヘルプテキストキー

Element.Image.size=サイズ

Element.Image.scale=スケール

Element.Image.source=ソース

Element.Image.uri=URI
Element.Image.imageName=イメージ名 :

Element.Image.valueExpr=値式

Element.Image.typeExpr=式の入力

Element.Image.action=アクション

Element.Image.onPrepare=準備時

Element.Image.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Image.onPrepare.this=イメージ

Element.Image.onPrepare.reportContext=レポートのコンテキスト

Element.Image.onCreate=作成時

Element.Image.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Image.onCreate.this=イメージインスタンス

Element.Image.onCreate.dataRow=データ行

Element.Image.onCreate.reportContext=レポートのコンテキスト

Element.Image.onRender=レンダリング時

Element.Image.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Image.onRender.this=イメージインスタンス

Element.Image.onRender.reportContext=レポートのコンテキスト

Element.Image.onPageBreak=改ページ

Element.Image.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.Image.onPageBreak.this=イメージインスタンス

Element.Image.onPageBreak.reportContext=レポートのコンテキスト

Element.Image.fitToContainer=コンテナに合わせる

Element.Image.proportionalScale=プロポーショナルスケール

Element.Image.tagType=PDF タグの種類
  
#24.Grid  ( prefix  Element.Grid )
Element.Grid.slot.columns=列

Element.Grid.slot.rows=行

Element.Grid.onPrepare=準備時

Element.Grid.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.Grid.onPrepare.this=グリッド

Element.Grid.onPrepare.reportContext=レポートのコンテキスト

Element.Grid.onCreate=作成時

Element.Grid.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.Grid.onCreate.this=グリッドインスタンス

Element.Grid.onCreate.dataRow=データ行

Element.Grid.onCreate.reportContext=レポートのコンテキスト

Element.Grid.onRender=レンダリング時

Element.Grid.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.Grid.onRender.this=グリッドインスタンス

Element.Grid.onRender.reportContext=レポートのコンテキスト

Element.Grid.onPageBreak=改ページ

Element.Grid.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.Grid.onPageBreak.this=グリッドインスタンス

Element.Grid.onPageBreak.reportContext=レポートのコンテキスト

Element.Grid.caption=キャプション

Element.Grid.captionID=キャプションキー

Element.Grid.summary=サマリ

Element.Grid.tagType=PDF タグの種類
  
#25.Line  ( prefix  Element.Line )
Element.Line.orientation=向き
  
#26.TextData  ( prefix  Element.TextData )
Element.TextData.valueExpr=値式

Element.TextData.contentType=コンテンツの種類

Element.TextData.onPrepare=準備時

Element.TextData.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.TextData.onPrepare.this=ダイナミックテキスト

Element.TextData.onPrepare.reportContext=レポートのコンテキスト

Element.TextData.onPrepare.object=オブジェクト

Element.TextData.onCreate=作成時

Element.TextData.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.TextData.onCreate.this=ダイナミックテキストインスタンス

Element.TextData.onCreate.dataRow=データ行

Element.TextData.onCreate.reportContext=レポートのコンテキスト

Element.TextData.onRender=レンダリング時

Element.TextData.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.TextData.onRender.this=ダイナミックテキストインスタンス

Element.TextData.onRender.reportContext=レポートのコンテキスト

Element.TextData.onPageBreak=改ページ

Element.TextData.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.TextData.onPageBreak.this=ダイナミックテキストインスタンス

Element.TextData.onPageBreak.reportContext=レポートのコンテキスト

Element.TextData.hasExpression=式あり

Element.TextData.tagType=PDF タグの種類

Element.TextData.altText=代替テキスト

#27.ExtendedItem   ( prefix  Element.ExtendedItem )
Element.ExtendedItem.altText=代替テキスト

Element.ExtendedItem.altTextID=代替テキストキー

Element.ExtendedItem.extensionName=拡張子名 :

Element.ExtendedItem.filter=フィルタ

Element.ExtendedItem.onPrepare=準備時

Element.ExtendedItem.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.ExtendedItem.onPrepare.this=拡張項目

Element.ExtendedItem.onPrepare.reportContext=レポートのコンテキスト

Element.ExtendedItem.onCreate=作成時

Element.ExtendedItem.onCreate.toolTip=Factory で要素を作成する際に実行されます

Element.ExtendedItem.onCreate.this=拡張項目インスタンス

Element.ExtendedItem.onCreate.reportContext=レポートのコンテキスト

Element.ExtendedItem.onRender=レンダリング時

Element.ExtendedItem.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際に実行されます。

Element.ExtendedItem.onRender.this=拡張項目インスタンス

Element.ExtendedItem.onRender.reportContext=レポートのコンテキスト

Element.ExtendedItem.onPageBreak=改ページ

Element.ExtendedItem.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.ExtendedItem.onPageBreak.this=拡張項目インスタンス

Element.ExtendedItem.onPageBreak.reportContext=レポートのコンテキスト

Element.ExtendedItem.extensionVersion=拡張バージョン

#28.DataSource ( prefix Element.DataSource )
Element.DataSource.beforeOpen=開く前

Element.DataSource.beforeOpen.toolTip=このメソッドは、BIRT によってデータソースが開かれる直前に呼び出されます

Element.DataSource.beforeOpen.this=データソース

Element.DataSource.beforeOpen.reportContext=レポートのコンテキスト

Element.DataSource.beforeClose=閉じる前

Element.DataSource.beforeClose.toolTip=このメソッドは、BIRT によってデータソースが閉じられる直前に呼び出されます

Element.DataSource.beforeClose.reportContext=レポートのコンテキスト

Element.DataSource.beforeClose.this=データソース

Element.DataSource.afterOpen=開いた後

Element.DataSource.afterOpen.toolTip=このメソッドは、BIRT によってデータソースが開かれた直後に呼び出されます

Element.DataSource.afterOpen.reportContext=レポートのコンテキスト

Element.DataSource.afterOpen.this=データソース

Element.DataSource.afterClose=閉じた後

Element.DataSource.afterClose.toolTip=このメソッドは、BIRT によってデータソースが閉じられた直後に呼び出されます

Element.DataSource.afterClose.reportContext=レポートのコンテキスト

#29.ScriptDataSource ( prefix Element.ScriptDataSource )
Element.ScriptDataSource.open=開く

Element.ScriptDataSource.open.toolTip=データソースへの接続を確立します

Element.ScriptDataSource.open.this=スクリプトデータソース

Element.ScriptDataSource.close=閉じる

Element.ScriptDataSource.close.toolTip=データソースへの接続を切断します

Element.ScriptDataSource.close.this=スクリプトデータソース

#31 ScriptDataSet ( prefix Element.ScriptDataSet )
Element.ScriptDataSet.dataSource=データソース

Element.ScriptDataSet.open=開く

Element.ScriptDataSet.open.toolTip=データセットを開きます

Element.ScriptDataSet.open.this=スクリプトデータセット

Element.ScriptDataSet.describe=記述

Element.ScriptDataSet.describe.toolTip=結果セットを記述します

Element.ScriptDataSet.describe.reportContext=レポートのコンテキスト

Element.ScriptDataSet.describe.this=スクリプトデータセット

Element.ScriptDataSet.describe.dataSetMetaData=データセットのメタデータ

Element.ScriptDataSet.fetch=フェッチ

Element.ScriptDataSet.fetch.currentRow=現在の行

Element.ScriptDataSet.fetch.toolTip=DataRow 型のオブジェクトを作成し、読み込み、返します

Element.ScriptDataSet.fetch.reportContext=レポートのコンテキスト

Element.ScriptDataSet.fetch.this=スクリプトデータセット

Element.ScriptDataSet.close=閉じる

Element.ScriptDataSet.close.toolTip=データセットを閉じます

Element.ScriptDataSet.close.this=スクリプトデータセット

Element.ScriptDataSet.resultSetHints=結果セットのヒント

#32 Listing ( prefix Element.Lising )
Element.Listing.sorting=ソート

Element.Listing.filter=フィルタ

Element.Listing.repeatHeader=ヘッダの繰り返し

Element.Listing.pageBreakInterval=改ページの間隔

Element.Listing.sortByGroups=グループを基準にソート

Element.Listing.onStart=開始時

Element.Listing.onStart.toolTip=データセットを開いた後で、ヘッダバンドの作成前、およびこの要素のデータセットから最初の行が取得される前に呼び出されます

Element.Listing.onRow=行ごと

Element.Listing.onRow.toolTip=この要素のデータセットから各行が取得された際に、その行のコンテンツが作成される前に呼び出されます

Element.Listing.onFinish=終了時

Element.Listing.onFinish.toolTip=この要素のデータセットから最後の行が読み込まれた後で、フッタバンドが作成される前に呼び出されます

#33 GraphicMasterPage ( Prefix Element.GraphicMasterPage)
Element.GraphicMasterPage.slot.contents=コンテンツ

#34 SimpleMasterPage ( Prefix Element.SimpleMasterPage)
Element.SimpleMasterPage.showHeaderOnFirst=先頭ページにヘッダを表示

Element.SimpleMasterPage.showFooterOnLast=最終ページにフッタを表示

Element.SimpleMasterPage.headerHeight=ヘッダの高さ

Element.SimpleMasterPage.footerHeight=フッタの高さ

Element.SimpleMasterPage.floatingFooter=浮動フッタ

Element.SimpleMasterPage.slot.pageHeader=ページヘッダ

Element.SimpleMasterPage.slot.pageFooter=ページフッタ

#35 OdaDataSource ( Prefix Element.OdaDataSource )
Element.OdaDataSource.driverName=ドライバ名

Element.OdaDataSource.extensionID=拡張 ID

Element.OdaDataSource.publicDriverProperties=パブリックドライバのプロパティ

Element.OdaDataSource.privateDriverProperties=プライベートドライバのプロパティ

Element.OdaDataSource.designerState=デザイナの状態

#36 OdaDataSet ( Prefix Element.OdaDataSet )
Element.OdaDataSet.extensionID=拡張 ID

Element.OdaDataSet.resultSet=結果セットの列

Element.OdaDataSet.queryText=クエリ

Element.OdaDataSet.type=種類

Element.OdaDataSet.resultSetName=結果セット名

Element.OdaDataSet.publicDriverProperties=パブリックドライバのプロパティ

Element.OdaDataSet.privateDriverProperties=プライベートドライバのプロパティ

Element.OdaDataSet.designerState=デザイナの状態

Element.OdaDataSet.designerValues=Designer の値

Element.OdaDataSet.resultSetNumber=結果セット番号

Element.OdaDataSet.dataSource=データソース

#37 DesignElement ( Prefix Element.DesignElement )
Element.DesignElement.customXml=カスタム XML

Element.DesignElement.propertyMasks=プロパティマスク

Element.DesignElement.userProperties=ユーザのプロパティ

#38 Theme ( Prefix Element.Theme )
Element.Theme.slot.styles=スタイル

#39 TemplateParameterDefinition ( Prefix Element.TemplateParameterDefinition )
Element.TemplateParameterDefinition.name=名前

Element.TemplateParameterDefinition.allowedType=使用可能な種類

Element.TemplateParameterDefinition.description=説明

Element.TemplateParameterDefinition.descriptionID=説明 ID

Element.TemplateParameterDefinition.slot.default=デフォルト

#40 TemplateElement ( Prefix Element.TemplateElement )
Element.TemplateElement.name=名前

Element.TemplateElement.refTemplateParameter=参照先テンプレートのパラメタ

Element.TemplateElement.displayName=表示名

Element.TemplateElement.displayNameID=表示名 ID

#41 JointDataset ( Prefix Element.JointDataset )
Element.JointDataSet.resultSet=結果セットの列

Element.JointDataSet.dataSets=データセット

Element.JointDataSet.joinConditions=ジョインの条件

#42 Rectangle  ( prefix  Element.Rectangle )

#43. TemplateReportItem
Element.TemplateReportItem.visibility=可視性

#44.AutoText ( prefix Element.AutoText )
Element.AutoText.type = 定型句の種類

Element.AutoText.onPrepare=準備時

Element.AutoText.onPrepare.this=AutoText
Element.AutoText.onPrepare.toolTip=要素の設定中に実行します。 ここで要素の設計を変更します。 データバインドはまだ使用できません。

Element.AutoText.onPrepare.reportContext=レポートのコンテキスト

Element.AutoText.onCreate=作成時

Element.AutoText.onCreate.toolTip=ファクトリで要素を作成する際にスクリプトが実行されます。

Element.AutoText.onCreate.this=定型句のインスタンス

Element.AutoText.onCreate.reportContext=レポートのコンテキスト

Element.AutoText.onRender=レンダリング時

Element.AutoText.onRender.toolTip=プレゼンテーションエンジンで要素をレンダリング用に準備する際にスクリプトが実行されます

Element.AutoText.onRender.this=定型句のインスタンス

Element.AutoText.onRender.reportContext=レポートのコンテキスト

Element.AutoText.onPageBreak=改ページ

Element.AutoText.onPageBreak.toolTip=改ページが発生する際に、すべてのレポート項目に対して実行されます

Element.AutoText.onPageBreak.this=定型句のインスタンス

Element.AutoText.onPageBreak.reportContext=レポートのコンテキスト

Element.AutoText.pageVariable=ページ変数

#45. Cube ( prefix Element.Cube )
Element.Cube.filter=フィルタ条件

Element.Cube.defaultMeasureGroup=デフォルトのメジャーグループ

Element.Cube.ACLExpression=ACL 式

#46. Dimension ( prefix Element.Dimension )
Element.Dimension.isTimeType=Is time 型

Element.Dimension.defaultHierarchy=デフォルトの階層

Element.Dimension.ACLExpression=ACL 式

#47. Hierarchy ( prefix Element.Hierarchy )
Element.Hierarchy.filter=フィルタ条件

#48. Level ( prefix Element.Level )
Element.Level.dataType=データ型

Element.Level.dateTimeLevelType=日付と時間のレベルの種類

Element.Level.dateTimeFormat=日付/時間の書式

Element.Level.Interval=間隔

Element.Level.intervalBase=間隔の基準

Element.Level.interval=間隔

Element.Level.intervalRange=間隔の範囲

Element.Level.levelType=レベルの型

Element.Level.staticValues=静的な値

Element.Level.defaultValue=デフォルト値

Element.Level.ACLExpression=ACL 式

Element.Level.MemberACLExpression=メンバの ACL 式

Element.Level.action=アクション

Element.Level.format=書式

Element.Level.alignment=配置

#49. Measure ( prefix Element.Measure )
Element.Measure.function=関数

Element.Measure.isCalculated=計算

Element.Measure.measureExpression=メジャー式

Element.Measure.dataType=データ型

Element.Measure.ACLExpression=ACL 式

Element.Measure.action=アクション

Element.Measure.format=書式

Element.Measure.alignment=配置

Element.Measure.isVisible=表示する

#50. MeasureGroup ( prefix Element.MeasureGroup )
Element.MeasureGroup.measures=メジャー

#51. TabularCube ( prefix Element.TabularCube )
Element.TabularCube.dataSet=データセット

Element.TabularCube.dimensions=ディメンション

Element.TabularCube.measureGroups=メジャーグループ

Element.TabularCube.dimensionConditions=ディメンション条件

Element.TabularCube.autoKey=自動プライマリキー

#52. TabularDimension ( prefix Element.TabularDimension )
Element.TabularDimension.hierarchies=階層

Element.TabularDimension.internalDimensionRef=内部ディメンションの参照

#53. TabularHierarchy ( prefix Element.TabularHierarchy )
Element.TabularHierarchy.dataSet=データセット

Element.TabularHierarchy.levels=レベル

Element.TabularHierarchy.primaryKeys=プライマリキー

#54. TabularLevel ( prefix Element.TabularLevel )
Element.TabularLevel.columnName=列名

Element.TabularLevel.displayColumnName=列名の表示

Element.TabularLevel.attributes=属性

#55. OdaCube ( prefix Element.OdaCube )
Element.OdaCube.dimensions=ディメンション

Element.OdaCube.measureGroups=メジャーグループ

Element.OdaCube.nativeName=ネイティブの名前

#56. OdaDimension ( prefix Element.OdaDimension )
Element.OdaDimension.hierarchies=階層

Element.OdaDimension.nativeName=ネイティブの名前

#57. OdaHierarchy ( prefix Element.OdaHierarchy )
Element.OdaHierarchy.levels=レベル

Element.OdaHierarchy.nativeName=ネイティブの名前

#58. OdaLevel ( prefix Element.OdaLevel )
Element.OdaLevel.attributes=属性

#59. MemberValue ( prefix Element.MemberValue )
Element.MemberValue.value=値

Element.MemberValue.level=レベル

Element.MemberValue.memberValues=メンバ値

Element.MemberValue.filter=フィルタ

#60. SortElement ( prefix Element.SortElement )
Element.SortElement.key=キー

Element.SortElement.direction=方向

Element.SortElement.member=メンバ

Element.SortElement.strength=ソートの度合い

Element.SortElement.locale=ソートのロケール

#61. FilterConditionElement ( prefix Element.FilterConditionElement )
Element.FilterConditionElement.operator=演算子

Element.FilterConditionElement.expr=式

Element.FilterConditionElement.value1=値 1

Element.FilterConditionElement.value2=値 2

Element.FilterConditionElement.filterTarget=フィルタのターゲット

Element.FilterConditionElement.member=メンバ

Element.FilterConditionElement.isOptional=Is オプション

Element.FilterConditionElement.extensionName=拡張子名

Element.FilterConditionElement.extensionExprId=拡張式 ID

Element.FilterConditionElement.pushDown=プッシュ (下へ)

Element.FilterConditionElement.dynamicFilterParameter=動的フィルタパラメタ

Element.FilterConditionElement.type=種類

Element.FilterConditionElement.updateAggregation=集計の更新


#62. AbstractMultiViews ( prefix Element.AbstractMultiViews )
Element.AbstractMultiViews.index=インデックス

#63. MultiViews ( prefix Element.MultiViews )
Element.MultiViews.views=ビュー

#64. VariableElement ( prefix Element.VariableElement )
Element.VariableElement.name=名前

Element.VariableElement.value=値

Element.VariableElement.type=種類

#65. DataGroup  ( prefix  Element.DataGroup )
Element.DataGroup.groupName=名前

Element.DataGroup.keyExpr=キーの式

Element.DataGroup.sort=ソート

Element.DataGroup.filter=フィルタ

Element.DataGroup.Interval=間隔

Element.DataGroup.interval=間隔

Element.DataGroup.intervalBase=間隔の基準

Element.DataGroup.intervalRange=間隔の範囲

Element.DataGroup.sortDirection=ソート方向

Element.DataGroup.sortType=ソート基準

#66. DerivedDataSet ( prefix Element.DerivedDataSet )
Element.DerivedDataSet.inputDataSets=データセットの入力

Element.DerivedDataSet.queryText=クエリテキスト

Element.DerivedDataSet.extensionID=拡張 ID

#67. AbstractScalarParameter  ( prefix  AbstractScalarParameter )
Element.AbstractScalarParameter.isRequired=必須

Element.AbstractScalarParameter.dynamicList=動的なリスト

Element.AbstractScalarParameter.dynamicList.dataSetName=データセット名

Element.AbstractScalarParameter.dynamicList.linkedDataModelName=リンクされたデータモデル名

Element.AbstractScalarParameter.dynamicList.valueExpr=値式

Element.AbstractScalarParameter.dynamicList.labelExpr=ラベル式

Element.AbstractScalarParameter.listLimit=一覧の制限

Element.AbstractScalarParameter.sortDirection=ソート方向

Element.AbstractScalarParameter.sortBy=方向

Element.AbstractScalarParameter.valueType=値の型

Element.AbstractScalarParameter.sortByColumn=列を基準に並べ替える

Element.AbstractScalarParameter.selectionList=選択リスト

Element.AbstractScalarParameter.dataType=データ型

Element.AbstractScalarParameter.distinct=個別

#68. DynamicFilterParameter  ( prefix  Element.DynamicFilterParameter )
Element.DynamicFilterParameter.displayType=表示型

Element.DynamicFilterParameter.filterOperator=演算子

Element.DynamicFilterParameter.column=列

Element.DynamicFilterParameter.defaultValue=デフォルト値

Element.DynamicFilterParameter.nativeDataType=ネイティブのデータ型

Element.DynamicFilterParameter.controlType=コントロール型

#78 DataMart ( prefix  Element.DataMart )
Element.DataMart.slot.dataSources=データソース

Element.DataMart.slot.dataSets=データセット

Element.DataMart.slot.cubes=キューブ

Element.DataMart.slot.parameters=パラメタ

Element.DataMart.slot.dimensions=共有ディメンション

Element.DataMart.enableACL=データセキュリティの有効化

Element.DataMart.ACLExpression=ACL 式

Element.DataMart.enableIncrementalUpdate=増分更新を有効にする

#79 DataMartDataSource ( prefix Element.DataMartDataSource )
Element.DataMartDataSource.datamartURL =Data Mart URL

Element.DataMartDataSource.datamartVersion=Data Mart バージョン

Element.DataMartDataSource.paramBindings=パラメタバインド

Element.DataMartDataSource.accessType=アクセスの種類

#80 DataMartDataSet ( prefix Element.DataMartDataSet )
Element.DataMartDataSet.dataSource=データソース

Element.DataMartDataSet.dataObject=データオブジェクト

Element.DataMartDataSet.selectedColumns=選択済みの列

#81 DataMartCube ( prefix Element.DataMartCube )
Element.DataMartCube.dataSource=データソース

Element.DataMartCube.dataObject=データオブジェクト

#82 TimeDimension ( prefix Element.TimeDimension )
Element.TimeDimension.startTimePoint=開始時刻ポイント

Element.TimeDimension.endTimePoint=終了時刻ポイント

Element.TimeDimension.hierarchies=階層

#83 DataMartLinkedDataModel ( prefix Element.DataMartLinkedDataModel )
Element.DataMartLinkedDataModel.dataSource=データソース

Element.DataMartLinkedDataModel.dataObject=データオブジェクト

#63 ReportItemTheme ( Prefix Element.ReportItemTheme )
Element.ReportItemTheme.slot.styles=スタイル

Element.ReportItemTheme.type=種類

Element.ReportItemTheme.customValues=カスタム値

#64 AbstractTheme ( Prefix Element.AbstractTheme )
Element.AbstractTheme.cssStyleSheets=CSS スタイルシート

#Category
Element.Category.helpText = ヘルプテキスト

Element.Category.helpTextKey = ヘルプテキストリソースキー

#Category Column
Element.CategoryColumn.aggregationFunction = 集計関数

Element.CategoryColumn.aliasDataSet = エイリアスデータセット

Element.CategoryColumn.dataType = データ型

Element.CategoryColumn.expression =式

Element.CategoryColumn.resultSetColumnName = 結果セットの列名

Element.CategoryColumn.helpText =ヘルプテキスト

Element.CategoryColumn.ACLExpression =ACL 式

Element.CategoryColumn.heading =見出し

Element.CategoryColumn.horizontalAlign =横方向の配置

Element.CategoryColumn.format =書式

Element.CategoryColumn.valueFormat =値の書式

Element.CategoryColumn.action =アクション

Element.CategoryColumn.analysis =解析

###########################################################
# Predefined style
Style.Report=レポート

Style.List=リスト


Style.GroupHeader1=GroupHeader1
Style.GroupHeader2=GroupHeader1
Style.GroupHeader3=GroupHeader1
Style.GroupHeader4=GroupHeader1
Style.GroupHeader5=GroupHeader1
Style.GroupHeader6=GroupHeader1
Style.GroupHeader7=GroupHeader1
Style.GroupHeader8=GroupHeader8
Style.GroupHeader9=GroupHeader9
Style.GroupFooter1=GroupFooter1
Style.GroupFooter2=GroupFooter2
Style.GroupFooter3=GroupFooter3
Style.GroupFooter4=GroupFooter4
Style.GroupFooter5=GroupFooter5
Style.GroupFooter6=GroupFooter6
Style.GroupFooter7=GroupFooter7
Style.GroupFooter8=GroupFooter8
Style.GroupFooter9=GroupFooter9

Style.ListHeader=ListHeader
Style.ListFooter=ListFooter
Style.ListGroupHeader1=ListGroupHeader1

Style.ListGroupHeader2=ListGroupHeader2

Style.ListGroupHeader3=ListGroupHeader3

Style.ListGroupHeader4=ListGroupHeader4

Style.ListGroupHeader5=ListGroupHeader5

Style.ListGroupHeader6=ListGroupHeader6

Style.ListGroupHeader7=ListGroupHeader7

Style.ListGroupHeader8=ListGroupHeader8

Style.ListGroupHeader9=ListGroupHeader9

Style.ListGroupFooter1=ListGroupFooter1

Style.ListGroupFooter2=ListGroupFooter2

Style.ListGroupFooter3=ListGroupFooter3

Style.ListGroupFooter4=ListGroupFooter4

Style.ListGroupFooter5=ListGroupFooter5

Style.ListGroupFooter6=ListGroupFooter6

Style.ListGroupFooter7=ListGroupFooter7

Style.ListGroupFooter8=ListGroupFooter8

Style.ListGroupFooter9=ListGroupFooter9


Style.ListDetail=ListDetail
Style.Table=テーブル

Style.TableHeader=TableHeader
Style.TableHeaderCell=TableHeaderCell
Style.TableFooter=TableFooter
Style.TableFooterCell=TableFooterCell
Style.TableGroupHeader1=TableGroupHeader1

Style.TableGroupHeader2=TableGroupHeader2

Style.TableGroupHeader3=TableGroupHeader3

Style.TableGroupHeader4=TableGroupHeader4

Style.TableGroupHeader5=TableGroupHeader5

Style.TableGroupHeader6=TableGroupHeader6

Style.TableGroupHeader7=TableGroupHeader7

Style.TableGroupHeader8=TableGroupHeader8

Style.TableGroupHeader9=TableGroupHeader9

Style.TableGroupHeaderCell=TableGroupHeaderCell

Style.TableGroupFooter1=TableGroupFooter1

Style.TableGroupFooter2=TableGroupFooter2

Style.TableGroupFooter3=TableGroupFooter3

Style.TableGroupFooter4=TableGroupFooter4

Style.TableGroupFooter5=TableGroupFooter5

Style.TableGroupFooter6=TableGroupFooter6

Style.TableGroupFooter7=TableGroupFooter7

Style.TableGroupFooter8=TableGroupFooter8

Style.TableGroupFooter9=TableGroupFooter9

Style.TableGroupFooterCell=TableGroupFooterCell


Style.TableColumnHeader=TableColumnHeader
Style.TableDetail=TableDetail
Style.TableDetailCell=TableDetailCell
Style.Page=ページ

Style.Matrix=マトリックス

Style.MatrixRowHeader1=MatrixRowHeader1

Style.MatrixRowHeader2=MatrixRowHeader2

Style.MatrixRowHeader3=MatrixRowHeader3

Style.MatrixRowHeader4=MatrixRowHeader4

Style.MatrixRowHeader5=MatrixRowHeader5

Style.MatrixRowHeader6=MatrixRowHeader6

Style.MatrixRowHeader7=MatrixRowHeader7

Style.MatrixRowHeader8=MatrixRowHeader8

Style.MatrixRowHeader9=MatrixRowHeader9

Style.MatrixRowFooter1=MatrixRowFooter1

Style.MatrixRowFooter2=MatrixRowFooter2

Style.MatrixRowFooter3=MatrixRowFooter3

Style.MatrixRowFooter4=MatrixRowFooter4

Style.MatrixRowFooter5=MatrixRowFooter5

Style.MatrixRowFooter6=MatrixRowFooter6

Style.MatrixRowFooter7=MatrixRowFooter7

Style.MatrixRowFooter8=MatrixRowFooter8

Style.MatrixRowFooter9=MatrixRowFooter9

Style.MatrixColumnHeader1=MatrixColumnHeader1

Style.MatrixColumnHeader2=MatrixColumnHeader2

Style.MatrixColumnHeader3=MatrixColumnHeader3

Style.MatrixColumnHeader4=MatrixColumnHeader4

Style.MatrixColumnHeader5=MatrixColumnHeader5

Style.MatrixColumnHeader6=MatrixColumnHeader6

Style.MatrixColumnHeader7=MatrixColumnHeader7

Style.MatrixColumnHeader8=MatrixColumnHeader8

Style.MatrixColumnHeader9=MatrixColumnHeader9

Style.MatrixColumnFooter1=MatrixColumnFooter1

Style.MatrixColumnFooter2=MatrixColumnFooter2

Style.MatrixColumnFooter3=MatrixColumnFooter3

Style.MatrixColumnFooter4=MatrixColumnFooter4

Style.MatrixColumnFooter5=MatrixColumnFooter5

Style.MatrixColumnFooter6=MatrixColumnFooter6

Style.MatrixColumnFooter7=MatrixColumnFooter7

Style.MatrixColumnFooter8=MatrixColumnFooter8

Style.MatrixColumnFooter9=MatrixColumnFooter9


Style.MatrixCell=MatrixCell
Style.Chart=グラフ

Style.ChartTitle=ChartTitle
Style.ChartLegend=ChartLegend
Style.ChartValue=ChartValue
Style.ChartAxis=ChartAxis
Style.Label=ラベル

Style.Data=データ

Style.Number=数値

Style.String=文字列

Style.Text=テキスト

Style.Grid=グリッド

Style.Toc=目次レベル 0

Style.Toc1=目次レベル 1

Style.Toc2=目次レベル 2

Style.Toc3=目次レベル 3

Style.Toc4=目次レベル 4

Style.Toc5=目次レベル 5

Style.Toc6=目次レベル 6

Style.Toc7=目次レベル 7

Style.Toc8=目次レベル 8

Style.Toc9=目次レベル 9


Style.FreeForm=FreeForm
Style.Line=線

Style.Rectangle=四角形

Style.TextData=TextData
Style.ExtendedItem=ExtendedItem
Style.Image=イメージ

###########################################################
# Messages.
Message.NamePrefixNew=新規

Message.ChangeProperty=プロパティを変更してください : {0}

Message.ChangePropertyDefinition=プロパティの定義を変更してください。

Message.MoveItem=項目を移動してください。

Message.AddTranslation=変換情報を追加してください。

Message.DropTranslation=変換情報を削除してください。

Message.ChangeTranslation=変換情報を変更してください。

Message.MoveContent=コンテンツを移動してください。

Message.AddProperty=プロパティを追加してください。

Message.DropProperty=プロパティを削除してください。

Message.SetName=名前を設定してください。

Message.AddElement=要素を追加してください。

Message.DropElement=要素を削除してください。

Message.SetStyle=スタイルを設定してください。

Message.ReplaceItem=項目を置換してください。

Message.SetLocale=ロケールを設定してください。

Message.SetTranslationText=変換文字列を設定してください。

Message.MoveElement=要素を移動してください。

Message.InsertElement=要素を挿入してください。

Message.DeleteElement=要素を削除してください。

Message.SetExtends=拡張を設定してください。

Message.ChangeItem=項目を変更してください。

Message.AddItem=項目を追加してください。

Message.InsertItem=項目を挿入してください。

Message.RemoveItem=項目を削除してください。

Message.ReplaceElement=要素の置換

Message.SetTheme=テーマの設定

Message.ChangePropertyEncryption=プロパティ {0} の暗号化を変更します。

Message.RenameCSSFile=CSS ファイルの名前の変更

Message.ImportCSSStyles=CSS スタイルのインポート

Message.InsertAndPasteColumnBand=コピーした列の挿入

Message.PasteColumnBand=列の貼り付け

Message.ShiftColumnBand=列のシフト

Message.InsertColumnBand=列の挿入

Message.InsertRow=行の挿入

Message.InsertAndPasteRow=コピーした行の挿入

Message.PasteRow=行の貼り付け

Message.ShiftRow=行のシフト

Message.ClearProperties=プロパティのクリア

Message.CreateTemplateElement=テンプレートの要素の作成

Message.TransformToReportItem=レポート項目に変換

Message.TransformToDataSet=データセットに変換

Message.ExportElementToLibrary=要素をライブラリにエクスポート

Message.ExportElementToDataMart=要素を DataMart にエクスポート

###########################################################
#Element new names.

New.ReportDesign=NewReportDesign
New.Parameter=NewParameter
New.CascadingParameterGroup=新しいカスケードパラメタグループ

New.ScalarParameter=NewParameter
New.DynamicFilterParameter=新しい動的フィルタパラメタ

New.ParameterGroup=NewParameterGroup
New.Style=NewStyle
New.MasterPage=NewMasterPage
New.ReportItem=NewReportItem
New.Label=NewLabel
New.FreeForm=NewFreeForm
New.Data=NewData
New.Text=NewText
New.ListingGroup=NewListingGroup
New.List=NewList
New.ListGroup=NewListGroup
New.Listint=NewListing
New.Table=NewTable
New.TableGroup=NewTableGroup
New.Column=NewColumn
New.Row=新しい行

New.Cell=NewCell
New.Image=NewImage
New.Grid=NewGrid
New.Line=NewLine
New.Tile=NewTile
New.Rectangle=NewRectangle
New.GraphicMasterPage=新しいグラフィックマスタページ

New.SimpleMasterPage=新しい簡易マスタページ

New.TextData=NewTextData
New.ExtendedItem=NewExtendedItem
New.ScriptDataSource=新しいスクリプトデータソース

New.ScriptDataSet=新しいスクリプトデータセット

New.OdaDataSource=新しいODAデータソース

New.OdaDataSet=新しいODAデータセット

New.TemplateParameterDefinition=新しいテンプレートのパラメタ定義

New.TemplateDataSet=新しいテンプレートのデータセット

New.TemplateReportItem=新しいテンプレートのレポート項目

New.Theme=NewTheme
New.AutoText=NewAutoText
New.TabularCube=データキューブ

New.TabularDimension=グループ

New.TabularHierarchy=NewTabularHierarchy
New.TabularLevel=NewTabularLevel
New.TabularMeasure=NewTabularMeasure
New.TabularMeasureGroup=サマリフィールド

New.OdaCube=新規 ODA キューブ

New.OdaDimension=NewOdaDimension
New.OdaHierarchy=NewOdaHierarchy
New.OdaLevel=NewOdaLevel
New.OdaMeasure=NewOdaMeasure
New.OdaMeasureGroup=NewOdaMeasureGroup
New.DerivedDataSet=NewDerivedDataSet
New.VariableElement=NewVariableElement
New.DataMartDataSource=NewDataMartDataSource
New.DataMartDataSet=NewDataMartDataSet
New.DataMartCube=NewDataMartCube
New.TimeDimension=NewTimeDimension
New.ReportItemTheme=新規レポート項目テーマ

New.CategoryColumn=NewCategoryColumn

###########################################################
#Default theme name.
Theme.defaultThemeName=defaultTheme

###########################################################
# Error Messages

#ContentException
Error.ContentException.SLOT_NOT_FOUND={0} にはスロット (ID : {1}) が定義されていません。

Error.ContentException.WRONG_TYPE={1} は {0} のスロット ({2}) では使用できません。

Error.ContentException.CONTENT_NOT_FOUND={0} は、{2} のスロット ({1}) に表示されません。

Error.ContentException.NOT_CONTAINER={0} はコンテナではありません。

Error.ContentException.SLOT_IS_FULL={1} のスロット ({0}) はいっぱいです。

Error.ContentException.RECURSIVE={0} のいずれかのレベルに {1} が含まれているため、前者を後者に移動できません。

Error.ContentException.HAS_NO_CONTAINER={0} にはコンテナがないため、レポートに追加できません。

Error.ContentException.MOVE_FORBIDDEN={0} またはそのコンテンツが参照されているため、{0} を移動できません。

Error.ContentException.HAS_DESCENDENTS={0} には下位レベルが含まれるため、移動または削除できません。

Error.ContentException.INVALID_CONTEXT_CONTAINMENT={0} を直接または間接的に {1} のスロット ({2}) で使用することはできません。

Error.ContentException.STRUCTURE_CHANGE_FORBIDDEN=要素 "{0}" は、子要素または仮想要素 (子要素の内側) のため、構造を変更できません。

Error.ContentException.CONTENT_NAME_REQUIRED={0} には名前が付いていないため、{1} のスロット {2} に追加できません。

Error.ContentException.INVALID_TEMPLATE_ELEMENT={0} にはテンプレートのパラメタが明示的に定義されていないため、{1} のスロット ({2}) に挿入できません。

Error.ContentException.TEMPLATE_TRANSFORM_FORBIDDEN=この要素はテンプレートに変換できません。

Error.ContentException.GROUPS_CHANGE_FORBIDDEN=要素 "{0}" には共有のレポート項目があるため、グループの追加、削除、移動は行えません。

Error.ContentException.CONTENT_NOT_ALLOWED_PASTED=コピーはできないため、{0} のスロット {1} に貼り付けられません。

Error.ContentException.CONTENT_ALREADY_INSERTED=要素 "{0}" はすでにデザインツリーに存在するため "{1}" の ({2}) に追加できません。

Error.ContentException.INVALID_POSITION=位置が範囲外か無効です。

Error.ContentException.SHARE_DIMENSION_NOT_EXIST=レポート / ライブラリに要素 [{0}] を挿入できません。共有ディメンション [{1}] が存在しません。

#CustomMsgException
Error.CustomMsgException.RESOURCE_KEY_REQUIRED=変換情報のリソースキーが必要です。

Error.CustomMsgException.DUPLICATE_LOCALE=リソース "{1}" のロケール "{0}" の変換情報はすでに定義されています。

Error.CustomMsgException.INVALID_LOCALE="{0}" は無効なロケール名です。

Error.CustomMsgException.TRANSLATION_NOT_FOUND=リソース "{1}" のロケール "{0}" の変換情報が見つかりません。

    
#ExtendsException

#InvalidParentException
Error.InvalidParentException.NO_PARENT=要素 "{0}" には親がないため、ローカライズできません。

Error.InvalidParentException.PARENT_NOT_FOUND=親要素 "{0}" がありません。

Error.InvalidParentException.UNNAMED_PARENT=親要素に名前が指定されていません。

Error.InvalidParentException.PARENT_NOT_INCLUDE=親要素 "{0}" のライブラリがインクルードされていません。

#WrongTypeException
Error.WrongTypeException.WRONG_TYPE=親 {0} の要素型は、ターゲット {1} の要素型と異なります。

Error.WrongTypeException.WRONG_EXTENSION_TYPE=親 {0} の拡張型は、ターゲット {1} の要素の拡張型と異なります。

#CircularExtendsException
Error.CircularExtendsException.SELF_EXTEND={0} はそれ自体では拡張されません。

Error.CircularExtendsException.CIRCULAR=循環拡張です。{0} は、すでに {1} から拡張されています。

#ExtendsForbiddenException
Error.ExtendsForbiddenException.PARENT_NOT_IN_COMPONENT=親要素 "{0}" は、コンポーネント内での最上位のレポート項目ではありません。

Error.ExtendsForbiddenException.EXTENDS_FORBIDDEN=要素 "{0}" は、子要素の内側にあるかすでに親要素があるため、拡張できません。

Error.ExtendsForbiddenException.CANT_EXTEND=要素 "{0}" を派生させることはできません。

Error.ExtendsForbiddenException.RESULT_SET_SHARED_CANT_EXTEND=共有している結果セットのレポート項目 "{0}" を派生させることはできません。
    
#NameException
Error.NameException.NAME_REQUIRED={0} に名前を指定する必要があります。

Error.NameException.NAME_FORBIDDEN={0} には名前を指定できません。

Error.NameException.DUPLICATE=この名前 "{0}" は、すでに使用されています。別の名前を付けてください。

Error.NameException.HAS_REFERENCES={0} には、派生した要素があります。名前をクリアできません。

Error.NameException.INVALID_NAME="{0}" は無効な名前です。名前には "." (ピリオド)、"/" (スラッシュ)、"\" (円マーク)、"!" (感嘆符)、";" (セミコロン)、"," (コンマ) を使用できません。

Error.NameException.INVALID_STYLE_NAME=スタイル名 "{0}" は CSS2 の指定に対して無効です。
    
#PropertyNameException
Error.PropertyNameException.PROPERTY_NOT_VALID=プロパティ "{0}" は、要素 "{1}" では使用できません。

Error.PropertyNameException.MEMBER_NOT_VALID=メンバ "{0}" は、構造 "{1}" では使用できません。

    
#PropertyValueException
Error.PropertyValueException.INVALID_VALUE=値 "{0}" は、型 "{1}" では使用できません。

Error.PropertyValueException.PROPERTY_CHANGE_FORBIDDEN=要素 "{1}" は、子要素の内側にあるため、プロパティ "{0}" の値を変更できません。

Error.PropertyValueException.CHOICE_NOT_FOUND=選択値 "{0}" が見つかりません。

Error.PropertyValueException.NOT_LIST_TYPE=プロパティ "{0}" はリスト型ではありません。

Error.PropertyValueException.ITEM_NOT_FOUND=指定された構造がプロパティ "{0}" に見つかりません。

Error.PropertyValueException.WRONG_ELEMENT_TYPE="{0}" は、有効な要素の参照型ではありません。

Error.PropertyValueException.VALUE_EXISTS=値 "{0}" はすでに存在します。

Error.PropertyValueException.VALUE_REQUIRED=プロパティ "{0}" には値を指定する必要があります。

Error.PropertyValueException.VALUE_LOCKED=プロパティ "{0}" はロックされているため、値を指定できません。

Error.PropertyValueException.UNIT_NOT_ALLOWED=プロパティ "{1}" には単位 "{0}" を使用できません。

Error.PropertyValueException.CHOICE_NOT_ALLOWED=選択値 "{0}" は使用できません。

Error.PropertyValueException.WRONG_ITEM_TYPE=構造型は "{1}" ではなく "{0}" です。

Error.PropertyValueException.NEGATIVE_VALUE=プロパティ "{1}" の値 "{0}" が無効です。負の値を指定することはできません。

Error.PropertyValueException.NON_POSITIVE_VALUE=プロパティ "{1}" の値 "{0}" が無効です。正の値を指定する必要があります。

Error.PropertyValueException.EXTENSION_SETTING_FORBIDDEN=拡張項目 "{1}" のプロパティ "{0}" は、コマンドによって指定することはできません。

Error.PropertyValueException.DOT_FORBIDDEN="{0}" は無効な名前です。名前には "." (ピリオド) を使用できません。

Error.PropertyValueException.NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE=レポート項目テーマの型 "{0}" はサポートされていません。
    
#SemanticError
Error.SemanticError.INCONSISTENT_TABLE_COL_COUNT=テーブル "{0}" の列数に不整合があります。

Error.SemanticError.INCONSISTENT_TABLE_COL_COUNT_COZ_DROP=グループヘッダ内のいくつかのセルのドロッププロパティが原因で、テーブル "{0}" の列数に不整合があります。

Error.SemanticError.INCONSISTENT_GRID_COL_COUNT=グリッド "{0}" の列数に不整合があります。

Error.SemanticError.OVERLAPPING_CELLS={0} の行に重なり合ったセルがあります。

Error.SemanticError.INCONSISTENT_TABLE_DROP_HEADINGS=テーブル "{0}" に定義されているドロップ見出しは無効です。

Error.SemanticError.INVALID_PAGE_SIZE=マスタページ "{0}" のページサイズが無効です。

Error.SemanticError.MISSING_PAGE_SIZE=マスタページ "{0}" のページの種類がカスタムのため、サイズを設定する必要があります。

Error.SemanticError.CANNOT_SPECIFY_PAGE_SIZE=マスタページ "{0}" のページの種類がカスタムでないため、サイズを指定できません。

Error.SemanticError.INVALID_PAGE_MARGINS=マスタページ "{0}" の余白の値が無効です。

Error.SemanticError.MISSING_MASTER_PAGE=レポートには、マスタページが少なくとも 1 つ含まれている必要があります。

Error.SemanticError.INVALID_MULTI_COLUMN=マスタページ "{0}" のページコンテンツの外部に列の幅が設定されています。

Error.SemanticError.INVALID_ELEMENT_REF=要素 "{0}" には、未定義の要素または要素自体を参照するプロパティ "{1}" が含まれています : "{2}"

Error.SemanticError.INVALID_DATA_BINDING_REF=要素 "{0}" には、要素 "{1}" (コンテナまたは要素の内容) を参照するプロパティ dataBindingRef が含まれています。

Error.SemanticError.MISSING_SQL_STMT=JDBC 選択データセット "{0}" には、SQL 文が含まれている必要があります。

Error.SemanticError.MISSING_DATA_SET={0} は、1 つのデータセットにアクセスできる必要があります。

Error.SemanticError.INVALID_IMAGE_SCALE_VALUE=イメージ "{0}" のスケールは正の値である必要があります。

Error.SemanticError.IMAGE_FILE_NOT_FOUND=イメージ "{1}" で定義されているファイル "{0}" が見つかりません。

Error.SemanticError.NEGATIVE_REPORT_REFRESH_RATE=レポートの更新頻度には、負の値を設定することはできません。

Error.SemanticError.NEGATIVE_CELL_COLUMN=テーブル/グリッド/テーブルグループ "{0}" には、負の値で始まる列が含まれています。

Error.SemanticError.NEGATIVE_WIDTH=要素 "{0}" またはそのコンテンツには、負の値の幅を設定することはできません。

Error.SemanticError.NEGATIVE_HEIGHT=要素 "{0}" またはそのコンテンツには、負の値の高さを設定することはできません。

Error.SemanticError.INVALID_COLUMN_REPEAT=テーブル "{0}" には、繰り返し回数が 1 より小さい列が含まれています。

Error.SemanticError.INVALID_PAGE_COLUMNS=マスタページ "{0}" には、1 より小さい列が含まれています。

Error.SemanticError.NEGATIVE_FONT_SIZE=スタイル "{0}" のフォントサイズには、負の値を設定することはできません。

Error.SemanticError.NEGATIVE_BORDER_WIDTH=スタイル "{0}" の罫線の幅には、負の値を設定することはできません。

Error.SemanticError.INVALID_PROPERTY_NAME=要素 "{1}" のプロパティ "{0}" が定義されていません。

Error.SemanticError.UNSUPPORTED_ELEMENT=要素 {0} はまだサポートされていません。

Error.SemanticError.AT_LEAST_ONE_COLUMN=データセット "{0}" の結果セットには、少なくとも 1 つの結果セット列が含まれている必要があります。

Error.SemanticError.NEGATIVE_LINE_HEIGHT=要素 "{0}" の行の高さには、負の値を設定することはできません。

Error.SemanticError.DUPLICATE_GROUP_NAME=テーブル/リスト "{0}" には、重複するグループ名 "{1}" が含まれています。

Error.SemanticError.INVALID_CUSTOM_COLOR_NAME="{0}" は CSS の標準のカラー名のため、カスタムカラー名として使用できません。

Error.SemanticError.DUPLICATE_CUSTOM_COLOR_NAME=カラーパレットに、重複するカスタムカラー名 "{0}" が含まれています。

Error.SemanticError.EXTENSION_NOT_FOUND=この要素 "{1}" の拡張子名 "{0}" が見つかりません。

Error.SemanticError.MISSING_EXTENSION=この要素 "{0}" には、拡張子名を定義しなければなりません。

Error.SemanticError.PROPERTY_COPY_FORBIDDEN=このプロパティ "{0}" はコピーできません。

Error.SemanticError.COLUMN_COPY_FORBIDDEN=要素 "{1}" の列 "{0}" をコピーすることはできません。

Error.SemanticError.COLUMN_PASTE_FORBIDDEN=要素 "{1}" を貼り付けることはできません。

Error.SemanticError.COLUMN_PASTE_DIFFERENT_LAYOUT=コピー領域と貼り付け領域の形が違うため、情報を貼り付けることができません。

Error.SemanticError.INVALID_STRUCTURE_REF=この要素 "{0}" には、未定義の構造 "{2}" を参照するプロパティ "{1}" が含まれています。

Error.SemanticError.INVALID_SCALAR_PARAMETER_TYPE=スカラーパラメタ "{0}" は無効な型です。カスケードパラメタグループに属しているパラメタの型は、動的である必要があります。

Error.SemanticError.MISSING_TEMPLATE_PARAMETER_TYPE=要素 {0} の "allowedType" を明示的に定義する必要があります。

Error.SemanticError.INCONSISTENT_TEMPLATE_PARAMETER_TYPE=要素 {0} のデフォルトの要素 {1} は {2} 型ではありません。

Error.SemanticError.INVALID_LIBRARY_REFERENCE={0} のライブラリ参照 "{2}" に埋め込んだ "{1}" は無効です。参照した構造が存在しません。

Error.SemanticError.INVALID_PROPERTY_BINDING_ID={0} にあるプロパティのバインド "{1}" は、存在しない要素を参照しています。

Error.SemanticError.PROPERTY_BINDING_FORBIDDEN=デザインツリーには要素 "{0}" がないため、プロパティのバインドを設定できません。

Error.SemanticError.INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT=マスターページ "{0}" の内部では、テーブルやリストを直接・間接的に使用することができません。

Error.SemanticError.DATA_SET_MISSED_IN_JOINT_DATA_SET = ジョインデータセット "{0}" にデータセット "{1}" がありません。

Error.SemanticError.MISSING_COLUMN_BINDING = カラム名 "{1}" のデータ "{0}" のカラムバインドが定義されていません。

Error.SemanticError.COLUMN_INSERT_FORBIDDEN=要素 "{0}" を挿入することはできません。

Error.SemanticError.CANNOT_SPECIFY_VALUE=\"{4}\" の値 \"{3}\" が原因で、要素 "{0}"は値 \"{1}\" をプロパティ \"{2}\" に指定できません。

Error.SemanticError.ROW_COPY_FORBIDDEN=要素 "{0}" をコピーすることはできません。

Error.SemanticError.ROW_PASTE_FORBIDDEN=要素 "{0}" を貼り付けることはできません。

Error.SemanticError.ROW_INSERT_FORBIDDEN=要素 "{0}" を挿入することはできません。

Error.SemanticError.ROW_SHIFT_FORBIDDEN=要素 "{0}" をシフトすることはできません。

Error.SemanticError.ROW_INSERTANDPASTE_FORBIDDEN=要素 "{0}" の挿入や貼り付けはできません。

Error.SemanticError.CIRCULAR_ELEMENT_REFERNECE=要素の参照が循環しています。{0} は、すでに {1} を参照しています。

Error.SemanticError.INCONSISTENT_DATA_GROUP=要素 {0} のデータバインドの参照には、その参照先の要素 {1} とは異なる数のグループがあります。

Error.SemanticError.INVALID_MANIFEST=要素 {0} に無効なマニフェストがあります。

Error.SemanticError.VALUE_FORBIDDEN=リスト型のプロパティ "{0}" が空です。項目を使用することができません。

Error.SemanticError.TABLE_NO_COLUMN_FOUND = 列が見つからないため、テーブルの幅を計算できません。

Error.SemanticError.TABLE_COLUMN_WITH_NO_WIDTH = インデックス {0} の列の幅が定義されていないため、テーブルの幅を計算できません。

Error.SemanticError.TABLE_COLUMN_INCONSISTENT_UNIT_TYPE = テーブルに、割合以外の絶対単位および相対単位の複数の列が含まれているため、幅を計算できません。

Error.SemanticError.TABLE_COLUMN_INCONSISTENT_RELATIVE_UNIT = テーブルに、異なる相対単位の複数の列が含まれているため、幅を計算できません。

Error.SemanticError.TABLE_COLUMN_ILLEGAL_PERCENTAGE = 列幅の割合の合計が 100% 以上であるため、テーブルの幅を計算できません。

Error.SemanticError.CANNOT_SPECIFY_DATA_OBJECT=外部のレポート項目が別のキューブやデータセット、およびマルチビューを定義できるため、レポート項目 "{0}" ではキューブやデータセットを指定できません。

#SemanticException
Error.SemanticException.EXPORT_ELEMENT_FAIL=ターゲットライブラリで要素名が重複しています。要素 {0} をエクスポートできません。エクスポートされた要素名を変更してください。

#StyleException
Error.StyleException.FORBIDDEN={0} にはスタイルを適用できません。

Error.StyleException.NOT_FOUND={1} で使用されているスタイル "{0}" が見つかりません。
    
#ThemeException
Error.ThemeException.NOT_FOUND={1} で使用されているテーマ "{0}" が見つかりません。

Error.ThemeException.WRONG_TYPE={1} で使用されているテーマ "{0}" は、レポート項目テーマの種類が正しくありません。
    
#DesignParserException
Error.DesignParserException.FILE_NOT_FOUND=ファイル "{0}" が見つかりません。

Error.DesignParserException.FILE_FORMAT_NOT_SUPPORT="{0}" ファイルの書式はサポートされていません。

Error.DesignParserException.RGB_REQUIRED=カスタムカラーには、正しい RGB 値を指定する必要があります。

Error.DesignParserException.COLOR_NAME_REQUIRED=カスタムカラーには、カラー名を指定する必要があります。

Error.DesignParserException.ILLEGAL_EXTENDS=要素を拡張できません。

Error.DesignParserException.IMAGE_REF_CONFLICT=イメージに、競合する参照が含まれます。

Error.DesignParserException.INVALID_IMAGEREF_EXPR_VALUE=イメージの参照式の値が無効です。

Error.DesignParserException.INVALID_IMAGE_URL_VALUE=イメージの URL の値が無効です。

Error.DesignParserException.INVALID_IMAGE_NAME_VALUE=イメージの名前が無効です。

Error.DesignParserException.ACTION_REPORTNAME_REQUIRED=このアクションを実行するには、レポート名が必要です。

Error.DesignParserException.ACTION_PARAMETER_NAME_REQUIRED=このアクションを実行するには、パラメタ名が必要です。

Error.DesignParserException.CHOICE_RESTRICTION_VIOLATION=選択制限に違反しています。

Error.DesignParserException.MESSAGE_KEY_REQUIRED=ユーザ定義のメッセージには、キーの値を指定する必要があります。

Error.DesignParserException.DUPLICATE_TRANSLATION_LOCALE=ユーザ定義のメッセージに、同じロケールの変換情報が 2 つ表示されます。

Error.DesignParserException.NAME_REQUIRED=要素のプロパティ名または構造メンバ名が必要です。

Error.DesignParserException.WRONG_STRUCTURE_LIST_TYPE=指定された要素のプロパティまたは構造メンバは、構造リスト型ではありません。

Error.DesignParserException.WRONG_EXTENDED_PROPERTY_TYPE=このプロパティは、拡張プロパティではありません。

Error.DesignParserException.INVALID_STRUCTURE_NAME=この名前は、構造名ではありません。

Error.DesignParserException.INVALID_PROPERTY_SYNTAX=プロパティは、要素の属性として定義する必要があります。

Error.DesignParserException.UNDEFINED_PROPERTY=プロパティ "{0}" が定義されていません。

Error.DesignParserException.UNSUPPORTED_ENCODING=この XML ファイルのエンコード型はサポートされていません。

Error.DesignParserException.PROPERTY_IS_NOT_ENCRYPTABLE=プロパティ "{0}" を暗号化できません。

Error.DesignParserException.UNSUPPORTED_VERSION=このレポートファイルは、サポート対象のバージョンよりも新しいバージョン "{0}" で作成されているため、無効です。

Error.DesignParserException.INVALID_VERSION=バージョン "{0}" のレポートは無効です。

Error.DesignParserException.INVALID_ELEMENT_ID={0} の ID ({1}) が無効です。

Error.DesignParserException.VIRTUAL_PARENT_NOT_FOUND=BaseID "{0}" が仮想の親要素を参照できないか、参照した親が親要素にありません。

Error.DesignParserException.DUPLICATE_ELEMENT_ID={0} と {1} の ID と重複しています。

Error.DesignParserException.INCONSISTENT_TEMPLATE_ELEMENT_TYPE=テンプレートの要素 {0} は、参照先テンプレートのパラメタの定義 {1} のデフォルトの要素と整合性がありません。

Error.DesignParserException.MISSING_TEMPLATE_PARAMETER_DEFAULT=要素 {0} のデフォルトの要素を明示的に定義する必要があります。

Error.DesignParserException.WRONG_SIMPLE_LIST_TYPE=指定された要素のプロパティは、簡易リスト型ではありません。

#DesignFileException
Error.DesignFileException.SYNTAX_ERROR=構文エラーが検出されました。エラーの詳細を参照してください。

Error.DesignFileException.SEMANTIC_WARNING=セマンティックの警告が検出されました。警告の詳細を参照してください。

Error.DesignFileException.SEMANTIC_ERROR=セマンティックエラーが検出されました。エラーの詳細を参照してください。

Error.DesignFileException.INVALID_XML=このデザインファイルは、有効な XML ファイルではありません。

#XMLParserException
Error.XMLParserException.UNKNOWN_TAG=この XML ファイルには、サポートされていない要素が含まれています。

Error.XMLParserException.SAX_ERROR=SAX によって、ファイルの基本 XML 構文のエラーが検出されました。

Error.XMLParserException.INVALID_BOOLEAN=Boolean 属性に有効な値が含まれていません。

Error.XMLParserException.WARNINGS_FOUND=解析が完了しましたが、回復可能なエラーが発生しました。

Error.XMLParserException.EXCEPTION=一般的な例外が発生しました。

Error.XMLParserException.INVALID_INTEGER=integer 属性に無効な値が含まれています。
    
#UserPropertyException
Error.UserPropertyException.NAME_REQUIRED=ユーザプロパティには、名前を指定する必要があります。

Error.UserPropertyException.DUPLICATE_NAME="{0}" という名前のユーザプロパティは、すでに使用されています。

Error.UserPropertyException.INVALID_TYPE=ユーザプロパティの種類が無効です。

Error.UserPropertyException.MISSING_CHOICES=ユーザプロパティの種類は choice ですが、choice が定義されていません。

Error.UserPropertyException.INVALID_DISPLAY_ID=ユーザプロパティ "{0}" の表示名 ID および表示名が定義されていません。

Error.UserPropertyException.NOT_FOUND=ユーザプロパティ "{0}" が見つかりません。

Error.UserPropertyException.USER_PROP_DISALLOWED={0} には、ユーザプロパティを設定できません。

Error.UserPropertyException.CHOICE_VALUE_REQUIRED=ユーザプロパティ "{0}" のユーザー定義の choice には、値を指定する必要があります。

Error.UserPropertyException.CHOICE_NAME_REQUIRED=ユーザプロパティ "{0}" のユーザー定義の choice には、名前を指定する必要があります。

Error.UserPropertyException.INVALID_CHOICE_VALUE=この choice の値は、ユーザプロパティ "{0}" の型には使用できません。

Error.UserPropertyException.INVALID_DEFINITION=ユーザプロパティの定義が無効です。

Error.UserPropertyException.INVALID_DEFAULT_VALUE="{3}" 型では、{0} のユーザプロパティ "{1}" のデフォルト値として "{2}" を使用できません。

#MetaDataReaderException
Error.MetaDataReaderException.META_DATA_ERROR=このメタデータは無効です。

#LibraryException
Error.LibraryException.LIBRARY_NOT_FOUND=名前空間 "{0}" のライブラリが見つかりません。

Error.LibraryException.DUPLICATE_LIBRARY_NAMESPACE=ライブラリの "{0}" という名前空間はすでに存在します。

Error.LibraryException.LIBRARY_INCLUDED_RECURSIVELY="{0}" という名前空間を持つライブラリが繰り返し含まれています。

Error.LibraryException.LIBRARY_HAS_DESCENDENTS=ライブラリ "{0}" には現在のモジュールに含まれる下位レベル "{1}" があるため、削除できません。

Error.LibraryException.LIBRARY_ALREADY_INCLUDED=ライブラリ "{0}" はすでにインクルードされています。同じライブラリを 2 回追加することはできません。

Error.LibraryException.INVALID_LIBRARY_NAMESPACE=ライブラリの名前空間 "{0}" が無効です。空であるか、または無効な文字が含まれています。

#StyleSheetException
Error.StyleSheetException.STYLE_SHEET_NOT_FOUND=スタイルシートのリソースが見つかりません。

Error.StyleSheetException.SYNTAX_ERROR=CSS2 に従っていない構文エラーがあります。

#IncludedCssStyleSheetException
Error.CSSException.CSS_NOT_FOUND="{0}" という名前のスタイルシートが見つかりません。

Error.CSSException.DUPLICATE_CSS="{0}" という名前の CSS スタイルシートはすでに存在します。

Error.CSSException.BADCSSFILE="{0}" ファイルに構文エラーがあります。

Error.CssException.READONLY="{0}" ファイルのスタイルが読み取り専用です。

#StyleSheetParserException
Error.StyleSheetParserException.RULE_NOT_SUPPORTED=規則 "{0}" はサポートされていません。

Error.StyleSheetParserException.STYLE_NOT_SUPPORTED=スタイル "{0}" はサポートされていません。

Error.StyleSheetParserException.PROPERTY_NOT_SUPPORTED=CSS2 プロパティ "{0}" の値 "{1}" はサポートされていません。

Error.StyleSheetParserException.INVALID_SHORT_HAND_CSSPROPERTY_VALUE=ショートハンドプロパティ "{0}" の値 "{1}" は、CSS2 の文法では無効です。

Error.StyleSheetParserException.INVALID_SIMPLE_CSSPROPERTY_VALUE=プロパティ "{0}" の値 "{1}" は無効です。
	
#TemplateException
Error.TemplateException.INVALID_TEMPLATE_ELEMENT_TYPE={0} はレポート項目またはデータセット要素ではないため、テンプレートの要素に変換できません。

Error.TemplateException.TRANSFORM_TO_REPORT_ITEM_FORBIDDEN={0} にはテンプレートのパラメタが明示的に定義されていないため、レポート項目に変換できません。

Error.TemplateException.TRANSFORM_TO_DATA_SET_FORBIDDEN={0} にはテンプレートのパラメタが明示的に定義されていないため、データセットに変換できません。

Error.TemplateException.REVERT_TO_TEMPLATE_FORBIDDEN={0} にはテンプレートのパラメタが明示的に定義されていないため、テンプレートの要素に戻すことはできません。

Error.TemplateException.CREATE_TEMPLATE_ELEMENT_FORBIDDEN={0} はレポートに含まれていないため、変換できません。

Error.TemplateException.TEMPLATE_ELEMENT_NOT_SUPPORTED=ライブラリでは、これらのテンプレートの要素を使用できません。

#VersionInfo
Info.VersionInfo.INVALID_DESIGN_FILE=開こうとしたデザインファイルは、有効なデザインファイルではないか、ファイルが存在しません。

Info.VersionInfo.CONVERT_INFO=このデザインファイルは、以前のバージョンの BIRT で作成されています。[OK] をクリックすると、現在のバージョンでサポートされている形式に変換されます。

Info.VersionInfo.INVALID_VERSION=バージョン "{0}" のレポートファイルは使用できません。

Info.VersionInfo.LATER_VERSION=デザインファイルは後発バージョンの BIRT で作成されています。 [OK] をクリックすると、現在のバージョンでサポートされている形式に変換されます。

Info.VersionInfo.EXTENSION_COMPATIBILITY =開いているデザインやライブラリまたはそれらに含まれているライブラリに下位互換が適用されたあとで、そのデザインまたはライブラリに変更が加えられた可能性があります。

#EncryptionException
Error.EncryptionException.INVALID_ENCRYPTABLE_PROPERTY={0} のプロパティ {1} は暗号化できないため、暗号化を変更できません。

Error.EncryptionException.INVALID_ENCRYPTION=この暗号は存在しないため、暗号化 ID "{0}" は無効です。

#Dashboard Exporter
Message.ExportGadgetToSharedGadgetFile=ガジェットを共有ガジェットファイルにエクスポートします。

Message.ExportTabToSharedTabFile=タブを共有タブファイルにエクスポートします。

#Dashboard Exception
Error.ExternalGadgetFileIncludedRecursively=外部ガジェットが繰り返し含まれています。

Error.NotFoundExporter=ダッシュボードエクスポータが見つかりません。
