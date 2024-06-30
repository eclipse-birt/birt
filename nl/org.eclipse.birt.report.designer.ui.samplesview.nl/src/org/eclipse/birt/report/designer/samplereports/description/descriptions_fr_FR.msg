###########################################################
#Solution reports descriptions

#Listing
ProductCatalog=Imprime le catalogue de produits Classic Models, regroupés par catégorie de produits. Indique le nom du produit, son coût et une description. Explique le regroupement sur un niveau et comment utiliser une grille dans une ligne de table pour structurer l'espacement. Présente également l'inclusion d'images et explique comment utiliser la balise <value-of> dans un élément de texte afin d'inclure le contenu d'une colonne de base de données. Enfin, des styles sont appliqués à l'état afin de simplifier sa maintenance et d'en harmoniser l'apparence. 
TopSellingProducts=Affiche un graphique à secteurs qui représente les revenus par gamme de produits. Répertorie les produits les plus vendus, triés par revenu. Explique comment utiliser un graphique et trier un jeu de résultats. Présente également l'inclusion d'images et explique comment utiliser une grille et des tables pour structurer l'état. Enfin, des styles sont appliqués à celui-ci afin de simplifier sa maintenance et d'en harmoniser l'apparence. 
TopNPercent=Grâce aux filtres n éléments du haut et pourcentage maximal de BIRT 2.0, cet état présente un graphique à barres représentant les N principaux clients par revenu, puis fournit une liste des M % des principaux clients par revenu triés par nom de client. Cela implique d'inspecter les données à plusieurs reprises, mais cette opération est effectuée de manière invisible par le moteur d'états BIRT. N et M sont des paramètres qui permettent aux utilisateurs de l'état de sélectionner un nombre et un pourcentage en cours d'exécution. 
StaticCrosstab=L'exemple ci-dessous explique comment créer un état statique croisé via la fonction Total de BIRT. Cet état présente le chiffre d'affaires annuel par trimestre pour chaque gamme de produits commercialisée par Classic Models Inc. Les totaux sont également agrégés en vue horizontale et verticale. La fonction Total de BIRT peut prendre en compte un deuxième paramètre permettant de filtrer les données agrégées. Dans cet exemple, cette fonction permet de n'afficher que les valeurs associées à une offre de produit, telle que les Trains. Le premier paramètre de la fonction Total est la valeur à agréger. Dans cet exemple, cette valeur correspond à la quantité vendue multipliée par le prix unitaire. Le second paramètre est le filtre. Si celui-ci est spécifié ainsi : dataSetRow["Produits::GAMME"]=="Motos", seule la gamme de produits Motos sera agrégée. 

#Business Forms
SalesInvoice=Imprime une facture correspondant à la commande sélectionnée. Sur cette facture figurent notamment des détails sur le client et sur la facture, ainsi que la liste des produits commandés. Explique comment utiliser un paramètre pour sélectionner la commande à facturer et comment utiliser des expressions pour plusieurs champs calculés, notamment le total des remises et des commandes. Utilise une expression pour créer la chaîne d'adresse client et illustre la suppression des valeurs nulles dans les champs de la base de données grâce au remplacement de la fonction Javascript. Présente également l'inclusion d'images et explique comment utiliser des grilles et des tables pour structurer l'état. Enfin, des styles sont appliqués à celui-ci afin de simplifier sa maintenance et d'en harmoniser l'apparence. 

###########################################################
#Reporting Feature Examples descriptions

#XML Data Source
XMLDS_After=Un état simple qui explique comment exploiter des sources de données XML. 

#Libraries
Newsfeeds=Cet exemple montre comment utiliser BIRT pour créer des bibliothèques d'états de composants réutilisables. La première étape consiste à créer une bibliothèque. URL pour le flux RSS Eclipse est utilisé comme source de données de l'état et est enregistré dans la bibliothèque. Celle-ci est ensuite publiée dans un dossier de ressources. Cet exemple illustre la création d'éléments non visuels dans une bibliothèque. Remarque importante : il est possible de créer et d'enregistrer des éléments visuels dans la bibliothèque. 
Newsfeeds_After=Cet exemple montre comment utiliser BIRT pour créer des bibliothèques d'états de composants réutilisables. La première étape consiste à créer une bibliothèque. URI pour le flux RSS Eclipse est utilisé comme source de données de l'état et est enregistré dans la bibliothèque. Celle-ci est ensuite publiée dans un dossier de ressources. Cet exemple illustre la création d'éléments non visuels dans une bibliothèque. Remarque importante : il est possible de créer et d'enregistrer des éléments visuels dans la bibliothèque.

