/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionCellEditor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.GroupPropertyHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.property.widgets.BackgroundImageCellEditor;
import org.eclipse.birt.report.designer.internal.ui.views.property.widgets.ComboBoxColorCellEditor;
import org.eclipse.birt.report.designer.internal.ui.views.property.widgets.ComboBoxDimensionCellEditor;
import org.eclipse.birt.report.designer.internal.ui.views.property.widgets.DateTimeCellEditor;
import org.eclipse.birt.report.designer.internal.ui.views.property.widgets.DimensionCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.CTextCellEditor;
import org.eclipse.birt.report.designer.ui.widget.ComboBoxCellEditor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Populates PropertyDescriptor of property sheet page.
 */
public class PropertyEditorFactory {

	private static PropertyEditorFactory instance = new PropertyEditorFactory();

	private static String booleanValues[] = new String[] { "false", "true" }; //$NON-NLS-1$ //$NON-NLS-2$

	private static String booleanDisplayValues[] = new String[] {
			Messages.getString("PropertyEditorFactory.Boolean.False"),
			Messages.getString("PropertyEditorFactory.Boolean.True") }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Avoid instantiation.
	 */
	private PropertyEditorFactory() {
	}

	/**
	 * Gets the single instance of this class.
	 * 
	 * @return instance
	 */
	public static PropertyEditorFactory getInstance() {
		return instance;
	}

	public CellEditor createPropertyEditor(Composite parent, Object o) {
		// create different editors according to property input type
		CellEditor editor = null;

		String[] unitsList = null;

		GroupPropertyHandleProvider handle = GroupPropertyHandleProvider.getInstance();

		// not editable property
		if (handle.isReadOnly(o)) {
			return null;
		}

		String[] values = getChoiceNames(o);
		String[] displayNames = getChoiceDisplayNames(o);
		String value = ((GroupPropertyHandle) o).getStringValue();

		if (value == null) {
			value = ""; //$NON-NLS-1$
		}

		if (handle.isBooleanProperty(o)) {
			if (handle.isEditable(o)) {
				editor = new ComboBoxCellEditor(parent, booleanDisplayValues, booleanValues, SWT.NONE);
			} else {
				editor = new ComboBoxCellEditor(parent, booleanDisplayValues, booleanValues, SWT.READ_ONLY);
			}
			editor.setValue(value);
		} else if (handle.isColorProperty(o)) {
			editor = new ComboBoxColorCellEditor(parent, displayNames, values, SWT.READ_ONLY);

			editor.setValue(value);
		} else if (handle.isDateTimeProperty(o)) {
			editor = new DateTimeCellEditor(parent);
			editor.setValue(value);
		} else if (handle.isFontSizeProperty(o)) {
			editor = new ComboBoxDimensionCellEditor(parent, displayNames, values);

			IChoiceSet choiceSet = DesignEngine.getMetaDataDictionary()
					.getChoiceSet(DesignChoiceConstants.CHOICE_UNITS);
			unitsList = ChoiceSetFactory.getNamefromChoiceSet(choiceSet);
			((ComboBoxDimensionCellEditor) editor).setUnitsList(unitsList);

			DimensionValue dimensionValue = null;
			try {
				dimensionValue = DimensionValue.parse(value);
				if (dimensionValue != null) {
					editor.setValue(dimensionValue.toDisplayString());
					((ComboBoxDimensionCellEditor) editor).setUnits(dimensionValue.getUnits());
				}
			} catch (PropertyValueException e) {
				editor.setValue(value);
			}
		} else if (handle.isDimensionProperty(o)) {
			IChoiceSet choiceSet = ((GroupPropertyHandle) o).getPropertyDefn().getAllowedUnits();
			values = ChoiceSetFactory.getNamefromChoiceSet(choiceSet);

			DimensionValue dimensionValue = null;
			try {

				dimensionValue = DimensionValue.parse(value);
			} catch (PropertyValueException e) {
				// Do nothing
			}
			if (handle.isEditable(o)) {
				editor = new DimensionCellEditor(parent, values, SWT.READ_ONLY);
			} else {
				editor = new DimensionCellEditor(parent, values, SWT.NONE);
			}

			if (dimensionValue != null) {
				((DimensionCellEditor) editor).setUnits(dimensionValue.getUnits());
				editor.setValue(dimensionValue.toDisplayString());
			}
		} else if (handle.isElementRefValue(o)) {
			GroupPropertyHandle propertyHandle = (GroupPropertyHandle) o;
			List handles = propertyHandle.getReferenceableElementList();

			values = new String[handles.size()];
			for (int i = 0; i < handles.size(); i++) {
				values[i] = ((DesignElementHandle) handles.get(i)).getQualifiedName();
			}

			ElementPropertyDefn propDefn = (ElementPropertyDefn) propertyHandle.getPropertyDefn();
			ElementDefn elementDefn = (ElementDefn) propDefn.getTargetElementType();
			assert elementDefn != null;
			if (ReportDesignConstants.STYLE_ELEMENT.equals(elementDefn.getName())) {
				values = filterPreStyles(values);
			}

			editor = new ComboBoxCellEditor(parent, values);
			editor.setValue(value);
		} else if (handle.isExpressionProperty(o)) {
			editor = new ExpressionCellEditor(parent, SWT.READ_ONLY, handle.supportConstantExpression(o));
			editor.setValue(((GroupPropertyHandle) o).getValue());
		} else if (handle.isPassProperty(o)) {
			editor = new CTextCellEditor(parent, SWT.PASSWORD);
			editor.setValue(value);
		} else if (handle.isBackgroundImageProperty(o)) {
			editor = new BackgroundImageCellEditor(parent);
			editor.setValue(value);
		} else if (displayNames.length > 0) {
			if (handle.isEditable(o)) {
				editor = new ComboBoxCellEditor(parent, displayNames, values, SWT.NONE);
			} else {
				editor = new ComboBoxCellEditor(parent, displayNames, values, SWT.READ_ONLY);
			}
			editor.setValue(value);
		} else {
			editor = new CTextCellEditor(parent);
			editor.setValue(value);
		}

		return editor;
	}

