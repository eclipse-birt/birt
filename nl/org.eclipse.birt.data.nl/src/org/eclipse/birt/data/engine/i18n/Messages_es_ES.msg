#************************************************************************
# Copyright (c) 2004, 2008 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#  
#************************************************************************
# Localized messages for the DTE Engine. Messages here give 
# the text of Data Engine exceptions.

###########################################################
#for test only
Error.Msg001=El valor {0} no se encuentra en {0}.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=No se ha definido la fuente de datos: {0}.
data.engine.UndefinedDataSet=No se ha definido el conjunto de datos: {0}.
data.engine.UnsupportedDataSourceType=No se admite el tipo de la fuente de datos "{0}".
data.engine.UnsupportedDataSetType=No se admite el tipo del conjunto de datos "{0}".
data.engine.UnsupportedGroupOnClobAndBLOB=No se admite el grupo en datos "{0}" CLOB o BLOB.
data.engine.UnsupportedSortOnClobAndBLOB=No se admite la clasificación en datos "{0}" CLOB o BLOB.

data.engine.EndOfGroupOnly=Esta expresión de agregación sólo está disponible al final de un grupo.
data.engine.InvalidAggregation=The aggregation definition is invalid:{0}.
data.engine.InvalidAggrGroup=Nivel de agrupación no válido para la función de agregación: {0}.
data.engine.WrongNumAggrArgs=Número de argumentos incorrecto para la función de agregación: {0}.
data.engine.AggrParamGroupLevelInconsistent=In nested aggregation, all the non-constant expression must have same group level.
data.engine.DupPropertyName=Se ha encontrado un nombre de propiedad duplicado: {0}.
data.engine.NoSubQueryName=No se puede encontrar la subconsulta con el nombre: {0}.

data.engine.DataSourceMissingExtId=Falta el id de la extensión en la definición de la fuente de datos {0}.
data.engine.NullGroupStart=El valor de inicio del grupo no puede ser nulo.
data.engine.UnsupportedDirectNestedAggregate=No se admite el anidado directo de totales.

data.engine.ResultClosed=Se ha cerrado QueryResults o su iterador.
data.engine.NoCurrentRow=Result iterator no tiene ninguna fila actual. No ha comenzado o no ha finalizado.
data.engine.InvalidJSExpr=Expresión Javascript no válida: {0}.
data.engine.InvalidTotalFuncName=Nombre de función total no válido: {0}.
data.engine.InvalidTotalFunc=Función total no válida.
data.engine.NoAggrFunc=No se esperaba ninguna llamada de función de agregación en esta expresión.
data.engine.InvalidExprHandle=Controlador de expresión no válido.
data.engine.InvalidAggrGroupExpression=Expresión de grupo incorrecta para la función de agregación: {0}.
data.engine.InvalidSortKeyColumn=Columna de clave de ordenación no válida: {0}.
data.engine.InvalidGroupLevel= El nivel de grupo en agregación: {0} no es válido. 
data.engine.InvalidGroupKeyName=Nombre de columna de clave de grupo no válido: {0}.
data.engine.BadCloseScript=Error al evaluar closeScript en CustomDataSet.
data.engine.InvalidCustomFieldIdx=Índice de campo personalizado no válido:{0}.
data.engine.InvalidGroupName=El nombre de grupo no es válido.
data.engine.DuplicateGroupName=Los nombres de grupo están duplicados.
data.engine.BadOpenScript=Error al evaluar openScript en CustomDataSet.
data.engine.DataSourceIsOpen=La fuente de datos ya está abierta.
data.engine.DataSourceNotOpen=La fuente de datos no está abierta.
data.engine.EmptyCustomFieldName=El nombre de campo personalizado no puede estar vacío.
data.engine.DupCustomFieldName=Nombre de campo personalizado duplicado: {0}.
data.engine.NoResultSet=La ejecución de la consulta no devolvió ningún conjunto de resultados.
data.engine.NoResultMetadata=Los metadatos de resultados no están disponibles.
data.engine.QueryAlreadyPrepared=La consulta ya se ha preparado.
data.engine.QueryNotPrepared=La consulta todavía no se ha preparado.
data.engine.InvalidFieldName=Nombre de campo no válido: {0}.
data.engine.DupColumnName=Nombre de columna duplicado: {0}.
data.engine.DupAlias=Nombre o alias de columna duplicado: {0}.
data.engine.InvalidFieldIndex=Índice de campo no válido: {0}.

data.engine.MatchError=Compruebe la sintaxis del patrón. Error al hacer coincidir el string con el patrón: {0}.

