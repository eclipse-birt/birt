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
 *	AbstractReportComponent.
 *		Base class for all UI components.
 */
AbstractReportComponent = function( ) { };

AbstractReportComponent.prototype =
{
	/**
	 *	UI component html instance.
	 */
	__instance : null,
	
	/**
	 *	Re-render ui object with new content.
	 *
	 *	@id, ui object id
	 *	@content, new html UI content
	 *	@return, void
	 */
	__cb_render : function( id, content )
	{
		var oDiv = $( id );
		
		while( oDiv.childNodes.length > 0)
		{
			oDiv.removeChild(oDiv.firstChild);
		}
		
		var container = document.createElement( "div" );
		container.style.position = "relative";
		container.style.padding = "15px";
		container.innerHTML = content;
		oDiv.appendChild( container );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, table object id
	 *	@return, void
	 */
	__cb_installEventHandlers : function( id, children, bookmark, type )
	{
		if ( this.__local_installEventHandlers )
		{
			this.__local_installEventHandlers( id, children, bookmark, type );
		}
		
		var container = $( id );

		container[ Constants.reportBase ] = ( type != 'Group' && type != 'ColumnInfo');
		container[ Constants.activeIds ] = [ ]; // Need to remember active children
		container[ Constants.activeIdTypes ] = [ ]; // Need to remember active children types
		
		if ( !children )
		{
			return;
		}

		// Also need to take care the active children.
		for( var i = 0; i < children.length; i++ )
		{
			var oElementIds = children[i].getElementsByTagName( 'Id' );
			var oElementTypes = children[i].getElementsByTagName( 'Type' );

			var birtObj = ReportComponentIdRegistry.getObjectForType( oElementTypes[0].firstChild.data );
			
			if ( !birtObj || !birtObj.__cb_installEventHandlers )
			{
				continue;
			}
			
			container[ Constants.activeIds ].push( oElementIds[0].firstChild.data );
			container[ Constants.activeIdTypes ].push( oElementTypes[0].firstChild.data );

			birtObj.__cb_installEventHandlers( oElementIds[0].firstChild.data, null, null, oElementTypes[0].firstChild.data );
		}
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *	Remove local event listeners
	 *
	 *	@id, object id
	 *	@return, void
	 */
	__cb_disposeEventHandlers : function( id, type )
	{
		if ( this.__local_disposeEventHandlers )
		{
			this.__local_disposeEventHandlers( id, type );
		}

		var container = $( id );
		
		var id = null;
		while( container[ Constants.activeIds ].length > 0 )
		{
			var id = container[ Constants.activeIds ].shift( )
			var type = container[ Constants.activeIdTypes ].shift( );
			var birtObj = ReportComponentIdRegistry.getObjectForType( type );
			if ( !birtObj || !birtObj.__cb_disposeEventHandlers )
			{
				continue;
			}
			birtObj.__cb_disposeEventHandlers( id, type );
		}
	}
}