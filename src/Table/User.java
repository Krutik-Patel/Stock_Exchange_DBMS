import java.util.Date;
import java.text.SimpleDateFormat;

public class User {
    int user_id;
    String fname;
    String mname;
    String lname;
    String dob;

    public User() {
    }

    public User(int u, String f, String m, String l, Date d) {
        user_id = u;
        fname = f;
        mname = m;
        lname = l;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(d);
        dob = strDate;
    }

    public int get_user_id() {
        return user_id;
    }

    public String get_fname() {
        return fname;
    }

    public String get_mname() {
        return mname;
    }

    public String get_lname() {
        return lname;
    }

    public String get_dob() {
        return dob;
    }

    public void set_user_id(int u) {
        user_id = u;
    }

    public void set_fname(String f) {
        fname = f;
    }

    public void set_mname(String m) {
        mname = m;
    }

    public void set_lname(String l) {
        lname = l;
    }

    public void set_dob(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(d);
        dob = strDate;
    }

}
