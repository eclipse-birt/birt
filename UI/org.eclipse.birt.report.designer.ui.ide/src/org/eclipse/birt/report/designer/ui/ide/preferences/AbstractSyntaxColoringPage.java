/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui.ide.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public abstract class AbstractSyntaxColoringPage extends PreferencePage {

	private Color fForegroundColor;
	private Color fBackgroundColor;
	private Color fSelectionForegroundColor;
	private Color fSelectionBackgroundColor;

	final private IPropertyChangeListener fListener = new IPropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent event) {
			final String property = event.getProperty();
			if (AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_SELECTION_FOREGROUND.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_SELECTION_BACKGROUND.equals(property)
					|| AbstractTextEditor.PREFERENCE_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT.equals(property)) {
				initializeSourcePreviewColors(getSourcePreviewViewer());
			}
		}
	};

	public AbstractSyntaxColoringPage() {
		final IPreferenceStore store = EditorsUI.getPreferenceStore();
		if (store != null) {
			store.addPropertyChangeListener(fListener);
		}
	}

	/**
	 * Initializes the colors of the source preview window based on the values in
	 * the Editors' UI preference store
	 * 
	 * @param viewer the {@link ISourceViewer} used as the source preview
	 */
	protected void initializeSourcePreviewColors(ISourceViewer viewer) {
		final IPreferenceStore store = EditorsUI.getPreferenceStore();
		if (store != null && viewer != null) {

			final StyledText styledText = viewer.getTextWidget();

			// ----------- foreground color --------------------
			Color color = store.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? null
					: createColor(store, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, styledText.getDisplay());
			styledText.setForeground(color);

			if (fForegroundColor != null)
				fForegroundColor.dispose();

			fForegroundColor = color;

			// ---------- background color ----------------------
			color = store.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT) ? null
					: createColor(store, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, styledText.getDisplay());
			styledText.setBackground(color);

			if (fBackgroundColor != null)
				fBackgroundColor.dispose();

			fBackgroundColor = color;

			// ----------- selection foreground color --------------------
			color = store.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT) ? null
					: createColor(store, AbstractTextEditor.PREFERENCE_COLOR_SELECTION_FOREGROUND,
							styledText.getDisplay());
			styledText.setSelectionForeground(color);

			if (fSelectionForegroundColor != null)
				fSelectionForegroundColor.dispose();

			fSelectionForegroundColor = color;

			// ---------- selection background color ----------------------
			color = store.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT) ? null
					: createColor(store, AbstractTextEditor.PREFERENCE_COLOR_SELECTION_BACKGROUND,
							styledText.getDisplay());
			styledText.setSelectionBackground(color);

			if (fSelectionBackgroundColor != null)
				fSelectionBackgroundColor.dispose();

			fSelectionBackgroundColor = color;
		}
	}

	/**
	 * Provides the {@link ISourceViewer} that is acting as the source preview
	 * 
	 * @return
	 */
	protected ISourceViewer getSourcePreviewViewer() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	public void dispose() {
		if (fForegroundColor != null) {
			fForegroundColor.dispose();
			fForegroundColor = null;
		}
		if (fBackgroundColor != null) {
			fBackgroundColor.dispose();
			fBackgroundColor = null;
		}
		if (fSelectionForegroundColor != null) {
			fSelectionForegroundColor.dispose();
			fSelectionForegroundColor = null;
		}
		if (fSelectionBackgroundColor != null) {
			fSelectionBackgroundColor.dispose();
			fSelectionBackgroundColor = null;
		}
		if (fListener != null) {
			final IPreferenceStore store = EditorsUI.getPreferenceStore();
			if (store != null) {
				store.removePropertyChangeListener(fListener);
			}
		}
		super.dispose();
	}

	/**
	 * Creates a color from the information stored in the given preference store.
	 * Returns <code>null</code> if there is no such information available.
	 * 
	 * @param store   the store to read from
	 * @param key     the key used for the lookup in the preference store
	 * @param display the display used create the color
	 * @return the created color according to the specification in the preference
	 *         store
	 * @since 2.0
	 */
	private Color createColor(IPreferenceStore store, String key, Display display) {

		RGB rgb = null;

		if (store.contains(key)) {

			if (store.isDefault(key))
				rgb = PreferenceConverter.getDefaultColor(store, key);
			else
				rgb = PreferenceConverter.getColor(store, key);

			if (rgb != null)
				return new Color(display, rgb);
		}

		return null;
	}
}
