import java.util.*;
public class tst {
    public static ArrayList<Integer> tl;
    public static void main(String[] args) {
        // Create a Date object representing the current date and time
        String m ="a-b-c";
        String t = "a$b$c";
        String[] mg = m.split("-");
        String[] tg = t.split("[$]");

        for(int i=0;i<tg.length;i++){
            System.out.println(tg[i]);
        }
    }
}
