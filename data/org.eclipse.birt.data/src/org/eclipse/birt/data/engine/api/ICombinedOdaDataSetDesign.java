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
package org.eclipse.birt.data.engine.api;

import java.util.Set;

/**
 * Special Oda data set, containing multiple oda data set design
 * 
 * @author xzhao
 *
 */
public interface ICombinedOdaDataSetDesign extends IOdaDataSetDesign {
	void addDataSetDesign(IOdaDataSetDesign dataSetDesign);

	Set<IOdaDataSetDesign> getDataSetDesigns();
}
