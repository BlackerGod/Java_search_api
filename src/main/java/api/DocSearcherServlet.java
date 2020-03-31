package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import searcher.Result;
import searcher.Searcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DocSearcherServlet extends HttpServlet {

    private Searcher searcher = new Searcher();
    private Gson gson = new GsonBuilder().create();

    public DocSearcherServlet() throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String query = req.getParameter("query");
        if(query == null){
            resp.setStatus(404);
            resp.getWriter().write("query参数非法");
            return;
        }
        List<Result> results = searcher.search(query);
        String respString = gson.toJson(results);
        resp.getWriter().write(respString);

    }
}
