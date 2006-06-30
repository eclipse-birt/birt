
package org.eclipse.birt.report.service.actionhandler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

public abstract class AbstractExtractResultSetActionHandler
		extends
			AbstractBaseActionHandler
{

	public AbstractExtractResultSetActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws Exception
	{
		BaseAttributeBean attrBean = (BaseAttributeBean) context.getBean( );

		String docName = attrBean.getReportDocumentName( );
		String resultSetName = ParameterAccessor.getResultSetName( context
				.getRequest( ) );
		Collection columns = ParameterAccessor.getSelectedColumns( context
				.getRequest( ) );
		Set colSet = new HashSet( );
		colSet.addAll( columns );
		Set filters = Collections.EMPTY_SET;
		Locale locale = attrBean.getLocale( );
		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		options.setOption( InputOptions.OPT_LOCALE, locale );
		options.setOption( InputOptions.OPT_RTL,
				new Boolean( attrBean.isRtl( ) ) );
		
		ServletOutputStream out = context.getResponse( ).getOutputStream( );
		getReportService( ).extractResultSet( docName, resultSetName,
				colSet, filters, options, out );
	}
}
