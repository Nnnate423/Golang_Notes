
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TestLambda {

    List<Integer> nums = Arrays.asList(1,2,3,4,5);
    List<String> strs = Arrays.asList("Sydney", "Dhaka", "New York", "London");

    @Test
    public void TestConsumer(){
        Consumer<String> show_str = str -> {System.out.println(str);};
        Consumer<String> show_str2 = str -> {System.out.println(str);};
        Consumer<Integer> Increment_num = num -> {num+=1;};
        strs.forEach(show_str);
        show_str.andThen(show_str2).accept("andThen and accept test.");
    }

    @Test
    public void TestSupplier(){
        Random r = new Random();
        Supplier<Integer> intSupplier = () -> r.nextInt();
        DoubleSupplier doubleSupplier = Math::random;
        System.out.println(intSupplier.get());
        System.out.println(doubleSupplier.getAsDouble());
    }

    @Test
    public void TestFunction(){
        Function<String, Integer> samplefunc= (a) -> {return a.length();};
        strs.stream().map(samplefunc).collect(Collectors.toList());
    }

    @Test
    public void TestLambda(){
        Runner runner = (time) ->{System.out.println("Runner run for: "+ time+ " seconds.");};
        runner.goRun(3);
    }

}

interface Runner{
    public void goRun(int time);
}
