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
 *  Actuate Corporation  - modification of Batik's ValueManager.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine;

import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

/**
 * This interface is implemented by objects which manage the values associated
 * with a property.
 *
 */
public interface ValueManager {

	/**
	 * Returns the name of the property handled.
	 */
	String getPropertyName();

	/**
	 * Whether the handled property is inherited or not.
	 */
	boolean isInheritedProperty();

	/**
	 * Returns the default value for the handled property.
	 */
	Value getDefaultValue();

	/**
	 * Creates a value from a lexical unit.
	 * 
	 * @param lu     The SAC lexical unit used to create the value.
	 * @param engine The calling CSSEngine.
	 */
	Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException;

	/**
	 * Computes the given value.
	 * 
	 * @param engine The CSSEngine.
	 * @param idx    The property index in the engine.
	 * @param value  The value to compute.
	 */
	Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value);
}
