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
 *	BirtNavigationBar.
 *	TODO: comments.
 */
BirtNavigationBar = Class.create( );

BirtNavigationBar.prototype = Object.extend( new AbstractBaseNavigationBar( ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id );
		this.__oPageNumber = $( 'pageNumber' );
		this.__oTotalPage = $( 'totalPage' );
		this.__cb_installEventHandlers( );
	}
}
);