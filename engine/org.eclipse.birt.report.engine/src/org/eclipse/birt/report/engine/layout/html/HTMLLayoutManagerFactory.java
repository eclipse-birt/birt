/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.util.FastPool;

public class HTMLLayoutManagerFactory {

	private HTMLReportLayoutEngine engine;

	public HTMLLayoutManagerFactory(HTMLReportLayoutEngine engine) {
		this.engine = engine;
	}

	public HTMLReportLayoutEngine getLayoutEngine() {
		return engine;
	}

	public HTMLAbstractLM createLayoutManager(HTMLAbstractLM parent, IContent content, IReportItemExecutor executor,
			IContentEmitter emitter) throws BirtException {
		HTMLAbstractLM layout = getLayoutManager(content);
		layout.initialize(parent, content, executor, emitter);
		return layout;
	}

	FastPool freeLeaf = new FastPool();
	FastPool freeBlock = new FastPool();
	FastPool freeTable = new FastPool();
	FastPool freeTableBand = new FastPool();
	FastPool freeRow = new FastPool();
	FastPool freeList = new FastPool();
	FastPool freeGroup = new FastPool();
	FastPool freeListBand = new FastPool();

	public void releaseLayoutManager(HTMLAbstractLM manager) {
		switch (manager.getType()) {
		case HTMLAbstractLM.LAYOUT_MANAGER_LEAF:
			freeLeaf.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_BLOCK:
			freeBlock.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_TABLE:
			freeTable.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_TABLE_BAND:
			freeTableBand.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_ROW:
			freeRow.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_LIST:
			freeList.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_GROUP:
			freeGroup.add(manager);
			break;
		case HTMLAbstractLM.LAYOUT_MANAGER_LIST_BAND:
			freeListBand.add(manager);
			break;
		}
	}

	private HTMLAbstractLM getLayoutManager(IContent content) throws BirtException {
		int type = content.getContentType();
		switch (type) {
		case IContent.DATA_CONTENT:
		case IContent.FOREIGN_CONTENT:
		case IContent.AUTOTEXT_CONTENT:
		case IContent.IMAGE_CONTENT:
		case IContent.LABEL_CONTENT:
		case IContent.SERIALIZE_CONTENT:
		case IContent.TEXT_CONTENT:
			return createLeafLM();
		case IContent.CELL_CONTENT:
		case IContent.CONTAINER_CONTENT:
			return createContainerLM();
		case IContent.GROUP_CONTENT:
		case IContent.LIST_GROUP_CONTENT:
		case IContent.TABLE_GROUP_CONTENT:
			return createGroupLM();
		case IContent.LIST_BAND_CONTENT:
			return createListBandLM();
		case IContent.LIST_CONTENT:
			return createListLM();
		case IContent.ROW_CONTENT:
			return createRowLM();
		case IContent.TABLE_BAND_CONTENT:
			return createTableBandLM();
		case IContent.TABLE_CONTENT:
			return createTableLM();
		default:
			throw new IllegalStateException();
		}
	}

	private HTMLLeafItemLM createLeafLM() {
		if (!freeLeaf.isEmpty()) {
			return (HTMLLeafItemLM) freeLeaf.remove();
		}
		return new HTMLLeafItemLM(this);
	}

	private HTMLAbstractLM createContainerLM() {
		if (!freeBlock.isEmpty()) {
			return (HTMLBlockStackingLM) freeBlock.remove();
		}
		return new HTMLBlockStackingLM(this);
	}

	private HTMLAbstractLM createTableLM() {
		if (!freeTable.isEmpty()) {
			return (HTMLTableLM) freeTable.remove();
		}
		return new HTMLTableLM(this);
	}

	private HTMLAbstractLM createGroupLM() {
		if (!freeGroup.isEmpty()) {
			return (HTMLGroupLM) freeGroup.remove();
		}
		return new HTMLGroupLM(this);
	}

	private HTMLAbstractLM createTableBandLM() {
		if (!freeTableBand.isEmpty()) {
			return (HTMLTableBandLM) freeTableBand.remove();
		}
		return new HTMLTableBandLM(this);
	}

	private HTMLAbstractLM createRowLM() {
		if (!freeRow.isEmpty()) {
			return (HTMLRowLM) freeRow.remove();
		}
		return new HTMLRowLM(this);
	}

	private HTMLAbstractLM createListLM() {
		if (!freeList.isEmpty()) {
			return (HTMLListLM) freeList.remove();
		}
		return new HTMLListLM(this);

	}

	private HTMLAbstractLM createListBandLM() {
		if (!freeListBand.isEmpty()) {
			return (HTMLListingBandLM) freeListBand.remove();
		}
		return new HTMLListingBandLM(this);
	}
}
