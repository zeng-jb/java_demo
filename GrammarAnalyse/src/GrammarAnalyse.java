
import sun.rmi.runtime.Log;

import java.util.*;

import static java.util.Collections.sort;

public class GrammarAnalyse {

    public HashSet<Character> Terminal = new HashSet<>();//总结符集合
    public HashSet<Character> NonTerminal = new HashSet<>();//非总结符集合
    public ArrayList<String> production =  new ArrayList<String>();//推导式集合
    public static final char[] operator =  {'+', '-', '*', '/', '%', '(', ')','@'};//自定义非终结符列表
    public HashMap<Character,HashSet<Character>> first = new HashMap<>();//first集合
    public HashMap<Character,HashSet<Character>> follow = new HashMap<>();//follow集合
    public String [][] table; //预测分析表
    public Stack<Character> stack = new Stack<>();//分析栈

    public void inputGrammar( String pro){
        String[] line= pro.split("\n");
        for (String s : line) {
            if (!s.equals("$")){
                String[] str = s.split("->");//分隔符
                String S = "";//第一个字符
                S = Character.toString(str[0].charAt(0));//第一个字符为产生式左侧
                NonTerminal.add(str[0].charAt(0));//左侧都为非总结符
                //从->开始读
                for (int i = 0;i < str[1].length(); i++){
                    if(isTerminal(str[1].charAt(i))){
                        Terminal.add(str[1].charAt(i));
                    } else if (Character.isUpperCase(str[1].charAt(i))){
                        NonTerminal.add(str[0].charAt(0));
                    }
                    //遇到|，初始化S
                    if (str[1].charAt(i) == '|'){
                        production.add(S);
                        S = Character.toString(str[0].charAt(0));
                    }else{
                        S += str[1].charAt(i);
                    }
                }
                production.add(S);
            }
        }



    }

    //判断是否为总结符
    public static boolean isTerminal(char ch) {
        if (Character.isLowerCase(ch))
            return true;
        else {
            for (char c : operator) {
                if (ch == c)
                    return true;
            }
        }
        return false;
    }

    public void outputGrammar(){
        //产生式集
        System.out.println("======产生式集合======");

        for (Object object : production) {
            String s = object.toString();
            System.out.println(s.charAt(0) + "->" + s.substring(1, s.length()));
        }
    }
    public String outputGrammar1(){
        //产生式集
        String str="";
        str+="======产生式集合======\n";


        for (Object object : production) {
            String s = object.toString();
            //System.out.println(s.charAt(0) + "->" + s.substring(1, s.length()));
            str+=s.charAt(0) + "->" + s.substring(1, s.length())+"\n";
        }
        return str;
    }

    /*
    文法中不得含有有害规则和多余规则。
    “有害规则”：形如U→U的产生式。会引起文法的二义性。
    “多余规则”：指文法中任何句子的推导都不会用到的规则。
    1）文法中某些非终结符不在任何规则的右部出现，该非终结符称为不可到达的。
    2）文法中某些非终结符，由它不能推出终结符号串来，称为不可终止的。

     */
    public void deleteHarmful(){
        //删除有害规则 U->U
        for(int i = 0;i<production.size();i++){
            String s = production.get(i);
            if (s.charAt(0)==s.charAt(1) && s.length()==2)
                production.remove(i);
        }
    }

    public void deleteUnAble(){
        /*
         1.分别置VN’、 VT’、P’为φ
        2.将S 放入VN’中。
        3.对于G中任何型如A→α1|……|αm的产生式，若A∈VN’则将α1……αm 中的全部非终结符放入VN’中，终结符放入VT’中。
        4.重复③直至VN’、 VT’不增大为止。
        5.将P中左右部仅含VN’∪ VT’中符号的所有产生式放入P’ 中。
         */
        HashSet<Character> Vn = new HashSet<>();
        HashSet<Character> Vt = new HashSet<>();
        ArrayList<String> production1 = new ArrayList<>();

        for(int i=0; i < production.size(); i++){
            String str = production.get(i);
            int VnSize = Vn.size();
            int VtSize = Vt.size();
            for(int j=0;j<str.length();j++){
                if (isTerminal(str.charAt(j))){
                    Vt.add(str.charAt(j));
                } else {
                    Vn.add(str.charAt(j));
                }
            }
            if(VnSize == Vn.size() && VtSize == Vt.size()){ //如果Vn和Vt不再变大，退出循环
                break;
            }
        }
        NonTerminal.removeAll(Vn);          //找出不在Vn的非终结符
        production1 = production;
        Iterator<String> iterator =production1.iterator();
        while (iterator.hasNext()){
            String str = iterator.next();
            for (Character character : NonTerminal) {
                if (str.contains(character.toString()))
                    iterator.remove();
            }
        }

        NonTerminal = Vn;
        production = production1;
        NonTerminal.remove('@');
    }

