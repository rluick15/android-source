package com.bloc.singletons;

import java.util.ArrayList;
import com.bloc.singletons.*;
import com.bloc.singletons.listeners.*;
import com.bloc.singletons.talkers.*;

/*
 * This is a singleton class which maintains communication
 * between classes and Listeners
 */
public class Speakerphone extends Object {

	private static Speakerphone sSpeakerPhone;
	private static ArrayList<Listener> mListener;

	/*
	 * get
	 * @return the singleton instance of Speakerphone
	 */
	public static Speakerphone get() {
		if(sSpeakerPhone == null) {
			sSpeakerPhone = new Speakerphone();
			mListener = new ArrayList<Listener>();
		}

		return sSpeakerPhone;
	}

	/*
	 * addListener
	 * Add a listener to Speakerphone's list
	 * @param listener an instance of the Listener interface
	 */
	public void addListener(Listener listener) {
		mListener.add(listener);
	}


	/*
	 * removeListener
	 * Remove a Listener from the Speakerphone's list
	 * @param listener the Listener to remove
	 */
	public void removeListener(Listener listener) {
		mListener.remove(listener);
	}

	/*
	 * shoutMessage
	 * Sends the message to all of the Listeners tracked by Speakerphone
	 * @param talker a Talker whose message will be sent
	 */
	public void shoutMessage(Talker talker) {
		String message = talker.getMessage();
        for(Listener listener : mListener) {
        	listener.onMessageReceived(message);
        }
	}

	/*
	 * shoutMessage
	 * Sends the message to all of the Listeners which are instances of
	 * the class parameter
	 * @param talker a Talker whose message will be sent
	 * @param cls a Class object representing the type which the Listener
	 *			  should extend from in order to receive the message
	 *
	 * HINT: see Class.isAssignableFrom()
	 *		 http://docs.oracle.com/javase/7/docs/api/java/lang/Class.html#isAssignableFrom(java.lang.Class)
	 */
	public void shoutMessage(Talker talker, Class cls) {
		String message = talker.getMessage();
        for(Listener listener : mListener) {
        	if(cls.isAssignableFrom(listener.getClass())) {
            	listener.onMessageReceived(message);
   			}
        }
	}

	/*
	 * removeAll
	 * Removes all Listeners from Speakerphone
	 */
	public void removeAll() {
		mListener.clear();
	}

}