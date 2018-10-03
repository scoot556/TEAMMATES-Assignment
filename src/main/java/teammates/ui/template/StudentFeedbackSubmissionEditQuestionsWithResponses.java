package teammates.ui.template;

import java.util.List;

public class StudentFeedbackSubmissionEditQuestionsWithResponses {
    private FeedbackSubmissionEditQuestion question;
    private List<FeedbackSubmissionEditResponse> responses;
    private int numOfResponseBoxes;
    private int maxResponsesPossible;
    
    private String pdfAttachmentKey;

    public StudentFeedbackSubmissionEditQuestionsWithResponses(FeedbackSubmissionEditQuestion question,
                                    List<FeedbackSubmissionEditResponse> responses, int numOfResponseBoxes,
                                    int maxResponsesPossible) {
        this.question = question;
        this.responses = responses;
        this.numOfResponseBoxes = numOfResponseBoxes;
        this.maxResponsesPossible = maxResponsesPossible;
        
        this.pdfAttachmentKey = getPdfAttachmentKeyFromResponses(responses);
    }

    public FeedbackSubmissionEditQuestion getQuestion() {
        return question;
    }

    public List<FeedbackSubmissionEditResponse> getResponses() {
        return responses;
    }

    public int getNumOfResponseBoxes() {
        return numOfResponseBoxes;
    }

    public int getMaxResponsesPossible() {
        return maxResponsesPossible;
    }

    public String getPdfAttachmentKey() {
        return pdfAttachmentKey;
    }
    
    private String getPdfAttachmentKeyFromResponses(List<FeedbackSubmissionEditResponse> responses) {
        for (FeedbackSubmissionEditResponse response : responses) {
            if (response.getPdfAttachmentKey() != null) {
                return response.getPdfAttachmentKey();
            }
        }
        return null;
    }
    
    
}
