/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.HashMap;
import java.util.Stack;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;

public class WrappedPDFLayoutEmitter extends LayoutEmitterAdapter implements IContentEmitter {
	private PDFLayoutEmitter layoutEmitter;

	public WrappedPDFLayoutEmitter(IReportExecutor executor, IContentEmitter emitter, LayoutEngineContext context) {
		layoutEmitter = new PDFLayoutEmitter(executor, emitter, context);
		layoutEmitter.context.setCachedHeaderMap(cachedTableHeaders, cachedGroupHeaders);
	}

//	public WrappedPDFLayoutEmitter( LayoutEngineContext context )
//	{
//		layoutEmitter = new PDFLayoutEmitter( context );
//		layoutEmitter.context.setCachedHeaderMap( cachedTableHeaders, cachedGroupHeaders );
//	}

	public void initialize(IEmitterServices service) throws BirtException {
		layoutEmitter.initialize(service);
	}

	public String getOutputFormat() {
		return layoutEmitter.getOutputFormat();
	}

	public void start(IReportContent report) throws BirtException {
		layoutEmitter.start(report);
	}

	public void end(IReportContent report) throws BirtException {
		layoutEmitter.end(report);
	}

	protected void resolveTotalPage(IContentEmitter emitter) throws BirtException {
		layoutEmitter.resolveTotalPage(emitter);
	}

	public void startContainer(IContainerContent container) throws BirtException {
		layoutEmitter.startContainer(container);
		if (isInHeader()) {
			constructClonedContent(container);
		}
	}

	public void endContainer(IContainerContent container) throws BirtException {
		layoutEmitter.endContainer(container);
		if (isInHeader()) {
			destructClonedContent();
		}
	}

	public void startContent(IContent content) throws BirtException {
		layoutEmitter.startContent(content);
		if (isInHeader()) {
			if (content instanceof IContainerContent) {
				constructClonedContent((IContainerContent) content);
			} else {
				IContainerContent pContent = (IContainerContent) parentContents.peek();
				pContent.getChildren().add(content);
			}
		}
	}

	public void endContent(IContent content) {
		layoutEmitter.endContent(content);
		if (isInHeader()) {
			if (content instanceof IContainerContent) {
				destructClonedContent();
			}
		}
	}

	public void startTable(ITableContent table) throws BirtException {
		layoutEmitter.startTable(table);
		if (isInHeader()) {
			constructClonedContent(table);
		}
	}

	public void endTable(ITableContent table) throws BirtException {
		layoutEmitter.endTable(table);
		InstanceID tableID = table.getInstanceID();
		removeCachedTableHeader(tableID);
		if (isInHeader()) {
			destructClonedContent();
		}

	}

	public void startListBand(IListBandContent listBand) {
		layoutEmitter.startListBand(listBand);
		if (isInHeader()) {
			constructClonedContent(listBand);
		}
	}

	public void endListBand(IListBandContent listBand) {
		layoutEmitter.endListBand(listBand);
		if (isInHeader()) {
			destructClonedContent();
		}
	}

	public void startListGroup(IListGroupContent listGroup) throws BirtException {
		layoutEmitter.startListGroup(listGroup);
		if (isInHeader()) {
			constructClonedContent(listGroup);
		}
	}

	public void endListGroup(IListBandContent listGroup) {
		layoutEmitter.endListBand(listGroup);
		if (isInHeader()) {
			destructClonedContent();
		}
	}

	public void startPage(IPageContent page) throws BirtException {
		layoutEmitter.startPage(page);
	}

	public void outputPage(IPageContent page) throws BirtException {
		layoutEmitter.outputPage(page);
	}

	public void endPage(IPageContent page) throws BirtException {
		layoutEmitter.endPage(page);
	}

//	protected void startTableContainer(IContainerContent container)
//	{
//		layoutEmitter.startTableContainer( container );
//		if ( isInHeader( ) )
//		{
//			constructClonedContent( container );
//		}
//	}
//	
//	protected void endTableContainer(IContainerContent container)
//	{
//		layoutEmitter.endTableContainer( container );
//		if ( container instanceof ITableContent )
//		{
//			InstanceID tableID = ((ITableContent)container).getInstanceID( );
//			removeCachedTableHeader( tableID );
//		}
//		if ( isInHeader( ) )
//		{
//			destructClonedContent( );
//		}
//	}

	public void startRow(IRowContent row) throws BirtException {
		layoutEmitter.startRow(row);
		if (isInHeader()) {
			constructClonedContent(row);
		}
	}

	public void endRow(IRowContent row) throws BirtException {
		layoutEmitter.endRow(row);
		if (isInHeader()) {
			destructClonedContent();
		}
	}

