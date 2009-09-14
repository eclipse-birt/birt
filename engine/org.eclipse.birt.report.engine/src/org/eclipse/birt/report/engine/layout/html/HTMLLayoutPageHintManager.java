package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.nLayout.area.impl.SizeBasedContent;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;
import org.eclipse.birt.report.engine.presentation.SizeBasedPageSection;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class HTMLLayoutPageHintManager
{
	protected HTMLLayoutContext context;
	
	/**
	 * cache the content is finished or not
	 */
	protected HashMap layoutHint = new HashMap( );
	
	/**
	 * content instanceID to size based content mapping.
	 */
	protected HashMap<String, SizeBasedContent> sizeBasedContentMapping = new HashMap<String, SizeBasedContent>( );

	protected ArrayList pageHints = new ArrayList( );
	
	public HTMLLayoutPageHintManager( HTMLLayoutContext context )
	{
		this.context = context;
	}
	
	public void setPageHint( List hints )
	{
		pageHints.addAll( hints );
	}

	public ArrayList getPageHint( )
	{
		ArrayList hints = new ArrayList( );
		hints.addAll( pageHints );
		return hints;
	}
	
	public void reset( )
	{
		layoutHint = new HashMap( );
		sizeBasedContentMapping = new HashMap<String, SizeBasedContent>( );
		context.setFinish( false );
		context.setAllowPageBreak( true );
		context.setMasterPage( null );
	}

	public void addLayoutHint( IContent content, boolean finished )
	{
		layoutHint.put( content, new Boolean( finished ) );
	}
	
	public void removeLayoutHint(IContent content)
	{
		layoutHint.remove( content );
	}

	public boolean getLayoutHint( IContent content )
	{
		Object finished = layoutHint.get( content );
		if ( finished != null && finished instanceof Boolean )
		{
			return ( (Boolean) finished ).booleanValue( );
		}
		return true;
	}

	public void removeLayoutHint( )
	{
		layoutHint.clear( );
	}
	
	protected HashMap<String, UnresolvedRowHint> lastPageUnresolvedRowHints = new HashMap<String, UnresolvedRowHint>( );
	protected HashMap<String, UnresolvedRowHint> currentPageUnresolvedRowHints = new HashMap<String, UnresolvedRowHint>( );
	protected HashMap<String, UnresolvedRowHint> parallelPagesUnresolvedRowHints = new HashMap<String, UnresolvedRowHint>( );
	
	public void generatePageRowHints(Collection<String> keys )
	{
		lastPageUnresolvedRowHints.clear( );
		Iterator<String> iter = keys.iterator( );
		while(iter.hasNext( ))
		{
			String key = iter.next( );
			UnresolvedRowHint hint = parallelPagesUnresolvedRowHints.get( key );
			if ( hint != null )
			{
				lastPageUnresolvedRowHints.put( key, hint );
			}
		}
	}
	
	public HashMap<String, UnresolvedRowHint> getUnresolvedRowHints( )
	{
		return lastPageUnresolvedRowHints;
	}
	
	protected ArrayList columnHints = new ArrayList( );

	public List getTableColumnHints( )
	{
		return columnHints;
	}

	public void addTableColumnHints( List hints )
	{
		columnHints.addAll( hints );
	}
	
	public void addTableColumnHint(TableColumnHint hint)
	{
		columnHints.add( hint );
	}

	public UnresolvedRowHint getUnresolvedRowHint( String key )
	{
		if ( parallelPagesUnresolvedRowHints.size( ) > 0 )
		{
			return parallelPagesUnresolvedRowHints.get( key );
		}
		return null;
	}

	public void addUnresolvedRowHint(String key, UnresolvedRowHint hint )
	{
		currentPageUnresolvedRowHints.put( key, hint );
	}

	public void clearPageHint( )
	{
		columnHints.clear( );
		pageHints.clear( );
	}
	
	public void resetRowHint()
	{
		if ( !context.emptyPage )
		{
			parallelPagesUnresolvedRowHints.clear( );
			parallelPagesUnresolvedRowHints.putAll( currentPageUnresolvedRowHints );
			currentPageUnresolvedRowHints.clear( );
		}
	}
	
	public void setLayoutPageHint( IPageHint pageHint )
	{
		if ( pageHint != null )
		{
			context.pageNumber =  pageHint.getPageNumber( );
			context.masterPage = pageHint.getMasterPage( );
			// column hints
			int count = pageHint.getTableColumnHintCount( );
			for ( int i = 0; i < count; i++ )
			{
				columnHints.add( pageHint.getTableColumnHint( i ) );
			}
			// unresolved row hints
			count = pageHint.getUnresolvedRowCount( );
			if(count>0)
			{
				for ( int i = 0; i < count; i++ )
				{
					UnresolvedRowHint hint = pageHint.getUnresolvedRowHint( i );
					String key = getHintMapKey(hint.getTableId( ));
					parallelPagesUnresolvedRowHints.put( key, hint );
				}
			}
			// size based page break hints
			for ( int i = 0; i < pageHint.getSectionCount( ); i++ )
			{
				PageSection section = pageHint.getSection( i );
				if ( section instanceof SizeBasedPageSection )
				{
					SizeBasedPageSection sizeBasedSection = (SizeBasedPageSection) section;
					if ( sizeBasedSection.start.dimension != -1 )
					{
						InstanceID startID = sizeBasedSection.starts[sizeBasedSection.starts.length - 1]
								.getInstanceID( );
						if ( startID != null )
						{
							sizeBasedContentMapping.put( startID
									.toUniqueString( ), sizeBasedSection.start );
						}
					}
					if ( sizeBasedSection.end.dimension != -1 )
					{
						InstanceID endID = sizeBasedSection.ends[sizeBasedSection.ends.length - 1]
								.getInstanceID( );
						if ( endID != null )
						{
							sizeBasedContentMapping.put(
									endID.toUniqueString( ),
									sizeBasedSection.end );
						}
					}
				}
			}
		}
	}
	
	public HashMap<String, SizeBasedContent> getSizeBasedContentMapping( )
	{
		return sizeBasedContentMapping;
	}
	
	public String getHintMapKey(String tableId)
	{
		String key = tableId;
		List hints = getTableColumnHint( key );
		Iterator iter = hints.iterator( );
		while(iter.hasNext( ))
		{
			int[] vs = (int[])iter.next( );
			key = key +"-" + vs[0] + "-" + vs[1];
		}
		return key;
	}
	
	public List getTableColumnHint( String tableId )
	{
		List list = new ArrayList();
		if ( columnHints.size( ) > 0 )
		{
			Iterator iter = columnHints.iterator( );
			while ( iter.hasNext( ) )
			{
				TableColumnHint hint = (TableColumnHint) iter.next( );
				if ( tableId.equals( hint.getTableId( ) ) )
				{
					list.add( new int[]{hint.getStart( ),
							hint.getStart( ) + hint.getColumnCount( )} );
				}
			}
		}
		return list;
	}

}
