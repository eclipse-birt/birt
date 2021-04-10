
package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIHelper;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;

public abstract class AccordionControl extends Composite {

	/** Pixel spacing between items in the content area */
	private static final int ITEM_SPACING = 0;

	private static final String KEY_CONTENT = "content"; //$NON-NLS-1$
	private static final String KEY_HEADER = "header"; //$NON-NLS-1$

	private Image mClosed;
	private Image mOpen;
	private boolean mSingle = true;
	private boolean mWrap;

	public static final Image ICON_COLLAPSE = UIHelper.getImage(ReportPlugin.getDefault().getBundle(),
			ReportPlatformUIImages.ICONS_PATH + ReportPlatformUIImages.OBJ16_PATH + "collapse.png");

	public static final Image ICON_EXPAND = UIHelper.getImage(ReportPlugin.getDefault().getBundle(),
			ReportPlatformUIImages.ICONS_PATH + ReportPlatformUIImages.OBJ16_PATH + "expand.png");

	/**
	 * Creates the container which will hold the items in a category; this can be
	 * overridden to lay out the children with a different layout than the default
	 * vertical RowLayout
	 */
	protected Composite createChildContainer(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		if (mWrap) {
			RowLayout layout = new RowLayout(SWT.HORIZONTAL);
			layout.center = true;
			composite.setLayout(layout);
		} else {
			RowLayout layout = new RowLayout(SWT.VERTICAL);
			layout.spacing = ITEM_SPACING;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.marginLeft = 0;
			layout.marginTop = 0;
			layout.marginRight = 0;
			layout.marginBottom = 0;
			composite.setLayout(layout);
		}

		// TODO - maybe do multi-column arrangement for simple nodes
		return composite;
	}

	/**
	 * Creates the children under a particular header
	 * 
	 * @param parent the parent composite to add the SWT items to
	 * @param header the header object that is being opened for the first time
	 */
	protected abstract void createChildren(Composite parent, Object header);

	/**
	 * Set whether a single category should be enforced or not (default=true)
	 * 
	 * @param single if true, enforce a single category open at a time
	 */
	public void setAutoClose(boolean single) {
		mSingle = single;
	}

	/**
	 * Returns whether a single category should be enforced or not (default=true)
	 * 
	 * @return true if only a single category can be open at a time
	 */
	public boolean isAutoClose() {
		return mSingle;
	}

	/**
	 * Returns the labels used as header categories
	 * 
	 * @return list of header labels
	 */
	public List<AccordionLabel> getHeaderLabels() {
		List<AccordionLabel> headers = new ArrayList<AccordionLabel>();
		for (Control c : getChildren()) {
			if (c instanceof AccordionLabel) {
				headers.add((AccordionLabel) c);
			}
		}

		return headers;
	}

	/**
	 * Show all categories
	 * 
	 * @param performLayout if true, call {@link #layout} and {@link #pack} when
	 *                      done
	 */
	public void expandAll(boolean performLayout) {
		for (Control c : getChildren()) {
			if (c instanceof AccordionLabel) {
				if (!isOpen(c)) {
					toggle((AccordionLabel) c, false, false);
				}
			}
		}
		if (performLayout) {
			pack();
			layout();
		}
	}

	/**
	 * Hide all categories
	 * 
	 * @param performLayout if true, call {@link #layout} and {@link #pack} when
	 *                      done
	 */
	public void collapseAll(boolean performLayout) {
		for (Control c : getChildren()) {
			if (c instanceof AccordionLabel) {
				if (isOpen(c)) {
					toggle((AccordionLabel) c, false, false);
				}
			}
		}
		if (performLayout) {
			layout();
		}
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent       the parent widget to add the accordion to
	 * @param style        the SWT style mask to use
	 * @param headers      a list of headers, whose {@link Object#toString} method
	 *                     should produce the heading label
	 * @param greedy       if true, grow vertically as much as possible
	 * @param wrapChildren if true, configure the child area to be horizontally laid
	 *                     out with wrapping
	 * @param expand       Set of headers to expand initially
	 */

	public AccordionControl(Composite parent, int style, List<?> headers, boolean greedy, boolean wrapChildren) {
		super(parent, style);

		mWrap = wrapChildren;

		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 4;
		gridLayout.marginHeight = 5;
		setLayout(gridLayout);

		// this.setBackground( Display.getDefault( ).getSystemColor(
		// SWT.COLOR_WHITE ) );

		Font labelFont = null;

		mOpen = ICON_EXPAND; // $NON-NLS-1$
		mClosed = ICON_COLLAPSE; // $NON-NLS-1$
		List<AccordionLabel> expandLabels = new ArrayList<AccordionLabel>();

		for (int i = 0; i < headers.size(); i++) {
			Object header = headers.get(i);
			final AccordionLabel label = new AccordionLabel(this, SWT.SHADOW_ETCHED_OUT);

			GridLayout gl = new GridLayout();
			gl.marginHeight = 0;
			label.setLayout(gl);

			label.setAction(getHeaderAction(header));
			label.setText(getHeaderTitle(header).replace("&", "&&")); //$NON-NLS-1$ //$NON-NLS-2$
			updateBackground(label, false);
			if (labelFont == null) {
				labelFont = label.getFont();
				FontData normal = labelFont.getFontData()[0];
				FontData bold = new FontData(normal.getName(), normal.getHeight(), SWT.BOLD);
				labelFont = new Font(null, bold);
			}
			label.setFont(labelFont);
			GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			if (i > 0)
				gd.verticalIndent = 5;
			label.setLayoutData(gd);
			setHeader(header, label);
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseUp(MouseEvent e) {
					if (e.button == 1 && (e.stateMask & SWT.MODIFIER_MASK) == 0) {
						if (label.isActionArea(e.x, e.y)) {
							if (label.getAction() != null) {
								label.getAction().run();
							}
						} else {
							toggle(label, true, mSingle);
						}
					}
				}
			});
			label.addMouseTrackListener(new MouseTrackListener() {

				public void mouseEnter(MouseEvent e) {
					updateBackground(label, true);
				}

				public void mouseExit(MouseEvent e) {
					updateBackground(label, false);
				}

				public void mouseHover(MouseEvent e) {
				}
			});

			// Turn off border?
			final ScrolledComposite scrolledComposite = new AccordionSubComposite(this,
					SWT.V_SCROLL | SWT.SHADOW_ETCHED_OUT);
			ScrollBar verticalBar = scrolledComposite.getVerticalBar();
			verticalBar.setIncrement(20);
			verticalBar.setPageIncrement(100);

			// Do we need the scrolled composite or can we just look at the next
			// wizard in the hierarchy?

			setContentArea(label, scrolledComposite);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			GridData scrollGridData = new GridData(SWT.FILL, greedy ? SWT.FILL : SWT.TOP, false, greedy, 1, 1);
			scrollGridData.exclude = true;
			scrollGridData.grabExcessHorizontalSpace = wrapChildren;
			scrolledComposite.setLayoutData(scrollGridData);

			if (wrapChildren) {
				scrolledComposite.addControlListener(new ControlAdapter() {

					@Override
					public void controlResized(ControlEvent e) {
						Rectangle r = scrolledComposite.getClientArea();
						Control content = scrolledComposite.getContent();
						if (content != null && r != null) {
							Point minSize = content.computeSize(r.width, SWT.DEFAULT);
							scrolledComposite.setMinSize(minSize);
							ScrollBar vBar = scrolledComposite.getVerticalBar();
							vBar.setPageIncrement(r.height);
						}
					}
				});
			}

			updateIcon(label);
			if (getHeaderExpandStatus(header)) {
				expandLabels.add(label);
			}
		}

		for (AccordionLabel label : expandLabels) {
			toggle(label, false, false);
		}
	}

