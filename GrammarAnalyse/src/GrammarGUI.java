import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GrammarGUI {
    public static void main(String[] args) {
        GrammarFrame grammarFrame = new GrammarFrame();
        grammarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grammarFrame.setLocationRelativeTo(null);//居中
        grammarFrame.setResizable(false);
        grammarFrame.setVisible(true);

    }
}
class GrammarFrame extends JFrame implements ActionListener{

    private JPanel main_panel;

    private JMenuBar main_menu_bar;
    private JMenu menu_file;
    private JMenu menu_clear;
    private JMenuItem file_open;
    private JMenuItem file_save;
    private JMenuItem exit;
    private JMenuItem creardata;

    private JLabel lb_text_edit;
    private JLabel lb_text_gra;
    private JLabel lb_result;

    private JTextArea ta_gra_input;
    private JTextArea ta_input;
    private JScrollPane scrollpane_input;

    private JTextArea ta_result;
    private JScrollPane scrollpane_result;

    private JButton btn_start_grammar;
    private JButton btn_cleardata;

    public GrammarFrame(){
        this.setTitle("语法分析器");
        this.setSize(900,800);
        initPanel();
    }

    private void initPanel() {
        main_menu_bar = new JMenuBar();
        menu_file = new JMenu("文件");
        file_open = new JMenuItem("打开");
        file_save = new JMenuItem("保存");
        exit = new JMenuItem("退出");
        file_open.addActionListener(this);
        file_save.addActionListener(this);
        exit.addActionListener(this);
        menu_file.add(file_open);
        menu_file.add(file_save);
        menu_file.add(exit);
        main_menu_bar.add(menu_file);
        menu_clear = new JMenu("清空");
        creardata = new JMenuItem("清空");
        creardata.addActionListener(this);
        menu_clear.add(creardata);
        main_menu_bar.add(menu_clear);
        this.setJMenuBar(main_menu_bar);

        main_panel = new JPanel();
        main_panel.setLayout(null);

        lb_text_edit = new JLabel("文法编辑区");
        main_panel.add(lb_text_edit);
        lb_text_edit.setBounds(10, 10, 70, 20);

        ta_input = new JTextArea();
        ta_input.setFont(new Font("宋体", Font.BOLD,18));
        scrollpane_input = new JScrollPane(ta_input);
        main_panel.add(scrollpane_input);
        scrollpane_input.setBounds(10, 40, 400, 300);

        btn_start_grammar = new JButton("语法编译");
        main_panel.add(btn_start_grammar);
        btn_start_grammar.setBounds(450, 200, 200, 100);
        btn_start_grammar.addActionListener(this);

        btn_cleardata = new JButton("清空");
        main_panel.add(btn_cleardata);
        btn_cleardata.setBounds(200, 350, 60, 20);
        btn_cleardata.addActionListener(this);

        lb_text_gra = new JLabel("待分析的文法：");
        main_panel.add(lb_text_gra);
        lb_text_gra.setBounds(450,10,100,20);

        ta_gra_input = new JTextArea();
        ta_gra_input.setFont(new Font("宋体", Font.BOLD,18));
        main_panel.add(ta_gra_input);
        ta_gra_input.setBounds(450,40,300,100);

        lb_result = new JLabel("语法分析结果");
        main_panel.add(lb_result);
        lb_result.setBounds(50,380,80,20);

        ta_result = new JTextArea();
        ta_result.setEnabled(false);
        ta_result.setFont(new Font("宋体", Font.BOLD,18));
        ta_result.setCaretColor(Color.black);
        scrollpane_result = new JScrollPane(ta_result);
        main_panel.add(scrollpane_result);
        scrollpane_result.setBounds(50,400,800,300);

        add(main_panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == file_open){
            String file_name;
            JFileChooser file_open_filechooser = new JFileChooser();
            file_open_filechooser.setCurrentDirectory(new File("."));
            file_open_filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = file_open_filechooser.showOpenDialog(main_panel);
            // 证明有选择
            if (result==JFileChooser.APPROVE_OPTION) {
                file_name = file_open_filechooser.getSelectedFile().getPath();
                // 读取文件，写到JTextArea里面
                File file = new File(file_name);
                try{
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file),"utf-8");
                    BufferedReader reader=new BufferedReader(read);
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        ta_input.append(line);
                        ta_input.append("\n");
                    }
                    read.close();
                }
                catch(Exception event){
                    event.printStackTrace();
                }
            }
        }
        else if (e.getSource() == file_save){
            //弹出文件选择框
            JFileChooser chooser = new JFileChooser();

            //后缀名过滤器
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "文本文件(*.txt)", "txt");
            chooser.setFileFilter(filter);

            //下面的方法将阻塞，直到【用户按下保存按钮且“文件名”文本框不为空】或【用户按下取消按钮】
            int option = chooser.showSaveDialog(null);
            if(option==JFileChooser.APPROVE_OPTION){	//假如用户选择了保存
                File file = chooser.getSelectedFile();
                String fname = chooser.getName(file);	//从文件名输入框中获取文件名

                //假如用户填写的文件名不带我们制定的后缀名，那么我们给它添上后缀
                if(fname.indexOf(".txt")==-1){
                    file=new File(chooser.getCurrentDirectory().getPath(),fname+".txt");
                    System.out.println("renamed");
                    System.out.println(file.getName());
                }

                try {
                    FileWriter fw = new FileWriter(file);
                    String str = ta_input.getText();
                    for (int i=0;i<str.length();i++){
                        if (str.charAt(i)==10){
                            fw.write(13);
                            fw.write(10);
                            //写入\r\n
                        }else {
                            fw.write(str.charAt(i));
                        }
                    }
                    fw.close();

                } catch (IOException exp) {
                    System.err.println("IO异常");
                    exp.printStackTrace();
                }
            }
        }
        else if (e.getSource() == exit){
            System.exit(1);
        }
        else if(e.getSource() == btn_cleardata || e.getSource() == creardata){
            ta_input.setText("");
            ta_gra_input.setText("");
            ta_result.setText("");
        }
        else if (e.getSource() == btn_start_grammar){
            if(ta_input.getText().equals("")){
                JOptionPane.showMessageDialog(main_panel, "您什么都没有输入啊！", "提示", JOptionPane.ERROR_MESSAGE);
                System.out.println("nothing input!");
            }else if (ta_gra_input.getText().equals("")){
                JOptionPane.showMessageDialog(main_panel, "您什么还没输入待分析的语法！", "提示", JOptionPane.ERROR_MESSAGE);
                System.out.println("nothing input grammar!");
            }else{
                String pro = ta_input.getText();
                String gra = ta_gra_input.getText();
                GrammarAnalyse grammarAnalyse =new GrammarAnalyse();

                grammarAnalyse.inputGrammar(pro);
                ta_result.append(grammarAnalyse.outputGrammar1());

                ta_result.append("\n=======删除有害规则后======\n");
                grammarAnalyse.deleteHarmful();
                ta_result.append(grammarAnalyse.outputGrammar1());

                ta_result.append("\n=======删除不可到达规则后======\n");
                grammarAnalyse.deleteUnAble();
                ta_result.append(grammarAnalyse.outputGrammar1());

                ta_result.append("\n=======删除不可终止规则后======\n");
                grammarAnalyse.deleteUnStop();
                ta_result.append(grammarAnalyse.outputGrammar1());

                ta_result.append(grammarAnalyse.printF1());

                grammarAnalyse.getTable();
                ta_result.append(grammarAnalyse.printTable1());

                ta_result.append(grammarAnalyse.analyze1(gra));
            }
        }
        else{
            System.out.println("nothing!");
        }
    }

}