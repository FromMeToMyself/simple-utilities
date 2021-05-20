package onelie.stringtek;

import com.google.common.collect.ImmutableList;
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

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <T> List<T> asyncBlockingDo(Class<T> clazz, Callable<T>... callableGroup) {
        return (List<T>) asyncBlockingDo(callableGroup);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> asyncBlockingDo(Class<T> clazz, Collection<Callable<?>> callableGroup) {
        return (List<T>) asyncBlockingDoCore(callableGroup);
    }

    public static List<?> asyncBlockingDo(Callable<?>... callableGroup) {
        return asyncBlockingDoCore(Arrays.asList(callableGroup));
    }

    public static List<?> asyncBlockingDo(Collection<Callable<?>> callableGroup) {
        return asyncBlockingDoCore(callableGroup);
    }

    private static void asyncNonBlockingDoCore(Collection<FutureTask<?>> futureTaskGroup) {
        ThreadPoolExecutor threadPool = newFixedThreadPoolExecutorWithoutBlockingQueue(
            futureTaskGroup.size());
        try {
            for (FutureTask<?> futureTask : futureTaskGroup) {
                threadPool.execute(futureTask);
            }
        } finally {
            threadPool.shutdownNow();
        }
    }


    private static List<Pair<?, Exception>> asyncBlockingDoCore(
        Collection<Callable<?>> callableGroup) {
        List<Pair<?, Exception>> result = new ArrayList<>();
        List<Future<?>> futureList = new ArrayList<>();
        ThreadPoolExecutor threadPool = newFixedThreadPoolExecutorWithoutBlockingQueue(
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

    private static ThreadPoolExecutor newFixedThreadPoolExecutorWithoutBlockingQueue(int size) {
        return new ThreadPoolExecutor(size, size, 0, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void main(String[] args) {
    }
}
