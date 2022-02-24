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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.metadata.ChoiceSet;

/**
 * ChoiceSetFactory provides common interface to access all kinds of collection
 * on given property.
 */

public class ChoiceSetFactory {

	public static final String CHOICE_NONE = Messages.getString("ChoiceSetFactory.choice.None"); //$NON-NLS-1$

	public static final String CHOICE_AUTO = Messages.getString("ChoiceSetFactory.choice.Auto"); //$NON-NLS-1$

	/**
	 * Gets the collection that given property value can selected from them.
	 * 
	 * @param property DE Property key.
	 * @return The ChoiceSet instance contains all the allowed values.
	 * @deprecated Use getDEChoiceSet( String property ,String elementName) instead
	 */
	public static IChoiceSet getDEChoiceSet(String property) {
		String unitKey = DesignChoiceConstants.CHOICE_UNITS;
		if (AttributeConstant.BACKGROUND_COLOR.equals(property)) {
			unitKey = IColorConstants.COLORS_CHOICE_SET;
		} else if (AttributeConstant.FONT_COLOR.equals(property)) {
			unitKey = IColorConstants.COLORS_CHOICE_SET;
		} else if (AttributeConstant.FONT_SIZE.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_FONT_SIZE;
		} else if (AttributeConstant.FONT_FAMILY.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_FONT_FAMILY;
		} else if (AttributeConstant.TEXT_FORMAT.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_TEXT_CONTENT_TYPE;
		} else if (AttributeConstant.BORDER_STYLE.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_LINE_STYLE;
		} else if (AttributeConstant.BORDER_WIDTH.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_LINE_WIDTH;
		} else if (SortKey.DIRECTION_MEMBER.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_SORT_DIRECTION;
		} else if (FilterCondition.OPERATOR_MEMBER.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_FILTER_OPERATOR;
		} else if (StyleHandle.VERTICAL_ALIGN_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_VERTICAL_ALIGN;
		} else if (StyleHandle.TEXT_ALIGN_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_TEXT_ALIGN;
		} else if (MasterPageHandle.ORIENTATION_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_ORIENTATION;
		} else if (MasterPageHandle.TYPE_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_SIZE;
		} else if (GroupHandle.INTERVAL_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_INTERVAL;
		} else if (StyleHandle.PAGE_BREAK_BEFORE_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_BREAK_BEFORE;
		} else if (StyleHandle.PAGE_BREAK_AFTER_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER;
		}
		// bidi_hcg
		else if (StyleHandle.TEXT_DIRECTION_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_BIDI_DIRECTION;
		}
		// else if ( StyleHandle.PAGE_BREAK_INSIDE_PROP.equals( property ) )
		// {
		// unitKey = DesignChoiceConstants.CHOICE_PAGE_BREAK_INSIDE;
		// }
		return DEUtil.getMetaDataDictionary().getChoiceSet(unitKey);

	}

	/**
	 * Gets the collection that given property value can selected from them.
	 * 
	 * @param elementName The name of the element.
	 * @param property    DE Property key.
	 * @return The ChoiceSet instance contains all the allowed values.
	 */

	public static IChoiceSet getElementChoiceSet(String elementName, String property) {
		IElementPropertyDefn propertyDefn = DEUtil.getMetaDataDictionary().getElement(elementName)
				.getProperty(property);

		return propertyDefn.getAllowedChoices();
	}

	/**
	 * Gets the dimension collection that given property value can selected from
	 * them.
	 * 
	 * @param elementName The name of the element.
	 * @param property    DE Property key.
	 * @return The ChoiceSet instance contains all the allowed values.
	 */
	public static IChoiceSet getDimensionChoiceSet(String elementName, String property) {
		IElementPropertyDefn propertyDefn = DEUtil.getMetaDataDictionary().getElement(elementName)
				.getProperty(property);
		if (propertyDefn.getTypeCode() == IPropertyType.DIMENSION_TYPE) {
			return propertyDefn.getAllowedUnits();
		}
		return null;
	}

	/**
	 * Gets the collection that given structure property value can selected from
	 * them.
	 * 
	 * @param elementName The name of the element.
	 * @param property    DE Property key.
	 * @return The ChoiceSet instance contains all the allowed values.
	 */
	public static IChoiceSet getStructChoiceSet(String structName, String property) {
		return getStructChoiceSet(structName, property, false);
	}

