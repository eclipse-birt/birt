/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;



public class BlockTextArea extends BlockContainerArea implements ILayout
{
	public BlockTextArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
	}
	
	public BlockTextArea(BlockTextArea area)
	{
		super(area);
	}
	
	public void layout( ) throws BirtException
	{
		initialize();
		LineArea line = new TextLineArea(this, context);
		line.initialize( );
		TextAreaLayout text = new TextAreaLayout( line, context, content);
		text.initialize( );
		text.layout( );
		text.close( );
		line.close( );
		close();
	}
	
	public BlockTextArea cloneArea()
	{
		return new BlockTextArea(this);
	}
	
}
