/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.html.buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.presentation.TableColumnHint;

public class HTMLPageBuffer implements IPageBuffer {

	protected IContainerNode currentNode;
	protected PageHintGenerator generator;

	protected HTMLLayoutContext context;

	protected boolean isRepeated = false;
	protected boolean finished = false;

	protected ArrayList columnHints = new ArrayList();

	protected HashSet tableIds = new HashSet();

	public HTMLPageBuffer(HTMLLayoutContext context) {
		this.context = context;
		this.generator = new PageHintGenerator();
	}

	public void startContainer(IContent content, boolean isFirst, IContentEmitter emitter, boolean visible)
			throws BirtException {
		int type = content.getContentType();
		switch (type) {
		case IContent.TABLE_BAND_CONTENT:
		case IContent.LIST_BAND_CONTENT:
			boolean first = isFirst && !isRepeated;
			ContainerBufferNode bandNode = new ContainerBufferNode(content, emitter, generator, visible);
			setup(bandNode, first);
			currentNode = bandNode;
			break;
		case IContent.CELL_CONTENT:
			ContainerBufferNode cellNode = new ContainerBufferNode(content, emitter, generator, visible);
			setup(cellNode, isFirst);
			if (currentNode.isStarted()) {
				cellNode.start();
			}
			currentNode = cellNode;
			break;
		case IContent.PAGE_CONTENT:
			PageNode pageNode = new PageNode(content, emitter, generator, visible);
			setup(pageNode, isFirst);
			currentNode = pageNode;
			break;
		case IContent.TABLE_CONTENT:
			tableIds.add(content.getInstanceID().toUniqueString());
		default:
			ContainerBufferNode node = new ContainerBufferNode(content, emitter, generator, visible);
			setup(node, isFirst);
			currentNode = node;
			break;
		}
	}

	protected boolean isParentStarted() {
		INode parentNode = currentNode.getParent();
		if (parentNode != null) {
			return parentNode.isStarted();
		}
		return false;
	}

	public void startContent(IContent content, IContentEmitter emitter, boolean visible) throws BirtException {
		if (isRepeated || (!visible && !currentNode.isStarted())) {
			LeafBufferNode leafNode = new LeafBufferNode(content, emitter, generator, visible);
			setup(leafNode, true);
		} else {
			LeafBufferNode leafNode = new LeafBufferNode(content, emitter, generator, visible);
			setup(leafNode, true);
			currentNode.start();
			if (visible) {
				ContentEmitterUtil.startContent(content, emitter);
			}
			generator.start(content, true);
			generator.end(content, true);
			currentNode.removeChildren();
		}
	}

	public void endContainer(IContent content, boolean finished, IContentEmitter emitter, boolean visible)
			throws BirtException {
		int type = content.getContentType();
		switch (type) {
		case IContent.TABLE_BAND_CONTENT:
		case IContent.LIST_BAND_CONTENT:
			boolean isFinished = finished && !isRepeated;
			_endContainer(content, isFinished, emitter, visible);
			break;
		case IContent.PAGE_CONTENT:
			endPage(content, finished, emitter);
			break;
		case IContent.ROW_CONTENT:
			isFinished = finished && !isRepeated;
			endRow(content, finished, emitter, visible);
			break;
		case IContent.CELL_CONTENT:
			endCell(content, finished, emitter, visible);
			break;
		default:
			_endContainer(content, finished, emitter, visible);
			break;
		}

	}

	private void _endContainer(IContent content, boolean finished, IContentEmitter emitter, boolean visible)
			throws BirtException {
		((AbstractNode) currentNode).setFinished(finished);
		if (currentNode.isStarted()) {
			currentNode.end();
		} else {
			if (finished && !isRepeated) {
				if (visible) {
					currentNode.flush();
				} else if (isParentStarted()) {
					currentNode.flush();
				}
			}
		}

		currentNode = currentNode.getParent();
		if (currentNode != null && finished && !isRepeated) {
			if (visible) {
				currentNode.removeChildren();
			} else if (isParentStarted()) {
				if (((ContainerBufferNode) currentNode).isVisible) {
					currentNode.removeChildren();
				}
			}
		}
	}

