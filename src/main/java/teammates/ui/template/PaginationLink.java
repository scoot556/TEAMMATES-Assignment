package teammates.ui.template;

public class PaginationLink {
	private String label;
	private String url;
	
	public PaginationLink(String label, String url) {
		this.label = label;
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public String getUrl() {
		return url;
	}
	
	
}
