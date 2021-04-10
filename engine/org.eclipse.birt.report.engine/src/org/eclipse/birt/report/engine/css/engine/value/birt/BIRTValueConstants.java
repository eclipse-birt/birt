/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
