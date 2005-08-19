   echo on
   setlocal
   cd %~dp0
   start javaw -cp startup.jar org.eclipse.core.launcher.Main -application org.eclipse.birt.report.designer.ui.rcp.DesignerApplication %*
   endlocal