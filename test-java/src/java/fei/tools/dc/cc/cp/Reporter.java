package fei.tools.dc.cc.cp;

import java.util.List;

import fei.tools.dc.cc.CompareResult;




public interface Reporter {
	void report(String name, List<CompareResult> compareResults);
}
