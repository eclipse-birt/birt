/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;

public interface IBirtConstants
{
	//Oprand Name Value pair: Names
	static public final String OPRAND_PAGENO = "page";	//$NON-NLS-1$
	static public final String OPRAND_BOOKMARK = "bookmark";	//$NON-NLS-1$
	static public final String OPRAND_SVG = "svg";	//$NON-NLS-1$
	static public final String OPRAND_PARAM = "param"; //$NON-NLS-1$
	static public final String OPRAND_IID = "iid"; //$NON-NLS-1$

	static public String MIME_TYPE = "text/xml"; //$NON-NLS-1$
	static public String RENDERFORMAT = HTMLRenderOption.OUTPUT_FORMAT_HTML ;
}