package StramApi;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Методы потоков делятся на 2 категории.
 * Конвейерные позволяют производить какие-то действия с потоком(например применять методы к каждому элементу,изменять,фильтровать
 * данные и тд). Конвейерные методы после их применения, данные остаются в состоянии потока.Что бы вернуть их в нормлаьное
 * состояние,например сделать снова лист,для этого нужны терминальные методы.
 *
 * Терминальные методы возвращают в понятное нам состояние данные из потока.
 *
 *  List<Person> persons2 = persons.stream()
 *
 *  хотим отфильтровать список персон(по возрасту.кто старше 30лет). p-это условно обозначили person
 *
 *                 .filter(p -> p.getAge() > 30)..toList(); и вернули поток в вид коллекции обратно
 *                 .sort() отсортировали объекты в листе по имени(задали условие в Person в методе compareTo)
 *
 *                  for (int i = 0; i < persons2.size(); i++ ){
 *             System.out.println(persons2.get(i)); вывели список кто старше 30 лет
 *         }
 *
 *
 *
 *
 *         .map() позволяет изменять данные,применять методы к данным и тд
 *
 *          .map(p -> new Person(p.getFirstname(),p.getLastname(),p.getAge() + 100)).toList();
 *          взяли нашего персон получили его имя,фамилию,возраст и сделали +100. теперь они старше на 100 лет стали
 *
 *           .map(p -> new Person(p.getFirstname(),"Иванов",p.getAge())).toList(); сделали всех  Ивановыми
 *
 *
 *
 *
 *           Optional -может содержать ,а может и не содержать
 *
 *            Optional<Person> person = persons.stream()
 *                 .filter(p -> p.getAge() > 30)/ищем кто старше 30
 *                 .findFirst();первый в списке
 *
 *         System.out.println(person.get());
 *         get() потому что нет toString у Optional. вернет персона который у него в параметрах
 *
 *         .findAny(); любого ищем
 *
 *
 *
 *          long count = persons.stream()    завели переменную
 *                 .filter(p -> p.getAge() > 30)
 *                 .count();   получаем кол-во записей подходящих под наш фильтр
 *         System.out.println(count);  выводит число. 7 тут. 7 людей старше 30 в списке
 *
 *
 */

public class Main {
    public static void main(String[] args) {
        List<Person> persons = Data.getPersons();// получили список людей обратившись к методу get.Persons()
        // обратились к persons.создали поток.и применяем методы разные
        //List<Person> persons2 = persons.stream()
               // .filter(p ->p.getAge() > 30)// хотим отфильтровать список персон(по возрасту.кто старше 30лет)
                //.map(p -> new Person(p.getFirstname(),"Иванов",p.getAge())).toList();
                //.sorted()// сортируем список. условия задали в Person в compareTo()
                //.toList();// вернули поток в вид коллекции обратно





        //////////////////////////COUNT///////////////////////////////////////////
       /* long count = persons.stream()// завели переменную
                .filter(p -> p.getAge() > 30)
                .count();// получаем кол-во записей подходящих под наш фильтр
        System.out.println(count);// выводит число. 7 тут. 7 людей старше 30 в списке*/





                ///////////////OPTIONAL//////////////////
        // Optional<Person> person = persons.stream()
                //.filter(p -> p.getAge() > 30)// ищем кто старше 30
                //.findFirst();// первый в списке
                       //.findAny();// любого ищем теперь

       // System.out.println(person.get());// get() потому что нет toString у Optional. вернет персона который у него в параметрах



        List<Person> persons2 = persons.stream()
                .filter(p -> p.getAge() > 40)
                .toList();

        // пример не через for
        persons2.forEach(p -> System.out.println(p));// можно заменить ссылкой на метод System.out::println





        //for (int i = 0; i < persons2.size(); i++ ){
           // System.out.println(persons2.get(i));
       // }
    }
}
