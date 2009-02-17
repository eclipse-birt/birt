/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
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

	public static final StringBuffer STYLES = new StringBuffer( ).append( ".tooltip.text{ text-anchor:start;font-size:12pt;fill:black;}.tooltip{fill:rgb(244,245,235)}" ); //$NON-NLS-1$

	public static final StringBuffer CONTENT = new StringBuffer( ).append( "function BuildHelper(tag, attrList, text) {\n" ) //$NON-NLS-1$
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
			.append( "	TM.toggleToolTip = function TooltipManager_toggleToolTip(evt){	\n" ) //$NON-NLS-1$
			.append( "	if (typeof this.group != 'undefined'){	\n" ) //$NON-NLS-1$
			.append( "	  TM.remove();\n" ) //$NON-NLS-1$
			.append( "	}else{	\n" ) //$NON-NLS-1$
			.append( "	  TM.show(evt);\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "	TM.remove = function TooltipManager_removeTooltip(){	\n" ) //$NON-NLS-1$
			.append( "	if (typeof this.group != 'undefined'){	\n" ) //$NON-NLS-1$
			.append( "	  this.group.removeNode();\n" ) //$NON-NLS-1$
			.append( "	  this.group = undefined;\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "	}	\n" ) //$NON-NLS-1$
			.append( "		\n" ) //$NON-NLS-1$
			.append( "			TM.show = function TooltiplManager_showTooltip(evt,id){	\n" ) //$NON-NLS-1$
			.append( "        if (typeof id != 'undefined'){	\n" ) //$NON-NLS-1$
			.append( "     	       var mainSvg = evt.target.ownerDocument;	\n" ) //$NON-NLS-1$
			.append( "               var comp = mainSvg.getElementById(id);	\n" ) //$NON-NLS-1$
			.append( "               var styleStr = comp.getAttribute(\"style\");	\n" ) //$NON-NLS-1$
			.append( "               rHiddenExp=/visibility:[ ]*hidden/g;	\n" ) //$NON-NLS-1$
			.append( "               results = styleStr.search(rHiddenExp);	\n" ) //$NON-NLS-1$
			.append( "               if (results != -1)	\n" ) //$NON-NLS-1$
			.append( "     		       return;	\n" ) //$NON-NLS-1$
			.append( "     }	\n" ) //$NON-NLS-1$
			.append( "		var text = TM.getText(TM.getTitleElement(evt));	\n" ) //$NON-NLS-1$
			.append( "		x = evt.clientX;	\n" ) //$NON-NLS-1$
			.append( "		y = evt.clientY;	\n" ) //$NON-NLS-1$
			.append( "		update = true;\n" ) //$NON-NLS-1$
			.append( "	    if (this.oldX != 'undefined'){\n" ) //$NON-NLS-1$
			.append( "	      diffX = (x - this.oldX);\n" ) //$NON-NLS-1$
			.append( "	      if (diffX < 0) diffX= diffX*(-1);\n" ) //$NON-NLS-1$
			.append( "	      diffY = (y - this.oldY);\n" ) //$NON-NLS-1$
			.append( "	      if (diffY < 0) diffY= diffY*(-1);\n" ) //$NON-NLS-1$
			.append( "	      if ((diffY > 5) || (diffX > 5))\n" ) //$NON-NLS-1$
			.append( "	        update = true;\n" ) //$NON-NLS-1$
			.append( "	    }\n" ) //$NON-NLS-1$
			.append( "	    if (update)\n" ) //$NON-NLS-1$
			.append( "	       TM.remove();			\n" ) //$NON-NLS-1$
			.append( "		if (typeof this.group == 'undefined'){	\n" ) //$NON-NLS-1$
			.append( "	           this.oldX = x;\n" ) //$NON-NLS-1$
			.append( "	           this.oldY = y;		\n" ) //$NON-NLS-1$
			.append( "	   	   this.height = 15;	\n" ) //$NON-NLS-1$
			.append( "	 	   this.xPadding = 5;	\n" ) //$NON-NLS-1$
			.append( "		   this.yPadding = 20;\n" ) //$NON-NLS-1$
			.append( "		   var mainSvg = evt.target.ownerDocument.documentElement;\n" ) //$NON-NLS-1$
			.append( "		   var off = mainSvg.currentTranslate;\n" ) //$NON-NLS-1$
			.append( "		   var scl = mainSvg.currentScale;\n" ) //$NON-NLS-1$
			.append( "	           var adjustedX = (x-off.x)/scl;\n" ) //$NON-NLS-1$
			.append( "	           var adjustedY = (y-off.y)/scl;\n" ) //$NON-NLS-1$
			.append( "		   this.group = new BuildHelper(\"g\",\n" ) //$NON-NLS-1$
			.append( "		     {\n" ) //$NON-NLS-1$
			.append( "	             opacity:0.8,\n" ) //$NON-NLS-1$
			.append( "	  	     display: \"inline\",\n" ) //$NON-NLS-1$
			.append( "		     transform:\"translate(\"+(adjustedX + (10/scl))+\",\"+(adjustedY + (10/scl))+\")\"\n" ) //$NON-NLS-1$
			.append( "		 } );\n" ) //$NON-NLS-1$
			.append( "		   this.group.addToParent(mainSvg);\n" ) //$NON-NLS-1$
			.append( "		   this.rectangle = new BuildHelper(\"rect\",\n" ) //$NON-NLS-1$
			.append( "		     {id:\"test\",\n" ) //$NON-NLS-1$
			.append( "		      x: 0,\n" ) //$NON-NLS-1$
			.append( "		      y: 5,\n" ) //$NON-NLS-1$
			.append( "		      transform:\"scale(\"+(1/scl)+\",\"+(1/scl)+\")\",\n" ) //$NON-NLS-1$
			.append( "	              rx: 2,\n" ) //$NON-NLS-1$
			.append( "	              ry: 2,\n" ) //$NON-NLS-1$
			.append( "	              stroke: \"black\",\n" ) //$NON-NLS-1$
			.append( "		      height: this.height,\n" ) //$NON-NLS-1$
			.append( "		      classType: \"tooltip\"\n" ) //$NON-NLS-1$
			.append( "		 } );\n" ) //$NON-NLS-1$
			.append( "		   this.rectangle.addToParent(this.group.element);\n" ) //$NON-NLS-1$
			.append( "		   var textObj = new BuildHelper(\"text\",\n" ) //$NON-NLS-1$
			.append( "		     {id:\"tooltip\",\n" ) //$NON-NLS-1$
			.append( "		      x: this.xPadding,\n" ) //$NON-NLS-1$
			.append( "		      y: (this.yPadding),\n" ) //$NON-NLS-1$
			.append( "		      transform:\"scale(\"+(1/scl)+\",\"+(1/scl)+\")\",\n" ) //$NON-NLS-1$
			.append( "		      classType: \"tooltip text\"\n" ) //$NON-NLS-1$
			.append( "		 });\n" ) //$NON-NLS-1$
			.append( "		   textObj.addToParent(this.group.element);\n" ) //$NON-NLS-1$
			.append( "	           TM.setContent(textObj, text);\n" ) //$NON-NLS-1$
			.append( "	           var outline = textObj.element.getBBox();\n" ) //$NON-NLS-1$
			.append( "                   var tooltipHeight = outline.height+6;\n" ) //$NON-NLS-1$
			.append( "                   var tooltipWidth = outline.width+2*this.xPadding;\n" ) //$NON-NLS-1$
			.append( "                   var root=evt.target.ownerDocument.documentElement;\n" ) //$NON-NLS-1$
			.append( "                   var rootWidth =root.getAttribute('width');\n" ) //$NON-NLS-1$
			.append( "                   var rootHeight = root.getAttribute('height');\n" ) //$NON-NLS-1$
			.append( "                   if (((y+tooltipHeight)> rootHeight) || ((x+tooltipWidth)> rootWidth)){\n" ) //$NON-NLS-1$
			.append( "                      var transformX = x + this.xPadding;\n" ) //$NON-NLS-1$
			.append( "                      var transformY = y+ this.yPadding;\n" ) //$NON-NLS-1$
			.append( "                      if ((y+tooltipHeight)> rootHeight)\n" ) //$NON-NLS-1$
			.append( "                        transformY  = (rootHeight-tooltipHeight)-this.yPadding;\n" ) //$NON-NLS-1$
			.append( "                   if ((x+tooltipWidth)> rootWidth)\n" ) //$NON-NLS-1$
			.append( "                        transformX  = (rootWidth-tooltipWidth)-this.xPadding;\n" ) //$NON-NLS-1$
			.append( "                      this.group.element.setAttributeNS(null, \"transform\", \"translate(\"+(transformX*xScale)+\", \"+(transformY*yScale)+\")\");\n" ) //$NON-NLS-1$
			.append( "                   }\n" ) //$NON-NLS-1$
			.append( "                   this.rectangle.element.setAttributeNS(null, \"width\", tooltipWidth);\n" ) //$NON-NLS-1$
			.append( "                   this.rectangle.element.setAttributeNS(null, \"height\", tooltipHeight);\n" ) //$NON-NLS-1$
			.append( "		  }\n" ) //$NON-NLS-1$
			.append( "		}\n" ) //$NON-NLS-1$
			.append( "		TM.setContent = function TooltipManager_setContent(textElement, text){\n" ) //$NON-NLS-1$
			.append( "		    text = text.replace(/\\n/g, \"\\\\n\");\n" ) //$NON-NLS-1$
			.append( "		    var multiLine = text.split(/\\\\n/);\n" ) //$NON-NLS-1$
			.append( "		    for (var x=0; x<multiLine.length; x++){\n" ) //$NON-NLS-1$
			.append( "			if (x == 0){\n" ) //$NON-NLS-1$
			.append( "	 	         textObj = new BuildHelper(\"tspan\",\n" ) //$NON-NLS-1$
			.append( "	   	             {x: 5\n" ) //$NON-NLS-1$
			.append( "		             },multiLine[x]);\n" ) //$NON-NLS-1$
			.append( "	                 }\n" ) //$NON-NLS-1$
			.append( "	                 else{\n" ) //$NON-NLS-1$
			.append( "	 	         textObj = new BuildHelper(\"tspan\",\n" ) //$NON-NLS-1$
			.append( "	   	             {x: 5,\n" ) //$NON-NLS-1$
			.append( "	                      dy:17\n" ) //$NON-NLS-1$
			.append( "		             },multiLine[x]);\n" ) //$NON-NLS-1$
			.append( "	                 }\n" ) //$NON-NLS-1$
			.append( "		      textObj.addToParent(textElement.element);\n" ) //$NON-NLS-1$
			.append( "	            }\n" ) //$NON-NLS-1$
			.append( "	        }\n" ) //$NON-NLS-1$
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
			.append( "	        function toggleLabelsVisibility(evt, id, compList, labelList){\n" ) //$NON-NLS-1$
		.append( "       var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$
		.append( "    for (i = 0; i < compList.length; i=i+1){\n" ) //$NON-NLS-1$
			.append( "         var comp = mainSvg.getElementById(id+'_'+compList[i]);\n" ) //$NON-NLS-1$
			.append( "         if ( comp == null ) continue;\n")		//$NON-NLS-1$
			.append( "         var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
			.append( "        rVisibleExp=/visibility:[ ]*visible/g;\n" ) //$NON-NLS-1$
			.append( "        rInheritExp=/visibility:[ ]*inherit/g;\n" ) //$NON-NLS-1$
			.append( "        rHiddenExp=/visibility:[ ]*hidden/g;\n" ) //$NON-NLS-1$
			.append( "        results = styleStr.search(rVisibleExp);\n" ) //$NON-NLS-1$
			.append( "        inResults = styleStr.search(rInheritExp);\n" ) //$NON-NLS-1$
			.append( "        if ((results == -1) && (inResults == -1)){\n" ) //$NON-NLS-1$
 			.append( "           results = styleStr.search(rHiddenExp);\n" ) //$NON-NLS-1$
			.append( "            if (results == -1)\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr + \"visibility:hidden;\";\n" ) //$NON-NLS-1$
    			.append( "            else\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr.replace(rHiddenExp,\"visibility:visible\");\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         else{\n" ) //$NON-NLS-1$
			.append( "         if (inResults == -1){\n" ) //$NON-NLS-1$
 			.append( "                styleStr = styleStr.replace(rVisibleExp,\"visibility:hidden\");\n" ) //$NON-NLS-1$
			.append( "         }else{\n" ) //$NON-NLS-1$
 			.append( "                styleStr = styleStr.replace(rInheritExp,\"visibility:hidden\");\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
			.append( "     }\n" ) //$NON-NLS-1$
			.append( "	}\n" ) //$NON-NLS-1$
			.append( "	        function toggleVisibility(evt, id, compList, labelList){\n" ) //$NON-NLS-1$
		.append( "       var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$
		.append( "       var isHidden = true;\n" ) //$NON-NLS-1$
		.append( "    for (i = 0; i < compList.length; i=i+1){\n" ) //$NON-NLS-1$
			.append( "         var comp = mainSvg.getElementById(id+'_'+compList[i]);\n" ) //$NON-NLS-1$
			.append( "         if ( comp == null ) continue;\n")		//$NON-NLS-1$
			.append( "         var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
			.append( "        rVisibleExp=/visibility:[ ]*visible/g;\n" ) //$NON-NLS-1$
			.append( "        rHiddenExp=/visibility:[ ]*hidden/g;\n" ) //$NON-NLS-1$
			.append( "        results = styleStr.search(rVisibleExp);\n" ) //$NON-NLS-1$
			.append( "        if (results == -1){\n" ) //$NON-NLS-1$
 			.append( "           results = styleStr.search(rHiddenExp);\n" ) //$NON-NLS-1$
			.append( "            if (results == -1)\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr + \"visibility:hidden;\";\n" ) //$NON-NLS-1$
    			.append( "            else{\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr.replace(rHiddenExp,\"visibility:visible\");\n" ) //$NON-NLS-1$
			.append( "                isHidden = false;\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         else{\n" ) //$NON-NLS-1$
 			.append( "                styleStr = styleStr.replace(rVisibleExp,\"visibility:hidden\");\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
			.append( "     }\n" ) //$NON-NLS-1$
			.append( "        if (labelList != null){\n" ) //$NON-NLS-1$
		.append( "    for (i = 0; i < labelList.length; i=i+1){\n" ) //$NON-NLS-1$
			.append( "         var comp = mainSvg.getElementById(id+'_'+labelList[i]+'_g');\n" ) //$NON-NLS-1$
			.append( "         if ( comp == null ) continue;\n")		//$NON-NLS-1$			
			.append( "         var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
			.append( "        if (isHidden){\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr + \"visibility:hidden;\";\n" ) //$NON-NLS-1$
			.append( "         toggleLabelVisibility(evt, id+'_'+labelList[i], 'inherit');\n" ) //$NON-NLS-1$
    			.append( "            }else{\n" ) //$NON-NLS-1$
			.append( "                styleStr = styleStr.replace(rHiddenExp,\"visibility:visible\");\n" ) //$NON-NLS-1$
    			.append( "         }\n" ) //$NON-NLS-1$
			.append( "         comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
			.append( "     }\n" ) //$NON-NLS-1$
			.append( "     }\n" ) //$NON-NLS-1$
		.append( "     }			\n" ) //$NON-NLS-1$
		.append( "        function toggleLabelVisibility(evt, id, property){\n" ) //$NON-NLS-1$
		.append( "	      var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$
		.append( "	        var comp = mainSvg.getElementById(id);\n" ) //$NON-NLS-1$
		.append( "	        var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
		.append( "	       rVisibleExp=/visibility:[ ]*visible/g;\n" ) //$NON-NLS-1$
		.append( "	       rInheritExp=/visibility:[ ]*inherit/g;\n" ) //$NON-NLS-1$
		.append( "	       rHiddenExp=/visibility:[ ]*hidden/g;\n" ) //$NON-NLS-1$
		.append( "	       results = styleStr.search(rVisibleExp);\n" ) //$NON-NLS-1$
		.append( "	       inResults = styleStr.search(rInheritExp);\n" ) //$NON-NLS-1$
		.append( "	       if ((results == -1) && (inResults == -1)){\n" ) //$NON-NLS-1$
		.append( "	          results = styleStr.search(rHiddenExp);\n" ) //$NON-NLS-1$
		.append( "	           if (results == -1)\n" ) //$NON-NLS-1$
		.append( "	               styleStr = styleStr + \"visibility:\"+property+\";\";\n" ) //$NON-NLS-1$
		.append( "	        }\n" ) //$NON-NLS-1$
		.append( "	        else{\n" ) //$NON-NLS-1$
		.append( "	            if (inResults == -1)\n" ) //$NON-NLS-1$
		.append( "	               styleStr = styleStr.replace(rVisibleExp,\"visibility:\"+property);\n" ) //$NON-NLS-1$		
		.append( "        else\n" ) //$NON-NLS-1$
		.append( "            styleStr = styleStr.replace(rInheritExp,\"visibility:\"+property);\n" ) //$NON-NLS-1$
		.append( "     }\n" ) //$NON-NLS-1$
		.append( "     comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
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
    .append( "    var fillToColor = new Array(); \n" ) //$NON-NLS-1$
    .append( "    var strokeToColor = new Array();	\n" ) //$NON-NLS-1$
 	.append( "   function highlight(evt, id, compList){\n" ) //$NON-NLS-1$
		.append( "       highlightElement(evt, oldCompId, oldCompList, false);\n" ) //$NON-NLS-1$
    	.append( "       if (id != oldCompId){\n" ) //$NON-NLS-1$
        	.append( "           highlightElement(evt, id, compList, true);\n" ) //$NON-NLS-1$
       	.append( "           oldCompId = id;\n" ) //$NON-NLS-1$
       	.append( "          oldCompList = compList;\n" ) //$NON-NLS-1$
       	.append( "        }\n" ) //$NON-NLS-1$
    	.append( "        else{\n" ) //$NON-NLS-1$
        	.append( "           oldCompId = null;\n" ) //$NON-NLS-1$
       	.append( "           oldCompList = null;\n" ) //$NON-NLS-1$
       	.append( "       	fillToColor = new Array();\n" ) //$NON-NLS-1$
		.append( "      strokeToColor = new Array();\n" ) //$NON-NLS-1$
       	.append( "        }\n" ) //$NON-NLS-1$
    	.append( "     }\n" ) //$NON-NLS-1$
    	.append( "    	function highlightElement(evt, id, compList, highlight){\n" ) //$NON-NLS-1$
    	.append( "		   if ((id == null) || (compList == null)) return;\n" ) //$NON-NLS-1$
    	.append( "	       var mainSvg = evt.target.ownerDocument;\n" ) //$NON-NLS-1$

    	.append( "	    for (i = 0; i < compList.length; i=i+1){\n" ) //$NON-NLS-1$
    	.append( "	    var comp = mainSvg.getElementById(id+'_'+compList[i]);\n" ) //$NON-NLS-1$
    	.append( "         if ( comp == null ) continue;\n")		//$NON-NLS-1$
    	.append( "	    var styleStr = comp.getAttribute(\"style\");\n" ) //$NON-NLS-1$
    	.append( "		   fillIndex = styleStr.search(\"fill:\");\n" ) //$NON-NLS-1$
    	.append( "		   if (fillIndex != -1){\n" ) //$NON-NLS-1$
    	.append( "	              styleStr = getNewStyle(styleStr, fillIndex, \"fill:\", highlight, fillToColor, compList[i]);\n" ) //$NON-NLS-1$
    	.append( "		   }\n" ) //$NON-NLS-1$
    	.append( "		   strokeIndex = styleStr.search(\"stroke:\");\n" ) //$NON-NLS-1$
    	.append( "		   if (strokeIndex != -1){\n" ) //$NON-NLS-1$
    	.append( "	              styleStr = getNewStyle(styleStr, strokeIndex, \"stroke:\", highlight, strokeToColor, compList[i]);\n" ) //$NON-NLS-1$
    	.append( "		   }\n" ) //$NON-NLS-1$
    	.append( "	   comp.setAttributeNS(null, \"style\", styleStr);\n" ) //$NON-NLS-1$
    	.append( "	     }\n" ) //$NON-NLS-1$
    	.append( "	     }\n" ) //$NON-NLS-1$			    	
    	
    	.append( "    	function getNewStyle(style, index, styleAttr, highlight, lookUpTable, id){\n" ) //$NON-NLS-1$
    	.append( "	     color = style.substring(index+styleAttr.length, style.length );\n" ) //$NON-NLS-1$
   	
    	.append( "             if (color.substring(0, 6).search(\"none\")  != -1) return style;\n" ) //$NON-NLS-1$
    	.append( "              rgbIndex = color.search(\"rgb\");\n" ) //$NON-NLS-1$
    	.append( "              if (rgbIndex == -1){\n" ) //$NON-NLS-1$
    	.append( "          if (styleAttr == \"fill:\")\n" ) //$NON-NLS-1$
    	.append( "             urlStr = /fill:\\s*url\\(#([^\\x27]+)\\);/g;\n" ) //$NON-NLS-1$
    	.append( "         else\n" ) //$NON-NLS-1$
    	.append( "             urlStr = /stroke:\\s*url\\(#([^\\x27]+)\\);/g;\n" ) //$NON-NLS-1$
    	
    	.append( "        result = urlStr.exec(style);\n" ) //$NON-NLS-1$
    	.append( "        if (result != null){\n" ) //$NON-NLS-1$
    	.append( "    		endOf= /\\w+h\\b/g;\n" ) //$NON-NLS-1$
    	.append( "    		if (endOf.exec(result[1])== null){\n" ) //$NON-NLS-1$    	
    	.append( "             return style.replace(urlStr, styleAttr+\"url(#\"+result[1]+\"h);\");\n") //$NON-NLS-1$
    	.append( "          }\n" ) //$NON-NLS-1$
    	.append( "          else{\n" ) //$NON-NLS-1$
    	.append( "             return style.replace(urlStr, styleAttr+\"url(#\"+result[1].substring(0, result[1].length-1)+\");\");\n") //$NON-NLS-1$
    	.append( "          }\n" ) //$NON-NLS-1$    	
    	.append( "        }\n" ) //$NON-NLS-1$
    	.append( "               else{\n" ) //$NON-NLS-1$
    	.append( "	        hexColor = color.substring(1, 7);\n" ) //$NON-NLS-1$
    	.append( "	        hc = getHighlight(hexColor, highlight, lookUpTable, id);\n" ) //$NON-NLS-1$
    	.append( "	        return style.replace(styleAttr+\"#\"+hexColor,styleAttr+hc);\n" ) //$NON-NLS-1$
    	.append( "               }\n" ) //$NON-NLS-1$
    	.append( "	     }\n" ) //$NON-NLS-1$
    	.append( "	     else{\n" ) //$NON-NLS-1$
    	.append( "	        bracketIndex = color.search(\"\\\\)\");\n" ) //$NON-NLS-1$
    	.append( "	        color = color.substring(0, bracketIndex);\n" ) //$NON-NLS-1$
    	.append( "	        hexColor = getHexFromRGB(color);\n" ) //$NON-NLS-1$
    	.append( "	        hc = getHighlight(hexColor, highlight, lookUpTable, id);\n" ) //$NON-NLS-1$
    	.append( "	        return style.substring(0, index) + styleAttr+hc+ style.substring(index+bracketIndex+styleAttr.length+1, style.length);\n" ) //$NON-NLS-1$
    	.append( "	   }    \n" ) //$NON-NLS-1$
    	.append( "	}\n" ) //$NON-NLS-1$
		//////////////////////////////////////////////////////////////////
		//    	 function: redirect
		//    	 description: Redirects url to a certain target instance
		//    	 inputs:
		//    	   target - target instance
    	//         url - url
		////////////////////////////////////////////////////////////////    	
    	.append( "	function redirect(target, url){\n" ) //$NON-NLS-1$
    	.append( "		if (target =='_blank'){\n" ) //$NON-NLS-1$    	
    	.append( "		try{\n" ) //$NON-NLS-1$
    	.append( "			open(url);\n" ) //$NON-NLS-1$
    	.append( "		}catch(e){}\n" ) //$NON-NLS-1$
    	.append( "		}\n" ) //$NON-NLS-1$
    	.append( "		else if (target == '_top'){\n" ) //$NON-NLS-1$
    	.append( "          window.top.location.href=url;\n" ) //$NON-NLS-1$									
    	.append( "		}\n" ) //$NON-NLS-1$
    	.append( "		else if (target == '_parent'){\n" ) //$NON-NLS-1$
    	.append( "          parent.location.href=url;\n" ) //$NON-NLS-1$									
    	.append( "		}\n" ) //$NON-NLS-1$
    	.append( "		else if (target == '_self'){\n" ) //$NON-NLS-1$
    	.append( "          parent.location.href=url;\n" ) //$NON-NLS-1$									
    	.append( "		}\n" ) //$NON-NLS-1$    	
    	.append( "		else if (target == '_self'){\n" ) //$NON-NLS-1$
    	.append( "          parent.location.href=url;\n" ) //$NON-NLS-1$									
    	.append( "		}\n" ) //$NON-NLS-1$    	
    	.append( "		else{\n" ) //$NON-NLS-1$
    	.append( "		try{\n" ) //$NON-NLS-1$
    	.append( "			open(url);\n" ) //$NON-NLS-1$
    	.append( "		}catch(e){}\n" ) //$NON-NLS-1$
    	.append( "		}\n" ) //$NON-NLS-1$
    	.append( "	}\n" ) //$NON-NLS-1$    	    
		//////////////////////////////////////////////////////////////////
		//    	 function: scaleSVG
		//    	 description: scales the svg document by a specified zoom factor
		//    	 inputs:
		//    	   factor - zoom factor 
		////////////////////////////////////////////////////////////////
//    	.append( "function scaleSVG(factor){\n" ) //$NON-NLS-1$
//    	.append( "  root=document.rootElement;\n" ) //$NON-NLS-1$
//    	.append( "  root.currentScale = factor\n" ) //$NON-NLS-1$
//    	.append( "}    	\n" ) //$NON-NLS-1$
 		//////////////////////////////////////////////////////////////////
		//    	 function: isIE
		//    	 description: detects whether the browser is a Mircosoft internet explorer browser
		//    	 outputs:
		//    	   true/false 
		////////////////////////////////////////////////////////////////   	
    	.append( "	function isIE(){\n" )//$NON-NLS-1$
		.append( "   var agt=parent.navigator.userAgent.toLowerCase();\n" )//$NON-NLS-1$
		.append( "   return (agt.indexOf(\"msie\")!=-1);\n" )//$NON-NLS-1$
		.append( "}  \n" )//$NON-NLS-1$  	
 		//////////////////////////////////////////////////////////////////
		//    	 function: resizeSVG
		//    	 description: changes the size of the SVG to the width and height
		//                    of the containing embed tag.  Currently this 
		//                    support is only for IE browsers.
		//    	 input: 
		//            e - the event that triggers the resize
		////////////////////////////////////////////////////////////////
		.append( "       var xScale = 1;\n" )//$NON-NLS-1$
		.append( "       var yScale = 1;\n" )//$NON-NLS-1$
    	.append( "	function resizeSVG(e){\n" )//$NON-NLS-1$
    	.append( "    try{\n" )//$NON-NLS-1$
		.append( "       var root=e.target.ownerDocument.documentElement;\n" )//$NON-NLS-1$
		.append( "       var hotSpot = e.target.ownerDocument.getElementById('hotSpots');\n" )//$NON-NLS-1$
		.append( "       var g = e.target.ownerDocument.getElementById('outerG');\n" )//$NON-NLS-1$
		.append( "       xScale = (innerWidth) / root.getAttribute('initialWidth');\n" )//$NON-NLS-1$
		.append( "       yScale = (innerHeight) / root.getAttribute('initialHeight');\n" )//$NON-NLS-1$
		.append( "       root.setAttribute('width', xScale*root.getAttribute('initialWidth'));\n" )//$NON-NLS-1$
		.append( "       root.setAttribute('height', yScale*root.getAttribute('initialHeight'));\n" )//$NON-NLS-1$
		.append( "       g.setAttributeNS(null, 'transform', 'scale('+xScale+','+yScale+')');\n" )//$NON-NLS-1$
		.append( "       hotSpot.setAttributeNS(null, 'transform', 'scale('+xScale+','+yScale+')');\n" )//$NON-NLS-1$
		.append( "    }catch(e){}\n" )//$NON-NLS-1$
		.append( "  }\n" )//$NON-NLS-1$		
		.append( "        function getHighlight(color, highlight, lookupTable, id){\n" )//$NON-NLS-1$
    	.append( "        if (!(highlight)){\n" )//$NON-NLS-1$
    	.append( "            color = lookupTable[id];\n" )//$NON-NLS-1$
    	.append( "        }\n" )//$NON-NLS-1$
    	.append( "        else{\n" )//$NON-NLS-1$
    	.append( "            lookupTable[id] = color;\n" )//$NON-NLS-1$
    	.append( "        }\n" )//$NON-NLS-1$
    	.append( "        var r = color.substring(0, 2);\n" )//$NON-NLS-1$
    	.append( "        r = parseInt(r, 16);\n" )//$NON-NLS-1$
    	.append( "        var g = color.substring(2, 4);\n" )//$NON-NLS-1$
    	.append( "        g = parseInt(g, 16);\n" )//$NON-NLS-1$
    	.append( "        var b = color.substring(4, 6);\n" )//$NON-NLS-1$
    	.append( "        b = parseInt(b, 16);\n" )//$NON-NLS-1$
    	.append( "    var value = parseInt(r, 16);\n" )//$NON-NLS-1$
    	.append( "        if (highlight){\n" )//$NON-NLS-1$
    	.append( "           r = Math.ceil( (r + 255) / 2 );\n" )//$NON-NLS-1$
    	.append( "           g = Math.ceil( (g + 255) / 2 );\n" )//$NON-NLS-1$
    	.append( "           b = Math.ceil( (b + 255) / 2 );\n" )//$NON-NLS-1$
    	.append( "        }\n" )//$NON-NLS-1$
    	.append( "        rStr = r.toString(16);\n" )//$NON-NLS-1$		
    	.append( "        gStr = g.toString(16);\n" )//$NON-NLS-1$
    	.append( "    bStr = b.toString(16);\n" )//$NON-NLS-1$
    	.append( "    while (rStr.length < 2){\n" )//$NON-NLS-1$
    	.append( "        rStr = \"0\"+rStr;\n" )//$NON-NLS-1$
    	.append( "    }\n" )//$NON-NLS-1$
    	.append( "    while (gStr.length < 2){\n" )//$NON-NLS-1$
    	.append( "        gStr = \"0\"+gStr;\n" )//$NON-NLS-1$
    	.append( "    }\n" )//$NON-NLS-1$
    	.append( "    while (bStr.length < 2){\n" )//$NON-NLS-1$
    	.append( "        bStr = \"0\"+bStr;\n" )//$NON-NLS-1$
    	.append( "    }\n" )//$NON-NLS-1$
    	.append( "return \"#\"+rStr+gStr+bStr;\n" )//$NON-NLS-1$
    	.append( "}\n" )//$NON-NLS-1$    	    	
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
