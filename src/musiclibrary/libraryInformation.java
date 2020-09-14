/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

/**
 *
 * @author ISHRAK-Ultra
 */
public class libraryInformation 
{
    private int id;
    private String name;
    
    public libraryInformation(int i,String n)
    {
        id=i;
        name=n;
    }
    
    public int getID()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
}
