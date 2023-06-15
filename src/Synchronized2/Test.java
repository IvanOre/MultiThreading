package Synchronized2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Для синхронизации 2 или более потоков в Джаве, нам нужен объект.Тк у них есть монитор.
 * <p>
 * public synchronized void increment(){
 * counter++;
 * }
 * Тут мы не явно указали объект на котором будем синхронизироваться. по умолчанию Test. Неявный метод.
 * <p>
 * <p>
 * public void increment(){
 * synchronized (this) { synchronized block. Указали на каком объекте конкретно хотим синхронизироваться
 * counter++;
 * }
 * }
 *
 * Если мы синхронизуем потоки через ключевое слово synchronized то мы гарантируем правильное выполнение методо тут.
 * Но теряем в скорости. Тк ждем пока один поток выполнит задачу и потом второй после него выполнит.
 * Что бы добиться параллельной работы потоков , то можем синхронизовать потоки на разных объектах.
 * И теперь у нас выполнятся в 2 раза больше работы одновременно. Заполняются параллельно 2 листа сразу.
 * Их нужно создать и назвать типа lock. Хороший тон разработки.
 *  Object lock1 = new Object(); Как пример взяли Object. Но можем использовать любой объект.
 *
 *   public void addToList1() throws InterruptedException {// методы заполнения листов 1000 элементами рандомно
 *         synchronized (lock1) {//через синхронайз блок синхронизируемся на разных объектах
 *         for (int i = 0; i < 1000; i++) {
 *             Thread.sleep(1);
 *
 *             list1.add(random.nextInt(100));// добавляем рандомные значения в лист от 0 до 9
 *         }
 *         }
 *     }
 *
 *    а на 2 методе добавления в лист синхронизируемся на lock2
 *

 *
 */
public class Test {
    private int counter;

    public static void main(String[] args) throws InterruptedException {
        new Worker().main();//  в потоке main вызываем метод main() в Worker

    }
}

class Worker {
    Random random = new Random();

    Object lock1 = new Object();// создали объекты разные для синхронизации разных потоков для паралеллизма
    Object lock2 = new Object();
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();

    public void addToList1() throws InterruptedException {// методы заполнения листов 1000 элементами рандомно
        synchronized (lock1) {//через синхронайз блок синхронизируемся на разных объектах
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1);

            list1.add(random.nextInt(100));// добавляем рандомные значения в лист от 0 до 9
        }
        }
    }

    public synchronized void addToList2() throws InterruptedException {
        synchronized (lock2){
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1);

            list2.add(random.nextInt(100));// добавляем рандомные значения в лист от 0 до 99
        }
        }
    }

    public void work() throws InterruptedException {
        addToList1();
        addToList2();
    }

    public void main() throws InterruptedException {// выводим время заполнения 2 листов из метода work().
        long before = System.currentTimeMillis();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    work();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    work();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        long after = System.currentTimeMillis();

        System.out.println("Program took " + (after - before) + " ms to run");
        System.out.println("List1 : " + list1.size());
        System.out.println("List2 : " + list2.size());
    }
}


