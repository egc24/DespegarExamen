/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egc.despegar.examen.batch.states;

/**
 *
 * @author Eduardo
 */
public class FinishState extends State{
    
    @Override
    public String getName() {
        return "Finalizada";
    }      

    @Override
    public boolean isRunnable() {
        return false;
    }

    @Override
    public void run() {
    }  
}
