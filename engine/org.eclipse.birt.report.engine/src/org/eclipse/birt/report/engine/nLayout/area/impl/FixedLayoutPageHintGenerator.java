/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.internal.content.wrap.AbstractContentWrapper;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutPageHintManager;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

public class FixedLayoutPageHintGenerator {
	protected SizeBasedContent startContent = null;
	protected SizeBasedContent currentContent = null;

	protected HTMLLayoutContext htmlLayoutContext = null;
	private ArrayList<SizeBasedContent[]> fixedLayoutPageHints = new ArrayList<SizeBasedContent[]>();

	protected HashSet<String> tableIds = new HashSet<String>();
	HashMap<String, UnresolvedRowHint> htmlUnresolvedRowHints = null;
	HashMap<String, UnresolvedRowHint> currentPageUnresolvedRowHints = null;
	HashMap<String, UnresolvedRowHint> docUnresolvedRowHints = new HashMap<String, UnresolvedRowHint>();

	protected LayoutContext context;

	public FixedLayoutPageHintGenerator(LayoutContext context) {
		this.context = context;
		htmlLayoutContext = context.getHtmlLayoutContext();
	}

	public ArrayList getPageHint() {
		return fixedLayoutPageHints;
	}

	public void addUnresolvedRowHint(String tableId, UnresolvedRowHint hint) {
		if (currentPageUnresolvedRowHints == null) {
			currentPageUnresolvedRowHints = new HashMap<String, UnresolvedRowHint>();
		}
		currentPageUnresolvedRowHints.put(htmlLayoutContext.getPageHintManager().getHintMapKey(tableId), hint);
	}

	public void resetRowHint() {
		docUnresolvedRowHints.clear();
		if (currentPageUnresolvedRowHints != null) {
			docUnresolvedRowHints.putAll(currentPageUnresolvedRowHints);
			currentPageUnresolvedRowHints.clear();
		}
	}

