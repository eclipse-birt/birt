
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.content.ListContainerExecutor;

public class PDFListGroupLM extends PDFGroupLM implements IBlockStackingLayoutManager {
	protected boolean needRepeat = false;

	public PDFListGroupLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
	}

	protected IListBandContent getHeader() {
		return (IListBandContent) ((IGroupContent) content).getHeader();
	}

	protected IReportItemExecutor createExecutor() {
		return new ListContainerExecutor(content, executor);
	}

	protected void repeatHeader() throws BirtException {
		if (isFirst) {
			return;
		}
		if (!needRepeat || !isRepeatHeader() || !isCurrentDetailBand()) {
			return;
		}
		IListBandContent band = getHeader();
		if (band == null) {
			return;
		}
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor(band);
		headerExecutor.execute();
		ContainerArea headerArea = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		headerArea.setAllocatedWidth(parent.getCurrentMaxContentWidth());
		PDFRegionLM regionLM = new PDFRegionLM(context, headerArea, band, headerExecutor);
		boolean allowPB = context.allowPageBreak();
		context.setAllowPageBreak(false);
		regionLM.layout();
		context.setAllowPageBreak(allowPB);
		if (headerArea.getAllocatedHeight() < getCurrentMaxContentHeight())// FIXME need check
		{
			addArea(headerArea, false, pageBreakAvoid);
			repeatCount++;
		}
		needRepeat = false;

	}

	protected void createRoot() {
		if (root == null) {
			root = (ContainerArea) AreaFactory.createBlockContainer(content);
		}
	}

	protected void initialize() throws BirtException {
		if (root == null && keepWithCache.isEmpty() && !isFirst) {
			repeatCount = 0;
			needRepeat = true;
		}
		super.initialize();

	}

	protected boolean isCurrentDetailBand() {
		if (child != null) {
			IContent c = child.getContent();
			if (c != null) {
				if (c instanceof IGroupContent) {
					return true;
				}
				IElement p = c.getParent();
				if (p instanceof IBandContent) {
					IBandContent band = (IBandContent) p;
					if (band.getBandType() == IBandContent.BAND_DETAIL) {
						return true;
					}
				}

			}
		} else {
			return true;
		}
		return false;
	}
}
