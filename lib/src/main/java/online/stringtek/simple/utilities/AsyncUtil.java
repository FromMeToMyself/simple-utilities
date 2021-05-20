package online.stringtek.simple.utilities;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;

public class AsyncUtil {

    public static void asyncNonBlockingDo(Collection<FutureTask<?>> futureTaskGroup) {
        asyncNonBlockingDoCore(futureTaskGroup);
    }

    public static void asyncBlockingDo(Runnable... runnableGroup) {
        List<Callable<?>> callableList = Arrays.stream(runnableGroup).map(Executors::callable)
            .collect(Collectors.toList());
        asyncBlockingDoCore(callableList);
    }

    public static List<Pair<?, Exception>> asyncBlockingDo(Callable<?>... callableGroup) {
        return asyncBlockingDoCore(Arrays.asList(callableGroup));
    }

    public static List<Pair<?, Exception>> asyncBlockingDo(Collection<Callable<?>> callableGroup) {
        return asyncBlockingDoCore(callableGroup);
    }

    private static void asyncNonBlockingDoCore(Collection<FutureTask<?>> futureTaskGroup) {
        ExecutorService threadPool = newFixedThreadPoolExecutorWithoutBlockingQueue(
            futureTaskGroup.size());
        try {
            for (FutureTask<?> futureTask : futureTaskGroup) {
                threadPool.submit(futureTask);
            }
        } finally {
            threadPool.shutdownNow();
        }
    }


    private static List<Pair<?, Exception>> asyncBlockingDoCore(
        Collection<Callable<?>> callableGroup) {
        List<Pair<?, Exception>> result = new ArrayList<>();
        List<Future<?>> futureList = new ArrayList<>();
        ExecutorService threadPool = newFixedThreadPoolExecutorWithoutBlockingQueue(
            callableGroup.size());
        try {
            for (Callable<?> callable : callableGroup) {
                futureList.add(threadPool.submit(callable));
            }
            for (Future<?> future : futureList) {
                try {
                    Object obj = future.get();
                    result.add(Pair.create(obj, null));
                } catch (Exception e) {
                    result.add(Pair.create(null, e));
                }
            }
            return result;
        } finally {
            threadPool.shutdownNow();
        }
    }

    private static ExecutorService newFixedThreadPoolExecutorWithoutBlockingQueue(int size) {
        return new ThreadPoolExecutor(size, size, 0, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
