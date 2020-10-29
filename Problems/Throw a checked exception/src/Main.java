import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void method() throws Exception {
        // write your code here
        if (LocalDate.now().isBefore(LocalDate.of(2020, 10, 10))) {
            throw new RuntimeException("RuntimeException");
        }
        throw new Exception("Exception");
    }

    /* Do not change code below */
    public static void main(String[] args) {
        try {
            method();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException");
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }
}
