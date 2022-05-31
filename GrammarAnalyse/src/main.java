import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        System.out.println("输入文法规则，以$结束：");
        GrammarAnalyse grammarAnalyse = new GrammarAnalyse();
        Scanner sc = new Scanner(System.in);
        String pro= "";
        while (!sc.hasNext("#"))
            pro += sc.nextLine()+"\n";


        System.out.println(pro);

        grammarAnalyse.inputGrammar(pro);
        grammarAnalyse.outputGrammar();
        System.out.println("=======删除有害规则后======");
        grammarAnalyse.deleteHarmful();
        grammarAnalyse.outputGrammar();
        System.out.println("=======删除不可到达规则后======");
        grammarAnalyse.deleteUnAble();
        grammarAnalyse.outputGrammar();
        System.out.println("=======删除不可终止规则后======");
        grammarAnalyse.deleteUnStop();
        grammarAnalyse.outputGrammar();
        System.out.println("=======提取左公因子后======");
        grammarAnalyse.removeLeftGene();
        grammarAnalyse.outputGrammar();

        grammarAnalyse.printF();
        grammarAnalyse.getTable();
        grammarAnalyse.printTable();

        System.out.print("请输入待判断字符串:");
        sc = new Scanner(System.in);
        String word = sc.nextLine();
        grammarAnalyse.analyze(word);
    }

/*
E->TA
A->+TA|@
T->FB
B->*FB|@
F->(E)|i
$
i*i+i

S->abA
S->abB
S->c
$
#


S->Be
B->B|Ce|Af
A->Ae|e
C->Cf|Ce
D->f
$

 */
}