data.engine.ExpectBooleanReturnType=el script {0} para datos en script debe devolver un resultado booleano.
data.engine.BadIntervalUnit=Valor de la unidad de intervalo no válido: {0}.
data.engine.InvalidFieldName=Nombre de campo no válido: {0}.
data.engine.BadGroupKeyType=La clave de grupo tiene un tipo de datos no admitido: {0}.
data.engine.group.interval=No se admite el establecimiento de un intervalo {0} en una columna con el tipo {1}.
data.engine.group.interval.Rowid=El intervalo numérico es el único intervalo que se admite en la columna ROWID.
data.engine.group.interval.invalid=El intervalo indicado no es válido.
data.engine.group.intervalRange.invalid=Rango del intervalo no válido: {0}.
data.engine.NoSupportFilterOnGroup=En este momento no se admite el filtrado en grupos.
data.engine.InvalidTopBottomArgument=El argumento de los filtros Superior/Inferior debe ser un número.
data.engine.InvalidTopBottomNArgument=El argumento de los filtros TopN/BottomN debe ser cero o un entero positivo.
data.engine.InvalidTopBottomPercentArgument=Los argumentos de los filtros TopPercent/BottomPercent deben ser ambos positivo e inferiores o iguales a 100.

data.engine.UnsupportedCondOp=No se admite el operador condicional: {0}.
data.engine.EmptyExpression=La expresión no puede ser nula ni estar vacía.
data.engine.AggregationBinding.EmptyArgument=El enlace de agregación "{1}" requiere el argumento "{0}".

data.engine.InvalidJoinType=Tipo de combinación no válido.
data.engine.InvalidJoinOperator=Operador de combinación no válido.
data.engine.BadDataSetParamName=Parámetro de conjunto de datos no definido: {0}.
data.engine.FailComputeOutputParameterValue=Fallo al evaluar el valor del parámetro de salida: {0}.
data.engine.LinkedReportParamAllowMultiValues=El parámetro de informe "{0}" que admite varios valores no se puede utilizar para enlazar con el parámetro de conjunto de datos "{1}".

#column binding
data.engine.EmptyBindingName=El nombre de enlace no puede ser nulo o estar vacío. 
data.engine.InvalidBoundColumnName=Nombre de columna enlazada no válido: {0}.
data.engine.InvalidGroupKey=La clave de grupo "{0}" del grupo {1} no puede depender de los enlaces de columna definidos en el grupo inferior al mismo. 
data.engine.ColumnBindingNotExist=El enlace de columna "{0}" no existe.
data.engine.ColumnBindingCycle=El enlace "{0}" tiene ciclo de dependencia. 
data.engine.ColumnBindingReferToInexistColumn=El enlace de columna "{0}" hace referencia a un enlace "{1}" que no existe.
data.engine.ColumnBindingReferToInexistBinding=Column binding "{0}" has referred to a data set column "{1}" which does not exist.
data.engine.ColumnBindingReferToAggregationColumnBindingInParentQuery=El enlace de columna "{0}" es incorrecto: los enlaces de la columna de consulta principal que incluyen agregaciones no pueden utilizarse en enlaces de columnas de subconsulta.
data.engine.ReadColumnValueFromDocumentError=Se produce un error al intentar leer un valor de la columna: {0}. Esto puede deberse a un error del tiempo de ejecución en la evaluación de una expresión de enlace, como pueda ser un error de división por 0.
data.engine.NoOuterResultsExist=No existen resultados de consulta externa. Puede deberse a que una referencia "_externa" en una consulta no tiene una consulta principal.
data.engine.DuplicatedBindingName=Nombre de enlace duplicado: {0}.
#computed column value
data.engine.BadCompColExpr=La expresión de la columna procesada "{0}" no es válida.
data.engine.CompCol.FailRetrieveValueComputedColumn=Error al procesar el valor para la columna procesada "{0}".
data.engine.ScriptResult.WrongSystemComputedColumn=Error al procesar el valor de la expresión de filtro, grupo u orden.
data.engine.ComputedColumnCycle=La columna procesada "{0}" tiene ciclo de dependencia.

data.engine.SortOnAggregation=Existe una ordenación no válida cuya clave de ordenación hace referencia a una agregación "{0}".

data.engine.BadFilterExpr=La expresión de filtro "{0}" no es válida.
data.engine.BadFilterDefn=La definición de filtro mediante la expresión "{0}" no es válida.

#clob and blob error
data.engine.opencloberror=Error de apertura de secuencia de caracteres CLOB.
data.engine.readcloberror=Error de lectura de secuencia de caracteres CLOB.
data.engine.openbloberror=Error de apertura de secuencia binaryStream BLOB.
data.engine.readbloberror=Error de lectura de secuencia binaryStream BLOB.

