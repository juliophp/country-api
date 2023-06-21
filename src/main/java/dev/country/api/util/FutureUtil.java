package dev.country.api.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;


@Component
public class FutureUtil {

    private final ExecutorService executorService;

    public FutureUtil(final ExecutorService executorService){
        this.executorService = executorService;
    }
    public <T> T getResult(Future<T> fu)
    {
        T obj = null;
        try
        {
            obj = fu.get();
        }
        catch (InterruptedException  | ExecutionException e )
        {
            e.printStackTrace();
        }
        return obj;
    }


    public <T> List<Future<T>> invokeMultipleTask(List<Callable<T>> callableList){
        try {
            return executorService.invokeAll(callableList);
        }
        catch (InterruptedException | CancellationException e) {
            throw new RuntimeException(e);
        }
    }
    public <T> Future<T> invokeSingleTask(Callable<T> callable){
        try {
            return executorService.submit(callable);
        }
        catch (CancellationException e) {
            throw new RuntimeException(e);
        }
    }
}
