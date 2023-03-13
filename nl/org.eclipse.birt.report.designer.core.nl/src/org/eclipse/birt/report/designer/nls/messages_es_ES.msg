#/*******************************************************************************
# * Copyright (c) 2006, 2012 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v2.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-2.0.html
# *
# * Contributors:
# *  Actuate Corporation - initial API and implementation
# *******************************************************************************/

#########################################################################
# Bidi RTL Language Resolution:
# If the display text is all English, it should insert the RLM character 
# '\u200f' before the string.  
# Phone(Count) : (Phone(Count
# Phone(\u200fCount) : (Count)Phone
#########################################################################

###########################################################
#AbstractDataSetWizard properties
AbstractDataSetWizard.ModelTrans.Create=Definir el nombre, la fuente y el tipo del conjunto de datos
###########################################################
#CssErrDialog
CssErrDialog.shellTitle.ImportCssStyleMssageTitle=Importar mensaje

CssErrDialog.AreaTitle=Importar mensaje de estilos CSS

CssErrDialog.AreaMessage=Se han producido algunos errores o advertencias al importar los estilos CSS.

CssErrDialog.Severity=Gravedad:

CssErrDialog.Message=Mensaje:

CssErrDialog.FatalError=Error fatal

CssErrDialog.Error=Error

CssErrDialog.Warning=Advertencia

###########################################################
#AddGroupAction properties
AddGroupAction.actionMsg.addGroup=Insertar grupo...

AddGroupAction.Menu.Text=Insertar grupo...

AddGroupAction.stackMsg.addGroup=Insertar grupo

###########################################################
#AddLibraryAction properties
AddLibraryAction.Error.FileNotFound=No se ha encontrado el archivo ''{0}''.

AddLibraryAction.Error.FileIsNotLibrary=El archivo ''{0}'' no termina en ''{1}''.

AddLibraryAction.Error.Message=El archivo ya existe.

AddLibraryAction.Error.Title=Error

AddLibraryAction.Text=Añadir librería...

AddLibraryAction.Dialog.Tilte=Seleccione una librería

###########################################################
#AddCSSAction properties
AddCSSAction.Text=Añadir archivo CSS...

###########################################################
#AddResourceAction properties
AddResourceAction.Text=Añadir &recurso...

AddResourceAction.Dialog.Tilte=Seleccionar un recurso

AddResourceAction.Error.Message=El archivo ya existe.

AddResourceAction.Error.FileNotFound=No se ha encontrado el archivo ''{0}''.

AddResourceAction.Error.Title=Error

AddResourceAction.Error.FileIsNotResource=El archivo ''{0}'' no es un recurso.

###########################################################
#AddCSSDialog
AddCSSWizardDialog.ShellText=Añadir archivo CSS

AddCSSWizardDialog.TitleArea=Añadir archivo CSS a la carpeta de recursos

AddCSSWizardDialog.Message=Seleccione un archivo CSS para importar estilos a una carpeta de recursos

###########################################################
#MoveResourceDialog
MoveResourceDialog.Title=Mover recurso

MoveResourceDialog.Message=Seleccionar una carpeta

###########################################################
#NewLibraryDialog
NewLibraryDialog.Title=Nueva librería

NewLibraryDialog.Message=Seleccionar una carpeta

NewLibraryDialog.label.NewLibrary=Nuevo nombre de librería

###########################################################
#PublishCSSAction properties
PublishCSSAction.wizard.errorTitle=Error

PublishCSSAction.wizard.message=No es posible publicar el archivo CCS en el directorio donde se encuentra, seleccione otro directorio para la publicación.

PublishCSSAction.wizard.msgDirErr=El directorio de destino no existe y no se puede crear.

PublishCSSAction.wizard.message.SourceFileNotExist=El archivo fuente no existe.

PublishCSSAction.wizard.notvalidfolder=La carpeta del archivo CSS definida como preferencia no es válida.


###########################################################
#ResourceAction properties
DeleteLibraryAction.Text=&Eliminar

CopyLibraryAction.Text=&Copiar

PasteLibraryAction.Text=&Pegar

MoveLibraryAction.Text=&Mover...

RenameLibraryAction.Text=Ca&mbiar nombre

NewFolderAction.Text=&Carpeta

NewLibraryAction.Text=&Librería

NewResource.MenuGroup.Text=&Nuevo

RenameResourceAction.nameExists=Ya existe un archivo o carpeta con este nombre

RenameResourceAction.inputDialogTitle=Cambiar nombre del recurso

RenameResourceAction.inputDialogMessage=Nuevo nombre:

NewFolderAction.nameExists=Ya existe una carpeta con este nombre

NewFolderAction.emptyName=El nombre no puede estar vacío

NewFolderAction.inputDialogTitle=Nueva carpeta

NewFolderAction.inputDialogMessage=Nombre de carpeta:

DeleteResourceAction.Dialog.Title=Eliminar recurso

DeleteResourceAction.Dialog.Message=¿Seguro que desea eliminar los recursos seleccionados?

PasteResourceAction.Dialog.Title=Pegar recurso

PasteResourceAction.ReadOnlyEncounter.Title=Se ha encontrado un archivo de sólo lectura

PasteResourceAction.ReadOnlyEncounter.Message=El archivo ''{0}'' es de sólo lectura, asegúrese primero de que se puede escribir en él.

PasteResourceAction.Dialog.Message=El recurso existe. Especificar un &nuevo nombre:

PasteResourceAction.Dialog.FilenameSuffix.CopyOf=Copia de

MoveResourceAction.Dialog.Title=Mover recurso

MoveResourceAction.Dialog.Message=El recurso ya existe. ¿Desea sustituirlo?

ResourceAction.ConvertToFile.URLIsNull=La URL es nula, no se puede convertir a un archivo.

ResourceAction.FileRemoveFailure=Error al eliminar archivos.

LibraryNotExist.Dialog.Title=Abrir librería

LibraryNotExist.Dialog.Message=La librería no existe, ¿desea actualizar el explorador de recursos?

DesignerNotExist.Dialog.Title=Abrir diseño de informe

DesignerNotExist.Dialog.Message=El diseño de informe no existe, ¿desea actualizar el explorador de recursos?
###########################################################
#LibraryFileFilterAction properties
LibraryFileFilterAction.Text=Filtrar...

###########################################################
#AddStyleRuleAction properties
AddStyleAction.actionMsg.addStyleRule=&Nuevo estilo...

AddStyleAction.stackMsg.addStyle=&Nuevo estilo

###########################################################
#ApplyStyleAction properties
ApplyStyleAction.actionLabel.none=Ninguno

ApplyStyleAction.stackMsg.applyStyle=Aplicar estilo

###########################################################
#ApplyThemeAction properties
ApplyThemeAction.actionLabel.none=Ninguno

ApplyThemeAction.stackMsg.applyStyle=Aplicar tema

###########################################################
#AttributesBuilder properties
AttributesBuilder.Label.Cell=Editor de propiedades - Celda

AttributesBuilder.Label.Column=Editor de propiedades - Columna

AttributesBuilder.Label.Data=Editor de propiedades - Datos

AttributesBuilder.Label.Generic=Editor de propiedades - {0}

AttributesBuilder.Label.Grid=Editor de propiedades - Cuadrícula

AttributesBuilder.Label.Label=Editor de propiedades - Etiqueta

AttributesBuilder.Label.Library=Editor de propiedades - Librería

AttributesBuilder.Label.List=Editor de propiedades - Lista

AttributesBuilder.Label.Cube=Editor de propiedades - Cubo

AttributesBuilder.Label.DynamicFilterParameter=Editor de propiedades - Parámetro de filtro dinámico

AttributesBuilder.Label.MasterPage=Editor de propiedades - Página maestra

AttributesBuilder.Label.None=Editor de propiedades - Ninguno

AttributesBuilder.Label.Picture=Editor de propiedades - Imagen

AttributesBuilder.Label.Report=Editor de propiedades - Informe

AttributesBuilder.Label.Row=Editor de propiedades - Fila

AttributesBuilder.Label.Table=Editor de propiedades - Tabla

AttributesBuilder.Label.Text=Editor de propiedades - Texto

AttributesBuilder.Label.TextData=Editor de propiedades - Texto dinámico

AttributesBuilder.Label.DataSourceHandle=Editor de propiedades - Fuente de datos

AttributesBuilder.Label.VariableElementHandle=Editor de propiedades - Variable

AttributesBuilder.Label.DataSetHandle=Editor de propiedades - Conjunto de datos

AttributesBuilder.Label.ScalarParameterHandle=Editor de propiedades - Parámetro

AttributesBuilder.Label.TemplateReportItemHandle=Editor de propiedades - Elemento de informe de plantilla

AttributesBuilder.Label.AutoText=Editor de propiedades - AutoText

AttributesBuilder.Label.ParameterGroup=Editor de propiedades - Grupo de parámetros

AttributesBuilder.Label.CascadingParameterGroup=Editor de propiedades - Grupo de parámetros en cascada

###########################################################
#AttributeView properties
AttributeView.dialg.Message.Warning=Advertencia

AttributeView.dialg.Message.Yes=S&í

AttributeView.dialg.Message.No=N&o

AttributeView.dialg.Message.Cancel=C&ancelar

AttributeView.dialg.Message.PromptMsg=¿Desea borrar todas las propiedades locales?

AttributeView.toolbar.tooltip.EditingToggleAction.ClearMsg=Borrar propiedades locales.

AttributeView.toolbar.tooltip.EditingToggleAction.EnableEditingMsg=Activar la edición de las propiedades.

AttributeView.toolbar.tooltip.EditingToggleAction.DisabledActionMsg=Esta acción no se encuentra disponible para elementos locales.

AttributeView.view.message.Emptypage= Este valor {0} se extiende desde el valor {1} en la librería {2}. Para modificar las propiedades del valor {3} en este diseño de informe, haga clic en el botón Activar de la barra de herramientas Editor de propiedades.

AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg=Restaurar propiedades del elemento de la librería original

AttributeView.defaultText.noAttributeView = El editor de propiedades no está disponible.
###########################################################
#BackGroundPreferencePage properties
BackGroundPreferencePage.displayname.Title=Fondo

###########################################################
#BasAttributePage properties
BasAttributePage.Label.Attributes=Propiedades:

BaseAttributePage.CommandStack.ResetStyle.Message=Restaurar al estilo predeterminado

###########################################################
#BaseInsertMenuAction properties
BaseInsertMenuAction.stackMsg.insertElement=Insertar elemento



###########################################################
#BasePaletteFactory properties
BasePaletteFactory.AutoText.Confidential=Confidencial

BasePaletteFactory.AutoTextLabel.CreatedBy=Creado por:

BasePaletteFactory.AutoTextLabel.CreatedOn=Creado el:

BasePaletteFactory.AutoTextLabel.Filename=Nombre de archivo:

BasePaletteFactory.AutoTextLabel.LastPrinted=Última impresión:

BasePaletteFactory.AutoTextLabel.Page=Página:

BasePaletteFactory.AutoTextLabel.PageCount=Recuento de páginas:

BasePaletteFactory.AutoTextLabel.PageXofY=Página X de Y

BasePaletteFactory.categoryName.PointerSelect=Selección de puntero

BasePaletteFactory.categoryName.RectangleSelect=Selección de rectángulo

BasePaletteFactory.Group.Items=Elementos

BasePaletteFactory.toolTip.PointerSelect=Haga clic en un elemento para seleccionar o presione CTRL y haga clic en los elementos que desee seleccionar.

BasePaletteFactory.toolTip.RectangleSelect=Haga clic y arrastre para describir un rectángulo y seleccionar todos los elementos contenidos en el área.

###########################################################
#BaseStylePreferencePage properties
BaseStylePreferencePage.displayname.Title=General

###########################################################
#BgImageFieldEditor properties
BgImageFieldEditor.displayname.Browse=E&xaminar...

###########################################################
#BindingPage properties
BindingPage.Dataset.Label=Con&junto de datos:

BindingPage.DataSetBindingSelector.Title.DataSet=Enlace del conjunto de datos

BindingPage.DataSetBindingSelector.Title.LinkModel=Enlace del modelo de datos

BindingPage.ReportItem.Label=Elemento de &informe:

BindingPage.ReportItem.NoName=Sin nombre

BindingPage.Label.Binding=Enlace

BindingPage.MessageDlg.SaveParamBinding=Guardar ParamBinding

BindingPage.None=Ninguno

BindingPage.TableColumn.DataType=Tipo de datos

BindingPage.TableColumn.Parameter=Parámetro

BindingPage.TableColumn.Value=Valor

ColumnBingingDialog.Label.Warn=Cambiar el conjunto de datos o la referencia del elemento de informe podría invalidar todas las expresiones de la columna de conjunto de resultados.

ColumnBindingDialog.Label.DataSet=Conjunto de datos:

ColumnBindingDialog.Column.Expression=Expresión

ColumnBindingDialog.DialogTitle=Seleccionar enlace de datos

ColumnBindingDialog.Column.Name=Nombre

ColumnBindingDialog.Column.Filter=Filtro

ColumnBindingDialog.Column.Function=Función

ColumnBindingDialog.Column.DisplayName=Nombre en pantalla

ColumnBindingDialog.Column.DisplayNameID=ID de nombre en pantalla

ColumnBindingDialog.InputPrompt=<Introduzca el nuevo valor aquí...>

ColumnBindingDialog.Button.Generate=Generar enlaces de columna

ColumnBindingDialog.Column.DataType=Tipo de datos

ColumnBindingDialog.Column.AggregateOn=Agregar en

ColumnBindingDialog.All=Todos

ColumnBindingDialog.NONE=Ninguno

ColumnBindingDialog.Choice.DatasetFromContainer=Conjunto de datos del contenedor

ColumnBindingDialog.Choice.ReportItemFromContainer=Elemento de informe al que se hace referencia del contenedor

ColumnBindingDialog.AGGREGATEON.NONE=N/A
ColumnBindingDialog.Text.Add=&Añadir...

ColumnBindingDialog.Text.Edit=&Editar...

ColumnBindingDialog.Text.Del=Quita&r

ColumnBindingDialog.Text.Refresh=Ac&tualizar

ColumnBindingDialog.Text.AddAggr=Añadir a&gregación...

###########################################################
#BlockPreferencePage properties
BlockPreferencePage.displayname.Title=Bloque de texto

###########################################################
#BlockPreferencePage properties
SizePreferencePage.displayname.Title=Tamaño

###########################################################
#BorderCanvas properties
BorderCanvas.Label.Inherited=(Heredado)

###########################################################
#BorderPreferencePage properties
BorderPreferencePage.displayname.Color=Color

BorderPreferencePage.displayname.Style=Estilo

BorderPreferencePage.displayname.Title=Borde

BorderPreferencePage.displayname.Width=Ancho

BorderPreferencePage.text.Bottom=Inferior:

BorderPreferencePage.text.Left=Izquierdo:

BorderPreferencePage.text.Right=Derecho:

BorderPreferencePage.text.Top=Superior:

###########################################################
#BordersPage properties
BordersPage.Label.Borders=&Bordes:

BordersPage.Label.Color=&Color:

BordersPage.Label.Presets=Ajustes previos:

BordersPage.Label.Style=Est&ilo:

BordersPage.Label.Width=Anch&ura:

BordersPage.Tooltip.All=Todos los bordes

BordersPage.Tooltip.Bottom=Borde inferior

BordersPage.Tooltip.Left=Borde izquierdo

BordersPage.Tooltip.None=Ninguno

BordersPage.Tooltip.Right=Borde derecho

BordersPage.Tooltip.Top=Borde superior

BordersPage.text.Preview=Vista previa:

BordersPage.Trans.SelectBorder=Seleccionar borde

BordersPage.Trans.UnSelectBorder=Descartar borde

BordersPage.Trans.SelectAllborders=Seleccionar todos los bordes

BordersPage.Trans.UnSelectAllborders=Descartar todos los bordes

###########################################################
#BoxPreferencePage properties
BoxPreferencePage.displayname.Title=Cuadro

BoxPreferencePage.text.Margin=Margen

BoxPreferencePage.text.Padding=Relleno

###########################################################
#CommentsPreferencePage properties
CommentsPreferencePage.displayname.Title=Comentarios

###########################################################
#ButtonPaneComposite properties
ButtonPaneComposite.tooltip.MasterPage=Página maestra

ButtonPaneComposite.tooltip.Normal=Normal

ButtonPaneComposite.tooltip.Ruler=Regla

###########################################################
#SettingsPage properties
SettingsPage.dataFetchSetting.label=Número &máximo de filas que recuperar de la fuente de datos:

SettingsPage.CachePreference.button.clear=&Borrar caché

SettingsPage.CachePreference.checkBox.text=Los datos de la cac&hé establecen los datos para la vista previa del informe

SettingsPage.CachePreference.description=Definir tamaño de caché:

SettingsPage.CachePreference.Filter.Tooltip=Utilizar caché en o fuera del conjunto de datos

SettingsPage.CachePreference.label.text=filas

SettingsPage.CachePreference.messageBox.info=Se ha borrado la caché

SettingsPage.CachePreference.messageBox.title=Caché borrada

SettingsPage.CachePreference.radioBox.entireNumText=Guardar en caché &todo el conjunto de datos

SettingsPage.CachePreference.radioBox.numText=Guardar en caché y &usar primero

###########################################################
#CascadingParametersDialog properties
CascadingParametersDialog.Button.isRequired=&Es necesario

CascadingParametersDialog.default.preview.string=Mi string

CascadingParametersDialog.display.controlType.comboBox=Cuadro combinado

CascadingParametersDialog.display.controlType.listBox=Cuadro de lista

CascadingParametersDialog.Label.button.changeFormat=Camb&iar...

CascadingParametersDialog.Label.button.createNew.dataset=Crear nue&vo...

CascadingParametersDialog.Label.cascadingParam.name=Nombre de grupo de pa&rámetros en cascada:

CascadingParametersDialog.Label.column.displayText=Mostrar texto

CascadingParametersDialog.Label.column.dataSet=Conjunto de datos

CascadingParametersDialog.Label.column.name=Nombre

CascadingParametersDialog.Label.column.value=Valor

CascadingParametersDialog.Label.createNewParam=<Crear nuevo parámetro...>

CascadingParametersDialog.Label.dataSets=Conjunto de d&atos:

CascadingParametersDialog.Label.dataType=T&ipo de datos:

CascadingParametersDialog.Label.defaultValue=&Valor predeterminado:

CascadingParametersDialog.Label.displayType=Mo&strar tipo:

CascadingParametersDialog.Label.formatAs=Formato como:

CascadingParametersDialog.Label.group.general=General

CascadingParametersDialog.Label.group.moreOptions=Más opciones

CascadingParametersDialog.Label.group.properties=Propiedades

CascadingParametersDialog.Label.helpText=Texto de ayu&da:

CascadingParametersDialog.Label.listLimit=&Límite de lista:

CascadingParametersDialog.Label.NoColumnAvailable=<No hay columnas disponibles>

CascadingParametersDialog.Label.param.name=&Nombre:

CascadingParametersDialog.Label.parameters=Parámetros

CascadingParametersDialog.Label.preview=Vista previa con formato

CascadingParametersDialog.Label.groupPromptText=Texto de solicit&ud:

CascadingParametersDialog.Label.parameterPromptText=Texto de solicit&ud:

CascadingParametersDialog.Label.selectDataSet=<Seleccionar un conjunto de datos>

CascadingParametersDialog.Label.selectDisplayColumn=<Columna en pantalla no definida>

CascadingParametersDialog.Label.selectValueColumn=<Columna de valores no definida>

CascadingParametersDialog.Label.values=valores

CascadingParametersDialog.Radio.Single=Conjunto de datos úni&co

CascadingParametersDialog.Radio.Mutli=Conjunto de datos m&últiple

CascadingParametersDialog.Choice.NullValue=Valor nulo

CascadingParametersDialog.Choice.BlankValue=Valor en blanco

CascadingParametersDialog.Choice.SelectValue=<Seleccionar valor...>

CascadingParametersDialog.Button.Add=&Añadir

CascadingParametersDialog.Button.Edit=Ed&itar

CascadingParametersDialog.Button.Delete=Elimi&nar

CascadingParametersDialog.Button.IsMultiple=Permi&tir valores múltiples

CascadingParametersDialog.DefalutValue.Add=Añad&ir

CascadingParametersDialog.ExpressionEditor.Title=Editar expresión

CascadingParametersDialog.Title.AddCascadingParameter=Añadir grupo de parámetros en cascada

CascadingParametersDialog.Title.EditCascadingParameter=Editar grupo de parámetros en cascada

CascadingParametersDialog.items.None=<Ninguno>

###########################################################
#CellPageGenerator properties
CellPageGenerator.List.Borders=Borde

CellPageGenerator.List.CellPadding=Relleno

CellPageGenerator.List.Font=Fuente

CellPageGenerator.List.General=General


###########################################################
#ChoiceSetFactory properties
ChoiceSetFactory.choice.Auto=Automático

ChoiceSetFactory.choice.None=Ninguno

###########################################################
#CodePageAction properties
CodePageAction.actionText.code=Script

###########################################################
#ColorBuilder properties
ColorBuilder.text.Auto=Automático

ColorBuilder.Button.ChooseColor=Abrir panel de color

ColorSelector.ColorDialog.Title=Color

ColorSelector.Name=Selector de color
###########################################################
#ColumnPageGenerator properties
ColumnPageGenerator.List.Borders=Borde

ColumnPageGenerator.List.CellPadding=Relleno de celdas

ColumnPageGenerator.List.Font=Fuente

ColumnPageGenerator.List.General=General

ColumnPageGenerator.List.Visibility=Visibilidad

###########################################################
#Commom properties
Commom.ShortDateTime.Fri=VIE

Commom.ShortDateTime.Mon=LUN

Commom.ShortDateTime.Sat=SÁB

Commom.ShortDateTime.Sun=DOM

Commom.ShortDateTime.Thu=JUE

Commom.ShortDateTime.Tue=MAR

Commom.ShortDateTime.Wed=MIÉ

###########################################################
#CopyAction properties
CopyAction.text=&Copiar

###########################################################
#CreatePlaceHolderAction properties
CreatePlaceHolderAction.text=&Crear elemento de informe de plantilla...

###########################################################
#CutAction properties
CutAction.text=Cor&tar

CutAction.trans=Cortar elementos

###########################################################
#dataBinding properties
dataBinding.label.selectBinding=Seleccionar enlace de datos:

dataBinding.message.changeDataSet=Va a cambiar el enlace de datos del elemento. \n\n¿Desea borrar toda la información del campo de datos existente?

dataBinding.message.changeReference=Está a punto de cambiar el enlace de datos del elemento.  \n\n¿Desea realizar la operación?

dataBinding.title.changeDataSet=¿Desea cambiar el conjunto de datos?

dataBinding.title=Enlace de datos

dataBinding.message.haveNoName=El elemento que ha seleccionado no tiene nombre.  \n\nRetroceda y otorgue un nombre al elemento para que éste se pueda seleccionar.

dataBinding.title.haveNoName=¿No tiene nombre?

dataBinding.button.OK=&Aceptar

parameterBinding.title=&Enlace de parámetros del conjunto de datos...

ParameterBindingDialog.title=Enlace de parámetros del conjunto de datos

dataBinding.title.generateOutputParam=¿Generar enlaces?

dataBinding.msg.generateOutputParam=El conjunto de datos tiene uno o más parámetros de salida.\n\n ¿Desea generar enlaces para ellos?

###########################################################
#DataEditPart properties
DataEditPart.Figure.Dafault=Datos

DataEditPart.Figure.Expression=expresión

DataEditPart.stackMsg.edit=Editar enlace de datos

DataEditPart.text.AggregateOn=Agregar en

###########################################################
#DataEditPart properties
DestroyEditPart.Message=El contenido de este elemento de informe está dañado. Si desea continuar trabajando en este informe, elimine primero este elemento. De lo contrario, pueden producirse más daños en el archivo de diseño de informe o el diseñador de informes puede no responder.

###########################################################
#DataPageGenerator properties
DataPageGenerator.List.Bookmark=Marcador

DataPageGenerator.List.Borders=Borde

DataPageGenerator.List.Duplicates=Duplicados

DataPageGenerator.List.Expression=Expresión

DataPageGenerator.List.Format=Formato

DataPageGenerator.List.formatDateTime=Formato de fecha/hora

DataPageGenerator.List.formatNumber=Formato de número

DataPageGenerator.List.formatString=Formato de string

DataPageGenerator.List.General=General

DataPageGenerator.List.Highlights=Resaltados

DataPageGenerator.List.HyperLink=Hyperlink

DataPageGenerator.List.Margin=Margen

DataPageGenerator.List.Padding=Relleno

DataPageGenerator.List.Section=Salto de página

DataPageGenerator.List.TOC=Tabla de contenido

DataPageGenerator.List.Visibility=Visibilidad

###########################################################
#dataset properties
dataset.edit=Editar conjunto de datos - {0}

dataset.editor.settings=Ajustes

dataset.editor.settings.dataFetchSetting.errorNumberFormat=Introduzca un número entero mayor que cero para el límite de recuperación de línea.

dataset.editor.settings.dataFetchSetting.errorNumberFormatForResultSet=Introduzca un número entero positivo como Número de conjunto del resultado

dataset.editor.settings.dataFetchSetting=Ajustes de recuperación de datos

dataset.editor.settings.resultsetselection.resultSetSelection=Selección de conjunto de resultados

dataset.editor.settings.resultsetselection.enableResultSetSelection=&Activar selección de conjunto de resultados

dataset.editor.settings.resultsetselection.selectResultSetByName=Seleccionar conjunto de &resultados por nombre:

dataset.editor.settings.resultsetselection.selectResultSetByNumber=&Seleccionar conjunto de resultados por número:

dataset.editor.settings.dataFetchSetting.fetchAll=Recuperar &todas las filas de la fuente de datos.

dataset.editor.settings.cachePreference.errorNumberFormat=Introduzca un número mayor que cero para el tamaño de la fila de caché.

dataset.editor.settings.cachePreference=Preferencia de caché

dataset.editor.computedColumns=Columnas procesadas

dataset.editor.dataSource.Tooltip=Ver o cambiar fuente de datos.

dataset.editor.dataSource=Fuente de datos

dataset.editor.error.columnOrAliasNameAlreadyUsed=El nuevo nombre o alias de columna ({0}) ya se está utilizando en la columna del índice {1}.

dataset.editor.error.computedColumnNameAlreadyUsed=El nuevo nombre de columna procesada ({0}) ya se está utilizando como alias de la columna ({1}) en Columnas de salida.

dataset.editor.error.duplicateParameterName=Nombre del parámetro duplicado.

dataset.editor.error.nonemptyDefaultvalue=El valor predeterminado no puede estar en blanco.

dataset.editor.error.emptyName=El nombre no puede estar en blanco.

dataset.editor.error.missingComputedColumnExpression=La expresión de la columna procesada {0} no puede ser nula ni estar en blanco.

dataset.editor.error.missingComputedColumnName=El nombre de columna procesada no puede ser nulo ni estar en blanco.

dataset.editor.error.noDataSource=No hay ninguna fuente de datos disponible para el conjunto de datos {0}; seleccione una fuente de datos en la entrada "fuente de datos" de la vista de propiedades.

dataset.editor.error.nonmatchedDataSource=Fuente de datos sin coincidencia para conjunto de datos "{0}", seleccione una fuente de datos que coincida con el tipo de conjunto de datos "{1}".

dataset.editor.error.noEditorsFound=No se han encontrado editores para el conjunto de datos {0}.

dataset.editor.error.noInputParameterDefaultValue=El parámetro de entrada {0} debe tener un valor predeterminado que no esté vacío.

dataset.editor.error.title=Advertencia

dataset.editor.error.unsupportedDataSet=Tipo de conjunto de datos no admitido - {0}.

dataset.editor.error.validationParameter=El parámetro de entrada [{0}] tiene un valor vacío. ¿Desea dejarlo vacío?

dataset.editor.filters=Filtros

dataset.editor.label.selectDataSource=Seleccionar &fuente de datos:

dataset.editor.outputColumns=Columnas de salida

dataset.editor.parameters=Parámetros

dataset.editor.preview=Resultados de vista previa

dataset.editor.outputparameters=Vista previa de parámetros de salida

dataset.editor.title.alias=Alias

dataset.editor.title.columnName=Nombre de la columna

dataset.editor.title.dataType=Tipo de datos

dataset.editor.title.defaultValue=Valor predeterminado

dataset.editor.title.direction=Dirección

DataSetParameterPage.editor.title.linkToSalarParameter=Vinculado al parámetro del informe

dataset.editor.title.displayName=Nombre en pantalla

dataset.editor.title.displayNameKey=Clave de nombre en pantalla

dataset.editor.title.expression=Expresión

dataset.editor.title.aggrFunc=Agregación

dataset.editor.title.aggrArgu=Arg&umento

dataset.editor.title.filter=Filtro

dataset.editor.title.helpText=Texto de ayuda

dataset.editor.title.isInput=Es de entrada

dataset.editor.title.isNullable=Permite nulo

dataset.editor.title.isOptional=Opcional

dataset.editor.title.isOutput=Es de salida

dataset.editor.title.name=Nombre

dataset.editor.title.nativeName=Nombre nativo

dataset.editor.title.operator=Operador

dataset.editor.title.type=Tipo

dataset.editor.title.value1=Valor 1

dataset.editor.title.value2=Valor 2

dataset.editor.inputDialog.displayName=Nom&bre en pantalla

dataset.editor.inputDialog.displayNameKey=&Clave de nombre en pantalla

dataset.editor.inputDialog.name=&Nombre

dataset.editor.inputDialog.nativeName=&Nombre nativo:

dataset.editor.inputDialog.type=&Tipo

dataset.editor.inputDialog.alias=&Alias

dataset.editor.inputDialog.columnName=&Nombre de columna

dataset.editor.inputDialog.dataType=&Tipo de datos

dataset.editor.inputDialog.expression=&Expresión

dataset.editor.inputDialog.aggrArgu=Arg&umento

dataset.editor.inputDialog.aggrFunc=A&gregación

dataset.editor.inputDialog.filter=&Filtro

dataset.editor.inputDialog.value1=Valor &1

dataset.editor.inputDialog.value2=Valor &2

dataset.editor.inputDialog.operator=&Operador

dataset.editor.inputDialog.defaultValue=Va&lor predeterminado

dataset.editor.inputDialog.direction=Di&rección

dataset.editor.historybar.backward=Anterior

dataset.editor.historybar.forward=Siguiente

dataset.editor.historybar.to=a

dataset.error.noDataSources=No hay ninguna fuente de datos disponible. ¿Desea agregar una fuente de datos ahora?

dataset.error.dataSourceMissing=Falta la fuente de datos de este conjunto de datos.

dataset.error.title.noDataSources=No hay fuentes de datos disponibles.

dataset.message.create=Crear un nuevo conjunto de datos.

dataset.new.defaultName=Conjunto de datos

dataset.new=Nuevo conjunto de datos

dataset.action.new=Nuevo conjunto de d&atos

dataset.join.new=Nuevo conjunto de datos de combinación

dataset.action.join.new=Nuevo &conjunto de datos de combinación

dataset.resultset.preview.tooltip=Vista previa de resultados de la consulta.

dataset.resultset.preview.promptMessage.recordsNum=Número total de registro(s) mostrado(s) {0}.

dataset.resultset.preview.promptMessage.MoreRecordsExist=Los resultados contienen más registros que no se muestran.

dataset.outputparameters.preview.tooltip=Vista previa de parámetros de salida.

dataset.wizard.label.datasetName=Nombre de conjunto de dato&s:

dataset.wizard.label.datasetType=Tipo de conjunto de dat&os:

dataset.wizard.label.dataSource=Fuente de dat&os:

dataSourceSelectionPage.title=Link con fuente de datos.

dataset.error.title.noReportDesign=Error

dataset.error.msg.noReportDesign=Diseño de informe no disponible.

dataset.warning.invalidReponseStatus=Respuesta de diseño no válida en la página personalizada ODA. Respuesta de la sesión de diseño ODA: {0}.

###########################################################
#DataSetBasePage properties
DataSetBasePage.tooltip=El nombre no puede estar vacío.

DataSetBasePage.ScriptedDataSet.name=Conjunto de datos de script

DataSetBasePage.ScriptedDataSource.name=Fuente de datos en script

DataSetBasePage.Group.DataSourceSelection=Selección de fuente de datos

DataSetBasePage.CassandraScriptedDataSet.name=Conjunto de datos en script Cassandra

DataSetBasePage.CassandraScriptedDataSource.name=Fuente de datos en script Cassandra

DataSetBindingSelector.Button.DeselectAll=&Anular la selección de todo

DataSetBindingSelector.Button.SelectAll=&Seleccionar todo

DataSetBindingSelector.Combo.DataSet=&Conjunto de datos:

DataSetBindingSelector.Label.SelectBindingColumns=Seleccione las columnas de enlace.

DataSetBindingSelectorPage.Label.SelectBindingColumns=Seleccione las columnas de enlace.

DataSetBindingSelectorPage.Title=Enlace del conjunto de datos

