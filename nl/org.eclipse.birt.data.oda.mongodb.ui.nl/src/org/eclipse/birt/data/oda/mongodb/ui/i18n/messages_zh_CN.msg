##************************************************************************
# Copyright (c) 2013 Actuate Corporation.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#  Actuate Corporation - initial API and implementation
#  
#*************************************************************************
#
MongoDBDataSourceWizardPage.message.default=Mongo ���ݿ�����Դ��������
MongoDBDataSourceWizardPage.GroupTitle.URIElements=Mongo ���ݿ� URI Ԫ��
MongoDBDataSourceWizardPage.text.label.ServerHost=����������(&H):
MongoDBDataSourceWizardPage.text.label.ServerPort=�������˿�(&O):
MongoDBDataSourceWizardPage.text.label.DatabaseName=���ݿ�����(&D):
MongoDBDataSourceWizardPage.text.label.UserName=�û���(&S):
MongoDBDataSourceWizardPage.text.label.Password=����(&W):
MongoDBDataSourceWizardPage.RadioButton.label.DatabaseURI=Mongo ���ݿ� URI(&M):
MongoDBDataSourceWizardPage.GroupTitle.SupplementalSetting=����ͻ�������(&C)
MongoDBDataSourceWizardPage.checkbox.label.SocketAlive=ʹ�׽��ֱ��ֻ�Ծ״̬(&A)
MongoDBDataSourceWizardPage.checkbox.label.RequestSession=һ�µ�����Ự(&R)

MongoDBDataSourceWizardPage.error.MissingConnectionProperty=ȱ�ٱ�����������ԣ�

#
MongoDBDataSetWizardPage.label.DBName=Mongo ���ݿ�(&G):
MongoDBDataSetWizardPage.label.DBCollection=MongoDB ����(&C)
MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection=����ϵͳ����(&I)
MongoDBDataSetWizardPage.Button.Refresh=ˢ��(&R)
MongoDBDataSetWizardPage.label.DocumentNumber=�ĵ�����������ֶ�(&M):
MongoDBDataSetWizardPage.group.text=�������(&O)
MongoDBDataSetWizardPage.label.OperationType=��������(&T):
MongoDBDataSetWizardPage.Button.FindFields=�����ֶ�(&D)
MongoDBDataSetWizardPage.Button.text.CommandExpression=����ʽ(&E)...
MongoDBDataSetWizardPage.Button.tooltip.CommandExpression=�༭�������ʽ��
MongoDBDataSetWizardPage.Button.QueryExpression=$��ѯ����ʽ(&Q)...
MongoDBDataSetWizardPage.Button.SortExpression=�������ʽ(&S)...
MongoDBDataSetWizardPage.Button.AdvancedSettings=�߼�����(&V)...
MongoDBDataSetWizardPage.label.AvailableFields=�����ֶ�(&A):
MongoDBDataSetWizardPage.label.SelectedFields=ѡ���ֶ�(&S):
MongoDBDataSetWizardPage.HeaderText.SelectedFields=�ֶ���ʾ����
MongoDBDataSetWizardPage.menuItem.remove=ɾ��(&M)
MongoDBDataSetWizardPage.menuItem.removeAll=ȫ��ɾ��(&L)

MongoDBDataSetWizardPage.message.default=ѡ�����ݼ��е������ֶ�
MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections=ȷ���Լ���
MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections=ѡ���������ϻ������ǰ��ѡ���ֶΡ���  �Ƿ�Ҫ����?
MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields=ȷ����ˢ��
MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields=�Ƿ�Ҫ���ݸ��ĵġ��������ʽ��ˢ�¡������ֶΡ�?

MongoDBDataSetWizardPage.MessageDialog.title.GeneralError=����
MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage=�޷��� MongoDB ����Դ��ȡ�����б���

MongoDBDataSetWizardPage.ExceptionDialog.title=����
MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields=�Ҳ��������ֶΡ�
MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData=�޻����Դ���ӡ�
MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage=���� MongoDB ���ݼ�ʱ������
MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression=�������ʽ��Ч��
MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression=��ѯ����ʽ��Ч��
MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression=�������ʽ��Ч��

MongoDBDataSetWizardPage.message.error.MissingCollectionName=�������Ʋ���Ϊ�ա�
MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber=Ϊ�ĵ�������һ������ֵ�Բ����ֶ�
MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound=ѡ�����ֶΡ�{0}�������ڡ�
MongoDBDataSetWizardPage.message.error.MissingCommandExpression=�������ʽ����Ϊ�ա�
MongoDBDataSetWizardPage.message.error.MissingSelectedField=����ѡ������һ���ֶΡ�

