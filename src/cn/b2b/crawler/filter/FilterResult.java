package cn.b2b.crawler.filter;

public class FilterResult {
	private boolean result;
	private String falseReason;
	private String trueReason;
	
	public FilterResult(boolean result, String falseReason, String trueReason) {
		super();
		this.result = result;
		this.falseReason = falseReason;
		this.trueReason = trueReason;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getFalseReason() {
		return falseReason;
	}
	public void setFalseReason(String falseReason) {
		this.falseReason = falseReason;
	}
	public String getTrueReason() {
		return trueReason;
	}
	public void setTrueReason(String trueReason) {
		this.trueReason = trueReason;
	}
	@Override
	public String toString() {
		return "FilterResult [result=" + result + ", falseReason="
				+ falseReason + ", trueReason=" + trueReason + "]";
	}
	
}
