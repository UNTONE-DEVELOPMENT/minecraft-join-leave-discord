# Discord Join/Leave message webhook for Minecraft

This is a simple and minimal plugin for sending player join, leave, and death events to a webhook URL. Currently the features are

### Join / Leave messages
Get a message in your chat with the following content:
- Player Name
- Player Head
- Currently online player count
- Join/Leave time (optional)
- Client brand (optional)
- UUID (optional)

### Death messages

Specify a separate URL optionally to send death messages with the following content:

- Death reason
- Whomst has dieded
- etc.

you get the idea :^)

## COMMANDS

`/djlm reload`

Reloads the plugin


## BUILDING
Should be as simple as running `mvn package`

## CONTRIBUTING
Always happy to merge cool PRs, if you have any good feature ideas feel free to make one!