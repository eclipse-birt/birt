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
 *	AbstractBaseDocument
 *	...
 */
AbstractBaseReportDocument = function( ) { };

AbstractBaseReportDocument.prototype = Object.extend( new AbstractReportComponent( ),
{
	__instance : null,
	__has_svg_support : false,
	
	/**
	 *	Event handler closures.
	 */
	__neh_resize_closure : null,
	__neh_select_closure : null,
	__beh_toc_closure : null,
	__beh_getPage_closure : null,
	__beh_changeParameter_closure : null,
		 		
	/**
	 *	Local version of __cb_installEventHandlers.
	 */
	__local_installEventHandlers : function( id, children, bookmark )
	{
		// jump to bookmark.
		if ( bookmark )
		{
			var obj = $( bookmark );
			if ( obj && obj.scrollIntoView )
			{
				obj.scrollIntoView( true );
			}
		}
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *
	 *	@id, object id
	 *	@return, void
	 */
	__local_disposeEventHandlers : function( id )
	{
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_resize : function( event )
	{
		var width;
		if( rtl )
		{
			var offsetRight = this.__instance.offsetLeft + this.__instance.offsetWidth;
			var offsetPadding = BirtPosition.viewportWidth( ) - offsetRight;					
			width = BirtPosition.viewportWidth( ) -  ( offsetPadding >= 250 ? 250 : 0 ) - 3;
		}
		else
		{
			width = BirtPosition.viewportWidth( ) -  ( this.__instance.offsetLeft >= 250 ? 250 : 0 ) - 3;
		}
		
		if( width > 0 )
			this.__instance.style.width = width + "px";
			
		var height = BirtPosition.viewportHeight( ) - this.__instance.offsetTop - 2;
		if( height > 0 )
			this.__instance.style.height = height + "px";
	},
	
	/**
	 *	Birt event handler for "getpage" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_parameter : function( id )
	{
		birtParameterDialog.__cb_bind( );
	},

	/**
	 *	Birt event handler for "change parameter" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_changeParameter : function( id )
	{
		// set task id
		var taskid = birtUtility.setTaskId( );
		
		if ( birtParameterDialog.__parameter > 0 )
		{
	        birtParameterDialog.__parameter.length = birtParameterDialog.__parameter.length - 1;
		}
		
		// Get current page number
		var pageNum = birtUtility.getPageNumber( );
		
		if( pageNum > 0 )
		{		
	        birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
	        							  "ChangeParameter", null, birtParameterDialog.__parameter,
										  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
										  { name : Constants.PARAM_PAGE, value : pageNum },
										  { name : Constants.PARAM_TASKID, value : taskid } );
		}
		else
		{
	        birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
	        							  "ChangeParameter", null, birtParameterDialog.__parameter,
										  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
										  { name : Constants.PARAM_TASKID, value : taskid } );			
		}
		birtSoapRequest.setURL( soapURL );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},
	
	/**
	 *	Handle change cascade parameter.
	 */
	__beh_cascadingParameter : function( id, object )
	{
		// set task id
		var taskid = birtUtility.setTaskId( );
		
	    birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
	    							 "GetCascadingParameter", null, object,
	    							 { name : Constants.PARAM_TASKID, value : taskid } );
		birtSoapRequest.setURL( soapURL );
		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__beh_toc : function( id )
	{
		var width;
		if( rtl )
		{
			var offsetRight = this.__instance.offsetLeft + this.__instance.offsetWidth;
			var offsetPadding = BirtPosition.viewportWidth( ) - offsetRight;		
			width = BirtPosition.viewportWidth( ) - ( offsetPadding < 250 ? 250 : 0 ) - 3;
		}
		else
		{
			width = BirtPosition.viewportWidth( ) -  ( this.__instance.offsetLeft < 250 ? 250 : 0 ) - 3;			
		}
		this.__instance.style.width = width + "px";
	},

	/**
	 *	Birt event handler for "getpage" event.
	 *
	 *	@param id, document id (optional since there's only one document instance)
	 *  @param object, pass some settings, for example: page,bookmark...
	 *	@return, true indicating server call
	 */
	__beh_getPage : function( id, object )
	{
		// set task id
		var taskid = birtUtility.setTaskId( );
		
		birtSoapRequest.setURL( soapURL );
		if ( object )
		{
			birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
										  "GetPage", null,
										  object,
										  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
										  { name : Constants.PARAM_TASKID, value : taskid } );
		}
		else
		{
			birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
										  "GetPage", null,
										  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
										  { name : Constants.PARAM_TASKID, value : taskid } );
		}

		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},

	/**
	 *	Birt event handler for "getpage" event with collected parameters.
	 *
	 *	@param id, document id (optional since there's only one document instance)
	 *  @param object, pass some settings, for example: page...
	 *	@return, true indicating server call
	 */
	__beh_getPageInit : function( id, object )
	{
		// set task id
		var taskid = birtUtility.setTaskId( );
		
		// Get current page number
		var pageNum = birtUtility.getPageNumber( );
		
		birtSoapRequest.setURL( soapURL );
		if ( object )
		{
			if( pageNum > 0 )
			{
				birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
											  "GetPage", null, birtParameterDialog.__parameter,
											  object,
											  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
											  { name : Constants.PARAM_PAGE, value : pageNum },
											  { name : Constants.PARAM_TASKID, value : taskid } );
			}
			else
			{
				birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
											  "GetPage", null, birtParameterDialog.__parameter,
											  object,
											  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
											  { name : Constants.PARAM_TASKID, value : taskid } );				
			}
		}
		else
		{
			if( pageNum > 0 )
			{
				birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
											  "GetPage", null, birtParameterDialog.__parameter,
											  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
											  { name : Constants.PARAM_PAGE, value : pageNum },
											  { name : Constants.PARAM_TASKID, value : taskid } );				
			}
			else
			{
				birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
											  "GetPage", null, birtParameterDialog.__parameter,
											  { name : Constants.PARAM_SVG, value : this.__has_svg_support? "true" : "false" },
											  { name : Constants.PARAM_TASKID, value : taskid } );
			}
		}

		birtEventDispatcher.setFocusId( null );	// Clear out current focusid.
		return true;
	},
	
	/**
	 *	Birt event handler for "print" event.
	 *
	 *	@id, document id (optional since there's only one document instance)
	 *	@return, true indicating server call
	 */
	__beh_export : function( id )
	{
		birtSoapRequest.setURL( soapURL);
		birtSoapRequest.addOperation( "Document", Constants.Document, "QueryExport", null );
		return true;
	}
});