    public void deleteUnStop() {
        /*
        分别置VN1，P1为Φ。
        对P中每一个产生式A→δ，若δ∈VT*，则将A放入VN1中。
        对P中每一个产生式A→X1 X2……XK，若每一个Xi 都属于VT或VN1，则将A放入VN1中。
        重复③直至VN1不增大。
        对于P中的每一个产生式B→Y1 Y2……Yn ，若B及每一个Yi ，都属于VN1∪VT  ，则将B→Y1 Y2……Yn，放入P1中。
        其次，对以给文法G[S]，若执行算法2.2可得到一等价文法G’=（VN’， VT’ ，P’，S）使得对任一X∈VN’∪ VT’都存在α，β∈（VN’∪ VT’）有S=*>αXβ.
         */
        Set<String> production1 = new HashSet<>();
        for (int i=0;i<production.size();i++){
            String str = production.get(i);
            production1.add(str.substring(0,2));
        }
        List list = new ArrayList<String>();
        for (Character character : NonTerminal) {
            char ch = character;
            for (String s : production1) {
                if (s.charAt(0) == ch){
                    list.add(s);
                }
            }
            if (list.size()==0) continue;
            else if (list.size()==1 ){
                if (list.get(0).toString().charAt(0) == list.get(0).toString().charAt(1)){
                    NonTerminal.remove(ch);
                    Iterator<String> iterator = production.iterator();
                    while (iterator.hasNext()){
                        if (iterator.next().contains(String.valueOf(ch))){
                            iterator.remove();
                        }
                    }
                }
            }
            list.clear();
        }
    }


    public void removeLeftGene() {

        ArrayList<String> production1 = new ArrayList(production);

        for (Character ch : NonTerminal) {
            char newVn = 0;
            for (int i = 0; i < 26; i++) {
                newVn = (char) (i + 'A');
                if (!NonTerminal.contains(newVn)) {
                    break;
                }
            }

            ArrayList<String> list = new ArrayList();
            Iterator<String> iterator = production.iterator();
            while (iterator.hasNext()) {
                String str = iterator.next();
                if (ch.equals(str.charAt(0))) {
                    String right = str.substring(1);
                    list.add(right);
                }
            }


            int k = 0;
            int index = 1;
            String longtemp = "";
            Collections.sort(list, new SortByLengthComparator());
            ArrayList<String> list1 = new ArrayList<>(list);
            for (int i = 0; i < list.size(); ) {
                if (k > list.get(i).length())
                    break;
                char c = list.get(i).charAt(k);
                for (int j = list.size() - 1; j > i; j--) {
                    if (list.get(j).charAt(k) == c) {
                        index++;
                    }
                }
                if (index < 2 && i == list.size() - 1) {
                    break;
                } else if (index < 2) {
                    list.remove(i);
                    i = 0;
                } else {
                    index = 1;
                    longtemp += c;
                    k++;

                }

            }
            System.out.println(longtemp);

            ArrayList<String> newPro = new ArrayList<>();
            if (!longtemp.equals(""))
                newPro.add(newVn+longtemp);
            for (String s : list1) {
                if (!s.contains(longtemp))
                    newPro.add(String.valueOf(ch)+s);
                else{
                    String[] str = s.split(longtemp);
                    newPro.add(ch+str[1]);
                }
            }
            System.out.println("newpro"+newPro);
            for (int i=0;i<production1.size();i++){
                if (production1.get(i).charAt(0) == ch)
                    production1.remove(i);
            }

            System.out.println("pro1"+production1);
            production1.addAll(newPro);

        }
        production = production1;
        System.out.println("pro"+production);
    }



