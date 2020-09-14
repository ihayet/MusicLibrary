/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ISHRAK-Ultra
 */
public class MessageBoxFXMLController implements Initializable 
{
    private static MessageBoxFXMLController instance = null;
    
    @FXML Label message;
    @FXML Button proceed;
    @FXML Button cancel;
    
    private Stage stage = null;
    
    public MessageBoxFXMLController()
    {
        instance = this;
    }
    
    public static MessageBoxFXMLController getInstance()
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
        message.setText("THERE");
    }
    
    public void setMessage(String messageText)
    {
        message.setText(messageText);
    }
    
    public void setStage(Stage st)
    {
        stage = st;
    }
    
    public void setCancelDeactivate(boolean val)
    {
        cancel.setDisable(val);
    }
    
    public void loadLogin()
    {
        if(LibraryPageFXMLController.getInstance().getMediaPlayer()!=null)
        {
            LibraryPageFXMLController.getInstance().getMediaPlayer().stop();
        }
        
        MusicLibrary.getInstance().loadPage("Login");
        
        stage.close();
    }
    
    @FXML
    public void proceedClicked(MouseEvent ev)
    {
        loadLogin();
    }
    
    @FXML
    public void proceedPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            loadLogin();
        }
    }
    
    @FXML
    public void cancelClicked(MouseEvent ev)
    {
        stage.close();
    }
    
    @FXML
    public void cancelPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            stage.close();
        }
    }
}
