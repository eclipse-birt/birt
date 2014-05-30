#/*******************************************************************************
# * Copyright (c) 2008 Actuate Corporation.
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# *  Actuate Corporation  - initial API and implementation
# *******************************************************************************/

###########################################################
#HTML Emitter
HTMLEmitter.Description=Configure las opciones para presentar un informe en formato HTML (HyperText Markup Language).

HTMLEmitter.DisplayName=HTML
XHTMLEmitter.Description=Configure las opciones para presentar un informe en formato XHTML.

XHTMLEmitter.DisplayName=XHTML

###########################################################
#Option Display Value
OptionDisplayValue.AgentStyleEngine=Delegar procesamiento de estilo al agente del usuario

OptionDisplayValue.Enable=Activar

OptionDisplayValue.Disable=Desactivar

OptionDisplayValue.OutputMasterPageMargins=Márgenes de salida de la página maestra

OptionDisplayValue.MasterPageContent=Encabezado y pie de la página de salida

OptionDisplayValue.PageFooterFloat=Pie de página flotante

OptionDisplayValue.LayoutPreference=Preferencia de diseño:

OptionDisplayValue.FixedLayout=Diseño fijo

OptionDisplayValue.AutoLayout=Diseño automático

OptionDisplayValue.HTMLType=Tipo de salida:

OptionDisplayValue.CompleteHTML=HTML completo

OptionDisplayValue.HTMLNoCSS=HTML sin CSS

###########################################################
#Option Description
OptionDescription.AgentStyleEngine=True: significa que el emisor de HTML utilizará los estilos de BIRT directamente en el informe y dependerá del explorador para implementar el cálculo de estilos.\nFalse: significa que el emisor HTML utilizará el motor de estilos de BIRT para calcular los estilos y presentar el resultado en el informe.

OptionDescription.OutputMasterPageMargins=Utilice los márgenes de la página maestra para el informe de diseño automático.

OptionDescription.MasterPageContent=Contiene el encabezado y pie de página, sólo tiene efecto si la paginación está activada.

OptionDescription.PageFooterFloat=Pie de página flotante, sólo tiene efecto si la paginación está activada.

OptionDescription.HTMLType=Tipo de HTML.
