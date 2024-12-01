/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

/**
 * Implementation of block container area
 *
 * @since 3.3
 *
 */
public class BlockContainerArea extends ContainerArea implements IContainerArea {

	/**
	 * Constructor
	 *
	 * @param parent
	 * @param context
	 * @param content
	 */
	public BlockContainerArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
		if (parent == null) {
			isInInlineStacking = false;
		} else {
			isInInlineStacking = parent.isInlineStacking || parent.isInInlineStacking;
		}
	}

	/**
	 * Constructor
	 */
	public BlockContainerArea() {
		super();
	}

	BlockContainerArea(BlockContainerArea area) {
		super(area);
	}

	@Override
	public void add(AbstractArea area) {
		children.add(area);
		area.setAllocatedPosition(currentIP + getOffsetX(), currentBP + getOffsetY());

	}

	@Override
	public void update(AbstractArea area) throws BirtException {
		int aHeight = area.getAllocatedHeight();
		currentBP += aHeight;
		height += aHeight;
		if (currentIP + area.getAllocatedWidth() > maxAvaWidth) {
			setNeedClip(true);
		}
	}

	@Override
	public void close() throws BirtException {
		if (hasStyle) {
			int height = currentBP + localProperties.getPaddingTop() + boxStyle.getTopBorderWidth()
					+ localProperties.getPaddingBottom() + boxStyle.getBottomBorderWidth();

			if (specifiedHeight > height) {

				if (CSSValueConstants.BOTTOM_VALUE.equals(vAlign)) {
					int offset = specifiedHeight - height;
					Iterator<IArea> iter = getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setY(offset + child.getY());
					}
				} else if (CSSValueConstants.MIDDLE_VALUE.equals(vAlign)) {
					int offset = (specifiedHeight - height) / 2;
					Iterator<IArea> iter = getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setY(child.getY() + offset);
					}
				}

				height = specifiedHeight;
			}
			this.height = height;
			updateBackgroundImage();
		} else if (specifiedHeight > currentBP) {
			height = specifiedHeight;
		} else {
			height = currentBP;
		}
		update();
		finished = true;
		checkDisplayNone();
	}

	protected void update() throws BirtException {
		if (context.isFixedLayout() && getContentHeight() > specifiedHeight && specifiedHeight > 0) {
			setContentHeight(specifiedHeight);
			setNeedClip(true);
		}
		if (parent != null) {
			checkPageBreak();
			parent.update(this);
		}
	}

	@Override
	public void initialize() throws BirtException {
		if (content == null) {
			this.maxAvaWidth = width;
			return;
		}
		IStyle style = content.getStyle();
		calculateSpecifiedWidth(content);
		calculateSpecifiedHeight(content);

		if (style == null || style.isEmpty()) {
			hasStyle = false;
			boxStyle = BoxStyle.DEFAULT;
			localProperties = LocalProperties.DEFAULT;
			if (specifiedWidth > 0) {
				setContentWidth(specifiedWidth);
			} else if (parent != null) {
				this.width = parent.getMaxAvaWidth();
			}
			this.maxAvaWidth = width;
		} else {
			validateBoxProperty(content.getComputedStyle(),
					parent == null ? context.getMaxWidth() : parent.getMaxAvaWidth(), context.getMaxHeight());
			buildProperties(content, context);
			if (specifiedHeight > 0) {
				setContentHeight(specifiedHeight);
			}

			if (specifiedWidth > 0) {
				setContentWidth(specifiedWidth);
			} else if (parent != null) {
				setAllocatedWidth(parent.getMaxAvaWidth());
			}
			maxAvaWidth = getContentWidth();
		}
		textAlign = content.getComputedStyle().getProperty(StyleConstants.STYLE_TEXT_ALIGN);
		this.bookmark = content.getBookmark();
		this.action = content.getHyperlinkAction();
		parent.add(this);
	}

	@Override
	public BlockContainerArea cloneArea() {
		return new BlockContainerArea(this);
	}

	@Override
	public SplitResult splitLines(int lineCount) throws BirtException {
		if (isPageBreakInsideAvoid()) {
			if (isPageBreakBeforeAvoid()) {
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			return SplitResult.SUCCEED_WITH_NULL;
		}
		int contentHeight = getContentHeight();
		LinkedList<ContainerArea> result = new LinkedList<ContainerArea>();
		int size = children.size();
		SplitResult childSplit = null;
		for (int i = size - 1; i >= 0; i--) {
			ContainerArea child = (ContainerArea) children.get(i);
			int ah = child.getAllocatedHeight();
			childSplit = child.splitLines(lineCount);
			if (childSplit.status == SplitResult.SPLIT_BEFORE_AVOID_WITH_NULL) {
				result.addFirst(child);
				contentHeight -= ah;
			} else if (childSplit.status == SplitResult.SPLIT_SUCCEED_WITH_NULL) {
				result.addFirst(child);
				contentHeight -= ah;
				if (i > 0) {
					ContainerArea preChild = (ContainerArea) children.get(i - 1);
					if (preChild.isPageBreakAfterAvoid()) {
						continue;
					}
					contentHeight = contentHeight - ah + child.getAllocatedHeight();
					BlockContainerArea newContainer = cloneArea();
					newContainer.updateContentHeight(contentHeight);
					Iterator<IArea> iter = children.iterator();
					while (iter.hasNext()) {
						ContainerArea childArea = (ContainerArea) iter.next();
						if (!result.contains(childArea)) {
							iter.remove();
							newContainer.addChild(childArea);
							newContainer.setParent(newContainer);
						}
					}
					updateChildrenPosition();
					return new SplitResult(newContainer, SplitResult.SPLIT_SUCCEED_WITH_PART);

				} else if (isPageBreakBeforeAvoid()) {
					return SplitResult.BEFORE_AVOID_WITH_NULL;
				} else {
					return SplitResult.SUCCEED_WITH_NULL;
				}
			} else if (childSplit.status == SplitResult.SPLIT_SUCCEED_WITH_PART) {
				result.addFirst(child);
				ContainerArea splitChildArea = childSplit.getResult();
				contentHeight = contentHeight - ah + splitChildArea.getAllocatedHeight();
				BlockContainerArea newContainer = cloneArea();
				newContainer.updateContentHeight(contentHeight);
				Iterator<IArea> iter = children.iterator();
				while (iter.hasNext()) {
					ContainerArea childArea = (ContainerArea) iter.next();
					if (!result.contains(childArea)) {
						iter.remove();
						newContainer.addChild(childArea);
						newContainer.setParent(newContainer);
					}
				}
				newContainer.addChild(splitChildArea);
				addRepeatedItem();
				updateChildrenPosition();
				return new SplitResult(newContainer, SplitResult.SPLIT_SUCCEED_WITH_PART);
			}
		}
		return SplitResult.BEFORE_AVOID_WITH_NULL;
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		final SplitResult ret;
		if (force) {
			ret = _split(height, true);
		} else if (isPageBreakInsideAvoid()) {
			if (isPageBreakBeforeAvoid()) {
				ret = SplitResult.BEFORE_AVOID_WITH_NULL;
			} else {
				ret = SplitResult.SUCCEED_WITH_NULL;
			}
		} else {
			ret = _split(height, false);
		}
		if (ret.getResult() != null) {
			setPreviousPart(ret.getResult());
		}
		return ret;
	}

	protected SplitResult _split(int height, boolean force) throws BirtException {
		if (children.size() == 0) {
			if (isPageBreakBeforeAvoid() && !force) {
				updateChildrenPosition();
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			updateChildrenPosition();
			return SplitResult.SUCCEED_WITH_NULL;
		}
		BlockContainerArea newContainer = null;
		int status = SplitResult.SPLIT_BEFORE_AVOID_WITH_NULL;
		int cheight = getContentHeight(height);
		ListIterator<IArea> iter = children.listIterator();
		int contentHeight = 0;
		ArrayList<ContainerArea> result = new ArrayList<ContainerArea>();
		ContainerArea current = null;
		ContainerArea previous = null;
		while (iter.hasNext()) {
			previous = current;
			current = (ContainerArea) iter.next();
			int ah = current.getAllocatedHeight();
			contentHeight += ah;
			if (contentHeight <= cheight && current.finished) {
				result.add(current);
				continue;
			}
			contentHeight -= ah;
			int childSplitHeight = cheight - contentHeight;
			SplitResult splitResult = current.split(childSplitHeight, force && !isValidResult(result));
			if (splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_PART) {
				ContainerArea splitChildArea = splitResult.getResult();
				result.add(splitChildArea);
				status = SplitResult.SPLIT_SUCCEED_WITH_PART;
				contentHeight += splitChildArea.getAllocatedHeight();
				break;
			} else if (splitResult.status == SplitResult.SPLIT_BEFORE_AVOID_WITH_NULL) {
				if (force) {
					if (result.size() > 0) {
						status = SplitResult.SPLIT_SUCCEED_WITH_PART;
					}
				}
				break;
			} else if (splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_NULL) {
				if (isValidResult(result)) {
					if (force) {
						status = SplitResult.SPLIT_SUCCEED_WITH_PART;
						break;
					}
					if (previous.isPageBreakAfterAvoid()) {
						status = SplitResult.SPLIT_BEFORE_AVOID_WITH_NULL;
						break;
					}
					status = SplitResult.SPLIT_SUCCEED_WITH_PART;
					break;
				} else if (force) {
					// error status
					status = SplitResult.SPLIT_SUCCEED_WITH_PART;
					break;
				} else {
					if (isPageBreakBeforeAvoid()) {
						return SplitResult.BEFORE_AVOID_WITH_NULL;
					}
					return SplitResult.SUCCEED_WITH_NULL;
				}
			}
		}
		// split height is larger than content height.(cell)
		if (result.size() == children.size()) {
			status = SplitResult.SPLIT_SUCCEED_WITH_PART;
		}

		if (!force && status == SplitResult.SPLIT_BEFORE_AVOID_WITH_NULL) {
			if (result.size() == 0) {
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}
			// locate current child
			iter.previous();
			while (iter.hasPrevious()) {
				current = (ContainerArea) iter.previous();
				int ah = current.getAllocatedHeight();
				SplitResult splitResult = current.splitLines(1);
				if (splitResult.status == SplitResult.SPLIT_BEFORE_AVOID_WITH_NULL) {
					result.remove(current);
					contentHeight -= ah;
					continue;
				} else if (splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_PART) {
					result.remove(current);
					ContainerArea splitChildArea = splitResult.getResult();
					contentHeight = contentHeight - ah + splitChildArea.getAllocatedHeight();
					result.add(splitChildArea);
					status = SplitResult.SPLIT_SUCCEED_WITH_PART;
					break;
				} else if (splitResult.status == SplitResult.SPLIT_SUCCEED_WITH_NULL) {
					result.remove(current);
					contentHeight -= ah;
					int preIndex = iter.previousIndex();
					if (preIndex >= 0) {
						ContainerArea prev = (ContainerArea) children.get(preIndex);
						if (prev.isPageBreakAfterAvoid()) {
							continue;
						}
						status = SplitResult.SPLIT_SUCCEED_WITH_PART;
						break;
					} else if (isPageBreakBeforeAvoid()) {
						return SplitResult.BEFORE_AVOID_WITH_NULL;
					} else {
						return SplitResult.SUCCEED_WITH_NULL;
					}
				}
			}
			if (result.size() == 0) {
				return SplitResult.BEFORE_AVOID_WITH_NULL;
			}

		}

		if (status == SplitResult.SPLIT_SUCCEED_WITH_PART) {
			if (isValidResult(result) || force) {
				newContainer = getSplitArea(result, contentHeight);
			} else {
				return SplitResult.SUCCEED_WITH_NULL;
			}
		}
		if (newContainer != null) {
			addRepeatedItem();
			updateChildrenPosition();
		}
		return new SplitResult(newContainer, status);
	}

	/**
	 * Gets the area which is split from the original area.
	 *
	 * @param ablatedChildren the children which is split off the original area.
	 * @param newHeight       the new content height
	 * @return Return the block container area
	 */
	protected BlockContainerArea getSplitArea(ArrayList<ContainerArea> ablatedChildren, int newHeight) {
		BlockContainerArea newContainer = cloneArea();
		for (int i = 0; i < ablatedChildren.size(); i++) {
			ContainerArea child = ablatedChildren.get(i);
			child.setParent(newContainer);
			newContainer.addChild(child);
			children.remove(child);
			if (children.isEmpty()) {
				isChildrenRemoved = true;
			}
		}

		newContainer.updateContentHeight(newHeight);
		applyAlignment(newContainer);
		return newContainer;
	}

	private void applyAlignment(BlockContainerArea area) {
		IContent content = area.getContent();
		if (content == null) {
			return;
		}
		CSSValue align = content.getComputedStyle().getProperty(StyleConstants.STYLE_TEXT_ALIGN);

		// bidi_hcg: handle empty or justify align in RTL direction as right
		// alignment
		boolean isRightAligned = BidiAlignmentResolver.isRightAligned(content, align, false);

		// single line
		if (isRightAligned || CSSValueConstants.CENTER_VALUE.equals(align)) {
			Iterator<IArea> iter = area.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				int spacing = area.getContentWidth() - child.getAllocatedWidth();
				if (spacing > 0) {
					if (isRightAligned) {
						child.setAllocatedX(spacing + area.getOffsetX());
					} else if (CSSValueConstants.CENTER_VALUE.equals(align)) {
						child.setAllocatedX(spacing / 2 + area.getOffsetX());
					}
				}
			}
		}
	}

	protected void addRepeatedItem() throws BirtException {

	}

	protected boolean isValidResult(List<ContainerArea> result) {
		return result.size() > 0;
	}

	@Override
	public int getBaseLine() {
		if (baseLine == 0) {
			// use the first child baseline.
			if (children.size() > 0) {
				AbstractArea child = (AbstractArea) children.get(children.size() - 1);
				baseLine = child.getY() + child.getBaseLine();
			} else {
				baseLine = height;
			}
		}
		return baseLine;
	}

	@Override
	public boolean isPageBreakInsideAvoid() {
		if (context.isFixedLayout() && specifiedHeight > 0) {
			return true;
		}
		return super.isPageBreakInsideAvoid();
	}

	@Override
	public void updateChildrenPosition() {
		first = false;
		currentBP = 0;
		if (children.size() > 0) {
			Iterator<IArea> iter = children.iterator();
			int y = getOffsetY();
			int h = 0;
			while (iter.hasNext()) {
				ContainerArea area = (ContainerArea) iter.next();
				// if(iter.hasNext( ))
				{
					area.setAllocatedPosition(getOffsetX(), y);
					int ah = area.getAllocatedHeight();
					y += ah;
					h += ah;
					if (area.finished) {
						currentBP += ah;
					}
				}
			}
			setContentHeight(h);
		} else {
			setContentHeight(0);
		}
	}

}
