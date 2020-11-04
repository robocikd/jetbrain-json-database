import java.util.*;

public class Main {

    public static void main(String[] args) {
        Set<String> nameSet = new TreeSet<>(Arrays.asList("Mr.Green", "Mr.Yellow", "Mr.Red"));
        // write your code here
        for (String s: nameSet) {
            System.out.println(s);
        }
    }
}
