package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

import java.io.Serializable;
import java.util.List;

/**
 * 系统公告
 *
 * @author DoneYang 2017/6/16
 */

public class NoticeBean extends BaseSerializable {
    /**
     * pageNum : 1
     * pageSize : 3
     * size : 3
     * startRow : 1
     * endRow : 3
     * total : 6
     * pages : 2
     * list : [{"page":null,"size":null,"createTimeStart":null,"createTimeEnd":null,"updateTimeStart":null,"updateTimeEnd":null,"id":9,"createTime":1501071672000,"updateTime":1501071671000,"articleCats":null,"catId":1,"title":"开放商家入驻","isShow":"True","keyw":null,"staffId":1,"dataFlag":"True","solve":null,"unsolve":null,"content":"感谢您注册使用本APP，为感谢您对我们产品的信赖，恭喜您获得本平台商家免费入驻资格，时间从现在起30日内。入驻详情请咨询平台客服或登陆官网留言。客服电话：4001508508  网址：www.weilaitongfu.com"},{"page":null,"size":null,"createTimeStart":null,"createTimeEnd":null,"updateTimeStart":null,"updateTimeEnd":null,"id":8,"createTime":1501071654000,"updateTime":1501071654000,"articleCats":null,"catId":1,"title":"城市联合代理计划","isShow":"True","keyw":null,"staffId":1,"dataFlag":"True","solve":null,"unsolve":null,"content":"按照平台市场推广战略，7月中旬已正式启动城市联合代理招募计划。按照\u201c开放生态、合作共享\u201d原则，未来通付联合代理商将享有代理区域内商家销售分佣、会员升级奖励等多项补贴政策。了解城市联合代理详情，请联系公司客服或登陆公司官网留言。客服电话：4001508508  网址：www.weilaitongfu.com"},{"page":null,"size":null,"createTimeStart":null,"createTimeEnd":null,"updateTimeStart":null,"updateTimeEnd":null,"id":7,"createTime":1501071639000,"updateTime":1501071638000,"articleCats":null,"catId":1,"title":"内测公告","isShow":"True","keyw":null,"staffId":1,"dataFlag":"True","solve":null,"unsolve":null,"content":"未来通付，是一个基于共享经济理论，提供多端口支付、会员管理、互动营销、数据分析、商圈等功能，通过数据共享、支付创新，满足传统商家转型升级诉求，带给消费者增值体验的智慧型消费平台。<br />未来通付APP安卓版将于7月底开始内测。届时，用户可以在手机应用商城下载，用户体验需正式上线后。"}]
     * prePage : 0
     * nextPage : 2
     * isFirstPage : true
     * isLastPage : false
     * hasPreviousPage : false
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [1,2]
     * navigateFirstPage : 1
     * navigateLastPage : 2
     * lastPage : 2
     * firstPage : 1
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int lastPage;
    private int firstPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean implements Serializable {
        /**
         * page : null
         * size : null
         * createTimeStart : null
         * createTimeEnd : null
         * updateTimeStart : null
         * updateTimeEnd : null
         * id : 9
         * createTime : 1501071672000
         * updateTime : 1501071671000
         * articleCats : null
         * catId : 1
         * title : 开放商家入驻
         * isShow : True
         * keyw : null
         * staffId : 1
         * dataFlag : True
         * solve : null
         * unsolve : null
         * content : 感谢您注册使用本APP，为感谢您对我们产品的信赖，恭喜您获得本平台商家免费入驻资格，时间从现在起30日内。入驻详情请咨询平台客服或登陆官网留言。客服电话：4001508508  网址：www.weilaitongfu.com
         */

        private int page;
        private int size;
        private String createTimeStart;
        private String createTimeEnd;
        private String updateTimeStart;
        private String updateTimeEnd;
        private int id;
        private long createTime;
        private long updateTime;
        private String articleCats;
        private int catId;
        private String title;
        private String isShow;
        private String keyw;
        private int staffId;
        private String dataFlag;
        private String solve;
        private String unsolve;
        private String content;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public Object getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public Object getCreateTimeStart() {
            return createTimeStart;
        }

        public void setCreateTimeStart(String createTimeStart) {
            this.createTimeStart = createTimeStart;
        }

        public Object getCreateTimeEnd() {
            return createTimeEnd;
        }

        public void setCreateTimeEnd(String createTimeEnd) {
            this.createTimeEnd = createTimeEnd;
        }

        public Object getUpdateTimeStart() {
            return updateTimeStart;
        }

        public void setUpdateTimeStart(String updateTimeStart) {
            this.updateTimeStart = updateTimeStart;
        }

        public Object getUpdateTimeEnd() {
            return updateTimeEnd;
        }

        public void setUpdateTimeEnd(String updateTimeEnd) {
            this.updateTimeEnd = updateTimeEnd;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public Object getArticleCats() {
            return articleCats;
        }

        public void setArticleCats(String articleCats) {
            this.articleCats = articleCats;
        }

        public int getCatId() {
            return catId;
        }

        public void setCatId(int catId) {
            this.catId = catId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public Object getKeyw() {
            return keyw;
        }

        public void setKeyw(String keyw) {
            this.keyw = keyw;
        }

        public int getStaffId() {
            return staffId;
        }

        public void setStaffId(int staffId) {
            this.staffId = staffId;
        }

        public String getDataFlag() {
            return dataFlag;
        }

        public void setDataFlag(String dataFlag) {
            this.dataFlag = dataFlag;
        }

        public Object getSolve() {
            return solve;
        }

        public void setSolve(String solve) {
            this.solve = solve;
        }

        public Object getUnsolve() {
            return unsolve;
        }

        public void setUnsolve(String unsolve) {
            this.unsolve = unsolve;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

//    public int id;
//    public String type;
//    public String status;
//    public String title;
//    public String content;
//    public long createTime;




}
