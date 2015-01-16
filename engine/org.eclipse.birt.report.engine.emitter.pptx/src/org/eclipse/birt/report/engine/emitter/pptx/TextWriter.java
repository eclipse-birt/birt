
package org.eclipse.birt.report.engine.emitter.pptx;

import org.eclipse.birt.report.engine.nLayout.area.impl.BlockTextArea;

public class TextWriter
{

	private PPTXRender render;

	public TextWriter( PPTXRender render )
	{
		this.render = render;
	}

	public void outputText( BlockTextArea text )
	{
		render.visitText( text );
	}
}