	/**
	 * Gets the collection that given structure property value can selected from
	 * them.
	 * 
	 * @param structName             The name of the element.
	 * @param property               DE Property key.
	 * @param removeNoSupportFilters indicate to shorter the choices excluding
	 *                               Top/Bottom N and others
	 * @return The ChoiceSet instance contains all the allowed values.
	 */
	public static IChoiceSet getStructChoiceSet(String structName, String property, boolean removeNoSupportFilters) {
		IPropertyDefn propertyDefn = DEUtil.getMetaDataDictionary().getStructure(structName).findProperty(property);
		IChoiceSet cs = propertyDefn.getAllowedChoices();
		if (removeNoSupportFilters) {
			return removeNoSupportedChoices(cs);
		}
		return cs;
	}

	private static IChoiceSet removeNoSupportedChoices(IChoiceSet cs) {
		if (cs == null) {
			return null;
		}
		ArrayList<String> notSupportedList = new ArrayList<String>();
		notSupportedList.add(DesignChoiceConstants.FILTER_OPERATOR_TOP_N);
		notSupportedList.add(DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N);
		notSupportedList.add(DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT);
		notSupportedList.add(DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT);
		IChoice[] choiceList = cs.getChoices();
		ArrayList<IChoice> newChoiceList = new ArrayList<IChoice>();
		for (int i = 0; i < choiceList.length; i++) {
			if (!notSupportedList.contains(choiceList[i].getName())) {
				newChoiceList.add(choiceList[i]);
			}
		}
		ChoiceSet newcs = new ChoiceSet(cs.getName());
		newcs.setChoices(newChoiceList.toArray(new IChoice[newChoiceList.size()]));
		return newcs;
	}

	/**
	 * Gets all displayNames that a given ChoiceSet instance contained.
	 * 
	 * @param choiceSet The ChoiceSet instance.
	 * @return A String array contains displayNames.
	 */
	public static String[] getDisplayNamefromChoiceSet(IChoiceSet choiceSet) {
		return getDisplayNamefromChoiceSet(choiceSet, null);
	}

