/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiclibrary;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * This is a generic timer class for performing object specific tasks when the timer is stopped i.e. the set delay has elapsed. Every MyTimer object is characterized by the object associated with it.
 * One MyTimer object can be used with only one object (with respect to which the timer will be run).
 * The delay time can be set in two ways - One. Constructor parameter Two. startTimer() parameter
 * The default timeDelay is 0.
 * Execution type - asynchronous i.e. Multiple MyTimer objects can run simultaneously
 * 
 * @author Ishrak Hayet
 * @version 1.0
 */
public final class MyTimer <T> extends AnimationTimer
{    
    /**
     * The delay during which the timer will be run
     */
    private long timeDelay = 0;
    /**
     * The current system time in nanoseconds - this is required to compare with the "now" time in "handle" method to determine if the delay has elapsed
     */
    private long startTime = 0;
    
    /**
     * The object with respect to which the task will be performed when the timer is stopped - Generic variable
     */
    private T receivedObject = null;
    
    /**
     * sets the object associated with the timer
     * 
     * @param obj the object with respect to which the timer will be run
     */
    public MyTimer(T obj)
    {
        timeDelay = 0;
        receivedObject = obj;
        
        System.out.println(receivedObject.toString());
    }
    
    /**
     * sets the time delay during which the timer will be run and the object associated with the timer - overload
     * 
     * @param tDelay the time delay during which the timer will be run - in milliseconds
     * @param obj the object with which the MyTimer object will be associated
     */
    public MyTimer(long tDelay,T obj)
    {
        timeDelay = tDelay*1000000;
        receivedObject = obj;
    }
    
    /**
     * Resets startTimeSet boolean and calls the start() method of the AnimationTimer base class
     * 
     * @param tDelay the time delay during which the timer will be run
     */
    public void startTimer(long tDelay)
    {
        timeDelay = tDelay*1000000;
        startTime = System.nanoTime();                                          //setting start time once when start() is called from other classes
                
        super.start();
    }
    
    /**
     * Overrides the method in the abstract base class AnimationTimer. This method gets called continuously when AnimationTimer.start() is called.
     * 
     * @param now The current system time in nanoseconds - gets updated every time this method is called by AnimationTimer.start()
     */
    @Override
    public void handle(long now)
    {
        if(now-startTime>=timeDelay)
        {
            stop();
            
            String objectName = identifyObject(receivedObject);                     //identifying the object to perform the object specific tasks
            System.out.println("Name of the received object: " + objectName);

            performTask(objectName);                                                //performing the object specific tasks

            System.out.println("Timer has been stopped");
        }
    }
    
    /**
     * performs a substring search for "id=" in order to identify the name of the object. The name of the object is required to perform object specific tasks when the timer is stopped.
     * 
     * @param obj The object received from the LoginPageFXMLController
     * @return String - The name of the object
     */
    private String identifyObject(T obj)
    {
        int i,k;
        String objectName = null;
        
        if(obj!=null)
        {
            String temp = obj.toString();
        
            System.out.println("Received Object: "+temp);

            char[] tempCharArray = new char[30];

            boolean substringFound = false;

            //substring search for the "id=" - start
            for(i=0,k=0;i<temp.length();i++)
            {
                if((temp.charAt(i)=='i' && temp.charAt(i+1)=='d' && temp.charAt(i+2)=='=') && i<(temp.length()-3) && (substringFound==false))
                {
                    substringFound = true;
                    i+=2;
                }
                else if(substringFound==true)
                {
                    tempCharArray[k] = temp.charAt(i);

                    if(tempCharArray[k]==',')
                    {
                        tempCharArray[k]='\0';
                        break;
                    }
                    k++;
                }
            }
            //substring search for the "id=" - end

            String conversion = new String(tempCharArray);
            objectName = conversion.trim();
        }
        
        return objectName;
    }
    
    /**
     * performs object specific tasks when the timer is stopped in handle
     * 
     * @param objectName The name of the received object
     */
    private void performTask(String objectName)
    {
        if(objectName!=null)
        {
            if(objectName.equals("notification"))
            {
                Label notification = (Label)receivedObject;                             //downcasting

                notification.setText("");
            }
            else if(objectName.equals("username"))
            {
                TextField username = (TextField)receivedObject;

                username.setText("");
            }
            else if(objectName.equals("password"))
            {
                PasswordField password = (PasswordField)receivedObject;

                password.setText("");
            }
            else if(objectName.equals("nameTextLabel"))
            {
                Label nameText = (Label)receivedObject;
                
                nameText.setText("");
            }
            else if(objectName.equals("deletePlaylistLabel"))
            {
                Label deletePlaylistLabel = (Label)receivedObject;
                
                deletePlaylistLabel.setText("");
            }
            else if(objectName.equals("settingsNotification"))
            {
                Label settingsNotificationLabel = (Label)receivedObject;
                
                settingsNotificationLabel.setText("");
            }
        }
    }
}
