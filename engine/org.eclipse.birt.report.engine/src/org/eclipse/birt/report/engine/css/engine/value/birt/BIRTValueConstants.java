package org.eclipse.birt.report.engine.css.engine.value.birt;

import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSPrimitiveValue;

public interface BIRTValueConstants {
	Value TRUE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			BIRTConstants.BIRT_TRUE_VALUE);
	Value FALSE_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT,
			BIRTConstants.BIRT_FALSE_VALUE);
	Value ALL_VALUE = new StringValue(CSSPrimitiveValue.CSS_IDENT, BIRTConstants.BIRT_ALL_VALUE);
}
