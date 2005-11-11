/*

   Copyright 2000-2004  The Apache Software Foundation 

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
package org.eclipse.birt.report.engine.css.engine.value.birt;

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;

/**
 * Define BIRT constants, such as tag names, attribute names and URI
 *
 * @version $Id: BIRTConstants.java,v 1.2 2005/10/27 09:27:31 wyan Exp $
 */
public interface BIRTConstants extends CSSConstants {

    /////////////////////////////////////////////////////////////////////////
    // BIRT attributes
    /////////////////////////////////////////////////////////////////////////
	String BIRT_BACKGROUND_POSITION_X_PROPERTY = "background-position-x";
	String BIRT_BACKGROUND_POSITION_Y_PROPERTY = "background-position-y";
	String BIRT_CAN_SHRINK_PROPERTY = "can-shrink"; //$NON-NLS-1$
	String BIRT_DATE_TIME_FORMAT_PROPERTY = "date-format"; //$NON-NLS-1$
	String BIRT_MASTER_PAGE_PROPERTY= "master-page"; //$NON-NLS-1$
	String BIRT_NUMBER_ALIGN_PROPERTY = "number-align";
	String BIRT_NUMBER_FORMAT_PROPERTY = "number-format"; //$NON-NLS-1$
	String BIRT_SHOW_IF_BLANK_PROPERTY= "show-if-blank"; //$NON-NLS-1$
	String BIRT_STRING_FORMAT_PROPERTY = "string-format"; //$NON-NLS-1$
	String BIRT_TEXT_UNDERLINE_PROPERTY = "text-underline"; //$NON-NLS-1$
	String BIRT_TEXT_OVERLINE_PROPERTY = "text-overline"; //$NON-NLS-1$
	String BIRT_TEXT_LINETHROUGH_PROPERTY = "text-linethrough"; //$NON-NLS-1$
	String BIRT_VISIBLE_FORMAT_PROPERTY = "visible-formats";
	
    /////////////////////////////////////////////////////////////////////////
    // BIRT attribute value
    /////////////////////////////////////////////////////////////////////////
	
	String BIRT_TRUE_VALUE = "true";
	String BIRT_FALSE_VALUE = "false";
	String BIRT_ALL_VALUE = "all";
}