CassandraScriptedDataSource.display.name=Fuente de datos en script Cassandra

CassandraScriptedDataSource.error.classNotFound=No es posible encontrar los archivos de la librería del cliente Hector para admitir la fuente de datos en script Cassandra.

###########################################################
#JointDataSetPage properties
JointDataSetPage.pageName=Conjunto de datos de combinación

JointDataSetPage.page.detail=Seleccione un conjunto de datos y una columna de cada lado para definir una combinación.

JointDataSetPage.button.innerJoin=Combinación &interna

JointDataSetPage.button.leftJoin=Combinación externa &izquierda

JointDataSetPage.button.rightJoin=Combinación externa &derecha

JointDataSetPage.button.fullJoin=&Combinación externa completa

JointDataSetPage.joinType=Tipos de combinación

JointDataSetPage.error.nodataset.title=No se ha encontrado ningún conjunto de datos.

JointDataSetPage.error.nodataset.right=No se ha encontrado el conjunto de datos derecho.

JointDataSetPage.error.nodataset.left=No se ha encontrado el conjunto de datos izquierdo.

JointDataSetPage.pageDescription=Definir conjunto de datos de combinación

JointDataSetPage.query=Consulta

###########################################################
#DerivedDataSet properties
DerivedDataSet.action.error.message.NoDataSet=No se ha encontrado ningún conjunto de datos disponible.

DerivedDataSet.action.error.title.createDerivedDataSet=Error al crear conjunto de datos derivados

DerivedDataSet.action.error.message.InvalidNumber.DataSet=El número de conjuntos de datos disponibles es inferior a dos.

###########################################################
#DataSetComputedColumnsPage properties
DataSetComputedColumnsPage.ComputedColumns.Tooltip=Definir columnas procesadas.

DataSetComputedColumnsPage.description=Definir columnas procesadas:

DataSetComputedColumnsPage.duplicatedName=Nombre de columna duplicado.

DataSetComputedColumnsPage.numberName=El nombre de columna no puede ser un número.

DataSetComputedColumnsPage.toolTipText.Down=Mover columna procesada hacia abajo.

DataSetComputedColumnsPage.toolTipText.Up=Mover columna procesada hacia arriba.

DataSetComputedColumnsPage.toolTipText.Remove=Quitar columna procesada.

DataSetComputedColumnsPage.toolTipText.New=Nueva columna procesada.

DataSetComputedColumnsPage.toolTipText.Edit=Editar columna procesada.

DataSetComputedColumnsPage.InputDialog.title.New=Nueva columna procesada

DataSetComputedColumnsPage.InputDialog.title.Edit=Editar columna procesada

DataSetComputedColumnsPage.InputDialog.message.error.AggregationExpression=La expresión de agregación no puede incluir una columna procesada de agregación.

DataSetComputedColumnsPage.title.ComputedColumnInputDialog=Columna procesada

###########################################################
#DataSetFiltersPage properties
DataSetFiltersPage.description=Definir filtros:

DataSetFiltersPage.exception.msg.operator.notSupport=No se admite este operador:

DataSetFiltersPage.Filter.Tooltip=Filtrar filas de datos utilizando expresiones BIRT.

DataSetFiltersPage.toolTipText.Down=Mover filtro hacia abajo.

DataSetFiltersPage.toolTipText.Up=Mover filtro hacia arriba.

DataSetFiltersPage.toolTipText.Remove=Quitar filtro.

DataSetFiltersPage.toolTipText.New=Nuevo filtro.

DataSetFiltersPage.toolTipText.Edit=Editar filtro.

DataSetFiltersPage.title.FilterInputDialog=Filtro

DataSetFiltersPage.messages.selectVauleUnavailable=Lista de valores no disponible con la expresión indicada.

DataSetFiltersPage.DynamicFilter.DynamicParameter.NotFound=El parámetro dinámico vinculado "{0}" del filtro dinámico no existe.

###########################################################
#DataSetNodeProvider properties
DataSetNodeProvider.menu.text=&Editar

###########################################################
#DataSetParametersPage properties
DataSetParametersPage.description=Definir parámetros de entrada:

DataSetParametersPage.Filter.Tooltip=Pasar parámetros hasta o desde el conjunto de datos.

DataSetParameterBindingInputDialog.Title=Editar enlace de parámetros del conjunto de datos

DataSetParametersPage.ErrorTitle.InvalidInput=Entrada no válida.

DataSetParameterBindingInputDialog.Label.Name=Nombre del parámetro:

DataSetParametersPage.ErrorMessage.CanootBeNull=El valor no puede ser nulo.

DataSetParameterBindingInputDialog.Title.NewParameter=Nuevo parámetro

DataSetParameterBindingInputDialog.Title.EditParameter=Editar parámetro

DataSetParameterBindingInputDialog.Label.DataType=Tipo de datos:

DataSetParameterBindingInputDialog.Label.Value=&Valor:

DataSetParametersPage.ParameterButton.Tooltip.Edit=Editar parámetro de informe

DataSetParametersPage.ParameterButton.Tooltip.New=Nuevo parámetro de informe

DataSetParametersPage.Prompt.new=<Nuevo parámetro aquí...>

DataSetParametersPage.default.None=N/A
DataSetParametersPage.reportParam.None=Ninguno

DataSetParametersPage.errorMessage.LinkedReportParamNotFound=El parámetro del informe vinculado "{0}" del parámetro del conjunto de datos "{1}" ya no existe.

DataSetParametersPage.errorMessage.InvalidType.LinkedReportParam=Si un usuario configura múltiples valores para el parámetro de informe, el parámetro del conjunto de datos utiliza sólo el primer valor.

DataSetParameterPage.cell.linkToSalarParameter=&Vinculado a parámetro de informe

DataSetParameterPage.title.ParameterInputDialog=Parámetro

DataSetParameterPage.toolTipText.New=Nuevo parámetro

DataSetParameterPage.toolTipText.Edit=Editar parámetro

DataSetParameterPage.toolTipText.Remove=Quitar parámetro

DataSetParameterPage.toolTipText.Up=Mover parámetro hacia arriba

DataSetParameterPage.toolTipText.Down=Mover parámetro hacia abajo

DataSetParameterPage.WarningMessage.updatePosition=Algunos o todos los parámetros de conjunto de datos tienen valores de posición no válidos. Actualizando la posiciones según la secuencia.

DataSetParameterPage.warning.UnmatchedParamDataType=El tipo de parámetro del conjunto de datos no coincide con el tipo de parámetro del informe vinculado.

DataSetParameterPage.multiValuesType=Tipo de valores múltiples

DataSetParameterPage.warningLinkedMultipleValuesParams.title=Diálogo de advertencia

DataSetParameterPage.updateReportParameter.title=Ajuste de actualización de parámetro de informe

DataSetParameterPage.updateReportParameter.message=¿Desea actualizar el parámetro del informe vinculado con los ajustes de los parámetros del conjunto de datos?

DataSetParameterPage.updateReportParameter.promptButtonYes=Sí

DataSetParameterPage.updateReportParameter.promptButtonNo=No

DataSetParameterPage.updateReportParameter.propmtText=Recordar mi decisión

PromptParameterDialog.title=Confirmar atributos de diseño de actualización

PromptParameterDialog.okButton.text=Aplicar

PromptParameterDialog.parameterGroup.title=Atributos de diseño de parámetro

PromptParameterDialog.parameterGroup.label=Actualizar los parámetros seleccionados con los valores de atributo revisados por el diseño del conjunto de datos editado.

PromptParameterDialog.parameterGroup.nameColumn=Nombre

PromptParameterDialog.parameterGroup.previousValue=Valor anterior

PromptParameterDialog.parameterGroup.revisedValue=Valor revisado

###########################################################
#PropertyBindingpage properties
PropertyBindingPage.datasource.property.empty=La extensión ODA de esta fuente de datos no ha definido ninguna propiedad pública.

PropertyBindingPage.dataset.property.empty=La extensión ODA de este conjunto de datos no ha definido ninguna propiedad pública.

PropertyBindingPage.dataset.queryText=Texto de consult&a

PropertyBindingPage.property.tooltip=Definir enlace de propiedades para tiempo de ejecución.

PropertyBindingPage.button.tooltip=Abrir el compilador de expresiones para introducir valor de {0}.

PropertyBindingPage.label.colon=:
PropertyBindingPage.label.LeftParenthesis=(
PropertyBindingPage.label.RightParenthesis=)

###########################################################
#DataSetProcess properties
DataSetProcess.action.Delete=Eliminar conjunto de datos

DataSetProcess.action.Edit=Editar conjunto de datos

DataSetProcess.action.Preview=Vista previa

DataSetProcess.text.DisplayName=Conjunto de datos

DataSetProcess.text.Prefix=Los siguientes conjuntos de datos utilizan esta fuente de datos:

DataSetProcess.text.Show=Muestra la página del editor de conjunto de datos para el conjunto de datos.

DataSetProcess.text.Suffix=¿Seguro que desea eliminar este elemento?

###########################################################
#datasource properties
datasource.action.edit=&Editar

datasource.edit=Editar fuente de datos - {0}

datasource.new.defaultName=Fuente de Datos

datasource.new=Nueva fuente de datos

datasource.action.new=Nueva &fuente de datos

datasource.wizard.label.dataSourceName=Nombre de fuente de dato&s:

datasource.wizard.label.datasources=Crear a partir de un tipo de fuente de &datos de la siguiente lista

datasource.wizard.label.connectionfile=Crear a partir de un perfil de &conexión del almacenamiento de perfiles

datasource.wizard.message.selectType=Facilite la configuración de la nueva fuente de datos o seleccione un perfil de conexión predeterminado.

datasource.wizard.title.select=Seleccione un tipo de fuente de datos o un perfil de conexión.

datasource.editor.property=Enlace de propiedades

datasource.editor.duplicatedName=El nombre de fuente de datos "{0}" está duplicado.

datasource.error.title.noReportDesign=Error

datasource.error.msg.noReportDesign=Diseño de informe no disponible.

datasource.exprotToCP.title=Exportar

datasource.exportToCP.message=Está exportando "{0}" al explorador de fuentes de datos.

datasource.exportToCP.checkBox=&Crear almacenamiento de perfil de conexión para la fuente de datos exportada.

datasource.exportToCP.externalCheckBox=&Utilizar el perfil de conexión externalizado de la fuente de datos.

datasource.exportToCP.label.specifyName=Nombre del perfil de con&exión:

datasource.exportToCP.errorBox.button.OK=Aceptar

datasource.exportToCP.error.emptyFileName=Debe especificar un nombre de perfil de conexión que no esté vacío.

datasource.exportToCP.error.invalidFileName=El nombre del perfil de conexión especificado incluye caracteres no válidos.

datasource.exportToCP.error.duplicatedFileName=El nombre del perfil de conexión ya existe.

datasource.exportToCP.error.alreadyExported=La fuente de datos "{0}" ya ha externalizado sus propiedades.


###########################################################
#DataSourceProcess properties
DataSourceProcess.action.Delete=Eliminar fuente de datos

DataSourceProcess.action.Edit=Editar fuente de datos

DataSourceProcess.text.DisplayName=DataSource
DataSourceProcess.text.Prefix=Los siguientes conjuntos de datos utilizan esta fuente de datos

DataSourceProcess.text.Show=Muestra la página del editor de fuente de datos

DataSourceProcess.text.Suffix=¿Seguro que desea eliminar este elemento?

###########################################################
#DataSourceSelectionPage properties
DataSourceSelectionPage.ScriptDataSource.DisplayName=Fuente de datos de script

###########################################################
#DataSourcesSlotOutlineProcess properties
DataSourcesSlotOutlineProcess.action.New=Nueva fuente de datos

###########################################################
#datatypes properties
datatypes.any=Cualquiera

datatypes.auto=Automático

datatypes.dateTime=Fecha/hora

datatypes.decimal=Decimal

datatypes.float=Flotante

datatypes.integer=Entero

datatypes.string=String

###########################################################
#DataView properties
DataView.defaultText.noDataView=No está disponible ninguna vista de datos.

###########################################################
#DataViewTreeViewerPage properties
DataViewTreeViewerPage.tooltip.DoubleClickToEdit=Hacer doble clic para editar

DataViewTreeViewerPage.tooltip.DragToInsertColumn=Arrastrar para insertar columna

DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns=Arrastrar para insertar todas las columnas del conjunto de datos.

DataViewTreeViewerPage.tooltip.DragToInsertParameter=Arrastrar para insertar parámetro de informe;

###########################################################
#DebugUtil properties
DebugUtil.importProject.fail="Error al exportar proyecto existente."

###########################################################
#DecorationFieldEditor properties
DecorationFieldEditor.text.Linethrough=Tachado

DecorationFieldEditor.text.Overline=Línea superior

DecorationFieldEditor.text.Underline=Subrayado

###########################################################
#DefaultNodeProvider properties
DefaultNodeProvider.Dlg.Confirm=Haga clic en Sí para borrar el elemento {0}. De lo contrario, haga clic en No

DefaultNodeProvider.ParameterGroup.ConfirmMessage=Va a eliminar un grupo de parámetros. Si elimina el grupo de parámetros, eliminará todos los parámetros incluidos en el grupo. ¿Desea continuar?

DefaultNodeProvider.ParameterGroup.ConfirmTitle=Confirmar eliminación de grupo de parámetros

DefaultNodeProvider.Tree.Body=Texto

DefaultNodeProvider.Tree.Clients=Los siguientes elementos usan {0}

DefaultNodeProvider.Tree.ColumnHedings=Encabezados de columna

DefaultNodeProvider.Tree.Components = Elementos de informe

DefaultNodeProvider.Tree.DataSets=Conjuntos de datos

DefaultNodeProvider.Tree.DataSources=Fuentes de datos

DefaultNodeProvider.Tree.Detail=Detalle

DefaultNodeProvider.Tree.Footer=Pie de página

DefaultNodeProvider.Tree.Groups=Grupos

DefaultNodeProvider.Tree.Header=Encabezado

DefaultNodeProvider.Tree.Images=Imágenes incrustadas

DefaultNodeProvider.Tree.Invalid=Nodo no válido

DefaultNodeProvider.Tree.MasterPages=MasterPages
DefaultNodeProvider.Tree.PageSetup=Configurar página

DefaultNodeProvider.Tree.Parameters=Parámetros de informe

DefaultNodeProvider.Tree.Reference=Advertencia

DefaultNodeProvider.Tree.Scratch=Bloc de notas

DefaultNodeProvider.Tree.Styles=Estilos

DefaultNodeProvider.Tree.Themes=Temas

DefaultNodeProvider.WarningDialog.Title=Advertencia

DefaultNodeProvider.WarningDialog.EmptyList=Sin elemento disponible para insertar.

DefaultNodeProvider.stackMsg.changeBinding=Cambiar enlace de datos

###########################################################
#DeleteAction properties
DeleteAction.text=&Eliminar

DeleteAction.trans=Eliminar elementos

###########################################################
#DeleteColumnAction properties
DeleteColumnAction.actionMsg.delete=&Eliminar

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.actionMsg.deleteGroup=Eliminar &grupo

###########################################################
#DeleteListGroupAction properties
DeleteListGroupAction.actionMsg.deleteGroup=Eliminar &grupo

###########################################################
#DeleteRowAction properties
DeleteRowAction.actionMsg.delete=&Eliminar

###########################################################
#designer properties
designer.preview.browserTitle=Vista previa

designer.preview.preference.browser.autoSave=Guardar automáticamente antes de la vista previa

designer.preview.preference.browser.browse=&Examinar...

designer.preview.preference.browser.currentBrowsers=Adaptadores de explorador web externo actua&les:

designer.preview.preference.browser.description=La selección determina los exploradores web utilizados para mostrar la vista previa de los informes BIRT.\nSi sólo se ha configurado un adaptador de explorador, la selección no\npuede cambiarse.

designer.preview.preference.browser.details=Seleccionar un programa de explorador

designer.preview.preference.browser.program=Comando de explorador personali&zado:

designer.preview.preference.browser.svg=Activar diagrama &SVG

designer.preview.preference.browser.extbrowser.link=Consulte <a>''{0}''</a> para configurar el explorador externo utilizado para mostrar informes.

designer.preview.preference.masterpagecontent=Activar contenido de página &maestra

designer.preview.preference.appcontextkey=&Usar contexto de aplicación personalizado

designer.preview.preference.browser.useExternal=Usar siempre explora&dores externos

designer.preview.preference.elementname.columnwidth=125

designer.preview.preference.elementname.customname.Enabled=true

designer.preview.preference.elementname.customname=Nombre predeterminado

designer.preview.preference.elementname.defaultname.Enabled=false

designer.preview.preference.elementname.defaultname=Nombre de elemento:

designer.preview.preference.elementname.description.Enabled=false

designer.preview.preference.elementname.description=Descripción

designer.preview.preference.elementname.Enabled=true

designer.preview.preference.locale.description=Elegir configuración &regional:

designer.preview.preference.timezone.description=&Zona horaria:

designer.preview.preference.resultset.sqldatasetpage.group.title=Página de consulta

designer.preview.preference.resultset.previewpage.group.title=Página de vista previa

designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting=Ajuste de límites de fila de vista previa

designer.preview.preference.resultset.NoLimitOfDispalyRow=&No hay límite de número de filas que mostrar.

designer.preview.preference.resultset.maxNoOfSchema.description=Número máximo de e&squemas que mostrar:

designer.preview.preference.resultset.maxNoOfTable.description=Número máximo de &tablas de cada esquema que mostrar:

designer.preview.preference.resultset.maxNoOfSchema.errormessage=Introduzca un número entre 0 y {0} para el número máximo de esquemas.

designer.preview.preference.resultset.maxNoOfTable.errormessage=Introduzca un número entre 0 y {0} para el número máximo de tablas de cada esquema.

designer.preview.preference.resultset.maxrow.description=Número de &filas para mostrar:

designer.preview.preference.resultset.maxrow.errormessage=Introduzca un número entre 1 y {0} para el número máximo de filas.

designer.preview.preference.resultset.maxrowlevelmember.description=Recuento de miembros de cada &nivel de fila para mostrar:

designer.preview.preference.resultset.maxrowlevelmember.errormessage=Introduzca un número entre 1 y {0} para el número máximo de miembros por nivel de fila.

designer.preview.preference.resultset.maxcolumnlevelmember.description=Recuento de miembros de cada &nivel de columna para mostrar:

designer.preview.preference.resultset.maxcolumnlevelmember.errormessage=Introduzca un número entre 1 y {0} para el número máximo de miembros por nivel de columna.

designer.preview.preference.resultset.maxinmemorycubesize.description=Tamaño del cubo de memoria (MB):

designer.preview.preference.resultset.maxinmemorycubesize.errormessage=Introduzca un número entre 1 y {0} para el tamaño máximo del cubo de memoria.

designer.preview.preference.server.description=Configuración del servidor de aplicaciones interno. El frame de vista previa de BIRT Report Designer\nutiliza el servidor internamente.

designer.preview.preference.server.hostDescription=&Nombre del anfitrión o dirección IP de equipo local. Deje en blanco, se\ndetermina automáticamente.

designer.preview.preference.server.note=Nota:

designer.preview.preference.server.portDescription=&Puerto de comunicación del servidor. Establezca en 0 para asignación dinámica.

designer.preview.preference.server.requireRestart=Si el servidor ya está en ejecución, estas preferencias sólo serán efectivas\ndespués de reiniciar.

designer.preview.preference.bidiOrientation.label=&Orientación bidimensional:

designer.preview.preference.bidiOrientation.auto=Automático

designer.preview.preference.bidiOrientation.ltr=De izquierda a derecha

designer.preview.preference.bidiOrientation.rtl=De derecha a izquierda

designer.preview.preference.updateParameter.confirmMessage=Confirmar actualización

designer.preview.preference.updateParameter.confirmButton=&Solicitar siempre confirmación antes de sincronizar las propiedades de parámetro del conjunto de datos con las propiedades de parámetro ODA si se cambia la consulta.

designer.preview.preference.updateReportParameter.confirmButton=&No solicitar confirmación para actualizar un parámetro del informe vinculado.

designer.preview.preference.updateReportParameter.confirmOption.always=Sie&mpre:

designer.preview.preference.updateReportParameter.confirmOption.update=&Actualizar

designer.preview.preference.updateReportParameter.confirmOption.ignore=&Omitir

designer.preview.previewaction.label.webviewer=Ver informe en Web Viewer

designer.preview.previewaction.label=Ver informe como {0}

designer.preview.previewaction.ErrorMessage=La generación de PDF no está activada ya que no se encuentra iText o no está instalado.

designer.preview.previewaction.ErrorMessage.title=Advertencia

designer.preview.run.webviewer=En Web Viewer

designer.preview.run=Como {0}

###########################################################
#DesignerActionBarContributor properties
DesignerActionBarContributor.element.group=Insertar &grupo

DesignerActionBarContributor.element.InsertColumnLeft=Insertar columnas a la &izquierda

DesignerActionBarContributor.element.InsertColumnRight=Insertar columnas a la &derecha

DesignerActionBarContributor.element.InsertRowAbove=Insertar fila a&rriba

DesignerActionBarContributor.element.InsertRowBelow=Insertar fila a&bajo

DesignerActionBarContributor.element.merge=&Combinar celdas

DesignerActionBarContributor.element.split=&Dividir celda

designerActionBarContributor.menu.data-newdataset=Nuevo c&onjunto de datos

designerActionBarContributor.menu.data-newdatasource=Nueva f&uente de datos

DesignerActionBarContributor.menu.data=&Datos

DesignerActionBarContributor.menu.element-EditGroup=&Editar grupo

DesignerActionBarContributor.menu.element-LayoutMode=Modo de di&seño

DesignerActionBarContributor.menu.element-showRuler=Mostrar &reglas

DesignerActionBarContributor.menu.element.editDataBinding=Editar &enlace de datos...

DesignerActionBarContributor.menu.element=Ele&mento

DesignerActionBarContributor.menu.format=Formato

DesignerActionBarContributor.menu.insert=&Insertar

DesignerActionBarContributor.menu.page=Pá&gina

DesignerActionBarContributor.menu.style-rule-apply=A&plicar estilo

DesignerActionBarContributor.menu.style-rule-edit=E&ditar estilo

DesignerActionBarContributor.menu.style-rule-remove=&Quitar estilo

DesignerActionBarContributor.style.new=&Nuevo estilo...

DesignerActionBarContributor.menu.data-NewParameter=Nuevo &parámetro de informe

DesignerActionBarContributor.menu.data-NewDataSetParent=Nuevo conjunto de d&atos

designerActionBarContributor.menu.data-newDataset=Nuevo c&onjunto de datos

designerActionBarContributor.menu.data-newJointDataset=Nuevo &conjunto de datos de combinación

DesignerActionBarContributor.menu.element.insert=&Insertar

DesignerActionBarContributor.menu.element.group=&Grupo

DesignerActionBarContributor.menu.element.style=Est&ilo

DesignerActionBarContributor.menu.zoomCombo.tooltip=Nivel de zoom
###########################################################
#DesignerOutlinePage properties
DesignerOutlinePage.action.Delete=Eliminar selecciones

DesignerOutlinePage.action.Hide=Ocultar encabezado y pie de página

DesignerOutlinePage.action.Redo=Rehacer

DesignerOutlinePage.action.Undo=Deshacer

DesignerOutlinePage.menu.Filters=Filtros

###########################################################
#Content outline page switch action
ContentOutlinePage.action.text.reportOutline=Cambiar a contorno de fuente XML

ContentOutlinePage.action.text.reportXMLSourceOutline=Cambiar a contorno de informe

###########################################################
#DesignerPaletteFactory properties
DesignerPaletteFactory.categoryName.reportItems=Elementos de informe

DesignerPaletteFactory.elementLabel.data=Datos

DesignerPaletteFactory.elementLabel.grid=Cuadrícula

DesignerPaletteFactory.elementLabel.image=Imagen

DesignerPaletteFactory.elementLabel.label=Etiqueta

DesignerPaletteFactory.elementLabel.list=Lista

DesignerPaletteFactory.elementLabel.tabel=Tabla

DesignerPaletteFactory.elementLabel.text=Texto

DesignerPaletteFactory.elementName.data=Datos

DesignerPaletteFactory.elementName.grid=Cuadrícula

DesignerPaletteFactory.elementName.image=Imagen

DesignerPaletteFactory.elementName.label=Etiqueta

DesignerPaletteFactory.elementName.list=Lista

DesignerPaletteFactory.elementName.table=Tabla

DesignerPaletteFactory.elementName.text=Texto

DesignerPaletteFactory.elementName.textdata=Texto dinámico

DesignerPaletteFactory.toolTip.dataReportItem=Insertar una columna de conjunto de datos o un resultado de expresión.

DesignerPaletteFactory.toolTip.gridReportItem=Insertar un diseño de columna/fila para organizar de forma visual otros elementos.

DesignerPaletteFactory.toolTip.imageReportItem=Insertar una imagen de un archivo o URL.

DesignerPaletteFactory.toolTip.labelReportItem=Insertar un texto estático.

DesignerPaletteFactory.toolTip.listReportItem=Insertar la presentación de formato flexible de las filas de conjunto de datos en las bandas de encabezado/detalle/pie de página

DesignerPaletteFactory.toolTip.multiLineDataReportItem=Insertar un bloque de texto (texto sin formato/HTML/RTF) de un archivo, columna de conjunto de datos o resultado de expresión.

DesignerPaletteFactory.toolTip.tableReportItem=Insertar la presentación de columna de las filas de conjunto de datos en las bandas de encabezado/detalle/pie de página

DesignerPaletteFactory.toolTip.textDataReportItem=Insertar un bloque de texto con expresiones de datos CLOB opcionales

DesignerPaletteFactory.toolTip.textReportItem=Insertar un bloque de texto (texto sin formato/HTML) con expresiones opcionales en línea.

DesignerPaletteFactory.quicktool.title=Herramientas rápidas

DesignerPaletteFactory.quicktool.agg.title=Agregación

DesignerPaletteFactory.quicktool.agg.toolTip=Insertar elemento de agregación

DesignerPaletteFactory.quicktool.timeperiod.title=Período de tiempo relativo

DesignerPaletteFactory.quicktool.timeperiod.toolTip=Insertar elemento TimePeriod

###########################################################
#DimensionBuilderDialog properties
DimensionBuilderDialog.LabelMeasure=&Medida:

DimensionBuilderDialog.LabelUnit=&Unidades:

DimensionBuilderDialog.Title=Dimension Builder

###########################################################
#DNDUtil properties
DNDUtil.trans.copy=Copiar elementos

DNDUtil.trans.cut=Mover elementos

###########################################################
#DuplicatesPage properties
DuplicatesPage.Check.HideDuplicates=Ocultar duplicados

DuplicatesPage.Group.Repeat=Duplicación (Repetir)

DuplicatesPage.Group.RepeatonPage=Duplicación (Repetir en página)

DuplicatesPage.Label.RepeatInfo=Ocultar valores que sean iguales que la fila de datos anterior.

DuplicatesPage.Label.RepeatOnPage.1=Ocultar valores que sean iguales que la fila anterior,

DuplicatesPage.Label.RepeatOnPage.2=\\ pero mostrar siempre el valor en la parte superior de cada página nueva.

DuplicatesPage.Label.ResetonGroup=Restablecer grupo siguiente

DuplicatesPage.Radio.Repeat=Repetir

DuplicatesPage.Radio.RepeatonPage=Repetir en página

###########################################################
#EditAction properties
EditAction.text=&Editar

###########################################################
#EditGroupAction properties
EditGroupAction.stackMsg.editGroup=Editar grupo

###########################################################
#EditGroupAction properties
InsertGroupHeaderFooterAction.stackMsg.editGroup=Insertar encabezado/pie de página de grupo

InsertGroupHeaderFooterAction.Text.Header=Encabezado

InsertGroupHeaderFooterAction.Text.Footer=Pie de página

###########################################################
#DeleteGroupAction properties
DeleteGroupAction.stackMsg.deleteGroup=Eliminar grupo

###########################################################
#EditLabelAction properties
EditLabelAction.text=&Editar

EditorDragGuidePolicy.pixels.label=píxeles

EditorGuide.bottom.label=Inferior:

EditorGuide.left.label=Izquierdo:

EditorGuide.right.label=Derecho:

EditorGuide.top.label=Superior:

###########################################################
#Element properties
Element.AbstractScalarParameter.dataType=Tipo de &datos:

Element.AutoText.type=Tipo:

Element.Cell.colSpan=Extensión de columna:

Element.Cell.column=Columna:

Element.Cell.drop=&Suprimir:

Element.Cell.height=Altura:

Element.Cell.rowSpan=Extensión de fila:

Element.Cell.slot.content=Contenido:

Element.Cell.verticalAlign=Alineación vertical:

Element.Cell.width=Ancho:

Element.Column.alignment=Alineación:

Element.Column.repeat=Repetir:

Element.Column.width=&Anchura:

Element.Column.suppressDuplicates=Suprimir duplicados

Element.Container.slot.reportItems=Elementos de informe:

Element.Data.action=Acción:

Element.Data.helpText=Texto de ayuda:

Element.Data.helpTextID=Clave de texto de ayuda

Element.TextData.valueExpr=Expresión de val&or:

Element.DataSet.afterClose.toolTip=Llamado justo después de cerrar el conjunto de datos.

Element.DataSet.afterClose=Después de cerrar:

Element.DataSet.afterOpen.toolTip=Llamado justo después de abrir el conjunto de datos.

Element.DataSet.afterOpen=Después de abrir:

Element.DataSet.beforeClose.toolTip=Llamado justo antes de cerrar el conjunto de datos.

Element.DataSet.beforeClose=Antes de cerrar:

Element.DataSet.beforeOpen.toolTip=Llamado justo antes de abrir el conjunto de datos.

Element.DataSet.beforeOpen=Antes de abrir:

Element.DataSet.cachedMetaData=Metadatos en caché:

Element.DataSet.columnHints=Sugerencias de columna:

Element.DataSet.computedColumns=Columnas procesadas:

Element.DataSet.dataSource=Fuente de datos:

Element.DataSet.filter=Filtro:

Element.DataSet.inputParameters=Parámetros de entrada:

Element.DataSet.onFetch.toolTip=Llamado justo antes de recuperar una fila de datos del conjunto de datos.

Element.DataSet.onFetch=En recuperación:

Element.DataSet.outputParameters=Parámetros de salida:

Element.DataSet.paramBindings=Enlaces de parámetro:

Element.DataSet.resultSet=Conjunto de resultados:

Element.DataSource.afterClose.toolTip=Llamado justo después de cerrar la fuente de datos.

Element.DataSource.afterClose=Después de cerrar:

Element.DataSource.afterOpen.toolTip=Llamado justo después de abrir la fuente de datos.

Element.DataSource.afterOpen=Después de abrir:

Element.DataSource.beforeClose.toolTip=Llamado justo antes de cerrar la fuente de datos.

Element.DataSource.beforeClose=Antes de cerrar:

Element.DataSource.beforeOpen.toolTip=Llamado justo antes de abrir la fuente de datos.

Element.DataSource.beforeOpen=Antes de abrir:

Element.ExtendedItem.extension=nombre de extensión:

Element.ExtendedItem.extensionName=nombre de extensión:

Element.FreeForm.slot.reportItems=Elementos de informe:

Element.GraphicMasterPage.columns=Columnas:

Element.GraphicMasterPage.columnSpacing=Espacio de columnas:

Element.GraphicMasterPage.slot.contents=Contenido:

Element.Grid.slot.columns=columnas

Element.Grid.slot.row=fila:

Element.Image.action=Acción:

Element.Image.altText=Texto al&ternativo:

Element.Image.altTextID=Clave de texto alternativa:

Element.Image.helpText=Texto de ayuda:

Element.Image.helpTextID=Clave de texto de ayuda

Element.Image.imageName=Nombre de imagen:

Element.Image.scale=escala:

Element.Image.size=tamaño:

Element.Image.source=Fuente:

Element.Image.typeExpr=Expresión de tipo:

Element.Image.uri=uri:
Element.Image.valueExpr=Expresión de valor:

Element.JdbcDataSource.dataSource=Fuente de datos:

Element.JdbcDataSource.driver=Controlador

Element.JdbcDataSource.password=Contraseña:

Element.JdbcDataSource.url=Url:
Element.JdbcDataSource.userName=Nombre de usuario:

Element.JdbcSelectDataSet.getStatement.toolTip=Devuelve una sentencia SQL personalizada para la consulta.

Element.JdbcSelectDataSet.getStatement=Obtener sentencia:

Element.JdbcSelectDataSet.statement=Sentencia:

Element.Label.action=Acción:

Element.Label.helpText=Texto de ayuda:

Element.Label.helpTextID=Clave de texto de ayuda

Element.Label.text=Texto:

Element.Label.textID=&Clave de texto:

Element.Line.orientation=orientación:

Element.List.slot.columnHeader=columnHeader:
Element.List.slot.detail=detalle:

Element.List.slot.footer=pie de página:

Element.List.slot.group=grupo:

Element.List.slot.header=encabezado:

Element.ListGroup.slot.footer=pie de página:

Element.ListGroup.slot.header=encabezado:

