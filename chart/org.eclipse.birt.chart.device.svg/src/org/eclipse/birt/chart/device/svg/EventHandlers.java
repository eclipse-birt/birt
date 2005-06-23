/***********************************************************************
 * Copyright (c) 2005 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import org.eclipse.birt.chart.device.svg.i18n.Messages;

/**
 * This class provides javascript helper functions to enable user interactions such
 * as tooltip support.  Defines default styles for svg elements.
 */
public final class EventHandlers {

	public static StringBuffer styles = new StringBuffer()
		.append(".tooltip.text{ text-anchor:left;font-size:12pt;fill:black;}.tooltip{fill:rgb(244,245,235)}"); //$NON-NLS-1$
	
	public static StringBuffer content = new StringBuffer()
.append("function BuildHelper(tag, attrList, text) {\n") //$NON-NLS-1$
.append("	this.tag = tag;\n") //$NON-NLS-1$
.append("	this.attrList = attrList;\n") //$NON-NLS-1$
.append("	this.text       = text;\n") //$NON-NLS-1$
.append("	this.element  = null;\n") //$NON-NLS-1$
.append("	this.textNode = null;\n") //$NON-NLS-1$
.append("	}	\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	BuildHelper.prototype.addToParent= function(parent) {	\n") //$NON-NLS-1$
.append("	if (!parent) return;	\n") //$NON-NLS-1$
.append("	var svgDocument = parent.ownerDocument;\n") //$NON-NLS-1$
.append("	this.element = svgDocument.createElementNS(\"http://www.w3.org/2000/svg\",this.tag);\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	for (var attribute in this.attrList){\n") //$NON-NLS-1$
.append("	if (attribute == 'classType')	\n") //$NON-NLS-1$
.append("		     this.element.setAttributeNS(null, 'class', this.attrList[attribute]);\n") //$NON-NLS-1$
.append("	else	\n") //$NON-NLS-1$
.append("		     this.element.setAttributeNS(null, attribute, this.attrList[attribute]);\n") //$NON-NLS-1$
.append("	}	\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	if (this.text) {\n") //$NON-NLS-1$
.append("		this.textNode = svgDocument.createTextNode(this.text);\n") //$NON-NLS-1$
.append("		this.element.appendChild(this.textNode);\n") //$NON-NLS-1$
.append("	}\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	parent.appendChild(this.element);\n") //$NON-NLS-1$
.append("	};	\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("BuildHelper.prototype.insertBefore = function(parent, before) {\n") //$NON-NLS-1$
.append("	if (typeof parent == 'undefined') return;	\n") //$NON-NLS-1$
.append("	var svgDocument = parent.ownerDocument;\n") //$NON-NLS-1$
.append("	this.element = svgDocument.createElementNS(\"http://www.w3.org/2000/svg\",this.tag);\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	for (var attribute in this.attrList){\n") //$NON-NLS-1$
.append("	if (attribute == 'classType')	\n") //$NON-NLS-1$
.append("		     this.element.setAttributeNS(null, 'class', this.attrList[attribute]);\n") //$NON-NLS-1$
.append("	else	\n") //$NON-NLS-1$
.append("		     this.element.setAttributeNS(null, attribute, this.attrList[attribute]);\n") //$NON-NLS-1$
.append("	}	\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	if (this.text) {\n") //$NON-NLS-1$
.append("		this.textNode = svgDocument.createTextNode(this.text);\n") //$NON-NLS-1$
.append("		this.element.appendChild(textNode);\n") //$NON-NLS-1$
.append("	}\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	parent.insertBefore(this.element, before);\n") //$NON-NLS-1$
.append("	};	\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("		\n") //$NON-NLS-1$
.append("	BuildHelper.prototype.removeNode = function() {	\n") //$NON-NLS-1$
.append("	if (this.element) this.element.parentNode.removeChild(this.element);\n") //$NON-NLS-1$
.append("	this.tag = \"\";\n") //$NON-NLS-1$
.append("	this.attrList = null;\n") //$NON-NLS-1$
.append("	this.text = null;\n") //$NON-NLS-1$
.append("	this.element = null;\n") //$NON-NLS-1$
.append("	this.textNode = null;\n") //$NON-NLS-1$
.append("	};	\n") //$NON-NLS-1$
	.append("	function TM(){	\n") //$NON-NLS-1$
	.append("	}	\n") //$NON-NLS-1$
	.append("		\n") //$NON-NLS-1$
	.append("TM.setParent = function TooltipManager_setParent(parent, mainSvg){\n") //$NON-NLS-1$
	.append("	this.parent = parent;	\n") //$NON-NLS-1$
	.append("	this.mainSvg = mainSvg	\n") //$NON-NLS-1$
	.append("	}	\n") //$NON-NLS-1$
	.append("		\n") //$NON-NLS-1$
	.append("	TM.remove = function TooltipManager_removeTooltip(){	\n") //$NON-NLS-1$
	.append("	if (typeof this.group != 'undefined'){	\n") //$NON-NLS-1$
	.append("	  this.group.removeNode();\n") //$NON-NLS-1$
	.append("	  this.group = undefined;\n") //$NON-NLS-1$
	.append("	}	\n") //$NON-NLS-1$
	.append("	}	\n") //$NON-NLS-1$
	.append("		\n") //$NON-NLS-1$
	.append("	TM.show = function TooltiplManager_showTooltip(evt){	\n") //$NON-NLS-1$
	.append("	var text = TM.getText(TM.getTitleElement(evt));	\n") //$NON-NLS-1$
	.append("	x = evt.clientX;	\n") //$NON-NLS-1$
	.append("	y = evt.clientY;	\n") //$NON-NLS-1$
	.append("	if (typeof this.group == 'undefined'){	\n") //$NON-NLS-1$
	.append("	this.height = 15;	\n") //$NON-NLS-1$
	.append("	this.xPadding = 5;	\n") //$NON-NLS-1$
	.append("	   this.yPadding = -20;\n") //$NON-NLS-1$
	.append("	   var mainSvg = evt.target.ownerDocument.documentElement;\n") //$NON-NLS-1$
	.append("	   var off = mainSvg.currentTranslate;\n") //$NON-NLS-1$
	.append("	   var scl = mainSvg.currentScale;\n") //$NON-NLS-1$
	.append("	   this.group = new BuildHelper(\"g\",\n") //$NON-NLS-1$
	.append("	     {\n") //$NON-NLS-1$
	.append("	     transform:\"translate(\"+(((x+20))/scl)+\",\"+(((y+20))/scl)+\")\"\n") //$NON-NLS-1$
	.append("	 } );\n") //$NON-NLS-1$
	.append("	   this.group.addToParent(mainSvg);\n") //$NON-NLS-1$
	.append("	   this.rectangle = new BuildHelper(\"rect\",\n") //$NON-NLS-1$
	.append("	     {id:\"test\",\n") //$NON-NLS-1$
	.append("	      x: 0,\n") //$NON-NLS-1$
	.append("	      y: this.yPadding,\n") //$NON-NLS-1$
	.append("	      height: this.height,\n") //$NON-NLS-1$
	.append("	      classType: \"tooltip\"\n") //$NON-NLS-1$
	.append("	 } );\n") //$NON-NLS-1$
	.append("	   this.rectangle.addToParent(this.group.element);\n") //$NON-NLS-1$
	.append("	   var textObj = new BuildHelper(\"text\",\n") //$NON-NLS-1$
	.append("	     {id:\"tooltip\",\n") //$NON-NLS-1$
	.append("	      x: this.xPadding,\n") //$NON-NLS-1$
	.append("	      y: (this.height/2+4+this.yPadding),\n") //$NON-NLS-1$
	.append("	      classType: \"tooltip text\"\n") //$NON-NLS-1$
	.append("	 },text);\n") //$NON-NLS-1$
	.append("	   textObj.addToParent(this.group.element);\n") //$NON-NLS-1$
	.append("	   var itemlength = textObj.element.getComputedTextLength();\n") //$NON-NLS-1$
	.append("	   this.rectangle.element.setAttributeNS(null, \"width\", (itemlength+2*this.xPadding));\n") //$NON-NLS-1$
	.append("	  }\n") //$NON-NLS-1$
	.append("	}\n") //$NON-NLS-1$
	.append("		\n") //$NON-NLS-1$
	.append("		\n") //$NON-NLS-1$
	.append("	TM.getTitleElement = function TM_getTitleElement(evt){\n") //$NON-NLS-1$
	.append("	    var elem = evt.currentTarget;\n") //$NON-NLS-1$
	.append("	if (elem == null ) return;	\n") //$NON-NLS-1$
	.append("	    var childs = elem.childNodes;\n") //$NON-NLS-1$
	.append("	    for (var x=0; x<childs.length; x++){\n") //$NON-NLS-1$
	.append("		if (childs.item(x).nodeType == 1 && childs.item(x).nodeName == \"title\")\n") //$NON-NLS-1$
	.append("		    return childs.item(x);\n") //$NON-NLS-1$
	.append("	    }\n") //$NON-NLS-1$
	.append("	    return null;\n") //$NON-NLS-1$
	.append("	}\n") //$NON-NLS-1$
	.append("		\n") //$NON-NLS-1$
	.append("	TM.getText = function TM_getText(elem){\n") //$NON-NLS-1$
	.append("	   var childs = elem ? elem.childNodes : null;\n") //$NON-NLS-1$
	.append("	   for (var x = 0;  childs && x < childs.length; x++)\n") //$NON-NLS-1$
	.append("	      if (childs.item(x).nodeType == 3)\n") //$NON-NLS-1$
	.append("		  return childs.item(x).nodeValue;\n") //$NON-NLS-1$
	.append("	   return \"\";\n") //$NON-NLS-1$
	.append("	}\n") //$NON-NLS-1$
	.append("		\n"); //$NON-NLS-1$
}
