###########################################################
#Primitive Charts

SideBySideBar=並列棒グラフ。各系列の棒が横に並んで表示されます。各棒の幅はすべて等しく、プロットされる系列の数に基づいて決まります。
StackedBar=積み上げ棒グラフ。積み重なった横棒で表示されます。正の値と負の値は、原点をはさんで上下にそれぞれ積み重ねられます。
PercentStackedBar=積み上げ棒グラフ (割合)。積み上げ棒グラフの高さの合計 (各単位の最低点から最高点まで) は 100 になります。
OverlayLine=オーバーレイ線グラフ。各系列の折れ線がそれぞれ個別に表示されます。折れ線は、系列の値を結合します。
StackedLine=積み上げ線グラフ。積み重なった横線で表示されます。正の値と負の値は、原点をはさんで上下にそれぞれ積み重ねられます。
PercentStackedLine=積み上げ線グラフ (割合)。積み上げ線グラフの高さの合計 (各単位の最低点から最高点まで) は 100 になります。
OverlayArea=オーバーレイ面積グラフ。各系列の面積がそれぞれ個別に表示されます。面積は、系列の値を結合します。
StackedArea=積み上げ面積グラフ。積み重なった面積で表示されます。正の値と負の値は、原点をはさんで上下にそれぞれ積み重ねられます。
PercentStackedArea=積み上げ面積グラフ (割合)。積み上げ面積グラフの高さの合計 (各単位の最低点から最高点まで) は 100 になります。
Pie=パイグラフ。それぞれの値を 1 つのスライスとして表します。各スライスの大きさは、全体に占める割合を表します。複数の系列をパイグラフで表す場合は、各系列につき 1 つのパイがプロットされ、複数のパイグラフが作成されます。
StandardMeter=標準メーターグラフには、1つのダイヤルが含まれます。ダイヤルの背景は、いくつかの領域に色分けできます。
SuperImposedMeter=重ね合わせメーターグラフには、同じ領域を持つ複数のダイヤルが含まれます。各ダイヤルは重なり合い、1つの領域に複数の針を表示できます。
Scatter=分布グラフ。カテゴリデータと値データを座標とし、値を点でプロットします。各データ値はマーカで示します。
BarStickStock=棒株価グラフ。棒状の縦線とそれらを結ぶ 2 本の横線から構成されます。縦線の上下のポイントは株価の始値と終値を示します。2 本の横線は、高値と安値を示します。
CandleStickStock=ローソク足株価グラフ。ボックスとその上下両端から伸びる線から構成されます。ボックスの上下の両端は株価の始値と終値を示します。各線の上端は高値を表し、下端は安値を表します。
Bubble=バブルチャートは分布グラフを拡張したもので、データポイントがバブルとして表示されます。バブルの直径は、x と y の座標値に次ぐ 3 番目のデータ値に比例します。
Difference=差分チャートは、値を示す 2 本の線の間の領域を表します。領域系列のすべてのプロパティを継承し、マイナスの領域は別の色で示すこともできます。
Gantt=ガントチャートはタスクの期間と進捗状況を追跡するためのグラフです。Y 軸で日時を示し、値を棒で示します。棒の幅や開始マーカと終了マーカはユーザが指定できます。各棒はカテゴリ、タスクラベル、開始時間と終了時間によって定義されます。異なる幅のガント系列をいくつか重ねて、予定の作業の実際の進捗状況を追跡することもできます。
Tube=円筒グラフは、棒グラフを円筒で示したものです。
Cone=円錐グラフは、棒グラフを円錐で示したものです。
Pyramid=ピラミッドグラフは、棒グラフをピラミッドで示したものです。

###########################################################
#3D Charts

Bar=立体並列棒グラフ。
Line=立体オーバーレイ線グラフ。
Area=立体オーバーレイ面積グラフ。

###########################################################
#Combination Charts

BarAndLine=棒グラフと折れ線グラフを組み合わせた 2 系列のグラフ。同じカテゴリ値をそれぞれの方式でグラフ化します。


