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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
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

	public void setInHeaderBand( boolean inHeaderBand )
	{
		this.inHeaderBand = inHeaderBand;
	}
	
	//TODO refactor
	protected boolean isFirstChildInHeaderBand()
	{
		//check if the first child is in head band;
		if(children.size( )>0)
		{
			AbstractArea first = (AbstractArea)children.get(0);
			if(isInRepeatHeader(first))
			{
				return true;
			}
			
		}
		return false;
	}

	protected void addRepeatedItem( ) throws BirtException
	{
		if ( repeatList != null && repeatList.size( ) > 0 )
		{
			if ( !inHeaderBand && !isFirstChildInHeaderBand())
			{
				if ( getRepeatedHeight( ) < getMaxAvaHeight( ) )
				{
					for ( int i = 0; i < repeatList.size( ); i++ )
					{
						ContainerArea row = (ContainerArea) repeatList.get( i );
						ContainerArea cloneRow = row.deepClone( );
						if ( cloneRow instanceof RowArea )
						{
							( (RowArea) cloneRow ).needResolveBorder = true;
						}
						cloneRow.finished = true;
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
			if ( repeatList != null )
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

	public SplitResult split( int height, boolean force ) throws BirtException
	{
		// repeat header can not be split.
		if ( !force && repeatList != null && repeatList.size( ) > 0 )
		{
			Iterator i = children.iterator( );
			boolean firstHeaderRow = true;
			while ( i.hasNext( ) )
			{
				ContainerArea area = (ContainerArea) i.next( );
				if ( isInRepeatHeader( area ) )
				{
					if ( firstHeaderRow )
					{
						area.setPageBreakInside( IStyle.AVOID_VALUE );
						firstHeaderRow = false;
					}
					else
					{
						area.setPageBreakInside( IStyle.AVOID_VALUE );
						area.setPageBreakBefore( IStyle.AVOID_VALUE );
					}
				}
			}
		}

    // we cannot render repeated header that is greater than height of root area
		if (context.isFixedLayout()) {
			int maxCellHeight = getMaxCellHeight();
			if (context.getMaxBP() < maxCellHeight) {
				ReportElementDesign red = (ReportElementDesign) this.getContent().getGenerateBy();
				throw new EngineException(String.format("There is no enought place to render repetable area: [ID=%s, name=%s]", red.getID(), red.getName()));
			}
		}
		return super.split( height, force );
	}
	
	private int getMaxCellHeight() {
		Stream<RowArea> rows = Stream.empty();
		if (repeatList != null) {
			rows = repeatList.stream().filter(r -> r instanceof RowArea).map(r -> (RowArea) r);
		}
		return rows.flatMap(row -> Arrays.stream(row.cells)).mapToInt(c -> c != null ? c.getCurrentBP() : 0).max().orElse(0);
	}

	protected boolean isValidResult( List result )
	{
		assert result != null;
		if ( repeatList != null && !repeatList.isEmpty( ) )
		{
			if ( result.size( ) > repeatList.size( ) )
			{
				return true;
			}
			else
			{
				int index = result.indexOf( repeatList
						.get( repeatList.size( ) - 1 ) );
				if ( index != -1 && result.size( ) - 1 > index )
				{
					return true;
				}
			}
			return false;
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
		if ( repeatList != null && isInRepeatHeader( area ) )
		{
			repeatList.add( area );
		}
	}

	private boolean isInRepeatHeader( AbstractArea area )
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
						if ( rowDesign == null || rowDesign.getRepeatable( ) )
						{
							return true;
						}
					}
					else
					{
						return true;
					}
				}
			}
		}
		return false;
	}

}
