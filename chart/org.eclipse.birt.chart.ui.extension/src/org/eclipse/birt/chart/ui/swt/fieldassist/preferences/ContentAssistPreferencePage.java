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
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a content assist preference page. By subclassing
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

public class ContentAssistPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage
{

	/**
	 * Create a ContentAssistPreferencePage
	 */
	public ContentAssistPreferencePage( )
	{
		super( GRID );
		setPreferenceStore( ChartUIExtensionPlugin.getDefault( )
				.getPreferenceStore( ) );
		setDescription( Messages.getString( "ssPreferencesContentAssistDescription" ) ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent){
		super.createControl( parent );
		ChartUIUtil.bindHelp( getControl(),
				ChartHelpContextIds.PREFERENCE_CHART_CONTENT_ASSIST );
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
		CustomKeyRadioGroupFieldEditor rgfe = new CustomKeyRadioGroupFieldEditor( PreferenceConstants.PREF_CONTENTASSISTKEY,
				PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOMKEY,
				Messages.getString( "ssPreferencesContentAssistKey" ), //$NON-NLS-1$
				new String[][]{
						{
								Messages.getString( "ssPreferencesContentAssistKeyCtlSpace" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTKEY1
						},
						{
								Messages.getString( "ssPreferencesContentAssistKeyAsterisk" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTKEY2
						},
						{
								Messages.getString( "ssPreferencesContentAssistKeyAnyKey" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTKEYAUTO
						},
						{
								Messages.getString( "ssPreferencesContentAssistKeyCustom" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOM
						}
				},
				getFieldEditorParent( ) );
		addField( rgfe );

		IntegerFieldEditor editor = new IntegerFieldEditor( PreferenceConstants.PREF_CONTENTASSISTDELAY,
				Messages.getString( "ssPreferencesContentAssistDelay" ), //$NON-NLS-1$
				getFieldEditorParent( ) );
		editor.setValidRange( 0, 10000 );
		addField( editor );

		addField( new BooleanFieldEditor( PreferenceConstants.PREF_CONTENTASSISTKEY_PROPAGATE,
				Messages.getString( "ssPreferencesContentAssistKeyPropagate" ), //$NON-NLS-1$
				getFieldEditorParent( ) ) );

		addField( new RadioGroupFieldEditor( PreferenceConstants.PREF_CONTENTASSISTRESULT,
				Messages.getString( "ssPreferencesContentAssistResult" ), //$NON-NLS-1$
				1,
				new String[][]{
						{
								Messages.getString( "ssPreferencesContentAssistResultReplace" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTRESULT_REPLACE
						},
						{
								Messages.getString( "ssPreferencesContentAssistResultInsert" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTRESULT_INSERT
						}
				},
				getFieldEditorParent( ) ) );

		addField( new RadioGroupFieldEditor( PreferenceConstants.PREF_CONTENTASSISTFILTER,
				Messages.getString( "ssPreferencesContentAssistFilter" ), //$NON-NLS-1$
				1,
				new String[][]{
						{
								Messages.getString( "ssPreferencesContentAssistFilterCharacter" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTFILTER_CHAR
						},
						{
								Messages.getString( "ssPreferencesContentAssistFilterCumulative" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTFILTER_CUMULATIVE
						},
						{
								Messages.getString( "ssPreferencesContentAssistFilterNone" ), //$NON-NLS-1$
								PreferenceConstants.PREF_CONTENTASSISTFILTER_NONE
						}
				},
				getFieldEditorParent( ) ) );

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