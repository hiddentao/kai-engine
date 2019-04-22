/**
 * Copyright (C) 2010 Ramesh Nair (www.hiddentao.com)
 * 
 * This is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This is distributed in the hope that it will  be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this.  If not, see <http://www.gnu.org/licenses/>.
 */


--------------------------------------------------------------------------
--------------------------------------------------------------------------
Compiling on Windows (tested on WinXP Pro Service Pack 3)
--------------------------------------------------------------------------
--------------------------------------------------------------------------


This guide assumes that you've unzipped the 3D engine code into 
C:\dev\kai-engine such that the src folder is at C:\dev\kai-engine\src.


-----------------------------
Step 1: Setup environment
-----------------------------

Install the following tools somewhere:

  * JDK 1.5+ (http://java.sun.com/javase/downloads/index.jsp) - Java development kit.
  * Ant (http://ant.apache.org/) - build tool
  * Proguard(http://proguard.sourceforge.net/) - shrinker and obfuscator

Ensure the Ant binary is accessible via your ``PATH``. Ensure the 
following environment variables are set:

  * JAVA_HOME - path to JDK root folder, e.g. ``C:\Program Files\Java\jdk1.6.0_10``.
  * PROGUARD_HOME - path to Proguard root folder, e.g. ``C:\Java\proguard4.4``.


-----------------------------
Step 2: Get Common-Utils 
-----------------------------

Unless already present you will need to grab the Common-Utils library from 
http://www.hiddentao.com/code/common-utils and unzip it such that it's in 
the parent folder of the engine folder. For example:

C:\dev\kai-engine
C:\dev\common-utils



-----------------------------
Step 3: Build project
-----------------------------

Run Ant:

C:\dev\kai-engine> ant

You should see something like the following:

		init:
		     [echo] Kai Engine: D:\dev\java\trunk\kai-engine\build.xml
		   [delete] Deleting directory D:\dev\java\trunk\kai-engine\bin
		    [mkdir] Created dir: D:\dev\java\trunk\kai-engine\bin
		   [delete] Deleting directory D:\dev\java\trunk\kai-engine\deploy
		    [mkdir] Created dir: D:\dev\java\trunk\kai-engine\deploy
		   [delete] Deleting directory D:\dev\java\trunk\kai-engine\temp
		    [mkdir] Created dir: D:\dev\java\trunk\kai-engine\temp
		
		build-classes:
		     [echo] Build classes.
		     [echo] Build common-utils
		    [javac] Compiling 7 source files to D:\dev\java\trunk\kai-engine\bin
		     [echo] Build my classes
		    [javac] Compiling 49 source files to D:\dev\java\trunk\kai-engine\bin
		
		build-jar:
		     [echo] Build JAR
		      [jar] Building jar: D:\dev\java\trunk\kai-engine\temp\kai.unobfuscated.jar
		
		obfuscate-jar:
		     [echo] Obfuscate JAR
		 [proguard] ProGuard, version 4.4
		 [proguard] Reading program jar [D:\dev\java\trunk\kai-engine\temp\kai.unobfuscated.jar]
		 [proguard] Reading library jar [C:\Program Files\Java\jdk1.6.0_10\jre\lib\rt.jar]
		 [proguard] com.hiddentao.kai.demos.DemoApplet
		 [proguard] javax.swing.applet.JAppletSimulator
		 [proguard] javax.swing.applet.JAppletSimulator: void main(java.lang.String[])
		 [proguard] Preparing output jar [D:\dev\java\trunk\kai-engine\deploy\kai.jar]
		 [proguard]   Copying resources from program jar [D:\dev\java\trunk\kai-engine\temp\kai.unobfuscated.jar]
		
		copy-html:
		     [echo] Copy over HTML files
		     [copy] Copying 1 file to D:\dev\java\trunk\kai-engine\deploy
		
		clean-build-project:
		
		BUILD SUCCESSFUL
		Total time: 8 seconds


-----------------------------
Step 4: Run the demo applet
-----------------------------

The obfuscated and shrunk JAR file is now in C:\dev\kai-engine\deploy. You'll 
also find an applet.html in this folder. Double-click this to view the demo 
applet in your browser.



-----------------------------
Additional information 
-----------------------------

You can also run the demo applet as a standalone application by typing:

C:\dev\kai-engine> ant demo

This will use an applet simulation environment and the unobfuscated JAR 
file to run the applet in a standalone window.
