/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.viewers.TableViewer;

public abstract class AbstractDatasetSortingFormHandleProvider extends AbstractSortingFormHandleProvider {

	public abstract void clearAllBindingColumns();

	public abstract boolean isClearEnable();

	public abstract void setBindingObject(DesignElementHandle bindingObject);

	public abstract void setTableViewer(TableViewer tableViewer);

	public abstract void generateAllBindingColumns();

}
