Hi! Welcome to "Services, Processes and Binder"!

  This workshop will be a very deep and technical dive into the
guts of the Android framework.  If you are new to Android it may be
a stretch.

  This will not be a lecture!  It is very much my intention that
you leave this workshop with actual experience developing the
constructs that it introduces. You will get much more out of it if
you actually code.  I will be live-coding and I encourage you to
follow along.  In addition there will be several coding exercises.
You are more than welcome to work as teams, if you have the
opportunity.

  The framework code for the workshop at:

	https://github.com/bmeike/ServiceExperiment.git

  The repo contains four applications and a library. Please be
sure that you can compile and run them all (they may crash),
*before* you head to the conference.  Wifi is always spotty and
Gradle is a pig.  You will not be happy if you are sitting in the
back of the room, trying to download the Guava library.

  I recommend that you have Java 1.8.0_131 installed.  Any
Java 8 should be sufficient. The project was built with the
current Canary AndroidStudio, 3.0. and uses Gradle
4.1-milestone-1.  It uses grade plugin 3.0.0-alpha5
BuildTools 26.0.0 and Android SDK 26.  I've run it on a
Pixel emulator, running O.

  Other configurations may work but please test to confirm. 
In particular, some of the code does really horrible things
to the UI thread and may not work at all on a real device.

  I hope to give away several copies of my new book, "Android
Concurrency" for feats of coding skill, during the workshop.  Come
curious and ready to code.

Blake Meike
