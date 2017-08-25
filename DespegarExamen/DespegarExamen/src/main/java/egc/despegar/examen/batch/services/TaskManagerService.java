/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egc.despegar.examen.batch.services;

import egc.despegar.examen.batch.states.State;
import egc.despegar.examen.batch.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author Eduardo
 */
public class TaskManagerService {
    
    protected HashMap<String, Task> tasks = new HashMap<String, Task>();
    protected ThreadPoolTaskExecutor threadPool;
    final static Logger logger = Logger.getLogger(TaskManagerService.class);
    
    public void init() {
        logger.info("Generando TaskManager Service");
        addTask("Ejemplo 1");
        addTask("Ejemplo 2");
        addTask("Ejemplo 3");
        addTask("Ejemplo 4");
        addTask("Ejemplo 5");
        logger.info("TaskManager Service Generado");
    }
    
    public void destroy() {
        logger.info("Destruyendo TaskManager Service");
        while (true) {
            if (threadPool.getActiveCount() == 0) {
                    logger.info("Cerrando Pool de conexiones");
                    threadPool.shutdown();
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

	}
        logger.info("Destruyendo TaskManager Destruido");
    }
    
    public String addTask(String taskName) {
        logger.info("Agregando nueva Tarea "+taskName);
        Task task = new Task(taskName, threadPool);
        tasks.put(taskName, task);
        logger.info("Nueva Tarea agregada "+taskName);
        return taskName;
    }
    
    public State getTaskState(String taksName) {
        return tasks.get(taksName).getState();
    }
    
    public void execute(Object externalInput, String taskName) {
        logger.info("Ejecutando Tarea "+taskName);
        Task task = tasks.get(taskName);
        task.update(externalInput, Task.EXTERNAL_EXECUTION_START);
        logger.info("Tarea "+taskName+" Ejecutada");
    }
    
    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<String, Task> tasks) {
        this.tasks = tasks;
    }

    public ThreadPoolTaskExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolTaskExecutor threadPool) {
        this.threadPool = threadPool;
    }
    
    public List<String> getCurrentTasksNames() {
        List tasksNames = new ArrayList<String>();
        tasksNames.addAll(tasks.keySet());
        return tasksNames;
    }
    
    public int tasksActiveCount() {
        return threadPool.getActiveCount();
    }
}
