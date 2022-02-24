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

public class DocProperty extends DocObject {
	PropertyDefn defn;
	HashMap notes = null;
	ArrayList choices = null;

	public DocProperty(PropertyDefn propDefn) {
		defn = propDefn;
		IChoiceSet choiceSet = defn.getChoices();
		if (choiceSet == null)
			return;
		choices = new ArrayList();
		IChoice set[] = choiceSet.getChoices();
		for (int i = 0; i < set.length; i++) {
			choices.add(new DocChoice(set[i]));
		}
	}

	public String getName() {
		return defn.getName();
	}

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
		} else
			type = makeTypeLink(defn.getType(), "element"); //$NON-NLS-1$
		if (defn.isList())
			type = "List of " + type + "s"; //$NON-NLS-1$//$NON-NLS-2$
		return type;
	}

	public String getSince() {
		// Style is special

		if (defn.getName().equals("style")) //$NON-NLS-1$
			return "1.0"; //$NON-NLS-1$

		return defn.getSince();
	}

	public String getRequired() {
		return yesNo(defn.isValueRequired());
	}

	public String getDisplayName() {
		return defn.getDisplayName();
	}

	public String getJSType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDefaultValue() {
		String note = getNote("Default value"); //$NON-NLS-1$
		if (note != null)
			return note;
		Object value = defn.getDefault();
		if (value != null)
			return value.toString();
		return "None"; //$NON-NLS-1$
	}

	public String getInherited() {
		return yesNo(((ElementPropertyDefn) defn).canInherit());
	}

	public String getRuntimeSettable() {
		return yesNo(defn.isRuntimeSettable());
	}

	public boolean hasChoices() {
		return defn.getChoices() != null;
	}

	public String getVisibility(DocElement element) {
		if (element.getElementDefn().isPropertyReadOnly(defn.getName()))
			return "Read-only"; //$NON-NLS-1$
		if (element.getElementDefn().isPropertyVisible(defn.getName()))
			return "Visible"; //$NON-NLS-1$
		return "Hidden"; //$NON-NLS-1$
	}

	public String getGroup() {
		String group = ((ElementPropertyDefn) defn).getGroupName();
		if (group == null)
			return "Top"; //$NON-NLS-1$
		return group;
	}

	public void addNote(String key, String note) {
		if (notes == null)
			notes = new HashMap();
		notes.put(key.toLowerCase(), note);
	}

	public String getNote(String key) {
		if (notes == null)
			return null;
		return (String) notes.get(key.toLowerCase());
	}

	public DocChoice findChoice(String name) {
		if (choices == null)
			return null;
		for (int i = 0; i < choices.size(); i++) {
			DocChoice choice = (DocChoice) choices.get(i);
			if (choice.getName().equals(name))
				return choice;
		}
		return null;
	}

	public AbstractList getChoices() {
		return choices;
	}

	public boolean isExpression() {
		return defn.getTypeCode() == IPropertyType.EXPRESSION_TYPE;
	}

	public String getContext() {
		return defn.getContext();
	}

	public String getReturnType() {
		if (defn.getReturnType() == null)
			return "None";
		return defn.getReturnType();
	}
}
