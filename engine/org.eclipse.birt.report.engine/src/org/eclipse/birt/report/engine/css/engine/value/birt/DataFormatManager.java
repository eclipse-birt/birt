/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.css.engine.value.birt;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.value.AbstractValueManager;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class DataFormatManager extends AbstractValueManager {

	static DataFormatValue DEFAULT = new DataFormatValue();

	public DataFormatManager() {
	}

	@Override
	public String getPropertyName() {
		return BIRTConstants.BIRT_STYLE_DATA_FORMAT;
	}

	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		return null;
	}

	@Override
	public Value getDefaultValue() {
		return DEFAULT;
	}

	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	@Override
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {

		assert value instanceof DataFormatValue;

		DataFormatValue fv = (DataFormatValue) value;
		DataFormatValue cv = fv.clone();
		CSSStylableElement parent = (CSSStylableElement) elt.getParent();
		if (parent != null) {
			DataFormatValue pcv = parent.getComputedStyle().getDataFormat();
			if (pcv != null) {
				String pattern = cv.getStringPattern();
				String locale = cv.getStringLocale();
				if (locale == null && pattern == null) {
					cv.setStringFormat(pcv.getStringPattern(), pcv.getStringLocale());
				}
				pattern = cv.getNumberPattern();
				locale = cv.getNumberLocale();
				if (locale == null && pattern == null) {
					cv.setNumberFormat(pcv.getNumberPattern(), pcv.getNumberLocale());
				}
				pattern = cv.getDatePattern();
				locale = cv.getDateLocale();
				if (locale == null && pattern == null) {
					cv.setDateFormat(pcv.getDatePattern(), pcv.getDateLocale());
				}
				pattern = cv.getTimePattern();
				locale = cv.getTimeLocale();
				if (locale == null && pattern == null) {
					cv.setTimeFormat(pcv.getTimePattern(), pcv.getTimeLocale());
				}
				pattern = cv.getDateTimePattern();
				locale = cv.getDateTimeLocale();
				if (locale == null && pattern == null) {
					cv.setDateTimeFormat(pcv.getDateTimePattern(), pcv.getDateTimeLocale());
				}
			}
		}
		return cv;
	}
}
