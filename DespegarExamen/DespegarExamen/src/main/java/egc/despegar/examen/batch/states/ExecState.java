/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egc.despegar.examen.batch.states;

import egc.despegar.examen.batch.services.TaskManagerService;
import egc.despegar.examen.batch.tasks.Task;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 *
 * @author Eduardo
 */
public class ExecState extends State {

    final static Logger logger = Logger.getLogger(ExecState.class);
    
    @Override
    public String getName() {
        return "Ejecutando";
    }  

    @Override
    public boolean isRunnable() {
        return true;
    }

    @Override
    public void run() {
        this.logger.info("Ejecutando Tarea Primos para "+input);
        setChanged();
        try {
           Random random = new Random();
           if (1 == random.ints(1, 6).findFirst().getAsInt()) {
               this.logger.info("Uno de cada Cinco que falla");
               Thread.sleep(10000);
               Exception exception = new Exception("Ocurrio un error inesperado");
               output = exception;
               throw exception;
           } else {
               output = isPrime((Integer) input);
               Thread.sleep(10000);
           }
        } catch (Exception e) {
            notifyObservers(Task.INTERNAL_EXECUTION_RESULT_ERROR);
        }
        notifyObservers(Task.INTERNAL_EXECUTION_RESULT_OK);
        this.logger.info("Tarea Primos ejecutada con exito");
    }    
    
    private boolean isPrime(Integer number) {
        int n = number;
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n%2==0) return false;
        
        for(int i=3;i*i<=n;i+=2) {
            if(n%i==0)
                return false;
        }
        return true;
    }
}
