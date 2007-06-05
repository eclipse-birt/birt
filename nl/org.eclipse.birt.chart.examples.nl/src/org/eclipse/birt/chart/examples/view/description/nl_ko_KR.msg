###########################################################
#Primitive Charts

SideBySideBar=연속 막대형 차트에는 각 계열의 막대가 나란히 표시됩니다. 이 막대는 동일한 너비로 정렬됩니다. 막대의 너비는 그려지는 계열 수에 따라 달라집니다.
StackedBar=누적 막대형 차트에는 서로 누적된 막대가 표시됩니다. 양수 값과 음수 값은 각각 기점 위와 아래에 따로 누적됩니다.
PercentStackedBar=백분율 누적 막대형 차트에는 누적된 막대(가장 낮은 점에서부터 가장 높은 점까지)의 총 높이가 100이 되도록 막대가 서로 누적되어 표시됩니다.
OverlayLine=이중 선형 차트에는 각 계열의 선이 서로 독립적으로 표시됩니다. 계열의 값을 결합하는 선이 표시됩니다.
StackedLine=누적 선형 차트에는 서로 누적된 선이 표시됩니다. 양수 값과 음수 값은 각각 기점 위와 아래에 따로 누적됩니다.
PercentStackedLine=백분율 누적 선형 차트에는 누적된 선(각 단위에서 가장 낮은 점에서부터 가장 높은 점까지)의 총 높이가 100이 되도록 선이 서로 누적되어 표시됩니다.
OverlayArea=이중 영역형 차트에는 각 계열의 영역이 서로 독립적으로 표시됩니다. 계열의 값을 결합하는 영역이 표시됩니다.
StackedArea=누적 영역형 차트에는 서로 누적된 영역이 표시됩니다. 양수 값과 음수 값은 각각 기점 위와 아래에 따로 누적됩니다.
PercentStackedArea=백분율 누적 영역형 차트에는 누적된 영역(각 단위에서 가장 낮은 점에서부터 가장 높은 점까지)의 총 높이가 100이 되도록 영역이 서로 누적되어 표시됩니다.
Pie=원형 차트에는 값이 원형의 조각으로 표시됩니다. 각 조각의 크기는 해당 조각이 나타내는 값에 비례합니다. 복수 계열의 원형 차트는 각 계열에 하나씩 복수의 원형으로 그려집니다.
StandardMeter=표준 측정기 차트에는 영역을 포함한 단일 다이얼이 포함됩니다. 다이얼 배경은 다른 색상을 가진 영역으로 분류할 수 있습니다.
SuperImposedMeter=중첩 측정기 차트에는 동일한 영역을 포함한 다중 다이얼이 포함됩니다. 다이얼이 동시에 겹치므로 단일 영역 내 여러 바늘을 나타낼 수 있습니다.
Scatter=분산형 차트에는 범주와 값 데이터를 좌표로 사용하여 플롯 위에 정렬된 값이 표시됩니다. 각 데이터 값은 표지로 표시됩니다.
BarStickStock=막대형 주식 차트에는 수직선 한 개가 있고 두 수평선이 수직선에 붙어 있습니다. 수직선의 위쪽 끝과 아래쪽 끝이 주식의 시가와 종가 값입니다. 두 수평선은 고가와 저가를 나타냅니다.
CandleStickStock=원통형 주식 차트에는 상자 한 개가 있고 상자의 위 아래로 선이 나와 있습니다. 상자의 위쪽 가장자리와 아래쪽 가장자리가 주식의 시가와 종가 값입니다. 위로 나온 선은 고가를, 아래로 나온 선은 저가를 나타냅니다. 

###########################################################
#3D Charts

Bar=3차원 연속 막대형 차트입니다.
Line=3차원 이중 선형 차트입니다.
Area=3차원 이중 영역형 차트입니다.

###########################################################
#Combination Charts

BarAndLine=막대 및 선 결합 차트에는 2개의 계열이 있습니다. 2개의 계열은 동일한 범주 값을 사용합니다.


###########################################################
#Formatted Charts