#resultsetcache exception
data.engine.BadDestIndex=El valor {0} de destIndex no es válido. Debe estar entre 0 y {1}.
data.engine.WriteTempError=Se ha encontrado una excepción al escribir en el archivo temporal de un conjunto de datos grande.
data.engine.ReadTempError=Excepción al leer en el archivo temporal de un conjunto de datos grande.

#resultiterator cache exception
data.engine.CreateCacheTempError=Excepción al crear un archivo temporal para filas de resultados de caché.
data.engine.OpenCacheTempError=Excepción al abrir un archivo temporal para filas de resultados de caché.
data.engine.WriteCacheTempError=Excepción al escribir un archivo temporal para filas de resultados de caché.
data.engine.ReadCacheTempError=Excepción al leer un archivo temporal para filas de resultados de caché.
data.engine.CloseCacheTempError=Excepción al cerrar un archivo temporal para filas de resultados de caché.

#load data engine factory exception
load.factory.error=Hay un error al cargar la fábrica del motor de datos.

# Wrapped Error
data.engine.DataConversionError=Se ha producido un error en la conversión del tipo de datos.
data.engine.BadDataTypeCondExpr=Se ha encontrado un tipo de datos no válido en la expresión condicional.
data.engine.BirtException=Se ha producido una excepción BIRT: {0}. Consulte la siguiente excepción para obtener más información.

# data set cache error
data.engine.datasetcache.save.error=Hay un error al guardar los archivos de la caché de datos.
data.engine.datasetcache.load.error=Hay un error al cargar los archivos de la caché de datos.

# error related with data engine context
data.engine.InvalidMode=El valor de modo {0} del contexto del motor de datos no es válido.
data.engine.InvalidArchive=El archivo de informes no puede ser nulo en generaciones o presentaciones. 
data.engine.SaveReportDocumentError=Se ha producido un error en el documento de informe al guardarlo: {0}.
data.engine.LoadReportDocumentError=Se ha producido un error en el documento de informe al cargarlo: {0}.
data.engine.GetSaveStreamError=Se ha producido un error en el documento de informe al obtener la secuencia de guardado.
data.engine.GetLoadStreamError=Se ha producido un error en el documento de informe al obtener la secuencia de carga.
data.engine.NullExpr=La expresión no puede ser nula para recuperar el valor en presentaciones.
data.engine.InValidExpr=Expresión no válida al recuperar el valor en la presentación.
data.engine.BadGroupExpr=La expresión de grupo no puede estar vacía. 
data.engine.filterExprContainRowNum=La expresión de filtro no puede hacer referencia a un objeto de java script "row.__num".
data.engine.sortExprContainRowNum=La expresión de ordenación no puede hacer referencia a un objeto de java script "row.__num".


data.engine.ResultSetNotStart=El conjunto de resultados no se ha iniciado todavía.

data.engine.invalidRowIndex=Valor de índice de fila no válido: {0}.
data.engine.backwardSeekError=Esta versión no admite la búsqueda inversa.
data.engine.startError=Se debe solicitar en primer lugar el método siguiente.

# query running based on report document
data.engine.rd.errorfilters=No se admiten los filtros definidos en la consulta.

# other error
data.engine.ScriptEvalError=Error al evaluar script (fuente:{0}, línea:{1}:  {2}.
data.engine.PreparedQueryClosed=La instancia PreparedQuery está cerrada.
data.engine.NullDefaultInParamValue=El valor predeterminado para el parámetro de entrada no puede ser nulo.
data.engine.BadGroupKeyExpr=La expresión de clave de grupo {0} no es válida.
data.engine.GroupNotExist=El grupo "{0}" utilizado en la columna {1} no existe.
data.engine.BadSortKeyExpr=La expresión de clave de ordenación {0} no es válida.
data.engine.NoParamMetaForDataSource=No se pueden obtener los metadatos de parámetro para este tipo de fuente de datos.
data.engine.UnexpectedError=Error de la operación del motor de datos debido a una excepción inesperada. 
data.engine.ConvertDataTypeError=No se puede convertir el valor del parámetro {0} al tipo {1}.
data.engine.CannotInstantiateAggregationFactory=No se puede crear una instancia de la fábrica de agregaciones {0}.
date.engine.DuplicateAggregationName=Nombre de agregación duplicado: {0}.
data.engine.InconsistentAggregationArgumentDefinition=El argumento de agregación definido en la implementación de la extensión de agregación no es coherente con el definido en el plugin.xml.
data.engine.incorrectGroupKeyValues=La matriz de valores de clave de grupo dada es incorrecta.
data.engine.NoExpressionProcessorAvailable=No hay ningún ExpressionProcessor disponible.
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} no puede convertirse en {1}.
data.engine.NoRowUpdate=La fila de datos no se puede actualizar en este momento.
data.engine.BadDataType=No se admite el tipo de datos{0}.
data.engine.BadDataExpression=La expresión de datos no debe ser nula.
data.engine.aggregation.InvalidBindingExpression=Expresión no aceptada para el enlace de agregación "{0}".
data.engine.InvalidExpression=Expresión no válida"{0}".

