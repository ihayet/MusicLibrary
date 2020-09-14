/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ISHRAK-Ultra
 */
public class LibraryPageFXMLController implements Initializable 
{
    ReturnContainer returnContainer = new ReturnContainer();
    
    @FXML AnchorPane anchorPaneLibrary;
    @FXML TabPane tab;
    @FXML BorderPane playlistPanel;
    @FXML ListView playlistView;
    @FXML BorderPane artistWikiPanel;
    @FXML ScrollPane artistWikiPane;
    @FXML AnchorPane homeTab,libraryTab,playlistTab,settingsTab,logoutTab;
    @FXML Label playlistLabel,artistWikiLabel;
    @FXML AnchorPane homePane,libraryPane,playlistPane,settingsPane;
    @FXML Label welcomeLabel,dateofbirthLabel,countryLabel,aboutLabel,sexLabel;
    @FXML ListView artistListView;
    @FXML Label artistWikiDescription,artistAlbumLabel;
    @FXML ScrollPane albumExpander;
    @FXML Label changeSettingsLabel,changeNameLabel,changeCountryLabel,changeAboutLabel,changeThemeLabel,changePasswordLabel,currentPasswordLabel;
    @FXML TextField changeNameText,changeCountryText;
    @FXML TextArea changeAboutText;
    @FXML ComboBox changeThemeCombo;
    @FXML PasswordField changePasswordText,currentPasswordText;
    @FXML Button changeSubmitButton;
    @FXML Label settingsNotification;
    @FXML TextField searchText;
    @FXML Button searchButton;
    @FXML Label searchResultLabel;
    @FXML Label savePlaylistLabel,nameLabel;
    @FXML Button saveButton;
    @FXML TextField nameText;
    @FXML Label loadPlaylistLabel;
    @FXML Label nameTextLabel;
    @FXML ComboBox loadPlaylistCombo;
    @FXML Button deleteButton;
    @FXML Label deletePlaylistLabel;
    @FXML Button logoutButton;
    @FXML Label recommendedSongLabel;
    @FXML ListView recommendedSongListView;
    @FXML AnchorPane musicPlayerAnchorPane;
    
    private ArtistStore[] artistList;
    
    private themeInformation[] themes = null;
    private int userID = 0, themeID = 0;
    private String themeURL = null, imgURL = null, backgroundColor = null, foregroundColor = null, foregroundForTransparentBackground = null, borderColor = null;
    private String nameOfUser = null, dateOfBirth = null, country = null, about = null, sex = null;
    private int changeSelectedThemeID = 0, lastSelectedThemeID = 0;
    private boolean artistLoaded;
    private String defaultWikiText = "About: \n\nOrigin: \n\nGenres: \n\nYears Active: ";
    private int playlistCounter = 0;
    private ArtistStore tempIterator = null;
    private SongStore currentPlaylistTop = null;
    
    private static ObservableList playlistItems = FXCollections.observableArrayList();
    private ObservableList artistListItems = FXCollections.observableArrayList();
    private ObservableList recommendedSongListItems = FXCollections.observableArrayList();
    
    MyTimer timerNotification = null;
    MyTimer timerResultLabel = null;
    MyTimer timerNameText = null;
    MyTimer timerDeletePlaylist = null;
    
    Rectangle2D screenRect;
    
    ImageView libraryImgView = new ImageView();
    
    private static LibraryPageFXMLController instance;
    
    MediaPlayer mediaPlayer = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        timerNotification = new MyTimer(settingsNotification);
        timerResultLabel = new MyTimer(searchResultLabel);
        timerNameText = new MyTimer(nameTextLabel);
        timerDeletePlaylist = new MyTimer(deletePlaylistLabel);

        MusicLibrary.getInstance().setGetScreenProperty(anchorPaneLibrary,returnContainer);
        
        screenRect = returnContainer.getScreenRect();
        
        MusicLibrary.getInstance().getCrossClassUserInformation(returnContainer);
        getUserInformation();
        
        System.out.println(userID + " " + themeID);
        
        lastSelectedThemeID=0;
        artistLoaded=false;
        
        getThemes();
        initScreen(themeID);
        tabSelectionAddListener();
        artistListViewAddListener();
        themeComboAddListener();
        playlistComboAddListener();
        addClickPressEvent();
        
        anchorPaneLibrary.getChildren().add(0,libraryImgView);
        
