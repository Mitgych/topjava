package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            dateCalories.merge(date, meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean isExcess = dateCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay;
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> dateCalories = meals.stream().collect( new sumCollector());

        return meals.stream()
                .filter(i -> ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen(i.getDateTime().toLocalTime(), startTime, endTime))
                .map(i -> {
                    boolean isExcess = dateCalories.getOrDefault(i.getDateTime().toLocalDate(), 0) > caloriesPerDay;
                    return new UserMealWithExcess(i.getDateTime(), i.getDescription(), i.getCalories(), isExcess);
                }).collect(Collectors.toList());
    }

    static class sumCollector implements Collector<UserMeal, Map<LocalDate, Integer>, Map<LocalDate, Integer>> {

        @Override
        public Supplier<Map<LocalDate, Integer>> supplier() {
            return HashMap::new;
        }

        @Override
        public BiConsumer<Map<LocalDate, Integer>, UserMeal> accumulator() {
            return (map, userMeal) ->
            {
                LocalDate date = userMeal.getDateTime().toLocalDate();
                map.merge(date, userMeal.getCalories(), Integer::sum);
            };
        }

        @Override
        public BinaryOperator<Map<LocalDate, Integer>> combiner() {
            return (l, r) -> {
                l.forEach((key, value) -> r.merge(key, value, Integer::sum));
                return r;
            };
        }

        @Override
        public Function<Map<LocalDate, Integer>, Map<LocalDate, Integer>> finisher() {
            return s -> s;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.UNORDERED);
        }
    }
}
