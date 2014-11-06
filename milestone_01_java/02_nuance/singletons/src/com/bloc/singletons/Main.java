package com.bloc.singletons;

	import com.bloc.singletons.*;
	import com.bloc.singletons.listeners.*;
	import com.bloc.singletons.talkers.*;

public class Main extends Object {

	public static void main(String [] args) {
		// Instantiate some talkers and some listeners
		Talker parent = new Parent();
		Talker petOwner = new PetOwner();
		Talker singer = new Singer();

		Listener audienceMember = new AudienceMember();
		Listener child = new Child();
		Listener pet = new Pet();

		// Register listeners
		Speakerphone speakerphone = Speakerphone.get();

		speakerphone.addListener(audienceMember);
		speakerphone.addListener(child);
		speakerphone.addListener(pet);

		// Send messages!
		speakerphone.shoutMessage(parent); //sends message to everyone

		//sends messages to appropriate listeners
		speakerphone.shoutMessage(parent, Child.class);
		speakerphone.shoutMessage(petOwner, Pet.class);
		speakerphone.shoutMessage(singer, AudienceMember.class);

		speakerphone.removeAll(); //removes all listeners

		speakerphone.shoutMessage(petOwner); //no one recieves this message

	}
}