	// Fix 256847
	protected void endRow(IContent content, boolean finished, IContentEmitter emitter, boolean visible)
			throws BirtException {
		((AbstractNode) currentNode).setFinished(finished);
		if (currentNode.isStarted()) {
			currentNode.end();
		} else {
			if (!isRepeated) {
				if (finished) {
					if (visible) {
						currentNode.flush();
					} else if (isParentStarted()) {
						currentNode.flush();
					}
				} else {
					if (allCellFinished((ContainerBufferNode) currentNode)) {
						currentNode.flush();
					}
				}
			}
		}

		currentNode = currentNode.getParent();
		if (currentNode != null && finished && !isRepeated) {
			if (visible) {
				currentNode.removeChildren();
			} else if (isParentStarted()) {
				if (((ContainerBufferNode) currentNode).isVisible) {
					currentNode.removeChildren();
				}
			}
		}
	}

	private boolean allCellFinished(ContainerBufferNode node) {
		if (node.children.size() > 0) {
			Iterator iter = node.children.iterator();
			while (iter.hasNext()) {
				AbstractNode cell = (AbstractNode) iter.next();
				if (!cell.finished) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	protected void endCell(IContent content, boolean finished, IContentEmitter emitter, boolean visible)
			throws BirtException {
		AbstractNode current = (AbstractNode) currentNode;
		// Fix 213900
		if (!current.isFirst) {
			current.setFinished(false);
		} else {
			current.setFinished(finished);
		}
		if (currentNode.isStarted()) {
			currentNode.end();
		} else {
			if (finished && !isRepeated) {
				/*
				 * currentNode.start( ); currentNode.end( );
				 */
			}
		}
		currentNode = currentNode.getParent();
	}

	public void endPage(IContent content, boolean finished, IContentEmitter emitter) throws BirtException {
		((AbstractNode) currentNode).setFinished(finished);
		if (currentNode.isStarted()) {
			context.getPageHintManager().generatePageRowHints(getTableKeys());
			currentNode.end();
			pageBreakEvent();
			if (!finished) {
				context.setPageNumber(context.getPageNumber() + 1);
				context.setPageCount(context.getPageCount() + 1);
			}
		} else {
			context.setEmptyPage(true);
			if (finished) {
				if (context.getPageNumber() == 1) {
					currentNode.flush();
					pageBreakEvent();
				} else {
					context.setPageNumber(context.getPageNumber() - 1);
					context.setPageCount(context.getPageCount() - 1);
				}
			}
		}
		this.finished = true;
		generator.reset();
		// context.removeLayoutHint( );
		context.getPageHintManager().clearPageHint();
		currentNode = null;
	}

	protected Collection<String> getTableKeys() {
		HashSet keys = new HashSet();
		Iterator iter = tableIds.iterator();
		while (iter.hasNext()) {
			String tableId = (String) iter.next();
			String key = context.getPageHintManager().getHintMapKey(tableId);
			keys.add(key);
		}
		return keys;
	}

	protected void pageBreakEvent() {
		context.getPageHintManager().setPageHint(generator.getPageHint());
		// context.addTableColumnHints( columnHints );
		long pageNumber = context.getPageNumber();
		ILayoutPageHandler pageHandler = context.getLayoutEngine().getPageHandler();
		if (pageHandler != null) {
			pageHandler.onPage(pageNumber, context);
		}

	}

	private void setup(AbstractNode node, boolean isFirst) {
		node.setFirst(isFirst);
		if (currentNode != null) {
			node.setParent(currentNode);
			currentNode.addChild(node);
		}
	}

	public boolean isRepeated() {
		return isRepeated;
	}

	public void setRepeated(boolean isRepeated) {
		this.isRepeated = isRepeated;
	}

	public void flush() throws BirtException {

	}

	public boolean finished() {
		return finished;
	}

	public void closePage(INode[] nodeList) throws BirtException {
		int length = nodeList.length;
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				AbstractNode node = (AbstractNode) nodeList[i];
				endContainer(node.content, false, node.emitter, true);
			}
		}
		finished = true;
	}

	public void openPage(INode[] nodeList) throws BirtException {
		int length = nodeList.length;
		if (length > 0) {
			for (int i = length - 1; i >= 0; i--) {
				AbstractNode node = (AbstractNode) nodeList[i];
				startContainer(node.content, false, node.emitter, true);
			}
		}
	}

	public INode[] getNodeStack() {
		ArrayList<INode> nodeList = new ArrayList<INode>();
		if (currentNode != null) {
			nodeList.add(currentNode);
			INode parent = currentNode.getParent();
			while (parent != null) {
				nodeList.add(parent);
				parent = parent.getParent();
			}
		}
		INode[] list = new INode[nodeList.size()];
		nodeList.toArray(list);
		return list;
	}

	public void addTableColumnHint(TableColumnHint hint) {
		context.getPageHintManager().addTableColumnHint(hint);
	}
}
