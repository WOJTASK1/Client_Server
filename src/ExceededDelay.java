public class ExceededDelay extends Exception{
    public ExceededDelay(){}
    public ExceededDelay(int message)
    {
        super("Given Delay is incorrect and equals: "+ message +". The limitation is 0-180.");
    }
}
