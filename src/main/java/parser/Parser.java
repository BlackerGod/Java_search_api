package parser;

import java.io.*;
import java.util.ArrayList;

/**
 * 遍历文档目录，读取所有的html文档内容，把结果解析成行文本文件
 * 每一行对应一个文档，每一行都包含文档信息
 * Parser是一个单独可以执行的类（含main）
 */
public class Parser {
    // "E:\\IDEA\\docs\\api"
    // "E:\\IDEA\\tmp.txt"
    private static final String INPUT_PATH = null;
    private static final String OUTPUT_PATH = null;

    /**
     * //完成预处理
     * 1.枚举INPUT_PATH下所有html文件（递归）
     * 2.对html文件路径进行遍历，一次打开每个文件，并读取内容
     * 3.把内容转换成需要结构化的数据（DocInfo对象）,然后写出文件
     * @param args
     */
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(OUTPUT_PATH));
        ArrayList<File> fileList = new ArrayList<>();
        enumFile(INPUT_PATH,fileList);
        for (File f : fileList){
            //System.out.println("converting" + f.getAbsolutePath() + "...");
            String line = convertLine(f);
            //System.out.println(line);
            fileWriter.write(line);
        }
        fileWriter.close();
    }

    /**
     *
     * @param f 文件
     * @return  根据文件来获取标题.url.content；
     */
    private static String convertLine(File f) throws IOException {
        String title = convertTitle(f);
        String url = convertUrl(f);
        String content = convertContent(f);
        // \3起到分隔三个部分的效果. \3为ascii码为3的字符
        return title + "\3" + url + "\3" + content + "\n";
    }

    private static String convertContent(File f) throws IOException {
    //把标签和\n去掉
        FileReader fileReader = new FileReader(f);
        boolean isContent = true;
        StringBuilder output = new StringBuilder();
        while (true){
            int ret = fileReader.read();
            if(ret == -1){
                break;
            }
            char c = (char)ret;
            if(isContent){//是正文
                if(c == '<'){
                    isContent = false;
                    continue;
                }
                if(c == '\n' || c == '\r'){  //\n换行，\r表示回车
                    c = ' ';
                }
                output.append(c);
            } else { // 是标签
                if(c == '>'){
                    isContent = true;
                }
            }
        }
        fileReader.close();
        return output.toString();
    }
    private static String convertUrl(File f) {
        //线上文档对应的Url
        String prev = "https://docs.oracle.com/javase/8/docs/api";
        String text = f.getAbsolutePath().substring(INPUT_PATH.length());
        text = text.replaceAll("\\\\","/");
        return prev + text;
    }
    private static String convertTitle(File f) {
        //把文件名当做标题就可以了（去掉.html）
        String name = f.getName();
        return name.substring(0,name.length() - ".html".length());
    }

    /**
     *
     * @param inputPath  当前目录
     * @param fileList     已经保存的文件列表
     */
    private static void enumFile(String inputPath, ArrayList<File> fileList) {
        //递归把inputPath对应的全部目录和文件都遍历一遍
        File root = new File(inputPath);
        File[] files = root.listFiles(); //查看当前路径下的所有文件(包括文件夹)
        for (File f : files){
            if(f.isDirectory()){
                enumFile(f.getAbsolutePath(),fileList);
                //递归向下
            } else if(f.getAbsolutePath().endsWith(".html")){
                //是否是.html,是的话就添加
                fileList.add(f);
            }
        }

    }
}
