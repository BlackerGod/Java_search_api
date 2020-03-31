package index;

import common.DocInfo;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建索引，正排索引（ID =》 文档），倒排索引（文档 =》 ID）
 */
public class Index {
    /**
     * 这个静态类是为了计算权重。
     */
    static public class Weight{
        public String word;
        public int docId;
        public int weight;
        //weight = titleCount*10 + textCount;
    }

    // 正排索引
    private ArrayList<DocInfo> forwardIndex = new ArrayList<>();

    // 倒排索引,不仅知道在那个docId下，而且要显示其权重
    // 权重：该词和文档的关联程度
    private HashMap<String,ArrayList<Weight>> invertedIndex = new HashMap<>();


    /**
     * 查询正排
     * @param docId 文章ID
     * @return  文章信息
     */
    public DocInfo getDocInfo(int docId){
        return forwardIndex.get(docId);
    }

    /**
     * 查询倒排
     * @param term 目标词
     * @return 文章列表
     */
    public ArrayList<Weight> getInverted(String term){
        return invertedIndex.get(term);
    }

    /**
     * 把txt文件内容读取出来，加载到内存上面的数据结构
     * \3分隔
     */
    public void build(String path) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("build start");

        // 1.打开文件，按行读取
        BufferedReader bw = new BufferedReader(new FileReader(new File(path)));

        // 2.接收每一行
        String line = "";

        while((line = bw.readLine()) != null){
        // 3.构造正排的过程：按照 \3来切分，切分结果构造成DocInfo对象，加入数据结构
            DocInfo docInfo = buildForward(line);

        // 4.构造倒排的过程
            buidInverted(docInfo);
            System.out.println("Build" + docInfo.getTitle() + "Finished");

        }
        bw.close();
        long finishTime = System.currentTimeMillis();
        System.out.println("build finished Time" + (finishTime - startTime)+"ms");
    }


    /**
     *
     * @param line 正排就是字符串切分
     * @return 返回docInfo
     */
    private DocInfo buildForward(String line) {
        // 把一行按照\3切分
        // 分出来的三个部分就是一个文档的 标题 url 正文；
        String[] tokens = line.split("\3");
        if(tokens.length != 3){
            // 文件格式有问题
            System.out.println("文件格式存在问题：" + line);
            return null;
        }
        DocInfo docInfo = new DocInfo();
        // id 就是正排索引下标
        docInfo.setDocId(forwardIndex.size());
        docInfo.setTitle(tokens[0]);
        docInfo.setUrl(tokens[1]);
        docInfo.setContent(tokens[2]);
        forwardIndex.add(docInfo);
        return docInfo;
    }


    private void buidInverted(DocInfo docInfo) {
        class WordCnt{
            public int titleCount;
            public int contengtCount;

            public WordCnt(int titleCount, int contengtCount) {
                this.titleCount = titleCount;
                this.contengtCount = contengtCount;
            }
        }

        HashMap<String,WordCnt> wordCntHashMap = new HashMap<>();
        // 1.对标题分词
        List<Term> titleTerms = ToAnalysis.parse(docInfo.getTitle()).getTerms();
        // 2.遍历分词结果，统计标题中的每个词出现频率
        for (Term term : titleTerms){
            // 此处word已经转成小写了
            String word = term.getName();
            WordCnt wordCnt = wordCntHashMap.get(word);
            if(wordCnt == null){ // 不存在
                wordCntHashMap.put(word,new WordCnt(1,0));
            } else {
                wordCnt.titleCount++;
            }
        }



        // 3.针对正文分词
        List<Term> contentTerms = ToAnalysis.parse(docInfo.getContent()).getTerms();
        // 4.遍历分词结果，统计正文中词出现的频率
        for (Term term : contentTerms){
            String word = term.getName();
            WordCnt wordCnt = wordCntHashMap.get(word);
            if(wordCnt == null){
                wordCntHashMap.put(word,new WordCnt(0,1));
            } else {
                wordCnt.contengtCount++;
            }
        }

        // 5.遍历HashMap，一次构建weight对象并更新倒排索引的映射关系
        for (Map.Entry<String,WordCnt> entry : wordCntHashMap.entrySet()){
            Weight weight = new Weight();
            weight.word = entry.getKey();
            weight.docId = docInfo.getDocId();
            weight.weight = entry.getValue().titleCount * 10 + entry.getValue().contengtCount;

            //weight加入到倒排索引中
            ArrayList<Weight> invertedList = invertedIndex.get(entry.getKey());
            if(invertedList == null){
                // 不存在
                invertedList = new ArrayList<>();
                invertedIndex.put(entry.getKey(),invertedList);
            }
            invertedList.add(weight);
        }

    }
}
