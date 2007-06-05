###########################################################
#Primitive Charts

SideBySideBar=Los diagramas de barras laterales muestran barras de cada serie una junto a la otra. Estas barras se organizan para que tengan el mismo ancho, según el número de series que se vaya a procesar.
StackedBar=Los diagramas de barras apiladas muestran las barras apiladas una sobre la otra. Los valores positivos y negativos se apilan por separado, por encima y por debajo del origen.
PercentStackedBar=Los diagramas de barras apiladas de porcentaje muestran barras apiladas una sobre la otra de forma que la altura total sea 100, midiendo desde el punto más bajo hasta el más alto de cada unidad.
OverlayLine=Los diagramas de líneas superpuestas muestran las líneas de cada serie por separado. Las líneas se muestran vinculando los valores de la serie.
StackedLine=Los diagramas de líneas apiladas muestran las líneas apiladas una sobre la otra. Los valores positivos y negativos se apilan por separado, por encima y por debajo del origen.
PercentStackedLine=Los diagramas de líneas apiladas de porcentaje muestran las líneas apiladas una sobre la otra de forma que la altura total sea 100, midiendo desde el punto más bajo hasta el más alto de cada unidad.
OverlayArea=Los diagramas de áreas superpuestas muestran las áreas de cada serie por separado. Las áreas se muestran vinculando los valores de la serie.
StackedArea=Los diagramas de áreas apiladas muestran las áreas apiladas una sobre la otra. Los valores positivos y negativos se apilan por separado, por encima y por debajo del origen.
PercentStackedArea=Los diagramas de áreas apiladas de porcentaje muestran las áreas apiladas una sobre la otra de forma que la altura total sea 100, midiendo desde el punto más bajo hasta el más alto de cada unidad.
Pie=Los diagramas circulares muestran los valores como cortes de un círculo. El tamaño de cada corte es proporcional al valor que representa. Los diagramas circulares de varias series se dibujan como múltiples círculos, uno para cada serie.
StandardMeter=Los diagramas métricos estándar presentan un solo cuadrante con una o varias regiones. El fondo del cuadrante puede dividirse en varias regiones con diferentes colores.
SuperImposedMeter=Los diagramas métricos superpuestos contienen varios cuadrantes con idénticas regiones. Los cuadrantes se superponen, por lo que pueden presentar múltiples indicadores dentro de una misma región.
Scatter=Los diagramas de dispersión organizan los valores en el dibujo usando los datos de categoría y de valor como coordenadas. Cada valor de datos se indica con un marcador.
BarStickStock=Un diagrama de cotización de barras contiene una línea vertical con dos líneas horizontales pegadas. Los puntos superior e inferior de la línea vertical son los valores de cotización de apertura y cierre. Las dos líneas horizontales son los valores máximo y mínimo.
CandleStickStock=Un diagrama de cotización de velas contiene un cuadro con líneas que se extienden hacia arriba y hacia abajo de los extremos. Los bordes superior e inferior del cuadro son los valores de cotización de apertura y cierre. Los puntos superior e inferior de la línea son los valores máximo y mínimo. 

###########################################################
#3D Charts

Bar=Diagramas de barras laterales en 3 dimensiones.
Line=Diagramas de líneas superpuestas en 3 dimensiones.
Area=Diagramas de áreas superpuestas en 3 dimensiones.

###########################################################
#Combination Charts

BarAndLine=Los diagramas de combinación de barras y líneas contienen 2 series. Comparten el mismo valor de categoría.


###########################################################
#Formatted Charts

AxisLabelFormat=Especifique el formato para etiquetas de eje: eje Y ($###,###) y eje X (MM/dd/yyyy). Los usuarios también han podido establecer el color, fuente, contorno y otros atributos de las etiquetas.
AxisTitleFormat=Todos los atributos de etiqueta se pueden aplicar al título del eje.
ColoredByCategorySeries=Establezca Tipo de elemento de leyenda como categoría.
ColoredByValueSeries=Establezca Tipo de elemento de leyenda como serie.
CurveFittingArea=Active atributo de la curva ajustada de la serie de áreas.
CurveFittingBar=Cree una curva ajustada a línea para la serie de barras.
CurveFittingLine=Active atributo de la curva ajustada de la serie de áreas.
CurveFittingStock=Cree una curva ajustada a línea para la serie de cotización de acciones.
LegendFormat=La leyenda se puede situar a la izquierda, derecha, arriba, abajo y en el interior. El área del cliente se encuentra dentro de la leyenda. Contiene los elementos de leyenda.
MarkerLine=La línea de marcador se puede crear para los ejes X e Y (sólo para el tipo ChartWithAxes).
MarkerRange=El intervalo de marcador se puede crear para los ejes X e Y (sólo para el tipo ChartWithAxes).
MultiplePie=Un diagrama circular individual contiene varias series circulares que pueden formar un grupo circular para presentar la comparación de valores.
MDialSRegionMeter=Múltiples indicadores en un tablón de cuadrante individual.
SDialSRegionMeter=Un indicador en un tablón de cuadrante individual.
PercentageValueFormat=Presente el valor de serie como una figura de porcentaje.
PlotFormat=Especifique la apariencia de un área de dibujo y del cliente.
SeriesValueFormat=Especifique el formato de los datos de valor ($###,###.00) y datos de categoría (MM/dd/yyyy).

###########################################################
#Scripted Charts

AxisLabelScript=beforeDrawAxisLabel() permite a los usuarios manipular atributos de eje y etiqueta (Axis.getLabel()).
AxisTitleScript=beforeDrawAxisTitle() permite a los usuarios manipular atributos de eje y etiqueta (Axis.getTitle()).
BlockScript=beforeDrawBlock() permite a los usuarios manipular atributos de bloque (p. ej., dibujo, título, leyenda).
CurveFittingScript=beforeDrawFittingCurve() permite a los usuarios manipular atributos de curva ajustada.
DataPointLabelScript=beforeDrawDataPointLabel() permite a los usuarios manipular atributos de etiqueta y DataPointHints.
LegendScript=beforeDrawLegendEntry() permite a los usuarios manipular atributos de etiqueta (para cada leyenda).
MarkerLineScript=beforeDrawMarkerLine() permite a los usuarios manipular atributos de línea de marcador.
MarkerRangeScript=beforeDrawMarkerRange() permite a los usuarios manipular atributos de intervalo de marcador.
SeriesScript=beforeDrawSeries() permite a los usuarios manipular atributos de serie y ISeriesRenderer (BaseRenderer o AxesRenderer).
SeriesTitleScript=beforeDrawSeriesTitle() permite a los usuarios manipular atributos de serie y etiqueta (Series.getTitle()). Sólo se aplica al diagrama circular.

###########################################################
#Data Operations

MinSlice=El usuario puede especificar el valor mínimo (absoluto o porcentaje) de un corte. El diagrama circular se puede separar estableciendo la expresión en atributo de separación.
MultipleYAxes=Un diagrama puede contener más de un eje Y.
MultipleYSeries=Un diagrama puede contener más de una serie Y (valor). 

###########################################################
#Default
DefaultDescription=Seleccione un ejemplo entre las categorías.