Element.Listing.filter=Filtro:

Element.Listing.onFinish.toolTip=Llamado después de leer la última fila del conjunto de datos para este elemento, pero antes de crear la banda de pie de página.

Element.Listing.onFinish=En fin:

Element.Listing.onRow.toolTip=Llamado para cada fila recuperada del conjunto de datos para este elemento, pero antes de crear cualquier contenido para dicha fila.

Element.Listing.onRow=En fila:

Element.Listing.onStart.toolTip=Llamado después de abrir el conjunto de datos, pero antes de crear la banda de encabezado y antes de recuperar la primera fila del conjunto de datos para este elemento.

Element.Listing.onStart=En inicio:

Element.Listing.pageBreakInterval=Intervalo de salto de págin&a:

Element.Listing.sorting=Ordenar:

Element.ListingGroup.filter=Filtro:

Element.ListingGroup.groupName=Nombre

Element.ListingGroup.interval=Intervalo:

Element.ListingGroup.intervalRange=Rango del intervalo:

Element.ListingGroup.keyExpr=Expresión clave:

Element.ListingGroup.onFinish.toolTip=Llamado después de leer la última fila del conjunto de datos para este elemento, pero antes de crear la banda de pie de página.

Element.ListingGroup.onFinish=En fin:

Element.ListingGroup.onRow.toolTip=Llamado para cada fila recuperada del conjunto de datos para este elemento, pero antes de crear cualquier contenido para dicha fila.

Element.ListingGroup.onRow=En fila:

Element.ListingGroup.onStart.toolTip=Llamado después de abrir el conjunto de datos pero antes de crear la banda de encabezado y antes de recuperar la primera fila del conjunto de datos para este elemento.

Element.ListingGroup.onStart=En inicio:

Element.ListingGroup.pageBreakAfter=Salto de página después:

Element.ListingGroup.pageBreakBefore=Salto de página antes:

Element.ListingGroup.sort=Ordenar:

Element.ListingGroup.sortDirection=Dirección de orden:

Element.ListingGroup.toc=Tabla de contenido:

Element.MasterPage.bottomMargin=Margen in&ferior:

Element.MasterPage.columns=Columnas:

Element.MasterPage.columnSpacing=&Separación:

Element.MasterPage.height=Alt&ura:

Element.MasterPage.leftMargin=Margen &izquierdo:

Element.MasterPage.margin=Margen:

Element.MasterPage.orientation=O&rientación:

Element.MasterPage.rightMargin=Margen &derecho:

Element.MasterPage.slot.contents=Contenido:

Element.MasterPage.topMargin=Margen &superior:

Element.MasterPage.type=T&ipo:

Element.MasterPage.width=&Ancho:

Element.Module.author=&Autor:

Element.Module.base=Base:

Element.Module.colorPalette=Paleta de colores:

Element.Module.comments=&Comentarios:

Element.Module.configVars=Configurar variables:

Element.Module.createdBy=C&reado por:

Element.Module.description=D&escripción:

Element.Module.descriptionID=Clave de descripción:

Element.Module.helpGuide=Guía de ayuda:

Element.Module.images=Imágenes:

Element.Module.includeLibraries=Incluir bibliotecas:

Element.Module.includeResource=A&rchivo de recursos:

Element.Module.includeScripts=Incluir scripts:

Element.Module.initialize.toolTip=Llamado al iniciar la ejecución del informe.

Element.Module.initialize=Inicializar:

Element.LayoutModule.theme=T&emas

Element.Module.title=T&ítulo:

Element.Module.titleID=Clave de título:

Element.Module.units=Unid&ades:

Element.MultiLineData.contentTypeExpr=Expresión de tipo de contenido:

Element.MultiLineData.valueExpr=Expresión de valor:

Element.OdaDataSet.privateDriverDesignState=Estado de diseño de controlador privado:

Element.OdaDataSet.privateDriverProperties=Propiedades de controlador privadas:

Element.OdaDataSet.publicDriverProperties=Propiedades de controlador públicas:

Element.OdaDataSet.queryScript.toolTip=TBD:

Element.OdaDataSet.queryScript=Script:

Element.OdaDataSet.queryText=Consulta:

Element.OdaDataSet.resultSetName=Nombre de conjunto de resultados:

Element.OdaDataSet.type=Tipo:

Element.OdaDataSource.driverName=Nombre de controlador:

Element.OdaDataSource.privateDriverProperties=Propiedades de controlador privadas:

Element.OdaDataSource.publicDriverProperties=Propiedades de controlador públicas:

Element.Parameter.helpText=Texto de ayuda:

Element.Parameter.helpTextID=Clave de texto de a&yuda:

Element.Parameter.hidden=Oculto:

Element.Parameter.promptTextID=Clave de texto de &solicitud:

Element.ParameterGroup.helpText=Texto de ayuda:

Element.ParameterGroup.helpTextID=Clave de texto de a&yuda:

Element.ParameterGroup.slot.parameters=Parámetros:

Element.ParameterGroup.startExpanded=Iniciar expandido:

Element.ParameterGroup.promptTextID=Clave de texto de &solicitud:

Element.ReportDesign.afterCloseDoc.toolTip=Llamado justo después de cerrar el documento de informe en la fase de generación.

Element.ReportDesign.layoutPreference=Preferencia de diseño:

Element.ReportDesign.afterCloseDoc=Después de cerrar documento:

Element.ReportDesign.afterFactory.toolTip=Llamado al final de la fase de generación, después de cerrar el documento de informes (si existe). Éste es el último método llamado en la fase de generación.

Element.ReportDesign.afterFactory=Después de fábrica:

Element.ReportDesign.afterOpenDoc.toolTip=Llamado justo después de abrir el documento de informe en la fase de generación.

Element.ReportDesign.afterOpenDoc=Después de abrir documento:

Element.ReportDesign.afterRender.toolTip=Llamado después de iniciar una acción de hora de presentación.

Element.ReportDesign.afterRender=Después de procesamiento:

Element.ReportDesign.beforeCloseDoc.toolTip=Llamado justo antes de cerrar el documento de informe en la fase de generación.

Element.ReportDesign.beforeCloseDoc=Antes de cerrar documento:

Element.ReportDesign.beforeFactory.toolTip=Llamado al inicio de la fase de generación, después del método de inicialización ( ) y antes de abrir el documento de informe (si lo hay).

Element.ReportDesign.beforeFactory=Antes de fábrica:

Element.ReportDesign.beforeOpenDoc.toolTip=Llamado justo antes de abrir el documento de informe en la fase de generación.

Element.ReportDesign.beforeOpenDoc=Antes de abrir el documento:

Element.ReportDesign.beforeRender.toolTip=Llamado antes de iniciar una acción de hora de presentación.

Element.ReportDesign.beforeRender=Antes de procesamiento:

Element.ReportDesign.displayName=N&ombre en pantalla:

Element.ReportDesign.eventHandlerClass=&Clase de controlador de evento:

Element.ReportDesign.iconFile=Miniatura:

Element.ReportDesign.refreshRate=Intervalo de actualización:

Element.ReportDesign.bidiLayoutOrientation=&Orientación del informe:

Element.ReportElement.comments=&Comentarios:

Element.ReportElement.Cube=&Datos:

Element.ReportElement.customXml=XML personalizado:

Element.ReportElement.displayName=&Nombre en pantalla:

Element.ReportElement.displayNameID=Clave de nombre en pantalla:

Element.ReportElement.extends=Extends:

Element.ReportElement.javaClass=Clase Java:

Element.ReportElement.name=&Nombre:

Element.ReportElement.propertyMask=Máscara de propiedad:

Element.ReportElement.style=Est&ilo:

Element.ReportItem.bookmark=&Marcador:

Element.ReportItem.dataSet=Conjunto de datos:

Element.ReportItem.height=&Altura:

Element.ReportItem.onCreate.toolTip=Ejecutado al crear el elemento en la fase de generación.

Element.ReportItem.onCreate=Al crear:

Element.ReportItem.onRender.toolTip=Ejecutado al preparar el elemento para procesamiento en la fase de presentación.

Element.ReportItem.onRender=Al procesar:

Element.ReportItem.paramBindings=Enlaces de parámetro:

Element.ReportItem.toc=&Tabla de contenido:

Element.ReportItem.visibility=Visibilidad:

Element.ReportItem.width=&Anchura:

Element.ReportItem.x=X:

Element.ReportItem.y=Y:

Element.ReportItem.altText=Texto alternativo

Element.ReportItem.altTextID=Clave de texto alternativo

Element.Row.bookmark=Marcador:

Element.Row.height=A&ltura:

Element.Row.slot.cell=celda:

Element.Row.visibility=Visibilidad:

Element.ScalarParameter.alignment=Alineación:

Element.ScalarParameter.allowBlank=Permitir en blanco:

Element.ScalarParameter.allowNull=Permitir nulo:

Element.ScalarParameter.concealValue=Ocultar valor:

Element.ScalarParameter.controlType=Tipo de &control:

Element.ScalarParameter.defaultValue=Valor predeterminado:

Element.ScalarParameter.dynamicList.dataSetName=Nombre del conjunto de datos:

Element.ScalarParameter.dynamicList.labelExpr=Expresión de etiqueta:

Element.ScalarParameter.dynamicList.valueExpr=Expresión de valor:

Element.ScalarParameter.dynamicList=Lista dinámica:

Element.ScalarParameter.format=Formato:

Element.ScalarParameter.selectionList.fixedOrder=Orden fijo:

Element.ScalarParameter.selectionList.mustMatch=Debe coincidir:

Element.ScalarParameter.selectionList.selectionList=Lista de selección:

Element.ScalarParameter.selectionList=Lista de selección:

Element.ScalarParameter.promptTextID=Clave de texto de &solicitud:

Element.ScriptDataSet.close.toolTip=Cierra el conjunto de datos.

Element.ScriptDataSet.close=Cerrar:

Element.ScriptDataSet.describe.toolTip=Describe el conjunto de resultados.

Element.ScriptDataSet.describe=Describir:

Element.ScriptDataSet.fetch.toolTip=Crea, aplica y devuelve un objeto de tipo de fila de datos.

Element.ScriptDataSet.fetch=Recuperar:

Element.ScriptDataSet.open.toolTip=Abre el conjunto de datos.

Element.ScriptDataSet.open=Abrir:

Element.ScriptDataSource.close.toolTip=Suprime la conexión con la fuente de datos.

Element.ScriptDataSource.close=Cerrar:

Element.ScriptDataSource.open.toolTip=Establece una conexión con la fuente de datos.

Element.ScriptDataSource.open=Abrir:

Element.SimpleMasterPage.floatingFooter=&Pie de página flotante

Element.SimpleMasterPage.footerHeight=Al&tura de pie de página:

Element.SimpleMasterPage.headerHeight=&Altura de encabezado:

Element.SimpleMasterPage.showFooterOnLast=Mostrar &pie de página al final

Element.SimpleMasterPage.showHeaderOnFirst=Mostrar e&ncabezado al principio

Element.SimpleMasterPage.slot.pageFooter=Pie de página:

Element.SimpleMasterPage.slot.pageHeader=Encabezado de página:

Element.Style.background.attachment=Ar&chivo adjunto:

Element.Style.background.color=&Color de fondo:

Element.Style.background.image=Imagen de &fondo:

Element.Style.background.positionX=Posición &horizontal:

Element.Style.background.positionY=Posición &vertical:

Element.Style.background.repeat=&Repetir:

Element.Style.background=Fondo:

Element.Style.border.bottom.color=&Abajo:

Element.Style.border.bottom.style=&Inferior:

Element.Style.border.bottom.width=Inferior&:

Element.Style.border.fillColor=Color de relleno de borde:

Element.Style.border.hmiddle.color=Color medio horizontal de borde:

Element.Style.border.hmiddle.style=Estilo medio horizontal de borde:

Element.Style.border.hmiddle.width=Ancho medio horizontal de borde:

Element.Style.border.left.color=Iz&quierda:

Element.Style.border.left.style=&Izquierda:

Element.Style.border.left.width=Izqui&erda:

Element.Style.border.right.color=Derec&ha:

Element.Style.border.right.style=&Derecha:

Element.Style.border.right.width=De&recha:

Element.Style.border.top.color=A&rriba:

Element.Style.border.top.style=&Superior:

Element.Style.border.top.width=Arri&ba:

Element.Style.border.vmiddle.color=Color medio vertical de borde:

Element.Style.border.vmiddle.style=Estilo medio vertical de borde:

Element.Style.border.vmiddle.width=Ancho medio vertical de borde:

Element.Style.border=Borde:

Element.Style.box=Cuadro:

Element.Style.canShrink=Se puede &reducir

Element.Style.color=&Color:

Element.Style.display=Vi&sualizar:

Element.Style.font.family=&Fuente:

Element.Style.font.size=Tamañ&o:

Element.Style.font.style=E&stilo:

Element.Style.font.textLineThrough=&Tachado

Element.Style.font.textOverline=&Línea superior

Element.Style.font.textUnderline=&Subrayado

Element.Style.font.variant=Variante de fuente:

Element.Style.font.weight=&Grosor:

Element.Style.font=&Fuente:

Element.Style.format.dateTime=Fecha/hora:

Element.Style.format.number.align=Alineación de número de formato:

Element.Style.format.number=Número:

Element.Style.format.string=String:

Element.Style.format=Formato:

Element.Style.highlight.rules=Resaltar reglas:

Element.Style.highlight.testExpr=Resaltar expresión de prueba:

Element.Style.letterSpacing=Separación de &letras:

Element.Style.lineHeight=Altura de lí&nea:

Element.Style.map.rules=Asignar reglas:

Element.Style.map.testExpr=Asignar expresión de prueba:

Element.Style.margin.bottom=&Inferior:

Element.Style.margin.left=Izqui&erda:

Element.Style.margin.right=De&recha:

Element.Style.margin.top=Su&perior:

Element.Style.masterPage=&Página maestra:

Element.Style.orphans=&Huérfanas:

Element.Style.padding.bottom=&Inferior:

Element.Style.padding.left=&Izquierda:

Element.Style.padding.right=&Derecha:

Element.Style.padding.top=&Superior:

Element.Style.padding=Relleno:

Element.Style.pageBreakAfter=&Después:

Element.Style.pageBreakBefore=&Antes:

Element.Style.pageBreakInside=&Interior:

Element.Style.section=Sección:

Element.Style.showIfBlank=Mostrar si en &blanco

Element.Style.text.align=A&lineación de texto:

Element.Style.width=&Anchura:

Element.Style.height=A&ltura:
#bidi_hcg
Element.Style.text.direction=Dirección &bidimensional del texto:

Element.Style.text.indent=Sangrado de text&o:

Element.Style.text.transform=Transfo&rmación de texto:

Element.Style.text=Texto:

Element.Style.verticalAlign=&Alineación vertical:

Element.Style.whiteSpace=Espac&io en blanco:

Element.Style.widows=Viuda&s:

Element.Style.wordSpacing=&Separación de palabras:

Element.Table.caption=Título:

Element.Table.captionID=Clave de título:

Element.Table.repeatHeader=Repetir encabezado:

Element.Table.slot.columnHeader=columnHeader:
Element.Table.slot.columns=columnas

Element.Table.slot.detail=detalle:

Element.Table.slot.footer=pie de página:

Element.Table.slot.group=grupo:

Element.Table.slot.header=encabezado:

Element.TableGroup.slot.footer=pie de página:

Element.TableGroup.slot.header=encabezado:

Element.Text.content=Contenido:

Element.Text.contentID=Clave de conteni&do:

Element.Text.contentType=Tipo de conte&nido:

Element.TemplateElement.name=&Nombre

Element.Toc.Sytle=Est&ilo

Element.VariableElement.name=&Nombre:

Element.VariableElement.type=&Tipo:

Element.VariableElement.value=&Valor:

###########################################################
#EmbeddedImageNodeProvider properties
EmbeddedImageNodeProvider.action.New=Nueva &imagen incrustada

###########################################################
#EmbeddedImagesNodeProvider properties
EmbeddedImagesNodeProvider.action.New=Nueva &imagen incrustada

###########################################################
#ElementIdDescriptorProvider properties
ElementIdDescriptorProvider.Display.Element.ID=&ID del elemento:

###########################################################
#EmptyEditPart properties
EmptyEditPart.Msg.EmptyLib=La librería no contiene elementos visuales. Para añadir elementos visuales, arrastre el elemento desde la paleta a la vista de contorno de la librería o utilice el menú de inserción de la vista de contorno.

EmptyEditPart.Msg.NonVisual=Se ha seleccionado un elemento no visual.

###########################################################
#error properties
error.duplicateName=El nuevo nombre duplica un nombre existente en el mismo espacio de nombre.

error.DataSource.emptyName=El campo nombre de la fuente de datos no puede estar vacío.

error.DataSet.emptyName=El campo nombre del conjunto de datos no puede estar vacío.

error.OutputColumn.emptyName=El campo nombre de la columna de salida no puede estar vacío.

error.invalidName=El nombre "{0}" no es válido, ya que contiene uno de los siguientes caracteres ".", "/", "\\", "!", ";", "," .

###########################################################
#ExceptionHandler properties
ExceptionHandler.Label.ErrorCode=Código de error

ExceptionHandler.Label.ErrorMessage=Mensaje de error

ExceptionHandler.Label.PluginId=ID de complemento:

ExceptionHandler.Label.PluginName=Nombre de complemento:

ExceptionHandler.Label.PluginProvider=Proveedor de complemento:

ExceptionHandler.Label.PluginVersion=Versión:

ExceptionHandler.Label.UnknownPlugin=Complemento desconocido

ExceptionHandler.Meesage.UnexceptedExceptionOccur=Se ha producido una excepción interna inesperada. Vea el registro para obtener más información.

ExceptionHandler.Message.BirtExceptionOccur=Se ha producido una excepción BIRT.

ExceptionHandler.Message.CausedBy=Causado por {0}.

ExceptionHandler.Message.FileNotFound=No se ha encontrado el archivo:

ExceptionHandler.Message.OutOfMemory=No hay suficiente memoria.

ExceptionHandler.Message.PartInitError=Error en la inicialización parcial.

ExceptionHandler.Message.SemanticFail=Se ha producido un error en la comprobación.

ExceptionHandler.Message.UnexceptedExceptionOccur=Se ha producido una excepción interna. Consulte el archivo de registro para obtener más información.

ExceptionHandler.Message.UnknownHost=Anfitrión desconocido:

ExceptionHandler.Title.Error=Error

ExceptionHandler.Title.FileNotFound=No se ha encontrado el archivo.

ExceptionHandler.Title.PartInitError=Error en la inicialización parcial.

ExceptionHandler.Title.UnknownHost=Anfitrión desconocido

ExceptionHandler.Meesage.ExceptionOccur=Se ha producido una excepción inesperada. Vea el registro para obtener más detalles.

###########################################################
#ExportAction properties
ExportAction.actionMsg.export=Exportar

###########################################################
#ExportToLibraryAction properties
ExportToLibraryAction.action.text=E&xportar a librería...

ExportToLibraryAction.Button.Cancel=Cancelar

ExportToLibraryAction.Button.No=No

ExportToLibraryAction.Button.Yes=Sí

ExportToLibraryAction.Dialog.Message=\nLa librería de destino debe tener el mismo elemento de diseño. ¿Desea sobrescribirlo?

ExportToLibraryAction.Dialog.Title=Exportar elemento

ExportToLibraryAction.Message.RememberDecision=&Recordar mi decisión


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.action.text=E&xportar a librería...

ExportToCPSouceAction.action.text=Externalizar a p&erfil de conexión...


###########################################################
#LibraryExplorerTreeViewPage tooltips
LibraryElementsToolHandleExtends_message=El elemento contiene enlaces locales sin conjunto de datos explícito especificado, lo que puede provocar un bajo rendimiento en el tiempo de ejecución. ¿Desea mover los enlaces locales al principal para disponer de un mejor rendimiento?

LibraryElementsToolHandleExtends_question=¿Mover los enlaces locales al principal para disponer de un mejor rendimiento?

LibraryElementsToolHandleExtends_toggle=&Recordar mi decisión

LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline=Arrastrar y colocar en la vista de contorno

LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout=Arrastrar y colocar en el editor de diseños

LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout=Arrastrar y colocar en la vista de contorno o en el editor de diseños

###########################################################
#ExpressionBuidler properties
ExpressionBuidler.Dialog.Title=Compilador de expresiones (JavaScript)

ExpressionBuidler.Title=Compilador de expresiones

ExpressionBuilder.tooltip.Sort=Orden

ExpressionBuilder.toolTipText.Button=Abrir el compilador de expresiones

###########################################################
#ExpressionBuilder properties
ExpressionBuilder.Label.Category=&Categoría:

ExpressionBuilder.Label.Functions=Doble clic para &insertar:

ExpressionBuilder.Label.Hint=Sugerencia

ExpressionBuilder.Label.Operators=Operadores:

ExpressionBuilder.Label.SubCategory=&Subcategoría:

ExpressionBuilder.Message.Prompt=Introduzca una expresión en el campo Expresión. Busque la lista de objetos disponibles y haga doble clic para copiarla en la expresión.

ExpressionBuilder.ToolTip=Abrir el compilador de expresiones

ExpressionBuilder.toolTipText.validate=Validar

ExpressionBuilder.Script.NoError=No se ha encontrado ningún error de sintaxis en el script actual.

ExpressionBuilder.Script.Error=[Fila:{0}, Columna:{1}] {2}

ExpressionBuilder.Script.Warning=Advertencia

ExpressionBuilder.Script.Confirm=\nSe ha encontrado un error de sintaxis en el script actual. ¿Desea guardarlo?

ExpressionBuilder.toolTipText.calendar=Calendario

ExpressionBuilder.Calendar.Button.OK=Aceptar

ExpressionButtonProvider.Constant=Constante

ExpressionButtonProvider.Javascript=Sintaxis JavaScript

###########################################################
#ExpressionPropertyDescriptor properties
ExpressionPropertyDescriptor.text.Edit=Compilador de ex&presiones...

###########################################################
#ExpressionProvider properties
ExpressionProvider.Category.Context=Contexto

ExpressionProvider.Category.BirtObjects=Funciones de BIRT

ExpressionProvider.Category.DataSets=Conjuntos de datos disponibles

ExpressionProvider.Category.NativeObjects=Funciones de objetos nativos de JavaScript

ExpressionProvider.Category.ColumnBinding=Enlaces de columna disponibles

ExpressionProvider.Category.Operators=Operadores

ExpressionProvider.Category.Parameters=Parámetros de informe

ExpressionProvider.Category.DataCubes=Cubo de datos actual

ExpressionProvider.Category.Variables=Variables

ExpressionProvider.Label.All=---Todos---

ExpressionProvider.Operator.Add=Añade dos valores juntos.

ExpressionProvider.Operator.AddTo=Acceso directo para añadir al valor actual.

ExpressionProvider.Operator.And=Consulta dos expresiones y devuelve un valor true si ambas expresiones son verdaderas.

ExpressionProvider.Operator.Assign=Establece un valor igual que otro.

ExpressionProvider.Operator.BitAnd=A & B: Devuelve el resultado del operador de bits AND de A y B.

ExpressionProvider.Operator.BitOr=A | B: Devuelve el resultado del operador de bits OR de A y B.

ExpressionProvider.Operator.Dec=Disminuye el valor y, a continuación, devuelve el resultado.

ExpressionProvider.Operator.DividingFrom=Acceso directo para dividir el valor actual.

ExpressionProvider.Tooltip.ColumnBinding=Hace referencia a

ExpressionProvider.Operator.Divides=Divide el valor de la izquierda por el de la derecha y devuelve el resultado.

ExpressionProvider.Operator.Equals=Devuelve un valor True si los elementos son los mismos.

ExpressionProvider.Operator.Greater=A > B: devuelve un valor True si A es mayor que B.

ExpressionProvider.Operator.GreaterEquals=A >= B: devuelve un valor True si A es mayor o igual que B.

ExpressionProvider.Operator.Inc=Aumenta el valor y, a continuación, devuelve el resultado.

ExpressionProvider.Operator.LeftBracket=Paréntesis izquierdo

ExpressionProvider.Operator.RightBracket=Paréntesis derecho

ExpressionProvider.Operator.Less=A < B: devuelve un valor True si A es menor que B.

ExpressionProvider.Operator.LessEqual=A <= B: devuelve un valor True si A es menor o igual que B.

ExpressionProvider.Operator.Mult=Multiplica dos valores.

ExpressionProvider.Operator.MultTo=Acceso directo para multiplicar el valor actual.

ExpressionProvider.Operator.Not=!A: Devuelve un valor true si A es false o false si A es true.

ExpressionProvider.Operator.NotEqual=A <> B: devuelve un valor True si A no es igual que B.

ExpressionProvider.Operator.Or=Consulta dos expresiones y devuelve un valor true si una de las expresiones es verdadera.

ExpressionProvider.Operator.ReturnDec=Devuelve el valor y, a continuación, disminuye el valor.

ExpressionProvider.Operator.ReturnInc=Devuelve el valor y, a continuación, aumenta el valor.

ExpressionProvider.Operator.Sub=Resta un valor de otro.

ExpressionProvider.Operator.SubFrom=Acceso directo para restar del valor actual.

ExpressionProvider.Operators.Assignment=Asignación

ExpressionProvider.Operators.Comparison=Comparación

ExpressionProvider.Operators.Computational=Computacional

ExpressionProvider.Operators.Logical=Lógica

ExpressionProvider.EditBindings=Editar enlaces...

ExpressionProvider.Expression.RowNumName=RowNum

ExpressionProvider.Expression.RowNumTooltip=Expresión que devuelve el número actual de filas.

ExpressionProvider.Variables=Variables

ExpressionProvider.Variables.Report=Variables de informe

ExpressionProvider.Variables.Page=Variables de página

ExpressionTreeSupport.Category.Global=Global

###########################################################
#ExpressionValueCellEditor properties
ExpressionValueCellEditor.buildExpressionAction=<Compilar expresión...>

ExpressionValueCellEditor.selectValueAction=<Seleccionar valor...>

ExpressionValueCellEditor.title=Seleccionar valor

###########################################################
#ExtendedEditPart properties
ExtendedEditPart.edit=Editar {0}

###########################################################
#ExtendedItemPageGenerator properties
ExtendedItemPageGenerator.List.Bookmark=Marcador

ExtendedItemPageGenerator.List.TOC=Tabla de contenido

###########################################################
#FilterHandleProvider properties
FilterHandleProvider.ColumnName.Value=Valor

FilterHandleProvider.Label.Filterby=Filtrar por:

FilterHandleProvider.Modify.SaveValues=Guardar valores

###########################################################
#filters properties
filters.empty.columnName=La expresión no puede estar vacía.

filters.error.title=Entrada no válida.

filters.operators.any=es cualquiera

filters.operators.between=Está entre

filters.operators.bottomN=(n) elementos inferiores

filters.operators.bottomPercent=(n) porcentaje inferior

filters.operators.equal=es igual que

filters.operators.false=es False

filters.operators.greaterThan=es mayor que

filters.operators.greaterThanEqual=es mayor o igual que

filters.operators.lessThan=es menor que

filters.operators.lessThanEqual=es menor o igual que

filters.operators.like=es como

filters.operators.notBetween=no está entre

filters.operators.notEqual=no es igual que

filters.operators.notNull=no es nulo

filters.operators.null=es nulo

filters.operators.topN=son (n) elementos superiores

filters.operators.topPercent=es (n) porcentaje superior

filters.operators.true=es True

filters.prompt.new=<Nuevo filtro aquí...>

###########################################################
#FixTableLayoutCalculator properties
FixTableLayoutCalculator.Error.CannotParse=No se puede analizar el valor de entrada.

###########################################################
#FlowMoveChildCommand properties
FlowMoveChildCommand.transLabel.moveElement=Mover elemento

###########################################################
#FontPreferencePage properties
FontPreferencePage.displayname.Title=Fuente

FontPreferencePage.label.fontDecoration=Decoración:

FontPreferencePage.text.Preview=Vista previa de muestra

FontPreferencePage.text.PreviewContent=AaBbYyZz

###########################################################
#FontSizeBuilder properties
FontSizeBuilder.displayname.Auto=Automático

###########################################################
#FormatBuilder properties
FormatAdapter.DisplayName.None=Automático

FormatBuilder.Title=Compilador de formatos

###########################################################
#FormatDateTimeAttributePage properties
FormatDateTimeAttributePage.Trans.SetDateTimeFormat = Establezca un formato de fecha/hora.

###########################################################
#FormatDateTimePage properties
FormatDateTimePage.label.custom.settings.label=Formatos de ejem&plo (haga clic en uno para copiarlo y, a continuación, modifíquelo):

FormatDateTimePage.label.custom.settings=Ajustes personalizados

FormatDateTimePage.label.format.code=Códi&go de formato:

FormatDateTimePage.label.format.page=&Formato como:

FormatDateTimePage.label.format.locale=&Configuración regional:

FormatDateTimePage.label.general.preview.group=Vista previa con formato

###########################################################
# LOCALE SPECIAL:
# The datetime pattern string in this text should be formatted locale-dependent if needed
FormatDateTimePage.label.guide.format=dd/MM/yyyy hh:mm:ss a

FormatDateTimePage.label.guide.text=Introducir como: dd/MM/yyyy hh:mm:ss AM/PM

FormatDateTimePage.label.preview.dateTime=Fecha/hora para &vista previa:

FormatDateTimePage.label.preview.group=Vista previa

FormatDateTimePage.label.preview.label=Vista previa con formato:

FormatDateTimePage.label.table.column.format.name=Formato de ejemplo

FormatDateTimePage.label.table.column.format.result=Resultado de formato de ejemplo

FormatDateTimePage.label.table.column.format.code=Código de ejemplo

FormatDateTimePage.preview.invalid.dateTime=Fecha/hora no válida para vista previa

FormatDateTimePage.preview.invalid.formatCode=Código de formato no válido

###########################################################
#FormatDateTimePreferencePage properties
FormatDateTimePreferencePage.formatDateTime.title=Formato de fecha/hora

###########################################################
#FormatNumberAttributePage properties
FormatNumberAttributePage.Trans.SetNumberFormat = Establezca un formato de número.

###########################################################
#FormatNumberPage properties
FormatNumberPage.currency.symbol.none=Sin símbolo

FormatNumberPage.label.currency.settings=Ajustes de moneda

FormatNumberPage.label.custom.preview.group=Vista previa

FormatNumberPage.label.custom.preview.label=Vista previa con formato:

FormatNumberPage.label.custom.settings.lable=Formatos de ejem&plo (haga clic en uno para copiarlo y, a continuación, modifíquelo):

FormatNumberPage.label.custom.settings=Ajustes personalizados

FormatNumberPage.label.decimal.places=Nú&mero de decimales:

FormatNumberPage.label.rounding.mode=&Modo de redondeo:

FormatNumberPage.label.fixed.settings=Ajustes fijos

FormatNumberPage.label.format.code=Códi&go de formato:

FormatNumberPage.label.format.number.page=&Formato como:

FormatNumberPage.label.format.number.locale=&Configuración regional:

FormatNumberPage.label.general.preview.group=Vista previa con formato

FormatNumberPage.label.negative.numbers=&Números negativos:

FormatNumberPage.label.percent.settings=Ajustes de porcentaje

FormatNumberPage.label.preview.number=Número para vis&ta previa:

FormatNumberPage.label.scientific.settings=Ajustes científicos

FormatNumberPage.label.symbol.position=Pos&ición del símbolo:

FormatNumberPage.label.symbol=Sím&bolo:

FormatNumberPage.label.table.column.format.code=Formato de ejemplo

FormatNumberPage.label.table.column.format.result=Resultado de formato de ejemplo

FormatNumberPage.label.use1000sSeparator=&Usar separador de millares

FormatNumberPage.label.useLeadingZero=Usar cero inicial

FormatNumberPage.label.useSymbolSpace=&Usar símbolo de espacio

FormatNumberPage.preview.invalidFormatCode=Código de formato no válido

FormatNumberPage.preview.invalidNumber=Número no válido para vista previa

FormatNumberPage.symblePos.after=Después

FormatNumberPage.symblePos.before=Antes

FormatNumberPage.roundingMode.halfUp=Al alza

FormatNumberPage.roundingMode.halfDown=A la baja

FormatNumberPage.roundingMode.halfEven=A la mitad

FormatNumberPage.roundingMode.up=Arriba

FormatNumberPage.roundingMode.down=Abajo

FormatNumberPage.roundingMode.ceiling=Máximo

FormatNumberPage.roundingMode.floor=Mínimo

FormatNumberPage.roundingMode.unnecessary=Innecesario

###########################################################
#FormatNumberPreferencePage properties
FormatNumberPreferencePage.formatNumber.title=Formato de número

###########################################################
#FormatPreferencePage properties
FormatPreferencePage.displayname.Title=Formato

###########################################################
#FormatStringAttributePage properties
FormatStringAttributePage.Trans.SetStringFormat = Establezca un formato de cadena.

###########################################################
#FormatStringPage properties
FormatStringPage.default.preview.text=Mi string

FormatStringPage.label.custom.preview.label=Vista previa con formato:

