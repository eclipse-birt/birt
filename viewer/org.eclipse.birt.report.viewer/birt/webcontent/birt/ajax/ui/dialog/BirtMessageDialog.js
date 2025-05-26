/*******************************************************************************
 * Copyright (c) 2025 Thomas Gutmann.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial API and implementation
 *******************************************************************************/
/**
 *	Birt message dialog.
 */
BirtMessageDialog = Class.create( );

BirtMessageDialog.prototype = Object.extend( new AbstractBaseDialog(),
{
	/**
	 * indicate whether exception detail is show or not.
	 */
	__isShow: false,
	
	/**
	 * control id definitions
	 */
	__TRACE_DIALOG: 'messageDialog',		
	
	/**
	 * Event handler closures.
	 */
	__neh_click_input_closurre : null,
	__neh_click_icon_closure : null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( id )
	{
		var dialogWidth = 480;
		this.__initBase( id, dialogWidth + "px" );
		this.__allowSelection = true; // allow selecting text with the mouse
		this.__z_index = 300;
		
		this.__neh_click_input_closure = this.__neh_click_input.bindAsEventListener( this );
		this.__neh_key_input_closure = this.__neh_key_input.bindAsEventListener( this );

		// default dialog: remove cancel button
		var oCancelButton = $("messageDialogcancelButton");
		if (oCancelButton) {
			oCancelButton.style.display = "none";
		}

		// default dialog: shrink the button container
		var oButtonContainer = $("messageDialogdialogCustomButtonContainer");
		if (oButtonContainer) {
			oButtonContainer.style.width = "80px";
		}
	},	

	showMessage : function(messageText)
	{
		var objMessageArea = $("message-content");
		if (objMessageArea && messageText !== null) {
			objMessageArea.innerText = messageText;
		}
		this.showDialog();
	},
	
	__neh_key_input: function( event )
	{
		if ( event.keyCode == 13 || event.keyCode == 32 )
		{
			this.__neh_click_input();
		}
	},
	
	/**
	*	Handle clicking on input control.
	* 
	* 	@return, void
	*/
	__neh_click_input: function( event )
	{
		var that = this;
		// BirtPosition.center(this.__TRACE_DIALOG);
		this.__isShow = !this.__isShow;
		
		// refresh the dialog size (Mozilla/Firefox element resize bug)
		birtUtility.refreshElement(this.__instance);
		
		if ( Constants.request.servletPath == Constants.SERVLET_PARAMETER )
		{
			// in designer mode, recenter the dialog
			BirtPosition.center( this.__instance );
		}
	},

	__neh_key_icon: function( event )
	{
		if ( event.keyCode == 13 || event.keyCode == 32 )
		{
			this.__neh_click_icon();
		}
	},
	
	__bind : function( data )
	{
		// call to super
		AbstractExceptionDialog.prototype.__bind.apply( this, arguments );
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
		console.log("BirtMessageDialog, __preShow: function()")
		// disable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", true );
		
		// disable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", true );
		
		this.__isShow = false;
	},
	
	/**
	Called before element is hidden
	*/
	__preHide: function()
	{
		console.log("BirtMessageDialog, __preHide: function()")

		// enable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", false );
		
		// enable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", false );		
	}	
} );