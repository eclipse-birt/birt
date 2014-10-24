###########################################################
# Crosstab name conventions
Element.Crosstab=Tableau croisé

Element.CrosstabCell=Cellule du tableau croisé

Element.CrosstabAggregationCell=Cellule d'agrégation du tableau croisé

Element.AggregationOnRow=Agrégation à la ligne

Element.AggregationOnColumn=Agrégation sur la colonne

Element.AggregationHeaderLocation=Emplacement de l'en-tête d'agrégation

Element.GrandTotalLocation=Emplacement du total général

Element.MeasureView=Affichage de mesure

Element.ComputedMeasureView=Affichage mesure calculée

Element.PageBreakBefore=Saut de page avant

Element.PageBreakAfter=Saut de page après

Element.PageBreakInside=Saut de page à l'intérieur

Element.PageBreakInterval=Intervalle de saut de page

Element.SortDirection=Direction du tri

Element.SortType=Type de tri

Element.LevelView=Affichage de niveau

Element.DisplayField=Afficher le champ

Element.DimensionView=Affichage de dimension

Element.CrosstabView=Affichage du tableau croisé

Element.Cube=Cube
Element.Dimension=Dimension

Element.Level=Niveau

Element.Measure=Mesure

Element.Caption=Légende

Element.CaptionID=Clé de légende

Element.Summary=Résumé

Element.MeasureDirection=Direction de la mesure

Element.PageLayout=Mise en page

Element.EmptyCellValue=Valeur de cellule vide

Element.RepeatRowHeader=Répéter l'en-tête de ligne

Element.RepeatColumnHeader=Répéter l'en-tête de colonne

Element.Mirrored=En miroir

Element.MirrorStartingLevel=Niveau de départ de la mise en miroir

Element.HideMeasureHeader=Masquer l'en-tête de Mesure

Element.SpanOverOnColumn=Couvrir une colonne

Element.SpanOverOnRow=Couvrir une ligne

Element.Direction.Horizontal=Horizontale

Element.Direction.Vertical=Verticale

Element.choice.before=Avant

Element.choice.after=Après

Element.RowPageBreakInterval=Ligne intervalle de saut de page

Element.ColumnPageBreakInterval=Colonne intervalle de saut de page

Element.TagType=Type de balise

Method.onPrepare=A la préparation

Method.onCreate=A la création

Method.onRender=Au rendu

Method.onPageBreak=Au saut de page

###########################################################
# Crosstab exception
Error.CrosstabException.DUPLICATE_DIMENSION=L'élément de dimension du cube ''{0}'' ne peut pas être appliqué à un tableau croisé qui possède un doublon.

Error.CrosstabException.DUPLICATE_MEASURE=L'élément de mesure du cube ''{0}'' ne peut pas être appliqué à un tableau croisé qui possède un doublon.

Error.CrosstabException.DUPLICATE_LEVEL=L'élément de niveau du cube ''{0}'' ne peut pas être appliqué à un tableau croisé qui possède un doublon.

Error.CrosstabException.DIMENSION_NOT_FOUND=Impossible de trouver un affichage de dimension ''{0}'' dans le tableau croisé.

Error.CrosstabException.MEASURE_NOT_FOUND=Impossible de trouver un affichage de mesure ''{0}'' dans le tableau croisé.

Error.CrosstabException.LEVEL_NOT_FOUND=Impossible de trouver un affichage de niveau ''{0}'' dans le tableau croisé.

###########################################################
# Crosstab executor runtime error message
CrosstabReportItemQuery.error.crosstab.loading=Echec du chargement de l'élément de tableau croisé à partir de l'élément étendu.

CrosstabReportItemQuery.error.query.building=Modèle de tableau croisé non valide pour la génération de requête.

CrosstabReportItemQuery.error.register.empty.cell.value=Erreur lors de l'enregistrement de la valeur de cellule vide.

CrosstabQueryHelper.error.invalid.measure=Le tableau croisé contient une définition de mesure nulle ; ou alors la mesure spécifique "{0}" n''existe pas dans le cube qui lui est associé.

CrosstabQueryHelper.error.invalid.dimension.row=La zone de ligne du tableau croisé contient une définition de dimension nulle ; ou alors la dimension spécifique "{0}" n''existe pas dans le cube qui lui est associé.

CrosstabQueryHelper.error.invalid.level.row=La zone de ligne du tableau croisé contient une définition de niveau nulle ; ou alors le niveau spécifique "{0}" n''existe pas dans le cube qui lui est associé.

CrosstabQueryHelper.error.invalid.dimension.column=La zone de colonne du tableau croisé contient une définition de dimension nulle ; ou alors la dimension spécifique "{0}" n''existe pas dans le cube qui lui est associé.

