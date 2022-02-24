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
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class DisplayManager extends IdentifierManager {
	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_INLINE_VALUE, CSSValueConstants.INLINE_VALUE);
		values.put(CSSConstants.CSS_BLOCK_VALUE, CSSValueConstants.BLOCK_VALUE);
		values.put(CSSConstants.CSS_LIST_ITEM_VALUE, CSSValueConstants.LIST_ITEM_VALUE);
		values.put(CSSConstants.CSS_RUN_IN_VALUE, CSSValueConstants.RUN_IN_VALUE);
		values.put(CSSConstants.CSS_INLINE_BLOCK_VALUE, CSSValueConstants.INLINE_BLOCK_VALUE);
		values.put(CSSConstants.CSS_TABLE_VALUE, CSSValueConstants.TABLE_VALUE);
		values.put(CSSConstants.CSS_INLINE_TABLE_VALUE, CSSValueConstants.INLINE_TABLE_VALUE);
		values.put(CSSConstants.CSS_TABLE_ROW_GROUP_VALUE, CSSValueConstants.TABLE_ROW_GROUP_VALUE);
		values.put(CSSConstants.CSS_TABLE_HEADER_GROUP_VALUE, CSSValueConstants.TABLE_HEADER_GROUP_VALUE);
		values.put(CSSConstants.CSS_TABLE_FOOTER_GROUP_VALUE, CSSValueConstants.TABLE_FOOTER_GROUP_VALUE);
		values.put(CSSConstants.CSS_TABLE_ROW_VALUE, CSSValueConstants.TABLE_ROW_VALUE);
		values.put(CSSConstants.CSS_TABLE_COLUMN_GROUP_VALUE, CSSValueConstants.TABLE_COLUMN_GROUP_VALUE);
		values.put(CSSConstants.CSS_TABLE_COLUMN_VALUE, CSSValueConstants.TABLE_COLUMN_VALUE);
		values.put(CSSConstants.CSS_TABLE_CELL_VALUE, CSSValueConstants.TABLE_CELL_VALUE);
		values.put(CSSConstants.CSS_TABLE_CAPTION_VALUE, CSSValueConstants.TABLE_CAPTION_VALUE);
		values.put(CSSConstants.CSS_NONE_VALUE, CSSValueConstants.NONE_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public DisplayManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_DISPLAY_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.BLOCK_VALUE;
	}

}
