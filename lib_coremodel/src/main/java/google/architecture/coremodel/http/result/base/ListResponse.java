package google.architecture.coremodel.http.result.base;

import java.util.List;

/**
 * 订单list数据
 * Created by ASUS on 2017/8/8.
 */

public class ListResponse<T> {
    public int endRow;
    public int firstPage;
    public boolean hasNextPage;
    public boolean hasPreviousPage;
    public boolean isFirstPage;
    public boolean isLastPage;
    public int lastPage;
    public List<T> list;
    public int navigatePages;
    public List<Integer> navigatepageNums;
    public int nextPage;
    public String orderBy;
    public int pageNum;
    public int pageSize;
    public int pages;
    public int prePage;
    public int size;
    public int startRow;
    public int total;
    public int pageNo;

    public List<T> getList() {
        return list;
    }

    public void appendData(ListResponse<T> result) {
        if (result != null) {
            this.endRow = result.endRow;
            this.firstPage = result.firstPage;
            this.hasNextPage = result.hasNextPage;
            this.hasPreviousPage = result.hasPreviousPage;
            this.isFirstPage = result.isFirstPage;
            this.isLastPage = result.isLastPage;
            this.lastPage = result.lastPage;
            this.navigatePages = result.navigatePages;
            this.navigatepageNums = result.navigatepageNums;
            this.orderBy = result.orderBy;
            this.pageNum = result.pageNum;
            this.nextPage = result.nextPage;
            this.pageSize = result.pageSize;
            this.pages = result.pages;
            this.prePage = result.prePage;
            this.startRow = result.startRow;
            this.total = result.total;
            //更新数据
            if (this.list == null) {
                this.list = result.list;
            } else {
                if (result.list != null) {
                    this.list.addAll(result.list);
                }
            }
            //更新数据数量
            this.size = this.list != null ? this.list.size() : 0;
        }
    }
}
