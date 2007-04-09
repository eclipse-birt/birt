###########################################################
# Crosstab name conventions
Element.Crosstab=Tables croisées
Element.CrosstabCell=Cellule du tableau croisé
Element.CrosstabAggregationCell=Cellule d'agrégation du tableau croisé
Element.AggregationOnRow=Agrégation à la ligne
Element.AggregationOnColumn=Agrégation sur la colonne
Element.AggregationHeaderLocation=Aggregation Header Location
Element.MeasureView=Affichage de mesure
Element.PageBreakBefore=Page Break Before
Element.PageBreakAfter=Page Break After
Element.SortDirection=Sort Direction
Element.SortType=Sort Type
Element.LevelView=Affichage de niveau
Element.DimensionView=Affichage de dimension
Element.CrosstabView=Affichage du tableau croisé
Element.Cube=Cube
Element.Dimension=Dimension
Element.Level=Level
Element.Measure=Measure
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

CrosstabQueryHelper.error.invalid.measure=The associated measure for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.dimension=The associated dimension for crosstab is inavlid.
CrosstabQueryHelper.error.invalid.level=The associated level for crosstab is inavlid.

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
CrosstabReportItemExecutor.error.generate.columns=Error happend when generate columns for crosstab.
CrosstabReportItemExecutor.error.prepare.children=Error happend when prepare child executor for crosstab.
CrosstabReportItemExecutor.error.invalid.cube.result=The cube result for crosstab is invalid(=null).

CrosstabRowExecutor.error.check.force.empty=Une erreur s'est produite lors de la vérification de l'obligation d'état vide de la cellule en cours.
CrosstabRowExecutor.error.check.edge.start=Une erreur s'est produite lors de la vérification de l'état de début du bord de la cellule en cours.
CrosstabRowExecutor.error.retrieve.child.executor=Une erreur s'est produite lors de la récupération du module d'exécution enfant.

CrosstabSubTotalRowExecutor.error.check.edge.start=Une erreur s'est produite lors de la vérification de l'état de début du bord de la cellule en cours.
CrosstabSubTotalRowExecutor.error.retrieve.child.executor=Une erreur s'est produite lors de la récupération du module d'exécution enfant.

TableColumnGenerator.error.process.visibility=Une erreur s'est produite lors du traitement de la visibilité des colonnes

CrosstabReportItemHandle.error.locate.column.cell=Error locating relevant crosstab cell on columns
CrosstabReportItemHandle.error.locate.row.cell=Error locating relevant crosstab cell on rows
CrosstabReportItemHandle.error.set.column.width=Error setting crosstab column width
CrosstabReportItemHandle.error.set.row.height=Error setting crosstab row height
