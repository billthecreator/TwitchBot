import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jibble.pircbot.*;

public class TwitchBot extends PircBot{
    static TwitchBot bot = new TwitchBot();
    
    static final String MY_CHANNEL = "";
    static final String BOT_NAME = "";
    static final String TWITCH_IRC = "irc.twitch.tv";
    static final String TWITCH_OAUTH = "";
    static final int    TWITCH_PORT = 6667;
    static final String FILE_LOCATION = "questions.txt";
    
    static ArrayList<String> questionArray = new ArrayList<String>();
    static String currentQuestion = "";
    static double currentQuestionPosition = -1;

    public static void main(String[] args) throws Exception {
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        try {
            bot.connect(TWITCH_IRC, TWITCH_PORT, TWITCH_OAUTH);
        } catch (NickAlreadyInUseException e) {
            System.err.println("Nickname is currently in use");
        } catch (IrcException e) {
            System.err.println("Server did not accept connection");
            e.printStackTrace();
        }

        // Join the #pircbot channel.
        bot.joinChannel(MY_CHANNEL);
        
        // Get the questions from the file
        _getQuestions(FILE_LOCATION);
    }
    public TwitchBot(){
        this.setName(BOT_NAME);
    }
    @Override
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
        String[] messageArray = message.split(" ");
        String command = messageArray[0];

        if (sender.equalsIgnoreCase("rithek")){
            // Commands only for Rithek
            switch(command){
                case "!question":
                    sendMessage(channel, _getNextQuestions());
                    return;
                case "":
                    
                    return;
                default:break;

            }
        }
        
        switch(command){
            case "!currentquestion":
                sendMessage(channel, _getCurrentQuestion());
                return;
            default:break;

        }
    }
    /*
        _getQuestion
        with txt file, get the list of questions separated by a new line
    */
    private static void _getQuestions(String file) throws FileNotFoundException{
        
        Scanner inFile = new Scanner(new File(FILE_LOCATION));
        while (inFile.hasNextLine()){
            questionArray.add(inFile.next());
        }
        inFile.close();
    }
    private String _getNextQuestions(){
        
        // remove the last question from array
        if (currentQuestionPosition >= 0 ) {
            questionArray.remove(currentQuestionPosition);
        }
        // get a new random position
        int randomNumber = Integer.parseInt(Math.floor(Math.random() * questionArray.size()) + "");
        String randomQuestion = questionArray.get(randomNumber);
        
        currentQuestion = randomQuestion;
        currentQuestionPosition = randomNumber;
        return "";
    }
    private String _getCurrentQuestion(){
        return currentQuestion;
    }
    
}
