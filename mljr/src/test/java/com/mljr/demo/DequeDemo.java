package com.mljr.demo;


import java.util.concurrent.*;

/**
 * @author zicheng.liang
 * @version 1.0
 * @className:DequeDemo <br/>
 * @function: TODO ADD FUNCTION. <br/>
 * @createDate: 2016/11/14 <br/>
 * @since JDK 1.8
 */
public class DequeDemo {

    private static int THREAD_NUM;
    private static int N = 1000000;
    private static ExecutorService executor;
    
    public static void main(String[] args) throws Exception {
        System.out.println("Producer\tConsumer\tcapacity \t LinkedBlockingQueue \t ArrayBlockingQueue \t SynchronousQueue");

        for (int j = 0; j < 10; j++) {
            THREAD_NUM = (int) Math.pow(2, j);
            executor = Executors.newFixedThreadPool(THREAD_NUM * 2);

            for (int i = 0; i < 10; i++) {
                int length = (i == 0) ? 1 : i * 10;
                System.out.print(THREAD_NUM + "\t\t");
                System.out.print(THREAD_NUM + "\t\t");
                System.out.print(length + "\t\t");
                System.out.print(doTest2(new LinkedBlockingQueue<Integer>(length), N) + "/s\t\t\t");
                System.out.print(doTest2(new ArrayBlockingQueue<Integer>(length), N) + "/s\t\t\t");
                System.out.print(doTest2(new SynchronousQueue<Integer>(), N) + "/s");
                System.out.println();
            }

            executor.shutdown();
        }
    }

    private static class Producer implements Runnable {
        int n;
        BlockingQueue<Integer> q;

        public Producer(int initN, BlockingQueue<Integer> initQ) {
            n = initN;
            q = initQ;
        }

        public void run() {
            for (int i = 0; i < n; i++)
                try {
                    q.put(i);
                } catch (InterruptedException ex) {
                }
        }
    }

    private static class Consumer implements Callable<Long> {
        int n;
        BlockingQueue<Integer> q;

        public Consumer(int initN, BlockingQueue<Integer> initQ) {
            n = initN;
            q = initQ;
        }

        public Long call() {
            long sum = 0;
            for (int i = 0; i < n; i++)
                try {
                    sum += q.take();
                } catch (InterruptedException ex) {
                }
            return sum;
        }
    }

    private static long doTest2(final BlockingQueue<Integer> q, final int n)
            throws Exception {
        CompletionService<Long> completionServ = new ExecutorCompletionService<Long>(executor);

        long t = System.nanoTime();
        for (int i = 0; i < THREAD_NUM; i++) {
            completionServ.submit(new Consumer(n / THREAD_NUM, q));
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            executor.submit(new Producer(n / THREAD_NUM, q));
        }

        for (int i = 0; i < THREAD_NUM; i++) {
            completionServ.take().get();
        }

        t = System.nanoTime() - t;
        return (long) (1000000000.0 * N / t); // Throughput, items/sec
    }
}
