# Description errors in metadata
MISSING_ELEMENT_NAME=Beim Hinzufügen eines Elements zur Metadatensammlung fehlt der Elementname.

DUPLICATE_ELEMENT_NAME=Beim Hinzufügen des Elements [{0}] zur Metadatensammlung sind Elementnamen doppelt vorhanden.

MISSING_STYLE_NAME=Beim Hinzufügen eines vordefinierten Stils zur Metadatensammlung fehlt der Stilname.

DUPLICATE_STYLE_NAME=Beim Hinzufügen des vordefinierten Stils [{0}] zur Metadatensammlung sind Stilnamen doppelt vorhanden.

STYLE_TYPE_MISSING=In der Metadatensammlung fehlt ein Stilelement.

STYLE_NOT_DEFINED=Der Stil sollte vor der Erstellung des Elements [{0}] festgelegt werden.

DUPLICATE_PROPERTY=Beim Hinzufügen der Eigenschaft zu Element [{1}] sind Eigenschaftennamen [{0}] doppelt vorhanden.

ELEMENT_PARENT_NOT_FOUND=Übergeordnetes Element [{0}] für Element [{1}] wurde nicht gefunden.

ILLEGAL_STYLE_PROPS=Für Element [{0}] wurden unzulässige Stileigenschaften festgelegt. Hierfür kann es zwei Gründe geben: 1. Das Attribut "hasStyle" des Elements lautet "False", doch es sind trotzdem Parameter für "StyleProperties" für das Element vorhanden. 2. Das Attribut "hasStyle" des Elements lautet "True", und es fungiert als Container (Anzahl der Felder ist größer als 0), doch es sind trotzdem "StyleProperties" für das Element vorhanden.

ILLEGAL_ABSTRACT_ELEMENT=Das Element [{0}] ist abstrakt, somit muss das übergeordnete Element [{1}] ebenfalls abstrakt sein.

INVALID_NAME_OPTION=Unzulässige Namensoption für Element [{0}].

INVALID_NAME_SPACE=Ungültiger Namensraum.

INVALID_STYLE_PROP_OPTION=Überprüfung fehlgeschlagen. Element [{0}] kann nicht zur Festlegung von Stileigenschaften [{1}] dienen, es sei denn, es fungiert als Stilelement.

STYLE_PROP_NOT_FOUND=Stileigenschaft [{0}] für Element [{1}] konnte nicht im Stilelement gefunden werden.

PROP_TYPE_ERROR=Der Parameter "PropertyType" für Eigenschaft [{0}] ist nicht angegeben. ( propDefn.getType() == Null )

MISSING_PROP_CHOICES=Fehlender Wert für "ChoiceSet" für die Auswahleigenschaft [{0}].

MISSING_SLOT_TYPE=Fehlende Feldinhaltstypen für "SlotDefn [{0}]".

MISSING_SLOT_NAME="displayNameID" für Feld [{0}] fehlt.

INVALID_SLOT_TYPE=Der Feldinhaltstyp [{0}] für Feld [{1}] ist ungültig.

ELEMENT_NAME_CONST=Das Element [{0}] wurde nicht definiert.

MISSING_CHOICE_SET_NAME=Beim Hinzufügen eines Auswahlsatzes zur Metadatensammlung wurde ein Auswahlsatzname nicht angegeben.

DUPLICATE_CHOICE_SET_NAME=Beim Hinzufügen des Auswahlsatzes [{0}] zur Metadatensammlung ist der Auswahlsatzname doppelt vorhanden.

MISSING_STRUCT_NAME=Beim Hinzufügen einer Struktur zur Metadatensammlung fehlt ein Strukturname.

DUPLICATE_STRUCT_NAME=Beim Hinzufügen der Struktur [{0}] zur Metadatensammlung ist der Strukturname doppelt vorhanden.

MISSING_STRUCT_DEFN=In der Strukturlistentypeigenschaft [{0}] unter [{1}] wurde eine Strukturdefinition nicht angegeben.

UNREFERENCABLE_STRUCT_DEFN=Die Strukturdefinition kann nicht als Referenz für Strukturverweistypeigenschaft [{0}] verwendet werden.

MISSING_ELEMENT_TYPE=In der Elementverweistypeigenschaft [{0}] wurde ein Elementverweis nicht angegeben.

UNDEFINED_ELEMENT_TYPE=Das Element [{0}], auf das in der Eigenschaft [{1}] verwiesen wird, wurde nicht definiert.

UNNAMED_ELEMENT_TYPE=Beim Element [{0}], auf das in der Eigenschaft [{1}] verwiesen wird, handelt es sich um ein unbenanntes Element (Namespace==''no_namespace'').

INCONSISTENT_PROP_TYPE=Die Eigenschaft [{0}] ist widersprüchlich. Die Stileigenschaft kann keine intrinsische Eigenschaft sein.

MISSING_JAVA_CLASS=Das Element [{0}] ist nicht abstrakt. Geben Sie Javaklassenattribute an.

JAVA_CLASS_INITIALIZE_ERROR=Das Javaklassenattribut für Element [{0}] ist ungültig. Die Instanziierung für den Klassennamen [{1}] konnte nicht erfolgen. Dies liegt möglicherweise daran, dass es sich um eine Schnittstelle oder eine abstrakte Klasse handelt oder dass für die angegebene Javaklasse kein Standardkonstruktor (bzw. ein Konstruktor, der keine Argumente zulässt) bereitgestellt wird.