#Combination Chart
CustomerOrdersFinal=Cet exemple explique comment créer un graphique de combinaison dans BIRT et comment explorer les détails à l'aide de signets internes et de l'Editeur d'interactivité du graphique. Le graphique de combinaison présente l'encours de la dette des 5 principaux clients sous forme de graphique à barres. Des lignes représentant le plafond de crédit de chaque client sont superposées sur le graphique. Les barres du graphique sont ensuite liées aux détails des commandes de chaque client. 

#Cascading
cascade=Cet exemple décrit comment créer un état BIRT avec un groupe de paramètres en cascade. Ce type de groupe permet de lier un groupe de paramètres de sorte que la valeur sélectionnée pour le premier paramètre affecte les choix disponibles pour les paramètres suivants. Il est possible de lier les paramètres en cascade à un ou plusieurs jeux de données. Ces derniers peuvent être utilisés à tous les niveaux du groupe. Combinée avec les paramètres des jeux de données, cette fonctionnalité est une solution particulièrement pratique pour récupérer les données renvoyées d'un état.

#Drill to details
MasterDetailOneReport=Cet exemple décrit comment créer un état réunissant des informations générales et des informations plus précises.
OrderMasterAfter=Cet exemple illustre la création d'un état principal et d'un état détaillé. L'état principal répertorie les commandes et permet à l'utilisateur de cliquer sur un numéro de commande afin d'explorer les détails de celle-ci. Cet exemple décrit comment utiliser un paramètre simple pour rassembler les résultats dans l'état détaillé et comment utiliser les propriétés des liens hypertexte dans l'état principal pour transmettre ce paramètre à l'état détaillé. Les propriétés des hyperliens de l'état principal sont générées dynamiquement en fonction des résultats renvoyés par une requête.
OrderDetailAfter=L'état détaillé accessible en cliquant sur l'état principal.

#Dynamic Report Parameter
ParameterToDataSetParameter=Cet exemple décrit comment lier un paramètre d'état à un paramètre de jeu de données.

#Expressions
Expressions=Cet exemple décrit comment créer un état BIRT utilisant des fonctions.

#Grouping
CustomerListAfter_Grouping=Cet exemple explique comment créer un état groupé dans BIRT. Dans cet exemple, l'état comporte une liste de clients et les regroupe par pays. Les principales fonctions de BIRT évoquées sont le regroupement, les sauts de page, la table des matières et l'agrégation de données par groupe ou sous forme de total. 

#Highlighting and Conditional Formatting
ProductListAfter=Cet exemple décrit comment mettre des éléments en surbrillance ou comment appliquer un formatage conditionnel dans un état BIRT. Un état de liste de produits comportant tous les produits de l'entreprise est développé. Dans l'exemple, différentes couleurs sont appliquées aux lignes et les produits en faible quantité sont mis en surbrillance. Cet exemple explique également comment ajouter un script à l'événement onCreate d'une ligne afin de mettre celle-ci en italiques pour signaler les produits ayant subi une forte majoration. 

#Mapping and Sorting 
CustomerListAfter_Mapping=Cet exemple décrit les fonctions de mise en correspondance et de tri de BIRT. La mise en correspondance permet à un élément d'état BIRT d'afficher une valeur différente en fonction d'une expression. Ainsi, si une requête renvoie un champ numérique pour le mois (1-12), vous pouvez utiliser la fonction de mise en correspondance pour afficher janvier - décembre. Dans cet exemple, la mise en correspondance permet d'afficher un rang de crédit pour les clients Classic Models. Le tri est une fonction courante et flexible de BIRT. Cet exemple décrit comment ajouter une expression de tri à un élément de table lié à un paramètre d'état. Celui-ci permet d'entrer une chaîne de recherche, qui est ensuite utilisée dans l'expression de tri afin de définir l'ordre de tri de la table. 

#Parallel Report 
EmployeeAfter=Cet exemple explique comment créer un état avec des sections parallèles. Il explique également comment créer des filtres de tableau afin de rassembler les données affichées. Dans cet état, un élément de grille est utilisé pour mettre en page deux colonnes. Dans chaque colonne, un jeu de tables imbriquées est ajouté afin de récupérer des informations sur les employés et les regrouper par position hiérarchique. Les tables sont filtrées par code de bureau. 

