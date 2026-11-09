package Trivia;
public class QuestionReader
{

    public QuestionReader()
    {
        // TODO Auto-generated constructor stub
    }
    
    
    public String format(Question q, int number)
    {
        String result = "Question " + number + ": " + q.getPrompt() + "\n";
        
        String[] choices = q.getChoices();
        
        for (int i = 0; i < choices.length; i++)
        {
            result += (char)('A' + i) + ". " + choices[i] + "\n";
        }
        
        return result;
    }

}