	public void startTableBand(ITableBandContent band) throws BirtException {
		layoutEmitter.startTableBand(band);

		if (band.getBandType() == ITableBandContent.BAND_GROUP_HEADER) {
			IElement group = band.getParent();
			if (group instanceof ITableGroupContent && ((ITableGroupContent) group).isHeaderRepeat()) {
				InstanceID id = ((ITableGroupContent) group).getInstanceID();
				repeatedHeaderLevel++;
				ITableBandContent clonedBand = (ITableBandContent) constructClonedContent(band);
				createCachedGroupHeader(id, clonedBand);
			}
		} else if (band.getBandType() == ITableBandContent.BAND_HEADER) {
			IElement table = band.getParent();
			if (table instanceof ITableContent && ((ITableContent) table).isHeaderRepeat()) {
				InstanceID tableID = ((ITableContent) table).getInstanceID();
				repeatedHeaderLevel++;
				ITableBandContent clonedBand = (ITableBandContent) constructClonedContent(band);
				createCachedTableHeader(tableID, clonedBand);
			}
		} else if (isInHeader()) {
			constructClonedContent(band);
		}

	}

	public void endTableBand(ITableBandContent band) throws BirtException {
		layoutEmitter.endTableBand(band);
		if (isInHeader()) {
			destructClonedContent();
			if (band.getBandType() == ITableBandContent.BAND_GROUP_HEADER) {
				repeatedHeaderLevel--;
			}
			if (band.getBandType() == ITableBandContent.BAND_HEADER) {
				repeatedHeaderLevel--;
			}
		}
	}

	public void startTableGroup(ITableGroupContent group) throws BirtException {
		layoutEmitter.startTableGroup(group);
		if (isInHeader()) {
			constructClonedContent(group);
		}
	}

	public void endTableGroup(ITableGroupContent group) throws BirtException {
		layoutEmitter.endTableGroup(group);
		removeCachedGroupHeader(group.getInstanceID());
		if (isInHeader()) {
			destructClonedContent();
		}
	}

	public void startCell(ICellContent cell) throws BirtException {
		layoutEmitter.startCell(cell);
		if (isInHeader()) {
			constructClonedContent(cell);
		}
	}

	public void endCell(ICellContent cell) throws BirtException {
		layoutEmitter.endCell(cell);
		if (isInHeader()) {
			destructClonedContent();
		}
	}

//	protected void visitContent( IContent content, IContentEmitter emitter)
//	{
//		layoutEmitter.visitContent( content, emitter );
//	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		layoutEmitter.startForeign(foreign);
		if (isInHeader()) {
			IContainerContent pContent = (IContainerContent) parentContents.peek();
			pContent.getChildren().add(foreign);
		}
	}

	public ILayoutPageHandler getPageHandler() {
		return layoutEmitter.getPageHandler();
	}

	public void setPageHandler(ILayoutPageHandler pageHandler) {
		layoutEmitter.setPageHandler(pageHandler);
	}

	/**
	 * the counter plus one when entering a header which should be repeated, and
	 * minus one when exiting a header which should be repeated.
	 */
	private int repeatedHeaderLevel = 0;

	private Stack parentContents = new Stack();

	private boolean isInHeader() {
		return repeatedHeaderLevel > 0 ? true : false;
	}

	/**
	 * Key: table instance id/group instance id. Value: tableHeader/groupHeader.
	 */
	private HashMap cachedGroupHeaders = new HashMap();
	private HashMap cachedTableHeaders = new HashMap();

	private void createCachedGroupHeader(InstanceID id, ITableBandContent header) {
		cachedGroupHeaders.put(id, header);
	}

	private void removeCachedGroupHeader(InstanceID id) {
		cachedGroupHeaders.remove(id);
	}

	private void createCachedTableHeader(InstanceID id, ITableBandContent header) {
		cachedTableHeaders.put(id, header);
	}

	private void removeCachedTableHeader(InstanceID id) {
		cachedTableHeaders.remove(id);
	}

	private IContainerContent constructClonedContent(IContainerContent container) {
		if (parentContents.isEmpty()) {
			IContainerContent clonedContainer = (IContainerContent) container.cloneContent(false);
			clonedContainer.setParent(container.getParent());
			// clonedContainer.getChildren( ).clear( );
			parentContents.push(clonedContainer);
			return clonedContainer;
		} else {
			IContainerContent pContent = (IContainerContent) parentContents.peek();
			IContainerContent clonedContainer = (IContainerContent) container.cloneContent(false);
			clonedContainer.setParent(container.getParent());
			// clonedContainer.getChildren( ).clear( );
			pContent.getChildren().add(clonedContainer);
			parentContents.push(clonedContainer);
			return clonedContainer;
		}
	}

	private void destructClonedContent() {
		if (!parentContents.isEmpty()) {
			parentContents.pop();
		}
	}

}
