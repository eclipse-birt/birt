/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.swt.layout.GridData;

/**
 * Provides background preference page.
 */

public class BackgroundPreferencePage extends BaseStylePreferencePage
{

	/**
	 * the preference store( model ) for the preference page.
	 */
	private Object model;

	/**
	 * field editors.
	 */
	private ColorFieldEditor color;

	private ComboBoxFieldEditor repeat;

	private ComboBoxFieldEditor attachMent;

	private BgImageFieldEditor bgImage;

	private ComboBoxMeasureFieldEditor horizonPos;

	private ComboBoxMeasureFieldEditor verticalPos;

	/**
	 * Constructs a new instance of background preference page.
	 * 
	 * @param model
	 *            the preference store( model ) for the following field editors.
	 */
	public BackgroundPreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "BackGroundPreferencePage.displayname.Title" ) ); //$NON-NLS-1$

		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout( )
	{
		super.adjustGridLayout( );

		( (GridData) repeat.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 167;

		( (GridData) attachMent.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 167;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{

		super.createFieldEditors( );

		color = new ColorFieldEditor( Style.BACKGROUND_COLOR_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( Style.BACKGROUND_COLOR_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getFieldEditorParent( ) );

		bgImage = new BgImageFieldEditor( Style.BACKGROUND_IMAGE_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( Style.BACKGROUND_IMAGE_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getFieldEditorParent( ) );

		repeat = new ComboBoxFieldEditor( Style.BACKGROUND_REPEAT_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( Style.BACKGROUND_REPEAT_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.BACKGROUND_REPEAT_PROP ) ),
				getFieldEditorParent( ) );

		attachMent = new ComboBoxFieldEditor( Style.BACKGROUND_ATTACHMENT_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( Style.BACKGROUND_ATTACHMENT_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.BACKGROUND_ATTACHMENT_PROP ) ),
				getFieldEditorParent( ) );

		horizonPos = new ComboBoxMeasureFieldEditor( Style.BACKGROUND_POSITION_X_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( Style.BACKGROUND_POSITION_X_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.BACKGROUND_POSITION_X_PROP ) ),
				getChoiceArray( ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.BACKGROUND_POSITION_X_PROP ) ),
				getFieldEditorParent( ) );

		verticalPos = new ComboBoxMeasureFieldEditor( Style.BACKGROUND_POSITION_Y_PROP,
				Messages.getString( ( (StyleHandle) model ).getPropertyHandle( Style.BACKGROUND_POSITION_Y_PROP )
						.getDefn( )
						.getDisplayNameID( ) ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.BACKGROUND_POSITION_Y_PROP ) ),
				getChoiceArray( ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.BACKGROUND_POSITION_Y_PROP ) ),
				getFieldEditorParent( ) );

		addField( color );
		addField( bgImage );
		addField( repeat );
		addField( attachMent );
		addField( horizonPos );
		addField( verticalPos );
	}

	/**
	 * Gets choice array of the given choise set.
	 * 
	 * @param set
	 *            The given choice set.
	 * @return String[][]: The choice array of the key, which contains he names
	 *         (labels) and underlying values, will be arranged as: { {name1,
	 *         value1}, {name2, value2}, ...}
	 */
	private String[][] getChoiceArray( ChoiceSet set )
	{

		String[][] names = null;
		if ( set == null )
		{
			return names;
		}
		Choice[] choices = set.getChoices( );

		if ( choices.length > 0 )
		{
			names = new String[choices.length][2];
			for ( int i = 0; i < choices.length; i++ )
			{
				names[i][0] = choices[i].getDisplayName( );
				names[i][1] = choices[i].getName( );
			}
		}
		return names;
	}

}