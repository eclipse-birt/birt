#Properties file for org.eclipse.birt.core.script.bre
Bundle-Name.0 = Plug-in de la fonction

dataType.String=Chaîne

dataType.Any=Indifférent

dataType.DateTime=DateTime
dataType.Integer=Entier

dataType.Double=Double

dataType.Number=Numéro

dataType.Boolean=Booléen

dataType.DoubleArray=Double[]

FunctionDesc.charLength=Renvoie le nombre de caractères de la chaîne.

FunctionDesc.concat=Renvoie la chaîne concaténée par toutes les chaînes données.

FunctionDesc.indexOf=Recherche la cible dans la source de la chaîne et revoie l'index de la première occurrence de correspondance. \nLa recherche commence au début de l'emplacement. Toutes les valeurs d'index ont 0 comme base.\nSi le début est omis, une valeur de 0 est appliquée. \nLa chaîne de recherche est sensible à la casse.

FunctionDesc.left=Renvoie les n premiers caractères de la chaîne ch. \nSi n correspond à la valeur 0, une chaîne vide est renvoyée. \nSi n est supérieur à la longueur de ch, toute la chaîne est renvoyée. \nSi n est omis, une valeur de 1 est utilisée.

FunctionDesc.right=Renvoie les n derniers caractères de la chaîne ch. \nSi n a la valeur 0, une chaîne vide est renvoyée. Si n est supérieur à la longueur de ch, toute la chaîne est renvoyée. \nSi n est omis, une valeur de 1 est utilisée.

FunctionDesc.search=Similaire à la fonction indexOf, sauf que : \n(1) la comparaison de chaînes est sensible à la casse \n(2) la chaîne de correspondance peut contenir des caractères génériques : \n* correspond à n'importe quelle séquence de caractères (y compris un caractère vide); \n? correspond à n'importe quel caractère unique.

FunctionDesc.toLower=Convertit tous les caractères de la source de la chaîne en minuscules.

FunctionDesc.toUpper=Convertit tous les caractères de la source de la chaîne en majuscules.

FunctionDesc.trim=Supprime tous les caractères d'espace de début et de fin (espace, tabulation, etc.).\nEn outre, tous les caractères d'espace consécutifs sont fusionnés en un seul caractère.

FunctionDesc.trimLeft=Supprime tous les espaces de début. Les espaces de fin et les espaces entre les mots ne sont pas supprimés.

FunctionDesc.trimRight=Supprime tous les espaces de fin. Les espaces de début et les espaces entre les mots ne sont pas supprimés.

FunctionDesc.anyOf=la source se trouve dans l'intervalle cible

FunctionDesc.between=VRAI si la source se trouve entre la cible1 et la cible2. FAUX dans les autres cas

FunctionDesc.notBetween=VRAI si la source ne se trouve pas entre la cible1 et la cible2. FAUX dans les autres cas

FunctionDesc.equalTo=la source est égale à la cible

FunctionDesc.greaterThan=la source est supérieure à la cible

FunctionDesc.lessThan=la source est inférieure à la cible

FunctionDesc.greaterOrEqual=la source est supérieure ou égale à la cible

FunctionDesc.lessOrEqual=la source est inférieure ou égale à la cible

FunctionDesc.notEqual=la source n'est pas égale à la cible

FunctionDesc.like=la source correspond à la cible de correspondance SQL

FunctionDesc.notLike=la source ne correspond pas à la cible de correspondance SQL

FunctionDesc.match=la source correspond à la cible de correspondance JavaScript

FunctionDesc.compareString=comparer les deux chaînes par rapport aux conditions spécifiées

FunctionDesc.add=Somme des deux nombres n1 et n2.

FunctionDesc.substract=n1 auquel on soustrait n2

FunctionDesc.multiple=Multiplie n1 par n2. Cette fonction est utilisée uniquement pour la compatibilité descendante de la fonction "multiply".

FunctionDesc.multiply=Multiplie n1 par n2

FunctionDesc.divide=le dividende est divisé par le diviseur

FunctionDesc.round=Arrondit un nombre à la quantité spécifiée de chiffres. \nDec est un nombre entier et peut être négatif. \nLa valeur par défaut pour dec est 0 lorsqu'elle est omise.

FunctionDesc.roundUp=Arrondit au nombre supérieur, autre que 0, à la quantité spécifiée de chiffres. \nLa valeur par défaut pour dec est 0.

FunctionDesc.roundDown=Arrondit au nombre inférieur, autre que 0, à la quantité spécifiée de chiffres. \nLa valeur par défaut pour dec est 0.

FunctionDesc.ceiling=Arrondit au nombre supérieur, autre que zéro, au multiple significatif le plus proche.

FunctionDesc.mod=Reste après la division du nombre par le diviseur. Le résultat a le même signe que le diviseur.

FunctionDesc.safeDivide=Renvoie le résultat si le résultat de la division du dividende par le diviseur n'est pas zéro ou l'infini. \nDans le cas contraire, renvoie la valeur donnée si le résultat est zéro.

