/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * TODO: Please document
 *
 * @author Roshan Ail
 * @version $Revision: 1.2 $ $Date: 2006/06/16 03:12:04 $
 */
public abstract class AbstractCubePropertyPage extends AbstractPropertyPage {

	private transient Label pageDescription = null;
	private Composite composite;
	private ScrolledComposite sComposite;

	/**
	 *
	 */
	public AbstractCubePropertyPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createPageControl(Composite parent) {
		sComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		sComposite.setExpandHorizontal(true);
		sComposite.setExpandVertical(true);
		sComposite.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				computeSize();
			}
		});

		composite = new Composite(sComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		if (getPageDescription() != null) {
			pageDescription = new Label(composite, SWT.NONE);
			pageDescription.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			pageDescription.setText(getPageDescription());
			pageDescription.setToolTipText(getPageDescription());
		}
		GridData data = new GridData(GridData.FILL_BOTH);
		Control control = createContents(composite);
		control.setLayoutData(data);

		sComposite.setContent(composite);

		return sComposite;
	}

	private void computeSize() {
		sComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.layout();
	}

	public abstract Control createContents(Composite parent);

	/**
	 * This method returns the page description. It is displayed just below the
	 * title. The default implementation returns null in which case it doesn't
	 * display anything. Subclasses must reimplement this method and return a string
	 * if they wish to display a short one line description.
	 *
	 * @return The one line description.
	 */
	public String getPageDescription() {
		return null;
	}
}
