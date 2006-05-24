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
 *	BirtParameterDialog
 *	...
 */
BirtParameterDialog = Class.create( );

BirtParameterDialog.prototype = Object.extend( new AbstractParameterDialog( ),
{
	/**
	 *	Parameter dialog working state. Whether embedded inside
	 *	designer dialog.
	 */
	__mode : 'frameset',
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id, mode )
	{
		this.initializeBase( id );
		this.__mode = mode;
		
		if ( this.__mode == 'parameter' )
		{
			// Hide dialog title bar if embedded in designer.
			var paramDialogTitleBar = $( id + 'dialogTitleBar' );
			paramDialogTitleBar.style.display = 'none';			
		}
	},

	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind : function( data )
	{
		if ( !data )
		{
			return;
		}
		
		var cascadeParamObj = data.getElementsByTagName( 'CascadeParameter' );
		var confirmObj = data.getElementsByTagName( 'Confirmation' );
		if ( cascadeParamObj.length > 0 )
		{
			this.__propogateCascadeParameter( data );
		}
		else if ( confirmObj.length > 0 )
		{
			this.__close( );
		}
	},

	/**
	 *	Handle clicking on okRun.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress : function( )
	{
		if( birtParameterDialog.collect_parameter( ) )
		{
			if ( this.__mode == 'parameter' )
			{
				birtEventDispatcher.broadcastEvent( birtEvent.__E_CACHE_PARAMETER );
			}
			else if ( this.__mode == 'run' )
			{
				this.__doSubmit( );
			}
			else
			{
				birtEventDispatcher.broadcastEvent( birtEvent.__E_CHANGE_PARAMETER );
				this.__l_hide( );
			}
		}
	},
	
	/**
	 *	Override cancel button click.
	 */
	__neh_cancel : function( )
	{
		if ( this.__mode == 'parameter' )
		{
			this.__cancel();
		}
		else
		{
			this.__l_hide( );
		}
	},

	/**
	 *	Handle submit form with current parameters.
	 *
	 *	@return, void
	 */
	__doSubmit : function( )
	{
		var divObj = document.createElement( "DIV" );
		document.body.appendChild( divObj );
		divObj.style.display = "none";
		
		var formObj = document.createElement( "FORM" );
		divObj.appendChild( formObj );
		formObj.action = window.location.href;
		formObj.method = "post";
		
		if ( this.__parameter != null )
		{
			for( var i = 0; i < this.__parameter.length; i++ )	
			{
				var param = document.createElement( "INPUT" );
				formObj.appendChild( param );
				param.TYPE = "HIDDEN";
				param.name = this.__parameter[i].name;
				param.value = this.__parameter[i].value;
			}
		}
		
		this.__l_hide( );
		formObj.submit( );		
	},

	/**
	 *	Caching parameters success, close window.
	 *
	 *	@return, void
	 */	
	__close : function( )
	{
		window.status = "close";
		//window.opener = null;
		//window.close( );
	},
	
	/**
	 *	Click 'Cancel', close window.
	 *
	 *	@return, void
	 */	
	__cancel : function( )
	{
		window.status = "cancel";
	}
}
);