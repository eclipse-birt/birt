
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class PDFLeafItemLM extends PDFAbstractLM
		implements
			ILayoutManager
{

	public PDFLeafItemLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content,  executor );
	}

	protected void cancelChildren( )
	{

	}

	/**
	 * need overwrite this mothod to change default behavour.
	 * For inline leaf elements, page-break is handled by this layout manager.
	 * For block leaf elements. page-break is handled by it's block container
	 */
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

	/**
	 * need overwrite this mothod to change default behavour.
	 * For inline leaf elements, page-break is handled by this layout manager.
	 * For block leaf elements. page-break is handled by it's block container
	 */
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
