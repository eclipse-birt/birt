/*

 Copyright 2003  The Apache Software Foundation 

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

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.URIValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

/**
 * One line Class Desc
 * 
 * Complete Class Desc
 * 
 * @author <a href="mailto:deweese@apache.org">l449433</a>
 * @version $Id: URIManager.java,v 1.1 2005/10/13 09:59:54 wyan Exp $
 */
public class URIManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NONE_VALUE, CSSValueConstants.NONE_VALUE);
	}

	protected String propertyName;
	protected Value defaultValue;
	protected boolean inherit;
	
	public URIManager(String propertyName, boolean inherit, Value defaultValue) {
		this.propertyName = propertyName;
		this.defaultValue = defaultValue;
		this.inherit = inherit;
	}

	/**
	 * Implements {@link
	 * org.apache.batik.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	public boolean isInheritedProperty() {
		return inherit;
	}

	/**
	 * Implements {@link
	 * org.apache.batik.css.engine.value.ValueManager#getPropertyName()}.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Implements {@link
	 * org.apache.batik.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	public Value getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine)
			throws DOMException {

		if (lu.getLexicalUnitType() == LexicalUnit.SAC_URI)
		{
			   String uri = resolveURI(engine.getCSSBaseURI(), 
                       lu.getStringValue());
			   return new URIValue(lu.getStringValue(), uri);			
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	public StringMap getIdentifiers() {
		return values;
	}
}