    //消除直接左递归
    public void removeLeftRecursion(){
        boolean isReForm = false;//production是否被修改
        ArrayList<String> nullSign = new ArrayList<>();
        nullSign.add("@");//空串
        Iterator<String> it = production.iterator();
        while (it.hasNext()){
            String str = it.next();
            char left = str.charAt(0);
            boolean flag = false;//是否有左递归
            String right = str.substring(1);
            String oldRightCell;//旧产生式的右边
            String newLeftAndRightCell;//新产生式的左右边

            //消除直接左递归
            for (int i=0;i<right.length();i++){
                String newRightCell;//新产生式右边
                if (left == right.charAt(i)){
                    newRightCell = right.substring(1);
                }
                flag = true;
            }

        }
    }




    public void getFirst(Character c){
        //https://www.cnblogs.com/guopinghai/p/10094403.html
        HashSet<Character> set = first.containsKey(c) ? first.get(c) : new HashSet<>();//有就放到set中，没有创建
        if (Terminal.contains(c)){  //c为终结符直接添加
            set.add(c);
            first.put(c,set);
            return;
        }else{
            //非终结符，处理所有的产生式
            for (int i = 0; i<production.size();i++){
                //求非终结符开头的产生式
                if (production.get(i).charAt(0) == c){
                    //产生式右部
                    String s = production.get(i).substring(1,production.get(i).length());
                    //空串直接添加，这里是@，
                    if (s.equals(Character.toString('@'))){
                        set.add('@');
                    }else{
                        //遍历产生式右部
                        int j = 0;
                        while (j<s.length()){
                            getFirst(s.charAt(j));//区分终结符和非终结符
                            HashSet<Character> tempfirst = first.get(s.charAt(j));//如果遇到相同的字符
                            //将新的first集合加入原来的first集合
                            for (Character temp : tempfirst) {
                                set.add(temp);
                            }
                            break;//处理完直接退出
                        }
                    }
                    first.put(c,set);
                }
            }
        }
    }

    /******** 生成follow集
     * A->aBb A即传入的c，B为tempc
     * 若b存在且firstb不包含空串，把firstb-ε加入followB
     * 若b存在且firstb包含空串，把followA加入followB
     * 若b不存在，followA加入followB
     ********/
    public void getFollow(Character c) {
        HashSet<Character> setA = follow.containsKey(c) ? follow.get(c) : new HashSet<>();//没有就创建，有就加入
        setA.add('$');//为所有项加$
        for (int i = 0;i<production.size();i++){    //查找所有输入项产生式，确定c的后跟终结符
            String s = production.get(i).substring(1,production.get(i).length());//产生式右部
            for (int j = 0;j<s.length();j++){
                if (s.charAt(j) == c && j+1 <s.length() && Terminal.contains(s.charAt(j+1))){
                    setA.add(s.charAt(j+1));
                }
            }
        }
        follow.put(c,setA);
        //处理c的每一条产生式
        for (int i=0;i<production.size();i++){
            //求以非终结符开头的产生式
            if (production.get(i).charAt(0) == c){
                String s = production.get(i).substring(1,production.get(i).length());//产生式左部为c，右部为s
                int j = s.length()-1;//从右往左开始遍历
                while (j>=0){
                    char tempc = s.charAt(j);//tempc即为b
                    if (NonTerminal.contains(tempc)) {//只处理非终结符，如果b存在
                        if (s.length() - j -1 > 0){//确保有a存在
                            String right = s.substring(j+1,j+2);//取b
                            HashSet<Character> firstb = first.get(right.charAt(0));
                            HashSet<Character> followb = follow.containsKey(tempc) ? follow.get(tempc) : new HashSet<>();//是否存在followb
                            //firstb不能包含空串@，把firstb-@加入followb
                            for (Character ch : firstb) {
                                if (ch != '@')
                                    followb.add(ch);//非空串加入
                            }
                            follow.put(tempc,followb);
                            //如果b存在firstb包含空串，把followA加入followB
                            if (firstb.contains('@')){
                                if (tempc != c){
                                    HashSet<Character> setB = follow.containsKey(tempc) ? follow.get(tempc) : new HashSet<>();//是否存在followb
                                    for (Character ch : setA) {
                                        setB.add(ch);
                                    }
                                    follow.put(tempc,setB);
                                }
                            }
                        }else{//如果b不存在，followA加入followB
                            //A和B相同不添加
                            if (tempc != c){
                                HashSet<Character> setB =follow.containsKey(tempc) ? follow.get(tempc) : new HashSet<>();//是否存在followb
                                for (Character ch : setA) {
                                    setB.add(ch);
                                }
                                follow.put(c,setB);
                            }
                        }
                        j--;
                    }else{
                        j--;
                    }
                }
            }
        }
    }

