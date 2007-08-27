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
AbstractParameterDialog = function( ) { };

AbstractParameterDialog.prototype = Object.extend( new AbstractBaseDialog( ),
{
    /**
     *__parameter to store "name" and "value" pairs 
     */
     __parameter : [],
         
    /**
     *__cascadingParameter to store "name" and "value" pairs
     */
     __cascadingParameter : [],

    /**
	 *	Event handler closures.
	 */
	 __neh_click_radio_closure : null,
	 __neh_change_select_closure : null,

	/**
	 *	Initialize dialog base.
	 *	@return, void
	 */
	initializeBase : function( id )
	{
		this.__initBase( id, '500px' );
		this.__z_index = 200;
		
	    this.__neh_click_radio_closure = this.__neh_click_radio.bindAsEventListener( this );
	    this.__neh_change_select_closure = this.__neh_change_select.bindAsEventListener( this );

	    this.__local_installEventHandlers(id);
	},
	
	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind : function( data )
	{
		this.__propogateCascadeParameter( data );
	},

	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__propogateCascadeParameter : function( data )
	{
		if( data )
		{
			var cascade_param = data.getElementsByTagName( 'CascadeParameter' )[0];//assume there is only one cascadeparameter
			var selectionLists = data.getElementsByTagName( 'SelectionList' );
			if ( !selectionLists )
			{
				return;
			}
			
			for ( var k = 0; k < selectionLists.length; k++ )
			{
				var param_name = selectionLists[k].getElementsByTagName( 'Name' )[0].firstChild.data;
				var selections = selectionLists[k].getElementsByTagName( 'Selections' );
				
				var append_selection = document.getElementById( param_name + "_selection" );
				var len = append_selection.options.length;
								
				// Clear our selection list.
				for( var i = 0, index = 0; i < len; i++ )
				{
					/*
					if ( append_selection.options[index].value == "" )
					{
						index++;
						continue;
					}
					*/
					append_selection.remove( index );
				}
				
				// Add new options based on server response.
				for( var i = 0; i < selections.length; i++ )
				{
					if ( !selections[i].firstChild )
					{
						continue;
					}
	
					var oOption = document.createElement( "OPTION" );
					var oLabel = selections[i].getElementsByTagName( 'Label' )[0].firstChild;
					if( oLabel )
						oOption.text = oLabel.data;
					else
						oOption.text = "";

					var oValue = selections[i].getElementsByTagName( 'Value' )[0].firstChild;
					if( oValue )
						oOption.value = oValue.data;
					else
						oOption.value = "";
					append_selection.options.add( oOption );
				}
			}
		}
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__local_installEventHandlers : function( id )
	{
		//install UIComponent native handler
		var oTBC = document.getElementById("parameter_table").getElementsByTagName( 'TABLE' );
		for( var k = 0, counter = 0; k < oTBC.length; k++ )
		{
		    var temp = oTBC[k].getElementsByTagName( 'TABLE' );
		    if( !temp.length )
		    {
		        //install select event handler in cascade parameters
		        this.__install_cascade_parameter_event_handler( oTBC[k], counter++ );
		    }
		}
		
		var oTRC = document.getElementById( "parameter_table" ).getElementsByTagName( "TR" );
		for( var i = 0; i < oTRC.length; i++ )
		{
			var oInput = oTRC[i].getElementsByTagName( "input" );
			var oTable = oTRC[i].getElementsByTagName( "table" );
			var oSelect = oTRC[i].getElementsByTagName( "select" );
			if( oTable.length > 0 )
			{
				continue;
			}
			//find radio with textbox or select items to install event listener.
			var flag = false;
			for( var j = 0; j < oInput.length; j++ )
			{
				if( oInput[j].type == "radio" && !flag )
				{
					var tempRadio = oInput[j];
					flag = true;
					continue;
				}
	  
				if( oInput[j].type == "radio" && tempRadio != {} && oInput[j].id != tempRadio.id )
				{
					Event.observe( tempRadio, 'click', this.__neh_click_radio_closure, false );
					Event.observe( oInput[j], 'click', this.__neh_click_radio_closure, false );
				}
			}
		}
	},
	
	/**
	 *	Intall the event handlers for cascade parameter.
	 *
	 *	@table_param, container table object.
	 *	@counter, index of possible cascade parameter.
	 *	@return, void
	 */
	__install_cascade_parameter_event_handler : function( table_param, counter )
	{
		var oSC = table_param.getElementsByTagName( "select" );
		var matrix = new Array( );
		var m = 0;
		
		var oTRC = table_param.getElementsByTagName( "TR" );
		for( var i = 0; i < oTRC.length; i++ )
		{
			var oSelect = oTRC[i].getElementsByTagName( "select" );
			
			// find select items to install event listener
			if( oSelect.length > 0 )
			{
				Event.observe( oSelect[0], 'change', this.__neh_change_select_closure, false );
				if( !matrix[m] )
				{
					matrix[m] = {};
				}
				matrix[m].name = oSelect[0].id.substr( 0, oSelect[0].id.length - 10 );
				matrix[m++].value = oSelect[0].value;
			}
		}
		
		this.__cascadingParameter[counter] = matrix;
	},
	
	/**
	 *	Collect parameters, include five cases appear in Birt1.0 Viewer.
	 *
	 *	@return, void
	 */
	collect_parameter : function( )
	{
		var k = 0;
		//oTRC[i] is <tr></tr> section
		var oTRC = document.getElementById( "parameter_table" ).getElementsByTagName( "TR" );
		for( var i = 0; i < oTRC.length; i++ )
		{
			if( !this.__parameter[k] )
			{
				this.__parameter[k] = { };
			}
			//input element collection
			var oIEC = oTRC[i].getElementsByTagName( "input" );
			//select element collection
			var oSEC = oTRC[i].getElementsByTagName( "select" );
			//avoid group parameter
			var oTable = oTRC[i].getElementsByTagName( "table" );
			if( oTable.length > 0 || ( oSEC.length == 0 && oIEC.length == 0 ) || ( oIEC.length == 1 && oIEC[0].type == 'submit' ) )
			{
				continue;
			}
			
			if( oSEC.length == 1 && oIEC.length <= 1 )
			{
				// deal with "select" parameter
				if( oIEC.length == 1 )
				{
					this.__parameter[k].name = oIEC[0].name;
				}
				else
				{
					this.__parameter[k].name = oSEC[0].name;
				}
				this.__parameter[k].value = oSEC[0].options[oSEC[0].selectedIndex].value;
				k++;
			}
			
			if( oSEC.length == 0 && ( oIEC.length == 2 || oIEC.length == 1 ) )
			{
				var temp = {};
				if( oIEC.length == 1 )
				{
					temp = oIEC[0];
				}
				else if( oIEC[0].type == 'hidden' )
				{
					temp = oIEC[1];
				}
				else
				{
					continue;
				}
				
				if( temp.type == 'text' || temp.type == 'password' )
				{
					// deal with "text" parameter
					this.__parameter[k].name = temp.name;
					// if the parameter neither has a value nor a default value, error
					if( temp.value == "" )
					{
						if( temp.defaultValue == "" )
						{
							alert( birtUtility.formatMessage( Constants.error.parameterRequired, temp.name ) );
							return false;
						}
						this.__parameter[k].value = temp.defaultValue;
					}
					else
					{
						this.__parameter[k].value = temp.value;
					}
					k++;
				}
				else if( temp.type == 'checkbox' )
				{
					// deal with checkbox
					this.__parameter[k].name = temp.value;
					temp.checked?this.__parameter[k].value = 'true':this.__parameter[k].value = 'false';  
					k++;
				}
				else
				{
					//handle more cases
				}
			}
			else if( oSEC.length <= 1 && oIEC.length > 2 )
			{
				for( var j = 0; j < oIEC.length; j++ )
				{
					// deal with radio
					if( oIEC[j].type == 'radio' && oIEC[j].checked )
					{
						if( oIEC[j+1] && ( oIEC[j+1].type == 'text' || oIEC[j+1].type == 'password' ) )
						{
							// deal with radio box with textarea or password area
							if( oIEC[j+1].name && oIEC[j+1].value )
							{
								this.__parameter[k].name = oIEC[j+1].name
								this.__parameter[k].value = oIEC[j+1].value;
								k++;
							}
							else
							{
								this.__parameter[k].name = oIEC[j].value
								this.__parameter[k].value = oIEC[j+1].value;
								oIEC[j+1].value = "";
								k++;	            
							}
						}
						else if( oSEC[0] )
						{
							// deal with "select" parameter
							this.__parameter[k].name = oIEC[j].value;
							
							if ( oSEC[0].selectedIndex == -1 )
							{
								alert( birtUtility.formatMessage( Constants.error.parameterRequired, oIEC[j].value ) );
								return false;
							}
							else
							{
								this.__parameter[k].value = oSEC[0].options[oSEC[0].selectedIndex].value;
							}
							
							k++;
						}
						else if( !oIEC[j+1] && !oIEC[j].name )
						{
							//deal with common radio
							this.__parameter[k].name = oIEC[j].value;
							this.__parameter[k].value = "NULL";
							k++;	            
						}
						else
						{
							//deal with common radio
							this.__parameter[k].name = oIEC[j].name;
							this.__parameter[k].value = oIEC[j].value;
							k++;
						}
					}
				}
			}
		}
		return true;
	},
	
	
	/**
	 *	Handle clicking on ok.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress : function( )
	{
		if( birtParameterDialog.collect_parameter( ) )
		{
			birtEventDispatcher.broadcastEvent( birtEvent.__E_CHANGE_PARAMETER );
			this.__l_hide( );
		}
	},

	/**
	 *	Handle clicking on radio.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */	
	__neh_click_radio : function( event )
	{
		var temp = Event.element( event );
		var oInput = temp.parentNode.getElementsByTagName( "input" );
		var oSelect = temp.parentNode.getElementsByTagName( "select" );
					
		for( var i = 0; i < oInput.length; i++ )
		{
			if( oInput[i].id == temp.id )
			{
				//enable the next component
				oInput[i].checked = true;
				if( oInput[i+1] && ( oInput[i+1].type == "text" || oInput[i+1].type == "password" ) )
				{
					oInput[i+1].disabled = false;
					oInput[i+1].focus( );
				}
				else if( oSelect[0] )
				{
					oSelect[0].disabled = false;
					oSelect[0].focus( );
				}
			}
			else if( oInput[i].type == "radio" && oInput[i].id != temp.id )
			{
				//disable the next component and clear the radio
				oInput[i].checked = false;
				if( oInput[i+1] && ( oInput[i+1].type == "text" || oInput[i+1].type == "password" ) )
				{
					oInput[i+1].disabled = true;
				}
				else if( oSelect[0] )
				{
					oSelect[0].disabled = true;
				}
		    }
		}
	},
	
	/**
	 *	Handle change event when clicking on select.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_change_select : function( event )
	{
	    var matrix = new Array( );
	    var m = 0;
        for( var i = 0; i < this.__cascadingParameter.length; i++ )
        {
            for( var j = 0; j < this.__cascadingParameter[i].length; j++ )
            {
                if( this.__cascadingParameter[i][j].name == Event.element( event ).id.substr( 0, Event.element( event ).id.length - 10 ) )
                {
                    this.__cascadingParameter[i][j].value = Event.element( event ).options[Event.element( event ).selectedIndex].value;
                    for( var m = 0; m <= j; m++ )
                    {
					    if( !matrix[m] )
				        {
				            matrix[m] = {};
				        }
				        matrix[m].name = this.__cascadingParameter[i][m].name;
				        matrix[m].value = this.__cascadingParameter[i][m].value;
				    }                    
                    birtEventDispatcher.broadcastEvent( birtEvent.__E_CASCADING_PARAMETER, matrix );
                }
            }
        }
	}
} );