
package org.eclipse.birt.report.engine.content.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ListItemDesign;

public class ListContent extends ContainerContent implements IListContent
{

	Boolean headerRepeat;

	ListContent( IReportContent report )
	{
		super( report );
	}
	
	ListContent(IListContent listContent)
	{
		super(listContent);
		this.headerRepeat = new Boolean(listContent.isHeaderRepeat( ));
	}
	

	public int getContentType( )
	{
		return LIST_CONTENT;
	}

	public Object accept( IContentVisitor visitor, Object value )
			throws BirtException
	{
		return visitor.visitList( this, value );
	}

	public void setHeaderRepeat( boolean headerRepeat )
	{
		if ( generateBy instanceof ListItemDesign )
		{
			Expression<Boolean> repeatHeader = ( (ListItemDesign) generateBy ).isRepeatHeader( );
			if ( !repeatHeader.isExpression( )
					&& repeatHeader.getValue( ) == headerRepeat )
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
		if ( generateBy instanceof ListItemDesign )
		{
			return getBooleanValue( ( (ListItemDesign) generateBy )
					.isRepeatHeader( ), false );
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
	static final protected short FIELD_HEADER_REPEAT = 1300;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( headerRepeat != null )
		{
			IOUtil.writeShort( out, FIELD_HEADER_REPEAT);
			IOUtil.writeBool( out, headerRepeat.booleanValue( ) );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in,
			ClassLoader loader ) throws IOException
	{
		switch ( filedId )
		{
			case FIELD_HEADER_REPEAT :
				headerRepeat = Boolean.valueOf( IOUtil.readBool( in ) );
				break;
			default :
				super.readField( version, filedId, in, loader );
		}
	}
	
	public boolean needSave( )
	{
		if ( headerRepeat != null )
		{
			return true;
		}
		return super.needSave( );
	}
	
	protected IContent cloneContent()
	{
		return new ListContent(this);
	}

}
