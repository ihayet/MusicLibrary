/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 *
 * @author ISHRAK-Ultra
 */
public class ArtistStore extends libraryInformation
{
    private static ReturnContainer returnContainer = new ReturnContainer();
    
    @FXML Label artistDescriptionLabel;
    @FXML Label artistAlbumLabel;
    
    private String description,origin,genres,yearsactive;
    private AlbumStore albumList;
    private ArtistStore nextArtist;
    private AnchorPane albumPane;
    
    private int numberOfAlbums;
    
    /**
     * Constructor for individual artists
     * 
     * @param id ArtistID
     * @param n Name of artist
     * @param d Description
     * @param o Origin
     * @param g Genres
     * @param y Years active
     */
    public ArtistStore(int id,String n,String d,String o,String g,String y)
    {
        super(id,n);
        description=d;
        origin=o;
        genres=g;
        yearsactive=y;
        
        nextArtist=null;
        albumPane=new AnchorPane();
    }
    
    public String getDescription()
    {
        return description;
    }
    public String getOrigin()
    {
        return origin;
    }
    public String getGenres()
    {
        return genres;
    }
    public String getYearsActive()
    {
        return yearsactive;
    }
    
    public void setNextArtist(int id,String n,String d,String o,String g,String y)
    {
        nextArtist=new ArtistStore(id,n,d,o,g,y);
    }
    public ArtistStore getNextArtist()
    {
        return nextArtist;
    }
    
    public void setAlbumList(AlbumStore firstAlbum)
    {
        albumList = firstAlbum;
    }
    public AlbumStore getAlbumList()
    {
        return albumList;
    }
    
    public void setAlbumPane(AnchorPane a)
    {
        albumPane=a;
    }
    public AnchorPane getAlbumPane()
    {
        return albumPane;
    }
    
    public void setNumberOfAlbums(int i)
    {
        numberOfAlbums=i;
    }
    public int getNumberOfAlbums()
    {
        return numberOfAlbums;
    }
    
