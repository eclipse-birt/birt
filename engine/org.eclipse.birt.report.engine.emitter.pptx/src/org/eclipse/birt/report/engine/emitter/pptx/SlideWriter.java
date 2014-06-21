
package org.eclipse.birt.report.engine.emitter.pptx;

import org.eclipse.birt.report.engine.emitter.pptx.writer.SlideMaster;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;

public class SlideWriter
{

	private PPTXRender render;

	public SlideWriter( PPTXRender render )
	{
		this.render = render;
	}

	public void writeSlideMaster( SlideMaster slide, PageArea pageArea )
	{
		PPTXRender masterRender = new PPTXRender( this.render,
				slide.getCanvas( ) );
		
		IContainerArea pageHeader = pageArea.getHeader( );
		if ( pageHeader != null )
		{
			pageHeader.accept( masterRender );
		}
		IContainerArea pageFooter = pageArea.getFooter( );
		if ( pageFooter != null )
		{
			pageFooter.accept( masterRender );
		}
	}

	public void writeSlide( PageArea pageArea )
	{
		pageArea.getBody( ).accept( render );
	}
}
