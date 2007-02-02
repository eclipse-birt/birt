###############################################################################
# Copyright (c) 2004 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation  - initial API and implementation
###############################################################################

birt.viewer.title=BIRT Report Viewer
birt.viewer.title.navigation=Navegación
birt.viewer.title.error=Error

birt.viewer.parameter=Parámetro
birt.viewer.runreport=Ejecutar informe
birt.viewer.required=Los parámetros marcados con <FONT COLOR="red">*</FONT> son obligatorios.

birt.viewer.viewinpdf=Ver en PDF
birt.viewer.maximize=Ocultar parámetros de informe
birt.viewer.restore=Mostrar parámetros de informe

birt.viewer.error=Mensaje de error
birt.viewer.error.noparameter=No hay ningún parámetro para este informe.

###############################################################################
# Toolbar
###############################################################################
birt.viewer.toolbar.print=Imprimir informe
birt.viewer.toolbar.toc=Mostrar tabla de contenido
birt.viewer.toolbar.parameter=Ejecutar informe
birt.viewer.toolbar.export=Exportar datos
birt.viewer.toolbar.font=Cambiar fuente
birt.viewer.toolbar.enableiv=Llamar a Interactive Viewer
birt.viewer.toolbar.pdf=Imprimir informe como PDF

###############################################################################
# navbar
###############################################################################
birt.viewer.navbar.first=Primera página
birt.viewer.navbar.previous=Página anterior
birt.viewer.navbar.next=Siguiente página
birt.viewer.navbar.last=Última página
birt.viewer.navbar.goto=Ir a
birt.viewer.navbar.lable.goto=Ir a la página:
birt.viewer.navbar.prompt.one=Mostrando página
birt.viewer.navbar.prompt.two=de
birt.viewer.navbar.error.blankpagenum=Introduzca un número de página válido.
###############################################################################
# progress bar
###############################################################################
birt.viewer.progressbar.prompt=Procesando, espere...

###############################################################################
# Dialog 
###############################################################################
birt.viewer.dialog.ok=Aceptar
birt.viewer.dialog.cancel=Cancelar
birt.viewer.dialog.run=Ejecutar informe
birt.viewer.dialog.close=Cerrar

###############################################################################
# Dialog title
###############################################################################
birt.viewer.dialog.exception.title=Excepción
birt.viewer.dialog.exportReport.title=Exportar informe
birt.viewer.dialog.parameter.title=Parámetro
birt.viewer.dialog.simpleExportData.title=Exportar datos

###############################################################################
# Export report dialog 
###############################################################################
birt.viewer.dialog.export.format=Formato de exportación:
birt.viewer.dialog.export.format.pdf=PDF
birt.viewer.dialog.export.format.excel=EXCEL
birt.viewer.dialog.export.all=Exportar informe detallado
birt.viewer.dialog.export.all.detail=Se exportarán todos los datos disponibles del informe independientemente de<br>las modificaciones de vista actuales.
birt.viewer.dialog.export.modified=Exportar informe con modificaciones actuales
birt.viewer.dialog.export.modified.detail=Sólo se exportarán los datos visibles. No se exportarán los datos que se<BR>filtraron u ocultaron.

###############################################################################
# Export data dialog 
###############################################################################
birt.viewer.dialog.exportdata.tab.field=Campos de datos
birt.viewer.dialog.exportdata.tab.filter=Criterios de filtro
birt.viewer.dialog.exportdata.resultsets=Grupos de resultados disponibles
birt.viewer.dialog.exportdata.availablecolumn=Columnas disponibles
birt.viewer.dialog.exportdata.selectedcolumn=Columnas seleccionadas
birt.viewer.dialog.exportdata.format=Los datos se exportarán en formato csv.
birt.viewer.dialog.exportdata.encoding=Codificación de salida:
birt.viewer.dialog.exportdata.encoding.other=Otro
birt.viewer.dialog.exportdata.encoding.comment=(Si está en blanco, utilice la codificación local)
birt.viewer.dialog.exportdata.addall=Añadir todos
birt.viewer.dialog.exportdata.add=Añadir
birt.viewer.dialog.exportdata.removeall=Quitar todos
birt.viewer.dialog.exportdata.remove=Quitar

###############################################################################
# General exception
###############################################################################
birt.viewer.generalException.DOCUMENT_FILE_ERROR=El archivo de documento: {0} no existe o contiene errores.
birt.viewer.generalException.DOCUMENT_ACCESS_ERROR=No se puede acceder al archivo de documento: {0}.
birt.viewer.generalException.REPORT_FILE_ERROR=El archivo de informe: {0} no existe o contiene errores.
birt.viewer.generalException.REPORT_ACCESS_ERROR=No se puede acceder al archivo de informe: {0}.
birt.viewer.generalException.DOCUMENT_FILE_PROCESSING=Procesando archivo de documento, inténtelo más tarde.

###############################################################################
# Report service exception
###############################################################################
birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT=No hay ningún archivo de documento disponible para extraer los datos.
birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET=No hay ningún conjunto de resultados disponible para extraer los datos.
birt.viewer.reportServiceException.INVALID_TOC=Consulta de TCD no válida.
birt.viewer.reportServiceException.INVALID_PARAMETER=Parámetro no válido: {0}, ya que no está definido.
birt.viewer.reportServiceException.STARTUP_REPORTENGINE_ERROR=Fatal error occurred when try to startup report engine.

###############################################################################
# Birt action handler exception
###############################################################################
birt.viewer.actionException.NO_REPORT_DOCUMENT=La instancia de documento de informe está vacía.
birt.viewer.actionException.INVALID_BOOKMARK=El marcador: {0} no es válido. Compruebe y restablezca el link.
birt.viewer.actionException.INVALID_PAGE_NUMBER=Número de página no válido. 
birt.viewer.actionException.INVALID_ID_FORMAT=Formato de ID no válido. Id={0}.
birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST=La sesión ha alcanzado el límite de tiempo o no hay ningún archivo de documento disponible.

###############################################################################
# Birt soap bingding impl exception
###############################################################################
birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET=No se puede encontrar ningún controlador para este destino. Destino: {0}.

###############################################################################
# Component processor exception
###############################################################################
birt.viewer.componentProcessorException.MISSING_OPERATOR=Error: Falta operador.

###############################################################################
# Exception dialog
###############################################################################
birt.viewer.exceptionDialog.stackTrace=Apilar rastreo:
birt.viewer.exceptionDialog.showStackTrace=Show Exception Stack Trace
birt.viewer.exceptionDialog.hideStackTrace=Hide Exception Stack Trace

###############################################################################
# Birt Viewer JSP Taglib
###############################################################################
birt.viewer.taglib.NO_VIEWER_ID=Se debe especificar el ID de atributo.
birt.viewer.taglib.INVALID_VIEWER_ID=El ID de atributo contiene caracteres no válidos.
birt.viewer.taglib.VIEWER_ID_DUPLICATE=El ID de atributo debe ser exclusivo.
birt.viewer.taglib.NO_REPORT_SOURCE=Se debe especificar el diseño de informe o archivo de documento.
birt.viewer.taglib.NO_REPORT_DOCUMENT=El informe necesita el archivo de documento de informe.
birt.viewer.taglib.NO_REQUESTER_NAME=Se debe especificar un nombre de solicitante
