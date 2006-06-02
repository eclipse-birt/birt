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
 *	BirtReportDocument
 *	...
 */
BirtReportDocument = Class.create( );

BirtReportDocument.prototype = Object.extend( new AbstractBaseReportDocument( ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__instance = $( id );
		this.__neh_resize( );
		this.__has_svg_support = hasSVGSupport;
		
		//	Prepare closures.
		this.__neh_resize_closure = this.__neh_resize.bindAsEventListener( this );
		this.__beh_getPage_closure = this.__beh_getPage.bind( this );
		this.__beh_changeParameter_closure = this.__beh_changeParameter.bind( this );
		this.__beh_toc_closure = this.__beh_toc.bindAsEventListener( this );
		this.__beh_cacheParameter_closure = this.__beh_cacheParameter.bind( this );
		this.__beh_print_closure = this.__beh_print.bind( this );
		this.__beh_pdf_closure = this.__beh_pdf.bind( this );
				
		Event.observe( window, 'resize', this.__neh_resize_closure, false );
		
		birtEventDispatcher.registerEventHandler( birtEvent.__E_GETPAGE, this.__instance.id, this.__beh_getPage_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PARAMETER, this.__instance.id, this.__beh_parameter );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CHANGE_PARAMETER, this.__instance.id, this.__beh_changeParameter_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CASCADING_PARAMETER, this.__instance.id, this.__beh_cascadingParameter );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_TOC, this.__instance.id, this.__beh_toc_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_QUERY_EXPORT, this.__instance.id, this.__beh_export );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CACHE_PARAMETER, this.__instance.id, this.__beh_cacheParameter_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PRINT, this.__instance.id, this.__beh_print_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PDF, this.__instance.id, this.__beh_pdf_closure );
				
  		birtGetUpdatedObjectsResponseHandler.addAssociation( "Docum", this );
  		
		// TODO: rename it to birt event
		this.__cb_installEventHandlers( id );
	},

	/**
	 *	Birt event handler for "cache parameter" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_cacheParameter : function( id )
	{
		if ( birtParameterDialog.__parameter > 0 )
		{
	        birtParameterDialog.__parameter.length = birtParameterDialog.__parameter.length - 1;
		}
        birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
        							  "CacheParameter", null, birtParameterDialog.__parameter );
		birtSoapRequest.setURL( document.location );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},

	/**
	 *	Birt event handler for "print" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_print : function( id )
	{
		var docObj = document.getElementById( 'Document' );
		if ( docObj )
		{
			var pwin = window.open( "", "print" ); 
			pwin.document.write( docObj.innerHTML ); 
			pwin.print( );
			pwin.location.reload( );
		}
		else
		{
			window.print( );
		}
	},

	/**
	 *	Birt event handler for "pdf" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_pdf : function( id )
	{	
		var docObj = document.getElementById( 'Document' );
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

			// Replace "html" to "pdf"		
			var action = window.location.href;		
			var reg = new RegExp( "&__format=html", "g" );
			action = action.replace( reg, "&__format=pdf" );

			formObj.action = action;
			formObj.method = "post";			
			formObj.submit( );
		}
	}		
});