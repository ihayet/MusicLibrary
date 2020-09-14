/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import java.sql.Connection;
import javafx.geometry.Rectangle2D;

/**
 *
 * @author ISHRAK-Ultra
 */
public class ReturnContainer 
{    
    private Rectangle2D screenRect = null;
    private Connection cn = null;
    
    private int userID;
    private String nameOfUser,dateOfBirth,country,about,sex,username,password;
    private int themeID;
    
    //Screen size Rectangle2D - start
    public void setScreenRect(Rectangle2D rect)
    {
        screenRect = new Rectangle2D(0,0,rect.getWidth(),rect.getHeight());
    }
    public Rectangle2D getScreenRect()
    {
        return screenRect;
    }
    //Screen size Rectangle2D - end

    //Database Connection - start
    public void setConnection(Connection con)
    {
        cn = con;
    }
    public Connection getConnection()
    {
        return cn;
    }
    //Database Connection - end
    
    //Cross-class user information - start
    public void setUserInformation(int uID,String n,String d,String c,String a,String s,String u,String p,int tID)
    {
        userID = uID;
        nameOfUser = n;
        dateOfBirth = d;
        country = c;
        about = a;
        sex = s;
        username=u;
        password=p;
        themeID = tID;
    }
    public int getUserID()
    {
        return userID;
    }
    public int getThemeID()
    {
        return themeID;
    }
    public String getNameOfUser()
    {
        return nameOfUser;
    }
    public String getDateOfBirth()
    {
        return dateOfBirth;
    }
    public String getCountry()
    {
        return country;
    }
    public String getAbout()
    {
        return about;
    }
    public String getSex()
    {
        return sex;
    }
    public String getUsername()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }
    //Cross-class user information - end
}
