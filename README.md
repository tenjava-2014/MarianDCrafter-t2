MarianDCrafter's ten.java submission
==============================

[![ten.java](https://cdn.mediacru.sh/hu4CJqRD7AiB.svg)](https://tenjava.com/)

This is a submission for the 2014 ten.java contest.

- __Theme:__ How can energy be harnessed and used in the Minecraft world?
- __Time:__ Time 2 (7/12/2014 09:00 to 7/12/2014 19:00 UTC)
- __MC Version:__ 1.7.9 (latest Bukkit beta)
- __Stream URL:__ https://twitch.tv/MarianDCrafter

<!-- put chosen theme above -->

---------------------------------------

Compilation
===========

- Download & Install [Maven 3](http://maven.apache.org/download.html)
- Clone the repository: `git clone https://github.com/tenjava/MarianDCrafter-t2`
- Compile and create the plugin package using Maven: `mvn`

Maven will download all required dependencies and build a ready-for-use plugin package!

---------------------------------------

Usage
=====

1. Install plugin
2. Do things with it

---------------------------------------

Features
=========

DELAY can be: SECOND | MINUTE | HOUR

Calculator
----------
- Type '/calculator' to open the calculator view
- Click the numbers and operators to calculate the result
- Material is needed for the Calculator

Configuring of Calculator
-------------------------
- enabled: true|false to enable/disable the Calculator
- driveMaterial: the Material you have to pay when you have the calculator open
- delay: The delay how often you have to pay if you have the calculator open
- materialPerDelay: the The Number of 'materials' you have to pay when you have the calculator open

Bag
---
- Type '/bag' to create a new bag or open the bag if you have already created one.
- Material is needed for the Bag
- The Bag will be saved in bags.yml

Configuring of Bag
------------------
- enabled: true|false to enable/disable the Bag
- driveMaterial: the Material which you have to pay
- delayInBag: The delay how often you have to pay if you have the bag open
- materialPerDelayInBag: The number of 'materials' you have to pay, when you have the bag open 
- delayOutsideBag: The delay how often you have to pay if you have the bag closed
- materialPerDelayOutsideBag: The number of 'materials' you have to pay, when you have the bag closed


ItemChanger
-----------
- Type '/itemchanger' to start a conversation.
- Type 'list' to see a list of the ItemChangers
- Type 'stop' to stop the conversation
- Type '#' to start an ItemChanger with the given ID

Configuring of ItemChanger
--------------------------
- enabled: true|false to enable/disable the ItemChanger
- starting with 0, give every ItemChanger one entry which looks like this:


- from: The Material which you have to put in, formatted like this: MATERIAL AMOUNT
- to: The Material which you get after the change, formatted like this: MATERIAL AMOUNT
- delay: the delay in which to start the timer
- delayTime: the number of delays which the timer have to run before you get the 'to' item
- materialPerDelay: The number of 'materials' which you have to pay every delay (delayTime times until you get the item)
- driveMaterial: the Material, which you have to pay for changing the item. Paid every delayTime materialPerDelay times