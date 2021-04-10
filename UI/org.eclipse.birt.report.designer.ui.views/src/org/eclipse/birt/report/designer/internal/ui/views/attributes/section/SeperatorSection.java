
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SeperatorSection extends Section {

	private int style;

	public SeperatorSection(Composite parent, int style) {
		super(" ", parent, false); //$NON-NLS-1$
		this.style = style;
	}

	protected Label seperator;

	public void createSection() {
		getSeperatorControl(parent);
		getGridPlaceholder(parent);
	}

	public Label getSeperatorControl() {
		return seperator;
	}

	protected Label getSeperatorControl(Composite parent) {
		if (seperator == null) {
			seperator = FormWidgetFactory.getInstance().createSeparator(parent, style);
			seperator.setLayoutData(new GridData());
			seperator.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					seperator = null;
				}
			});
		} else {
			checkParent(seperator, parent);
		}
		return seperator;
	}

	public void layout() {
		GridData gd = (GridData) seperator.getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		gd.horizontalAlignment = GridData.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillSeperator;

	}

	public void load() {
		// TODO Auto-generated method stub

	}

	public void setInput(Object input) {
		// TODO Auto-generated method stub

	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private boolean fillSeperator = true;

	public boolean isFillSeperator() {
		return fillSeperator;
	}

	public void setFillSeperator(boolean fillSeperator) {
		this.fillSeperator = fillSeperator;
	}

	public void setHidden(boolean isHidden) {
		if (seperator != null)
			WidgetUtil.setExcludeGridData(seperator, isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (seperator != null)
			seperator.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}
}
