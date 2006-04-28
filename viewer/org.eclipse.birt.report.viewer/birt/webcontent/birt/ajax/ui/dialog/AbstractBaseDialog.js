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
 *	Dialog base class
 */
AbstractBaseDialog = function(){};

AbstractBaseDialog.prototype =
{		
	contentHolderWidth: 500, //TODO - move to display constants? Default width in pixels
	visible: null, //Is the dialog currently visible		
	
	 
	/**
	 Initialize dialog base
	 */
	__initBase: function(htmlId, contentWidth)
	{
		this.__instance = $(htmlId);
		this.htmlId = htmlId;
		this.visisble = false;
		
			//Instance is given a location within screen to avoid
			//extra scroll bar creation
		this.__instance.style.top = '0px';
		this.__instance.style.left = '0px';
		
		//Sizing
		this.contentHolderName = htmlId + "dialogContentContainer";
		if(contentWidth)
		{
			this.contentHolderWidth = parseInt(contentWidth);
		}	
		
		this.__neh_resize_closure = this.__neh_resize.bindAsEventListener( this );
		
		// Initialize event handler closures	
		this.__neh_okay_closure = this.__neh_okay.bind(this);
		this.__neh_cancel_closure = this.__neh_cancel.bind(this);
		this.mousedown_closure = this.__neh_mousedown.bindAsEventListener(this);
		this.mouseup_closure = this.__neh_mouseup.bindAsEventListener(this);
		this.drag_closure = this.__neh_drag.bindAsEventListener(this);
		
		// Initialize shared events	
		this.__base_installEventHandlers(htmlId);	
	},
	
	/**
	Install event handlers shared across all dialogs.
	Buttons (close, cancel, ok), move dialog (drag and drop), screen resize.
	*/
	__base_installEventHandlers : function( id )
	{
		//Initialize iframe
		this.__iframe = $(id + "iframe");
		
		// Close button
		var closeBtn = $(id + "dialogCloseBtn");
		Event.observe( closeBtn, 'click', this.__neh_cancel_closure, false );
		Event.observe( closeBtn, 'mousedown', this.__neh_stopEvent.bindAsEventListener(this), false );
		
		// OK and Cancel buttons
		//TODO change to get by id
		var oInputs = this.__instance.getElementsByTagName( 'input' );
		Event.observe( oInputs[oInputs.length - 2], 'click', this.__neh_okay_closure , false );
		Event.observe( oInputs[oInputs.length - 1], 'click', this.__neh_cancel_closure , false );
		
			//Drag and Drop
		this.dragBarName = id + "dialogTitleBar";
		var dragArea = $(this.dragBarName);	
		Event.observe(dragArea, 'mousedown', this.mousedown_closure, false);
	},
	
	
	/**
	 *	Binding data to the dialog UI.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__cb_bind : function( data )
	{
		this.__bind( data );
			
		this.__l_show( );
	},
	
	/**
	ABSTRACT - must be implemented by extending class
	Gets xml data before dialog is shown
	*/
	__bind: function(data)
	{
	
	},
	
	/**
	Trigger dialog from client (bypasses bind step)
	*/
	showDialog: function()
	{
		this.__l_show( );
	},
	
	/**
	 *	
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__l_show : function( )
	{	
		this.__preShow();
			
				//check if the dialog is already shown
		if(!this.visible)
		{
			var zIndex = Mask.show(); 
			debug("showing at zIndex " + zIndex);
			this.__instance.style.zIndex = zIndex;
					
			Element.show( this.__instance );
			this.visible = true;
			
			//workaround for Mozilla bug https://bugzilla.mozilla.org/show_bug.cgi?id=167801
			if(BrowserUtility.useIFrame())
			{
				//show iframe under dialog
				Element.show( this.__iframe );
			}
			
			this.__setWidth();
				
			BirtPosition.center( this.__instance );
			
			Event.observe( window, 'resize', this.__neh_resize_closure, false );
		}
		
		this.__postShow();	
	},
	

	/**
	Called right before element is shown
	*/
	__preShow: function()
	{
		//implementation is left to extending class
	},
	
	/**
	Called after element is shown
	*/
	__postShow: function()
	{
		//implementation is left to extending class
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__l_hide : function( )
	{
		this.__preHide();
		Event.stopObserving( window, 'resize', this.__neh_resize_closure, false );
		Element.hide( this.__instance, this.__iframe );
		this.visible = false;
		Mask.hide();
	},
		
	/**
	Called before element is hidden
	*/
	__preHide: function()
	{
		//implementation is left to extending clas
	},
	
	/**
	Stop event
	*/
	__neh_stopEvent: function(event)
	{
		Event.stop(event);
	},
	
	/**
	Handle mouse down
	*/
	__neh_mousedown: function(event)
	{
		debug("AbstractBaseDialog __neh_mousedown");
		
		//Event.stop(event);
		var target = Event.element( event );
		
		Event.observe( target, 'mouseup', this.mouseup_closure , false );
		Event.observe( target, 'mousemove', this.drag_closure , false );
	},
	
	/**
	Handle mouse up
	*/
	__neh_mouseup: function(event)
	{
		var target = Event.element( event );

		Event.stopObserving( target, 'mouseup',  this.mouseup_closure , false );
		Event.stopObserving( target, 'mousemove', this.drag_closure , false );
	},
	
	/**
	Handle mousemove 
	*/
	__neh_drag: function(event)
	{
		debug("Mouse move");
		
		Event.stop( event );

		var target = Event.element( event );
		Event.stopObserving( target, 'mouseup',  this.mouseup_closure , false );
		Event.stopObserving( target, 'mousemove', this.drag_closure , false );
					
		DragDrop.startDrag(this.__instance, event, null);	
	},
	
	/**
	 *	Handle native event 'resize'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_resize : function( event )
	{
		BirtPosition.center( this.__instance );
	},
	
	__neh_cancel: function()
	{
		this.__l_hide( );
	},
	
	__neh_okay: function()
	{
		this.__okPress( );
	},

	/**
	 ABSTRACT - Handle clicking on ok.
	*/
	__okPress: function( )
	{		
		//ABSTRACT - needs to be implemented by extending class
		this.__l_hide( );
	},

	//TODO change so called once
	__setWidth: function()
	{	
		var contentHolder = $(this.contentHolderName);
		var innerWidth = contentHolder.offsetWidth;
		var outerWidth = this.__instance.offsetWidth;
		var difference = outerWidth - innerWidth;
		var newOuterWidth = this.contentHolderWidth + difference;
		this.__instance.style.width = newOuterWidth + 'px';
		contentHolder.style.width = this.contentHolderWidth + 'px';
		
		this.__iframe.style.width = this.__instance.offsetWidth + 'px';
		this.__iframe.style.height = this.__instance.offsetHeight + 'px';
	},
	
	/**
	@returns html id attribute of associated html element for this dialog
	*/
	getHtmlId: function()
	{
		return this.htmlId;
	}
}