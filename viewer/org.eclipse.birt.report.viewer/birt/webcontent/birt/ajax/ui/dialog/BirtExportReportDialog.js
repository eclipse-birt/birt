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
 *	Birt export report dialog.
 */
BirtExportReportDialog = Class.create( );

BirtExportReportDialog.prototype = Object.extend( new AbstractBaseDialog( ),
{
	__neh_select_change_closure : null,
	__neh_radio_click_closure : null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id );
		this.__z_index = 200;
		
		this.__enableExtSection( );
		
		// Binding
		this.__neh_select_change_closure = this.__neh_select_change.bindAsEventListener( this );
		this.__neh_radio_click_closure = this.__neh_radio_click.bindAsEventListener( this );
			
		this.__installEventHandlers( id );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		Event.observe( oSelects[0], 'change', this.__neh_select_change_closure, false );
		
		var oInputs = $( 'exportPageSetting' ).getElementsByTagName( 'input' );
		for( var i=0; i<oInputs.length; i++ )
		{
			if( oInputs[i].type == 'radio' )			
				Event.observe( oInputs[i], 'click', this.__neh_radio_click_closure,false );
		}		
	},

	/**
	 *	Handle clicking on ok.
	 *
	 *	@return, void
	 */
	__okPress : function( )
	{
		var oSelect = $( 'exportFormat' );
		if( oSelect.value == '' )
		{
			this.__l_hide( );
			return;
		}
		
		this.__exportAction( );
	},
	
	/**
	 * Handle export report action
	 * 
	 * @return, void
	 */
	__exportAction : function( )
	{
		var format = $( 'exportFormat' ).value.toLowerCase( );
		
		var docObj = document.getElementById( "Document" );
		if ( !docObj || birtUtility.trim( docObj.innerHTML ).length <= 0)
		{
			alert ( "Report document should be generated first." );	
			return;
		}	
		else
		{	
			var divObj = document.createElement( "DIV" );
			document.body.appendChild( divObj );
			divObj.style.display = "none";
		
			var formObj = document.createElement( "FORM" );
			divObj.appendChild( formObj );

			// Set selected output format
			var action = window.location.href;
			var reg = new RegExp( "([&|?]{1}__format\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&__format=" + format;
			}
			else
			{
				action = action.replace( reg, "$1=" + format );
			}
			
			// Delete page and pagerange settings in url if existed
			reg = new RegExp( "([&|?]{1})__page\s*=[^&|^#]*", "gi" );
			action = action.replace( reg, "$1");
			
			reg = new RegExp( "([&|?]{1})__pagerange\s*=[^&|^#]*", "gi" );
			action = action.replace( reg, "$1");				
			
			if( $( 'exportPageCurrent' ).checked )
			{
				// Set page setting
				var currentPage = birtUtility.trim( $( 'pageNumber' ).innerHTML );
				action = action + "&__page=" + currentPage;				
			}
			else if( $( 'exportPageRange' ).checked )
			{
				// Set page range setting
				var pageRange = birtUtility.trim( $( 'exportPageRange_input' ).value );
				action = action + "&__pagerange=" + pageRange;
			}			
			
			// If output format is pdf/postscript, set some options
			if( format == 'pdf' )
			{
				var fittopage = "false";
				var pagebreakonly = "false";
				
				// fit to page width
				if( $( 'exportFitToWidth' ).checked )
				{
					fittopage = "true";
				}
				else if( $( 'exportFitToWhole' ).checked )
				{
					fittopage = "true";
					pagebreakonly = "true";
				}

				reg = new RegExp( "([&|?]{1}__fittopage\s*)=([^&|^#]*)", "gi" );
				if( action.search( reg ) < 0 )
				{
					action = action + "&__fittopage=" + fittopage;
				}
				else
				{
					action = action.replace( reg, "$1=" + fittopage );
				}
				
				reg = new RegExp( "([&|?]{1}__pagebreakonly\s*)=([^&|^#]*)", "gi" );
				if( action.search( reg ) < 0 )
				{
					action = action + "&__pagebreakonly=" + pagebreakonly;
				}
				else
				{
					action = action.replace( reg, "$1=" + pagebreakonly );
				}				
			}
			
			// Force "__asattachment" as true
			reg = new RegExp( "([&|?]{1}__asattachment\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&__asattachment=true";
			}
			else
			{
				action = action.replace( reg, "$1=true" );
			}			
			
			// Force "__overwrite" as false
			reg = new RegExp( "([&|?]{1}__overwrite\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&__overwrite=false";
			}
			else
			{
				action = action.replace( reg, "$1=false" );
			}
						
			formObj.action = action;
			formObj.method = "post";			
			formObj.submit( );
		}		
	},
	
	/**
	 *	Native event handler for radio control.
	 */
	__neh_radio_click : function( event )
	{
		var oSC = Event.element( event );		
		if( oSC.type == 'radio' )
		{
			var oInput = $( 'exportPageRange_input' );
			if( oSC.id == 'exportPageRange' )
			{
				oInput.disabled = false;
				oInput.focus( );
			}
			else
			{
				oInput.disabled = true;
				oInput.value = "";
			}
		}		
	},
	
	/**
	 *	Native event handler for select control.
	 */
	__neh_select_change : function( event )
	{
		this.__enableExtSection( );		
	},
	
	/**
	 * Enable the extended setting controls according to current selected output format.
	 */
	__enableExtSection : function( )
	{		
		var format = $( 'exportFormat' ).value.toLowerCase( );
		if( format == 'pdf' )
		{
			this.__setDisabled( 'exportFitSetting', false );
		}
		else
		{
			this.__setDisabled( 'exportFitSetting', true );
		}
	},
	
	/**
	 * Set disabled flag for all the controls in the container
	 * 
	 * @param id, html container id. ( DIV/TABLE....)
	 * @param flag, true or false
	 * @return, void
	 */
	__setDisabled: function( id, flag )
	{
		var container = $( id );
		if( container )
		{
			var oInputs = container.getElementsByTagName( 'input' );
			for( var i=0; i<oInputs.length; i++ )
				oInputs[i].disabled = flag;
		}
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