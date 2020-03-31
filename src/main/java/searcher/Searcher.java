package searcher;

import common.DocInfo;
import index.Index;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Searcher {
    private Index index = new Index();

    public Searcher() throws IOException {
        // /home/ZMQ/JavaAPI/tmp.txt
        // E:\IDEA
        index.build("/home/ZMQ/JavaAPI/tmp.txt");
    }
    
    public List<Result> search(String query){
        // 1.[分词]对查询词分词
        List<Term> terms = ToAnalysis.parse(query).getTerms();
        // 2.针对查询词分词结果查找倒排索引，得到docId
        ArrayList<Index.Weight> allTokenResult = new ArrayList<>();
        for (Term term : terms){
            String word = term.getName();
            List<Index.Weight> invertedList = index.getInverted(word);
            if(invertedList == null){
                // 用户输入的这部分词很生僻，在所有文档中都不存在
                continue;
            }
            allTokenResult.addAll(invertedList);
        }
        // 3.[排序]
        allTokenResult.sort(new Comparator<Index.Weight>() {
            @Override
            public int compare(Index.Weight o1, Index.Weight o2) {
                return o2.weight - o1.weight;
            }
        });

        // 4[包装结果]
        ArrayList<Result> results = new ArrayList<>();
        for (Index.Weight weight : allTokenResult){
            // 根据docId
            DocInfo docInfo = index.getDocInfo(weight.docId);
            Result result = new Result();
            result.setTitle(docInfo.getTitle());
            result.setShowUrl(docInfo.getUrl());
            result.setClickUrl(docInfo.getUrl());
            //从正文中摘取一段摘要信息，根据这个词找到这个词在正文中的文职，再把这个为
            result.setDesc(GenDesc(docInfo.getContent(),weight.word));
            results.add(result);
        }
        return results;
    }

    private String GenDesc(String content,String word) {
        // 查找word在content中出现的位置
        // word里面内容全小写了，content还是大小写都有
        int firstPos = content.toLowerCase().indexOf(word);
        if(firstPos == -1){
            //极端情况下，只在标题中出现，没在标题中出现（比较极端，不处理）
            return "";
        }
        // 往前找60个字符，作为描述开始，不足60就从正文开始；
        // 往后找160个字符，作为描述结束，不足160就继续添加
        int descBeg = firstPos < 60 ? 0 : firstPos - 60;
        if (descBeg + 160 > content.length()){
            return content.substring(descBeg);
        }
        return content.substring(descBeg,descBeg+160) + "...";
    }
}
