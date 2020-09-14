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
 * @author ISHRAK
 */
public class PlaylistInformation extends libraryInformation
{
    public PlaylistInformation(int i,String n)
    {
        super(i,n);
    }
    
    public SongStore[] getPlaylist(int i,int userID)
    {
        ReturnContainer retCont = new ReturnContainer();
        
        SongStore[] playlistSongs = null;
        int numberOfSongs = 0;
        int counter =0 ;
        
        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        
        try
        {
            MusicLibrary.getInstance().connectDB(retCont);
            
            con = retCont.getConnection();
            
            stmt = con.prepareStatement("select count(songid) number_of_songs from pcontainss where playlistid=? and userid=?");
            stmt.setInt(1,i);
            stmt.setInt(2,userID);
            
            rs = stmt.executeQuery();
            
            while(rs.next())
            {
                numberOfSongs = rs.getInt("number_of_songs");
            }
            
            playlistSongs = new SongStore[numberOfSongs];
            
            System.out.println("Playlist ID: " + i + " " + "User ID: " + userID);
            
            stmt1 = con.prepareStatement("select songtable.songid,songtable.songname,songtable.artistid,songtable.albumid,songtable.length,artisttable.nameofartist,albumtable.nameofalbum from pcontainss,songtable,artisttable,albumtable where pcontainss.playlistid=? and pcontainss.userid=? and pcontainss.artistid=artisttable.artistid and pcontainss.albumid=albumtable.albumid and pcontainss.artistid=songtable.artistid and pcontainss.albumid=songtable.albumid and pcontainss.songid=songtable.songid and songtable.artistid=artisttable.artistid and albumtable.artistid=artisttable.artistid and songtable.albumid=albumtable.albumid");
            stmt1.setInt(1,i);
            stmt1.setInt(2,userID);
            
            rs1 = stmt1.executeQuery();
            
            while(rs1.next())
            {
                int songID = rs1.getInt("songid");
                String songName = rs1.getString("songname");
                int artistID = rs1.getInt("artistid");
                int albumID = rs1.getInt("albumid");
                String length = rs1.getString("length");
                String artistname = rs1.getString("nameofartist");
                String albumname = rs1.getString("nameofalbum");
                
                playlistSongs[counter] = new SongStore(songID,songName,artistID,albumID,length,artistname,albumname);
                counter++;
            }
        }
        catch(SQLException ex)
        {
            System.err.println(ex.toString());
            ex.printStackTrace();
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
                ex.printStackTrace();
            }
        }
        
        return playlistSongs;
    }
    
    @Override
    public String toString()
    {
        return this.getName();
    }
}
