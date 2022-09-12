package homework;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ComplexExamples {

  static class Person {

    final int id;

    final String name;

    Person(int id, String name) {
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Person person)) {
        return false;
      }
      return getId() == person.getId() && getName().equals(person.getName());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getId(), getName());
    }
  }

  private static Person[] RAW_DATA = new Person[]{
      new Person(0, "Harry"),
      new Person(0, "Harry"), // дубликат
      new Person(1, "Harry"), // тёзка
      new Person(2, "Harry"),
      new Person(3, "Emily"),
      new Person(4, "Jack"),
      new Person(4, "Jack"),
      new Person(5, "Amelia"),
      new Person(5, "Amelia"),
      new Person(6, "Amelia"),
      new Person(7, "Amelia"),
      new Person(8, "Amelia"),
  };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

  public static void main(String[] args) {
    System.out.println("Raw data:");
    System.out.println();

    for (Person person : RAW_DATA) {
      System.out.println(person.id + " - " + person.name);
    }

    System.out.println();
    System.out.println("**************************************************");
    System.out.println();
    System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");


        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться
                Key:Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

    Map<String, Integer> personMap = transformRawData(RAW_DATA);

    //Iteration over map to print results.
    personMap.forEach((key, value) -> {
      System.out.println("Key: " + key);
      System.out.println("Value:" + value);
    });

    System.out.println();


        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару именно в скобках, которые дают сумму - 10
         */

    System.out.println("**************************************************");
    System.out.println();
    System.out.println("Задание 2. Найти два элемента, сумма которых равна переданной.");
    System.out.println("Input: [3, 4, 2, 7], 10 -> "
        + Arrays.toString(findPair(new Integer[]{3, 4, 2, 7}, 10)));

    assert Arrays.equals(findPair(new Integer[]{3, 4, 2, 7}, 6),
        new Integer[]{4, 2});

    assert Arrays.equals(findPair(new Integer[]{4, 3, 6, 7}, 10),
        new Integer[]{4, 6});

    assert Arrays.equals(findPair(new Integer[]{4, 3, 4, 5}, 10),
        new Integer[]{});

    System.out.println("Input: [3, 4, 2, 7], 6 -> "
        + Arrays.toString(findPair(new Integer[]{3, 4, 2, 7}, 6)));
    System.out.println();


        /*
        Task3
            Реализовать функцию нечеткого поиска
                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */

    System.out.println("**************************************************");
    System.out.println();
    System.out.println("Задание 3. Реализвать функцию нечеткого поиска.");
    System.out.println("Input: \"car\", \"ca6$$#_rtwheel\" - "
        + fuzzySearch("car", "ca6$$#_rtwheel"));
    System.out.println();

    assert fuzzySearch("cwhl", "cartwheel");
    assert fuzzySearch("cwhee", "cartwheel");
    assert fuzzySearch("cartwheel", "cartwheel");
    assert !fuzzySearch("cwheeel", "cartwheel");
    assert !fuzzySearch("lw", "cartwheel");
  }

  public static Map<String, Integer> transformRawData(Person[] data) {

    Map<String, Integer> personMap = new HashMap<>();

    if (data != null) {
      List<Person> personList = Arrays
          .stream(data)
          .sorted(Comparator.comparing(Person::getName).thenComparing(Person::getId))
          .distinct()
          .toList();

      for (Person p : personList) {
        if (personMap.containsKey(p.name)) {
          personMap.put(p.name, personMap.get(p.name) + 1);
        } else {
          personMap.put(p.name, 1);
        }
      }
    }

    return personMap;
  }


  public static Integer[] findPair(Integer[] array, int sum) {
    Set<Integer> set = new HashSet<>(array.length);
    for (int num : array) {
      if (set.contains(sum - num)) {
        return new Integer[]{sum - num, num};
      }
      set.add(num);
    }
    return new Integer[0];
  }

  public static boolean fuzzySearch(String substring, String string) {
    //Position of char in substring.
    int substringPos = 0;
    //Amount of same symbols in string.
    int foundCount = 0;

    if (substring != null && string != null) {
      //Iterates over main string (where to find chars from substring).
      for (char c : string.toCharArray()) {
        if (foundCount < substring.length() && c == substring.charAt(substringPos)) {
          foundCount++;
          substringPos++;
        }
      }
      return foundCount == substring.length();
    } else {
      return false;
    }
  }
}
