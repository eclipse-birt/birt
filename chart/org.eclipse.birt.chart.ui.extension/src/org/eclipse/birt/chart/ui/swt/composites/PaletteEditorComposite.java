/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * This class implements a palette editor widget capable of maintaining a list
 * of fill definitions. Entries may be updated, new entries added at the end or
 * the existing fill entries may be removed.
 */
public final class PaletteEditorComposite extends Composite implements PaintListener, ControlListener, DisposeListener,
		SelectionListener, MouseListener, Listener, KeyListener {

	/**
	 * An internally maintained list of fills directly referenced into a palette
	 */
	private final EList<Fill> elPaletteEntries1;

	/**
	 * Miscellaneous variables used in
	 */
	private int iViewY = 0, iViewHeight = 0, iVisibleCount = 0, iSelectedIndex = 0;

	/**
	 * The height of each color entry in the palette
	 */
	private static final int ITEM_HEIGHT = 30;

	/**
	 * The vertical scrollbar associated with the widget
	 */
	private final ScrollBar sb;

	/**
	 * An offscreen image used to render the palette entries using double buffering.
	 * This image is re-created when a composite resize occurs.
	 */
	private Image imgBuffer = null;

	/**
	 * Associated with the offscreen image and whose lifecycle depends on the
	 * buffered image's lifecycle
	 */
	private GC gc = null;

	/**
	 * Used to edit the color definition in-place
	 */
	private Control coEditor = null;

	/**
	 * Buttons provided to alter the contents of the palette
	 */
	private Button btnAdd, btnRemove, btnUp, btnDown;

	/**
	 *
	 */
	private FillChooserComposite fccNewEntry = null;

	/**
	 *
	 */
	private Composite coPaletteEntries = null;

	/**
	 *
	 */
	private IDeviceRenderer idrSWT = null;

	private ChartWizardContext wizardContext;

	private SeriesDefinition[] vSeriesDefns = null;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.composites"); //$NON-NLS-1$

	private final int iFillChooserStyle;

	/**
	 * Composites for series palette
	 *
	 * @param coParent
	 * @param wizardContext
	 * @param pa1
	 * @param vSeriesDefns
	 * @param iFillChooserStyle style to decide what fill types should display in
	 *                          fill chooser
	 */
	public PaletteEditorComposite(Composite coParent, ChartWizardContext wizardContext, Palette pa1,
			SeriesDefinition[] vSeriesDefns, int iFillChooserStyle) {
		this(coParent, wizardContext, pa1, vSeriesDefns, iFillChooserStyle, SWT.NONE);
	}

	/**
	 * Composites for series palette
	 *
	 * @param coParent
	 * @param wizardContext
	 * @param pa1
	 * @param vSeriesDefns
	 * @param iFillChooserStyle style to decide what fill types should display in
	 *                          fill chooser
	 * @param styles            custom UI styles
	 */
	public PaletteEditorComposite(Composite coParent, ChartWizardContext wizardContext, Palette pa1,
			SeriesDefinition[] vSeriesDefns, int iFillChooserStyle, int styles) {
		super(coParent, styles);
		this.wizardContext = wizardContext;
		this.vSeriesDefns = vSeriesDefns;
		this.iFillChooserStyle = iFillChooserStyle;
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		setLayout(gl);

		coPaletteEntries = new Composite(this, SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		coPaletteEntries.setLayoutData(gd);
		elPaletteEntries1 = pa1.getEntries();
		sb = coPaletteEntries.getVerticalBar();
		sb.addSelectionListener(this);

		Composite coControlPanel = new Composite(this, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		coControlPanel.setLayoutData(gd);
		gl = new GridLayout();
		gl.numColumns = 5;
		coControlPanel.setLayout(gl);
		btnAdd = new Button(coControlPanel, SWT.PUSH);
		gd = new GridData();
		btnAdd.setLayoutData(gd);
		btnAdd.setText(Messages.getString("PaletteEditorComposite.Lbl.Add")); //$NON-NLS-1$
		btnAdd.addSelectionListener(this);

		fccNewEntry = new FillChooserComposite(coControlPanel, SWT.NONE, iFillChooserStyle, wizardContext,
				ColorDefinitionImpl.WHITE());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fccNewEntry.setLayoutData(gd);
		fccNewEntry.addScreenReaderAccessibility(Messages.getString("PaletteEditorComposite.Label.EditColor")); //$NON-NLS-1$

		btnRemove = new Button(coControlPanel, SWT.PUSH);
		gd = new GridData();
		btnRemove.setLayoutData(gd);
		btnRemove.setText(Messages.getString("PaletteEditorComposite.Lbl.Remove")); //$NON-NLS-1$
		btnRemove.addSelectionListener(this);
		btnRemove.setEnabled(elPaletteEntries1.size() > 1 ? true : false);

		btnUp = new Button(coControlPanel, SWT.ARROW | SWT.UP);
		gd = new GridData();
		btnUp.setLayoutData(gd);
		btnUp.setToolTipText(Messages.getString("PaletteEditorComposite.Lbl.Up")); //$NON-NLS-1$
		btnUp.addSelectionListener(this);
		btnUp.setEnabled(elPaletteEntries1.size() > 1 ? true : false);
		btnDown = new Button(coControlPanel, SWT.ARROW | SWT.DOWN);
		gd = new GridData();
		btnDown.setLayoutData(gd);
		btnDown.setToolTipText(Messages.getString("PaletteEditorComposite.Lbl.Down")); //$NON-NLS-1$
		btnDown.addSelectionListener(this);
		btnDown.setEnabled(elPaletteEntries1.size() > 1 ? true : false);

		addControlListener(this);
		addDisposeListener(this);
		coPaletteEntries.addPaintListener(this);
		coPaletteEntries.addMouseListener(this);
		coPaletteEntries.addKeyListener(this);

		try {
			idrSWT = ChartEngine.instance().getRenderer("dv.SWT"); //$NON-NLS-1$
		} catch (ChartException pex) {
			logger.log(pex);
			return;
		}

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (idrSWT != null) {
					idrSWT.dispose();
					idrSWT = null;
				}
			}
		});
	}

	/**
	 * Repaints the palette content
	 */
	@Override
	public void paintControl(PaintEvent pev) {
		Composite co = (Composite) pev.getSource();
		GC gcComposite = pev.gc;
		Display d = pev.display;
		Rectangle rCA = coPaletteEntries.getClientArea();
		if (coEditor == null) {
			coEditor = new FillChooserComposite(co, SWT.NONE, iFillChooserStyle, wizardContext, null);
			coEditor.setBounds(3, 3, rCA.width - 6, ITEM_HEIGHT - 6);
			((FillChooserComposite) coEditor).addListener(this);
		}

		if (imgBuffer == null) {
			imgBuffer = new Image(d, rCA.width, rCA.height);
			gc = new GC(imgBuffer);
			idrSWT.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);
		}
		gc.setBackground(getBackground());
		gc.fillRectangle(rCA);

		iViewHeight = rCA.height;
		int iStartIndex = iViewY / ITEM_HEIGHT;
		if (iStartIndex < 0) {
			iStartIndex = 0;
		}
		iVisibleCount = iViewHeight / ITEM_HEIGHT + 2;
		int iAvailableItems = Math.min(iVisibleCount, elPaletteEntries1.size() - iStartIndex);
		int iY = -(iViewY % ITEM_HEIGHT);

		gc.setForeground(d.getSystemColor(SWT.COLOR_GRAY));

		final RectangleRenderEvent rre = (RectangleRenderEvent) ((EventObjectCache) idrSWT).getEventObject(this,
				RectangleRenderEvent.class);
		final Bounds bo = BoundsImpl.create(0, 0, 0, 0);
		rre.setOutline(LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1));
		rre.setBounds(bo);
		Fill fi;

		for (int i = iStartIndex; i < iStartIndex + iAvailableItems; i++) {
			fi = elPaletteEntries1.get(i);
			if (!super.isEnabled() && fi instanceof ColorDefinition) {
				((ColorDefinitionImpl) fi).setTransparency(128); // set half transparency when using 'auto' color
																	// palette
			}

			if (fi instanceof MultipleFill) {
				rre.setBackground(((MultipleFill) fi).getFills().get(0));
				bo.set(3, iY + 3, (rCA.width - 6) / 2.0, ITEM_HEIGHT - 6);
				try {
					idrSWT.fillRectangle(rre);
				} catch (ChartException rex) {
					logger.log(rex);
				}

				bo.set(rCA.width / 2.0, iY + 3, (rCA.width - 6) / 2.0, ITEM_HEIGHT - 6);
				rre.setBackground((Fill) ((MultipleFill) fi).getFills().get(1));
				try {
					idrSWT.fillRectangle(rre);
				} catch (ChartException rex) {
					logger.log(rex);
				}

				bo.set(3, iY + 3, rCA.width - 6, ITEM_HEIGHT - 6);
				try {
					idrSWT.drawRectangle(rre);
				} catch (ChartException rex) {
					logger.log(rex);
				}
			} else {
				rre.setBackground(fi);
				bo.set(3, iY + 3, rCA.width - 6, ITEM_HEIGHT - 6);
				try {
					if (!super.isEnabled()) {
						rre.getLineAttributes().getColor().set(171, 173, 179);
					}
					idrSWT.fillRectangle(rre);
					idrSWT.drawRectangle(rre);
					if (!super.isEnabled()) {
						rre.getLineAttributes().getColor().set(255, 255, 255);
						Insets ins = GObjectFactory.instance().createInsets(1, 1, 1, 1);
						rre.getBounds().adjust(ins);
						idrSWT.drawRectangle(rre);
					}
				} catch (ChartException rex) {
					logger.log(rex);
				}
			}

			if (i == iSelectedIndex && super.isEnabled()) {
				// WITHIN RANGE; SHOW EDITOR AND UPDATE POSITION
				if (!coEditor.isVisible()) {
					coEditor.setVisible(true);
				}
				coEditor.setLocation(3, iY + 3);
				((FillChooserComposite) coEditor).setFill(fi);
			}
			iY += ITEM_HEIGHT;
		}

		// OUT OF RANGE; HIDE EDITOR
		if (iSelectedIndex < iStartIndex || iSelectedIndex >= iStartIndex + iAvailableItems || !super.isEnabled()) {
			if (coEditor.isVisible()) {
				coEditor.setVisible(false);
			}
		}
		gcComposite.drawImage(imgBuffer, rCA.x, rCA.y);
	}

	/**
	 * The scrollbar's thumb is updated based on the palette entry count and the
	 * current selection
	 */
	private void updateScrollBar() {
		sb.setPageIncrement(iViewHeight);
		sb.setMaximum(ITEM_HEIGHT * elPaletteEntries1.size() - iViewHeight);
		sb.setSelection(iViewY);
	}

	/**
	 *
	 * @param iIndex
	 */
	private void scrollToView(int iIndex) {
		if (iIndex == -1) {
			return;
		}

		int iStartIndex = iViewY / ITEM_HEIGHT;
		if (iStartIndex < 0) {
			iStartIndex = 0;
		}

		if (iIndex > iStartIndex && iIndex < iStartIndex + iVisibleCount - 1) {
			iViewY = (iIndex * ITEM_HEIGHT) - iViewHeight + ITEM_HEIGHT;
			if (iViewY < 0) {
				iViewY = 0;
			}
		} else if (iIndex <= iStartIndex) {
			int iMoveUpTo = iIndex - iVisibleCount;
			if (iMoveUpTo < 0) {
				iMoveUpTo = 0;
			}
			iViewY = iMoveUpTo * ITEM_HEIGHT;
		} else {
			// ADJUST LOWER END IF WE GO BEYOND
			int iY = (iIndex - iStartIndex) * ITEM_HEIGHT - (iViewY % ITEM_HEIGHT);
			if (iY + ITEM_HEIGHT > iViewHeight) // BELOW THE LOWER EDGE
			{
				iViewY += iY + ITEM_HEIGHT - iViewHeight;
			}
		}
		updateScrollBar();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	@Override
	public void controlResized(ControlEvent arg0) {
		updateScrollBar();
		if (imgBuffer != null) {
			gc.dispose();
			imgBuffer.dispose();
			gc = null;
			imgBuffer = null;
		}

		if (coEditor != null) {
			final Rectangle rCA = coPaletteEntries.getClientArea();
			coEditor.setSize(rCA.width - 6, ITEM_HEIGHT - 6);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.
	 * DisposeEvent)
	 */
	@Override
	public void widgetDisposed(DisposeEvent arg0) {
		if (imgBuffer != null) {
			gc.dispose();
			imgBuffer.dispose();
			gc = null;
			imgBuffer = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent sev) {
		if (sev.getSource() == sb) {
			int iSelection = sb.getSelection();
			iViewY = iSelection;
			coPaletteEntries.redraw();
		} else {
			final Button btn = (Button) sev.getSource();
			if (btn == btnAdd) {
				if (fccNewEntry.getFill() != null) {
					append(fccNewEntry.getFill().copyInstance());
				} else {
					append(ColorDefinitionImpl.TRANSPARENT());
				}
			} else if (btn == btnRemove) {
				remove(iSelectedIndex);
			} else if ((btn.getStyle() & SWT.UP) == SWT.UP) {
				if (iSelectedIndex > 0) {
					swap(iSelectedIndex, iSelectedIndex - 1);
				}
			} else if ((btn.getStyle() & SWT.DOWN) == SWT.DOWN) {
				if (iSelectedIndex < elPaletteEntries1.size() - 1) {
					swap(iSelectedIndex, iSelectedIndex + 1);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent mev) {
		int iClickedY = mev.y;
		int iStartIndex = iViewY / ITEM_HEIGHT;
		if (iStartIndex < 0) {
			iStartIndex = 0;
		}
		int iY = -(iViewY % ITEM_HEIGHT);
		int iClickedIndex = iStartIndex + (iClickedY - iY) / ITEM_HEIGHT;
		if (iClickedIndex < 0 || iClickedIndex > elPaletteEntries1.size()) {
			return;
		}

		iSelectedIndex = iClickedIndex;
		coPaletteEntries.redraw();
	}

	/**
	 * Returns the index of the currently selected fill in the palette list
	 *
	 */
	public int getSelectedIndex() {
		return iSelectedIndex;
	}

	/**
	 * Returns the currently selected fill
	 *
	 */
	public ColorDefinitionImpl getSelectedFill() {
		return (ColorDefinitionImpl) elPaletteEntries1.get(iSelectedIndex);
	}

	/**
	 * Removes an entry from the list at the specified index
	 *
	 * @param iIndex
	 */
	public void remove(int iIndex) {
		if (iIndex < 0 || iIndex >= elPaletteEntries1.size()) {
			return;
		}

		int size = elPaletteEntries1.size();
		elPaletteEntries1.remove(iIndex);

		if (vSeriesDefns != null) {
			for (int i = 0; i < vSeriesDefns.length; i++) {
				EList<Fill> el = vSeriesDefns[i].getSeriesPalette().getEntries();
				if ((iIndex - i) >= 0) {
					if (elPaletteEntries1 != el) {
						el.remove(iIndex - i);
					}
				} else {
					int index = size - i + iIndex;
					while (index < 0) {
						index += size;
					}
					if (elPaletteEntries1 != el) {
						el.remove(index);
					}
					if (el.size() > 1) {
						final Fill o = el.get(0);
						el.remove(0);
						el.add(o);
					}
				}
			}
		}

		if (iIndex < iSelectedIndex) {
			iSelectedIndex--;
		} else if (iIndex == iSelectedIndex) {
			if (iSelectedIndex > elPaletteEntries1.size() - 1) {
				iSelectedIndex--;
			}
		}

		if (elPaletteEntries1.size() <= 1) {
			btnRemove.setEnabled(false);
			btnUp.setEnabled(false);
			btnDown.setEnabled(false);
		}

		scrollToView(iSelectedIndex);
		coPaletteEntries.redraw();
	}

	/**
	 * Updates the current selected entry with the specified fill
	 *
	 * @param cdi
	 */
	public void updateSelectionFill(Fill f) {
		if (iSelectedIndex == -1) {
			return;
		}
		elPaletteEntries1.set(iSelectedIndex, f);

		if (vSeriesDefns != null) {
			int size = elPaletteEntries1.size();
			for (int i = 0; i < vSeriesDefns.length; i++) {
				if ((iSelectedIndex - i) >= 0) {
					vSeriesDefns[i].getSeriesPalette().getEntries().set(iSelectedIndex - i, f.copyInstance());
				} else {
					int index = size - i + iSelectedIndex;
					while (index < 0) {
						index += size;
					}
					// Fixed bugzilla bug #198612.
					// The index may be out of size of series palette, so ignore it.
					if (index < vSeriesDefns[i].getSeriesPalette().getEntries().size()) {
						vSeriesDefns[i].getSeriesPalette().getEntries().set(index, f.copyInstance());
					}
				}
			}
		}
		coPaletteEntries.redraw();
	}

	/**
	 * Appends a new fill to the end of the palette list and selects it
	 *
	 * @param cdi
	 */
	public void append(Fill fi) {
		elPaletteEntries1.add(fi);
		iSelectedIndex = elPaletteEntries1.size() - 1;

		if (vSeriesDefns != null) {
			int size = elPaletteEntries1.size();
			for (int i = 0; i < vSeriesDefns.length; i++) {
				if (i < size) {
					if (elPaletteEntries1 != vSeriesDefns[i].getSeriesPalette().getEntries()) {
						vSeriesDefns[i].getSeriesPalette().getEntries().add(size - i - 1, fi.copyInstance());
					}
				} else {
					EList<Fill> el = vSeriesDefns[i - size].getSeriesPalette().getEntries();
					for (int j = 0; j < el.size(); j++) {
						if (elPaletteEntries1 != vSeriesDefns[i].getSeriesPalette().getEntries()) {
							vSeriesDefns[i].getSeriesPalette().getEntries().add(j, el.get(j).copyInstance());
						}
					}
					for (int j = el.size(); j < vSeriesDefns[i].getSeriesPalette().getEntries().size(); j++) {
						vSeriesDefns[i].getSeriesPalette().getEntries().remove(j);
					}
				}
			}
		}

		if (!btnRemove.isEnabled()) {
			btnRemove.setEnabled(true);
			btnUp.setEnabled(true);
			btnDown.setEnabled(true);
		}
		scrollToView(iSelectedIndex);
		coPaletteEntries.redraw();
	}

	/**
	 * Swap two consecutive entries
	 *
	 * @param iIndex1
	 * @param iIndex2
	 */
	private void swap(int iIndex1, int iIndex2) {
		final Fill o1 = elPaletteEntries1.get(iIndex1);
		final Fill o2 = elPaletteEntries1.get(iIndex2);

		if (iIndex1 < iIndex2) {
			elPaletteEntries1.remove(iIndex2);
			elPaletteEntries1.add(iIndex1, o2);
			elPaletteEntries1.remove(iIndex2);
			elPaletteEntries1.add(iIndex2, o1);
		} else {
			elPaletteEntries1.remove(iIndex1);
			elPaletteEntries1.add(iIndex2, o1);
			elPaletteEntries1.remove(iIndex1);
			elPaletteEntries1.add(iIndex1, o2);
		}

		if (iSelectedIndex == iIndex1) {
			iSelectedIndex = iIndex2;
		} else if (iSelectedIndex == iIndex2) {
			iSelectedIndex = iIndex1;
		}

		if (vSeriesDefns != null) {
			int index1 = iIndex1;
			int index2 = iIndex2;
			if (isMultiAxes()
					&& wizardContext.getModel().getLegend().getItemType().getValue() == LegendItemType.SERIES) {
				int min = Math.min(index1, index2);
				int max = Math.max(index1, index2);

				if (vSeriesDefns.length - 1 < min) {
					// do not need to change series definitions.
				} else if (min <= vSeriesDefns.length - 1 && vSeriesDefns.length - 1 < max) {
					// change the min
					vSeriesDefns[min].getSeriesPalette().getEntries().set(0, elPaletteEntries1.get(min).copyInstance());
				} else if (vSeriesDefns.length - 1 >= max) {
					// change the min and max
					vSeriesDefns[min].getSeriesPalette().getEntries().set(0, elPaletteEntries1.get(min).copyInstance());
					vSeriesDefns[max].getSeriesPalette().getEntries().set(0, elPaletteEntries1.get(max).copyInstance());
				}
			} else {
				for (int i = 0; i < vSeriesDefns.length; i++) {
					int size = vSeriesDefns[i].getSeriesPalette().getEntries().size();
					if ((iIndex1 - i) >= 0) {
						index1 = iIndex1 - i;
					} else {
						index1 = size - i + iIndex1;
					}

					if ((iIndex2 - i) >= 0) {
						index2 = iIndex2 - i;
					} else {
						index2 = size - i + iIndex2;
					}

					EList<Fill> el = vSeriesDefns[i].getSeriesPalette().getEntries();
					final Fill o3 = el.get(index1);
					final Fill o4 = el.get(index2);

					if (index1 < index2) {
						el.remove(index2);
						el.remove(index1);
						el.add(index1, o4);
						el.add(index2, o3);
					} else {
						el.remove(index1);
						el.remove(index2);
						el.add(index2, o3);
						el.add(index1, o4);
					}
				}
			}
		}

		scrollToView(iSelectedIndex);
		coPaletteEntries.redraw();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event ev) {
		updateSelectionFill((Fill) ev.data);
	}

	// UNUSED INTERFACE METHODS:

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	@Override
	public void controlMoved(ControlEvent arg0) {
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent sev) {
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent arg0) {
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.
	 * KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.keyCode) {
		case SWT.ARROW_DOWN:
			if (iSelectedIndex < elPaletteEntries1.size() - 1) {
				iSelectedIndex++;
				scrollToView(iSelectedIndex);
				coPaletteEntries.redraw();
			}
			break;
		case SWT.ARROW_UP:
			if (iSelectedIndex > 0) {
				iSelectedIndex--;
				scrollToView(iSelectedIndex);
				coPaletteEntries.redraw();
			}
			break;
		case SWT.CR:
			if (coPaletteEntries.isFocusControl()) {
				coEditor.setFocus();
			}
			break;
		case SWT.PAGE_DOWN:
			iSelectedIndex += 8;
			if (iSelectedIndex > elPaletteEntries1.size()) {
				iSelectedIndex = elPaletteEntries1.size() - 1;
			}
			scrollToView(iSelectedIndex);
			sb.setSelection(sb.getMaximum() * (iSelectedIndex + 1) / elPaletteEntries1.size());
			coPaletteEntries.redraw();
			break;
		case SWT.PAGE_UP:
			iSelectedIndex -= 8;
			if (iSelectedIndex < 0) {
				iSelectedIndex = 0;
			}
			scrollToView(iSelectedIndex);
			sb.setSelection(sb.getMaximum() * iSelectedIndex / elPaletteEntries1.size());
			coPaletteEntries.redraw();
			break;
		case SWT.TAB:
			btnAdd.setFocus();
			break;
		case SWT.ESC:
			this.getShell().close();
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.
	 * KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	private boolean isMultiAxes() {
		return ChartUIUtil.getOrthogonalAxisNumber(wizardContext.getModel()) > 1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnAdd.setEnabled(enabled);
		btnRemove.setEnabled(enabled);
		btnDown.setEnabled(enabled);
		btnUp.setEnabled(enabled);
		if (coEditor != null) {
			coEditor.setEnabled(enabled);
		}
		if (coPaletteEntries != null) {
			coPaletteEntries.setEnabled(enabled);
		}
	}
}
