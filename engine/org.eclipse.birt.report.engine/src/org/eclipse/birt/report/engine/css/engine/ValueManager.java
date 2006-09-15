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

package org.eclipse.birt.report.engine.css.engine;

import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;


/**
 * This interface is implemented by objects which manage the values associated
 * with a property.
 *
 * @version $Id: ValueManager.java,v 1.2 2005/11/22 09:59:57 wyan Exp $
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
     * @param lu The SAC lexical unit used to create the value.
     * @param engine The calling CSSEngine.
     */
    Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException;

    /**
     * Creates and returns a new float value.
     * @param unitType    A unit code as defined above. The unit code can only 
     *                    be a float unit type
     * @param floatValue  The new float value. 
     */
    Value createFloatValue(short unitType, float floatValue)
	throws DOMException;

    /**
     * Creates and returns a new string value.
     * @param type   A string code as defined in CSSPrimitiveValue. The string
     *               code can only be a string unit type.
     * @param value  The new string value.
     * @param engine The CSS engine.
     */
    Value createStringValue(short type, String value, CSSEngine engine)
        throws DOMException;

    /**
     * Computes the given value.
     * @param engine The CSSEngine.
     * @param idx The property index in the engine.
     * @param value The value to compute.
     */
    Value computeValue(CSSStylableElement elt, CSSEngine engine,
                       int idx,
                       Value value
                       );
}
