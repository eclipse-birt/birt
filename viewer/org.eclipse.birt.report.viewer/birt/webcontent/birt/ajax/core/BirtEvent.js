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
 
/**
 *	BirtEvent
 */
BirtEvent = Class.create( );

BirtEvent.prototype =
{
	__E_WARN : '__E_WARN',
	__E_BLUR : '__E_BLUR', // Blur current selection.
	__E_GETPAGE : '__E_GETPAGE', // Getting designated page.
	__E_PRINT : '__E_PRINT', // Print event.
	__E_QUERY_EXPORT : '__E_QUERY_EXPORT',
	__E_TOC : '__E_TOC',
	__E_TOC_IMAGE_CLICK : '__E_TOC_IMAGE_CLICK',
	__E_PARAMETER : '__E_PARAMETER',
	__E_CHANGE_PARAMETER : '__E_CHANGE_PARAMETER',  //Change parameter event.
	__E_CACHE_PARAMETER : '__E_CACHE_PARAMETER',  //Cache parameter event.
	__E_CASCADING_PARAMETER : '__E_CASCADING_PARAMETER',  //Cascading parameter event.
	__E_PDF : '__E_PDF', // Create pdf event.
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	Define available birt events.
	 *
	 *	@return, void
	 */
	initialize: function( )
	{
	}
}

var birtEvent = new BirtEvent( );