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

package org.eclipse.birt.report.engine.api;

/**
 * output settings for HTML output format 
 */
public class HTMLRenderOption extends RenderOptionBase {
	
	public static final String HTML_TYPE = "HTMLType"; 		//$NON-NLS-1$
	public static final String HTML = "HTML"; 				//$NON-NLS-1$
	public static final String HTML_NOCSS = "HTMLNoCSS"; 	//$NON-NLS-1$
	public static final String USER_AGENT = "user-agent"; //$NON-NLS-1$
	public static final String BROWSER_IE = "IE";
	
	/**
	 * constructor
	 */
	public HTMLRenderOption() {
	}
	
	/**
	 * sets whether the HTML output can be embedded directly into an HTML page
	 * 
	 * @param embeddable whether the HTML output can be embedded directly into 
	 * an HTML page
	 */
	public void setEmbeddable(boolean embeddable) {
		if (embeddable)
			options.put(HTML_TYPE, HTML_NOCSS);
		else
			options.put(HTML_TYPE, HTML);	
	}
	
	/**
	 * @return whether the output is embeddable 
	 */
	public boolean getEmbeddable()
	{
		String htmlType = (String)options.get(HTML_TYPE);
		if (htmlType != null && htmlType.compareTo(HTML_NOCSS) == 0)
			return true;
		return false;
	}
}
