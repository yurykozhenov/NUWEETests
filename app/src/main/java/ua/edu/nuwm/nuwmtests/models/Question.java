package ua.edu.nuwm.nuwmtests.models;

public class Question extends MongoDocument {
    public String question;
    public Boolean matchingQuestion;

    public Answer[] simpleQuestionAnswers;

    public int numberedAnswersQuantity;
    public int letteredAnswersQuantity;
    public String[][] table;
    public String[] matchingQuestionAnswers;
}
