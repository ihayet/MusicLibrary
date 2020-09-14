/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

/**
 *
 * @author ISHRAK
 */
public class SongStoreDerived extends SongStore
{
    public SongStoreDerived(int i,String n,int artist,int album,String l,String arName,String alName)
    {
        super(i,n,artist,album,l,arName,alName);
    }
    
    @Override
    public String toString()
    {
        return this.getName() + "            " + this.getLength() + "    " + this.getArtistName() + "    " + this.getAlbumName();
    }
}