###########################################################
#Formatted Charts

AxisLabelFormat=Y 軸のラベル (\\###,###) と X 軸のラベル (yyyy/mm/dd) の形式を指定します。ラベルの色や、フォント、外枠など、その他の属性も設定できます。
AxisTitleFormat=すべてのラベル属性を軸タイトルに適用できます。
ColoredByCategorySeries=凡例項目の型をカテゴリとして設定します。
OpenJavaSourceAction.Text.Description=Java ソースを該当するディレクトリから開きます。
OpenJavaSourceAction.Text.ToolTip=Java ソースを開く
SaveXMLAction.MessageBox.Message=ディレクトリには、すでに XML ソースが存在します。\n置き換えますか ?
SaveXMLAction.MessageBox.Text=XML ソースの保存
SaveXMLAction.Text.Description=XML ソースを指定したディレクトリに保存します。
SaveXMLAction.Text.ToolTip=XML ソースの保存
ColoredByValueSeries=凡例項目の型を系列として設定します。
CurveFittingArea=面積系列の曲線のあてはめ属性を有効にします。
CurveFittingBar=棒系列の曲線のあてはめ属性を作成します。
CurveFittingLine=面積系列の曲線のあてはめ属性を有効にします。
CurveFittingStock=株価系列の曲線のあてはめ属性を作成します。
LegendFormat=凡例はグラフの上下左右、または内側に配置できます。凡例項目を含むクライアント領域は、凡例の内側に配置されます。
MarkerLine=マーカの線は、X と Y の両方の軸で作成できます (ChartWithAxes 型グラフの場合のみ)。
MarkerRange=マーカの範囲は、X と Y の両方の軸で作成できます (ChartWithAxes 型グラフの場合のみ)。
MultiplePie=1 つのパイグラフが複数の系列に分類されて表示されます。同類の系列をまとめて出力して、値を比較できます。
MDialSRegionMeter=1 つのダイヤルボードに複数の針で値を出力します。
SDialSRegionMeter=1 つのダイヤルボードに 1 つの針で値を出力します。
PercentageValueFormat=各系列の値を比率 (%) で出力します。
PlotFormat=プロットとクライアント面積の外観を指定します。
SeriesValueFormat=値データ (\\###,###) とカテゴリデータ (yyyy/mm/dd) の形式を指定します。

###########################################################
#Scripted Charts

AxisLabelScript=beforeDrawAxisLabel() で軸とラベル (Axis.getLabel()) の属性を操作できます。
AxisTitleScript=beforeDrawAxisTitle() で軸とラベル (Axis.getTitle()) の属性を操作できます。
BlockScript=beforeDrawBlock() でブロック (プロット、タイトル、凡例など) の属性を操作できます。
CurveFittingScript=beforeDrawFittingCurve() で曲線のあてはめの属性を操作できます。
DataPointLabelScript=beforeDrawDataPointLabel() でデータポイントのヒントとラベルの属性を操作できます。
LegendScript=beforeDrawLegendEntry() で各凡例のラベル属性を操作できます。
MarkerLineScript=beforeDrawMarkerLine() で軸とマーカの線の属性を操作できます。
MarkerRangeScript=beforeDrawMarkerLine() で軸とマーカの範囲の属性を操作できます。
SeriesScript=beforeDrawSeries() で系列と ISeriesRenderer (BaseRenderer または AxesRenderer) の属性を操作できます。
SeriesTitleScript=beforeDrawSeriesTitle() で系列とラベル (Series.getTitle()) の属性を操作できます。この関数はパイグラフにのみ適用されます。

###########################################################
#Data Operations

MinSlice=スライスの最小値を絶対値か比率 (%) で指定できます。パイグラフのスライスを切り出すには、切り出し属性に数式を設定します。
MultipleYAxes=1 つのグラフに複数の Y 軸を使用できます。
MultipleYSeries=1 つのグラフに複数の Y (値) 系列を使用できます。 
WorkingWithBIRTDataEngine=ODA/DtE (BIRT Data Engine) を使ったグラフでは、データをグループ化・集計する際にグラフ自身を除外します。

###########################################################
#Default
DefaultDescription=カテゴリからサンプルを 1 つ選択してください。

###########################################################
#Example View labels
ChartExamples.Description=説明
ChartExamples.Preview=プレビュー
ChartExamples.SelectExamples=サンプルの選択

############################################################
#Category Names
SampleChartCategory.PrimitiveCharts=Primitive Charts
SampleChartCategory.3DCharts=3D Charts
SampleChartCategory.CombinationCharts=Combination Charts
SampleChartCategory.FormattedCharts=Formatted Charts
SampleChartCategory.ScriptedCharts=Scripted Charts
SampleChartCategory.DataOperations=Data Operations
#Sample Chart Names
#Primitive Charts
SampleChartName.SideBySideBar=Side By Side Bar
SampleChartName.StackedBar=Stacked Bar
SampleChartName.PercentStackedBar=Percent Stacked Bar
SampleChartName.OverlayLine=Overlay Line
SampleChartName.StackedLine=Stacked Line
SampleChartName.PercentStackedLine=Percent Stacked Line
SampleChartName.OverlayArea=Overlay Area
SampleChartName.StackedArea=Stacked Area
SampleChartName.PercentStackedArea=PercentStacked Area
SampleChartName.Pie=Pie
SampleChartName.StandardMeter=Standard Meter
SampleChartName.SuperImposedMeter=Super Imposed Meter
SampleChartName.Scatter=Scatter
SampleChartName.BarStickStock=BarStick Stock
SampleChartName.CandleStickStock=Candle Stick Stock
SampleChartName.Bubble=Bubble
SampleChartName.Difference=Difference
SampleChartName.Gantt=Gantt
SampleChartName.Tube=Tube
SampleChartName.Cone=Cone
SampleChartName.Pyramid=Pyramid
#3D Charts
SampleChartName.Bar=Bar
SampleChartName.Line=Line
SampleChartName.Area=Area
#Combination Charts
SampleChartName.BarAndLine=Bar And Line
#Formatted Charts
SampleChartName.AxisLabelFormat=Axis Label Format
SampleChartName.AxisTitleFormat=Axis Title Format
SampleChartName.ColoredByCategorySeries=Colored By Category Series
SampleChartName.ColoredByValueSeries=Colored By Value Series
SampleChartName.CurveFittingArea=Curve Fitting Area
SampleChartName.CurveFittingBar=Curve Fitting Bar
SampleChartName.CurveFittingLine=Curve Fitting Line
SampleChartName.CurveFittingStock=Curve Fitting Stock
SampleChartName.LegendFormat=Legend Format
SampleChartName.MarkerLine=Marker Line
SampleChartName.MarkerRange=Marker Range
SampleChartName.MultiplePie=Multiple Pie
SampleChartName.MDialSRegionMeter=M Dial S Region Meter
SampleChartName.SDialSRegionMeter=S Dial S Region Meter
SampleChartName.PercentageValueFormat=Percentage Value Format
SampleChartName.PlotFormat=Plot Format
SampleChartName.SeriesValueFormat=Series Value Format
#Scripted Charts
SampleChartName.AxisLabelScript=Axis Label Script
SampleChartName.AxisTitleScript=Axis Title Script
SampleChartName.BlockScript=Block Script
SampleChartName.CurveFittingScript=Curve Fitting Script
SampleChartName.DataPointLabelScript=Data Point Label Script
SampleChartName.LegendScript=Legend Script
SampleChartName.MarkerLineScript=Marker Line Script
SampleChartName.MarkerRangeScript=Marker Range Script
SampleChartName.SeriesScript=Series Script
SampleChartName.SeriesTitleScript=Series Title Script
#Data Operations
SampleChartName.MinSlice=Min Slice
SampleChartName.MultipleYAxes=Multiple Y Axes
SampleChartName.MultipleYSeries=Multiple Y Series
SampleChartName.WorkingWithBIRTDataEngine=Working With BIRT Data Engine


