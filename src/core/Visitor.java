/*******************************************************************************
* Copyright (c) 2010 Luaj.org. All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
******************************************************************************/
package core;

import java.util.List;

import core.Exp.VarExp;

abstract public class Visitor {
	
	public void visit(Trecho trecho) { 
		trecho.bloco.accept(this); 
	};
	
	public void visit(Bloco bloco) {
		visit(bloco.escopo);
		if ( bloco.comandos != null )
			for ( int i=0, n=bloco.comandos.size(); i<n; i++ )
				((Comando)bloco.comandos.get(i)).accept(this);
	};
	
	public void visit(Comando.Assign comando) {
		visitVars(comando.vars);
		visitExps(comando.exps);
	}

	public void visit(Exp.BinopExp exp) {
		exp.lhs.accept(this);
		exp.rhs.accept(this);
	}
	
	public void visit(Exp.Constante exp) {
	}
	
	public void visit(Exp.FieldExp exp) {
		exp.lhs.accept(this);
		visit(exp.name);
	}

	public void visit(Exp.NameExp exp) {
		visit(exp.name);
	}
	
	public void visit(Exp.ParensExp exp) {
		exp.exp.accept(this);
	}
	
	public void visit(Exp.UnopExp exp) {
		exp.rhs.accept(this);
	}
	
	public void visitVars(List<VarExp> vars) {
		if ( vars != null )
			for ( int i=0, n=vars.size(); i<n; i++ )
				((Exp.VarExp)vars.get(i)).accept(this);
	}
	
	public void visitExps(List<Exp> exps) {
		if ( exps != null )
			for ( int i=0, n=exps.size(); i<n; i++ )
				((Exp)exps.get(i)).accept(this);
	}
	
	public void visitNames(List<Nome> names) {
		if ( names != null )
			for ( int i=0, n=names.size(); i<n; i++ )
				visit((Nome) names.get(i));
	}
	
	public void visit(Nome name) {
	}
	
	public void visit(String name) {
	}
	
	public void visit(NameScope scope) {
	}
}
