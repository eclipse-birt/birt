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
	 * indicate whether exception detail is show or not.
	 */
	__isShow: false,
	
	/**
	 * control id definitions
	 */
	__TRACE_CONTAINER: 'exceptionTraceContainer',
	__LABEL_SHOW_TRACE: 'showTraceLabel',
	__LABEL_HIDE_TRACE: 'hideTraceLabel',
	
	/**
	 * Event handler closures.
	 */
	__neh_click_input_closurre : null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id, "600px" );
		this.__allowSelection = true; // allow selecting text with the mouse
		
		// it looks like IE 6 handles the width differently
		var faultDetailContainer = $( "faultdetail" ); 
		var faultStringContainer = $("faultStringContainer");
		if ( BrowserUtility.isIE && !BrowserUtility.isIE7 )
		{
			this.__setFaultContainersWidth( "580px" );
			faultStringContainer.style.overflowX = "auto";
			faultStringContainer.style.paddingBottom = "20px";
			faultDetailContainer.parentNode.style.width = "570px";
			faultDetailContainer.style.width = "100%";
		}
		else
		{
			this.__setFaultContainersWidth( "520px" );
			faultStringContainer.style.overflow = "auto";
		}

		// Bugzilla 225924: Fix overflow issue in the stack trace container		
		if ( BrowserUtility.isSafari || BrowserUtility.isIE7 || BrowserUtility.isFirefox3 )
		{
			faultDetailContainer.parentNode.style.width = "510px";	
		}
		
		this.__z_index = 300;
		
		// click event on input control
		this.__neh_click_input_closure = this.__neh_click_input.bindAsEventListener( this );
		Event.observe( $( this.__LABEL_SHOW_TRACE ), 'click', this.__neh_click_input_closure, false );				
		Event.observe( $( this.__LABEL_HIDE_TRACE ), 'click', this.__neh_click_input_closure, false );
	},	
	
	/**
	*	Handle clicking on input control.
	* 
	* 	@return, void
	*/
	__neh_click_input: function( event )
	{
		if( !this.__isShow )
		{
			$( this.__TRACE_CONTAINER ).style.display = "block";
			$( this.__LABEL_SHOW_TRACE ).style.display = "none";
			$( this.__LABEL_HIDE_TRACE ).style.display = "block";
		}
		else
		{
			$( this.__TRACE_CONTAINER ).style.display = "none";
			$( this.__LABEL_SHOW_TRACE ).style.display = "block";
			$( this.__LABEL_HIDE_TRACE ).style.display = "none";			
		}
		
		this.__isShow = !this.__isShow;
		
		// refresh the dialog size (Mozilla/Firefox element resize bug)
		birtUtility.refreshElement(this.__instance);
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
		
		// close the exception stack trace
		$( this.__TRACE_CONTAINER ).style.display = 'none';
		$( this.__LABEL_SHOW_TRACE ).style.display = "block";
		$( this.__LABEL_HIDE_TRACE ).style.display = "none";			
		
		this.__isShow = false;
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