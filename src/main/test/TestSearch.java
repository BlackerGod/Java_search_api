import searcher.Result;
import searcher.Searcher;

import java.io.IOException;
import java.util.List;

public class TestSearch {
    public static void main(String[] args) throws IOException {
        Searcher searcher = new Searcher();
        List<Result> results = searcher.search("ArrayList");
        for (Result result : results){
            System.out.println(result);
            System.out.println("==============================");
        }

    }
}
