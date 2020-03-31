import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

public class TestAnsj {
    public static void main(String[] args) {
        String str = "";
       List<Term> tems =  ToAnalysis.parse(str).getTerms();
       for (Term term : tems){
           System.out.println(term);
       }

    }
}