data.engine.cacheFunctionWrongMode=La función de caché sólo funciona para el modo DIRECT_PRESENTATION.
data.engine.document.invalidIndexArray=La matriz de índice no es válida.
data.engine.document.invalidGroupItem=El elemento de grupo eliminado no es correcto.
data.engine.resultIteratorNotOpen=Result iterator no se ha abierto todavía.
data.engine.resultIteratorClosed=Result iterator se ha cerrado.
data.engine.invalidOutputParameterIndex=Índice de parámetro de salida no válido: {0}.
data.engine.outputParameterOutOfBound=Índice de parámetro de salida fuera de los límites: {0}.
data.engine.invalidOutputParameterName=Nombre de parámetro de salida no válido: {0}.
data.engine.document.notSupportInPresentation=No admitido en la presentación.
data.engine.wrongStatus=Estado incorrecto.
data.engine.wrongVersion=No admitido en versiones anteriores a la 2.1.
data.engine.unsupportOperationException=Operación no admitida.
data.engine.possibleMixedDataTypeInColumn=Se ha producido un error durante la aplicación de los datos. Esto se puede deber a la inclusión de datos de distinto tipo en una columna de tipo "CUALQUIERA".
data.engine.resultClassSaveError=No se pueden encontrar las referencias de columna siguientes: {0}.
data.engine.invalidMemBufferSize=El tamaño de búfer de memoria "{0}" no es válido. El tamaño de búfer de memoria debe ser un número entero superior a cero. 
data.engine.document.error.cannotLoadStream=La secuencia de documento "{0}" no es válida.
data.engine.failToCreateTempDir=Error al crear el directorio temporal en "{0}"; 
data.engine.reportItem.SubQuery.Unsupported=Los elementos de informe para compartir no se pueden usar para una subconsulta.
data.engine.emptyResultSet=El conjunto de resultados está vacío.
data.engine.errorHappenWhenRetrieveResultSet=Se ha producido un error durante el acceso al conjunto de resultados "{0}". Es posible que el conjunto de resultados no exista.
data.engine.invalidAggregationBindingForPLSReport=El enlace de agregación "{0}" no es válido cuando la seguridad a nivel de página está activada.

