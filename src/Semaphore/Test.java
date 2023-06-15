package Semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore нужен когда у нас есть ресурс и много потоков его используют. Помогает регулировать доступ потоков к
 * ресурсу.
 * При создании Semaphore мы должны указать число(permits).Это число разрешений для потоков. Если есть ресурс,то это будет
 * сколько потоков одновременно могут в него писать.Допустим 3. Значит хотим что бы максимум 3 потока отправляли
 * данные на ресурс
 *
 *
 * Метод release() вызываем когда в потоке заканчиваем использовать ресурс. Возвращает разрешение семафору.
 * Допустим поток работал. а у семафора было 3 разрешения. поток забрал себе одно. у семафора осталось 2.
 * вызываем в потоке релиз. он отдает разрешение семафору и у него сново их 3.Метод release() всегда должен
 * вызываться в finally блоке.
 *
 *
 * Метод acquire() вызываем когда в потоке начинаем взаимодействие с ресурсом
 *
 *
 * Метод availablePermits метод возвращает число доступных свободных разрешений
 *
 *
 * Тк мы допустим указал 3 в разрешениях. То 4 поток,вызывая acquire(),не сможет воспользоваться ресурсом,
 * пока какой-нибудь поток уже работающий с ресурсом,не вызовет release и не освободит разрешение.
 *
 *
 * 200 потоков имеем. Хотим что бы не более 10 потоков одновременно использовали наш Connection
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(200);// создали 10 потоков

        Connection connection = Connection.getConnection();// получили нащ конекшн в мейн метод
        for (int i = 0; i < 200; i++) {
            executorService.submit(() -> {
                try {
                    connection.doWorkWithSemaphore();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            });

        }
        executorService.shutdown();//закончили отправлять задания в наш ексекуторсервис и потоки начинают выполнени
        executorService.awaitTermination(1, TimeUnit.DAYS);//ждем максимум 1 день пока потоки все выполнят

    }
}

class Connection {
    private static Connection connection = new Connection();
    private int connectionsCount;
    private Semaphore semaphore = new Semaphore(10);// создали семафор для регуляции доступа к конекшн. 10 за раз только

    private Connection() {// приватный тк даем понять что нельзя создавать сколько угодно классов Connection(singleton)


    }

    public static Connection getConnection() {
        return connection;

    }

    public void doWorkWithSemaphore() throws InterruptedException {// метод для семафора и обернули в него doWork
        semaphore.acquire();// 10 потоков из 200 начали работу. 11 поток ждет
        try {
            doWork();// выполняют работу метода в конекшн
        }finally {// гарантирует что бы не случилось в doWork() метод release() будет вызван все равно и освободит место потокам
            semaphore.release();//один поток выполнил работу, освобождаем место и 11 поток может начать работу и тд
        }

    }

    private void doWork() throws InterruptedException {// делаем в нем какую-то работу с нашим connection
        synchronized (this) {
            connectionsCount++; // инкриментируем переменную в синхронайз блоке
            System.out.println(connectionsCount);
        }
        Thread.sleep(5000);// симуляция работы какой-то. поспали 5 сек

        synchronized (this) {
            connectionsCount--;// дикриментим переменную

        }

    }
}