    /**
     * Gets the artist information from the database. The artist information is stored using a lexicographic hashing i.e. these are stored in alphabetical
     * order. There are 26 elements in the artistList with the index representing the artist name's first letter. Each element in the artistList array is 
     * the first node of a linked list. The list expands if there are multiple artists with the same first letter. The nodes in the linked list are of
     * ArtistStore type. For every artist the corresponding album information is collected by the AlbumStore.getAlbumFromDatabase(integer artistID)
     * method.
     * 
     * @return artistList The array containing the artist information
     */
    public static ArtistStore[] getArtistFromDatabase()
    {
        ArtistStore[] artistList = new ArtistStore[26];
        
        for(ArtistStore iterator:artistList)
        {
            iterator=null;
        }
        
        Connection cn = null;
        ResultSet rs = null;
        
        int artistID;
        String name,description,origin,genres,yearsactive;
        
        ArtistStore currentIterator = null;
        
        try
        {
            MusicLibrary.getInstance().connectDB(returnContainer);
            cn=returnContainer.getConnection();
            
            PreparedStatement stmt = cn.prepareStatement("SELECT * FROM ARTISTTABLE");
            
            rs = stmt.executeQuery();
            
            while(rs.next())
            {
                artistID=rs.getInt("ARTISTID");
                name=rs.getString("NAMEOFARTIST");
                description=rs.getString("DESCRIPTION");
                origin=rs.getString("ORIGIN");
                genres=rs.getString("GENRES");
                yearsactive=rs.getString("YEARSACTIVE");
                
                if(artistList[name.charAt(0)-65]==null)
                {
                    artistList[name.charAt(0)-65]=new ArtistStore(artistID,name,description,origin,genres,yearsactive);
                    currentIterator=artistList[name.charAt(0)-65];
                }
                else                                                                //for multiple artists with the same first letter
                {
                    ArtistStore iterator = artistList[name.charAt(0)-65];
                    
                    while(iterator.getNextArtist()!=null)
                    {
                        iterator=iterator.getNextArtist();
                    }
                    
                    iterator.setNextArtist(artistID,name,description,origin,genres,yearsactive);
                    currentIterator=iterator.getNextArtist();
                }
                
                //Getting the link to the first album of each artist - start
                AlbumStore tempAlbumList = AlbumStore.getAlbumFromDatabase(artistID);
                
                currentIterator.setAlbumList(tempAlbumList);                            
                currentIterator.setNumberOfAlbums(AlbumStore.getAlbumCounter());
//                AlbumStore.showAlbumList(tempAlbumList);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Artist table exception: ");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            System.err.println("Artist table exception: ");
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
        
        return artistList;
    }
    
    /**
     * Displaying the artist information according to the lexicographic hashing. All the lists expanding from the array element are traversed. The nodes
     * which are not null, are printed.
     * 
     * @param artistList The array containing the artist information
     */
    public static void showArtistList(ArtistStore[] artistList)
    {
        for(ArtistStore iterator:artistList)
        {
            while(iterator!=null)
            {
                System.out.println("Artist id: " + iterator.getID() + "\nName of artist: " + iterator.getName() + "\nDescription: " + iterator.getDescription() + "\nOrigin: " + iterator.getOrigin() + "\nGenres: " + iterator.getGenres() + "\nYears active: " + iterator.getYearsActive());
                System.out.println();
                
                iterator=iterator.getNextArtist();
            }
        }
    }
    
    /**
     * Shows the library starting from artist information
     * 
     * @param artistList The array of artist information
     */
    public static void showLibrary(ArtistStore[] artistList)
    {
        for(ArtistStore iterator:artistList)
        {
            while(iterator!=null)
            {
                System.out.println("Artist id: " + iterator.getID() + "\nName of artist: " + iterator.getName() + "\nDescription: " + iterator.getDescription() + "\nOrigin: " + iterator.getOrigin() + "\nGenres: " + iterator.getGenres() + "\nYears active: " + iterator.getYearsActive());
                System.out.println();
                
                AlbumStore.showLibrary(iterator.getAlbumList());
                
                iterator=iterator.getNextArtist();
            }
        }
    }
    
    public static void createExpanders(ArtistStore[] artistList)
    {        
        for(ArtistStore iterator:artistList)
        {
            while(iterator!=null)
            {
                double xPos = 0.0;
                double yPos = 0.0;
                
                AlbumStore albumIterator = iterator.getAlbumList();
                Accordion a = new Accordion();

                while(albumIterator!=null)
                {
                    try
                    {
                        TitledPane t = new TitledPane();

                        t.setText(albumIterator.getName());
                        t.setStyle("-fx-font-size: 16; -fx-font-family: 'SketchFlow Print'; -fx-text-fill: black; -fx-background-color: transparent;");

                        albumIterator.setXPos(xPos);
                        albumIterator.setYPos(yPos);

                        SongStore songIterator = albumIterator.getSongList();

                        ListView l = new ListView();
                        l.setOrientation(Orientation.VERTICAL);
                        l.setStyle("-fx-font-size: 12; -fx-font-family: 'SketchFlow Print';");

                        int expansion = 0;

                        ObservableList songListItems = FXCollections.observableArrayList();

                        while(songIterator!=null)
                        {                        
                            songListItems.add(songIterator);

                            songIterator=songIterator.getNextSong();
                        }

                        l.setItems(songListItems);

                        l.setOnMouseClicked(new EventHandler<MouseEvent>()
                        {
                            @Override
                            public void handle(MouseEvent click)
                            {
                                if(click.getClickCount()==2)
                                {
                                    ListView l = (ListView)click.getSource();

                                    SongStore s = (SongStore)l.getSelectionModel().getSelectedItem();
                                    
                                    if(LibraryPageFXMLController.getInstance().getCurrentPlaylistTop()==null)
                                    {
                                        LibraryPageFXMLController.getInstance().setCurrentPlaylistTop(s);
                                    }
                                    else
                                    {
                                        LibraryPageFXMLController.getInstance().getCurrentPlaylistTop().setNextSong(s.getID(),s.getName(),s.getArtistID(),s.getAlbumID(),s.getLength(),s.getArtistName(),s.getAlbumName());
                                        LibraryPageFXMLController.getInstance().setCurrentPlaylistTop(LibraryPageFXMLController.getInstance().getCurrentPlaylistTop().getNextSong());
                                    }
                                    
                                    LibraryPageFXMLController.getInstance().addToPlaylist(LibraryPageFXMLController.getInstance().getCurrentPlaylistTop());
                                    LibraryPageFXMLController.getInstance().setPlaylistView(LibraryPageFXMLController.getInstance().getItemsList());
                                }
                            }
                        });

                        l.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                        t.setContent(l);
                        t.setExpanded(false);

                        setAdjustments(t,albumIterator.getXPos(),albumIterator.getYPos(),750.0,albumIterator.getNumberOfSongs()*38);

                        a.getPanes().add(t);

                        yPos+=30;

                        albumIterator=albumIterator.getNextAlbum();
                    }
                    catch(Exception e)
                    {
                        System.err.println("Exception: ");
                        e.printStackTrace();
                    }
                }
                iterator.getAlbumPane().getChildren().add(a);
                
                iterator=iterator.getNextArtist();
            }
        }
    }
    
    public static void setAdjustments(Region r,double xPos,double yPos,double width,double height)
    {
        r.setLayoutX(xPos);
        r.setLayoutY(yPos);
        r.setPrefWidth(width);
        r.setPrefHeight(height);
    }
    
    @Override
    public String toString()
    {
        return getName();
    }
}
