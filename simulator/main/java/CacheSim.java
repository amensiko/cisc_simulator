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

    public static String[] w = new String[8];
    List<Map<String , Object>> line  = new ArrayList<Map<String,Object>>(); //size 16
    Queue queue=new Queue();


    public CacheSim (){
        for (int i = 0; i < 16; i++) {
            Map<String, Object> value = new HashMap<String, Object>();
            value.put("tag", -1);
            value.put("w", w);
            line.add(i, value);
        }

    }
    public String[] read_from_memory(int tag, String[]memory){
        int size=queue.getSize();
        int index=tag*8;
        String[] temp=new String[8];
        for(int j=0 ;j<8;j++){
            temp[j]=memory[index+j];
        }
        if(size<16){
            queue.enqueue(size);
            line.get(size).put("tag",tag);
            line.get(size).put("w",temp);
        }
        else{
            int i=queue.dequeue();
            queue.enqueue(i);
            line.get(i).put("tag",tag);
            line.get(i).put("w",temp);
        }
        return temp;

    }
    public int find_match(int tag){
        int size=queue.getSize();

        for(int i=0;i<size;i++){
            int temp_tag=(int)line.get(i).get("tag");
            if(temp_tag==tag){
                return i;
            }

        }
        return -1;
    }
    public String copy_data_from_cache(int i, int w_id){
        String[] w=(String[])line.get(i).get("w");
        return w[w_id];

    }

    public String access_memory(int decemal_address, String[] memory){
        int tag=decemal_address/8;
        int w_id=decemal_address%8;
        int i=find_match(tag);
        if(i==-1){
            return read_from_memory(tag,memory)[w_id];
        }
        else{
            return copy_data_from_cache(i, w_id);
        }
    }

    public String[] write_to_memory(String data, int decemal_address, String[] memory){
        int tag=decemal_address/8;
        int w_id=decemal_address%8;
        int i=find_match(tag);
        if(i==-1){
            read_from_memory(tag,memory);
            i=find_match(tag);
        }
        String[] w=(String[])line.get(i).get("w");
        w[w_id]=data;
        line.get(i).put("w",w);
        memory[decemal_address]=w[w_id];
        return memory;

    }




}