MongoDBAdvancedSettingsDialog.dialogTitle=�߼����ݼ�����ʱ����
MongoDBAdvancedSettingsDialog.Label.MaxResultObjects=һ�������Ľ��������(&B): 
MongoDBAdvancedSettingsDialog.Tooltip.MaxResultObjects=һ�������� MongoDB cursor ��ȡ\n���������������  Ĭ��ֵ = 100
MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSearch=Ҫ����Ԫ���ݵ�����ĵ���(&M):
MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSearch=������ʱ��ȡ��ѯ�����Ԫ����\nҪ�������ĵ�����  Ĭ��ֵ = 10
MongoDBAdvancedSettingsDialog.Label.MaxDocumentsToSkip=Ҫ����������ĵ���(&K):
MongoDBAdvancedSettingsDialog.Tooltip.MaxDocumentsToSkip=����������е�ǰ n ���ĵ��������������͡�\nĬ��ֵ = 0
MongoDBAdvancedSettingsDialog.Label.IndexHintExpression=������ʾ����ʽ(&H):
MongoDBAdvancedSettingsDialog.Tooltip.IndexHintExpression=����������е�һ����Ҫ��ò�ѯ�������ڲ�ѯ�Ż���\nʹ�� Mongo JSON ��ʾ����ʽ�﷨�г����������ļ���
MongoDBAdvancedSettingsDialog.Group.text.QueryCursorPreference=��ѯָ���ȡ��ѡ��
MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.Mode=ģʽ(&D):
MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.Mode=��ѯָ������Ķ�ȡ��ѡ��ģʽ��
MongoDBAdvancedSettingsDialog.Label.QueryCursorPreference.TagSet=��Ǽ�(&G):
MongoDBAdvancedSettingsDialog.Tooltip.QueryCursorPreference.TagSet=���ó�Ա�ڵ�����ı�Ǽ��ϡ�\n�����Ǽ�����ָ��Ϊ�����е��ĵ���
MongoDBAdvancedSettingsDialog.Button.text.NoCursorTimeOut=ָ��δ��ʱ(&N) 
MongoDBAdvancedSettingsDialog.Button.tooltip.NoCursorTimeOut=ͨ������ָ����һ��ʱ���ڣ�10 ���ӣ����ʱ����������������ָ������ʱ�������Է�ռ�ù����ڴ档\n���ô�ѡ��ɱ����������� 
MongoDBAdvancedSettingsDialog.Button.text.AllowPartialResults=�������ֽ��(&P)
MongoDBAdvancedSettingsDialog.Button.tooltip.AllowPartialResults=���Ƭ���� (mongos) һ��ʹ��ʱ������κη�ƬʧЧ��Ⱥ����ʧ�κη�Ƭ��\n��������Ƭϵͳ�������ֽ����  ���δ���ã���� mongos ����������һ������ 
MongoDBAdvancedSettingsDialog.Button.text.FlattenNestedDocuments=���л�Ƕ�׼���(&F)
MongoDBAdvancedSettingsDialog.Button.tooltip.FlattenNestedDocuments=���ڽ�Ƕ���ֶμ��ϴ��л��ɶ�����ݼ��е��û�ѡ� 
MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxResultObjects=�����������������Ҫһ������ֵ!
MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSearch=��Ҫ����������ĵ�������Ҫһ������ֵ!
MongoDBAdvancedSettingsDialog.error.NotNumberFormat.MaxDocumentsToSkip=��Ҫ����������ĵ�������Ҫһ������ֵ!
MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title=Reset Design Setting
MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button=OK
MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message=The design-time value of "Number of Documents to search for fields" is reset to match the "Maximum documents to search for metadata" run-time property value.

MongoDBExpressionBuilder.dialogTitle.default=����ʽ������
MongoDBExpressionBuilder.ExprssionText.label=����ʽ�ı�(&X)��
MongoDBExpressionBuilder.Button.import=����(&M)...
MongoDBExpressionBuilder.Button.tooltip.import=�ӱ����ļ��������ʽ�ı�
MongoDBExpressionBuilder.Button.export=����(&E)...
MongoDBExpressionBuilder.Button.tooltip.export=������ʽ�ı������������ļ�
MongoDBExpressionBuilder.Button.ValidateSyntax=��֤�﷨(&V)
MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax=��֤����ʽ�ı����﷨��
MongoDBExpressionBuilder.InfoDialog.title.validateSyntax=����ʽ�ı���֤
MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid=����ʽ�﷨��Ч��
MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid=����ʽ�﷨��Ч��
MongoDBExpressionBuilder.CommandExpression.DialogTitle=�������ʽ������
MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression=����ʽ�ı�����Ϊ��!
MongoDBExpressionBuilder.QueryExpression.DialogTitle=��ѯ����ʽ������
MongoDBExpressionBuilder.SortExpression.DialogTitle=�������ʽ������




