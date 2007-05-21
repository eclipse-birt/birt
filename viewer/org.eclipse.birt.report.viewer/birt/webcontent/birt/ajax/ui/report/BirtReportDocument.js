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
		this.__beh_getPageInit_closure = this.__beh_getPageInit.bind( this );
		this.__beh_changeParameter_closure = this.__beh_changeParameter.bind( this );
		this.__beh_toc_closure = this.__beh_toc.bindAsEventListener( this );
		this.__beh_cacheParameter_closure = this.__beh_cacheParameter.bind( this );
		this.__beh_print_closure = this.__beh_print.bind( this );
		this.__beh_pdf_closure = this.__beh_pdf.bind( this );
		this.__beh_cancelTask_closure = this.__beh_cancelTask.bind( this );
		this.__beh_getPageAll_closure = this.__beh_getPageAll.bind( this );
		this.__beh_exportReport_closure = this.__beh_exportReport.bind( this );
				
		Event.observe( window, 'resize', this.__neh_resize_closure, false );
		
		birtEventDispatcher.registerEventHandler( birtEvent.__E_GETPAGE, this.__instance.id, this.__beh_getPage_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_GETPAGE_INIT, this.__instance.id, this.__beh_getPageInit_closure );		
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PARAMETER, this.__instance.id, this.__beh_parameter );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CHANGE_PARAMETER, this.__instance.id, this.__beh_changeParameter_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CASCADING_PARAMETER, this.__instance.id, this.__beh_cascadingParameter );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_TOC, this.__instance.id, this.__beh_toc_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_QUERY_EXPORT, this.__instance.id, this.__beh_export );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CACHE_PARAMETER, this.__instance.id, this.__beh_cacheParameter_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PRINT, this.__instance.id, this.__beh_print_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_PDF, this.__instance.id, this.__beh_pdf_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_CANCEL_TASK, this.__instance.id, this.__beh_cancelTask_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_GETPAGE_ALL, this.__instance.id, this.__beh_getPageAll_closure );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_EXPORT_REPORT, this.__instance.id, this.__beh_exportReport_closure );
						
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
		birtSoapRequest.setURL( soapURL );
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
		birtPrintReportDialog.__cb_bind( );
	},
	
	/* 
	__beh_print : function( id )
	{		
		// If print the whole report
		if ( confirm( "Do you want to print the whole report?" ) )
		{
			var action = window.location.href;
			var reg = new RegExp( "/frameset[^\\?]*", "g" );
			action = action.replace( reg, "/preview" );

			var divObj = document.createElement( "DIV" );
			document.body.appendChild( divObj );
			divObj.style.display = "none";
			
			var formObj = document.createElement( "FORM" );
			divObj.appendChild( formObj );
			
			if ( !birtParameterDialog.collect_parameter( ) )
				return;
			
			if ( birtParameterDialog.__parameter != null )
			{
				for( var i = 0; i < birtParameterDialog.__parameter.length; i++ )	
				{
					var param = document.createElement( "INPUT" );
					formObj.appendChild( param );
					param.TYPE = "HIDDEN";
					param.name = birtParameterDialog.__parameter[i].name;
					param.value = birtParameterDialog.__parameter[i].value;
					
					//replace the URL parameter			
					var reg = new RegExp( "&" + param.name + "[^&]*&*", "g" );
					action = action.replace( reg, "&" );
				}
			}	
	
			formObj.action = action;
			formObj.method = "post";
			formObj.submit( );
					
			window.print( );
		}
		else
		{
			var docObj = document.getElementById( "Document" );
			if ( !docObj || birtUtility.trim( docObj.innerHTML ).length <= 0)
			{
				alert ( "Please generate the current report first." );
				return;
			}	
			
			var pwin = window.open( "", "print" ); 
			if ( !pwin )
			{
				pwin = window;
			}
				
			pwin.document.write( docObj.innerHTML ); 		
			pwin.print( );
			pwin.location.reload( );			
		}		
	},
	*/
	
	/**
	 *	Birt event handler for "pdf" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_pdf : function( id )
	{	
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

			// Replace "html" to "pdf"
			var action = soapURL;
			var reg = new RegExp( "([&|?]{1}__format\s*)=([^&|^#]*)", "gi" );
			if( action.search( reg ) < 0 )
			{
				action = action + "&__format=pdf";
			}
			else
			{
				action = action.replace( reg, "$1=pdf" );
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
	 *	Birt event handler for "CancelTask" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_cancelTask : function( id, object )
	{	
        birtSoapRequest.addOperation( Constants.documentId, Constants.Document, "CancelTask", null, object );
		birtSoapRequest.setURL( soapURL );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},
	
	/**
	 *	Birt event handler for "GetPageAll" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_getPageAll : function( id, object )
	{	
		// set task id
		var taskid = birtUtility.setTaskId( );
		
		if( object )
		{
	        birtSoapRequest.addOperation( Constants.documentId, Constants.Document, "GetPageAll",
	        							   null, birtParameterDialog.__parameter, object,
	        							   { name : "svg", value : this.__has_svg_support? "true" : "false" },
	        							   { name : this.__task_id, value : taskid } );
		}
		else
		{
	        birtSoapRequest.addOperation( Constants.documentId, Constants.Document, "GetPageAll",
	        							   null, birtParameterDialog.__parameter,
        							       { name : "svg", value : this.__has_svg_support? "true" : "false" },
	        							   { name : this.__task_id, value : taskid } );			
		}
		birtSoapRequest.setURL( soapURL );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},

	/**
	 *	Birt event handler for "ExportReport" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_exportReport : function( id )
	{
		birtExportReportDialog.__cb_bind( );
	}
});