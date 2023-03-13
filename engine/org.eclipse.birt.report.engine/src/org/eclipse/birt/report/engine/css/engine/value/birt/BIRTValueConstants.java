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
package org.eclipse.birt.report.engine.css.engine.value.birt;

import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSPrimitiveValue;

public interface BIRTValueConstants {

	Value TRUE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, BIRTConstants.BIRT_TRUE_VALUE);
	Value FALSE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, BIRTConstants.BIRT_FALSE_VALUE);
	Value ALL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, BIRTConstants.BIRT_ALL_VALUE);
	Value SOFT_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, BIRTConstants.BIRT_SOFT_VALUE);
}
