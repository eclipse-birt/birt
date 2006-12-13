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
 *	Birt error dialog.
 */
AbstractExceptionDialog = function( ) { };

AbstractExceptionDialog.prototype = Object.extend( new AbstractBaseDialog( ),
{
	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind: function( data )
	{
	 	if( !data )
	 	{
	 		return;
	 	}
	 	
	 	var oSpans = this.__instance.getElementsByTagName( 'SPAN' );

	 	// Prepare fault string (reason)
	 	var faultStrings = data.getElementsByTagName( 'faultstring' );
	 	if ( faultStrings[0] && faultStrings[0].firstChild )
	 	{
			oSpans[0].innerHTML = faultStrings[0].firstChild.data;
		}
		else
		{
			oSpans[0].innerHTML = "";
		}

	 	// Prepare fault detail (Stack trace)
	 	var faultDetail = data.getElementsByTagName( 'string' );
	 	if ( faultDetail[0] && faultDetail[0].firstChild )
	 	{
			oSpans[1].innerHTML = faultDetail[0].firstChild.data;
		}
		else
		{
			oSpans[1].innerHTML = "";
		}
	}
} );