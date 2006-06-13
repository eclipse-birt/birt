package org.eclipse.birt.report.engine.content.impl;

import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.TableItemDesign;


public class ListContent extends ContainerContent implements IListContent
{

	Boolean headerRepeat;
	
	public ListContent( IReportContent report )
	{
		super( report );
	}
	
	public int getContentType( )
	{
		return LIST_CONTENT;
	}

	
	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitList(this, value);
	}

	public void setHeaderRepeat( boolean headerRepeat )
	{
		if (generateBy instanceof TableItemDesign)
		{
			if ( ( (TableItemDesign) generateBy ).isRepeatHeader( ) == headerRepeat )
			{
				this.headerRepeat = null;
				return;
			}
		}
		this.headerRepeat = Boolean.valueOf( headerRepeat );
	}

	public boolean isHeaderRepeat( )
	{
		if ( headerRepeat != null )
		{
			return headerRepeat.booleanValue( );
		}
		if ( generateBy instanceof TableItemDesign )
		{
			return ( (TableItemDesign) generateBy ).isRepeatHeader( );
		}

		return false;
	}
	
	public IListBandContent getHeader( )
	{
		return getListBand( IListBandContent.BAND_HEADER );
	}

	protected IListBandContent getListBand( int type )
	{
		IListBandContent listBand;
		if ( children == null )
		{
			return null;
		}
		Iterator iter = children.iterator( );
		while ( iter.hasNext( ) )
		{
			Object child = iter.next( );
			if ( child instanceof IListBandContent )
			{
				listBand = (IListBandContent) child;
				if ( listBand.getBandType( ) == type )
				{
					return listBand;
				}
			}
		}
		return null;
	}
}
