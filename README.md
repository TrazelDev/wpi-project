# Traffic Detector

## Overview:
This is a traffic detector project aimed at identifying areas with the most traffic in Karmiel and storing relevant data for later analysis. The goal is to address these traffic problems. The project was developed in collaboration with our school and a local university to create an application for collecting dataa, and WPI University, tasked with analyzing this data.

## Features:
- Options to select your mode of transportation: `private vehicle`, `bike`, `walking`, `public bus`.
- Detection of your `current location`, `time of trip`, `trip route`, etc.
- Stores the information of every trip along with user information in the MongoDB database for later analysis.

## Implementation:
- We implement routes by checking the current location and time for each type of transportation, allowing us to create specific user routes. Additionally, users can signal when they encounter a traffic jam or make a stop. Automatic detection is also available in case the user forgets to signal.

> [!Note]
> The final version of this project works only on the Flamingo version of Android Studio. We couldn't make it work with other versions due to Android Studio compatibility issues.
