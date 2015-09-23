# Description errors in metadata
MISSING_ELEMENT_NAME=Nom d'élément manquant lors de l'ajout d'un élément dans le dictionnaire de métadonnées.

DUPLICATE_ELEMENT_NAME=Noms d'éléments dupliqués lors de l'ajout de l'élément [{0}] dans le dictionnaire de métadonnées.

MISSING_STYLE_NAME=Nom de style manquant lors de l'ajout d'un style prédéfini dans le dictionnaire de métadonnées.

DUPLICATE_STYLE_NAME=Noms de style dupliqués lors de l'ajout du style prédéfini [{0}] dans le dictionnaire de métadonnées.

STYLE_TYPE_MISSING=Elément de style manquant dans le dictionnaire de métadonnées.

STYLE_NOT_DEFINED=Le style doit être défini avant l''élément stylisé [{0}].

DUPLICATE_PROPERTY=Noms de propriétés dupliqués [{0}] lors de l''ajout de la propriété à l''élément [{1}].

ELEMENT_PARENT_NOT_FOUND=Le parent [{0}] de l''élément [{1}] est introuvable.

ILLEGAL_STYLE_PROPS=Des propriétés de style incorrectes sont définies pour l''élément [{0}]. Deux causes possibles : 1. L''attribut "hasStyle" d''un élément est faux, mais StyleProperties est défini pour ce dernier. 2. L''attribut "hasStyle" d''un élément est vrai et il s''agit d''un conteneur(nombre d''emplacements>0), mais StyleProperties est défini pour ce dernier.

ILLEGAL_ABSTRACT_ELEMENT=L''élément [{0}] est abstrait, son parent [{1}] doit également l''être.

INVALID_NAME_OPTION=Option de nom incorrecte pour l''élément [{0}].

INVALID_NAME_SPACE=Espace de noms incorrect.

INVALID_STYLE_PROP_OPTION=Echec du contrôle de validité. L''élément [{0}] ne peut pas définir de propriété de style [{1}] à moins qu''il ne s''agisse de l''élément de style.

STYLE_PROP_NOT_FOUND=La propriété de style [{0}] de l''élément [{1}] est introuvable dans l''élément de style.

PROP_TYPE_ERROR=PropertyType de la propriété [{0}] n''a pas été défini. ( propDefn.getType() == null )

MISSING_PROP_CHOICES=ChoiceSet manquant pour la propriété de type choix [{0}].

MISSING_SLOT_TYPE=Types de contenu d''emplacement manquants pour SlotDefn [{0}].

MISSING_SLOT_NAME=displayNameID pour l''emplacement [{0}] est manquant.

INVALID_SLOT_TYPE=Le type de contenu d'emplacement [{0}] pour l'emplacement [{1}] est incorrect.

ELEMENT_NAME_CONST=L''élément [{0}] n''a pas été défini.

MISSING_CHOICE_SET_NAME=Nom de jeu de choix manquant lors de l'ajout d'un jeu de choix dans le dictionnaire de métadonnées.

DUPLICATE_CHOICE_SET_NAME=Noms de jeux de choix dupliqués lors de l'ajout du jeu de choix [{0}] dans le dictionnaire de métadonnées.

MISSING_STRUCT_NAME=Nom de structure manquant lors de l'ajout d'une structure dans le dictionnaire de métadonnées.

DUPLICATE_STRUCT_NAME=Noms de structure dupliqués lors de l''ajout de la structure [{0}] dans le dictionnaire de métadonnées.

MISSING_STRUCT_DEFN=Définition de structure manquante pour la propriété de type de liste de structure [{0}] dans [{1}].

UNREFERENCABLE_STRUCT_DEFN=La définition de structure n''est pas référençable pour la propriété de type de référence de structure [{0}].

MISSING_ELEMENT_TYPE=Référence d''élément manquante pour la propriété de type de référence d''élément [{0}].

UNDEFINED_ELEMENT_TYPE=L''élément [{0}] référencé par la propriété [{1}] n''est pas défini.

UNNAMED_ELEMENT_TYPE=L''élément [{0}] référencé par la propriété [{1}] est un élément sans nom(namespace==''no_namespace'').

INCONSISTENT_PROP_TYPE=Propriété incohérente [{0}], la propriété de style ne peut pas également être intrinsèque.

MISSING_JAVA_CLASS=L''élément [{0}] n''est pas abstrait, un attribut de classe Java doit être fourni.

JAVA_CLASS_INITIALIZE_ERROR=L''attribut de classe Java pour l''élément [{0}] est incorrect. Echec de l''instanciation du nom de classe [{1}], probablement parce qu''il s''agit d''une interface, d''une classe abstraite ou parce que la classe Java spécifiée ne fournit pas de constructeur par défaut (ou bien un constructeur n''acceptant pas d''argument).

