import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class test {
    // long time = startDate.getTime();
    Calendar cal = Calendar.getInstance();
    Scanner sc = new Scanner(System.in);
    @Test
    public void test() throws IOException {
        for (int i = 0; i < 50; i++) {
            System.out.println(i%12);
        }

    }
}
