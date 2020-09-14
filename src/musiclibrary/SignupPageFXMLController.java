/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;
import java.time.*;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.*;

/**
 * FXML Controller class
 *
 * @author ISHRAK-Ultra
 */
public class SignupPageFXMLController implements Initializable 
{
    private ReturnContainer returnContainer = new ReturnContainer();
    
    private static SignupPageFXMLController instance = null;
    
    @FXML AnchorPane anchorPaneSignup;
    
    @FXML TextField name;
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML DatePicker dateofbirth;
    @FXML TextField country;
    @FXML TextArea about;
    
    @FXML CheckBox termsandconditions;
    
    @FXML Button submit;
    @FXML Button back;
    
    @FXML Label nameNotification;
    @FXML Label usernameNotification;
    @FXML Label passwordNotification;
    @FXML Label dateofbirthNotification;
    @FXML Label sexNotification;
    @FXML Label countryNotification;
    @FXML Label termsandconditionsNotification;
    
    private String dateofbirthText, sexText;
    
    private boolean nameChecked = false, usernameChecked = false, passwordChecked = false, dateofbirthChecked = false, sexChecked = false, countryChecked = false, termsChecked = false;
    
    public SignupPageFXMLController()
    {
        instance = this;
    }
    
    public static SignupPageFXMLController getInstance()
    {
        return instance;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        try
        {            
            MusicLibrary.getInstance().setGetScreenProperty(anchorPaneSignup, returnContainer);
            
            Rectangle2D screenRect = returnContainer.getScreenRect();
            
            ImageView backgroundImgView = new ImageView();
            MusicLibrary.getInstance().addImage("resources/images/signup.png",backgroundImgView,screenRect,0,0);

            anchorPaneSignup.getChildren().add(0,backgroundImgView);
            
            initLabels();
        }
        catch(Exception e)
        {
            System.err.println("Initialize exception: ");
            e.printStackTrace();
        }
    }
    
    public void initLabels()
    {
        nameNotification.setText("");
        usernameNotification.setText("");
        passwordNotification.setText("");
        dateofbirthNotification.setText("");
        sexNotification.setText("");
        countryNotification.setText("");
        termsandconditionsNotification.setText("");
    }
    
    @FXML
    public void nameTextPressed(KeyEvent ev)
    {
        if(name.getText().length()>0)
        {
            nameChecked = true;
        }
        else
        {
            nameChecked = false;
        }
        
        if(nameNotification.getText().equals("*required"))
        {
            nameNotification.setText("");
        }
    }
    
    @FXML
    public void nameTextReleased(KeyEvent ev)
    {
        if(name.getText().length()>0)
        {
            nameChecked = true;
        }
        else
        {
            nameChecked = false;
        }
        
        if(nameNotification.getText().equals("*required"))
        {
            nameNotification.setText("");
        }
    }
    
    public void usernameDuplicateCheck(String nameText)
    {        
        String uName = nameText.trim();
        
        if(usernameNotification.getText().equals("*required"))
        {
            usernameNotification.setText("");
        }
        
        try
        {
            Thread.sleep(50l);
            
            usernameValidityCheck task = new usernameValidityCheck(uName);

            Thread th = new Thread(task);
            th.setDaemon(false);

            th.start();
        }
        catch(Exception e)
        {
            System.out.println("Username validity check exception: "+e.getMessage());
        }
    }
    
    @FXML
    public void usernameTextPressed(KeyEvent ev)
    {        
        System.out.println(username.getText());
        
        if(username.getText().length()==0)
        {
            usernameChecked = false;
        }
        else
        {
            usernameDuplicateCheck(username.getText());
        }
    }
    
    @FXML
    public void usernameTextReleased(KeyEvent ev)
    {
        System.out.println(username.getText());
        
        if(username.getText().length()==0)
        {
            usernameChecked = false;
        }
        else
        {
            usernameDuplicateCheck(username.getText());
        }
    }
    
    public void checkPasswordLength()
    {
        if(passwordNotification.getText().equals("*required"))
        {
           passwordNotification.setText(""); 
        }
        
        if(password.getText().length()>0 && password.getText().length()<6)
        {
            passwordNotification.setText("Password must be atleast six characters");
            passwordChecked = false;
        }
        else
        {
            passwordNotification.setText("");
            
            if(password.getText().length()>0)
            {
                passwordChecked = true;
            }
            else
            {
                passwordChecked = false;
            }
        }
    }
    
    @FXML
    public void passwordTextPressed(KeyEvent ev)
    {
        checkPasswordLength();
    }
    
