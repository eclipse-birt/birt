###########################################################
#Primitive Charts

SideBySideBar=Les graphiques à barres côte à côte affichent les barres de chaque série l'une à côté de l'autre. Ces barres sont arrangées de sorte qu'elles aient la même largeur. La largeur des barres dépend du nombre de séries tracées.
StackedBar=Les graphiques à barres empilées affichent des barres les unes sur les autres. Les valeurs positives et négatives sont empilées séparément au-dessus et au-dessous de l'origine.
PercentStackedBar=Les graphiques à barres empilées à pourcentage affichent les barres empilées les unes sur les autres, de sorte que la hauteur totale des barres empilées (du point le plus bas au point le plus haut) atteigne 100.
OverlayLine=Les graphiques à lignes superposées affichent les lignes de chaque série indépendamment des autres. Les lignes regroupent les valeurs pour la série.
StackedLine=Les graphiques de lignes empilées affichent des lignes les unes sur les autres. Les valeurs positives et négatives sont empilées séparément au-dessus et au-dessous de l'origine.
PercentStackedLine=Les graphiques de lignes empilées à pourcentage affichent des lignes empilées les unes sur les autres de telle manière que la hauteur totale des lignes (du point le plus bas au point le plus haut dans chaque unité) représente 100.
OverlayArea=Les graphiques en aires superposées affichent les aires de chaque série indépendamment des autres. Les aires regroupent les valeurs pour la série.
StackedArea=Les graphiques en aires empilées affichent des aires les unes sur les autres. Les valeurs positives et négatives sont empilées séparément au-dessus et au-dessous de l'origine.
PercentStackedArea=Les graphiques en aires empilées à pourcentage affichent des aires empilées les unes sur les autres de telle manière que la hauteur totale des aires (du point le plus bas au point le plus haut dans chaque unité) représente 100.
Pie=Les graphiques à secteurs affichent les valeurs sous la forme de tranches de secteur. La taille de chaque tranche est proportionnelle à la valeur qu'elle représente. Les graphiques à secteurs pour séries multiples sont tracés sous la forme de secteurs multiples, un secteur par série.
StandardMeter=Les graphiques à cadrans standard contiennent un seul cadran par zone(s). L'arrière-plan du cadran peut être divisé en zones de différentes couleurs.
SuperImposedMeter=Les graphiques à cadrans superposés contiennent plusieurs cadrans avec des zones identiques. Les cadrans se chevauchent afin que plusieurs aiguilles apparaissent dans une même zone.
Scatter=Les graphiques en nuage de points affichent les valeurs disposées sur le tracé en utilisant les données de catégorie et de valeur comme coordonnées. Chaque valeur de données est indiquée par un repère.
BarStickStock=Un graphique boursier à barres se présente sous la forme d'une ligne verticale et de deux lignes horizontales associées. Les points supérieurs et inférieurs de la ligne verticale correspondent aux valeurs d'ouverture et de fermeture. Les deux lignes horizontales correspondent à la valeur la plus élevée et la plus basse.
CandleStickStock=Un graphique boursier en chandelier se présente sous la forme d'un cadre dont les côtés sont joints par des courbes. Les coins supérieurs et inférieurs du cadre correspondent aux valeurs d'ouverture et de fermeture. Les points supérieur et inférieur de la ligne correspondent à la valeur la plus élevée et la plus basse. 

###########################################################
#3D Charts

Bar=Graphique à barres côte à côte en 3 dimensions.
Line=Graphique à lignes superposées en 3 dimensions.
Area=Graphique en aires superposées en 3 dimensions.

###########################################################
#Combination Charts

BarAndLine=Les graphiques à barres et à lignes contiennent 2 séries. Elles partagent la même valeur de catégorie.


###########################################################
#Formatted Charts

