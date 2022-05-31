import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Test {
    public static void main(String[] args) {


        ArrayList<String> list = new ArrayList();

        char  S= 'S';
        char  newVn = 'Z';
//        Iterator<String> iterator = production.iterator();
//        while (iterator.hasNext()){
//            String str = iterator.next();
//            if (ch.equals(str.charAt(0))){
//                String right = str.substring(1);
//                list.add(right);
//            }
//        }
//        list.add("abA");
//        list.add("abB");
//        list.add("c");
        list.add("aaB");
        list.add("aaC");
        list.add("aaaDd");
        list.add("d");

        int k = 0;
        int index = 1;
        String longtemp="";

        Collections.sort(list,new SortByLengthComparator());
        ArrayList<String> list1 = new ArrayList<>(list);
        for (int i=0;i<list.size();) {
            if (k > list.get(i).length())
                break;
            char c = list.get(i).charAt(k);
            for (int j=list.size()-1;j>i;j--){
                if (list.get(j).charAt(k) == c){
                    index ++;
                }
            }
            if (index<2 && i==list.size()-1){
                break;
            }else if(index<2){
                list.remove(i);
                i=0;
            }else {
                index=1;
                longtemp+=c;
                k++;

            }

        }

        System.out.println(longtemp);

        ArrayList<String> newPro = new ArrayList<>();
        newPro.add(newVn+longtemp);
        for (String s : list1) {
            if (!s.contains(longtemp))
                newPro.add(String.valueOf(S)+s);
            else{
                String[] str = s.split(longtemp);
                newPro.add(S+str[1]);
            }
        }
        System.out.println(newPro);

    }
}


 class SortByLengthComparator implements Comparator<String> {

    @Override
    public int compare(String var1, String var2) {
        if (var1.length() > var2.length()) {
            return 1;
        } else if (var1.length() == var2.length()) {
            return 0;
        } else {
            return -1;
        }
    }

}