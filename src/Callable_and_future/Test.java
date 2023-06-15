package Callable_and_future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Классы Callable and Future позволяют нам возвращать значения из потоков.И выбрасывать исключения из потоков.
 * Зачем нам это надо.
 * из анонимного класса new Runnable мы не можем возвращать значение.если заменим лямбдой,то сможем,но почему,есть момент.
 * Когда Runnable заменяем лямбдой,и возвращаем значение,Джава сама понимает,что это не Runnable,а Callable.
 *
 *
 *
 * Для наглядности в примере используем Runnable
 *
 * public class Test {
 *     private static int result;// пример как можем возвращать из Runnable
 *
 *     public static void main(String[] args) {
 *         ExecutorService executorService = Executors.newFixedThreadPool(1);
 *
 *         executorService.submit(new Runnable() {// создали еще новый поток
 *             @Override
 *             public void run() {
 *                 System.out.println("Starting");
 *                 try {
 *                     Thread.sleep(3000);
 *                 } catch (InterruptedException e) {
 *                     throw new RuntimeException(e);
 *                 }
 *                 System.out.println("Finished");
 *                 result = 5;// можем возвращать значение
 *             }
 *         });
 *         executorService.shutdown();
 *
 *         try {
 *             executorService.awaitTermination(1, TimeUnit.DAYS);
 *         } catch (InterruptedException e) {
 *             throw new RuntimeException(e);
 *         }
 *     }
 *
 *     Таким образом в потоке и в Runnable с его методом run() которое void,
 *     можем что-то сделать,что-то вычислить и передать значение.
 *     Но у нас много кода и тд и тп. Не совсем красиво и удобно.
 *
 *
 * Вместо всего этого можно использовать интерфейс Callable.У него метод не void и предполагает что-то вернуть из метода.
 * Интерфейс Callable параметризованный <>. Указываем тип возвращаемого значения.
 *  Используем Callable<Integer> сейчас в коде.
 *  И получаем значение теперь через интерфейс Future. Он тоже параметризованный<>. Тем же типом что и Callable делали.
 *  В нем ожидаем,что когда поток завершит свое выполнение,в переменной future, мы получим
 *  какое-либо значение целого числа интежер.
 *
 *   Future<Integer> future = executorService.submit(new Callable<Integer>() {
 *             @Override
 *             public Integer call() throws Exception {
 *                 System.out.println("Starting");
 *
 *                 Thread.sleep(3000);
 *
 *                 System.out.println("Finished");
 *                 Random random = new Random();
 *                 return random.nextInt(10);
 *             }
 *         });
 *
 *         executorService.shutdown();закрываем поток для приема новых задач
 *
 *          try {
 *             int result = future.get();получаем значение в future через .get метод.get() дожидается завершения работы потока
 *              System.out.println(result);// выводим на экран значение
 *         } catch (InterruptedException e) {
 *             throw new RuntimeException(e);
 *         } catch (ExecutionException e) {
 *             throw new RuntimeException(e);
 *         }
 *
 *
 *     }
 * }
 *
 *                    Пример еще с выбрасыванием исключений из потока.
 *
 *  Создали свое исключение
 *  int randomValue = random.nextInt();
 *                 if (randomValue < 5)
 *                     throw new Exception("Something bad happened");// создали свою исключение. если меньше 5 выпадает число
 *                 // выбрасываем ексепшн с сообщением
 *
 *
 *
 *
 *
 */
public class Test {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

       Future<Integer> future = executorService.submit(() -> {
           System.out.println("Starting");
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
           System.out.println("Finished");
           Random random = new Random();
           int randomValue = random.nextInt();
           if (randomValue < 5)
               throw new Exception("Something bad happened");// создали свою исключение. если меньше 5 выпадает число
           // выбрасываем ексепшн с сообщением
           return random.nextInt(10);
       });

        executorService.shutdown();// закрываем поток для приема новых задач

        try {                     // get() дожидается завершения работы потока
            int result = future.get();// получаем значение в future.get(); и передаем в переменную result
            System.out.println(result);// выводим на экран значение
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            Throwable ex = e.getCause();// тут поймали исключение которое создали выше.
            System.out.println(ex.getMessage());
        }
    }
}
