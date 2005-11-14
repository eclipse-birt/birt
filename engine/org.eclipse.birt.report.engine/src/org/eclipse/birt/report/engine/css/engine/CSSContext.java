/*

   Copyright 2002-2004  The Apache Software Foundation 

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

import org.w3c.dom.css.CSSValue;

/**
 * This interface allows the user of a CSSEngine to provide contextual
 * informations.
 *
 * @version $Id: CSSContext.java,v 1.1 2005/11/11 06:26:48 wyan Exp $
 */
public interface CSSContext {
    
    /**
     * Returns the Value corresponding to the given system color.
     */
    CSSValue getSystemColor(String ident);

    /**
     * Returns the value corresponding to the default font-family.
     */
    CSSValue getDefaultFontFamily();

    /**
     * Returns a lighter font-weight.
     */
    float getLighterFontWeight(float f);

    /**
     * Returns a bolder font-weight.
     */
    float getBolderFontWeight(float f);

    /**
     * Returns the size of a px CSS unit in millimeters.
     */
    float getPixelUnitToMillimeter();

    /**
     * Returns the medium font size.
     */
    float getMediumFontSize();
}