    //打印first和follow
    public void printF(){
        //为终结符和非终结符计算 First 集
        for (Character c : Terminal) {
            getFirst(c);
        }
        for (Character c : NonTerminal) {
            getFirst(c);
        }
        //first 集
        System.out.println("\n=======First集======");
        for (Character key : first.keySet()) {
            System.out.println(key + ":" + first.get(key));
        }

        //两遍确保尾也全部运算到
        for (Character c : NonTerminal) {
            getFollow(c);
        }
        for (Character c : NonTerminal) {
            getFollow(c);
        }

        //follow 集
        System.out.println("\n=======Follow集======");
        for (Character key : follow.keySet()) {
            System.out.println(key + ":" + follow.get(key));
        }
    }

    //打印first和follow
    public String printF1(){
        //为终结符和非终结符计算 First 集
        for (Character c : Terminal) {
            getFirst(c);
        }
        for (Character c : NonTerminal) {
            getFirst(c);
        }

        String str="";

        //first 集
        str += ("\n=======First集======\n");
        for (Character key : first.keySet()) {
            str += (key + ":" + first.get(key)) + "\n";
        }

        //两遍确保尾也全部运算到
        for (Character c : NonTerminal) {
            getFollow(c);
        }
        for (Character c : NonTerminal) {
            getFollow(c);
        }

        //follow 集
        str+=("\n=======Follow集======\n");
        for (Character key : follow.keySet()) {
            str+=(key + ":" + follow.get(key))+"\n";
        }
        return str;
    }

    //生成预测分析表
    public void getTable(){
        Object[] tArray = Terminal.toArray();
        Object[] ntArray = NonTerminal.toArray();
        //预测分析表初始化
        table = new String[ntArray.length+1][tArray.length+1];
        table[0][0] = "Vn/Vt";
        //初始化首行首列
        for (int i=0;i<tArray.length;i++){
            table[0][i+1] = (tArray[i].toString().charAt(0) == '@') ? "$" : tArray[i].toString();//是空串当$输出
        }
        for (int i=0;i<ntArray.length;i++){
            table[i+1][0] = ntArray[i]+"";
        }
        for (int i=0;i<ntArray.length;i++)
            for (int j=0;j<tArray.length;j++)
                table[i+1][j+1] = "/";//初始化全部置空
        //处理每一条生产式
        for (int i=0;i<production.size();i++){
            char c = production.get(i).charAt(0);//已所求非终结符开头的生产式
            if (NonTerminal.contains(c)){
                String s = production.get(i).substring(1,production.get(i).length());//生产式右部
                if (s.equals("@")){ //直接推出@，需要follow集合
                    HashSet<Character> set = follow.get(c);
                    for (Character ch : set) {
                        addTable(c,ch,s);
                    }
                }else if (Terminal.contains(s.charAt(0))){
                    addTable(c,s.charAt(0),s);//直接推出终结符，加在终结符项
                }else {
                    HashSet<Character> set = first.get(c);//需要first集合
                    for (Character ch : set) {
                        addTable(c,ch,s);
                    }
                }
            }
        }
    }
    //填写表格 ，c为非终结符，ch为终结符，s为表示式右部
    public void addTable(char c,Character ch,String s){
        if (ch == '@')
            ch = '$';//替换
        for (int i=0;i<=NonTerminal.size();i++){
            if (table[i][0].charAt(0) == c){
                for (int j=0;j<=Terminal.size();j++){
                    if (table[0][j].charAt(0) == ch){
                        table[i][j] = c+"->"+s;
                        return;
                    }
                }
            }
        }
    }

