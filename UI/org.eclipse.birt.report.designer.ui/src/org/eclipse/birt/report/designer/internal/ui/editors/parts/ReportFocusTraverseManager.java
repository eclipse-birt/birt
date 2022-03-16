/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;

/**
 * add comment here
 *
 */
public class ReportFocusTraverseManager {

	/**
	 * Default constructor.
	 */
	public ReportFocusTraverseManager() {
	}

	private IFigure findDeepestRightmostChildOf(IFigure fig) {
		while (fig.getChildren().size() != 0) {
			fig = (IFigure) fig.getChildren().get(fig.getChildren().size() - 1);
		}
		return fig;
	}

	/**
	 * Returns the IFigure that will receive focus upon a 'tab' traverse event.
	 *
	 * @param root       the {@link LightweightSystem LightweightSystem's} root
	 *                   figure
	 * @param focusOwner the IFigure who currently owns focus
	 * @return the next focusable figure
	 */
	public IFigure getNextFocusableFigure(IFigure root, IFigure focusOwner) {
		boolean found = false;
		IFigure nextFocusOwner = focusOwner;

		/*
		 * If no Figure currently has focus, apply focus to root figure's first
		 * focusable child
		 */
		if (focusOwner == null) {
			if (root.getChildren().size() != 0) {
				nextFocusOwner = ((IFigure) root.getChildren().get(0));
				if (isFocusEligible(nextFocusOwner)) {
					return nextFocusOwner;
				}
			} else {
				return null;
			}
		}
		while (!found) {
			IFigure parent = nextFocusOwner.getParent();

			/*
			 * Figure traversal is implemented using the pre-order left to right tree
			 * traversal algorithm.
			 *
			 * If the focused sibling has children, traverse to its leftmost child. If the
			 * focused sibling has no children, traverse to the sibling to its right. If
			 * there is no sibling to the right, go up the tree until a node with
			 * untraversed siblings is found.
			 */
			List siblings = parent.getChildren();
			int siblingPos = siblings.indexOf(nextFocusOwner);

			if (nextFocusOwner.getChildren().size() != 0) {
				nextFocusOwner = ((IFigure) (nextFocusOwner.getChildren().get(0)));
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				}
			} else if (siblingPos < siblings.size() - 1) {
				nextFocusOwner = ((IFigure) (siblings.get(siblingPos + 1)));
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				}
			} else {
				boolean untraversedSiblingFound = false;
				while (!untraversedSiblingFound) {
					IFigure p = nextFocusOwner.getParent();
					IFigure gp = p.getParent();

					if (gp != null) {
						int parentSiblingCount = gp.getChildren().size();
						int parentIndex = gp.getChildren().indexOf(p);
						if (parentIndex < parentSiblingCount - 1) {
							nextFocusOwner = ((IFigure) p.getParent().getChildren().get(parentIndex + 1));
							untraversedSiblingFound = true;
							if (isFocusEligible(nextFocusOwner)) {
								found = true;
							}
						} else {
							nextFocusOwner = p;
						}
					} else {
						nextFocusOwner = null;
						untraversedSiblingFound = true;
						found = true;
					}
				}
			}
		}
		return nextFocusOwner;
	}

	/**
	 * Returns the IFigure that will receive focus upon a 'tab' traverse event.
	 *
	 * @param root       the {@link LightweightSystem LightweightSystem's} root
	 *                   figure
	 * @param focusOwner the IFigure who currently owns focus
	 * @return the next focusable figure
	 */
	public IFigure getNextFocusableFigureInSameOrder(IFigure root, IFigure focusOwner) {
		boolean found = false;
		IFigure nextFocusOwner = focusOwner;

		/*
		 * If no Figure currently has focus, apply focus to root figure's first
		 * focusable child
		 */
		if (focusOwner == null) {
			if (root.getChildren().size() != 0) {
				nextFocusOwner = ((IFigure) root.getChildren().get(0));
				if (isFocusEligible(nextFocusOwner)) {
					return nextFocusOwner;
				}
			} else {
				return null;
			}
		}
		ArrayList list = new ArrayList();
		while (!found) {
			IFigure parent = nextFocusOwner.getParent();

			/*
			 * Figure traversal is implemented using the pre-order left to right tree
			 * traversal algorithm.
			 *
			 * If the focused sibling has children, traverse to its leftmost child. If the
			 * focused sibling has no children, traverse to the sibling to its right. If
			 * there is no sibling to the right, go up the tree until a node with
			 * untraversed siblings is found.
			 */
			List siblings = parent.getChildren();
			int siblingPos = siblings.indexOf(nextFocusOwner);
			if (siblingPos < siblings.size() - 1) {
				nextFocusOwner = ((IFigure) (siblings.get(siblingPos + 1)));
				list.add(nextFocusOwner);
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				}
			} else if (siblings.size() == 1) {
				nextFocusOwner = ((IFigure) (siblings.get(0)));

				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				} else {
					return null;
				}
			} else {
				nextFocusOwner = ((IFigure) (siblings.get(0)));

				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				} else if (list.contains(nextFocusOwner)) {
					return null;
				} else {
					list.add(nextFocusOwner);
				}

			}
		}
		return nextFocusOwner;
	}

	/**
	 * Returns the IFigure that will receive focus upon a 'shift-tab' traverse
	 * event.
	 *
	 * @param root       The {@link LightweightSystem LightweightSystem's} root
	 *                   figure
	 * @param focusOwner The IFigure who currently owns focus
	 * @return the previous focusable figure
	 */
	public IFigure getPreviousFocusableFigureInSameOrder(IFigure root, IFigure focusOwner) {

		IFigure nextFocusOwner = focusOwner;
		if (focusOwner == null) {
			if (root.getChildren().size() != 0) {
				nextFocusOwner = findDeepestRightmostChildOf(root);
				if (isFocusEligible(nextFocusOwner)) {
					return nextFocusOwner;
				}
			} else {
				return null;
			}
		}

		boolean found = false;
		ArrayList list = new ArrayList();
		while (!found) {
			IFigure parent = nextFocusOwner.getParent();

			/*
			 * At root, return null to indicate traversal is complete.
			 */
			if (parent == null) {
				return null;
			}

			List siblings = parent.getChildren();
			int siblingPos = siblings.indexOf(nextFocusOwner);

			/*
			 * Figure traversal is implemented using the post-order right to left tree
			 * traversal algorithm.
			 *
			 * Find the rightmost child. If this child is focusable, return it If not
			 * focusable, traverse to its sibling and repeat. If there is no sibling,
			 * traverse its parent.
			 */
			if (siblingPos != 0) {
				nextFocusOwner = ((IFigure) (siblings.get(siblingPos - 1)));
				list.add(nextFocusOwner);
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				}

			} else if (siblings.size() == 1) {
				nextFocusOwner = ((IFigure) (siblings.get(0)));
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				} else {
					return null;
				}
			} else {

				nextFocusOwner = ((IFigure) (siblings.get(siblings.size() - 1)));
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				} else if (list.contains(nextFocusOwner)) {
					return null;
				} else {
					list.add(nextFocusOwner);
				}

			}
		}
		return nextFocusOwner;
	}

	/**
	 * Returns the IFigure that will receive focus upon a 'shift-tab' traverse
	 * event.
	 *
	 * @param root       The {@link LightweightSystem LightweightSystem's} root
	 *                   figure
	 * @param focusOwner The IFigure who currently owns focus
	 * @return the previous focusable figure
	 */
	public IFigure getPreviousFocusableFigure(IFigure root, IFigure focusOwner) {

		IFigure nextFocusOwner = focusOwner;
		if (focusOwner == null) {
			if (root.getChildren().size() != 0) {
				nextFocusOwner = findDeepestRightmostChildOf(root);
				if (isFocusEligible(nextFocusOwner)) {
					return nextFocusOwner;
				}
			} else {
				return null;
			}
		}

		boolean found = false;

		while (!found) {
			IFigure parent = nextFocusOwner.getParent();

			/*
			 * At root, return null to indicate traversal is complete.
			 */
			if (parent == null) {
				return null;
			}

			List siblings = parent.getChildren();
			int siblingPos = siblings.indexOf(nextFocusOwner);

			/*
			 * Figure traversal is implemented using the post-order right to left tree
			 * traversal algorithm.
			 *
			 * Find the rightmost child. If this child is focusable, return it If not
			 * focusable, traverse to its sibling and repeat. If there is no sibling,
			 * traverse its parent.
			 */
			if (siblingPos != 0) {
				IFigure child = findDeepestRightmostChildOf((IFigure) siblings.get(siblingPos - 1));
				if (isFocusEligible(child)) {
					found = true;
					nextFocusOwner = child;
				} else if (child.equals(nextFocusOwner)) {
					if (isFocusEligible(nextFocusOwner)) {
						found = true;
					}
				} else {
					nextFocusOwner = child;
				}
			} else {
				nextFocusOwner = parent;
				if (isFocusEligible(nextFocusOwner)) {
					found = true;
				}
			}
		}
		return nextFocusOwner;
	}

	public boolean isFocusEligible(IFigure fig) {
		if (fig == null || !fig.isFocusTraversable() || !fig.isShowing()) {
			return false;
		}
		return true;
	}

}
