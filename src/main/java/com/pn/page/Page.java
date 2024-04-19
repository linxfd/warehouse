package com.pn.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 分页信息的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Page {

    //当前页码
    private Integer pageNum;
    //每一页需要显示的数据
    private Integer pageSize;
    //数据的总行数
    private Integer totalNum;
    //总页数
    private Integer pageCount;
    //limit（分页）函数的参数 -- 每一页的起始行
    private Integer limitIndex;
    //储存当前页查询到的数据的List<?>集合
    private List<?> resultList;

    //计算总页数
    public Integer getPageCount(){
        //例如：有50条数据，每页显示10条，总共就是5页；有53条数据，每页显示10条，总共6页
        return totalNum % pageSize == 0 ? totalNum/pageSize : totalNum/pageSize + 1;
    }

    //计算limit函数的起始行
    public Integer getLimitIndex(){
        //例如：一页5行，查询第三页，起始行是10
        return pageSize * (pageNum - 1);
    }

}