# oda consumer errors
odaconsumer.ColumnNameCannotBeEmptyOrNull=El nombre de columna no puede ser nulo ni estar vacío.
odaconsumer.ColumnPositionIsOneBased=Column position is 1-based and cannot be less than 1.
odaconsumer.ColumnAliasCannotBeEmpty=El alias de columna no puede ser una cadena vacía.
odaconsumer.CannotProcessDriverConfig=No se pueden procesar las configuraciones del controlador {0}.
odaconsumer.OdaInitEntryNotFound=No se ha encontrado el punto de entrada inicial del controlador {0} o no se puede crear una instancia del mismo.
odaconsumer.OdaDriverOnUnsupportedPlatform=El controlador {0} no admite {1}.
odaconsumer.CannotGenerateUrl=No se puede generar la URL {0} del cargador de clases.
odaconsumer.UnsupportedNativeType=Tipo nativo {0} no admitido para el tipo de conjunto de datos {1} del controlador {2}.
odaconsumer.UnsupportedDataSetType=El controlador {1} no admite el tipo de conjunto de datos {0}.
odaconsumer.CannotDetermineDefaultDataSetType=Tipo de conjunto de datos nulo. No se puede determinar el tipo de conjunto de datos predeterminado del controlador {0}.
odaconsumer.InvalidDataSetType=Tipo de conjunto de datos no válido {0}. No se puede determinar el tipo de conjunto de datos predeterminado para el conjunto de datos {1}.
odaconsumer.ParameterNameCannotBeEmptyOrNull=El nombre de parámetro no puede ser nulo ni estar vacío.
odaconsumer.ParameterPositionIsOneBased=La posición de parámetro no puede ser menor de 1.
odaconsumer.CannotGetParameterNativeName=No se puede obtener el nombre de parámetro definido por controlador: {0}.
odaconsumer.CannotGetParameterType=No se puede obtener el tipo del parámetro: {0}.
odaconsumer.CannotGetParameterTypeName=No se puede obtener el nombre del tipo del parámetro: {0}.
odaconsumer.CannotGetParameterMode=No se puede obtener el modo del parámetro: {0}.
odaconsumer.CannotGetParameterScale=No se puede obtener la escala del parámetro: {0}.
odaconsumer.CannotGetParameterPrecision=No se puede obtener la precisión del parámetro: {0}.
odaconsumer.CannotGetParameterIsNullable=No se puede determinar la capacidad para aceptar valores nulos del parámetro: {0}.
odaconsumer.SameParamNameForDifferentHints=Hay otro parámetro en el conjunto de datos definido con el mismo nombre {0}.
odaconsumer.DifferentParamNameForSamePosition=Hay otro parámetro {0} en el conjunto de datos definido con la misma posición {1}.
odaconsumer.ParameterValueIsNull=El valor del parámetro es nulo.
odaconsumer.UnsupportedParameterValueType=Tipo de valor de parámetro no admitido: {0}.
odaconsumer.UnknownExceptionThrown=Se ha producido una excepción desconocida en la última llamada del establecedor.
odaconsumer.CannotConvertIndexedParameterValue=No se puede convertir el valor del parámetro {0} en el índice {1} del tipo de valor de {2} al tipo de ODA de {3}.
odaconsumer.CannotConvertNamedParameterValue=No se puede convertir el valor del parámetro {0} con el nombre de parámetro de {1} del tipo de valor de {2} al tipo de ODA de {3}.
odaconsumer.UnrecognizedProjectedColumnName=Nombre de columna proyectada no reconocido: {0}.
odaconsumer.ColumnNameOrAliasAlreadyUsed=El nuevo nombre o alias de columna {0} ya se está utilizando en la columna del índice {1}.
odaconsumer.NamedResultSetsUnsupported=Esta sentencia no admite grupos de resultados con nombre.
odaconsumer.OutputParameterUnsupported=Esta sentencia no admite parámetros de salida.
odaconsumer.UnsupportedMultipleResultSets=Esta sentencia no admite varios grupos de resultados.
odaconsumer.CannotGetConnectionMetaData=No se pueden obtener los metadatos de conexión.
odaconsumer.CannotGetMaxConnections=No se puede obtener el número máximo de conexiones de la fuente de datos.
odaconsumer.CannotGetMaxStatements=No se puede obtener el número máximo de consultas para la fuente de datos.
odaconsumer.CannotGetDataSetMetaData=No se pueden obtener los metadatos del conjunto de datos {0} de la fuente de datos.
odaconsumer.CannotPrepareStatement=Error al preparar la siguiente consulta para el tipo de conjunto de datos {1}. \n[{0}]
odaconsumer.CannotCloseConnection=No se puede cerrar la conexión.
odaconsumer.CannotOpenConnection=No se puede abrir la conexión del controlador: {0}.
odaconsumer.CannotDetermineSupportForMultipleOpenResults=No se puede determinar la asistencia para varios resultados abiertos.
odaconsumer.CannotDetermineSupportForMultipleResultSets=No se puede determinar la asistencia para varios grupos de resultados.
odaconsumer.CannotDetermineSupportForNamedResultSets=No se puede determinar la asistencia para grupos de resultados con nombre.
odaconsumer.CannotDetermineSupportForNamedParameters=No se puede determinar la asistencia para parámetros con nombre.
odaconsumer.CannotDetermineSupportForInParameters=No se puede determinar la asistencia para parámetros de entrada.
odaconsumer.CannotDetermineSupportForOutParameters=No se puede determinar la asistencia para parámetros de salida.
odaconsumer.CannotSetStatementProperty=No se puede establecer la propiedad de la sentencia.
odaconsumer.CannotSetSortSpec=No se puede establecer la especificación de orden.
odaconsumer.CannotSetMaxRows=No se puede establecer el número máximo de filas.
odaconsumer.CannotGetResultSetMetaData=No se pueden obtener los metadatos del grupo de resultados.
odaconsumer.CannotGetMetaDataForNamedResultSet=No se pueden obtener los metadatos del grupo de resultados con nombre: {0}.
odaconsumer.CannotExecuteStatement=No se puede ejecutar la sentencia.
odaconsumer.CannotGetResultSet=No se puede obtener el grupo de resultados.
odaconsumer.CannotGetNamedResultSet=No se puede obtener el grupo de resultados con nombre: {0}.
odaconsumer.CannotGetMoreResults=No se pueden obtener más conjuntos de resultados de la sentencia.
odaconsumer.CannotFindOutParameter=No se puede encontrar el parámetro de salida: {0}.
odaconsumer.CannotCloseStatement=No se puede cerrar la sentencia.
odaconsumer.CannotCancelStatement=No es posible cancelar la sentencia.
odaconsumer.CannotGetColumnCount=No se puede obtener el recuento de columnas.
odaconsumer.CannotGetColumnName=No se puede obtener el nombre de la columna: {0}.
odaconsumer.CannotGetColumnLabel=No se puede obtener la etiqueta de la columna: {0}.
odaconsumer.CannotGetColumnType=No se puede obtener el tipo de la columna: {0}.
odaconsumer.CannotFetchNextRow=No se puede recuperar la fila de datos siguiente.
odaconsumer.CannotDetermineWasNull=No se puede determinar si el último valor recuperado era nulo.
odaconsumer.CannotGetIntFromColumn=No se puede obtener el valor entero de la columna: {0}.
odaconsumer.CannotGetDoubleFromColumn=No se puede obtener el valor doble de la columna: {0}.
odaconsumer.CannotGetStringFromColumn=No se puede obtener el valor de string de la columna: {0}.
odaconsumer.CannotGetBigDecimalFromColumn=No se puede obtener el valor decimal de la columna: {0}.
odaconsumer.CannotGetDateFromColumn=No se puede obtener el valor de fecha de la columna: {0}.
odaconsumer.CannotGetTimeFromColumn=No se puede obtener el valor de hora de la columna: {0}.
odaconsumer.CannotGetTimestampFromColumn=No se puede obtener el valor de fecha/hora de la columna: {0}.
odaconsumer.CannotGetClobFromColumn=No se puede obtener el valor CLOB de la columna: {0}.
odaconsumer.CannotGetBlobFromColumn=No se puede obtener el valor BLOB de la columna: {0}.
odaconsumer.CannotGetBooleanFromColumn=No se puede obtener el valor booleano de la columna: {0}.
odaconsumer.CannotGetRowPosition=No se puede obtener la posición de fila actual.
odaconsumer.CannotCloseResultSet=No se puede cerrar el grupo de resultados.
odaconsumer.CannotGetParameterCount=No se puede obtener el recuento de parámetros.
odaconsumer.CannotGetIntFromParameter=No se puede obtener el valor entero del parámetro: {0}.
odaconsumer.CannotGetDoubleFromParameter=No se puede obtener el valor doble del parámetro: {0}.
odaconsumer.CannotGetStringFromParameter=No se puede obtener el valor de string del parámetro: {0}.
odaconsumer.CannotGetBigDecimalFromParameter=No se puede obtener el valor decimal del parámetro: {0}.
odaconsumer.CannotGetDateFromParameter=No se puede obtener el valor de fecha del parámetro: {0}.
odaconsumer.CannotGetTimeFromParameter=No se puede obtener el valor de hora del parámetro: {0}.
odaconsumer.CannotGetTimestampFromParameter=No se puede obtener el valor de fecha/hora del parámetro: {0}.
odaconsumer.CannotGetBlobFromParameter=No se puede obtener el valor BLOB del parámetro: {0}.
odaconsumer.CannotGetClobFromParameter=No se puede obtener el valor CLOB del parámetro: {0}.
odaconsumer.CannotGetBooleanFromParameter=No se puede obtener el valor booleano del parámetro: {0}.
odaconsumer.CannotFindInParameter=No se puede encontrar el parámetro de entrada: {0}.
odaconsumer.CannotSetIntParameter=No se puede establecer el valor entero ({0}) del parámetro {1}.
odaconsumer.CannotSetDoubleParameter=No se puede establecer el valor doble ({0}) del parámetro {1}.
odaconsumer.CannotSetStringParameter=No se puede establecer el valor de string ({0}) del parámetro {1}.
odaconsumer.CannotSetBigDecimalParameter=No se puede establecer el valor decimal ({0}) del parámetro {1}.
odaconsumer.CannotSetDateParameter=No se puede establecer el valor de fecha ({0}) del parámetro {1}.
odaconsumer.CannotSetTimeParameter=No se puede establecer el valor de hora ({0}) del parámetro {1}.
odaconsumer.CannotSetTimestampParameter=No se puede establecer el valor de fecha/hora ({0}) del parámetro {1}.
odaconsumer.CannotSetBooleanParameter=No se puede establecer el valor booleano ({0}) del parámetro {1}.
odaconsumer.CannotSetNullParameter=No se puede establecer el valor nulo del parámetro {0}.
odaconsumer.CannotClearInParameters=No se pueden eliminar los valores de los parámetros de entrada.
odaconsumer.UnrecognizedOdaType=Tipo de datos ODA no reconocido: {0}
odaconsumer.CannotGetColumnNativeDataTypeName=No se puede obtener el nombre del tipo de datos nativo de la columna: {0}
odaconsumer.CannotGetParameterMetaData=No se pueden obtener los metadatos del parámetro del controlador ODA.
odaconsumer.CannotLoadOdaAdapter=No es posible cargar el controlador {0}; falta el complemento del adaptador DTP-to-BIRT ODA {1}.
odaconsumer.MustSpecifyColumnName=Se debe especificar un nombre de columna al añadir una sugerencia de columna.
odaconsumer.CannotFindLogDirectory=No se puede encontrar el directorio de registro.
odaconsumer.ParameterNames=\nParámetro definido con nombre de modelo ({0}) y nombre de definición por controlador ({1}).
odaconsumer.InvalidArgument=El método ({0}) se solicitó con un argumento no válido ({1}).

