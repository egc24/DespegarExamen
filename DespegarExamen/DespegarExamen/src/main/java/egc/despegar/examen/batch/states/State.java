/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egc.despegar.examen.batch.states;

import java.util.Observable;

/**
 *
 * @author Eduardo
 */
public abstract class State extends Observable implements Runnable{
    
    protected Object input;
    protected Object output;
    
    public abstract String getName();
    
    public abstract boolean isRunnable();

    public void setInput(Object input) {
        this.input = input;
        this.output = this.input;
    }

    public Object getOutput() {
        return output;
    }
}
