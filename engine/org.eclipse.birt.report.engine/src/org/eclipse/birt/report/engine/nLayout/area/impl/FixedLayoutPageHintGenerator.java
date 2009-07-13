/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.internal.content.wrap.AbstractContentWrapper;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class FixedLayoutPageHintGenerator
{
	protected SizeBasedContent startContent = null;
	protected SizeBasedContent currentContent = null;

	protected HTMLLayoutContext htmlLayoutContext = null;
	private ArrayList<SizeBasedContent[]> fixedLayoutPageHints = new ArrayList<SizeBasedContent[]>( );

	protected HashSet<String> tableIds = new HashSet<String>( );
	HashMap<String, UnresolvedRowHint> htmlUnresolvedRowHints = null;
	HashMap<String, UnresolvedRowHint> unresolvedRowHints = null;
	
	protected LayoutContext context;

	public FixedLayoutPageHintGenerator( LayoutContext context )
	{
		this.context = context;
		htmlLayoutContext = context.getHtmlLayoutContext( );
	}

	public ArrayList getPageHint( )
	{
		return fixedLayoutPageHints;
	}

	public void addUnresolvedRowHint( String tableId, UnresolvedRowHint hint )
	{
		if ( unresolvedRowHints == null )
		{
			unresolvedRowHints = new HashMap<String, UnresolvedRowHint>( );
		}
		unresolvedRowHints.put( htmlLayoutContext.getPageHintManager( )
				.getHintMapKey( tableId ), hint );
	}

	public List<UnresolvedRowHint> getUnresolvedRowHints( )
	{
		ArrayList<UnresolvedRowHint> unresolvedRowHintsList = new ArrayList<UnresolvedRowHint>( );
		Iterator<String> iter = getTableKeys( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String key = iter.next( );
			if ( unresolvedRowHints != null )
			{
				UnresolvedRowHint hint = unresolvedRowHints.get( key );
				if ( hint != null )
				{
					unresolvedRowHintsList.add( hint );
					continue;
				}
			}
			UnresolvedRowHint hint = htmlUnresolvedRowHints.get( key );
			if ( hint != null )
			{
				unresolvedRowHintsList.add( hint );
			}
		}
		return unresolvedRowHintsList;
	}

	public void generatePageHints( IPageContent page ) throws BirtException
	{
		PageArea pageArea = (PageArea) page
				.getExtension( IContent.LAYOUT_EXTENSION );
		if ( pageArea != null )
		{
			reset( );
			htmlUnresolvedRowHints = htmlLayoutContext.getPageHintManager( )
					.getCurrentPageUnresolvedRowHints( );
			for ( Iterator<IArea> i = pageArea.getBody( ).getChildren( ); i
					.hasNext( ); )
			{
				IArea child = i.next( );
				traverse( child );
			}
		}
		if ( startContent != null )
		{
			if ( currentContent != null )
			{
				fixedLayoutPageHints.add( new SizeBasedContent[]{startContent,
						currentContent} );
			}
		}
	}

	private void traverse( IArea area )
	{
		if ( area instanceof ContainerArea )
		{
			if ( area instanceof TableArea )
			{
				tableIds.add( ( (TableArea) area ).getContent( )
						.getInstanceID( ).toUniqueString( ) );
			}
			ContainerArea container = (ContainerArea) area;
			if ( container.content != null )
			{
				start( container );
			}
			if ( !( container.content instanceof ForeignContent ) )
			{
				for ( Iterator<IArea> i = container.getChildren( ); i.hasNext( ); )
				{
					IArea child = i.next( );
					traverse( child );
				}
			}
		}
		String bookmark = area.getBookmark();
		if ( bookmark != null )
		{
			context.addBookmarkMap( context.getPageNumber( ), bookmark  );
		}
	}

	private void start( ContainerArea area )
	{
		if ( startContent == null )
		{
			startContent = createSizeBasedContent( area );
			currentContent = startContent;
		}
		else
		{
			if ( currentContent != null )
			{
				if ( InstanceIDComparator.isNextWith( currentContent.content,
						area.content ) )
				{
					currentContent = createSizeBasedContent( area );
				}
				else if ( InstanceIDComparator.equals( startContent.content,
						currentContent.content ) )
				{
					if ( startContent.content instanceof IContainerContent )
					{
						startContent = createSizeBasedContent( area );
						currentContent = startContent;
					}
				}
				else
				{
					fixedLayoutPageHints.add( new SizeBasedContent[]{
							startContent, currentContent} );
					startContent = createSizeBasedContent( area );
					currentContent = startContent;
				}
			}
		}
	}

	private SizeBasedContent createSizeBasedContent( ContainerArea area )
	{
		SizeBasedContent sizeBasedContent = new SizeBasedContent( );
		if ( area.content instanceof AbstractContentWrapper )
		{
			sizeBasedContent.content = ( (AbstractContentWrapper) area.content )
					.getContent( );
		}
		else
		{
			sizeBasedContent.content = area.content;
		}

		if ( area instanceof BlockTextArea )
		{
			BlockTextArea blockText = (BlockTextArea) area;
			sizeBasedContent.floatPos = 0;
			ArrayList<BlockTextArea> list = (ArrayList<BlockTextArea>) area.content
					.getExtension( IContent.LAYOUT_EXTENSION );
			if ( list.size( ) > 1 )
			{
				Iterator<BlockTextArea> i = list.iterator( );
				int offsetInContent = 0;
				int lastHeight = 0;
				while ( i.hasNext( ) )
				{
					offsetInContent += lastHeight;
					BlockTextArea current = i.next( );
					if ( current == area )
					{
						break;
					}
					lastHeight = current.getHeight( );
				}
				sizeBasedContent.offsetInContent = offsetInContent;
			}
			else
			{
				sizeBasedContent.offsetInContent = 0;	
			}
			sizeBasedContent.dimension = blockText.getHeight( );
			sizeBasedContent.width = blockText.getWidth( );
		}
		else if ( area instanceof InlineTextArea )
		{
			InlineTextArea inlineText = (InlineTextArea) area;
			InlineTextExtension ext = (InlineTextExtension) area.content
					.getExtension( IContent.LAYOUT_EXTENSION );
			ext.updatePageHintInfo( inlineText );
			
			sizeBasedContent.floatPos = ext.getFloatPos( );
			sizeBasedContent.offsetInContent = ext.getOffsetInContent( );
			sizeBasedContent.dimension = ext.getDimension( );
			sizeBasedContent.width = ext.getWidthRestrict( );
		}
		else
		{
			sizeBasedContent.floatPos = 0;
			sizeBasedContent.offsetInContent = 0;
			sizeBasedContent.dimension = 0;
			sizeBasedContent.width = 0;
		}
		return sizeBasedContent;
	}
	
	private void reset( )
	{
		startContent = null;
		currentContent = null;
		tableIds = new HashSet<String>( );
		fixedLayoutPageHints.clear( );
	}

	private Collection<String> getTableKeys( )
	{
		HashSet keys = new HashSet( );
		Iterator iter = tableIds.iterator( );
		while ( iter.hasNext( ) )
		{
			String tableId = (String) iter.next( );
			String key = htmlLayoutContext.getPageHintManager( ).getHintMapKey(
					tableId );
			keys.add( key );
		}
		return keys;
	}

	static class InstanceIDComparator
	{

		static boolean isNextWith( IContent content1, IContent content2 )
		{
			if ( content1 == null || content2 == null || content1 == content2 )
			{
				return false;
			}
			InstanceID id1 = content1.getInstanceID( );
			InstanceID id2 = content2.getInstanceID( );
			if ( id1 == null || id2 == null )
			{
				return false;
			}
			if ( id2.getUniqueID( ) == 0 )
			{
				// first child
				return equals( content1, (IContent) content2.getParent( ) );
			}
			else if ( ( content1 != null ) && content1.isLastChild( ) )
			{
				// cross level
				content1 = (IContent) content1.getParent( );
				return isNextWith( content1, content2 );
			}
			else if ( id1.getUniqueID( ) + 1 == id2.getUniqueID( ) )
			{
				// the siblings
				IContent parent1 = (IContent) content1.getParent( );
				IContent parent2 = (IContent) content2.getParent( );
				return equals( parent1, parent2 );
			}
			return false;
		}

		static boolean equals( IContent content1, IContent content2 )
		{
			if ( content1 == content2 )
				return true;
			if ( content1 == null )
			{
				if ( content2 == null )
					return true;
				else
					return false;
			}
			else
			{
				if ( content2 == null )
					return false;
			}
			InstanceID id1 = content1.getInstanceID( );
			InstanceID id2 = content2.getInstanceID( );
			if ( id1 == null || id2 == null )
				return false;
			if ( id1.getUniqueID( ) == id2.getUniqueID( ))
			{
				IContent parent1 = (IContent)content1.getParent( );
				IContent parent2 = (IContent)content2.getParent( );
				return equals( parent1, parent2 );	
			}
			else
			{
				return false;	
			}
		}
	}
}