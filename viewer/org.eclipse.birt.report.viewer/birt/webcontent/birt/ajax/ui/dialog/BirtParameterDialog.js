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
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.initializeBase(id);
		
		// Initialize event handler closures	
		this.__neh_ok_run_closure = this.__neh_ok_run.bind(this);		
		this.__localext_installEventHandlers(id);
	},

	/**
	Install event handlers based on Input Object Name.
	*/
	__localext_installEventHandlers : function( id )
	{	
		// OK and Cancel buttons
		var oInputs = this.__instance.getElementsByTagName( 'input' );
//		if ( 'okRun' == oInputs[oInputs.length - 2].name )
		{
			// reset event on okRun button
//			Event.stopObserving( oInputs[oInputs.length - 2], 'click', this.__neh_okay_closure , false );
//			Event.observe( oInputs[oInputs.length - 2], 'click', this.__neh_ok_run_closure , false );
		}
	},

	/**
	 *	Handle clicking on okRun.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_ok_run: function()
	{
		if( birtParameterDialog.collect_parameter( ) )
		{
			this.__doSubmit( );
		}
	},

	__doSubmit: function()
	{
		var divObj = document.createElement("DIV");
		document.body.appendChild( divObj );
		divObj.style.display = "none";
		
		var formObj = document.createElement("FORM");
		divObj.appendChild( formObj );
		formObj.action = window.location.href;
		formObj.method = "post";
		
		if ( this.__parameter != null )
		{
			for( var i = 0; i<this.__parameter.length; i++ )	
			{
				var param = document.createElement("INPUT");
				formObj.appendChild(param);
				param.TYPE = "HIDDEN";
				param.name = this.__parameter[i].name;
				param.value = this.__parameter[i].value;
			}
		}
										
		this.__l_hide( );				
		
		formObj.submit();		
	}
} );