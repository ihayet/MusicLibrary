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
public class SongStore extends libraryInformation
{
    private static ReturnContainer returnContainer = new ReturnContainer();
    
    private int artistID,albumID;
    private String length;
    private String artistName,albumName;
    private SongStore nextSong;
    
    private static int songCounter;
    
    public SongStore(int i,String n,int artist,int album,String l,String arName,String alName)
    {
        super(i,n);
        artistID=artist;
        albumID=album;
        length=l;
        nextSong=null;
        artistName=arName;
        albumName=alName;
    }
    
    public String getLength()
    {
        return length;
    }
    
    public void setNextSong(int i,String n,int artist,int album,String l,String arName,String alName)
    {
        nextSong=new SongStore(i,n,artist,album,l,arName,alName);
    }
    public SongStore getNextSong()
    {
        return nextSong;
    }
    
    public static void setSongCounter(int i)
    {
        songCounter=i;
    }
    public static int getSongCounter()
    {
        return songCounter;
    }
    
    public int getArtistID()
    {
        return artistID;
    }
    public String getArtistName()
    {
        return artistName;
    }
    public int getAlbumID()
    {
        return albumID;
    }
    public String getAlbumName()
    {
        return albumName;
    }
    /**
     * Gets the song information from the database. The information is stored in a linked list. The first node of the linked list is returned to the
     * AlbumStore.getAlbumFromDatabase() method. The list of songs is for a single album.
     * 
     * @param artistID The artist whose albumID is received
     * @param albumID The album which is explored
     * @return 
     */
    public static SongStore getSongFromDatabase(int artistID,int albumID)
    {
        SongStore firstSong = null, iterator = null;
        int songCounter=0;
        
        Connection cn = null;
        ResultSet rs = null;
        
        int songID;
        String name,length,artistname,albumname;
        
        try
        {
            MusicLibrary.getInstance().connectDB(returnContainer);
            cn=returnContainer.getConnection();
            
            PreparedStatement stmt = cn.prepareStatement("SELECT SONGID,SONGNAME,LENGTH,NAMEOFARTIST,NAMEOFALBUM FROM ARTISTTABLE,ALBUMTABLE,SONGTABLE WHERE SONGTABLE.ARTISTID=ARTISTTABLE.ARTISTID AND ALBUMTABLE.ARTISTID=SONGTABLE.ARTISTID AND ALBUMTABLE.ALBUMID=SONGTABLE.ALBUMID AND SONGTABLE.ARTISTID=? AND SONGTABLE.ALBUMID=?");
            stmt.setInt(1,artistID);
            stmt.setInt(2,albumID);
            
            rs=stmt.executeQuery();
            
            while(rs.next())
            {
                songID=rs.getInt("SONGID");
                name=rs.getString("SONGNAME");
                length=rs.getString("LENGTH");
                artistname=rs.getString("NAMEOFARTIST");
                albumname=rs.getString("NAMEOFALBUM");
                
                if(firstSong==null)
                {
                    firstSong=new SongStore(songID,name,artistID,albumID,length,artistname,albumname);
                    iterator=firstSong;
                }
                else
                {
                    iterator.setNextSong(songID,name,artistID,albumID,length,artistname,albumname);
                    iterator=iterator.getNextSong();
                }
                
                songCounter++;
            }
            
            setSongCounter(songCounter);
        }
        catch(SQLException e)
        {
            System.err.println("Song exception: ");
            e.printStackTrace();
        }
        catch(Exception e)
        {
            System.err.println("Song exception: ");
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
        return firstSong;
    }
    
    /**
     * Displays the song information for one album
     * 
     * @param firstSong The first node of the linked list having the information of songs
     */
    public static void showSongList(SongStore firstSong)
    {
        SongStore iterator = firstSong;
        
        while(iterator!=null)
        {
            System.out.println("Song id: " + iterator.getID() + "\nSong name: " + iterator.getName() + "\nLength: " + iterator.getLength());
            System.out.println();
            
            iterator=iterator.getNextSong();
        }
    }
    
    /**
     * Shows the library starting from song information
     * 
     * @param firstSong The link to the first node in the song linked list
     */
    public static void showLibrary(SongStore firstSong)
    {
        SongStore iterator = firstSong;
        
        while(iterator!=null)
        {
            System.out.println("Song id: " + iterator.getID() + "\nSong name: " + iterator.getName() + "\nLength: " + iterator.getLength());
            System.out.println();
            
            iterator=iterator.getNextSong();
        }
    }
    
    @Override
    public String toString()
    {        
        return getName()+ "    " + getLength();
    }
}
