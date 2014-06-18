package memory;

import core.LuaValor;

public class ContextoCompilacaoImperativa extends ContextoCompilacao implements AmbienteCompilacaoImperativa {

    private ListaValor entrada;

    public ContextoCompilacaoImperativa(ListaValor entrada){
        super();
        this.entrada = entrada;        
    }

    public LuaValor getEntrada() throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException,
    		EntradaVaziaException {
        if(entrada == null || entrada.getHead() == null) {
            throw new EntradaVaziaException();
        }
        LuaValor aux = entrada.getHead();
        entrada = (ListaValor)entrada.getTail();
        return aux;            
    }
}

