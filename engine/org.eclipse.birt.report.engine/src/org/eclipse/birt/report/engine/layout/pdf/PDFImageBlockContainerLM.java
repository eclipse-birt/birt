
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.content.ItemExecutorWrapper;
import org.eclipse.birt.report.engine.layout.content.LineStackingExecutor;

public class PDFImageBlockContainerLM extends PDFBlockContainerLM
		implements
			IBlockStackingLayoutManager
{

	public PDFImageBlockContainerLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IContentEmitter emitter,
			IReportItemExecutor executor )
	{
		super( context, parent, content, emitter, executor );
		child = new PDFLineAreaLM( context, this, emitter,
				new LineStackingExecutor( new ItemExecutorWrapper( executor,
						content ), executor ) );
	}

	protected boolean traverseChildren( )
	{
		return traverseSingleChild( );
	}

	protected void closeLayout( )
	{
		assert ( parent != null );
		IStyle areaStyle = root.getStyle( );
		// set dimension property for root TODO suppport user defined height
		root
				.setHeight( getCurrentBP( )
						+ getOffsetY( )
						+ getDimensionValue( areaStyle
								.getProperty( StyleConstants.STYLE_PADDING_BOTTOM ) )
						+ getDimensionValue( areaStyle
								.getProperty( StyleConstants.STYLE_BORDER_BOTTOM_WIDTH ) ) );
	}

	protected void createRoot( )
	{
		super.createRoot( );
		IStyle style = root.getStyle( );
		removeBoxProperty( root.getStyle( ) );
		style.setProperty( StyleConstants.STYLE_BACKGROUND_IMAGE, IStyle.NONE_VALUE );
		style.setProperty( StyleConstants.STYLE_BACKGROUND_COLOR, IStyle.AUTO_VALUE );
	}

	protected void closeExecutor( )
	{

	}

}
