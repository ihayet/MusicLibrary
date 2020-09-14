/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;


/**
 *
 * @author ISHRAK-Ultra
 */
public class MusicLibrary extends Application
{
    public static Pane rootLogin,rootSignup;
    public static Scene sceneLogin,sceneSignup,sceneMessageBox,sceneLibrary;
    public static Stage globalStage;
    
    private static MusicLibrary instance;
    
    @FXML AnchorPane anchorPaneLogin;
    @FXML AnchorPane anchorPaneSignup;
    @FXML AnchorPane anchorPaneMessage;
    @FXML AnchorPane anchorPaneLibrary;
    
    private int userID;
    private String nameOfUser,dateOfBirth,country,about,sex,username,password;
    private int themeID;
    
    //Access from Other classes - Non-static methods of this class can be called via an instance of this class, from other classes. But, we have to access the instance (of this class) using a static method.
    public MusicLibrary()
    {
        instance=this;      //this returns the instance of the current thread in which the program is running
    }
    
    public static MusicLibrary getInstance()
    {
        return instance;
    }
    //end
    
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            URL loginURL = new URL("file:src\\musiclibrary\\LoginPageFXML.fxml");           
            
            FXMLLoader loaderLogin = new FXMLLoader(loginURL);
            
            anchorPaneLogin = (AnchorPane)loaderLogin.load();
            sceneLogin = new Scene(anchorPaneLogin);
            
            primaryStage.setScene(sceneLogin);
            primaryStage.setTitle("Music Library");
            primaryStage.setMaximized(true);
            primaryStage.setResizable(true);
            
            primaryStage.show();
            
            globalStage = primaryStage;
            
