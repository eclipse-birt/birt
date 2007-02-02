# Localized messages for the DTE Engine. Messages here give 
# the text of Design Engine exceptions.

###########################################################
#for test only
Error.Msg001=El valor {0} no se encuentra en {0}.

###########################################################

# DTE error code
data.engine.UndefinedDataSource=No se ha definido la fuente de datos: {0}
data.engine.UndefinedDataSet=No se ha definido el conjunto de datos: {0}
data.engine.UnsupportedDataSourceType=No se admite el tipo de la fuente de datos "{0}".
data.engine.UnsupportedDataSetType=No se admite el tipo del conjunto de datos "{0}".
data.engine.UnsupportedGroupOnClobAndBLOB=No se admite el grupo en datos "{0}" CLOB o BLOB.
data.engine.UnsupportedSortOnClobAndBLOB=No se admite la clasificación en datos "{0}" CLOB o BLOB.

data.engine.EndOfGroupOnly=Esta expresión de agregación sólo está disponible al final de un grupo
data.engine.InvalidAggrGroup=Nivel de agrupación no válido para la función de agregación: {0}
data.engine.WrongNumAggrArgs=Número de argumentos incorrecto para la función de agregación: {0}

data.engine.DupPropertyName=Se ha encontrado un nombre de propiedad duplicado: {0}
data.engine.NoSubQueryName=No se puede encontrar la subconsulta con el nombre: {0}

data.engine.DataSourceMissingExtId=Falta el id de la extensión en la definición de la fuente de datos {0}.
data.engine.NullGroupStart=El valor de inicio del grupo no puede ser nulo.
data.engine.UnsupportedDirectNestedAggregate=No se admite el anidado directo de totales.

data.engine.ResultClosed=Se ha cerrado QueryResults o su iterador.
data.engine.NoCurrentRow=Result iterator no tiene ninguna fila actual. No ha comenzado o no ha finalizado.
data.engine.InvalidJSExpr=Expresión Javascript no válida: {0}
data.engine.InvalidTotalFuncName=Nombre de función total no válido: {0}
data.engine.InvalidTotalFunc=Función total no válida.
data.engine.NoAggrFunc=No se esperaba ninguna llamada de función de agregación en esta expresión.
data.engine.InvalidExprHandle=Controlador de expresión no válido.
data.engine.InvalidAggrGroupExpression=Expresión de grupo incorrecta para la función de agregación: {0}
data.engine.InvalidSortKeyColumn=Columna de clave de ordenación no válida: {0}
data.engine.InvalidGroupLevel=Nivel de grupo no válido: {0}
data.engine.InvalidGroupKeyName=Nombre de columna de clave de grupo no válido: {0}
data.engine.BadCloseScript=Error al evaluar closeScript en CustomDataSet.
data.engine.InvalidCustomFieldIdx=Índice de campo personalizado no válido:{0}
data.engine.InvalidGroupName=El nombre de grupo no es válido.
data.engine.DuplicateGroupName=Los nombres de grupo están duplicados.
data.engine.BadOpenScript=Error al evaluar openScript en CustomDataSet.
data.engine.DataSourceIsOpen=La fuente de datos ya está abierta.
data.engine.DataSourceNotOpen=La fuente de datos no está abierta.
data.engine.EmptyCustomFieldName=El nombre de campo personalizado no puede estar vacío.
data.engine.DupCustomFieldName=Nombre de campo personalizado duplicado: {0}
data.engine.NoResultSet=La ejecución de la consulta no devolvió ningún conjunto de resultados.
data.engine.NoResultMetadata=Los metadatos de resultados no están disponibles.
data.engine.QueryAlreadyPrepared=La consulta ya se ha preparado.
data.engine.QueryNotPrepared=La consulta todavía no se ha preparado.
data.engine.InvalidFieldName=Nombre de campo no válido: {0}
data.engine.DupColumnName=Nombre de columna duplicado: {0}
data.engine.DupAlias=Nombre o alias de columna duplicado: {0}
data.engine.InvalidFieldIndex=Índice de campo no válido: {0}

data.engine.MatchError=Compruebe la sintaxis del patrón. Error al hacer coincidir string con el patrón: {0} 
data.engine.BadOperandType=Tipo de datos de operando inesperado: {0}

