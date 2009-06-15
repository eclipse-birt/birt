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

public class InlineTextArea extends InlineContainerArea implements ILayout
{
	int splitOffset= 0;
	//protected int fixedDimension = 0;
	
	public InlineTextArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
//		InstanceID id = content.getInstanceID( );
//		if ( id != null )
//		{
//			SizeBasedContent hint = (SizeBasedContent) context
//					.getSizeBasedContentMapping( ).get( id.toUniqueString( ) );
//			if ( hint != null )
//			{
//				currentIP += hint.floatPos - hint.offsetInContent;
//				//fixedDimension = hint.dimension;
//			}
//		}
	}

	public void layout( ) throws BirtException
	{
		initialize( );
		TextAreaLayout inlineText = new TextAreaLayout( this, context, content );
		inlineText.initialize( );
		if ( context.isFixedLayout( ) )
		{
			inlineText.addListener( new FixedLayoutInlineTextListener() );
		}
		inlineText.layout( );
		inlineText.close( );
		close( );
	}
	
	public SplitResult split( int height, boolean force ) throws BirtException
	{
		if ( force )
		{
			ContainerArea newArea= cloneArea();
			newArea.children.addAll( children );
			children.clear( );
			this.height = 0;
			return new SplitResult( newArea, SplitResult.SPLIT_SUCCEED_WITH_PART );
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}
}
