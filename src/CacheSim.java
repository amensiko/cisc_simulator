import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * A simple cache simulator
 * line: list of cache lines, where each cache line is represented by a HashMap
 *
 * @author: Anastasija Mensikova
 */

public class CacheSim {

    public static int[] w = new int[6];
    List<Map<String , Object>> line  = new ArrayList<Map<String,Object>>(); //size 8

    public CacheSim (){
        for (int i = 0; i <= 7; i++) {
            Map<String, Object> value = new HashMap<String, Object>();
            value.put("tag", -1);
            value.put("inline", -1);
            value.put("written", 0);
            value.put("w", w);
            line.add(i, value);
        }
    }

}
