package ca.ubc.cs.testcaseview10;

import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ASTVisitorImpl extends ASTVisitor {
	
	CompilationUnit cu;
	TestInformation localTestInformation = new TestInformation();	
	TestInformation globalTestInformation = null;
	
	ASTVisitorImpl(CompilationUnit cu, TestInformation testinfo) {
		this.cu = cu;
		this.globalTestInformation = testinfo;
		if (!this.globalTestInformation.isLock()) {
			this.globalTestInformation.setSourceFile(cu.toString());
		}
	}

	Stack<String> currentMethod = new Stack<String>();
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TypeDeclaration)
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		// TODO Auto-generated method stub
		currentMethod.push(node.getName().toString());
		if(!globalTestInformation.isLock()) {
			globalTestInformation.getMethodDList().add(node.getName().toString());
		}
		localTestInformation.getMethodDList().add(node.getName().toString());

		return super.visit(node);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TypeDeclaration)
	 */
	@Override
	public void endVisit(TypeDeclaration node) {
		// TODO Auto-generated method stub
		if (node.getName().equals(currentMethod.peek())) {
			currentMethod.pop();
		}

		super.endVisit(node);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		// TODO Auto-generated method stub
		currentMethod.push(node.getName().toString());
		if(!globalTestInformation.isLock()) {
			globalTestInformation.getMethodDList().add(node.getName().toString());
		}
		localTestInformation.getMethodDList().add(node.getName().toString());
		return super.visit(node);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public void endVisit(MethodDeclaration node) {
		// TODO Auto-generated method stub
		if (node.getName().equals(currentMethod.peek())) {
			currentMethod.pop();
		}
		super.endVisit(node);
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
				globalTestInformation.getMethodADList().add(currentMethod.peek());				
				globalTestInformation.getMethodAList().add(node.toString());
				globalTestInformation.getMethodAintsList().add(node.getStartPosition());
				globalTestInformation.getMethodAintlList().add(node.getLength());
			}
			localTestInformation.getMethodADList().add(currentMethod.peek());				
			localTestInformation.getMethodAList().add(node.toString());
			localTestInformation.getMethodAintsList().add(node.getStartPosition());
			localTestInformation.getMethodAintlList().add(node.getLength());

			assertFlag = true;
		} else {
			if(!globalTestInformation.isLock()) {
				globalTestInformation.getMethodIDList().add(currentMethod.peek());				
				globalTestInformation.getMethodIList().add(node.toString());
				globalTestInformation.getMethodIintsList().add(node.getStartPosition());
				globalTestInformation.getMethodIintlList().add(node.getLength());
			}
			localTestInformation.getMethodIDList().add(currentMethod.peek());				
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
			globalTestInformation.getMethodIDList().add(currentMethod.peek());				
			globalTestInformation.getMethodIList().add(node.toString());
			globalTestInformation.getMethodIintsList().add(node.getStartPosition());
			globalTestInformation.getMethodIintlList().add(node.getLength());
		}
		localTestInformation.getMethodIDList().add(currentMethod.peek());
		localTestInformation.getMethodIList().add(node.toString());			
		localTestInformation.getMethodIintsList().add(node.getStartPosition());
		localTestInformation.getMethodIintlList().add(node.getLength());
		return super.visit(node);
	}
	
}
