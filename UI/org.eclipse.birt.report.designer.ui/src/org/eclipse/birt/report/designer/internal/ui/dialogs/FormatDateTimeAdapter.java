/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

import java.util.Date;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.ui.dialogs.FormatBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.FormatDateTimePattern;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TimeFormatValue;

import com.ibm.icu.util.ULocale;

/**
 * FormatDateTimeAdapter
 */
public final class FormatDateTimeAdapter extends FormatAdapter {

	private static final String[] DATETIME_FORMAT_TYPES = { DesignChoiceConstants.DATETIME_FORMAT_TYPE_GENERAL_DATE,
			DesignChoiceConstants.DATETIME_FORMAT_TYPE_LONG_DATE,
			DesignChoiceConstants.DATETIME_FORMAT_TYPE_MEDIUM_DATE,
			DesignChoiceConstants.DATETIME_FORMAT_TYPE_SHORT_DATE, DesignChoiceConstants.DATETIME_FORMAT_TYPE_LONG_TIME,
			DesignChoiceConstants.DATETIME_FORMAT_TYPE_MEDIUM_TIME,
			DesignChoiceConstants.DATETIME_FORMAT_TYPE_SHORT_TIME };

	private static final String[] DATE_FORMAT_TYPES = { DesignChoiceConstants.DATE_FORMAT_TYPE_GENERAL_DATE,
			DesignChoiceConstants.DATE_FORMAT_TYPE_LONG_DATE, DesignChoiceConstants.DATE_FORMAT_TYPE_MEDIUM_DATE,
			DesignChoiceConstants.DATE_FORMAT_TYPE_SHORT_DATE };

	private static final String[] TIME_FORMAT_TYPES = { DesignChoiceConstants.TIME_FORMAT_TYPE_LONG_TIME,
			DesignChoiceConstants.TIME_FORMAT_TYPE_MEDIUM_TIME, DesignChoiceConstants.TIME_FORMAT_TYPE_SHORT_TIME };

	private static String UNFORMATTED_DISPLAYNAME, CUSTOM, UNFORMATTED_NAME;

	private int type;

	private Date defaultDate = new Date();

	private String[][] categoryChoiceArray = null;

	private String[] formatTypes = null;

	/**
	 * Constructor
	 *
	 * @param type date time type
	 */
	public FormatDateTimeAdapter(int type) {
		this.type = type;
		init();
	}

	private void init() {
		switch (type) {
		case FormatBuilder.DATETIME:
			UNFORMATTED_DISPLAYNAME = DesignChoiceConstants.DATETIME_FORMAT_TYPE_UNFORMATTED;
			CUSTOM = DesignChoiceConstants.DATETIME_FORMAT_TYPE_CUSTOM;
			UNFORMATTED_NAME = DateFormatter.DATETIME_UNFORMATTED;
			break;
		case FormatBuilder.DATE:
			UNFORMATTED_DISPLAYNAME = DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED;
			CUSTOM = DesignChoiceConstants.DATE_FORMAT_TYPE_CUSTOM;
			UNFORMATTED_NAME = DateFormatter.DATE_UNFORMATTED;
			break;
		case FormatBuilder.TIME:
			UNFORMATTED_DISPLAYNAME = "Unformatted"; //$NON-NLS-1$
			CUSTOM = DesignChoiceConstants.TIME_FORMAT_TYPE_CUSTOM;
			UNFORMATTED_NAME = DateFormatter.TIME_UNFORMATTED;
			break;
		}
	}

