import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestStreamandIterable {
    List<Integer> nums = Arrays.asList(1,2,3,4,5);
    List<String> strs = Arrays.asList("Sydney", "Dhaka", "New York", "London");

    @Test
    public void IterableTest(){
        nums.forEach(e->System.out.println(e));
    }
}
