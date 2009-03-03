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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

public abstract class RepeatableArea extends BlockContainerArea
{

	protected List repeatList = null;

	public RepeatableArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
		if ( needRepeat( ) )
		{
			repeatList = new ArrayList( );
		}
	}

	protected void updateChildrenPosition( ) throws BirtException
	{
		if ( repeatList != null && repeatList.size( ) > 0 )
		{
			if ( !isInHeaderBand( ) )
			{
				for ( int i = 0; i < repeatList.size( ); i++ )
				{
					ContainerArea row = (ContainerArea) repeatList.get( i );
					ContainerArea cloneRow = row.deepClone( );
					children.add( i, cloneRow );
					update( cloneRow );
				}
			}

		}
		super.updateChildrenPosition( );
	}

	protected abstract boolean isInHeaderBand( );

	protected abstract boolean needRepeat( );

	public RepeatableArea( RepeatableArea area )
	{
		super( area );
	}

	public void add( AbstractArea area )
	{
		super.add( area );
		// cache repeat list;
		if ( repeatList != null )
		{
			IContent content = ( (ContainerArea) area ).getContent( );
			if ( content != null )
			{
				IElement parent = content.getParent( );
				if ( parent != null && parent instanceof IBandContent )
				{
					int type = ( (IBandContent) parent ).getBandType( );
					if ( type == IBandContent.BAND_HEADER
							|| type == IBandContent.BAND_GROUP_HEADER )
					{
						repeatList.add( area );
					}
				}
			}
		}
	}

}
