/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ParameterDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * DefaultParameterDialogControlTypeHelper
 */
public class DefaultParameterDialogControlTypeHelper extends
		AbstractDialogHelper
{

	protected static final IChoiceSet CONTROL_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_CONTROL );

	protected Combo dataTypeChooser;

	public void createContent( Composite parent )
	{
		dataTypeChooser = new Combo( parent, SWT.READ_ONLY | SWT.DROP_DOWN );
		dataTypeChooser.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event e )
			{
				List<Listener> listeners = DefaultParameterDialogControlTypeHelper.this.listeners.get( SWT.Selection );
				if ( listeners == null )
					return;
				for ( int i = 0; i < listeners.size( ); i++ )
					listeners.get( i ).handleEvent( e );
			}
		} );
	}

	public Control getControl( )
	{
		return dataTypeChooser;
	}

	protected boolean isStatic( )
	{
		return (Boolean) this.getProperty( ParameterDialog.STATIC_VALUE );
	}

	protected String getDataType( )
	{
		return (String) this.getProperty( ParameterDialog.DATATYPE_VALUE );
	}

	protected String getInputControlType( )
	{
		return (String) this.getProperty( ParameterDialog.CONTROLTYPE_INPUTVALUE );
	}

	public void update( boolean inward )
	{
		if ( inward )
		{
			inwardUpdate( );
		}
		else
		{
			outwardUpdate( );
		}
	}

	protected void outwardUpdate( )
	{
		String displayText = dataTypeChooser.getText( );
		if ( StringUtil.isBlank( displayText ) )
		{
			return;
		}
		if ( ParameterDialog.DISPLAY_NAME_CONTROL_COMBO.equals( displayText ) )
		{
			this.setProperty( ParameterDialog.CONTROLTYPE_VALUE,
					ParameterDialog.PARAM_CONTROL_COMBO );
			return;
		}
		if ( ParameterDialog.DISPLAY_NAME_CONTROL_LIST.equals( displayText ) )
		{
			this.setProperty( ParameterDialog.CONTROLTYPE_VALUE,
					ParameterDialog.PARAM_CONTROL_LIST );
			return;
		}
		this.setProperty( ParameterDialog.CONTROLTYPE_VALUE,
				CONTROL_TYPE_CHOICE_SET.findChoiceByDisplayName( displayText )
						.getName( ) );
	}

	protected void inwardUpdate( )
	{
		String[] choices;
		if ( isStatic( ) )
		{
			if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( getDataType( ) ) )
			{
				choices = new String[3];
			}
			else
			{
				choices = new String[4];
			}
		}
		else
		{
			choices = new String[2];
		}
		if ( dataTypeChooser.getItemCount( ) != choices.length )
		{
			String originalSelection = dataTypeChooser.getText( );
			if ( isStatic( ) )
			{
				if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( getDataType( ) ) )
				{
					choices[0] = CONTROL_TYPE_CHOICE_SET.findChoice( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX )
							.getDisplayName( );
					choices[1] = ParameterDialog.DISPLAY_NAME_CONTROL_COMBO;
				}
				else
				{
					choices[0] = CONTROL_TYPE_CHOICE_SET.findChoice( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX )
							.getDisplayName( );
					// choices[1] = DISPLAY_NAME_CONTROL_LIST;
					choices[1] = ParameterDialog.DISPLAY_NAME_CONTROL_COMBO;
					choices[2] = ParameterDialog.DISPLAY_NAME_CONTROL_LIST;
				}
				// choices[choices.length - 2] = DISPLAY_NAME_CONTROL_COMBO;
				// choices[choices.length - 2] = DISPLAY_NAME_CONTROL_LIST;
				choices[choices.length - 1] = CONTROL_TYPE_CHOICE_SET.findChoice( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON )
						.getDisplayName( );

			}
			else
			{
				choices[0] = ParameterDialog.DISPLAY_NAME_CONTROL_COMBO;
				choices[1] = ParameterDialog.DISPLAY_NAME_CONTROL_LIST;
			}
			dataTypeChooser.setItems( choices );
			if ( originalSelection.length( ) == 0 )
			{// initialize
				dataTypeChooser.setText( getInputControlDisplayName( ) );
			}
			else
			{
				int index = dataTypeChooser.indexOf( originalSelection );
				if ( index == -1 )
				{// The original control type cannot be
					// supported
					dataTypeChooser.select( 0 );
					dataTypeChooser.notifyListeners( SWT.Selection, new Event( ) );
				}
				dataTypeChooser.setText( originalSelection );
			}
		}
	}

	protected String getInputControlDisplayName( )
	{
		String type = getInputControlType( );
		String displayName = null;
		if ( CONTROL_TYPE_CHOICE_SET.findChoice( type ) != null )
		{
			displayName = CONTROL_TYPE_CHOICE_SET.findChoice( type )
					.getDisplayName( );
		}
		else
		{
			if ( ParameterDialog.PARAM_CONTROL_COMBO.equals( type ) )
			{
				displayName = ParameterDialog.DISPLAY_NAME_CONTROL_COMBO;
			}
			else if ( ParameterDialog.PARAM_CONTROL_LIST.equals( type ) )
			{
				displayName = ParameterDialog.DISPLAY_NAME_CONTROL_LIST;
			}
		}
		return displayName;
	}
}
