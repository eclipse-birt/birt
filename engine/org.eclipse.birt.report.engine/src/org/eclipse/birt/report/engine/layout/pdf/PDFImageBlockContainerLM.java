
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.content.ItemExecutorWrapper;
import org.eclipse.birt.report.engine.layout.content.LineStackingExecutor;

public class PDFImageBlockContainerLM extends PDFBlockContainerLM implements IBlockStackingLayoutManager {

	public PDFImageBlockContainerLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		child = new PDFLineAreaLM(context, this,
				new LineStackingExecutor(new ItemExecutorWrapper(executor, content), executor));
	}

	protected boolean traverseChildren() throws BirtException {
		return traverseSingleChild();
	}

	protected void closeLayout() {
		/**
		 * set root height. For Image block container, OffsetY and box property should
		 * be zero
		 */
		root.setHeight(getCurrentBP());
	}

	protected void createRoot() {
		root = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
	}

	protected void closeExecutor() {

	}

}
