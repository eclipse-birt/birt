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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;

/**
 * Preference page for Box Style.
 */

public class BoxPreferencePage extends BaseStylePreferencePage
{

	private Object model;

	private ComboBoxMeasureFieldEditor paddingTop, paddingRight, paddingBottom,
			paddingLeft;
	private ComboBoxMeasureFieldEditor marginTop, marginRight, marginBottom,
			marginLeft;

	private SeparatorFieldEditor paddingSep, marginSep;
	private Group gpPadding, gpMargin;

	/**
	 * Default constructor.
	 * 
	 * @param model
	 *            the model of preference page.
	 */
	public BoxPreferencePage( Object model )
	{
		super( model );
		this.model = model;
		setTitle( Messages.getString( "BoxPreferencePage.displayname.Title" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout( )
	{
		( (GridData) paddingSep.getLabelControl( ).getLayoutData( ) ).heightHint = 3;
		( (GridData) paddingSep.getLabelControl( ).getLayoutData( ) ).horizontalSpan = 3;

		( (GridData) paddingTop.getLabelControl( gpPadding ).getLayoutData( ) ).horizontalIndent = 8;
		( (GridData) paddingBottom.getLabelControl( gpPadding ).getLayoutData( ) ).horizontalIndent = 8;
		( (GridData) paddingRight.getLabelControl( gpPadding ).getLayoutData( ) ).horizontalIndent = 8;
		( (GridData) paddingLeft.getLabelControl( gpPadding ).getLayoutData( ) ).horizontalIndent = 8;

		( (GridData) paddingTop.getTextControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 110;
		( (GridData) paddingBottom.getTextControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 110;
		( (GridData) paddingRight.getTextControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 110;
		( (GridData) paddingLeft.getTextControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 110;

		( (GridData) marginSep.getLabelControl( ).getLayoutData( ) ).heightHint = 3;
		( (GridData) marginSep.getLabelControl( ).getLayoutData( ) ).horizontalSpan = 3;

		( (GridData) marginTop.getLabelControl( gpMargin ).getLayoutData( ) ).horizontalIndent = 8;
		( (GridData) marginBottom.getLabelControl( gpMargin ).getLayoutData( ) ).horizontalIndent = 8;
		( (GridData) marginRight.getLabelControl( gpMargin ).getLayoutData( ) ).horizontalIndent = 8;
		( (GridData) marginLeft.getLabelControl( gpMargin ).getLayoutData( ) ).horizontalIndent = 8;

		( (GridData) marginTop.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 100;
		( (GridData) marginBottom.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 100;
		( (GridData) marginRight.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 100;
		( (GridData) marginLeft.getComboBoxControl( getFieldEditorParent( ) )
				.getLayoutData( ) ).widthHint = 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.dialogs.BaseStylePreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );

		getFieldEditorParent( ).setLayout( new GridLayout( ) );

		gpPadding = new Group( getFieldEditorParent( ), 0 );
		gpPadding.setText( Messages.getString( "BoxPreferencePage.text.Padding" ) ); //$NON-NLS-1$
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 120;
		gpPadding.setLayoutData( gdata );
		gpPadding.setLayout( new GridLayout( 3, false ) );

		paddingSep = new SeparatorFieldEditor( gpPadding, false );

		paddingTop = new ComboBoxMeasureFieldEditor( Style.PADDING_TOP_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PADDING_TOP_PROP )
						.getDefn( )
						.getDisplayName( ),
				getMeasureChoiceArray( Style.PADDING_TOP_PROP ),
				gpPadding );

		paddingRight = new ComboBoxMeasureFieldEditor( Style.PADDING_RIGHT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PADDING_RIGHT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getMeasureChoiceArray( Style.PADDING_RIGHT_PROP ),
				gpPadding );

		paddingBottom = new ComboBoxMeasureFieldEditor( Style.PADDING_BOTTOM_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PADDING_BOTTOM_PROP )
						.getDefn( )
						.getDisplayName( ),
				getMeasureChoiceArray( Style.PADDING_BOTTOM_PROP ),
				gpPadding );

		paddingLeft = new ComboBoxMeasureFieldEditor( Style.PADDING_LEFT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PADDING_LEFT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getMeasureChoiceArray( Style.PADDING_LEFT_PROP ),
				gpPadding );

		gpMargin = new Group( getFieldEditorParent( ), 0 );
		gpMargin.setText( Messages.getString( "BoxPreferencePage.text.Margin" ) ); //$NON-NLS-1$
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 120;
		gpMargin.setLayoutData( gdata );
		gpMargin.setLayout( new GridLayout( 3, false ) );

		marginSep = new SeparatorFieldEditor( gpMargin, false );

		marginTop = new ComboBoxMeasureFieldEditor( Style.MARGIN_TOP_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.MARGIN_TOP_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.MARGIN_TOP_PROP ),
				getMeasureChoiceArray( Style.MARGIN_TOP_PROP ),
				gpMargin );

		marginRight = new ComboBoxMeasureFieldEditor( Style.MARGIN_RIGHT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.MARGIN_RIGHT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.MARGIN_RIGHT_PROP ),
				getMeasureChoiceArray( Style.MARGIN_RIGHT_PROP ),
				gpMargin );

		marginBottom = new ComboBoxMeasureFieldEditor( Style.MARGIN_BOTTOM_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.MARGIN_BOTTOM_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.MARGIN_BOTTOM_PROP ),
				getMeasureChoiceArray( Style.MARGIN_BOTTOM_PROP ),
				gpMargin );

		marginLeft = new ComboBoxMeasureFieldEditor( Style.MARGIN_LEFT_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.MARGIN_LEFT_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.MARGIN_LEFT_PROP ),
				getMeasureChoiceArray( Style.MARGIN_LEFT_PROP ),
				gpMargin );

		addField( paddingTop );
		addField( paddingRight );
		addField( paddingBottom );
		addField( paddingLeft );

		addField( marginTop );
		addField( marginRight );
		addField( marginBottom );
		addField( marginLeft );
	}

	private String[][] getChoiceArray( String propName )
	{
		ChoiceSet ci = ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
				propName );

		if ( ci != null )
		{
			Choice[] cs = ci.getChoices( );

			String[][] rt = new String[cs.length][2];

			for ( int i = 0; i < cs.length; i++ )
			{
				rt[i][0] = cs[i].getDisplayName( );
				rt[i][1] = cs[i].getName( );
			}

			return rt;
		}

		return new String[0][2];
	}

	private String[][] getMeasureChoiceArray( String propName )
	{
		ChoiceSet ci = ChoiceSetFactory.getDimensionChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
				propName );

		if ( ci != null )
		{
			Choice[] cs = ci.getChoices( );

			String[][] rt = new String[cs.length][2];

			for ( int i = 0; i < cs.length; i++ )
			{
				rt[i][0] = cs[i].getDisplayName( );
				rt[i][1] = cs[i].getName( );
			}

			return rt;
		}

		return new String[0][2];
	}
}