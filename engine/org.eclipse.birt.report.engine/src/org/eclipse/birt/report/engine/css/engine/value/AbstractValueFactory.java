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

import java.net.URI;

import org.w3c.dom.DOMException;

/**
 * This class provides a base implementation for the value factories.
 * 
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractValueFactory.java,v 1.2 2005/10/13 09:59:59 wyan Exp $
 */
public abstract class AbstractValueFactory {

	/**
	 * Returns the name of the property handled.
	 */
	public abstract String getPropertyName();


	/**
	 * Creates a DOM exception, given an invalid identifier.
	 */
	protected DOMException createInvalidIdentifierDOMException(String ident) {
		Object[] p = new Object[] { getPropertyName(), ident };
		String s = Messages.formatMessage("invalid.identifier", p);
		return new DOMException(DOMException.SYNTAX_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid lexical unit type.
	 */
	protected DOMException createInvalidLexicalUnitDOMException(short type) {
		Object[] p = new Object[] { getPropertyName(), new Integer(type) };
		String s = Messages.formatMessage("invalid.lexical.unit", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid float type.
	 */
	protected DOMException createInvalidFloatTypeDOMException(short t) {
		Object[] p = new Object[] { getPropertyName(), new Integer(t) };
		String s = Messages.formatMessage("invalid.float.type", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid float value.
	 */
	protected DOMException createInvalidFloatValueDOMException(float f) {
		Object[] p = new Object[] { getPropertyName(), new Float(f) };
		String s = Messages.formatMessage("invalid.float.value", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	/**
	 * Creates a DOM exception, given an invalid string type.
	 */
	protected DOMException createInvalidStringTypeDOMException(short t) {
		Object[] p = new Object[] { getPropertyName(), new Integer(t) };
		String s = Messages.formatMessage("invalid.string.type", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected DOMException createMalformedLexicalUnitDOMException() {
		Object[] p = new Object[] { getPropertyName() };
		String s = Messages.formatMessage("malformed.lexical.unit", p);
		return new DOMException(DOMException.INVALID_ACCESS_ERR, s);
	}

	protected DOMException createDOMException() {
		Object[] p = new Object[] { getPropertyName() };
		String s = Messages.formatMessage("invalid.access", p);
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}
	
    protected static String resolveURI(URI base, String value) {
    	try
    	{
    		return base.resolve(value).toString();
    	}
    	catch(Exception ex)
    	{
    		return value;
    	}
    }
	
}
