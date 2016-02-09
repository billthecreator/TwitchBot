/*
    TwitchBot using PIRCBOT
    Created by William R.
    
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.*;

public class TwitchBot extends PircBot{
    // initiate bot
    static TwitchBot bot = new TwitchBot();
    
    // channel settings
    static final String MY_CHANNEL = "";
    static final String MY_NAME = "";
    static final String BOT_NAME = "";
    static final String TWITCH_IRC = "irc.twitch.tv";
    static final String TWITCH_OAUTH = "";
    static final int    TWITCH_PORT = 6667;
    
    // question settings
    static final String FILE_LOCATION = "questions.txt";
    //
    static ArrayList<String> questionArray = new ArrayList();
    static String currentQuestion = "";
    static double currentQuestionPosition = -1;
    
    //
    static final String PB_FILE_LOCATION = "pb.txt";
    static ArrayList<String> PBsArray = new ArrayList();

    public static void main(String[] args) throws Exception {
        
        // Enable debugging output.
//        bot.setVerbose(true);
        
        // Connect
        // prevent duplication
        try {
            bot.connect(TWITCH_IRC, TWITCH_PORT, TWITCH_OAUTH);
        } catch (NickAlreadyInUseException e) {
            System.err.println("Nickname is currently in use");
        } catch (IrcException e) {
            System.err.println("Server did not accept connection");
            e.printStackTrace();
        }

        // Join the channel.
        bot.joinChannel(MY_CHANNEL);
        
        // Get the questions from the file
        _getQuestions(FILE_LOCATION);
    }
    
    
    public TwitchBot(){this.setName(BOT_NAME);}
    
    
    @Override
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
        String[] messageArray = message.split(" ");
        String command = messageArray[0];

        if (sender.equalsIgnoreCase(MY_NAME)){
            // Commands only for "MY_NAME"
            // should be host
            switch(command){
                case "!question":
                    // display the next question
                    sendMessage(channel, _getNextQuestion());
                    break;
                default:break;

            }
        }
        // commands for everyone in chat
        switch(command){
            case "!currentquestion": // display the current question
                sendMessage(channel, _getCurrentQuestion());
            case "!frames":
            case "!mac": // mac quotes
            case "!pb":
                try{
                    String type = messageArray[1];
                    _getPBs(PB_FILE_LOCATION);
                    switch(type){
                        case "apr":
                            sendMessage(channel,PBsArray.get(0) + "\n" +
                                                PBsArray.get(1));break;
                        case "425":      
                            sendMessage(channel,PBsArray.get(2));break;
                        case "arm":      
                            sendMessage(channel,PBsArray.get(3));break;
                        case "peak":     
                            sendMessage(channel,PBsArray.get(4));break;
                        case "snow":    
                            sendMessage(channel,PBsArray.get(5));break;
                        case "metro":    
                            sendMessage(channel,PBsArray.get(6));break;
                        case "intim":
                            sendMessage(channel,PBsArray.get(7));break;
                        case "throne":   
                            sendMessage(channel,PBsArray.get(8));break;
                        default:
                            sendMessage(channel,"!pb [apr, 425, arm, peak, snow, metro, intim, throne]");
                            break;
                    }
                } catch(ArrayIndexOutOfBoundsException e){                    
                    sendMessage(channel,"!pb [apr, 425, arm, peak, snow, metro, intim, throne]");
                } catch (FileNotFoundException ex) {
                }  
                
            case "":
            case "!qcommands":
                sendMessage(channel,    "!currentquestion\n" +
                                        "!pb [apr, 425, arm, peak, snow, metro, intim, throne]\n" +
                                        "");
            default:break;

        }
    }
    /*
        _getQuestion
        with txt file, get the list of questions separated by a new line
    */
    private static void _getQuestions(String file) throws FileNotFoundException{
        // Avoid apending
        questionArray.clear();
        // Try to read the file.
        // Cycle through each line, adding it to the array
        try (Scanner inFile = new Scanner(new File(file))) {
            while (inFile.hasNextLine()){
                questionArray.add(inFile.next());
            }
        } catch (FileNotFoundException e){
            System.err.println("Can't find the file: " + file);
        }
    }
    /*
        _getPBs
    */
    private static void _getPBs(String file) throws FileNotFoundException{
        // Avoid apending
        PBsArray.clear();
        // Try to read the file.
        // Cycle through each line, adding it to the array
        try (Scanner inFile = new Scanner(new File(file))) {
            while (inFile.hasNextLine()){
                PBsArray.add(inFile.next());
            }
        } catch (FileNotFoundException e){
            System.err.println("Can't find the file: " + file);
        }
    }
    /*
        _getNextQuestion
        Remove the last question, unless it's the first time.
        Randomly pick from the array and display that question,
        and set the current position to remove when this method is called
        again.
    */
    private String _getNextQuestion(){
        
        // remove the last question used from array
        if (currentQuestionPosition >= 0 ) {
            questionArray.remove(currentQuestionPosition);
        }
        // get a new random position
        int randomNumber = Integer.parseInt(
                Math.floor(Math.random() * questionArray.size()) + "");
        // grab question with random number ^
        
        try{
            String randomQuestion = questionArray.get(randomNumber);
            currentQuestion = randomQuestion;
            currentQuestionPosition = randomNumber;
        } catch(ArrayIndexOutOfBoundsException e){
            currentQuestion = "No more questions to display!";
            currentQuestionPosition = -1;
        }        
        
        // TODO - clear timer
        //      - set time for 5 minutes
        
        return currentQuestion;
    }
    /*
        _getCurrentQuestion
        Return the current question.
        // TODO - Needs better handling for spam
        //      - add a delay **
    */
    private String _getCurrentQuestion(){
        
        // Is timer cleared?
        // Is timer past 5 minutes?
        
        return currentQuestion;
    }
    
}
