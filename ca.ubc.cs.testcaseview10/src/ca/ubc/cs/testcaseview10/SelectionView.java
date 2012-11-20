package ca.ubc.cs.testcaseview10;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

/**
 * This view simply mirrors the current selection in the workbench window.
 * It works for both, element and text selection.
 */
public class SelectionView extends ViewPart {

	private PageBook pagebook;
	private TableViewer tableviewer;
	private TextViewer textviewer;
	
	// the listener we register with the selection service 
	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			// we ignore our own selections
			if (sourcepart != SelectionView.this) {
			    showSelection(sourcepart, selection);
			}
		}
	};
	
	/**
	 * Shows the given selection in this view.
	 */
	private void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		setContentDescription(sourcepart.getTitle() + " (" + selection.getClass().getName() + ")");
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object firstElement = ss.getFirstElement();
			
			if (firstElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) firstElement;
				Object project = null;
				boolean showitemcalled = false;
				try {
					project = adaptable.getAdapter(IProject.class);
					if (project instanceof IProject) {
						showItems(printProjectInfo((IProject)project));
						showitemcalled = true;
					}
					
					project = adaptable.getAdapter(IPackageFragment.class);				
					if (project instanceof IPackageFragment) {
						showItems(getICompilationUnitInfo((IPackageFragment)project));
						showitemcalled = true;
					}
					
					project = adaptable.getAdapter(ICompilationUnit.class);								
					if (project instanceof ICompilationUnit) {
						showText(getOneICompilationUnitInfo((ICompilationUnit)project));
						showitemcalled = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (!showitemcalled) {
					showItems(ss.toArray());					
				}
			} else {
				showItems(ss.toArray());
			}
		}
		if (selection instanceof ITextSelection) {
			ITextSelection ts  = (ITextSelection) selection;
			showText(ts.getText());
		}
		if (selection instanceof IMarkSelection) {
			IMarkSelection ms = (IMarkSelection) selection;
			try {
			    showText(ms.getDocument().get(ms.getOffset(), ms.getLength()));
			} catch (BadLocationException ble) { }
		}
	}
	
	private void showItems(Object[] items) {
		tableviewer.setInput(items);
		pagebook.showPage(tableviewer.getControl());
	}
	
	private void showText(String text) {
		textviewer.setDocument(new Document(text));
		pagebook.showPage(textviewer.getControl());
	}
	
	public void createPartControl(Composite parent) {
		// the PageBook allows simple switching between two viewers
		pagebook = new PageBook(parent, SWT.NONE);
		
		tableviewer = new TableViewer(pagebook, SWT.NONE);
		tableviewer.setLabelProvider(new WorkbenchLabelProvider());
		tableviewer.setContentProvider(new ArrayContentProvider());
		
		// we're cooperative and also provide our selection
		// at least for the tableviewer
		getSite().setSelectionProvider(tableviewer);
		
		textviewer = new TextViewer(pagebook, SWT.H_SCROLL | SWT.V_SCROLL);
		textviewer.setEditable(false);
		
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
	}

	public void setFocus() {
		pagebook.setFocus();
	}

	public void dispose() {
		// important: We need do unregister our listener when the view is disposed
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}
	
	private Object[] printProjectInfo(IProject project) throws CoreException,
		JavaModelException {
		List<Object> str = new ArrayList<Object>();	
		str.add(project); 

		//System.out.println("Working in project " + project.getName());
		// Check if we have a Java project
		if (project.isOpen() && project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			return printPackageInfos(javaProject);
		} else {
			return str.toArray();
		}
	}

	private Object[] printPackageInfos(IJavaProject javaProject)
		throws JavaModelException {
		List<Object> str = new ArrayList<Object>();
		str.add(javaProject);
		
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			// Package fragments include all packages in the
			// classpath
			// We will only look at the package from the source
			// folder
			// K_BINARY would include also included JARS, e.g.
			// rt.jar
			//System.out.println("--------------------------------------------------------------------");
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				str.add(mypackage);
				//getICompilationUnitInfo(mypackage);
			}
		}

		//getMapInfo(this.opList, this.asList);
		return str.toArray();
	}
	
	private Object[] getICompilationUnitInfo(IPackageFragment mypackage) 
			throws JavaModelException {
		List<Object> str = new ArrayList<Object>();	

		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			// assert statement search
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(unit);
			CompilationUnit unitp = (CompilationUnit)parser.createAST(new NullProgressMonitor());
			str.add(unit);
			
			/*
			ASTVisitorImpl astvi = new ASTVisitorImpl();
			unitp.accept(astvi);
			astvi.printAllAfterPhrase(true, true, true, true);
			astvi.getInformation(this.opList, this.asList, this.assignList, this.oponlyList);
			*/
		}
		return str.toArray();
	}
	
	private String getOneICompilationUnitInfo(ICompilationUnit unit) 
			throws JavaModelException {

		// assert statement search
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(unit);
		CompilationUnit unitp = (CompilationUnit)parser.createAST(new NullProgressMonitor());
			
		/*
		ASTVisitorImpl astvi = new ASTVisitorImpl();
		unitp.accept(astvi);
		astvi.printAllAfterPhrase(true, true, true, true);
		astvi.getInformation(this.opList, this.asList, this.assignList, this.oponlyList);
		*/
		return unit.toString();
	}

}
