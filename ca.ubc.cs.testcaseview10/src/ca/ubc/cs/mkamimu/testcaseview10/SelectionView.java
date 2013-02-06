package ca.ubc.cs.mkamimu.testcaseview10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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
import org.eclipse.jdt.internal.core.CompilationUnitElementInfo;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

import ca.ubc.cs.mkamimu.testcaseview10.marker.SampleMarker;
import ca.ubc.cs.mkamimu.testcaseview10.marker.SampleMarker10;
import ca.ubc.cs.mkamimu.testcaseview10.marker.SampleMarker2;
import ca.ubc.cs.mkamimu.testcaseview10.marker.SampleMarker20;
import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.hapax.index.LogLikelihood;

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
						showText(getICompilationUnitInfo((IPackageFragment)project));
						showitemcalled = true;
					}
					
					project = adaptable.getAdapter(ICompilationUnit.class);								
					if (project instanceof ICompilationUnit) {
						showText(getOneMethodICompilationUnitInfo((ICompilationUnit)project, false));
						showitemcalled = true;
					}
					//project = adaptable.getAdapter(IClass)
					
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
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				    .getActivePage();
			IEditorInput editorInput = page.getActiveEditor().getEditorInput();
			IFile file = (IFile) editorInput.getAdapter(IFile.class);
			ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
			try {
				showText(getOneMethodICompilationUnitInfo(unit, false));
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			ITextSelection ts  = (ITextSelection) selection;
			showText(ts.getText());
			*/
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
	
	private String getICompilationUnitInfo(IPackageFragment mypackage) 
			throws JavaModelException {
		List<String> str = new ArrayList<String>();	

		StringBuffer compstr = new StringBuffer();
		for(ICompilationUnit mycompunit: mypackage.getCompilationUnits() ) {
			compstr.append(mycompunit.getElementName());
			compstr.append(" ");
		}
		//str.add(mypackage.getElementName());
		//str.add(compstr.toString());
		//str.add("\n");
		str.add(shouldComputeLogLikelihood(getAllIPackageInfo(mypackage.getJavaProject()), compstr.toString()));

		return str.toString();
	}
	
	private void callshouldComputeLogLikelihood(
			List<String> str,
			List<String> gmethodlist, String localfile) {
		StringBuffer strbufgd = new StringBuffer();
		//StringBuffer strbufgl = new StringBuffer();

		for (String each: gmethodlist) {
			strbufgd.append(each);
			strbufgd.append(" ");
		}

		//str.add(strbufgd.toString());
		//str.add(strbufgl.toString());
		
		str.add(shouldComputeLogLikelihood(strbufgd.toString(), localfile.toString()));
		return;
	}
	
	int[] a1 = {0,0,0,0};
	
	public int largest() {
		int current, max, count;
		
		current = a1[0];
		max = current;
		count = -1;
		for(int i = 0; i < a1.length; i++) {
			current = a1[i];
			if(current > max) {
				max = current;
				count = i;
			}
		}
		return count;
	}
	
	
	public String getOneMethodICompilationUnitInfo(ICompilationUnit unit, boolean loglikelihood) 
			throws JavaModelException {
		getAllMethodICompliationUnitInfo(unit.getJavaProject());
		List<String> str = new ArrayList<String>();	

		// assert statement search
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(unit);
		
		CompilationUnit unitp = (CompilationUnit)parser.createAST(new NullProgressMonitor());
		ASTVisitorImpl astvis = new ASTVisitorImpl(unitp, this.globalTestInformation);
		unitp.accept(astvis);
		
		if (loglikelihood) {
			//callshouldComputeLogLikelihood(str,astvis.globalTestInformation.getSourceFiles(), unitp.toString());
		}

		/*
		callshouldComputeLogLikelihood(str,astvis.globalTestInformation.getMethodDList(), astvis.localTestInformation.getMethodDList());
		str.add("Invocation\n");
		callshouldComputeLogLikelihood(str,astvis.globalTestInformation.getMethodIList(), astvis.localTestInformation.getMethodIList());
		str.add("Asserts\n");
		callshouldComputeLogLikelihood(str,astvis.globalTestInformation.getMethodAList(), astvis.localTestInformation.getMethodAList());
		*/
		
		IResource irs = ((ICompilationUnit) unit).getCorrespondingResource();
		a1[0] = SampleMarker.createMarker(irs, astvis.localTestInformation, globalTestInformation);
		a1[1] = SampleMarker10.createMarker(irs, astvis.localTestInformation, globalTestInformation);
		a1[2] = SampleMarker2.createMarker(irs, astvis.localTestInformation, globalTestInformation);
		a1[3] = SampleMarker20.createMarker(irs, astvis.localTestInformation, globalTestInformation);
		
		str.add(SampleMarker.getLastm());
		str.add(SampleMarker2.getLastm());
		
		return str.toString();
	}
	
	/*
	private String getOneICompilationUnitInfo(ICompilationUnit unit) 
			throws JavaModelException {
		List<Object> str = new ArrayList<Object>();	

		// assert statement search
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(unit);
		CompilationUnit unitp = (CompilationUnit)parser.createAST(new NullProgressMonitor());
		
		str.add(unitp);
		str.add("\n");
		str.add(shouldComputeLogLikelihood(getAllICompliationUnitInfo(unit.getJavaProject()), unitp.toString(),false));
			
		return str.toString();
	}
	*/

	
	private String getAllIPackageInfo(IJavaProject javaProject)
			throws JavaModelException {
		StringBuffer strlen = new StringBuffer();
		
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			for(ICompilationUnit mycompunit: mypackage.getCompilationUnits()) {
				strlen.append(mycompunit.getElementName());
			}
		}
		return strlen.toString();
	}
	
	String currentProject = null;
	TestInformation globalTestInformation = new TestInformation();

	private TestInformation getAllMethodICompliationUnitInfo(IJavaProject javaProject)
			throws JavaModelException {
		if (currentProject != null && currentProject.equals(javaProject.getElementName())) {
			return this.globalTestInformation;
		} else {
			currentProject = javaProject.getElementName();
			this.globalTestInformation.setLock(false);
			this.globalTestInformation.getMethodDList().clear();
			this.globalTestInformation.getMethodIList().clear();
			this.globalTestInformation.getMethodAList().clear();
			//this.globalTestInformation.getSourceFiles().clear();
		}
		
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
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					// assert statement search
					ASTParser parser = ASTParser.newParser(AST.JLS4);
					parser.setSource(unit);
					CompilationUnit unitp = (CompilationUnit)parser.createAST(new NullProgressMonitor());
					ASTVisitorImpl astvis = new ASTVisitorImpl(unitp, this.globalTestInformation);
					unitp.accept(astvis);
				}
			}
		}
		this.globalTestInformation.setLock(true);
		return this.globalTestInformation;
	}
	
	/*
	private String getAllICompliationUnitInfo(IJavaProject javaProject)
			throws JavaModelException {
		StringBuffer strlen = new StringBuffer();
		
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
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					// assert statement search
					ASTParser parser = ASTParser.newParser(AST.JLS4);
					parser.setSource(unit);
					CompilationUnit unitp = (CompilationUnit)parser.createAST(new NullProgressMonitor());
					strlen.append(unitp);
				}
			}
		}
		return strlen.toString();
	}
	*/
	
	public void shouldComputeLogLikelihood() {
		Terms all = new Terms("A A A A A B C C C D D");
		Terms doc = new Terms("A A B B X");
		for (String each: doc.elements()) {
			LogLikelihood loglr = new LogLikelihood(all, doc, each);
			System.out.println(loglr);
		}
	}
	
	public String shouldComputeLogLikelihood(String strall, String strdoc) {
		Terms all = new Terms(strall);
		Terms doc = new Terms(strdoc);
		StringBuffer strbuf = new StringBuffer();
		List<LogLikelihood> col = new ArrayList<LogLikelihood>();
		for (String each: doc.elements()) {
			LogLikelihood loglr = new LogLikelihood(doc, all, each);
			col.add(loglr);
		}
		Collections.sort(col);

		for (LogLikelihood logLikelihood : col) {
			//System.out.println(loglr);			
			strbuf.append(logLikelihood.toString() + "\n");
		}

		return strbuf.toString();
	}

	/**
	 * @return the currentProject
	 */
	public String getCurrentProject() {
		return currentProject;
	}

	/**
	 * @param currentProject the currentProject to set
	 */
	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
	}
	
}