JAVA_CLASS_LOAD_ERROR=Das Javaklassenattribut für Element [{0}] ist ungültig. Die Klasse [{1}] kann unter dem aktuellen Pfad nicht geladen werden.

INVALID_ELEMENT_JAVA_CLASS=Bei der angegebenen Javaklasse für das Element [{0}] handelt es sich nicht um ein DesignElement, d. h., die Klasse ist keine Instanz von DesignElement.

DUPLICATE_METHOD_NAME=Das Element [{0}] verfügt über zwei Methoden mit demselben Namen [{1}].

DUPLICATE_ARGUMENT_NAME=Die Methode [{0}] verfügt über zwei Argumente mit demselben Namen [{1}].

INVALID_DEFAULT_VALUE=Der Standardwert {1} für Eigenschaft {0} ist ungültig.

MISSING_CLASS_NAME=Beim Hinzufügen einer Klasse zur Metadatensammlung fehlt ein Klassenname.

DUPLICATE_CLASS_NAME=Beim Hinzufügen der Klasse [{0}] zur Metadatensammlung ist der Klassenname doppelt vorhanden.

MISSING_METHOD_NAME=Beim Hinzufügen einer Methode zur Klasse fehlt ein Methodenname.

CONSTRUCTOR_EXISTING=Der Konstruktor ist bereits vorhanden.

DUPLICATE_MEMBER_NAME=Beim Hinzufügen eines Mitglieds zu einer Klasse ist der Mitgliedername doppelt vorhanden.

MISSING_MEMBER_NAME=Beim Hinzufügen eines Mitglieds zur Klasse fehlt der Mitgliedername.

INVALID_LIST_TYPE=Die Liste von [{0}] wird nicht unterstützt.

VALIDATOR_NOT_FOUND=Der Validator [{0}] für Eigenschaft [{1}] wurde nicht in der Sammlung gefunden.

MISSING_VALIDATOR_NAME=Der Validator ist unbenannt.

DUPLICATE_VALIDATOR_NAME=Der Name des Validators [{0}] ist bereits vorhanden.

MISSING_EXTENSION_NAME=Beim Hinzufügen einer Erweiterung zur Metadatensammlung fehlt der Name der Erweiterung.

DUPLICATE_EXTENSION_NAME=Beim Hinzufügen der Erweiterung [{0}] zur Metadatensammlung ist der Erweiterungsname doppelt vorhanden.

EXTENSION_POINT_NOT_FOUND=Der Erweiterungspunkt [{0}] wurde nicht gefunden.

FAILED_TO_CREATE_INSTANCE=Die Klasse [{0}] kann nicht instanziiert werden.

VALUE_REQUIRED=Der Wert für {0} ist erforderlich.

DUPLICATE_CHOICE_NAME=Eine Auswahl mit dem Namen [{0}] ist bereits vorhanden.

INVALID_PROPERTY_TYPE=Der Eigenschaftstyp [{0}] ist ungültig.

INVALID_UNIT=Die Standardeinheit "{1}" für Eigenschaft "{0}" ist ungültig.

ENCYRPTION_EXTENSION_EXISTS=Die Verschlüsselungserweiterung mit der ID '{0}' ist vorhanden.

VISIBILITY_PROPERTY_NOT_FOUND=Eigenschaftssichtbarkeit:[{1}] des Elements: [{0}] verweist nicht auf eine bestehende Eigenschaft.

REPORT_MISSING=Das Stammelement Berichtdesign ist nicht definiert.

INVALID_CHOICE_VALUE=Der Auswahlwert "{0}" für Eigenschaft "{1}" ist ungültig.

MISSING_SUB_TYPE=Listeneigenschaft "{0}" verfügt nicht über einen untergeordneten Typ.

UNSUPPORTED_SUB_TYPE=Untergeordneter Typ "{1}" in Listeneigenschaft "{0}" wird nicht unterstützt.

SUB_TYPE_FORBIDDEN=Eigenschaft "{0}" ist keine Listeneigenschaft. Der untergeordnete Typ muss gleich null sein.

MISSING_XML_NAME=Der XML-Name wird nicht durch das konkrete Element "{0}" definiert.

DUPLICATE_XML_NAME=Der XML-Name "{0}" des Elements "{1}" steht im Konflikt mit vorhandenen Namen.

INVALID_ELEMENT_TYPE=Der Elementtyp "{0}" ist ungültig.

INVALID_CHOICE_PROPERTY=Auswahleigenschaft "{0}" in der Erweiterung "{1}" ist ungültig, da sie sowohl den Detailtyp als auch ihre eigene Auswahlliste definiert.

EXTENDED_ITEM_MISSING=In der Metadatensammlung fehlt ein erweitertes Element.

DEFAULT_ENCRYPTION_EXIST=Verschlüsselung "{0}" kann nicht als Standard eingestellt werden, da die Standardverschlüsselung "{1}" bereits vorhanden ist.

DEFAULT_EMPTY_STYLE_NAME=Der Standardstil für das Erweiterungselement "{0}" darf nicht leer sein.

EMPTY_DELTA_ROM_STREAM=Der von der Klasse '{1}' vom Erweiterungselement '{0}' zurückgegebene delta-ROM-Eingabe-Stream darf nicht leer sein.