CrosstabQueryHelper.error.invalid.level.column=La zone de colonne du tableau croisé contient une définition de niveau nulle ; ou alors le niveau spécifique "{0}" n''existe pas dans le cube qui lui est associé.

BaseCrosstabExecutor.error.process.visibility=Une erreur s'est produite lors du traitement de la visibilité.

BaseCrosstabExecutor.error.process.bookmark=Une erreur s'est produite lors du traitement des en-têtes.

BaseCrosstabExecutor.error.process.headers=Une erreur s'est produite lors du traitement des en-têtes.

BaseCrosstabExecutor.error.process.row.height=Une erreur s'est produite lors du traitement de la hauteur des lignes.

CrosstabCellExecutor.error.restor.data.position=Une erreur s'est produite lors de la restauration de la position des données.

CrosstabGrandTotalRowExecutor.error.generate.child.executor=Une erreur s'est produite lors de la génération du module d'exécution enfant.

CrosstabGrandTotalRowExecutor.error.check.child.executor=Une erreur s'est produite lors de la vérification de l'état du module d'exécution enfant.

CrosstabHeaderExecutor.error.reset.row.position=Une erreur s'est produite lors de la réinitialisation de la position du curseur de bord de ligne.

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

TableColumnGenerator.error.process.column.width=Une erreur s'est produite lors du traitement de la largeur des colonnes.

CrosstabReportItemHandle.error.locate.column.cell=Erreur lors de la localisation de la cellule de tableau croisé appropriée sur les colonnes

CrosstabReportItemHandle.error.locate.row.cell=Erreur lors de la localisation de la cellule de tableau croisé appropriée sur les lignes

CrosstabReportItemHandle.error.set.column.width=Erreur lors de la définition de la largeur des colonnes du tableau croisé

CrosstabReportItemHandle.error.set.row.height=Erreur lors de la définition de la hauteur des lignes du tableau croisé

CrosstabReportItemHandle.msg.change.measure.direction=Modifier la direction de la mesure

CrosstabReportItemHandle.msg.insert.measure=Insérer une mesure

CrosstabReportItemHandle.exception.name.blank=Le nom de la mesure calculée est requis.

CrosstabReportItemHandle.exception.name.duplicate=Le nom de mesure indiqué est déjà utilisé. Deux noms ne peuvent pas être identiques.

MeasureViewHandle.info.aggregation.already.exist=L'agrégation requise existe déjà.

MeasureViewHandle.msg.add.aggregation=Ajouter l'agrégation

LevelViewTask.msg.remove.subtotal=Supprimer le sous-total

LevelViewTask.msg.add.subtotal=Ajouter le sous-total

DimensionViewTask.msg.remove.level=Supprimer le niveau

DimensionViewTask.msg.insert.level=Insérer un niveau

MeasureViewTask.msg.remove.header=Supprimer l'en-tête de mesure

MeasureViewTask.msg.add.header=Ajouter l'en-tête de mesure

CrosstabViewTask.msg.add.dimension=Ajouter une dimension

CrosstabViewTask.msg.remove.dimension=Supprimer la dimension

CrosstabViewTask.msg.remove.grandtotal=Supprimer le total général

CrosstabViewTask.msg.add.grandtotal=Ajouter le total général

CrosstabReportItemTask.msg.add.grandtotal=Ajouter le total général

CrosstabReportItemTask.msg.set.aggregate.function=Définir la fonction d'agrégation

CrosstabReportItemTask.msg.pivot.dimension=Faire pivoter la dimension

CrosstabReportItemTask.msg.insert.dimension=Insérer une dimension

CrosstabReportItemTask.msg.set.mirroredStartingLevel=Définir le niveau de départ de la mise en miroir

CrosstabReportItemTask.msg.pivot.crosstab=Faire pivoter le tableau croisé

CrosstabReportItemHandle.Error.HasNoCube=Le tableau croisé doit pouvoir accéder à un cube.

CrosstabReportItemHandle.log.validate=validation(...)

CrosstabScriptHandler.error.javascript={0} à la ligne {1} du JavaScript :\r\n{2}\r\n

CrosstabScriptHandler.info.try.register.crosstab.javascript.content=Tentative d'enregistrement du contenu JavaScript du tableau croisé...

CrosstabScriptHandler.info.crosstab.javascript.content.registered=Le contenu JavaScript du tableau croisé a été enregistré avec succès.

CrosstabScriptHandler.info.try.load.crosstab.java.handler=Tentative de chargement du gestionnaire d'événements Java du tableau croisé...

CrosstabScriptHandler.info.crosstab.java.handler.loaded=Le gestionnaire d'événements Java du tableau croisé [{0}] a été chargé avec succès.

CrosstabScriptHandler.info.invalid.crosstab.java.handler=Le gestionnaire d'événements Java du tableau croisé [{0}] n'est pas une implémentation compatible.
