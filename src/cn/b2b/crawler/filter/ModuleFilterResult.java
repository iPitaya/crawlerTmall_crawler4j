package cn.b2b.crawler.filter;

public class ModuleFilterResult {
	private String name;
	private boolean result;
	private FilterResult filterResult;
	
	public ModuleFilterResult(String name, boolean result,
			FilterResult filterResult) {
		super();
		this.name = name;
		this.result = result;
		this.filterResult = filterResult;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public FilterResult getFilterResult() {
		return filterResult;
	}
	public void setFilterResult(FilterResult filterResult) {
		this.filterResult = filterResult;
	}

	@Override
	public String toString() {
		return "ModuleFilterResult [name=" + name + ", result=" + result
				+ ", filterResult=" + filterResult + "]";
	}	
	
}
