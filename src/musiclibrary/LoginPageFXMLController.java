/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import com.sun.deploy.Environment;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.concurrent.*;

/**
 * Controller class for the LoginPageFXML page
 * Multi-threading - implemented for the validity-check of login credentials in loginCheckTask
 * 
 * @author ISHRAK-Ultra
 */
public class LoginPageFXMLController implements Initializable 
{
    private ReturnContainer returnContainer = new ReturnContainer();
    
    @FXML private AnchorPane anchorPaneLogin;
    
    @FXML private Label notification;
    @FXML private TextField username;
    @FXML private PasswordField password;
    
    @FXML private Button login;
    @FXML private Button signup;
    
    MyTimer timerNotification, timerUsername, timerPassword;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO              
        initTimers();
        
        MusicLibrary.getInstance().setGetScreenProperty(anchorPaneLogin, returnContainer);
        
        Rectangle2D screenRect = returnContainer.getScreenRect();
//        Rectangle2D screenRect1 = new Rectangle2D(0,0,1000,560);
        
        ImageView backgroundImgView = new ImageView();
        MusicLibrary.getInstance().addImage("resources/images/login.jpg",backgroundImgView,screenRect,0.0,-45.0);                   //(URI,ImageView,height,width,xPos,yPos)
        
        setUsernamePasswordNotificationLoginSignupProperty(screenRect);                                                             //(Rectangle2D)
        
