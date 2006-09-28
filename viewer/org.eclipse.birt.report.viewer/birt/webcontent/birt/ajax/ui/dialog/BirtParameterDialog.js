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
	 *	Event handler closures.
	 */
	 __neh_change_cascade_text_closure : null,

    /**
	 *	Check if parameter isnot allowBlank.
	 */
	__is_parameter_not_allowblank : null,
	
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

		// Change event for Cascading parameter text field
		this.__neh_change_cascade_text_closure = this.__neh_change_cascade_text.bindAsEventListener( this );
			    
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
				if ( oLastInput.name == "isCascade" )
					oCascadeFlag = oLastInput.value;
			}
			
			// find select items to install event listener
			if( oSelect.length > 0 && oCascadeFlag == "true" )
			{
				if ( i < oTRC.length - 1 )
				{
					Event.observe( oSelect[0], 'change', this.__neh_click_select_closure, false );
					
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
	 *	Collect parameters, include five cases appear in Birt1.0 Viewer.
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
						
			if( oSEC.length == 1 && oIEC.length <= 3 )
			{
				// deal with "select" parameter		
				if( oIEC.length > 0 )
				{
					this.__parameter[k].name = oIEC[0].name;
				}
				else
				{
					this.__parameter[k].name = oSEC[0].name;
				}	
									
				if ( oSEC[0].selectedIndex < 0 )
				{
					oSEC[0].focus( );
					alert( this.__parameter[k].name + " should have a value" );
					return false;
				}
				
				// Check if select 'Null Value' option
				var tempText = oSEC[0].options[oSEC[0].selectedIndex].text;						
				if( tempText && tempText == this.__display_null )
				{	
					this.__parameter[k].name = this.__isnull;
					if ( oIEC.length > 0 )
						this.__parameter[k].value = oIEC[0].name;
					else
						this.__parameter[k].value = oSEC[0].name;
					k++;
					continue;
				}														
				
				// check if value is blank, don't allow new value
				var tempValue = oSEC[0].options[oSEC[0].selectedIndex].value;
				if ( birtUtility.trim( tempValue ) == '' )
				{
					oSEC[0].focus( );
					alert( this.__parameter[k].name + " cannot be blank" );
					return false;
				}
							
				this.__parameter[k].value = tempValue;				
				k++;
				
				// set display text for the "select" parameter
				if( !this.__parameter[k] )
				{
					this.__parameter[k] = { };
				}
				this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
				this.__parameter[k].value = tempText;
				k++;
				
				continue;
			}
			
			if( oSEC.length == 0 && ( oIEC.length == 1 || oIEC.length == 2 ) )
			{
				var temp = {};
				var tempDef = null;
				if( oIEC.length == 1 )
				{
					temp = oIEC[0]
				}
				else if( oIEC[0].type == 'hidden' )
				{
					tempDef = oIEC[0]
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
					if( birtUtility.trim( temp.value ) == '' )
					{
						if( tempDef )
						{
							temp.focus( );
							alert( temp.name + ' cannot be blank' );
							return false;
						}
						else
						{
							this.__parameter[k].value = temp.value;
						}
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
				else if( temp.type == 'radio' )
				{
					// deal with radio
					this.__parameter[k].name = temp.name;
					this.__parameter[k].value = temp.value;
					k++;		
					
					// set display text for the "radio" parameter
					var displayLabel = document.getElementById( temp.id + "_label" );
					if( !displayLabel )
						continue;
						
					if( !this.__parameter[k] )
					{
						this.__parameter[k] = { };
					}
					this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
					this.__parameter[k].value = displayLabel.title;
					k++;			
				}
			}
			else if( oSEC.length <= 1 && oIEC.length >= 3 )
			{
				for( var j = 0; j < oIEC.length; j++ )
				{
					// deal with radio
					if( oIEC[j].type == 'radio' && oIEC[j].checked )
					{
						if( oIEC[j+1] && ( oIEC[j+1].type == 'text' || oIEC[j+1].type == 'password' ) )
						{
							//Check if allow blank
							var temp = oIEC[j+2];
							if ( temp && temp.id == 'isNotBlank' && temp.value == 'true' )
							{
								if ( birtUtility.trim( oIEC[j+1].value ) == '' )
								{
									oIEC[j+1].focus( );
									alert( temp.name + ' cannot be blank' );
									return false;
								}
							}
							
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
								//oIEC[j+1].value = "";
								k++;	            
							}
							
							// set display text for the List type parameter with entered value
							if( !this.__parameter[k] )
							{
								this.__parameter[k] = { };
							}
							this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
							this.__parameter[k].value = oIEC[j+1].value;
							k++;
						}
						else if( oSEC[0] )
						{
							// deal with "select" parameter							
							if ( oSEC[0].selectedIndex < 0 )
							{
								oSEC[0].focus( );
								alert( oIEC[j].value + " should have a value" );
								return false;
							}
																					
							var tempText = oSEC[0].options[oSEC[0].selectedIndex].text;
							var tempValue = oSEC[0].options[oSEC[0].selectedIndex].value;
							
							// Check if select 'Null Value' option
							if( tempText == this.__display_null )
							{
								this.__parameter[k].name = this.__isnull;
								this.__parameter[k].value = oIEC[j].value;
								k++;	
								continue;
							}

							if ( this.__is_parameter_not_allowblank( oIEC ) && birtUtility.trim( tempValue ) == '' )
							{
								oSEC[0].focus( );
								alert( oIEC[j].value + " cannot be blank" );
								return false;
							}
							
							
							this.__parameter[k].name = oIEC[j].value;							
							this.__parameter[k].value = tempValue;
							k++;
							
							// set display text for the "select" parameter
							if( !this.__parameter[k] )
							{
								this.__parameter[k] = { };
							}
							this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
							this.__parameter[k].value = tempText;
							k++;
						}
						else if( !oIEC[j+1] && !oIEC[j].name )
						{
							//deal with common radio with null value
							this.__parameter[k].name = this.__isnull;
							this.__parameter[k].value = oIEC[j-1].name;
							k++;
						}
						else
						{
							//deal with common radio
							this.__parameter[k].name = oIEC[j].name;
							this.__parameter[k].value = oIEC[j].value;
							k++;
						
							// set display text for the "radio" parameter
							var displayLabel = document.getElementById( oIEC[j].id + "_label" );
							if( !displayLabel )
								continue;
								
							if( !this.__parameter[k] )
							{
								this.__parameter[k] = { };
							}
							this.__parameter[k].name = this.__isdisplay + this.__parameter[k-1].name;
							this.__parameter[k].value = displayLabel.title;
							k++;
						}
					}
				}
			}
		}
		return true;
	},

	/**
	 *	Check if current parameter isnot allowBlank.
	 *
	 *	@oInputs, Input control collection 
	 *	@return, true or false
	 */
	__is_parameter_not_allowblank : function( oInputs )
	{
		if( !oInputs || oInputs.length <= 0 )
			return false;
		
		var flag = false;		
		for( var i = 0; i< oInputs.length; i++ )
		{
			// if find defined input control
			if( oInputs[i].id == 'isNotBlank' && oInputs[i].value == 'true' )
			{
				flag = true;
				break;		
			}
		}
		
		return flag;
	},

	/**
	 *	Handle changing on cascading parameter text field.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_change_cascade_text : function( event )
	{						
	    var matrix = new Array( );
	    var m = 0;
        for( var i = 0; i < this.__cascadingParameter.length; i++ )
        {
            for( var j = 0; j < this.__cascadingParameter[i].length; j++ )
            {
                if( this.__cascadingParameter[i][j].name == Event.element( event ).id.substr( 0, Event.element( event ).id.length - 6 ) )
                {
                    this.__cascadingParameter[i][j].value = Event.element( event ).value;
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
			var action = window.location.href.toLowerCase( );
			
			if ( this.__mode == 'parameter' )
			{
				birtEventDispatcher.broadcastEvent( birtEvent.__E_CACHE_PARAMETER );
			}
			else if ( this.__mode == 'run' 
					|| ( this.__mode == 'frameset' && action.indexOf( '&__format=pdf' ) > 0 ) )
			{
				this.__doSubmit( );
			}
			else
			{
				birtEventDispatcher.broadcastEvent( birtEvent.__E_CHANGE_PARAMETER );
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
	 *	Handle submit form with current parameters.
	 *
	 *	@return, void
	 */
	__doSubmit : function( )
	{
		var action = window.location.href;
		
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

		formObj.action = action;
		formObj.method = "post";
				
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
			
		// focus on the first input text/password or button control
		var oITCs = this.__instance.getElementsByTagName( "input" );
		for( var i = 0; i < oITCs.length; i++ )
		{
			if( oITCs[i].type == "text" 
			    || oITCs[i].type == "password" || oITCs[i].type == 'button' )
			{
				if( !oITCs[i].disabled )
				{
					oITCs[i].focus( );
					break;
				}
			}			
		}
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
}
);