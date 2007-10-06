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
 *	AbstractBaseToc
 *	...
 */
AbstractBaseToc = function( ) { };

AbstractBaseToc.prototype = Object.extend( new AbstractUIComponent( ),
{
	__neh_click_closure : null,
	__neh_resize_closure : null,
	__beh_toc_closure : null,
	__beh_toc_image_closure : null,
	__neh_img_click_closure : null,
	
	__nodeid : '0',
	__neh_item_mouse_over : null,
	__neh_item_mouse_out  : null,
	__neh_item_click : null,
	
	__clickcount : 0,
		
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, TOC id (optional since there is only one nav bar)
	 *	@return, void
	 */
	__cb_installEventHandlers : function( )
	{
		var oImgs = this.__instance.getElementsByTagName( "img" );
		if ( oImgs )
		{
			for ( var i = 0; i < oImgs.length; i++ )
			{
				Event.observe( oImgs[i], 'click', this.__neh_click_closure, false );
			}
		}
		// Birt event handler
		Event.observe( window, 'resize', this.__neh_resize_closure, false );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_TOC, this.__instance.id, this.__beh_toc_closure );				
		birtEventDispatcher.registerEventHandler( birtEvent.__E_TOC_IMAGE_CLICK, this.__instance.id, this.__beh_toc_image_closure );
	},
	
	/**
	 *	Handle mouse over.
	 */
	__neh_mousemove : function ( event )
	{
		var obj = Event.element( event );
		obj.style.filter = 'alpha( opacity=80)';
		obj.style.opacity = 0.8;
		obj.style.MozOpacity = 0.8;
	},
	
	/**
	 *	Handle mouse out.
	 */
	__neh_mouseout  : function ( event )
	{
		var obj = Event.element( event );
		obj.style.filter = 'alpha( opacity=100)';
		obj.style.opacity = 1;
		obj.style.MozOpacity = 1;		
	},
	
	/**
	 *	Binding data to the TOC UI. Data includes a TOC tree.
	 *
	 *	@data, data DOM tree (Schema TBD)
	 *	@return, void
	 */
	__cb_bind : function( data )
	{		
		var datas = data.getElementsByTagName( 'Child' );
		var len = datas.length;
		if( len == 0 )
		{
			return ;
		}
		
		var tableEle = document.createElement( "table" );
		tableEle.border = '0';
		tableEle.cellspacing = '0';
		tableEle.cellpadding = '0';
		var tbody = document.createElement( "tbody" );
		for( i = 0; i < len; i++ )
		{
			var imgid = this.__nodeid + '_' + i ;
			var tr1 = document.createElement( "tr" );
			var td1 = document.createElement( "td" );
			td1.valign = "top";
			td1.id = "td" + imgid ;

			var tmp = datas[i];

			var isLeafs = tmp.getElementsByTagName( 'IsLeaf' );
			var img = document.createElement( "img" );
			img.style.backgroundRepeat = 'no-repeat';
			img.style.paddingLeft = '0px';
			img.style.width = '8px';
			img.style.height = '8px';
			img.plusMinus = '+';	//default it is collapsed
			img.id = imgid;
			img.query = '0';		//default it needs to communicate with the server.
			
			if ( isLeafs[0].firstChild.data == "false" )
			{
				img.src = "birt/images/Expand.gif" ;
				img.style.cursor = 'pointer';
				Event.observe( img, 'click', this.__neh_img_click_closure, false );
			}
			else
			{
				img.src = "birt/images/Leaf.gif" ;
				img.style.cursor = 'default';
			}

			var nodeIds = tmp.getElementsByTagName( 'Id' );
			img.nodeId = nodeIds[0].firstChild.data;
			
			var bookmarks = tmp.getElementsByTagName( 'Bookmark' );
			img.bookmark = bookmarks[0].firstChild.data;
			td1.appendChild( img );
			td1.appendChild( document.createTextNode( " " ) );

			var spantmp = document.createElement( "input" );
			var displaynames = tmp.getElementsByTagName( 'DisplayName' );
			var displayname = displaynames[0].firstChild;
			var s_displayname = "";
			if( displayname )
				s_displayname = displayname.data;
				
			spantmp.type = "text";
			spantmp.readOnly = true;			
			spantmp.value = s_displayname;
			spantmp.title = "TOC Link " + s_displayname;
			spantmp.id =  'span_' + imgid;			
			if( s_displayname.length > 0 )			
				spantmp.size = s_displayname.length;	
			
			var cssText = "cursor:pointer;border:0px;font-family:Verdana;font-size:9pt;background-color:#FFFFFF;overflow:visible;";			
			var styles = tmp.getElementsByTagName( 'Style' );
			if( styles && styles.length > 0 )
			{
				if( styles[0].firstChild )
					spantmp.style.cssText = cssText + styles[0].firstChild.data;
				else
					spantmp.style.cssText = cssText;							
			}							
			td1.appendChild( spantmp );
			td1.noWrap = true;
			
			tr1.appendChild( td1 );
			
			var tr2 = document.createElement( "tr" );
			var td2 = document.createElement( "td" );
			td2.id = 'display' + imgid;
			td2.style.paddingLeft = '16px';
			td2.style.display = 'none';
			tr2.appendChild( td2 );
			
			tbody.appendChild( tr1 );
			tbody.appendChild( tr2 );
			
			//observe the text so that when click the text ,we can expand or collapse the toc
			Event.observe( spantmp , 'mouseover', this.__neh_item_mouse_over, false );
			Event.observe( spantmp , 'mouseout', this.__neh_item_mouse_out, false );	
			Event.observe( spantmp , 'click', this.__neh_item_click, false );
			Event.observe( spantmp , 'keydown', this.__neh_item_click, false );
		}
		tableEle.appendChild( tbody );
		var displayid = 'display' + this.__nodeid;
		var ele= $( displayid );
		var childLength = ele.childNodes.length;
		if ( childLength == 0 )
		{
			ele.appendChild( tableEle );
		}
		else
		{
			ele.replaceChild( tableEle, ele.childNodes[0] );
		}
		
		this.__neh_resize( ); // hack
	},
		
	/**
	 *	when click the text ,also need to collapse or expand the toc
	 */
	__neh_text_click : function ( event )
	{
		if( event.type == 'keydown' )
		{
			// Press "Enter" and "Space"
			if( event.keyCode != 13 && event.keyCode != 32)
				return;					
		}
		var clickText = Event.element( event );
		var clickId = clickText.id;
		//as the clicktextid is 'span_' + id, so we need to substr to get the imgid
		var spanlength = "span_".length;
		var imgid = clickId.substr( spanlength );
		var clickImg = $( imgid );
		var query = clickImg.query;
		var plusMinus = clickImg.plusMinus;
		var bookmark = clickImg.bookmark;
		
		var params = new Array( );
		params[0] = { };
		params[0].name = Constants.PARAM_BOOKMARK;
		params[0].value = bookmark;
		
		// passed bookmark name is not a TOC name.
		params[1] = { };
		params[1].name = Constants.PARAM_ISTOC;
		params[1].value = "false";
		
		birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, params );
		Event.stop(event);
	},
	
	/**
	 *	when click the img , collpase or expand the toc 
	 */
	__neh_img_click : function( event )
	{
		var clickImg = Event.element( event );
		var query = clickImg.query;
		var nodeId = clickImg.nodeId;
		var clickId = this.__neh_click_process( clickImg, 0 );
		this.__neh_click_broadcast( query, nodeId );
	},
	
	/**
	 *	Process click.
	 */
	__neh_click_process : function ( clickImg, textClick )
	{
		var clickId = clickImg.id;
		this.__nodeid = clickId;
		var plusMinus = clickImg.plusMinus;
		var query = clickImg.query;
		if( textClick == 1 && plusMinus != '+' )
		{
		}
		else
		{
			clickImg.src = this.__icon_change( plusMinus );
		}
		
		clickImg.plusMinus = this.__state_change( plusMinus );
		var displayid = 'display' + clickId;
		var elem = $( displayid );
		if( ( textClick == 1 && plusMinus == '+' ) || textClick == 0 )
		{
			if( clickImg.plusMinus == '+' )
			{
				//collapse
				elem.style.display = 'none';
			}
			else
			{
				//expand
				elem.style.display = '';
			}
		}
		
		if( query == '0' )
		{
			clickImg.query = '1';
		}
		
		return clickId;
	},
	
	/**
	 *	Broadcast event.
	 */
	__neh_click_broadcast : function( query, realId )
	{
		birtSoapRequest.setURL( soapURL );
		if( query == '0' )
		{
			birtSoapRequest.addOperation( Constants.documentId, Constants.Document,
										  "GetToc", null,
										  { name : "realId", value : realId } );
			birtEventDispatcher.broadcastEvent( birtEvent.__E_TOC_IMAGE_CLICK );
		}
	},
	
	/*
	 *	change the img plusMinus
	 *	+ to -
	 *	- to +
	 *	0 to 0
	*/
	__state_change : function ( plusMinus )
	{
		var stat = '+';
		if( plusMinus == '+' )
		{
			stat = '-';
		}
		else if( plusMinus == '-' )
		{
			stat = '+';
		}
		else if( plusMinus == '0' )
		{
			stat = '0';
		}
		return stat;
	},
	
	/**
	 *	change the img icon
	 */
	__icon_change : function ( plusMinus )
	{
		var srcLoc = "birt/images/Expand.gif";
		if( plusMinus == '+' )
		{
			srcLoc = "birt/images/Collapse.gif";
		}
		else if( plusMinus == '-' )
		{
			srcLoc = "birt/images/Expand.gif";
		}
		
		return srcLoc;
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_resize : function( event )
	{
		//var width = BirtPosition.viewportWidth( ) -  ( this.__instance.offsetLeft >= 250 ? 250 : 0 ) - 3;
		//this.__instance.style.width = width + "px";
		var height = BirtPosition.viewportHeight( ) - this.__instance.offsetTop - 2;
		this.__instance.style.height = height + "px";
	},

	/**
	 *	what is this?
	 */
	__beh_toc_image : function (  )
	{
		return true;
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click: function( event )
	{
		var clickId = Event.element( event ).id;
		var clickName = Event.element( event ).name;
		
		if ( Event.element( event ).name == 'close' )
		{
			birtEventDispatcher.broadcastEvent( birtEvent.__E_TOC );
		}
		
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__beh_toc: function( id )
	{
		Element.toggle( this.__instance );

		this.__nodeid = '0';
		this.__clickcount = 0;
		var displayid = 'display' + this.__nodeid;
		
		var root = $( displayid );
		if( root.query != 1 )
		{
			root.query = '1';
			birtSoapRequest.setURL( soapURL );
			birtSoapRequest.addOperation( Constants.documentId,  Constants.Document, "GetToc", null );
			return true;
		}
		else
		{
			root.query = '0';
		}
	}
} );