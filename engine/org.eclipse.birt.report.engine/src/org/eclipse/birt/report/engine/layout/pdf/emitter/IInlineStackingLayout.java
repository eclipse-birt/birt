package org.eclipse.birt.report.engine.layout.pdf.emitter;



public interface IInlineStackingLayout
{
	boolean endLine( );

	boolean isEmptyLine( );
	
	int getMaxLineWidth();
	
	
}
