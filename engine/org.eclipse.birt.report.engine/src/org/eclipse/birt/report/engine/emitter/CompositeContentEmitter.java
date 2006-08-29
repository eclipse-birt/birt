/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * Emitter the input to mutiple outputs.
 *
 * @version $Revision: 1.3 $ $Date: 2006/06/13 15:37:15 $
 */
public class CompositeContentEmitter extends ContentEmitterAdapter
{
	protected ArrayList emitters = new ArrayList();

	protected String format = "mutliple";
	
	public CompositeContentEmitter()
	{
	}
	
	public CompositeContentEmitter(String format)
	{
		this.format = format;
	}
	
	public void addEmitter(IContentEmitter emitter)
	{
		emitters.add(emitter);
	}

	public void end( IReportContent report )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).end(report);
		}
	}

	public void endGroup( IGroupContent group )
	{
		for (int i = 0; i < emitters.size( ); i++)
		{
			((IContentEmitter)emitters.get(i)).endGroup(group);
		}
	}


	public void endList( IListContent list )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).endList( list );
		}
	}


	public void endListGroup( IListGroupContent group )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).endListGroup( group );
		}
	}


	public void endTableGroup( ITableGroupContent group )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).endTableGroup( group );
		}
	}


	public void startAutoText( IAutoTextContent autoText )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).startAutoText( autoText );
		}
	}


	public void startGroup( IGroupContent group )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).startGroup( group );
		}
	}


	public void startListGroup( IListGroupContent group )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).startListGroup( group );
		}
	}


	public void startTableGroup( ITableGroupContent group )
	{
		for ( int i = 0; i < emitters.size( ); i++ )
		{
			( (IContentEmitter) emitters.get( i ) ).startTableGroup( group );
		}
	}


	public void endCell( ICellContent cell )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endCell(cell);
		}
	}

	public void endContainer( IContainerContent container )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endContainer(container);
		}
	}

	public void endContent( IContent content )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endContent(content);
		}
	}

	public void endPage( IPageContent page )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endPage(page);
		}
	}

	public void endRow( IRowContent row )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endRow(row);
		}
	}
	public void startTableBand( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startTableBand(band);
		}
	}
	
	public void endTableBand( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endTableBand(band);
		}
	}
	public void endTable( ITableContent table )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endTable(table);
		}
	}
	

	public String getOutputFormat( )
	{
		return format;
	}

	public void initialize( IEmitterServices service )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).initialize(service);
		}
	}

	public void start( IReportContent report )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).start(report);
		}
	}

	public void startCell( ICellContent cell )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startCell(cell);
		}
	}

	public void startContainer( IContainerContent container )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startContainer(container);
		}
	}

	public void startContent( IContent content )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startContent(content);
		}
	}

	public void startData( IDataContent data )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startData(data);
		}
	}

	public void startForeign( IForeignContent foreign )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startForeign(foreign);
		}
	}

	public void startImage( IImageContent image )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startImage(image);
		}
	}

	public void startLabel( ILabelContent label )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startLabel(label);
		}
	}

	public void startPage( IPageContent page )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startPage(page);
		}
	}

	public void startRow( IRowContent row )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startRow(row);
		}
	}

	public void startTable( ITableContent table )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startTable(table);
		}
	}

	public void startListBand( IListBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startListBand(band);
		}
	}
	public void endListBand( IListBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endListBand(band);
		}
	}

	public void startList( IListContent list )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startList(list);
		}
	}

	public void startText( ITextContent text )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startText(text);
		}
	}

}