	/**
	 * Gets all displayNames that a given ChoiceSet instance contained.
	 * 
	 * @param choiceSet  The ChoiceSet instance.
	 * @param comparator The sort comparator.
	 * @return A String array contains displayNames.
	 */
	public static String[] getDisplayNamefromChoiceSet(IChoiceSet choiceSet, Comparator comparator) {
		String[] displayNames = new String[0];
		if (choiceSet == null) {
			return displayNames;
		}
		IChoice[] choices = choiceSet.getChoices(comparator);

		if (choices == null)
			return displayNames;

		displayNames = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			displayNames[i] = choices[i].getDisplayName();
		}
		return displayNames;
	}

	/**
	 * Gets all names that a given ChoiceSet instance contained.
	 * 
	 * @param choiceSet The ChoiceSet instance.
	 * @return A String array contains names.
	 */
	public static String[] getNamefromChoiceSet(IChoiceSet choiceSet) {
		String[] names = new String[0];
		if (choiceSet == null)
			return names;
		IChoice[] choices = choiceSet.getChoices();
		if (choices == null)
			return names;

		names = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			names[i] = choices[i].getName();
		}
		return names;
	}

	/**
	 * Gets property 's display names given element name and the property name.
	 * 
	 * @param elemenName The design element name.
	 * @param property   The property name.
	 * @return The given property 's display names
	 */
	public static String[] getPropertyDisplayNames(String elementName, String property) {
		IChoiceSet choiceSet = getElementChoiceSet(elementName, property);
		return getDisplayNamefromChoiceSet(choiceSet);
	}

	/**
	 * Gets property 's display name given element name, property name and the
	 * property's value.
	 * 
	 * @param elemenName The design element name.
	 * @param property   The property name.
	 * @param valule     The property 's value.
	 * @return The given property 's display name
	 */
	public static String getPropDisplayName(String elementName, String property, String value) {
		IChoiceSet set = getElementChoiceSet(elementName, property);
		return getDisplayNameFromChoiceSet(value, set);
	}

	/**
	 * Gets structure property 's display name given element name, property name and
	 * the property's value.
	 * 
	 * @param structName The structure name.
	 * @param memberName The member name.
	 * @param valule     The property 's value.
	 * @return The given property 's display name
	 */

	public static String getStructDisplayName(String structName, String memberName, String value) {
		IChoiceSet set = getStructChoiceSet(structName, memberName);
		return getDisplayNameFromChoiceSet(value, set);
	}

	/**
	 * Gets property value given element name, property name and its the property's
	 * display name.
	 * 
	 * @param elemenName  The design element name.
	 * @param property    The property name.
	 * @param displayName The property 's display name.
	 * @return The given property 's value
	 */
	public static String getPropValue(String elementName, String property, String displayName) {
		IChoiceSet set = getElementChoiceSet(elementName, property);
		return getValueFromChoiceSet(displayName, set);
	}

	/**
	 * Gets structure property value given element name, property name and its the
	 * property's display name.
	 * 
	 * @param structName  The design element name.
	 * @param memberName  The property name.
	 * @param displayName The property 's display name.
	 * @return The given property 's value
	 */
	public static String getStructPropValue(String structName, String memberName, String displayName) {
		IChoiceSet set = getStructChoiceSet(structName, memberName);
		return getValueFromChoiceSet(displayName, set);
	}

	/**
	 * Gets UI display name from a choice set given the the value and the choice set
	 * name.
	 * 
	 * @param value The value corresponding to the display name.
	 * @param set   The choice set name from which to get display name.
	 * @return The display name of the given value
	 */
	public static String getDisplayNameFromChoiceSet(String value, IChoiceSet set) {
		String name = value;
		if (set == null) {
			return name;
		}
		IChoice[] choices = set.getChoices();
		if (choices == null) {
			return name;
		}
		for (int i = 0; i < choices.length; i++) {
			if (choices[i].getName().equals(value)) {
				return choices[i].getDisplayName();
			}
		}
		return name;
	}

	/**
	 * Gets the value from a choice set given the UI display name and the choice set
	 * name.
	 * 
	 * @param displayName The UI display name corresponding to the value.
	 * @param set         The choice set name from which to get property value.
	 * @return The value of the given UI display name.
	 */
	public static String getValueFromChoiceSet(String displayName, IChoiceSet set) {
		String value = displayName;
		if (set == null) {
			return value;
		}
		IChoice[] choices = set.getChoices();
		if (choices == null) {
			return value;
		}
		for (int i = 0; i < choices.length; i++) {
			if (choices[i].getDisplayName().equals(displayName)) {
				return choices[i].getName();
			}
		}
		return value;
	}

	/**
	 * Gets the collection that given property value can selected from them.
	 * 
	 * @param property DE Property key.
	 * @return A String array contains all the allowed values.
	 * @deprecated Use getDEChoiceSet( String property ,String elementName) instead
	 */

	public static Object[] getChoiceSet(String property) {
		// The dataSet has different access method.
		if (AttributeConstant.DATASET.equals(property)) {
			return getDataSets();
		}

		String unitKey = DesignChoiceConstants.CHOICE_UNITS;
		if (AttributeConstant.BACKGROUND_COLOR.equals(property)) {
			unitKey = IColorConstants.COLORS_CHOICE_SET;
		} else if (AttributeConstant.FONT_COLOR.equals(property)) {
			unitKey = IColorConstants.COLORS_CHOICE_SET;
		} else if (AttributeConstant.FONT_SIZE.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_FONT_SIZE;
		} else if (AttributeConstant.FONT_FAMILY.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_FONT_FAMILY;
		} else if (AttributeConstant.TEXT_FORMAT.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_TEXT_CONTENT_TYPE;
		} else if (AttributeConstant.BORDER_STYLE.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_LINE_STYLE;
		} else if (AttributeConstant.BORDER_WIDTH.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_LINE_WIDTH;
		} else if (SortKey.DIRECTION_MEMBER.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_SORT_DIRECTION;
		} else if (FilterCondition.OPERATOR_MEMBER.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_FILTER_OPERATOR;
		} else if (StyleHandle.VERTICAL_ALIGN_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_VERTICAL_ALIGN;
		} else if (StyleHandle.TEXT_ALIGN_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_TEXT_ALIGN;
		} else if (MasterPageHandle.ORIENTATION_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_ORIENTATION;
		} else if (MasterPageHandle.TYPE_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_SIZE;
		} else if (GroupHandle.INTERVAL_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_INTERVAL;
		} else if (StyleHandle.PAGE_BREAK_BEFORE_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_BREAK_BEFORE;
		} else if (StyleHandle.PAGE_BREAK_AFTER_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER;
		} else if (StyleHandle.PAGE_BREAK_INSIDE_PROP.equals(property)) {
			unitKey = DesignChoiceConstants.CHOICE_PAGE_BREAK_INSIDE;
		}
		return getUnitChoiceSet(unitKey);
	}

	/**
	 * Gets the collection that DE provides.
	 * 
	 * @param unitKey Choice type key.
	 * @return A String array contains all the allowed values.
	 */

	private static Object[] getUnitChoiceSet(String unitKey) {
		ArrayList list = new ArrayList();
		IChoiceSet choiceSet = DEUtil.getMetaDataDictionary().getChoiceSet(unitKey);
		if (choiceSet != null) {
			IChoice[] choices = choiceSet.getChoices();
			for (int i = 0; i < choices.length; i++) {
				list.add(choices[i]);
			}
		}
		return list.toArray(new IChoice[0]);
	}

	/**
	 * Gets all the DataSets available.
	 * 
	 * @return A String array contains all the DataSets.
	 */
	public static String[] getDataSets() {
		ArrayList list = new ArrayList();

		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();

		for (Iterator iterator = handle.getVisibleDataSets().iterator(); iterator.hasNext();) {
			DataSetHandle DataSetHandle = (DataSetHandle) iterator.next();
			list.add(DataSetHandle.getQualifiedName());
		}

		list.addAll(getLinkedDataSetNames());

		return (String[]) list.toArray(new String[0]);
	}

	public static List<String> getLinkedDataSetNames() {
		LinkedDataSetAdapter adapter = new LinkedDataSetAdapter();
		return adapter.getVisibleLinkedDataSets();
	}

	/**
	 * Gets all the Cubes available.
	 * 
	 * @return A String array contains all the Cubs.
	 */
	public static String[] getCubes() {
		ArrayList list = new ArrayList();

		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();

		for (Iterator iterator = handle.getVisibleCubes().iterator(); iterator.hasNext();) {
			CubeHandle CubeHandle = (CubeHandle) iterator.next();
			list.add(CubeHandle.getQualifiedName());
		}
		list.addAll(getLinkedDataSetNames());

		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * Gets all the MasterPages available.
	 * 
	 * @return A String array contains all the MasterPages.
	 */
	public static String[] getMasterPages() {
		ArrayList list = new ArrayList();
		list.add(CHOICE_NONE);
		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		SlotHandle pages = handle.getMasterPages();
		if (pages != null) {
			Iterator iterator = pages.iterator();
			while (iterator.hasNext()) {
				ReportElementHandle elementHandle = (ReportElementHandle) iterator.next();
				list.add(elementHandle.getQualifiedName());
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * Gets all the Styles available.
	 * 
	 * @return A String array contains all the Styles.
	 */
	public static String[] getStyles() {
		ArrayList list = new ArrayList();
		list.add(CHOICE_NONE);

		Iterator iterator = DEUtil.getStyles();

		if (iterator != null) {
			while (iterator.hasNext()) {
				StyleHandle styleHandle = (StyleHandle) iterator.next();
				list.add(styleHandle.getName());
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * Gets all available themes .
	 * 
	 * @return A String array contains all available themes.
	 */
	public static String[] getThemes() {
		ArrayList list = new ArrayList();
		list.add(CHOICE_NONE);

		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		Iterator iterator = handle.getVisibleThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL).iterator();

		if (iterator != null) {
			while (iterator.hasNext()) {
				ReportElementHandle elementHandle = (ReportElementHandle) iterator.next();
				list.add(elementHandle.getQualifiedName());
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	public static String[] getReportItemThemes(String type) {
		ArrayList list = new ArrayList();
		list.add(CHOICE_NONE);

		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		Iterator iterator = handle.getVisibleReportItemThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL, type).iterator();

		if (iterator != null) {
			while (iterator.hasNext()) {
				ReportElementHandle elementHandle = (ReportElementHandle) iterator.next();
				list.add(elementHandle.getQualifiedName());
			}
		}
		return (String[]) list.toArray(new String[0]);
	}

	/**
	 * Gets the default format value given the struct name of format property .
	 * 
	 * @param structName The struct name of the format property.
	 * @return formatValue = new String[2]. The default format value, String[0]:
	 *         default category; String[1]: default pattern.
	 */
	public static String[] getDefaultFormatValue(String structName) {
		String[] formatValue = new String[2];
		IStructureDefn def = DEUtil.getMetaDataDictionary().getStructure(structName);
		formatValue[0] = (String) def.getMember(FormatValue.CATEGORY_MEMBER).getDefault();
		formatValue[1] = (String) def.getMember(FormatValue.PATTERN_MEMBER).getDefault();
		return formatValue;
	}
}
