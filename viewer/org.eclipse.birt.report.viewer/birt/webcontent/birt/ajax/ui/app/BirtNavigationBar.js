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
 *	BirtNavigationBar
 *	...
 */
BirtNavigationBar = Class.create( );
BirtNavigationBar.prototype = Object.extend( new AbstractUIComponent( ),
{
	/**
	 *	Total number of pages.
	 */
	__oTotalPage : null,
	
	/**
	 *	Current page number.
	 */
	__oPageNumber : null,
	
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
	},

	/**
	 *	Binding data to the navigation bar UI. Data includes page number, total
	 *	page number (optional).
	 *
	 *	@data, data DOM tree (Schema TBD)
	 *	@return, void
	 */
	__cb_bind : function( data )
	{
		if ( !data )
		{
			return;
		}
		
		var oPageNumbers = data.getElementsByTagName( 'PageNumber' );
		if ( !oPageNumbers && !oPageNumbers[0] )
		{
			return;
		}
		
		this.__oPageNumber.innerHTML = oPageNumbers[0].firstChild.data;
		
		var oTotalPages = data.getElementsByTagName( 'TotalPage' );
		this.__oTotalPage.innerHTML = ( oTotalPages && oTotalPages[0] )? oTotalPages[0].firstChild.data : '+';
		
		var pageNumber = parseInt( this.__oPageNumber.firstChild.data );
		var totalPage = ( this.__oTotalPage.firstChild.data == '+' )? '+' : parseInt( this.__oTotalPage.firstChild.data );

		var oImgs = this.__instance.getElementsByTagName( "INPUT" );
		oImgs[0].src = ( pageNumber > 1 ) ? "birt/images/FirstPage.gif" : "birt/images/FirstPage_disabled.gif";
		oImgs[0].style.cursor = ( pageNumber > 1 ) ? "pointer" : "default";
		oImgs[1].src = ( pageNumber > 1 ) ? "birt/images/PreviousPage.gif" : "birt/images/PreviousPage_disabled.gif";
		oImgs[1].style.cursor = ( pageNumber > 1 ) ? "pointer" : "default";
		oImgs[2].src = ( totalPage == '+' || pageNumber < totalPage ) ? "birt/images/NextPage.gif" : "birt/images/NextPage_disabled.gif";
		oImgs[2].style.cursor = ( totalPage == '+' || pageNumber < totalPage ) ? "pointer" : "default";
		oImgs[3].src = ( totalPage == '+' || pageNumber < totalPage ) ? "birt/images/LastPage.gif" : "birt/images/LastPage_disabled.gif";
		oImgs[3].style.cursor = ( totalPage == '+' || pageNumber < totalPage ) ? "pointer" : "default";
		
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, navigation bar id (optional since there is only one nav bar)
	 *	@return, void
	 */
	__cb_installEventHandlers : function( )
	{
		var oImgs = this.__instance.getElementsByTagName( 'INPUT' );
		
		if ( oImgs )
		{
			for ( var i = 0; i < oImgs.length; i++ )
			{
				if ( oImgs[i].type == 'image' )
				{
					Event.observe( oImgs[i], 'click', this.__neh_click.bindAsEventListener( this ), false );
				}
			}
		}
		
		// Observe "keydown" event
		this.keydown_closure = this.__neh_keydown.bindAsEventListener(this);
		Event.observe($('gotoPage'), 'keydown', this.keydown_closure, false);
	},

	/**
	 *	Handle press "Enter" key.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_keydown: function( event )
	{
		// If press 'Enter' key
		if( event.keyCode == 13 )
		{
			this.__gotoGage( );
			Event.stop( event );
		}
	},	

	/**
	 *	Handle clicking 'Goto' event.
	 *
	 *	@return, void
	 */	
	__gotoGage : function( )
	{
		var iPageNo = -1;
		var totalPage = ( this.__oTotalPage.firstChild.data == '+' )? '+' : parseInt( this.__oTotalPage.firstChild.data );
		
		var oGotoPage = $( 'gotoPage' );
		var pageNo = oGotoPage.value;
		if ( pageNo != null && birtUtility.trim( pageNo ).length > 0 )
		{
			iPageNo = parseInt( pageNo );
		}
		if ( iPageNo > 0 && iPageNo <= totalPage )
		{
			birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : oGotoPage.value } );
		}
		else
		{			
			alert( Constants.error.invalidPageNumber );
			oGotoPage.focus( );
		}
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click : function( event )
	{
		var pageNumber = parseInt( this.__oPageNumber.firstChild.data );
		var totalPage = ( this.__oTotalPage.firstChild.data == '+' )? '+' : parseInt( this.__oTotalPage.firstChild.data );
		
		var oBtn = Event.element( event );
		if ( oBtn )
		{
			switch ( oBtn.name )
			{
   				case 'first':
 				{
 					if ( pageNumber > 1 )
 					{
						birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : 1 } );
					}
 					break;
 				}
   				case 'previous':
 				{
 					if ( pageNumber > 1 )
 					{
						birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : pageNumber - 1 } );
					}
 					break;
 				}
   				case 'next':
 				{
 					if ( totalPage == '+' || pageNumber < totalPage )
 					{
	 					birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : pageNumber + 1 } );
 					}
 					break;
 				}
   				case 'last':
 				{
 					if ( totalPage == '+' || pageNumber < totalPage )
 					{
 						birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : totalPage } );
 					}
 					break;
 				}
   				case 'goto':
   				{
   					this.__gotoGage( );
   					break;
   				}
				default:
				{
					break;
				}	
			}
		}
	},
	
	__get_current_page : function( )
	{
		return this.__oPageNumber.innerHTML;
	},
	
	/**
	 * Load current page. Triggered by init.
	 */
	__init_page : function( )
	{
		if( birtParameterDialog.collect_parameter( ) )
		{			
			if ( this.__oPageNumber.firstChild )
			{
				var pageNumber = parseInt( this.__oPageNumber.firstChild.data );
				birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE_INIT, { name : "page", value : pageNumber } );
			}
			else
			{
				birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE_INIT );
			}
		}
	}
}
);