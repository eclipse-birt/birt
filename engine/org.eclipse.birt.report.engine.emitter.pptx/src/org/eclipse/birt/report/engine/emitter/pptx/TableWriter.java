
package org.eclipse.birt.report.engine.emitter.pptx;

import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea;

public class TableWriter
{

	private PPTXRender render;

	public TableWriter( PPTXRender render )
	{
		this.render = render;
	}

	public void outputTable( TableArea table )
	{
		render.visitTable( table );
	}
}