#Query Modification
NullParameter=Cet exemple décrit comment modifier une requête en fonction de paramètres d'état utilisant des événements de jeu de données. Par exemple, la requête est modifiée si une valeur est fournie pour le paramètre d'état. 
OrderDetailsAfter=Cet exemple décrit comment modifier une requête JDBC via une liaison de propriété ou un script. La liaison de propriété est une fonction de BIRT qui permet d'effectuer des modifications en cours d'exécution des propriétés publiques des sources et jeux de données. Elle est particulièrement utile pour ajouter des informations contextuelles à un élément. Par exemple, il est possible de modifier la clause Where pour récupérer le nom d'utilisateur d'une session ou pour faire passer l'URL d'une base de données du développement en production en fonction d'un paramètre d'état. 

#Report Elements
ProductLinesAfter=Cet exemple décrit comment utiliser les éléments Etiquette, Texte, Texte dynamique, Données et Image d'un modèle d'état. Il identifie également certaines fonctions clé et des différences entre les éléments. Cet exemple décrit également la récupération de données BLOB et CLOB à partir d'une source de données. 

#Sub Report
OrdersAfter=Cet exemple explique comment créer un état utilisant des tables imbriquées. La requête liée à la table interne est paramétrée afin de renvoyer les détails de commande d'un numéro de commande donné d'une table externe. Cet exemple explique également comment référencer des colonnes de table externe dans une table imbriquée. Cette opération permet d'intégrer des informations principales provenant d'un état dans les états BIRT. La table externe traite ses lignes et la requête de la table interne est réexécutée en fonction des informations de la table externe. 

#Sorting
SortTableByReportParameter=Cet exemple explique comment créer un état utilisant des tables imbriquées. La requête liée à la table interne est paramétrée afin de renvoyer les détails de commande d'un numéro de commande donné d'une table externe. Cet exemple explique également comment référencer des colonnes de table externe dans une table imbriquée. Cette opération permet d'intégrer des informations principales provenant d'un état dans les états BIRT. La table externe traite ses lignes et la requête de la table interne est réexécutée en fonction des informations de la table externe. 

#Cross tab
CrosstabSampleRevenue=Cet exemple explique comment créer un état simple avec un élément d'état de tableau croisé (basé sur un cube BIRT). Il crée un tableau croisé affichant la gamme des produits dans la zone de colonne et année->trimestre dans la zone de ligne.
###########################################################
#Contribute Samples
Contribute_Samples=Veuillez soumettre une entrée bugzilla à l'adresse https://bugs.eclipse.org/bugs/enter_bug.cgi?product=BIRTb&bug_severity=enhancement et joignez le fichier du modèle d'état et une capture d'écran de la sortie d'état (au format .png, résolution de 800*600, taille inférieure ou égale à 250 Ko)

###########################################################
#Scripting

#Dynamic Map Rule
DynamicMapRule=Cet exemple décrit comment appeler l'outil de conception de rapports API dans le script avant génération pour ajouter une règle de mappage à un élément d'état.  

#Group Separator
GroupSeparator=Cet exemple décrit comment ajouter, dans un groupe, un séparateur toutes les 3 lignes. Ce nombre est réinitialisé lors d'un saut de page.  

#Retrieve Session Information
Application_Session_IntegrationBF=Cet exemple décrit comment utiliser un JavaScript pour récupérer des informations à partir d'une session Windows.

#Scripted Data Source
ScriptedDatasourceExampleAfter=Les exemples présentés dans cette section illustrent l'utilisation de scripts dans BIRT, à l'aide d'états simples. La plupart de ces exemples comportent un pré-état et un post-état, qu'il est possible de télécharger. En outre, les exemples sont disponibles sous la forme de présentations Flash. Pour charger l'objet Java exporté et faire en sorte que l'état puisse l'utiliser, veuillez redémarrer BIRT après avoir ouvert cet exemple d'état.  

#Visibility Rule
Addvisrule=Cet exemple décrit comment ajouter une règle de visibilité à un script.  

#Chart
SimpleChartAPI=Cet exemple illustre un grand nombre des méthodes de script java disponibles dans l'API graphique simple.  Ils proviennent du gestionnaire d'événement avant génération de l'état.

###########################################################
#Extending BIRT

#Aggreate function extension point
optim=Les exemples présentés dans cette section illustrent l'extension de BIRT, en décrivant l'implémentation de points d'extension BIRT fournis. Ces points permettent aux développeurs BIRT de modifier ou d'ajouter des paramètres au comportement par défaut du concepteur ou du moteur d'exécution. Ils peuvent ainsi ajouter des types de graphiques, des fonctions d'agrégation, des sources de données et des éléments d'état. 


 
