#************************************************************************
# Copyright (c) 2012 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#************************************************************************
#
# User messages for the Excel File ODA Designer

wizard.defaultMessage.selectExcelFile=Select or enter an Excel data source file path or URI.
wizard.defaultMessage.selectDataSet=Seleccione la hoja de trabajo y las columnas que desea incluir en el conjunto de datos.
connection_CANNOT_OPEN_EXCEL_FILE_DB_DIR=No es posible abrir el archivo de fuente de datos de Excel en la carpeta seleccionada. 
ui.ExcelFileNotFound=No es posible localizar el archivo o carpeta de fuente de datos de Excel o especificado 
label.selectFolder=S&eleccionar carpeta: 
label.selectFile=Seleccionar &archivo:
label.selectworksheet=Hoja de trabajo:
label.includeTypeLine=La segunda línea contiene tipos de &datos.
label.includeColumnNameLine=La primera líne&a contiene nombres de columnas.

button.selectFolder.browse=Examina&r...
button.moveUp=A&rriba
button.moveDown=A&bajo
button.delete=Elimi&nar
button.selectFileURI.menuItem.relativePath=&Ruta relativa
button.selectFileURI.menuItem.absolutePath=&Ruta absoluta

editor.title.name=Nombre
editor.title.originalName=Nombre original
editor.title.type=Tipo

datatypes.dateTime=Fecha Hora
datatypes.decimal=Decimal
datatypes.float=Flotante
datatypes.integer=Entero
datatypes.string=String
datatypes.date=Fecha
datatypes.time=Hora
datatypes.boolean=Booleano

tooltip.columnnameline=Asegúrese de que los nombres de columna aparecen\n en la primera fila del archivo seleccionado.
tooltip.typeline=Asegúrese de que el indicador de tipo de datos aparece\n en la segunda fila del archivo seleccionado.
tooltip.button.add=Añada todas las columnas seleccionadas de la tabla izquierda al conjunto de datos de archivo de Excel.
tooltip.button.up=Mover hacia arriba el elemento seleccionado.
tooltip.button.down=Mover hacia abajo el elemento seleccionado.
tooltip.button.delete=Eliminar el elemento seleccionado.
tooltip.trailNull=Coloque al final las columnas nulas al leer líneas de datos. 
error.selectFolder=Seleccione una carpeta válida.
error.selectWorksheet=Please select a worksheet for the data set.
error.selectColumns=Please select at least one column for this data set.
error.emptyPath=Introduzca una ruta.
error.unexpectedError=Se ha producido un error inesperado.
error.invalidConnectionFilePath=No existe la ruta especificada "{0}" para la fuente del perfil de conexión.
error.errorExist=Hay un error en la página actual.  ¿Desea guardarla de todas formas?
error.noFile=The Excel data source file ({0}) does not exist.
error.invalidFilePath=La ruta del archivo no es válida.  ¿Desea continuar?
error.emptyFilePath=La ruta del archivo está vacía.  ¿Desea continuar?
error.duplicatedNameValueOrEmpty=Valor de nombre duplicado o vacío
error.emptyFolder=No se ha encontrado ningún archivo de fuente de datos de Excel en la carpeta seleccionada.

title.warning=Advertencia
warning.fileExtensionInvalid=Es posible que el archivo seleccionado no sea un archivo de Excel válido.
confirm.reselectWorksheetTitle=Confirme para seleccionar una hoja diferente.
confirm.reselectWorksheetMessage=Si selecciona una hoja diferente no se guardarán las operaciones anteriores.\n\n¿Seguro que desea seleccionar una hoja diferente?
confirm.reselectFileTitle=Confirme para seleccionar un archivo diferente.
confirm.reselectFileMessage=Si selecciona un archivo diferente no se guardarán las operaciones anteriores.\n\n¿Seguro que desea seleccionar un archivo diferente?

RelativeFolderSelectionDialog.Title.SelectFolder=Seleccionar una carpeta
RelativeFileSelectionDialog.button.importFile=Importar archivo...
RelativeFileSelectionDialog.title.error=Error
RelativeFileSelectionDialog.title.overwrite=Sobrescribir
RelativeFileSelectionDialog.overwrite.msg=Ya existe un archivo con este nombre, ¿desea sobrescribirlo?
RelativeFileSelectionDialog.import.msg=Importando archivo...
