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
 *	Birt error dialog.
 */
BirtExceptionDialog = Class.create( );

BirtExceptionDialog.prototype = Object.extend( new AbstractExceptionDialog( ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id, "600px" );
		this.__z_index = 300;
	},
	
	/**
	*	Handle clicking on ok.
	* 
	* 	@return, void
	*/
	__okPress: function( )
	{
		this.__l_hide( );
	},
	
	/**
	Called right before element is shown
	*/
	__preShow: function()
	{
		// disable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", true );
		
		// disable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", true );
	},
	
	/**
	Called before element is hidden
	*/
	__preHide: function()
	{
		// enable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", false );
		
		// enable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", false );		
	}	
} );