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
import org.eclipse.birt.report.engine.nLayout.area.IArea;

public abstract class RepeatableArea extends BlockContainerArea
{

	protected List repeatList = null;
	
	protected int repeatHeight = 0;

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
				if(getRepeatedHeight( )<getMaxAvaHeight())
				{
					for ( int i = 0; i < repeatList.size( ); i++ )
					{
						ContainerArea row = (ContainerArea) repeatList.get( i );
						ContainerArea cloneRow = row.deepClone( );
						children.add( i, cloneRow );
						cloneRow.setParent( this );
						update( cloneRow );
					}
				}
			}

		}
		super.updateChildrenPosition( );
	}
	
	public int getMaxAvaHeight( )
	{
		return super.getMaxAvaHeight( ) - getRepeatedHeight( );
	}

	protected int getRepeatedHeight( )
	{
		if ( repeatList != null )
		{
			AbstractArea area = (AbstractArea) repeatList.get( repeatList
					.size( )-1 );
			return area.getY( ) + area.getAllocatedHeight( );
		}
		return 0;
	}
	
	protected boolean isValidResult(List result)
	{
		if(repeatList!=null )
		{
			return result.size( )>repeatList.size( );
		}
		return super.isValidResult( result );
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