JAVA_CLASS_LOAD_ERROR=L''attribut de classe Java spécifié pour l''élément [{0}] est incorrect, impossible de charger la classe [{1}] dans le chemin de classe actuel.

INVALID_ELEMENT_JAVA_CLASS=La classe Java de l''élément spécifié [{0}] n''est pas un élément de modèle, c''est-à-dire qu''elle n''est pas de type DesignElement.

DUPLICATE_METHOD_NAME=L''élément [{0}] comporte deux méthodes portant le même nom [{1}].

DUPLICATE_ARGUMENT_NAME=La méthode [{0}] comporte deux arguments portant le même nom [{1}].

INVALID_DEFAULT_VALUE=La valeur par défaut {1} de la propriété: {0} est incorrecte.

MISSING_CLASS_NAME=Nom de classe manquant lors de l'ajout d'une classe dans le dictionnaire de métadonnées.

DUPLICATE_CLASS_NAME=Nom de classe dupliqué lors de l''ajout de la classe [{0}] dans le dictionnaire de métadonnées.

MISSING_METHOD_NAME=Nom de méthode manquant lors de l''ajout d''une méthode à la classe.

CONSTRUCTOR_EXISTING=Le constructeur existe déjà.

DUPLICATE_MEMBER_NAME=Nom de membre dupliqué lors de l'ajout d'un membre à la classe.

MISSING_MEMBER_NAME=Nom de membre manquant lors de l'ajout d'un membre à la classe.

INVALID_LIST_TYPE=La liste de [{0}] n''est pas prise en charge.

VALIDATOR_NOT_FOUND=Le validateur [{0}] pour la propriété [{1}] est introuvable dans le dictionnaire.

MISSING_VALIDATOR_NAME=Le nom du validateur est manquant.

DUPLICATE_VALIDATOR_NAME=Le nom du validateur [{0}] existe déjà.

MISSING_EXTENSION_NAME=Nom d'extension manquant lors de l'ajout d'une extension dans le dictionnaire de métadonnées.

DUPLICATE_EXTENSION_NAME=Nom d''extension dupliqué lors de l''ajout de l''extension [{0}] dans le dictionnaire de métadonnées.

EXTENSION_POINT_NOT_FOUND=Le point d''extension [{0}] est introuvable.

FAILED_TO_CREATE_INSTANCE=Impossible d''instancier la classe [{0}].

VALUE_REQUIRED=La valeur de [{0}] est requise.

DUPLICATE_CHOICE_NAME=Le choix portant le nom [{0}] existe déjà.

INVALID_PROPERTY_TYPE=Le type de propriété [{0}] est incorrect.

INVALID_UNIT=L''unité par défaut "{1}" de la propriété "{0}" est incorrecte.

ENCYRPTION_EXTENSION_EXISTS=L''extension de chiffrement avec l''ID "{0}" existe.

VISIBILITY_PROPERTY_NOT_FOUND=Visibilité des propriétés :[{1}] dans l''élément : [{0}] ne désigne aucune propriété existante.

REPORT_MISSING=L'élément racine Modèle d'état n'est pas défini.

INVALID_CHOICE_VALUE=La valeur de choix "{0}" de la propriété "{1}" est incorrecte.

MISSING_SUB_TYPE=La propriété de liste "{0}" n''a aucun sous-type.

UNSUPPORTED_SUB_TYPE=Le sous-type "{1}" de la propriété de liste "{0}" n''est pas pris en charge.

SUB_TYPE_FORBIDDEN=La propriété "{0}" n''est pas une propriété de liste ; le sous-type doit donc être nul.

MISSING_XML_NAME=L''élément concret "{0}" ne définit pas le nom xml.

DUPLICATE_XML_NAME=Le nom xml "{0}" de l'élément "{1}" est en conflit avec les noms existants.

INVALID_ELEMENT_TYPE=Le type d''élément "{0}" est incorrect.

INVALID_CHOICE_PROPERTY=Propriété de choix "{0}" dans l'extension "{1}" incorrecte car elle définit à la fois le type de détails et sa propre liste de choix.

EXTENDED_ITEM_MISSING=Elément étendu manquant dans le dictionnaire de métadonnées.

DEFAULT_ENCRYPTION_EXIST=Le chiffrement "{0}" ne peut pas être défini comme chiffrement par défaut, car le chiffrement par défaut "{1}" existe déjà.

DEFAULT_EMPTY_STYLE_NAME=Le style par défaut pour l'élément d'extension	"{0}" ne doit pas être vierge.

EMPTY_DELTA_ROM_STREAM=L'entrée delta-rom renvoyée par la classe "{1}" depuis l'élément d'extension "{0}" ne doit pas être vide.