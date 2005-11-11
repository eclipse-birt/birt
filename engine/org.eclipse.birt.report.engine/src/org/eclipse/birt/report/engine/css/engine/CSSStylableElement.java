/*

   Copyright 2002  The Apache Software Foundation 

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

import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IStyle;

/**
 * This interface must be implemented by the DOM elements which needs
 * CSS support.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: CSSStylableElement.java,v 1.2 2005/10/20 07:47:44 wyan Exp $
 */
public interface CSSStylableElement extends IElement{
    
    /**
     * Returns the computed style of this element/pseudo-element.
     */
     IStyle getComputedStyle();

     /**
      * return the style of the element
      * @return
      */
    IStyle getStyle();
}