FormatStringPage.label.custom.settings.label=Formatos de ejem&plo (haga clic en uno para copiarlo y, a continuación, modifíquelo):

FormatStringPage.label.customSettings=Ajustes personalizados

FormatStringPage.label.format.code=Códi&go de formato:

FormatStringPage.label.formatStringAs=&Formato como:

FormatStringPage.label.locale=&Configuración regional:

FormatStringPage.label.preview.group=Vista previa

FormatStringPage.label.preview.string=St&ring para vista previa:

FormatStringPage.label.previewWithFormat=Vista previa con formato

FormatStringPage.label.table.collumn.exampleFormatCode=Formato de ejemplo

FormatStringPage.label.table.collumn.exampleFormatResult=Resultado de formato de ejemplo

FormatStringPage.previewText.invalidFormatCode=Código de formato no válido

###########################################################
#FormatStringPreferencePage properties
FormatStringPreferencePage.formatString.title=Formato de string

###########################################################
#FormPage properties
FormPage.Button.Add=&Añadir

FormPage.Button.AddWithDialog=&Añadir...

FormPage.Button.AddWithDialog.PROP=&Añadir archivo...

FormPage.Button.AddWithDialog.JAR=Añadir archi&vo...

FormPage.Button.AddWithDialog.JS=Añadir archivo&...

FormPage.Button.Add.AggregateOn=Añadir a&gregación

FormPage.Button.AddWithDialog.AggregateOn=Añadir a&gregación...

FormPage.Button.Delete=&Quitar

FormPage.Button.DeleteFile=&Eliminar archivo

FormPage.Button.DeleteFile.Alt1=Eliminar arch&ivo

FormPage.Button.DeleteFile.Alt2=Eliminar &archivo

FormPage.Button.Down=Mover hacia abaj&o

FormPage.Button.Down.Alt1=Mover hacia aba&jo

FormPage.Button.Down.Alt2=Mover hacia &abajo

FormPage.Button.Edit=Ed&itar

FormPage.Button.EditWithDialog=E&ditar...

FormPage.Button.Up=Mover hacia &arriba

FormPage.Button.Up.Alt1=Mover hacia a&rriba

FormPage.Button.Up.Alt2=Mov&er hacia arriba

FormPage.Button.Binding.Refresh=Ac&tualizar

FormPage.Button.Binding.Clear=&Borrar

FormPage.Menu.ModifyProperty=Modificar propiedad

FormPage.toolTipText.Up=Mover hacia arriba

FormPage.toolTipText.Down=Mover hacia abajo
###########################################################
#GeneralInsertMenuAction properties
GeneralInsertMenuAction.text.Data=&Datos

GeneralInsertMenuAction.text.DynamicText=Tex&to dinámico

GeneralInsertMenuAction.text.Grid=&Cuadrícula

GeneralInsertMenuAction.text.Image=&Imagen

GeneralInsertMenuAction.text.Label=&Etiqueta

GeneralInsertMenuAction.text.List=&Lista

GeneralInsertMenuAction.text.Table=Ta&bla

GeneralInsertMenuAction.text.Text=&Texto

###########################################################
#GeneralPreferencePage properties
GeneralPreferencePage.errorMsg.duplicate.styleName=Nombre de estilo duplicado.

GeneralPreferencePage.label.styleNameDuplicate=El nombre de estilo "{0}" ya existe, elija un nombre diferente

GeneralPreferencePage.label.styleNamePredefind=El nombre de estilo "{0}" está predefinido, elija un nombre diferente

GeneralPreferencePage.label.customStyle=&Estilo personalizado

GeneralPreferencePage.Label.CustomStyle=Nombre:

GeneralPreferencePage.Label.Note=NOTA: * Aplique las propiedades de estilo a un tipo de elemento de informe específico.

GeneralPreferencePage.Label.PreDefinedStyle=Seleccionar elemento de informe:

GeneralPreferencePage.label.predefinedStyle=Estilo &predefinido *

GeneralPreferencePage.label.nameEmpty=No se permite que el nombre de estilo esté vacío.

###########################################################
#GridEditPart properties
GraphicalEditorWithFlyoutPalette_Redo.Text0=&Rehacer

GraphicalEditorWithFlyoutPalette_Redo.Text1=&Rehacer {0}

GraphicalEditorWithFlyoutPalette_Redo.ToolTip0=Rehacer

GraphicalEditorWithFlyoutPalette_Redo.ToolTip1=Rehacer {0}

GraphicalEditorWithFlyoutPalette_Undo.Text0=&Deshacer

GraphicalEditorWithFlyoutPalette_Undo.Text1=&Deshacer {0}

GraphicalEditorWithFlyoutPalette_Undo.ToolTip0=Deshacer

GraphicalEditorWithFlyoutPalette_Undo.ToolTip1=Deshacer {0}

GridEditPart.GUIDEHANDLE_TEXT = Cuadrícula

###########################################################
#GridFigure properties
GridFigure.BORDER_TEXT=Cuadrícula

###########################################################
#GridPageGenerator properties
GridPageGenerator.List.Bookmark=Marcador

GridPageGenerator.List.Borders=Borde

GridPageGenerator.List.Font=Fuente

GridPageGenerator.List.General=General

GridPageGenerator.List.Margin=Margen

GridPageGenerator.List.Section=Salto de página

GridPageGenerator.List.TOC=Tabla de contenido

GridPageGenerator.List.Visibility=Visibilidad

###########################################################
#GridProvider properties
GridProvider.action.text=In&sertar fila

###########################################################
#GroupDialog properties
GroupDialog.Dialog.GroupDetail=Detalles de grupo

GroupDialog.Label.Bookmark=&Marcador:

GroupDialog.Dialog.TOCGroup=Tabla de contenido

GroupDialog.Dialog.TOC=Expresión de elemen&to:

GroupDialog.Dialog.TOCStyle=Est&ilo:

GroupDialog.DialogTitle.NoData=Error en la inicialización

GroupDialog.DialogTitle.SemanticCheckFailed=Se ha producido un error en la comprobación semántica

GroupDialog.ErrorMesaage.IllegalGroup=Este grupo no es válido.

GroupDialog.ErrorMessage.NoData=No hay ninguna fila de datos disponible.

GroupDialog.Label.FilterSorting=Filtros y orden

GroupDialog.Label.GroupOn=&Agrupar en:

GroupDialog.Label.HeaderFooter=Encabezado y pie de página

GroupDialog.Label.IncludeFooter=Incluir pie de &grupo

GroupDialog.Label.IncludeHeader=Incluir &encabezado de grupo

GroupDialog.Label.Interval=I&ntervalo:

GroupDialog.PageBreakBefore=Ante&rior:

GroupDialog.PageBreakAfter=P&osterior:

GroupDialog.PageBreakInside=&Interior:

GroupDialog.Label.IntervalBase=Utilizar un valor de base fi&jo para el intervalo:

GroupDialog.Label.Name=No&mbre:

GroupDialog.Label.Range=Int&ervalo:

GroupDialog.Tab.Binding=Enlaces de datos

GroupDialog.Tab.Filter=Filtros

GroupDialog.Tab.Sorting=Ordenar

GroupDialog.RepeatHeader=Re&petir encabezado

GroupDialog.Title.Edit=Editar grupo

GroupDialog.Title.New=Nuevo grupo

GroupDialog.PageBreak=Salto de página

GroupDialog.buttion.HideDetail=&Ocultar detalle

GroupDialog.toolTipText.openExprButton=Abrir el compilador de expresiones

GroupDialog.Button.Ascending=A&scendente

GroupDialog.Button.Descending=Des&cendente
###########################################################
#GroupHandleProvider properties
GroupHandleProvider.Label.Groups=Lista de grupo:

###########################################################
#GroupProvider properties
GroupProvider.action.text=&Editar grupo

###########################################################
#GuiExtensionManager properties
GuiExtensionManager.label.name=Elemento ampliado

GuiExtensionManager.tooltip.insert=Insertar {0}

