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
 * Preference page for page break styles.
 */

public class PageBreakPreferencePage extends BaseStylePreferencePage
{

	private Object model;

	/**
	 * Default constructor.
	 * 
	 * @param model
	 *            the model of preference page.
	 */
	public PageBreakPreferencePage( Object model )
	{
		super( model );
		this.model = model;
		setTitle( Messages.getString( "PageBreakPreferencePage.displayname.Title" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );

		GridData gdata;

		EditableComboFieldEditor widows = new EditableComboFieldEditor( Style.WIDOWS_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.WIDOWS_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.WIDOWS_PROP ),
				getFieldEditorParent( ) );
		gdata = new GridData( );
		gdata.widthHint = 96;
		widows.getComboBoxControl( getFieldEditorParent( ) )
				.setLayoutData( gdata );

		addField( widows );

		EditableComboFieldEditor orphans = new EditableComboFieldEditor( Style.ORPHANS_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.ORPHANS_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.ORPHANS_PROP ),
				getFieldEditorParent( ) );
		gdata = new GridData( );
		gdata.widthHint = 96;
		orphans.getComboBoxControl( getFieldEditorParent( ) )
				.setLayoutData( gdata );

		addField( orphans );

		addField( new SeparatorFieldEditor( getFieldEditorParent( ), false ) );

		ComboFieldEditor before = new ComboFieldEditor( Style.PAGE_BREAK_BEFORE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PAGE_BREAK_BEFORE_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.PAGE_BREAK_BEFORE_PROP ),
				getFieldEditorParent( ) );
		gdata = new GridData( );
		gdata.widthHint = 100;
		before.getComboBoxControl( getFieldEditorParent( ) )
				.setLayoutData( gdata );

		addField( before );

		ComboFieldEditor inside = new ComboFieldEditor( Style.PAGE_BREAK_INSIDE_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PAGE_BREAK_INSIDE_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.PAGE_BREAK_INSIDE_PROP ),
				getFieldEditorParent( ) );
		gdata = new GridData( );
		gdata.widthHint = 100;
		inside.getComboBoxControl( getFieldEditorParent( ) )
				.setLayoutData( gdata );

		addField( inside );

		ComboFieldEditor after = new ComboFieldEditor( Style.PAGE_BREAK_AFTER_PROP,
				( (StyleHandle) model ).getPropertyHandle( Style.PAGE_BREAK_AFTER_PROP )
						.getDefn( )
						.getDisplayName( ),
				getChoiceArray( Style.PAGE_BREAK_AFTER_PROP ),
				getFieldEditorParent( ) );
		gdata = new GridData( );
		gdata.widthHint = 100;
		after.getComboBoxControl( getFieldEditorParent( ) )
				.setLayoutData( gdata );

		addField( after );
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

}