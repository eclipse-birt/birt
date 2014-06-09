
package org.eclipse.birt.report.engine.emitter.pptx;

import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;

public class SlideWriter
{

	private PPTXRender render;

	public SlideWriter( PPTXRender render )
	{
		this.render = render;
	}

	public void outputSlide( PageArea pageArea )
	{
		render.visitPage( pageArea );
	}

}
