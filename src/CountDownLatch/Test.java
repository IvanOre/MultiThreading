package CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Классы из Джава.Ютил.Канкарент потокобезопасные. И им по сути не нужны методы wait() and notify()
 * CountDownLatch-защелка обратного отсчета.При создании указываем кол-во итераций,которые хотим отсчитать назад,
 * прежде чем защелка откроется.
 *
 * Сделали наоборот. В мейн методе отсчитываем наш CountDownLatch,а в 3 потоках будем ждать. Закомментированные
 * строки это наоборот было. В мейн методе ждали.а потоки отсчитывали
 *
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);// число итераций(countDown) до открытия защелки

        ExecutorService executorService = Executors.newFixedThreadPool(3);// 3 потока создали

        for (int i = 0; i < 3; i++)// назначили задания 3 потокам
            executorService.submit(new Processor(i,countDownLatch));// дикриментировать переменную countDownLatch

        executorService.shutdown();//закрываем потоки

        for (int i = 0; i < 3; i++){//считаем - 1 от нашего countDownLatch
            Thread.sleep(1000);
            countDownLatch.countDown();
        }

        /*countDownLatch.await();// защелка. ждем 3 итерации заданных
        System.out.println("Latch has been opened,main thread is proceeding!");*/

    }
}

class Processor implements Runnable{
    private int id;
    private CountDownLatch countDownLatch;

    public Processor(int id,CountDownLatch countDownLatch){
        this.id = id;

        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Thread with id " + id + " proceeded");

        /* countDownLatch.countDown();// отсчитываем наш латч назад. countDown--;*/

    }
}
