package routeros;
import java.util.Map;
import me.legrange.mikrotik.MikrotikApiException;
import me.legrange.mikrotik.ResultListener;

/**
 * Example 5: Asynchronous results, with error and completion. Run a command and receive results, errors and completion notification for it asynchronously with a ResponseListener
 *
 * @author gideon
 */
public class Example5 extends Example {

    public static void main(String... args) throws Exception {
        Example5 ex = new Example5();
        ex.connect();
        ex.test();
        ex.disconnect();
    }

    private void test() throws MikrotikApiException, InterruptedException {
        boolean completed = false;
       String id = con.execute("/interface/wireless/monitor .id=wlan1", new ResultListener() {
           private int prev = 0;

           public void receive(Map<String, String> result) {
               int val = Integer.parseInt(result.get("signal-strength"));
               String sym = (val == prev) ? " " : ((val < prev) ? "-" : "+");
               System.out.printf("%d %s\n", val, sym);
               prev = val;
           }

           public void error(MikrotikApiException ex) {
               System.out.printf("An error ocurred: %s\n", ex.getMessage());
               ex.printStackTrace();
           }

           public void completed() {
               System.out.printf("The request has been completed\n");
           }


        });
       // let it run for 60 seconds 
       Thread.sleep(10000);
       con.cancel(id);
       Thread.sleep(2000);
    }
}
