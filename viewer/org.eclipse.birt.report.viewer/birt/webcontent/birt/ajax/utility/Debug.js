/******************************************************************************
 *	Copyright (c) 2004 Actuate Corporation and others.
 *	All rights reserved. This program and the accompanying materials 
 *	are made available under the terms of the Eclipse Public License v1.0
 *	which accompanies this distribution, and is available at
 *		http://www.eclipse.org/legal/epl-v10.html
 *	
 *	Contributors:
 *		Actuate Corporation - Initial implementation.
 *****************************************************************************/

/*
 * If the debug window exists, write debug messages to it.
 * If isSoapMessage, write output to special soap message window
 */
function debug( text, isSoapMessage )
{
	//debug( birtSoapRequest.prettyPrintXML(request.responseXML.documentElement), true);
	if (window.top.debugWindow && !window.top.debugWindow.closed)
	{
		if(window.top.debugWindow.soapMsgWindow)
		{
			var debugDiv;
			if(isSoapMessage)
			{
			 	debugDiv = window.top.debugWindow.document.getElementById("soapMsgDebug");
			 	var div = window.top.debugWindow.document.createElement("div");
				div.innerHTML = "<pre>" + text;
				debugDiv.insertBefore(window.top.debugWindow.document.createTextNode("-------------START--------------"),debugDiv.firstChild);
				debugDiv.insertBefore(div,debugDiv.firstChild);
				debugDiv.insertBefore(window.top.debugWindow.document.createTextNode("-------------END----------------"),debugDiv.firstChild);
				debugDiv.insertBefore(window.top.debugWindow.document.createElement("br"),debugDiv.firstChild);
			 	
			 }
			 else
			 {
			 	debugDiv = window.top.debugWindow.document.getElementById("regularDebug").firstChild;
			 	var div = window.top.debugWindow.document.createElement("div");
				div.innerHTML = "<pre>" + text;				
				debugDiv.appendChild(div);				 	
			}
		}
			 
		else
		{
	    	window.top.debugWindow.document.write(text+"\n");	
	   	}
	}
}

/**
 * If the debug window exists, then close it
 */
function hideDebug()
{
	if (window.top.debugWindow && !window.top.debugWindow.closed)
	{
		window.top.debugWindow.close();
		window.top.debugWindow = null;
	}
}