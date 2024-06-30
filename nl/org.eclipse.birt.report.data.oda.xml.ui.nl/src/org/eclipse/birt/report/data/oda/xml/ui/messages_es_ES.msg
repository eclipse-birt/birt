# wizard dialog messages
wizard.title.selectFolder=Fuente de datos XML
wizard.defaultMessage.selectFolder=Defina las URL al archivo XML y la información de esquema.
wizard.title.newDataSet=Nuevo conjunto de datos
wizard.title.selectXmlFile=Seleccionar datos XML para la vista previa
wizard.defaultMessage.selectFile=Seleccione una fuente de XML de muestra para activar la vista previa de datos en esta sección.
wizard.title.xPathChoose=Definir asignación de tablas
wizard.defaultMessage.selectXPath=Defina el elemento que se va a utilizar como tabla seleccionando el elemento en la estructura XML y añadiéndolo a la asignación de tablas. Sólo puede haber una asignación de tablas por cada conjunto de datos XML.
wizard.title.selectColumn=Definir asignación de columnas
wizard.title.defineColumnMapping=Defina la asignación de columnas seleccionando un elemento o un atributo en la estructura XML y añadiéndolo a la asignación de columnas. 

#preference
designer.preview.preference.numberOfElementPassedToGetSchema=Número de elementos con acceso en el análisis de árbol de esquema XML.
designer.preview.preference.numberOfElementPassedToGetSchema.errormessage=Introduzca un número entre 0 y {0}.
designer.preview.preference.columnMappingPage.group.title=Página de asignación de columnas

#table mapping page messages
xPathChoosePage.messages.rowMapping=Asignación de tablas
xPathChoosePage.messages.xPathExpression=Expresión &XPath
xPathChoosePage.messages.xmlStructure=Estructura XML
xPathChoosePage.messages.xmlColumnMapping=Asignación de columnas

#sample xml data file page messages
xmlDataSetSelectionPage.messages.lineofdata=líneas de fuente de datos
xmlDataSetSelectionPage.warning.errorReadXMLFile=Error al leer los datos XML del archivo [{0}]\n{1}. ¿Desea continuar?

#column mapping page messages
button.refresh=Actualizar
menu.menuItem.removeAll=Quitar todos
menu.menuItem.remove=Quitar
lable.selectXmlFile=Introduzca la URL de la fuente &XML o busque el archivo que contiene los datos:
lable.selectXmlSchmaFile=Introduzca la URL del esquema X&ML o busque el archivo que contiene el\n esquema. Déjelo vacío si no hay ningún esquema disponible:
label.preview=&Previsualizar primero
label.useXMLFileFromDataSource=Utilizar el archivo XML definido en la fuente de datos
RowMappingDialog.title=Seleccione o edite la expresión XPath.
RowMappingDialog.info=Seleccione una de las expresiones XPath predefinidas para el nodo que ha seleccionado o\n introduzca otra expresión:
ColumnMappingDialog.info.columnName=Nombre de columna:
ColumnMappingDialog.info.xPath=Expresión XPath:
ColumnMappingDialog.info.dataType=Tipo de datos:
ColumnMappingDialog.info.dataPreview=Vista previa de tablas
ColumnMappingDialog.prompt.new=Nueva asignación aquí...
ColumnMappingDialog.prompt.maxRow=Los datos fuente tienen más de 500 líneas. La vista previa sólo mostrará las primeras 500 líneas en esta página.
ColumnMappingDialog.prompt.dialog.title=Vista previa de resultados

dataset.editor.columnName=Nombre
dataset.editor.xpathexpression=XPath
dataset.editor.datatype=Tipo
dataset.editor.rowelement=Elemento de fila
dataset.error.populateXMLTree=No se puede recuperar la fuente de datos XML. El archivo de fuente de datos XML no es válido o no existe.

error.label=Error
error.selectFolder=La URL de archivo seleccionada no es válida o el archivo no existe.
error.emptyPath=La URL del archivo está vacía.
error.xpath.canNotContainAttribute=No se puede asignar la expresión XPath a un atributo XML.
error.dataset.maxRowNumberError=La fila máxima debe ser un número.
error.columnMapping.noMappingColumn=Asigne al menos una columna.
error.file.null=Todas las URL están vacías, lo que significa que no ha seleccionado ninguna fuente de datos. ¿Seguro que desea conservarlo?
error.columnMapping.sameColumnName=El nombre de la columna [{0}] ya existe. Utilice otro nombre.
error.invalidXpath=La expresión XPath  "{0}" no es válida.

#data types for column mapping
datatypes.dateTime=Fecha/hora
datatypes.decimal=Decimal largo
datatypes.float=Doble
datatypes.integer=Entero
datatypes.string=String
datatypes.date=Fecha
datatypes.time=Hora

#column name in column mapping table
columnName.name=Nombre
columnName.xPathName=XPath
columnName.typeName=Tipo

dataset.new=Nuevo conjunto de datos
file.choose=&Examinar...
title.warning=Advertencia

datasource.propertyPage.tooltip=Fuente de datos XML
