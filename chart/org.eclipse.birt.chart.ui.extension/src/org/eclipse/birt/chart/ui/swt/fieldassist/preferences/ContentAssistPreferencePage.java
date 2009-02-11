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

import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.fieldassist.FieldAssistMessages;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
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
		setDescription( FieldAssistMessages.Preferences_ContentAssistDescription );
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and
	 */
	public void createFieldEditors( )
	{
		CustomKeyRadioGroupFieldEditor rgfe = new CustomKeyRadioGroupFieldEditor( PreferenceConstants.PREF_CONTENTASSISTKEY,
				PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOMKEY,
				FieldAssistMessages.Preferences_ContentAssistKey,
				new String[][]{
						{
								FieldAssistMessages.Preferences_ContentAssistKeyCtlSpace,
								PreferenceConstants.PREF_CONTENTASSISTKEY1
						},
						{
								FieldAssistMessages.Preferences_ContentAssistKeyAsterisk,
								PreferenceConstants.PREF_CONTENTASSISTKEY2
						},
						{
								FieldAssistMessages.Preferences_ContentAssistKeyAnyKey,
								PreferenceConstants.PREF_CONTENTASSISTKEYAUTO
						},
						{
								FieldAssistMessages.Preferences_ContentAssistKeyCustom,
								PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOM
						}
				},
				getFieldEditorParent( ) );
		addField( rgfe );

		IntegerFieldEditor editor = new IntegerFieldEditor( PreferenceConstants.PREF_CONTENTASSISTDELAY,
				FieldAssistMessages.Preferences_ContentAssistDelay,
				getFieldEditorParent( ) );
		editor.setValidRange( 0, 10000 );
		addField( editor );

		addField( new BooleanFieldEditor( PreferenceConstants.PREF_CONTENTASSISTKEY_PROPAGATE,
				FieldAssistMessages.Preferences_ContentAssistKeyPropagate,
				getFieldEditorParent( ) ) );

		addField( new RadioGroupFieldEditor( PreferenceConstants.PREF_CONTENTASSISTRESULT,
				FieldAssistMessages.Preferences_ContentAssistResult,
				1,
				new String[][]{
						{
								FieldAssistMessages.Preferences_ContentAssistResultReplace,
								PreferenceConstants.PREF_CONTENTASSISTRESULT_REPLACE
						},
						{
								FieldAssistMessages.Preferences_ContentAssistResultInsert,
								PreferenceConstants.PREF_CONTENTASSISTRESULT_INSERT
						}
				},
				getFieldEditorParent( ) ) );

		addField( new RadioGroupFieldEditor( PreferenceConstants.PREF_CONTENTASSISTFILTER,
				FieldAssistMessages.Preferences_ContentAssistFilter,
				1,
				new String[][]{
						{
								FieldAssistMessages.Preferences_ContentAssistFilterCharacter,
								PreferenceConstants.PREF_CONTENTASSISTFILTER_CHAR
						},
						{
								FieldAssistMessages.Preferences_ContentAssistFilterCumulative,
								PreferenceConstants.PREF_CONTENTASSISTFILTER_CUMULATIVE
						},
						{
								FieldAssistMessages.Preferences_ContentAssistFilterNone,
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