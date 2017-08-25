package egc.despegar.examen.site.controllers;

import egc.despegar.examen.site.entities.TaskView;
import egc.despegar.examen.batch.services.TaskManagerService;
import egc.despegar.examen.batch.states.State;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;

@Named
@Scope("session")
public class TaskManager {

    private TaskManagerService taskManagerService;
    private List<TaskView> tasks = new ArrayList<TaskView>();
    private String input;
    private String newTask;
    
    public TaskManagerService getTaskManagerService() {
        return taskManagerService;
    }

    public void setTaskManagerService(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    public List<TaskView> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskView> tasks) {
        this.tasks = tasks;
    }
    
    @PostConstruct
    public void init(){
        List<String> currentTasks = taskManagerService.getCurrentTasksNames();
        
        for (String taskName : currentTasks) {
            tasks.add(new TaskView(taskName));    
        }
    }
    
    public void updateTasks() {
        for (TaskView task : tasks) {
            State taskState = taskManagerService.getTaskState(task.getName());
            task.setStatus(taskState.getName());
            Object taskOutput = taskState.getOutput();
            if (taskOutput != null && (taskState.getName().equals("Finalizada") || 
                    taskState.getName().equals("Fallida"))) {
                String result = taskOutput.toString();
                task.setResult((result.equals("true")?"Es Primo":(result.equals("false")?"No es Primo":result)));
            } else {
                task.setResult("");
            }
            task.setDisabled(((taskManagerService.tasksActiveCount() >= 3) || (taskState.getName().equals("Ejecutando")))?"false":"true");
        }
    }
        
    public void execute(TaskView task) {
        try {
            taskManagerService.execute(Integer.parseInt(input), task.getName());
        } catch (Exception e) {
        }
    }
    
    public void addNewTask() {
        taskManagerService.addTask(newTask);
        tasks.add(new TaskView(newTask));  
    }
    
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getNewTask() {
        return newTask;
    }

    public void setNewTask(String newTask) {
        this.newTask = newTask;
    }    
}
