###########################################################
# Crosstab name conventions
Element.Crosstab=Tables croisées
Element.CrosstabCell=Cellule du tableau croisé
Element.CrosstabAggregationCell=Cellule d'agrégation du tableau croisé
Element.AggregationOnRow=Agrégation à la ligne
Element.AggregationOnColumn=Agrégation sur la colonne
Element.AggregationHeaderLocation=Emplacement de l'en-tête d'agrégation
Element.MeasureView=Affichage de mesure
Element.PageBreakBefore=Saut de page avant
Element.PageBreakAfter=Saut de page après
Element.SortDirection=Direction du tri
Element.SortType=Type de tri
Element.LevelView=Affichage de niveau
Element.DimensionView=Affichage de dimension
Element.CrosstabView=Affichage du tableau croisé
Element.Cube=Cube
Element.Dimension=Dimension
Element.Level=Niveau
Element.Measure=Mesure
Element.Caption=Légende
Element.CaptionID=Clé de légende
Element.MeasureDirection=Direction de la mesure
Element.PageLayout=Mise en page
Element.EmptyCellValue=Valeur de cellule vide

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=L''élément de dimension du cube {0} présente des doublons appliqués pour l''affichage de dimension dans le tableau croisé {1}.
Error.CrosstabException.DUPLICATE_MEASURE=L''élément de mesure du cube {0} présente des doublons appliqués pour l''affichage de mesure dans le tableau croisé {1}.
Error.CrosstabException.DUPLICATE_LEVEL=

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=Echec du chargement de l'élément de tableau croisé à partir de l'élément étendu.
CrosstabReportItemQuery.error.query.building=Modèle de tableau croisé non valide pour la génération de requête.
CrosstabReportItemQuery.error.register.empty.cell.value=Error on registering empty cell value.

CrosstabQueryHelper.error.invalid.measure=La mesure associée pour le tableau croisé est incorrecte.
CrosstabQueryHelper.error.invalid.dimension=La dimension associée pour le tableau croisé est incorrecte.
CrosstabQueryHelper.error.invalid.level=Le niveau associé pour le tableau croisé est incorrect.

BaseCrosstabExecutor.error.process.visibility=Une erreur s'est produite lors du traitement de la visibilité.
BaseCrosstabExecutor.error.process.bookmark=Une erreur s'est produite lors du traitement du signet.

CrosstabCellExecutor.error.restor.data.position=Une erreur s'est produite lors de la restauration de la position des données.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=Une erreur s'est produite lors de la génération du module d'exécution enfant.
CrosstabGrandTotalRowExecutor.error.check.child.executor=Une erreur s'est produite lors de la vérification de l'état du module d'exécution enfant.

CrosstabGroupExecutor.error.close.executor=Une erreur s'est produite lors de la fermeture du module d'exécution de groupe.
CrosstabGroupExecutor.error.prepare.group=Une erreur s'est produite lors de la préparation des groupes enfants.
CrosstabGroupExecutor.error.check.child.executor=Une erreur s'est produite lors de la vérification de l'état du module d'exécution enfant.

CrosstabHeaderRowExecutor.error.check.force.empty=Une erreur s'est produite lors de la vérification de l'obligation d'état vide de la cellule en cours.
CrosstabHeaderRowExecutor.error.check.edge.end=Une erreur s'est produite lors de la vérification de l'état de fin du bord de la cellule en cours.
CrosstabHeaderRowExecutor.error.retrieve.child.executor=Une erreur s'est produite lors de la récupération du module d'exécution enfant.

CrosstabMeasureHeaderRowExecutor.error.generate.child.executor=Une erreur s'est produite lors de la génération du module d'exécution enfant.
CrosstabMeasureHeaderRowExecutor.error.check.child.executor=Une erreur s'est produite lors de la vérification de l'état du module d'exécution enfant.

CrosstabMeasureRowExecutor.error.generate.child.executor=Une erreur s'est produite lors de la génération du module d'exécution enfant.
CrosstabMeasureRowExecutor.error.check.child.executor=Une erreur s'est produite lors de la vérification de l'état du module d'exécution enfant.

CrosstabReportItemExecutor.error.crosstab.loading=Echec du chargement de l'élément de tableau croisé à partir de l'élément étendu.
CrosstabReportItemExecutor.error.generate.columns=Une erreur s'est produite lors de la génération des colonnes pour le tableau croisé.
CrosstabReportItemExecutor.error.prepare.children=Une erreur s'est produite lors de la préparation du module d'exécution enfant pour le tableau croisé.
CrosstabReportItemExecutor.error.invalid.cube.result=Le cube obtenu pour le tableau croisé est incorrect (=nul).

CrosstabRowExecutor.error.check.force.empty=Une erreur s'est produite lors de la vérification de l'obligation d'état vide de la cellule en cours.
CrosstabRowExecutor.error.check.edge.start=Une erreur s'est produite lors de la vérification de l'état de début du bord de la cellule en cours.
CrosstabRowExecutor.error.retrieve.child.executor=Une erreur s'est produite lors de la récupération du module d'exécution enfant.

CrosstabSubTotalRowExecutor.error.check.edge.start=Une erreur s'est produite lors de la vérification de l'état de début du bord de la cellule en cours.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Une erreur s'est produite lors de la récupération du module d'exécution enfant.

TableColumnGenerator.error.process.visibility=Une erreur s'est produite lors du traitement de la visibilité des colonnes

CrosstabReportItemHandle.error.locate.column.cell=Erreur lors de la localisation de la cellule de tableau croisé appropriée sur les colonnes
CrosstabReportItemHandle.error.locate.row.cell=Erreur lors de la localisation de la cellule de tableau croisé appropriée sur les lignes
CrosstabReportItemHandle.error.set.column.width=Erreur lors de la définition de la largeur des colonnes du tableau croisé
CrosstabReportItemHandle.error.set.row.height=Erreur lors de la définition de la hauteur des lignes du tableau croisé
CrosstabReportItemHandle.msg.change.measure.direction=Change Measure Direction
CrosstabReportItemHandle.msg.insert.measure=Insert Measure

MeasureViewHandle.info.aggregation.already.exist=The requested aggregation is already existed.
MeasureViewHandle.msg.add.aggregation=Add Aggregation

LevelViewTask.msg.remove.subtotal=Remove Subtotal
LevelViewTask.msg.add.subtotal=Add Subtotal

DimensionViewTask.msg.remove.level=Remove Level

CrosstabViewTask.msg.remove.dimension=Remove Dimension
CrosstabViewTask.msg.remove.grandtotal=Remove Grand Total
CrosstabViewTask.msg.add.grandtotal=Add Grand Total

CrosstabReportItemTask.msg.add.grandtotal=Add Grand Total
CrosstabReportItemTask.msg.set.aggregate.function=Set Aggregate Function
CrosstabReportItemTask.msg.pivot.dimension=Pivot Dimension
CrosstabReportItemTask.msg.insert.dimension=Insert Dimension
