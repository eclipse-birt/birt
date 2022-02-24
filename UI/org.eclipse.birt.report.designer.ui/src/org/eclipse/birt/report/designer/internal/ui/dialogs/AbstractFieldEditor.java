/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */

public abstract class AbstractFieldEditor extends FieldEditor {

	/**
	 * Indicates that no value change should fired when the field editor is not
	 * loaded.
	 */
	protected boolean isLoaded = false;

	private boolean isDirty = false;

	private String oldValue = ""; //$NON-NLS-1$

	private String propValue = ""; //$NON-NLS-1$

	private String displayValue = ""; //$NON-NLS-1$

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	private String defaultUnit = ""; //$NON-NLS-1$

	/**
	 * Creates a new abstract field editor.
	 */
	public AbstractFieldEditor() {
		super();
	}

	/**
	 * Creates a new abstract field editor.
	 * 
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent    the parent of the field editor's control
	 */
	public AbstractFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	/**
	 * @param defaultUnit The defaultUnit to set.
	 */
	public void setDefaultUnit(String defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	/**
	 * @return Returns the defaultUnit.
	 */
	public String getDefaultUnit() {
		return defaultUnit;
	}

	public void load() {
		if (getPreferenceStore() != null) {
			setPresentsDefaultValue(false);

			isLoaded = false;
			doLoad();
			isLoaded = true;

			refreshValidState();
		}
	}

	public void loadDefault() {
		if (getPreferenceStore() != null) {
			setPresentsDefaultValue(true);

			doLoadDefault();

			refreshValidState();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		if (isDirty()) {
			if (propValue != null) {
				getPreferenceStore().setValue(getPreferenceName(), propValue);
			} else {
				getPreferenceStore().setValue(getPreferenceName(), null);
			}

		}
	}

	public void store() {
		if (getPreferenceStore() == null) {
			return;
		}
		doStore();
	}

	/**
	 * Gets old value of the field editor.
	 * 
	 * @return Returns the old value.
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * Gets property value of the field editor.
	 * 
	 * @return Returns the property value.
	 */
	public String getPropValue() {
		return propValue;
	}

	/**
	 * Sets old value of the field editor.
	 * 
	 * @param oldValue The oldValue to set.
	 */
	protected void setOldValue(String oldValue) {
		this.oldValue = oldValue;
		this.propValue = oldValue;
		this.displayValue = oldValue;
		markDirty(false);
	}

	/**
	 * Sets property value of the field editor.
	 * 
	 * @param newValue The newValue to set.
	 */
	protected void setPropValue(String newValue) {
		this.oldValue = this.displayValue;
		this.propValue = newValue;
		this.displayValue = newValue;
	}

	protected void setDefaultValue(String newValue) {
		this.oldValue = this.displayValue;
		this.propValue = null;
		this.displayValue = newValue;
	}

	/**
	 * Gets string value of the field editor.
	 * 
	 */
	protected abstract String getStringValue();

	/**
	 * Performs value changes.
	 * 
	 * @param name value name to changed.
	 */
	protected void valueChanged(String name) {
		if (!isLoaded) {
			return;
		}
		String curValue = getPropValue();
		String newValue = getStringValue();
		setPresentsDefaultValue(false);
		if (curValue != null && !curValue.equals(newValue) || (curValue == null && newValue != null)) {
			fireValueChanged(name, curValue, newValue);
			setPropValue(newValue);
			markDirty(true);
		}
	}

	private static boolean refresh = false;

	/**
	 * Marks the field editor is dirty.
	 */
	protected void markDirty(boolean value) {
		isDirty = value;
		if (this.getPage() instanceof BaseStylePreferencePage) {
			if (refresh)
				return;
			else {
				refresh = true;
				Display.getDefault().asyncExec(new Runnable() {

					public void run() {
						if ((BaseStylePreferencePage) getPage() == null)
							return;
						else if (((BaseStylePreferencePage) getPage()).getBuilder() == null
								|| ((BaseStylePreferencePage) getPage()).getBuilder().getShell() == null
								|| ((BaseStylePreferencePage) getPage()).getBuilder().getShell().isDisposed())
							return;
						((BaseStylePreferencePage) getPage()).getBuilder().refreshPagesStatus();
						refresh = false;
					}
				});
			}
		}
	}

	/**
	 * Returns the dirty marker of the field editor.
	 */
	protected boolean isDirty() {
		return isDirty;
		// if ( oldValue.equals( propValue ) )
		// {
		// return false;
		// }
		// return true;
	}

	public boolean hasLocaleValue() {
		if (propValue == null)
			return false;
		else {
			if (isDirty)
				return true;
			else {
				if (this.getPreferenceStore() instanceof StylePreferenceStore) {
					StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
					if (store.hasLocalValue(getPreferenceName()))
						return true;
					else
						return false;
				} else
					return true;
			}
		}
	}
}