data.engine.BadParameterCount=Se ha proporcionado un número incorrecto de parámetros.
data.engine.BadParameterType=Uno de los parámetros tiene un tipo de datos incorrecto.
data.engine.NoIrrSolution=No se puede encontrar una solución para la función Irr con los datos proporcionados.
data.engine.ExpectBooleanReturnType=el script {0} para datos en script debe devolver un resultado booleano.
data.engine.BadIntervalUnit=Valor de la unidad de intervalo no válido: {0}
data.engine.InvalidFieldName=Nombre de campo no válido: {0}
data.engine.BadGroupKeyType=La clave de grupo tiene un tipo de datos no admitido: {0}
data.engine.group.interval=No se admite el establecimiento de un intervalo {0} en una columna con el tipo {1}.
data.engine.group.interval.Rowid=El intervalo numérico es el único intervalo que se admite en la columna rowId.
data.engine.group.interval.invalid=El intervalo indicado no es válido.
data.engine.NoSupportFilterOnGroup=En este momento no se admite el filtrado en grupos.
data.engine.InvalidTopBottomArgument=El argumento de los filtros Superior/Inferior debe ser un número.
data.engine.InvalidTopBottomNArgument=El argumento de los filtros TopN/BottomN debe ser cero o un entero positivo.
data.engine.InvalidTopBottomPercentArgument=Los argumentos de los filtros TopPercent/BottomPercent deben ser ambos positivo e inferiores o iguales a 100.
data.engine.InvalidTopBottomN=El número total de filas procesadas debe ser inferior a {0}
data.engine.InvalidPercentileArgument=La función Total.Percentile sólo acepta números entre el 0 y 1 (incluidos) como segundo argumento.
data.engine.InvalidQuartileArgument=La función Total.Quartile sólo acepta enteros entre 0 y 4 (incluidos) como segundo argumento.
data.engine.BadAggrFuncParam=Parámetro no válido para la función de agregación: {0}
data.engine.ResetRateGuess=El usuario debe restablecer un nuevo rate guess.
data.engine.UnsupportedCondOp=No se admite el operador condicional: {0}
data.engine.EmptyExpression=La expresión no puede ser nula ni estar vacía.

data.engine.InvalidJoinType=Tipo de combinación no válido.
data.engine.InvalidJoinOperator=Operador de combinación no válido.
data.engine.BadDataSetParamName=Parámetro de conjunto de datos no definido: {0}.

#column binding
data.engine.InvalidBoundColumnName=Nombre de columna enlazada no válido: {0}.
data.engine.InvalidGroupKey=La clave de grupo "{0}" del grupo {1} no puede depender de los enlaces de columna definidos en el grupo inferior al mismo. 
data.engine.ColumnBindingNotExist=El enlace de columna "{0}" no existe.
data.engine.ColumnBindingCycle=El enlace de columna "{0}" tiene un ciclo de dependencia.
data.engine.ColumnBindingReferToInexistColumn=Column binding "{0}" has referred to a data set column "{1}" which is not exist.
#computed column value
data.engine.BadCompColExpr=La expresión de la columna procesada "{0}" no es válida.
data.engine.CompCol.WrongDataType=El tipo de datos de la columna procesada "{0}" es {1}, pero uno de sus valores es "{2}", que no se puede convertir en {1}.
data.engine.ScriptResult.WrongDataType=El tipo de datos del resultado de la expresión de script es {0}, pero uno de sus valores es "{1}", que no se puede convertir en {0}.

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
data.engine.SaveReportDocumentError=Se ha producido un error en el documento de informe al guardarlo: {0}
data.engine.LoadReportDocumentError=Se ha producido un error en el documento de informe al cargarlo: {0}
data.engine.GetSaveStreamError=Se ha producido un error en el documento de informe al obtener la secuencia de guardado.
data.engine.GetLoadStreamError=Se ha producido un error en el documento de informe al obtener la secuencia de carga.
data.engine.NullExpr=La expresión no puede ser nula para recuperar el valor en presentaciones.
data.engine.InValidExpr=Expresión no válida al recuperar el valor en la presentación.
data.engine.BadGroupExpr=La expresión de grupo no puede estar vacía. 
data.engine.ResultSetNotStart=El conjunto de resultados no se ha iniciado todavía.

data.engine.invalidRowIndex=Valor de índice de fila no válido: {0}
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
date.engine.DuplicateAggregationName=Duplicar nombre de agregación: {0}
data.engine.incorrectGroupKeyValues=La matriz de valores de clave de grupo dada es incorrecta.
data.engine.NoExpressionProcessorAvailable=No hay ningún ExpressionProcessor disponible.
data.engine.InconvertibleDatatype=The original dataType of value {0} is {1} which can not be converted to the assigned dataType {2}.
data.engine.ConvertToDataTypeError={0} no puede convertirse en {1}.
data.engine.NoRowUpdate=La fila de datos no se puede actualizar en este momento.
data.engine.BadDataType=No se admite el tipo de datos.
data.engine.BadDataExpression=La expresión de datos no debe ser nula.

