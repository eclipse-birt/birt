
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;

public abstract class PDFGroupLM extends PDFBlockStackingLM
		implements
			IBlockStackingLayoutManager
{

	protected IGroupContent groupContent;

	protected int repeatCount = 0;

	public PDFGroupLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
		groupContent = (IGroupContent) content;
	}

	protected boolean traverseChildren( )
	{
		repeatHeader( );
		boolean childBreak = super.traverseChildren( );
		return childBreak;
	}

	protected boolean isRepeatHeader( )
	{
		return ( (IGroupContent) content ).isHeaderRepeat( );
	}

	protected abstract void repeatHeader( );

	protected boolean isRootEmpty( )
	{
		return !( root != null && root.getChildrenCount( ) > repeatCount );
	}

}
