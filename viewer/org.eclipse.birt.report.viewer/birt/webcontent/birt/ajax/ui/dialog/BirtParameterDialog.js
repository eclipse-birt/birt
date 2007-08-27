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
	 *	Parameter dialog working state. Whether embedded inside
	 *	designer dialog.
	 */
	__mode : 'frameset',

	/**
	 *	Identify the parameter is null.
	 */
	__isnull : '__isnull',

	/**
	 *	Identify display text for a null value.
	 */
	__display_null : 'Null Value',
	
	/**
	 *	Prefix that identify the parameter is to set Display Text for "select" parameter
	 */
	__isdisplay : '__isdisplay__',
	
	/**
	 * identify the parameter value is a locale string
	 */
	__islocale : '__islocale',

	/**
	 * Prefix that identify the parameter value is a locale string
	 */	
	__prefix_islocale : '__islocale__',
	
    /**
	 *	Event handler closures.
	 */
	 __neh_change_cascade_text_closure : null,
	 __neh_mouseover_select_closure : null,
	 __neh_mouseout_select_closurre : null,

    /**
	 *	Check if parameter is required or not.
	 */
	__is_parameter_required : null,
	
    /**
	 *	if previous is visible.
	 */	 
	 preVisible: null, 
	 
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	initialize : function( id, mode )
	{
		this.__mode = mode;
		this.preVisible = false;
		
		if ( this.__mode == 'parameter' )
		{
			// Hide dialog title bar if embedded in designer.
			var paramDialogTitleBar = $( id + 'dialogTitleBar' );
			paramDialogTitleBar.style.display = 'none';			
		}

		// Change event for parameter text field
		this.__neh_change_cascade_text_closure = this.__neh_change_cascade_text.bindAsEventListener( this );
				
		// Mouse over event for Select field
		this.__neh_mouseover_select_closure = this.__neh_mouseover_select.bindAsEventListener( this );
		this.__neh_mouseout_select_closure = this.__neh_mouseout_select.bindAsEventListener( this );
			    
	    this.initializeBase( id );
	    this.__local_installEventHandlers_extend( id );
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__local_installEventHandlers_extend : function( id )
	{
		// Observe "keydown" event
		this.keydown_closure = this.__neh_keydown.bindAsEventListener( this );
		Event.observe( $(id), 'keydown', this.keydown_closure, false );
		
		var oSC = document.getElementById( "parameter_table" ).getElementsByTagName( "select" );
		for( var i = 0; i < oSC.length; i++ )
		{
			Event.observe( oSC[i], 'mouseover', this.__neh_mouseover_select_closure, false );
			Event.observe( oSC[i], 'mouseout', this.__neh_mouseout_select_closure, false );
			
			// Set size for multi-value parameter
			if( oSC[i].multiple )
			{
				var scSize = 8;
				var len = oSC[i].options.length;
				if( len < scSize )
					scSize = len;
				
				oSC[i].size = scSize;
			}
		}
	},
	
	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind : function( data )
	{
		if ( !data )
		{
			return;
		}
		
		var cascadeParamObj = data.getElementsByTagName( 'CascadeParameter' );
		var confirmObj = data.getElementsByTagName( 'Confirmation' );
		if ( cascadeParamObj.length > 0 )
		{
			this.__propogateCascadeParameter( data );
		}
		else if ( confirmObj.length > 0 )
		{
			this.__close( );
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
			var oInput = oTRC[i].getElementsByTagName( "input" );
			var oCascadeFlag = "";
			
			if ( oInput && oInput.length > 0 )
			{
				var oLastInput = oInput[oInput.length - 1];
				if ( oLastInput.id == "isCascade" )
					oCascadeFlag = oLastInput.value;
			}
						
			// find select items to install event listener
			if( oSelect.length > 0 && oCascadeFlag == "true" )
			{
				if ( i < oTRC.length - 1 )
				{
					Event.observe( oSelect[0], 'change', this.__neh_change_select_closure, false );
					
					// find text item to instanll event listener
					var oText;
					for( var j = 0; j < oInput.length; j++ )
					{
						if( oInput[j].type == "text" )
						{
							oText = oInput[j];
							break;
						}
					}
					if( oText )
					{
						Event.observe( oText, 'change', this.__neh_change_cascade_text_closure, false );
					}
				}
				
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
	 *	Collect parameters, Support ComboBox/Listbox,Hidden,Radio,TextBox,Checkbox.
	 *
	 *	@return, void
	 */
	collect_parameter : function( )
	{
		// Clear parameter array
		this.__parameter = new Array( );		
				
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
			
			// control type
			var oType = oIEC[0].value;

			// deal with "hidden" parameter
			if( oType == 'hidden' )
			{
				var temp = oIEC[1];
				this.__parameter[k].name = temp.name;
				this.__parameter[k].value = temp.value;
				k++;
				
				// set display text
				if( !this.__parameter[k] )
				{
					this.__parameter[k] = { };
				}
				this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
				this.__parameter[k].value = oIEC[2].value;
				k++;		
				
				continue;
			}
			
			// deal with "checkbox" parameter
			if( oType == 'checkbox' )
			{
				var temp = oIEC[2];
				this.__parameter[k].name = temp.value;
				temp.checked?this.__parameter[k].value = 'true':this.__parameter[k].value = 'false';  
				k++;
				continue;
			}
						
			// deal with "text" parameter
			if( oType == 'text' )
			{
				// allow null
				if( oIEC[1] && oIEC[1].type == 'radio' )
				{
					if( oIEC[1].checked )
					{
						var paramName = oIEC[2].name;
						var paramValue = oIEC[3].value;
						var displayText = oIEC[4].value;
						
						if( displayText != oIEC[2].value )
						{
							// change the text field value,regard as a locale string
							paramValue = oIEC[2].value;
							
							// set isLocale flag							
							this.__parameter[k].name = this.__islocale;
							this.__parameter[k].value = paramName;
							k++;	
						}
						
						// check if required
						if( this.__is_parameter_required( oIEC ) 
							&& birtUtility.trim( paramValue ) == '' && this.visible )
						{
							oIEC[2].focus( );
							alert( birtUtility.formatMessage( Constants.error.parameterRequired, paramName ) );
							return false;
						}
													
						// set parameter value
						if( !this.__parameter[k] )
						{
							this.__parameter[k] = { };
						}
						this.__parameter[k].name = paramName;
						this.__parameter[k].value = paramValue;
						k++;
						
						// set display text
						if( !this.__parameter[k] )
						{
							this.__parameter[k] = { };
						}
						this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
						this.__parameter[k].value = oIEC[2].value;
						k++;						
					}
					else
					{
						// select null value
						this.__parameter[k].name = this.__isnull;
						this.__parameter[k].value = oIEC[1].value;
						k++;
					}										
				}
				// not allow null
				else
				{
					var paramName = oIEC[1].name;
					var paramValue = oIEC[2].value;
					var displayText = oIEC[3].value;
					
					if( displayText != oIEC[1].value )
					{
						// change the text field value,regard as a locale string
						paramValue = oIEC[1].value;
						
						// set isLocale flag							
						this.__parameter[k].name = this.__islocale;
						this.__parameter[k].value = paramName;
						k++;	
					}
					
					// check if required
					if( this.__is_parameter_required( oIEC ) 
						&& birtUtility.trim( paramValue ) == '' && this.visible )
					{
						oIEC[1].focus( );
						alert( birtUtility.formatMessage( Constants.error.parameterRequired, paramName ) );
						return false;
					}
						
					// set parameter value
					if( !this.__parameter[k] )
					{
						this.__parameter[k] = { };
					}
					this.__parameter[k].name = paramName;
					this.__parameter[k].value = paramValue;
					k++;
						
					// set display text
					if( !this.__parameter[k] )
					{
						this.__parameter[k] = { };
					}
					this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
					this.__parameter[k].value = oIEC[1].value;
					k++;					
				}
				
				continue;
			}
			
			// deal with "radio" parameter
			if( oType == 'radio' )
			{
				if( oIEC.length > 1 )
				{
					for( var j = 1; j < oIEC.length; j++ )
					{
						// deal with radio
						if( oIEC[j].type == 'radio' && oIEC[j].checked )
						{
							// null value
							if( oIEC[j].id == oIEC[j].name + "_null" )
							{
								this.__parameter[k].name = this.__isnull;
								this.__parameter[k].value = oIEC[j].name;
								k++;
							}
							else
							{
								// common radio value
								this.__parameter[k].name = oIEC[j].name;
								this.__parameter[k].value = oIEC[j].value;	
								k++;
								
								// set display text for the "radio" parameter
								var displayLabel = document.getElementById( oIEC[j].id + "_label" );
								if( displayLabel )
								{							
									if( !this.__parameter[k] )
									{
										this.__parameter[k] = { };
									}
									this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
									this.__parameter[k].value = displayLabel.title;
									k++;			
								}
							}	
							
							break;								
						}	
					}
				}
								
				continue;		
			}
			
			// deal with "select" parameter
			if( oType == 'select' && oSEC.length == 1 )
			{
				var paramName = oIEC[1].name;
				
				var flag = true;
				if( oIEC[2] && oIEC[2].type == 'radio' && !oIEC[2].checked )
				{
					flag = false;
				}
				
				// check select
				if( flag )
				{
					if ( oSEC[0].selectedIndex < 0 && this.visible )
					{
						oSEC[0].focus( );
						alert( birtUtility.formatMessage( Constants.error.parameterRequired, paramName ) );
						return false;
					}
																									
					if( oSEC[0].multiple )
					{
						if( this.__is_parameter_required( oIEC ) )						
						{
							var options = oSEC[0].options;
							for( var l = 0; l < options.length; l++ )
							{
								if( !options[l].selected )
									continue;
								
								var tempValue = options[l].value;								
								if( birtUtility.trim( tempValue ) == '' && this.visible )
								{
									oSEC[0].focus( );
									alert( birtUtility.formatMessage( Constants.error.parameterRequired, paramName ) );
									return false;									
								}
							}
						}
					}
					else
					{
						var tempText = oSEC[0].options[oSEC[0].selectedIndex].text;
						var tempValue = oSEC[0].options[oSEC[0].selectedIndex].value;
					
						if ( this.__is_parameter_required( oIEC ) && birtUtility.trim( tempValue ) == '' && this.visible )
						{
							oSEC[0].focus( );
							alert( birtUtility.formatMessage( Constants.error.parameterRequired, paramName ) );
							return false;
						}	

						// Check if select 'Null Value' option for single parameter
						if( tempText == this.__display_null && tempValue == '' )
						{
							this.__parameter[k].name = this.__isnull;
							this.__parameter[k].value = paramName;
							k++;	
							continue;
						}
					}					
				}
				
				// allow new value
				if( oIEC[2] && oIEC[2].type == 'radio' )
				{					
					if( oIEC[2].checked )
					{
						// select value
						var tempText = oSEC[0].options[oSEC[0].selectedIndex].text;
						var tempValue = oSEC[0].options[oSEC[0].selectedIndex].value;
						
						// set value
						this.__parameter[k].name = paramName;
						this.__parameter[k].value = tempValue;
						k++;
						
						// set display text
						if( !this.__parameter[k] )
						{
							this.__parameter[k] = { };
						}
						this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
						this.__parameter[k].value = tempText;
						k++;						
					}
					else
					{
						var inputValue = oIEC[4].value;
						var paramValue = oIEC[1].value;
						var displayText = oIEC[5].value;
						
						// if change the text field value or input text field isn't focus default,regard as a locale string 
						if( displayText != inputValue || oIEC[4].name.length <= 0 )
						{							
							paramValue = inputValue;
							
							// set isLocale flag							
							this.__parameter[k].name = this.__islocale;
							this.__parameter[k].value = paramName;
							k++;	
						}
						
						// text value
						if ( this.__is_parameter_required( oIEC ) && birtUtility.trim( paramValue ) == '' && this.visible )
						{
							oIEC[4].focus( );
							alert( birtUtility.formatMessage( Constants.error.parameterRequired, paramName ) );
							return false;
						}						

						// set value
						if( !this.__parameter[k] )
						{
							this.__parameter[k] = { };
						}
						this.__parameter[k].name = paramName;
						this.__parameter[k].value = paramValue;
						k++;
											
						// set display text
						if( !this.__parameter[k] )
						{
							this.__parameter[k] = { };
						}
						this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
						this.__parameter[k].value = inputValue;
						k++;						
					}
				}
				else
				{
					// don't allow new value
					if( oSEC[0].multiple )
					{
						// allow multi value
						var options = oSEC[0].options;
						for( var l = 0; l < options.length; l++ )
						{
							if( !options[l].selected )
								continue;
							
							var tempText = options[l].text;
							var tempValue = options[l].value;
							
							// Check if select 'Null Value' option
							if( tempText == this.__display_null && tempValue == '' )
							{
								if( !this.__parameter[k] )
								{
									this.__parameter[k] = { };
								}
							
								this.__parameter[k].name = this.__isnull;
								this.__parameter[k].value = paramName;
								k++;	
								continue;
							}
					
							// set value
							if( !this.__parameter[k] )
							{
								this.__parameter[k] = { };
							}
							this.__parameter[k].name = paramName;
							this.__parameter[k].value = tempValue;
							k++;
						
							// set display text
							if( !this.__parameter[k] )
							{
								this.__parameter[k] = { };
							}
							this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
							this.__parameter[k].value = tempText;
							k++;
						}
					}
					else
					{
						// allow single value
						var tempText = oSEC[0].options[oSEC[0].selectedIndex].text;
						var tempValue = oSEC[0].options[oSEC[0].selectedIndex].value;
						
						// set value
						this.__parameter[k].name = paramName;
						this.__parameter[k].value = tempValue;
						k++;
						
						// set display text
						if( !this.__parameter[k] )
						{
							this.__parameter[k] = { };
						}
						this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
						this.__parameter[k].value = tempText;
						k++;
					}					
				}
				
				continue;
			}			
		}
		
		return true;
	},

	/**
	 *	Check if current parameter is required or not.
	 *
	 *	@oInputs, Input control collection 
	 *	@return, true or false
	 */
	__is_parameter_required : function( oInputs )
	{
		if( !oInputs || oInputs.length <= 0 )
			return false;
		
		var flag = false;		
		for( var i = 0; i< oInputs.length; i++ )
		{
			// if find defined input control
			if( oInputs[i].id == 'isRequired' && oInputs[i].value == 'true' )
			{
				flag = true;
				break;		
			}
		}
		
		return flag;
	},

	/**
	 *	Handle mouseover event on select.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_mouseover_select : function( event )
	{
		var oSC = Event.element( event );
		var tempText;
		if( oSC.selectedIndex >=0 )
			tempText = oSC.options[oSC.selectedIndex].text;
		
		var hint = document.getElementById( "birt_hint" );	
		if( tempText && hint )
		{
			hint.innerHTML = tempText;
			hint.style.display = "block"; 			
			hint.style.left = ( event.clientX - parseInt( this.__instance.style.left ) ) + "px";
			hint.style.top = ( event.clientY - parseInt( this.__instance.style.top ) ) + "px";
		}			
	},

	/**
	 *	Handle mouseout event on select.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_mouseout_select : function( event )
	{
		var hint = document.getElementById( "birt_hint" );
		if( hint )
		{
			hint.style.display = "none"; 
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
            	var paramName = this.__cascadingParameter[i][j].name;
            	if( paramName == this.__isnull )
            		paramName = this.__cascadingParameter[i][j].value;
            		
                if( paramName == Event.element( event ).id.substr( 0, Event.element( event ).id.length - 10 ) )
                {
                	var tempText = Event.element( event ).options[Event.element( event ).selectedIndex].text;
					var tempValue = Event.element( event ).options[Event.element( event ).selectedIndex].value;
					
                	// Null Value Parameter
                	if( tempText == this.__display_null && tempValue == '' )
                	{
                		this.__cascadingParameter[i][j].name = this.__isnull;
                		this.__cascadingParameter[i][j].value = paramName;						
                	}
                	else
                	{
                		this.__cascadingParameter[i][j].name = paramName;
                	    this.__cascadingParameter[i][j].value = tempValue;
                	}
                	
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
		
		// check if current parameter is cascading parameter
		var oCascadeFlag = "";
		if ( oInput && oInput.length > 0 )
		{
			var oLastInput = oInput[oInput.length - 1];
			if ( oLastInput.id == "isCascade" )
				oCascadeFlag = oLastInput.value;
		}
			
		for( var i = 0; i < oInput.length; i++ )
		{
			if( oInput[i].id == temp.id )
			{
				//enable the next component
				oInput[i].checked = true;
				if( oInput[i+1] && ( oInput[i+1].type == "text" || oInput[i+1].type == "password" ) )
				{
					oInput[i+1].disabled = false;
					// if cascading parameter and not the last one, clear value
					if( oCascadeFlag == "true" && !this.__ifLastSelect( oSelect[0] ) )
						oInput[i+1].value = "";
					oInput[i+1].focus( );
				}
				else if( oSelect[0] )
				{
					oSelect[0].disabled = false;
					// if cascading parameter and not the last one, clear value
					if( oCascadeFlag == "true" && !this.__ifLastSelect( oSelect[0] ) )
						oSelect[0].selectedIndex = -1;
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
	 * Check whether obj is the last select control
	 */
	__ifLastSelect : function( obj )
	{
		if( obj )
		{
			var oTABLE = obj.parentNode.parentNode.parentNode;
			if( oTABLE )
			{
				var oSelect = oTABLE.getElementsByTagName( "select" );
				if( oSelect && oSelect.length > 0 && oSelect[oSelect.length - 1].id == obj.id )
				{
					return true;	
				}
			}
		}
		return false;
	},

	/**
	 *	Handle changing on cascading parameter text field.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_change_cascade_text : function( event )
	{	
		var temp = Event.element( event );
		var paramName = temp.id.substr( 0, temp.id.length - 6 );
						
	    var matrix = new Array( );
	    var m = 0;
        for( var i = 0; i < this.__cascadingParameter.length; i++ )
        {
            for( var j = 0; j < this.__cascadingParameter[i].length; j++ )
            {
                if( this.__cascadingParameter[i][j].name == paramName )
                {
                    this.__cascadingParameter[i][j].value = temp.value;
                    for( var m = 0; m <= j; m++ )
                    {
					    if( !matrix[m] )
				        {
				            matrix[m] = {};
				        }
				        matrix[m].name = this.__prefix_islocale + this.__cascadingParameter[i][m].name;
				        matrix[m].value = this.__cascadingParameter[i][m].value;			
				    }                    
                    birtEventDispatcher.broadcastEvent( birtEvent.__E_CASCADING_PARAMETER, matrix );
                }
            }
        }
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
			var target = Event.element( event );
						
			// Focus on INPUT(exclude 'button' type) and SELECT controls
			if( (target.tagName == "INPUT" && target.type != "button" ) 
					|| target.tagName == "SELECT")
			{
				this.__okPress( );
				Event.stop( event );
			}
		}
	},	
		
	/**
	 *	Handle clicking on okRun.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress : function( )
	{
		if( birtParameterDialog.collect_parameter( ) )
		{
			// workaround for Bugzilla Bug 146566. 
			// If change parameter and re-generate docuemnt file, close TOC panel.
			if ( this.__mode == 'frameset' )
			{
				var oToc = $( 'display0' );
				var oDoc = $( 'Document' );
				if( oToc && oDoc )
				{ 		
					oDoc.style.width = BirtPosition.viewportWidth( ) + "px";
					oToc.style.display="none";
					oToc.query = '0';
				}
			}
		
			var action = soapURL.toLowerCase( );
			
			if ( this.__mode == 'parameter' )
			{
				// check whether set __nocache setting in URL
				if ( this.__ifCache( action ) )
					birtEventDispatcher.broadcastEvent( birtEvent.__E_CACHE_PARAMETER );
				else
					this.__doSubmitWithPattern( );
			}
			else if ( this.__ifSubmit( this.__mode, action ) )
			{
				this.__doSubmit( );
			}
			else
			{
				if( this.__mode == 'run' )
				{
					// if 'run' mode, fire GetPageAll event
					this.__init_page_all( );
				}
				else
				{
					birtEventDispatcher.broadcastEvent( birtEvent.__E_CHANGE_PARAMETER );
				}
				
				this.__l_hide( );
			}
		}
	},
	
	/**
	 *	Override cancel button click.
	 */
	__neh_cancel : function( )
	{
		if ( this.__mode == 'parameter' )
		{
			this.__cancel();
		}
		else
		{
			this.__l_hide( );
		}
	},

	/**
	 *	Handle submit form with defined servlet pattern and current parameters.
	 *
	 *	@return, void
	 */
	__doSubmitWithPattern : function( )
	{
		var url = soapURL;
		
		// parse pattern
		var reg = new RegExp( "[&|?]{1}__pattern\s*=([^&|^#]*)", "gi" );
		var arr = url.match( reg );
		var pattern;
		if( arr && arr.length > 0 )		
			pattern = RegExp.$1;
		else
			pattern = "frameset";
						
		// parse target
		reg = new RegExp( "[&|?]{1}__target\s*=([^&|^#]*)", "gi" );
		arr = url.match( reg );			
		var target;
		if( arr && arr.length > 0 )
			target = RegExp.$1;
		
		reg = new RegExp( "[^/|^?]*[?]{1}", "gi" );
		if( url.search( reg ) > -1 )
			url = url.replace( reg, pattern + "?" );
		
		this.__doSubmit( url, target );
	},
	
	/**
	 *	Handle submit form with current parameters.
	 *
	 *  @param, url
	 *  @param, target
	 *	@return, void
	 */
	__doSubmit : function( url, target )
	{
		var action = url;
		if( !action )
			action = soapURL;
		
		var divObj = document.createElement( "DIV" );
		document.body.appendChild( divObj );
		divObj.style.display = "none";
		
		var formObj = document.createElement( "FORM" );
		divObj.appendChild( formObj );
		
		if ( this.__parameter != null )
		{
			for( var i = 0; i < this.__parameter.length; i++ )	
			{
				var param = document.createElement( "INPUT" );
				formObj.appendChild( param );
				param.TYPE = "HIDDEN";
				param.name = this.__parameter[i].name;
				param.value = this.__parameter[i].value;
				
				//replace the URL parameter			
				var reg = new RegExp( "&" + param.name + "[^&]*&*", "g" );
				action = action.replace( reg, "&" );
			}
		}
		
		// replace __parameterpage setting
		var reg = new RegExp( "([&|?]{1})(__parameterpage\s*=[^&|^#]*)","gi" );
		if ( action.search( reg ) > -1 )
		{
			action = action.replace( reg, "$1" );
		}	
		
		// set target window
		if( target )
			formObj.target = target;
			
		formObj.action = action;
		formObj.method = "post";
		
		// if don't set target, hide the parameter dialog
		if( !target )		
			this.__l_hide( );
						
		formObj.submit( );		
	},

	/**
	 *	Caching parameters success, close window.
	 *
	 *	@return, void
	 */	
	__close : function( )
	{
		if ( BrowserUtility.__isIE( ) )
		{
			window.opener = null;
			window.close( );
		}
		else
		{
			window.status = "close";
		}
	},
	
	/**
	 *	Click 'Cancel', close window.
	 *
	 *	@return, void
	 */	
	__cancel : function( )
	{
		window.status = "cancel";
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
		
		// set preVisible
		this.preVisible = this.visible;
	},

	/**
	Called after element is shown
	*/
	__postShow: function()
	{	
		// if previous is visible, return directly
		if( this.preVisible )	
			return;
				
		// focus on the first input text/password or select or button control
		this.__init_focus( );
	},
	
	/**
	 * Try to focus on the first control.
	 * Input text/password, select and button.
	 */
	__init_focus: function( )
	{
		var oFirstITC;
		var oFirstIBT;
		var oFirstST;
		
		var oITCs = this.__instance.getElementsByTagName( "input" );
		for( var i = 0; i < oITCs.length; i++ )
		{
			// get the first input text/password control
			if( oITCs[i].type == "text" 
			    || oITCs[i].type == "password"  )
			{
				if( !oITCs[i].disabled && !oFirstITC )
				{
					oFirstITC = oITCs[i];
				}
				continue;
			}
			
			// get the first input button control
			if( !oFirstIBT && oITCs[i].type == "button" && !oITCs[i].disabled )
			{
				oFirstIBT = oITCs[i];
			}
		}
		
		// get the first select control
		var oSTs = this.__instance.getElementsByTagName( "select" );
		for( var i = 0; i < oSTs.length; i++ )
		{
			if( !oSTs[i].disabled )
			{
				oFirstST = oSTs[i];
				break;
			}
		}
				
		if( oFirstITC && !oFirstST )
		{
			// if exist input text/password, no select control
			oFirstITC.focus( );
		}
		else if( !oFirstITC && oFirstST )
		{
			// if exist select control, no input text/password
			oFirstST.focus( );
		}
		else if( oFirstITC && oFirstST )
		{
			// exist select control and input text/password
			// compare the parent div offsetTop
			if( oFirstITC.parentNode && oFirstST.parentNode )
			{
				var offsetITC = oFirstITC.parentNode.offsetTop;
				var offsetST = oFirstST.parentNode.offsetTop;
				
				if( offsetITC > offsetST )
				{
					oFirstST.focus( );				
				}
				else
				{
					oFirstITC.focus( );
				}
			}
			else
			{
				// default to focus on input control
				oFirstITC.focus( );
			}
		}
		else
		{
			// focus on button control
			oFirstIBT.focus( );
		}		
	},
		
	/**
	Called before element is hidden
	*/
	__preHide: function( )
	{
		// enable the toolbar buttons
		birtUtility.setButtonsDisabled ( "toolbar", false );
		
		// enable the Navigation Bar buttons
		birtUtility.setButtonsDisabled ( "navigationBar", false );		
	},
	
	/**
	 * Retrieve all pages
	 */
	__init_page_all: function( )
	{
		if( birtParameterDialog.collect_parameter( ) )
		{
			birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE_ALL );
		}
	},

	/**
	 * Check if cache parameter, default to true
	 * 
	 * @param url
	 * @return, true or false
	 */
	__ifCache: function( url )
	{		
		if( url )
			url = url.toLowerCase( );
		else
			url = "";
			
		// if don't set __nocache, default is true
		var reg = new RegExp( "[&|?]{1}__nocache[^&|^#]*", "gi" );
		if( url.search( reg ) < 0 )
			return true;
		else
			return false;			
					
		return true;
	},
		
	/**
	 * Check if submit request
	 * @param mode
	 * @param url
	 * @return, true or false
	 */
	__ifSubmit: function( mode, url )
	{
		// if use '/preview' pattern, submit anyway
		if( mode == 'preview' )
			return true;
		
		if( url )
			url = url.toLowerCase( );
		else
			url = "";
			
		// if use '/frameset' or '/run', check format.
		// if format is not HTML, submit request.	
		if( mode == 'run' || mode == 'frameset' )
		{
			// if don't set format, default is HTML
			var reg = new RegExp( "[&|?]{1}__format\s*=[^&|^#]*", "gi" );
			if( url.search( reg ) < 0 )
				return false

			// if format is htm/html, return false				
			reg = new RegExp( "[&|?]{1}__format\s*=htm[l]{0,1}", "gi" )	
			if( url.search( reg ) > -1 )
				return false;
			
			return true;			
		}
		
		return false;
	}
}
);