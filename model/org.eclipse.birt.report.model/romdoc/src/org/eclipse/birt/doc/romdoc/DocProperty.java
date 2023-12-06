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

package org.eclipse.birt.doc.romdoc;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Documentation property object
 *
 * @since 3.3
 *
 */
public class DocProperty extends DocObject {
	PropertyDefn defn;
	HashMap<String, String> notes = null;
	ArrayList<DocChoice> choices = null;

	/**
	 * Constructor
	 *
	 * @param propDefn property definition
	 */
	public DocProperty(PropertyDefn propDefn) {
		defn = propDefn;
		IChoiceSet choiceSet = defn.getChoices();
		if (choiceSet == null) {
			return;
		}
		choices = new ArrayList<DocChoice>();
		IChoice set[] = choiceSet.getChoices();
		for (int i = 0; i < set.length; i++) {
			choices.add(new DocChoice(set[i]));
		}
	}

	@Override
	public String getName() {
		return defn.getName();
	}

	/**
	 * Get property type
	 *
	 * @return property type
	 */
	public String getType() {
		String type;
		if (defn.getTypeCode() == IPropertyType.STRUCT_TYPE) {
			type = makeStructureLink(defn.getStructDefn(), "element") + //$NON-NLS-1$
					" Structure"; //$NON-NLS-1$
		} else if (defn.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			type = makeElementLink(defn.getTargetElementType().getName(), "element") + //$NON-NLS-1$
					" Reference"; //$NON-NLS-1$
		} else if (defn.getTypeCode() == IPropertyType.CHOICE_TYPE) {
			type = makeTypeLink(defn.getType(), "element") + //$NON-NLS-1$
					" (" + defn.getChoices().getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			type = makeTypeLink(defn.getType(), "element"); //$NON-NLS-1$
		}
		if (defn.isList()) {
			type = "List of " + type + "s"; //$NON-NLS-1$//$NON-NLS-2$
		}
		return type;
	}

	/**
	 * Get property since value
	 *
	 * @return Return the property since value.
	 */
	public String getSince() {
		// Style is special

		if (defn.getName().equals("style")) { // $NON-NLS-1$
			return "1.0"; //$NON-NLS-1$
		}

		return defn.getSince();
	}

	/**
	 * Get required value of the property
	 *
	 * @return Return the required value of the property.
	 */
	public String getRequired() {
		return yesNo(defn.isValueRequired());
	}

	/**
	 * Get the display name of the property
	 *
	 * @return Return the display name
	 */
	public String getDisplayName() {
		return defn.getDisplayName();
	}

	/**
	 * Get the JS return type
	 *
	 * @return Return the JSType (currently unused, default return value null)
	 */
	public String getJSType() {
		return null;
	}

	/**
	 * Get the default return value
	 *
	 * @return Return the default return value
	 */
	public String getDefaultValue() {
		String note = getNote("Default value"); //$NON-NLS-1$
		if (note != null) {
			return note;
		}
		Object value = defn.getDefault();
		if (value != null) {
			return value.toString();
		}
		return "None"; //$NON-NLS-1$
	}

	/**
	 * Get the inherited value
	 *
	 * @return Return the inherited value
	 */
	public String getInherited() {
		return yesNo(((ElementPropertyDefn) defn).canInherit());
	}

	/**
	 * Get the runtime set table
	 *
	 * @return Return the runtime set table
	 */
	public String getRuntimeSettable() {
		return yesNo(defn.isRuntimeSettable());
	}

	/**
	 * Property is choice property
	 *
	 * @return Return if it a cjoice property
	 */
	public boolean hasChoices() {
		return defn.getChoices() != null;
	}

	/**
	 * Get the visibility of the element
	 *
	 * @param element document element
	 * @return Return the visibility of the element
	 */
	public String getVisibility(DocElement element) {
		if (element.getElementDefn().isPropertyReadOnly(defn.getName())) {
			return "Read-only"; //$NON-NLS-1$
		}
		if (element.getElementDefn().isPropertyVisible(defn.getName())) {
			return "Visible"; //$NON-NLS-1$
		}
		return "Hidden"; //$NON-NLS-1$
	}

	/**
	 * Get the group property of the element
	 *
	 * @return Return the group property of the element
	 */
	public String getGroup() {
		String group = ((ElementPropertyDefn) defn).getGroupName();
		if (group == null) {
			return "Top"; //$NON-NLS-1$
		}
		return group;
	}

	/**
	 * @param key
	 * @param note
	 */
	public void addNote(String key, String note) {
		if (notes == null) {
			notes = new HashMap<String, String>();
		}
		notes.put(key.toLowerCase(), note);
	}

	/**
	 * Get the note of the document element based on the key
	 *
	 * @param key key of the note
	 * @return Return the note of the document element based on the key
	 */
	public String getNote(String key) {
		if (notes == null) {
			return null;
		}
		return notes.get(key.toLowerCase());
	}

	/**
	 * Find choice of the document element
	 *
	 * @param name name value of the choice
	 * @return Return the choice value
	 */
	public DocChoice findChoice(String name) {
		if (choices == null) {
			return null;
		}
		for (int i = 0; i < choices.size(); i++) {
			DocChoice choice = choices.get(i);
			if (choice.getName().equals(name)) {
				return choice;
			}
		}
		return null;
	}

	/**
	 * Get a list of all choices
	 *
	 * @return Return a list of all choices
	 */
	public AbstractList<DocChoice> getChoices() {
		return choices;
	}

	/**
	 * Check if the element is an expression
	 *
	 * @return Return the check result whether the element is an expression
	 */
	public boolean isExpression() {
		return defn.getTypeCode() == IPropertyType.EXPRESSION_TYPE;
	}

	/**
	 * Get the context
	 *
	 * @return Return the context
	 */
	public String getContext() {
		return defn.getContext();
	}

	/**
	 * Get the return type
	 *
	 * @return Return the return type
	 */
	public String getReturnType() {
		if (defn.getReturnType() == null) {
			return "None";
		}
		return defn.getReturnType();
	}
}
