
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class PDFLeafItemLM extends PDFAbstractLM
		implements
			ILayoutManager
{

	public PDFLeafItemLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
	}

	protected void cancelChildren( )
	{

	}

	public boolean isInlineFlow( )
	{
		return true;
	}

	protected boolean isPageBreakBefore( )
	{
		if ( content != null )
		{
			if ( PropertyUtil.isInlineElement( content ) )
			{
				return super.isPageBreakBefore( );
			}
		}
		return false;
	}

	protected boolean isPageBreakAfter( )
	{
		if ( content != null )
		{
			if ( PropertyUtil.isInlineElement( content ) )
			{
				return super.isPageBreakAfter( );
			}
		}
		return false;
	}

	public boolean allowPageBreak( )
	{
		return false;
	}

}
