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

/**
 * This class provides javascript helper functions to enable user interactions
 * such as tooltip support. Defines default styles for svg elements.
 */
public final class EventHandlers
{

	public static StringBuffer styles = new StringBuffer( ).append( ".tooltip.text{ text-anchor:left;font-size:12pt;fill:black;}.tooltip{fill:rgb(244,245,235)}" ); //$NON-NLS-1$

	public static StringBuffer content = new StringBuffer( ).append( "function BuildHelper(tag, attrList, text) {\n" ) //$NON-NLS-1$
			.append( "	this.tag = tag;\n" ) //$NON-NLS-1$
			.append( "	this.attrList = attrList;\n" ) //$NON-NLS-1$
			.append( "	this.text       = text;\n" ) //$NON-NLS-1$
			.append( "	this.element  = null;\n" ) //$NON-NLS-1$
			.append( "	this.textNode = null;\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	BuildHelper.prototype.addToParent= function(parent) {	\n" ) //$NON-NLS-1$
			.append( "	if (!parent) return;	\n" ) //$NON-NLS-1$
			.append( "	var svgDocument = parent.ownerDocument;\n" ) //$NON-NLS-1$
			.append( "	this.element = svgDocument.createElementNS(\"http://www.w3.org/2000/svg\",this.tag);\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	for (var attribute in this.attrList){\n" ) //$NON-NLS-1$
			.append( "	if (attribute == 'classType')	\n" ) //$NON-NLS-1$
			.append( "		     this.element.setAttributeNS(null, 'class', this.attrList[attribute]);\n" ) //$NON-NLS-1$
			.append( "	else	\n" ) //$NON-NLS-1$
			.append( "		     this.element.setAttributeNS(null, attribute, this.attrList[attribute]);\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	if (this.text) {\n" ) //$NON-NLS-1$
			.append( "		this.textNode = svgDocument.createTextNode(this.text);\n" ) //$NON-NLS-1$
			.append( "		this.element.appendChild(this.textNode);\n" ) //$NON-NLS-1$
			.append( "	}\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	parent.appendChild(this.element);\n" ) //$NON-NLS-1$
			.append( "	};	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "BuildHelper.prototype.insertBefore = function(parent, before) {\n" ) //$NON-NLS-1$
			.append( "	if (typeof parent == 'undefined') return;	\n" ) //$NON-NLS-1$
			.append( "	var svgDocument = parent.ownerDocument;\n" ) //$NON-NLS-1$
			.append( "	this.element = svgDocument.createElementNS(\"http://www.w3.org/2000/svg\",this.tag);\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	for (var attribute in this.attrList){\n" ) //$NON-NLS-1$
			.append( "	if (attribute == 'classType')	\n" ) //$NON-NLS-1$
			.append( "		     this.element.setAttributeNS(null, 'class', this.attrList[attribute]);\n" ) //$NON-NLS-1$
			.append( "	else	\n" ) //$NON-NLS-1$
			.append( "		     this.element.setAttributeNS(null, attribute, this.attrList[attribute]);\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	if (this.text) {\n" ) //$NON-NLS-1$
			.append( "		this.textNode = svgDocument.createTextNode(this.text);\n" ) //$NON-NLS-1$
			.append( "		this.element.appendChild(textNode);\n" ) //$NON-NLS-1$
			.append( "	}\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	parent.insertBefore(this.element, before);\n" ) //$NON-NLS-1$
			.append( "	};	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	BuildHelper.prototype.removeNode = function() {	\n" ) //$NON-NLS-1$
			.append( "	if (this.element) this.element.parentNode.removeChild(this.element);\n" ) //$NON-NLS-1$
			.append( "	this.tag = \"\";\n" ) //$NON-NLS-1$
			.append( "	this.attrList = null;\n" ) //$NON-NLS-1$
			.append( "	this.text = null;\n" ) //$NON-NLS-1$
			.append( "	this.element = null;\n" ) //$NON-NLS-1$
			.append( "	this.textNode = null;\n" ) //$NON-NLS-1$
			.append( "	};	\n" ) //$NON-NLS-1$
			.append( "	function TM(){	\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "TM.setParent = function TooltipManager_setParent(parent, mainSvg){\n" ) //$NON-NLS-1$
			.append( "	this.parent = parent;	\n" ) //$NON-NLS-1$
			.append( "	this.mainSvg = mainSvg	\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	TM.remove = function TooltipManager_removeTooltip(){	\n" ) //$NON-NLS-1$
			.append( "	if (typeof this.group != 'undefined'){	\n" ) //$NON-NLS-1$
			.append( "	  this.group.removeNode();\n" ) //$NON-NLS-1$
			.append( "	  this.group = undefined;\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	TM.show = function TooltiplManager_showTooltip(evt){	\n" ) //$NON-NLS-1$
			.append( "	var text = TM.getText(TM.getTitleElement(evt));	\n" ) //$NON-NLS-1$
			.append( "	x = evt.clientX;	\n" ) //$NON-NLS-1$
			.append( "	y = evt.clientY;	\n" ) //$NON-NLS-1$
			.append( "	if (typeof this.group == 'undefined'){	\n" ) //$NON-NLS-1$
			.append( "	this.height = 15;	\n" ) //$NON-NLS-1$
			.append( "	this.xPadding = 5;	\n" ) //$NON-NLS-1$
			.append( "	   this.yPadding = -20;\n" ) //$NON-NLS-1$
			.append( "	   var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$
			.append( "	   var off = mainSvg.currentTranslate;\n" ) //$NON-NLS-1$
			.append( "	   var scl = mainSvg.currentScale;\n" ) //$NON-NLS-1$
			.append( "	   this.group = new BuildHelper(\"g\",\n" ) //$NON-NLS-1$
			.append( "	     {\n" ) //$NON-NLS-1$
			.append( "	     transform:\"translate(\"+(((x+20))/scl)+\",\"+(((y+20))/scl)+\")\"\n" ) //$NON-NLS-1$
			.append( "	 } );\n" ) //$NON-NLS-1$
			.append( "	   this.group.addToParent(mainSvg);\n" ) //$NON-NLS-1$
			.append( "	   this.rectangle = new BuildHelper(\"rect\",\n" ) //$NON-NLS-1$
			.append( "	     {id:\"test\",\n" ) //$NON-NLS-1$
			.append( "	      x: 0,\n" ) //$NON-NLS-1$
			.append( "	      y: this.yPadding,\n" ) //$NON-NLS-1$
			.append( "	      height: this.height,\n" ) //$NON-NLS-1$
			.append( "	      classType: \"tooltip\"\n" ) //$NON-NLS-1$
			.append( "	 } );\n" ) //$NON-NLS-1$
			.append( "	   this.rectangle.addToParent(this.group.element);\n" ) //$NON-NLS-1$
			.append( "	   var textObj = new BuildHelper(\"text\",\n" ) //$NON-NLS-1$
			.append( "	     {id:\"tooltip\",\n" ) //$NON-NLS-1$
			.append( "	      x: this.xPadding,\n" ) //$NON-NLS-1$
			.append( "	      y: (this.height/2+4+this.yPadding),\n" ) //$NON-NLS-1$
			.append( "	      classType: \"tooltip text\"\n" ) //$NON-NLS-1$
			.append( "	 },text);\n" ) //$NON-NLS-1$
			.append( "	   textObj.addToParent(this.group.element);\n" ) //$NON-NLS-1$
			.append( "	   var itemlength = textObj.element.getComputedTextLength();\n" ) //$NON-NLS-1$
			.append( "	   this.rectangle.element.setAttributeNS(null, \"width\", (itemlength+2*this.xPadding));\n" ) //$NON-NLS-1$
			.append( "	  }\n" ) //$NON-NLS-1$
			.append( "	}\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	TM.getTitleElement = function TM_getTitleElement(evt){\n" ) //$NON-NLS-1$
			.append( "	    var elem = evt.currentTarget;\n" ) //$NON-NLS-1$
			.append( "	if (elem == null ) return;	\n" ) //$NON-NLS-1$
			.append( "	    var childs = elem.childNodes;\n" ) //$NON-NLS-1$
			.append( "	    for (var x=0; x<childs.length; x++){\n" ) //$NON-NLS-1$
			.append( "		if (childs.item(x).nodeType == 1 && childs.item(x).nodeName == \"title\")\n" ) //$NON-NLS-1$
			.append( "		    return childs.item(x);\n" ) //$NON-NLS-1$
			.append( "	    }\n" ) //$NON-NLS-1$
			.append( "	    return null;\n" ) //$NON-NLS-1$
			.append( "	}\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "	TM.getText = function TM_getText(elem){\n" ) //$NON-NLS-1$
			.append( "	   var childs = elem ? elem.childNodes : null;\n" ) //$NON-NLS-1$
			.append( "	   for (var x = 0;  childs && x < childs.length; x++)\n" ) //$NON-NLS-1$
			.append( "	      if (childs.item(x).nodeType == 3)\n" ) //$NON-NLS-1$
			.append( "		  return childs.item(x).nodeValue;\n" ) //$NON-NLS-1$
			.append( "	   return \"\";\n" ) //$NON-NLS-1$
			.append( "	}\n" ) //$NON-NLS-1$
			.append( "	        function toggleVisibility(evt, id, compList){\n" ) //$NON-NLS-1$
		.append( "       var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$
		.append( "    for (i = 0; i < compList.length; i=i+1){\n" ) //$NON-NLS-1$
			.append( "         var comp = mainSvg.getElementById(id+'_'+compList[i]);\n" ) //$NON-NLS-1$
			.append( "         var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
			.append( "        rVisibleExp=/visibility:visible/g;\n" ) //$NON-NLS-1$
			.append( "        rHiddenExp=/visibility:hidden/g;\n" ) //$NON-NLS-1$
			.append( "        results = styleStr.search(rVisibleExp);\n" ) //$NON-NLS-1$
			.append( "        if (results == -1){\n" ) //$NON-NLS-1$
 			.append( "           results = styleStr.search(rHiddenExp);\n" ) //$NON-NLS-1$
			.append( "            if (results == -1)\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr + \"visibility:hidden;\";\n" ) //$NON-NLS-1$
    			.append( "            else\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr.replace(rHiddenExp,\"visibility:visible\");\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         else{\n" ) //$NON-NLS-1$
 			.append( "                styleStr = styleStr.replace(rVisibleExp,\"visibility:hidden\");\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
			.append( "     }\n" ) //$NON-NLS-1$
		.append( "     }			\n" ) //$NON-NLS-1$
		.append( "   		function toHex(val)\n" ) //$NON-NLS-1$
	.append( "	{\n" ) //$NON-NLS-1$
	.append( "	    strVal = Number(val).toString(16);\n" ) //$NON-NLS-1$
	.append( "	    while (strVal.length < 6){\n" ) //$NON-NLS-1$
	.append( "	        strVal = \"0\"+strVal;\n" ) //$NON-NLS-1$
	.append( "	    }\n" ) //$NON-NLS-1$
	.append( "	    return strVal;\n" ) //$NON-NLS-1$
	.append( "	}\n" ) //$NON-NLS-1$
	.append( "	function getXorColor(color){\n" ) //$NON-NLS-1$
	.append( "	    var value = parseInt(color, 16);\n" ) //$NON-NLS-1$
	.append( "	    value = 0xFFFFFF ^ value;\n" ) //$NON-NLS-1$
	.append( "	    return \"#\"+toHex(value);\n" ) //$NON-NLS-1$
	.append( "	}		\n" ) //$NON-NLS-1$
	.append( "	var oldCompId = null;\n" ) //$NON-NLS-1$
	.append( "	var oldCompList = null;\n" ) //$NON-NLS-1$
 	.append( "   function highlight(evt, id, compList){\n" ) //$NON-NLS-1$
		.append( "       highlightElement(evt, oldCompId, oldCompList);\n" ) //$NON-NLS-1$
    	.append( "       if (id != oldCompId){\n" ) //$NON-NLS-1$
        	.append( "           highlightElement(evt, id, compList);\n" ) //$NON-NLS-1$
       	.append( "           oldCompId = id;\n" ) //$NON-NLS-1$
       	.append( "          oldCompList = compList;\n" ) //$NON-NLS-1$
       	.append( "        }\n" ) //$NON-NLS-1$
    	.append( "        else{\n" ) //$NON-NLS-1$
        	.append( "           oldCompId = null;\n" ) //$NON-NLS-1$
       	.append( "           oldCompList = null;\n" ) //$NON-NLS-1$
       	.append( "        }\n" ) //$NON-NLS-1$
    	.append( "     }\n" ) //$NON-NLS-1$
    	.append( "    	function highlightElement(evt, id, compList){\n" ) //$NON-NLS-1$
    	.append( "		   if ((id == null) || (compList == null)) return;\n" ) //$NON-NLS-1$
    	.append( "	       var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$

    	.append( "	    for (i = 0; i < compList.length; i=i+1){\n" ) //$NON-NLS-1$
    	.append( "	    var comp = mainSvg.getElementById(id+'_'+compList[i]);\n" ) //$NON-NLS-1$
    	.append( "	    var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
    	.append( "		   fillIndex = styleStr.search(\"fill:\");\n" ) //$NON-NLS-1$
    	.append( "		   if (fillIndex != -1){\n" ) //$NON-NLS-1$
    	.append( "	              styleStr = getNewStyle(styleStr, fillIndex, \"fill:\");\n" ) //$NON-NLS-1$
    	.append( "		   }\n" ) //$NON-NLS-1$
    	.append( "		   strokeIndex = styleStr.search(\"stroke:\");\n" ) //$NON-NLS-1$
    	.append( "		   if (strokeIndex != -1){\n" ) //$NON-NLS-1$
    	.append( "	              styleStr = getNewStyle(styleStr, strokeIndex, \"stroke:\");\n" ) //$NON-NLS-1$
    	.append( "		   }\n" ) //$NON-NLS-1$
    	.append( "	   comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
    	.append( "	     }\n" ) //$NON-NLS-1$
    	.append( "	     }\n" ) //$NON-NLS-1$				
    	.append( "	function getNewStyle(style, index, styleAttr){\n" ) //$NON-NLS-1$
    	.append( "	     color = style.substring(index+styleAttr.length, style.length );\n" ) //$NON-NLS-1$
    	.append( "	     rgbIndex = color.search(\"rgb\");\n" ) //$NON-NLS-1$
    	.append( "	     if (rgbIndex == -1){\n" ) //$NON-NLS-1$
    	.append( "	        hexColor = color.substring(1, 7);\n" ) //$NON-NLS-1$
    	.append( "	        hc = getXorColor(hexColor);\n" ) //$NON-NLS-1$
    	.append( "	        return style.replace(\"fill:#\"+hexColor,styleAttr+hc);\n" ) //$NON-NLS-1$
    	.append( "	     }\n" ) //$NON-NLS-1$
    	.append( "	     else{\n" ) //$NON-NLS-1$
	     .append( "	        bracketIndex = color.search(\"\\\\)\");\n" ) //$NON-NLS-1$
    	.append( "	        color = color.substring(0, bracketIndex);\n" ) //$NON-NLS-1$
    	.append( "	        hexColor = getHexFromRGB(color);\n" ) //$NON-NLS-1$
    	.append( "	        hc = getXorColor(hexColor);\n" ) //$NON-NLS-1$
    	.append( "	        return style.substring(0, index) + styleAttr+hc+ style.substring(index+bracketIndex+styleAttr.length+1, style.length);\n" ) //$NON-NLS-1$
    	.append( "	   }    \n" ) //$NON-NLS-1$
    	.append( "	}\n" ) //$NON-NLS-1$
    	.append( "	function getHexFromRGB(color){\n" ) //$NON-NLS-1$
    	.append( "	        findThem = /\\d{1,3}/g;\n" ) //$NON-NLS-1$
    	.append( "	        listOfnum = color.match(findThem);\n" ) //$NON-NLS-1$
    	.append( "	        r = Number(listOfnum[0]).toString(16);\n" ) //$NON-NLS-1$
    	.append( "	        while (r.length < 2){\n" ) //$NON-NLS-1$
    	.append( "		    r = \"0\"+r;\n" ) //$NON-NLS-1$
    	.append( "	        }\n" ) //$NON-NLS-1$
    	.append( "	        g = Number(listOfnum[1]).toString(16);\n" ) //$NON-NLS-1$
    	.append( "	        while (g.length < 2){\n" ) //$NON-NLS-1$
    	.append( "		   g = \"0\"+g;\n" ) //$NON-NLS-1$
    	.append( "	        }\n" ) //$NON-NLS-1$
    	.append( "	        b = Number(listOfnum[2]).toString(16);\n" ) //$NON-NLS-1$
    	.append( "	        while (b.length < 2){\n" ) //$NON-NLS-1$
    	.append( "	 	   b = \"0\"+b;\n" ) //$NON-NLS-1$
    	.append( "	        }\n" ) //$NON-NLS-1$
    	.append( "		return r+g+b;\n" ) //$NON-NLS-1$
    	.append( "	}\n" ); //$NON-NLS-1$
}