            /*
            this.setCrossClassUserInformation(1,"A","B","C","D","E","F","G",1);
            this.loadPage("Library");
            */
        }
        catch(Exception e)
        {
            System.err.println("Create Scene Exception: ");
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Stage getGlobalStage()
    {
        return globalStage;
    }
    
    public void loadPage(String pageName)
    {        
        try
        {
            switch (pageName) 
            {
                case "Signup":
                    
                    try
                    {
                        URL signupURL = new URL("file:src\\musiclibrary\\SignupPageFXML.fxml");
                        
                        FXMLLoader loaderSignup = new FXMLLoader(signupURL);
                        
                        anchorPaneSignup = (AnchorPane)loaderSignup.load();
                        sceneSignup = new Scene(anchorPaneSignup);
                        
                        globalStage.setScene(sceneSignup);
                    }
                    catch(Exception e)
                    {
                        System.err.println("Signup loading exception: ");
                        e.printStackTrace();
                    }
                    
                    break;
                    
                case "Login":
                    
                    LibraryPageFXMLController.getInstance().getItemsList().removeAll(LibraryPageFXMLController.getInstance().getItemsList());
                    LibraryPageFXMLController.getInstance().getRecommendedSongList().removeAll(LibraryPageFXMLController.getInstance().getRecommendedSongList());
                    
                    try
                    {
                        URL loginURL = new URL("file:src\\musiclibrary\\LoginPageFXML.fxml");           
            
                        FXMLLoader loaderLogin = new FXMLLoader(loginURL);
                        
                        anchorPaneLogin = (AnchorPane)loaderLogin.load();
                        sceneLogin = new Scene(anchorPaneLogin);

                        globalStage.setScene(sceneLogin);
                    }
                    catch(Exception e)
                    {
                        System.err.println("Login Exception: "+e.getMessage());
                        e.printStackTrace();
                    }
                    
                    break;
                    
                case "SuccessfulSignup":
                    
                    try
                    {
                        URL messageBoxURL = new URL("file:src\\musiclibrary\\MessageBoxFXML.fxml");
                            
                        FXMLLoader loaderMessageBox = new FXMLLoader(messageBoxURL);

                        anchorPaneMessage = (AnchorPane)loaderMessageBox.load();

                        sceneMessageBox = new Scene(anchorPaneMessage);

                        Stage messageStage = new Stage();

                        messageStage.setScene(sceneMessageBox);
                        messageStage.setTitle("Welcome");
                        messageStage.initModality(Modality.WINDOW_MODAL);
                        messageStage.initOwner(globalStage);

                        MessageBoxFXMLController.getInstance().setStage(messageStage);
                        MessageBoxFXMLController.getInstance().setCancelDeactivate(true);
                        MessageBoxFXMLController.getInstance().setMessage("You have been successfully signed up");

                        messageStage.show();
                    }
                    catch(Exception e)
                    {
                        System.err.println("Message Box exception: "+e.getMessage());
                        e.printStackTrace();
                    }
                    
                    break;
                    
                case "Library":
                    
                    try
                    {
                        URL libraryURL = new URL("file:src\\musiclibrary\\LibraryPageFXML.fxml");
                        
                        FXMLLoader loaderLibrary = new FXMLLoader(libraryURL);
                        
                        anchorPaneLibrary = (AnchorPane)loaderLibrary.load();
                        sceneLibrary = new Scene(anchorPaneLibrary);
                        
                        globalStage.setScene(sceneLibrary);
                    }
                    catch(Exception e)
                    {
                        System.err.println("Library exception: "+e.getMessage());
                        e.printStackTrace();
                    }
                
                    break;
                    
                    case "Logout":
                    
                    try
                    {
                        URL messageBoxURL = new URL("file:src\\musiclibrary\\MessageBoxFXML.fxml");
                            
                        FXMLLoader loaderMessageBox = new FXMLLoader(messageBoxURL);

                        anchorPaneMessage = (AnchorPane)loaderMessageBox.load();

                        sceneMessageBox = new Scene(anchorPaneMessage);

                        Stage messageStage = new Stage();

                        messageStage.setScene(sceneMessageBox);
                        messageStage.setTitle("We'll miss you");
                        messageStage.initModality(Modality.WINDOW_MODAL);
                        messageStage.initOwner(globalStage);

                        MessageBoxFXMLController.getInstance().setStage(messageStage);
                        MessageBoxFXMLController.getInstance().setCancelDeactivate(false);
                        MessageBoxFXMLController.getInstance().setMessage("Are you sure you want to logout?");

                        messageStage.show();
                    }
                    catch(Exception e)
                    {
                        System.err.println("Message Box exception: "+e.getMessage());
                        e.printStackTrace();
                    }
                    
                    break;
                    
                    case "Changed Successfully":
                    
                    try
                    {
                        URL messageBoxURL = new URL("file:src\\musiclibrary\\MessageBoxFXML.fxml");
                            
                        FXMLLoader loaderMessageBox = new FXMLLoader(messageBoxURL);

                        anchorPaneMessage = (AnchorPane)loaderMessageBox.load();

                        sceneMessageBox = new Scene(anchorPaneMessage);

                        Stage messageStage = new Stage();

                        messageStage.setScene(sceneMessageBox);
                        messageStage.setTitle("Logging Out");
                        messageStage.initModality(Modality.WINDOW_MODAL);
                        messageStage.initOwner(globalStage);

                        MessageBoxFXMLController.getInstance().setStage(messageStage);
                        MessageBoxFXMLController.getInstance().setCancelDeactivate(true);
                        MessageBoxFXMLController.getInstance().setMessage("Logging out to validate the changes");

                        messageStage.show();
                    }
                    catch(Exception e)
                    {
                        System.err.println("Message Box exception: "+e.getMessage());
                        e.printStackTrace();
                    }
                    
                    break;
                
            }
        }
        catch(Exception e)
        {
            System.err.println("Load Page Exception: ");
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the screen size and sets the size of the anchor pane
     * 
     * @param pane The Anchor Pane which has to resized according to the screen size
     * @param retContainer ReturnContainer object - it is used to set and get the Rectangle2D object (containing the screen size)
     */
    public void setGetScreenProperty(AnchorPane pane,ReturnContainer retContainer)
    {
        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D screenRect = primaryScreen.getBounds();
        
        retContainer.setScreenRect(screenRect);
        
        double anchorOffsetX = 0.008*screenRect.getWidth();
        double anchorOffsetY = 0.08*screenRect.getHeight();
        
        pane.setMinHeight(screenRect.getHeight()*0.65274151);
        pane.setMinWidth(screenRect.getWidth()*0.73206442);
        
        pane.setPrefHeight(screenRect.getHeight()-anchorOffsetY);
        pane.setPrefWidth(screenRect.getWidth()+anchorOffsetX);
    }
    
    /**
    *This method adds an image to the background of the login page
    *
    *@param path The URI of the image 
    *@param imgView The image view which will hold the image
    *@param screenRect The Rectangle2D object which holds the screen size
    *@param xPos The xPos (WRT the top-left corner)
    *@param yPos (WRT the top-left corner)
    */
    public void addImage(String path,ImageView imgView,Rectangle2D screenRect,double xPos,double yPos)
    {      
        Image img = new Image("file:"+path);
        
        imgView.setImage(img);
        
        double width = screenRect.getWidth();
        double height = screenRect.getHeight();
        
        double imageOffsetX = 0.008*width;
        double imageOffsetY = 0.02*height;
        
        imgView.setLayoutX(xPos);
        imgView.setLayoutY(yPos);
        imgView.setFitHeight(height-imageOffsetY);
        imgView.setFitWidth(width+imageOffsetX);
        
        imgView.setSmooth(true);
        imgView.setCache(true);
    }
    
    /**
    *This method adds an image to the background of the login page
    *
    *@param path The URI of the image 
    *@param imgView The image view which will hold the image
    *@param height The height of the image
    *@param width The width of the image
    *@param xPos The xPos (WRT the top-left corner)
    *@param yPos (WRT the top-left corner)
    */
    public void addImage(String path,ImageView imgView,double height,double width,double xPos,double yPos)
    {      
        Image img = new Image("file:"+path);
        
        imgView.setImage(img);
        
        double imageOffsetX = 0.008*width;
        double imageOffsetY = 0.02*height;
        
        imgView.setLayoutX(xPos);
        imgView.setLayoutY(yPos);
        imgView.setFitHeight(height-imageOffsetY);
        imgView.setFitWidth(width+imageOffsetX);
        
        imgView.setSmooth(true);
        imgView.setCache(true);
    } 
    
     /**
     * Loads the static block in the driver class and gets the connection with the specified database URL
     * 
     * @param retContainer ReturnContainer object - it is used to set and get the Connection object (with the database)
     */
    public void connectDB(ReturnContainer retContainer)
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","admin","admin");
            
            retContainer.setConnection(cn);
        }
        catch(Exception e)
        {
            System.err.println("Error in opening database: "+e.getMessage());
        }
    }
    
    /**
     * This method sets the userID and themeID for cross-class access
     * 
     * @param uID userID
     * @param n name of user
     * @param d date of birth
     * @param c country
     * @param a about
     * @param s sex
     * @param tID themeID
     */
    public void setCrossClassUserInformation(int uID, String n, String d, String c, String a, String s, String u, String p, int tID)
    {
        userID = uID;
        nameOfUser = n;
        dateOfBirth = d;
        country = c;
        about = a;
        sex = s;
        username = u;
        password = p;
        themeID = tID;
    }
    
    /**
     * This method gets the user information and stores in the retContainer for cross-class access
     * 
     * @param retContainer the ReturnContainer object to store the user information
     */
    public void getCrossClassUserInformation(ReturnContainer retContainer)
    {
        retContainer.setUserInformation(userID,nameOfUser,dateOfBirth,country,about,sex,username,password,themeID);
    }
}
