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
 *	Birt print report dialog.
 */
BirtPrintReportDialog = Class.create( );

BirtPrintReportDialog.prototype = Object.extend( new AbstractBaseDialog( ),
{
	/**
	 * Print window instance.
	 */
	__printWindow : null,

	/**
	 * Timer instance control the popup print dialog.
	 */
	__timer : null,
		
	__printFormat : 'html',
	__neh_formatradio_click_closure : null,
	__neh_pageradio_click_closure : null,
	
	/**
	* PDF page fit setting
	*/
	FIT_TO_ACTUAL : '0',
	FIT_TO_WIDTH : '1',
	FIT_TO_WHOLE : '2',
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id );
		this.__z_index = 200;
		
		this.__initLayout( );		
		
		// Binding
		this.__neh_formatradio_click_closure = this.__neh_formatradio_click.bindAsEventListener( this );
		this.__neh_pageradio_click_closure = this.__neh_pageradio_click.bindAsEventListener( this );
			
		this.__installEventHandlers( id );		
	},
	
	/**
	 * Initilize dialog layout
	 * 
	 * @return, void
	 */
	__initLayout : function( )
	{
	
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{	
		// switch print format
		var oInputs = $( 'printFormatSetting' ).getElementsByTagName( 'input' );
		for( var i=0; i<oInputs.length; i++ )
		{
			if( oInputs[i].type == 'radio' )			
				Event.observe( oInputs[i], 'click', this.__neh_formatradio_click_closure,false );
		}

		// page setting
		var oInputs = $( 'printPageSetting' ).getElementsByTagName( 'input' );
		for( var i=0; i<oInputs.length; i++ )
		{
			if( oInputs[i].type == 'radio' )			
				Event.observe( oInputs[i], 'click', this.__neh_pageradio_click_closure,false );
		}					
	},

	/**
	 *	Handle clicking on ok.
	 *
	 *	@return, void
	 */
	__okPress : function( )
	{
		this.__printAction( );
	},
		
	/**
	 * Handle print report action
	 * 
	 * @return, true or false
	 */
	__printAction : function( )
	{					
		var docObj = document.getElementById( "Document" );
		if ( !docObj || birtUtility.trim( docObj.innerHTML ).length <= 0)
		{
			alert ( "Report document should be generated first." );	
			return false;
		}	
		else
		{	
			var divObj = document.createElement( "DIV" );
			document.body.appendChild( divObj );
			divObj.style.display = "none";
		
			var formObj = document.createElement( "FORM" );
			divObj.appendChild( formObj );

			// Replace format in URL with selected print format
			var action = soapURL;
			var reg = new RegExp( "([&|?]{1}" + Constants.PARAM_FORMAT + "\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&" + Constants.PARAM_FORMAT + "=" + this.__printFormat;
			}
			else
			{
				action = action.replace( reg, "$1=" + this.__printFormat );
			}
			
			// Delete page and pagerange settings in url if existed
			reg = new RegExp( "([&|?]{1})" + Constants.PARAM_PAGE + "\s*=[^&|^#]*", "gi" );
			action = action.replace( reg, "$1");
			
			reg = new RegExp( "([&|?]{1})" + Constants.PARAM_PAGERANGE + "\s*=[^&|^#]*", "gi" );
			action = action.replace( reg, "$1");				
			
			if( $( 'printPageCurrent' ).checked )
			{
				// Set page setting
				var currentPage = birtUtility.trim( $( 'pageNumber' ).innerHTML );
				action = action + "&" + Constants.PARAM_PAGE + "=" + currentPage;				
			}
			else if( $( 'printPageRange' ).checked )
			{
				// Set page range setting
				var pageRange = birtUtility.trim( $( 'printPageRange_input' ).value );
				action = action + "&" + Constants.PARAM_PAGERANGE + "=" + pageRange;
			}			

			var oSelect = this.__instance.getElementsByTagName( 'select' )[0];
			var fittopage = "false";
			var pagebreakonly = "false";
			
			// fit to page width
			if( oSelect.value == this.FIT_TO_WIDTH )
			{
				fittopage = "true";
			}
			else if( oSelect.value == this.FIT_TO_WHOLE )
			{
				fittopage = "true";
				pagebreakonly = "true";
			}

			reg = new RegExp( "([&|?]{1}" + Constants.PARAM_FITTOPAGE + "\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&" + Constants.PARAM_FITTOPAGE + "=" + fittopage;
			}
			else
			{
				action = action.replace( reg, "$1=" + fittopage );
			}
			
			reg = new RegExp( "([&|?]{1}" + Constants.PARAM_PAGEBREAKONLY + "\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&" + Constants.PARAM_PAGEBREAKONLY + "=" + pagebreakonly;
			}
			else
			{
				action = action.replace( reg, "$1=" + pagebreakonly );
			}				
												
			// Force "__overwrite" as false
			reg = new RegExp( "([&|?]{1}" + Constants.PARAM_OVERWRITE + "\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&" + Constants.PARAM_OVERWRITE + "=false";
			}
			else
			{
				action = action.replace( reg, "$1=false" );
			}
			
			// Replace servlet pattern as output
			action = action.replace( /[\/][a-zA-Z]+[?]/, "/"+Constants.SERVLET_OUTPUT+"?" );
			
			// Open a new window to print
			this.__printWindow = window.open('','birtPrint');
									
			formObj.action = action;
			formObj.method = "post";
			formObj.target = "birtPrint";			
			formObj.submit( );
			
			// Launch print dialog.
			this.__timer = window.setTimeout( this.__cb_print.bindAsEventListener( this ), 1000 );						
		}
		
		return true;		
	},

	/**
	 * Timer call back function. Control the popup print dialog.
	 */
	__cb_print : function( )
	{
		try
		{
			var url = this.__printWindow.location.toString( );
			var err = this.__printWindow.document.getElementById( "birt_errorPage" );
			if( err && err.innerHTML != '' )
			{
				window.clearTimeout( this.__timer );
				return;
			}
			if ( url.indexOf( Constants.SERVLET_OUTPUT ) < 0 )
			{
				this.__timer = window.setTimeout( this.__cb_print.bindAsEventListener( this ), 1000 );
			}
			else
		  	{
				window.clearTimeout( this.__timer );
				this.__printWindow.print( );
		  	}
		}
		catch( e )
		{
		}
	},
	
	/**
	 *	Native event handler for print format radio control.
	 */
	__neh_formatradio_click : function( event )
	{
		var oSC = Event.element( event );
		var oSelect = this.__instance.getElementsByTagName( 'select' )[0];
		if( oSC.checked && oSC.id == 'printAsPDF' )
		{
			this.__printFormat = 'pdf';
			oSelect.disabled = false;
			oSelect.focus();
		}
		else
		{
			this.__printFormat = 'html';
			oSelect.disabled = true;
		}
	},

	/**
	 *	Native event handler for page radio control.
	 */
	__neh_pageradio_click : function( event )
	{
		var oSC = Event.element( event );	
		var oInput = $( 'printPageRange_input' );
		if( oSC.checked && oSC.id == 'printPageRange' )
		{
			oInput.disabled = false;
			oInput.focus( );
		}
		else
		{
			oInput.disabled = true;
			oInput.value = "";
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