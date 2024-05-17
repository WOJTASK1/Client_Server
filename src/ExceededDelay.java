public class ExceededDelay extends Exception{
    public ExceededDelay(int message)
    {
        super(STR."Given Delay is incorrect and equals: \{message}. The limitation is 0-180.");
    }
}
