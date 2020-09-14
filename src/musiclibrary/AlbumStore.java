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

/**
 *
 * @author ISHRAK-Ultra
 */
public class AlbumStore extends libraryInformation
{
    private static ReturnContainer returnContainer = new ReturnContainer();
    
    private int yearofrelease,artistID;
    private SongStore songList;
    private AlbumStore nextAlbum;
    
    private double xPos,yPos;
    private int numberOfSongs;
    
    private static int albumCounter;
    
    /**
     * Constructor for storing album information
     * 
     * @param i AlbumID
     * @param y Year of release
     * @param artist ArtistID
     * @param n Name of album
     */
    public AlbumStore(int i,int y,int artist,String n)
    {
        super(i,n);
        yearofrelease=y;
        artistID=artist;
        nextAlbum=null;
    }
    
    public void setXPos(double i)
    {
        xPos=i;
    }
    public double getXPos()
    {
        return xPos;
    }
    public void setYPos(double i)
    {
        yPos=i;
    }
    public double getYPos()
    {
        return yPos;
    }
    public int getYearOfRelease()
    {
        return yearofrelease;
    }
    
    public void setNextAlbum(int i,int y,int artist,String n)
    {
        nextAlbum=new AlbumStore(i,y,artist,n);
    }
    public AlbumStore getNextAlbum()
    {
        return nextAlbum;
    }
    
    public void setSongList(SongStore firstSong)
    {
        songList=firstSong;
    }
    public SongStore getSongList()
    {
        return songList;
    }
    
    public void setNumberOfSongs(int i)
    {
        numberOfSongs=i;
    }
    public int getNumberOfSongs()
    {
        return numberOfSongs;
    }
    
    public static void setAlbumCounter(int i)
    {
        albumCounter=i;
    }
    public static int getAlbumCounter()
    {
        return albumCounter;
    }
    
    /**
     * Gets album information from the database. The album information is stored in a linked list. The first node of the linked list is returned to the
     * ArtistStore.getArtistFromDatabase() method. The list of albums is for a single artist.
     * 
     * @param artistID The artist whose albums have to be stored
     * @return firstAlbum The first node of the album linked list
     */
    public static AlbumStore getAlbumFromDatabase(int artistID)
    {
        AlbumStore firstAlbum = null, iterator = null;
        int albumCounter=0;
        
        Connection cn = null;
        ResultSet rs = null;
        
        int albumID,yearofrelease;
        String name;
        
        try
        {
            MusicLibrary.getInstance().connectDB(returnContainer);
            cn=returnContainer.getConnection();
            
            PreparedStatement stmt = cn.prepareStatement("SELECT ALBUMID,NAMEOFALBUM,YEAROFRELEASE FROM ARTISTTABLE,ALBUMTABLE WHERE ARTISTTABLE.ARTISTID=ALBUMTABLE.ARTISTID AND ALBUMTABLE.ARTISTID=?");
            stmt.setInt(1,artistID);
            
            rs=stmt.executeQuery();
            
            while(rs.next())
            {
                albumID=rs.getInt("ALBUMID");
                yearofrelease=rs.getInt("YEAROFRELEASE");
                name=rs.getString("NAMEOFALBUM");
                
                if(firstAlbum==null)
                {
                    firstAlbum=new AlbumStore(albumID,yearofrelease,artistID,name);
                    iterator=firstAlbum;
                }
                else
                {
                    iterator.setNextAlbum(albumID,yearofrelease,artistID,name);
                    iterator=iterator.getNextAlbum();
                }
                
                //Getting the link to the first song of each album - start
                SongStore tempSongList = SongStore.getSongFromDatabase(artistID,albumID);
                
                iterator.setSongList(tempSongList);
                iterator.setNumberOfSongs(SongStore.getSongCounter());
//                SongStore.showSongList(tempSongList);
                
                albumCounter++;
            }
            setAlbumCounter(albumCounter);
        }
        catch(SQLException e)
        {
            System.err.println("Album exception: ");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            System.err.println("Album exception: ");
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
        return firstAlbum;
    }
    
    /**
     * Displays the album information for one artist.
     * 
     * @param firstAlbum The first node of the linked list having the information of albums
     */
    public static void showAlbumList(AlbumStore firstAlbum)
    {
        AlbumStore iterator = firstAlbum;
        
        while(iterator!=null)
        {
            System.out.println("Album id: " + iterator.getID() + "\nAlbum name: " + iterator.getName() + "\nYear Of Release: " + iterator.getYearOfRelease());
            System.out.println();
            
            iterator=iterator.getNextAlbum();
        }
    }
    
    /**
     * Shows the library starting from album information
     * 
     * @param firstAlbum The link to the first node in the album linked list
     */
    public static void showLibrary(AlbumStore firstAlbum)
    {
        AlbumStore iterator = firstAlbum;
        
        while(iterator!=null)
        {
            System.out.println("Album id: " + iterator.getID() + "\nAlbum name: " + iterator.getName() + "\nYear Of Release: " + iterator.getYearOfRelease());
            System.out.println();
            
            SongStore.showLibrary(iterator.getSongList());
            
            iterator=iterator.getNextAlbum();
        }
    }
}
