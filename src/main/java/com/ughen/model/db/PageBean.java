package com.ughen.model.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 
 * @author feifei
 * @描述: 分页组件.
 * @创建时间: 2013-7-25,下午11:33:41 .
 * @版本: 1.0 .
 */
@Data
public class PageBean<T> implements Serializable {
	private static final long serialVersionUID = 8470697978259453214L;
	/**
	 * 当前页
	 */
	private int currentPage;
	/**
	 * 每页显示多少条
	 */
	private int showCount;
	/**
	 * 总数量
	 */
	private int totalCount;
	/**
	 * 本页的数据列表
	 */
	private List<T> recordList;
	/**
	 * 总页数
	 */
	private int pageCount;
	/**
	 * 页码列表的开始索引（包含）
	 */
	private int beginPageIndex;
	/**
	 * 页码列表的结束索引（包含）
	 */
	private int endPageIndex;
	/**
	 * 当前分页条件下的统计结果
	 */
	private Map<String, Object> countResultMap;
	
	
	public PageBean(){}
	
	/**
	 * 只接受前4个必要的属性，会自动的计算出其他3个属生的值
	 * 
	 * @param currentPage
	 * @param numPerPage
	 * @param totalCount
	 * @param recordList
	 */
	public PageBean(int currentPage, int numPerPage, int totalCount, List<T> recordList) {
		this.currentPage = currentPage;
		this.showCount = numPerPage;
		this.totalCount = totalCount;
		this.recordList = recordList;

		// 计算总页码
		pageCount = (totalCount + numPerPage - 1) / numPerPage;

		// 计算 beginPageIndex 和 endPageIndex
		// >> 总页数不多于10页，则全部显示
		if (pageCount <= 10) {
			beginPageIndex = 1;
			endPageIndex = pageCount;
		}
		// >> 总页数多于10页，则显示当前页附近的共10个页码
		else {
			// 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
			beginPageIndex = currentPage - 4;
			endPageIndex = currentPage + 5;
			// 当前面的页码不足4个时，则显示前10个页码
			if (beginPageIndex < 1) {
				beginPageIndex = 1;
				endPageIndex = 10;
			}
			// 当后面的页码不足5个时，则显示后10个页码
			if (endPageIndex > pageCount) {
				endPageIndex = pageCount;
				beginPageIndex = pageCount - 10 + 1;
			}
		}
	}
	
	/**
	 * 只接受前5个必要的属性，会自动的计算出其他3个属生的值
	 * 
	 * @param currentPage
	 * @param numPerPage
	 * @param totalCount
	 * @param recordList
	 */
	public PageBean(int currentPage, int numPerPage, int totalCount, List<T> recordList, Map<String, Object> countResultMap) {
		this.currentPage = currentPage;
		this.showCount = numPerPage;
		this.totalCount = totalCount;
		this.recordList = recordList;
		this.countResultMap = countResultMap;

		// 计算总页码
		pageCount = (totalCount + numPerPage - 1) / numPerPage;

		// 计算 beginPageIndex 和 endPageIndex
		// >> 总页数不多于10页，则全部显示
		if (pageCount <= 10) {
			beginPageIndex = 1;
			endPageIndex = pageCount;
		}
		// >> 总页数多于10页，则显示当前页附近的共10个页码
		else {
			// 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
			beginPageIndex = currentPage - 4;
			endPageIndex = currentPage + 5;
			// 当前面的页码不足4个时，则显示前10个页码
			if (beginPageIndex < 1) {
				beginPageIndex = 1;
				endPageIndex = 10;
			}
			// 当后面的页码不足5个时，则显示后10个页码
			if (endPageIndex > pageCount) {
				endPageIndex = pageCount;
				beginPageIndex = pageCount - 10 + 1;
			}
		}
	}

}
