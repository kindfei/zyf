package fei.tools.dc.cc;

import fei.tools.dc.da.QueryInfo;

public interface CompareCase {
	QueryInfo getQueryInfo1();
	QueryInfo getQueryInfo2();
	String getDataAccessorName();
	void compare(FetchResult fr1, FetchResult fr2);
}