        anchorPaneLogin.getChildren().add(0, backgroundImgView);
    }
    
    /**
     * instantiates the timers
     */
    private void initTimers()
    {
        timerNotification = new MyTimer(3000L,notification);                        //Or, timerNotification = new MyTimer(notification);
        timerUsername = new MyTimer(1000L,username);                                //Or, timerUsername = new MyTimer(username);
        timerPassword = new MyTimer(1000L,password);                                //Or, timerPassword = new MyTimer(password);
    }
    
     /**
     * Sets the username text field, password password field, notification label, login button, sign up button properties(height,width,layoutX,layoutY) based on the screen size
     * 
     * @param screenRect The Rectangle2D object which contains screen size
     */
    private void setUsernamePasswordNotificationLoginSignupProperty(Rectangle2D screenRect)
    {
        double layoutX = 0.8*(screenRect.getWidth()/2);
        double layoutY = 0.68*(screenRect.getHeight());
        
        double width = 2*(screenRect.getWidth()/2-layoutX);
        double height = 0.018*(screenRect.getHeight());
        
        double notificationWidth = 0.21961932*(screenRect.getWidth());
        double notificationHeight = 0.00652741*(screenRect.getHeight());
        
        double distanceBetweenLoginSignup = 0.00366032*screenRect.getWidth();
        double buttonWidth = width/2 - distanceBetweenLoginSignup;
        
        double fontSizeUsernamePassword = 0.027*(screenRect.getHeight());
        double fontSizeNotification = 0.015*(screenRect.getHeight());
        
        double distanceBetweenUsernamePassword = height+ 0.04527415*(screenRect.getHeight());
        double distanceBetweenPasswordNotification = height + 0.04527415*(screenRect.getHeight());
        double distanceBetweenNotificationButton = 0.03*(screenRect.getHeight());
        
        Font fontUsernamePassword = new Font("SketchFlow Print",fontSizeUsernamePassword);
        Font fontNotification = new Font("System",fontSizeNotification);
        
        username.setPrefWidth(width);
        password.setPrefWidth(width);
        notification.setPrefWidth(notificationWidth);
        login.setPrefWidth(buttonWidth);
        signup.setPrefWidth(buttonWidth);
        
        username.setPrefHeight(height);
        password.setPrefHeight(height);
        notification.setPrefHeight(notificationHeight);
        login.setPrefHeight(height);
        signup.setPrefHeight(height);
        
        username.setLayoutX(layoutX);
        password.setLayoutX(layoutX);
        notification.setLayoutX(layoutX);
        login.setLayoutX(layoutX);
        signup.setLayoutX(layoutX+buttonWidth+(2*distanceBetweenLoginSignup));
        
        username.setLayoutY(layoutY);
        password.setLayoutY(layoutY+distanceBetweenUsernamePassword);
        notification.setLayoutY(password.getLayoutY()+distanceBetweenPasswordNotification);
        login.setLayoutY(notification.getLayoutY()+distanceBetweenNotificationButton);
        signup.setLayoutY(notification.getLayoutY()+distanceBetweenNotificationButton);
        
        username.setFont(fontUsernamePassword);
        password.setFont(fontUsernamePassword);
        notification.setFont(fontNotification);
        login.setFont(fontUsernamePassword);
        signup.setFont(fontUsernamePassword);
    }
    
    /**
     * Checks the database whether the provided username and password combination exists
     * 
     * @param cn database connection set by connectDB()
     * @param usernameText  text from the username Text Field
     * @param passwordText  text from the password Password Field
     * @return boolean whether the provided login credentials exist in the database
     */
    private void validLoginCheck(String usernameText,String passwordText)
    {        
        if(username.getText().equals("") && password.getText().equals(""))
        {
            notification.setText("Please provide username and password");
            
            timerNotification.startTimer(5000L);
        }
        else if(username.getText().equals(""))
        {
            notification.setText("Please provide username");
            
            timerNotification.startTimer(5000L);
        }
        else if(password.getText().equals(""))
        {
            notification.setText("Please provide password");
            
            timerNotification.startTimer(5000L);
        }
        else
        {            
            loginCheckTask task = new loginCheckTask(usernameText,passwordText);        //loginCheckTask extends javafx.concurrent.Task

            Thread th = new Thread(task);
            th.setDaemon(false);
            
            th.start();
        }
    }
    
    @FXML 
    public void loginClicked(MouseEvent ev)
    {      
        try
        {            
            validLoginCheck(username.getText(),password.getText());
        }
        catch(Exception e)
        {
            System.err.println("Login Pressed Exception: "+e.getMessage());
        }
    }
    
    @FXML 
    public void loginPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            try
            {
                validLoginCheck(username.getText(),password.getText());
            }
            catch(Exception e)
            {
                System.err.println("Login Pressed Exception: "+e.getMessage());
            }
        }
    }
    
    @FXML 
    public void signupClicked(MouseEvent ev)
    {        
        MusicLibrary.getInstance().loadPage("Signup");
    }
    
    @FXML 
    public void signupPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {            
            MusicLibrary.getInstance().loadPage("Signup");
        }
    }
    
    /**
     * Implements the login credentials validity check in a different thread. The database connectivity is established and the necessary check is performed
     * inside the new thread. As the elements in the application thread cannot be accessed from this new thread (Runtime Exception) when it is running, they are accessed
     * when the status of the Thread is either "SUCCEEDED" or "FAILED".
     */
    private class loginCheckTask extends Task<Void>
    {
        String usernameText,passwordText,nameOfUser,dateOfBirth,country,about,sex;
        private int validIDCount,userID,themeID;
        public boolean valid;
        
        public loginCheckTask(String uName,String pWord)
        {
            usernameText = uName;
            passwordText = pWord;
            
            validIDCount = 0;
            userID = 0;
            themeID = 0;
            valid = false;
        }
        
        @Override
        protected Void call() throws SQLException
        {
            Connection cn = null;
            ResultSet rs = null;

            try
            {
                MusicLibrary.getInstance().connectDB(returnContainer);
                cn = returnContainer.getConnection();

                PreparedStatement stmt = cn.prepareStatement("SELECT USERID,THEMEID,NAMEOFUSER,DOB,COUNTRY,ABOUT,SEX,COUNT(USERID) AS COUNTER FROM USERINFO WHERE USERNAME=? AND PASSWORD=? GROUP BY USERID,THEMEID,NAMEOFUSER,DOB,COUNTRY,ABOUT,SEX");
                stmt.setString(1, usernameText);
                stmt.setString(2, passwordText);

                rs = stmt.executeQuery();

                while(rs.next())
                {
                    userID=rs.getInt("USERID");
                    themeID=rs.getInt("THEMEID");
                    nameOfUser=rs.getString("NAMEOFUSER");
                    dateOfBirth=rs.getDate("DOB").toString();
                    country=rs.getString("COUNTRY");
                    about=rs.getString("ABOUT");
                    sex=rs.getString("SEX");
                    validIDCount=rs.getInt("COUNTER");
                }

                //System.out.println("The id counter is: "+idCount);
            }
            catch(SQLException e)
            {
                System.err.println("Error accessing USERINFO entity");
                System.err.println("Error: "+e.getMessage());
                
                valid=false;
            }
            finally
            {
                try
                {
                    cn.close();
                    rs.close();
                }
                catch(Exception e)
                {
                    System.err.println("Closing exception: "+e.getMessage());
                }
            }
            
            return null;
        }
        
        @Override
        public void succeeded()
        {            
            if(validIDCount==0)
            {
                notification.setText("Wrong username or password");

                timerNotification.startTimer(5000L);
                timerUsername.startTimer(500L);
                timerPassword.startTimer(500L);

                valid=false;
            }
            else if(validIDCount==1)
            {                
                notification.setText("Login successful");
                
                MusicLibrary.getInstance().setCrossClassUserInformation(userID,nameOfUser,dateOfBirth,country,about,sex,usernameText,passwordText,themeID);
                MusicLibrary.getInstance().loadPage("Library");

                valid=true;
            }
            else if(validIDCount>1)
            {
                notification.setText("Possible duplicate account");

                timerNotification.startTimer(5000L);
                timerUsername.startTimer(500L);
                timerPassword.startTimer(500L);

                valid=false;
            }
        }
        
        @Override
        public void failed()
        {
            notification.setText("Ooops, something went wrong");
            
            timerNotification.startTimer(5000L);
            
            super.failed();
        }
    }
}
