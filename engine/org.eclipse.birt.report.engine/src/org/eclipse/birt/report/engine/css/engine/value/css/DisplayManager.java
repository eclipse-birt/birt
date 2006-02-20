/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Apache - initial API and implementation
 *  Actuate Corporation - changed by Actuate
 *******************************************************************************/
/*

   Copyright 1999-2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
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
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.BLOCK_VALUE;
	}

}