        setVisibility(true,false,false,false);                                //Only home pane is visible
    }
    
    public LibraryPageFXMLController()
    {
        instance=this;
    }
    public static LibraryPageFXMLController getInstance()
    {
        return instance;
    }
    
    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }
    
    public void setCurrentPlaylistTop(SongStore s)
    {
        currentPlaylistTop = s;
    }
    public SongStore getCurrentPlaylistTop()
    {
        return currentPlaylistTop;
    }
    
    public void addToPlaylist(SongStore item)
    {
        playlistItems.add(item);
        
        recommendedSongListItems.removeAll(recommendedSongListItems);
        
        ReturnContainer retCont = new ReturnContainer();
        
        MusicLibrary.getInstance().connectDB(retCont);
        
        Connection cn = retCont.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        
        int artistid,albumid,songid;
        String songname,songlength,artistname,albumname;
        
        try 
        {
            stmt = cn.prepareStatement("insert into currentplaylist values(?,?,?)");
            stmt.setInt(1,item.getArtistID());
            stmt.setInt(2,item.getAlbumID());
            stmt.setInt(3,item.getID());
            
            stmt.executeQuery();
            
            stmt = cn.prepareStatement("select * from recommendationview");
            
            rs = stmt.executeQuery();
            
            while(rs.next())
            {
                artistid = rs.getInt("artistid");
                albumid = rs.getInt("albumid");
                songid = rs.getInt("songid");
                songname = rs.getString("songname");
                songlength = rs.getString("length");
                artistname = rs.getString("nameofartist");
                albumname = rs.getString("nameofalbum");
                
                SongStore s = new SongStore(songid,songname,artistid,albumid,songlength,artistname,albumname);
                recommendedSongListItems.add(s);
            }
            
            recommendedSongListView.setItems(recommendedSongListItems);
        }
        catch (SQLException ex)
        {
            System.err.println(ex.toString());
        }
        finally
        {
            if(cn!=null)
            {
                try 
                {
                    cn.close();
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.toString());
                }
            }
            if(stmt!=null && stmt1!=null)
            {
                try 
                {
                    stmt.close();
                    stmt1.close();
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.toString());
                }
            }
            if(rs!=null)
            {
                try
                {
                    rs.close();
                }
                catch(SQLException ex)
                {
                    System.err.println(ex.toString());
                }
            }
        }   
    }
    
    public void removeFromPlaylist(SongStore item)
    {
        playlistItems.remove(item);
    }
    
    public void setPlaylistView(ObservableList list)
    {
        playlistView.setItems(list);
    }
    
    public ObservableList getItemsList()
    {
        return playlistItems;
    }
    
    public void savePlaylist()
    {
        if(nameText.getText().equals(""))
        {
            nameTextLabel.setText("No name selected!");
            
            timerNameText.startTimer(3000);
            
            return;
        }
        else if(playlistItems.isEmpty())
        {
            nameTextLabel.setText("No songs selected!");
            
            timerNameText.startTimer(3000);
            
            return;
        }
        
        ReturnContainer retCont = new ReturnContainer();
        
        Connection cn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtPlaylistID = null;
        ResultSet rs = null;
        ResultSet rsPlaylistID = null;
        
        MusicLibrary.getInstance().connectDB(retCont);
        
        try
        {
            cn = retCont.getConnection();
            stmt = cn.prepareStatement("insert into playlist values(0,?,?)");   
            stmt.setString(1,playlistLabel.getText());
            stmt.setInt(2,userID);
            stmt.executeQuery();
            
            stmtPlaylistID = cn.prepareStatement("select max(playlistID) highest_playlist from playlist where userID=?");
            stmtPlaylistID.setInt(1,userID);
            rsPlaylistID = stmtPlaylistID.executeQuery();
            
            while(rsPlaylistID.next())
            {
                playlistCounter = rsPlaylistID.getInt("highest_playlist");
            }
            
            for(Object o : playlistItems)
            {
                SongStore s = (SongStore)o;
                
                CallableStatement stmt1 = cn.prepareCall("{call fill_playlist(?,?,?,?,?)}");
                
                stmt1.setInt(1,playlistCounter);
                stmt1.setInt(2,userID);
                stmt1.setInt(3,s.getArtistID());
                stmt1.setInt(4,s.getAlbumID());
                stmt1.setInt(5,s.getID());
                
                stmt1.execute();
                
                stmt1.close();
                
                getPlaylistInformation();
                
                nameText.setText("");
            }
        }
        catch(SQLException ex)
        {
            System.err.println(ex.toString());
        }
        finally
        {
            try
            {
                if(cn!=null)
                {
                    cn.close();
                }
                if(stmt!=null)
                {
                    stmt.close();
                }
                if(stmtPlaylistID!=null)
                {
                    stmtPlaylistID.close();
                }
                if(rs!=null)
                {
                    rs.close();
                }
                if(rsPlaylistID!=null)
                {
                    rsPlaylistID.close();
                }
            }
            catch(SQLException ex)
            {
                System.err.println(ex.toString());
            }
        }
    }
    
    @FXML
    public void saveClicked(MouseEvent ev)
    {
        savePlaylist();
    }
    
    @FXML
    public void savePressed(KeyEvent ev)
    {
        if(ev.getSource().toString().equals("ENTER"))
        {
            savePlaylist();
        }
    }
    
    public void deletePlaylist()
    {
        if(loadPlaylistCombo.getSelectionModel().getSelectedItem()==null)
        {
            deletePlaylistLabel.setText("No Playlist Selected");
            
            timerDeletePlaylist.startTimer(3000);
            
            return;
        }
        
        ReturnContainer retCont = new ReturnContainer();
        
        Connection cn = null;
        int modifiedTuples = 0;
        
        MusicLibrary.getInstance().connectDB(retCont);
        cn = retCont.getConnection();
        
        PlaylistInformation pDelete = (PlaylistInformation)loadPlaylistCombo.getSelectionModel().getSelectedItem();
        
        try
        {
            CallableStatement stmt1 = cn.prepareCall("{? = call deletePlaylist(?,?)}");
            stmt1.registerOutParameter(1,oracle.jdbc.OracleTypes.INTEGER);
            stmt1.setInt(2,pDelete.getID());
            stmt1.setInt(3,userID);
            
            stmt1.executeUpdate();
            
            modifiedTuples = stmt1.getInt(1);
            
            getPlaylistInformation();
            
            playlistLabel.setText("Playlist");
            playlistItems.removeAll(playlistItems);
            playlistView.setItems(playlistItems);
        }
        catch(Exception ex)
        {
            System.err.println(ex.toString());
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if(cn!=null)
                {
                    cn.close();
                }
            }
            catch(Exception ex)
            {
                System.err.println(ex.toString());
            }
        }
    }
    
    @FXML
    public void deleteClicked(MouseEvent ev)
    {
        deletePlaylist();
    }
    
    @FXML
    public void deletePressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            deletePlaylist();
        }
    }
    
    public void setRecommendedSongListView(ObservableList list)
    {
        recommendedSongListView.setItems(list);
    }
    
    public ObservableList getRecommendedSongList()
    {
        return recommendedSongListItems;
    }
    
    private void getThemes()
    {
        Connection cn = null;
        ResultSet rs1 = null, rs2 = null;
        
        int numberOfThemes = 0, i;
        String name;
        
        try
        {
            MusicLibrary.getInstance().connectDB(returnContainer);
            cn = returnContainer.getConnection();
            
            //Get total number of themes to initialize the themeInformation array - start
            PreparedStatement stmtGetNumberOfThemes = cn.prepareStatement("SELECT COUNT(THEMEID) AS COUNTER FROM THEMETABLE");
            
            rs1 = stmtGetNumberOfThemes.executeQuery();
            
            if(rs1.next())
            {
                numberOfThemes = rs1.getInt("COUNTER");
            }
            
            System.out.println(numberOfThemes);
            
            themes = new themeInformation[numberOfThemes+1];
            //Get total number of themes to initialize the themeInformation array - end
            
            PreparedStatement stmtGetThemeInformation = cn.prepareStatement("SELECT * FROM THEMETABLE");
            
            rs2 = stmtGetThemeInformation.executeQuery();
            
            while(rs2.next())
            {
                i = rs2.getInt("THEMEID");
                name = rs2.getString("THEMENAME");
                imgURL = "resources/images/themes/"+rs2.getString("IMAGE");          //"file:" will be added at the beginning of this string in addImage
                themeURL = "file:resources/images/themes/"+rs2.getString("CSS");
                backgroundColor = rs2.getString("BACKGROUND");
                foregroundColor = rs2.getString("FOREGROUND");
                foregroundForTransparentBackground = rs2.getString("FOREGROUND1");
                borderColor = rs2.getString("BORDER");
                
                System.out.println(i+ " " + name + " " + imgURL + " " + themeURL + " " + backgroundColor + " " + foregroundColor + " " + foregroundForTransparentBackground);
                
                themes[i] = new themeInformation();
                
                themes[i].themeName = name;
                themes[i].imgURL = imgURL;
                themes[i].themeURL = themeURL;
                themes[i].backgroundColor = backgroundColor;
                themes[i].foregroundColor = foregroundColor;
                themes[i].foregroundForTransparentBackground = foregroundForTransparentBackground;
                themes[i].borderColor = borderColor;
                themes[i].themeID = i;
            }
        }
        catch(SQLException e)
        {
            System.err.println("Theme table exception: "+e.getMessage());
        }
        catch(Exception e)
        {
            System.err.println("Theme table exception: ");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                cn.close();
                rs1.close();
                rs2.close();
            }
            catch(Exception e)
            {
                System.err.println("Exception: "+e.getMessage());
            }
        }
    }
    
    private void getPlaylistInformation()
    {
        ReturnContainer retCont = new ReturnContainer();
        
        PlaylistInformation[] playlist = null;
        
        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        
        int numberOfPlaylists = 0;
        int counter = 0;
        
        try
        {
            MusicLibrary.getInstance().connectDB(retCont);
            
            con = retCont.getConnection();
            
            stmt = con.prepareStatement("select count(playlistid) number_of_playlists from playlist where userid=?");
            stmt.setInt(1,userID);
            
            rs = stmt.executeQuery();
            
            while(rs.next())
            {
                numberOfPlaylists = rs.getInt("number_of_playlists");
            }
            
            playlist = new PlaylistInformation[numberOfPlaylists];
            
            stmt1 = con.prepareStatement("select playlistid,name from playlist where userid=?");
            stmt1.setInt(1,userID);
            
            rs1 = stmt1.executeQuery();
            
            while(rs1.next())
            {
                int playlistID = rs1.getInt("playlistid");
                String name = rs1.getString("name");
                
                playlist[counter] = new PlaylistInformation(playlistID,name);
                counter++;
            }
            
            ObservableList playlistNames = FXCollections.observableArrayList();
            
            for(PlaylistInformation p:playlist)
            {
                playlistNames.add(p);
            }
            
            loadPlaylistCombo.setItems(playlistNames);
        }
        catch(SQLException ex)
        {
            System.err.println(ex.toString());
        }
        finally
        {
            try
            {
                if(con!=null)
                {
                    con.close();
                }
                if(stmt!=null)
                {
                    stmt.close();
                }
                if(stmt1!=null)
                {
                    stmt1.close();
                }
                if(rs!=null)
                {
                    rs.close();
                }
                if(rs1!=null)
                {
                    rs1.close();
                }
            }
            catch(SQLException ex)
            {
                System.err.println(ex.toString());
            }
        }
    }
    
    private void initScreen(int tID)
    {
        MusicLibrary.getInstance().addImage(themes[tID].imgURL, libraryImgView, screenRect, 0.0, 0.0);
        
        System.out.println("Screen: " + screenRect.getWidth() + " " + screenRect.getHeight());
        
        setAdjustments(playlistPanel,0.01*screenRect.getWidth(),10.0,0.25*screenRect.getWidth(),0.44*screenRect.getHeight()-38.0);
        playlistPanel.setStyle("-fx-border-color: " + themes[tID].borderColor + "; -fx-border-width: 10px;");
        playlistLabel.setStyle(" -fx-text-fill: " + themes[tID].foregroundForTransparentBackground + ";");
        
        setAdjustments(playlistView,playlistPanel.getLayoutX(),playlistPanel.getLayoutY(),playlistPanel.getPrefWidth(),playlistPanel.getPrefHeight()-58.0);
        playlistView.setStyle("-fx-font-size: 15; -fx-font-family: 'SketchFlow Print';");
        playlistViewRecommendedSongListViewAddListener();
        
        setAdjustments(artistWikiPanel,0.01*screenRect.getWidth(),playlistPanel.getLayoutY()+playlistPanel.getPrefHeight()+30.0,0.25*screenRect.getWidth(),0.44*screenRect.getHeight()-38.0);
        artistWikiPanel.setStyle("-fx-border-color: " + themes[tID].borderColor + "; -fx-border-width: 10px;");
        artistWikiLabel.setPrefHeight(100.0);
        artistWikiLabel.setStyle(" -fx-text-fill: " + themes[tID].foregroundForTransparentBackground + ";");
        
        setAdjustments(artistWikiPane,0.01*screenRect.getWidth(),artistWikiPanel.getLayoutY()+50.0,artistWikiPanel.getPrefWidth()-30.0,artistWikiPanel.getPrefHeight()-30.0);
        
        setAdjustments(artistWikiDescription,0.0,30.0,artistWikiPane.getPrefWidth()-30.0,1000.0);
        artistWikiDescription.setStyle("-fx-text-fill: " + themes[tID].foregroundForTransparentBackground + ";" + "-fx-font-size: 24; -fx-font-family: 'SketchFlow Print';");
        artistWikiDescription.setText(defaultWikiText);
        
        setAdjustments(logoutButton,0.01*screenRect.getWidth(),artistWikiPanel.getLayoutY()+artistWikiPanel.getPrefHeight()+10.0,playlistPanel.getPrefWidth(),0.89*screenRect.getHeight()-(playlistPanel.getPrefHeight()+30.0+artistWikiPanel.getPrefHeight()+10.0));
        String size = String.valueOf(logoutButton.getPrefHeight()-24.0);
        logoutButton.setStyle("-fx-font-size: " + size + "; -fx-font-family: 'SketchFlow Print'; -fx-background-color: linear-gradient(white," + themes[tID].borderColor + "); -fx-background-insets: 0,1,2;");
        
        setAdjustments(tab,0.27*screenRect.getWidth(),10.0,0.715*screenRect.getWidth(),0.89*screenRect.getHeight()-(logoutButton.getPrefHeight()+10.0));
        if(lastSelectedThemeID!=0)
        {
            tab.getStylesheets().remove(themes[lastSelectedThemeID].themeURL);
        }
        tab.getStylesheets().add(themes[tID].themeURL);
        lastSelectedThemeID=tID;
        tab.setStyle("-fx-border-width: 10px; -fx-border-color: " + themes[tID].borderColor + ";");
        
        double tabLayoutX = tab.getLayoutX();
        double tabLayoutY = tab.getLayoutY()+50;
        double tabWidth = tab.getPrefWidth();
        double tabHeight = tab.getPrefHeight()-50;
        
        initHome(tID,tabLayoutX,tabLayoutY,tabWidth,tabHeight);
        initLibrary(tID,tabLayoutX,tabLayoutY,tabWidth,tabHeight);
        initPlaylist(tID,tabLayoutX,tabLayoutY,tabWidth,tabHeight);
        initSettings(tID,tabLayoutX,tabLayoutY,tabWidth,tabHeight);
        
        /*
        setListCellPropertySongStore(playlistView,themes[tID].borderColor,playlistView.getPrefWidth(),30.0);
        setListCellPropertyArtistStore(artistListView,themes[tID].borderColor,100.0,30.0);
        setListCellPropertySongStore(recommendedSongListView,themes[tID].borderColor,recommendedSongListView.getPrefWidth(),30.0);
        */
    }
    
    public void initHome(int tID,double xPos,double yPos,double width,double height)
    {
        homeTab.setStyle("-fx-background-color: " + themes[tID].backgroundColor + "; -fx-opacity: 0.30;");
        setAdjustments(homePane,xPos,yPos,width,height);
        
        setAdjustments(welcomeLabel,50.0,50.0,900.0,50.0);
        welcomeLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        welcomeLabel.setText("Welcome, " + nameOfUser);
        
        setAdjustments(dateofbirthLabel,60.0,welcomeLabel.getLayoutY()+100.0,900.0,100.0);
        dateofbirthLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        dateofbirthLabel.setText("Date Of Birth:    " + dateOfBirth);
        
        setAdjustments(countryLabel,60.0,dateofbirthLabel.getLayoutY()+100.0,900.0,100.0);
        countryLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        countryLabel.setText("Country:          " + country);
        
        setAdjustments(aboutLabel,60.0,countryLabel.getLayoutY()+100.0,900.0,100.0);
        aboutLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        aboutLabel.setText("About:             " + about);
        
        setAdjustments(sexLabel,60.0,aboutLabel.getLayoutY()+100.0,900.0,100.0);
        sexLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        sexLabel.setText("Sex:               " + sex);
    }
    public void initLibrary(int tID,double xPos,double yPos,double width,double height)
    {
        libraryTab.setStyle("-fx-background-color: " + themes[tID].backgroundColor + "; -fx-opacity: 0.30;");
        setAdjustments(libraryPane,xPos,yPos,width,height);
        
        setAdjustments(searchText,50.0,30.0,750.0,50.0);
        
        setAdjustments(searchButton,searchText.getPrefWidth()+55.0,30.0,150.0,50.0);
        searchButton.setStyle("-fx-background-color: linear-gradient(white," + themes[tID].borderColor + "); -fx-background-insets: 0,1,2;");
        
        setAdjustments(searchResultLabel,100.0,searchText.getLayoutY()+60.0,750.0,10.0);
        searchResultLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        
        setAdjustments(artistAlbumLabel,50.0,searchText.getLayoutY()+searchText.getPrefHeight()+30.0,150.0,10.0);
        artistAlbumLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + "; -fx-font-size: 30; -fx-font-family: 'SketchFlow Print';");
        
        setAdjustments(albumExpander,tab.getPrefWidth()/4,artistAlbumLabel.getLayoutY()+artistAlbumLabel.getPrefHeight()+50.0,tab.getPrefWidth()/2,150.0);
        albumExpander.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        albumExpander.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        albumExpander.setVisible(false);
        
        setAdjustments(artistListView,albumExpander.getLayoutX(),albumExpander.getLayoutY()+albumExpander.getPrefHeight()+10.0,albumExpander.getPrefWidth(),50.0);
        artistListView.setStyle("-fx-font-size: 24; -fx-font-family: 'SketchFlow Print';");
        
        setAdjustments(recommendedSongLabel,artistAlbumLabel.getLayoutX(),artistListView.getLayoutY()+artistListView.getPrefHeight()+10.0,600.0,10.0);
        recommendedSongLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + "; -fx-font-size: 30; -fx-font-family: 'SketchFlow Print';");
        
        setAdjustments(recommendedSongListView,albumExpander.getLayoutX(),recommendedSongLabel.getLayoutY()+recommendedSongLabel.getPrefHeight()+50.0,albumExpander.getPrefWidth(),albumExpander.getPrefHeight()-40.0);
        recommendedSongListView.setStyle("-fx-text-fill: " + themes[tID].foregroundForTransparentBackground + "; -fx-font-size: 16; -fx-font-family: 'SketchFlow Print';");
        
        if(artistLoaded==false)
        {
            //Getting the library - start        
            artistList = ArtistStore.getArtistFromDatabase();
            ArtistStore.showLibrary(artistList);

            //Artist List View - start
            for(ArtistStore iterator:artistList)
            {
                while(iterator!=null)
                {
                    artistListItems.add(iterator);
                    iterator=iterator.getNextArtist();
                }
            }
            artistListView.setItems(artistListItems);

            //Album expander - start
            ArtistStore.createExpanders(artistList);
            artistLoaded=true;
        }
    }
    public void initPlaylist(int tID,double xPos,double yPos,double width,double height)
    {
        playlistTab.setStyle("-fx-background-color: " + themes[tID].backgroundColor + "; -fx-opacity: 0.30;");
        setAdjustments(playlistPane,xPos,yPos,width,height);
        
        setAdjustments(savePlaylistLabel,50.0,30.0,750.0,50.0);
        savePlaylistLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        
        setAdjustments(nameLabel,100.0,savePlaylistLabel.getLayoutY()+95.0,100.0,50.0);
        nameLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        
        setAdjustments(nameText,300.0,savePlaylistLabel.getLayoutY()+108.0,300.0,30.0);
        
        setAdjustments(nameTextLabel,nameText.getLayoutX()+nameText.getPrefWidth()+30.0,nameText.getLayoutY(),300.0,30.0);
        nameTextLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        
        setAdjustments(saveButton,300.0,nameText.getLayoutY()+50.0,100.0,30.0);
        
        setAdjustments(loadPlaylistLabel,50.0,saveButton.getLayoutY()+100.0,750.0,50.0);
        loadPlaylistLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        
        setAdjustments(loadPlaylistCombo,100.0,loadPlaylistLabel.getLayoutY()+100.0,300.0,30.0);
        loadPlaylistCombo.setStyle("-fx-font-size: 12; -fx-font-family: 'SketchFlow Print';");
        
        setAdjustments(deleteButton,100.0,loadPlaylistCombo.getLayoutY()+50.0,100.0,30.0);
        
        setAdjustments(deletePlaylistLabel,deleteButton.getLayoutX()+deleteButton.getPrefWidth()+50.0,
                deleteButton.getLayoutY(),300.0,30.0);
        deletePlaylistLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        
        getPlaylistInformation();
    }
    public void initSettings(int tID,double xPos,double yPos,double width,double height)
    {
        settingsTab.setStyle("-fx-background-color: " + themes[tID].backgroundColor + "; -fx-opacity: 0.30;");
        setAdjustments(settingsPane,xPos,yPos,width,height);
        
        double xLayout = 750-xPos-50.0;
        
        changeSettingsLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(changeSettingsLabel,50.0,30.0,750.0,50.0);
        
        changeNameLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(changeNameLabel,50.0,changeSettingsLabel.getLayoutY()+90.0,750.0,50.0);
        setAdjustments(changeNameText,xLayout,changeSettingsLabel.getLayoutY()+95.0,500.0,38.0);
        
        changeCountryLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(changeCountryLabel,50.0,changeNameLabel.getLayoutY()+50.0,750.0,50.0);
        setAdjustments(changeCountryText,xLayout,changeNameText.getLayoutY()+50.0,500.0,38.0);
        
        changeAboutLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(changeAboutLabel,50.0,changeCountryLabel.getLayoutY()+50.0,750.0,50.0);
        setAdjustments(changeAboutText,xLayout,changeCountryText.getLayoutY()+50.0,500.0,100.0);
        
        changeThemeLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(changeThemeLabel,50.0,changeAboutLabel.getLayoutY()+changeAboutText.getPrefHeight()+35.0,750.0,50.0);
        changeThemeCombo.setStyle("-fx-font-size: 12; -fx-font-family: 'SketchFlow Print'; -fx-background-color: linear-gradient(white," + themes[tID].borderColor + "); -fx-background-insets: 0,1,2;");
        setAdjustments(changeThemeCombo,xLayout,changeAboutText.getLayoutY()+changeAboutText.getPrefHeight()+35.0,300.0,38.0);
        
        ObservableList themeList = FXCollections.observableArrayList();
        for(themeInformation iterator:themes)
        {
            themeList.add(iterator);
        }
        changeThemeCombo.setItems(themeList);
        
        changePasswordLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(changePasswordLabel,50.0,changeThemeLabel.getLayoutY()+50.0,750.0,50.0);
        setAdjustments(changePasswordText,xLayout,changeThemeCombo.getLayoutY()+50.0,500.0,38.0);
        
        currentPasswordLabel.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(currentPasswordLabel,50.0,changePasswordLabel.getLayoutY()+50.0,750.0,50.0);
        setAdjustments(currentPasswordText,xLayout,changePasswordText.getLayoutY()+50.0,500.0,38.0);
        
        settingsNotification.setStyle("-fx-text-fill: " + themes[tID].foregroundColor + ";");
        setAdjustments(settingsNotification,xLayout,currentPasswordLabel.getLayoutY()+38.0,500.0,38.0);
        
        setAdjustments(changeSubmitButton,xLayout,currentPasswordLabel.getLayoutY()+75.0,150.0,10.0);
        changeSubmitButton.setStyle("-fx-font-size: 18; -fx-font-family: 'SketchFlow Print'; -fx-background-color: linear-gradient(white," + themes[tID].borderColor + "); -fx-background-insets: 0,1,2;");
    }
    
    public void logout()
    {        
        ReturnContainer retCont = new ReturnContainer();
        
        MusicLibrary.getInstance().connectDB(retCont);
        Connection cn = retCont.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        
        try 
        {
            stmt = cn.prepareStatement("delete from currentplaylist");
            stmt1 = cn.prepareStatement("delete from currentrecommendation");
            
            stmt.executeQuery();
            stmt1.executeQuery();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.toString());
        }
        finally
        {
            if(cn!=null)
            {
                try 
                {
                    cn.close();
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.toString());
                }
            }
            if(stmt!=null && stmt1!=null)
            {
                try 
                {
                    stmt.close();
                    stmt1.close();
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.toString());
                }
            }
        }
        
        MusicLibrary.getInstance().loadPage("Logout");
    }
    
    @FXML
    public void logoutClicked(MouseEvent ev)
    {
        logout();
    }
    
    @FXML
    public void logoutPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            logout();
        }
    }
    
    public void submit()
    {
        if(currentPasswordText.getText().equals(""))
        {
            settingsNotification.setText("Current password is required");
            timerNotification.startTimer(5000L);
        }
        else if(changePasswordText.getText().length()>0 && changePasswordText.getText().length()<6)
        {
            settingsNotification.setText("Password must be of at least 6 characters");
            timerNotification.startTimer(5000L);
        }
        else
        {
            String name,country,about,password;
            int themeID;
            
            if(changeNameText.getText().length()>0)
            {
                name=changeNameText.getText();
            }
            else
            {
                name=returnContainer.getNameOfUser();
            }
            if(changeCountryText.getText().length()>0)
            {
                country=changeCountryText.getText();
            }
            else
            {
                country=returnContainer.getCountry();
            }
            if(changeAboutText.getText().length()>0)
            {
                about=changeAboutText.getText();
            }
            else
            {
                about=returnContainer.getAbout();
            }
            if(changePasswordText.getText().length()>0)
            {
                password=changePasswordText.getText();
            }
            else
            {
                password=returnContainer.getPassword();
            }
            if(changeThemeCombo.getSelectionModel().getSelectedItem()!=null)
            {
                themeID=changeSelectedThemeID;
            }
            else
            {
                themeID=returnContainer.getThemeID();
            }
            
            submissionTask task = new submissionTask(name,country,about,password,returnContainer.getUserID(),themeID);
            
            Thread th = new Thread(task);
            th.setDaemon(false);
            
            th.start();
        }
    }
    
    @FXML
    public void submitClicked(MouseEvent ev)
    {
        submit();
    }
    
    @FXML
    public void submitPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            submit();
        }
    }
    
    public void nameTextChanged()
    {
        String name = nameText.getText().trim();
        
        playlistLabel.setText(name);
        
        if(nameText.getText().equals(""))
        {
            playlistLabel.setText("Playlist");
        }
    }
    
    @FXML
    public void nameTextPressed(KeyEvent ev)
    {
        nameTextChanged();
    }
    
    @FXML
    public void nameTextReleased(KeyEvent ev)
    {
        nameTextChanged();
    }
    
    public void searchTextChanged()
    {
        if(searchText.getText().length()==0)
        {
            
        }
    }
    
    @FXML
    public void searchTextPressed(KeyEvent ev)
    {
        searchTextChanged();
    }
    
    @FXML
    public void searchTextReleased(KeyEvent ev)
    {
        searchTextChanged();
    }
    
    public void search()
    {
        if(searchText.getText().length()>0)
        {
            String text = searchText.getText().trim();
            
            searchTask task = new searchTask(text);
            
            Thread th = new Thread(task);
            th.setDaemon(false);
            
            th.start();
        }
    }
    
    @FXML
    public void searchClicked(MouseEvent ev)
    {
        search();
    }
    
    @FXML
    public void searchPressed(KeyEvent ev)
    {
        if(ev.getCode().toString().equals("ENTER"))
        {
            search();
        }
    }
    
    public void getUserInformation()
    {
        userID = returnContainer.getUserID();
        nameOfUser = returnContainer.getNameOfUser();
        dateOfBirth = returnContainer.getDateOfBirth();
        country = returnContainer.getCountry();
        about = returnContainer.getAbout();
        sex = returnContainer.getSex();
        themeID = returnContainer.getThemeID();
    }
    
    public void tabSelectionAddListener()
    {
        tab.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>()
            {
                @Override
                public void changed(ObservableValue<? extends Tab> ov,Tab t,Tab t1)
                {
                    int selectedIndex = tab.getSelectionModel().getSelectedIndex();
                    
                    System.out.println(selectedIndex);
                    
                    if(selectedIndex==0)
                    {                        
                        setVisibility(true,false,false,false);
                    }
                    else if(selectedIndex==1)
                    {
                        setVisibility(false,true,false,false);
                    }
                    else if(selectedIndex==2)
                    {
                        setVisibility(false,false,true,false);
                    }
                    else if(selectedIndex==3)
                    {
                        setVisibility(false,false,false,true);
                    }
                }
            }
        );
    }
    
    public void artistListViewAddListener()
    {
        artistListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ArtistStore>()
                {
                    @Override
                    public void changed(ObservableValue<? extends ArtistStore> ov, ArtistStore a, ArtistStore a1)
                    {
                        ArtistStore iterator = (ArtistStore)artistListView.getSelectionModel().getSelectedItem();
                        
                        artistWikiDescription.setText("About: " + iterator.getDescription() + "\n\n" + "Origin: " + iterator.getOrigin() + "\n\n" + "Genres: " + iterator.getGenres() + "\n\n" + "Years Active: " + iterator.getYearsActive());
                        
                        albumExpander.setContent(iterator.getAlbumPane());
                        albumExpander.setVisible(true);
                    }
                }
        );
    }
    
    public void themeComboAddListener()
    {
        changeThemeCombo.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<themeInformation>()
            {
                @Override
                public void changed(ObservableValue<? extends themeInformation> ov,themeInformation t,themeInformation t1)
                {
                    try
                    {
                        Thread.sleep(1000L);
                        
                        themeInformation theme = (themeInformation)changeThemeCombo.getSelectionModel().getSelectedItem();

                        if(theme!=null)
                        {
                            initScreen(theme.themeID);
                            changeSelectedThemeID=theme.themeID;
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println("Exception: ");
                        e.printStackTrace();
                    }
                }
            }
        );
    }
    
    public void addClickPressEvent()
    {
        playlistView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent click)
            {
                if(click.getClickCount()==2)
                {
                    ListView l = (ListView)click.getSource();
                    
                    SongStore s = (SongStore)l.getSelectionModel().getSelectedItem();
                    
                    playSong(s);
                }
                else if(click.getClickCount()==1)
                {                    
                    ListView l = (ListView)click.getSource();
                    
                    SongStore s = (SongStore)l.getSelectionModel().getSelectedItem();
                    
                    updateArtistWiki(s);
                }
            }
        });
        
        playlistView.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ev)
            {
                if(ev.getCode().toString().equals("ENTER"))
                {
                    ListView l = (ListView)ev.getSource();
                    
                    SongStore s = (SongStore)l.getSelectionModel().getSelectedItem();
                    
                    playSong(s);   
                }
                else if(ev.getCode().toString().equals("DELETE"))
                {
                    ListView l = (ListView)ev.getSource();
                    
                    removeFromPlaylist((SongStore)l.getSelectionModel().getSelectedItem());
                    setPlaylistView(playlistItems);
                    
                    if(playlistItems.isEmpty())
                    {
                        currentPlaylistTop = null;
                        playlistLabel.setText("Playlist");
                        System.out.println("NULLIFIED");
                    }
                }
            }
        });
        
        recommendedSongListView.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent click)
            {
                if(click.getClickCount()==2)
                {
                    ListView l = (ListView)click.getSource();
                    
                    addToPlaylist((SongStore)l.getSelectionModel().getSelectedItem());
                    setPlaylistView(playlistItems);
                }
                else if(click.getClickCount()==1)
                {                    
                    ListView l = (ListView)click.getSource();
                    
                    SongStore s = (SongStore)l.getSelectionModel().getSelectedItem();
                    
                    updateArtistWiki(s);
                }
            }
        });
    }
    
    private void updateArtistWiki(SongStore s)
    {
        artistWikiUpdateTask t = new artistWikiUpdateTask(s);
        Thread th = new Thread(t);

        th.setDaemon(false);
        th.start();
    }
    
    private class artistWikiUpdateTask extends Task<Void>
    {
        private SongStore s = null;
        
        public artistWikiUpdateTask(SongStore _s)
        {
            s = _s;
        }
        
        @Override
        protected Void call()
        {
            try
            {                
                ArtistStore iterator = artistList[s.getArtistName().charAt(0)-65];

                while(true)
                {
                    if(iterator.getID()==s.getArtistID())
                    {                        
                        tempIterator = iterator;
                        
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                artistWikiDescription.setText("About: " + tempIterator.getDescription() + "\n\n" + "Origin: " + tempIterator.getOrigin() + "\n\n" + "Genres: " + tempIterator.getGenres() + "\n\n" + "Years Active: " + tempIterator.getYearsActive());
                            }
                        });
                        break;
                    }
                    iterator = iterator.getNextArtist();
                }

                return null;
            }
            catch(Exception ex)
            {
                System.err.println(ex.toString());
            }
            
            return null;
        }
    }
    
    public void playlistViewRecommendedSongListViewAddListener()
    {
        playlistView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<SongStore>()
        {
            @Override
            public void changed(ObservableValue<? extends SongStore> ov, SongStore s, SongStore s1)
            {
                updateArtistWiki(s1);
            }
        });
        
        recommendedSongListView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<SongStore>()
        {
            @Override
            public void changed(ObservableValue<? extends SongStore> ov, SongStore s, SongStore s1)
            {
                updateArtistWiki(s1);
            }
        });
    }
    
    public void playSong(SongStore s)
    {
        musicTask m = new musicTask(s);
        Thread th = new Thread(m);
        
        th.start();
    }
    
    public void playlistComboAddListener()
    {
        loadPlaylistCombo.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<PlaylistInformation>()
        {
            @Override
            public void changed(ObservableValue <? extends PlaylistInformation> ov, PlaylistInformation p, PlaylistInformation p1)
            {                
                try
                {
                    Thread.sleep(100);
                    
                    if(p1!=null)
                    {
                        playlistLabel.setText(p1.getName());
                        
                        playlistItems.removeAll(playlistItems);
                    
                        SongStore[] playlistSongs = p1.getPlaylist(p1.getID(),userID);
                        
                        currentPlaylistTop = null;

                        for(SongStore s:playlistSongs)
                        {
                            playlistItems.add(s);
                            
                            if(currentPlaylistTop == null)
                            {
                                currentPlaylistTop = s;
                            }
                            else
                            {
                                currentPlaylistTop.setNextSong(s.getID(),s.getName(),s.getArtistID(),s.getAlbumID(),s.getLength(),s.getArtistName(),s.getAlbumName());
                                currentPlaylistTop = currentPlaylistTop.getNextSong();
                            }
                        }

                        playlistView.setItems(playlistItems);
                    }
                }
                catch(InterruptedException ex)
                {
                    System.err.println(ex.toString());
                }                
            }
        });
    }
    
    public static void setAdjustments(Region r,double xPos,double yPos,double width,double height)
    {
        r.setLayoutX(xPos);
        r.setLayoutY(yPos);
        r.setPrefWidth(width);
        r.setPrefHeight(height);
    }
    
    public void setVisibility(boolean b1,boolean b2,boolean b3,boolean b4)
    {
        homePane.visibleProperty().setValue(b1);
        libraryPane.visibleProperty().setValue(b2);
        playlistPane.visibleProperty().setValue(b3);
        settingsPane.visibleProperty().setValue(b4);
    }
    
    private class themeInformation
    {
        public String themeName;
        public String imgURL;
        public String themeURL;
        public String backgroundColor;
        public String foregroundColor;
        public String foregroundForTransparentBackground;
        public String borderColor;
        public int themeID;
        
        @Override
        public String toString()
        {
            return themeName;
        }
    }
    
    private class submissionTask extends Task<Void>
    {
        private String name,country,about,password;
        int userID,themeID;
        
        public submissionTask(String n,String c,String a,String p,int u,int t)
        {
            name=n;
            country=c;
            about=a;
            password=p;
            userID=u;
            themeID=t;
        }
        
        @Override
        protected Void call()
        {
            Connection cn = null;
            
            try
            {
                MusicLibrary.getInstance().connectDB(returnContainer);
                cn=returnContainer.getConnection();
                
                PreparedStatement stmt = cn.prepareStatement("UPDATE USERINFO SET NAMEOFUSER=?,COUNTRY=?,ABOUT=?,THEMEID=?,PASSWORD=? WHERE USERID=?");
                stmt.setString(1,name);
                stmt.setString(2,country);
                stmt.setString(3,about);
                stmt.setInt(4,themeID);
                stmt.setString(5,password);
                stmt.setInt(6,userID);
                
                stmt.executeQuery();
            }
            catch(SQLException e)
            {
                System.err.println("Update exception: ");
                e.printStackTrace();
            }
            catch(Exception e)
            {
                System.err.println("Update exception: ");
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    cn.close();
                }
                catch(Exception e)
                {
                    System.err.println("Exception: ");
                    e.printStackTrace();
                }
            }
            
            return null;
        }
        
        @Override
        public void succeeded()
        {
            MusicLibrary.getInstance().loadPage("Changed Successfully");
        }
    }
    
    private class searchTask extends Task<Void>
    {
        private String text,songName,songLength,artistName,albumName,returnString = null;
        int songID,artistID,albumID;
        
        //MessageBox
        private AnchorPane messageBoxAnchorPane = new AnchorPane();
        
        private ListView searchResults = new ListView();
        private ObservableList resultItems = FXCollections.observableArrayList();
        private Button messageBoxButton = new Button();
        
        public searchTask(String t)
        {
            text=t;
        }
        
        @Override
        protected Void call()
        {
            Connection cn = null;
            ResultSet rs = null;
            
            try
            {
                MusicLibrary.getInstance().connectDB(returnContainer);
                cn=returnContainer.getConnection();
                
                PreparedStatement stmt = cn.prepareStatement("SELECT SONGNAME,LENGTH,NAMEOFARTIST,NAMEOFALBUM,SONGTABLE.SONGID,SONGTABLE.ARTISTID,SONGTABLE.ALBUMID,SONGTABLE.PRIORITY FROM SONGTABLE,ARTISTTABLE,ALBUMTABLE WHERE SONGNAME=? AND SONGTABLE.ALBUMID=ALBUMTABLE.ALBUMID AND SONGTABLE.ARTISTID=ALBUMTABLE.ARTISTID AND ARTISTTABLE.ARTISTID=ALBUMTABLE.ARTISTID ORDER BY PRIORITY DESC");
                stmt.setString(1,text);
                
                rs = stmt.executeQuery();
                
                while(rs.next())
                {
                    songName=rs.getString("SONGNAME");
                    songLength=rs.getString("LENGTH");
                    artistName=rs.getString("NAMEOFARTIST");
                    albumName=rs.getString("NAMEOFALBUM");
                    songID=rs.getInt("SONGID");
                    artistID=rs.getInt("ARTISTID");
                    albumID=rs.getInt("ALBUMID");
                    
                    returnString=songName + " " + songLength + " Artist: " + artistName + " Album: " + albumName + " " + artistID + " " + albumID + " " + songID;
                    
                    SongStoreDerived s = new SongStoreDerived(songID,songName,artistID,albumID,songLength,artistName,albumName);
                    resultItems.add(s);
                }
            }
            catch(SQLException e)
            {
                System.err.println("Search Exception: ");
                e.printStackTrace();
            }    
            catch(Exception e)
            {
                System.err.println("Search Exception: ");
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
                    System.err.println("Exception: ");
                    e.printStackTrace();
                }
            }
            
            return null;
        }
        
        @Override
        public void succeeded()
        {
            setAdjustments(messageBoxAnchorPane,0.0,0.0,600.0,180.0);
            setAdjustments(searchResults,messageBoxAnchorPane.getPrefWidth()/2-287.5,messageBoxAnchorPane.getPrefHeight()/2-75.0,575.0,100.0);
            searchResults.setItems(resultItems);
            searchResults.setStyle("-fx-font-size:15; -fx-font-family:'SketchFlow Print';");
            
            searchResults.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent click)
                {
                    if(click.getClickCount()==2)
                    {
                        ListView l = (ListView)click.getSource();
                        
                        SongStoreDerived s = (SongStoreDerived)l.getSelectionModel().getSelectedItem();
                        
                        SongStore s_store = new SongStore(s.getID(),s.getName(),s.getArtistID(),s.getAlbumID(),s.getLength(),s.getArtistName(),s.getAlbumName());
                        
                        LibraryPageFXMLController.getInstance().addToPlaylist(s_store);
                        LibraryPageFXMLController.getInstance().setPlaylistView(LibraryPageFXMLController.getInstance().getItemsList());
                    }
                }
            });
            
            messageBoxAnchorPane.getChildren().add(searchResults);
            
            Stage messageBoxStage = new Stage();
            
            setAdjustments(messageBoxButton,messageBoxAnchorPane.getPrefWidth()-100.0,searchResults.getLayoutY()+searchResults.getPrefHeight()+10.0,50.0,15.0);
            messageBoxButton.setText("OK");
            messageBoxButton.setStyle("-fx-font-size:18;-fx-font-family:'SketchFlow Print'");
            messageBoxButton.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
               @Override
               public void handle(MouseEvent ev)
               {
                   messageBoxStage.close();
               }
            });
            messageBoxButton.setOnKeyPressed(new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent ev)
                {
                    if(ev.getCode().toString().equals("ENTER"))
                    {
                        messageBoxStage.close();
                    }
                }
            });
            
            messageBoxAnchorPane.getChildren().add(messageBoxButton);
            
            Scene messageBoxScene = new Scene(messageBoxAnchorPane);
            
            messageBoxStage.setScene(messageBoxScene);
            messageBoxStage.setTitle("Results");
            messageBoxStage.initModality(Modality.WINDOW_MODAL);
            messageBoxStage.initOwner(MusicLibrary.getInstance().getGlobalStage());
            messageBoxStage.initStyle(StageStyle.DECORATED);
            
            messageBoxStage.show();
        }
    }
    
    private void setListCellPropertySongStore(ListView p,String c,double w,double h)
    {
        p.setCellFactory(new Callback<ListView<SongStore>,ListCell<SongStore>>()
        {
            @Override
            public ListCell<SongStore> call(ListView<SongStore> l)
            {
                return new ColorRectCellSongStore(c,w,h);
            }
        });
    }
    
    private static class ColorRectCellSongStore extends ListCell<SongStore>
    {
        String color = null;
        double width,height;
        
        public ColorRectCellSongStore(String c,double w,double h)
        {
            color = c;
            width = w;
            height = h;
        }
        
        @Override
        public void updateItem(SongStore item,boolean empty)
        {
            super.updateItem(item,empty);
            Rectangle rect = new Rectangle(width,height);
            
            if(item!=null)
            {
                rect.setFill(Color.AZURE);
                setGraphic(rect);
                setText(item.toString());
            }
        }
    }
    
    private void setListCellPropertyArtistStore(ListView p,String c,double w,double h)
    {
        p.setCellFactory(new Callback<ListView<ArtistStore>,ListCell<ArtistStore>>()
        {
            @Override
            public ListCell<ArtistStore> call(ListView<ArtistStore> l)
            {
                return new ColorRectCellArtistStore(c,w,h);
            }
            
        });
    }
    
    private static class ColorRectCellArtistStore extends ListCell<ArtistStore>
    {
        String color = null;
        double width,height;
        
        public ColorRectCellArtistStore(String c,double w,double h)
        {
            color = c;
            width = w;
            height = h;
        }
        
        @Override
        public void updateItem(ArtistStore item,boolean empty)
        {
            super.updateItem(item,empty);
            Rectangle rect = new Rectangle(width,height);
            
            if(item!=null)
            {
                rect.setFill(Color.BLANCHEDALMOND);
                setGraphic(rect);
                setText(item.toString());
            }
        }
    }
    
    private class musicTask extends Task<Void>
    {
        SongStore sPlay = null;
        
        public musicTask(SongStore s)
        {
            sPlay = s;
        }
        
        @Override
        protected Void call()
        {
            try
            {
                String applicationPath = System.getProperty("user.dir");
            
                File musicFile = new File(applicationPath,"/resources/music/"+sPlay.getArtistName()+"/"+sPlay.getName()+".mp3");
                URI musicURI = musicFile.toURI();
                String musicTest = musicURI.toString();

                Media hit = new Media(musicTest);
                
                if(mediaPlayer!=null)
                {
                    mediaPlayer.stop();
                }
                
                mediaPlayer = new MediaPlayer(hit);
                
                mediaPlayer.setOnEndOfMedia(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        sPlay = sPlay.getNextSong();
                        
                        if(sPlay!=null)
                        {
                            musicTask m = new musicTask(sPlay);
                            Thread th_next = new Thread(m);
                            
                            th_next.start();
                        }
                    }
                });
                
                mediaPlayer.play();
                
                System.err.println("Music Playing");
            }
            catch(Exception ex)
            {               
                System.err.println("Music Playing - Exception!");
            }
                     
            return null;
        }
    }
}
