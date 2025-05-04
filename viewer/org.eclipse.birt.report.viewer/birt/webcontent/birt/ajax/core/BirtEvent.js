/******************************************************************************
 *	Copyright (c) 2004, 2025 Actuate Corporation and others.
 *	All rights reserved. This program and the accompanying materials 
 *	are made available under the terms of the Eclipse Public License v2.0
 *	which accompanies this distribution, and is available at
 *		http://www.eclipse.org/legal/epl-2.0.html
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
	__E_GETPAGE : '__E_GETPAGE', // Getting pagination page.
	__E_GETPAGE_INIT : '__E_GETPAGE_INIT', // Getting pagination page with parameters.
	__E_PRINT : '__E_PRINT', // Print event.
	__E_PRINT_SERVER : '__E_PRINT_SERVER', // Print event.
	__E_QUERY_EXPORT : '__E_QUERY_EXPORT',
	__E_TOC : '__E_TOC',
	__E_TOC_IMAGE_CLICK : '__E_TOC_IMAGE_CLICK',
	__E_PARAMETER : '__E_PARAMETER',
	__E_CHANGE_PARAMETER : '__E_CHANGE_PARAMETER',  //Change parameter event.
	__E_CACHE_PARAMETER : '__E_CACHE_PARAMETER',  //Cache parameter event.
	__E_CASCADING_PARAMETER : '__E_CASCADING_PARAMETER',  //Cascading parameter event.
	__E_PDF : '__E_PDF', // Create pdf event.
	__E_CANCEL_TASK : '__E_CANCEL_TASK', // Cancel current task event.
	__E_GETPAGE_ALL : '__E_GETPAGE_ALL', // Get all pages.
	__E_EXPORT_REPORT : '__E_EXPORT_REPORT', // Export report
	__E_PREVIEW_LAYOUT : '__E_PREVIEW_LAYOUT', // Preview layout
 	
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