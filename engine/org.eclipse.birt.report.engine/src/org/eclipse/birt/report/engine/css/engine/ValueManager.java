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

package org.eclipse.birt.report.engine.css.engine;

import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;


/**
 * This interface is implemented by objects which manage the values associated
 * with a property.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: ValueManager.java,v 1.2 2005/10/13 10:00:01 wyan Exp $
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
