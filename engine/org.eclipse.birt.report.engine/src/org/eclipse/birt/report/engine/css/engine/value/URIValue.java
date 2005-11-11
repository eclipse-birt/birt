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
package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents uri values.
 *
 * @version $Id: URIValue.java,v 1.1 2005/10/08 07:30:59 wyan Exp $
 */
public class URIValue extends StringValue {

    String cssText;

    /**
     * Creates a new StringValue.
     */
    public URIValue(String cssText, String uri) {
        super(CSSPrimitiveValue.CSS_URI, uri);
        this.cssText = cssText;
    }

    /**
     * A string representation of the current value. 
     */
    public String getCssText() {
        return "url(" + cssText + ")";
    }
}