	public abstract IAction getHeaderAction(Object header);

	public abstract String getHeaderTitle(Object header);

	public abstract boolean getHeaderExpandStatus(Object header);

	/** Updates the background gradient of the given header label */
	private void updateBackground(AccordionLabel label, boolean mouseOver) {
		Display display = label.getDisplay();
		label.setBackground(
				new Color[] { display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
						display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
						display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW) },
				new int[] { mouseOver ? 40 : 20, 100 }, true);
	}

	public void refresh() {
		for (int i = 0; i < getHeaderLabels().size(); i++) {
			final AccordionLabel label = getHeaderLabels().get(i);
			String text = getHeaderTitle(label.getData(KEY_HEADER));
			if (text != null && text.trim().length() > 0) {
				label.setText(text.replace("&", "&&"));
			}
			updateBackground(label, false);
		}
	}

	/**
	 * Updates the icon for a header label to be open/close based on the
	 * {@link #isOpen} state
	 */
	private void updateIcon(AccordionLabel label) {
		label.setImage(isOpen(label) ? mOpen : mClosed);
	}

	/** Returns true if the content area for the given label is open/showing */
	private boolean isOpen(Control label) {
		if (getContentArea(label) == null)
			return false;
		return !((GridData) getContentArea(label).getLayoutData()).exclude;
	}

	/** Toggles the visibility of the children of the given label */
	private void toggle(AccordionLabel label, boolean performLayout, boolean autoClose) {
		if (autoClose) {
			collapseAll(true);
		}
		ScrolledComposite scrolledComposite = getContentArea(label);

		GridData scrollGridData = (GridData) scrolledComposite.getLayoutData();
		boolean close = !scrollGridData.exclude;
		scrollGridData.exclude = close;
		scrolledComposite.setVisible(!close);
		updateIcon(label);

		if (!scrollGridData.exclude && scrolledComposite.getContent() == null) {
			Composite composite = createChildContainer(scrolledComposite);
			Object header = getHeader(label);
			createChildren(composite, header);
			scrolledComposite.setContent(composite);
			scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

		if (performLayout) {
			layout(true);
		}

		Event event = new Event();
		event.widget = this;
		notifyListeners(SWT.SELECTED, event);
	}

	/** Returns the header object for the given header label */
	private Object getHeader(Control label) {
		return label.getData(KEY_HEADER);
	}

	/** Sets the header object for the given header label */
	private void setHeader(Object header, final AccordionLabel label) {
		label.setData(KEY_HEADER, header);
	}

	/** Returns the content area for the given header label */
	private ScrolledComposite getContentArea(Control label) {
		return (ScrolledComposite) label.getData(KEY_CONTENT);
	}

	/** Sets the content area for the given header label */
	private void setContentArea(final AccordionLabel label, ScrolledComposite scrolledComposite) {
		label.setData(KEY_CONTENT, scrolledComposite);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Returns the set of expanded categories in the palette. Note: Header labels
	 * will have escaped ampersand characters with double ampersands.
	 * 
	 * @return the set of expanded categories in the palette - never null
	 */
	public List getExpandedCategories() {
		List expanded = new ArrayList();
		for (Control c : getChildren()) {
			if (c instanceof AccordionLabel) {
				if (isOpen(c)) {
					expanded.add(((AccordionLabel) c).getData(KEY_HEADER));
				}
			}
		}
		return expanded;
	}
}