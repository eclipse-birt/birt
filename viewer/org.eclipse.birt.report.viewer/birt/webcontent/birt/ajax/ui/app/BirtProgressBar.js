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
 *	BirtProgressBar
 *	...
 */
BirtProgressBar = Class.create( );
BirtProgressBar.prototype = Object.extend( new AbstractUIComponent( ),
{
	/**
	 *	Latency that will trigger the progress bar.
	 */
	__interval : 300,
	
	/**
	 *	Timer instance.
	 */
	__timer : null,
	
	/**
	 *	mask instance.
	 */
	__mask : null,
	
	/**
	 *	Closures
	 */
	__cb_bind_closure : null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id );
		this.__mask = this.__create_mask( );
		this.__cb_bind_closure = this.__cb_bind.bindAsEventListener( this );
	},

	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@return, void
	 */
	__cb_bind : function( )
	{
		if( birtCommunicationManager.__active )
		{
			this.__timer = window.setTimeout( this.__cb_bind_closure, this.__interval );
			this.__l_show( );
		}
		else
	  	{
			window.clearTimeout( this.__timer );
			this.__l_hide( );
	  	}
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, response id 
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, response id 
	 *	@return, void
	 */
	__start : function( )
	{
		this.__timer = window.setTimeout( this.__cb_bind_closure, this.__interval );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, response id 
	 *	@return, void
	 */
	__stop : function( )
	{
		window.clearTimeout( this.__timer );
		this.__l_hide( );
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, response id 
	 *	@return, void
	 */
	__create_mask : function( )
	{
		var oMask = document.createElement( 'iframe' );
		// Workaround for IE https secure warning
		oMask.src = "birt/pages/common/blank.html";
		oMask.style.position = 'absolute';
		oMask.style.top = '0px';
		oMask.style.left = '0px';
		oMask.style.width = '100%';
		var height = BirtPosition.viewportHeight( );
		oMask.style.height = height + 'px';
		oMask.style.zIndex = '300';
		oMask.style.backgroundColor = '#dbe4ee';
		oMask.style.filter = 'alpha( opacity = 0.0 )';
		oMask.style.opacity = '.0';
		oMask.scrolling = 'no';
		oMask.marginHeight = '0px';
		oMask.marginWidth = '0px';
		oMask.style.display = 'none';
		document.body.appendChild( oMask );
		
		return oMask;		
	},

	/**
	 *	Show progress bar.
	 */
	__l_show : function( )
	{
		Element.show( this.__mask, this.__instance );
		BirtPosition.center( this.__instance );
	},
	
	/**
	 *	Hide progress bar.
	 */
	__l_hide : function( )
	{
		Element.hide( this.__instance, this.__mask );
	}
} );