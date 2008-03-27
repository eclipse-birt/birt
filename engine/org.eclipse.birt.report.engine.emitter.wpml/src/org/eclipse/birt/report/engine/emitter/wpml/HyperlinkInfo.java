/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.wpml;

public class HyperlinkInfo
{
	
	
	HyperlinkInfo ( int type, String url , String toolTip)
	{
		
		this(type, url, null , toolTip);
	}
	
	HyperlinkInfo ( int type, String url, String bookmark , String toolTip)
	{
		this.type = type;
		this.url = url;
		this.mark = bookmark;
		this.toolTip = toolTip;
	}
	
	int type;

	String url, mark , toolTip;
    	
	static int BOOKMARK = 0;

	static int HYPERLINK = 1;

	static int DRILL = 2;
}
