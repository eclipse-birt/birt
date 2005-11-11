package org.eclipse.birt.report.engine.emitter;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;


public class CompositeContentEmitter extends ContentEmitterAdapter
{
	public ArrayList emitters = new ArrayList();
	CompositeContentEmitter()
	{
	}
	
	CompositeContentEmitter (IContentEmitter emitter)
	{
		emitters.add(emitter);
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

	public void endTable( ITableContent table )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endTable(table);
		}
	}

	public void endTableBody( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endTableBody(band);
		}
	}

	public void endTableFooter( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endTableFooter(band);
		}
	}

	public void endTableHeader( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).endTableHeader(band);
		}
	}

	public String getOutputFormat( )
	{
		return "mutliple";
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

	public void startTableBody( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startTableBody(band);
		}
	}

	public void startTableFooter( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startTableFooter(band);
		}
	}

	public void startTableHeader( ITableBandContent band )
	{
		for (int i = 0; i < emitters.size(); i++)
		{
			((IContentEmitter)emitters.get(i)).startTableHeader(band);
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
