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
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

public abstract class RepeatableArea extends BlockContainerArea
{

	protected List repeatList = null;

	protected int repeatHeight = 0;

	protected boolean inHeaderBand = false;
	
	public RepeatableArea( ContainerArea parent, LayoutContext context,
			IContent content )
	{
		super( parent, context, content );
		if ( needRepeat( ) )
		{
			repeatList = new ArrayList( );
		}
	}
	
	public void setInHeaderBand(boolean inHeaderBand)
	{
		this.inHeaderBand = inHeaderBand;
	}
	
	protected void addRepeatedItem() throws BirtException
	{
		if ( repeatList != null && repeatList.size( ) > 0 )
		{
			if ( !inHeaderBand )
			{
				if ( getRepeatedHeight( ) < getMaxAvaHeight( ) )
				{
					for ( int i = 0; i < repeatList.size( ); i++ )
					{
						ContainerArea row = (ContainerArea) repeatList.get( i );
						ContainerArea cloneRow = row.deepClone( );
						if(i==0 && cloneRow instanceof RowArea)
						{
							((RowArea)cloneRow).needResolveBorder = true;
						}
						children.add( i, cloneRow );
						cloneRow.setParent( this );
						update( cloneRow );
						cloneRow.setAllocatedY( currentBP );
					}
				}
				else
				{
					// remove repeat list.
					repeatList = null;
				}
			}
		}
	}

	public int getMaxAvaHeight( )
	{
		return super.getMaxAvaHeight( ) - getRepeatedHeight( );
	}

	protected int getRepeatedHeight( )
	{
		if ( inHeaderBand )
		{
			return 0;
		}
		if ( repeatHeight != 0 )
		{
			return repeatHeight;
		}
		else
		{
			if(repeatList!=null)
			{
				for ( int i = 0; i < repeatList.size( ); i++ )
				{
					AbstractArea area = (AbstractArea) repeatList.get( i );
					repeatHeight += area.getAllocatedHeight( );
				}
				return repeatHeight;
			}
		}
		return 0;
	}

	protected boolean isValidResult( List result )
	{
		if ( repeatList != null && !inHeaderBand )
		{
			return result.size( ) > repeatList.size( );
		}
		return super.isValidResult( result );
	}

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
						if ( content instanceof IRowContent )
						{
							RowDesign rowDesign = (RowDesign) content
									.getGenerateBy( );
							if ( rowDesign.getRepeatable( ) )
								repeatList.add( area );
						}
						else
							repeatList.add( area );
					}
				}
			}
		}
	}

}
