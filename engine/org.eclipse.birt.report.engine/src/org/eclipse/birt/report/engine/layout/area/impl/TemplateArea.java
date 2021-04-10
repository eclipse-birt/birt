package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.ITemplateArea;

public class TemplateArea extends ContainerArea implements ITemplateArea {
	TemplateArea(IAutoTextContent autoText) {
		super(autoText);
	}

	public void accept(IAreaVisitor visitor) {
		visitor.visitAutoText(this);
	}

}