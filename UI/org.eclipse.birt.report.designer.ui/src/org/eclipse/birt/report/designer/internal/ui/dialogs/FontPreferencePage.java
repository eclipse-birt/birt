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

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.DimensionUtil;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * Provides font preference page.
 */

public class FontPreferencePage extends BaseStylePreferencePage
{

	/**
	 * the preference store( model ) for the preference page.
	 */
	private Object model;

	/**
	 * field editors.
	 */
	private ColorFieldEditor color;

	private ComboFieldEditor name;

	private ComboFieldEditor style;

	private ComboFieldEditor weight;

	private ComboMeasureFieldEditor size;

	private DecorationFieldEditor docoration;

	/**
	 * preview label for previewing sample text.
	 */
	private PreviewLabel sample;

	/**
	 * Constructs a new instance of font preference page.
	 * 
	 * @param model
	 *            the preference store( model ) for the following field editors.
	 */
	public FontPreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "FontPreferencePage.displayname.Title" ) ); //$NON-NLS-1$

		this.model = model;
	}

	/**
	 * Adjust the layout of the field editors so that they are properly aligned.
	 */
	protected void adjustGridLayout( )
	{
		super.adjustGridLayout( );

		( (GridData) name.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 200;

		( (GridData) style.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 200;

		( (GridData) weight.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 200;

	}

	/**
	 * Returns the model.
	 * 
	 * @return
	 */
	public Object getModel( )
	{
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );

		name = new ComboFieldEditor( Style.FONT_FAMILY_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.FONT_FAMILY_PROP )
						.getDefn( )
						.getDisplayName( ),
				getFontChoiceArray( ),
				getFieldEditorParent( ) );

		color = new ColorFieldEditor( Style.COLOR_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.COLOR_PROP )
						.getDefn( )
						.getDisplayName( ),
				getFieldEditorParent( ) );

		size = new ComboMeasureFieldEditor( Style.FONT_SIZE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.FONT_SIZE_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( DesignChoiceConstants.CHOICE_FONT_SIZE ),
				getChoiceArray( DesignChoiceConstants.CHOICE_UNITS ),
				getFieldEditorParent( ) );

		style = new ComboFieldEditor( Style.FONT_STYLE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.FONT_STYLE_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( DesignChoiceConstants.CHOICE_FONT_STYLE ),
				getFieldEditorParent( ) );

		weight = new ComboFieldEditor( Style.FONT_WEIGHT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.FONT_WEIGHT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( DesignChoiceConstants.CHOICE_FONT_WEIGHT ),
				getFieldEditorParent( ) );

		docoration = new DecorationFieldEditor( Style.TEXT_UNDERLINE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.TEXT_UNDERLINE_PROP )
						.getDefn( )
						.getDisplayName( ),
				Style.TEXT_OVERLINE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.TEXT_OVERLINE_PROP )
						.getDefn( )
						.getDisplayName( ),
				Style.TEXT_LINE_THROUGH_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.TEXT_LINE_THROUGH_PROP )
						.getDefn( )
						.getDisplayName( ),
				Messages.getString( "FontPreferencePage.label.fontDecoration" ), //$NON-NLS-1$
				getFieldEditorParent( ) );

		addField( name );
		addField( color );
		addField( size );
		addField( style );
		addField( weight );
		addField( docoration );

		addField( new SeparatorFieldEditor( getFieldEditorParent( ), false ) );

		Group group = new Group( getFieldEditorParent( ), SWT.SHADOW_OUT );
		group.setText( Messages.getString( "FontPreferencePage.text.Preview" ) ); //$NON-NLS-1$
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 400;
		gd.heightHint = 100;
		gd.horizontalSpan = 4;
		group.setLayoutData( gd );

		group.setLayout( new GridLayout( ) );
		sample = new PreviewLabel( group, SWT.NONE );
		sample.setText( Messages.getString( "FontPreferencePage.text.PreviewContent" ) ); //$NON-NLS-1$
		sample.setLayoutData( new GridData( GridData.FILL_BOTH ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		Control ct = super.createContents( parent );

		updatePreview( );

		return ct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange( PropertyChangeEvent event )
	{
		updatePreview( );
	}

	/**
	 * Updates sample text for preview according to the property change.
	 *  
	 */
	private void updatePreview( )
	{
		if ( sample != null )
		{
			String fontFamily = name.getValueForName( name.getComboBoxControl( null )
					.getText( ) );
			String familyValue = (String) DesignerConstants.familyMap.get( fontFamily );

			if ( familyValue == null )
			{
				familyValue = fontFamily;
			}

			//set default font size.
			String fontSize = DesignChoiceConstants.FONT_SIZE_MEDIUM;
			int sizeValue = Integer.valueOf( (String) DesignerConstants.fontMap.get( fontSize ) )
					.intValue( );

			if ( size.InComboNamesList( size.getComboBoxControl( getFieldEditorParent( ) )
					.getText( ) ) )
			{
				fontSize = size.getBoxValueForName( size.getComboBoxControl( getFieldEditorParent( ) )
						.getText( ) );
				if ( DesignChoiceConstants.FONT_SIZE_LARGER.equals( fontSize ) )
				{
					fontSize = DesignChoiceConstants.FONT_SIZE_LARGE;
				}
				else if ( DesignChoiceConstants.FONT_SIZE_SMALLER.equals( fontSize ) )
				{
					fontSize = DesignChoiceConstants.FONT_SIZE_SMALL;
				}
				sizeValue = Integer.valueOf( (String) DesignerConstants.fontMap.get( fontSize ) )
						.intValue( );
			}
			else
			{
				String text = size.getComboBoxControl( getFieldEditorParent( ) )
						.getText( );
				String pre = size.getMeasureValueForName( size.getMeasureControl( getFieldEditorParent( ) )
						.getText( ) );
				String target = DesignChoiceConstants.UNITS_PT;

				if ( DimensionUtil.isAbsoluteUnit( pre ) )
				{
					if ( DEUtil.isValidNumber( text ) )
					{
						try
						{
							sizeValue = (int) ( DimensionUtil.convertTo( text,
									pre,
									target ) ).getMeasure( );
						}
						catch ( PropertyValueException e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}
				else
				{
					// use default font size.
				}
			}

			boolean italic = false;
			String fontStyle = style.getValueForName( style.getComboBoxControl( getFieldEditorParent( ) )
					.getText( ) );
			if ( DesignChoiceConstants.FONT_STYLE_ITALIC.equals( fontStyle ) )
			{
				italic = true;
			}

			String fontWeight = weight.getValueForName( weight.getComboBoxControl( null )
					.getText( ) );
			boolean bold = false;
			int fw = 400;
			if ( DesignChoiceConstants.FONT_WEIGHT_NORMAL.equals( fontWeight ) )
			{
				//
			}
			else if ( DesignChoiceConstants.FONT_WEIGHT_BOLD.equals( fontWeight ) )
			{
				bold = true;
				fw = 700;
			}
			else if ( DesignChoiceConstants.FONT_WEIGHT_BOLDER.equals( fontWeight ) )
			{
				bold = true;
				fw = 1000;
			}
			else if ( DesignChoiceConstants.FONT_WEIGHT_LIGHTER.equals( fontWeight ) )
			{
				fw = 100;
			}
			else
			{
				try
				{
					fw = Integer.parseInt( fontWeight );
				}
				catch ( NumberFormatException e )
				{
					fw = 400;
				}

				if ( fw > 700 )
				{
					bold = true;
				}
			}

			sample.setFontFamily( familyValue );
			sample.setFontSize( sizeValue );
			sample.setBold( bold );
			sample.setItalic( italic );
			sample.setFontWeight( fw );

			//			sample.setForeground( new Color( Display.getCurrent( ),
			//					color.getColorSelector( ).getColorValue( ) ) );
			sample.setForeground( ColorManager.getColor( color.getColorSelector( )
					.getColorValue( ) ) );

			sample.setUnderline( docoration.getUnderLinePropControl( null )
					.getSelection( ) );
			sample.setLinethrough( docoration.getLineThroughPropControl( null )
					.getSelection( ) );
			sample.setOverline( docoration.getOverLinePropControl( null )
					.getSelection( ) );

			sample.updateView( );
		}
	}

	private String[][] getFontChoiceArray( )
	{
		String[][] fca = getChoiceArray( DesignChoiceConstants.CHOICE_FONT_FAMILY );

		String[] sf = DEUtil.getSystemFontNames( );

		String[][] rt = new String[fca.length + sf.length][2];

		for ( int i = 0; i < rt.length; i++ )
		{
			if ( i < fca.length )
			{
				rt[i][0] = fca[i][0];
				rt[i][1] = fca[i][1];
			}
			else
			{
				rt[i][0] = sf[i - fca.length];
				rt[i][1] = sf[i - fca.length];
			}
		}

		return rt;
	}

	/**
	 * Gets choice array of the given property name ( key ).
	 * 
	 * @param key
	 *            The given property name.
	 * @return String[][]: The choice array of the key, which contains he names
	 *         (labels) and underlying values, will be arranged as: { {name1,
	 *         value1}, {name2, value2}, ...}
	 */
	private String[][] getChoiceArray( String key )
	{
		Choice[] choices = MetaDataDictionary.getInstance( )
				.getChoiceSet( key )
				.getChoices( );

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