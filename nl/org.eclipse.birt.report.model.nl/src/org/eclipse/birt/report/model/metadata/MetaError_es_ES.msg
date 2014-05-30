# Description errors in metadata
MISSING_ELEMENT_NAME=Falta el nombre de elemento al añadir un elemento al diccionario de metadatos.

DUPLICATE_ELEMENT_NAME=Nombres de elemento duplicados al añadir el elemento [{0}] al diccionario de metadatos.

MISSING_STYLE_NAME=Falta el nombre de estilo al añadir un estilo predefinido al diccionario de metadatos.

DUPLICATE_STYLE_NAME=Nombres de estilo duplicados al añadir el estilo predefinido [{0}] al diccionario de metadatos.

STYLE_TYPE_MISSING=Falta el elemento de estilo en el diccionario de metadatos.

STYLE_NOT_DEFINED=Debe definir el estilo antes que el elemento de estilo [{0}].

DUPLICATE_PROPERTY=Nombres de propiedad duplicados [{0}] al añadir la propiedad al elemento [{1}].

ELEMENT_PARENT_NOT_FOUND=No se ha encontrado el principal [{0}] para el elemento [{1}].

ILLEGAL_STYLE_PROPS=Se han definido propiedades de estilo no válidas en el elemento [{0}]. Dos posibles razones: 1. El atributo "hasStyle" de un elemento devuelve false, pero tiene definidas StyleProperties. 2. El atributo "hasStyle" de un elemento devuelve true, y es un contenedor(recuento de slot>0), pero tiene definidas StyleProperties.

ILLEGAL_ABSTRACT_ELEMENT=El elemento [{0}] es abstracto. Su principal [{1}] también debe serlo.

INVALID_NAME_OPTION=Opción de nombre no válida para el elemento [{0}].

INVALID_NAME_SPACE=Espacio de nombre no válido.

INVALID_STYLE_PROP_OPTION=Se ha producido un error al comprobar la validez. El elemento [{0}] no puede definir las propiedades de estilo [{1}] a menos que sea el elemento de estilo.

STYLE_PROP_NOT_FOUND=No se ha encontrado la propiedad de estilo [{0}] para el elemento [{1}] en el elemento de estilo.

PROP_TYPE_ERROR=No se ha ajustado PropertyType de la propiedad [{0}]. ( propDefn.getType() == null )

MISSING_PROP_CHOICES=Falta ChoiceSet para la propiedad del tipo de selección [{0}].

MISSING_SLOT_TYPE=Faltan los tipos de contenido del slot para SlotDefn [{0}].

MISSING_SLOT_NAME=Falta displayNameID del slot [{0}].

INVALID_SLOT_TYPE=El tipo de contenido del slot [{0}] para el slot [{1}] no es válido.

ELEMENT_NAME_CONST=No se ha definido el elemento [{0}].

MISSING_CHOICE_SET_NAME=Falta el nombre de conjunto de selecciones al añadir un conjunto de selecciones al diccionario de metadatos.

DUPLICATE_CHOICE_SET_NAME=Nombres de conjunto de selecciones duplicados al añadir el conjunto de selecciones [{0}] al diccionario de metadatos.

MISSING_STRUCT_NAME=Falta el nombre de estructura al añadir una estructura al diccionario de metadatos.

DUPLICATE_STRUCT_NAME=Nombres de estructura duplicados al añadir la estructura [{0}] al diccionario de metadatos.

MISSING_STRUCT_DEFN=Falta la definición de estructura para la propiedad del tipo de lista de estructuras [{0}] en [{1}].

UNREFERENCABLE_STRUCT_DEFN=La definición de estructura no es referenciable para la propiedad de tipo de referencia de estructura [{0}].

MISSING_ELEMENT_TYPE=Falta la referencia de elemento para la propiedad del tipo de referencia de elemento [{0}].

UNDEFINED_ELEMENT_TYPE=No se ha definido el elemento [{0}] al que hace referencia la propiedad [{1}].

UNNAMED_ELEMENT_TYPE=El elemento [{0}] al que hace referencia la propiedad [{1}] es un elemento sin denominar(namespace==''no_namespace'').

INCONSISTENT_PROP_TYPE=La propiedad [{0}] es incoherente. La propiedad de estilo no puede ser intrínseca.

MISSING_JAVA_CLASS=El elemento [{0}] no es abstracto. Debe proporcionar el atributo de clase Java.

JAVA_CLASS_INITIALIZE_ERROR=El atributo de clase Java para el elemento [{0}] no es válido. Se ha producido un error al crear instancias para el nombre de la clase [{1}]. Puede que sea una interfaz o una clase abstracta o que la clase Java especificada no proporcione un constructor predeterminado (o un constructor sin argumento).

