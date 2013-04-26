package ca.ubc.cs.mkamimu.testcaseview10.decorator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

import ca.ubc.cs.mkamimu.testcaseview10.SelectionView;


public class Decorator implements ILightweightLabelDecorator{

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	SelectionView sv = new SelectionView();

	@Override
	public void decorate(Object element, IDecoration decoration) {
		// TODO Auto-generated method stub
		//decoration.addOverlay(ImageDescriptor.createFromFile(Decorator.class, 
		//		"/icons/sample.gif"), IDecoration.TOP_RIGHT);
		//decoration.addPrefix("My Prefix ");
		//decoration.addSuffix(" My Suffix");
		if (element instanceof IProject) {
			if (((IProject) element).isOpen()) {
				RGB rgb = new RGB(255, 200, 200);
				Color color = new Color(PlatformUI.getWorkbench().getDisplay(), rgb);
				decoration.setBackgroundColor(color);
			}
		}
		if (element instanceof IFile) {
			IFile file = (IFile)element;
			try {
				ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
				if (unit != null) {					
					RGB rgb = null;
					//sv.getOneMethodICompilationUnitInfo(unit, false);
					int count = sv.largest();
					if (count == 0) {
						rgb = new RGB(255, 255, 0);
					} else if (count == 1) {
						rgb = new RGB(255, 255, 200);						
					} else if (count == 2) {
						rgb = new RGB(0,255,0);
						/*Font font = new Font(PlatformUI.getWorkbench().getDisplay(),new FontData("Arial", 10, SWT.BOLD));
						decoration.setFont(font);*/
					} else {
						rgb = new RGB(200, 255, 200);												
					}
					Color color = new Color(PlatformUI.getWorkbench().getDisplay(), rgb);
					decoration.setBackgroundColor(color);
					
					//file.findMarkers(IM, includeSubtypes, depth);
					// set for next survey.
					sv.setCurrentProject(unit.getJavaProject().getElementName());
				}
			} catch (IllegalArgumentException e) {
				// ignore
			} catch (ClassCastException e) {
				// ignore
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		/*
		IStructuredSelection selection = 
				  (IStructuredSelection) viewer.getSelection();
				Person p = (Person) selection.getFirstElement();
		*/
	}
}