FunctionDesc.year=Numéro à 4 chiffres de la valeur date/heure donnée

FunctionDesc.quarter=Numéro du trimestre (1 à 4) de la valeur date/heure donnée

FunctionDesc.month=Mois de la valeur date/heure. L'option est un entier :\n1 (par défaut) : renvoyer le numéro de mois (1-12) ;\n2 : renvoyer le nom de mois complet selon les paramètres locaux de l'utilisateur (par ex., de janvier à décembre pour les paramètres locaux français) ;\n3 : renvoyer le nom de mois abrégé selon les paramètres locaux de l'utilisateur (par ex., de janv. à déc. pour les paramètres locaux français).

FunctionDesc.week=Numéro de semaine de l'année (de 1 à 52) de la valeur date/jeure d.

FunctionDesc.day=Numéro de jour du mois (de 1 à 31).

FunctionDesc.weekDay=Jour de la semaine. L'option est un entier\n1 : renvoyer un numéro entre 1 (dimanche) et 7 (samedi) ;\n2 : renvoyer un numéro entre 1 (lundi) et 7 (dimanche) ;\n3 : renvoyer un numéro entre 0 (lundi) et 6 (dimanche) ;\n4 : renvoyer le nom du jour de la semaine selon les paramètres locaux de l'utilisateur (par ex., dimanche à samedi pour le français) ;\n5 : renvoyer le nom du jour de la semaine abrégé selon les paramètres locaux de l'utilisateur (par ex., dim. à sam. pour le français).

FunctionDesc.today=Renvoie une valeur d'horodatage qui correspond à minuit de la date actuelle.

FunctionDesc.now=Renvoie l'horodatage actuel.

FunctionDesc.diffYear=Renvoie une différence en nombre d'années.

FunctionDesc.diffMonth=Renvoie une différence en nombre de mois.

FunctionDesc.diffQuarter=Renvoie une différence en nombre de trimestres.

FunctionDesc.diffWeek=Renvoie une différence en nombre de semaines.

FunctionDesc.diffDay=Renvoie une différence en nombre de jours.

FunctionDesc.diffHour=Renvoie une différence en nombre d'heures.

FunctionDesc.diffMinute=Renvoie une différence en nombre de minutes.

FunctionDesc.diffSecond=Renvoie une différence en nombre de secondes.

FunctionDesc.addYear=Ajouter n année(s) à la date

FunctionDesc.addMonth=Ajouter n mois à la date

FunctionDesc.addQuarter=Ajouter n trimestre(s) à la date

FunctionDesc.addWeek=Ajouter n semaine(s) à la date

FunctionDesc.addDay=Ajouter n jour(s) à la date

FunctionDesc.addHour=Ajouter n heure(s) à la date

FunctionDesc.addMinute=Ajouter n minute(s) à la date

FunctionDesc.addSecond=Ajouter n seconde(s) à la date

FunctionDesc.firstDayOfYear=Renvoie le premier jour de l'année correspondant à la date.

FunctionDesc.firstDayOfQuarter=Renvoie le premier jour du trimestre correspondant à la date.

FunctionDesc.firstDayOfMonth=Renvoie le premier jour du mois correspondant à la date.

FunctionDesc.firstDayOfWeek=Renvoie le premier jour de la semaine correspondant à la date.

FunctionDesc.ddb=Dépréciation d'un bien sur une période simple donnée en utilisant les remarques sur la méthode d'amortissement dégressif à taux double.

FunctionDesc.fv=Valeur future d'une annuité, basée sur des remboursements périodiques et constants et sur un taux d'intérêt constant.

FunctionDesc.ipmt=Paiement d'intérêts sur une période donnée d'une annuité, \nbasé sur des remboursements périodiques et constants et sur un taux d'intérêt constant.

FunctionDesc.irr=Taux de retour interne d'une série de flux de trésorerie périodiques, de paiements et de reçus dans un tableau existant.

FunctionDesc.mirr=Taux de retour modifié pour une série de flux de trésorerie périodiques (paiements et reçus) dans un tableau existant.

FunctionDesc.nper=Nombre de périodes d'une annuité, basée sur des paiements périodiques et constants et sur un taux d'intérêt constant.

FunctionDesc.npv=Valeur actualisée nette d'une série variable de flux de trésorerie, positifs et négatifs, à un taux d'intérêt donné.

FunctionDesc.percent=Calcule le pourcentage des deux nombres

FunctionDesc.pmt=Remboursement pour une annuité, basé sur des paiements périodiques et constants et sur un taux d'intérêt constant.

FunctionDesc.ppmt=Remboursement principal pour une période donnée d'une annuité, basé sur un taux d'intérêt et des remboursements constants.

FunctionDesc.pv=Valeur actualisée d'une annuité basée sur des remboursements futurs périodiques et constants et sur un taux d'intérêt constant.

FunctionDesc.rate=Taux d'intérêt par période pour une annuité.

