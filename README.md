# Android-Youtube-NatGeo

## What is does?
An Android app that uses Youtube API to play the lastest 25 videos upload 
by a channel. In this case, the National Geographic Youtube channel.

## How it looks?
Here is an screenshot:
![alt text](https://github.com/francoj11/Android-Youtube-NatGeo/blob/master/Screenshots/Screenshot_2015-02-15-04-15-14.png "Screenshot 1")


You can see a video of the app running in here:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=cPZbfxhy55A
" target="_blank"><img src="http://img.youtube.com/vi/cPZbfxhy55A/0.jpg" 
alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /></a>

## How it works? 
First of all, it downloads an XML file containing the lastest 25 videos of the National Geographic channel.
Then it gets parsed using SAXParser, an then the videos are shown in a list (a RecyclerView that uses 
CardView). You can now select any video an it will start playing in the YoutubePlayerView at the top.

## How to compile it?
Just download the project or clone it, and put your own Youtube Key in the MainActivity.java variable 
called APP_YOUTUBE, and you should be able to compile without problem.

To get your Youtube API key you need to go to https://console.developers.google.com and create a new project,
then enable the Youtube API, and then get the key in the credentials section.

------------------
##Credits:
This projects uses the [Picasso](http://square.github.io/picasso/) library, a powerful image downloading and caching library for Android.

-----------------
## Developed by:
Franco Jaramillo - francoj11@gmail.com

## License:
    Copyright 2015 francoj11

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
