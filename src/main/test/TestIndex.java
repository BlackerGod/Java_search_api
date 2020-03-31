import index.Index;

import java.io.IOException;
import java.util.List;

public class TestIndex {
    public static void main(String[] args) throws IOException {
        Index index = new Index();
        index.build("E:\\IDEA\\java_api_search\\tmp.txt");
        List<Index.Weight> indexWeightList = index.getInverted("arraylist");
        for (Index.Weight weight : indexWeightList){
            System.out.println(weight.weight);
            System.out.println(index.getDocInfo(weight.docId).getTitle());
            System.out.println(index.getDocInfo(weight.docId).getUrl());
        }
    }
}
