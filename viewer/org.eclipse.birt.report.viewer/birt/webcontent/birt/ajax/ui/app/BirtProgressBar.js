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

BirtProgressBar.prototype = Object.extend( new AbstractBaseProgressBar( ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id );
		this.__mask = this.__create_mask( );
		this.__cb_bind_closure = this.__cb_bind.bindAsEventListener( this );
	}
} );