    @FXML
    public void passwordTextReleased(KeyEvent ev)
    {
        checkPasswordLength();
    }
    
    @FXML
    public void dateofbirthAction(ActionEvent ev)
    {
        if(dateofbirthNotification.getText().equals("*required"))
        {
            dateofbirthNotification.setText("");
        }
        
        try
        {
            LocalDate dt = dateofbirth.getValue();
            
            if(dt!=null)
            {
                Month month = dt.getMonth();

                //Getting the first three letters of the month - start
                int i;
                char[] tempCharArray = new char[3];

                for(i=0;i<3;i++)
                {
                    tempCharArray[i] = month.toString().charAt(i);
                }

                String monthName = new String(tempCharArray);
                //Completed

                dateofbirthText = dt.getDayOfMonth() + "-" + monthName + "-" + dt.getYear();

                System.out.println(dateofbirthText);
                
                dateofbirthChecked = true;
            }
            else
            {
                dateofbirthChecked = false;
            }
        }
        catch(Exception e)
        {
            System.err.println("Date of birth exception: ");
            e.printStackTrace();
        }
    }
    
    @FXML 
    public void maleRadioClicked()
    {
        sexText = "MALE";
        sexChecked = true;
        sexNotification.setText("");
    }
    
    @FXML
    public void femaleRadioClicked()
    {
        sexText = "FEMALE";
        sexChecked = true;
        sexNotification.setText("");
    }
    
    @FXML
    public void countryTextPressed(KeyEvent ev)
    {
        if(country.getText().length()>0)
        {
            countryChecked = true;
        }
        else
        {
            countryChecked = false;
        }
        
        if(countryNotification.getText().equals("*required"))
        {
            countryNotification.setText("");
        }
    }
    
    @FXML
    public void countryTextReleased(KeyEvent ev)
    {
        if(country.getText().length()>0)
        {
            countryChecked = true;
        }
        else
        {
            countryChecked = false;
        }
        
        if(countryNotification.getText().equals("*required"))
        {
            countryNotification.setText("");
        }
    }
    
    @FXML
    public void termsandconditionsClicked(ActionEvent ev)
    {
        if(termsandconditions.isSelected())
        {
            termsandconditionsNotification.setText("");
            termsChecked = true;
        }
        else
        {
            termsandconditionsNotification.setText("");
            termsChecked = false;
        }
    }
    
    public void submitForm(String nameText, String uNameText, String pWordText, String countryText, String aboutText)
    {
        try
        {
            submissionTask taskSubmission = new submissionTask(nameText, uNameText, pWordText, dateofbirthText, sexText, countryText, aboutText, nameChecked, usernameChecked, passwordChecked, dateofbirthChecked, sexChecked, countryChecked, termsChecked);
            
            Thread th = new Thread(taskSubmission);
            th.setDaemon(false);

            th.start();
        }
        catch(Exception e)
        {
            System.err.println("Submit - exception: " + e.getCause());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void submitClicked(MouseEvent ev)
    {
        try
        {
            submitForm(name.getText(),username.getText(),password.getText(),country.getText(),about.getText());
        }
        catch(Exception e)
        {
            System.err.println("Submit call exception - ");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void submitPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            try
            {
                submitForm(name.getText(),username.getText(),password.getText(),country.getText(),about.getText());
            }
            catch(Exception e)
            {
                System.err.println("Submit call exception - ");
                e.printStackTrace();
            }
        }
    }
   
    @FXML
    public void backClicked(MouseEvent ev)
    {
        MusicLibrary.getInstance().loadPage("Login");
    }
    
    @FXML
    public void backPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            MusicLibrary.getInstance().loadPage("Login");
        }
    }
    
    private class usernameValidityCheck extends Task<Void>
    {
        int duplicateIDCounter;
        String usernameText = null;
        
        public usernameValidityCheck(String uName)
        {
            usernameText = uName;
            duplicateIDCounter = 0;
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
                
                PreparedStatement stmt = cn.prepareStatement("SELECT COUNT(USERID) AS COUNTER FROM USERINFO WHERE USERNAME=?");
                stmt.setString(1, usernameText);
                
                rs = stmt.executeQuery();
                
                if(rs.next())
                {
                    duplicateIDCounter = rs.getInt("COUNTER");
                }
            }
            catch(SQLException e)
            {
                System.err.println("Username valdiity check exception: "+e.getMessage());
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
                    System.err.println("Username valdiity check exception: "+e.getMessage());
                }
            }
            