	/**
	 * Returns the array of choice names if the property has a choice list;or null
	 * otherwise.
	 * 
	 * @return the list of available choice names.
	 */
	private String[] getChoiceNames(Object o) {
		String[] values = null;

		if (o instanceof GroupPropertyHandle) {
			if (((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices() != null) {
				IChoice[] choices = ((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices().getChoices();
				if (choices.length > 0) {
					values = new String[choices.length];
					for (int i = 0; i < choices.length; i++) {
						// temp: displayname
						values[i] = choices[i].getName();
					}
				}
			}
		}
		if (values == null)
			return new String[] {};

		return values;
	}

	private String[] getChoiceDisplayNames(Object o) {
		String[] values = null;

		if (o instanceof GroupPropertyHandle) {
			if (((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices() != null) {
				IChoice[] choices = ((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices().getChoices();
				if (choices.length > 0) {
					values = new String[choices.length];
					for (int i = 0; i < choices.length; i++) {
						// temp: displayname
						values[i] = choices[i].getDisplayName();
					}
				}
			}
		}
		if (values == null)
			return new String[] {};

		return values;
	}

	/**
	 * Gets the name of the model
	 * 
	 * @param o
	 * @return the name of the input object
	 */
	public String getName(Object o) {
		if (o instanceof String) {
			return (String) o;
		}
		if (o instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) o).getPropertyDefn().getName();
		}
		return "nullname"; //$NON-NLS-1$
	}

	/**
	 * Gets the tool tip that identifies where the value is wet; it returns the
	 * localized text of the following:
	 * <ul>
	 * <li>"Set on this element" if set locally.
	 * <li>"Inherited from xxx" if inherited.
	 * <li>"Inherited from Style xxx" if the value is inherited from a style.
	 * </ul>
	 */
	public String getTooltip(Object o) {
		// TODO: need model support for local or inherited style support
		// if ( o instanceof SimpleValueHandle )
		// {
		// String name = getName( o );
		// if ( ( (SimpleValueHandle) o ).getElementHandle( )
		// .getPrivateStyle( ).getPropertyHandle( name ) != null )
		// {
		// return "Set on this element"; //$NON-NLS-1$
		//
		// }
		//
		// return "Inherited from Style " + name; //$NON-NLS-1$
		//
		// }
		return null;
	}

	/**
	 * @param model
	 * @return the display name for this model instance
	 */
	public String getDisplayName(Object model) {
		if (model instanceof GroupPropertyHandle) {
			return ((GroupPropertyHandle) model).getPropertyDefn().getDisplayName();
		}

		return ""; //$NON-NLS-1$
	}

	private String[] filterPreStyles(String items[]) {
		List preStyles = DesignEngine.getMetaDataDictionary().getPredefinedStyles();
		List preStyleNames = new ArrayList();

		for (int i = 0; i < preStyles.size(); i++) {
			preStyleNames.add(((IPredefinedStyle) preStyles.get(i)).getName());
		}

		List sytleNames = new ArrayList();
		for (int i = 0; i < items.length; i++) {
			if (preStyleNames.indexOf(items[i]) == -1) {
				sytleNames.add(items[i]);
			}
		}

		return (String[]) (sytleNames.toArray(new String[] {}));

	}
}
