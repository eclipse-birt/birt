/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.Arrays;
import java.util.Map;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class TabbedPropertyList extends Canvas implements IPropertyList {

	private static final ListElement[] ELEMENTS_EMPTY = {};

	protected static final int NONE = -1;

	protected static final int INDENT = 7;

	private boolean focus = false;

	private ListElement[] elements;

	private int selectedElementIndex = NONE;

	private int topVisibleIndex = NONE;

	private int bottomVisibleIndex = NONE;

	private TopNavigationElement topNavigationElement;

	private BottomNavigationElement bottomNavigationElement;

	private int widestLabelIndex = NONE;

	private int tabsThatFitInComposite = NONE;

	private Color widgetForeground;

	private Color widgetBackground;

	private Color widgetNormalShadow;

	private Color widgetDarkShadow;

	private Color listBackground;

	private Color hoverGradientStart;

	private Color hoverGradientEnd;

	private Color defaultGradientStart;

	private Color defaultGradientEnd;

	private Color indentedDefaultBackground;

	private Color indentedHoverBackground;

	private Color navigationElementShadowStroke;

	private Color bottomNavigationElementShadowStroke1;

	private Color bottomNavigationElementShadowStroke2;

	private GC textGc;

	private FormToolkit factory;

	public class ListElement extends Canvas {

		private Tab tab;

		private int index;

		private boolean selected;

		private boolean hover;

		public ListElement(Composite parent, final Tab tab, int index) {
			super(parent, SWT.NO_FOCUS);
			this.tab = tab;
			hover = false;
			selected = false;
			this.index = index;

			addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					paint(e);
				}
			});
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDown(MouseEvent e) {
					if (!selected) {
						select(getIndex(ListElement.this), true);
					}
					Composite tabbedPropertyComposite = getParent();
					Control[] children = tabbedPropertyComposite.getParent().getTabList();
					if (children != null && children.length > 0) {
						for (int i = 0; i < children.length; i++) {
							if (children[i] == TabbedPropertyList.this) {
							} else if (children[i].setFocus()) {
								focus = false;
								return;
							}
						}
					}
				}
			});
			addMouseMoveListener(new MouseMoveListener() {

				@Override
				public void mouseMove(MouseEvent e) {
					if (!hover) {
						hover = true;
						redraw();
					}
				}
			});
			addMouseTrackListener(new MouseTrackAdapter() {

				@Override
				public void mouseExit(MouseEvent e) {
					hover = false;
					redraw();
				}
			});
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
			redraw();
		}

		/**
		 * Draws elements and collects element areas.
		 */
		private void paint(PaintEvent e) {
			/*
			 * draw the top two lines of the tab, same for selected, hover and default
			 */
			Rectangle bounds = getBounds();
			e.gc.setForeground(widgetNormalShadow);
			e.gc.drawLine(0, 0, bounds.width - 1, 0);
			e.gc.setForeground(listBackground);
			e.gc.drawLine(0, 1, bounds.width - 1, 1);

			/* draw the fill in the tab */
			if (selected) {
				e.gc.setBackground(listBackground);
				e.gc.fillRectangle(0, 2, bounds.width, bounds.height - 1);
			} else if (hover && tab.isIndented()) {
				e.gc.setBackground(indentedHoverBackground);
				e.gc.fillRectangle(0, 2, bounds.width - 1, bounds.height - 1);
			} else if (hover) {
				e.gc.setForeground(hoverGradientStart);
				e.gc.setBackground(hoverGradientEnd);
				e.gc.fillGradientRectangle(0, 2, bounds.width - 1, bounds.height - 1, true);
			} else if (tab.isIndented()) {
				e.gc.setBackground(indentedDefaultBackground);
				e.gc.fillRectangle(0, 2, bounds.width - 1, bounds.height - 1);
			} else {
				e.gc.setForeground(defaultGradientStart);
				e.gc.setBackground(defaultGradientEnd);
				e.gc.fillGradientRectangle(0, 2, bounds.width - 1, bounds.height - 1, true);
			}

			if (!selected) {
				e.gc.setForeground(widgetNormalShadow);
				e.gc.drawLine(bounds.width - 1, 1, bounds.width - 1, bounds.height + 1);
			}

			int textIndent = INDENT;
			FontMetrics fm = e.gc.getFontMetrics();
			int height = fm.getHeight();
			int textMiddle = (bounds.height - height) / 2;

			if (selected && tab.getImage() != null && !tab.getImage().isDisposed()) {
				/* draw the icon for the selected tab */
				if (tab.isIndented()) {
					textIndent = textIndent + INDENT;
				} else {
					textIndent = textIndent - 3;
				}
				e.gc.drawImage(tab.getImage(), textIndent, textMiddle - 1);
				textIndent = textIndent + 16 + 5;
			} else if (tab.isIndented()) {
				textIndent = textIndent + INDENT;
			}

			/* draw the text */
			e.gc.setForeground(widgetForeground);
			if (selected) {
				/* selected tab is bold font */
				e.gc.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
			}
			e.gc.drawText(tab.getText(), textIndent, textMiddle, true);

			if (((TabbedPropertyList) getParent()).focus && selected && focus) {
				/* draw a line if the tab has focus */
				Point point = e.gc.textExtent(tab.getText());
				e.gc.drawLine(textIndent, bounds.height - 4, textIndent + point.x, bounds.height - 4);
			}

			/* draw the bottom line on the tab for selected and default */
			if (!hover) {
				e.gc.setForeground(listBackground);
				e.gc.drawLine(0, bounds.height - 1, bounds.width - 2, bounds.height - 1);
			}

		}

		public String getText() {
			return tab.getText();
		}

		@Override
		public String toString() {
			return tab.getText();
		}
	}

	public class TopNavigationElement extends Canvas {

		/**
		 * @param parent
		 */
		public TopNavigationElement(Composite parent) {
			super(parent, SWT.NO_FOCUS);
			addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					paint(e);
				}
			});
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseUp(MouseEvent e) {
					if (isUpScrollRequired()) {
						bottomVisibleIndex--;
						if (topVisibleIndex != 0) {
							topVisibleIndex--;
						}
						layoutTabs();
						topNavigationElement.redraw();
						bottomNavigationElement.redraw();
					}
				}
			});
		}

		/**
		 * @param e
		 */
		private void paint(PaintEvent e) {
			e.gc.setBackground(widgetBackground);
			e.gc.setForeground(widgetForeground);
			Rectangle bounds = getBounds();

			if (elements.length != 0) {
				e.gc.fillRectangle(0, 0, bounds.width, bounds.height);
				e.gc.setForeground(widgetNormalShadow);
				e.gc.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
			} else {
				e.gc.setBackground(listBackground);
				e.gc.fillRectangle(0, 0, bounds.width, bounds.height);
				int textIndent = INDENT;
				FontMetrics fm = e.gc.getFontMetrics();
				int height = fm.getHeight();
				int textMiddle = (bounds.height - height) / 2;
				e.gc.setForeground(widgetForeground);
				String properties_not_available = Messages.getString("TabbedPropertyList.properties.not.available"); //$NON-NLS-1$
				e.gc.drawText(properties_not_available, textIndent, textMiddle);
			}

			if (isUpScrollRequired()) {
				e.gc.setForeground(widgetDarkShadow);
				int middle = bounds.width / 2;
				e.gc.drawLine(middle + 1, 3, middle + 5, 7);
				e.gc.drawLine(middle, 3, middle - 4, 7);
				e.gc.drawLine(middle - 3, 7, middle + 4, 7);

				e.gc.setForeground(listBackground);
				e.gc.drawLine(middle, 4, middle + 1, 4);
				e.gc.drawLine(middle - 1, 5, middle + 2, 5);
				e.gc.drawLine(middle - 2, 6, middle + 3, 6);

				e.gc.setForeground(widgetNormalShadow);
				e.gc.drawLine(0, 0, bounds.width - 2, 0);
				e.gc.setForeground(navigationElementShadowStroke);
				e.gc.drawLine(0, 1, bounds.width - 2, 1);
				e.gc.drawLine(0, bounds.height - 1, bounds.width - 2, bounds.height - 1);
			}

		}
	}

	public class BottomNavigationElement extends Canvas {

		/**
		 * @param parent
		 */
		public BottomNavigationElement(Composite parent) {
			super(parent, SWT.NO_FOCUS);
			addPaintListener(new PaintListener() {

				@Override
				public void paintControl(PaintEvent e) {
					paint(e);
				}
			});
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseUp(MouseEvent e) {
					if (isDownScrollRequired()) {
						topVisibleIndex++;
						if (bottomVisibleIndex != elements.length - 1) {
							bottomVisibleIndex++;
						}
						layoutTabs();
						topNavigationElement.redraw();
						bottomNavigationElement.redraw();
					}
				}
			});
		}

		/**
		 * @param e
		 */
		private void paint(PaintEvent e) {
			e.gc.setBackground(widgetBackground);
			e.gc.setForeground(widgetForeground);
			Rectangle bounds = getBounds();

			if (elements.length != 0) {
				e.gc.fillRectangle(0, 0, bounds.width, bounds.height);
				e.gc.setForeground(widgetNormalShadow);
				e.gc.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
				e.gc.drawLine(0, 0, bounds.width - 1, 0);

				e.gc.setForeground(bottomNavigationElementShadowStroke1);
				e.gc.drawLine(0, 1, bounds.width - 2, 1);
				e.gc.setForeground(bottomNavigationElementShadowStroke2);
				e.gc.drawLine(0, 2, bounds.width - 2, 2);
			} else {
				e.gc.setBackground(listBackground);
				e.gc.fillRectangle(0, 0, bounds.width, bounds.height);
			}

			if (isDownScrollRequired()) {
				e.gc.setForeground(widgetDarkShadow);
				int middle = bounds.width / 2;
				int bottom = bounds.height - 3;
				e.gc.drawLine(middle + 1, bottom, middle + 5, bottom - 4);
				e.gc.drawLine(middle, bottom, middle - 4, bottom - 4);
				e.gc.drawLine(middle - 3, bottom - 4, middle + 4, bottom - 4);

				e.gc.setForeground(listBackground);
				e.gc.drawLine(middle, bottom - 1, middle + 1, bottom - 1);
				e.gc.drawLine(middle - 1, bottom - 2, middle + 2, bottom - 2);
				e.gc.drawLine(middle - 2, bottom - 3, middle + 3, bottom - 3);

				e.gc.setForeground(widgetNormalShadow);
				e.gc.drawLine(0, bottom - 7, bounds.width - 2, bottom - 7);
				e.gc.setForeground(navigationElementShadowStroke);
				e.gc.drawLine(0, bottom + 2, bounds.width - 2, bottom + 2);
				e.gc.drawLine(0, bottom - 6, bounds.width - 2, bottom - 6);
			}
		}
	}

	public TabbedPropertyList(Composite parent) {
		super(parent, SWT.NONE);
		factory = FormWidgetFactory.getInstance();
		removeAll();
		setLayout(new FormLayout());
		initColours();
		initAccessible();
		topNavigationElement = new TopNavigationElement(this);
		bottomNavigationElement = new BottomNavigationElement(this);

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				focus = true;
				int i = getSelectionIndex();
				if (i >= 0) {
					elements[i].redraw();
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				focus = false;
				int i = getSelectionIndex();
				if (i >= 0) {
					elements[i].redraw();
				}
			}
		});

		// do nothing, just for tab traverse.
		this.addKeyListener(new KeyAdapter() {
		});

		this.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				topNavigationElement.redraw();
				bottomNavigationElement.redraw();
				computeTopAndBottomTab();
			}
		});

		this.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ARROW_PREVIOUS || e.detail == SWT.TRAVERSE_ARROW_NEXT) {
					int nMax = elements.length - 1;
					int nCurrent = getSelectionIndex();
					if (e.detail == SWT.TRAVERSE_ARROW_PREVIOUS) {
						nCurrent -= 1;
						nCurrent = Math.max(0, nCurrent);
					} else if (e.detail == SWT.TRAVERSE_ARROW_NEXT) {
						nCurrent += 1;
						nCurrent = Math.min(nCurrent, nMax);
					}
					select(nCurrent, true);
					redraw();
				} else {
					e.doit = true;
				}
			}
		});
	}

	/**
	 * Calculate the number of tabs that will fit in the tab list composite.
	 */
	protected void computeTabsThatFitInComposite() {
		tabsThatFitInComposite = ((getSize().y - 22)) / getTabHeight();
		if (tabsThatFitInComposite <= 0) {
			tabsThatFitInComposite = 1;
		}
	}

	/**
	 * Returns the element with the given index from this list viewer. Returns
	 * <code>null</code> if the index is out of range.
	 *
	 * @param index the zero-based index
	 * @return the element at the given index, or <code>null</code> if the index is
	 *         out of range
	 */
	public Object getElementAt(int index) {
		if (index >= 0 && index < elements.length) {
			return elements[index];
		}
		return null;
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected in
	 * the receiver, or -1 if no item is selected.
	 *
	 * @return the index of the selected item
	 */
	@Override
	public int getSelectionIndex() {
		return selectedElementIndex;
	}

	@Override
	public String getSelectionKey() {
		return elementMap.keySet().toArray()[selectedElementIndex].toString();
	}

	/**
	 * Removes all elements from this list.
	 */
	public void removeAll() {
		if (elements != null) {
			for (int i = 0; i < elements.length; i++) {
				elements[i].dispose();
			}
		}
		elements = ELEMENTS_EMPTY;
		selectedElementIndex = NONE;
		widestLabelIndex = NONE;
		topVisibleIndex = NONE;
		bottomVisibleIndex = NONE;
	}

	/**
	 * Sets the new list elements.
	 */

	private Map elementMap = null;

	@Override
	public void setElements(Map children) {
		elementMap = children;
		if (elements != ELEMENTS_EMPTY) {
			removeAll();
		}
		elements = new ListElement[children.size()];
		if (children.size() == 0) {
			widestLabelIndex = NONE;
		} else {
			widestLabelIndex = 0;
			for (int i = 0; i < children.size(); i++) {
				elements[i] = new ListElement(this, (Tab) children.get(children.keySet().toArray()[i]), i);
				elements[i].setVisible(false);
				elements[i].setLayoutData(null);

				if (i != widestLabelIndex) {
					String label = ((Tab) children.get(children.keySet().toArray()[i])).getText();
					if (getTextDimension(label).x > getTextDimension(
							((Tab) children.get(children.keySet().toArray()[widestLabelIndex])).getText()).x) {
						widestLabelIndex = i;
					}
				}
			}
		}

		computeTopAndBottomTab();
	}

	/**
	 * Selects one for the elements in the list.
	 */
	public void select(int index, boolean reveal) {
		if (getSelectionIndex() == index) {
			/*
			 * this index is already selected.
			 */
			return;
		}
		if (index >= 0 && index < elements.length) {
			int lastSelected = getSelectionIndex();
			elements[index].setSelected(true);
			selectedElementIndex = index;
			if (lastSelected != NONE) {
				elements[lastSelected].setSelected(false);
				if (getSelectionIndex() != elements.length - 1) {
					/*
					 * redraw the next tab to fix the border by calling setSelected()
					 */
					elements[getSelectionIndex() + 1].setSelected(false);
				}
			}
			topNavigationElement.redraw();
			bottomNavigationElement.redraw();

			if (selectedElementIndex < topVisibleIndex || selectedElementIndex > bottomVisibleIndex) {
				computeTopAndBottomTab();
			}
		}
		notifyListeners(SWT.Selection, new Event());
	}

	@Override
	public void setSelection(String key, int index) {
		if (elementMap.containsKey(key)) {
			index = Arrays.asList(elementMap.keySet().toArray()).indexOf(key);
		}
		if (getSelectionIndex() == index) {
			/*
			 * this index is already selected.
			 */
			return;
		}
		if (index >= 0 && index < elements.length) {
			int lastSelected = getSelectionIndex();
			elements[index].setSelected(true);
			selectedElementIndex = index;
			if (lastSelected != NONE) {
				elements[lastSelected].setSelected(false);
				if (getSelectionIndex() != elements.length - 1) {
					/*
					 * redraw the next tab to fix the border by calling setSelected()
					 */
					elements[getSelectionIndex() + 1].setSelected(false);
				}
			}
			topNavigationElement.redraw();
			bottomNavigationElement.redraw();

			if (selectedElementIndex < topVisibleIndex || selectedElementIndex > bottomVisibleIndex) {
				computeTopAndBottomTab();
			}
		}
	}

	/**
	 * Selects one for the elements in the list.
	 */
	public void deselectAll() {
		if (getSelectionIndex() != NONE) {
			elements[getSelectionIndex()].setSelected(false);
			selectedElementIndex = NONE;
		}
	}

	private int getIndex(ListElement element) {
		return element.index;
	}

	/**
	 * Computes the size based on the widest string in the list.
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point result = super.computeSize(hHint, wHint, changed);
		if (widestLabelIndex == -1) {
			String properties_not_available = Messages.getString("TabbedPropertyList.properties.not.available"); //$NON-NLS-1$
			result.x = getTextDimension(properties_not_available).x + INDENT;
		} else {
			int width = getTextDimension(elements[widestLabelIndex].getText()).x;
			/*
			 * To anticipate for the icon placement we should always keep the space
			 * available after the label. So when the active tab includes an icon the width
			 * of the tab doesn't change.
			 */
			result.x = width + 32;
			result.x = result.x >= 125 ? result.x : 125;
		}
		return result;
	}

	private Point getTextDimension(String text) {
		if (textGc == null || textGc.isDisposed()) {
			textGc = new GC(this);
			addDisposeListener(e -> UIUtil.dispose(textGc));
		}
		textGc.setFont(getFont());
		Point point = textGc.textExtent(text);
		point.x++;
		return point;
	}

	/**
	 * Initialize the colours used.
	 */
	private void initColours() {
		/*
		 * Colour 3 COLOR_LIST_BACKGROUND
		 */
		listBackground = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND);

		/*
		 * Colour 13 COLOR_WIDGET_BACKGROUND
		 */
		widgetBackground = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		/*
		 * Colour 15 COLOR_WIDGET_DARK_SHADOW
		 */
		widgetDarkShadow = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);

		/*
		 * Colour 16 COLOR_WIDGET_FOREGROUND
		 */
		widgetForeground = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);

		/*
		 * Colour 19 COLOR_WIDGET_NORMAL_SHADOW
		 */
		widgetNormalShadow = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);

		RGB infoBackground = Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND).getRGB();
		RGB white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE).getRGB();
		RGB black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK).getRGB();

		/*
		 * gradient in the default tab: start colour WIDGET_NORMAL_SHADOW 100% + white
		 * 20% + INFO_BACKGROUND 60% end colour WIDGET_NORMAL_SHADOW 100% +
		 * INFO_BACKGROUND 40%
		 */
		defaultGradientStart = factory.getColors().createColor("TabbedPropertyList.defaultTabGradientStart", //$NON-NLS-1$
				FormColors.blend(infoBackground, FormColors.blend(white, widgetNormalShadow.getRGB(), 20), 60));
		defaultGradientEnd = factory.getColors().createColor("TabbedPropertyList.defaultTabGradientEnd", //$NON-NLS-1$
				FormColors.blend(infoBackground, widgetNormalShadow.getRGB(), 40));

		navigationElementShadowStroke = factory.getColors().createColor("TabbedPropertyList.shadowStroke", //$NON-NLS-1$
				FormColors.blend(white, widgetNormalShadow.getRGB(), 55));
		bottomNavigationElementShadowStroke1 = factory.getColors().createColor("TabbedPropertyList.tabShadowStroke1", //$NON-NLS-1$
				FormColors.blend(black, widgetBackground.getRGB(), 10));
		bottomNavigationElementShadowStroke2 = factory.getColors().createColor("TabbedPropertyList.tabShadowStroke2", //$NON-NLS-1$
				FormColors.blend(black, widgetBackground.getRGB(), 5));

		/*
		 * gradient in the hover tab: start colour WIDGET_BACKGROUND 100% + white 20%
		 * end colour WIDGET_BACKGROUND 100% + WIDGET_NORMAL_SHADOW 10%
		 */
		hoverGradientStart = factory.getColors().createColor("TabbedPropertyList.hoverBackgroundGradientStart", //$NON-NLS-1$
				FormColors.blend(white, widgetBackground.getRGB(), 20));
		hoverGradientEnd = factory.getColors().createColor("TabbedPropertyList.hoverBackgroundGradientEnd", //$NON-NLS-1$
				FormColors.blend(widgetNormalShadow.getRGB(), widgetBackground.getRGB(), 10));

		indentedDefaultBackground = factory.getColors().createColor("TabbedPropertyList.indentedDefaultBackground", //$NON-NLS-1$
				FormColors.blend(white, widgetBackground.getRGB(), 10));
		indentedHoverBackground = factory.getColors().createColor("TabbedPropertyList.indentedHoverBackground", //$NON-NLS-1$
				FormColors.blend(white, widgetBackground.getRGB(), 75));
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		if (textGc != null && !textGc.isDisposed()) {
			textGc.dispose();
			textGc = null;
		}
		super.dispose();
	}

	/**
	 * Get the height of a tab. The height of the tab is the height of the text plus
	 * buffer.
	 *
	 * @return the height of a tab.
	 */
	private int getTabHeight() {
		int tabHeight = getTextDimension("").y + INDENT; //$NON-NLS-1$
		if (tabsThatFitInComposite == 1) {
			/*
			 * if only one tab will fix, reduce the size of the tab height so that the
			 * navigation elements fit.
			 */
			int ret = getBounds().height - 20;
			return (ret > tabHeight) ? tabHeight : (ret < 5) ? 5 : ret;
		}
		return tabHeight;
	}

	private boolean isDownScrollRequired() {
		return elements.length > tabsThatFitInComposite && bottomVisibleIndex != elements.length - 1;
	}

	private boolean isUpScrollRequired() {
		return elements.length > tabsThatFitInComposite && topVisibleIndex != 0;
	}

	private void computeTopAndBottomTab() {
		computeTabsThatFitInComposite();
		if (elements.length == 0) {
			/*
			 * no tabs to display.
			 */
			topVisibleIndex = 0;
			bottomVisibleIndex = 0;
		} else if (tabsThatFitInComposite >= elements.length) {
			/*
			 * all the tabs fit.
			 */
			topVisibleIndex = 0;
			bottomVisibleIndex = elements.length - 1;
		} else if (getSelectionIndex() == NONE) {
			/*
			 * there is no selected tab yet, assume that tab one would be selected for now.
			 */
			topVisibleIndex = 0;
			bottomVisibleIndex = tabsThatFitInComposite - 1;
		} else if (getSelectionIndex() + tabsThatFitInComposite > elements.length) {
			/*
			 * the selected tab is near the bottom.
			 */
			bottomVisibleIndex = elements.length - 1;
			topVisibleIndex = bottomVisibleIndex - tabsThatFitInComposite + 1;
		} else {
			/*
			 * the selected tab is near the top.
			 */
			topVisibleIndex = selectedElementIndex;
			bottomVisibleIndex = selectedElementIndex + tabsThatFitInComposite - 1;
		}
		layoutTabs();
	}

	/**
	 * Layout the tabs.
	 *
	 * @param up if <code>true</code>, then we are laying out as a result of an
	 *           scroll up request.
	 */
	private void layoutTabs() {
		// System.out.println("TabFit " + tabsThatFitInComposite + " length "
		// + elements.length + " top " + topVisibleIndex + " bottom "
		// + bottomVisibleIndex);
		if (tabsThatFitInComposite == NONE || elements.length == 0) {
			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, 0);
			formData.top = new FormAttachment(0, 0);
			formData.height = getTabHeight();
			topNavigationElement.setLayoutData(formData);

			formData = new FormData();
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, 0);
			formData.top = new FormAttachment(topNavigationElement, 0);
			formData.bottom = new FormAttachment(100, 0);
			bottomNavigationElement.setLayoutData(formData);
		} else {

			FormData formData = new FormData();
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, 0);
			formData.top = new FormAttachment(0, 0);
			formData.height = 10;
			topNavigationElement.setLayoutData(formData);

			/*
			 * use nextElement to attach the layout to the previous canvas widget in the
			 * list.
			 */
			Canvas nextElement = topNavigationElement;

			for (int i = 0; i < elements.length; i++) {
				// System.out.print(i + " [" + elements[i].getText() + "]");
				if (i < topVisibleIndex || i > bottomVisibleIndex) {
					/*
					 * this tab is not visible
					 */
					elements[i].setLayoutData(null);
					elements[i].setVisible(false);
				} else {
					/*
					 * this tab is visible.
					 */
					// System.out.print(" visible");
					formData = new FormData();
					formData.height = getTabHeight();
					formData.left = new FormAttachment(0, 0);
					formData.right = new FormAttachment(100, 0);
					formData.top = new FormAttachment(nextElement, 0);
					nextElement = elements[i];
					elements[i].setLayoutData(formData);
					elements[i].setVisible(true);
				}

				// if (i == selectedElementIndex) {
				// System.out.print(" selected");
				// }
				// System.out.println("");
			}
			formData = new FormData();
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, 0);
			formData.top = new FormAttachment(nextElement, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.height = 10;
			bottomNavigationElement.setLayoutData(formData);
		}
		// System.out.println("");

		// layout so that we have enough space for the new labels
		Composite grandparent = getParent().getParent();
		grandparent.layout(true);
		layout(true);
	}

	/**
	 * Initialize the accessibility adapter.
	 */
	private void initAccessible() {
		final Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getName(AccessibleEvent e) {
				if (getSelectionIndex() != NONE) {
					e.result = elements[getSelectionIndex()].getText();
				}
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				if (getSelectionIndex() != NONE) {
					e.result = elements[getSelectionIndex()].getText();
				}
			}
		});

		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {

			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point pt = toControl(new Point(e.x, e.y));
				e.childID = (getBounds().contains(pt)) ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				if (getSelectionIndex() != NONE) {
					Rectangle location = elements[getSelectionIndex()].getBounds();
					Point pt = toDisplay(new Point(location.x, location.y));
					e.x = pt.x;
					e.y = pt.y;
					e.width = location.width;
					e.height = location.height;
				}
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_TABITEM;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL | ACC.STATE_SELECTABLE | ACC.STATE_SELECTED | ACC.STATE_FOCUSED
						| ACC.STATE_FOCUSABLE;
			}
		});

		addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (isFocusControl()) {
					accessible.setFocus(ACC.CHILDID_SELF);
				}
			}
		});

		addListener(SWT.FocusIn, new Listener() {

			@Override
			public void handleEvent(Event event) {
				accessible.setFocus(ACC.CHILDID_SELF);
			}
		});
	}

	@Override
	public Control getControl() {
		return this;
	}

	@Override
	public Control getItem(int index) {
		if (index >= 0 && index < elements.length - 1) {
			return elements[index];
		}
		return null;
	}
}
