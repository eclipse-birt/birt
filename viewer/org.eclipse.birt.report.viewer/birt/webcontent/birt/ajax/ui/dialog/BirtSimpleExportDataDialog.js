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
 *	Birt export report dialog.
 */
BirtSimpleExportDataDialog = Class.create( );

BirtSimpleExportDataDialog.prototype = Object.extend( new AbstractBaseDialog( ),
{
	__neh_select_change_closure : null,
	__neh_switchResultSet_closure : null,
	
	availableResultSets : [],
	selectedColumns : [],

	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__initBase( id );
		this.__z_index = 200;
		
		// Closures
		this.__neh_switchResultSet_closure = this.__neh_switchResultSet.bindAsEventListener( this );
		this.__neh_click_exchange_closure = this.__neh_click_exchange.bindAsEventListener( this );
		this.__neh_dblclick_src_closure = this.__neh_dblclick_src.bindAsEventListener( this );
		this.__neh_dblclick_dest_closure = this.__neh_dblclick_dest.bindAsEventListener( this );
		
		this.__installEventHandlers( id );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		Event.observe( oSelects[0], 'change', this.__neh_switchResultSet_closure, false );
		
		// Initialize exchange buttons
		var oInputs = this.__instance.getElementsByTagName( 'input' );
		for ( var i = 0; i < oInputs.length ; i++ )
		{
			Event.observe( oInputs[i], 'click', this.__neh_click_exchange_closure, false );
		}
		
		// Initialize exchange selects
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		if( oSelects.length > 2 )
		{
			Event.observe( oSelects[1], 'dblclick', this.__neh_dblclick_src_closure, false );
			Event.observe( oSelects[2], 'dblclick', this.__neh_dblclick_dest_closure, false );
		}
	},
	
	/**
	 *	Native event handler for selection item movement.
	 */
	__neh_click_exchange : function( event )
	{
		var oInputs = this.__instance.getElementsByTagName( 'input' );
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		
		switch ( Event.element( event ).name )
		{
			case 'Addall':
			{
				if ( oSelects[1].options.length  > 0 )
				{
					this.moveAllItems( oSelects[1], oSelects[2] );
				}
				break;
			}
			case 'Add':
			{
				if ( oSelects[1].options.length  > 0 )
				{
					this.moveSingleItem( oSelects[1], oSelects[2] );
				}
				break;
			}
			case 'Remove':
			{
				if ( oSelects[2].options.length  > 0 )
				{
					this.moveSingleItem( oSelects[2], oSelects[1] );
				}
				break;
			}
			case 'Removeall':
			{
				if ( oSelects[2].options.length  > 0 )
				{
					this.moveAllItems( oSelects[2], oSelects[1] );
				}
				break;
			}
		}
		
		this.__updateButtons( );
	},

	/**
	 *	Native event handler for double click source select element.
	 */
	__neh_dblclick_src : function( event )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		
		if ( oSelects[1].options.length  > 0 )
		{
			this.moveSingleItem( oSelects[1], oSelects[2] );
		}
		
		this.__updateButtons( );
	},

	/**
	 *	Native event handler for double click dest select element.
	 */
	__neh_dblclick_dest : function( event )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		
		if ( oSelects[2].options.length  > 0 )
		{
			this.moveSingleItem( oSelects[2], oSelects[1] );
		}
		
		this.__updateButtons( );
	},
		
	/**
	 *	Update button status.
	 */
	__updateButtons : function( )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		var canExport = oSelects[0].options.length > 0;
		var canAdd = oSelects[1].options.length > 0;
		var canRemove = oSelects[2].options.length  > 0;

		var oInputs = this.__instance.getElementsByTagName( 'input' );
		
		oInputs[0].src = canAdd ? "birt/images/AddAll.gif" : "birt/images/AddAll_disabled.gif";
		oInputs[0].style.cursor = canAdd ? "pointer" : "default";
		
		oInputs[1].src = canAdd ? "birt/images/Add.gif" : "birt/images/Add_disabled.gif";
		oInputs[1].style.cursor = canAdd ? "pointer" : "default";
		
		oInputs[2].src = canRemove ? "birt/images/Remove.gif" : "birt/images/Remove_disabled.gif";
		oInputs[2].style.cursor = canRemove ? "pointer" : "default";

		oInputs[3].src = canRemove ? "birt/images/RemoveAll.gif" : "birt/images/RemoveAll_disabled.gif";
		oInputs[3].style.cursor = canRemove ? "pointer" : "default";
		
		oInputs[4].disabled = canExport ? false : true;
	},
	
	/**
	 *	Move single selection item.
	 */
	moveSingleItem : function( sel_source, sel_dest )
	{
		if ( sel_source.selectedIndex == -1 )
		{
			return;
		}
		
		for ( var i=0; i<sel_source.options.length; i++ )
		{
			if ( sel_source.options[i].selected )
			{
				var selectedItem = sel_source.options[i];
				sel_dest.options.add( new Option( selectedItem.text, selectedItem.value ) );
				sel_source.remove( i );
				i = i - 1;
			}							
		}
		
		sel_source.selectedIndex = 0;
	},
	
	/**
	 *	Move all selection items.
	 */
	moveAllItems : function( sel_source, sel_dest )
	{
   		for ( var i = 0; i < sel_source.length; i++ )
   		{
     		var SelectedText = sel_source.options[i].text;
     		var SelectedValue = sel_source.options[i].value;
	   		var newOption = new Option( SelectedText );
			newOption.value = SelectedValue;
     		sel_dest.options.add( newOption );
   		}
   		
   		sel_dest.selectedIndex = 0;
   		sel_source.length = 0;
	},	

	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	 __bind : function( data )
	 {
	 	if ( !data )
	 	{
	 		return;
	 	}
	 	
	 	var oSelects = this.__instance.getElementsByTagName( 'select' );
		oSelects[0].options.length = 0;
		oSelects[1].options.length = 0;
		oSelects[2].options.length = 0;
		
		this.availableResultSets = [];
	 	
	 	var resultSets = data.getElementsByTagName( 'ResultSet' );
	 	for ( var k = 0; k < resultSets.length; k++ )
	 	{
	 		var resultSet = resultSets[k];
	 		
		 	var queryNames = resultSet.getElementsByTagName( 'QueryName' );
			oSelects[0].options.add( new Option( queryNames[0].firstChild.data ) );
				 	
			this.availableResultSets[k] = {};
			
		 	var columns = resultSet.getElementsByTagName( 'Column' );
		 	for( var i = 0; i < columns.length; i++ )
		 	{
		 		var column = columns[i];
		 		
		 		var columnName = column.getElementsByTagName( 'Name' );
		 		var label = column.getElementsByTagName( 'Label' );
				this.availableResultSets[k][label[0].firstChild.data] = columnName[0].firstChild.data;
		 	}
		}
		
		this.__neh_switchResultSet( );
	 },
	 
	 /**
	  *	switch result set.
	  */
	 __neh_switchResultSet : function( )
	 {
	 	var oSelects = this.__instance.getElementsByTagName( 'select' );
		oSelects[1].options.length = 0;
		oSelects[2].options.length = 0;	
	 	
	 	var columns = this.availableResultSets[oSelects[0].selectedIndex];
	 	for( var label in columns )
	 	{
	 		var colName = columns[label];
			var option = new Option( label );
			option.value = colName;
			oSelects[1].options.add( option );
	 	}
	 	
		this.__updateButtons( );
	 },

	/**
	 *	Handle clicking on ok.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress : function( )
	{
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		this.__l_hide( );
		if ( oSelects[2].options.length > 0 )
		{
			for( var i = 0; i < oSelects[2].options.length; i++ )
			{
				this.selectedColumns[i] = oSelects[2].options[i].value;
			}
			
			this.__constructForm( );
		}
	},
	
	/**
	 *	Construct extract data form. Post it to server.
	 */
	__constructForm : function( )
	{
		var dialogContent = $( 'simpleExportDialogBody' );
		var hiddenDiv = document.createElement( 'div' );
		hiddenDiv.style.display = 'none';

		var hiddenForm = document.createElement( 'form' );
		hiddenForm.method = 'post';
		hiddenForm.target = '_self';
		var url = document.location.href;
		url = url.replace( /[\/][a-zA-Z]+[?]/, '/download?' );
		hiddenForm.action = url;
		
		// Pass over current element's iid.
		var queryNameInput = document.createElement( 'input' );
		queryNameInput.type = 'hidden';
		queryNameInput.name = "ResultSetName";
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		queryNameInput.value = oSelects[0].options[oSelects[0].selectedIndex].text;
		hiddenForm.appendChild( queryNameInput );

		// Total # of selected columns.
		if ( this.selectedColumns.length > 0 )
		{
			var hiddenSelectedColumnNumber = document.createElement( 'input' );
			hiddenSelectedColumnNumber.type = 'hidden';
			hiddenSelectedColumnNumber.name = "SelectedColumnNumber";
			hiddenSelectedColumnNumber.value = this.selectedColumns.length;
			hiddenForm.appendChild( hiddenSelectedColumnNumber );

			// data of selected columns.
			for( var i = 0; i < this.selectedColumns.length; i++ )
			{
				var hiddenSelectedColumns = document.createElement( 'input' );
				hiddenSelectedColumns.type = 'hidden';
				hiddenSelectedColumns.name = "SelectedColumn" + i;
				hiddenSelectedColumns.value = this.selectedColumns[i];
				hiddenForm.appendChild( hiddenSelectedColumns );
			}
		}
		
		this.selectedColumns = [];
		
		var tmpSubmit = document.createElement( 'input' );
		tmpSubmit.type = 'submit';
		tmpSubmit.value = 'TmpSubmit';
		hiddenForm.appendChild( tmpSubmit );
		
		hiddenDiv.appendChild( hiddenForm );
		dialogContent.appendChild( hiddenDiv );
		tmpSubmit.click( );
		dialogContent.removeChild( hiddenDiv );
	},

	/**
	Called right before element is shown
	*/
	__preShow: function()
	{
		// disable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", true );
		
		// disable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", true );
	},
	
	/**
	Called before element is hidden
	*/
	__preHide: function()
	{
		// enable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", false );
		
		// enable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", false );		
	}	
} );