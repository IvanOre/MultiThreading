package Producer_Consumer2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * создаем паттерн продюсер-консюмер
 * Хотим синхронизировать потоки. Что бы если очередь пустая,то консюмер ждал. А продюсер не добавлял новые элементы
 * в очередь если она уже полная.
 * Чаще всего notify() вызывается в конце блока. Это правильно тк это финализирующий метод,который будит потокБ и после
 * него не должно быть ничего,иначе будет задержка на пробуждение.
 *
 * Те самые 2 условия:
 * вызван ан объекте notify() и был освобожден монитор
 *
 * Почему юзаем цикл while а не for.
 * Хотим себя обезопасить. Крутимся каждый раз что бы удостовериться,что элемент ушел или элемент добавлен.
 * ЧТо бы не было добавления 11 элемента и очередь не была пустой.
 *
 */

public class Test {

    public static void main(String[] args) throws InterruptedException {

        ProducerConsumer pc = new ProducerConsumer();

        Thread thread1 = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }
}

class ProducerConsumer {
    private Queue<Integer> queue = new LinkedList<>();// реализуем через обычную очередь не thread safe
    private final int LIMIT = 10;// максимум элементов сколько хотим видеть в нашей очереди
    private final Object lock = new Object();// на этом объекте будет синхронизация

    public void produce() throws InterruptedException {// добавляем новые числа в очередь
        int value = 0;
        while (true) {
            synchronized (lock) {
                while (queue.size() == LIMIT) {// если очередь полная.10 элементов уже содержит
                    lock.wait();// то ждем пока не освободится место
                }
                queue.offer(value++);
                lock.notify();// даем понять консюмеру что есть элементы и можно забирать. пробуждает его
            }
        }
    }

    public void consume() throws InterruptedException {// из очереди вынимаем
        while (true) {
            synchronized (lock) {
                while (queue.size() == 0){// если очередь пустая то ждем заполнения
                    lock.wait();
                }
                int value = queue.poll();// достаем из очереди элемент
                System.out.println(value);// и выводим его на экран
                System.out.println("Queue size is : " + queue.size());
                lock.notify();// даем команду продюсеру не ждать больше и работать дальше
            }
            Thread.sleep(1000);// что бы консюмер по 1 элементу извлекал из очереди ,а не скопом
        }
    }
}

