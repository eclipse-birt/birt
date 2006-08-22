
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
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
	
	 
	
	/**
	 * create inline container area by content
	 * @param content the content object
	 * @param isFirst if this area is the first area of the content
	 * @param isLast if this area is the last area of the content
	 * @return
	 */
	protected IContainerArea createInlineContainer(IContent content, boolean isFirst, boolean isLast)
	{
		IContainerArea containerArea = AreaFactory.createInlineContainer( content );
		IStyle style = containerArea.getStyle();
		//remove left padding, border and margin if it is not the first child
		if(!isFirst)
		{
			style.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
		}
		//remove right padding, border and margin if it is not the last child
		if(!isLast)
		{
			style.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0);
			style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
		}
		return containerArea;
	}

}
