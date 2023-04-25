import java.util.Calendar;
import java.util.Date;

public class tst {
    public static void main(String[] args) {
        // Create a Date object representing the current date and time
        Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
        long token = calendar.get(Calendar.YEAR) * (long)10e12;
        token += calendar.get(Calendar.MONTH)* 10e10;
        token += calendar.get(Calendar.DATE) * 10e8;
        token += calendar.get(Calendar.HOUR) * 10e6;
        token += calendar.get(Calendar.MINUTE) * 10e4;
        token += calendar.get(Calendar.SECOND) * 10e2;
        token += calendar.get(Calendar.MILLISECOND);


        // Convert the long value to an int value

        // Display the generated integer token
        System.out.println("Generated Integer Token: " + token);
    }
}
