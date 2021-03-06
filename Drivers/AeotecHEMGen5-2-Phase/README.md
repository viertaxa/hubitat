# Aeotec HEM Gen5 (2 Phase) Driver

This driver is for the Aeotec Gen 5 Home Energy Monitor (HEM), 2 Phase Version.

**IMPORTANT RESTRICTIONS:**

- *THIS DRIVER IS CURRENTLY FOR THE H.E. C-7 HUB ONLY*
- *THIS DRIVER WAS DEVELOPED AND TESTED ONLY AGAINST FIRMWARE 1.37 ON THE HEM*
- *THIS DRIVER IS FOR THE ZW095-A ONLY*

## Status

This driver is in `BETA` status and is considered a *MVP* (Minimum Viable Product). While all basic functionality should work, nothing extra has been added. Please file issues with feature requests if there is a functionality you would like to see added.

## Known Issues

- Hubitat's Driver interface does not handle multi-select options well. This means a few things:
    - You will not see the option to multi-select until you save the configuration at least once.
    - Selections do not persist after refresh or saving.
    - Unfortunately, there is no workaround for this. If you find it frustrating, please bring it up with Hubitat support.

## Tested environments

**Hubs:**
- `C-7`

**HE Software:**
- `2.2.4.158`

## Release Notes

- 0.1.0: Initial Release
- 0.1.1: Fix typo introduced in cleanup of code
- 0.1.2: Add back in ability to enable debug messaging
- 0.1.3: Cleanup of cruft and a bit of debug logging added
- 0.1.4: Tiny bit of cleanup