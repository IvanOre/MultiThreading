package ReentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock нужен для синхронизации потока. Примерно тоже самое что synchronized ,но со своими плюсами.
 * Все создали запустили и у нас число выводящиеся не 20000. а меньше. образовался race condition
 * Надо фиксить. Тут нам поможет ReentrantLock
 * Reentrantlock это класс который реализует интерфейс Lock
 * lock на одном потоке рабоатет. он выполняет инкримент до 1000. после вызывается unlock. и метод lock забирает
 * себе другой поток и тоже выполняет инкримент до 10000.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                task.firstThread();

            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                task.secondThread();

            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        task.showCounter();//вызываем когда 2 потока отработают


    }
}

class Task {
    private int counter;
    private Lock lock = new ReentrantLock();// создали наш метод решения гонки потоков

    private void increment() {
        for (int i = 0; i < 10000; i++) {
            counter++;
        }
    }

    public void firstThread() {
        lock.lock();//закрыли
        increment();// вызвали инкремент в потоке 10000
        lock.unlock();// открыли

    }

    public void secondThread() {
        lock.lock();
        increment();
        lock.unlock();

    }

    public void showCounter() {
        System.out.println(counter);
    }
}