	/**
	 * Method to return the date time format types
	 *
	 * @return Return the date time format types
	 */
	public String[] getSimpleDateTimeFormatTypes() {
		if (type == FormatBuilder.DATETIME) {
			return DATETIME_FORMAT_TYPES;
		} else if (type == FormatBuilder.DATE) {
			return DATE_FORMAT_TYPES;
		} else if (type == FormatBuilder.TIME) {
			return TIME_FORMAT_TYPES;
		} else {
			return new String[0];
		}
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 *
	 * @return Return the choiceArray of this choice element from model.
	 */
	public String[][] getFormatTypeChoiceSet() {
		String structName, property;

		if (type == FormatBuilder.DATETIME) {
			structName = DateTimeFormatValue.FORMAT_VALUE_STRUCT;
			property = FormatValue.CATEGORY_MEMBER;
		} else if (type == FormatBuilder.DATE) {
			structName = DateFormatValue.FORMAT_VALUE_STRUCT;
			property = FormatValue.CATEGORY_MEMBER;
		} else {
			structName = TimeFormatValue.FORMAT_VALUE_STRUCT;
			property = FormatValue.CATEGORY_MEMBER;
		}

		return getChoiceArray(structName, property);
	}

	@Override
	public String[][] initChoiceArray() {
		if (categoryChoiceArray == null) {
			switch (type) {
			case FormatBuilder.DATETIME:
			default:
				categoryChoiceArray = getChoiceArray(DateTimeFormatValue.FORMAT_VALUE_STRUCT,
						FormatValue.CATEGORY_MEMBER);
				break;
			case FormatBuilder.DATE:
				categoryChoiceArray = getChoiceArray(DateFormatValue.FORMAT_VALUE_STRUCT,
						FormatValue.CATEGORY_MEMBER);
				break;
			case FormatBuilder.TIME:
				categoryChoiceArray = getChoiceArray(TimeFormatValue.FORMAT_VALUE_STRUCT,
						FormatValue.CATEGORY_MEMBER);
				break;
			}
		}
		return categoryChoiceArray;
	}

	@Override
	public String getCategory4DisplayName(String displayName) {
		if (initChoiceArray() != null) {
			for (int i = 0; i < categoryChoiceArray.length; i++) {
				if (formatTypes[i].equals(displayName)) {
					return categoryChoiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	@Override
	public String getDisplayName4Category(String category) {
		return ChoiceSetFactory.getStructDisplayName(DateTimeFormatValue.FORMAT_VALUE_STRUCT,
				FormatValue.CATEGORY_MEMBER, category);
	}

	@Override
	public String[] getFormatTypes(ULocale locale) {
		if (initChoiceArray() != null) {
			formatTypes = new String[categoryChoiceArray.length];

			for (int i = 0; i < categoryChoiceArray.length; i++) {
				String fmtStr = ""; //$NON-NLS-1$
				String category = categoryChoiceArray[i][1];
				if (category.equals(CUSTOM) || category.equals(UNFORMATTED_DISPLAYNAME)
						|| category.startsWith("Date Picker") || category.startsWith("Time Picker")) {
					fmtStr = categoryChoiceArray[i][0];
				} else {
					// uses UI specified display names.
					String pattern = FormatDateTimePattern.getPatternForCategory(category);
					// FIXME There maybe waste a lot of time.
					fmtStr = new DateFormatter(pattern, locale).format(defaultDate);
				}
				formatTypes[i] = fmtStr;
			}

		} else {
			formatTypes = new String[0];
		}
		return formatTypes;
	}

	@Override
	public int getIndexOfCategory(String category) {
		if (initChoiceArray() != null) {
			for (int i = 0; i < categoryChoiceArray.length; i++) {
				if (categoryChoiceArray[i][1].equals(category)) {
					return i;
				}
			}
		}
		return 0;
	}

	@Override
	public String getPattern4DisplayName(String displayName, ULocale locale) {
		String category = ChoiceSetFactory.getStructPropValue(DateTimeFormatValue.FORMAT_VALUE_STRUCT,
				FormatValue.CATEGORY_MEMBER, displayName);
		return FormatDateTimePattern.getPatternForCategory(category);
	}

	/**
	 * Get the display name of unformatted category
	 *
	 * @return Return the display name of unformatted category
	 */
	public String getUnformattedCategoryDisplayName() {
		return UNFORMATTED_DISPLAYNAME;
	}

	/**
	 * Get the category name of custom
	 *
	 * @return Return the category name of custom
	 */
	public String getCustomCategoryName() {
		return CUSTOM;
	}

	/**
	 * Get the category name of unformatted
	 *
	 * @return Return the category name of unformatted
	 */
	public String getUnformattedCategoryName() {
		return UNFORMATTED_NAME;
	}

}
