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
 * Provides block preference page.
 */

public class BlockPreferencePage extends BaseStylePreferencePage
{

	/**
	 * the preference store( model ) for the preference page.
	 */
	private Object model;

	/**
	 * field editors.
	 *  
	 */
	private ComboBoxMeasureFieldEditor lineHeight;

	private ComboBoxMeasureFieldEditor charSpacing;

	private ComboBoxMeasureFieldEditor wordSpacing;

	private ComboBoxMeasureFieldEditor textIndent;

	private ComboBoxFieldEditor verticalAlign;

	private ComboBoxFieldEditor textAlign;

	private ComboBoxFieldEditor textTrans;

	private ComboBoxFieldEditor whiteSpace;

	private ComboBoxFieldEditor display;

	/**
	 * Constructs a new instance of block preference page.
	 * 
	 * @param model
	 *            the preference store( model ) for the following field editors.
	 */
	public BlockPreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "BlockPreferencePage.displayname.Title" ) ); //$NON-NLS-1$

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

		( (GridData) verticalAlign.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 170;

		( (GridData) textAlign.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 170;

		( (GridData) textTrans.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 170;

		( (GridData) whiteSpace.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 170;

		( (GridData) display.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 170;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );

		lineHeight = new ComboBoxMeasureFieldEditor( Style.LINE_HEIGHT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.LINE_HEIGHT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.LINE_HEIGHT_PROP ) ),
				getChoiceArray( ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.LINE_HEIGHT_PROP ) ),
				getFieldEditorParent( ) );

		charSpacing = new ComboBoxMeasureFieldEditor( Style.LETTER_SPACING_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.LETTER_SPACING_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.LETTER_SPACING_PROP ) ),
				getChoiceArray( ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.LETTER_SPACING_PROP ) ),
				getFieldEditorParent( ) );

		wordSpacing = new ComboBoxMeasureFieldEditor( Style.WORD_SPACING_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.WORD_SPACING_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.WORD_SPACING_PROP ) ),
				getChoiceArray( ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.WORD_SPACING_PROP ) ),
				getFieldEditorParent( ) );

		verticalAlign = new ComboBoxFieldEditor( Style.VERTICAL_ALIGN_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.VERTICAL_ALIGN_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.VERTICAL_ALIGN_PROP ) ),
				getFieldEditorParent( ) );

		textAlign = new ComboBoxFieldEditor( Style.TEXT_ALIGN_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.TEXT_ALIGN_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.TEXT_ALIGN_PROP ) ),
				getFieldEditorParent( ) );

		textIndent = new ComboBoxMeasureFieldEditor( Style.TEXT_INDENT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.TEXT_INDENT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.TEXT_INDENT_PROP ) ),
				getFieldEditorParent( ) );

		textTrans = new ComboBoxFieldEditor( Style.TEXT_TRANSFORM_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.TEXT_TRANSFORM_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.TEXT_TRANSFORM_PROP ) ),
				getFieldEditorParent( ) );

		whiteSpace = new ComboBoxFieldEditor( Style.WHITE_SPACE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.WHITE_SPACE_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.WHITE_SPACE_PROP ) ),
				getFieldEditorParent( ) );

		display = new ComboBoxFieldEditor( Style.DISPLAY_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.DISPLAY_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
						Style.DISPLAY_PROP ) ),
				getFieldEditorParent( ) );

		addField( lineHeight );
		addField( charSpacing );
		addField( wordSpacing );
		addField( verticalAlign );
		addField( textAlign );
		addField( textIndent );
		addField( textTrans );
		addField( whiteSpace );
		addField( display );
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
		Choice[] choices = set.getChoices( );

		String[][] names = null;
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