AxisLabelFormat=Spécifiez le format des étiquettes d'axe : axe des Y($###,###) et axe des X(MM/dd/yyyy). Les utilisateurs ont également la possibilité de définir la couleur, la police, le contour ainsi que d'autres attributs d'étiquette.
AxisTitleFormat=Tous les attributs d'étiquette peuvent être appliqués au titre des axes.
ColoredByCategorySeries=Définissez le type d'élément de légende en tant que catégorie.
ColoredByValueSeries=Définissez le type d'élément de légende en tant que série.
CurveFittingArea=Activez l'attribut d'ajustement de courbe de la série d'aires.
CurveFittingBar=Créez une ligne d'ajustement de courbe pour la série de barres.
CurveFittingLine=Activez l'attribut d'ajustement de courbe de la série d'aires.
CurveFittingStock=Créez une ligne d'ajustement de courbe pour la série d'actions.
LegendFormat=La légende peut être placée sur la gauche, sur la droite, en haut, en bas ou à l'intérieur. La zone client se trouve dans la légende. Elle contient les éléments de légende.
MarkerLine=La ligne de repères peut être créée pour l'axe des X et l'axe des Y (pour le type ChartWithAxes seulement).
MarkerRange=La gamme de repères peut être créée pour l'axe des X et l'axe des Y (pour le type ChartWithAxes seulement).
MultiplePie=Un graphique à secteurs contient plusieurs séries secteur. Elles peuvent constituer un groupe de secteurs représentant une comparaison de valeurs.
MDialSRegionMeter=Plusieurs aiguilles dans un même tableau à cadran.
SDialSRegionMeter=Une aiguille dans un seul tableau à cadran.
PercentageValueFormat=Représentez la valeur de série en tant que valeur de pourcentage.
PlotFormat=Spécifiez l'apparence de la zone de tracé et de la zone client.
SeriesValueFormat=Spécifiez le format des données de valeur ($###,###.00) et des données de catégorie (MM/dd/yyyy).

###########################################################
#Scripted Charts

AxisLabelScript=beforeDrawAxisLabel() permet aux utilisateurs de manipuler les attributs Axis et Label(Axis.getLabel()).
AxisTitleScript=beforeDrawAxisTitle() permet aux utilisateurs de manipuler les attributs Axis et Label(Axis.getTitle()).
BlockScript=beforeDrawBlock() permet aux utilisateurs de manipuler les attributs Block (par exemple, Tracé, Titre, Légende).
CurveFittingScript=beforeDrawFittingCurve() permet aux utilisateurs de manipuler les attributs CurveFitting.
DataPointLabelScript=beforeDrawDataPointLabel() permet aux utilisateurs de manipuler les attributs DataPointHints et Label.
LegendScript=beforeDrawLegendEntry() permet aux utilisateurs de manipuler les attributs Label (pour chaque légende).
MarkerLineScript=beforeDrawMarkerLine() permet aux utilisateurs de manipuler les attributs Axis et MarkerLine.
MarkerRangeScript=beforeDrawMarkerRange() permet aux utilisateurs de manipuler les attributs Axis et MarkerRange.
SeriesScript=beforeDrawSeries() permet aux utilisateurs de manipuler les attributs Series et ISeriesRenderer(BaseRenderer ou AxesRenderer).
SeriesTitleScript=beforeDrawSeriesTitle() permet aux utilisateurs de manipuler les attributs Series et Label(Series.getTitle()). Il s'applique uniquement aux graphiques à secteurs.

###########################################################
#Data Operations

MinSlice=L'utilisateur peut spécifier une valeur minimale (valeur absolue ou pourcentage) pour une tranche. Les tranches de graphiques à secteurs peuvent être éclatées en définissant l'expression sur l'attribut d'éclatement.
MultipleYAxes=Un graphique peut contenir plusieurs axes des Y.
MultipleYSeries=Un graphique peut contenir plusieurs séries (de valeurs) Y. 

###########################################################
#Default
DefaultDescription=Sélectionnez un exemple dans les catégories