	public List<UnresolvedRowHint> getUnresolvedRowHints() {
		ArrayList<UnresolvedRowHint> unresolvedRowHintsList = new ArrayList<UnresolvedRowHint>();
		Iterator<String> iter = getTableKeys().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (docUnresolvedRowHints != null) {
				UnresolvedRowHint hint = docUnresolvedRowHints.get(key);
				if (hint != null) {
					unresolvedRowHintsList.add(hint);
					continue;
				}
			}
			UnresolvedRowHint hint = htmlUnresolvedRowHints.get(key);
			if (hint != null) {
				unresolvedRowHintsList.add(hint);
			}
		}
		return unresolvedRowHintsList;
	}

	public void generatePageHints(IPageContent page) throws BirtException {
		PageArea pageArea = (PageArea) page.getExtension(IContent.LAYOUT_EXTENSION);
		if (pageArea != null) {
			reset();
			for (Iterator<IArea> i = pageArea.getBody().getChildren(); i.hasNext();) {
				IArea child = i.next();
				traverse(child);
			}
			HTMLLayoutPageHintManager pm = htmlLayoutContext.getPageHintManager();
			pm.generatePageRowHints(getTableKeys());
			htmlUnresolvedRowHints = pm.getUnresolvedRowHints();
		}
		if (startContent != null) {
			if (currentContent != null) {
				fixedLayoutPageHints.add(new SizeBasedContent[] { startContent, currentContent });
			}
		}
	}

	private void traverse(IArea area) {
		if (area instanceof ContainerArea) {
			if (area instanceof TableArea) {
				InstanceID instanceID = ((TableArea) area).getContent().getInstanceID();
				// The table content may be generated from a section of HTML
				// text, which content does not have an instance id.
				if (instanceID != null) {
					tableIds.add(instanceID.toUniqueString());
				}
			}
			ContainerArea container = (ContainerArea) area;
			if (container.content != null && !container.isDummy && container.content.getInstanceID() != null) {
				start(container);
			}
			if (!(container.content instanceof ForeignContent)) {
				for (Iterator<IArea> i = container.getChildren(); i.hasNext();) {
					IArea child = i.next();
					traverse(child);
				}
			}
		}
		String bookmark = area.getBookmark();
		if (bookmark != null) {
			context.addBookmarkMap(context.getPageNumber(), bookmark);
		}
	}

	private void start(ContainerArea area) {
		// AbstractArea.debugPrint2( area );
		if (startContent == null) {
			startContent = createSizeBasedContent(area);
			currentContent = startContent;
		} else {
			if (currentContent != null) {
				if (InstanceIDComparator.isNextWith(currentContent.content, currentContent.isChildrenRemoved,
						area.content)) {
					if (currentContent.dimension > 0) {
						fixedLayoutPageHints.add(new SizeBasedContent[] { startContent, currentContent });
						startContent = createSizeBasedContent(area);
						currentContent = startContent;
						return;
					}
					currentContent = createSizeBasedContent(area);
				} else if (InstanceIDComparator.equals(currentContent.content, area.content)) {
					// Does nothing. this case is for inline text.
				} else {
					fixedLayoutPageHints.add(new SizeBasedContent[] { startContent, currentContent });
					startContent = createSizeBasedContent(area);
					currentContent = startContent;
				}
			}
		}
	}

	private SizeBasedContent createSizeBasedContent(ContainerArea area) {
		SizeBasedContent sizeBasedContent = new SizeBasedContent();
		if (area.content instanceof AbstractContentWrapper) {
			sizeBasedContent.content = ((AbstractContentWrapper) area.content).getContent();
		} else {
			sizeBasedContent.content = area.content;
		}

		if (area instanceof BlockTextArea) {
			BlockTextArea blockText = (BlockTextArea) area;
			sizeBasedContent.floatPos = 0;
			ArrayList<BlockTextArea> list = (ArrayList<BlockTextArea>) area.content
					.getExtension(IContent.LAYOUT_EXTENSION);
			if (list.size() > 1) {
				Iterator<BlockTextArea> i = list.iterator();
				int offsetInContent = 0;
				int lastHeight = 0;
				while (i.hasNext()) {
					offsetInContent += lastHeight;
					BlockTextArea current = i.next();
					if (current == area) {
						break;
					}
					lastHeight = current.getContentHeight();
				}
				sizeBasedContent.offsetInContent = offsetInContent;
				sizeBasedContent.dimension = blockText.getContentHeight();
				sizeBasedContent.width = blockText.getWidth();
			} else if (list.size() == 1) {
				sizeBasedContent.offsetInContent = 0;
				if (list.get(0).finished) {
					sizeBasedContent.dimension = -1;
					sizeBasedContent.width = -1;
				} else {
					sizeBasedContent.dimension = blockText.getContentHeight();
					sizeBasedContent.width = blockText.getWidth();
				}
			} else {
				sizeBasedContent.offsetInContent = 0;
				sizeBasedContent.dimension = -1;
				sizeBasedContent.width = -1;
			}
		} else if (area instanceof InlineTextArea) {
			InlineTextArea inlineText = (InlineTextArea) area;
			InlineTextExtension ext = (InlineTextExtension) area.content.getExtension(IContent.LAYOUT_EXTENSION);
			ext.updatePageHintInfo(inlineText);

			sizeBasedContent.floatPos = ext.getFloatPos();
			sizeBasedContent.offsetInContent = ext.getOffsetInContent();
			sizeBasedContent.dimension = ext.getDimension();
			sizeBasedContent.width = ext.getWidthRestrict();
		} else {
			sizeBasedContent.floatPos = -1;
			sizeBasedContent.offsetInContent = -1;
			sizeBasedContent.dimension = -1;
			sizeBasedContent.width = -1;
		}
		sizeBasedContent.isChildrenRemoved = area.isChildrenRemoved;
		return sizeBasedContent;
	}

	private void reset() {
		startContent = null;
		currentContent = null;
		tableIds = new HashSet<String>();
		fixedLayoutPageHints.clear();
	}

	private Collection<String> getTableKeys() {
		HashSet keys = new HashSet();
		Iterator iter = tableIds.iterator();
		while (iter.hasNext()) {
			String tableId = (String) iter.next();
			String key = htmlLayoutContext.getPageHintManager().getHintMapKey(tableId);
			keys.add(key);
		}
		return keys;
	}

	static class InstanceIDComparator {

		static boolean isNextWith(IContent content1, boolean isContent1ChildrenRemoved, IContent content2) {
			if (content1 == null || content2 == null || content1 == content2) {
				return false;
			}
			InstanceID id1 = content1.getInstanceID();
			InstanceID id2 = content2.getInstanceID();
			// only foreign content has a null InstanceID.
			if (id1 == null || id2 == null) {
				return false;
			}

			// Case 1: content2 is the first child.
			if (id2.getUniqueID() == 0) {
				IContent parent2 = (IContent) content2.getParent();
				if (parent2 instanceof IListBandContent || parent2 instanceof ITableBandContent) {
					InstanceID pid2 = parent2.getInstanceID();
					if (pid2 == null) {
						return false;
					} else {
						// the parent2 is the first child.
						if (pid2.getUniqueID() == 0) {
							return isNextWith(content1, isContent1ChildrenRemoved, parent2);
						} else {
							// content1 must be the last child.
							if (!content1.isLastChild()) {
								return false;
							}
							// if content1's children are removed, the page hint should break here.
							else if (isContent1ChildrenRemoved) {
								return false;
							}

							IContent parent1 = (IContent) content1.getParent();
							while (parent1.isLastChild()) {
								parent1 = (IContent) parent1.getParent();
							}
							if (parent1 instanceof IListBandContent || parent1 instanceof ITableBandContent) {
								return isSibling(parent1, parent2);
							} else {
								return false;
							}
						}
					}
				} else {
					return equals(content1, parent2);
				}
			} else
			// Case 2: content2 is NOT the first child.
			{
				// content1 is a container content
				if (content1.hasChildren()) {
					return false;
				}
				// the content1 is a leaf content or a container content without
				// any children.
				if (content1.isLastChild()) {
					IContent parent1 = (IContent) content1.getParent();
					while (parent1.isLastChild()) {
						parent1 = (IContent) parent1.getParent();
					}
					if (parent1 instanceof IListBandContent || parent1 instanceof ITableBandContent) {
						// the parent of content1 is a bandContent, and it
						// is not the last child.
						// so, the parent of content2 should also be a band
						// Content, and content2 should be the first child
						// of its parent.
						// Since content2 is the first child, it should have
						// been handled in case 1,
						// it should never reach here.
						return false;
					} else {
						return isSibling(parent1, content2);
					}
				} else {
					return isSibling(content1, content2);
				}

			}
		}

		static boolean isSibling(IContent content1, IContent content2) {
			if (content1 == null || content2 == null || content1 == content2) {
				return false;
			}
			InstanceID id1 = content1.getInstanceID();
			InstanceID id2 = content2.getInstanceID();
			if (id1 == null || id2 == null) {
				return false;
			}

			if (id1.getUniqueID() + 1 == id2.getUniqueID()) {
				// the siblings
				IContent parent1 = (IContent) content1.getParent();
				IContent parent2 = (IContent) content2.getParent();
				return equals(parent1, parent2);
			}
			return false;
		}

//		static boolean isNextWith( IContent content1, IContent content2 )
//		{
//			if ( content1 == null || content2 == null || content1 == content2 )
//			{
//				return false;
//			}
//			InstanceID id1 = content1.getInstanceID( );
//			InstanceID id2 = content2.getInstanceID( );
//			if ( id1 == null || id2 == null )
//			{
//				return false;
//			}
//			
//			// 1. content2 is the first child of content1
//			if ( id2.getUniqueID( ) == 0 )
//			{
//				return equals( content1, (IContent) content2.getParent( ) );
//			}
//			// 2. content1 is the last child of its parent p, and content2 is the sibling of p. 
//			else if ( ( content1 != null ) && content1.isLastChild( ) )
//			{
//				// cross level
//				content1 = (IContent) content1.getParent( );
//				return isNextWith( content1, content2 );
//			}
//			else if ( id1.getUniqueID( ) + 1 == id2.getUniqueID( ) )
//			{
//				// the siblings
//				IContent parent1 = (IContent) content1.getParent( );
//				IContent parent2 = (IContent) content2.getParent( );
//				return equals( parent1, parent2 );
//			}
//			return false;
//		}

		static boolean equals(IContent content1, IContent content2) {
			if (content1 == content2)
				return true;
			if (content1 == null) {
				return false;
			} else {
				if (content2 == null)
					return false;
			}
			InstanceID id1 = content1.getInstanceID();
			InstanceID id2 = content2.getInstanceID();
			if (id1 == null || id2 == null)
				return false;
			if (id1.getUniqueID() == id2.getUniqueID()) {
				IContent parent1 = (IContent) content1.getParent();
				IContent parent2 = (IContent) content2.getParent();
				return equals(parent1, parent2);
			} else {
				return false;
			}
		}
	}
}