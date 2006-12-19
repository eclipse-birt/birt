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

import org.eclipse.birt.report.engine.content.IStyle;



public class SpanInfo
{
	public SpanInfo(int x, int cs, int width, boolean start, IStyle style)
	{
		this.x = x;
		this.cs = cs;
		this.width = width;
		this.start = start;
		this.style = style;		
	}
	
	int x =0 ;
	int cs = 0;
	int width = 0;
	boolean start = false;
	
	IStyle style = null;	
}
