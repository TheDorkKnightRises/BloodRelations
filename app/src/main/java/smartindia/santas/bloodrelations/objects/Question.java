package smartindia.santas.bloodrelations.objects;

/**
 * Created by adityadesai on 01/04/17.
 */

public class Question {

    private String qNumber;
    private String qAnswer;

    public Question(String qNumber, String qAnswer) {
        this.qNumber = qNumber;
        this.qAnswer = qAnswer;
    }

    public String getqNumber() {
        return qNumber;
    }

    public String getqAnswer() {
        return qAnswer;
    }
}