AxisLabelFormat=Y축($###,###) 및 X축(MM/dd/yyyy)의 축 레이블 서식을 지정합니다. 또한 사용자가 레이블 색상, 글꼴, 윤곽선 및 기타 특성도 설정할 수 있습니다.
AxisTitleFormat=모든 레이블 특성은 축 제목에 적용할 수 있습니다.
ColoredByCategorySeries=범례 항목 형식을 범주로 설정합니다.
ColoredByValueSeries=범례 항목 형식을 계열로 설정합니다.
CurveFittingArea=영역형 계열의 곡선 근사 특성을 사용합니다.
CurveFittingBar=막대형 계열에 곡선 근사 줄을 생성합니다.
CurveFittingLine=영역형 계열의 곡선 근사 특성을 사용합니다.
CurveFittingStock=주식형 계열에 곡선 근사 줄을 생성합니다.
LegendFormat=범례는 왼쪽, 오른쪽, 위쪽, 아래쪽 및 안쪽에 위치할 수 있습니다. 클라이언트 영역은 범례 내에 위치하며, 범례 항목을 포함합니다.
MarkerLine=MarkerLine은 X축 및 Y축 모두에 만들 수 있습니다(ChartWithAxes 형식에만 해당).
MarkerRange=MarkerRange는 X축 및 Y축 모두에 만들 수 있습니다(ChartWithAxes 형식에만 해당).
MultiplePie=개별 원형 차트에는 여러 조각의 계열이 포함됩니다. 계열은 조각의 그룹을 구성하여 값을 비교할 수 있습니다.
MDialSRegionMeter=개별 다이얼 보드의 복수 바늘입니다.
SDialSRegionMeter=개별 다이얼 보드의 바늘입니다.
PercentageValueFormat=계열 값을 백분율 수치로 표시합니다.
PlotFormat=플롯 및 클라이언트 영역의 모양을 지정합니다.
SeriesValueFormat=값 데이터($###,###.00) 및 범주 데이터(MM/dd/yyyy)의 서식을 지정합니다.

###########################################################
#Scripted Charts

AxisLabelScript=beforeDrawAxisLabel()을 사용해 축 및 레이블(Axis.getLabel()) 특성을 조작할 수 있습니다.
AxisTitleScript=beforeDrawAxisTitle()을 사용해 축 및 레이블(Axis.getTitle()) 특성을 조작할 수 있습니다.
BlockScript=beforeDrawBlock()을 사용해 블록(예: 플롯, 제목, 범례) 특성을 조작할 수 있습니다.
CurveFittingScript=beforeDrawFittingCurve()를 사용해 CurveFitting 특성을 조작할 수 있습니다.
DataPointLabelScript=beforeDrawDataPointLabel()을 사용해 DataPointHints 및 레이블 특성을 조작할 수 있습니다.
LegendScript=beforeDrawLegendEntry()를 사용해 각 범례에 대한 레이블 특성을 조작할 수 있습니다.
MarkerLineScript=beforeDrawMarkerLine()을 사용해 축 및 MarkerLine 특성을 조작할 수 있습니다.
MarkerRangeScript=beforeDrawMarkerRange()를 사용해 축 및 MarkerRange 특성을 조작할 수 있습니다.
SeriesScript=beforeDrawSeries()를 사용해 계열 및 ISeriesRenderer(BaseRenderer 또는 AxesRenderer) 특성을 조작할 수 있습니다.
SeriesTitleScript=beforeDrawSeriesTitle()을 사용해 계열 및 레이블(Series.getTitle()) 특성을 조작할 수 있습니다. 이는 원형 차트에만 적용됩니다.

###########################################################
#Data Operations

MinSlice=사용자가 조각의 최소값(절대값 또는 백분율)을 지정할 수 있습니다. 원형 차트 조각은 쪼개기 특성을 식에 설정하여 쪼갤 수 있습니다.
MultipleYAxes=차트는 한 개 이상의 Y축을 포함할 수 있습니다.
MultipleYSeries=차트는 한 개 이상의 Y(값) 계열을 포함할 수 있습니다. 

###########################################################
#Default
DefaultDescription=범주에서 예를 선택하십시오.
