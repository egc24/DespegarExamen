/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egc.despegar.examen.batch.tasks;

import egc.despegar.examen.batch.states.State;
import egc.despegar.examen.batch.states.ExecState;
import egc.despegar.examen.batch.states.FailedState;
import egc.despegar.examen.batch.states.FinishState;
import egc.despegar.examen.batch.states.WaitState;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Eduardo
 */
public class Task implements Observer {
    final static Logger logger = Logger.getLogger(Task.class);
    
    public static final Integer INTERNAL_EXECUTION_RESULT_OK = 1;
    public static final Integer INTERNAL_EXECUTION_RESULT_ERROR = 2;
    public static final Integer EXTERNAL_EXECUTION_START = 3;
    
    protected ThreadPoolTaskExecutor threadPool;
    protected HashMap<String, HashMap<Integer, State>> transitions = new HashMap<String, HashMap<Integer, State>>();
    protected String name;
    protected State state;
    
    public Task(String name, ThreadPoolTaskExecutor threadPool) {
        this.logger.info("Generando la nueva tarea "+name);
        this.name = name;
        this.threadPool = threadPool;
        this.state = new WaitState();
        
        this.logger.info("Generando las transiciones de estados para la tarea "+name);
        WaitState waitState = new WaitState();
        waitState.addObserver(this);
        ExecState execState = new ExecState();
        execState.addObserver(this);
        FailedState faileState = new FailedState();
        faileState.addObserver(this);
        FinishState finishState = new FinishState();        
        finishState.addObserver(this);
        
        HashMap<Integer, State> waitStateTransitions = new HashMap<Integer, State>(); 
        waitStateTransitions.put(EXTERNAL_EXECUTION_START, execState);
        transitions.put(waitState.getName(),waitStateTransitions);
        
        HashMap<Integer, State> execStateTransitions = new HashMap<Integer, State>();
        execStateTransitions.put(INTERNAL_EXECUTION_RESULT_OK, finishState);
        execStateTransitions.put(INTERNAL_EXECUTION_RESULT_ERROR, faileState);
        transitions.put(execState.getName(),execStateTransitions);
        
        HashMap<Integer, State> failedStateTransitions = new HashMap<Integer, State>();
        failedStateTransitions.put(EXTERNAL_EXECUTION_START, waitState);
        transitions.put(faileState.getName(),failedStateTransitions);
        
        HashMap<Integer, State> finishStateTransitions = new HashMap<Integer, State>();
        finishStateTransitions.put(EXTERNAL_EXECUTION_START, waitState);
        transitions.put(finishState.getName(),finishStateTransitions); 
        this.logger.info("Tarea "+name+" generada con exito");
    }

    public String getName() {
        return name;
    }
    
    public State getState() {
        return state;
    }
    
    public void update(Object externalInput,Integer externalTransition) {
        state.setInput(externalInput);
        updateState((State) state, externalTransition);
    }

    public void update(Integer externalTransition) {
        updateState((State) state, externalTransition);
    }    
    
    
    @Override
    public void update(Observable executedState, Object internalTransition) {
        updateState((State) state, (Integer) internalTransition);
    }
    
    protected void updateState(State currentState, Integer transition) {
        state = transitions.get(currentState.getName()).get(transition);
        this.logger.info("Tarea "+name+", transicion efectuada de "+currentState.getName()+" a "+state.getName()+
                " a traves de "+transition+" con resultado del estado anterior de "+
                currentState.getOutput());        
        
        state.setInput(currentState.getOutput());
        if (state.isRunnable()) {
            this.logger.info("Tarea "+name+", comienza la ejecucion del estado "+state.getName());
            threadPool.execute(state);
        }
    }
    
}
