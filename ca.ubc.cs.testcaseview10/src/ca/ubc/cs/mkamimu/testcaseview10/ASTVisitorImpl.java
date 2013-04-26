package ca.ubc.cs.mkamimu.testcaseview10;

import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class ASTVisitorImpl extends ASTVisitor {
	
	CompilationUnit cu;
	TestInformation localTestInformation = new TestInformation();	
	TestInformation globalTestInformation = null;
	
	ASTVisitorImpl(CompilationUnit cu, TestInformation testinfo) {
		this.cu = cu;
		this.globalTestInformation = testinfo;
		if (!this.globalTestInformation.isLock()) {
			//this.globalTestInformation.setSourceFile(cu.toString());
		}
		localTestInformation.setClassName(cu.getJavaElement().getElementName());
		//System.out.println(cu.getJavaElement().getElementName());
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

	private boolean assertFlag = false;
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
				globalTestInformation.getMethodOnlyAList().add(node.getName().toString());
				globalTestInformation.getMethodAArgList().add(node.arguments().toString());
				globalTestInformation.getTryflag().add(tryflag);
				globalTestInformation.getCatchflag().add(catchflag);
				if (node.getExpression() != null) {
					globalTestInformation.getExprOnlyAList().add(node.getExpression().toString());
				} else {
					globalTestInformation.getExprOnlyAList().add("");				
				}
			}
			localTestInformation.getMethodADList().add(currentMethod.peek());				
			localTestInformation.getMethodAList().add(node.toString());
			localTestInformation.getMethodAintsList().add(node.getStartPosition());
			localTestInformation.getMethodAintlList().add(node.getLength());
			localTestInformation.getMethodOnlyAList().add(node.getName().toString());
			localTestInformation.getMethodAArgList().add(node.arguments().toString());
			localTestInformation.getTryflag().add(tryflag);
			localTestInformation.getCatchflag().add(catchflag);
			if (node.getExpression() != null) {
				localTestInformation.getExprOnlyAList().add(node.getExpression().toString());
			} else {
				localTestInformation.getExprOnlyAList().add("");				
			}

			/*
			System.out.println("localap:" + node.getParent().toString());
			System.out.println("locala:" + node.toString());
			System.out.println("localaa:" + node.arguments().toString());
			if (node.getExpression() != null) {
				System.out.println("localaex:" + node.getExpression().toString());
			} else {
				System.out.println("localaex:" + "no expression");
			}
			*/
			assertFlag = true;
		} else {
			if(!globalTestInformation.isLock()) {
				globalTestInformation.getMethodIDList().add(currentMethod.peek());				
				globalTestInformation.getMethodIList().add(node.toString());
				globalTestInformation.getMethodIintsList().add(node.getStartPosition());
				globalTestInformation.getMethodIintlList().add(node.getLength());
				globalTestInformation.getMethodOnlyIList().add(node.getName().toString());
				globalTestInformation.getMethodIArgList().add(node.arguments().toString());
				globalTestInformation.getTryflag().add(tryflag);
				globalTestInformation.getCatchflag().add(catchflag);
				if (node.getExpression() != null) {
					globalTestInformation.getExprOnlyIList().add(node.getExpression().toString());
				} else {
					globalTestInformation.getExprOnlyIList().add("");				
				}
			}
			localTestInformation.getMethodIDList().add(currentMethod.peek());				
			localTestInformation.getMethodIList().add(node.toString());			
			localTestInformation.getMethodIintsList().add(node.getStartPosition());
			localTestInformation.getMethodIintlList().add(node.getLength());
			localTestInformation.getMethodOnlyIList().add(node.getName().toString());
			localTestInformation.getMethodIArgList().add(node.arguments().toString());
			localTestInformation.getTryflag().add(tryflag);
			localTestInformation.getCatchflag().add(catchflag);
			if (node.getExpression() != null) {
				localTestInformation.getExprOnlyIList().add(node.getExpression().toString());
			} else {
				localTestInformation.getExprOnlyIList().add("");				
			}

			/*
			System.out.println("localip:" + node.getParent().toString());
			System.out.println("locali:" + node.toString());
			System.out.println("localia:" + node.arguments().toString());
			if (node.getExpression() != null) {
				System.out.println("localiex:" + node.getExpression().toString());
			} else {
				System.out.println("localiex:" + "no expression");
			}
			*/

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
			globalTestInformation.getMethodOnlyIList().add(node.getType().toString());
			globalTestInformation.getMethodIArgList().add(node.arguments().toString());
			globalTestInformation.getTryflag().add(tryflag);
			globalTestInformation.getCatchflag().add(catchflag);
			if (node.getExpression() != null) {
				globalTestInformation.getExprOnlyIList().add(node.getExpression().toString());
			} else {
				globalTestInformation.getExprOnlyIList().add("");				
			}
		}
		localTestInformation.getMethodIDList().add(currentMethod.peek());
		localTestInformation.getMethodIList().add(node.toString());			
		localTestInformation.getMethodIintsList().add(node.getStartPosition());
		localTestInformation.getMethodIintlList().add(node.getLength());
		localTestInformation.getMethodOnlyIList().add(node.getType().toString());
		localTestInformation.getMethodIArgList().add(node.arguments().toString());
		localTestInformation.getTryflag().add(tryflag);
		localTestInformation.getCatchflag().add(catchflag);
		if (node.getExpression() != null) {
			localTestInformation.getExprOnlyIList().add(node.getExpression().toString());
		} else {
			localTestInformation.getExprOnlyIList().add("");				
		}

		return super.visit(node);
	}
	
	
	private boolean variableDeclarationFragFlag = false;
	private boolean isAssignFlag = true;
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
	 */
	public boolean visit(VariableDeclarationFragment node) {
		if (node.getName() != null && node.getInitializer() != null) {
			AssignInformation asninfo = new AssignInformation();
			asninfo.setTestinfo(this.currentMethod.peek());
			asninfo.setAssignInfo(
					node.getName().toString(), node.getInitializer().toString(),
					node.getStartPosition(), node.getLength()
					);
			this.localTestInformation.addAssigninfo(asninfo);

			/*
			System.out.print(this.currentMethod.peek() + ":");
			System.out.print(node.getName().toString());
			System.out.print(":assign:");
			System.out.println(node.getInitializer().toString());
			*/
		}
		variableDeclarationFragFlag = true;
		return super.visit(node);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.VariableDeclarationFragment)
	 */
	public void endVisit(VariableDeclarationFragment node) {
		variableDeclarationFragFlag = false;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.Assignment)
	 */
	public boolean visit(Assignment node) {
		isAssignFlag = true;

		if (node.getLeftHandSide().toString() != null && node.getRightHandSide().toString() != null) {
			AssignInformation asninfo = new AssignInformation();
			asninfo.setTestinfo(this.currentMethod.peek());
			asninfo.setAssignInfo(
					node.getLeftHandSide().toString(), node.getRightHandSide().toString(),
					node.getStartPosition(), node.getLength()
					);
			this.localTestInformation.addAssigninfo(asninfo);
			
			/*
			System.out.print(this.currentMethod.peek() + ":");
			System.out.print(node.getLeftHandSide().toString());
			System.out.print(":leftright:");
			System.out.println(node.getRightHandSide().toString());
			*/
		}
		return super.visit(node);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.Assignment)
	 */
	public void endVisit(Assignment node) {
		isAssignFlag = false;
	}
	
	private boolean tryflag = false;
	private boolean catchflag = false;
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.TryStatement)
	 */
	public boolean visit(TryStatement node) {
		tryflag = true;
		return super.visit(node);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.TryStatement)
	 */
	public void endVisit(TryStatement node) {
		tryflag = false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.CatchClause)
	 */
	public boolean visit(CatchClause node) {
		catchflag = true;
		return super.visit(node);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom.CatchClause)
	 */
	public void endVisit(CatchClause node) {
		catchflag = false;
	}

	/**
	 * @return
	 */
	public boolean isTryflag() {
		return tryflag;
	}

	/**
	 * @return
	 */
	public boolean isCatchflag() {
		return catchflag;
	}
	
	
}