odaconsumer.manager.CannotGetNamedResultsAfterExecuteQuery=No se pueden obtener los grupos de resultados por nombre después de que se haya llamado a executeQuery().
odaconsumer.manager.CannotSetParamBeforePrepare=No se pueden establecer parámetros de salida antes de preparar la sentencia.
odaconsumer.manager.CannotGetParamBeforeExecute=No se pueden establecer parámetros de salida antes de ejecutarse la sentencia.
odaconsumer.manager.CannotGetParamMdBeforePrepare=No se pueden obtener los metadatos de parámetro antes de preparar la sentencia.
odaconsumer.manager.CannotSetSortSpecBeforePrepare=No se puede establecer la especificación de orden antes de preparar la sentencia.
odaconsumer.manager.MaxConcurrentConnectionsReached=Se ha alcanzado el número máximo de conexiones simultáneas activas.
odaconsumer.manager.ConnectionFailed=No es posible abrir la conexión.
odaconsumer.manager.CannotConstructConnectionFactory=No es posible crear la fábrica de conexión del controlador ODA.
odaconsumer.manager.InadequatePermissionsForCompatibility=No se puede proporcionar compatibilidad retroactiva porque los permisos no son los adecuados.
odaconsumer.manager.ConnectionIsInactive=La conexión establecida con la fuente de datos no está activa.
odaconsumer.manager.MaxConcurrentStatementsReached=Se ha alcanzado el número máximo de sentencias simultáneas activas para esta conexión.
odaconsumer.manager.PrepareFailed=Error de preparación.
odaconsumer.manager.CannotGetResultSetMdBeforePrepare=No se pueden obtener los metadatos del grupo de resultados antes de preparar una sentencia.
odaconsumer.manager.CannotExecuteBeforePrepare=No se puede ejecutar la sentencia antes de prepararla.


