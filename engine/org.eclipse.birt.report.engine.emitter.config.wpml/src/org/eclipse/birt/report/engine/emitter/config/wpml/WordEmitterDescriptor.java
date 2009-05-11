/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config.wpml;

import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.wpml.i18n.Messages;

/**
 * This class is a descriptor of word emitter.
 */
public class WordEmitterDescriptor extends AbstractEmitterDescriptor
{

	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new WordOptionObserver( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "WordEmitter.Description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#
	 * getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "WordEmitter.DisplayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IEmitterDescriptor#getID()
	 */
	public String getID( )
	{
		return "org.eclipse.birt.report.engine.emitter.word"; //$NON-NLS-1$
	}

	class WordOptionObserver extends AbstractConfigurableOptionObserver
	{

		@Override
		public IConfigurableOption[] getOptions( )
		{
			return null;
		}

		@Override
		public IRenderOption getPreferredRenderOption( )
		{
			RenderOption renderOption = new RenderOption( );

			renderOption.setEmitterID( getID( ) );
			renderOption.setOutputFormat( "doc" ); //$NON-NLS-1$

			// TODO set option values

			return renderOption;
		}
	}

}
