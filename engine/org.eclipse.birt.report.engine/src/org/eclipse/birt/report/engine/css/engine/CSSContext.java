/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Apache - initial API and implementation
 *  Actuate Corporation - changed by Actuate
 *******************************************************************************/
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

import org.w3c.dom.css.CSSValue;

/**
 * This interface allows the user of a CSSEngine to provide contextual
 * informations.
 *
 * @version $Id: CSSContext.java,v 1.3 2005/11/22 09:59:57 wyan Exp $
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
