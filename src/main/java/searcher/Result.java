package searcher;

/**
 * 表示一条搜索结果，是根据DocInfo得到的
 */
public class Result {
    private String title;
    private String clickUrl;
    private String ShowUrl;
    private String Desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getShowUrl() {
        return ShowUrl;
    }

    public void setShowUrl(String showUrl) {
        ShowUrl = showUrl;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    @Override
    public String toString() {
        return "Result{" +
                "title='" + title + '\'' +
                ", clickUrl='" + clickUrl + '\'' +
                ", ShowUrl='" + ShowUrl + '\'' +
                ", Desc='" + Desc + '\'' +
                '}';
    }
}