            return null;
        }
        
        @Override
        public void succeeded()
        {            
            if(duplicateIDCounter>0)
            {
                usernameNotification.setText("Sorry! It's taken.");
                usernameChecked = false;
            }
            else
            {
                usernameNotification.setText("");
                usernameChecked = true;
            }
        }
    }
    
    private class submissionTask extends Task<Void>
    {
        String nameText1, usernameText1, passwordText1, dateofbirthText1, sexText1, countryText1, aboutText1;
        private boolean nameChecked1, usernameChecked1, passwordChecked1, dateofbirthChecked1, sexChecked1, countryChecked1, termsChecked1, signupCompleted;
        
        public submissionTask(String name, String uNameT, String pWordT, String birthdateT, String sexT, String countryT, String aboutT, boolean nameCheck, boolean uNameCheck, boolean pWordCheck, boolean birthdateCheck, boolean sexCheck, boolean countryCheck, boolean termsCheck)
        {
            nameText1 = name;
            usernameText1 = uNameT;
            passwordText1 = pWordT;
            dateofbirthText1 = birthdateT;
            sexText1 = sexT;
            countryText1 = countryT;
            aboutText1 = aboutT;
            
            nameChecked1 = nameCheck;
            usernameChecked1 = uNameCheck;
            passwordChecked1 = pWordCheck;
            dateofbirthChecked1 = birthdateCheck;
            sexChecked1 = sexCheck;
            countryChecked1 = countryCheck;
            termsChecked1 = termsCheck;
            
            signupCompleted = false;
        }
        
        @Override
        protected Void call() throws SQLException
        {
            if(validityCheck())
            {                
                Connection cn = null;
                ResultSet rs = null;
                
                int userIDCounter = 0;
                int themeID = 1;
                String dateFormat = "DD-MON-YYYY";
                
                try
                {
                    ReturnContainer returnContainerForJDBC = new ReturnContainer();
                    
                    MusicLibrary.getInstance().connectDB(returnContainerForJDBC);
                    cn = returnContainerForJDBC.getConnection();
                    
                    PreparedStatement stmtGetTotalUsers = cn.prepareStatement("SELECT COUNT(USERID) AS COUNTER FROM USERINFO");
                    rs = stmtGetTotalUsers.executeQuery();
                    
                    if(rs.next())
                    {
                        userIDCounter = rs.getInt("COUNTER") + 1;
                    }
                    
                    PreparedStatement stmtSignup = cn.prepareStatement("INSERT INTO USERINFO VALUES(?,?,?,?,?,TO_DATE(?,?),?,?,?)");
                    
                    stmtSignup.setInt(1, userIDCounter);
                    stmtSignup.setInt(2, themeID);
                    stmtSignup.setString(3, nameText1);
                    stmtSignup.setString(4, usernameText1);
                    stmtSignup.setString(5, passwordText1);
                    stmtSignup.setString(6, dateofbirthText1);
                    stmtSignup.setString(7, dateFormat);
                    stmtSignup.setString(8, countryText1);
                    stmtSignup.setString(9, aboutText1);
                    stmtSignup.setString(10, sexText1);
                    
                    stmtSignup.executeQuery();
                    signupCompleted = true;
                }
                catch(SQLException e)
                {
                    System.err.println("Signup exception: ");
                    e.printStackTrace();
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
                        System.err.println("Signup exception: ");
                        e.printStackTrace();
                    }
                }
            }
            
            return null;
        }
        
        private boolean validityCheck()
        {
            return (nameChecked1 & usernameChecked1 & passwordChecked1 & dateofbirthChecked1 & sexChecked1 & countryChecked1 & termsChecked1);
        }
        
        @Override
        protected void succeeded()
        {
            if(signupCompleted==false)
            {
                if(nameChecked1==false)
                {
                    nameNotification.setText("*required");
                }
                if(usernameChecked1==false)
                {
                    usernameNotification.setText("*required");
                }
                if(passwordChecked1==false)
                {
                    passwordNotification.setText("*required");
                }
                if(dateofbirthChecked1==false)
                {
                    dateofbirthNotification.setText("*required");
                }
                if(sexChecked1==false)
                {
                    sexNotification.setText("*required");
                }
                if(countryChecked1==false)
                {
                    countryNotification.setText("*required");
                }
                if(termsChecked1==false)
                {
                    termsandconditionsNotification.setText("*required");
                }
            }
            else
            {
                MusicLibrary.getInstance().loadPage("SuccessfulSignup");
            }
        }
    }
}