    public void printTable() {
        System.out.println("\n======预测分析表:======");
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                System.out.printf("%6s", table[i][j] + " ");
            }
            System.out.println();
        }
    }
    public String printTable1() {
        String str="";
        str+=("\n======预测分析表:======\n");
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                str+=("\t"+table[i][j] + " ");
            }
            str+="\n";
        }
        return str;
    }

    //分析输入的字符串
    public void analyze(String word){
        System.out.println("\n======执行分析======");
        System.out.println("          Stack     Input     Action");
        stack = new Stack<Character>();
        String action = "";
        stack.push('$');//开始符号压栈
        stack.push(production.get(0).charAt(0));//生产式开始符压栈
        char x = stack.peek();//取栈顶符号
        int index = 0;
        word = word + "$";//加上终止符
        printStack(index,action,word);
        while (x != '$'){
            char ch = word.charAt(index);
            //表示匹配栈顶符号，符号栈弹出一个，index++指向下一个
            if (x == ch){
                action = "match " + stack.peek();
                stack.pop();
                index++;
            }else if(Terminal.contains(x)){
                System.out.println("表内未找到");
                return;
            }else if (search(x,ch).equals("/")){
                System.out.println("表内未找到");
                return;
            }else if (search(x,ch).equals('@')){
                stack.pop();
                action = x + "->@";
            }else {
                //否则查表正确，弹出栈顶符号，右部符号进栈
                String result = search(x,ch);
                if (!result.equals("")){
                    action = x + "->" + result;
                    stack.pop();
                    for (int i = result.length()-1;i>=0;i--){
                        if (result.charAt(i)!='@')
                            stack.push(result.charAt(i));
                    }
                }else {
                    System.out.println("匹配失败，在第"+index+"个"+word.charAt(index)+"字符出失败");
                    return;
                }
            }
            x = stack.peek();
            printStack(index,action,word);
        }
        System.out.println("匹配成功");
    }



    private String search(char x, char ch) {
        for (int i=0;i<=NonTerminal.size();i++){
            if (table[i][0].charAt(0) == x){
                for (int j=0;j<=Terminal.size();j++){
                    if (table[0][j].charAt(0) == ch){
                        return table[i][j].substring(3);//取产生式的右端
                    }
                }
            }
        }
        return "";
    }

    //打印栈情况
    private void printStack(int index, String action, String word) {
        Stack<Character> s = stack;
        System.out.printf("%20s", s);
        System.out.printf("%10s", word.substring(index));
        System.out.printf("%10s", action);
        System.out.println();
    }
    //打印栈情况
    private String printStack1(int index, String action, String word) {
        Stack<Character> s = stack;
        String str="";
        str+=("\t"+ s);
        str+=("\t"+ word.substring(index));
        str+=("\t"+action + "\n");

        return str;
    }

    //分析输入的字符串
    public String analyze1(String word){
        String str="";
        str+=("\n======执行分析======\n");
        str+=("\tStack\t\tInput\t\tAction\n");
        stack = new Stack<Character>();
        String action = "";
        stack.push('$');//开始符号压栈
        stack.push(production.get(0).charAt(0));//生产式开始符压栈
        char x = stack.peek();//取栈顶符号
        int index = 0;
        word = word + "$";//加上终止符
//        printStack(index,action,word);
        str+=("\t"+ stack );
        str+=("\t\t"+ word.substring(index));
        str+=("\t\t"+action + "\n");

        while (x != '$'){
            char ch = word.charAt(index);
            //表示匹配栈顶符号，符号栈弹出一个，index++指向下一个
            if (x == ch){
                action = "match " + stack.peek();
                stack.pop();
                index++;
            }else if(Terminal.contains(x)){
                str+=("\n表内未找到\n");
                return str;
            }else if (search(x,ch).equals("/")){
                str+=("\n表内未找到\n");
                return str;
            }else if (search(x,ch).equals('@')){
                stack.pop();
                action = x + "->@";
            }else {
                //否则查表正确，弹出栈顶符号，右部符号进栈
                String result = search(x,ch);
                if (!result.equals("")){
                    action = x + "->" + result;
                    stack.pop();
                    for (int i = result.length()-1;i>=0;i--){
                        if (result.charAt(i)!='@')
                            stack.push(result.charAt(i));
                    }
                }else {
                    str+=("\n匹配失败，在第"+index+"个"+word.charAt(index)+"字符出失败\n");
                    return str;
                }
            }
            x = stack.peek();
//            printStack(index,action,word);

            str+=("\t"+ stack);
            str+=("\t"+ word.substring(index));
            str+=("\t"+action + "\n");

        }
        str+=("\n匹配成功\n");

        return str;
    }
}
