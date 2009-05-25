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

package org.eclipse.birt.report.engine.emitter.config.excel;

import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.emitter.config.AbstractConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor;
import org.eclipse.birt.report.engine.emitter.config.ConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOption;
import org.eclipse.birt.report.engine.emitter.config.IConfigurableOptionObserver;
import org.eclipse.birt.report.engine.emitter.config.IOptionValue;
import org.eclipse.birt.report.engine.emitter.config.excel.i18n.Messages;

/**
 * This class is a descriptor of excel emitter.
 */
public class ExcelEmitterDescriptor extends AbstractEmitterDescriptor
{

	private IConfigurableOption[] options;

	public ExcelEmitterDescriptor( )
	{
		initOptions( );
	}

	private void initOptions( )
	{
		// Initializes the option for WrappingText.
		ConfigurableOption wrappingText = new ConfigurableOption(
				IExcelRenderOption.WRAPPING_TEXT );
		wrappingText.setDisplayName( Messages
				.getString( "OptionDisplayValue.WrappingText" ) ); //$NON-NLS-1$
		wrappingText.setDataType( IConfigurableOption.DataType.BOOLEAN );
		wrappingText.setDisplayType( IConfigurableOption.DisplayType.CHECKBOX );
		wrappingText.setDefaultValue( Boolean.TRUE );
		wrappingText.setToolTip( null );
		wrappingText.setDescription( Messages
				.getString( "OptionDescription.WrappingText" ) ); //$NON-NLS-1$

		options = new IConfigurableOption[]{wrappingText};
	}

	@Override
	public IConfigurableOptionObserver createOptionObserver( )
	{
		return new ExcelOptionObserver( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.AbstractEmitterDescriptor
	 * #getDescription()
	 */
	public String getDescription( )
	{
		return Messages.getString( "ExcelEmitter.Description" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getDisplayName
	 * ()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "ExcelEmitter.DisplayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitters.IEmitterDescriptor#getID()
	 */
	public String getID( )
	{
		return "org.eclipse.birt.report.engine.emitter.prototype.excel"; //$NON-NLS-1$
	}

	/**
	 * ExcelOptionObserver
	 */
	class ExcelOptionObserver extends AbstractConfigurableOptionObserver
	{

		public IConfigurableOption[] getOptions( )
		{
			return options;
		}

		public IRenderOption getPreferredRenderOption( )
		{
			EXCELRenderOption renderOption = new EXCELRenderOption( );

			renderOption.setEmitterID( getID( ) );
			renderOption.setOutputFormat( "xls" ); //$NON-NLS-1$

			
			for ( IOptionValue optionValue : values )
			{
				if ( optionValue != null )
				{
					renderOption.setOption( optionValue.getName( ), optionValue
							.getValue( ) );
				}
			}

			return renderOption;
		}
	}
}