FunctionDesc.sln=Dépréciation linéaire d'un bien sur une période simple.

FunctionDesc.syd=Dépréciation d'un bien sur une période donnée en utilisant la méthode d'amortissement dégressif à taux fixe (méthode sum-of-years'-digits). Remarque

FunctionDesc.addDate=Renvoie une date obtenue en ajoutant les années, les mois et les jours à la date de début.

FunctionDesc.addTime=Renvoie une date obtenue en ajoutant les heures, les minutes et les secondes à la date de début.

FunctionDesc.subDate=Renvoie une date obtenue en soustrayant les années, les mois et les jours de la date de début.

FunctionDesc.subTime=Renvoie une date obtenue en soustrayant les heures, les minutes et les secondes à la date de début.

FunctionDesc.days=Le nombre de jours compris entre deux dates

FunctionDesc.hours=Le nombre d'heures comprises entre deux dates

FunctionDesc.minutes=Le nombre de minutes comprises entre deux dates

FunctionDesc.months=Le nombre de mois compris entre deux dates

FunctionDesc.seconds=Le nombre de secondes comprises entre deux dates

FunctionDesc.years=Le nombre d'années comprises entre deux dates

FunctionDesc.date= Renvoie un objet Date initialisé avec une année, un mois, un jour, une heure, une minute et une seconde donnés. \n(année, mois, jours (données obligatoires) ; heures, minutes, secondes (données facultatives)

FunctionDesc.duration.year=Permet d'obtenir la valeur du champ ANNEES en tant qu'entier, * ou 0 si manquant.\nLa valeur lexicalDuration est la présentation de la chaîne de durée, "PnYnMnDTnHnMnS"(par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.month=Permet d'obtenir la valeur du champ MOIS en tant qu'entier, * ou 0 si manquant.\nLa valeur lexicalDuration est la présentation de la chaîne de durée, "PnYnMnDTnHnMnS"(par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.day=Permet d'obtenir la valeur du champ JOURS en tant qu'entier, * ou 0 si manquant.\nLa valeur lexicalDuration est la présentation de la chaîne de durée, "PnYnMnDTnHnMnS"(par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.hour=Permet d'obtenir la valeur du champ HEURES en tant qu'entier, * ou 0 si manquant.\nLa valeur lexicalDuration est la présentation de la chaîne de durée, "PnYnMnDTnHnMnS"(par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.minute=Permet d'obtenir la valeur du champ MINUTES en tant qu'entier, * ou 0 si manquant.\nLa valeur lexicalDuration est la présentation de la chaîne de durée, "PnYnMnDTnHnMnS"(par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.second=Permet d'obtenir la valeur du champ SECONDES en tant qu'entier, * ou 0 si manquant.\nLa valeur lexicalDuration est la présentation de la chaîne de durée, "PnYnMnDTnHnMnS"(par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.timeInMills=Renvoie la durée en millisecondes.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. : P5Y2M15DT4H40M20S)\nL'argument startDate est utilisé pour ôter l'ambiguïté de cet écart.

FunctionDesc.duration.isLongerThan=Permet de vérifier si lexicalDuration1 est strictement plus long que lexicalDuration2.\nPar exemple, "P1D" (un jour) > "PT12H" (12 heures) et "P2Y" (deux ans) > "P23M" (23 mois)

FunctionDesc.duration.isShorterThan=Permet de vérifier si lexicalDuration1 est strictement plus court que lexicalDuration2.\nPar exemple,"PT12H" (12 heures) < "P1D" (un jour).

FunctionDesc.duration.add=Calcule une nouvelle durée, dont la valeur est : lexicalDuration1+lexicalDuration2.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. P5Y2M15DT4H40M20S)

FunctionDesc.duration.substract=Calcule une nouvelle durée, dont la valeur est : lexicalDuration1-lexicalDuration2.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. P5Y2M15DT4H40M20S)

FunctionDesc.duration.multiply=Calcule une nouvelle durée, dont la valeur est x fois plus longue que la valeur de lexicalDuration.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. P5Y2M15DT4H40M20S)\nFactor is the times longer of new Duration to create

FunctionDesc.duration.negate=Calcule un nouvel objet de durée, dont la valeur est : -lexicalDuration.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. P5Y2M15DT4H40M20S)

FunctionDesc.duration.getSign=Renvoie le signe de cette durée en -1,0 ou 1.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. : P5Y2M15DT4H40M20S)

FunctionDesc.duration.compare=Relation de commande partielle entre lexicalDuration1 et lexicalDuration2.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. P5Y2M15DT4H40M20S)

FunctionDesc.duration.addTo=Ajoute cette durée à un objet de date.\nLa valeur lexicalDuration est la présentation de la chaîne, "PnYnMnDTnHnMnS" (par ex. P5Y2M15DT4H40M20S)\nL'argument startDate est utilisé pour ôter l'ambiguïté de cet écart.

FunctionDesc.format=Permet d'obtenir la chaîne formatée en fonction de l'objet et de la correspondance spécifiés.