# Data Olap error code
data.olap.OlapDirCreateFail=Error al crear directorio {0}.
data.olap.OlapFileCreateFail=Error al crear archivo {0}.
data.olap.OlapFileNotFound=Archivo Olap {0} no encontrado.
data.olap.OlapFileFormatInvalid=El formato del archivo no es válido. El nombre del objeto de datos es: {0}.
data.olap.DimensionNotExist=La dimensión {0} no existe.
data.olap.KeyValueCanNotBeNull=El valor de la columna de clave no puede ser nulo. Columna de clave: {0}.
data.olap.DetailMemberHaveMultiParent=El miembro de detalle {0} tiene más de un principal.
data.olap.OlapFileDataError=Datos no válidos en el objeto de los datos {0}.
data.olap.DocumentObjectAlreadyExists =El objeto del documento ya existe. Error al crear objeto de datos: {0}.
data.olap.DocumentObjectNotExists =No se ha podido cargar el cubo "{0}" del documento. El objeto de documento no existe.
data.olap.FacttableRowNotDistinct=Las filas deben ser diferentes en la tabla de hechos. "{0}" aparece más de una vez.
data.olap.FacttableJointColumnNotExist=La columna de combinación "{0}" no existe en dimensiones.
data.olap.ParameterColumnOfAggregationNotExist=La columna de parámetro "{0}" no existe en dimensiones.
data.olap.FacttableNullMeasureValue=El valor de una columna de medida es nulo "{0}".
data.olap.InvalidDimensionPositionFacttableRow=El valor clave de dimensión"{0}" no existe en la dimensión "{1}".
data.olap.measureNameNotFound=No se ha encontrado el nombre de medida "{0}".
data.olap.levelNameNotFound=No se ha encontrado el nombre de nivel "{0}".
data.olap.dimensionNameNotFound=No se ha encontrado el nombre de dimensión "{0}".
data.olap.ReferencedDimensionNotFound=Definición de la dimensión a la que se hace referencia:{0} no existe.
data.olap.ReferencedLevelNotFound=El nivel de referencia no existe.
data.olap.cursorPositionError=El cursor no puede navegar a la posición objetivo.
data.olap.cursorSeekError=El cursor no puede navegar a la posición de búsqueda.
data.olap.CannotGetMeasureValue=No se puede encontrar la definición "{0}" de medida en el cubo, la medida no existe o no es válida.
data.olap.CannotFindLevel=No se puede encontrar el nivel de definición "{0}" en la jerarquía "{1}" de la dimensión "{2}".
data.olap.tryToAddIdenticalAggrGroup=El valor "{0}" ya existe en la adición de la lista de enlace "{1}";
data.olap.NoEdgeDefinitionFound=Error al ejecutar la consulta, ya que no contiene ni extremo de fila, ni extremo de columna. Se precisa la definición de un extremo como mínimo.
data.olap.invalidSortDefinition=La definición de orden no es válida. 
data.olap.invalidMeasureRef="{0}" no es una expresión de referencia de medida válida.
data.olap.invalidLevelAttr=El atributo no es válido:
data.olap.referencedBindingNotExist=El enlace referenciado "{0}" no existe.
data.olap.invalidBindingReferToInexitDim=El enlace "{0}" hace referencia a niveles no válidos.
data.olap.invalidBindingReferToInexitMeasure=El enlace "{0}" hace referencia a medidas no válidas.  
data.olap.invalidBindingMissingAggrFunc=Falta la función de agregación de enlace "{0}".
data.olap.invalidAggrArgument=Sólo el atributo de nivel puede servir como argumento de agregación.
data.olap.axis.level.CantBeNull=El nivel de los elementos de la columna no puede ser nulo.
data.olap.axis.value.CantBeNull=El valor de los elementos de la columna del nivel \"{0}\" no puede ser nulo.
data.olap.fetchlimitWarning=El tamaño de recuperación es {0}, menor que la longitud de todo el conjunto de resultados[{1}].
data.olap.failComputeComputedMeasureValue=Error al procesar los valores para la medida procesada.
data.olap.duplicateMeasureName=Duplicar nombre de medida \"{0}\".
data.olap.dataBindingExpressionEmpty=La expresión de enlace de datos no puede ser nula ni estar vacía.
data.olap.missingParentCursor=El conjunto de resultados principal no existe.
data.olap.CannotAccessNullDimensionRow=No se puede acceder a filas de dimensión nula
data.olap.NonexistentDimension=Dimensión inexistente:
data.olap.NonexistentLevel=Nivel inexistente: 
data.olap.NonexistentKeyOrAttr=Clave o atributo inexistente:
data.olap.UnsupportedFunction=Función no admitida:
data.olap.UnspecifiedBindingName=Nombre de enlace no especificado.
data.olap.NotNestAggregationBinding=Sin enlace de agregación de nido de cubo: {0}
data.olap.InvalidNestAggregationExpression=La expresión de una agregación de nido de cubo no es válida.
data.olap.sort.exprError=El nivel de la dimensión a la que se hace referencia establecido sólo debería contener un nivel.
data.olap.query.noCubeBinding=Failed to execute the cube query because no cube binding is available!

data.cache.UnsupportedIncrementalCacheMode=El modo \"{0}\" de caché de incremento no se admite aún.
# Configuration file parser error code
data.executor.configFileParser.getModeByID=Error al obtener el modo de caché de incremento mediante el nombre del conjunto de datos.
data.executor.configFileParser.getTSFormatByID=Error al analizar el formato de fecha/hora mediante el nombre de conjunto de datos especificados desde el archivo de configuración.
data.executor.configFileParser.getTimeStampColumnByID=Error al analizar el formato de fecha/hora mediante el nombre de conjunto de datos especificados desde el archivo de configuración.
data.executor.configFileParser.getQueryTextByID=Error al obtener el texto de consulta mediante el nombre de conjunto de datos especificados desde el archivo de configuración.
data.executor.configFileParser.getParametersByID=Error al obtener los parámetros por el nombre de conjunto de datos especificados desde el archivo de configuración.

data.aggregation.expression.displayName=Expresión
data.aggregation.argumentError=Expresión del argumento \"{0}\" para la agregación \"{1}\" nula o vacía.