data.engine.cacheFunctionWrongMode=La función de caché sólo funciona para el modo DIRECT_PRESENTATION.
data.engine.document.invalidIndexArray=La matriz de índice no es válida.
data.engine.document.invalidGroupItem=El elemento de grupo eliminado no es correcto.
data.engine.resultIteratorNotOpen=Result iterator no se ha abierto todavía.
data.engine.resultIteratorClosed=Result iterator se ha cerrado.
data.engine.invalidOutputParameterIndex=Índice de parámetro de salida no válido: {0}
data.engine.outputParameterOutOfBound=Índice de parámetro de salida fuera de los límites: {0}
data.engine.invalidOutputParameterName=Nombre de parámetro de salida no válido: {0}
data.engine.document.notSupportInPresentation=No admitido en la presentación.
data.engine.wrongStatus=Estado incorrecto.
data.engine.wrongVersion=No admitido en versiones anteriores a la 2.1.
data.engine.unsupportOperationException=Operación no admitida.
data.engine.resultClassSaveError=Algunas referencias de columnas en el enlace de columna no existen.
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
odaconsumer.CannotGetParameterTypeName=No se puede obtener el nombre del tipo del parámetro: {0}
odaconsumer.CannotGetParameterMode=No se puede obtener el modo del parámetro: {0}
odaconsumer.CannotGetParameterScale=No se puede obtener la escala del parámetro: {0}
odaconsumer.CannotGetParameterPrecision=No se puede obtener la precisión del parámetro: {0}
odaconsumer.CannotGetParameterIsNullable=No se puede determinar la capacidad para aceptar valores nulos del parámetro: {0}
odaconsumer.SameParamNameForDifferentHints=Hay otro parámetro en el conjunto de datos definido con el mismo nombre {0}.
odaconsumer.DifferentParamNameForSamePosition=Hay otro parámetro {0} en el conjunto de datos definido con la misma posición {1}.
odaconsumer.ParameterValueIsNull=El valor del parámetro es nulo.
odaconsumer.UnsupportedParameterValueType=Tipo de valor de parámetro no admitido: {0}
odaconsumer.UnknownExceptionThrown=Se ha producido una excepción desconocida en la última llamada del establecedor.
odaconsumer.CannotConvertIndexedParameterValue=No se puede convertir el valor del parámetro {0} en el índice {1} del tipo de valor de {2} al tipo de ODA de {3}.
odaconsumer.CannotConvertNamedParameterValue=No se puede convertir el valor del parámetro {0} con el nombre de parámetro de {1} del tipo de valor de {2} al tipo de ODA de {3}.
odaconsumer.UnrecognizedProjectedColumnName=Nombre de columna proyectada no reconocido: {0}.
odaconsumer.ColumnNameOrAliasAlreadyUsed=El nuevo nombre o alias de columna {0} ya se está utilizando en la columna del índice {1}
odaconsumer.NamedResultSetsUnsupported=Esta sentencia no admite grupos de resultados con nombre.
odaconsumer.OutputParameterUnsupported=Esta sentencia no admite parámetros de salida.
odaconsumer.CannotGetConnectionMetaData=No se pueden obtener los metadatos de conexión.
odaconsumer.CannotGetMaxConnections=No se puede obtener el número máximo de conexiones de la fuente de datos.
odaconsumer.CannotGetMaxStatements=No se puede obtener el número máximo de consultas para la fuente de datos.
odaconsumer.CannotGetDataSetMetaData=No se pueden obtener los metadatos del conjunto de datos {0} de la fuente de datos.
odaconsumer.CannotPrepareStatement=Error al preparar la siguiente consulta para el tipo de conjunto de datos {1}. \n[{0}]
odaconsumer.CannotCloseConnection=No se puede cerrar la conexión.
odaconsumer.CannotOpenConnection=No se puede abrir la conexión del controlador: {0}
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
odaconsumer.CannotGetMetaDataForNamedResultSet=No se pueden obtener los metadatos del grupo de resultados con nombre: {0}
odaconsumer.CannotExecuteStatement=No se puede ejecutar la sentencia.
odaconsumer.CannotGetResultSet=No se puede obtener el grupo de resultados.
odaconsumer.CannotGetNamedResultSet=No se puede obtener el grupo de resultados con nombre: {0}
odaconsumer.CannotFindOutParameter=No se puede encontrar el parámetro de salida: {0}
odaconsumer.CannotGetParameterType=No se puede obtener el tipo del parámetro: {0}
odaconsumer.CannotCloseStatement=No se puede cerrar la sentencia.
odaconsumer.CannotGetColumnCount=No se puede obtener el recuento de columnas.
odaconsumer.CannotGetColumnName=No se puede obtener el nombre de la columna: {0}
odaconsumer.CannotGetColumnLabel=No se puede obtener la etiqueta de la columna: {0}
odaconsumer.CannotGetColumnType=No se puede obtener el tipo de la columna: {0}
odaconsumer.CannotFetchNextRow=No se puede recuperar la fila de datos siguiente.
odaconsumer.CannotDetermineWasNull=No se puede determinar si el último valor recuperado era nulo.
odaconsumer.CannotGetIntFromColumn=No se puede obtener el valor entero de la columna: {0}
odaconsumer.CannotGetDoubleFromColumn=No se puede obtener el valor doble de la columna: {0}
odaconsumer.CannotGetStringFromColumn=No se puede obtener el valor de string de la columna: {0}
odaconsumer.CannotGetBigDecimalFromColumn=No se puede obtener el valor decimal de la columna: {0}
odaconsumer.CannotGetDateFromColumn=No se puede obtener el valor de fecha de la columna: {0}
odaconsumer.CannotGetTimeFromColumn=No se puede obtener el valor de hora de la columna: {0}
odaconsumer.CannotGetTimestampFromColumn=No se puede obtener el valor de fecha/hora de la columna: {0}
odaconsumer.CannotGetClobFromColumn=No se puede obtener el valor CLOB de la columna: {0}
odaconsumer.CannotGetBlobFromColumn=No se puede obtener el valor BLOB de la columna: {0}
odaconsumer.CannotGetRowPosition=No se puede obtener la posición de fila actual.
odaconsumer.CannotCloseResultSet=No se puede cerrar el grupo de resultados.
odaconsumer.CannotGetParameterCount=No se puede obtener el recuento de parámetros.
odaconsumer.CannotGetIntFromParameter=No se puede obtener el valor entero del parámetro: {0}
odaconsumer.CannotGetDoubleFromParameter=No se puede obtener el valor doble del parámetro: {0}
odaconsumer.CannotGetStringFromParameter=No se puede obtener el valor de string del parámetro: {0}
odaconsumer.CannotGetBigDecimalFromParameter=No se puede obtener el valor decimal del parámetro: {0}
odaconsumer.CannotGetDateFromParameter=No se puede obtener el valor de fecha del parámetro: {0}
odaconsumer.CannotGetTimeFromParameter=No se puede obtener el valor de hora del parámetro: {0}
odaconsumer.CannotGetTimestampFromParameter=No se puede obtener el valor de fecha/hora del parámetro: {0}
odaconsumer.CannotGetBlobFromParameter=No se puede obtener el valor BLOB del parámetro: {0}
odaconsumer.CannotGetClobFromParameter=No se puede obtener el valor CLOB del parámetro: {0}
odaconsumer.CannotFindInParameter=No se puede encontrar el parámetro de entrada: {0}
odaconsumer.CannotSetIntParameter=No se puede establecer el valor entero del parámetro: {0}
odaconsumer.CannotSetDoubleParameter=No se puede establecer el valor doble del parámetro: {0}
odaconsumer.CannotSetStringParameter=No se puede establecer el valor de string del parámetro: {0}
odaconsumer.CannotSetBigDecimalParameter=No se puede establecer el valor decimal del parámetro: {0}
odaconsumer.CannotSetDateParameter=No se puede establecer el valor de fecha del parámetro: {0}
odaconsumer.CannotSetTimeParameter=No se puede establecer el valor de hora del parámetro: {0}
odaconsumer.CannotSetTimestampParameter=No se puede establecer el valor de fecha/hora del parámetro: {0}
odaconsumer.CannotClearInParameters=No se pueden eliminar los valores de los parámetros de entrada.
odaconsumer.UnrecognizedOdaType=Tipo de datos ODA no reconocido: {0}
odaconsumer.CannotGetColumnNativeDataTypeName=No se puede obtener el nombre del tipo de datos nativo de la columna: {0}
odaconsumer.CannotGetParameterMetaData=No se pueden obtener los metadatos del parámetro del controlador ODA.
odaconsumer.CannotLoadOdaAdapter=No es posible cargar el controlador {0}; falta el complemento del adaptador DTP-to-BIRT ODA {1}.
odaconsumer.MustSpecifyColumnName=Se debe especificar un nombre de columna al añadir una sugerencia de columna.

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
odaconsumer.CannotFindLogDirectory=No se puede encontrar el directorio de registro.
