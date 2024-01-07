# (ANDROID APP) Philips Hue Lights Control

## Overview

The Philips Hue Lights Control app is an Android application designed to remotely control Philips Hue lights. Users can turn lights on/off, adjust brightness, and schedule turn-on/turn-off times.

## Features

- **Turn On/Off:** Toggle Philips Hue lights on or off.
- **Brightness Control:** Adjust the brightness of the lights using a slider.
- **Scheduling:** Schedule times for the lights to automatically turn on or off.
- **Schedule Management:** View and remove scheduled actions.

## Installation

Clone this repository and import it into Android Studio:
-- **git clone [repository URL]

## Configuration

Update the `BRIDGE_IP` and `USERNAME` in `MainActivity.java` to match your Philips Hue Bridge IP address and the generated username from your Philips Hue setup.

## Usage

- **Turn On/Off:** Press the "TURN ON" or "TURN OFF" button to control the lights.
- **Adjust Brightness:** Use the slider to set the desired brightness level.
- **Scheduling:** Use the time picker to select a time and then press either "Schedule Turn On" or "Schedule Turn Off" to set a schedule.
- **Viewing Schedules:** View the list of scheduled actions in the `ListView` at the bottom.
- **Removing Schedules:** Tap on a scheduled action in the list to remove it.

## Dependencies

- **OkHttp:** For handling HTTP requests to the Philips Hue Bridge.

## Building the Project

Open the project in Android Studio and run it on an emulator or a physical device.