###########################################################
#HighlightHandleProvider properties
HighlightHandleProvider.text.Preview=AaBbCcYyZz  (tamaño de fuente =

###########################################################
#HighlightRuleBuilderDialog properties
HightlightRuleBuilder.button.add=&Añadir

HightlightRuleBuilder.button.add.tooltip=Añadir el valor a la lista de valores

HightlightRuleBuilder.button.delete=Eli&minar

HightlightRuleBuilder.button.delete.tooltip=Eliminar el valor seleccionado de la lista

HightlightRuleBuilder.button.deleteall=Elimi&nar todos

HightlightRuleBuilder.button.deleteall.tooltip=Eliminar todos los valores de la lista

HightlightRuleBuilder.button.edit=&Editar...

HightlightRuleBuilder.button.edit.tooltip=Editar la selección en la lista de valores

HightlightRuleBuilder.label.value=&Valor:

HightlightRuleBuilder.list.item1=Valor

HightlightRuleBuilder.MsgDlg.Msg=Advertencia

HightlightRuleBuilder.MsgDlg.Title=El valor no puede estar vacío.

HightlightRuleBuilder.text.AND=AND

HighlightRuleBuilderDialog.choice.ValueOfThisDataItem=Valor de este elemento de datos

HighlightRuleBuilderDialog.text.AND=Y

HighlightRuleBuilderDialog.text.Any=es cualquiera

HighlightRuleBuilderDialog.text.BackgroundColor=&Color de fondo:

HighlightRuleBuilderDialog.text.Between=Está entre

HighlightRuleBuilderDialog.text.Color=&Color:

HighlightRuleBuilderDialog.text.Default=Automático

HighlightRuleBuilderDialog.text.ET=es igual que

HighlightRuleBuilderDialog.text.False=es False

HighlightRuleBuilderDialog.text.Font=&Fuente:

HighlightRuleBuilderDialog.text.GE=es mayor o igual que

HighlightRuleBuilderDialog.text.GT=es mayor que

HighlightRuleBuilderDialog.text.LE=es menor o igual que

HighlightRuleBuilderDialog.text.Like=es como

HighlightRuleBuilderDialog.text.LT=es menor que

HighlightRuleBuilderDialog.text.NE=no es igual que

HighlightRuleBuilderDialog.text.NotBetween=no está entre

HighlightRuleBuilderDialog.text.NotNull=no es nulo

HighlightRuleBuilderDialog.text.Null=es nulo

HighlightRuleBuilderDialog.text.Preview=Vista previa:

HighlightRuleBuilderDialog.text.PreviewContent=AaBbCcYyZz
HighlightRuleBuilderDialog.text.Size=Tamañ&o:

HighlightRuleBuilderDialog.text.Title=Resaltar

HighlightRuleBuilderDialog.text.True=es True

HighlightRuleBuilderDialog.tooltip.ExpBuilder=Abrir el compilador de expresiones

HighlightRuleBuilderDialog.tooltip.Bold=Negrita

HighlightRuleBuilderDialog.tooltip.Italic=Cursiva

HighlightRuleBuilderDialog.tooltip.Underline=Subrayado

HighlightRuleBuilderDialog.tooltip.Text_Line_Through=Línea a través del texto

HighlightRuleBuilderDialog.text.applyStyle=Aplicar est&ilo:

HighlightRuleBuilderDialog.displayText.None=Ninguno

HighlightRuleBuilderDialog.text.Description=Aplique formato condicional a {0}. Especifique la expresión y el formato condicional.\nEl formato se aplicará si la expresión se evalúa con True.

HighlightRuleBuilderDialog.text.Description.Element.Table=tabla

HighlightRuleBuilderDialog.text.Description.Element.Grid=cuadrícula

HighlightRuleBuilderDialog.text.Description.Element.List=lista

HighlightRuleBuilderDialog.text.Description.Element.Row=fila

HighlightRuleBuilderDialog.text.Description.Element.Column=columna

HighlightRuleBuilderDialog.text.Description.Element.DataItem=elemento de datos

HighlightRuleBuilderDialog.text.Description.Element.Cell=celda

HighlightRuleBuilderDialog.text.Group.Condition=Clá&usula condicional

HighlightRuleBuilderDialog.text.Group.Format=Formato

HighlightRuleBuilderDialog.text.Description.Element.ReportElement=elemento de informe

###########################################################
#HighlightsPage properties
HighlightsPage.Button.Add=&Añadir...

HighlightsPage.Button.Delete=&Quitar

HighlightsPage.Dialog.EditHighlight=Editar resaltado

HighlightsPage.Dialog.NewHighlight=Nuevo resaltado

HighlightsPage.Label.Highlights=Resaltar lista:

HighlightsPage.TableColumn.Condition=Cláusula condicional

HighlightsPage.TableColumn.Preview=Vista previa:

HighlightsPage.trans.Add=Añadir regla de resaltado

HighlightsPage.trans.Delete=Eliminar regla de resaltado

HighlightsPage.trans.Edit=Editar regla de resaltado

HighlightsPage.trans.MoveDown=Mover regla de resaltado hacia abajo

HighlightsPage.trans.MoveUp=Mover regla de resaltado hacia arriba

HighlightsPage.toolTipText.MoveDown=Mover regla de resaltado hacia abajo

HighlightsPage.toolTipText.MoveUp=Mover regla de resaltado hacia arriba

HighlightsPage.label.duplicate=Dupli&cado

HighlightsPage.toolTipText.duplicate=Regla de resaltado de duplicado

HighlightsPage.trans.Duplicate=Regla de resaltado de duplicado

###########################################################
#HighlightsPreferencePage properties
HighlightsPreferencePage.displayname.Condition=Cláusula condicional

HighlightsPreferencePage.displayname.Preview=Vista previa

HighlightsPreferencePage.displayname.Title=Resaltados

HighlightsPreferencePage.text.Add=&Añadir...

HighlightsPreferencePage.text.Delete=&Quitar

HighlightsPreferencePage.text.Description=Resaltar lista:

HighlightsPreferencePage.text.Edit=E&ditar...

HighlightsPreferencePage.toolTipText.Up=Mover resaltado hacia arriba

HighlightsPreferencePage.toolTipText.Down=Mover resaltado hacia abajo

HighlightsPreferencePage.text.Duplicate=&Duplicado

HighlightsPreferencePage.toolTipText.Duplicate=Resaltado de duplicado


###########################################################
#HyperlinkBuilder properties
HyperlinkBuilder.Column.Parameters=Parámetros

HyperlinkBuilder.DrillThrough.SelectTargetReport=Seleccionar un informe de destino

HyperlinkBuilder.DrillThrough.SelectTargetAnchor=Seleccionar un delimitador de destino

HyperlinkBuilder.DrillThrough.CreateLinkExpr=Crear una expresión de link que coincida con el marcador de destino

HyperlinkBuilder.DrillThrough.ShowTargetReport=Mostrar informe de destino en

HyperlinkBuilder.DrillThrough.SelectFormat=Seleccionar un formato para el informe de destino

HyperlinkBuilder.DrillThrough.Tooltip=Información sobre herramienta

HyperlinkBuilder.Step.1=Paso 1:

HyperlinkBuilder.Step.2=Paso 2:

HyperlinkBuilder.Step.3=Paso 3:

HyperlinkBuilder.Step.4=Paso 4:

HyperlinkBuilder.Step.5=Paso 5:

HyperlinkBuilder.Step.6=Paso 6:

HyperlinkBuilder.ParameterRequired=Requerido

HyperlinkBuilder.ParamterBuilder.Add.Title=Añadir parámetro de hyperlink

HyperlinkBuilder.ParamterBuilder.Edit.Title=Editar parámetro de hyperlink

HyperlinkBuilder.Column.DataType=Tipo de datos

HyperlinkBuilder.Column.Values=Valores

HyperlinkBuilder.DialogTitle=Opciones de hiperlink

HyperlinkBuilder.ErrorMessage.InvalidReport=No es posible resolver el diseño del informe de destino, la información del parámetro predefinido no estará disponible.

HyperlinkBuilder.ErrorMessage.ReportReqired=Debe especificarse el archivo de informe.

HyperlinkBuilder.DrillThroughTargetBookmark=Marcador de &destino

HyperlinkBuilder.DrillThroughLinkExpression=&Expresión de link:

HyperlinkBuilder.Label.Bookmark=&Marcador:

HyperlinkBuilder.Label.Format=Formato:

HyperlinkBuilder.Label.LinkedExpression=&Expresión vinculada:

HyperlinkBuilder.DrillThroughTargetToc=Entrada de ta&bla de contenido en informe de destino

HyperlinkBuilder.DrillThroughSameFrame=&Mismo frame

HyperlinkBuilder.DrillThroughNewWindow=&Nueva ventana

HyperlinkBuilder.DrillThroughWholePage=&Toda la página

HyperlinkBuilder.DrillThroughParentFrame=&Frame principal

HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName=Nombre del parámetro duplicado

HyperlinkBuilder.Label.Location=&Ubicación:

HyperlinkBuilder.Label.Location.hint=(Ejemplos: http://host/path, folder/file)

HyperlinkBuilder.Label.Parameters=Parámetros de informe:

HyperlinkBuilder.Label.Report=Informe:

HyperlinkBuilder.Label.SelectType=Seleccione tipo de hiperlink:

HyperlinkBuilder.DrillThroughHtml=HTML
HyperlinkBuilder.Label.Target=&Destino:

HyperlinkBuilder.Label.Tooltip=&Información sobre herramienta:

HyperlinkBuilder.Radio.Bookmark=Marcad&or interno

HyperlinkBuilder.DrillThroughPdf=PDF
HyperlinkBuilder.Radio.DrillThrough=&Aumentar

HyperlinkBuilder.ReportDesignButton=Diseño de info&rme:

HyperlinkBuilder.ReportDocumentButton=Docu&mento de informe:

HyperlinkBuilder.Radio.None=S&in Link

HyperlinkBuilder.Radio.Uri=&URI
HyperlinkBuilder.BrowseForFile=Buscar archivo

HyperlinkBuilder.Button.AddParam=&Añadir...

HyperlinkBuilder.Button.EditParam=Editar&...

HyperlinkBuilder.Button.RemoveAllParam=Quitar to&do

HyperlinkBuilder.Button.RemoveParam=Eli&minar

HyperlinkBuilder.OpenExpression=Abrir el compilador de expresiones

HyperlinkBuilder.FileNameError.Title=Error

HyperlinkBuilder.FileNameError.Message=El archivo seleccionado no es válido.

HyperlinkBuilder.TargetReportFormat=Dar for&mato a informe de destino en:

HyperlinkParameterBuilder.Label.DataType=Tipo de datos:

HyperlinkParameterBuilder.Label.Parameter=&Parámetro:

HyperlinkParameterBuilder.Label.Value=&Valor:

HyperlinkParameterBuilder.Lable.Required=Requerido:

HyperlinkParameterBuilder.Required.Choice.No=No

HyperlinkParameterBuilder.Required.Choice.Yes=Sí

###########################################################
#HyperLinkPage properties
HyperLinkPage.Label.LnikTo=&Link a:

HyperLinkPage.Label.None=Ninguno

HyperLinkPage.Menu.Save=Cambiar hiperlink

HyperLinkPage.Button.Text=Edi&tar...

HyperLinkPage.toolTipText.Button=Abrir opciones de hiperlink

###########################################################
#I18nPage properties
I18nPage.text.Note=Nota: Para establecer la localización, seleccione el informe y defina el nombre del archivo de recursos en la ficha Recursos del editor de propiedades.

###########################################################
#ImageBuilder properties
ImageBuilder.Chooser.Title=Abrir archivo de imagen

ImageBuilder.DialogTitle.Edit=Editar elemento de imagen

ImageBuilder.DialogTitle.ImportFailed=Error al importar

ImageBuilder.DialogTitle.LoadingFailed=Error al cargar imagen

ImageBuilder.DialogTitle.New=Nuevo elemento de imagen:

ImageBuilder.ErrorMessage.FileLoaded=El nombre de archivo ya existe, cámbielo y cárguelo de nuevo.

ImageBuilder.ErrorMessage.LoadingFailed=La imagen no se puede cargar correctamente.

ImageBuilder.BrowserResourceDialog.Title=Buscar archivos de imagen

ImageBuilder.BrowserResourceDialog.Message=Seleccione los archivos de imagen de la carpeta de ruta de recursos

ImageBuilder.ErrorMessage.OutOfMemory=La imagen no se puede guardar en la memoria.

ImageBuilder.FileDialog.FilterMessage=Archivo de imagen admitido

ImageBuilder.Button.Browser=Examinar...

ImageBuilder.Button.Import=&Añadir imagen...

ImageBuilder.Button.SelectImageData=Se&leccionar datos de imagen...

ImageBuilder.Button.Insert=Insertar

ImageBuilder.Label.SelectFrom=Seleccionar imagen de:

ImageBuilder.Label.EnterURI=Introducir &URI:

ImageBuilder.Label.EnterResourceFile=&Introducir archivo de recursos:

ImageBuilder.Label.EnterExpr=Introduci&r expresión de imagen dinámica:

ImageBuilder.Type.URI=&URI
ImageBuilder.Type.ResourceFile=Archivo de &imagen en recursos compartidos

ImageBuilder.Type.EmbededImage=&Imagen incrustada

ImageBuilder.Type.DynamicImage=&Imagen dinámica

ImageBuilder.Description.URI=Link a imagen en web

ImageBuilder.Description.ResourceFile=Link a imagen en recursos compartidos

ImageBuilder.Description.Embedded=Incrustar i&magen en el informe

ImageBuilder.Description.Dynamic=Recuperar imagen desde conjunto de datos (bases de datos)

ImageBuilder.Button.Browser.Tooltip=Examinar archivo de imagen

ImageBuilder.Reload.Tooltip=Volver a cargar la imagen

###########################################################
#ImageEditPart properties
ImageEditPart.trans.editImage=Cambiar imagen

###########################################################
#ImagePageGenerator properties
ImagePageGenerator.List.AltText=Texto alternativo

ImagePageGenerator.List.Bookmark=Marcador

ImagePageGenerator.List.Borders=Borde

ImagePageGenerator.List.General=General

ImagePageGenerator.List.HyperLink=Hyperlink

ImagePageGenerator.List.Reference=Referencia

ImagePageGenerator.List.Section=Salto de página

ImagePageGenerator.List.TOC=Tabla de contenido

ImagePageGenerator.List.Visibility=Visibilidad

###########################################################
#ImportCSSStyleAction properties
ImportCSSStyleAction.text=&Importar estilo CSS...

###########################################################
#ImportLibraryAction properties
ImportLibraryAction.Error.CannotBeEmpty=El espacio de nombres no puede estar vacío.

ImportLibraryAction.Error.HasBeenUsed=El espacio de nombres se está usando.

ImportLibraryAction.Error.HasDot=El espacio de nombres no debe contener '.'

ImportLibraryAction.Message.ImportSuccessfully=La librería {0} se ha importado.

ImportLibraryAction.Message=¿Desea usar esta librería en el diseño del informe?

ImportLibraryAction.Namespace=Espacio de nombres:

ImportLibraryAction.Prefix=Prefijo:

ImportLibraryAction.Title.ImportSuccessfully=Se ha importado correctamente

ImportLibraryAction.Dialog.Titile=Usar librería

ImportLibraryAction.Dialog.Message=Seleccione una librería para incluirla en el informe.

###########################################################
#ImportLibraryDialog properties
ImportLibraryDialog.Title=Usar librería

###########################################################
#ImportValueDialog properties
ImportValueDialog.Label.SelectColumn=Seleccionar &columna:

ImportValueDialog.Label.SelectDataSet=Seleccionar conjunto &de datos:

ImportValueDialog.Label.SelectValue=Se&leccionar o introducir el valor que se debe agregar

ImportValueDialog.Title=Importar valores

ImportValueDialog.Button.Add.ToolTip=Añadir

ImportValueDialog.Button.AddAll.ToolTip=Añadir todos

ImportValueDialog.Button.Remove.ToolTip=Quitar

ImportValueDialog.Button.RemoveAll.ToolTip=Quitar todos
###########################################################
#IncludeDetailAction properties
IncludeDetailAction.actionMsg.includeDetail=&Detalle

###########################################################
#IncludeFooterAction properties
IncludeFooterAction.actionMsg.includeFooter=&Pie de página

###########################################################
#IncludeHeaderAction properties
IncludeHeaderAction.actionMsg.includeHeader=&Encabezado

###########################################################
#IncludeListDetailAction properties
IncludeListDetailAction.actionMsg.includeDetail=Incluir detalle

###########################################################
#IncludeListFooterAction properties
IncludeListFooterAction.actionMsg.includeFooter=Incluir pie de página

###########################################################
#IncludeListHeaderAction properties
IncludeListHeaderAction.actionMsg.includeHeader=Incluir encabezado

###########################################################
#IncludeTableGroupFooterAction properties
IncludeTableGroupFooterAction.actionMsg.includeFooter=Pie de página de grupo

###########################################################
#IncludeTableGroupHeaderAction properties
IncludeTableGroupHeaderAction.actionMsg.includeHeader=Encabezado de grupo

###########################################################
#InputParameterDailog properties
InputParameterDailog.dialog.title.required=Parámetro necesario: debe tener un valor.

InputParameterDailog.dialog.title=Introducir parámetros

InputParameterDailog.parameter.noparameter=No hay ningún parámetro asociado a este informe.

InputParameterDialog.ErrorMessage.InvalidFile=El informe contiene errores.

InputParameterDailog.parameter.null=Valor nulo

InputParameterDialog.ErrorTitle.Error=Error

InputParameterDialog.boolean.checked=Sí

###########################################################
#InsertAction properties
InsertAction.text=I&nsertar elemento

###########################################################
#InsertColumnAction properties
InsertColumnAction.actionMsg.insert=Insertar

###########################################################
#InsertColumnLeftAction properties
InsertColumnLeftAction.actionMsg.columnToLeft=Columna a la &izquierda

###########################################################
#InsertColumnRightAction properties
InsertColumnRightAction.actionMsg.columnToRight=Columna a la &derecha

###########################################################
#InsertExpressionMenuAction properties
InsertExpressionMenuAction.text.Expression=Expresión...

###########################################################
#InsertGroupAction properties
InsertGroupAction.actionMsg.group=Insertar &grupo

InsertGroupAction.stackMsg.addGroup=Añadir grupo

###########################################################
#InsertGroupActionFactory properties
InsertGroupActionFactory.label.insertGroup=Insertar &grupo

###########################################################
#InsertInLayoutAction properties
InsertInLayoutAction.action.text=I&nsertar en diseño

###########################################################
#InsertListGroupAction properties
InsertListGroupAction.actionMsg.insertGroup=Insertar grupo

InsertListGroupAction.stackMsg.insertListGroup=Insertar grupo de lista

###########################################################
#InsertPasteColumnAction properties
InsertPasteColumnAction.text=In&sertar columna copiada

###########################################################
#InsertPositionGroupAction properties
InsertPositionGroupAction.Label.Above=&Anterior

InsertPositionGroupAction.Label.Below=&Siguiente

InsertPositionGroupAction.Label.Into=En

###########################################################
#InsertRowAboveAction properties
InsertRowAboveAction.actionMsg.rowAbove=&Anterior

###########################################################
#InsertRowAction properties
InsertRowAction.actionMsg.insert=&Insertar

###########################################################
#InsertRowBelowAction properties
InsertRowBelowAction.actionMsg.rowBelow=&Siguiente

###########################################################
#IReportGraphicConstants properties
IReportGraphicConstants.ImageType.All=Todos los archivos de imagen (*.bmp,*.JPG,*.JPEG,*.GIF,*.TIF,*.PNG,*.ICO)

IReportGraphicConstants.ImageType.Bmp=Archivos de mapas de bits (*.bmp)

IReportGraphicConstants.ImageType.Gif=Archivos GIF (*.GIF)

IReportGraphicConstants.ImageType.Icon=Archivos de iconos (*.ICO)

IReportGraphicConstants.ImageType.Jpg=Archivos JPEG (*.JPG,*.JPEG)

IReportGraphicConstants.ImageType.Png=Archivos PNG (*.PNG)

IReportGraphicConstants.ImageType.Tif=Archivos TIF (*.TIF)

###########################################################
#JSDocumentProvider properties
JSDocumentProvider.Content.Default=Nuevo contenido

JSDocumentProvider.document.newName=Nuevo contenido

###########################################################
#JSEditor properties
JSEditor.cmb.NewEventFunction=Función del nuevo evento...

JSEditor.Label.Script=Script:

JSEditor.Button.Validate=Validar script

JSEditor.Button.Reset=Restablecer script

JSEditor.Button.Help=Mostrar descripción

JSEditor.Display.DefaultText=//Editor de script JS\n

JSEditor.Display.NoExpression=Este elemento no tiene métodos.

JSEditor.Input.trial=prueba

JSEditor.Text.NoText=Sin texto

JSEditor.Text.NoDescription=Este método no tiene descripción.

JSEditor.Validate.Title=Validación

JSEditor.Validate.NoError=No se ha encontrado ningún error de sintaxis en el script actual.

JSEditor.Folding.Group=Plegar

JSEditor.Folding.ExpandAll.label=Expandir todo

JSEditor.Folding.CollapseAll.label=Contraer todo

JSEditor.Folding.CollapseMethods.label=Contraer métodos

JSEditor.Folding.CollapseComments.label=Contraer comentarios

###########################################################
#label properties
label.defaultValue=(Valor predeterminado)

label.input=Entrada

label.inputOutput=Entrada/Salida

label.no=No

label.output=Salida

label.redo=Rehacer

label.undo=Deshacer

label.yes=Sí

###########################################################
#LabelEditPart properties
LabelEditPart.Figure.Default=Etiqueta

###########################################################
#LabelPageGenerator properties
LabelPageGenerator.List.Bookmark=Marcador

LabelPageGenerator.List.Borders=Borde

LabelPageGenerator.List.General=General

LabelPageGenerator.List.HyperLink=Hyperlink

LabelPageGenerator.List.I18n=Localización

LabelPageGenerator.List.Margin=Margen

LabelPageGenerator.List.Padding=Relleno

LabelPageGenerator.List.Section=Salto de página

LabelPageGenerator.List.TOC=Tabla de contenido

LabelPageGenerator.List.Visibility=Visibilidad

###########################################################
#LayoutConfigurationBlock properties
LayoutConfigurationBlock.Default.Unit.Auto=Automático

LayoutConfigurationBlock.Default.Unit.Label=Unidad &predeterminada:
LayoutConfigurationBlock.Default.Layout.Label=Diseño predeterminado:
LayoutConfigurationBlock.Default.Orientation.Label=Orientación predeterminada:

###########################################################
#LayoutEditor properties
LayoutEditor.ErrorMessage.OpenError=Se ha producido un error al abrir el editor.

LayoutEditor.Page.Layout=Normal

LayoutEditor.Page.MasterPage=Página maestra

LayoutEditor.Page.Source=Fuente XML

###########################################################
#LayoutPageAction properties
LayoutPageAction.actionText.layout=Diseño

###########################################################
#LibrariesNodeProvider properties
LibrariesNodeProvider.DisplayLabel.Libraries=Librerías

###########################################################
#LibraryExplorerView properties
LibraryExplorerView.defaultText.notAvailable=Los recursos no están disponibles.

###########################################################
#LibraryExplorerProvider properties
LibraryExplorerProvider.FolderNotExist=La carpeta de recursos de BIRT definida como preferencia no existe.

###########################################################
#LibraryPage properties
LibraryPage.Label.Namespace=Espacio de &nombres:

LibraryPage.Label.Path=&Ruta:

###########################################################
#LibraryProvider properties
LibraryProvider.openDefinedLibrary.error.dialog.message=Error al abrir la librería definida

LibraryProvider.openDefinedLibrary.error.dialog.title=Error

###########################################################
#ListBandControlFigure properties
ListBandControlFigure.tooltip.GroupFooter=Grupo de lista - Pie de página

ListBandControlFigure.tooltip.GroupHeader=Grupo de lista - Encabezado

ListBandControlFigure.tooltip.ListDetail=Lista - Detalle

ListBandControlFigure.tooltip.ListFooter=Lista - Pie de página

ListBandControlFigure.tooltip.ListHeader=Lista - Encabezado

###########################################################
#ListBandProvider properties
ListBandProvider.action.text=I&nsertar grupo

###########################################################
#ListBandProxy properties
ListBandProxy.list.detail=Detalle

ListBandProxy.list.footer=Pie de página

ListBandProxy.list.groupFooter=GF

ListBandProxy.list.groupHeader=GH

ListBandProxy.list.header=Encabezado

###########################################################
#ListContainProcess properties
ListContainProcess.action.New=Nuevo...

ListContainProcess.action.NewRow=Nueva fila

ListContainProcess.action.NewSubSection=Nueva subsección

###########################################################
#ListEditPart properties
ListEditPart.GUIDEHANDLE_TEXT = Lista

###########################################################
#ListFigure properties
ListFigure.BORDER_TEXT=Lista

###########################################################
#ListHandleAdapt properties
ListHandleAdapt.transLabel.insertGroup=Insertar grupo

###########################################################
#ListPageGenerator properties
ListPageGenerator.List.Bookmark=Marcador

ListPageGenerator.List.Borders=Borde

ListPageGenerator.List.Font=Fuente

ListPageGenerator.List.General=General

ListPageGenerator.List.Section=Salto de página

ListPageGenerator.List.TOC=Tabla de contenido

ListPageGenerator.List.Visibility=Visibilidad


###########################################################
#MapPage properties
MapPage.label.add=&Añadir...

MapPage.label.condition=Cláusula condicional

MapPage.label.delete=&Quitar

MapPage.label.displayValue=Mostrar valor:

MapPage.label.mapList=Lista de asignación:

MapPage.transName.addMapRule=Añadir regla de asignación

MapPage.transName.deleteMapRule=Eliminar regla de asignación

MapPage.transName.editMapRule=Editar regla de asignación

MapPage.transName.moveDownRule=Mover hacia abajo la regla de asignación

MapPage.transName.moveUpMapRule=Mover hacia arriba la regla de asignación

MapPage.toolTipText.moveUp=Mover regla de asignación hacia arriba

MapPage.toolTipText.moveDown=Mover regla de asignación hacia abajo

MapPage.label.duplicate=Dupli&cado

MapPage.toolTipText.duplicate=Regla de asignación de duplicado

MapPage.transName.Duplicate=Regla de asignación de duplicado

###########################################################
#MapPreferencePage properties
MapPreferencePage.displayname.Condition=Cláusula condicional

MapPreferencePage.displayname.DisplayValue=Mostrar valor

MapPreferencePage.displayname.Title=Asignar

MapPreferencePage.text.Add=&Añadir...

MapPreferencePage.text.Delete=&Quitar

MapPreferencePage.text.Description=Reglas de asignación:

MapPreferencePage.text.Edit=&Editar...

MapPreferencePage.toolTipText.Up=Mover regla de asignación hacia arriba

MapPreferencePage.toolTipText.Down=Mover regla de asignación hacia abajo

MapPreferencePage.text.Duplicate=&Duplicado

MapPreferencePage.toolTipText.Duplicate=Regla de asignación de duplicado

###########################################################
#MapRuleBuilderDialog properties
MapRuleBuilder.button.add=&Añadir

MapRuleBuilder.button.add.tooltip=Añadir el valor a la lista de valores

MapRuleBuilder.button.delete=Quita&r

MapRuleBuilder.button.delete.tooltip=Eliminar el valor seleccionado de la lista

MapRuleBuilder.button.deleteall=Elimi&nar todos

MapRuleBuilder.button.deleteall.tooltip=Eliminar todos los valores de la lista

MapRuleBuilder.button.edit=&Editar...

MapRuleBuilder.button.edit.tooltip=Editar la selección en la lista de valores

MapRuleBuilder.DialogTitle.New=Nueva regla de asignación

MapRuleBuilder.DialogTitle.Edit=Editar regla de asignación

MapRuleBuilder.Button.ResourceKey=Clave de &recursos:

MapRuleBuilder.Button.Reset=&Restablecer

MapRuleBuilder.Button.Browse=&Examinar...

MapRuleBuilder.Button.Browse.Tooltip=Examinar y seleccionar una clave

MapRuleBuilder.label.value=&Valor:

MapRuleBuilder.list.item1=Valor

MapRuleBuilder.MsgDlg.Msg=Advertencia

MapRuleBuilder.MsgDlg.Title=El valor no puede estar vacío.

MapRuleBuilder.text.AND=AND

MapRuleBuilderDialog.text.Between=está entre

MapRuleBuilderDialog.text.Condition=Si la siguiente condición es true:

MapRuleBuilderDialog.text.Display=&Mostrar valor siguiente:

MapRuleBuilderDialog.text.ET=es igual que

MapRuleBuilderDialog.text.GE=es mayor o igual que

MapRuleBuilderDialog.text.GT=es mayor que

MapRuleBuilderDialog.text.LE=es menor o igual que

MapRuleBuilderDialog.text.Like=es como

MapRuleBuilderDialog.text.LT=es menor que

MapRuleBuilderDialog.text.NE=no es igual que

MapRuleBuilderDialog.text.NotBetween=no está entre

MapRuleBuilderDialog.text.NotNull=no es nulo

MapRuleBuilderDialog.text.Null=es nulo

MapRuleBuilderDialog.text.Title=Asignar

MapRuleBuilderDialog.MsgDlg.Title=Advertencia

MapRuleBuilderDialog.MsgDlg.Msg=El valor no puede estar vacío.

###########################################################
#SortkeyBuilder dialog properties
SortkeyBuilder.DialogTitle.New=Nueva clave de ordenación

SortkeyBuilder.DialogTitle.Edit=Editar clave de ordenación

SortkeyBuilder.DialogMessage.New=Rellene el siguiente contenido para crear una clave de ordenación.

SortkeyBuilder.DialogMessage.Edit=Edite el siguiente contenido para actualizar la clave de ordenación.

SortkeyBuilder.DialogTitle.Label.Prompt=Introducir una clave para ordenar según:

SortkeyBuilder.DialogTitle.Label.Key=&Clave:

SortkeyBuilder.DialogTitle.Label.Direction=Di&rección

SortkeyBuilder.Label.Locale=&Configuración regional:

SortkeyBuilder.Label.Strength=&Intensidad:

SortkeyBuilder.DialogTitle.Button.ExpressionBuilder=Abrir el compilador de expresiones

SortkeyBuilder.DialogTitle.Error.SetSortKey.Title=Error

SortkeyBuilder.Locale.Auto=Automático

SortkeyBuilder.Strength.ASCII=ASCII
SortkeyBuilder.Strength.PRIMARY=Primario

SortkeyBuilder.Strength.SECONDARY=Secundario

SortkeyBuilder.Strength.TERTIARY=Terciario

SortkeyBuilder.Strength.QUATERNARY=Cuaternario

SortkeyBuilder.Strength.IDENTICAL=Idéntico

###########################################################
#FilterConditionBuilder dialog properties
FilterConditionBuilder.DialogTitle.New=Nueva condición de filtro

FilterConditionBuilder.DialogTitle.Edit=Editar condición de filtro

FilterConditionBuilder.DialogMessage.New=Rellene el siguiente contenido para crear una condición de filtro.

FilterConditionBuilder.DialogMessage.Edit=Edite el siguiente contenido para actualizar la condición de filtro.

FilterConditionBuilder.choice.ValueOfThisDataItem=Valor de este elemento de datos

FilterConditionBuilder.tooltip.ExpBuilder=Abrir el compilador de expresiones

FilterConditionBuilder.text.AND=Y

FilterConditionBuilder.text.Condition=Introducir una condicion de filtro:

FilterConditionBuilder.Shell.Title=Condición de filtro

FilterConditionBuilder.list.item1=Valor

FilterConditionBuilder.button.edit=&Editar...

FilterConditionBuilder.button.delete=Eli&minar

FilterConditionBuilder.button.deleteall=Elimi&nar todos

FilterConditionBuilder.button.add=&Añadir

FilterConditionBuilder.button.edit.tooltip=Editar la selección en lista de valores

FilterConditionBuilder.button.delete.tooltip=Eliminar el valor seleccionado de la lista

FilterConditionBuilder.button.deleteall.tooltip=Eliminar todos los valores de la lista

FilterConditionBuilder.button.add.tooltip=Añadir el valor a la lista de valores

FilterConditionBuilder.label.value=&Valor:

FilterConditionBuilder.multipleValue.label=&Valor

FilterConditionBuilder.multipleValue.button.add=&Añadir

FilterConditionBuilder.multipleValue.button.remove=Quita&r

FilterConditionBuilder.multipleValue.button.removeAll=Qui&tar todos

FilterConditionBuilder.multipleValue.button.edit=E&ditar...

FilterConditionBuilder.Button.UpdateAggregation=Recalcular &totales

###########################################################
#MarignPropertyDescriptor properties
MarignPropertyDescriptor.error.message=El valor introducido es demasiado grande.

MarignPropertyDescriptor.error.title=Valor demasiado grande

###########################################################
#MasterPageAction properties
MasterColumnsPropertyDescriptor.Button.Text.Custom=&Personalizado

MasterColumnsPropertyDescriptor.Button.Text.OneColumn=&Uno

MasterColumnsPropertyDescriptor.Button.Text.ThreeColumns=&Tres

MasterColumnsPropertyDescriptor.Button.Text.TwoColumns=&Dos

MasterColumnsPropertyDescriptor.Combo.Text.Column.Number=&Número de columnas:

MasterPageAction.actionText.masterPage=Página maestra

###########################################################
#MasterPageGenerator properties
MasterPageGenerator.List.Columns=Columnas

MasterPageGenerator.List.General=General

MasterPageGenerator.List.Borders=Borde

MasterPageGenerator.List.Header&Footer=Encabezado/Pie de página

MasterPageGenerator.List.Margins=Margen

###########################################################
#MasterPageGeneralPage properties
MasterPageGeneralPage.Trans.SetType=Tipo de conjunto

###########################################################
#MasterPagePaletteFactory properties
MasterPagePaletteFactory.Autotext.TotalPageCount=Recuento de páginas total

MasterPagePaletteFactory.Autotext.Author_Page_Date=Autor\#, Página\#, Fecha\#

MasterPagePaletteFactory.Autotext.Confidential_Page=Confidencial, Página\#

MasterPagePaletteFactory.Autotext.CreatedBy=Creado por

MasterPagePaletteFactory.Autotext.CreatedOn=Creado el

MasterPagePaletteFactory.Autotext.Date=Fecha

MasterPagePaletteFactory.Autotext.Filename=FileName

MasterPagePaletteFactory.Autotext.Label=AutoText
MasterPagePaletteFactory.Autotext.LastPrinted=Última impresión

MasterPagePaletteFactory.Autotext.Page=Página

MasterPagePaletteFactory.Autotext.PageXofY=Página n de m

MasterPagePaletteFactory.AutotextTooltip.TotalPageCount=Elemento de informe de recuento de páginas total

MasterPagePaletteFactory.AutotextTooltip.AuthorPageDate=Autor#, Fecha# Elemento de informe

MasterPagePaletteFactory.AutotextTooltip.ConfidentialPage=Elemento de informe confidencial

MasterPagePaletteFactory.AutotextTooltip.CreatedBy=Creado por elemento de informe

MasterPagePaletteFactory.AutotextTooltip.CreatedOn=Creado en elemento de informe

MasterPagePaletteFactory.AutotextTooltip.Date=Elemento de informe de fecha

MasterPagePaletteFactory.AutoTextTooltip.Filename=Elemento de informe de nombre de archivo

MasterPagePaletteFactory.AutotextTooltip.LastPrinted=Elemento de informe de última impresión

MasterPagePaletteFactory.AutotextTooltip.Page=Elemento de informe de página

MasterPagePaletteFactory.AutotextTooltip.PageXofY=Página n de m del elemento de informe

MasterPagePaletteFactory.elementLabel.authorPageDate=Autor nº, Página nº, Fecha

MasterPagePaletteFactory.elementLabel.confidentialPage=Confidencial, Página

MasterPagePaletteFactory.elementLabel.createdBy=Creado por

MasterPagePaletteFactory.elementLabel.createdOn=Creado el

MasterPagePaletteFactory.elementLabel.date=Fecha

MasterPagePaletteFactory.elementLabel.fileName=FileName

MasterPagePaletteFactory.elementLabel.lastPrinted=Última impresión

MasterPagePaletteFactory.elementLabel.page=Página

MasterPagePaletteFactory.elementLabel.pageOf=Página n de m

MasterPagePaletteFactory.paletteDrawerName.autoText=AutoText
MasterPagePaletteFactory.ToolTip.Grid=Elemento de informe de cuadrícula

MasterPagePaletteFactory.ToolTip.Text=Elemento de informe de texto

MasterPagePaletteFactory.Autotext.Variable=Variable

MasterPagePaletteFactory.AutotextTooltip.Variable=Elemento variable

###########################################################
#MasterPageProcess properties
MasterPageProcess.action.Edit=Editar

MasterPageProcess.action.New=Nuevo

MasterPageProcess.text.CannotDelete=El diseño contiene sólo una página maestra, no se puede eliminar.

MasterPageProcess.text.Display=Muestra el cuadro de diálogo de configuración de página

MasterPageProcess.text.DisplayName=MasterPage

###########################################################
#MergeAction properties
MergeAction.actionMsg.merge=&Combinar celdas

###########################################################
#ModulePage properties
ModulePage.ErrorMessage.Duplicate=Este nombre ya existe.

ModulePage.ErrorMessage.WrongExtensionName=El nombre debe terminar en ".properties".

ModulePage.ErrorMessage.InfoExtensionName=Introduzca un nombre que finalice con ".properties".

ModulePage.text.Add=&Añadir...

ModulePage.text.ExistingFile=Archivos de recursos existentes:

ModulePage.title.AddNewFile=Añadir nuevo archivo de recursos

ModulePage.Resourcefile.Dialog.Title=Examinar archivos de recursos

ModulePage.Resourcefile.Dialog.Message=Seleccione un archivo de recurso o seleccione una carpeta e introduzca un nuevo nombre de archivo para crearlo.

ModulePage.text.Path=&Ruta:

###########################################################
#MultiPageEditorContributor properties
MultiPageEditorContributor.actionText.sampleAction=Acción de muestra

MultiPageEditorContributor.actionToolTip.sampleActionToolTip=Información sobre herramienta de acción de muestra

MultiPageEditorContributor.actionToolTipText.sampleActionToolTip=Información sobre herramienta de acción de muestra

MultiPageEditorContributor.dialogMsg.sampleActionExecuted=Acción de muestra ejecutada

MultiPageEditorContributor.menuText.editorMenu=&Menú del editor

###########################################################
#NewLibraryWizard properties
NewLibraryWizard.choicePageTitle.report=Librería

NewLibraryWizard.displayName.NewReportFileExtension=.rptlibrary
NewLibraryWizard.displayName.NewReportFileNamePrefix=nueva_librería

NewLibraryWizard.error.NoProject=No hay ningún proyecto abierto en esta área de trabajo. Seleccione Archivo->Nuevo->Proyecto para crear un nuevo proyecto o abrir un proyecto existente.

NewLibraryWizard.pageDescription.createNewReport=Crear una nueva librería.

NewLibraryWizard.taskName.openFileForEdit=Abriendo archivo para editar...

NewLibraryWizard.text.ConfirmSwitchPerspective=Confirmar cambio de perspectiva

NewLibraryWizard.text.Container=Contenedor "

NewLibraryWizard.text.CreateReport=Crear una nueva librería.

NewLibraryWizard.text.Creating=Creando

NewLibraryWizard.text.DoesNotExist=" no existe.

NewLibraryWizard.text.OpenFileForEditing=Abriendo archivo para editar...

NewLibraryWizard.text.SwitchPerspective=Este tipo de archivo está asociado a la perspectiva {0}. ¿Desea cambiar a esta perspectiva ahora?

NewLibraryWizard.title.Choose=Seleccionar cómo crear la librería

NewLibraryWizard.title.Error=Error

NewLibraryWizard.title.New=Nueva librería

NewLibraryWizard.title.Report=Librería

NewLibraryWizard.title.ReportWizard=Asistente de librerías

NewLibraryWizard.title.Template=TemplateChoicePage
NewLibraryWizard.title.WizardPage=WizardPage
NewLibraryWizard.widowTitle.new=Nuevo

NewLibraryWizard.wizardPageTitle.report=Librería

###########################################################
#NewReportProjectWizard properties

NewReportProjectWizard.bin=bin
NewReportProjectWizard.caseVariantExistsError=El sistema de archivos no distingue entre mayúsculas y minúsculas. Hay un proyecto existente que entra en conflicto con ''{0}''.

NewReportProjectWizard.description=Crear un nuevo proyecto de informe.

NewReportProjectWizard.errorMessage=Se ha producido un problema al crear el nuevo proyecto de informe.

NewReportProjectWizard.errorOpeningWindow=Se ha producido un problema al abrir una ventana.

NewReportProjectWizard.internalError=Error interno: {0}

NewReportProjectWizard.javaProject=Crear un proyecto Java

NewReportProjectWizard.perspSwitchMessage=Este tipo de proyecto está asociado a la perspectiva {0}. ¿Desea cambiar a esta perspectiva ahora?

NewReportProjectWizard.perspSwitchTitle=Confirmar cambio de perspectiva

NewReportProjectWizard.projectSetting=Ajustes de proyecto

NewReportProjectWizard.src=src
NewReportProjectWizard.title=Proyecto de informe

NewReportProjectWizard.windowTitle=Nuevo proyecto de informe

###########################################################
#NewReportWizard properties
NewReportWizard.choicePageTitle.report=Informe

NewReportWizard.displayName.NewReportFileExtension=.rptdesign
NewReportWizard.displayName.NewReportFileNamePrefix=new_report
NewReportWizard.error.NoProject=No hay ningún proyecto abierto en esta área de trabajo. Seleccione Archivo->Nuevo->Proyecto para crear un nuevo proyecto o abrir un proyecto existente.

NewReportWizard.pageDescription.createNewReport=Crear un nuevo informe.

NewReportWizard.pageDescription.selectReportTemplate=Seleccione el tipo de informe que desea crear

NewReportWizard.taskName.openFileForEdit=Abriendo archivo para editar...

NewReportWizard.text.ConfirmSwitchPerspective=Confirmar cambio de perspectiva

NewReportWizard.text.Container=Contenedor "

NewReportWizard.text.CreateReport=Crear un nuevo informe.

NewReportWizard.text.Creating=Creando

NewReportWizard.text.DoesNotExist=" no existe.

NewReportWizard.text.OpenFileForEditing=Abriendo archivo para editar...

NewReportWizard.text.SelectTemplate=Seleccione el tipo de informe que desea crear.

NewReportWizard.text.SwitchPerspective=Este tipo de archivo está asociado a la perspectiva {0}. ¿Desea cambiar a esta perspectiva ahora?

NewReportWizard.title.Choose=Seleccionar cómo crear el informe.

NewReportWizard.title.Error=Error

NewReportWizard.title.New=Nuevo informe

NewReportWizard.title.Report=Informe

NewReportWizard.title.ReportWizard=Asistente de informes

NewReportWizard.title.Template=TemplateChoicePage
NewReportWizard.title.WizardPage=WizardPage
NewReportWizard.widowTitle.new=Nuevo

NewReportWizard.wizard.msgDirErr=El directorio de destino no existe y no se puede crear.


###########################################################
#NewResourceFileDialog properties
NewResourceFileDialog.label.NewFile=&Nuevo nombre de archivo:

NewResourceFileDialog.ErrorMessage=El nombre de archivo debe terminar en .properties

NewResourceFileDialog.ErrorMessageInvalid=El nombre de archivo no es válido.

NewResourceFileDialog.FileExists={0} existe. ¿Desea sobrescribir?

###########################################################
#ProjectFileDialog properties
ProjectFileDialog.ErrorMessage=El archivo debe ser un archivo, no un directorio.

ProjectFileDialog.Title=Seleccionar archivo de proyecto

ProjectFileDialog.Message=Seleccione un archivo del proyecto actual.

ProjectFileDialog.Text.Menu=menú

###########################################################
#ProjectSelectionDialog properties
ProjectSelectionDialog.Message=Seleccionar el proyecto que se va a configurar:

ProjectSelectionDialog.Text.SpecialSettings=Mostrar sólo proyectos con ajustes específicos de proyecto

ProjectSelectionDialog.Title=Configuración específica de proyecto

###########################################################
#ResourceFilterDialog properties
ResourceFilterDialog.Title=Filtro de elementos de informe

ResourceFilterDialog.Lable.Description=Descripción de filtro:

ResourceFilterDialog.Lable.Viewer=Seleccionar los elementos que se van a excluir de la vista:

###########################################################
#NewSectionDialog properties
NewSectionDialog.displayname.Container=Contenedor

NewSectionDialog.displayname.Data=Datos

NewSectionDialog.displayname.Grid=Cuadrícula

NewSectionDialog.displayname.Image=Imagen

NewSectionDialog.displayname.Label=Etiqueta

NewSectionDialog.displayname.Line=Línea

NewSectionDialog.displayname.List=Lista

NewSectionDialog.displayname.Rectangle=Rectángulo

NewSectionDialog.displayname.Table=Tabla

NewSectionDialog.displayname.Text=Texto

NewSectionDialog.text.Prefix=Secciones disponibles:

NewSectionDialog.title.insertElement=Insertar elemento

NewSectionDialog.Title=Insertar elemento

###########################################################
#NewTemplateWizard properties
NewTemplateWizard.displayName.NewReportFileNamePrefix=nueva_plantilla

NewTemplateWizard.pageDescription.createNewTemplate=Crear una nueva plantilla de informe.

NewTemplateWizard.text.CreateReport=Crear una nueva plantilla.

NewTemplateWizard.text.Creating=Creando

NewTemplateWizard.text.OpenFileForEditing=Abriendo archivo de plantilla para editar...

NewTemplateWizard.title.New=Nueva plantilla

NewTemplateWizard.title.Report=Informe

NewTemplateWizard.title.Template=Plantilla

NewTemplateWizard.title.WizardPage=WizardPage

###########################################################
#NoneAction properties
NoneAction.text=Ninguno

###########################################################
#OutputColumnDefnPage properties
OutputColumnDefnPage.description=Introducir la definición de las columas de salida que produce el script de recuperación:

OutputColumnDefnPage.OutputColumns.Tooltip=Ver y definir las columnas en una fila de datos

OutputColumnPage.error.createNewColumn=Se ha producido un error al añadir una nueva columna.

OutputColumnPage.error.invalidSelection=No se ha seleccionado ningún elemento de tabla válido.

###########################################################
#ColumnDefPage properties
ColumnDefPage.description=Página de definición de la columna de salida del conjunto de datos de script

ColumnDefPage.title=Columnas de salida

###########################################################
#ResultSetColumnPage properties
ResultSetColumnPage.button.add=&Añadir...

ResultSetColumnPage.button.edit=&Editar...

ResultSetColumnPage.button.delete=&Eliminar

ResultSetColumnPage.button.up=A&rriba

ResultSetColumnPage.button.down=A&bajo

ResultSetColumnPage.inputDialog.newColumn.title=Nueva columna de conjuntos de datos de script

ResultSetColumnPage.inputDialog.editColumn.title=Editar columna de conjuntos de datos de script

ResultSetColumnPage.inputDialog.label.columnName=Nombre de &columna:

ResultSetColumnPage.inputDialog.label.dataType=&Tipo de datos:

ResultSetColumnPage.inputDialog.label.alias=&Alias de columna:

ResultSetColumnPage.inputDialog.label.displayName=N&ombre en pantalla:

ResultSetColumnPage.inputDialog.label.displayNameKey=&Clave de nombre en pantalla:

ResultSetColumnPage.inputDialog.warning.emptyColumnName=Debe introducir el nombre de columna.

ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias=El nombre de columna y el alias no deben coincidir.

ResultSetColumnPage.inputDialog.error.duplicatedColumnName=El nombre de columna "{0}" ya se ha utilizado.

ResultSetColumnPage.inputDialog.error.duplicatedAlias=El alias de columna "{0}" ya se ha utilizado.

###########################################################
#OutputColumnsPage properties
OutputColumnPage.OutputColumns.Tooltip=Ver y definir las columnas en una fila de datos

OutputColumnPage.toolTipText.Edit=Editar columna de salida

OutputColumnPage.title.OutputColumnInputDialog=Columna de salida

OutputColumnPage.title.OutputColumnInputDialog.NewOutputColumn=Nueva columna de salida

OutputColumnPage.title.OutputColumnInputDialog.EditOutputColumn=Editar columna de salida

OutputColumnPage.OutputColumns.DuplicatedName=El nombre ya existe.

OutputColumnsPage.description=Definir columnas de salida:

OutputColumnsPage.DataTypeConvert.Note=Nota

OutputColumnsPage.DataTypeConvert.Message=La conversión puede producir pérdidas de precisión o un error de tiempo de ejecución, ¿está seguro de que desea aplicarla?

###########################################################
#PageBreakPreferencePage properties
PageBreakPreferencePage.displayname.Title=Salto de página

###########################################################
#PageSetAction properties
PageSetAction.text.code=&Script

PageSetAction.Text.EditCode=Editar &script...

PageSetAction.text.layout=&Diseño

PageSetAction.text.master=&Maestro

PageSetAction.text.normal=&Normal

PageSetAction.text.preview=&Vista previa

###########################################################
#PageSetupOutlineProcess properties
PageSetupOutlineProcess.action.Edit=Editar configuración de página

PageSetupOutlineProcess.action.New=Nueva MasterPage

PageSetupOutlineProcess.text.Display=Muestra el cuadro de diálogo de configuración de página

PageSetupOutlineProcess.text.Switch=Cambia al editor de página maestra

###########################################################
#ParameterDialog properties
ParameterDialog.Boolean.False=False

ParameterDialog.Boolean.True=True

ParameterDialog.Button.ChangeFormat=Camb&iar...

ParameterDialog.Button.CreateDataSet=Crear nue&vo...

ParameterDialog.Button.ImportValue=Importar valor&es...

ParameterDialog.Button.RemoveDefault=Quitar pre&determinado

ParameterDialog.Button.SetDefault=Configurar &como predeterminada

ParameterDialog.CheckBox.IsRequired=&Es necesario

ParameterDialog.CheckBox.DoNotEchoInput=No realizar comando &eco de entrada

ParameterDialog.CheckBox.Hidden=&Oculto

ParameterDialog.CheckBox.Distinct=Permitir va&lores duplicados

ParameterDialog.Choice.NoDefault=Ningún valor predeterminado

ParameterDialog.Choice.NullValue=Valor nulo

ParameterDialog.Choice.BlankValue=Valor en blanco

ParameterDialog.Choice.None=Ninguno

ParameterDialog.Choice.DisplayText=Mostrar texto

ParameterDialog.Choice.ValueColumn=Columna de valores

ParameterDialog.Choice.ASCENDING=Ascendente

ParameterDialog.Choice.DESCENDING=Descendente

ParameterDialog.Choice.SelectValue=<Seleccionar valor...>

ParameterDialog.Column.Default=Predeterminado

ParameterDialog.Column.DisplayText=Texto en pantalla

ParameterDialog.Column.DisplayTextKey=Visualizar clave de texto

ParameterDialog.Column.Value=Valor

ParameterDialog.DefaultValue.Add=&Añadir

ParameterDialog.DisplayLabel.Combo=Cuadro combinado

ParameterDialog.DisplayLabel.List=Cuadro de lista

ParameterDialog.ErrorMessage.CanootBeBlank=El valor no puede estar en blanco.

ParameterDialog.ErrorMessage.CanootBeNull=El valor no puede ser nulo.

ParameterDialog.ErrorMessage.DuplicatedLabel=El texto en pantalla ya existe.

ParameterDialog.ErrorMessage.DuplicatedLabelKey=La clave de texto en pantalla ya existe.

ParameterDialog.ErrorMessage.DuplicatedName=El nombre se está usando.

ParameterDialog.ErrorMessage.DuplicatedValue=El valor ya existe.

ParameterDialog.ErrorMessage.EmptyName=El nombre está vacío.

ParameterDialog.ErrorMessage.InvalidListLimit=El valor de limitación debe estar entre 0 y {0}.

ParameterDialog.ErrorMessage.MismatchDataType=El valor no coincide con el tipo de fecha.

ParameterDialog.ErrorMessage.NoAvailableColumn=No hay ninguna columna disponible.

ParameterDialog.ErrorMessage.ValueColumnEmpty=Se debe especificar la columna de valores.

ParameterDialog.ErrorMessage.NoDefaultValue=Debe establecer un valor predeterminado.

ParameterDialog.ErrorTitle.InvalidListLimit=Límite de lista no válido

ParameterDialog.ExpressionEditor.Title=Editar expresión

ParameterDialog.Flag.Default=Predeterminado

ParameterDialog.Group.MoreOption=Mostrar como

ParameterDialog.Group.Properties=Propiedades

ParameterDialog.Label.DataType=Tipo de &datos:

ParameterDialog.SelectionDialog.Edit=Editar opción de selección

ParameterDialog.Label.DefaultValue=Valor predete&rminado:

ParameterDialog.Label.DisplayPrompt=Mostrar solicitud:

ParameterDialog.Label.DisplayType=Mostrar tip&o:

ParameterDialog.Label.Format=Formato como:

ParameterDialog.Label.HelpText=Te&xto de ayuda:

ParameterDialog.Label.ListOfValue=Valores de lista de selección

ParameterDialog.Label.SortGroup=Orden

ParameterDialog.Label.Name=&Nombre:

ParameterDialog.Label.None=<Ninguno>

ParameterDialog.Label.Null=Nulo

ParameterDialog.Label.Preview=Vista previa con formato

ParameterDialog.Label.PromptText=Texto de solicit&ud:

ParameterDialog.Label.SelectDataSet=Conjunto de &datos:

ParameterDialog.Label.SortKey=Ordenar &por:

ParameterDialog.Label.SortDirection=Dire&cción de orden:

ParameterDialog.SelectionDialog.New=Nueva opción de selección

ParameterDialog.SelectionDialog.Label.DisplayTextKey=&Visualizar clave de texto:

ParameterDialog.SelectionDialog.Label.DisplayText=Visualizar &texto:

ParameterDialog.SelectionDialog.Label.Value=&Valor:

ParameterDialog.SelectionDialog.Button.Resource=...
ParameterDialog.SelectionDialog.Button.Resource.Tooltip=Examinar y seleccionar una clave

ParameterDialog.SelectionDialog.Button.Remove.Tooltip=Quitar clave

ParameterDialog.SelectionDialog.Label.Note=Nota: Para establecer la clave de texto, seleccione el informe y defina el nombre del archivo de recursos en la ficha Recursos del editor de propiedades.

ParameterDialog.Label.SelectDisplayText=Seleccionar te&xto en pantalla:

ParameterDialog.Label.SelectValueColumn=Seleccionar &columna de valores:

ParameterDialog.Label.Value=Valores:

ParameterDialog.Label.values=valores

ParameterDialog.Label.Sample=Muestra

ParameterDialog.Label.Listlimit=Límite de &lista:

ParameterDialog.Radio.Dynamic=&Dinámico

ParameterDialog.Radio.Static=&Estático

ParameterDialog.toolTipText.OpenExprButton=Abrir el compilador de expresiones

ParameterDialog.datetime.prompt=Introduzca valores de fecha y hora como: {0}

ParameterDialog.date.prompt=Introduzca valores de fecha como: {0}

ParameterDialog.time.prompt=Introduzca valores de hora como: {0}

ParameterDialog.Check.AllowMulti=Permitir &valores múltiples

ParameterDialog.checkBox.InputValue=Valor de entrada

ParameterDialog.checkBox.BlankValue=Valor en blanco

ParameterDialog.checkBox.NullValue=Valor nulo

ParameterDialog.emptyColumnValueExpression.title=La expresión de columna de valor no es válida

ParameterDialog.emptyColumnValueExpression.message=La expresión de columna de valor no puede estar vacía

ParameterDialog.Value.Empty=Valor vacío

ParameterDialog.Value.Null=Valor nulo

ParameterDialog.message=Defina las propiedades del parámetro de informe.

###########################################################
#SelectParameterDefaultValueDialog properties
SelectParameterDefaultValueDialog.Title=Seleccionar valor:

###########################################################
#ParameterDropListener properties
ParameterDropListener.Command.Copy=Copiar elemento

ParameterDropListener.Command.Move=Mover elemento

###########################################################
#ParameterGroupDialog properties
ParameterGroupDialog.Page.Title=General

ParameterGroupDialog.pageTitle.general=General

###########################################################
#ParameterGroupGeneralPage properties
ParameterGroupGeneralPage.Label.DisplayName=N&ombre en pantalla:

ParameterGroupGeneralPage.Label.Name=&Nombre:

###########################################################
#ParameterGroupNodeProvider properties
ParameterGroupNodeProvider.Action.GroupNew=Nuevo grupo de parámetros

ParameterGroupNodeProvider.Action.ParameterEdit=&Editar

ParameterGroupNodeProvider.Action.ParameterNew=&Nuevo parámetro

ParameterGroupNodeProvider.Dialogue.ParameterEdit=Editar grupo de parámetros

ParameterGroupNodeProvider.Dialogue.ParameterNew=Nuevo parámetro

ParameterGroupNodeProvider.Node.Group=Grupo de parámetros

###########################################################
#ParameterGroupProcess properties
ParameterGroupProcess.action.Delete=Eliminar Parametergroup

ParameterGroupProcess.action.Edit=Editar Parametergroup

ParameterGroupProcess.action.New=Nuevo parámetro

ParameterGroupProcess.text.DisplayName=ParameterGroup
ParameterGroupProcess.text.Show=muestra la página del editor de fuente de datos

###########################################################
#ParameterNodeProvider properties
ParameterNodeProvider.dial.title.editCascading=Editar grupo de parámetros en cascada

ParameterNodeProvider.dial.title.editScalar=Editar parámetro

ParameterNodeProvider.dial.title.newCascading=Nuevo grupo de parámetros en cascada

ParameterNodeProvider.menu.text.edit=&Editar

ParameterNodeProvider.menu.text.new=Nuevo parámetro

###########################################################
#ParameterProcess properties
ParameterProcess.action.Delete=Eliminar parámetro

ParameterProcess.action.Edit=Editar parámetro

ParameterProcess.text.DisplayName=Parámetro

ParameterProcess.text.Show=muestra la página del editor de fuente de datos

###########################################################
#parameters properties
parameters.datatypes.boolean=Booleano

parameters.datatypes.dateTime=DateTime
parameters.datatypes.decimal=Decimal

parameters.datatypes.float=Flotante

parameters.datatypes.string=String

parameters.menuItem.reset=Restablecer

###########################################################
#ParametersNodeProvider properties
ParametersNodeProvider.dialogue.title.group=Nuevo grupo de parámetros

ParametersNodeProvider.dialogue.title.parameter=Nuevo parámetro

ParametersNodeProvider.menu.text.cascadingParameter=Nuevo grupo de parámetros en casca&da

ParametersNodeProvider.menu.text.group=Nu&evo grupo de parámetros

ParametersNodeProvider.menu.text.parameter=Nuevo p&arámetro

###########################################################
#ParametersOutlineProcess properties
ParametersOutlineProcess.action.Edit=Editar

ParametersOutlineProcess.action.New=Nuevo parámetro

ParametersOutlineProcess.action.NewGroup=Nuevo grupo

ParametersOutlineProcess.text.Open=abre el editor de parámetros

ParametersOutlineProcess.text.Show=muestra la página del editor de conjunto de datos para el conjunto de datos

###########################################################
#PasteAction properties
PasteAction.dlg.title=Información

PasteAction.ok=Aceptar

PasteAction.text=&Pegar

###########################################################
#PlaceHolderEditPart properties
PlaceHolderEditPart.Trans.TransferToElement=Transferir a elemento de informe

###########################################################
#PreviewAction properties
PreviewAction.Text=Vista previa

###########################################################
#PreviewEditor properties
PreviewEditor.parameter.tooltip=Mostrar parámetros de informe

PreviewEditor.parameter.hint=Actualizar informe

PreviewEditor.parameter.note=Nota:

PreviewEditor.parameter.info=El número máximo actual de filas de datos está limitado a {0}.

PreviewEditor.parameter.change=Haga clic para cambiar las preferencias de vista previa

PreviewEditor.ConfirmRefresh.Title=Confirmar actualización

PreviewEditor.ConfirmRefresh.Message=La operación puede influir en el resultado de vista previa. ¿Desea actualizar la página de previsualización?

PreviewEditor.browser.notfound=El explorador interno no está disponible.

###########################################################
#PreviewPageAction properties
PreviewPageAction.actionText.preview=Vista previa

###########################################################
#PropertiesAction properties
PropertiesAction.Text=Propiedades

###########################################################
#PropertyHandleTableViewer properties
PropertyHandleTableViewer.Button.New=&Nuevo...

PropertyHandleTableViewer.Button.Edit=&Editar...

PropertyHandleTableViewer.Button.Remove=&Quitar

PropertyHandleTableViewer.Button.Up=A&rriba

PropertyHandleTableViewer.Button.Down=&Abajo

PropertyHandleTableViewer.Menu.Remove=Quitar

PropertyHandleTableViewer.Menu.Up=Arriba

PropertyHandleTableViewer.Menu.Down=Abajo

PropertyHandleTableViewer.Menu.RemoveAll=Quitar todos

###########################################################
#PropertyHandleInputDialog properties
PropertyHandleInputDialog.Action.New=Nuevo

PropertyHandleInputDialog.Action.Edit=Editar

PropertyHandleInputDialog.messages.error.blankProperty={0} no puede estar en blanco

###########################################################
#PropertyAndPreferencePage properties
PropertyAndPreferencePage.Text.Configure.Special.Settings=Configurar ajustes específicos de proyecto...

PropertyAndPreferencePage.Text.Configure.Workspace.Settings=Configurar ajustes del área de trabajo...

PropertyAndPreferencePage.Text.Enable.SpecialSettings=Activar ajustes específicos de pr&oyecto

###########################################################
#PublishTemplateAction properties
PublishTemplateAction.wizard.page.browse.cheatsheet.title=Seleccione un archivo de consejos útiles

PublishTemplateAction.wizard.page.browse.title=Seleccionar un archivo de imagen

PublishTemplateAction.wizard.page.btn.browse=E&xaminar...

PublishTemplateAction.wizard.page.cheatsheetError=El archivo de consejos útiles ''{0}'' no existe.

PublishTemplateAction.wizard.page.desc=Registrar platilla en la carpeta de plantillas.

PublishTemplateAction.wizard.page.imageError=El archivo de imagen ''{0}'' no existe.

PublishTemplateAction.wizard.page.label.cheatsheet=Archivo de consejos útiles:

PublishTemplateAction.wizard.page.label.description=&Descripción:

PublishTemplateAction.wizard.page.label.displayName=Nombre en pa&ntalla:

PublishTemplateAction.wizard.page.label.image=Imagen de &plantilla:

PublishTemplateAction.wizard.page.title=Publicar plantilla

PublishTemplateAction.wizard.title=Publicar plantilla

PublishTemplateAction.wizard.errorTitle=Error

PublishTemplateAction.wizard.message=No es posible publicar la plantilla en el directorio donde se encuentra, seleccione otro directorio para la publicación.

PublishTemplateAction.wizard.msgDirErr=El directorio de destino no existe y no se puede crear.

PublishTemplateAction.wizard.notvalidfolder=La carpeta de plantillas definida como preferencia no es válida.

PublishTemplateAction.wizard.page.nameInfo=Es necesario un nombre en pantalla

PublishTemplateAction.wizard.message.SourceFileNotExist=El archivo fuente no existe.

PublishTemplateAction.wizard.message.ThumbnailImageNotExist=La imagen en miniatura no existe.

PublishTemplateAction.wizard.message.PreviewImageNotExist=El archivo de vista previa de imagen no existe.

PublishTemplateAction.wizard.message.PreviewImageNotValid=El archivo de vista previa no es una imagen válida.

PublishTemplateAction.SaveBeforeGenerating.dialog.title=Guardar informe

PublishTemplateAction.SaveBeforeGenerating.dialog.message={0} se ha modificado. ¿Guardar cambios antes de publicar?

PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes=&Sí

PublishTemplateAction.SaveBeforeGenerating.dialog.button.no=&No
###########################################################
#PublishLibraryAction properties
PublishLibraryAction.wizard.errorTitle=Error

PublishLibraryAction.wizard.message=No es posible publicar la librería en el directorio donde se encuentra, seleccione otro directorio para la publicación.

PublishLibraryAction.wizard.msgDirErr=El directorio de destino no existe y no se puede crear.

PublishLibraryAction.wizard.message.SourceFileNotExist=El archivo fuente no existe.

PublishLibraryAction.wizard.notvalidfolder=La carpeta de librerías definida como preferencia no es válida.

###########################################################
#RedoAction properties
RedoAction.Tooltip=Rehacer {0}

###########################################################
#ExportReportWizard
ExportToLibraryAction.wizard.windowTitle=Exportar

ExportToLibraryAction.wizard.page.title=Exportar el archivo a la librería

ExportToLibraryAction.wizard.page.desc=Exportar el diseño de informe a la librería

ExportToLibraryAction.wizard.page.label.filename=&Nombre de archivo:

ExportToLibraryAction.wizard.page.label.folder=&Carpeta:

ExportToLibraryAction.wizard.page.button.browser=&Examinar...

ExportToLibraryAction.wizard.page.dirdialog.message=Seleccionar el directorio de los contenidos de archivo

ExportToLibraryAction.wizard.page.nameempty=Se debe especificar un nombre de archivo.

ExportToLibraryAction.wizard.page.folderempty=Se debe especificar una carpeta.

ExportToLibraryAction.wizard.page.foldererror=La carpeta especificada no existe

###########################################################
#PublishLibraryToResourceFolderAction
PublishLibraryToResourceFolderAction.Action.Text=Copiar Li&brería para compartir carpeta de recursos...

###########################################################
#PublishTemplateViewAction
DefaultNodeProvider.template.suffix=.rpttemplate
PublishTemplateViewAction.action.displaytext=Registrar plantilla con el as&istente de nuevos informes...

###########################################################
#RefreshModuleHandleAction
RefreshModuleHandleAction.Action.Text=Actualizar &librería

RefreshModuleHandleAction.MessageBox.Title=Actualizar librería

RefreshModuleHandleAction.MessageBox.Text=El diseño actual tiene cambios no guardados. ¿Desea guardar estos cambios primero?

RefreshModuleHandleAction.MessageBox.SaveButton=&Guardar

RefreshModuleHandleAction.MessageBox.DiscardButton=&Desechar

RefreshModuleHandleAction.MessageBox.CancelButton=Cancelar
###########################################################
#ReloadCssStylesAction
ReloadCssStylesAction.Action.Text=Volve&r a cargar el archivo CSS

###########################################################
#ReferencePage properties
ReferencePage.Button.Edit=E&ditar...

ReferencePage.Label.Source=Fu&ente:

###########################################################
#RefreshAction properties
RefreshAction.text=Ac&tualizar

###########################################################
#RefreshLibExplorerAction properties
RefreshLibExplorerAction.Text=Ac&tualizar

###########################################################
#RemoveLibraryAction properties
RemoveLibraryAction.Text=Quitar li&brería

RemoveLibraryAction.config.Title=Confirmar eliminación de librería

RemoveLibraryAction.config.Message=¿Desea eliminar esta librería?

###########################################################
#UseCssInReportDesignAction properties
UseCssInReportDesignAction.Text=Utilizar CSS en el diseño de informe...

###########################################################
#UseCssInReportDesignAction properties
UseCssInThemeAction.Text=Utilizar CSS en librería...

###########################################################
#RenameAction properties
RenameAction.text=Ca&mbiar nombre

###########################################################
#RenameDialog properties
RenameDialog.action.Cancel=Cancelar

RenameDialog.action.Ok=\\ \\ Aceptar

RenameDialog.text.NewName=Nuevo nombre:

RenameDialog.text.Rename=Cambiar nombre

RenameDialog.text.ToolTip=El nombre no puede estar vacío


###########################################################
#ExportElementToLibraryAction properties
ExportElementToLibraryAction.SetNameAction.text=Establecer nombre

ExportElementToLibraryAction.DialogTitle.setNameFailed=Error al establecer nombre

ExportElementToLibraryAction.TransLabel.Setname=Establecer nombre

ExportElementToLibraryAction.DialogTitle=Establecer nombre

ExportElementToLibraryAction.DialogMessage=Es posible establecer un nombre para un elemento seleccionado antes de exportarlo. En caso de que no se especifique, se utilizará un nombre predeterminado.


###########################################################
#RenameInlineTool properties
RenameInlineTool.DialogTitle.RenameFailed=Error al cambiar el nombre

RenameInlineTool.TransLabel.Rename=Cambiar nombre

###########################################################
#RenameInputDialog properties
RenameInputDialog.DialogTitle=Cambiar nombre de la entrada

RenameInputDialog.DialogMessage=Introduzca el nuevo nombre

###########################################################
#report properties
report.designer.ui.preferences.buttonadd=Añad&ir...

report.designer.ui.preferences.buttonremove=&Quitar

report.designer.ui.preferences.buttonremoveall=Quitar &todos

report.designer.ui.preferences.dialogtitle=Abrir una librería

report.designer.ui.preferences.errormessage=La librería ya existe.

report.designer.ui.preferences.errortitle=Error

report.designer.ui.preferences.extensions=*.rptlibrary
report.designer.ui.preferences.labeltitle=Añadir/quitar librerías

report.designer.ui.preferences.library.export.overwrite.always=Sie&mpre

report.designer.ui.preferences.library.export.overwrite.never=&Nunca

report.designer.ui.preferences.library.export.overwrite.prompt=&Solicitar

report.designer.ui.preferences.library.export.title=Sobrescribir elemento existente al exportar el elemento de diseño a la librería.

report.designer.ui.preferences.template.openDialogTitle=Selección de carpeta

report.designer.ui.preferences.template.select=Sel&eccionar...

report.designer.ui.preferences.template.templatedir=plantillas

report.designer.ui.preferences.template.templatefolder=&Carpeta de plantillas

report.designer.ui.preferences.template.title=Seleccione la carpeta de plantillas.

org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment=&Generar comentario al crear un diseño de informe

org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment=Copyright (c) 2008 <<ponga el nombre de su empresa aquí>>
#bidi_hcg start
report.designer.ui.preferences.bidiproperties.defaultdirection=Orientación del in&forme predeterminado:

report.designer.ui.preferences.bidiproperties.ltrdirection=De izquierda a derecha

report.designer.ui.preferences.bidiproperties.rtldirection=De derecha a izquierda
#bidi_hcg end
###########################################################
#Resource Preference page properties
ResourecePreferencePage.title=Escriba o seleccione la carpeta de recursos predeterminada que desee utilizar para el explorador de librería, imágenes,\narchivos de propiedad, etc.

ResourecePreferencePage.folder=Carpeta de &recursos

ResourecePreferencePage.select=Sel&eccionar...

ResourecePreferencePage.openDialogTitle=Buscar carpeta

ResourecePreferencePage.openDialogMessage=Seleccione la carpeta de recursos predeterminada:

ResourecePreferencePage.defaultResourceFolder.dispaly=<Carpeta de proyecto actual>

###########################################################
#ReportAdvancedLauncherTab properties
ReportAdvancedLauncherTab.DefaultsButtonText=&Restaurar predeterminados

ReportAdvancedLauncherTab.DeselectButtonText=&Anular la selección de todo

ReportAdvancedLauncherTab.Name=Proyectos de informe

ReportAdvancedLauncherTab.SelectAllButtonText=&Seleccionar todo

ReportAdvancedLauncherTab.UseListRadioText=Seleccionar proyectos para iniciar de la lista

ReportAdvancedLauncherTab.VisibleLabelText=&Proyectos disponibles

ReportAdvancedLauncherTab.WorkspaceReportProjects=proyectos de informe de área de trabajo

###########################################################
#ReportClasspathLaucnTab properties
ReportClasspathLaucnTab.DefaultButtonText=&Restaurar predeterminados

ReportClasspathLaucnTab.DeselectButtonText=&Anular la selección de todo

ReportClasspathLaucnTab.Name=Proyectos Java

ReportClasspathLaucnTab.SelectAllButtonText=&Seleccionar todo

ReportClasspathLaucnTab.UseListRadioText=Elija el proyecto que desea iniciar de la lista, la carpeta de salida de clase del proyecto de selección se añadirá a la ruta de clase

ReportClasspathLaucnTab.VisibleLabelText=&Proyectos disponibles

ReportClasspathLaucnTab.WorkspaceJavaProjects=proyectos java de área de trabajo

###########################################################
#ReportCreationTool properties
ReportCreationTool.ModelTrans.CreateElement=Insertar elemento

###########################################################
#ReportEditor properties
ReportEditor.Button.Close=Cerrar

ReportEditor.Button.Save=Guardar

ReportEditor.Dlg.Confirm=El archivo se ha eliminado del sistema. ¿Desea guardar los cambios o cerrar el editor sin guardar?

ReportEditor.Dlg.Save=Guardar...

ReportEditor.error.activated.outofsync.message=El archivo se ha cambiado en el sistema de archivos. ¿Desea cargar los cambios?

ReportEditor.error.activated.outofsync.title=Archivo cambiado

ReportEditor.Error.Open=Error de apertura

ReportEditor.Error.OpenEditor=No se puede abrir el archivo.

ReportEditor.Page.Code=Script

ReportEditor.Page.Layout=Diseño

ReportEditor.Page.MasterPage=MasterPage
ReportEditor.Page.Preview=Vista previa

ReportEditor.Page.PreviewSaveMessage=Se ha modificado el archivo de informe. ¿Desea guardarlo antes de la vista previa?

ReportEditor.Page.PreviewSaveTitle=Guardar

ReportEditor.Page.PreviewSaveToggle=Guarde siempre el archivo antes de la vista previa.

###########################################################
#ReportLaunchConfigurationDelegate properties
ReportLaunchConfigurationDelegate.badFeatureSetup=Configuración de función incorrecta

ReportLaunchConfigurationDelegate.noStartup=Sin inicio

###########################################################
#ReportPage properties
ReportPage.text.Add=&Añadir...

ReportPage.text.Browse=&Examinar...

ReportPage.title.Location=Ubicación:

ReportPage.title.setPrewImg=Seleccionar un archivo de imagen

ReportPage.text.Resource.Add=&Examinar...

ReportPage.text.Thumbnail=&Miniaturas...

###########################################################
#ReportPageGenerator properties
ReportPageGenerator.List.Comments=Comentarios

ReportPageGenerator.List.Description=Descripción

ReportPageGenerator.List.EventHandler=Controlador de evento

ReportPageGenerator.List.General=General

ReportPageGenerator.List.Measurement=Medida

ReportPageGenerator.List.NamedExpressions=Expresiones con nombre

ReportPageGenerator.List.UserProperties=Propiedades de usuario

ReportPageGenerator.List.Resources=Recursos

ReportPageGenerator.List.Resources.JarFile=Archivos Jar:

ReportPageGenerator.List.AdvancedProperty=Avanzada

#OdaDataSourceGenerator properties
DataSourcePageGenerator.List.General=General

#OdaDataSetGenerator properties
DataSetPageGenerator.List.General=General

DataSetPageGenerator.DataSourceName=Fuente de dat&os

#ScalarParameterPageGenerator properties
ScalarParameterPageGenerator.List.General=General

ScalarParameterPageGenerator.List.I18n=Localización

#TemplateReportItemPageGenerator properties
TemplateReportItemPageGenerator.List.General=General

TemplateReportItemPage.description.Label.Instructions=&Instrucciones:

TemplateReportItemPageGenerator.List.I18n=Localización

TemplateReportItemPageGenerator.List.TextKey=Clave de texto:

###########################################################
#ReportPropertySheetPage properties
ReportPropertySheetPage.Column.Title.Property=Propiedad

ReportPropertySheetPage.Column.Title.Value=Valor

ReportPropertySheetPage.Root.Default.Title=Información

ReportPropertySheetPage.Tooltip.Alphabetic=Ordenar alfabéticamente

ReportPropertySheetPage.Tooltip.Group=Ordenar por grupo

ReportPropertySheetPage.Tooltip.Local=Mostrar propiedades locales

ReportPropertySheetPage.Value.Inherited=Heredado

###########################################################
#ReportTemplateTransferDropTargetListener properties
ReportTemplateTransferDropTargetListener.ModelTrans.CreateElement=Crear elemento

ReportTemplateTransferDropTargetListener.transLabel.createElement=Insertar elemento

###########################################################
#ResourceEditDialog properties
ResourceEditDialog.message.AddNote=Añadir rápido guardará la clave en el archivo de recursos actual.

ResourceEditDialog.message.ResourceFile=Archivo de recursos:

ResourceEditDialog.text.Add=&Añadir

ResourceEditDialog.text.AddWarningMsg=No se permiten claves en blanco.

ResourceEditDialog.text.AddWarningTitle=Advertencia

ResourceEditDialog.text.Delete=&Eliminar

ResourceEditDialog.text.Key.TableColumn=Clave

ResourceEditDialog.text.Key=&Clave

ResourceEditDialog.text.QuickAdd=Añadir rápido

ResourceEditDialog.text.Value.TableColumn=Valor

ResourceEditDialog.text.Value=&Valor

###########################################################
#ResourceKeyDescriptor properties
ResourceKeyDescriptor.text.Browse=&Examinar...

ResourceKeyDescriptor.text.Browse.Alt=E&xaminar...

ResourceKeyDescriptor.text.Reset=&Restablecer

ResourceKeyDescriptor.text.Reset.Alt=R&establecer

ResourceKeyDescriptor.title.SelectKey=Seleccionar clave

ResourceKeyDescriptor.button.reset.tooltip=Reiniciar la clave

ResourceKeyDescriptor.button.browse.tooltip=Examinar y seleccionar una clave

ResourceKey.invalid.containSpace=Clave de nombre en pantalla no válida, no debe contener ningún espacio en blanco incrustado
###########################################################
#RevertToReportItemAction properties
RevertToReportItemAction.text=&Volver a elemento de informe

###########################################################
#RevertToTemplateAction properties
RevertToTemplateAction.text=&Volver a elemento de plantilla

###########################################################
#RowHandle properties
RowHandle.tooltip.GridRow=Cuadrícula - Fila

RowHandle.tooltip.GroupFooter=Grupo de tabla - Pie de página

RowHandle.tooltip.GroupHeader=Grupo de tabla - Encabezado

RowHandle.tooltip.TableDetail=Tabla - Detalle

RowHandle.tooltip.TableFooter=Tabla - Pie de página

RowHandle.tooltip.TableHeader=Tabla - Encabezado

###########################################################
#RowHandleAdapter properties
RowHandleAdapter.table.detail=Fila de detalle

RowHandleAdapter.table.footer=Fila de pie de página

RowHandleAdapter.table.groupFooter=Fila de pie de grupo

RowHandleAdapter.table.groupHeader=Fila de encabezado de grupo

RowHandleAdapter.table.header=Línea de cabecera

###########################################################
#RowPageGenerator properties
RowPageGenerator.List.Bookmark=Marcador

RowPageGenerator.List.Borders=Borde

RowPageGenerator.List.Section=Salto de página

RowPageGenerator.List.CellPadding=Relleno de celdas

RowPageGenerator.List.Font=Fuente

RowPageGenerator.List.General=General

RowPageGenerator.List.Visibility=Visibilidad

###########################################################
#RowProvider properties
RowProvider.action.text.above=Insertar fila &encima

RowProvider.action.text.below=Insertar fila &debajo

###########################################################
#SaveAsDialog properties
SaveAsDialog.message=Guardar archivo en otra ubicación.

SaveAsDialog.overwriteQuestion=El archivo ''{0}'' ya existe. ¿Desea reemplazar el archivo existente?

SaveAsDialog.Question=Pregunta

SaveAsDialog.text=Guardar como

SaveAsDialog.title=Guardar como

###########################################################
#CreateDirectory properties
DirectoryDialog.createQuestion=El directorio ''{0}'' no existe. ¿Desea crearlo?

DirectoryDialog.Question=Pregunta

###########################################################
#SchematicContextMenuProvider properties
SchematicContextMenuProvider.ActionText.editData=Editar &valor/expresión...

SchematicContextMenuProvider.ActionText.editLabel=&Editar

SchematicContextMenuProvider.label.ResetSize=Restablecer tamaño de &imagen

SchematicContextMenuProvider.label.ResetToImageDPI=Mejor resolución de &imagen de ajuste

SchematicContextMenuProvider.label.ResetToReportDPI=Mejor resolución &de informe de ajuste

SchematicContextMenuProvider.label.ResetToScreenDPI=Mejor resolución de &pantalla de ajuste

SchematicContextMenuProvider.label.RestToOrginalPixels=En &píxeles originales

SchematicContextMenuProvider.Menu.EditGroup=Editar &grupo

SchematicContextMenuProvider.Menu.Insert=In&sertar

SchematicContextMenuProvider.Menu.insertElement=I&nsertar

SchematicContextMenuProvider.Menu.insertRow=&Fila

SchematicContextMenuProvider.Menu.List=Lista

SchematicContextMenuProvider.Menu.Show=Mostrar

SchematicContextMenuProvider.Menu.Style=Est&ilo

SchematicContextMenuProvider.Menu.Style.Apply=A&plicar estilo

SchematicContextMenuProvider.Menu.Theme=Te&ma

SchematicContextMenuProvider.Menu.Theme.Apply=A&plicar tema

SchematicContextMenuProvider.Menu.Layout=P&referencia de diseño
#SchematicContextMenuProvider.menutitle.applystylerule=Apply Style
#SchematicContextMenuProvider.menutitle.applythemerule=&Apply Theme
#SchematicContextMenuProvider.menutitle.editgroup=Edit Group
#SchematicContextMenuProvider.menutitle.insert=Insert
#SchematicContextMenuProvider.menutitle.list=List
#SchematicContextMenuProvider.menutitle.show=Show
#SchematicContextMenuProvider.menutitle.style=Style
SchematicContextMenuProvider.Menu.EditStyle=Edi&tar estilo

SchematicContextMenuProvider.Menu.DeleteStyle=Quitar e&stilo

SchematicContextMenuProvider.Menu.DeleteGroup=E&liminar grupo

SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter=Insertar e&ncabezado/pie de página de grupo

SchematicContextMenuProvider.Menu.NewStyle=Nuevo Est%ilo

###########################################################
#ApplyLayoutPreferenceAction properties
ApplyLayoutPreferenceAction.autoLayout=Diseño a&utomático

ApplyLayoutPreferenceAction.fixedLayout=Diseño &fijo

###########################################################
#SectionProcess properties
SectionProcess.action.Clear=Borrar

SectionProcess.action.Delete=Eliminar

SectionProcess.action.DeleteRow=Eliminar fila

SectionProcess.action.New=Nuevo

SectionProcess.action.NewCell=Nueva celda

SectionProcess.action.NewGroup=Nuevo grupo

SectionProcess.action.NewRow=Nueva fila

SectionProcess.action.NewSection=Nueva sección

SectionProcess.action.NewSubSection=Nueva subsección

SectionProcess.displayname.GridProcess=Cuadrícula

SectionProcess.displayname.GroupProcess=Grupo

SectionProcess.displayname.ListProcess=Lista

SectionProcess.displayname.TableProcess=Tabla

SectionProcess.displayname.TableRowProcess=Fila

SectionProcess.type.Container=(Contenedor)

SectionProcess.type.Data=(Datos)

SectionProcess.type.Image=(Imagen)

SectionProcess.type.Label=(Etiqueta)

SectionProcess.type.List=(Lista)

SectionProcess.type.Prefix=Columna

SectionProcess.type.Table=(Tabla)

SectionProcess.type.Text=(Texto)

###########################################################
#SelectAllAction properties
SelectAllAction.text=Seleccionar &todo

###########################################################
#SelectCssStyleWizard properties
SelectCssStyleWizard.wizard.title=Importar estilos CSS

SelectCssStyleWizard.wizardPage.description.report=Seleccione un archivo CSS e importe estilos a un diseño de informe.

SelectCssStyleWizard.wizardPage.description.library=Seleccione un archivo CSS e importe estilos a la librería.

SelectCssStyleWizard.wizardPage.name=Seleccionar estilos Css

SelectCssStyleWizard.wizardPage.title=Importar estilo desde un archivo CSS

###########################################################
#UseCssInReportDialog properties
UseCssInReportDialog.Wizard.Title=Utilizar CSS

UseCssInReportDialog.Wizard.Filename=&Nombre de archivo:

UseCssInReportDialog.Label.Styles=&Estilos disponibles de {0}

UseCssInReportDialog.Label.notifications=&Notificaciones

UseCssInReportDialog.TitleArea.Title=Utilizar el archivo CSS en el diseño de informes

UseCssInReportDialog.TitleArea.Message=Seleccionar un archivo CSS de la carpeta compartida de librerías y utilizar sus estilos en el diseño de informes.

UseCssInReportDialog.Label.No.File=No se ha seleccionado ningún archivo CSS

UseCssInReportDialog.Dialog.Browse=Examinar

UseCssInReportDialog.Dialog.Browse.Title=Seleccionar un archivo CSS para incluirlo en el informe

UseCssInReportDialog.Dialog.Browse.Library.Title=Seleccionar un archivo CSS para incluirlo en la librería

UseCssInReportDialog.Error.Already.Include={0} ya se ha incluido

UseCssInReportDialog.Dialog.Button.viewTimeBtn.Text=&Incluir el archivo CSS en el momento de la visualización (sólo en formato HTML)

UseCssInReportDialog.Dialog.Label.viewTimeLb=Visualizar la ubicación temporal del archivo CSS:

UseCssInReportDialog.Dialog.Label.example=por ejemplo, http://mydomain/test.css, myfolder/test/css

UseCssInReportDialog.Dialog.Label.Theme.Text=&Tema:

UseCssInReportDialog.Dialog.Text.uri=&URI:
UseCssInThemeDialog.TitleArea.Title=Utilizar el archivo CSS en la librería de informes

UseCssInThemeDialog.TitleArea.Message=Seleccionar un tema al que aplicar los estilos del archivo CSS.

CssStyleSheetNodeProvider.Label.FromServer=Del servidor

CssStyleSheetNodeProvider.Tooltip.URI=(Aplicado en el momento de la visualización)


###########################################################
#SelectValueDialog properties
SelectValueDialog.errorRetrievinglist=Error al recuperar la lista

SelectValueDialog.messages.info.selectVauleUnavailable=La lista de valores no está disponible.

SelectValueDialog.messages.info.invalidSelectVauleExpression=La función "Seleccionar valor" sólo acepta una columna de expresión simple. Se rechazan las expresiones complejas.

SelectValueDialog.messages.warning.BlobColumn.unsupported=No se puede recuperar la lista de valores de la expresión referenciada para enlace/columna de tipo Blob.

SelectValueDialog.messages.error.selectVauleUnavailable=No se puede recuperar el valor de la expresión.

SelectValueDialog.messages.error.selectValueNotSupported=No se admite este tipo de selección de valor.

SelectValueDialog.noExpressionSet=No se ha ajustado ninguna expresión

SelectValueDialog.retrieving=Recuperando lista...

SelectValueDialog.selectValue=Seleccionar valores

SelectValueDialog.SelectValue.NullValue=<Valor nulo>

###########################################################
#SetConstraintCommand properties
SetConstraintCommand.Error.SetConstraintError=Se ha producido un error al establecer una restricción en el modelo.

SetConstraintCommand.transLabel.setConstraint=Establecer restricción

SetConstraintCommand.commandLabel.ResizeCommand=Cambiar tamaño
###########################################################
#ProjectFilterAction properties
ProjectFilterAction.text=Filtrar...

###########################################################
#ResourceFileFolderSelectionAction properties
ResourceFileFolderSelectionAction.text=Filtrar...

###########################################################
#ResourceFileFolderSelectionDialog properties
ResourceFileFolderSelectionDialog.Text.Menu=menú

ResourceFileFolderSelectionDialog.button.importFile=Importar archivo...

ResourceFileFolderSelectionDialog.title.error=Error

ResourceFileFolderSelectionDialog.title.overwrite=Sobrescribir

ResourceFileFolderSelectionDialog.overwrite.msg=Ya existe un archivo con este nombre, ¿desea sobrescribirlo?

ResourceFileFolderSelectionDialog.import.msg=Importando archivo...

###########################################################
#ShowPropertyAction properties
ShowPropertyAction.text=P&ropiedades

###########################################################
#SortingHandleProvider properties
SortingHandleProvider.Label.SortOn=Ordenar según:

###########################################################
#Spinner properties
Spinner.DialogTitle.InvalidNumber=Número no válido

###########################################################
#SplitAction properties
SplitAction.actionMsg.splitCells=&Dividir celda

###########################################################
#StyleBuilder properties
StringVariableSelectionDialog.Dialog.Label=&Descripción de la variable:

StringVariableSelectionDialog.Dialog.Prompt=&Seleccionar una variable (? = cualquier carácter, * = cualquier string):

StringVariableSelectionDialog.Dialog.Title=Seleccionar variable

StyleBuilder.action.Apply=&Aplicar

StyleBuilder.Edit.Info=Modificar las propiedades de estilo deseadas. Todos los elementos de informe que utilizan este estilo se actualizan automáticamente para reflejar el nuevo formato.

StyleBuilder.New.Info=Para crear un estilo nuevo, se debe seleccionar una categoría de propiedades en la parte izquierda y especificar los valores de las propiedades.

StyleBuilder.Title=Estilo

SytleBuilder.DialogTitle.New=Nuevo estilo

SytleBuilder.DialogTitle.Edit=Editar estilo

###########################################################
#StyleComboProvider properties
StyleComboProvider.LineStyle.None=Sin estilo

###########################################################
#StyleNodeProvider properties
StyleNodeProvider.action.Edit=&Editar estilo...

StyleNodeProvider.action.New=&Nuevo estilo...

###########################################################
#StyleProcess properties
StyleProcess.action.Delete=Quitar estilo

StyleProcess.action.Edit=&Editar estilo...

StyleProcess.action.New=&Nuevo estilo...

StyleProcess.text.CannotDelete=No se puede quitar el estilo predeterminado.

StyleProcess.text.Show=Muestra el editor de conjunto de datos para el conjunto de datos.

###########################################################
#StylesNodeProvider properties
StylesNodeProvider.action.New=&Nuevo estilo...

###########################################################
#StylesOutlineProcess properties
StylesOutlineProcess.action.New=&Nuevo estilo...

###########################################################
#TableBandProvider properties
TableBandProvider.action.text.group=I&nsertar grupo

TableBandProvider.action.text.row=In&sertar fila

###########################################################
#TableEditPart properties
TableEditPart.GUIDEHANDLE_TEXT =Tabla

TableEditPart.Label.Merge=Combinar celdas

TableEditPart.Label.ResizeColumn=Cambiar tamaño de columna

TableEditPart.Label.ResizeRow=Cambiar tamaño de fila

###########################################################
#TableFigure properties
TableFigure.BORDER_TEXT=Tabla

TableArea.Button.New=Nue&vo...

TableArea.Button.Edit=&Editar...

TableArea.Button.Remove=&Quitar

TableArea.Button.Down=&Abajo

TableArea.Button.RemoveAll=Qui&tar todos

###########################################################
#TableHandleAdapter properties
TableHandleAdapter.name.detail=detalle

TableHandleAdapter.name.footer=pie de página

TableHandleAdapter.name.header=encabezado

TableHandleAdapter.transLabel.deleteColumn=Eliminar columna

TableHandleAdapter.transLabel.deleteColumns=Eliminar columnas

TableHandleAdapter.transLabel.deleteRow=Eliminar fila

TableHandleAdapter.transLabel.deleteRows=Eliminar filas

TableHandleAdapter.transLabel.include=Incluir

TableHandleAdapter.transLabel.insertColumn=insertar columna

TableHandleAdapter.transLabel.insertGroup=insertar grupo

TableHandleAdapter.transLabel.insertRow=insertar fila

TableHandleAdapter.transLabel.notInclude=Excluir

TableHandleAdapter.transLabel.splitCells=Dividir celda

TableHandleAdapter.transLable.deleteGroup=Eliminar grupo de tablas

###########################################################
#TableOptionDialog properties
TableOptionDialog.message.RememberGrid=&Utilizar estas dimensiones para nuevas cuadrículas.

TableOptionDialog.message.RememberTable=U&tilizar estas dimensiones para nuevas tablas.

TableOptionDialog.text.Column=Número de columna&s:

TableOptionDialog.text.GridRow=Número de &filas:

TableOptionDialog.text.GridSize=Tamaño de cuadrícula

TableOptionDialog.text.TableDetail=Número de deta&lles:

TableOptionDialog.text.TableSize=Tamaño de tabla

TableOptionDialog.title.InsertGrid=Insertar cuadrícula

TableOptionDialog.title.InsertTable=Insertar tabla

TableOptionDialog.text.DataSet=Conjunto de d&atos:

TableOptionDialog.text.AutoSummary=Resumen &automático activado

TableOptionPage.Title=Insertar tabla

###########################################################
#TablePageGenerator properties
TablePageGenerator.List.Bookmark=Marcador

TablePageGenerator.List.Borders=Borde

TablePageGenerator.List.CellPadding=Relleno de celdas

TablePageGenerator.List.Font=Fuente

TablePageGenerator.List.General=General

TablePageGenerator.List.Marign=Margen

TablePageGenerator.List.Section=Salto de página

TablePageGenerator.List.TOC=Tabla de contenido

TablePageGenerator.List.Visibility=Visibilidad

###########################################################
#TemplateReportItemPropertiesDialog properties
TemplateReportItemPropertiesDialog.errorMessage=El texto de solicitud no puede estar vacío.

TemplateReportItemPropertiesDialog.errorTitle=Error

TemplateReportItemPropertiesDialog.group=Propiedades de elemento de informe de plantilla

TemplateReportItemPropertiesDialog.objectType=Tipo de elemento:

TemplateReportItemPropertiesDialog.nameText=&Nombre:

TemplateReportItemPropertiesDialog.promptText=&Instrucciones:

TemplateReportItemPropertiesDialog.title=Crear elemento de informe de plantilla

###########################################################
#performCreatePlaceHolder action
performCreatePlaceHolder.errorMessage.title=Error

performCreatePlaceHolder.errorMessage.content=El nombre especificado existe, seleccione otro o deje el espacio en blanco.

###########################################################
#TogglePropertyDescriptor properties
TogglePropertyDescriptor.toolTipText.Bold=Negrita

TogglePropertyDescriptor.toolTipText.Italic=Cursiva

TogglePropertyDescriptor.toolTipText.Underline=Subrayado

TogglePropertyDescriptor.toolTipText.Text_Line_Through=Línea a través del texto

###########################################################
#TextAlignPropertyDescriptor properties
TextAlignPropertyDescriptor.0=Izquierda

TextAlignPropertyDescriptor.1=Centro

TextAlignPropertyDescriptor.2=Derecha

TextAlignPropertyDescriptor.3=Justificado

###########################################################
#TextAreaContextMenu properties
TextAreaContextMenu.Copy.description=Copiar

TextAreaContextMenu.Copy.image=

TextAreaContextMenu.Copy.label=&Copiar

TextAreaContextMenu.Copy.tooltip=Copiar

TextAreaContextMenu.Cut.description=Cortar

TextAreaContextMenu.Cut.image=

TextAreaContextMenu.Cut.label=Cor&tar

TextAreaContextMenu.Cut.tooltip=Cortar

TextAreaContextMenu.Paste.description=Pegar

TextAreaContextMenu.Paste.image=

TextAreaContextMenu.Paste.label=&Pegar

TextAreaContextMenu.Paste.tooltip=Pegar

TextAreaContextMenu.Redo.description=Rehacer

TextAreaContextMenu.Redo.image=

TextAreaContextMenu.Redo.label=&Rehacer

TextAreaContextMenu.Redo.tooltip=Rehacer

TextAreaContextMenu.SelectAll.description=Seleccionar todo

TextAreaContextMenu.SelectAll.image=

TextAreaContextMenu.SelectAll.label=Selecci&onar todo

TextAreaContextMenu.SelectAll.tooltip=Seleccionar todo

TextAreaContextMenu.Undo.description=Deshacer

TextAreaContextMenu.Undo.image=

TextAreaContextMenu.Undo.label=&Deshacer

TextAreaContextMenu.Undo.tooltip=Deshacer

###########################################################
#TextDataEditPart properties
TextDataEditPart.Figure.Dafault=Texto dinámico

###########################################################
#TextEditDialog properties
TextEditDialog.action.text.direction=&Sentido de lectura de derecha a izquierda

TextEditDialog.action.text.editDynamicText=Editar te&xto dinámico

TextEditDialog.action.text.formatDateTime=DateTim&e de formato

TextEditDialog.action.text.formatNumber=&Formato de número

TextEditDialog.action.text.formatString=Strin&g de formato

TextEditDialog.action.text.formatHTML=Formato &HTML

TextEditDialog.choice.HTML=Texto HTML/dinámico

TextEditDialog.choice.plainText=Texto sin formato

TextEditDialog.choice.Auto=Automático

TextEditDialog.comboText.formattingAndLayout=Formato y diseño

TextEditDialog.format.dynamicText=Texto dinámico

TextEditDialog.format.formatting=Formato y diseño

TextEditDialog.format.lists=Listas

TextEditDialog.format.pageComposition=Composición de página

TextEditDialog.format.pageElements=Elementos de página

TextEditDialog.formatChoice.content=Contenido

TextEditDialog.formatChoice.dynamicText=Texto dinámico

TextEditDialog.formatChoice.formatting=Formato

TextEditDialog.formatChoice.layout=Diseño

TextEditDialog.formatChoice.lists=Listas

TextEditDialog.toolTip.tag.a=Delimitador

TextEditDialog.toolTip.tag.b=Estilo de texto negrita

TextEditDialog.toolTip.tag.birtImage=Imagen específica de BIRT

TextEditDialog.toolTip.tag.br=Interrupción de línea

TextEditDialog.toolTip.tag.center=Centra el texto

TextEditDialog.toolTip.tag.code=Fragmento de código informático

TextEditDialog.toolTip.tag.dd=Descripción de definición

TextEditDialog.toolTip.tag.del=Texto eliminado

TextEditDialog.toolTip.tag.dl=Lista de definición

TextEditDialog.toolTip.tag.dt=Término de definición

TextEditDialog.toolTip.tag.em=Énfasis

TextEditDialog.toolTip.tag.font=Cambio local a fuente

TextEditDialog.toolTip.tag.genericStyleContainer=Contenedor de estilo/idioma genérico

TextEditDialog.toolTip.tag.h1=Nivel de encabezado 1

TextEditDialog.toolTip.tag.h2=Nivel de encabezado 2

TextEditDialog.toolTip.tag.h3=Nivel de encabezado 3

TextEditDialog.toolTip.tag.h4=Nivel de encabezado 4

TextEditDialog.toolTip.tag.h5=Nivel de encabezado 5

TextEditDialog.toolTip.tag.h6=Nivel de encabezado 6

TextEditDialog.toolTip.tag.i=Estilo de texto en cursiva

TextEditDialog.toolTip.tag.img=Referencia de imagen HTML estándar

TextEditDialog.toolTip.tag.ins=Texto insertado

TextEditDialog.toolTip.tag.li=Elemento de la lista

TextEditDialog.toolTip.tag.p=Párrafo

TextEditDialog.toolTip.tag.pre=Texto con formato previo

TextEditDialog.toolTip.tag.strong=Énfasis fuerte

TextEditDialog.toolTip.tag.sub=Subíndice

TextEditDialog.toolTip.tag.sup=Superíndice

TextEditDialog.toolTip.tag.tt=Estilo de texto espacio sencillo o teletipo

TextEditDialog.toolTip.tag.u=Estilo de texto subrayado

TextEditDialog.toolTip.tag.ul=Lista desordenada

TextEditDialog.toolTip.valueOf=Valor de

TextEditDialog.toolTipText.copy=Copiar

TextEditDialog.toolTipText.cut=Cortar

TextEditDialog.toolTipText.delete=Eliminar

TextEditDialog.toolTipText.paste=Pegar

TextEditDialog.toolTipText.redo=Rehacer

TextEditDialog.toolTipText.undo=Deshacer

TextEditDialog.toolTipText.fx=Abrir el compilador de expresiones

TextEditDialog.action.item.formatHTML=Formato HTML
TextEditDialog.action.item.formatDateTime=Formato de fecha/hora

TextEditDialog.action.item.formatNumber=Formato de número

TextEditDialog.action.item.formatString=Formato de string

TextEditDialog.title.DlgNew=Nuevo elemento de texto

TextEditDialog.title.DlgEdit=Editar elemento de texto

###########################################################
#TextEditPart properties
TextEditPart.Figure.Dafault=Texto

TextEditPart.trans.editText=Editar elemento de texto

###########################################################
#TextExtendsTools properties
TextExtendsTools.Message.DefaultAuthor=Versión de Birt Eclipse Report Designer

###########################################################
#TextPageGenerator properties
TextPageGenerator.List.Bookmark=Marcador

TextPageGenerator.List.Borders=Borde

TextPageGenerator.List.General=General

TextPageGenerator.List.I18n=Localización

TextPageGenerator.List.Margin=Margen

TextPageGenerator.List.Padding=Relleno

TextPageGenerator.List.Section=Salto de página

TextPageGenerator.List.TOC=Tabla de contenido

TextPageGenerator.List.Visibility=Visibilidad

###########################################################
#TextPropertyDescriptor properties
TextPropertyDescriptor.Message.Version=Eclipse BIRT Designer versión {0} compilación <{1}>

###########################################################
#AutoTextPageGenerator properties
AutoTextPageGenerator.List.Bookmark=Marcador

AutoTextPageGenerator.List.Borders=Borde

AutoTextPageGenerator.List.General=General

AutoTextPageGenerator.List.Margin=Margen

AutoTextPageGenerator.List.Padding=Relleno

AutoTextPageGenerator.List.Section=Salto de página

AutoTextPageGenerator.List.TOC=Tabla de contenido

AutoTextPageGenerator.List.Visibility=Visibilidad

###########################################################
#ThemesNodeProvider properties
ThemesNodeProvider.action.New=&Nuevo tema...

ThemesNodeProvider.Failed=Error en el nuevo tema

NewThemeDialog.DialogTitle=Nuevo tema

NewThemeDialog.DialogMessage=Introduzca el nombre

###########################################################
#TimeOptionDialog properties
TimeOptionDialog.Label.Format=Formato

TimeOptionDialog.Label.TimeZone=TimeZone

###########################################################
#ToggleMarginVisibilityAction properties
ToggleMarginVisibilityAction.text.Label=Mostrar &márgenes

ToggleMarginVisibilityAction.text.Tooltip=Alternar márgenes

###########################################################
#TwinExpressionDialog properties
TwinExpressionDialog.Dialog.Title=Seleccionar valores

TwinExpressionDialog.lable.value1=Valor 1:

TwinExpressionDialog.lable.value2=Valor 2:

###########################################################
#UndoAction properties
UndoAction.Tooltip=Deshacer {0}

###########################################################
#UnitPropertyDescriptor properties
UnitPropertyDescriptor.error.message=Introduzca un valor más pequeño\!

UnitPropertyDescriptor.error.title=Valor demasiado grande

###########################################################
#UseLibraryAction properties
UseLibraryAction.Text=&Usar librería

UseLibraryAction.Error.Title=Sustituir archivo existente

UseLibraryAction.Error.Message=El archivo ''{0}'' ya existe la carpeta de recursos de BIRT. ¿Desea reemplazar el archivo existente?

###########################################################
#UseCSSAction properties
UseCSSAction.Text=&Usar CSS

###########################################################
#UserPropertyBuilder properties
UserPropertyBuilder.0=Nueva propiedad de usuario

UserPropertyBuilder.ErrorMessage.DuplicatedName=El nombre especificado ya se ha utilizado.

UserPropertyBuilder.ErrorMessage.NoName=Especifique un nombre.

UserPropertiesHandleProvier.Column.Name=Nombre

UserPropertiesHandleProvier.Column.Type=Tipo

UserPropertyBuilder.Label.DefaultValue=&Valor predeterminado:

UserPropertyBuilder.Label.ExpressionName=Nombre de la ex&presión:

UserPropertyBuilder.Label.PropertyName=Nombre de la propie&dad:

UserPropertyBuilder.Label.PropertyType=Tipo de propieda&d:

UserPropertyBuilder.Title.NamedExpression=Nueva expresión con nombre

###########################################################
#VisibilityPage properties
VisibilityPage.Check.HideElement=&Ocultar elemento

VisibilityPage.Check.HTML=H&TML
VisibilityPage.Check.PDF=&PDF
VisibilityPage.Label.DetailAll=Detalle (todas las salidas)

VisibilityPage.Label.DetailSpecific=Detalle (salidas específicas)

VisibilityPage.Label.Expression=Expresión:

VisibilityPage.menu.SaveHides=Guardar elementos ocultos

VisibilityPage.Radio.AllOutputs=Para todas las s&alidas

VisibilityPage.Radio.SpecificOutputs=Para salidas espe&cíficas...

###########################################################
#WidgetUtil properties
WidgetUtil.MessageBox.Error=Error

###########################################################
#wizard properties
wizard.transaction.createDataSource=Defina el nombre y el tipo de la fuente de datos.

###########################################################
#WizardChoicePage properties
WizardChoicePage.radio.createBlank=Crear un nuevo informe en blanco sin usar una plantilla.

WizardChoicePage.radio.createFromCustom=Crear el informe como una copia de otro informe.

WizardChoicePage.radio.createFromTemplate=Crear el informe a partir de una plantilla.

###########################################################
#WizardCustomTemplatePage properties
WizardCustomTemplatePage.button.Browse=Examinar

WizardCustomTemplatePage.label.FileName=Introducir o seleccionar un nombre de archivo de informe existente:

WizardCustomTemplatePage.label.FromLocal=Examinar en el sistema de archivos locales.

WizardCustomTemplatePage.label.FromProjectDescription=Seleccionar archivo de informe existente.

WizardCustomTemplatePage.label.FromProjectTitle=Selección de archivo de informe

###########################################################
#WizardNewReportCreationPage properties
WizardNewReportCreationPage.Errors.nameExists=El nombre ya existe.

WizardNewReportCreationPage.Errors.nameEmpty=El nombre no puede estar vacío.

WizardNewReportCreationPage.label.browse=Examinar...

WizardNewReportCreationPage.label.directory=Directorio:

WizardNewReportCreationPage.label.file.location=Ubicación de archivo

WizardNewReportCreationPage.label.file.name=Nombre de archivo:

WizardNewReportCreationPage.label.select.directory=Seleccione un directorio.

WizardNewReportCreationPage.label.useDefault=Usar predeterminado

WizardNewReportCreationPage.msg.duplicate.fileName=El nombre ya existe.

WizardNewReportCreationPage.msg.empty.file.locationDirectory=El directorio de ubicación de archivo no puede estar vacío.

WizardNewReportCreationPage.msg.empty.file.name=El nombre de archivo no puede estar vacío.

###########################################################
#WizardSelectCssStylePage properties
WizardSelectCssStylePage.button.label.browse=&Examinar...

WizardSelectCssStylePage.button.label.deselectAll=&Anular la selección de todo

WizardSelectCssStylePage.button.label.selectAll=&Seleccionar todo

WizardSelectCssStylePage.errorMsg.FileNotExist=El archivo no existe.

WizardSelectCssStylePage.info.canNotImport=\ \ (No se pueda importar)

WizardSelectCssStylePage.label.fileName=&Nombre de archivo:

WizardSelectCssStylePage.label.importToReportDesign=\ para importar al diseño de informe.

WizardSelectCssStylePage.label.notifications=Notificaciones:

WizardSelectCssStylePage.label.selectStylesFrom=Seleccionar los estilos de

WizardSelectCssStylePage.msg.FileNameEmpty=El nombre de archivo no puede estar vacío.

WizardSelectCssStylePage.text.cannot.import.style=\ - No se puede importar el estilo.

WizardSelectCssStylePage.FileNameError.Title=Error

WizardSelectCssStylePage.FileNameError.Message=El archivo seleccionado no es válido.

###########################################################
#WizardTemplateChoicePage properties
WizardTemplateChoicePage.label.Description=Descripción:

WizardTemplateChoicePage.label.Preview=Vista previa:

WizardTemplateChoicePage.label.ReportTemplates=Plantillas de informes:

WizardTemplateChoicePage.label.ShowCheatSheets=&Mostrar archivo de consejos útiles para la creación de informes

WizardTemplateChoicePage.label.EnableDefaultThemes=&Incluir los temas predeterminados

WizardTemplateChoicePage.message.BlankReport=Crear un informe en blanco sin contenido predefinido.

WizardTemplateChoicePage.message.ChartListing=Crear un informe agrupado con un diagrama que ofrece una representación visual de cada dato del grupo.

WizardTemplateChoicePage.message.DualColumnChartListing=Crear un informe agrupado con dos subgrupos de "diagrama y listado".

WizardTemplateChoicePage.message.DualColumnListing=Crear un informe agrupado con dos subgrupos.

WizardTemplateChoicePage.message.FirstReport=Proporciona instrucciones para crear un informe de listado de clientes.

WizardTemplateChoicePage.message.GroupedListing=Crear un informe de listado con agrupaciones en algunos campos.

WizardTemplateChoicePage.message.Letter=Crear una plantilla para generar cartas personalizadas con los campos de la fuente de datos.

WizardTemplateChoicePage.message.MailingLabels=Etiquetas de envío

WizardTemplateChoicePage.message.SideBySideChartListing=Crear un informe agrupado en el que cada grupo contiene un diagrama y un listado en paralelo.

WizardTemplateChoicePage.message.SimpleListing=Crear un informe de listado con campos de la fuente de datos.

WizardTemplateChoicePage.message.Crosstab=Use un tutorial para crear un informe de tabla de referencias cruzadas

WizardTemplateChoicePage.message.BlankAuto=Una plantilla de informe en blanco, que activará automáticamente la interactividad cuando se visualice.

WizardTemplateChoicePage.title.BlankAuto=Informe interactivo automático en blanco

WizardTemplateChoicePage.title.BlankReport=Informe en blanco

WizardTemplateChoicePage.title.ChartListing=Diagrama y listado

WizardTemplateChoicePage.title.DualColumnChartListing=Diagrama y listado de columna duales

WizardTemplateChoicePage.title.DualColumnListing=Listado de columnas dual

WizardTemplateChoicePage.title.FirstReport=Mi primer informe

WizardTemplateChoicePage.title.GroupedListing=Listado agrupado

WizardTemplateChoicePage.title.Letter=Carta

WizardTemplateChoicePage.title.MailingLabels=Etiquetas de envío

WizardTemplateChoicePage.title.SideBySideChartListing=Diagrama y listado en paralelo

WizardTemplateChoicePage.title.SimpleListing=Listado simple

WizardTemplateChoicePage.title.Crosstab=Tabla de referencias cruzadas
#bidi_hcg start
WizardTemplateChoicePage.label.rtlBiDiOrientation=De derecha a izquierda

WizardTemplateChoicePage.label.ltrBiDiOrientation=De izquierda a derecha

WizardTemplateChoicePage.label.chooseBiDiDirection=&Orientación del informe:

WizardTemplateChoicePage.RootDisplayName=Estándar
#bidi_hcg end
###########################################################
#XMLSourcePage properties
XMLSourcePage.Error.Dialog.Message.InvalidFile=Se debe corregir el error de la página fuente XML antes de poder utilizar otras páginas.

XMLSourcePage.Error.Dialog.Message.SaveFile=La página fuente XML se ha modificado. No se pueden utilizar otras páginas hasta que se guarde la fuente.

XMLSourcePage.Error.Dialog.title=Error

XMLSourcePage.Error.Dialog.Message.PromptMsg=La fuente del diseño de informe XML se ha modificado. ¿Desea guardar los cambios?

XMLSourcePage.Error.Dialog.Message.Yes=Sí

XMLSourcePage.Error.Dialog.Message.No=No

XMLSourcePage.Error.Dialog.Message.Cancel=Cancelar

###########################################################
#SaveReportAsWizard properties
SaveReportAsWizard.SettingPage.title=Establecer propiedad de plantilla

SaveReportAsWizard.SaveAsPageTitle=Guardar como

SaveReportAsWizard.SaveAsLibraryMessage=Guardar archivo como librería con otro nombre.

SaveReportAsWizard.SaveAsReportorTemplateMessage=Guardar archivo como un diseño de informe o plantilla con otro nombre.

SaveReportAsWizard.SettingPageTitle=Establecer propiedad del informe

SaveReportAsWizard.SettingPage.message=Estas propiedades se utilizan si el informe está guardado como plantilla.

SaveReportAsWizard.SaveAsWizardWindowTitle=Guardar como

SaveReportAsWizard.SaveAsWizardPageTitle=Guardar como

SaveReportAsWizard.SaveAsWizardPageDesc=Guarde el archivo en otra ubicación.

SaveReportAsWizard.SettingPage.Messages=Establezca las propiedades de plantilla.

###########################################################
#WizardReportSettingPage properties
WizardReportSettingPage.Error.ReportorTemplate=El nombre de archivo debe finalizar con {0} o .rpttemplate.

WizardReportSettingPage.Error.Library=El nombre de archivo debe finalizar con .rptlibrary.

###########################################################
#WizardSaveAsPage properties
WizardSaveAsPage.FileLabel=&Nombre de archivo:

WizardSaveAsPage.Question=Pregunta

WizardSaveAsPage.FolderEmpty=\ La carpeta está vacía.

WizardSaveAsPage.NoProject=\ El proyecto especificado no existe.

WizardSaveAsPage.PathOccupied=\ Ya existe un archivo en esa ubicación: {0}

WizardSaveAsPage.EmptyName=\ El nombre ''{0}'' está vacío.

WizardSaveAsPage.SelectFolder=\ Seleccione la carpeta:

WizardSaveAsPage.NameExists=\ Este nombre ya existe.

WizardSaveAsPage.OverwriteQuestion=El archivo ''{0}'' ya existe. ¿Desea reemplazar el archivo existente?

WizardSaveAsPage.InvalidFileName=\ ''{0}'' no es un nombre de archivo válido.

WizardSaveAsPage.ContainerGroup=\ &Introduzca o seleccione la carpeta principal:

###########################################################
#Library DND message
Library.DND.messages.outofsync = Podría haberse cambiado la librería utilizada en este informe, cierre este informe y vuelva a abrirlo para utilizar la librería actualizada.

NamedExpressionsHandleProvier.Column.Name=Nombre

NamedExpressionsHandleProvier.Column.DefaultValue=Valor predeterminado

Library.DND.messages.cannotApplyTheme=El tema no se puede aplicar.

JarFileFormProvider.Column.Name=Nombre

JsFileFormProvider.Column.Name=Nombre

ReportPageGenerator.List.Resources.JsFile=Archivos de JavaScript:

PropertiesFileFormProvider.Column.Name=Nombre

ReportPageGenerator.List.Resources.PropertiesFile=Archivos de propiedades:

###########################################################
#ClassFinder
ClassFinder.TaskStart=Buscando clase de controlador...

ClassFinder.DialogTitle=Selección de clase

ClassFinder.DialogMessage=&Seleccionar una clase de controlador:

ClassFinder.ErrorMessage=No se ha encontrado ninguna clase

ClassPathBlock_button.addExtFolder=Añad&ir carpeta de clase externa...

ClassPathBlock_button.addExtJars=Añadir archivos JAR ex&ternos...

ClassPathBlock_button.down=Abajo

ClassPathBlock_button.edit=&Editar...

ClassPathBlock_button.remove=Quita&r

ClassPathBlock_button.up=Arriba

ClassPathBlock_FileDialog.edit.text=Editar JAR

ClassPathBlock_FileDialog.jar.text=Selección JAR

ClassPathBlock_FolderDialog.edit.message=Indique la nueva ubicación de la carpeta de clase:

ClassPathBlock_FolderDialog.edit.text=Editar carpeta de clase

ClassPathBlock_FolderDialog.message=Seleccione la ubicación de la carpeta de clase:

ClassPathBlock_FolderDialog.text=Selección de carpeta de clase externa

ClassPathBlock_label.LibrariesList=Carpetas de clase y JAR en la ruta de e&jecución del informe:

###########################################################
#ListingSectionPage
ListingSectionPage.RepeatHeader=&Repetir encabezado

###########################################################
#ResourceEditDialog
ResourceEditDialog.NotFile.Title=No se ha encontrado el archivo

ResourceEditDialog.NotFile.Message=El archivo ''{0}'' no existe.

ResourceEditDialog.ReadOnlyEncounter.Title=Se ha encontrado un archivo de sólo lectura

ResourceEditDialog.ReadOnlyEncounter.Message=El archivo ''{0}'' es de sólo lectura, asegúrese primero de que se puede escribir en él.

###########################################################
#EventHandler
EventHandlerPage.Browse=&Examinar...


###########################################################
#IDEFileReportProvider
IDEClassPathBlock.button_addExtFolder=Añad&ir carpeta de clase externa...

IDEClassPathBlock.button_addEXTJar=Añadir archivos JAR ex&ternos...

IDEClassPathBlock.button_addJar=Añadir arc&hivos JAR...

IDEClassPathBlock.button_addProject=Añadir pro&yectos...

IDEClassPathBlock.button_addVar=Añadir va&riable...

IDEClassPathBlock.button_down=Abajo

IDEClassPathBlock.button_edit=&Editar...

IDEClassPathBlock.button_folder=Añadir c&arpeta de clase...

IDEClassPathBlock.button_remove=Quita&r

IDEClassPathBlock.button_up=Arriba

IDEClassPathBlock.fLibrariesList_text=Carpetas de clase y JAR en la ruta de e&jecución del informe:

IDEClassPathBlock.ProjectDialog_message=Seleccione los proyectos que desea añadir:

IDEClassPathBlock.ProjectDialog_title=Selección de proyectos requerida

IDECPListLabelProvider.folder=(carpeta de clase)

IDECPListLabelProvider.missing=(falta)

IDECPListLabelProvider.new=(nuevo)

IDECPListLabelProvider.unknown=elemento desconocido

IDEFileReportProvider.ReadOnlyEncounter.Title=Se ha encontrado un archivo de sólo lectura

IDEFileReportProvider.ReadOnlyEncounter.Message=El archivo ''{0}'' es de sólo lectura, asegúrese primero de que se puede escribir en él.

IDEResourcePageHelper.Dialog.Prompt=Seleccionar un directorio:

IDEResourcePageHelper.Dialog.Text=Seleccionar una u&bicación relativa al área de trabajo:

IDEResourcePageHelper.Dialog.Title=Selección de un directorio

IDEResourcePageHelper.File.Button=&Sistema de archivos...

IDEResourcePageHelper.Varible.Button=&Variables...

IDEResourcePageHelper.WorkSpace.Button=Á&rea de trabajo...

###########################################################
#DataSetColumnBindingsFormHandleProvider
DataSetColumnBindingsFormHandleProvider.Column.Position=Position
DataSetColumnBindingsFormHandleProvider.Column.Name=Nombre

DataSetColumnBindingsFormHandleProvider.Column.DisplayNameID=ID de nombre en pantalla

DataSetColumnBindingsFormHandleProvider.Column.DisplayName=Nombre en pantalla

DataSetColumnBindingsFormHandleProvider.DatasetTitle=Enlace de columna de datos:

DataSetColumnBindingsFormHandleProvider.RefreshBindingColumns=Actualizar columnas de enlace

DataSetColumnBindingsFormHandleProvider.ReportItemTitle=Enlace de columna disponible

DataSetColumnBindingsFormHandleProvider.NewBinding=Nuevo enlace

DataSetColumnBindingsFormHandleProvider.Column.DataType=Tipo de datos

DataSetColumnBindingsFormHandleProvider.Column.Expression=Expresión

DataSetColumnBindingsFormHandleProvider.Column.AggregateOn=Agregar en

DataSetColumnBindingsFormHandleProvider.Column.Filter=Filtro

DataSetColumnBindingsFormHandleProvider.Column.Function=Función

DataSetColumnBindingsFormHandleProvider.ALL=Todos

DataSetColumnBindingsFormHandleProvider.GenerateBindingColumns=Generar columnas de enlace

DataSetColumnBindingsFormHandleProvider.NONE=N/A
DataSetColumnBindingsFormHandleProvider.Trans.GenerateBindingColumns=Generar columnas de enlace

DataSetColumnBindingsFormHandleProvider.Trans.RefreshBindingColumns=Actualizar columnas de enlace

###########################################################
#DataSetColumnBindingsFormPage
DataSetColumnBindingsFormPage.Button.RemoveUnused=Quitar columna no usada

###########################################################
#DataSetSettingsPage
DataSetSettingsPage.DataGroup.Title=Ajustes del motor BIRT para comparar valores de la columna de conjunto de datos

DataSetSettingsPage.Locale.Label=&Configuración regional de los valores de string de carácter:

DataSetSettingsPage.NullValueOrdering.Label=&Orden de valores nulos:

###########################################################
#ResourceSelectionValidator
ResourceSelectionValidator.ExtError=El archivo seleccionado debe terminar en

ResourceSelectionValidator.EmptyError=Seleccione un archivo

ResourceSelectionValidator.ExtErrorSplitter=\ o

###########################################################
#GeneralPage
GeneralPage.Library.Included=Librería:

GeneralPage.Library.Modified.Note=NOTA: Se ha modificado el elemento del informe de la librería. Para restaurar el elemento de informe original, haga clic en {0} "Restaurar propiedades"

#BookMarkPage
BookMarkPage.Modified.Note=El marcador contiene caracteres espaciales distintos a estos: A-Z, a-z, 0-9, '-', '_', ':', '.'. Esto podría causar un problema en el tiempo de ejecución.

BookMarkPage.Modified.WithoutQuotes=El marcador no está contenido en comillas dobles. Esto podría causar un problema en el tiempo de ejecución.

###########################################################
# LOCALE SPECIAL:
# following string need be specially localized, e.g. as per local country custom, not just simple translation
# for example:
#       the social security number in US is like 760-01-0101, the format should be @@@-@@-@@@@
#       but the SSN in Swedish is like 760101-0101, the format should be @@@@@@-@@@@
# 
FormatStringPage.SimpleTextZipCode=94103

FormatStringPage.SimpleTextZipCode4=94123

FormatStringPage.PhoneNumber=4155551111

FormatStringPage.SocialSecurityNumber=123456789

FormatStringPage.simpleTexZipCodeFormat=@@@@@

FormatStringPage.simpleTexZipCode4Format=@@@@@-@@@@
FormatStringPage.phoneNumberFormat=(@@@)@@@-@@@@
FormatStringPage.securityNumberFormat=@@@-@@-@@@@
FormatStringPage.Label.PreserveWhiteSpaces=Conservar espacios en blanco

FormatStringPage.Preview.PreserveWhiteSpaces=\    string con espacio en blanco al principio

###########################################################
#PublishLibraryDialog
PublishLibraryDialog.ShellText=Compartir librería

PublishLibraryDialog.TitleArea=Compartir librería

PublishLibraryDialog.Message=Copiar librería para compartir carpeta de recursos.

PublishLibraryDialog.Label.SourceFileName=&Archivo fuente:

PublishLibraryDialog.Label.FileName=&Nombre de archivo:

PublishLibraryDialog.Label.Folder=Carp&eta:

PublishLibraryDialog.Label.Browse=&Examinar...

PublishLibraryDialog.Message.FileNameEmpty=Es necesario un nombre de archivo.

PublishLibraryDialog.Message.FileNameError=El nombre de archivo debe finalizar con .rptlibrary.

PublishLibraryDialog.Message.FolderEmpty=Es necesaria una carpeta de publicación.

PublishLibraryDialog.Message.SourceFileEmpty=Es necesario un nombre de archivo fuente.

PublishLibraryDialog.Label.Browse2=E&xaminar...

PublishLibraryDialog.AddText=Añadir librería

PublishLibraryDialog.AddMessage=Añadir librería a carpeta de recursos de BIRT

WizardLibrarySettingPage.ErrorMessage=Seleccione una carpeta

WizardLibrarySettingPage.Resourcefile.Dialog.Title=Examinar carpetas de recursos

WizardLibrarySettingPage.Resourcefile.Dialog.Message=Seleccione la carpeta de recursos o sus subcarpetas.

PublishLibraryDialog.AddLibrary=Añadir librería


###########################################################
#PublishCSSDialog
PublishCSSDialog.ShellText=Publicar archivo CSS

PublishCSSDialog.TitleArea=Publicar archivo CSS en la carpeta de recursos

PublishCSSDialog.Message=Seleccione un archivo CSS para importar estilos a una carpeta de recursos

PublishCSSDialog.Label.SourceFileName=&Archivo fuente:

PublishCSSDialog.Label.FileName=&Nombre de archivo:

PublishCSSDialog.Label.Folder=&Carpeta:

PublishCSSDialog.Label.Browse=&Examinar...

PublishCSSDialog.Label.Browse2=E&xaminar...

PublishCSSDialog.ErrorMessage.SelectFolder=Seleccione una carpeta

PublishCSSDialog.Dialog.FileSeclect.Tilte=Seleccionar un archivo CSS

PublishCSSDialog.Error.Exception.Title=Error

PublishCSSDialog.Error.FileNotFound=No se ha encontrado el archivo ''{0}''.

PublishCSSDialog.Error.FileIsNotCSSFile=El archivo ''{0}'' no termina en ''{1}''.

PublishCSSDialog.Error.FileExist=El archivo ya existe.

PublishCSSDialog.Message.SourceFileEmpty=Es necesario un nombre de archivo fuente.

PublishCSSDialog.Message.FileNameEmpty=Es necesario un nombre de archivo.

PublishCSSDialog.Message.FileNameError=El nombre de archivo debe terminar en .css.

PublishCSSDialog.Message.FolderEmpty=Es necesaria una carpeta de publicación.

WizardCSSSettingPage.ErrorMessage=Seleccione una carpeta

WizardCSSSettingPage.Resourcefile.Dialog.Title=Examinar carpetas de recursos

WizardCSSSettingPage.Resourcefile.Dialog.Message=Seleccione la carpeta de recursos o sus subcarpetas.

###########################################################
#PublishResourceDialog
PublishResourceDialog.ShellText=Publicar recurso

PublishResourceDialog.TitleArea=Publicar recurso en la carpeta de recursos

PublishResourceDialog.Message=Colocar un archivo de recursos en la carpeta de recursos

PublishResourceDialog.AddResource=Añadir recurso

PublishResourceDialog.AddText=Añadir recurso

PublishResourceDialog.AddMessage=Añadir recurso a la carpeta de recursos

PublishResourceAction.wizard.errorTitle=Error

PublishResourceAction.wizard.message=No es posible publicar el recurso en el directorio donde se encuentra, seleccione otro directorio para la publicación.

PublishResourceAction.wizard.message.SourceFileNotExist=El archivo fuente no existe.

PublishResourceAction.wizard.notvalidfolder=La carpeta del archivo CSS definida como preferencia no es válida.

PublishResourceDialog.Label.FileName=&Nombre de archivo:

PublishResourceDialog.Label.SourceFileName=&Archivo fuente:

PublishResourceDialog.Label.Folder=Carpeta:

PublishResourceDialog.Label.Browse=&Examinar...

PublishResourceDialog.Label.Browse2=E&xaminar...

WizardResourceSettingPage.ErrorMessage=Seleccione una carpeta

WizardResourceSettingPage.Resourcefile.Dialog.Title=Examinar carpetas de recursos

WizardResourceSettingPage.Resourcefile.Dialog.Message=Seleccione la carpeta de recursos o sus subcarpetas.

PublishResourceDialog.Message.SourceFileEmpty=Es necesario un nombre de archivo fuente.

PublishResourceDialog.Message.FileNameEmpty=Es necesario un nombre de archivo.

PublishResourceDialog.Message.FolderEmpty=Es necesaria una carpeta de publicación.

###########################################################
#EmbeddedImagesNodeProvider
EmbeddedImagesNodeProvider.FileNameError.Title=Error

EmbeddedImagesNodeProvider.FileNameError.Message=El archivo seleccionado no es una imagen.

EmbeddedImagesNodeProvider.stackMsg.insert=Insertar imagen

###########################################################
#AddElementtoAction properties
AddElementtoAction.Text=&Añadir a informe

ApplyThemeAction.Text=&Aplicar al informe

###########################################################
#MultiPageReportEditor properties
MultiPageReportEditor.CheckVersion.Dialog.Title=Advertencia

MultiPageReportEditor.CloseButton=Cerrar

MultiPageReportEditor.ConfirmTitle=El archivo no existe

MultiPageReportEditor.ConfirmVersion.Dialog.Message=Una versión anterior de BIRT Designer ha creado el diseño de informe. Al guardarlo, se actualizará a la versión actual y podría no ser accesible a versiones anteriores de BIRT. ¿Aún desea guardarlo?

MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage=El contenido de este editor puede haber cambiado desde fuera, ¿desea volver a cargar los cambios?

MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage=El contenido de este editor puede haber cambiado desde fuera, ¿desea volver a cargar el contenido con estos cambios?

MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage=El contenido de este editor puede haber cambiado desde fuera y el editor tiene cambios no guardados.\n\n¿Desea guardar el editor y volver a cargar los cambios de fuera?

MultiPageReportEditor.ConfirmVersion.Dialog.Title=Confirmar

MultiPageReportEditor.SaveButton=&Guardar

MultiPageReportEditor.DiscardButton=&Desechar

MultiPageReportEditor.CancelButton=Cancelar

MultiPageReportEditor.SaveConfirmMessage=El archivo se ha eliminado. ¿Desea guardar los cambios o cerrar el editor sin guardar?

TableArea.Button.Up=A&rriba

###########################################################
#AddJarResourceFileFolderSelectionDialog
AddJarResourceFileFolderSelectionDialog.ErrorMessage.Exist=Uno o más de los archivos seleccionados ya existen.

AddJarResourceFileFolderSelectionDialog.Message=Seleccione {0} archivos:

AddJarResourceFileFolderSelectionDialog.ErrorMessage.ExtError=El nombre de archivo debe finalizar con {0}.

AddJarResourceFileFolderSelectionDialog.Message.Or=o


###########################################################
#AddImageResourceFileFolderSelectionDialog
AddImageResourceFileFolderSelectionDialog.Title=Uno o más de los archivos seleccionados ya existen.

AddImageResourceFileFolderSelectionDialog.Message=Seleccione una imagen:

###########################################################
#DataItemBindingDialog properties
DataItemBindingDialog.text.Name=Nombre:

DataItemBindingDialog.text.displayName=Nombre en pantalla:

DataItemBindingDialog.text.DataType=Tipo de datos:

DataItemBindingDialog.text.Expression=Expresión:

DataItemBindingDialog.text.AggregateOn=Agregar en:

DataItemBindingDialog.text.ForceBinding=Crear un nuevo enlace con la misma expresión

DataItemBindingDialog.text.All=Todos

DataItemBindingDialog.text.None=N/A
DataItemBindingDialog.text.Null=Ninguno

DataItemBindingDialog.title.CreateNewItem=Nuevo enlace de elemento de datos

DataItemBindingDialog.title.EditDataItem=Editar enlace de elemento de datos

DataItemBindingDialog.text.Function=Función:

DataItemBindingDialog.text.Filter=Filtro:

DataItemBindingDialog.text.ArgumentList=Argumento:

DataItemBindingDialog.Button.Add=Añadir

DataItemBindingDialog.Button.Edit=Editar

DataItemBindingDialog.Button.Delete=Eliminar

###########################################################
#DataColumBindingDialog properties
DataColumBindingDialog.title.CreateNewDataBinding=Nuevo enlace de datos

DataColumBindingDialog.title.EditDataBinding=Editar enlace de datos

DataColumBindingDialog.title.AggBuilder=Compilador de agregación

DataColumnBindingDialog.NewBindingDialogInputNewNameMessage=Nuevo nombre de enlace de datos

DataColumnBindingDialog.NewBindingDialogInputNewNameTitle=Introduzca un nuevo nombre de enlace de datos

DataColumnBindingDialog.NewBindingDialogMessage=Este enlace de datos está siendo utilizado por más de un elemento y celda de informe. ¿Desea crear una nueva expresión para este elemento de informe? Haga clic en Sí para crear un nuevo enlace de datos, o bien haga clic en No para cambiar el enlace de datos en todas las ubicaciones.

DataColumnBindingDialog.NewBindingDialogButtonYes=Sí

DataColumnBindingDialog.NewBindingDialogButtonNo=No

DataColumnBindingDialog.NewBindingDialogButtonCancel=Cancelar

DataColumnBindingDialog.NewBindingDialogTitle=¿Desea crear un nuevo enlace de datos?

DataColumnBindingDialog.title.TimePeriodBuild=Compilador de agregación de periodo de tiempo relativo

DataColumBindingDialog.stackMsg.resetReference=Restablecer referencia

###########################################################
#CreatePlaceHolderAction properties
ChangeDataColumnAction.text=Ca&mbiar columna de datos...

###########################################################
#ThumbnailBuilder properties
ThumbnailBuilder.Text.Title=Administrar miniaturas

ThumbnailBuilder.Button.Text.Generate=&Generar

ThumbnailBuilder.Button.Text.Remove=&Quitar

ThumbnailBuilder.Button.Text.Import=&Importar...

ThumbnailBuilder.Button.Text.Browse=&Examinar...

ThumbnailBuilder.FileDialog.Title=Seleccionar archivo de imagen

ThumbnailBuilder.FileDialog.FileNameError.Title=Error

ThumbnailBuilder.FileDialog.FileNameError.Message=El archivo seleccionado no es una imagen.

ThumbnailBuilder.Button.GenerateFromReport=Ge&nerar desde el informe

ThumbnailBuilder.Button.BrowseFromFileSystem=E&xplorar desde el sistema de archivos

ThumbnailBuilder.Button.ImportFromResource=I&mportar desde los recursos compartidos

ThumbnailBuilder.Image.DefaultName=Imagen en miniatura

ThumbnailBuilder.PreviewImage.WarningDialog.Title=Advertencia

ThumbnailBuilder.PreviewImage.WarningDialog.Prompt=Ha seleccionado una imagen de gran tamaño (superior a 1014 x 768) que puede provocar una excepción por memoria insuficiente.\n¿Desea continuar?


###########################################################
#FragmentResourceEntry properties
FragmentResourceEntry.RootName=Fragmentos

FragmentResourceEntry.RootDisplayName=Recursos del sistema


FragmentTemplateResourceEntry.RootName=TemplateFragments
FragmentTemplateResourceEntry.RootDisplayName=Recursos de la plantilla

###########################################################
#PathResourceEntry properties
PathResourceEntry.RootName=Carpeta de recursos

PathResourceEntry.RootDisplayName=Recursos compartidos

###########################################################
#ResourceFilter properties
ResourceFilter.DisplayName.CVS=Recursos CVS

ResourceFilter.DisplayName.Dot=.* recursos

ResourceFilter.DisplayName.Data=*.recursos de datos

ResourceFilter.DisplayName.EmptyFolder=Vaciar carpetas

ResourceFilter.Description.CVS=Oculta recursos con nombres que comienzan por \"CVS\"

ResourceFilter.Description.Dot=Oculta recursos con nombres que comienzan por "."

ResourceFilter.Description.Data=Oculta todos los archivos de datos

ResourceFilter.Description.EmptyFolder=Oculta carpetas vacías

###########################################################
#BIRTGotoMarker properties
BIRTGotoMarker.Error.Title=Error

BIRTGotoMarker.Error.Message=El elemento de informe no existe.

###########################################################
#TabbedList properties
TabbedPropertyList.properties.not.available=Las propiedades no están disponibles

###########################################################
#Sample Reports View properties
SampleReportsView.defautDescription=Seleccione un informe de muestra entre las categorías

SampleReportsView.Text.Explorer=Informes de muestras

SampleReportsView.Text.Preview=Vista previa

SampleReportsView.Text.Description=Descripción

SampleReportsView.MessageDialog.Title=Pregunta

SampleReportsView.MessageDialog.Message=El ''{0}'' ya existe. ¿Desea sustituirlo?

SampleReportsView.Action.exportSampleReport=&Exportar

SampleReportsView.Action.openSampleReport=&Importar

SampleReportsView.Action.exportSampleReport.toolTipText=Exportar el informe de muestras seleccionado

SampleReportsView.Action.openSampleReport.toolTipText.ide=Crear un proyecto de informe y colocar el informe en la carpeta del proyecto

SampleReportsView.Action.openSampleReport.toolTipText.rcp=Exportar el informe de muestra seleccionado directamente a la carpeta del área de trabajo RCP

SampleReportsView.Text.Contribute_Samples=Muestras de aportación

UseCssStyleAction.text=&Utilizar archivo CSS...

EditUseCssStyleAction.text=&Editar archivo CSS...

EditUseCssStyleAction.EditCssTitle=Editar CSS

EditUseCssStyleAction.EditCssAreaTitle.Report=Editar el archivo CSS en el diseño de informes

EditUseCssStyleAction.EditCssAreaTitle.Libary=Editar el archivo CSS en la librería de informes

###########################################################
#GrossTabPageGenerator properties
CrosstabPageGenerator.List.General=General

CrosstabPageGenerator.List.Margin=Margen

CrosstabPageGenerator.List.Font=Fuente

CrosstabPageGenerator.List.Borders=Borde

CrosstabPageGenerator.List.Section=Salto de página

CrosstabPageGenerator.List.Visibility=Visibilidad

CrosstabPageGenerator.List.TOC=Tabla de contenido

CrosstabPageGenerator.List.Bookmark=Marcador

CrosstabPageGenerator.List.NamedExpressions=Expresiones con nombre

CrosstabPageGenerator.List.UserProperties=Propiedades de usuario

CrosstabPageGenerator.List.AdvancedProperty=Avanzada

CrosstabPageGenerator.List.SubTotals=Subtotales

CrosstabPageGenerator.List.GrandTotals=Totales generales

CrosstabPageGenerator.List.PageBreak=Salto de página

###########################################################
#CubePageGenerator properties
CubePageGenerator.List.General=General

CubePageGenerator.List.Comments=Comentarios

CubePageGenerator.List.AdvancedProperty=Avanzado


###########################################################
#LibraryLayoutEditorFormPage
LibraryLayoutEditorFormPage.warning.title=Librería

LibraryLayoutEditorFormPage.warning.message=Para añadir un elemento de informe a la librería, arrastre el elemento desde la paleta o utilice el menú de inserción.\n\nPara ver los elementos de informe en el editor, haga clic sobre ellos en la vista de contorno.

LibraryLayoutEditorFormPage.warning.prompt=No &volver a mostrar este mensaje

###########################################################
#PageGenerator Item properties
PageGenerator.TabItem.Attributes=Propiedades

PageGenerator.TabItem.Binding=Enlace

PageGenerator.TabItem.Filters=Filtros

PageGenerator.TabItem.Groups=Grupos

PageGenerator.TabItem.Highlights=Resaltados

PageGenerator.TabItem.map=Asignar

PageGenerator.TabItem.Sorting=Ordenar

###########################################################
#AggDataDropAdapter
AggDataDropAdapter.Trans.Name=Crear agregación

###########################################################
#BindingDialogHelper
BindingDialogHelper.text.Name=Nombre de &enlace de columna:

BindingDialogHelper.text.DataType=&Tipo de datos:

BindingDialogHelper.text.Function=&Función:

BindingDialogHelper.text.DataField=Campo de &datos:

BindingDialogHelper.text.Filter=&Condición de filtro:

BindingDialogHelper.text.AggOn=A&gregar en:

BindingDialogHelper.text.Table=Tab&la

BindingDialogHelper.text.List=&Lista

BindingDialogHelper.text.Grid=Cuadrícula

BindingDialogHelper.text.Group=Gr&upo

BindingDialogHelper.text.Expression=&Expresión:

BindingDialogHelper.text.displayName=&Nombre en pantalla:

BindingDialogHelper.text.allowExport=Exp&ortar:

BindingDialogHelper.text.allowExport.button=Permitir exportación

BindingDialogHelper.text.displayNameID=ID de &nombre en pantalla:

BindingDialogHelper.bindingName.dataitem=Enlace de columna

BindingDialogHelper.bindingName.aggregation=Agregación

BindingDialogHelper.error.nameduplicate=Ya existe un enlace de columna con el nombre {0}.

BindingDialogHelper.error.improperexpression=La agregación en medidas de cubo no puede ser de este tipo: "{0}".

BindingDialogHelper.error.empty=Se requiere {0}.

BindingDialogHelper.error.text.Name=Nombre de enlace de columna

BindingDialogHelper.text.Colon=:
BindingDialogHelper.warning.function=El tipo de datos recomendado para la función actual es "{0}". ¿Desea mantener el tipo de datos que ha seleccionado?

BindingDialogHelper.warning.parameter=El tipo de datos de devolución de la expresión "{0}" no es compatible con el parámetro "{1}". ¿Desea mantenerlo?

BindingDialogHelper.warning.button.yes=Sí

BindingDialogHelper.warning.button.no=No

BindingGroupDescriptorProvider.Flag.DataModel=\ (Modelo de datos)

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel=Enlace del modelo de datos

BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet=Enlace del conjunto de datos

###########################################################
#Scripts Node
Scripts.Node.DisplayName=Scripts

ScriptElementNode.Action.Text=&Ir a Elemento de informe

ScriptObjectNode_0=&Restablecer script

ReportPage.PreviewImage.Button.ToolTip=Administrar miniaturas

###########################################################
#CrosstabBindingDialogHelper
CrosstabBindingDialogHelper.AggOn.All=Todos

CrosstabBindingDialogHelper.bindngName.timeperiod=Período

CrosstabBindingDialogHelper.calculation.label=&Periodo de tiempo:

CrosstabBindingDialogHelper.calculation.group=Periodo de tiempo

CrosstabBindingDialogHelper.recentday.description=Última fecha del período

CrosstabBindingDialogHelper.referencedate.label=&Fecha de referencia:

CrosstabBindingDialogHelper.thisdate.label=Esta fecha:

CrosstabBindingDialogHelper.thisdate.example.label=Incluya el valor de la fecha proporcionada entre comillas.\nPor ejemplo: "2012-01-23", "1/23/2012".

CrosstabBindingDialogHelper.thisdate.example.label.constant=Por ejemplo: 2012-01-23, 1/23/2012.

CrosstabBindingDialogHelper.timedimension.label=Di&mensión de tiempo:

CrosstabBindingDialogHelper.timeperiod.wrongdate=Esta dimensión no es compatible con la fecha de referencia reciente

CrosstabBindingDialogHelper.today.label=Hoy

AddEditCascadingParameterDialog.label.name=&Nombre:

AddEditCascadingParameterDialog.label.dataset=&Conjunto de datos:

AddEditCascadingParameterDialog.label.value=&Valor:

AddEditCascadingParameterDialog.label.displaytext=Visualizar &texto:

###########################################################
#FormatDateTimePattern
FormatDateTimePattern.datetiem_format_type_year=Año

FormatDateTimePattern.datetiem_format_type_short_year=Año corto

FormatDateTimePattern.datetiem_format_type_long_month_year=Mes largo, año

FormatDateTimePattern.datetiem_format_type_shot_month_year=Mes corto, año

FormatDateTimePattern.datetiem_format_type_month=Mes

FormatDateTimePattern.datetiem_format_type_long_day_of_week=Día largo de la semana

FormatDateTimePattern.datetiem_format_type_day_of_month=Día del mes

FormatDateTimePattern.datetiem_format_type_medium_day_of_year=Día medio del año

FormatDateTimePattern.datetiem_format_type_minutes=Minutos

FormatDateTimePattern.datetiem_format_type_secontds=Segundos

FormatDateTimePattern.datetiem_format_type_general_time=Tiempo general

###############################################################################
# LOCALE SPECIAL:
# following text should be localized specifically for each locale, 
# the pattern may be different for each locale. 
# For example, in Polish locale, "general time" pattern should be "HH:mm:ss.SSS" 
# instead of "ahh:mm:ss.SSS".
#
FormatDateTimePattern.pattern.datetiem_format_type_year=aaaa

FormatDateTimePattern.pattern.datetiem_format_type_short_year=aa

FormatDateTimePattern.pattern.datetiem_format_type_long_month_year=MMMM aaaa

FormatDateTimePattern.pattern.datetiem_format_type_shot_month_year=MMM aa

FormatDateTimePattern.pattern.datetiem_format_type_month=MMMM

FormatDateTimePattern.pattern.datetiem_format_type_long_day_of_week=EEEE

FormatDateTimePattern.pattern.datetiem_format_type_day_of_month=dd

FormatDateTimePattern.pattern.datetiem_format_type_medium_day_of_year=MMMM dd, aa

FormatDateTimePattern.pattern.datetiem_format_type_minutes=mm

FormatDateTimePattern.pattern.datetiem_format_type_secontds=seg

FormatDateTimePattern.pattern.datetiem_format_type_general_time=ahh:mm:ss.SSS

FormatDescriptorProvider.DisplayName.None=Automático

ReportExamples.ContributeSamples.description=Envíe una entrada de Bugzilla a {0}Envíe un error a BIRT{1}.\nAdjunte el archivo de diseño de informe y la captura de pantalla de la salida de informe.\n\n Nota: La captura de pantalla debe tener estas características:\n  1. Formato de imagen *.png;\n  2. Resolución de 800*600;\n  3. Tamaño inferior a 250 KB.

ReportExamples.ContributeSamples.title=Muestras de aportación

ReportExamples.ContributeSamples.messages=La información que aparece a continuación le guiará hasta las muestras de aportación.

ReportExamples.OpenBrowser.Error.title=Error

ReportExamples.OpenBrowser.Error.message=No se puede abrir el explorador Web.

###########################################################
#CopyCellContentsContextAction
CopyCellContentsContextAction.actionText=Copiar cont&enidos

###########################################################
#CopyCellContentsAction
CopyCellContentsAction.actionText=Copiar cont&enidos

###########################################################
#CopyFormatAction
CopyFormatAction.text=Copiar &formato

###########################################################
#PasteFormatAction
PasteFormatAction.text=Pegar f&ormato

###########################################################
#InsertAggregationAction
InsertAggregationAction.text=A&gregación


###########################################################
#InsertRelativeTimePeriodAction
InsertRelativeTimePeriodAction.text=Período de tiempo relati&vo

###########################################################
#Multiple views
MultipleAdapter.SetCurrentView=Establecer vista actual

MultipleAdapter.RemoveView=Quitar vista

MultipleGuideHandle.RemoveView=Quitar

CreateChartAction.text=Crear vis&ta de diagrama

CreateChartHandler.text=Crear vista de diagrama

###########################################################
#Input Parameters
InputParameterDialog.msg.title=Parámetros de entrada

InputParameterDialog.msg.requiredParam=Los parámetros marcados con * son obligatorios.

InputParameterDialog.err.requiredParam=No se ha especificado el valor del parámetro necesario [{0}].

InputParameterDialog.err.invalidValueTitle=Valor no válido

InputParameterDialog.err.invalidValue=El valor "{0}" no es válido con el tipo {1}.

InputParameterDialog.err.emptyValue=El valor de entrada para {0} no puede estar vacío.

InputParameterDialog.err.emptyValue2=El segundo valor de entrada {0} no puede estar vacío.

###########################################################
#Crosstab drag tracker
ColumnDragTracker.ResizeColumn=cambiar tamaño de columna

ColumnDragTracker.Show.Label=Ancho:

CPListLabelProvider.misssing=(falta)

RowDragTracker.ResizeRow=cambiar tamaño de fila

RowDragTracker.Show.Label=Altura:

TableDragGuideTracker.Pixels=píxeles

###########################################################
#General Designer settings
GeneralConfigurationBlock.message.general.description=Ajustes generales para Report Designer

GeneralConfigurationBlock.button.text.enable.gradient=Activar &selección gradiente

GeneralConfigurationBlock.button.text.enable.animation=Activar selección a&nimada

GeneralConfigurationBlock.group.label=Solicitar

GeneralConfigurationBlock.group.defaultThemes=Librería de temas predeterminados

GeneralConfigurationBlock.button.text.prompt=Mostrar &solicitud después de abrir el archivo de la librería

GeneralConfigurationBlock.button.text.defaultTheme.enable=Activar librería de &temas predeterminados.

GeneralConfigurationBlock.button.text.defaultTheme.include=&Incluir los temas predeterminados.

GeneralConfigurationBlock.move.binding.group=Mover enlaces locales al principal para disponer de un mejor rendimiento

GeneralConfigurationBlock.move.binding.always=Sie&mpre

GeneralConfigurationBlock.move.binding.never=Nu&nca

GeneralConfigurationBlock.move.binding.prompt=S&olicitud

###########################################################
#ResetImageSizeAction
ResetImageOriginalSizeAction.trans.label=Restablecer tamaño de imagen

ResetImageSizeAction.label=Restablecer al tama&ño original
###########################################################
#Report document editor
ReportDocumentEditor.1=Editor de documentos de informe

ReportDocumentEditor.2=Información de documento

ReportDocumentEditor.3=Nombre:

ReportDocumentEditor.4=Versión:

ReportDocumentEditor.5=Recuento de páginas:

ReportDocumentEditor.errorMessage=Se ha producido un error durante la apertura del documento en el editor.

###########################################################
#Export Element Dialog
ExportElementDialog.Dialog.Title=Exportar elementos a librería

ExportElementDialog.Dialog.Message=Seleccione la librería de destino o cree un nuevo destino.

ExportElementDialog.label.NewFile=&Nuevo nombre de archivo:

ExportElementDialog.ErrorMessage=El nombre de archivo debe terminar en .rptlibrary.

ExportElementDialog.ErrorMessageInvalid=El nombre de archivo no es válido.

ExportElementDialog.ErrorMessageDuplicate=El elemento ya existe en la librería.

ExportElementDialog.ErrorStatusCanNotExport=No se puede exportar el elemento.

ExportElementDialog.WarningMessageDuplicate.Message=El nombre de componente está repetido. ¿Desea sobrescribir el componente actual de la librería?\n\nHaga clic en {0} para sustituir el componente actual.\nHaga clic en {1} para volver al cuadro de diálogo de exportar elemento.\nHaga clic en {2} para cancelar la acción.

ExportElementDialog.WarningMessageDuplicate.Title=Confirmar sustitución

ExportElementDialog.WarningMessageDuplicate.OverrideImage=La imagen incrustada a la que hace referencia la imagen exportada ya existe en la librería. ¿Desea sobrescribirla?

###########################################################
#NewStyleHandler
NewStyleHandler.transaction.label=Nuevo estilo...

AdvancePropertyDescriptorProvider.DisplayName=Avanzar

AdvancePropertyDescriptorProvider.Tooltip.Alphabetic=Ordenar alfabéticamente

AdvancePropertyDescriptorProvider.Tooltip.Group=Ordenar por grupo

AdvancePropertyDescriptorProvider.Tooltip.Local=Mostrar propiedades locales

PropertyEditorFactory.Boolean.True=true

PropertyEditorFactory.Boolean.False=false

PropertyEditorFactory.Value.Auto=Automático

ExportElementToLibraryAction.waring=Advertencia

ExportElementToLibraryAction.waringMsg=El elemento no tiene nombre, pero no se puede establecer su nombre.

###########################################################
#ExportStyleDialog
ExportStyleDialog.Label.Text=&Tema:

ExportStyleDialog.Message=Seleccione el tema que desee exportar a la librería

ExportStyleDialog.Title=Seleccionar tema

###########################################################
#UIUtil
UIUtil.previewconfirm.title=Errores en el informe

UIUtil.previewconfirm.message=El informe contiene errores, ¿desea continuar para realizar la vista previa?

###########################################################
#FileReportProvider
FileReportProvider.CopyConfigFile.DeleteFailure=Error al eliminar archivo: {0}

###########################################################
#ShowPropertiesViewMenu
ShowPropertiesViewMenu.ActionName=Propiedad&es

ShowPropertiesViewMenu.ErrorMessage=Error al abrir la vista de propiedades

ShowPropertiesViewMenu.ErrorTitle=Error

###########################################################
#ResetAttributePage
ResetAttributePage.Style.Restore.TooltipText=Restaurar las propiedades de estilo de la página actual a los parámetros predeterminados

ResetAttributePage.Style.Restore.Transaction.Name=Restaurar estilo
ResetAttributePage.Style.Restore.Menu.Name=Restaurar estilos predeterminados
ResetAttributePage.Style.Restore.All.Transaction.Name=Restaurar todos los estilos
ResetAttributePage.Style.Restore.All.Menu.Name=Restaurar estilos predeterminados en todas las fichas

###########################################################
#TextEditor
TextEditor.PasteFormattedText=Pegar texto con formato

###########################################################
#ReloadImageAction
ReloadImageAction.Text=Volver a cargar la &imagen

###########################################################
#VariablesNodeProvider
VariablesNodeProvider.NewActionName=&Nueva variable

VariablesNodeProvider.Dialog.Title=Nueva variable

VariablesNodeProvider.NodeName=Variables

###########################################################
#VariableNodeProvider
VariableNodeProvider.DialogTitle=Editar variable

###########################################################
#SelectVariableDialog
SelectVariableDialog.AvailableVariables=&Variables disponibles:

SelectVariableDialog.PageSeperator=----- Variables de página -----

SelectVariableDialog.ReportSeperator=----- Variables de informe -----

SelectVariableDialog.Title=Insertar variable

###########################################################
#VariableDialog
VariableDialog.DataType=&Tipo de datos:

VariableDialog.DefaultValue=&Valor predeterminado:

VariableDialog.Name=&Nombre:

VariableDialog.PageVariable=Variable de &página

VariableDialog.ReportVariable=Variable de &informe

VariableDialog.VariableType=Tipo:

VariableDialog.Error.NameDuplicate=El nombre \"{0}\" duplica un nombre existente. Elija un nombre diferente.

###########################################################
#ExpressionBuilderConfigurationBlock
ExpressionBuilderConfigurationBlock.DefaultSyntax=Sintaxis &predeterminada:

###########################################################
#ExpressionEditor
ExpressionEditor.Error.EmptyExpression=La expresión no puede estar vacía.

ExpressionEditor.Label.Expression=&Expresión:

ExpressionEditor.Title=Editar expresión

ExpressionSyntaxColoringPage.Accessible.Name.Background=Selector de color de fondo

ExpressionSyntaxColoringPage.Accessible.Name.Foreground=Selector de color de primer plano

ExpressionSyntaxColoringPage.Button.Bold=&Negrita

ExpressionSyntaxColoringPage.Button.Enable=Activar

ExpressionSyntaxColoringPage.Button.Italic=Curs&iva

ExpressionSyntaxColoringPage.Button.Restore=&Restaurar predeterminado

ExpressionSyntaxColoringPage.Button.Strikethrough=&Tachado

ExpressionSyntaxColoringPage.Button.Underline=&Subrayado

ExpressionSyntaxColoringPage.Decscription=Personalice el resaltado de sintaxis seleccionando el tipo de texto cuyo estilo desee personalizar en la lista o en el área de texto de ejemplo.

ExpressionSyntaxColoringPage.Element.Comment=Comentario

ExpressionSyntaxColoringPage.Element.Default=Predeterminado

ExpressionSyntaxColoringPage.Element.Keyword=Palabra clave

ExpressionSyntaxColoringPage.Element.String=String

ExpressionSyntaxColoringPage.Label.Background=Fond&o:

ExpressionSyntaxColoringPage.Label.Foreground=&Primer plano:

ExpressionSyntaxColoringPage.Label.SampleText=&Texto de muestra:

ExpressionSyntaxColoringPage.Label.SyntaxElement=Elemento de s&intaxis:

ExpressionSyntaxColoringPage.Link.ColorAndFont=Los colores y la fuente predeterminados se pueden configurar en las páginas de preferencias <a href="org.eclipse.ui.preferencePages.GeneralTextEditor">Editores de texto</a> y <a href="org.eclipse.ui.preferencePages.ColorsAndFonts">Colores y fuentes</a> .

###########################################################
#VariableDropAdapter
VariableDropAdapter.TranasctionName=Crear elemento de fecha de la variable

###########################################################
#RenameInputDialog
RenameInputDialog.Message.BlankName=El nombre no puede estar en blanco.

RenameInputDialog.Message.DuplicateName=El nombre duplicado existe.

###########################################################
#Breadcrumb
BreadcrumbItemDropDown.showDropDownMenu.action.toolTip=Mostrar secundarios

ToggleBreadcrumbAction.tooltip.switch.breadcrumb=Alternar ruta de exploración

###########################################################
#HyperlinkParameterProvider Category
ReportHyperlinkParameterProvider.Category.Report=Parámetros de informe

RenderHyperlinkParameterProvider.Category.Render=Opciones de procesamiento
ExceptionDialog.title=Error
