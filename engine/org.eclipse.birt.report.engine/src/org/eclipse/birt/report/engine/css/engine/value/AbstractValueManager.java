/*

 Copyright 2002-2003  The Apache Software Foundation 

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
package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;


/**
 * This class provides an abstract implementation of the ValueManager interface.
 * 
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractValueManager.java,v 1.2 2005/10/13 09:59:59 wyan Exp $
 */
public abstract class AbstractValueManager extends AbstractValueFactory
		implements ValueManager {

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	public Value createFloatValue(short unitType, float floatValue)
			throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link
	 * ValueManager#createStringValue(short,String,CSSEngine)}.
	 */
	public Value createStringValue(short type, String value, CSSEngine engine)
			throws DOMException {
		throw createDOMException();
	}

	/**
	 * Implements {@link
	 * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {

		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			CSSPrimitiveValue pvalue = (CSSPrimitiveValue) value;
			if (pvalue.getPrimitiveType() == CSSPrimitiveValue.CSS_URI) {
				// Reveal the absolute value as the cssText now.
				return new URIValue(pvalue.getStringValue(), pvalue
						.getStringValue());
			}
		}
		return value;
	}
}
