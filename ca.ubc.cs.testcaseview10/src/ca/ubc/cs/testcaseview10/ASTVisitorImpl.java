package ca.ubc.cs.testcaseview10;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
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
		if(!globalTestInformation.isLock()) {
			globalTestInformation.getMethodDList().add(node.getName().toString());
		}
		localTestInformation.getMethodDList().add(node.getName().toString());
		
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
			if(!globalTestInformation.isLock()) {
				globalTestInformation.getMethodAList().add(node.toString());
				globalTestInformation.getMethodAintsList().add(node.getStartPosition());
				globalTestInformation.getMethodAintlList().add(node.getLength());
			}
			localTestInformation.getMethodAList().add(node.toString());
			localTestInformation.getMethodAintsList().add(node.getStartPosition());
			localTestInformation.getMethodAintlList().add(node.getLength());

			assertFlag = true;
		} else {
			if(!globalTestInformation.isLock()) {
				globalTestInformation.getMethodIList().add(node.toString());
				globalTestInformation.getMethodIintsList().add(node.getStartPosition());
				globalTestInformation.getMethodIintlList().add(node.getLength());
			}
			localTestInformation.getMethodIList().add(node.toString());			
			localTestInformation.getMethodIintsList().add(node.getStartPosition());
			localTestInformation.getMethodIintlList().add(node.getLength());
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

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.ClassInstanceCreation)
	 */
	@Override
	public void endVisit(ClassInstanceCreation node) {
		// TODO Auto-generated method stub
		super.endVisit(node);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.ClassInstanceCreation)
	 */
	@Override
	public boolean visit(ClassInstanceCreation node) {
		// TODO Auto-generated method stub
		if(!globalTestInformation.isLock()) {
			globalTestInformation.getMethodIList().add(node.toString());
			globalTestInformation.getMethodIintsList().add(node.getStartPosition());
			globalTestInformation.getMethodIintlList().add(node.getLength());
		}
		localTestInformation.getMethodIList().add(node.toString());			
		localTestInformation.getMethodIintsList().add(node.getStartPosition());
		localTestInformation.getMethodIintlList().add(node.getLength());
		return super.visit(node);
	}
	
}
