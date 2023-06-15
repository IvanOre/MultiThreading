package Producer_Consumer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Паттерн производитель-потребитель
 * Реализуем интерфейс BlockingQueue через ArrayBlockingQueue
 *Обычная очередь FIFO,но она потокобезопасная,есть предельный размер(указываем в capacity 10)
 * put потокобезопасный метод добавления. юзаем его в потоках
 *
 * Создаем потоки после создания методов проюсер и консюмер,а так же создания БлокингКью в классе Тест
 * Producer это один или более потоков которые создают-заполняют что-то в систему
 * Consumer берет из этого общего пула сущностей сущность и как-то ее обрабатывает
 *
 *
 *
 *
 */
public class Test {
       private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {// добавляет элементы
            try {
                produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        Thread thread2 = new Thread(() -> {//получает эелементы
            try {
                consumer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    private static void produce() throws InterruptedException {// пишущий поток. закидываем в очередь числа
        Random random = new Random();
        while (true){
            queue.put(random.nextInt(100));// быстро заполняет 10 элементов.
            // и ждет консюмера пока он не достанет свои элементы и только потом идет дальше заполнять пустоты

        }

    }
    private static void consumer() throws InterruptedException {
        Random random = new Random();
        while (true){
            Thread.sleep(100);

            if (random.nextInt(10) == 5) {// берем только числа которые в пределах 0-9 и кратны 5.

                System.out.println(queue.take());
                System.out.println("Queue size is :" + queue.size());
            }
        }
    }
}
