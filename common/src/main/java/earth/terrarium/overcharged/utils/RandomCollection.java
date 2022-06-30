package earth.terrarium.overcharged.utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;
/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 *
 * @author Team Resourceful
 */
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(Math.min(total, Double.MAX_VALUE), result);
        return this;
    }

    public E get(int index) {
        Double key = new LinkedList<>(map.keySet()).get(index);
        return map.get(key);
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public NavigableMap<Double, E> getMap(){
        return map;
    }

    public double getAdjustedWeight(double weight) {
        return weight / total;
    }

    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (E e : map.values()) {
            action.accept(e);
        }
    }

    public void forEachWithSelf(BiConsumer<RandomCollection<E>, ? super E> action) {
        forEach(element -> action.accept(this, element));
    }

    public Stream<E> stream() {
        return map.values().stream();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public int getSize() {
        return map.size();
    }

    public static <T> Collector<T, ?, RandomCollection<T>> getCollector(ToDoubleFunction<T> weightGetter) {
        return Collector.of(RandomCollection::new, (c, t) -> c.add(weightGetter.applyAsDouble(t), t), (left, right) -> {
            left.forEach(item -> right.add(weightGetter.applyAsDouble(item), item));
            return right;
        });
    }
}