package ca.ubc.cs.testcaseview10;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ASTVisitorImpl extends ASTVisitor {
	
	CompilationUnit cu;
	TestInformation localTestInformation = new TestInformation();	
	TestInformation globalTestInformation = null;
	
	ASTVisitorImpl(CompilationUnit cu, TestInformation testinfo) {
		this.cu = cu;
		this.globalTestInformation = testinfo;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		// TODO Auto-generated method stub
		globalTestInformation.methodDList.add(node.getName().toString());
		localTestInformation.methodDList.add(node.getName().toString());
		
		return super.visit(node);
	}

	
	boolean assertFlag = false;
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodInvocation)
	 */
	@Override
	public boolean visit(MethodInvocation node) {
		// TODO Auto-generated method stub
		if (assertFlag || node.getName().toString().startsWith("assert")) {
			globalTestInformation.methodAList.add(node.getName().toString());
			globalTestInformation.methodAintsList.add(node.getStartPosition());
			globalTestInformation.methodAintlList.add(node.getLength());

			localTestInformation.methodAList.add(node.getName().toString());
			localTestInformation.methodAintsList.add(node.getStartPosition());
			localTestInformation.methodAintlList.add(node.getLength());

			assertFlag = true;
		} else {
			globalTestInformation.methodIList.add(node.getName().toString());
			globalTestInformation.methodIintsList.add(node.getStartPosition());
			globalTestInformation.methodIintlList.add(node.getLength());

			localTestInformation.methodIList.add(node.getName().toString());			
			localTestInformation.methodIintsList.add(node.getStartPosition());
			localTestInformation.methodIintlList.add(node.getLength());
		}
		
		return super.visit(node);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MethodInvocation)
	 */
	@Override
	public void endVisit(MethodInvocation node) {
		// TODO Auto-generated method stub
		if (assertFlag || node.getName().toString().startsWith("assert")) {
			assertFlag = false;
		}
		super.endVisit(node);
	}
	
	
		
	
}