JAVA_CLASS_LOAD_ERROR=El atributo de clase Java especificado para el elemento [{0}] no es válido. No se puede cargar la clase [{1}] en la ruta de clase actual.

INVALID_ELEMENT_JAVA_CLASS=La clase Java de elemento especificada [{0}] no es un elemento de diseño, esto es, la clase no es un tipo de elemento de diseño.

DUPLICATE_METHOD_NAME=El elemento [{0}] tiene dos métodos con el mismo nombre [{1}].

DUPLICATE_ARGUMENT_NAME=El método [{0}] tiene dos argumentos con el mismo nombre [{1}].

INVALID_DEFAULT_VALUE=El valor predeterminado {1} para la propiedad: {0} no es válido.

MISSING_CLASS_NAME=Falta el nombre de la clase al añadir una clase al diccionario de metadatos.

DUPLICATE_CLASS_NAME=Nombre de la clase duplicado al añadir la clase [{0}] al diccionario de metadatos.

MISSING_METHOD_NAME=Falta el nombre de método al añadir un método a la clase.

CONSTRUCTOR_EXISTING=Ya existe este constructor.

DUPLICATE_MEMBER_NAME=Nombre de miembro duplicado al añadir un miembro a la clase.

MISSING_MEMBER_NAME=Falta el nombre de miembro al añadir un miembro a la clase.

INVALID_LIST_TYPE=No se admite la lista de [{0}].

VALIDATOR_NOT_FOUND=No se ha encontrado en el diccionario el validador [{0}] de la propiedad [{1}].

MISSING_VALIDATOR_NAME=Falta el nombre del validador.

DUPLICATE_VALIDATOR_NAME=El nombre del validador [{0}] ya existe.

MISSING_EXTENSION_NAME=Falta el nombre de extensión al añadir una extensión al diccionario de metadatos.

DUPLICATE_EXTENSION_NAME=Nombre de extensión duplicado al añadir la extensión [{0}] al diccionario de metadatos.

EXTENSION_POINT_NOT_FOUND=No se ha encontrado el punto de extensión [{0}].

FAILED_TO_CREATE_INSTANCE=No se puede crear una instancia de la clase [{0}].

VALUE_REQUIRED=Es necesario el valor de [{0}].

DUPLICATE_CHOICE_NAME=La selección con el nombre [{0}] ya existe.

INVALID_PROPERTY_TYPE=El tipo de propiedad [{0}] no es válido.

INVALID_UNIT=La unidad predeterminada "{1}" en la propiedad "{0}" no es válida.

ENCYRPTION_EXTENSION_EXISTS=La extensión de cifrado con ID "{0}" ya existe.

VISIBILITY_PROPERTY_NOT_FOUND=La visibilidad de las propiedades:[{1}] del elemento: [{0}] no indica una propiedad existente.

REPORT_MISSING=Elemento raíz: el diseño de informe no está definido.

INVALID_CHOICE_VALUE=El valor de selección "{0}" de la propiedad "{1}" no es válido.

MISSING_SUB_TYPE=La propiedad de lista "{0}" no tiene ningún subtipo.

UNSUPPORTED_SUB_TYPE=No se admite el subtipo "{1}" de la propiedad de lista "{0}".

SUB_TYPE_FORBIDDEN=La propiedad "{0}" no es una propiedad de lista, por tanto, el subtipo debe ser nulo.

MISSING_XML_NAME=El elemento concreto "{0}" no define el nombre xml.

DUPLICATE_XML_NAME=El nombre xml "{0}" del elemento "{1}" entra en conflicto con los nombres existentes.

INVALID_ELEMENT_TYPE=El tipo de elemento [{0}] no es válido.

INVALID_CHOICE_PROPERTY=La propiedad de selección "{0}" de la extensión "{1}" no es válida, ya que define tanto el detalle-tipo como su propia lista de selección.

EXTENDED_ITEM_MISSING=Falta un elemento ampliado en el diccionario de metadatos.

DEFAULT_ENCRYPTION_EXIST=La encriptación "{0}" no se puede configurar de forma predeterminada, ya que existe la encriptación predeterminada"{1}".

DEFAULT_EMPTY_STYLE_NAME=El estilo predeterminado del elemento de extensión	"{0}" no puede estar vacío.

EMPTY_DELTA_ROM_STREAM=La secuencia de entrada delta-rom devuelta por clase "{1}" del elemento de extensión "{0}" no debe estar vacía.