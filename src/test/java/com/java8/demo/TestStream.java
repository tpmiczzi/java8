package com.java8.demo;

import com.java8.demo.model.EqualsUser;
import com.java8.demo.model.Role;
import com.java8.demo.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStream extends DemoApplicationTests{

    private Collection<User> getCollectionUsers() {
        return Arrays.asList(
                new User(1, "user 1", Role.ADMIN),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.ADMIN),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.GUEST)
        );
    }

    @Test
    public void testSimpleStreamDemo_java7() {
        Collection<User> users = Arrays.asList(
                new User(1, "Pupkin", Role.ADMIN),
                new User(123, "Matata", Role.GUEST),
                new User(11, "Puper", Role.USER),
                new User(13, "Man", Role.USER),
                new User(12, "Woman", Role.USER)
        );

        List<User> filtered = new LinkedList<>();
        for (User user : users) {
            if (user.getRole() == Role.USER) {
                filtered.add(user);
            }
        }

        Collections.sort(filtered, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Long.compare(o2.getId(), o1.getId());
            }
        });

        List<String> names = new LinkedList<>();
        for (User user : filtered) {
            names.add(user.getName());
        }

        Assert.assertEquals("[Man, Woman, Puper]", names.toString());
    }

    @Test
    public void testSimpleStreamDemo_java8() {
        Collection<User> users = Arrays.asList(
                new User(1, "Pupkin", Role.ADMIN),
                new User(123, "Matata", Role.GUEST),
                new User(11, "Puper", Role.USER),
                new User(13, "Man", Role.USER),
                new User(12, "Woman", Role.USER)
        );

//        List<String> names = users.stream()
//                .filter(user -> user.getRole() == Role.USER)
//                .sorted((o1, o2) -> Long.compare(o2.getId(), o1.getId()))
//                .map(user -> user.getName())
//                .collect(Collectors.toList());
//
//        Assert.assertEquals("[Man, Woman, Puper]", names.toString());

        List<String> names = users.stream()
                .filter(user -> user.getRole() == Role.USER)
                .sorted(Comparator.comparing(User::getId).reversed())
                .map(User::getName)
                .collect(Collectors.toList());

        Assert.assertEquals("[Man, Woman, Puper]", names.toString());
    }

    @Test
    public void testFilter() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.USER),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.USER),
                new User(4, "user 4", Role.USER)
        );

        List<User> result = users.stream()
                .filter(user -> user.getId() % 2 == 0)
                .collect(Collectors.toList());

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testDistinct() {
        Collection<User> users = Arrays.asList(
                new EqualsUser(1, "user 1", Role.USER),
                new EqualsUser(2, "user 2", Role.USER),
                new EqualsUser(3, "user 3", Role.USER),
                new EqualsUser(4, "user 4", Role.USER),
                new EqualsUser(2, "user 2 updated", Role.USER),
                new EqualsUser(3, "user 3 updated", Role.USER)
        );

        List<User> result = users.stream()
                .unordered()
                .distinct()
                .collect(Collectors.toList());

        Assert.assertEquals(4, result.size());
    }

    @Test
    public void testLimitAndSkip() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.USER),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.USER),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.USER)
        );

        List<User> resultLimit = users.stream()
                .limit(2)
                .collect(Collectors.toList());

        Assert.assertEquals(2, resultLimit.size());

        List<User> resultSkip = users.stream()
                .skip(2)
                .collect(Collectors.toList());

        Assert.assertEquals(3, resultSkip.size());
    }

    @Test
    public void testIntermediateAndTerminalOperations() {
        List<String> phases = new LinkedList<>();
        Collection<Integer> users = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        List<Integer> names = users.stream()
                .filter(n -> {
                    phases.add("f-" + n);
                    return n % 2 == 0;
                })
                .map(n -> {
                    phases.add("m-" + n);
                    return n * n;
                })
                .sorted((n1, n2) -> {
                    phases.add("s-" + n1 + "-" + n2);
                    return Integer.compare(n2, n1);
                })
                .limit(2)
                .collect(Collectors.toList());

        System.out.println(names);
        System.out.println(phases);
    }

    @Test
    public void testPeek() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.USER),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.USER),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.USER)
        );

        List<User> sorted = new LinkedList<>();

        List<String> names = users.stream()
                .sorted(Comparator.comparing(User::getId).reversed())
                .peek(user -> sorted.add(user))
                .map(User::getName)
                .collect(Collectors.toList());

        System.out.println(names);
        System.out.println(sorted);
    }

    @Test
    public void testForEachOrdered() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.USER),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.USER),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.USER)
        );

        List<String> names = new LinkedList<>();

        users.stream()
                .map(User::getName)
                .forEachOrdered(name -> names.add(name));

        System.out.println(names);
    }

    @Test
    public void testMap() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.USER),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.USER),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.USER)
        );

        List<String> names = users.stream()
                .map(user -> user.getName())
                .collect(Collectors.toList());

        System.out.println(names);

        List<Long> ids = users.stream()
                .map(user -> user.getId() + 100L)
                .collect(Collectors.toList());

        System.out.println(ids);
    }


    @Test
    public void testMapToLong() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.ADMIN),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.ADMIN),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.GUEST)
        );

        long[] ids = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .peek(user -> System.out.println(user.getName()))
                .mapToLong(user -> user.getId())
                .peek(user -> System.out.println(user))
                .toArray();

        System.out.println("SUM " + users.stream().mapToInt(user -> (int) user.getId()).sum());
    }

    @Test
    public void testFlatMap() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.ADMIN),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.ADMIN),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.GUEST)
        );

        List<String> data = users.stream()
                .sorted(Comparator.comparing(User::getId))
                .flatMap(user -> Stream.of(
                        "id:" + user.getId(),
                        "role:" + user.getRole(),
                        "name:" + user.getName()
                ))
                .collect(Collectors.toList());

        System.out.println(data);
    }

    @Test
    public void testToArray() {
        Collection<User> users = Arrays.asList(
                new User(1, "user 1", Role.ADMIN),
                new User(2, "user 2", Role.USER),
                new User(3, "user 3", Role.ADMIN),
                new User(4, "user 4", Role.USER),
                new User(5, "user 5", Role.GUEST)
        );

        Object[] sorted = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .toArray();

        for (Object user : sorted) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void testCollect() {
        Collection<User> users = getCollectionUsers();

        List<String> result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .map(User::toString)
                .collect(
                        LinkedList::new,
                        LinkedList::add,
                        LinkedList::addAll
                );

//                        () -> new LinkedList<String>(),
//                        (container, name) -> container.add(name),
//                        (container, sub) -> container.addAll(sub)

        System.out.println(result);
    }

    @Test
    public void testCollectWithCollector() {
        Collection<User> users = getCollectionUsers();

        Set<String> result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .map(User::toString)
                .collect(Collector.of(
                        LinkedList<String>::new,
                        List::add,
                        (container, sub) -> {
                            container.addAll(sub);
                            return container;
                        },
                        (container) -> new LinkedHashSet<String>() {{
                            addAll(container);
                        }},
                        Collector.Characteristics.CONCURRENT,
                        Collector.Characteristics.UNORDERED
                        )
                );


        System.out.println(result);
    }

    @Test
    public void testCollectWithCollector_toMap() {
        Collection<User> users = getCollectionUsers();

        Map<Long, String> result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .collect(Collectors.toMap(
                        User::getId,
                        User::toString
                ));

        System.out.println(result);
    }

    @Test
    public void testCollectWithCollector_joining() {
        Collection<User> users = getCollectionUsers();

        String result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .map(User::getName)
                .collect(Collectors.joining());

        System.out.println(result);
    }

    @Test
    public void testCollectWithCollector_joiningChar() {
        Collection<User> users = getCollectionUsers();

        String result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .map(User::getName)
                .collect(Collectors.joining("_____"));

        System.out.println(result);
    }

    @Test
    public void testCollectWithCollector_joiningCharPref() {
        Collection<User> users = getCollectionUsers();

        String result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .map(User::getName)
                .collect(Collectors.joining(" _ ", "pref ", " suf"));

        System.out.println(result);
    }

    @Test
    public void testCollectWithCollector_counting() {
        Collection<User> users = getCollectionUsers();

        Long result = users.stream()
                .sorted(Comparator.comparing(User::getRole))
                .collect(Collectors.counting());

        System.out.println(result);
    }

    @Test
    public void testReduceOptional() {
        Collection<User> users = getCollectionUsers();

        Optional<Long> result = users.stream()
                .map(User::getId)
                .reduce((a, b) -> a + b);

        System.out.println(result);

        Long res = users.stream()
                .map(User::getId)
                .reduce(300L, (a, b) -> a + b);

        System.out.println(res);

        StringBuilder res1 = users.stream()
                .reduce(
                        new StringBuilder(),
                        (builder, user) -> builder.append(user.toString()),
                        (builder, anotherBuilder) -> builder.append(anotherBuilder.toString())
                );

        System.out.println(res1);
    }

    @Test
    public void testMin() {
        Collection<User> users = getCollectionUsers();

        Optional<User> result = users.stream()
                .min(Comparator.comparing(User::getRole)
                        .thenComparing(Comparator.comparing(User::getName).reversed()));

        System.out.println(result);

        Optional<User> res = users.stream()
                .max(Comparator.comparing(User::getId));

        System.out.println(res);
    }


}
