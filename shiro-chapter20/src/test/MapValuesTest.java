import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapValuesTest {
    public static void main(String[] args) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("A", "A");
        map.put("B", "B");
        map.put("C", "C");
        map.put("D", "D");
        map.put("E", "E");

        //直接打印
        System.out.println(map.values()); //[A, B, C, D, E]

        //报错：java.util.HashMap$Values cannot be cast to java.util.List
        //List<String> valuesList = (List<String>) map.values();
        List<String> valuesList=new ArrayList<>(map.values());

        for(String str:valuesList){
            System.out.println(str); //A回车B回车...
        }
    }
}
