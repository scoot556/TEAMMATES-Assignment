package teammates.ui.pagedata;

import teammates.common.datatransfer.attributes.AccountAttributes;

public class InstructorProfileCreateFormUrlAjaxPageData extends PageData {

    public String formUrl;
    boolean isError;

    public InstructorProfileCreateFormUrlAjaxPageData(AccountAttributes account, String sessionToken, String url,
            boolean hasError) {
        super(account, sessionToken);
        formUrl = url;
        isError = hasError;
    }

}
