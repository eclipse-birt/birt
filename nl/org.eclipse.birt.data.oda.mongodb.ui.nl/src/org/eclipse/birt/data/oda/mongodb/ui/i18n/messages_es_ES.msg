##************************************************************************
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#  
#*************************************************************************
#
MongoDBDataSourceWizardPage.message.default=Propiedades de conexión de fuente de datos Mongo

MongoDBDataSourceWizardPage.GroupTitle.URIElements=Elementos URI de base de datos Mongo

MongoDBDataSourceWizardPage.text.label.ServerHost=&Anfitrión del servidor:

MongoDBDataSourceWizardPage.text.label.ServerPort=P&uerto del servidor:

MongoDBDataSourceWizardPage.text.label.DatabaseName=&Nombre de base de datos:

MongoDBDataSourceWizardPage.text.label.UserName=Nombre de u&suario:

MongoDBDataSourceWizardPage.text.label.Password=Cont&raseña:

MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=URI de base de datos &Mongo:

MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=Ajustes suplementarios del &cliente

MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=Mantener socket &activo

MongoDBDataSourceWizardPage.checkbox.label.RequestSession=Sesión de &solicitud coherente

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=Falta propiedad de conexión necesaria

#
MongoDBDataSetWizardPage.label.DBName=Base de datos Mon&go:

MongoDBDataSetWizardPage.label.DBCollection=&Colección MongoDB

MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=&Incluir colección del sistema

MongoDBDataSetWizardPage.Button.Refresh=&Actualizar

MongoDBDataSetWizardPage.label.DocumentNumber=Nú&mero de documentos para buscar campos:

MongoDBDataSetWizardPage.group.text=&Operación de comando

MongoDBDataSetWizardPage.label.OperationType=&Tipo de comando:

MongoDBDataSetWizardPage.Button.FindFields=Busca&r campos

MongoDBDataSetWizardPage.Button.text.CommandExpression=&Expresión...

MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=Editar la expresión de comando.

MongoDBDataSetWizardPage.Button.QueryExpression=Expresión de $&consulta...

MongoDBDataSetWizardPage.Button.SortExpression=&Expresión de ordenación...

MongoDBDataSetWizardPage.Button.AdvancedSettings=Ajustes a&vanzados...

MongoDBDataSetWizardPage.label.AvailableFields=Campos &disponibles:

MongoDBDataSetWizardPage.label.SelectedFields=Campos &seleccionados:

MongoDBDataSetWizardPage.HeaderText.SelectedFields=Nombre en pantalla del campo

MongoDBDataSetWizardPage.menuItem.remove=Eli&minar

MongoDBDataSetWizardPage.menuItem.removeAll=Quitar to&do

MongoDBDataSetWizardPage.message.default=Seleccione los campos deseados para las columnas de conjunto de datos

MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=Confirmar para continuar

MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=Si selecciona otra colección se descartarán los campos seleccionados actualmente.  ¿Desea continuar?

MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=Confirmar para actualizar

MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=¿Desea actualizar los campos disponibles según la expresión de comando modificada?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=Error

MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=No es posible obtener la lista de colecciones de la fuente de datos MongoDB.

MongoDBDataSetWizardPage.ExceptionDialog.title=Error

MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=No es posible encontrar los campos disponibles.

MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=Ninguna conexión de fuente de datos activa.

MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=Se ha producido un error al actualizar el conjunto de datos MongoDB.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=Expresión de comando no válida.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=Expresión de consulta no válida.

MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=Expresión de ordenación no válida.

MongoDBDataSetWizardPage.message.error.MissingCollectionName=El nombre de colección no puede estar vacío.

MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=Introduzca un valor entero en el número de documentos para encontrar los campos

MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=El campo seleccionado "{0}" no existe.

MongoDBDataSetWizardPage.message.error.MissingCommandExpression=La expresión de comando no puede estar vacía.

MongoDBDataSetWizardPage.message.error.MissingSelectedField=Se debe seleccionar al menos un campo.

MongoDBAdvancedSettingsDialog.dialogTitle=Propiedades avanzadas del tiempo de ejecución del conjunto de datos

MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=Máximo de objetos de resultado en un &lote:

MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=El número máximo de objetos de resultado recuperados en un lote\n por el cursor MongoDB.  Valor predeterminado = 100

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=Máximo de documentos para buscar para &metadatos:

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=El número de documentos que se deben procesar para obtener los metadatos del conjunto\n de resultados de una consulta en tiempo de ejecución.  Valor predeterminado = 10

MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=Máximo de documentos para o&mitir:

MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=Omitir los primeros n documentos en el conjunto de resultados; tipo de datos enteros.\nValor predeterminado = 0

MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=Expresión de sugeren&cia de índice:

MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=Una de las distintas claves de índice que debe usar la consulta para la optimización de la misma.\n Use la sintaxis de la expresión de sugerencia JSON de Mongo para mostrar las claves indexadas.

MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=Preferencia de lectura de cursor de consultas

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=Mo&do:

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=El modo de preferencia de lectura de una operación de cursor de consultas.

MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=Co&njuntos de etiquetas:

MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=Conjuntos de etiquetas necesarias configuradas para un nodo miembro.\n  Los conjuntos de etiquetas alternativos deben especificarse como documentos en una matriz.

MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=&Sin límite de tiempo de cursor

MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=Normalmente, el servidor alcanza el límite de tiempo después de un periodo de inactividad (10 minutos) para evitar el uso excesivo de la memoria.\n Esto se puede evitar al activar esta opción.

MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=Permite resultados &parciales

MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=Cuando se utiliza con la fragmentación (mongos), se pueden obtener resultados parciales a partir de un sistema fragmentado cuando existan\n fragmentos que dependan/falten en el clúster.  En caso contrario, el servidor de mongos devolverá un error.

MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=&Contraer colecciones anidadas

MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=Una opción de usuario para contraer una colección de campos anidada en varias filas de conjunto de datos.

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=Requiere un valor entero para "máximo de objetos de resultado".

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=Requiere un valor entero para "máximo de objetos para buscar".

MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=Requiere un valor entero para "máximo de objetos para omitir".

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=Restablecer ajustes de diseño

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=Aceptar

MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=El valor de tiempo de diseño de "Número de documentos para buscar campos" se ha restablecido para que coincida con el valor de propiedad de tiepo de ejecución "Máximo de documentos para buscar metadatos".

MongoDBExpressionBuilder.dialogTitle.default=Compilador de expresiones

MongoDBExpressionBuilder.ExprssionText.label=Texto de la e&xpresión:

MongoDBExpressionBuilder.Button.import=I&mportar...

MongoDBExpressionBuilder.Button.tooltip.import=Importar texto de la expresión de un archivo local

MongoDBExpressionBuilder.Button.export=&Exportar...

MongoDBExpressionBuilder.Button.tooltip.export=Exportar el texto de la expresión a un archivo local

MongoDBExpressionBuilder.Button.ValidateSyntax=&Validar sintaxis

MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=Validar la sintaxis del texto de la expresión.

MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=Validación de texto de expresión

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=Sintaxis de expresión válida.

MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=Sintaxis de expresión no válida.

MongoDBExpressionBuilder.CommandExpression.DialogTitle=Compilador de expresión de comando

MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=El texto de expresión no puede estar vacío.

MongoDBExpressionBuilder.QueryExpression.DialogTitle=Compilador de expresión de consulta

MongoDBExpressionBuilder.SortExpression.DialogTitle=Compilador de expresión de ordenación





