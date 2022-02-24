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

import java.util.HashMap;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.FormColors;

public class TabbedPropertyTitle extends Composite implements IPropertyChangeListener {

	private CLabel label;

	private Image image = null;

	private String text = null;

	private static final String BLANK = ""; //$NON-NLS-1$

	/**
	 * Width of the margin that will be added around the control.
	 */
	public int marginWidth = 4;

	/**
	 * Height of the margin that will be added around the control.
	 */
	public int marginHeight = 4;

	private FormWidgetFactory factory;

	private ToolBar toolbar;

	// private Button resetButton;

	/**
	 * Constructor for TabbedPropertyTitle.
	 * 
	 * @param parent  the parent composite.
	 * @param factory the widget factory for the tabbed property sheet
	 */
	public TabbedPropertyTitle(Composite parent, FormWidgetFactory factory) {
		super(parent, SWT.NONE);
		this.factory = factory;

		bg = factory.getColors().getColor(FormColors.TB_BG);
		gbg = factory.getColors().getColor(FormColors.TB_GBG);
		border = factory.getColors().getColor(FormColors.TB_BORDER);

		this.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (image == null && (text == null || text.equals(BLANK))) {
					label.setVisible(false);
				} else {
					label.setVisible(true);
					drawTitleBackground(e);
				}
			}
		});

		this.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				if (toolbar != null) {
					toolbar.setFocus();

				} else
					getParent().setFocus();
			}

			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

		});

		factory.getColors().initializeSectionToolBarColors();
		setBackground(factory.getColors().getBackground());
		setForeground(factory.getColors().getForeground());

		FormLayout layout = new FormLayout();
		layout.marginWidth = ITabbedPropertyConstants.HSPACE + 6;
		layout.marginHeight = 5;
		setLayout(layout);

		label = new CLabel(this, SWT.NONE) {

			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point p = super.computeSize(wHint, hHint, changed);
				p.y = p.y + 2;
				return p;
			}
		};
		label.setBackground(parent.getBackground());
		label.setText(BLANK);
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		label.setLayout(gl);

		// resetButton = new Button( label, SWT.FLAT );
		// resetButton.setText( Messages.getString(
		// "TabbedPropertyTitle.Button.Default.Text" ) ); //$NON-NLS-1$
		// GridData gd = new GridData( );
		// gd.grabExcessHorizontalSpace = true;
		// gd.horizontalAlignment = SWT.END;
		// gd.grabExcessVerticalSpace = true;
		// gd.verticalAlignment = SWT.CENTER;
		// resetButton.setLayoutData( gd );
		// resetButton.setVisible( false );
		// resetButton.addSelectionListener( new SelectionAdapter( ) {
		//
		// public void widgetSelected( SelectionEvent e )
		// {
		// Event event = new Event( );
		// event.widget = resetButton;
		// TabbedPropertyTitle.this.notifyListeners( SWT.SELECTED, event );
		// }
		//
		// } );
		// resetButton.setToolTipText( Messages.getString(
		// "TabbedPropertyTitle.Button.Default.TooltipText" ) ); //$NON-NLS-1$

		label.setBackground(new Color[] { factory.getColors().getColor(FormColors.TB_BG),
				factory.getColors().getColor(FormColors.TB_GBG) }, new int[] { 100 }, true);
		label.setFont(JFaceResources.getBannerFont());
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		label.setLayoutData(data);

		/*
		 * setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
		 * ISharedImages.IMG_OBJ_ELEMENT));
		 */

		new ToolBar(this, SWT.FLAT);
	}

	public void showResetButton(boolean show) {
		// resetButton.setVisible( show );
		// resetButton.setEnabled( show );
	}

	/**
	 * @param e
	 */
	protected void drawTitleBackground(PaintEvent e) {

		Rectangle bounds = getClientArea();
		Point tsize = null;
		Point labelSize = null;
		int twidth = bounds.width - marginWidth - marginWidth;
		if (label != null) {
			labelSize = label.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		}
		if (labelSize != null) {
			twidth -= labelSize.x + 4;
		}
		int tvmargin = 4;
		int theight = getHeight();
		if (tsize != null) {
			theight += Math.max(theight, tsize.y);
		}
		if (labelSize != null) {
			theight = Math.max(theight, labelSize.y);
		}
		theight += tvmargin + tvmargin;
		int midpoint = (theight * 66) / 100;
		int rem = theight - midpoint;
		GC gc = e.gc;
		gc.setForeground(bg);
		gc.setBackground(gbg);
		gc.fillGradientRectangle(marginWidth, marginHeight, bounds.width - 1 - marginWidth - marginWidth, midpoint - 1,
				true);
		gc.setForeground(gbg);
		gc.setBackground(getBackground());
		gc.fillGradientRectangle(marginWidth, marginHeight + midpoint - 1, bounds.width - 1 - marginWidth - marginWidth,
				rem - 1, true);
		gc.setForeground(border);
		gc.drawLine(marginWidth, marginHeight + 2, marginWidth, marginHeight + theight - 1);
		gc.drawLine(marginWidth, marginHeight + 2, marginWidth + 2, marginHeight);
		gc.drawLine(marginWidth + 2, marginHeight, bounds.width - marginWidth - 3, marginHeight);
		gc.drawLine(bounds.width - marginWidth - 3, marginHeight, bounds.width - marginWidth - 1, marginHeight + 2);
		gc.drawLine(bounds.width - marginWidth - 1, marginHeight + 2, bounds.width - marginWidth - 1,
				marginHeight + theight - 1);
	}

	/**
	 * Set the text label.
	 * 
	 * @param text the text label.
	 */
	public void setTitle(String text, Image image) {
		this.text = text;
		this.image = image;
		if (text != null) {
			label.setText(text);
		} else {
			label.setText(BLANK);
		}
		label.setImage(image);
		redraw();
	}

	/**
	 * @return the height of the title.
	 */
	public int getHeight() {
		Shell shell = new Shell();
		GC gc = new GC(shell);
		gc.setFont(getFont());
		Point point = gc.textExtent(BLANK);
		point.x++;
		int textOrImageHeight = Math.max(point.x, 16);
		gc.dispose();
		shell.dispose();
		return textOrImageHeight + 8;
	}

	private HashMap actionMap = new HashMap();

	public void setActions(IAction[] actions) {
		if (actions != null) {
			if (toolbar != null) {
				while (toolbar.getItemCount() > 0) {
					ToolItem item = toolbar.getItem(0);
					IAction action = (IAction) actionMap.get(item);
					if (action != null)
						action.removePropertyChangeListener(this);
					item.dispose();
				}
				actionMap.clear();
				toolbar.dispose();
			}

			toolbar = new ToolBar(label, SWT.FLAT);
			toolbar.setBackground(gbg);
			GridData gd = new GridData();
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.END;
			gd.grabExcessVerticalSpace = true;
			gd.verticalAlignment = SWT.CENTER;
			toolbar.setLayoutData(gd);

			for (int i = 0; i < actions.length; i++) {
				IAction action = actions[i];
				int flags = SWT.PUSH;
				if (action != null) {
					int style = action.getStyle();
					if (style == IAction.AS_CHECK_BOX) {
						flags = SWT.CHECK;
					} else if (style == IAction.AS_RADIO_BUTTON) {
						flags = SWT.RADIO;
					} else if (style == IAction.AS_DROP_DOWN_MENU) {
						flags = SWT.DROP_DOWN;
					}
				}
				ToolItem item = new ToolItem(toolbar, flags);
				item.addListener(SWT.Selection, getToolItemListener());
				action.addPropertyChangeListener(this);
				actionMap.put(item, action);
			}
			updateToolBar();
			label.layout();
		} else {
			if (toolbar != null) {
				toolbar.dispose();
				toolbar = null;
			}
		}
	}

	private Listener toolItemListener;

	private Listener getToolItemListener() {
		if (toolItemListener == null) {
			toolItemListener = new Listener() {

				public void handleEvent(Event event) {
					switch (event.type) {
					case SWT.Selection:
						Widget ew = event.widget;
						if (ew != null) {
							handleWidgetSelection(event, ((ToolItem) ew));
						}
						break;
					}
				}
			};
		}
		return toolItemListener;
	}

	private void handleWidgetSelection(Event e, ToolItem item) {

		boolean selection = item.getSelection();

		int style = item.getStyle();
		IAction action = (IAction) actionMap.get(item);

		if ((style & (SWT.TOGGLE | SWT.CHECK)) != 0) {
			if (action.getStyle() == IAction.AS_CHECK_BOX) {
				action.setChecked(selection);
			}
		} else if ((style & SWT.RADIO) != 0) {
			if (action.getStyle() == IAction.AS_RADIO_BUTTON) {
				action.setChecked(selection);
			}
		} else if ((style & SWT.DROP_DOWN) != 0) {
			if (e.detail == 4) { // on drop-down button
				if (action.getStyle() == IAction.AS_DROP_DOWN_MENU) {
					IMenuCreator mc = action.getMenuCreator();
					ToolItem ti = (ToolItem) item;
					if (mc != null) {
						Menu m = mc.getMenu(ti.getParent());
						if (m != null) {
							Point point = ti.getParent().toDisplay(new Point(e.x, e.y));
							m.setLocation(point.x, point.y); // waiting
							m.setVisible(true);
							return; // we don't fire the action
						}
					}
				}
			}
		}

		action.runWithEvent(e);
	}

	private LocalResourceManager imageManager;

	private Color bg;

	private Color gbg;

	private Color border;

	public void propertyChange(PropertyChangeEvent event) {
		updateToolBar();
	}

	private void updateToolBar() {
		if (toolbar != null) {
			ResourceManager parentResourceManager = JFaceResources.getResources();
			LocalResourceManager localManager = new LocalResourceManager(parentResourceManager);

			for (int i = 0; i < toolbar.getItemCount(); i++) {
				ToolItem item = toolbar.getItem(i);
				IAction action = (IAction) actionMap.get(item);
				if (action != null) {
					ImageDescriptor image = null;
					if (action.getImageDescriptor() != null)
						image = action.getImageDescriptor();
					if (image != null)
						item.setImage(localManager.createImageWithDefault(image));

					item.setToolTipText(action.getToolTipText());
					if (IAction.AS_CHECK_BOX == action.getStyle()) {
						item.setSelection(action.isChecked());
					}

					item.setEnabled(action.isEnabled());
				}
			}

			disposeOldImages();
			imageManager = localManager;

			if (toolbar.isFocusControl())
				toolbar.setFocus();
		}
	}

	private void disposeOldImages() {
		if (imageManager != null) {
			imageManager.dispose();
			imageManager = null;
		}
	}

	public void dispose() {
		super.dispose();
		disposeOldImages();
	}
}
