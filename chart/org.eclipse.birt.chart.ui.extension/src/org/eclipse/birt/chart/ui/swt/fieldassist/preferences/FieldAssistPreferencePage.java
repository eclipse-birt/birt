/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.fieldassist.preferences;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a field assist preference page. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built
 * into JFace that allows us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 * 
 * @since 2.5
 */

public class FieldAssistPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage
{

	/**
	 * Create the FieldAssistPreferencePage
	 */
	public FieldAssistPreferencePage( )
	{
		super( GRID );
		setPreferenceStore( ChartUIExtensionPlugin.getDefault( )
				.getPreferenceStore( ) );
		setDescription( Messages.getString( "ssPreferencesDescription" ) ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl( parent );
		ChartUIUtil.bindHelp( getControl(),
				ChartHelpContextIds.PREFERENCE_CHART_FIELD_ASSIST );
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		addField( new BooleanFieldEditor( PreferenceConstants.PREF_SHOWREQUIREDFIELDLABELINDICATOR,
				Messages.getString( "ssPreferencesShowRequiredFieldLabelIndicator" ), //$NON-NLS-1$
				getFieldEditorParent( ) ) );

		Group g = new Group( getFieldEditorParent( ), SWT.NONE );
		g.setText( Messages.getString( "ssPreferencesAssistSymbol" ) ); //$NON-NLS-1$
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		g.setLayoutData( gd );

		addField( new RadioGroupFieldEditor( PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION,
				Messages.getString( "ssPreferencesDecoratorVert" ), //$NON-NLS-1$
				1,
				new String[][]{
						{
								Messages.getString( "ssPreferencesDecoratorTop" ), //$NON-NLS-1$
								PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION_TOP
						},
						{
								Messages.getString( "ssPreferencesDecoratorCenter" ), //$NON-NLS-1$
								PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION_CENTER
						},
						{
								Messages.getString( "ssPreferencesDecoratorBottom" ), //$NON-NLS-1$
								PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION_BOTTOM
						}
				},
				g ) );

		addField( new RadioGroupFieldEditor( PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION,
				Messages.getString( "ssPreferencesDecoratorHorz" ), //$NON-NLS-1$
				1,
				new String[][]{
						{
								Messages.getString( "ssPreferencesDecoratorLeft" ), //$NON-NLS-1$
								PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION_LEFT
						},
						{
								Messages.getString( "ssPreferencesDecoratorRight" ), //$NON-NLS-1$
								PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION_RIGHT
						}
				},
				g ) );

		IntegerFieldEditor editor = new IntegerFieldEditor( PreferenceConstants.PREF_DECORATOR_MARGINWIDTH,
				Messages.getString( "ssPreferencesDecoratorMargin" ), //$NON-NLS-1$
				g );
		editor.setValidRange( 0, 10 );
		addField( editor );

		g.setLayout( new GridLayout( ) );

		Dialog.applyDialogFont( getFieldEditorParent( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
	}

}
