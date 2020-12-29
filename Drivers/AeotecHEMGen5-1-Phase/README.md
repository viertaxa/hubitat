# Aeotec HEM Gen5 (1 Phase) Driver

This driver is for the Aeotec Gen 5 Home Energy Monitor (HEM), 1 Phase Version.

**IMPORTANT RESTRICTIONS:**

- *THIS DRIVER IS CURRENTLY FOR THE H.E. C-7 HUB ONLY*
- *THIS DRIVER IS FOR THE ZW095-A ONLY*

## Status

This driver is in `ALPHA` status and is considered a *MVP* (Minimum Viable Product). While all basic functionality should work, nothing extra has been added. Please file issues with feature requests if there is a functionality you would like to see added.

Additionally, I do not own this hardware. This driver is based off of the Engineering Sheet provided by Aeotec, and the 2-Phase version, which I do own. Please consider giving a sponsorship as outlined in the main [README.md](https://github.com/viertaxa/hubitat/blob/main/README.md#sponsoring) file if you would like me to purchase one for testing.

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
